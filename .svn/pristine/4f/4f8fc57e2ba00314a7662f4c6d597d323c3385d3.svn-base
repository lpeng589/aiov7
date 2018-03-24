package com.menyi.email.util;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

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
public class EMailMessage {
    /**
     * 接收者
     */
    public static int TO = 0;
    /**
     * 抄送
     */
    public static int CC = 1;
    /**
     * 盲抄送
     */
    public static int BCC = 2;

    private ArrayList recipientList = new ArrayList();

    private String messageId; //邮件代号
    private String subject; //主题（原始邮件的主题 也就是文件名，但是中途可能有人将文件名称进行修改，所以引入了 fileName这个字段）mj
    private String content; //正文
    private boolean isContentHtml; //正文类型
    private String fileName;//文件名mj

    private HashMap files = new HashMap();
    
    private ArrayList conentIdList = new ArrayList();

    private Recipienter sender;

    private java.util.Date date;
    
    private String headCharset;
    
    private int mailSize;
     
    private boolean begReplay=false; //请求回执
    
    

    public boolean isBegReplay() {
		return begReplay;
	}



	public void setBegReplay(boolean begReplay) {
		this.begReplay = begReplay;
	}



	/**
     * 清除所有附件
     */
    public void clearFile() {
        files.clear();
    }



    public int getMailSize() {
		return mailSize;
	}



	public void setMailSize(int mailSize) {
		this.mailSize = mailSize;
	}



	public String getHeadCharset() {
		return headCharset;
	}



	public void setHeadCharset(String headCharset) {
		this.headCharset = headCharset;
	}



	public String getMessageId() {
		return messageId;
	}



	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}



	/**
     * 添加附件
     * @param fileName String 附件名称
     * @param file File
     */
    public void putFile(String fileName, File file) {
        files.put(fileName, file);
    }

    /**
     * 添加附件
     * @param fileName String 附件名称
     * @param file byte[] 文件数组byte[]用于内存附件
     */
    public void putFile(String fileName, byte[] file) {
        files.put(fileName, file);
    }

    /**
     * 添加附件
     * @param fileName String 附件名称
     * @param file Object
     */
    public void putFile(String fileName, InputStream file) {
        files.put(fileName, file);
    }
    
    public void putFileContentId(String contentId,String fileName){
    	conentIdList.add(new String[]{contentId, fileName});
    }
    

    /**
     * 添加附件
     * @param fileName String 附件名称
     * @param file Object String文本
     */
    public void putFile(String fileName, String file) {
        files.put(fileName, file);
    }

    public HashMap getFiles(){
        return files;
    }


    /**
     * 设置邮件主题
     * @param subject String
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setSender(Recipienter sender) {
        this.sender = sender;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public void setIsContentHtml(boolean isContentHtml) {
        this.isContentHtml = isContentHtml;
    }

    public void setFiles(HashMap files) {
        this.files = files;
    }

    public void setRecipientList(ArrayList recipientList) {
        this.recipientList = recipientList;
    }


    /**
     * 取主题 
     * @return String
     */
    public String getSubject(){
        return this.subject;
    }

    /**
     * 设置邮件 正文
     * @param isContentHtml boolean 正文是否是html格式
     * @param content String
     */
    public void setContent(boolean isContentHtml, String content) {
        this.isContentHtml = isContentHtml;
        this.content = content;
    }
    
    public ArrayList getContentIdList(){
    	return this.conentIdList;
    }


    public String getContent(){    	  	
        return content;
    }

    public Recipienter getSender() {
        return sender;
    }

    public java.util.Date getDate() {
        return date;
    }

    public boolean isIsContentHtml() {
        return isContentHtml;
    }

    /**
     * 增加接收者
     * @param type int 接收者的类型，@see TO,CC,BCC
     * @param emailAddress String
     */
    public void addRecipient(int type, String emailAddress,String display) {
        recipientList.add(new Recipienter(type, emailAddress,display));
    }
    /**
     * 取接收者
     * @return ArrayList
     */
    public ArrayList<Recipienter> getRecipientList(){
        return recipientList;
    }

    public Recipienter newRecipienter(int type, String emailAddress,String display){
        return new Recipienter(type,emailAddress,display);
    }

    public class Recipienter {
        public int type;
        public String emailAddress;
        public String display;
        public Recipienter(int type, String emailAddress,String display) {
            this.type = type;
            this.emailAddress = emailAddress;
            this.display = display;
        }
        public String toString(){
            return display+"<"+emailAddress+">";
        }
    }

	public String getFileName() {
		return fileName;
	}



	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


}
