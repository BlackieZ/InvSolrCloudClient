/**
 * 
 */
package com.valueclickbrands.framework.util;

/**
 * @author 潘冬
 * 2009-7-11 下午09:28:06
 */
public class PageControl {
	private int pageno = 1; // 当前页码
	private int pagesize = 20; // 每页行数
	private int rowcount; // 总行数
	private int pagecount; // 总页数
	private boolean useprevious;// 前一页是否能用
	private boolean usebehind;// 后一页是否能用
	private boolean usepage = true;// 是否分页

	public int getPageno() {
		if(pageno>pagecount){
			pageno = pagecount;
			usebehind = false;
		}
		return pageno;
	}

	public void setPageno(int pageno) {
		this.pageno = pageno;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		if (pagesize == 0) {// 0:不分页
			usepage = false;
			pagecount = 1;
			pageno = 1;
			useprevious = false;
			usebehind = false;
		}
		this.pagesize = pagesize;
	}

	public int getRowcount() {
		return rowcount;
	}
	
	public void setRowcount(int rowcount) {
		this.rowcount = rowcount;
		if (usepage) {
			pagecount = (rowcount % pagesize == 0) ? (rowcount / pagesize)
					: (rowcount / pagesize + 1);
			useprevious = pageno == 1 ? false : true;
			usebehind = pageno == pagecount ? false : true;
		}
	}
	
	public int getStart(){
		return getSkipResults();
	}

	public int getPagecount() {
		return pagecount;
	}
	
	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}

	public boolean isUseprevious() {
		return useprevious;
	}

	public void setUseprevious(boolean useprevious) {
		this.useprevious = useprevious;
	}

	public boolean isUsebehind() {
		return usebehind;
	}

	public void setUsebehind(boolean usebehind) {
		this.usebehind = usebehind;
	}

	public boolean isUsepage() {
		return usepage;
	}

	public void setUsepage(boolean usepage) {
		this.usepage = usepage;
	}

	public int getSkipResults() {
		return (this.pageno - 1) * this.pagesize;
	}

	public int getMaxResults() {
		return this.pagesize;
	}
}
