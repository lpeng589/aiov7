package com.koron.crm.weixin.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.koron.crm.weixin.bean.ClientRequestBean;
import com.koron.crm.weixin.bean.ClientResponeBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;

/**
 * 
 * <p>
 * Title:我的项目
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2013/12/30
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 徐杰俊
 */
public class ClientAction extends BaseAction {
	private static Gson gson = new GsonBuilder().setDateFormat(
			"yyyy-MM-DD hh:mm:ss").create();
	ClientMgt mgt = new ClientMgt();
	ClientRequestBean reqBean = null;
	ClientResponeBean responeBean = new ClientResponeBean();
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 跟据不同操作类型分配给不同函数处理
		ActionForward forward = null;
		
		//获取requestBean
		reqBean = this.getRequestBeanByRequest(request);
		String type = getParameter("type",request);//sendMessage表示发送微信信息
		
		if("sendMessage".equals(type)){
			forward = sendMessage(mapping, form, request, response);//发送微信信息
		}else{
			if(checkRequest(reqBean)){
				if("addFollowUp".equals(reqBean.getType())){
					forward = addFollowUp(mapping, form, request, response);//添加联系记录
				}else if("bindClient".equals(reqBean.getType())){
					forward = bindClient(mapping, form, request, response);// 绑定客户
				}else{
					forward = clientQuery(mapping, form, request, response);// 客户查询
				}
			}else{
				request.setAttribute("msg", gson.toJson(responeBean));
				return getForward(request, mapping, "blank");
			}
		}
		
		return forward;
	}

	/**
	 * 没有reqBean方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private boolean checkRequest(ClientRequestBean reqBean){
		boolean status = true;
		if(reqBean==null){
			responeBean.setMessage("获取不到ClientRequestBean");
			responeBean.setMessageCode(1);
			status = false;
		}else{
			String weixinToken = GlobalsTool.getSysSetting("weixinToken");//获取系统配置微信token号
			if(!weixinToken.equals(reqBean.getTokenid())){
				responeBean.setMessage("与AIO系统配置的tokenId匹配不上");
				responeBean.setMessageCode(1);
				status = false;
			}
		}
		
		return status;
	}
	
	/**
	 * 客户查询
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward clientQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Result rs = mgt.clientQuery(reqBean.getClientKeyWord());
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			ArrayList<String> clientInfos = (ArrayList<String>) rs.retVal;
			responeBean.setClientInfos(clientInfos);
			responeBean.setMessage("success");
			responeBean.setMessageCode(0);
		}else{
			responeBean.setMessage("查询客户失败");
			responeBean.setMessageCode(1);
		}
		request.setAttribute("msg", gson.toJson(responeBean));
		return getForward(request, mapping, "blank");
	}

	/**
	 * 添加联系记录
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addFollowUp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		if (reqBean == null) {
			responeBean.setMessage("error");
			responeBean.setMessageCode(1);
		}else{
			String clientId = reqBean.getClientId();
			if ("0".equals(reqBean.getFollowUpOpertaion())) {
				//查询联系记录
				Result rs = mgt.followUpQuery(clientId);
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					ArrayList<String> followUpContList = (ArrayList<String>) rs.retVal;
					responeBean.setFollowUpContList(followUpContList);
					responeBean.setMessage("success");
					responeBean.setMessageCode(0);
				}
			}else{
				//新增联系记录
				Boolean isExistClient = mgt.isExistClient(clientId);
				Result rs = mgt.addFollowUp(clientId, reqBean
						.getClientName(), isExistClient, reqBean
						.getFollowUpContents(),"1");
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					responeBean.setMessage("success");
					responeBean.setMessageCode(0);
				}else{
					responeBean.setMessage("添加联系记录失败");
					responeBean.setMessageCode(1);
				}
			}
			request.setAttribute("msg", gson.toJson(responeBean));
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * 发送信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward sendMessage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String weixinBgUrl = GlobalsTool.getSysSetting("weixinBgUrl");//获取系统配置后台网址
		String[] keyIds = getParameters("keyId", request);
		String weixinContents = getParameter("weixinContents", request);
		ClientRequestBean reqBean = new ClientRequestBean();
		reqBean.setClientIds(keyIds);
		reqBean.setClientContents(weixinContents);
		reqBean.setAsync(0);
		reqBean.setType("sendMessage");

		ClientResponeBean bean = sendObject(weixinBgUrl, reqBean,ClientResponeBean.class);
		//成功
		if(bean!=null && bean.getMessageCode()==0){
			EchoMessage.success().add(getMessage(request, "common.note.sendsuccess")).setBackUrl("/CRMClientAction.do").setAlertRequest(request);
		}else{
			EchoMessage.error().add(getMessage(request, "common.note.senderror")).setBackUrl("/CRMClientAction.do").setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}

	/**
	 * 绑定
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward bindClient(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Boolean isExistClient = mgt.isExistClient(reqBean.getClientId());
		if (isExistClient) {
			Result rs = mgt.bindClient(reqBean.getOpenid(), reqBean
					.getClientId());
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				responeBean.setMessage("success");
				responeBean.setMessageCode(0);
			}else{
				responeBean.setMessage("绑定失败");
				responeBean.setMessageCode(0);
			}
		}else{
			responeBean.setMessage("绑定的客户不存在");
			responeBean.setMessageCode(1);
		}
		request.setAttribute("msg", gson.toJson(responeBean));
		return getForward(request, mapping, "blank");
	}

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		return null;
	}

	/**
	 * 获取clientRequsetBean
	 * 
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public ClientRequestBean getRequestBeanByRequest(HttpServletRequest request)
			throws UnsupportedEncodingException, IOException {
		Reader r = new InputStreamReader(request.getInputStream(), "utf-8");
		Gson g = new Gson();
		ClientRequestBean bean = g.fromJson(r, ClientRequestBean.class);
		r.close();
		return bean;
	}

	@SuppressWarnings("unchecked")
	public static <T> T sendObject(String url, Object obj, Class<T> t) {
		try {
			URL u = new URL(url+"/clientdeal.html");
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("content-type", "text/html");
			OutputStream ous = conn.getOutputStream();
			ous.write(new Gson().toJson(obj).getBytes("utf-8"));
			ous.flush();
			ous.close();
			Reader r = new InputStreamReader(conn.getInputStream(), "utf-8");
			T ret = (T) new Gson().fromJson(r, t);
			r.close();
			conn.disconnect();
			return ret;
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
