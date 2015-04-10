package com.valueclickbrands.solr.model;

public class InvUrlAlias extends InvObject {
	private String source;
	private String alias;
	private String language = "und";

	public InvUrlAlias(){
		
	}
	
	public InvUrlAlias(String source,String alias){
		this.source = source;
		this.alias = alias;
		this.language = "und";
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
