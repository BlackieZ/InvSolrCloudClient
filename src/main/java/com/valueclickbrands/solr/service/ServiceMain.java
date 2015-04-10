package com.valueclickbrands.solr.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;

import com.valueclickbrands.solr.util.Configure;
import com.valueclickbrands.zookeeper.service.ZKWatch;

/** 
 * @author Vanson Zou
 * @date Dec 15, 2014 
 */

public class ServiceMain extends ZKWatch{      
	
	public ServiceMain(ZKService cliKclient) {
		super(cliKclient);
	}

	private static Logger logger = Logger.getLogger(TaskService.class);
	private ExecutorService  pool = Executors.newFixedThreadPool(1); 
	private TaskService taskService;
	public static long sleepTime = 2000;
	private static int  count=0;
	
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}
	
	public TaskService getTaskService() {
		return taskService;
	}

	/**
	public void start(){
		
		logger.info("Zookeeper queue monitor start");
		pool = Executors.newFixedThreadPool(threadNum);
		pool.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					 int count = taskService.getTaskCount();
					 if(count>0){
						TaskEntity taskEntity = taskService.getFirstTask();
						 Future<String> future  = pool.submit(new ThreadService(taskEntity,taskService));
							if(null != future){
								try {
									if(future.get().equalsIgnoreCase("success")){
										boolean delete = taskService.removeTask(taskEntity.getId());
										if(delete){
											logger.info("Job proccess done ");
										}
									}
								} catch (InterruptedException e) {
									e.printStackTrace();
								} catch (ExecutionException e) {
									e.printStackTrace();
								}
							}
					 }else {
						try {
							Thread.sleep(sleepTime);
							logger.info(" sleep "+sleepTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					 
			  }
			}
		});
			
	}
	**/
	public List<String> getTasks(){
		List<String> list = new ArrayList<String>();
		
		return list;
	}
	
	public void dataChangeNotify() {
		Boolean work = taskService.checkCurrentNodeIfWork();
		if(work){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
			pool.submit(new ThreadService(taskService));
			logger.info("Submit a task ,Node:"+Configure.dump_node_name);
		}else{
			logger.info("Node :"+Configure.dump_node_name+" on standby");
			taskService.getZkService().initWatcher(Configure.ZookeeperQueuePath);
		}
			
	}
	
	

	public static void main(String[] args) {
		
		ZKService client = new ZKService("solrcloud002.la1.vcinv.net:2181,solrcloud001.la1.vcinv.net:2181,solrcloud003.la1.vcinv.net:2181","","/solrcloud/task_queue");
		client.start();
		try {
			Thread.sleep(10000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

	
