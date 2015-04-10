package com.valueclickbrands.solr.model;

import java.io.Serializable;

public class Image implements Serializable {
	private String uri;
	private int entity_id;
	private int revision_id;
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
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
	
}
