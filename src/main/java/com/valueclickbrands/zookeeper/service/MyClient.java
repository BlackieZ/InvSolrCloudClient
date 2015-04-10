package com.valueclickbrands.zookeeper.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.valueclickbrands.solr.service.ZKService;
import com.valueclickbrands.solr.util.Configure;

/** 
 * @author Vanson Zou
 * @date Jul 11, 2014 
 */

public class MyClient extends ZKService{
	static Logger LOG = Logger.getLogger(MyClient.class);

	public static void main(String[] args) throws InterruptedException, IOException {
		Configure.init();
		ZKService client = new MyClient();
		client.start();

	//	File file = new File("C:\\Users\\wzou\\workspace\\InvSolrCloudClient\\src\\main\\source\\SolrCloudConfig\\schema.xml");
//		File file = new File("C:\\Users\\wzou\\workspace\\InvSolrCloudClient\\src\\main\\source\\SolrCloudConfig\\schema_feed.xml");
//		File file = new File("C:\\Users\\wzou\\workspace\\InvSolrCloudClient\\src\\main\\source\\SolrCloudConfig\\schema_search.xml");
	File file = new File("C:\\Users\\wzou\\workspace\\InvSolrCloudClient\\src\\main\\source\\SolrCloudConfig\\synonyms.txt");
//		File file = new File("C:\\Users\\wzou\\workspace\\InvSolrCloudClient\\src\\main\\source\\SolrCloudConfig\\stopwords_en.txt");
	//	File file = new File("C:\\Users\\wzou\\workspace\\InvSolrCloudClient\\src\\main\\source\\SolrCloudConfig\\protwords.txt");
		//File file = new File("C:\\Users\\wzou\\workspace\\InvSolrCloudClient\\src\\main\\source\\SolrCloudConfig\\solrconfig.xml");
		// File file = new File("C:\\Users\\wzou\\Desktop\\solrconfig.xml");
		//File file = new File("C:\\Users\\wzou\\Desktop\\protwords.txt");
    	FileReader fileReader = new FileReader(file);
    	 BufferedReader br = new BufferedReader (fileReader);
    	  String s;
    	  StringBuffer sb = new StringBuffer("");
    	 while ((  s = br.readLine() )!=null) {
    		 sb.append(s).append("\n");
          }
    //	client.addPathEphemeral("/solrcloud/live_dump_host/001", "");
   	//Thread.sleep(300000);
    //	 client.deletePath("/solrcloud/livedumphost", false);
//     	client.setPathValue("/configs/myconf/schema.xml", sb.toString());
  //	client.setPathValue("/configs/myconf/schema.xml", sb.toString());
 //   	client.setPathValue("/configs/myconf/schema_search.xml", sb.toString());
  	//client.setPathValue("/configs/myconf/solrconfig.xml", sb.toString());
     	//client.setPathValue("/configs/myconf/protwords.txt", sb.toString());
//client.setPathValue("/configs/myconf/synonyms.txt", sb.toString());
//    	client.setPathValue("/configs/myconf/stopwords.txt", sb.toString());
  //  	client.setPathValue("/configs/myconf/protwords.txt", sb.toString());
    //	client.addPath("/configs/myconf/schema_search.xml", sb.toString());
   //  	client.deletePath("/configs/myconf/schema_search.xml0000000022", false);
 	//client.deletePath("/solrcloud/task_queue", true);
 	//List<String> list = client.getChildrenList("/solrcloud/task_queue");
 	//System.out.println(list.size());
    	// client.setPathValue("/solrcloud/task_processtime", String.valueOf(System.currentTimeMillis()));
  // 	 client.addPath("/solrcloud/task_queue/task_", "{\"data_type\":\"node\",\"action\":\"update\",\"nid\":144691,\"vid\":162442,\"date\":1419323440572}");
    	
    //	client.addPath("/solrcloud/task_queue/task_", "{\"data_type\":\"taxonomy\",\"action\":\"add\",\"url\":\"/markets/stocks/baaaaa.to/\",\"template_pid\":\"18066\",\"date\":1420787737}");
   // 	 client.addPath("/solrcloud/task_queue/task_", "{\"data_type\":\"taxonomy\",\"action\":\"add\",\"url\":\"/contributors/test\",\"template_pid\":\"18066\",\"date\":1420787737}");
//client.addPath("/solrcloud/task_queue/task_", "{\"data_type\":\"all\",\"action\":\"fully\",\"nid\":10000,\"vid\":10000,\"date\":1419323440572,\"istree\":\"true\", \"tree_action\":\"fully\"}");
client.addPath("/solrcloud/task_queue/task_", "{\"data_type\":\"node\",\"action\":\"update\",\"nid\":19828,\"vid\":19828,\"date\":1419323440572}");

    //	 List<String> list = client.getChildrenList("/solrcloud");
    //	 	for(String ss:list){
    //	 		System.out.println(ss);
    //	 	}
	//   client.deletePath("/solrcloud/test0000000004", false);
	    //	client.listTrace("task_queue", "1");
		//client.addPath("/task_queue/task_", "{\"data_type\":\"node\",\"action\":\"delete\",\"nid\":1,\"vid\":1,\"date\":1419323440572}");
	  // 	client.start();
	}
	
	MyClient(){
	//super("10.16.3.29:2181,10.16.3.30:2181,10.16.3.58:2181","","");//prod
		super("192.168.214.88:2181,192.168.214.96:2181,192.168.214.89:2181","","");//staging
//		super("10.16.3.60:2181,10.16.3.61:2181,10.16.3.62:2181","","");//solr feed 

//		super("10.96.252.200:2181,10.96.252.203:2181,10.96.252.206:2181","","");//IAD STAGING
	}

	
}
