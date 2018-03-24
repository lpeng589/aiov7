package com.menyi.aio.web.alert;

import java.util.Date;

import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.BaseForm;
import com.menyi.web.util.BaseSearchForm;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: уея╘</p>
 *
 * @author уея╘
 * @version 1.0
 */
public class AlertCenterForm extends BaseSearchForm {
   private String name="";
   private String userId;
   private String userName;
   private String startDate ;
   private String endDate ;
   private String alertStatus;
   private String alertTime;
    public String getAlertStatus() {
        return alertStatus;
    }

    public String getAlertTime() {
        return alertTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setAlertTime(String alertTime) {
        this.alertTime = alertTime;
    }

    public void setAlertStatus(String alertStatus) {
        this.alertStatus = alertStatus;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
