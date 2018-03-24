package com.menyi.web.util;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.w3c.dom.*;

import com.menyi.aio.web.colconfig.ColConfigMgt;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.userFunction.PropMgt;
import com.menyi.aio.bean.ColConfigBean;
import com.menyi.aio.bean.ColDisplayBean;
import com.menyi.aio.bean.DefineChartBean;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.bean.EnumerateItemBean;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;

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
public class DefineReportBean {
	private String reportId;

	private String reportName;
	private String reportNumber;

	private ArrayList<ReportField> fieldInfo = new ArrayList<ReportField>();

	private String from;

	private String sql;

	private ReportField classCode;

	private ArrayList<ReportField> disFields = new ArrayList<ReportField>();//�û��Զ���������ʾ�ֶ�

	private ArrayList<ReportField> disFields2 = new ArrayList<ReportField>();//������ʾ�ֶ�

	private ArrayList<ReportField> conFields = new ArrayList<ReportField>();//�����ֶ�

	private ArrayList<ReportField> queryFields = new ArrayList<ReportField>();//��ѯ�ֶ�

	private ArrayList<ReportField> statFields = new ArrayList<ReportField>();//ͳ�Ʒ����ֶ�

	private boolean haveStat = false;

	private boolean isCross = false;
	/**
	 * �����У�����ֻ֧��һ�������У�������汨����
	 */
	private ReportField crossField;

	private ArrayList chartList = new ArrayList();

	private ArrayList fusionList = new ArrayList();

	//�������ֶ����ڽ��汨���¼��ǰҳ�Ŀ�ʼ��������������
	private int pageSizeS = 0;

	private int pageSizeE = 0;
	public ArrayList<String[]> userConfig = new ArrayList<String[]>(); //��¼�û�����������������Ϣ��
	
	
	

	/**
	 * �汾�ţ�ȱʡΪ1���������ļ�����versionʱ���������°汾�ĵ�����
	 * �汾�Ŵ���1ʱ�������������棬����ݵ�����parentdisplay���ԣ����������г������Ŀ
	 */
	//    private int version = 1;
	public static DefineReportBean parse(String fileName, String locale,String userId) throws Exception {

		
		DefineReportBean bean = new DefineReportBean();

		Hashtable tables =BaseEnv.tableInfos;
		//��ȡ�û��Զ���������Ϣ
		final Hashtable<String, ColDisplayBean> userLanguage =BaseEnv.userLanguage;
		final Hashtable<String, ArrayList<ColConfigBean>> userConfigBean =BaseEnv.colConfig;
		String tableName = fileName.replace("SQL.xml", "");
		bean.reportNumber = tableName;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		File file = new File(BaseEnv.USERREPORTPATH + "\\" + fileName);
		if (file == null || file.isFile() == false) {
			file = new File(BaseEnv.REPORTPATH + "\\" + fileName);
		}
		
		
		//��ѯ�Ƿ��û��и��Զ���
		Result rs = new ColConfigMgt().getUserConfig(tableName,userId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null && ((ArrayList)(rs.retVal)).size() > 0){
			bean.userConfig = (ArrayList<String[]>)(rs.retVal);
		}
		
		
		Document document = dbf.newDocumentBuilder().parse(file);
		Node config = document.getFirstChild();
		NodeList nodeList = config.getChildNodes();
		
		
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node temp = nodeList.item(i);
			if (temp.getNodeName().equalsIgnoreCase("Report")) {
				NamedNodeMap nns = temp.getAttributes();
				bean.reportId = nns.getNamedItem("reporteId").getNodeValue();
				bean.reportName = nns.getNamedItem("reportName").getNodeValue();
				//				if (nns.getNamedItem("version") != null) {
				//					String tempVersion = nns.getNamedItem("version").getNodeValue();
				//
				//					if (tempVersion != null && !tempVersion.equals("")) {
				//						try {
				//							bean.version = Integer.parseInt(tempVersion);
				//						} catch (Exception e) {
				//							e.printStackTrace();
				//						}
				//					}
				//				}
			} else if (temp.getNodeName().equalsIgnoreCase("fieldInfo")) {
				NodeList fieldList = temp.getChildNodes();
				for (int j = 0; j < fieldList.getLength(); j++) {
					Node fieldNode = fieldList.item(j);
					if (fieldNode.getNodeName().equalsIgnoreCase("field")) {
						NamedNodeMap fieldAtt = fieldNode.getAttributes();

						ReportField field = new ReportField();
						field.setFieldName(fieldAtt.getNamedItem("fieldname").getNodeValue().trim());
						field.setAsFieldName(fieldAtt.getNamedItem("AsfieldName").getNodeValue().trim());
						field.setCondition(fieldAtt.getNamedItem("condition").getNodeValue().trim());
						field.setConditionJoin(fieldAtt.getNamedItem("conditionJoin").getNodeValue().trim());
						field.setDisplayFlag(fieldAtt.getNamedItem("displayFlag").getNodeValue().trim());
						String filedType = fieldAtt.getNamedItem("fieldType").getNodeValue().trim();
						if (filedType.length() > 0)
							field.setFieldType(Byte.parseByte(filedType));
						else
							field.setFieldType(Byte.parseByte("2"));
						if (fieldAtt.getNamedItem("repeatNotShow") != null) {
							String repeatNotShow = fieldAtt.getNamedItem("repeatNotShow").getNodeValue();
							if (repeatNotShow != null && repeatNotShow.length() > 0)
								field.setRepeatNotShow(Byte.parseByte(repeatNotShow.trim()));
							else
								field.setRepeatNotShow(Byte.parseByte("0"));
						}
						field.setGroupByFlag(fieldAtt.getNamedItem("groupByFlag").getNodeValue().trim());
						String inputType = fieldAtt.getNamedItem("inputType").getNodeValue().trim();
						if (inputType.length() > 0)
							field.setInputType(Byte.parseByte(inputType));
						else
							field.setInputType(Byte.parseByte("0"));
						field.setOrderByFlag(fieldAtt.getNamedItem("orderbyFlag").getNodeValue().trim());
						field.setPopUpName(fieldAtt.getNamedItem("popUpName").getNodeValue().trim());
						field.setOrder(fieldAtt.getNamedItem("order").getNodeValue().trim());
						if (userLanguage != null && userLanguage.size() > 0) {
							ColDisplayBean colBean = userLanguage.get(tableName + field.getAsFieldName());
							if (colBean != null && colBean.getColWidth() != null && colBean.getColWidth().length() > 0) {
								field.setWidth(colBean.getColWidth());
							} else {
								field.setWidth(fieldAtt.getNamedItem("width").getNodeValue().trim());
							}
						} else {
							field.setWidth(fieldAtt.getNamedItem("width").getNodeValue().trim());
						}

						field.setClassCode(fieldAtt.getNamedItem("classCode").getNodeValue().trim());
						field.setLinkAdd(fieldAtt.getNamedItem("linkAdd").getNodeValue().trim());
						if (fieldAtt.getNamedItem("isStat") != null) {
							field.setIsStat(fieldAtt.getNamedItem("isStat").getNodeValue().trim());
							if (field.getIsStat().equals("1")) {
								bean.haveStat = true;
							}
						} else {
							field.setIsStat("0");
						}
						if (fieldAtt.getNamedItem("fieldSysType") != null) {
							field.setFieldSysType(fieldAtt.getNamedItem("fieldSysType").getNodeValue().trim());
						} else {
							field.setFieldSysType("");
						}
						if (fieldAtt.getNamedItem("fieldIdentity") != null) {
							field.setFieldIdentity(fieldAtt.getNamedItem("fieldIdentity").getNodeValue().trim());
						} else {
							field.setFieldIdentity("");
						}
						if (fieldAtt.getNamedItem("fixColName") != null) {
							field.setFixColName(fieldAtt.getNamedItem("fixColName").getNodeValue().trim());
						}
						if (fieldAtt.getNamedItem("isNull") != null) {
							field.setIsNull(fieldAtt.getNamedItem("isNull").getNodeValue().trim());
						}
						if (fieldAtt.getNamedItem("isMobile") != null) {
							field.setIsMobile(Byte.parseByte(fieldAtt.getNamedItem("isMobile").getNodeValue().trim()));
						}
						if (fieldAtt.getNamedItem("planarField") != null) {
							field.setPalnarField(fieldAtt.getNamedItem("planarField").getNodeValue().trim());
						}
						if (fieldAtt.getNamedItem("subSQL") != null) {
							field.setSubSQL(fieldAtt.getNamedItem("subSQL").getNodeValue().trim());
						}
						if (fieldAtt.getNamedItem("crossField") != null && fieldAtt.getNamedItem("crossField").getNodeValue().trim().length() > 0) {
							field.setCrossField(Byte.parseByte(fieldAtt.getNamedItem("crossField").getNodeValue()));
							if (field.getCrossField() == 1) {
								bean.setCross(true);
								bean.crossField = field;
							}
						}
						if (fieldAtt.getNamedItem("zeroDisplay") != null) {
							field.setZeroDisplay(Byte.parseByte(fieldAtt.getNamedItem("zeroDisplay").getNodeValue()));
						}
						if (fieldAtt.getNamedItem("groupName") != null) {
							field.setGroupName(fieldAtt.getNamedItem("groupName").getNodeValue());
						} else {
							field.setGroupName("");
						}
						if (fieldAtt.getNamedItem("analysis") != null) {
							field.setAnalysis(fieldAtt.getNamedItem("analysis").getNodeValue());
						}
						//ƥ������ֶ����ֺ���Ʒ���Ե�����һ����������NV��colorName,colorNameNV�ֶΣ����Զ���Ϊ����һ�������ֶ�
						//����������������������Сд���淶���������
						if (BaseEnv.propIgnoreCaseMap.get(field.getAsFieldName().toLowerCase()) != null) {
							field.setFieldSysType("GoodsField");
						} else if (BaseEnv.propIgnoreCaseMap.get(field.getAsFieldName().toLowerCase() + "NV") != null) {
							field.setFieldSysType("GoodsField");
						} else if (field.getAsFieldName().equalsIgnoreCase("colorName") || field.getAsFieldName().equalsIgnoreCase("colorNameNV")) {
							//����ֶ�����colorName��colorNameNV�����Զ���Ϊ����һ�������ֶ�
							field.setFieldSysType("GoodsField");
						}

						NodeList list = fieldNode.getChildNodes();
						//�����ֶε���ʾ����
						for (int k = 0; k < list.getLength(); k++) {
							Node tempNode = list.item(k);
							if (tempNode.getNodeName().equalsIgnoreCase("Displays")) {
								NodeList disNodes = tempNode.getChildNodes();
								for (int l = 0; l < disNodes.getLength(); l++) {
									Node disNode = disNodes.item(l);
									if (disNode.getNodeName().equalsIgnoreCase("Display")) {
										NamedNodeMap disAtt = disNode.getAttributes();
										String local = disAtt.getNamedItem("localStr").getNodeValue().trim();
										if (local.equals(locale)) {
											if ("0".equals(field.getFixColName())) {
												String display = "";
												if ((field.getFieldSysType().equals("GoodsField") || field.getFieldSysType().equals("TwoUnit")) && field.getAsFieldName().indexOf("colorName") == -1) {
													String fieldName = field.getFieldName();
													if (fieldName.indexOf(".") != -1) {
														DBFieldInfoBean dbField = GlobalsTool.getFieldBean(fieldName.substring(0, fieldName.indexOf(".")), fieldName
																.substring(fieldName.indexOf(".") + 1));
														if (dbField != null) {
															display = (dbField.getDisplay() == null || dbField.getDisplay().get(local) == null) ? disAtt.getNamedItem("display").getNodeValue().trim()
																	: dbField.getDisplay().get(local).toString();
														} else {
															display = disAtt.getNamedItem("display").getNodeValue().trim();
														}
													} else {
														display = disAtt.getNamedItem("display").getNodeValue().trim();
													}
												} else {
													display = disAtt.getNamedItem("display").getNodeValue().trim();
												}
												if (field.getFieldName().contains(".")) {
													String[] fieldName = field.getFieldName().split("\\.");
													DBFieldInfoBean fieldBean = DDLOperation.getFieldInfo(tables, fieldName[0], fieldName[1]);
													if (fieldBean == null) {
														field.setDisplay(display);
													} else {
														field.setDisplay(fieldBean.getDisplay() == null || fieldBean.getDisplay().equals("")? display : fieldBean.getDisplay().get(locale));
													}
												} else {
													field.setDisplay(display);
												}
											} else {
												field.setDisplay(disAtt.getNamedItem("display").getNodeValue().trim());
											}
										}
									}
								}
							} else if (tempNode.getNodeName().equalsIgnoreCase("groupNames")) {
								NodeList disNodes = tempNode.getChildNodes();
								for (int l = 0; l < disNodes.getLength(); l++) {
									Node disNode = disNodes.item(l);
									if (disNode.getNodeName().equalsIgnoreCase("Display")) {
										NamedNodeMap disAtt = disNode.getAttributes();
										String local = disAtt.getNamedItem("localStr").getNodeValue().trim();
										String display = disAtt.getNamedItem("display").getNodeValue().trim();

										if (local.equals(locale)) {
											field.setGroupName(display);
											break;
										}
									}
								}
							}
						}
						if (fieldAtt.getNamedItem("defaultValue") != null)
							field.setDefaultValue(fieldAtt.getNamedItem("defaultValue").getNodeValue().trim());
						//������ʾ�ֶ�
						if (field.getFieldName().indexOf("[") > 0 && field.getFieldName().indexOf("]") > 0 && field.getFieldName().indexOf("@")==-1) {
							String preFN = field.getFieldName().substring(0, field.getFieldName().indexOf("["));
							String showSet = field.getFieldName().substring(field.getFieldName().indexOf("[") + 1, field.getFieldName().indexOf("]"));
							if ("GOODSPROP".equals(showSet)) {
								//��ʾ����Ʒ����
								for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
									if (gpBean != null && !gpBean.getPropName().toLowerCase().equals("seq")) {
										if(gpBean.getPropName().toLowerCase().equals("color") && (BaseEnv.version==3||BaseEnv.version==11) ){
											//��ƥ�Ļ�Ҫ��ʾ��ɫ���
											ReportField rf = field.clone();
											rf.setFieldName(preFN + gpBean.getPropName());
											rf.setAsFieldName(gpBean.getPropName());
											//�����Ƿ�Ҫ�������������õ�					
											//ȡtblStockDet���color�ֶ���
											String dis= GlobalsTool.getFieldDisplay(BaseEnv.tableInfos, "tblStockDet.color", locale);
											rf.setDisplay( dis);
											rf.setFieldType((byte)2);
											rf.setInputType((byte)2);
											rf.setPopUpName(gpBean.getAlertName());								
											rf.setWidth("90");
											rf.setFieldSysType("GoodsField");
											rf.setFixColName("0");
											setBean(bean, rf, userConfigBean, tableName);
										}
										
										//���������õ�
										ReportField rf = field.clone();
										rf.setFieldName(preFN + gpBean.getPropName());
										rf.setAsFieldName(gpBean.getPropName());
										//�����Ƿ�Ҫ�������������õ�
//										rf.setCondition("=?");
//										rf.setConditionJoin("and");										
										rf.setDisplay(gpBean.getDisplay().get(locale));
										//�ֶ�����Ϊ����
										if(gpBean.getPropName().equals("ProDate")||gpBean.getPropName().equals("Availably")){
											rf.setFieldType((byte)5);
										}else{
											rf.setFieldType((byte)2);
										}
										rf.setInputType((byte)0); //��������
										if(gpBean.getInputBill()==2){
											//������ѡ��
											rf.setInputType((byte)2);
											rf.setPopUpName(gpBean.getAlertName());
											//V7��ֱ�Ӵ�����
											if(BaseEnv.version !=12){
												//��ƥ���ù�����ɫ��
												if(gpBean.getPropName().toLowerCase().equals("color") && (BaseEnv.version==3||BaseEnv.version==11) ){
													rf.setCondition("");
													rf.setConditionJoin("");
													rf.setInputType((byte)0); //��������
													rf.setPopUpName("");
													rf.setFieldName("case when isnull("+gpBean.getPropName() + "_Prop.PropItemName,'') = '' then pub_Color_Prop.PropItemName  else "+gpBean.getPropName() + "_Prop.PropItemName end" );
												}else{
													rf.setFieldName(gpBean.getPropName() + "_Prop.PropItemName");
												}
												rf.setAsFieldName(gpBean.getPropName()+"Name"); 
											}
										}										
								        Hashtable<String,ColDisplayBean> userSetWidth =BaseEnv.userSettingWidth ;
								        ColDisplayBean tmpBean = userSetWidth.get(tableName+rf.getAsFieldName());
										if(tmpBean!=null)
											rf.setWidth(tmpBean.getColWidth());
										else
											rf.setWidth("100");
								        rf.setFieldSysType("GoodsField");
										rf.setFixColName("0");
										setBean(bean, rf, userConfigBean, tableName);
										
									}
								}
							} else { 
								for (String[] shows : BaseEnv.reportShowSet) { 
									if (showSet.equals(shows[0]) && "1".equals(shows[2])) {
										/*
										if(field.getCondition() != null && field.getCondition().length() > 0 && field.getConditionJoin() != null && field.getConditionJoin().length() > 0 ){
											//����������ͻ���like
											field.setCondition("like '%'+?+'%'");
										}*/
										ReportField rf = field.clone();
										rf.setFieldName(preFN + shows[1]);
										//rf.setAsFieldName("gd_"+shows[1]);
										rf.setAsFieldName(shows[1]);
										
										DBFieldInfoBean fb = null;
										if(shows[0].indexOf("_")>0){
											fb = GlobalsTool.getFieldBean(shows[0].substring(0,shows[0].indexOf("_")) + "." + shows[1]);	
										}else{
											fb = GlobalsTool.getFieldBean(shows[0] + "." + shows[1]);	
										}
								        Hashtable<String,ColDisplayBean> userSetWidth =BaseEnv.userSettingWidth;
								        ColDisplayBean tmpBean = userSetWidth.get(tableName+rf.getAsFieldName());
										if(tmpBean!=null)
											rf.setWidth(tmpBean.getColWidth());
										else
											rf.setWidth(fb.getWidth() + "");
										rf.setDisplay(fb.getDisplay().get(locale));
										
										if("ComFullName".equals(shows[1])){
											if("tblCompany_2".equals(shows[0])){
												rf.setDisplay(BaseEnv.ModuleTable.get("tblCompany_2")==null? "�ͻ�ȫ��":BaseEnv.ModuleTable.get("tblCompany_2").get("zh_CN")+"ȫ��");
											}else if("tblCompany_1".equals(shows[0])){
												rf.setDisplay(BaseEnv.ModuleTable.get("tblCompany_1")==null? "��Ӧ��ȫ��":BaseEnv.ModuleTable.get("tblCompany_1").get("zh_CN")+"ȫ��");
											}
										}else if("ComName".equals(shows[1])){
											if("tblCompany_2".equals(shows[0])){
												rf.setDisplay(BaseEnv.ModuleTable.get("tblCompany_2")==null? "�ͻ����":BaseEnv.ModuleTable.get("tblCompany_2").get("zh_CN")+"���");
											}else if("tblCompany_1".equals(shows[0])){
												rf.setDisplay(BaseEnv.ModuleTable.get("tblCompany_1")==null? "��Ӧ�̼��":BaseEnv.ModuleTable.get("tblCompany_1").get("zh_CN")+"���");
											}
										}else if("ComNumber".equals(shows[1])){
											if("tblCompany_2".equals(shows[0])){
												rf.setDisplay(BaseEnv.ModuleTable.get("tblCompany_2")==null? "�ͻ����":BaseEnv.ModuleTable.get("tblCompany_2").get("zh_CN")+"���");
											}else if("tblCompany_1".equals(shows[0])){
												rf.setDisplay(BaseEnv.ModuleTable.get("tblCompany_1")==null? "��Ӧ�̱��":BaseEnv.ModuleTable.get("tblCompany_1").get("zh_CN")+"���");
											}
										}
										
										rf.setFieldType(fb.getFieldType());
										//rf��odga
										rf.setInputType(fb.getInputType()==16?0:fb.getInputTypeOld());
										if(fb.getInputTypeOld()== 1){
											rf.setPopUpName(fb.getRefEnumerationName());
										}
										/*
										if(rf.getCondition() != null && rf.getCondition().length() > 0 && rf.getConditionJoin() != null && rf.getConditionJoin().length() > 0 ){
											if("tblGoods".equals(shows[0]) && shows[1].equals("GoodsNumber")){
												rf.setInputType(DBFieldInfoBean.INPUT_MAIN_TABLE);
												rf.setPopUpName("ReportSelectGoodsNumber");												
											}else if("tblGoods".equals(shows[0]) && shows[1].equals("GoodsFullName")){
												rf.setInputType(DBFieldInfoBean.INPUT_MAIN_TABLE);
												rf.setPopUpName("ReportSelectGoodsFullName");
											}else if("tblCompany_1".equals(shows[0]) && shows[1].equals("ComNumber")){
												rf.setInputType(DBFieldInfoBean.INPUT_MAIN_TABLE);
												rf.setPopUpName("ReportSelectProviderNum");
											}else if("tblCompany_1".equals(shows[0]) && shows[1].equals("ComFullName")){
												rf.setInputType(DBFieldInfoBean.INPUT_MAIN_TABLE);
												rf.setPopUpName("ReportSelectProvider");
											}else if("tblCompany_2".equals(shows[0]) && shows[1].equals("ComNumber")){
												rf.setInputType(DBFieldInfoBean.INPUT_MAIN_TABLE);
												rf.setPopUpName("ReportSelectClientNum");
											}else if("tblCompany_2".equals(shows[0]) && shows[1].equals("ComFullName")){
												rf.setInputType(DBFieldInfoBean.INPUT_MAIN_TABLE);
												rf.setPopUpName("ReportSelectClient");
											}else if("tblCompany".equals(shows[0]) && shows[1].equals("ComNumber")){
												rf.setInputType(DBFieldInfoBean.INPUT_MAIN_TABLE);
												rf.setPopUpName("ReportSelectCompanyNum");
											}else if("tblCompany".equals(shows[0]) && shows[1].equals("ComFullName")){
												rf.setInputType(DBFieldInfoBean.INPUT_MAIN_TABLE);
												rf.setPopUpName("ReportSelectCompany");
											}
											field.setCondition("=?");
										} */
										
										setBean(bean, rf, userConfigBean, tableName);

									}
								}
							}
						} else {
							setBean(bean, field, userConfigBean, tableName);
						}

					}

				}
			} else if (temp.getNodeName().equalsIgnoreCase("from")) {
				NamedNodeMap nns = temp.getAttributes();
				bean.from = nns.getNamedItem("text").getNodeValue();
			} else if (temp.getNodeName().equalsIgnoreCase("SQL")) {
				NamedNodeMap nns = temp.getAttributes();
				String sql = nns.getNamedItem("text").getNodeValue().replaceAll("vbCrLf", " ").trim();
				//����Ƿ������⴦�����,������Ʒ��ʾ�ֶκ������ֶ�

				//����orderby
				if(sql.lastIndexOf("order by")>0){
					String orderBy = sql.substring(sql.lastIndexOf("order by"));
					if (orderBy.indexOf("[") > 0 && orderBy.indexOf("]") > 0 && orderBy.indexOf("@")==-1) {
						String lsql = "";
						while (orderBy.indexOf("[") > 0 && orderBy.indexOf("]") > 0) {
							String preFN = orderBy.substring(0, orderBy.indexOf("["));
							String showSet = orderBy.substring(orderBy.indexOf("[") + 1, orderBy.indexOf("]"));
							String lastFN = orderBy.substring(orderBy.indexOf("]") + 1);
							if ("GOODSPROP".equals(showSet)) {
								//��ʾ����Ʒ����
								for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
									if (gpBean != null && gpBean.getIsUsed() == 1&&!gpBean.getPropName().toLowerCase().equals("seq")) {
										orderBy = preFN + gpBean.getPropName() + lastFN;
										break;
									}
								}	
							} else {
								for (String[] shows : BaseEnv.reportShowSet) {
									if (showSet.equals(shows[0]) && "1".equals(shows[2])) {
										orderBy = preFN + shows[1] + lastFN;
										break;
									}
								}
							}
						}
						sql = sql.substring(0, sql.lastIndexOf("order by")) + orderBy;
					}
				}
				//����from ��������Ϊ����ǵ���������Ҫ�������Ա�
				if(sql.indexOf("{") > 0 && sql.indexOf("}") > 0){
					Pattern pattern = Pattern.compile("\\{[\\s]*([\\w]*)\\.[^\\}]*\\}");
					Matcher matcher = pattern.matcher(sql);
					String joinStr="";
					if(matcher.find()){
						String allstr=matcher.group();
						String ts = matcher.group(1);
						if(BaseEnv.version!=12  ){
							for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
								if (gpBean != null && gpBean.getIsUsed() == 1&&!gpBean.getPropName().toLowerCase().equals("seq")&&gpBean.getInputBill()==2) {
									if(gpBean.getPropName().toLowerCase().equals("color")){
										//��ƥ����ɫ����Ҫ���⴦��
										//������ù�����ɫ
										if((BaseEnv.version==3||BaseEnv.version==11) ){
											joinStr +=" left join tblGoodsOfProp pub_GoodsOfProp on isnull(pub_GoodsOfProp.GoodsCode,'')='' and pub_GoodsOfProp.propId='color' " +
													" left join tblGoodsOfPropdet pub_Color_Prop on "+ts+".color=pub_Color_Prop.propItemID and pub_Color_Prop.f_Ref=pub_GoodsOfProp.id ";
										}
										joinStr +=" left join tblGoodsOfProp on tblGoodsOfProp.GoodsCode="+ts+".GoodsCode and tblGoodsOfProp.propId='color' " +
										" left join tblGoodsOfPropdet "+gpBean.getPropName() + 
											"_Prop on "+ts+".color="+gpBean.getPropName() + "_Prop.propItemID and "+gpBean.getPropName() + "_Prop.f_Ref=tblGoodsOfProp.id ";
										
									}else{									
										joinStr +=" left join tblGoodsOfPropdet "+gpBean.getPropName() + 
											"_Prop on "+ts+"."+gpBean.getPropName()+"="+gpBean.getPropName() + "_Prop.propItemID and "+gpBean.getPropName() + "_Prop.propId='"+gpBean.getPropName()+"' ";
										if(BaseEnv.version==8){
											joinStr+=" and "+gpBean.getPropName() +"_Prop.f_ref='"+gpBean.getPropName() +"_Default' ";
										}
									}
								}
							}					
							sql= sql.substring(0,sql.indexOf(allstr))+joinStr+sql.substring(sql.indexOf(allstr)+allstr.length());
						}else{
							sql= sql.substring(0,sql.indexOf(allstr))+sql.substring(sql.indexOf(allstr)+allstr.length());
						}
					}
				}
				
				if (sql.indexOf("[") > 0 && sql.indexOf("]") > 0) {
					//������
					Pattern pattern = Pattern.compile("[\\s]*[\\w]*\\.\\[([\\w]*)\\] as ([\\w]*)[, ]{1}");

					Matcher matcher = pattern.matcher(sql);

					while (matcher.find()) {
						String allstr = matcher.group(0);
						String showSet = matcher.group(1);
						String asName = matcher.group(2);
						String lsql = "";
						if ("GOODSPROP".equals(showSet)) {
							//��ʾ����Ʒ����
							for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
								if (gpBean != null && !gpBean.getPropName().toLowerCase().equals("seq")) {
									if(gpBean.getPropName().toLowerCase().equals("color") && (BaseEnv.version==3||BaseEnv.version==11) ){
										//��ƥ�Ļ�Ҫ��ʾ��ɫ���
										lsql += allstr.replaceFirst("\\[" + showSet + "\\]", gpBean.getPropName()).replaceFirst(asName, gpBean.getPropName()) + (allstr.endsWith(",") ? "" : ",");
									}
									//V7 ����ͨ���Դ���
									if(gpBean.getInputBill()==2 && BaseEnv.version!=12){ 
										//������ѡ�� 
										if(gpBean.getPropName().toLowerCase().equals("color") && (BaseEnv.version==3||BaseEnv.version==11) ){
											lsql +=  "case when isnull("+gpBean.getPropName() + "_Prop.PropItemName,'') = '' then pub_Color_Prop.PropItemName  else "+gpBean.getPropName() + "_Prop.PropItemName end as "+gpBean.getPropName()+"Name,"+ (allstr.endsWith(",") ? "" : ",");
										}else{
											lsql +=  gpBean.getPropName() + "_Prop.PropItemName as "+gpBean.getPropName()+"Name,"+ (allstr.endsWith(",") ? "" : ",");
										}
									}else{
										lsql += allstr.replaceFirst("\\[" + showSet + "\\]", gpBean.getPropName()).replaceFirst(asName, gpBean.getPropName()) + (allstr.endsWith(",") ? "" : ",");
									}
								}
							}
						} else {
							for (String[] shows : BaseEnv.reportShowSet) {
								if (showSet.equals(shows[0]) && "1".equals(shows[2])) { //"gd_"+
									lsql += allstr.replaceFirst(asName, shows[1]).replaceFirst("\\[" + showSet + "\\]", shows[1]) + (allstr.endsWith(",") ? "" : ",");
								}
							}
						}
						if (allstr.endsWith(" ")) {
							lsql = lsql.substring(0, lsql.length() - 1);
						}
						sql = sql.substring(0, sql.indexOf(allstr)) + lsql + sql.substring(sql.indexOf(allstr) + allstr.length());
					}
				
					//����where�����е�����				
					//       and (tblStockDet.[GOODSPROP] =?)       
					pattern = Pattern.compile("[\\s]*and[\\s]*\\(([\\w]*)\\.\\[([^\\]]*)\\][^\\)]*\\)");

					matcher = pattern.matcher(sql);
					while (matcher.find()) {
						String allstr = matcher.group(0);
						String pre = matcher.group(1);
						String type = matcher.group(2);
						String lsql = "";
						if("GOODSPROP".equals(type)){						
							//��ʾ����Ʒ����
							for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
								if (gpBean != null && !gpBean.getPropName().toLowerCase().equals("seq")) {
									//���������õ�
									if(gpBean.getInputBill()==2&& BaseEnv.version!=12){
										//������ѡ��
										if(gpBean.getPropName().toLowerCase().equals("color") && (BaseEnv.version==3||BaseEnv.version==11) ){
											lsql += allstr.replaceFirst("\\[GOODSPROP\\]", gpBean.getPropName());
										}else{
											lsql += allstr.replaceFirst("[\\w]*\\.\\[GOODSPROP\\]", gpBean.getPropName()+"_Prop.PropItemName");
										}
										
									}else{
										lsql += allstr.replaceFirst("\\[GOODSPROP\\]", gpBean.getPropName());
									}
								}
							}
						} else { 
							/*
							for (String[] shows : BaseEnv.reportShowSet) { 
								if (type.equals(shows[0]) && "1".equals(shows[2])) { //and (tblCompany.[tblCompany_1] =?)							
									if(type.equals("tblGoods") && shows[1].equals("GoodsNumber") && sql.matches("[\\S\\s]*getClassCode\\(tblGoods[\\s]*,[\\s]*GoodsNumber[\\S\\s]*")){
										//����getClassCode����ʱ������Ҫ��or 1=1
										lsql += allstr.substring(0,allstr.indexOf(".")+1)+shows[1]+" =? or 1=1)";
										for (ReportField rf : bean.fieldInfo){
											if(rf.getAsFieldName().equals(shows[1])){
												rf.setCondition("=? or 1=1");
												break;
											}
										}
									}else if(type.equals("tblGoods") && shows[1].equals("GoodsFullName") && sql.matches("[\\S\\s]*getClassCode\\(tblGoods[\\s]*,[\\s]*GoodsFullName[\\S\\s]*")){
										lsql += allstr.substring(0,allstr.indexOf(".")+1)+shows[1]+" =? or 1=1)";
										for (ReportField rf : bean.fieldInfo){
											if(rf.getAsFieldName().equals(shows[1])){
												rf.setCondition("=? or 1=1");
												break;
											}
										}
									}else if(type.startsWith("tblCompany") && shows[1].equals("ComNumber") && sql.matches("[\\S\\s]*getClassCode\\(tblCompany[\\s]*,[\\s]*ComNumber[\\S\\s]*")){
										//����getClassCode����ʱ������Ҫ��or 1=1
										lsql += allstr.substring(0,allstr.indexOf(".")+1)+shows[1]+" =? or 1=1)";
										for (ReportField rf : bean.fieldInfo){
											if(rf.getAsFieldName().equals(shows[1])){
												rf.setCondition("=? or 1=1");
												break;
											}
										}
									}else if(type.startsWith("tblCompany") && shows[1].equals("ComFullName") && sql.matches("[\\S\\s]*getClassCode\\(tblCompany[\\s]*,[\\s]*ComFullName[\\S\\s]*")){
										lsql += allstr.substring(0,allstr.indexOf(".")+1)+shows[1]+" =? or 1=1)";
										for (ReportField rf : bean.fieldInfo){
											if(rf.getAsFieldName().equals(shows[1])){
												rf.setCondition("=? or 1=1");
												break;
											}
										}
									}else{
										//lsql += allstr.replaceFirst("\\["+type+"\\]", shows[1]);
										lsql += allstr.substring(0,allstr.indexOf(".")+1)+shows[1]+"like '%'+?+'%')";
									}
								}
							} */
						}					
						
						sql = sql.substring(0, sql.indexOf(allstr)) + lsql + sql.substring(sql.indexOf(allstr) + allstr.length());
					}					
					
					
					//����group by 
					pattern = Pattern.compile("[\\s]*[\\w]*\\.\\[([\\w]*)\\][, ]{0,1}");

					matcher = pattern.matcher(sql);

					while (matcher.find()) {
						String allstr = matcher.group(0);
						String showSet = matcher.group(1);
						String lsql = "";
						if ("GOODSPROP".equals(showSet)) {
							//��ʾ����Ʒ����
							for (GoodsPropInfoBean gpBean : BaseEnv.propList) { 
								if (gpBean != null &&!gpBean.getPropName().toLowerCase().equals("seq")) {
									//���������õ�
									if(gpBean.getInputBill()==2 && BaseEnv.version!=12){ 
										//������ѡ�� 
										if(gpBean.getPropName().toLowerCase().equals("color") && (BaseEnv.version==3||BaseEnv.version==11) ){
											lsql +=allstr.replaceFirst("\\[" + showSet + "\\]", gpBean.getPropName()) + (allstr.endsWith(",") ? "" : ",")+ " case when isnull("+gpBean.getPropName() + "_Prop.PropItemName,'') = '' then pub_Color_Prop.PropItemName  else "+gpBean.getPropName() + "_Prop.PropItemName end ,";
										}else{
											lsql +=  " "+gpBean.getPropName() + "_Prop.PropItemName ,";
										}
									}else{
										lsql += allstr.replaceFirst("\\[" + showSet + "\\]", gpBean.getPropName()) + (allstr.endsWith(",") ? "" : ",");
									}
								}
							}
						} else {
							for (String[] shows : BaseEnv.reportShowSet) {
								if (showSet.equals(shows[0]) && "1".equals(shows[2])) {
									lsql += allstr.replaceFirst("\\[" + showSet + "\\]", shows[1]) + (allstr.endsWith(",") ? "" : ",");
								}
							}
						}
						if (allstr.endsWith(" ")) {
							lsql = lsql.substring(0, lsql.length() - 1);
						}
						if(!allstr.endsWith(",")){ //������ԣ��Ž�β����Ҫȥ�����ģ���
							lsql = lsql.substring(0, lsql.length() - 1);
						}
						sql = sql.substring(0, sql.indexOf(allstr)) + lsql + sql.substring(sql.indexOf(allstr) + allstr.length());
					}
					
				}
				
				bean.sql = sql;
			} else if ("charts".equalsIgnoreCase(temp.getNodeName())) {
				/*����ͼ�α�������*/
				NodeList noteList2 = temp.getChildNodes();
				ArrayList<DefineChartBean> chartList = new ArrayList<DefineChartBean>();
				for (int k = 0; k < noteList2.getLength(); k++) {
					Node chart = noteList2.item(k);
					if ("chart".equalsIgnoreCase(chart.getNodeName())) {
						DefineChartBean chartBean = new DefineChartBean();
						NodeList fieldList = chart.getChildNodes();
						for (int j = 0; j < fieldList.getLength(); j++) {
							Node fieldNode = fieldList.item(j);
							if ("chartName".equals(fieldNode.getNodeName())) {
								chartBean.setChartName(fieldNode.getTextContent());
							}
							if ("type".equals(fieldNode.getNodeName())) {
								chartBean.setType(fieldNode.getTextContent());
							}
							if ("xAxis".equals(fieldNode.getNodeName())) {
								NamedNodeMap xAtt = fieldNode.getAttributes();
								if (xAtt.getNamedItem("name") != null) {
									chartBean.setXAxisName(xAtt.getNamedItem("name").getNodeValue().trim());
								} else {
									chartBean.setXAxisName("");
								}
								NodeList xAxis = fieldNode.getChildNodes();
								ArrayList<String> axis = new ArrayList<String>();
								for (int m = 0; m < xAxis.getLength(); m++) {
									Node xNode = xAxis.item(m);
									if ("x".equals(xNode.getNodeName())) {
										axis.add(xNode.getTextContent());
									}
								}
								chartBean.setXAxis(axis);
							}
							if ("yAxis".equals(fieldNode.getNodeName())) {
								NamedNodeMap yAtt = fieldNode.getAttributes();
								if (yAtt.getNamedItem("name") != null) {
									chartBean.setYAxisName(yAtt.getNamedItem("name").getNodeValue().trim());
								} else {
									chartBean.setYAxisName("");
								}
								NodeList yAxis = fieldNode.getChildNodes();
								ArrayList<String> axis = new ArrayList<String>();
								for (int m = 0; m < yAxis.getLength(); m++) {
									Node yNode = yAxis.item(m);
									if ("y".equals(yNode.getNodeName())) {
										axis.add(yNode.getTextContent());
									}
								}
								chartBean.setYAxis(axis);
							}
						}
						chartList.add(chartBean);
					}
				}
				bean.setChartList(chartList);
			}
		}
		
		//��ѯ�Ƿ��û��и��Զ���
		if(bean.userConfig.size() > 0){ //�û������ж����ĸ���������
			//���û�����������			
			ArrayList<ReportField> df=new ArrayList();
			ArrayList<ReportField> df2=new ArrayList();
			for(String[] colB:bean.userConfig){
				for(ReportField rf:bean.disFields){ 
					if(colB[0].equals(rf.getAsFieldName())){
						df.add(rf);
						int defineWidth = 0;
						try{
							defineWidth = Integer.parseInt(colB[1]);
						}catch(Exception e){}
						if(defineWidth != 0){
							rf.setWidth(defineWidth+"");
						}
					}
				}
				for(ReportField rf:bean.disFields2){
					if(colB[0].equals(rf.getAsFieldName())){
						df2.add(rf);
					}
				}
			}
			//δ���������е��м���			
			for(ReportField rf:bean.disFields){
				if(!df.contains(rf)){
					df.add(0,rf);
				}    
			}
			for(ReportField rf:bean.disFields2){
				if(!df2.contains(rf)){
					df2.add(0,rf);
				}
			}
			bean.disFields = df;
			bean.disFields2 =df2;
		}else{
			//����������ã�������������		 disFields2
			ArrayList<ColConfigBean> colList = userConfigBean.get(tableName + "list");
			if(colList != null){
				ArrayList<ReportField> df=new ArrayList();
				ArrayList<ReportField> df2=new ArrayList();
				for(ColConfigBean colB:colList){
					for(ReportField rf:bean.disFields){ 
						if(colB.getColName().equals(rf.getAsFieldName())){
							df.add(rf);
							int defineWidth = GlobalsTool.getFieldWidth(tableName, colB.getColName());
							if(defineWidth != 0){
								rf.setWidth(defineWidth+"");
							}
						}
					}
					for(ReportField rf:bean.disFields2){
						if(colB.getColName().equals(rf.getAsFieldName())){
							df2.add(rf);
						}
					}
				}
				//δ���������е��м���			
				for(ReportField rf:bean.disFields){
					if(!df.contains(rf)){
						df.add(0,rf);
					}    
				}
				for(ReportField rf:bean.disFields2){
					if(!df2.contains(rf)){
						df2.add(0,rf);
					}
				}
				bean.disFields = df;
				bean.disFields2 =df2;
			}
		}
		
		return bean;
	}

	public static void setBean(DefineReportBean bean, ReportField field, Hashtable<String, ArrayList<ColConfigBean>> userConfigBean, String tableName) {
		boolean flag = true;
		bean.fieldInfo.add(field);
		if (BaseEnv.systemSet.get(field.getFieldSysType()) != null && !Boolean.parseBoolean(BaseEnv.systemSet.get(field.getFieldSysType()).getSetting())) {
			field.setInputType(Byte.parseByte("100"));
			flag = false;
		}

		if ("GoodsField".equals(field.getFieldSysType())) {
			//��������ֶ�δ���ã������ֶ�ϵͳ����Ϊ��Ʒ��������治��ʾ
			String propFieldName = field.getAsFieldName();
			if (propFieldName.toLowerCase().endsWith("nv")) {
				propFieldName = propFieldName.substring(0, propFieldName.length() - 2);
			}
			//����ֶ�����Ϊ��ɫ���ƣ�����ɫ�������������
			if ("colorName".equalsIgnoreCase(propFieldName)) {
				propFieldName = "color";
			}
			GoodsPropInfoBean gpBean = (GoodsPropInfoBean) BaseEnv.propIgnoreCaseMap.get(propFieldName.toLowerCase());
			if (gpBean != null && gpBean.getIsUsed() == 2) {
				field.setInputType(Byte.parseByte("100"));
				flag = false;
			}

		}

		if (!field.getWidth().equals("0") && !field.getWidth().equals("")) {
			if (flag) {
				if ("1".equals(field.getAnalysis())) {
					bean.statFields.add(field);
				} else {
					if (userConfigBean.containsKey(tableName + "list") || bean.userConfig.size() > 0) { 
						boolean existConfig = false;
						if(bean.userConfig.size() > 0){ //����и��������ã����û���
							for(String[] colB:bean.userConfig){
								if(colB[0].equals(field.getAsFieldName())){
									existConfig = true;
									break;
								}
							}
						}else{
							existConfig = GlobalsTool.colIsExistConfigList(tableName, field.getAsFieldName(), "list") ;
						}
						if (existConfig || field.getCrossField() == 1 || field.getCrossField() == 2 || "-1".equals(field.getWidth())) {
							if (6 == field.getInputType() && field.getPopUpName().length() > 0) {
								EnumerateBean enumBean = (EnumerateBean) BaseEnv.enumerationMap.get(field.getPopUpName());
								if ("language".equals(field.getPopUpName())) {
									field.setInputType(Byte.valueOf("4"));
								} else if (enumBean != null) {
									field.setInputType(Byte.valueOf("1"));
								} else {
									field.setInputType(Byte.valueOf("2"));
								}
							}
							bean.disFields.add(field);
						}
					} else {
						if (field.getInputType() != 6)//���ؿ���ʾĬ�ϲ�����
							bean.disFields.add(field);
					}
					bean.disFields2.add(field);
				}
			}
		}
		if (field.getCondition().indexOf("?") > 0 || field.getCondition().indexOf("@ValueofFun") > 0) {
			bean.conFields.add(field);
		}

		if (field.getDisplayFlag().equals("1")) {
			bean.queryFields.add(field);
		}

		//���Ϊ�����У��ڴ�ָ��Ϊ�˱���ķ�����
		if (field.getClassCode().equals("0")) {
			bean.classCode = field;
		}
	}

	
	/**
	 * �������������ļ�������
	 * @param fileName				//����
	 * @return
	 * @throws Exception
	 */
	public static DefineReportBean parseReport(String fileName) throws Exception{
		DefineReportBean bean = new DefineReportBean();

		String tableName = fileName.replace("SQL.xml", "");
		String locale = "";
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		File file = new File(BaseEnv.USERREPORTPATH + "\\" + fileName);
		if (file == null || file.isFile() == false) {
			file = new File(BaseEnv.REPORTPATH + "\\" + fileName);
		}
		Document document = dbf.newDocumentBuilder().parse(file);
		Node config = document.getFirstChild();
		NodeList nodeList = config.getChildNodes();
		
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node temp = nodeList.item(i);
			if (temp.getNodeName().equalsIgnoreCase("Report")) {
				NamedNodeMap nns = temp.getAttributes();
				bean.reportId = nns.getNamedItem("reporteId").getNodeValue();
				bean.reportName = nns.getNamedItem("reportName").getNodeValue();
			} else if (temp.getNodeName().equalsIgnoreCase("fieldInfo")) {
				NodeList fieldList = temp.getChildNodes();
				for (int j = 0; j < fieldList.getLength(); j++) {
					Node fieldNode = fieldList.item(j);
					if (fieldNode.getNodeName().equalsIgnoreCase("field")) {
						NamedNodeMap fieldAtt = fieldNode.getAttributes();
	
						ReportField field = new ReportField();
						field.setFieldName(fieldAtt.getNamedItem("fieldname").getNodeValue().trim());
						field.setAsFieldName(fieldAtt.getNamedItem("AsfieldName").getNodeValue().trim());
						field.setCondition(fieldAtt.getNamedItem("condition").getNodeValue().trim());
						field.setConditionJoin(fieldAtt.getNamedItem("conditionJoin").getNodeValue().trim());
						field.setDisplayFlag(fieldAtt.getNamedItem("displayFlag").getNodeValue().trim());
						String filedType = fieldAtt.getNamedItem("fieldType").getNodeValue().trim();
						if (filedType.length() > 0)
							field.setFieldType(Byte.parseByte(filedType));
						else
							field.setFieldType(Byte.parseByte("2"));
						if (fieldAtt.getNamedItem("repeatNotShow") != null) {
							String repeatNotShow = fieldAtt.getNamedItem("repeatNotShow").getNodeValue();
							if (repeatNotShow != null && repeatNotShow.length() > 0)
								field.setRepeatNotShow(Byte.parseByte(repeatNotShow.trim()));
							else
								field.setRepeatNotShow(Byte.parseByte("0"));
						}
						field.setGroupByFlag(fieldAtt.getNamedItem("groupByFlag").getNodeValue().trim());
						String inputType = fieldAtt.getNamedItem("inputType").getNodeValue().trim();
						if (inputType.length() > 0)
							field.setInputType(Byte.parseByte(inputType));
						else
							field.setInputType(Byte.parseByte("0"));
						field.setOrderByFlag(fieldAtt.getNamedItem("orderbyFlag").getNodeValue().trim());
						field.setPopUpName(fieldAtt.getNamedItem("popUpName").getNodeValue().trim());
						field.setOrder(fieldAtt.getNamedItem("order").getNodeValue().trim());
						field.setClassCode(fieldAtt.getNamedItem("classCode").getNodeValue().trim());
						field.setLinkAdd(fieldAtt.getNamedItem("linkAdd").getNodeValue().trim());
						if (fieldAtt.getNamedItem("isStat") != null) {
							field.setIsStat(fieldAtt.getNamedItem("isStat").getNodeValue().trim());
							if (field.getIsStat().equals("1")) {
								bean.haveStat = true;
							}
						} else {
							field.setIsStat("0");
						}
						if (fieldAtt.getNamedItem("fieldSysType") != null) {
							field.setFieldSysType(fieldAtt.getNamedItem("fieldSysType").getNodeValue().trim());
						} else {
							field.setFieldSysType("");
						}
						if (fieldAtt.getNamedItem("fieldIdentity") != null) {
							field.setFieldIdentity(fieldAtt.getNamedItem("fieldIdentity").getNodeValue().trim());
						} else {
							field.setFieldIdentity("");
						}
						if (fieldAtt.getNamedItem("fixColName") != null) {
							field.setFixColName(fieldAtt.getNamedItem("fixColName").getNodeValue().trim());
						}
						if (fieldAtt.getNamedItem("isNull") != null) {
							field.setIsNull(fieldAtt.getNamedItem("isNull").getNodeValue().trim());
						}
						if (fieldAtt.getNamedItem("planarField") != null) {
							field.setPalnarField(fieldAtt.getNamedItem("planarField").getNodeValue().trim());
						}
						if (fieldAtt.getNamedItem("subSQL") != null) {
							field.setSubSQL(fieldAtt.getNamedItem("subSQL").getNodeValue().trim());
						}
						if (fieldAtt.getNamedItem("crossField") != null && fieldAtt.getNamedItem("crossField").getNodeValue().trim().length() > 0) {
							field.setCrossField(Byte.parseByte(fieldAtt.getNamedItem("crossField").getNodeValue()));
							if (field.getCrossField() == 1) {
								bean.setCross(true);
								bean.crossField = field;
							}
						}
						if (fieldAtt.getNamedItem("zeroDisplay") != null) {
							field.setZeroDisplay(Byte.parseByte(fieldAtt.getNamedItem("zeroDisplay").getNodeValue()));
						}
						if (fieldAtt.getNamedItem("groupName") != null) {
							field.setGroupName(fieldAtt.getNamedItem("groupName").getNodeValue());
						} else {
							field.setGroupName("");
						}
						if (fieldAtt.getNamedItem("analysis") != null) {
							field.setAnalysis(fieldAtt.getNamedItem("analysis").getNodeValue());
						}
						//ƥ������ֶ����ֺ���Ʒ���Ե�����һ����������NV��colorName,colorNameNV�ֶΣ����Զ���Ϊ����һ�������ֶ�
						//����������������������Сд���淶���������
						if (BaseEnv.propIgnoreCaseMap.get(field.getAsFieldName().toLowerCase()) != null) {
							field.setFieldSysType("GoodsField");
						} else if (BaseEnv.propIgnoreCaseMap.get(field.getAsFieldName().toLowerCase() + "NV") != null) {
							field.setFieldSysType("GoodsField");
						} else if (field.getAsFieldName().equalsIgnoreCase("colorName") || field.getAsFieldName().equalsIgnoreCase("colorNameNV")) {
							//����ֶ�����colorName��colorNameNV�����Զ���Ϊ����һ�������ֶ�
							field.setFieldSysType("GoodsField");
						}
						NodeList list = fieldNode.getChildNodes();
						if (fieldAtt.getNamedItem("defaultValue") != null){
							field.setDefaultValue(fieldAtt.getNamedItem("defaultValue").getNodeValue().trim());
						}
//						������ʾ�ֶ�
						if (field.getFieldName().indexOf("[") > 0 && field.getFieldName().indexOf("]") > 0 && field.getFieldName().indexOf("@")==-1) {
							String preFN = field.getFieldName().substring(0, field.getFieldName().indexOf("["));
							String showSet = field.getFieldName().substring(field.getFieldName().indexOf("[") + 1, field.getFieldName().indexOf("]"));
							if ("GOODSPROP".equals(showSet)) {
								//��ʾ����Ʒ����
								for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
									if (gpBean != null && gpBean.getIsUsed() == 1&&!gpBean.getPropName().toLowerCase().equals("seq")) {
										if(gpBean.getPropName().toLowerCase().equals("color") && (BaseEnv.version==3||BaseEnv.version==11) ){
											//��ƥ�Ļ�Ҫ��ʾ��ɫ���
											ReportField rf = field.clone();
											rf.setFieldName(preFN + gpBean.getPropName());
											rf.setAsFieldName(gpBean.getPropName());
											//�����Ƿ�Ҫ�������������õ�					
											//ȡtblStockDet���color�ֶ���
											String dis= GlobalsTool.getFieldDisplay(BaseEnv.tableInfos, "tblStockDet.color", locale);
											rf.setDisplay( dis);
											rf.setFieldType((byte)2);
											rf.setInputType((byte)2);
											rf.setPopUpName(gpBean.getAlertName());								
											rf.setWidth("90");
											rf.setFieldSysType("GoodsField");
											rf.setFixColName("0");
											if (rf.getCondition().indexOf("?") > 0 || rf.getCondition().indexOf("@ValueofFun") > 0) {
												bean.conFields.add(rf);
											}

											if (rf.getDisplayFlag().equals("1")) {
												bean.queryFields.add(rf);
											}
											bean.fieldInfo.add(rf);
										}
										
										//���������õ�
										ReportField rf = field.clone();
										rf.setFieldName(preFN + gpBean.getPropName());
										rf.setAsFieldName(gpBean.getPropName());
										//�����Ƿ�Ҫ�������������õ�
										rf.setDisplay(gpBean.getDisplay().get(locale));
										//�ֶ�����Ϊ����
										if(gpBean.getPropName().equals("ProDate")||gpBean.getPropName().equals("Availably")){
											rf.setFieldType((byte)5);
										}else{
											rf.setFieldType((byte)2);
										}
										rf.setInputType((byte)0); //��������
										if(gpBean.getInputBill()==2){
											//������ѡ��
											rf.setInputType((byte)2);
											rf.setPopUpName(gpBean.getAlertName());
											//��ƥ���ù�����ɫ��
											if(gpBean.getPropName().toLowerCase().equals("color") && (BaseEnv.version==3||BaseEnv.version==11) ){
												rf.setCondition("");
												rf.setConditionJoin("");
												rf.setInputType((byte)0); //��������
												rf.setPopUpName("");
												rf.setFieldName("case when isnull("+gpBean.getPropName() + "_Prop.PropItemName,'') = '' then pub_Color_Prop.PropItemName  else "+gpBean.getPropName() + "_Prop.PropItemName end" );
											}else{
												rf.setFieldName(gpBean.getPropName() + "_Prop.PropItemName");
											}
											rf.setAsFieldName(gpBean.getPropName()+"Name");
										}										
										rf.setWidth("90");
										rf.setFieldSysType("GoodsField");
										rf.setFixColName("0");
										if (rf.getCondition().indexOf("?") > 0 || rf.getCondition().indexOf("@ValueofFun") > 0) {
											bean.conFields.add(rf);
										}

										if (rf.getDisplayFlag().equals("1")) {
											bean.queryFields.add(rf);
										}
										bean.fieldInfo.add(rf);
									}
								}
							} else {
								for (String[] shows : BaseEnv.reportShowSet) {
									if (showSet.equals(shows[0]) && "1".equals(shows[2])) {
										ReportField rf = field.clone();
										rf.setFieldName(preFN + shows[1]);
										rf.setAsFieldName(shows[1]);
										String tn = showSet;
										if(tn.indexOf("_")> 0){
											tn = tn.substring(0,tn.indexOf("_"));
										}
										DBFieldInfoBean fb = GlobalsTool.getFieldBean(tn + "." + shows[1]);
//										rf.setWidth(fb.getWidth() + "");
										if(fb != null){
											rf.setDisplay(fb.getDisplay().get(locale));
											rf.setFieldType(fb.getFieldType());
											rf.setInputType(fb.getInputType()==16?0:fb.getInputType());
											if(fb.getInputType()== 1){
												rf.setPopUpName(fb.getRefEnumerationName());
											} 
										}
										if (rf.getCondition().indexOf("?") > 0 || rf.getCondition().indexOf("@ValueofFun") > 0) {
											bean.conFields.add(rf);
										}

										if (rf.getDisplayFlag().equals("1")) {
											bean.queryFields.add(rf);
										}
										bean.fieldInfo.add(rf);
									}
								}
							}
						} else {
							if (field.getCondition().indexOf("?") > 0 || field.getCondition().indexOf("@ValueofFun") > 0) {
								bean.conFields.add(field);
							}

							if (field.getDisplayFlag().equals("1")) {
								bean.queryFields.add(field);
							}
							bean.fieldInfo.add(field);
						}
					}
				}
			} else if (temp.getNodeName().equalsIgnoreCase("from")) {
				NamedNodeMap nns = temp.getAttributes();
				bean.from = nns.getNamedItem("text").getNodeValue();
			} else if (temp.getNodeName().equalsIgnoreCase("SQL")) {
				NamedNodeMap nns = temp.getAttributes();
				String sql = nns.getNamedItem("text").getNodeValue().replaceAll("vbCrLf", " ").trim();
				//����Ƿ������⴦�����,������Ʒ��ʾ�ֶκ������ֶ�
	
				//����orderby
				if(sql.lastIndexOf("order by")>0){
					String orderBy = sql.substring(sql.lastIndexOf("order by"));
					if (orderBy.indexOf("[") > 0 && orderBy.indexOf("]") > 0 && orderBy.indexOf("@")==-1) {
						String lsql = "";
						while (orderBy.indexOf("[") > 0 && orderBy.indexOf("]") > 0) {
							String preFN = orderBy.substring(0, orderBy.indexOf("["));
							String showSet = orderBy.substring(orderBy.indexOf("[") + 1, orderBy.indexOf("]"));
							String lastFN = orderBy.substring(orderBy.indexOf("]") + 1);
							if ("GOODSPROP".equals(showSet)) {
								//��ʾ����Ʒ����
								for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
									if (gpBean != null && gpBean.getIsUsed() == 1&&!gpBean.getPropName().toLowerCase().equals("seq")) {
										orderBy = preFN + gpBean.getPropName() + lastFN;
										break;
									}
								}	
							} else {
								for (String[] shows : BaseEnv.reportShowSet) {
									if (showSet.equals(shows[0]) && "1".equals(shows[2])) {
										orderBy = preFN + shows[1] + lastFN;
										break;
									}
								}
							}
						}
						sql = sql.substring(0, sql.lastIndexOf("order by")) + orderBy;
					}
				}
				//����from ��������Ϊ����ǵ���������Ҫ�������Ա�
				if(sql.indexOf("{") > 0 && sql.indexOf("}") > 0){
					Pattern pattern = Pattern.compile("\\{[\\s]*([\\w]*)\\.[^\\}]*\\}");
					Matcher matcher = pattern.matcher(sql);
					String joinStr="";
					if(matcher.find()){
						String allstr=matcher.group();
						String ts = matcher.group(1);
						for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
							if (gpBean != null && gpBean.getIsUsed() == 1&&!gpBean.getPropName().toLowerCase().equals("seq")&&gpBean.getInputBill()==2) {
								if(gpBean.getPropName().toLowerCase().equals("color")){
									//��ƥ����ɫ����Ҫ���⴦��
									//������ù�����ɫ
									if((BaseEnv.version==3||BaseEnv.version==11) ){
										joinStr +=" left join tblGoodsOfProp pub_GoodsOfProp on isnull(pub_GoodsOfProp.GoodsCode,'')='' and pub_GoodsOfProp.propId='color' " +
												" left join tblGoodsOfPropdet pub_Color_Prop on "+ts+".color=pub_Color_Prop.propItemID and pub_Color_Prop.f_Ref=pub_GoodsOfProp.id ";
									}
									joinStr +=" left join tblGoodsOfProp on tblGoodsOfProp.GoodsCode="+ts+".GoodsCode and tblGoodsOfProp.propId='color' " +
									" left join tblGoodsOfPropdet "+gpBean.getPropName() + 
										"_Prop on "+ts+".color="+gpBean.getPropName() + "_Prop.propItemID and "+gpBean.getPropName() + "_Prop.f_Ref=tblGoodsOfProp.id ";
									
								}else{									
									joinStr +=" left join tblGoodsOfPropdet "+gpBean.getPropName() + 
										"_Prop on "+ts+"."+gpBean.getPropName()+"="+gpBean.getPropName() + "_Prop.propItemID and "+gpBean.getPropName() + "_Prop.propId='"+gpBean.getPropName()+"' ";
									if(BaseEnv.version==8){
										joinStr+=" and "+gpBean.getPropName() +"_Prop.f_ref='"+gpBean.getPropName() +"_Default' ";
									}
								}
							}
						}					
						sql= sql.substring(0,sql.indexOf(allstr))+joinStr+sql.substring(sql.indexOf(allstr)+allstr.length());
					}
				}
				
				if (sql.indexOf("[") > 0 && sql.indexOf("]") > 0) {
					//������
					Pattern pattern = Pattern.compile("[\\s]*[\\w]*\\.\\[([\\w]*)\\] as ([\\w]*)[, ]{1}");
	
					Matcher matcher = pattern.matcher(sql);
	
					while (matcher.find()) {
						String allstr = matcher.group(0);
						String showSet = matcher.group(1);
						String asName = matcher.group(2);
						String lsql = "";
						if ("GOODSPROP".equals(showSet)) {
							//��ʾ����Ʒ����
							for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
								if (gpBean != null && gpBean.getIsUsed() == 1&&!gpBean.getPropName().toLowerCase().equals("seq")) {
									if(gpBean.getPropName().toLowerCase().equals("color") && (BaseEnv.version==3||BaseEnv.version==11) ){
										//��ƥ�Ļ�Ҫ��ʾ��ɫ���
										lsql += allstr.replaceFirst("\\[" + showSet + "\\]", gpBean.getPropName()).replaceFirst(asName, gpBean.getPropName()) + (allstr.endsWith(",") ? "" : ",");
									}
									//���������õ�
									if(gpBean.getInputBill()==2){ 
										//������ѡ�� 
										if(gpBean.getPropName().toLowerCase().equals("color") && (BaseEnv.version==3||BaseEnv.version==11) ){
											lsql +=  "case when isnull("+gpBean.getPropName() + "_Prop.PropItemName,'') = '' then pub_Color_Prop.PropItemName  else "+gpBean.getPropName() + "_Prop.PropItemName end as "+gpBean.getPropName()+"Name,"+ (allstr.endsWith(",") ? "" : ",");
										}else{
											lsql +=  gpBean.getPropName() + "_Prop.PropItemName as "+gpBean.getPropName()+"Name,"+ (allstr.endsWith(",") ? "" : ",");
										}
									}else{
										lsql += allstr.replaceFirst("\\[" + showSet + "\\]", gpBean.getPropName()).replaceFirst(asName, gpBean.getPropName()) + (allstr.endsWith(",") ? "" : ",");
									}
								}
							}
						} else {
							for (String[] shows : BaseEnv.reportShowSet) {
								if (showSet.equals(shows[0]) && "1".equals(shows[2])) { //"gd_"+
									lsql += allstr.replaceFirst(asName, shows[1]).replaceFirst("\\[" + showSet + "\\]", shows[1]) + (allstr.endsWith(",") ? "" : ",");
								}
							}
						}
						if (allstr.endsWith(" ")) {
							lsql = lsql.substring(0, lsql.length() - 1);
						}
						sql = sql.substring(0, sql.indexOf(allstr)) + lsql + sql.substring(sql.indexOf(allstr) + allstr.length());
					}
				
					//����where�����е�����				
					//       and (tblStockDet.[GOODSPROP] =?)       
					pattern = Pattern.compile("[\\s]*and[\\s]*\\([\\w]*\\.\\[GOODSPROP\\][^\\)]*\\)");
	
					matcher = pattern.matcher(sql);
	
					while (matcher.find()) {
						String allstr = matcher.group(0);
						String lsql = "";
						
						//��ʾ����Ʒ����
						for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
							if (gpBean != null && gpBean.getIsUsed() == 1&&!gpBean.getPropName().toLowerCase().equals("seq")) {
								//���������õ�
								if(gpBean.getInputBill()==2){
									//������ѡ��
									if(gpBean.getPropName().toLowerCase().equals("color") && (BaseEnv.version==3||BaseEnv.version==11) ){
										lsql += allstr.replaceFirst("\\[GOODSPROP\\]", gpBean.getPropName());
									}else{
										lsql += allstr.replaceFirst("[\\w]*\\.\\[GOODSPROP\\]", gpBean.getPropName()+"_Prop.PropItemName");
									}
									
								}else{
									lsql += allstr.replaceFirst("\\[GOODSPROP\\]", gpBean.getPropName());
								}
							}
						}
						sql = sql.substring(0, sql.indexOf(allstr)) + lsql + sql.substring(sql.indexOf(allstr) + allstr.length());
					}
					
					//����group by 
					pattern = Pattern.compile("[\\s]*[\\w]*\\.\\[([\\w]*)\\][, ]{1}");
	
					matcher = pattern.matcher(sql);
	
					while (matcher.find()) {
						String allstr = matcher.group(0);
						String showSet = matcher.group(1);
						String lsql = "";
						if ("GOODSPROP".equals(showSet)) {
							//��ʾ����Ʒ����
							for (GoodsPropInfoBean gpBean : BaseEnv.propList) { 
								if (gpBean != null && gpBean.getIsUsed() == 1&&!gpBean.getPropName().toLowerCase().equals("seq")) {
									//���������õ�
									if(gpBean.getInputBill()==2){ 
										//������ѡ�� 
										if(gpBean.getPropName().toLowerCase().equals("color") && (BaseEnv.version==3||BaseEnv.version==11) ){
											lsql +=allstr.replaceFirst("\\[" + showSet + "\\]", gpBean.getPropName()) + (allstr.endsWith(",") ? "" : ",")+ " case when isnull("+gpBean.getPropName() + "_Prop.PropItemName,'') = '' then pub_Color_Prop.PropItemName  else "+gpBean.getPropName() + "_Prop.PropItemName end ,";
										}else{
											lsql +=  " "+gpBean.getPropName() + "_Prop.PropItemName ,";
										}
									}else{
										lsql += allstr.replaceFirst("\\[" + showSet + "\\]", gpBean.getPropName()) + (allstr.endsWith(",") ? "" : ",");
									}
								}
							}
						} else {
							for (String[] shows : BaseEnv.reportShowSet) {
								if (showSet.equals(shows[0]) && "1".equals(shows[2])) {
									lsql += allstr.replaceFirst("\\[" + showSet + "\\]", shows[1]) + (allstr.endsWith(",") ? "" : ",");
								}
							}
						}
						if (allstr.endsWith(" ")) {
							lsql = lsql.substring(0, lsql.length() - 1);
						}
						sql = sql.substring(0, sql.indexOf(allstr)) + lsql + sql.substring(sql.indexOf(allstr) + allstr.length());
					}
					
				}
				bean.sql = sql;
			} else if ("charts".equalsIgnoreCase(temp.getNodeName())) {
				/*����ͼ�α�������*/
				NodeList noteList2 = temp.getChildNodes();
				ArrayList<DefineChartBean> chartList = new ArrayList<DefineChartBean>();
				for (int k = 0; k < noteList2.getLength(); k++) {
					Node chart = noteList2.item(k);
					if ("chart".equalsIgnoreCase(chart.getNodeName())) {
						DefineChartBean chartBean = new DefineChartBean();
						NodeList fieldList = chart.getChildNodes();
						for (int j = 0; j < fieldList.getLength(); j++) {
							Node fieldNode = fieldList.item(j);
							if ("chartName".equals(fieldNode.getNodeName())) {
								chartBean.setChartName(fieldNode.getTextContent());
							}
							if ("type".equals(fieldNode.getNodeName())) {
								chartBean.setType(fieldNode.getTextContent());
							}
							if ("xAxis".equals(fieldNode.getNodeName())) {
								NamedNodeMap xAtt = fieldNode.getAttributes();
								if (xAtt.getNamedItem("name") != null) {
									chartBean.setXAxisName(xAtt.getNamedItem("name").getNodeValue().trim());
								} else {
									chartBean.setXAxisName("");
								}
								NodeList xAxis = fieldNode.getChildNodes();
								ArrayList<String> axis = new ArrayList<String>();
								for (int m = 0; m < xAxis.getLength(); m++) {
									Node xNode = xAxis.item(m);
									if ("x".equals(xNode.getNodeName())) {
										axis.add(xNode.getTextContent());
									}
								}
								chartBean.setXAxis(axis);
							}
							if ("yAxis".equals(fieldNode.getNodeName())) {
								NamedNodeMap yAtt = fieldNode.getAttributes();
								if (yAtt.getNamedItem("name") != null) {
									chartBean.setYAxisName(yAtt.getNamedItem("name").getNodeValue().trim());
								} else {
									chartBean.setYAxisName("");
								}
								NodeList yAxis = fieldNode.getChildNodes();
								ArrayList<String> axis = new ArrayList<String>();
								for (int m = 0; m < yAxis.getLength(); m++) {
									Node yNode = yAxis.item(m);
									if ("y".equals(yNode.getNodeName())) {
										axis.add(yNode.getTextContent());
									}
								}
								chartBean.setYAxis(axis);
							}
						}
						chartList.add(chartBean);
					}
				}
				bean.setChartList(chartList);
			}
		}
		return bean;
	}
	
	
	public ArrayList<ReportField> getFieldInfo() {
		return fieldInfo;
	}

	public String getSql() {
		return sql;
	}

	public String getFrom() {
		return from;
	}

	public String getReportId() {
		return reportId;
	}

	public String getReportName() {
		return reportName;
	}

	public ReportField getClassCode() {
		return classCode;
	}

	public ArrayList<ReportField> getConFields() {
		return conFields;
	}

	public ArrayList<ReportField> getDisFields() {
		return disFields;
	}

	public ArrayList<ReportField> getQueryFields() {
		return queryFields;
	}

	public boolean isHaveStat() {
		return haveStat;
	}

	public void setFieldInfo(ArrayList<ReportField> fieldInfo) {
		this.fieldInfo = fieldInfo;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setClassCode(ReportField classCode) {
		this.classCode = classCode;
	}

	public void setConFields(ArrayList<ReportField> conFields) {
		this.conFields = conFields;
	}

	public void setDisFields(ArrayList<ReportField> disFields) {
		this.disFields = disFields;
	}

	public ArrayList<ReportField> getDisFields2() {
		return disFields2;
	}

	public void setDisFields2(ArrayList<ReportField> disFields2) {
		this.disFields2 = disFields2;
	}

	public void setQueryFields(ArrayList<ReportField> queryFields) {
		this.queryFields = queryFields;
	}

	public void setHaveStat(boolean haveStat) {
		this.haveStat = haveStat;
	}

	public boolean isCross() {
		return isCross;
	}

	public void setCross(boolean isCross) {
		this.isCross = isCross;
	}
	/**
	 * ��ȡ����Ľ������
	 * @return���������
	 */
	public ReportField getCrossField() {
		return crossField;
	}
	/**
	 * ���ñ���Ľ�����
	 * @param crossField �������
	 */
	public void setCrossField(ReportField crossField) {
		this.crossField = crossField;
	}

	public ArrayList getChartList() {
		return chartList;
	}

	public void setChartList(ArrayList chartList) {
		this.chartList = chartList;
	}

	public ArrayList getFusionList() {
		return fusionList;
	}

	public void setFusionList(ArrayList fusionList) {
		this.fusionList = fusionList;
	}

	public ArrayList<ReportField> getStatFields() {
		return statFields;
	}

	public void setStatFields(ArrayList<ReportField> statFields) {
		this.statFields = statFields;
	}

	public int getPageSizeE() {
		return pageSizeE;
	}

	public void setPageSizeE(int pageSizeE) {
		this.pageSizeE = pageSizeE;
	}

	public int getPageSizeS() {
		return pageSizeS;
	}

	public String getReportNumber() {
		return reportNumber;
	}

	public void setReportNumber(String reportNumber) {
		this.reportNumber = reportNumber;
	}

	public void setPageSizeS(int pageSizeS) {
		this.pageSizeS = pageSizeS;
	}

	//	public int getVersion() {
	//		return version;
	//	}
	//
	//	public void setVersion(int version) {
	//		this.version = version;
	//	}
	//	
}
