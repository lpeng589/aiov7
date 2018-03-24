package com.koron.oa.publicMsg.newsInfo;

import com.menyi.web.util.BaseSearchForm;

/**
 * 
 * <p>Title:新闻中心</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-8
 * @Copyright: 2012-6-5
 * @Author 李文祥
 * 
 */
public class OANewsSearchForm extends BaseSearchForm {
	

	private static final long serialVersionUID = 1L;
	private String keyWord;
	private String newsType;
	private String newsTitle;
	private String createBy;
	private String beginTime ;
	private String endTime ;
	private String timeType;
	private String selectType;   //存储当前选择的查询方式
	private String HavingType;   //存储选择的查询方式（时间关键字、类型），以便再次请求时判断
	private String selectId;     //存储当前查询Id
	private String HavingId;     //存储选择的查询方式Id（每个时间关键字、每个新闻类型），以便再次请求时判断

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
	public String getNewsTitle() {
		return newsTitle;
	}
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}
	public String getNewsType() {
		return newsType;
	}
	public void setNewsType(String newsType) {
		this.newsType = newsType;
	}
	public String getHavingId() {
		return HavingId;
	}
	public void setHavingId(String havingId) {
		HavingId = havingId;
	}
	
	
	
}
