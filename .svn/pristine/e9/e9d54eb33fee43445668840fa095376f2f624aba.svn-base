package com.menyi.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Enumeration;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;

public class KRLanguageQuery {
	private StringBuilder idStr = new StringBuilder();
	private int count = 0;

	public void addLanguageId(String languageId) {
		languageId = languageId==null ?null: languageId.replaceAll("'", "''");
		if (languageId != null && languageId.length() > 0)
			idStr.append("'" + languageId + "',");
		count++;
	}

	public static KRLanguage create(String zh_CN, String en, String zh_TW) {
		KRLanguage lan = new KRLanguage();
		lan.putLanguage("zh_CN", zh_CN);
		lan.putLanguage("en", en);
		lan.putLanguage("zh_HK", zh_TW);
		lan.putLanguage("zh_TW", zh_TW);
		lan.setId(IDGenerater.getId());
		return lan;
	}

	public static KRLanguage create(Hashtable<String,String> localeTable, Locale defLocale, String str) {
		KRLanguage lan = new KRLanguage();
		if (str != null && str.trim().length() != 0) {
			//由于值中也可能出现：号，所以要计算是否有zh_cn:
			boolean hasLan = false;
			Enumeration<String> hem = localeTable.keys();
			while (hem.hasMoreElements()) {
				String key = hem.nextElement().toString();
				if(str.indexOf(key + ":") >  -1){
					hasLan = true;
					break;
				}
			}
			if (hasLan) {
				String[] ss = str.split(";"); //多语言中不允许有分号
				for(String s : ss){
					if(s.trim().length() > 0 && s.indexOf(":") > 0){
						if(s.substring(s.indexOf(":")+1).trim().length() > 0){
							if(localeTable.containsKey(s.substring(0,s.indexOf(":")).trim())){
								lan.putLanguage(s.substring(0,s.indexOf(":")).trim(), s.substring(s.indexOf(":")+1).trim());
							}
						}
					}
				}
			} else {
				lan.putLanguage(defLocale.toString(), str);
			}
			lan.setId(IDGenerater.getId());
		}
		return lan;
	}

	public static void delete(Connection conn, DBTableInfoBean tableInfo, String id, String flag) throws SQLException {
		List list = tableInfo.getFieldInfos();
		for (int i = 0; i < list.size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) list.get(i);
			if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE) {
				// 多语言字段， 要先删除语言表数据
				String sql = "delete FROM  tblLanguage where id in( select " + fieldInfo.getFieldName() + " from " + tableInfo.getTableName() + " WHERE "
						+ flag + " = ? ) ";

				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, id);
				stmt.executeUpdate();

			}
		}
	}

	public static void saveToDB(HashMap map, String id, Connection conn) throws SQLException {
		if (map.size() > 0) {
			Iterator it = map.keySet().iterator();
			ArrayList list = new ArrayList();
			String nstr = "id";
			String vstr = "?";
			while (it.hasNext()) {
				String key = it.next().toString();
				nstr += "," + key;
				vstr += ",?";
				list.add(key);
			}
			String sql = " insert into tblLanguage(" + nstr + ") values(" + vstr + ") ";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			for (int i = 0; i < list.size(); i++) {
				stmt.setString(i + 2, map.get(list.get(i)).toString());
			}
			stmt.executeUpdate();
		}
	}

	public HashMap<String,KRLanguage> query(Connection conn) throws SQLException {
		HashMap<String,KRLanguage> krlanMap = new HashMap<String,KRLanguage>();
		if (idStr.length() > 0) {
			idStr.deleteCharAt(idStr.length() - 1);
			String sql = "select * from tblLanguage";
			if (count < 10000)
				sql += " where id in (" + idStr + ") and isnull(id,'') <> '' ";
			else {
				sql += " where isnull(id,'') <> '' ";
			}
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Enumeration en = BaseEnv.localeTable.keys();
				KRLanguage lan = new KRLanguage();
				lan.setId(rs.getString("id"));
				while (en.hasMoreElements()) {
					String key = en.nextElement().toString();
					String value = rs.getString(key);
					if (value != null) {
						value = GlobalsTool.revertTextCode2(value);
						lan.putLanguage(key, value);
					}
				}
				krlanMap.put(lan.getId(), lan);
			}
		}
		return krlanMap;
	}
	
	public HashMap<String,KRLanguage> query()  {
           final Result rs=new Result();
           int retCode = DBUtil.execute(new IfDB() {
               public int exec(Session session) {
                   session.doWork(new Work() {
                       public void execute(Connection connection) throws
                               SQLException {
                    	   try{
                    	   	   HashMap map =query(connection);
                               rs.setRetVal(map);
                               rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                           } catch (SQLException ex) {
                               rs.setRetCode(ErrorCanst.
                                             DEFAULT_FAILURE);
                               return  ;
                           }
                       }
                   });
                   return 0;
               }
            });
           return (HashMap<String,KRLanguage>)rs.retVal;
    }
}
