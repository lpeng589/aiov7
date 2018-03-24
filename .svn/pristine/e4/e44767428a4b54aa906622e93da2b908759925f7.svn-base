package hf;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;

public class HFData extends AIODBManager{
	private static Gson gson;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}
	
	public String exec(HttpServletRequest request){
		String op = request.getParameter("op");
		if("GetGoods".equals(op)){
			return getGoods(request);
		}else if("PutGoods".equals(op)){
			return putGoods(request);
		}else if("getBill".equals(op)){
			return getBill(request);
		}else if("DelGoods".equals(op)){
			return delGoods(request);
		}else if("GetSeq".equals(op)){
			return getSeq(request);
//		}else if("GetOutBill".equals(op)){
//			return getOutBill(request);
		}else{
			return "";
		}
	}
	
	/**
	 * 扫描出库，查询订单
	 * 1、如果订单已结案，已强结，不允许出库
	 * 2、如果订单已有一个草稿状态的出库单，直接调草稿。
	 * 3、如果订单已有一个非草稿，但未审核完毕的出库单，提示审核完成，不允许继续出库
	 * 4、什么都没有，调订单详情显示
	 * @param request
	 * @return
	 */
//	public String getOutBill(HttpServletRequest request){
//		String ret="";
//		final HashMap rMap = new HashMap();
//		final String BillNo = request.getParameter("BillNo");
//		
//		final LoginBean lb = (LoginBean)request.getSession().getAttribute("LoginBean");
//		if(lb == null){
//			rMap.put("code", "NOLOGIN");
//        	rMap.put("msg", "登陆失效");
//        	ret = gson.toJson(rMap);
//    		return ret;
//		}
//		
//		String sql  = " select id,StatusId from tblSalesOrder where BillNo = ? ";
//		ArrayList param = new ArrayList();
//		param.add(BillNo);
//		Result rs = this.sqlList(sql, param);
//		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
//			rMap.put("code", "ERROR");
//        	rMap.put("msg", rs.getRetVal());
//        	ret = gson.toJson(rMap);
//    		return ret;
//		}
//		if(((ArrayList)rs.retVal).size()==0){
//			rMap.put("code", "ERROR");
//        	rMap.put("msg", "未找到该订单信息，请确认订单编号是否正确");
//        	ret = gson.toJson(rMap);
//    		return ret;
//		}
//		Object[] order = ((ArrayList<Object[]>)rs.retVal).get(0);
//		final String salesOrderId = order[0]+"";
//		String statusId = order[1]+"";
//		if("1".equals(statusId)){
//			rMap.put("code", "ERROR");
//        	rMap.put("msg", "本单已经结案，不可以出库");
//        	ret = gson.toJson(rMap);
//    		return ret;
//		}else if("2".equals(statusId)){
//			rMap.put("code", "ERROR");
//        	rMap.put("msg", "本单已经强制结案，不可以出库");
//        	ret = gson.toJson(rMap);
//    		return ret;
//		}
//		//查，有无未审核出库单
//		sql  = " select id from tblSalesOutStock where workFlowNodeName = 'notApprove'  and  SalesOrderID=? ";
//		param = new ArrayList();
//		param.add(salesOrderId);
//		rs = this.sqlList(sql, param);
//		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
//			rMap.put("code", "ERROR");
//        	rMap.put("msg", rs.getRetVal());
//        	ret = gson.toJson(rMap);
//    		return ret;
//		}
//		if(((ArrayList)rs.retVal).size() > 0){
//			rMap.put("code", "ERROR");
//        	rMap.put("msg", "本订单当前已经有一个正在审核中的出库单，请先审核完毕！");
//        	ret = gson.toJson(rMap);
//    		return ret;
//		}
//		
//		//查，有无草稿状态出库单
//		sql  = " select id from tblSalesOutStock where workFlowNodeName = 'draft'  and  SalesOrderID=? ";
//		param = new ArrayList();
//		param.add(salesOrderId);
//		rs = this.sqlList(sql, param);
//		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
//			rMap.put("code", "ERROR");
//        	rMap.put("msg", rs.getRetVal());
//        	ret = gson.toJson(rMap);
//    		return ret;
//		}
//		if(((ArrayList)rs.retVal).size() > 0){
//			sql  = " select SourceId,sum(Qty) Qty from tblSalesOutStockDet where f_ref=? group by SourceId ";
//			param = new ArrayList();
//			param.add((((ArrayList<Object[]>)rs.retVal).get(0))[0]+"");
//			rs = this.sqlListMap(sql, param);
//			rMap.put("code", "OK");
//        	rMap.put("salesOrderId", salesOrderId);
//        	rMap.put("list", rs.retVal);
//        	ret = gson.toJson(rMap);
//    		return ret;
//		}else{
//			//先创建一个出库单草稿
//			final Result rst = new Result();
//	        int retCode = DBUtil.execute(new IfDB() {
//	            public int exec(Session session) {
//	                session.doWork(new Work() {
//	                    public void execute(Connection conn) throws
//	                            SQLException {
//	                    	PreparedStatement cs=null;
//	                        try {
//	                        	String outStockId=IDGenerater.getId();
//	                        	String sql = "INSERT INTO [tblSalesOutStock]([id],[BillDate],[InVoiceType],[DepartmentCode],[CompanyCode],[StockCode],[BackAmount],[AlrAccAmt],"
//	                        			+ "[CurBackAmount],[CurAlrAccAmt],[CurPreAccAmt],[createBy],[lastUpdateBy],[createTime],[lastUpdateTime],[statusId],[SaleOutStockID],"
//	                        			+ "[EmployeeID],[PeriodYear],[PeriodMonth],[Period],[NeedReturnAmt],[RelationTabID],[SalesOrderNo],[SalesOrderID],[TotalAlrAccAmt],"
//	                        			+ "[AcceptDate],[Remark],[Attachment],[SCompanyID],[DiscountAmount],[CurTotalAlrAccAmt],[CurNeedReturnAmt],[CurDiscountAmount],[classCode],"
//	                        			+ "[RowON],[InvoiceStatus],[workFlowNodeName],[workFlowNode],[FreightCom],[printCount],[ConfirmID],[ConfirmDate],[BillNo],[ChangeAmt],"
//	                        			+ "[InvoiceAmount],[checkPersons],[TrackNo],[AccAmt],[FactIncome],[NoInvoiceAmount],[CurProceDureExp],[BuyInID],[finishTime],"
//	                        			+ "[PresentTotalAmt],[AfterChangeAmt],[CheckPersont],[CertificateNo],[deputyRece],[deputyReceAmt],[hashDeputyReceAmt],[EmpAccAmt],"
//	                        			+ "[ProjectCode],[Discount],[DisAmount],[Tax],[CurrencyRate],[TotalAmount],[TotalTaxAmount],[TotalCoTaxAmt],[CurTotalAmount],[Currency])"
//	                        			+ " select ?,CONVERT(varchar(10),getdate(),120),[InVoiceType],?,"
//	                        			+ "[CompanyCode],[StockCode],0,0,0,0,0,?,?,CONVERT(varchar(19),getdate(),120),CONVERT(varchar(19),getdate(),120),0,'',"
//	                        			+ "?,0,0,0,0,'',(select top 1 BillNo from tblSalesOrder where id=?),?,0,'','','',"
//	                        			+ "[SCompanyID],0,0,0,0,[classCode],0,0,'draft','0','',0,'','','',0,0,'','',0,0,0,0,'','',0,0,'','','',0,0,0,'',[Discount],0,[Tax],"
//	                        			+ "[CurrencyRate],0,0,0,0,[Currency] from tblSalesOrder where id=?";
//	                        	cs= conn.prepareStatement(sql);
//	                        	cs.setString(1, outStockId);
//	                        	cs.setString(2, lb.getDepartCode());
//	                        	cs.setString(3, lb.getId());
//	                        	cs.setString(4, lb.getId());
//	                        	cs.setString(5, lb.getId());
//	                        	cs.setString(6, salesOrderId);
//	                        	cs.setString(7, salesOrderId);
//	                        	cs.setString(8, salesOrderId);
//	                            cs.execute();
//	                            sql = "INSERT INTO [tblSalesOutStockDet]([id],[f_ref],[GoodsCode],[BackQty],[InvoiceQty],[StockCode],[SecUnitQty],[FactOutQty],[Remark],"
//	                            		+ "[SCompanyID],[classCode],[RowON],[BatchNo],[InvoiceStatus],[SourceID],[PresentSampleType],[yearNO],[Inch],[Seq],[Hue],"
//	                            		+ "[Availably],[ProDate],[CheckBarCode],[AccountingPrice],[lastQty],[NoInvoiceAmount],[InvoiceAmount],[BackAmount],"
//	                            		+ "[TotalReceiveAmt],[NeedReceiveAmt],[PreferAmount],[SalesOrder],[UseQty],[PreferBackAmount],[InvoiceAmountH],"
//	                            		+ "[AfterChangeAmt],[PresentAmt],[PresentPrice],[SalesOrderID],[PreReceiveAmt],[ReceiveAmt],[CurPreReceiveAmt],"
//	                            		+ "[CurReceiveAmt],[CurTotalReceiveAmt],[trackNo],[NoInvoiceQty],[BuyQty],[NotBuyQty],[PayQty],[NotPayQty],[BackPayQty],"
//	                            		+ "[ViewTotalQty],[Qty],[Price],[Amount],[UnitBaseQty],[UnitQty],[UnitPrice],[Discount],[DisPrice],[DisBackAmt],[DisAmount],"
//	                            		+ "[TaxPrice],[TaxAmount],[CoTaxAmt],[CurPrice],[CurAmount],[SecQty],[BaseQty],[UnitIntQty],[CostPrice],[CostAmount],[inPrice],"
//	                            		+ "[hasCheck]) select SUBSTRING(convert(varchar(40),newid()),1,30),?,[GoodsCode],0,0,'',0,0,'',[SCompanyID],[classCode],[RowON],"
//	                            		+ "'','False',id,[PresentSampleType],'',[Inch],'',[Hue],[Availably],[ProDate],'',0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,?,0,0,0,0,0,"
//	                            		+ "[trackNo],0,0,0,0,0,0,0,Qty,[Price],0,0,0,0,[Discount],0,0,0,[TaxPrice],0,0,[CurPrice],0,0,0,0,[CostPrice],[CostAmount],0,0 "
//	                            		+ "from tblSalesOrderDet where f_ref=? and NotOutQty>0 and GoodsCode not like '00001%' ";
//	                            BaseEnv.log.debug("扫描出库，礼品sql="+sql);
//	                        	cs= conn.prepareStatement(sql);
//	                        	cs.setString(1, outStockId);
//	                        	cs.setString(2, salesOrderId);
//	                        	cs.setString(3, salesOrderId);
//	                            cs.execute();
//	                        } catch (Exception ex) {
//	                            BaseEnv.log.error("HFData.getOutBill",ex);
//	                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//	                            rst.setRetVal(ex.getMessage());
//	                            return;
//	                        }
//	                    }
//	                });
//	                return rst.getRetCode();
//	            }
//	        });
//	        rst.setRetCode(retCode);
//			if(retCode==ErrorCanst.DEFAULT_SUCCESS){
//				rMap.put("code", "OK");
//	        	rMap.put("salesOrderId", salesOrderId);
//	        	rMap.put("list", new ArrayList());
//	        	ret = gson.toJson(rMap);
//	    		return ret;
//			}else{
//				rMap.put("code", "ERROR");
//	        	rMap.put("msg", rst.retVal==null||rst.retVal.equals("")?"执行失败":rst.getRetVal());
//	        	ret = gson.toJson(rMap);
//	    		return ret;
//			}
//			
//			
//		}
//	}
	/**
	 * 出库扫描 ，扫描物流码
	 * 1、判断没有草稿，则自动建立草稿。
	 * 2、把序列号插入草稿
	 * @param request
	 * @return
	 */
	public String getSeq(HttpServletRequest request){
		String ret="";
		final HashMap rMap = new HashMap();
		
		final String Seq = request.getParameter("Seq");
		final String SalesOutId = request.getParameter("SalesOutId");
		
		BaseEnv.log.debug("扫描出库：序号："+Seq);
		
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                            SQLException {
                    	CallableStatement cs=null;
                        try {
                        	String sql = "{call proc_scanSeq(?,?,?,?)}";
                        	cs= conn.prepareCall(sql);
                        	cs.setString(1, SalesOutId);
                        	cs.setString(2, Seq);
                            cs.registerOutParameter(3, Types.INTEGER);
                            cs.registerOutParameter(4, Types.VARCHAR, 50);
                            cs.execute();
                            rst.setRetCode(cs.getInt(3));
                            rst.setRetVal(cs.getString(4));
                            
                            if(BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT){
                	            SQLWarning warn = cs.getWarnings();
                	            while(warn != null){  
                	            	if(warn.getMessage() !=null && warn.getMessage().indexOf("正在直接执行 SQL；无游标") == -1){
                	            		BaseEnv.log.debug("存储过程内部信息： "+warn.getMessage());
                	            	}
                	            	warn = warn.getNextWarning();
                	            }
                            }	
                        	
                        }catch(SQLException ex){
                        	try{
                        		if(BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT){
                    	            SQLWarning warn = cs.getWarnings();
                    	            while(warn != null){  
                    	            	if(warn.getMessage() !=null && warn.getMessage().indexOf("正在直接执行 SQL；无游标") == -1){
                    	            		BaseEnv.log.debug("存储过程内部信息： "+warn.getMessage());
                    	            	}
                    	            	warn = warn.getNextWarning();
                    	            }
                                }	
                        	}catch(SQLException ex2){
                        		BaseEnv.log.error("HFData.getSeq Error",ex);
                        	}
                        	BaseEnv.log.error("HFData.getSeq Error",ex);
                        	BaseEnv.log.error("HFData.getSeq",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            rst.setRetVal(ex.getMessage());
                            return;
                        } catch (Exception ex) {
                            BaseEnv.log.error("HFData.getSeq",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            rst.setRetVal(ex.getMessage());
                            return;
                        }
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        BaseEnv.log.debug("扫描出库：序号："+Seq+";结果="+rst.getRetVal());
        
        String retstr=null;
        if(retCode == ErrorCanst.DEFAULT_SUCCESS){
	        rMap.put("code", "OK");
	        retstr = rst.getRetVal()+"";
        }else{
        	rMap.put("code", "ERROR");
        	rMap.put("msg", rst.getRetVal());
        }
		ret = gson.toJson(rMap);
		if(retstr != null) { 
			ret = ret.substring(0,ret.length()-1)+","+retstr+"}";
		} 
		return ret;
	}
	
	
	
	public Result putGoods(Connection conn,final String userId,final String GoodsCode,final String WorkOrder,final String BatchNo,final String yearNO,final String Seq,final HashMap rMap){
		CallableStatement cs=null;
		Result rst = new Result();
        try {
        	String sql = "{call proc_scanInStock(?,?,?,?,?,?,?,?,?)}";
        	cs= conn.prepareCall(sql);
        	cs.setString(1, GoodsCode);
        	cs.setString(2, WorkOrder);
        	cs.setString(3, BatchNo);
        	cs.setString(4, yearNO);
        	cs.setString(5, Seq);
        	cs.setString(6, userId);
        	cs.registerOutParameter(7, Types.INTEGER);
            cs.registerOutParameter(8, Types.INTEGER);
            cs.registerOutParameter(9, Types.VARCHAR, 50);
            cs.execute();
            int yearNoNum = cs.getInt(7);
            rst.setRetCode(cs.getInt(8));
            rst.setRetVal(cs.getString(9));
            if(rst.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
            	rMap.put("yearNoNum", yearNoNum+"");
            }
            
            if(BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT){
	            SQLWarning warn = cs.getWarnings();
	            while(warn != null){  
	            	if(warn.getMessage() !=null && warn.getMessage().indexOf("正在直接执行 SQL；无游标") == -1){
	            		BaseEnv.log.debug("存储过程内部信息： "+warn.getMessage());
	            	}
	            	warn = warn.getNextWarning();
	            }
            }	
        	return rst;
        }catch(SQLException ex){
        	try{
        		if(BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT){
    	            SQLWarning warn = cs.getWarnings();
    	            while(warn != null){  
    	            	if(warn.getMessage() !=null && warn.getMessage().indexOf("正在直接执行 SQL；无游标") == -1){
    	            		BaseEnv.log.debug("存储过程内部信息： "+warn.getMessage());
    	            	}
    	            	warn = warn.getNextWarning();
    	            }
                }	
        	}catch(SQLException ex2){
        		BaseEnv.log.error("HFData.putStock Error",ex);
        	}
        	BaseEnv.log.error("HFData.putStock Error",ex);
        	BaseEnv.log.error("HFData.putStock",ex);
            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
            rst.setRetVal(ex.getMessage());
            return rst;
        } catch (Exception ex) {
            BaseEnv.log.error("HFData.putStock",ex);
            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
            rst.setRetVal(ex.getMessage());
            return rst;
        }
	}
	
	public String putGoods(HttpServletRequest request){
		String ret="";
		final HashMap rMap = new HashMap();
		final LoginBean lb = (LoginBean)request.getSession().getAttribute("LoginBean");
		if(lb == null){
			rMap.put("code", "NOLOGIN");
        	rMap.put("msg", "登陆失效");
        	ret = gson.toJson(rMap);
    		return ret;
		}
		
		final String GoodsCode = request.getParameter("GoodsCode");
		final String WorkOrder = request.getParameter("WorkOrder");
		final String BatchNo = request.getParameter("BatchNo");
		final String yearNO = request.getParameter("yearNO");
		final String Seq = request.getParameter("Seq");
		
		BaseEnv.log.debug("扫描入库：商品："+GoodsCode+";工令："+WorkOrder+";批号："+BatchNo+";箱号："+yearNO+";物流码："+Seq);
		
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                            SQLException {
                    	
                    	Result rs=putGoods(conn, lb.getId(), GoodsCode, WorkOrder, BatchNo, yearNO, Seq, rMap);
                    	
                		CallableStatement cs=null;

                		rst.setRetCode(rs.getRetCode());
                		rst.setRetVal(rs.retVal);
                		
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        if(retCode == ErrorCanst.DEFAULT_SUCCESS){
	        rMap.put("code", "OK");
        }else{
        	rMap.put("code", "ERROR");
        	rMap.put("msg", rst.getRetVal());
        }
		
		ret = gson.toJson(rMap);
		return ret;
	}
	
	public String getBill(HttpServletRequest request){
		String ret="";
		final HashMap rMap = new HashMap();
		final LoginBean lb = (LoginBean)request.getSession().getAttribute("LoginBean");
		if(lb == null){
			rMap.put("code", "NOLOGIN");
        	rMap.put("msg", "登陆失效");
        	ret = gson.toJson(rMap);
    		return ret;
		}
		
		final String BillNo = request.getParameter("BillNo");
		
		BaseEnv.log.debug("取出库单：商品："+BillNo);
		
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                            SQLException {
                    	String sql  = " select a.id, BillNo,BillDate,ComNumber,ComFullName,a.workFlowNodeName from tblSalesOutStock a join tblCompany b on a.CompanyCode=b.classCode "
                    			+ "where BillNo=? ";
                    	PreparedStatement pst = conn.prepareStatement(sql);
                    	pst.setString(1, BillNo);
                    	ResultSet rset = pst.executeQuery();
                    	String id="";
                    	if(rset.next()){
                    		rMap.put("BillNo", BillNo);
                    		rMap.put("BillDate", rset.getString("BillDate"));
                    		rMap.put("ComNumber", rset.getString("ComNumber"));
                    		rMap.put("ComFullName", rset.getString("ComFullName"));
                    		id = rset.getString("id");
                    		rMap.put("id", id);
                    		
                    		String workFlowNodeName = rset.getString("workFlowNodeName");
                    		if(workFlowNodeName.equals("finish")){
                    			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                        		rst.setRetVal("本单已经审核完成");
                        		return;
                    		}else if(workFlowNodeName.equals("draft")){
                    			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                        		rst.setRetVal("本单是草稿，请先过帐再扫描");
                        		return;
                    		}
                    		
                    	}else{
                    		rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                    		rst.setRetVal("单据编号"+BillNo+"不存在");
                    		return;
                    	}
                    	
                    	sql = " select a.id,GoodsCode,GoodsNumber,Qty,ScanQty,Qty-ScanQty NoScanQty from tblSalesOutStockDet a join tblGoods b on a.GoodsCode=b.classCode where f_ref=?  ";
                    	pst = conn.prepareStatement(sql);
                    	pst.setString(1, id);
                    	rset = pst.executeQuery();
                    	ArrayList gList = new ArrayList();
                    	rMap.put("Dets", gList);
                    	while(rset.next()){
                    		HashMap map = new HashMap();
                    		map.put("GoodsNumber", rset.getString("GoodsNumber"));
                    		map.put("GoodsCode", rset.getString("GoodsCode"));
                    		map.put("Qty", rset.getInt("Qty"));
                    		map.put("ScanQty", rset.getInt("ScanQty"));
                    		map.put("NoScanQty", rset.getInt("NoScanQty"));
                    		map.put("id", rset.getString("id"));
                    		gList.add(map);
                    	}
                    	
                    	sql = " select yearNo,Seq from tblSalesOutStockScan a  where f_ref=?  ";
                    	pst = conn.prepareStatement(sql);
                    	pst.setString(1, id);
                    	rset = pst.executeQuery();
                    	gList = new ArrayList();
                    	rMap.put("Seqs", gList);
                    	while(rset.next()){
                    		HashMap map = new HashMap();
                    		map.put("yearNo", rset.getString("yearNo"));
                    		map.put("Seq", rset.getString("Seq"));
                    		gList.add(map);
                    	}
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        if(retCode == ErrorCanst.DEFAULT_SUCCESS){
	        rMap.put("code", "OK");
        }else{
        	rMap.put("code", "ERROR");
        	rMap.put("msg", rst.getRetVal());
        }
		
		ret = gson.toJson(rMap);
		return ret;
	}
	
	public String delGoods(HttpServletRequest request){
		String ret="";
		final HashMap rMap = new HashMap();
		final LoginBean lb = (LoginBean)request.getSession().getAttribute("LoginBean");
		if(lb == null){
			rMap.put("code", "NOLOGIN");
        	rMap.put("msg", "登陆失效");
        	ret = gson.toJson(rMap);
    		return ret;
		}
		
		final String Seq = request.getParameter("Seq");
		BaseEnv.log.debug("扫描入库：删除商品：物流码："+Seq);
		
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                            SQLException {
                    	
                    	CallableStatement cs=null;
                        try {
                        	String sql = "{call proc_scanDelStock(?,?,?,?)}";
                        	cs= conn.prepareCall(sql);
                        	cs.setString(1, Seq);
                        	cs.setString(2, lb.getId());
                            cs.registerOutParameter(3, Types.INTEGER);
                            cs.registerOutParameter(4, Types.VARCHAR, 50);
                            cs.execute();
                            rst.setRetCode(cs.getInt(3));
                            rst.setRetVal(cs.getString(4));

                            if(BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT){
                	            SQLWarning warn = cs.getWarnings();
                	            while(warn != null){  
                	            	if(warn.getMessage() !=null && warn.getMessage().indexOf("正在直接执行 SQL；无游标") == -1){
                	            		BaseEnv.log.debug("存储过程内部信息： "+warn.getMessage());
                	            	}
                	            	warn = warn.getNextWarning();
                	            }
                            }	
                        }catch(SQLException ex){
                        	try{
                        		if(BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT){
                    	            SQLWarning warn = cs.getWarnings();
                    	            while(warn != null){  
                    	            	if(warn.getMessage() !=null && warn.getMessage().indexOf("正在直接执行 SQL；无游标") == -1){
                    	            		BaseEnv.log.debug("存储过程内部信息： "+warn.getMessage());
                    	            	}
                    	            	warn = warn.getNextWarning();
                    	            }
                                }	
                        	}catch(SQLException ex2){
                        		BaseEnv.log.error("HFData.delStock Error",ex);
                        	}
                        	BaseEnv.log.error("HFData.delStock Error",ex);
                        	BaseEnv.log.error("HFData.delStock",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            rst.setRetVal(ex.getMessage());
                        } catch (Exception ex) {
                            BaseEnv.log.error("HFData.delStock",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            rst.setRetVal(ex.getMessage());
                        }
                		
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        if(retCode == ErrorCanst.DEFAULT_SUCCESS){
	        rMap.put("code", "OK");
        }else{
        	rMap.put("code", "ERROR");
        	rMap.put("msg", rst.getRetVal());
        }
		
		ret = gson.toJson(rMap);
		return ret;
	}
	public String getGoods(HttpServletRequest request){
		String ret="";
		HashMap rMap = new HashMap();
		String barCode = request.getParameter("barCode");
		
		String sql  = " select classCode GoodsCode,GoodsNumber,GoodsFullName,GoodsSpec,barCode,outPackageNum from tblGoods where barCode = ? ";
		ArrayList param = new ArrayList();
		param.add(barCode);
		Result rs = sqlListMap(sql,param);
		if(rs.retCode== ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<HashMap> list = ((ArrayList<HashMap>)rs.retVal);
			if(list.size() ==0){
				rMap.put("code", "ERROR");
				rMap.put("msg", "条码"+barCode+"对应的商品不存在，请重新扫描正确的商品条码");
			}else{
				rMap.put("code", "OK");
				rMap.putAll(list.get(0));
				//查询相关工令
				sql = " select PDWorkOrder.id,PDWorkOrder.BillNo,PDWorkOrder.BillDate,PDWorkOrder.WOClassType,"
						+ "(case when PDWorkOrder.WOClassType='1' then tblCompany.ComName else PDWorkShop.name end) WorkShop,"
						+ "PDWorkOrder.Qty- (select count(0) from tblStockDet where TrackNo=PDWorkOrder.id and instoreQty>0) NoInNum "
						+ " from PDWorkOrder left join tblCompany on PDWorkOrder.CompanyCode=tblCompany.classCode"
						+ " left join PDWorkShop on PDWorkShop.id=PDWorkOrder.WorkShop where PDWorkOrder.GoodsCode = ? "
						+ " and PDWorkOrder.WorkFlowNodeName='finish' "
						+ " and PDWorkOrder.statusId=0 order by PDWorkOrder.BillDate ";
				param.clear();
				param.add(rMap.get("GoodsCode"));
				rs = sqlListMap(sql,param);
				if(rs.retCode== ErrorCanst.DEFAULT_SUCCESS){
					list = ((ArrayList<HashMap>)rs.retVal);
					if(list.size() ==0){
						rMap.put("code", "ERROR");
						rMap.put("msg", "条码"+barCode+"对应的商品没有任何工令单，请先下工令再缴库");
					}else{
						rMap.put("WorkOrder", list);
					}
				}else{
					rMap.put("code", "ERROR");
					rMap.put("msg", rs.retVal==null?"查询失败":rs.retVal);
				}
			}
		}else{
			rMap.put("code", "ERROR");
			rMap.put("msg", rs.retVal==null?"查询失败":rs.retVal);
		}
		ret = gson.toJson(rMap);
		return ret;
	}
	
	private void updateImportInfo(final String id,final int i){
		Thread th = new Thread(){
			public void run(){
				
				final Result rst = new Result() ;
				int retCode = DBUtil.execute(new IfDB(){
					public int exec(Session session){
						session.doWork(new Work(){
							public void execute(Connection conn) throws SQLException {
								try{
									
									PreparedStatement upst = conn.prepareStatement("update HFSCode set importStatus=? where id=?");
									upst.setString(1, "正在导入...("+i+")");
									upst.setString(2, id);
									upst.execute();
								}catch(Exception ex){
									BaseEnv.log.error("HFData.updateImportInfo Error:",ex);
								}
							}
						}) ;
						return rst.getRetCode() ;
					}
			    }) ;
				rst.setRetCode(retCode) ;
				
			}
		};
		th.setName("ImportFangWeiMaInfo");
		th.start();
	}
	
	/**
	 * 导入序列号
	 * @param data
	 * @param id
	 * @throws Exception
	 */  
	public void importSeq(final Connection conn,final String data,String WorkOrder,final String userId,HashMap rMap,final String BillDate) throws Exception{
		CallableStatement cs=null;
		String[] ds = data.split("\\n");
		if( ds.length == 0) return;
		String qsql = " select top 1 1 a from [AIOFW].[dbo].[stockDetail] where seq=? ";
		PreparedStatement qpst = conn.prepareStatement(qsql);
		for(int i=0;i<ds.length;i++){
			String one = ds[i];
			
			if(one.trim().length() > 0&&one.split(",").length>3){
				String[] os = one.split(",");
				
				String GoodsCode=os[0].trim();
				String sql = " select classCode from tblGoods where GoodsNumber=? ";
				PreparedStatement pst = conn.prepareStatement(sql);
				pst.setString(1, GoodsCode);
				ResultSet rr = pst.executeQuery();
				if(rr.next()){
					GoodsCode=rr.getString(1);
				}else{
					throw new Exception("商品编号"+GoodsCode+"不存在");
				}
				
				if(WorkOrder==null || WorkOrder.equals("")) 
					WorkOrder="期初";
				String BatchNo=os[1].trim();
				String yearNO=os[2].trim();
				String Seq=os[3].trim();
				
				qpst.setString(1, Seq);
				ResultSet rst = qpst.executeQuery();
				boolean hasExist = false;
				if(rst.next()){
					hasExist = true;
				}
				
				if(!hasExist){
                    try {
                    	sql = "{call proc_scanInStock(?,?,?,?,?,?,?,?,?,?)}";
                    	cs= conn.prepareCall(sql);
                    	cs.setString(1, GoodsCode);
                    	cs.setString(2, WorkOrder);
                    	cs.setString(3, BatchNo);
                    	cs.setString(4, yearNO);
                    	cs.setString(5, Seq);
                    	cs.setString(6, userId);
                    	cs.setString(7,BillDate);
                    	cs.registerOutParameter(8, Types.INTEGER);
                        cs.registerOutParameter(9, Types.INTEGER);
                        cs.registerOutParameter(10, Types.VARCHAR, 50);
                        cs.execute();
                        int retCode = cs.getInt(9);
                        if(retCode!=0){
                        	throw new Exception(cs.getString(10));
                        }
                        
                        
                        if(BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT){
            	            SQLWarning warn = cs.getWarnings();
            	            while(warn != null){  
            	            	if(warn.getMessage() !=null && warn.getMessage().indexOf("正在直接执行 SQL；无游标") == -1){
            	            		BaseEnv.log.debug("存储过程内部信息： "+warn.getMessage());
            	            	}
            	            	warn = warn.getNextWarning();
            	            }
                        }	
                    	
                    }catch(SQLException ex){
                    	try{
                    		if(BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT){
                	            SQLWarning warn = cs.getWarnings();
                	            while(warn != null){  
                	            	if(warn.getMessage() !=null && warn.getMessage().indexOf("正在直接执行 SQL；无游标") == -1){
                	            		BaseEnv.log.debug("存储过程内部信息： "+warn.getMessage());
                	            	}
                	            	warn = warn.getNextWarning();
                	            }
                            }	
                    	}catch(SQLException ex2){
                    		BaseEnv.log.error("HFData.importSeq Error",ex);
                    	}
                    	BaseEnv.log.error("HFData.importSeq Error",ex);
                    	BaseEnv.log.error("HFData.importSeq",ex);
                    	
                    	throw ex;
                    }
					
					BaseEnv.log.debug("HFData.importSeq insert:"+one);
				}else{
					BaseEnv.log.debug("HFData.importSeq Has Double:"+one);
				}
			}
		}
		
	}
	
	
	/**
	 * 导入序列号
	 * @param data
	 * @param id
	 * @throws Exception
	 */  
	public void importSeqDel(final Connection conn,final String data,final String userId,HashMap rMap,final String BillDate) throws Exception{
		CallableStatement cs=null;
		String[] ds = data.split("\\n");
		if( ds.length == 0) return;
		for(int i=0;i<ds.length;i++){
			String one = ds[i];
			
			if(one.trim().length() > 0&&one.split(",").length>3){
				String[] os = one.split(",");
				
				String Seq=os[3].trim();
				
                try {
                	String sql = "{call proc_scanDelStock(?,?,?,?)}";
                	cs= conn.prepareCall(sql);
                	cs.setString(1, Seq);
                	cs.setString(2, userId);
                    cs.registerOutParameter(3, Types.INTEGER);
                    cs.registerOutParameter(4, Types.VARCHAR, 50);
                    cs.execute();
                    int retCode = cs.getInt(3);
                    if(retCode!=0){
                    	throw new Exception(cs.getString(4));
                    }
                    
                    
                    if(BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT){
        	            SQLWarning warn = cs.getWarnings();
        	            while(warn != null){  
        	            	if(warn.getMessage() !=null && warn.getMessage().indexOf("正在直接执行 SQL；无游标") == -1){
        	            		BaseEnv.log.debug("存储过程内部信息： "+warn.getMessage());
        	            	}
        	            	warn = warn.getNextWarning();
        	            }
                    }	
                	
                }catch(SQLException ex){
                	try{
                		if(BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT){
            	            SQLWarning warn = cs.getWarnings();
            	            while(warn != null){  
            	            	if(warn.getMessage() !=null && warn.getMessage().indexOf("正在直接执行 SQL；无游标") == -1){
            	            		BaseEnv.log.debug("存储过程内部信息： "+warn.getMessage());
            	            	}
            	            	warn = warn.getNextWarning();
            	            }
                        }	
                	}catch(SQLException ex2){
                		BaseEnv.log.error("HFData.importSeqDel Error",ex);
                	}
                	BaseEnv.log.error("HFData.importSeqDel Error",ex);
                	BaseEnv.log.error("HFData.importSeqDel",ex);
                	
                	throw ex;
                }
				
				BaseEnv.log.debug("HFData.importSeqDel delete:"+one);
			}
		}
		
	}
	
	public void importFW(final String data,final String id) throws Exception{
		Thread th = new Thread(){
			public void run(){
				
				final Result rst = new Result() ;
				int retCode = DBUtil.execute(new IfDB(){
					public int exec(Session session){
						session.doWork(new Work(){
							public void execute(Connection conn) throws SQLException {
								try{
									String[] ds = data.split("\\n");
									if( ds.length == 0) return;
									String sql = "INSERT INTO [AIOFW].[dbo].[t_barcode_query]([c_bq_barcode],[c_bq_barcode2],[c_bq_sign]) values(?,?,0)";
									String qsql = " select top 1 1 a from [AIOFW].[dbo].[t_barcode_query] where c_bq_barcode=? ";
									PreparedStatement pst = conn.prepareStatement(sql);
									PreparedStatement qpst = conn.prepareStatement(qsql);
									long begin = System.currentTimeMillis();
									for(int i=0;i<ds.length;i++){
										String one = ds[i];
										if (System.currentTimeMillis() - begin > 15000 ){
											updateImportInfo(id, i);
											begin = System.currentTimeMillis();
										}
										if(one.trim().length() > 0&&one.split(",").length>1){
											String seq = one.split(",")[0].trim();
											String fwm = one.split(",")[1].trim();
											
											qpst.setString(1, seq);
											ResultSet rst = qpst.executeQuery();
											boolean hasExist = false;
											if(rst.next()){
												hasExist = true;
											}
											if(!hasExist){
												pst.setString(1, seq);
												pst.setString(2, fwm);
												pst.addBatch();
												BaseEnv.log.debug("HFData.importFw insert:"+one);
											}else{
												BaseEnv.log.debug("HFData.importFw Has Double:"+one);
											}
										}
									}
									pst.executeBatch();
									sql = " update HFSCode set importStatus='导入完毕！' where id=? ";
									pst = conn.prepareStatement(sql);
									pst.setString(1, id);
									pst.execute();
								}catch(Exception ex){
									BaseEnv.log.error("HFData.importFW Error:",ex);
								}
							}
						}) ;
						return rst.getRetCode() ;
					}
			    }) ;
				rst.setRetCode(retCode) ;
				
			}
		};
		th.setName("ImportFangWeiMa");
		th.start();
	}
	public void importFWDel(final String data,final Connection conn) throws Exception{
		String[] ds = data.split("\\n");
		if( ds.length == 0) return;
		String sql = "delete [AIOFW].[dbo].[t_barcode_query] where [c_bq_barcode]=? ";
		PreparedStatement pst = conn.prepareStatement(sql);
		for(String one :ds){
			if(one.trim().length() > 0&&one.split(",").length>1){
				String seq = one.split(",")[0].trim();
				String fwm = one.split(",")[1].trim();
				
				pst.setString(1, seq);
				pst.addBatch();
				BaseEnv.log.debug("HFData.importFw delete:"+one);
			}
		}
		pst.executeBatch();
	}
}
