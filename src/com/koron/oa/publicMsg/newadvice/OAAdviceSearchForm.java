package com.koron.oa.publicMsg.newadvice;

import com.menyi.web.util.BaseSearchForm;

/**
 * 
 * <p>Title:新闻中心</p> 
 * <p>Description: </p>
 *
 * @Copyright: 2012-6-18
 * @Author 李文祥
 * 
 */
public class OAAdviceSearchForm extends BaseSearchForm {
	private static final long serialVersionUID = 1L;
	private String adviceType;
	private String adviceTitle;
	private String createBy;
	private String proUserName;
	private String beginTime ;
	private String endTime ;
	private String timeType;
	private String selectType;   //存储当前选择的查询方式
	private String HavingType;   //存储选择的查询方式（时间关键字、类型），以便再次请求时判断
	private String selectId;     //存储当前查询Id
	private String HavingId;     //存储选择的查询方式Id（每个时间关键字、每个新闻类型），以便再次请求时判断
	private String keyWord;
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getSelectId() {
		return selectId;
	}
	public void setSelectId(String selectId) {
		this.selectId = selectId;
	}
	public String getHavingType() {
		return HavingType;
	}
	public void setHavingType(String havingType) {
		HavingType = havingType;
	}
	public String getSelectType() {
		return selectType;
	}
	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getAdviceTitle() {
		return adviceTitle;
	}
	public void setAdviceTitle(String adviceTitle) {
		this.adviceTitle = adviceTitle;
	}
	public String getAdviceType() {
		return adviceType;
	}
	public void setAdviceType(String adviceType) {
		this.adviceType = adviceType;
	}
	public String getHavingId() {
		return HavingId;
	}
	public void setHavingId(String havingId) {
		HavingId = havingId;
	}
	public String getProUserName() {
		return proUserName;
	}
	public void setProUserName(String proUserName) {
		this.proUserName = proUserName;
	}
	
	
	
}
