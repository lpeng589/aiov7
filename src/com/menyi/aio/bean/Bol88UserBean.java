package com.menyi.aio.bean;

import javax.persistence.*;

/**
 * <p>bol88��Ա��Ϣ: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: ��־��</p>
 *
 * @author ��־��
 * @version 1.0
 * @preserve all
 */
@Entity
@Table(name = "tblBol88User")
public class Bol88UserBean {
    
	@Id
    private String id;
    
    private String userName;
    
    private String pass;

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
    
    

}
