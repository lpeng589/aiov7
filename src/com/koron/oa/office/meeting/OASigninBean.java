package com.koron.oa.office.meeting;

import java.io.Serializable;

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
@Table(name="OAMeetingSignin")
public class OASigninBean implements Serializable{
	@Id
     private String id;
     private String meetingId;
     private String startTime;
     private String signin;
     private String meetingNote;
     
	public String getMeetingNote() {
		return meetingNote;
	}
	public void setMeetingNote(String meetingNote) {
		this.meetingNote = meetingNote;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getSignin() {
		return signin;
	}
	public void setSignin(String signin) {
		this.signin = signin;
	}
     
}
