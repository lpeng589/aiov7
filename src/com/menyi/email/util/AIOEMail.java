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
    public String accountId = ""; //邮件的帐户代号
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
    public Message curMsg; //当前的消息对象，用于分步接收
    public EMailMessage curem; //当前的消息对象，用于分步接收
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
     * 发送邮件，供一般接口调用，如在define中调用
     * @param emailAddress 发送方邮件地址
     * @param displayName  发送方显示名字
     * @param smtpAuth     true:发送服务器需要授权，false：不是授权
     * @param smtpHost     邮件发送服务器地址
     * @param smtpPort     邮件发送服务器端口
     * @param smtpUser     邮件发送帐号
     * @param smtpPassword 邮件发送密码
     * @param smtpssl      邮件发送服务器是否启用ssl加密 true:是，false：否
     * @param toEmail      邮件发送到地址，多个地址以";"号分隔
     * @param subject      邮件主题
     * @param content      邮件内容
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
	        //指定SMTP服务器
	        props.put("mail.smtp.auth", smtpAuth.equals("true") ? "true" : "false");
	
	        props.put("mail.stmp.timeout", 120000); //两分钟超时
	
	        if (Integer.parseInt(smtpPort) != 25) {
	            props.put("mail.stmp.port", smtpPort);
	        }
	        //指定是否需要SMTP验证

            Session mailSession = Session.getInstance(props);
            //是否在控制台显示debug信息
            mailSession.setDebug(false);
            Message message = new MimeMessage(mailSession);
            message.setSentDate(new Date());
            //发件人
            message.setFrom(new InternetAddress(emailAddress,MimeUtility.encodeText( displayName,"utf-8","B")));
            //收件人
            if(toEmail ==null || toEmail.length() ==0){
            	rs.retCode= ErrorCanst.DEFAULT_FAILURE;
            	rs.retVal = "件件人为空";
            	return rs;
            }
            String[] toEmails = toEmail.split(";");
            for (String o : toEmails) {
                message.addRecipient(Message.RecipientType.TO,
                                         new InternetAddress(o,MimeUtility.encodeText( o,"utf-8","B")));
            }
            message.setSubject(MimeUtility.encodeText( subject,"utf-8","B"));//设置主题
            Multipart container = new MimeMultipart(); //主容器
            MimeBodyPart textBodyPart = new MimeBodyPart();
            
            AIOEMail.emailLog.info("==================System encoding ====="+System.getProperty("file.encoding")); 
            
            //设置正文
            textBodyPart.setContent(new String( content.getBytes(),"utf-8"), "text/html;charset=utf-8");
            container.addBodyPart(textBodyPart); //将正文放入容器中

            //处理嵌入式图片，查找内容的为ReadFile的图片，找到后做为 符件上传
        	//邮件转发中的图片
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
            		//要发送的附件
                    fileBodyPart.setDataHandler(new DataHandler(ds));
                    sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();                    
                    //fileBodyPart.setFileName("=?GBK?B?" + enc.encode((f.getName()).getBytes()) + "?=");
                    fileBodyPart.setHeader("Content-ID", "<__" +i+"@koronsoft.com>");
                    container.addBodyPart(fileBodyPart); 
                    content = content.substring(0,content.indexOf(readFile))+"cid:__" +i+"@koronsoft.com"+content.substring(content.indexOf(readFile)+readFile.length());
        		}
                i++; 
        	}
        	//编辑器直接上传的图片
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
        		//要发送的附件
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

            //邮件内容
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
            rs.setRetVal("发送邮件失败"+e);
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
        //指定SMTP服务器
        props.put("mail.smtp.auth", smtpAuth ? "true" : "false");

        props.put("mail.stmp.timeout", 120000); //两分钟超时

        if (smtpPort != 25) {
            props.put("mail.stmp.port", smtpPort);
        }
        //指定是否需要SMTP验证
        try {
            Session mailSession = Session.getInstance(props);
            //是否在控制台显示debug信息
            mailSession.setDebug(false);
            Message message = new MimeMessage(mailSession);
            message.setSentDate(new Date());
            if(sendMessage.isBegReplay()){
            	message.setHeader("Disposition-Notification-To",emailAddress);
            }
            //发件人
            message.setFrom(new InternetAddress(emailAddress,MimeUtility.encodeText( displayName,"utf-8","B")));
            //收件人
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
            message.setSubject(MimeUtility.encodeText( sendMessage.getSubject(),"utf-8","B"));//设置主题
            Multipart container = new MimeMultipart(); //主容器
            MimeBodyPart textBodyPart = new MimeBodyPart();
            
            AIOEMail.emailLog.info("==================System encoding ====="+System.getProperty("file.encoding")); 
            
            //设置正文
            if (sendMessage.isIsContentHtml()) {
                textBodyPart.setContent(new String( sendMessage.getContent().getBytes(),"utf-8"), "text/html;charset=utf-8");
            } else {
                textBodyPart.setText(new String( sendMessage.getContent().getBytes(),"utf-8"));
            }
            container.addBodyPart(textBodyPart); //将正文放入容器中
            
            

            //增加附件
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

                //要发送的附件
                fileBodyPart.setDataHandler(new DataHandler(ds));
                sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                fileBodyPart.setFileName(MimeUtility.encodeText( (String) entry.getKey(),"utf-8","B"));

                container.addBodyPart(fileBodyPart);
            }
            //          处理嵌入式图片，查找内容的为ReadFile的图片，找到后做为 符件上传
            if (sendMessage.isIsContentHtml()) {
            	String content = sendMessage.getContent();
            	//邮件转发中的图片
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
	            		//要发送的附件
	                    fileBodyPart.setDataHandler(new DataHandler(ds));
	                    sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();                    
	                    //fileBodyPart.setFileName("=?GBK?B?" + enc.encode((f.getName()).getBytes()) + "?=");
	                    fileBodyPart.setHeader("Content-ID", "<__" +i+"@koronsoft.com>");
	                    container.addBodyPart(fileBodyPart); 
	                    content = content.substring(0,content.indexOf(readFile))+"cid:__" +i+"@koronsoft.com"+content.substring(content.indexOf(readFile)+readFile.length());
            		}
                    i++; 
            	}
            	//编辑器直接上传的图片
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
            		//要发送的附件
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

            //邮件内容
            message.saveChanges();

            Transport transport = mailSession.getTransport("smtp");
            transport.connect(smtpHost, smtpUser, smtpPassword);
            message.setHeader("X-Mailer", "EXC Mail Sender 1.0");
            // message.setHeader("To", "\"ksalon\" <ksalon@163.com>, \"info\" <info@koronsoft.com>");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            
            if(userId!=null && !"".equals(userId)){
            	//外部邮件(修改客户操作1.更新最后联系时间，2.插入客户日志)
            	new EMailMgt().emailRelateClient(sendMessage.getRecipientList(),userId);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            AIOEMail.emailLog.error("AIOEMail Send Error", e);
            throw e;
        }

    }
    
    /*
     * 读取eml格式文件
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
     * 读取eml格式文件体
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
     * 使用指定的语言来解码文件
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
    	
    	if(recCount >300) return null; //为递归设置次数，避免死循环
    	
        int mpCount = mp.getCount();
        String disposition;
        
        //Miltipart的数量,用于除了多个part,比如多个附件
        for (int m = 0; m < mpCount; m++) {
            BodyPart part = mp.getBodyPart(m);
           //根据Content-Type 判断是否是附件，因为嵌入内容，disposition为空
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
        //得到未经处理的附件名字
        String fileName;        
          
        try {
        	temp = AIOMimeUtility.decodeText(temp,true,charset); //再重新解码
        } catch (Exception e1) {
            AIOEMail.emailLog.error("AIOEMail.handle  decode Subject", e1);
        }
        
        fileName = temp;
        //去掉文件中空格
        fileName = fileName.replaceAll("[\\s:\\[\\]]*", "");
        
        if(!fileName.equals(thefileName)){
        	return null;
        }      
        
        //fileName前加时间                
        return part.getInputStream();
    }
    
    
    public void saveFile(EMailMessage sendMessage,OutputStream os) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        //指定SMTP服务器
        props.put("mail.smtp.auth", smtpAuth ? "true" : "false");

        props.put("mail.stmp.timeout", 120000); //两分钟超时

        if (smtpPort != 25) {
            props.put("mail.stmp.port", smtpPort);
        }
        //指定是否需要SMTP验证
        try {
            Session mailSession = Session.getInstance(props);
            //是否在控制台显示debug信息
            mailSession.setDebug(false);
            Message message = new MimeMessage(mailSession);
            message.setSentDate(sendMessage.getDate());

            //发件人
            message.setFrom(new InternetAddress(sendMessage.getSender().emailAddress,MimeUtility.encodeText( sendMessage.getSender().display,"utf-8","B")));
            //收件人
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
            message.setSubject(MimeUtility.encodeText( sendMessage.getSubject(),"utf-8","B"));//设置主题
            Multipart container = new MimeMultipart(); //主容器
            MimeBodyPart textBodyPart = new MimeBodyPart();
            
            AIOEMail.emailLog.info("==================System encoding ====="+System.getProperty("file.encoding")); 
            
            //设置正文
            if (sendMessage.isIsContentHtml()) {
                textBodyPart.setContent(new String( sendMessage.getContent().getBytes(),"utf-8"), "text/html;charset=utf-8");
            } else {
                textBodyPart.setText(new String( sendMessage.getContent().getBytes(),"utf-8"));
            }
            container.addBodyPart(textBodyPart); //将正文放入容器中
            
            

            //增加附件
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

                //要发送的附件
                fileBodyPart.setDataHandler(new DataHandler(ds));
                sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                fileBodyPart.setFileName(MimeUtility.encodeText( (String) entry.getKey(),"utf-8","B"));

                container.addBodyPart(fileBodyPart);
            }
            //          处理嵌入式图片，查找内容的为ReadFile的图片，找到后做为 符件上传
            if (sendMessage.isIsContentHtml()) {
            	String content = sendMessage.getContent();
            	//邮件转发中的图片
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
            		//要发送的附件
                    fileBodyPart.setDataHandler(new DataHandler(ds));
                    sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();                    
                    //fileBodyPart.setFileName("=?GBK?B?" + enc.encode((f.getName()).getBytes()) + "?=");
                    fileBodyPart.setHeader("Content-ID", "<__" +i+"@koronsoft.com>");
                    container.addBodyPart(fileBodyPart); 
                    content = content.substring(0,content.indexOf(readFile))+"cid:__" +i+"@koronsoft.com"+content.substring(content.indexOf(readFile)+readFile.length

());

                    i++; 
            	}
            	//编辑器直接上传的图片
            	rfp = Pattern.compile("src=\\\"(/upload/[^\\s\\\"]*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            	mat= rfp.matcher(content);
            	while(mat.find()){ 
            		String readFile = mat.group(1);
            		MimeBodyPart fileBodyPart = new MimeBodyPart();
            		File f = new File(BaseEnv.systemRealPath+readFile);
            		DataSource ds = new FileDataSource(f);
            		//要发送的附件
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

            //邮件内容
            message.saveChanges();

            message.writeTo(os);
        } catch (Exception e) {
            e.printStackTrace();
            AIOEMail.emailLog.error("AIOEMail Send Error", e);
            throw e;
        }
    }
    
  
    //连接远程服务器，并返回邮件个数
    public String[] connect() throws Exception{
    	msgMap.clear();
    	newMsgList.clear();
        try {
            ArrayList list = new ArrayList();
	        openPop3(true);
	        
	        int count = inbox.getMessageCount();
            Message[] msgs = inbox.getMessages();
            FetchProfile profile = new FetchProfile();
            //profile.add(FetchProfile.Item.ENVELOPE);  //这步将会取信息头，影响速度，所以不取
            profile.add(UIDFolder.FetchProfileItem.UID); 
            inbox.fetch(msgs, profile);
            
            for(Message msg:msgs){
            	String uid = inbox.getUID(msg);
            	if(msgMap.get(uid) ==null){
	            	msgMap.put(uid, msg);
	            	newMsgList.add(msg);
            	}
            }            
            System.out.println("-----------------邮件总数:"+msgs.length+"-------");
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
			AIOEMail.emailLog.error("AIOMail openPop3() 连接邮件服务器错误。。。",e) ;
			if(isConnect){
				//如果第一次连接时报错
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
					//邮箱中存储的邮件太多，每个都超过1万5千多封邮件，邮件数太多，接收下来的邮件头要在数据库中处理的时间超过10秒多钟，而阿里巴巴邮箱对邮件的连接超时时间
					//  又太短,导致邮箱超时中断连接,这里给一次超过重连的机会。
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
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
				'E', 'F' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(strs.getBytes());
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32
			// 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>>
				// 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串
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
	        
	        //      如果有内嵌图片，这里做转换
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
    	//文件名一般都经过了base64编码,下面是解码        
        String aPath = BaseEnv.FILESERVERPATH + "/email/" + accountId+"/";        
        File ap = new File(aPath);
        if(!ap.exists()){
            ap.mkdirs();
        }
        //fileName前加时间
        String oldfileName = fileName;
        File file = new File(aPath,fileName);                    
        for(int i=1;file.exists()&& i<1000;i++){
        	//文件名存在，改名
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
    	
    	if(recCount >300) return; //为递归设置次数，避免死循环
    	
        int mpCount = mp.getCount();
        String disposition;
        
        //Miltipart的数量,用于除了多个part,比如多个附件
        for (int m = 0; m < mpCount; m++) {
            BodyPart part = mp.getBodyPart(m);
           //根据Content-Type 判断是否是附件，因为嵌入内容，disposition为空
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
                	// 回退消息 忽略
                }else if (mbp.isMimeType("message/rfc822")) {
                	//回退消息 忽略
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
        //得到未经处理的附件名字
        String fileName;
        
          
        try {
        	temp = AIOMimeUtility.decodeText(temp,useCharset,charset); //再重新解码
        } catch (Exception e1) {
            AIOEMail.emailLog.error("AIOEMail.handle  decode Subject", e1);
        }
        
        fileName = temp;
        //去掉文件中空格
        fileName = fileName.replaceAll("[\\s:\\[\\]]*", "");
        
        String aPath = BaseEnv.FILESERVERPATH + "/email/" + accountId+"/";        
        File ap = new File(aPath);
        if(!ap.exists()){
            ap.mkdirs();
        }
        File file = new File(aPath,fileName);  
        
        String[] contentids = part.getHeader("Content-ID");
        
        if(contentids != null && contentids.length >0 ){ 

	        //文件名一般都经过了base64编码,下面是解码        
	        
	        
	        //fileName前加时间
	        String oldfileName = fileName;
	                          
	        for(int i=1;file.exists()&& i<1000;i++){
	        	//文件名存在，改名
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

    //base64解码
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
        	em.setHeadCharset(""); //置空字符集
	        //获取字符集
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
        	subj = AIOMimeUtility.decodeText(subj,useCharset,thecharset); //再重新解码
        } catch (Exception e1) {
            AIOEMail.emailLog.error("AIOEMail.handle  decode Subject", e1);
        }       
        
         
//        String realCode = this.getCode(subj.getBytes(charset));
//        System.out.print(realCode+":"+new String(subj.getBytes("ISO8859-1"),"big5"));
//        if(realCode.length() > 0 && realCode.toLowerCase().indexOf(charset.toLowerCase()) == 0 ){
//        	//这里意味着，指定的编码不在检测的真实编码中
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
    			if(ads[i].indexOf("@") > 0){ //只要包含回执信息就标记，这里忽略具体回执给谁，因为一般都直接回给发件人，除此之外的回执不理会
    				em.setBegReplay(true);
    			}    			
    		}
    	}
      em.setDate(msg.getSentDate());        

    }
    
	/*判断是否是邮件*/
	public static boolean isMail(String str){	
		if(str==null || str.length() == 0) return true;
		Pattern p=Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$"); 
		Matcher m=p.matcher(str); 
		return m.find();
	}
    
    private String[] parseMailAddress(String str,EMailMessage em,boolean useCharset,String thecharset){
    	//分析格式如[0]	""slsgcewf" <dominikkpaw@yahoo.cn>"	
    	//info@hatour.com
    	//<info@mykeji.com>
    	//?gb2312?B?w+fqv73gIA==?= <tsp@ggud.com>
    	//MAILER-DAEMON@mail.mail181.cn4e.com (=?utf-8?B?57O757uf566h55CG5ZGY?=)

    	String name = "";
    	String email = "";
    	try{
    		//如果存在<
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
	        	name = AIOMimeUtility.decodeText(name,useCharset,thecharset); //再重新解码
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
        	//会议邮件
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
    	//如果em已有邮件内容，则不再处理，避免被一些不符规范的邮件把内容冲掉，因为一般邮件内容都是在第一层就给出
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
        	//非html,转化空格和回车
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
    			//位数不够
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
    			//位数不够
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
    			//位数不够
    			return false;
    		}
    	}        
    	return false;
    }
    
    /**
     * 测试字符串的编码
     * 优先检查，是不是big5，因big5和gbk有重叠,gb2312也和gbk有重叠
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
    			//如果有2个以上为true,则继续测试下一个字符.
    			pos +=2;
    		}else{
    			//位数不够
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
    	//System.out.println( AIOEMail.md5("周新宇"));
    	
        try{
            AIOEMail sm = new AIOEMail();

            //指定要使用的邮件服务器
            sm.setAccount("1317183824@qq.com", "pop.qq.com", "1317183824", "koron88",true,  "smtp.qq.com", 
            		"1317183824", "koron88", 110, 25, "ss", "ss","", false, false,false);
            //指定帐号和密码
            
            //String s = "中";
            String s = "繁體郵件測試";
            byte[] bb= s.getBytes();
            byte[] b2= s.getBytes("GB2312");
            byte[] b3= s.getBytes("BIG5");
            byte[] b4 = new String(b3,"BIG5").getBytes("GB2312");
            
            System.out.println("--------"+getCode(b3));
            
            sm.send("1317183824@qq.com", "周", "true", "smtp.qq.com", "25", "1317183824", "koron88", "false", "ksalon@163.com", "开会", "记的开会");
            
            //sm.connect();
            //sm.receiveNextHeader();
//            sm.receiveBody();
                          

//            StringBuffer theMessage = new StringBuffer();
//            theMessage.append(
//                    "<h2><font color=red>这倒--------------霉孩子</font></h2>");
//            theMessage.append("<hr>");
//            theMessage.append("<i>年年失望年年望</i>");
//
//            EMailMessage smes = new EMailMessage();
//            smes.addRecipient(EMailMessage.TO, "zhouxinyu@createam.com.cn");
//            smes.addRecipient(EMailMessage.TO, "alpha@koronsoft.com");
//            //smes.addRecipient(EMailMessage.TO,"fhq_verace@126.com");
//            smes.addRecipient(EMailMessage.CC, "seward@163.com");
//            smes.setSubject("正宗的测试邮件");
//            smes.setContent(false, theMessage.toString());
//            smes.putFile("文件附件", new File("e:/a.jpg"));
//            smes.putFile("数组附件", "数组附件".getBytes());
//            smes.putFile("流附件", new java.io.ByteArrayInputStream("流附件".getBytes()));
//            smes.putFile("文本附件", "文本附件");
//
//            sm.send(smes);

       

//        for (EMailMessage em:list) {
//            System.out.println("---------------------------------");
//            System.out.println("主题："+em.getSubject());
//            System.out.println("发送人："+em.getSender());
//            System.out.println("日期："+em.getDate());
//            System.out.println("HTML："+em.isIsContentHtml());
//            for (Object o : em.getRecipientList()) {
//                EMailMessage.Recipienter r = (EMailMessage.Recipienter)o;
//                System.out.println(""+(r.type == EMailMessage.TO?"接收者":"抄送者")+r.emailAddress);
//            }
//            System.out.println("主题：\n"+em.getContent());
//            System.out.println("附件：\n");
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
