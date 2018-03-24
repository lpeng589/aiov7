package com.menyi.aio.web.lrp;


import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.billNumber.BillNo;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.mobile.Message;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.web.util.AIOConnect;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConfirmBean;
import com.menyi.web.util.DefineSQLBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;


public class LrpMgt extends AIODBManager {
	private static Gson gson;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}
	DynDBManager dbmgt = new DynDBManager();
	
	Logger log= Logger.getLogger("LRPLog");

	UserFunctionMgt userFunMgt = new UserFunctionMgt();
	
	public Result update(final String tableName,final String fieldName,final String value,final String id,final String userId){
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                        	String val = value;
                        	if(fieldName.endsWith("Qty")){
                        		val=value.replace(",", "");
                        	}
                        	if(fieldName.equals("WOClassType") && tableName.equals("PDLrpProdPlan")){
                        		//如果工令类型改为自制时要检查，物料属性是否允许自制
                        		String sql  = " select attrType from tblGoods a join PDLrpProdPlan b on a.classCode=b.GoodsCode where b.id=?  ";
                        		PreparedStatement stmt =connection.prepareStatement(sql);
                            	stmt.setString(1, id);
                            	ResultSet rs = stmt.executeQuery();
                            	if(rs.next()){
                            		String attrType = rs.getString(1);
                            		if(!attrType.equals("4")){
                            			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            			rst.setRetVal("非万能码工令类型不可以修改");
                            			return;
                            		}
                            	}
                        	}
                        	//修改生产计划的生产数量，要重算的面所有计划单的数量
                        	if(fieldName.equals("LossBuyQty")){
                        		String sql = "select a.statusId from [PDLRP] a join PDLrpBuyPlan b on a.id=b.lrpId where b.id=?";
                        		PreparedStatement stmt =connection.prepareStatement(sql);
                            	stmt.setString(1, id);
                            	ResultSet rs = stmt.executeQuery();
                            	if(rs.next()){
                            		int statusId = rs.getInt(1);
                            		if(statusId != 0){
                            			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            			rst.setRetVal("已抛转工令或采购的计算单不能再修改数量");
                            			return;
                            		}
                            	}
                        	}
                        	//修改生产计划的生产数量，要重算的面所有计划单的数量
                        	if(fieldName.equals("LossProdQty")){
                            	//取出原记录的基本信息
                            	String sql = "select a.id computeId,a.BillNo computeNo,b.lrpId,a.isLoss,a.isLowQty,a.isReplace,b.parentProdId,b.startDate,"
                            			+ "b.endDate,b.billType,b.billId,b.billNo,c.sourceId,b.GoodsCode,b.BomId,b.BomGrade,c.statusId  "
                            			+ "from PDLRPCompute a join PDLrpProdPlan b on a.id=b.ComputerId "
                            			+ "join PDLrp c on b.LrpId=c.id where b.id=?";
                            	PreparedStatement stmt =connection.prepareStatement(sql);
                            	stmt.setString(1, id);
                            	ResultSet rs = stmt.executeQuery();
                            	String computeNo=null;
                            	String computeId=null;
                            	String lrpId=null;
                            	String isloss=null;
                            	String isLowQty=null;
                            	String isReplace=null;
                            	String planId=null;
                            	String startDate=null;
                            	String endDate=null;
                            	String billType=null;
                            	String billId=null;
                            	String billNo=null;
                            	String sourceId=null;
                            	String GoodsCode=null;
                            	String BomId=null;
                            	int bomGrade=0;
                            	if(rs.next()){
                            		int statusId = rs.getInt("statusId");
                            		if(statusId != 0){
                            			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            			rst.setRetVal("已抛转工令或采购的计算单不能再修改数量");
                            			return;
                            		}
                            		computeNo=rs.getString("computeNo");
                                	computeId=rs.getString("computeId");
                                	lrpId=rs.getString("lrpId");
                                	isloss=rs.getString("isloss");
                                	isLowQty=rs.getString("isLowQty");
                                	isReplace=rs.getString("isReplace");
                                	planId=rs.getString("parentProdId");
                                	startDate=rs.getString("startDate");
                                	endDate=rs.getString("endDate");
                                	billType=rs.getString("billType");
                                	billId=rs.getString("billId");
                                	billNo=rs.getString("billNo");
                                	sourceId=rs.getString("sourceId");
                                	GoodsCode=rs.getString("GoodsCode");
                                	BomId=rs.getString("BomId");
                                	bomGrade=rs.getInt("bomGrade");
                            	}
                            	//先删除后面的子工令级子工令的采购替换再重算
                        		delProdPlan(id, connection);
                        		
                        		compute(computeNo, computeId, lrpId, isloss, isLowQty, isReplace, planId, startDate, endDate, 
                	        			userId, billType, billId, billNo, sourceId, GoodsCode, Double.parseDouble(val), BomId, bomGrade+1,"", connection);
                        	}
                        	String sql  = "update "+tableName+" set "+fieldName+"=? where id=?";
                        	PreparedStatement stmt =connection.prepareStatement(sql);
                        	stmt.setString(1, val);
                        	stmt.setString(2, id);
                        	stmt.execute();
                        } catch (Exception ex) {
                            BaseEnv.log.error("LrpMgt.update",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            rst.setRetVal("执行失败"+ex.getMessage());
                            return;
                        }
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        return rst;
	}
	//删除生产计划级其子计划
	public void delProdPlan(String id,Connection connection) throws Exception{
		//查询有没有子计划
		String sql  = "select id from PDLrpProdPlan where parentProdId=?";
		PreparedStatement pstmt =connection.prepareStatement(sql);
    	pstmt.setString(1, id);
    	ResultSet rs =pstmt.executeQuery();
    	while(rs.next()){
    		delProdPlan(rs.getString(1),connection);
    	}
    	BaseEnv.log.debug("删除计划单，id："+id);
    	sql = " delete from PDLrpProdPlan where id=? ";
		pstmt = connection.prepareStatement(sql);
    	pstmt.setString(1, id);
    	pstmt.execute();
    	sql = " delete from PDLrpBuyPlan where ProdPlanId=? ";
		pstmt = connection.prepareStatement(sql);
    	pstmt.setString(1, id);
    	pstmt.execute();
    	sql = " delete from PDLrpReplacePlan where ProdPlanId=? ";
		pstmt = connection.prepareStatement(sql);
    	pstmt.setString(1, id);
    	pstmt.execute();
	}

	
	/**
	 * 运算各种库存量，并存在PDMRPStocks中，作为历史记录，也方便读取
	 * 计算方法：
	 * 1、当前库存 （当前实际库存部分）
	 * 2、当前欠料（当前实际库存部分）
	 * 3、在途量 =当前未结案请购单未入库部分
	 * 4、在制量 =当前未结案工令单
	 * 5、占用量（销售订单/制造需求单占用量+已请购量-已发料量）（当前未结案未终止的订单，如果已结案其占用的料自动释放为公共资源）
	 * a、在订单终止和自动结案时要终止其物料占用
	 * b、下请购单和订单要回填物料需求表相应字段
	 * c、发料，退料，超领，补料要回填物料需求表相应字段
	 * @param bomId
	 * @param mrpId
	 * @param parentCode
	 * @return
	 */
	public Result compute(final String computeNo,final String isloss,final String isLowQty,final String isReplace,final String billType,final String userId,final ArrayList<HashMap<String,String>> list){
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                        	String id = IDGenerater.getId();
                        	String sql  = "select id from  PDLRPCompute where BillNo=?";
                        	PreparedStatement stmt =connection.prepareStatement(sql);
                        	stmt.setString(1, computeNo);
                        	ResultSet rs =stmt.executeQuery();
                        	if(rs.next()){//如果编号存在，则不增加新的计算编号，直接返回
                        		id =rs.getString(1);
                        	}else{
	                        	sql  = "insert into PDLRPCompute(id,BillDate,BillNo,createBy,createTime,isloss, isLowQty, isReplace) values(?,?,?,?,?,?,?,?)";
	                        	stmt =connection.prepareStatement(sql);
	                        	stmt.setString(1, id);
	                        	stmt.setString(2, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
	                        	stmt.setString(3, computeNo);
	                        	stmt.setString(4, userId);
	                        	stmt.setString(5, BaseDateFormat.getNowTime());
	                        	stmt.setString(6, isloss);
	                        	stmt.setString(7, isLowQty);
	                        	stmt.setString(8, isReplace);
	                        	stmt.execute();
                        	}
                        	//检查是否已经运算过，如果有，先删除
                        	for(final HashMap<String,String> map :list){
                        		sql = "select id,statusId from [PDLRP] where [BillType]=? and [BillId]=? and [SourceId]=?";
                            	PreparedStatement pstmt = connection.prepareStatement(sql);
                            	pstmt.setString(1, billType);
                            	pstmt.setString(2, map.get("billId"));
                            	pstmt.setString(3, map.get("sourceId"));
                            	rs =pstmt.executeQuery();
                            	String oldId=null;
                            	int oldStatusId=0;
                            	if(rs.next()){
                            		oldId=rs.getString("id");
                            		oldStatusId=rs.getInt("statusId");
                            		
                            	}
                            	if(oldStatusId == 1){ //原单状态为已处理，则不能再重算
                            		rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                                    rst.setRetVal("单据编号"+map.get("billNo")+"的商品已执行计算且已全部或部分抛转为采购或工令单，不可以再重算");
                                    return;
                            	}
                            	if(oldId != null && oldStatusId==0){//已计算，且状态还未抛转
                            		//先删除再重算
                            		sql = " delete from PDLrpProdPlan where lrpId=? ";
                            		pstmt = connection.prepareStatement(sql);
                                	pstmt.setString(1, oldId);
                                	pstmt.execute();
                                	sql = " delete from PDLrpBuyPlan where lrpId=? ";
                            		pstmt = connection.prepareStatement(sql);
                                	pstmt.setString(1, oldId);
                                	pstmt.execute();
                                	sql = " delete from PDLrpReplacePlan where lrpId=? ";
                            		pstmt = connection.prepareStatement(sql);
                                	pstmt.setString(1, oldId);
                                	pstmt.execute();
                                	sql = " delete from PDLRP where id=? ";
                            		pstmt = connection.prepareStatement(sql);
                                	pstmt.setString(1, oldId);
                                	pstmt.execute();
                            	}
                            	//更新单据信息
                            	if(billType.equals("tblSalesOrder")){
                            		sql = " update tblSalesOrderDet set MRPQty=? where id=? ";
                        		}else if(billType.equals("PDPlan")){
                        			sql = " update PDPlan set MRPQty=? where id=? ";
                        		}else if(billType.equals("PDMRP")){
                        			sql = " update PDMRP set MRPQty=? where id=? ";
                        		}else if(billType.equals("PDProduceRequire")){
                        			 sql = " update PDProduceRequireDet set MRPQty=? where id=? ";
                        		}
                            	pstmt = connection.prepareStatement(sql);
                            	pstmt.setDouble(1, 0d);
                            	pstmt.setString(2, map.get("sourceId"));
                            	pstmt.execute();
                        	}
                        	
                        	rst.setRetVal(id);
                        } catch (Exception ex) {
                            BaseEnv.log.error("LrpMgt.compute",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            rst.setRetVal("执行失败"+ex.getMessage());
                            return;
                        }
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode); 
        if(retCode==ErrorCanst.DEFAULT_SUCCESS){
        	final String computeId = rst.getRetVal()+"";
        	for(final HashMap<String,String> map :list){
        		retCode = DBUtil.execute(new IfDB() {
                    public int exec(Session session) {
                        session.doWork(new Work() {
                            public void execute(Connection connection) throws
                                    SQLException {
                                try {
                                	//插入PDLRP记录运算信息
                                	String sql = "INSERT INTO [PDLRP]([id],[ComputerId],[BillType],[BillId],[SourceId],[GoodsCode],[Qty],[createBy],[createTime],BillNo,[statusId])"
                                			+ "values(?,?,?,?,?,?,?,?,?,?,0);";
                                	PreparedStatement pstmt = connection.prepareStatement(sql);
                                	String lrpId = IDGenerater.getId();
                                    pstmt.setString(1, lrpId);
                                    pstmt.setString(2, computeId);
                                    pstmt.setString(3, billType);
                                    pstmt.setString(4, map.get("billId"));
                                    pstmt.setString(5, map.get("sourceId"));
                                    pstmt.setString(6, map.get("GoodsCode"));
                                    pstmt.setString(7, map.get("qty"));
                                    pstmt.setString(8, userId);
                                    pstmt.setString(9, BaseDateFormat.getNowTime());
                                    pstmt.setString(10, map.get("billNo"));
                                    pstmt.execute();
                                    String merge= "";
                                    if(billType.equals("PDMRP")){
                                    	sql = " select merge from PDMRP where id=? ";
                                    	pstmt = connection.prepareStatement(sql);
                                        pstmt.setString(1, map.get("billId"));
                                        ResultSet rset = pstmt.executeQuery();
                                        if(rset.next()){
                                        	merge = rset.getInt(1)==1?"merge":"";
                                        }
                                    	
                                    }
                                    //查询是否合并BOM
                                	compute(computeNo,computeId,lrpId,isloss,isLowQty,isReplace,"",map.get("startDate"),map.get("endDate"),userId,billType, map.get("billId"), map.get("billNo"), map.get("sourceId"), map.get("GoodsCode"),Double.parseDouble(map.get("qty")),"",1,merge, connection);
                                	//更新单据信息
                                	if(billType.equals("tblSalesOrder")){
                                		sql = " update tblSalesOrderDet set MRPQty=? where id=? ";
                            		}else if(billType.equals("PDPlan")){
                            			sql = " update PDPlan set MRPQty=? where id=? ";
                            		}else if(billType.equals("PDMRP")){
                            			sql = " update PDMRP set MRPQty=? where id=? ";
                            		}else if(billType.equals("PDProduceRequire")){
                            			 sql = " update PDProduceRequireDet set MRPQty=? where id=? ";
                            		}
                                	pstmt = connection.prepareStatement(sql);
                                	pstmt.setDouble(1, Double.parseDouble(map.get("qty")));
                                	pstmt.setString(2, map.get("sourceId"));
                                	pstmt.execute();
                                } catch (Exception ex) {
                                    BaseEnv.log.error("LrpMgt.compute",ex);
                                    rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                                    rst.setRetVal("执行失败"+ex.getMessage());
                                    return;
                                }
                            }
                        });
                        return rst.getRetCode();
                    }
                });
                rst.setRetCode(retCode);
                if(rst.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
                	return rst;
                }
        	}
        }
        return rst;
	}
	
	public HashMap<String,BigDecimal > computeQty(final String computeNo,final String computeId,String lrpId,final String isloss,final String isLowQty,final String isReplace,final String parentPlanId,final String startDate,
			final String endDate,String userId, final String billType,final String billId,final String billNo,final String sourceId,
			final String GoodsCode,final Double qty,String bomId,final int bomGrade,Connection conn) throws Exception{
		HashMap<String,BigDecimal > map = new HashMap<String,BigDecimal >();
		map.put("StockQty", new BigDecimal("0"));
		map.put("curStocks", new BigDecimal("0"));
		map.put("oweStocks", new BigDecimal("0"));
		map.put("SafetyQty",new BigDecimal("0"));
		map.put("PlanProdQty",new BigDecimal("0"));
		map.put("PlanBuyQty",new BigDecimal("0"));
		map.put("PlanOutMaterials",new BigDecimal("0"));
		map.put("SalesQty",new BigDecimal("0"));
		map.put("ProdQty",new BigDecimal("0"));
        map.put("BuyQty",new BigDecimal("0"));
        map.put("OutMaterials",new BigDecimal("0"));
        map.put("ByReplace",new BigDecimal("0"));
        map.put("Replace",new BigDecimal("0"));
        //先计算9大量
		//1、当前库存
        String sql =" select isnull(sum(isnull(tblstocks.lastQty,0)),0) qty from "+
				"  tblStocks  "+
				"  join tblStock on tblStocks.StockCode=tblStock.classCode  where GoodsCode =? and isnull(tblstocks.lastQty,0) >0 and StockType='Main'";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, GoodsCode);
        ResultSet rset = pstmt.executeQuery();
        if(rset.next()){
        	map.put("curStocks",rset.getBigDecimal(1));
        }
        log.debug(computeNo+" 类型："+billType+",单据:"+billNo+",BOM级别:"+bomGrade+",商品:"+GoodsCode+",需求数量："+qty+"..当前库存："+map.get("curStocks"));
        //当前欠料
        sql = " select isnull(sum(isnull(tblstocks.OweQty,0)),0) qty from "+
				"  tblStocks"+
				"  join tblStock on tblStocks.StockCode=tblStock.classCode  where GoodsCode =? and isnull(tblstocks.OweQty,0) > 0 and StockType !='Main' and  StockType !='Client' ";

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, GoodsCode);
        rset = pstmt.executeQuery();
        if(rset.next()){
        	map.put("oweStocks",rset.getBigDecimal(1));
        }
        log.debug(computeNo+" 类型："+billType+",单据:"+billNo+",BOM级别:"+bomGrade+",商品:"+GoodsCode+",需求数量："+qty+"..当前欠料："+map.get("oweStocks"));
        //安全库存
        sql = " select isnull(lowerQty,0) qty from "+
				"  tblGoods where classCode =? ";

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, GoodsCode);
        rset = pstmt.executeQuery();
        if(rset.next()){
        	map.put("SafetyQty",rset.getBigDecimal(1));
        }
        log.debug(computeNo+" 类型："+billType+",单据:"+billNo+",BOM级别:"+bomGrade+",商品:"+GoodsCode+",需求数量："+qty+"..安全库存："+map.get("SafetyQty"));
        //计划生产量=已进行MRP运算，产生生产计划，但未抛转成工令单。
        sql = " select isnull(sum(LossProdQty),0) qty from "+
				"  PDLrpProdPlan where GoodsCode =? and statusId=0 ";

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, GoodsCode);
        rset = pstmt.executeQuery();
        if(rset.next()){
        	map.put("PlanProdQty",rset.getBigDecimal(1));
        }
        log.debug(computeNo+" 类型："+billType+",单据:"+billNo+",BOM级别:"+bomGrade+",商品:"+GoodsCode+",需求数量："+qty+"..计划生产量："+map.get("PlanProdQty"));
//		计划采购量=已进行MRP运算，产生采购计划，但未抛转成请购单。
        sql = " select isnull(sum(LossBuyQty),0) qty from "+
				"  PDLrpBuyPlan where GoodsCode =? and statusId=0 ";

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, GoodsCode);
        rset = pstmt.executeQuery();
        if(rset.next()){
        	map.put("PlanBuyQty",rset.getBigDecimal(1));
        }
        log.debug(computeNo+" 类型："+billType+",单据:"+billNo+",BOM级别:"+bomGrade+",商品:"+GoodsCode+",需求数量："+qty+"..计划采购量："+map.get("PlanBuyQty"));
//		计划领料量=已进行MRP运算，产生生产计划，但未抛转成工令单的原材料的占用量。这里要计算毛需求量，因为这才是真正要领的数量，如果有库存，则生产量就减少，但毛需求不变---错
        //改进后算法，未抛转的工令单的原料需求
        BigDecimal PlanOutMaterials = new BigDecimal(0);
		sql = "select isnull(sum(a.GrossReqQty-a.ByReplace),0) qty "
				+ "from PDLrpBuyPlan a join PDLrpProdPlan b on a.ProdPlanId=b.id  where a.GoodsCode =? and b.statusId=0 ";
		pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, GoodsCode);
        rset = pstmt.executeQuery();
        if(rset.next()){
        	PlanOutMaterials=PlanOutMaterials.add(rset.getBigDecimal(1));
        }
		sql = "select isnull(sum(replaceQty),0) qty"
				+ " from PDLrpReplacePlan a  join PDLrpProdPlan b on a.ProdPlanId=b.id where a.GoodsCode =? and b.statusId=0 ";
		pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, GoodsCode);
        rset = pstmt.executeQuery();
        if(rset.next()){
        	PlanOutMaterials=PlanOutMaterials.add(rset.getBigDecimal(1));
        }
		sql = "select isnull(sum(a.GrossReqQty-a.ByReplace),0) qty "
				+ "from PDLrpProdPlan a  join PDLrpProdPlan b on a.parentProdId=b.id  where a.GoodsCode =? and b.statusId=0 ";
		pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, GoodsCode);
        rset = pstmt.executeQuery();
        if(rset.next()){
        	PlanOutMaterials=PlanOutMaterials.add(rset.getBigDecimal(1));
        }
        map.put("PlanOutMaterials",PlanOutMaterials);
        log.debug(computeNo+" 类型："+billType+",单据:"+billNo+",BOM级别:"+bomGrade+",商品:"+GoodsCode+",需求数量："+qty+"..计划领料量："+map.get("PlanOutMaterials"));
//		预计销货量=未结订单的预计订单量(已审核的订单预计出货量-已出货量-借出未销数量)。
//	    	已参与MRP运算的销售订单，如果已做生产规划的按已做生产规划数量算
        //订单运算后回填MRPQty,规划单回填PlanQty,PlanQty 大于0，MRPQty就必须等于0，MRPQty大于0.PlanQty就必须等于0，规划单运算后不会回填对应订单的MRPQty
        //销售订单,未做生产规划，已做lrp运算，但不包括自己
        sql = " select isnull(sum(isnull(tblSalesOrderDet.MRPQty,0) -tblSalesOrderDet.OutQty),0) qty from "+
				"  tblSalesOrder join tblSalesOrderDet on tblSalesOrder.id=tblSalesOrderDet.f_ref  "
				+ " where tblSalesOrderDet.GoodsCode =? and tblSalesOrder.statusId=0 and tblSalesOrder.workFlowNodeName='finish' and "
				+ "isnull(tblSalesOrderDet.MRPQty,0)>0 and isnull(tblSalesOrderDet.PlanQty,0)=0 and tblSalesOrderDet.id <>? ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, GoodsCode);
        pstmt.setString(2, sourceId);
        rset = pstmt.executeQuery();
        BigDecimal salesQty = new BigDecimal(0);
        if(rset.next()){
        	salesQty = salesQty.add(rset.getBigDecimal(1));
        }
        //生产规划单,已做运算不包括自己总数减对应所有订单的已出库数
        sql = " select isnull(sum(sumd-outQ),0) qty from (  select sum(PDPlanDet.Qty) sumd,tblSalesOrderDet.id ,isnull( tblSalesOrderDet.OutQty,0) outQ   from "+
				"  PDPlanDet join tblSalesOrderDet on tblSalesOrderDet.f_ref=PDPlanDet.SalesOrderID and tblSalesOrderDet.id = PDPlanDet.SourceId  "
				+ " join tblSalesOrder on tblSalesOrder.id=tblSalesOrderDet.f_ref and tblSalesOrder.statusId=0  and tblSalesOrder.workFlowNodeName='finish'   "
				+ " join PDPlan on PDPlanDet.f_ref=PDPlan.id and isnull(PDPlan.MRPQty,0)>0 and PDPlan.id <> ? "
				+ " where tblSalesOrderDet.GoodsCode =? group by tblSalesOrderDet.id , tblSalesOrderDet.OutQty ) a ";

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, sourceId);
        pstmt.setString(2, GoodsCode);
        rset = pstmt.executeQuery();
        if(rset.next()){
        	salesQty = salesQty.add(rset.getBigDecimal(1));
        }
        map.put("SalesQty",salesQty);
        log.debug(computeNo+" 类型："+billType+",单据:"+billNo+",BOM级别:"+bomGrade+",商品:"+GoodsCode+",需求数量："+qty+"..预计销货量："+map.get("SalesQty"));
//		预计生产量=未结工单的未完工量(工令单预计生产量-已生产量)
        sql = " select isnull(sum(Qty-isnull(InPayQty,0)),0) qty from "+
				"  PDWorkOrder where GoodsCode =? and  PDWorkOrder.workFlowNodeName!='draft' and PDWorkOrder.statusId not in (1,2) ";

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, GoodsCode);
        rset = pstmt.executeQuery();
        if(rset.next()){
        	map.put("ProdQty",rset.getBigDecimal(1));
        }
        log.debug(computeNo+" 类型："+billType+",单据:"+billNo+",BOM级别:"+bomGrade+",商品:"+GoodsCode+",需求数量："+qty+"..预计生产量："+map.get("ProdQty"));
//		采购在途量=所有未完请购量(请购量-已入库量)。
        sql = " select isnull(sum(ISNULL( tblBuyApplicationDet.NotInQty,0)),0) qty from "+
				" tblBuyApplication join tblBuyApplicationDet on tblBuyApplication.id=tblBuyApplicationDet.f_ref "+ 
				" where  tblBuyApplication.statusId not in (1,2) and tblBuyApplication.workflowNodeName!='draft' "
				+ "  and  tblBuyApplicationDet.GoodsCode=? ";

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, GoodsCode);
        rset = pstmt.executeQuery();
        if(rset.next()){
        	map.put("BuyQty",rset.getBigDecimal(1));
        }
        log.debug(computeNo+" 类型："+billType+",单据:"+billNo+",BOM级别:"+bomGrade+",商品:"+GoodsCode+",需求数量："+qty+"..采购在途量："+map.get("BuyQty"));
//		预计领料量=未结工单的预计领料量(已发放工单的预计领用量-已领用量)
        sql = " select isnull(sum(ISNULL( SendQtyExt,0)),0) qty from "+
				"   PDWorkOrder join PDWorkOrderDet on PDWorkOrder.id = PDWorkOrderDet.f_ref "
				+ " where PDWorkOrderDet.GoodsCode =? and  PDWorkOrder.workFlowNodeName!='draft' and PDWorkOrder.statusId not in (1,2)  ";

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, GoodsCode);
        rset = pstmt.executeQuery();
        if(rset.next()){
        	map.put("OutMaterials",rset.getBigDecimal(1));
        }
        log.debug(computeNo+" 类型："+billType+",单据:"+billNo+",BOM级别:"+bomGrade+",商品:"+GoodsCode+",需求数量："+qty+"..预计领料量："+map.get("OutMaterials"));
//		被取替代=被替换的量。
//		替代他料=替换别的工单的量。
        sql = " select isnull(sum(replaceQty),0) qty from "+
				"  PDLrpReplacePlan where ByGoodsCode =? and statusId=0 ";

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, GoodsCode);
        rset = pstmt.executeQuery();
        if(rset.next()){
        	map.put("Replace",rset.getBigDecimal(1));
        }
        
//		毛需求量=bom数量
//		缺货数量=毛需求量-(库存量+计划生产量+计划采购量+预计生产量+采购在途量-计划领料量-预计领料量-预计销货量)
        BigDecimal StockQty = map.get("curStocks").add(map.get("oweStocks"));
        map.put("StockQty", StockQty);
        BigDecimal canUseQty = map.get("StockQty").add(map.get("PlanProdQty")).add(map.get("PlanBuyQty")).add(map.get("ProdQty"))
        		.add(map.get("BuyQty")).subtract(map.get("PlanOutMaterials")).subtract(map.get("OutMaterials")).subtract(map.get("SalesQty"))
        		.subtract(map.get("Replace"));
        //被别的计划单替换的量要扣除在可用库存外。
        map.put("canUseQty", canUseQty);
        log.debug(computeNo+" 类型："+billType+",单据:"+billNo+",BOM级别:"+bomGrade+",商品:"+GoodsCode+",需求数量："+qty+"..可用量："+map.get("canUseQty"));
        //当可用数量小于0时，表明当前没有可用数量，需置0
        if(canUseQty.compareTo(new BigDecimal(0))<0)
        	canUseQty = new BigDecimal(0);
        BigDecimal LossQty = new BigDecimal(qty).subtract(canUseQty).setScale(Integer.parseInt(GlobalsTool.getSysSetting("DigitsQty")), BigDecimal.ROUND_HALF_UP);
        if(LossQty.compareTo(new BigDecimal(0))<0)
        	LossQty = new BigDecimal(0);
        map.put("LossQty", LossQty);
        log.debug(computeNo+" 类型："+billType+",单据:"+billNo+",BOM级别:"+bomGrade+",商品:"+GoodsCode+",需求数量："+qty+"..正常缺料量："+map.get("LossQty"));
        return map;
	}
	
	/**
	 *  库存量=当前仓库中实际存量+委外仓中欠料量。
		安全存量=商品表中存有每个商品最低库存量。
		计划生产量=已进行MRP运算，产生生产计划，但未抛转成工令单。
		计划采购量=已进行MRP运算，产生采购计划，但未抛转成请购单。
		计划领料量=已进行MRP运算，产生生产计划，但未抛转成工令单的原材料的占用量。
		预计销货量=未结订单的预计订单量(已审核的订单预计出货量-已出货量-借出未销数量)。
		    已参与MRP运算的销售订单，如果已做生产规划的按已做生产规划数量算
		预计生产量=未结工单的未完工量(工令单预计生产量-已生产量)
		采购在途量=所有未完请购量(请购量-已入库量)。
		预计领料量=未结工单的预计领料量(已发放工单的预计领用量-已领用量)
		被取替代=被替换的量。
		替代他料=替换别的单的量。
		毛需求量=bom数量
		缺货数量=毛需求量-(库存量+计划生产量+计划采购量+预计生产量+采购在途量-计划领料量-预计领料量-预计销货量)
	 * @param computeNo 运算单号，用于日志记录
	 * @param computeId
	 * @param billId
	 * @param billNo
	 * @param sourceId
	 * @param GoodsCode
	 * @param merge merge:合并物料的成品，mergedet合并物料的明细，''非合并物料
	 * @param conn
	 * @throws Exception
	 */
	public void compute(final String computeNo,final String computeId,String lrpId,final String isloss,final String isLowQty,final String isReplace,final String parentPlanId,final String startDate,
			final String endDate,String userId, final String billType,final String billId,final String billNo,final String sourceId,
			final String GoodsCode,final Double qty,String bomId,final int bomGrade,String merge,Connection conn) throws Exception{
		HashMap<String,BigDecimal > map = computeQty(computeNo, computeId, lrpId, isloss, isLowQty, isReplace, parentPlanId, startDate, endDate, userId, billType, billId, billNo, sourceId, GoodsCode, qty, bomId, bomGrade, conn);
		String planId=parentPlanId;//默认计划id
		
        //存入数据库,查询是否有可用bom，如果没有存入计划采购表，如果有存入计划生产表
        //先确定有没有下级bom可用   
        String attrType="";//tblGoods.attrType 0 自制,'2' 委外,'3' 虚拟
        String sendRound="";//sendRound 1是
        String GoodsNumber="";//商品编号
        BigDecimal lowerQty= new BigDecimal(0);//库存下限
        String newBomId=null;
        BigDecimal reTimes=new BigDecimal(0);
        String sql = "select tblGoods.attrType,PDBom.id bomId,tblGoods.sendRound,isnull(tblGoods.lowerQty,0) lowerQty,isnull(tblGoods.reTimes,0) reTimes,PDBom.workflowNodeName,PDBom.BillNo "
        		+ ",tblGoods.GoodsNumber from tblGoods left join PDBom on tblGoods.classCode=PDBom.GoodsCode and PDBom.isLast=1 "
        		+ "where tblGoods.classCode=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, GoodsCode);
        ResultSet rset = pstmt.executeQuery();
        if(rset.next()){
        	attrType = rset.getString("attrType");
        	newBomId = rset.getString("bomId");
        	sendRound = rset.getString("sendRound");
        	lowerQty = rset.getBigDecimal("lowerQty");
        	reTimes=rset.getBigDecimal("reTimes");
        	GoodsNumber = rset.getString("GoodsNumber");
        	if(attrType.equals("0")||attrType.equals("2")){
        		if(newBomId==null){
        			throw new Exception("物料:"+GoodsNumber+"不存在BOM！");
        		}
	        	if(!rset.getString("workflowNodeName").equals("finish")){
	        		throw new Exception("BOM单:"+rset.getString("BillNo")+";"+GoodsNumber+"处在反审核状态！");
	        	}
        	}
        	//如果来源码是万能码，要检查没有有做规划单，并且有规划此物料
        	if(attrType.equals("4")){
        		if(!billType.equals("PDMRP")){
        			throw new Exception("物料:"+GoodsNumber+"是万能码物料，"+billNo+"必须做生产规划单！");
        		}else{
        			sql = " select dtype from PDMRPGoodsDeploy where f_ref=? and GoodsCode=? ";
                	pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, billId);
                    pstmt.setString(2, GoodsCode);
                    ResultSet rset1 = pstmt.executeQuery();
                    if(rset1.next()){
                    	attrType = rset1.getString(1);
                    }else{
                    	throw new Exception("物料:"+GoodsNumber+"是万能码物料，"+billNo+"必须未规划此物料！");
                    }
        		}
        	}
        }else{
        	throw new Exception("GoodsCode:"+GoodsCode+"不存在！");
        }
//        if(sendRound != null && sendRound.equals("1")){//取整
//        	LossQty = new BigDecimal( Math.ceil(LossQty.doubleValue())+"");
//        }
        //计算替换料，
        if(isReplace.equals("1")){
        	//TODO 排除bom不允许替换料配置
        	BigDecimal LossQty = map.get("LossQty");//
        	BigDecimal byReplaceQty = new BigDecimal(0);//
        	sql = "select a.GoodsCode from PDGoodsReplaceDet a join PDGoodsReplace b on a.f_ref=b.id where b.GoodsCode=? "
        			+ "and b.PGoodsCode in (select GoodsCode from PDBom where id=?)";
        	PreparedStatement pstmt1 = conn.prepareStatement(sql);
        	pstmt1.setString(1, GoodsCode);
        	pstmt1.setString(2, bomId);
            ResultSet rset1 = pstmt1.executeQuery();
            while(rset1.next()){
            	String rGoodsCode=rset1.getString(1);
            	log.debug(computeNo+" 类型："+billType+",单据:"+billNo+",BOM级别:"+bomGrade+",商品:"+GoodsCode+",需求数量："+qty+"..开始计算替换料："+rGoodsCode);
            	HashMap<String,BigDecimal > rmap = computeQty(computeNo, computeId, lrpId, "0", "0", "0", parentPlanId, 
            			startDate, endDate, userId, billType, billId, billNo, sourceId, rGoodsCode, qty, bomId, bomGrade, conn);
            	if(rmap.get("canUseQty").doubleValue()>0){//有可用替换料
            		BigDecimal replaceQty;
            		if(rmap.get("canUseQty").compareTo(LossQty)>=0){
            			//用部用替换料，
            			replaceQty = LossQty;
            			LossQty = new BigDecimal(0);
            		}else{
            			//部分用此替换料
            			replaceQty = rmap.get("canUseQty");
            			LossQty = LossQty.subtract(rmap.get("canUseQty"));
            		}
            		byReplaceQty = byReplaceQty.add(replaceQty);
            		//记录被替换部分
                	sql="INSERT INTO [PDLrpReplacePlan]([id],[ComputerId],[LrpId],[Remark],[ProdPlanId],[GoodsCode],ByGoodsCode,replaceQty,[startDate],"
                			+ "[BillType],[BillId],[BillNo],[StockQty],[SafetyQty],[PlanProdQty],[PlanBuyQty],[PlanOutMaterials],[SalesQty],[ProdQty],"
                			+ "[BuyQty],[OutMaterials],[ByReplace],[Replace],[GrossReqQty],[createBy],[createTime],[statusId],[BomId],[BomGrade],[endDate])"
                			+ "values(substring(CONVERT(varchar(40),newid()),1,30),?,?,?,?,? ,?,?,?,?,? ,?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?)";
                	pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, computeId);
                    pstmt.setString(2, lrpId);
                    pstmt.setString(3, "");
                    pstmt.setString(4, planId);
                    pstmt.setString(5, GoodsCode);
                    pstmt.setString(6, rGoodsCode);//被谁替换
                    
                    pstmt.setBigDecimal(7, replaceQty); //被替换数量
                    pstmt.setString(8, "");
                    pstmt.setString(9, billType);
                    pstmt.setString(10, billId);
                    pstmt.setString(11, billNo);
                    pstmt.setBigDecimal(12, map.get("StockQty"));
                    pstmt.setBigDecimal(13, map.get("SafetyQty"));
                    pstmt.setBigDecimal(14, map.get("PlanProdQty"));
                    pstmt.setBigDecimal(15, map.get("PlanBuyQty"));
                    pstmt.setBigDecimal(16, map.get("PlanOutMaterials"));
                    pstmt.setBigDecimal(17, map.get("SalesQty"));
                    pstmt.setBigDecimal(18, map.get("ProdQty"));
                    pstmt.setBigDecimal(19, map.get("BuyQty"));
                    pstmt.setBigDecimal(20, map.get("OutMaterials"));
                    pstmt.setBigDecimal(21, map.get("ByReplace"));
                    pstmt.setBigDecimal(22, map.get("Replace"));
                    pstmt.setDouble(23, qty);
                    pstmt.setString(24, userId);
                    pstmt.setString(25, BaseDateFormat.getNowTime());
                    pstmt.setInt(26, 0);
                    pstmt.setString(27, bomId);
                    pstmt.setInt(28, bomGrade);//bom级别
                    pstmt.setString(29, "");//bom级别
                    pstmt.execute();
                    
                    log.debug(computeNo+" 类型："+billType+",单据:"+billNo+",BOM级别:"+bomGrade+",商品:"+GoodsCode+",需求数量："+qty+"..被替换料："+rGoodsCode+"替换"+replaceQty);
                    
            		if(LossQty.doubleValue()==0){
            			break;
            		}
            	}
            }
            map.put("ByReplace",byReplaceQty);
            map.put("LossQty", LossQty);
            log.debug(computeNo+" 类型："+billType+",单据:"+billNo+",BOM级别:"+bomGrade+",商品:"+GoodsCode+",需求数量："+qty+"..替换后缺货量："+map.get("LossQty"));
        }
        BigDecimal LossProdQty = map.get("LossQty");
        
        
        
        if(newBomId !=null && (attrType.equals("0")||attrType.equals("2")) && !"mergedet".equals(merge)){//合并的明细物料全是采购
        	bomId = newBomId;
        	//自制或委外
        	sql = "INSERT INTO [PDLrpProdPlan]([id],[ComputerId],[LrpId],[Remark],[GoodsCode],[LossQty],[endDate],[startDate],"
        			+ "[WOClassType],[WorkShop],[CompanyCode],[BillType],[BillId],[BillNo],[StockQty],[SafetyQty],[PlanProdQty],"
        			+ "[PlanBuyQty],[PlanOutMaterials],[SalesQty],[ProdQty],[BuyQty],[OutMaterials],[ByReplace],[Replace],[GrossReqQty],"
        			+ "[createBy],[createTime],[statusId],[LossProdQty],[BomId],[BomGrade],parentProdId)"
        			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        	planId = IDGenerater.getId();
        	pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, planId);
            pstmt.setString(2, computeId);
            pstmt.setString(3, lrpId);
            pstmt.setString(4, "");
            pstmt.setString(5, GoodsCode);
            pstmt.setBigDecimal(6, map.get("LossQty"));
            pstmt.setString(7, endDate);
            pstmt.setString(8, startDate);
            pstmt.setString(9, attrType.equals("0")?"2":"1");
            pstmt.setString(10, "");
            pstmt.setString(11, "");
            pstmt.setString(12, billType);
            pstmt.setString(13, billId);
            pstmt.setString(14, billNo);
            pstmt.setBigDecimal(15, map.get("StockQty"));
            pstmt.setBigDecimal(16, map.get("SafetyQty"));
            pstmt.setBigDecimal(17, map.get("PlanProdQty"));
            pstmt.setBigDecimal(18, map.get("PlanBuyQty"));
            pstmt.setBigDecimal(19, map.get("PlanOutMaterials"));
            pstmt.setBigDecimal(20, map.get("SalesQty"));
            pstmt.setBigDecimal(21, map.get("ProdQty"));
            pstmt.setBigDecimal(22, map.get("BuyQty"));
            pstmt.setBigDecimal(23, map.get("OutMaterials"));
            pstmt.setBigDecimal(24, map.get("ByReplace"));
            pstmt.setBigDecimal(25, map.get("Replace"));
            pstmt.setDouble(26, qty); 
            pstmt.setString(27, userId);
            pstmt.setString(28, BaseDateFormat.getNowTime());
            pstmt.setInt(29, 0);
            
            if(isLowQty.equals("1") && lowerQty != null && lowerQty.doubleValue() != 0d && LossProdQty.compareTo(lowerQty)<0){//安全库存
            	LossProdQty = lowerQty;
            }
            pstmt.setBigDecimal(30, LossProdQty);
            pstmt.setString(31, bomId);
            pstmt.setInt(32, bomGrade);//bom级别
            pstmt.setString(33, parentPlanId);//父级计划ID,用于生成工令时寻找子工令
            pstmt.execute();
            map.get("SafetyQty").doubleValue();
        }else {
        	//采购件
        	sql="INSERT INTO [PDLrpBuyPlan]([id],[ComputerId],[LrpId],[Remark],[ProdPlanId],[GoodsCode],[LossQty],[LossBuyQty],[startDate],"
        			+ "[BillType],[BillId],[BillNo],[StockQty],[SafetyQty],[PlanProdQty],[PlanBuyQty],[PlanOutMaterials],[SalesQty],[ProdQty],"
        			+ "[BuyQty],[OutMaterials],[ByReplace],[Replace],[GrossReqQty],[createBy],[createTime],[statusId],[BomId],[BomGrade],[endDate])"
        			+ "values(substring(CONVERT(varchar(40),newid()),1,30),?,?,?,?,? ,?,?,?,?,? ,?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?)";
        	pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, computeId);
            pstmt.setString(2, lrpId);
            pstmt.setString(3, "");
            pstmt.setString(4, planId);
            pstmt.setString(5, GoodsCode);
            pstmt.setBigDecimal(6, map.get("LossQty"));
            BigDecimal LossBuyQty=new BigDecimal(map.get("LossQty").toString());

//            if(reTimes != null && reTimes.doubleValue() != 0d && LossBuyQty.doubleValue()<reTimes.doubleValue()){//最低采购量
//            	LossBuyQty = reTimes;
//            }
            if(isLowQty.equals("1") && lowerQty != null && lowerQty.doubleValue() != 0d && LossBuyQty.doubleValue()<lowerQty.doubleValue()){//安全库存
            	LossBuyQty = lowerQty;
            }
            pstmt.setBigDecimal(7, LossBuyQty);
            pstmt.setString(8, "");
            pstmt.setString(9, billType);
            pstmt.setString(10, billId);
            pstmt.setString(11, billNo);
            pstmt.setBigDecimal(12, map.get("StockQty"));
            pstmt.setBigDecimal(13, map.get("SafetyQty"));
            pstmt.setBigDecimal(14, map.get("PlanProdQty"));
            pstmt.setBigDecimal(15, map.get("PlanBuyQty"));
            pstmt.setBigDecimal(16, map.get("PlanOutMaterials"));
            pstmt.setBigDecimal(17, map.get("SalesQty"));
            pstmt.setBigDecimal(18, map.get("ProdQty"));
            pstmt.setBigDecimal(19, map.get("BuyQty"));
            pstmt.setBigDecimal(20, map.get("OutMaterials"));
            pstmt.setBigDecimal(21, map.get("ByReplace"));
            pstmt.setBigDecimal(22, map.get("Replace"));
            pstmt.setDouble(23, qty);
            pstmt.setString(24, userId);
            pstmt.setString(25, BaseDateFormat.getNowTime());
            pstmt.setInt(26, 0);
            pstmt.setString(27, bomId);
            pstmt.setInt(28, bomGrade);//bom级别
            pstmt.setString(29, "");//bom级别
            pstmt.execute();
        }
        if(newBomId !=null && (attrType.equals("0")||attrType.equals("2"))){//有下级bom时递归到下级,自制，委外，虚拟
        	ArrayList<HashMap<String,Object>> bomDetList;
        	if("merge".equals(merge)){
        		bomDetList = new ArrayList<HashMap<String,Object>>();
        		getBomMerge(conn, newBomId, billId, billNo, 1d, 0d, bomDetList);
        	}else{
        		bomDetList = getBom(conn,newBomId);
        	}
        	for(HashMap<String,Object> bomDet :bomDetList){
	        	BigDecimal q = new BigDecimal((Double)bomDet.get("qty"));
	        	q = q.multiply(LossProdQty);//缺料生产数*bom数量
	        	Double lr = (Double)bomDet.get("lossRate");
	        	if(isloss.equals("1") && lr != null ){//如果考虑损耗，且损耗率不为空，则计算损耗
	        		q = q.multiply(new BigDecimal(100+lr)).divide(new BigDecimal(100));
	        	}
	        	if(sendRound != null && sendRound.equals("1")){//下级的毛需求量先取整
	            	q = new BigDecimal( Math.ceil(q.doubleValue())+"");
	            }
	        	q=q.setScale(Integer.parseInt(GlobalsTool.getSysSetting("DigitsQty")), BigDecimal.ROUND_HALF_UP);//四舍五入
	        	compute(computeNo, computeId, lrpId, isloss, isLowQty, isReplace, planId, startDate, endDate, 
	        			userId, billType, billId, billNo, sourceId, bomDet.get("GoodsCode")+"", q.doubleValue(), newBomId, bomGrade+1,merge, conn);
        	}
        }
	}
	
	public void  getBomMerge(Connection conn,String bomId,String billId,String billNo,Double pQty,Double pLossRate,ArrayList<HashMap<String,Object>> bomDetList) throws Exception{
		String sql = " select PDBomDet.GoodsCode,PDBomDet.qty,PDBomDet.lossRate,tblGoods.GoodsNumber,tblGoods.attrType,PDBom.id bomId,PDBom.workflowNodeName,PDBom.BillNo from "
        		+ " PDBomDet join tblGoods on PDBomDet.GoodsCode=tblGoods.classCode "
        		+ " left join PDBom on tblGoods.classCode=PDBom.GoodsCode and PDBom.isLast=1   where  PDBomDet.f_ref=? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, bomId);
        ResultSet rset = pstmt.executeQuery();
        while(rset.next()){
        	String GoodsCode = rset.getString("GoodsCode");
        	String GoodsNumber = rset.getString("GoodsNumber");
        	String attrType = rset.getString("attrType");
        	String newBomId = rset.getString("bomId");
        	Double qty = rset.getDouble("qty")*pQty;
        	Double lossRate = rset.getDouble("qty")+pLossRate;
        	//如果attrType是万能码，要查规划单
        	if(attrType.equals("4")){
    			sql = " select dtype from PDMRPGoodsDeploy where f_ref=? and GoodsCode=? ";
            	pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, billId);
                pstmt.setString(2, GoodsCode);
                ResultSet rset1 = pstmt.executeQuery();
                if(rset1.next()){
                	attrType = rset1.getString(1);
                }else{
                	throw new Exception("物料:"+GoodsNumber+"是万能码物料，"+billNo+"必须未规划此物料！");
                }
        	}
        	if(attrType.equals("0")||attrType.equals("2")){
        		if(newBomId==null||newBomId.equals("")){
        			throw new Exception("物料:"+GoodsNumber+"不存在BOM！");
        		}
            	if(!rset.getString("workflowNodeName").equals("finish")){
            		throw new Exception("BOM单:"+rset.getString("BillNo")+";"+GoodsNumber+"处在反审核状态！");
            	}
        		getBomMerge(conn, newBomId, billId, billNo,qty,lossRate, bomDetList);
        	}else{
	        	HashMap<String,Object> map = new HashMap<String,Object>();
	        	map.put("GoodsCode", GoodsCode);
	        	map.put("qty", qty);
	        	map.put("lossRate", lossRate);
	        	bomDetList.add(map);
        	}
        }
        return ;
	}
	public ArrayList<HashMap<String,Object>> getBom(Connection conn,String bomId) throws Exception{
		ArrayList<HashMap<String,Object>> ret = new ArrayList<HashMap<String,Object>>();
		String sql = " select PDBomDet.GoodsCode,PDBomDet.qty,PDBomDet.lossRate from "
        		+ " PDBomDet where  PDBomDet.f_ref=? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, bomId);
        ResultSet rset = pstmt.executeQuery();
        while(rset.next()){
        	HashMap<String,Object> map = new HashMap<String,Object>();
        	map.put("GoodsCode", rset.getString("GoodsCode"));
        	map.put("qty", rset.getDouble("qty"));
        	map.put("lossRate", rset.getDouble("lossRate"));
        	ret.add(map);
        }
        return ret;
	}
	public Result queryProd(String computeId){
		ArrayList param = new ArrayList();
		String sql = "SELECT a.[id],[ComputerId],[LrpId],a.[Remark],[GoodsCode],[LossQty],[endDate],[startDate],[WOClassType],"
				+ "a.[WorkShop],a.[CompanyCode],[BillType],[BillId],[BillNo],[StockQty],[SafetyQty],[PlanProdQty],[PlanBuyQty],[PlanOutMaterials],"
				+ "[SalesQty],[ProdQty],[BuyQty],[OutMaterials],[ByReplace],[Replace],[GrossReqQty],a.[statusId],"
				+ "[LossProdQty],[BomId],[BomGrade],GoodsNumber,GoodsFullName,GoodsSpec,BaseUnit,ComFullName,d.name WorkShopName"
				+ "  FROM [PDLrpProdPlan] a join tblGoods b on a.GoodsCode=b.classCode "
				+ "  left join tblCompany c on a.CompanyCode=c.classCode "
				+ "  left join PDWorkShop d on a.WorkShop=d.id where a.ComputerId=? order by a.listOrder ";
		param.add(computeId);
		return sqlListMap(sql,param);
	}
	//查生产计划的物料明细
	public ArrayList queryProdGoods(String ProdPlanId){
		ArrayList ret = new ArrayList();
		ArrayList param = new ArrayList();
		param.add(ProdPlanId);
		String sql = "select GoodsCode,GoodsNumber,GoodsFullName,GoodsSpec,BaseUnit,GrossReqQty-ByReplace qty,a.Remark "
				+ "from PDLrpBuyPlan a join tblGoods b on a.GoodsCode=b.classCode where ProdPlanId=? and GrossReqQty-ByReplace>0 ";
		Result rs = sqlListMap(sql,param);
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			return null;
		}else{
			ret.addAll((ArrayList)rs.retVal);
		}
		sql = "select ByGoodsCode GoodsCode,b.GoodsNumber,b.GoodsFullName,b.GoodsSpec,b.BaseUnit,replaceQty qty,a.Remark,"
				+ "GoodsCode as fromGoodsCode ,c.GoodsNumber fromGoodsNumber,c.GoodsFullName fromGoodsFullName,"
				+ "c.GoodsSpec fromGoodsSpec,c.BaseUnit fromBaseUnit from PDLrpReplacePlan a  join tblGoods b on a.ByGoodsCode=b.classCode "
				+ "join tblGoods c on a.GoodsCode=c.classCode where ProdPlanId=?";
		rs = sqlListMap(sql,param);
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			return null;
		}else{
			ret.addAll((ArrayList)rs.retVal);
		}
		sql = "select GoodsCode,GoodsNumber,GoodsFullName,GoodsSpec,BaseUnit,GrossReqQty-ByReplace qty,a.Remark from PDLrpProdPlan a "
				+ "join tblGoods b on a.GoodsCode=b.classCode where parentProdId=?  and GrossReqQty-ByReplace>0";
		rs = sqlListMap(sql,param);
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			return null;
		}else{
			ret.addAll((ArrayList)rs.retVal);
		}
		return ret;
	}
	public Result queryBuy(String computeId){
		ArrayList param = new ArrayList();
		String sql = "SELECT a.[id],[ComputerId],[LrpId],a.[Remark],[ProdPlanId],[GoodsCode],[LossQty],[LossBuyQty],[startDate],[endDate],"
				+ "[BillType],[BillId],[BillNo],[StockQty],[SafetyQty],[PlanProdQty],[PlanBuyQty],[PlanOutMaterials],[SalesQty],[ProdQty],"
				+ "[BuyQty],[OutMaterials],[ByReplace],[Replace],[GrossReqQty],a.[statusId],[BomId],[BomGrade]  ,"
				+ "GoodsNumber,GoodsFullName,GoodsSpec,BaseUnit"
				+ "  FROM [PDLrpBuyPlan] a join tblGoods b on a.GoodsCode=b.classCode where a.ComputerId=?  order by a.listOrder";
		param.add(computeId);
		return sqlListMap(sql,param);
	}
	
	/**
	 * 选择计算单号
	 * @param startDate
	 * @param endDate
	 * @param computeNo
	 * @param billType
	 * @param BillNo
	 * @param GoodsNumber
	 * @param GoodsFullName
	 * @return
	 */
	public Result selCompute(String startDate,String endDate,String computeNo,String billType,String BillNo,String GoodsNumber,String GoodsFullName){
		ArrayList param = new ArrayList();
		String sql = " select a.id,BillDate,a.BillNo,b.EmpFullName,c.BillType,c.BillNo cBillNo,c.Qty,d.GoodsNumber,d.GoodsFullName,"
				+ "row_number() over ( order by BillDate desc,a.billNo desc) row_id from PDLRPCompute a "
				+ "join tblEmployee b on a.createBy=b.id join PDLRP c on a.id=c.ComputerId join tblGoods d on c.GoodsCode=d.classCode where 1=1 ";
		if(computeNo != null && computeNo.length() > 0){
			sql += " and a.billNo like '%'+?+'%' ";
			param.add(computeNo);
		}
		if(startDate != null && startDate.length() > 0){
			sql += " and a.BillDate >=? ";
			param.add(startDate);
		}
		if(endDate != null && endDate.length() > 0){
			sql += " and a.BillDate <=? ";
			param.add(endDate);
		}
		if(billType != null && billType.length() > 0){
			sql += " and c.BillType = ? ";
			param.add(billType);
		}
		if(BillNo != null && BillNo.length() > 0){
			sql += " and c.billNo like '%'+?+'%' ";
			param.add(BillNo);
		}
		if(GoodsNumber != null && GoodsNumber.length() > 0){
			sql += " and d.GoodsNumber like '%'+?+'%' ";
			param.add(GoodsNumber);
		}
		if(GoodsFullName != null && GoodsFullName.length() > 0){
			sql += " and d.GoodsFullName like '%'+?+'%' ";
			param.add(GoodsFullName);
		}
		BaseEnv.log.debug("LrpMgt.selCompute sql="+sql);
		return sqlListMap(sql,param,1,1000);
	}
	
	private void getChildProdId(String keyId,ArrayList<String> prodlist){
		String sql  = "select id from PDLrpProdPlan where parentProdId=?";
		ArrayList list = new ArrayList();
		list.add(keyId);
		Result rs = sqlListMap(sql,list);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS ){
			ArrayList<HashMap<String,Object>> rl = (ArrayList<HashMap<String,Object>>)rs.retVal;
			for(HashMap map :rl){
				prodlist.add(map.get("id")+"");
				getChildProdId(map.get("id")+"", prodlist);
			}
		}
	}
	
	public Result toMerge(final String keyId,LoginBean loginBean){
		Result rs = new Result();
		final ArrayList<String> cprodlist=new ArrayList<String>();
		getChildProdId(keyId, cprodlist);
		if(cprodlist.size() ==0){
			return rs;//没有子级工令要合并
		}else{
			final Result rst = new Result();
	        int retCode = DBUtil.execute(new IfDB() {
	            public int exec(Session session) {
	                session.doWork(new Work() {
	                    public void execute(Connection connection) throws
	                            SQLException {
	                        try {
	                        	for(String cid :cprodlist){//处理每一个子工令
	                        		//1、把子工令采购计划合并
		                        	String sql  = "update PDLrpBuyPlan set ProdPlanId=? where ProdPlanId=?";
		                        	PreparedStatement stmt =connection.prepareStatement(sql);
		                        	stmt.setString(1, keyId);
		                        	stmt.setString(2, cid);
		                        	stmt.execute();
		                        	//2.把替换料计划合并
		                        	sql  = "update PDLrpReplacePlan set ProdPlanId=? where ProdPlanId=?";
		                        	stmt =connection.prepareStatement(sql);
		                        	stmt.setString(1, keyId);
		                        	stmt.setString(2, cid);
		                        	stmt.execute();
		                        	//3、如果子级生产计划中有占用半成品加入到采购计划中,如果毛需求减替换料减缺料数
		                        	sql = "INSERT INTO [PDLrpBuyPlan]([id],[classCode],[isCatalog],[ComputerId],[LrpId],[Remark],[ProdPlanId],[GoodsCode],[LossQty],"
		                        			+ "[LossBuyQty],[startDate],[BillType],[BillId],[BillNo],[StockQty],[SafetyQty],[PlanProdQty],[PlanBuyQty],[PlanOutMaterials],"
		                        			+ "[SalesQty],[ProdQty],[BuyQty],[OutMaterials],[ByReplace],[Replace],[GrossReqQty],[createBy],[lastUpdateBy],[createTime],"
		                        			+ "[lastUpdateTime],[statusId],[SCompanyID],[finishTime],[printCount],[workFlowNode],[workFlowNodeName],[checkPersons],"
		                        			+ "[BomId],[BomGrade],[endDate]) "
		                        			+ "select [id],[classCode],[isCatalog],[ComputerId],[LrpId],[Remark],?,[GoodsCode],0,"
		                        			+ "0,[startDate],[BillType],[BillId],[BillNo],[StockQty],[SafetyQty],[PlanProdQty],[PlanBuyQty],"
		                        			+ "[PlanOutMaterials],[SalesQty],[ProdQty],[BuyQty],[OutMaterials],[ByReplace],[Replace],GrossReqQty-ByReplace-LossQty,[createBy],"
		                        			+ "[lastUpdateBy],[createTime],[lastUpdateTime],[statusId],[SCompanyID],[finishTime],[printCount],[workFlowNode],"
		                        			+ "[workFlowNodeName],[checkPersons],[BomId],[BomGrade],[endDate] from PDLrpProdPlan where id=? and GrossReqQty-ByReplace-LossQty >0 ";
		                        	stmt =connection.prepareStatement(sql);
		                        	stmt.setString(1, keyId);
		                        	stmt.setString(2, cid);
		                        	stmt.execute();
		                        	//4、删除子级计划
		                        	sql  = "delete PDLrpProdPlan  where id=?";
		                        	stmt =connection.prepareStatement(sql);
		                        	stmt.setString(1, cid);
		                        	stmt.execute();
	                        	}
	                        	
	                        	rst.setRetVal("");
	                        } catch (Exception ex) {
	                            BaseEnv.log.error("LrpMgt.compute",ex);
	                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
	                            rst.setRetVal("执行失败"+ex.getMessage());
	                            return;
	                        }
	                    }
	                });
	                return rst.getRetCode();
	            }
	        });
	        rst.setRetCode(retCode); 
	        return rst;
		}
	}
	
	/**
	 * 抛转工令单
	 * @param planid
	 * @return
	 */
	public Result toWorkOrder(ArrayList<String> planids,LoginBean loginBean){
		Result rs = new Result();
		while(planids.size() > 0){
			String planid=planids.get(0);
			rs = toWorkOrder(planid, planids, loginBean);
			if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
				return rs;
			}
		}
		return rs;
	}
	/**
	 * 抛转工令单
	 * @param planid
	 * @return
	 */
	public Result toWorkOrder(String planid,ArrayList<String> planids,LoginBean loginBean){
		Result rs = new Result();
		
		String tableName = "PDWorkOrder";
		/*父类的classCode*/
		String parentCode = "";
		//要执行的define的信息
		String defineInfo = "";
		
		String deliverTo = "";
		
		String locale = "zh_CN";
		String saveType = ""; //保存类型 saveDraft 草稿
		
		HashMap values=new HashMap();
		values.put("id", IDGenerater.getId());
		values.put("classCode", "");
		values.put("isCatalog", "0");
		values.put("BillDate", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
		values.put("DepartmentCode", loginBean.getDepartCode());
		values.put("EmployeeID", loginBean.getId());
		String sql  = "select * from PDLrpProdPlan where id=?";
		ArrayList list = new ArrayList();
		list.add(planid);
		rs = sqlListMap(sql,list);
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS || ((ArrayList)rs.retVal).size()==0){
			rs.retCode=ErrorCanst.DEFAULT_FAILURE;
			rs.retVal="查询生产计划单失败";
			return rs;
		}
		HashMap pm = ((ArrayList<HashMap>)rs.retVal).get(0);
		values.put("GoodsCode", pm.get("GoodsCode"));
		values.put("WOClassType", pm.get("WOClassType"));
		values.put("Qty", ((BigDecimal)pm.get("LossProdQty")).doubleValue()+"");
		values.put("CompDate", pm.get("endDate"));
		values.put("Remark", pm.get("Remark"));
		values.put("createBy", loginBean.getId());
		values.put("lastUpdateBy", loginBean.getId());
		values.put("createTime", BaseDateFormat.getNowTime());
		values.put("lastUpdateTime",BaseDateFormat.getNowTime());
		values.put("statusId", "0");
		values.put("SCompanyID", loginBean.getSunCmpClassCode());
		values.put("lastUpdateTime",BaseDateFormat.getNowTime());
		values.put("finishTime", pm.get("GoodsCode"));
		values.put("printCount", "0");
		values.put("workFlowNode", "0");
		values.put("workFlowNodeName", "noApprove");
		values.put("moduleType", "0");
		values.put("CompanyCode", pm.get("CompanyCode"));
		values.put("WorkShop", pm.get("WorkShop"));
		values.put("InPayQty", "0");
		values.put("BomId", pm.get("BomId"));
		values.put("StartDate", pm.get("StartDate"));
		values.put("BomId", pm.get("BomId"));
		values.put("LrpPlanId", pm.get("id"));
		
		//所有采购计划的原料
		sql  = "select * from PDLrpBuyPlan where ProdPlanId=?";
		list = new ArrayList();
		list.add(planid);
		rs = sqlListMap(sql,list);
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS ){
			rs.retCode=ErrorCanst.DEFAULT_FAILURE;
			rs.retVal="查询采购计划单失败";
			return rs;
		}
		ArrayList<HashMap> detList = new ArrayList<HashMap>();
		values.put("TABLENAME_PDWorkOrderDet", detList);
		for(HashMap dm :((ArrayList<HashMap>)rs.retVal)){
			//检查采购计划是否抛转
			if(((BigDecimal)dm.get("LossBuyQty")).compareTo(new BigDecimal(0))>0 && ((Integer)dm.get("statusId"))==0){
				rs.retCode=ErrorCanst.DEFAULT_FAILURE;
				rs.retVal="采购计划单还未抛转";
				return rs;
			}
			//数量等于毛需求量减去被替换数量，如果大于0，就下工令。
			BigDecimal qty = (BigDecimal)dm.get("GrossReqQty");
			qty=qty.subtract((BigDecimal)dm.get("ByReplace"));
			if(qty.doubleValue()<=0){
				continue;
			}
			
			HashMap det = new HashMap();
			det.put("f_ref", values.get("id"));
			det.put("GoodsCode", dm.get("GoodsCode"));
			det.put("Inch", "");
			det.put("Hue", "");
			det.put("yearNO", "");
			det.put("BatchNo", "");
			det.put("ProDate", "");
			det.put("Availably", "");
			sql=" select qty,b.lossRate from PDBom a join PDBomDet b on a.id=b.f_ref where a.id=? and b.GoodsCode=? ";
			list = new ArrayList();
			list.add(dm.get("BomId"));
			list.add(dm.get("GoodsCode"));
			rs = sqlListMap(sql,list);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList)rs.retVal).size()>0){
				HashMap bm = ((ArrayList<HashMap>)rs.retVal).get(0);
				det.put("UseQty", ((BigDecimal)bm.get("qty")).doubleValue()+"");
				det.put("StandLossRate", ((BigDecimal)bm.get("lossRate")).doubleValue()+"");
			}
			det.put("SendQty", "0");
			det.put("SendQtyExt", qty+"");
			det.put("SendBakQty", "0");
			BigDecimal sqe = new BigDecimal(det.get("SendQtyExt")+"").multiply(new BigDecimal(det.get("StandLossRate")+"")).divide(new BigDecimal(100)).
					setScale(Integer.parseInt(GlobalsTool.getSysSetting("DigitsQty")), BigDecimal.ROUND_HALF_UP);
			det.put("SendBakQtyExt", sqe.doubleValue()+"");
			det.put("BakQty", sqe.doubleValue()+"");
			det.put("NoBakQty", "0");
			det.put("ReturnQty", "0");
			det.put("OutQty", "0");
			det.put("CheckQty", "0");
			det.put("LossRate", "0");
			det.put("Qty", qty+"");
			det.put("Remark", dm.get("Remark")+"");
			det.put("InPayQty", "0");
			det.put("isClient", "2");
			det.put("bakOutQty", "0");
			det.put("noBakOutQty", "0");
			det.put("bomId", dm.get("BomId"));
			boolean found = false;
			for(HashMap map :detList){
				if(det.get("GoodsCode").equals(map.get("GoodsCode"))){
					map.put("SendQtyExt", (Double.parseDouble(map.get("SendQtyExt")+"")+Double.parseDouble(det.get("SendQtyExt")+""))+"");
					map.put("SendBakQtyExt", (Double.parseDouble(map.get("SendBakQtyExt")+"")+Double.parseDouble(det.get("SendBakQtyExt")+""))+"");
					map.put("Qty", map.get("SendQtyExt"));
					found = true;
					break;
				}
			}
			if(!found){
				detList.add(det);
			}
		}
		//查询所有替换料
		sql  = "select * from PDLrpReplacePlan where ProdPlanId=?";
		list = new ArrayList();
		list.add(planid);
		rs = sqlListMap(sql,list);
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS ){
			rs.retCode=ErrorCanst.DEFAULT_FAILURE;
			rs.retVal="查询生产计划单失败";
			return rs;
		}
		for(HashMap dm :((ArrayList<HashMap>)rs.retVal)){
			HashMap det = new HashMap();
			det.put("f_ref", values.get("id"));
			det.put("GoodsCode", dm.get("ByGoodsCode"));
			det.put("Inch", "");
			det.put("Hue", "");
			det.put("yearNO", "");
			det.put("BatchNo", "");
			det.put("ProDate", "");
			det.put("Availably", "");
			sql=" select qty,b.lossRate from PDBom a join PDBomDet b on a.id=b.f_ref where a.id=? and b.GoodsCode=? ";
			list = new ArrayList();
			list.add(pm.get("BomId"));//父级bomId
			list.add(dm.get("GoodsCode"));
			rs = sqlListMap(sql,list);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList)rs.retVal).size()>0){
				HashMap bm = ((ArrayList<HashMap>)rs.retVal).get(0);
				det.put("UseQty", ((BigDecimal)bm.get("qty")).doubleValue()+"");
				det.put("StandLossRate", ((BigDecimal)bm.get("lossRate")).doubleValue()+"");
			}
			det.put("SendQty", "0");
			det.put("SendQtyExt", dm.get("replaceQty")+"");
			det.put("SendBakQty", "0");
			BigDecimal sqe = new BigDecimal(dm.get("replaceQty")+"").multiply(new BigDecimal(det.get("StandLossRate")+"")).divide(new BigDecimal(100)).
					setScale(Integer.parseInt(GlobalsTool.getSysSetting("DigitsQty")), BigDecimal.ROUND_HALF_UP);
			det.put("SendBakQtyExt", sqe.doubleValue()+"");
			det.put("BakQty", sqe.doubleValue()+"");
			det.put("NoBakQty", "0");
			det.put("ReturnQty", "0");
			det.put("OutQty", "0");
			det.put("CheckQty", "0");
			det.put("LossRate", "0");
			det.put("Qty", dm.get("replaceQty")+"");
			det.put("Remark", dm.get("Remark")+"");
			det.put("InPayQty", "0");
			det.put("isClient", "2");
			det.put("bakOutQty", "0");
			det.put("noBakOutQty", "0");
			det.put("bomId", pm.get("BomId")); //父级bomId
			detList.add(det);
		}
		
		//查询所有子级工令
		sql  = "select * from PDLrpProdPlan where parentProdId=?";
		list = new ArrayList();
		list.add(planid);
		rs = sqlListMap(sql,list);
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS ){
			rs.retCode=ErrorCanst.DEFAULT_FAILURE;
			rs.retVal="查询生产计划单失败";
			return rs;
		}
		for(HashMap dm :((ArrayList<HashMap>)rs.retVal)){
			//检查下级计划是否抛转，因为如果下级未抛转工令，上级先抛转，会导致，半成品在计划领料中还存在，但上级工令的发料清单中已有半成品，所以半成品又出现在预计领料中，从而半成品的运算出现错误
			if(((BigDecimal)dm.get("LossProdQty")).compareTo(new BigDecimal(0))>0 && ((Integer)dm.get("statusId"))==0){
				rs =toWorkOrder(dm.get("id")+"", planids, loginBean);
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
					return rs;
				}
			}
			//数量等于毛需求量减去被替换数量，如果大于0，就下工令。
			BigDecimal qty = (BigDecimal)dm.get("GrossReqQty");
			qty=qty.subtract((BigDecimal)dm.get("ByReplace"));
			if(qty.doubleValue()<=0){
				continue;
			}
			HashMap det = new HashMap();
			det.put("f_ref", values.get("id"));
			det.put("GoodsCode", dm.get("GoodsCode"));
			det.put("Inch", "");
			det.put("Hue", "");
			det.put("yearNO", "");
			det.put("BatchNo", "");
			det.put("ProDate", "");
			det.put("Availably", "");
			det.put("GoodsCode", dm.get("GoodsCode"));
			sql=" select qty,b.lossRate from PDBom a join PDBomDet b on a.id=b.f_ref where a.id=? and b.GoodsCode=? ";
			list = new ArrayList();
			list.add(pm.get("BomId"));//父级bomId
			list.add(dm.get("GoodsCode"));
			rs = sqlListMap(sql,list);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList)rs.retVal).size()>0){
				HashMap bm = ((ArrayList<HashMap>)rs.retVal).get(0);
				det.put("UseQty", ((BigDecimal)bm.get("qty")).doubleValue()+"");
				det.put("StandLossRate", ((BigDecimal)bm.get("lossRate")).doubleValue()+"");
			}
			det.put("SendQty", "0");
			det.put("SendQtyExt", qty+"");
			det.put("SendBakQty", "0");
			BigDecimal sqe = new BigDecimal(det.get("SendQtyExt")+"").multiply(new BigDecimal(det.get("StandLossRate")+"")).divide(new BigDecimal(100)).
					setScale(Integer.parseInt(GlobalsTool.getSysSetting("DigitsQty")), BigDecimal.ROUND_HALF_UP);
			det.put("SendBakQtyExt", sqe.doubleValue()+"");
			det.put("BakQty", sqe.doubleValue()+"");
			det.put("NoBakQty", "0");
			det.put("ReturnQty", "0");
			det.put("OutQty", "0");
			det.put("CheckQty", "0");
			det.put("LossRate", "0");
			det.put("Qty", qty+"");
			det.put("Remark", dm.get("Remark")+"");
			det.put("InPayQty", "0");
			det.put("isClient", "2");
			det.put("bakOutQty", "0");
			det.put("noBakOutQty", "0");
			det.put("bomId", pm.get("BomId")); //父级bomId
			detList.add(det);
		}
		
		String valuesStr = gson.toJson(values);
		Message msg = AIOConnect.add(locale, loginBean, tableName, parentCode, defineInfo, saveType, valuesStr, deliverTo);
		if(!msg.getCode().equals("OK")){
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			rs.retVal=msg.getMsg();
			return rs;
		}else{
			//成功处理删除这个号,并继续处理下一个号
			planids.remove(planid);
		}
			
		
		rs.retVal = "";
		return rs;
	}
	
	public Result toBuy(final String computeId,LoginBean loginBean){
		Result rs = new Result();
		String sql = "select GoodsCode,sum(LossBuyQty) LossBuyQty,min(startDate) startDate,max(endDate) endDate from PDLrpBuyPlan where ComputerId=? group by GoodsCode";
		ArrayList list = new ArrayList();
		list.add(computeId);
		rs = sqlListMap(sql,list);
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS || ((ArrayList)rs.retVal).size()==0){
			rs.retCode=ErrorCanst.DEFAULT_FAILURE;
			rs.retVal="查询采购计划单失败";
			return rs;
		}
		String bid = IDGenerater.getId();
		ArrayList dmapList = new ArrayList();
		for(HashMap map :(ArrayList<HashMap>)rs.retVal){
			if(((BigDecimal)map.get("LossBuyQty")).compareTo(new BigDecimal(0))<=0){
				continue;
			}
			HashMap dmap = new HashMap();
			dmap.put("f_ref", bid);
			dmap.put("GoodsCode", map.get("GoodsCode"));
			dmap.put("BatchNo", "");
			dmap.put("color", "");
			dmap.put("Hue", "");
			dmap.put("Inch", "");
			dmap.put("yearNO", "");
			dmap.put("ProDate", "");
			dmap.put("Availably", "");
			dmap.put("Qty", map.get("LossBuyQty").toString());
			dmap.put("Price", "0");
			dmap.put("Amount", "0");
			dmap.put("OrderQty", "0");
			dmap.put("NoOrderQty", map.get("LossBuyQty").toString());
			dmap.put("lackQty", map.get("LossBuyQty").toString());
			dmap.put("totalLoss", "0");
			dmap.put("FinishStatus", "0");
			dmap.put("ArriveDate", map.get("endDate"));
			dmap.put("OrderDate", map.get("startDate"));
			dmap.put("", map.get(""));
			dmapList.add(dmap);
		}
		if(dmapList.size() > 0){
			//有明细行，才进行采购
			String tableName = "tblBuyApplication";
			/*父类的classCode*/
			String parentCode = "";
			//要执行的define的信息
			String defineInfo = "";
			
			String deliverTo = "";
			
			String locale = "zh_CN";
			String saveType = ""; //保存类型 saveDraft 草稿
			HashMap values=new HashMap();
			values.put("TABLENAME_tblBuyApplicationDet", dmapList);
			values.put("id", bid);
			values.put("workFlowNode", "0");
			values.put("workFlowNodeName", "noApprove");
			values.put("BillDate", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
			values.put("DepartmentCode", loginBean.getDepartCode());
			values.put("EmployeeID", loginBean.getId());
			values.put("createBy", loginBean.getId());
			values.put("lastUpdateBy", loginBean.getId());
			values.put("createTime", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
			values.put("lastUpdateTime", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
			values.put("statusId", "0");
			values.put("SCompanyID", loginBean.getSunCmpClassCode());
			values.put("LrpComputeId", computeId);
			
			String valuesStr = gson.toJson(values);
			Message msg = AIOConnect.add(locale, loginBean, tableName, parentCode, defineInfo, saveType, valuesStr, deliverTo);
			if(!msg.getCode().equals("OK")){
				rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
				rs.retVal=msg.getMsg();
				return rs;
			}
		}else{
			//如果都没有需要请购的，直接设置状态为已请购
			final Result rst = new Result();
			int retCode = DBUtil.execute(new IfDB() {
	            public int exec(Session session) {
	                session.doWork(new Work() {
	                    public void execute(Connection connection) throws
	                            SQLException {
	                        try {
	                        	String sql  = "update PDLrpBuyPlan set statusId=1 where  ComputerId =?";
	                        	PreparedStatement stmt =connection.prepareStatement(sql);
	                        	stmt.setString(1, computeId);
	                        	stmt.execute();
	                        	sql  = "update PDLrp set statusId=1 where  ComputerId =? ";
	                        	stmt =connection.prepareStatement(sql);
	                        	stmt.setString(1, computeId);
	                        	stmt.execute();
	                        	rst.setRetVal("");
	                        } catch (Exception ex) {
	                            BaseEnv.log.error("LrpMgt.toBuy",ex);
	                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
	                            rst.setRetVal("执行失败"+ex.getMessage());
	                            return;
	                        }
	                    }
	                });
	                return rst.getRetCode();
	            }
	        });
	        rst.setRetCode(retCode);
	        rs.retCode=retCode;
	        rs.retVal=rst.retVal;
		}
		return rs;
	}
}
