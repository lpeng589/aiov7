package com.menyi.aio.web.iniSet;

import com.menyi.web.util.BaseSearchForm;

/**
 * 
 * <p>
 * Title:�����ڳ�SearchForm
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-10-22 ���� 16:40
 * @Copyright: �������
 * @Author fjj
 * @preserve all
 */
public class IniAccSearchForm extends BaseSearchForm{
	
	private String accNumber;							//��ĿclassCode
	private String accCode;								//��Ŀ����
	private String accName;								//��Ŀ����
	private String keyword;								//ģ����ѯ
	
	
	public String getAccCode() {
		return accCode;
	}
	public void setAccCode(String accCode) {
		this.accCode = accCode;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getAccNumber() {
		return accNumber;
	}
	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	
	
	
}
