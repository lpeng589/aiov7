package com.koron.oa.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.dom4j.tree.AbstractEntity;

import com.google.gson.annotations.Expose;
/**
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Nov 25, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 * @preserve all
 */
@Entity
@Table(name="OAWorkFlowNode")
public class FlowNodeBean  extends AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = -4722778268431977994L;
	@Id
	private String id = "";
	@Expose
	private String flowId;				//流程版本Id
	@Expose
	private String keyId;				//结点Id
	@Expose
	private String zAction = "";		//结点类型
	@Expose
	private String display = "";		//结点名称
	@Expose
	@Column(name="[to]")
	private String to = "";				//下个结点
	
	@Expose
	private boolean allowBack;			//允许回退
	@Expose
	private boolean allowStop;			//允许结束
	@Expose
	private boolean allowJump;			//允许滤过
	@Expose
	private boolean allowCancel;		//允许撤回
	@Expose
	private boolean useAllApprove;		//允许全审
	@Expose
	private boolean ideaRequired ;		/*会签意见必填*/
	
	@Expose
	private boolean forwardTime;		//转交时设置办理时限
	@Expose
	private float timeLimit;			//办理时限
	@Expose
	private float noteTime;				//提前提醒
	@Expose
	private float noteRate;				//超时提醒频率
	@Expose
	private float noteTimeUnit;			//提前提醒 类型
	@Expose
	private float noteRateUnit;			//超时提醒频率 类型
	@Expose
	private float timeLimitUnit;		//办理时限 类型
	
	@Expose
	private float limitMinute=0;		//
	@Expose
	private float rateMinute=0;			//
	@Expose
	private float awokeMinute=0;		//
	
	@Expose
	private String passExec = "";		//执行通过时的define文件
	@Expose
	private String backExec = "";		//执行回退时的define文件
	@Expose
	private String stopExec = "";		//执行结束时的define文件
	
	@Expose
	private int filterSet;//选人过滤规则0：全部经办人1：本部门经办人2：上级部门经办人3：下级部门经办人4：一级部门经办人，5：自定义过滤规则
	@Expose
	private int autoSelectPeople;//自动选人规则0：不自动选人1：自动选择流程发起人，2，自动选择直属上师，3：自定义选人
	
	@Expose
	private String filterSetSQL;//自定义过滤规则SQL
	@Expose
	private String autoSelectPeopleSQL;//自定义选人SQL
	
	@Transient
	public final static int FILTER_ALL=0;
	@Transient
	public final static int FILTER_OWNERDEPT=1;
	@Transient
	public final static int FILTER_UPPERDEPT=2;
	@Transient
	public final static int FILTER_LOWERDEPT=3;
	@Transient
	public final static int FILTER_FIRSTDEPT=4;
	@Transient
	public final static int FILTER_DEFINESQL=5;//过滤自定义审核人
	@Transient
	public final static int FILTER_DEFINESQLSELONLY=6;//只选择自定义审核人
	@Transient
	public final static int FILTER_NOOWNERDEPT=7;//不选择本部门及子部门审核人
	@Transient
	public final static int FILTER_NOCREATER=8;//不选择制单人
	@Transient
	public final static int FILTER_OWNERDEPTUPPERDEPT=9;//只选择本部门及上级部门审核人
	@Expose
	private String approvePeople = "";		//允许选择审批人:select	固定审批人:fix
	@OneToMany(mappedBy="flowNode",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private Set<FieldBean> fieldSet = new HashSet<FieldBean>();
	@OneToMany(mappedBy="flowNode2",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private Set<ApproveBean> approveSet = new HashSet<ApproveBean>() ;
	@OneToMany(mappedBy="flowNode3",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private Set<ConditionsBean> conditionSet = new HashSet<ConditionsBean>();
	@Transient
	@Expose
	private ArrayList<String> hiddenFields = new ArrayList<String>();		//流程结束后 显示哪些隐藏字段
	@Expose
	@Transient
	private ArrayList<String[]> checkPeople = new ArrayList<String[]>();
	@Expose
	@Transient
	private List<FieldBean> fields = new ArrayList<FieldBean>();		//结点 字段设置  隐藏:hidden  必填:notnull	只读:readOnly
	@Expose
	@Transient
	private List<ApproveBean> approvers = new ArrayList<ApproveBean>();			//结点审批人
	@Expose
	@Transient
	private List<ConditionsBean> conditionList = new ArrayList<ConditionsBean>();		//条件判断
	
	@Expose
	boolean standaloneNoteSet;	//是否设置单独提醒方式
	@Expose
	boolean nextSMS;			//下一步经办人 通知提醒
	@Expose
	boolean nextMobile;			//下一步经办人 手机提醒
	@Expose
	boolean nextMail;			//下一步经办人 邮件提醒
	@Expose
	boolean startSMS;			//发起人经办人 通知提醒
	@Expose
	boolean startMobile;		//发起人经办人 手机提醒
	@Expose
	boolean startMail;			//发起人经办人 邮件提醒
	@Expose
	boolean allSMS;				//全部经办人 通知提醒
	@Expose
	boolean allMobile;			//全部经办人 手机提醒
	@Expose
	boolean allMail;			//全部经办人 邮件提醒
	@Expose
	boolean setSMS;				//以下指定经办人 通知提醒
	@Expose
	boolean setMobile;			//以下指定经办人 手机提醒
	@Expose
	boolean setMail;			//以下指定经办人 邮件提醒
	@Expose
	@Transient
	ArrayList<ApproveBean> notePeople = new ArrayList<ApproveBean>();	//以下指定经办人
	
	
	
	
	public String getAutoSelectPeopleSQL() {
		return autoSelectPeopleSQL;
	}
	public void setAutoSelectPeopleSQL(String autoSelectPeopleSQL) {
		this.autoSelectPeopleSQL = autoSelectPeopleSQL;
	}
	public String getFilterSetSQL() {
		return filterSetSQL;
	}
	public void setFilterSetSQL(String filterSetSQL) {
		this.filterSetSQL = filterSetSQL;
	}
	public Set<FieldBean> getFieldSet() {
		return fieldSet;
	}
	public void setFieldSet(Set<FieldBean> fieldSet) {
		this.fieldSet = fieldSet;
	}
	public Set<ApproveBean> getApproveSet() {
		return approveSet;
	}
	public void setApproveSet(Set<ApproveBean> approveSet) {
		this.approveSet = approveSet;
	}
	public Set<ConditionsBean> getConditionSet() {
		return conditionSet;
	}
	public void setConditionSet(Set<ConditionsBean> conditionSet) {
		this.conditionSet = conditionSet;
	}
	//时间限制的单位 0：天 1：小时 2:分钟
	public boolean isAllowBack() {
		return allowBack;
	}
	public void setAllowBack(boolean allowBack) {
		this.allowBack = allowBack;
	}
	public boolean isAllowCancel() {
		return allowCancel;
	}
	public void setAllowCancel(boolean allowCancel) {
		this.allowCancel = allowCancel;
	}
	public boolean isAllowJump() {
		return allowJump;
	}
	public void setAllowJump(boolean allowJump) {
		this.allowJump = allowJump;
	}
	public boolean isAllowStop() {
		return allowStop;
	}
	public void setAllowStop(boolean allowStop) {
		this.allowStop = allowStop;
	}
	public String getApprovePeople() {
		return approvePeople;
	}
	public void setApprovePeople(String approvePeople) {
		this.approvePeople = approvePeople;
	}
	
	public List<ApproveBean> getApprovers() {
		return approvers;
	}
	public void setApprovers(List<ApproveBean> approvers) {
		this.approvers = approvers;
	}
	public List<ConditionsBean> getConditionList() {
		return conditionList;
	}
	public void setConditionList(List<ConditionsBean> conditionList) {
		this.conditionList = conditionList;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public List<FieldBean> getFields() {
		return fields;
	}
	public void setFields(List<FieldBean> fields) {
		this.fields = fields;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public float getNoteRate() {
		return noteRate;
	}
	public void setNoteRate(float noteRate) {
		this.noteRate = noteRate;
	}
	public float getNoteRateUnit() {
		return noteRateUnit;
	}
	public void setNoteRateUnit(float noteRateUnit) {
		this.noteRateUnit = noteRateUnit;
	}
	public float getNoteTime() {
		return noteTime;
	}
	public void setNoteTime(float noteTime) {
		this.noteTime = noteTime;
	}
	public float getNoteTimeUnit() {
		return noteTimeUnit;
	}
	public void setNoteTimeUnit(float noteTimeUnit) {
		this.noteTimeUnit = noteTimeUnit;
	}

	public float getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(float timeLimit) {
		this.timeLimit = timeLimit;
	}
	public float getTimeLimitUnit() {
		return timeLimitUnit;
	}
	public void setTimeLimitUnit(float timeLimitUnit) {
		this.timeLimitUnit = timeLimitUnit;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public boolean isUseAllApprove() {
		return useAllApprove;
	}
	public void setUseAllApprove(boolean useAllApprove) {
		this.useAllApprove = useAllApprove;
	}
	public String getZAction() {
		return zAction;
	}
	public void setZAction(String action) {
		zAction = action;
	}
	public float getAwokeMinute() {
		return awokeMinute;
	}
	public void setAwokeMinute(float awokeMinute) {
		this.awokeMinute = awokeMinute;
	}
	public float getLimitMinute() {
		return limitMinute;
	}
	public void setLimitMinute(float limitMinute) {
		this.limitMinute = limitMinute;
	}
	public float getRateMinute() {
		return rateMinute;
	}
	public void setRateMinute(float rateMinute) {
		this.rateMinute = rateMinute;
	}
	
	/*
	 * 将所有时间转换成提醒分钟
	 */
	public void transMinute(){
		//转换限制时间
		if(timeLimitUnit==0){
			limitMinute=timeLimit*24*60;
		}else if(timeLimitUnit==1){
			limitMinute=timeLimit*60;
		}else{
			limitMinute=timeLimit;
		}
		
		//转换提前提醒时间
		if(noteTimeUnit==0){
			awokeMinute=noteTime*24*60;
		}else if(noteTimeUnit==1){
			awokeMinute=noteTime*60;
		}else{
			awokeMinute=noteTime;
		}
		
		//转换超时提醒频率
		if(noteRateUnit==0){
			rateMinute=noteRate*24*60;
		}else if(noteRateUnit==1){
			rateMinute=noteRate*60;
		}else{
			rateMinute=noteRate;
		}
	}
	public String getPassExec() {
		return passExec;
	}
	public void setPassExec(String passExec) {
		this.passExec = passExec;
	}
	public String getBackExec() {
		return backExec;
	}
	public void setBackExec(String backExec) {
		this.backExec = backExec;
	}
	public String getStopExec() {
		return stopExec;
	}
	public void setStopExec(String stopExec) {
		this.stopExec = stopExec;
	}
	public int getFilterSet() {
		return filterSet;
	}
	public void setFilterSet(int filterSet) {
		this.filterSet = filterSet;
	}
	public int getAutoSelectPeople() {
		return autoSelectPeople;
	}
	public void setAutoSelectPeople(int autoSelectPeople) {
		this.autoSelectPeople = autoSelectPeople;
	}
	public boolean isForwardTime() {
		return forwardTime;
	}
	public void setForwardTime(boolean forwardTime) {
		this.forwardTime = forwardTime;
	}
	public boolean isAllMail() {
		return allMail;
	}
	public void setAllMail(boolean allMail) {
		this.allMail = allMail;
	}
	public boolean isAllMobile() {
		return allMobile;
	}
	public void setAllMobile(boolean allMobile) {
		this.allMobile = allMobile;
	}
	public boolean isAllSMS() {
		return allSMS;
	}
	public void setAllSMS(boolean allSMS) {
		this.allSMS = allSMS;
	}
	public boolean isNextMail() {
		return nextMail;
	}
	public void setNextMail(boolean nextMail) {
		this.nextMail = nextMail;
	}
	public boolean isNextMobile() {
		return nextMobile;
	}
	public void setNextMobile(boolean nextMobile) {
		this.nextMobile = nextMobile;
	}
	public boolean isNextSMS() {
		return nextSMS;
	}
	public void setNextSMS(boolean nextSMS) {
		this.nextSMS = nextSMS;
	}
	public boolean isSetMail() {
		return setMail;
	}
	public void setSetMail(boolean setMail) {
		this.setMail = setMail;
	}
	public boolean isSetMobile() {
		return setMobile;
	}
	public void setSetMobile(boolean setMobile) {
		this.setMobile = setMobile;
	}
	public boolean isSetSMS() {
		return setSMS;
	}
	public void setSetSMS(boolean setSMS) {
		this.setSMS = setSMS;
	}
	public boolean isStandaloneNoteSet() {
		return standaloneNoteSet;
	}
	public void setStandaloneNoteSet(boolean standaloneNoteSet) {
		this.standaloneNoteSet = standaloneNoteSet;
	}
	public boolean isStartMail() {
		return startMail;
	}
	public void setStartMail(boolean startMail) {
		this.startMail = startMail;
	}
	public boolean isStartMobile() {
		return startMobile;
	}
	public void setStartMobile(boolean startMobile) {
		this.startMobile = startMobile;
	}
	public boolean isStartSMS() {
		return startSMS;
	}
	public void setStartSMS(boolean startSMS) {
		this.startSMS = startSMS;
	}
	public ArrayList<ApproveBean> getNotePeople() {
		return notePeople;
	}
	public void setNotePeople(ArrayList<ApproveBean> notePeople) {
		this.notePeople = notePeople;
	}
	public ArrayList<String> getHiddenFields() {
		return hiddenFields;
	}
	public void setHiddenFields(ArrayList<String> hiddenFields) {
		this.hiddenFields = hiddenFields;
	}
	public boolean getIdeaRequired() {
		return ideaRequired;
	}
	public void setIdeaRequired(boolean ideaRequired) {
		this.ideaRequired = ideaRequired;
	}
	public ArrayList<String[]> getCheckPeople() {
		return checkPeople;
	}
	public void setCheckPeople(ArrayList<String[]> checkPeople) {
		this.checkPeople = checkPeople;
	}
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	
	
}
