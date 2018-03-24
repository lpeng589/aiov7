package com.menyi.email;

import com.menyi.web.util.BaseForm;
import org.apache.struts.upload.FormFile;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: ÷‹–¬”Ó</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class EMailForm extends BaseForm{
    private String emailType;
    private String saveType;
    private String to;
    private String cc;
    private String bcc;
    private String mailTitle;
    private String mailContent;
    private String begReplay;
    
    public String getBegReplay() {
		return begReplay;
	}

	public void setBegReplay(String begReplay) {
		this.begReplay = begReplay;
	}

	public String getBcc() {
        return bcc;
    }

    public String getCc() {
        return cc;
    }

    public String getEmailType() {
        return emailType;
    }

    public String getMailContent() {
        return mailContent;
    }

    public String getMailTitle() {
        return mailTitle;
    }

    public String getSaveType() {
        return saveType;
    }

    public String getTo() {
        return to;
    }



    public void setTo(String to) {
        this.to = to;
    }

    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }

    public void setMailTitle(String mailTitle) {
        this.mailTitle = mailTitle;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }




}
