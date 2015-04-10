package com.valueclickbrands.solr.model;

public class InvField extends InvObject {
	private String entity_type = "invpage";
	private String bundle = "invautopage";
	private int deleted = 0;
	private long entity_id;
	private long revision_id;
	private String language = "und";
	private int delta = 0;
	
	public InvField(){
		
	}
	
	public InvField(long entity_id){
		this.entity_id = entity_id;
		this.revision_id = entity_id;
	}

	public String getEntity_type() {
		return entity_type;
	}

	public void setEntity_type(String entity_type) {
		this.entity_type = entity_type;
	}

	public String getBundle() {
		return bundle;
	}

	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public long getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(long entity_id) {
		this.entity_id = entity_id;
	}

	public long getRevision_id() {
		return revision_id;
	}

	public void setRevision_id(long revision_id) {
		this.revision_id = revision_id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getDelta() {
		return delta;
	}

	public void setDelta(int delta) {
		this.delta = delta;
	}
}
