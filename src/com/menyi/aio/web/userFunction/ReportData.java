package com.menyi.aio.web.userFunction;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.write.Alignment;
import jxl.write.Border;
import jxl.write.BorderLineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.bsf.BSFManager;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.mobile.bean.MobileAttrs;
import com.koron.mobile.bean.MobileDetail;
import com.koron.mobile.bean.MobileListData;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.menyi.aio.bean.ColConfigBean;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.bean.EnumerateItemBean;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.bean.ReportsBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.report.ReportDataMgt;
import com.menyi.aio.web.report.ReportSetMgt;
import com.menyi.aio.web.report.TableListResult;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConfigParse;
import com.menyi.web.util.DefineReportBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.ListGrid;
import com.menyi.web.util.ListGridHtml;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.PublicMgt;
import com.menyi.web.util.ReportField;
  
public class ReportData {
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	/**
	 * ���ݱ���
	 */
	public static final String BILL = "BILL";

	/**
	 * ���౨��
	 */
	public static final String CLASS = "CLASS";

	/**
	 * ��ͨ����
	 */
	public static final String NORMAL = "NORMAL";

	/**
	 * ���ݱ��б�
	 */
	public static final String TABLELIST = "TABLELIST";

	/**
	 * ��ʱ����
	 */
	public static final String TEMPTABLE = "TEMPTABLE";

	/**
	 * �洢���̱���
	 */
	public static final String PROCLIST = "PROCLIST";

	public static Integer rowSize = 0;

	public static String winCurIndex = "";

	/**
	 * �����б�������չ��ť
	 * @param tableInfo DBTableInfoBean ��ṹ
	 * @param operation String ��������
	 * @return String ����һ��<button>��ť
	 *
	 * <button type="copy" operation="list">
	 * <button type="quote" operation="add" value="tblBuyOrder:quote_buyOrder">
	 * <button type="define" operation="list" value="activeX">
	 */

	private void parseExtendButton(HttpServletRequest request, ReportsBean reportSetBean) throws Exception {

		StringBuffer queryBtn = new StringBuffer(); //��������а�Ť
		StringBuffer toolBtn = new StringBuffer(); //���ߵ����а�Ť
		String extendButton = reportSetBean.getExtendsBut();
		int n = 1; //��¼�м�����չ���ð�ť
		if (extendButton != null) {
			while (extendButton.indexOf("<button") > -1) {
				String button = extendButton.substring(extendButton.indexOf("<button"), extendButton.indexOf(">"));
				extendButton = extendButton.substring(extendButton.indexOf(">") + 1);
				String type = getAttribute("type", button);

				String buttonName = "";
				if (button.indexOf("name=") > 0) {
					buttonName = button.substring(button.indexOf("name=\"") + "name=\"".length(), button.indexOf("\"", button.indexOf("name=\"") + "name=\"".length()));
				}
				if (type.equals("tool")) { //���� 
					n++;
					if (buttonName == null || buttonName.length() == 0)
						buttonName = "����";

					String buttonDis = null;
					if (buttonDis == null) {
						buttonDis = buttonName;
					}
					toolBtn.append(
							" <a href=\"javascript:billTool('" + getAttribute("value", button) + "','" + buttonDis + "','" + getAttribute("width", button) + "','" + getAttribute("height", button)
									+ "')\">").append(buttonDis).append("</a>");

				} else if (type.equals("query")) { //����
					n++;
					if (buttonName == null || buttonName.length() == 0)
						buttonName = "����";

					String buttonDis = null;
					if (buttonDis == null) {
						buttonDis = buttonName;
					}
					queryBtn.append(
							" <a href=\"javascript:billQuery('" + getAttribute("value", button) + "','" + buttonDis + "','" + getAttribute("width", button) + "','" + getAttribute("height", button)
									+ "')\">").append(buttonDis).append("</a>");

				}
			}
		}
		/*���ð�ť��������һ��*/
		request.setAttribute("toolBtn", toolBtn.toString());
		request.setAttribute("queryBtn", queryBtn.toString());
	}

	private String getAttribute(String attr, String str) {
		//zxy �޸�   =\"([/\\?&\\=\\,\\@\\.\\w\\:\\-\\u4e00-\\u9fa5]+)\"
		Pattern pattern = Pattern.compile(attr + "=\"([^\"]+)\"");
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}

	/**
	 * ��ʾ����
	 * 
	 * @param request
	 * @param scopeRight
	 * @param reportNumber
	 *            ��������
	 * @param reportType
	 *            ��������
	 * @param pageNo
	 *            ���ڼ�ҳ
	 * @param pageSize
	 *            ��ÿҳ��¼��
	 * @param scopeRightUpdate
	 * @param scopeRightDel
	 * @param loginBean
	 *            ����¼��Ϣ
	 * @return
	 * @throws Exception
	 */
	public Result showData(MOperation mop, HttpServletRequest request, ArrayList scopeRight, String reportNumber, DefineReportBean defBean,String reportType, int pageNo, int pageSize, ArrayList scopeRightUpdate,
			ArrayList scopeRightDel, LoginBean loginBean, String detTable_list) throws Exception {
		this.winCurIndex = request.getParameter("winCurIndex");// mj
		Result rs = new Result();
		ReportDataMgt mgt = new ReportDataMgt();

		String locale = GlobalsTool.getLocale(request).toString();
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		// �������룬�ں����п��ܻ��滻���ڻ��滻����û����ʲô����»��滻
		String parentCode = request.getParameter("parentCode") == null ? "" : request.getParameter("parentCode");

		request.getSession().setAttribute("nextMap", null);
		ReportSetMgt setMgt = new ReportSetMgt();

		ReportsBean reportSetBean = (ReportsBean) setMgt.getReportSetInfo(reportNumber, locale).getRetVal();
		
		if(reportSetBean == null){
			rs.retCode= ErrorCanst.DEFAULT_FAILURE;
			rs.retVal = "����"+reportNumber+"������";
			return rs;
		}
		if(defBean == null){
			rs.retCode= ErrorCanst.DEFAULT_FAILURE;
			rs.retVal = "�����ļ�"+reportNumber+"SQL.xml������";
			return rs;
		}
		if (reportSetBean.getPageSize() != null && reportSetBean.getPageSize().intValue() > 0 && pageSize == 0) {
			pageSize = reportSetBean.getPageSize().intValue();
			System.out.println(reportSetBean.getPageSize().intValue());
			if (reportSetBean.getPageSize().intValue() == -1)
				request.setAttribute("notShowPage", "-1");
		}
		if (pageSize == 0) {
			pageSize = GlobalsTool.getPageSize();
		}
		//������չ��Ť
		parseExtendButton(request, reportSetBean);

		request.setAttribute("fixListTitle", reportSetBean.getFixListTitle());
		request.setAttribute("pageSize", pageSize);
		
		request.setAttribute("disFields", defBean.getDisFields());
		request.setAttribute("disFields2", defBean.getDisFields2());
		request.setAttribute("statFields", defBean.getStatFields());
		// ���ð汾��Ϣ���ͻ��˿��Ը��ݲ�ͬ�İ汾��Ϣ
		// request.setAttribute("reportversion", defBean.getVersion());
		ArrayList condField = defBean.getConFields();
		ArrayList disField = defBean.getDisFields();

		// �Ƿ�ּ�����=1��ʱ������Ƿּ��б������Ƿּ��б����зּ��б��ʱ�򣬱����������飬δ�����У�
		String reportIfClass = "";
		if (defBean.getClassCode() != null) {
			reportIfClass = "1";
			request.setAttribute("reportIfClass", "1");
		}
		/*
		if (defBean.getClassCode() != null) {
			reportIfClass = "1";
			request.setAttribute("reportIfClass", "1");
			long time = System.currentTimeMillis();
			if (!TABLELIST.equals(reportSetBean.getReportType())) {
				// �����Ƿּ����������û�ѡ�������������������
				String parentTable = defBean.getClassCode().getFieldName().substring(0, defBean.getClassCode().getFieldName().indexOf("."));
				//zxy,����ʹ�ñ����ᵼ���Ҳ����������
				if (BaseEnv.tableInfos.get(parentTable) == null) {
					rs.retCode = ErrorCanst.DEFAULT_FAILURE;
					rs.retVal = "�����ֶ�[" + defBean.getClassCode().getFieldName() + "]��Ӧ�ı���[" + parentTable + "]�����ڣ��ּ�����Ӧʹ����ȷ�ı���";
					return rs;
				}
				boolean isSpec = false;
				String sql = "";
				ArrayList param = new ArrayList();
				for (int i = 0; i < condField.size(); i++) {
					ReportField field = (ReportField) condField.get(i);
					String value = request.getParameter(field.getAsFieldName());
					// ��ʱ���߼�̫���ӣ�û����
					if (field.getWidth() != null && !field.getWidth().equals("")
							&& (Integer.parseInt(field.getWidth()) > 0 || (Integer.parseInt(field.getWidth()) == -1 && !"true".equals(request.getParameter("isnext"))))
							&& field.getFieldName().substring(0, field.getFieldName().indexOf(".")).equals(parentTable) && value != null && value.length() > 0) {
						if (DDLOperation.getFieldInfo(BaseEnv.tableInfos, parentTable, field.getFieldName().substring(field.getFieldName().indexOf(".") + 1)) == null) {
							isSpec = true;
						}
						param.add(value.split(",")[0]);
						sql += " and " + field.getFieldName() + "=?";
					}
				}
				if (sql.length() > 0) {
					AIODBManager aioMgt = new AIODBManager();
					if (isSpec) {
						if (sql.contains("GoodsFullName")) {
							sql = "select classCode from tblGoods where 1=1 " + (sql.replaceAll(parentTable, "tblGoods"));
						}
					} else {
						sql = "select classCode from " + parentTable + " where 1=1 " + sql;
					}
					Result rst = aioMgt.sqlList(sql, param);
					if (rst.retCode == ErrorCanst.DEFAULT_SUCCESS && rst.retVal != null) {
						List list = (List) rst.retVal;
						if (list.size() > 0) {
							String classCode = ((Object[]) list.get(0))[0].toString();
							parentCode = classCode.substring(0, classCode.length() - 5);
						}
					}
					BaseEnv.log.debug("��ѯ�����ķ������ʱ�䣺" + (System.currentTimeMillis() - time));
				}
			}

		}
		*/

		String type = request.getParameter("type") == null ? "" : request.getParameter("type");
		if (type.equals("back") && parentCode.length() >= 5) {
			parentCode = parentCode.substring(0, parentCode.length() - 5);
		}

		/**
		 * Ԥ�����⴦��
		 */
		Object sysAlert = request.getAttribute("sysAlert"); // �����ж��Ƿ���Ԥ�����ܵ��ø÷���true
		HashMap parameterMap = new HashMap();
//		if (sysAlert != null && "true".equals(sysAlert)) {
//			parameterMap = (HashMap) request.getAttribute("parameterMap");
//		}

		String selectType = request.getParameter("selectType");
		request.setAttribute("selectType", selectType);
		ArrayList conditions = new ArrayList();
		ArrayList cols = new ArrayList();
		ArrayList rows = new ArrayList();

		ArrayList configField = new ArrayList();

		OAWorkFlowTemplate workFlowTemp = BaseEnv.workFlowInfo.get(reportNumber);
		ReportField reportField = null;
		String approveStatus = "all";

		/* �ݸ��ѯ */
		String draftQuery = request.getParameter("draftQuery");
		request.setAttribute("draftQuery", draftQuery);
		if (null != workFlowTemp && null != workFlowTemp.getTemplateFile() && !"".equals(workFlowTemp.getTemplateFile()) && workFlowTemp.getTemplateStatus() == 1 && !"draft".equals(draftQuery)
				&& !"draftPop".equals(draftQuery)) {
			if (workFlowTemp.getDefStatus() != null && workFlowTemp.getDefStatus().length() > 0) {
				approveStatus = workFlowTemp.getDefStatus();
			}

			request.setAttribute("isStatart", "true"); //�Ƿ����������
			reportField = new ReportField();
			reportField.setAsFieldName("workFlowNodeName");
			reportField.setClassCode("1");
			reportField.setCondition(" = ?");
			reportField.setConditionJoin("and");
			reportField.setDefaultValue(approveStatus);
			reportField.setDisplay("���״̬");
			reportField.setDisplayFlag("1");
			reportField.setFieldName(reportNumber + ".workFlowNodeName");
			reportField.setFieldSysType("");
			reportField.setFieldType((byte) 2);
			reportField.setGroupByFlag("0");
			reportField.setInputType((byte) 1);
			reportField.setIsStat("0");
			reportField.setLinkAdd("");
			reportField.setOrder("6");
			reportField.setOrderByFlag("");
			reportField.setPopUpName("ApproveStatus");
			reportField.setWidth("0");
			condField.add(reportField);
			

			WorkFlowDesignBean designBean = BaseEnv.workFlowDesignBeans.get(workFlowTemp.getId());
			ArrayList<KeyPair> nodes = new ArrayList<KeyPair>();
			nodes.add(new KeyPair("ȫ��","all"));
			nodes.add(new KeyPair("δ���","notApprove"));
			nodes.add(new KeyPair("��˽���","-1"));
			if (designBean != null) {
				HashMap<String, FlowNodeBean> nodeMap = designBean.getFlowNodeMap();
				if(nodeMap != null){
					FlowNodeBean tfb = nodeMap.get("0");
					nodes.add(new KeyPair(tfb.getDisplay(),tfb.getKeyId()));
					for(FlowNodeBean fnb :nodeMap.values()){
						if(!fnb.getKeyId().equals("-1") && !fnb.getKeyId().equals("0")){
							nodes.add(new KeyPair(fnb.getDisplay(),fnb.getKeyId()));
						}
					}
				}
			}
			nodes.add(new KeyPair("�ݸ�","draft"));
			request.setAttribute("nodes", nodes);
			

			String sql = defBean.getSql();
			int indOrder = sql.lastIndexOf("order by ");
			int indGroup = sql.lastIndexOf("group by");
			if (indOrder >= 0 && indGroup >= 0) {
				sql = sql.substring(0, indGroup) + " and (" + reportField.getFieldName() + " = ?) " + sql.substring(indGroup);
			} else if (indOrder >= 0) {
				sql = sql.substring(0, indOrder) + " and (" + reportField.getFieldName() + " = ?) " + sql.substring(indOrder);
			} else {
				sql = sql + " and (" + reportField.getFieldName() + " = ?) ";
			}

			defBean.getFieldInfo().add(reportField);
			defBean.getQueryFields().add(reportField);

			/* ���빤������ǰ�ڵ� */
			ReportField fieldNode = new ReportField();
			fieldNode.setAsFieldName("workFlowNode");
			fieldNode.setFieldName(reportSetBean.getReportNumber() + ".workFlowNode");
			fieldNode.setFieldType(DBFieldInfoBean.FIELD_ANY);
			defBean.getFieldInfo().add(fieldNode);
			fieldNode.setDisplayFlag("1");
			defBean.getQueryFields().add(fieldNode);

			/* ���빤������ǰ�ڵ������ */
			ReportField checkPerson = new ReportField();
			checkPerson.setAsFieldName("checkPersons");
			checkPerson.setFieldName(reportNumber + ".checkPersons");
			checkPerson.setFieldType(DBFieldInfoBean.FIELD_ANY);
			checkPerson.setDisplayFlag("1");
			defBean.getFieldInfo().add(checkPerson);
			defBean.getQueryFields().add(checkPerson);

			/* ���빤������ǰ�ڵ����һ�ڵ� */
			ReportField lastNodeID = new ReportField();
			lastNodeID.setAsFieldName("lastNodeID");
			lastNodeID.setFieldName("(select lastNodeId from OAMyWorkFlow where OAMyWorkFlow.keyID=" + reportNumber + ".id " + "and OAMyWorkFlow.tableName='" + reportNumber + "' and '"
					+ loginBean.getId() + "' = OAMyWorkFlow.lastCheckPerson " + "and " + reportNumber + ".workFlowNode!='-1' )");
			lastNodeID.setFieldType(DBFieldInfoBean.FIELD_ANY);
			lastNodeID.setDisplayFlag("1");
			defBean.getFieldInfo().add(lastNodeID);
			defBean.getQueryFields().add(lastNodeID);

			sql = sql.substring(0, 6) + " " + fieldNode.getFieldName() + " as workFlowNode," + reportField.getFieldName() + " as workFlowNodeName," + checkPerson.getFieldName() + " as checkPersons,"
					+ lastNodeID.getFieldName() + " as lastNodeID, " + sql.substring(6);
			defBean.setSql(sql);
		} else if (TABLELIST.equals(reportSetBean.getReportType()) && !"draftPop".equals(draftQuery) && GlobalsTool.getTableInfoBean(reportNumber).getDraftFlag() == 1) {
			reportField = new ReportField();
			reportField.setAsFieldName("workFlowNodeName");
			reportField.setClassCode("1");
			reportField.setCondition(" = ?");
			reportField.setConditionJoin("and");
			reportField.setDefaultValue(approveStatus);
			reportField.setDisplay("���״̬");
			reportField.setDisplayFlag("1");
			reportField.setFieldName(reportNumber + ".workFlowNodeName");
			reportField.setFieldSysType("");
			reportField.setFieldType((byte) 2);
			reportField.setGroupByFlag("0");
			reportField.setInputType((byte) 1);
			reportField.setIsStat("0");
			reportField.setLinkAdd("");
			reportField.setOrder("6");
			reportField.setOrderByFlag("");
			reportField.setPopUpName("ApproveStatus2");
			reportField.setWidth("0");
			condField.add(reportField);
			
			ArrayList<KeyPair> nodes = new ArrayList<KeyPair>();
			
			nodes.add(new KeyPair("ȫ��","all"));
			nodes.add(new KeyPair("����","finish"));
			nodes.add(new KeyPair("�ݸ�","draft"));
			request.setAttribute("nodes", nodes);

			String sql = defBean.getSql();
			int indOrder = sql.lastIndexOf("order by ");
			int indGroup = sql.lastIndexOf("group by");
			if (indOrder >= 0 && indGroup >= 0) {
				sql = sql.substring(0, indGroup) + " and (" + reportField.getFieldName() + " = ?) " + sql.substring(indGroup);
			} else if (indOrder >= 0) {
				sql = sql.substring(0, indOrder) + " and (" + reportField.getFieldName() + " = ?) " + sql.substring(indOrder);
			} else {
				sql = sql + " and (" + reportField.getFieldName() + " = ?) ";
			}

			defBean.getFieldInfo().add(reportField);
			defBean.getQueryFields().add(reportField);

			sql = sql.substring(0, 6) + " " + reportField.getFieldName() + " as workFlowNodeName, " + sql.substring(6);
			defBean.setSql(sql);
		}
		
		request.setAttribute("oldCondField", condField);

		ArrayList<String> procParams = new ArrayList<String>();

		request.setAttribute("NoButton", request.getParameter("NoButton"));//�絥�����֣�����Ҫ�κΰ�Ť����
		String linkType = request.getParameter("LinkType");
		if (linkType != null)
			linkType = linkType.trim();
		String statType = "";

		HashMap conditionSessionMap = (HashMap) request.getSession().getAttribute("conditinSessionMap");
		String moduleNumber = "";
		String moduleType = request.getParameter("moduleType");
		if (moduleType != null && moduleType.trim().length() > 0) {
			moduleNumber = "-" + moduleType;
		}
		if (TABLELIST.equals(reportSetBean.getReportType()) && parentCode != null && conditionSessionMap != null && conditionSessionMap.get(reportNumber+moduleType + "parentCode") != null
				&& !parentCode.equals(conditionSessionMap.get(reportNumber+moduleType + "parentCode"))) {
			conditionSessionMap = null;
			request.getSession().setAttribute("conditinSessionMap", null);
		}

		if (conditionSessionMap == null) {
			conditionSessionMap = new HashMap();
			request.getSession().setAttribute("conditinSessionMap", conditionSessionMap);
		}
		
		//���ӱ���ָ�����ĸ��������ӹ����ģ�������������ڴ���ȡԭ�������������Ϊ�������ܴӽ��������������Ҫ���ڴ���ȡ
		HashMap linkFromConditionMap = null;
		String reportCond = request.getParameter("reportCond"); 
		if(reportCond != null && reportCond.length() > 0){
			linkFromConditionMap = (HashMap)conditionSessionMap.get(reportCond);
		}

		defBean.setSql(defBean.getSql().replaceAll("@condition:classLen", String.valueOf((parentCode.length() + 5))));

		//��δ���������ע�͹���ԭ�������������û����δ���ᵼ�µ�����ǣ��Ӳɹ�ѯ������ɹ����������ѯ�۵��ţ��ٴ����뵥�����鶩���������ѯ�۵��Ŷ��鲻�����ݣ�����
		//��δ��벻��ע�ͣ����������鱨��Ҫ��src=menu,ע��ǰ������bug�Ժ�����˵.
		if ("menu".equals(request.getParameter("src")) || (sysAlert != null && "true".equals(sysAlert))) {
			conditionSessionMap.remove(reportNumber + moduleNumber);
		}
		//��ѯ�û��Զ����ѯ��������
		ArrayList<String[]> userConditions = new ArrayList();
		Result rscond = mgt.loadDefAddanceCond(reportNumber,loginBean.getId());
		if(rscond.retCode == ErrorCanst.DEFAULT_SUCCESS && rscond.retVal != null){
			userConditions = (ArrayList)rscond.retVal;
		}
		boolean hasConditionValue = false;//���ڼ�¼�Ƿ��в�ѯ����������Ǵ����ӹ����Ĵ��в�ѯ�����ģ�����ֱ�Ӳ�ѯ����
		boolean noClassConditon = false;//���ڼ�¼�������������Զ�����classCode�Ƿ������ݣ�
		for (int i = 0; i < condField.size(); i++) {
			ReportField field = (ReportField) condField.get(i);
			//��������а���DateType,��������һ��ͳ�����͵ı�������dateType��ֵҪ����classCode�ֶεı�ʶ
			if (field.getAsFieldName().equals("DataType") && request.getParameter("DataType") != null && request.getParameter("DataType").length() > 0 && defBean.getClassCode() != null) {
				//�ֿ�:StockCode;����:DepartmentCode;�ͻ�:CompanyCode;ְԱ:EmployeeID  
				//��Ʒ:tblGoods;�ͻ�:tblCompany;ְԱ:tblEmployee;����:tblDepartment;�ֿ�:tblStock 
				String tabName = request.getParameter("DataType");
				if (tabName.equals("StockCode"))
					tabName = "tblStock";
				else if (tabName.equals("DepartmentCode"))
					tabName = "tblDepartment";
				else if (tabName.equals("CompanyCode"))
					tabName = "tblCompany";
				else if (tabName.equals("EmployeeID"))
					tabName = "tblEmployee";
				ReportField classCode = defBean.getClassCode();
				if(classCode != null){
					classCode.setFieldName(tabName + ".classCode");
				}
			}

			String sql = defBean.getSql();


			String fieldName = field.getFieldName();
			String fieldIdentity = field.getFieldIdentity();
			boolean flag = true;
			flag = DynDBManager.getViewRight(reportNumber, fieldName, scopeRight, fieldIdentity, loginBean);
			if (!flag) {
				procParams.add("");
				//��������ֶ�ûȨ����ʾ�����������ֶε�@condsent_����ȫ��ɾ��
				while (sql.indexOf("@condsent_" + field.getAsFieldName()) > 0) {
					int indexs = sql.indexOf("@condsent_" + field.getAsFieldName() + ":[");
					int indexe = sql.indexOf("]", indexs);
					sql = sql.substring(0, indexs) + sql.substring(indexe + 1);
				}
				defBean.setSql(sql);
				continue;
			}

			if ("GoodsField".equals(field.getFieldSysType())) {
				// ��������ֶ�δ���ã������ֶ�ϵͳ����Ϊ��Ʒ��������治��ʾ
				String propFieldName = field.getAsFieldName();
				if (propFieldName.toLowerCase().endsWith("nv")) {
					propFieldName = propFieldName.substring(0, propFieldName.length() - 2);
				}
				// ����ֶ�����Ϊ��ɫ���ƣ�����ɫ�������������
				if ("colorName".equalsIgnoreCase(propFieldName)) {
					propFieldName = "color";
				}
				GoodsPropInfoBean gpBean = (GoodsPropInfoBean) BaseEnv.propIgnoreCaseMap.get(propFieldName.toLowerCase());
				if (gpBean != null && gpBean.getIsUsed() == 2) {
					procParams.add("");
					continue;
				}
			}

			List sysTypeList = ((EnumerateBean) BaseEnv.enumerationMap.get("FieldSysType")).getEnumItem();
			if (sysTypeList != null) {
				boolean isContinue = false;
				for (Object obj : sysTypeList) {
					EnumerateItemBean item = (EnumerateItemBean) obj;
					boolean openSysType = BaseEnv.systemSet.get(item.getEnumValue()) == null ? true : Boolean.parseBoolean(BaseEnv.systemSet.get(item.getEnumValue()).getSetting());
					if (!openSysType && field.getFieldSysType().equals(item.getEnumValue())) {
						procParams.add("");
						isContinue = true;
						break;
					}
				}
				if (isContinue) {
					sql = sql.replaceAll("@conditionv2:" + field.getAsFieldName(), "''");
					defBean.setSql(sql);
					continue;
				}
			}

			byte fieldType = field.getFieldType();
			byte inputType = field.getInputType();

			/**
			 * 0 : ��ʾ��Dispaly 1 : �ֶ���AsFieldName; 2 : ֵ Value 3 : ���� inputType 4
			 * : ����������ö���� 5 ��ϵͳ����getFieldSysType 6 : ��������getPopUpName�� ��ѡ��Ҳָö����
			 * 7 ���Ƿ��ѡ 8 : �������� 9 : Ĭ��ֵ 10:classCode
			 * 11:�������汾�Ŵ���1��ѵ�����������displayfield����Ϊtrue���ֶδ洢��cond
			 * [11]�У��ڿͻ��˽�������ֶΣ������ı��� 12���洢�Ӳ�ѯ���
			 * 13���洢��������������belongTableName
			 */
			String[] cond = new String[14];
			cond[0] = field.getDisplay();
			cond[12] = field.getSubSQL();

			cond[1] = field.getAsFieldName();

			String value = null;
			
//			if (sysAlert != null && "true".equals(sysAlert.toString())) {
//				Object filedvalue = parameterMap.get(field.getAsFieldName());
//				if (filedvalue != null) {
//					value = filedvalue.toString();
//				} else {
//					value = null;
//				}
//			} else {
				value = request.getParameter(field.getAsFieldName());
	//		}
			value = GlobalsTool.encodeTextCode(value);

			if (linkType != null && (linkType.equals("@URL") || linkType.equals("@URL:")) && value != null) {
				value = new String(value.getBytes("iso-8859-1"), "UTF-8");
			}
			if (value != null) {//������������ֵ
				hasConditionValue = true;
			}

			if (value == null) { //!delParamFlag && (value != null && value.length() == 0) || value == null   --zxy ���洫�롰���ű�����ȡ��ֵ��
				String val = GlobalsTool.getUrlBillDef(request, field.getAsFieldName());
				value = val == null ? value : val;
			}
			//BillNo���ܴ��ڴ�ȡֵ����Ϊ�ڲɹ�����������鶩����ϸ���ٵ���������ʱ����BillNo�����Ŵ�ȥ��Ϊ��ⵥ��BillNo�ˣ���ʱ�ô˷�ȥ��BillNo
			if (value == null && linkFromConditionMap != null && !field.getAsFieldName().equals("BillNo")) {
				//����ǵ�һ�δ򿪵����ӱ���������Ϊ��ʱ�������ӹ����ı����ڴ�������ȡֵ
				value = (String)linkFromConditionMap.get(field.getAsFieldName());
			} //������һ������ȡĬ��ֵ����ɹ�ͳ��ѡ�ֿ���δ������Ϊ��Ĭ��ֵ���ᵼ��ȡ�������ӹ�ȥ��ͳ�� 

			String dv = field.getDefaultValue();
			String hide_value = "";
			boolean hasDefineCond = false;
			if("menu".equals(request.getParameter("src"))){
				//����ǳ����ҿ�����Ҫ����û���û���������ֵ
				if(userConditions.size() > 0){
					hasDefineCond = true;
					for(String[] ss : userConditions){
						if(ss[0].equals(cond[1])){
							if(ss[1] != null ){
								value = ss[1];
								hide_value= ss[2];
							}
							break;
						}
					}
				}
			}
			//(value == null|| value.length() == 0)
			if (value == null && dv != null && dv.length() > 0
					&& ("menu".equals(request.getParameter("src")) || selectType != null && ("normal").equals(selectType) || ("1").equals(field.getIsNull()))) {

				if (dv.indexOf("@MEM:") >= 0 || dv.indexOf("@Sess:") >= 0) {
					ArrayList sysParam = new ArrayList();
					ArrayList sessParam = new ArrayList();
					ArrayList codeParam = new ArrayList();
					ArrayList queryParam = new ArrayList();
					ConfigParse.parseSentenceGetParam(dv, new ArrayList(), sysParam, sessParam, codeParam, queryParam, null);
					HashMap sysMap = ConfigParse.getSystemParam(sysParam, loginBean.getSunCmpClassCode());
					HashMap sessMap = ConfigParse.getSessParam(loginBean.getId(), sessParam);
					Connection conn = null;
					HashMap codeMap = ConfigParse.getCodeParam(codeParam, conn);
					dv = ConfigParse.parseSentencePutParam(dv, new HashMap(), sysMap, new HashMap(), sessMap, codeMap, null, null, null);
					BSFManager evalM = BaseEnv.evalManager;

					if (fieldType == DBFieldInfoBean.FIELD_INT) {
						if (dv.length() > 0 && dv.indexOf(".") > -1) {
							dv = dv.substring(0, dv.indexOf("."));
						}
					}
				}
				value = dv;
				if ("-1".equals(dv) && "AccYear".equals(field.getAsFieldName())) {
					value = "";
				}
			}
			cond[2] = value;
			HashMap condRepMap = (HashMap) conditionSessionMap.get(reportNumber + moduleNumber);
			if (condRepMap == null) {
				condRepMap = new HashMap();
				condRepMap.put("queryType", "normal");
				conditionSessionMap.put(reportNumber + moduleNumber, condRepMap);
			}

			if(cond[2] != null && cond[2].contains("@join:")){
				cond[2] = cond[2].replaceAll("@join:", "&");
			}
			
			if (cond[2] == null) {
				if (field.getInputType() == 5 || field.getPopUpName().equals("zero")) {
					cond[2] = "";
					condRepMap.remove(field.getAsFieldName());
				} else {
					cond[2] = (String) condRepMap.get(field.getAsFieldName());
					value = cond[2];
				}
			} else {
				condRepMap.put(field.getAsFieldName(), cond[2]);
			}

			cond[2] = cond[2] == null ? "" : cond[2];
			procParams.add(cond[2]);

			//��������
			if (inputType == DBFieldInfoBean.INPUT_HIDDEN_INPUT && existListColConfig(reportNumber, field.getAsFieldName(), request)) {
				if (field.getPopUpName() != null && field.getPopUpName().length() > 0) {
					EnumerateBean enumBean = (EnumerateBean) BaseEnv.enumerationMap.get(field.getPopUpName());
					if (enumBean != null) {
						inputType = DBFieldInfoBean.INPUT_ENUMERATE;
					} else {
						inputType = DBFieldInfoBean.INPUT_MAIN_TABLE;
					}
				} else {
					inputType = DBFieldInfoBean.INPUT_NORMAL;
				}
			}
			cond[3] = "0";
			if (inputType == DBFieldInfoBean.INPUT_ENUMERATE) {
				cond[3] = "1";
			} else if (inputType == DBFieldInfoBean.INPUT_DOWNLOAD_SELECT) {
				cond[3] = "16";
			} else if (inputType == DBFieldInfoBean.INPUT_CHECKBOX) {
				cond[3] = "5";
				cond[6] = field.getPopUpName();
			} else if (fieldType == DBFieldInfoBean.FIELD_DATE_LONG || fieldType == DBFieldInfoBean.FIELD_DATE_SHORT) {
				cond[3] = "2";
			} else if (inputType == DBFieldInfoBean.INPUT_HIDDEN) {
				cond[3] = "3";
			} else if (inputType == DBFieldInfoBean.INPUT_HIDDEN_INPUT) {
				cond[3] = "3";
			} else if (inputType == DBFieldInfoBean.INPUT_MAIN_TABLE) {
				cond[3] = "4";
				cond[6] = field.getPopUpName();
			} else if (inputType == DBFieldInfoBean.INPUT_NO) {
				cond[3] = "100";
				cond[2] = "";
			}

			cond[4] = field.getPopUpName();
			if (!cond[4].equals("")) {
				PopupSelectBean bean = (PopupSelectBean) BaseEnv.popupSelectMap.get(cond[4]);
				if (bean != null && bean.getReturnFields() != null) {
					cond[13] = bean.getBelongTableName();
					for (Object o : bean.getReturnFields()) {
						PopField tmp_f = (PopField) o;
						if (tmp_f.fieldName.toLowerCase().indexOf("iscatalog") != -1) {
							cond[10] = field.getCondition();
							//HashMap condRepMap = (HashMap) conditionSessionMap.get(reportNumber + moduleNumber);
							
							String tmpStr;
							if(hasDefineCond){ //���Զ����ѯ������
								tmpStr = hide_value;
							}else{
								tmpStr = getCondition(request, "hide_" + field.getAsFieldName(), condRepMap);								
								
								if (linkType != null && (linkType.equals("@URL") || linkType.equals("@URL:")) && tmpStr != null) {
									tmpStr = new String(tmpStr.getBytes("iso-8859-1"), "UTF-8");
								}
								if ((tmpStr == null || tmpStr.length()==0) && linkFromConditionMap != null) {
									//����ǵ�һ�δ򿪵����ӱ���������Ϊ��ʱ�������ӹ����ı����ڴ�������ȡֵ
									tmpStr = (String)linkFromConditionMap.get("hide_" +field.getAsFieldName());
								}
							}
							if (tmpStr != null && condRepMap != null) {
								
								condRepMap.put("hide_" + field.getAsFieldName(), tmpStr);
							}
							if(cond[2]==null || cond[2].length() ==0){
								//�����ӦֵΪ�գ��򵯳�����ʾҲҪΪ��
								tmpStr = "";
							}

							request.setAttribute("hide_" + field.getAsFieldName(), GlobalsTool.replaceSpecLitter(tmpStr));
							if (cond[10].equals("")) {
								cond[10] = field.getFieldName().substring(0, field.getFieldName().indexOf('.')) + ".classCode";
							}
							break;
						}
					}
					if (!(request.getAttribute("reportversion") != null && (Integer) request.getAttribute("reportversion") > bean.getVersion()))
						request.setAttribute("reportversion", bean.getVersion());

					if (bean.getVersion() > 1) {// �汾�Ŵ���1��ѵ�����������displayfield����Ϊtrue���ֶδ洢��cond[11]�У��ڿͻ��˽�������ֶΣ������ı���

						// �洢��ʽΪ �ֶ���(�����԰汾);���ֶ���|�ֶ�֮���ã��ָ���ÿ���ֶ������ã��ָ�
						if (bean != null && bean.getDisplayField() != null && bean.getDisplayField().size() > 0) {
							StringBuffer sb = new StringBuffer();
							//HashMap condRepMap = (HashMap) conditionSessionMap.get(reportNumber + moduleNumber);
							int parentDcount = bean.getSaveFields().size();
							ArrayList hasValueList = new ArrayList(); //���ڴ洢��ֵ������
							for (Object o : bean.getDisplayField()) {
								PopField tmp_f = (PopField) o;
								if (!tmp_f.parentDisplay) {
									continue;
								}
								String display = tmp_f.getDisplay();
								if (display == null || display.trim().equals(""))
									display = tmp_f.getFieldName();
								if (display.indexOf('.') == -1) {
									sb.append(display + "#;#");
								} else {
									DBFieldInfoBean dbf = GlobalsTool.getFieldBean(display);
									if (dbf != null)
										display = dbf.getDisplay().get(loginBean.getDefLoc());
									else if (tmp_f.getFieldName().indexOf('.') > -1) { //�ñ�����ʽ������GoodsFullName��display��������@TableName.GoodsCode���Ի�����ԭfieldName
										dbf = GlobalsTool.getFieldBean(tmp_f.getFieldName());
										if (dbf != null)
											display = dbf.getDisplay().get(loginBean.getDefLoc());
									}

									sb.append(display + "#;#");
								}

								String inputStr = tmp_f.getAsName();
								if (inputStr == null || inputStr.equals(""))
									inputStr = tmp_f.getFieldName();
								if ("normal".equals(request.getParameter("queryChannel")) || "menu".equals(request.getParameter("src")) ) {
									if (condRepMap != null)
										condRepMap.remove(inputStr);
								}
								String retValue="";
								if(hasDefineCond){ //���Զ����ѯ������
									String[] hs = hide_value.split("#;#");
									if(parentDcount < hs.length){
										retValue = hs[parentDcount];
									}
								}else{
									retValue = getCondition2(request, inputStr, condRepMap);
								}


								if ((retValue == null || retValue.length()==0) && linkFromConditionMap != null) {
									//����ǵ�һ�δ򿪵����ӱ���������Ϊ��ʱ�������ӹ����ı����ڴ�������ȡֵ
									retValue = (String)linkFromConditionMap.get(inputStr);
								}
								
								if(retValue != null && retValue.contains("@join:")){
									retValue = retValue.replaceAll("@join:", "&");
								}
								
								//if(cond[2]==null || cond[2].length() ==0){
									//�����ӦֵΪ�գ��򵯳�����ʾҲҪΪ��	
									//retValue = "";
								//}
								
								if (retValue == null)
									retValue = "";								
								
								if (condRepMap != null)
									condRepMap.put(inputStr, retValue);
								parentDcount ++;
								sb.append(inputStr + "#;#" + GlobalsTool.replaceSpecLitter(retValue) + "#|#");
								if(!retValue.equals("")){
									hasValueList.add(tmp_f.getFieldName()+":"+retValue);
								}
							}
							if (sb.length() > 0 && sb.toString().endsWith("#|#")){
								sb.delete(sb.length() - 3, sb.length());
							}
							cond[11] = sb.toString();
							if( (cond[2]==null || cond[2].length() ==0 ) &&(cond[10]!=null && cond[10].length() >0 )  && hasValueList.size() > 0){
								//�������ֵΪ�գ����ǽ�����¼��ֵ�������зּ��ģ�˵�������ǿͻ�ֱ��¼��Ĳ�ѯ����������ͨ��ѡ������ģ���ʱҪ�����û�¼���ֵ����ѯ����Ӧ��classCode
								if(bean.getBelongTableName()==null || bean.getBelongTableName().length() ==0){
									rs.retCode= ErrorCanst.DEFAULT_FAILURE;
									rs.retVal = "������"+bean.getDesc()+"����ָ��������������";
									return rs;
								}
								Result cnvaluers = mgt.getClassCode(bean.getBelongTableName(), hasValueList);
								if(cnvaluers.retCode != ErrorCanst.DEFAULT_SUCCESS){
									rs.retCode= ErrorCanst.DEFAULT_FAILURE;
									rs.retVal = "ת������ΪclassCodeʱ����"+cnvaluers.getRetVal();
									return rs;
								}
								Collection<String[]> cnvalList = (Collection<String[]>)cnvaluers.retVal;
								if(cnvalList.size() == 0){
									//������������ֹͣ��ѯ
									noClassConditon = true;
								}else if(cnvalList.size()>500){
									rs.retCode= ErrorCanst.DEFAULT_FAILURE;
									rs.retVal = "ָ��������Χ������¼�����ȷ�Ĳ�ѯ����";
									//cond[11]="";
									return rs;
								}else{
									String tval = "";
									String hval ="";
									for(String[] ss:cnvalList){
										tval +=","+ss[0];
										hval +="#|#"+ss[0]+"#;#"+ss[1]+"";
									}
									tval = tval.substring(1);
									hval = hval.substring(3);
									cond[2]= tval;
									procParams.remove(procParams.size()-1);
									procParams.add(cond[2]);
									condRepMap.put(field.getAsFieldName(), cond[2]);
									BaseEnv.log.debug("ReportData �Զ�ȡ����classCode ��� :"+tval);
									
									condRepMap.put("hide_" + field.getAsFieldName(), hval);
									request.setAttribute("hide_" + field.getAsFieldName(), hval);
								}
							}
						}
					}
				}
			}
			cond[5] = field.getFieldSysType();
			cond[7] = field.getIsNull();
			if (fieldType == DBFieldInfoBean.FIELD_ANY) {
				cond[8] = "any";
			} else if (fieldType == DBFieldInfoBean.FIELD_DOUBLE) {
				cond[8] = "double";
			} else if (fieldType == DBFieldInfoBean.FIELD_INT) {
				cond[8] = "int";
			} else if (fieldType == DBFieldInfoBean.FIELD_DATE_LONG || fieldType == DBFieldInfoBean.FIELD_DATE_SHORT) {
				cond[8] = "date";
			} else if (fieldType == DBFieldInfoBean.FIELD_DATE_TIME) {
				cond[8] = "time";
			} else {
				cond[8] = "any";
			}
			cond[9] = field.getDefaultValue();
			conditions.add(cond);

			/* ����@conditionFun����ʶʱ�� ����ֵ���н��� */
			if (sql.contains("@conditionFun:" + field.getAsFieldName())) {
				sql = sql.replaceAll("@conditionFun:" + field.getAsFieldName(), "'" + cond[2] + "'");
			} else {
				PopupSelectBean popSelect = null;
				if (field.getInputType() == 2) {
					popSelect = (PopupSelectBean) BaseEnv.popupSelectMap.get(field.getPopUpName());
				}

				// ������������ǰ�������@condsent_:[]�����û�û����������ֵ����ô��@condsent_:[...]ȥ��,����û�������ֵ����@condsent_:[]ȥ��
				while (sql.indexOf("@condsent_" + field.getAsFieldName()) > 0) {
					int indexs = sql.indexOf("@condsent_" + field.getAsFieldName() + ":[");
					int indexe = sql.indexOf("]", indexs);
					if (value != null && value.trim().length() > 0) {
						sql = sql.substring(0, indexs) + sql.substring(indexs + ("@condsent_" + field.getAsFieldName() + ":[").length(), indexe) + sql.substring(indexe + 1);
					} else {
						sql = sql.substring(0, indexs) + sql.substring(indexe + 1);
					}
				}

				if (popSelect != null && popSelect.getVersion() > 1) {
					// String pat =
					// "(\\)| and | or )(.*?(like|=|\\<|\\>).*?\\@condition:"
					// + field.getAsFieldName() + ".*?)( and | or |\\()";
					// Pattern p = Pattern.compile(pat);
					// Matcher m = p.matcher(sql);
					sql = sql.replaceAll("@conditionv2:" + field.getAsFieldName(), "'" + cond[2] + "'");
					String pat = "@condition:" + field.getAsFieldName();
					int pos = 0;
					PopupSelectBean bean = (PopupSelectBean) BaseEnv.popupSelectMap.get(cond[4]);
					while (bean.getVersion() > 1 && (pos = sql.indexOf(pat, pos)) != -1) {
						int left = sql.lastIndexOf(")", pos) + 1;
						left = Math.max(left, sql.lastIndexOf(" and ", pos) + 5);
						left = Math.max(left, sql.lastIndexOf(" or ", pos) + 4);
						left = Math.max(left, sql.lastIndexOf("where ", pos) + 6);

						int right = pos + pat.length();

						int op = sql.lastIndexOf("=", pos);
						op = Math.max(op, sql.lastIndexOf(" like ", pos));
						op = Math.max(op, sql.lastIndexOf(" in ", pos));

						//HashMap condRepMap = (HashMap) conditionSessionMap.get(reportNumber + moduleNumber);

						String tmpStr = (String)condRepMap.get("hide_" + field.getAsFieldName());

						String tmpField = sql.substring(left, op).trim();
						while (tmpField.startsWith("("))
							tmpField = tmpField.substring(1);
						String tmpValue = replaceClassCode(field, tmpStr, tmpField);

						if (tmpValue != null) {
							String tmpC = sql.substring(0, left);
							tmpC += " (" + tmpValue + ") ";
							pos = tmpC.length();
							tmpC += sql.substring(right);
							sql = tmpC;
						} else if (getCondition(request, field.getAsFieldName(), condRepMap) == null || getCondition(request, field.getAsFieldName(), condRepMap).equals("")) {
							String tmpC = sql.substring(0, left);
							pos = tmpC.length();
							tmpC += sql.substring(right);
							sql = tmpC;
						} else {
							pos = right;
						}

					}
				}
				if (popSelect != null && "checkBox".equals(popSelect.getType()) && value != null && value.contains(",") && sql.contains("@condition:" + field.getAsFieldName())) {
					sql = this.parseMultiVal("@condition:" + field.getAsFieldName(), sql, null, value.replaceAll("'", "''"));
				} else {
					String cnd = "'" + (cond[2]==null?"":cond[2].replaceAll("'", "''")) + "'";
					while(sql.indexOf("@condition:" + field.getAsFieldName()) > -1){
						int posc = sql.indexOf("@condition:" + field.getAsFieldName());
						sql = sql.substring(0,posc)+cnd + sql.substring(posc+("@condition:" + field.getAsFieldName()).length());
					}
					sql = sql.replaceAll("@condition:" + field.getAsFieldName(), cnd);
				}
			}
			for (int j = 0; j < defBean.getQueryFields().size(); j++) {
				ReportField rf = (ReportField) defBean.getQueryFields().get(j);
				if (rf.getFieldName().contains("@condition:" + field.getAsFieldName())) {
					String fieldStr = "";
					/* ����@conditionFun����ʶʱ�� ����ֵ���н��� */
					if (sql.contains("@conditionFun:" + field.getAsFieldName())) {
						sql = sql.replaceAll("@conditionFun:" + field.getAsFieldName(), "'" + cond[2] + "'");
					} else {
						PopupSelectBean popSelect = null;
						if (field.getInputType() == 2) {
							popSelect = (PopupSelectBean) BaseEnv.popupSelectMap.get(field.getPopUpName());
						}
						if (popSelect != null && "checkBox".equals(popSelect.getType()) && cond[2] != null && cond[2].contains(",")
								&& rf.getFieldName().contains("@condition:" + field.getAsFieldName())) {
							fieldStr = this.parseMultiVal("@condition:" + field.getAsFieldName(), rf.getFieldName(), null, cond[2]);
						} else {
							fieldStr = rf.getFieldName().replaceAll("@condition:" + field.getAsFieldName(), "'" + cond[2] + "'");
						}
					}
					rf.setFieldName(fieldStr);
				}
				if (rf.getLinkAdd() != null && rf.getLinkAdd().contains("@condition:" + field.getAsFieldName())) {
					String linkAdd = rf.getLinkAdd().replaceAll("@condition:" + field.getAsFieldName(), cond[2]);
					rf.setLinkAdd(linkAdd);
				}
				if (rf.getCondition() != null && rf.getCondition().contains("@condition:" + field.getAsFieldName())) {
					String condition = "";
					/* ����@conditionFun����ʶʱ�� ����ֵ���н��� */
					if (sql.contains("@conditionFun:" + field.getAsFieldName())) {
						sql = sql.replaceAll("@conditionFun:" + field.getAsFieldName(), "'" + cond[2] + "'");
					} else {
						PopupSelectBean popSelect = null;
						if (field.getInputType() == 2) {
							popSelect = (PopupSelectBean) BaseEnv.popupSelectMap.get(field.getPopUpName());
						}
						if (popSelect != null && "checkBox".equals(popSelect.getType()) && cond[2] != null && cond[2].contains(",")
								&& rf.getCondition().contains("@condition:" + field.getAsFieldName())) {
							condition = this.parseMultiVal("@condition:" + field.getAsFieldName(), rf.getCondition(), null, cond[2]);
						} else {
							condition = rf.getCondition().replaceAll("@condition:" + field.getAsFieldName(), "'" + cond[2] + "'");
						}
					}
					rf.setCondition(condition);
				}
			}
			defBean.setSql(sql);
		}

		final Hashtable<String, ArrayList<ColConfigBean>> userConfigBean = (Hashtable<String, ArrayList<ColConfigBean>>) request.getSession().getServletContext().getAttribute("userSettingColConfig");
		ArrayList<ColConfigBean> colList = userConfigBean.get(reportNumber + "list");

		String configType = request.getParameter("configType");
		if ("listConfig".equals(configType)) {
			if (colList != null) {
				for (int m = 0; m < colList.size(); m++) {
					ColConfigBean colBean = colList.get(m);
					for (int i = 0; i < disField.size(); i++) {
						ReportField field = (ReportField) disField.get(i);
						if (colBean.getColName().equals(field.getAsFieldName())) {
							configField.add(field);
							break;
						}
					}
				}
			}
			request.setAttribute("configField", configField);
			rs.setRetCode(ErrorCanst.RETURN_COL_CONFIG_SETTING);
			return rs;
		}
		HashMap queryMaps = new HashMap();
		String sql = defBean.getSql();
		ArrayList paramList = new ArrayList();
		int queryCount = 0;
		String temp = "";
		if (defBean.getClassCode() != null && reportSetBean.getReportType().equals(PROCLIST))// ����Ǵ洢���̱��������Ƿּ��������ڲ�����ǰ�����Parentcode,�����ɴ洢���̱�����ӦSQL����ʱ��Ҳ������Ӧ����
		{
			paramList.add(parentCode);
		}
		//���δ�������Զ��Ӹ�parentCode����		
		if (reportSetBean.isEndClassNumber() && reportSetBean.getReportType().equals(PROCLIST))// ����Ǵ洢���̱���������δ���������ڲ�����ǰ�����Parentcode,�����ɴ洢���̱�����ӦSQL����ʱ��Ҳ������Ӧ����
		{
			paramList.add(parentCode);
		}

		for (int i = 0; i < condField.size(); i++) {
			ReportField field = (ReportField) condField.get(i);

			//�����������Ĵ����У�����ֵ���Ѿ��������map�У�����ֱ��ȡֵ����	
			String value = "";
			HashMap condRepMap = (HashMap) conditionSessionMap.get(reportNumber + moduleNumber);
			if (condRepMap != null) {
				value = (String) condRepMap.get(field.getAsFieldName());
				value = value == null ? "" : value;
			}

			// ��ѯ�ֶεĲ�ѯ�����Ƿ�ȥ�����ж�.����¼����߷�����һ��ʱ������ǲ�ѯ����Ҳ����ʾ�ֶξͰ�ֵȥ��
			boolean delParamFlag = ("true".equals(request.getParameter("next")) || "true".equals(request.getParameter("backParent"))) && field.getDisplayFlag().equals("1")
					&& field.getWidth().length() > 0 && !field.getWidth().equals("0");

			if (field.getAsFieldName().equals("workFlowNodeName") && ((value == null && "all".equals(field.getDefaultValue())) || "all".equals(value))) {
				value = "";
				field.setDefaultValue("");
			}else if (TABLELIST.equals(reportSetBean.getReportType()) && field.getAsFieldName().equals("workFlowNodeName") && value != null && "-1".equals(value) ) {
				value = "finish";
			}else if (TABLELIST.equals(reportSetBean.getReportType()) && field.getAsFieldName().equals("workFlowNodeName") && value != null && "notApprove".equals(value) ) {
				value = "notApprove";
			}

			/*
			int indexs = sql.indexOf(field.getConditionJoin() + " (" + field.getFieldName().replaceAll("\n", ""));
			//int indexe = sql.indexOf(field.getCondition() + ")") + field.getCondition().length() + 1; //��Ϊcondition��)֮����ܳ��ֿո񣬲�������ķ����ֶ�
			int indexe = -1;
			if(indexs > -1 ){
				indexe = sql.indexOf(field.getCondition(),indexs);
				indexe = sql.indexOf(")",indexe)+1;
			} */
			//������ܸģ�������ö�����
			int indexs = sql.indexOf(field.getConditionJoin() + " (" + field.getFieldName().replaceAll("\n", ""));
			int indexe = sql.indexOf(field.getCondition() + ")") + field.getCondition().length() + 1;

			// ����˲�ѯ��������䲻�� ���ֶ��� like�� ���� ���ֶ��� =�� �Ľṹ��������Ϊ�Ӳ�ѯ����������������ȡֵ
			if (indexs < 0 || indexe < 0) {
				indexs = sql.indexOf(field.getConditionJoin() + " " + field.getCondition());
				indexe = sql.indexOf(field.getCondition()) + field.getCondition().length();
			}
			if (indexs < 0) {
				System.out.println("Error report field -------------" + field.getFieldName() + " ����Ǹ����ֶΣ�����ü�����");
			}

			String t = "";
			if ((value != null && value.length() > 0) || (value != null && reportSetBean.getReportType().equals(PROCLIST))) {
				if (field.getCondition().contains("like")) {
					value = value.replace("%", "!%");
					value = value.replace("[", "![");
					value = value.replace("]", "!]");
					value = value.replace(" ", "%");
				}
				if (value.contains(",") && !",".equals(value)) { //�������ֵ�Զ��ŷֿ�
					if (field.getCondition() != null && field.getCondition().contains("@ValueofFun:getClassCode")) {
						int p = sql.indexOf("@ValueofFun:getClassCode");
						if (p < 0)
							continue;
						String getCcFun = sql.substring(sql.indexOf("(", p) + 2, sql.indexOf(")", p));
						String[] ccfs = getCcFun.split(",");
						String condition = mgt.getClassCode(ccfs[0], ccfs[1], value);
						indexe = p + condition.length() + sql.substring(sql.indexOf(")", p)).indexOf(")") + 2;
						sql = sql.substring(0, p) + condition + sql.substring(sql.indexOf(")", p) + 2);
						t = sql.substring(0, indexe);
						// System.out.println(rf.getCondition());
					} else {
						t = sql.substring(0, indexs);
						// �����������ش���iscatalog���ֵ�SQL���

						String tmpValue = null;
						if (condRepMap.get("hide_" + field.getAsFieldName()) != null) {
							tmpValue = replaceClassCode(field, (String)condRepMap.get("hide_" + field.getAsFieldName()));

							if ((sql.indexOf(field.getCondition())) != -1) {
								if (field.getCondition().indexOf(" or 1=1") == -1 && tmpValue != null)
									t += " " + field.getConditionJoin() + " (" + tmpValue + ") ";
								else
									t += " " + field.getConditionJoin() + " ( 1=1 ) ";
							}
						}

						// ======================================������������iscatalog�ֶ�
						if (tmpValue == null && !reportSetBean.getReportType().equals(PROCLIST))
							t += this.parseMultiVal("?", sql.substring(indexs, indexe), paramList, value);

						if (reportSetBean.getReportType().equals(PROCLIST))// ����Ǵ洢���̱�����Ѳ�������paramList��
						{
							if (condRepMap.get("hide_" + field.getAsFieldName()) != null) {
								paramList.add(replaceClassCode(field.getPopUpName(), (String)condRepMap.get("hide_" + field.getAsFieldName())));
							} else
								paramList.add(value);
						}
					}
				} else {
					// �����������ش���iscatalog���ֵ�SQL���
					String tmpValue = replaceClassCode(field, (String)condRepMap.get("hide_" + field.getAsFieldName()));

					if ((sql.indexOf(field.getCondition())) != -1) {
						if (field.getCondition().indexOf(" or 1=1") == -1 && tmpValue != null)
							t += " " + field.getConditionJoin() + " (" + tmpValue + ") ";
						else
							t += " " + field.getConditionJoin() + " ( 1=1 ) ";
					}

					// ======================================������������iscatalog�ֶ�

					// ���������@ValueofFun:getClassCode�����Ľ���
					if (field.getCondition() != null && field.getCondition().contains("@ValueofFun:getClassCode")) {
						int p = sql.indexOf("@ValueofFun:getClassCode");
						if (p < 0)
							continue;
						String getCcFun = sql.substring(sql.indexOf("(", p) + 1, sql.indexOf(")", p));
						String[] ccfs = getCcFun.split(",");
						String condition = mgt.getClassCode(ccfs[0], ccfs[1], ccfs[2]);
						indexe = p + condition.length() + sql.substring(sql.indexOf(")", p)).indexOf(")") + 1;
						sql = sql.substring(0, p) + condition + sql.substring(sql.indexOf(")", p) + 1);
						t = sql.substring(0, indexe);
						// System.out.println(rf.getCondition());
					} else {
						if (reportSetBean.getReportType().equals(PROCLIST))// ����Ǵ洢���̱�����Ѳ�������paramList��
						{
							if (condRepMap.get("hide_" + field.getAsFieldName()) != null && 
									((String)condRepMap.get("hide_" + field.getAsFieldName())).length() > 0) {
								paramList.add(replaceClassCode(field.getPopUpName(), (String)condRepMap.get("hide_" + field.getAsFieldName())));
							} else
								paramList.add(value);
						} else if (indexe != -1 && tmpValue == null) {
							t = sql.substring(0, indexe);
							paramList.add(value);
						} else if (indexs != -1) {
							t = sql.substring(0, indexs) + t;
						}
					}
					if (field.getInputType() != DBFieldInfoBean.INPUT_HIDDEN && field.getInputType() != DBFieldInfoBean.INPUT_HIDDEN_INPUT) {
						queryCount++;
					}
				}
				queryMaps.put(field.getAsFieldName(), value);
			} else {
				if (field.getDefaultValue() != null && field.getDefaultValue().length() > 0 && field.getInputType() != DBFieldInfoBean.INPUT_NO
						&& (("menu".equals(request.getParameter("src")) || delParamFlag) && selectType != null && selectType.equals("normal"))) {
					String se = sql.substring(indexs, indexe);
					if (field.getFieldType() != 0 && field.getFieldType() != 1) {
						se = se.replace("?", "'" + field.getDefaultValue() + "'");
					} else {
						se = se.replace("?", field.getDefaultValue());
					}
					t = sql.substring(0, indexs) + se;
				} else {
					if (indexs != -1)
						t = sql.substring(0, indexs);
				}
			}
			DBFieldInfoBean fieldBean = null;
			if (field.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE && field.getFieldName().indexOf(".") > 0) {
				fieldBean = GlobalsTool.getFieldBean(field.getFieldName().substring(0, field.getFieldName().indexOf(".")), field.getFieldName().substring(field.getFieldName().indexOf(".") + 1));
			}

			if (value != null && value.length() > 0 && (field.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || (fieldBean != null && fieldBean.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE))) {
				if (t.indexOf("where 1=1") > 0) {
					t = t.substring(0, t.indexOf("where 1=1")) + t.substring(t.indexOf("where 1=1")).replace(field.getFieldName(), "a" + i + "." + locale);
				} else {
					t = t.replace(field.getFieldName(), "a" + i + "." + locale);
				}

			}
			if (TABLELIST.equals(reportSetBean.getReportType()) && field.getAsFieldName().equals("workFlowNodeName") && 
					value != null &&  !"".equals(value) &&  !"draft".equals(value)  &&  !"finish".equals(value)  &&  !"notApprove".equals(value)) {
				t=t.substring(0,t.indexOf(field.getFieldName()))+ field.getFieldName()+" = 'notApprove' and " + (field.getFieldName().replaceFirst("workFlowNodeName", "workFlowNode")) +t.substring(t.indexOf(field.getFieldName())+field.getFieldName().length());
			}
			temp += t;
			if (value != null && value.length() > 0 && (field.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || (fieldBean != null && fieldBean.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE))) {
				temp = temp.substring(0, temp.indexOf("where 1=1")) + " left join tblLanguage a" + i + " on " + field.getFieldName() + "=a" + i + ".id " + temp.substring(temp.indexOf("where 1=1"));
			}
			sql = sql.substring(indexe);
			
			//�����ѯ�ǲ��ǿ�ʼ���ͽ������
			
		}

		String sunCmp_sql = "";

		String tableName = reportNumber;

		String sunClassCode = loginBean.getSunCmpClassCode();
		if ("".equals(sunClassCode) || sunClassCode == null || !TABLELIST.equals(reportSetBean.getReportType())) {
			sunCmp_sql = "";
		} else {
			Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
			boolean isSunCmpShare = false;
			if (tableInfo == null || tableInfo.getIsSunCmpShare() == 1) {
				isSunCmpShare = true;
			}
			if (!isSunCmpShare) {
				//				sunCmp_sql = " and " + tableName + ".SCompanyID like '" + sunClassCode + "%'  ";
				sunCmp_sql = "";
			} else {
				sunCmp_sql = "";
			}
		}
		sql = temp + sql;
		sql = sql.replace("'%')", "'%' escape '!')");

		if (!DynDBManager.hasAdminRole(loginBean)) {
			int count = sql.indexOf("1=1");
			if (count != -1) {
				if (tableName.equals("tblSunCompanys")) {
				} else {
					count += 3;
					String sql_index = sql.substring(0, count);
					String sql_last = sql.substring(count);
					sql = sql_index + " " + sunCmp_sql + " " + sql_last;
				}
			}
		}

		ArrayList sysParam = new ArrayList();
		ArrayList tabParam = new ArrayList();
		ArrayList sessParam = new ArrayList();
		ArrayList codeParam = new ArrayList();
		ArrayList queryParam = new ArrayList();
		HashMap tabMap = new HashMap();

		ConfigParse.parseSentenceGetParam(sql, tabParam, sysParam, sessParam, codeParam, queryParam, null);

		HashMap sysMap = ConfigParse.getSystemParam(sysParam, loginBean.getSunCmpClassCode());
		HashMap sessMap = ConfigParse.getSessParam(loginBean.getId(), sessParam);
		Connection conn = null;
		HashMap codeMap = ConfigParse.getCodeParam(codeParam, conn);
		sql = ConfigParse.parseSentencePutParam(sql, null, sysMap, null, sessMap, codeMap, null, null, null);

		for (int i = 0; i < defBean.getQueryFields().size(); i++) {
			ReportField rf = (ReportField) defBean.getQueryFields().get(i);
			rf.setFieldName(ConfigParse.parseSentencePutParam(rf.getFieldName(), null, sysMap, null, sessMap, codeMap, null, null, null));
		}
		// ���������@ValueofFun:getClassCode�����Ľ���
		while (sql.indexOf("@ValueofFun:getClassCode") > 0) {
			int p = sql.indexOf("@ValueofFun:getClassCode");
			if (p < 0)
				continue;
			String getCcFun = sql.substring(sql.indexOf("(", p) + 1, sql.indexOf(")", p));
			String[] ccfs = getCcFun.split(",");
			String condition = mgt.getClassCode(ccfs[0], ccfs[1], ccfs[2]);
			sql = sql.substring(0, p) + condition + sql.substring(sql.indexOf(")", p) + 1);
		}
		String f_brother = request.getParameter("f_brother");

		String hasFrame = request.getParameter("hasFrame");// ��ȡ�Ƿ���frame���
		String brotherEnter = request.getParameter("brotherEnter");// true��ʾ��CRM��ҳ����鿴�ֵܱ�����ERP��ǰ�����ϸ����ֵܱ����
		request.setAttribute("brotherEnter", brotherEnter);
		// ���hasFrame = "true",f_brother����Ϊ��ֵ.������CRM�ֵܱ�鿴�б�
		if (hasFrame == null || "".equals(hasFrame)) {
			if (brotherEnter == null || "".equals(brotherEnter)) {
				f_brother = "";
			}
		}

		if (TABLELIST.equals(reportSetBean.getReportType()) && (f_brother == null || f_brother.trim().length() == 0)) {
			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, tableName);
			if (tableInfo != null && tableInfo.getDraftFlag() == 1) {
				if (draftQuery != null && draftQuery.equalsIgnoreCase("draftPop")) {
					if (sql.indexOf("where 1=1") > -1) {
						sql = sql.replace("where 1=1", "where 1=1 and " + tableName + ".workFlowNodeName='draft'");
					}
				} else {
					/* Ĭ�ϲ��Ѵ�ӡ��ѯ���� */
					if (sql.indexOf("where 1=1") > -1) {
						sql = sql.replace("where 1=1", "where 1=1 and isNull(" + tableName + ".workFlowNodeName,'')!='print'");
					}
				}

			}

			DBTableInfoBean tableInfoBean = (DBTableInfoBean) map.get(reportNumber);
		}

		String src = request.getParameter("src");
		int isUseFlow = 0;
		if (workFlowTemp != null) {
			isUseFlow = workFlowTemp.getTemplateStatus();
		}
		if (f_brother == null || f_brother.trim().length() == 0) {
			if (defBean.getClassCode() == null
					&& ("endClass".equals(selectType) || (!"menu".equals(src) && queryCount > 0) || (!"menu".equals(src) && "".equals(selectType)) || (defBean.getClassCode() == null && !TABLELIST
							.equals(reportSetBean.getReportType())))) {
				// Ȩ��
				sql = DynDBManager.scopeRightHandler(reportNumber, reportSetBean.getReportType(), detTable_list, loginBean.getId(), scopeRight, sql, "endClass", "");
			} else {
				if (sql.contains(reportSetBean.getReportNumber() + ".classCode like ") && parentCode.length() == 0) {
					// ����Ƿּ������������в�ѯĳ��������������ݣ���ô��û�в�ѯ����������£�Ĭ����ʾ��һ��
					int index = sql.indexOf(reportSetBean.getReportNumber() + ".classCode like ");
					int index2 = sql.indexOf("%", index);
					sql = sql.substring(0, index2) + sql.substring(index2 + 1);
				}
				// Ȩ��
				String classF = defBean.getClassCode() == null ? "" : defBean.getClassCode().getFieldName();
				if (TABLELIST.equals(reportSetBean.getReportType())) {
					DBTableInfoBean tableInfoBean = (DBTableInfoBean) map.get(reportNumber);
					if (tableInfoBean.getClassFlag() == 1) {
						classF = reportNumber + ".classCode";
					}
				}
				sql = DynDBManager.scopeRightHandler(reportNumber, reportSetBean.getReportType(), detTable_list, loginBean.getId(), scopeRight, sql, parentCode, classF); // ����ΧȨ��
			}
		} else {
			if (sql.indexOf("and (exists") > -1) {
				String scopeSql = "";
				if (sql.contains(".workFlowNodeName like")) {
					scopeSql = sql.substring(sql.indexOf("and (exists"), sql.lastIndexOf(")   and (") + 1);
				} else {
					scopeSql = sql.substring(sql.indexOf("and (exists"), sql.lastIndexOf(")") + 1);
				}
				sql = sql.replace(scopeSql, "");
			}
		}
		if (!reportType.equals("scopeClassPop") && !reportType.equals("popData")) {
			reportType = reportSetBean.getReportType();
		}

		/* �Ƿ�����ֵܱ� */
		String parentTableName = request.getParameter("parentTableName");
		String isTab = request.getParameter("isTab");
		if (f_brother != null && !"".equals(f_brother)) {
			HashMap winIndexMap = (HashMap) request.getSession().getAttribute(BaseEnv.WIN_MAP);
			String winCurIndex = request.getParameter("winCurIndex");
			String crmPopup = (String) request.getAttribute("popup");
			if (crmPopup == null) {
				crmPopup = request.getParameter("popup");
			}
			if (sql.indexOf("where 1=1") > -1 && (parentTableName != null || "crmPopup".equals(crmPopup))) {
				/* �ֵܱ�����ʾ����f_brother */
				sql = sql.replace("where 1=1", "where 1=1 and " + tableName + ".f_brother='" + f_brother + "'");
				/* �������f_brother��CRMClientInfo.Status!=1ȥ�� */
				if ("CRMClientInfo".equals(parentTableName)) {
					sql = sql.replace("and CRMClientInfo.Status!=1", "");
				}
			}
		}
		request.setAttribute("f_brother", f_brother);
		String isMenu = request.getParameter("src");
		request.setAttribute("formMenu", isMenu);
		String showCondition = request.getParameter("showCondition");
		/* �߼���ѯ֮�� �ٵ�������ѯ Ĭ�ϰѲ�ѯ�������� ��ʾ���� */
		if ("true".equals(showCondition)) {
			request.setAttribute("showQuery", "1");
		} else {
			request.setAttribute("showQuery", reportSetBean.getShowCondition());
		}
		Result rs2 = new Result();
		String saveSql = "";
		if (!noClassConditon && ((reportSetBean.getDefaultNoshow() == 0 && !"true".equals(showCondition) && 0 == reportSetBean.getShowCondition()) || hasConditionValue || !"menu".equals(isMenu)
				|| (sysAlert != null && "true".equals(sysAlert.toString())) )) {
			String defineOrderBy = request.getParameter("defineOrderBy");//������Զ�������
			request.setAttribute("defineOrderBy", defineOrderBy);
			if(defineOrderBy != null && defineOrderBy.length() > 0){
				String[] defineOrders = defineOrderBy.split(",");
				defineOrderBy = "";
				for(String defineOrder : defineOrders){
					defineOrder=defineOrder.trim();
					String dname = defineOrder.substring(0,defineOrder.indexOf(" "));
					String dasc = defineOrder.substring(defineOrder.indexOf(" "));
					for (int i = 0; i < disField.size(); i++) {
						ReportField field = (ReportField) disField.get(i);
						String fieldName = field.getFieldName();
						if(field.getAsFieldName().equals(dname)){
							defineOrderBy += field.getFieldName() + " "+ dasc+",";
						}
					}
				}
				if(defineOrderBy.length() > 0){
					defineOrderBy = defineOrderBy.substring(0,defineOrderBy.length() -1);
				}
				if (sql.indexOf("order by") > -1) {
					sql = sql.substring(0, sql.indexOf("order by")) + " order by "+defineOrderBy;
				} else {
					sql = sql + " order by "+defineOrderBy;
				}
			}
			rs2 = this.buildSQL(reportSetBean, defBean, reportType, sql, request, parentCode, pageNo, pageSize, paramList, procParams, hasConditionValue);

			if (rs2.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
				return rs2;
			}
			HashMap sqls = (HashMap) rs2.getRetVal();
			sql = sqls.get("pageSql").toString();
			saveSql = sqls.get("dataSql").toString();

			String winCurIndex = request.getParameter("winCurIndex") == null ? "" : request.getParameter("winCurIndex");
			OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(tableName);
			String host = request.getHeader("Host").contains(":") ? request.getHeader("Host") : request.getHeader("Host") + ":80";
			String export = request.getParameter("export");
			/*
			if ("all".equals(export)) {
	        	pageNo=0;
	        	pageSize=0;
	        } */
			String exportType = (String) request.getAttribute("exportType");
			if ("mobile".equals(exportType)) {
				export = exportType;
			}
			int crossPageSize = request.getAttribute("CrossPageSize") == null ? pageSize : Integer.parseInt(request.getAttribute("CrossPageSize").toString()); // ���汨��ĵ�ǰҳ����

			if ("all".equals(export)) {
				String accName = GlobalsTool.getSysSetting("DefBackPath");
				File file = new File("../../AIOBillExport");
				if (!file.exists()) {
					file.mkdirs();
				}
				String fileName = file.getCanonicalPath() + "\\" + reportSetBean.getReportName() + "_" + loginBean.getEmpFullName() + ".zip";
				/* ������ڣ���ɾ��ѹ���ļ� */
				File zipFile = new File(fileName);
				if (zipFile.exists()) {
					zipFile.delete();
				}
				String filePath = file.getCanonicalPath();
				try {
					boolean exportSucess = true;
					int rowCount = 20000; 
					if (defBean.isCross()) {
						rowCount = 40000;
					}
					int num = 1;
					int startRow = 1;
					int endRow = rowCount+1;
					while (exportSucess) {
						String esql = sql;
						if (sql.indexOf("a  where row_id between") != -1) {
							esql = sql.substring(0, sql.indexOf("a  where row_id between") + 23) + " " + startRow + " and " + endRow + " ";
						}
						BaseEnv.log.debug("==============������" + startRow + "��" + endRow + "�����ݣ�" + esql);
						// System.out.println("sql:"+sql);
						// ���ڵ���ʱ���ܻ�ı�defBean.queryFields��ֵ���������������ݣ������Ҫ��ԭ������Ĳ�ѯ���ݲſ�����ȷ
						ArrayList oldQueryList = (ArrayList) defBean.getQueryFields().clone();
						ArrayList oldDisFields = (ArrayList) defBean.getDisFields().clone();
						ArrayList oldFieldInfo = (ArrayList) defBean.getFieldInfo().clone();
						int returnRow = exportAllData(defBean, reportSetBean, reportType, reportNumber, esql, paramList, locale, scopeRight, loginBean.getId(), queryMaps, colList, winCurIndex,
								conditions, workFlow, loginBean.getSunCmpClassCode(), rowCount,num,  host, filePath, num, statType, loginBean, request);
						defBean.setQueryFields(oldQueryList);
						defBean.setDisFields(oldDisFields);
						defBean.setFieldInfo(oldFieldInfo);

						if (returnRow > rowCount) {
							startRow += rowCount;
							endRow += rowCount;
						} else {
							exportSucess = false;
						}
						num++;
					}
					if((num -1) == 1){ //�������ǵ����ļ���ֱ����ʾ�ļ���ѹ��
						request.setAttribute("ExportFileName", reportSetBean.getReportName()+"-"+loginBean.getEmpFullName() + "-1.xls");
						rs.setRetVal(GlobalsTool.getMessage(request, "excel.export.success") + "," + "<a href='/ReadFile?tempFile=export&fileName="
								+ reportSetBean.getReportName()+"-"+loginBean.getEmpFullName() + "-1.xls" + "'>" + GlobalsTool.getMessage(request, "com.export.success.download")
								+ "</a><br/>" + GlobalsTool.getMessage(request, "com.export.success.over") + "<br/>" + ((filePath +"\\"+reportSetBean.getReportName()+"-"+loginBean.getEmpFullName() + "-1.xls").replaceAll("\\\\", "\\\\\\\\")));
					}else{
						/* ѹ��Excel�ļ� */
						FileOutputStream fileOutputStream = new FileOutputStream(filePath + "//" + reportSetBean.getReportName() + "_" + loginBean.getEmpFullName() + ".zip");
						CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
						ZipOutputStream out = new ZipOutputStream(fileOutputStream);
						File pathSrc = new File(filePath);
						for(int kk = 1 ;kk<=num-1;kk++){
							File comFile = new File(filePath,reportSetBean.getReportName()+"-"+loginBean.getEmpFullName() + "-"+kk +".xls");
							compressFile(comFile, out);
							comFile.delete();
						}
						
						out.close();
						fileOutputStream.close();
						request.setAttribute("ExportFileName", reportSetBean.getReportName() + "_" + loginBean.getEmpFullName() + ".zip");						
						rs.setRetVal(GlobalsTool.getMessage(request, "excel.export.success") + "," + "<a href='/ReadFile?tempFile=export&fileName="
								+ GlobalsTool.encode(reportSetBean.getReportName() + "_" + loginBean.getEmpFullName() + ".zip") + "'>" + GlobalsTool.getMessage(request, "com.export.success.download")
								+ "</a><br/>" + GlobalsTool.getMessage(request, "com.export.success.over") + "<br/>" + (fileName.replaceAll("\\\\", "\\\\\\\\")));
					}
					new DynDBManager().addLog(15, loginBean.getId(), loginBean.getEmpFullName(), loginBean.getSunCmpClassCode(), "����"+reportSetBean.getReportName(), reportSetBean.getReportNumber(), reportSetBean.getReportName(),"");
				} catch (Exception e) {
					e.printStackTrace();
					rs.setRetVal("����ʧ��.");
				}
				if (winCurIndex == null || winCurIndex.length() == 0) {
					request.setAttribute("noback", true);
				}
				rs.setRetCode(ErrorCanst.EXCEL_REPORT_EXPORT);
				// zxy
				// Ϊ�˽������ʱ�������ص����⣨��ѡ���ѯ�����������ɹ���������أ�������ʧ��,���ﲻֱ�ӷ��أ����Ǽ���ִ��������ѯ��������ˢ���ڼ��ExportMsg�����Ϊ�գ��������ҳ
				request.setAttribute("ExportMsg", rs.getRetVal().toString());
				// return rs;
			} else if ("mobile".equals(export)) {
				String layoutType = (String) request.getAttribute("layoutType");
				return mobileDisplay(defBean, reportSetBean, reportType, reportNumber, sql, paramList, locale, scopeRight, loginBean.getId(), queryMaps, colList, winCurIndex, conditions, workFlow,
						loginBean.getSunCmpClassCode(), pageSize, pageNo, host, statType, layoutType, loginBean, request);
			}

			rs = mgt.getDisplayData(defBean, reportSetBean, reportType, reportNumber, sql, paramList, locale, scopeRight, loginBean.getId(), queryMaps, colList, winCurIndex, conditions, workFlow,
					loginBean.getSunCmpClassCode(), crossPageSize, pageNo, host, "page", statType, loginBean, request);

			if (reportSetBean.getShowTotalPage() != 0) {
				rs.setTotalPage(rs2.getTotalPage());
			}
			rs.setRealTotal(rs2.getRealTotal());
		}

		/**
		 * Ԥ������
		 */
		if (sysAlert != null && "true".equals(sysAlert.toString())) {
			HashMap sqlsmap = new HashMap();
			sqlsmap.put("pageSql", sql);
			sqlsmap.put("paramList", paramList);
			rs2.setRetVal(sqlsmap);
			return rs2;
		}

		String rowRemark = "";
		// ��ѯ����ʱ����ǽ��汨����������,�����ڲ�ѯ�����ݺ�������ҳ��Ҫ��ʾ����
		// zxy
		// ��defineReportBean���Ѿ����������ù�����һ�Σ��ù����Ǽ�������goodsCode�ֶεģ���goodsCode�Ȳ��ǿɼ��ֶ����������в����ܴ��ڣ�����ɱ�׼��ͳһ

		for (int i = 0; i < disField.size(); i++) {
			ReportField field = (ReportField) disField.get(i);
			String fieldName = field.getFieldName();
			String fieldIdentity = field.getFieldIdentity();
			DBFieldInfoBean fieldInfo = GlobalsTool.getFieldBean(fieldName);
			if (fieldInfo != null && fieldInfo.getFieldSysType() != null && fieldInfo.getFieldSysType().equals("RowMarker") && rowRemark.length() == 0
					&& fieldInfo.getTableBean().getTableName().equals(reportNumber)) {
				rowRemark = fieldInfo.getFieldName();
			}
			boolean flag = true;
			flag = DynDBManager.getViewRight(reportNumber, fieldName, scopeRight, fieldIdentity, loginBean);

			if (flag) {
				byte fieldType = field.getFieldType();
				String[] col = new String[5];
				col[0] = field.getDisplay() == null ? "" : field.getDisplay();
				col[1] = field.getWidth();
				switch (fieldType) {
				case DBFieldInfoBean.FIELD_INT:
					col[2] = "int";
					break;
				case DBFieldInfoBean.FIELD_DOUBLE:
					col[2] = "float";
					break;
				case DBFieldInfoBean.FIELD_DATE_LONG:
				case DBFieldInfoBean.FIELD_DATE_SHORT:
					col[2] = "date";
					break;
				case DBFieldInfoBean.FIELD_PIC:
					col[2] = "img";
					break;
				default:
					col[2] = "string";
					break;
				}
				if (field.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
					col[2] = "other";
				}
				if (field.getLinkAdd() != null && field.getLinkAdd().length() > 0) {
					if (field.getLinkAdd().equals("@ClassCode:")) {
						col[2] = "call:nextClass";
					} else {
						col[2] += "call";
					}
				}
				col[3] = field.getGroupName();
				col[4] = field.getAsFieldName();
				cols.add(col);
			}
		}

		if (rowRemark.length() > 0) {
			request.setAttribute("rowRemark", rowRemark);
		}

		StringBuffer SQLSave = new StringBuffer("");
		saveSql = saveSql.replaceAll("%", "@CentSign:");
		while (saveSql.indexOf("+") > -1) {
			String t = saveSql.substring(0, saveSql.indexOf("+"));
			saveSql = t + "@AddSign:" + saveSql.substring(saveSql.indexOf("+") + 1);
		}
		SQLSave.append("@SQL:").append(saveSql);

		StringBuffer paramListStr = new StringBuffer("");
		for (int i = 0; i < paramList.size(); i++) {
			String paramStr = String.valueOf(paramList.get(i));
			paramStr = paramStr.replaceAll("%", "@CentSign:");
			while (paramStr.indexOf("+") > -1) {
				String t = paramStr.substring(0, paramStr.indexOf("+"));
				paramStr = t + "@AddSign:" + paramStr.substring(paramStr.indexOf("+") + 1);
			}
			paramListStr.append(paramStr).append("@Param:");
		}
		if (paramListStr.length() > 0) {
			SQLSave.append("@ParamList:").append(paramListStr.substring(0, paramListStr.length() - "@Param:".length()));
		} else {
			SQLSave.append("@ParamList:");
		}
		SQLSave.append("@end:");
		request.setAttribute("SQLSave", SQLSave.toString());

		if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
			return rs;
		}

		String parentTemp = parentCode;
		if (!reportType.equals("popData")) {
			if (rs.getRetVal() != null) {
				rows = (ArrayList) rs.getRetVal();
			}
			request.setAttribute("reportNumber", reportNumber);
			request.setAttribute("reportName", reportSetBean.getReportName());

			//�����conditions��˳����˵��������ڱ�����Ƶ�ȱ�ݣ�û���������û��Լ�����������˳�򣬶��������е�˳����������
			//һ���е�˳�����������Ҫ�Բ�һ�£����������ԭ���ǣ����ڵ�һλ�������Ǹ�������ѡ������������������λ���ֿ⣬�����ˣ����ݱ�ţ�
			//���������һ���������е�˳��һ��
			//0�����ԣ�1����.�ֶ�����2ֵ��3����
			//����1ö�٣�16������5��ѡ��2�����ڣ�3���أ�4������

			ArrayList dateConditions = new ArrayList();//����������������������Ҫ��ʾ�ؼ������Ե��ݴ���
			ArrayList newConditions = new ArrayList();
			if (conditions.size() >= 2 && (((ArrayList<String[]>) conditions).get(0))[3].equals("2") && (((ArrayList<String[]>) conditions).get(1))[3].equals("2")) {
				newConditions.add((((ArrayList<String[]>) conditions).get(0))); //���ڷ�һλ
				dateConditions.add((((ArrayList<String[]>) conditions).get(0)));
				newConditions.add((((ArrayList<String[]>) conditions).get(1))); //���ڷ�һλ
				dateConditions.add((((ArrayList<String[]>) conditions).get(1)));
			}

			for (String[] row1 : (ArrayList<String[]>) conditions) {
				if (row1[3].equals("1") || row1[3].equals("16")) {
					newConditions.add(row1); //ö��
				}
			}
			//�������Ȼ�Ų�����
//			for (String[] row1 : (ArrayList<String[]>) conditions) {
//				if (row1[1].equals("ComFullName") || row1[1].equals("StockFullName") || row1[1].equals("EmpFullName") || row1[1].equals("BillNo")) {
//					newConditions.add(row1); //������λ��
//				}
//			}
			for (String[] row1 : (ArrayList<String[]>) conditions) {
				if (!newConditions.contains(row1)) {
					newConditions.add(row1); //����
				}
			}
			if (dateConditions.size() == 2) {
				//ֻ����������������һ�����ڣ�һ��С�ڵģ������Ĳ����д���
				
				//0�����ƣ�1����ʼ��������2�������ֶ�����3����ʼvalue��4������value
				String[] dataRow = new String[6];
				String[] row1 = (String[]) dateConditions.get(0);
				String[] row2 = (String[]) dateConditions.get(1);
				dataRow[0] = row1[0].endsWith("��") ? row1[0].substring(0, row1[0].length() - 1) : row1[0];
				dataRow[1] = row1[1];
				dataRow[2] = row2[1];
				dataRow[3] = row1[2];
				dataRow[4] = row2[2];
				request.setAttribute("dateConditions", dataRow);
				request.setAttribute("dateConditionsStr", gson.toJson(dataRow));
				newConditions.remove(0);
				newConditions.remove(0);
				
				if(userConditions.size() == 0){//û���û���������
					request.setAttribute("userDateConditions", dataRow);
				}else{
					for(String[] ss:userConditions){
						if(ss[0].equals(dataRow[1])){
							//���������д�����������
							request.setAttribute("userDateConditions", dataRow);
							break;
						}
					}
				}
			}
			//���״̬
			if (TABLELIST.equals(reportSetBean.getReportType())) {
				for (String[] row1 : (ArrayList<String[]>) newConditions) {
					if (row1[1].equals("workFlowNodeName")) {
						request.setAttribute("workFlowNodeNameCond", row1);
						request.setAttribute("workFlowNodeNameCondStr", gson.toJson(row1));
						newConditions.remove(row1); //���ڷ�һλ
						
						if(userConditions.size() == 0){//û���û���������
							request.setAttribute("userWorkFlowNodeNameCond", row1);
						}else{
							for(String[] ss:userConditions){
								if(ss[0].equals(row1[1])){
									//���������д�����������
									request.setAttribute("userWorkFlowNodeNameCond", row1);
									break;
								}
							}
						}
						break;
					}
				}
			}
			if(userConditions.size() == 0){//û���û���������
				request.setAttribute("userConditions", newConditions);
			}else{
				ArrayList userNewConditions = new ArrayList();
				for(String[] ss:userConditions){
					for (String[] row1 : (ArrayList<String[]>) newConditions) {
						if(ss[0].equals(row1[1])){
							//���������д���
							userNewConditions.add(row1);
							break;
						}
					}
				}
				request.setAttribute("userConditions", userNewConditions);
			}

			request.setAttribute("conditions", newConditions);
			request.setAttribute("conditionsStr", gson.toJson(newConditions));
			request.setAttribute("cols", cols);
			request.setAttribute("result", rows);
			request.setAttribute("haveStat", defBean.isHaveStat());

			String tableList="";
			if("true".equals(request.getAttribute("mobileAjax"))){
				//�����Ǵ��ֻ�ajax���Ĳ�ѯ����������Լ���֯���룬ͨ��request attribute���������е�ֵ
				
				request.setAttribute("allConditions", conditions); //�������е�����
				
			}else if (reportSetBean.getReportType().equals(TABLELIST)) {
				DBTableInfoBean dbTableInfo = DDLOperation.getTableInfo(map, tableName);
				if (dbTableInfo.getPerantTableName() != null && !"".equals(dbTableInfo.getPerantTableName())) {
					parentTableName = parentTableName == null ? "" : parentTableName;
					String mainTableName = parentTableName;
					request.setAttribute("mainTableName", mainTableName);
				} else {
					request.setAttribute("mainTableName", tableName);
				}
				request.setAttribute("tableName", tableName);

				DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, reportNumber);
				request.setAttribute("lngIfClass", tableInfo.getClassFlag());

				String isAllListQuery = request.getParameter("isAllListQuery");
				if (request.getParameter("query") != null && request.getParameter("query").equals("true") && selectType != null && !selectType.equals("normal")) {
					isAllListQuery = "YES";
					selectType = "endClass";
				}
				boolean cantAddLevel = false;
				//if (tableInfo.getClassFlag() == 1 && "YES".equals(isAllListQuery)) {
				//	cantAddLevel = true; //�ּ�����ѯ����δ����������ʾ��Ӱ�Ť
				//}else if (tableInfo.getClassFlag() == 1 && !"YES".equals(isAllListQuery)) {
				if (tableInfo.getClassFlag() == 1){ //ԭ���ǲ�ѯ����δ��ʱ��������Ӱ�Ť�����ڸĳɿ�����ӣ���ӵ���ǰparentCode�� zxy 
					if (scopeRight != null) {
						for (Object o : scopeRight) {
							LoginScopeBean lsb = (LoginScopeBean) o;
							if (lsb.getFlag().equals("0") && lsb.getIsAddLevel().equals("0") && lsb.getTableName().equals(tableInfo.getTableName())) {
								String[] claCodes = lsb.getScopeValue().split(";");
								if (claCodes.length > 0) {
//									for (String ccode : claCodes) {
//										//���ڵ����б��в���ʾ���������ˣ��������ַ������ܼ�鲻����
//										for (int i = 0; i < rows.size(); i++) {
//											TableListResult row = (TableListResult) rows.get(i);
//											if (row.getClassCode().equals(ccode) ) {
//												cantAddLevel = true;
//												break;
//											}
//										}
//										if (cantAddLevel)
//											break;
//									}
									//�з�ΧȨ�޿��ƣ���û��parentCode,˵�����Ǹ���Ŀ¼������ʾ��Ӱ�Ť
									if(parentCode==null || parentCode.length() ==0){
										cantAddLevel = true;
									}
								}
							}
							if (cantAddLevel)
								break;
						}
					}

					moduleType = moduleType == null ? "" : moduleType;
					Result rs3 = mgt.getParentName(parentCode, tableInfo, locale);
					if (rs3.retCode == ErrorCanst.RET_NOTSETROWMARKER) {
						request.setAttribute("parentName", "");
					} else {
						ArrayList parentName = (ArrayList) rs3.retVal;
						String parentUrl = "";
						if (parentName.size() == 0) {
							parentUrl += GlobalsTool.getMessage(request, "com.acc.ini.root");
						} else {
							parentUrl += "<a href=\"/UserFunctionQueryAction.do?tableName=" + tableName + "&parentCode=&operation=" + OperationConst.OP_QUERY + "&moduleType=" + moduleType
									+ "&winCurIndex=" + request.getParameter("winCurIndex") + "&checkTab=Y\">" + GlobalsTool.getMessage(request, "com.acc.ini.root") + "</a> >> ";
							for (int i = 0; i < parentName.size(); i++) {
								String[] nameClass = (String[]) parentName.get(i);
								if (nameClass[1].length() == (parentName.size()) * 5) {
									parentUrl += nameClass[0];
								} else {
									parentUrl += "<a href=\"/UserFunctionQueryAction.do?tableName=" + tableName + "&parentCode=" + nameClass[1] + "&operation=" + OperationConst.OP_QUERY
											+ "&moduleType=" + moduleType + "&winCurIndex=" + request.getParameter("winCurIndex") + "&checkTab=Y\">" + nameClass[0] + "</a> >> ";
								}

							}
						}
						if (parentCode.length() == 0 && selectType != null && (selectType.equals("endClass") || selectType.equals(""))) {
							parentUrl = "";
						}
						request.setAttribute("parentName", parentUrl);
					}

				}
				// CRM ��ѯ�ֵܱ�����
				if ("crmPopup".equals(request.getAttribute("popup"))) {
					rs.setRetVal(rows);
					return rs;
				}
				request.setAttribute("cantAddLevel", cantAddLevel);
				tableList = tableListPageTable(mop, request, cols, defBean, rows, tableName, loginBean, parentCode, reportSetBean, scopeRight, scopeRightUpdate, scopeRightDel,
						pageNo, pageSize, rowRemark);
				if ("draftPop".equals(draftQuery)) {
					tableList = tableList.replace("<input type=\"checkbox\" name=\"selAll\"  onClick=\"checkAll('keyId')\">", GlobalsTool.getMessage(request, "mrp.lb.chooseOne"));
					//tableList = tableList.replaceAll("Check", "Radio");
				}
			} else {
				request.setAttribute("tableName", tableName);
				ReportField classCode = defBean.getClassCode();
				if (classCode != null) {
					String name = classCode.getFieldName().split("\\.")[0];
					DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, name);
					if (tableInfo != null) {
						Result rs3 = mgt.getParentName(parentCode, tableInfo, locale);
						if (rs3.retCode == ErrorCanst.RET_NOTSETROWMARKER) {
							request.setAttribute("parentName", "");
						} else {
							ArrayList parentName = (ArrayList) rs3.retVal;
							String parentUrl = "";
							if (parentName.size() == 0) {
								parentUrl += GlobalsTool.getMessage(request, "com.acc.ini.root");
							} else {
								parentUrl += "<a href=\"javascript:gotoClass('');\">" + GlobalsTool.getMessage(request, "com.acc.ini.root") + "</a> >> ";
								for (int i = 0; i < parentName.size(); i++) {
									String[] nameClass = (String[]) parentName.get(i);
									if (nameClass[1].length() == (parentName.size()) * 5) {
										parentUrl += nameClass[0];
									} else {
										parentUrl += "<a href=\"javascript:gotoClass('" + nameClass[1] + "');\">" + nameClass[0] + "</a> >> ";
									}
									if (parentCode.length() == 0 && selectType != null && selectType.equals("endClass")) {
										parentUrl = "";
									}
								}
							}
							request.setAttribute("parentName", parentUrl);
						}
					}
				}
				tableList = reportPageTable(mop, request, cols, rows, defBean, reportIfClass, reportSetBean, conditions, pageNo, pageSize, disField, defBean.getDisFields2(), defBean.isCross(),
						loginBean, scopeRight);
			}
			conditionSessionMap.put(reportNumber+moduleType+"parentCode", parentCode); 
			request.setAttribute("endClassNumber", reportSetBean.getEndClassNumber());
			request.setAttribute("parentCode", parentCode);
			rs.setPageNo(pageNo);
			rs.setPageSize(pageSize);
			request.setAttribute("fusionList", defBean.getFusionList());
			request.setAttribute("tableList", tableList);
		}
		return rs;
	}

	public String parseMultiVal(String mark, String content, ArrayList paramList, String value) {
		if(mark.indexOf("?") == -1){
			
			for(int i=0;i<200;i++){ //�������ë��bug
				String ss = "";
				//markǰû��+�ŵ�
				Pattern pattern = Pattern.compile("([\\w\\.]+)[\\s]*(=|like|in)[\\s]*(" + mark+")" , Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(content);
				
				boolean hasTable=false;
				while (matcher.find()) {
					String all = matcher.group();
					String field = matcher.group(1);
					String ys = matcher.group(2);
					int mpos = matcher.start() ; //��������λ����Ϣ��Դ�ַ�����λ�ã����Ǿ���ѭ�����ַ��������˳��ȣ����Ա�������������
					
					//��mark���ǲ��Ǹ�+�ţ������+�Ż�Ҫ���ϼӵ�����
					String ends = content.substring(mpos+all.length());
					if(ends.trim().startsWith("+")){
						all += ends.substring(0,ends.indexOf("+")+1);
						ends = ends.substring(ends.indexOf("+")+1);
						//���+���ǡ��ţ��ҵ���һ������
						if(ends.trim().startsWith("'")){
							all +=ends.substring(0,ends.indexOf("'",ends.indexOf("'")+1)+1);
						}else{
							ends = ends.trim();
							all += ends.substring(0,ends.indexOf(" "));
						}
					}
					
					String retStr = "";
					
					if(ys.equals("in")){
						String[] vs = value.split(",");
						for(String v:vs){
							retStr += ",'"+v+"'";
						}
						retStr = field +" in ( "+(retStr.substring(1))+" )";
					}else{
						String[] vs = value.split(",");
						for(String v:vs){
							retStr += " or "+all.replace(mark, "'" + v + "'");
						}
						retStr = " ( "+(retStr.substring(3))+" )";
					}				
					
					content = content.substring(0, mpos) + retStr + content.substring(mpos+all.length());
					
					hasTable = true; //��־�ҵ����ݡ�
					break;
				}
				if (!hasTable) {
					//�Ҳ������ݣ��˳�ѭ��
					break;
				}
			}
			
			for(int i=0;i<200;i++){ //�������ë��bug
				String ss = "";
				//markǰ��+�ŵ�
				Pattern pattern = Pattern.compile("([\\w\\.]+)[\\s]*(=|like|in)[\\s]*(['%\\w\\.]+\\+[\\s]*" + mark+")" , Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(content);
				
				boolean hasTable=false;
				while (matcher.find()) {
					String all = matcher.group();
					String field = matcher.group(1);
					String ys = matcher.group(2);
					int mpos = matcher.start() ; //��������λ����Ϣ��Դ�ַ�����λ�ã����Ǿ���ѭ�����ַ��������˳��ȣ����Ա�������������
					
					//��mark���ǲ��Ǹ�+�ţ������+�Ż�Ҫ���ϼӵ�����
					String ends = content.substring(mpos+all.length());
					if(ends.trim().startsWith("+")){
						all += ends.substring(0,ends.indexOf("+")+1);
						ends = ends.substring(ends.indexOf("+")+1);
						//���+���ǡ��ţ��ҵ���һ������
						if(ends.trim().startsWith("'")){
							all +=ends.substring(0,ends.indexOf("'",ends.indexOf("'")+1)+1);
						}else{
							ends = ends.trim();
							all += ends.substring(0,ends.indexOf(" "));
						}
					}
					
					String retStr = "";
					
					if(ys.equals("in")){
						String[] vs = value.split(",");
						for(String v:vs){
							retStr += ",'"+v+"'";
						}
						retStr = field +" in ( "+(retStr.substring(1))+" )";
					}else{
						String[] vs = value.split(",");
						for(String v:vs){
							retStr += " or "+all.replace(mark, "'" + v + "'");
						}
						retStr = " ( "+(retStr.substring(3))+" )";
					}									
					content = content.substring(0, mpos) + retStr + content.substring(mpos+all.length());
					
					hasTable = true; //��־�ҵ����ݡ�
					break;
				}
				if (!hasTable) {
					//�Ҳ������ݣ��˳�ѭ��
					break;
				}
			}
			
			//����������δ�ܴ���˵��������δ���ǵ��ĵط�
			if(content.indexOf(mark) > -1){
				BaseEnv.log.error("�����ֵ����parseMultiValδ�ܴ����ֶ�"+mark+";sql="+content);
				throw new RuntimeException("�����ֵ����parseMultiValδ�ܴ����ֶ�"+mark);
			}
			return content;
		}else{  //�������?�ŵĲ��ܰ�����������ʽ��ʽ��������Ȼ���Ϸ���
			String ss = "";
			while (content.contains(mark)) {
				String s = "";
				int index2 = content.indexOf(")", content.indexOf(mark));
				int index3 = content.indexOf(" and ", content.indexOf(mark));
				int index4 = content.indexOf(" or ", content.indexOf(mark));
				if (index2 != -1 && (index2 < index3 || index3 == -1) && (index2 < index4 || index4 == -1)) {
					s = content.substring(0, index2 + 1);
					content = content.substring(index2 + 1);
				} else if (index3 != -1 && (index3 < index2 || index2 == -1) && (index3 < index4 || index4 == -1)) {
					s = content.substring(0, index3 + 5);
					content = content.substring(index3 + 5);
				} else if (index4 != -1 && (index4 < index3 || index3 == -1) && (index4 < index2 || index2 == -1)) {
					s = content.substring(0, index4 + 4);
					content = content.substring(index4 + 4);
				}
	
				int index1 = s.indexOf(mark);
				String preS = s.substring(0, index1);
				String preE = s.substring(index1);
				String ts = "";
				index2 = preS.lastIndexOf("(");
				index3 = preS.lastIndexOf(" and ");
				index4 = preS.lastIndexOf(" or ");
				int index5 = preS.lastIndexOf(")");
	
				String condField = "";
	
				while (index2 > index3 && index2 > index4 && index5 > index2) {
					index2 = preS.substring(0, index2).lastIndexOf("(");
					index3 = preS.substring(0, index2).lastIndexOf(" and ");
					index4 = preS.substring(0, index2).lastIndexOf(" or ");
					index5 = preS.substring(0, index5).lastIndexOf(")");
				}
				if (index2 > index3 && index2 > index4) {
					condField = preS.substring(index2 + 1);
					ts = preS.substring(0, index2 + 1);
				} else if (index3 > index2 && index3 > index4) {
					condField = preS.substring(index3 + 5);
					ts = preS.substring(0, index3 + 5);
				} else if (index4 > index3 && index4 > index2) {
					condField = preS.substring(index4 + 4);
					ts = preS.substring(0, index4 + 4);
				}
	
				index2 = preE.indexOf(")");
				index3 = preE.indexOf(" and ");
				index4 = preE.indexOf(" or ");
				String te = "";
				if (index2 != -1 && (index2 < index3 || index3 == -1) && (index2 < index4 || index4 == -1)) {
					condField += preE.substring(0, index2);
					te = preE.substring(index2);
				} else if (index3 != -1 && (index3 < index2 || index2 == -1) && (index3 < index4 || index4 == -1)) {
					condField += preE.substring(0, index3);
					te = preE.substring(index3);
				} else if (index4 != -1 && (index4 < index3 || index3 == -1) && (index4 < index2 || index2 == -1)) {
					condField += preE.substring(0, index4);
					te = preE.substring(index4);
				}
	
				String temp = ts + "(";
				String[] vals = value.split(",");
				for (int i = 0; i < vals.length; i++) {
					if (paramList != null) {
						paramList.add(vals[i]);
						temp += condField + " or ";
					} else {
						temp += condField.replace(mark, "'" + vals[i] + "'") + " or ";
					}
				}
	
				temp = temp.substring(0, temp.length() - 3) + ")" + te;
				ss += temp;
			}
			return ss + content;
		}
	}

	private String reportPageTable(MOperation mop, HttpServletRequest request, ArrayList cols, ArrayList rows, DefineReportBean defBean, String reportIfClass, ReportsBean reportSetBean,
			ArrayList conditions, int pageNo, int pageSize, ArrayList disField, ArrayList disField2, boolean isCross, LoginBean loginBean, ArrayList scopeRight) throws Exception {
		HttpSession sess = request.getSession();
		LoginBean bean = (LoginBean) sess.getAttribute("LoginBean");
		String moreLanguage = GlobalsTool.getMessage(request, "com.listgrid.lock") + ";" + GlobalsTool.getMessage(request, "com.listgrid.cancel.lock") + ";"
				+ GlobalsTool.getMessage(request, "com.listgrid.save.config") + ";" + GlobalsTool.getMessage(request, "com.listgrid.default.config") + ";"
				+ GlobalsTool.getMessage(request, "com.listgrid.hidden.column") + ";" + GlobalsTool.getMessage(request, "com.listgrid.no.hidden") + ";"
				+ GlobalsTool.getMessage(request, "com.listgrid.add.column") + ";" + GlobalsTool.getMessage(request, "common.lb.ok") + ";" + GlobalsTool.getMessage(request, "mrp.lb.cancel");
		String reportNumber = defBean.getReportNumber();
		ListGrid listGrid = new ListGrid(bean, defBean);
		Boolean safari = (Boolean) request.getAttribute("Safari");
		if ((safari != null && safari == true) || "false".equals(GlobalsTool.getSysSetting("reportFlash"))) {
			listGrid = new ListGridHtml(bean);
		}
		listGrid.moreLanguage = moreLanguage;
		listGrid.isSort = reportSetBean.getColTitleSort() == 1 ? true : false;
		listGrid.pageNo = pageNo;
		listGrid.pageSize = pageSize;
		listGrid.fixNumberCol = reportSetBean.getFixNumberCol();
		listGrid.fixTitle = reportSetBean.getFixListTitle() == 1 ? true : false;
		listGrid.hiddenList = queryHiddenConfig(disField2, reportNumber, request);
		if (reportSetBean.getShowHead() != null && reportSetBean.getShowHead() == 0)
			listGrid.noHead = true; // �ⷢ������ϸ����ʾ��ͷ
		if (reportSetBean.getShowRowNumber() != null && reportSetBean.getShowRowNumber() == 0)
			listGrid.autoNumber = false;// ����ʾ�к�

		if (mop == null) {
			String parentTableName = request.getParameter("parentTableName");
			parentTableName = parentTableName == null ? "" : parentTableName;
			if (parentTableName != null && !"".equals(parentTableName)) {
				mop = (MOperation) loginBean.getOperationMap().get("/UserFunctionQueryAction.do?parentTableName=" + parentTableName + "&tableName=" + reportNumber);
				if (mop == null) {
					mop = (MOperation) loginBean.getOperationMap().get("/UserFunctionQueryAction.do?tableName=" + parentTableName);
				}
			}
		}
		if (mop != null) {
			listGrid.configScope =true;// loginBean.getId().equals("1");
		}
		for (int i = 0; i < cols.size(); i++) {
			String[] col = (String[]) cols.get(i);
			listGrid.addCols(col[0], col[4], Integer.parseInt(col[1]), col[2], col[3]);
		}
		if ("1".equals(reportIfClass)) {
			listGrid.addCols(GlobalsTool.getMessage(request, "common.lb.viewNextClass"), "nextClass", 80, "nosort", "");
			if (reportSetBean.getEndClassNumber() != null && reportSetBean.getEndClassNumber().length() > 0) {
				listGrid.addCols(GlobalsTool.getMessage(request, "common.lb.operation"), "endClass", 80, "nosort", "");
			}
		}

		for (int i = 0; i < rows.size(); i++) {
			Object[] row = (Object[]) rows.get(i);
			String[] rowData = row[0].toString().split("','");
			String[] rowNew = null;
			if ("1".equals(reportIfClass)) {
				if (reportSetBean.getEndClassNumber() != null && reportSetBean.getEndClassNumber().length() > 0) {
					rowNew = new String[rowData.length + 3];
				} else {
					rowNew = new String[rowData.length + 2];
				}
			} else {
				rowNew = new String[rowData.length + 1];
			}

			if (row[0].toString().contains("rowOne")) {
				rowNew = new String[rowData.length];
				rowNew[0] = GlobalsTool.getMessage(request, "common.lb.total");
				for (int j = 1; j < rowData.length; j++) {
					if (!rowData[j].equals("rowOne'")) {
						rowNew[j] = rowData[j];
					}
				}
				rowNew[rowNew.length - 1] = row[1].toString();
				listGrid.addStats(rowNew);
			} else if (row[0].toString().contains("rowAll")) {
				rowNew[0] = GlobalsTool.getMessage(request, "common.lb.totalAll");
				for (int j = 1; j < rowData.length; j++) {
					if (!rowData[j].equals("rowAll'")) {
						rowNew[j] =(j== rowData.length-1?rowData[j].substring(0,rowData[j].length()-1):rowData[j]);
						
					}
				}
				rowNew[rowNew.length - 1] = row[1].toString();
				listGrid.addStatsAll(rowNew);
			} else {
				for (int j = 0; j < rowData.length; j++) {
					rowNew[j] = rowData[j];
				}
				rowNew[0] = rowNew[0].substring(1);
				String temp;
				String tempEnd = "";
				if ("1".equals(reportIfClass)) {
					if (reportSetBean.getEndClassNumber() != null && reportSetBean.getEndClassNumber().length() > 0) {
						rowNew[rowNew.length - 4] = rowNew[rowNew.length - 4].substring(0, rowNew[rowNew.length - 4].length() - 1);
					} else {
						rowNew[rowNew.length - 3] = rowNew[rowNew.length - 3].substring(0, rowNew[rowNew.length - 3].length() - 1);
					}

					rowNew[rowNew.length - 1] = row[1].toString();
					if (row[3] != null && Integer.parseInt(row[3].toString()) > 0) { 
						
						temp = GlobalsTool.getMessage(request, "common.lb.viewNextClass") + "#;#" + row[2] ;
						rowNew[rowNew.length - 1] = "nextClass(&apos;" + row[2] + "&apos;)"; 
						if (reportSetBean.getEndClassNumber() != null && reportSetBean.getEndClassNumber().length() > 0) {
							tempEnd = GlobalsTool.getMessage(request, "common.lb.endClass") + "#;# " + row[2];
							//�ѷּ��������������ȥ 
							//���������ķ����Ǵ���ģ�Ӧ��Ҫ��δ���б�������з�����Ҫ��Щ�������оʹ���ȥ�����������ڴ����ּ�����������з���
							//δ��������������ȡ��Ӧ��ֵ

							//							DefineReportBean endClassBean = DefineReportBean.parse(reportSetBean.getEndClassNumber() + "SQL.xml", GlobalsTool.getLocale(request).toString(), request);
							//							ArrayList endcondField = endClassBean.getConFields();
							//							System.out.print(0);
						}
					} else {
						temp = GlobalsTool.getMessage(request, "common.lb.noChild");
						tempEnd = GlobalsTool.getMessage(request, "common.lb.noChild");
					}
					if (reportSetBean.getEndClassNumber() != null && reportSetBean.getEndClassNumber().length() > 0) {
						rowNew[rowNew.length - 3] = temp;
						rowNew[rowNew.length - 2] = tempEnd;
					} else {
						rowNew[rowNew.length - 2] = temp;
					}
				} else {
					rowNew[rowNew.length - 2] = rowNew[rowNew.length - 2].substring(0, rowNew[rowNew.length - 2].length() - 1);
					rowNew[rowNew.length - 1] = row[1].toString();
				}
				for (int j = 0; j < rowNew.length; j++) {
					rowNew[j] = GlobalsTool.encodeHTML2(rowNew[j]);
				}
				listGrid.addRows(rowNew);
			}
		}
		return listGrid.toString();
	}

	/**
	 * @return String
	 */
	private String tableListPageTable(MOperation mo, HttpServletRequest request, ArrayList cols, DefineReportBean defBean , ArrayList rows, String tableName,LoginBean lg , String parentCode,
			ReportsBean report, ArrayList scopeRightAll, ArrayList scopeRightUpdate, ArrayList scopeRightDel, int pageNo, int pageSize, String rowRemark) throws Exception {

		boolean CannotOper = false;
		String loginId = lg.getId();
		// �����ǰ�ڼ䲻Ϊ�ڳ������ڳ������ӣ�ɾ�����޸İ�ť������ʾ
		if (!"-1".equals(((Hashtable) BaseEnv.sessionSet.get(loginId)).get("NowPeriod"))) {
			String moduleType = request.getParameter("moduleType");
			if (tableName.equals("tblCompanybeginning") || tableName.equals("tblBeginStock") || (tableName.equals("tblFixedAssetAdd") && "0".equals(moduleType))) {
				CannotOper = true;
				request.setAttribute("CannotOper", "true");
			}
		}

		this.rowSize = rows.size();// mj
		DynDBManager mgt = new DynDBManager();
		// �������Щ��,���Ŵ����ĵ������޸�Ȩ��
		String[] deptRight = mgt.getDeptRight(scopeRightUpdate, scopeRightDel, scopeRightAll);
		String updateOtherRight = deptRight[0];//�鿴���˵���Ȩ��
		String updateDeptRight = deptRight[1];//���Ź�ϽȨ��
		// �������Щ��,���Ŵ����ĵ�����ɾ��Ȩ��
		String delOtherRight = deptRight[2];
		String delDeptRight = deptRight[3];
		
		ArrayList disField2 = defBean.getDisFields2();
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		DBTableInfoBean dbTableInfo = DDLOperation.getTableInfo(map, tableName);
		String parentTableName = request.getParameter("parentTableName");
		parentTableName = parentTableName == null ? "" : parentTableName;

		String f_brother = request.getParameter("f_brother");
		f_brother = f_brother == null ? "" : f_brother.trim();
		String currentRow = request.getParameter("currentRow");
		currentRow = currentRow == null ? "" : currentRow.trim();
		String moreLanguage = GlobalsTool.getMessage(request, "com.listgrid.lock") + ";" + GlobalsTool.getMessage(request, "com.listgrid.cancel.lock") + ";"
				+ GlobalsTool.getMessage(request, "com.listgrid.save.config") + ";" + GlobalsTool.getMessage(request, "com.listgrid.default.config") + ";"
				+ GlobalsTool.getMessage(request, "com.listgrid.hidden.column") + ";" + GlobalsTool.getMessage(request, "com.listgrid.no.hidden") + ";"
				+ GlobalsTool.getMessage(request, "com.listgrid.add.column") + ";" + GlobalsTool.getMessage(request, "common.lb.ok") + ";" + GlobalsTool.getMessage(request, "mrp.lb.cancel");
		ListGrid listGrid = new ListGrid(lg,defBean);
		Boolean safari = (Boolean) request.getAttribute("Safari");
		if ((safari != null && safari == true) || "false".equals(GlobalsTool.getSysSetting("reportFlash"))) {
			listGrid = new ListGridHtml(lg);
		}
		listGrid.moreLanguage = moreLanguage;
		listGrid.setReportType(report.getReportType());
		listGrid.isSort = report.getColTitleSort().intValue() == 1 ? true : false;
		listGrid.pageNo = pageNo;
		listGrid.rowRemark = rowRemark;
		listGrid.pageSize = pageSize;
		listGrid.fixNumberCol = report.getFixNumberCol();
		listGrid.listType = report.getListType();
		listGrid.currentRow = currentRow;
		listGrid.hiddenList = queryHiddenConfig(disField2, tableName, request);
		listGrid.configScope =true;// "1".equals(((LoginBean) request.getSession().getAttribute("LoginBean")).getId());

		listGrid.addCols("hidden", "hidden", 0, "", "");

		for (int i = 0; i < cols.size(); i++) {
			String[] col = (String[]) cols.get(i);
			int width = 0;
			if (col[1].indexOf(".") > -1) {
				width = Integer.parseInt(col[1].substring(0, col[1].indexOf(".")));
			} else {
				width = Integer.parseInt(col[1]);
			}
			listGrid.addCols(col[0], col[4], width, col[2], col[3]);
		}

		String draftQuery = request.getParameter("draftQuery");
		listGrid.draftQuery = draftQuery;

		request.setAttribute("draftQuery", draftQuery);
		request.setAttribute("saveDraft", draftQuery);
		request.setAttribute("showDetail", report.getShowDet());
		OAWorkFlowTemplate workFlowDesign = BaseEnv.workFlowInfo.get(tableName);
		int lngIfClass = Integer.parseInt(request.getAttribute("lngIfClass").toString());

		if (!"draftPop".equals(draftQuery)) {
			/* �Ƶ�����û���޸�Ȩ�޵������ �����޸Ĳݸ� */
			if (report.getShowDet() == 1) {
				listGrid.addCols(GlobalsTool.getMessage(request, "common.lb.detail"), "detail", 40, "nosort", "");
			}
			String draftUpdate = BaseEnv.systemSet.get("draftUpdate").getSetting();
			if ((mo.update() || ("draft".equals(draftQuery) && "true".equals(draftUpdate))) && !CannotOper) {
				listGrid.addCols(GlobalsTool.getMessage(request, "common.lb.update"), "update", 40, "nosort", "");
			}
			if (lngIfClass != 0) {
				if("true".equals(GlobalsTool.getSysSetting("showparentClass"))){
					listGrid.addCols(GlobalsTool.getMessage(request, "common.lb.viewNextClass"), "next", 53, "nosort", "");
				}
				if (mo.add() && (parentCode.length() / 5 < dbTableInfo.getClassCount() - 1)) {
					listGrid.addCols(GlobalsTool.getMessage(request, "common.lb.addNextClass"), "addClass", 67, "nosort", "");
				}
			}
		}
		if (!"draft".equals(draftQuery) && !"draftPop".equals(draftQuery) && null != workFlowDesign && workFlowDesign.getTemplateStatus() == 1) {
			request.setAttribute("designId", workFlowDesign.getId());
			listGrid.addCols("���״̬", "status", 64, "nosort", "");
			listGrid.addCols(GlobalsTool.getMessage(request, "common.lb.check"), "check", 52, "nosort", "");
			listGrid.addCols(GlobalsTool.getMessage(request, "oa.workflow.hurryTrans"), "hurryTrans", 52, "nosort", "");
		} else if (!"draftPop".equals(draftQuery) && TABLELIST.equals(report.getReportType()) && dbTableInfo.getDraftFlag() == 1) {
			listGrid.addCols("���״̬", "status", 64, "nosort", "");
		}

		HashMap winIndexMap = (HashMap) request.getSession().getAttribute(BaseEnv.WIN_MAP);
		String winCurIndex = request.getParameter("winCurIndex"); 
		String reverses = "";
		/* ��ѯ��ǰ���ݱ���ί�и��ҵĹ������� */
		HashMap<String, String> consignMap = new HashMap<String, String>();
		if (workFlowDesign != null) {
			reverses = workFlowDesign.getAllowVisitor();
			consignMap = DynDBManager.queryConsignation(loginId, workFlowDesign.getId());
		}

		/** �Ƿ�ֱ�ӽ��뵽�������* */
		String goDetail = request.getParameter("toDetail");

		for (int i = 0; i < rows.size(); i++) {

			TableListResult row = (TableListResult) rows.get(i);
			if (row.getKeyId().length() > 0) {//row[1]�����¼ID�������ʾ�����У����Ϊ�ձ�ʾͳ����
			//				if (nextMap != null) {
			//					nextMap.put(i + 1, (String) row[1]);
			//				}
				boolean delFlag = false;
				boolean upFlag = false;

				String[] rowNew = new String[listGrid.getCols().size() + 2];
				//���еĲ��Ŵ��ţ������departmentCode�ֶ���ȡdepartmentCode����ȡ�����˲��ţ��޾�����ȡ�����˲���
				//String rowDept = row.getDeptCodeRE().length() > 0 ? row.getDeptCodeRE() : row.getDeptCodeR();
				//Ŀǰ�����ݶ��в���
				String rowDept = row.getDeptCodeRE() !=null? row.getDeptCodeRE() : row.getDeptCodeR();

				String del = "";
				if (mo.delete() && !CannotOper
						&& mgt.getDeleteRight(rowDept, delDeptRight, delOtherRight, loginId, row.getCreateBy(), row.getEmployeeId(), row.getWorkFlowNode(), row.getWorkFlowNodeName(), workFlowDesign)) {
					del = "true";
				} else {
					del = "false";
				}
				/* �Ƿ��Ƿ���� */
				if (row.getChildCount() != null && Integer.parseInt(row.getChildCount().toString()) > 0) {
					rowNew[0] = row.getKeyId() + ";hasChild;" + row.getClassCode();
				} else {
					rowNew[0] = String.valueOf(row.getKeyId());
				}

				String[] rowData = row.getRowDis().split("','");
				for (int j = 0; j < rowData.length; j++) {
					rowNew[j + 1] = rowData[j];
				}
				rowNew[1] = rowNew[1].substring(1);
				rowNew[rowData.length] = rowNew[rowData.length].substring(0, rowNew[rowData.length].length() - 1);
				int rowCol = rowData.length + 1;
				String temp = "";
				temp = "";
				if (report.getShowDet() == 1 && !"draftPop".equals(draftQuery)) {
					String strTemp = GlobalsTool.getMessage(request, "common.lb.detail") + "#;#" + row.getKeyId() + "," + row.getClassCode();
					temp += strTemp;
					if ("go".equals(goDetail) && i == 0 && dbTableInfo.getHasNext() == 1) {
						request.setAttribute("toDetail", strTemp);
					}
					rowNew[rowCol] = temp;
					rowCol = rowCol + 1;
				}
				temp = "";
				boolean flag = false;
				if (!"draftPop".equals(draftQuery)) {
					if (mo.update() && !CannotOper) {
						flag = mgt.getUpdateRight(rowDept, updateDeptRight, updateOtherRight, loginId, row.getCreateBy(), row.getEmployeeId(), row.getCheckPersons(), consignMap,
								row.getWorkFlowNode(), row.getWorkFlowNodeName(), workFlowDesign);
						if (flag) {
							temp = GlobalsTool.getMessage(request, "common.lb.update") + "#;#" + row.getKeyId() + "," + row.getClassCode();
						} else {
							temp = GlobalsTool.getMessage(request, "common.lb.update");
						}

						rowNew[rowCol] = temp;
						rowCol = rowCol + 1;
					}

					if (lngIfClass != 0) {
						if("true".equals(GlobalsTool.getSysSetting("showparentClass"))){
							if (row.getChildCount() != null && Integer.parseInt(row.getChildCount().toString()) > 0) {
								temp = GlobalsTool.getMessage(request, "common.lb.viewNextClass") + "#;#" + row.getKeyId() + "," + row.getClassCode();
							} else {
								temp = GlobalsTool.getMessage(request, "common.lb.noChild");
							}
							rowNew[rowCol] = temp;
							rowCol = rowCol + 1;
						}

						if (mo.add() && (parentCode.length() / 5 < dbTableInfo.getClassCount() - 1)) {
							temp = GlobalsTool.getMessage(request, "common.lb.addNextClass") + "#;#" + row.getClassCode();
							rowNew[rowCol] = temp;
							rowCol = rowCol + 1;
						}
					}
				}

				if (!"draft".equals(draftQuery) && !"draftPop".equals(draftQuery) && null != workFlowDesign && workFlowDesign.getTemplateStatus() == 1) {
					if (row.getWorkFlowNode() == null)
						row.setWorkFlowNode("-1");
					// �����ǰ�������Ѿ������̰汾����Ҫ���ݵ���id��ѯģ��id
					String designId = workFlowDesign.getId();
					if (!workFlowDesign.getId().equals(workFlowDesign.getSameFlow())) {
						designId = new PublicMgt().getDesignIdByKeyId((String) row.getKeyId(), tableName);
						if (designId == null || designId.length() == 0) {
							designId = workFlowDesign.getId();
						}
					}
					WorkFlowDesignBean designBean = BaseEnv.workFlowDesignBeans.get(designId);
					FlowNodeBean curNode = null;
					if (designBean != null) {
						HashMap<String, FlowNodeBean> nodeMap = designBean.getFlowNodeMap();
						curNode = nodeMap.get(row.getWorkFlowNode());
					}
					if (curNode != null) {
						// ��ǰ���ݵ����״̬
						if(row.getChildCount() != null && Integer.parseInt(row.getChildCount()) > 0){
							rowNew[rowCol]= ""; //�����Ŀ¼������ʾ״̬
						}else if ("draft".equals(row.getWorkFlowNodeName())) {
							rowNew[rowCol] = "�ݸ�";
						} else { //״̬�ֶ�
							rowNew[rowCol] = curNode.getDisplay() + "#;#" + row.getKeyId();
						}
						rowCol = rowCol + 1;
						if (row.getCheckPersons() != null && consignMap.size() > 0) {
							for (String person : row.getCheckPersons().split(";")) {
								if (consignMap.get(person) != null) {
									row.setCheckPersons(row.getCheckPersons() + loginId + ";");
								}
							}
						}
						// ����ˣ����̽��������з����Ȩ�޵��ˣ������,����(��һ�ڵ��г��صĹ���)��ť
						if (!"draft".equals(row.getWorkFlowNodeName()) && row.getWorkFlowNode().equals("-1") && 
								(DynDBManager.isRetCheckPer(lg,workFlowDesign, row.getDeptCodeR()) || DynDBManager.isRetCheckPer(lg,workFlowDesign, row.getDeptCodeRE()) ) ) {
							rowNew[rowCol] = GlobalsTool.getMessage(request, "define.button.reverse") + "#;#" + row.getKeyId() + ",reverse";
						} else if ((row.getCheckPersons().indexOf(";" + lg.getId() + ";") >= 0 && !"-1".equals(row.getWorkFlowNode())) 
								|| ("0".equals(row.getWorkFlowNode()) && flag)) {
							rowNew[rowCol] = GlobalsTool.getMessage(request, "common.lb.approve") + "#;#" + row.getKeyId() + ",approve";
						} else if (curNode.isAllowCancel() && row.getLastNodeID() != null && !"null".equals(row.getLastNodeID())) {
							rowNew[rowCol] = GlobalsTool.getMessage(request, "oa.lb.cancel") + "#;#" + row.getKeyId() + ",cancel," + (row.getLastNodeID() == null ? "0" : row.getLastNodeID()) + ","
									+ row.getWorkFlowNode();
						} else {
							//zxyע��----�����ܷ���ˣ����أ���ˣ���ζ��û���κβ��������Բ��ܸ�ֵ�����������հװ�Ť
							//rowNew[rowCol] = ";" + row[1];
							rowNew[rowCol] = "";
						}
						rowCol = rowCol + 1;

						// �߰�,ֻ�д����˿��Դ߰�,���������ǰ����˲��Լ�
						if (!row.getWorkFlowNode().equals("-1") && loginId.equals(row.getCreateBy()) && row.getCheckPersons().indexOf(";" + lg.getId() + ";") < 0) {
							rowNew[rowCol] = GlobalsTool.getMessage(request, "oa.workflow.hurryTrans") + "#;#" + row.getCheckPersons();
						} else {
							rowNew[rowCol] = "";
						}
						rowCol = rowCol + 1;
					} else {
						if ("draft".equals(row.getWorkFlowNodeName())) {
							rowNew[rowCol] = "�ݸ�";
						} else if (row.getWorkFlowNode().equals("-1")) {
							rowNew[rowCol] = "��˽���#;#" + row.getKeyId();
						} else if (row.getWorkFlowNode().equals("0")) {
							rowNew[rowCol] = "��ʼ;" + row.getKeyId();
						} else { //״̬�ֶ�
							rowNew[rowCol] = "�����#;#" + row.getKeyId();
						}
						rowCol = rowCol + 1;
						rowNew[rowCol] = ";";
						rowCol = rowCol + 1;
						rowNew[rowCol] = ";";
						rowCol = rowCol + 1;
					}
				} else if (!"draftPop".equals(draftQuery) && TABLELIST.equals(report.getReportType()) && dbTableInfo.getDraftFlag() == 1) {
					rowNew[rowCol] = "draft".equals(row.getWorkFlowNodeName()) ? "�ݸ�" : "����";
					if(row.getChildCount() != null && Integer.parseInt(row.getChildCount()) > 0){
						rowNew[rowCol]= ""; //�����Ŀ¼������ʾ״̬
					}
					rowCol = rowCol + 1;
				}
				if (lngIfClass != 0) {
					if (row.getChildCount() != null && Integer.parseInt(row.getChildCount()) > 0) {
						temp = "/UserFunctionQueryAction.do?tableName=" + tableName + "&amp;keyId=" + row.getKeyId() + "&amp;parentCode=" + row.getClassCode() + "&amp;operation="
								+ OperationConst.OP_QUERY;
					} else {
						temp = "/UserFunctionAction.do?tableName=" + tableName + "&amp;keyId=" + row.getKeyId() + "&amp;parentCode=" + row.getClassCode() + "&amp;operation="
								+ OperationConst.OP_UPDATE_PREPARE;
					}
				} else {
					temp = "/UserFunctionAction.do?tableName=" + tableName + "&amp;keyId=" + row.getKeyId() + "&amp;operation=" + OperationConst.OP_DETAIL + "&amp;tabList=Y";
				}
				if (parentTableName.length() > 0) {
					temp = temp + "&amp;parentTableName=" + parentTableName + "&amp;f_brother=" + f_brother + "&amp;winCurIndex=" + request.getParameter("winCurIndex");
				}
				rowNew[rowCol] = temp;
				rowCol = rowCol + 1;

				if (lngIfClass != 0) {
					if (row.getChildCount() != null && Integer.parseInt(row.getChildCount()) > 0) {
						temp = "next(&apos;" + row.getKeyId() + "," + row.getClassCode() + "&apos;)~" + del;
					} else {
						temp = "detail(&apos;" + row.getKeyId() + "," + row.getClassCode() + "&apos;)~" + del + "~" + row.getWorkFlowNodeName();
					}
				} else {
					temp = "detail(&apos;" + row.getKeyId() + ",," + "&apos;)~" + del + "~" + row.getWorkFlowNodeName();
				}
				rowNew[rowCol] = temp;
				for (int j = 0; j < rowNew.length; j++) {

					rowNew[j] = rowNew[j].replaceAll("\\\\\'", "'");
				}
				listGrid.addRows(rowNew);
			} else {
				if (!"draftPop".equals(draftQuery)) {
					if (row.getRowDis().toString().contains("rowOne")) {
						String[] rowData = row.getRowDis().toString().split("','");
						String[] rowNew = new String[rowData.length + 1];
						rowNew[0] = GlobalsTool.getMessage(request, "common.lb.total");
						for (int j = 0; j < rowData.length; j++) {
							if (!rowData[j].contains("rowOne")) {
								rowNew[j + 1] = rowData[j];
							}
						}
						rowNew[1] = rowNew[1].substring(1);
						listGrid.addStats(rowNew);
					} else if (row.getRowDis().contains("rowAll")) {
						String[] rowData = row.getRowDis().split("','");
						String[] rowNew = new String[rowData.length + 1];
						rowNew[0] = GlobalsTool.getMessage(request, "common.lb.totalAll");
						for (int j = 0; j < rowData.length; j++) {
							if (!rowData[j].contains("rowAll")) {
								rowNew[j + 1] = rowData[j];
							}
						}
						rowNew[1] = rowNew[1].substring(1);
						listGrid.addStatsAll(rowNew);
					}
				}
			}

		}
		//		if (hasNextMap != null) {
		//			hasNextMap.put(tableName, nextMap);
		//		}
		return listGrid.toString();
	}

	/**
	 * @param fieldName
	 *            String
	 * @param tableInfoBean
	 *            DBTableInfoBean
	 * @return boolean
	 */
	public static boolean containsField(String fieldName, DBTableInfoBean tableInfoBean) {
		for (Object o : tableInfoBean.getFieldInfos()) {
			DBFieldInfoBean field = (DBFieldInfoBean) o;
			if (fieldName.equalsIgnoreCase(field.getFieldName())) {
				return true;
			}
		}
		return false;
	}

	public Result buildSQL(ReportsBean reportSetBean, DefineReportBean defBean, String listType, String sql, HttpServletRequest request, String parentCode, int pageNo, int pageSize,
			ArrayList paramList, ArrayList<String> procParams, boolean hasConditionValue) {
		int slen = "select".length();
		ReportDataMgt mgt = new ReportDataMgt();
		String locale = GlobalsTool.getLocale(request).toString();
		Result rs = new Result();
		String isAllListQuery = request.getParameter("isAllListQuery");
		String selectType = request.getParameter("selectType");
		if (request.getParameter("query") != null && request.getParameter("query").equals("true") && selectType != null && !selectType.equals("normal")) {
			isAllListQuery = "YES";
			selectType = "endClass";
		}
		request.setAttribute("selectType", selectType);
		request.setAttribute("isAllListQuery", isAllListQuery);
		ReportField classRF = defBean.getClassCode();

		if (TEMPTABLE.equals(reportSetBean.getReportType())) {
			if (defBean.getClassCode() != null) {
				;
				procParams.add(selectType == null ? "normal" : selectType);
			}
			if (reportSetBean.getDefaultNoshow() == 0 || hasConditionValue || !"menu".equals(request.getParameter("src"))) {
				rs = mgt.execTemplateProc(reportSetBean.getProcName(), locale, request, procParams, parentCode, classRF);
				if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
					return rs;
				}
			}
			sql = sql.substring(0, sql.toLowerCase().indexOf("where 1=1") + "where 1=1".length()) + " and UserId='" + GlobalsTool.getLoginBean(request).getId() + "'"
					+ sql.substring(sql.indexOf("where 1=1") + "where 1=1".length());
		} else if (TABLELIST.equals(reportSetBean.getReportType())) {
			LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
			String sqlTemp = "";
			DBTableInfoBean tab = (DBTableInfoBean) BaseEnv.tableInfos.get(reportSetBean.getReportNumber());
			boolean flag = ReportData.containsField("createby", tab);
			boolean flagE = ReportData.containsField("employeeId", tab);
			boolean flagD = ReportData.containsField("departmentCode", tab);
			String deptFieldName = "";
			String deptEFieldName = "";
			OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(reportSetBean.getReportNumber());
			//
			if (flag && tab.getIsBaseInfo() == 0 || (template != null && template.getTemplateStatus() == 1)) {
				//�����˲��Ŵ���
				deptFieldName = "(select isnull(DepartmentCode,'') from tblEmployee where tblEmployee.id=" + reportSetBean.getReportNumber() + ".createBy)";
				//��ѯ������keyId,createBy,�����˲��Ŵ���
				sqlTemp = sql.substring(0, slen) + " " + reportSetBean.getReportNumber() + ".id as keyId," + reportSetBean.getReportNumber() + ".createBy as createBy," + deptFieldName
						+ " as deptCodeR,";
				//ȡ�����˵Ĳ��Ŵ���
				if (flagD) { //�����departmentCode�ֶεģ������˲��Ŵ���ȡdepartmentCode�ֶη�����employeeIdһ��
					deptEFieldName = reportSetBean.getReportNumber() + ".departmentCode";

				} else if (flagE) { //û�в����ֶ��о������ֶΣ�ȡ�����˲��Ŵ���
					deptEFieldName = "(select isnull(DepartmentCode,'') from tblEmployee where tblEmployee.id=" + reportSetBean.getReportNumber() + ".employeeId)";
				} else { //û�в����ֶ�Ҳû�о������ֶΣ���Ϊ��
					deptEFieldName = "null";
				}
				if (flagE) {
					sqlTemp += reportSetBean.getReportNumber() + ".employeeId as employeeId,";
				} else {
					sqlTemp += "'' as employeeId,";
				}
				sqlTemp += deptEFieldName + " as deptCodeRE,";

				sqlTemp += sql.substring(slen);
				
				
			} else {
				//�����˲��Ŵ���
				deptFieldName = "(select isnull(DepartmentCode,'') from tblEmployee where tblEmployee.id='" + lg.getId() + "')";
				deptEFieldName = "(select isnull(DepartmentCode,'') from tblEmployee as e where e.id='" + lg.getId() + "')";
				sqlTemp = sql.substring(0, slen) + " " + reportSetBean.getReportNumber() + ".id as keyId,'" + lg.getId() + "' as createBy," + deptFieldName + " as deptCodeR,";

				sqlTemp += "'" + lg.getId() + "' as employeeId," + deptEFieldName + " as deptCodeRE,";

				sqlTemp += sql.substring(slen);

				
			}
			sql = sqlTemp;
			ReportField fieldId = new ReportField();
			fieldId.setAsFieldName("keyId");
			fieldId.setFieldName(reportSetBean.getReportNumber() + ".id");
			fieldId.setFieldType(DBFieldInfoBean.FIELD_ANY);
			defBean.getFieldInfo().add(fieldId);
			defBean.getQueryFields().add(fieldId);

			ReportField fieldCreateBy = new ReportField();
			fieldCreateBy.setAsFieldName("createBy");
			fieldCreateBy.setFieldName(reportSetBean.getReportNumber() + ".createBy");
			fieldCreateBy.setFieldType(DBFieldInfoBean.FIELD_ANY);
			defBean.getFieldInfo().add(fieldCreateBy);
			defBean.getQueryFields().add(fieldCreateBy);

			ReportField fieldDepartCode = new ReportField();
			fieldDepartCode.setAsFieldName("deptCodeR");
			fieldDepartCode.setFieldName(deptFieldName);
			fieldDepartCode.setFieldType(DBFieldInfoBean.FIELD_ANY);
			defBean.getFieldInfo().add(fieldDepartCode);
			defBean.getQueryFields().add(fieldDepartCode);

			ReportField fieldEmployee = new ReportField();
			fieldEmployee.setAsFieldName("employeeId");
			fieldEmployee.setFieldName(reportSetBean.getReportNumber() + ".employeeId");
			fieldEmployee.setFieldType(DBFieldInfoBean.FIELD_ANY);
			defBean.getFieldInfo().add(fieldEmployee);
			defBean.getQueryFields().add(fieldEmployee);

			ReportField fieldDepartCodeE = new ReportField();
			fieldDepartCodeE.setAsFieldName("deptCodeRE");
			fieldDepartCodeE.setFieldName(deptEFieldName);
			fieldDepartCodeE.setFieldType(DBFieldInfoBean.FIELD_ANY);
			defBean.getFieldInfo().add(fieldDepartCodeE);
			defBean.getQueryFields().add(fieldDepartCodeE);

			Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, reportSetBean.getReportNumber());
			String sqlT = sql;

			String parentCodeList = request.getParameter("parentCode");
			if (tableInfo.getClassFlag() == 1 ) {
				sql = sql.substring(0, slen) + " " + reportSetBean.getReportNumber() + ".classCode as classCode," + reportSetBean.getReportNumber() + ".isCatalog as childCount," + sql.substring(slen);
				sql = sql.substring(0, sql.toLowerCase().indexOf("where 1=1") + "where 1=1".length());
				
				if("YES".equals(isAllListQuery)){ //��������δ��
					sql = sql + " and " + reportSetBean.getReportNumber() + ".isCatalog=0 ";
					if(parentCodeList != null && parentCodeList.length() > 0){
						sql += " and "+ reportSetBean.getReportNumber() + ".classCode like '" + parentCode + "%'";
					}
				}else { //��ǰ�����ѯ
					if(!"true".equals(GlobalsTool.getSysSetting("showparentClass"))){
						sql = sql + " and " + reportSetBean.getReportNumber() + ".isCatalog=0 "; //zxy �����б�Ҳֻ��ʾ�ӽ�㣬����ʾĿ¼
					}
					sql = sql + " and " + reportSetBean.getReportNumber() + ".classCode like '" + parentCode + "_____'";
				}

				sql = sql + sqlT.substring(sqlT.toLowerCase().indexOf("where 1=1") + "where 1=1".length());
				ReportField fieldClass = new ReportField();
				fieldClass.setAsFieldName("classCode");
				fieldClass.setFieldName("classCode");
				fieldClass.setFieldType(DBFieldInfoBean.FIELD_ANY);
				defBean.getFieldInfo().add(fieldClass);
				defBean.getQueryFields().add(fieldClass);
				ReportField childCount = new ReportField();
				childCount.setAsFieldName("childCount");
				childCount.setFieldName(reportSetBean.getReportNumber() + ".isCatalog");
				childCount.setFieldType(DBFieldInfoBean.FIELD_INT);
				defBean.getFieldInfo().add(childCount);
				defBean.getQueryFields().add(childCount);
			}
			if (classRF != null) {
				rs.retCode = ErrorCanst.DEFAULT_FAILURE;
				rs.retVal = "�����б�ָ���˷����ֶΣ���ȥ��";
				return rs;
				
			}
		}
		
		

		if (classRF != null) {
			String sqlT = sql;
			sql = sql.substring(0, sql.toLowerCase().indexOf("where 1=1") + "where 1=1".length());

			if ("YES".equals(isAllListQuery)) {
				String tableName = classRF.getFieldName().substring(0, classRF.getFieldName().indexOf("."));
				sql += " and " + tableName + ".isCatalog!=1";
				if (parentCode.length() > 0) {
					sql = sql + " and " + classRF.getFieldName() + " like '" + parentCode + "_____'";
				}
			} else {
				if (parentCode.length() > 0) {
					sql = sql + " and " + classRF.getFieldName() + " like '" + parentCode + "_____'";
				} else {
					if (!sqlT.contains((classRF.getFieldName() + " = ").toUpperCase())) {
						sql = sql + " and len(" + classRF.getFieldName() + ")=5";
					}
				}
			}

			sql = sql + sqlT.substring(sqlT.toLowerCase().indexOf("where 1=1") + "where 1=1".length());

		}

		HashMap result = new HashMap();
		String export = request.getParameter("export");
		if (!"popData".equals(listType)) {
			String orderByContent = "";
			if (sql.indexOf("order by") > -1) {
				orderByContent = sql.substring(sql.indexOf("order by"));
				sql = sql.substring(0, sql.indexOf("order by"));
			} else {
				orderByContent = "order by " + ((ReportField) defBean.getQueryFields().get(0)).getFieldName();
			}
			String fieldAS = "";// ������Ҫ��ʾ���ֶΣ��ö��Ÿ���
			DBTableInfoBean tableInfo = (DBTableInfoBean) BaseEnv.tableInfos.get(reportSetBean.getReportNumber());
			String orderAS = "";// �����õ��ֶ�

			for (int i = 0; i < defBean.getFieldInfo().size(); i++) {
				ReportField rep = (ReportField) defBean.getFieldInfo().get(i);
				if (rep.getDisplayFlag() != null && Integer.parseInt(rep.getDisplayFlag()) == 1) {
					fieldAS = fieldAS + rep.getAsFieldName() + ",";
					if ("��".equals(rep.getOrderByFlag())) {
						orderAS += rep.getAsFieldName() + " desc,";
					}
					if ("��".equals(rep.getOrderByFlag())) {
						orderAS += rep.getAsFieldName() + " asc,";
					}
				}
			}
			if (orderAS.length() > 0) {
				orderAS = " order by " + orderAS.substring(0, orderAS.length() - 1);
			}
			if (tableInfo == null || !tableInfo.getTableName().startsWith("CRM")) {
				orderAS = "";
			} else {
				orderAS += " ";
			}
			fieldAS = fieldAS.substring(0, fieldAS.length() - 1);

			if (reportSetBean.getReportType().equals(PROCLIST)) {
				StringBuffer sb = new StringBuffer();
				int size = procParams.size();
				while (size-- > 0) {
					sb.append(",@" + ((ReportField) defBean.getConFields().get(procParams.size() - size - 1)).getAsFieldName() + "=?");
				}
				sql = "{call " + reportSetBean.getProcName() + " (@pageNo=?,@pageSize=?,@locale=?,@userId=?,@sunCompany=?";
				//�ּ������Զ��ӷּ�classCode
				if (defBean.getClassCode() != null) {
					sql += ",@" + defBean.getClassCode().getAsFieldName() + "=?";
				}
				//δ�������Զ��Ӹ���classCode
				if(reportSetBean.isEndClassNumber()){
					sql += ",@parentCode=?";
				}
				sql += sb.toString() + ")}";
				defBean.setPageSizeE(99999999);
				defBean.setPageSizeS(0);
			} else {
				rs = mgt.getPageSQL(sql, pageNo, pageSize, paramList, orderByContent, fieldAS, defBean.getQueryFields(), reportSetBean.getShowTotalPage(), orderAS, defBean, request, reportSetBean
						.getCrossColNum());
				sql = rs.getRetVal().toString();
			}
			result.put("dataSql", sql);
		} else {
			result.put("dataSql", sql);
			rs.setRetVal(sql);
		}
		result.put("pageSql", sql);

		rs.setRetVal(result);
		return rs;
	}

	/**
	 * DisField�ֶΰ�order����
	 */
	public class SortDisField implements Comparator {
		public int compare(Object o1, Object o2) {
			ReportField field1 = (ReportField) o1;
			ReportField field2 = (ReportField) o2;
			if (field1.getOrder() == null || field2.getOrder() == null) {
				return 0;
			}
			int order1 = Integer.parseInt(field1.getOrder());
			int order2 = Integer.parseInt(field2.getOrder());
			if (order1 > order2) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	/**
	 * �ж����ؿ���ʾ�ֶ��Ƿ��ڱ�����������
	 * 
	 * @param fieldNames
	 * @return
	 */
	public boolean existListColConfig(String tableName, String fieldName, HttpServletRequest request) {
		boolean exist = false;
		Hashtable<String, ArrayList<ColConfigBean>> userColConfig = (Hashtable<String, ArrayList<ColConfigBean>>) request.getSession().getServletContext().getAttribute("userSettingColConfig");
		if (userColConfig != null) {
			ArrayList<ColConfigBean> configList = userColConfig.get(tableName + "list");
			if (configList != null) {
				for (ColConfigBean bean : configList) {
					if (bean.getColName().equals(fieldName)) {
						exist = true;
						break;
					}
				}
			}
		}
		return exist;
	}

	/**
	 * ת�����ֻ���������ʾ��ʽ
	 * 
	 * @return
	 */
	public Result mobileDisplay(DefineReportBean reportBean, ReportsBean reportSet, String reportType, String reportNumber, String sql, ArrayList paramList, String locale, ArrayList scopeRight,
			String userId, HashMap queryMaps, ArrayList<ColConfigBean> configList, String winCurIndex, ArrayList conditions, OAWorkFlowTemplate workFlow, String scompayId, int pageSize, int pageNo,
			String host, String statField, String layoutType, LoginBean loginBean, HttpServletRequest request) {

		ReportDataMgt mgt = new ReportDataMgt();
		Result rs = mgt.getDisplayData(reportBean, reportSet, reportType, reportNumber, sql, paramList, locale, scopeRight, userId, queryMaps, configList, winCurIndex, conditions, workFlow,
				scompayId, pageSize, pageNo, host, "page", statField, loginBean, request);
		ArrayList disField = reportBean.getDisFields();
		ArrayList cols = new ArrayList();
		ArrayList rows = new ArrayList();
		// ��ѯ����ʱ����ǽ��汨����������,�����ڲ�ѯ�����ݺ�������ҳ��Ҫ��ʾ����
		if (configList != null) {
			for (int i = 0; i < disField.size(); i++) {
				ReportField field = (ReportField) disField.get(i);

				for (int m = 0; m < configList.size(); m++) {
					ColConfigBean colBean = configList.get(m);
					if (colBean.getColName().equals(field.getAsFieldName()) || (field.getGroupName() != null && field.getGroupName().length() > 0)) {

						String fieldName = field.getFieldName();
						String fieldIdentity = field.getFieldIdentity();
						boolean flag = true;
						flag = DynDBManager.getViewRight(reportNumber, fieldName, scopeRight, fieldIdentity, loginBean);

						if (flag) {
							byte fieldType = field.getFieldType();
							String[] col = new String[6];
							col[0] = field.getDisplay();
							col[1] = field.getWidth();
							switch (fieldType) {
							case DBFieldInfoBean.FIELD_INT:
								col[2] = "int";
								break;
							case DBFieldInfoBean.FIELD_DOUBLE:
								col[2] = "float";
								break;
							case DBFieldInfoBean.FIELD_DATE_LONG:
							case DBFieldInfoBean.FIELD_DATE_SHORT:
								col[2] = "date";
								break;
							case DBFieldInfoBean.FIELD_PIC:
								col[2] = "img";
								break;
							default:
								col[2] = "string";
								break;
							}
							if (field.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
								col[2] = "other";
							}
							if (field.getLinkAdd() != null && field.getLinkAdd().length() > 0) {
								if (field.getLinkAdd().equals("@ClassCode:")) {
									col[2] = "call:nextClass";
								} else {
									col[2] = "call";
								}
							}
							col[3] = field.getGroupName();
							col[4] = field.getAsFieldName();
							col[5] = field.getLinkAdd();
							cols.add(col);
							break;
						}
					}
				}
			}
		} else {
			for (int i = 0; i < disField.size(); i++) {
				ReportField field = (ReportField) disField.get(i);
				String fieldName = field.getFieldName();
				String fieldIdentity = field.getFieldIdentity();
				boolean flag = true;
				flag = DynDBManager.getViewRight(reportNumber, fieldName, scopeRight, fieldIdentity, loginBean);

				if (flag) {
					byte fieldType = field.getFieldType();
					String[] col = new String[6];
					col[0] = field.getDisplay();
					col[1] = field.getWidth();
					switch (fieldType) {
					case DBFieldInfoBean.FIELD_INT:
						col[2] = "int";
						break;
					case DBFieldInfoBean.FIELD_DOUBLE:
						col[2] = "float";
						break;
					case DBFieldInfoBean.FIELD_DATE_LONG:
					case DBFieldInfoBean.FIELD_DATE_SHORT:
						col[2] = "date";
						break;
					case DBFieldInfoBean.FIELD_PIC:
						col[2] = "img";
						break;
					default:
						col[2] = "string";
						break;
					}
					if (field.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
						col[2] = "other";
					}
					if (field.getLinkAdd() != null && field.getLinkAdd().length() > 0) {
						if (field.getLinkAdd().equals("@ClassCode:")) {
							col[2] = "call:nextClass";
						} else {
							col[2] = "call";
						}
					}
					col[3] = field.getGroupName();
					col[4] = field.getAsFieldName();
					col[5] = field.getLinkAdd();
					cols.add(col);
				}
			}
		}
		if (rs.getRetVal() != null) {
			rows = (ArrayList) rs.getRetVal();
		}
		/* ��ʾ�������� */
		if (!"bill".equals(layoutType)) {
			// ��ʾͷ��
			MobileListData listData = new MobileListData();
			listData.setLayout(4);
			listData.setType(1);
			// ��һ�� ������id
			String[] strId = (String[]) cols.get(0);
			listData.setParameterKey("id");
			listData.setParameterKey2("reportNumber");
			listData.setValuename1(((String[]) cols.get(1))[0]);
			if (cols.size() > 2) {
				listData.setValuename2(((String[]) cols.get(2))[0]);
			}
			if (cols.size() > 3) {
				listData.setValuename3(((String[]) cols.get(3))[0]);
			}
			// ��ʾ����
			List<Properties> datas = new ArrayList<Properties>();
			for (int i = 0; i < rows.size(); i++) {
				Object[] row = (Object[]) rows.get(i);
				String rowDetail = row[0].toString();
				rowDetail = rowDetail.substring(1, rowDetail.length() - 1);
				String[] arrayRow = rowDetail.split("','");
				Properties data = new Properties();
				if (strId[5] != null && strId[5].length() > 0) {
					data.setProperty("id", arrayRow[0].split(";")[0]);
				}
				data.setProperty(listData.getValuename1(), arrayRow[1]);
				String valueName2 = listData.getValuename2();
				String valueName3 = listData.getValuename3();
				if (valueName2 != null && valueName2.length() > 0 && arrayRow.length > 2) {
					data.setProperty(valueName2, arrayRow[2]);
				}
				if (valueName3 != null && valueName3.length() > 0 && arrayRow.length > 3) {
					data.setProperty(valueName3, arrayRow[3]);
				}
				data.setProperty("target", "doDetailsview");
				data.setProperty("reportNumber", strId[5]);
				datas.add(data);
			}
			listData.setData(datas);
			rs.setRetVal(listData);
		} else {
			/* ����������Ϣ */
			List<MobileAttrs> billList = new ArrayList<MobileAttrs>();
			if (rows.size() > 1) {
				/* �жϴ����п�ʼ�� �ӱ����� */
				Object[] row1 = (Object[]) rows.get(0);
				String rowDetail1 = row1[0].toString();
				rowDetail1 = rowDetail1.substring(1, rowDetail1.length() - 1);
				String[] arrayRow1 = rowDetail1.split("','");

				Object[] row2 = (Object[]) rows.get(1);
				String rowDetail2 = row2[0].toString();
				rowDetail2 = rowDetail2.substring(1, rowDetail2.length() - 1);
				String[] arrayRow2 = rowDetail2.split("','");
				int number = 0;
				/* �õ���������� */
				for (int i = 0; i < arrayRow1.length; i++) {
					if (arrayRow1[i].equals(arrayRow2[i])) {
						MobileAttrs keyValue = new MobileAttrs();
						String[] col = (String[]) cols.get(i);
						keyValue.setKey(col[0] + "|txt");
						keyValue.setValue(arrayRow2[i]);
						billList.add(keyValue);
					} else {
						number = i;
						break;
					}
				}
				/* �õ��ӱ������ */
				for (int i = 0; i < rows.size(); i++) {
					Object[] row = (Object[]) rows.get(i);
					String rowDetail = row[0].toString();
					rowDetail = rowDetail.substring(1, rowDetail.length() - 1);
					String[] arrayRow = rowDetail.split("','");
					for (int j = number; j < cols.size(); j++) {
						String[] col = (String[]) cols.get(j);
						MobileAttrs keyValue2 = new MobileAttrs();
						keyValue2.setKey(col[0] + (i > 1 ? i : "") + "|txt");
						if (j >= arrayRow.length) {
							/* ������Ϊ�յ�ʱ�� split���صĽ������һ�� */
							keyValue2.setValue("");
						} else {
							keyValue2.setValue(arrayRow[j]);
						}
						billList.add(keyValue2);
					}
				}
			} else {
				Object[] row = (Object[]) rows.get(0);
				String rowDetail = row[0].toString();
				rowDetail = rowDetail.substring(1, rowDetail.length() - 1);
				String[] arrayRow = rowDetail.split("','");
				for (int i = 0; i < cols.size(); i++) {
					String[] col = (String[]) cols.get(i);
					MobileAttrs keyValue = new MobileAttrs();
					keyValue.setKey(col[0] + "|txt");
					if (i >= arrayRow.length) {
						/* ������Ϊ�յ�ʱ�� split���صĽ������һ�� */
						keyValue.setValue("");
					} else {
						keyValue.setValue(arrayRow[i]);
					}
					billList.add(keyValue);
				}
				rs.setRetVal(billList);
			}
			MobileDetail detail = new MobileDetail();
			detail.setData(billList);
			detail.setType(2);
			rs.setRetVal(detail);
		}
		return rs;
	}

	private static int rowCount = 1;

	/**
	 * ����ȫ������
	 * 
	 * @return
	 */
	public int exportAllData(DefineReportBean reportBean, ReportsBean reportSet, String reportType, String reportNumber, String sql, ArrayList paramList, String locale, ArrayList scopeRight,
			String userId, HashMap queryMaps, ArrayList<ColConfigBean> configList, String winCurIndex, ArrayList conditions, OAWorkFlowTemplate workFlow, String scompayId, int pageSize, int pageNo,
			String host, String filePath, int num, String statField, LoginBean loginBean, HttpServletRequest request) {

		ReportDataMgt mgt = new ReportDataMgt();
		ArrayList<ReportField> disField = reportBean.getDisFields();

		Result rs = mgt.getDisplayData(reportBean, reportSet, reportType, reportNumber, sql, paramList, locale, scopeRight, userId, queryMaps, configList, winCurIndex, conditions, workFlow,
				scompayId, pageSize, pageNo, host, "export", statField, loginBean, request);

		ArrayList cols = new ArrayList();
		ArrayList rows = new ArrayList();
		// ��ѯ����ʱ����ǽ��汨����������,�����ڲ�ѯ�����ݺ�������ҳ��Ҫ��ʾ����
		for (int i = 0; i < disField.size(); i++) {
			ReportField field = (ReportField) disField.get(i);
			if (field.getWidth().equals("-1") || field.getDisplay().equals("����"))//���ص��в���ʾ
				continue;

			String fieldName = field.getFieldName();
			String fieldIdentity = field.getFieldIdentity();
			boolean flag = true;
			flag = DynDBManager.getViewRight(reportNumber, fieldName, scopeRight, fieldIdentity, loginBean);
			if (field.getFieldType() == DBFieldInfoBean.FIELD_PIC) {
				//����ʱ����ͼƬ
				flag = false;
			}
			if (flag) {
				byte fieldType = field.getFieldType();
				String[] col = new String[5];
				col[0] = field.getDisplay();
				col[1] = field.getWidth();
				switch (fieldType) {
				case DBFieldInfoBean.FIELD_INT:
					col[2] = "int";
					break;
				case DBFieldInfoBean.FIELD_DOUBLE:
					col[2] = "float";
					break;
				case DBFieldInfoBean.FIELD_DATE_LONG:
				case DBFieldInfoBean.FIELD_DATE_SHORT:
					col[2] = "date";
					break;
				case DBFieldInfoBean.FIELD_PIC:
					col[2] = "img";
					break;
				default:
					col[2] = "string";
					break;
				}
				if (field.getInputType() == DBFieldInfoBean.INPUT_ENUMERATE) {
					col[2] = "other";
				}
				if (field.getLinkAdd() != null && field.getLinkAdd().length() > 0) {
					if (field.getLinkAdd().equals("@ClassCode:")) {
						col[2] = "call:nextClass";
					} else {
						col[2] = "call";
					}
				}
				col[3] = field.getGroupName();
				col[4] = field.getAsFieldName();
				cols.add(col);
			}
		}

		if (rs.getRetVal() != null) {
			rows = (ArrayList) rs.getRetVal();
		}
		try {
			WritableCellFormat wf = new WritableCellFormat();
			wf.setBorder(Border.ALL, BorderLineStyle.THIN);
			wf.setAlignment(Alignment.LEFT);
			WritableCellFormat wfnum = new WritableCellFormat();
			wfnum.setBorder(Border.ALL, BorderLineStyle.THIN);
			wfnum.setAlignment(Alignment.RIGHT);
			WritableCellFormat wftitle = new WritableCellFormat();
			wftitle.setBorder(Border.ALL, BorderLineStyle.THIN);
			wftitle.setAlignment(Alignment.CENTRE);

			BaseEnv.log.debug("���ڵ����ļ�" + filePath + "//" + reportSet.getReportName()+ "-"+loginBean.getEmpFullName() + "-" + num + ".xls" + "  :����=" + rows.size());
			WritableWorkbook wbook = Workbook.createWorkbook(new File(filePath + "//" + reportSet.getReportName()+ "-"+loginBean.getEmpFullName() + "-" + num + ".xls"));
			rowCount = createExcelTitle(wbook, reportSet.getReportName(), cols, wftitle);
			ArrayList<Integer> hideColumn = new ArrayList<Integer>();
			for (int i = 0; i < disField.size(); i++) {
				if (disField.get(i).getWidth().equals("-1") || disField.get(i).getDisplay().equals("����"))
					hideColumn.add(i);
			}
			for (int j = 0; j < rows.size()&& j<pageSize; j++) {

				if(j==199){
					System.out.println("0000");
				}
				WritableSheet ws = wbook.getSheet(0);
				String rowDetail;
				if (TABLELIST.equals(reportType)) {
					TableListResult row = (TableListResult) rows.get(j);
					rowDetail = row.getRowDis().toString();
				} else {
					Object[] row = (Object[]) rows.get(j);
					rowDetail = row[0].toString();
				}
				rowDetail = GlobalsTool.revertTextCode2(rowDetail);
				rowDetail = rowDetail.substring(1, rowDetail.length() - 1);
				String[] arrayRow = rowDetail.split("','");
				int colNum = 0;
				for (int m = 0; m < arrayRow.length; m++) {
					if (hideColumn.contains(m))//���ص��в���ʾ
						continue;
					String strs = arrayRow[m];
					if (arrayRow[m].contains("#;#")) {
						strs = arrayRow[m].split("#;#")[0];
					}
					if (GlobalsTool.isDouble(strs)) {
						try {
							strs = strs.replaceAll(",", "");
							int length = strs.length();
							// strs�����0��ͷ����ʾ���ַ�
							if ((strs.startsWith("0") && !strs.startsWith("0.")) || (strs.indexOf(".") == -1 && strs.length() > 11)) {
								Label cell = new Label(colNum, rowCount, strs, wf);
								ws.addCell(cell);
							} else {
								BigDecimal doubleValue = new BigDecimal(Double.parseDouble(strs));
								Number cell = new Number(colNum, rowCount, doubleValue.doubleValue(), wfnum);
								ws.addCell(cell);
							}
						} catch (Exception e) {
							
							Label cell = new Label(colNum, rowCount, strs, wf);
							ws.addCell(cell);
						}
					} else {
						strs = strs==null?"":GlobalsTool.rereplaceSpecLitter(strs).replaceAll("\\\\'", "'");
						//strs = strs == null ? "" : strs.replaceAll("\\|=", "");						
						Label cell = new Label(colNum, rowCount, strs, wf);
						ws.addCell(cell);
					}
					colNum++;
				}
				rowCount++;

			}
			if (wbook != null) {
				wbook.write();
				wbook.close();
				wbook = null;
			}
		} catch (Exception e) {
			BaseEnv.log.error("ReportData exportAllData method", e);
			return 0;
		}
		return rows.size();
	}

	/**
	 * ����ѹ���ļ�
	 */
	public class CompressFilter implements FileFilter {

		private String compressName;

		public CompressFilter(String compressName) {
			this.compressName = compressName;
		}

		public boolean accept(File file) {
			if (file.getName().endsWith(".xls") && file.getName().startsWith(compressName)) {
				return true;
			}
			return false;
		}

	}

	/** ѹ��һ���ļ� */
	private void compressFile(File file, ZipOutputStream out) {
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			out.setEncoding("GBK");
			out.putNextEntry(new ZipEntry(file.getName()));
			int count;
			byte data[] = new byte[8192];
			while ((count = bis.read(data, 0, 8192)) != -1) {
				out.write(data, 0, count);
			}
			bis.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ����Excel��ͷ
	 * 
	 * @return
	 */
	public int createExcelTitle(WritableWorkbook wbook, String reportName, List cols, WritableCellFormat wf) {
		int row = 1;
		try {

			WritableSheet ws = wbook.createSheet(reportName, 0);
			String groupName = "";
			for (int i = 0; i < cols.size(); i++) {
				String[] col = (String[]) cols.get(i);
				if (col[3] != null && col[3].length() > 0 && groupName.indexOf(col[3] + ",") == -1) {
					groupName = col[3] + ",";

					int number = 0;
					for (int j = 0; j < cols.size(); j++) {
						String[] colj = (String[]) cols.get(j);
						if (colj[3] != null && col[3].equals(colj[3])) {
							number++;
						}
					}
					if (number > 1) {
						Label cell = new Label(i, 0, col[3], wf);
						ws.addCell(cell);
						ws.mergeCells(i, 0, (i + number - 1), 0);
						row = 2;
					}
				}
			}
			if (row == 2) { //��������У���Ҫ����		
				for (int i = 0; i < cols.size(); i++) {
					String[] col = (String[]) cols.get(i);
					if (col[3] == null || col[3].length() == 0) {
						Label cell = new Label(i, 0, "", wf);
						ws.addCell(cell);
					}
				}
			}

			/* д��ͷ */
			int c = 0;
			for (int i = 0; i < cols.size(); i++) {
				String[] col = (String[]) cols.get(i);

				Label cell = new Label(i, (row == 1 ? 0 : 1), col[0], wf);
				ws.addCell(cell);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return row;
	}

	/**
	 * ��ѯĳ������ص���
	 * 
	 * @param fieldNames
	 * @return
	 */
	public ArrayList queryHiddenConfig(ArrayList disField, String tableName, HttpServletRequest request) {

		ArrayList<ReportField> hiddenList = new ArrayList<ReportField>();
		Hashtable<String, ArrayList<ColConfigBean>> userColConfig = (Hashtable<String, ArrayList<ColConfigBean>>) request.getSession().getServletContext().getAttribute("userSettingColConfig");
		ArrayList<ColConfigBean> configList = new ArrayList<ColConfigBean>();
		if (userColConfig != null) {
			configList = userColConfig.get(tableName + "list");
		}
		for (int i = 0; i < disField.size(); i++) {
			ReportField field = (ReportField) disField.get(i);
			if (!field.getWidth().equals("0") && !field.getWidth().equals("") && field.getCrossField() != 1) {
				if (configList != null && configList.size() > 0) {
					if (!GlobalsTool.existListColConfig(tableName, field.getAsFieldName(), request)) {
						hiddenList.add(field);
					}
				} else {
					// ���ؿ���ʾ
					if (field.getInputType() == 6) {
						hiddenList.add(field);
					}
				}
			}
		}
		return hiddenList;
	}

	/**
	 * ��request��ȡ��ֵ�����ֵ���������map��ȡֵ��
	 * 
	 * @param request
	 * @param key
	 * @param map
	 * @return
	 */
	private String getCondition(HttpServletRequest request, String key, HashMap map) {
		String value = request.getParameter(key);
		Object sysAlert = request.getAttribute("sysAlert");
		if (sysAlert != null && "true".equals(sysAlert.toString())) {
			HashMap parameterMap = (HashMap) request.getAttribute("parameterMap");
			Object o = parameterMap.get(key);
			value = o == null ? null : String.valueOf(o);
		}
		if (value == null && map != null) {
			Object o = map.get(key);
			if (o == null) {
				value = null;
			} else {
				value = String.valueOf(o);
			}
		}
		return value;
	}

	/**
	 * ��request��ȡ��ֵ������ֵ�Ƿ���Ҫ����ת���жϣ������ֵ���������map��ȡֵ��
	 * 
	 * @param request
	 * @param key
	 * @param map
	 * @return
	 */
	private String getCondition2(HttpServletRequest request, String key, HashMap map) {
		String linkType = request.getParameter("LinkType");
		if (linkType != null)
			linkType = linkType.trim();
		String value = request.getParameter(key);
		Object sysAlert = request.getAttribute("sysAlert");
		if (sysAlert != null && "true".equals(sysAlert.toString())) {
			HashMap parameterMap = (HashMap) request.getAttribute("parameterMap");
			Object o = parameterMap.get(key);
			value = o == null ? null : String.valueOf(o);
		}
		if (linkType != null && (linkType.equals("@URL") || linkType.equals("@URL:")) && value != null) {
			try {
				value = new String(value.getBytes("iso-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		}
		if (value == null && map != null) {
			Object o = map.get(key);
			if (o == null) {
				value = null;
			} else {
				value = String.valueOf(o);
			}
		}
		return value;
	}

	private String replaceClassCode(ReportField field, String values) {
		return replaceClassCode(field, values, field.getFieldName());
	}

	/**
	 * ��������iscatalog�ֶε����������������滻
	 * 
	 * @param field
	 *            �����ֶ�
	 * @param values
	 *            Ҫ�����滻��ֵ
	 * @return �������null��ʾδ���滻������Ϊ�滻����ַ���
	 */
	private String replaceClassCode(ReportField field, String values, String fieldStr) {
		if(values==null){
			return null;
		} 
		boolean willReplace = true;
		PopupSelectBean bean = (PopupSelectBean) BaseEnv.popupSelectMap.get(field.getPopUpName());
		int classCodePos = -1;
		if (bean != null && bean.getReturnFields() != null) {
			for (int j = 0; j < bean.getReturnFields().size(); j++) {
				Object o = bean.getReturnFields().get(j);
				PopField tmp_f = (PopField) o;

				if (tmp_f.fieldName.toLowerCase().indexOf("classcode") != -1) {// �ж�classcode��λ��
					{
						classCodePos = j;
						break;
					}
				}
			}
			for (int j = 0; j < bean.getReturnFields().size(); j++) {
				Object o = bean.getReturnFields().get(j);
				PopField tmp_f = (PopField) o;
				if (tmp_f.fieldName.toLowerCase().indexOf("iscatalog") != -1) {
					if (classCodePos != -1)// ����Ѿ��ҵ���classcode�ڵ������з����ֶ��е�λ��
					{
						willReplace = false;
						StringBuffer tmpSql = new StringBuffer();// �������������滻�õ�SQL���
						if (values == null || values.length() == 0)// ���û������,��ֱ���滻��һ����ֵ�����
						{
							tmpSql.append(" 1=1 ");
						} else {
							String[] s = values.split("#\\|#");
							ArrayList<String> categoryValue = new ArrayList<String>();
							ArrayList<String> notCategoryValue = new ArrayList<String>();
							for (String string : s) {
								String[] itemValue = string.split("#;#");
								if (itemValue[j].trim().equals("1"))// ��������ӽڵ�
									categoryValue.add(itemValue[classCodePos]);
								else
									notCategoryValue.add(itemValue[classCodePos]);
							}
							for (String string : categoryValue) {
								tmpSql.append(fieldStr + "  like '" + string + "%' or ");// likeǰ���������ո񣬲���ɾ��,ɾ�˺����Ĺ����Ѵ˾��滻��
							}
							if (notCategoryValue.size() > 0) {
								tmpSql.append(fieldStr + " in (");
								for (String string : notCategoryValue) {
									tmpSql.append("'" + string + "',");
								}
								tmpSql = tmpSql.deleteCharAt(tmpSql.length() - 1);
								tmpSql.append(")");
							} else {
								tmpSql.delete(tmpSql.length() - 4, tmpSql.length() - 1);
							}

						}
						return tmpSql.toString();
					}
					break;
				}
			}
		}
		return null;
	}

	/**
	 * ��������iscatalog�ֶε����������������滻
	 * 
	 * @param field
	 *            �����ֶ�
	 * @param values
	 *            Ҫ�����滻��ֵ
	 * @return �������null��ʾδ���滻������Ϊ�滻����ַ���
	 */
	private String replaceClassCode(String popupName, String values) {
		int classCodePos = -1;
		PopupSelectBean bean = (PopupSelectBean) BaseEnv.popupSelectMap.get(popupName);
		if (bean != null && bean.getReturnFields() != null) {
			for (int j = 0; j < bean.getReturnFields().size(); j++) {
				Object o = bean.getReturnFields().get(j);
				PopField tmp_f = (PopField) o;

				if (tmp_f.fieldName.toLowerCase().indexOf("classcode") != -1) {// �ж�classcode��λ��
					{
						classCodePos = j;
						break;
					}
				}
			}
			for (int j = 0; j < bean.getReturnFields().size(); j++) {
				Object o = bean.getReturnFields().get(j);
				PopField tmp_f = (PopField) o;
				if (tmp_f.fieldName.toLowerCase().indexOf("iscatalog") != -1) {
					if (classCodePos != -1)// ����Ѿ��ҵ���classcode�ڵ������з����ֶ��е�λ��
					{
						StringBuffer tmpSql = new StringBuffer();// �������������滻�õ�SQL���
						if (values == null || values.length() == 0)// ���û������,��ֱ���滻��һ����ֵ�����
						{
							tmpSql.append("");
						} else {
							String[] s = values.split("#\\|#");
							ArrayList<String> categoryValue = new ArrayList<String>();
							ArrayList<String> notCategoryValue = new ArrayList<String>();
							for (String string : s) {
								String[] itemValue = string.split("#;#");
								//���Ǵ洢���̴���classCode��isCatalog,�����ܳ���������ţ�Ϊ��֤�����ܼ��ٴ洢���̸��ˣ����ﲻ�޸ķָ���
								tmpSql.append(itemValue[classCodePos]).append(";").append(itemValue[j]).append("|");
							}
							if (tmpSql.length() > 1)
								tmpSql.deleteCharAt(tmpSql.length() - 1);
						}
						return tmpSql.toString();
					}
					break;
				}
			}
		}
		return null;
	}
}
