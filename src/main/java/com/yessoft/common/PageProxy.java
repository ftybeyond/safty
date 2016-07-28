package com.yessoft.common;

import java.io.Serializable;

/**
 * 
* @作者 付天有
* @编写日期 2013-11-13
* @功能描述 分页信息传输对象
*          	需要设定 currentPage  当前页
*                   allRecord    总记录数
*                   recordOfPage 每页显示条数
*           通常由前台页面给出数据
 */
@SuppressWarnings("serial")
public class PageProxy implements Serializable {

	/**
	 * 总页数
	 */
	private int allPage;

	/**
	 * 总记录数
	 */
	private long allRecord;

	/**
	 * 当前页数 默认1
	 */
	private int currentPage = 1;
	
	
	@SuppressWarnings("unused")
	private int firstResult;
	
	private String url;
	

	public String getUrl() {
		if(url!=null){
			//判断url后是否有参数
			int paramIndex = url.indexOf("?");
			if(paramIndex < 0){
				//没有参数，加入一个空参数参数，方便页面追加参数
				url += "?_dt=&";
			}
		}
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public PageProxy() {
		// TODO Auto-generated constructor stub
	}

	public PageProxy(int currentPage,int pageSize) {
		// TODO Auto-generated constructor stub
		this.currentPage = currentPage;
		this.recordOfPage = pageSize;
	}
	
	/**
	 * 每页显示条数
	 */
	private int recordOfPage = 15;
	
	@SuppressWarnings("unused")
	private boolean firstPage;
	
	@SuppressWarnings("unused")
	private boolean lastPage;
	
	public boolean getFirstPage() {
		if (currentPage == 1) {
			return true;
		}else{
			return false;
		}
	}

	public void setFirstPage(boolean firstPage) {
		this.firstPage = firstPage;
	}

	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}


	public boolean getLastPage() {
		if (currentPage == getAllPage()||getAllRecord()==0) {
			return true;
		}else{
			return false;
		}
	}

	public int getAllPage() {
		if (getAllRecord() % getRecordOfPage() == 0) {
			allPage = (int) (getAllRecord() / getRecordOfPage());
		} else {
			allPage = (int) (getAllRecord() / getRecordOfPage() + 1);
		}
		return allPage;
	}

	public long getAllRecord() {
		
		return allRecord;
	}

	public int getCurrentPage() {
		return currentPage;
	}



	public void setAllPage(int allPage) {
		this.allPage = allPage;
	}

	public void setAllRecord(long allRecord) {
		this.allRecord = allRecord;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}



	public int getFirstResult() {
		return (this.getCurrentPage() - 1)* this.getRecordOfPage();
	}
	
	public int getLimitResult(){
		return getFirstResult() + getRecordOfPage();
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}


	public int getRecordOfPage() {
		return recordOfPage;
	}


	public void setRecordOfPage(int recordOfPage) {
		this.recordOfPage = recordOfPage;
	}
	
	
}