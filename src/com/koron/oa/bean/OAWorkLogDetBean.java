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
@Table(name = "OAWorkLogDet")
public class OAWorkLogDetBean {
	
	@Id
	private String id; 

	private String contents;//����

	private String contentType;// �������ͣ�1:�ܽ�,2:�ƻ�

	private String relationType;// ���������� todo:����,task:����: calendar:�ճ�

	private String relationId;//����ID

	private String workLogId;// �������
	
	private String schedule;// ��ɽ���(%)
	
	private String createTime;//����ʱ��
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getWorkLogId() {
		return workLogId;
	}

	public void setWorkLogId(String workLogId) {
		this.workLogId = workLogId;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
