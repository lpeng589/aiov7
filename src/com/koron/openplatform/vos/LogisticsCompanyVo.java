package com.koron.openplatform.vos;

/**
 * 
 * @author mj
 *
 */
public class LogisticsCompanyVo {
	private String logistics_id;// 物流公司ID
	 
	private String logistics_name;//物流公司名称
	
	private String logistics_remark;//备注说明
	
	private String sequence;

	public String getLogistics_id() {
		return logistics_id;
	}

	public void setLogistics_id(String logistics_id) {
		this.logistics_id = logistics_id;
	}

	public String getLogistics_name() {
		return logistics_name;
	}

	public void setLogistics_name(String logistics_name) {
		this.logistics_name = logistics_name;
	}

	public String getLogistics_remark() {
		return logistics_remark;
	}

	public void setLogistics_remark(String logistics_remark) {
		this.logistics_remark = logistics_remark;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}


}
