package com.menyi.aio.bean;

import java.io.Serializable;

public class SquadSect implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String squadEnactmentNo;
	private int onDutyAvailabilityTime;
	private int offDutyAvailabilityTime;
	private String onDutyTime;
	private String offDutyTime;
	private String squadSectType;
	private int statusId;
	
	public SquadSect(){}
	
	public SquadSect(String squadEnactmentNo,int onDutyAvailabilityTime,int offDutyAvailabilityTime,
			String onDutyTime,String offDutyTime,String squadSectType){
		this.squadEnactmentNo = squadEnactmentNo;
		this.onDutyAvailabilityTime = onDutyAvailabilityTime;
		this.offDutyAvailabilityTime = offDutyAvailabilityTime;
		this.onDutyTime = onDutyTime;
		this.offDutyTime = offDutyTime;
		this.squadSectType = squadSectType;
	}
	
	public SquadSect(String onDutyTime,String offDutyTime,int onDutyAvailabilityTime,
			int offDutyAvailabilityTime,String squadSectType){
		this.onDutyAvailabilityTime = onDutyAvailabilityTime;
		this.offDutyAvailabilityTime = offDutyAvailabilityTime;
		this.onDutyTime = onDutyTime;
		this.offDutyTime = offDutyTime;
		this.squadSectType = squadSectType;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getOffDutyAvailabilityTime() {
		return offDutyAvailabilityTime;
	}

	public void setOffDutyAvailabilityTime(int offDutyAvailabilityTime) {
		this.offDutyAvailabilityTime = offDutyAvailabilityTime;
	}

	public String getOffDutyTime() {
		return offDutyTime;
	}

	public void setOffDutyTime(String offDutyTime) {
		this.offDutyTime = offDutyTime;
	}

	public int getOnDutyAvailabilityTime() {
		return onDutyAvailabilityTime;
	}

	public void setOnDutyAvailabilityTime(int onDutyAvailabilityTime) {
		this.onDutyAvailabilityTime = onDutyAvailabilityTime;
	}

	public String getOnDutyTime() {
		return onDutyTime;
	}

	public void setOnDutyTime(String onDutyTime) {
		this.onDutyTime = onDutyTime;
	}

	public String getSquadSectType() {
		return squadSectType;
	}

	public void setSquadSectType(String squadSectType) {
		this.squadSectType = squadSectType;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getSquadEnactmentNo() {
		return squadEnactmentNo;
	}

	public void setSquadEnactmentNo(String squadEnactmentNo) {
		this.squadEnactmentNo = squadEnactmentNo;
	}
	
	

}
