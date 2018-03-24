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
 * @Date:2013-11-12
 * @Copyright: �������
 * @Author ��ܿ�
 * @preserve all
 */

@Entity
@Table(name = "OAWorkLog")
public class OAWorkLogBean {
	
	@Id
	private String id; 

	private String type;//��־���� day:�� week:��

	private String workLogDate;// ��־����

	private String shareBy;// �����

	private String affix;//����
	
	private String summaryAffix;//�ܽḽ��

	private String lacation;// λ��

	private String nextAlertTime;//����ʱ�䣬��ʱ���ʾ����������

	private String clientId;// �����ͻ�
	
	private String createBy;// ������

	private String createTime;// ����ʱ��

	private String lastUpdateBy;// ����޸���

	private String lastUpdateTime;// ����޸�ʱ��
	
	private String isPlanTemplate;// �Ƿ�ʹ��ģ��true��ʾ��

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getShareBy() {
		return shareBy;
	}

	public void setShareBy(String shareBy) {
		this.shareBy = shareBy;
	}

	public String getAffix() {
		return affix;
	}

	public void setAffix(String affix) {
		this.affix = affix;
	}

	public String getLacation() {
		return lacation;
	}

	public void setLacation(String lacation) {
		this.lacation = lacation;
	}

	public String getNextAlertTime() {
		return nextAlertTime;
	}

	public void setNextAlertTime(String nextAlertTime) {
		this.nextAlertTime = nextAlertTime;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
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

	public String getWorkLogDate() {
		return workLogDate;
	}

	public void setWorkLogDate(String workLogDate) {
		this.workLogDate = workLogDate;
	}

	public String getIsPlanTemplate() {
		return isPlanTemplate;
	}

	public void setIsPlanTemplate(String isPlanTemplate) {
		this.isPlanTemplate = isPlanTemplate;
	}

	public String getSummaryAffix() {
		return summaryAffix;
	}

	public void setSummaryAffix(String summaryAffix) {
		this.summaryAffix = summaryAffix;
	}
	

		
	
}
