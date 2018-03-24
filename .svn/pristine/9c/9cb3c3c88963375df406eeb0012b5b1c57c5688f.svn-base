package com.menyi.aio.web.stockgoods;

import java.sql.*;
import java.util.*;
import java.util.Map.Entry;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.StockGoodsModuleBean;
import com.menyi.web.util.*;

/**
 * 库存商品图片操作类
 * <p>Title:库存商品mgt</p> 
 * <p>Description: </p>
 *
 * @Date:2013-08-08
 * @Copyright: 科荣软件
 * @Author fjj
 */
public class StockGoodsMgt extends AIODBManager{

	
	/**
	 * 查询库存商品数据
	 * @param conMap			搜索条件HashMap
	 * @return
	 */
	public Result queryStockGoods(HashMap searchMap,String fields,String otherField,String orders,int pageNo, int pageSize){
		StringBuffer sql = new StringBuffer("select tblGoods.id,"+fields+otherField+"row_number() over(order by");
		
		String orderValue = "";
		//排序
		if(orders == null || "".equals(orders)){
			sql.append(" finishTime desc");
			orderValue = "finishTime";
		}else{
			sql.append(orders.split(",")[0] + " " + orders.split(",")[1]);
			orderValue = orders.split(",")[0];
		}
		sql.append(") row_id from tblGoods ");
		if(otherField.length()>0){
			sql.append(" left join (select t.totalQty as LastQty,t.LastTwoQty as LastTwoQty,totalAmt as lastAmount,tblGoods.classCode from tblgoods join tblStocks t on tblGoods.classCode=substring(t.GoodsCode,1,len(tblGoods.classCode))) stock ON tblgoods.classCode=stock.classCode ");
		}
		sql.append(" where 1=1 ");
		//搜索
		Iterator iter = searchMap.entrySet().iterator();
		while(iter.hasNext()){
			Entry entry = (Entry)iter.next();
			if(entry.getValue() != null && !"".equals(entry.getValue())){
				sql.append(" AND "+entry.getKey()+" like '%"+entry.getValue()+"%'");
			}
		}
		if(otherField.length()>0){
			sql.append(" group by tblGoods.id,"+fields+orderValue);
		}
		System.out.println("库存商品查询sql:"+sql.toString());
		return sqlListMaps(sql.toString(), new ArrayList(), pageNo, pageSize) ;
	}
	
	/**
	 * 查询模板
	 * @param searchValue   关键字搜索
	 * @return
	 */
	public Result queryModule(){
		String hql = "FROM StockGoodsModuleBean ORDER BY createTime";
		List<String> param = new ArrayList<String>();
		return list(hql, param);
	}
	
	/**
	 * 保存模板
	 * @param bean			模板bean
	 * @return
	 */
	public Result addModule(StockGoodsModuleBean bean){
		return addBean(bean);
	}
	
	/**
	 * 修改模板
	 * @param bean			模板bean
	 * @return
	 */
	public Result updateModule(StockGoodsModuleBean bean){
		return updateBean(bean);
	}
	
	
	/**
	 * 根据Id加载模板数据
	 * @param id
	 * @return
	 */
	public Result loadModule(String id){
		return loadBean(id,StockGoodsModuleBean.class);
	}
	
	/**
	 * 删除模板
	 * @param id
	 * @return
	 */
	public Result deleteModule(final String id){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("DELETE FROM tblStockGoodsModule WHERE id=?");
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ps.setString(1, id);
							int count = ps.executeUpdate();
							result.setRetVal(count);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("StockGoodsMgt deleteModule:",ex) ;
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
