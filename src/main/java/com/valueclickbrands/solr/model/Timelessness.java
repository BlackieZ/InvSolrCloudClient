package com.valueclickbrands.solr.model;

public class Timelessness extends InvField {
	private long field_page_timelessness_tid;
	
	public Timelessness(){
		
	}
	
	public Timelessness(long entity_id,long field_page_timelessness_tid){
		super(entity_id);
		this.field_page_timelessness_tid = field_page_timelessness_tid;
	}

	public long getField_page_timelessness_tid() {
		return field_page_timelessness_tid;
	}

	public void setField_page_timelessness_tid(long field_page_timelessness_tid) {
		this.field_page_timelessness_tid = field_page_timelessness_tid;
	}
}
