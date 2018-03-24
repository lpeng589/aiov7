package com.koron.oa.oaWorkLog;

import com.menyi.web.util.BaseSearchForm;

public class OAWorkLogSearchForm extends BaseSearchForm{
	
	
	private String weekStartTime;//周开始时间
	
	private String followEmpId;//关注人ID

	private String searchWorkLogDate;//周开始时间
	
	private String tabSelectName;//tab名称
	
	private String teamLogDate;//团队日志日期
	
	private String teamLogType;//团队日志类型 day,week
	
	
	public String getWeekStartTime() {
		return weekStartTime;
	}

	public void setWeekStartTime(String weekStartTime) {
		this.weekStartTime = weekStartTime;
	}

	public String getFollowEmpId() {
		return followEmpId;
	}

	public void setFollowEmpId(String followEmpId) {
		this.followEmpId = followEmpId;
	}

	public String getSearchWorkLogDate() {
		return searchWorkLogDate;
	}

	public void setSearchWorkLogDate(String searchWorkLogDate) {
		this.searchWorkLogDate = searchWorkLogDate;
	}

	public String getTabSelectName() {
		return tabSelectName;
	}

	public void setTabSelectName(String tabSelectName) {
		this.tabSelectName = tabSelectName;
	}

	public String getTeamLogDate() {
		return teamLogDate;
	}

	public void setTeamLogDate(String teamLogDate) {
		this.teamLogDate = teamLogDate;
	}

	public String getTeamLogType() {
		return teamLogType;
	}

	public void setTeamLogType(String teamLogType) {
		this.teamLogType = teamLogType;
	}
	
	
}
