package com.menyi.web.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.Set;

import com.koron.oa.bean.OAWorkFlowTemplate;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.GoodsPropEnumItemBean;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.bean.KRLanguage;

import java.util.ArrayList;
import com.menyi.aio.bean.ReportsBean;
import com.menyi.aio.bean.ReportsDetBean;
import com.menyi.aio.bean.TableMappedBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.customize.CustomizeMgt;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.report.ReportSetMgt;
import com.dbfactory.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class ConvertToSql {
    static CustomizeMgt mgt = new CustomizeMgt();
    static ReportSetMgt rpMgt=new ReportSetMgt();
    public ConvertToSql() {
    }

    //保存插入tblDBTableInfo表的sql脚本
    public static void saveSqlByTableInfo(DBTableInfoBean ti, String path) {
        Result rs = mgt.queryFieldsByTableName("tblDBTableInfo");
        String sql = "";
        if (rs.getRetCode() != ErrorCanst.DEFAULT_FAILURE && (((ArrayList) rs.getRetVal()).size() > 0)) {
            sql = "insert into tblDBTableInfo (";
            String sqlValue = "select ";
            ArrayList list = (ArrayList) rs.getRetVal();
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    sql += list.get(i) + ") values(";
                    sqlValue += list.get(i) + " from tblDBTableInfo where id='" + ti.getId() + "'";
                } else {
                    sql += list.get(i) + ",";
                    sqlValue += list.get(i) + ",";
                }
            }
            rs = mgt.queryFieldsValue(sqlValue, list);
            if (rs.getRetCode() != ErrorCanst.DEFAULT_FAILURE && (((ArrayList) rs.getRetVal()).size() > 0)) {
                List<Object> values = (List<Object>) rs.getRetVal();
                for (int i = 0; i < values.size(); i++) {
                    if (i == values.size() - 1) {
                        sql += "'" + values.get(i) + "')";
                    } else {
                        sql += "'" + values.get(i) + "',";
                    }
                }
            }
        }
        TestRW.saveToSql(path, sql);
    }

    //保存插入tblDBTableDisplay表的sql脚本
    public static void saveSqlByTableDisplay(DBTableInfoBean ti, String path) {
        if(ti.getDisplay().map.size() ==0 ) return;
        String sf = "id";
        String sv = "'"+ti.getDisplay().getId()+"'";

        Iterator it = ti.getDisplay().map.keySet().iterator();
        while (it.hasNext()) {
            String key = (String)it.next();
            sf += ","+key;
            sv += ",'"+ti.getDisplay().map.get(key)+"'";
        }
        String sql = "insert into tblLanguage("+sf+") values("+sv+")";
        TestRW.saveToSql(path, sql);
    }

    //保存插入tblDBFieldInfo表的sql脚本
    public static void saveSqlByDBField(DBTableInfoBean ti, String path) {
        Result rs = mgt.queryFieldsByTableName("tblDBFieldInfo");
        if (rs.getRetCode() != ErrorCanst.DEFAULT_FAILURE && (((ArrayList) rs.getRetVal()).size() > 0)) {

            ArrayList list = (ArrayList) rs.getRetVal();
            List fieldsInfo = ti.getFieldInfos();
            for (int k = 0; k < fieldsInfo.size(); k++) {
                String sql = "insert into tblDBFieldInfo (";
                String sqlValue = "select ";
                DBFieldInfoBean fieldInfo = (DBFieldInfoBean) fieldsInfo.get(k);
                for (int i = 0; i < list.size(); i++) {
                    if (i == list.size() - 1) {
                        sql += list.get(i) + ") values(";
                        sqlValue += list.get(i) +
                            " from tblDBFieldInfo where id='" +
                            fieldInfo.getId() +
                            "'";
                    } else {
                        sql += list.get(i) + ",";
                        sqlValue += list.get(i) + ",";
                    }
                }
                rs = mgt.queryFieldsValue(sqlValue, list);
                if (rs.getRetCode() != ErrorCanst.DEFAULT_FAILURE && (((ArrayList) rs.getRetVal()).size() > 0)) {
                    List<Object>
                        values = (List<Object>) rs.getRetVal();
                    for (int i = 0; i < values.size(); i++) {
                        if (i == values.size() - 1) {
                            sql += "'" + values.get(i) + "')";
                        } else {
                            sql += "'" + values.get(i) + "',";
                        }
                    }
                }
                TestRW.saveToSql(path, sql);

            }
        }

    }

    //保存插入tblDBFieldDisplay表的sql脚本
    public static void saveSqlByFieldDisplay(DBTableInfoBean ti, String path) {
        for (int i = 0; i < ti.getFieldInfos().size(); i++) {
            DBFieldInfoBean fi = (DBFieldInfoBean) ti.getFieldInfos().get(i);
            if(fi.getDisplay() != null && fi.getDisplay().map.size() >0 ) {
	            String sf = "id";
	            String sv = "'" +(fi.getDisplay() == null?"": fi.getDisplay().getId()) + "'";
	
	            Iterator it = fi.getDisplay().map.keySet().iterator();
	            while (it.hasNext()) {
	                String key = (String) it.next();
	                sf += "," + key;
	                sv += ",'" + (fi.getDisplay() == null?"" :fi.getDisplay().map.get(key))+ "'";
	            }
	            String sql = "insert into tblLanguage(" + sf + ") values(" + sv + ")";
	            TestRW.saveToSql(path, sql);
            }
            if(fi.getGroupDisplay() != null && fi.getGroupDisplay().map.size() >0 ) {
	            String sf = "id";
	            String sv = "'" +(fi.getGroupDisplay() == null?"": fi.getGroupDisplay().getId()) + "'";
	
	            Iterator it = fi.getGroupDisplay().map.keySet().iterator();
	            while (it.hasNext()) {
	                String key = (String) it.next();
	                sf += "," + key;
	                sv += ",'" + (fi.getGroupDisplay() == null?"" :fi.getGroupDisplay().map.get(key))+ "'";
	            }
	            String sql = "insert into tblLanguage(" + sf + ") values(" + sv + ")";
	            TestRW.saveToSql(path, sql);
            }
        }
    }

    //获得插入tblModules表或tblModelOperations表的脚本
    public static String getSqlByTNameAndParams(String tableName, ArrayList list, String nstr,ArrayList moreLan,String id) {
        String sql = "";
        if (tableName.equalsIgnoreCase("tblModules")) {
        	String sqlTime=BaseDateFormat.format(new Date(),
                    BaseDateFormat.yyyyMMddHHmmss);
            String condition = "--"+sqlTime+"新增模块\nif exists(select * from tblModules where id='" + id + "')\nbegin\n";
            for(int i=0;i<moreLan.size();i++){
            	  KRLanguage lan = (KRLanguage)moreLan.get(i);
            	  condition+="delete from tblLanguage where id='"+lan.getId()+"'\n";
            }
            condition+="delete from tblModelOperations where f_ref='" + id + "';\n" +
                               "delete from tblModules where id='" + id + "';\n";
            String temp = "insert into tblModules(" + nstr + ") values (";
            for (int i = 0; i < list.size(); i++) {
                Object tempstr = list.get(i);
                tempstr = tempstr == null ? "" : tempstr;
                if (i == list.size() - 1) {
                    temp += "'" + tempstr.toString() + "');\n";
                } else {
                    temp += "'" + tempstr.toString() + "',";
                }
            }
            sql = condition + temp + "end\nelse\n" + temp + "\n";
        } else if (tableName.equalsIgnoreCase("tblModelOperations")) {
            sql = "insert into tblModelOperations(" + nstr + ") values (";
            //生成的脚本中要把moduleOpId清零
            String[] ms = sql.split(",");
            int pos = 0;
            for (int i = 0; i < ms.length; i++) {
            	if(ms[i].trim().toLowerCase().equals("moduleopid")){
            		pos = i;
            	}
            }
            for (int i = 0; i < list.size(); i++) {
                Object tempstr = list.get(i);
                tempstr = tempstr == null ? "" : tempstr;
                if(i==pos){
                	tempstr = "0";
                }
                if (i == list.size() - 1) {
                    sql += "'" + tempstr.toString() + "');\n";
                } else {
                    sql += "'" + tempstr.toString() + "',";
                }
            }
            sql += "update tblModelOperations set moduleOpId=id where moduleOpId=0 \n";

        }

        return sql;
    }

    //获得更新tblModules表的脚本
    public static String getUpModuleSqlBy(Connection conn, ArrayList list, String id,DBTableInfoBean tableInfo) {
        Result rs = queryFieldsByTableName(conn, "tblModules");
        String sqlTime=BaseDateFormat.format(new Date(),
                BaseDateFormat.yyyyMMddHHmmss);
        String sql = "--"+sqlTime+"修改模块";
        if (rs.getRetCode() != ErrorCanst.DEFAULT_FAILURE && (((ArrayList) rs.getRetVal()).size() > 0)) {
            String sqlValue = "select ";
            ArrayList fieldsName = (ArrayList) rs.getRetVal();
            for (int i = 0; i < fieldsName.size(); i++) {
                if (i == fieldsName.size() - 1) {
                    sqlValue += fieldsName.get(i) + " from tblModules where id='" + id + "'";
                } else {
                    sqlValue += fieldsName.get(i) + ",";
                }
            }
            rs = queryFieldsValue(conn, sqlValue, fieldsName);
            if (rs.getRetCode() != ErrorCanst.DEFAULT_FAILURE && (((ArrayList) rs.getRetVal()).size() > 0)) {
            	List fields=tableInfo.getFieldInfos();
                for(int i=0;i<fields.size();i++){
                	DBFieldInfoBean df=(DBFieldInfoBean)fields.get(i);
                	if(df.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE){
                		sql+="\ndelete from tblLanguage where id in (select "+df.getFieldName()+" from "+tableInfo.getTableName()+" where id='"+id+"')";
                		sql +="\n delete from tblModelOperations where f_ref='"+id+"'";
                	}
                }
                sql += "\nupdate  tblModules set ";
                List<Object> values = (List<Object>) rs.getRetVal();
                for (int i = 0; i < values.size(); i++) {
                    if (i == values.size() - 1) {
                        sql += fieldsName.get(i) + "='" + values.get(i) + "' where id='" + id + "'\n";
                    } else {
                        sql += fieldsName.get(i) + "='" + values.get(i) + "',";
                    }
                }
            }
        }
        return sql;
    }

    public static String getSqlOfAddProp(Connection conn,GoodsPropInfoBean propInfo) {
        	String sqlTime = BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		String sql = "--" + sqlTime + "新增属性\n";
		String insMain = "insert into tblGoodsPropInfo(id,propName,SCompanyID,createBy,lastUpdateBy,createTime,lastUpdateTime,isUsed,isCalculate,joinTable,alertName,inputBill,twoUnit,languageId,isSequence,sepAppoint)values('"
			+propInfo.getId()+"','"+propInfo.getPropName()+"','"+propInfo.getSCompanyID()+"','"+propInfo.getCreateBy()+"','"
			+propInfo.getLastUpdateBy()+"','"+propInfo.getCreateTime()+"','"+propInfo.getLastUpdateTime()+"',"+propInfo.getIsUsed()+","+propInfo.getIsCalculate()+","+propInfo.getJoinTable()+",'"+propInfo.getAlertName()+"',"
			+propInfo.getInputBill()+","+propInfo.getTwoUnit()+",'"+propInfo.getLanguageId()+"',"+propInfo.getIsSequence()+","+propInfo.getSepAppoint()+")\n";
		if (propInfo.getDisplay() != null
				&& propInfo.getDisplay().map.size() != 0) {
			String sf = "id";
			String sv = "'"
					+ (propInfo.getDisplay() == null ? "" : propInfo
							.getDisplay().getId()) + "'";

			Iterator it = propInfo.getDisplay().map.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				sf += "," + key;
				sv += ",'"
						+ (propInfo.getDisplay() == null ? "" : propInfo
								.getDisplay().map.get(key)) + "'";
			}
			insMain += "insert into tblLanguage(" + sf + ") values(" + sv
					+ ")\n";
		}
		sql += insMain;
        String childSql="insert into tblGoodsPropEnumItem(id,SCompanyID,enumValue,propId,isUsed,printCount,languageId) values(";
        
		List items = propInfo.getEnumItem();
		for (int i = 0; i < items.size(); i++) {
			String tempChildSql=childSql;
			GoodsPropEnumItemBean item = (GoodsPropEnumItemBean) items.get(i);
			tempChildSql+="'"+item.getId()+"','"+item.getSCompanyID()+"','"+item.getEnumValue()+"','"+propInfo.getId()+"',"+item.getIsUsed()+",0,'"+item.getLanguageId()+"')";
			sql+=tempChildSql;
			if (item.getDisplay() != null && item.getDisplay().map.size() != 0) {
				String sf = "id";
				String sv = "'"
						+ (item.getDisplay() == null ? "" : item.getDisplay()
								.getId()) + "'";

				Iterator it = item.getDisplay().map.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					sf += "," + key;
					sv += ",'"
							+ (item.getDisplay() == null ? "" : item
									.getDisplay().map.get(key)) + "'";
				}
				sql += "insert into tblLanguage(" + sf + ") values(" + sv
						+ ")\n";
			}
		} 

        return sql;
    }
    /**
	 * 获得表字段
	 * 
	 * @param conn
	 *            Connection
	 * @param tableName
	 *            String
	 * @return Result
	 */
    private static Result queryFieldsByTableName(Connection conn, String tableName) {
        Result rs = new Result();
        try {
            List<String> fields = new ArrayList<String>();
            Statement cs = conn.createStatement();
            String sql = "select NAME from syscolumns where id = object_id('" +
                         tableName + "')";
            ResultSet rss = cs.executeQuery(sql);
            while (rss.next()) {
                fields.add(rss.getString("NAME"));
            }
            rs.setRetVal(fields);
            rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
        } catch (SQLException ex) {
            rs.setRetCode(ErrorCanst.
                          DEFAULT_FAILURE);
        }
        return rs;
    }

    /**
     * 获得表字段对应的数据
     * @param conn Connection
     * @param sql String
     * @param fieldsName ArrayList
     * @return Result
     */
    private static Result queryFieldsValue(Connection conn, String sql, ArrayList fieldsName) {
        Result rs = new Result();
        try {
            List<Object> values = new ArrayList<Object>();
            Statement cs = conn.createStatement();
            ResultSet rss = cs.executeQuery(sql);
            while (rss.next()) {
                for (int i = 0; i < fieldsName.size(); i++) {
                    Object value = rss.getObject(fieldsName.get(i).toString());
                    value = value == null ? "" : value;
                    values.add(value);
                }

            }
            rs.setRetVal(values);
            rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
        } catch (SQLException ex) {
            rs.setRetCode(ErrorCanst.
                          DEFAULT_FAILURE);
        }
        return rs;
    }

   

   
    //获得删除报表的脚本
    public static List<String> getReportsDelSql(String[] keyIds) {
    	String sqlTime=BaseDateFormat.format(new Date(),
                BaseDateFormat.yyyyMMddHHmmss);
        List<String> list = new ArrayList<String>();
        list.add("--"+sqlTime+"删除报表\n");
        for (int i = 0; i < keyIds.length; i++) {
            String sql = "delete from tblReportsDet where f_ref='" + keyIds[i] + "';\n" +
                         "delete from tblLanguage where id=(select ReportName from tblReports where id='"+keyIds[i]+"')\n"+
                         "delete from tblReports where id='" + keyIds[i] + "';";
            list.add(sql);
        }
        return list;
    }

    //获得映射的脚本
    public static List<String> getMappedSql(List<TableMappedBean> mapBean) {
    	String sqlTime=BaseDateFormat.format(new Date(),
                BaseDateFormat.yyyyMMddHHmmss);
        List<String> list = new ArrayList<String>();
        TableMappedBean mpb = (TableMappedBean) mapBean.get(0);
        String sql = "--"+sqlTime+"新增源表:"+mpb.getMostlyTable()+"与目标表:+"+mpb.getChildTable()+"之间的映射\nif exists(select * from tblTableMapped where mostlyTable='" + mpb.getMostlyTable() + "' and childTable='" +
                     mpb.getChildTable() + "' )\ndelete from tblTableMapped where mostlyTable='" + mpb.getMostlyTable() +
                     "' and childTable='" + mpb.getChildTable() + "'\ngo";
        list.add(sql);
        for (int i = 0; i < mapBean.size(); i++) {
            list.add(getSqlByTblTableMapped(mapBean.get(i)));

        }
        return list;
    }

    public static String getSqlByTblTableMapped(TableMappedBean bean) {
        String id = bean.getId();
        String mostlyTable = bean.getMostlyTable();
        String mostalTableField = bean.getMostlyTableField();
        String childTable = bean.getChildTable();
        String childTableField = bean.getChildTableField();
        String createBy = bean.getCreateBy();
        String lastUpdateBy = bean.getLastUpdateBy();
        String createTime = bean.getCreateTime();
        String lastUpdateTime = bean.getLastUpdateTime();
        String sql = "insert into tblTableMapped(id,mostlyTable,mostlyTableField,childTable,childTableField,createBy,lastUpdateBy,createTime,lastUpdateTime,SCompanyId) values('" +
                     id + "','" +
                     mostlyTable + "','" +
                     mostalTableField + "','" + childTable + "','" +
                     childTableField + "','" + createBy + "','" + lastUpdateBy +
                     "','" +
                     createTime + "','" + lastUpdateTime + "','" + bean.getScompanyId() + "')";
        return sql;

    }

    /**
     * 获得多语言脚本
     * @param lan
     * @return
     */
    public static String getLanguageStr(KRLanguage lan,String tableName){
    	String sql="";
    	if(tableName.equalsIgnoreCase("tblModules")||tableName.equalsIgnoreCase("tblModelOperations")){
    	if(lan.map.size() > 0 ){
            Iterator it = lan.map.keySet().iterator();
            ArrayList list = new ArrayList();
            String nstr=  "id";
            String vstr = "'"+lan.getId()+"'";
            while (it.hasNext()) {
                String key = it.next().toString();
                nstr +=","+key;
                vstr +=",'"+lan.map.get(key)+"'";
               
            }
            sql = " insert into tblLanguage("+nstr+") values("+vstr+")\n";
    	}
    	}
    	return sql;
    }
   /**
    * 获得多语言脚本
    * @param lan
    * @return
    */
    public static String getLanguageStr(KRLanguage lan){
    	String sql="";
    	if(lan.map.size() > 0 ){
            Iterator it = lan.map.keySet().iterator();
            ArrayList list = new ArrayList();
            String nstr=  "id";
            String vstr = "'"+lan.getId()+"'";
            while (it.hasNext()) {
                String key = it.next().toString();
                nstr +=","+key;
                vstr +=",'"+lan.map.get(key)+"'";
               
            }
            sql = " insert into tblLanguage("+nstr+") values("+vstr+")\n";
    	}
    	return sql;
    }
    /**
     * 保存新增或修改报表脚本
     * @param path
     * @param id
     */
    public static void saveReports(String  path,String id,int type){
    	String sqlTime=BaseDateFormat.format(new Date(),
                BaseDateFormat.yyyyMMddHHmmss);
    	String rpSql="";
    	if(type==1){
    	  rpSql+="--"+sqlTime+"新增报表\n";	
    	}else{
    	  rpSql+="--"+sqlTime+"修改报表\n";	
    	}
    	rpSql += "if exists(select * from tblReports where id='" + id + "')\nbegin\n"+
        "delete from tblReportsDet where f_ref='" + id + "';\n" +
        "delete from tblLanguage where id=(select ReportName from tblReports where id='"+id+"')\n"+
        "delete from tblReports where id='" + id +"'\nend\n";
    	String insMainSql="insert tblReports(";
    	String insChildSql="insert tblReportsDet(";
    	Result rs=mgt.queryFieldsByTableName("tblReports");
    	if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){return;}
    	List mainFnames=(ArrayList)rs.getRetVal();
    	rs=mgt.queryFieldsByTableName("tblReportsDet");
    	if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){return;}
    	List childFnames=(ArrayList)rs.getRetVal();
        rs=rpMgt.queryReportsNewData(id,mainFnames,"tblReports");
    	if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
    		HashMap mainValues=(HashMap)rs.getRetVal();
    		for(int i=0;i<mainFnames.size();i++){
    			if(i==mainFnames.size()-1){
    				insMainSql+=mainFnames.get(i)+") values(";
    			}else{
    			insMainSql+=mainFnames.get(i)+",";
    			}
    		}
    		for(int i=0;i<mainFnames.size();i++){
    			if(i==mainFnames.size()-1){
    				insMainSql+="'"+mainValues.get(mainFnames.get(i))+"')\n";
    			}else{
    				insMainSql+="'"+mainValues.get(mainFnames.get(i))+"',";
    			}
    		}
    		rpSql+=insMainSql;
    		rs=rpMgt.queryReportNameLanguage(mainValues.get("ReportName").toString());
    		if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
    			KRLanguage lan =(KRLanguage)rs.getRetVal();
    			String sql="insert into tblLanguage(id";
    			String vSql=" values('"+lan.getId()+"'";
    			Iterator it=lan.map.keySet().iterator();
    			while(it.hasNext()){
    				Object key=it.next();
    				String value=lan.map.get(key).toString();
    				sql+=","+key;
    				vSql+=",'"+value+"'";
    			}
    			sql+=")"+vSql+")\n";
    			rpSql+=sql;
    		}
    	}else{
    		return;
    	}
    	rs=rpMgt.queryReportsDetNewData(id, childFnames,"tblReportsDet","f_ref");
    	if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
    		for(int i=0;i<childFnames.size();i++){
    			if(i==childFnames.size()-1){
    				insChildSql+=childFnames.get(i)+") values(";
    			}else{
    				insChildSql+=childFnames.get(i)+",";
    			}
    		}
    		ArrayList childValues=(ArrayList)rs.getRetVal();
    		for(int j=0;j<childValues.size();j++){
    			String tempStr=insChildSql;
    			List row=(ArrayList)childValues.get(j);
    		for(int i=0;i<row.size();i++){
    			if(i==row.size()-1){
    				tempStr+="'"+row.get(i)+"')\n";
    			}else{
    				tempStr+="'"+row.get(i)+"',";
    			}
    		}
    		rpSql+=tempStr;
    		}
    	}else{
    		return;
    	}
    	TestRW.saveToSql(path, rpSql);
    }
    /**
     * 保存新增或修改枚举脚本
     * @param path
     * @param id
     * @param type
     */
    public static void saveEnum(String  path,String id,String enumName,int type){
    	String sqlTime=BaseDateFormat.format(new Date(),
                BaseDateFormat.yyyyMMddHHmmss);
    	String rpSql="";
    	if(type==1){
    	  rpSql+="--"+sqlTime+"新增枚举\n";	
    	}else{
    	  rpSql+="--"+sqlTime+"修改枚举\n";	
    	}
    	rpSql += "if exists(select * from tblDBEnumeration where enumName='"+enumName+"')\nbegin\n" +
    	"delete from tblLanguage where id in (select languageId from tblDBEnumerationItem where enumId=(select id from tblDBEnumeration where enumName='"+enumName+"'))\n"+
    	"delete from tblLanguage where id in (select languageId from tblDBEnumeration where enumName='"+enumName+"')\n"+
        "delete from tblDBEnumerationItem where enumId=(select id from tblDBEnumeration where enumName='"+enumName+"');\n" +
        "delete from tblDBEnumeration where enumName='" +enumName + "';\nend\n";
    	String insMainSql="insert tblDBEnumeration(";
    	String insChildSql="insert tblDBEnumerationItem(";
    	Result rs=mgt.queryFieldsByTableName("tblDBEnumeration");
    	if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){return;}
    	List mainFnames=(ArrayList)rs.getRetVal();
    	rs=mgt.queryFieldsByTableName("tblDBEnumerationItem");
    	if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){return;}
    	List childFnames=(ArrayList)rs.getRetVal();
        rs=rpMgt.queryReportsNewData(id,mainFnames,"tblDBEnumeration");
    	if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
    		HashMap mainValues=(HashMap)rs.getRetVal();
    		for(int i=0;i<mainFnames.size();i++){
    			if(i==mainFnames.size()-1){
    				insMainSql+=mainFnames.get(i)+") values(";
    			}else{
    			insMainSql+=mainFnames.get(i)+",";
    			}
    		}
    		for(int i=0;i<mainFnames.size();i++){
    			if(i==mainFnames.size()-1){
    				insMainSql+="'"+mainValues.get(mainFnames.get(i))+"')\n";
    			}else{
    				insMainSql+="'"+mainValues.get(mainFnames.get(i))+"',";
    			}
    		}
    		rpSql+=insMainSql;
    	}else{
    		return;
    	}
    	rs=rpMgt.queryReportsDetNewData(id, childFnames,"tblDBEnumerationItem","enumId");
    	if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
    		for(int i=0;i<childFnames.size();i++){
    			if(i==childFnames.size()-1){
    				insChildSql+=childFnames.get(i)+") values(";
    			}else{
    				insChildSql+=childFnames.get(i)+",";
    			}
    		}
    		ArrayList childValues=(ArrayList)rs.getRetVal();
    		for(int j=0;j<childValues.size();j++){
    			String tempStr=insChildSql;
    			List row=(ArrayList)childValues.get(j);
    		for(int i=0;i<row.size();i++){
    			if(i==row.size()-1){
    				tempStr+="'"+row.get(i)+"')\n";
    			}else{
    				tempStr+="'"+row.get(i)+"',";
    			}
    		}
    		rpSql+=tempStr;
    		}
    	}else{
    		return;
    	}
    	TestRW.saveToSql(path, rpSql);
    }
/**
 * 保存删除枚举脚本
 * @param path
 * @param ids
 */
    public static void saveDelEnum(String path,String []ids){
    	String sqlTime=BaseDateFormat.format(new Date(),
                BaseDateFormat.yyyyMMddHHmmss);
    	String del="--"+sqlTime+"删除枚举\n";
    	for(int i=0;i<ids.length;i++){
    		del+="delete from tblLanguage where id in (select languageId from tblDBEnumerationItem where enumId='"+ids[i]+"')\n"+
        	"delete from tblLanguage where id in (select languageId from tblDBEnumeration where id='"+ids[i]+"')\n"+
            "delete from tblDBEnumerationItem where enumId='" + ids[i] + "';\n" +
            "delete from tblDBEnumeration where id='" + ids[i] + "';\n";
    	}
    	TestRW.saveToSql(path, del);
    	
    }
}
