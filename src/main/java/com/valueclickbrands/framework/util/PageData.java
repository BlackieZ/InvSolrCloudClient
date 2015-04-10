package com.valueclickbrands.framework.util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 分页对象. 包含数据及分页信息.
 * @author 潘冬
 * 2009-7-10 上午10:56:57
 */
public class PageData implements Serializable {

	static private int DEFAULT_PAGE_SIZE = 200;

	/**
	 * 每页的记录数
	 */
	private int pageSize = DEFAULT_PAGE_SIZE;

	/**
	 * 当前页第一条数据在List中的位置,从0开始
	 */
	private int start;

	/**
	 * 当前页中存放的记录,类型一般为List
	 */
	private Object data;

	/**
	 * 总记录数
	 */
	private long totalCount;

	/**
	 * 构造方法，只构造空页
	 */
	public PageData() {
		this(0, 0, DEFAULT_PAGE_SIZE, new ArrayList());
	}

	/**
	 * 默认构造方法
	 * 
	 * @param start
	 *            本页数据在数据库中的起始位置
	 * @param totalSize
	 *            数据库中总记录条数
	 * @param pageSize
	 *            本页容量
	 * @param data
	 *            本页包含的数据
	 */
	public PageData(int start, long totalSize, int pageSize, Object data) {
		this.pageSize = pageSize;
		this.start = start;
		this.totalCount = totalSize;
		this.data = data;
	}

	/**
	 * 取数据库中包含的总记录数
	 */
	public long getTotalCount() {
		return this.totalCount;
	}

	/**
	 * 取总页数
	 */
	public long getTotalPageCount() {
		if (totalCount % pageSize == 0)
			return totalCount / pageSize;
		else
			return totalCount / pageSize + 1;
	}

	/**
	 * 取每页数据容量
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 当前页中的记录
	 */
	public Object getResult() {
		return data;
	}
	
	public void setResult(Object result){
		this.data = result;
	}

	/**
	 * 取当前页码,页码从1开始
	 */
	public int getCurrentPageNo() {
		return start / pageSize + 1;
	}

	/**
	 * 是否有下一页
	 */
	public boolean hasNextPage() {
		return this.getCurrentPageNo() < this.getTotalPageCount() - 1;
	}

	/**
	 * 是否有上一页
	 */
	public boolean hasPreviousPage() {
		return this.getCurrentPageNo() > 1;
	}

	/**
	 * 获取任一页第一条数据的位置，每页条数使用默认值
	 */
	protected static int getStartOfPage(int pageNo) {
		return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
	}

	/**
	 * 获取任一页第一条数据的位置,startIndex从0开始
	 */
	public static int getStartOfPage(int pageNo, int pageSize) {
		return (pageNo - 1) * pageSize;
	}
}
