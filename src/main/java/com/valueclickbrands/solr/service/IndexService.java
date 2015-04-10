package com.valueclickbrands.solr.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import sun.print.resources.serviceui;

import com.google.gson.Gson;
import com.sun.net.httpserver.Authenticator.Success;
import com.valueclickbrands.solr.model.Node;
import com.valueclickbrands.solr.model.ResponseHeader;
import com.valueclickbrands.solr.model.Taxonomy;
import com.valueclickbrands.solr.util.Configure;
import com.valueclickbrands.solr.util.DataConvertor;
import com.valueclickbrands.solr.util.Http;
import com.valueclickbrands.solr.util.MailUtil;
import com.valueclickbrands.solr.util.SendMailUtil;

/** 
 * @author Vanson Zou
 * @date Dec 18, 2014 
 */

public class IndexService {
	protected static Logger logger = Logger.getLogger(IndexService.class);

	 private static CloudSolrServer cloudSolrServer; 
	 private static int commitCount;
	 private DumpService dumpService;
	 private ZKService zkService;
	 private TaxonomyService taxonomyService;
	 private Gson gson = new Gson();
	 private Pattern p_html = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);

	
	 public TaxonomyService getTaxonomyService() {
		return taxonomyService;
	}

	public void setTaxonomyService(TaxonomyService taxonomyService) {
		this.taxonomyService = taxonomyService;
	}

	public static int getCommitCount() {
		return commitCount;
	}

	public static void setCommitCount(int commitCount) {
		IndexService.commitCount = commitCount;
	}

	public void setDumpService(DumpService dumpService) {
		this.dumpService = dumpService;
	}

	public void setZkService(ZKService zkService) {
		this.zkService = zkService;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}


	public static synchronized CloudSolrServer getCloudSolrServer(final String zkHost,String defaultCollection) {  
		try {
			if(cloudSolrServer!=null){
				cloudSolrServer.shutdown();
				cloudSolrServer=null;
			}
				 
			cloudSolrServer = new CloudSolrServer(zkHost);
		    cloudSolrServer.setDefaultCollection(defaultCollection);
		    cloudSolrServer.connect();
			
        }catch(Exception e) {  
            e.printStackTrace();
            logger.error(e.getMessage());
        }  
	        return cloudSolrServer;  
	}  
	 
	      
	    public void search(SolrServer solrServer, String String) {        
	        SolrQuery query = new SolrQuery();  
	        query.setQuery(String);  
	        try {  
	            QueryResponse response = solrServer.query(query);  
	            SolrDocumentList docs = response.getResults();  
	  
	            System.out.println("文档个数：" + docs.getNumFound());  
	            System.out.println("查询时间：" + response.getQTime());  
	  
	            for (SolrDocument doc : docs) {  
	                String area = (String) doc.getFieldValue("area");  
	                Long id = (Long) doc.getFieldValue("id");  
	                System.out.println("id: " + id);  
	                System.out.println("area: " + area);  
	                System.out.println();  
	            }  
	        } catch (SolrServerException e) {  
	            e.printStackTrace();
	            logger.error(e.getMessage());
	        } catch(Exception e) {
	            e.printStackTrace();
	            logger.error(e.getMessage());
	        }  
	    }  
	      
	    public boolean deleteAllIndex(SolrServer solrServer) {  
	    	boolean success = false;
	        try {
	            solrServer.deleteByQuery("*:*");// delete everything!  
	            UpdateResponse ups=  solrServer.commit();  
	            if(ups.getStatus() ==0){
	            	success = true;
	            }
	        }catch(SolrServerException e){  
	            e.printStackTrace();
	            logger.error(e.getMessage());
	        }catch(IOException e) {  
	            e.printStackTrace();
	            logger.error(e.getMessage());
	        }catch(Exception e) {  
	            e.printStackTrace();
	            logger.error(e.getMessage());
	        }  
	        return success;
	    }
	    
	    public boolean switchIndex(String collectionName,String alias,String hostname){
	    	boolean re = false;
	    	String url = Configure.SolrCollectionUrlCreatealiasUrl;
	    	url = url.replace("[hostname]", hostname)+"&name="+alias+"&collections="+collectionName;	
	    	String response = Http.sendRequest(url);
	    	ResponseHeader responseHeader = gson.fromJson(response, ResponseHeader.class);
	    	if(responseHeader!=null && responseHeader.getStatus() == 0){
	    		re = true;
		    	logger.info("Success, Solr Collection Url Createalias url :"+url);
	    	}else {
		    	logger.info("Failed, Solr Collection Url Createalias url :"+url);
			}
	    	
	    	return re;
	    }
	    
	    public boolean createIndexIncrementalForNode(int nid,int vid,String treeAction){
	    	boolean re = false;
	    	Map<String, Map<String, Node>> node_map = dumpService.dumpIncremental(nid, vid,treeAction,Configure.index_type_all);
//	    	String c1 = getCollection(false);
	    	String c1 = Configure.SolrCollectionNodeAlias;
	    	String c1_feed = Configure.SolrCollectionNodeAlias_feed;
			if(node_map.size()>0 && StringUtils.isNotEmpty(c1) && StringUtils.isNotEmpty(c1_feed)){
				CloudSolrServer cloudSolrServer = getCloudSolrServer(Configure.ZookeeperHosts, c1);
				int count = createIndexForNode(cloudSolrServer,node_map.get(Configure.index_type_api),false);//
				
				CloudSolrServer cloudSolrServer_feed = getCloudSolrServer(Configure.ZookeeperHosts_feed, c1_feed);
				int count_feed = createIndexForFeed(cloudSolrServer_feed,node_map.get(Configure.index_type_feed),false);//
				
				if(count >0 && count_feed == count){
					logger.info("Solr api & Solr feed index Incremental success ,nid:"+nid+",vid:"+vid);
					re = true;
				}
			 }else {
				logger.info(" create index failed node_map size =0 ,nid="+nid+",vid="+vid);
			}
	    	return re;
	    }
	    
	    public boolean createIndexIncrementalForTaxonomy(long pId,String url){
	    	boolean re = false;
	    	if(pId > 0 && !StringUtils.isEmpty(url)){
	    		url = url.toLowerCase();
	    		if(url.startsWith("/contributors/") || url.startsWith("/user/") || url.startsWith("/admin/")){
	    			logger.info("Url start with 'contributors' or 'user' or 'admin',skip! ");
	    			return true;
	    		}
	    		Taxonomy taxonomy = taxonomyService.addTaxonomy(pId,url);
		    	//String c1 = getCollection(true,false);
		    	String c1 = Configure.SolrCollectionTaxonomyAlias;
				if(taxonomy!=null && !StringUtils.isEmpty(c1) ){
			    	List<Taxonomy> list = new ArrayList<Taxonomy>();
					list.add(taxonomy);
					CloudSolrServer cloudSolrServer = getCloudSolrServer(Configure.ZookeeperHosts, c1);    
					int count =  createIndexForTaxonomy(cloudSolrServer, list, false);
					if(count >0){
						logger.info("taxonomy index Incremental success ,pid:"+taxonomy.getPid());
						re = true;
					}
				 }else {
					logger.info(" taxonomy create index failed  size =0   null value");
				}
	    	}else {
				logger.warn("Taxonomy template_pId is 0");
			}
	    	return re;
	    }
	    
	    public boolean deleteIndex(int nid){
	    	boolean re = false;
//	    	String c1 = getCollection(false);
	    	String c1 = Configure.SolrCollectionNodeAlias;
	    	String c1_feed = Configure.SolrCollectionNodeAlias_feed;
	    	String c1_tax = Configure.SolrCollectionTaxonomyAlias;
			if(StringUtils.isNotEmpty(c1) && StringUtils.isNotEmpty(c1_feed)){
				CloudSolrServer cloudSolrServer = getCloudSolrServer(Configure.ZookeeperHosts, c1);    
				boolean stat =  deleteIndex(cloudSolrServer,String.valueOf(nid));

				CloudSolrServer cloudSolrServer_tax = getCloudSolrServer(Configure.ZookeeperHosts, c1_tax);    
				boolean stat_tax =  deleteIndex(cloudSolrServer_tax,"node_"+nid);
				
				CloudSolrServer cloudSolrServer_feed = getCloudSolrServer(Configure.ZookeeperHosts_feed, c1_feed);    
				boolean stat_feed =  deleteIndex(cloudSolrServer_feed,String.valueOf(nid));
				

				if(stat && stat_feed || stat_tax){
					logger.info("Solr_api & Solr_Feed delete Index success ,nid:"+nid+",stat_tax:"+stat_tax);
					re = true;
				}else{
					logger.error("Solr_api & Solr_Feed delete Index failed ,nid:"+nid);
				}	
			 } else {
				 logger.error("Collection name can't be null. nid:" + nid);
			 }
	    	return re;
	    }
	    
	    public boolean deleteIndex(SolrServer solrServer,String nid){
	    	boolean re =false;
	    	try {
				solrServer.deleteById(nid);
				UpdateResponse updateResponse = solrServer.commit();
				int stat = updateResponse.getStatus();
				if(stat==0){
					re =true;
				}
			} catch (SolrServerException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
	    	return re;
	    }
	    
	    public String getCollection(boolean needSwitch,Boolean isNode){
	    	String c1 = null;
	    	String json = zkService.getPathValue(Configure.ZookeeperCollectionAliasesPath);
	    	JSONObject jsonObject = JSONObject.fromObject(json);
	    	JSONObject o= (JSONObject) jsonObject.get("collection");
	    	String alias = Configure.SolrCollectionTaxonomyAlias;
	    	if(isNode){
	    		alias =  Configure.SolrCollectionNodeAlias;
	    	}
	    	if(o.containsKey(alias)){
	    		String collectionName = (String) o.get(alias);
	    		String[] array = collectionName.split(",");
	    		c1 = array[0];
	    		if(needSwitch){
	    			if(c1.endsWith("1")){
		    			c1 = c1.replace("1", "2");
		    		}else if(c1.endsWith("2")){
		    			c1 = c1.replace("2", "1");
					}
	    		}
	    	}
	    	return c1;
	    }
	    
	    public String getCollection_feed(boolean needSwitch){
	    	String c1 = "";
	    	CloudSolrServer cloudSolrServer = getCloudSolrServer(Configure.ZookeeperHosts_feed, Configure.SolrCollectionNodeAlias_feed); 
	    	c1 = cloudSolrServer.getZkStateReader().getAliases().getCollectionAlias(Configure.SolrCollectionNodeAlias_feed);
    		if(needSwitch){
    			if(c1.endsWith("1")){
	    			c1 = c1.replace("1", "2");
	    		}else if(c1.endsWith("2")){
	    			c1 = c1.replace("2", "1");
				}
    		}
	    	return c1;
	    }
	    
	    public String getCollectionForTaxonomy(boolean needSwitch){
	    	String c1 = null;
	    	String json = zkService.getPathValue(Configure.ZookeeperCollectionAliasesPath);
	    	JSONObject jsonObject = JSONObject.fromObject(json);
	    	JSONObject o= (JSONObject) jsonObject.get("collection");
	    	if(o.containsKey(Configure.SolrCollectionNodeAlias)){
	    		String collectionName = (String) o.get(Configure.SolrCollectionNodeAlias);
	    		String[] array = collectionName.split(",");
	    		c1 = array[0];
	    		if(needSwitch){
	    			if(c1.endsWith("1")){
		    			c1 = c1.replace("1", "2");
		    		}else if(c1.endsWith("2")){
		    			c1 = c1.replace("2", "1");
					}
	    		}
	    	}
	    	return c1;
	    }
	    
	    public String createIndexFullyForNode(boolean needSwitch){
	    	String re = null;
	    	Map<String, Map<String, Node>> node_map = dumpService.dump(Configure.index_type_all);
	    	String c1 = getCollection(true,true);
	    	String c1_feed = getCollection_feed(true);
	    	//String c1 = "";
	    
			 if(node_map.size()>0 && !StringUtils.isEmpty(c1) && !StringUtils.isEmpty(c1_feed) ){
				CloudSolrServer cloudSolrServer = getCloudSolrServer(Configure.ZookeeperHosts, c1);;    
				boolean success = deleteAllIndex(cloudSolrServer);  
				
				if(success){
	            	logger.info("Solr api delete All Index success ,collection:"+c1);
					long count =  createIndexForNode(cloudSolrServer,node_map.get(Configure.index_type_api),true);
					long foundCnt = getSolrDataCount(getCloudSolrServer(Configure.ZookeeperHosts, Configure.SolrCollectionNodeAlias));
					
					CloudSolrServer cloudSolrServer_feed = getCloudSolrServer(Configure.ZookeeperHosts_feed, c1_feed);
					boolean success_feed = deleteAllIndex(cloudSolrServer_feed);
					long count_feed = 0;
					long foundCnt_feed = 0;
					if(success_feed){
		            	logger.info("Solr feed delete All Index success ,collection:"+c1_feed);
						count_feed =  createIndexForFeed(cloudSolrServer_feed,node_map.get(Configure.index_type_feed),true);
						foundCnt_feed = getSolrDataCount(getCloudSolrServer(Configure.ZookeeperHosts_feed, Configure.SolrCollectionNodeAlias_feed));
					}else {
						logger.error("Solr_feed deleteAllIndex error.");
					}
					if(count > 0 && count_feed>0 ){
						if(count > foundCnt*0.7 && count_feed > foundCnt_feed*0.7){
							re = c1;							
							if(needSwitch){
								logger.info("index success ,size:"+count);
								boolean suc = switchIndex(c1,Configure.SolrCollectionNodeAlias, zkService.getLiveNode());
								boolean suc_feed = switchIndex(c1_feed,Configure.SolrCollectionNodeAlias_feed, zkService.getLiveNode_feed());
								if(suc && suc_feed){
									logger.info(c1+","+c1_feed+" switch success ");
								}
							}
						}else {
					    	try {
								MailUtil mail = MailUtil.newInstance(Configure.mail_smtp_host, Configure.mail_user, Configure.mail_password);
								mail.send(Configure.mail_from, Configure.mail_to, "SolrClient prod -- Taxonomy Data number Less Than 70% ", "SolrClient -- Taxonomy Data number Less Than 70% ");
							} catch (Exception e) {
								logger.error(e.getMessage());
							} 
						}
							
					} else {
						logger.error("Solr api or solr feed createIndexFully error. commit count=" + count + ",  count from Collection=" + foundCnt);
					}
				}else {
					logger.error("Solr_api deleteAllIndex error.");
				}
			 } else {
				 logger.error("createIndexFully error. dump data fail. size=" + node_map.size() + ", collection=" + c1);
			 }
			 
	    	return re;
	    }
	    
	    
	    public String createIndexFullyForSearch(boolean needSwitch){
	    	String re = null;
	    	Map<String, Map<String, Node>> node_map = dumpService.dump(Configure.index_type_search);
	    	//String c1 = getCollection(needSwitch,true);
	    	String c1 = Configure.SolrCollectionNodeSearchAlias;
	    	//String c1 = "inv_staging_search_collection1";
			 if(node_map.size()>0 && !StringUtils.isEmpty(c1) ){
				CloudSolrServer cloudSolrServer = getCloudSolrServer(Configure.ZookeeperHosts, c1);    
				boolean success = deleteAllIndex(cloudSolrServer);
				if(success){
	            	logger.info("delete All Search Index success ,collection:"+c1);
					long count =  createIndexForSearch(cloudSolrServer,node_map.get(Configure.index_type_search),true);
				//	long count =  createIndexForNode(cloudSolrServer,node_map,true);
					long foundCnt = getSolrDataCount(getCloudSolrServer(Configure.ZookeeperHosts, Configure.SolrCollectionNodeSearchAlias));
					if(count > 0 ){
					//	if(count > foundCnt*0.7){
							logger.info("index success ,size:"+count);
							re = c1;
							if(needSwitch){
								boolean suc = switchIndex(c1,Configure.SolrCollectionNodeAlias, zkService.getLiveNode());
								if(suc){
									logger.info(c1+"switch success ");
								}
							}
						/**}else {
					    	try {
								MailUtil mail = MailUtil.newInstance(Configure.mail_smtp_host, Configure.mail_user, Configure.mail_password);
								mail.send(Configure.mail_from, Configure.mail_to, "SolrClient prod -- Taxonomy Data number Less Than 70% ", "SolrClient -- Taxonomy Data number Less Than 70% ");
							} catch (Exception e) {
								logger.error(e.getMessage());
							} 
						}**/
							
					} else {
						logger.error("createSearchIndexFully error. commit count=" + count + ", found count from Collection=" + foundCnt);
					}
				}else {
					logger.error("deleteAllIndex error.");
				}
			 } else {
				 logger.error("createSearchIndexFully error. dump data fail. size=" + node_map.size() + ", collection=" + c1);
			 }
			 
	    	return re;
	    }
	    
	    
	    public boolean createIndexFullyForAll(boolean needSwitch){
	    	boolean re = false;
	    	try {
	    		String node_collection = createIndexFullyForNode(needSwitch);
		    	
		    	String taxonomy_collection = createIndexFullyForTaxonomy(needSwitch);
		    	
		    	if(node_collection !=null && taxonomy_collection != null){
		    		//String live_node = zkService.getLiveNode();
		    		//if(needSwitch){
					//	re = switchIndex(node_collection,Configure.SolrCollectionNodeAlias, live_node) && switchIndex(taxonomy_collection,Configure.SolrCollectionTaxonomyAlias, live_node);
						logger.info(" Switch IndexFullyForAll success !");
						re = true;
		    		//}
		    	}else {
					logger.error(" CreateIndexFullyForAll failed !");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	return re;
	    }
	    
	    public long getSolrDataCount(SolrServer solrServer) {
	    	long count = 0;
	    	SolrQuery query = new SolrQuery();
	        query.setQuery("*:*");
	        try {
	            QueryResponse response = solrServer.query(query);  
	            SolrDocumentList docs = response.getResults();
	            if (docs != null) {
	            	count = docs.getNumFound();
	            }
	        } catch (SolrServerException e) {  
	            e.printStackTrace();  
	            logger.error(e.getMessage());
	        } catch(Exception e) {
	            e.printStackTrace();
	            logger.error(e.getMessage());
	        }
	    	return count;
	    }
	    
	    public int createIndexForNode(SolrServer solrServer,Map<String, Node> nodeMap,boolean isFully){
			logger.info("create index begin.....");
			long starttime = System.currentTimeMillis();
	    	String key = null;
	    	Node node = null;
            Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();  

            int count = 0;
            try {
	        	for(Entry<String, Node> entry : nodeMap.entrySet()){
	        		count++;
	    			key = entry.getKey();
	    			node = entry.getValue();
    			
		            SolrInputDocument doc = new SolrInputDocument();
		            doc.addField("id", node.getNid());
		            doc.addField("nid", node.getNid());  
		            doc.addField("vid", node.getVid());  
		            doc.addField("title", node.getTitle());  
		            doc.addField("summary", node.getSummary());  
		            doc.addField("sitedate", node.getSitedate());  
		            doc.addField("body", node.getInvbody());  
		            doc.addField("type_tid", node.getContentTypeTid());  
		            doc.addField("type_name", node.getContentTypeName());  
		          //  doc.addField("channel_tid", node.getChannelTid());  
		            doc.addField("channel_name", node.getChannelName());  
		          //  doc.addField("sub_channel_tid", node.getSubChannelTid());  
		            doc.addField("sub_channel_name", node.getSubChannelName());  
		            doc.addField("partner_name", node.getPartenr_name());  
		            doc.addField("partner_id", node.getPartenr_id());  
		            doc.addField("image_url", node.getImage_url());  
		            doc.addField("image_size", node.getImage_size());  
		            doc.addField("video_url", node.getVideo_url());  
		            doc.addField("video_size", node.getVideo_size());  
		            doc.addField("img_alt", node.getImg_alt());
		            doc.addField("img_title", node.getImg_title());  
		            doc.addField("author_id", node.getAuthor_id());  
		            doc.addField("author_name", node.getAuthor_name());
		            doc.addField("ticker", node.getTickers());  
		            doc.addField("url", node.getUrl());
		            doc.addField("tag_name", node.getTag_names());
		            doc.addField("tag_id", node.getTag_ids()); 
		            doc.addField("is_mfo_url", node.isIs_mfo_url()); 
		            doc.addField("is_include_ticker", node.isIs_include_ticker()); 
		            doc.addField("tag_group_name", node.getTag_group_name()); 
		            doc.addField("tag_group_id", node.getTag_group_id()); 
		            doc.addField("is_sponsored_content", node.getSponsored_content()==1?true:false); 
		            doc.addField("is_auto_republish", node.getAuto_republish_type()!=null && node.getAuto_republish_type().contains(1)?true:false); 
		            doc.addField("term_type", node.getTerm_type());
		            doc.addField("advertising_name", node.getAdvertising_name());
		            doc.addField("sub_advertising_name", node.getSub_advertising_name());
		           // doc.addField("titleOrder", node.getTitleOrder());
		            doc.addField("titleOrder", node.getTitleOrder());
		            doc.addField("priority", node.getPriorities());
		            doc.addField("featured_video_nid", node.getFeatured_video_nid());
		            doc.addField("show_image", node.getShow_main_image()==1?true:false);
		            doc.addField("original_sitedate", node.getOriginal_sitedate());
		            doc.addField("final_time", node.getFinal_time());
		            doc.addField("seo_standout", node.getSeo_standout()==1?true:false);
		            doc.addField("index", node.isIndex());
		            doc.addField("ticker_search", node.getTicker_search());
		            doc.addField("partner_url", node.getPartner_url());
		            doc.addField("partner_image_url", node.getPartner_image_url());
		            doc.addField("partnerlinks_title", node.getPartnerlinks_titles());
		            doc.addField("partnerlinks_url", node.getPartnerlinks_urls());
		            doc.addField("node_type", node.getNode_type());
		            doc.addField("root_nid", node.getRoot_nid());
		            doc.addField("root_title", node.getRoot_title());
		            doc.addField("author_url", node.getAuthor_url());
		            doc.addField("author_legal_disclaimer", node.getAuthor_legal_disclaimer());
		            doc.addField("video_series_id", node.getVideo_series_id());
		            doc.addField("video_series_name", node.getVideo_series_name());
		            doc.addField("sitedate_dt", new Date(node.getSitedate()*1000));
		            doc.addField("sitedate_pub", node.getSitedate()==0?"": DataConvertor.longString2Publish(node.getSitedate()+"000"));
		            doc.addField("video_filemime", node.getVideo_filemime());
		            doc.addField("image_filemime", node.getImage_filemime());
		            
		            doc.addField("has_children", node.getHas_children()==1?true:false);
		            doc.addField("depth", node.getDepth());
		            doc.addField("weight", node.getWeight());
		            doc.addField("nav_map", node.getNavMap());
		            doc.addField("plid", node.getPlid());
		            doc.addField("mlid", node.getMlid());
		            doc.addField("pnid", node.getPnid());
			        doc.addField("tree_index", node.getTree_index()==null?"":node.getTree_index());
			        long syndate =  node.getSitedate()>node.getSyn_date()?node.getSitedate():node.getSyn_date();
			        doc.addField("syndate_pub", syndate==0?"": DataConvertor.longString2Publish(syndate+"000"));
			        doc.addField("syndate_dt", new Date(syndate*1000));
			        doc.addField("auto_republish_type", node.getAuto_republish_type());
			        doc.addField("related_id", node.getRelatedList());
			        
			        
		            docs.add(doc);  
		            
		            if(count%10000==0 || count>=nodeMap.size()){
		            	logger.info("doc commit count:"+count);
		            	solrServer.add(docs);
						solrServer.commit();
						docs.clear();
		            }
	    		}
	        	commitCount = count;
	        	if(isFully){
	        		long ostime =System.currentTimeMillis();
		    	    UpdateResponse updataR = solrServer.optimize();
					logger.info("optimize stat :"+updataR.getStatus()+",exe time:"+(System.currentTimeMillis()-ostime));
	        	}

            }catch(SolrServerException e) {  
	            e.printStackTrace();
	            logger.error(e.getMessage());
	            return 0;
	        }catch(IOException e){  
	            e.printStackTrace();
	            logger.error(e.getMessage());
	            return 0;
	        }catch (Exception e) {  
	            e.printStackTrace();
	            logger.error(e.getMessage());
	            return 0;
	        }        
			logger.info("create index success,exe time :"+(System.currentTimeMillis()-starttime));

	    	return count;
	    }
	    
	    public int createIndexForFeed(SolrServer solrServer,Map<String, Node> nodeMap,boolean isFully){
			logger.info("create index begin.....");
			long starttime = System.currentTimeMillis();
	    	String key = null;
	    	Node node = null;
            Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();  

            int count = 0;
            try {
	        	for(Entry<String, Node> entry : nodeMap.entrySet()){
	        		count++;
	    			key = entry.getKey();
	    			node = entry.getValue();
    			
		            SolrInputDocument doc = new SolrInputDocument();
		            doc.addField("id", node.getNid());
		            doc.addField("nid", node.getNid());  
		            doc.addField("vid", node.getVid());  
		            doc.addField("title", node.getTitle());  
		            doc.addField("summary", node.getSummary());  
		            doc.addField("sitedate", node.getSitedate());  
		            doc.addField("body", node.getInvbody());  
		            doc.addField("type_tid", node.getContentTypeTid());  
		            doc.addField("type_name", node.getContentTypeName());  
		          //  doc.addField("channel_tid", node.getChannelTid());  
		            doc.addField("channel_name", node.getChannelName());  
		          //  doc.addField("sub_channel_tid", node.getSubChannelTid());  
		            doc.addField("sub_channel_name", node.getSubChannelName());  
		            doc.addField("partner_name", node.getPartenr_name());  
		            doc.addField("partner_id", node.getPartenr_id());  
		            doc.addField("image_url", node.getImage_url());  
		            doc.addField("image_size", node.getImage_size());  
		            doc.addField("video_url", node.getVideo_url());  
		            doc.addField("video_size", node.getVideo_size());  
		            doc.addField("img_alt", node.getImg_alt());
		            doc.addField("img_title", node.getImg_title());  
		            doc.addField("author_id", node.getAuthor_id());  
		            doc.addField("author_name", node.getAuthor_name());
		            doc.addField("ticker", node.getTickers());  
		            doc.addField("url", node.getUrl());
		            doc.addField("tag_name", node.getTag_names());
		            doc.addField("tag_id", node.getTag_ids()); 
		            doc.addField("is_mfo_url", node.isIs_mfo_url()); 
		            doc.addField("is_include_ticker", node.isIs_include_ticker()); 
		            doc.addField("tag_group_name", node.getTag_group_name()); 
		            doc.addField("tag_group_id", node.getTag_group_id()); 
		            doc.addField("is_sponsored_content", node.getSponsored_content()==1?true:false); 
		            doc.addField("is_auto_republish", node.getAuto_republish_type()!=null && node.getAuto_republish_type().contains(1)?true:false); 
		            doc.addField("term_type", node.getTerm_type());
		            doc.addField("advertising_name", node.getAdvertising_name());
		            doc.addField("sub_advertising_name", node.getSub_advertising_name());
		           // doc.addField("titleOrder", node.getTitleOrder());
		            doc.addField("titleOrder", node.getTitleOrder());
		            doc.addField("priority", node.getPriorities());
		            doc.addField("featured_video_nid", node.getFeatured_video_nid());
		            doc.addField("show_image", node.getShow_main_image()==1?true:false);
		            doc.addField("original_sitedate", node.getOriginal_sitedate());
		            doc.addField("final_time", node.getFinal_time());
		            doc.addField("seo_standout", node.getSeo_standout()==1?true:false);
		            doc.addField("index", node.isIndex());
		            doc.addField("ticker_search", node.getTicker_search());
		            doc.addField("partner_url", node.getPartner_url());
		            doc.addField("partner_image_url", node.getPartner_image_url());
		            doc.addField("partnerlinks_title", node.getPartnerlinks_titles());
		            doc.addField("partnerlinks_url", node.getPartnerlinks_urls());
		            doc.addField("node_type", node.getNode_type());
		            doc.addField("root_nid", node.getRoot_nid());
		            doc.addField("root_title", node.getRoot_title());
		            doc.addField("author_url", node.getAuthor_url());
		            doc.addField("author_legal_disclaimer", node.getAuthor_legal_disclaimer());
		            doc.addField("video_series_id", node.getVideo_series_id());
		            doc.addField("video_series_name", node.getVideo_series_name());
		            doc.addField("sitedate_dt", new Date(node.getSitedate()*1000));
		            doc.addField("sitedate_pub", node.getSitedate()==0?"": DataConvertor.longString2Publish(node.getSitedate()+"000"));
		            doc.addField("video_filemime", node.getVideo_filemime());
		            doc.addField("image_filemime", node.getImage_filemime());
		            
		            doc.addField("has_children", node.getHas_children()==1?true:false);
		            doc.addField("depth", node.getDepth());
		            doc.addField("weight", node.getWeight());
		            doc.addField("nav_map", node.getNavMap());
		            doc.addField("plid", node.getPlid());
		            doc.addField("mlid", node.getMlid());
		            doc.addField("pnid", node.getPnid());
			        doc.addField("tree_index", node.getTree_index()==null?"":node.getTree_index());
			        long syndate =  node.getSitedate()>node.getSyn_date()?node.getSitedate():node.getSyn_date();
			        doc.addField("syndate_pub", syndate==0?"": DataConvertor.longString2Publish(syndate+"000"));
			        doc.addField("syndate_dt", new Date(syndate*1000));
			        doc.addField("auto_republish_type", node.getAuto_republish_type());
			        doc.addField("related_id", node.getRelatedList());
			        doc.addField("url_page", node.getUrl_page());
			        
			        
		            docs.add(doc);  
		            
		            if(count%10000==0 || count>=nodeMap.size()){
		            	logger.info("doc commit count:"+count);
		            	solrServer.add(docs);
						solrServer.commit();
						docs.clear();
		            }
	    		}
	        	commitCount = count;
	        	if(isFully){
	        		long ostime =System.currentTimeMillis();
		    	    UpdateResponse updataR = solrServer.optimize();
					logger.info("optimize stat :"+updataR.getStatus()+",exe time:"+(System.currentTimeMillis()-ostime));
	        	}

            }catch(SolrServerException e) {  
	            e.printStackTrace();
	            logger.error(e.getMessage());
	            return 0;
	        }catch(IOException e){  
	            e.printStackTrace();
	            logger.error(e.getMessage());
	            return 0;
	        }catch (Exception e) {  
	            e.printStackTrace();
	            logger.error(e.getMessage());
	            return 0;
	        }        
			logger.info("create index success,exe time :"+(System.currentTimeMillis()-starttime));

	    	return count;
	    }

	    public int createIndexForSearch(SolrServer solrServer,Map<String, Node> nodeMap,boolean isFully){
			logger.info("create index begin.....");
			long starttime = System.currentTimeMillis();
	    	String key = null;
	    	Node node = null;
            Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();  

            int count = 0;
            try {
	        	for(Entry<String, Node> entry : nodeMap.entrySet()){
	        		count++;
	    			key = entry.getKey();
	    			node = entry.getValue();
    			
		            SolrInputDocument doc = new SolrInputDocument();
		            doc.addField("id", node.getNid());
		            doc.addField("nid", node.getNid());  
		            doc.addField("vid", node.getVid());  
		            doc.addField("title", node.getTitle()); 
		    		Matcher m_html_summary = p_html.matcher(node.getSummary()==null?"":node.getSummary());
		            doc.addField("summary",m_html_summary.replaceAll(""));  
		            doc.addField("sitedate", node.getSitedate());  
		            Matcher m_html = p_html.matcher(node.getInvbody()==null?"":node.getInvbody());
		            doc.addField("body",  m_html.replaceAll(""));  
		            doc.addField("type_tid", node.getContentTypeTid());  
		            doc.addField("type_name", node.getContentTypeName());  
		            doc.addField("channel_tid", node.getChannelTid());  
		            doc.addField("channel_name", node.getChannelName());  
		            doc.addField("sub_channel_tid", node.getSubChannelTid());  
		            doc.addField("sub_channel_name", node.getSubChannelName());  
		            doc.addField("partner_name", node.getPartenr_name());  
		            doc.addField("partner_id", node.getPartenr_id());  
		            doc.addField("image_url", node.getImage_url());  
		            doc.addField("image_size", node.getImage_size());  
		            doc.addField("video_url", node.getVideo_url());  
		            doc.addField("video_size", node.getVideo_size());  
		            doc.addField("img_alt", node.getImg_alt());
		            doc.addField("img_title", node.getImg_title());  
		            doc.addField("author_id", node.getAuthor_id());  
		            doc.addField("author_name", node.getAuthor_name());
		            doc.addField("ticker", node.getTickers());  
		            doc.addField("url", node.getUrl());
		            doc.addField("tag_name", node.getTag_names());
		            doc.addField("tag_id", node.getTag_ids()); 
		            doc.addField("is_mfo_url", node.isIs_mfo_url()); 
		            doc.addField("is_include_ticker", node.isIs_include_ticker()); 
		            doc.addField("tag_group_name", node.getTag_group_name()); 
		            doc.addField("tag_group_id", node.getTag_group_id()); 
		            doc.addField("is_sponsored_content", node.getSponsored_content()==1?true:false); 
		            doc.addField("is_auto_republish", node.getAuto_republish_type()!=null && node.getAuto_republish_type().contains(1)?true:false); 
		            doc.addField("term_type", node.getTerm_type());
		            doc.addField("advertising_name", node.getAdvertising_name());
		            doc.addField("sub_advertising_name", node.getSub_advertising_name());
		           // doc.addField("titleOrder", node.getTitleOrder());
		            doc.addField("titleOrder", node.getTitleOrder());
		            doc.addField("priority", node.getPriorities());
		            doc.addField("featured_video_nid", node.getFeatured_video_nid());
		            doc.addField("show_image", node.getShow_main_image()==1?true:false);
		            doc.addField("original_sitedate", node.getOriginal_sitedate());
		            doc.addField("final_time", node.getFinal_time());
		            doc.addField("seo_standout", node.getSeo_standout()==1?true:false);
		            doc.addField("index", node.isIndex());
		            doc.addField("ticker_search", node.getTicker_search());
		            doc.addField("partner_url", node.getPartner_url());
		            doc.addField("partner_image_url", node.getPartner_image_url());
		            doc.addField("partnerlinks_title", node.getPartnerlinks_titles());
		            doc.addField("partnerlinks_url", node.getPartnerlinks_urls());
		            doc.addField("node_type", node.getNode_type());
		            doc.addField("root_nid", node.getRoot_nid());
		            doc.addField("root_title", node.getRoot_title());
		            doc.addField("author_url", node.getAuthor_url());
		            doc.addField("author_legal_disclaimer", node.getAuthor_legal_disclaimer());
		            doc.addField("video_series_id", node.getVideo_series_id());
		            doc.addField("video_series_name", node.getVideo_series_name());
		            doc.addField("sitedate_dt", new Date(node.getSitedate()*1000));
		            doc.addField("sitedate_pub", node.getSitedate()==0?"": DataConvertor.longString2Publish(node.getSitedate()+"000"));
		            doc.addField("video_filemime", node.getVideo_filemime());
		            doc.addField("image_filemime", node.getImage_filemime());
		            
		            doc.addField("has_children", node.getHas_children()==1?true:false);
		            doc.addField("depth", node.getDepth());
		            doc.addField("weight", node.getWeight());
		            doc.addField("nav_map", node.getNavMap());
		            doc.addField("plid", node.getPlid());
		            doc.addField("mlid", node.getMlid());
		            doc.addField("pnid", node.getPnid());
			        doc.addField("tree_index", node.getTree_index()==null?"":node.getTree_index());
			        long syndate =  node.getSitedate()>node.getSyn_date()?node.getSitedate():node.getSyn_date();
			        doc.addField("syndate_pub", syndate==0?"": DataConvertor.longString2Publish(syndate+"000"));
			        doc.addField("syndate_dt", new Date(syndate*1000));
			        doc.addField("auto_republish_type", node.getAuto_republish_type());
			        
			        
		            docs.add(doc);  
		            
		            if(count%10000==0 || count>=nodeMap.size()){
		            	logger.info("doc commit count:"+count);
		            	solrServer.add(docs);
						solrServer.commit();
						docs.clear();
		            }
	    		}
	        	commitCount = count;
	        	if(isFully){
	        		long ostime =System.currentTimeMillis();
		    	    UpdateResponse updataR = solrServer.optimize();
					logger.info("optimize stat :"+updataR.getStatus()+",exe time:"+(System.currentTimeMillis()-ostime));
	        	}

            }catch(SolrServerException e) {  
	            e.printStackTrace();
	            logger.error(e.getMessage());
	            return 0;
	        }catch(IOException e){  
	            e.printStackTrace();
	            logger.error(e.getMessage());
	            return 0;
	        }catch (Exception e) {  
	            e.printStackTrace();
	            logger.error(e.getMessage());
	            return 0;
	        }        
			logger.info("create index success,exe time :"+(System.currentTimeMillis()-starttime));

	    	return count;
	    }

	    public int createIndexForTaxonomy(SolrServer solrServer,List<Taxonomy> list,boolean isFully){
			logger.info("create index begin.....");
			long starttime = System.currentTimeMillis();
	    	String key = null;
            Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();  
            int count = 0;
            long nid ;
            try {
	        	for(Taxonomy t : list){
	        		count++;
	    			nid = t.getNid();
	    			key = nid+"_"+t.getVid();
	    			
		            SolrInputDocument doc = new SolrInputDocument();  
		            doc.addField("id",t.getId());
		            doc.addField("nid", t.getNid());
		            doc.addField("ad_target", t.getAdTarget());
		            doc.addField("advertising", t.getAdvertising());  
		            doc.addField("channel", t.getChannel());  
		            doc.addField("created_by", t.getCreatedBy());  
		            doc.addField("created_on",t.getCreatedOn()==null?null: DataConvertor.longString2Calendar(t.getCreatedOn()+"000"));  
		        //    doc.addField("design", t.getDesign());  
		            doc.addField("feature", t.getFeature());  
		            doc.addField("follow", t.isFollow());  
		            doc.addField("hash_key", t.getHashKey());  
		            doc.addField("index", t.isIndex());  
		            doc.addField("interest_level", t.getInterestLevel());  
		            doc.addField("master", t.getMaster());  
		            doc.addField("no_index_params", Boolean.valueOf(t.getOutNoIndexParams()));  
		            doc.addField("page_id", t.getPageID());  
		            doc.addField("path", t.getPath());  
		            doc.addField("sub_advertising", t.getSubAdvertising());  
		            doc.addField("sub_channel", t.getSubChannel());  
		            doc.addField("tag_name", t.getTag_names());
		            doc.addField("tag_id", t.getTag_ids());
		            doc.addField("priority", t.getTag_prioritys());
		            doc.addField("timelessness", t.getTimelessness());  
		            doc.addField("type", t.getType());
		            doc.addField("updated_by", t.getUpdatedBy());
		            doc.addField("updated_on", t.getUpdatedOn()==null?null:DataConvertor.longString2Calendar(t.getUpdatedOn()+"000"));  
		            doc.addField("website", t.getWebsite());
		            doc.addField("data_type", t.getData_type());
		            doc.addField("url", t.getUrl());
		            doc.addField("keywords", t.getKeywords());
		            doc.addField("tag_group_name", t.getTag_group_name()); 
		            doc.addField("tag_group_id", t.getTag_group_id()); 
		            doc.addField("node_type", t.getNode_type()); 
		            doc.addField("web_tool_settings", t.getWebToolSettings()); 
		            doc.addField("metatags_title", t.getMetatags_title()); 
		            doc.addField("metatags_description", t.getMetatags_description()); 
		            
		            t.getLucrativeness();
		            docs.add(doc);  
		            
		            if(count%10000==0 || count>=list.size()){
		            	logger.info("doc commit count:"+count);
		            	solrServer.add(docs);
						solrServer.commit();
						docs.clear();
		            }
	    		}
	        	commitCount = count;
	        	if(isFully){
	        		long ostime =System.currentTimeMillis();
		    	    UpdateResponse updataR = solrServer.optimize();
					logger.info("optimize stat :"+updataR.getStatus()+",exe time:"+(System.currentTimeMillis()-ostime));
	        	}
            }catch(SolrServerException e) {  
	            e.printStackTrace(); 
	            return 0;
	        }catch(IOException e){  
	            e.printStackTrace();
	            return 0;
	        }catch (Exception e) {  
	            e.printStackTrace();  
	            return 0;
	        }        
			logger.info("create index success,exe time :"+(System.currentTimeMillis()-starttime));
	    	return count;
	    }
	    
	    public String createIndexFullyForTaxonomy(boolean needSwitch){
	    	String re = null;
	    	try {
	    		List<Taxonomy> list = taxonomyService.getTaxonomyListForNode();
				List<Taxonomy> list2 = taxonomyService.getTaxonomyListForPage();
				
				if(list != null && list.size()>0){
					list.addAll(list2);
					logger.info("dump all taxonomy size :"+list.size());
					String collection_name = getCollection(true, false);
					CloudSolrServer cloudSolrServer = IndexService.getCloudSolrServer(Configure.ZookeeperHosts, collection_name); 
					boolean delete = deleteAllIndex(cloudSolrServer);
					if(delete){
		            	logger.info("delete All Index success ,collection:"+collection_name);
						int count =  createIndexForTaxonomy(cloudSolrServer, list, true);
						long foundCnt = getSolrDataCount(getCloudSolrServer(Configure.ZookeeperHosts, Configure.SolrCollectionTaxonomyAlias));
						if(count > 0 ){
							if(count > foundCnt*0.7){
								re = collection_name;
								if(needSwitch){
									boolean success = switchIndex(collection_name,Configure.SolrCollectionTaxonomyAlias, zkService.getLiveNode());
									if(success){
										logger.info("index switchIndex success ,size:"+count);
									}
								}
							}else {
						    	try {
									MailUtil mail = MailUtil.newInstance(Configure.mail_smtp_host, Configure.mail_user, Configure.mail_password);
									mail.send(Configure.mail_from, Configure.mail_to, "SolrClient prod -- Taxonomy Data number Less Than 70% ", "SolrClient -- Taxonomy Data number Less Than 70% ");
								} catch (Exception e) {
									logger.error(e.getMessage());
								} 
							}
							
						} else {
							logger.error("createIndexFully error. commit count=" + count + ", found count from Collection=" + foundCnt);
						}
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	return re;
			
	    }
	    
	    public boolean createNodeIndexIncrementalForTaxonomy(int nid,int vid,String treeAction){
	    	boolean re = false;
	    	try {
	    		List<Taxonomy> list = taxonomyService.getTaxonomyListForNode(nid, vid, treeAction);
				
				if(list != null && list.size()>0){
					logger.info("dump taxonomy node size :"+list.size());
					String collection_name = Configure.SolrCollectionTaxonomyAlias;
					CloudSolrServer cloudSolrServer = IndexService.getCloudSolrServer(Configure.ZookeeperHosts, collection_name); 
					int count =  createIndexForTaxonomy(cloudSolrServer, list, false);
					if(count > 0 ){
						re = true;
						logger.info("create taxonomy index success. commit count=" + count+ ",nid="+nid);
					} else {
						logger.error("create taxonomy  index error. commit count=" + count+ ",nid="+nid );
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
	    	return re;
			
	    }
	    
	    
	    /**
	    public int createIndexMult(Map<String, Node> nodeMap){
	    	int count = 0;
	    	String solrServerUrl = "http://solrcloud001.la1.vcinv.net:8080/solr/invDataSolr,http://solrcloud002.la1.vcinv.net:8080/solr/invDataSolr,http://solrcloud003.la1.vcinv.net:8080/solr/invDataSolr";
	    	ConcurrentUpdateSolrServer server = new ConcurrentUpdateSolrServer(solrServerUrl, 100000000, 5);
	        server.setParser(new XMLResponseParser());  
	        server.setConnectionTimeout(10000);
	        server.setSoTimeout(200000);
	        Collection<SolrInputDocument> docs = new LinkedList<SolrInputDocument>();  
	        SolrInputDocument doc ;  
	        String key = null;
	    	Node node = null;
	    	try {
		    	for(Entry<String, Node> entry : nodeMap.entrySet()){
	        		count++;
	        		doc = new SolrInputDocument(); 
	    			key = entry.getKey();
	    			node = entry.getValue();
		            doc.addField("id", node.getNid());
		            doc.addField("nid", node.getNid());  
		            doc.addField("vid", node.getVid());  
		            doc.addField("title", node.getTitle());  
		            doc.addField("summary", node.getSummary());  
		            doc.addField("sitedate", node.getSitedate());  
		            doc.addField("body", node.getInvbody());  
		            doc.addField("type_tid", node.getContentTypeTid());  
		            doc.addField("type_name", node.getContentTypeName());  
		            doc.addField("channel_tid", node.getChannelTid());  
		            doc.addField("channel_name", node.getChannelName());  
		            doc.addField("partenr_name", node.getPartenr_name());  
		            doc.addField("partenr_id", node.getPartenr_id());  
		            doc.addField("image_url", node.getImage_url());  
		            doc.addField("image_size", node.getImage_size());  
		            doc.addField("video_url", node.getVideo_url());  
		            doc.addField("video_size", node.getVideo_size());  
		            doc.addField("img_alt", node.getImg_alt());
		            doc.addField("img_title", node.getImg_title());  
		            doc.addField("author_id", node.getAuthor_id());  
		            doc.addField("author_name", node.getAuthor_name());
		            doc.addField("ticker", node.getTickers());  
		            doc.addField("url", node.getUrl());
		            doc.addField("tag_name", node.getTag_names());
		            doc.addField("tag_id", node.getTag_ids()); 
		            doc.addField("is_mfo_url", node.isIs_mfo_url()); 
		            doc.addField("is_include_ticker", node.isIs_include_ticker()); 
		            doc.addField("tag_group_name", node.getTag_group_name()); 
		            doc.addField("tag_group_id", node.getTag_group_id()); 
		            
		            docs.add(doc);  
		             
		            To immediately commit after adding documents, you could use: 
		   
		             UpdateRequest req = new UpdateRequest(); 
		             req.setAction( UpdateRequest.ACTION.COMMIT, false, false ); 
		             req.add( docs ); 
		             UpdateResponse rsp = req.process( server ); 
		             
		            if(count%5000==0 || count>=nodeMap.size()){
	            		server.add(docs);  
		                server.commit();  
		                docs.clear();  
						logger.info("doc commit times:"+count);
		            }
		         
	    	}
			

	    }catch(SolrServerException e) {  
        	logger.error("Add docs Exception !!!");  
            e.printStackTrace(); 
            return 0;
        }catch(IOException e){  
            e.printStackTrace();
            return 0;
        }catch (Exception e) {  
        	logger.error("Unknowned Exception!!!!!");  
            e.printStackTrace();  
            return 0;
        }        
	        logger.info("Commits successfully!......");  
	    	return count;
	    }
	    
	    **/
	    
	    
	    
	    /** 
	     * @param args 
	     * @throws SecurityException 
	     * @throws NoSuchFieldException 
	     */  
	    public static void main(String[] args) throws NoSuchFieldException, SecurityException { 
	    	
	    	
	    	
	    	Configure.init();
	    	cloudSolrServer = getCloudSolrServer(Configure.ZookeeperHosts_feed, "inv_feed_prod_collection2");
	    	String co = cloudSolrServer.getZkStateReader().getAliases().getCollectionAlias("inv_feed_prod");
	    	System.out.println(co);
	    	
	    	/**
	    	MailUtil mail = MailUtil.newInstance(Configure.mail_smtp_host, Configure.mail_user, Configure.mail_password);
	    	try {
				mail.send(Configure.mail_from, Configure.mail_to, "SolrClient prod -- Taxonomy Data number Less Than 70% ", "SolrClient -- Taxonomy Data number Less Than 70% ");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    	
	    	
	    	String json = "{\"collection\":{\"invDataSolrAlias\":\"invDataSolr\",\"inv_staging\":\"inv_staging_collection2\",\"inv_prod\":\"inv_prod_collection1\"}}";
	    
	    	JSONObject jsonObject = JSONObject.fromObject(json);
	    	JSONObject o= (JSONObject) jsonObject.get("collection");
	    	for(Object name : o.keySet()){
		    	System.out.println(o.get(name.toString()));
	    	}
	    	
	    	long a = System.currentTimeMillis();
	    	TaskEntity taskEntity = new TaskEntity("node", "update", 10000, 10000,a );
	    	Gson gson = new Gson();
	    	System.out.println(gson.toJson(taskEntity));
	    	///System.out.println(System.currentTimeMillis());
	    	
	    	// 	JsonElement jsonElement = g
//System.out.println(jsonElement.get);
	    	
	    	System.out.println(System.currentTimeMillis());
	    	
		        final String zkHost = "solrcloud002.la1.vcinv.net:2181,solrcloud001.la1.vcinv.net:2181,solrcloud003.la1.vcinv.net:2181";       
		        final String  defaultCollection = "inv_staging_collection1";  
		          
		        CloudSolrServer cloudSolrServer = getCloudSolrServer(zkHost,defaultCollection);         
		        System.out.println("The Cloud SolrServer Instance has benn created!");            
		        cloudSolrServer.setDefaultCollection(defaultCollection);  
		        cloudSolrServer.shutdown();
		        
		        CloudSolrServer cloudSolrServer2 = getCloudSolrServer(zkHost,"inv_staging_collection2");   
		        
			    System.out.println(cloudSolrServer.getDefaultCollection());;
**/
		        /**
		        System.out.println("The cloud Server has been connected !!!!"); 
		        Configure.init();
				ZKService zkService = new ZKService("solrcloud002.la1.vcinv.net:2181,solrcloud001.la1.vcinv.net:2181,solrcloud003.la1.vcinv.net:2181", "", "/solrcloud/task_queue");
				zkService.start();
				System.out.println(zkService.getLiveNode());
**/
/**
		        IndexService main = new IndexService();  
		        //main.setZkService(zkService);
		        boolean success = main.deleteAllIndex(cloudSolrServer);
		        System.out.println(success);
		        
		        /**
		        Map<String,List<Tag>> tagMap = new HashMap<String, List<Tag>>();
		        List<Tag> list = new ArrayList<Tag>();
		        list.add(new Tag(new String[]{"1","1","1","test"}));
		        tagMap.put("1_1", list);
		        
		        Map<String,String> aliasMap = new HashMap<String, String>();
		        aliasMap.put("1_1", "/url/1");
		        
		        Map<String,Node> nodeMap = new HashMap<String, Node>();
		        nodeMap.put("1_1", new Node(new String[]{"1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"}));
		        
		        Map<String,List<String>> tickerNodeMap = new HashMap<String, List<String>>();
		        List<String> list2 = new ArrayList<String>();
		        list2.add("msft");
		        tickerNodeMap.put("1_1", list2);
		        //测试实例！  
		        IndexService main = new IndexService();  
		        main.createIndex(cloudSolrServer,nodeMap);
		        **/
		//        System.out.println("测试添加index！！！");       
		        //添加index  
		//        test.addIndex(cloudSolrServer);  
		          
		//        System.out.println("测试查询query！！！！");  
		//        test.search(cloudSolrServer, "id:*");  
		//          
		//        System.out.println("测试删除！！！！");  
		//        test.deleteAllIndex(cloudSolrServer);  
		//        System.out.println("删除所有文档后的查询结果：");  
		    //    test.search(cloudSolrServer, "zhan");      
		//        System.out.println("hashCode"+test.hashCode());
		                  
		         // release the resource   
		   //     cloudSolrServer.shutdown();  
	   
	    }  
}
