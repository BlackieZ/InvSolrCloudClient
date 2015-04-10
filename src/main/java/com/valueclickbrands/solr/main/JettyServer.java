package com.valueclickbrands.solr.main;

import java.text.ParseException;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.StatisticsServlet;
import org.eclipse.jetty.servlets.GzipFilter;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.quartz.SchedulerException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.valueclickbrands.solr.util.DateUtil;
import com.valueclickbrands.solr.util.QuartzManager;
import com.valueclickbrands.solr.job.PollingMonitorZkQueue;
import com.valueclickbrands.solr.service.DumpService;
import com.valueclickbrands.solr.service.IndexService;
import com.valueclickbrands.solr.service.ServiceMain;
import com.valueclickbrands.solr.service.TaskService;
import com.valueclickbrands.solr.service.TaxonomyService;
import com.valueclickbrands.solr.service.ZKService;
import com.valueclickbrands.solr.servlet.TestServlet;
import com.valueclickbrands.solr.util.Configure;
import com.valueclickbrands.zookeeper.service.ZKWatch;

public  class JettyServer{
	private static Logger logger = Logger.getLogger(JettyServer.class);
	private Server server;
	private int port = 8080;
	private int queueSize = 100;
	protected ClassPathXmlApplicationContext content;
	private String logPath = "";
	private static final int DEFAULT_PORT = 8081;
	private static ZKService zkService; 
	public JettyServer(int port, int queueSize) {
		this.port = port;
		this.queueSize = queueSize;
		init();
	}
	
	public JettyServer(int port, int queueSize,String logPath) {
		this.port = port;
		this.queueSize = queueSize;
		this.logPath = logPath;
		init();
		//SearchService search = (SearchService)content.getBean("SearchService");
		//search.initCache();
	}
	

	
	public void init(){
		content = new ClassPathXmlApplicationContext(getSpringConfig());// init spring
		zkService = (ZKService)content.getBean("zkService");
		
		server = new Server();
		SelectChannelConnector connector=new SelectChannelConnector();
        connector.setPort(port);
        connector.setStatsOn(true);
        connector.setThreadPool(new QueuedThreadPool(queueSize));
        server.addConnector(connector);
		ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");
		NCSARequestLog reqLog = new NCSARequestLog();
		reqLog.setFilename(logPath+"request.log.yyyy_mm_dd");
		reqLog.setFilenameDateFormat("yyyy_MM_dd");
		reqLog.setRetainDays(90);
		reqLog.setAppend(true);
		reqLog.setExtended(false);
		reqLog.setLogCookies(false);
		reqLog.setLogLatency(true);
		reqLog.setLogTimeZone("GMT");
		RequestLogHandler reqLogHeader = new RequestLogHandler();
		reqLogHeader.setRequestLog(reqLog);
		DefaultHandler defaultHandler = new DefaultHandler();
		Handler[] handlers = {context, defaultHandler, reqLogHeader};
		HandlerCollection handlerCollection = new HandlerCollection();
        handlerCollection.setHandlers(handlers);
        
        StatisticsHandler stats = new StatisticsHandler();
        stats.setHandler(handlerCollection);
        server.setHandler(stats);
		
		addServlet(context);
		
		ServiceMain serviceMain = (ServiceMain)content.getBean("serviceMain");
		
		ZKWatch watcher = (ZKWatch)content.getBean("watcher");
        
        zkService.setWatcher(watcher);
        zkService.start();
        zkService.addPathEphemeral(Configure.zookeeper_live_dump_node_path+"/"+Configure.dump_node_name,  DateUtil.currentTimeStampToString(System.currentTimeMillis()),true);
        serviceMain.dataChangeNotify();
	}
	
	public void zkStart(){
		System.out.println("sss");
		zkService.start();
	}

	public void startup() throws Exception {
		server.start();
		server.join();
	}

	public void shutdown() throws Exception {
		server.stop();
	}
	
	public String getServicePath(){
		return "/search";
	}
	
	public String getSpringConfig() {
		return "classpath:applicationContext-task.xml";
	}

	public String getSearchSerletBeanId() {
		return "SearchServlet";
	}

	public void addServlet(ServletContextHandler context) {
		EnumSet<DispatcherType> all = EnumSet.of(DispatcherType.ASYNC, DispatcherType.ERROR, DispatcherType.FORWARD,
	            DispatcherType.INCLUDE, DispatcherType.REQUEST);
		FilterHolder gzipFilter = new FilterHolder(new GzipFilter()); 
		gzipFilter.setInitParameter("mimeTypes","text/html,text/xml,application/json"); //text/html,text/plain,application/xhtml+xml,text/css,application/javascript,image/svg+xml,text/zip,text/gzip,
		gzipFilter.setInitParameter("minGzipSize", "0");
		context.addFilter(gzipFilter, "/*", all);
		
		
		StatisticsServlet statServlet = new StatisticsServlet();
		ServletHolder statSH = new ServletHolder(statServlet);
		statSH.setInitParameter("restrictToLocalhost", "false");
		context.addServlet(statSH, "/stat");
		
		TestServlet testServlet= new TestServlet();
		DumpService dumpService = (DumpService)content.getBean("DumpService");
		testServlet.setDumpService(dumpService);
		TaxonomyService taxonomyService = (TaxonomyService)content.getBean("TaxonomyService");
		testServlet.setTaxonomyService(taxonomyService);
		IndexService indexService = (IndexService)content.getBean("IndexService");
		indexService.setZkService(zkService);
		testServlet.setIndexService(indexService);
		ServletHolder testServletH = new ServletHolder(testServlet);
		context.addServlet(testServletH, "/addIndex");
		
	}

	public static void main(String[] args) {
		int port = DEFAULT_PORT;
		String logPath = "";
		if(args.length==2){
			try{
				port = Integer.parseInt(args[0]);
			}catch(Exception e){
				port = DEFAULT_PORT;
				System.out.println("port is error,using default port "+DEFAULT_PORT);
			}
			logPath = args[1]; 
		}else if(args.length==1){
			try{
				port = Integer.parseInt(args[0]);
			}catch(Exception e){
				port = DEFAULT_PORT;
				System.out.println("port is error,using default port "+DEFAULT_PORT);
			}
		}else{
			System.out.println("Using default port "+DEFAULT_PORT);
		}
		
		JettyServer server = new JettyServer(port,100,logPath);
		
		try {
			server.startup();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
