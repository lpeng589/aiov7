package com.koron.crm.carefor;

import javax.persistence.*;

import com.menyi.web.util.BaseForm;
/**
 * �ͻ��ػ�����tblCarefor��Form
 * @version CRM4.3
 * @author ����
 *
 */

public class CareforForm  extends BaseForm{
	private String id = "";
	private String name = "";
	private String status;

	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}