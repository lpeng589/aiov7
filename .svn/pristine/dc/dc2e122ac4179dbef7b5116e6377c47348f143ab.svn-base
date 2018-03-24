package com.menyi.aio.bean;

import java.util.List;

import javax.persistence.*;

/**
 * 
 * <p>Title:凭证主表Bean</p> 
 * <p>Description: </p>
 * @Date:2013-03-20
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
@Entity
@Table(name = "tblAccMain")
public class AccMainBean {

	@Id
    @Column(nullable=false,length=30)
    
	private String id;
	private Integer SignerID;
	private Integer CredNo;
	private String BillDate;
	private String RefBillType;
	private Integer AcceNum;
	private Integer CredYear;
	private Integer CredMonth;
	private Integer AuditorID;
	private Integer BinderID;
	private String createBy;
	private String lastUpdateBy;
	private String createTime;
	private String lastUpdateTime;
	private Integer statusId;
	private Integer Period;
	private String RefBillNo;
	private String RefBillID;
	private String EmployeeID;
	private String DepartmentCode;
	private Integer OrderNo;
	private String CredTypeID;
	private String Remark;
	private String Attachment;
	private String SCompanyID;
	private String classCode;
	private String RowON;
	private Integer AutoBillMarker;
	private String workFlowNodeName;
	private String workFlowNode;
	private Integer printCount;
	private String checkPersons;
	private String finishTime;
	private String CheckPersont;
	private String AcceptMode;
	private String AcceptNumber;
	
	@OneToMany(mappedBy="accMainBean",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<AccDetailBean> beanDet;
	
	
	public String getAcceptMode() {
		return AcceptMode;
	}
	public void setAcceptMode(String acceptMode) {
		AcceptMode = acceptMode;
	}
	public String getAcceptNumber() {
		return AcceptNumber;
	}
	public void setAcceptNumber(String acceptNumber) {
		AcceptNumber = acceptNumber;
	}
	public String getAttachment() {
		return Attachment;
	}
	public void setAttachment(String attachment) {
		Attachment = attachment;
	}
	public String getBillDate() {
		return BillDate;
	}
	public void setBillDate(String billDate) {
		BillDate = billDate;
	}
	public String getCheckPersons() {
		return checkPersons;
	}
	public void setCheckPersons(String checkPersons) {
		this.checkPersons = checkPersons;
	}
	public String getCheckPersont() {
		return CheckPersont;
	}
	public void setCheckPersont(String checkPersont) {
		CheckPersont = checkPersont;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCredTypeID() {
		return CredTypeID;
	}
	public void setCredTypeID(String credTypeID) {
		CredTypeID = credTypeID;
	}
	public String getDepartmentCode() {
		return DepartmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		DepartmentCode = departmentCode;
	}
	public String getEmployeeID() {
		return EmployeeID;
	}
	public void setEmployeeID(String employeeID) {
		EmployeeID = employeeID;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}
	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public Integer getAcceNum() {
		return AcceNum;
	}
	public void setAcceNum(Integer acceNum) {
		AcceNum = acceNum;
	}
	public Integer getAuditorID() {
		return AuditorID;
	}
	public void setAuditorID(Integer auditorID) {
		AuditorID = auditorID;
	}
	public Integer getAutoBillMarker() {
		return AutoBillMarker;
	}
	public void setAutoBillMarker(Integer autoBillMarker) {
		AutoBillMarker = autoBillMarker;
	}
	public Integer getBinderID() {
		return BinderID;
	}
	public void setBinderID(Integer binderID) {
		BinderID = binderID;
	}
	public Integer getCredMonth() {
		return CredMonth;
	}
	public void setCredMonth(Integer credMonth) {
		CredMonth = credMonth;
	}
	public Integer getCredNo() {
		return CredNo;
	}
	public void setCredNo(Integer credNo) {
		CredNo = credNo;
	}
	public Integer getCredYear() {
		return CredYear;
	}
	public void setCredYear(Integer credYear) {
		CredYear = credYear;
	}
	public Integer getOrderNo() {
		return OrderNo;
	}
	public void setOrderNo(Integer orderNo) {
		OrderNo = orderNo;
	}
	public Integer getPeriod() {
		return Period;
	}
	public void setPeriod(Integer period) {
		Period = period;
	}
	public Integer getPrintCount() {
		return printCount;
	}
	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
	}
	public Integer getSignerID() {
		return SignerID;
	}
	public void setSignerID(Integer signerID) {
		SignerID = signerID;
	}
	public Integer getStatusId() {
		return statusId;
	}
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	public String getRefBillID() {
		return RefBillID;
	}
	public void setRefBillID(String refBillID) {
		RefBillID = refBillID;
	}
	public String getRefBillNo() {
		return RefBillNo;
	}
	public void setRefBillNo(String refBillNo) {
		RefBillNo = refBillNo;
	}
	public String getRefBillType() {
		return RefBillType;
	}
	public void setRefBillType(String refBillType) {
		RefBillType = refBillType;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getRowON() {
		return RowON;
	}
	public void setRowON(String rowON) {
		RowON = rowON;
	}
	public String getSCompanyID() {
		return SCompanyID;
	}
	public void setSCompanyID(String companyID) {
		SCompanyID = companyID;
	}
	public String getWorkFlowNode() {
		return workFlowNode;
	}
	public void setWorkFlowNode(String workFlowNode) {
		this.workFlowNode = workFlowNode;
	}
	public String getWorkFlowNodeName() {
		return workFlowNodeName;
	}
	public void setWorkFlowNodeName(String workFlowNodeName) {
		this.workFlowNodeName = workFlowNodeName;
	}
	public List<AccDetailBean> getBeanDet() {
		return beanDet;
	}
	public void setBeanDet(List<AccDetailBean> beanDet) {
		this.beanDet = beanDet;
	}
	
	
	
	
}
