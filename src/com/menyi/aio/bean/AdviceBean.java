package com.menyi.aio.bean;

import javax.persistence.*;

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
 * @preserve all
 */
@Entity
@Table(name = "tblAdvice")
public class AdviceBean {
    @Id
    @Column(nullable = false,length=30 )
    private String id;
    @Column(nullable = false,length=30 )
    private String Send;
    @Column(nullable = false,length=100 )
    private String Title;
    @Column(nullable = false,length=30 )
    private String Receive;
    @Column(nullable = false,length=5000 )
    private String Content;
    @Column(nullable = true,length=30  )
    private String RelatMsgId;
    @Column(nullable = true,length=10 )
    private String OperType;
    @Column(nullable = true,length=30 )
    private String createBy;
    @Column(nullable = true,length=30 )
    private String lastUpdateBy;
    @Column(nullable = true,length=19 )
    private String createTime;
    @Column(nullable = true,length=19 )
    private String  lastUpdateTime;
    @Column(nullable = true )
    private Integer statusId;
    @Column(nullable = true,length=10 )
    private String Status;
    @Column(nullable = true,length=50 )
    private String SendName;
    @Column(nullable = true,length=50 )
    private String ReceiveName;
    @Column(nullable = true,length=10 )
    private String exist;
    @Column(nullable = true,length=50)
    private String relationId ;
    @Column(nullable = true,length=50)
    private String type ;
    
    public String getContent() {
        return Content;
    }

    public String getCreateBy() {
        return createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getId() {
        return id;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public String getExist() {
        return exist;
    }

    public String getReceiveName() {
        return ReceiveName;
    }

    public String getSendName() {
        return SendName;
    }

    public String getStatus() {
        return Status;
    }

    public String getTitle() {
        return Title;
    }

    public String getSend() {
        return Send;
    }

    public String getRelatMsgId() {
        return RelatMsgId;
    }

    public String getReceive() {
        return Receive;
    }

    public String getOperType() {
        return OperType;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setExist(String exist) {
        this.exist = exist;
    }

    public void setReceiveName(String ReceiveName) {
        this.ReceiveName = ReceiveName;
    }

    public void setSendName(String SendName) {
        this.SendName = SendName;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public void setOperType(String OperType) {
        this.OperType = OperType;
    }

    public void setReceive(String Receive) {
        this.Receive = Receive;
    }

    public void setRelatMsgId(String RelatMsgId) {
        this.RelatMsgId = RelatMsgId;
    }

    public void setSend(String Send) {
        this.Send = Send;
    }

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
