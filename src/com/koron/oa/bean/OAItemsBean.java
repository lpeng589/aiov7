package com.koron.oa.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>
 * Title:�ֶ�����
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2013-11-2
 * @Copyright: �������
 * @Author ��ܿ�
 * @preserve all
 */

@Entity
@Table(name = "OAItems")
public class OAItemsBean {
	@Id
	private String id;//ID��ŵ��Ǳ���

	private String title;//��Ŀ����

	private String executor;//ִ���ˣ������ˣ�

	private String beginTime;//��ʼʱ��

	private String endTime;//����ʱ��
	
	private String participant;//������

	private String remark;//��Ŀ����

	private String affix;//����
	
	private String status;//״̬ 0=����,1=�����У�2=��ɣ�3=��ʱ
	
	private String createBy;//������
	
	private String createTime;//����ʱ��
	
	private String lastUpdateBy;//����޸���
	
	private String lastUpdateTime;//����޸�ʱ��

	private String nextAlertTime;//����ʱ��
	
	private String schedule;// ��ɽ���(%)
	
	private String finishTime;//���ʱ��
	
	private String clientId;//�ͻ�id
	
	private int itemOrder;//������

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAffix() {
		return affix;
	}

	public void setAffix(String affix) {
		this.affix = affix;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getNextAlertTime() {
		return nextAlertTime;
	}

	public void setNextAlertTime(String nextAlertTime) {
		this.nextAlertTime = nextAlertTime;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public int getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(int itemOrder) {
		this.itemOrder = itemOrder;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	
}
