

/**
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
 */
package com.menyi.aio.bean;

import java.util.ArrayList;
import java.util.List;

public class BillMsgBean {
    public BillMsgBean() {
    }

    private String id;
    private String classCode;
    private String workFlowNode;
    private String workFlowNodeName;
    private String SqlDefineName;
    private String Display;
    private String Type;
    private int Status;
    private String createBy;
    private String lastUpdateBy;
    private String createTime;
    private String lastUpdateTime;
    private int statusId;
    private String SCompanyID;
    private List<BillMsgDetBean> billMsgDets=new ArrayList<BillMsgDetBean>();
    public void setid(String id) {
        this.id = id;
    }

    public String getid() {
        return id;
    }

    public void setclassCode(String classcode) {
        this.classCode = classcode;
    }

    public String getclassCode() {
        return classCode;
    }

    public void setworkFlowNode(String workflownode) {
        this.workFlowNode = workflownode;
    }

    public String getworkFlowNode() {
        return workFlowNode;
    }

    public void setworkFlowNodeName(String workflownodename) {
        this.workFlowNodeName = workflownodename;
    }

    public String getworkFlowNodeName() {
        return workFlowNodeName;
    }

    public void setSqlDefineName(String sqldefinename) {
        this.SqlDefineName = sqldefinename;
    }

    public String getSqlDefineName() {
        return SqlDefineName;
    }

    public void setDisplay(String display) {
        this.Display = display;
    }

    public String getDisplay() {
        return Display;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getType() {
        return Type;
    }

    public void setStatus(int status) {
        this.Status = status;
    }

    public int getStatus() {
        return Status;
    }

    public void setcreateBy(String createby) {
        this.createBy = createby;
    }

    public String getcreateBy() {
        return createBy;
    }

    public void setlastUpdateBy(String lastupdateby) {
        this.lastUpdateBy = lastupdateby;
    }

    public String getlastUpdateBy() {
        return lastUpdateBy;
    }

    public void setcreateTime(String createtime) {
        this.createTime = createtime;
    }

    public String getcreateTime() {
        return createTime;
    }

    public void setlastUpdateTime(String lastupdatetime) {
        this.lastUpdateTime = lastupdatetime;
    }

    public String getlastUpdateTime() {
        return lastUpdateTime;
    }

    public void setstatusId(int statusid) {
        this.statusId = statusid;
    }

    public void setBillMsgDets(List billMsgDets) {
        this.billMsgDets = billMsgDets;
    }

    public int getstatusId() {
        return statusId;
    }

    public List getBillMsgDets() {
        return billMsgDets;
    }

    public void setSCompanyID(String scompanyid) {
        this.SCompanyID = scompanyid;
    }

    public String getSCompanyID() {
        return SCompanyID;
    }

}
