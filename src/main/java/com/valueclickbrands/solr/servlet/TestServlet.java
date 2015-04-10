package com.valueclickbrands.solr.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.cloud.Aliases;
import org.apache.solr.common.cloud.ZkStateReader;
import com.google.gson.Gson;
import com.valueclickbrands.solr.model.Node;
import com.valueclickbrands.solr.model.Taxonomy;
import com.valueclickbrands.solr.service.DumpService;
import com.valueclickbrands.solr.service.IndexService;
import com.valueclickbrands.solr.service.TaxonomyService;
import com.valueclickbrands.solr.util.Configure;
import com.valueclickbrands.solr.util.MailUtil;

public class TestServlet extends HttpServlet {
	protected static Logger logger = Logger.getLogger(TestServlet.class);
	public static final String RETURN_FORMAT_TYPE_XML = "xml";
	public static final String RETURN_FORMAT_TYPE_JSON = "json";
	private DumpService dumpService;
	private IndexService indexService;
	private TaxonomyService taxonomyService;
	private Gson gson = new Gson();
	
	public void setTaxonomyService(TaxonomyService taxonomyService) {
		this.taxonomyService = taxonomyService;
	}

	public IndexService getIndexService() {
		return indexService;
	}

	public void setIndexService(IndexService indexService) {
		this.indexService = indexService;
	}

	private String siteID;
	
	
	public DumpService getDumpService() {
		return dumpService;
	}


	public void setDumpService(DumpService dumpService) {
		this.dumpService = dumpService;
	}


	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		service(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		service(req, resp);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		long starttime = System.currentTimeMillis();
		request.setCharacterEncoding("UTF-8");
		HashMap<String,Object> parameter = null;
		String msg = "success";
		logger.info("Parameter--->"+parameter);
		String  defaultCollection = request.getParameter("collection")==null?null:request.getParameter("collection").trim();
		String  action = request.getParameter("action")==null?null:request.getParameter("action").trim();
		String  nid = request.getParameter("nid")==null?null:request.getParameter("nid").trim();
		String  vid = request.getParameter("vid")==null?null:request.getParameter("vid").trim();

		if (action == null || !"1".equals(action) && defaultCollection == null || "1".equals(action) && (nid == null || vid == null)) {
			msg = "The Parameter Error. collection=" + defaultCollection + ", action=" + action + ", nid=" + nid + ", vid=" + vid;
			long endtime = System.currentTimeMillis();
        	Map<String, String> result = new HashMap<String, String>();
        	result.put("message", msg);
        	result.put("delay", endtime-starttime + "ms");
        	response.setContentType("application/json;charset=utf-8");
			response.getWriter().println(gson.toJson(result));
		}
//		int hasContent = 0;
//		if(parameter.containsKey(PARAMETER_NAME_CONTENT)){
//			hasContent = (Integer)parameter.get(PARAMETER_NAME_CONTENT);
//		}
		StopWatch watch = new StopWatch();
		watch.start();
//		boolean isPhrase = (Integer)parameter.get(PARAMETER_NAME_PHRASE)==1;
		if(action.equals("1")){
			boolean count =  indexService.createIndexIncrementalForNode(Integer.valueOf(nid), Integer.valueOf(vid),"fully");
			if(!count){
				msg = "failed";
			}
			logger.info("index success ,size:"+count);

		}else if(action.equals("2")){
			String zkHost = Configure.ZookeeperHosts;
			 CloudSolrServer cloudSolrServer = IndexService.getCloudSolrServer(zkHost, defaultCollection);
			 ZkStateReader zkStateReader = cloudSolrServer.getZkStateReader();
			 Aliases aliases = zkStateReader.getAliases();
			 String collection = aliases.getCollectionAlias(Configure.SolrCollectionNodeAlias);
			 if (defaultCollection.equals(collection) || Configure.SolrCollectionNodeAlias.equals(defaultCollection)) {
				 msg = "This collection(" + defaultCollection + ") is used by server, Can't update all index.";
			 } else {
				 Map<String, Map<String, Node>> node_map = dumpService.dump(Configure.index_type_all);
				 if(node_map.size()>0){
					 logger.info("The Cloud SolrServer Instance has benn created!");
					 int count =  indexService.createIndexForNode(cloudSolrServer,node_map.get(Configure.index_type_all),true);
					 // int count =  indexService.updateIndexForNode(cloudSolrServer,node_map,true,"fully");

					 if (count > 0) {
						 msg = "Load all db data to " + defaultCollection + " success. You can manual Replace the collection alias later.";
					 } else {
						 msg = "Load all db data to " + defaultCollection + " failed. Please check the error.";
					 }
				 }
			 }

		}else if(action.equals("3")) {
			indexService.createIndexFullyForTaxonomy(true);
		}else if(action.equals("4")) {
			indexService.createIndexFullyForAll(true);
		}else if(action.equals("5")) {
			indexService.createIndexIncrementalForTaxonomy(18066,"/markets/stocks/bna/");
			
		}else if(action.equals("6")) {
			try {
				MailUtil mail = MailUtil.newInstance(Configure.mail_smtp_host, Configure.mail_user, Configure.mail_password);
				mail.send(Configure.mail_from, Configure.mail_to, "SolrClient prod -- Taxonomy Data number Less Than 70% ", "SolrClient -- Taxonomy Data number Less Than 70% ");
			} catch (Exception e) {
				logger.error(e.getMessage());
			} 
		}else if (action.equals("7")){
			indexService.createIndexFullyForSearch(false);
		}

		 
		 /**
		response.setCharacterEncoding("UTF-8");
		response.setDateHeader("Expires", 0);
        response.addHeader("Pragma", "no-cache");  
        response.setHeader("Cache-Control", "no-cache");
        /**
        if(true){
        	response.setContentType("text/xml;charset=utf-8");
        }else{
        	response.setContentType("application/json;charset=utf-8");
        }
        **/
		
        try {
        	long endtime = System.currentTimeMillis();
        	Map<String, String> result = new HashMap<String, String>();
        	result.put("message", msg);
        	result.put("delay", endtime-starttime + "ms");
        	response.setContentType("application/json;charset=utf-8");
			response.getWriter().println(gson.toJson(result));
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			response.getWriter().close();
		}
	}

	public String getStringParameter(HttpServletRequest request,String name){
		return request.getParameter(name);
	}
	
	
	
}
