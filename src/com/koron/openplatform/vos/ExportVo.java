package com.koron.openplatform.vos;

import java.util.List;



/**
 * @author mj
 *
 */
public class ExportVo {
	
	private int totalNum;//总数量
	private int totalPage;//总页数
	private List<TblOrder> list;
	private List<LogisticsCompanyVo> logisticsCompanyVos;
	
	public List<LogisticsCompanyVo> getLogisticsCompanyVos() {
		return logisticsCompanyVos;
	}
	public void setLogisticsCompanyVos(List<LogisticsCompanyVo> logisticsCompanyVos) {
		this.logisticsCompanyVos = logisticsCompanyVos;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public List<TblOrder> getList() {
		return list;
	}
	public void setList(List<TblOrder> list) {
		this.list = list;
	}
	
}
