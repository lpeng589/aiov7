package com.koron.oa.bean;

/**
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:Feb 22, 2012
 * @Copyright: 科荣软件
 * @Author 周新宇
 * @preserve all
 */
public class MyWorkFlow {

	private String billNo ; //单据编号
	private String applyDate ;//申请日期
	private String applyBy ; //申请人
	private String createBy ;//创建者
	private String checkPerson ;//审核人
	private String department ;//部门
	private String applyType ;//申请类型
	private String templateName;//流程名称
	private String noteType ;//结点类型
	private String flowName ;
	private int orderIndex ;//顺审顺序
	private String hasChecked ;//全审时，每个人是否已审
	private String billId ;//单据ID
	private String companyCode ;//往来单位
	private String sCompanyID ;//分支机构
	private String billDate;//单据日期
	private String applyContent ;//申请内容
	private String workFlowNode ;//结点
	private String workFlowNodeName ;//结点名称
	private String currentNode ;//当前是否是审核结点
	private String flowType ; //data=数据工作流，file=文件工作流
	private String nextNodeIds;
	private String tableName;
	private String departmentCode;
	private int oaTimeLimitUnit;
	private float benchmarkTime;
	private String lastCheckPerson;//上一结点审核人
	private String lastNodeId;
	private String lastNodeName;
	private String lastNodeCheckperson;
	private String cancelNodeId;
	private String currNodeDetID;
	private String startTime;//明细表中的开始节点
	private String endTime;  //明细表中的结束节点
	private String lastUpdateTime;
	private String setType;
	private String createTime;
	private String flowDepict;
	private String flowDepictTitle;
	private String id;
	private String currNodeName ;
	private String checkPersonName ;
	private String workFlowType;
	private String finishTime;
	
	private String parentTableName;
	
	private String allApprovePerson=""; //启用全审时，记录归些人审核过
	
	
	
	public String getParentTableName() {
		return parentTableName;
	}
	public void setParentTableName(String parentTableName) {
		this.parentTableName = parentTableName;
	}
	public String getAllApprovePerson() {
		return allApprovePerson;
	}
	public void setAllApprovePerson(String allApprovePerson) {
		this.allApprovePerson = allApprovePerson;
	}
	public String getWorkFlowType() {
		return workFlowType;
	}
	public void setWorkFlowType(String workFlowType) {
		this.workFlowType = workFlowType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFlowDepictTitle() {
		return flowDepictTitle;
	}
	public void setFlowDepictTitle(String flowDepictTitle) {
		this.flowDepictTitle = flowDepictTitle;
	}
	public String getFlowDepict() {
		return flowDepict;
	}
	public void setFlowDepict(String flowDepict) {
		this.flowDepict = flowDepict;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getSetType() {
		return setType;
	}
	public void setSetType(String setType) {
		this.setType = setType;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getCurrNodeDetID() {
		return currNodeDetID;
	}
	public void setCurrNodeDetID(String currNodeDetID) {
		this.currNodeDetID = currNodeDetID;
	}
	public String getLastCheckPerson() {
		return lastCheckPerson;
	}
	public void setLastCheckPerson(String lastCheckPerson) {
		this.lastCheckPerson = lastCheckPerson;
	}
	public float getBenchmarkTime() {
		return benchmarkTime;
	}
	public void setBenchmarkTime(float benchmarkTime) {
		this.benchmarkTime = benchmarkTime;
	}
	public int getOaTimeLimitUnit() {
		return oaTimeLimitUnit;
	}
	public void setOaTimeLimitUnit(int oaTimeLimitUnit) {
		this.oaTimeLimitUnit = oaTimeLimitUnit;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getNextNodeIds() {
		return nextNodeIds;
	}
	public void setNextNodeIds(String nextNodeIds) {
		this.nextNodeIds = nextNodeIds;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	
	public String getCheckPerson() {
		return checkPerson;
	}
	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
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
	public String getApplyBy() {
		return applyBy;
	}
	public void setApplyBy(String applyBy) {
		this.applyBy = applyBy;
	}
	public String getApplyContent() {
		return applyContent;
	}
	public void setApplyContent(String applyContent) {
		this.applyContent = applyContent;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getApplyType() {
		return applyType;
	}
	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getNoteType() {
		return noteType;
	}
	public void setNoteType(String noteType) {
		this.noteType = noteType;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getSCompanyID() {
		return sCompanyID;
	}
	public void setSCompanyID(String companyID) {
		sCompanyID = companyID;
	}
	public int getOrderIndex() {
		return orderIndex;
	}
	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}
	public String getHasChecked() {
		return hasChecked;
	}
	public void setHasChecked(String hasChecked) {
		this.hasChecked = hasChecked;
	}
	public String getCurrentNode() {
		return currentNode;
	}
	public void setCurrentNode(String currentNode) {
		this.currentNode = currentNode;
	}
	public String getFlowType() {
		return flowType;
	}
	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}
	public String getCancelNodeId() {
		return cancelNodeId;
	}
	public void setCancelNodeId(String cancelNodeId) {
		this.cancelNodeId = cancelNodeId;
	}
	public String getLastNodeId() {
		return lastNodeId;
	}
	public void setLastNodeId(String lastNodeId) {
		this.lastNodeId = lastNodeId;
	}
	public String getCurrNodeName() {
		return currNodeName;
	}
	public void setCurrNodeName(String currNodeName) {
		this.currNodeName = currNodeName;
	}
	public String getCheckPersonName() {
		return checkPersonName;
	}
	public void setCheckPersonName(String checkPersonName) {
		this.checkPersonName = checkPersonName;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public String getLastNodeName() {
		return lastNodeName;
	}
	public void setLastNodeName(String lastNodeName) {
		this.lastNodeName = lastNodeName;
	}
	public String getLastNodeCheckperson() {
		return lastNodeCheckperson;
	}
	public void setLastNodeCheckperson(String lastNodeCheckperson) {
		this.lastNodeCheckperson = lastNodeCheckperson;
	}
	
}
