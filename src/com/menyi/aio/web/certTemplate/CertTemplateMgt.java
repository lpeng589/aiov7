package com.menyi.aio.web.certTemplate;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.bean.ModuleBean;
import com.menyi.aio.bean.UnitBean;
import com.menyi.aio.web.certificate.CertificateBillBean;
import com.menyi.aio.web.certificate.CertificateTemplateBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.SystemState;

/**
 * <p>Title: </p>
 *
 * <p>Description: 单位管理的接口类</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class CertTemplateMgt extends DBManager  {

    /**
     * 填加一条单位记录
     * @param id long
     * @param name String
     * @return Result
     */
    public Result save(final CertificateBillBean bean,final boolean isadd) {
    	final Result rs=new Result();
		   int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) throws
	                           SQLException {
	                       try {
	                    	   FileOutputStream fos = null;
	                    	   //if(SystemState.instance.develope){
	                    		   fos = new FileOutputStream("../../website/certTemplate.sql",true);
	                    	   //}
	                    	   
	                    	   
	                    	   if(!isadd){
	                    		   //修改前先删除，再插入
	                    		   String selSql = "delete certificateBill where id='"+bean.getId()+"' " ;
	                    		   //if(SystemState.instance.develope){
	                    			   fos.write((selSql+"\n").getBytes());
	                    		   //}
		                    	   PreparedStatement ps2 = conn.prepareStatement(selSql) ;
		                    	   ps2.executeUpdate() ;
	                    	   }
	                    	   String selSql = "insert into certificateBill(tempNumber,tempName,popupName,credTypeID,tableName,type,id) " +
	                    	   		" values('"+bean.getTempNumber()+"','"+bean.getTempName()+"','"+bean.getPopupName()+
	                    	   		"','"+bean.getCredTypeID()+"','"+bean.getTableName()+"','"+bean.getType()+"','"+bean.getId()+"')" ;
	                    	   //if(SystemState.instance.develope){
                    			   fos.write((selSql+"\n").getBytes());
                    		   //}
	                    	   PreparedStatement ps2 = conn.prepareStatement(selSql) ;
	                    	   ps2.executeUpdate() ;
	                    	   
	                    	   selSql = "delete certificateTemplate where f_ref='"+bean.getId()+"' " ;
	                    	   //if(SystemState.instance.develope){
                    			   fos.write((selSql+"\n").getBytes());
                    		   //}
	                    	   ps2 = conn.prepareStatement(selSql) ;
	                    	   ps2.executeUpdate() ;
	                    	   for(CertificateTemplateBean tb:bean.getTempList()){
	                    		   
	                    		   selSql = "insert into certificateTemplate(id,tableName,accCode,dirc,fieldName,curFieldName,companyCode,departmentCode,employee,stockCode,goodsCode,currency,CurrencyRate,projectCode,comment ,f_ref) " +
	                    		   		" values('"+tb.getId()+"','"+tb.getTableName()+"','"+tb.getAccCode().replace("'", "''")+"','"+tb.getDirc()+"','"+tb.getFieldName().replace("'", "''")+"','"+tb.getCurFieldName().replace("'", "''")+"','"+tb.getCompanyCode()+
	                    		   		"','"+tb.getDepartmentCode()+"','"+tb.getEmployeeID()+"','"+tb.getStockCode()+"','"+tb.getGoodsCode()+"','"+tb.getCurrency()+"','"+tb.getCurrencyRate()+"','"+
	                    		   		tb.getProjectCode()+"','"+tb.getComment()+"','"+bean.getId()+"')" ;
	                    		   
	                    		   //if(SystemState.instance.develope){
	                    			   fos.write((selSql+"\n").getBytes());
	                    		   //} 
	                    		   ps2 = conn.prepareStatement(selSql) ;
	                    		   BaseEnv.log.debug("CertTemplateMgt.save selSql="+selSql);
		                    	   ps2.executeUpdate() ;
	                    	   }
	                    	   fos.close();
	                       } catch (Exception ex) {
	                    	   BaseEnv.log.error("CertTemplateMgt.update-------",ex) ;
	                           rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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
     * 删除多条单位记录
     * @param ids long[]
     * @return Result
     */
    public Result delete(final String ids) {
    	final Result rs=new Result();
		   int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) throws
	                           SQLException {
	                       try {
	                    	   String selSql = "select tempName from  certificateBill where id=?" ;
	                    	   PreparedStatement ps2 = conn.prepareStatement(selSql) ;
	                    	   ps2.setString(1, ids);
	                    	   ResultSet res =ps2.executeQuery() ;
	                    	   if(res.next()){
	                    		   rs.retVal = res.getString(1);
	                    	   }
	                    	   
	                    	   selSql = "delete certificateBill where id=?" ;
	                    	   ps2 = conn.prepareStatement(selSql) ;
	                    	   ps2.setString(1, ids);
	                    	   ps2.executeUpdate() ;
	                    	   
	                    	   selSql = "delete certificateTemplate where f_ref=? " ;
	                    	   ps2 = conn.prepareStatement(selSql) ;
	                    	   ps2.setString(1, ids);
	                    	   ps2.executeUpdate() ;
	                       } catch (Exception ex) {
	                    	   BaseEnv.log.error("CertTemplateMgt.update-------",ex) ;
	                           rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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
     * 查询单位记录
     * @param name String
     * @param pageNo int
     * @param pageSize int
     * @return Result
     */
    public Result query(final String type) {
    	final Result rs=new Result();
		   int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) throws
	                           SQLException {
	                       try {
	                    	   String selSql = "select id,tempNumber,tempName,popupName,credTypeID,tableName from certificateBill  "+
	                    	   (type==null?"":"where type="+type)+" order by tempName " ;
	                    	   PreparedStatement ps2 = conn.prepareStatement(selSql) ;
	                    	   
	                    	   ResultSet rss = ps2.executeQuery() ;
	                    	   ArrayList list = new ArrayList();
	                    	   
	                    	   while(rss.next()){
	                    		   CertificateBillBean bean = new CertificateBillBean();
	                    		   bean.setId(rss.getString("id"));
	                    		   bean.setPopupName(rss.getString("popupName"));
	                    		   bean.setTempNumber(rss.getString("tempNumber"));
	                    		   bean.setTempName(rss.getString("tempName"));
	                    		   bean.setCredTypeID(rss.getString("credTypeID"));
	                    		   bean.setTableName(rss.getString("tableName"));
	                    		   list.add(bean);
	                    	   }
	                    	   rs.retVal = list;
	                       } catch (Exception ex) {
	                    	   BaseEnv.log.error("CertTemplateMgt.-------getBillList",ex) ;
	                           rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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
     * 查一条单位的详细信息
     * @param notepadId long 代号
     * @return Result 返回结果
     */
    public Result detail(final String tempNumber) {
    	final Result rs=new Result();
		   int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) throws
	                           SQLException {
	                       Result r = detail(tempNumber,conn);
	                       rs.retCode = r.retCode;
	                       rs.retVal = r.retVal;
	                   }
	               });
	               return rs.getRetCode();
	           }
	       });
	       rs.setRetCode(retCode);
	       return rs;
    }
    /**
     * 查一条单位的详细信息
     * @param notepadId long 代号
     * @return Result 返回结果
     */
    public Result detail(final String tempNumber,Connection conn) {
    	final Result rs=new Result();
        try {
    	   String selSql = "select id,tempNumber,tempName,popupName,credTypeID,tableName,type from certificateBill where tempNumber=?" ;
    	   PreparedStatement ps2 = conn.prepareStatement(selSql) ;
    	   ps2.setString(1, tempNumber);
    	   ResultSet rss = ps2.executeQuery() ;
    	   
    	   if(rss.next()){
    		   CertificateBillBean bean = new CertificateBillBean();
    		   bean.setId(rss.getString("id"));
    		   bean.setPopupName(rss.getString("popupName"));
    		   bean.setTempNumber(rss.getString("tempNumber"));
    		   bean.setTempName(rss.getString("tempName"));
    		   bean.setCredTypeID(rss.getString("credTypeID"));
    		   bean.setTableName(rss.getString("tableName"));	     
    		   bean.setType(rss.getString("Type"));	        
    		   rs.retVal =bean;
    		   
    		   selSql = "select id,tableName,accCode,dirc,fieldName,curFieldName,companyCode,departmentCode,employee,stockCode,goodsCode,currency,CurrencyRate,projectCode,comment from certificateTemplate where f_ref=?" ;
        	   ps2 = conn.prepareStatement(selSql) ;
        	   ps2.setString(1, bean.getId());
        	   rss = ps2.executeQuery() ;
        	   while(rss.next()){
        		   CertificateTemplateBean tb = new CertificateTemplateBean();
        		   tb.setId(rss.getString("id"));
        		   tb.setTableName(rss.getString("tableName"));
        		   tb.setAccCode(rss.getString("accCode"));
        		   tb.setDirc(rss.getInt("dirc"));
        		   tb.setFieldName(rss.getString("fieldName"));
        		   tb.setCurFieldName(rss.getString("curFieldName"));
        		   tb.setCompanyCode(rss.getString("companyCode"));
        		   tb.setDepartmentCode(rss.getString("departmentCode"));
        		   tb.setEmployeeID(rss.getString("employee"));
        		   tb.setStockCode(rss.getString("stockCode"));
        		   tb.setGoodsCode(rss.getString("goodsCode"));
        		   tb.setCurrency(rss.getString("currency"));
        		   tb.setCurrencyRate(rss.getString("CurrencyRate"));
        		   tb.setProjectCode(rss.getString("projectCode"));
        		   tb.setComment(rss.getString("comment"));
        		   bean.getTempList().add(tb);
        	   }
    	   }
       } catch (Exception ex) {
    	   BaseEnv.log.error("CertTemplateMgt.detail-------",ex) ;
           rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
       }
	   return rs;
    }
    
	/**
	 * 取所有主模块
	 * @return
	 */
	public ArrayList  queryModuleType(String locale) {
		ArrayList list = new ArrayList();
		String moduleType = "1";
		boolean hasData=false;
		ArrayList mbean = (ArrayList) BaseEnv.moduleMap.get(moduleType);
		HashMap map = new HashMap();
		for (Object o : mbean) {
			ModuleBean bean = (ModuleBean) o;
			if ((moduleType.equals(bean.getMainModule()) || "0".equals(bean.getMainModule()))) {			
				getChildModuleType(bean,list,moduleType,locale) ;
			}
		}
		return list;
	}
	
	/**
	 * @param bean ModuleBean
	 * @param list ArrayList
	 */
	private boolean getChildModuleType(ModuleBean bean, ArrayList list, String moduleType,String locale) {
		if (bean == null || bean.getChildList() == null || bean.getChildList().size() == 0) {
			return false;
		}
		boolean hasData=false;
		for (Object o : bean.getChildList()) {
			ModuleBean childBean = (ModuleBean) o;
			if ((moduleType.equals(childBean.getMainModule()) || childBean.getMainModule().equals("0")) && childBean.getLinkAddress() != null && 
					childBean.getIsHidden()==2 && childBean.getLinkAddress().length() > 0 ) {
				if(childBean.getLinkAddress().indexOf("/UserFunctionQueryAction.do") > -1){
					//如果表，要过滤掉基础表
					Pattern pattern = Pattern.compile("tableName=([^&]*)");
					Matcher matcher = pattern.matcher(childBean.getLinkAddress());
					String tableName="";
					if (matcher.find()) {
						tableName = matcher.group(1);
					}
					DBTableInfoBean tBean =GlobalsTool.getTableInfoBean(tableName);
					if(tBean != null && tBean.getIsBaseInfo() != 1 && tBean.getIsView() != 1 && !tBean.getTableName().equals("tblBillView")){
						list.add(new KeyPair(childBean.getModelDisplay().get(locale),tBean.getTableName()));
					}
				}
			}
			getChildModuleType(childBean, list, moduleType,locale);
		}
		return hasData;
	}
    
    
}
