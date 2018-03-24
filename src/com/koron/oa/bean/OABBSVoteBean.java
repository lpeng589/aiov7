package com.koron.oa.bean;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * <p>Title:投票</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-18
 * @Copyright: 科荣软件
 * @Author 文小钱
 * @preserve all
 */
@Entity
@Table(name="OABBSVote")
public class OABBSVoteBean {

	@Id
	private String id ;
	private String voteTopic ;		/*投票主题*/
	private String voteRemark ;		/*投票备注*/
	private String voteType ;		/*投票方式*/
	private String beginTime ;		/*开始时间*/
	private String endTime ;		/*结束时间*/
	private String answerType ;		/*回答方式*/
	private String wakeUpMode ;		/*提醒方式*/
 
	@OneToMany(mappedBy="voteBean", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	private List<OABBSVoteAnswerBean> voteAnswer ;	/*调查结果*/
	private String createBy ;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAnswerType() {
		return answerType;
	}
	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getVoteRemark() {
		return voteRemark;
	}
	public void setVoteRemark(String voteRemark) {
		this.voteRemark = voteRemark;
	}
	public String getVoteTopic() {
		return voteTopic;
	}
	public void setVoteTopic(String voteTopic) {
		this.voteTopic = voteTopic;
	}
	public String getVoteType() {
		return voteType;
	}
	public void setVoteType(String voteType) {
		this.voteType = voteType;
	}
	public String getWakeUpMode() {
		return wakeUpMode;
	}
	public void setWakeUpMode(String wakeUpMode) {
		this.wakeUpMode = wakeUpMode;
	}
	public List<OABBSVoteAnswerBean> getVoteAnswer() {
		return voteAnswer;
	}
	public void setVoteAnswer(List<OABBSVoteAnswerBean> voteAnswer) {
		this.voteAnswer = voteAnswer;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

}
