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
@Table(name="OAMeetingNote")
public class OANoteBean  implements Serializable{
	@Id
    private String noteId;
    private String readyContent;
    private String readyFilePath;
    private String readyProblem;
    private String summary;
    private String meetingExecute;
    //private String keepMeeting;
    private String meetingId;
    
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public String getNoteId() {
		return noteId;
	}
	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}
	public String getReadyContent() {
		return readyContent;
	}
	public void setReadyContent(String readyContent) {
		this.readyContent = readyContent;
	}
	public String getReadyFilePath() {
		return readyFilePath;
	}
	public void setReadyFilePath(String readyFilePath) {
		this.readyFilePath = readyFilePath;
	}
	public String getReadyProblem() {
		return readyProblem;
	}
	public void setReadyProblem(String readyProblem) {
		this.readyProblem = readyProblem;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getMeetingExecute() {
		return meetingExecute;
	}
	public void setMeetingExecute(String meetingExecute) {
		this.meetingExecute = meetingExecute;
	}

}
