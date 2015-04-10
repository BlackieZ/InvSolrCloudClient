package com.valueclickbrands.solr.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import org.apache.log4j.Logger;
import com.google.gson.Gson;
import com.opensymphony.oscache.util.StringUtil;
import com.valueclickbrands.solr.model.TaskEntity;
import com.valueclickbrands.solr.util.Configure;
import com.valueclickbrands.solr.util.DateUtil;

/** 
 * @author Vanson Zou
 * @date Oct 11, 2014 
 */

public class TaskService {
	private static Logger logger = Logger.getLogger(TaskService.class);
	private int threadNum =1;
	private ExecutorService  pool; 
	private ZKService zkService;
	private IndexService indexService;
	private TaxonomyService taxonomyService;
	private Properties prop = new Properties();
	public static int count=3;

	
	public void setZkService(ZKService zkService) {
		this.zkService = zkService;
	}
	
	public ZKService getZkService() {
		return zkService;
	}

	public IndexService getIndexService() {
		return indexService;
	}

	public void setIndexService(IndexService indexService) {
		this.indexService = indexService;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	
	public enum Action {
	       ADD("add") , UPDATE("update") , DELETE("delete") , FULLY("fully") ;
	       private String nCode ;
	       
	       public String getNCode(){
	    	   return nCode;
	       }
	       private Action(String nCode){
	    	   this.nCode = nCode;
	       }
		}
	
	public static enum TreeAction {
	       ADD("add") , UPDATE("update") , DELETE("delete") , FULLY("fully") ;
	       private String nCode ;
	       
	       public String getNCode(){
	    	   return nCode;
	       }
	       private TreeAction(String nCode){
	    	   this.nCode = nCode;
	       }
		}
	public enum DataType{
	       NODE("node"),TAX("taxonomy"),ALL("all");;
	       private String nCode ;
	       
	       public String getNCode(){
	    	   return nCode;
	       }
	       private DataType(String nCode){
	    	   this.nCode = nCode;
	       }
		}
	
	

	public boolean run(TaskEntity job){
		String actionType = job.getAction();
		int nid = job.getNid();
		int vid = job.getVid();
		boolean isTree = job.isIstree();
		String treeAction = job.getTree_action();
		boolean success = false;
		int branch_nid = job.getBranch_nid();
		if(DataType.NODE.nCode.equalsIgnoreCase(job.getData_type())){
			if(Action.ADD.nCode.equalsIgnoreCase(actionType)){
				success = indexService.createIndexIncrementalForNode(nid, vid,treeAction);
				success = indexService.createNodeIndexIncrementalForTaxonomy(nid, vid, treeAction);
				if(branch_nid>0){
					success = indexService.deleteIndex(branch_nid);
				}
			}else if (Action.DELETE.nCode.equalsIgnoreCase(actionType)) {
				success = indexService.deleteIndex(nid);
			}else if (Action.UPDATE.nCode.equalsIgnoreCase(actionType)) {
				success = indexService.createIndexIncrementalForNode(nid, vid,treeAction);
				success = indexService.createNodeIndexIncrementalForTaxonomy(nid, vid, treeAction);

				if(branch_nid>0){
					success = indexService.deleteIndex(branch_nid);
				}
			}else if (Action.FULLY.nCode.equalsIgnoreCase(actionType)) {
				String collection = indexService.createIndexFullyForNode(true);
				if(collection !=null){
					success = true;
				}
			}
		}else if (DataType.TAX.nCode.equalsIgnoreCase(job.getData_type())) {
			
			if(Action.ADD.nCode.equalsIgnoreCase(actionType)){
				success = indexService.createIndexIncrementalForTaxonomy(job.getTemplate_pid(), job.getUrl());
			}else if (Action.DELETE.nCode.equalsIgnoreCase(actionType)) {
				
			}else if (Action.UPDATE.nCode.equalsIgnoreCase(actionType)) {
				
			}else if (Action.FULLY.nCode.equalsIgnoreCase(actionType)) {
				String collection = indexService.createIndexFullyForTaxonomy(true);
				if(collection != null){
					success = true;
				}
			}
		}else if(DataType.ALL.nCode.equalsIgnoreCase(job.getData_type())) {
			if(Action.FULLY.nCode.equalsIgnoreCase(actionType)){
				success = indexService.createIndexFullyForAll(true);
			}
		}
		return success;
		
	}
		
	public List<String> getTasks(){
		List<String> list = new ArrayList<String>();
		return list;
	}
	
	public int getTaskCount(){
		List<String> list = zkService.getChildrenList(Configure.ZookeeperQueuePath);
		return list!=null?list.size():0;
	}
	
	public TaskEntity getFirstTask() {
		List<String> list = zkService.getChildrenList(Configure.ZookeeperQueuePath);
		long min = Long.MAX_VALUE;
		if(list!=null){
			for(String taskName:list){
				long n = 0;
				try {
					n = Long.parseLong(taskName.substring(5));
				} catch (Exception e) {
					logger.error("invalid task");
					continue;
				}
	            if (min > n) {
	                min = n;
	            }
	            
			}
		}
		TaskEntity taskEntity =null;

		if(min>0){

			StringBuffer taskName = new StringBuffer("");
			int count = String.valueOf(min).length();
			if(count<10){
				for(int i=0;i<10-count;i++){
					taskName.append("0");
				}
				taskName.append(String.valueOf(min));
			}
			if(!StringUtil.isEmpty(taskName.toString())){
				try {
					String data = zkService.getPathValue(Configure.ZookeeperQueuePath+"/task_"+taskName);
					Gson gson = new Gson();
			    	taskEntity = gson.fromJson(data, TaskEntity.class);
			    	
				} catch (Exception e) {
					logger.error(" get task failed, task_name:task_"+taskName+" error :"+e.getMessage());
				}
			}
			
			
			if(taskEntity == null){
	    		taskEntity = new TaskEntity();
	    		taskEntity.setInvalid(true);
	    	}
    		taskEntity.setTaskName(taskName.toString());

		}
		
		
    	return taskEntity;
    	

	}
	
	public List<TaskEntity> getTaskList() {
		setProcessTime();
		List<String> list = zkService.getChildrenList(Configure.ZookeeperQueuePath);
		List<TaskEntity> taskList = new ArrayList<TaskEntity>();
		if (list != null && list.size() > 0) {
			Collections.sort(list);
			for (String taskName:list) {
				TaskEntity taskEntity = null;
				String data = null;
				if(!StringUtil.isEmpty(taskName.toString())){
					try {
						data = zkService.getPathValue(Configure.ZookeeperQueuePath+"/"+taskName);
						Gson gson = new Gson();
				    	taskEntity = gson.fromJson(data, TaskEntity.class);
				    	
				    	
					} catch (Exception e) {
						logger.error(" get task failed, task_name:task_"+taskName+" error :"+e.getMessage());
					}
				}
				
				if(taskEntity == null){
		    		taskEntity = new TaskEntity();
		    		taskEntity.setInvalid(true);
		    	}
	    		taskEntity.setJsonData(data);
				taskEntity.setTaskName(taskName);
				taskList.add(taskEntity);
				break;
			}
		}
		
    	return taskList;
	}
	
	public void setProcessTime(){
		try {
			zkService.setPathValue(Configure.ZookeeperTaskProcessTimePath,String.valueOf(System.currentTimeMillis()));
		} catch (Exception e) {
			logger.info("setProcessTime error:"+e.getMessage());
		}
	}
	

	public boolean removeTask(String taskName) {
		setProcessTime();
		boolean result = false;
		try {
			result = zkService.deletePath(Configure.ZookeeperQueuePath+"/"+taskName, false);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return result;
	}
	/**
	 * 
	 * @return
	 */
	public boolean checkCurrentNodeIfWork(){
		boolean re =  false;
		String currentNodeName = Configure.dump_node_name;
		List<String> list = zkService.getChildrenList(Configure.zookeeper_live_dump_node_path);
		Collections.sort(list);
		if(list == null || list.size()==0){
			return false;
		}
		if(!list.contains(currentNodeName)){
	        zkService.addPathEphemeral(Configure.zookeeper_live_dump_node_path+"/"+currentNodeName, DateUtil.currentTimeStampToString(System.currentTimeMillis()),false);
		}else if(list.get(0).equals(currentNodeName)){
			re = true;
		}
		
		return re;
	}
	
	public static void main(String[] args) {
		System.out.println(DateUtil.currentTimeStampToString(System.currentTimeMillis()));;

	}
}

class ThreadService implements Runnable{
	private static Logger logger = Logger.getLogger(ThreadService.class);
	TaskService taskService;
	public ThreadService(TaskService taskService ){
		this.taskService= taskService;
	}

	@Override
	public void run() {
			logger.info("Task to proccess begin");
			long start = System.currentTimeMillis();
			List<TaskEntity> taskList = taskService.getTaskList();
			boolean watchFlag = true;
			if (taskList != null && taskList.size() > 0) {
				int i = 0;
				for (TaskEntity t:taskList) {
					i++;
					boolean success = false;
					boolean delete =false;
					
					try {
						if(!StringUtil.isEmpty(t.getTaskName())){
							delete = taskService.removeTask(t.getTaskName());
						}
						if(delete){
							if (i == taskList.size()) {
								taskService.getZkService().initWatcher(Configure.ZookeeperQueuePath);
								watchFlag = false;
							}
							if(!t.isInvalid()) {
								logger.info("Job start ,task info:"+t.getJsonData());
								success = taskService.run(t);
							}
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
					
					logger.info("Task proccess done,taskname:"+t.getTaskName()+",data type:"+t.getData_type()+",action type:"+t.getAction()+",taskdetail:"+t.getJsonData()+",success:"+success+",isdelete:"+delete+",exe time:"+(System.currentTimeMillis()-start));
				}
			}
			if (watchFlag) {
				taskService.getZkService().initWatcher(Configure.ZookeeperQueuePath);
			}
	}
	
	
}
