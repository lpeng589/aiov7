package com.koron.oa.bean;


/**
 * 
 * <p>Title:流程审批记录</p> 
 * <p>Description: </p>
 *
 * @Date:Dec 18, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 * @preserve all
 */
public class OAMyWorkFlowDetBean {
	String nodeID;			/*审批节点*/
	String checkPerson;		/*审批人*/
	String workFlowNode;	/*下一审批节点*/
	String checkPersons;	/*下一审批人*/
	String startTime;		/*办理开始时间*/
	String endTime;			/*办理完成时间*/
	String oaTimeLimitUnit;	/*限制时间 单位*/
	String benchmarkTime;	/*限制时间*/
	String nodeType;		/*审批类型 转交 回退，撤回*/
	String f_ref;			/*工作流记录Id*/
	String attTime;			/*补意见时间*/
	String approvalOpinions;/*审批意见*/
	String affix;			/*附件*/
	String createBy ;		/*创建人*/
	String statusId ;		/*0=转交 1=(撤回，回退等动作)*/
	
	
	public String getApprovalOpinions() {
		return approvalOpinions;
	}
	public void setApprovalOpinions(String approvalOpinions) {
		this.approvalOpinions = approvalOpinions;
	}
	
	public String getF_ref() {
		return f_ref;
	}
	public void setF_ref(String f_ref) {
		this.f_ref = f_ref;
	}
	
	public String getBenchmarkTime() {
		return benchmarkTime;
	}
	public void setBenchmarkTime(String benchmarkTime) {
		this.benchmarkTime = benchmarkTime;
	}
	
	
	public String getCheckPerson() {
		return checkPerson;
	}
	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}
	
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getNodeID() {
		return nodeID;
	}
	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getOaTimeLimitUnit() {
		return oaTimeLimitUnit;
	}
	public void setOaTimeLimitUnit(String oaTimeLimitUnit) {
		this.oaTimeLimitUnit = oaTimeLimitUnit;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getWorkFlowNode() {
		return workFlowNode;
	}
	public void setWorkFlowNode(String workFlowNode) {
		this.workFlowNode = workFlowNode;
	}
	public String getCheckPersons() {
		return checkPersons;
	}
	public void setCheckPersons(String checkPersons) {
		this.checkPersons = checkPersons;
	}
	public String getAttTime() {
		return attTime;
	}
	public void setAttTime(String attTime) {
		this.attTime = attTime;
	}
	public String getAffix() {
		return affix;
	}
	public void setAffix(String affix) {
		this.affix = affix;
	}
	
}
