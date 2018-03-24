/*
 * Created on 2004-4-26
 */
package com.menyi.email.util;


import java.io.*;
import java.security.Security;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;

import net.fortuna.ical4j.data.CalendarBuilder;


import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.email.EMailMgt;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ClientConnection;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.ServerConnection;
import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.util.BASE64DecoderStream;

/**
 * @author Bromon
 */
public class AIOEMail {
	public static Logger emailLog= Logger.getLogger("EmailLog");
	
    private String emailAddress = "";
    private String smtpHost = ""; 
    private boolean smtpAuth;
    private String smtpUser = "";
    private String smtpPassword = "";
    private String pop3Host = "";
    private String pop3User = "";
    private String pop3Password = "";
    private int smtpPort = 25;
    private int pop3Port = 110;
    private String displayName="";
    public String accountId = ""; //�ʼ����ʻ�����
    private boolean popssl;
    private boolean smtpssl;
    public boolean autoAssign;
    public String accountUserId;
    
    private int connectTimes;
    
    public int retentDay;
    
    public Set<String[]> setOfRules;
    
    private static Pattern encodeStringPattern = Pattern.compile("=\\?([^\\?]+?)\\?(B|Q)\\?([^\\?]+?)\\?=", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private final String[] CHARTSET_HEADER = new String[] { "Subject", "From", "To", "Cc", "Delivered-To" };
    
    
    public HashMap<String,Message> msgMap = new HashMap<String,Message>();
    public ArrayList<Message> newMsgList = new ArrayList<Message>();
    public Message curMsg; //��ǰ����Ϣ�������ڷֲ�����
    public EMailMessage curem; //��ǰ����Ϣ�������ڷֲ�����
    private POP3Folder inbox;
    private Store store;
    
    public static String decodePass(String pass){
    	if(pass.startsWith("ENC:")){
    		ServerConnection con = new ServerConnection("", new byte[] {0x11, 0x22, 0x4F, 0x58, (byte) 0x88, 0x10, 0x40, 0x38
    	            , 0x28, 0x25, 0x79, 0x51, (byte) 0xCB, (byte) 0xDD, 0x55, 0x66
    	            , 0x77, 0x29, 0x74, (byte) 0x98, 0x30, 0x40, 0x36, (byte) 0xE2});

    	        byte[] encoded = ClientConnection.hexStringToBytes(pass.substring(4));
    	        byte[] srcBytes = con.decryptMode(encoded);
    	        try {
					return new String(srcBytes,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					return "";
				}
    	}else{
    		return pass;
    	}
    }


    public void setAccount(String emailAddress, String pop3Host, String pop3User, String pop3password, boolean smtpAuth, String smtpHost,
                           String smtpUser, String smtpPassword,int pop3Port,int smtpPort,String displayName,String accountId,
                           String accountUserId,boolean popssl,boolean smtpssl,boolean autoAssign) {
        this.emailAddress = emailAddress;
        this.pop3Host = pop3Host;
        this.pop3User = pop3User;
        this.pop3Password = decodePass(pop3password);
        this.smtpHost = smtpHost;
        this.smtpAuth = smtpAuth;
        this.smtpUser = smtpUser;
        this.smtpPassword = decodePass(smtpPassword);
        this.smtpPort = smtpPort ;
        this.pop3Port = pop3Port ;
        this.displayName = displayName;
        this.accountId = accountId;
        this.popssl = popssl;
        this.smtpssl = smtpssl;
        this.autoAssign = autoAssign;
        this.accountUserId=accountUserId;
    }
    
    public void send(EMailMessage sendMessage) throws Exception {
    	send(sendMessage,null);
    }
    
    
    /**
     * �����ʼ�����һ��ӿڵ��ã�����define�е���
     * @param emailAddress ���ͷ��ʼ���ַ
     * @param displayName  ���ͷ���ʾ����
     * @param smtpAuth     true:���ͷ�������Ҫ��Ȩ��false��������Ȩ
     * @param smtpHost     �ʼ����ͷ�������ַ
     * @param smtpPort     �ʼ����ͷ������˿�
     * @param smtpUser     �ʼ������ʺ�
     * @param smtpPassword �ʼ���������
     * @param smtpssl      �ʼ����ͷ������Ƿ�����ssl���� true:�ǣ�false����
     * @param toEmail      �ʼ����͵���ַ�������ַ��";"�ŷָ�
     * @param subject      �ʼ�����
     * @param content      �ʼ�����
     * @return
     */
    public Result send(String emailAddress,String displayName, String smtpAuth, String smtpHost,String smtpPort,
            String smtpUser, String smtpPassword,String smtpssl,String toEmail,String subject,String content) {
    	Result rs = new Result();
        try {
	        Properties props = new Properties();
	        
	        //ssl ----
	        if(smtpssl.equals("true")){	
	        	Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
	            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	        	props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
	        	props.setProperty("mail.smtp.socketFactory.fallback", "false");
	        	props.setProperty("mail.smtp.socketFactory.port", smtpPort+"");
	        }
	        //ssl ----
	
	        
	        props.put("mail.smtp.host", smtpHost);
	        //ָ��SMTP������
	        props.put("mail.smtp.auth", smtpAuth.equals("true") ? "true" : "false");
	
	        props.put("mail.stmp.timeout", 120000); //�����ӳ�ʱ
	
	        if (Integer.parseInt(smtpPort) != 25) {
	            props.put("mail.stmp.port", smtpPort);
	        }
	        //ָ���Ƿ���ҪSMTP��֤

            Session mailSession = Session.getInstance(props);
            //�Ƿ��ڿ���̨��ʾdebug��Ϣ
            mailSession.setDebug(false);
            Message message = new MimeMessage(mailSession);
            message.setSentDate(new Date());
            //������
            message.setFrom(new InternetAddress(emailAddress,MimeUtility.encodeText( displayName,"utf-8","B")));
            //�ռ���
            if(toEmail ==null || toEmail.length() ==0){
            	rs.retCode= ErrorCanst.DEFAULT_FAILURE;
            	rs.retVal = "������Ϊ��";
            	return rs;
            }
            String[] toEmails = toEmail.split(";");
            for (String o : toEmails) {
                message.addRecipient(Message.RecipientType.TO,
                                         new InternetAddress(o,MimeUtility.encodeText( o,"utf-8","B")));
            }
            message.setSubject(MimeUtility.encodeText( subject,"utf-8","B"));//��������
            Multipart container = new MimeMultipart(); //������
            MimeBodyPart textBodyPart = new MimeBodyPart();
            
            AIOEMail.emailLog.info("==================System encoding ====="+System.getProperty("file.encoding")); 
            
            //��������
            textBodyPart.setContent(new String( content.getBytes(),"utf-8"), "text/html;charset=utf-8");
            container.addBodyPart(textBodyPart); //�����ķ���������

            //����Ƕ��ʽͼƬ���������ݵ�ΪReadFile��ͼƬ���ҵ�����Ϊ �����ϴ�
        	//�ʼ�ת���е�ͼƬ
        	Pattern rfp = Pattern.compile("(/ReadFile[^\\s\\\"]*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        	Matcher mat= rfp.matcher(content);
        	int i = 0;
        	while(mat.find()){
        		String readFile = mat.group(1);
        		File f = null;
        		MimeBodyPart fileBodyPart = new MimeBodyPart();
                DataSource ds = null;
        		if(readFile.indexOf("email")>-1){
        			String fileName = readFile.substring(readFile.indexOf("fileName=")+9);
        			fileName = java.net.URLDecoder.decode(fileName, "UTF-8");
        			f = new File(BaseEnv.FILESERVERPATH+"/email/"+fileName);
        		}
        		if(f.exists()){
        			ds = new FileDataSource(f);
            		//Ҫ���͵ĸ���
                    fileBodyPart.setDataHandler(new DataHandler(ds));
                    sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();                    
                    //fileBodyPart.setFileName("=?GBK?B?" + enc.encode((f.getName()).getBytes()) + "?=");
                    fileBodyPart.setHeader("Content-ID", "<__" +i+"@koronsoft.com>");
                    container.addBodyPart(fileBodyPart); 
                    content = content.substring(0,content.indexOf(readFile))+"cid:__" +i+"@koronsoft.com"+content.substring(content.indexOf(readFile)+readFile.length());
        		}
                i++; 
        	}
        	//�༭��ֱ���ϴ���ͼƬ
        	rfp = Pattern.compile("src=\\\"(/upload/[^\\s\\\"]*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        	mat= rfp.matcher(content);
        	while(mat.find()){ 
        		String readFile = mat.group(1);            		
        		MimeBodyPart fileBodyPart = new MimeBodyPart();
        		File f = new File(BaseEnv.systemRealPath+readFile);
        		if(!f.exists()){
        			continue;
        		}
        		DataSource ds = new FileDataSource(f);
        		//Ҫ���͵ĸ���
                fileBodyPart.setDataHandler(new DataHandler(ds));
                sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();                    
                //fileBodyPart.setFileName("=?GBK?B?" + enc.encode((f.getName()).getBytes()) + "?=");
                fileBodyPart.setHeader("Content-ID", "<__" +i+"@koronsoft.com>");
                container.addBodyPart(fileBodyPart); 
                content = content.substring(0,content.indexOf("src=\""+readFile)+5)+"cid:__" +i+"@koronsoft.com"+content.substring(content.indexOf

("src=\""+readFile)+("src=\""+readFile).length());
 
                    i++; 
            }
            	
            textBodyPart.setContent(content, "text/html;charset=utf-8");  
            
            
            message.setContent(container);

            //�ʼ�����
            message.saveChanges();

            Transport transport = mailSession.getTransport("smtp");
            transport.connect(smtpHost, smtpUser, smtpPassword);
            message.setHeader("X-Mailer", "EXC Mail Sender 1.0");
            // message.setHeader("To", "\"ksalon\" <ksalon@163.com>, \"info\" <info@koronsoft.com>");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            
            return rs;
        } catch (Exception e) {
            AIOEMail.emailLog.error("AIOEMail Send Error", e);
            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
            rs.setRetVal("�����ʼ�ʧ��"+e);
            return rs;
        }

    }

    public void send(EMailMessage sendMessage,String userId) throws Exception {
        Properties props = new Properties();
        
        //ssl ----
        if(smtpssl){	
        	Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        	props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        	props.setProperty("mail.smtp.socketFactory.fallback", "false");
        	props.setProperty("mail.smtp.socketFactory.port", smtpPort+"");
        }
        //ssl ----

        
        props.put("mail.smtp.host", smtpHost);
        //ָ��SMTP������
        props.put("mail.smtp.auth", smtpAuth ? "true" : "false");

        props.put("mail.stmp.timeout", 120000); //�����ӳ�ʱ

        if (smtpPort != 25) {
            props.put("mail.stmp.port", smtpPort);
        }
        //ָ���Ƿ���ҪSMTP��֤
        try {
            Session mailSession = Session.getInstance(props);
            //�Ƿ��ڿ���̨��ʾdebug��Ϣ
            mailSession.setDebug(false);
            Message message = new MimeMessage(mailSession);
            message.setSentDate(new Date());
            if(sendMessage.isBegReplay()){
            	message.setHeader("Disposition-Notification-To",emailAddress);
            }
            //������
            message.setFrom(new InternetAddress(emailAddress,MimeUtility.encodeText( displayName,"utf-8","B")));
            //�ռ���
            for (Object o : sendMessage.getRecipientList()) {
                EMailMessage.Recipienter rc = (EMailMessage.Recipienter) o;
                if (rc.type == EMailMessage.TO) {
                    message.addRecipient(Message.RecipientType.TO,
                                         new InternetAddress(rc.emailAddress,MimeUtility.encodeText( rc.display,"utf-8","B")));
                } else if (rc.type == EMailMessage.CC) {
                    message.addRecipient(Message.RecipientType.CC,
                                         new InternetAddress(rc.emailAddress,MimeUtility.encodeText( rc.display,"utf-8","B")));
                } else if (rc.type == EMailMessage.BCC) {
                    message.addRecipient(Message.RecipientType.BCC,
                                         new InternetAddress(rc.emailAddress,MimeUtility.encodeText( rc.display,"utf-8","B")));
                }
            }
            message.setSubject(MimeUtility.encodeText( sendMessage.getSubject(),"utf-8","B"));//��������
            Multipart container = new MimeMultipart(); //������
            MimeBodyPart textBodyPart = new MimeBodyPart();
            
            AIOEMail.emailLog.info("==================System encoding ====="+System.getProperty("file.encoding")); 
            
            //��������
            if (sendMessage.isIsContentHtml()) {
                textBodyPart.setContent(new String( sendMessage.getContent().getBytes(),"utf-8"), "text/html;charset=utf-8");
            } else {
                textBodyPart.setText(new String( sendMessage.getContent().getBytes(),"utf-8"));
            }
            container.addBodyPart(textBodyPart); //�����ķ���������
            
            

            //���Ӹ���
            for (Object o : sendMessage.getFiles().entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                MimeBodyPart fileBodyPart = new MimeBodyPart();
                DataSource ds = null;
                if (entry.getValue() instanceof File) {
                	File f = (File) entry.getValue();
                	if(f.exists()){
                		ds = new FileDataSource(f);
                	}
                } else if (entry.getValue() instanceof byte[]) {
                    ds = new ByteArrayDataSource((byte[]) entry.getValue(), "application/octet-stream");
                } else if (entry.getValue() instanceof InputStream) {
                    ds = new ByteArrayDataSource((InputStream) entry.getValue(), "application/octet-stream");
                } else if (entry.getValue() instanceof String) {
                    ds = new ByteArrayDataSource((String) entry.getValue(), "application/octet-stream");
                }

                //Ҫ���͵ĸ���
                fileBodyPart.setDataHandler(new DataHandler(ds));
                sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                fileBodyPart.setFileName(MimeUtility.encodeText( (String) entry.getKey(),"utf-8","B"));

                container.addBodyPart(fileBodyPart);
            }
            //          ����Ƕ��ʽͼƬ���������ݵ�ΪReadFile��ͼƬ���ҵ�����Ϊ �����ϴ�
            if (sendMessage.isIsContentHtml()) {
            	String content = sendMessage.getContent();
            	//�ʼ�ת���е�ͼƬ
            	Pattern rfp = Pattern.compile("(/ReadFile[^\\s\\\"]*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            	Matcher mat= rfp.matcher(content);
            	int i = 0;
            	while(mat.find()){
            		String readFile = mat.group(1);
            		File f = null;
            		MimeBodyPart fileBodyPart = new MimeBodyPart();
                    DataSource ds = null;
            		if(readFile.indexOf("email")>-1){
            			String fileName = readFile.substring(readFile.indexOf("fileName=")+9);
            			fileName = java.net.URLDecoder.decode(fileName, "UTF-8");
            			f = new File(BaseEnv.FILESERVERPATH+"/email/"+fileName);
            		}
            		if(f.exists()){
            			ds = new FileDataSource(f);
	            		//Ҫ���͵ĸ���
	                    fileBodyPart.setDataHandler(new DataHandler(ds));
	                    sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();                    
	                    //fileBodyPart.setFileName("=?GBK?B?" + enc.encode((f.getName()).getBytes()) + "?=");
	                    fileBodyPart.setHeader("Content-ID", "<__" +i+"@koronsoft.com>");
	                    container.addBodyPart(fileBodyPart); 
	                    content = content.substring(0,content.indexOf(readFile))+"cid:__" +i+"@koronsoft.com"+content.substring(content.indexOf(readFile)+readFile.length());
            		}
                    i++; 
            	}
            	//�༭��ֱ���ϴ���ͼƬ
            	rfp = Pattern.compile("src=\\\"(/upload/[^\\s\\\"]*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            	mat= rfp.matcher(content);
            	while(mat.find()){ 
            		String readFile = mat.group(1);            		
            		MimeBodyPart fileBodyPart = new MimeBodyPart();
            		File f = new File(BaseEnv.systemRealPath+readFile);
            		if(!f.exists()){
            			continue;
            		}
            		DataSource ds = new FileDataSource(f);
            		//Ҫ���͵ĸ���
                    fileBodyPart.setDataHandler(new DataHandler(ds));
                    sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();                    
                    //fileBodyPart.setFileName("=?GBK?B?" + enc.encode((f.getName()).getBytes()) + "?=");
                    fileBodyPart.setHeader("Content-ID", "<__" +i+"@koronsoft.com>");
                    container.addBodyPart(fileBodyPart); 
                    content = content.substring(0,content.indexOf("src=\""+readFile)+5)+"cid:__" +i+"@koronsoft.com"+content.substring(content.indexOf

("src=\""+readFile)+("src=\""+readFile).length());
 
                    i++; 
            	}
            	
            	textBodyPart.setContent(content, "text/html;charset=utf-8");  
            }
            
            message.setContent(container);

            //�ʼ�����
            message.saveChanges();

            Transport transport = mailSession.getTransport("smtp");
            transport.connect(smtpHost, smtpUser, smtpPassword);
            message.setHeader("X-Mailer", "EXC Mail Sender 1.0");
            // message.setHeader("To", "\"ksalon\" <ksalon@163.com>, \"info\" <info@koronsoft.com>");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            
            if(userId!=null && !"".equals(userId)){
            	//�ⲿ�ʼ�(�޸Ŀͻ�����1.���������ϵʱ�䣬2.����ͻ���־)
            	new EMailMgt().emailRelateClient(sendMessage.getRecipientList(),userId);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            AIOEMail.emailLog.error("AIOEMail Send Error", e);
            throw e;
        }

    }
    
    /*
     * ��ȡeml��ʽ�ļ�
     * 
     */
    public String readFileHeader(InputStream is)  throws Exception {
    	try {
	    	Properties props = new Properties();
	    	Session mailSession = Session.getInstance(props);
	    	
	    	this.curMsg = new MimeMessage(mailSession,is);	  
	    	return ((MimeMessage)curMsg).getMessageID();
    	} catch (Exception e) {
            e.printStackTrace();
            AIOEMail.emailLog.error("AIOEMail Send Error", e);
            throw e;
        }
    }
    /*
     * ��ȡeml��ʽ�ļ���
     * 
     */
    public EMailMessage readFileBody()  throws Exception {
    	try {
			this.curem = new EMailMessage();
			this.handle(curMsg, curem,false,"");			
			this.curem.setMessageId(((MimeMessage)curMsg).getMessageID());				
			return receiveBody(false,"");
    	} catch (Exception e) {
            e.printStackTrace();
            AIOEMail.emailLog.error("AIOEMail Send Error", e);
            throw e;
        }
    }
    /**
     * ʹ��ָ���������������ļ�
     * @param is
     * @param charset
     * @return
     */
    public EMailMessage readFileByCharset(InputStream is,String charset,String accountId)  throws Exception{
    	try {
    		this.accountId = accountId;
	    	Properties props = new Properties();
	    	Session mailSession = Session.getInstance(props);
	    	
	    	this.curMsg = new MimeMessage(mailSession,is);	  
	    	this.curem = new EMailMessage();
			this.handle(curMsg, curem,true,charset);			
			this.curem.setMessageId(((MimeMessage)curMsg).getMessageID());				
			return receiveBody(true,charset);
    	} catch (Exception e) {
            e.printStackTrace();
            AIOEMail.emailLog.error("AIOEMail Send Error", e);
            throw e;
        }
    }
    
    public static InputStream readFileFormEml(String emlFile,String fileName,String thecharset)  throws Exception{
    	try {
	    	Properties props = new Properties();
	    	Session mailSession = Session.getInstance(props);
	    	
	    	MimeMessage msg = new MimeMessage(mailSession,new FileInputStream(emlFile)); 
	    	Object ocon = msg.getContent();
	    	if(ocon instanceof Multipart){
		        Multipart mp = (Multipart) ocon;
		        return recGetFile(mp,0,fileName,thecharset);	       
		       
	    	}else if(ocon instanceof BASE64DecoderStream){
	    		return (BASE64DecoderStream)ocon;
	    	}	
	    	return null;
    	} catch (Exception e) {
            e.printStackTrace();
            AIOEMail.emailLog.error("AIOEMail Send Error", e);
            throw e;
        }
    }
    
    private static InputStream recGetFile(Multipart mp,int recCount,String thefileName,String thecharset) throws Exception{
    	
    	if(recCount >300) return null; //Ϊ�ݹ����ô�����������ѭ��
    	
        int mpCount = mp.getCount();
        String disposition;
        
        //Miltipart������,���ڳ��˶��part,����������
        for (int m = 0; m < mpCount; m++) {
            BodyPart part = mp.getBodyPart(m);
           //����Content-Type �ж��Ƿ��Ǹ�������ΪǶ�����ݣ�dispositionΪ��
            boolean isAttach = false;
            if(part instanceof MimeBodyPart){
            	MimeBodyPart mbp = (MimeBodyPart) part;
                String[] hs = mbp.getHeader("Content-Type");
                if(hs != null && hs.length >0 && ( hs[0].indexOf("octet-stream")>-1 || hs[0].indexOf("image")>-1) ){
                	isAttach = true;
                }
            }
            disposition = part.getDisposition();
            if (isAttach || ((disposition != null) &&
                ((disposition.equals(Part.ATTACHMENT) ||
                  (disposition.equals(Part.INLINE)&&
                		  part.getFileName() !=null &&
                		  part.getFileName().length() !=0))))) {
            	InputStream is = readAttach(part,thefileName,thecharset);
                if(is != null){
                	recCount = 100;
                	return is;
                }
                
            } else  {
                MimeBodyPart mbp = (MimeBodyPart) part;                
                
                if (mbp.isMimeType("multipart/alternative") || mbp.isMimeType("multipart/related")|| mbp.isMimeType("multipart/mixed")) {
                	InputStream is = recGetFile((Multipart) mbp.getContent(),recCount++,thefileName,thecharset);
                	if(is != null){
                		recCount = 100;
                		return is;
                	}
                }
            } 
        }
        return null;
    }
    
    private static InputStream readAttach(BodyPart part,String  thefileName,String charset) throws Exception {
        String temp = part.getFileName();
        if(temp == null) return null;
        //�õ�δ������ĸ�������
        String fileName;        
          
        try {
        	temp = AIOMimeUtility.decodeText(temp,true,charset); //�����½���
        } catch (Exception e1) {
            AIOEMail.emailLog.error("AIOEMail.handle  decode Subject", e1);
        }
        
        fileName = temp;
        //ȥ���ļ��пո�
        fileName = fileName.replaceAll("[\\s:\\[\\]]*", "");
        
        if(!fileName.equals(thefileName)){
        	return null;
        }      
        
        //fileNameǰ��ʱ��                
        return part.getInputStream();
    }
    
    
    public void saveFile(EMailMessage sendMessage,OutputStream os) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        //ָ��SMTP������
        props.put("mail.smtp.auth", smtpAuth ? "true" : "false");

        props.put("mail.stmp.timeout", 120000); //�����ӳ�ʱ

        if (smtpPort != 25) {
            props.put("mail.stmp.port", smtpPort);
        }
        //ָ���Ƿ���ҪSMTP��֤
        try {
            Session mailSession = Session.getInstance(props);
            //�Ƿ��ڿ���̨��ʾdebug��Ϣ
            mailSession.setDebug(false);
            Message message = new MimeMessage(mailSession);
            message.setSentDate(sendMessage.getDate());

            //������
            message.setFrom(new InternetAddress(sendMessage.getSender().emailAddress,MimeUtility.encodeText( sendMessage.getSender().display,"utf-8","B")));
            //�ռ���
            for (Object o : sendMessage.getRecipientList()) {
                EMailMessage.Recipienter rc = (EMailMessage.Recipienter) o;
                if (rc.type == EMailMessage.TO) {
                    message.addRecipient(Message.RecipientType.TO,
                                         new InternetAddress(rc.emailAddress,MimeUtility.encodeText( rc.display,"utf-8","B")));
                } else if (rc.type == EMailMessage.CC) {
                    message.addRecipient(Message.RecipientType.CC,
                                         new InternetAddress(rc.emailAddress,MimeUtility.encodeText( rc.display,"utf-8","B")));
                } else if (rc.type == EMailMessage.BCC) {
                    message.addRecipient(Message.RecipientType.BCC,
                                         new InternetAddress(rc.emailAddress,MimeUtility.encodeText( rc.display,"utf-8","B")));
                }
            }
            message.setSubject(MimeUtility.encodeText( sendMessage.getSubject(),"utf-8","B"));//��������
            Multipart container = new MimeMultipart(); //������
            MimeBodyPart textBodyPart = new MimeBodyPart();
            
            AIOEMail.emailLog.info("==================System encoding ====="+System.getProperty("file.encoding")); 
            
            //��������
            if (sendMessage.isIsContentHtml()) {
                textBodyPart.setContent(new String( sendMessage.getContent().getBytes(),"utf-8"), "text/html;charset=utf-8");
            } else {
                textBodyPart.setText(new String( sendMessage.getContent().getBytes(),"utf-8"));
            }
            container.addBodyPart(textBodyPart); //�����ķ���������
            
            

            //���Ӹ���
            for (Object o : sendMessage.getFiles().entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                MimeBodyPart fileBodyPart = new MimeBodyPart();
                DataSource ds = null;
                if (entry.getValue() instanceof File) {
                    ds = new FileDataSource((File) entry.getValue());
                } else if (entry.getValue() instanceof byte[]) {
                    ds = new ByteArrayDataSource((byte[]) entry.getValue(), "application/octet-stream");
                } else if (entry.getValue() instanceof InputStream) {
                    ds = new ByteArrayDataSource((InputStream) entry.getValue(), "application/octet-stream");
                } else if (entry.getValue() instanceof String) {
                    ds = new ByteArrayDataSource((String) entry.getValue(), "application/octet-stream");
                }

                //Ҫ���͵ĸ���
                fileBodyPart.setDataHandler(new DataHandler(ds));
                sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                fileBodyPart.setFileName(MimeUtility.encodeText( (String) entry.getKey(),"utf-8","B"));

                container.addBodyPart(fileBodyPart);
            }
            //          ����Ƕ��ʽͼƬ���������ݵ�ΪReadFile��ͼƬ���ҵ�����Ϊ �����ϴ�
            if (sendMessage.isIsContentHtml()) {
            	String content = sendMessage.getContent();
            	//�ʼ�ת���е�ͼƬ
            	Pattern rfp = Pattern.compile("(/ReadFile[^\\s\\\"]*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            	Matcher mat= rfp.matcher(content);
            	int i = 0;
            	while(mat.find()){
            		String readFile = mat.group(1);
            		File f = null;
            		MimeBodyPart fileBodyPart = new MimeBodyPart();
                    DataSource ds = null;
            		if(readFile.indexOf("email")>-1){
            			String fileName = readFile.substring(readFile.indexOf("fileName=")+9);
            			fileName = java.net.URLDecoder.decode(fileName, "UTF-8");
            			f = new File(BaseEnv.FILESERVERPATH+"/email/"+fileName);
            		}
            		ds = new FileDataSource(f);
            		//Ҫ���͵ĸ���
                    fileBodyPart.setDataHandler(new DataHandler(ds));
                    sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();                    
                    //fileBodyPart.setFileName("=?GBK?B?" + enc.encode((f.getName()).getBytes()) + "?=");
                    fileBodyPart.setHeader("Content-ID", "<__" +i+"@koronsoft.com>");
                    container.addBodyPart(fileBodyPart); 
                    content = content.substring(0,content.indexOf(readFile))+"cid:__" +i+"@koronsoft.com"+content.substring(content.indexOf(readFile)+readFile.length

());

                    i++; 
            	}
            	//�༭��ֱ���ϴ���ͼƬ
            	rfp = Pattern.compile("src=\\\"(/upload/[^\\s\\\"]*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            	mat= rfp.matcher(content);
            	while(mat.find()){ 
            		String readFile = mat.group(1);
            		MimeBodyPart fileBodyPart = new MimeBodyPart();
            		File f = new File(BaseEnv.systemRealPath+readFile);
            		DataSource ds = new FileDataSource(f);
            		//Ҫ���͵ĸ���
                    fileBodyPart.setDataHandler(new DataHandler(ds));
                    sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();                    
                    //fileBodyPart.setFileName("=?GBK?B?" + enc.encode((f.getName()).getBytes()) + "?=");
                    fileBodyPart.setHeader("Content-ID", "<__" +i+"@koronsoft.com>");
                    container.addBodyPart(fileBodyPart); 
                    content = content.substring(0,content.indexOf("src=\""+readFile)+5)+"cid:__" +i+"@koronsoft.com"+content.substring(content.indexOf

("src=\""+readFile)+("src=\""+readFile).length());
 
                    i++; 
            	}
            	
            	textBodyPart.setContent(content, "text/html;charset=utf-8");  
            }
            
            message.setContent(container);

            //�ʼ�����
            message.saveChanges();

            message.writeTo(os);
        } catch (Exception e) {
            e.printStackTrace();
            AIOEMail.emailLog.error("AIOEMail Send Error", e);
            throw e;
        }
    }
    
  
    //����Զ�̷��������������ʼ�����
    public String[] connect() throws Exception{
    	msgMap.clear();
    	newMsgList.clear();
        try {
            ArrayList list = new ArrayList();
	        openPop3(true);
	        
	        int count = inbox.getMessageCount();
            Message[] msgs = inbox.getMessages();
            FetchProfile profile = new FetchProfile();
            //profile.add(FetchProfile.Item.ENVELOPE);  //�ⲽ����ȡ��Ϣͷ��Ӱ���ٶȣ����Բ�ȡ
            profile.add(UIDFolder.FetchProfileItem.UID); 
            inbox.fetch(msgs, profile);
            
            for(Message msg:msgs){
            	String uid = inbox.getUID(msg);
            	if(msgMap.get(uid) ==null){
	            	msgMap.put(uid, msg);
	            	newMsgList.add(msg);
            	}
            }            
            System.out.println("-----------------�ʼ�����:"+msgs.length+"-------");
            return msgMap.keySet().toArray(new String[0]);
        } catch (Exception ex) {
            //ex.printStackTrace();
            //AIOEMail.emailLog.error("AIOEMail receive Error", ex);
            throw ex;
        } 
    }
    
    private void openPop3(boolean isConnect)  throws Exception{
    	Properties prop = new Properties();
        
        
        //ssl ----
        if(popssl){	
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
            prop.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
            prop.setProperty("mail.pop3.socketFactory.fallback", "false");
            prop.setProperty("mail.pop3.socketFactory.port", pop3Port+"");
        }
        //ssl ----
                    
        prop.put("mail.pop3.host", pop3Host);
        if (pop3Port != 110) {
            prop.put("mail.pop3.port", pop3Port);
        } 
        Session session = Session.getInstance(prop);
        //session.setDebug(true);
        store = session.getStore("pop3");
        try{
        	store.connect(pop3Host, pop3User, pop3Password);
        	inbox = (POP3Folder)store.getDefaultFolder().getFolder("INBOX");
        	inbox.open(Folder.READ_WRITE);
        }catch (Exception e) {
			AIOEMail.emailLog.error("AIOMail openPop3() �����ʼ����������󡣡���",e) ;
			if(isConnect){
				//�����һ������ʱ����
				throw e;
			}
		}
    }


    
    public EMailMessage receiveNextHeader() throws Exception {
    	if(this.newMsgList.size() == 0){
			return null;
		}
		Message tempmsg = this.newMsgList.remove(this.newMsgList.size()-1);
		for(int i=0;i<3;i++){
			try {			
				String messageId = inbox.getUID(tempmsg);
				
				this.curMsg = inbox.getMessage(tempmsg.getMessageNumber());
				
				this.curem = new EMailMessage();
				this.handle(curMsg, curem,false,"");
				
				this.curem.setMessageId(messageId);	
//				System.out.println("-------------"+(this.newMsgList.size()+1)+curem.getSender().emailAddress+"   "+curem.getSubject()+"   "+curem.getDate()+"--------------");
				
				return this.curem;
			} catch (Exception ex) {
				if(ex.toString().indexOf("Connection reset") > -1 && connectTimes<1){
					System.out.println("-----------------------------------Connection reset----------------------");
					//�����д洢���ʼ�̫�࣬ÿ��������1��5ǧ����ʼ����ʼ���̫�࣬�����������ʼ�ͷҪ�����ݿ��д����ʱ�䳬��10����ӣ�������Ͱ�������ʼ������ӳ�ʱʱ��
					//  ��̫��,�������䳬ʱ�ж�����,�����һ�γ��������Ļ��ᡣ
					openPop3(false);
					connectTimes ++;
				}else{
					ex.printStackTrace();
					AIOEMail.emailLog.error("AIOEMail receive Error", ex);
					throw ex;
				}
			}
		}
		return null;
	}
    public EMailMessage receiveBody(boolean useCharset,String thecharset) throws Exception {
		try {
			if (retentDay == 0)
				curMsg.setFlag(Flags.Flag.DELETED, true);

			if (curMsg.isMimeType("text/*")) {
				return handleText(curMsg, curem,useCharset,thecharset);
			} else {
				return handleMultipart(curMsg, curem,useCharset,thecharset);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			AIOEMail.emailLog.error("AIOEMail receive Error", ex);
			throw ex;
		}		
	}
    public boolean isOpen(){
    	return inbox.isOpen();
    }
    
    private static String md5(String strs) {
		String s = null;
		char hexDigits[] = { // �������ֽ�ת���� 16 ���Ʊ�ʾ���ַ�
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
				'E', 'F' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(strs.getBytes());
			byte tmp[] = md.digest(); // MD5 �ļ�������һ�� 128 λ�ĳ�������
			// ���ֽڱ�ʾ���� 16 ���ֽ�
			char str[] = new char[16 * 2]; // ÿ���ֽ��� 16 ���Ʊ�ʾ�Ļ���ʹ�������ַ���
			// ���Ա�ʾ�� 16 ������Ҫ 32
			// ���ַ�
			int k = 0; // ��ʾת������ж�Ӧ���ַ�λ��
			for (int i = 0; i < 16; i++) { // �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ�
				// ת���� 16 �����ַ���ת��
				byte byte0 = tmp[i]; // ȡ�� i ���ֽ�
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // ȡ�ֽ��и� 4 λ������ת��,
				// >>>
				// Ϊ�߼����ƣ�������λһ������
				str[k++] = hexDigits[byte0 & 0xf]; // ȡ�ֽ��е� 4 λ������ת��
			}
			s = new String(str); // ����Ľ��ת��Ϊ�ַ���
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
    
    public void closeInbox() throws Exception{
    	if (inbox != null) {
    		//AIOEMail.emailLog.debug("AIOEMail.closeInbox closeInbox ");
            inbox.close(true);
        }
        if (store != null) {
        	//AIOEMail.emailLog.debug("AIOEMail.closeInbox close store ");
            store.close();
        }    	
    }

    private EMailMessage handleMultipart(Message msg,EMailMessage em,boolean useCharset,String thecharset) throws Exception {
        Object ocon = msg.getContent();
    	if(ocon instanceof Multipart){
	        Multipart mp = (Multipart) ocon;
	        recHandleMultipart(em,mp,0,useCharset,thecharset);
	        
	        //      �������ǶͼƬ��������ת��
	    	for(Object o:em.getContentIdList()){
	    		//    		<__0@Foxmail.net>
	        	//<IMG src="cid:__0@Foxmail.net">   
	    		//<IMG src="cid:12e893b$99812b81c29317$Coremail$dcz000@188.com">
	    		///ReadFile?tempFile=email&fileName=20100918114943%2Fa.jpg    		
	    		String[] strs = (String[])o;
	    		String cid = strs[0];
	        	cid = cid.replaceAll("<|>", "");
	        	cid = "cid:"+cid;
	        	String fn = strs[1];
	        	fn = "/ReadFile?tempFile=email&fileName="+accountId+"/"+GlobalsTool.encode(fn);
	    		em.setContent(em.getContent().replaceFirst(cid, fn));
	    	} 
	        return em;
    	}else if(ocon instanceof BASE64DecoderStream){
    		saveFile(em.getSubject(),(BASE64DecoderStream)ocon,em);
    		return em;
    	}else{
    		AIOEMail.emailLog.error("AIOEMail.handleMultipart content Error Class "+ocon.getClass().getName());
    		return em;
    	}
    }
    
    private void saveFile(String fileName,BASE64DecoderStream fileStream,EMailMessage em){
    	//�ļ���һ�㶼������base64����,�����ǽ���        
        String aPath = BaseEnv.FILESERVERPATH + "/email/" + accountId+"/";        
        File ap = new File(aPath);
        if(!ap.exists()){
            ap.mkdirs();
        }
        //fileNameǰ��ʱ��
        String oldfileName = fileName;
        File file = new File(aPath,fileName);                    
        for(int i=1;file.exists()&& i<1000;i++){
        	//�ļ������ڣ�����
        	if(oldfileName.indexOf(".") > -1){
        		fileName = oldfileName.substring(0,oldfileName.lastIndexOf("."))+"("+i+")"+oldfileName.substring(oldfileName.lastIndexOf("."));
        	}else{
        		fileName = oldfileName+"("+i+")";
        	}
        	file = new File(aPath, fileName); 
        }
        try{
	        InputStream in = fileStream;
	        FileOutputStream writer =
	            new FileOutputStream(file);
	        byte[] content = new byte[255];
	        int read = 0;
	        while ((read = in.read(content)) != -1) {
	            writer.write(content);
	        }
	        writer.close();
	        in.close();
	        em.putFile(fileName,file);
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
    
    private void recHandleMultipart(EMailMessage em,Multipart mp,int recCount,boolean useCharset,String thecharset) throws Exception{
    	
    	if(recCount >300) return; //Ϊ�ݹ����ô�����������ѭ��
    	
        int mpCount = mp.getCount();
        String disposition;
        
        //Miltipart������,���ڳ��˶��part,����������
        for (int m = 0; m < mpCount; m++) {
            BodyPart part = mp.getBodyPart(m);
           //����Content-Type �ж��Ƿ��Ǹ�������ΪǶ�����ݣ�dispositionΪ��
            boolean isAttach = false;
            if(part instanceof MimeBodyPart){
            	MimeBodyPart mbp = (MimeBodyPart) part;
                String[] hs = mbp.getHeader("Content-Type");
                if(hs != null && hs.length >0 && ( hs[0].indexOf("octet-stream")>-1 || hs[0].indexOf("image")>-1) ){
                	isAttach = true;
                }
            }
            disposition = part.getDisposition();
            if (isAttach || ((disposition != null) &&
                ((disposition.equals(Part.ATTACHMENT) ||
                  (disposition.equals(Part.INLINE)&&
                		  part.getFileName() !=null &&
                		  part.getFileName().length() !=0))))) {
                this.saveAttach(part,em,useCharset,thecharset);
            } else  {
                MimeBodyPart mbp = (MimeBodyPart) part;                
                String[] hs = mbp.getHeader("Content-Type");
                String charset="";
                if(useCharset){
                	charset = thecharset;
                }else{
	                if(hs != null && hs.length>0 && hs[0].indexOf("charset")>-1){
	                	Pattern pt = Pattern.compile("charset[\\s]*=[\\s]*[\\\"]?([^\\\" ]*)[\\\"]?", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	                	Matcher mat = pt.matcher(hs[0]);
	                	if(mat.find()){
	                		charset =mat.group(1);                		
	                	}
	                }
                }
                
                
                if (mbp.isMimeType("text/plain")) {
                    if(!em.isIsContentHtml()){
                    	setContent(em,mbp.getInputStream(),charset,false);
                    }
                } else if (mbp.isMimeType("text/html")) {
                	setContent(em,mbp.getInputStream(),charset,true);
                } else if (mbp.isMimeType("multipart/alternative") || mbp.isMimeType("multipart/related")|| mbp.isMimeType("multipart/mixed")) {
                	recHandleMultipart(em,(Multipart) mbp.getContent(),recCount++,useCharset,thecharset);
                }else if (mbp.isMimeType("message/delivery-status")) {
                	// ������Ϣ ����
                }else if (mbp.isMimeType("message/rfc822")) {
                	//������Ϣ ����
                }else {
                    if(!em.isIsContentHtml()){  
                    	setContent(em,mbp.getInputStream(),charset,false);
                    }
                }
            } 
        }
    }

    

    private void saveAttach(BodyPart part,EMailMessage em,boolean useCharset,String charset) throws Exception {
        String temp = part.getFileName();
        if(temp == null) temp = "temp.png";
        //�õ�δ������ĸ�������
        String fileName;
        
          
        try {
        	temp = AIOMimeUtility.decodeText(temp,useCharset,charset); //�����½���
        } catch (Exception e1) {
            AIOEMail.emailLog.error("AIOEMail.handle  decode Subject", e1);
        }
        
        fileName = temp;
        //ȥ���ļ��пո�
        fileName = fileName.replaceAll("[\\s:\\[\\]]*", "");
        
        String aPath = BaseEnv.FILESERVERPATH + "/email/" + accountId+"/";        
        File ap = new File(aPath);
        if(!ap.exists()){
            ap.mkdirs();
        }
        File file = new File(aPath,fileName);  
        
        String[] contentids = part.getHeader("Content-ID");
        
        if(contentids != null && contentids.length >0 ){ 

	        //�ļ���һ�㶼������base64����,�����ǽ���        
	        
	        
	        //fileNameǰ��ʱ��
	        String oldfileName = fileName;
	                          
	        for(int i=1;file.exists()&& i<1000;i++){
	        	//�ļ������ڣ�����
	        	if(oldfileName.indexOf(".") > -1){
	        		fileName = oldfileName.substring(0,oldfileName.lastIndexOf("."))+"("+i+")"+oldfileName.substring(oldfileName.lastIndexOf("."));
	        	}else{
	        		fileName = oldfileName+"("+i+")";
	        	}
	        	file = new File(aPath, fileName); 
	        }
	        
	        InputStream in = part.getInputStream();
	        FileOutputStream writer;
	        try{
		        writer =
		            new FileOutputStream(file);
	        }catch(Exception e){
	        	fileName = System.currentTimeMillis()+""+(fileName.indexOf(".")>0?fileName.substring(fileName.lastIndexOf(".")):"");
	        	file = new File(aPath, fileName); 
	        	writer =
		            new FileOutputStream(file);
	        }
	        byte[] content = new byte[255];
	        int read = 0;
	        while ((read = in.read(content)) != -1) {
	            writer.write(content);
	        }
	        writer.close();
	        in.close();
        }
        
        
        if(contentids != null && contentids.length >0 ){        	
        	em.putFileContentId(contentids[0], fileName);
        }else{
        	em.putFile(fileName,file);
        }
    }

    //base64����
    private String base64Decoder(String s,String encode) throws Exception {
        sun.misc.BASE64Decoder decoder =
            new sun.misc.BASE64Decoder();
        byte[] b = decoder.decodeBuffer(s);

        try {
            return (new String(b, encode));
        } catch (Exception ex) {
            AIOEMail.emailLog.error("AIOEMail.base64Decoder donot know encode "+s,ex);
            return (new String(b));
        }
    }


    private void handle(Message msg, EMailMessage em,boolean useCharset,String thecharset) throws Exception {
        
        if(!useCharset){
        	//String subjd = msg.getSubject();
        	em.setHeadCharset(""); //�ÿ��ַ���
	        //��ȡ�ַ���
	        Enumeration enume = msg.getMatchingHeaders(CHARTSET_HEADER);
	        while (enume.hasMoreElements()) { 
	        	Header header = (Header) enume.nextElement();
	        	Matcher m = encodeStringPattern.matcher(header.getValue());
	            if (m.find()) {
	                  em.setHeadCharset(m.group(1));
	                  break;
	            }
	        }	        
        }else{
        	em.setHeadCharset(thecharset);
        }
        
        String[] subs =msg.getHeader("Subject");
        String subj="";
        if(subs != null && subs.length>0){
        	subj = msg.getHeader("Subject")[0];
        }
               
          
        try {
        	subj = AIOMimeUtility.decodeText(subj,useCharset,thecharset); //�����½���
        } catch (Exception e1) {
            AIOEMail.emailLog.error("AIOEMail.handle  decode Subject", e1);
        }       
        
         
//        String realCode = this.getCode(subj.getBytes(charset));
//        System.out.print(realCode+":"+new String(subj.getBytes("ISO8859-1"),"big5"));
//        if(realCode.length() > 0 && realCode.toLowerCase().indexOf(charset.toLowerCase()) == 0 ){
//        	//������ζ�ţ�ָ���ı��벻�ڼ�����ʵ������
//        	subj = new String(subj.getBytes(charset),realCode.substring(0,realCode.indexOf("|")));
//        }

        em.setSubject(subj);
        String[] froms = msg.getHeader("From");
        if(froms != null && froms.length> 0){
	        String[] add = parseMailAddress(froms[0],em,useCharset,thecharset);
	        EMailMessage.Recipienter rec =  em.newRecipienter(
	        		EMailMessage.TO, add[1], add[0]);
	        em.setSender(rec);
        }
        em.setMailSize(msg.getSize());
        
        //em.setMessageId(((MimeMessage)msg).getMessageID());
        
        String[] ads = msg.getHeader("To");        
    	for (int i = 0; ads != null && i < ads.length; i++) {
    		if(ads[i]!= null && ads[i].length() >0){
    			String [] strb = ads[i].split("\n|,|;");
    	    	for(String str:strb){    	    		
    	    		if(str==null || str.trim().length() ==0){
    	    			continue;
    	    		}
    	    		str = str.trim();
			        String[] add = parseMailAddress(str,em,useCharset,thecharset);
			        if(isMail(add[1]))
			        	em.addRecipient(EMailMessage.TO,add[1], add[0]);
    	    	}
    		}
    	}
    	ads = msg.getHeader("Cc");        
    	for (int i = 0; ads != null && i < ads.length; i++) {
    		if(ads[i]!= null && ads[i].length() >0){
    			String [] strb = ads[i].split("\n|,|;");
    	    	for(String str:strb){    	    		
    	    		if(str==null || str.trim().length() ==0){
    	    			continue;
    	    		}
    	    		str = str.trim();
			        String[] add = parseMailAddress(str,em,useCharset,thecharset);
			        if(isMail(add[1]))
			        	em.addRecipient(EMailMessage.CC,add[1], add[0]);
    	    	}
    		}
    	}
    	ads = msg.getHeader("Bcc");        
    	for (int i = 0; ads != null && i < ads.length; i++) {
    		if(ads[i]!= null && ads[i].length() >0){
    			String [] strb = ads[i].split("\n|,|;");
    	    	for(String str:strb){    	    		
    	    		if(str==null || str.trim().length() ==0){
    	    			continue;
    	    		}
    	    		str = str.trim();
			        String[] add = parseMailAddress(str,em,useCharset,thecharset);
			        if(isMail(add[1]))
			        	em.addRecipient(EMailMessage.BCC,add[1], add[0]);
    	    	}
    		}
    	}
    	ads = msg.getHeader("Disposition-Notification-To");       
    	for (int i = 0; ads != null && i < ads.length; i++) {
    		if(ads[i]!= null && ads[i].length() >0){
    			if(ads[i].indexOf("@") > 0){ //ֻҪ������ִ��Ϣ�ͱ�ǣ�������Ծ����ִ��˭����Ϊһ�㶼ֱ�ӻظ������ˣ�����֮��Ļ�ִ�����
    				em.setBegReplay(true);
    			}    			
    		}
    	}
      em.setDate(msg.getSentDate());        

    }
    
	/*�ж��Ƿ����ʼ�*/
	public static boolean isMail(String str){	
		if(str==null || str.length() == 0) return true;
		Pattern p=Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$"); 
		Matcher m=p.matcher(str); 
		return m.find();
	}
    
    private String[] parseMailAddress(String str,EMailMessage em,boolean useCharset,String thecharset){
    	//������ʽ��[0]	""slsgcewf" <dominikkpaw@yahoo.cn>"	
    	//info@hatour.com
    	//<info@mykeji.com>
    	//?gb2312?B?w+fqv73gIA==?= <tsp@ggud.com>
    	//MAILER-DAEMON@mail.mail181.cn4e.com (=?utf-8?B?57O757uf566h55CG5ZGY?=)

    	String name = "";
    	String email = "";
    	try{
    		//�������<
    		if(str.indexOf("<")>-1&&str.indexOf(">")>-1){
    			email = str.substring(str.indexOf("<")+1,str.indexOf(">"));
    		}else if(str.indexOf("(")>-1&&str.indexOf(")")>-1){
    			email = str.substring(0,str.lastIndexOf(" "));
    		}else if(str.indexOf(" ")>-1){
    			email = str.substring(str.lastIndexOf(" ")+1);
    		}else{
    			email = str;
    		}
    		if(str.indexOf("(")>-1&&str.indexOf(")")>-1){
    			name = str.substring(str.indexOf("(")+1,str.indexOf(")"));
    		}else if(str.indexOf(" ")>-1){
    			name = str.substring(0,str.indexOf(" "));
    		}else if(str.indexOf("<")>-1){
    			name = str.substring(0,str.indexOf("<"));
    		}else{    			
        		name = "";
    		}
    		if(name.startsWith("\"")){
    			name = name.replace('"', ' ').trim();
    		}
    		   
     
	        try {
	        	name = AIOMimeUtility.decodeText(name,useCharset,thecharset); //�����½���
	        } catch (Exception e1) {
	            AIOEMail.emailLog.error("AIOEMail.handle  decode name ", e1);
	        }       
	        
   		    	
	    	if(name==null ||name.equals("")){
	    		name = email; 
	    	}
    	}catch(Exception ex){
    		AIOEMail.emailLog.error("AIOEMail.parseMailAddress Error str="+str,ex);    		
    	}
    	return new String[]{name,email};
    }

    private EMailMessage handleText(Message msg,EMailMessage em,boolean useCharset,String thecharset) throws Exception {
        
        em.setIsContentHtml(msg.isMimeType("text/html"));
        
        String[] hs = msg.getHeader("Content-Type");
        
        String charset=""; 
        if(useCharset){
        	charset = thecharset;
        }else{
	        if(hs != null && hs.length>0 && hs[0].indexOf("charset")>-1){
	           	Pattern pt = Pattern.compile("charset[\\s]*=[\\s]*[\\\"]?([^\\\" ]*)[\\\"]?", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	        	Matcher mat = pt.matcher(hs[0]);
	        	if(mat.find()){
	        		charset =mat.group(1);        		
	        	}
	        }       
        }
        if(msg.isMimeType("text/calendar")){
        	//�����ʼ�
            CalendarBuilder builder = new CalendarBuilder(); 
            net.fortuna.ical4j.model.Calendar cal = builder.build((InputStream)msg.getContent()); 
            net.fortuna.ical4j.model.Component comp = cal.getComponent("VEVENT");
            String description = comp.getProperty("DESCRIPTION").getValue();
            String location = comp.getProperty("LOCATION").getValue();
            String dtstart = BaseDateFormat.format(((net.fortuna.ical4j.model.property.DateProperty)comp.getProperty("DTSTART")).getDate(),BaseDateFormat.yyyyMMddHHmmss);
            String dtend = BaseDateFormat.format(((net.fortuna.ical4j.model.property.DateProperty)comp.getProperty("DTEND")).getDate(),BaseDateFormat.yyyyMMddHHmmss);
            String str = "Meeting\r\n"+"Location:"+location+"\r\n"+"Time:"+dtstart+"-"+dtend+"\r\n\r\n"+description;
            str = str.replaceAll("\\n", "<br>");
            em.setContent(str);
            
            return em;

        }        
        setContent(em,msg.getInputStream(),charset,em.isIsContentHtml());        
        return em;
    }
    
    private void setContent(EMailMessage em,InputStream is,String charset,boolean isHTML) throws Exception{
    	//���em�����ʼ����ݣ����ٴ������ⱻһЩ�����淶���ʼ������ݳ������Ϊһ���ʼ����ݶ����ڵ�һ��͸���
    	if(em.getContent() != null && em.getContent().length() > 0 && em.isIsContentHtml()){
    		return ;
    	}
    	if(charset == null || charset.length() == 0){
    		charset = em.getHeadCharset();
    	}
    	
        if(charset.toLowerCase().equals("gb2312")){
        	charset ="gbk";
	    }
    	
    	if(charset != null && charset.toLowerCase().indexOf("utf-7")>-1){
    		ByteArrayOutputStream out = new ByteArrayOutputStream();
    		int c;
    		while ((c = is.read()) != -1) {
    			out.write(c);
    		}
    		byte[] bytes = out.toByteArray();

    		try {
	    		ByteToCharUTF7 btc = new ByteToCharUTF7();
	    		char[] chars = new char[bytes.length / 2 + 1];
	    		btc.convert(bytes, 0, bytes.length, chars, 0, chars.length);
	    		em.setContent(isHTML,new String(chars));
    		} catch (Exception e) {
    			em.setContent(isHTML,new String(bytes, "ISO8859-1"));
    		}
    		return;
    	}
    	try{
    		byte[] b = new byte[1024];
    		byte[] bs = new byte[0];
    		int count=0;
    		while((count=is.read(b)) > -1){
    			byte[] temp = bs;
    			bs = new byte[temp.length + count];
    			System.arraycopy(temp, 0, bs, 0, temp.length);
    			System.arraycopy(b,0, bs, temp.length, count);
    		}
    		String bsstr;
    		if(charset != null && charset.length() >0){
    			try{
    				bsstr = new String(bs,charset);
    			}catch(Exception e1){
    				bsstr = new String(bs);
    			}
    		}else{
    			bsstr = new String(bs);
    		}
    		if(bsstr != null && bsstr.length() > 0){    			
    			em.setContent(isHTML,bsstr.trim());
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        
        if(!em.isIsContentHtml()){
        	//��html,ת���ո�ͻس�
        	em.setContent(GlobalsTool.encodeHTML( em.getContent()));
        }
        
    }

    public static boolean isEn(byte b){
    	if((b) > 0){
    		return true;
    	}
    	return false;
    }
    public static boolean isGB2312(byte[] bs)

    {
    	int pos = 0;
    	while(pos <bs.length -1){
    		if(bs[pos] > 0){
    			pos++;
    		}else if(pos <bs.length-1){    			
    			int i1=0xff & bs[pos];
    			int i2=0xff & bs[pos+1];
    			if(i1 >=176 && i1<=247 && i2 >=160 && i2<=254){
    				return true;
    			}
    			pos +=2;
    		}else{
    			//λ������
    			return false;
    		}
    	}        
    	return false;

    }
    public static boolean isGBK(byte[] bs)

    {
    	int pos = 0;
    	while(pos <bs.length -1){
    		if(bs[pos] > 0){
    			pos++;
    		}else if(pos <bs.length-1){    			
    			int i1=0xff & bs[pos];
    			int i2=0xff & bs[pos+1];
    			if(i1 >=129 && i1<=254 && i2 >=64 && i2<=254){
    				return true;
    			}
    			pos +=2;
    		}else{
    			//λ������
    			return false;
    		}
    	}        
    	return false;
    }
    
    public static boolean isBig5(byte[] bs)

    {
    	int pos = 0;
    	while(pos <bs.length -1){
    		if(bs[pos] > 0){
    			pos++;
    		}else if(pos <bs.length-1){    			
    			int i1=0xff & bs[pos];
    			int i2=0xff & bs[pos+1];
    			if(i1 >=129 && i1<=254 && ((i2 >=64 && i2<=126) ||(i2 >=161 && i2<=254))){
    				return true;
    			}
    			pos +=2;
    		}else{
    			//λ������
    			return false;
    		}
    	}        
    	return false;
    }
    
    /**
     * �����ַ����ı���
     * ���ȼ�飬�ǲ���big5����big5��gbk���ص�,gb2312Ҳ��gbk���ص�
     * @param bs
     * @return
     */
    public static String getCode(byte[] bs){
    	boolean isgb2312 = true;
		boolean isbig5 = true;
		boolean isgbk = true;
    	int pos = 0;
    	while(pos <bs.length -1){
    		if(bs[pos] > 0){
    			pos++;
    		}else if(pos <bs.length-1){    			
    			int i1=0xff & bs[pos];
    			int i2=0xff & bs[pos+1];
    			
    			if(!(i1 >=176 && i1<=247 && i2 >=160 && i2<=254)) isgb2312 = false;
    			if(!(i1 >=129 && i1<=254 && ((i2 >=64 && i2<=126) ||(i2 >=161 && i2<=254))))  isbig5 = false;
    			if(!(i1 >=129 && i1<=254 && i2 >=64 && i2<=254))  isgbk = false;
    			//�����2������Ϊtrue,�����������һ���ַ�.
    			pos +=2;
    		}else{
    			//λ������
    			return "";
    		}
    	}     
    	
    	String ret = "";
    	if(isbig5){
    		ret+= "BIG5|";
		}
		if(isgb2312){
			ret+=  "GB2312|";
		}
		if(isgbk){
			ret+=  "GBK|";
		}
    	return ret;
    }




    public static void main(String args[]) {
    	//System.out.println( AIOEMail.md5("������"));
    	
        try{
            AIOEMail sm = new AIOEMail();

            //ָ��Ҫʹ�õ��ʼ�������
            sm.setAccount("1317183824@qq.com", "pop.qq.com", "1317183824", "koron88",true,  "smtp.qq.com", 
            		"1317183824", "koron88", 110, 25, "ss", "ss","", false, false,false);
            //ָ���ʺź�����
            
            //String s = "��";
            String s = "���w�]���yԇ";
            byte[] bb= s.getBytes();
            byte[] b2= s.getBytes("GB2312");
            byte[] b3= s.getBytes("BIG5");
            byte[] b4 = new String(b3,"BIG5").getBytes("GB2312");
            
            System.out.println("--------"+getCode(b3));
            
            sm.send("1317183824@qq.com", "��", "true", "smtp.qq.com", "25", "1317183824", "koron88", "false", "ksalon@163.com", "����", "�ǵĿ���");
            
            //sm.connect();
            //sm.receiveNextHeader();
//            sm.receiveBody();
                          

//            StringBuffer theMessage = new StringBuffer();
//            theMessage.append(
//                    "<h2><font color=red>�⵹--------------ù����</font></h2>");
//            theMessage.append("<hr>");
//            theMessage.append("<i>����ʧ��������</i>");
//
//            EMailMessage smes = new EMailMessage();
//            smes.addRecipient(EMailMessage.TO, "zhouxinyu@createam.com.cn");
//            smes.addRecipient(EMailMessage.TO, "alpha@koronsoft.com");
//            //smes.addRecipient(EMailMessage.TO,"fhq_verace@126.com");
//            smes.addRecipient(EMailMessage.CC, "seward@163.com");
//            smes.setSubject("���ڵĲ����ʼ�");
//            smes.setContent(false, theMessage.toString());
//            smes.putFile("�ļ�����", new File("e:/a.jpg"));
//            smes.putFile("���鸽��", "���鸽��".getBytes());
//            smes.putFile("������", new java.io.ByteArrayInputStream("������".getBytes()));
//            smes.putFile("�ı�����", "�ı�����");
//
//            sm.send(smes);

       

//        for (EMailMessage em:list) {
//            System.out.println("---------------------------------");
//            System.out.println("���⣺"+em.getSubject());
//            System.out.println("�����ˣ�"+em.getSender());
//            System.out.println("���ڣ�"+em.getDate());
//            System.out.println("HTML��"+em.isIsContentHtml());
//            for (Object o : em.getRecipientList()) {
//                EMailMessage.Recipienter r = (EMailMessage.Recipienter)o;
//                System.out.println(""+(r.type == EMailMessage.TO?"������":"������")+r.emailAddress);
//            }
//            System.out.println("���⣺\n"+em.getContent());
//            System.out.println("������\n");
//            for (Object o : em.getFiles().keySet()) {
//                String r = (String)o;
//                System.out.println(""+r);
//            }
//
//        }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPop3Host() {
        return pop3Host;
    }

    public String getPop3Password() {
        return pop3Password;
    }

    public int getPop3Port() {
        return pop3Port;
    }

    public String getPop3User() {
        return pop3User;
    }

    public boolean isSmtpAuth() {
        return smtpAuth;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public String getSmtpUser() {
        return smtpUser;
    }

    public void setSmtpUser(String smtpUser) {
        this.smtpUser = smtpUser;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public void setSmtpAuth(boolean smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public void setPop3User(String pop3User) {
        this.pop3User = pop3User;
    }

    public void setPop3Port(int pop3Port) {
        this.pop3Port = pop3Port;
    }

    public void setPop3Password(String pop3Password) {
        this.pop3Password = pop3Password;
    }

    public void setPop3Host(String pop3Host) {
        this.pop3Host = pop3Host;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


}
