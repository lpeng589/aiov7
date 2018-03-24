package com.menyi.web.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.bsf.BSFException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.struts.action.ActionForward;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.ColConfigBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.GlobalMgt;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.mobile.DetailBean;
import com.menyi.aio.web.mobile.MFlowNodeBean;
import com.menyi.aio.web.mobile.Message;
import com.menyi.aio.web.sysAcc.ReCalcucateMgt;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.msgcenter.server.MSGConnectCenter;
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
public class AIOConnect extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Gson gson;
	static { 
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}

	private static UserMgt userMgt = new UserMgt();

	private static DynDBManager dbmgt = new DynDBManager();

	private static UserFunctionMgt userFunMgt = new UserFunctionMgt();

	private static OAMyWorkFlowMgt oamgt = new OAMyWorkFlowMgt();

	private static PublicMgt pcmgt = new PublicMgt();
	
	public static String strDefaultKey = "laozhouzxy"; 
	public static DES des=new DES();

	
    /**
	 * �������߰�
	 * 
	 * @param tableName  ����
	 * @param keyId ����ID
	 * <br/><br/>
	 * ����ֵ: {code:OK,msg:�ݰ�ɹ�}
	 */
    public static Message hurryTrans(String locale,LoginBean loginBean,String keyId, String tableName,String content,String wakeType) {
		OAMyWorkFlowMgt mgt=new OAMyWorkFlowMgt();
		HashMap map=mgt.getOAMyWorkFlowInfo(keyId,tableName);
		
		if(map!=null){
			String createBy=map.get("createBy").toString();
			String applyType=map.get("applyType").toString();
			String checkPerson=map.get("checkPerson").toString();
			String nextNodeIds=map.get("nextNodeIds").toString();
			String currNodeId=map.get("currentNode").toString();
			String oaTimeLimit=map.get("benchmarkTime").toString();
			String oaTimeLimitUnit=map.get("oaTimeLimitUnit").toString();
			
			String applyBy=OnlineUserInfo.getUser(createBy).getName();
			wakeType=wakeType==null?"":wakeType;
			mgt.hurryTrans(applyBy,applyType,checkPerson,keyId,nextNodeIds,currNodeId,oaTimeLimit,oaTimeLimitUnit,
					new Locale(locale),"",content,wakeType,"hurryTrans","",loginBean,"",getResources());
		
    	}
		Message msg = new Message("OK", "�ݰ�ɹ�");
		return msg;
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
	public static Message update(String locale,LoginBean loginBean,String tableName, String saveType,
			String defineInfo,String valuesJson) {
		
		HashMap<String, String> values = gson.fromJson(valuesJson, HashMap.class);

		Result rs = new Result();
		Hashtable map = BaseEnv.tableInfos;

		//�����ֵܱ��ID
		String f_brother=values.get("f_brother");
		f_brother = f_brother == null ? "" : f_brother;
		String moduleType = values.get("moduleType");

		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, tableName);
		LoginBean lg = loginBean;

		OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(tableName);
		String currNodeId = "";
		String strCheckPersons = "";
		String designId = "";
		if (workFlow != null && workFlow.getTemplateStatus() == 1) {
			HashMap oaMap = pcmgt.getOAMyWorkFlowInfo(String.valueOf(values.get("id")), tableName);
			if (oaMap != null) {
				designId = String.valueOf(oaMap.get("applyType"));
				strCheckPersons = String.valueOf(oaMap.get("checkPerson"));
				currNodeId = String.valueOf(oaMap.get("currentNode"));
				/*�鿴��ǰ���������û��ί�и��ҵ�*/
				HashMap<String, String> consignMap = OAMyWorkFlowMgt.queryConsignation(lg.getId(), designId);
				for (String person : strCheckPersons.split(";")) {
					if (consignMap.get(person) != null) {
						strCheckPersons += lg.getId() + ";";
					}
				}
			}
			if ("-1".equals(currNodeId) || (strCheckPersons.length() > 0 && !strCheckPersons.contains(";" + lg.getId() + ";")) && !"0".equals(currNodeId)) {
				Message msg = new Message("OK", GlobalsTool.getMessage(locale, "common.msg.hasAudit"));
				return msg;
			}
			
		}
		String sysParamter = tableInfo.getSysParameter();

		//�ж��Ƿ����÷�֧����
		String sunClassCode = lg.getSunCmpClassCode();//��LoginBean��ȡ����֧������classCode

		MessageResources resources = getResources();

		/*���淽ʽ*/
		//zxy ��midCalculate д��value�У����� ��define����@valueOfDB:midCalculate=true���жϵ�ǰ����Ϊ�м�����
		boolean isMidCalculate = false;
		if ("midCalculate".equals(saveType)) {
			saveType = "";
			isMidCalculate = true;
			values.put("midCalculate", "true");
		}
		//�޸Ĵ�ӡ����
		int printCount = new GlobalMgt().getPrintCount(tableName, String.valueOf(values.get("id")));
		values.put("printCount", printCount + "");
		Hashtable props = (Hashtable) BaseEnv.servletContext.getAttribute(BaseEnv.PROP_INFO);
		Result rso = dbmgt.detail(tableName, BaseEnv.tableInfos, values.get("id").toString(), lg.getSunCmpClassCode(), props, lg.getId(), false,  "");

		if("zh_CN".equals(locale)){
			rs = dbmgt.update(tableInfo.getTableName(), map, values, lg.getId(), defineInfo, resources, new Locale("zh","CN"), saveType,lg, workFlow, props);
		} else{
			rs = dbmgt.update(tableInfo.getTableName(), map, values, lg.getId(), defineInfo, resources, new Locale(locale), saveType,lg, workFlow, props);
		}
		

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS || rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
			/*���ϵͳ��־*/
			//�õ��޸�ǰ��¼
			HashMap valuesOld = new HashMap();
			if (rso.retCode == ErrorCanst.DEFAULT_SUCCESS && rso.retVal != null) {
				valuesOld = (HashMap) rso.retVal;
			}
			int operation = 1;
			if ("saveDraft".equals(saveType))
				operation = 6;
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				//����ģ����û�и���ģ��ȥȡ������ֱ���Ǳ���
				String billTypeName = tableInfo.getDisplay().get(locale); //getModuleNameByLinkAddr(request, mapping);
				dbmgt.addLog(operation, values, valuesOld, tableInfo, locale, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(), "", billTypeName);
			}
			//���³ɹ��󱣴�ű�
			String path = BaseEnv.servletContext.getRealPath("DoSysModule.sql");
			TestRW.saveToSql(path, DynDBManager.mainUpdate);
			ArrayList childsDel = DynDBManager.childsDel;
			for (int i = 0; i < childsDel.size(); i++) {
				TestRW.saveToSql(path, childsDel.get(i).toString());
			}
			DynDBManager.childsDel.clear();
			ArrayList childsIns = DynDBManager.childsIns;
			for (int i = 0; i < childsIns.size(); i++) {
				TestRW.saveToSql(path, childsIns.get(i).toString());
			}
			DynDBManager.childsIns.clear();

			//ͬ����֯�ܹ��� ��ʱͨѶ�ͻ���
			String objId = (String) values.get("id");
			if ("tblEmployee".equals(tableName)) {
				boolean flag = new UserMgt().getOpenFlagUserById(objId);
				if (flag == true) {
					MSGConnectCenter.refreshEmpInfo(objId);
				}
			}
			if ("tblDepartment".equals(tableName)) {
				MSGConnectCenter.updateDeptInfo(objId);
			}
			//��鵱ǰ�����Ƿ����������,��Ҫ���������ȡȨ��
			if (tableName.equals("tblStockAnalysisInfo")) {
				StockAnalysisInfoMgt mgt = new StockAnalysisInfoMgt();
				mgt.serivce();
			}

			//�޸ĳɹ�
			String alertMessage = GlobalsTool.getMessage(locale, "common.msg.updateSuccess");
			if (rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
				alertMessage = dbmgt.getDefSQLMsg(locale, (String) rs.retVal);
			}
			Message msg = new Message("OK", alertMessage);
			return msg;

		} else {
			if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
				//�Զ���������Ҫ�û�ȷ��
				ConfirmBean confirm = (ConfirmBean) rs.getRetVal();
				String content = dbmgt.getDefSQLMsg(locale, confirm.getMsgContent());
				String jsConfirmYes = "";
				String jsConfirmNo = "";

				jsConfirmYes = confirm.getFormerDefine() + ":" + confirm.getYesDefine() + ";";
				if (confirm.getNoDefine().length() > 0) {
					jsConfirmNo = confirm.getFormerDefine() + ":" + confirm.getNoDefine() + ";";
				}
				Message msg = new Message("CONFIRM", content + "#;#" + jsConfirmYes + "#;#" + jsConfirmNo);
				return msg;

			} else {
				SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, locale, saveType);
				Message msg = new Message("ERROR", saveErrrorObj.getMsg());
				return msg;
			}
		}
	}

	/**
	 * У�鵥�ݵĻ���ڼ�
	 * @param tableName
	 * @param keyId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private static boolean vildatePeriod(String tableName, String keyId,LoginBean loginBean) throws Exception {
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
			AccPeriodBean accBean = (AccPeriodBean) BaseEnv.accPerios.get(loginBean.getSunCmpClassCode());
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
	 * DBTableInfoBean ��tbleName����
	 */
	private static class SortDBTable implements Comparator {
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
	 * ȡ�Զ��嵥����ϸ����(���ӿڲ����⿪��)��
	 * 
	 * <br/><br/>
	 * ����ֵ: DetailBean���󣬼�DetailBean�ӿ�˵��
	 * @see DetailBean
	 */
	public static  DetailBean  detail(String locale, LoginBean loginBean,  String keyId, String tableName) {
		DetailBean detailBean = new DetailBean();

		DBTableInfoBean tableInfo = BaseEnv.tableInfos.get(tableName);
		boolean isDetail = true;

		String parentTableName = tableInfo.getPerantTableName();
		if (parentTableName != null && parentTableName.indexOf(";") > 0) {
			parentTableName = parentTableName.substring(0, parentTableName.indexOf(";"));
		}
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
		
		Hashtable<String, ArrayList<ColConfigBean>> userColConfig = (Hashtable<String, ArrayList<ColConfigBean>>) BaseEnv.servletContext.getAttribute("userSettingColConfig");


		final Result billDetail = dbmgt.detail(tableName, BaseEnv.tableInfos, keyId, loginBean.getSunCmpClassCode(), BaseEnv.propMap, loginBean.getId(), true,  "");
		if (billDetail.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			detailBean.setCode(ErrorCanst.DEFAULT_FAILURE);
			detailBean.setMsg("��ѯ���鱨��" + billDetail.getRetVal());
			return detailBean;
		}
		HashMap values = (HashMap) billDetail.getRetVal();
		if(values==null || values.get("id")==null){
    		//δ�ҵ���¼
			detailBean.setCode(ErrorCanst.DEFAULT_FAILURE);
			detailBean.setMsg("��¼������");
			return detailBean;
    	}

		String moduleType = values.get("moduleType") == null ? "" : values.get("moduleType") + "";
		MOperation mop = GlobalsTool.getMOperationMap(parentTableName, tableName, moduleType, loginBean);
		if (mop == null) { 
			//�������ܲ鿴�ĵ��ݣ��Ҷ��а���Ͳ鿴�����Ȩ��
			mop=new MOperation();
            mop.update=false;
            mop.print=false;
            mop.query=true;
            mop.sendEmail=false;
            mop.oaWorkFlow=true;
			
			//detailBean.setCode(ErrorCanst.DEFAULT_FAILURE);
			//detailBean.setMsg("Ȩ��MOPΪ�գ������Ƿ����ģ��Ȩ��");
			//return detailBean;
		}

		boolean CannotOper = false;
		//�����ǰ�ڼ䲻Ϊ�ڳ������ڳ������ӣ�ɾ�����޸İ�ť������ʾ
		if (!"-1".equals(((Hashtable) BaseEnv.sessionSet.get(loginBean.getId())).get("NowPeriod"))) {
			if (tableName.equals("tblCompanybeginning") || tableName.equals("tblBeginStock") || (tableName.equals("tblFixedAssetAdd") && "0".equals(moduleType))) {
				CannotOper = true;
			}
		}
		HashMap flowMap = oamgt.getOAMyWorkFlowInfo(keyId, tableName);
		OAWorkFlowTemplate template = flowMap == null ? BaseEnv.workFlowInfo.get(tableName) : BaseEnv.workFlowInfo.get(flowMap.get("applyType"));

		//�������Щ��,���Ŵ����ĵ������޸�Ȩ��
		String[] deptRight = dbmgt.getDeptRight(mop.getScope(MOperation.M_UPDATE), mop.getScope(MOperation.M_DELETE), loginBean.getAllScopeRight());
		String updateOtherRight = deptRight[0];//�鿴���˵���Ȩ��
		String updateDeptRight = deptRight[1];//���Ź�ϽȨ��
		// �������Щ��,���Ŵ����ĵ�����ɾ��Ȩ��
		String delOtherRight = deptRight[2];
		String delDeptRight = deptRight[3];
		String createBy = (String) values.get("createBy");
		String employeeId = (String) values.get("EmployeeID");
		//��������ȡdeparmentCode,���employeeId,�ٴ�createBy
		String dept = (String) values.get("DepartmentCode");
		//��������ȡdeparmentCode,���employeeId,�ٴ�createBy
		if (dept == null && employeeId != null)
			dept = OnlineUserInfo.getUser(employeeId) == null ? null : OnlineUserInfo.getUser(employeeId).getDeptId();
		if (dept == null)
			dept = OnlineUserInfo.getUser(createBy) == null ? "" : OnlineUserInfo.getUser(createBy).getDeptId();
		boolean deleteRight = mop.delete()
				&& !CannotOper
				&& dbmgt.getDeleteRight(dept, delDeptRight, delOtherRight, loginBean.getId(), (String) values.get("createBy"), (String) values.get("EmployeeID"), (String) values.get("workFlowNode"),
						(String) values.get("workFlowNodeName"), template);
		detailBean.setDeleteRight(deleteRight);

		if (values.get("createBy") != null) {
			/* ��ѯ��ǰ���ݱ���ί�и��ҵĹ������� */
			HashMap<String, String> consignMap = new HashMap<String, String>();

			if (template != null) {
				consignMap = DynDBManager.queryConsignation(loginBean.getId(), template.getId());
			}

			//������Ϣ����δ���������
			if (tableInfo.getIsBaseInfo() == 1 && (template == null || template.getTemplateStatus() == 0)) {
				employeeId = loginBean.getId();
				dept = loginBean.getDepartCode();
			}

			boolean isPeriodBefor = false; //�Ƿ��ǻ���ڼ�ǰ������
			//����Ƿ��ǻ���ڼ�ǰ������      
			String copy = "";
			if (!"copy".equals((String) copy)) {
				String sysParamter = tableInfo.getSysParameter();//����Ϣϵͳ����
				Date time = null;
				for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
					DBFieldInfoBean bean = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
					if (bean.getDefaultValue() != null && "accendnotstart".equals(bean.getFieldIdentityStr())) {
						String timeStr = values.get(bean.getFieldName()).toString();
						if (timeStr != null) {
							try {
								time = BaseDateFormat.parse(timeStr, BaseDateFormat.yyyyMMdd);
							} catch (Exception e) {
								BaseEnv.log.error("MobileAjax.detail ת�����ڸ�ʽ����", e);
							}
						}
					}
				}
				int currentMonth = 0;
				int currentYear = 0;
				if (null != time) {
					currentMonth = time.getMonth() + 1;
					currentYear = time.getYear() + 1900;
				}
				int periodMonth = -1;
				int periodYear = -1;
				AccPeriodBean accBean = (AccPeriodBean) sessionSet.get("AccPeriod");
				if (accBean != null) {
					periodMonth = accBean.getAccMonth();
					periodYear = accBean.getAccYear();

				}
				String workFlowNodeName = (String) values.get("workFlowNodeName");
				if ((currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth) || periodMonth < 0) && currentMonth != 0 && !"draft".equals(workFlowNodeName)) {
					if (null != sysParamter) {
						if (sysParamter.equals("CurrentAccBefBill") && (currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth))) {
							isPeriodBefor = true;
						}

						if (sysParamter.equals("CurrentAccBefBillAndUnUseBeforeStart") && (currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth) || periodMonth < 0)) {
							isPeriodBefor = true;
						}
					}

				}
			}

			boolean updateRight = mop.update()
					&& !CannotOper
					&& dbmgt.getUpdateRight(dept, updateDeptRight, updateOtherRight, loginBean.getId(), (String) values.get("createBy"), employeeId, (String) values.get("checkPersons"), consignMap,
							(String) values.get("workFlowNode"), (String) values.get("workFlowNodeName"), template);

			//����������жϴ��û��Ƿ����޸�Ȩ��
			if (template != null && !template.getTemplateClass().equals("00002") && template.getTemplateStatus() == 1 && !"draft".equals(values.get("workFlowNodeName"))) {

				if ("-1".equals((String) values.get("workFlowNode"))) {
					if (OnlineUserInfo.getUser(values.get("createBy").toString()) != null) {
						boolean flag = dbmgt.isRetCheckPer(loginBean, template, OnlineUserInfo.getUser(values.get("createBy").toString()).getDeptId());
						if (!flag && values.get("DepartmentCode") != null && !values.get("DepartmentCode").equals("")) {
							flag = dbmgt.isRetCheckPer(loginBean, template, values.get("DepartmentCode") + "");
						}
						if (flag) {//����з���ˣ���ʾ����˰�ť
							detailBean.setRetCheckRight(true);
						}
					}
				} else if (flowMap != null && "0".equals(String.valueOf(flowMap.get("currentNode")))) {
					//��ʼ�������ͨȨ��һ��
					if (updateRight) { //���޸�Ȩ�޾������Ȩ��
						detailBean.setCheckRight(true);
					}
				} else {
					String strCheckPersons = String.valueOf(values.get("checkPersons"));
					if (strCheckPersons != null) {
						// �߰�,ֻ�д����˿��Դ߰�,���������ǰ����˲��Լ�
						if (values.get("createBy").toString().equals(loginBean.getId()) && strCheckPersons.indexOf(";" + loginBean.getId() + ";") < 0) {
							detailBean.setHurryTrans(true);
						}
						/*�鿴��ǰ���������û��ί�и��ҵ�*/
						String designId = flowMap != null ? String.valueOf(flowMap.get("applyType")) : "";
						for (String person : strCheckPersons.split(";")) {
							if (consignMap.get(person) != null) {
								strCheckPersons += loginBean.getId() + ";";
							}
						}
						if (strCheckPersons.indexOf(";" + loginBean.getId() + ";") >= 0 || ("0".equals((String) values.get("workFlowNode")) && updateRight)) {
							detailBean.setCheckRight(true);
						}
						if (flowMap != null && (!detailBean.isCheckRight())) { //�б�������˰�Ť�Ͳ����ֳ��ذ�Ť�����鱣��һ��
							//�ж��Ƿ���г��ذ�ť
							String lastCheckPerson = String.valueOf(flowMap.get("lastCheckPerson"));
							String currentNode = String.valueOf(flowMap.get("currentNode"));
							FlowNodeBean flowNode = BaseEnv.workFlowDesignBeans.get(designId).getFlowNodeMap().get(currentNode);
							if (loginBean.getId().equals(lastCheckPerson) && flowNode != null && flowNode.isAllowCancel()) {
								detailBean.setHasCancel(true);
							}
						}

					}
				}
			}
			detailBean.setUpdateRight(updateRight);
			//�ǻ���ڼ�ǰ�����޸�Ȩ�ޣ�
			if (!isPeriodBefor && updateRight) {
				isDetail = false;
				detailBean.setOpType("update");
			} else {
				isDetail = true;
				detailBean.setOpType("detail");
			}

		}

		//ȡ��ΧȨ�ޣ�Ҫ���������Ϊdetail����Ȩ��ʱ�Զ����޸�
		ArrayList scopeRight = new ArrayList();
		scopeRight.addAll(isDetail == true ? mop.getScope(MOperation.M_QUERY) : mop.getScope(MOperation.M_UPDATE));
		scopeRight.addAll(loginBean.getAllScopeRight());

		detailBean.setValues(values); //���÷���ֵ		

		ArrayList childTableList = DDLOperation.getChildTables(tableName, BaseEnv.tableInfos);

		HashMap<String, ArrayList<String[]>> moduleTable = (HashMap) BaseEnv.ModuleField.get(mop.getModuleUrl()); // ͬһ���ж��ģ�飬�����õĲ�ͬģ���ֶβ�һ�µ���Ϣ

		String allTableName = tableName + ",";

		ArrayList<ColConfigBean> allConfigList = new ArrayList<ColConfigBean>();
		ArrayList<ColConfigBean> configList = new ArrayList<ColConfigBean>();// �����������
		Hashtable<String, ArrayList<ColConfigBean>> childTableConfigList = new Hashtable<String, ArrayList<ColConfigBean>>();// �ӱ��������
		if (userColConfig != null && userColConfig.size() > 0) { // ���������������
			configList = userColConfig.get(tableName + "bill");
			if (configList != null) {
				allConfigList.addAll(configList);
			}
		}
		for (int i = 0; i < childTableList.size(); i++) { // ������ϸ���������
			DBTableInfoBean childTableInfo = (DBTableInfoBean) childTableList.get(i);
			allTableName += childTableInfo.getTableName() + ",";
			// ������ϸ���Զ����е���ʾ
			if (allConfigList != null && allConfigList.size() > 0) {
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
			Map kv = new HashMap();
			if(dtb.getIsUsed() == 1){
				kv.put("name",dtb.getTableName());
				kv.put("display",dtb.getDisplay().get(locale));
				ctn.add(kv);
			}
			
			/*
			if(dtb.getIsUsed()==1){
				HashMap map = new HashMap();
				map.put("tableName", dtb.getTableName());
				map.put("display", dtb.getDisplay().get(locale));
				ctn.add(map);
			}*/
		}
		detailBean.setChildTableList(ctn);

		String userLastNode = ""; // �û���ǰ���һ�������Ϣ

		// ���ú͹����������Ϣ
		if (template != null && template.getTemplateStatus() == 1 && flowMap != null) {
			String currNodeId = "";
			String designId = "";
			if (flowMap != null) {
				currNodeId = String.valueOf(flowMap.get("currentNode"));
				designId = String.valueOf(flowMap.get("applyType"));
			} else {
				currNodeId = "-1";
			}

			WorkFlowDesignBean designBean = BaseEnv.workFlowDesignBeans.get(designId);
			if (designBean == null) {
				detailBean.setCode(ErrorCanst.DEFAULT_FAILURE);
				detailBean.setMsg("������" + designId + "������");
				return detailBean;
			}

			if (isDetail) {
				Result rst = oamgt.getUserLastNode(keyId, loginBean.getId(), tableName);
				if (rst.retCode == ErrorCanst.DEFAULT_SUCCESS && rst.retVal != null) {
					userLastNode = rst.retVal.toString();
				}
				if (userLastNode == null || userLastNode.length() == 0) {
					userLastNode = currNodeId;
				}
				if (designBean.getFlowNodeMap().get(userLastNode) == null) {
					userLastNode = currNodeId;
				}
			} else {
				userLastNode = currNodeId;
			}

			Result rs1 = oamgt.flowDepict(designId, keyId, tableName);
			if (rs1.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				detailBean.setFlowDepict((ArrayList) rs1.getRetVal());
			}
		}

		String f_brother = values.get("f_brother") == null ? "" : values.get("f_brother") + "";

		// ����Ȩ��,�����ã�������������ʾ��Щ�ֶ�
		ArrayList<DBFieldInfoBean> fieldList;
		try {
			fieldList = DynDBManager.getMainFieldList(tableName, tableInfo,null, configList, template, flowMap, moduleTable, mop, userLastNode, isDetail, f_brother,
				loginBean, scopeRight);
		} catch (Exception e) {
			detailBean.setCode(ErrorCanst.DEFAULT_FAILURE);
			detailBean.setMsg(e.getMessage());
			return detailBean;
		}
		detailBean.setFieldList(DynDBManager.distinctList(fieldList));

		HashMap moreLan = (HashMap) values.get("LANGUAGEQUERY");
		for (int i = 0; i < fieldList.size(); i++) {
			DBFieldInfoBean bean = (DBFieldInfoBean) fieldList.get(i);
			if (bean.getInputType() == bean.INPUT_MAIN_TABLE) {
				if (bean.getSelectBean() != null) {
					for (int j = 0; j < bean.getSelectBean().getViewFields().size(); j++) {
						PopField pop = (PopField) bean.getSelectBean().getViewFields().get(j);
						DBFieldInfoBean bean2 = GlobalsTool.getFieldBean(pop.getFieldName().substring(0, pop.getFieldName().indexOf(".")), pop.getFieldName().substring(
								pop.getFieldName().indexOf(".") + 1));
						if (bean2 == null && pop.getDisplay() != null && pop.getDisplay().length() > 0) {
							bean2 = GlobalsTool.getFieldBean(pop.getDisplay().substring(0, pop.getDisplay().indexOf(".")), pop.getDisplay().substring(pop.getDisplay().indexOf(".") + 1));
						}
						// ����������ı�����ͼʱ����ѯ����ʱ�����ο���̨UserFunctionAction#updatePrepare��ע�͸ô���
						if (bean2 == null) {
//							detailBean.setCode(ErrorCanst.DEFAULT_FAILURE);
//							detailBean.setMsg("�ֶ�" + bean.getFieldName() + "������" + bean.getSelectBean().getName() + "�ֶ�" + pop.getFieldName() + "������");
//							return detailBean;
							continue;
						}
						if (bean2.getInputType() == bean2.INPUT_LANGUAGE || bean2.getInputTypeOld() == bean2.INPUT_LANGUAGE) {
							if (moreLan.get(values.get(pop.getAsName())) != null) {
								values.put(pop.getAsName(), ((KRLanguage) moreLan.get(values.get(pop.getAsName()))).get(locale));
							} else {
								values.put(pop.getAsName(), values.get(pop.getAsName()));
							}
						}
					}
				}
			}
		}

		
		
		HashMap childShowField = new HashMap();
		for (int i = 0; i < childTableList.size(); i++) { // ��ϸ�����ʾ��
			DBTableInfoBean childTableInfo = (DBTableInfoBean) childTableList.get(i);
			
			try {
				childShowField.put(childTableInfo.getTableName(), DynDBManager.getMainFieldList(childTableInfo.getTableName(), childTableInfo,null, childTableConfigList.get(childTableInfo.getTableName()), template, flowMap, moduleTable,
						mop, userLastNode, isDetail, f_brother, loginBean, scopeRight));
			} catch (Exception e) {
				detailBean.setCode(ErrorCanst.DEFAULT_FAILURE);
				detailBean.setMsg(e.getMessage());
				return detailBean;
			}
		}
		detailBean.setChildShowField(childShowField);
		

		return detailBean;
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
	public static Message add(String locale,LoginBean loginBean,String tableName,String parentCode,String defineInfo,String saveType,String valuesJson,String deliverTo) {
		
		
		// =======�κ��˲���ɾ�����δ��� ������=============
		/**
		 * ����������в������м��ܹ��ļ�飬 ���ð�Ҳ������������������м�飬 ���������Զ�����ɱ�����ִ�У� ���Լ��ı�����Ϊ�ٷ�֮һ
		 * 
		 */
		if ((System.currentTimeMillis() - com.menyi.web.util.SystemState.uCheckTime) > 1 * 24 * 60 * 60000) {
			com.menyi.web.util.SystemState.uCheckTime = System.currentTimeMillis();
			System.out.println("UserFunctionAction check Dog----------------------");
			if (com.menyi.web.util.SystemState.instance.encryptionType == com.menyi.web.util.SystemState.ENCRYPTION_EVAL) {
				// �������
				// ������ģ��ļ��
				// ��ѯ�ɹ������ۣ�����ģ����ʱ��
				int er = new InitMenData().checkEnv();
				if (2 == er) {
					SystemState.instance.dogState = SystemState.DOG_ERROR_ENV_BIS;
					Message msg = new Message("ERROR", "���ó���");
					return msg;
				}
			} else if (com.menyi.web.util.SystemState.instance.encryptionType == com.menyi.web.util.SystemState.ENCRYPTION_SOFT) {
				File file = new File("aio.cert");
				if (!file.exists()) {
					com.menyi.web.util.SystemState.instance.dogState = com.menyi.web.util.SystemState.DOG_RESTART;
				}
				// ���ļ�
				byte[] bs = null;
				try {
					FileInputStream fis = new FileInputStream(file);
					int len = fis.available();
					bs = new byte[len];
					fis.read(bs);
				} catch (Exception e) {
					// ���ܹ�������ܴ���
					com.menyi.web.util.SystemState.instance.dogState = com.menyi.web.util.SystemState.DOG_RESTART;
				}
				// �����ں͹�˾����
				byte[] tempBs = new byte[bs.length - 128];
				System.arraycopy(bs, 128, tempBs, 0, tempBs.length);
				// ����
				try {
					// ����Կ
					FileInputStream fisKey = new FileInputStream("../../website/WEB-INF/private1024.key");
					ObjectInputStream oisKey = new ObjectInputStream(fisKey);
					Key key = (Key) oisKey.readObject();
					oisKey.close();
					fisKey.close();

					Cipher cipher = Cipher.getInstance("RSA");

					// ��˽Կ����
					cipher.init(Cipher.DECRYPT_MODE, key);
					bs = cipher.doFinal(tempBs);
					// �����к�
					tempBs = new byte[30];
					System.arraycopy(bs, 0, tempBs, 0, tempBs.length);
					SystemState.instance.dogId = new String(tempBs).trim();
					// ֤��汾��Ԥ��
					// ��������
					tempBs = new byte[32];
					System.arraycopy(bs, 32, tempBs, 0, tempBs.length);
					SystemState.instance.pcNo = new String(tempBs).trim();
					// ȡ������
					Random rd = new Random();
					rd.setSeed(System.currentTimeMillis());
					int rdi = rd.nextInt(4000);
					String s = CallSoftDll.get(rdi + "");
					byte[] mb = new byte[16];
					mb = SecurityLock2.hexStringToBytes(s);
					rdi += 5;
					for (int i = 0; i < mb.length; i++) {
						mb[i] = (byte) (mb[i] - rdi);
						if (i > 8) {
							mb[i] = (byte) (mb[i] - 2);
						}
					}
					// У�������
					String realPcNo = SecurityLock2.bytesToHexString(mb);

					if (SystemState.instance.pcNo == null || !SystemState.instance.pcNo.equals(realPcNo)) {
						// �����벻һ��
						s = CallSoftDll.get("mac");
						if (SystemState.instance.pcNo == null || !SystemState.instance.pcNo.equals(s)) {
							com.menyi.web.util.SystemState.instance.dogState = com.menyi.web.util.SystemState.DOG_RESTART;
						}

					}
				} catch (Exception e) {

				}
			} else if (com.menyi.web.util.SystemState.instance.encryptionType == com.menyi.web.util.SystemState.ENCRYPTION_DOG2) {
				int version = SecurityLock.getVersion();
				if (version == 1) {
					// �ڶ������ܹ�
					byte[] bs = null;

					// ����
					try {
						// ����Կ
						FileInputStream fisKey = new FileInputStream("../../website/WEB-INF/private1024.key");
						ObjectInputStream oisKey = new ObjectInputStream(fisKey);
						Key key = (Key) oisKey.readObject();
						oisKey.close();
						fisKey.close();

						Cipher cipher = Cipher.getInstance("RSA");

						// ��˽Կ����
						cipher.init(Cipher.DECRYPT_MODE, key);
						byte[] tempBs = SecurityLock.getData();
						bs = cipher.doFinal(tempBs);
						//������
						tempBs = new byte[30];
						System.arraycopy(bs, 0, tempBs, 0, tempBs.length);
						SystemState.instance.dogId = new String(tempBs).trim();
						//֤��汾��Ԥ��
						//��������
						tempBs = new byte[32];
						System.arraycopy(bs, 32, tempBs, 0, tempBs.length);
						SystemState.instance.pcNo = new String(tempBs).trim();
						//У�������
						String[] realPcNo = SecurityLock.F_ReadKey(1);

						if (SystemState.instance.pcNo == null || !SystemState.instance.pcNo.equalsIgnoreCase(realPcNo[0] + "-" + realPcNo[1]) || SystemState.instance.dogId == null
								|| !SystemState.instance.dogId.equalsIgnoreCase(SecurityLock.readKeyId() + "")) {
							//���ܹ��Ż������벻һ��
							com.menyi.web.util.SystemState.instance.dogState = com.menyi.web.util.SystemState.DOG_RESTART;
						}
					} catch (Exception e) {

					}
				}
			}
		}
		//=======�κ��˲���ɾ�����ϴ��� ������=============

		Result rs = new Result();
		/*�������ڴ������б�ṹ����Ϣ*/
		Hashtable map = BaseEnv.tableInfos;

		LoginBean lg = loginBean;
		
		/*�����ֵܱ��f_brother*/
		
		HashMap values = gson.fromJson(valuesJson, HashMap.class);

		/*��ṹ��Ϣ*/
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, tableName);

		/*���ڴ��ж�ȡ��ǰ���ݵĹ�������Ϣ*/
		OAWorkFlowTemplate workFlow = null;

		workFlow = BaseEnv.workFlowInfo.get(tableName);
		//System.out.println("*****workFlow********"+workFlow);
		/*��ʼ��һЩ�ֶεĻ�����Ϣ*/
		Result rs_initDBField = userFunMgt.initDBFieldInfo(tableInfo, lg, values, parentCode, workFlow);
		/* fjj���ɹ� ɾ�����ݱ��*/
		
		if (rs_initDBField.retCode == ErrorCanst.CURRENT_ACC_BEFORE_BILL) {
			/*����¼����ǰ����*/
			Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "com.currentaccbefbill"));
			return msg;
		} else if (rs_initDBField.retCode == ErrorCanst.BILL_DATE_NOT_EXIST_CURRENT_ACC) {
			/*�������ڵ��ڼ��ڻ���ڼ��в�����*/
			Message msg = new Message("ERROR", "�������ڵ��ڼ��ڻ���ڼ��в�����");
			return msg;
		}else if (rs_initDBField.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			BaseEnv.log.error("AIOConnect.add initDBFieldInfo Error:"+rs_initDBField.retCode+":"+rs_initDBField.retVal);
			Message msg = new Message("ERROR", "��ʼ���ֶδ���"+rs_initDBField.retVal);
			return msg;
		}

		//��ȡ·��
		String path = BaseEnv.servletContext.getRealPath("DoSysModule.sql");

		MessageResources resources = getResources();
		
		Hashtable props = (Hashtable) BaseEnv.servletContext.getAttribute(BaseEnv.PROP_INFO);

		//zxy ��midCalculate д��value�У����� ��define����@valueOfDB:midCalculate=true���жϵ�ǰ����Ϊ�м�����
		boolean isMidCalculate = false;
		if ("midCalculate".equals(saveType)) {
			saveType = "saveAdd";
			isMidCalculate = true;
			values.put("midCalculate", "true");
		}

		if("zh_CN".equals(locale)){
			rs = dbmgt.add(tableInfo.getTableName(), map, values, lg.getId(), path, defineInfo, resources,new Locale("zh","CN"), saveType, lg, workFlow, deliverTo, props);
		} else{
			rs = dbmgt.add(tableInfo.getTableName(), map, values, lg.getId(), path, defineInfo, resources,new Locale(locale), saveType, lg, workFlow, deliverTo, props);
		}
		

		String kid = values.get("id") == null ? "" : values.get("id").toString();
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS || rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {

			String billTypeName = tableInfo.getDisplay().get(locale);
			
			String[] returnValue = (String[]) rs.retVal;
			/*��ǰ����������*/
			String addMessage = GlobalsTool.getMessage(locale, "common.lb.add");
			/*���ϵͳ��־*/
			int operation = 0;
			if ("saveDraft".equals(saveType))
				operation = 5;
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				dbmgt.addLog(operation, values, null, tableInfo, locale.toString(), lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(), "", billTypeName);
			}

			String message = GlobalsTool.getMessage(locale, "common.msg.addSuccess");
			if (returnValue[1] != null && returnValue.length > 0) {
				if (rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
					message = dbmgt.getDefSQLMsg(locale, returnValue[1]);
				} else {
					message += "<br>" + GlobalsTool.getMessage(locale, "userfunction.msg.billNoExistNew") + ":" + returnValue[1];
				}
			}
			HashMap retMap = new HashMap();
			retMap.put("msg", message);
			retMap.put("keyId", kid);
			Message msg = new Message("OK",message, retMap);
			return msg;
		} else {
			if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
				// �Զ���������Ҫ�û�ȷ��
				ConfirmBean confirm = (ConfirmBean) rs.getRetVal();
				String content = dbmgt.getDefSQLMsg(locale, confirm.getMsgContent());
				String jsConfirmYes = "";
				String jsConfirmNo = "";

				jsConfirmYes = confirm.getFormerDefine() + ":" + confirm.getYesDefine() + ";";
				if (confirm.getNoDefine().length() > 0) {
					jsConfirmNo = confirm.getFormerDefine() + ":" + confirm.getNoDefine() + ";";
				}
				Message msg = new Message("CONFIRM", content + "#;#" + jsConfirmYes + "#;#" + jsConfirmNo);
				return msg;
			} else {
				SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, locale, saveType);
				Message msg = new Message("ERROR", saveErrrorObj.getMsg());
				return msg;
			}
		}
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
	public static Message  execDefine(String locale,LoginBean loginBean,String defineName, String buttonTypeName,String[] keyIds,String tableName,String defineInfo) {
		
		MessageResources resources = getResources();

		Result rs = dbmgt.defineDelSql(defineName, keyIds, loginBean.getId(), resources, new Locale(locale), defineInfo);

		if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS&& rs.getRetCode() != ErrorCanst.RET_DEFINE_SQL_ALERT) {
			if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
				// �Զ���������Ҫ�û�ȷ��
				ConfirmBean confirm = (ConfirmBean) rs.getRetVal();
				String content = dbmgt.getDefSQLMsg(locale, confirm.getMsgContent());
				String jsConfirmYes = "";
				String jsConfirmNo = "";

				jsConfirmYes = confirm.getFormerDefine() + ":" + confirm.getYesDefine() + ";";
				if (confirm.getNoDefine().length() > 0) {
					jsConfirmNo = confirm.getFormerDefine() + ":" + confirm.getNoDefine() + ";";
				}
				Message msg = new Message("CONFIRM", content + "#;#" + jsConfirmYes + "#;#" + jsConfirmNo);
				return msg;
			} else {
				SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, locale, "");
				Message msg = new Message("ERROR", saveErrrorObj.getMsg());
				return msg;
			}
		} else {
			int operation = 14;
			Hashtable map = BaseEnv.tableInfos;
			Hashtable props = (Hashtable) BaseEnv.servletContext.getAttribute(BaseEnv.PROP_INFO);
			Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
			boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
			DBTableInfoBean tableInfo = (DBTableInfoBean) map.get(tableName);
			for (int i = 0; i < keyIds.length; i++) {
				String keyId = keyIds[i];
				/*���ڴ��ж�ȡ��ǰ���ݵĹ�������Ϣ*/
				OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(tableName);
				if (keyId.indexOf("hasChild") > 0)
					keyId = keyId.substring(0, keyId.indexOf(";hasChild"));
				Result result = dbmgt.detail(tableName, map, keyId, loginBean.getSunCmpClassCode(), props, loginBean.getId(), isLastSunCompany, "");

				HashMap values = (HashMap) result.getRetVal();
				dbmgt.addLog(operation, values, null, tableInfo, locale.toString(), loginBean.getId(), loginBean.getEmpFullName(), loginBean.getSunCmpClassCode(), buttonTypeName, tableInfo.getDisplay().get(locale));
			}
			HashMap msg = new HashMap();
			msg.put("code", "OK");
			msg.put("msg", "ִ�гɹ�");
			if(rs.retVal instanceof String[]){
				String[] ss = (String[])rs.retVal;
				if(ss.length > 1){
					msg.put("toUrl", ss[1]);
				}
				msg.put("msg", ss[0]);
			}
			return new Message("OK","",msg);
		}
	}


	
	/**
	 * ���ǰ׼���ӿڣ�������˽�㣬����˵���Ϣ��
	 * 
	 * @param keyId ��˵���ID
	 * @param tableName ��˱���
	 * <br/><br/>
	 * ����ֵ: {code:OK,obj:{nextNodes����һ������б������ж����currNode����ǰ�����Ϣ��designId��������ģ��ID��moduleName��ģ�����֣�keyId����������Ӧ����ID��tableName������dispenseWake������ַ���Ϣ}}
	 */
	public static Message deliverToPrepare(String locale,LoginBean loginBean,String keyId,String tableName,String currNodeId) {
		
		ArrayList<FlowNodeBean> nodeBeans = new ArrayList<FlowNodeBean>();
		//����Ƿ��ǻ���ڼ�ǰ������(������еĵ���)
		if (tableName != null && !"".equals(tableName)) {
			boolean vpError;
			try {
				vpError = vildatePeriod(tableName, keyId, loginBean);
				if (vpError) {
					Message msg = new Message("ERROR", "����ڼ�ǰ���ݲ��������");
					return msg;
				}
			} catch (Exception e) {
				BaseEnv.log.error("MobileAjax.deliverToPrepare Error:",e);
				Message msg = new Message("ERROR", "����ڼ�ǰ���ݱ���"+e.getMessage());
				return msg;
			}
			
		}
		//������ת��ʱ���жϵ����Ƿ��Ѿ������ϣ���������ϲ�����ִ��ת������,��������û�е�ǰ�����Ҳ������ִ�д˲���
		String designId = "";
		String department = "";
		String strCheckPersons = "";
		String currentNode = "";
		String myflowId = "";
		HashMap map = oamgt.getOAMyWorkFlowInfo(keyId, tableName);
		if (map != null) {
			designId = String.valueOf(map.get("applyType"));
			department = String.valueOf(map.get("departmentCode"));
			currentNode = String.valueOf(map.get("currentNode"));
			strCheckPersons = String.valueOf(map.get("checkPerson"));
			myflowId = String.valueOf(map.get("id"));
		}

		/*�鿴��ǰ���������û��ί�и��ҵ�*/
		HashMap<String, String> consignMap = OAMyWorkFlowMgt.queryConsignation(loginBean.getId(), designId);
		for (String person : strCheckPersons.split(";")) {
			if (consignMap.get(person) != null) {
				strCheckPersons += loginBean.getId() + ";";
			}
		}
		if ("-1".equals(currentNode)) {
			Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "common.msg.hasAudit"));
			return msg;
		}
		//������ֻ������Ϣ�����г��֣����������Ӳ�����˲���
		String outNodeId = currNodeId;
		if (outNodeId != null && outNodeId.length() > 0 && !currentNode.equals(outNodeId)) {
			//��ʼ���ת���󣬶Է�����Ϣδ����Ȼ���Ƶ��˳��ؿ�ʼ��㣬�Է��ټ���������Ϣʱ���ⲿ����ĵ�ǰ���id�����ݿ��¼�ĵ�ǰ��㲻һ�£��豨��
			Message msg = new Message("ERROR", "�˵���ǰ��㷢���仯������ԭ���г��أ���ֹ��������������˴˽��");
			return msg;
		}

		//!"0".equals(currentNode) ����������ܱ�ȥ������Ϊ�ڿ�ʼ��㵥��Ȩ�޺�����Ȩ��һ�������д˵��޸�Ȩ�޵��ڿ�ʼ������ת������
		if (strCheckPersons.length() > 0 && !strCheckPersons.contains(";" + loginBean.getId() + ";") && !"0".equals(currentNode)) {
			Message msg = new Message("ERROR", "�����Ǵ˵��������");
			return msg;
		}

		WorkFlowDesignBean designBean = BaseEnv.workFlowDesignBeans.get(designId);
		if (designBean == null) {
			Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "oa.fileflow.error2"));
			return msg;
		}
		String nextNodes = "";
		Result rs = oamgt.getNextNodeIdByVal(designId, currentNode, keyId, true, loginBean);
		if ("".equals(rs.retVal) || rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "oa.errormsg.noNode") + rs.retVal);
			return msg;
		} else {
			nextNodes = rs.retVal.toString();
		}

		WorkFlowDesignBean bean = BaseEnv.workFlowDesignBeans.get(designId);
		OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(designId);

		oamgt.transactStart(currentNode, keyId, loginBean, tableName);

		String[] nextNodeIds = nextNodes.split(";");
		FlowNodeBean currNB = bean.getFlowNodeMap().get(currentNode);

		//�����ڵ�������
		for (int i = 0; i < nextNodeIds.length; i++) {
			FlowNodeBean nodeBean = bean.getFlowNodeMap().get(nextNodeIds[i]);
			if (nodeBean == null) {
				Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "oa.errormsg.noCheckPerson"));
				return msg;
			}
			FlowNodeBean nodeBeanNew = new FlowNodeBean();
			nodeBeanNew.setKeyId(nodeBean.getKeyId());
			nodeBeanNew.setDisplay(nodeBean.getDisplay());
			nodeBeanNew.setApprovePeople(nodeBean.getApprovePeople());
			nodeBeanNew.setZAction(nodeBean.getZAction());
			nodeBeanNew.setForwardTime(nodeBean.isForwardTime());
			if (nodeBean.getZAction().equals("CHECK")) {
				try {
					ArrayList<String[]> checkPersons = oamgt.getNodeCheckPerson(nodeBean, department, keyId, null, tableName, loginBean.getId());
					if (checkPersons.size() > 0) {
						nodeBeanNew.setCheckPeople(checkPersons);
					} else {
						Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "oa.errormsg.noCheckPerson"));
						return msg;
					}
				} catch (Exception e) {
					Message msg = new Message("ERROR", e.getMessage());
					return msg;
				}
			}
			nodeBeans.add(nodeBeanNew);
		}
		//�鿴��ǰ�ڵ�����л��˹��ܣ���ô��ѯ֮ǰ������˹��Ľڵ㼰��Щ�ڵ�������
		if (currNB.isAllowBack()) {
			try {
				oamgt.getBackNodeBean(myflowId, currNB, nodeBeans, bean);
			} catch (Exception e) {
				BaseEnv.log.error("MobileAjax.deliverToPrepare error:",e);
				Message msg = new Message("ERROR", e.getMessage());
				return msg;
			}
		}

		if (tableName == null || tableName.length() == 0) {
			tableName = workFlow.getTemplateFile();
		}
		HashMap<String,Object> data = new HashMap();
		ArrayList<MFlowNodeBean> mnodeBeans = new ArrayList<MFlowNodeBean>();
		for(FlowNodeBean b:nodeBeans){
			MFlowNodeBean mb = new MFlowNodeBean();
			mb.setApprovePeople(b.getApprovePeople());
			mb.setCheckPeople(b.getCheckPeople());
			mb.setDisplay(b.getDisplay());
			mb.setForwardTime(b.isForwardTime());
			mb.setKeyId(b.getKeyId());
			mb.setUseAllApprove(b.isUseAllApprove());
			mb.setZAction(b.getZAction());
			mnodeBeans.add(mb);
		}
		
		MFlowNodeBean mcurrNB = new MFlowNodeBean();
		mcurrNB.setApprovePeople(currNB.getApprovePeople());
		mcurrNB.setCheckPeople(currNB.getCheckPeople());
		mcurrNB.setDisplay(currNB.getDisplay());
		mcurrNB.setForwardTime(currNB.isForwardTime());
		mcurrNB.setKeyId(currNB.getKeyId());
		mcurrNB.setUseAllApprove(currNB.isUseAllApprove());
		mcurrNB.setZAction(currNB.getZAction());
		
		data.put("nextNodes", mnodeBeans);
		data.put("currNode", mcurrNB);
		data.put("designId", designId);
		data.put("moduleName", BaseEnv.workFlowInfo.get(designId).getTemplateName());
		data.put("keyId", keyId);
		data.put("tableName", tableName);
		data.put("dispenseWake", workFlow.getDispenseWake()); // �ַ����ֻ�����ʱ�����˹���
		Message msg = new Message("OK","", data);
		return msg;
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
	public static Message delete(String locale,LoginBean loginBean,String tableName, String keyId) {

		ActionForward forward = null;

		if( keyId ==null || keyId.length() == 0){
			Message msg = new Message("ERROR", "��ѡ��Ҫɾ��������");
			return msg;
		}
		OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(tableName);

		/** ������������������ѯ��ǰ�Ƿ��Ѿ���ˣ�����Ѿ���˲���ɾ������* */
		if ( keyId !=null && keyId.length() > 0 && workFlow != null && workFlow.getTemplateStatus() == 1) {
			OAMyWorkFlowMgt workFlowMgt = new OAMyWorkFlowMgt();
			Object[] workFlowNode = workFlowMgt.getTableWorkFlowName(tableName, keyId);
			if (workFlowNode != null && (!"draft".equals(workFlowNode[1]) && !workFlowNode[0].equals("0"))) {
				Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "com.approvedNotDelete"));
				return msg;
			}
		}

		Result rsAcc = dbmgt.hasCreateAcc(tableName, keyId);
		if (rsAcc.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if (rsAcc.retVal != null) {// ����Ѿ�����ƾ֤�������������ʾ
				String[] str = (String[]) rsAcc.retVal;
				Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "common.hasCreateAcc.Oper.error", str[0], str[1]));
				return msg;
			}
		} else {
			Message msg = new Message("ERROR", new ErrorMessage().toString(rsAcc.retCode, locale));
			return msg;
		}
		Hashtable map = BaseEnv.tableInfos;
		DBTableInfoBean tableInfo = (DBTableInfoBean) map.get(tableName);
		LoginBean lg = loginBean;
		String sunClassCode = lg.getSunCmpClassCode(); // ��LoginBean��ȡ����֧������classCode
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		// ����Ѿ��½ᣬ������ɾ��
		HashMap values = new HashMap();
		String sysParamter = tableInfo.getSysParameter();
		Date time = null;
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean fi = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			String o = fi.getDefaultValue();
			String type = fi.getFieldIdentityStr();
			if (null != type) {
				if (null != o && type.equals("accendnotstart")) {
					String date = (String) dbmgt.getFieldValueById(new String[]{keyId}, tableInfo.getTableName(), fi.getFieldName());
					if (null != date) {
						try {
							time = BaseDateFormat.parse(date, BaseDateFormat.yyyyMMdd);
						} catch (Exception e) {
							Message msg = new Message("ERROR", "�ֶ�" + fi.getFieldName() + "����Ϊ����ǰ����������ʽ����ȷ" + date);
							return msg;
						}
					}
				}
			}
		}
		int currentMonth = 0;
		int currentYear = 0;
		if (null != time) {
			currentMonth = time.getMonth() + 1;
			currentYear = time.getYear() + 1900;
		}
		int periodMonth = -1;
		int periodYear = -1;
		GlobalMgt mgt = new GlobalMgt();
		AccPeriodBean accBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		if (accBean != null) {
			periodMonth = accBean.getAccMonth();
			periodYear = accBean.getAccYear();
		}
		if ((currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth)) && currentMonth != 0) {
			if (null != sysParamter) {
				if (sysParamter.equals("CurrentAccBefBill") && (currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth))) {
					Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "com.currentaccbefbill.delete"));
					return msg;
				}

				if (sysParamter.equals("CurrentAccBefBillAndUnUseBeforeStart") && (currentYear < periodYear || (currentYear == periodYear && currentMonth < periodMonth))) {
					Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "com.can.not.monthly"));
					return msg;
				}
			}
		}

		/* query reation table */
		Result rs = dbmgt.getRelation(tableName,new String[]{ keyId}, map, lg.toString());
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			if (rs.retCode == ErrorCanst.RET_EXISTSRELATION_ERROR) {
				Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "common.msg.RET_EXISTSRELATION_ERROR", rs.retVal.toString()));
				return msg;
			} else {
				Message msg = new Message("ERROR", new ErrorMessage().toString(rs.retCode, locale));
				return msg;
			}
		}

		// �����˷�֧����ʱ�����������ĩ����֧����������ɾ�������������
		String openValue = BaseEnv.systemSet.get("sunCompany").getSetting();
		boolean openSunCompany = Boolean.parseBoolean(openValue);
		boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
		boolean isSharedTable = tableInfo.getIsSunCmpShare() == 1 ? true : false;
		// ����ְԱ����ű�ʱ������ĩ����֧��������
		boolean isEmpOrDept = "tblEmployee".equalsIgnoreCase(tableInfo.getTableName()) || "tblDepartment".equalsIgnoreCase(tableInfo.getTableName()) ? true : false;
		if (openSunCompany && !isLastSunCompany && !isSharedTable && !isEmpOrDept) {
			Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "common.msg.notLastSunCompany"));
			return msg;
		}

		MessageResources resources = getResources();

		String deptCode[] = userMgt.getDeptCodeById(new String[]{keyId});

		Result result = userMgt.getOpenFlagUser(new String[]{keyId});
		List objList = (List) result.retVal;
		String userIds[] = new String[objList.size()];
		for (int i = 0; i < objList.size(); i++) {
			Object[] obj = (Object[]) objList.get(i);
			userIds[i] = (String) obj[0];
		}
		Hashtable props = (Hashtable) BaseEnv.servletContext.getAttribute(BaseEnv.PROP_INFO);

		// �õ�Ҫɾ�����ݵ���Ϣ
		ArrayList manyValues = new ArrayList();
		Result rso = dbmgt.detail(tableName, BaseEnv.tableInfos, keyId, lg.getSunCmpClassCode(), props, lg.getId(), false, "");
		if (rso.retCode == ErrorCanst.DEFAULT_SUCCESS && rso.retVal != null) {
			manyValues.add(rso.getRetVal());
		}

		rs = dbmgt.delete(tableName, map, new String[]{keyId}, lg.getId(),  resources, new Locale(locale), "draft".equals("") ? true : false);

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// ��¼ɾ����־��Ϣ
			for (int i = 0; i < manyValues.size(); i++) {
				HashMap valuesOld = (HashMap) manyValues.get(i);
				int operation = 2;
				if ("draft".equals(valuesOld.get("workFlowNodeName")))
					operation = 7;
				String billTypeName = tableInfo.getDisplay().get(locale);
				dbmgt.addLog(operation, valuesOld, null, tableInfo, locale, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(), "", billTypeName);
			}
			// ͬ����֯�ܹ��� ��ʱͨѶ�ͻ���
			if ("tblEmployee".equals(tableName)) {
				if (userIds.length > 0) {
					MSGConnectCenter.deleteObj(userIds, "employee");
				}
			}
			if ("tblDepartment".equals(tableName)) {
				MSGConnectCenter.deleteObj(deptCode, "dept");
			}

			// ����ǵ���Ҫ��������ɱ�
			if (tableInfo.getSysParameter().equals("CurrentAccBefBillAndUnUseBeforeStart") && BaseEnv.systemSet.get("SaveBillAutoRecalc") != null
					&& BaseEnv.systemSet.get("SaveBillAutoRecalc").getSetting().equals("true")) {
				ReCalcucateMgt sysMgt = new ReCalcucateMgt();
				sysMgt.reCalcucateData(lg.getSunCmpClassCode(), accBean.getAccYear(), accBean.getAccMonth(), lg.getId(), "reCalcucate", "", "");
			}

			// ��������˹�������ɾ���ҵĹ������ĵ�����Ϣ
			if (workFlow != null && workFlow.getTemplateStatus() == 1) {
				new OAMyWorkFlowMgt().deleteOAMyWorkFlow("'" + keyId + "'");
			}

			// ����ɾ���ű�
			String path = BaseEnv.servletContext.getRealPath("DoSysModule.sql");
			ArrayList delArray = DynDBManager.delArray;
			for (int i = 0; i < delArray.size(); i++) {
				TestRW.saveToSql(path, delArray.get(i).toString());
			}
			DynDBManager.delArray.clear();
			// ///////////////////////
			/* ɾ��ʱ �Զ�������Ϣ */
			Result smsResult = userFunMgt.querySMSModelInfo(tableName, "delete");
			if (smsResult != null && smsResult.retVal != null) {
				String[] sms = (String[]) smsResult.retVal;
				userFunMgt.autoSendMessage(values, lg, tableInfo, locale, sms, "delete");
			}
			// ɾ���ɹ�
			Message msg = new Message("OK","ɾ���ɹ�");
			return msg;
			
		} else {
			if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
				//�Զ���������Ҫ�û�ȷ��
				ConfirmBean confirm = (ConfirmBean) rs.getRetVal();
				String content = dbmgt.getDefSQLMsg(locale, confirm.getMsgContent());
				String jsConfirmYes = "";
				String jsConfirmNo = "";

				jsConfirmYes = confirm.getFormerDefine() + ":" + confirm.getYesDefine() + ";";
				if (confirm.getNoDefine().length() > 0) {
					jsConfirmNo = confirm.getFormerDefine() + ":" + confirm.getNoDefine() + ";";
				}
				Message msg = new Message("CONFIRM", content + "#;#" + jsConfirmYes + "#;#" + jsConfirmNo);
				return msg;

			} else {
				SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, locale, "");
				Message msg = new Message("ERROR", saveErrrorObj.getMsg());
				return msg;
			}
		}

	}
	
	/**
	 * ����˵��ݣ�
	 * 
	 * @param keyId ����ID
	 * @param tableName ����
	 * <br/><br/>
	 * ����ֵ: {code:'OKִ�гɹ���CONFIRM������ȷ�Ͽ�ERROR��ִ�д���',msg����ʾ��Ϣ}
	 */
	public static Message retCheck(String locale,LoginBean loginBean,String keyId, String tableName) {

		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(BaseEnv.tableInfos, tableName);

		String workFlowNodeName = "";
		String tempTableName = new DynDBManager().getInsertTableName(tableName);// ����CRM��ģ���������
		Result rst = new AIODBManager().sqlList("select workFlowNodeName,checkPersons from " + tempTableName + " where id='" + keyId + "'", new ArrayList());
		if (rst.retVal != null && ((ArrayList) rst.retVal).size() > 0) {
			Object[] obj = ((Object[]) ((ArrayList) rst.retVal).get(0));
			workFlowNodeName = obj[0] == null ? "" : obj[0].toString();
		}
		String checkPersons = BaseEnv.workFlowInfo.get(tableName).getAllowVisitor();
		if (workFlowNodeName.equals("notApprove") || !(checkPersons.contains("," + loginBean.getId() + ",") || checkPersons.contains("," + loginBean.getDepartCode() + ","))) {
			Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "common.msg.hasAudit"));
			return msg;
		}
		/* �жϵ�ǰ�����Ƿ��Ѿ�����ƾ֤ */
		Result rsAcc = dbmgt.hasCreateAcc(tableName, keyId);
		if (rsAcc.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if (rsAcc.retVal != null) {// ����Ѿ�����ƾ֤�������������ʾ
				String[] str = (String[]) rsAcc.retVal;
				Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "common.hasCreateAcc.Oper.error", str[0], str[1]));
				return msg;
			}
		} else {
			Message msg = new Message("ERROR", new ErrorMessage().toString(rsAcc.retCode, locale));
			return msg;
		}
		/** *******************************�õ���ǰҪ����˵��ݵ����ڣ��Ƿ����½�ǰ����***************************** */
		String sysParamter = tableInfo.getSysParameter();// ����Ϣϵͳ����
		Date time = null;
		String timeStr = "";
		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean bean = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
			if (bean.getDefaultValue() != null && "accendnotstart".equals(bean.getFieldIdentityStr())) {
				AIODBManager aioMgt = new AIODBManager();
				Result rs = aioMgt.sqlList("select " + bean.getFieldName() + ",workFlowNodeName from " + tempTableName + " where id='" + keyId + "'", new ArrayList());
				if (((ArrayList) rs.retVal).size() > 0) {
					timeStr = ((Object[]) ((ArrayList) rs.retVal).get(0))[0].toString();
					workFlowNodeName = ((Object[]) ((ArrayList) rs.retVal).get(0))[1].toString();
				}
			}
		}
		if (timeStr.length() > 0) {
			try {
				time = BaseDateFormat.parse(timeStr, BaseDateFormat.yyyyMMdd);
			}catch (Exception e) {
				BaseEnv.log.error("");
			}
			int currentMonth = 0;
			int currentYear = 0;
			if (null != time) {
				currentMonth = time.getMonth() + 1;
				currentYear = time.getYear() + 1900;
			}
			int periodMonth = -1;
			int periodYear = -1;
			AccPeriodBean accBean = (AccPeriodBean) BaseEnv.accPerios.get(loginBean.getSunCmpClassCode());
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
						Message msg = new Message("ERROR", GlobalsTool.getMessage(locale, "com.cantupdatebefbill"));
						return msg;
					}

				}
			}
		}
		/** *******************************�õ���ǰҪ����˵��ݵ����ڣ��Ƿ����½�ǰ����***************************** */

		OAMyWorkFlowMgt oaMgt = new OAMyWorkFlowMgt();
		Hashtable table = ((Hashtable) BaseEnv.sessionSet.get(loginBean.getId()));
		table.put("BillOper", "returnAuditing");
		Result rs = oaMgt.execRetCheckDefine(tableName, keyId, loginBean, getResources(), new Locale( locale));

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			Message msg = new Message("OK", GlobalsTool.getMessage(locale, "define.button.reverse.success"));
			return msg;
		} else {
			SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, locale, "");
			Message msg = new Message("ERROR", saveErrrorObj.getMsg());
			return msg;
		}

	}
	/**
	 * ������ˣ�
	 * 
	 * @param keyId ����ID
	 * @param tableName ����
	 * <br/><br/>
	 * ����ֵ: {code:'OKִ�гɹ���CONFIRM������ȷ�Ͽ�ERROR��ִ�д���',msg����ʾ��Ϣ}
	 */
	public static Message cancelTo(String locale,LoginBean loginBean,String keyId, String tableName) {
		Result rs;
		try {
			HashMap flowMap = oamgt.getOAMyWorkFlowInfo(keyId, tableName);

			OAWorkFlowTemplate template = flowMap == null ? BaseEnv.workFlowInfo.get(tableName) : BaseEnv.workFlowInfo.get(flowMap.get("applyType"));

			// �ж��Ƿ���г��ذ�ť
			String lastCheckPerson = String.valueOf(flowMap.get("lastCheckPerson"));
			String currentNode = String.valueOf(flowMap.get("currentNode"));
			FlowNodeBean flowNode = BaseEnv.workFlowDesignBeans.get(flowMap.get("applyType")).getFlowNodeMap().get(currentNode);
			if (loginBean.getId().equals(lastCheckPerson) && flowNode != null && flowNode.isAllowCancel()) {
				Result rsDel = oamgt.queryDeleteAdvice(keyId, currentNode);
				if (rsDel.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					String ids = (String) rsDel.getRetVal();
					new AdviceMgt().deleteById(ids);
				}
				String lastNodeId = flowMap.get("lastNodeId")+"";

				rs = oamgt.cancel(keyId, lastNodeId, currentNode, loginBean, flowMap.get("applyType") + "");
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){					
					return new Message("ERROR", "ִ��ʧ��"+rs.retVal);
				}
				rs = oamgt.updateFlowDepict(flowMap.get("applyType") + "", keyId, new Locale(locale));
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){					
					return new Message("ERROR", "ִ��ʧ��"+rs.retVal);
				}else{
					return new Message("OK", GlobalsTool.getMessage(locale, "oa.msg.cancelSucc"));
				}
			} else {
				return new Message("ERROR", "��ǰ״̬�������أ������Ѿ���ˣ�");
			}
		} catch (BSFException e) {
			BaseEnv.log.error("MobileAjax.cancelTo Error,", e);
			return new Message("ERROR", "ִ�д���");
		}
	}
	
	public static Message cardScan(String key,String secret,String file){
		
		String ret = "";
		String typeId = "20";//��Ƭɨ��
		String url = BaseEnv.PicScanURL;
		String format = "json";
			
		//*******�ϴ���ͼƬ*******//
		try{
			HttpClient client = new HttpClient(); // 1.����httpclient����
			PostMethod post = new PostMethod(url); // 2.ͨ��url����post����
			post.setRequestHeader("Accept", "application/json");			
			FilePart fp = new FilePart("file", new File(file)); 
			 
			Part[] parts = {new StringPart("secret", secret),new StringPart("key", key),new StringPart("typeId", typeId),new StringPart("format", format),fp};  
			MultipartRequestEntity mre = new MultipartRequestEntity(parts, post.getParams());  
			post.setRequestEntity(mre);    
			post.getParams().setContentCharset("utf-8");
			client.executeMethod(post);
			ret = post.getResponseBodyAsString();
			return new Message("OK",ret);
		} catch(Exception e){
			BaseEnv.log.error("AIOConnect.cardScan ����post�����쳣��" , e);
			return new Message("ERROR","����apiʧ��");
		}
		//********end********//
	}
	
	public static Message toURL(String url,String data){
		if(!url.startsWith("http://")){
			url = "http://"+url;
		}
		
		long time = System.currentTimeMillis();		
//		String secData = des.Encode(password+strDefaultKey, "AB:"+json);
//		BaseEnv.log.debug("AIOConnect����Զ������url="+url+",time="+System.currentTimeMillis()+",data=��"+json);
		PrintWriter out = null;
		DataInputStream in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// �򿪺�URL֮�������
			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
			// ����ͨ�õ��������� 
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestMethod("POST");
			// ����POST�������������������
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// ��ȡURLConnection�����Ӧ�������
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8"));
			// �����������
			out.print("data="+data);
			// flush������Ļ���
			out.flush();
			// ����BufferedReader����������ȡURL����Ӧ
			in = new DataInputStream(conn.getInputStream());
			byte[] bs= new byte[0];
			byte[] b = new byte[1024];
			int count = 0;
			while ((count = in.read(b)) != -1) {
				byte[] temp = bs;
				bs = new byte[temp.length+count];
				System.arraycopy(temp, 0, bs, 0, temp.length);
				System.arraycopy(b, 0, bs, temp.length, count);
			}
			result = new String(bs,"UTF-8");
			BaseEnv.log.debug("AIOConnect����Զ�����󷵻أ�"+result);
			if(result==null || result.trim().length() ==0){
				Message msg = new Message("ERROR","�����δ�����κ�����");
				return msg;
			}
			//Message msg = gson.fromJson(result, Message.class);
			Message msg = new Message("OK",result);
			return msg;
		} catch (Exception e) {
			BaseEnv.log.error("AIOConnect���� POST ��������쳣��" , e);
			return new Message("ERROR",e.getMessage());
		}
		// ʹ��finally�����ر��������������
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				BaseEnv.log.error("AIOConnect���� POST  �ر����ӳ����쳣��" , ex);
			}
		}
	}
	
	public static Message toURL(String url,String password,HashMap map){
		if(!url.startsWith("http://")){
			url = "http://"+url;
		}
		if(!url.endsWith("AIOApi")){
			url = (url.endsWith("/")?url:url+"/")+"AIOApi";
		}
		long time = System.currentTimeMillis();
		map.put("time", time+""); 
		String json = gson.toJson(map);
		String secData="AB:"+json;
//		BaseEnv.log.debug("AIOConnect����Զ������ʼѹ����url="+url+",time="+System.currentTimeMillis());
//		json=ZipUtil.zip(json);
//		BaseEnv.log.debug("AIOConnect����Զ������ʼ���ܣ�url="+url+",time="+System.currentTimeMillis());
//		String secData = des.Encode(password+strDefaultKey, "AB:"+json);
//		BaseEnv.log.debug("AIOConnect����Զ������url="+url+",time="+System.currentTimeMillis()+",data=��"+json);
		PrintWriter out = null;
		DataInputStream in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// �򿪺�URL֮�������
			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
			// ����ͨ�õ��������� 
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestMethod("POST");
			// ����POST�������������������
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// ��ȡURLConnection�����Ӧ�������
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8"));
			// �����������
			out.print("secData="+secData);
			// flush������Ļ���
			out.flush();
			// ����BufferedReader����������ȡURL����Ӧ
			in = new DataInputStream(conn.getInputStream());
			byte[] bs= new byte[0];
			byte[] b = new byte[1024];
			int count = 0;
			while ((count = in.read(b)) != -1) {
				byte[] temp = bs;
				bs = new byte[temp.length+count];
				System.arraycopy(temp, 0, bs, 0, temp.length);
				System.arraycopy(b, 0, bs, temp.length, count);
			}
			result = new String(bs,"UTF-8");
			BaseEnv.log.debug("AIOConnect����Զ�����󷵻أ�"+result);
			if(result==null || result.trim().length() ==0){
				Message msg = new Message("ERROR","�����δ�����κ�����");
				return msg;
			}
			Message msg = gson.fromJson(result, Message.class);
			return msg;
		} catch (Exception e) {
			BaseEnv.log.error("AIOConnect���� POST ��������쳣��" , e);
			return new Message("ERROR",e.getMessage());
		}
		// ʹ��finally�����ر��������������
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				BaseEnv.log.error("AIOConnect���� POST  �ر����ӳ����쳣��" , ex);
			}
		}
	}
	
	public static Message update(final String url,String password,String userId,String tableName, String saveType,Connection conn,String sql) throws Exception{
		return _update(url,password,  userId, tableName, saveType, conn, sql);
	}
	public static Message update(final String url,String password, String userId,String tableName, String saveType,Connection conn,String sql,String sql1) throws Exception{
		return _update(url,password,  userId, tableName, saveType, conn, sql,sql1);
	}
	public static Message update(final String url,String password, String userId,String tableName, String saveType,Connection conn,String sql,String sql1,String sql2) throws Exception{
		return _update(url,password,  userId, tableName, saveType, conn, sql,sql1,sql2);
	}
	public static Message update(final String url,String password, String userId,String tableName, String saveType,Connection conn,String sql,String sql1,String sql2,String sql3) throws Exception{
		return _update(url,password,  userId, tableName, saveType, conn, sql,sql1,sql2,sql3);
	}
		
	
	/**
	 * ����Զ�̵�ַ�޸ı�����
	 * @param url
	 * @param locale
	 * @param userId
	 * @param tableName
	 * @param saveType
	 * @param defineInfo
	 * @param valuesJson
	 * @return
	 * @throws Exception
	 */
	private static Message _update(final String url,final String password, String userId,String tableName, String saveType,Connection conn,String ...sqls) throws Exception{
		HashMap map = new HashMap();
		map.put("locale", "zh_CN");
		map.put("userId", userId);
		map.put("tableName", tableName);
		map.put("defineInfo", "");
		map.put("saveType", saveType);
		map.put("deliverTo", "");
		map.put("op", "update");
		
		HashMap values = new HashMap();
		for(String sql:sqls){
			String cTable = "";
			if(sql.indexOf(":")>-1){
				//�����ӱ�����
				cTable = sql.substring(0,sql.indexOf(":"));
				sql = sql.substring(sql.indexOf(":")+1);
			}
			selectData(conn, sql, cTable, values);
		}
		String valuesJson = gson.toJson(values);
		map.put("values", valuesJson);
		Message msg = toURL(url,password, map); 
		if(!"OK".equals(msg.getCode())){
			throw new Exception("Զ��ִ�д���,"+msg.getMsg());
		}
		return msg;
	}
	
	private static void selectData(Connection conn,String sql,String cTable,HashMap values) throws Exception{
		//ת������е����Ķ���
		sql = sql.replaceAll("��", ",");
		BaseEnv.log.debug("AIOConnect.selectData sql="+sql);
		Statement pstmt =  conn.createStatement();
		ResultSet rs =pstmt.executeQuery(sql);
		ArrayList<HashMap> list = new ArrayList();
		while (rs.next()) {
        	HashMap map=new HashMap();
        	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
        		String obj=rs.getString(i);
        		if(obj==null){
        			if(rs.getMetaData().getColumnType(i)==Types.NUMERIC||rs.getMetaData().getColumnType(i)==Types.INTEGER){
        				map.put(rs.getMetaData().getColumnName(i), "0");
        			}else{
        				map.put(rs.getMetaData().getColumnName(i), "");
        			}
        		}else{
        			map.put(rs.getMetaData().getColumnName(i), obj);
        		}
        	}
        	list.add(map);
        }
		if(cTable==null||cTable.length()==0){
			//��������
			values.putAll(list.get(0));
		}else{
			//������ϸ��
			values.put("TABLENAME_"+cTable, list);
		}
	}
	
	public static Message add(final String url,String password, String userId,String tableName,String parentCode,String saveType,Connection conn,String sql) throws Exception{
		return _add(url,password, userId, tableName, parentCode, saveType, conn, sql);
	}
	public static Message add(final String url,String password, String userId,String tableName,String parentCode,String saveType,Connection conn,String sql,String sql1) throws Exception{
		return _add(url,password, userId, tableName, parentCode, saveType, conn, sql,sql1);
	}
	public static Message add(final String url,String password, String userId,String tableName,String parentCode,String saveType,Connection conn,String sql,String sql1,String sql2) throws Exception{
		return _add(url,password,  userId, tableName, parentCode, saveType, conn, sql,sql1,sql2);
	}
	public static Message add(final String url,String password, String userId,String tableName,String parentCode,String saveType,Connection conn,String sql,String sql1,String sql2,String sql3) throws Exception{
		return _add(url,password,  userId, tableName, parentCode, saveType, conn, sql,sql1,sql2,sql3);
	}
		
	
	/**
	 * ����Զ�̵�ַ���������
	 * @param url
	 * @param locale
	 * @param userId
	 * @param tableName
	 * @param parentCode
	 * @param defineInfo
	 * @param saveType
	 * @param valuesJson
	 * @return
	 * @throws Exception
	 */
	private static Message _add(final String url,String password, String userId,String tableName,String parentCode,String saveType,Connection conn,String ...sqls) throws Exception{
		HashMap map = new HashMap();
		map.put("locale", "zh_CN");    
		map.put("userId", userId);
		map.put("tableName", tableName);
		map.put("parentCode", parentCode);
		map.put("defineInfo", "");
		map.put("saveType", saveType);
		
		HashMap values = new HashMap();
		for(String sql:sqls){
			String cTable = "";
			if(sql.indexOf(":")>-1){
				//�����ӱ�����
				cTable = sql.substring(0,sql.indexOf(":"));
				sql = sql.substring(sql.indexOf(":")+1);
			}
			selectData(conn, sql, cTable, values);
		}
		String valuesJson = gson.toJson(values);
		map.put("values", valuesJson);
		map.put("deliverTo", "");
		map.put("op", "add");
		Message msg = toURL(url,password, map); 
		if(!"OK".equals(msg.getCode())){
			throw new Exception("Զ��ִ�д���,"+msg.getMsg());
		}
		return msg;
	}
		
	/**
	 * ����Զ�̵�ִַ��define����
	 * @param url
	 * @param locale
	 * @param userId
	 * @param defineName
	 * @param buttonTypeName
	 * @param keyId
	 * @param tableName
	 * @param defineInfo
	 * @return
	 * @throws Exception
	 */
	public static Message  execDefine(final String url,String password, String userId,String defineName, String buttonTypeName,String keyId,String tableName) throws Exception{
		HashMap map = new HashMap();
		map.put("locale", "zh_CN");
		map.put("userId", userId);
		map.put("defineName", defineName);
		map.put("buttonTypeName", buttonTypeName);
		map.put("keyId", keyId);
		map.put("tableName", tableName);
		map.put("defineInfo", "");
		map.put("op", "execDefine");
		Message msg = toURL(url,password, map); 
		if(!"OK".equals(msg.getCode())){
			throw new Exception("Զ��ִ�д���,"+msg.getMsg());
		}
		return msg;
	}
		
	
	/**
	 * ֱ��ִ����˵����¸��ڵ�����񣬲�ָ��urlȥִ����һ�������������
	 * ������define�е��û����һ�������������õķ������ӣ��������ٿ�����ͬһ�����У����Բ������̵߳ķ�ʽ��ִ�У���������Ƕ�״���
	 * ���define�ڵ����������ʱҪע��ִ��ʱ�������ǲ���������Ƶģ���Ҫ����define�Ŀ�ʼ��Ӧ�÷������
	 * @param locale
	 * @param userId
	 * @param keyId
	 * @param tableName
	 * @return
	 */
	public static Message deliverToNext(final String url,String password, final String userId,final String keyId,final String tableName)  throws Exception{
		HashMap map = new HashMap();
		map.put("locale", "zh_CN");
		map.put("userId", userId);
		map.put("keyId", keyId);
		map.put("tableName", tableName);
		map.put("op", "deliverToNext");
		Message msg = toURL(url,password, map); 
		if(!"OK".equals(msg.getCode())){
			throw new Exception("Զ��ִ�д���,"+msg.getMsg());
		}
		return msg;
	}
	/**
	 * ��������������define�е��ã�ִ����˵���һ���ڵ���������ںܶ��Զ����ɵı�����ϣ��ֱ�ӽ����һ����˽ڵ㣬
	 * ������define�е��û����һ�������������õķ������ӣ��������ٿ�����ͬһ�����У����Բ������̵߳ķ�ʽ��ִ�У���������Ƕ�״���
	 * ���define�ڵ����������ʱҪע��ִ��ʱ�������ǲ���������Ƶģ���Ҫ����define�Ŀ�ʼ��Ӧ�÷������
	 * @param locale
	 * @param userId
	 * @param keyId
	 * @param tableName
	 * @return
	 */
	public static Message deliverToNext(final String userId,final String keyId,final String tableName) throws Exception{
		final LoginBean loginBean = new LoginBean(userId,"");
		final ArrayList<Message> list = new ArrayList();
		Thread thread = new Thread(){
			public void run(){
				Message msg = deliverToNext("zh_CN", loginBean, keyId, tableName);
				list.add(msg);
			}
		};
		thread.start();
		//�ȴ��߳�ִ�н�����
		do{
			try{
			Thread.sleep(500);
			}catch(Exception e){}
		}while(thread.isAlive());
		
		if(!"OK".equals(list.get(0).getCode())){
			throw new Exception("Զ��ִ�д���"+list.get(0).getMsg());
		}
		return list.get(0);
	}
	
	
	/**
	 * ��˵���һ���ڵ㣬���ںܶ��Զ����ɵı�����ϣ��ֱ�ӽ����һ����˽ڵ㣬
	 * 
	 * @param keyId ��˵���ID
	 * @param tableName ��˱���
	 * <br/><br/>
	 * ����ֵ: {code:'OKִ�гɹ���CONFIRM������ȷ�Ͽ�ERROR��ִ�д���',msg����ʾ��Ϣ}
	 */
	public static Message deliverToNext(String locale,LoginBean loginBean,String keyId,String tableName) {
		Message msg = deliverToPrepare(locale, loginBean, keyId, tableName, "");
		if(!msg.getCode().equals("OK")){
			return msg;
		}
		HashMap<String,Object> map = (HashMap<String,Object>)msg.getObj();
		MFlowNodeBean mcurrNB = (MFlowNodeBean)map.get("currNode");
		ArrayList<MFlowNodeBean> mnodeBeans = (ArrayList<MFlowNodeBean>)map.get("nextNodes");
		String designId = map.get("designId")+"";
		String moduleName = map.get("moduleName")+"";
		MFlowNodeBean mnextNB = mnodeBeans.get(0);
		String checkPersion = "";
		for(String[] ss:mnextNB.getCheckPeople()){
			checkPersion +=";"+ss[0];
		}
		if(checkPersion.length() > 0)
			checkPersion = checkPersion.substring(1);
		msg = deliverTo(locale, loginBean, keyId, tableName, mnextNB.getKeyId(), mcurrNB.getKeyId(), designId, 
				checkPersion, "", "", "", "0", "0");
		return msg;
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
	public static Message deliverTo(String locale,LoginBean loginBean,String keyId,String tableName,String nextStep,String currNode,String designId,
			String checkPerson,String deliverance,String userIds,String deptIds,String oaTimeLimit,String oaTimeLimitUnit) {
		try {
			
			String type = ""; 

			String msg = GlobalsTool.getMessage(locale, "oa.msg.deliverToSucc");


			OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(designId);

			if (keyId == null && keyId.length() == 0) {
				return new Message("ERROR", "���ݱ�Ų���Ϊ�գ�");
			}

			String[] checkPersons = checkPerson.split(";");

			/*����ERP���ݵķ���·��*/
			String approveStatus = "transing";

			/*���¸������͵��������� ����ת��,���أ�ת��������*/
			Result rs = new Result();
			if (checkPerson == null || checkPerson.length() == 0 && !"-1".equals(nextStep)) {
				return new Message("ERROR", "�����˲���Ϊ�գ�");
			}
			/**ת��������*/
			if ((checkPersons == null || checkPersons.length == 0) && !"-1".equals(nextStep)) {
				return new Message("ERROR", "�����˲���Ϊ�գ�");
			}
			String appendWake = deliverance;
			//ת�������º�̨�¸������Ϣ
			rs = oamgt.deliverTo(keyId, nextStep, checkPersons, currNode, loginBean, designId,new Locale(locale), deliverance, "", oaTimeLimit, oaTimeLimitUnit, appendWake,
					getResources(), userIds, deptIds, type);

			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				/*��������������Ϣ*/
				rs = oamgt.updateFlowDepict(designId, keyId, new Locale(locale));
			}
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				if ("cancel".equals(nextStep)) {//����
					return new Message("OK", "���سɹ���");
				}else{
					return new Message("OK", "�����ɹ���");
				}
			} else {
				SaveErrorObj saveErrrorObj = new DynDBManager().saveError(rs, locale, "");
				return new Message("ERROR", saveErrrorObj.getMsg());
			}
		} catch (Exception e) {
			return new Message("ERROR", e.getMessage());
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
	public static Message check(String locale,LoginBean loginBean,String keyId, String tableName,String defineInfo) {
		try {
			
			HashMap flowMap = oamgt.getOAMyWorkFlowInfo(keyId, tableName);
			OAWorkFlowTemplate template = flowMap == null ? BaseEnv.workFlowInfo.get(tableName) : BaseEnv.workFlowInfo.get(flowMap.get("applyType"));
			
			
			final Result billDetail = dbmgt.detail(tableName, BaseEnv.tableInfos, keyId, loginBean.getSunCmpClassCode(),BaseEnv.propMap ,loginBean.getId(),true,"");
			HashMap values = (HashMap) billDetail.getRetVal() ;
			//ִ��һ��update����
			if(values.get("BillDate")!=null && !values.get("BillDate").equals("")){
				int periodYear=Integer.parseInt(values.get("BillDate").toString().substring(0,4));
				int period=Integer.parseInt(values.get("BillDate").toString().substring(5,7));
				
				Hashtable ht = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
				int curPeriodYear=Integer.parseInt(ht.get("NowYear").toString());
				int curPeriod=Integer.parseInt(ht.get("NowPeriod").toString());
				
				if(periodYear<curPeriodYear||(periodYear==curPeriodYear&&period<curPeriod)){
					return new Message("ERROR", GlobalsTool.getMessage(locale,"com.currentaccbefbill"));
				} 
			}
			
			if(template==null || template.getTemplateStatus() == 0){
				values.put("workFlowNodeName", "finish") ;
				values.put("workFlowNode", "-1");
        		values.put("checkPersons", "") ;
        		values.put("finishTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			}else if("draft".equals(values.get("workFlowNodeName"))){
				values.put("workFlowNodeName", "notApprove") ;
				values.put("workFlowNode", "0") ;
            	values.put("checkPersons", ";"+loginBean.getId()+";") ;
            	values.put("finishTime","");
			}
			values.put("lastUpdateTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			
			//Ҫִ�е�define����Ϣ
	        
	        DBTableInfoBean tb = BaseEnv.tableInfos.get(tableName);
	        String parentTableName = tb==null?"":tb.getPerantTableName();
	        String moduleType = values.get("moduleType")==null?"":values.get("moduleType")+"";
	        MOperation mop = GlobalsTool.getMOperationMap(parentTableName, tableName, moduleType, loginBean);
			Hashtable props = BaseEnv.propMap;
			Object obj = BaseEnv.servletContext.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
			MessageResources resources = null;
			if (obj instanceof MessageResources) {
				resources = (MessageResources) obj;
			}
	        Result updateRs = dbmgt.update(tableName, BaseEnv.tableInfos, values,loginBean.getId(), 
					defineInfo,resources,new Locale(locale),"",loginBean,template,props);	        
	        
	        if (updateRs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
				//�Զ���������Ҫ�û�ȷ��
				ConfirmBean confirm = (ConfirmBean) updateRs.getRetVal();
				String content = dbmgt.getDefSQLMsg(locale, confirm.getMsgContent());
				String jsConfirmYes = "";
				String jsConfirmNo = "";

				jsConfirmYes = confirm.getFormerDefine() + ":" + confirm.getYesDefine() + ";";
				if (confirm.getNoDefine().length() > 0) {
					jsConfirmNo = confirm.getFormerDefine() + ":" + confirm.getNoDefine() + ";";
				}
				Message msg = new Message("CONFIRM", content + "#;#" + jsConfirmYes + "#;#" + jsConfirmNo);
				return msg;
			}else  if (updateRs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
				SaveErrorObj saveErrrorObj = dbmgt.saveError(updateRs, locale, "");
				Message msg = new Message("ERROR", saveErrrorObj.getMsg());
				return msg;
			}else{
				Message msg = new Message("OK", "�����޸�");
				return msg;
			}
		} catch (Exception e) {
			return new Message("ERROR", e.getMessage());
		}
	}

	private static MessageResources getResources() {

		return (MessageResources) BaseEnv.servletContext.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
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


}
