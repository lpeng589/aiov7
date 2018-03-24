package com.menyi.aio.bean;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name="tblBillFlow")
public class BillFlowBean {
    private String flowTypeDis;
    @Id
    @Column(nullable=false,length=30)
    private String id;
    @Column(nullable=false,length=10)
    private String isUsed;
    @Column(nullable=false,length=100)
    private String flowName;
    @Column(nullable=false,length=100)
    private String fileName;
    @Column(nullable=false,length=19)
    private String createTime;
    @Column(nullable=false,length=19)
    private String lastUpdateTime;
    @Column(nullable=true)
    private Integer statusId;
    @Column(nullable=false,length=30)
    private String flowType;
    @Column(nullable=false,length=10)
    private String newFlag;
    @Column(nullable=false,length=30)
    private String createBy;
    @Column(nullable=false,length=30)
    private String lastUpdateBy;

    public String getCreateTime() {
        return createTime;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFlowName() {
        return flowName;
    }

    public String getFlowType() {
        return flowType;
    }

    public String getId() {
        return id;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public String getNewFlag() {
        return newFlag;
    }



    public String getCreateBy() {
        return createBy;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public String getFlowTypeDis() {
        return flowTypeDis;
    }


    public void setNewFlag(String newFlag) {
        this.newFlag = newFlag;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public void setFlowTypeDis(String flowTypeDis) {
        this.flowTypeDis = flowTypeDis;
    }

}
