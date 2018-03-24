package com.menyi.aio.bean;

import java.util.List;

import javax.persistence.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: ������</p>
 *
 * @author ������
 * @version 1.0
 * @preserve all
 */
@Entity
@Table(name = "tblSysAlert")
public class SysAlertBean {
	
    @Id
    private String id;
    private String SqlDefineName;							
    private String alertName;								//Ԥ������
	private int status;                                     //״̬��0���ã�1�����ã�
	private String createby;								//������
	private String lastUpdateBy;							//�޸���
	private String createtime;								//����ʱ��
	private String lastUpdateTime;							//�޸�ʱ��
	private int statusId;									//statusId
	private String moduleType;								//Ԥ�������� ��Ӧ��tblAlertGroup id
	private String modelId;									//��Ӧ����ģ��id
	private String condition;								//����
	private String actionTime;								//��ʼʱ�� �磺08����8�㿪ʼ����
	private int actionFrequency;							//Ԥ��ִ��Ƶ��
	private String alertType;								//���ѷ�ʽ��1���ţ�2�ʼ���4֪ͨ��Ϣ��
	private String nextAlertTime;							//��һ��ʱ��
	private String alertCode;								//Ԥ���ı�ʶ
	private String bewrite;									//Ԥ������ ���磺�������[input]Ԥ��
	private int conditionStatus;							//����״̬��0��ʾ��1����
	private String remark;									//����
	private int isHidden;									//��ʾ������
	
	@OneToMany(mappedBy = "sysAlertBean", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<SysAlertDetBean> sysAlertDetBeanList;		//��ϸ
	
	
	public int getActionFrequency() {
		return actionFrequency;
	}
	public void setActionFrequency(int actionFrequency) {
		this.actionFrequency = actionFrequency;
	}
	public String getActionTime() {
		return actionTime;
	}
	public void setActionTime(String actionTime) {
		this.actionTime = actionTime;
	}
	public String getAlertName() {
		return alertName;
	}
	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getCreateby() {
		return createby;
	}
	public void setCreateby(String createby) {
		this.createby = createby;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
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
	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	public String getModuleType() {
		return moduleType;
	}
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	public String getNextAlertTime() {
		return nextAlertTime;
	}
	public void setNextAlertTime(String nextAlertTime) {
		this.nextAlertTime = nextAlertTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public String getSqlDefineName() {
		return SqlDefineName;
	}
	public void setSqlDefineName(String sqlDefineName) {
		SqlDefineName = sqlDefineName;
	}
	public List<SysAlertDetBean> getSysAlertDetBeanList() {
		return sysAlertDetBeanList;
	}
	public void setSysAlertDetBeanList(List<SysAlertDetBean> sysAlertDetBeanList) {
		this.sysAlertDetBeanList = sysAlertDetBeanList;
	}
	public String getAlertCode() {
		return alertCode;
	}
	public void setAlertCode(String alertCode) {
		this.alertCode = alertCode;
	}
	public String getBewrite() {
		return bewrite;
	}
	public void setBewrite(String bewrite) {
		this.bewrite = bewrite;
	}
	public int getConditionStatus() {
		return conditionStatus;
	}
	public void setConditionStatus(int conditionStatus) {
		this.conditionStatus = conditionStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getIsHidden() {
		return isHidden;
	}
	public void setIsHidden(int isHidden) {
		this.isHidden = isHidden;
	}
	

}
