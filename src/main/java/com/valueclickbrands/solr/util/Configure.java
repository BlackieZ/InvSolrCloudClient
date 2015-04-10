package com.valueclickbrands.solr.util;

import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.resource.Resource;

/** 
 * @author Vanson Zou
 * @date Aug 7, 2013 
 */

public class Configure {
	public static Logger logger  = Logger.getLogger(Configure.class);

	public static String JobReloadSymbolDate;
	public static String HttpConnectionTimedOut;
	public static String HttpRetryTimes;
	public static String SolrCollectionUrlCreatealiasUrl;
	public static String SolrCollectionNodeAlias;
	public static String SolrCollectionNodeSearchAlias;
	public static String SolrCollectionTaxonomyAlias;
	public static String ZookeeperHosts;
	public static String ZookeeperCollectionAliasesPath;
	public static String ZookeeperLiveNodePath;
	public static String ZookeeperQueuePath;
	public static int ZookeeperRetryTimes;
	public static int ZookeeperSleepMsBetweenRetries;
	public static int ZookeeperConnectionTimeoutMs;
	public static int ZookeeperSessionTimeoutMs;
	public static String ZkTaskProcessTimePath;
	public static int MonitorAllowCount;
	public static int MonitorZkTaskTimeOut;
	public static int MonitorZkGetTaskSleepTime;
	public static String MonitorStopServerShellPath;
	public static String MonitorStartServerShellPath;
	public static String MonitorServerPidFilePath;
	public static String zookeeper_live_dump_node_path;
	public static String dump_node_name;
	
	public static String ZookeeperHosts_feed;
	public static String SolrCollectionNodeAlias_feed;

	//mail
	public static String mail_smtp_host;
	public static String mail_auth;
	public static String mail_user;
	public static String mail_password;
	public static String mail_from;
	public static String mail_to;
	public static String mail_cc;
	public static String mail_template;
	public static String template_path;
	
	public static String ZookeeperTaskProcessTimePath;
	public static Resource fileserver_config = Resource.newClassPathResource("config.properties");
	public static String imagesPath;
	public static String imageUrl;
	public static String index_type_feed="solr_feed";
	public static String index_type_api="solr_api";
	public static String index_type_search="solr_search";
	public static String index_type_all="solr_all";

	public static void init(){
		Properties prop = new Properties();
		try {
			prop.load(fileserver_config.getInputStream());

			Configure.JobReloadSymbolDate = prop.getProperty("fetch.job.fetch.date");
			Configure.SolrCollectionUrlCreatealiasUrl = prop.getProperty("solr.collection.url.createalias.url");
			Configure.SolrCollectionNodeAlias = prop.getProperty("solr.collection.alias.node");
			Configure.SolrCollectionTaxonomyAlias = prop.getProperty("solr.collection.alias.taxonomy");
			Configure.ZookeeperHosts = prop.getProperty("zookeeper.hosts");
			Configure.ZookeeperCollectionAliasesPath = prop.getProperty("zookeeper.collection.aliases.path");
			Configure.ZookeeperLiveNodePath = prop.getProperty("zookeeper.live.node.path");
			Configure.ZookeeperQueuePath = prop.getProperty("zookeeper.queue.path");
			Configure.ZookeeperRetryTimes = Integer.valueOf(prop.getProperty("zookeeper.retrytimes"));
			Configure.ZookeeperSleepMsBetweenRetries = Integer.valueOf(prop.getProperty("zookeeper.betweenretries.sleepms"));
			Configure.ZookeeperConnectionTimeoutMs = Integer.valueOf(prop.getProperty("zookeeper.connectiontimeoutms"));
			Configure.ZookeeperSessionTimeoutMs = Integer.valueOf(prop.getProperty("zookeeper.sessiontimeoutms"));
			Configure.SolrCollectionNodeSearchAlias = prop.getProperty("solr.collection.node.alias.search");
			ZkTaskProcessTimePath = prop.getProperty("zookeeper.task.processtime.path");
			MonitorAllowCount = Integer.valueOf(prop.getProperty("monitor.zk.task.queue.allow.count"));
			MonitorZkTaskTimeOut = Integer.valueOf(prop.getProperty("monitor.zk.task.timeOut"));
			MonitorZkGetTaskSleepTime = Integer.valueOf(prop.getProperty("monitor.zk.getTask.sleep.time"));
			MonitorStopServerShellPath = prop.getProperty("monitor.stopServer.shell.path");
			MonitorStartServerShellPath = prop.getProperty("monitor.startServer.shell.path");
			MonitorServerPidFilePath = prop.getProperty("monitor.server.pid.file.path");
			zookeeper_live_dump_node_path = prop.getProperty("zookeeper.live.dump.node.path");
			dump_node_name = prop.getProperty("dump.node.name");
			//mail
			mail_smtp_host = prop.getProperty("mail.smtp.host");
			mail_auth = prop.getProperty("mail.password.auth");
			mail_user = prop.getProperty("mail.user");
			mail_password = prop.getProperty("mail.password");
			mail_from = prop.getProperty("mail.from");
			mail_to = prop.getProperty("mail.to");
			mail_cc = prop.getProperty("mail.cc");
			mail_template = prop.getProperty("template.email");
			template_path = prop.getProperty("template.path");
			Configure.ZookeeperTaskProcessTimePath = prop.getProperty("zookeeper.task.process.time.path");
			//feed
			Configure.ZookeeperHosts_feed = prop.getProperty("zookeeper.hosts.feed");
			Configure.SolrCollectionNodeAlias_feed = prop.getProperty("solr.collection.node.alias.feed");
			Configure.imagesPath = prop.getProperty("inv.images.path");
			Configure.imageUrl = prop.getProperty("inv.images.url");

			
		
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		logger.info("init config success ");

	}
	public static void main(String[] args) {
		Configure.init();
		System.out.println(Configure.ZookeeperTaskProcessTimePath);
	}
	
}
