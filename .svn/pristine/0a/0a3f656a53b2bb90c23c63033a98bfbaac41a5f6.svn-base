package com.menyi.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dbfactory.Result;
import com.google.gson.JsonObject;
import com.koron.oa.bean.OAMailInfoBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.advice.AdviceForm;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.email.EMailMgt;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

/**
 * 
 * <p>
 * 该类是多线程类。 该类是一个公共类，几种常用的信息通知方式，如邮件，手机，即时消息，待办
 * </p>
 * 
 * @Copyright: 科荣软件
 * 
 * @Date:2009-10-7
 * 
 * @Author 文小钱
 */
public class NotifyFashion extends Thread {

	private PublicMgt pubMgt = new PublicMgt();
	private String userId = ""; // 用户ID
	private String title = ""; // 标题
	private String content = ""; // 内容
	private String popedomUserIds = ""; // 通知对象
	private int wakeType = 0; // 提醒方式
	private String isOA = ""; // 是否是OA模块调 其它（进销存,CRM,HR）
	private String keyId = ""; // 关系单据ID
	private String otherEmail ;
	private String otherSMS ;
	private String type ;

	public NotifyFashion(String userId, String title, String content,
			String popedomUserIds, int wakeType, String isOA, String keyId) {
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.popedomUserIds = popedomUserIds;
		this.wakeType = wakeType;
		this.isOA = isOA;
		this.keyId = keyId;
	}
	public NotifyFashion(){
		
	}
	
	public NotifyFashion(String userId, String title, String content,
			String popedomUserIds, int wakeType,String isOA, String keyId,
			String otherEmail, String otherSMS,String type) {
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.popedomUserIds = popedomUserIds;
		this.wakeType = wakeType;
		this.isOA = isOA;
		this.keyId = keyId;
		this.otherEmail = otherEmail ;
		this.otherSMS = otherSMS ;
		this.type = type ;
	}

	/**
	 * 根据不同的提醒方式调用相应的通知方法
	 * 
	 * @param request
	 * @param title
	 * @param content
	 * @param popedomUserIds
	 * @param wakeType
	 *            提醒方式
	 */
	public void sendNotify(String userId, String title, String content,
			String popedomUserIds, int wakeType, String isOA, String keyId,
			String otherEmail, String otherSMS,String type) {
		switch (wakeType) {
		
		case 1: // 手机提醒
			sendSMS(userId, title, content, popedomUserIds, isOA, otherSMS);
			break;
		case 2: // 邮件提醒
			sendInnerMail(userId, title, content, popedomUserIds, isOA, keyId,otherEmail);
			break;
		case 4: // 通知提醒
			sendAdvice(userId, title, content, popedomUserIds, keyId,type);
			break;
		default:
			break;
		}
	}

	/**
	 * 用邮件的方式，通知对方
	 * 
	 * @param userId 用户ID
	 * @param title 标题
	 * @param content  内容
	 * @param popedomUserIds  通知对象
	 */
	public void sendInnerMail(String userId, String title, String content,
			String popedomUserIds, String isOA, String keyId,String otherEmail) {

		// 查询发送人的名称，邮件发送人保持的是用户的名称
		Result rs = new EMailMgt().selectUserNames("'" + popedomUserIds.replaceAll(",", "','") + "'");
		String popedomUserName = "";
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			HashMap<String, String> userMap = (HashMap<String, String>) rs.getRetVal();
			for (String userName : userMap.values()) {
				popedomUserName += userName + ",";
			}
			if (popedomUserName.length() > 0) {
				popedomUserName = popedomUserName.substring(0, popedomUserName.length() - 1);
			}
		}

		EMailMgt infoMget = new EMailMgt();
		OAMailInfoBean info = new OAMailInfoBean();
		String createTime = BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss);
		String infoId = IDGenerater.getId();
		info.setId(infoId);
		info.setFromUserId(userId);
		info.setCreateTime(createTime);
		info.setMailTitle(title);
		info.setMailContent(content);
		info.setEmailType(0);
		info.setGroupId("3");
		info.setMailTo(popedomUserName);
		info.setState(1);
		info.setUserId(userId);
		info.setMailTime(createTime);
		info.setRelationId(keyId);
		info.setAccount("");
		Result oaRs=new Result();
		if(!"workflow".equals(isOA)){
			oaRs = infoMget.addMail(info);
		}

		if (oaRs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			// 向指定的用户邮箱中添加一条纪录
			EMailMgt sendMgt = new EMailMgt();
			String[] popedomUser = null;
			String strContent = replaceA(content);
			if (popedomUserIds != null && !"".equals(popedomUserIds)) {
				popedomUser = popedomUserIds.split(","); // 通知对象
				for (int i = 0; i < popedomUser.length; i++) {
					if(popedomUser[i]==null || popedomUser[i].length()==0){
						continue ;
					}
					// 发送外部邮件
					//if ("yes".equals(isOA)||"workflow".equals(isOA)) {
					String[] to = (String[]) pubMgt.selTelByUserId(popedomUser[i]).getRetVal();// 外部邮件地址
					if (to!=null && to.length>0 && to[1] != null && !"".equals(to[1]) && to[1].contains("@")) {
							sendMgt.sendOuterMailInfter("1", to[1], title,strContent, null);
					}else {
						String[] empArray = (String[]) pubMgt.selTelEmailByUserId(userId).getRetVal();
						String emial = empArray[0];
						if (!"".equals(emial) && emial.contains("@")) {
							sendMgt.sendOuterMailInfter(userId, emial, title,strContent, null);
						}
					}
				}
			}
			if(otherEmail!=null && otherEmail.length()>0){
				String[] otherEmails = otherEmail.split(":") ;
				for(String email : otherEmails){
					sendMgt.sendOuterMailInfter("1", email, title,strContent, null);
				}
			}
		}
	}

	/**
	 * 用通知的方式，通知对方
	 * 
	 * @param userId  用户ID
	 * @param title  标题
	 * @param content  内容
	 * @param popedomUserIds  通知对象
	 */
	public void sendAdvice(String userId, String title, String content,
			String receiveIds, String relationId, String type) {
		if (receiveIds != null && !"".equals(receiveIds) && content != null
				&& content.trim().length() > 0) {
			new AdviceMgt().add(userId, title, content, receiveIds, relationId,
					type);
		}
	}

	/**
	 * 用手机短信的方式，通知对方
	 */
	public void sendSMS(String userId, String title, String content,
			String popedomUserIds, String isOA, String otherSMS) {

		String[] popedomUser = null;
		content = replaceA(content);
		if (popedomUserIds != null && !"".equals(popedomUserIds)
								   && content!=null && content.trim().length()>0) {
			popedomUser = popedomUserIds.split(","); // 通知对象
			String ms = "";
			String names = "";
			for (String user : popedomUser) {
				if(user==null || user.length()==0) continue;
				String mobile = "";
				String[] telPhone = (String[]) pubMgt.selTelByUserId(user).getRetVal();// 获取用户的手机号码
				if(telPhone!=null && telPhone.length>0 && telPhone[0]!=null && telPhone[0].length()>0){
					mobile = telPhone[0];
				}else{
					String[] empArray = (String[])pubMgt.selTelEmailByUserId(user).getRetVal();
					mobile = empArray[1];
				}
				if(mobile!=null && mobile.length() == 11){
					ms += mobile +",";
					names += GlobalsTool.getEmpNameById(user) +",";
				}
			}
			if (BaseEnv.telecomCenter!=null && ms != null && !"".equals(ms)) {
				ms = ms.substring(0,ms.length() -1);
				BaseEnv.telecomCenter.send(title, ms.split(","), userId);				
			}
		}
		if(otherSMS!=null && otherSMS.length()>0){
			String[] otherSMSs = otherSMS.split(":") ;
			String ms = "";
			for(String mobile : otherSMSs){				
				if (mobile != null && !"".equals(mobile) && mobile.length() == 11) {
					ms += mobile +",";							
				}
			}
			if (BaseEnv.telecomCenter!=null && ms != null && !"".equals(ms)) {
				ms = ms.substring(0,ms.length() -1);
				BaseEnv.telecomCenter.send(title, ms.split(","), userId);				
			}
		}
	}


	/**
	 * 启动线程
	 */
	public void run() {
		sendNotify(userId, title, content, popedomUserIds, wakeType, isOA,keyId,otherEmail,otherSMS,type);
	}

	/**
	 * 去掉连接地址<a href="/userfunction.do">aaaa</a>-->aaaaa
	 * 
	 * @param str
	 * @return
	 */
	public String replaceA(String str) {
		if(str==null || str.length()==0){
			return "" ;
		}
		Pattern pattern = Pattern
				.compile("<a[/\":()\\?@&,'\\u4e00-\\u9fa5.=\\s\\w]+>");
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			str = str.replace(matcher.group(), "");
		}
		return str.replace("</a>", "");
	}
}
