package com.valueclickbrands.solr.model;

/** 
 * @author Vanson Zou
 * @date Dec 22, 2014 
 */

public class ResponseHeader {
	private int status;
	private int QTime;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getQTime() {
		return QTime;
	}
	public void setQTime(int qTime) {
		QTime = qTime;
	}
	
	
}

