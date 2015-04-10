package com.valueclickbrands.solr;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
//		 ZooKeeper zk = new ZooKeeper("solrcloud001.la1.vcinv.net:2181,solrcloud002.la1.vcinv.net:2181,solrcloud003.la1.vcinv.net:2181", 
		ZooKeeper zk = new ZooKeeper("api101.la1.vcinv.net:2181,api102.la1.vcinv.net:2181,api103.la1.vcinv.net:2181",
		        20000, new Watcher() { 
			 		@Override
		            public void process(WatchedEvent event) { 
		                System.out.println("watch start" + event.getType() + "..."); 
		            }
		        });
//		 zk.create("/testRootPath", "testRootData".getBytes(), Ids.OPEN_ACL_UNSAFE,
//		   CreateMode.PERSISTENT);

//		 zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(),
//		   Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT); 
//		 System.out.println(new String(zk.getData("/testRootPath",false,null))); 

//		 System.out.println(zk.getChildren("/testRootPath",true));
//
//		 zk.setData("/testRootPath/testChildPathOne","modifyChildDataOne".getBytes(),-1); 
//		 System.out.println("zk.exists["+zk.exists("/testRootPath",true)+"]"); 
//
//		 zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(), 
//		   Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT); 
//		 System.out.println(new String(zk.getData("/testRootPath/testChildPathTwo",true,null))); 

//		 zk.delete("/testRootPath/testChildPathTwo",-1);
//		 zk.delete("/testRootPath/testChildPathOne",-1);

//		 zk.delete("/testRootPath",-1);
		 
		 zk.create("/solrcloud/task_processtime", String.valueOf(System.currentTimeMillis()).getBytes(), Ids.OPEN_ACL_UNSAFE,
		   CreateMode.PERSISTENT);

		 zk.close();
	}

}
