package com.koron.oa.bean;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 * @preserve all
 */
@Entity
@Table(name = "MailinfoSetting")
public class MailinfoSettingBean {
    /** identifier field */
    @Id
    private String id;
 
    private String mailaddress;
    private String pop3server;
    private String pop3username;
    private String pop3userpassword;
    private String smtpserver;
    private String smtpusername;
    private String smtpuserpassword;
    private String createby;
    private String lastupdateby;
    private String createtime;
    private String lastupdatetime;
    private int statusid = 1;
    private String scompanyid;
    private int smtpAuth;
    private int pop3Port;
    private int smtpPort;
    private String defaultUser; //默认账户
    private String displayName;
    private String account; //账户名称
    private int popssl;
    private int smtpssl;
    private int smtpsamepop;
    private int autoReceive;
    private String signature;    
    private int retentDay;
    private int autoAssign;
    private String mainAccount;
    
    
	public String getMainAccount() {
		return mainAccount;
	}
	public void setMainAccount(String mainAccount) {
		this.mainAccount = mainAccount;
	}
	public int getAutoAssign() {
		return autoAssign;
	}
	public void setAutoAssign(int autoAssign) {
		this.autoAssign = autoAssign;
	}
	public int getStatusid() {
		return statusid;
	}
	public void setStatusid(int statusid) {
		this.statusid = statusid;
	}
	public int getRetentDay() {
		return retentDay;
	}
	public void setRetentDay(int retentDay) {
		this.retentDay = retentDay;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public int getAutoReceive() {
		return autoReceive;
	}
	public void setAutoReceive(int autoReceive) {
		this.autoReceive = autoReceive;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}

	public String getCreateby() {
		return createby;
	}
	public void setCreateby(String createby) {
		this.createby = createby;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getDefaultUser() {
		return defaultUser;
	}
	public void setDefaultUser(String defaultUser) {
		this.defaultUser = defaultUser;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLastupdateby() {
		return lastupdateby;
	}
	public void setLastupdateby(String lastupdateby) {
		this.lastupdateby = lastupdateby;
	}
	public String getLastupdatetime() {
		return lastupdatetime;
	}
	public void setLastupdatetime(String lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}
	public String getMailaddress() {
		return mailaddress;
	}
	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
	}
	
	public int getPop3Port() {
		return pop3Port;
	}
	public void setPop3Port(int pop3Port) {
		this.pop3Port = pop3Port;
	}
	public String getPop3server() {
		return pop3server;
	}
	public void setPop3server(String pop3server) {
		this.pop3server = pop3server;
	}
	public String getPop3username() {
		return pop3username;
	}
	public void setPop3username(String pop3username) {
		this.pop3username = pop3username;
	}
	public String getPop3userpassword() {
		return pop3userpassword;
	}
	public void setPop3userpassword(String pop3userpassword) {
		this.pop3userpassword = pop3userpassword;
	}
	public int getPopssl() {
		return popssl;
	}
	public void setPopssl(int popssl) {
		this.popssl = popssl;
	}
	public String getScompanyid() {
		return scompanyid;
	}
	public void setScompanyid(String scompanyid) {
		this.scompanyid = scompanyid;
	}
	public int getSmtpAuth() {
		return smtpAuth;
	}
	public void setSmtpAuth(int smtpAuth) {
		this.smtpAuth = smtpAuth;
	}
	public int getSmtpPort() {
		return smtpPort;
	}
	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}
	public int getSmtpsamepop() {
		return smtpsamepop;
	}
	public void setSmtpsamepop(int smtpsamepop) {
		this.smtpsamepop = smtpsamepop;
	}
	public String getSmtpserver() {
		return smtpserver;
	}
	public void setSmtpserver(String smtpserver) {
		this.smtpserver = smtpserver;
	}
	public int getSmtpssl() {
		return smtpssl;
	}
	public void setSmtpssl(int smtpssl) {
		this.smtpssl = smtpssl;
	}
	public String getSmtpusername() {
		return smtpusername;
	}
	public void setSmtpusername(String smtpusername) {
		this.smtpusername = smtpusername;
	}
	public String getSmtpuserpassword() {
		return smtpuserpassword;
	}
	public void setSmtpuserpassword(String smtpuserpassword) {
		this.smtpuserpassword = smtpuserpassword;
	}


    



}
