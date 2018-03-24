package com.menyi.aio.web.advance;

import java.sql.*;
import java.util.*;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.web.util.*;

/**
 * 高级功能操作类
 * <p>Title:高级功能mgt</p> 
 * <p>Description: </p>
 *
 * @Date:2013-08-13
 * @Copyright: 科荣软件
 * @Author fjj
 */
public class AdvanceMgt extends AIODBManager{

	
	/**
	 * 查询系统模块
	 * @return
	 */
	public Result queryModule(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("select tblModules.id,tblModules.linkAddress,tblLanguage.zh_cn as moduleName FROM tblModules left join tblLanguage on tblModules.modelName=tblLanguage.id where classCode like '00100%' and classCode!='00100' ORDER BY orderBy");
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sql.toString());
							List list = new ArrayList();
							while (rs.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
                            		Object obj=rs.getObject(i);
                            		if(obj==null){
                            			if(rs.getMetaData().getColumnType(i)==Types.NUMERIC||rs.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rs.getMetaData().getColumnName(i), 0);
                            			}else{
                            				map.put(rs.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			map.put(rs.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	list.add(map);
                            }
							result.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("AdvanceMgt queryModule:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
}
