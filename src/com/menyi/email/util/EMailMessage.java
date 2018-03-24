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
 * <p>Company: ������</p>
 *
 * @author ������
 * @version 1.0
 */
public class EMailMessage {
    /**
     * ������
     */
    public static int TO = 0;
    /**
     * ����
     */
    public static int CC = 1;
    /**
     * ä����
     */
    public static int BCC = 2;

    private ArrayList recipientList = new ArrayList();

    private String messageId; //�ʼ�����
    private String subject; //���⣨ԭʼ�ʼ������� Ҳ�����ļ�����������;�������˽��ļ����ƽ����޸ģ����������� fileName����ֶΣ�mj
    private String content; //����
    private boolean isContentHtml; //��������
    private String fileName;//�ļ���mj

    private HashMap files = new HashMap();
    
    private ArrayList conentIdList = new ArrayList();

    private Recipienter sender;

    private java.util.Date date;
    
    private String headCharset;
    
    private int mailSize;
     
    private boolean begReplay=false; //�����ִ
    
    

    public boolean isBegReplay() {
		return begReplay;
	}



	public void setBegReplay(boolean begReplay) {
		this.begReplay = begReplay;
	}



	/**
     * ������и���
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
     * ��Ӹ���
     * @param fileName String ��������
     * @param file File
     */
    public void putFile(String fileName, File file) {
        files.put(fileName, file);
    }

    /**
     * ��Ӹ���
     * @param fileName String ��������
     * @param file byte[] �ļ�����byte[]�����ڴ渽��
     */
    public void putFile(String fileName, byte[] file) {
        files.put(fileName, file);
    }

    /**
     * ��Ӹ���
     * @param fileName String ��������
     * @param file Object
     */
    public void putFile(String fileName, InputStream file) {
        files.put(fileName, file);
    }
    
    public void putFileContentId(String contentId,String fileName){
    	conentIdList.add(new String[]{contentId, fileName});
    }
    

    /**
     * ��Ӹ���
     * @param fileName String ��������
     * @param file Object String�ı�
     */
    public void putFile(String fileName, String file) {
        files.put(fileName, file);
    }

    public HashMap getFiles(){
        return files;
    }


    /**
     * �����ʼ�����
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
     * ȡ���� 
     * @return String
     */
    public String getSubject(){
        return this.subject;
    }

    /**
     * �����ʼ� ����
     * @param isContentHtml boolean �����Ƿ���html��ʽ
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
     * ���ӽ�����
     * @param type int �����ߵ����ͣ�@see TO,CC,BCC
     * @param emailAddress String
     */
    public void addRecipient(int type, String emailAddress,String display) {
        recipientList.add(new Recipienter(type, emailAddress,display));
    }
    /**
     * ȡ������
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
