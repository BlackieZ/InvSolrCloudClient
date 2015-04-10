package com.valueclickbrands.solr.model;

import java.io.Serializable;

public class Tag implements Serializable {
	private int tid;
	private String name;
	private int entity_id;
	private int revision_id;
	private int field_private_taxonomy_value;

	public Tag(){
		
	}
	
	public Tag(String[] data){
		tid = Integer.parseInt(data[0]);
		entity_id = Integer.parseInt(data[1]);
		revision_id = Integer.parseInt(data[2]);
		name = data[3];
	}
	
	
	public int getField_private_taxonomy_value() {
		return field_private_taxonomy_value;
	}

	public void setField_private_taxonomy_value(int field_private_taxonomy_value) {
		this.field_private_taxonomy_value = field_private_taxonomy_value;
	}

	public int getEntity_id() {
		return entity_id;
	}
	public void setEntity_id(int entity_id) {
		this.entity_id = entity_id;
	}
	public int getRevision_id() {
		return revision_id;
	}
	public void setRevision_id(int revision_id) {
		this.revision_id = revision_id;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
