package com.valueclickbrands.solr.model;

public class InterestLevel extends InvField {
	private long field_page_interest_level_tid;
	
	public InterestLevel(){
		
	}
	
	public InterestLevel(long entity_id, long field_page_interest_level_tid){
		super(entity_id);
		this.field_page_interest_level_tid = field_page_interest_level_tid;
	}

	public long getField_page_interest_level_tid() {
		return field_page_interest_level_tid;
	}

	public void setField_page_interest_level_tid(long field_page_interest_level_tid) {
		this.field_page_interest_level_tid = field_page_interest_level_tid;
	}
}
