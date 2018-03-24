package com.koron.oa.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * <p>Title:工作流模板</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-13
 * @Copyright: 科荣软件
 * @Author 文小钱
 * @preserve all
 */
@Entity
@Table(name="OAWorkFlowTemplate")
public class OAWorkFlowTemplate {
	
	@Id
	private String id ;
	private String templateName;	//模板名称
	private int templateType;		//模板类型
	private String designType;		//模板类型 自定义表单还是个性化表单
	private String templateClass;	//模板分类
	private String templateFile;	//模板数据库物理表名	
	private String workFlowFile;	//流程文件
	private String allowVisitor;	//OA工作流的目标用户 | ERP,CRM,HR工作流的反审核人
	private int templateStatus;		//模板状态
	private String defStatus;		//默认查询状态
	private String wakeUp;			//提醒
	@Transient
	private int finishTime;			
	private int flowOrder;			//流程排序号
	@Transient
	private int readLimit;			
	private String detail;			//流程说明
	private String affix;			//说明附件
	private int statusId;			
	private String fileFinish;		
	@Column(name="depMonitorScope",nullable=false,columnDefinition="INT default 0")
	private int depMonitorScope;	//部门监控类型
	@Column(name="perMonitorScope",nullable=false,columnDefinition="INT default 0")
	private int perMonitorScope;	//人员监控类型
	private String depMonitor;		//部门监控范围
	private String perMonitor;		//人员监控范围
	private String titleTemp;		//模板主题
	private String dispenseWake;	//分发提醒
	private String nextWake;		//下一步经办人 提醒方式
	private String overTimeWake;		//超时 提醒方式
	private String startWake;		//发起人 提醒方式
	private String allWake;			//节点提醒 全部经办人 提醒方式
	private String setWake;			//节点提醒 以下指定人 提醒方式
	private String setWakeDept;		//节点提醒 以下指定人  部门范围
	private String setWakePer;		//节点提醒 以下指定人  人员范围
	private String setWakeGroup;	//节点提醒 以下指定人  分组
	private String stopStartWake;	//结束提醒  发起人 提醒方式
	private String stopSAllWake;	//结束提醒  全部经办人 提醒方式
	private String stopSetWake;		//结束提醒  以下指定人 提醒方式 
	private String stopSetWakeDept;	//结束提醒  以下指定人 部门范围
	private String stopSetWakePer;	//结束提醒  以下指定人 部门范围
	private String stopSetWakeGroup;//结束提醒  以下指定人 分组
	private String autoPass;		//发起人和紧邻步骤的审批人相同时自动略过
	private String applyLookFieldDisplay;
	private String usefulLifeS;		
	private String usefulLifeE;		
	@Transient
	private int allowAdd;			//过时是否允许添加
	private String retCheckPerRule;	
	private String retdefineName;	//反审核执行的define文件
	private String defineName;		//审核通过执行的define文件
	@Transient
	private int timeType;			//流程有效时段类型
	@Transient
	private String timeVal;			//流程有效时段指定值
	private String fileContent;		
	private String version;			//流程版本号
	private String sameFlow;		//开始创建流程的ID
	private String createTime;		//创建时间 
	private String updateBy;		//修改人
	private String lines ;          //新版工作流的xml结点位置线条
	private String reviewWake;		
	private String wakeLimitSQL;    //指定人限定范围
	
	private String parentTableName;
	
	
	
	public String getWakeLimitSQL() {
		return wakeLimitSQL;
	}
	public void setWakeLimitSQL(String wakeLimitSQL) {
		this.wakeLimitSQL = wakeLimitSQL;
	}
	public String getParentTableName() {
		return parentTableName;
	}
	public void setParentTableName(String parentTableName) {
		this.parentTableName = parentTableName;
	}
	public String getOverTimeWake() {
		return overTimeWake;
	}
	public void setOverTimeWake(String overTimeWake) {
		this.overTimeWake = overTimeWake;
	}
	public String getDefStatus() {
		return defStatus;
	}
	public void setDefStatus(String defStatus) {
		this.defStatus = defStatus;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public int getTemplateType() {
		return templateType;
	}
	public void setTemplateType(int templateType) {
		this.templateType = templateType;
	}
	public String getTemplateClass() {
		return templateClass;
	}
	public void setTemplateClass(String templateClass) {
		this.templateClass = templateClass;
	}
	public String getTemplateFile() {
		return templateFile;
	}
	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}
	public String getWorkFlowFile() {
		return workFlowFile;
	}
	public void setWorkFlowFile(String workFlowFile) {
		this.workFlowFile = workFlowFile;
	}
	public String getAllowVisitor() {
		return allowVisitor;
	}
	public void setAllowVisitor(String allowVisitor) {
		this.allowVisitor = allowVisitor;
	}
	public int getTemplateStatus() {
		return templateStatus;
	}
	public void setTemplateStatus(int templateStatus) {
		this.templateStatus = templateStatus;
	}
	public String getWakeUp() {
		return wakeUp;
	}
	public void setWakeUp(String wakeUp) {
		this.wakeUp = wakeUp;
	}
	public int getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(int finishTime) {
		this.finishTime = finishTime;
	}
	public int getFlowOrder() {
		return flowOrder;
	}
	public void setFlowOrder(int flowOrder) {
		this.flowOrder = flowOrder;
	}
	public int getReadLimit() {
		return readLimit;
	}
	public void setReadLimit(int readLimit) {
		this.readLimit = readLimit;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getAffix() {
		return affix;
	}
	public void setAffix(String affix) {
		this.affix = affix;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public String getFileFinish() {
		return fileFinish;
	}
	public void setFileFinish(String fileFinish) {
		this.fileFinish = fileFinish;
	}
	public int getDepMonitorScope() {
		return depMonitorScope;
	}
	public void setDepMonitorScope(int depMonitorScope) {
		this.depMonitorScope = depMonitorScope;
	}
	public int getPerMonitorScope() {
		return perMonitorScope;
	}
	public void setPerMonitorScope(int perMonitorScope) {
		this.perMonitorScope = perMonitorScope;
	}
	public String getDepMonitor() {
		return depMonitor;
	}
	public void setDepMonitor(String depMonitor) {
		this.depMonitor = depMonitor;
	}
	public String getPerMonitor() {
		return perMonitor;
	}
	public void setPerMonitor(String perMonitor) {
		this.perMonitor = perMonitor;
	}
	public String getTitleTemp() {
		return titleTemp;
	}
	public void setTitleTemp(String titleTemp) {
		this.titleTemp = titleTemp;
	}
	public String getNextWake() {
		return nextWake;
	}
	public void setNextWake(String nextWake) {
		this.nextWake = nextWake;
	}
	public String getStartWake() {
		return startWake;
	}
	public void setStartWake(String startWake) {
		this.startWake = startWake;
	}
	public String getAllWake() {
		return allWake;
	}
	public void setAllWake(String allWake) {
		this.allWake = allWake;
	}
	public String getSetWake() {
		return setWake;
	}
	public void setSetWake(String setWake) {
		this.setWake = setWake;
	}
	public String getSetWakeDept() {
		return setWakeDept;
	}
	public void setSetWakeDept(String setWakeDept) {
		this.setWakeDept = setWakeDept;
	}
	public String getSetWakePer() {
		return setWakePer;
	}
	public void setSetWakePer(String setWakePer) {
		this.setWakePer = setWakePer;
	}
	public String getSetWakeGroup() {
		return setWakeGroup;
	}
	public void setSetWakeGroup(String setWakeGroup) {
		this.setWakeGroup = setWakeGroup;
	}
	public String getStopStartWake() {
		return stopStartWake;
	}
	public void setStopStartWake(String stopStartWake) {
		this.stopStartWake = stopStartWake;
	}
	public String getStopSAllWake() {
		return stopSAllWake;
	}
	public void setStopSAllWake(String stopSAllWake) {
		this.stopSAllWake = stopSAllWake;
	}
	public String getStopSetWake() {
		return stopSetWake;
	}
	public void setStopSetWake(String stopSetWake) {
		this.stopSetWake = stopSetWake;
	}
	public String getStopSetWakeDept() {
		return stopSetWakeDept;
	}
	public void setStopSetWakeDept(String stopSetWakeDept) {
		this.stopSetWakeDept = stopSetWakeDept;
	}
	public String getStopSetWakePer() {
		return stopSetWakePer;
	}
	public void setStopSetWakePer(String stopSetWakePer) {
		this.stopSetWakePer = stopSetWakePer;
	}
	public String getStopSetWakeGroup() {
		return stopSetWakeGroup;
	}
	public void setStopSetWakeGroup(String stopSetWakeGroup) {
		this.stopSetWakeGroup = stopSetWakeGroup;
	}
	public String getAutoPass() {
		return autoPass;
	}
	public void setAutoPass(String autoPass) {
		this.autoPass = autoPass;
	}
	public String getApplyLookFieldDisplay() {
		return applyLookFieldDisplay;
	}
	public void setApplyLookFieldDisplay(String applyLookFieldDisplay) {
		this.applyLookFieldDisplay = applyLookFieldDisplay;
	}
	public String getUsefulLifeS() {
		return usefulLifeS;
	}
	public void setUsefulLifeS(String usefulLifeS) {
		this.usefulLifeS = usefulLifeS;
	}
	public String getUsefulLifeE() {
		return usefulLifeE;
	}
	public void setUsefulLifeE(String usefulLifeE) {
		this.usefulLifeE = usefulLifeE;
	}
	public int getAllowAdd() {
		return allowAdd;
	}
	public void setAllowAdd(int allowAdd) {
		this.allowAdd = allowAdd;
	}
	public String getRetCheckPerRule() {
		return retCheckPerRule;
	}
	public void setRetCheckPerRule(String retCheckPerRule) {
		this.retCheckPerRule = retCheckPerRule;
	}
	public String getRetdefineName() {
		return retdefineName;
	}
	public void setRetdefineName(String retdefineName) {
		this.retdefineName = retdefineName;
	}
	public String getDefineName() {
		return defineName;
	}
	public void setDefineName(String defineName) {
		this.defineName = defineName;
	}
	public int getTimeType() {
		return timeType;
	}
	public void setTimeType(int timeType) {
		this.timeType = timeType;
	}
	public String getTimeVal() {
		return timeVal;
	}
	public void setTimeVal(String timeVal) {
		this.timeVal = timeVal;
	}
	public String getFileContent() {
		return fileContent;
	}
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getSameFlow() {
		return sameFlow;
	}
	public void setSameFlow(String sameFlow) {
		this.sameFlow = sameFlow;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getDispenseWake() {
		return dispenseWake;
	}
	public void setDispenseWake(String dispenseWake) {
		this.dispenseWake = dispenseWake;
	}
	public String getReviewWake() {
		return reviewWake;
	}
	public void setReviewWake(String reviewWake) {
		this.reviewWake = reviewWake;
	}
	public String getDesignType() {
		return designType;
	}
	public void setDesignType(String designType) {
		this.designType = designType;
	}
	public String getLines() {
		return lines;
	}
	public void setLines(String lines) {
		this.lines = lines;
	}
	
}
