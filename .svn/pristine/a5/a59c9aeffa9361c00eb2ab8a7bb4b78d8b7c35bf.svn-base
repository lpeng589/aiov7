package com.koron.oa.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.mobile.bean.OpenDialogVo;
import com.koron.oa.bean.MessageBean;
import com.koron.oa.bean.MessageBean2;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.msgcenter.msgif.CancelMsgReq;
import com.menyi.msgcenter.msgif.FileReq;
import com.menyi.msgcenter.msgif.FriendItem;
import com.menyi.msgcenter.msgif.GroupItem;
import com.menyi.msgcenter.msgif.MsgReq;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.msgcenter.server.MSGSession;
import com.menyi.msgcenter.server.MsgMgt;
import com.menyi.web.util.*;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.*;
import sun.misc.BASE64Decoder;

/**
 * 
 * <p>Title:即时消息</p> 
 * <p>Description: </p>
 *
 * @Date:2012-3-26
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class MessageAction extends MgtBaseAction {
	private final static String DES = "DES"; 
    MessageMgt mgt = new MessageMgt();
    MsgMgt msgMgt=new MsgMgt();
    private static Gson gson ;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}
    /**
	 * exe 控制器入口函数
	 * 
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 * @todo Implement this com.huawei.sms.web.util.BaseAction method
	 */
    protected ActionForward exe(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

        // 跟据不同操作类型分配给不同函数处理
        int operation = getOperation(request);
        String type = getParameter("type", request) ;
        ActionForward forward = null;
        switch (operation) {
        case OperationConst.OP_SEND_PREPARE:
        	
        	String dialogType = request.getParameter("dialogType");
        	System.out.println("MessageAction********** dialogType = "+dialogType);
        	if (StringUtils.equals(dialogType,"dialogType")){
        		 forward = openDialog(mapping, form, request, response);
        	} else {
        		 forward = sendPrepare(mapping, form, request, response);
        	}
            break;
        case OperationConst.OP_SEND:
            forward = send(mapping, form, request, response);
            break;
        case OperationConst.OP_DELETE:
            forward = delete(mapping, form, request, response);
            break;
        case OperationConst.OP_RECEIVE:
        	forward = receive(mapping, form, request, response) ;
        	break;
        case OperationConst.OP_UPDATE:
        	forward = receiveAffaix(mapping, form, request, response) ;
        	break;
        case OperationConst.OP_QUERY:
        	if("history".equals(type)){
        		forward = history(mapping, form, request, response) ;
        	}else{
        		forward = query(mapping, form, request, response);
        	}
            break;
       
         
        default: 
        	forward = query(mapping, form, request, response);
        }
        return forward;
    }

    

	/**
	 * 打开聊天窗口 
	 * 
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
    protected ActionForward sendPrepare(ActionMapping mapping, ActionForm form,
              HttpServletRequest request, HttpServletResponse response) throws  Exception {
    	
    	String sendType = getParameter("sendType", request) ;/*聊天类型 person=个人，dept=部门，group=分组*/
    	String sendId = getParameter("sendId", request) ;	
    	if("dept".equals(sendType)){
    		Result result = mgt.selectDeptByClassCode(sendId) ;
    		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
    			request.setAttribute("empList", result.retVal) ;
    		}
    	}else if("group".equals(sendType)){
    		Result result = mgt.queryGroupEmp(sendId) ;
    		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
    			request.setAttribute("empList", result.retVal) ;
    		}
    	}else{
    		Result result = new UserMgt().detail(sendId) ;
    		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
    			EmployeeBean employee = (EmployeeBean) result.retVal ;
    			request.setAttribute("user", employee) ;
    			request.setAttribute("deptName", OnlineUserInfo.getUser(employee.getId()).departmentName) ;
    			String myPhoto = GlobalsTool.checkEmployeePhoto("140",employee.getId());
				request.setAttribute("myPhoto", myPhoto) ;
    		}
    	}
    	request.setAttribute("sendType", sendType) ;
    	request.setAttribute("sendId", sendId) ;
    	return getForward(request, mapping, "msgSend");
    }
    
    /**
	 * 接收消息
	 * 
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
    protected ActionForward receive(ActionMapping mapping, ActionForm form,
              HttpServletRequest request, HttpServletResponse response) throws  Exception {
    	
    	String receiveType = getParameter("receiveType", request) ;
    	String receiveId   = getParameter("receiveId", request) ;
    	String receiveTime = getParameter("receiveTime", request) ;
    	Result result = MSGConnectCenter.receiveMsg(getLoginBean(request).getId(), receiveId);
    	if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){

        	ArrayList<MessageBean> msgList = (ArrayList<MessageBean>) result.retVal ;
            for(int i=0;i<msgList.size();i++){
        		   MessageBean message = (MessageBean) msgList.get(i) ;
        		   //更改网页端图片路径
        		   if(message.getContent().contains("<img") && message.getContent().contains("zoomImage")){
        			   String img=message.getContent();
            		   message.setContent(img.replace("onclick=\"zoomImage(this)\" src=\"", "onclick=\"zoomImage(this)\" src=\"/ReadFile.jpg?tempFile=false&type=PIC&tableName=tblEmployee&fileName="));
        		   }
        		   //替换客户端的QQ表情
        		   if(message.getContent().contains("<img") && message.getContent().contains("type=\"face\"")){
        			   String img=message.getContent();
        			   message.setContent(img.replace("type=\"face\" src=\"", "src=\"/js/plugins/emoticons/images/"));
        		   }
        		   
        		   //此处替换客户端传来的包含图片的信息
        		   if(message.getContent().contains("<img") && message.getContent().contains("type=\"img\"")){
            		   String img=message.getContent();
            		   message.setContent(img.replace("type=\"img\" src=\"", "style=\"cursor: pointer;\" onclick=\"zoomImage(this)\" title=\"单击放大\" alt=\"单击放大\"  src=\"/ReadFile.jpg?type=PIC&tempFile=path&path=/msgPic/&fileName="));
        		   }
        	   }
    		request.setAttribute("msgList", result.retVal) ;
    	}
    	return getForward(request, mapping, "msgReceive") ;
    }
    
    /**
	 * 接收附件
	 * 
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
    protected ActionForward receiveAffaix(ActionMapping mapping, ActionForm form,
              HttpServletRequest request, HttpServletResponse response) throws  Exception {
    	
    	String msgId = getParameter("msgId", request) ;
    	if(msgId!=null && msgId.length()>0){
    		Result result = mgt.loadMsg(msgId) ;
    		if(result.retCode != ErrorCanst.DEFAULT_SUCCESS){
    			request.setAttribute("msg", "NO") ;
    		}
    	}
    	
    	return getForward(request, mapping, "blank") ;
    }
    

    /**
	 * 发送消息
	 * 
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request  HttpServletRequest
	 * @param response  HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
    protected ActionForward send(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

		LoginBean loginBean = getLoginBean(request);
		String receiveId = getParameter("receiveId", request);
		String sendType = getParameter("sendType", request);
		String content = getParameter("content", request);
		if(content != null){
			content = URLDecoder.decode(content,"UTF-8");
		}
		String affix = getParameter("affix", request);
		String picture =getParameter("picture", request);

		if (affix != null) {
			if (!"person".equals(sendType)) {
				request.setAttribute("msg", "NO");
				return getForward(request, mapping, "blank");
			}
			affix = GlobalsTool.toChinseChar(affix);

			String[] affs = affix.split(";");
			for (String aff : affs) {
				MSGConnectCenter.sendMsg(loginBean.getId(), receiveId, aff, "file");
			}
			return getForward(request, mapping, "blank");
		}
		// 网页端发送的是图片消息 yy
		if (picture != null) {
			if (!"person".equals(sendType)) {
				request.setAttribute("msg", "NO");
				return getForward(request, mapping, "blank");
			}			
			//String pictureUrl = "/ReadFile.jpg?tempFile=false&type=PIC&tableName=tblEmployee&fileName="+picture;
			//content = "<img type=\"img\" src=\""+picture+"\" />"; 	
			content = "<img style=\"cursor: pointer;\" height=\"100\" onclick=\"zoomImage(this)\" src=\""+picture+"\" width=\"200\" />";
		}
		
		if (content != null) {
			//content = GlobalsTool.toChinseChar(content).replaceAll("/upload/", "");
			//content = GlobalsTool.toChinseChar(content);
			content = GlobalsTool.replaceHTML2(content);
			// 网页端发送的是图片消息
			if (content.contains("<img") && content.contains("src")) {
				/*String data[] = content.split("src=\"");
				for (String d : data) {					 
					FileInputStream fis = null;
					FileOutputStream fos = null;
					try {
						String url = this.getClass().getResource("/").getPath();
						String imgName = d.substring(0, d.indexOf("\""));
							url = url.substring(1, url.indexOf("website"));
							File fileIn = new File(url + "website/upload/"
									+ imgName);
							if (!fileIn.exists()) {
								continue;
							}
							fis = new FileInputStream(fileIn);
//						}

						File fileOut = new File(BaseEnv.FILESERVERPATH + "/pic/tblEmployee/" + imgName);
						if (!fileOut.getParentFile().exists()) {
							fileOut.getParentFile().mkdirs();
						}
						byte[] buffer = new byte[102400];
						fos = new FileOutputStream(fileOut);
						int num = -1;
//						while (null != is && (num = is.read(buffer)) != -1) {
//							fos.write(buffer, 0, num);
//						}
						while (null != fis && (num = fis.read(buffer)) != -1) {
							fos.write(buffer, 0, num);
						}
					} catch (Exception e) {
						try {
//							if (is != null)
//								is.close();
							if (fis != null) {
								fis.close();
							}
							if (fos != null) {
								fos.close();
							}
						} catch (Exception ee) {
						}
					}
				}*/
			}
			// 网页端发送的是QQ表情
			if (content.contains("<img") && content.contains("src")
					&& content.contains("/js/plugins/emoticons/images")) {
				content = content.replace("src=\"/js/plugins/emoticons/images/",
						"type=\"face\" src=\"");
			}
		}

		MSGConnectCenter.sendMsg(loginBean.getId(), receiveId, content,
				sendType);

		return getForward(request, mapping, "blank");
	}


    /**
	 * 删除
	 * 
	 * @param mapping  ActionMapping
	 * @param form ActionForm
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
    protected ActionForward delete(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        String receive = getParameter("receive", request) ;
        String operType = getParameter("operType", request) ;
        MSGConnectCenter.delMsg(getLoginBean(request).getId(), receive);
        return history(mapping, form, request, response); 
    }

    /**
	 * 查询
	 * 
	 * @param mapping  ActionMapping
	 * @param form  ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
    protected ActionForward query(ActionMapping mapping, ActionForm form,
              HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String firstLoad = getParameter("firstLoad", request) ;
    	if(firstLoad!=null){
    		firstLoad = GlobalsTool.toChinseChar(firstLoad) ;
    		firstLoad = firstLoad.replaceAll("@", "'") ;
    	}
    	request.setAttribute("firstLoad", firstLoad) ;
    	request.setAttribute("loginBean", getLoginBean(request)) ;
    	return getForward(request, mapping, "msgList");
    }
    
    /**
	 * 查询历史记录
	 * 
	 * @param mapping  ActionMapping
	 * @param form  ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
    protected ActionForward history(ActionMapping mapping, ActionForm form,
              HttpServletRequest request,HttpServletResponse response) throws Exception {

		BaseEnv.log.debug("MessageAction-history-------方法开始");
		
    	String receive = getParameter("receive", request) ;
    	String operType = getParameter("operType", request) ;
    	String keyWord = getParameter("keyWord", request) ;
    	if(keyWord!=null && keyWord.length()>0){
    		keyWord = GlobalsTool.toChinseChar(keyWord) ;
    	}
    	OnlineUser user = OnlineUserInfo.getUser(receive) ;
    	if(user!=null){
    		request.setAttribute("receiveName", user.getName()) ;
    	}
    	if(!"person".equals(operType)){
    		request.setAttribute("receiveName", getParameter("receiveName", request)) ;
    	}
    	request.setAttribute("keyWord", keyWord) ;
    	request.setAttribute("receive", receive) ;
    	request.setAttribute("operType", operType) ;
    	int pageSize = getParameterInt("pageSize", request);
        int pageNo = getParameterInt("pageNo", request);
        if (pageNo == 0) {
        	pageNo = 1;
        }
        if (pageSize == 0) {
        	pageSize = 20;
        }
		BaseEnv.log.debug("MessageAction-history-------查询数据库开始");
        Result result = mgt.history(keyWord, pageSize, pageNo, receive, getLoginBean(request).getId()) ;
		BaseEnv.log.debug("MessageAction-history-------查询数据库结束");
        if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
        	ArrayList<MessageBean2> msgList = (ArrayList<MessageBean2>) result.retVal ;
//        	Collections.sort(msgList, new SortMessageField()) ;	// 数据库查询时已经排过
        	
           for(int i=0;i<msgList.size();i++){
       		   MessageBean2 message = (MessageBean2) msgList.get(i) ;
       		   //更改网页端图片路径
       		   if(message.getContent().contains("<img") && message.getContent().contains("zoomImage")){
       			   String img=message.getContent();
           		   message.setContent(img.replace("onclick=\"zoomImage(this)\" src=\"", "onclick=\"zoomImage(this)\" src=\"/ReadFile.jpg?tempFile=false&type=PIC&tableName=tblEmployee&fileName="));
       		   }
       		   //替换客户端的QQ表情
       		   if(message.getContent().contains("<img") && message.getContent().contains("type=\"face\"")){
       			   String img=message.getContent();
       			   message.setContent(img.replace("type=\"face\" src=\"", "src=\"/js/plugins/emoticons/images/"));
       		   }
       		   
       		   //此处替换客户端传来的包含图片的信息
       		   if(message.getContent().contains("<img") && message.getContent().contains("type=\"img\"")){
           		   String img=message.getContent();
           		   message.setContent(img.replace("type=\"img\" src=\"", "style=\"cursor: pointer;\" onclick=\"zoomImage(this)\" title=\"单击放大\" alt=\"单击放大\"  src=\"/ReadFile.jpg?type=PIC&tempFile=path&path=/msgPic/&fileName="));
       		   }
       	   }
        	
        	request.setAttribute("msgList", msgList) ;
        	request.setAttribute("pageBar", pageBar(result, request)) ;
        }

		BaseEnv.log.debug("MessageAction-history-------方法结束");
    	return getForward(request, mapping, "msgHistory");
    }
    
    /**
	 * MessageBean字段按createTime排序
	 */
	public class SortMessageField implements Comparator{
		public int compare(Object o1, Object o2) {
			MessageBean2 field1 = (MessageBean2) o1 ;
			MessageBean2 field2 = (MessageBean2) o2 ;
			if(field1.getCreateTime()==null || field2.getCreateTime()==null){
				return 0 ;
			}
			if(field1.getCreateTime().compareTo(field2.getCreateTime())>0){
				return 1 ;
			}else{
				return 0;
			}
		}
	}

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		LoginBean loginBean = getLoginBean(req) ;
        if (loginBean == null) {
            BaseEnv.log.debug("MgtBaseAction.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage");
        }
		return null;
	}
	/**
	 * 从web段调用即时消息接口
	 *  需要对数据进行封装 加密等处理
	 */
	public ActionForward openDialog(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response){
		try {
			String uid = request.getParameter("uid");// 对话方
			Object o = request.getSession().getAttribute("LoginBean");
			LoginBean loginBean = new LoginBean();
			System.out.println("openDialog********* uid = "+uid+"***"+o);
			if (o != null) {
				loginBean = (LoginBean) o;
			}
			

			String sendName = loginBean.getName();// 当前登录人 也就是发信人 登录名
			//System.out.println("openDialog********* sendName = "+sendName);
			Result resultSend = new UserMgt().queryEmployeeBySysName(sendName);
			EmployeeBean beanSend = new EmployeeBean();
			if (resultSend.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				beanSend = ((ArrayList<EmployeeBean>) resultSend.getRetVal()).get(0);
			}
			EmployeeBean bean = ((ArrayList<EmployeeBean>) new UserMgt().queryEmp(uid).getRetVal()).get(0);
			String pwd = URLEncoder.encode(CommonUtil.encryptByBASE64(beanSend.getPassword()), "utf-8");
			uid = URLEncoder.encode(CommonUtil.encryptByBASE64(bean.getSysName()), "utf-8");
			sendName = URLEncoder.encode(CommonUtil.encryptByBASE64(sendName),"utf-8");
			// uid = URLEncoder.encode(getBASE64(uid));
			// sendName = URLEncoder.encode(getBASE64(sendName));
			//System.out.println("openDialog********* pwd = "+pwd+"****sendName"+sendName);
			OpenDialogVo vo = new OpenDialogVo();
			vo.setMyid(sendName);
			vo.setMypwd(pwd);
			vo.setSuccess(1);
			vo.setUid(uid);
			String json = gson.toJson(vo);
			request.setAttribute("msg", json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getForward(request, mapping, "blank");
	}
	// 将 s 进行 BASE64 编码
	public static String getBASE64(String str) {
		if (str == null) return null;
			return (new sun.misc.BASE64Encoder()).encode( str.getBytes() 
		);
	}
	//将 BASE64 编码的字符串 s 进行解码
	public static String getFromBASE64(String str) {
		if (str == null) return null;
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				byte[] b = decoder.decodeBuffer(str);
				return new String(b);
			} catch (Exception e) {
				return null;
		}
	}
	
	
	/** 
	* 加密 
	* @param src 数据源 
	* @param key 密钥，长度必须是8的倍数 
	* @return 返回加密后的数据 
	* @throws Exception 
	*/ 
	
	public static byte[] encrypt(byte[] src, byte[] key)throws Exception { 
			// DES算法要求有一个可信任的随机数源
			SecureRandom sr = new SecureRandom(); 
			// 从原始密匙数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key); 
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES); 
			SecretKey securekey = keyFactory.generateSecret(dks); 
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance(DES); 
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, sr); 
			// 现在，获取数据并加密
			// 正式执行加密操作
			return cipher.doFinal(src); 
	} 
	/** 
	* 密码加密 
	* @param password mj
	* @return 
	* @throws Exception 
	*/ 
	public String encrypt(String password){ 
		try { 
			String password_crypt_key  = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
			return byte2hex(encrypt(password.getBytes(),password_crypt_key.getBytes())); 
		}catch(Exception e) { 
		} 
		return null; 
	} 
	/**
	 * 转换成十六进制字符串
	 * mj
	 */
    public String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
            if (n < b.length - 1)
                hs = hs + "";
        }
        return hs.toUpperCase();
    }

    
}
