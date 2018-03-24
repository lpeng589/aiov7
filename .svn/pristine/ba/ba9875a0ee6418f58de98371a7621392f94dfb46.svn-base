package com.menyi.web.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.koron.wechat.common.message.SendTextBean;
import com.koron.wechat.common.util.WXSetting;
import com.koron.wechat.common.util.WXSettingBase;
import com.tencent.xinge.XinGeDeal;
import com.tencent.xinge.XinGeIOSDeal;


/**
 * 发送推送
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Feb 24, 2012
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class AppleApnsSend extends Thread{
	private static String key;
	private String dogId ;
	private String token ;
	private String content ;
	private String url ;
	private String serverKey ;
	
	
	public AppleApnsSend(String dogId,String token,String content,String url,String serverKey){
		this.dogId = dogId ;
		this.token = token ;
		this.content = content ;
		this.url = url ;
		this.serverKey = serverKey ;
	}

	@SuppressWarnings("unchecked")
	public void run() {
	/*	try {
			ServerConnection conn = new ServerConnection(
		           "http://aio.koronsoft.com:8090/appleServlet", new byte[] {0x21, 0x12,
		            0xF, 0x58, (byte) 0x88, 0x10, 0x40, 0x38
		           , 0x28, 0x25, 0x79, 0x51, (byte) 0xC1, (byte) 0xDD, 0x55, 0x66
		           , 0x77, 0x29, 0x74, (byte) 0x28, 0x30, 0x40, 0x37, (byte) 0xE2});

			String posStr = "<str1>" + dogId + "</str1>" 
					+ "<str2>" + token + "</str2>" 
					+ "<str3>" + content + "</str3>" 
					+ "<str4>" + url + "</str4>" 
					+ "<str5>" + serverKey + "</str5>";
		       conn.send(posStr);
		} catch (Exception ex1) {
//			ex1.printStackTrace() ;
			BaseEnv.log.error("AppleApnsSend error:", ex1) ;
		}*/
		//信鸽推送
		try{
			Map<String,String> map =new Gson().fromJson(url, Map.class);
			String receiverid =map.get("userId");
			//	Random random = new Random();
			//  String id = Integer.toHexString(random.nextInt());
			
			 // 推送到微信
			 sendWXMsg(receiverid,content,url);
			
			//推送到手机上的消息要截取前40个字附
		    if(content!=null&&content.length()>40)
				content=content.substring(0,40);
			JsonObject json =new JsonObject();
			//json.addProperty("id",id );
			//json.addProperty("apiKey","1234567890");
			//json.addProperty("title",content );
			json.addProperty("content",content );
			json.addProperty("uri",url ); 
			//安卓手机信鸽
			System.out.println("android:"+XinGeDeal.pushMsgSingle(dogId+"_"+receiverid, json.toString()));
			//安卓平板信鸽
			System.out.println("androidpad:"+XinGeDeal.pushMsgSingle("androidpad_"+dogId+"_"+receiverid, json.toString()));
			//System.out.println("androidpad:"+XinGeDeal.pushNoticeSingle("androidpad_"+dogId+"_"+receiverid,content));
			//ios测试
			System.out.println("ios:"+XinGeIOSDeal.pushMsgSingleDevelop(dogId+"_"+receiverid,content));
			//ios正式
			System.out.println("ios正式:"+XinGeIOSDeal.pushMsgSingleProduct(dogId+"_"+receiverid,content));
    
		}catch(Exception e){ 
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 推送消息到微信
	 * @param touser
	 * @param content
	 * @param url
	 */
	@SuppressWarnings("unchecked")
	public void sendWXMsg(String touser,String content,String url) {
		String[] keyNames = {WXSetting.AGENTKEYNAME_WXQY, WXSetting.AGENTKEYNAME_WXWORK, WXSetting.AGENTKEYNAME_WX};
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		HashMap<String, String> urlMap = gson.fromJson(url, HashMap.class);
		String[] contents = urlMap.get("content").split("&");
		String tableName = "";
		String keyId = "";
		boolean isWorkflow = contents[0].indexOf("UserFunctionAction.do?") >= 0;
		if(isWorkflow) {//审核消息
			for (String string : contents) {
				if(string.startsWith("tableName")){
					tableName = string.substring(10);
				}
				else if(string.startsWith("keyId")){
					keyId = string.substring(6);
				}
			}
		}
		boolean isMobilevue = contents[0].indexOf("mobilevue") == 0; 
		WXSetting wxsetting = WXSetting.getInstance();
		for (String keyName : keyNames) {
			WXSettingBase settingBase = wxsetting.getSettingBase(keyName);
			if (settingBase == null || !settingBase.getFlag())
				continue;
			SendTextBean wxWorkSendTextBean = new SendTextBean(keyName);
			if (keyName.equals(WXSetting.AGENTKEYNAME_WX)) { // 微信
				String openid = getOpenid(touser);
				if (openid == null || openid.trim().equals(""))
					continue;
				wxWorkSendTextBean.setTouser(openid);
			}
			else {
				wxWorkSendTextBean.setTouser(touser);
			}
			if (isWorkflow) {
				String remoteUrl= settingBase.getRemoteurl() + "mobilevue/workflow/approvedetredirect/" + tableName + "/" + keyId + "?opType=detail&state=" + settingBase.getState();
				String contentText = content.replaceAll(" ", "") + "<a href='" + remoteUrl + "'>点击查看</a>";
				wxWorkSendTextBean.setContent(contentText);
			} else if (isMobilevue) {
				String remoteUrl;
				if (contents[0].indexOf("?") > 0) {
					remoteUrl = settingBase.getRemoteurl() + contents[0] + "&state=" + settingBase.getState();
				} else {
					remoteUrl = settingBase.getRemoteurl() + contents[0] + "?state=" + settingBase.getState();
				}
				String contentText = content.replaceAll(" ", "") + "<a href='" + remoteUrl + "'>点击查看</a>";
				wxWorkSendTextBean.setContent(contentText);
			}
			else {
				wxWorkSendTextBean.setContent(content.replaceAll(" ", ""));
			}
			com.koron.wechat.common.util.BaseResultBean result = wxWorkSendTextBean.send();
			BaseEnv.log.debug("发送消息到微信：" + keyName + result.getErrcode()+" "+result.getErrmsg()+"   content:"+wxWorkSendTextBean.getContent());
		}
	}
	
	/**
	 * 根据userid查询微信openid
	 * @param userid
	 * @return
	 */
	private static String getOpenid(final String userid) {
		final Result rs = new Result();
		rs.retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection conn) throws SQLException {
							try {
								PreparedStatement ps = conn.prepareStatement("select openid from tblEmployee where id = ?");
								ps.setString(1, userid);
								ResultSet rset = ps.executeQuery();
								if (rset.next()) {
									rs.setRetVal(rset.getString("openid"));
								}
							} catch (Exception ex) {
								System.out.println("初始化微信出错");
								return;
							}
						}
					});
					return ErrorCanst.DEFAULT_SUCCESS;
				}
			});
		return rs.retVal != null ? ((String) rs.getRetVal()) : null;
	}

}
