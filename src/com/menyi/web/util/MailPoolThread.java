package com.menyi.web.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import com.dbfactory.Result;
import com.koron.oa.bean.MailinfoSettingBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.bol88.AIOBOL88Bean;
import com.menyi.aio.web.bol88.AIOBOL88Mgt;
import com.menyi.aio.web.bol88.CRMMailObj;
import com.menyi.email.EMailMgt;
import com.menyi.email.util.AIOEMail;
import com.menyi.email.util.EMailMessage;

public class MailPoolThread implements Runnable{
	
	public static String INFO="";
	
	public int totalCount = 0;
	
	private Object totalLock = new Object();
	
	private String mailaccount; 
	private String title;
	private String content;
	private String attach;
	private String curKeywordId; 
	private int curKwMailNum;
	private int leaveDate=6;
	private int startTime=0;
	private int endTime=5;
	private String ccMail;	
	private String sCompanyId;
	private String startSendDate;
	private String endSendDate;
	private int threadNum; 
	private String username = "";
	private String JSESSION  = ""; 
	
	private boolean go= true;
	private String delDate ; //最后执行删除动作的日期
	private String ccDate ; //最后执行抄送动作的日期
	
	private String from;
	
	private ArrayList threadList = new ArrayList();
	
	
	public boolean init(String mailaccount,String title,String content,String attach,String ccMail,
			String curKeywordId,int curKwMailNum,int leaveDate,int startTime,int endTime,
			String startSendDate,String endSendDate,int threadNum,String sCompanyId,String from){
		this.mailaccount = mailaccount;
		this.title = title;
		this.content = content;
		this.attach = attach;
		this.curKeywordId = curKeywordId;
		this.curKwMailNum = curKwMailNum;
		this.leaveDate = leaveDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.ccMail = ccMail;
		this.sCompanyId = sCompanyId;
		this.startSendDate = startSendDate;
		this.endSendDate = endSendDate;
		this.threadNum = threadNum;		
		this.from = from ;
		
		go = true;
		if(threadNum == -1){
			Thread th = new Thread(this);
			th.setName("MailPoolThread_0");			
			th.start();
			threadList.add(th);
		}else{
			for(int i = 0;i<threadNum;i++){
				Thread th = new Thread(this);
				th.setName("MailPoolThread_"+i);			
				th.start();
				threadList.add(th);
			}	
		}
		
		return true;
	}
	public boolean stopThread(){
		go = false;
		return true;
	}
	
	public void run(){
		AIOBOL88Mgt mgt = new AIOBOL88Mgt();
		
		//从CRM发送邮件，不需取bol88信息
		if(!"CRM".equals(from)){			
			//查询用户名
			Result rss = mgt.queryBol88();
			if(rss.retCode != ErrorCanst.DEFAULT_SUCCESS||rss.retVal ==null){
				
			}else{
				AIOBOL88Bean bean  = (AIOBOL88Bean)rss.retVal;
				username = bean.getUserName();
				JSESSION = bean.getPassword();
			}
		}
		
		while(go){
			try{
				
				System.out.println("-----------------线程："+Thread.currentThread().getName());
				
				//每天执行一次老邮件清除动作
				String curD = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
				if(!curD.equals(delDate)){
					mgt.delOldMailHash(leaveDate, sCompanyId);
					delDate = curD;
				}
				
				//时期则停止线程
				if(endSendDate != null && endSendDate.length() > 0 && curD.compareTo(endSendDate) > 0){
					mgt.stop(sCompanyId,from);
					go = false;
					break;
				}
				//未到发送时间
				if(startSendDate != null && endSendDate.length() > 0 && curD.compareTo(startSendDate) < 0){
					//不在时间内，暂停10分钟
					Thread.sleep(10*60000);
					continue;
				}
				
				
				int nowHour = Integer.parseInt(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss).substring(11,13));
				
				if(nowHour >= startTime && nowHour<=endTime){
					
					String mail = "";
					
					//取邮件					
					if("CRM".equals(from)){
						//从CRM中取数据
						mail = CRMMailObj.getCRMMail();
					}else{
						if(curKwMailNum == 0){
							//查询总邮件数量						
							curKwMailNum = mgt.getKeywordNum(curKeywordId);
						}
						//从bol88中取数据
						if(curKwMailNum > 0)
							mail = mgt.getMail(curKeywordId,curKwMailNum,username,JSESSION);
					}
					
					if(mail!= null && mail.length() > 0){	
						//将取得的邮件，每个邮件地址发送一封邮件，避免垃圾过滤
						String[] mails = mail.split(";");
						System.out.println("-----------------线程："+Thread.currentThread().getName()+";取邮件地址个数="+mails.length);
						for(int i=0;i<mails.length && go  ;i++){
							if(mails[i] != null && mails[i].length() > 0){
								send(mails[i]);
								if(threadNum== -1){
									//启用安全速度，每10分钟发送一次
									Thread.sleep(10*60000);
								}else{
									//每2秒发送一次
									Thread.sleep(2000);
								}
							}
						}
					}
					if(threadNum== -1){
						//启用安全速度，每10分钟发送一次
						Thread.sleep(10*60000);
					}else{
						//每2秒发送一次
						Thread.sleep(2000);
					}
				}else{
					//不在时间内，暂停10分钟
					Thread.sleep(10*60000);
				}
			}catch(Exception e){
				BaseEnv.log.error("MailPoolThread.run Error:",e);
				try{
					Thread.sleep(10000);
				}catch(Exception ee){}
			}
		}
	}
	
	public void send(String mail){
		AIOBOL88Mgt bol88mgt = new AIOBOL88Mgt();
		//发送外部邮件
		try {
			EMailMgt mgt = new EMailMgt();
			// 查询帐号信息
			Result rs = mgt.loadAccount(mailaccount);
			if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
				BaseEnv.log.error("MailPoolThread.send Error mailaccount "+mailaccount);
				INFO = "邮箱帐号不正确";
				return;
			}
			//计算邮件列表是否重复.
			//生成邮件hash
			String[] mails = mail.split(";");
			long[] mailHash = new long[mails.length];
			for(int i=0;i<mails.length ;i++){
				mailHash[i] = (mails[i]!=null&&mails[i].length()>0)?mails[i].hashCode():0;
			}
			//过滤已经发送过的邮件
			bol88mgt.checkMailHash(mailHash, sCompanyId);
			
			MailinfoSettingBean setting = (MailinfoSettingBean) rs
					.getRetVal();

			AIOEMail sm = new AIOEMail();
			boolean smtpauth = setting.getSmtpAuth() == 1 ? true : false;
			// 指定要使用的邮件服务器
			sm.setAccount(setting.getMailaddress(),
					setting.getPop3server(), setting.getPop3username(),
					setting.getPop3userpassword(), smtpauth, setting
							.getSmtpserver(), setting.getSmtpusername(),
					setting.getSmtpuserpassword(), setting.getPop3Port(),
					setting.getSmtpPort(), setting.getDisplayName(),
					"bol88",setting.getCreateby(),setting.getPopssl()==1?true:false,setting.getSmtpssl()==1?true:false,setting.getAutoAssign()==1?true:false);

			// 指定帐号和密码
			EMailMessage smes = new EMailMessage();
			// 增加收件人
			ArrayList mailList = new ArrayList();
			
			boolean hasReceived = false;
			for(int i=0;i<mailHash.length ;i++){
				if(mailHash[i] != 0){
					smes.addRecipient(EMailMessage.TO,
							mails[i], "");
					hasReceived = true;
				}
			}
			if(!hasReceived){
				return;
			}
			
			//每天执行一次抄送动作
			String curD = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
			if(!curD.equals(ccDate)){
				ccDate = curD;
				if(ccMail != null && ccMail.length() >0){
					String[] cm = ccMail.split(",|;");
					for(String cmail:cm){
						if(cmail!=null && cmail.length() > 0 ){
							smes.addRecipient(EMailMessage.CC,
									cmail, "");
						}
					}
				}
			}			
			
			// 原草稿附件
			if (attach != null && attach.length() > 0) {
				for (String of : attach.split(";")) {
					if (of != null && of.length() > 0)
						smes.putFile(of, new File(
								BaseEnv.FILESERVERPATH + "/email/"
										+ "bol88" + "/" + of));
				}
			}
			smes.setSubject(title);
			smes.setContent(true, content);
			smes.setDate(new Date());
			// 调用发送邮件接口
			sm.send(smes);
			INFO = "";
			
			//保存邮件地址
			bol88mgt.saveMailHash(mailHash, sCompanyId,from);
			
			int count =0;
            for(int i=0;i<mailHash.length;i++){
        		if(mailHash[i] != 0){
        			count ++;
        		}
        	}
            
            synchronized(totalLock){
            	totalCount += count;
            }
            
			
		} catch (AuthenticationFailedException ex) {
			BaseEnv.log.error("EMailAction.receiveMail Error ", ex);
			INFO = "邮箱:"+ex.getMessage();
		} catch (MessagingException ex) {
			BaseEnv.log.error("EMailAction.receiveMail Error ", ex);
			INFO = "邮箱:"+ex.getMessage();
		} catch (Exception ex) {
			BaseEnv.log.error("EMailAction.add SendMail Error ", ex);
			INFO = "邮箱:"+ex.getMessage();
		}
	}
	
}
