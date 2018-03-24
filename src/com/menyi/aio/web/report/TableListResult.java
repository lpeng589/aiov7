package com.menyi.aio.web.report;

public class TableListResult {

	private String rowDis; //cols[0]=rowDis; 用于显示的所有数据

	private String keyId; //cols[1]=values.get("keyId")==null?"1":values.get("keyId").toString(); 单据代号

	private String createBy; //cols[7]=values.get("createBy")==null?"":values.get("createBy").toString();

	private String deptCodeR; //cols[8]=values.get("deptCodeR")==null?"":values.get("deptCodeR").toString(); 创建人部门

	private String f_brother; //cols[6]=values.get("f_brother")==null?"":values.get("f_brother").toString();

	private String classCode; //    cols[2]=values.get("classCode").toString();

	private String childCount; //    cols[3]=values.get("childCount").toString()==""?"0":values.get("childCount").toString();

	private String workFlowNode; //cols[9] =values.get("workFlowNode");

	private String workFlowNodeName; //cols[10]= values.get("workFlowNodeName") ;

	private String checkPersons; //cols[11]= values.get("checkPersons")==null?"":values.get("checkPersons") ;

	private String lastNodeID; //cols[12]=values.get("lastNodeID"); 审核流最后一个结点

	private String employeeId; //cols[13]=values.get("employeeId"); 
	
	private String deptCodeRE; //xxx=values.get("deptCodeRE")==null?"":values.get("deptCodeRE").toString(); 经手人部门

	public String getCheckPersons() {
		return checkPersons;
	}

	public void setCheckPersons(String checkPersons) {
		this.checkPersons = checkPersons;
	}

	public String getChildCount() {
		return childCount;
	}

	public void setChildCount(String childCount) {
		this.childCount = childCount;
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

	public String getDeptCodeR() {
		return deptCodeR;
	}

	public void setDeptCodeR(String deptCodeR) {
		this.deptCodeR = deptCodeR;
	}

	public String getDeptCodeRE() {
		return deptCodeRE;
	}

	public void setDeptCodeRE(String deptCodeRE) {
		this.deptCodeRE = deptCodeRE;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getF_brother() {
		return f_brother;
	}

	public void setF_brother(String f_brother) {
		this.f_brother = f_brother;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getLastNodeID() {
		return lastNodeID;
	}

	public void setLastNodeID(String lastNodeID) {
		this.lastNodeID = lastNodeID;
	}

	public String getRowDis() {
		return rowDis;
	}

	public void setRowDis(String rowDis) {
		this.rowDis = rowDis;
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
	
	
	
	
}
