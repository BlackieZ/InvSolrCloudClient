package com.valueclickbrands.solr.model;

public class Noindexparams extends InvField {
	private String field_page_noindexparams_value;
	
	public Noindexparams(){
		
	}
	
	public Noindexparams(long entity_id, String field_page_noindexparams_value){
		super(entity_id);
		this.field_page_noindexparams_value = field_page_noindexparams_value;
	}

	public String getField_page_noindexparams_value() {
		return field_page_noindexparams_value;
	}

	public void setField_page_noindexparams_value(
			String field_page_noindexparams_value) {
		this.field_page_noindexparams_value = field_page_noindexparams_value;
	}
}
