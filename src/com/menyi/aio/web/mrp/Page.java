package com.menyi.aio.web.mrp;

public class Page {
	
	private int pageNo = 1;
	
	private int nextPage = 2;
	
	private int privousPage = 1;
	
	private int pageSize = 10;
	
	private int totalNum = 0;
	
	private int totalPage = 1;	
	
	private int start = (pageNo-1)*pageSize+1;
	
	private int end = pageNo*pageSize;
	
	public Page(){		
	}
	public Page(int pageSize){
		
		this.pageSize = pageSize;
		this.end = this.pageNo*this.pageSize;
	}
	public void setPage(Page page){
		
		this.setPageNo(page.getPageNo());
		this.setPageSize(page.getPageSize());
		this.setTotalNum(page.getTotalNum());
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNoStr){
		if(pageNoStr==null){
			pageNo = 1;
		}else{
			try {this.setPageNo(Integer.parseInt(pageNoStr));} catch (NumberFormatException e) {}
		}		
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
		this.nextPage = pageNo+1;
		this.privousPage = pageNo-1;
		this.start = (pageNo-1)*pageSize+1;
		this.end = pageNo*pageSize;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
		this.totalPage = this.totalNum/this.pageSize+(this.totalNum%this.pageSize>0?1:0);
		this.totalPage = this.totalPage>0?this.totalPage:1;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getNextPage() {
		return nextPage;
	}
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	public int getPrivousPage() {
		return privousPage;
	}
	public void setPrivousPage(int privousPage) {
		this.privousPage = privousPage;
	}
	
}
