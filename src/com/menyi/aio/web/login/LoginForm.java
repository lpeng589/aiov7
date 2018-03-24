package com.menyi.aio.web.login;

import com.menyi.web.util.BaseForm;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class LoginForm extends BaseForm{
    private String name;
    private String password;
    private String sunCompany;
    private String style ;
    private String loc ;

    public String getName() {
        return name;
    }

    public String getPassword() {

        return password;
    }

    public String getSunCompany() {
        return sunCompany;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public void setSunCompany(String sunCompany) {
        this.sunCompany = sunCompany;
    }

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
    
}
