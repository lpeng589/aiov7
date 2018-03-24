package com.koron.oa.office.meeting;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>Title:h会议</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-14
 * @Copyright: 科荣软件
 * @Author kezhiliang
 * @preserve all
 */
@Entity
@Table(name="OAMeeting")
public class OAMeetingBean  implements Serializable {
	@Id
	private String id;
	private String title;
	private String meetingContent;
	private String filePath;
	//private int significance;
	
	//private String punish;
	private int signinTime;
	private String status;
	private String boardroomId;
	//private String outerAddress;
	private Date startTime;
	private Date endTime;
	private String createTime;
	private String lastUpdateTime;
	private String wakeUpMode;
	private int warnTime;
	
	private String sponsor;//发起
	private String toastmaster;//主持
	private String toastmasterName;
	private String participant;//参与
	private String participantName;
	private String meetingNote;
	
	
	private String signin;//签到人员
	private String taker;  //笔录员
	
	//查看权限人员指定
	/*private String authority;
	private String authorityName;
	private int selectop=0;*/
	
	
	//例会
	private int regularMeeting=0;
	private int regularDay=1;
	
	//例会结束时间
	//例会结束时间
	private String regularend;
	
	
	
	
	public String getSponsor() {
		return sponsor;
	}
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	public String getToastmaster() {
		return toastmaster;
	}
	public void setToastmaster(String toastmaster) {
		this.toastmaster = toastmaster;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRegularend() {
		return regularend;
	}
	public void setRegularend(String regularend) {
		this.regularend = regularend;
	}

	public int getRegularMeeting() {
		return regularMeeting;
	}
	public void setRegularMeeting(int regularMeeting) {
		this.regularMeeting = regularMeeting;
	}
	public int getRegularDay() {
		return regularDay;
	}
	public void setRegularDay(int regularDay) {
		this.regularDay = regularDay;
	}
	
	public String getTaker() {
		return taker;
	}
	public void setTaker(String taker) {
		this.taker = taker;
	}
	public String getSignin() {
		return signin;
	}
	public void setSignin(String signin) {
		this.signin = signin;
	}
	public String getMeetingNote() {
		return meetingNote;
	}
	public void setMeetingNote(String meetingNote) {
		this.meetingNote = meetingNote;
	}
	public String getToastmasterName() {
		return toastmasterName;
	}
	public void setToastmasterName(String toastmasterName) {
		this.toastmasterName = toastmasterName;
	}
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMeetingContent() {
		return meetingContent;
	}
	public void setMeetingContent(String meetingContent) {
		this.meetingContent = meetingContent;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public int getSigninTime() {
		return signinTime;
	}
	public void setSigninTime(int signinTime) {
		this.signinTime = signinTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBoardroomId() {
		return boardroomId;
	}
	public void setBoardroomId(String boardroomId) {
		this.boardroomId = boardroomId;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getWakeUpMode() {
		return wakeUpMode;
	}
	public void setWakeUpMode(String wakeUpMode) {
		this.wakeUpMode = wakeUpMode;
	}
	public int getWarnTime() {
		return warnTime;
	}
	public void setWarnTime(int warnTime) {
		this.warnTime = warnTime;
	}
	public String getParticipant() {
		return participant;
	}
	public void setParticipant(String participant) {
		this.participant = participant;
	}
	
	
	
	
	
}
