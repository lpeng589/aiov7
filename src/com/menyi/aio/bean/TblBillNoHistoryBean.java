package com.menyi.aio.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>Title:���ݱ����ʷ��¼</p> 
 * <p>Description: </p>
 * @Date:
 * @Copyright: �������
 * @Author fjj
 * @preserve all
 */
@Entity
@Table(name="tblBillNoHistory")
public class TblBillNoHistoryBean {

	@Id
    private Integer id;						//id
	private String key;						//���ڶ�����Ϊ�������ֶ������»������ӡ����磺tblrole_id
	private Integer value;					//��ǰ��ˮ��ֵ
	private String formatedString;			//ϵ�к�
	private long timestamp;					//��ȡ��ֵ��ʱ��
	private Integer removed;				//0��ʹ1�ճ���2�����
	
	
	
	public String getFormatedString() {
		return formatedString;
	}
	public void setFormatedString(String formatedString) {
		this.formatedString = formatedString;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Integer getRemoved() {
		return removed;
	}
	public void setRemoved(Integer removed) {
		this.removed = removed;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	
}
