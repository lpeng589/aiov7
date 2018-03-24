package com.koron.oa.message;

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
public class MessageForm extends BaseForm {
    String id;
    String send;
    String title;
    String receive;
    String content;
    String relatMsgId;
    String operType;
    String createTime;
    String msgType;
    String sendName;
    String receiveName;
    String relationId;			/*关联单据Id*/
    String affix;

    public String getAffix() {
		return affix;
	}

	public void setAffix(String affix) {
		this.affix = affix;
	}

	public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getOperType() {
        return operType;
    }

    public String getReceive() {
        return receive;
    }

    public String getRelatMsgId() {
        return relatMsgId;
    }

    public String getSend() {
        return send;
    }

    public String getTitle() {
        return title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public String getSendName() {
        return sendName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public void setRelatMsgId(String relatMsgId) {
        this.relatMsgId = relatMsgId;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	
}
