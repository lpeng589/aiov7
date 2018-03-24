package com.koron.oa.bean;

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
@Table(name = "OAMessage2")
public class MessageBean2 {
    @Id
    @Column(nullable = false,length=60 )
    private String id;
    @Column(nullable = false,length=30 )
    private String send;
    @Column(nullable = false,length=30 )
    private String receive;
    @Column(nullable = false,length=5000 )
    private String content;
    @Column(nullable = true,length=30  )
    private String relatMsgId;
    @Column(nullable = true,length=10 )
    private String operType;
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
    private String status;
    @Column(nullable = true,length=50 )
    private String sendName;
    @Column(nullable = true,length=50 )
    private String receiveName;
    @Column(nullable = true,length=10 )
    private String exist;
    @Column(nullable = true,length=50)
    private String relationId ;
    @Column(nullable = true,length=50)
    private String relationName ;
    @Column(nullable = true,length=50)
    private String affix ;

    public String getContent() {
        return content;
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
        return receiveName;
    }

    public String getSendName() {
        return sendName;
    }

    public String getStatus() {
        return status;
    }

    public String getSend() {
        return send;
    }

    public String getRelatMsgId() {
        return relatMsgId;
    }

    public String getReceive() {
        return receive;
    }

    public String getOperType() {
        return operType;
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
        this.receiveName = ReceiveName;
    }

    public void setSendName(String SendName) {
        this.sendName = SendName;
    }

    public void setStatus(String Status) {
        this.status = Status;
    }

    public void setContent(String Content) {
        this.content = Content;
    }

    public void setOperType(String OperType) {
        this.operType = OperType;
    }

    public void setReceive(String Receive) {
        this.receive = Receive;
    }

    public void setRelatMsgId(String RelatMsgId) {
        this.relatMsgId = RelatMsgId;
    }

    public void setSend(String Send) {
        this.send = Send;
    }

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public String getAffix() {
		return affix;
	}

	public void setAffix(String affix) {
		this.affix = affix;
	}

}
