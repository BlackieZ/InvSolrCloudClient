package com.valueclickbrands.solr.model;

public class Advertising extends InvField {
	private long field_page_advertising_tid;
	
	public Advertising(){
		
	}
	
	public Advertising(long entity_id, long field_page_advertising_tid){
		super(entity_id);
		this.field_page_advertising_tid = field_page_advertising_tid;
	}

	public long getField_page_advertising_tid() {
		return field_page_advertising_tid;
	}

	public void setField_page_advertising_tid(long field_page_advertising_tid) {
		this.field_page_advertising_tid = field_page_advertising_tid;
	}
	
}
