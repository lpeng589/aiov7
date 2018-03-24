package com.menyi.aio.web.logo;

import org.apache.struts.upload.FormFile;

import com.menyi.web.util.BaseForm;

public class LogoSetForm extends BaseForm{

	private String logoType ;
	private FormFile loginLogo_1 ;//µÇÂ½
	private FormFile loginLogo_2;//µÇÂ½
	private FormFile loginLogo_3 ;//µÇÂ½
	private FormFile printLogo ;//´òÓ¡
	private FormFile company;//¹«Ë¾
	
	
	public FormFile getLoginLogo_1() {
		return loginLogo_1;
	}
	public void setLoginLogo_1(FormFile loginLogo_1) {
		this.loginLogo_1 = loginLogo_1;
	}
	public FormFile getLoginLogo_2() {
		return loginLogo_2;
	}
	public void setLoginLogo_2(FormFile loginLogo_2) {
		this.loginLogo_2 = loginLogo_2;
	}
	public FormFile getLoginLogo_3() {
		return loginLogo_3;
	}
	public void setLoginLogo_3(FormFile loginLogo_3) {
		this.loginLogo_3 = loginLogo_3;
	}
	public FormFile getCompany() {
		return company;
	}
	public void setCompany(FormFile company) {
		this.company = company;
	}
	public String getLogoType() {
		return logoType;
	}
	public void setLogoType(String logoType) {
		this.logoType = logoType;
	}
	public FormFile getPrintLogo() {
		return printLogo;
	}
	public void setPrintLogo(FormFile printLogo) {
		this.printLogo = printLogo;
	}

	
}
