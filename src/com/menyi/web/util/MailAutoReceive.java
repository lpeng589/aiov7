package com.menyi.web.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;

import com.dbfactory.Result;
import com.koron.oa.bean.EmailFilter;
import com.koron.oa.bean.MailinfoSettingBean;
import com.koron.oa.bean.OAMailInfoBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.email.EMailMgt;
import com.menyi.email.emailFilter.EmailFilterMgt;
import com.menyi.email.util.AIOEMail;
import com.menyi.email.util.EMailMessage;


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
public class MailAutoReceive extends Thread {
    private boolean go = true;
    private HashMap<String,Long> mailMap = new HashMap<String,Long>();
    private ServletContext servletContext;
    private ArrayList<String> delMailList = new ArrayList<String>();
    final static EmailFilterMgt efMgt = new EmailFilterMgt();
    public MailAutoReceive(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.setName("Mail Auto Receive Thread");
        this.setDaemon(true);
    }

    public void stopThread() {
        go = false;
    }

    public void run() {
        while (go) {
            try {
                if (SystemState.instance.dogState == SystemState.DOG_FORMAL ||
                    SystemState.instance.dogState == SystemState.DOG_EVALUATE ) {
                	//查询所有需自动接收的帐户。逐个接收
                	EMailMgt mgt = new EMailMgt();
                	Result rs = mgt.selectAutoReplayAccount();
                	if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
						for(MailinfoSettingBean bean :(List<MailinfoSettingBean>)rs.retVal){
							if (!go) {
								break;
							}
							//如果邮箱超限直接提示
							if(!mgt.checkMailSizeList(1, bean.getId(), bean.getCreateby())){
								BaseEnv.log.info("----------MailAutoReceive mail sapce limit "+bean.getMailaddress());
								continue;
							}
							if(mailMap.get(bean.getId())==null){
								mailMap.put(bean.getId(), System.currentTimeMillis());
							}else{
								long preTime = (Long)mailMap.get(bean.getId());
								//时间为-1表明正在执行。
								if(preTime != -1 && (System.currentTimeMillis() - preTime)/60000 > bean.getAutoReceive()){
									receiveByThread(bean);
								}
							}
                		}
                	}
                }

                try {
                    Thread.sleep(1000 * 60); //每分钟启动一次
                } catch (InterruptedException ex) {
                }
                if (!go) {
                    break;
                }
            } catch (Exception ex1) {
                BaseEnv.log.error("---MailAutoReceive run() error -" + ex1);
            }
        }
    }
    
    public void receiveByThread(final MailinfoSettingBean bean){
    	//启动一个新线程来执行
    	Thread td = new Thread(){
    		public void run(){
    			runReceive(bean);
    		}
    	};
    	td.setName("receiveThread_"+bean.getId());
    	td.start();
    	
    }
    
    public void runReceive(MailinfoSettingBean bean) {    	
    	EMailMgt mgt = new EMailMgt();
    	String mssId = bean.getId();
    	mailMap.put(mssId , -1l);	//表明正在执行接收操作
		//超过预置时间，可以进行自动接收
		try{
			AIOEMail sm = new AIOEMail();
			boolean smtpauth = bean.getSmtpAuth() == 1 ? true : false;
			// 指定要使用的邮件服务器
			sm.setAccount(bean.getMailaddress(),
					bean.getPop3server(), bean.getPop3username(),
					bean.getPop3userpassword(), smtpauth, bean
							.getSmtpserver(), bean.getSmtpusername(),
							bean.getSmtpuserpassword(), bean.getPop3Port(),
							bean.getSmtpPort(), bean.getDisplayName(),
							mssId,bean.getCreateby(),bean.getPopssl()==1?true:false,bean.getSmtpssl()==1?true:false,bean.getAutoAssign()==1?true:false);

			String[] uids = null ;
			try{
				uids = sm.connect();
			}catch (Exception e) {
//				BaseEnv.log.error("MailAutoReceive runReceive() 连接邮件服务器错误。。。") ;
			}
			
			
			sm.retentDay = bean.getRetentDay();	  
			
			
			//这里进行邮件，过滤
			int count = 1000;
			//这里进行邮件，过滤
			while(uids !=null && uids.length > 0){
				String[] tempUids = uids;
				String[] uid = new String[0];
				if(uids.length > count){
					uids = new String[tempUids.length -count];
					uid = new String[count];
					System.arraycopy(tempUids, count, uids, 0, tempUids.length -count);
					System.arraycopy(tempUids, 0, uid, 0, count);
				}else{
					uid = uids;
					uids = new String[0];
				}
				Result result= mgt.handNewMail1000(uid, sm, mssId, bean.getRetentDay());
				if(result.retCode != ErrorCanst.DEFAULT_SUCCESS){
					BaseEnv.log.info("MailAutoReceive.run handNewMail Error "+result.retCode);
					continue;
				}
			}
			
			//在此处获得对应的本账户的自己的邮件过滤规则详细
			sm.setOfRules = new HashSet<String[]>();//存放所有的规则信息以及分组groupID信息
			Result rsOfFilterInfo = efMgt.getFilterInfoByMsId(mssId);
			if (rsOfFilterInfo.retCode == ErrorCanst.DEFAULT_SUCCESS) {// 查询成功

				// 得到过滤规则数组
				List<EmailFilter> list = (List<EmailFilter>) rsOfFilterInfo.retVal;
				for (int i = 0; i < list.size(); i++) {
					EmailFilter ef = (EmailFilter) list.get(i);
					String rule = ef.getFilterCondition();
					String groupId = ef.getRefOaFolderId();
					String[] filterArr = rule.split(",|;| |\\n");							
					for (int j = 0; j < filterArr.length; j++) {
						String f = filterArr[j].trim();
						sm.setOfRules.add(new String[]{f,groupId});
					}
				}
			}
			
			boolean hasMail = false;
			for(int i=sm.newMsgList.size()-1;i>=0;i--){
				if (!go) { 
					break;
				} 
				EMailMessage em = sm.receiveNextHeader();	 
				OAMailInfoBean mailInfobean = this.receiveContent(sm, bean.getCreateby(), bean.getId());
				
				if(mailInfobean != null){
					hasMail = true;
				}
				//如果邮件中带有特殊内容，则删除服务器上的邮件地址
				if("info@auware.com".equals(bean.getMailaddress())){
					filterMail(mailInfobean);
				}
				//mailMap.put(bean.getId(), System.currentTimeMillis()); //每收一封邮件更新一次时，避免，因邮件格式导致内存溢出后，线程死亡后，无法再次启动新线程		
			}	                					
			
			sm.closeInbox(); 	                					
			
			
			
			//如果邮件中带有特殊内容，则删除服务器上的邮件地址
			if("erp@koronsoft.com".equals(bean.getMailaddress())){
				commitDel();
			}
			if(hasMail){
				NotifyFashion notify = new NotifyFashion(bean.getCreateby(),bean.getMailaddress()+"RES<oa.mail.msg.newMail>","<a href=\"javascript:mdiwin('/EMailAction.do?operation=4&isFromDeskTop=trues&groupId=1&emailType="+bean.getId()+"','RES<oa.mail.myMail>')\">"+bean.getMailaddress()+"RES<oa.mail.msg.newMail></a>",bean.getCreateby(),4,"OA","") ; 
				notify.sendAdvice(bean.getCreateby(),bean.getMailaddress()+"RES<oa.mail.msg.newMail>","<a href=\"javascript:mdiwin('/EMailAction.do?operation=4&isFromDeskTop=trues&groupId=1&emailType="+bean.getId()+"','RES<oa.mail.myMail>')\">"+bean.getMailaddress()+"RES<oa.mail.msg.newMail></a>",bean.getCreateby(),"","email");
			}
		}catch(Exception e){
			//BaseEnv.log.error("---MailAutoReceive run() - receive mail error -" , e);
		}
		mailMap.put(bean.getId(), System.currentTimeMillis()); //置执行结束时间标识		
    }
    
     
    /**
     * 如果来自奥威的邮箱，过滤出需删除的邮件
     * @param mailInfobean
     */
    private void filterMail(OAMailInfoBean mailInfobean){
    	String title = mailInfobean.getMailTitle();
    	if(title == null || title.trim().length() ==0) return;
    	if(title.indexOf("failure")> -1 || title.indexOf("Failure")> -1  || title.indexOf("Undelivered")> -1 || title.indexOf("未送达")> -1 ||
    			 title.indexOf("未送达")> -1 || title.indexOf("failed")> -1 || title.indexOf("超时错误")> -1 || title.indexOf("Undeliverable")> -1  ||
    			 title.indexOf("传输失败")> -1){
    		Pattern pattern = Pattern.compile("([\\w\\.\\-]+@[\\w\\-\\.]+[\\.]{1}[\\w\\-]+)",Pattern.CASE_INSENSITIVE);
    		Matcher matcher = pattern.matcher(mailInfobean.getMailContent());
    		StringBuffer buffer = new StringBuffer();
    		while(matcher.find()){        
    			if(!"erp@koronsoft.com".equals(matcher.group(1))){
    				delMailList.add( matcher.group(1));
    			}
    		}
    	}
    }
    
    private void commitDel(){    	
    	while(delMailList.size() > 0){
            HttpURLConnection url_con = null;
            String responseContent = null;
            String delMailStr = "";
            try
            {
            	
            	int size = delMailList.size();
            	for(int i=0;i< size && i<50;i++){
            		delMailStr += delMailList.remove(0)+";";
            	}
            	
                URL url = new URL(BaseEnv.bol88URL+"/MailPool?op=del");
                url_con = (HttpURLConnection) url.openConnection();
                url_con.setRequestMethod("POST");
                url_con.setConnectTimeout(20000);//（单位：毫秒）jdk 1.5换成这个,连接超时
                url_con.setReadTimeout(20000);//（单位：毫秒）jdk 1.5换成这个,读操作超时
                url_con.setDoOutput(true);
                url_con.getOutputStream().write(("data="+delMailStr).getBytes());
                url_con.getOutputStream().flush();
                url_con.getOutputStream().close();
                
                InputStream in = url_con.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(in));
                String tempLine = rd.readLine();
                StringBuffer tempStr = new StringBuffer();
                String crlf=System.getProperty("line.separator");
                while (tempLine != null)
                {
                    tempStr.append(tempLine);
                    tempStr.append(crlf);
                    tempLine = rd.readLine();
                }
                responseContent = tempStr.toString();
                System.out.println("return:"+responseContent);
                rd.close();
                in.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (url_con != null)
                {
                    url_con.disconnect();
                }
            }
            System.out.println("--自动删除邮件地址："+delMailStr);
    	}
    }
    
    public static OAMailInfoBean receiveContent(AIOEMail sm, String userId,
			String emailType) {
		try {
			EMailMessage em = sm.receiveBody(false,"");
			// 保存邮件
			OAMailInfoBean mailInfo = new OAMailInfoBean();
			mailInfo.setBegReplay(em.isBegReplay()?"1":"0");
			mailInfo.setId(IDGenerater.getId());
			mailInfo.setCreateTime(BaseDateFormat.format(new Date(),
					BaseDateFormat.yyyyMMddHHmmss));
			mailInfo.setEmailType(1);
			// mailInfo.setFromUserId();
			// 附件
			String mailAttaches = "";
			Map map_file = em.getFiles();
			if (map_file != null) {
				Set set = map_file.keySet();
				Iterator iterator = set.iterator();
				while (iterator.hasNext()) {
					String fileName = iterator.next().toString();
					mailAttaches += fileName + ";";
				}
			}
			mailInfo.setMailAttaches(mailAttaches);

			String innerAttaches = "";
			if (em.getContentIdList() != null) {
				for (Object o : em.getContentIdList()) {
					String[] strs = (String[]) o;
					innerAttaches += strs[1];
				}
			}
			mailInfo.setInnerAttaches(innerAttaches);

			// 接收人
			String mto = "";
			String mcc = "";
			String mbcc = "";
			for (EMailMessage.Recipienter rc : em.getRecipientList()) {
				if (rc.type == EMailMessage.TO) {
					mto += rc.toString() + ";";
				} else if (rc.type == EMailMessage.CC) {
					mcc += rc.toString() + ";";
				} else if (rc.type == EMailMessage.BCC) {
					mbcc += rc.toString() + ";";
				}
			}
			mailInfo.setMailCc(mcc);
			mailInfo.setMailTo(mto);
			mailInfo.setMailBCc(mbcc);

			mailInfo.setMailContent(em.getContent());
			mailInfo.setMailFrom(em.getSender().toString());
			mailInfo.setMailTime(BaseDateFormat.format(em.getDate()==null?new Date():em.getDate(),
					BaseDateFormat.yyyyMMddHHmmss));
			mailInfo.setMailTitle(em.getSubject());
			mailInfo.setState(0); // 新邮件
			mailInfo.setToUserId(userId); // 发送给自已的邮件
			mailInfo.setUserId(userId);
			mailInfo.setAccount(emailType);
			mailInfo.setMailSize(em.getMailSize());
			mailInfo.setMailUID(em.getMessageId());

			// 保存到数据库中去
			EMailMgt mgt = new EMailMgt();			
			
			mailInfo.setGroupId(mgt.filterMailFrom(mailInfo)?"1":"4"); //进行规则过滤
			
			
			//如果要保留服务器备份，则需保存guid,这里不管有没有保留服务器备份,都存储guid,因为有些服务器设置删除标识后，不会真的删除
			//if(sm.retentDay != 0){
				Result rs =mgt.addGuid(mailInfo.getAccount(), mailInfo.getMailUID(),mailInfo.getMailTime());
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
					return mailInfo;
				}
			//}
			
			//保存eml原文和语言
			mailInfo.setMailcharset(em.getHeadCharset());
			mailInfo.setEmlfile(mailInfo.getId()+".eml");
			
			
			
			// 获得该邮箱对应的详细规则信息 mj
			// 得到过滤规则数组
			if(sm.setOfRules != null){
				String maddr = GlobalsTool.getMailAddress(mailInfo.getMailFrom());
				for (Iterator iterator = sm.setOfRules.iterator(); iterator
						.hasNext();) {
					String[] filter = (String[]) iterator.next();
					// 发送邮件的用户的邮箱地址 是否包含在这个规则里面							
					if (StringUtils.isNotBlank(maddr)
							&& maddr.toLowerCase().endsWith(
									filter[0].toLowerCase())) {
						mailInfo.setGroupId(filter[1]);
						break;
					}

				}
			}
			
			rs = mgt.addMail(mailInfo);			
			if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
				return null;
			}
			
			File file = new File(BaseEnv.FILESERVERPATH + "/email/" + sm.accountId+"/eml/"+mailInfo.getId()+".eml");
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			} 
			try{
				sm.curMsg.writeTo(new FileOutputStream(file));
			}catch(Exception e){
				BaseEnv.log.error("无法保存邮件原文",e);
				//e.printStackTrace();
			}
			
			
			//是否需要自动分派
			if(sm.autoAssign){
				String mf = mailInfo.getMailFrom();
				if(mf.indexOf("<")> 0){
					mf = mf.substring(mf.indexOf("<")+1,mf.indexOf(">"));
				}
				String assignusers="";
				Result ownerrs = mgt.getClientOwnerByEmail("'"+mf+"'");
				if(ownerrs.retCode == ErrorCanst.DEFAULT_SUCCESS){
					ArrayList list = (ArrayList)ownerrs.retVal;
					for(int i=0;list != null && i<list.size();i++){
						String uId = list.get(i).toString();
						assignusers += list.get(i)+",";								
					}
				}
				if(assignusers.length() > 0){
					mgt.mailAssign(new String[]{mailInfo.getId()}, emailType, userId, assignusers);
					NotifyFashion notify = new NotifyFashion() ; 
					String adviceTitle = GlobalsTool.replaceHTML(mailInfo.getMailTo()).replaceAll(";", "") + mailInfo.getMailTitle();
					notify.sendAdvice(sm.accountUserId,
							adviceTitle,
							"<a href=\"javascript:mdiwin('/EMailAction.do?operation=5&emailType=assign&noback=true&keyId="+mailInfo.getId()+"&newOpen=true','RES<mail.msg.assignnewMail>')\">"+adviceTitle,
							assignusers,mailInfo.getId(),"email");

				}
			}
			
			return mailInfo;
		} catch (Exception e) {
			//BaseEnv.log.error("MailAutoReceive.receiveContent Error", e);
			return null;
		}
		
	}
    public static void main(String[] args) {
		String a1 = "abc";
		String a2 = "b";
		if (StringUtils.contains(a1, a2)) {
			System.out.println(33);
			
		}
	}
}
