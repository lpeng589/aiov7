package com.menyi.aio.bean;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * 
 * <p>
 * Company: ¸¶Ïæ¶õ
 * </p>
 * 
 * @author ¸¶Ïæ¶õ
 * @version 1.0
 */
public class TimingMsgDetBean {
	public TimingMsgDetBean() {
	}

	private String id;
	private String f_ref;
	private String email;
	private String phonenumber;
	private String languageSort;
	
	public String getLanguageSort() {
		return languageSort;
	}

	public void setLanguageSort(String languageSort) {
		this.languageSort = languageSort;
	}

	public void setid(String id) {
		this.id = id;
	}

	public String getid() {
		return id;
	}

	public void setf_ref(String f_ref) {
		this.f_ref = f_ref;
	}

	public String getf_ref() {
		return f_ref;
	}

	public void setemail(String email) {
		this.email = email;
	}

	public String getemail() {
		return email;
	}

	public void setphoneNumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getphoneNumber() {
		return phonenumber;
	}

}
