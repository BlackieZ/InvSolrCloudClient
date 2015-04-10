package com.valueclickbrands.solr.model;

public class ContentType extends InvField {
	
	private long field_page_content_type_tid;

	public ContentType() {

	}

	public ContentType(long entity_id, long field_page_content_type_tid) {
		super(entity_id);
		this.field_page_content_type_tid = field_page_content_type_tid;
	}

	public long getField_page_content_type_tid() {
		return field_page_content_type_tid;
	}

	public void setField_page_content_type_tid(long field_page_content_type_tid) {
		this.field_page_content_type_tid = field_page_content_type_tid;
	}
}
