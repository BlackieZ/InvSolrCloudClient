package com.valueclickbrands.solr.model;

/** 
 * @author Vanson Zou
 * @date Dec 18, 2014 
 */

public class TaskEntity {
	
	private String taskName;
	private String data_type ;
	private String action ;
	private int nid;
	private int vid;
	private long date;
	private boolean istree;//
	private String tree_action;//value:fully 
	private int branch_nid;// the child node id which need to be deleted
	private boolean isInvalid;
	private String url;
	private long template_pid;
	private String jsonData;
	
	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}


	public TaskEntity() {
		
	}
	
	
	public TaskEntity(long id,String dataType,String action,int nid,int vid,long date,boolean istree,String tree_action) {
		this.data_type = dataType;
		this.action = action;
		this.nid = nid;
		this.vid = vid;
		this.date = date;
		this.istree = istree;
	}

	public int getBranch_nid() {
		return branch_nid;
	}

	public void setBranch_nid(int branch_nid) {
		this.branch_nid = branch_nid;
	}

	public String getTree_action() {
		return tree_action;
	}

	public void setTree_action(String tree_action) {
		this.tree_action = tree_action;
	}

	public boolean isIstree() {
		return istree;
	}

	public void setIstree(boolean istree) {
		this.istree = istree;
	}

	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public long getTemplate_pid() {
		return template_pid;
	}


	public void setTemplate_pid(long template_pid) {
		this.template_pid = template_pid;
	}


	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public int getNid() {
		return nid;
	}

	public void setNid(int nid) {
		this.nid = nid;
	}

	public int getVid() {
		return vid;
	}

	public void setVid(int vid) {
		this.vid = vid;
	}

	

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public boolean isInvalid() {
		return isInvalid;
	}

	public void setInvalid(boolean isInvalid) {
		this.isInvalid = isInvalid;
	}

	
	
}
