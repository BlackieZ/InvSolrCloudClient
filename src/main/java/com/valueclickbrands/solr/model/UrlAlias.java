package com.valueclickbrands.solr.model;

import java.io.Serializable;

public class UrlAlias implements Serializable {
	private String source;
	private String url;

	public UrlAlias() {

	}

	public UrlAlias(String source,String url) {
		this.source = source;
		this.url = url;
	}
	
	public UrlAlias(int nid,String url) {
		this.source = "node/"+nid;
		this.url = url;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
