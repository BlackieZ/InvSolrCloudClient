package com.valueclickbrands.solr.model;

public class Invpage extends InvObject {
	private long pid;
	private String type = "invautopage";
	private String language = "und";
	private String title;
	private String owner = "SolrClient";
	private long uid = 3;
	private long status = 0;
	private long created;
	private long changed;
	private String data = "a:0:{}";

	public Invpage() {

	}
	
	public Invpage(String title){
		this.title = title;
	}
	
	public Invpage(String title,long created){
	
		this.title = title;
		this.created = created;
		this.changed = created;
	}
	
	public Invpage(String title,long created,long changed){
		this.title = title;
		this.created = created;
		this.changed = changed;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public long getChanged() {
		return changed;
	}

	public void setChanged(long changed) {
		this.changed = changed;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
