package com.valueclickbrands.solr.model;

import com.google.gson.Gson;

public class Channel extends InvField {
	private long field_page_channel_tid;
	
	public Channel(){
		
	}
	
	public Channel(long entity_id, long field_page_channel_tid){
		super(entity_id);
		this.field_page_channel_tid = field_page_channel_tid;
	}

	public long getField_page_channel_tid() {
		return field_page_channel_tid;
	}

	public void setField_page_channel_tid(long field_page_channel_tid) {
		this.field_page_channel_tid = field_page_channel_tid;
	}
	public static void main(String[] args) {
			Gson gson = new Gson();
			String data = "{\"data_type\":\"taxonomy\",\"action\":\"add\",\"url\":\"\\/markets\\/stocks\\/msft\\/\",\"template_pid\":\"18066\",\"date\":1420466904}";
			TaskEntity taskEntity = gson.fromJson(data, TaskEntity.class);
			System.out.println(taskEntity.getAction());
			System.out.println(taskEntity.getUrl());
			System.out.println(taskEntity.getTemplate_pid());
			
	}
}
