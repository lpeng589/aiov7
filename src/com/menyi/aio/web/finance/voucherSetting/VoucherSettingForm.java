package com.menyi.aio.web.finance.voucherSetting;

import com.menyi.web.util.BaseForm;

/**
 * 
 * <p>Title:ƾ֤���� Form</p> 
 * <p>Description: </p>
 * @Date:2013-03-18
 * @Copyright: �������
 * @Author fjj
 */
public class VoucherSettingForm extends BaseForm{
	
	private String id;									//id
	private Integer isAuditing;							//�������
	private Integer isCheck;							//ƾ֤����ǰ������ɸ���
	private Integer isCash;								//¼��ƾ֤ʱ����ָ���ֽ�����
	private Integer isAccountAuditing;					//ƾ֤����ǰ�������
	private String auditingPersont;						//�����
	private String reverseAuditing;						//�������
	private String binderPersont;						//������
	private String reverseBinder;						//��������
	private String checkPersont;						//������
	private String cashPersont;							//ָ���ֽ�������
	
	
	public String getAuditingPersont() {
		return auditingPersont;
	}
	public void setAuditingPersont(String auditingPersont) {
		this.auditingPersont = auditingPersont;
	}
	public String getBinderPersont() {
		return binderPersont;
	}
	public void setBinderPersont(String binderPersont) {
		this.binderPersont = binderPersont;
	}
	public String getCashPersont() {
		return cashPersont;
	}
	public void setCashPersont(String cashPersont) {
		this.cashPersont = cashPersont;
	}
	public String getCheckPersont() {
		return checkPersont;
	}
	public void setCheckPersont(String checkPersont) {
		this.checkPersont = checkPersont;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getIsAccountAuditing() {
		return isAccountAuditing;
	}
	public void setIsAccountAuditing(Integer isAccountAuditing) {
		this.isAccountAuditing = isAccountAuditing;
	}
	public Integer getIsAuditing() {
		return isAuditing;
	}
	public void setIsAuditing(Integer isAuditing) {
		this.isAuditing = isAuditing;
	}
	public Integer getIsCash() {
		return isCash;
	}
	public void setIsCash(Integer isCash) {
		this.isCash = isCash;
	}
	public Integer getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
	}
	public String getReverseAuditing() {
		return reverseAuditing;
	}
	public void setReverseAuditing(String reverseAuditing) {
		this.reverseAuditing = reverseAuditing;
	}
	public String getReverseBinder() {
		return reverseBinder;
	}
	public void setReverseBinder(String reverseBinder) {
		this.reverseBinder = reverseBinder;
	}
	
	
}
