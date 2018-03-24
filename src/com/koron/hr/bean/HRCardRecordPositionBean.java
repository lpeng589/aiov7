package com.koron.hr.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>Title:打卡记录位置设置</p> 
 * <p>Description: </p>
 * @Date:2012-10-24
 * @Copyright: 科荣软件
 * @Author:徐洁俊
 * @preserve all
 */
@Entity
@Table(name="HRCardRecordPosition")
public class HRCardRecordPositionBean {

	@Id
	private String id;

	private int numStart;

	private int numEnd;

	private int dateStart;

	private int dateEnd;

	private int timeStart;

	private int timeEnd;

	public int getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(int dateEnd) {
		this.dateEnd = dateEnd;
	}

	public int getDateStart() {
		return dateStart;
	}

	public void setDateStart(int dateStart) {
		this.dateStart = dateStart;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumEnd() {
		return numEnd;
	}

	public void setNumEnd(int numEnd) {
		this.numEnd = numEnd;
	}

	public int getNumStart() {
		return numStart;
	}

	public void setNumStart(int numStart) {
		this.numStart = numStart;
	}

	public int getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(int timeEnd) {
		this.timeEnd = timeEnd;
	}

	public int getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(int timeStart) {
		this.timeStart = timeStart;
	}

}
