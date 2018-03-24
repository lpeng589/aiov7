package com.koron.oa.OACalendar;

import java.io.Serializable;
import javax.persistence.*;

import org.dom4j.tree.AbstractEntity;
/**
 * 
 * <p>Title:版块</p> 
 * <p>Description: </p>
 *
 * @Date:2013-11-10
 * @Copyright: 科荣软件
 * @Author wyy
 * @preserve all
 */
@Entity
@Table(name="OACalendar")
public class OACalendarBean extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String title;
	private String finishTime;
	private String userId;//创建人或其他表的创建人
	private String stratTime;
	private String type;
	private String alertTime;//
	private String delStatus;//0可删，1不可删 只是导入数据	
	private String alertId;
	private String participant; //会议的参与人
	private String relationId;  //外部添加进日程的关联Id
	private String clientId; //关联客户Id
	private String finishStatus; //完成状态
	private String taskId; //指派任务Id
	
	
	public String getFinishStatus() {
		return finishStatus;
	}
	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getRelationId() {
		return relationId;
	}
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	public String getParticipant() {
		return participant;
	}
	public void setParticipant(String participant) {
		this.participant = participant;
	}
	public String getAlertId() {
		return alertId;
	}
	public void setAlertId(String alertId) {
		this.alertId = alertId;
	}
	public String getAlertTime() {
		return alertTime;
	}
	public void setAlertTime(String alertTime) {
		this.alertTime = alertTime;
	}
	public String getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}
	public String getStratTime() {
		return stratTime;
	}
	public void setStratTime(String stratTime) {
		this.stratTime = stratTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
