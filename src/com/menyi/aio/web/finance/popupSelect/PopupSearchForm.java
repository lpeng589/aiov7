package com.menyi.aio.web.finance.popupSelect;

import com.menyi.web.util.BaseSearchForm;

public class PopupSearchForm extends BaseSearchForm{

	private String selectType;			//��ѯ��ʽ��keyword�ؼ��ֲ�ѯ��group���ѯ��choose��ѡ���ݲ�ѯ��all������Ϣ��
	private String chooseType;			//ѡ�����ͣ�chooseChildֻ����ѡ�����¼����ߣ�allȫ����choosePerentֻ����ѡ�񸸼�������ѡ�����¼���
	private String selectValue;			//��ѯ��ֵ����ͬ��ѯ��ʽ��ֵ��ͬ��
	private String isCease;				//�Ƿ���ʾ�����õĿ�Ŀ��Ĭ�ϲ���ʾ����ʾ��Ϊtrue��
	private String inputType;			//���ͣ���ѡ��checkbox����ѡ��radio��
	private String returnName;			//������ص���������
	private String isCheckItem;			//�Ƿ���ʾ������(false����ʾ��true��ʾ Ĭ��true)
	
	
	public String getChooseType() {
		return chooseType;
	}
	public void setChooseType(String chooseType) {
		this.chooseType = chooseType;
	}
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	public String getIsCease() {
		return isCease;
	}
	public void setIsCease(String isCease) {
		this.isCease = isCease;
	}
	public String getSelectType() {
		return selectType;
	}
	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}
	public String getSelectValue() {
		return selectValue;
	}
	public void setSelectValue(String selectValue) {
		this.selectValue = selectValue;
	}
	public String getReturnName() {
		return returnName;
	}
	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}
	public String getIsCheckItem() {
		return isCheckItem;
	}
	public void setIsCheckItem(String isCheckItem) {
		this.isCheckItem = isCheckItem;
	}
	
	
}
