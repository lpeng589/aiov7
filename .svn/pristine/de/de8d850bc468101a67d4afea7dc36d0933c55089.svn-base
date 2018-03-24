package com.menyi.email;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;

public class EMailSettingMgt extends AIODBManager {
	
	public static String[] labelcolors = new String[]{"FF0000","790000","FFFF00","00FF00","00FFFF","0000FF","FF00FF","005E20","8C6239","8781BD","EC008C"};
	
	private static HashMap<String,String> mailParam = new HashMap<String,String>();
	private static ArrayList<String> mailLabels = new ArrayList<String>();
	private static HashMap<String,String> mailLabelMap = new HashMap<String,String>();
	
	public static HashMap<String,String> getMailLabelMap(){
		if(mailLabelMap.size() ==0){
			ArrayList<String> list= getMailLabels();
			for(int i=0;i<list.size();i++){
				if(i<labelcolors.length){
					mailLabelMap.put(list.get(i),labelcolors[i]);
				}else{
					mailLabelMap.put(list.get(i),labelcolors[labelcolors.length -1]);
				}
			}
		}
		return mailLabelMap;
	}
	public static ArrayList<String> getMailLabels(){
		if(mailLabels.size() ==0){
			String mailLabel = listParamMap().get("mailLabel");
			for(String s:mailLabel.split(";|,|\\s")){
				if(s != null && s.trim().length()>0){
					mailLabels.add(s);
				}
			}
		}
		return mailLabels;
	}
	public Result listParam(){
		List param = new ArrayList();
		String sql = " select paramName,paramValue from tblMailSystemParam ";		
		return this.sqlList(sql, param);
	}
	public static HashMap<String,String> listParamMap(){
		if(mailParam.size() ==0){
			//重新读取
			Result rs = new EMailSettingMgt().listParam();			
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null){
				for(Object o:(ArrayList)rs.retVal){
					Object os[] = (Object[])o;
					mailParam.put(os[0].toString(), os[1].toString());
				}
			}	
		}		
		return mailParam;
	}
	
	public Result updateParam(final String paramName,final String paramValue){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {                    	
                        Connection conn = connection;
                        String sql = "";
                        sql = "update tblMailSystemParam set paramValue=? where paramName=?";
                        PreparedStatement s = conn.prepareStatement(sql);
                        s.setString(1,paramValue);
                        s.setString(2,paramName);
                        s.executeUpdate();                        
                    }
                });
                return rs.retCode;
            }
        });
		if(retCode != ErrorCanst.DEFAULT_SUCCESS){
			BaseEnv.log.error("EMailSettingMgt.updateParam() Error ");
			rs.retCode = retCode;
			return rs;
		}
		//清除规则和标签数据。
		mailParam.clear();
		mailLabels.clear();
		mailLabelMap.clear();
		return rs;
	}
	
	public Result listLabel(){
		List param = new ArrayList();
		String sql = " select id,name from tblmaillabel ";		
		return this.sqlList(sql, param);
	}
	
	public Result updateLabel(final String labelStr){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {                    	
                        Connection conn = connection;
                        String sql = "";
                        sql = "update tblMailSystemParam set paramValue=? where paramName=?";
                        PreparedStatement s = conn.prepareStatement(sql);
//                        s.setString(1,paramName);
//                        s.setString(1,paramValue);
                        s.executeUpdate();                        
                    }
                });
                return rs.retCode;
            }
        });
		if(retCode != ErrorCanst.DEFAULT_SUCCESS){
			BaseEnv.log.error("EMailSettingMgt.updateParam() Error ");
			rs.retCode = retCode;
			return rs;
		}
		return rs;
	}
	
	public Result listBlack(String email,String empName,int pageSize,int pageNo){
		List param = new ArrayList();
		String sql = " select a.email,b.empfullname,a.createTime from tblMailBlack a join tblemployee b on a.createBy = b.id where 1=1 ";
		if(email != null &&email.length() > 0){
			sql += " and a.email like ? ";
			param.add("%"+email+"%");
		}
		if(empName != null &&empName.length() > 0){
			sql += " and b.empfullname like ? ";
			param.add("%"+empName+"%");
		}
		return this.sqlList(sql, param, pageSize, pageNo, true);
	}
	
	public Result addBlack(final String email,final String userId) {		
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                    	
                        Connection conn = connection;
                        String sql = "";
                        sql = "select count(*) from tblMailBlack where email=?";
                        PreparedStatement s = conn.prepareStatement(sql);
                        s.setString(1,email);
                        ResultSet rset = s.executeQuery();
                        if(rset.next()){
                        	int count = rset.getInt(1);
                        	if(count > 0 ){
                        		rs.retCode = ErrorCanst.MULTI_VALUE_ERROR;
                        		return;
                        	}
                        }
                        sql = "  insert tblMailBlack(email,createBy,createTime) values(?,?,?) ";
                        s = conn.prepareStatement(sql);
                        s.setString(1,email);
                        s.setString(2,userId);
                        s.setString(3,BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
                        s.executeUpdate();  
                    }
                });
                return ErrorCanst.DEFAULT_SUCCESS;
            }
        });
		if(retCode != ErrorCanst.DEFAULT_SUCCESS){
			BaseEnv.log.error("EMailSettingMgt.addBlack() Error ");
			rs.retCode = retCode;
			return rs;
		}
		return rs;
	}
	public Result deleteBlack(final String email) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                    	
                        Connection conn = connection;
                        String sql = "";
                        
                        sql = "  delete tblMailBlack where email = ? ";
                        PreparedStatement s = conn.prepareStatement(sql);
                        s.setString(1,email);
                        s.executeUpdate();  
                    }
                });
                return ErrorCanst.DEFAULT_SUCCESS;
            }
        });
		if(retCode != ErrorCanst.DEFAULT_SUCCESS){
			BaseEnv.log.error("EMailSettingMgt.deleteBlack() Error ");
			rs.retCode = retCode;
			return rs;
		}
		return rs;
	}

}
