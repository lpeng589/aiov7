package com.koron.oa.bean;

/**
 * �������ļ�ʵ����
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Sep 3, 2010
 * @Copyright: �������
 * @Author ��СǮ
 */
public class OAWorkFlowFileBean {

	private String id ;						/*�ļ�Id*/
	private String templateId;				/*ģ��Id*/
	private String workFlowList;			/*������ϸ*/
	private int    fileNumber;				/*�ļ����*/
	private String fileName ;				/*�ļ�����*/
	private String createBy;				/*������ID*/
	private String createName ;				/*����������*/
	private String createTime ;				/*����ʱ��*/
	private String department;				/*����*/
	private String topic;					/*����������*/
	private String flowVariable;			/*���̱���*/
	private String flowVestige ;			/*���̹켣*/
	private String workFlowNode ;			/*��ǰ���*/
	private String workFlowNodeName ;		/*��ǰ�������*/
	private String workFlowNodeStatus ;		/*��ǰ������״̬*/
	private String checkPersons ;			/*�����*/
	private String hasChecked ;				/*ȫ��ʱ �Ѿ�����˵���*/
	private int    orderChecked ;			/*˳��ʱ ���˳��*/
	private String htmlContent ;			/*HTMLģ������*/
	private String isDraft ;				/*�ݸ�*/
	private String readUserId ;				/*�Ѿ��İ��˵�ID*/
	private String signPersons;				/*��ǩ��Ա*/
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getWorkFlowList() {
		return workFlowList;
	}
	public void setWorkFlowList(String workFlowList) {
		this.workFlowList = workFlowList;
	}
	public int getFileNumber() {
		return fileNumber;
	}
	public void setFileNumber(int fileNumber) {
		this.fileNumber = fileNumber;
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
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public String getFlowVariable() {
		return flowVariable;
	}
	public void setFlowVariable(String flowVariable) {
		this.flowVariable = flowVariable;
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
	public String getWorkFlowNodeStatus() {
		return workFlowNodeStatus;
	}
	public void setWorkFlowNodeStatus(String workFlowNodeStatus) {
		this.workFlowNodeStatus = workFlowNodeStatus;
	}
	public String getCheckPersons() {
		return checkPersons;
	}
	public void setCheckPersons(String checkPersons) {
		this.checkPersons = checkPersons;
	}
	public String getHasChecked() {
		return hasChecked;
	}
	public void setHasChecked(String hasChecked) {
		this.hasChecked = hasChecked;
	}
	public int getOrderChecked() {
		return orderChecked;
	}
	public void setOrderChecked(int orderChecked) {
		this.orderChecked = orderChecked;
	}
	public String getFlowVestige() {
		return flowVestige;
	}
	public void setFlowVestige(String flowVestige) {
		this.flowVestige = flowVestige;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public String getHtmlContent() {
		return htmlContent;
	}
	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getIsDraft() {
		return isDraft;
	}
	public void setIsDraft(String isDraft) {
		this.isDraft = isDraft;
	}
	public String getReadUserId() {
		return readUserId;
	}
	public void setReadUserId(String readUserId) {
		this.readUserId = readUserId;
	}
	public String getSignPersons() {
		return signPersons;
	}
	public void setSignPersons(String signPersons) {
		this.signPersons = signPersons;
	}
}
