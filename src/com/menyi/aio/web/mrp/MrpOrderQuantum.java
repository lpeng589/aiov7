package com.menyi.aio.web.mrp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ReportField;
import com.menyi.web.util.IDGenerater;;
/**
 * 
 * @author  
 *
 */
public class MrpOrderQuantum extends DBManager {
	public Result orderQuantum(final ArrayList orderBomIds,final String createBy,final HashMap<String,ArrayList> orderMap){		
		final Result rs=new Result();
		int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection connection) throws
	                           SQLException {
	                	   try{
		                       Connection conn = connection;
		                       ArrayList<GoodsPropInfoBean> propBeans=BaseEnv.propList;
		                       String propField="";
		                       for(int i=0;i<propBeans.size();i++){
		                    	   GoodsPropInfoBean propBean=propBeans.get(i);
		                    	   if(propBean.getIsUsed()==2&&!propBean.getPropName().equals("Seq")){
		                    		   propField+=","+propBean.getPropName();
		                    	   }
		                       }
		                       
		                       CallableStatement cst;
		               		   for(int i=0;i<orderBomIds.size();i++){//所选的订单的成品对应的BOM单，在此先将所运算的订单的成品的物料保存到“销售订单分配量”
			               			String []orderBom=(String[])orderBomIds.get(i);
		               				String trackNo=orderBom[0];
		               				String goodsCode=orderBom[1];
		               				String bomId=orderBom[2];
		               				cst = conn.prepareCall("{call proc_insertOrderQuantum(?,?,?,?,?)}");
		               				cst.setString(1, bomId);
		               				cst.setString(2, createBy);
		               				cst.setString(3, trackNo);
		               				cst.setString(4, goodsCode);
		               				cst.registerOutParameter(5, Types.INTEGER);
		               				cst.execute();
		               				
		               				ArrayList bomDetList=orderMap.get(trackNo);
		               				for(int j=0;j<bomDetList.size();j++){
		               					HashMap detMap=(HashMap)bomDetList.get(j);
		               					cst = conn.prepareCall("{call proc_updateSalesOrderQuantum(?,?,?,?,?,?,?,?,?)}");
		               					cst.setString(1, detMap.get("TrackNo").toString());
		               					cst.setString(2, detMap.get("GoodsCode").toString());
		               					cst.setString(3,"add");
		               					cst.setDouble(4, Double.parseDouble(detMap.get("stockQuantum").toString()));
		               					cst.setDouble(5, 0d);
		               					cst.setDouble(6, Double.parseDouble(detMap.get("buyingNum").toString()));
		               					cst.setDouble(7, Double.parseDouble(detMap.get("proingNum").toString()));
		               					cst.setDouble(8, 0d);
		               					cst.registerOutParameter(9, Types.INTEGER);
		               					cst.registerOutParameter(10, Types.STRUCT);
		               					cst.execute();
		               				}
		               		  }		               		   
	                	   }catch(Exception ex){
	                		   ex.printStackTrace();
	                	   }
	                   }
	               });
	               return rs.getRetCode();
	           }
	       });
	       rs.setRetCode(retCode);
		return rs;
	}
	
	public void insertOrderQuantum(String orderId,String propField,String createBy,String goodsCode,Connection conn,String[]propFieldVal)throws Exception{
		String sql="insert into tblSalesOrderQuantum (id,salesOrderID,goodsCode,salesOrderQty,stockQuantum,orderQuantum,buyIngNum,proingNum,createBy,statusId,stockName,levelCount"
				+propField+") values (?,?,?,?,?,?,?,?,?,?,?,?";
		for(int j=0;j<propField.split(",").length;j++){
			sql+=",?";
		}
		sql+=")";
		PreparedStatement ps=conn.prepareStatement(sql);
		ps.setString(1, IDGenerater.getId());
		ps.setString(2, orderId);
		ps.setString(3, goodsCode);
		ps.setDouble(4, 0) ;
		ps.setDouble(5, 0) ;
		ps.setDouble(6, 0) ;
		ps.setDouble(7, 0) ;
		ps.setDouble(8, 0) ;
		ps.setString(9, createBy);
		ps.setInt(10, 0);
		ps.setString(11, "");
		ps.setInt(12, 0);
		for(int j=0;j<propField.split(",").length;j++){
			ps.setString(j+13,propFieldVal[j]);
		}	
		ps.execute();
	}
}
