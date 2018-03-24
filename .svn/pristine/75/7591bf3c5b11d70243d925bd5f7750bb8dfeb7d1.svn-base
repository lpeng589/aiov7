package com.menyi.email;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbfactory.Result;
import com.koron.oa.bean.OAMailInfoBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.web.advice.AdviceForm;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.email.util.AIOEMail;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;

public class EMailSendThread extends Thread {
	private EMailMgt mgt = new EMailMgt();
	public EMailSendThread(){}
	
	/**
	 * 分析字符串中的人员信息。 返回邮件分析不成功的地址信息. 邮件匹配成功放入list中 匹配规则，是先匹配，部门，匹配成功，则给部门所人有员发送信息
	 * 失败再匹配职员列表，匹配成功，则取职员信息， 匹配不上职员，则反回匹配不成功
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	private String innerMailParse(String str, ArrayList list) {
		String emails[] = str.split(",|;");
		String error = "";
		HashMap map = new HashMap();
		for (String email : emails) {
			if (email != null && email.length() > 0&& !"null".equals(email)) {
				// 从部门表中查询
				Result rs = mgt.selectDeptUser(email);
				if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS
						&& ((ArrayList) rs.getRetVal()).size() > 0) {
					for (Object o : (ArrayList) rs.getRetVal()) {
						Object[] os = (Object[]) o;
						map.put(os[0], os[0]);
					}
				} else {
					// 从职员表中查询
					rs = mgt.selectUser(email);
					if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS
							&& ((ArrayList) rs.getRetVal()).size() > 0) {
						for (Object o : (ArrayList) rs.getRetVal()) {
							Object[] os = (Object[]) o;
							map.put(os[0], os[0]);
						}
					} else {
						error += email + ";";
					}
				}
			}
		}
		list.addAll(map.keySet());
		return error;
	}
	private OAMailInfoBean mailInfo;
	private EMailForm mailForm;
	private LoginBean loginBean;
	private Map<String,String> map;
	public  EMailSendThread(OAMailInfoBean mailInfo,EMailForm mailForm,LoginBean loginBean,Map<String,String> map){
		this.mailInfo=mailInfo;
		this.mailForm=mailForm;
		this.loginBean=loginBean;
		this.map=map;
	}
	
	
	private void send(){
		Result rs =null;
		String pId = mailInfo.getId();//mj	
		// 发送内部邮件
		mailInfo = new OAMailInfoBean();

		mailInfo.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		mailInfo.setEmailType(0);
		mailInfo.setFromUserId(loginBean.getId());
		mailInfo.setGroupId("1"); // 存入收件人的收件箱
		// 附件
		mailInfo.setMailAttaches(map.get("mailAttaches"));

		// 接收人
		mailInfo.setMailCc(mailForm.getCc());
		mailInfo.setMailTo(mailForm.getTo());
		mailInfo.setMailBCc(mailForm.getBcc());

		mailInfo.setMailContent(mailForm.getMailContent());
		mailInfo.setMailFrom(loginBean.getEmpFullName());
		mailInfo.setMailTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		mailInfo.setMailTitle(mailForm.getMailTitle());
		mailInfo.setState(0); // 新邮件
		mailInfo.setAccount("");
		mailInfo.setSendeMailType(map.get("sendEmailType"));
		mailInfo.setMailSize( Integer.parseInt(map.get("mailSize")));
		mailInfo.setBegReplay(mailForm.getBegReplay());
		mailInfo.setCollectionType("0");
		
		// 分析收件人等。
		ArrayList mailList = new ArrayList();
		String errorEmail = innerMailParse(mailInfo.getMailTo() + ";" + mailInfo.getMailCc() + ";" + mailInfo.getMailBCc(),mailList);
		UserMgt ugt = new UserMgt() ;
		AdviceMgt adviceMgt = new AdviceMgt() ;
		//对收件人进行处理，单独发送到收件人的邮箱中
		for (Object o : mailList) {
			String keyId = IDGenerater.getId() ;
			mailInfo.setId(keyId);
			mailInfo.setMailUID(mailInfo.getId());
			mailInfo.setToUserId(o.toString()); // 发送给自已的邮件
			mailInfo.setUserId(o.toString());
			mailInfo.setPId(pId);//标示该收件人邮件对应的发件人邮件id mj
			String otherMailAttach = "";
			//拷贝附件
			String mailAttaches=map.get("mailAttaches");
			String attachPath=map.get("attachPath");
			if (mailAttaches != null) {
				String atts[] = mailAttaches.split(";");
				for (String att : atts) {
					if (att != null && att.length() > 0) {
						try {
							InputStream is = null;
							if(att.indexOf("emlfile") != -1 && att.substring(att.indexOf("emlfile=")+"emlfile=".length(),att.indexOf("&charset")).length() > 0){
								//emlfile=c5daa3c8_1205141821114950004.eml&charset=&attPath=c5daa3c8_1205141821011640001&fileName=QQ截图20120113150732(1).png
								int pos = att.indexOf("emlfile=")+"emlfile=".length();
								String emlfile = att.substring(pos,att.indexOf("&",pos));
								pos = att.indexOf("attPath=")+"attPath=".length();
								String attPath = att.substring(pos,att.indexOf("&",pos));
								pos = att.indexOf("fileName=")+"fileName=".length();
								String fileName = att.substring(pos);
								att = fileName;
								is = AIOEMail.readFileFormEml(BaseEnv.FILESERVERPATH+"/email/"+attPath+"/eml/"+emlfile,fileName, map.get("charset"));		  
								mailInfo.setMailSize(mailInfo.getMailSize()	+ is.available());
							}else{								
								is = new FileInputStream(
										BaseEnv.FILESERVERPATH + "/email/"
												+ attachPath + "/" + att);
							}
							
							String oldfileName = att;
							String path = BaseEnv.FILESERVERPATH
								+ "/email/inner" + mailInfo.getUserId()
								+ "/";
							File newFile = new File(path, att);                       
	                        for(int i=1;newFile.exists()&& i<1000;i++){
	                        	//文件名存在，改名
	                        	att = oldfileName.substring(0,oldfileName.lastIndexOf("."))+"("+i+")"+oldfileName.substring(oldfileName.lastIndexOf("."));
	                        	newFile = new File(path, att); 
	                        }
							newFile.getParentFile().mkdirs();
							FileOutputStream fos = new FileOutputStream(
									newFile);
							byte[] bs = new byte[8192];
							int count = 0;
							while((count = is.read(bs)) > -1){
								fos.write(bs,0,count);
							}
							is.close();
							fos.close();
							otherMailAttach +=att+";";
						} catch (Exception e) {
						}
					}
				}
			}
			mailInfo.setMailAttaches(otherMailAttach);
			// 保存到数据库中去
			try{
			rs = mgt.addMail(mailInfo);
			
			/*添加通知消息提醒*/
	        List list=(List)ugt.queryEmp(o.toString()).getRetVal();
				if (list != null && list.size() > 0) {
					adviceMgt.add(
							loginBean.getId(),
							map.get("oa.mail.email") + "：" + mailInfo.getMailTitle(),
							"<a href=\"javascript:mdiwin('/EMailAction.do?operation=5&detailType=true&keyId="
									+ keyId
									+ "&groupId="
									+ mailInfo.getGroupId()
									+ "&emailType="
									+ map.get("emailType")
									+ "&noback=true','"
									+ map.get("oa.mail.myMail")
									+ "')\" >"
									+ map.get("oa.mail.email")
									+ "："
									+ mailInfo.getMailTitle() + "</a>",
							o.toString(), 
							keyId, 
							"email");
				}
            } catch (Exception e) {
				// 错误提示
				adviceMgt.add(
						loginBean.getId(),
						"邮件发送失败提示：" + mailInfo.getMailTitle(),
						"<a href=\"javascript:mdiwin('/EMailAction.do?operation=5&detailType=true&keyId="
								+ pId
								+ "&groupId="
								+ mailInfo.getGroupId()
								+ "&emailType="
								+ map.get("emailType")
								+ "&noback=true','"
								+ map.get("oa.mail.myMail")
								+ "')\" >"
								+ map.get("oa.mail.email")
								+ "："
								+ mailInfo.getMailTitle() + "</a>",
						loginBean.getId(), 
						pId, 
						"email");
		    }
			
		}
		
		//增加发件人历史记录
		mgt.emailSendHistory(mailInfo);
	}
	
     public void run(){
    	 send();
     }
}
