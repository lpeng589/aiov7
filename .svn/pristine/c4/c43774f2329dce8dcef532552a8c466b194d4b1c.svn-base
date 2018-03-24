package com.koron.hr.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.menyi.aio.bean.BrushCardAnnalBean;
import com.menyi.aio.bean.EvectionBean;
import com.menyi.aio.bean.LeaveBean;

/**
 * 职员考勤综合信息
 * @author Administrator
 *
 */
public class EmployeeWorkRuleInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String employeeNo;
	private WorkRuleBean workRuleBean;
	private List<DutyPeriodsBean> periodsBean = new ArrayList<DutyPeriodsBean>();
	private List<BrushCardAnnalBean> annals = new ArrayList<BrushCardAnnalBean>();
	private List<LeaveBean> leaves = new ArrayList<LeaveBean>();
	private List<EvectionBean> evections = new ArrayList<EvectionBean>();

	public EmployeeWorkRuleInfoBean(){}
	
	public EmployeeWorkRuleInfoBean(String employeeNo,WorkRuleBean workRuleBean,
			List<DutyPeriodsBean> periodsBean,List<BrushCardAnnalBean> annals,
			List<LeaveBean> leaves,List<EvectionBean> evections){
		this.employeeNo = employeeNo;
		this.workRuleBean = workRuleBean;
		this.periodsBean = periodsBean;
		this.annals = annals;
		this.leaves = leaves;
		this.evections = evections;
	}
	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public List<BrushCardAnnalBean> getAnnals() {
		return annals;
	}
	public void setAnnals(List<BrushCardAnnalBean> annals) {
		this.annals = annals;
	}
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	public List<DutyPeriodsBean> getPeriodsBean() {
		return periodsBean;
	}
	public void setPeriodsBean(List<DutyPeriodsBean> periodsBean) {
		this.periodsBean = periodsBean;
	}
	public WorkRuleBean getWorkRuleBean() {
		return workRuleBean;
	}
	public void setWorkRuleBean(WorkRuleBean workRuleBean) {
		this.workRuleBean = workRuleBean;
	}
	public List<EvectionBean> getEvections() {
		return evections;
	}

	public void setEvections(List<EvectionBean> evections) {
		this.evections = evections;
	}

	public List<LeaveBean> getLeaves() {
		return leaves;
	}

	public void setLeaves(List<LeaveBean> leaves) {
		this.leaves = leaves;
	}
}
