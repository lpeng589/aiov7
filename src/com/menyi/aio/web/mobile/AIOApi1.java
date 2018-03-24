package com.menyi.aio.web.mobile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts.action.ActionForward;
import org.apache.struts.util.MessageResources;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.context.ViewContext;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.crm.bean.ClientModuleBean;
import com.koron.crm.setting.ClientSetingMgt;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.OAMyWorkFlowDetBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.koron.oa.workflow.OAMyWorkFlowForm;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.koron.wechat.common.media.Media;
import com.koron.wechat.common.util.BaseResultBean;
import com.koron.wechat.common.util.WXSetting;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.ColConfigBean;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.RoleBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.GlobalMgt;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.favourstyle.MessageBean;
import com.menyi.aio.web.logManage.LogManageMgt;
import com.menyi.aio.web.login.LoginAction;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.report.TableListResult;
import com.menyi.aio.web.role.RoleMgt;
import com.menyi.aio.web.sysAcc.SysAccMgt;
import com.menyi.aio.web.userFunction.DynAjaxSelect;
import com.menyi.aio.web.userFunction.ReportData;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.aio.web.wxqy.WeixinApiMgt;
import com.menyi.msgcenter.msgif.EmployeeItem;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.AIOConnect;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.DESPlus;
import com.menyi.web.util.DefineReportBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.ErrorMessage;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.PublicMgt;
import com.menyi.web.util.SystemState;
/**
 * �����п�������ɷ����޹�˾�Զ���ƽ̨�ӿ�
 * 
 * @version 1.0<br/>
 * ������ʷ��2016-03-23<br/>
 * �޸���ʷ��<br/>
 * 
 * @author ������<br/><br/><br/>
 * �Զ��幦�ܽӿ�<br/>
 * ���ӿ����з�������json���󣬽������ajax���á�<br/>
 * ���ӿ�ͬ������C/S������ֻ�APP����JAVA HttpConnect���ã�����ע������ڲ��ӿ�ǰӦʡ�ȵ��õ�½�ӿ�<br/>
 * �����C/S������ýӿڣ�Ӧ�ȵ��õ�½�ӿڣ��������ӿڷ���ͷ�ļ��е�Cookieȡ�ñ�ʶJSESSIONID,���ڵ��������ӿ�ǰ��JSESSIONIDֵͨ��Cookie��URL��ͬ�ӿ�Ҫ�����һ���͸�������
 * <br/>
 * ���ӿڵ��õ�ַΪhttp://���IP������/AIOApi?op=���� ��http://���IP������/MobileAjax?op=����(�ϰ汾��ַ)<br/>
 * <br/>
 * ��:<br/>
 * jQuery.post("/MobileAjax?op=LOGIN",<br/>
 * &nbsp;&nbsp;      { name: loginName, password: password },<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;      function(data){<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;        if(data.code != "OK"){<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;         	alert(data.msg);//ʧ��ԭ��<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;         }else{<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;         //�ɹ�<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;         } <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;      },<br/>
 * &nbsp;&nbsp;      "json" <br/>
 *	);<br/>
 * Ӧ�ó���һ����ӵ��ݣ���<br/><br/>
 * 1������addPrepare.��ʼ���������ݣ���Ӧaio�����Ӱ�Ť��������ӽ��棬�ӿڻ᷵��Ĭ��ֵ����Ϣ<br/>
 * 2������add,�������ݵ����ݱ���Ӧaio������水Ť<br/>
 * 3������deliverToPrepare��ȡ���ݵ��������Ϣ����Ӧaio��������˽��棨
 * �ӿڷ�����һ��˽ڵ㣬��һ�������Ϣ���������δ�������������ӿڿ��Բ�ִ�У����ӿ�ȡ����Ϣ����Ե����������û�ѡ��ת����Ϣ��Ҳ����ֱ�ӵ���deliver�ӿڣ�<br/>
 * 4������deliver��ִ����˳��򡣶�Ӧaio��˽���ı��水Ť��<br/><br/>
 * Ӧ�ó��������޸ĵ��ݣ���<br/><br/>
 * 1������detail ȡ�������ݣ���Ӧaio����޸ģ������޸Ľ��棬�ӿ�ͬʱ���ر�����Ȩ����Ϣ���Ƿ������޸ģ���ˣ�����ˣ����أ�ͬʱ���������ֶ�ֻ�������ص���Ϣ<br/>
 * 2������update,�������ݵ����ݱ���Ӧaio������水Ť<br/>
 * 3������deliverToPrepare��ȡ���ݵ��������Ϣ����Ӧaio��������˽��棨�ӿڷ�����һ��˽ڵ㣬��һ�������Ϣ��<br/>
 * 4������deliver��ִ����˳��򡣶�Ӧaio��˽���ı��水Ť��<br/><br/>
 * Ӧ�ó���������˵��ݣ���<br/><br/>
 * 1������check У�鵥�ݣ�������޸ĵ��ݣ���ʡ�Ա��ӿڣ����û�е��ñ��水Ť��һ��Ҫ�ȵ�����Ʒ��У�鵥�������Ƿ���ȷ��<br/>
 * 3������deliverToPrepare��ȡ���ݵ��������Ϣ����Ӧaio��������˽��棨�ӿڷ�����һ��˽ڵ㣬��һ�������Ϣ��<br/>
 * 4������deliver��ִ����˳��򡣶�Ӧaio��˽���ı��水Ť��<br/><br/>
 * Ӧ�ó����ģ��鱨����<br/><br/>
 * 1������report ȡ�������ݡ�<br/><br/>
 * Ӧ�ó����壨����������<br/><br/>
 * 1������popupInfo ȡ����������ʾ����Ϣ��<br/>
 * 2������popup ȡ���������ݡ�<br/><br/>
 * 
 */
public class AIOApi1 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Gson gson;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}

	private UserMgt userMgt = new UserMgt();

	private DynDBManager dbmgt = new DynDBManager();

	private UserFunctionMgt userFunMgt = new UserFunctionMgt();

	private OAMyWorkFlowMgt oamgt = new OAMyWorkFlowMgt();

	private PublicMgt pcmgt = new PublicMgt();
	
	

	
	
	@Override
	protected void service(final HttpServletRequest req, HttpServletResponse rsp) throws ServletException, IOException {
		if (GlobalsTool.application == null) {
			GlobalsTool globals = new GlobalsTool();
			ViewContext vc = new ViewContext() {
				public Object getAttribute(String arg0) {
					return null;
				}
				public HttpServletRequest getRequest() {
					return req;
				}
				public HttpServletResponse getResponse() {
					return null;
				}

				public ServletContext getServletContext() {
					return req.getSession().getServletContext();
				}
				public Context getVelocityContext() {
					return null;
				}
				public VelocityEngine getVelocityEngine() {
					return null;
				}
			};
			globals.init(vc);
		}
		//��¼�����������־ getParameterMap() getQueryString() getRequestURI()
		// ��ϵͳ����ʱ
		if (SystemState.instance.dogState == SystemState.DOG_RESTART) {
			setMsg(req, rsp, setMsg("ERROR", "ϵͳ�������������Ժ�����"));
			return;
		}
		// �ж�ϵͳ�Ƿ���ס
		if (OnlineUserInfo.lockState()) {
			setMsg(req, rsp, setMsg("ERROR", "ϵͳ�ѱ���"));
			return;
		}
		if (SystemState.instance.lastErrorCode != SystemState.NORMAL || (SystemState.instance.dogState != SystemState.DOG_FORMAL && SystemState.instance.dogState != SystemState.DOG_EVALUATE)) {
			setMsg(req, rsp, setMsg("ERROR", "ϵͳ״̬�쳣��Ƿ���Ȩ"));
			return;
		}

		String charset = req.getHeader("Charset");
		if (charset != null) {
			req.setCharacterEncoding(charset);
		}
		debugRequest(req);
		
		/**
		 * ����Ǽ������ݹ�����˵����������ǴӼ���ͨ�������ġ�ֱ��ת���ܷ�������
		 */
		String secData = req.getParameter("secData");
		if(secData != null && secData.length() > 0){
			handSecData(req, rsp);
			return;
		}
		
		
		String userId = req.getParameter("sid");
		String op = req.getParameter("op");
		if (op.equals("sendmsg")) {
			sendMsg(req);
			return;
		}
		
		BaseEnv.log.debug("AIOApi op="+op+"&sessionid="+req.getSession().getId());
		
		if (!"LOGIN".equals(op) && this.getLoginBean(req) == null) {
			setMsg(req, rsp, setMsg("NOLOGIN", "δ��½"));
			return;
		}
		
		if ("LOGIN".equals(op)) { //��¼
			login(req, rsp);
		} else if ("deliverTo".equals(op)) { //���
			deliverTo(req, rsp);
		} else if ("deliverToNext".equals(op)) { //��˵���һ�����
			deliverToNext(req, rsp);
		} else if ("getHome".equals(op)) { //ȡ��ҳ������
			getHome(req, rsp);
		}  else if ("getMenu".equals(op)) { //ȡ��ǰ�û��˵�
			getMenu(req, rsp);
		} else if ("report".equals(op)) { //ȡ��ҳ������
			report(req, rsp);
		} else if ("sync".equals(op)) { //����ͬ��ͨѶ¼����,���ز������ҳ��
			sync(req, rsp);
		} else if ("execDefine".equals(op)) {
			execDefine(req, rsp);
		} else if ("getEnum".equals(op)) {
			getEnum(req, rsp);
		} else if ("add".equals(op)) {
			add(req, rsp);
		} else if ("addPrepare".equals(op)) {
			addPrepare(req, rsp);
		} else if ("detail".equals(op)) {
			detail(req, rsp);
		} else if("popup_m".equals(op)){
			//*****�����ƶ��˵�������******//
			popup_m(req,rsp);			
		} else if ("popup".equals(op)) {
			//*****���õ��������ݻ�ȡ�ӿ�*****//			
			popup(req, rsp);
		} else if ("popupInfo".equals(op)) {
			popupInfo(req, rsp);
		} else if ("update".equals(op)) {
			update(req, rsp);
		} else if ("deliverToPrepare".equals(op)) {
			deliverToPrepare(req, rsp);
		} else if ("check".equals(op)) { //�Ե�����ִ��һ���޸ı��涯����Ŀ��ִ��defineУ��
			check(req, rsp);
		} else if ("hurryTrans".equals(op)) { //�Ե�����ִ��һ���޸ı��涯����Ŀ��ִ��defineУ��
			hurryTrans(req, rsp);
		} else if ("cancelTo".equals(op)) { //����
			cancelTo(req, rsp);
		} else if ("retCheck".equals(op)) { //�����
			retCheck(req, rsp);
		}  else if ("delete".equals(op)) { //�����
			delete(req, rsp);
		}  else if ("canAdd".equals(op)) { //�Ƿ������Ȩ��
			canAdd(req, rsp);
		}  else if ("myworkflow".equals(op)) { //��ѯ�ҵĹ�����
			myworkflow(req, rsp);
		}  else if("workflowDet".equals(op)){
			myworkflowDet(req,rsp);
		}  else if("cardscan".equals(op)){
			cardscan(req,rsp);
		}  else if("getJsticket".equals(op)){
			getJsticket(req,rsp);
		} else if("getSessEmp".equals(op)) {
			getSessEmp(req, rsp);
		}
	}
	
	
	/**
	 * @throws IOException 
	 * ������Ƭɨ������
	 * @param req
	 * @param rsp
	 * @throws  
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void cardscan(HttpServletRequest req, HttpServletResponse rsp) throws IOException{
		DiskFileItemFactory factory = new DiskFileItemFactory();   
        ServletFileUpload upload = new ServletFileUpload(factory);   
        upload.setHeaderEncoding("UTF-8");  
        List<FileItem> items = null;       
        try {
			items = upload.parseRequest(req);
		} catch (Exception e) {
			respMsg(req, rsp, gson.toJson(new Message("error", "����Ϣ����ʧ��")));
			return;
		}  
    	String uploadPath = BaseEnv.FILESERVERPATH+"/temp/";
    	String fileFullName =  new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.random()*10000;
    	//Map<String, Object> params = new HashMap<>();
    	Map params = new HashMap();
    	for(FileItem fileItem : items) {
	      if (fileItem.isFormField()) //΢���ϴ��ͼƬ   
	        params.put(fileItem.getFieldName(), fileItem.getString("utf-8"));   
	      else//ֱ���ϴ��ͼƬ
	       	params.put("file", fileItem);	
        }
    	// �ж��Ƿ��ϴ�ɹ�
    	Boolean uploadFileSuccess= false;
    	if(params.get("wxstate") != null && params.get("serverId") != null) { // ȥ΢����ȡͼƬȻ�󱣴�
    		fileFullName += "_"+params.get("wxstate") + ".jpeg";
    		BaseResultBean getMediaRet = new Media(params.get("wxstate").toString()).get(params.get("serverId").toString(), uploadPath + fileFullName);
    		if(getMediaRet != null && getMediaRet.getErrcode().equals("0"))
    			uploadFileSuccess = true;
    		else 
    			BaseEnv.log.debug("��΢����ȡ��Ƭɨ���ͼƬʧ��:" + gson.toJson(getMediaRet)) ;
    	} 
    	else if(params.get("file") != null) {// ֱ�ӱ���ͼƬ
    		FileItem fileItem = (FileItem)params.get("file");
    		fileFullName += fileItem.getName();
    		try {
    			fileItem.write(new File(uploadPath, fileFullName));
    			uploadFileSuccess = true;
    		}
    		catch(Exception e) {					
    			respMsg(req, rsp, gson.toJson(new Message("error", "ͼƬ��ȡʧ��")));
    			return;
    		}        	     		
    	}
    	if (!uploadFileSuccess) {
    		respMsg(req, rsp, gson.toJson(new Message("error", "�ύ�Ĳ�������")));
			return;
    	}
    	
        rsp.setHeader("Content-type", "application/json;charset=UTF-8");
        rsp.setCharacterEncoding("UTF-8");
    	final String eid = getLoginBean(req).getId();
    	try{
    		//ʹ��DES
    		final DESPlus des = new DESPlus(BaseEnv.bol88URL);
    		Map<String, String> data = new HashMap();
    		//data.put("dogid",SystemState.instance.dogId);
    		data.put("dogid","1111-1111-1111-1862");
	    	String str = des.encrypt(gson.toJson(data));
	    	// ��ѯbol88�˿ͻ��Ƿ��е���Ȩ��
	    	Message msg = AIOConnect.toURL(BaseEnv.bol88URL + "/cardscan?op=query", str);
	    	if("OK".equals(msg.getCode())){
	    		HashMap r = gson.fromJson(des.decrypt(msg.getMsg()), HashMap.class);
	    		if("success".equals(r.get("code"))){
	    			HashMap map = (HashMap)r.get("data");
	    			if((Double)map.get("BALANCE") <= 0.0){
	    				respMsg(req, rsp, gson.toJson(new Message("error", "�Ѵ�ͼƬʶ������")));
    					return;
	    			}
	    			//��ȡͼƬɨ��api�˻����벢����api������Ƭɨ����
	    			Message _r = AIOConnect.cardScan((String)map.get("mykey"),(String)map.get("secret"),uploadPath+fileFullName);
	    			final Map card = new HashMap();
	    			if("OK".equals(_r.getCode())){
	    				String cardInf = _r.getMsg();
	        			Map cardMaps = gson.fromJson(cardInf, HashMap.class);    			
	        			//��Ƭʶ����������
	        			Map ms = (Map)cardMaps.get("message");        			
	        			if((Double)ms.get("status") == 0.0){		
	        				List<HashMap> cardInfDet = (List<HashMap>)cardMaps.get("cardsinfo");
	        				if(cardInfDet.size()>0){
	        					HashMap cardMap = cardInfDet.get(0);
	        					List<HashMap> dets = (List<HashMap>)cardMap.get("items");
	        					for(HashMap item : dets){
	        						card.put(item.get("desc"),item.get("content"));
	        					}
	        					card.put("id",IDGenerater.getId());
	        				} else{
	        					rsp.getWriter().print(new Gson().toJson(new Message("error","ͼƬʶ��ʧ��")));
	        					return;
	        				}
	        			} else{
	        				rsp.getWriter().print(new Gson().toJson(new Message("error","ͼƬʶ��ʧ��")));
	        				return;
	        			}
	    			} else{
	    				rsp.getWriter().print(new Gson().toJson(new Message("error",_r.getMsg())));
	    				return;
	    			}
	    			
	    			//�����̻߳ش�ɨ������bol88
	    			new Thread(new Runnable() {
	    				public void run() {
	    					Map map = new HashMap();
	    					map.put("dogid", SystemState.instance.dogId);
	    					map.put("ComContactor",card.get("����"));
	    					map.put("ComContactorMobile",card.get("�ֻ�"));
	    					map.put("ComName",card.get("��˾"));
	    					map.put("ComTel", card.get("�绰"));
	    					map.put("ComEmail", card.get("��������"));
	    					map.put("ComAddress",card.get("��ַ"));
	    					map.put("QQ", card.get("QQ"));
	    						    				
	    					try {
								Message msg = AIOConnect.toURL(BaseEnv.bol88URL+"/cardscan?op=add", des.encrypt(gson.toJson(map)));
							} catch (Exception e) {
								
							}
	    				}
	    			}).start();
	    			//end
	    			//������Ƭ��Ϣ
	    			DBUtil.execute(new IfDB() {
	    				public int exec(Session session) {
	    					session.doWork(new Work() {
	    						public void execute(Connection connection) throws SQLException {
	    							try {
	    								Connection conn = connection;
	    								
	    								
	    									String addSQL = " insert into  tblcardscan(id,createBy,createTime,ComContactor,ComContactorMobile,ComName,ComTel,ComEmail,ComAddress,QQ) values(?,?,?,?,?,?,?,?,?,?)";
	    									PreparedStatement pstmt2 = conn.prepareStatement(addSQL);
	    									pstmt2.setString(1, (String)card.get("id"));
	    									pstmt2.setString(2, eid);
	    									pstmt2.setString(3, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	    									pstmt2.setString(4, (String)card.get("����"));
	        								pstmt2.setString(5, (String)card.get("�ֻ�"));
	        								pstmt2.setString(6, (String)card.get("��˾"));
	        								pstmt2.setString(7, (String)card.get("�绰"));
	        								pstmt2.setString(8, (String)card.get("����"));
	        								pstmt2.setString(9, (String)card.get("��ַ"));
	        								pstmt2.setString(10, (String)card.get("QQ"));
	        								int r = pstmt2.executeUpdate();
	    								
	    							} catch (Exception ex) {
	    								BaseEnv.log.error("��Ƭ��Ϣ��ϸʧ�ܣ�", ex);
	    								return ;
	    							}
	    						}
	    					});
	    					return 1;
	    				}
	    			});
	    			//end
	    			//���ؽ����ǰ��	    			
	    			Map d = new HashMap();
	    			d.put("id", card.get("id"));
	    			d.put("ComContactor", card.get("����"));
	    			d.put("ComContactorMobile", card.get("�ֻ�"));
	    			d.put("ComName", card.get("��˾"));
	    			d.put("ComTel", card.get("�绰"));
	    			d.put("ComEmail", card.get("��������"));
	    			d.put("ComAddress", card.get("��ַ"));
	    			d.put("QQ", card.get("QQ"));
	    			rsp.getWriter().print(new Gson().toJson(new Message("success","ʶ��ɹ�",d)));
	    			return;
	    			//end
	    		} else{
	    			rsp.getWriter().print(new Gson().toJson(new Message("error",String.valueOf(r.get("msg")))));
	    			return;
	    		}
	    	} else{    		
				rsp.getWriter().print(new Gson().toJson(new Message("error",msg.getMsg())));
				return;
	    	}  
    	}catch(Exception e){
    		rsp.getWriter().print(new Gson().toJson(new Message("error",e.getMessage())));
			return;
    	}
    	//end
	}
	
	/**
	 * ����������ӹ���������
	 * @param secData
	 */
	private void handSecData(HttpServletRequest req, HttpServletResponse rsp){
		String password = GlobalsTool.getSysSetting("remotePassword");
		if(password ==null || "".equals(password)){
			BaseEnv.log.debug("Զ�̵��ã�����ֹ");
			Message msg = new Message("ERROR","�Է�ϵͳ��ֹԶ�̵���");
			setMsg(req, rsp, msg);
			return;
		}
		
		String secData = req.getParameter("secData");
		
		BaseEnv.log.debug("AIOApi���ռ�����Ϣ:ʱ��"+System.currentTimeMillis());
		String data = secData;
//		String data = AIOConnect.des.Decode(password+AIOConnect.strDefaultKey, secData);
//		if (BaseEnv.log.isDebugEnabled()) {
//			BaseEnv.log.debug("AIOApi���ռ�����Ϣ���룺ʱ��"+System.currentTimeMillis()+",���ݣ�"+data);
//		}
//		//data= data.trim();
//		data = ZipUtil.unzip(data);
//		if (BaseEnv.log.isDebugEnabled()) {
//			BaseEnv.log.debug("AIOApi���ռ�����Ϣ��ѹ����ʱ��"+System.currentTimeMillis()+",����:"+data);
//		}
		if(!data.startsWith("AB:")){
			BaseEnv.log.debug("�������ݷǷ�����ʽ����ȷ���������벻һ��");
			Message msg = new Message("ERROR","�������ݲ�����ȷ���룬�������벻һ��");
			setMsg(req, rsp, msg);
			return;
		}
		data = data.substring(3); 
		HashMap map = gson.fromJson(data, HashMap.class);
		
		Object time = map.get("time");
		if(time==null||time.equals("")){
			BaseEnv.log.debug("�������ݷǷ�������ʱ�����Ϣ");
			Message msg = new Message("ERROR","���ݷǷ�");
			setMsg(req, rsp, msg);
			return;
		}
		if(System.currentTimeMillis()-Long.parseLong(time+"") > 10000){
			BaseEnv.log.debug("�������ݷǷ���ʱ�䳬��10�룬������Ҫͬ��ʱ��"+System.currentTimeMillis());
			Message msg = new Message("ERROR","����ʧЧ����ȷ���������ǺϷ��ģ���ͬ����������ʱ��");
			setMsg(req, rsp, msg);
			return;
		}
		String type=map.get("op")+"";
		if("deliverToNext".equals(type)){
			Message msg = AIOConnect.deliverToNext(map.get("locale")+"", new LoginBean(map.get("userId")+"",""), map.get("keyId")+"", map.get("tableName")+"");
			setMsg(req, rsp, msg);
			return;
		}else if("execDefine".equals(type)){
			String defineName = map.get("defineName")+"";
			String buttonTypeName = map.get("buttonTypeName")+"";
			String[] keyIds = new String[]{ map.get("keyId")+""};
			String tableName = map.get("tableName")+"";
			String defineInfo = map.get("defineInfo")+"";
			String locale = map.get("locale")+"";
			LoginBean loginBean = new LoginBean(map.get("userId")+"","");
			Message msg = AIOConnect.execDefine(locale, loginBean, defineName, buttonTypeName, keyIds, tableName, defineInfo);
			if(msg.getCode().equals("OK")){
				String msgStr = gson.toJson(msg.getObj());
				respMsg(req, rsp, msgStr);
				return;
			}else{
				setMsg(req, rsp, msg);
				return;
			}
		}else if("add".equals(type)){
			String locale = map.get("locale")+"";
			LoginBean loginBean = new LoginBean(map.get("userId")+"","");

			String tableName = map.get("tableName")+"";
			/*�����classCode*/
			String parentCode = map.get("parentCode")+"";
			//Ҫִ�е�define����Ϣ
			String defineInfo = map.get("defineInfo")+"";
			
			String saveType = map.get("saveType")+""; //�������� saveDraft �ݸ�
			
			String valuesStr = map.get("values")+"";
			
			String deliverTo = map.get("deliverTo")+"";
			Message msg = AIOConnect.add(locale, loginBean, tableName, parentCode, defineInfo, saveType, valuesStr, deliverTo);
			this.setMsg(req, rsp, msg);
			return ;
		}else if("update".equals(type)){
			String locale = map.get("locale")+"";
			LoginBean loginBean = new LoginBean(map.get("userId")+"","");

			String tableName = map.get("tableName")+"";
			/*�����classCode*/
			String parentCode = map.get("parentCode")+"";
			//Ҫִ�е�define����Ϣ
			String defineInfo = map.get("defineInfo")+"";
			
			String saveType = map.get("saveType")+""; //�������� saveDraft �ݸ�
			
			String valuesStr = map.get("values")+"";
			
			String deliverTo = map.get("deliverTo")+"";
			
			Message msg = AIOConnect.update(locale, loginBean, tableName, saveType, defineInfo, valuesStr);
			this.setMsg(req, rsp, msg);
			return;
		}else{
			Message msg = new Message("ERROR","û��op����������Ϣ"+type);
			this.setMsg(req, rsp, msg);
			return;
		}
		
	}
	
	
	
	
	/**
	 * �ҵĹ�����--��ѯ���к͵�ǰ��½����ص����й�������
	 * 
	 * @param approveStatus  transing:����consignation:ί���ҵģ�transing2�������У�finish���Ѱ��
	 * @param keyWord �ؼ���
	 * @param pageNo ҳ��
	 * <br/><br/>
	 * ����ֵ: {code:OK,obj:{totalPage:��ҳ��,list���������б�[]}}
	 */
    public void myworkflow(HttpServletRequest request, HttpServletResponse rsp) {
    	String approveStatus = request.getParameter("approveStatus");
    	String keyWord = request.getParameter("keyWord");
    	String pageNo = request.getParameter("pageNo");
    	String tempClass = request.getParameter("tempClass");
    	String tempFile = request.getParameter("tempFile");
    	
    	if(pageNo == null || pageNo.length()==0){
    		pageNo = "1";
    	}
    	OAMyWorkFlowForm workFlowForm = new OAMyWorkFlowForm();
    	workFlowForm.setApproveStatus(approveStatus==null ||approveStatus.length()==0?"transing":approveStatus);
		workFlowForm.setKeyWord(keyWord);
		workFlowForm.setPageSize(10);
		workFlowForm.setPageNo(Integer.parseInt(pageNo));
		workFlowForm.setClassCode(tempClass);
		workFlowForm.setTemplateFile(tempFile);
		Result rs = oamgt.query(workFlowForm, getLoginBean(request).getId(),
				workFlowForm.getApproveStatus(), this.getLocale(request).toString(),
				"transing".equals(workFlowForm.getApproveStatus()) ? true : false);
		HashMap ret = new HashMap();
		ret.put("totalPage", rs.getTotalPage());
		ret.put("list", rs.retVal);
		Message msg = new Message("OK","��ѯ�ɹ�", ret);
		setMsg(request, rsp, msg);
    }
    
    /**
	 * ����������
	 * ����ֵ: {code:OK,obj:{totalPage:��ҳ��,list���������б�[]}}
	 */
    public void myworkflowDet(HttpServletRequest req, HttpServletResponse rsp) {
    	String keyId = req.getParameter("keyId");
		String tableName = req.getParameter("tableName");
				
		Map ret = new HashMap();
		
		
		DBTableInfoBean tableInfo = BaseEnv.tableInfos.get(tableName);
		
		LoginBean loginBean = this.getLoginBean(req);
	
		DetailBean bean = AIOConnect.detail(GlobalsTool.getLocale(req).toString(), loginBean,keyId, tableName);
		
		if (bean.getCode() == ErrorCanst.DEFAULT_SUCCESS) {
			List<OAMyWorkFlowDetBean> flows = bean.getFlowDepict();			
			
			//ת��fieldList
			bean.setFieldList(toFieldList(bean.getFieldList(), GlobalsTool.getLocale(req).toString()));
			for(Object tn : bean.getChildShowField().keySet().toArray()){
				ArrayList list  = (ArrayList)bean.getChildShowField().get(tn);
				bean.getChildShowField().put(tn, toFieldList(list, GlobalsTool.getLocale(req).toString()));
			}
			
			/*
			OAMobileDetBean detBean = new OAMobileDetBean();
			detBean.setValues(bean.getValues());
			detBean.setChildTableList(bean.getChildTableList());
			detBean.setFlowDepict(bean.getFlowDepict());
			detBean.setFieldList(bean.getFieldList());
			detBean.setChildShowField(bean.getChildShowField());
			detBean.setCheckRight(bean.isCheckRight());
			detBean.setRetCheckRight(bean.isRetCheckRight());
			detBean.setHurryTrans(bean.isHurryTrans());
			detBean.setHasCancel(bean.isHasCancel());
			detBean.setModuleName(tableInfo.getDisplay().get(GlobalsTool.getLocale(req).toString()));
			*/	
				
			Message msg = new Message("OK","", bean);			
			setMsg(req, rsp, msg);			
			return;
		} else {
			Message msg = new Message("ERROR","�������Ϣ��ȡʧ��");
			setMsg(req, rsp, msg);
			return;		
		}
			    
    }
    
    /**
	 * �жϵ�ǰ��½���Ƿ������ĳģ���Ȩ��
	 * 
	 * @param tableName  ����
	 * @param parentTableName ������
	 * @param moduleType ģ������
	 * <br/><br/>
	 * ����ֵ: {code:OK,msg:'true:�����Ȩ�ޣ�false�������Ȩ��'}
	 */
    public void canAdd(HttpServletRequest request, HttpServletResponse rsp) {
		String tableName=request.getParameter("tableName");
		String parentTableName=request.getParameter("parentTableName");
		String moduleType=request.getParameter("moduleType");
		LoginBean loginBean  = this.getLoginBean(request);
		MOperation mop = GlobalsTool.getMOperationMap(parentTableName, tableName, moduleType, loginBean);
		
		Message msg = new Message("OK",mop.add()+"");
		setMsg(request, rsp, msg);
    }
	
    /**
	 * �������߰�
	 * 
	 * @param tableName  ����
	 * @param keyId ����ID
	 * <br/><br/>
	 * ����ֵ: {code:OK,msg:�ݰ�ɹ�}
	 */
    public void hurryTrans(HttpServletRequest request, HttpServletResponse rsp) {
    	String keyId=request.getParameter("keyId");
		String tableName=request.getParameter("tableName");
		String content=request.getParameter("content");
		String locale = getLocale(request).toString();
		LoginBean loginBean=getLoginBean(request);
		String wakeType=request.getParameter("wakeType");
		Message msg = AIOConnect.hurryTrans(locale, loginBean, keyId, tableName, content, wakeType);
		setMsg(request, rsp, msg);
    }
	
    /**
	 * ȡ�Զ��嵯�������ݣ��Զ��嵯������AIO��ƣ��ӿڵ���������PC������һ��
	 * 
	 * @param tableName  ����
	 * @param fieldName �ֶ���
	 * @param selectName �������������Բ�ָ���������ָ���������ֶ���������£����Զ�ȡ��Ӧ����ֶεĵ����������û��ָ������ֶ�������ֱ��ָ���������ĵ���������
	 * <br/><br/>
	 * ����ֵ: {code:OK,obj:{result:[���������ݣ������ֶ��������������䵯��������]}}
	 */
    public void popup_m(HttpServletRequest req,HttpServletResponse rsp){
    	//����
    			String tableName = req.getParameter("tableName");
    			//�ֶ���
    			String fieldName = req.getParameter("fieldName");
    			//��������
    			String selectName = req.getParameter("selectName");

    			//��ǰҳ���һ�йؼ�KeyField
    			String keyField = req.getParameter("keyF");
    			
    			//��ǰҳ���һ�йؼ�KeyValue
    			String keyValue = req.getParameter("keyV");
    			
    			LoginBean loginBean = this.getLoginBean(req);

    			Map<String, Object> data = new HashMap<String, Object>();
    			//�������ص�����
    			ArrayList<HashMap> rows = new ArrayList<HashMap>(); // ������

    			Hashtable<String, DBTableInfoBean> allTables = BaseEnv.tableInfos;
    			Locale locale = GlobalsTool.getLocale(req);
    			//��������Ӧ��Bean
    			PopupSelectBean popSelectBean = null;
    			if (selectName != null && selectName.length() > 0) {
    				popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(selectName);
    			} else {
    				// ���ҳ�洫������ֶ�����������ֶ������ҳ���Ӧ�ĵ���ѡ���
    				popSelectBean = DDLOperation.getFieldInfo(allTables, tableName, fieldName).getSelectBean();
    			}
    			if (popSelectBean.getTopPopup() != null && popSelectBean.getTopPopup().length() > 0) {
    				//����һ�������������ĵ���
    				popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(popSelectBean.getTopPopup());
    			}

    			//��ѯ�ؼ���
    			String keyword = req.getParameter("keyword");
    			keyword = keyword == null ? "" : keyword.trim();
    			if (req.getMethod().equals("GET"))
    				keyword = GlobalsTool.toChinseChar(keyword);
    			keyword = GlobalsTool.encodeTextCode(keyword);
    			if (keyword.endsWith(","))
    				keyword = keyword.substring(0, keyword.length() - 1);

    			boolean hasTopPopup = false;
    			if (popSelectBean.getTopPopup() != null && popSelectBean.getTopPopup().length() > 0) {
    				//����һ�������������ĵ���
    				popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(popSelectBean.getTopPopup());
    				hasTopPopup = true;
    			}

    			String sunCompanyID = loginBean.getSunCmpClassCode();

    			//����˳��Ϊ 0 ����.�ֶ��� 1 ֵ 2 �Ա�(= like >= <=)
    			ArrayList<String[]> param = new ArrayList<String[]>();
    			//�μ��������ֶ�
    			ArrayList<PopField> cfields = new ArrayList<PopField>();

    			//�ȴ�����������ؼ������������
    			if (popSelectBean.isKeySearch()) {
    				for (PopField popField : popSelectBean.getDisplayField2()) {
    					if (popField.keySearch)
    						cfields.add(popField);
    				}
    			} else {
    				for (PopField popField : popSelectBean.getDisplayField2()) {
    					if (popField.getSearchType() != 0 && popField.getSearchType() != PopField.SEARCH_SCOPE) {
    						//û��keyword������£�Ҫģ��ƥ�����������ֶΣ�����Ҫ��ȥ�����ͺͷ�Χ��
    						DBFieldInfoBean dfb = GlobalsTool.getFieldBean(popField.getFieldName());
    						if (dfb == null || (dfb.getFieldType() != DBFieldInfoBean.FIELD_INT && dfb.getFieldType() != DBFieldInfoBean.FIELD_DOUBLE))
    							cfields.add(popField);
    					}
    				}
    			}

    			//���û��keysearch��displayField�������й���
    			param = DynAjaxSelect.getCondition(keyword, allTables, cfields);
    			keyword = "";

    			ArrayList<String> tabParam = popSelectBean.getTableParams();
    			//��ȡ���������������ϵĸ���������ֵ
    			HashMap<String, String> mainParam = new HashMap<String, String>();
    			String mainField = "";
    			String value = "";
    			for (int i = 0; i < tabParam.size(); i++) {
    				mainField = tabParam.get(i).toString();
    				if (mainField.indexOf("@TABLENAME") >= 0) {
    					mainField = tableName + "_" + mainField.substring(mainField.indexOf("_") + 1);
    				}
    				value = req.getParameter(mainField) == null ? "" : req.getParameter(mainField);
    				mainParam.put(mainField, value);
    				try {
    					mainParam.put(mainField, java.net.URLDecoder.decode(value, "UTF-8"));
    				} catch (UnsupportedEncodingException e) {
    					e.printStackTrace();
    				}
    			}

    			// ����ģ�����ȡ�ò�ѯʱ�ķ�ΧȨ��

    			// �ж�ģ������Ƿ���ڣ��粻��������ʾ����Դģ��
    			String moduleId = req.getParameter("MOID");
    			MOperation mop;
    			if (moduleId != null && moduleId.length() > 0) {
    				mop = (MOperation) (loginBean.getOperationMapKeyId().get(moduleId));
    			} else {
    				String parentTableName = null;
    				if(null != tableName && !tableName.equals("")){
    					DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, tableName);
    					parentTableName = tableInfo.getPerantTableName();
    				}			
    				
    				if (parentTableName != null && parentTableName.indexOf(";") > 0) {
    					parentTableName = parentTableName.substring(0, parentTableName.indexOf(";"));
    				}
    				String moduleType = req.getParameter("moduleType");
    				mop = GlobalsTool.getMOperationMap(parentTableName, tableName, moduleType, loginBean);
    			}
    			ArrayList<LoginScopeBean> scopeRight = new ArrayList<LoginScopeBean>();
    			if (mop != null) {
    				scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
    				DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, tableName);
    				if (tableInfo != null && tableInfo.getPerantTableName() != null && !"".equals(tableInfo.getPerantTableName())) {
    					// ������ֵܱ�,����Ĳ�ѯ��ΧȨ�޼�����ϸ��ķ�ΧȨ��
    					MOperation mopDet = (MOperation) loginBean.getOperationMap().get("/UserFunctionQueryAction.do?parentTableName=" + "&tableName=" + tableName);
    					if (mopDet != null) {
    						scopeRight.addAll(mopDet.queryScope);
    					}
    				}
    			}
    			// �������з���Ȩ��
    			scopeRight.addAll(mop.classScope);
    			// ���빫��Ȩ��
    			ArrayList<LoginScopeBean> allScopeList = loginBean.getAllScopeRight();
    			scopeRight.addAll(allScopeList);

    			DynDBManager dyn = new DynDBManager();
    			popSelectBean.setPopEnter(true);
    			int tmpPage = 1;
    			int pageSize = 9999;
    			if (req.getParameter("pageNo") != null) {
    				try {
    					tmpPage = Integer.parseInt(req.getParameter("pageNo"));
    				} catch (NumberFormatException e) {
    					e.printStackTrace();
    				}
    			}
    			if (req.getParameter("pageSize") != null) {
    				try {
    					pageSize = Integer.parseInt(req.getParameter("pageSize"));
    				} catch (NumberFormatException e) {
    					e.printStackTrace();
    				}
    			}
    			//��ѯ���ݿ⣬������ǰ9999������    			
    			/*
    			Map ret = dyn.popSelect_m(fieldName, tableName, popSelectBean, allTables, param, mainParam, scopeRight, tmpPage, pageSize, "", "", loginBean.getId(), sunCompanyID, locale, keyword, req, "",
    					popSelectBean.isSaveParentFlag() ? 0 : PopupSelectBean.LEAFRULE, "");
    			*/
    			
    			Result rs = dyn.popSelect(fieldName, tableName, popSelectBean, allTables, param, mainParam, scopeRight, tmpPage, pageSize, "", "", loginBean.getId(), sunCompanyID, locale, keyword, req, "",
    					popSelectBean.isSaveParentFlag() ? 0 : PopupSelectBean.LEAFRULE, "");
				/*
				Result rs = null;
    			if("OK".equals(ret.get("code"))){
    				rs = (Result)ret.get("data");
    			} else{
    				Message msg = new Message("Error","-1");
        			setMsg(req, rsp, msg);
        			return;
    			}
    			*/
    			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
    				for (Object[] os : (List<Object[]>) rs.retVal) {
    					HashMap one = new HashMap();

    					for (int k = 0; k < os.length; k++) {
    						if(k>=popSelectBean.getAllFields().size()){
    							one.put("childCount", os[k]);
    							continue;
    						}
    						PopField fv = popSelectBean.getAllFields().get(k);
    						String osstr = os[k] + "";

    						String display = fv.fieldName;
    						DBFieldInfoBean tempfib = DDLOperation.getFieldInfo(allTables, popSelectBean.getTableName(display), popSelectBean.getFieldName(display));
    						if (tempfib == null) {
    							display = fv.parentName;

    							if (display == null || display.length() == 0) {
    								display = fv.getDisplay();
    							}
    							if (display == null || display.length() == 0) {
    								BaseEnv.log.info("--------------������������Ϣ��������" + popSelectBean.getName() + "�����ֶ�'" + fv.getFieldName() + "'�ڱ��в��棬��δָ��parentName��display");
    							}
    							if (tableName != null) {
    								display = display.replace("@TABLENAME", tableName);
    							}
    							tempfib = DDLOperation.getFieldInfo(allTables, popSelectBean.getTableName(display), popSelectBean.getFieldName(display));
    						}
    						if (tempfib != null && tempfib.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
    							osstr = GlobalsTool.getEnumerationItemsDisplay(tempfib.getRefEnumerationName(), value, locale.toString());
    						} else if (tempfib != null && tempfib.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
    							if (osstr.equals("null") || osstr.equals("")) {
    								osstr = "0";
    							}
    							osstr = GlobalsTool.newFormatNumber(new Double(osstr), false, false, "true".equals(BaseEnv.systemSet.get("intswitch").getSetting()), popSelectBean.getTableName(fv.fieldName),
    									popSelectBean.getFieldName(fv.fieldName), true);
    						}
    						osstr = GlobalsTool.revertTextCode2(osstr);

    						String fvn = "";
    						if (fv.type == 1) {
    							fvn = fv.parentName;
    							if (fvn == null || fvn.length() == 0) {
    								fvn = fv.fieldName;
    							}
    						} else {
    							fvn = fv.asName;
    							if (fvn == null || fvn.length() == 0) {
    								fvn = fv.fieldName;
    							}
    							//����Ǹ������ֶΣ������display
    							if (!fvn.matches("[\\w\\.]*")) {
    								fvn = fv.getDisplay();
    								if (fvn == null || fvn.length() == 0) {
    									Message msg = new Message("ERROR", "�ֶ�" + fv.getFieldName() + "�Ǹ����ֶα�������asName��display");
    									setMsg(req, rsp, msg);
    									return;
    								}
    							}
    						}
    						fvn = fvn.replace("@TABLENAME.", "").replace('.', '_');

    						one.put(fvn, osstr);
    					}
    					rows.add(one);
    				}
    			}
    			data.put("result", rows);

    			Message msg = new Message("OK","", data);
    			setMsg(req, rsp, msg);
    }

    /**
	 * ȡ�Զ��嵯�������ݣ��Զ��嵯������AIO��ƣ��ӿڵ���������PC������һ��
	 * 
	 * @param tableName  ����
	 * @param fieldName �ֶ���
	 * @param selectName �������������Բ�ָ���������ָ���������ֶ���������£����Զ�ȡ��Ӧ����ֶεĵ����������û��ָ������ֶ�������ֱ��ָ���������ĵ���������
	 * <br/><br/>
	 * ����ֵ: {code:OK,obj:{result:[���������ݣ������ֶ��������������䵯��������]}}
	 */
	public void popup(HttpServletRequest req, HttpServletResponse rsp) {
		//����
		String tableName = req.getParameter("tableName");
		//�ֶ���
		String fieldName = req.getParameter("fieldName");
		//��������
		String selectName = req.getParameter("selectName");

		LoginBean loginBean = this.getLoginBean(req);

		Map<String, Object> data = new HashMap<String, Object>();
		//�������ص�����
		ArrayList<HashMap> rows = new ArrayList<HashMap>(); // ������

		Hashtable<String, DBTableInfoBean> allTables = BaseEnv.tableInfos;
		Locale locale = GlobalsTool.getLocale(req);
		//��������Ӧ��Bean
		PopupSelectBean popSelectBean = null;
		if (selectName != null && selectName.length() > 0) {
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(selectName);
		} else {
			// ���ҳ�洫������ֶ�����������ֶ������ҳ���Ӧ�ĵ���ѡ���
			popSelectBean = DDLOperation.getFieldInfo(allTables, tableName, fieldName).getSelectBean();
		}
		if (popSelectBean.getTopPopup() != null && popSelectBean.getTopPopup().length() > 0) {
			//����һ�������������ĵ���
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(popSelectBean.getTopPopup());
		}

		//��ѯ�ؼ���
		String keyword = req.getParameter("keyword");
		keyword = keyword == null ? "" : keyword.trim();
		if (req.getMethod().equals("GET"))
			keyword = GlobalsTool.toChinseChar(keyword);
		keyword = GlobalsTool.encodeTextCode(keyword);
		if (keyword.endsWith(","))
			keyword = keyword.substring(0, keyword.length() - 1);

		boolean hasTopPopup = false;
		if (popSelectBean.getTopPopup() != null && popSelectBean.getTopPopup().length() > 0) {
			//����һ�������������ĵ���
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(popSelectBean.getTopPopup());
			hasTopPopup = true;
		}

		String sunCompanyID = loginBean.getSunCmpClassCode();

		//����˳��Ϊ 0 ����.�ֶ��� 1 ֵ 2 �Ա�(= like >= <=)
		ArrayList<String[]> param = new ArrayList<String[]>();
		//�μ��������ֶ�
		ArrayList<PopField> cfields = new ArrayList<PopField>();

		//�ȴ�����������ؼ������������
		if (popSelectBean.isKeySearch()) {
			for (PopField popField : popSelectBean.getDisplayField2()) {
				if (popField.keySearch)
					cfields.add(popField);
			}
		} else {
			for (PopField popField : popSelectBean.getDisplayField2()) {
				if (popField.getSearchType() != 0 && popField.getSearchType() != PopField.SEARCH_SCOPE) {
					//û��keyword������£�Ҫģ��ƥ�����������ֶΣ�����Ҫ��ȥ�����ͺͷ�Χ��
					DBFieldInfoBean dfb = GlobalsTool.getFieldBean(popField.getFieldName());
					if (dfb == null || (dfb.getFieldType() != DBFieldInfoBean.FIELD_INT && dfb.getFieldType() != DBFieldInfoBean.FIELD_DOUBLE))
						cfields.add(popField);
				}
			}
		}

		//���û��keysearch��displayField�������й���
		param = DynAjaxSelect.getCondition(keyword, allTables, cfields);
		keyword = "";

		ArrayList<String> tabParam = popSelectBean.getTableParams();
		//��ȡ���������������ϵĸ���������ֵ
		HashMap<String, String> mainParam = new HashMap<String, String>();
		String mainField = "";
		String value = "";
		for (int i = 0; i < tabParam.size(); i++) {
			mainField = tabParam.get(i).toString();
			if (mainField.indexOf("@TABLENAME") >= 0) {
				mainField = tableName + "_" + mainField.substring(mainField.indexOf("_") + 1);
			}
			value = req.getParameter(mainField) == null ? "" : req.getParameter(mainField);
			mainParam.put(mainField, value);
			try {
				mainParam.put(mainField, java.net.URLDecoder.decode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		// ����ģ�����ȡ�ò�ѯʱ�ķ�ΧȨ��

		// �ж�ģ������Ƿ���ڣ��粻��������ʾ����Դģ��
		String moduleId = req.getParameter("MOID");
		MOperation mop;
		if (moduleId != null && moduleId.length() > 0) {
			mop = (MOperation) (loginBean.getOperationMapKeyId().get(moduleId));
		} else {
			String parentTableName = null;
			if(null != tableName && !tableName.equals("")){
				DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, tableName);
				parentTableName = tableInfo.getPerantTableName();
			}			
			
			if (parentTableName != null && parentTableName.indexOf(";") > 0) {
				parentTableName = parentTableName.substring(0, parentTableName.indexOf(";"));
			}
			String moduleType = req.getParameter("moduleType");
			mop = GlobalsTool.getMOperationMap(parentTableName, tableName, moduleType, loginBean);
		}
		ArrayList<LoginScopeBean> scopeRight = new ArrayList<LoginScopeBean>();
		if (mop != null) {
			scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, tableName);
			if (tableInfo != null && tableInfo.getPerantTableName() != null && !"".equals(tableInfo.getPerantTableName())) {
				// ������ֵܱ�,����Ĳ�ѯ��ΧȨ�޼�����ϸ��ķ�ΧȨ��
				MOperation mopDet = (MOperation) loginBean.getOperationMap().get("/UserFunctionQueryAction.do?parentTableName=" + "&tableName=" + tableName);
				if (mopDet != null) {
					scopeRight.addAll(mopDet.queryScope);
				}
			}
		}
		// �������з���Ȩ��
		scopeRight.addAll(mop.classScope);
		// ���빫��Ȩ��
		ArrayList<LoginScopeBean> allScopeList = loginBean.getAllScopeRight();
		scopeRight.addAll(allScopeList);

		DynDBManager dyn = new DynDBManager();
		popSelectBean.setPopEnter(true);
		int tmpPage = 1;
		int pageSize = 9999;
		if (req.getParameter("pageNo") != null) {
			try {
				tmpPage = Integer.parseInt(req.getParameter("pageNo"));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if (req.getParameter("pageSize") != null) {
			try {
				pageSize = Integer.parseInt(req.getParameter("pageSize"));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		//��ѯ���ݿ⣬������ǰ9999������
		Result rs = dyn.popSelect(fieldName, tableName, popSelectBean, allTables, param, mainParam, scopeRight, tmpPage, pageSize, "", "", loginBean.getId(), sunCompanyID, locale, keyword, req, "",
				popSelectBean.isSaveParentFlag() ? 0 : PopupSelectBean.LEAFRULE, "");

		
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			for (Object[] os : (List<Object[]>) rs.retVal) {
				HashMap one = new HashMap();

				for (int k = 0; k < os.length; k++) {
					if(k>=popSelectBean.getAllFields().size()){
						one.put("childCount", os[k]);
						continue;
					}
					PopField fv = popSelectBean.getAllFields().get(k);
					String osstr = os[k] + "";

					String display = fv.fieldName;
					DBFieldInfoBean tempfib = DDLOperation.getFieldInfo(allTables, popSelectBean.getTableName(display), popSelectBean.getFieldName(display));
					if (tempfib == null) {
						display = fv.parentName;

						if (display == null || display.length() == 0) {
							display = fv.getDisplay();
						}
						if (display == null || display.length() == 0) {
							BaseEnv.log.info("--------------������������Ϣ��������" + popSelectBean.getName() + "�����ֶ�'" + fv.getFieldName() + "'�ڱ��в��棬��δָ��parentName��display");
						}
						if (tableName != null) {
							display = display.replace("@TABLENAME", tableName);
						}
						tempfib = DDLOperation.getFieldInfo(allTables, popSelectBean.getTableName(display), popSelectBean.getFieldName(display));
					}
					if (tempfib != null && tempfib.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
						osstr = GlobalsTool.getEnumerationItemsDisplay(tempfib.getRefEnumerationName(), value, locale.toString());
					} else if (tempfib != null && tempfib.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
						if (osstr.equals("null") || osstr.equals("")) {
							osstr = "0";
						}
						osstr = GlobalsTool.newFormatNumber(new Double(osstr), false, false, "true".equals(BaseEnv.systemSet.get("intswitch").getSetting()), popSelectBean.getTableName(fv.fieldName),
								popSelectBean.getFieldName(fv.fieldName), true);
					}
					osstr = GlobalsTool.revertTextCode2(osstr);

					String fvn = "";
					if (fv.type == 1) {
						fvn = fv.parentName;
						if (fvn == null || fvn.length() == 0) {
							fvn = fv.fieldName;
						}
					} else {
						fvn = fv.asName;
						if (fvn == null || fvn.length() == 0) {
							fvn = fv.fieldName;
						}
						//����Ǹ������ֶΣ������display
						if (!fvn.matches("[\\w\\.]*")) {
							fvn = fv.getDisplay();
							if (fvn == null || fvn.length() == 0) {
								Message msg = new Message("ERROR", "�ֶ�" + fv.getFieldName() + "�Ǹ����ֶα�������asName��display");
								setMsg(req, rsp, msg);
								return;
							}
						}
					}
					fvn = fvn.replace("@TABLENAME.", "").replace('.', '_');

					one.put(fvn, osstr);
				}
				rows.add(one);
			}
		}
		data.put("result", rows);

		Message msg = new Message("OK","", data);
		setMsg(req, rsp, msg);

	}

	/**
	 * ȡ�Զ��嵯������ʾ�е��ֶ���Ϣ��
	 * 
	 * @param tableName  ����
	 * @param fieldName �ֶ���
	 * @param selectName �������������Բ�ָ���������ָ���������ֶ���������£����Զ�ȡ��Ӧ����ֶεĵ����������û��ָ������ֶ�������ֱ��ָ���������ĵ���������
	 * <br/><br/>
	 * ����ֵ: {code:OK,obj:{showfields:��ʾ�ֶ���������,tabParam�������}}
	 */
	public void popupInfo(HttpServletRequest req, HttpServletResponse rsp) {
		//����
		String tableName = req.getParameter("tableName");
		//�ֶ���
		String fieldName = req.getParameter("fieldName");
		//��������
		String selectName = req.getParameter("selectName");

		LoginBean loginBean = this.getLoginBean(req);

		Map<String, Object> data = new HashMap<String, Object>();
		//�������ص�����
		ArrayList<HashMap> rows = new ArrayList<HashMap>(); // ������

		Hashtable<String, DBTableInfoBean> allTables = BaseEnv.tableInfos;
		Locale locale = GlobalsTool.getLocale(req);
		//��������Ӧ��Bean
		PopupSelectBean popSelectBean = null;
		if (selectName != null && selectName.length() > 0) {
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(selectName);
		} else {
			// ���ҳ�洫������ֶ�����������ֶ������ҳ���Ӧ�ĵ���ѡ���
			popSelectBean = DDLOperation.getFieldInfo(allTables, tableName, fieldName).getSelectBean();
		}
		if (popSelectBean.getTopPopup() != null && popSelectBean.getTopPopup().length() > 0) {
			//����һ�������������ĵ���
			popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(popSelectBean.getTopPopup());
		}
		Map<String, String> map = new HashMap<String, String>();
		//����Ϊ��ʾ�ֶ�
		for (PopField field : popSelectBean.getDisplayField()) {
			//��ʾ�ֶβ�����ͼƬ
			if (13 == field.fieldType) {
				continue;
			}

			String display = field.fieldName;
			DBFieldInfoBean tempfib = DDLOperation.getFieldInfo(allTables, popSelectBean.getTableName(display), popSelectBean.getFieldName(display));
			if (tempfib == null) {
				display = field.parentName;

				if (display == null || display.length() == 0) {
					display = field.getDisplay();
				}
				if (display == null || display.length() == 0) {
					BaseEnv.log.info("--------------������������Ϣ��������" + popSelectBean.getName() + "�����ֶ�'" + field.getFieldName() + "'�ڱ��в��棬��δָ��parentName��display");
				}
				if (tableName != null) {
					display = display.replace("@TABLENAME", tableName);
				}
			}

			map.put(display.replace(".", "_"), field.getDisplayLocale(tableName, allTables, locale));
		}
		data.put("showfields", map);

		ArrayList<String> tabParam = popSelectBean.getTableParams();
		ArrayList<String> tabParamList = new ArrayList<String>();
		String mainField = "";
		String value = "";
		for (int i = 0; i < tabParam.size(); i++) {
			mainField = tabParam.get(i).toString();
			if (mainField.indexOf("@TABLENAME") >= 0) {
				mainField = tableName + "_" + mainField.substring(mainField.indexOf("_") + 1);
			}
			tabParamList.add(mainField);
		}
		data.put("tabParam", tabParamList);

		Message msg = new Message("OK","", data);
		setMsg(req, rsp, msg);

	}

	/**
	 * �޸��Զ��嵥�ݣ�
	 * 
	 * @param tableName  ����
	 * @param parentTableName ������
	 * @param fieldName �ֶ���
	 * @param saveType saveDraft:��ݸ�;��:��ʽ����
	 * @param defineInfo ���Զ������е�����ѡȷ�Ͽ򣬻��Զ�����һ���ֶ����ʶ���룬��ԭ�Ĵ��롣Ĭ��Ϊ��
	 * @param values  �洢���е����ֶμ���ֵ��HashMap<fieldName,value>
	 * <br/><br/>
	 * ����ֵ: {code:'OKִ�гɹ���CONFIRM������ȷ�Ͽ�ERROR��ִ�д���',msg����ʾ��Ϣ}
	 */
	public void update(HttpServletRequest request, HttpServletResponse rsp) {
		String tableName = request.getParameter("tableName");
		String parentTableName = request.getParameter("parentTableName");
		
		String saveType = request.getParameter("saveType"); //�������� saveDraft �ݸ�
		String defineInfo = request.getParameter("defineInfo"); //��¼��ѡdefineInfo��Ϣ
		String valuesStr = request.getParameter("values");

		//�����ֵܱ��ID
		String locale = getLocale(request).toString();
		LoginBean loginBean = getLoginBean(request);
		
		Message msg = AIOConnect.update(locale, loginBean, tableName, saveType, defineInfo, valuesStr);
		this.setMsg(request, rsp, msg);
		return;
	}

	/**
	 * У�鵥�ݵĻ���ڼ�
	 * @param tableName
	 * @param keyId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private boolean vildatePeriod(String tableName, String keyId, HttpServletRequest request) throws Exception {
		DBTableInfoBean tableInfo = (DBTableInfoBean) BaseEnv.tableInfos.get(tableName);
		String sysParamter = tableInfo.getSysParameter();// ����Ϣϵͳ����
		Date time = null;
		String timeStr = "";
		String workFlowNodeName = "";
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean bean = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			if (bean.getDefaultValue() != null && "accendnotstart".equals(bean.getFieldIdentityStr())) {
				AIODBManager aioMgt = new AIODBManager();
				Result rs = aioMgt.sqlList("select " + bean.getFieldName() + ",workFlowNodeName from " + tableName + " where id='" + keyId + "'", new ArrayList());
				if (((ArrayList) rs.retVal).size() > 0) {
					timeStr = ((Object[]) ((ArrayList) rs.retVal).get(0))[0].toString();
					workFlowNodeName = ((Object[]) ((ArrayList) rs.retVal).get(0))[1].toString();
				}
			}
		}
		if (timeStr.length() > 0) {
			time = BaseDateFormat.parse(timeStr, BaseDateFormat.yyyyMMdd);
			int currentMonth = 0;
			int currentYear = 0;
			if (null != time) {
				currentMonth = time.getMonth() + 1;
				currentYear = time.getYear() + 1900;
			}
			int periodMonth = -1;
			int periodYear = -1;
			AccPeriodBean accBean = (AccPeriodBean) BaseEnv.accPerios.get(this.getLoginBean(request).getSunCmpClassCode());
			if (accBean != null) {
				periodMonth = accBean.getAccMonth();
				periodYear = accBean.getAccYear();

			}
			if ((currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth) || periodMonth < 0) && currentMonth != 0 && !"draft".equals(workFlowNodeName)) {
				if (null != sysParamter) {
					boolean flag = false;
					if (sysParamter.equals("CurrentAccBefBill") && (currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth))) {
						flag = true;
					}

					if (sysParamter.equals("CurrentAccBefBillAndUnUseBeforeStart") && (currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth) || periodMonth < 0)) {
						flag = true;
					}
					if (flag) {
						return true;
					}

				}
			}
		}
		return false;
	}


	/**
	 * ȡ�Զ��嵥����ϸ���ݣ�
	 * 
	 * @param tableName  ����
	 * @param keyId ����ID
	 * <br/><br/>
	 * ����ֵ: {code:OK,obj:DetailBean���󣬼�DetailBean�ӿ�˵��}
	 * @see DetailBean
	 */
	public void detail(HttpServletRequest req, HttpServletResponse rsp) {
		String keyId = req.getParameter("keyId");
		String tableName = req.getParameter("tableName");
		LoginBean loginBean = this.getLoginBean(req);
		
		String keyName = req.getParameter("keyName"); //��ѯ���鲻һ������ID,����������ֶβ�ѯ���飬����ָ���ֶ�����
        if(keyName != null && !keyName.equals("")){
        	Result rk = userFunMgt.getDataId(tableName,keyName,keyId);
        	if(rk.retCode== ErrorCanst.DEFAULT_SUCCESS && rk.retVal!= null&&((ArrayList)rk.retVal).size()>0){
        		keyId = (String)(((Object[])(((ArrayList)rk.retVal).get(0)))[0]);
        	}else{
        		Message msg = new Message("ERROR", "���ݲ�����");
    			setMsg(req, rsp, msg);
    			return;
        	}
        }
		
		//*****������������Ϣ*****//
        ArrayList<ColConfigBean> allConfigList = new ArrayList<ColConfigBean>() ;
        // �����������Զ�������
        ArrayList<ColConfigBean> configList = new ArrayList<ColConfigBean>() ;
        Hashtable<String, ArrayList<ColConfigBean>> childTableConfigList = new Hashtable<String,ArrayList<ColConfigBean>>() ;
        Hashtable<String,ArrayList<ColConfigBean>> userColConfig = (Hashtable<String,ArrayList<ColConfigBean>>)
												req.getSession().getServletContext().getAttribute("userSettingColConfig") ;
        if(userColConfig!=null && userColConfig.size()>0){
        	configList = userColConfig.get(tableName+"bill") ;
        	if(configList!=null){
        		allConfigList.addAll(configList) ;
        	}
        }
        
        ArrayList childTableList = DDLOperation.getChildTables(tableName, BaseEnv.tableInfos);
		//������ϸ���Զ����е���ʾ
		for(int i=0;i<childTableList.size();i++){
			DBTableInfoBean childTableInfo=(DBTableInfoBean)childTableList.get(i);
		    if(userColConfig!=null && userColConfig.size()>0){
		    	ArrayList<ColConfigBean> childConfigList = userColConfig.get(childTableInfo.getTableName()+"bill") ;
		    	if(childConfigList!=null){
		    		//�������ģ���ֶ����ã���ô�������ֶ��н���������Щ�ֶΣ�ҳ�潫����������
		        	allConfigList.addAll(childConfigList) ;
		    	}
		    }
		}
        //********end********//
        
		DetailBean bean = AIOConnect.detail(GlobalsTool.getLocale(req).toString(), loginBean, keyId, tableName);
		if (bean.getCode() == ErrorCanst.DEFAULT_SUCCESS) {
			//��Կͻ����Ϲ�ϵ���ֶ������⴦��
			String serialStr = "";
			if("CRMClientInfo".equals(tableName)){
				ClientSetingMgt moduleMgt = new ClientSetingMgt() ;
				String moduleId = (String)bean.getValues().get("ModuleId");
				Result rs = moduleMgt.detailCrmModule(moduleId);
				ClientModuleBean moduleBean = (ClientModuleBean)rs.retVal;
				serialStr = moduleBean.getTableInfo().split(":")[0].substring(tableName.length());
				//if(serialStr.length()>0){
					ArrayList<DBFieldInfoBean> mainFields =  bean.getFieldList();
					for(int i = 0 ;i<= mainFields.size()-1;i++){
						DBFieldInfoBean _i = mainFields.get(i);
						if(_i.getRefEnumerationName() == null){
							continue;
						}
						if(_i.getFieldName().startsWith("extend") || _i.getFieldName().startsWith("extent")){
							_i.setRefEnumerationName(_i.getFieldName());
						} else{
							_i.setRefEnumerationName(_i.getRefEnumerationName().replaceAll("[0-9]", ""));
						}						
						if(_i.getInputType() == 1 && !_i.getRefEnumerationName().contains(serialStr)){						
							_i.setRefEnumerationName(_i.getRefEnumerationName()+serialStr);
						}
						
					}					
				//}
			}
			
			//ת��fieldList
			bean.setFieldList(toFieldList(bean.getFieldList(), GlobalsTool.getLocale(req).toString(),allConfigList,bean.getValues()));						
			
			for(Object tn : bean.getChildShowField().keySet().toArray()){				
				ArrayList list  = (ArrayList)bean.getChildShowField().get(tn);
				
				//��Կͻ����Ϲ�ϵ���ֶ������⴦��
				if("CRMClientInfo".equals(tableName)){
					ArrayList<DBFieldInfoBean> childFields = list;
					for(DBFieldInfoBean _subItem : childFields){
						if(_subItem.getRefEnumerationName() == null){
							continue;
						}
						if(_subItem.getFieldName().startsWith("extend") || _subItem.getFieldName().startsWith("extent")){
							_subItem.setRefEnumerationName(_subItem.getFieldName());
						} else{
							_subItem.setRefEnumerationName(_subItem.getRefEnumerationName().replaceAll("[0-9]", ""));
						}												
						
						if(_subItem.getInputType() == 1 && !_subItem.getRefEnumerationName().contains(serialStr)){
							_subItem.setRefEnumerationName(_subItem.getRefEnumerationName()+serialStr);
						}
					}
					bean.getChildShowField().put(tn, childFields);
				}
				
				//*******�������ֶν������δ���********//
				ArrayList<FieldBean> childList = toFieldList(list, GlobalsTool.getLocale(req).toString(),allConfigList,bean.getValues());
				for(int i = childList.size()-1;i>=0;i--){
					FieldBean fBean = childList.get(i);
					if("id".equals(fBean.getFieldName()) || "createBy".equals(fBean.getFieldName()) || "createTime".equals(fBean.getFieldName()) || "lastUpdateBy".equals(fBean.getFieldName()) || "lastUpdateTime".equals(fBean.getFieldName()) || "f_brother".equals(fBean.getFieldName())){
						childList.remove(i);
						continue;
					}
				}
				//*************end**************//
				bean.getChildShowField().put(tn, childList);
			}						
			
			Message msg = new Message("OK","", bean); 
			setMsg(req, rsp, msg);
		} else {
			Message msg = new Message("ERROR", bean.getMsg());
			setMsg(req, rsp, msg);
		}
	}

	/**
	 * ���ֶ�ת��Ϊ�ӿ��ֶ�����
	 * @param fieldList
	 * @param locale
	 * @return
	 */
	private ArrayList<FieldBean> toFieldList(ArrayList<DBFieldInfoBean> fieldList, String locale,List allConfigList,HashMap fieldVal) {
		ArrayList<FieldBean> list = new ArrayList<FieldBean>();
		for (DBFieldInfoBean bean : fieldList) {
			FieldBean fb = new FieldBean();
			Field[] fs = fb.getClass().getDeclaredFields();
			//****����ĳЩ�ֶ�****//
			//#if("$row.inputType" != "100" && "$row.fieldType" != "16"   && $row.fieldName != "id" && $row.fieldName != "createBy" && $row.fieldName != "createTime" && $row.fieldName != "lastUpdateBy" && $row.fieldName != "lastUpdateTime" && "$row.fieldName"!="moduleType" && "$row.fieldName"!="f_brother")
			//if("100".equals(String.valueOf(bean.getInputType())) || "16".equals(String.valueOf(bean.getFieldType())) || "f_brother".equals(bean.getFieldName())){
			if("16".equals(String.valueOf(bean.getFieldType())) || "f_brother".equals(bean.getFieldName())){
				continue;
			}
			//*******end******//
			for (Field f : fs) {
				String name = f.getName();
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				Method getm;
				try {
					if (name.equals("TableName")) {
						getm = bean.getClass().getMethod("getTableBean");
						DBTableInfoBean value = (DBTableInfoBean) getm.invoke(bean);
						Method setm = fb.getClass().getMethod("set" + name, String.class);
						setm.invoke(fb, value.getTableName());
					} else {
						if(f.getName().equals("enumItem") || f.getName().equals("popupDis"))
							continue;
						getm = bean.getClass().getMethod("get" + name);
						Object value = getm.invoke(bean);
						if (name.equals("Display")) {
							String display = ((KRLanguage) value).get(locale);
							Method setm = fb.getClass().getMethod("set" + name, String.class);
							setm.invoke(fb, display);
						} else {
							Method setm = fb.getClass().getMethod("set" + name, f.getType());
							setm.invoke(fb, value);
						}
					}
				} catch (Exception e) {
					BaseEnv.log.error("MobileAjax.toFieldList ", e);
				}
			}
			//
			if(fb.getInputType()==1 || fb.getInputTypeOld()==1 || fb.getInputTypeOld()==10 || fb.getInputTypeOld()==5){//����ö�ٻ�ѡ����ѡʱ��
				fb.setEnumItem(GlobalsTool.getEnumerationItems(fb.getRefEnumerationName(), locale));
			}else if(fb.getInputType() == 15){
				//������
				fb.setDefaultValue(GlobalsTool.getEmpFullNameByUserId((String)fieldVal.get(bean.getFieldName())));
				/*
				ArrayList<FieldBean> pops = new ArrayList<FieldBean>();
				FieldBean f = new FieldBean();
				f.setInputType(bean.getInputType());
				f.setFieldName(bean.getFieldName());
				f.setDisplay(Globals.getEmpFullNameByUserId(""));
				pops.add(f);
				*/
			}else if(fb.getInputType()==2 || ( fb.getInputTypeOld()==2 && fb.getInputType()==8 )){
				//������
				ArrayList<FieldBean> popList = new ArrayList<FieldBean>();
				if(bean.getSelectBean() !=null && bean.getSelectBean().getRelationKey().hidden){
					FieldBean f = new FieldBean();
					f.setInputType((byte)3);
					f.setFieldName(bean.getSelectBean().getFieldName(bean.getSelectBean().getRelationKey().parentName));
					popList.add(f);
				}else if( "GoodsField".equals(bean.getFieldSysType()) && GlobalsTool.getPropBean(bean.getFieldName()).getIsSequence()==1){
					FieldBean f = new FieldBean();
					f.setInputType((byte)3);
					f.setFieldName(bean.getFieldName());
					popList.add(f);
					
					String dismh=bean.getFieldName()+"_mh";       
					f = new FieldBean();
					f.setInputType(bean.getInputType());
					f.setFieldName(dismh);
					f.setDisplay(bean.getDisplay().get(locale));
					popList.add(f);
				}else{
					FieldBean f = new FieldBean();
					f.setInputType(bean.getInputType());
					f.setFieldName(bean.getSelectBean().getFieldName(bean.getSelectBean().getRelationKey().parentName));
					f.setDisplay(bean.getDisplay().get(locale));
					popList.add(f);
				}


				if(!( "GoodsField".equals(bean.getFieldSysType()) && GlobalsTool.getPropBean(bean.getFieldName()).getIsSequence()==1)){
					for(PopField srow : bean.getSelectBean().getViewFields()){
						String tableField = "";
			 			if(srow.display!=null && !srow.display.equals("")){
							if(srow.display.indexOf("@TABLENAME")==0){
								int index=srow.display.indexOf(".")+1;
								tableField=fb.getTableName()+"."+srow.display.substring(index);
							}else{
								tableField=srow.display;
							}
			 			}else{
							tableField="";
			 			}
						
						String dis="";
						if(srow.display!=null && !srow.display.equals("") && srow.display.indexOf(".")==-1){
							dis = srow.display;
						}else {
							dis = getFieldDisplay(fb.getTableName(),bean.getSelectBean().getName(),tableField,locale);
							if(dis == "") 
								dis = getFieldDisplay(srow.fieldName,locale);
						}
						if(allConfigList.size()>0){
							if(GlobalsTool.colIsExistConfigList(fb.getTableName(),srow.asName,"bill")&&GlobalsTool.getFieldBean(srow.asName)!=null){
								FieldBean f = new FieldBean();
								f.setInputType(bean.getInputType());
								f.setFieldType(GlobalsTool.getFieldBean(srow.asName).getFieldType());
								f.setFieldName(getTableField(srow.asName));
								f.setDisplay(dis);
								popList.add(f);
							}
						}else{ //û��������ʱ  
							if("true".equals(srow.hiddenInput)){
								continue;
								/*
								FieldBean f = new FieldBean();
								f.setInputType((byte)3);
								f.setFieldName(getTableField(srow.asName));
								popList.add(f);
								
								f = new FieldBean();
								f.setInputType((byte)3);
								f.setFieldName(getTableField(srow.asName));
								popList.add(f);
								*/
							}else{
								FieldBean f = new FieldBean();
								f.setInputType(bean.getInputType());
								if(GlobalsTool.getFieldBean(srow.asName) == null){
									if(GlobalsTool.getFieldBean(srow.fieldName) == null){
										continue;
									}
									f.setFieldType(GlobalsTool.getFieldBean(srow.fieldName).getFieldType());
								} else{
									f.setFieldType(GlobalsTool.getFieldBean(srow.asName).getFieldType());
								}
								
								f.setFieldName(getTableField(srow.asName));
								f.setDisplay(dis);
								popList.add(f);
							}
						}	//û�������ý���ʱ			
					}	
				}
				fb.setPopupDis(popList);
			}
			list.add(fb);
		}
		return list;
	}
	
	public static String getTableField(String fieldName) {
		return fieldName.substring(0, fieldName.indexOf(".")) + "." + fieldName.substring(fieldName.indexOf(".") + 1);
	}
	
	public String getFieldDisplay(String fieldName,String locale) {
		// ����ֶ����а����ֺţ��������ж�������ֶ���ʾ�����ؿ�
		if (fieldName == null || fieldName.trim().length() == 0 || fieldName.indexOf(";") > 0) {
			return "";
		}
		String table = fieldName.substring(0, fieldName.indexOf("."));
		String field = fieldName.substring(fieldName.indexOf(".") + 1);

		Hashtable allTables = BaseEnv.tableInfos;
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(table);
		if (tableInfo == null) {
			return table + " not Exist";
		}
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			if (fieldInfo.getFieldName().equals(field)) {
				try {
					return fieldInfo.getDisplay().get(locale).toString();
				} catch (Exception ex) {
					return fieldInfo.getFieldName();
				}
			}
		}
		return field;
	}
	
	public String getFieldDisplay(String tableNeme, String popupName, String fieldName,String locale) {
		// ����ֶ����а����ֺţ��������ж�������ֶ���ʾ�����ؿ�
		if (fieldName == null || fieldName.trim().length() == 0 || fieldName.indexOf(";") > 0) {
			return "";
		}
		
		
//		Hashtable<String, KRLanguage> moduleLanguage = (Hashtable<String, KRLanguage>) application.getAttribute("moduleColLanguage");
//		String strType = "bill" ;
//		KRLanguage language = moduleLanguage.get(tableNeme + popupName + fieldName + strType);
//		if (language != null) {
//			return language.get(getLocale());
//		}
		String table = fieldName.substring(0, fieldName.indexOf("."));
		String field = fieldName.substring(fieldName.indexOf(".") + 1);

		Hashtable allTables = BaseEnv.tableInfos;
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(table);
		if (tableInfo == null) {
			return table + " not Exist";
		}
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			if (fieldInfo.getFieldName().equals(field)) {
				try {
					return fieldInfo.getDisplay().get(locale).toString();
				} catch (Exception ex) {
					return fieldInfo.getFieldName();
				}
			}
		}
		return field;
	}
	
	private ArrayList<FieldBean> toFieldList(ArrayList<DBFieldInfoBean> fieldList, String locale) {
		ArrayList<FieldBean> list = new ArrayList<FieldBean>();
		for (DBFieldInfoBean bean : fieldList) {
			FieldBean fb = new FieldBean();
			Field[] fs = fb.getClass().getDeclaredFields();
			for (Field f : fs) {
				String name = f.getName();
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				Method getm;
				try {
					if (name.equals("TableName")) {
						getm = bean.getClass().getMethod("getTableBean");
						DBTableInfoBean value = (DBTableInfoBean) getm.invoke(bean);
						Method setm = fb.getClass().getMethod("set" + name, String.class);
						setm.invoke(fb, value.getTableName());
					} else {
						getm = bean.getClass().getMethod("get" + name);
						Object value = getm.invoke(bean);
						if (name.equals("Display")) {
							String display = ((KRLanguage) value).get(locale);
							Method setm = fb.getClass().getMethod("set" + name, String.class);
							setm.invoke(fb, display);
						} else {
							Method setm = fb.getClass().getMethod("set" + name, f.getType());
							setm.invoke(fb, value);
						}
					}
				} catch (Exception e) {
					BaseEnv.log.error("MobileAjax.toFieldList ", e);
				}
			}
			list.add(fb);
		}
		return list;
	}


	/**
	 * �Զ��嵥�����ǰ׼�����ݽӿڣ�
	 * 
	 * @param tableName  ����
	 * @param parentTableName ������
	 * @param f_brother �ֵܱ�ĸ�����ID
	 * @param parentCode ��Ŀ¼classCode
	 * @param moduleType ģ������
	 * <br/><br/>
	 * ����ֵ: {code:OK,obj:DetailBean���󣬼�DetailBean�ӿ�˵��}
	 * @see DetailBean
	 */
	public void addPrepare(HttpServletRequest request, HttpServletResponse rsp) {
		GlobalsTool globals = new GlobalsTool();
	
		DetailBean detailBean = new DetailBean();
		detailBean.setOpType("add");
		try {
			// ͨ���ര�ڵõ����ӵ�ַ�õ�Ȩ��
			String tableName = request.getParameter("tableName");
			String parentTableName = request.getParameter("parentTableName");
			parentTableName = parentTableName == null ? "" : parentTableName;
			// �����ֵܱ��ID
			String f_brother = request.getParameter("f_brother");
			f_brother = f_brother == null ? "" : f_brother;
			String parentCode = request.getParameter("parentCode");
			parentCode = parentCode == null ? "" : parentCode;
			String moduleType = request.getParameter("moduleType");

			Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
			MOperation mop = GlobalsTool.getMOperationMap(request);
			if (mop == null) {
				// ������ģ���޷���Ȩ��
				Message msg = new Message("ERROR", GlobalsTool.getMessage(request, "common.msg.RET_NO_RIGHT_ERROR"));
				setMsg(request, rsp, msg);
				return;
			}
			ArrayList scopeRight = new ArrayList();
			scopeRight.addAll(mop.getScope(MOperation.M_ADD));
			scopeRight.addAll(this.getLoginBean(request).getAllScopeRight());

			// �õ�����ģ����ֶ���Ϣ���˹�����Ϊ��ʵ�ֲ�ͬģ��ʹ��ͬһ��������ͬ��define
			HashMap<String, ArrayList<String[]>> moduleTable = (HashMap<String, ArrayList<String[]>>) BaseEnv.ModuleField.get(mop.getModuleUrl());

			LoginBean lg = this.getLoginBean(request);
			Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());

			Hashtable map = allTables;

			// �����˷�֧����ʱ�����������ĩ����֧������������Ӳ����������
			String openValue = BaseEnv.systemSet.get("sunCompany").getSetting();
			boolean openSunCompany = Boolean.parseBoolean(openValue);
			boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
			boolean isSharedTable = tableInfo.getIsSunCmpShare() == 1 ? true : false;
			// ����ְԱ����ű�ʱ������ĩ����֧��������
			boolean isEmpOrDept = "tblEmployee".equalsIgnoreCase(tableInfo.getTableName()) || "tblDepartment".equalsIgnoreCase(tableInfo.getTableName()) ? true : false;
			if (openSunCompany && !isLastSunCompany && !isSharedTable && !isEmpOrDept) {
				Message msg = new Message("ERROR", GlobalsTool.getMessage(request, "common.msg.notLastSunCompany"));
				setMsg(request, rsp, msg);
				return;
			}
			String sysParamter = tableInfo.getSysParameter();
			int periodMonth = -1;
			AccPeriodBean accBean = (AccPeriodBean) sessionSet.get("AccPeriod");
			if (accBean != null) {
				periodMonth = accBean.getAccMonth();
			}

			if (periodMonth < 0) {
				if (null != sysParamter) {
					if (sysParamter.equals("UnUseBeforeStart") || sysParamter.equals("CurrentAccBefBillAndUnUseBeforeStart")) {
						Message msg = new Message("ERROR", GlobalsTool.getMessage(request, "com.UnUseBeforeStart"));
						setMsg(request, rsp, msg);
						return;
					}
				}
			}

			HashMap values = new HashMap();
			values.put("f_brother", f_brother);
			String urlValue = "";
			//����Ĭ��ֵ
			List listField = tableInfo.getFieldInfos();
			for (int i = 0; i < listField.size(); i++) {
				DBFieldInfoBean field = (DBFieldInfoBean) listField.get(i);
				if (field.getDefaultValue() != null && !field.getDefaultValue().equals("")) {
					//�������͸�Ĭ������
					if (field.getFieldType() == 5) { 
						field.setDefaultValue(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
						if ("BornDate".equals(field.getFieldName())) {
							field.setDefaultValue("");// Bug #10752
														// ����Ĭ�ϵ�ǰ���ڣ����Ϊ��
						}
					} else if (field.getFieldType() == 6) {
						field.setDefaultValue(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
					}
				}
				if("BillNo".equalsIgnoreCase(field.getFieldIdentityStr())){
					field.setDefaultValue(globals.getBillNoCode(tableInfo.getTableName()+"_"+field.getFieldName(),request));
				}
				if ("paramDefaultValue".equals(field.getFieldIdentityStr())) {//�ֶ��Ǵ�����Ĭ��ֵ
					/* ���ô�����Ĭ��ֵ */
					String fieldValue = request.getParameter(field.getFieldName());
					values.put(field.getFieldName(), fieldValue);
				}
				// ������������Ĭ��ֵ
				if (field.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE
						|| (field.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT && field.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE)) {
					if (field.getInputValue() != null && field.getInputValue().length() > 0 && field.getDefaultValue() != null && field.getDefaultValue().length() > 0) {
						dbmgt.getDefRefValue(field, values, sessionSet, getLoginBean(request).getId(), allTables, "");
					}
				} else {
					if (field.getDefaultValue() != null && field.getDefaultValue().contains("@Sess:")) {
						String name = field.getDefaultValue().substring(field.getDefaultValue().indexOf("@Sess:") + "@Sess:".length());
						String value;
						if (name.equals("parentCode")) {
							value = request.getParameter("parentCode");
						} else {
							value = sessionSet.get(name) == null ? "" : sessionSet.get(name).toString();
						}
						values.put(field.getFieldName(), value);
					}
				}
				// ͬһ�ű�ͬ��ģ�飬�������ӵ�ַ�м����ֶ�������ʾ���ģ�飬������Щ�ֶ���ֵ�����ڲ�ѯ �ֶ���Ҫ�ۼ�������¼
				if (GlobalsTool.getUrlBillDef(request, field.getFieldName()) != null && GlobalsTool.getUrlBillDef(request, field.getFieldName()).length() > 0) {
					if (field != null && field.getFieldType() == DBFieldInfoBean.FIELD_INT) {
						urlValue += " and " + field.getFieldName() + "=" + GlobalsTool.getUrlBillDef(request, field.getFieldName()) + "";
					} else {
						urlValue += " and " + field.getFieldName() + "='" + GlobalsTool.getUrlBillDef(request, field.getFieldName()) + "'";
					}
				}
				//�����moduleType�����ѯ���ۼ�ֵʱҪ����moduleType
				if ("moduleType".equals(field.getFieldName()) && moduleType != null && moduleType.length() > 0
						&& !urlValue.contains("moduleType")) {
					if (field != null && field.getFieldType() == DBFieldInfoBean.FIELD_INT) {
						urlValue += " and moduleType=" + moduleType + "";
					} else {
						urlValue += " and moduleType='" + moduleType + "'";
					}
				}
			}
			// ������ϸ��Ĵ�sess��Ĭ��ֵ
			ArrayList childTableList = DDLOperation.getChildTables(tableName, map);
			for (int i = 0; i < childTableList.size(); i++) {
				DBTableInfoBean db = (DBTableInfoBean) childTableList.get(i);
				List childFields = db.getFieldInfos();

				for (int j = 0; j < childFields.size(); j++) {
					DBFieldInfoBean field = (DBFieldInfoBean) childFields.get(j);
					if (field.getDefaultValue() != null && field.getDefaultValue().contains("@Sess:")) {
						dbmgt.getChildDefRefValue(field, values, sessionSet, getLoginBean(request).getId(), allTables, db.getTableName());
					}
				}
			}

			/* ��ȡ���ӵ�ַ�������ӵ�ַ�д���(paramValue=true)ʱ������Ĳ���ֵ�����ӵ�values�� */
			String paramStr = request.getQueryString();
			String pStr = "paramValue=true";
			if (paramStr.indexOf(pStr) != -1) {
				// ����
				String paramVal = paramStr.substring(paramStr.indexOf(pStr) + pStr.length());
				String[] paramSplit = paramVal.split("&");
				for (String s : paramSplit) {
					if (s != null && !"".equals(s) && s.indexOf("=") != -1 && s.split("=").length > 1) {
						String val = URLDecoder.decode(s.split("=")[1], "UTF-8");
						if (val != null) {
							val = GlobalsTool.replaceSpecLitter(val);
						}
						values.put(s.split("=")[0], val);
						// ����ǹ�����ѡ��ҲҪ����ʾ�ֶε�ֵ���浽values��
						DBFieldInfoBean field = GlobalsTool.getFieldBean(tableName, s.split("=")[0]);
						if (field != null && field.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE) {
							DynDBManager dyn = new DynDBManager();
							dyn.getRefReturnValues(field, val, allTables, lg.getSunCmpClassCode(), values, values, lg.getId(), true, null, null);
						}

					}
				}
			}
			// ���Ǹ��������paramValue,��Ϊ�ڱ�ṹ�޸�ʱ��֪��ʲôԭ��@paramValue
			// ǰ���@para������룬�����޸�һ�κ�ֵ�ͱ��ˣ����Կ���һ��defaultValue������
			paramStr = request.getQueryString();
			pStr = "defaultValue=true";
			if (paramStr.indexOf(pStr) != -1) {
				// ����
				String paramVal = paramStr.substring(paramStr.indexOf(pStr) + pStr.length());
				String[] paramSplit = paramVal.split("&");
				for (String s : paramSplit) {
					if (s != null && !"".equals(s) && s.indexOf("=") != -1 && s.split("=").length > 1) {
						String val = URLDecoder.decode(s.split("=")[1], "UTF-8");
						if (val != null) {
							val = GlobalsTool.replaceSpecLitter(val);
						}
						values.put(s.split("=")[0], val);
						// ����ǹ�����ѡ��ҲҪ����ʾ�ֶε�ֵ���浽values��
						DBFieldInfoBean field = GlobalsTool.getFieldBean(tableName, s.split("=")[0]);
						if (field != null && field.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE) {
							DynDBManager dyn = new DynDBManager();
							dyn.getRefReturnValues(field, val, allTables, lg.getSunCmpClassCode(), values, values, lg.getId(), true, null, null);
						}

					}
				}
			}
			detailBean.setValues(values);

			// ����ӱ��й�����ѡ���ֶ��еĵ������Ƿ������ͬ�ķ��ص�displayField�ֶκ͹��������ֶ�
			ArrayList<ColConfigBean> allConfigList = new ArrayList<ColConfigBean>();
			// �����������Զ�������
			ArrayList<ColConfigBean> configList = new ArrayList<ColConfigBean>();
			Hashtable<String, ArrayList<ColConfigBean>> childTableConfigList = new Hashtable<String, ArrayList<ColConfigBean>>();
			Hashtable<String, ArrayList<ColConfigBean>> userColConfig = (Hashtable<String, ArrayList<ColConfigBean>>) request.getSession().getServletContext().getAttribute("userSettingColConfig");
			if (userColConfig != null && userColConfig.size() > 0) {
				configList = userColConfig.get(tableName + "bill");
				if (configList != null) {
					allConfigList.addAll(configList);
				}
			}
			String allChildName = tableName + ",";// ��������ӱ�ȫ���ö�������
			// ������ϸ���Զ����е���ʾ
			for (int i = 0; i < childTableList.size(); i++) {
				DBTableInfoBean childTableInfo = (DBTableInfoBean) childTableList.get(i);
				allChildName += childTableInfo.getTableName() + ",";
				if (userColConfig != null && userColConfig.size() > 0) {
					ArrayList<ColConfigBean> childConfigList = userColConfig.get(childTableInfo.getTableName() + "bill");
					if (childConfigList != null) {
						// �������ģ���ֶ����ã���ô�������ֶ��н���������Щ�ֶΣ�ҳ�潫����������
						if (moduleTable != null && moduleTable.get(childTableInfo.getTableName()) != null) {
							ArrayList<String[]> moduleFields=(ArrayList<String[]>)moduleTable.get(childTableInfo.getTableName());
                			ArrayList<ColConfigBean> cols=new ArrayList<ColConfigBean>();
                			for(ColConfigBean colBean :childConfigList){
                				boolean found = false;
                				for(String[] mf :moduleFields){
                					if(mf[0].equals(colBean.getColName())){
                						found=true;
                						break;
                					}
                				}
                				if(true){
                					cols.add(colBean);
                				}
                			}
							allConfigList.addAll(cols);
							childTableConfigList.put(childTableInfo.getTableName(), cols);
						} else {
							allConfigList.addAll(childConfigList);
							childTableConfigList.put(childTableInfo.getTableName(), childConfigList);
						}
					}
				}
			}

			Collections.sort(childTableList, new SortDBTable());
			ArrayList ctn = new ArrayList();
			for (DBTableInfoBean dtb : (ArrayList<DBTableInfoBean>) childTableList) {
				ctn.add(dtb.getTableName());
			}
			detailBean.setChildTableList(ctn);

			DBTableInfoBean tableinfo = DDLOperation.getTableInfo(map, tableName);

			OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(tableName);
			
			String defineInfo=request.getParameter("defineInfo");
	        Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
	    	MessageResources resources = null;
			if (ob instanceof MessageResources) {
			    resources = (MessageResources) ob;
			}
			
	        Result addView = dbmgt.addView(tableName, allTables, values, defineInfo, resources, this.getLocale(request), lg);
	        if(addView.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
	        	SaveErrorObj saveErrrorObj = dbmgt.saveError(addView, this.getLocale(request).toString(), "");
	        	Message msg = new Message("ERROR", saveErrrorObj.getMsg());
				setMsg(request, rsp, msg);
				return;
	        }
	        ArrayList<String> defineField = (ArrayList<String>)values.get("DEFINE_INPUTTYPE");

			// ����Ȩ��,�����ã�������������ʾ��Щ�ֶ�
			ArrayList<DBFieldInfoBean> fieldList = DynDBManager
					.getMainFieldList(tableName, tableInfo,defineField, configList, template, null, moduleTable, mop, "0", false, f_brother, lg, scopeRight);

			Locale locale = this.getLocale(request);
			detailBean.setFieldList(toFieldList(DynDBManager.distinctList(fieldList), locale.toString()));

			HashMap childShowField = new HashMap();
			for (int i = 0; i < childTableList.size(); i++) { // ��ϸ�����ʾ��
				DBTableInfoBean childTableInfo = (DBTableInfoBean) childTableList.get(i);
				childShowField.put(childTableInfo.getTableName(), toFieldList(DynDBManager.getMainFieldList(childTableInfo.getTableName(), childTableInfo, defineField,
						childTableConfigList.get(childTableInfo.getTableName()), template, null, moduleTable,
						 mop, "", false, f_brother, lg, scopeRight), locale.toString()));
			}
			detailBean.setChildShowField(childShowField);
			
			// �ж���Щ�ֶ���Ҫ�ۼ�������¼
			HashMap lastValues = new HashMap();
			for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
				DBFieldInfoBean b = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
				if (b.getFieldIdentityStr() != null && b.getFieldIdentityStr().equals("lastValueAdd")) {
					Result rs = dbmgt.getLastValue(tableName, parentCode, b.getFieldName(), urlValue);
					if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
						if (rs.getRetVal() != null) {
							values.put(b.getFieldName(), rs.getRetVal());
						}
					}
				}
			}
						
			Message msg = new Message("OK","", detailBean);
			setMsg(request, rsp, msg);
			return;
		}catch(Exception e){
			Message msg = new Message("ERROR", "ִ�д���"+e.getMessage());
			BaseEnv.log.error("MobileAjax.addPrepare Error:",e);
			setMsg(request, rsp, msg);
			return;
		}

	}
	
	/**
	 * DBTableInfoBean ��tbleName����
	 */
	private  class SortDBTable implements Comparator {
		public int compare(Object o1, Object o2) {
			DBTableInfoBean table1 = (DBTableInfoBean) o1;
			DBTableInfoBean table2 = (DBTableInfoBean) o2;

			if (table1 == null || table2 == null) {
				return 0;
			}

			String tableName1 = table1.getTableName();
			String tableName2 = table2.getTableName();

			return tableName1.compareToIgnoreCase(tableName2);
		}
	}
	
	/**
	 * �Զ��嵥����ӽӿڣ�
	 * 
	 * @param tableName  ����
	 * @param parentCode ��Ŀ¼classCode
	 * @param saveType saveDraft:��ݸ�;��:��ʽ����
	 * @param defineInfo ���Զ������е�����ѡȷ�Ͽ򣬻��Զ�����һ���ֶ����ʶ���룬��ԭ�Ĵ��롣Ĭ��Ϊ��
	 * @param values  �洢���е����ֶμ���ֵ��HashMap<fieldName,value>
	 * <br/><br/>
	 * ����ֵ: {code:'OKִ�гɹ���CONFIRM������ȷ�Ͽ�ERROR��ִ�д���',msg����ʾ��Ϣ}
	 */
	public void add(HttpServletRequest request, HttpServletResponse rsp) {
		
		/*�����ֵܱ��f_brother*/
		String tableName = request.getParameter("tableName");
		/*�����classCode*/
		String parentCode = request.getParameter("parentCode");
		//Ҫִ�е�define����Ϣ
		String defineInfo = request.getParameter("defineInfo");
		
		String saveType = request.getParameter("saveType"); //�������� saveDraft �ݸ�
		
		String valuesStr = request.getParameter("values");
		
		String deliverTo = request.getParameter("deliverTo");
		
		String locale = getLocale(request).toString();
		LoginBean loginBean = getLoginBean(request);
		
		Message msg = AIOConnect.add(locale, loginBean, tableName, parentCode, defineInfo, saveType, valuesStr, deliverTo);
		this.setMsg(request, rsp, msg);
		return ;
	}


	/**
	 * ȡ�Զ���ö��ֵ��
	 * 
	 * @param enum  ö�ٱ�ʶ����
	 * <br/><br/>
	 * ����ֵ: {code:OK,obj:[ö��ֵ]}
	 */
	public void getEnum(HttpServletRequest req, HttpServletResponse rsp) {
		String enumeration = req.getParameter("enum");
		List list = GlobalsTool.getEnumerationItems(enumeration, "zh_CN");
		Message msg = new Message("OK","", list);
		setMsg(req, rsp, msg);
	}

	/**
	 * ִ���Զ���Define���룬
	 * 
	 * @param tableName  ����
	 * @param defineName  �Զ���define������
	 * @param buttonTypeName define����������
	 * @param keyId ����ID���������ᴫ���Զ���������У����Լ���ID�ֶΣ���Define�����۷�
	 * @param defineInfo ���Զ������е�����ѡȷ�Ͽ򣬻��Զ�����һ���ֶ����ʶ���룬��ԭ�Ĵ��롣Ĭ��Ϊ��
	 
	 * <br/><br/>
	 * ����ֵ: {code:'OKִ�гɹ���CONFIRM������ȷ�Ͽ�ERROR��ִ�д���',msg����ʾ��Ϣ}
	 */
	public void execDefine(HttpServletRequest request, HttpServletResponse rsp) {
		String defineName = request.getParameter("defineName");
		String buttonTypeName = request.getParameter("buttonTypeName");
		String[] keyIds = request.getParameterValues("keyId");
		String tableName = request.getParameter("tableName");
		String defineInfo = request.getParameter("defineInfo");
		String locale = getLocale(request).toString();
		LoginBean loginBean = getLoginBean(request);
		Message msg = AIOConnect.execDefine(locale, loginBean, defineName, buttonTypeName, keyIds, tableName, defineInfo);
		if(msg.getCode().equals("OK")){
			String msgStr = gson.toJson(msg.getObj());
			respMsg(request, rsp, msgStr);
			return;
		}else{
			setMsg(request, rsp, msg);
			return;
		}
	}

	/**
	 * ȡ�Զ��屨��
	 * 
	 * @param reportNumber  ��������
	 * @param pageNo  ҳ��
	 * @param pageSize ÿҳ����
	 * @param parentTableName �����������ݱ��б��䴫��Ĳ���
	 * @param tableName ���������ݱ��б���
	 * @param moduleType ģ�����ƣ����ݱ��б���
	 * <br/><br/>
	 * ����ֵ: {code:OK,obj:MReportBean���󣬼�MReportBean�ӿ�˵��}
	 * @see MReportBean
	 */
	public void report(HttpServletRequest req, HttpServletResponse rsp) {
		String reportNumber = req.getParameter("reportNumber");
		String pageNoStr = req.getParameter("pageNo");
		String pageSizeStr = req.getParameter("pageSize");
		String parentTableName = req.getParameter("parentTableName"); //���ݱ��б���ת��Ĳ���
		String tableName = req.getParameter("tableName");
		String moduleType = req.getParameter("moduleType");

		int pageNo = 1;
		if (pageNoStr != null && pageNoStr.length() > 0) {
			pageNo = Integer.parseInt(pageNoStr);
		}
		int pageSize = 20;
		if (pageSizeStr != null && pageSizeStr.length() > 0) {
			pageSize = Integer.parseInt(pageSizeStr);
		}
		LoginBean lg = getLoginBean(req);

		boolean isTable = false;

		MOperation mop = null;
		if (tableName != null && tableName.length() > 0) {//���������tableNameʱ����ʾ����һ�����ݱ��б������Ǳ���
			mop = GlobalsTool.getMOperationMap(req);
			reportNumber = tableName;
			isTable = true;
			MOperation murl = null;
			if(parentTableName != null && !"".equals(parentTableName)){
				murl = (MOperation) lg.getOperationMap().get("/UserFunctionQueryAction.do?parentTableName="+parentTableName+"&tableName="+tableName);
			}			
			else{
				murl = (MOperation) lg.getOperationMap().get("/UserFunctionQueryAction.do?tableName="+tableName);
			}
			//murl = (MOperation) lg.getOperationMap().get("/ReportDataAction.do?reportNumber=" + reportNumber);
			if(murl == null){
				mop = null;
			}
		} else {
			mop = (MOperation) lg.getOperationMap().get("/ReportDataAction.do?reportNumber=" + reportNumber);
		}

		//��鵱ǰ�����Ƿ����������,��Ҫ���������ȡȨ��
		if (mop == null) {
			//������ģ���޷���Ȩ��
			setMsg(req, rsp, new Message("ERROR", "û��Ȩ��"));
			return;
		}
		ArrayList scopeRight = new ArrayList();
		scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
		//�������з���Ȩ��
		scopeRight.addAll(mop.classScope);
		ArrayList allScopeList = this.getLoginBean(req).getAllScopeRight();
		if (allScopeList != null) {
			scopeRight.addAll(allScopeList);
		}
		ArrayList scopeRightUpdate = mop.getScope(MOperation.M_UPDATE);
		ArrayList scopeRightDel = mop.getScope(MOperation.M_DELETE);

		DefineReportBean defBean;
		try {
			defBean = DefineReportBean.parse(reportNumber + "SQL.xml", GlobalsTool.getLocale(req).toString(), lg.getId());
			ReportData reportData = new ReportData();
			req.setAttribute("mobileAjax", "true");//�������Ǵ��ֻ�ajax���Ĳ�ѯ
			Result rs = reportData.showData(mop, req, scopeRight, reportNumber, defBean, "", pageNo, pageSize, scopeRightUpdate, scopeRightDel, lg, null);
			if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				Message msg = new Message("ERROR", new ErrorMessage().toString(rs.retCode, GlobalsTool.getLocale(req).toString()));
				setMsg(req, rsp, msg);
				return;
			} else {
				MReportBean mrb = new MReportBean();
				mrb.setTotalPage(rs.getTotalPage());
				ArrayList<String[]> mcond = new ArrayList<String[]>();
				ArrayList<String[]> conditions = (ArrayList<String[]>) req.getAttribute("allConditions");
				for (String[] cond : conditions) {
					mcond.add(new String[] { cond[1], cond[2] });
				}
				mrb.setConditions(mcond);
				ArrayList<String[]> cols = (ArrayList<String[]>) req.getAttribute("cols");
				mrb.setCols(cols);
				ArrayList rows = new ArrayList();
				if (req.getAttribute("result") != null && isTable) {
					ArrayList<TableListResult> result = (ArrayList<TableListResult>) req.getAttribute("result");
					OAWorkFlowTemplate workFlowDesign = BaseEnv.workFlowInfo.get(tableName); //��������Ϣ
					for (TableListResult tl : result) {

						String workFlowNode = tl.getWorkFlowNode() == null ? "-1" : tl.getWorkFlowNode();
						HashMap row = new HashMap();
						String rowDis = tl.getRowDis();
						String[] rowDiss = rowDis.split("','");
						rowDiss[0]=rowDiss[0].substring(1);
						rowDiss[rowDiss.length -1] = rowDiss[rowDiss.length -1].substring(0,rowDiss[rowDiss.length -1].length()-1);
						for (int i = 0; i < cols.size(); i++) {
							row.put(cols.get(i)[4], rowDiss[i]);
						}
						row.put("f_brother", tl.getF_brother());
						row.put("classCode", tl.getClassCode());
						row.put("workFlowNode", workFlowNode);
						row.put("workFlowNodeName", tl.getWorkFlowNodeName());
						
						if (null != workFlowDesign && workFlowDesign.getTemplateStatus() == 1) {
							// �����ǰ�������Ѿ������̰汾����Ҫ���ݵ���id��ѯģ��id
							String designId = workFlowDesign.getId();
							if (!workFlowDesign.getId().equals(workFlowDesign.getSameFlow())) {
								designId = new PublicMgt().getDesignIdByKeyId((String) tl.getKeyId(), tableName);
								if (designId == null || designId.length() == 0) {
									designId = workFlowDesign.getId();
								}
							}
							WorkFlowDesignBean designBean = BaseEnv.workFlowDesignBeans.get(designId);
							FlowNodeBean curNode = null;
							if (designBean != null) {
								HashMap<String, FlowNodeBean> nodeMap = designBean.getFlowNodeMap();
								curNode = nodeMap.get(workFlowNode);
							}
							if ("draft".equals(tl.getWorkFlowNodeName())) {
								row.put("workFlowStatus", "�ݸ�");
							} else if (curNode != null) {
								row.put("workFlowStatus", curNode.getDisplay());
							}
						}
						row.put("keyId", tl.getKeyId());
						rows.add(row);
					}
				} else if (req.getAttribute("result") != null && !isTable) {
					ArrayList<Object[]> result = (ArrayList<Object[]>) req.getAttribute("result");
					for (Object[] tl : result) {
						String rowDis = tl[0] + "";
						
						String[] rowDiss = rowDis.split("','");
						rowDiss[0]=rowDiss[0].substring(1);
						rowDiss[rowDiss.length -1] = rowDiss[rowDiss.length -1].substring(0,rowDiss[rowDiss.length -1].length()-1);
						
						HashMap row = new HashMap();
						for (int i = 0; i < cols.size(); i++) {
							row.put(cols.get(i)[4], rowDiss[i]);
						}
						rows.add(row);
					}
				}
				mrb.setRows(rows);
				Message msg = new Message("OK","", mrb);
				setMsg(req, rsp, msg);
				return;
			}
		} catch (Exception e) {
			BaseEnv.log.error("MobileAjax.report Error:", e);
			setMsg(req, rsp, new Message("ERROR", e.getMessage()));
			return;
		}
	}

	/**
	 * ȡ�ֻ�����ҳ�Ĺ����б�������ӵ�ַ
	 * <br/><br/>
	 * ����ֵ: {code:OK,msg:�����б�}
	 */
	public void getHome(HttpServletRequest req, HttpServletResponse rsp) {
		LoginBean lb = this.getLoginBean(req);
		Message msg = new Message("OK","", req.getSession().getAttribute("MOBILEBODY"));
		setMsg(req, rsp, msg);
	}
	
	/**
	 * ȡ�ֻ��浱ǰ�û��˵�
	 * <br/><br/>
	 * ����ֵ: {code:OK,msg:�����б�}
	 */
	public void getMenu(HttpServletRequest req, HttpServletResponse rsp) {
		LoginBean lb = this.getLoginBean(req);
		ArrayList list = new ArrayList();
		for(MOperation mop :(Collection<MOperation>)( lb.getOperationMap().values())){
			System.out.println("================"+mop.moduleUrl);
			if(((Integer)1).equals(mop.isMobile)){
				HashMap map = new HashMap();
				map.put("icon", mop.icon);
				map.put("url", mop.moduleUrl);
				list.add(map);
			}
		}
		Message msg = new Message("OK","", list);
		setMsg(req, rsp, msg);
	}
	/**
	 * ���ǰ׼���ӿڣ�������˽�㣬����˵���Ϣ��
	 * 
	 * @param keyId ��˵���ID
	 * @param tableName ��˱���
	 * <br/><br/>
	 * ����ֵ: {code:OK,obj:{nextNodes����һ������б������ж����currNode����ǰ�����Ϣ��designId��������ģ��ID��moduleName��ģ�����֣�keyId����������Ӧ����ID��tableName������dispenseWake������ַ���Ϣ}}
	 */
	public void deliverToPrepare(HttpServletRequest req, HttpServletResponse rsp) {
		String keyId = req.getParameter("keyId");
		String tableName = req.getParameter("tableName");
		LoginBean loginBean = this.getLoginBean(req);
		//������ֻ������Ϣ�����г��֣����������Ӳ�����˲���
		String outNodeId = req.getParameter("currNodeId");
		
		Message msg = AIOConnect.deliverToPrepare(getLocale(req).toString(), loginBean, keyId, tableName, outNodeId);
		setMsg(req, rsp, msg);
		return;
	}
	
	/**
	 * ��˵���һ���ڵ㣬���ںܶ�����û�ϣ��ֱ�ӽ�����һ��˽�㣬
	 * 
	 * @param keyId ��˵���ID
	 * @param tableName ��˱���
	 * <br/><br/>
	 * ����ֵ: {code:'OKִ�гɹ���CONFIRM������ȷ�Ͽ�ERROR��ִ�д���',msg����ʾ��Ϣ}
	 */
	public void deliverToNext(HttpServletRequest req, HttpServletResponse rsp) {
		String keyId = req.getParameter("keyId");
		String tableName = req.getParameter("tableName");
		LoginBean loginBean = this.getLoginBean(req);
		
		Message msg = AIOConnect.deliverToNext(getLocale(req).toString(), loginBean, keyId, tableName);
		setMsg(req, rsp, msg);
		return;
	}
	
	/**
	 * ɾ���Զ��嵥�ݣ�
	 * 
	 * @param keyId ����ID
	 * @param tableName ����
	 * <br/><br/>
	 * <br/><br/>
	 * ����ֵ: {code:'OKִ�гɹ���CONFIRM������ȷ�Ͽ�ERROR��ִ�д���',msg����ʾ��Ϣ}
	 */
	public void delete(HttpServletRequest request, HttpServletResponse rsp) {

		ActionForward forward = null;
		String tableName = request.getParameter("tableName");
		String keyId = request.getParameter("keyId");
		String locale = getLocale(request).toString();
		LoginBean loginBean = getLoginBean(request);
		Message msg = AIOConnect.delete(locale, loginBean, tableName, keyId);
		setMsg(request, rsp, msg);
		return;
	}
	
	/**
	 * ����˵��ݣ�
	 * 
	 * @param keyId ����ID
	 * @param tableName ����
	 * <br/><br/>
	 * ����ֵ: {code:'OKִ�гɹ���CONFIRM������ȷ�Ͽ�ERROR��ִ�д���',msg����ʾ��Ϣ}
	 */
	public void retCheck(HttpServletRequest request, HttpServletResponse rsp) {

		String keyId = request.getParameter("keyId");
		String tableName = request.getParameter("tableName");
		String locale = getLocale(request).toString();
		LoginBean loginBean = getLoginBean(request);
		Message msg = AIOConnect.retCheck(locale, loginBean, keyId, tableName);
		this.setMsg(request, rsp, msg);
		return;
	}
	/**
	 * ������ˣ�
	 * 
	 * @param keyId ����ID
	 * @param tableName ����
	 * <br/><br/>
	 * ����ֵ: {code:'OKִ�гɹ���CONFIRM������ȷ�Ͽ�ERROR��ִ�д���',msg����ʾ��Ϣ}
	 */
	public void cancelTo(HttpServletRequest req, HttpServletResponse rsp) {
		String keyId = req.getParameter("keyId"); /* ����ID */
		String tableName = req.getParameter("tableName"); /* ��ǰ������ */
		LoginBean loginBean = this.getLoginBean(req);
		String locale = getLocale(req).toString();
		Message msg = AIOConnect.cancelTo(locale, loginBean, keyId, tableName);
		this.setMsg(req, rsp, msg);
		return;
	}
	
	/**
	 * ��˵��ݣ�
	 * 
	 * @param keyId ����ID
	 * @param tableName ����
	 * @param nextStep �¸��������
	 * @param currNode ��ǰ�������
	 * @param designId ������ģ��ID
	 * @param checkPerson ѡ���������
	 * @param deliverance �������
	 * @param popedomUserIds �ַ�������
	 * @param popedomDeptIds �ַ����Ѳ���
	 * @param oaTimeLimit ��ʱ���� �磺10Сʱ��10��
	 * @param oaTimeLimitUnit ��ʱ��λ
	 * <br/><br/>
	 * ����ֵ: {code:'OKִ�гɹ���CONFIRM������ȷ�Ͽ�ERROR��ִ�д���',msg����ʾ��Ϣ}
	 */
	public void deliverTo(HttpServletRequest req, HttpServletResponse rsp) {
		try {
			String keyId = req.getParameter("keyId"); /*����ID*/
			String nextStep = req.getParameter("nextStep"); /*�¸��������*/
			String currNode = req.getParameter("currNode"); /*��ǰ�������*/
			String designId = req.getParameter("designId"); /*����ID*/
			String type = ""; 
			String tableName = req.getParameter("tableName"); /*��ǰ������*/

			String checkPerson = req.getParameter("checkPerson"); //ѡ���������
			String deliverance = req.getParameter("deliverance"); //�������
			deliverance = deliverance == null ? "" : deliverance;
			String userIds = req.getParameter("popedomUserIds"); //�ַ�������
			String deptIds = req.getParameter("popedomDeptIds"); //�ַ����Ѳ���
			String oaTimeLimit = req.getParameter("oaTimeLimit"); //��ʱ���� �磺10Сʱ��10�� 
			String oaTimeLimitUnit = req.getParameter("oaTimeLimitUnit"); //��ʱ��λ


			LoginBean loginBean = this.getLoginBean(req);
			String locale = getLocale(req).toString();
			
			Message msg =AIOConnect.deliverTo(locale, loginBean, keyId, tableName, nextStep, currNode, designId, checkPerson, deliverance,
					userIds, deptIds, oaTimeLimit, oaTimeLimitUnit);
			
			this.setMsg(req, rsp, msg);
			return;
		} catch (Exception e) {
			this.setMsg(req, rsp, setMsg("ERROR", e.getMessage()));
			return;
		}
	}
	
	
	
	/**
	 * ����ǰҪִ��һ��У�鶯����ͨ���޸ı��浥����ʵ�ִ˹��ܣ���ȷ��ϵͳ�ĸ���У���Ƿ�ɹ�������ڵ���˽ӿ�ǰ�Ѿ�ִ�����޸Ķ��������Բ�ִ���������������һ��Ҫִ��ȷ��������ȷ�ԣ�
	 * 
	 * @param keyId ����ID
	 * @param tableName ����
	 * @param defineInfo ���Զ������е�����ѡȷ�Ͽ򣬻��Զ�����һ���ֶ����ʶ���룬��ԭ�Ĵ��롣Ĭ��Ϊ��
	 * <br/><br/>
	 * ����ֵ: {code:'OKִ�гɹ���CONFIRM������ȷ�Ͽ�ERROR��ִ�д���',msg����ʾ��Ϣ}
	 */
	public void check(HttpServletRequest req, HttpServletResponse rsp) {
		try {
			String keyId = req.getParameter("keyId"); /*����ID*/
			String tableName = req.getParameter("tableName"); /*��ǰ������*/
			String defineInfo=req.getParameter("defineInfo");
			LoginBean loginBean = this.getLoginBean(req);
			String locale = getLocale(req).toString();
			
			Message msg =AIOConnect.check(locale, loginBean, keyId, tableName, defineInfo);
			this.setMsg(req, rsp,msg);
			return;
		} catch (Exception e) {
			this.setMsg(req, rsp, setMsg("ERROR", e.getMessage()));
			return;
		}
	}

	private static MessageResources getResources(HttpServletRequest req) {

		return (MessageResources) req.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
	}

	
	/**
	 * ��¼�ӿ�
	 * 
	 * @param name ��½�ʺ�
	 * @param password ���루MD5���ܣ�
	 * <br/><br/>
	 * ����ֵ: {code:'OKִ�гɹ���ERROR��ִ�д���',msg����ʾ��Ϣ}
	 */
	public void login(HttpServletRequest req, HttpServletResponse rsp) {
		String userName = req.getParameter("name");
		String password = req.getParameter("password");
		Result result = userMgt.login(userName, password);
		Message msg = loginAuth(req, rsp, result, userName);
		if ( msg.getCode().equals("OK") && req.getSession().getAttribute("wxopenid") != null ) // ��΢��openid
			userMgt.updateUserOpenid(userName, password, req.getSession().getAttribute("wxopenid").toString());
		setMsg(req, rsp, msg);
	} 

	/**
	 * ��¼�ӿڣ����ӿڲ����⿪�ţ�
	 * @param req
	 * @param rsp
	 * @param result
	 * @param userName
	 * @return
	 */

	public Message loginAuth(HttpServletRequest req, HttpServletResponse rsp, Result result, String userName) {
		//�ж��û����Ƿ񳬹�����
		if (SystemState.instance.userState != 0) {
			if (SystemState.instance.userState == SystemState.DOG_ERROR_USER) {
				return setMsg("ERROR", "ERPϵͳ�û��������������(" + SystemState.instance.userNum + "), \\n���ڵ��԰���admin�ʺŵ�½ɾ������ϵͳ�û�������ϵͳ");
			} else if (SystemState.instance.userState == SystemState.DOG_ERROR_USER_OA) {
				return setMsg("ERROR", "OAϵͳ�û��������������(" + SystemState.instance.oaUserNum + "), \\n���ڵ��԰���admin�ʺŵ�½ɾ������ϵͳ�û�������ϵͳ");
			} else if (SystemState.instance.userState == SystemState.DOG_ERROR_USER_CRM) {
				return setMsg("ERROR", "CRMϵͳ�û��������������(" + SystemState.instance.crmUserNum + "), \\n���ڵ��԰���admin�ʺŵ�½ɾ������ϵͳ�û�������ϵͳ");
			}
		}
		//������֤�ɹ�
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List userList = (ArrayList) result.retVal;
			if (userList.size() > 0) {
				EmployeeBean userBean = (EmployeeBean) userList.get(0);

				//�����û�Ȩ��
				//������ֻ���ҽ�ɫȨ��
				//�������Ա�����ò��ɫ��ֱ��ӵ������Ȩ��
				/**
				 * ȡȨ��
				 */
				ArrayList roleModuleList = new ArrayList();
				HashMap roleModuleScopeMap = new HashMap();
				ArrayList roles = new ArrayList();
				ArrayList allScopeRight = new ArrayList(); //��¼Ӧ��������ģ��ķ�ΧȨ��
				String hiddenField = "";
				if (!"admin".equalsIgnoreCase(userBean.getSysName())) {
					roles = (ArrayList) LoginAction.getRoles(userBean, "1");
					//�û�����Ҳ��һ����ɫ
					RoleBean selfRb = new RoleBean();
					selfRb.setId(userBean.getId());
					selfRb.setRoleName(userBean.getEmpFullName());
					selfRb.setRoleDesc(userBean.getEmpFullName());
					selfRb.setHiddenField(userBean.getHiddenField());
					roles.add(selfRb);
					RoleMgt roleMgt = new RoleMgt();

					for (Object o : roles) {
						RoleBean rb = (RoleBean) o;
						hiddenField += rb.getHiddenField() + ",";

						//��ѯ��ɫģ��Ȩ��
						result = roleMgt.queryRoleModuleByRoleid(rb.getId(), userBean.getId());
						if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
							new LogManageMgt().addLog(userBean.getId(), userBean.getEmpFullName(), "LOGIN", null, "FAIL", GlobalsTool.getMessage(req, "login.msg.loginError"), req);
							return setMsg("ERROR", GlobalsTool.getMessage(req, "login.msg.loginError"));
						} else {
							//�ϲ�����ģ��Ȩ��
							roleModuleList.addAll((List) result.getRetVal());
						}

						//��ѯ��ɫ��ΧȨ��
						result = roleMgt.queryRoleScopyByRoleid(rb.getId(), userBean.getId(), userBean.getEmpFullName(), userBean.getDepartmentCode());
						if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
							new LogManageMgt().addLog(userBean.getId(), userBean.getEmpFullName(), "LOGIN", null, "FAIL", GlobalsTool.getMessage(req, "login.msg.loginError"), req);
							return setMsg("ERROR", GlobalsTool.getMessage(req, "login.msg.loginError"));
						} else {
							//�ϲ����з�ΧȨ��
							HashMap hm = (HashMap) result.getRetVal();
							for (Object hmo : hm.keySet()) {
								ArrayList list = (ArrayList) roleModuleScopeMap.get(hmo);
								if (list == null) {
									roleModuleScopeMap.put(hmo, hm.get(hmo));
								} else {
									list.addAll((List) hm.get(hmo));
								}
							}
						}
					}

					//��ѯӦ��������ģ�鷶ΧȨ��------------------------------------
					result = roleMgt.queryAllModScope(userBean.getId(), userBean.getDepartmentCode());
					if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
						allScopeRight = (ArrayList) result.retVal;
					}
				}

				LoginBean loginBean = OnlineUserInfo.getLoginBean(userBean.getId(), userBean.getSysName());
				loginBean.setHiddenField(hiddenField); //�����ɫ�����������Ϣ��
				loginBean.setMobile(userBean.getMobile());
				loginBean.setDefSys(userBean.getDefSys());
				loginBean.setLoginTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
				loginBean.setRoleModuleList(roleModuleList);
				loginBean.setRoleModuleScopeMap(roleModuleScopeMap);
				loginBean.setAllScopeRight(allScopeRight);
				loginBean.setEmpFullName(userBean.getEmpFullName());
				loginBean.setLbxMonitorCh(userBean.getLbxMonitorCh());//����ͨ���趨
				loginBean.setTelPrefix(userBean.getTelPrefix());//�绰ǰ׺
				loginBean.setTelAreaCode(userBean.getTelAreaCode());//�û���λ
				loginBean.setTelpop(userBean.getTelpop());
				loginBean.setDepartCode(userBean.getDepartmentCode());
				loginBean.setShopName(userBean.getShopName());
				loginBean.setShopPwd(userBean.getShopPwd());
				loginBean.setDefaultPage(userBean.getDefaultPage());
				loginBean.setExtension(userBean.getExtension());
				loginBean.setViewLeftMenu(userBean.getViewLeftMenu());
				loginBean.setViewTopMenu(userBean.getViewTopMenu());
				loginBean.setJessionid(req.getSession().getId());
				loginBean.setDefStyle("1");
				loginBean.setModuleId(userBean.getModuleId());
				loginBean.setTitleId(userBean.getTitleID());
				loginBean.setDefDesk(userBean.getDefDesk());
				loginBean.setEmail(userBean.getEmail());
				loginBean.setQq(userBean.getQq());
				loginBean.setShowDesk(userBean.getShowDesk());//��ʾ����
				loginBean.setFirstShow(userBean.getFirstShow());
				loginBean.setShowWebNote(userBean.getShowWebNote());
				for (Object o : SystemState.instance.moduleList) {
					if (o.toString().equals("1") && userBean.getCanJxc() == 1) {
						loginBean.setCanJxc(1);
					}
					if (o.toString().equals("2") && userBean.getCanOa() == 1) {
						loginBean.setCanOa(1);
					}
					if (o.toString().equals("3") && userBean.getCanCrm() == 1) {
						loginBean.setCanCrm(1);
					}
					if (o.toString().equals("4") && userBean.getCanHr() == 1) {
						loginBean.setCanHr(1);
					}
				}
				if (SystemState.instance.funOrders && userBean.getCanOrders() == 1) {
					loginBean.setCanOrders(1);
				}
				if (userBean.getPhoto() != null && userBean.getPhoto().contains(":")) {
					loginBean.setPhoto(userBean.getPhoto().split(":")[0]);
				} else {
					loginBean.setPhoto(userBean.getPhoto());
				}
				loginBean.setSunCompany("00001");

				//���û�bean����session��
				req.getSession().removeAttribute("LoginBean");
				req.getSession().setAttribute("LoginBean", loginBean);
				//�ж�ϵͳ�������Ƿ������ʼ���ַ
				boolean HideEmail = (boolean) GlobalsTool.getHideEmail();
				req.getSession().setAttribute("HideEmail", HideEmail);
				//����������Ա�б�
				String userAgent = req.getHeader("user-agent");
				//����΢����ҵ�ŵ�½
				WeixinApiMgt.getLoginBean(req, loginBean, userBean);
				OnlineUserInfo.wxqyUserLogin(loginBean, req.getSession().getId());
				//��ǰ��¼��֧�����Ļ���ڼ�
				String nowPeriod;
				int nowYear = -1;
				int nowMonth = -1;
				AccPeriodBean bean = null;
				Result rs = new SysAccMgt().getCurrPeriod(loginBean.getSunCmpClassCode());
				if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
					nowPeriod = "-1";
				} else {
					bean = (AccPeriodBean) rs.getRetVal();
					nowPeriod = String.valueOf(bean.getAccPeriod());
					nowYear = bean.getAccYear();
					nowMonth = bean.getAccMonth();
				}

				AccPeriodBean AccBean = null;
				rs = new SysAccMgt().getCurrAccPeriod(loginBean.getSunCmpClassCode());
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					AccBean = (AccPeriodBean) rs.getRetVal();
				}

				int nowDay = Calendar.getInstance().get(Calendar.DATE);

				//��¼�ɹ�ʱ����һЩsess��Ϣ�ŵ�BaseEnv��
				if (BaseEnv.sessionSet == null) {
					BaseEnv.sessionSet = new Hashtable();
				}
				DynDBManager dbmgt = new DynDBManager();
				//ְԱ����ID
				String groupIds = (String) dbmgt.getGroupIds(userBean.getId()).getRetVal();
				loginBean.setGroupId(groupIds);
				boolean isLastSunCompany = dbmgt.getChildCount("tblSunCompanys.classCode", loginBean.getSunCmpClassCode()).getRetVal().toString().equals("0");
				Hashtable sessionSet = BaseEnv.sessionSet;
				Hashtable sess = new Hashtable();
				loginBean.setSessMap(sess);
				sess.put("SCompanyID", loginBean.getSunCmpClassCode());
				sess.put("IsLastSCompany", isLastSunCompany);
				sess.put("AccPeriod", bean);
				sess.put("AccPeriodAcc", AccBean);
				sess.put("NowPeriod", nowPeriod);
				sess.put("NowYear", nowYear);
				if (nowYear == -1) {//���ڲ�ѯ������Ĭ��ֵ
					sess.put("NowPeriodQ", "");
					sess.put("NowYearQ", "");
				} else {
					sess.put("NowPeriodQ", nowPeriod);
					sess.put("NowYearQ", nowYear);
				}
				sess.put("NowMonth", nowMonth);
				sess.put("NowDay", nowDay);
				sess.put("DepartmentCode", userBean.getDepartmentCode() == null ? "" : userBean.getDepartmentCode());
				if (userBean.getDepartmentCode() != null && userBean.getDepartmentCode().length() > 0) {
					Result rss = dbmgt.getDepartMent(userBean.getDepartmentCode());
					if (rss.retCode == ErrorCanst.DEFAULT_SUCCESS && rss.getRetVal() != null) {
						String[] depart = (String[]) rss.getRetVal();
						sess.put("DepartmentName", depart[0]);
						loginBean.setDepartmentName(depart[0]);
						loginBean.setDepartmentManager(depart[1]);
					}
				}
				sess.put("UserId", loginBean.getId());
				sess.put("UserName", loginBean.getEmpFullName());
				sess.put("Local", GlobalsTool.getLocale(req));
				sess.put("BillOper", "");
				sessionSet.put(loginBean.getId(), sess);
				rs = dbmgt.getAccPeriodOverYear();
				sessionSet.put("AccPeriodOverYear", rs.getRetVal());

				req.getSession().setAttribute("NowPeriod", nowPeriod);
				req.getSession().setAttribute("NowYear", nowYear);
				req.getSession().setAttribute("SCompanyID", loginBean.getSunCmpClassCode());

				//��¼������ʽ���
				loginBean.setDefStyle("1");
				loginBean.setDefLoc("zh_CN");
				GlobalMgt glmgt = new GlobalMgt();
				LoginAction.parseDefValSet(loginBean.getId(), sess);

				//  �����ڴ��и��û��ĵ�¼״̬����ʱͨѶ�ͻ�����Ҫ�õ�
				EmployeeItem loginItem = MSGConnectCenter.employeeMap.get(loginBean.getId());
				if (loginItem != null && loginItem.loginStatus == EmployeeItem.OFFLINE) {
					loginItem.loginType = EmployeeItem.MOBILE_LOGIN;
					loginItem.loginStatus = EmployeeItem.ONLINE;
					MSGConnectCenter.userStatus(loginItem.userId, loginItem.loginType, loginItem.loginStatus);
				}
				req.getSession().setAttribute("DEVICE", "mobile");
				//��¼��½��־
				new LogManageMgt().addLog(loginBean.getId(), loginBean.getEmpFullName(), "LOGIN", null, "SUCCESS", "��ҳ�ֻ���½", req);

				try {//��ȡ�û��Զ���˵������session ���� MOBILEBODY
					ArrayList retObj = new ArrayList();
					File file = new File(req.getRealPath("mobile/define/config.ini"));
					if(file.exists()){ 
						java.io.BufferedReader br = new java.io.BufferedReader(new FileReader(file));
						String b = null;
						
						while ((b = br.readLine()) != null) {
							b = b.trim();
							if (!b.equals("") && !b.startsWith("#")) {
								String bs[] = b.split(",");
								if (bs.length >= 5) {//(add,update,query,print)
									MOperation mop = (MOperation) loginBean.getOperationMap().get(bs[3]);
									if (mop != null) {
										if (bs[4].equals("add")) {
											if (mop.add) {
												retObj.add(new String[] { bs[0], bs[1], bs[2] });
											}
										} else if (bs[4].equals("update")) {
											if (mop.update) {
												retObj.add(new String[] { bs[0], bs[1], bs[2] });
											}
										} else if (bs[4].equals("query")) {
											if (mop.query) {
												retObj.add(new String[] { bs[0], bs[1], bs[2] });
											}
										} else if (bs[4].equals("print")) {
											if (mop.query) {
												retObj.add(new String[] { bs[0], bs[1], bs[2] });
											}
										}
									}
								}
							}
						}
					}
					req.getSession().setAttribute("MOBILEBODY", retObj);
				} catch (Exception e1) {
					BaseEnv.log.error("MobileAjax.login =", e1);
				}

				return setMsg("OK", "��½�ɹ�");
			} else {
				return setMsg("ERROR", GlobalsTool.getMessage(req, "login.msg.namepassworderror"));
			}
		}
		//������û�������
		else if (result.retCode == ErrorCanst.RET_NAME_PSW_ERROR) {
			new LogManageMgt().addLog("", userName, "LOGIN", null, "FAIL", GlobalsTool.getMessage(req, "login.msg.namepassworderror"), req);
			return setMsg("ERROR", GlobalsTool.getMessage(req, "login.msg.namepassworderror"));
		}
		// ������û�������
		else if (result.retCode == ErrorCanst.TIMER_COMPARE_ERROR) {
			new LogManageMgt().addLog("", userName, "LOGIN", null, "FAIL", GlobalsTool.getMessage(req, "login.msg.loginTimeError") + result.retVal, req);
			return setMsg("ERROR", GlobalsTool.getMessage(req, "login.msg.loginTimeError") + result.retVal);
		}// �û�ͣ��
		else if (result.retCode == ErrorCanst.USER_STOP) {
			new LogManageMgt().addLog("", userName, "LOGIN", null, "FAIL", "���û��ʺ���ͣ��", req);
			return setMsg("ERROR", "���û��ʺ���ͣ��");
		}
		//��������
		else {
			new LogManageMgt().addLog("", userName, "LOGIN", null, "FAIL", GlobalsTool.getMessage(req, "login.msg.loginError"), req);
			return setMsg("ERROR", GlobalsTool.getMessage(req, "login.msg.loginError"));
		}
	}

	/**
	 * ���json���
	 * @param req
	 * @param rsp
	 * @param code
	 * @param msg
	 */
	private Message setMsg(String code, String msg) {
		return new Message(code, msg);
	}

	/**
	 * ���json���
	 * @param req
	 * @param rsp
	 * @param code
	 * @param msg
	 */
	private void setMsg(HttpServletRequest req, HttpServletResponse rsp, Message msgObj) {
		try {
			String msgStr = gson.toJson(msgObj);
			respMsg(req, rsp, msgStr);
		} catch (Exception e) {
			BaseEnv.log.error("MobileAjax.setMsg", e);
		}
	}

	/**
	 * ���json���
	 * @param req
	 * @param rsp
	 * @param code
	 * @param msg
	 */
	private void respMsg(HttpServletRequest req, HttpServletResponse rsp, String msgStr) {
		try {
			if (BaseEnv.log.isDebugEnabled()) { //��ӡ������Ϣ
				String uName = "";
				if (this.getLoginBean(req) != null) {
					uName = this.getLoginBean(req).getEmpFullName();
				}
				BaseEnv.log.debug("MobileAjax ������:" + uName + ";" + "�������ݣ�" + (msgStr.length() > 500 ? msgStr.substring(0, 500) + "�ȵȣ�" : msgStr));
			}
			rsp.getOutputStream().write(msgStr.getBytes("UTF-8"));
		} catch (Exception e) {
			BaseEnv.log.error("MobileAjax.respMsg", e);
		}
	}

	/**
	 * ��ȡ�Ự�û�
	 * @param request
	 * @return
	 */
	private LoginBean getLoginBean(HttpServletRequest req) {
		Object o = req.getSession().getAttribute("LoginBean");
		if (o != null) {
			return (LoginBean) o;
		}
		return null;
	}

	/**
	 * ��ӡ������Ϣ
	 * @param request
	 */
	private void debugRequest(HttpServletRequest req) {
		if (BaseEnv.log.isDebugEnabled()) {
			String uName = "";
			if (this.getLoginBean(req) != null) {
				uName = this.getLoginBean(req).getEmpFullName();
			}
			BaseEnv.log.debug("MobileAjax �����ַ��" + req.getRequestURI() + (req.getQueryString() == null ? "" : "?" + req.getQueryString()));
			String rd = "";
			for (Object key : req.getParameterMap().keySet()) {
				Object value = req.getParameterMap().get(key);
				String values = "";
				for (String v : (String[]) value) {
					values += v + ",";
				}
				rd += key + "=[" + values + "];";
			}

			BaseEnv.log.debug("MobileAjax ������:" + uName + ";" + "�������ݣ�" + rd);
		}		
	}

	/**
	 * ����ͬ��ͨѶ¼����,���ز������ҳ�棨���ӿڲ����⿪�ţ�
	 * @param request
	 * @param response
	 * @param isWXWork �Ƿ�����ҵ΢��
	 **/
	public void sync(HttpServletRequest request, HttpServletResponse response) {
		String keyName = request.getParameter("KeyName");
		if (keyName == null || keyName.trim().equals(""))
			return ;
		HashMap<String, Object> result = new HashMap<String, Object>();
		try {
			List<SyncDepartmentBean> departmentlist = getDepartment();
			List<SyncEmployeeBean> employeelist = getEmployee();
			HashMap<String,String> deptMap  = new HashMap<String,String>();
			List<SyncResultBean> departmentResultList = new ArrayList<SyncResultBean>();
			List<SyncResultBean> employeeResultList = new ArrayList<SyncResultBean>();
			departmentResultList = WXWorkSyncContact.syncDepartment(departmentlist, deptMap, keyName);
			employeeResultList =  WXWorkSyncContact.syncEmployee(employeelist,deptMap, keyName);	
			result.put("employee", employeeResultList);
			result.put("department", departmentResultList);
			result.put("result", "ok");
			
			int nullerror = 0;
			int otherError=0;
			for(SyncResultBean sb:employeeResultList){
				if(sb.getErrcode().equals("60113")){
					nullerror ++;
				}else{
					otherError ++;
				}
			}
			String resultMsg = "ͬ���ɹ���"+(nullerror > 0 ? "�ֻ���Ϊ��"+nullerror+"��":"" )+(otherError > 0 ? "��������"+otherError+"��":"" );
			result.put("resultMsg", resultMsg);
		} catch (Exception e) {
			BaseEnv.log.error("MobileAjax.sync Error",e);
			result.put("result", "error");
			result.put("resultMsg", e.getMessage());
		}
		try{
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", -10);
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			response.getWriter().print(new Gson().toJson(result));
		} catch (Exception e) {
			BaseEnv.log.error("MobileAjax.sync Error",e);
		}
	}
	
    /**
     * ��ȡjsticket
     * @param request
     * @param response
     */
	public void getJsticket(HttpServletRequest request, HttpServletResponse response) {	
		String state = request.getParameter("wxstate");
		String url = request.getParameter("url");
		Message msg ;
		if(state == null || WXSetting.getInstance().getSettingBase(state) == null) {
			msg = new Message("ERROR", "�Ҳ�����ص�΢��������Ϣ");
		} else {
			msg = new Message("OK", "", WXSetting.getInstance().getJsapiSignature(state, url));
		}
		this.setMsg(request, response, msg);
	}	
	
	/**
	 * ��ȡ��½�û���ְԱ��Ϣ
	 * @param request
	 * @param response
	 */
	public void getSessEmp(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		LoginBean bean = this.getLoginBean(request);
		data.put("id", bean.getId());
		data.put("name", bean.getName());
		data.put("departmentName", bean.getDepartmentName());
		data.put("titleId", bean.getTitleId());
		Message msg = new Message("OK", "", data);
		this.setMsg(request, response, msg);
	}	
	
	/**
	 * ��ȡ���в���
	 * @return
	 */
	private List<SyncDepartmentBean> getDepartment() {
		final String sql = "SELECT classcode,DeptFullName,id FROM tblDepartment where statusId=0 ORDER BY classcode ";
		final List<SyncDepartmentBean> rst = new ArrayList<SyncDepartmentBean>();
		DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							Connection conn = connection;
							PreparedStatement pstmt = conn.prepareStatement(sql);
							ResultSet rs = pstmt.executeQuery();
							while (rs.next()) {
								SyncDepartmentBean bean = new SyncDepartmentBean();
								bean.setClasscode(rs.getString("classcode"));
								bean.setDeptFullName(rs.getString("DeptFullName"));
								bean.setId(rs.getString("id"));
								rst.add(bean);
							}

						} catch (Exception ex) {
							BaseEnv.log.error(sql, ex);
							return;
						}
					}
				});
				return 1;
			}
		});
		return rst;
	}

	/**
	 * ��ȡ���г�Ա
	 * @return
	 */
	private List<SyncEmployeeBean> getEmployee() {  
		final String sql = "select a.id,a.departmentCode,a.email,a.mobile,a.sysName,a.titleId,a.EmpFullName,a.tel,b.DeptFullName from tblemployee a left join " +
				" tbldepartment b on a.DepartmentCode=b.classcode where a.statusId=0 and openflag=1 and len(isnull(a.mobile,''))>0 order by a.isPublic";
		final List<SyncEmployeeBean> rst = new ArrayList<SyncEmployeeBean>();
		DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							Connection conn = connection;
							PreparedStatement pstmt = conn.prepareStatement(sql);
							ResultSet rs = pstmt.executeQuery();
							while (rs.next()) {
								SyncEmployeeBean bean = new SyncEmployeeBean();
								bean.setDeptFullName(rs.getString("DeptFullName"));
								bean.setId(rs.getString("id"));
								bean.setDepartmentCode(rs.getString("departmentCode"));
								bean.setEmail(rs.getString("email"));
								bean.setMobile(rs.getString("mobile"));
								bean.setSysName(rs.getString("sysName"));
								bean.setTitleId(rs.getString("titleId"));
								bean.setEmpFullName(rs.getString("EmpFullName"));
								bean.setTel(rs.getString("tel"));
								rst.add(bean);
							}

						} catch (Exception ex) {
							BaseEnv.log.error(sql, ex);
							return;
						}
					}
				});
				return 1;
			}
		});
		return rst;
	}

	private void sendMsg(HttpServletRequest request) {
		String key = "koronaio";
		String signPre = "";

		String userId = request.getParameter("userId");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String receiveIds = request.getParameter("receiveIds");
		String relationId = request.getParameter("relationId");
		String type = request.getParameter("type");
		String timeStamp = request.getParameter("timeStamp");
		signPre += "timeStamp" + timeStamp;
		signPre += key;
		String sign = request.getParameter("sign");
		if (sign.equals(MD5(signPre))) {
			new AdviceMgt().add(userId, title, content, receiveIds, relationId, type);
		}
	}

	private String MD5(String source) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source.getBytes());
			byte[] b = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte c : b) {
				int val = ((int) c) & 0xff;
				if (val < 16)
					sb.append("0");
				sb.append(Integer.toHexString(val));
			}
			return sb.toString().toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Locale getLocale(HttpServletRequest req) {
		Locale loc = null;
		Object obj = req.getSession(true).getAttribute(org.apache.struts.Globals.LOCALE_KEY);
		if (obj != null) {
			loc = (Locale) obj;
			return loc;
		}
		if (loc == null) {
			loc = req.getLocale();
		}
		if (loc == null) {
			loc = Locale.getDefault();
		}
		return loc;
	}
}
