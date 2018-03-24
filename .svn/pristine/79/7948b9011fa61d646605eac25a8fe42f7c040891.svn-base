package com.koron.oa.bean;

import java.io.Serializable;

import javax.persistence.*;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 *
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
@Table(name="OAMailInfo")
public class OAMailInfoBean implements Serializable {

    /** identifier field */
    @Id
    @Column(nullable = false, length = 50)
    private String id;
    /** persistent field */
    @Column(nullable = false )
    private String mailTitle;
    @Column(nullable = true )
    private String mailContent;
    @Column(nullable = true )
    private String mailAttaches;
    @Column(nullable = true )
    private String mailFrom;
    @Column(nullable = true )
    private String mailTo;
    @Column(nullable = true )
    private String mailCc;
    @Column(nullable = true )
    private String mailBCc;
    @Column(nullable = true )
    private String mailTime;
    @Column(nullable = true )
    private int emailType;
    @Column(nullable = true )
    private String userId;
    @Column(nullable = true )
    private int state;
    @Column(nullable = true )
    private String fromUserId;
    @Column(nullable = true )
    private String toUserId;
    @Column(nullable = true )
    private String createTime;
    @Column(nullable = true )
    private String groupId;
    @Column(nullable = true )
    private String account;
    @Column(nullable = true )
    private int mailSize;
    @Column(nullable = true )
    private String innerAttaches;
    @Column(nullable = true )
    private String mailUID;
    @Column(nullable = true )
    private String  labelId;
    @Column(nullable = true )
    private String  relationId;
    @Column(nullable = true )
    private String replayDate;
    @Column(nullable = true )
    private String revolveDate;
    @Column(nullable = true )
    private String sendeMailType;    //发送的邮箱代号
    @Column(nullable = true )
    private String begReplay;
    
    @Column(nullable = true )
    private String mailcharset;
    @Column(nullable = true )
    private String emlfile;
    
    @Column(nullable = true)
    private String pId;//发送内部邮件的时候，收信人生成的邮件对应的发送人的邮件id
    
    private String collectionType;
    
    @Column(nullable = true)
    private String remark;   //邮件备注
    
	public String getCollectionType() {
		return collectionType;
	}



	public void setCollectionType(String collectionType) {
		this.collectionType = collectionType;
	}



	public String getPId() {
		return pId;
	}



	public void setPId(String id) {
		pId = id;
	}



	public String toString(){
    	String str = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n";
    	str += "<mail>\r\n";
    	str += "<id>"+id+"</id>\r\n";
    	str += "<mailTitle>"+(mailTitle==null?"":mailTitle)+"</mailTitle>\r\n";
    	str += "<mailContent>"+(mailContent==null?"":mailContent)+"</mailContent>\r\n";
    	str += "<mailAttaches>"+(mailAttaches==null?"":mailAttaches)+"</mailAttaches>\r\n";
    	str += "<mailFrom>"+(mailFrom==null?"":mailFrom)+"</mailFrom>\r\n";
    	str += "<mailTo>"+(mailTo==null?"":mailTo)+"</mailTo>\r\n";
    	str += "<mailCc>"+(mailCc==null?"":mailCc)+"</mailCc>\r\n";
    	str += "<mailBCc>"+(mailBCc==null?"":mailBCc)+"</mailBCc>\r\n";
    	str += "<mailTime>"+(mailTime==null?"":mailTime)+"</mailTime>\r\n";
    	str += "<emailType>"+(emailType)+"</emailType>\r\n";
    	str += "<userId>"+(userId==null?"":userId)+"</userId>\r\n";
    	str += "<state>"+(state)+"</state>\r\n";
    	str += "<fromUserId>"+(fromUserId==null?"":fromUserId)+"</fromUserId>\r\n";
    	str += "<toUserId>"+(toUserId==null?"":toUserId)+"</toUserId>\r\n";
    	str += "<createTime>"+(createTime==null?"":createTime)+"</createTime>\r\n";
    	str += "<groupId>"+(groupId==null?"":groupId)+"</groupId>\r\n";
    	str += "<account>"+(account==null?"":account)+"</account>\r\n";
    	str += "<mailSize>"+(mailSize)+"</mailSize>\r\n";
    	str += "<innerAttaches>"+(innerAttaches==null?"":innerAttaches)+"</innerAttaches>\r\n";
    	str += "<mailUID>"+(mailUID==null?"":mailUID)+"</mailUID>\r\n";
    	str += "<labelId>"+(labelId==null?"":labelId)+"</labelId>\r\n";
    	str += "<replayDate>"+(replayDate==null?"":replayDate)+"</replayDate>\r\n";
    	str += "<revolveDate>"+(revolveDate==null?"":revolveDate)+"</revolveDate>\r\n";
    	str += "<sendeMailType>"+(sendeMailType==null?"":sendeMailType)+"</sendeMailType>\r\n";
    	str += "<begReplay>"+(begReplay==null?"":begReplay)+"</begReplay>\r\n";
    	str += "<mailcharset>"+(mailcharset==null?"":mailcharset)+"</mailcharset>\r\n";
    	str += "<emlfile>"+(emlfile==null?"":emlfile)+"</emlfile>\r\n";
    	str +="</mail>";
    	return str;    	
    }
    
	
	
    public String getEmlfile() {
		return emlfile;
	}



	public void setEmlfile(String emlfile) {
		this.emlfile = emlfile;
	}



	public String getMailcharset() {
		return mailcharset;
	}



	public void setMailcharset(String mailcharset) {
		this.mailcharset = mailcharset;
	}



	private static String getValue(String str,String key){
    	int pos = str.indexOf("<"+key+">");
    	if(pos > 0 && str.indexOf("</"+key+">",pos) >0){
    		return str.substring(pos+("<"+key+">").length(),str.indexOf("</"+key+">",pos));
    	}
    	return "";
    }
    private static int pInt(String str){
    	try{
    		return Integer.parseInt(str);
    	}catch(Exception e){
    		return 0;
    	}
    }
    
    public static OAMailInfoBean fromString(String str){
    	OAMailInfoBean bean  = new OAMailInfoBean();
    	bean.setId(getValue(str,"id"));
    	bean.setMailTitle(getValue(str,"mailTitle"));
    	bean.setMailContent(getValue(str,"mailContent"));
    	bean.setMailAttaches(getValue(str,"mailAttaches"));
    	bean.setMailFrom(getValue(str,"mailFrom"));
    	bean.setMailTo(getValue(str,"mailTo"));
    	bean.setMailCc(getValue(str,"mailCc"));
    	bean.setMailBCc(getValue(str,"mailBCc"));
    	bean.setMailTime(getValue(str,"mailTime"));
    	bean.setEmailType(pInt(getValue(str,"emailType")));
    	bean.setUserId(getValue(str,"userId"));
    	bean.setState(pInt(getValue(str,"state")));
    	bean.setFromUserId(getValue(str,"fromUserId"));
    	bean.setToUserId(getValue(str,"toUserId"));
    	bean.setCreateTime(getValue(str,"createTime"));
    	bean.setGroupId(getValue(str,"groupId"));
    	bean.setAccount(getValue(str,"account"));
    	bean.setMailSize(pInt(getValue(str,"mailSize")));
    	bean.setInnerAttaches(getValue(str,"innerAttaches"));
    	bean.setMailUID(getValue(str,"mailUID"));
    	bean.setLabelId(getValue(str,"labelId"));
    	bean.setReplayDate(getValue(str,"replayDate"));
    	bean.setRevolveDate(getValue(str,"revolveDate"));
    	bean.setSendeMailType(getValue(str,"sendeMailType"));
    	bean.setBegReplay(getValue(str,"begReplay"));
    	bean.setMailcharset(getValue(str,"mailcharset"));
    	bean.setEmlfile(getValue(str,"emlfile"));
    	return bean;    	
    }
    
    
    public String getBegReplay() {
		return begReplay;
	}

	public void setBegReplay(String begReplay) {
		this.begReplay = begReplay;
	}
    
    public String getRevolveDate() {
		return revolveDate;
	}

	public void setRevolveDate(String revolveDate) {
		this.revolveDate = revolveDate;
	}

	public String getSendeMailType() {
		return sendeMailType;
	}

	public void setSendeMailType(String sendeMailType) {
		this.sendeMailType = sendeMailType;
	}

	public String getReplayDate() {
		return replayDate;
	}

	public void setReplayDate(String replayDate) {
		this.replayDate = replayDate;
	}

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}


    public String getId() {
        return id;
    }

    public String getInnerAttaches() {
		return innerAttaches;
	}

	public void setInnerAttaches(String innerAttaches) {
		this.innerAttaches = innerAttaches;
	}

	public int getMailSize() {
		return mailSize;
	}

	public void setMailSize(int mailSize) {
		this.mailSize = mailSize;
	}

	public String getMailUID() {
		return mailUID;
	}

	public void setMailUID(String mailUID) {
		this.mailUID = mailUID;
	}

	public String getCreateTime() {
        return createTime;
    }

    public int getEmailType() {
        return emailType;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getMailAttaches() {
        return mailAttaches;
    }

    public String getMailCc() {
        return mailCc;
    }

    public String getMailContent() {
        return mailContent;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public String getMailTime() {
        return mailTime;
    }

    public String getMailTitle() {
        return mailTitle;
    }

    public String getMailTo() {
        return mailTo;
    }

    public int getState() {
        return state;
    }

    public String getToUserId() {
        return toUserId;
    }

    public String getUserId() {
        return userId;
    }

    public String getMailBCc() {
        return mailBCc;
    }

    public String getAccount() {
        return account;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public void setMailTitle(String mailTitle) {
        this.mailTitle = mailTitle;
    }

    public void setMailTime(String mailTime) {
        this.mailTime = mailTime;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public void setMailCc(String mailCc) {
        this.mailCc = mailCc;
    }

    public void setMailAttaches(String mailAttaches) {
        this.mailAttaches = mailAttaches;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public void setEmailType(int emailType) {
        this.emailType = emailType;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setMailBCc(String mailBCc) {
        this.mailBCc = mailBCc;
    }

    public void setAccount(String account) {
        this.account = account;
    }

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


}
