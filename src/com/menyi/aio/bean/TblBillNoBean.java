package com.menyi.aio.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>Title:���ݱ�Ź��</p> 
 * <p>Description: </p>
 * @Date:
 * @Copyright: �������
 * @Author fjj
 * @preserve all
 */
@Entity
@Table(name="tblBillNo")
public class TblBillNoBean {
	
	@Id
    private Integer id;						//id
	private String key;						//���ڶ�����Ϊ�������ֶ������»������ӡ����磺tblrole_id
	private String billName;				//��������
	private String pattern;					//���������ʵ��Ÿ�ʽ���ַ���
	private Integer billNO;					//ϵ�кŵ�ǰ��ʹ������
	private Integer start;					//��ʼ��ˮ��
	private Integer step;					//��ˮ�Ų���
	private Boolean isfillback;				//�Ƿ�ʵ������
	private Integer reset;					//��������
	private long laststamp;					//�ϴλ�ȡ��ֵ��ʱ��
	private String explain;					//���������ʵ��Ÿ�ʽ���ַ���ת��Ϊ����
	private Boolean isAddbeform;			//�Ƿ��������ǰ��ӵ���
	private String statusId;				//״̬��0��ʾ��-1���أ�
	private String isDefaultLoginPerson;	//Ĭ�Ͼ����ˣ�0�ǣ�-1��
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Boolean getIsfillback() {
		return isfillback;
	}
	public void setIsfillback(Boolean isfillback) {
		this.isfillback = isfillback;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public long getLaststamp() {
		return laststamp;
	}
	public void setLaststamp(long laststamp) {
		this.laststamp = laststamp;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public Integer getReset() {
		return reset;
	}
	public void setReset(Integer reset) {
		this.reset = reset;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getStep() {
		return step;
	}
	public void setStep(Integer step) {
		this.step = step;
	}
	public Integer getBillNO() {
		return billNO;
	}
	public void setBillNO(Integer billNO) {
		this.billNO = billNO;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public String getBillName() {
		return billName;
	}
	public void setBillName(String billName) {
		this.billName = billName;
	}
	public Boolean getIsAddbeform() {
		return isAddbeform;
	}
	public void setIsAddbeform(Boolean isAddbeform) {
		this.isAddbeform = isAddbeform;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getIsDefaultLoginPerson() {
		return isDefaultLoginPerson;
	}
	public void setIsDefaultLoginPerson(String isDefaultLoginPerson) {
		this.isDefaultLoginPerson = isDefaultLoginPerson;
	}
	
}
