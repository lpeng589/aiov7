package com.koron.crm.bean;


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
 * @Date:2013-7-31
 * @Copyright: �������
 * @Author ��࿡
 * @preserve all
 */

@Entity
@Table(name = "CRMBrotherFieldDisplay")
public class BrotherFieldDisplayBean {
	@Id
	private String id;//ID��ŵ��Ǳ���

	private String keyFields;//ģ����ѯ�ֶ�

	private String searchFields;//ö��ʱ���ѯ�ֶ�

	private String listFields;//�б���ʾ�ֶ�

	private String pageFields;//�����������޸ġ�������ʾ�ֶ�
	
	private String pageChildFields;//��ϸ�������޸ġ�������ʾ�ֶ�

	private String detailFields;//�б�չ�������ֶ�

	private String createBy;//������
	
	private String createTime;//����ʱ��
	
	private String lastUpdateBy;//����޸���
	
	private String lastUpdateTime;//������ʱ��

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKeyFields() {
		return keyFields;
	}

	public void setKeyFields(String keyFields) {
		this.keyFields = keyFields;
	}

	public String getSearchFields() {
		return searchFields;
	}

	public void setSearchFields(String searchFields) {
		this.searchFields = searchFields;
	}

	public String getListFields() {
		return listFields;
	}

	public void setListFields(String listFields) {
		this.listFields = listFields;
	}

	public String getPageFields() {
		return pageFields;
	}

	public void setPageFields(String pageFields) {
		this.pageFields = pageFields;
	}

	public String getDetailFields() {
		return detailFields;
	}

	public void setDetailFields(String detailFields) {
		this.detailFields = detailFields;
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

	public String getPageChildFields() {
		return pageChildFields;
	}

	public void setPageChildFields(String pageChildFields) {
		this.pageChildFields = pageChildFields;
	}


	

}
