package com.koron.hr.bean;

import java.util.ArrayList;
import java.util.List;

import com.menyi.aio.bean.SquadSect;


/**
 * 同一周期的工作周期信息类
 * @author Administrator
 *
 */
public class DutyPeriodsBean implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String periodsNo;
	private List<SquadSect> squadSects = new ArrayList<SquadSect>();
	private String dutyDate;
	
	public DutyPeriodsBean(){}
	
	public DutyPeriodsBean(String periods,List<SquadSect> squadSects,String dutyDate){
		this.periodsNo = periods;
		this.squadSects = squadSects;
		this.dutyDate = dutyDate;
	}
	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getPeriodsNo() {
		return periodsNo;
	}

	public void setPeriodsNo(String periodsNo) {
		this.periodsNo = periodsNo;
	}

	public String getDutyDate() {
		return dutyDate;
	}

	public void setDutyDate(String dutyDate) {
		this.dutyDate = dutyDate;
	}

	public List<SquadSect> getSquadSects() {
		return squadSects;
	}

	public void setSquadSects(List<SquadSect> squadSects) {
		this.squadSects = squadSects;
	}
}

	
