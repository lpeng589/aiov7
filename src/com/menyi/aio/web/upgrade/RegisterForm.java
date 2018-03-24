package com.menyi.aio.web.upgrade;

import org.apache.struts.upload.FormFile;

import com.menyi.web.util.BaseForm;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class RegisterForm extends BaseForm {
    private String companyAddress;
    private String connectorName;
    private String companyName;
    private String companyUrl;
    private String qq;
    private String tel;
    private int encryptionType;
    private String email;
    
    private FormFile certFile;
    
    

    public FormFile getCertFile() {
		return certFile;
	}

	public void setCertFile(FormFile certFile) {
		this.certFile = certFile;
	}

	public String getCompanyName() {

        return companyName;
    }

    public int getEncryptionType() {
		return encryptionType;
	}

	public void setEncryptionType(int encryptionType) {
		this.encryptionType = encryptionType;
	}

	public String getCompanyAddress() {

        return companyAddress;
    }

    public String getCompanyUrl() {

        return companyUrl;
    }

    public String getConnectorName() {

        return connectorName;
    }

    public String getQq() {
        return qq;
    }

    public String getTel() {
        return tel;
    }


    public String getEmail() {
        return email;
    }

    public void setConnectorName(String connectorName) {

        this.connectorName = connectorName;
    }

    public void setCompanyUrl(String companyUrl) {

        this.companyUrl = companyUrl;
    }

    public void setCompanyAddress(String companyAddress) {

        this.companyAddress = companyAddress;
    }

    public void setCompanyName(String companyName) {

        this.companyName = companyName;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


    public void setEmail(String email) {
        this.email = email;
    }
}
