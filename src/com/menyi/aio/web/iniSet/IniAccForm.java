package com.menyi.aio.web.iniSet;

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
public class IniAccForm extends BaseForm {
    private String id;
    private String accCode;
    private String accName;
    private String accNumber ;
    private String currYIniBase="0";
    private String currYIniDebitSumBase="0";
    private String currYIniCreditSumBase="0";
    private String currYIniBalaBase="0";
    private String curType="";
    private String curRate="0";
    private String department="";

    public String getAccCode() {
        return accCode;
    }

    public String getAccName() {
        return accName;
    }

    public String getAccNumber() {
        return accNumber;
    }



    public String getId() {
        return id;
    }


    public String getCurType() {
        return curType;
    }

    public String getCurRate() {
        return curRate;
    }
    public String getCurrYIniBase() {
        return currYIniBase;
    }

    public String getCurrYIniCreditSumBase() {
        return currYIniCreditSumBase;
    }

    public String getCurrYIniDebitSumBase() {
        return currYIniDebitSumBase;
    }

    public String getCurrYIniBalaBase() {
        return currYIniBalaBase;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public void setAccCode(String accCode) {
        this.accCode = accCode;
    }
    public void setCurType(String curType) {
        this.curType = curType;
    }

    public void setCurRate(String curRate) {
        this.curRate = curRate;
    }

    public void setCurrYIniBase(String currYIniBase) {
        this.currYIniBase = currYIniBase;
    }

    public void setCurrYIniCreditSumBase(String currYIniCreditSumBase) {
        this.currYIniCreditSumBase = currYIniCreditSumBase;
    }

    public void setCurrYIniDebitSumBase(String currYIniDebitSumBase) {
        this.currYIniDebitSumBase = currYIniDebitSumBase;
    }

    public void setCurrYIniBalaBase(String currYIniBalaBase) {
        this.currYIniBalaBase = currYIniBalaBase;
    }

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

}
