package com.valueclickbrands.solr.model;

public class Metatag extends InvObject {
	private String entity_type;
	private long entity_id;
	private String data;
	
	public Metatag(){
		
	}
	
	public Metatag(long entity_id){
		this.entity_type = "invpage";
		this.entity_id = entity_id;
	}
	
	public Metatag(long entity_id,String data){
		this.entity_type = "invpage";
		this.entity_id = entity_id;
		this.data = data;
	}

	public String getEntity_type() {
		return entity_type;
	}

	public void setEntity_type(String entity_type) {
		this.entity_type = entity_type;
	}

	public long getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(long entity_id) {
		this.entity_id = entity_id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
