package com.menyi.aio.web.processOldData;

import java.util.*;

import com.dbfactory.hibernate.DBManager;
import com.dbfactory.Result;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.aio.bean.ModuleBean;
import org.hibernate.Session;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.DBCanstant;
import com.dbfactory.hibernate.IfDB;
import org.hibernate.Query;
import com.menyi.aio.bean.RoleScopeBean;

import java.io.File;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.menyi.web.util.BaseEnv;
import java.sql.Connection;
import org.hibernate.jdbc.Work;
import java.sql.PreparedStatement;

import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.bean.RightType;
import com.menyi.aio.bean.RoleBean;
import com.menyi.aio.bean.RoleModuleBean;
import com.menyi.aio.bean.ModuleOperationBean;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;

/**
 * Description:
 * <br/>Copyright (C), 2008-2012, Justin.T.Wang
 * <br/>This Program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @autor Justin.T.Wang  yao.jun.wang@hotmail.com
 * @version 1.0
 */
public class ProcessOldDataMgt extends DBManager {
	
    public Result processAffix(final String upload) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                    	String str_sel="";
                    	try {
                    		ProcessOldDataMgt mgt=new ProcessOldDataMgt();
	                        Connection conn = connection;
	                        Statement st=conn.createStatement();
	                        ResultSet rs=null;
	                        ArrayList list=new ArrayList();
	                        //规则制度
	                        String sql="select id,accessories from dbo.OAOrdainInfo where charindex(',',accessories)>0 ";
	                        rs= st.executeQuery(sql);
	                        while(rs.next()){
	                        	list.add(new String[]{rs.getString(1),rs.getString(2)});
	                        }
	                        mgt.updateDir(conn, "OAOrdainInfo", "accessories",list, "OAOrdainInfo",upload);
	                        
	                        //我的资料夹
	                        list.clear();
	                        sql="select id,FilePath from OAFile where charindex(',',FilePath)>0 ";
	                        rs= st.executeQuery(sql);
	                        while(rs.next()){
	                        	list.add(new String[]{rs.getString(1),rs.getString(2)});
	                        }
	                        mgt.updateDir(conn, "OAFile", "FilePath",list, "OAFile",upload);
	                        
	                        //工作计划
	                        list.clear();
	                        sql="select id,accessories from OAMyWorkPlan where charindex(',',accessories)>0 ";
	                        rs= st.executeQuery(sql);
	                        while(rs.next()){
	                        	list.add(new String[]{rs.getString(1),rs.getString(2)});
	                        }
	                        mgt.updateDir(conn, "OAMyWorkPlan", "accessories",list, "OAMyWorkPlan",upload);
	                        
	                        //工作协助单
	                        list.clear();
	                        sql="select id,Attaches from OAJobodd where charindex(',',Attaches)>0 ";
	                        rs= st.executeQuery(sql);
	                        while(rs.next()){
	                        	list.add(new String[]{rs.getString(1),rs.getString(2)});
	                        }
	                        mgt.updateDir(conn, "OAJobodd", "Attaches",list, "OAJobodd",upload);
	                        
	                        //通知通告
	                        list.clear();
	                        sql="select id,filePath from OAAdviceInfo where charindex(',',filePath)>0 ";
	                        rs= st.executeQuery(sql);
	                        while(rs.next()){
	                        	list.add(new String[]{rs.getString(1),rs.getString(2)});
	                        }
	                        mgt.updateDir(conn, "OAAdviceInfo", "filePath",list, "OAAdviceInfo",upload);
	                        
	                        //知识中心
	                        list.clear();
	                        sql="select id,FilePath from OAKnowledgeCenterFile where charindex(',',FilePath)>0 ";
	                        rs= st.executeQuery(sql);
	                        while(rs.next()){
	                        	list.add(new String[]{rs.getString(1),rs.getString(2)});
	                        }
	                        mgt.updateDir(conn, "OAKnowledgeCenterFile", "FilePath", list, "OAKnowledgeCenterFile",upload);
                            
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
                            BaseEnv.log.error("Query data Error :" + str_sel, ex);
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
    
    public void updateDir(Connection  conn,String tableName,String fieldName,ArrayList list,String newDir,String upload) throws SQLException{
    	File dir= new File(BaseEnv.FILESERVERPATH + "/affix/"+newDir);
    	Statement st=conn.createStatement();
    	String sql="";
    	if(!dir.exists())dir.mkdirs();
    	for(int i=0;i<list.size();i++){
    		String[] obj=(String[])list.get(i);
    		String temp=obj[1];
    		String newFile="";
    		String [] str=temp.split(";");
    		for(int j=0;j<str.length;j++){
    			String []oldName=str[j].split(",");
    			File oldfile = new File(upload+"/" + oldName[1]);
    			if(oldfile.exists()){
    				newFile+=oldName[0]+";";
	    			File newfile = new File(BaseEnv.FILESERVERPATH + "/affix/"+newDir+"/" + oldName[0]);
	    			oldfile.renameTo(newfile);
    			}
    		}
    		if(newFile.length()>0){
    			sql="update "+tableName+" set "+fieldName+"='"+newFile+"' where id='"+obj[0]+"'";
        		st.addBatch(sql);
    		}
    	}    	
    	st.executeBatch();
    }
}
