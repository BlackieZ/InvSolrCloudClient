package com.valueclickbrands.solr.model;

public class Master extends InvField {
	private String field_page_master_value;
	
	public Master(){
		
	}
	
	public Master(long entity_id, String field_page_master_value){
		super(entity_id);
		this.field_page_master_value = field_page_master_value;
	}

	public String getField_page_master_value() {
		return field_page_master_value;
	}

	public void setField_page_master_value(String field_page_master_value) {
		this.field_page_master_value = field_page_master_value;
	}
}
