package com.koron.oa.workflow;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.MessageBean;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;

/**
 * <p>
 * Title:工作流表单设计数据库操作
 * Description:
 * </p>
 * @Date:2013-04-11
 * @CopyRight:科荣软件
 * @Author:李文祥
 */
public class OAWorkFlowTableMgt extends AIODBManager {

	public Result layoutSave(final String tableName, final String html) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							PreparedStatement pstmt = conn.prepareStatement("update tblDBTableInfo set isLayout = 1 ,layoutHTML=? where tableName = ?");
							pstmt.setString(1, html);
							pstmt.setString(2, tableName);							
							int row = pstmt.executeUpdate();
							if (row > 0) {
								rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
								rst.setRealTotal(row);
							}
						} catch (Exception ex) {
							BaseEnv.log.error("OAWorkFlowTableMgt.layoutSave Error ",ex);
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		if(retCode == ErrorCanst.DEFAULT_SUCCESS){
			DBTableInfoBean bean  = BaseEnv.tableInfos.get(tableName);
			bean.setIsLayout(1);
			bean.setLayoutHTML(html);
		}
		return rst;
	}

	/**
	 * 根据表名和流程名称获取最新的表单
	 * 
	 * @param tableId
	 * @param layOutHtml
	 * @param createBy
	 * @param createTime
	 * @return
	 */
	public Result getNewTable(String tableName, String designId) {
		List<String> param = new ArrayList<String>();
		String sql = "select top 1 * from  OAWorkFlowTableVersion where  designId=? and tableId=(select id from tbldbtableInfo where tableName=? ) order by createTime desc";
		param.add("" + designId + "");
		param.add("" + tableName + "");
		return this.sqlList(sql, param);
	}

	/**
	 * 更加表单版本Id获取表单版本
	 * @param keyId
	 * @return
	 */
	public Result getTableById(String keyId) {
		List<String> param = new ArrayList<String>();
		String sql = "select layOutHtml from  OAWorkFlowTableVersion where id=? ";
		param.add("" + keyId + "");
		return this.sqlList(sql, param);
	}

	/**
	 * 根据Id获取工作流流程
	 */
	public Result getWorkTemplate(String designId) {
		List<String> param = new ArrayList<String>();
		String sql = "select id,templateName,templateFile  from  OAWorkFlowTemplate where id= ? ";
		param.add("" + designId + "");
		return this.sqlList(sql, param);
	}

	/**
	 * 添加表单版本
	 * @param tableId
	 * @param layOutHtml
	 * @param createBy
	 * @param createTime
	 * @return
	 */
	public Result addTableVersion(String tableId, String layOutHtml, String createBy, String createTime, String saveType, String designId) {
		List<String> param = new ArrayList<String>();
		String sql = "insert into OAWorkFlowTableVersion (id,tableId,layOutHtml,createBy,createTime,saveType,designId) values(?,?,?,?,?,?,?)";
		param.add(IDGenerater.getId());
		param.add(tableId);
		param.add(layOutHtml);
		param.add(createBy);
		param.add(createTime);
		param.add(saveType);
		param.add(designId);
		return msgSql(sql, param);
	}

	/**
	 * 删除打印产生的表单版本
	 * @param tableId
	 * @param designId
	 * @return
	 */
	public Result deleteViewVersion(String tableId, String designId) {
		List<String> param = new ArrayList<String>();
		String sql = "delete  from OAWorkFlowTableVersion where tableId=? and designId=? and saveType='view' ";
		param.add(tableId);
		param.add(designId);
		return msgSql(sql, param);
	}

	/**
	 * 获取表自定义字段的字段值的数字最大值 ，如field_25,field_23 则返回25
	 * @param tableName
	 * @return
	 */
	public String getMaxFiledVal(String tableName) {
		List<String> param = new ArrayList<String>();
		String sql = "SELECT top 1 cast(REPLACE(name,'field_','') as INT) as MaxVal FROM syscolumns  WHERE  "
				+ "id=OBJECT_ID(?) AND name LIKE 'field_%' ORDER BY cast(REPLACE(name,'field_','') as INT) DESC ";
		param.add("" + tableName + "");
		Result rs = this.sqlList(sql, param);

		String maxVal = "";
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List obj = (List) rs.retVal;
			if (obj != null && obj.size() > 0) {
				Object[] objList = (Object[]) obj.get(0);
				maxVal = objList[0].toString();
			}
		}
		return maxVal;

	}

	/**
	 * 
	 * @param updFieldList  待修改的字段
	 * @param addFieldList  待增加的字段
	 * @param delFieldList  待删除的字段
	 * @param tabBean  主表
	 * @desc 修改表单后，需要修改物理表结构，因客户有可能还原旧表单，所以此处只对表结构添加列
	 * @return
	 */
	public Result updateField(final List<String> tempnewList, final List<String> addFieldList, final DBTableInfoBean tabBean) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement state = conn.createStatement();
							String delsql = "delete from  tbldbfieldinfo where fieldName like 'field_%' and tableId='" + tabBean.getId() + "'";
							state.executeUpdate(delsql);
							for (int i = 0; i < tempnewList.size(); i++) {
								String[] strList = tempnewList.get(i).split("\\|");
								String fieldName = strList[0];
								String display = strList[1];
								if ("undefined".equals(display)) {
									continue;
								}
								String Id = IDGenerater.getId();
								String isNull = strList[3];
								int nullVal = 0;
								if ("true".equals(isNull)) { //必填
									nullVal = DBFieldInfoBean.IS_NULL;
								} else {
									nullVal = DBFieldInfoBean.IS_NULL;
								}

								String defaultVal = "";
								String inputVal = "";
								if (!strList[2].contains("BillNo")) {
									if (strList[2].contains("popup") || strList[2].contains("count")) {// 定制弹出框口，需要保存弹出框值
										String strs[] = strList[2].split("&&");
										inputVal = strs[1];
										defaultVal = strs[0];
									} else {
										defaultVal = strList[2];
									}
								}
								if ("undefined".equals(defaultVal)) {
									defaultVal = "";
								}
								int isStat = DBFieldInfoBean.STAT_NO;
								int isUnique = DBFieldInfoBean.UNIQUE_NO;
								int udType = DBFieldInfoBean.USER_TYPE;
								int maxLength = 30;
								byte listOrder = 100;
								byte fieldType = DBFieldInfoBean.FIELD_ANY;
								byte inputType = DBFieldInfoBean.INPUT_NORMAL;
								String addsql = "";
								if (!strList[2].contains("BillNo")) {
									addsql = "insert into tbldbfieldinfo (id,fieldName,tableId,fieldType,languageId,defaultValue,listOrder,[isNull],maxLength,udType,inputType,isunique,isStat,inputValue)"
											+ " values('"
											+ Id
											+ "','"
											+ fieldName
											+ "','"
											+ tabBean.getId()
											+ "','"
											+ fieldType
											+ "','"
											+ display
											+ "','"
											+ defaultVal
											+ "','"
											+ listOrder
											+ "','"
											+ nullVal + "','" + maxLength + "','" + udType + "','" + inputType + "','" + isUnique + "','" + isStat + "','" + inputVal + "')";
								} else {
									String strs[] = strList[2].split("&&");
									inputVal = strs[1];
									String valList[] = inputVal.split("=");
									String resetVal = "";
									if ("year".equals(valList[3])) {
										resetVal = "1";
									} else if ("month".equals(valList[3])) {
										resetVal = "2";
									} else {
										resetVal = "5";
									}

									addsql = "insert into tbldbfieldinfo (id,fieldName,tableId,fieldType,languageId,listOrder,[isNull],maxLength,udType,inputType,isunique,isStat,fieldSysType,fieldIdentityStr,isCopy,statusId)"
											+ " values('" + Id + "','" + fieldName + "','" + tabBean.getId() + "','2','" + display + "','1','1','30','0','0','1','0','RowMarker','BillNo','1','1')";
									if (valList != null) {
										String key = tabBean.getTableName() + "_" + fieldName;
										int billNo = 0;
										if (!"undefined".equals(valList[1]) && !"undefined".equals(valList[2])) {
											billNo = Integer.parseInt(valList[1].toString()) - Integer.parseInt(valList[2].toString());
											String addBillsql2 = "if not exists(select * from tblBillNo where [key]='" + key
													+ "') insert into tblbillNo ([key],pattern,billNo,start,step,isfillback,reset,laststamp,billName) " + " values('" + key + "','" + valList[0]
													+ "','" + billNo + "','" + valList[1] + "','" + valList[2] + "','" + nullVal + "','" + resetVal + "','" + System.currentTimeMillis() + "','"
													+ tabBean.getDisplay().get("zh_CN") + "')";
											state.addBatch(addBillsql2);
										}
									}
								}
								state.addBatch(addsql);
							}
							for (int i = 0; i < addFieldList.size(); i++) { //更改物理表结构
								String[] strList = addFieldList.get(i).split("\\|");
								String fieldName = strList[0];
								String addColumn = "alter table " + tabBean.getTableName() + " add " + fieldName + " varchar(max)";
								state.addBatch(addColumn);
							}
							state.executeBatch();
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
						}
					}
				});
				return rs.retCode;
			}
		});
		rs.retCode = retCode;
		return rs;
	}

	/**
	 * jdbc调用公共方法
	 * @param sql
	 * @param param
	 * @return
	 */
	public Result msgSql(final String sql, final List param) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							PreparedStatement pstmt = conn.prepareStatement(sql);
							for (int i = 1; i <= param.size(); i++) {
								pstmt.setObject(i, param.get(i - 1));
							}
							int row = pstmt.executeUpdate();
							if (row > 0) {
								rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
								rst.setRealTotal(row);
							}
						} catch (Exception ex) {
							ex.printStackTrace();

							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
}
