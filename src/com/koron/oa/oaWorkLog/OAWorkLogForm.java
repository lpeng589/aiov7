package com.koron.oa.oaWorkLog;

import com.menyi.web.util.BaseSearchForm;

public class OAWorkLogForm extends BaseSearchForm{
	
	private String type;//日志类型 day:日 week:周

	private String journalDate;// 日志日期

	private String shareBy;// 分享给

	private String affix;//附件

	private String clientId;// 关联客户
	
	private String createBy;// 创建人

	private String lastUpdateBy;// 最后修改人
	
	private String summaryContents;//总结内容,用"|"隔开
	
	private String planContents;//计划内容,用"|"隔开
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getJournalDate() {
		return journalDate;
	}

	public void setJournalDate(String journalDate) {
		this.journalDate = journalDate;
	}

	public String getShareBy() {
		return shareBy;
	}

	public void setShareBy(String shareBy) {
		this.shareBy = shareBy;
	}

	public String getAffix() {
		return affix;
	}

	public void setAffix(String affix) {
		this.affix = affix;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public String getSummaryContents() {
		return summaryContents;
	}

	public void setSummaryContents(String summaryContents) {
		this.summaryContents = summaryContents;
	}

	public String getPlanContents() {
		return planContents;
	}

	public void setPlanContents(String planContents) {
		this.planContents = planContents;
	}

	
	
}
