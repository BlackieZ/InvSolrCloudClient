package com.valueclickbrands.solr.model;

public class InvTag extends InvField {
	private long field_page_tags_tid;
	
	public InvTag(){
		
	}
	
	public InvTag(long entity_id, long field_page_tags_tid,int delta){
		super(entity_id);
		this.field_page_tags_tid = field_page_tags_tid;
		this.setDelta(delta);
	}

	public long getField_page_tags_tid() {
		return field_page_tags_tid;
	}

	public void setField_page_tags_tid(long field_page_tags_tid) {
		this.field_page_tags_tid = field_page_tags_tid;
	}
}
