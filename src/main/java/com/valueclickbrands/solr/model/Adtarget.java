package com.valueclickbrands.solr.model;

public class Adtarget extends InvField {
	private String field_page_adtarget_value;
	
	public Adtarget(){
		
	}
	
	public Adtarget(long entity_id, String field_page_adtarget_value){
		super(entity_id);
		this.field_page_adtarget_value = field_page_adtarget_value;
	}

	public String getField_page_adtarget_value() {
		return field_page_adtarget_value;
	}

	public void setField_page_adtarget_value(String field_page_adtarget_value) {
		this.field_page_adtarget_value = field_page_adtarget_value;
	}
	
}
