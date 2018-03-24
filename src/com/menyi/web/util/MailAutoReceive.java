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
 * <p>Company: ������</p>
 *
 * @author ������
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
                	//��ѯ�������Զ����յ��ʻ����������
                	EMailMgt mgt = new EMailMgt();
                	Result rs = mgt.selectAutoReplayAccount();
                	if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
						for(MailinfoSettingBean bean :(List<MailinfoSettingBean>)rs.retVal){
							if (!go) {
								break;
							}
							//������䳬��ֱ����ʾ
							if(!mgt.checkMailSizeList(1, bean.getId(), bean.getCreateby())){
								BaseEnv.log.info("----------MailAutoReceive mail sapce limit "+bean.getMailaddress());
								continue;
							}
							if(mailMap.get(bean.getId())==null){
								mailMap.put(bean.getId(), System.currentTimeMillis());
							}else{
								long preTime = (Long)mailMap.get(bean.getId());
								//ʱ��Ϊ-1��������ִ�С�
								if(preTime != -1 && (System.currentTimeMillis() - preTime)/60000 > bean.getAutoReceive()){
									receiveByThread(bean);
								}
							}
                		}
                	}
                }

                try {
                    Thread.sleep(1000 * 60); //ÿ��������һ��
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
    	//����һ�����߳���ִ��
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
    	mailMap.put(mssId , -1l);	//��������ִ�н��ղ���
		//����Ԥ��ʱ�䣬���Խ����Զ�����
		try{
			AIOEMail sm = new AIOEMail();
			boolean smtpauth = bean.getSmtpAuth() == 1 ? true : false;
			// ָ��Ҫʹ�õ��ʼ�������
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
//				BaseEnv.log.error("MailAutoReceive runReceive() �����ʼ����������󡣡���") ;
			}
			
			
			sm.retentDay = bean.getRetentDay();	  
			
			
			//��������ʼ�������
			int count = 1000;
			//��������ʼ�������
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
			
			//�ڴ˴���ö�Ӧ�ı��˻����Լ����ʼ����˹�����ϸ
			sm.setOfRules = new HashSet<String[]>();//������еĹ�����Ϣ�Լ�����groupID��Ϣ
			Result rsOfFilterInfo = efMgt.getFilterInfoByMsId(mssId);
			if (rsOfFilterInfo.retCode == ErrorCanst.DEFAULT_SUCCESS) {// ��ѯ�ɹ�

				// �õ����˹�������
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
				//����ʼ��д����������ݣ���ɾ���������ϵ��ʼ���ַ
				if("info@auware.com".equals(bean.getMailaddress())){
					filterMail(mailInfobean);
				}
				//mailMap.put(bean.getId(), System.currentTimeMillis()); //ÿ��һ���ʼ�����һ��ʱ�����⣬���ʼ���ʽ�����ڴ�������߳��������޷��ٴ��������߳�		
			}	                					
			
			sm.closeInbox(); 	                					
			
			
			
			//����ʼ��д����������ݣ���ɾ���������ϵ��ʼ���ַ
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
		mailMap.put(bean.getId(), System.currentTimeMillis()); //��ִ�н���ʱ���ʶ		
    }
    
     
    /**
     * ������԰��������䣬���˳���ɾ�����ʼ�
     * @param mailInfobean
     */
    private void filterMail(OAMailInfoBean mailInfobean){
    	String title = mailInfobean.getMailTitle();
    	if(title == null || title.trim().length() ==0) return;
    	if(title.indexOf("failure")> -1 || title.indexOf("Failure")> -1  || title.indexOf("Undelivered")> -1 || title.indexOf("δ�ʹ�")> -1 ||
    			 title.indexOf("δ�ʹ�")> -1 || title.indexOf("failed")> -1 || title.indexOf("��ʱ����")> -1 || title.indexOf("Undeliverable")> -1  ||
    			 title.indexOf("����ʧ��")> -1){
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
                url_con.setConnectTimeout(20000);//����λ�����룩jdk 1.5�������,���ӳ�ʱ
                url_con.setReadTimeout(20000);//����λ�����룩jdk 1.5�������,��������ʱ
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
            System.out.println("--�Զ�ɾ���ʼ���ַ��"+delMailStr);
    	}
    }
    
    public static OAMailInfoBean receiveContent(AIOEMail sm, String userId,
			String emailType) {
		try {
			EMailMessage em = sm.receiveBody(false,"");
			// �����ʼ�
			OAMailInfoBean mailInfo = new OAMailInfoBean();
			mailInfo.setBegReplay(em.isBegReplay()?"1":"0");
			mailInfo.setId(IDGenerater.getId());
			mailInfo.setCreateTime(BaseDateFormat.format(new Date(),
					BaseDateFormat.yyyyMMddHHmmss));
			mailInfo.setEmailType(1);
			// mailInfo.setFromUserId();
			// ����
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

			// ������
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
			mailInfo.setState(0); // ���ʼ�
			mailInfo.setToUserId(userId); // ���͸����ѵ��ʼ�
			mailInfo.setUserId(userId);
			mailInfo.setAccount(emailType);
			mailInfo.setMailSize(em.getMailSize());
			mailInfo.setMailUID(em.getMessageId());

			// ���浽���ݿ���ȥ
			EMailMgt mgt = new EMailMgt();			
			
			mailInfo.setGroupId(mgt.filterMailFrom(mailInfo)?"1":"4"); //���й������
			
			
			//���Ҫ�������������ݣ����豣��guid,���ﲻ����û�б�������������,���洢guid,��Ϊ��Щ����������ɾ����ʶ�󣬲������ɾ��
			//if(sm.retentDay != 0){
				Result rs =mgt.addGuid(mailInfo.getAccount(), mailInfo.getMailUID(),mailInfo.getMailTime());
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
					return mailInfo;
				}
			//}
			
			//����emlԭ�ĺ�����
			mailInfo.setMailcharset(em.getHeadCharset());
			mailInfo.setEmlfile(mailInfo.getId()+".eml");
			
			
			
			// ��ø������Ӧ����ϸ������Ϣ mj
			// �õ����˹�������
			if(sm.setOfRules != null){
				String maddr = GlobalsTool.getMailAddress(mailInfo.getMailFrom());
				for (Iterator iterator = sm.setOfRules.iterator(); iterator
						.hasNext();) {
					String[] filter = (String[]) iterator.next();
					// �����ʼ����û��������ַ �Ƿ�����������������							
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
				BaseEnv.log.error("�޷������ʼ�ԭ��",e);
				//e.printStackTrace();
			}
			
			
			//�Ƿ���Ҫ�Զ�����
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
