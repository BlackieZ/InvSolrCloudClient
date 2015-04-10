package com.valueclickbrands.solr.model;

public class Website extends InvField {
	private String field_page_website_value;
	
	public Website(){
		
	}
	
	public Website(long entity_id, String field_page_website_value){
		super(entity_id);
		this.field_page_website_value = field_page_website_value;
	}

	public String getField_page_website_value() {
		return field_page_website_value;
	}

	public void setField_page_website_value(String field_page_website_value) {
		this.field_page_website_value = field_page_website_value;
	}
	
}
