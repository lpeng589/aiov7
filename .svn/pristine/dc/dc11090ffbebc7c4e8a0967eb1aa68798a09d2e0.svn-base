package com.menyi.aio.dyndb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest; 






import org.apache.log4j.Level;
import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.collection.PersistentBag;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.FieldBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.menyi.aio.bean.AccDetailBean;
import com.menyi.aio.bean.AccMainBean;
import com.menyi.aio.bean.AccMainSettingBean;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.AccTypeBean;
import com.menyi.aio.bean.AdviceBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.ColConfigBean;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.bean.EnumerateItemBean;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.web.billNumber.BillNo;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.certTemplate.CertTemplateMgt;
import com.menyi.aio.web.certificate.CertificateBillBean;
import com.menyi.aio.web.certificate.CertificateTemplateBean;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.finance.voucher.VoucherMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.sysAcc.ReCalcucateMgt;
import com.menyi.aio.web.sysAcc.SysAccMgt;
import com.menyi.aio.web.userFunction.ReportData;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.msgcenter.msgif.CancelMsgReq;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseCustomFunction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConfigParse;
import com.menyi.web.util.ConfirmBean;
import com.menyi.web.util.ConvertToSql;
import com.menyi.web.util.DefineSQLBean;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.ErrorMessage;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.Obj;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.TestRW;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

public class DynDBManager  extends AIODBManager{
	public static String addSql = ""; //存放插入的脚本

	public static String mainUpdate = ""; //存放更新主表的脚本

	public static ArrayList childsDel = new ArrayList(); //更新操作时,存放删除子表的脚本

	public static ArrayList childsIns = new ArrayList(); //更新操作时,存放插入子表的脚本

	public static ArrayList delArray = new ArrayList(); //存放删除操作脚本

	public Object lock = new Object();
	
	
	/**
	* 克隆一个DBFieldInfoBean对象
	* 
	* @param fieldBean
	* @return
	*/
	public static ArrayList<DBTableInfoBean> cloneObject(ArrayList<DBTableInfoBean> fieldList) {

		ArrayList<DBTableInfoBean> listField = new ArrayList<DBTableInfoBean>();
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(byteOut);
			out.writeObject(fieldList);
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			listField = (ArrayList<DBTableInfoBean>) in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listField;
	}
	
	  /**
	   * 克隆一个DBFieldInfoBean对象
	   * @param fieldBean
	   * @return
	   */
	public static DBFieldInfoBean cloneObject(DBFieldInfoBean fieldBean) {
		DBFieldInfoBean field = new DBFieldInfoBean();
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(byteOut);
			out.writeObject(fieldBean);
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			field = (DBFieldInfoBean) in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return field ;
	}
	
	/**
	 * 获取表信息列名，如果同一列名存在多个，则最后一个生效。
	 * @param al
	 * @return
	 */
	public static ArrayList<DBFieldInfoBean> distinctList(ArrayList<DBFieldInfoBean> al)
	{
		if(al==null) return null;
		ArrayList<DBFieldInfoBean> ret = new ArrayList<DBFieldInfoBean>();
		ArrayList<String> st = new ArrayList<String>();
		for (DBFieldInfoBean dbFieldInfoBean : al) {
			if(st.contains(dbFieldInfoBean.getFieldName()))
				continue;
			ret.add(dbFieldInfoBean);
			st.add(dbFieldInfoBean.getFieldName());
		}
		return ret;
	}
	
    /**
     * 根据权限,列配置，工作流决定显示哪些字段
     * @param tableName
     * @param tableInfo
     * @param configList
     * @param template
     * @param flowMap
     * @param moduleTable
     * @param languageMap
     * @param mop
     * @param userLastNode
     * @param isDetail
     * @param f_brother
     * @param loginBean 
     * @param scopeRight
     * @return
     */
	public static ArrayList<DBFieldInfoBean> getMainFieldList(String tableName, DBTableInfoBean tableInfo,ArrayList<String> defineField, ArrayList<ColConfigBean> configList,OAWorkFlowTemplate template,HashMap flowMap,HashMap<String,ArrayList<String[]>> moduleTable,
			MOperation mop,String userLastNode,boolean isDetail,String f_brother,LoginBean loginBean,ArrayList scopeRight) throws Exception{
		ArrayList<DBFieldInfoBean> fieldList = new ArrayList<DBFieldInfoBean>();
		ArrayList<DBFieldInfoBean> dbList = new ArrayList<DBFieldInfoBean>(); //表结构中的所有字段
		if (PersistentBag.class.getName().equals(tableInfo.getFieldInfos().getClass().getName())) {
			PersistentBag bag = (PersistentBag) tableInfo.getFieldInfos();
			for (Object object : bag) {
				dbList.add((DBFieldInfoBean) object);
			}
		} else {
			dbList = (ArrayList<DBFieldInfoBean>) tableInfo.getFieldInfos();
		}
		//这是处定义中设置字段类型
		if(defineField != null && defineField.size() > 0){
			ArrayList<DBFieldInfoBean> defList = new ArrayList<DBFieldInfoBean>();
			for(DBFieldInfoBean dfb : dbList){
				boolean found = false;
				for(String s:defineField){
					String fn = s.substring(0,s.indexOf("=")).trim();
					String tp = s.substring(s.indexOf("=")+1).trim();
					if(fn.equalsIgnoreCase(dfb.getTableBean().getTableName()+"."+dfb.getFieldName())||
							fn.equalsIgnoreCase(dfb.getTableBean().getTableName()+"_"+dfb.getFieldName())){
						if(tp.equalsIgnoreCase("ReadOnly")){
							dfb=cloneObject(dfb);
							dfb.setInputType(DBFieldInfoBean.INPUT_ONLY_READ);
							defList.add(dfb);
						}else if(tp.equalsIgnoreCase("Hidden")){
							dfb=cloneObject(dfb);
							dfb.setInputType(DBFieldInfoBean.INPUT_HIDDEN);
							defList.add(dfb);
						}else if(tp.equalsIgnoreCase("notNull")||tp.equalsIgnoreCase("noNull")){
							dfb=cloneObject(dfb);
							dfb.setIsNull((byte)1);
							defList.add(dfb);
						}else{
							defList.add(dfb);
						}
						found=true;
						break;
					}
				}
				if(!found){
					defList.add(dfb);
				}
			}
			dbList=defList;
		}
		if (configList != null && configList.size() > 0) { //存在列配置
			for (ColConfigBean configBean : configList) {
				for (DBFieldInfoBean fieldBean : dbList) {
					if(configBean.getFieldName()==null){
						throw new Exception("列配置configBean.getFieldName()为空，请删除列配置");
					}
					if (configBean.getFieldName().equals("@TABLENAME." + fieldBean.getFieldName()) || configBean.getFieldName().equals(tableName + "." + fieldBean.getFieldName())
							|| configBean.getFieldName().equals(fieldBean.getFieldName())) {
						
						boolean hasF = false;
						for (DBFieldInfoBean fieldBean2 : fieldList) {
							if(fieldBean2.getFieldName().equals(fieldBean.getFieldName())){
								hasF = true;
								break; //已经存在
							}
						}
						if(!hasF){
							if (fieldBean.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT) {
								DBFieldInfoBean field = cloneObject(fieldBean);
								field.setInputType(fieldBean.getInputTypeOld());
								fieldList.add(field);
							} else {
								fieldList.add(fieldBean);
							}
						}
						break;
					}
				}
			}
			//加入列配置中不存在的字段
			for (DBFieldInfoBean fieldBean : dbList) {
				if (3 == fieldBean.getInputType() || 100 == fieldBean.getInputType()) {
					fieldList.add(fieldBean);
				} else {
					boolean isExist = false;
					for (ColConfigBean configBean : configList) {
						if (configBean.getFieldName().contains(".") || configBean.getFieldName().contains("@TABLENAME")) {
							String strName = configBean.getFieldName();
							if (strName.substring(strName.indexOf(".") + 1, strName.length()).equals(fieldBean.getFieldName())) {
								isExist = true;
								break;
							}
						} else {
							if (configBean.getFieldName().equals(fieldBean.getFieldName())) {
								isExist = true;
								break;
							}
						}
					}
					if (!isExist) {
							DBFieldInfoBean field = cloneObject(fieldBean);
							field.setInputType((byte) 3);
							fieldList.add(field);
					}
				}
			}
		} else {
			fieldList = dbList;
		}

		 //一表多模块，且不同模块字段不一致，或名称也不一致
		if (moduleTable != null && moduleTable.get(tableName) != null) {
			ArrayList<String[]> moduleField = moduleTable.get(tableInfo.getTableName());
			ArrayList<DBFieldInfoBean> fieldModules = new ArrayList<DBFieldInfoBean>();
			for (int i = 0; i < fieldList.size(); i++) {
				DBFieldInfoBean dbField = fieldList.get(i);
				String[] mField = null;
				if(moduleField !=null){
					for(String[] mf:moduleField){
						if(mf[0].equals(dbField.getFieldName())){
							mField = mf;
						}
					}
				}
				if (mField != null) {
					KRLanguage language = null;
					HashMap<String,ArrayList<String[]>> tableMap = ((HashMap<String, HashMap<String,ArrayList<String[]>>>)BaseEnv.ModuleField).get(mop.getModuleUrl()+"");
					if(tableMap != null && tableMap.get(tableInfo.getTableName()) != null){
						for(String[] ss : tableMap.get(tableInfo.getTableName())){
							if (ss[2] != null && !ss[2].equals("")) {
								language = new KRLanguage();
								language.putLanguage("zh_CN", ss[2]);
							}
						}
					}

					if (language != null) {
						DBFieldInfoBean field = cloneObject((DBFieldInfoBean) dbField);
						field.setDisplay(language);
						
						if(mField[1].equals("true") && field.getInputType() != DBFieldInfoBean.INPUT_HIDDEN ){
							field.setInputType(DBFieldInfoBean.INPUT_ONLY_READ);
						}
						
						fieldModules.add(field);
					} else {
						if(mField[1].equals("true") && dbField.getInputType() != DBFieldInfoBean.INPUT_HIDDEN ){
							DBFieldInfoBean field = cloneObject((DBFieldInfoBean) dbField);
							field.setInputType(DBFieldInfoBean.INPUT_ONLY_READ);
							fieldModules.add(field);
						}else{
							fieldModules.add(dbField);
						}
					}
				} else {
					DBFieldInfoBean field = cloneObject((DBFieldInfoBean) dbField);
					field.setInputType((byte) 3);
					fieldModules.add(field);
				}
			}
			fieldList= fieldModules;
		}

		
        //工作流有隐藏字段的
        if( template!=null && template.getTemplateStatus()==1){                	
        	String currNodeId = "";
        	String designId = "";
        	if(flowMap!=null){
        		currNodeId = String.valueOf(flowMap.get("currentNode"));
        		designId = String.valueOf(flowMap.get("applyType"));
        	}else{
        		designId = template.getId();
        		currNodeId="-1";
        	}
        	
        	ArrayList fieldListFlow=new ArrayList();
        	WorkFlowDesignBean designBean=BaseEnv.workFlowDesignBeans.get(designId);
        	
        	if(designBean == null){
        		throw new Exception("工作流设计为空，可能原因，启用工作流，但未正确设计流程");
        	}
        	
        	String endAllowField=",";
        	if(isDetail){
        		//如果流程结束，当前登录人是否可以查看某些字段
        		if(designBean.getFlowNodeMap().get(userLastNode)!=null){
            		ArrayList hidFields=designBean.getFlowNodeMap().get(userLastNode).getHiddenFields();
            		if(currNodeId.equals("-1")&&hidFields!=null&&hidFields.size()>0){
            			for(int i=0;i<hidFields.size();i++){
            				endAllowField+=hidFields.get(i)+",";
            			}
            		}
        		}
        	}
        	
        	                	
        	if(designBean.getFlowNodeMap().get(userLastNode)!=null){
            	List<FieldBean> flowList = designBean.getFlowNodeMap().get(userLastNode).getFields();
            	for(int i=0;i<fieldList.size();i++){
            		DBFieldInfoBean field = cloneObject((DBFieldInfoBean)fieldList.get(i));
            		FieldBean fb=null;
            		if(f_brother!=null&&f_brother.length()>0){
            			fb=flowList!=null?GlobalsTool.getFlowField(flowList, tableName+"_"+field.getFieldName()):null;
            		}else{
            			fb=flowList!=null?GlobalsTool.getFlowField(flowList, field.getFieldName()):null;
            		}
            		if(fb!=null && field!=null && field.getInputType()!=100 && field.getInputType()!=3  && field.getInputType()!=6){
            			if(isDetail){
            				if(!(endAllowField.length()>0&&endAllowField.contains(fb.getFieldName()))){
            					if(fb.getInputType()==field.INPUT_HIDDEN){
                					field.setInputType(fb.getInputType());
                				}
            				}	                				
            			}else{
            				if(fb.getInputType()>0){
            					//如果原类型是只读，隐藏等类型不能改变
            	        		if(field.getInputType()!= DBFieldInfoBean.INPUT_ONLY_READ && field.getInputType()!= DBFieldInfoBean.INPUT_HIDDEN_INPUT){
            	        			field.setInputTypeOld(field.getInputType());
            	        		}
	                			field.setInputType(fb.getInputType());
            				}
                			
                			if(fb.isNotNull()){
                				field.setIsNull(Byte.parseByte("1"));
                			}
            			}
            		}
            		fieldListFlow.add(field);
                }
            	fieldList=fieldListFlow;	                	
        	}
        }

		//权限处理，设置各字段的是否隐藏  
		ArrayList fieldListRight = new ArrayList();
		for (int i = 0; i < fieldList.size(); i++) {
			//权限判断
			DBFieldInfoBean field = (DBFieldInfoBean) fieldList.get(i);
			DBFieldInfoBean newField = null;

			String fieldIdentity = field.getFieldIdentityStr();
			if (!"".equals(fieldIdentity) && fieldIdentity != null) {
				//获取登录用户的所有角色            				
				if (fieldIdentity.equals("priceIdentifier") || fieldIdentity.equals("AmountIdentifier")) { //判断是否隐藏成本价
					fieldIdentity = "1";
				} else if (fieldIdentity.equals("SPriceIdentifier") || fieldIdentity.equals("SAmountIdentifier")) { //判断是否隐藏销售价
					fieldIdentity = "2";
				} else if (fieldIdentity.equals("Customer")) { //判断是否隐藏客户
					fieldIdentity = "3";
				} else if (fieldIdentity.equals("Supplier")) { //判断是否隐藏供应商
					fieldIdentity = "4";
				} else if (fieldIdentity.equals("CompanyCodeIdentifier")) { //判断是否隐藏往来单位
					fieldIdentity = "5";
				}

				if (("," + loginBean.getHiddenField()).contains("," + fieldIdentity)) {
					newField = cloneObject(field);
					newField.setInputType(DBFieldInfoBean.INPUT_HIDDEN);
				}
			}

			if (scopeRight != null && scopeRight.size() > 0) {
				for (Object sco : scopeRight) {
					LoginScopeBean lsb = (LoginScopeBean) sco;
					if (lsb.getFlag().equals("4")) {
						if (lsb.getTableName().trim().equalsIgnoreCase(tableName) && lsb.getFieldName().trim().equalsIgnoreCase(field.getFieldName()) && "1".equals(lsb.getScopeValue())) {
							newField = cloneObject(field);
							newField.setInputType(DBFieldInfoBean.INPUT_HIDDEN);
						} else if (lsb.getTableName().trim().equalsIgnoreCase(tableName) && lsb.getFieldName().trim().equalsIgnoreCase(field.getFieldName()) && "1".equals(lsb.getEscopeValue())) {
							newField = cloneObject(field);
							newField.setInputTypeOld(newField.getInputType());
							newField.setInputType(DBFieldInfoBean.INPUT_ONLY_READ);
						}
					}
				}
			}
			if (newField == null) {
				fieldListRight.add(field);
			} else {
				fieldListRight.add(newField);
			}
		}
		fieldList = fieldListRight;

		return  fieldList;
	}
	


	public Result getTableMapping(String sourceTable, String targetTable) {
		final Result rs = new Result();
		final String souT = sourceTable;
		final String tarT = targetTable;

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						String querysql = "select childTableField,mostlyTableField from tbltableMapped where mostlyTable='" + souT + "' and childTable='" + tarT + "'";
						try {
							Statement cs = conn.createStatement();
							BaseEnv.log.debug(querysql);
							ResultSet rset = cs.executeQuery(querysql);
							HashMap map = new HashMap();
							int count = 0;
							while (rset.next()) {
								count++;
								map.put(rset.getString(1), rset.getString(2));
							}
							rs.setRealTotal(count);
							rs.setRetVal(map);
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + querysql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);

		return rs;
	}

	/**
	 * 获取会计科目表中会计期间年、是否复制标示
	 * @return
	 */
	public Result getAccPeriodOverYear() {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						String querysql = "select AccYear,AccOverYear from  tblAccPeriod group by Accyear,AccOverYear";
						try {
							Statement cs = conn.createStatement();
							BaseEnv.log.debug(querysql);
							ResultSet rset = cs.executeQuery(querysql);
							HashMap map = new HashMap();
							while (rset.next()) {
								map.put(rset.getString(1), rset.getString(2));
							}
							rs.setRetVal(map);
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + querysql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);

		return rs;
	}

	public static boolean getViewRight(String reportName, String reportfieldName, ArrayList scopeRight, String fieldIdentity, LoginBean loginBean) {

		boolean viewRight = true;
		DBFieldInfoBean fileBean = new DBFieldInfoBean();
		if ("popselect".equals(fieldIdentity)) { //判断是否是弹出框
			//如果是弹出框，reportName为空,reportFieldName传入display，或fieldName			
			fileBean = GlobalsTool.getFieldBean(reportfieldName);
			if (fileBean != null) {
				fieldIdentity = fileBean.getFieldIdentityStr();
			} else {
				fieldIdentity = "";
			}
		}
		if (!"".equals(fieldIdentity) && fieldIdentity != null) {
			if (fieldIdentity.equals("priceIdentifier") || fieldIdentity.equals("AmountIdentifier")) { //判断是否隐藏成本价
				fieldIdentity = "1";
			} else if (fieldIdentity.equals("SPriceIdentifier") || fieldIdentity.equals("SAmountIdentifier")) { //判断是否隐藏销售价
				fieldIdentity = "2";
			} else if (fieldIdentity.equals("Customer")) { //判断是否隐藏客户
				fieldIdentity = "3";
			} else if (fieldIdentity.equals("Supplier")) { //判断是否隐藏供应商
				fieldIdentity = "4";
			} else if (fieldIdentity.equals("CompanyCodeIdentifier")) { //判断是否隐藏往来单位
				fieldIdentity = "5";
			}

			if (("," + loginBean.getHiddenField()).contains("," + fieldIdentity))
				viewRight = false;
		}

		if (scopeRight != null && !"popselect".equals(fieldIdentity)) {

			int index = reportfieldName.indexOf(".");
			boolean flag = true;
			if (index > 0) {
				ArrayList<String[]> list = ConfigParse.getTableFieldByReportField(reportfieldName);
				for (int j = 0; j < list.size(); j++) {
					String[] obj = list.get(j);

					for (Object sco : scopeRight) {
						LoginScopeBean lsb = (LoginScopeBean) sco;
						if (lsb.getFlag().equals("4") && "1".equals(lsb.getScopeValue())) {
							if(lsb.getFieldName().indexOf(".") >0   ) { //报表
								if(lsb.getTableName().trim().equalsIgnoreCase(reportName) && 
										lsb.getFieldName().trim().equalsIgnoreCase(obj[0]+"."+obj[1])){
									return false;
								}
							}else {
								if (lsb.getTableName().trim().equalsIgnoreCase(obj[0]) && 
									lsb.getFieldName().trim().equalsIgnoreCase(obj[1]) ) { //单据								
									return false;
								}
							}
						}
					}
				}
			}else{
				for (Object sco : scopeRight) {
					LoginScopeBean lsb = (LoginScopeBean) sco;
					if (lsb.getFlag().equals("4") && "1".equals(lsb.getScopeValue())) {
						if(lsb.getFieldName().indexOf(".") >0   ) { //报表
							if(lsb.getTableName().trim().equalsIgnoreCase(reportName) && 
									lsb.getFieldName().trim().equalsIgnoreCase(reportName+"."+reportfieldName)){
								return false;
							}
						}else {
							if (lsb.getTableName().trim().equalsIgnoreCase(reportName) && 
								lsb.getFieldName().trim().equalsIgnoreCase(reportfieldName) ) { //单据								
								return false;
							}
						}
					}
				}
			}
		}
		return viewRight;
	}
	/**
	 * 本方法在引用单据时如果弹出窗字段有内容，执行此方法取默认弹出窗的值,返回所有getViewFields要返回的值 ，因为引用弹出窗有些值是引用过来的，不需要默认值
	 * @param fi
	 * @param value
	 * @param allTables
	 * @param sunCompany
	 * @param targetValues
	 * @param values
	 * @param userId
	 * @param childSunCompany
	 * @param kr
	 * @param scopeList
	 * @return
	 */
	public Result getRefViewValues(final DBFieldInfoBean fi, final String value, final Hashtable allTables, final String sunCompany, final HashMap targetValues, final HashMap values, final String userId,
			final boolean childSunCompany, final KRLanguageQuery kr, final ArrayList scopeList) {
		final Result rss = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String refsql = "";
						try {
							Statement cs = conn.createStatement();
							//找出主表选择的关联数据							
							refsql = getRefSql(fi,false, value, sunCompany, allTables, childSunCompany, values, userId);
							if (refsql != null) {
								refsql = refsql.replaceAll("(?i)\\bEMPRIGHT\\b", ""); //去掉语句中的部门临时标志
								refsql = refsql.replaceAll("(?i)\\bNORIGHT\\b", ""); //去掉语句中的部门临时标志
								
								ResultSet crset = cs.executeQuery(refsql);
								DBFieldInfoBean bean;
								if (crset.next()) {
									for (int j = 0; j < fi.getSelectBean().getViewFields().size(); j++) {
										PopField pop = (PopField) fi.getSelectBean().getViewFields().get(j);
										
										//取多语言
										bean = GlobalsTool.getFieldBean(pop.getFieldName().substring(0, pop.getFieldName().indexOf(".")), pop.getFieldName().substring(
												pop.getFieldName().indexOf(".") + 1));
										if (bean == null && pop.getDisplay() !=null && pop.getDisplay().indexOf(".") > -1) {
											bean = GlobalsTool
													.getFieldBean(pop.getDisplay().substring(0, pop.getDisplay().indexOf(".")), pop.getDisplay().substring(pop.getDisplay().indexOf(".") + 1));
										}
										if (bean != null && kr != null && (bean.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || bean.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE)) {
											kr.addLanguageId(crset.getObject(j + 1) == null ? "" : crset.getObject(j + 1).toString());
										}
										Object val =  crset.getObject(j + 1);
										if(val instanceof String){
											val = val==null?null:GlobalsTool.replaceSpecLitter(String.valueOf(val));
										}
										//本方法一般只是主表取默认值用，暂时明细表没有默认值
										//这里如果是返回字段 要取ParentName，否则取asName 并转化成界面字段赋值
										String fn = pop.getParentName(); 
										if(fn == null || fn.length() ==0){
											fn = pop.asName;
										}
										if(fn.indexOf("@TABLENAME") > -1){
											fn = fn.replaceAll("@TABLENAME", fi.getTableBean().getTableName());
										}
										if(pop.type ==1 &&  fn.startsWith(fi.getTableBean().getTableName()+".")){//保存字段不包括表名，主表
											fn = fn.substring(fi.getTableBean().getTableName().length() +1) ; 
										}
										targetValues.put(fn, val);
									}
								}

							}

						} catch (Exception ex) {
							rss.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + refsql, ex);
							return;
						}
					}
				});
				return rss.getRetCode();
			}
		});
		rss.setRetCode(retCode);
		return rss;

	}
	
    /**
     * 本方法在新增单据时如果弹出窗字段有内容，执行此方法取默认弹出窗的值,返回所有getReturnFields要返回的值 
     * @param fi
     * @param value
     * @param allTables
     * @param sunCompany
     * @param targetValues
     * @param values
     * @param userId
     * @param childSunCompany
     * @param kr
     * @param scopeList
     * @return
     */
	public Result getRefReturnValues(final DBFieldInfoBean fi, final String value, final Hashtable allTables, final String sunCompany, final HashMap targetValues, final HashMap values, final String userId,
			final boolean childSunCompany, final KRLanguageQuery kr, final ArrayList scopeList) {
		final Result rss = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String refsql = "";
						try {
							Statement cs = conn.createStatement();
							//找出主表选择的关联数据
							refsql = getRefSql(fi,true, value, sunCompany, allTables, childSunCompany, values, userId);
							if (refsql != null) {
								refsql = refsql.replaceAll("(?i)\\bEMPRIGHT\\b", ""); //去掉语句中的部门临时标志
								refsql = refsql.replaceAll("(?i)\\bNORIGHT\\b", ""); //去掉语句中的部门临时标志
								
								ResultSet crset = cs.executeQuery(refsql);
								DBFieldInfoBean bean;
								if (crset.next()) {
									for (int j = 0; j < fi.getSelectBean().getReturnFields().size(); j++) {
										PopField pop = (PopField) fi.getSelectBean().getReturnFields().get(j);
										
										//取多语言
										bean = GlobalsTool.getFieldBean(pop.getFieldName().substring(0, pop.getFieldName().indexOf(".")), pop.getFieldName().substring(
												pop.getFieldName().indexOf(".") + 1));
										if (bean == null && pop.getDisplay() !=null && pop.getDisplay().indexOf(".") > -1) {
											bean = GlobalsTool
													.getFieldBean(pop.getDisplay().substring(0, pop.getDisplay().indexOf(".")), pop.getDisplay().substring(pop.getDisplay().indexOf(".") + 1));
										}
										if (bean != null && kr != null && (bean.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || bean.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE)) {
											kr.addLanguageId(crset.getObject(j + 1) == null ? "" : crset.getObject(j + 1).toString());
										}
										Object val =  crset.getObject(j + 1);
										if(val instanceof String){
											val = val==null?null:GlobalsTool.replaceSpecLitter(String.valueOf(val));
										}
										//本方法一般只是主表取默认值用，暂时明细表没有默认值
										//这里如果是返回字段 要取ParentName，否则取asName 并转化成界面字段赋值
										String fn = pop.getParentName(); 
										if(fn == null || fn.length() ==0){
											fn = pop.asName;
										}
										if(fn.indexOf("@TABLENAME") > -1){
											fn = fn.replaceAll("@TABLENAME", fi.getTableBean().getTableName());
										}
										if(pop.type ==1 &&  fn.startsWith(fi.getTableBean().getTableName()+".")){//保存字段不包括表名，主表
											fn = fn.substring(fi.getTableBean().getTableName().length() +1) ; 
										}
										targetValues.put(fn, val);
									}
								}

							}

						} catch (Exception ex) {
							rss.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + refsql, ex);
							return;
						}
					}
				});
				return rss.getRetCode();
			}
		});
		rss.setRetCode(retCode);
		return rss;

	}

	public Result queryWorkFlowNodeName(final String tableName, final String id) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						String querysql = "";
						try {
							Statement st = conn.createStatement();
							querysql = "select workFlowNodeName from " + tableName + " where id='" + id + "'";
							ResultSet rss = st.executeQuery(querysql);
							if (rss.next()) {
								rs.setRetVal(rss.getString(1));
								if (rs.getRetVal() == null) {
									rs.setRetVal("");
								}
							}
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + querysql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.retCode = retCode;

		return rs;
	}

	/**
	 * 查询详情
	 * @param tableName String
	 * @param allTables Hashtable
	 * @param keyId int
	 * @return Result
	 */
	public Result detail(final String tableName, final Hashtable allTables, final String keyId, final String sunCompany, final Hashtable props, final String userId, final boolean childSunCompany,
			final Object copy, final Connection conn) {
		final Result rs = new Result();
		final HashMap values = new HashMap();
		final KRLanguageQuery languageQuery = new KRLanguageQuery();
		rs.retVal = values;
		final Result rsPop = new Result();
		String querysql = "";
		try {
			HashMap detMap = new HashMap();
			querysql = BaseEnv.popDisSentence.get(tableName) == null ? "" : BaseEnv.popDisSentence.get(tableName).toString();
			if (querysql.length() > 0) {
				//解释变量
				//tableName,fieldName,reportView,billView,popSel,keyword,popupView
				
				Pattern pattern = Pattern.compile("([\\w]+)\\.\\[([\\w]+)\\]",Pattern.CASE_INSENSITIVE);
		    	Matcher matcher = pattern.matcher(querysql);	
		        while (matcher.find()){
		        	String mall = matcher.group();
		        	String preFN = matcher.group(1);
		        	String showSet = matcher.group(2);
		        	String showStr = "";
		        	for (String[] shows : BaseEnv.reportShowSet) { 
						if (showSet.equals(shows[0]) && "1".equals(shows[3])) {
							showStr +=","+preFN+"."+shows[1]+ " as "+preFN+"_"+shows[1];
						}
                    }
		        	if(showStr.length() > 0){
		        		showStr = showStr.substring(1);
		        	}
		        	querysql = querysql.substring(0,querysql.indexOf(mall))+showStr + querysql.substring(querysql.indexOf(mall)+mall.length());
		    	}
				
				querysql += " where  " + tableName + ".id='" + keyId + "'";
				Statement cs = conn.createStatement();
				ResultSet rset = cs.executeQuery(querysql);

				java.sql.ResultSetMetaData rsm = rset.getMetaData();
				int colCount = rsm.getColumnCount();
				while (rset.next()) {
					HashMap fieldMap = new HashMap();
					for (int i = 1; i <= colCount; i++) {
						String columnName = rsm.getColumnName(i);
						int type = rsm.getColumnType(i);
						if (type == Types.NUMERIC) {
							BigDecimal bigDec = rset.getBigDecimal(i);
							if (bigDec != null && bigDec.doubleValue() == 0) {
								fieldMap.put(columnName, bigDec.doubleValue());
							} else {
								fieldMap.put(columnName, bigDec);
							}
						} else
							fieldMap.put(columnName, rset.getString(i));
						if (fieldMap.get(columnName) == null) {
							fieldMap.put(columnName, "");
						}
					}

					if (fieldMap.get("id") != null && fieldMap.get("id").toString().length() > 0) {
						detMap.put(fieldMap.get("id"), fieldMap);
					}
				}
			}
			rsPop.setRetVal(detMap);
		} catch (Exception ex) {
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			BaseEnv.log.error("Query data Error :" + querysql, ex);
		}
		if (rsPop.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return rsPop;
		}
		//查主表
		final DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, tableName);
		String insertTableName = this.getInsertTableName(tableInfo.getTableName());//处理CRM多模板插入数据库的表名
		querysql = " select a.*,b.empFullName as createByName,c.empFullName as lastUpdateByName from  " + insertTableName
				+ " a join tblEmployee b on a.createBy=b.id join tblEmployee c on a.lastUpdateBy=c.id " + " where a.id = '" + keyId + "'";
		try {
			HashMap detMap = null;
			if (rsPop.retVal != null) {
				detMap = (HashMap) rsPop.retVal;
			}
			Statement cs = conn.createStatement();

			ResultSet rset = cs.executeQuery(querysql);
			while (rset.next()) {
				for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
					DBFieldInfoBean fi = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
					if(fi.getInputType()==DBFieldInfoBean.INPUT_CUT_LINE){
						continue;
					}
					Object fiValue;
					if (fi.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
						BigDecimal bigDec = rset.getBigDecimal(fi.getFieldName());
						if (bigDec != null && bigDec.doubleValue() == 0) {
							fiValue = bigDec.doubleValue();
						} else {
							fiValue = bigDec;
						}
					} else if (fi.getFieldType() == DBFieldInfoBean.FIELD_INT) {
						fiValue = rset.getInt(fi.getFieldName());
					} else if (fi.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fi.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE) {
						fiValue = rset.getString(fi.getFieldName().trim());
						languageQuery.addLanguageId((String) fiValue);
					} else {
						fiValue = rset.getObject(fi.getFieldName().trim());
					}
					if (fiValue != null && fiValue.toString().length() > 0) {
						List sysSCeList = ((EnumerateBean) BaseEnv.enumerationMap.get("SpecialCharacter")).getEnumItem();//特殊字符转换
						if (sysSCeList != null) {
							for (Object obj : sysSCeList) {
								EnumerateItemBean item = (EnumerateItemBean) obj;
								String itemValue = item.getEnumValue();
								String itemName = item.getDisplay().get("zh_CN");
								if (fiValue.toString().contains(itemValue)) {
									fiValue = fiValue.toString().replaceAll(item.getEnumValue(), itemName);
								}
							}
						}
					}
					values.put(fi.getFieldName(), fiValue);
					//BaseEnv.log.debug("----------------fieldName="+fi.getFieldName()+";value="+values.get(fi.getFieldName()));
				}
				values.put("createByName", rset.getString("createByName"));
				values.put("lastUpdateByName", rset.getString("lastUpdateByName"));
			}

			//找出主表选择的关联数据
			for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
				DBFieldInfoBean fi = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
				if ((fi.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE || fi.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE) && values.get(fi.getFieldName()) != null) {
					GoodsPropInfoBean gp = (GoodsPropInfoBean) props.get(fi.getFieldName());
					//查询对应表的数据
					if (values.get(fi.getFieldName()) == null || values.get(fi.getFieldName()).toString().length() == 0 || (gp != null && gp.getIsSequence() == 1)) {
						continue;
					}
					Iterator iter = detMap.values().iterator();
					HashMap fieldValue = null;
					if (iter.hasNext()) {
						fieldValue = (HashMap) iter.next();
					}

					if (detMap != null && fieldValue != null) {
						for (int j = 0; j < fi.getSelectBean().getViewFields().size(); j++) {
							PopField posf = ((PopField) fi.getSelectBean().getViewFields().get(j));
							String t = posf.fieldName.substring(0, posf.fieldName.indexOf("."));
							String f = posf.fieldName.substring(posf.fieldName.indexOf(".") + 1);

							DBFieldInfoBean fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
							if (fb == null && posf.getDisplay() != null && posf.getDisplay().length() > 0 
									&& posf.display.indexOf(".") > -1) {
								t = posf.display.substring(0, posf.display.indexOf("."));
								f = posf.display.substring(posf.display.indexOf(".") + 1);
								fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
							}
							if (fb != null && (fb.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fi.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE)) {
								//找出关联表的多语言字段
								String lid = fieldValue.get(posf.asName.replace(".", "_")) == null ? "" : fieldValue.get(posf.asName.replace(".", "_")).toString();
								languageQuery.addLanguageId(lid);
								values.put(posf.asName, lid);
							} else {
								values.put(posf.asName, fieldValue.get(posf.asName.replace(".", "_")) == null ? "" : fieldValue.get(posf.asName.replace(".", "_")).toString());
							}
						}
					} else {
						String refsql = getRefSql(fi,false, values.get(fi.getFieldName()).toString(), sunCompany, allTables, childSunCompany, values, userId);

						if (refsql != null) {
							refsql = refsql.replaceAll("(?i)\\bEMPRIGHT\\b", ""); //去掉语句中的部门临时标志
							refsql = refsql.replaceAll("(?i)\\bNORIGHT\\b", ""); //去掉语句中的部门临时标志
							BaseEnv.log.debug(refsql);
							ResultSet crset = cs.executeQuery(refsql);
							if (crset.next()) {
								for (int j = 0; j < fi.getSelectBean().getViewFields().size(); j++) {
									PopField posf = ((PopField) fi.getSelectBean().getViewFields().get(j));
									String t = posf.fieldName.substring(0, posf.fieldName.indexOf("."));
									String f = posf.fieldName.substring(posf.fieldName.indexOf(".") + 1);
									DBFieldInfoBean fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
									if (fb == null && posf.getDisplay() != null && posf.getDisplay().length() > 0 
											&& posf.display.indexOf(".") > -1) {
										t = posf.display.substring(0, posf.display.indexOf("."));
										f = posf.display.substring(posf.display.indexOf(".") + 1);
										fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
									}
									if (fb != null && (fb.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fi.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE)) {
										//找出关联表的多语言字段
										String lid = crset.getString(j + 1);
										languageQuery.addLanguageId(lid);
										values.put(posf.asName, lid);
									} else {
										values.put(posf.asName, crset.getObject(j + 1));
									}
								}
							}
						}
					}
				}
			}

		} catch (Exception ex) {
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			ex.printStackTrace();
			BaseEnv.log.error("Query data Error :" + ex);
		}
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return rs;
		}
		//找子表
		final ArrayList childTableList = DDLOperation.getChildTables(tableInfo.getTableName(), allTables);
		for (int i = 0; childTableList != null && i < childTableList.size(); i++) {
			final DBTableInfoBean childTable = (DBTableInfoBean) childTableList.get(i);
			insertTableName = this.getInsertTableName(childTable.getTableName());//处理CRM多模板插入数据库的表名
			final ArrayList childList = new ArrayList();
			//看id是否是整形的，如果是整形，则要按id排序
			boolean isInt = false;
			List<DBFieldInfoBean> dlist = (List) childTable.getFieldInfos();
			for (DBFieldInfoBean db : dlist) {
				if (db.getFieldName().equalsIgnoreCase("id")) {
					isInt = db.getFieldType() == DBFieldInfoBean.FIELD_INT;
				}
			}
			values.put("TABLENAME_" + childTable.getTableName(), childList);
			final String childquerysql = " select * from  " + insertTableName + " where f_ref = '" + keyId + "' order by " + (isInt ? "id" : "detOrderNo");
			
			try {
				HashMap detMap = null;
				if (rsPop.retVal != null) {
					detMap = (HashMap) rsPop.retVal;
				}
				Statement cs = conn.createStatement();
				BaseEnv.log.debug(childquerysql);
				ResultSet rset = cs.executeQuery(childquerysql);

				while (rset.next()) {
					HashMap cm = new HashMap();
					for (int j = 0; j < childTable.getFieldInfos().size(); j++) {
						DBFieldInfoBean fi = (DBFieldInfoBean) childTable.getFieldInfos().get(j);
						Object fiValue;
						if (fi.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
							BigDecimal bigDec = rset.getBigDecimal(fi.getFieldName());
							if (bigDec != null && bigDec.doubleValue() == 0) {
								fiValue = bigDec.doubleValue();
							} else {
								fiValue = bigDec;
							}
						} else if (fi.getFieldType() == DBFieldInfoBean.FIELD_INT) {
							fiValue = rset.getInt(fi.getFieldName());
						} else if (fi.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fi.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE) {
							fiValue = rset.getString(fi.getFieldName().trim());
							languageQuery.addLanguageId((String) fiValue);
						} else {
							fiValue = rset.getObject(fi.getFieldName().trim());
						}
						if (copy != null && copy.toString().equals("copy") && fi.getIsCopy() == 0) {
							if (fi.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE && (fi.getDefaultValue() == null || (fi.getDefaultValue() != null && fi.getDefaultValue().length() == 0))) {
								cm.put(fi.getFieldName(), 0);
							} else {
								cm.put(fi.getFieldName(), fi.getDefaultValue() == null ? "" : fi.getDefaultValue());
							}
						} else {
							cm.put(fi.getFieldName(), fiValue);
						}
					}
					childList.add(cm);
				}
				//找出主表选择的关联数据
				for (int m = 0; m < childTable.getFieldInfos().size(); m++) {
					DBFieldInfoBean fi = (DBFieldInfoBean) childTable.getFieldInfos().get(m);
					GoodsPropInfoBean gp = (GoodsPropInfoBean) props.get(fi.getFieldName());
					if (fi.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE
							 || (fi.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT && fi.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE)
							 || (fi.getInputType() == DBFieldInfoBean.INPUT_ONLY_READ && fi.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE)) {
						//查询对应表的数据
						for (int k = 0; k < childList.size(); k++) {
							HashMap cm = (HashMap) childList.get(k);
							if (cm.get(fi.getFieldName()) == null || cm.get(fi.getFieldName()).toString().length() == 0 || (gp != null && gp.getIsSequence() == 1)) {
								continue;
							}

							if (detMap != null && detMap.get(cm.get("id")) != null) {
								HashMap fieldValue = (HashMap) detMap.get(cm.get("id"));
								for (int j = 0; fi.getSelectBean() != null && j < fi.getSelectBean().getViewFields().size(); j++) {
									PopField posf = ((PopField) fi.getSelectBean().getViewFields().get(j));
									String t = posf.fieldName.substring(0, posf.fieldName.indexOf("."));
									String f = posf.fieldName.substring(posf.fieldName.indexOf(".") + 1);
									DBFieldInfoBean fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
									if (fb == null && posf.getDisplay() != null && posf.getDisplay().length() > 0 
											&& posf.display.indexOf(".") > -1) {
										t = posf.display.substring(0, posf.display.indexOf("."));
										f = posf.display.substring(posf.display.indexOf(".") + 1);
										fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
									}
									if (fb != null && (fb.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fb.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE)) {
										//找出关联表的多语言字段
										String lid = fieldValue.get(posf.asName.replace(".", "_")) == null ? "" : fieldValue.get(posf.asName.replace(".", "_")).toString();
										languageQuery.addLanguageId(lid);
										cm.put(posf.asName, lid);
									} else {
										cm.put(posf.asName, fieldValue.get(posf.asName.replace(".", "_")) == null ? "" : fieldValue.get(posf.asName.replace(".", "_")).toString());
									}
								}
							} else {
								//组织弹出框所需要的查询条件值
								HashMap fieldValues = new HashMap();
								for (int j = 0; j < fi.getSelectBean().getTableParams().size(); j++) {
									String name = fi.getSelectBean().getTableParams().get(j).toString();
									if (name.indexOf("_") > 0) {
										name = childTable.getTableName() + "_" + name.substring(name.indexOf("_") + 1);
										fieldValues.put(name, cm.get(name.substring(name.indexOf("_") + 1)));
									} else {
										fieldValues.put(name, values.get(name));
									}
								}

								for (int j = 0; j < fi.getSelectBean().getSaveFields().size(); j++) {
									PopField pop = (PopField) fi.getSelectBean().getSaveFields().get(j);
									String name = childTable.getTableName() + "_" + pop.getParentName().substring(pop.getParentName().indexOf(".") + 1);
									fieldValues.put(name, cm.get(pop.getParentName().substring(pop.getParentName().indexOf(".") + 1)));
								}

								String refsql = getRefSql(fi,false, cm.get(fi.getFieldName()).toString(), sunCompany, allTables, childSunCompany, fieldValues, userId);
								if (refsql == null) {
									continue;
								}
								refsql = refsql.replaceAll("(?i)\\bEMPRIGHT\\b", ""); //去掉语句中的部门临时标志
								refsql = refsql.replaceAll("(?i)\\bNORIGHT\\b", ""); //去掉语句中的部门临时标志
								Statement ccs = conn.createStatement();
								BaseEnv.log.debug(refsql);
								ResultSet crset = ccs.executeQuery(refsql);
								if (crset.next()) {
									for (int j = 0; j < fi.getSelectBean().getViewFields().size(); j++) {
										PopField posf = ((PopField) fi.getSelectBean().getViewFields().get(j));
										String t = posf.fieldName.substring(0, posf.fieldName.indexOf("."));
										String f = posf.fieldName.substring(posf.fieldName.indexOf(".") + 1);
										DBFieldInfoBean fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
										if (fb == null && posf.getDisplay() != null && posf.getDisplay().length() > 0 
												&& posf.display.indexOf(".") > -1) {
											t = posf.display.substring(0, posf.display.indexOf("."));
											f = posf.display.substring(posf.display.indexOf(".") + 1);
											fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
										}
										if (fb!=null &&(fb.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fb.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE)) {
											//找出关联表的多语言字段
											String lid = crset.getString(j + 1);
											languageQuery.addLanguageId(lid);
											cm.put(posf.asName, lid);
										} else {
											cm.put(posf.asName, crset.getObject(j + 1));
										}
									}
								}
							}
						}
					}

				}
			} catch (Exception ex) {
				rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
				BaseEnv.log.error("Query data Error :" + childquerysql, ex);
				ex.printStackTrace();
			}
		}
		try {
			HashMap lm = languageQuery.query(conn);
			values.put("LANGUAGEQUERY", lm);
		} catch (Exception ex) {
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			BaseEnv.log.error("Query data Error :" + languageQuery, ex);
			ex.printStackTrace();
		}
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return rs;
		}
		return rs;
	}

	/**
	 * 查询详情
	 * @param tableName String
	 * @param allTables Hashtable
	 * @param keyId int
	 * @return Result
	 */
	public Result detail(final String tableName, final Hashtable allTables, final String keyId, final String sunCompany, final Hashtable props, final String userId, final boolean childSunCompany,
			 final Object copy) {
		final Result rs = new Result();
		//文件导入标准格式时，传入的keyId=example,这里不需查询数据库,只需给出空值就行
		if ("example".equals(keyId)) {
			HashMap map = new HashMap();
			map.put("id", keyId);
			map.put("LANGUAGEQUERY", new HashMap());
			DBTableInfoBean tb = (DBTableInfoBean) allTables.get(tableName);
			for (DBFieldInfoBean fb : tb.getFieldInfos()) {
				map.put(fb.getFieldName(), "");
			}
			ArrayList childTableList = DDLOperation.getChildTables(tableName, allTables);
			for (int i = 0; childTableList != null && i < childTableList.size(); i++) {
				DBTableInfoBean ctb = (DBTableInfoBean) childTableList.get(i);
				map.put("TABLENAME_" + ctb.getTableName(), new ArrayList());
			}

			rs.retVal = map;
			return rs;
		}
		final HashMap values = new HashMap();
		final KRLanguageQuery languageQuery = new KRLanguageQuery();
		rs.retVal = values;
		final Result rsPop = new Result();

		//在配置文件中查找此单据中弹出框显示语句
		int retCode1 = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						String querysql = "";
						try {
							HashMap detMap = new HashMap();
							querysql = BaseEnv.popDisSentence.get(tableName) == null ? "" : BaseEnv.popDisSentence.get(tableName).toString();
							if (querysql.length() > 0) {
								//解释变量
								//tableName,fieldName,reportView,billView,popSel,keyword,popupView
								
								Pattern pattern = Pattern.compile("([\\w]+)\\.\\[([\\w]+)\\]",Pattern.CASE_INSENSITIVE);
						    	Matcher matcher = pattern.matcher(querysql);	
						        while (matcher.find()){
						        	String mall = matcher.group();
						        	String preFN = matcher.group(1);
						        	String showSet = matcher.group(2);
						        	String showStr = "";
						        	for (String[] shows : BaseEnv.reportShowSet) { 
										if (showSet.equals(shows[0]) && "1".equals(shows[3])) {
											showStr +=","+preFN+"."+shows[1]+ " as "+preFN+"_"+shows[1];
										}
			                        }
						        	if(showStr.length() > 0){
						        		showStr = showStr.substring(1);
						        	}
						        	querysql = querysql.substring(0,querysql.indexOf(mall))+showStr + querysql.substring(querysql.indexOf(mall)+mall.length());
						    	}
								
								
								
								querysql += " where  " + tableName + ".id='" + keyId + "'";
								Statement cs = conn.createStatement();
								ResultSet rset = cs.executeQuery(querysql);

								java.sql.ResultSetMetaData rsm = rset.getMetaData();
								int colCount = rsm.getColumnCount();
								while (rset.next()) {
									HashMap fieldMap = new HashMap();
									for (int i = 1; i <= colCount; i++) {
										String columnName = rsm.getColumnName(i);
										int type = rsm.getColumnType(i);
										if (type == Types.NUMERIC) {
											BigDecimal bigDec = rset.getBigDecimal(i);
											if (bigDec != null && bigDec.doubleValue() == 0) {
												fieldMap.put(columnName, bigDec.doubleValue());
											} else {
												fieldMap.put(columnName, bigDec);
											}
										} else{
											fieldMap.put(columnName, GlobalsTool.replaceSpecLitter(rset.getString(i)));
										}
										if (fieldMap.get(columnName) == null) {
											fieldMap.put(columnName, "");
										}
									}

									if (fieldMap.get("id") != null && fieldMap.get("id").toString().length() > 0) {
										detMap.put(fieldMap.get("id"), fieldMap);
									}
								}
							}
							rsPop.setRetVal(detMap);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + querysql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rsPop.retCode = retCode1;
		if (retCode1 != ErrorCanst.DEFAULT_SUCCESS) {
			return rsPop;
		}

		//查主表
		final DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, tableName);

		String insertTableName = this.getInsertTableName(tableInfo.getTableName());//处理CRM多模板插入数据库的表名

		final String querysql = " select a.*,b.empFullName as createByName,c.empFullName as lastUpdateByName from  " + insertTableName
				+ " a left join tblEmployee b on a.createBy=b.id left join tblEmployee c on a.lastUpdateBy=c.id " + " where a.id = '" + keyId + "'";

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							HashMap detMap = null;
							if (rsPop.retVal != null) {
								detMap = (HashMap) rsPop.retVal;
							}
							Statement cs = conn.createStatement();

							ResultSet rset = cs.executeQuery(querysql);
							while (rset.next()) {
								for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
									DBFieldInfoBean fi = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
									if(fi.getInputType()==DBFieldInfoBean.INPUT_CUT_LINE){
										continue;
									}
									Object fiValue;
									if (fi.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
										BigDecimal bigDec = rset.getBigDecimal(fi.getFieldName());
										if (bigDec != null && bigDec.doubleValue() == 0) {
											fiValue = bigDec.doubleValue();
										} else {
											fiValue = bigDec;
										}
									} else if (fi.getFieldType() == DBFieldInfoBean.FIELD_INT) {
										fiValue = rset.getInt(fi.getFieldName());
									} else if (fi.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fi.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE) {
										fiValue = rset.getString(fi.getFieldName().trim());
										languageQuery.addLanguageId((String) fiValue);
									} else {
										fiValue = rset.getObject(fi.getFieldName().trim());

									}
									
									if (fiValue != null && fiValue.toString().length() > 0) {
										List sysSCeList = ((EnumerateBean) BaseEnv.enumerationMap.get("SpecialCharacter")).getEnumItem();//特殊字符转换

										if (sysSCeList != null) {
											for (Object obj : sysSCeList) {
												EnumerateItemBean item = (EnumerateItemBean) obj;
												String itemValue = item.getEnumValue();
												String itemName = item.getDisplay().get("zh_CN");
												if (fiValue.toString().contains(itemValue)) {
													fiValue = fiValue.toString().replaceAll(item.getEnumValue(), itemName);
												}
											}
										}
									}
									
									if(fiValue instanceof String){
										fiValue = GlobalsTool.replaceSpecLitter(String.valueOf(fiValue));
									}
									
									values.put(fi.getFieldName(), fiValue);
								}
								values.put("createByName", rset.getString("createByName"));
								values.put("lastUpdateByName", rset.getString("lastUpdateByName"));
							}

							//找出主表选择的关联数据
							for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
								DBFieldInfoBean fi = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
								if ((fi.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE || fi.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE) && values.get(fi.getFieldName()) != null) {
									GoodsPropInfoBean gp = (GoodsPropInfoBean) props.get(fi.getFieldName());
									//查询对应表的数据
									if (values.get(fi.getFieldName()) == null || values.get(fi.getFieldName()).toString().length() == 0 || (gp != null && gp.getIsSequence() == 1)) {
										continue;
									}
									Iterator iter = detMap.values().iterator();
									HashMap fieldValue = null;
									if (iter.hasNext()) {
										fieldValue = (HashMap) iter.next();
									}

									if (detMap != null && fieldValue != null) {
										for (int j = 0; fi.getSelectBean() != null && j < fi.getSelectBean().getViewFields().size(); j++) {
											PopField posf = ((PopField) fi.getSelectBean().getViewFields().get(j));
											String t = posf.fieldName.substring(0, posf.fieldName.indexOf("."));
											String f = posf.fieldName.substring(posf.fieldName.indexOf(".") + 1);

											DBFieldInfoBean fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
											
											if (fb == null && posf.getDisplay() != null && posf.getDisplay().length() > 0 
													&& posf.display.indexOf(".") > -1) {
												t = posf.display.substring(0, posf.display.indexOf("."));
												f = posf.display.substring(posf.display.indexOf(".") + 1);
												fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
											}
											if (fb != null && (fb.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fb.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE)) {
												//找出关联表的多语言字段
												String lid = fieldValue.get(posf.asName.replace(".", "_")) == null ? "" : fieldValue.get(posf.asName.replace(".", "_")).toString();
												languageQuery.addLanguageId(lid);
												values.put(posf.asName, lid);
											} else {
												values.put(posf.asName, fieldValue.get(posf.asName.replace(".", "_")) == null ? "" : fieldValue.get(posf.asName.replace(".", "_")).toString());
											}
										}
									} else {
										String refsql = getRefSql(fi,false, values.get(fi.getFieldName()).toString(), sunCompany, allTables, childSunCompany, values, userId);

										if (refsql != null) {
											refsql = refsql.replaceAll("(?i)\\bEMPRIGHT\\b", ""); //去掉语句中的部门临时标志
											refsql = refsql.replaceAll("(?i)\\bNORIGHT\\b", ""); //去掉语句中的部门临时标志
											
											Statement ccs = conn.createStatement();
											try {
												BaseEnv.log.debug("DynDBManager.detail 查询详情时，查询关联表数据：表="+tableName+";字段="+fi.getFieldName()+";弹出窗="+fi.getSelectBean().getName()+";sql="+refsql);
												ResultSet crset = cs.executeQuery(refsql);
												if (crset.next()) {
													for (int j = 0; j < fi.getSelectBean().getViewFields().size(); j++) {
														PopField posf = ((PopField) fi.getSelectBean().getViewFields().get(j));
														String t = posf.fieldName.substring(0, posf.fieldName.indexOf("."));
														String f = posf.fieldName.substring(posf.fieldName.indexOf(".") + 1);
														DBFieldInfoBean fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
														if (fb == null && posf.getDisplay() != null && posf.getDisplay().length() > 0 
																&& posf.display.indexOf(".") > -1) {
															t = posf.display.substring(0, posf.display.indexOf("."));
															f = posf.display.substring(posf.display.indexOf(".") + 1);
															fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
														}
														if (fb != null && ( fb.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fb.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE)) {
															//找出关联表的多语言字段
															String lid = crset.getString(j + 1);
															languageQuery.addLanguageId(lid);
															values.put(posf.asName, lid);
														} else {
															Object fiValue = crset.getObject(j + 1);
															if(fiValue instanceof String){
																fiValue = GlobalsTool.replaceSpecLitter(String.valueOf(fiValue));
															}
															values.put(posf.asName, fiValue);
														}
													}
												}
											} catch (Exception ex) {
												rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
												rs.setRetVal(ex.getMessage());
												BaseEnv.log.error("DynDBManager.detail 查询详情时，查询关联表数据 Error 表="+tableName+";字段="+fi.getFieldName()+";弹出窗="+fi.getSelectBean().getName()+";sql="+refsql , ex);
												return;
											}
										}
									}
								}
							}

						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							rs.setRetVal(ex.getMessage());
							BaseEnv.log.error("DynDBManager.detail 查询详情时 ERROR :"+querysql , ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.retCode = retCode;
		if (retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return rs;
		}

		//找子表
		final ArrayList childTableList = DDLOperation.getChildTables(tableInfo.getTableName(), allTables);
		for (int i = 0; childTableList != null && i < childTableList.size(); i++) {
			final DBTableInfoBean childTable = (DBTableInfoBean) childTableList.get(i);
			final ArrayList childList = new ArrayList();
			values.put("TABLENAME_" + childTable.getTableName(), childList);

			//看id是否是整形的，如果是整形，则要按id排序
			boolean isInt = false;
			List<DBFieldInfoBean> dlist = (List) childTable.getFieldInfos();
			for (DBFieldInfoBean db : dlist) {
				if (db.getFieldName().equalsIgnoreCase("id")) {
					isInt = db.getFieldType() == DBFieldInfoBean.FIELD_INT;
				}
			}
			insertTableName = this.getInsertTableName(childTable.getTableName());//处理CRM多模板插入数据库的表名
			final String childquerysql = " select * from  " + insertTableName + " where f_ref = '" + keyId + "' order by " + (isInt ? "id" : "detOrderNo");

			retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection connection) throws SQLException {
							Connection conn = connection;
							try {
								HashMap detMap = null;
								if (rsPop.retVal != null) {
									detMap = (HashMap) rsPop.retVal;
								}
								Statement cs = conn.createStatement();
								BaseEnv.log.debug(childquerysql);
								ResultSet rset = cs.executeQuery(childquerysql);

								while (rset.next()) {
									HashMap cm = new HashMap();
									for (int i = 0; i < childTable.getFieldInfos().size(); i++) {
										DBFieldInfoBean fi = (DBFieldInfoBean) childTable.getFieldInfos().get(i);
										if(fi.getInputType()==DBFieldInfoBean.INPUT_CUT_LINE){
											continue;
										}
										Object fiValue;
										if (fi.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
											BigDecimal bigDec = rset.getBigDecimal(fi.getFieldName());
											if (bigDec != null && bigDec.doubleValue() == 0) {
												fiValue = bigDec.doubleValue();
											} else {
												fiValue = bigDec;
											}
										} else if (fi.getFieldType() == DBFieldInfoBean.FIELD_INT) {
											fiValue = rset.getInt(fi.getFieldName());
										} else if (fi.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fi.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE) {
											fiValue = rset.getString(fi.getFieldName().trim());
											languageQuery.addLanguageId((String) fiValue);
										} else {
											fiValue = rset.getObject(fi.getFieldName().trim());

										}
										if(fiValue instanceof String){
											fiValue = GlobalsTool.replaceSpecLitter(String.valueOf(fiValue));
										}
										if (copy != null && copy.toString().equals("copy") && fi.getIsCopy() == 0) {
											if (fi.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE
													&& (fi.getDefaultValue() == null || (fi.getDefaultValue() != null && fi.getDefaultValue().length() == 0))) {
												cm.put(fi.getFieldName(), 0);
											} else {
												cm.put(fi.getFieldName(), fi.getDefaultValue() == null ? "" : fi.getDefaultValue());
											}
										} else {
											cm.put(fi.getFieldName(), fiValue);
										}
									}
									//                                  物料清单特殊处理
									if ((cm.get("id") != null || !"".equals(cm.get("id"))) && !childTable.getPerantTableName().contains("CRMClientInfo")) {
										for (int j = 0; j < childTableList.size(); j++) {
											final DBTableInfoBean childs = (DBTableInfoBean) childTableList.get(j);
											final ArrayList childsList = new ArrayList();
											final String childsql = " select count(*) as num from  " + childs.getTableName() + " where f_ref = '" + cm.get("id") + "'";
											PreparedStatement psr = conn.prepareStatement(childsql);
											ResultSet rsr = psr.executeQuery();
											if (rsr.next()) {
												Integer num = rsr.getInt("num");
												if (num > 0) {
													values.put("TABLENAME_" + cm.get("id") + "_" + childs.getTableName(), childsList);
													final String childquerysql = "select * from " + childs.getTableName() + " where f_ref = '" + cm.get("id") + "' order by detOrderNo";
													try {
														HashMap detMaps = null;
														if (rsPop.retVal != null) {
															detMaps = (HashMap) rsPop.retVal;
														}
														psr = conn.prepareStatement(childquerysql);
														BaseEnv.log.debug(childquerysql);
														rsr = psr.executeQuery();
														while (rsr.next()) {
															HashMap cms = new HashMap();
															for (int i = 0; i < childs.getFieldInfos().size(); i++) {
																DBFieldInfoBean fis = (DBFieldInfoBean) childs.getFieldInfos().get(i);
																Object fiValue;
																if (fis.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
																	BigDecimal bigDec = rsr.getBigDecimal(fis.getFieldName());
																	if (bigDec != null && bigDec.doubleValue() == 0) {
																		fiValue = bigDec.doubleValue();
																	} else {
																		fiValue = bigDec;
																	}
																} else if (fis.getFieldType() == DBFieldInfoBean.FIELD_INT) {
																	fiValue = rsr.getInt(fis.getFieldName());
																} else if (fis.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fis.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE) {
																	fiValue = rsr.getString(fis.getFieldName().trim());
																	languageQuery.addLanguageId((String) fiValue);
																} else {
																	fiValue = rsr.getObject(fis.getFieldName().trim());
																}

																if (copy != null && copy.toString().equals("copy") && fis.getIsCopy() == 0) {
																	if (fis.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE
																			&& (fis.getDefaultValue() == null || (fis.getDefaultValue() != null && fis.getDefaultValue().length() == 0))) {
																		cms.put(fis.getFieldName(), 0);
																	} else {
																		cms.put(fis.getFieldName(), fis.getDefaultValue() == null ? "" : fis.getDefaultValue());
																	}
																} else {
																	cms.put(fis.getFieldName(), fiValue);
																}
															}
															childsList.add(cms);
														}
														//找出主表选择的关联数据
														for (int l = 0; l < childs.getFieldInfos().size(); l++) {
															DBFieldInfoBean fi = (DBFieldInfoBean) childs.getFieldInfos().get(l);
															GoodsPropInfoBean gp = (GoodsPropInfoBean) props.get(fi.getFieldName());
															if (fi.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE
																	|| (fi.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT && fi.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE)
																	|| (fi.getInputType() == DBFieldInfoBean.INPUT_ONLY_READ && fi.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE)) {
																//查询对应表的数据
																for (int k = 0; k < childsList.size(); k++) {
																	HashMap cms = (HashMap) childsList.get(k);
																	if (cms.get(fi.getFieldName()) == null || cms.get(fi.getFieldName()).toString().length() == 0
																			|| (gp != null && gp.getIsSequence() == 1)) {
																		continue;
																	}

																	if (detMaps != null && detMaps.get(cms.get("id")) != null) {
																		HashMap fieldValue = (HashMap) detMaps.get(cms.get("id"));
																		for (int q = 0; fi.getSelectBean() != null && q < fi.getSelectBean().getViewFields().size(); q++) {
																			PopField posf = ((PopField) fi.getSelectBean().getViewFields().get(q));
																			String t = posf.fieldName.substring(0, posf.fieldName.indexOf("."));
																			String f = posf.fieldName.substring(posf.fieldName.indexOf(".") + 1);
																			DBFieldInfoBean fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
																			if (fb == null && posf.getDisplay() != null && posf.getDisplay().length() > 0 
																					&& posf.display.indexOf(".") > -1) {
																				t = posf.display.substring(0, posf.display.indexOf("."));
																				f = posf.display.substring(posf.display.indexOf(".") + 1);
																				fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
																			}
																			if (fb !=null && ( fb.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fb.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE)) {
																				//找出关联表的多语言字段
																				String lid = fieldValue.get(posf.asName.replace(".", "_")) == null ? "" : fieldValue.get(posf.asName.replace(".", "_"))
																						.toString();
																				languageQuery.addLanguageId(lid);
																				cms.put(posf.asName, lid);
																			} else {
																				cms.put(posf.asName, fieldValue.get(posf.asName.replace(".", "_")) == null ? "" : fieldValue.get(
																						posf.asName.replace(".", "_")).toString());
																			}
																		}
																	} else {
																		//组织弹出框所需要的查询条件值
																		HashMap fieldValues = new HashMap();
																		for (int q = 0; q < fi.getSelectBean().getTableParams().size(); q++) {
																			String name = fi.getSelectBean().getTableParams().get(q).toString();
																			if (name.indexOf("_") > 0) {
																				name = childTable.getTableName() + "_" + name.substring(name.indexOf("_") + 1);
																				fieldValues.put(name, cms.get(name.substring(name.indexOf("_") + 1)));
																			} else {
																				fieldValues.put(name, values.get(name));
																			}
																		}

																		for (int q = 0; q < fi.getSelectBean().getSaveFields().size(); q++) {
																			PopField pop = (PopField) fi.getSelectBean().getSaveFields().get(q);
																			String name = childTable.getTableName() + "_" + pop.getParentName().substring(pop.getParentName().indexOf(".") + 1);
																			fieldValues.put(name, cms.get(pop.getParentName().substring(pop.getParentName().indexOf(".") + 1)));
																		}

																		String refsql = getRefSql(fi,false, cms.get(fi.getFieldName()).toString(), sunCompany, allTables, childSunCompany, fieldValues,
																				userId);
																		if (refsql == null) {
																			continue;
																		}
																		Statement ccs = conn.createStatement();
																		BaseEnv.log.debug(refsql);
																		ResultSet crset = ccs.executeQuery(refsql);
																		if (crset.next()) {
																			for (int q = 0; q < fi.getSelectBean().getViewFields().size(); q++) {

																				PopField posf = ((PopField) fi.getSelectBean().getViewFields().get(q));
																				String t = posf.fieldName.substring(0, posf.fieldName.indexOf("."));
																				String f = posf.fieldName.substring(posf.fieldName.indexOf(".") + 1);
																				DBFieldInfoBean fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
																				if (fb == null && posf.getDisplay() != null && posf.getDisplay().length() > 0 
																						&& posf.display.indexOf(".") > -1) {
																					t = posf.display.substring(0, posf.display.indexOf("."));
																					f = posf.display.substring(posf.display.indexOf(".") + 1);
																					fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
																				}
																				if (fb != null && (fb.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fb.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE)) {
																					//找出关联表的多语言字段
																					String lid = crset.getString(q + 1);
																					languageQuery.addLanguageId(lid);
																					cms.put(posf.asName, lid);
																				} else {
																					cms.put(posf.asName, crset.getObject(q + 1));
																				}
																			}
																		}
																	}
																}
															}

														}
													} catch (Exception e) {
														// TODO: handle exception
													}
												}
											}
										}
									}

									childList.add(cm);
								}
								//找出主表选择的关联数据
								for (int i = 0; i < childTable.getFieldInfos().size(); i++) {
									DBFieldInfoBean fi = (DBFieldInfoBean) childTable.getFieldInfos().get(i);
									GoodsPropInfoBean gp = (GoodsPropInfoBean) props.get(fi.getFieldName());
									if (fi.getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE
											|| (fi.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT && fi.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE)
											|| (fi.getInputType() == DBFieldInfoBean.INPUT_ONLY_READ && fi.getInputTypeOld() == DBFieldInfoBean.INPUT_MAIN_TABLE)) {
										//查询对应表的数据
										for (int k = 0; k < childList.size(); k++) {
											HashMap cm = (HashMap) childList.get(k);
											if (cm.get(fi.getFieldName()) == null || cm.get(fi.getFieldName()).toString().length() == 0 || (gp != null && gp.getIsSequence() == 1)) {
												continue;
											}
											if (detMap != null && detMap.get(cm.get("id")) != null) {
												HashMap fieldValue = (HashMap) detMap.get(cm.get("id"));
												for (int j = 0; fi.getSelectBean() != null && j < fi.getSelectBean().getViewFields().size(); j++) {
													PopField posf = ((PopField) fi.getSelectBean().getViewFields().get(j));
													String t = posf.fieldName.substring(0, posf.fieldName.indexOf("."));
													String f = posf.fieldName.substring(posf.fieldName.indexOf(".") + 1);
													DBFieldInfoBean fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
													if (fb == null && posf.getDisplay() != null && posf.getDisplay().length() > 0 
															&& posf.display.indexOf(".") > -1) {
														t = posf.display.substring(0, posf.display.indexOf("."));
														f = posf.display.substring(posf.display.indexOf(".") + 1);
														fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
													}
													if (fb !=null && (fb.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fb.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE)) {
														//找出关联表的多语言字段
														String lid = fieldValue.get(posf.asName.replace(".", "_")) == null ? "" : fieldValue.get(posf.asName.replace(".", "_")).toString();
														languageQuery.addLanguageId(lid);
														cm.put(posf.asName, lid);
													} else {
														cm.put(posf.asName, fieldValue.get(posf.asName.replace(".", "_")) == null ? "" : fieldValue.get(posf.asName.replace(".", "_")).toString());
													}
												}
											} else {
												//组织弹出框所需要的查询条件值
												HashMap fieldValues = new HashMap();
												if(fi.getSelectBean() == null){
													rs.retCode = ErrorCanst.DEFAULT_FAILURE;
													rs.retVal = "弹窗字段"+fi.getFieldName()+"，弹窗"+fi.getInputValue()+"不存在";
													return;
												}
												for (int j = 0; j < fi.getSelectBean().getTableParams().size(); j++) {
													String name = fi.getSelectBean().getTableParams().get(j).toString();
													if (name.indexOf("_") > 0) {
														name = childTable.getTableName() + "_" + name.substring(name.indexOf("_") + 1);
														fieldValues.put(name, cm.get(name.substring(name.indexOf("_") + 1)));
													} else {
														fieldValues.put(name, values.get(name));
													}
												}

												for (int j = 0; j < fi.getSelectBean().getSaveFields().size(); j++) {
													PopField pop = (PopField) fi.getSelectBean().getSaveFields().get(j);
													String name = childTable.getTableName() + "_" + pop.getParentName().substring(pop.getParentName().indexOf(".") + 1);
													fieldValues.put(name, cm.get(pop.getParentName().substring(pop.getParentName().indexOf(".") + 1)));
												}
												String refsql = getRefSql(fi,false, cm.get(fi.getFieldName()).toString(), sunCompany, allTables, childSunCompany, fieldValues, userId);
												if (refsql == null) {
													continue;
												}
												Statement ccs = conn.createStatement();
												refsql = refsql.replaceAll("(?i)\\bEMPRIGHT\\b", ""); //去掉语句中的部门临时标志
												refsql = refsql.replaceAll("(?i)\\bNORIGHT\\b", ""); //去掉语句中的部门临时标志
												
												BaseEnv.log.debug(refsql);
												ResultSet crset = ccs.executeQuery(refsql);
												if (crset.next()) {
													for (int j = 0; j < fi.getSelectBean().getViewFields().size(); j++) {

														PopField posf = ((PopField) fi.getSelectBean().getViewFields().get(j));
														String t = posf.fieldName.substring(0, posf.fieldName.indexOf("."));
														String f = posf.fieldName.substring(posf.fieldName.indexOf(".") + 1);
														DBFieldInfoBean fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
														if (fb == null && posf.getDisplay() != null && posf.getDisplay().length() > 0 
																&& posf.display.indexOf(".") > -1) {
															t = posf.display.substring(0, posf.display.indexOf("."));
															f = posf.display.substring(posf.display.indexOf(".") + 1);
															fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);
														}
														if (fb != null && ( fb.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fb.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE )) {
															//找出关联表的多语言字段
															String lid = crset.getString(j + 1);
															languageQuery.addLanguageId(lid);
															cm.put(posf.asName, lid);
														} else {
															Object fiValue = crset.getObject(j + 1);
															if(fiValue instanceof String){
																fiValue = GlobalsTool.replaceSpecLitter(String.valueOf(fiValue));
															}
															cm.put(posf.asName, fiValue);
														}
													}
												}
											}
										}
									}

								}
							} catch (Exception ex) {
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								BaseEnv.log.error("Query data Error :" + childquerysql, ex);
								return;
							}
						}
					});
					return rs.getRetCode();
				}
			});
			rs.retCode = retCode;
		}
		rs.retCode = retCode;
		if (retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return rs;
		}

		retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						//多语言执行
						HashMap lm = languageQuery.query(conn);
						values.put("LANGUAGEQUERY", lm);
						//把多语言进行转换
						for(Object o :lm.values()){
							KRLanguage kr = (KRLanguage)o;
							for(String k :kr.map.keySet()){
								String v = kr.map.get(k);
								kr.map.put(k, GlobalsTool.replaceSpecLitter(v));
							}
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.retCode = retCode;

		return rs;
	}

	public Result getLanguageId(final KRLanguageQuery languageQuery, final HashMap values) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						//多语言执行
						HashMap lm = languageQuery.query(conn);
						values.putAll(lm);
					}
				});
				return rs.getRetCode();
			}
		});
		rs.retCode = retCode;
		return rs;
	}

	/**
	 * 获取参考sql
	 * 在添加和修改时，须通过关联字段读取需关联显示的数据，此数据不保存在主表中
	 * @param fi DBFieldInfoBean
	 * @param values String
	 * @return String
	 */
	public String getRefSql(DBFieldInfoBean fi,boolean allReturn, String values, String sunCompany, Hashtable allTables, boolean childSunCompany, HashMap fieldValues, String userId) throws Exception {

		if (fi.getSelectBean() == null || fi.getSelectBean().getViewFields().size() == 0) {
			return null;
		}
		String refsql = " select ";//这里大部分只能显示viewFields字段，不能是包括salveField字段，因为单据详情调用的是这个方法，如果读取savefield字段可能把存在表中的字段给冲掉
		if(allReturn){//但是对于新增界面，取弹出窗的默认值 ，则必须全部returnFields都要取，且有asName的要按asName取值，saveField要按parent 才能保证回填到正确字段上去
			for (int k = 0; k < fi.getSelectBean().getReturnFields().size(); k++) {
				PopField field = (PopField) fi.getSelectBean().getReturnFields().get(k);
				refsql += field.fieldName + ","; 
			}
		}else{
			for (int k = 0; k < fi.getSelectBean().getViewFields().size(); k++) {
				PopField field = (PopField) fi.getSelectBean().getViewFields().get(k);
				refsql += field.fieldName + ",";
			}
		}

		refsql = refsql.substring(0, refsql.length() - 1);
		//查询from的位置
		int fromS = 0;
		for (int i = 0; i < fi.getSelectBean().getAllFields().size(); i++) {
			PopField rf = (PopField) fi.getSelectBean().getAllFields().get(i);
			if (rf.getFieldName().contains(" from ")) {
				int indexS = fi.getSelectBean().getPopsql().indexOf(rf.getFieldName());
				if (indexS > fromS) {
					fromS = indexS + rf.getFieldName().length();
				}
			}
		}
		HashMap sysParamMap = null;
		HashMap sessParamMap = null;
		HashMap codeParamMap = new HashMap();
		codeParamMap = ConfigParse.getCodeParam(fi.getSelectBean().getCodeParams(), null);
		sysParamMap = ConfigParse.getSystemParam(fi.getSelectBean().getSysParams(), sunCompany);
		sessParamMap = ConfigParse.getSessParam(userId, fi.getSelectBean().getSessParams());
		HashMap queryParamMap = new HashMap();

		String frc = fi.getSelectBean().getPopsql();
		int ind = frc.indexOf("from ", fromS);
		if(ind == -1){
			ind = frc.indexOf("from\n", fromS);
		}
		if ((frc.indexOf("from\n", fromS)) < ind && (frc.indexOf("from\n", fromS)) != -1) {
			ind = frc.indexOf("from\n", fromS);
		}
		if ((frc.indexOf("from(", fromS)) < ind && (frc.indexOf("from(", fromS)) != -1) {
			ind = frc.indexOf("from(", fromS);
		}
		frc = " " + frc.substring(ind);
		refsql += frc;
		if (refsql.length() > 0) {
			refsql = refsql.replace("@TABLENAME", fi.getTableBean().getTableName());
			try{
				refsql = ConfigParse.parseConditionSentencePutParam(refsql, fieldValues, sysParamMap, queryParamMap, sessParamMap, codeParamMap, new HashMap());
			}catch(Exception e){
				throw new Exception("弹了窗语句错误：字段："+fi.getFieldName()+";弹出窗："+fi.getSelectBean().getName()+":"+e.getMessage());
			}
		}

		ArrayList sFields = fi.getSelectBean().getSaveFields();
		if (refsql.contains("group by")) {
			refsql += " having 1=1 and " + fi.getSelectBean().getRelationKey().fieldName + " = '" + values + "'";
		} else {
			//如果已经存在where 			
			int wherepos = pos(refsql, "where");
			if(fi.getSelectBean()==null){  
				throw new Exception ("字段"+fi.getFieldName()+"弹出窗不存在");
			}
			if(fi.getSelectBean().getRelationKey()==null){
				throw new Exception ("字段"+fi.getFieldName()+"弹出窗"+fi.getSelectBean().getName()+"未设置RelationKey");
			}
			if (wherepos > 0 ) {
				refsql += " and " + fi.getSelectBean().getRelationKey().fieldName + " = '" + values + "'";
			} else {
				refsql += " where 1=1 and " + fi.getSelectBean().getRelationKey().fieldName + " = '" + values + "'";
			}
		}
		String prefix = "";
		if (fi.getTableBean().getTableType() != 0) {
			prefix = fi.getTableBean().getTableName() + "_";
		}
		
		for(int i=0;i<sFields.size();i++){
        	PopField fv=(PopField)sFields.get(i);
    		String parentName=fv.getParentName();
    		String fName=parentName.substring(parentName.indexOf(".")+1);
    		String fieldName=ConfigParse.parseConditionSentencePutParam(fv.getFieldName(),fieldValues, sysParamMap, queryParamMap, sessParamMap,
                    codeParamMap,new HashMap());
    		if(fv.hidden&&!fv.getFieldName().equals(fi.getSelectBean().getRelationKey().fieldName)&&fieldValues.get(prefix+fName)!=null&&!fieldValues.get(prefix+fName).equals("")){
    			refsql+=" and " +fieldName + " = '" +fieldValues.get(prefix+fName) +"'";
    		}
		}

		String condition = fi.getSelectBean().getCondition();
		if (condition != null && condition.length() > 0) {
			if (condition.indexOf("@TABLENAME") >= 0) {
				condition = condition.replaceAll("@TABLENAME", fi.getTableBean().getTableName());
			}
			if (("tblBOMDet".equals(fi.getTableBean().getTableName()) || "tblBOMDetail".equals(fi.getTableBean().getTableName())) && fieldValues.get("tblBOMDet_GoodsCode") == null) {
				condition = condition.replaceAll(fi.getTableBean().getTableName() + "_", "");
			}
			condition = ConfigParse.parseConditionSentencePutParam(condition, fieldValues, sysParamMap, queryParamMap, sessParamMap, codeParamMap, new HashMap());
			condition = condition == null ? "" : condition.trim();
			if (condition.length() > 0 && !"( )".equals(condition)) {
				refsql += "   and " + condition;
			}
		}
        if(refsql.indexOf("@TOPID") > 0){ //@TOPID多单引用时，一级弹出窗的结果，这里如果出现这个符号，说明是多单引用的明细弹出窗要替换这个值
			//一级有多个值。
			Pattern pattern = Pattern.compile("([\\w\\.]*)[\\s]*=[\\s]*'@TOPID'");
            Matcher matcher = pattern.matcher(refsql);
           
            while (matcher.find()) {
                String cfd = matcher.group(1);
                String all = matcher.group();
                refsql = refsql.substring(0,refsql.indexOf(all))+"1=1"+refsql.substring(refsql.indexOf(all) + all.length());
            }
    	}
		
		BaseEnv.log.debug("DynDBManager.getRefSql:弹出窗"+fi.getSelectBean().getName()+":"+refsql);
		return refsql;
	}

	/**
	 * 设置关联表的默认值
	 * @param tableName String
	 * @param allTables Hashtable
	 * @param keyId int
	 * @return Result
	 * @throws Exception 
	 */
	public void getDefRefValue(final DBFieldInfoBean fi, final HashMap values, final Hashtable sessionSet, final String userId, final Hashtable allTables, String tableName) throws Exception {
		String defPopValue = "";
		String defPopDis = "";
		if (tableName.length() > 0)
			tableName = tableName + "_";

		if (fi.getDefaultValue().indexOf(";") > 0) {
			defPopValue = fi.getDefaultValue().substring(0, fi.getDefaultValue().indexOf(";"));
			defPopDis = fi.getDefaultValue().substring(fi.getDefaultValue().indexOf(";") + 1);
		} else {
			defPopValue = fi.getDefaultValue();
		}

		if (defPopValue.indexOf("@Sess:") >= 0) {
			String name = defPopValue.substring(defPopValue.indexOf("@Sess:") + "@Sess:".length());
			values.put(tableName + fi.getFieldName(), GlobalsTool.replaceSpecLitter(String.valueOf(sessionSet.get(name))));
		} else {
			values.put(tableName + fi.getFieldName(), GlobalsTool.replaceSpecLitter(defPopValue));
		}

		if (defPopDis.length() >= 0 || "f_brother".equals(fi.getDefaultValue())) {
			if ("f_brother".equals(fi.getDefaultValue())) {
				values.put(fi.getFieldName(), values.get("f_brother"));
			}

			if (fi.getSelectBean() == null)
				return;

			List saves = fi.getSelectBean().getReturnFields();

			for (int i = 0; i < saves.size(); i++) {
				PopField po = (PopField) saves.get(i);
				if (po.display != null && po.display.length() > 0 && po.parentDisplay) {
					if (defPopDis.indexOf("@Sess:") >= 0) {
						String name = defPopDis.substring(defPopDis.indexOf("@Sess:") + "@Sess:".length());
						if (tableName.length() > 0) {
							String asName = po.getAsName().replace(".", "_");
							values.put(tableName + asName, GlobalsTool.replaceSpecLitter(String.valueOf(sessionSet.get(name))));
						} else {
							values.put(po.getAsName(), GlobalsTool.replaceSpecLitter(String.valueOf(sessionSet.get(name))));
						}
						break;
					} else {
						if ("f_brother".equals(fi.getDefaultValue())) {
							defPopDis = getDefaultValue((String) values.get("f_brother"), po.getFieldName());
						}
						values.put(po.getAsName(), GlobalsTool.replaceSpecLitter(defPopDis));
					}
				}
			}
		}
	}
	
	/**
	 * 设置明细表的默认值，因为明细表的列是在define.vjs中，是固化的，所以@Sess:参数不是先翻译成值
	 * @param tableName String
	 * @param allTables Hashtable
	 * @param keyId int
	 * @return Result
	 * @throws Exception 
	 */
	public void getChildDefRefValue(final DBFieldInfoBean fi, final HashMap values, final Hashtable sessionSet, final String userId, final Hashtable allTables, String tableName) throws Exception {
		
		String defPopValue = "";
		String defPopDis = "";
		if (tableName.length() > 0)
			tableName = tableName + "_";

		if (fi.getDefaultValue().indexOf(";") > 0) {
			defPopValue = fi.getDefaultValue().substring(0, fi.getDefaultValue().indexOf(";"));
			defPopDis = fi.getDefaultValue().substring(fi.getDefaultValue().indexOf(";") + 1);
		} else {
			defPopValue = fi.getDefaultValue();
		}
		

		
		if (defPopValue.indexOf("@Sess:") >= 0) {
			String name = defPopValue.substring(defPopValue.indexOf("@Sess:") + "@Sess:".length());
			values.put(tableName + fi.getFieldName(), "Sess_"+name);
		} else {
			values.put(tableName + fi.getFieldName(), defPopValue);
		}
		if(defPopValue.indexOf("@Sess:StockCode") >= 0 && ("false".equals(GlobalsTool.getSysSetting("ManyStockSales")) ||  "false".equals(GlobalsTool.getSysSetting("ManyStockBuy")) ) ){
			//未启用多仓库，则明细表仓库字段不设默认值，这里不好区分是否是采购类还是出库类单据多仓库，所以凡是有一个未启用多仓库，则明细表的默认仓库无效
			values.put(tableName + fi.getFieldName(), "");
		}

		if (defPopDis.length() >= 0 || "f_brother".equals(fi.getDefaultValue())) {
			if ("f_brother".equals(fi.getDefaultValue())) {
				values.put(fi.getFieldName(), values.get("f_brother"));
			}

			if (fi.getSelectBean() == null)
				return;

			List saves = fi.getSelectBean().getReturnFields();

			for (int i = 0; i < saves.size(); i++) {
				PopField po = (PopField) saves.get(i);
				if (po.display != null && po.display.length() > 0 && po.parentDisplay) {
					if (defPopDis.indexOf("@Sess:") >= 0) {
						String name = defPopDis.substring(defPopDis.indexOf("@Sess:") + "@Sess:".length());
						if (tableName.length() > 0) {
							String asName = po.getAsName().replace(".", "_");
							values.put(tableName + asName, "Sess_"+name);
						} else {
							values.put(po.getAsName(), "Sess_"+name);
						}
						if(defPopValue.indexOf("@Sess:StockCode") >= 0 && ("false".equals(GlobalsTool.getSysSetting("ManyStockSales")) ||  "false".equals(GlobalsTool.getSysSetting("ManyStockBuy")) ) ){
							//未启用多仓库，则明细表仓库字段不设默认值，这里不好区分是否是采购类还是出库类单据多仓库，所以凡是有一个未启用多仓库，则明细表的默认仓库无效
							values.put(po.getAsName(), "");
						}
						break;
					} else {
						if ("f_brother".equals(fi.getDefaultValue())) {
							defPopDis = getDefaultValue((String) values.get("f_brother"), po.getFieldName());
						}
						values.put(po.getAsName(), defPopDis);
					}
				}
			}
		}
	}

	/**
	 * CRM兄弟表 默认带出客户
	 * @param fi
	 * @param values
	 * @param allTables
	 * @param fieldValues
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String getDefaultValue(final String values, final String fieldName) throws Exception {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String[] arrField = fieldName.split("\\.");
							String sql = "select " + arrField[1] + " from " + arrField[0] + " where id=?";
							if ("tblEmployee".equals(arrField[0])) {
								sql = "select " + arrField[1] + " from " + arrField[0] + " where id=(select empFullName from HREmpinform where id=?)";
							}
							PreparedStatement pss = conn.prepareStatement(sql);

							pss.setString(1, values);
							ResultSet crset = pss.executeQuery();
							if (crset.next()) {
								result.setRetVal(crset.getString(1));
							} else {
								result.setRetVal("");
							}
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		return (String) result.getRetVal();
	}

	/**
	 * 获取参考sql(主要用于在添加前有默认值的，根据有的默认值设置SQL)
	 * 在添加和修改时，须通过关联字段读取需关联显示的数据，此数据不保存在主表中
	 * @param fi DBFieldInfoBean
	 * @param values String
	 * @return String
	 */
	private String getDefRefSql(DBFieldInfoBean fi, DBTableInfoBean tableInfo, String sunCompany, Hashtable allTables, int childSunCompany, String userId) {
		if (fi.getSelectBean().getReturnFields().size() == 0) {
			return null;
		}
		String refsql = " select ";
		for (int k = 0; k < fi.getSelectBean().getReturnFields().size(); k++) {
			PopField field = (PopField) fi.getSelectBean().getReturnFields().get(k);
			refsql += field.fieldName + ",";
		}

		refsql = refsql.substring(0, refsql.length() - 1);
		refsql += fi.getSelectBean().getPopsql().substring(fi.getSelectBean().getPopsql().indexOf(" from "));
		boolean flagDef = false;
		for (int i = 0; i < fi.getSelectBean().getSaveFields().size(); i++) {
			PopField field = (PopField) fi.getSelectBean().getSaveFields().get(i);
			List fis = tableInfo.getFieldInfos();
			Object value = "";
			for (int j = 0; j < fis.size(); j++) {
				DBFieldInfoBean fid = (DBFieldInfoBean) fis.get(j);
				if (field.getParentName().substring(field.getParentName().indexOf(".") + 1).equals(fid.getFieldName())) {
					//判断是否有@Sess，设置缺省值
					if (fid.getDefaultValue() != null && fid.getDefaultValue().contains("@Sess:")) {
						String sessStr = fid.getDefaultValue().substring(fid.getDefaultValue().indexOf(":") + 1);
						String sess = ((Hashtable) BaseEnv.sessionSet.get(userId)).get(sessStr).toString();
						value = sess;
					} else {
						value = fid.getDefaultValue();
					}
				}
			}

			if (value != null && value.toString().length() > 0) {
				flagDef = true;
				if (refsql.indexOf(" where ") > 0) {
					refsql += " and " + field.fieldName + " = '" + value + "'";
				} else {
					refsql += " where " + field.fieldName + " = '" + value + "'";
				}
			}
		}
		if (!flagDef) {
			return "";
		}
		ArrayList list = fi.getSelectBean().getTables();

		if (list.size() > 0) {
			String query = "";
			for (int i = 0; i < list.size(); i++) {
				DBTableInfoBean tableBean = DDLOperation.getTableInfo(allTables, list.get(i).toString());
				if (tableBean.getIsSunCmpShare() != 1 && childSunCompany == 0) {
					query += " " + list.get(i) + ".SCompanyID='" + sunCompany + "' and";
				}
			}
			if (query.length() > 0) {
				if (refsql.indexOf(" where ") > 0) {
					refsql += " and " + query;
				} else {
					refsql += " where " + query;
				}
				refsql = refsql.substring(0, refsql.length() - 3);
				//如果管理员不存在部门，则去掉tblDepartment.SCompanyID条件
				if ("1".equals(userId)) {
					refsql = refsql.substring(0, refsql.lastIndexOf("and"));
				}
			}
		}
		return refsql;
	}

	/**
	 * 是否属于超级管理员角色
	 * @param loginBean LoginBean
	 * @return boolean
	 */
	public static boolean hasAdminRole(LoginBean loginBean) {
		if ("1".equals(loginBean.getId())) {
			return true;
		}
		return false;
	}

	/**
	 * 查询他人委托给我的工作流
	 * 
	 * @param tableName
	 * @param ids
	 * @param tblMap
	 * @param local
	 * @return
	 */
	public static HashMap<String, String> queryConsignation(final String userId, final String flowId) {
		final Result res = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							HashMap<String, String> consignMap = new HashMap<String, String>();
							String sql = "select UserID from OAWorkConsignation where CongignUserID=? "
									+  " and (flowName like (select '%'+sameFlow+';%' from OAWorkFlowTemplate where id=?) or len(isNull(flowName,''))=0) and State = 1"
									+  " and substring(convert(varchar,GETDATE(),120),0,11)>=beginTime " 
									+  " and substring(convert(varchar,GETDATE(),120),0,11)<=endTime" ;
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, userId);
							pss.setString(2, flowId);
							ResultSet rss = pss.executeQuery();
							while (rss.next()) {
								consignMap.put(rss.getString("UserID"), userId);
							}
							res.setRetVal(consignMap);
						} catch (Exception ex) {
							res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
						}
					}
				});
				return res.getRetCode();
			}
		});
		if (retCode == ErrorCanst.DEFAULT_SUCCESS) {
			return (HashMap<String, String>) res.retVal;
		}
		return new HashMap<String, String>();
	}

	/**
	 * 本函数，用于在sql中查询指定字段串，中间要去掉（）子查询
	 * @param sql
	 * @param selStr
	 * @return
	 */
	public static int pos(String sql, String selStr) {
		sql = sql.toUpperCase();
		selStr = selStr.toUpperCase();
		int pos = -1;
		for (int i = 0; i < 100; i++) {
			pos = sql.indexOf(selStr, pos + 1);
			if (pos < 0) {
				return pos;
			}
			//查询本段中是否有匹配的()号，如果不匹配，找后一个selStr		
			if (ismatchkh(sql.substring(0, pos))) {
				return pos;
			}
		}

		return -1;
	}

	/**
	 * 计算字符串中是否是相匹配的(),如果()个数不想等为不匹配
	 * @param str
	 * @return
	 */
	private static boolean ismatchkh(String str) {
		boolean rs = false;
		Pattern pattern = Pattern.compile("(\\(|\\))", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		int kl = 0;
		int kr = 0;
		while (matcher.find()) {
			String s = matcher.group(1);
			if ("(".equals(s)) {
				kl++;
			} else if (")".equals(s)) {
				kr++;
			}
		}
		return kl == kr;
	}

	/**
	 * 从出现该表的地方开始一直找where 碰到）或字串结尾中止，如果先碰到（，则压入堆栈，直接相应个数的）出现
	 * @param sql
	 * @param pos
	 * @return
	 */
	private static int findWhere(String sql, int pos) {
		//1、找where
		sql = sql.toUpperCase();
		int khCount = 0;//括号的堆栈
		for (int i = pos; i < sql.length(); i++) {
			char c = sql.charAt(i);
			if (c == 'W') {
				//如果为W则检查其后是否是WHERE
				if (khCount == 0 && sql.substring(i).startsWith("WHERE 1=1")) {
					return i + 9;
				} else if (khCount == 0 && sql.substring(i).startsWith("WHERE")) {
					return i + 5;
				}
			} else if (c == '(') {
				khCount++;
			} else if (c == ')') {
				if (khCount == 0) {
					return i - 1;
				} else {
					khCount--;
				}
			}
		}
		return sql.length();
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

	/**
	 * * 范围权限鉴权，通过在sql语句后拼接where筛选条件，过滤记录
	 * 1、去掉tblpopedom表
	 * 1、分类资料，凡是能匹配上分类表的都自动加上分类控制代码，如果是视图等。必须用as 为和分类表一致的，形式，如totalview as tblCompany ,
	 *    当然如果语句不希望被控制可用as 翻译成其它不说嘴郎中的表名，则不会被 控制 
	 * @param reportName 报表的名称用于，查看范围值控制，取得报表的表名，其它时候该字段请填null
	 * @param detTableName 明细报表对应的主表表名，因为明细报表和汇总报表共享数据表列表的权限
	 * @param userId 执行者
	 * @param scopeRight
	 * @param sql
	 * @param parentCode
	 * @param classFieldName 如果分级报表，分级弹出窗，这里指明分级的字段名
	 * @return
	 */

	public static String scopeRightHandler(String reportName,String reportType,String detTableName, String userId, ArrayList scopeRight, String sql, String parentCode,String classFieldName) {
		if (userId.equals("1")) {
			sql = sql.replaceAll("(?i)\\bEMPRIGHT\\b", ""); //去掉语句中的部门临时标志
			sql = sql.replaceAll("(?i)\\bNORIGHT\\b", ""); //去掉语句中的部门临时标志
			return sql;
		}
		
		//在每执行一一个范围权限之后，都加一个" SCOPERIGHT ",用以记录前一段内容为范围权限，
		//当启用审核流时，由于要自动显示当前由我审核或委托我审核的单据，该内容和范围权限是一个或的关系，
		//以上标识可以帮助定位最后一范围权限，从而实际拼写sql的目的

		DBTableInfoBean reportBean = null;
		boolean isWorkFlow= false;//如果是数据表列表，是否启用审核流，如基础信息启用审核流，数据表列表权限与普通数据表列表一致
		boolean mainNoRight = false; //数据表列表是不是不控制权限
		if(ReportData.TABLELIST.equals(reportType)){
			reportBean = GlobalsTool.getTableInfoBean(reportName);
			OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(reportName);
			isWorkFlow = (template!=null && template.getTemplateStatus()==1);
			mainNoRight =sql.matches(".*[\\s]{1}(?i)"+reportName+"[\\s]*noRight[\\s]*.*");
		}
		
		boolean allDept = false;

		if (scopeRight != null && scopeRight.size() > 0) {
			//BaseEnv.log.debug("DynDBManager.scopeRightHandler 权限解释开始:" + sql);
			String sqlSelect = ""; // from语句之前的部分，独立开来，不参写权限控制
			String sqlFrom = "";// from 之后的语句要参写权限控制。
			// 找到from 语句的字位置，要考虑中间的子查询，把以在from之前应该是匹配对应个数的()号
			int pos = pos(sql, "from");
			if (pos == -1) {
				//找不到from 一般说明是存储过程报表，其权限应由存储过程自己处理
				return sql;
			}
			sqlSelect = sql.substring(0, pos);
			sqlFrom = sql.substring(pos);
			//BaseEnv.log.debug("DynDBManager.scopeRightHandler 权限解释 取得from语句:" + sqlFrom);

			//1、处理分类资料权限，分类资料中如果有重复的要合并
			ArrayList<LoginScopeBean> classList = new ArrayList<LoginScopeBean>();
			for (Object o : scopeRight) {
				LoginScopeBean lsb = (LoginScopeBean) o;
				if ("0".equals(lsb.getFlag())) {
					String tableName = lsb.getTableName();

					LoginScopeBean oldBean = null;
					for (LoginScopeBean hasbean : classList) {
						if (tableName.equals(hasbean.getTableName())) {
							oldBean = hasbean;
						}
					}
					if (oldBean == null) {
						//因为接下下会有合并scopeValue的行为，所以这里必须克隆，而不能简单赋值
						LoginScopeBean newls = new LoginScopeBean();
						newls.setTableName(tableName);
						newls.setFlag(lsb.getFlag());
						newls.setId(lsb.getId());
						newls.setIsAllModules(lsb.getIsAllModules());
						newls.setRoleId(lsb.getRoleId());
						newls.setScopeValue(lsb.getScopeValue());
						classList.add(newls);
					} else {
						//合并
						oldBean.setScopeValue(oldBean.getScopeValue() + lsb.getScopeValue());
					}
				}
			}
			for (Object o : classList) {
				LoginScopeBean lsb = (LoginScopeBean) o;
				if ("0".equals(lsb.getFlag())) {
					//BaseEnv.log.debug("DynDBManager.scopeRightHandler 权限解释 找到分类资料表" + lsb.getTableName() + "解释开始:");
					for(int i=0;i<200;i++){
						// 分类资料，该范围权限能出现在这里，说明都是对本模块生效的权限，所以不管是针对所有模块的权限，还是针对指定模块的权限都一样的处理.
						String tableName = lsb.getTableName();
	
						//表达式解释----凡是出现表名的地方，其后出现 left inner join on , where WITH(INDEX 即表明语句匹配上
						Pattern pattern = Pattern.compile("[\\s]{1}" + tableName + "[\\s]*(?=left|inner|join|on|,|where|(with[\\s]*\\())", Pattern.CASE_INSENSITIVE);
						Matcher matcher = pattern.matcher(sqlFrom);
						boolean hasTable=false;
						while (matcher.find()) {
							String all = matcher.group(0);
							int mpos = matcher.start() ; //这里计算的位置信息是源字符串的位置，但是经过循环后字符串增加了长度，所以必须加上这个长度
	
							//标识该表为noRight已经处理过权限
							sqlFrom = sqlFrom.substring(0, mpos + all.length()) + " noRight " + sqlFrom.substring(mpos + all.length());
	
							//从出现该表的地方开始一直找where 碰到）或字串结尾中止，如果先碰到（，则压入堆栈，直接相应个数的）出现
							mpos = findWhere(sqlFrom, mpos + all.length());
							//在此位置插入脚本						
							String inS = "";
							String[] svs = lsb.getScopeValue().split(";");
							if (svs != null) {
								for (String sv : svs) {
									if ((parentCode == null || parentCode.length() == 0) && 
											(classFieldName !=null && classFieldName.indexOf(".")>0&&tableName.equalsIgnoreCase(classFieldName.substring(0,classFieldName.indexOf("."))))) { 
										//分级报表第一级。 如果分级报表要分级时，无父记录，则用等于的方式只显示父级,这里非分级报表parentCode会传入一个endClass值所以不用担心非分级报表的传入问题
										inS += "OR " + tableName.toUpperCase() + ".CLASSCODE = '" + sv + "' ";
									} else {
										inS += "OR " + tableName.toUpperCase() + ".CLASSCODE LIKE '" + sv + "%' ";
									}
								}
							}
	
							if (inS.length() > 0) {
								inS = " ( LEN(ISNULL(" + tableName.toUpperCase() + ".CLASSCODE,''))=0 " + inS + ")";
								if (sqlFrom.substring(0, mpos).trim().toUpperCase().endsWith("WHERE 1=1")) {
									inS = " AND " + inS + " rightscope ";
								} else if (sqlFrom.substring(0, mpos).trim().toUpperCase().endsWith("WHERE")) {
									inS = inS + " rightscope " + (sqlFrom.substring(0, mpos).trim().length() > 0 ? " AND " : "");
								} else {
									inS = " WHERE 1=1 " + inS + " rightscope ";
								}
								sqlFrom = sqlFrom.substring(0, mpos) + inS + sqlFrom.substring(mpos);
							}
							//BaseEnv.log.debug("DynDBManager.scopeRightHandler 权限解释 找到分类资料表" + tableName + "解释后:" + sqlFrom);
							
							hasTable = true; //标志找到数据。
							break;
						}
						if (!hasTable) {
							//找不到数据，退出循环
							break;
						}
					}
				}
			}

			//2、处理查看范围值控制--非数字和日期字段，合并，用或关系
			String scopeStr6 = "";
			classList = new ArrayList<LoginScopeBean>();
			for (Object o : scopeRight) {
				LoginScopeBean lsb = (LoginScopeBean) o;
				if ("6".equals(lsb.getFlag()) && (lsb.getTableName().equals(reportName) || lsb.getTableName().equals(detTableName))) {
					if("0".equals(lsb.getValuetype()) || "1".equals(lsb.getValuetype())||
							"5".equals(lsb.getValuetype()) || "6".equals(lsb.getValuetype()))
					{
						classList.add(lsb);
					}else{
						LoginScopeBean oldBean = null;
						for (LoginScopeBean hasbean : classList) {
							if (lsb.getTableName().equals(hasbean.getTableName())&& lsb.getFieldName().equals(hasbean.getFieldName())) {
								oldBean = hasbean;
							}
						}
						if (oldBean == null) {
							//因为接下下会有合并scopeValue的行为，所以这里必须克隆，而不能简单赋值
							LoginScopeBean newls = new LoginScopeBean();
							newls.setTableName(lsb.getTableName());
							newls.setFieldName(lsb.getFieldName());
							newls.setFlag(lsb.getFlag());
							newls.setId(lsb.getId());
							newls.setIsAllModules(lsb.getIsAllModules());
							newls.setRoleId(lsb.getRoleId());
							newls.setScopeValue(lsb.getScopeValue());
							classList.add(newls);
						} else {
							//合并
							oldBean.setScopeValue(oldBean.getScopeValue() +",,"+ lsb.getScopeValue());
						}
					}
				}
			}			
			for (Object o : classList) {
				//先找到所有需要控制的代码
				LoginScopeBean lsb = (LoginScopeBean) o;
				if ("6".equals(lsb.getFlag()) && (lsb.getTableName().equals(reportName) || lsb.getTableName().equals(detTableName))) {
					String fieldN = lsb.getFieldName().toUpperCase();
					if ("0".equals(lsb.getValuetype()) || "1".equals(lsb.getValuetype())) {
						//数字型，条件是个区间
						if (lsb.getScopeValue() != null && lsb.getScopeValue().length() > 0) {
							scopeStr6 += " AND " + fieldN + " >= " + lsb.getScopeValue() + " rightscope ";
						}
						if (lsb.getEscopeValue() != null && lsb.getEscopeValue().length() > 0) {
							scopeStr6 += " AND " + fieldN + " <= " + lsb .getEscopeValue() + " rightscope ";
						}
					} else if ("5".equals(lsb.getValuetype()) || "6".equals(lsb.getValuetype())) {
						//日期型，条件是个区间
						if (lsb.getScopeValue() != null && lsb.getScopeValue().length() > 0) {
							scopeStr6 += " AND " + fieldN + " >= '" + lsb.getScopeValue() + "' rightscope  ";
						}
						if (lsb.getEscopeValue() != null && lsb.getEscopeValue().length() > 0) {
							scopeStr6 += " AND " + fieldN + " <= '" + lsb.getEscopeValue() + "' rightscope  ";
						}
					} else {
						if(lsb.getScopeValue().indexOf(",,")>0){
							String v = lsb.getScopeValue().replaceAll(",,", "','");
							scopeStr6 += " AND " + fieldN + " IN ('" + v + "') rightscope  ";
						}else{
							scopeStr6 += " AND " + fieldN + "= '" + lsb.getScopeValue() + "' rightscope  ";
						}
					}
				}
			}
			if (scopeStr6 != null && scopeStr6.length() > 0) {
				//查看范围值控制权限 都是针对整个报表的，报表至少会有条件1=1,所以可以直接加找到最后的where 加入条件
				int mpos = findWhere(sqlFrom, 0);
				if (mpos > -1) {
					sqlFrom = sqlFrom.substring(0, mpos) + (scopeStr6) + sqlFrom.substring(mpos);
					//BaseEnv.log.debug("DynDBManager.scopeRightHandler 权限解释 找到查看范围值控制权限:" + sqlFrom);
				} else {
					//BaseEnv.log.error("DynDBManager.scopeRightHandler 权限解释 查看范围值控制权限 找不到插入位置，最后可能没有WHERE条件------:" + sqlFrom);
				}

			}
			//3、处理部门，职员范围权限
			String deptStr = "";
			String empStr = "";
			
			for (Object o : scopeRight) {
				LoginScopeBean lsb = (LoginScopeBean) o;
				if ("1".equals(lsb.getFlag())) {//查看职员单据					
					for (String sc : lsb.getScopeValue().split(";")) {
						if (sc.length() > 0) {
							empStr += ",'" + sc + "'";
						}
					}
				} else if ("5".equals(lsb.getFlag())) {
					if (lsb.getScopeValue().equals("ALL")) {
						//设置有查看全公单据的权限,清空所有权限控制代码，退出
						deptStr = "";
						empStr = "";
						allDept = true;
						break;
					} else {
						for (String sc : lsb.getScopeValue().split(";")) {
							if (sc.length() > 0) {
								deptStr += " OR @@TBLEMPLOYEE.DEPARTMENTCODE LIKE '" + sc + "%' ";
							}
						}
					}
				}
			}
			if (!allDept) {
				empStr += ",'" + userId + "'";
			}

			if ((deptStr != null && deptStr.length() > 0) || (empStr != null && empStr.length() > 0)) {
				if (empStr.length() > 0)
					empStr = empStr.substring(1); //去掉逗号
				//查询需要控制的所有的表
				//BaseEnv.log.debug("DynDBManager.scopeRightHandler 权限解释 部门和职员管辖解释开始:");
				// 先检查所有要控件的表。
				for (int i = 0; i < 200; i++) {//最多循环200次处理sql中的语句，由于每循环处理一次sql发生变化，必须重新执行以下动作
					//表达式解释----凡是出现表名的地方，其后出现 left inner join on , where WITH(INDEX 即表明语句匹配上
					Pattern pattern = Pattern.compile("[\\s]{1}([\\w]+)[\\s]*(?=left|inner|join|on|,|where|(with[\\s]*\\())", Pattern.CASE_INSENSITIVE);
					Matcher matcher = pattern.matcher(sqlFrom);
					
					boolean hasTable=false;
					while (matcher.find()) {
						String tableName = matcher.group(1); //找到了表名
						//查看表该是是否存在，且非基础表且未启用审核流,否则跳过执行。
						DBTableInfoBean tbean = GlobalsTool.getTableInfoBean(tableName);
						if (tbean == null  || tbean.getTableType() == DBTableInfoBean.CHILD_TABLE ) {
							continue;
						}
						if (tbean.getIsBaseInfo() == 1) {
							if(reportBean==null){ //基础信息，非数据表列表
								continue;
							}else if(reportBean != null && !isWorkFlow){ //基础信息，数据表列表，未启用审核流
								continue;
							}							
						}
						boolean ifEmployeeID = containsField("EmployeeID", tbean);
						boolean ifCreateBy = containsField("createBy", tbean);
						boolean ifDepartmentCode = containsField("departmentCode", tbean);
						if (!ifEmployeeID && !ifCreateBy) { //不存在职员，或制单人字段
							continue;
						}
						
						if(reportBean != null){
							//reportBean 不为空说明是数据表列表，而所有数据表列表不能控制他联查的其它表
							if(!tableName.equals(reportBean.getTableName())){
								continue;
							}
							
						}

						String all = matcher.group(0);
						int mpos = matcher.start();
						//标识该表为noRight已经处理过权限
						sqlFrom = sqlFrom.substring(0, mpos + all.length()) + " noRight " + sqlFrom.substring(mpos + all.length());

						//从出现该表的地方开始一直找where 碰到）或字串结尾中止，如果先碰到（，则压入堆栈，直接相应个数的）出现
						mpos = findWhere(sqlFrom, mpos + all.length());

						String inEmpStr = ""; //插入的职员控制代码

						if (ifEmployeeID && empStr.length() > 0) {
							inEmpStr = " LEN(ISNULL(" + tableName.toUpperCase() + ".EMPLOYEEID,''))=0 OR "+ tableName.toUpperCase() + ".EMPLOYEEID IN (" + empStr + ")";
						}
						if (ifCreateBy && empStr.length() > 0) {
							//如果报表对应的表存在，且存在createBy字段，一般说明这是数据表列表，要加上他创建人字段
							inEmpStr += (inEmpStr.length() > 0 ? " OR " : "") + " LEN(ISNULL(" + tableName.toUpperCase() + ".CREATEBY,''))=0 OR "+  tableName.toUpperCase() + ".CREATEBY IN (" + empStr + ")";
						}
						
						//CRM客户列表，还要加上CRMClientInfoEmp表的数据
						if(tableName.equalsIgnoreCase("CRMClientInfo")){
							inEmpStr += (inEmpStr.length() > 0 ? " OR " : "") + " CRMCLIENTINFO.ID IN (SELECT F_REF FROM CRMCLIENTINFOEMP WHERE EMPLOYEEID ='"+userId+"') ";
						}

						String inS = "";
						if (deptStr != null && deptStr.length() > 0) {
							if(ifDepartmentCode){
								inEmpStr += (inEmpStr.length() > 0 ? " OR " : "") + " LEN(ISNULL(" + tableName.toUpperCase() + ".DEPARTMENTCODE,''))=0 OR "+ (deptStr.substring(3).replaceAll("@@TBLEMPLOYEE", tableName.toUpperCase())) ;
							}else{
								inEmpStr = (inEmpStr.length() > 0 ? inEmpStr+" OR " : " LEN(ISNULL(" + tableName.toUpperCase() + "."+(ifEmployeeID ? "EMPLOYEEID" : "CREATEBY")+",''))=0 OR ") + 
									tableName.toUpperCase() + "." + (ifEmployeeID ? "EMPLOYEEID" : "CREATEBY")
									+ " IN (SELECT TBLEMPLOYEE.ID FROM TBLEMPLOYEE WHERE " + (deptStr.substring(3).replaceAll("@@TBLEMPLOYEE", "TBLEMPLOYEE")) + ")";
							}
							inS = " (" + inEmpStr + ")";
						} else {
							inS = " (" + inEmpStr + ")";
						}
						

						
						if (sqlFrom.substring(0, mpos).trim().toUpperCase().endsWith("WHERE 1=1")) {
							inS = " AND " + inS + " rightscope ";
						} else if (sqlFrom.substring(0, mpos).trim().toUpperCase().endsWith("WHERE")) {
							inS = " "+inS + " rightscope " + (sqlFrom.substring(0, mpos).trim().length() > 0 ? " AND " : "");
						} else {
							inS = " WHERE 1=1 AND " + inS + " rightscope ";
						}

						sqlFrom = sqlFrom.substring(0, mpos) + inS + sqlFrom.substring(mpos);

						//BaseEnv.log.debug("DynDBManager.scopeRightHandler 权限解释 部门和职员管辖解释 找到" + tableName + "解释后:" + sqlFrom);
						
						hasTable = true; //标志找到数据。
						break;
					}
					if (!hasTable) {
						//找不到数据，退出循环
						break;
					}
				}
			}

			sql = sqlSelect + sqlFrom;
		} else {
			//没有范围权限时，要自动加入自己

			String sqlSelect = ""; // from语句之前的部分，独立开来，不参写权限控制
			String sqlFrom = "";// from 之后的语句要参写权限控制。
			// 找到from 语句的字位置，要考虑中间的子查询，把以在from之前应该是匹配对应个数的()号
			int pos = pos(sql, "from");
			if (pos == -1) {
				//找不到from 一般说明是存储过程报表，其权限应由存储过程自己处理
				return sql;
			}
			sqlSelect = sql.substring(0, pos);
			sqlFrom = sql.substring(pos);
			//BaseEnv.log.debug("DynDBManager.scopeRightHandler 权限解释 取得from语句:" + sqlFrom);
			for (int i = 0; i < 200; i++) {//最多循环200次处理sql中的语句，由于每循环处理一次sql发生变化，必须重新执行以下动作
				boolean hasTable = false;//如果找到要处理的表，标为true,再循环执行直到找不到表
				//表达式解释----凡是出现表名的地方，其后出现 left inner join on , where WITH(INDEX 即表明语句匹配上
				Pattern pattern = Pattern.compile("[\\s]{1}([\\w]+)[\\s]*(?=left|inner|join|on|,|where|(with[\\s]*\\())", Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(sqlFrom);

				while (matcher.find()) {
					String tableName = matcher.group(1); //找到了表名
					//查看表该是是否存在，且非基础表且未启用审核流,否则跳过执行。
					DBTableInfoBean tbean = GlobalsTool.getTableInfoBean(tableName);
					if (tbean == null  || tbean.getTableType() == DBTableInfoBean.CHILD_TABLE ) {
						continue;
					}
					if (tbean.getIsBaseInfo() == 1) {
						if(reportBean==null){ //基础信息，非数据表列表
							continue;
						}else if(reportBean != null && !isWorkFlow){ //基础信息，数据表列表，未启用审核流
							continue;
						}							
					}
					if(reportBean != null){
						//reportBean 不为空说明是数据表列表，而所有数据表列表不能控制他联查的其它表
						if(!tableName.equals(reportBean.getTableName())){
							continue;
						}
						
					}
					boolean ifEmployeeID = containsField("EmployeeID", tbean);
					boolean ifCreateBy = containsField("createBy", tbean);
					if (!ifEmployeeID && !ifCreateBy) { //不存在职员，或制单人字段
						continue;
					}

					String all = matcher.group(0);
					int mpos = matcher.start();
					//标识该表为noRight已经处理过权限
					sqlFrom = sqlFrom.substring(0, mpos + all.length()) + " noRight " + sqlFrom.substring(mpos + all.length());
					//从出现该表的地方开始一直找where 碰到）或字串结尾中止，如果先碰到（，则压入堆栈，直接相应个数的）出现
					mpos = findWhere(sqlFrom, mpos + all.length());

					String inS = "";
					if (containsField("createBy", tbean)) {
						inS = tableName.toUpperCase() + ".CREATEBY='" + userId + "' OR  LEN(ISNULL(" + tableName.toUpperCase() + ".CREATEBY,''))=0 ";
					}
					if (containsField("employeeId", tbean)) {
						inS += (inS.length() > 0 ? " OR " : "") + tableName.toUpperCase() + ".EMPLOYEEID='" + userId + "' OR LEN(ISNULL(" + tableName.toUpperCase() + ".EMPLOYEEID,''))=0 ";
					}
					
					//CRM客户列表，还要加上CRMClientInfoEmp表的数据
					if(tableName.equalsIgnoreCase("CRMClientInfo")){
						inS += (inS.length() > 0 ? " OR " : "") + " CRMCLIENTINFO.ID IN (SELECT F_REF FROM CRMCLIENTINFOEMP WHERE EMPLOYEEID ='"+userId+"') ";
					}
					
					inS = " ("+inS+") ";
					if (sqlFrom.substring(0, mpos).trim().toUpperCase().endsWith("WHERE 1=1")) {
						inS = " AND " + inS + " rightscope ";
					} else if (sqlFrom.substring(0, mpos).trim().toUpperCase().endsWith("WHERE")) {
						inS = " "+inS + " rightscope " + (sqlFrom.substring(0, mpos).trim().length() > 0 ? " AND " : "");
					} else {
						inS = " WHERE 1=1 " + inS + " rightscope ";
					}

					sqlFrom = sqlFrom.substring(0, mpos) + inS + sqlFrom.substring(mpos);

					//BaseEnv.log.debug("DynDBManager.scopeRightHandler 权限解释 加入自己 找到" + tableName + "解释后:" + sqlFrom);
					hasTable = true; //标志找到数据。
					break;
				}
				if (!hasTable) {
					//找不到数据，退出循环
					break;
				}
			}
			sql = sqlSelect + sqlFrom;
		}
		
		//如果是主表加了noRight，则表示不用控制权限，则也不用加工作流的信息
		if (!allDept && reportBean != null && !mainNoRight ) {//查看全公司权限后，不要加这个控制代码 (只要是启用审核流了，就要适用审核流的方式，不管是不是基础信息) && reportBean.getIsBaseInfo() != 1
			OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(reportName);
			String workflowStr = "";
			if (reportBean != null && (workFlow != null && workFlow.getTemplateClass() != null ) && workFlow.getTemplateStatus() == 1
					|| reportBean.equals("tblViewStayAuditingBill")) {
				//启用工作流后的控制代码

				//工作流暂时只从OAMyWorkFlowPerson 中取数				
				String workflowUser = "'" + userId + "'";
				//查询他人委托给我的工作流
				HashMap<String, String> consignMap = queryConsignation(userId, workFlow.getId());
				if (consignMap != null && consignMap.size() > 0) {
					for (String str : consignMap.keySet()) {
						workflowUser += " ,'" + str + "' ";
					} 
				}

				workflowStr += "  (select count(0) from OAMyWorkFlowPerson where keyId="+ reportBean.getTableName() +".id and tableName='"+reportBean.getTableName()+"' and checkPerson in("+workflowUser+"))>0 ";
				
				
				//查询最后一个where有没有范围权限	        	
				int mpos = findWhere(sql, 0);
				if (sql.substring(mpos).indexOf(" rightscope ") == -1) {
					//没有范围权限时直接加入审核流信息
					sql = sql.substring(0, mpos) + " AND (" + workflowStr + ") " + sql.substring(mpos);
				} else {
					//有范围权限，取范围权限，并和工作流条件OR
					int scopePos = sql.lastIndexOf(" rightscope ");
					mpos += 4;//跟过1=1后的and
					sql = sql.substring(0, mpos) + " ( (" + sql.substring(mpos, scopePos) + ") OR (" + workflowStr + ") ) " + sql.substring(scopePos);
				}
			}
		}

		sql = sql.replaceAll("(?i)\\brightscope\\b", ""); //去掉语句中的部门临时标志
		sql = sql.replaceAll("(?i)\\bEMPRIGHT\\b", ""); //去掉语句中的部门临时标志
		sql = sql.replaceAll("(?i)\\bNORIGHT\\b", ""); //去掉语句中的部门临时标志
		return sql;
	}

	public Result getChildCount(final String classField, final String parentValue) {
		final Result rs = new Result();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						Result rss = getChildCount(classField, parentValue, conn);
						rs.setRetCode(rss.getRetCode());
						rs.setRetVal(rss.getRetVal());
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	public Result getDepartMent(final String departCode) {
		final Result rs = new Result();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						Statement st = conn.createStatement();
						ResultSet rss = st.executeQuery("select DeptFullName,departmentManager from tblDepartment where classCode='" + departCode + "'");
						if (rss.next()) {
							String[] depart = new String[2];
							depart[0] = rss.getString(1);
							depart[1] = rss.getString(2);
							rs.setRetVal(depart);
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	public Result getGroupIds(final String userId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "select a.id from tblEmpGroup a ,tblEmpGroupUser b where a.id=b.f_ref and b.userID=? ";
						PreparedStatement pss = conn.prepareStatement(sql);
						pss.setString(1, userId);
						ResultSet rss = pss.executeQuery();
						String strGroup = "";
						while (rss.next()) {
							strGroup += rss.getString("id") + ";";
						}
						rs.setRetVal(strGroup);
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	public Result getChildCount(final String classField, final String parentValue, Connection conn) {
		final Result rs = new Result();
		String sql = "";
		if (classField != null && classField.length() > 0) {
			//设计SQL得到类别的子级的个数
			String tn = classField.substring(0, classField.indexOf("."));
			String fn = classField.substring(classField.indexOf(".") + 1);
			sql = "select count(*) from " + tn + " a where a." + fn + " like '" + parentValue + "_____%'";
		}

		try {
			Statement cs = conn.createStatement();
			BaseEnv.log.debug(sql);
			ResultSet rset = cs.executeQuery(sql);
			if (rset.next()) {
				rs.setRetVal(rset.getInt(1));
			}
		} catch (SQLException ex) {
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			BaseEnv.log.error("Pop Select  data Error :" + sql, ex);

		}

		return rs;
	}

	/**<pre>
	 * 弹出选择框查询数据
	 * 返回的{@link Result}的retVal为 {@link ArrayList}
	 * ArrayList里存<b>String</b>数组,为查询出来的数据,顺序为 {@link PopupSelectBean#getAllFields()}的顺序
	 * 选择类型: {@link PopupSelectBean#DEFAULTRULE}按原规则(有关键字则只查未级，无则逐级查),{@link PopupSelectBean#LEAFRULE}(查未级){@link PopupSelectBean#BRANCHRULE}(查非级) {@link PopupSelectBean#LEVELRULE}(当前级)
	 * 如只查当前级的未级那么是:{@value PopupSelectBean#LEAFRULE}+{@value PopupSelectBean#LEVELRULE}=5
	 * 如果要查询所有级别（包含未级和非未级）那用0而不是{@value PopupSelectBean#BRANCHRULE}+{@value PopupSelectBean#LEAFRULE}=3
	 * </pre>
	 * @param propFieldName 属性字段 
	 * @param tableName  表名
	 * @param selectBean  弹出框实体BEAN（XML生成）
	 * @param allTables  所有表
	 * @param param  所有输入值
	 * @param mainParam  界面上的参数
	 * @param scopeRight  范围权限
	 * @param pageNo 第几页
	 * @param pageSize 每页记录数
	 * @param parentCode  父级classcode
	 * @param mainId  引用单据时，单据的ID
	 * @param userId 用户ID
	 * @param sunCompanyID
	 * @param locale 多语言标识
	 * @param keySearch  关键字
	 * @param request
	 * @param src  应该是判断是否第一次点开
	 * @param searchType 选择类型
	 * @return
	 * @see PopupSelectBean
	 */
	public Result popSelect(final String propFieldName, final String tableName, final PopupSelectBean selectBean, final Hashtable allTables, final java.util.ArrayList param, final HashMap mainParam,
			final ArrayList scopeRight, final int pageNo, final int pageSize, String parentCode, String mainId, String userId, String sunCompanyID, final Locale locale, String keySearch,
			HttpServletRequest request, String src, int searchType,String topId) {
		boolean hasMainParam = false; //是否有从界面传入的参数
		//当mainParam 中有参数时，軒hasmaiParam
		for(Object o :mainParam.values()){
			if(o != null && !o.equals("") && !"BillDate".equalsIgnoreCase(o.toString())){ //BillDate一般是如传入供应商弹出窗算交货日期的，本身不参与条件过滤所以这里不能当参数
				hasMainParam = true;
			}
		}
		final Result rs = new Result();
		rs.pageNo = pageNo;
		rs.pageSize = pageSize;
		String sql = selectBean.getPopsql().trim();
		int condWhere = 0;
		//如果有查询字段为多语言，则要连接多语言查询
		if (param != null) {
			for (int i = 0; i < param.size(); i++) {
				String[] tempvalue = (String[]) param.get(i);
				DBFieldInfoBean fb = GlobalsTool.getFieldBean(tempvalue[0].substring(0, tempvalue[0].indexOf(".")), tempvalue[0].substring(tempvalue[0].indexOf(".") + 1));
				if (fb != null && (fb.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fb.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE)) {
					int wherepos = pos(sql,"where");
					if(wherepos == -1){
						sql = sql+  " left join tblLanguage language" + i + " on " + tempvalue[0] + "=language" + i + ".id ";
					}else{
						sql = sql.substring(0,wherepos-1)+  " left join tblLanguage language" + i + " on " + tempvalue[0] + "=language" + i + ".id "+sql.substring(wherepos);
					}
				}
			}
		}
		//启用了分支机构时，且不是分支机构共享，加入分支机构限制（只能查当前登录分支机构及其下级的数据）
		ArrayList tables = selectBean.getTables(); //取popup的xml中<table>节点所有表名
		if(tables == null){
			tables = new ArrayList();
		}
		if(tables.size() ==0){
			//取from 后第一个表做为tables，因为有几个地方，用到tables，如下面mainId的拼语句, 而新版的弹出窗则去掉了这个选项
			int pos = pos(sql,"from");
			String temp = sql.substring(pos+4).trim();
			pos = temp.indexOf(" ");
			if(temp.indexOf("\n") != -1 && temp.indexOf("\n")<pos){
				pos = temp.indexOf("\n");
			}
			if(pos== -1){
				pos = temp.length();
			}
			tables.add(temp.substring(0,pos).trim());
		}
		
		condWhere  = pos(sql,"where");
		
		//单据引用加入主弹出窗编号。
		if (mainId.length() > 0) {
			if (condWhere > 0) {
				sql += " and ";
			} else {
				sql += " where 1=1 and ";
				condWhere = sql.length();
			}
			sql += " " + tables.get(0) + ".f_ref='" + mainId + "'";

		}

		String dpCode = "";
		if (selectBean.getDefParentCode() != null && selectBean.getDefParentCode().length() > 0 && parentCode.length() == 0) {
			dpCode = selectBean.getDefParentCode();
		}  

		HashMap condParamMap = new HashMap();

		if (param != null) {
			boolean hasOr = false;//是否有or 条件
			for (int i = 0; i < param.size(); i++) {
				String[] tempvalue = (String[]) param.get(i);
				DBFieldInfoBean fb = GlobalsTool.getFieldBean(tempvalue[0].substring(0, tempvalue[0].indexOf(".")), tempvalue[0].substring(tempvalue[0].indexOf(".") + 1));
				String value = tempvalue[1] == null ? "" : tempvalue[1];
				value = value.replaceAll("'", "''");
				if (tempvalue[2].length() > 0) {
					if (condWhere > 0) {
						if(i ==0)
							sql += " and ( ";
						else if(!hasOr && (tempvalue.length>3 && tempvalue[3].equals("or")))
						{
							hasOr = true;
							sql += " ( " ;
						}
					} else {
						if (sql.contains("group by ")) {
							sql += " having 1=1 and (";
						} else {
							sql += " where 1=1 and (";
						}
						condWhere = sql.length();
					}

					if (fb != null && (fb.getInputType() == fb.INPUT_LANGUAGE || fb.getInputTypeOld() == fb.INPUT_LANGUAGE)) {
						sql += "language" + i + "." + locale + " " + tempvalue[2] + " '" + value + "'";
					} else {
						if (tempvalue[2].equals(">")) {
							sql += tempvalue[0] + " " + tempvalue[2] + value;
						} else {
							sql += tempvalue[0] + " " + tempvalue[2] + " '" + value + "'";
						}
					}
					if(i != param.size() -1)
					sql += ((tempvalue.length>3 && tempvalue[3].equals("or"))?" or ":" and ");
				}
				
				condParamMap.put(tempvalue[0], value);
			}
			if(hasOr) sql += ")";
			if(param.size() > 0)
				sql += ")";
		}
		String keyQuery = "";
		String leftStr = "";
		//keySearch都是拼字符串的，要转化'
		
		if (keySearch.length() > 0) {
			keySearch = keySearch.replaceAll("'", "''");
			for (int i = 0; i < selectBean.getDisplayField2().size(); i++) {
				PopField pop = (PopField) selectBean.getDisplayField2().get(i);
				if (pop.keySearch) {
					boolean flag = true;//检测如果此字段是要按关键字搜索，但用户在页面此字段中也输入条件则不进行模糊匹配
					for (Object o : param) {
						String[] tempvalue = (String[]) o;
						if (pop.getFieldName().equals(tempvalue[0])) {
							flag = false;
							break;
						}
					}
					if (flag) {
						String fieldName = pop.getFieldName();
						DBFieldInfoBean fieldBean = GlobalsTool.getFieldBean(fieldName.substring(0, fieldName.indexOf(".")), fieldName.substring(fieldName.indexOf(".") + 1));
						if (fieldBean != null && (fieldBean.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fieldBean.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE)) {
							leftStr += " left join tblLanguage language2" + i + " on " + pop.getFieldName() + "=" + "language2" + i + ".id ";
							keyQuery += " language2" + i + "." + locale + " like '%" + keySearch + "%' or ";
						} else {
							if (pop.getSearchType() == PopField.SEARCH_EQUAL) {
								keyQuery += pop.getFieldName() + " = '" + keySearch + "' or ";
							} else {
								keyQuery += pop.getFieldName() + " like '%" + keySearch + "%' or ";
							}
						}
					}
				}
			}
		}
		if (keyQuery.length() > 0) {
			keyQuery = "(" + keyQuery.substring(0, keyQuery.length() - 3) + ")";
			if (condWhere > 0) {
				if (leftStr.length() > 0) {
					sql = sql.replace(" where 1=1", leftStr + " where 1=1 ");
				}
				sql += " and ";
			} else {
				if (leftStr.length() > 0) {
					sql += leftStr + "  where 1=1 and ";
				} else {
					if (sql.contains("group by ")) {
						sql += " having 1=1 and ";
					} else {
						sql += " where 1=1 and ";
					}
				}
				condWhere = sql.length();
			}
			sql += keyQuery;
		}

		HashMap sysParamMap = null;
		HashMap sessParamMap = null;
		HashMap codeParamMap = null;
		HashMap queryParamMap = new HashMap();
		try {
			sysParamMap = ConfigParse.getSystemParam(selectBean.getSysParams(), sunCompanyID);
			sessParamMap = ConfigParse.getSessParam(userId, selectBean.getSessParams());
			Connection conn = null;
			codeParamMap = ConfigParse.getCodeParam(selectBean.getCodeParams(), conn);
		} catch (Exception ex) {
			ex.printStackTrace();
			return rs;
		}
		if (sql.indexOf("@TABLENAME") >= 0 && tableName != null && tableName.length() > 0) {
			sql = sql.replaceAll("@TABLENAME", tableName);
		}
		sql = ConfigParse.parseConditionSentencePutParam(sql, mainParam, sysParamMap, queryParamMap, sessParamMap, codeParamMap, condParamMap);
		String condition = selectBean.getCondition();
		String relation = selectBean.getRelation();
		if ((condition != null && condition.length() > 0) || (relation != null && relation.indexOf("ValueofDB") != -1)) {
			if (condition.indexOf("@TABLENAME") >= 0) {
				condition = condition.replaceAll("@TABLENAME", tableName);
			}
			if (propFieldName != null && propFieldName.length() > 0) {
				if (condition != null && condition.length() > 0) {
					condition = condition.replaceAll("@FieldName", propFieldName);
				}
			}

			condition = ConfigParse.parseConditionSentencePutParam(condition, mainParam, sysParamMap, queryParamMap, sessParamMap, codeParamMap, condParamMap);
			condition = condition == null ? "" : condition.trim();
			if (condition.length() > 0) {
				if (condWhere > 0) {
					sql += "   and ";
				} else {
					if (sql.contains("group by ")) {
						sql += " having 1=1 and ";
					} else {
						sql += " where 1=1  and ";
					}
					condWhere = sql.length();
				}
				sql += condition;

			}
			sql = ConfigParse.parseConditionSentencePutParam(sql, mainParam, sysParamMap, queryParamMap, sessParamMap, codeParamMap, condParamMap);
		}

		String changeCond = selectBean.getChangeCond();
		if (changeCond != null && changeCond.length() > 0) {
			if (changeCond.indexOf("@TABLENAME") >= 0) {
				changeCond = changeCond.replaceAll("@TABLENAME", tableName);
			}
			if (propFieldName != null && propFieldName.length() > 0) {
				if (changeCond != null && changeCond.length() > 0) {
					changeCond = changeCond.replaceAll("@FieldName", propFieldName);
				}
			}

			changeCond = ConfigParse.parseConditionSentencePutParam(changeCond, mainParam, sysParamMap, queryParamMap, sessParamMap, codeParamMap, condParamMap);
			changeCond = changeCond == null ? "" : changeCond.trim();
			if (changeCond.length() > 0) {
				if (condWhere > 0) {
					sql += "   and ";
				} else {
					if (sql.contains("group by ")) {
						sql += " having 1=1 and ";
					} else {
						sql += " where 1=1 and ";
					}
					condWhere = sql.length();
				}
				sql += changeCond;

			}
			sql = ConfigParse.parseConditionSentencePutParam(sql, mainParam, sysParamMap, queryParamMap, sessParamMap, codeParamMap, condParamMap);
		}

		if (selectBean.getClassCode() != null && selectBean.getClassCode().length() > 0) {
			if (condWhere <= 0) {
				if (sql.contains("group by ")) {
					sql += " having 1=1 ";
				} else {
					sql += " where 1=1 ";

				}
				condWhere = sql.length();
			}
		}
		//加入范围权限控制
		if ((selectBean.getClassCode() != null && selectBean.getClassCode().length() > 0 && ((keySearch.length() > 0) || param.size() > 0)) || selectBean.getClassCode() == null) {
			sql = scopeRightHandler(null,null,null, userId, scopeRight, sql, "endClass","");
		} else {
			sql = scopeRightHandler(null,null,null, userId, scopeRight, sql, parentCode,selectBean.getClassCode()==null?"":selectBean.getClassCode());
		}
		if (selectBean.getClassCode() != null && selectBean.getClassCode().length() > 0 && (searchType == PopupSelectBean.DEFAULTRULE || (searchType & 4) == PopupSelectBean.LEVELRULE)) {
			if (parentCode.length() > 0) {
				if (param.size() > 0 || keySearch.length() > 0) {
					sql += " and " + selectBean.getClassCode() + " like '" + parentCode + "_____%'";
				} else {
					sql += " and " + selectBean.getClassCode() + " like '" + parentCode + "_____'";
				}
			} else if (((src != null && src.equals("menu")) || param.size() > 0) && keySearch.length() == 0 && dpCode.length() > 0 && !sql.contains(selectBean.getClassCode().toUpperCase() + " = ")) {
				sql += " and " + selectBean.getClassCode() + " like '" + dpCode + "_____'";
			} else if ((condition == null || (condition != null && !condition.contains(selectBean.getClassCode()))) && !sql.contains(selectBean.getClassCode().toUpperCase() + " = ") && param.size() == 0
					&& keySearch.length() == 0 && !hasMainParam) { 
				// 原来当从界面有@ValueofDB录入的值时，不执行这段代码，但是发现象采购入库单供应商选择，会从界面取billDate来计算该单据的付款日期，这时初次进入弹出窗就没这个条件不同级别所有数据都显示了，导致bug,暂时屏蔽这个代码
				// ***!hasMainParam 这个条件是当界面传入参数作为过滤条件时不要加这个代码，这会导致界面带入的条件失效，尤其时霸客户存在大量这种需过滤的弹出窗，如选择物料根据主料过滤
				// ***，但是如采购入库单会带个BillDate过来计算该单据的付款日期，这个是不参与条件过滤的,如果这时也不带下面这段代码，会导致第一次打开弹窗后所有数据都出来的问题
				// *** 解决这个问题，是当界面只带个billDate这一个参数过来时，仍然执行这段代码
				
				//没有分类权限，并且没有父级代码和默认代码，默认显示第一级数据
				sql += " and " + selectBean.getClassCode() + " like '_____'";
			}
		}
		sql = sql.trim();
		/*销售出库单 数量1弹出窗 过滤右边已经选中数量*/
		if ("SelectQty".equals(selectBean.getName())) {
			String[] cutKeyId = request.getParameterValues("cutKeyId");
			if (cutKeyId != null && cutKeyId.length > 0) {
				String notInSql = "tblStocks.Seq not in(";
				for (String value : cutKeyId) {
					String[] arraySeq = value.split(";");
					notInSql += "'" + arraySeq[arraySeq.length - 1] + "',";
				}
				notInSql = notInSql.substring(0, notInSql.length() - 1);
				notInSql += ")";
				sql = sql.replace("where 1=1 ", "where 1=1  and " + notInSql);
			}
		}
		//如果是根据关键字调出弹出窗口
		String classCode = selectBean.getClassCode();
		if (classCode != null && classCode.length() > 0) {
			if (searchType == PopupSelectBean.DEFAULTRULE
					&& ((keySearch != null && keySearch.trim().length() > 0) || (param != null && param.size() > 0 && dpCode.length() == 0))) {
				if (sql.contains("group by ")) {
					sql = sql.replace("having 1=1", "having 1=1 and " + classCode.substring(0, classCode.indexOf(".")) + ".isCatalog=0 ");
				} else {
					sql = sql.replace("where 1=1", "where 1=1 and " + classCode.substring(0, classCode.indexOf(".")) + ".isCatalog=0 ");
				}
			} else if ((searchType & PopupSelectBean.LEAFRULE) == PopupSelectBean.LEAFRULE) {// 如果只查询未级
				if (sql.contains("group by ")) {
					sql = sql.replace("having 1=1", "having 1=1 and " + classCode.substring(0, classCode.indexOf(".")) + ".isCatalog=0 ");
				} else {
					sql = sql.replace("where 1=1", "where 1=1 and " + classCode.substring(0, classCode.indexOf(".")) + ".isCatalog=0 ");
				}
			} else if ((searchType & PopupSelectBean.BRANCHRULE) == PopupSelectBean.BRANCHRULE) {// 如果只查询非未级
				if (sql.contains("group by ")) {
					sql = sql.replace("having 1=1", "having 1=1 and " + classCode.substring(0, classCode.indexOf(".")) + ".isCatalog=1 ");
				} else {
					sql = sql.replace("where 1=1", "where 1=1 and " + classCode.substring(0, classCode.indexOf(".")) + ".isCatalog=1 ");
				}
			}
		}
		
		
		rs.setRetVal(new ArrayList());
		//解析分页语句
		String orderBy = "";
		if (selectBean.getOrderBy() != null) {
			orderBy = " order by " + selectBean.getOrderBy();
		}
		String querySql;
		if (mainId.length() > 0) {//引用单据的明细表不需要分页                	
			querySql = sql + orderBy;
		} else {//按行号进行分页
			if (orderBy.length() == 0) {
				orderBy = " order by " + ((PopField) selectBean.getAllFields().get(0)).getFieldName();
			}

			//查询from的位置
			int fromS = pos(sql,"from");			
			
			if (selectBean.getClassCode() != null && selectBean.getClassCode().length() > 0) {
				String table = selectBean.getClassCode().substring(0, selectBean.getClassCode().indexOf("."));
				querySql = "select * from (" + sql.substring(0, sql.indexOf("from", fromS)) + "," + table + ".isCatalog as childCount,ROW_NUMBER() over(" + orderBy + ") as row_id "
						+ sql.substring(sql.indexOf("from", fromS)) + ") a where  row_id between " + (pageSize * (pageNo - 1) + 1) + " and " + pageSize * pageNo+" order by row_id ";
			} else {
				String popSQL = "";
				/*注：CRM查询客户 在没改好之前 暂时使用distinct*/
				if (sql.contains("left join CRMClientInfoEmp")) {
					String[] arrayPop = selectBean.getPopsql().split(",");
					for (int i = 0; i < arrayPop.length; i++) {
						String str = "f" + (i + 1);
						if (selectBean.getPopsql().indexOf(str) != -1) {
							popSQL += str + ",";
						}
					}
					popSQL = "select distinct " + popSQL.substring(0,popSQL.length() -1)+" ";
					querySql = popSQL + " from (" + sql.substring(0, sql.indexOf("from", fromS)) + ",ROW_NUMBER() over(" + orderBy + ") as row_id " + sql.substring(sql.indexOf("from", fromS))
					+ ") a where  row_id between " + (pageSize * (pageNo - 1) + 1) + " and " + pageSize * pageNo+"  ";
				} else {
					popSQL = "select * ";
					querySql = popSQL + " from (" + sql.substring(0, sql.indexOf("from", fromS)) + ",ROW_NUMBER() over(" + orderBy + ") as row_id " + sql.substring(sql.indexOf("from", fromS))
					+ ") a where  row_id between " + (pageSize * (pageNo - 1) + 1) + " and " + pageSize * pageNo+" order by row_id ";
				}
				
			}
		}
		
        if(querySql.indexOf("@TOPID") > 0){ //@TOPID多单引用时，一级弹出窗的结果，这里如果出现这个符号，说明是多单引用的明细弹出窗要替换这个值
        	if(topId!=null && topId.length()>0){
        		if(topId.indexOf(";")> 0){
        			//一级有多个值。
        			Pattern pattern = Pattern.compile("([\\w\\.]*)[\\s]*=[\\s]*'@TOPID'");
                    Matcher matcher = pattern.matcher(querySql);
                    String[] topIds = topId.split(";");
                    String topIdStr = "";
                    for(String tid:topIds){
                    	topIdStr +=",'"+tid+"'";
                    }
                    topIdStr = topIdStr.substring(1);
                    while (matcher.find()) {
                        String cfd = matcher.group(1);
                        String all = matcher.group();
                        querySql = querySql.substring(0,querySql.indexOf(all))+""+cfd +" in("+topIdStr+")"+querySql.substring(querySql.indexOf(all) + all.length());
                    }
        		}else{
        			querySql = querySql.replaceAll("@TOPID", topId);
        		}
        	}
        	
        }

		final String querysql = querySql;

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							Statement cs = conn.createStatement();
							KRLanguageQuery klQuery = new KRLanguageQuery();
							BaseEnv.log.debug("DynDBManager.popSelect selectName="+selectBean.getName()+"; sql="+querysql);
							ResultSet rset = cs.executeQuery(querysql);
							while (rset.next()) {
								int size = selectBean.getAllFields().size();
								Object[] os;
								if (selectBean.getClassCode() != null && selectBean.getClassCode().length() > 0) {
									os = new Object[size + 1];
								} else {
									os = new Object[size];
								}
								int cid = 0;

								for (int i = 0; i < size; i++) {
									Object value = rset.getObject(i + 1);
									value = value == null ? "" : value.toString().trim();
									//部分字段中有“；”号要进行转码，如图片和符件
									//value  = value.toString().replaceAll(";", "%3B");
									value = GlobalsTool.replaceSpecLitter(value.toString());
									os[cid] = value;
									cid++;
									PopField field = (PopField) selectBean.getAllFields().get(i);
									if (field.fieldName.indexOf(".") > 0) {
										DBFieldInfoBean fb = GlobalsTool.getFieldBean(field.fieldName.substring(0, field.fieldName.indexOf(".")), field.fieldName.substring(field.fieldName
												.indexOf(".") + 1));
										if (fb != null && (fb.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fb.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE)) {
											klQuery.addLanguageId(value.toString());
										}
									}
								}

								if (selectBean.getClassCode() != null && selectBean.getClassCode().length() > 0) {
									os[cid] = rset.getString(cid + 1);
								}
								((ArrayList) rs.getRetVal()).add(os);
							}
							rs.setRealTotal(((ArrayList) rs.getRetVal()).size());
							//根据languageID取得显示名
							HashMap map = klQuery.query(conn);
							ArrayList list = (ArrayList) rs.getRetVal();
							HashMap lastValue = new HashMap();
							for (int i = 0; i < list.size(); i++) {
								Object[] os = (Object[]) list.get(i);
								for (int j = 0; j < selectBean.getAllFields().size(); j++) {
									PopField field = (PopField) selectBean.getAllFields().get(j);
									if (field.fieldName.indexOf(".") > 0) {
										DBFieldInfoBean fb = GlobalsTool.getFieldBean(field.fieldName.substring(0, field.fieldName.indexOf(".")), field.fieldName.substring(field.fieldName
												.indexOf(".") + 1));
										if (fb != null && (fb.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || fb.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE)) {
											KRLanguage lan = (KRLanguage) map.get(os[j]);
											if (lan != null) {
												os[j] = GlobalsTool.replaceSpecLitter(lan.get(locale.toString()));
											}
										}
									}
									//检查本列是否设置了重复不显示，并且是重复值
									if (field.getRepeatNotShow() == 1) {
										if (lastValue.get(field.getAsName()) != null && lastValue.get(field.getAsName()).equals(os[j])) {
											os[j] = "";
										} else {
											lastValue.put(field.getAsName(), os[j]);
										}
									}
								}

							}

						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Pop Select  data Error selectName="+selectBean.getName()+";sql=" + querysql, ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.retCode = retCode;

		return rs;
	}

	/**
	 * 添加操作日志数据
	 * @param operation 0:添加，1：修改，2：删除，3：转交，4：暂存，5：草稿添加，6：草稿修改，7：草稿删除，8：草稿审核，9：列配置，10：导入，11：零售上传，12：手机上传 13:单据推送,14:扩展,15：导出
	 * @param values //内容
	 * @param valuesOld //如果是修改动作：要记录修改前的内容
	 * @param tableInfo 操作表
	 * @param locale //
	 * @param userId //用户ID
	 * @param userName //用户名称
	 * @param SCompanyID //分支机构
	 * @param opResult //true 成功
	 * @param reason //原因
	 * @return
	 */

	public Result addLog(final int operation, HashMap values, HashMap valuesOld, DBTableInfoBean tableInfo, String locale, final String userId, final String userName, final String SCompanyID ,String extendFun,String billTypeName) {

		/*遍历当前表的所有字段，判断字段的isLog=1的，将其内容添加到日志内容contentstr中*/
		String contentstr = "";
		List<DBFieldInfoBean> listField = (List<DBFieldInfoBean>) tableInfo.getFieldInfos();
		for (int i = 0; i < listField.size(); i++) {
			DBFieldInfoBean fieldBean = listField.get(i);
			if (fieldBean.getIsLog()==1) {
				String fn = fieldBean.getDisplay().get(locale);
				Object vn = "";
				Object ovn = "";
				
				//根据字段的输入类型，解析字段对应值的显示值
				if (fieldBean.getInputType() == 4) {
					if(values.get(fieldBean.getFieldName()) instanceof KRLanguage){
						vn = values.get(fieldBean.getFieldName()) == null ? "" : ((KRLanguage) values.get(fieldBean.getFieldName())).get(locale);
					}else{
						valuesOld=values;
					}
					
					if (valuesOld != null) {
						if(valuesOld.get(fieldBean.getFieldName()) == null){
							ovn = "";
						}else{
							if( valuesOld.get(fieldBean.getFieldName()) instanceof KRLanguage){
								ovn = ((KRLanguage) valuesOld.get(fieldBean.getFieldName())).get(locale);
							}else{
								if(fieldBean.getInputType() == 4){
									//是多语言时，数据从LANGUAGEQUERY中取相应的多语言
									HashMap languageMap = (HashMap)valuesOld.get("LANGUAGEQUERY");
									if(languageMap == null){
										ovn = valuesOld.get(fieldBean.getFieldName());
									}else{
										Object lMap = (Object)languageMap.get(valuesOld.get(fieldBean.getFieldName()));
										if(lMap != null && languageMap.get(valuesOld.get(fieldBean.getFieldName())) instanceof KRLanguage ){
											ovn = ((KRLanguage) lMap).get(locale);
										}else{
											ovn = valuesOld.get(fieldBean.getFieldName());
										}
									}
								}else{
									ovn = valuesOld.get(fieldBean.getFieldName());
								}
							}
						}
					}
					if(!(values.get(fieldBean.getFieldName()) instanceof KRLanguage))vn=ovn;
				}else if(fieldBean.getInputType()==2){					
					Result rs=this.queryPopDisVal(fieldBean, values, SCompanyID, userId);
					if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS&&rs.retVal!=null){
						vn=rs.retVal.toString();
					}else{
						vn=values.get(fieldBean.getFieldName()).toString();
					}
			 
					if(operation == 1){
						Result rso=this.queryPopDisVal(fieldBean, valuesOld, SCompanyID, userId);
						if(rso.retCode==ErrorCanst.DEFAULT_SUCCESS&&rso.retVal!=null){
							ovn=rso.retVal.toString();
						}else{
							ovn=valuesOld.get(fieldBean.getFieldName()).toString();
						}
					}
				}else {
					vn =values.get(fieldBean.getFieldName());
					if (operation == 1 && valuesOld != null) {
						ovn =valuesOld.get(fieldBean.getFieldName());
					}
					if(fieldBean.getInputType()==6&&fieldBean.getInputTypeOld()==1){			
						vn=GlobalsTool.getEnumerationItemsDisplay(fieldBean.getRefEnumerationName(), vn.toString(),locale);
						if (operation == 1 && valuesOld != null) {
							ovn =GlobalsTool.getEnumerationItemsDisplay(fieldBean.getRefEnumerationName(), ovn.toString(),locale);
						}
					}
				}
				
				//根据当前的操作类型，以不同方式记录日志
				if (operation == 1) {
					contentstr += fn + "修改前" + ":" + ovn + ";" + "修改后" + ":" + vn + ";";
				}else {
					contentstr += fn + ":" + vn + ";";
				}
			}
		}
		final String content = contentstr + "Id:" + values.get("id") + ";";
		final String billType = tableInfo.getTableName();
		billTypeName =billTypeName.length()>0?billTypeName:tableInfo.getDisplay().get(locale);

		return addLog(operation, userId, userName, SCompanyID, content, billType, billTypeName,extendFun);
	}
	
	/**
	 * 
	 * @param operation 0:添加，1：修改，2：删除，3：转交，4：暂存，5：草稿添加，6：草稿修改，7：草稿删除，8：草稿过帐，9：列配置，10：导入，11：零售上传，12：手机上传
	 * @param userId
	 * @param userName
	 * @param SCompanyID
	 * @param opResult
	 * @param reason
	 * @param content
	 * @param billType
	 * @param billTypeName
	 * @return
	 */
	public Result queryPopDisVal(final DBFieldInfoBean fi,final HashMap values,final String SCompanyID,final String userId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "";
						try {
							System.out.println(fi.getFieldName()+" "+values.get(fi.getFieldName()));
							String refsql = getRefSql(fi,false, values.get(fi.getFieldName()).toString(), SCompanyID, BaseEnv.tableInfos, false, values, userId);

							if (refsql != null) {
								refsql = refsql.replaceAll("(?i)\\bEMPRIGHT\\b", ""); //去掉语句中的部门临时标志
								refsql = refsql.replaceAll("(?i)\\bNORIGHT\\b", ""); //去掉语句中的部门临时标志
								Statement ccs = conn.createStatement();
								BaseEnv.log.debug(refsql);
								ResultSet crset = ccs.executeQuery(refsql);
								String content="";
								if (crset.next()) {
									for (int j = 0; j < fi.getSelectBean().getViewFields().size(); j++) {
										PopField posf = ((PopField) fi.getSelectBean().getViewFields().get(j));
										
										if(true==posf.parentDisplay){
											content+=crset.getObject(j + 1)+",";
										}
									}
								}
								if(content.length()>0){
									rs.setRetVal(content.substring(0,content.length()-1));
								}
							}
						} catch (Exception ex) {
							BaseEnv.log.error("DynDBManager.addLog  data Error :" + sql, ex);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}

					}
				});

				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 
	 * @param operation 0:添加，1：修改，2：删除，3：转交，4：暂存，5：草稿添加，6：草稿修改，7：草稿删除，8：草稿过帐，9：列配置，10：导入，11：零售上传，12：手机上传，13：单据推送,14：扩展,15:导出
	 * @param userId
	 * @param userName
	 * @param SCompanyID
	 * @param opResult
	 * @param reason
	 * @param content
	 * @param billType
	 * @param billTypeName
	 * @param extendFun 扩展功能
	 * @return
	 */
	public Result addLog(final int operation, final String userId, final String userName, final String SCompanyID,  final String content,
			final String billType, final String billTypeName,final String extendFun) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "";
						try {
							sql = "insert into tblLog(id,userId,billType,operation,SCompanyID,operationTime,userName,billTypeName,content,extendFun) " + "values (?,?,?,?,?,?,?,?,?,?)";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, IDGenerater.getId());
							ps.setString(2, userId);
							ps.setString(3, billType);
							ps.setString(4, operation + "");
							ps.setString(5, SCompanyID);
							ps.setString(6, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
							ps.setString(7, userName);
							ps.setString(8, billTypeName);
							ps.setString(9, content);
							ps.setString(10, extendFun);

							int ret = ps.executeUpdate();
						} catch (Exception ex) {
							BaseEnv.log.error("DynDBManager.addLog  data Error :" + sql, ex);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}

					}
				});

				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	public Result execAdd(String tableName, Map allTables, HashMap values,Hashtable sessionSet, String userId, String path, String defineInfo, Connection conn, MessageResources resources, Locale locale, String saveType) {
		Result rs = new Result();
		/**
		 * 处理主从表
		 * 1、插入主表
		 * 2、查出主表对应的ID
		 * 2、把ID添入子表对应的map
		 * 3、插入子表
		 */
		String id = (String) values.get("id");
		if (id == null || id.trim().length() == 0) {
			id = IDGenerater.getId();
			values.put("id", id);
		}
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo((Hashtable) allTables, tableName);
		//转换特殊字符
		Result sp_values = GlobalsTool.conversionSpecialCharacter(tableInfo.getFieldInfos(), values);
		values = (HashMap) sp_values.getRetVal();

		String[] returnValue = new String[3];//返回值 
		try {


			ArrayList childTableList = DDLOperation.getChildTables(tableName, allTables);
			if ("quoteDraft".equals(saveType)) {
				/*验证主表非空字段*/
				rs = validator(tableInfo.getFieldInfos(), values, locale.toString());
				if (rs.retCode == ErrorCanst.RET_FIELD_IS_NULL) {
					return rs;
				}
				/*验证从表非空字段*/
				for (int i = 0; i < childTableList.size(); i++) {
					DBTableInfoBean childTb = (DBTableInfoBean) childTableList.get(i);
					if (childTb.getTableSysType() != null && childTb.getTableSysType().length() > 0 && BaseEnv.systemSet.get(childTb.getTableSysType()).getSetting().equals("true")) {
						ArrayList childList = (ArrayList) values.get("TABLENAME_" + childTb.getTableName());
						if (childTb.getIsNull() == 1 && childList.size() == 0) {
							rs.retCode = ErrorCanst.RET_FIELD_IS_NULL;
							rs.setRetVal(childTb.getDisplay().get(locale.toString()) + resources.getMessage(locale, "common.noData.error") + ",");
							return rs;
						}
						for (int j = 0; j < childList.size(); j++) {
							HashMap childMap = (HashMap) childList.get(j);
							rs = validator(childTb.getFieldInfos(), childMap, locale.toString());
							if (rs.retCode == ErrorCanst.RET_FIELD_IS_NULL) {
								return rs;
							}
						}
					}
				}
			}
			//设置主表的缺省值
			Result rs_values = setValuesDefault(tableInfo.getFieldInfos(), values,sessionSet);//设置缺省值
			this.setChildDefault(tableInfo, values,sessionSet);
			values = (HashMap) rs_values.getRetVal();

			Obj beforeObject = new Obj();
			Result defineRs = null;
			BaseCustomFunction impl = null;
			if (!"saveDraft".equals(saveType) && !"printSave".equals(saveType)) {
				//执行插入前自定义语句
				defineRs = defineSql(conn, "add", false, tableName, (Hashtable) allTables, values, id, userId, defineInfo, resources, locale);
				if (defineRs.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
					rs.setRetCode(defineRs.getRetCode());
					rs.setRetVal(defineRs.getRetVal());
					BaseEnv.log.error("DynDBManager Before add defineSql Error code = " + defineRs.getRetCode() + " Msg=" + defineRs.getRetVal());
					return rs;
				}

				//执行插入前自定义语句
				impl = (BaseCustomFunction) BaseEnv.functionInterface.get(tableName);
				if (impl != null) {
					Result interfaceRs = impl.onBeforeAdd(conn, tableName, allTables, values, beforeObject);
					if (interfaceRs.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
						rs.setRetCode(interfaceRs.getRetCode());
						BaseEnv.log.error("DynDBManager onBeforeAdd Error code = " + interfaceRs.getRetCode() + " Msg=" + interfaceRs.getRetVal());
						return rs;
					}
				}
			}
			//执行主表插
			Result ires = execInert(conn, tableName, allTables, values, saveType, resources, locale);
			if (ires.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
				BaseEnv.log.debug("DynDBManager.execAdd Insert Table " + tableName + " Info: " + ires.getRetVal());
				rs.setRetCode(ires.getRetCode());
  				rs.setRetVal(ires.getRetVal());
				return rs;
			}
			Result upCatalog = updateParentCatalog(conn, tableName, allTables, values);
			if (upCatalog.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				BaseEnv.log.debug("DynDBManager.execAdd Update Table " + tableName + " Info: " + upCatalog.getRetVal());
				rs.setRetCode(upCatalog.getRetCode());
				rs.setRetVal(upCatalog.getRetVal());
				return rs;
			}
			//保存主表新增脚本
			TestRW.saveToSql(path, addSql);
			addSql = "";

			for (int i = 0; i < childTableList.size(); i++) {
				DBTableInfoBean childTb = (DBTableInfoBean) childTableList.get(i);
				ArrayList childList = (ArrayList) values.get("TABLENAME_" + childTb.getTableName());
				if (childList == null) {
					continue;
				}
				for (int j = 0; j < childList.size(); j++) {
					HashMap childMap = (HashMap) childList.get(j);
					childMap.put("id", IDGenerater.getId());
					childMap.put("f_ref", id);
					//转换特殊字符
					sp_values = GlobalsTool.conversionSpecialCharacter(childTb.getFieldInfos(), childMap);
					childMap = (HashMap) sp_values.getRetVal();
					//执行从表插入
					ires = execInert(conn, childTb.getTableName(), allTables, childMap, "", resources, locale);
					//如果是插入重复键，跳过
					if (ires.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && ires.getRetCode() != 2601) {
						BaseEnv.log.debug("DynDBManager.execAdd Insert Table " + childTb.getTableName() + " Info: " + ires.getRetVal());
						rs.setRetCode(ires.getRetCode());
						rs.setRetVal(ires.getRetVal());
						return rs;
					}
					ires.setRetCode(ires.getRetCode() == 2601 ? ErrorCanst.DEFAULT_SUCCESS : ires.getRetCode());
					//保存新增脚本
					TestRW.saveToSql(path, addSql);
					addSql = "";

				}
			}

			if (!"saveDraft".equals(saveType) && !"printSave".equals(saveType)) {
				//执行表自定义语句
				defineRs = defineSql(conn, "add", true, tableName, (Hashtable) allTables, values, id, userId, defineInfo, resources, locale);
				if (defineRs.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
					rs.setRetCode(defineRs.getRetCode());
					rs.setRetVal(defineRs.getRetVal());
					if (defineRs.getRetVal() instanceof String[]) {
						String retVals[] = (String[]) defineRs.getRetVal();
						BaseEnv.log.error("DynDBManager After add defineSql Error code = " + defineRs.getRetCode() + " Msg=" + getResource(resources, locale, retVals));
						return rs;

					} else {
						BaseEnv.log.error("DynDBManager After add defineSql Error code = " + defineRs.getRetCode() + " Msg="
								+ (defineRs.getRetVal() == null ? "" : getResource(resources, locale, new String[] { defineRs.getRetVal().toString() })));
						return rs;
					}
				}
				if (defineRs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
					rs.setRetCode(defineRs.retCode);
					returnValue[1] = (String) defineRs.retVal;
				}
				//添加后的动作
				if (impl != null) {
					Result interfaceRs = impl.onAfterAdd(conn, tableName, allTables, values, id, beforeObject);
					if (interfaceRs.getRetCode() < 0) {
						rs.setRetCode(interfaceRs.getRetCode());
						BaseEnv.log.error("DynDBManager onAfterAdd Error code = " + interfaceRs.getRetCode() + "Msg=" + interfaceRs.getRetVal());
						return rs;
					}
				}
			}
		} catch (Exception ex) {
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			BaseEnv.log.error("add data Error :", ex);
			return rs;
		}
		returnValue[0] = id;
		rs.setRetVal(returnValue);
		return rs;
	}

	public Result execAddN(String tableName, Map allTables, HashMap values, String userId, String path, String defineInfo, Connection connection, MessageResources resources) {
		Result rs = new Result();
		/**
		 * 处理主从表
		 * 1、插入主表
		 * 2、查出主表对应的ID
		 * 2、把ID添入子表对应的map
		 * 3、插入子表
		 */
		String id = values.get("id").toString();
		try {
			Connection conn = connection;
			Obj beforeObject = new Obj();
			//执行主表插入
			Result ires = execInert(conn, tableName, allTables, values, "", resources, null);
			if (ires.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
				BaseEnv.log.debug("Insert Table " + tableName + " Info: " + ires.getRetVal());
				rs.setRetCode(ires.getRetCode());
				rs.setRetVal(ires.getRetVal());
				return rs;
			}
			Result upCatalog = updateParentCatalog(conn, tableName, allTables, values);
			if (upCatalog.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				BaseEnv.log.debug("Update Table " + tableName + " Info: " + upCatalog.getRetVal());
				rs.setRetCode(upCatalog.getRetCode());
				rs.setRetVal(upCatalog.getRetVal());
				return rs;
			}
			ArrayList childTableList = DDLOperation.getChildTables(tableName, allTables);

			for (int i = 0; i < childTableList.size(); i++) {
				DBTableInfoBean childTb = (DBTableInfoBean) childTableList.get(i);
				ArrayList childList = (ArrayList) values.get("TABLENAME_" + childTb.getTableName());
				if (childList == null) {
					continue;
				}
				for (int j = 0; j < childList.size(); j++) {
					HashMap childMap = (HashMap) childList.get(j);
					childMap.put("id", IDGenerater.getId());
					childMap.put("f_ref", id);
					//执行从表插入
					ires = execInert(conn, childTb.getTableName(), allTables, childMap, "", resources, null);
					if (ires.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
						BaseEnv.log.debug("Insert Table " + childTb.getTableName() + " Info: " + ires.getRetVal());
						rs.setRetCode(ires.getRetCode());
						rs.setRetVal(ires.getRetVal());
						return rs;
					}
				}
			}

		} catch (Exception ex) {
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			BaseEnv.log.error("add data Error :", ex);
			return rs;
		}
		rs.setRetVal(id);
		return rs;
	}
	/**
	 * 添加界面执行的动作
	 * @param tableName
	 * @param allTables
	 * @param values
	 * @param userId
	 * @param path
	 * @param defineInfo
	 * @param resources
	 * @param locale
	 * @param saveType
	 * @param loginBean
	 * @param workFlow
	 * @param deliverTo
	 * @param props
	 * @param mop
	 * @return
	 */
	public Result addView(final String tableName, final Hashtable allTables, final HashMap values, final String defineInfo, final MessageResources resources,
			final Locale locale, final LoginBean loginBean) {
		final Result rs = new Result();
		final Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(loginBean.getId()) ;
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						//执行插入界面自定义语句
						Result defineRs = defineSql(conn, "addView", false, tableName, (Hashtable) allTables, values, "", loginBean.getId(), defineInfo, resources, locale);
						if (defineRs.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
							rs.setRetCode(defineRs.getRetCode());
							rs.setRetVal(defineRs.getRetVal());
							BaseEnv.log.error("DynDBManager addView defineSql Error code = " + defineRs.getRetCode() + " Msg=" + defineRs.getRetVal());
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	/**
	 * 添加界面执行的动作
	 * @param tableName
	 * @param allTables
	 * @param values
	 * @param userId
	 * @param path
	 * @param defineInfo
	 * @param resources
	 * @param locale
	 * @param saveType
	 * @param loginBean
	 * @param workFlow
	 * @param deliverTo
	 * @param props
	 * @param mop
	 * @return
	 */
	public Result updateView(final String tableName, final Hashtable allTables, final HashMap values, final String defineInfo, final MessageResources resources,
			final Locale locale, final LoginBean loginBean) {
		final Result rs = new Result();
		final Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(loginBean.getId()) ;
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						//执行插入界面自定义语句
						Result defineRs = defineSql(conn, "updateView", false, tableName, (Hashtable) allTables, values, values.get("id")+"", loginBean.getId(), defineInfo, resources, locale);
						if (defineRs.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
							rs.setRetCode(defineRs.getRetCode());
							rs.setRetVal(defineRs.getRetVal());
							BaseEnv.log.error("DynDBManager updateView defineSql Error code = " + defineRs.getRetCode() + " Msg=" + defineRs.getRetVal());
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 增加一条动态数据，包括主从表，
	 * 主表中的每个字段都在valus的Map中
	 * 从表的Key为从表的tableId,values为 从表的ArrayList
	 * @param tableId String 主表的代号
	 * @param allTables HashMap 所有表的描述
	 * @param values HashMap 数据
	 * @return Result
	 */
	public Result add(final String tableName, final Hashtable<String,DBTableInfoBean> allTables, final HashMap values, final String userId, final String path, final String defineInfo, final MessageResources resources,
			final Locale locale, final String saveType, final LoginBean loginBean, final OAWorkFlowTemplate workFlow, final String deliverTo, final Hashtable props) {
		//这里需要对内容进行再次数据正确性校验，因为数据来源不只是界面，还可能是导入，零售，所以需要在这重新时行数据正确性校验。
		Result vrs = Validator.validator("saveDraft".equals(saveType) ? true : false, tableName, allTables, values, resources, locale);
		if (vrs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return vrs;
		}
		if(tableName.equalsIgnoreCase("tblCompany")){ //这张表因以前是用clientFlag来区分客户，供应商的，现改成moduleType,为了适应以前的老程序，做此特殊处理
			values.put("ClientFlag", values.get("moduleType"));
		}
		values.put("createBy", loginBean.getId());
		values.put("lastUpdateBy", loginBean.getId());

		final Result rs = new Result();
		final Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(userId) ;

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						/*删除草稿*/
						//引用草稿，并保存为草稿时，saveType为saveDraft,这里先删除老的草稿，再生成一个新的草稿
						if (("quoteDraft".equals(saveType) || "saveDraft".equals(saveType)) && values.get("id") != null &&values.get("id").toString().length() > 0) { 
							String[] files = new String[] { "", "" };
							ArrayList billNos = new ArrayList();
							Result rss = delete(tableName, allTables, (String)values.get("id"), userId,  resources, locale,  files, billNos, conn,false);
							rs.setRetCode(rss.getRetCode());
							/*if (rss.retCode == ErrorCanst.DEFAULT_SUCCESS) {
								DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, tableName);
								List fieldLists = tableInfo.getFieldInfos();
								for (int i = 0; i < fieldLists.size(); i++) {
									DBFieldInfoBean fieldInfo = (DBFieldInfoBean) fieldLists.get(i);
									if (fieldInfo.getFieldIdentityStr() != null && 
											DBFieldInfoBean.FIELD_IDENTIFIER.equals(fieldInfo.getFieldIdentityStr())&& 
											!fieldInfo.getFieldName().equals("TrackNo")) {
										// 单据编号
										String key = "";
										String defaultValue = fieldInfo.getDefaultValue();
										if (defaultValue != null && !"".equals(defaultValue)) {
											// 存在默认值
											key = defaultValue;
										} else {
											key = tableInfo.getTableName() + "_" + fieldInfo.getFieldName();
										}
										BillNo billno = BillNoManager.find(key, conn);
										if (billno.isFillBack()) {
											String valStr = BillNoManager.find(key, loginBean, conn);
											//先删除旧的单据编号
											billno.remove(String.valueOf(values.get(fieldInfo.getFieldName())), conn);
											values.put(fieldInfo.getFieldName(), valStr);
										}
									}
								}
							}*/
						}
						//系统配置启用保存前打印，删除打印数据
						if (BaseEnv.systemSet.get("saveBeforePrint") != null && "true".equals(BaseEnv.systemSet.get("saveBeforePrint").getSetting()) && tableName.indexOf("CRMClientInfo") == -1) {
							delPrintData(conn, tableName, userId);
						}

						if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
							//生成单据编号
							DBTableInfoBean tableInfo = allTables.get(tableName);
							List fieldLists = tableInfo.getFieldInfos();
							//如果是草稿过帐操作不应该重新生成单据编号
							if (!(("quoteDraft".equals(saveType) || "saveDraft".equals(saveType)) && values.get("id") != null &&values.get("id").toString().length() > 0)) { 
								for (int i = 0; i < fieldLists.size(); i++) {
									DBFieldInfoBean fieldInfo = (DBFieldInfoBean) fieldLists.get(i);
									if (fieldInfo.getFieldIdentityStr() != null && DBFieldInfoBean.FIELD_IDENTIFIER.equals(fieldInfo.getFieldIdentityStr())) {
										// 单据编号
										String key = "";
										String defaultValue = fieldInfo.getDefaultValue();
										if (defaultValue != null && !"".equals(defaultValue)) {
											// 存在默认值
											key = defaultValue;
										} else {
											key = tableInfo.getTableName() + "_" + fieldInfo.getFieldName();
										}
										BillNo billno = BillNoManager.find(key,conn);
										if (billno != null) {
											if (billno.isFillBack()||values.get(fieldInfo.getFieldName())==null||values.get(fieldInfo.getFieldName()).equals("")) { 
												//启用单据编号连续后，从数据库读编号,或者单据编号为空
												String valStr = BillNoManager.find(key, values, loginBean,conn);
												if ("".equals(valStr)) {
													/* 单据编号无法生成 */
													rs.setRetCode(ErrorCanst.BILL_ADD_WORK_FLOW_ERROR);
													rs.setRetVal(resources.getMessage("erp.billno.error"));
													return;
												} else {
													values.put(fieldInfo.getFieldName(), valStr);
												}
											}
										} 
									}
								}
							}
							
							
							long curTime = System.currentTimeMillis();
							Result result = execAdd(tableName, allTables, values,sessionSet, userId, path, defineInfo, conn, resources, locale, saveType);
							System.out.println("=====------添加后"+System.currentTimeMillis()+":"+(System.currentTimeMillis()-curTime));
							rs.setRetCode(result.getRetCode());
							if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
								/*假如启用了追踪单号，检查主表及主表对应的明细表中是否存在系统类型为追踪单号的字段，如果存在向追踪单号表（tblTrackBill）中插入数据*/
								if (BaseEnv.systemSet.get("TrackNo") != null && "true".equals(BaseEnv.systemSet.get("TrackNo").getSetting())) {
									insertTrackBill(conn, tableName, values);
								}

								String[] returnValue = (String[]) result.getRetVal();

								/*添加工作流数据*/
								if (!"printSave".equals(saveType) && null != workFlow && 1 == workFlow.getTemplateStatus()) {
									try {
										if(BaseEnv.workFlowDesignBeans.get(workFlow.getId()) == null){
											rs.setRetCode(ErrorCanst.BILL_ADD_WORK_FLOW_ERROR);
											rs.setRetVal(resources.getMessage("com.add.workfow.error"));
										}else{
											Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
											boolean isLastSunCompany = sessionSet.get("IsLastSCompany")==null?true: Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
											Result rsValue = detail(tableName, allTables, (String) values.get("id"), loginBean.getSunCmpClassCode(), props, userId, isLastSunCompany,
													 "", conn);
											HashMap hm = (HashMap) rsValue.retVal;
											
											Result rss = new OAMyWorkFlowMgt().addOAMyWorkFlow(workFlow.getId(), tableName, hm, loginBean, locale, resources, conn);
											if (rss.retCode == ErrorCanst.WORK_FLOW_NO_NEXT_NODE) {
												rs.setRetCode(rss.retCode);
											} else if (rss.retCode != ErrorCanst.DEFAULT_SUCCESS) {
												rs.setRetCode(ErrorCanst.BILL_ADD_WORK_FLOW_ERROR);
												rs.setRetVal(resources.getMessage("com.add.workfow.error"));
											} else {
												rs.setRetCode(rss.getRetCode());
											}
											returnValue[2] = (String) rss.getRetVal();
										}
									} catch (Exception ex) {
										BaseEnv.log.error("UserFunctionAction.java add method :" , ex);
										rs.setRetCode(ErrorCanst.BILL_ADD_WORK_FLOW_ERROR);
										rs.setRetVal(resources.getMessage("com.add.workfow.error"));
										conn.rollback();
									}
								}
								rs.setRetVal(returnValue);
							} else {
								rs.setRetVal(result.retVal);
							}

						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	private void insertTrackBill(Connection conn, String tableName, HashMap values) throws SQLException {
		String id = values.get("id").toString();
		String BillNo = values.get("BillNo") == null ? "" : values.get("BillNo").toString();
		String BillDate = values.get("BillDate") == null ? "" : values.get("BillDate").toString();
		String createBy = values.get("createBy") == null ? "" : values.get("createBy").toString();
		String SCompanyID = values.get("SCompanyID") == null ? "" : values.get("SCompanyID").toString();
		String TrackNo = "";

		Statement st = conn.createStatement();
		ResultSet rst;
		//遍历主表所有的字段，查询是否存在追踪单号字段并向追踪单号表中插入数据
		List<DBFieldInfoBean> fields = ((DBTableInfoBean) BaseEnv.tableInfos.get(tableName)).getFieldInfos();
		for (int i = 0; i < fields.size(); i++) {
			DBFieldInfoBean field = fields.get(i);
			if ("TrackNo".equals(field.getFieldSysType()) && values.get(field.getFieldName()) != null && values.get(field.getFieldName()).toString().length() > 0) {
				TrackNo = values.get(field.getFieldName()).toString();
				st.execute("insert into tblTrackBill(id,RelationID,BillNo,BillType,BillDate,TrackNo,createby,lastupdateBy,createTime,lastupdateTime" + ",ScompanyID) values ('" + IDGenerater.getId()
						+ "','" + id + "','" + BillNo + "','" + tableName + "','" + BillDate + "','" + values.get(field.getFieldName()).toString() + "','" + createBy + "','" + createBy + "','"
						+ BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss) + "','" + BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss) + "','" + SCompanyID + "')");
				break;
			}
		}

		//遍历所有明细表，查询明细表中是否有追踪单号，如果存在，查询当前及记录的明细中的追踪单号
		ArrayList childList = DDLOperation.getChildTables(tableName, BaseEnv.tableInfos);
		for (int i = 0; i < childList.size(); i++) {
			DBTableInfoBean childTable = (DBTableInfoBean) childList.get(i);
			fields = childTable.getFieldInfos();
			for (int j = 0; j < fields.size(); j++) {
				DBFieldInfoBean field = fields.get(j);
				if ("TrackNo".equals(field.getFieldSysType())) {
					boolean flag = false;
					rst = st.executeQuery("select " + field.getFieldName() + " from " + childTable.getTableName() + " where f_ref='" + id + "' and len(" + field.getFieldName() + ")>0 and "
							+ field.getFieldName() + "!='" + TrackNo + "' group by " + field.getFieldName());
					while (rst.next()) {
						st.addBatch("insert into tblTrackBill(id,RelationID,BillNo,BillType,BillDate,TrackNo,createby,lastupdateBy,createTime,lastupdateTime" + ",ScompanyID) values ('"
								+ IDGenerater.getId() + "','" + id + "','" + BillNo + "','" + tableName + "','" + BillDate + "','" + rst.getString(1) + "','" + createBy + "','" + createBy + "','"
								+ BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss) + "','" + BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss) + "','" + SCompanyID
								+ "')");
						flag = true;
					}
					if (flag) {
						st.executeBatch();
					}

					break;
				}
			}
		}
	}

	private void deleteTrackBill(Connection conn, String tableName, String id) throws SQLException {
		Statement st = conn.createStatement();
		//遍历主表,明细表所有的字段，查询是否存在追踪单号字段并删除追踪单号表中的数据
		List<DBFieldInfoBean> fields = ((DBTableInfoBean) BaseEnv.tableInfos.get(tableName)).getFieldInfos();
		boolean flag = false;
		for (int i = 0; i < fields.size(); i++) {
			DBFieldInfoBean field = fields.get(i);
			if ("TrackNo".equals(field.getFieldSysType())) {
				flag = true;
				break;
			}
		}
		//已经存在追踪单号，不用在子表中查询
		if (!flag) {
			ArrayList childList = DDLOperation.getChildTables(tableName, BaseEnv.tableInfos);
			for (int i = 0; i < childList.size(); i++) {
				DBTableInfoBean childTable = (DBTableInfoBean) childList.get(i);
				for (int j = 0; j < childTable.getFieldInfos().size(); j++) {
					DBFieldInfoBean field = childTable.getFieldInfos().get(j);
					if ("TrackNo".equals(field.getFieldSysType())) {
						flag = true;
						break;
					}
				}
			}
		}
		if (flag) {
			st.addBatch("delete from tblTrackBill where RelationID = '"+id+"'");
			st.executeBatch();
		}
	}

	private void delPrintData(Connection conn, String tableName, final String loginId) throws SQLException {
		Statement st = conn.createStatement();
		ArrayList list = DDLOperation.getChildTables(tableName, BaseEnv.tableInfos);
		for (int i = 0; i < list.size(); i++) {
			DBTableInfoBean cb = (DBTableInfoBean) list.get(i);
			st.addBatch("delete from " + cb.getTableName() + " where f_ref in (select id from " + tableName + " where  workFlowNodeName='print' and createBy='" + loginId + "')");
		}
		st.addBatch("delete from " + tableName + " where  workFlowNodeName='print' and createBy='" + loginId + "'");
		st.executeBatch();
	}

	private Result updateParentCatalog(Connection conn, String tableName, Map allTables, HashMap values) {
		Result rs = new Result();
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);

		if (tableInfo.getClassFlag() == 1) {//是分类表要更新父类的目录类型

			String classCode = values.get("classCode").toString();
			if (classCode.length() > 5) {
				try {
					String sql = "update " + tableName + " set isCatalog=1 where classCode=?";
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setObject(1, classCode.substring(0, classCode.length() - 5));
					pstmt.execute();
				} catch (Exception ex) {
					rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
					rs.setRetVal(ex.getMessage());
					BaseEnv.log.error("update parent isCatalog Error:", ex);
					return rs;
				}
			}
		}
		return rs;
	}

	private Result delParentCatalog(Connection conn, String tableName, Map allTables, String id) {
		Result rs = new Result();
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);

		if (tableInfo.getClassFlag() == 1) {//是分类表要更新父类的目录类型
			try {
				String sql = "select classCode from " + tableInfo.getTableName() + " where id='" + id + "'";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rss = pstmt.executeQuery();
				String classCode = "";
				if (rss.next()) {
					classCode = rss.getString(1);
				}
				if (classCode.length() > 5) {
					String parCode = classCode.substring(0, classCode.length() - 5);
					sql = "select count(*) from " + tableInfo.getTableName() + " where classCode like '" + parCode + "_____%' and classCode !='" + classCode + "'";
					pstmt = conn.prepareStatement(sql);
					rss = pstmt.executeQuery();
					int childCount = 0;
					if (rss.next()) {
						childCount = rss.getInt(1);
					}

					if (childCount == 0) {
						sql = "update " + tableName + " set isCatalog=0 where classCode=?";
						pstmt = conn.prepareStatement(sql);
						pstmt.setObject(1, parCode);
						pstmt.execute();
					}
				}

			} catch (Exception ex) {
				rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
				rs.setRetVal(ex.getMessage());
				BaseEnv.log.error("update parent isCatalog Error:", ex);
				return rs;
			}

		}
		return rs;
	}

	//保存<update saveField="">中要保存的字段值
	HashMap savedFields = new HashMap();

	/**
	 * 自定义sql的执行
	 * @param conn Connection
	 * @param type String
	 * @param tableName String
	 * @param allTables Hashtable
	 * @param values HashMap
	 * @param id int
	 * @return int
	 */
	public Result defineSql(Connection conn, String type, boolean isAfter, final String tableName, final Hashtable allTables, final HashMap values, String id, String userId, String defineInfo,
			MessageResources resources, Locale locale) {
		Hashtable table = ((Hashtable) BaseEnv.sessionSet.get(userId));
		table.put("BillOper", type);
		values.put("tableName", tableName);
		Result rs = new Result();
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, tableName);
		String fieldCalculate = tableInfo.getFieldCalculate();
		if (fieldCalculate == null || fieldCalculate.trim().length() == 0) {
			//没有定义sql语句
			return rs;
		}

		/**
		 * 表调用自定义语句的格式如下：
		 * <sql operation="add" post="before" id="testid" param1="tbltestsql.one" param2="tbltestsql.two">
		 * post表示执行位置有 before、after,默认值为after
		 * 分析每一条sql分别执行
		 */
		while (fieldCalculate.indexOf("<sql") > -1) {
			//分析出语句?
			String defineSql = fieldCalculate.substring(fieldCalculate.indexOf("<sql"), fieldCalculate.indexOf(">"));
			fieldCalculate = fieldCalculate.substring(fieldCalculate.indexOf(">") + 1);

			//分析sql类型是否type一致
			String operation = getAttribute("operation", defineSql);
			String post = getAttribute("post", defineSql);

			//表信息中<update saveField="" selfKey="" matchKey="">等三个属性
			String saveField = getAttribute("saveField", defineSql);
			String selfKey = getAttribute("selfKey", defineSql);
			String matchKey = getAttribute("matchKey", defineSql);

			boolean postIsAfter = "before".equalsIgnoreCase(post) ? false : true;
			//类型一致,且位置一致。则执行自定义语句
			if (type.equals(operation.trim()) && (postIsAfter == isAfter || type.equals("addView")||type.equals("updateView") )) {
				//如果表信息中<update saveField="" selfKey="" matchKey="">等三个属性不为空，保存saveField
				if (!postIsAfter && saveField != null && saveField.length() > 0 && selfKey != null && selfKey.length() > 0 && matchKey != null && matchKey.length() > 0) {

					String billDate = values.get("BillDate") == null ? "" : values.get("BillDate").toString();
					String oldBillDate = "";
					Object fieldValue = values.get(selfKey);

					String sql = "select BillDate," + saveField.replace("_", ".") + " from " + saveField.substring(0, saveField.indexOf("_")) + " w" + "" + "here " + matchKey.replace("_", ".") + "='"
							+ fieldValue + "'";

					Result myRs = new Result();
					try {
						PreparedStatement ps = conn.prepareStatement(sql);
						ResultSet resultSet = ps.executeQuery();
						if (resultSet.next()) {
							oldBillDate = resultSet.getObject(1).toString();
							myRs.setRetVal(resultSet.getObject(2));
						} else { //如果是手工凭证，（没有关联单据号）单据日期月份不同，不保存数据                        	
							fieldValue = values.get("id");
							sql = "select BillDate," + saveField.replace("_", ".") + " from " + saveField.substring(0, saveField.indexOf("_")) + " where id='" + fieldValue + "'";
							ps = conn.prepareStatement(sql);
							resultSet = ps.executeQuery();
							if (resultSet.next()) {
								oldBillDate = resultSet.getObject(1).toString();
								myRs.setRetVal(resultSet.getObject(2));
							}
						}
						Date time = BaseDateFormat.parse(billDate, BaseDateFormat.yyyyMMdd);
						Date oldTime = BaseDateFormat.parse(oldBillDate, BaseDateFormat.yyyyMMdd);
						if ((!saveField.equals("tblAccMain_OrderNo")) || (saveField.equals("tblAccMain_OrderNo") && (oldTime != null && time.getMonth() == oldTime.getMonth()))) {
							savedFields.put(saveField, myRs.getRetVal());
						} else {
							savedFields.put(saveField, "");
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						myRs.setRetCode(ErrorCanst.SQLEXCEPTION_ERROR);
					}

				}

				//分析id
				String sqlName = getAttribute("id", defineSql);
				String formerDefine = sqlName;
				if (defineInfo != null && defineInfo.length() > 0) {
					String[] defInfo = defineInfo.split(";");
					//如果有多个确定取消，取最后一个
					for (int i = defInfo.length - 1; i >= 0; i--) {
						String str = defInfo[i];
						if (str.length() > 0) {
							String[] def = str.split(":");
							if (sqlName.equals(def[0])) {
								sqlName = def[1];
								break;
							}
						}
					}
				}
				DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(sqlName);
				if (defineSqlBean == null) {
					BaseEnv.log.error("Define Sql Not Exist :Name = " + sqlName);
					rs.setRetVal("define:"+sqlName+"不存在");
					rs.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
					return rs;
				}

				Result ret = defineSqlBean.execute(conn, values, userId, resources, locale, defineInfo);
				if (ret.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
					if (ret.getRetCode() == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
						ConfirmBean bean = (ConfirmBean) ret.getRetVal();
						bean.setFormerDefine(formerDefine);
					}
					return ret;
				}
			}
		}
		return rs;
	}

	/**
	 * 获取属性
	 * @param attr String
	 * @return String
	 */
	public String getAttribute(String attr, String str) {
		//<sql operation="add" post="before" id="testid" param1="tbltestsql.one" param2="tbltestsql.two">
		Pattern pattern = Pattern.compile(attr + "=\"([\\w\\.]+)\"");
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}

	/**
	 * 自定义sql的删除
	 * @param conn Connection
	 * @param type String
	 * @param tableName String
	 * @param allTables Hashtable
	 * @param id String[]
	 * @return int
	 */
	private Result defineSqlDel(Connection conn, String type, boolean isAfter, final String tableName,final HashMap values, final Hashtable allTables, String id, String userId, final MessageResources resources,
			final Locale locale) {
		Hashtable table = ((Hashtable) BaseEnv.sessionSet.get(userId));
		table.put("BillOper", type);

		Result rs = new Result();
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, tableName);
		String fieldCalculate = tableInfo.getFieldCalculate();
		if (fieldCalculate == null || fieldCalculate.trim().length() == 0) {
			//没有定义sql语句
			return rs;
		}

		/**
		 * 表调用自定义语句的格式如下：
		 * <sql operation="add" post="before" id="testid" param1="tbltestsql.one" param2="tbltestsql.two">
		 * post表示执行位置有 before、after,默认值为after
		 * 分析每一条sql分别执行
		 */
		while (fieldCalculate.indexOf("<sql") > -1) {
			//分析出语句
			String defineSql = fieldCalculate.substring(fieldCalculate.indexOf("<sql"), fieldCalculate.indexOf(">"));
			fieldCalculate = fieldCalculate.substring(fieldCalculate.indexOf(">") + 1);

			//分析sql类型是否type一致
			String operation = getAttribute("operation", defineSql);
			String post = getAttribute("post", defineSql);
			boolean postIsAfter = "before".equalsIgnoreCase(post) ? false : true;
			if (type.equals(operation.trim()) && postIsAfter == isAfter) {
				//类型一致,且位置一致。则执行自定义语句
				//寻找对应的sql
				//分析id
				String sqlName = getAttribute("id", defineSql);
				;
				DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(sqlName);
				if (defineSqlBean == null) {
					BaseEnv.log.error("Define Sql Not Exist :Name = " + sqlName);
					rs.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
					return rs;
				}
				//取参数
				values.put("tableName", tableName);
				Result ret = defineSqlBean.execute(conn, values, userId, resources, locale, "");
				if (ret.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && ret.getRetCode() != ErrorCanst.RET_DEFINE_SQL_ALERT) {
					return ret;
				}
				if (ret.getRetCode() == ErrorCanst.RET_DEFINE_SQL_ALERT) {
					rs = ret;
				}
			}
		}
		return rs;
	}

	public Result defineDelSql(final String defineN, final String[] id, final String userId, final MessageResources resources, final Locale locale, final String defineInfo) {
		final Result rs = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						String defineName = defineN;
						if (defineInfo != null && defineInfo.length() > 0) {
							String[] defInfo = defineInfo.split(";");
							//如果有多个确定取消，取最后一个
							for (int i = defInfo.length - 1; i >= 0; i--) {
								String str = defInfo[i];
								if (str.length() > 0) {
									String[] def = str.split(":");
									if (defineName.equals(def[0])) {
										defineName = def[1];
										break;
									}
								}
							}
						}

						DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(defineName);
						if (defineSqlBean == null) {
							BaseEnv.log.error("Define Sql Not Exist :Name = " + defineName);
							rs.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
							return;
						}
						if (id == null) {
							HashMap paramList = new HashMap();
							Result ret = defineSqlBean.execute(conn, paramList, userId, resources, locale, defineInfo);
							if (ret.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
								if (ret.getRetCode() == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
									ConfirmBean bean = (ConfirmBean) ret.getRetVal();
									bean.setFormerDefine(defineName);
								}
								rs.retCode = ret.getRetCode();
								rs.retVal = ret.getRetVal();
								return;
							}
						} else {
							//取参数
							for (int i = 0; i < id.length; i++) {
								HashMap paramList = new HashMap();
								String strID = "";
								if (id[i].indexOf(";") != -1) {
									strID = id[i].substring(0, id[i].indexOf(";"));
								} else {
									strID = id[i];
								}
								paramList.put("id", strID);
								Result ret = defineSqlBean.execute(conn, paramList, userId, resources, locale, defineInfo);
								if (ret.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && ret.getRetCode() != ErrorCanst.RET_DEFINE_SQL_ALERT) {
									if (ret.getRetCode() == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
										ConfirmBean bean = (ConfirmBean) ret.getRetVal();
										bean.setFormerDefine(defineName);
									}
									rs.retCode = ret.getRetCode();
									rs.retVal = ret.getRetVal();
									return;
								}
								rs.retCode = ret.getRetCode();
								rs.retVal = ret.getRetVal();
							}
						}

					}
				});
				return rs.getRetCode();
			}
		});
		return rs;

	}

	public Result defineSql(final String defineName, final HashMap paramList, final String userId, final MessageResources resources, final Locale locale) {
		final Result rs = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(defineName);
						if (defineSqlBean == null) {
							BaseEnv.log.error("Define Sql Not Exist :Name = " + defineName);
							rs.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
							return;
						}
						//取参数
						Result ret = defineSqlBean.execute(conn, paramList, userId, resources, locale, "");
						if (ret.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
							rs.retCode = ret.getRetCode();
							rs.retVal = ret.getRetVal();
							return; 
						}
					}
				});
				return rs.getRetCode();
			}
		});
		return rs;

	}

	/**
	 * 执行数据搬移define操作
	 * @param definedName 
	 * @param keyIds
	 * @param tableName
	 * @param classCode
	 * @return
	 */
	public Result defineDataMoveSql(final String defineName, final String[] keyIds, final String tableName, final String classCode, final String userId, final MessageResources resources,
			final Locale locale) {
		final Result result = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(defineName);
						if (defineSqlBean == null) {
							BaseEnv.log.error("defineDataMoveSql Sql Not Exist :Name = " + defineName);
							result.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
							return;
						}
						//取参数
						for (int i = 0; i < keyIds.length; i++) {
							HashMap paramList = new HashMap();
							paramList.put("tableName", tableName);
							paramList.put("toClassCode", classCode);
							String strID = "";
							if (keyIds[i].indexOf(";") != -1) {
								strID = keyIds[i].substring(0, keyIds[i].indexOf(";"));
							} else {
								strID = keyIds[i];
							}
							paramList.put("id", strID);
							Result ret = defineSqlBean.execute(conn, paramList, userId, resources, locale, "");
							if (ret.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
								result.retCode = ret.getRetCode();
								result.retVal = ret.getRetVal();
								return;
							}
						}
					}
				});
				return result.getRetCode();
			}
		});
		return result;
	}
	
	/**
	 * 先执行弹出窗，再根据弹出窗的返回值，执行define
	 * @param defineName
	 * @param keyIds
	 * @param tableName
	 * @param classCode
	 * @param userId
	 * @param resources
	 * @param locale
	 * @return
	 */
	public Result popDefineSql(final String defineName, final String[] keyIds, final String tableName, final String popReturnVal, final String userId, final MessageResources resources,
			final Locale locale) {
		final Result result = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(defineName);
						if (defineSqlBean == null) {
							BaseEnv.log.error("defineDataMoveSql Sql Not Exist :Name = " + defineName);
							result.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
							return;
						}
						//取参数
						for (int i = 0; i < keyIds.length; i++) {
							HashMap paramList = new HashMap();
							paramList.put("tableName", tableName);
							paramList.put("popReturnVal", popReturnVal);
							String strID = "";
							if (keyIds[i].indexOf(";") != -1) {
								strID = keyIds[i].substring(0, keyIds[i].indexOf(";"));
							} else {
								strID = keyIds[i];
							}
							paramList.put("id", strID);
							Result ret = defineSqlBean.execute(conn, paramList, userId, resources, locale, "");
							if (ret.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
								result.retCode = ret.getRetCode();
								result.retVal = ret.getRetVal();
								return;
							}
						}
					}
				});
				return result.getRetCode();
			}
		});
		return result;
	}

	/**
	 * 检查关键字
	 * @param definedName 
	 * @param keyIds
	 * @param tableName
	 * @param classCode
	 * @return
	 */
	public Result checkKeyword(final String defineName, final String tableName, final String keyword, final String userId, final MessageResources resources, final Locale locale) {
		final Result result = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(defineName);
						if (defineSqlBean == null) {
							BaseEnv.log.error("defineDataMoveSql Sql Not Exist :Name = " + defineName);
							result.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
							return;
						}
						//取参数
						HashMap paramList = new HashMap();
						paramList.put("tableName", tableName);
						paramList.put("keyword", keyword);
						Result ret = defineSqlBean.execute(conn, paramList, userId, resources, locale, "");
						if (ret.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
							result.retCode = ret.getRetCode();
							result.retVal = ret.getRetVal();
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		return result;
	}

	/**
	 * <!-- 拓展按钮：盘点处理 --> 
	 * @param definedName 
	 * @param keyIds
	 * @param tableName
	 * @param classCode
	 * @return
	 */
	public Result checkReady(final String defineName, final String keyIds, final String userId, final MessageResources resources, final Locale locale) {
		final Result result = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(defineName);
						if (defineSqlBean == null) {
							BaseEnv.log.error("checkReady Sql Not Exist :Name = " + defineName);
							result.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
							return;
						}
						//取参数
						HashMap paramList = new HashMap();
						paramList.put("tblCheckReady_id", keyIds);
						Result ret = defineSqlBean.execute(conn, paramList, userId, resources, locale, "");
						if (ret.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
							result.retCode = ret.getRetCode();
							result.retVal = ret.getRetVal();
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		return result;
	}

	/**
	 * 销售出库数量弹出窗口 数量裁剪 生成布匹裁剪单
	 * @param definedName 
	 * @param keyIds
	 * @param tableName
	 * @param classCode
	 * @return
	 */
	public Result qtySplit(final String seq, final String billDate, final String qty, final String qty2, final String stockCode, final String userId, final MessageResources resources,
			final Locale locale) {
		final Result result = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("SalesOutstock_tblGoodsSplitForm_Add");
						if (defineSqlBean == null) {
							BaseEnv.log.error("defineQtySplit Sql Not Exist :Name = SalesOutstock_tblGoodsSplitForm_Add");
							result.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
							return;
						}
						//取参数
						HashMap paramList = new HashMap();
						paramList.put("Seq", seq);
						paramList.put("BillDate", billDate);
						paramList.put("StockCode", stockCode);
						paramList.put("Qty", qty);
						paramList.put("Qty2", qty2);
						Result ret = defineSqlBean.execute(conn, paramList, userId, resources, locale, "");
						if (ret.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
							String sql = "select Seq from tblStockDet where BillID=(select BillID from tblStockdet "
									+ " where Seq=? and StockCode=? and itemno=(select Max(itemno) from tblstockdet where Seq= ? " + " and StockCode=?)) and InstoreQty=" + qty2;
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, seq);
							pss.setString(2, stockCode);
							pss.setString(3, seq);
							pss.setString(4, stockCode);
							ResultSet rss = pss.executeQuery();
							String returnSeq = "";
							if (rss.next()) {
								returnSeq = rss.getString("Seq");
							}
							result.retCode = ret.getRetCode();
							result.retVal = returnSeq;
							return;
						} else {
							result.retCode = ret.retCode;
							result.retVal = ret.retVal;
						}
					}
				});
				return result.getRetCode();
			}
		});
		return result;
	}

	public Result defineTaskAllotSql(final String defineName, final String[] keyIds, final String classCode, final String userId, final MessageResources resources, final Locale locale) {
		final Result result = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(defineName);
						if (defineSqlBean == null) {
							BaseEnv.log.error("defineDataMoveSql Sql Not Exist :Name = " + defineName);
							result.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
							return;
						}
						// 取参数
						for (int i = 0; i < keyIds.length; i++) {
							HashMap paramList = new HashMap();
							paramList.put("toClassCode", classCode);
							String strID = "";
							if (keyIds[i].indexOf(";") != -1) {
								strID = keyIds[i].substring(0, keyIds[i].indexOf(";"));
							} else {
								strID = keyIds[i];
							}
							paramList.put("id", strID);
							Result ret = defineSqlBean.execute(conn, paramList, userId, resources, locale, "");
							if (ret.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
								result.retCode = ret.getRetCode();
								result.retVal = ret.getRetVal();
								return;
							}
						}
					}
				});
				return result.getRetCode();
			}
		});
		return result;
	}

	/**
	 * 修改数据
	 * @param tableName String
	 * @param allTables Map
	 * @param values HashMap
	 * @return Result
	 */
	public Result update(final String tableName, final Hashtable<String,DBTableInfoBean> allTables, final HashMap str_values, final String userId, 
			final String defineInfo, final MessageResources resources, final Locale locale,final String saveType, 
			final LoginBean loginBean, final OAWorkFlowTemplate workFlow, final Hashtable props) {
		final Result rs = new Result();
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, tableName);
		
		str_values.put("lastUpdateBy", loginBean.getId());
		
		String sysParamter = tableInfo.getSysParameter();
        Date time = null; //记录单据日期
        //加入修改s人信息
        for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) { 
            DBFieldInfoBean fi = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
            if (fi.getFieldName().equals("lastUpdateBy")) {
            	str_values.put("lastUpdateBy", loginBean.getId());
            } else if (fi.getFieldName().equals("lastUpdateTime")) {
            	str_values.put("lastUpdateTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
            }
            String o = fi.getDefaultValue();
            String type = fi.getFieldIdentityStr();
            if (null != type) {
                if (null != o && type.equals("accendnotstart")) {
                    Object oo = str_values.get(fi.getFieldName());
                    if (null != oo) {
                        try {
							time = BaseDateFormat.parse(oo.toString(),
							        BaseDateFormat.yyyyMMdd);
						}catch (Exception e) {
							rs.retCode= ErrorCanst.DEFAULT_FAILURE;
		            		rs.retVal="字段"+fi.getDisplay().get(locale.toString())+"值"+oo+"不正确";
		            		return rs;
						}
                    }
                }
            }
        }
        //如果已经月结，不允许修改
        int currentMonth = 0;
        int currentYear=0;
        if (null != time) {
            currentMonth = time.getMonth() + 1;
            currentYear=time.getYear()+1900;
        }
        int periodMonth = -1;
        int periodYear=-1;
        AccPeriodBean accBean=BaseEnv.accPerios.get(loginBean.getSunCmpClassCode());
        if (accBean!=null) {
            periodMonth = accBean.getAccMonth();
            periodYear=accBean.getAccYear();
        }
        if ((currentYear<periodYear||(currentYear==periodYear && currentMonth<periodMonth) ||periodMonth < 0 ) && currentMonth != 0) {
            if (null != sysParamter) {
                if (sysParamter.equals("CurrentAccBefBill") &&
                		(currentYear<periodYear||(currentYear==periodYear && currentMonth<periodMonth))) {
                	rs.retCode= ErrorCanst.DEFAULT_FAILURE;
            		rs.retVal=resources.getMessage(locale,"com.currentaccbefbill.update");
            		return rs;
                }

                if (sysParamter.equals("CurrentAccBefBillAndUnUseBeforeStart") &&
                		(currentYear<periodYear||(currentYear==periodYear && currentMonth<periodMonth) ||periodMonth < 0 )) {
                	rs.retCode= ErrorCanst.DEFAULT_FAILURE;
            		rs.retVal=resources.getMessage(locale,"com.currentaccbefbill.update");
            		return rs;
                }
            }
        }
        //检查单据日期的期间在会计期间中是否存在
        if (null != time) {
	        int billYear=time.getYear()+1900;
	        int billMonth=time.getMonth() + 1;
	        Result rspriod = periodIsExist(billYear,billMonth);
	        if (rspriod.retCode == ErrorCanst.DEFAULT_SUCCESS) {
	        	if(Integer.parseInt(rspriod.getRetVal().toString())==0){
	        		rspriod = addNextPriodArr(getNewAccPeriodBean(billYear, billMonth, loginBean.getId(),loginBean.getSunCmpClassCode(),periodYear,periodMonth));
	        		if(rspriod.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
	        			 //添加失败
	        			rs.retCode= rspriod.retCode;
	            		rs.retVal=rspriod.retVal;
	            		return rs;
	        		}
	        	}
	        }else{
	        	 //添加失败
	        	rs.retCode= rspriod.retCode;
        		rs.retVal=rspriod.retVal;
        		return rs;
	        }
        }
        //将日期所在的期间设置到对应的期间表中
        // 假如表结构中系统参数是“开账前及会计期间前均不能录入”,并且期间是不输入的，那么根据日期查询期间，并设置到values中
        DBFieldInfoBean periodField=DDLOperation.getFieldInfo(BaseEnv.tableInfos, tableName, "Period");
        if("CurrentAccBefBillAndUnUseBeforeStart".equals(sysParamter)&&currentYear>0&&currentMonth>0&&
        		periodField!=null&&periodField.getInputType()==DBFieldInfoBean.INPUT_NO){
        	String sql="select AccYear,AccMonth,AccPeriod from tblAccPeriod where AccMonth ="+currentMonth+" and AccYear="+currentYear
        			+" and SCompanyID='"+loginBean.getSunCmpClassCode()+"'";
        	Result rsperiod=new AIODBManager().sqlListMap(sql, null, 0, 0);
        	if(rsperiod!=null&&((ArrayList)rsperiod.retVal).size()>0){
        		HashMap periodMap=(HashMap)((ArrayList)rsperiod.retVal).get(0);
        		str_values.put("Period", periodMap.get("AccPeriod").toString());
        		str_values.put("PeriodYear", periodMap.get("AccYear").toString());
        		str_values.put("PeriodMonth", periodMap.get("AccMonth").toString());
        	}
        }
		
		if("saveDraft".equals(saveType)){
        	if(!"draft".equals(str_values.get("workFlowNodeName"))){//如果此时草稿已经过账,不允许再存为草稿
        		rs.retCode= ErrorCanst.DEFAULT_FAILURE;
        		rs.retVal=resources.getMessage(locale,"common.msg.draftHasPass");
        		return rs;
        	}
        	str_values.put("workFlowNodeName", "draft") ;
        }else{
        	Result rsAcc=hasCreateAcc(tableName, str_values.get("id").toString());
        	if(rsAcc.retCode==ErrorCanst.DEFAULT_SUCCESS){
        		if(rsAcc.retVal!=null){//如果已经生成凭证，则给出错误提示
        			String[] str=(String[])rsAcc.retVal;
        			rs.retCode= ErrorCanst.DEFAULT_FAILURE;
            		rs.retVal=resources.getMessage(locale,"common.hasCreateAcc.Oper.error",str[0],str[1]);
            		return rs;
        		}
        	}else{
        		rs.retCode= rsAcc.retCode;
        		rs.retVal=rsAcc.retVal;
        		return rs;
        	}
        }
		
		//这里需要对内容进行再次数据正确性校验，因为数据来源不只是界面，还可能是导入，零售，所以需要在这重新时行数据正确性校验。
		Result vrs = Validator.validator("saveDraft".equals(saveType) ? true : false, tableName, allTables, str_values, resources, locale);
		if (vrs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return vrs;
		}
		final Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(userId) ;
		
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						
						//验证数据是否存在，如果是草稿，要验证是否过帐
						String bsql  = "select workflowNodeName from "+tableName +" where id= ?";
						PreparedStatement pstmt =conn.prepareStatement(bsql);
						pstmt.setString(1, String.valueOf(str_values.get("id")));
						BaseEnv.log.debug("DynDBManager.update 检查原单据状态 sql ="+bsql + " id= "+String.valueOf(str_values.get("id")));
						ResultSet rset =pstmt.executeQuery();
						if(rset.next()){
							if("saveDraft".equals(saveType) && !"draft".equals(rset.getString("workflowNodeName"))){
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								rs.setRetVal("草稿已经过帐，不可再保存为草稿");
								BaseEnv.log.error("DynDBManager update Error code = " + rs.getRetCode() + " Msg=" + rs.getRetCode());
								return;
							}
						}else{
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							rs.setRetVal("数据不存在或已删除");
							BaseEnv.log.error("DynDBManager update Error code = " + rs.getRetCode() + " Msg=" + rs.getRetCode());
							return;
						}
						
						HashMap values = str_values;
						Obj beforeObject = new Obj();
						//转换特殊字符
						DBTableInfoBean tableInfo = DDLOperation.getTableInfo((Hashtable) allTables, tableName);
						Result sp_values = GlobalsTool.conversionSpecialCharacter(tableInfo.getFieldInfos(), values);
						values = (HashMap) sp_values.getRetVal();
						Result defineRs = null;
						//执行表自定义语句
						if (!"saveDraft".equals(saveType)) {
							if("draft".equals(values.get("workFlowNodeName"))){
								values.put("workFlowNodeName","notApprove");
							}
							defineRs = defineSql(conn, "update", false, tableName, (Hashtable) allTables, values, values.get("id").toString(), userId, defineInfo, resources, locale);
							if (defineRs.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
								rs.setRetCode(defineRs.getRetCode());
								rs.setRetVal(defineRs.getRetVal());
								BaseEnv.log.error("DynDBManager before update defineSql Error code = " + defineRs.getRetCode() + " Msg=" + defineRs.getRetCode());
								return;
							}
						}
						//执行修改前的接口
						BaseCustomFunction impl = (BaseCustomFunction) BaseEnv.functionInterface.get(tableName);
						if (impl != null) {
							Result interfaceRs = impl.onBeforUpdate(conn, tableName, allTables, values, beforeObject);
							if (interfaceRs.getRetCode() < 0) {
								rs.setRetCode(interfaceRs.getRetCode());
								BaseEnv.log.error("DynDBManager onBeforeUpdate Error code = " + interfaceRs.getRetCode() + " Msg=" + interfaceRs.getRetCode());
								return;
							}
						}
						
						//执行主表修改
						Result ires = execUpdate(conn, tableName, allTables, values,sessionSet);
						if (ires.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
							BaseEnv.log.debug("Update Table " + tableName + " Info: " + ires.getRetVal());
							rs.setRetCode(ires.getRetCode());
							rs.setRetVal(ires.getRetVal());
							return;
						}
						ArrayList childTableList = DDLOperation.getChildTables(tableName, allTables);
						for (int i = 0; i < childTableList.size(); i++) {
							DBTableInfoBean childTb = (DBTableInfoBean) childTableList.get(i);
							ArrayList childList = (ArrayList) values.get("TABLENAME_" + childTb.getTableName());
							if (childList == null) {
								continue;
							}

							String insertTableName = getInsertTableName(childTb.getTableName());//处理CRM多模板插入数据库的表名
							//删除多语言
							KRLanguageQuery.delete(conn, childTb, values.get("id").toString(), "f_ref");
							//先删除从表
							ires = execDelete(conn, insertTableName, values.get("id").toString(), childList);
							if (ires.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
								BaseEnv.log.debug("Delete child Table " + insertTableName + " Info: " + ires.getRetVal());
								rs.setRetCode(ires.getRetCode());
								rs.setRetVal(ires.getRetVal());
								return;
							}

							for (int j = 0; j < childList.size(); j++) {
								HashMap childMap = (HashMap) childList.get(j);
								childMap.put("id", IDGenerater.getId());
								childMap.put("f_ref", values.get("id"));
								//转换特殊字符
								//sp_values =  conversionSpecialCharacter(childTb.getFieldInfos(),childMap) ;
								//childMap = (HashMap) sp_values.getRetVal() ;
								//执行从表插入
								ires = execInert(conn, insertTableName, allTables, childMap, "", resources, locale);
								if (ires.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
									BaseEnv.log.debug("Insert child Table " + insertTableName + " Info: " + ires.getRetVal());
									rs.setRetCode(ires.getRetCode());
									rs.setRetVal(ires.getRetVal());
									return;
								}
								childsIns.add(addSql);
								addSql = "";
							}
						}

						if (!"saveDraft".equals(saveType)) {
							//执行表自定义语句
							defineRs = defineSql(conn, "update", true, tableName, (Hashtable) allTables, values, values.get("id").toString(), userId, defineInfo, resources, locale);
							if (defineRs.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
								rs.setRetCode(defineRs.getRetCode());
								rs.setRetVal(defineRs.getRetVal());
								BaseEnv.log.error("DynDBManager after update defineSql Error code = " + defineRs.getRetCode() + " Msg=" + defineRs.getRetVal());
								return;
							}
							if (defineRs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
								rs.setRetCode(defineRs.retCode);
								rs.setRetVal(defineRs.retVal);
							}
							//执行修改后的接口
							if (impl != null) {
								Result interfaceRs = impl.onAfterUpdate(conn, tableName, allTables, values, beforeObject);
								if (interfaceRs.getRetCode() < 0) {
									rs.setRetCode(interfaceRs.getRetCode());
									BaseEnv.log.error("DynDBManager onAfterUpdate Error code = " + interfaceRs.getRetCode() + " Msg=" + interfaceRs.getRetVal());
									return;
								}
							}

							//update之后，将saveField的值恢复
							String fieldCalculate = tableInfo.getFieldCalculate();
							while (fieldCalculate.indexOf("<sql") > -1) {
								String defineSql = fieldCalculate.substring(fieldCalculate.indexOf("<sql"), fieldCalculate.indexOf(">"));
								fieldCalculate = fieldCalculate.substring(fieldCalculate.indexOf(">") + 1);
								String post = getAttribute("post", defineSql);
								String saveField = getAttribute("saveField", defineSql);
								String selfKey = getAttribute("selfKey", defineSql);
								String matchKey = getAttribute("matchKey", defineSql);

								boolean postIsAfter = "before".equalsIgnoreCase(post) ? false : true;
								if (postIsAfter) {
									//位置是after。则执行保恢复saveField操作
									if (saveField != null && saveField.length() > 0 && selfKey != null && selfKey.length() > 0 && matchKey != null && matchKey.length() > 0) {
										Object fieldValue = values.get(selfKey);

										for (int i = 0; i < savedFields.size(); i++) {
											if (savedFields.get(savedFields.keySet().toArray()[i]) != null && savedFields.get(savedFields.keySet().toArray()[i]).toString().length() > 0) {
												String sql = "";
												if (fieldValue == null) { //如果是手工凭证
													fieldValue = values.get("id");
													sql = "update " + saveField.substring(0, saveField.indexOf("_")) + " set " + saveField.replace("_", ".") + "="
															+ savedFields.get(savedFields.keySet().toArray()[i]) + " where id='" + fieldValue + "'";
												} else {
													sql = "update " + saveField.substring(0, saveField.indexOf("_")) + " set " + saveField.replace("_", ".") + "="
															+ savedFields.get(savedFields.keySet().toArray()[i]) + " where " + matchKey.replace("_", ".") + "='" + fieldValue + "'";
												}
												Result myRs = new Result();
												try {
													PreparedStatement ps = conn.prepareStatement(sql);
													int result = ps.executeUpdate();
													myRs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
												} catch (Exception ex) {
													ex.printStackTrace();
													myRs.setRetCode(ErrorCanst.SQLEXCEPTION_ERROR);
												}
											}
										}
									}
								}

							}
						}
						if (workFlow != null && 1 == workFlow.getTemplateStatus()) {
							try {
								Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
								boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
								Result rsValue = detail(tableName, (Hashtable) allTables, (String) values.get("id"), loginBean.getSunCmpClassCode(), props, userId, isLastSunCompany,
										"", conn);
								HashMap hm = (HashMap) rsValue.retVal;
								Result flowRs = new OAMyWorkFlowMgt().update(tableName, hm, loginBean, locale, conn,resources);
								rs.setRetCode(flowRs.retCode);
								rs.setRetVal(flowRs.retVal);
							} catch (Exception e) {
								BaseEnv.log.error("DynDBManager.update Error",e);
								rs.setRetCode(ErrorCanst.BILL_UPDATE_WORK_FLOW_ERROR);
							}
						}
						
						/*检查主表及主表对应的明细表中是否存在系统类型为追踪单号的字段，如果存在删除追踪单号表（tblTrackBill）中数据*/
						deleteTrackBill(conn, tableName,  values.get("id").toString());
						/*假如启用了追踪单号，检查主表及主表对应的明细表中是否存在系统类型为追踪单号的字段，如果存在向追踪单号表（tblTrackBill）中插入数据*/
						if ("true".equals(BaseEnv.systemSet.get("TrackNo").getSetting())) {
							insertTrackBill(conn, tableName, values);
						}
					}
				});
				return rs.getRetCode();
			}
		});
		if (retVal == ErrorCanst.DEFAULT_SUCCESS) {
        	//如果是单据要调用重算成本
        	if(tableInfo.getSysParameter().equals("CurrentAccBefBillAndUnUseBeforeStart")&&BaseEnv.systemSet.get("SaveBillAutoRecalc")!=null&&BaseEnv.systemSet.get("SaveBillAutoRecalc").getSetting().equals("true")){
        		ReCalcucateMgt sysMgt=new ReCalcucateMgt();
        		sysMgt.reCalcucateData(loginBean.getSunCmpClassCode(),accBean.getAccYear(),  accBean.getAccMonth(), loginBean.getId(),"reCalcucate","","");
        	}
		}
		rs.setRetCode(retVal);
		return rs;
	}

	/**
	 * 删除数据
	 * @param tableName String
	 * @param allTables Hashtable
	 * @param id String[]
	 * @return Result
	 */
	public Result delete(final String tableName, final Hashtable allTables, final String ids[], final String userId,  final MessageResources resources,
			final Locale locale, final boolean isDraft) {
		final Result rs = new Result();
		final String[] files = new String[] { "", "" }; //0保存图片文件， 1保存附件文件
		final ArrayList billNos = new ArrayList();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {		
						for(String id:ids){
							Result delResult = delete(tableName, allTables, id, userId,  resources, locale,  files, billNos, conn,true);
							rs.setRetCode(delResult.getRetCode());
							rs.setRetVal(delResult.getRetVal());
						}
					}
				});

				return rs.getRetCode();
			}
		});
		rs.setRetCode(retVal);
		
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS || rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
			Object[] obj = new Object[2];
			obj[0] = rs.getRetVal();
			obj[1] = billNos;
			rs.setRetVal(obj);
			//删除相关的消息
			deleteByRelationId(ids);
		}
		return rs;
	}
	
	/**
	 * 根据详情ID删除关联消息
	 * @param relationIds
	 * @return
	 */
	public boolean deleteByRelationId(String relationIds[]) {
		if (null == relationIds ) {
			return false;
		}
		String []ids = relationIds;
		String str = "";
		for (String id : ids) {
			str += "'"+id+"',";
		}
		str = str.substring(0, str.length() - 1);
		String sql = "select bean from AdviceBean bean where bean.relationId in (" + str + ") ";
		ArrayList param = new ArrayList();
		Result rs = this.list(sql, param);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<AdviceBean> listAdvice = (List<AdviceBean>) rs.getRetVal();
			boolean result = listAdvice.size() == 0;	// 本来就没有，不用删了，直接返回true
			for (AdviceBean bean : listAdvice) {
				result |= delete(bean);	// 只要有一条成功，就当作成功了
			}
			return result;
		} else {
			return false;
		}
	}
	protected boolean delete(AdviceBean bean) {
		Result rs = deleteBean(bean.getId(), AdviceBean.class, "id");
		if (ErrorCanst.DEFAULT_SUCCESS == rs.getRetCode()) {
			// 通知KK
			MSGConnectCenter.cancelMsg(CancelMsgReq.TYPE_SYS, bean.getReceive(), bean.getId());
			return true;
		} else {
			return false;
		}
	}


	/**
	 * 标准财务启用的情况下，判断当前单据是否已经手工创建凭证
	 * @param tableName
	 * @param id
	 * @return
	 */
	public Result hasCreateAcc(final String tableName, final String id) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						//未启用自动产生凭证，判断当前单据是否已经手工生成凭证
						if ((BaseEnv.version == 12 && "false".equals(BaseEnv.systemSet.get("autoGenerateAcc").getSetting()))
								|| (BaseEnv.version != 12 && "true".equals(BaseEnv.systemSet.get("standardAcc").getSetting()))) {
							List<DBFieldInfoBean> list = ((DBTableInfoBean) BaseEnv.tableInfos.get(tableName)).getFieldInfos();
							boolean existsField = false;
							String rowMarker = "";
							for (int i = 0; i < list.size(); i++) {
								if ("CertificateNo".equals(list.get(i).getFieldName())) {
									existsField = true;
								}
								if ("RowMarker".equals(list.get(i).getFieldSysType())) {
									rowMarker = list.get(i).getFieldName();
								}
							}

							if (existsField) {
								String credNo = "";
								String billNo = "";
								rowMarker = rowMarker.length() > 0 ? "a." + rowMarker : "''";
								String sql = "select " + rowMarker + " as BillNo,CredTypeID+'_'+cast(OrderNo as varchar(10)) as credNo from " + tableName + " a,tblAccMain b where a.id='" + id
										+ "' and a.CertificateNo=b.id ";
								Statement st = conn.createStatement();
								ResultSet rst = st.executeQuery(sql);
								if (rst.next()) {
									billNo = rst.getString(1);
									credNo = rst.getString(2);
								}
								if (credNo.length() > 0) {
									rs.setRetVal(new String[] { billNo, credNo });
								}
							}
						}
					}
				});

				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 删除数据
	 * @param tableName String
	 * @param allTables Hashtable
	 * @param id String[]
	 * @param delBillNo 是否删除单据编号
	 * @return Result
	 */
	public Result delete(String tableName, Hashtable allTables, String id, String userId, MessageResources resources, Locale locale, 
			String[] files, ArrayList billNos, Connection conn,boolean delBillNo) {
		Result rs = new Result();
		rs.setRetVal(files);
		String execsql = "";
		try {
			/*检查主表及主表对应的明细表中是否存在系统类型为追踪单号的字段，如果存在删除追踪单号表（tblTrackBill）中数据*/
			deleteTrackBill(conn, tableName, id);

			Obj beforeObject = new Obj();
			ArrayList<DBFieldInfoBean> listField = (ArrayList<DBFieldInfoBean>) ((DBTableInfoBean) allTables.get(tableName)).getFieldInfos();
			// 保存删除的单据的编号，以便记录到日志
			String logfieldName = "";
			for (int i = 0; i < listField.size(); i++) {
				DBFieldInfoBean fieldBean = listField.get(i);
				if ("RowMarker".equals(fieldBean.getFieldSysType())) {
					logfieldName = fieldBean.getFieldName();
					break;
				}
			}
			// 保存删除的单据的编号，以便记录到日志,同时取得单据的类型是否是草稿
			String workflowNodeName = "";
			String sql = "";
			DBFieldInfoBean logdbField=null;
			if (logfieldName.length() > 0) {				
				logdbField = DDLOperation.getFieldInfo(allTables, tableName, logfieldName);
			}	
			if (logdbField != null && ( logdbField.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || logdbField.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE)) {
				sql = "select tblLanguage." + locale.toString() + " "+logfieldName+"_lan," + tableName + ".* from " + tableName + " left join tblLanguage on tblLanguage.id=" + 
				tableName + "." + logfieldName + " where " + tableName+ ".id = ? ";
			} else {
				sql = "select * from " + tableName + " where " + tableName + ".id = ? ";
			}
			HashMap values = new HashMap();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
            
            ResultSet rset = pstmt.executeQuery();
            if (rset.next()) {
            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
            		Object obj=rset.getObject(i);
            		if(obj==null){
            			if(rset.getMetaData().getColumnType(i)==Types.NUMERIC||rset.getMetaData().getColumnType(i)==Types.INTEGER){
            				values.put(rset.getMetaData().getColumnName(i), 0);
            			}else{
            				values.put(rset.getMetaData().getColumnName(i), "");
            			}
            		}else{
            			values.put(rset.getMetaData().getColumnName(i), obj);
            		}
            	}
            }else{
            	rs.retCode = ErrorCanst.DEFAULT_FAILURE;
            	rs.retVal="单据不存在，或已经被删除";
            	BaseEnv.log.error("DynDBManager.delete error 单据不存在");
            	return rs;
            }
		
			if (logdbField != null ) {
				if (logdbField.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || logdbField.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE ) {
					billNos.add(values.get(logfieldName+"_lan"));
				} else {
					billNos.add(values.get(logfieldName));
				}
			}
			workflowNodeName = values.get("workFlowNodeName")+"";
			
			Result defineRs = null;
			boolean isDraft = "draft".equals(workflowNodeName);
			if (!isDraft) {
				//执行表自定义语句
				defineRs = defineSqlDel(conn, "delete", false, tableName,values, (Hashtable) allTables, id, userId, resources, locale);
				if (defineRs.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
					rs.setRetCode(defineRs.getRetCode());
					rs.setRetVal(defineRs.getRetVal());
					BaseEnv.log.error("DynDBManager.delete before delete defineSqlDel Error code = " + defineRs.getRetCode() + " Msg=" + defineRs.getRetVal());
					return rs;
				}
				if (defineRs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
					rs.setRetCode(defineRs.retCode);
					rs.setRetVal(defineRs.retVal);
				}
			}

			//执行删除前的接口
			BaseCustomFunction impl = (BaseCustomFunction) BaseEnv.functionInterface.get(tableName);
			if (impl != null) {
				Result interfaceRs = impl.onBeforDelete(conn, tableName, allTables, new String[]{id}, beforeObject);
				if (interfaceRs.getRetCode() < 0) {
					rs.setRetCode(interfaceRs.getRetCode());
					rs.setRetVal(interfaceRs.getRetVal());
					BaseEnv.log.error("DynDBManager onBeforDelete Error code = " + interfaceRs.getRetCode() + " Msg=" + interfaceRs.getRetVal());
					return rs;
				}
				if (defineRs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
					rs.setRetCode(defineRs.retCode);
					rs.setRetVal(defineRs.retVal);
				}
			}

			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, tableName);
			//查询是否有图片和附件
			String pic = "";
			String affix = "";
			for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
				DBFieldInfoBean fib = (DBFieldInfoBean) tableInfo.getFieldInfos().get(i);
				if (fib.getFieldType() == DBFieldInfoBean.FIELD_PIC) {
					pic = fib.getFieldName();
				} else if (fib.getFieldType() == DBFieldInfoBean.FIELD_AFFIX) {
					affix = fib.getFieldName();
				}
			}

			ArrayList childTables = DDLOperation.getChildTables(tableName, allTables);
			ArrayList brotherTables = DDLOperation.getBrotherTables(tableName, allTables);

			/*如果是草稿，判断草稿是否存在*/
			if (isDraft) {
				execsql = "SELECT * FROM  " + tableName + "  WHERE ID = ? and workFlowNodeName='draft'";
				BaseEnv.log.debug("DynDBManager.delete id="+id+";sql ="+execsql);
				PreparedStatement pss = conn.prepareStatement(execsql);
				pss.setString(1, id);
				ResultSet rss = pss.executeQuery();
				if (!rss.next()) {
					rs.setRetCode(ErrorCanst.EXCELBILL_FIELDVALUE_NOTEXIST);
					conn.rollback();
					return rs;
				}
			}
			if (pic.length() > 0 || affix.length() > 0) {
				//有图片或附件，查询图片或附件删除
				String selectField = "";
				if (pic.length() > 0) {
					selectField = pic;
				}
				if (affix.length() > 0) {
					selectField = selectField.length() > 0 ? selectField + "," + affix : affix;
				}
				String querySql = " select " + selectField + " from  " + tableInfo.getTableName() + "  WHERE ID = ? ";
				PreparedStatement pss = conn.prepareStatement(querySql);
				pss.setString(1, id);
				BaseEnv.log.debug("DynDBManager.delete id="+id+";sql ="+querySql);
				ResultSet rss = pss.executeQuery();
				if (rss.next()) {
					if (pic.length() > 0) {
						files[0] += rss.getString(pic);
					}
					if (affix.length() > 0) {
						files[1] += rss.getString(affix);
					}
				}
			}
			//删除多语言
			KRLanguageQuery.delete(conn, tableInfo, id, "id");

			for (int j = 0; j < childTables.size(); j++) {
				DBTableInfoBean childTable = (DBTableInfoBean) childTables.get(j);
				if (childTable.getIsView() != 1) { //视图不进行删除
					//删除多语言
					KRLanguageQuery.delete(conn, childTable, id, "f_ref");
					String delSql = "delete from " + childTable.getTableName() + " WHERE f_ref = ? ";
					PreparedStatement pss = conn.prepareStatement(delSql);
					pss.setString(1, id);
					BaseEnv.log.debug("DynDBManager.delete id="+id+";sql ="+delSql);
					pss.execute();
				}
			}
			for (int j = 0; j < brotherTables.size(); j++) {
				DBTableInfoBean brotherTable = (DBTableInfoBean) brotherTables.get(j);
				if (brotherTable.getPerantTableName().split(";").length > 1) {
					//删除多语言
					KRLanguageQuery.delete(conn, brotherTable, id, "f_brother");
					String delSql = "delete from " + brotherTable.getTableName() + " WHERE f_brother = ? ";
					PreparedStatement pss = conn.prepareStatement(delSql);
					pss.setString(1, id);
					BaseEnv.log.debug("DynDBManager.delete id="+id+";sql ="+delSql);
					pss.execute();
				}
			}

			/*   zxy 现在采用目录树管理所以，在删除子级后不能把父级类型变火子节点了
			Result parCataRs = delParentCatalog(conn, tableName, allTables, id);
			if (parCataRs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				rs.setRetCode(parCataRs.getRetCode());
				rs.setRetVal(parCataRs.getRetVal());
				BaseEnv.log.error("DynDBManager before delete update ParentCatalog Error code = " + parCataRs.getRetCode() + " Msg=" + parCataRs.getRetVal());
				return rs;
			}*/

			//执行删除语句
			execsql = " DELETE FROM  " + tableInfo.getTableName() + "  WHERE ID = ? ";
			PreparedStatement cs = conn.prepareStatement(execsql);
			cs.setString(1, id);
			BaseEnv.log.debug("DynDBManager.delete id="+id+";sql ="+execsql);
			cs.execute();
			
			/*删除工作流信息*/
			execsql = "DELETE FROM OAMyWorkFlowDet where f_ref in (select id from OAMyWorkFlow where keyId=? and tableName=?)";
			cs = conn.prepareStatement(execsql);
			cs.setString(1, id);
			cs.setString(2, tableInfo.getTableName());
			BaseEnv.log.debug("DynDBManager.delete id="+id+";sql ="+execsql);
			cs.execute();
			execsql = "DELETE FROM OAMyWorkFlow where keyId=? and tableName=?";
			cs = conn.prepareStatement(execsql);
			cs.setString(1, id);
			cs.setString(2, tableInfo.getTableName());
			BaseEnv.log.debug("DynDBManager.delete id="+id+";sql ="+execsql);
			cs.execute();
			execsql = "DELETE FROM OAMyWorkFlowPerson where keyId=? and tableName=?";
			cs = conn.prepareStatement(execsql);
			cs.setString(1, id);
			cs.setString(2, tableInfo.getTableName());
			BaseEnv.log.debug("DynDBManager.delete id="+id+";sql ="+execsql);
			cs.execute();
			
			//删除完毕后重置单据编号  fjj
			if(delBillNo)
			for (int z = 0; z < tableInfo.getFieldInfos().size(); z++) {
				DBFieldInfoBean df = (DBFieldInfoBean) tableInfo.getFieldInfos().get(z);
				if (df.getFieldIdentityStr() != null && DBFieldInfoBean.FIELD_IDENTIFIER.equals(df.getFieldIdentityStr())) {
					//该字段是单据编号
					BillNo billno = new BillNoManager().find(tableInfo.getTableName() + "_" + df.getFieldName(), conn); //加载Map并赋值
					if (billno != null && billno.isFillBack()) {
						//ArrayList<E> billNos 保存要删除的单据编号
						for (int h = 0; h < billNos.size(); h++) {
							String billCode = String.valueOf(billNos.get(h));
							if (billCode != null && billCode.length() > 0) {
								billno.remove(billCode, conn);
							}
						}
					}
				}
			}

			//保存删除模块脚本
			if (tableInfo.getTableName().equalsIgnoreCase("tblModules") || tableInfo.getTableName().equalsIgnoreCase("tblModelOperations")) {
				String sqlTime = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
				delArray.add("--" + sqlTime + "删除模板\n");
				for (int j = 0; j < tableInfo.getFieldInfos().size(); j++) {
					DBFieldInfoBean df = (DBFieldInfoBean) tableInfo.getFieldInfos().get(j);
					if (df.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE || df.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE) {
						delArray.add("delete from tblLanguage where id in (select " + df.getFieldName() + " from " + tableInfo.getTableName() + " where id='" + id + "')\n");
					}
				}
				delArray.add("DELETE FROM tblModelOperations where f_ref='" + id + "'");
				delArray.add(" DELETE FROM  " + tableInfo.getTableName() + "  WHERE ID ='" + id + "'");
			}
			
			//如果该表存在子表或邻居表，并且子表和邻居表对应的主表有多个，则删除该表时应执行下面代码删除子表或邻居表数据
			for (int j = 0; j < childTables.size(); j++) {
				DBTableInfoBean childTable = (DBTableInfoBean) childTables.get(j);
				if (childTable.getPerantTableName().split(";").length > 1 && childTable.getIsView() == 0) {
					Result rs2 = delChildOrBrotherTables(conn, childTable.getTableName(), "f_ref", id);
					if (rs2.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
						rs.setRetCode(rs2.getRetCode());
						rs.setRetVal(rs2.getRetVal());
						BaseEnv.log.error("DynDBManager.delete  deletechild sql Error code = " + rs2.getRetCode() + " Msg=" + rs2.getRetVal());
						return rs;
					}
				}
			}
			for (int j = 0; j < brotherTables.size(); j++) {
				DBTableInfoBean brotherTable = (DBTableInfoBean) brotherTables.get(j);
				if (brotherTable.getPerantTableName().split(";").length > 1 && brotherTable.getIsView() == 0) {
					Result rs2 = delChildOrBrotherTables(conn, brotherTable.getTableName(), "f_brother", id);
					if (rs2.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
						rs.setRetCode(rs2.getRetCode());
						rs.setRetVal(rs2.getRetVal());
						BaseEnv.log.error("DynDBManager.delete  deletebrother sql Error code = " + rs2.getRetCode() + " Msg=" + rs2.getRetVal());
						return rs;
					}

				}
			}
			if (!isDraft) {
				//执行表自定义语句
				defineRs = defineSqlDel(conn, "delete", true, tableName,values, (Hashtable) allTables, id, userId, resources, locale);
				if (defineRs.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
					rs.setRetCode(defineRs.getRetCode());
					rs.setRetVal(defineRs.getRetVal());
					BaseEnv.log.error("DynDBManager.delete after delete defineSqlDel Error code = " + defineRs.getRetCode() + " Msg=" + defineRs.getRetVal());
					return rs;
				}
				if (defineRs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
					rs.setRetCode(defineRs.retCode);
					rs.setRetVal(defineRs.retVal);
				}
				//执行删除后的接口
				if (impl != null) {
					Result interfaceRs = impl.onAfterDelete(conn, tableName, allTables, new String[]{id}, beforeObject);
					if (interfaceRs.getRetCode() < 0) {
						rs.setRetCode(interfaceRs.getRetCode());
						rs.setRetVal(interfaceRs.getRetVal());
						BaseEnv.log.error("DynDBManager.delete onAfterDelete Error code = " + interfaceRs.getRetCode() + " Msg=" + interfaceRs.getRetVal());
						return rs;
					}
				}
			}
		} catch (SQLException ex) {
			if (ex.getErrorCode() == 2714) {
				rs.setRetCode(ErrorCanst.RET_TABLENAME_EXIST_ERROR);
			} else {
				rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			}
			BaseEnv.log.error("DynDBManager.delete data Error :" + execsql, ex);
			return rs;
		}
		return rs;
	}

	/**
	 *删除关联主表数据在子表或兄弟表的数据
	 * @param con
	 * @param tablename
	 * @param flag
	 * @param toId
	 * @return
	 */
	private Result delChildOrBrotherTables(Connection con, String tablename, String flag, String id) {
		Result rs = new Result();
		try {
			Statement stmt = con.createStatement();
				String sql = "delete from " + tablename + " where " + flag + "='" + id + "'";
				stmt.executeUpdate(sql);
				BaseEnv.log.debug("DynDBManager.delChildOrBrotherTables id="+id+"; sql ="+sql);
		} catch (Exception ex) {
			BaseEnv.log.error(" DynDBManager.delChildOrBrotherTables Error  ", ex);
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		}
		return rs;
	}

	/**
	 * 执行插入一条语句
	 * @param conn Connection
	 * @param tableName String
	 * @param allTables Map
	 * @param values HashMap
	 * @return Result
	 */
	public Result execInert(Connection conn, String tableName, Map allTables, HashMap values, String saveType, MessageResources resources, Locale locale) {
		Result rs = new Result();
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);

		String insertTableName = getInsertTableName(tableInfo.getTableName());//处理CRM多模板插入数据库的表名

		rs = validator(tableInfo.getFieldInfos(), values, conn, insertTableName, true, "", resources, locale);
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return rs;
		}

		String nstr = "";
		String vstr = "";
		ArrayList paramList = new ArrayList();
		ArrayList moreLangageList = new ArrayList();

		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean field = ((DBFieldInfoBean) tableInfo.getFieldInfos().get(i));
			String fieldName = field.getFieldName();
			int fieldType = field.getFieldType();

			if (tableInfo.getTableType() == DBTableInfoBean.CHILD_TABLE && fieldName.equals("id") && field.getFieldType() == DBFieldInfoBean.FIELD_INT) {
				continue; //如果是自增长的明细表
			}
			if(field.getInputType() == DBFieldInfoBean.INPUT_CUT_LINE){
				continue;
			}

			if (values.get(fieldName) != null && !"detOrderNo".equals(fieldName)) {
				nstr += "," + fieldName;
				vstr += ",?";
				if (fieldType == DBFieldInfoBean.FIELD_INT) {
					if (values.get(fieldName).toString().length() == 0) {
						paramList.add(0);
					} else {
						paramList.add((int)(Double.parseDouble(values.get(fieldName).toString())));
					}
				} else if (fieldType == DBFieldInfoBean.FIELD_DOUBLE) {
					if (values.get(fieldName).toString().length() == 0) {
						paramList.add(0);
					} else {
						paramList.add(new Double(values.get(fieldName).toString()));
					}
				} else if (field.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE) {
					//多语言
					KRLanguage lan = (KRLanguage) values.get(fieldName);
					moreLangageList.add(lan);
					paramList.add(lan.getId());
				} else {
					paramList.add(values.get(fieldName).toString());
				}
			}
		}
		if (nstr.length() > 0) {
			nstr = nstr.substring(1);
			vstr = vstr.substring(1);
		}

		String sql = " INSERT INTO  " + insertTableName + "(" + nstr + ") values(" + vstr + ") ";
		Object param="";
		BaseEnv.log.debug(sql);
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < paramList.size(); i++) {
				param=paramList.get(i);
				pstmt.setObject(i + 1,param);
				BaseEnv.log.debug(i+1+":"+param);
			}
			pstmt.execute();
			//获得insert脚本
			addSql = ConvertToSql.getSqlByTNameAndParams(tableInfo.getTableName(), paramList, nstr, moreLangageList, values.get("id").toString());
			//插入多语言脚本
			for (int i = 0; i < moreLangageList.size(); i++) {
				KRLanguage lan = (KRLanguage) moreLangageList.get(i);
				KRLanguageQuery.saveToDB(lan.map, lan.getId(), conn);
				addSql += ConvertToSql.getLanguageStr(lan, tableInfo.getTableName());
			}

			return rs;
		} catch (SQLException ex) {
			rs.setRetCode(ex.getErrorCode() == 2601 ? ex.getErrorCode() : ErrorCanst.DEFAULT_FAILURE);
			rs.setRetVal(ex.getMessage());
			BaseEnv.log.error("DynDBManager.execInert Error",ex);
			return rs;
		} catch (Exception ex) {
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			BaseEnv.log.error("DynDBManager.execInert Error",ex);
			rs.setRetVal(ex.getMessage());
			return rs;
		}
	}

	/**
	 * 执行整体删除从表
	 * @param conn Connection
	 * @param tableName String
	 * @param id String
	 * @return Result
	 */
	public Result execDelete(Connection conn, String tableName, String id, ArrayList childList) {
		Result rs = new Result();
		try {
			PreparedStatement pstmt;
			String sql;
			//删除子表数据脚本

			sql = " DELETE FROM  " + tableName + "  WHERE f_ref = ? ";
			BaseEnv.log.debug(sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.execute();

			return rs;
		} catch (SQLException ex) {
			BaseEnv.log.error(" DynDBManager.execDelete Error  ", ex);
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			return rs;
		}
	}

	/**
	 * 执行插入一条语句
	 * @param conn Connection
	 * @param tableName String
	 * @param allTables Map
	 * @param values HashMap
	 * @return Result
	 */
	public Result execUpdate(Connection conn, String tableName, Map allTables, HashMap values,Hashtable sessionSet) {
		Result rs = new Result();
		DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
		Result rs_values = setValuesDefault(tableInfo.getFieldInfos(), values,sessionSet);

		String insertTableName = getInsertTableName(tableInfo.getTableName());//处理CRM多模板插入数据库的表名
		rs = validator(tableInfo.getFieldInfos(), values, conn, insertTableName, false, values.get("id").toString(), null, null);
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return rs;
		}

		String nstr = "";
		String id = values.get("id").toString();
		ArrayList paramList = new ArrayList();
		ArrayList moreLangageList = new ArrayList();

		for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
			DBFieldInfoBean field = ((DBFieldInfoBean) tableInfo.getFieldInfos().get(i));
			String fieldName = field.getFieldName();
			int fieldType = field.getFieldType();
			if(field.getInputType() == DBFieldInfoBean.INPUT_CUT_LINE||
					(field.getInputType() == DBFieldInfoBean.INPUT_NO 
					&& !field.getFieldName().equalsIgnoreCase("Period")
					&& !field.getFieldName().equalsIgnoreCase("PeriodYear")
					&& !field.getFieldName().equalsIgnoreCase("PeriodMonth")
					&& !field.getFieldName().equalsIgnoreCase("lastUpdateBy")
					&& !field.getFieldName().equalsIgnoreCase("lastUpdateTime"))){
				continue;
			}
			if (values.get(fieldName) != null) {
				if (fieldName.equals("id")) {
					id = values.get(fieldName).toString();
				} else  {
					nstr += "," + fieldName + "=?";
					if (fieldType == DBFieldInfoBean.FIELD_INT) {
						if (values.get(fieldName).toString().length() == 0) {
							paramList.add(0);
						} else {
							paramList.add(new Integer(values.get(fieldName).toString()));
						}
					} else if (fieldType == DBFieldInfoBean.FIELD_DOUBLE) {
						if (values.get(fieldName).toString().length() == 0) {
							paramList.add(0);
						} else {
							paramList.add(new Double(values.get(fieldName).toString()));
						}
					} else if (field.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE) {
						//多语言
						KRLanguage lan = (KRLanguage) values.get(fieldName);
						moreLangageList.add(lan);
						paramList.add(lan.getId());

					} else {
						paramList.add(values.get(fieldName).toString());
					}
				}
			}
		}
		if (id == null || id.length() == 0) {
			rs.retCode = ErrorCanst.RET_ID_NO_VALUE_ERROR;
			rs.retVal = "ID is  Null";
			return rs;
		}
		if (nstr.length() > 0) {
			nstr = nstr.substring(1);
		}

		String sql = " UPDATE " + insertTableName + " SET " + nstr + " WHERE ID = ? ";
		Object param="";
		BaseEnv.log.debug(sql);
		try {
			//先删除所有多语言
			KRLanguageQuery.delete(conn, tableInfo, values.get("id").toString(), "id");

			PreparedStatement pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < paramList.size(); i++) {
				param=paramList.get(i);
				pstmt.setObject(i + 1,param);
				BaseEnv.log.debug(i + 1+":"+param);
			}
			pstmt.setString(paramList.size() + 1, id);
			pstmt.execute();
			//保存更新主表的脚本
			if (tableInfo.getTableName().equalsIgnoreCase("tblModules")) {
				mainUpdate = ConvertToSql.getUpModuleSqlBy(conn, paramList, id, tableInfo);
			}
			//插入多语言脚本
			for (int i = 0; i < moreLangageList.size(); i++) {
				KRLanguage lan = (KRLanguage) moreLangageList.get(i);
				KRLanguageQuery.saveToDB(lan.map, lan.getId(), conn);
				mainUpdate += ConvertToSql.getLanguageStr(lan, insertTableName);
			}

			return rs;
		} catch (SQLException ex) {
			BaseEnv.log.error(" DynDBManager.execInert Error  ", ex);
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			return rs;
		}
	}

	private Result setValuesDefault(List fieldList, HashMap values,Hashtable sessionSet) {
		Result rs = new Result();
		try {
			for (int i = 0; i < fieldList.size(); i++) {
				DBFieldInfoBean field = (DBFieldInfoBean) fieldList.get(i);
				Object o = values.get(field.getFieldName());

				if (o == null || (o != null && o.toString().trim().length() == 0)) {
					if (field.getFieldType() == 0 || field.getFieldType() == 1) {
						values.remove(field.getFieldName());
						if (field.getDefaultValue() != null && field.getDefaultValue().length() > 0) {
							if (field.getDefaultValue().indexOf("@MEM:") >= 0) {
								ArrayList sysParam = new ArrayList();
								ConfigParse.parseSentenceGetParam(field.getDefaultValue(), null, sysParam, null, null, null, null);

								HashMap sysMap = ConfigParse.getSystemParam(sysParam, "00001");
								String def = ConfigParse.parseSentencePutParam(field.getDefaultValue(), null, sysMap, null, null, null, null, null, null);
								values.put(field.getFieldName(), def);
							}else if(field.getDefaultValue().contains("@Sess:")){
			            		String name=field.getDefaultValue().substring(field.getDefaultValue().indexOf("@Sess:")+"@Sess:".length());
			            		name = name.indexOf(";")>0?name.substring(0,name.indexOf(";")):name;
			            		String value=sessionSet.get(name)==null?"":sessionSet.get(name).toString();
			            		values.put(field.getFieldName(),value);
			            	} else {
								values.put(field.getFieldName(), field.getDefaultValue());
							}
						} else {
							values.put(field.getFieldName(), 0);
						}
					}
				}

			}
		} catch (Exception ex) {
			BaseEnv.log.error("DYNDBManager.setValuesDefault Error ",ex);
		}
		rs.setRetVal(values);
		return rs;
	}

	public Result setChildDefault(DBTableInfoBean tableInfo, HashMap values,Hashtable sessionSet) {
		ArrayList childTableList = DDLOperation.getChildTables(tableInfo.getTableName(), BaseEnv.tableInfos);
		Result rs = new Result();
		try {
			for (int i = 0; i < childTableList.size(); i++) {
				DBTableInfoBean childTb = (DBTableInfoBean) childTableList.get(i);
				ArrayList childList = (ArrayList) values.get("TABLENAME_" + childTb.getTableName());
				if (childList == null) {
					continue;
				}
				for (int j = 0; j < childList.size(); j++) {
					HashMap childMap = (HashMap) childList.get(j);
					for (int k = 0; k < childTb.getFieldInfos().size(); k++) {
						DBFieldInfoBean field = (DBFieldInfoBean) childTb.getFieldInfos().get(k);
						if (field != null) {
							Object val = childMap.get(field.getFieldName());
							if ((val == null || (val != null && val.toString().length() == 0))) {
								if (field.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE || field.getFieldType() == DBFieldInfoBean.FIELD_INT) {
									if (field.getDefaultValue() != null && field.getDefaultValue().length() > 0) {
										if (field.getDefaultValue().indexOf("@MEM:") >= 0) {
											ArrayList sysParam = new ArrayList();
											ConfigParse.parseSentenceGetParam(field.getDefaultValue(), null, sysParam, null, null, null, null);
	
											HashMap sysMap = ConfigParse.getSystemParam(sysParam, "00001");
											String def = ConfigParse.parseSentencePutParam(field.getDefaultValue(), null, sysMap, null, null, null, null, null, null);
											values.put(field.getFieldName(), def);
										}else if(field.getDefaultValue().contains("@Sess:")){
						            		String name=field.getDefaultValue().substring(field.getDefaultValue().indexOf("@Sess:")+"@Sess:".length());
						            		name = name.indexOf(";")>0?name.substring(0,name.indexOf(";")):name;
						            		String value=sessionSet.get(name)==null?"":sessionSet.get(name).toString();
						            		childMap.put(field.getFieldName(),value);
						            	}else{
						            		childMap.put(field.getFieldName(), field.getDefaultValue());
						            	}
									} else {
										childMap.put(field.getFieldName(), "0");
									}
								} else {
									if (field.getDefaultValue() != null && field.getDefaultValue().length() > 0) {
										if (field.getDefaultValue().indexOf("@MEM:") >= 0) {
											ArrayList sysParam = new ArrayList();
											ConfigParse.parseSentenceGetParam(field.getDefaultValue(), null, sysParam, null, null, null, null);
	
											HashMap sysMap = ConfigParse.getSystemParam(sysParam, "00001");
											String def = ConfigParse.parseSentencePutParam(field.getDefaultValue(), null, sysMap, null, null, null, null, null, null);
											values.put(field.getFieldName(), def);
										}else if(field.getDefaultValue().contains("@Sess:")){
						            		String name=field.getDefaultValue().substring(field.getDefaultValue().indexOf("@Sess:")+"@Sess:".length());
						            		name = name.indexOf(";")>0?name.substring(0,name.indexOf(";")):name;
						            		String value=sessionSet.get(name)==null?"":sessionSet.get(name).toString();
						            		childMap.put(field.getFieldName(),value);
						            	}else{
						            		childMap.put(field.getFieldName(), field.getDefaultValue());
						            	}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			BaseEnv.log.error("DYNDBManager.setChildDefault Error ",ex);
		}
		return rs;
	}

	/**
	 * 验证非空
	 * @param fields
	 * @param values
	 * @param tableName
	 * @return
	 */
	private Result validator(List fields, Map values, String locale) {
		Result rs = new Result();
		for (int i = 0; i < fields.size(); i++) {
			DBFieldInfoBean field = (DBFieldInfoBean) fields.get(i);
			Object o = values.get(field.getFieldName());
			if (field.getFieldName().equals("id") || field.getFieldName().equals("f_ref")) {
				continue;
			}
			/*判断非空*/
			if (field.getInputType() != 3 && field.getInputType() != 100 && field.getIsNull() == 1 && (o == null || o.toString().trim().length() == 0)) {
				rs.retCode = ErrorCanst.RET_FIELD_IS_NULL;
				rs.setRetVal(field.getDisplay().get(locale));
				break;
			}
		}
		return rs;
	}

	/**
	 * 字段效验
	 * @param fields List
	 * @param values Map
	 * @return String
	 */
	private Result validator(List fields, Map values, Connection conn, String tableName, boolean isInsert, String id, MessageResources resources, Locale locale) {
		Result rs = new Result();

		for (int i = 0; i < fields.size(); i++) {
			DBFieldInfoBean field = (DBFieldInfoBean) fields.get(i);

			Object o = values.get(field.getFieldName());
			if (field.getFieldName().equals("id") || field.getFieldName().equals("f_ref") || o == null || o.toString().trim().length() == 0) {
				continue;
			}
			String value = o.toString().trim();
			//这里对数据合法性的教研，放到Validator类中了

			//进行唯一性判断且表名不等于CRMClientInfo,CRM单独处理唯一性
			if (field.getIsUnique() == DBFieldInfoBean.UNIQUE_YES && !tableName.startsWith("CRMClientInfo")) {
				try {
					String moduleType = String.valueOf(values.get("moduleType"));
					if("null".equals(moduleType)){
						moduleType = null;
					}
					if (isInsert) {
						String sql = "select count(*) from " + tableName + " where " + field.getFieldName() + "=?";
						if(moduleType != null && moduleType.trim().length() > 0){
							sql += " and moduleType='"+moduleType+"'";
						}
						PreparedStatement stmt = conn.prepareStatement(sql);
						stmt.setString(1, value);
						ResultSet rset = stmt.executeQuery();
						int count = 0;
						if (rset.next()) {
							count = rset.getInt(1);
						}

						if (count > 0) {
							rs.retCode = ErrorCanst.MULTI_VALUE_ERROR;
							rs.retVal = tableName + "." + field.getFieldName() +":"+value;
							return rs;
						}
					} else {
						String sql = "select count(*) from " + tableName + " where " + field.getFieldName() + "=? and id<>?";
						if(moduleType != null && moduleType.trim().length() > 0){
							sql += " and  moduleType='"+moduleType+"'";
						}
						PreparedStatement stmt = conn.prepareStatement(sql);
						stmt.setString(1, value);
						stmt.setString(2, id);
						ResultSet rset = stmt.executeQuery();
						int count = 0;
						if (rset.next()) {
							count = rset.getInt(1);
						}

						if (count > 0) {
							rs.retCode = ErrorCanst.MULTI_VALUE_ERROR;
							rs.retVal = tableName + "." + field.getFieldName() +":"+value;
							return rs;
						}
					}
				} catch (SQLException ex1) {
					BaseEnv.log.error("DynDBManager.validator Error",ex1);
					rs.retCode = ErrorCanst.DEFAULT_FAILURE;
					rs.retVal = "Error unique" + field.getFieldName();
					return rs;
				}
			}

		}
		return rs;
	}

	/**
	 * 根据表名，id值查出对应字段的值
	 * @param keyId String 表的id值
	 * @param tableName String 表名
	 * @param fieldName String 字段名
	 * @return Object 字段值
	 */
	public Object getFieldValueById(final String[] ids, final String tableName, final String fieldName) {
		final Result res = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String idVals = "";
							for (int i = 0; i < ids.length; i++) {
								idVals += "'" + ids[i] + "',";
							}
							String sql = " select " + fieldName + " from " + tableName + " where id in (" + idVals.substring(0, idVals.length() - 1) + ") and workFlowNodeName='finish' order by "
									+ fieldName;
							PreparedStatement psmt = connection.prepareStatement(sql);
							ResultSet rst = psmt.executeQuery();
							if (rst.next()) {
								res.retVal = rst.getObject(1);
							}
						} catch (Exception ex) {
							res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return res.getRetCode();
			}
		});
		res.setRetCode(retCode);

		if (res.retCode == ErrorCanst.DEFAULT_SUCCESS && res.retVal != null) {
			return res.retVal;
		}
		return null;
	}

	public Result defineAuditingOrReverseSql(final String tableName, final Hashtable allTables, final String[] keyid, final String userId, final String type, final MessageResources resources,
			final Locale locale) {
		final Result res = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, tableName);
							String fieldCalculate = tableInfo.getFieldCalculate();
							if (fieldCalculate == null || fieldCalculate.trim().length() == 0) {
								// 没有定义sql语句
								res.setRetCode(ErrorCanst.RET_NO_DEFINE_SQL);
								return;
							}

							while (fieldCalculate.indexOf("<sql") > -1) {
								// 分析出语句
								String defineSql = fieldCalculate.substring(fieldCalculate.indexOf("<sql"), fieldCalculate.indexOf(">"));
								fieldCalculate = fieldCalculate.substring(fieldCalculate.indexOf(">") + 1);

								// 分析sql类型是否type一致
								String operation = getAttribute("operation", defineSql);

								// 表信息中<update saveField="" selfKey="" matchKey="">等三个属性
								String saveField = getAttribute("saveField", defineSql);
								String selfKey = getAttribute("selfKey", defineSql);
								String matchKey = getAttribute("matchKey", defineSql);

								// 分析id
								String sqlName = getAttribute("id", defineSql);
								String formerDefine = sqlName;

								if (type != null && type != "" && type.equals(operation.trim())) {
									DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(sqlName);

									if (defineSqlBean == null) {
										BaseEnv.log.error("Define Sql Not Exist :Name = " + sqlName);
										res.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
										return;
									}

									for (int i = 0; i < keyid.length; i++) {
										HashMap paramList = new HashMap();
										paramList.put("id", keyid[i]);
										Result ret = defineSqlBean.execute(conn, paramList, userId, resources, locale, "");
										if (ret.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
											res.setRetCode(ret.getRetCode());
											res.setRetVal(ret.getRetVal());
											return;
										}
									}
								}
							}
						} catch (Exception ex) {
							res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
						}
					}
				});
				return res.getRetCode();
			}
		});
		res.setRetCode(retCode);
		return res;
	}

	public Result getSuperValue(final String tableName, final String parentCode, final ArrayList copySupField) {
		final Result res = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select ";
							for (int i = 0; i < copySupField.size(); i++) {
								sql += ((DBFieldInfoBean) copySupField.get(i)).getFieldName() + ",";
							}
							sql = sql.substring(0, sql.length() - 1);
							sql += " from " + tableName + " where classCode='" + parentCode + "'";

							Statement st = conn.createStatement();
							ResultSet rss = st.executeQuery(sql);
							HashMap copySupValue = new HashMap();
							if (rss.next()) {
								for (int i = 0; i < copySupField.size(); i++) {
									copySupValue.put(((DBFieldInfoBean) copySupField.get(i)).getFieldName(), rss.getString(i + 1));
								}
							}

							res.setRetVal(copySupValue);
						} catch (Exception ex) {
							res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
						}
					}
				});
				return res.getRetCode();
			}
		});
		res.setRetCode(retCode);
		return res;
	}

	public Result setPopDisplay(final DBFieldInfoBean fi, final String value, final HashMap values, final String sunCompany, final String userId, final Hashtable allTables) {
		final Result rs = new Result();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							Statement cs = conn.createStatement();
							String refsql = getRefSql(fi,false, value, sunCompany, allTables, true, values, userId);
							if (refsql != null) {
								BaseEnv.log.debug(refsql);
								ResultSet crset = cs.executeQuery(refsql);
								if (crset.next()) {
									for (int j = 0; j < fi.getSelectBean().getViewFields().size(); j++) {
										PopField posf = ((PopField) fi.getSelectBean().getViewFields().get(j));
										String t = posf.fieldName.substring(0, posf.fieldName.indexOf("."));
										String f = posf.fieldName.substring(posf.fieldName.indexOf(".") + 1);
										DBFieldInfoBean fb = DDLOperation.getFieldInfo(BaseEnv.tableInfos, t, f);

										values.put(posf.asName, crset.getObject(j + 1));
									}
								}

							}
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 判断单据编号是否已经存在
	 * @param tableName
	 * @param parentCode
	 * @param fieldName
	 * @param urlValue
	 * @return
	 */
	public Result existBillNo(String tableName, String billNo, DBFieldInfoBean field, Connection conn) {
		Result res = new Result();
		try {
			String sql = "select * from " + tableName + " where " + field.getFieldName() + "='" + billNo + "'";
			Statement st = conn.createStatement();
			ResultSet rss = st.executeQuery(sql);
			if (rss.next()) {
				res.retCode = ErrorCanst.DEFAULT_SUCCESS;
			} else {
				res.retCode = ErrorCanst.DEFAULT_FAILURE;
			}
		} catch (Exception ex) {
			res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			ex.printStackTrace();
		}
		return res;
	}

	public Result getLastValue(final String tableName, final String parentCode, final String fieldName, final String urlValue) {
		final Result res = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							/*  zxy
							 *  select top 1 EmpNumber from tblEmployee where EmpNumber like '%[0-9]' and (classCode is null or len(classCode)=0 or len(classCode)=5)  order by left(' '+EmpNumber,PATINDEX('%[0-9]%',EmpNumber)) desc,convert(decimal,right(EmpNumber,len(EmpNumber)-PATINDEX('%[0-9]%',EmpNumber)+1)) desc
							 *  如上语句在empnumber字段有数据有纯字母时，会报错，不如直接取最大的数据，一般来说一个公司的编号规则应该是固定的，所以通过max取会较简单
							 */
							String sql = "select max(right('                '+" + fieldName + ",20) ) ";
							if (parentCode.length() > 0) {
								sql += " from " + tableName + " where " + fieldName + " like '%[0-9]' and classCode like '" + parentCode + "_____' " + urlValue;
							} else {
								sql += " from " + tableName + " where " + fieldName + " like '%[0-9]' and (classCode is null or len(classCode)=0 or len(classCode)=5) " + urlValue;
							}
							//   						left(GoodsNumber,case patindex('[0-9]',GoodsNumber) when 0 then 1 else patindex('[0-9]',GoodsNumber) end -1)
							//sql += " order by left(' '+"+fieldName+",PATINDEX('%[0-9]%',"+fieldName+")) desc,convert(decimal,right("+fieldName+",len("+fieldName+")-PATINDEX('%[0-9]%',"+fieldName+")+1)) desc ";
							Statement st = conn.createStatement();

							ResultSet rss = st.executeQuery(sql);
							if (rss.next()) {
								String s = rss.getString(1);
								s = s==null?"":s.trim();
								//去掉空格
								res.setRetVal(s);
							}

						} catch (Exception ex) {
							res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
						}
					}
				});
				return res.getRetCode();
			}
		});
		res.setRetCode(retCode);
		
		if(res.retCode==ErrorCanst.DEFAULT_SUCCESS){
			if(res.getRetVal()!=null){
    			String value=res.getRetVal().toString();
    			if(value.matches("\\w*\\.?\\d*$")){ //原正则:"\\w*\\d$"--6月22号修改为 "\\w*\\.?\\d*$" 匹配01.0001类型数据
    				for(int j=value.length()-1;j>=0;j--){
    					if(!Character.isDigit(value.charAt(j))||(Character.isDigit(value.charAt(j))&&j==0)){
    						String numStr=value;
    						if(j>0||(!Character.isDigit(value.charAt(j))&&j==0)){
    							numStr=value.substring(j+1);
    						}
    						String numStr2=String.valueOf(Long.parseLong(numStr));
    						String zero=numStr.substring(0,numStr.indexOf(numStr2));
    						long num=Long.parseLong(numStr2);
    						if(!value.equals(numStr)){
    							value=value.substring(0,j+1);
    						}else{
    							value="";
    						}
    						if(String.valueOf(num+1).length()>String.valueOf(numStr2).length()&&zero.length()>0){
    							value+=zero.substring(0,zero.length()-1)+(num+1);
    						}else{
    							value+=zero+(num+1);
    						}
    						break;
    					}
    				}
    				res.setRetVal(value);
    			}
			}
		}
		
		return res;
	}

	/**
	 * 更新当前的单据编号+1
	 * @param code
	 * @param clear
	 * @param length
	 * @return
	 */
	public boolean updateNewCode(String defaultValue, Connection conn) {
		final Result rs = new Result();
		try {
			String code = defaultValue.substring("@CODE:".length(), defaultValue.indexOf("@", "@CODE:".length()));
			String sql = "update tblCodeGenerate set curValue=curValue+1 where code=?";
			PreparedStatement pss = conn.prepareStatement(sql);
			pss.setString(1, code);
			int n = pss.executeUpdate();
			if (n > 0) {
				rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
			} else {
				rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		}
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return false;
		} else {
			return true;
		}
	}

	public Result getRelation(final String tableName, final String[] ids, final Hashtable tblMap, final String local) {
		final Result res = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select TargetTbl,SRField,TRField from tblRelation where SourceTbl='" + tableName + "'";
							Statement st = conn.createStatement();
							ResultSet rss = st.executeQuery(sql);
							List list = new ArrayList();

							String queryField = "";

							while (rss.next()) {
								String[] obj = new String[3];
								obj[0] = rss.getString(1);
								obj[1] = rss.getString(2);
								obj[2] = rss.getString(3);
								list.add(obj);
								queryField += obj[1] + ",";
							}
							String idStr = "";
							for (int i = 0; i < ids.length; i++) {
								idStr += "'" + ids[i] + "',";
							}
							if (idStr.length() > 0) {
								idStr = idStr.substring(0, idStr.length() - 1);
							}
							String tblNames = "";
							if (queryField.length() > 0) {
								queryField = queryField.substring(0, queryField.length() - 1);
								sql = "select " + queryField + " from " + tableName + " where id in (" + idStr + ")";
								rss = st.executeQuery(sql);

								ArrayList dataList = new ArrayList();
								while (rss.next()) {
									String[] obj = new String[list.size()];
									for (int i = 0; i < list.size(); i++) {
										obj[i] = rss.getString(i + 1);
									}
									dataList.add(obj);
								}

								for (int i = 0; i < list.size(); i++) {
									String[] obj = (String[]) list.get(i);
									String SRValue = "";
									for (int j = 0; j < dataList.size(); j++) {
										String[] dataObj = (String[]) dataList.get(j);
										SRValue += "'" + dataObj[i] + "',";
									}
									SRValue = SRValue.substring(0, SRValue.length() - 1);
									sql = "select count(0) from " + obj[0] + " where " + obj[2] + " in (" + SRValue + ")";
									rss = st.executeQuery(sql);
									if (rss.next()) {
										if (rss.getInt(1) > 0) {
											DBTableInfoBean db = (DBTableInfoBean) tblMap.get(obj[0]);
											tblNames += db.getDisplay().get(local) + " ";
										}
									}
								}
							}
							if (tblNames.length() > 0) {
								res.setRetCode(ErrorCanst.RET_EXISTSRELATION_ERROR);
								res.setRetVal(tblNames);
							}
							res.setRetVal(tblNames);
						} catch (Exception ex) {
							res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
						}
					}
				});
				return res.getRetCode();
			}
		});
		res.setRetCode(retCode);
		return res;
	}

	/**
	 * 验证当前登录用户是否是反审核人
	 * @param loginId
	 * @param worFlowDesign
	 * @return
	 */

	public static boolean isRetCheckPer(LoginBean login, OAWorkFlowTemplate workFlowDesign, String deptCode) {
		String allPersons = workFlowDesign.getAllowVisitor();
		String rule = workFlowDesign.getRetCheckPerRule();

		//先查询当前登录用户是否在所选择的反审核人中
		if (allPersons!=null && (allPersons.indexOf("," + login.getDepartCode() + ",") >= 0 || allPersons.indexOf("," + login.getId() + ",") >= 0)) {
			if (rule == null || rule.equals("0")) {//全部审核人
				return true;
			} else if (rule.equals("1")) {//本部门审核人
				if (login.getDepartCode().equals(deptCode)) {
					return true;
				} else {
					return false;
				}
			} else if (rule.equals("2")) {//上级部门审核人
				if (deptCode.indexOf(login.getDepartCode()) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (rule.equals("3")) {//下级部门审核人
				if (login.getDepartCode().indexOf(deptCode) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (rule.equals("4")) {//一级部门审核人
				if (login.getDepartCode().length() == 5) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	public String getRetCheckPer(OAWorkFlowTemplate workFlowDesign, String deptCode) {
		if (workFlowDesign == null)
			return "";
		String allPerSet = workFlowDesign.getAllowVisitor();
		if (allPerSet == null)
			return "";
		String rule = workFlowDesign.getRetCheckPerRule();
		ArrayList<OnlineUser> allPersons = new ArrayList<OnlineUser>();
		String[] temp = allPerSet.split("\\|")[0].split(",");
		for (int i = 0; i < temp.length; i++) {
			if (temp[i].length() > 0) {
				ArrayList<OnlineUser> users = OnlineUserInfo.getDeptUser(temp[i]);
				allPersons.addAll(users);

			}
		}
		temp = allPerSet.split("\\|")[1].split(",");
		for (int i = 0; i < temp.length; i++) {
			if (temp[i].length() > 0) {
				if (OnlineUserInfo.getUser(temp[i]) != null) {
					allPersons.add(OnlineUserInfo.getUser(temp[i]));
				}
			}
		}
		String persons = "";

		if (rule == null || rule.equals("0")) {// 全部审核人
			for (int i = 0; i < allPersons.size(); i++) {
				persons += allPersons.get(i).getId() + ",";
			}
		} else if (rule.equals("1")) {// 本部门审核人
			for (int i = 0; i < allPersons.size(); i++) {
				if (allPersons.get(i).getDeptId().equals(deptCode)) {
					persons += allPersons.get(i).getId() + ",";
				}
			}
		} else if (rule.equals("2")) {// 上级部门审核人
			for (int i = 0; i < allPersons.size(); i++) {
				if (deptCode.indexOf(allPersons.get(i).getDeptId()) == 0) {
					persons += allPersons.get(i).getId() + ",";
				}
			}
		} else if (rule.equals("3")) {// 下级部门审核人
			for (int i = 0; i < allPersons.size(); i++) {
				if (allPersons.get(i).getDeptId().indexOf(deptCode) == 0) {
					persons += allPersons.get(i).getId() + ",";
				}
			}
		} else if (rule.equals("4")) {// 一级部门审核人
			for (int i = 0; i < allPersons.size(); i++) {
				if (allPersons.get(i).getDeptId().length() == 5) {
					persons += allPersons.get(i).getId() + ",";
				}
			}
		}
		persons = persons.length() > 0 ? ";" + persons.substring(0, persons.length() - 1).replace(",", ";") + ";" : "";
		return persons;
	}

	public String getResource(MessageResources resources, Locale locale, String[] ss) {
		String ret = "";
		if (ss.length == 1) {
			ret = resources.getMessage(locale, ss[0]);
		} else if (ss.length == 2) {
			ret = resources.getMessage(locale, ss[0], ss[1]);
		} else if (ss.length == 3) {
			ret = resources.getMessage(locale, ss[0], ss[1], ss[2]);
		} else {
			ret = resources.getMessage(locale, ss[0], ss[1], ss[2], ss[3]);
		}
		return ret == null ? ss[0] : ret;
	}

	/**
	 * 处理CRM多模板插入数据库的表名
	 * @param tableName
	 * @return
	 */
	public String getInsertTableName(String tableName) {
		String insertTableName = "";//插入数据库的表名,用于处理新增模板表结构的表名

		if (tableName.indexOf("CRMClientInfoDet") != -1 && GlobalsTool.isDouble(tableName.substring(16))) {
			insertTableName = tableName.substring(0, 16);
		} else if (tableName.indexOf("CRMClientInfo") != -1 && GlobalsTool.isDouble(tableName.substring(13))) {
			insertTableName = tableName.substring(0, 13);
		} else {
			insertTableName = tableName;
		}
		return insertTableName;
	}
	
	private String certSum(String fieldName,String tableName){
		Pattern p1 = Pattern.compile("sum\\(([^\\)]+)\\)", Pattern.CASE_INSENSITIVE);
		Matcher m1 = p1.matcher(fieldName);
		String newFiledName = fieldName;
		while (m1.find()) {
			String f = m1.group(1);
			String a = m1.group();
			if (f.indexOf(".") >0) {
				String t = f.substring(0,f.indexOf("."));
				if(!f.equals(tableName)){
					String sumstr = " select sum( "+f+") from  "+t +" where "+t+".f_ref="+ tableName+".id ";
					newFiledName = newFiledName.substring(0,newFiledName.indexOf(a))+" ( "+sumstr+" ) "+
						newFiledName.substring(newFiledName.indexOf(a)+a.length());
				}
			}
		}
		return newFiledName;
	}

	/**
	 * 插入凭证
	 * @param templateNumber
	 * @param id
	 * @param conn
	 * @return
	 */
	public Result genCertificate(String userId, String templateNumber,CertificateBillBean bean, String id, Connection conn, final MessageResources resources, final Locale locale,final boolean isAuto) {
		Result rs = new Result();
		//取模板
		if(bean==null){ //defineSqlBean中如果已取模板详情，这里不再取
			rs = new CertTemplateMgt().detail(templateNumber, conn);
			if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				//取模板失败
				rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
				rs.retVal = "取凭证模板[" + templateNumber + "]失败";
				return rs;
			} else if (rs.retVal == null) {
				//模板不存在
				rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
				rs.retVal = "凭证模板[" + templateNumber + "]不存在";
				return rs;
			}
			//加载成功
			bean = (CertificateBillBean) rs.retVal;
		}
		DBTableInfoBean tableBean = BaseEnv.tableInfos.get(bean.getTableName());
		if (tableBean == null) {
			//取表名不存在
			rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
			rs.retVal = "凭证模板对应表名[" + bean.getTableName() + "]不存在";
			return rs;
		}
		
		if("1".equals(bean.getType())){//月结模板
			return genSettleCertificate(userId,  bean, conn, resources, locale);
		}

		try {
			String accMainId = "0" + id;
			String workFlowNodeName = ""; //单据审核状态
			String certificateNo = ""; //是否已经产生凭证
			String billNo = "";
			String billDate = "";
			String createBy = "";
			String createTime = "";
			String lastUpdateBy = "";
			String lastUpdateTime = "";
			String SCompanyID = "";
			String departmentCode = "";
			String employeeID = "";
			String deptFullName = "";
			String empFullName = "";

			String comFullName = "";//往来单位名称，用于备注
			String goodsFullName = "";//商品名称，用于备注

			String periodStatusId = "";//当前单据的会计期单状态，2为已月结
			//凭证会计期间
			int period = 0;
			int periodYear = 0;
			int periodMonth = 0;
			//当前的会计期单
			Hashtable ht = (Hashtable) BaseEnv.sessionSet.get(userId);
			

			int maxOrderNo = 1;//当前期单最大凭证号
			boolean isExistCompanyCode = containsField("CompanyCode", tableBean);
			boolean isExistDepartmentCode = containsField("DepartmentCode", tableBean);
			boolean isExistEmployeeId = containsField("EmployeeId", tableBean);

			//查询单据会计期间的状态
			String sql = "select b.StatusId as PeriodStatusId,a.billNo,PeriodYear,Period from " + bean.getTableName() + " a,tblAccPeriod b where a.id=? and a.PeriodYear=b.AccYear and a.Period=b.AccPeriod";
			PreparedStatement ps2 = conn.prepareStatement(sql);
			ps2.setString(1, id);
			ResultSet rss = ps2.executeQuery();
			if (rss.next()) {
				periodStatusId = rss.getString("periodStatusId");
				billNo = rss.getString("billNo");
				periodYear = rss.getInt("PeriodYear");
				period = rss.getInt("Period");
				if("2".equals(periodStatusId)){					
					rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
					rs.retVal = "单据"+billNo+"期间"+periodYear+"."+period+"已月结，不可以再生成凭证";
					return rs;
				}
			}else{
				//如果找不到数据，肯定是tblAccPeriod表数据不正确
				rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
				rs.retVal = "会计期间表tblAccPeriod数据异常";
				return rs;
			}

			//查询单据----如果是手工产生凭证，可以凭证期间和单据业务期间不一致，所以如果当前会计期单已经月结，则要把产生的凭证期间调整为当前期单。
			sql = "select PeriodYear  as PeriodYear,Period  as Period,"
					+ " PeriodMonth as PeriodMonth,a.workFlowNodeName,CertificateNo,a.billNo,a.BillDate,a.createBy,"
					+ "a.createTime,a.lastUpdateBy,a.lastUpdateTime,a.SCompanyID,b.id EmployeeID,b.empFullName,"
					+(isExistDepartmentCode ? "a.DepartmentCode" : "''") + " as DepartmentCode ,"+ 
					(isExistDepartmentCode ? "c.deptFullName" : "''") + " as deptFullName, "+
					(isExistCompanyCode ? "d.ComFullName" : "''")
					+ " as ComFullName from " + bean.getTableName() + " a " + "left join tblEmployee b on "+(isExistEmployeeId?" a.EmployeeId":"a.createBy")+"=b.id " 
					+ (isExistDepartmentCode ? "left join tblDepartment c on a.departmentCode=c.classCode " : "") 
					+ (isExistCompanyCode ? "left join tblCompany d on a.CompanyCode=d.classCode " : "") + " where a.id=?";
			BaseEnv.log.debug(" DynDBManager. genCertificate 取表相关数据sql=" + sql);
			ps2 = conn.prepareStatement(sql);
			ps2.setString(1, id);
			rss = ps2.executeQuery();
			if (rss.next()) {
				periodYear = rss.getInt("PeriodYear");
				period = rss.getInt("Period");
				periodMonth = rss.getInt("PeriodMonth");
				workFlowNodeName = rss.getString("workFlowNodeName");
				certificateNo = rss.getString("CertificateNo");
				billNo = rss.getString("billNo");
				billDate = rss.getString("BillDate");
				createBy = rss.getString("createBy");
				createTime = rss.getString("createTime");
				lastUpdateBy = rss.getString("lastUpdateBy");
				lastUpdateTime = rss.getString("lastUpdateTime");
				SCompanyID = rss.getString("SCompanyID");
				departmentCode = rss.getString("DepartmentCode");
				employeeID = rss.getString("EmployeeID");
				empFullName = rss.getString("empFullName");
				deptFullName = rss.getString("deptFullName");
				comFullName = rss.getString("ComFullName");
			} else {
				rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
				rs.retVal = "生成凭证，单据不存在 ID=" + id;
				return rs;
			}

			//手工产生凭证时进行验证
			String autoGenerateAcc = GlobalsTool.getSysSetting("autoGenerateAcc");
			if (autoGenerateAcc != null && "false".equals(autoGenerateAcc)) {
				if (!"finish".equals(workFlowNodeName)) { //手工产生凭证时，如果没有审核完毕，则提示不能再生成凭证
					rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
					rs.retVal = resources.getMessage(locale, "common.NotAppFinish.createAcc.error", billNo);
					return rs;
				}
				if (certificateNo != null && certificateNo.length() > 0) { //手工产生凭证时，如果已经产生凭证，则提示不能再生成凭证
					rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
					rs.retVal = resources.getMessage(locale, "common.hasCreateAcc.error", billNo);
					return rs;
				}
			}
			//<!--拿到当前期间的最大凭证号-->
			sql = "select isnull(max(OrderNo),0) maxOrderNo from tblAccMain where SCompanyID=? and period=? and CredYear=?";
			ps2 = conn.prepareStatement(sql);
			ps2.setString(1, SCompanyID);
			ps2.setInt(2, period);
			ps2.setInt(3, periodYear);
			rss = ps2.executeQuery();
			if (rss.next()) {
				maxOrderNo = rss.getInt("maxOrderNo");
			}

			ArrayList<AccDetailBean> accList = new ArrayList(); //记录凭证明细

			//跟据凭证模板，插入凭证明细表
			for (CertificateTemplateBean ctb : bean.getTempList()) {
				//****科目要考虑核算的情况****
				//科目，如果是全数字，说明是科目编号，要加上引号
				if (ctb.getAccCode().matches("[\\d]*")) {
					ctb.setAccCode("'" + ctb.getAccCode() + "'");
				}
				
				
				//计算表的个数
				ArrayList<String> tableList = new ArrayList();

				if (ctb.getAccCode().indexOf(".") > 0) {//科目字段
					String t = ctb.getAccCode().substring(0, ctb.getAccCode().indexOf("."));
					if (!t.equals(tableBean.getTableName()) && !tableList.contains(t)) {
						tableList.add(t);
					}
				}
				//要去掉sum子查询
				String noSumField = ctb.getFieldName();
				noSumField = noSumField.replaceAll("sum\\(([^\\)]+)\\)", " ");
				noSumField = noSumField.replaceAll("SUM\\(([^\\)]+)\\)", " ");
				//金额字段
				Pattern pattern = Pattern.compile("([\\w]*)\\.", Pattern.CASE_INSENSITIVE);				
				Matcher matcher = pattern.matcher(noSumField);
				while (matcher.find()) {
					String t = matcher.group(1);
					//因为金额中可能包含 数字，所以这里要判断，如果表名是纯数字的，要去掉
					if (!t.matches("\\d") && !t.equals(tableBean.getTableName()) && !tableList.contains(t)) {
						tableList.add(t);
					}
				}

				ctb.setComment(ctb.getComment()==null || ctb.getComment().length()==0?"''":ctb.getComment());
				
				//如果fieldName中包含sum 应该被解释成子查询
				ctb.setFieldName(certSum(ctb.getFieldName(),tableBean.getTableName()));
				ctb.setCurFieldName(certSum(ctb.getCurFieldName(),tableBean.getTableName()));
				
				
				ctb.setDepartmentCode(ctb.getDepartmentCode()==null || ctb.getDepartmentCode().length()==0?"''":ctb.getDepartmentCode());
				ctb.setEmployeeID(ctb.getEmployeeID()==null || ctb.getEmployeeID().length()==0?"''":ctb.getEmployeeID());
				ctb.setCompanyCode(ctb.getCompanyCode()==null || ctb.getCompanyCode().length()==0?"''":ctb.getCompanyCode());
				ctb.setCurrency(ctb.getCurrency()==null || ctb.getCurrency().length()==0?"''":ctb.getCurrency());
				ctb.setCurrencyRate(ctb.getCurrencyRate()==null || ctb.getCurrencyRate().length()==0?"'1'":ctb.getCurrencyRate());
				ctb.setProjectCode(ctb.getProjectCode()==null || ctb.getProjectCode().length()==0?"''":ctb.getProjectCode());
				ctb.setStockCode(ctb.getStockCode()==null || ctb.getStockCode().length()==0?"''":ctb.getStockCode());
				
				String hasCompany = "";
				String hasGoods="";
				Pattern pt = Pattern.compile("([\\w]+\\.[\\w]+)",Pattern.CASE_INSENSITIVE);
				Matcher mat = pt.matcher(ctb.getComment());
				String commstr = "";
				String endstr = "";
				int start = 0;
				while (mat.find()) {
					String all = mat.group();
					String name = mat.group(1);
					//除了金额字段中的表，其它字段表不可以加入join当于，因为这会超成金额翻倍，比如金额从主表取数，其这字段有从明细表取数如商品，当明细有多行时，主表金额会翻倍，
					//所以如果从主表金额，是不可join明细表的，
//					String t  = name.substring(0,name.indexOf("."));
//					if (!t.equals(tableBean.getTableName()) && !tableList.contains(t)) {
//						tableList.add(t);
//					}
					if(name.endsWith("CompanyCode")){
						hasCompany = name;
						name = "tblCompanyCC.ComFullName";
					}else if(name.endsWith("GoodsCode")){
						hasGoods = name;
						name = "tblGoodsCC.GoodsFullName";
					}
					
					int spos = mat.start();
					commstr += "+'"+ ctb.getComment().substring(start,spos)+"'+"+name;
					start = mat.end();
				}
				if(start<ctb.getComment().length() -1){
					commstr += "+'"+ ctb.getComment().substring(start)+"'";
				}
				if(commstr.length() > 0){
					commstr = commstr.substring(1);
				}
				
				
				sql = "select Comment,CompanyCode,DepartmentCode,EmployeeID,AccCode,ProjectCode,StockCode,Currency,CurrencyRate,sum(DebitAmount) DebitAmount,"
						+ "sum(DebitCurrencyAmount) DebitCurrencyAmount,sum(LendAmount) LendAmount,sum(LendCurrencyAmount) LendCurrencyAmount  from " + "( select " + 
						"isnull("+commstr + ",'') Comment," + 
						"isnull("+ctb.getCompanyCode() + ",'') CompanyCode," + 
						"isnull("+ctb.getDepartmentCode() + ",'') DepartmentCode," + 
						"isnull("+ctb.getEmployeeID() + ",'') EmployeeID," + 
						"isnull("+ctb.getProjectCode() + ",'') ProjectCode, "+ 
						"isnull("+ctb.getStockCode() +  ",'') StockCode, " +
						"isnull("+ctb.getCurrency() + ",'') Currency," + 
						ctb.getCurrencyRate() + " CurrencyRate," + 
						ctb.getAccCode() + " AccCode," + 
						(ctb.getDirc() == 1 ? ctb.getFieldName() : "0") + " DebitAmount," + 
						
						(ctb.getDirc() == 1 ? ctb.getCurFieldName() : "0") + " DebitCurrencyAmount," + 
						(ctb.getDirc() == 2 ? ctb.getFieldName() : "0") + " LendAmount," + 
						(ctb.getDirc() == 2 ? ctb.getCurFieldName() : "0")+ " LendCurrencyAmount"+ 
						" from   " + tableBean.getTableName();

				//查询表中是否有tblGoodsAccProp
				if (tableList.contains("tblGoodsAccProp")) {
					//存货核算科目
					String goodTable = ctb.getGoodsCode().substring(0, ctb.getGoodsCode().indexOf("."));

					sql += " join " + goodTable + "  on " + tableBean.getTableName() + ".id=" + goodTable + ".f_ref ";
					sql += " join tblGoods on " + ctb.getGoodsCode() + "=tblGoods.classCode ";
					sql += " join tblGoodsAccProp on tblGoods.CostMethod=tblGoodsAccProp.id ";
					tableList.remove(goodTable);
					tableList.remove("tblGoodsAccProp");
				}
				for (String t : tableList) {
					sql += " join " + t + "  on " + tableBean.getTableName() + ".id=" + t + ".f_ref ";
				}
				if(!hasCompany.equals("")){
					sql += " join  tblCompany tblCompanyCC  on " + hasCompany + "=tblCompanyCC.classCode ";
				}
				if(!hasGoods.equals("")){
					sql += " join  tblGoods tblGoodsCC  on " + hasGoods + "=tblGoodsCC.classCode ";
				}
				

				sql += "  where " + tableBean.getTableName() + ".id=? ) detail " + 
				" group by Comment,CompanyCode,DepartmentCode,EmployeeID,AccCode,ProjectCode,StockCode,Currency,CurrencyRate";

				BaseEnv.log.debug(" DynDBManager. genCertificate 取凭证明细数据sql=" + sql);
				ps2 = conn.prepareStatement(sql);
				ps2.setString(1, id);
				rss = ps2.executeQuery();
				int digit = GlobalsTool.getFieldDigits("tblAccDetail", "DebitAmount");
				while (rss.next()) {
					AccDetailBean accb = new AccDetailBean();
					accb.setRefBillID(id); 
					accb.setRefBillType(bean.getTableName());
					accb.setCompanyCode(rss.getString("CompanyCode"));
					accb.setDepartmentCode(rss.getString("DepartmentCode"));
					accb.setEmployeeID(rss.getString("EmployeeID"));
					accb.setAccCode(rss.getString("AccCode"));
					accb.setDebitAmount(rss.getBigDecimal("DebitAmount").setScale(digit, BigDecimal.ROUND_HALF_UP));
					accb.setCurrency(rss.getString("Currency"));
					accb.setCurrencyRate(rss.getBigDecimal("CurrencyRate"));
					accb.setDebitCurrencyAmount(rss.getBigDecimal("DebitCurrencyAmount").setScale(digit, BigDecimal.ROUND_HALF_UP));
					accb.setLendAmount(rss.getBigDecimal("LendAmount").setScale(digit, BigDecimal.ROUND_HALF_UP));
					accb.setLendCurrencyAmount(rss.getBigDecimal("LendCurrencyAmount").setScale(digit, BigDecimal.ROUND_HALF_UP));
					accb.setPeriodYear(periodYear);
					accb.setPeriodMonth(periodMonth);
					accb.setAccDate(billDate);
					accb.setCreateBy(createBy);
					accb.setCreateTime(createTime);
					accb.setLastUpdateBy(lastUpdateBy);
					accb.setLastUpdateTime(lastUpdateTime);
					accb.setSCompanyID(SCompanyID);
					AccMainBean amb = new AccMainBean();
					amb.setId(accMainId);
					accb.setAccMainBean(amb);
					accb.setRecordComment(rss.getString("Comment"));
					accb.setProjectCode(rss.getString("ProjectCode"));
					accb.setStockCode(rss.getString("StockCode"));
					if (accb.getCurrency() == null || accb.getCurrency().length() == 0) {
						accb.setDebitCurrencyAmount(new BigDecimal(0));
						accb.setLendCurrencyAmount(new BigDecimal(0));
					}
					accList.add(accb);
				}
			}
			ArrayList<AccDetailBean> newAccList = new ArrayList<AccDetailBean>();
			HashMap<String,AccTypeBean> accTypeMap = new HashMap<String,AccTypeBean>();
			for (int i = accList.size() - 1; i >= 0; i--) {
				AccDetailBean accb = accList.get(i);
				//根据核算合并信息
				if (accb.getDebitAmount().doubleValue() != 0 || accb.getLendAmount().doubleValue() != 0) {
					//检查没有启用核算的科目，合并数量
					if(accTypeMap.get(accb.getAccCode())==null){
						//查询科目核算信息
						AccTypeBean typeb =getAccType(accb.getAccCode(),conn);
						if(typeb == null){
							BaseEnv.log.error(" DynDBManager. genCertificate "+bean.getTempName()+":"+bean.getTempNumber()+" 科目代号不存在"+accb.getAccCode());
							rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
							rs.retVal = "凭证模板对应"+bean.getTempName()+":"+bean.getTempNumber()+" 科目代号不存在";
							return rs; 
						}
						if(typeb.getIsDept() != 1){
							accb.setDepartmentCode("");
						}
						if(typeb.getIsPersonal() != 1){
							accb.setEmployeeID("");
						}
						//单独产生的凭证可以屏蔽这一项目，因为往来是固定的，不会导致不同往来的非核算项目分开多行问题
						//if(typeb.getIsClient() != 1 && typeb.getIsProvider() != 1){
						//	accb.setCompanyCode("");
						//}
						if(typeb.getIsStock() != 1){
							accb.setStockCode("");
						}
						if(typeb.getIsForCur() != 1){
							accb.setCurrency("");
							accb.setCurrencyRate(new BigDecimal("1"));
						}
						if(typeb.getIsProject() != 1){
							accb.setProjectCode("");
						}
						newAccList.add(0,accb);
						accTypeMap.put(accb.getAccCode(), typeb);
					}else{
						AccTypeBean typeb =accTypeMap.get(accb.getAccCode());
						if(typeb.getIsDept() != 1){
							accb.setDepartmentCode("");
						}
						if(typeb.getIsPersonal() != 1){
							accb.setEmployeeID("");
						}
						//if(typeb.getIsClient() != 1 && typeb.getIsProvider() != 1){
						//	accb.setCompanyCode("");
						//}
						if(typeb.getIsStock() != 1){
							accb.setStockCode("");
						}
						if(typeb.getIsForCur() != 1){
							accb.setCurrency("");
							accb.setCurrencyRate(new BigDecimal("1"));
						}
						if(typeb.getIsProject() != 1){
							accb.setProjectCode("");
						}
						boolean found = false;
						for(AccDetailBean adb1 :newAccList){
							if(accb.getAccCode().equals(adb1.getAccCode())&&
									accb.getDepartmentCode().equals(adb1.getDepartmentCode())&&
									accb.getEmployeeID().equals(adb1.getEmployeeID())&&
									accb.getCompanyCode().equals(adb1.getCompanyCode())&&
									accb.getStockCode().equals(adb1.getStockCode())&&
									accb.getCurrency().equals(adb1.getCurrency())&&
									accb.getProjectCode().equals(adb1.getProjectCode())){
								if((adb1.getDebitAmount().doubleValue()!=0&&accb.getDebitAmount().doubleValue()!=0)||
										(adb1.getLendAmount().doubleValue()!=0&&accb.getLendAmount().doubleValue()!=0)){
									adb1.setDebitAmount(adb1.getDebitAmount().add(accb.getDebitAmount()));
									adb1.setDebitCurrencyAmount(adb1.getDebitCurrencyAmount().add(accb.getDebitCurrencyAmount()));
									adb1.setLendAmount(adb1.getLendAmount().add(accb.getLendAmount()));
									adb1.setLendCurrencyAmount(adb1.getLendCurrencyAmount().add(accb.getLendCurrencyAmount()));
									found = true;
								}
							}
						}
						if(!found){
							newAccList.add(0,accb);
						}
					}
				}
			}		
			BaseEnv.log.debug(" DynDBManager. genCertificate "+bean.getTempName()+":"+bean.getTempNumber()+"取凭证明细 返回总行数=" + accList.size()+"合并后："+newAccList.size());
			accList = newAccList;
			
			BigDecimal debit = new BigDecimal("0");
			BigDecimal lend = new BigDecimal("0");
			for (int i = accList.size() - 1; i >= 0; i--) {
				AccDetailBean accb = accList.get(i);				
				debit =debit.add(accb.getDebitAmount());
				lend = lend.add(accb.getLendAmount());
				//检查科目有没有启用核算
				accb.setAccCode(getAccNumber(accb.getAccCode(), accb.getCompanyCode(), accb.getStockCode(), accb.getDepartmentCode(), accb.getEmployeeID(), accb.getProjectCode(), accb
						.getCurrency(), accb.getSCompanyID(), conn));
				
			}
			if (accList.size() == 0) {
				rs.retCode = ErrorCanst.DEFAULT_SUCCESS;
				return rs;
			}
			if (debit.compareTo(lend) != 0) {
				rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
				rs.retVal = "生成凭证借贷不平衡（借:"+debit+";贷:"+lend+"），请检查凭证模板正确性";
				return rs;
			}

			//插入凭证主表数据
			sql = "insert into tblAccMain (id,CredTypeID,OrderNo,BillDate,RefBillType,RefBillID,RefBillNo,CredYear,CredMonth,Period,"
					+ "createBy,createTime,lastUpdateBy,lastUpdateTime,SCompanyID,workFlowNodeName,workFlowNode,checkPersons,AutoBillMarker, DepartmentCode,EmployeeID,isAuditing) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			ps2 = conn.prepareStatement(sql);
			ps2.setString(1, accMainId);
			ps2.setString(2, bean.getCredTypeID());
			ps2.setInt(3, maxOrderNo + 1);
			ps2.setString(4, billDate);
			ps2.setString(5, bean.getTableName());
			ps2.setString(6, id);
			ps2.setString(7, billNo);
			ps2.setInt(8, periodYear);
			ps2.setInt(9, periodMonth);
			ps2.setInt(10, period);
			ps2.setString(11, createBy);
			ps2.setString(12, createTime);
			ps2.setString(13, lastUpdateBy);
			ps2.setString(14, lastUpdateTime);
			ps2.setString(15, SCompanyID);
			//如果凭证启用审核流，则要置workFlowNode为开始结点 

			ps2.setString(16, "notApprove");
			ps2.setString(17, "0");
			ps2.setString(18, ';' + createBy + ';');
			ps2.setInt(19, isAuto?1:0);
			ps2.setString(20, departmentCode);
			ps2.setString(21, employeeID);
			ps2.setString(22, "start");
			ps2.executeUpdate();

			for (AccDetailBean accb : accList) {
				//插入凭证明细
				sql = "insert into tblAccDetail (id,RefBillID,RefBillType,CompanyCode,DepartmentCode,EmployeeID,AccCode,DebitAmount,Currency,CurrencyRate,"
						+ "DebitCurrencyAmount,LendAmount,LendCurrencyAmount,PeriodYear,PeriodMonth,AccDate,createBy,createTime,lastUpdateBy,lastUpdateTime,"
						+ "SCompanyID,f_ref,RecordComment,ProjectCode) " + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				ps2 = conn.prepareStatement(sql);
				ps2.setString(1, IDGenerater.getId());
				ps2.setString(2, accb.getRefBillID());
				ps2.setString(3, accb.getRefBillType());
				ps2.setString(4, accb.getCompanyCode());
				ps2.setString(5, accb.getDepartmentCode());
				ps2.setString(6, accb.getEmployeeID());
				ps2.setString(7, accb.getAccCode());
				ps2.setBigDecimal(8, accb.getDebitAmount());
				ps2.setString(9, accb.getCurrency());
				ps2.setBigDecimal(10, accb.getCurrencyRate());
				ps2.setBigDecimal(11, accb.getDebitCurrencyAmount());
				ps2.setBigDecimal(12, accb.getLendAmount());
				ps2.setBigDecimal(13, accb.getLendCurrencyAmount());
				ps2.setInt(14, accb.getPeriodYear());
				ps2.setInt(15, accb.getPeriodMonth());
				ps2.setString(16, accb.getAccDate());
				ps2.setString(17, accb.getCreateBy());
				ps2.setString(18, accb.getCreateTime());
				ps2.setString(19, accb.getLastUpdateBy());
				ps2.setString(20, accb.getLastUpdateTime());
				ps2.setString(21, accb.getSCompanyID());
				ps2.setString(22, accb.getAccMainBean().getId());
				ps2.setString(23, accb.getRecordComment());
				ps2.setString(24, accb.getProjectCode());
				ps2.executeUpdate();
				
				System.out.println("acccode ="+accb.getAccCode());
			}
			//调proc_insertAccAfterRefOper
			CallableStatement cs = conn.prepareCall("{call proc_insertAccAfterRefOper(?,?,?,?,?,?,?)}");
			//凭证设置
			AccMainSettingBean settingBean = null;
			Result result = new VoucherMgt().queryVoucherSetting(conn);
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				settingBean = (AccMainSettingBean) result.retVal;
			}
			//IsAuditing=0不启用审核
			int isstart = settingBean != null ? settingBean.getIsAuditing() : 0;
			cs.setInt(1, isstart);
			cs.setString(2, accMainId);
			cs.setString(3, billDate);
			cs.setString(4, createBy);
			cs.setString(5, createTime);

			cs.registerOutParameter(6, Types.INTEGER);
			cs.registerOutParameter(7, Types.VARCHAR, 500);
			cs.execute();
			int retCode = cs.getInt(6);
			String retVal = cs.getString(7);
			if (retCode < 0) {
				rs.setRetCode(retCode);
				rs.setRetVal(retVal);
				return rs;
			}
			//更新，凭证标识
			sql = "update " + tableBean.getTableName() + " set CertificateNo=? where id=?";
			ps2 = conn.prepareStatement(sql);
			ps2.setString(1, accMainId);
			ps2.setString(2, id);
			ps2.execute();

		} catch (Exception e) {
			BaseEnv.log.error("DynDBManager.genCertificate Error:", e);
			rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
			rs.retVal = "生成凭证失败"+e.getMessage();
		}
		return rs;
	}
	
	/**
	 * 合并生成凭证
	 * @param userId
	 * @param SCompanyID
	 * @param departmentCode
	 * @param templateNumber
	 * @param ids
	 * @param conn
	 * @param resources
	 * @param locale
	 * @return
	 */
	public Result genAllCertificate(String userId, String SCompanyID, String departmentCode, String templateNumber, ArrayList<String> idList, Connection conn, final MessageResources resources, final Locale locale) {
		String idstr = "";
		for (String id : idList) {
			if (id != null && id.length() > 0) {
				idstr += ",'" + id + "'";
			}
		}
		idstr = idstr.substring(1);

		Result rs = new Result();
		//取模板
		rs = new CertTemplateMgt().detail(templateNumber, conn);
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			//取模板失败
			rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
			rs.retVal = "取凭证模板[" + templateNumber + "]失败";
			return rs;
		} else if (rs.retVal == null) {
			//模板不存在
			rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
			rs.retVal = "凭证模板[" + templateNumber + "]不存在";
			return rs;
		}
		//加载成功
		CertificateBillBean bean = (CertificateBillBean) rs.retVal;
		DBTableInfoBean tableBean = BaseEnv.tableInfos.get(bean.getTableName());
		if (tableBean == null) {
			//取表名不存在
			rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
			rs.retVal = "凭证模板对应表名[" + bean.getTableName() + "]不存在";
			return rs;
		}

		try {
			String accMainId = IDGenerater.getId();

			String createBy = userId;
			String createTime = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
			String lastUpdateBy = userId;
			String lastUpdateTime = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
			int periodYear = 0;
			int period = 0;
			

			//查询字些单据中是否有不在同一期间的数据，且是否月结
			String sql = "select b.StatusId as PeriodStatusId,PeriodYear,Period from " + bean.getTableName() + " a,tblAccPeriod b where a.id in ("+idstr+") and a.PeriodYear=b.AccYear and a.Period=b.AccPeriod group by b.StatusId ,PeriodYear,Period ";
			PreparedStatement ps2 = conn.prepareStatement(sql);
			ResultSet rss = ps2.executeQuery();
			int countPeriod = 0;
			while (rss.next()) {
				countPeriod ++;
				String periodStatusId = rss.getString("periodStatusId");
				periodYear = rss.getInt("PeriodYear");
				period = rss.getInt("Period");
				if("2".equals(periodStatusId)){					
					rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
					rs.retVal = "单据期间"+periodYear+"."+period+"已月结，不可以再生成凭证";
					return rs;
				}
			}
			if(countPeriod ==0){
				//如果找不到数据，肯定是tblAccPeriod表数据不正确
				rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
				rs.retVal = "会计期间表tblAccPeriod数据异常";
				return rs;
			}else if(countPeriod > 1){
				rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
				rs.retVal = "不同会计期间的单据不可以合并生成凭证";
				return rs;
			}
			
			
			//当前的会计期单
			Hashtable ht = (Hashtable) BaseEnv.sessionSet.get(userId);
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, periodYear);
			cal.set(Calendar.MONTH, period - 1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.roll(Calendar.DAY_OF_MONTH, -1);
			String billDate = BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd);

			int maxOrderNo = 1;//当前期单最大凭证号
			boolean isExistCompanyCode = containsField("CompanyCode", tableBean);

			//<!--拿到当前期间的最大凭证号-->
			sql = "select isnull(max(OrderNo),0) maxOrderNo from tblAccMain where SCompanyID=? and period=? and CredYear=?";
			ps2 = conn.prepareStatement(sql);
			ps2.setString(1, SCompanyID);
			ps2.setInt(2, period);
			ps2.setInt(3, periodYear);
			rss = ps2.executeQuery();
			if (rss.next()) {
				maxOrderNo = rss.getInt("maxOrderNo");
			}

			ArrayList<AccDetailBean> accList = new ArrayList(); //记录凭证明细

			//跟据凭证模板，插入凭证明细表
			for (CertificateTemplateBean ctb : bean.getTempList()) {
				//****科目要考虑核算的情况****
				//科目，如果是全数字，说明是科目编号，要加上引号
				if (ctb.getAccCode().matches("[\\d]*")) {
					ctb.setAccCode("'" + ctb.getAccCode() + "'");
				}
				
				//摘要
				ctb.setComment(GlobalsTool.getTableInfoBean(bean.getTableName()).getDisplay().get("zh_CN") + "合并凭证");
				//摘要
//				if (ctb.getComment() != null && ctb.getComment().length() > 0) { 
//					Pattern pattern = Pattern.compile("\\[([^\\[\\]]+)\\]", Pattern.CASE_INSENSITIVE);
//					Matcher matcher = pattern.matcher(ctb.getComment());
//					String rstr = ctb.getComment();
//					while (matcher.find()) {
//						String all = matcher.group();
//						String name = matcher.group(1);
//						rstr = rstr.replaceAll("\\[" + name + "\\]", "");
//					}
//					rstr+="合并凭证";
//					ctb.setComment(rstr);
//				}
				
				
				//计算表的个数
				ArrayList<String> tableList = new ArrayList();

				if (ctb.getAccCode().indexOf(".") > 0) {//科目字段
					String t = ctb.getAccCode().substring(0, ctb.getAccCode().indexOf("."));
					//因为金额中可能包含 数字，所以这里要判断，如果表名是纯数字的，要去掉
					if (!t.equals(tableBean.getTableName()) && !tableList.contains(t)) {
						tableList.add(t);
					}
				}
				//要去掉sum子查询
				String noSumField = ctb.getFieldName();
				noSumField = noSumField.replaceAll("sum\\(([^\\)]+)\\)", " ");
				noSumField = noSumField.replaceAll("SUM\\(([^\\)]+)\\)", " ");
				//金额字段
				Pattern pattern = Pattern.compile("([\\w]*)\\.", Pattern.CASE_INSENSITIVE);				
				Matcher matcher = pattern.matcher(noSumField);
				while (matcher.find()) {
					String t = matcher.group(1);
					if (!t.matches("\\d") && !t.equals(tableBean.getTableName()) && !tableList.contains(t)) {
						tableList.add(t);
					}
				}

				
				//如果fieldName中包含sum 应该被解释成子查询
				ctb.setFieldName(certSum(ctb.getFieldName(),tableBean.getTableName()));
				ctb.setCurFieldName(certSum(ctb.getCurFieldName(),tableBean.getTableName()));

				ctb.setDepartmentCode(ctb.getDepartmentCode()==null || ctb.getDepartmentCode().length()==0?"''":ctb.getDepartmentCode());
				ctb.setEmployeeID(ctb.getEmployeeID()==null || ctb.getEmployeeID().length()==0?"''":ctb.getEmployeeID());
				ctb.setCompanyCode(ctb.getCompanyCode()==null || ctb.getCompanyCode().length()==0?"''":ctb.getCompanyCode());
				ctb.setCurrency(ctb.getCurrency()==null || ctb.getCurrency().length()==0?"''":ctb.getCurrency());
				ctb.setCurrencyRate(ctb.getCurrencyRate()==null || ctb.getCurrencyRate().length()==0?"'1'":ctb.getCurrencyRate());
				ctb.setProjectCode(ctb.getProjectCode()==null || ctb.getProjectCode().length()==0?"''":ctb.getProjectCode());
				ctb.setStockCode(ctb.getStockCode()==null || ctb.getStockCode().length()==0?"''":ctb.getStockCode());
				
				int digit = GlobalsTool.getFieldDigits("tblAccDetail", "DebitAmount");
				sql = "select CompanyCode,DepartmentCode,EmployeeID,AccCode,ProjectCode,StockCode,Currency,CurrencyRate,sum(DebitAmount) DebitAmount,"
						+ "sum(DebitCurrencyAmount) DebitCurrencyAmount,sum(LendAmount) LendAmount,sum(LendCurrencyAmount) LendCurrencyAmount  from " + 
						"( select " + 
						"isnull("+ctb.getCompanyCode() + ",'') CompanyCode," + 
						"isnull("+ctb.getDepartmentCode() + ",'') DepartmentCode," + 
						"isnull("+ctb.getEmployeeID() + ",'') EmployeeID," + 
						"isnull("+ctb.getProjectCode() + ",'') ProjectCode, " + 
						"isnull("+ctb.getStockCode() + ",'') StockCode, " +
						"isnull("+ctb.getCurrency() + ",'') Currency," + 
						ctb.getCurrencyRate() + " CurrencyRate,"+ 
						ctb.getAccCode() + " AccCode,"+ 
						(ctb.getDirc() == 1 ? "round("+ctb.getFieldName()+","+digit+")" : "0") + " DebitAmount," + 						
						(ctb.getDirc() == 1 ? "round("+ctb.getCurFieldName()+","+digit+")" : "0") + " DebitCurrencyAmount," + 
						(ctb.getDirc() == 2 ? "round("+ctb.getFieldName()+","+digit+")" : "0") + " LendAmount,"+ 
						(ctb.getDirc() == 2 ? "round("+ctb.getCurFieldName()+","+digit+")" : "0")+ " LendCurrencyAmount"+ 	
						" from   " + tableBean.getTableName();

				//查询表中是否有tblGoodsAccProp
				if (tableList.contains("tblGoodsAccProp")) {
					//存货核算科目
					String goodTable = ctb.getGoodsCode().substring(0, ctb.getGoodsCode().indexOf("."));

					sql += " join " + goodTable + "  on " + tableBean.getTableName() + ".id=" + goodTable + ".f_ref ";
					sql += " join tblGoods on " + ctb.getGoodsCode() + "=tblGoods.classCode ";
					sql += " join tblGoodsAccProp on tblGoods.CostMethod=tblGoodsAccProp.id ";
					tableList.remove(goodTable);
					tableList.remove("tblGoodsAccProp");
				}
				for (String t : tableList) {
					sql += " join " + t + "  on " + tableBean.getTableName() + ".id=" + t + ".f_ref ";
				}

				sql += "  where " + tableBean.getTableName() + ".id in (" + idstr + ") ) detail "
						+ " group by CompanyCode,DepartmentCode,EmployeeID,AccCode,ProjectCode,StockCode,Currency,CurrencyRate";

				BaseEnv.log.debug(" DynDBManager. genAllCertificate 取凭证明细数据sql=" + sql);
				ps2 = conn.prepareStatement(sql);
				rss = ps2.executeQuery();
				while (rss.next()) {
					AccDetailBean accb = new AccDetailBean();
					accb.setRefBillID("");
					accb.setRefBillType(bean.getTableName());
					accb.setCompanyCode(rss.getString("CompanyCode"));
					accb.setDepartmentCode(rss.getString("DepartmentCode"));
					accb.setEmployeeID(rss.getString("EmployeeID"));
					accb.setAccCode(rss.getString("AccCode"));
					accb.setDebitAmount(rss.getBigDecimal("DebitAmount").setScale(digit, BigDecimal.ROUND_HALF_UP));
					accb.setCurrency(rss.getString("Currency"));
					accb.setCurrencyRate(rss.getBigDecimal("CurrencyRate"));
					accb.setDebitCurrencyAmount(rss.getBigDecimal("DebitCurrencyAmount").setScale(digit, BigDecimal.ROUND_HALF_UP));
					accb.setLendAmount(rss.getBigDecimal("LendAmount").setScale(digit, BigDecimal.ROUND_HALF_UP));
					accb.setLendCurrencyAmount(rss.getBigDecimal("LendCurrencyAmount").setScale(digit, BigDecimal.ROUND_HALF_UP));
					accb.setPeriodYear(periodYear);
					accb.setPeriodMonth(period);
					accb.setAccDate(billDate);
					accb.setCreateBy(createBy);
					accb.setCreateTime(createTime);
					accb.setLastUpdateBy(lastUpdateBy);
					accb.setLastUpdateTime(lastUpdateTime);
					accb.setSCompanyID(SCompanyID);
					AccMainBean amb = new AccMainBean();
					amb.setId(accMainId);
					accb.setAccMainBean(amb);
					accb.setRecordComment(ctb.getComment());
					accb.setProjectCode(rss.getString("ProjectCode"));
					accb.setStockCode(rss.getString("StockCode"));

					if (accb.getCurrency() == null || accb.getCurrency().length() == 0) {
						accb.setDebitCurrencyAmount(new BigDecimal(0));
						accb.setLendCurrencyAmount(new BigDecimal(0));
					}
					accList.add(accb);
				}
			}
			ArrayList<AccDetailBean> newAccList = new ArrayList<AccDetailBean>();
			HashMap<String,AccTypeBean> accTypeMap = new HashMap<String,AccTypeBean>();
			for (int i = accList.size() - 1; i >= 0; i--) {
				AccDetailBean accb = accList.get(i);
				//根据核算合并信息
				if (accb.getDebitAmount().doubleValue() != 0 || accb.getLendAmount().doubleValue() != 0) {
					//检查没有启用核算的科目，合并数量
					if(accTypeMap.get(accb.getAccCode())==null){
						//查询科目核算信息
						AccTypeBean typeb =getAccType(accb.getAccCode(),conn);
						if(typeb == null){
							BaseEnv.log.error(" DynDBManager. genAllCertificate "+bean.getTempName()+":"+bean.getTempNumber()+" 科目代号不存在"+accb.getAccCode());
							rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
							rs.retVal = "凭证模板对应"+bean.getTempName()+":"+bean.getTempNumber()+" 科目代号不存在";
							return rs; 
						}
						if(typeb.getIsDept() != 1){
							accb.setDepartmentCode("");
						}
						if(typeb.getIsPersonal() != 1){
							accb.setEmployeeID("");
						}
						//这里不能屏蔽这一项目，这会导致合并产生的凭证中不同往来的非核算项目分开
						if(typeb.getIsClient() != 1 && typeb.getIsProvider() != 1){
							accb.setCompanyCode("");
						}
						if(typeb.getIsStock() != 1){
							accb.setStockCode("");
						}
						if(typeb.getIsForCur() != 1){
							accb.setCurrency("");
							accb.setCurrencyRate(new BigDecimal("1"));
						}
						if(typeb.getIsProject() != 1){
							accb.setProjectCode("");
						}
						newAccList.add(0,accb);
						accTypeMap.put(accb.getAccCode(), typeb);
					}else{
						AccTypeBean typeb =accTypeMap.get(accb.getAccCode());
						if(typeb.getIsDept() != 1){
							accb.setDepartmentCode("");
						}
						if(typeb.getIsPersonal() != 1){
							accb.setEmployeeID("");
						}
						//这里不能屏蔽这一项目，这会导致合并产生的凭证中不同往来的非核算项目分开
						if(typeb.getIsClient() != 1 && typeb.getIsProvider() != 1){ 
							accb.setCompanyCode("");
						}
						if(typeb.getIsStock() != 1){
							accb.setStockCode("");
						}
						if(typeb.getIsForCur() != 1){
							accb.setCurrency("");
							accb.setCurrencyRate(new BigDecimal("1"));
						}
						if(typeb.getIsProject() != 1){
							accb.setProjectCode("");
						}
						boolean found = false;
						for(AccDetailBean adb1 :newAccList){
							if(accb.getAccCode().equals(adb1.getAccCode())&&
									accb.getDepartmentCode().equals(adb1.getDepartmentCode())&&
									accb.getEmployeeID().equals(adb1.getEmployeeID())&&
									accb.getCompanyCode().equals(adb1.getCompanyCode())&&
									accb.getStockCode().equals(adb1.getStockCode())&&
									accb.getCurrency().equals(adb1.getCurrency())&&
									accb.getProjectCode().equals(adb1.getProjectCode())){
								if((adb1.getDebitAmount().doubleValue()!=0&&accb.getDebitAmount().doubleValue()!=0)||
										(adb1.getLendAmount().doubleValue()!=0&&accb.getLendAmount().doubleValue()!=0)){
									adb1.setDebitAmount(adb1.getDebitAmount().add(accb.getDebitAmount()));
									adb1.setDebitCurrencyAmount(adb1.getDebitCurrencyAmount().add(accb.getDebitCurrencyAmount()));
									adb1.setLendAmount(adb1.getLendAmount().add(accb.getLendAmount()));
									adb1.setLendCurrencyAmount(adb1.getLendCurrencyAmount().add(accb.getLendCurrencyAmount()));
									found = true;
								}
							}
						}
						if(!found){
							newAccList.add(0,accb);
						}
					}
				}
			}		
			BaseEnv.log.debug(" DynDBManager. genAllCertificate "+bean.getTempName()+":"+bean.getTempNumber()+"取凭证明细 返回总行数=" + accList.size()+"合并后："+newAccList.size());
			accList = newAccList;
			
			BigDecimal debit = new BigDecimal("0");
			BigDecimal lend = new BigDecimal("0");
			for (int i = accList.size() - 1; i >= 0; i--) {
				AccDetailBean accb = accList.get(i);				
				debit =debit.add(accb.getDebitAmount());
				lend = lend.add(accb.getLendAmount());
				//检查科目有没有启用核算
				accb.setAccCode(getAccNumber(accb.getAccCode(), accb.getCompanyCode(), accb.getStockCode(), accb.getDepartmentCode(), accb.getEmployeeID(), accb.getProjectCode(), accb
						.getCurrency(), accb.getSCompanyID(), conn));
				
			}
			if (accList.size() == 0) {
				rs.retCode = ErrorCanst.DEFAULT_SUCCESS;
				return rs;
			}
			if (debit.compareTo(lend) != 0) {
				rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
				rs.retVal = "生成凭证借贷不平衡（借:"+debit+";贷:"+lend+"），请检查凭证模板正确性";
				return rs;
			}

			//插入凭证主表数据
			sql = "insert into tblAccMain (id,CredTypeID,OrderNo,BillDate,RefBillType,RefBillID,RefBillNo,CredYear,CredMonth,Period,"
					+ "createBy,createTime,lastUpdateBy,lastUpdateTime,SCompanyID,workFlowNodeName,workFlowNode,checkPersons,AutoBillMarker, DepartmentCode,EmployeeID,isAuditing) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,0,?,?,?)";
			ps2 = conn.prepareStatement(sql);
			ps2.setString(1, accMainId);
			ps2.setString(2, bean.getCredTypeID());
			ps2.setInt(3, maxOrderNo + 1);
			ps2.setString(4, billDate);
			ps2.setString(5, bean.getTableName());
			ps2.setString(6, "");
			ps2.setString(7, "");
			ps2.setInt(8, periodYear);
			ps2.setInt(9, period);
			ps2.setInt(10, period);
			ps2.setString(11, createBy);
			ps2.setString(12, createTime);
			ps2.setString(13, lastUpdateBy);
			ps2.setString(14, lastUpdateTime);
			ps2.setString(15, SCompanyID);
			//如果凭证启用审核流，则要置workFlowNode为开始结点 

			ps2.setString(16, "notApprove");
			ps2.setString(17, "0");
			ps2.setString(18, ';' + createBy + ';');
			ps2.setString(19, departmentCode);
			ps2.setString(20, userId);
			ps2.setString(21, "start");
			ps2.executeUpdate();

			for (AccDetailBean accb : accList) {
				//插入凭证明细
				sql = "insert into tblAccDetail (id,RefBillID,RefBillType,CompanyCode,DepartmentCode,EmployeeID,AccCode,DebitAmount,Currency,CurrencyRate,"
						+ "DebitCurrencyAmount,LendAmount,LendCurrencyAmount,PeriodYear,PeriodMonth,AccDate,createBy,createTime,lastUpdateBy,lastUpdateTime,"
						+ "SCompanyID,f_ref,RecordComment,ProjectCode) " + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				ps2 = conn.prepareStatement(sql);
				ps2.setString(1, IDGenerater.getId());
				ps2.setString(2, accb.getRefBillID());
				ps2.setString(3, accb.getRefBillType());
				ps2.setString(4, accb.getCompanyCode());
				ps2.setString(5, accb.getDepartmentCode());
				ps2.setString(6, accb.getEmployeeID());
				ps2.setString(7, accb.getAccCode());
				ps2.setBigDecimal(8, accb.getDebitAmount());
				ps2.setString(9, accb.getCurrency());
				ps2.setBigDecimal(10, accb.getCurrencyRate());
				ps2.setBigDecimal(11, accb.getDebitCurrencyAmount());
				ps2.setBigDecimal(12, accb.getLendAmount());
				ps2.setBigDecimal(13, accb.getLendCurrencyAmount());
				ps2.setInt(14, accb.getPeriodYear());
				ps2.setInt(15, accb.getPeriodMonth());
				ps2.setString(16, accb.getAccDate());
				ps2.setString(17, accb.getCreateBy());
				ps2.setString(18, accb.getCreateTime());
				ps2.setString(19, accb.getLastUpdateBy());
				ps2.setString(20, accb.getLastUpdateTime());
				ps2.setString(21, accb.getSCompanyID());
				ps2.setString(22, accb.getAccMainBean().getId());
				ps2.setString(23, accb.getRecordComment());
				ps2.setString(24, accb.getProjectCode());
				ps2.executeUpdate();
			}
			//调proc_insertAccAfterRefOper
			CallableStatement cs = conn.prepareCall("{call proc_insertAccAfterRefOper(?,?,?,?,?,?,?)}");
			//凭证设置
			AccMainSettingBean settingBean = null;
			Result result = new VoucherMgt().queryVoucherSetting(conn);
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				settingBean = (AccMainSettingBean) result.retVal;
			}
			//IsAuditing=0不启用审核
			int isstart = settingBean != null ? settingBean.getIsAuditing() : 0;
			cs.setInt(1, isstart);
			cs.setString(2, accMainId);
			cs.setString(3, billDate);
			cs.setString(4, createBy);
			cs.setString(5, createTime);

			cs.registerOutParameter(6, Types.INTEGER);
			cs.registerOutParameter(7, Types.VARCHAR, 500);
			cs.execute();
			int retCode = cs.getInt(6);
			String retVal = cs.getString(7);
			if (retCode < 0) {
				rs.setRetCode(retCode);
				rs.setRetVal(retVal);
				return rs;
			}
			//更新，凭证标识
			sql = "update " + tableBean.getTableName() + " set CertificateNo=? where id in (" + idstr + ")";
			ps2 = conn.prepareStatement(sql);
			ps2.setString(1, accMainId);
			ps2.execute();

		} catch (Exception e) {
			BaseEnv.log.error("DynDBManager.genCertificate Error:", e);
			rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
			rs.retVal = "生成凭证失败";
		}
		return rs;
	}

	/**
	 * 月结生成凭证
	 * @param userId
	 * @param SCompanyID
	 * @param departmentCode
	 * @param templateNumber
	 * @param ids
	 * @param conn
	 * @param resources
	 * @param locale
	 * @return
	 */
	public Result genSettleCertificate(String userId, CertificateBillBean bean, Connection conn, final MessageResources resources, final Locale locale) {
		
		AccPeriodBean apBean  = (AccPeriodBean)((Hashtable)BaseEnv.sessionSet.get(userId)).get("AccPeriod");
		int periodYear = apBean.getAccYear();
		int periodMonth = apBean.getAccMonth();

		Result rs = new Result();
		
		DBTableInfoBean tableBean = BaseEnv.tableInfos.get(bean.getTableName());
		if (tableBean == null) {
			//取表名不存在
			rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
			rs.retVal = "凭证模板对应表名[" + bean.getTableName() + "]不存在";
			return rs;
		}
		long curTime = System.currentTimeMillis();
		BaseEnv.log.debug(" DynDBManager. genSettleCertificate "+bean.getTempName()+":"+bean.getTempNumber()+" 开始生成月结凭证" );
		try {
			String accMainId = IDGenerater.getId();

			String createBy = userId;
			String createTime = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
			String lastUpdateBy = userId;
			String lastUpdateTime = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
			String SCompanyID= "";
			String departmentCode="";
			//当前的会计期单
			Hashtable ht = (Hashtable) BaseEnv.sessionSet.get(userId);
			int curPeriodYear = Integer.parseInt(ht.get("NowYear").toString());
			int curPeriod = Integer.parseInt(ht.get("NowPeriod").toString());
			int curMonth = Integer.parseInt(ht.get("NowMonth").toString());
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, curPeriodYear);
			cal.set(Calendar.MONTH, curMonth - 1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.roll(Calendar.DAY_OF_MONTH, -1);
			String billDate = BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd);

			int maxOrderNo = 1;//当前期单最大凭证号
			boolean isExistCompanyCode = containsField("CompanyCode", tableBean);

			String sql = "select SCompanyID,departmentCode from tblEmployee where id=?";
			PreparedStatement ps2 = conn.prepareStatement(sql);
			ps2.setString(1, userId);
			ResultSet rss = ps2.executeQuery();
			if (rss.next()) {
				SCompanyID = rss.getString("SCompanyID");
				departmentCode = rss.getString("departmentCode");
			}
			
			//<!--拿到当前期间的最大凭证号-->
			sql = "select isnull(max(OrderNo),0) maxOrderNo from tblAccMain where SCompanyID=? and period=? and CredYear=?";
			ps2 = conn.prepareStatement(sql);
			ps2.setString(1, SCompanyID);
			ps2.setInt(2, curPeriod);
			ps2.setInt(3, curPeriodYear);
			rss = ps2.executeQuery();
			if (rss.next()) {
				maxOrderNo = rss.getInt("maxOrderNo");
			}

			ArrayList<AccDetailBean> accList = new ArrayList(); //记录凭证明细

			//跟据凭证模板，插入凭证明细表
			for (CertificateTemplateBean ctb : bean.getTempList()) {
				//****科目要考虑核算的情况****
				//科目，如果是全数字，说明是科目编号，要加上引号
				if (ctb.getAccCode().matches("[\\d]*")) {
					ctb.setAccCode("'" + ctb.getAccCode() + "'");
				}
				//摘要
				ctb.setComment(GlobalsTool.getTableInfoBean(bean.getTableName()).getDisplay().get("zh_CN") + "月结凭证");
				
				
				//计算表的个数
				ArrayList<String> tableList = new ArrayList();

				if (ctb.getAccCode().indexOf(".") > 0) {//科目字段
					String t = ctb.getAccCode().substring(0, ctb.getAccCode().indexOf("."));
					//因为金额中可能包含 数字，所以这里要判断，如果表名是纯数字的，要去掉
					if (!t.equals(tableBean.getTableName()) && !tableList.contains(t)) {
						tableList.add(t);
					}
				}
				//要去掉sum子查询
				String noSumField = ctb.getFieldName();
				noSumField = noSumField.replaceAll("sum\\(([^\\)]+)\\)", " ");
				noSumField = noSumField.replaceAll("SUM\\(([^\\)]+)\\)", " ");
				//金额字段
				Pattern pattern = Pattern.compile("([\\w]*)\\.", Pattern.CASE_INSENSITIVE);				
				Matcher matcher = pattern.matcher(noSumField);
				while (matcher.find()) {
					String t = matcher.group(1);
					if (!t.matches("\\d") && !t.equals(tableBean.getTableName()) && !tableList.contains(t)) {
						tableList.add(t);
					}
				}

				
				//如果fieldName中包含sum 应该被解释成子查询
				ctb.setFieldName(certSum(ctb.getFieldName(),tableBean.getTableName()));
				ctb.setCurFieldName(certSum(ctb.getCurFieldName(),tableBean.getTableName()));
				
				ctb.setDepartmentCode(ctb.getDepartmentCode()==null || ctb.getDepartmentCode().length()==0?"''":ctb.getDepartmentCode());
				ctb.setEmployeeID(ctb.getEmployeeID()==null || ctb.getEmployeeID().length()==0?"''":ctb.getEmployeeID());
				ctb.setCompanyCode(ctb.getCompanyCode()==null || ctb.getCompanyCode().length()==0?"''":ctb.getCompanyCode());
				ctb.setCurrency(ctb.getCurrency()==null || ctb.getCurrency().length()==0?"''":ctb.getCurrency());
				ctb.setCurrencyRate(ctb.getCurrencyRate()==null || ctb.getCurrencyRate().length()==0?"'1'":ctb.getCurrencyRate());
				ctb.setProjectCode(ctb.getProjectCode()==null || ctb.getProjectCode().length()==0?"''":ctb.getProjectCode());
				ctb.setStockCode(ctb.getStockCode()==null || ctb.getStockCode().length()==0?"''":ctb.getStockCode());
				
				sql = "select CompanyCode,DepartmentCode,EmployeeID,AccCode,ProjectCode,StockCode,Currency,CurrencyRate,sum(DebitAmount) DebitAmount,"
						+ "sum(DebitCurrencyAmount) DebitCurrencyAmount,sum(LendAmount) LendAmount,sum(LendCurrencyAmount) LendCurrencyAmount"
						+ "  from " + 
						"( select " + 
						"isnull("+ctb.getCompanyCode()+ ",'') CompanyCode," + 
						"isnull("+ctb.getDepartmentCode() + ",'') DepartmentCode," + 
						"isnull("+ctb.getEmployeeID() + ",'') EmployeeID," + 
						"isnull("+ctb.getProjectCode() + ",'') ProjectCode, " + 
						"isnull("+ctb.getStockCode() + ",'') StockCode, " + 
						"isnull("+ctb.getCurrency() + ",'') Currency," + 
						ctb.getCurrencyRate() + " CurrencyRate,"+ 
						ctb.getAccCode() + " AccCode,"+ 
						(ctb.getDirc() == 1 ? ctb.getFieldName() : "0") + " DebitAmount," + 						
						(ctb.getDirc() == 1 ? ctb.getCurFieldName() : "0") + " DebitCurrencyAmount," + 
						(ctb.getDirc() == 2 ? ctb.getFieldName() : "0") + " LendAmount,"+ 
						(ctb.getDirc() == 2 ? ctb.getCurFieldName() : "0")+ " LendCurrencyAmount" + 						
						" from   " + tableBean.getTableName();

				//查询表中是否有tblGoodsAccProp
				if (tableList.contains("tblGoodsAccProp")) {
					//存货核算科目
					String goodTable = ctb.getGoodsCode().substring(0, ctb.getGoodsCode().indexOf("."));

					sql += " join " + goodTable + "  on " + tableBean.getTableName() + ".id=" + goodTable + ".f_ref ";
					sql += " join tblGoods on " + ctb.getGoodsCode() + "=tblGoods.classCode ";
					sql += " join tblGoodsAccProp on tblGoods.CostMethod=tblGoodsAccProp.id ";
					tableList.remove(goodTable);
					tableList.remove("tblGoodsAccProp");
				}
				for (String t : tableList) {
					sql += " join " + t + "  on " + tableBean.getTableName() + ".id=" + t + ".f_ref ";
				}

				sql += "  where " + tableBean.getTableName() + ".PeriodYear="+periodYear+" and " + tableBean.getTableName() + ".PeriodMonth ="+periodMonth+" "
						+ " and " + tableBean.getTableName() + ".workFlowNodeName='finish' ) detail "
						+ " group by CompanyCode,DepartmentCode,EmployeeID,AccCode,ProjectCode,StockCode,Currency,CurrencyRate";
				
				BaseEnv.log.debug(" DynDBManager. genSettleCertificate "+bean.getTempName()+":"+bean.getTempNumber()+"取凭证明细数据sql=" + sql);
				ps2 = conn.prepareStatement(sql);
				rss = ps2.executeQuery();

				int digit = GlobalsTool.getFieldDigits("tblAccDetail", "DebitAmount");
				int rowCount = 0;
				while (rss.next()) {
					AccDetailBean accb = new AccDetailBean();
					accb.setRefBillID("");
					accb.setRefBillType(bean.getTableName());
					accb.setCompanyCode(rss.getString("CompanyCode"));
					accb.setDepartmentCode(rss.getString("DepartmentCode"));
					accb.setEmployeeID(rss.getString("EmployeeID"));
					accb.setAccCode(rss.getString("AccCode"));
					accb.setDebitAmount(rss.getBigDecimal("DebitAmount").setScale(digit, BigDecimal.ROUND_HALF_UP));
					accb.setCurrency(rss.getString("Currency"));
					accb.setCurrencyRate(rss.getBigDecimal("CurrencyRate"));
					
					accb.setDebitCurrencyAmount(rss.getBigDecimal("DebitCurrencyAmount").setScale(digit, BigDecimal.ROUND_HALF_UP));
					accb.setLendAmount(rss.getBigDecimal("LendAmount").setScale(digit, BigDecimal.ROUND_HALF_UP));
					accb.setLendCurrencyAmount(rss.getBigDecimal("LendCurrencyAmount").setScale(digit, BigDecimal.ROUND_HALF_UP));
					accb.setPeriodYear(curPeriodYear);
					accb.setPeriodMonth(curMonth);
					accb.setAccDate(billDate);
					accb.setCreateBy(createBy);
					accb.setCreateTime(createTime);
					accb.setLastUpdateBy(lastUpdateBy);
					accb.setLastUpdateTime(lastUpdateTime);
					accb.setSCompanyID(SCompanyID);
					AccMainBean amb = new AccMainBean();
					amb.setId(accMainId);
					accb.setAccMainBean(amb);
					accb.setRecordComment(ctb.getComment());
					accb.setProjectCode(rss.getString("ProjectCode"));
					accb.setStockCode(rss.getString("StockCode"));

					if (accb.getCurrency() == null || accb.getCurrency().length() == 0) {
						accb.setDebitCurrencyAmount(new BigDecimal(0));
						accb.setLendCurrencyAmount(new BigDecimal(0));
					}
					accList.add(accb);
					rowCount ++;
				}
				BaseEnv.log.debug(" DynDBManager. genSettleCertificate "+bean.getTempName()+":"+bean.getTempNumber()+"取凭证明细 返回结果行数=" + rowCount);
			}
			
			ArrayList<AccDetailBean> newAccList = new ArrayList<AccDetailBean>();
			HashMap<String,AccTypeBean> accTypeMap = new HashMap<String,AccTypeBean>();
			for (int i = accList.size() - 1; i >= 0; i--) {
				AccDetailBean accb = accList.get(i);
				//根据核算合并信息
				if (accb.getDebitAmount().doubleValue() != 0 || accb.getLendAmount().doubleValue() != 0) {
					//检查没有启用核算的科目，合并数量
					if(accTypeMap.get(accb.getAccCode())==null){
						//查询科目核算信息
						AccTypeBean typeb =getAccType(accb.getAccCode(),conn);
						if(typeb == null){
							BaseEnv.log.error(" DynDBManager. genSettleCertificate "+bean.getTempName()+":"+bean.getTempNumber()+" 科目代号不存在"+accb.getAccCode());
							rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
							rs.retVal = "凭证模板对应"+bean.getTempName()+":"+bean.getTempNumber()+" 科目代号不存在";
							return rs; 
						}
						if(typeb.getIsDept() != 1){
							accb.setDepartmentCode("");
						}
						if(typeb.getIsPersonal() != 1){
							accb.setEmployeeID("");
						}
						if(typeb.getIsClient() != 1 && typeb.getIsProvider() != 1){
							accb.setCompanyCode("");
						}
						if(typeb.getIsStock() != 1){
							accb.setStockCode("");
						}
						if(typeb.getIsForCur() != 1){
							accb.setCurrency("");
							accb.setCurrencyRate(new BigDecimal("1"));
						}
						if(typeb.getIsProject() != 1){
							accb.setProjectCode("");
						}
						newAccList.add(0,accb);
						accTypeMap.put(accb.getAccCode(), typeb);
					}else{
						AccTypeBean typeb =accTypeMap.get(accb.getAccCode());
						if(typeb.getIsDept() != 1){
							accb.setDepartmentCode("");
						}
						if(typeb.getIsPersonal() != 1){
							accb.setEmployeeID("");
						}
						if(typeb.getIsClient() != 1 && typeb.getIsProvider() != 1){
							accb.setCompanyCode("");
						}
						if(typeb.getIsStock() != 1){
							accb.setStockCode("");
						}
						if(typeb.getIsForCur() != 1){
							accb.setCurrency("");
							accb.setCurrencyRate(new BigDecimal("1"));
						}
						if(typeb.getIsProject() != 1){
							accb.setProjectCode("");
						}
						boolean found = false;
						for(AccDetailBean adb1 :newAccList){
							if(accb.getAccCode().equals(adb1.getAccCode())&&
									accb.getDepartmentCode().equals(adb1.getDepartmentCode())&&
									accb.getEmployeeID().equals(adb1.getEmployeeID())&&
									accb.getCompanyCode().equals(adb1.getCompanyCode())&&
									accb.getStockCode().equals(adb1.getStockCode())&&
									accb.getCurrency().equals(adb1.getCurrency())&&
									accb.getProjectCode().equals(adb1.getProjectCode())){
								if((adb1.getDebitAmount().doubleValue()!=0&&accb.getDebitAmount().doubleValue()!=0)||
										(adb1.getLendAmount().doubleValue()!=0&&accb.getLendAmount().doubleValue()!=0)){
									adb1.setDebitAmount(adb1.getDebitAmount().add(accb.getDebitAmount()));
									adb1.setDebitCurrencyAmount(adb1.getDebitCurrencyAmount().add(accb.getDebitCurrencyAmount()));
									adb1.setLendAmount(adb1.getLendAmount().add(accb.getLendAmount()));
									adb1.setLendCurrencyAmount(adb1.getLendCurrencyAmount().add(accb.getLendCurrencyAmount()));
									found = true;
								}
							}
						}
						if(!found){
							newAccList.add(0,accb);
						}
					}
				}
			}		
			BaseEnv.log.debug(" DynDBManager. genSettleCertificate "+bean.getTempName()+":"+bean.getTempNumber()+"取凭证明细 返回总行数=" + accList.size()+"合并后："+newAccList.size());
			accList = newAccList;
			
			BigDecimal debit = new BigDecimal("0");
			BigDecimal lend = new BigDecimal("0");
			for (int i = accList.size() - 1; i >= 0; i--) {
				AccDetailBean accb = accList.get(i);				
				debit =debit.add(accb.getDebitAmount());
				lend = lend.add(accb.getLendAmount());
				//检查科目有没有启用核算
				accb.setAccCode(getAccNumber(accb.getAccCode(), accb.getCompanyCode(), accb.getStockCode(), accb.getDepartmentCode(), accb.getEmployeeID(), accb.getProjectCode(), accb
						.getCurrency(), accb.getSCompanyID(), conn));
				
			}
			if (accList.size() == 0) {
				rs.retCode = ErrorCanst.DEFAULT_SUCCESS;
				return rs;
			}
			if (debit.compareTo(lend) != 0) {
				rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
				rs.retVal = "生成凭证借贷不平衡，请检查凭证模板正确性("+bean.getTempName()+")";
				return rs;
			}

			//插入凭证主表数据
			sql = "insert into tblAccMain (id,CredTypeID,OrderNo,BillDate,RefBillType,RefBillID,RefBillNo,CredYear,CredMonth,Period,"
					+ "createBy,createTime,lastUpdateBy,lastUpdateTime,SCompanyID,workFlowNodeName,workFlowNode,checkPersons,AutoBillMarker, DepartmentCode,EmployeeID,isAuditing) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,?,?,?)";
			ps2 = conn.prepareStatement(sql);
			ps2.setString(1, accMainId);
			ps2.setString(2, bean.getCredTypeID());
			ps2.setInt(3, maxOrderNo + 1);
			ps2.setString(4, billDate);
			ps2.setString(5, bean.getTableName());
			ps2.setString(6, "settleAcc");
			ps2.setString(7, "");
			ps2.setInt(8, curPeriodYear);
			ps2.setInt(9, curMonth);
			ps2.setInt(10, curPeriod);
			ps2.setString(11, createBy);
			ps2.setString(12, createTime);
			ps2.setString(13, lastUpdateBy);
			ps2.setString(14, lastUpdateTime);
			ps2.setString(15, SCompanyID);
			//如果凭证启用审核流，则要置workFlowNode为开始结点 

			ps2.setString(16, "notApprove");
			ps2.setString(17, "0");
			ps2.setString(18, ';' + createBy + ';');
			ps2.setString(19, departmentCode);
			ps2.setString(20, userId);
			ps2.setString(21, "start");
			ps2.executeUpdate();

			for (AccDetailBean accb : accList) {
				//插入凭证明细
				sql = "insert into tblAccDetail (id,RefBillID,RefBillType,CompanyCode,DepartmentCode,EmployeeID,AccCode,DebitAmount,Currency,CurrencyRate,"
						+ "DebitCurrencyAmount,LendAmount,LendCurrencyAmount,PeriodYear,PeriodMonth,AccDate,createBy,createTime,lastUpdateBy,lastUpdateTime,"
						+ "SCompanyID,f_ref,RecordComment,ProjectCode) " + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				ps2 = conn.prepareStatement(sql);
				ps2.setString(1, IDGenerater.getId());
				ps2.setString(2, "settleAcc");
				ps2.setString(3, accb.getRefBillType());
				ps2.setString(4, accb.getCompanyCode());
				ps2.setString(5, accb.getDepartmentCode());
				ps2.setString(6, accb.getEmployeeID());
				ps2.setString(7, accb.getAccCode());
				ps2.setBigDecimal(8, accb.getDebitAmount());
				ps2.setString(9, accb.getCurrency());
				ps2.setBigDecimal(10, accb.getCurrencyRate());
				ps2.setBigDecimal(11, accb.getDebitCurrencyAmount());
				ps2.setBigDecimal(12, accb.getLendAmount());
				ps2.setBigDecimal(13, accb.getLendCurrencyAmount());
				ps2.setInt(14, accb.getPeriodYear());
				ps2.setInt(15, accb.getPeriodMonth());
				ps2.setString(16, accb.getAccDate());
				ps2.setString(17, accb.getCreateBy());
				ps2.setString(18, accb.getCreateTime());
				ps2.setString(19, accb.getLastUpdateBy());
				ps2.setString(20, accb.getLastUpdateTime());
				ps2.setString(21, accb.getSCompanyID());
				ps2.setString(22, accb.getAccMainBean().getId());
				ps2.setString(23, accb.getRecordComment());
				ps2.setString(24, accb.getProjectCode());
				ps2.executeUpdate();
			}
			//调proc_insertAccAfterRefOper
			CallableStatement cs = conn.prepareCall("{call proc_insertAccAfterRefOper(?,?,?,?,?,?,?)}");
			//凭证设置
			AccMainSettingBean settingBean = null;
			Result result = new VoucherMgt().queryVoucherSetting(conn);
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				settingBean = (AccMainSettingBean) result.retVal;
			}
			//IsAuditing=0不启用审核
			int isstart = settingBean != null ? settingBean.getIsAuditing() : 0;
			cs.setInt(1, isstart);
			cs.setString(2, accMainId);
			cs.setString(3, billDate);
			cs.setString(4, createBy);
			cs.setString(5, createTime);

			cs.registerOutParameter(6, Types.INTEGER);
			cs.registerOutParameter(7, Types.VARCHAR, 500);
			cs.execute();
			int retCode = cs.getInt(6);
			String retVal = cs.getString(7);
			if (retCode < 0) {
				rs.setRetCode(retCode);
				rs.setRetVal(retVal);
				return rs;
			}
			

		} catch (Exception e) {
			BaseEnv.log.error("DynDBManager.genSettleCertificate Error:", e);
			rs.retCode = ErrorCanst.RET_DEFINE_SENTENCE_ERROR;
			rs.retVal = "生成凭证失败";
		}
		BaseEnv.log.debug(" DynDBManager. genSettleCertificate "+bean.getTempName()+":"+bean.getTempNumber()+" 月结凭证完成，耗时"+(System.currentTimeMillis() - curTime) );
		return rs;
	}
	/**
	 * 取凭证编号
	 * @param accCode
	 * @param companyCode
	 * @param stockCode
	 * @param departmentCode
	 * @param employeeID
	 * @param projectCode
	 * @param CurrencyID
	 * @param sunCompany
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	private AccTypeBean getAccType(final String accCode,  Connection conn) throws Exception {
		PreparedStatement cs = null;
		try {
			String sql = "	SELECT isDept,IsPersonal,IsClient,IsProvider,IsStock ,IsForCur,IsProject FROM tblAccTypeInfo WHERE AccNumber=? ";
		
			cs = conn.prepareStatement(sql);
			cs.setString(1, accCode);
			ResultSet rset = cs.executeQuery();
			
			AccTypeBean typeBean = new AccTypeBean();
			if(rset.next()){
				typeBean.setIsDept(rset.getByte("isDept"));
				typeBean.setIsPersonal(rset.getByte("IsPersonal"));
				typeBean.setIsClient(rset.getByte("IsClient"));
				typeBean.setIsProvider(rset.getByte("IsProvider"));
				typeBean.setIsStock(rset.getByte("IsStock"));
				typeBean.setIsForCur(rset.getByte("IsForCur"));
				typeBean.setIsProject(rset.getByte("IsProject"));
				return typeBean;
			}
			return null;
		} catch (Exception ex) {			
			throw ex;
		}
	}
	/**
	 * 取凭证编号
	 * @param accCode
	 * @param companyCode
	 * @param stockCode
	 * @param departmentCode
	 * @param employeeID
	 * @param projectCode
	 * @param CurrencyID
	 * @param sunCompany
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	private String getAccNumber(final String accCode, final String companyCode, final String stockCode, final String departmentCode, final String employeeID, final String projectCode,
			final String CurrencyID, final String sunCompany, Connection conn) throws Exception {
		CallableStatement cs = null;
		try {
			cs = conn.prepareCall("{call getCAccNumer(?,?,?,?,?,?,?,?,?,?)}");

			cs.setString(1, accCode);
			cs.setString(2, companyCode);
			cs.setString(3, stockCode);
			cs.setString(4, departmentCode);
			cs.setString(5, employeeID);
			cs.setString(6, projectCode);
			cs.setString(7, CurrencyID);
			cs.setString(8, sunCompany);

			cs.registerOutParameter(9, Types.INTEGER);
			cs.registerOutParameter(10, Types.VARCHAR, 500);
			cs.execute();

			int retCode = cs.getInt(9);
			String retVal = cs.getString(10);

			if (BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT) {
				SQLWarning warn = cs.getWarnings();
				while (warn != null) {
					if (warn.getMessage() != null && warn.getMessage().indexOf("正在直接执行 SQL；无游标") == -1) {
						BaseEnv.log.debug("存储过程内部信息： " + warn.getMessage());
					}
					warn = warn.getNextWarning();
				}
			}
			if (retCode < 0) {
				throw new Exception("存储过程getCAccNumer返回错误:"+retVal);
			}
			return retVal;
		} catch (Exception ex) {
			try {
				if (BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT) {
					SQLWarning warn = cs.getWarnings();
					while (warn != null) {
						if (warn.getMessage() != null && warn.getMessage().indexOf("正在直接执行 SQL；无游标") == -1) {
							BaseEnv.log.debug("存储过程内部信息： " + warn.getMessage());
						}
						warn = warn.getNextWarning();
					}
				}
			} catch (SQLException ex2) {
				ex2.printStackTrace();
			}
			BaseEnv.log.debug("DynDBManager.getAccNumber {call getCAccNumer(?,?,?,?,?,?,?,?,?,?)}");
			BaseEnv.log.debug("DynDBManager.getAccNumber accCode="+accCode);
			BaseEnv.log.debug("DynDBManager.getAccNumber companyCode="+companyCode);
			BaseEnv.log.debug("DynDBManager.getAccNumber stockCode="+stockCode);
			BaseEnv.log.debug("DynDBManager.getAccNumber departmentCode="+departmentCode);
			BaseEnv.log.debug("DynDBManager.getAccNumber employeeID="+employeeID);
			BaseEnv.log.debug("DynDBManager.getAccNumber projectCode="+projectCode);
			BaseEnv.log.debug("DynDBManager.getAccNumber CurrencyID="+CurrencyID);
			BaseEnv.log.debug("DynDBManager.getAccNumber sunCompany="+sunCompany);
			throw ex;
		}

	}

	/**
	 * 单据推送
	 * @param tableName
	 * @param destTableName
	 * @param id
	 * @param userId
	 * @param detId
	 * @return
	 */
	public Result billPush(final String tableName, final String destTableName, final String id, final String userId, final LoginBean loginBean, 
			final String detId, final boolean isContinue,HttpServletRequest request) {
		final Result res = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							DBTableInfoBean mainBean = GlobalsTool.getTableInfoBean(tableName);

							//查询主表的对应关系
							String sql = "select mostlyTableField,childTableField from tblTableMapped where mostlyTable=? and childTable=?";
							PreparedStatement st = conn.prepareStatement(sql);
							st.setString(1, tableName);
							st.setString(2, destTableName);
							ResultSet rss = st.executeQuery();
							ArrayList<String[]> mapList = new ArrayList();
							String msql = "";
							String destIdField = ""; //用于记录主表的id字段对应的目标表的id字段，此方法需要检查该单是否已经进行过推单
							while (rss.next()) {
								String mf = rss.getString("mostlyTableField");
								String cf = rss.getString("childTableField");
								mapList.add(new String[] { mf, cf });
								msql += "," + mf;
								if ("id".equals(mf)) {
									destIdField = cf;
								}
							}
							msql = msql.substring(1);
//							if (destIdField.length() == 0) {
//								res.retCode = ErrorCanst.DEFAULT_FAILURE;
//								res.retVal = "映射关系中请指定目标表的参考字段并对应原表的ID，方便推单后最终来源";
//								return;
//							}
							if (!isContinue && destIdField.length() > 0) {
								//第一次执行时，要检查系统是否进行过推单，如果进行过，要提示是否继续
								sql = "select count(0) from " + destTableName + " where " + destIdField + "=? ";
								st = conn.prepareStatement(sql);
								st.setString(1, id);
								rss = st.executeQuery();
								int count = 0;
								if (rss.next()) {
									count = rss.getInt(1);
								}
								if (count > 0) {
									res.retCode = ErrorCanst.USER_STOP;
									res.retVal = "止单已进行过推送，是否继续";
									return;
								}
							}

							//查主表数据
							sql = "select " + msql + " from " + tableName + " where id=?";
							st = conn.prepareStatement(sql);
							st.setString(1, id);
							rss = st.executeQuery();
							HashMap saveValues = new HashMap();
							if (rss.next()) {
								for (String[] mapS : mapList) {
									DBFieldInfoBean dfb = GlobalsTool.getFieldBean(tableName, mapS[0]); //原表的bean 用以确认原表字段类型
									if (dfb.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
										saveValues.put(mapS[1], rss.getDouble(mapS[0]));
									} else {
										saveValues.put(mapS[1], rss.getString(mapS[0]));
									}
								}
							} else {
								res.retCode = ErrorCanst.DEFAULT_FAILURE;
								res.retVal = "主表数据不存在，或已经被删除";
								return;
							}

							ArrayList<DBTableInfoBean> mtbs = DDLOperation.getChildTables(tableName, BaseEnv.tableInfos);
							ArrayList<DBTableInfoBean> ctbs = DDLOperation.getChildTables(destTableName, BaseEnv.tableInfos);
							for (DBTableInfoBean mtb : mtbs) {
								DBTableInfoBean ctb = null;
								//查询明细表的对应关系
								sql = " select distinct childTable from tblTableMapped where mostlyTable=?";
								st = conn.prepareStatement(sql);
								st.setString(1, mtb.getTableName());
								rss = st.executeQuery();
								while (rss.next()) {
									String ctname = rss.getString(1);
									for (DBTableInfoBean ct : ctbs) {
										if (ct.getTableName().equals(ctname)) {
											//找到对应关系表
											ctb = ct;
											break;
										}
									}
								}
								if (ctb != null) {
									//有对应关系的，执行对应数据的插入
									sql = "select mostlyTableField,childTableField from tblTableMapped where mostlyTable=? and childTable=?";
									if (detId != null && detId.indexOf(ctb.getTableName() + ":") > 0) {
										//有限定明细表的范围
										int pos = detId.indexOf(ctb.getTableName() + ":") + (ctb.getTableName() + ":").length();
										int epos = detId.indexOf(detId.indexOf(";"), pos);
										if (epos == -1) {
											epos = detId.length();
										}
										String[] dIds = detId.substring(pos, epos).split(",");
										String detCondition = "";
										for (String did : dIds) {
											detCondition += ",'" + did + "'";
										}
										detCondition = detCondition.substring(1);
										sql += " and id in(" + detCondition + ")";
									}
									st = conn.prepareStatement(sql);
									st.setString(1, mtb.getTableName());
									st.setString(2, ctb.getTableName());

									rss = st.executeQuery();
									ArrayList<String[]> cmapList = new ArrayList();
									msql = "";
									while (rss.next()) {
										String mf = rss.getString("mostlyTableField");
										String cf = rss.getString("childTableField");
										cmapList.add(new String[] { mf, cf });
										msql += "," + mf;
									}
									msql = msql.substring(1);
									//查明表表数据
									//明细表是否有detOrderNo
									String orderBy = "id ";
									for(DBFieldInfoBean dfb :mtb.getFieldInfos()){
										if(dfb.getFieldName().equals("detOrderNo")){
											orderBy = "detOrderNo ";
										}
									}
									
									sql = "select " + msql + " from " + mtb.getTableName() + " where f_ref=? order by "+orderBy;
									st = conn.prepareStatement(sql);
									st.setString(1, id);
									rss = st.executeQuery();
									ArrayList<HashMap> vsqlList = new ArrayList();
									saveValues.put("TABLENAME_" + ctb.getTableName(), vsqlList);
									while (rss.next()) {
										String vsql = "";
										HashMap cmap = new HashMap();
										for (String[] mapS : cmapList) {
											DBFieldInfoBean dfb = GlobalsTool.getFieldBean(mtb.getTableName(), mapS[0]);//原表字段bean用以确定字段类型
											if (dfb.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE) {
												cmap.put(mapS[1], rss.getDouble(mapS[0]));
											} else {
												cmap.put(mapS[1], rss.getString(mapS[0]));
											}
										}
										cmap.put("id", IDGenerater.getId());
										vsqlList.add(cmap);
									}
								}
							}
							res.retVal = saveValues;
						} catch (Exception ex) {
							res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("DynDBManager.billPush Error", ex);
							res.retVal = ex.getMessage();
						}
					}
				});
				return res.getRetCode();
			}
		});
		res.setRetCode(retCode);
		if (res.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return res;
		}
		
		HashMap saveValues = (HashMap) res.retVal;
		saveValues.remove("id"); //删除id 避免有些人做对应关系中把源表的id对应到目标表的id
		saveValues.put("workFlowNodeName", "draft") ;
		String createTime = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);

		DBTableInfoBean destBean = GlobalsTool.getTableInfoBean(destTableName);
		

		
		//设置默认值
		UserFunctionMgt userMgt = new UserFunctionMgt();
		DynDBManager mgt = new DynDBManager();
		try {							
			userMgt.setDefault(destBean, saveValues, loginBean.getId());
			//明细表设置默认值
			ArrayList<DBTableInfoBean> ct = DDLOperation.getChildTables(destBean.getTableName(), BaseEnv.tableInfos);
			for (int j = 0; ct != null && j < ct.size(); j++) {
				DBTableInfoBean cbean = ct.get(j);
				ArrayList clist = (ArrayList) saveValues.get("TABLENAME_" + cbean.getTableName());
				for (int k = 0; clist != null && k < clist.size(); k++) {
					HashMap cmap = (HashMap) clist.get(k);
					userMgt.setDefault(cbean, cmap, loginBean.getId());
				}
			}
		} catch (Exception e) {
			res.retCode = ErrorCanst.DEFAULT_FAILURE;
			res.retVal = "设置默认值错误";
			return res;
		}	
		for (DBFieldInfoBean field : destBean.getFieldInfos()) {
			//检查表有没有employeeId字段
			if (field.getFieldName().equalsIgnoreCase("EmployeeId")) {
				saveValues.put(field.getFieldName(), userId);
			} else if (field.getFieldName().equalsIgnoreCase("BillDate")) {
				saveValues.put(field.getFieldName(), BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
			}else if (field.getFieldName().equalsIgnoreCase("createBy")) {
				saveValues.put(field.getFieldName(), userId);
			}else if (field.getFieldName().equalsIgnoreCase("departmentCode")) {
				OnlineUser onlineUser = (OnlineUser) OnlineUserInfo.getUser(userId);
				saveValues.put(field.getFieldName(), onlineUser==null?loginBean.getDepartCode():onlineUser.getDeptId());
			}
		}

		
		/********************************************
		 执行相关接口  saveValues 为保存数据的HashMap 传入此参数至相应接口完成数据插入
		 ********************************************/
		Result rs;
		try {
			// 获取路径
			String path = request.getSession().getServletContext().getRealPath("DoSysModule.sql");
			Locale locale = GlobalsTool.getLocale(request);
			String addMessage = GlobalsTool.getMessage(locale, "common.lb.add");
			
			Object o = request.getSession().getServletContext().getAttribute(
					org.apache.struts.Globals.MESSAGES_KEY);
			MessageResources resources = null;
			if (o instanceof MessageResources) {
				resources = (MessageResources) o;
			}
			Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
			rs = userMgt.add(destBean, saveValues, loginBean, "", BaseEnv.tableInfos, path, "", locale, addMessage, resources, props, null, "saveDraft");
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				rs.retVal= saveValues.get("id");
			}
			return rs;
		} catch (Exception e) {
			return res;
		}
	}
	/**
	 * 取部门管辖的修改，删除权限，职员管辖的修改，删除权限
	 * @param scopeRightUpdate 修改权限
	 * @param scopeRightDel 删除权限
	 * @param scopeRightAll 所有应用于所有模块的公共权限
	 * @return
	 */
	public String[] getDeptRight(ArrayList scopeRightUpdate,ArrayList scopeRightDel,ArrayList scopeRightAll){
		String updateOtherRight = "";//查看他人单据权限
		String updateDeptRight = "";//部门管辖权限
		for (int i = 0; i < scopeRightUpdate.size(); i++) {
			LoginScopeBean lsb = (LoginScopeBean) scopeRightUpdate.get(i);
			if ("1".equals(lsb.getFlag())) {
				updateOtherRight += lsb.getScopeValue();
			}
			if ("5".equals(lsb.getFlag())) {
				updateDeptRight += lsb.getScopeValue();
			}
		}
		// 中间有公共的修改权限
		for (int i = 0; i < scopeRightAll.size(); i++) {
			LoginScopeBean lsb = (LoginScopeBean) scopeRightAll.get(i);
			if ("1".equals(lsb.getFlag()) && "1".equals(lsb.getIsAllModules()) && 1 == lsb.getRightUpdate()) {
				updateOtherRight += lsb.getScopeValue();
			}
			if ("5".equals(lsb.getFlag()) && "1".equals(lsb.getIsAllModules()) && 1 == lsb.getRightUpdate()) {
				if ("ALL".equals(lsb.getScopeValue())) {
					updateDeptRight = "ALL";
					break;
				} else {
					updateDeptRight += lsb.getScopeValue();
				}
			}
		}

		// 整理对哪些人,部门创建的单据有删除权限
		String delOtherRight = "";
		String delDeptRight = "";
		for (int i = 0; i < scopeRightDel.size(); i++) {
			LoginScopeBean lsb = (LoginScopeBean) scopeRightDel.get(i);
			if ("1".equals(lsb.getFlag())) {
				delOtherRight += lsb.getScopeValue();
			}
			if ("5".equals(lsb.getFlag())) {
				delDeptRight += lsb.getScopeValue();
			}
		}
		// 中间有公共的修改权限
		for (int i = 0; i < scopeRightAll.size(); i++) {
			LoginScopeBean lsb = (LoginScopeBean) scopeRightAll.get(i);
			if ("1".equals(lsb.getFlag()) && "1".equals(lsb.getIsAllModules()) && 1 == lsb.getRightDelete()) {
				delOtherRight += lsb.getScopeValue();
			}
			if ("5".equals(lsb.getFlag()) && "1".equals(lsb.getIsAllModules()) && 1 == lsb.getRightDelete()) {
				if ("ALL".equals(lsb.getScopeValue())) {
					delDeptRight = "ALL";
					break;
				} else {
					delDeptRight += lsb.getScopeValue();
				}
			}
		}
		return new String[]{updateOtherRight,updateDeptRight,delOtherRight,delDeptRight};
	}
	
	/**
	 * 判断每一条记录是否有修改权限
	 * @param rowDept
	 * @param updateDeptRight
	 * @param updateOtherRight
	 * @param loginId
	 * @param createBy
	 * @param employeeId
	 * @param checkPersons
	 * @param consignMap
	 * @param workFlowNode
	 * @param workFlowNodeName
	 * @param workFlowDesign
	 * @return
	 */
	public boolean getUpdateRight(String rowDept,String updateDeptRight,String updateOtherRight,String loginId,String createBy,
			String employeeId,String checkPersons,HashMap<String, String> consignMap,String workFlowNode,String workFlowNodeName,
			OAWorkFlowTemplate workFlowDesign){
		boolean upFlag = false;
		for (int j = 1; rowDept != null && j < rowDept.length() / 5; j++) {
			if ("ALL".equals(updateDeptRight)
					|| updateDeptRight.indexOf(rowDept.substring(0, rowDept.length() - j * 5) + ";") >= 0) {
				upFlag = true;
			}
		}

		// 是自己创建的的单据,13
		// 经手人为自己的，或是系统管理员，或是授权的可以修改其它人,部门创建的单据,或者是有审核权限的人
		// 都有修改权限
		boolean flag = upFlag || loginId.equals(createBy) || loginId.equals(employeeId) || loginId.equals("1")
				|| updateOtherRight.indexOf(createBy + ";") >= 0 || updateOtherRight.indexOf(employeeId + ";") >= 0 
				|| "ALL".equals(updateDeptRight)
				|| ((";" + updateDeptRight).indexOf(";" + rowDept + ";") >= 0);
		//查当前行的checkPersons人中是不是包括委托给我的人，如果有，则把自己加入checkPersons中		
		if (checkPersons != null && consignMap.size() > 0 && !"-1".equals(workFlowNode)) {
			for (String person : checkPersons.split(";")) {
				if (consignMap.get(person) != null) {
					checkPersons =checkPersons + loginId + ";";
				}
			}
		}
		/**
		 * 1、不起用审核按权限
		 * 2、启用审核流，按是否包含自己审核
		 * 3、开始结点的，按权限
		 * 4、草稿按权限
		 * 
		 */
		if (((workFlowDesign == null || (workFlowDesign != null && workFlowDesign.getTemplateStatus() == 0)) && flag)
				|| (workFlowDesign != null && workFlowDesign.getTemplateStatus() == 1 && workFlowNodeName != null && 
						!workFlowNodeName.equals("finish")
						&& checkPersons != null && checkPersons.indexOf(";" + loginId + ";") >= 0)
				|| ((workFlowNodeName == null || "0".equals(workFlowNode)) && flag)
				|| ("draft".equals(workFlowNodeName) && flag)) {
			return true;
		} else {
			return false;
		}
		
		
	}
	/**
	 * 判断记录的删除权限
	 * @param rowDept
	 * @param delDeptRight
	 * @param delOtherRight
	 * @param loginId
	 * @param createBy
	 * @param employeeId
	 * @return
	 */
	public boolean getDeleteRight(String rowDept,String delDeptRight,String delOtherRight,String loginId,String createBy,String employeeId,String workFlowNode,String workFlowNodeName,
			OAWorkFlowTemplate workFlowDesign){
		boolean delFlag=false;
		for (int j = 1; rowDept != null && j < rowDept.length() / 5; j++) {
			if (delDeptRight.equals("ALL") || delDeptRight.indexOf(rowDept.substring(0, rowDept.length() - j * 5) + ";") >= 0) {
				delFlag = true;
			}
		}
		boolean flag =(delFlag || loginId.equals(createBy) || loginId.equals(employeeId) || loginId.equals("1") || 
				delOtherRight.indexOf(createBy + ";") >= 0 || delOtherRight.indexOf(employeeId + ";") >= 0
				|| delDeptRight.equals("ALL") || delDeptRight.indexOf(rowDept + ";") >= 0)  ;
		/**
		 * 1、不起用审核按权限
		 * 2、启用审核流，按是否包含自己审核
		 * 3、开始结点的，按权限
		 * 4、草稿按权限
		 * 
		 */
		if (((workFlowDesign == null || (workFlowDesign != null && workFlowDesign.getTemplateStatus() == 0)) && flag)
				|| ((workFlowNodeName == null || "0".equals(workFlowNode)) && flag)
				|| ("draft".equals(workFlowNodeName) && flag)) {
			return true;
		} else {
			return false;
		}
		
	}

    /**
     * 取字段的显示名
     * @param fieldName String tableName.fieldName
     * @return String
     */
    public static String getFieldDisplay(Hashtable allTables, String fieldName,String locale) {
        if (fieldName == null || fieldName.trim().length() == 0) {
            return null;
        }
        String table = fieldName.substring(0, fieldName.indexOf("."));
        String field = fieldName.substring(fieldName.indexOf(".") + 1);

        DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(table);
        if (tableInfo == null) {
            return null;
        }
        for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
            DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.
                                        getFieldInfos().get(i);
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
	public static String getDefSQLMsg(String locale, String o) {
		String msg = "";
		String[] os = (String[]) o.split(",");
		if (os.length == 1) {
			String key = os[0].replace("@RepComma:", ",");
			msg = GlobalsTool.getMessage(locale, key);
			if (msg == null || msg.length() == 0) {
				msg = key;
			}
		} else if (os.length == 2) {
			msg = GlobalsTool.getMessage(locale, os[0].replace("@RepComma:", ","), os[1]
					.replace("@RepComma:", ","));
		} else if (os.length == 3) {
			msg = GlobalsTool.getMessage(locale, os[0].replace("@RepComma:", ","), os[1]
					.replace("@RepComma:", ","), os[2].replace("@RepComma:",
					","));
		} else if (os.length >= 4) {
			msg = GlobalsTool.getMessage(locale, os[0].replace("@RepComma:", ","), os[1]
					.replace("@RepComma:", ","), os[2].replace("@RepComma:",
					","), os[3].replace("@RepComma:", ","));
		}
		if(msg==null){
			msg = o;
		}
		msg= msg.replaceAll("\\\\'", "'");
		return msg;
	}
	public static SaveErrorObj saveError(Result rs, String locale,String saveType) {
		if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_ERROR) {
			// 自定义sql语句定制返回结果
			String[] str = (String[]) rs.getRetVal();
			if (str != null) {
				String msg = getDefSQLMsg(locale, str[0]);
				if (str[1].length() > 0) {
					return new SaveErrorObj(msg,str[1]);
				} else {
					return new SaveErrorObj(msg);
				}
			} else {
				return new SaveErrorObj(new ErrorMessage().toString(rs.retCode, locale));
			}
		} else if (rs.getRetCode() == ErrorCanst.PROC_NEGATIVE_ERROR) {
			//存储过程返回负库存错误
			String errorMessage = rs.getRetVal().toString();
			errorMessage = GlobalsTool.revertTextCode2(errorMessage);
			String[] str = errorMessage.split(";");
			String msg = GlobalsTool.getMessage(locale, "common.error.negative2", errorMessage);
			return new SaveErrorObj(msg);
		} else if (rs.getRetCode() == ErrorCanst.MULTI_VALUE_ERROR) {
			//值重复
			Object o = rs.getRetVal();
			if (o != null) {
				String os = o.toString();
				String vl = "";
				if (os.indexOf(":") > -1) {
					vl = os.substring(os.indexOf(":") + 1);
					os = os.substring(0, os.indexOf(":"));
				}
				String msg =getFieldDisplay(BaseEnv.tableInfos, os, locale) + " " + GlobalsTool.getMessage(locale, "common.msg.MULTI_VALUE_ERROR") + vl;
				return new SaveErrorObj(msg == null ? o.toString() + GlobalsTool.getMessage(locale, "sendBill.lb.SelectNull") : msg);
			} else {
				return new SaveErrorObj(new ErrorMessage().toString(rs.retCode, locale));
			}
		} else if (rs.getRetCode() == ErrorCanst.RET_BILL_HASCERTIFICATE) {
			return new SaveErrorObj(GlobalsTool.getMessage(locale, "common.hasCertificate.error", rs.retVal.toString()));
		} else if (rs.getRetCode() == ErrorCanst.WORK_FLOW_NO_NEXT_NODE) {
			return new SaveErrorObj("找不到下一个工作流审核结点，请检查结点或条件是否正确！");
		} else if (rs.getRetCode() == ErrorCanst.BILL_ADD_WORK_FLOW_ERROR) {
			return new SaveErrorObj(GlobalsTool.getMessage(locale, "com.add.workfow.error"));
		} else if(rs.retCode == ErrorCanst.BILL_UPDATE_WORK_FLOW_ERROR) {
			return new SaveErrorObj(GlobalsTool.getMessage(locale, "com.update.workflow.error"));
		} else if (rs.getRetCode() == 2601) {
			return new SaveErrorObj(GlobalsTool.getMessage(locale, "common.error.laterExec"));
		} else if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SENTENCE_ERROR) {
			Object o = rs.getRetVal();
			if (o != null) {
				return new SaveErrorObj(o.toString());
			} else {
				return new SaveErrorObj(new ErrorMessage().toString(rs.retCode, locale));
			}
		} else if (rs.getRetCode() == ErrorCanst.QUORE_INTERNET_ORDER_FAILURE) {
			return new SaveErrorObj("销售出库引用网上订单 更新网上订单出错");
		} else if (rs.getRetCode() == ErrorCanst.RET_FIELD_VALIDATOR_ERROR) {
			//字段校验错误
			return new SaveErrorObj(new ErrorMessage().toString(rs.retCode, locale) + " " + rs.retVal);
		} else if (rs.getRetCode() == ErrorCanst.EXCELBILL_FIELDVALUE_NOTEXIST) {
			if ("quoteDraft".equals(saveType)) {
				return new SaveErrorObj("草稿不存在，或已过帐");
			} else {
				return new SaveErrorObj("数据不存在，或已删除");
			}
		}else if(rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_ALERT){
        	Object[] str = (Object[]) rs.getRetVal();
        	String msg = getDefSQLMsg(locale, (String)str[0]);
        	return new SaveErrorObj(msg);
        }else if(rs.getRetCode() == ErrorCanst.RET_FIELD_IS_NULL){
        	return new SaveErrorObj(rs.getRetVal()+"不能为空!");
        } else {
			if (rs.retVal != null && !rs.retVal.toString().equals("")) {
				return new SaveErrorObj(rs.retVal.toString());
			} else {
				//添加失败
				return new SaveErrorObj(new ErrorMessage().toString(rs.retCode, locale));
			}
		}

	}
	
	 /**
	 * 检查会计期间是否存在
	 * @param accYear
	 * @param accPeriod
	 * @return
	 */
	public Result periodIsExist(final int accYear, final int accPeriod) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Statement stmt = connection.createStatement();
						String sql = "select count(*) from tblAccperiod where AccYear=" + accYear + " and AccMonth=" + accPeriod + " and AccPeriod=" + accPeriod;
						ResultSet rss = stmt.executeQuery(sql);
						if (rss.next()) {
							rs.setRetVal(rss.getInt(1));
						}
					}
				});

				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	/**
	 * 添加会计期间
	 * @param periodBean
	 * @return
	 */
	public Result addNextPriodArr(List<AccPeriodBean> list) {
		Result rs = new Result();
		try {
			for (AccPeriodBean bean : list) {
				this.addBean(bean);
			}
		} catch (Exception e) {
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			return rs;
		}
		return rs;
	}
	   public List<AccPeriodBean> getNewAccPeriodBean(int billYear,int billMonth,String loginId,String sCompany,int periodYear,int periodMonth){
	 	   Result rs=null;
	 	   List<AccPeriodBean> list=new ArrayList<AccPeriodBean>();
	 	   SysAccMgt accMgt=new SysAccMgt();
	 	   String[] daysInMonth={"31","30","31","30","31","30","31","31","30","31","30","31"};
	 	   if(periodYear==-1){periodYear=billYear;}
	 	   int yearC=billYear-periodYear;
	 	   if(yearC==0){//本年跨月做单
	 		   rs=accMgt.getMaxPeriodOfCurrYear(periodYear);
	 		   int curMaxPeriod=Integer.parseInt(rs.getRetVal().toString());
	 		   for(int i=curMaxPeriod+1;i<=12;i++){
	 			   AccPeriodBean periodBean=this.getNewAccPeriodBean(billYear, i, loginId, sCompany);
	 		 	   list.add(periodBean);
	 		   }
	 	   }else{//跨年做单
	 		   if(periodMonth!=12){
	 			   rs=accMgt.getMaxPeriodOfCurrYear(periodYear);
	 			   int curMaxPeriod=Integer.parseInt(rs.getRetVal().toString());
	 			   for(int i=curMaxPeriod+1;i<=12;i++){
	 				   AccPeriodBean periodBean=this.getNewAccPeriodBean(periodYear, i, loginId, sCompany);
	 			 	   list.add(periodBean);
	 			   }
	 		   }
	 		   for(int i=periodYear+1;i<billYear;i++){
	 			   rs=accMgt.getMaxPeriodOfCurrYear(i);
	 			   int curMaxPeriod=Integer.parseInt(rs.getRetVal().toString());
	 			   for(int j=curMaxPeriod+1;j<=12;j++){
	 				   AccPeriodBean periodBean=this.getNewAccPeriodBean(i, j, loginId, sCompany);
	 			 	   list.add(periodBean);
	 			   }
	 		   }
	 		   rs=accMgt.getMaxPeriodOfCurrYear(billYear);
	 		   int curMaxPeriod=Integer.parseInt(rs.getRetVal().toString());
	 		   for(int i=curMaxPeriod+1;i<=billMonth;i++){
	 			   AccPeriodBean periodBean=this.getNewAccPeriodBean(billYear, i, loginId, sCompany);
	 		 	   list.add(periodBean);
	 		   }
	 	   }
	  		return list;
	    }
	    private AccPeriodBean getNewAccPeriodBean(int year,int month,String loginId,String sCompany){
	  	   String[] daysInMonth={"31","30","31","30","31","30","31","31","30","31","30","31"};
	  	   AccPeriodBean periodBean=new AccPeriodBean();
	  		periodBean.setId(IDGenerater.getId());
	  		periodBean.setAccYear(year);
	  		periodBean.setAccMonth(month);
	  		periodBean.setAccPeriod(month);
	  		periodBean.setCreateBy(loginId);
	  		periodBean.setLastUpdateBy(loginId);
	  		String ct=BaseDateFormat.format(new Date(),
	                BaseDateFormat.yyyyMMddHHmmss);
	  		periodBean.setCreateTime(ct);
	  		periodBean.setLastUpdateTime(ct);
	  	    periodBean.setStatusId(0);
	  		periodBean.setIsBegin(2);
	  		periodBean.setSCompanyID(sCompany);
	  		String daysInMon=daysInMonth[month-1];
	  		if(month==2){
	  			daysInMon=String.valueOf(((year%4==0)&&(year%100!=0)||(year%400==0))?29:28);
	  		}
	  		String periodBegin="";
	  		String periodEnd="";
	  		if(month>=10){
	  			periodBegin=year+"-"+month+"-01";
	  			periodEnd=year+"-"+month+"-"+daysInMon;
	  		}else{
	  			periodBegin=year+"-0"+month+"-01";
	  			periodEnd=year+"-0"+month+"-"+daysInMon;
	  		}
	  		periodBean.setPeriodBegin(periodBegin);
	  		periodBean.setPeriodEnd(periodEnd);
	  		return periodBean;
	     }
	
}
