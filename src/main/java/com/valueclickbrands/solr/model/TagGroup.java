package com.valueclickbrands.solr.model;

import java.io.Serializable;

public class TagGroup implements Serializable {
	
	private int tid;
	private int groupId;
	private String groupName;
	
	public TagGroup(){
		
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	
	
}
