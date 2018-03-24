package com.menyi.aio.web.customize;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;


import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.ApplyGoodsDecBean;
import com.menyi.aio.bean.BuyOrderBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;

public class ApplyGoodsBillSum {

	/**
	 * 根据所选单据ID来查询商品汇总
	 * @param ids 单据id集合字符
	 * @return
	 */
	public Result searchApplyGoodsByids(String ids,int startNo,int endNo){
		final List<ApplyGoodsDecBean> goods = new ArrayList<ApplyGoodsDecBean>();
		final Result result = new Result();
		
		final StringBuffer sqlCount = new StringBuffer();
		sqlCount.append("select count(0) from (");
		sqlCount.append("select row_number() over(order by bill.GoodsCode) as row_No,tblGoods.GoodsNumber as goodsNo,tblGoods.GoodsFullName as goodsName,bill.Qty as Qty,");
		sqlCount.append("(isnull(stockTemp.InstoreQty,0)-isnull(stockTemp.OutstoreQty,0)) as stockQty from ");
		sqlCount.append("(select GoodsCode,sum(Qty) as Qty from tblApplyGoodsBillDet ");
		sqlCount.append("where f_ref in ("+ids+") group by GoodsCode) as bill ");
		sqlCount.append("left join (select stock.GoodsCode,sum(stock.InstoreQty) as InstoreQty,sum(stock.OutstoreQty) as OutstoreQty from ");
		sqlCount.append(" tblStockDet as stock group by stock.GoodsCode) as stockTemp ");
		sqlCount.append("on bill.GoodsCode = stockTemp.GoodsCode ");
		sqlCount.append("left join tblGoods on bill.GoodsCode = tblGoods.classCode");
		sqlCount.append(") as tempRow");
		
		final StringBuffer sql = new StringBuffer();
		sql.append("select * from (");
		sql.append("select row_number() over(order by bill.GoodsCode) as row_No,bill.GoodsCode,tblGoods.GoodsNumber as goodsNo,tblGoods.GoodsFullName as goodsName,bill.Qty as Qty,");
		sql.append("(isnull(stockTemp.InstoreQty,0)-isnull(stockTemp.OutstoreQty,0)) as stockQty from ");
		sql.append("(select GoodsCode,sum(Qty) as Qty from tblApplyGoodsBillDet ");
		sql.append("where f_ref in ("+ids+") group by GoodsCode) as bill ");
		sql.append("left join (select stock.GoodsCode,sum(stock.InstoreQty) as InstoreQty,sum(stock.OutstoreQty) as OutstoreQty from ");
		sql.append(" tblStockDet as stock group by stock.GoodsCode) as stockTemp ");
		sql.append("on bill.GoodsCode = stockTemp.GoodsCode ");
		sql.append("left join tblGoods on bill.GoodsCode = tblGoods.classCode");
		sql.append(") as tempRow ");
		sql.append(" where tempRow.row_No between "+startNo+" and "+ endNo);
		
		DBUtil.execute(new IfDB(){
			@Override
			public int exec(Session session) {
				session.doWork(new Work(){
					public void execute(Connection con) throws SQLException {
						Statement state = null;
						try {
							state = con.createStatement();
							ResultSet rs = state.executeQuery(sqlCount.toString());
							if(rs.next()){
								result.setTotalPage(Integer.parseInt(rs.getString(1)));
							}
							
							rs = state.executeQuery(sql.toString());
							while(rs.next()){
								goods.add(new ApplyGoodsDecBean(
										rs.getString("GoodsCode"),
										rs.getString("goodsNo"),
										rs.getString("goodsName"),
										rs.getInt("Qty"),
										rs.getInt("stockQty"),
										(rs.getInt("Qty") - rs.getInt("stockQty")) < 0?0:
											(rs.getInt("Qty") - rs.getInt("stockQty")),
										-1
										));
								
							}
							result.setRetVal(goods);
						} catch (Exception e) {
							
						}
					}
				});
				return 0;
			}
		});
		
		return result;
	}
	
	public Result searchByWorkFlowNode(String ids){
		final StringBuffer sql = new StringBuffer();
		sql.append("select count(0) from (");
		sql.append("select * from tblApplyGoodsBill where id in ("+ids+")) as temp ");
		sql.append("where (workFlowNodeName <> 'finish' and (workFlowNode is not null and len(workFlowNode)>0) or OrderStatus <> 0)");
		final Result result = new Result();
		
		DBUtil.execute(new IfDB(){
			@Override
			public int exec(Session session) {
				session.doWork(new Work(){
					public void execute(Connection con) throws SQLException {
						Statement state = con.createStatement();
						ResultSet rs = state.executeQuery(sql.toString());
						if(rs.next()){
							result.setRetVal(rs.getString(1));
						}
					}
				});
				return 0;
			}
		});
		return result;
	}

	/**
	 * 根据id修改单据的状态
	 * @param ids
	 * @return
	 */
	public Result updateApplyGoodsBillState(String ids){
		final String sql = "update tblApplyGoodsBill set OrderStatus = 1 where id in ("+ids+")";
		final Result result = new Result();
		
		DBUtil.execute(new IfDB(){
			@Override
			public int exec(Session session) {
				session.doWork(new Work(){
					public void execute(Connection con) throws SQLException {
						Statement state = con.createStatement();
						result.setRetVal(state.executeUpdate(sql));
					}
				});
				return 0;
			}
		});
		return result;
	}
	
	/**
	 * 根据最近采购来查询往来
	 * @param isBillDateGoodsNoes
	 * @return
	 */
	public Map<String, BuyOrderBean> getCompanyCodeByDate(final List isBillDateGoodsNoes){
		final Map<String, BuyOrderBean> companyCodes = new HashMap<String, BuyOrderBean>();
		final StringBuffer sql = new StringBuffer();
		sql.append("select top 1 buyOrder.CompanyCode,det.Unit,det.price from tblBuyOrder as buyOrder ");
		sql.append("left join tblBuyOrderDet det on buyOrder.id=det.f_ref where det.goodscode=? ");
		sql.append("and buyOrder.billDate= (select max(billDate) from (select billDate from tblBuyOrder where id in ( ");
		sql.append("select f_ref from tblBuyOrderDet where goodsCode = ?)) as temp) order by det.price");
		
		DBUtil.execute(new IfDB(){
			@Override
			public int exec(Session session) {
				session.doWork(new Work(){
					public void execute(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(sql.toString());
						for (Object object : isBillDateGoodsNoes) {
							ApplyGoodsDecBean bean = (ApplyGoodsDecBean) object;
							ps.setObject(1, bean.getGoodsCode());
							ps.setObject(2, bean.getGoodsCode());
							ResultSet rs = ps.executeQuery();
							if(rs.next()){
								companyCodes.put(object.toString(), 
										new BuyOrderBean(
												rs.getString("CompanyCode"),
												bean.getGoodsCode(),
												bean.getOOSNumber(),
												rs.getString("Unit"),
												rs.getDouble("price")
												)
										);
							}
						}
						
					}
				});
				return 0;
			}
		});
		
		return companyCodes;
	}
	
	/**
	 * 根据价格来查询往来
	 * @param isPriceGoodsNoes
	 * @return
	 */
	public Map<String, BuyOrderBean> getCompanyCodeByPrice(final List isPriceGoodsNoes){
		final Map<String, BuyOrderBean> companyCodes = new HashMap<String, BuyOrderBean>();
		final StringBuffer sql = new StringBuffer();
		sql.append("select top 1 buyOrder.CompanyCode,det.Unit,det.price from tblBuyOrder buyOrder ");
		sql.append("left join tblBuyOrderDet as det on buyOrder.id = det.f_ref ");
		sql.append("where det.goodsCode = ? and Price =  ");
		sql.append("(select min(Price) from tblBuyOrderDet where goodsCode = ?)");
		
		DBUtil.execute(new IfDB(){
			@Override
			public int exec(Session session) {
				session.doWork(new Work(){
					public void execute(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(sql.toString());
						for (Object object : isPriceGoodsNoes) {
							ApplyGoodsDecBean bean = (ApplyGoodsDecBean) object;
							ps.setObject(1, bean.getGoodsCode());
							ps.setObject(2, bean.getGoodsCode());
							ResultSet rs = ps.executeQuery();
							if(rs.next()){
								companyCodes.put(object.toString(), 
										new BuyOrderBean(
												rs.getString("CompanyCode"),
												bean.getGoodsCode(),
												bean.getOOSNumber(),
												rs.getString("Unit"),
												rs.getDouble("price")
												)
										);
							}
						}
					}
				});
				return 0;
			}
		});
		
		return companyCodes;
	}
	
	public Result addBuyOrder(final Map<String,List> buyOrders,final String id,final String scompanyID){
		final Result result = new Result();
		final StringBuffer sqlBuy = new StringBuffer();
		final StringBuffer sqlDet = new StringBuffer();
		
		sqlBuy.append("insert into tblBuyOrder (id,createBy,createTime,BillNo,BillDate,CompanyCode,EmployeeID,SCompanyID) ");
		sqlBuy.append("values (?,?,convert(varchar(19),getDate(),120),?,convert(varchar(10),getDate(),120),?,?,?)");
		
		sqlDet.append("insert into tblBuyOrderDet(id,f_ref,Qty,Price,Amount,GoodsCode,Unit,SCompanyID) ");
		sqlDet.append("values (?,?,?,?,?,?,?,?)");
		
		int retCode = DBUtil.execute(new IfDB(){
			@Override
			public int exec(Session session) {
				session.doWork(new Work(){
					public void execute(Connection con){
						PreparedStatement ps = null;
						try {
							
							for (String companyCode : buyOrders.keySet()) {
								ps = con.prepareStatement(sqlBuy.toString());
								String SID = IDGenerater.getId();
								ps.setString(1, SID);
								ps.setString(2, id);
								ps.setString(3, getClassCode(SID, "BO"));
								ps.setString(4, companyCode);
								ps.setString(5, id);
								ps.setString(6, scompanyID);
								ps.executeUpdate();
								
								for (Object object : buyOrders.get(companyCode)) {
									BuyOrderBean bean = (BuyOrderBean) object;
									ps = con.prepareStatement(sqlDet.toString());
									ps.setString(1, IDGenerater.getId());
									ps.setString(2, SID);
									ps.setInt(3, bean.getQty());
									ps.setDouble(4, bean.getPrice());
									ps.setDouble(5, bean.getQty()*bean.getPrice());
									ps.setString(6, bean.getGoodsCode());
									ps.setString(7, bean.getUnit());
									ps.setString(8, scompanyID);
									ps.executeUpdate();
								}
							}
						} catch (Exception e) {
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(e.getMessage());
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
		
	}
	
	public String getClassCode(String id,String No){
		StringBuffer classCode = new StringBuffer(No);
		classCode.append(new SimpleDateFormat("yyMMdd").format(new Date()));
		classCode.append(id.substring(id.length()-4));
		return classCode.toString();
		
	}
}
