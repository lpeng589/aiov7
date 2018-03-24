package com.koron.oa.executive.job;

import java.util.Date;

import com.menyi.web.util.BaseForm;
import com.menyi.web.util.BaseSearchForm;

public class OAJobSearchForm extends BaseSearchForm{
	   
	    private String createPerson;//创建人
	    private String jobtheme ;//主题
	    private String jobType;//类型
	    private String assessor;    //审核员
	    private String participant;//参与者
	    private String createTime;// 创建时间
	    private String jobBeginTime;//开始时间
	    private String jobEndTime;//结束时间
	    private String intterfixServer;//相关客户
	    private String elaborateOn;//详细说明
	    private String attaches;//附件
	    private String state;//审核状态
	    
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getAttaches() {
			return attaches;
		}
		public void setAttaches(String attaches) {
			this.attaches = attaches;
		}
		public String getCreatePerson() {
			return createPerson;
		}
		public void setCreatePerson(String createPerson) {
			this.createPerson = createPerson;
		}
		public String getElaborateOn() {
			return elaborateOn;
		}
		public void setElaborateOn(String elaborateOn) {
			this.elaborateOn = elaborateOn;
		}
		public String getIntterfixServer() {
			return intterfixServer;
		}
		public void setIntterfixServer(String intterfixServer) {
			this.intterfixServer = intterfixServer;
		}
		public String getJobBeginTime() {
			return jobBeginTime;
		}
		public void setJobBeginTime(String jobBeginTime) {
			this.jobBeginTime = jobBeginTime;
		}
		public String getJobEndTime() {
			return jobEndTime;
		}
		public void setJobEndTime(String jobEndTime) {
			this.jobEndTime = jobEndTime;
		}
		public String getJobtheme() {
			return jobtheme;
		}
		public void setJobtheme(String jobtheme) {
			this.jobtheme = jobtheme;
		}
		public String getJobType() {
			return jobType;
		}
		public void setJobType(String jobType) {
			this.jobType = jobType;
		}
		public String getParticipant() {
			return participant;
		}
		public void setParticipant(String participant) {
			this.participant = participant;
		}
		public String getAssessor() {
			return assessor;
		}
		public void setAssessor(String assessor) {
			this.assessor = assessor;
		}
		public String getCreateTime() {
			return createTime;
		}
		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
	   
	    
}
