package com.valueclickbrands.solr.model;

/** 
 * @author Vanson Zou
 * @date Jan 22, 2015 
 */

public class WebToolSettings {

	private int id;
	private String url_alias;
	private String settings;
	private String status;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl_alias() {
		return url_alias;
	}
	public void setUrl_alias(String url_alias) {
		this.url_alias = url_alias;
	}
	public String getSettings() {
		return settings;
	}
	public void setSettings(String settings) {
		this.settings = settings;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
