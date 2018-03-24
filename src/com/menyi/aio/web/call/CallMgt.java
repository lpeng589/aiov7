package com.menyi.aio.web.call;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.dbfactory.Result;
import com.menyi.web.util.ErrorCanst;
/**
 * <p>Title: </p>
 *
 * <p>Description: 来电弹屏信息管理</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 科荣软件</p>
 *
 * @author 周世伟
 * @version 1.0
 */
public class CallMgt extends DBManager  {



    /**
     * 查询单位记录
     * @param txtRemote String来电号
     * @param pageNo int查询页
     * @param pageSize int 每页条数
     * @return Result查询结果 
     */
    public Result query(final String txtRemote,final  int pageNo,final  int pageSize) {
    	final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {

						Connection conn = connection;
						try {
							PreparedStatement cs = null;
							StringBuffer sqlb = new StringBuffer("select");
					        //要查询的数据字段
					        sqlb.append(" outInStock.BillDate,outInStock.BillNo,goods.goodsFullName");
					        sqlb.append(" ,goods.goodsSpec,unit.unitName");
					        sqlb.append(" ,outInStock.InstoreQty,outInStock.OutstoreQty,outInStock.InstorePrice");
					        sqlb.append(" ,outInStock.OutstorePrice");
					        sqlb.append(" ,employee.empFullName");
					        sqlb.append(" ,company.ClientFlag");//厂商类型 
					        
					        sqlb.append(" from tblCompany as company left join viewoutinstock as outInStock");
					        sqlb.append(" on company.classCode=outInStock.Company");
					        sqlb.append(" left join tblGoods goods on outInStock.GoodsCode=goods.classCode");
					        sqlb.append(" left join tblEmployee as employee on outInStock.EmployeeID=employee.id");
					        sqlb.append(" left join tblUnit as unit on goods.BaseUnit=unit.id ");
					        
					        sqlb.append(" where (company.ComTel='"+txtRemote+"' or company.ComContactorTel='"+txtRemote+"'");
					        sqlb.append(" or company.ComContactorMobile='"+txtRemote+"')");
					        //(按最近时间)排序
					        sqlb.append(" order by outInStock.BillDate desc");
							String sql = sqlb.toString();
							cs = conn.prepareStatement(sql);

							ResultSet rss = cs.executeQuery();
							List values = new ArrayList();
							ResultSetMetaData rsm = rss.getMetaData();
							int colsLen = rsm.getColumnCount();
							Map<String, String> map ;							
							while (rss.next()) {
								map = new HashMap<String, String>();
								for (int i = 0; i < colsLen; i++) {									
									map.put(rsm.getColumnName(i+1), rss.getString(i+1));
								}
								values.add(map);
							}
							
							//rs.setRetVal(values);
							setResultPageInfo(rs, values, pageNo, pageSize);

						} catch (SQLException ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							rs.setRetVal(ex.getMessage());
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
     * 查询该电话的公司
     * @return
     */
    public Result queryComByTel(final String tel){
    	final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {

						Connection conn = connection;
						String tels = "'"+tel.replaceAll(",", "','")+"'";
						try {
							PreparedStatement cs = null;
							String sql = "select id from tblCompany" +
									" where replace(comtel,'-','') in ("+tels+")" +
									" or replace(ComContactorTel,'-','') in("+tels+")" +
									" or replace(ComContactorMobile,'-','') in("+tels+")";
							cs = conn.prepareStatement(sql);

							ResultSet rss = cs.executeQuery();
							List<String> list = new ArrayList<String>();
							while (rss.next()) {
								list.add(rss.getString(1));
							}
							rs.setRealTotal(list.size());
							rs.setRetVal(list);

						} catch (SQLException ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							rs.setRetVal(ex.getMessage());
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
	 * 设置result中的分页信息
	 * @param rs
	 * @param pageSize
	 */
	public Result setResultPageInfo(Result rs,List list,int pageNo,int pageSize){
		if(list==null||list.size()==0){
			return rs;
		}
		rs.setRealTotal(list.size());
		rs.setPageSize(pageSize);

		int pageNos = pageNo > 0 ? pageNo : 1;
		double totalPage = (double) rs.getRealTotal()
				/ (double) rs.getPageSize();

		//rs.setPageNo(pageNos);
		
		rs.setTotalPage((int) Math.ceil(totalPage));
		pageNos = pageNos>rs.getTotalPage()?rs.getTotalPage():pageNos;
		rs.setPageNo(pageNos);
		int startNo = 1 + (rs.getPageNo() - 1)
		* rs.getPageSize();
		int endNo = rs.getPageSize() + (rs.getPageNo() - 1)
				* rs.getPageSize();
		
		ArrayList newList = new ArrayList();
		for (int i = startNo - 1; i < endNo
				&& i < list.size(); i++) {
			Object obj = list.get(i);
			newList.add(obj);
		}
		rs.setRetVal(newList);
		return rs;
	}
}
