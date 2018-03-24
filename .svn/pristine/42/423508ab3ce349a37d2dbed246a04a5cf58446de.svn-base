package com.koron.crm.robot;

import java.util.*;

import com.dbfactory.hibernate.DBManager;
import com.dbfactory.Result;
import com.koron.robot.InfoItem;
import com.menyi.web.util.ErrorCanst;
import com.menyi.aio.bean.EnumerateBean;
import org.hibernate.Session;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.dbfactory.DBCanstant;
import org.hibernate.Query;
import com.dbfactory.hibernate.IfDB;
import com.dbfactory.hibernate.DBUtil;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.ModuleOperationBean;
import com.menyi.aio.bean.UserModuleBean;
import com.menyi.aio.bean.ModuleBean;
import com.menyi.aio.bean.EmployeeBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.menyi.web.util.BaseEnv;
import java.sql.PreparedStatement;
import java.sql.Connection;
import org.hibernate.jdbc.Work;

import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.SystemState;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.dyndb.DDLOperation;

/**
 * Description: <br/>Copyright (C), 2008-2012, Justin.T.Wang <br/>This Program
 * is protected by copyright laws. <br/>Program Name: <br/>Date:
 *
 * @autor Justin.T.Wang yao.jun.wang@hotmail.com
 * @version 1.0
 */
public class RobotMgt extends DBManager {
	
	private Object lock = new Object() ;
//	/**
//	    * 插入CRM 客户资料
//	    * @param sqls
//	    * @return
//	    */
//	   public Result addRobotClient(final String keywords,String _clientName,final Vector<String> mfax,final String url,
//			   					   final Vector<String> mmobile,final Vector<String> memail,final Vector<String> mtelephone,
//			   					   final String dept,final String userId) {
//		   if(_clientName == null) _clientName = "";
//		   if(_clientName.length() >80){
//			   _clientName = _clientName.substring(0,80);
//		   }
//		   final String clientName = _clientName;
//	       final Result rs = new Result();
//	       	       
//	       int retCode = DBUtil.execute(new IfDB() {
//	           public int exec(Session session) {
//	               session.doWork(new Work() {
//	                   public void execute(Connection conn) {
//	                	   String sql = "" ;
//	                	   try{
//	                		   PreparedStatement pss = null;
//	                			 
//                    		   sql = "insert into tblRobotClient(id,keywords,name,url,tel,mobile,fax,email,state,createTime) values(?,?,?,?)" ;
//                    		   pss = conn.prepareStatement(sql) ;
//                    		   pss.setString(1, IDGenerater.getId()) ;
//                    		   pss.setString(2, clientId) ;
//                    		   pss.setString(3, dept) ;
//                    		   pss.setString(4, userId) ;
//                    		   pss.executeUpdate() ;
//	                	   }catch(Exception ex){
//	                		   ex.printStackTrace();
//	                		   rs.retCode = ErrorCanst.DEFAULT_FAILURE ;
//	                		   BaseEnv.log.debug("RobotMgt addClientInfo Error sql :"+sql);
//	                	   }
//	                   }
//	               });
//	               return rs.getRetCode();
//	           }
//	       });
//	       rs.setRetCode(retCode);
////	       if(retCode < 0){
////	    	   System.out.println("keywords:"+keywords+";clientName:"+clientName+";url:"+url);
////	       }
//	       return rs;
//	   }

	   /**
	    * 插入CRM 客户资料
	    * 
	    * 此方法做为同步方法调用
	    * @param sqls
	    * @return
	    */
	   public synchronized Result addClientInfo(final String keywords,String _clientName,final Vector<String> mfax,final String url,
			   					   final Vector<String> mmobile,final Vector<String> memail,final Vector<String> mtelephone,
			   					   final String dept,final String userId,final String departmentCode) {
		   
		   if(_clientName == null) _clientName = "";
		   _clientName = _clientName.trim();
		   if(_clientName.length() >80){
			   _clientName = _clientName.substring(0,80);
		   }
		   final String clientName = _clientName;
	       final Result rs = new Result();
	       
	       //System.out.println(InfoItem.getString(mobile)+":"+InfoItem.getString(telephone));
	       int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) {
	                	   String sql = "" ;
	                	   try{
	                		   DBFieldInfoBean dbField = GlobalsTool.getFieldBean("CRMClientInfo", "ClientNo") ;
	                		   String code =dbField.getDefValue(conn) ;
	                		   if(code!=null){	                    			
                					Result result = existBillNo("CRMClientInfo", code,dbField,conn) ;
                        			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
                        				//此单据已经存在
                        				code = dbField.getDefValue(conn) ;
                        			}
	                		   }
	                		   PreparedStatement pss = null;
	                		   sql = "select * from CRMClientInfoDet where (mobile is not null and mobile <> '' and ? like '%'+Mobile+'%') or (telephone is not null and telephone <> '' and ? like '%'+Telephone+'%') " ;
	                		   pss = conn.prepareStatement(sql) ;
//	                		   System.out.println(InfoItem.getString(mobile)+":"+InfoItem.getString(telephone));
	                		   String m1= InfoItem.getString(mmobile);
	                		   String m2= InfoItem.getString(mtelephone);
	                		   System.out.println(m1+":"+m2);
	                		   pss.setString(1, InfoItem.getString(mmobile)) ;
	                		   pss.setString(2, InfoItem.getString(mtelephone)) ;
	                		   ResultSet rss = pss.executeQuery() ;
	                		   if(!rss.next()){
	                			   String clientId = IDGenerater.getId() ;
	                    		   sql = "insert into CRMClientInfo(id,ClientNo,ClientName,Fax,Status,briefContent,sourceInfo,createBy,lastUpdateBy,createTime,lastUpdateTime,departmentCode,workFlowNode,workFlowNodeName,ClientType,Level,Scale,Emergency,ClientLabel,ModuleId) " +
	                    		   		"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	                    		   pss = conn.prepareStatement(sql) ;
	                    		   pss.setString(1, clientId) ;
	                    		   pss.setString(2, code) ;
	                    		   pss.setString(3, clientName) ;
	                    		   pss.setString(4, mfax.size() >0?mfax.get(0):"") ;
	                    		   pss.setInt(5, 1) ;
	                    		   pss.setString(6, "搜索关键字:"+keywords+"<br/> 来源网址 <a href='"+url+"' target=_blank>"+url+"</a>") ;
	                    		   pss.setString(7, "");
	                    		   pss.setString(8, userId);
                		    	   pss.setString(9, userId);
                		    	   pss.setString(10, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
                		    	   pss.setString(11, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
                		    	   pss.setString(12, departmentCode);
                		    	   pss.setString(13, "-1");
                		    	   pss.setString(14, "finish");
                		    	   pss.setString(15, "1");
                		    	   pss.setString(16, "2");
                		    	   pss.setString(17, "3");
                		    	   pss.setString(18, "1");
                		    	   pss.setString(19, "1");
                		    	   pss.setString(20, "1");
	                    		   pss.executeUpdate() ;
	                    		   /*单据编号是否启用单据编号连续*/
		               	           String isSequence = BaseEnv.systemSet.get("BillNoSequence").getSetting() ;
		               	           if("true".equals(isSequence)){
		               	         		updateNewCode(dbField.getDefaultValue(), conn) ;
		               	           }
	                    		   int i = 1;
	                    		   Vector<String> fax = (Vector<String>)mfax.clone();
	                    		   Vector<String> mobile = (Vector<String>)mmobile.clone();
	                    		   Vector<String> email = (Vector<String>)memail.clone();
	                    		   Vector<String> telephone = (Vector<String>)mtelephone.clone();
	                    		   while(fax.size() > 0 || mobile.size() >0|| email.size() > 0 ||telephone.size() >0){
	                    			   String s_fax = fax.size()>0?fax.remove(0):"";
	                    			   String s_mobile = mobile.size()>0?mobile.remove(0):"";
	                    			   String s_email = email.size()>0?email.remove(0):"";
	                    			   String s_telephone = telephone.size()>0?telephone.remove(0):"";
	                    			   
		                    		   sql = "insert into CRMClientInfoDet(id,f_ref,userName,Mobile,Telephone,ClientEmail) values(?,?,?,?,?,?)" ;
		                    		   pss = conn.prepareStatement(sql) ;
		                    		   pss.setString(1, IDGenerater.getId()) ;
		                    		   pss.setString(2, clientId) ; 
		                    		   pss.setString(3, "联系人"+i) ;
		                    		   pss.setString(4, s_mobile) ;
		                    		   pss.setString(5, s_telephone) ;
		                    		   pss.setString(6, s_email) ;
		                    		   pss.executeUpdate() ;
		                    		   i ++;
	                    		   }
	                    		   sql = "insert into CRMClientInfoEmp(id,f_ref,DepartmentCode,EmployeeID) values(?,?,?,?)" ;
	                    		   pss = conn.prepareStatement(sql) ;
	                    		   pss.setString(1, IDGenerater.getId()) ;
	                    		   pss.setString(2, clientId) ;
	                    		   pss.setString(3, dept) ;
	                    		   pss.setString(4, userId) ;
	                    		   pss.executeUpdate() ;
	                		   }else{
	                			   rs.retCode = ErrorCanst.MULTI_VALUE_ERROR ;
	                		   }
	                	   }catch(Exception ex){
	                		   ex.printStackTrace();
	                		   rs.retCode = ErrorCanst.DEFAULT_FAILURE ;
	                		   BaseEnv.log.debug("RobotMgt addClientInfo Error sql :"+sql);
	                	   }
	                   }
	               });
	               return rs.getRetCode();
	           }
	       });
	       rs.setRetCode(retCode);
	       
	       uploadbol88(keywords,memail,url,_clientName);
	       
//	       if(retCode < 0){
//	    	   System.out.println("keywords:"+keywords+";clientName:"+clientName+";url:"+url);
//	       }
	       return rs;
	   }
	   
	   public void uploadbol88(String keyWord,Vector<String>  memail,String urlStr,String title){
		   try {
			   if(memail.size() == 0){
				   return;
			   }
			   String em = "";
			   for(String emailaddr:memail){
				   if(emailaddr.trim().length() > 0){
					   em += "<e><d>"+URLEncoder.encode(emailaddr.trim(),"GB2312")+"</d><u>"+URLEncoder.encode(urlStr.trim(),"GB2312")+"</u><t></t></e>";
				   }
			   }
			   if(em.length() ==0){
				   return;
			   }
				URL url = new URL(BaseEnv.bol88URL + "/MailPool?op=up");
				HttpURLConnection url_con = (HttpURLConnection) url
						.openConnection();
				url_con.setRequestMethod("POST"); 
				url_con.setConnectTimeout(50000);//（单位：毫秒）jdk1.5换成这个,连接超时
				url_con.setReadTimeout(50000);//（单位：毫秒）jdk 1.5换成这个,读操作超时

				url_con.setDoOutput(true);
				String key =  "data=<k>"+keyWord+"</k>"+em;  
				url_con.getOutputStream().write(key.getBytes("GB2312"));   
				url_con.getOutputStream().flush(); 
				url_con.getOutputStream().close();

				InputStream in = url_con.getInputStream();
				byte[] b = new byte[8190];
				byte[] bs = new byte[0];
				int count = 0;
				while ((count = in.read(b)) > -1) {
					byte[] temp = bs;
					bs = new byte[temp.length + count];
					System.arraycopy(temp, 0, bs, 0, temp.length);
					System.arraycopy(b, 0, bs, temp.length, count);
				}
				String ret = new String(bs,"utf-8");
				in.close();
				url_con.disconnect();
				
			}catch (Exception e) {
				BaseEnv.log.error("RobotMgt.uploadbol88:", e);
			}
	   }
	   
	   /**
	    * 判断单据编号是否已经存在
	    * @param tableName
	    * @param parentCode
	    * @param fieldName
	    * @param urlValue
	    * @return
	    */
	   public Result existBillNo(String tableName,String billNo,
			   DBFieldInfoBean field,Connection conn) {
		    Result res = new Result() ;
	   		try{
	   			String sql="select * from "+tableName+" where "+field.getFieldName()+"='"+billNo+"'";
	   			Statement st=conn.createStatement();
	   			ResultSet rss=st.executeQuery(sql);
	   			if(rss.next()){
	   				res.retCode = ErrorCanst.DEFAULT_SUCCESS ;
	   			}else{
	   				res.retCode = ErrorCanst.DEFAULT_FAILURE ;
	   			}
	   		}catch (Exception ex){
	   			res.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
	   			ex.printStackTrace() ;
	   		}
		   	return res ;
	   }
	   
	   /**
	    * 更新当前的单据编号+1
	    * @param code
	    * @param clear
	    * @param length
	    * @return
	    */
	   public boolean updateNewCode(String defaultValue,Connection conn){
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
	       if(rs.retCode!=ErrorCanst.DEFAULT_SUCCESS){
	           return false ;
	       }else{
	           return true ;
	       }
	   }
}
