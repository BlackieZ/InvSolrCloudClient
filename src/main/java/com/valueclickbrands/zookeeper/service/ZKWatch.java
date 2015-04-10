package com.valueclickbrands.zookeeper.service;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.RetryNTimes;
import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import com.valueclickbrands.solr.service.ServiceMain;
import com.valueclickbrands.solr.service.ZKService;
import com.valueclickbrands.solr.util.Configure;

/** 
 * @author Vanson Zou
 * @date Jul 4, 2014 
 */

public  class ZKWatch implements CuratorWatcher{
	static Logger LOG = Logger.getLogger(ZKWatch.class);

	private ZKService client ;
	private ServiceMain serviceMain;
	
	public void setServiceMain(ServiceMain serviceMain) {
		this.serviceMain = serviceMain;
	}

	AtomicInteger seq = new AtomicInteger();
     
       public ZKWatch(ZKService cliKclient) {
           this.client = cliKclient;
       }
       
       @Override
       public void process(WatchedEvent event) {
       
   		if ( event==null ) {
   			return;
   		}
   		// connection stat
   		KeeperState keeperState = event.getState();
   		
   		EventType eventType = event.getType();
   		
   		String path = event.getPath();
   		
   		String logPrefix = "【Watcher-" + this.seq.incrementAndGet() + "】";

   		if ( KeeperState.SyncConnected == keeperState ) {
   			// 成功连接上ZK服务器
   			if ( EventType.None == eventType ) {
   				LOG.info( logPrefix + "Connect Server Success!" );
   			//	connectedSemaphore.countDown();
   			} else if ( EventType.NodeCreated == eventType ) {
   				LOG.info( logPrefix + "Node Created:\t"+ path);
   			
   			} else if ( EventType.NodeDataChanged == eventType ) {
   				LOG.info( logPrefix + "Node Data Changed--"+client.getPathValue(path) );
   				
   			} else if ( EventType.NodeChildrenChanged == eventType ) {
   				LOG.info( logPrefix + "Queue list change" ) ;
   				try {
   					serviceMain.dataChangeNotify();
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error(e.getMessage());
				}
   			} else if ( EventType.NodeDeleted == eventType ) {
   				LOG.info( logPrefix +path+ "Node  is deleted" ) ;
   			}
   			
   		} else if ( KeeperState.Disconnected == keeperState ) {
   			LOG.info( logPrefix + "ZK disconnected from the server" );
   		} else if ( KeeperState.AuthFailed == keeperState ) {
   			LOG.info( logPrefix + "Authentication failed" );
   		} else if ( KeeperState.Expired == keeperState ) {
   			LOG.info( logPrefix + "Session expired" );
   			client.initCService();
   			client.initWatcher();
   		}	

       }
       
      
}
