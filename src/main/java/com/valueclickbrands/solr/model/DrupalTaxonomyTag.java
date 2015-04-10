package com.valueclickbrands.solr.model;

import java.io.Serializable;



/** 
 * @author Vanson Zou
 * @date Dec 31, 2014 
 */

public class DrupalTaxonomyTag  {
	private long tagID;
	private String tag;
	private long priority;
	
	public long getTagID() {
		return tagID;
	}

	public void setTagID(long tagID) {
		this.tagID = tagID;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}

	public DrupalTaxonomyTag(String name,long tagId,long field_private_taxonomy_value){
		this.tagID = tagId;
		this.tag = name;
		this.priority=field_private_taxonomy_value;
	}

	
}
