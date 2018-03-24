package com.menyi.email;

import com.menyi.web.util.BaseSearchForm;

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
public class EMailSearchForm extends BaseSearchForm{

    private String keyword;
    private String email;
    private String beginTime;
    private String endTime;
    private String orderby;
    private String view;
    private String isdesc = "true"; 
    private String labelId;
    private String highKeyword;
    
    
    

    public String getHighKeyword() {
		return highKeyword;
	}

	public void setHighKeyword(String highKeyword) {
		this.highKeyword = highKeyword;
	}

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	public String getIsdesc() {
		return isdesc;
	}

	public void setIsdesc(String isdesc) {
		this.isdesc = isdesc;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getEmail() {
        return email;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
