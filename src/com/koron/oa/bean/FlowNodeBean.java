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
 * @Copyright: �����п���������޹�˾
 * @Author ǮС��
 * @preserve all
 */
@Entity
@Table(name="OAWorkFlowNode")
public class FlowNodeBean  extends AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = -4722778268431977994L;
	@Id
	private String id = "";
	@Expose
	private String flowId;				//���̰汾Id
	@Expose
	private String keyId;				//���Id
	@Expose
	private String zAction = "";		//�������
	@Expose
	private String display = "";		//�������
	@Expose
	@Column(name="[to]")
	private String to = "";				//�¸����
	
	@Expose
	private boolean allowBack;			//�������
	@Expose
	private boolean allowStop;			//�������
	@Expose
	private boolean allowJump;			//�����˹�
	@Expose
	private boolean allowCancel;		//������
	@Expose
	private boolean useAllApprove;		//����ȫ��
	@Expose
	private boolean ideaRequired ;		/*��ǩ�������*/
	
	@Expose
	private boolean forwardTime;		//ת��ʱ���ð���ʱ��
	@Expose
	private float timeLimit;			//����ʱ��
	@Expose
	private float noteTime;				//��ǰ����
	@Expose
	private float noteRate;				//��ʱ����Ƶ��
	@Expose
	private float noteTimeUnit;			//��ǰ���� ����
	@Expose
	private float noteRateUnit;			//��ʱ����Ƶ�� ����
	@Expose
	private float timeLimitUnit;		//����ʱ�� ����
	
	@Expose
	private float limitMinute=0;		//
	@Expose
	private float rateMinute=0;			//
	@Expose
	private float awokeMinute=0;		//
	
	@Expose
	private String passExec = "";		//ִ��ͨ��ʱ��define�ļ�
	@Expose
	private String backExec = "";		//ִ�л���ʱ��define�ļ�
	@Expose
	private String stopExec = "";		//ִ�н���ʱ��define�ļ�
	
	@Expose
	private int filterSet;//ѡ�˹��˹���0��ȫ��������1�������ž�����2���ϼ����ž�����3���¼����ž�����4��һ�����ž����ˣ�5���Զ�����˹���
	@Expose
	private int autoSelectPeople;//�Զ�ѡ�˹���0�����Զ�ѡ��1���Զ�ѡ�����̷����ˣ�2���Զ�ѡ��ֱ����ʦ��3���Զ���ѡ��
	
	@Expose
	private String filterSetSQL;//�Զ�����˹���SQL
	@Expose
	private String autoSelectPeopleSQL;//�Զ���ѡ��SQL
	
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
	public final static int FILTER_DEFINESQL=5;//�����Զ��������
	@Transient
	public final static int FILTER_DEFINESQLSELONLY=6;//ֻѡ���Զ��������
	@Transient
	public final static int FILTER_NOOWNERDEPT=7;//��ѡ�񱾲��ż��Ӳ��������
	@Transient
	public final static int FILTER_NOCREATER=8;//��ѡ���Ƶ���
	@Transient
	public final static int FILTER_OWNERDEPTUPPERDEPT=9;//ֻѡ�񱾲��ż��ϼ����������
	@Expose
	private String approvePeople = "";		//����ѡ��������:select	�̶�������:fix
	@OneToMany(mappedBy="flowNode",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private Set<FieldBean> fieldSet = new HashSet<FieldBean>();
	@OneToMany(mappedBy="flowNode2",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private Set<ApproveBean> approveSet = new HashSet<ApproveBean>() ;
	@OneToMany(mappedBy="flowNode3",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private Set<ConditionsBean> conditionSet = new HashSet<ConditionsBean>();
	@Transient
	@Expose
	private ArrayList<String> hiddenFields = new ArrayList<String>();		//���̽����� ��ʾ��Щ�����ֶ�
	@Expose
	@Transient
	private ArrayList<String[]> checkPeople = new ArrayList<String[]>();
	@Expose
	@Transient
	private List<FieldBean> fields = new ArrayList<FieldBean>();		//��� �ֶ�����  ����:hidden  ����:notnull	ֻ��:readOnly
	@Expose
	@Transient
	private List<ApproveBean> approvers = new ArrayList<ApproveBean>();			//���������
	@Expose
	@Transient
	private List<ConditionsBean> conditionList = new ArrayList<ConditionsBean>();		//�����ж�
	
	@Expose
	boolean standaloneNoteSet;	//�Ƿ����õ������ѷ�ʽ
	@Expose
	boolean nextSMS;			//��һ�������� ֪ͨ����
	@Expose
	boolean nextMobile;			//��һ�������� �ֻ�����
	@Expose
	boolean nextMail;			//��һ�������� �ʼ�����
	@Expose
	boolean startSMS;			//�����˾����� ֪ͨ����
	@Expose
	boolean startMobile;		//�����˾����� �ֻ�����
	@Expose
	boolean startMail;			//�����˾����� �ʼ�����
	@Expose
	boolean allSMS;				//ȫ�������� ֪ͨ����
	@Expose
	boolean allMobile;			//ȫ�������� �ֻ�����
	@Expose
	boolean allMail;			//ȫ�������� �ʼ�����
	@Expose
	boolean setSMS;				//����ָ�������� ֪ͨ����
	@Expose
	boolean setMobile;			//����ָ�������� �ֻ�����
	@Expose
	boolean setMail;			//����ָ�������� �ʼ�����
	@Expose
	@Transient
	ArrayList<ApproveBean> notePeople = new ArrayList<ApproveBean>();	//����ָ��������
	
	
	
	
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
	//ʱ�����Ƶĵ�λ 0���� 1��Сʱ 2:����
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
	 * ������ʱ��ת�������ѷ���
	 */
	public void transMinute(){
		//ת������ʱ��
		if(timeLimitUnit==0){
			limitMinute=timeLimit*24*60;
		}else if(timeLimitUnit==1){
			limitMinute=timeLimit*60;
		}else{
			limitMinute=timeLimit;
		}
		
		//ת����ǰ����ʱ��
		if(noteTimeUnit==0){
			awokeMinute=noteTime*24*60;
		}else if(noteTimeUnit==1){
			awokeMinute=noteTime*60;
		}else{
			awokeMinute=noteTime;
		}
		
		//ת����ʱ����Ƶ��
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
