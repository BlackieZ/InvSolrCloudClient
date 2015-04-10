package com.valueclickbrands.solr.job;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.valueclickbrands.solr.dao.DumpDao;
import com.valueclickbrands.solr.model.TagGroup;
import com.valueclickbrands.solr.model.TaskEntity;
import com.valueclickbrands.solr.service.ServiceMain;
import com.valueclickbrands.solr.service.TaskService;
import com.valueclickbrands.solr.service.TaxonomyService;
import com.valueclickbrands.solr.service.ZKService;
import com.valueclickbrands.solr.util.CacheUtil;
import com.valueclickbrands.solr.util.Configure;

public class IndexFullyJob {
	private static Logger logger = Logger.getLogger(IndexFullyJob.class);
	private static final ReentrantLock IndexFullyJob = new ReentrantLock();

	private ZKService zkService;
	private TaskService taskService;
	
	public ZKService getZkService() {
		return zkService;
	}

	public void setZkService(ZKService zkService) {
		this.zkService = zkService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public void execute() {
		if (IndexFullyJob.tryLock()) {
			try {
				Boolean work = taskService.checkCurrentNodeIfWork();
				if(work){
					logger.info("Add IndexFullyJob start.");
					long startTime = System.currentTimeMillis();
					String queuePath = Configure.ZookeeperQueuePath;
					//String data = "{\"data_type\":\"node\",\"action\":\"update\",\"nid\":10000,\"vid\":10000,\"date\":1419323440572}";
					
					TaskEntity taskEntity = new TaskEntity(0, "all", "fully", 0, 0, System.currentTimeMillis(),false,"");
					Gson gson = new Gson();
					String data = gson.toJson(taskEntity);
					zkService.addPath(queuePath+"/task_", data);
					logger.info("IndexFullyJob end. exe time:" +(System.currentTimeMillis()-startTime)+",data:"+queuePath+":"+data );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				IndexFullyJob.unlock();
			}
		}else {
			logger.info("IndexFullyJob is already excuting!");

		}

	}
	public static void main(String[] args) {
		IndexFullyJob indexFullyJob = new IndexFullyJob();
		indexFullyJob.execute();
	}

}
