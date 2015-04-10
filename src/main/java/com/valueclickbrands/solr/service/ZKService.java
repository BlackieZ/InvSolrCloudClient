package com.valueclickbrands.solr.service;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentSkipListSet;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundPathAndBytesable;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import com.valueclickbrands.solr.util.Configure;
import com.valueclickbrands.zookeeper.service.ZKWatch;

/** 
 * @author Vanson Zou
 * @date Dec 18, 2014 
 */

public  class ZKService{
	static Logger LOG = Logger.getLogger(ZKService.class);
	
	private CuratorFramework cService;
	private ZKWatch watcher;
	private ConcurrentSkipListSet watchers = new ConcurrentSkipListSet();
	
	private String [] monitorPaths ;

	
	
	public void setcService(CuratorFramework cService) {
		this.cService = cService;
	}

	public CuratorFramework getcService() {
		return cService;
	}

	public void setWatcher(ZKWatch watcher) {
		this.watcher = watcher;
	}

	public void setWatchers(ConcurrentSkipListSet watchers) {
		this.watchers = watchers;
	}


	public String[] getMonitorPaths() {
		return monitorPaths;
	}

	public void setMonitorPaths(String[] monitorPaths) {
		this.monitorPaths = monitorPaths;
	}

	public enum ZookeeperWatcherType{
	       GET_DATA,GET_CHILDREN,EXITS,CREATE_ON_NO_EXITS
	    }
	 
	

	/**
	 * 
	 * @param connectionString Connect host/ip and port,eg.'dpsjob103.dev.la.mezimedia.com:2181,dpsjob104.dev.la.mezimedia.com:2181'
	 * @param nameSpace	Service home path.
	 * @param retryTimes	
	 * @param sleepMsBetweenRetries
	 * @param connectionTimeoutMs
	 * @param sessionTimeoutMs
	 * @param monitorPaths
	 */
	
	public ZKService(String connectionString,String nameSpace,String monitorPaths){
		if(StringUtils.isEmpty(connectionString) ){
			LOG.error("ConnectionString or queuePath is empty");
			System.exit(-1);
		}
		this.monitorPaths = monitorPaths.split(",");
		//this.cService = createWithOptions(connectionString,nameSpace, new RetryNTimes(Configure.ZookeeperRetryTimes,Configure.ZookeeperSleepMsBetweenRetries), Configure.ZookeeperConnectionTimeoutMs, Configure.ZookeeperSessionTimeoutMs );
		this.cService = createWithOptions(connectionString,nameSpace, new RetryNTimes(Configure.ZookeeperRetryTimes,Configure.ZookeeperSleepMsBetweenRetries), Configure.ZookeeperConnectionTimeoutMs, Configure.ZookeeperSessionTimeoutMs);
	}
	
	public void initCService(){
		this.cService = createWithOptions(Configure.ZookeeperHosts,"", new RetryNTimes(Configure.ZookeeperRetryTimes,Configure.ZookeeperSleepMsBetweenRetries), Configure.ZookeeperConnectionTimeoutMs, Configure.ZookeeperSessionTimeoutMs );
	}
	
	/**
	 * 
	 */
	public void start(){
		
		this.cService.start();
	//	this.initWatcher();
	//	watcher.dataChangeNotify();
		LOG.info("ZKService start success !");
	}
	
	/**
	 * 
	 */
	public void close(){
		this.cService.close();
		LOG.info("ZKService stop success !");
	}
	
    /**
     * 
     * @param path full path
     * @throws Exception
     */
    public boolean addPath(String path,String value){
    	
    	boolean success = true;
    	if(null == value){
    		value ="";
    	}
    	 try {
    		 Stat stat=  cService.checkExists().usingWatcher(watcher).forPath(path);
    		// if(null == stat){
    		
	    		 BackgroundPathAndBytesable<String> bg= cService.create()//创建一个路径
				 .creatingParentsIfNeeded()//如果指定的节点的父节点不存在，递归创建父节点
				 .withMode(CreateMode.PERSISTENT_SEQUENTIAL)//存储类型（临时的还是持久的）
				 .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE);//访问权限
	    		 
	    		 if(!StringUtils.isEmpty(value)){
	    			 bg.forPath(path,value.getBytes(Charset.forName("utf-8")));//创建的路径
	    		 }else {
	    			 bg.forPath(path);
				}
	    		 LOG.info("Add a new path:"+path);
    		// }else {
			//	LOG.warn(path+" already exist!");
			//}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
	    	success = false;
		}
    	 return success;
    }  
    
    /**
    * 
    * @param path full path
    * @throws Exception
    */
   public boolean addPathEphemeral(String path,String value,boolean delete){
   	
   	boolean success = true;
   	if(null == value){
   		value ="";
   	}
   	 try {
   		 Stat stat=  cService.checkExists().usingWatcher(watcher).forPath(path);
   		 if(null != stat){
   			 if(delete){
   	   			 deletePath(path, false);
   			 }
   		 }
		 BackgroundPathAndBytesable<String> bg= cService.create()//创建一个路径
		 .creatingParentsIfNeeded()//如果指定的节点的父节点不存在，递归创建父节点
		 .withMode(CreateMode.EPHEMERAL)//存储类型（临时的还是持久的）
		 .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE);//访问权限
		 
		 if(!StringUtils.isEmpty(value)){
			 bg.forPath(path,value.getBytes(Charset.forName("utf-8")));//创建的路径
		 }else {
			 bg.forPath(path);
		}
		 LOG.info("Add a new path:"+path);
   		
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
	    	success = false;
		}
   	 return success;
   }  
   
    /**
     * 
     * @param path
     * @param value
     * @return
     */
    public boolean setPathValue(String path,String value){
    	boolean success = true;
		 try {
			Stat stat=  cService.checkExists().forPath(path);
			if(stat!=null){
				stat = cService.setData().forPath(path,value.getBytes(Charset.forName("utf-8")));
				LOG.info("Change path value "+value);
				}else {
					LOG.error(" node no exist ");
				}
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}

    	return success;
    }
    
    /**
     * 
     * @param path
     * @param deleteChildren
     * @return
     */
    public boolean deletePath(String path,boolean deleteChildren){
    	boolean success = true;
    	
    	if(!StringUtils.isEmpty(path) ){
		 try {
			 if(deleteChildren){
				 cService.delete().deletingChildrenIfNeeded().forPath(path);
			 }else {
				 cService.delete().forPath(path);
			 }
			 LOG.info("Delete path:"+path);
			} catch (Exception e) {
				LOG.error(e.getMessage(),e);
		    	success = false;
			}
    	}else {
    		success = false;
			LOG.error("Path should not empty!");
		}
   	
   	 	return success;
   }
   
    /**
     * 
     * @param path
     * @return
     */
    public String getPathValue(String path){
   	 try {
   		cService.sync();
   		// Stat stat=  cService.checkExists().usingWatcher(watcher).forPath(path);
			return new String(cService.getData().usingWatcher(watcher).forPath(path),Charset.forName("utf-8"));
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
   	 return null;
   }
    
    
    /**
     * 
     * @return
     */
    public List<String> getChildrenList(String path){
    	List<String> children = null;
	 	   try {
	 		  cService.sync();
		    	children = cService.getChildren().usingWatcher(watcher).forPath(path);
	      } catch (KeeperException e) {
	          LOG.error(e);
	      } catch (InterruptedException e) {
	          LOG.error(e);
	      } catch (Exception e) {
	          LOG.error(e);
	      }
 	   return children;

    }
    
    /**
     * 
     * @param path
     * @param deep
     * @throws Exception
     */
    public void listTrace(String path,String deep){
	       deep = deep + "	";
	       LOG.info(path);
	       try {
	    	   cService.sync();
	    	   List<String> children = cService.getChildren().usingWatcher(watcher).forPath(path);
	           if(!children.isEmpty()){
	              for(String child: children){
	                  if(!path.equals("/"))
	                     listTrace(path+"/"+child,deep + "  ");
	                  else
	                     listTrace(path+child,deep + "  ");
	              }
	           }
	       } catch (KeeperException e) {
	           LOG.error(e);
	       } catch (InterruptedException e) {
	           LOG.error(e);
	       } catch (Exception e) {
	           LOG.error(e);
		}
	    }
    
    /**
     * 
     * @param connectionString
     * @return
     */
    public static CuratorFramework createSimple(String connectionString)
    {
        // these are reasonable arguments for the ExponentialBackoffRetry. The first
        // retry will wait 1 second - the second will wait up to 2 seconds - the
        // third will wait up to 4 seconds.
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);

        // The simplest way to get a CuratorFramework instance. This will use default values.
        // The only required arguments are the connection string and the retry policy
        return CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
    }

    /**
     * 
     * @param connectionString
     * @param path
     * @param retryPolicy
     * @param connectionTimeoutMs
     * @param sessionTimeoutMs
     * @return
     */
    public static CuratorFramework createWithOptions(String connectionString,String path, RetryPolicy retryPolicy, int connectionTimeoutMs, int sessionTimeoutMs)
    {
        // using the CuratorFrameworkFactory.builder() gives fine grained control
        // over creation options. See the CuratorFrameworkFactory.Builder javadoc
        // details
        return CuratorFrameworkFactory.builder()
            .connectString(connectionString)
            .namespace(path)
            .retryPolicy(retryPolicy)
            .connectionTimeoutMs(connectionTimeoutMs)
            .sessionTimeoutMs(sessionTimeoutMs)
            // etc. etc.
            .build();
    }
    
    
    /**
     * 
     * @param monitorPath 
     */
    public void initWatcher() {
    	for(String path : monitorPaths){
    		cService.sync();
            try {
     	        Stat stat = cService.checkExists().usingWatcher(watcher).forPath(path);
     	        if(stat!=null){
     	        	cService.getChildren().usingWatcher(watcher).forPath(path);
     		    //    zkService.getData().usingWatcher(watcher).forPath(path);
     	        }
            	LOG.info("Add watched path:"+path);
    		} catch (Exception e) {
    			LOG.error(e);
    		}
    	}
    	
       //addReconnectionWatcher(monitorPath,null,watcher);
     }
    
    /**
     * 
     * @param path monitorPath 
    */
    public void initWatcher(String path) {
    	cService.sync();
        try {
        	LOG.info("Add watched path:"+path);
 	        Stat stat = cService.checkExists().usingWatcher(watcher).forPath(path);
 	        if(stat!=null){
 	        	cService.getChildren().usingWatcher(watcher).forPath(path);
 	        	//cService.getData().usingWatcher(watcher).forPath(path);
 	        }
		} catch (Exception e) {
			LOG.error(e);
		}
	}
    	
    // addReconnectionWatcher(monitorPath,null,watcher);
    
    public void addReconnectionWatcher(final String path,final ZookeeperWatcherType watcherType,final CuratorWatcher watcher){
        synchronized (this) {
            if(!watchers.contains(watcher.toString()))//不要添加重复的监听事件
            {
               watchers.add(watcher.toString());
               LOG.info("add new watcher " + watcher);
               cService.getConnectionStateListenable().addListener(new ConnectionStateListener() {  
            	   
                   @Override
                   public void stateChanged(CuratorFramework client, ConnectionState newState) {
                	   LOG.info(newState);
                	   LOG.info(path);
                          try{
                             if(watcherType == ZookeeperWatcherType.EXITS){
                            	 cService.checkExists().usingWatcher(watcher).forPath(path);
                             }else if(watcherType == ZookeeperWatcherType.GET_CHILDREN){
                            	 cService.getChildren().usingWatcher(watcher).forPath(path);
                             }else if(watcherType == ZookeeperWatcherType.GET_DATA){
                            	 cService.getData().usingWatcher(watcher).forPath(path);
                             }else if(watcherType == ZookeeperWatcherType.CREATE_ON_NO_EXITS){
                                 //ephemeral类型的节点session过期了，需要重新创建节点，并且注册监听事件，之后监听事件中，
                                 //会处理create事件，将路径值恢复到先前状态
                                 Stat stat = cService.checkExists().usingWatcher(watcher).forPath(path);                             
                                 if(stat == null){
                                	 cService.create()
                                    .creatingParentsIfNeeded()
                                    .withMode(CreateMode.PERSISTENT)
                                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                                    .forPath(path);                                     
                                 }
                                 addReconnectionWatcher(path, watcherType, watcher);
                                 
                             }
                          }catch (Exception e) {
                             e.printStackTrace();
                          }
                   }
               });          
            }
        }
     }
    
   
    
    
    public String getLiveNode(){
    	String re = null;
    	List<String> nodes = getChildrenList(Configure.ZookeeperLiveNodePath);
    	if(nodes !=null && nodes.size()>0){
        	Random rand = new Random();
        	re = nodes.get(rand.nextInt(nodes.size())).replace("_solr", "");
    	}
    	return re;
    }
    
    public String getLiveNode_feed(){
    	String re = null;
    	String host = Configure.ZookeeperHosts_feed.replace("2181", "8080");
    	String[] nodes = host.split(",");
    	if(nodes !=null && nodes.length>0){
        	Random rand = new Random();
        	re = nodes[rand.nextInt(nodes.length)];
    	}
    	return re;
    }
    
    public static void main(String[] args) throws Exception {
    	
    	Configure.init();
    	//String feed = getLiveNode_feed();
    //	System.out.println(feed);
    	/**
    	List<String> list = new ArrayList<String>();
    	list.add("003");
    	list.add("001");
    	list.add("002");
    	Collections.sort(list);
		for(String l: list){
			System.out.println(l);
		}
    	
    	
    	String json = "{\"data_type\":\"node\",\"action\":\"update\",\"nid\":10000,\"vid\":10000,\"date\":1419323440572}";
    	
    	Gson gson = new Gson();
    	TaskEntity taskEntity = gson.fromJson(json, TaskEntity.class);
    	
    	System.out.println(taskEntity.getAction());
    	**/
    	/**
    	
    	Random rand = new Random();
    	System.out.println(rand.nextInt(3));
    	
    	
    	ZKService client = new ZKService("solrcloud002.la1.vcinv.net:2181,solrcloud001.la1.vcinv.net:2181,solrcloud003.la1.vcinv.net:2181", "configs","");
    	client.start();
    	//String jsonString = client.getPathValue("/aliases.json");
    	//System.out.println(jsonString);
    	/**
    	File file = new File("C:\\Users\\wzou\\Desktop\\schema.xml");
    	FileReader fileReader = new FileReader(file);
    	 BufferedReader br = new BufferedReader (fileReader);
    	  String s;
    	  StringBuffer sb = new StringBuffer("");
    	 while ((  s = br.readLine() )!=null) {
    		 sb.append(s).append("\n");
          }
          client.setPathValue("/myconf/schema.xml", sb.toString());
           **/
         
    	//System.out.println( client.getPathValue("/myconf/schema.xml"));
          
    //	fileInputStream.close();
    	//System.out.println(fileInputStream.toString());
    	//StringBuffer sb = new String(bytes)
    //	System.out.println(sb.toString());
    //	System.out.println(client.getPathValue("/myconf/schema.xml"));

    //	client.listTrace("task_queue", "1");
    //	client.addPath("task_queue/test", "");
    //	client.listTrace("task_queue", "1");
    //	client.addPath(path, value);
    //	client.start();
    	//client.initWatcher();
    	//Thread.sleep(2000);
    	//client.addPath("test", "test");

    	//client.setPathValue("test", "value5");
    	//System.out.println(client.getPathValue("test"));
    	//CuratorFramework curatorService = client.getZkService();
    //	client.addPath("market_suggestion/db_config/driver", "com.mysql.jdbc.Driver");
    //	client.addPath("market_suggestion/db_config/url", "jdbc:mysql://cmsfdb101.beta.la.mezimedia.com/investopedia_market_simulator?useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8&zeroDateTimeBehavior=round");
   //	client.addPath("market_suggestion/db_config/user", "dt_cms");
    	//client.addPath("market_suggestion/db_config/pwd", "dtCms23!");
    			
    	// client.deletePath("test", false);
    	//client.setPathValue("market_suggestion/db_config/pwd", "dtCms23");
    	//System.out.println(client.getPathValue("test"));
    //	client.listTrace("", "2");
    	
    	//ZKWatch zkWatch =new ZKWatch("zk/config/inv_backend/market_simulator/db_config/db.url", curatorService);
    	//client.setPathValue("zk/config/inv_backend/market_simulator/db_config/db.url", "1");
//    		Thread.sleep(3000000);

    }
    
    
    
}
