package com.menyi.web.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.PayDet;
import com.menyi.aio.bean.SaleReceiveDet;
import com.menyi.aio.bean.ViewRefPay;
import com.menyi.aio.bean.ViewRefReceive;

public class RefPayMgt {
	public Result getBillName(final String locale,final String allBillType){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                    	
                        Connection conn = connection;
                        HashMap<String, String> allBillName =new HashMap<String, String>();
                        Statement s = conn.createStatement();
                        if("".equals(allBillType)){
                        	rs.setRetVal(allBillName);
                        	return;
                        }
                        String sql = "select l."+locale+",t.tablename from tbllanguage l,tbldbtableinfo t where l.id = t.languageId and t.tablename in ("+allBillType+")";
              
                        ResultSet rst = s.executeQuery(sql);
                       
                        
                        while(rst.next()){
                        	allBillName.put(rst.getString(2), rst.getString(1));
                        }
                        rs.setRetVal(allBillName);
                        
                    }
                });
                return rs.getRetCode();
            }
        });
		rs.setRetCode(retCode);           
		
		
		return rs;
	}
	
	public Result getPayDet(final String BillNo){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        Statement s = conn.createStatement();
                        String sql = "select * from tblpaydet where f_ref = (select id from tblPay where BillNo = '"+BillNo+"')";
                        ResultSet rst = s.executeQuery(sql);
                        ArrayList<PayDet> vs = new ArrayList<PayDet>();
                        while(rst.next()){
                        	PayDet p = new PayDet();
                        	p.setBackAmt(rst.getDouble("backAmt"));
                        	p.setBillAmt(rst.getDouble("billAmt"));
                        	p.setBillName(rst.getString("billName"));
                        	p.setBuyOrderID(rst.getString("buyOrderID"));
                        	p.setBuyOrderNo(rst.getString("buyOrderNo"));
                        	p.setClassCode(rst.getString("classCode"));
                        	p.setCurBackAmt(rst.getDouble("curBackAmt"));
                        	p.setCurBillAmt(rst.getDouble("curBillAmt"));
                        	p.setCurSettledAmt(rst.getDouble("curSettledAmt"));
                        	p.setCurWexeBalAmt(rst.getDouble("curWexeBalAmt"));
                        	p.setDetOrderNo(rst.getString("detOrderNo"));
                        	p.setExeBalAmt(rst.getDouble("exeBalAmt"));
                        	p.setExeBalFcAmt(rst.getDouble("exeBalFcAmt"));
                        	p.setF_ref(rst.getString("f_ref"));
                        	p.setId(rst.getString("id"));
                        	p.setMoneyID(rst.getString("moneyID"));
                        	p.setMoneyRate(rst.getString("moneyRate"));
                        	p.setPayBillType(rst.getString("payBillType"));
                        	p.setRefbillID(rst.getString("refbillID"));
                        	p.setRefBillNo(rst.getString("refBillNo"));
                        	p.setRefInvoiceID(rst.getString("refInvoiceID"));
                        	p.setRemark(rst.getString("remark"));
                        	p.setRowON(rst.getString("rowON"));
                        	p.setSCompanyID(rst.getString("SCompanyID"));
                        	p.setSettledAmt(rst.getDouble("settledAmt"));
                        	p.setWexeBalAmt(rst.getDouble("wexeBalAmt"));
                        	p.setWorkFlowNode(rst.getString("workFlowNode"));
                        	p.setWorkFlowNodeName(rst.getString("workFlowNodeName"));
                        	
                        	
                        	vs.add(p);
                        }
                        rs.setRetVal(vs);
                        
                    }
                });
                return rs.getRetCode();
            }
        });
		rs.setRetCode(retCode);           
		
		
		return rs;
	}
	
	public Result getRefPay(final String ComFullName,final String paytypeID){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        Statement s = conn.createStatement();
                        String sql = "select * from ViewRefPay where comfullname = '"+ComFullName+"' and needPayAmt > 0 " +
                        			 "and (ViewRefPay.BillType=case '"+paytypeID+"' when 'Pay' then 'tblBuyInStock' when 'PrePay' then 'tblBuyInStock' " +
                        			 "when 'RetrunPay' then 'tblPay' when 'ProcessFee' then 'tblPrecessCost' when 'TransportFee' then 'tblPrecessFare'" +
                        			 "when 'ProduceCheck' then 'tblSilkGlossInGoods' end  or ViewRefPay.BillType=case '"+paytypeID+"' " +
                        			 "when 'Pay' then 'tblSendInStock' when 'PrePay' then 'tblSendInStock' end) " +
                        			 "order by billDate";
                        ResultSet rst = s.executeQuery(sql);
                        ArrayList<ViewRefPay> vs = new ArrayList<ViewRefPay>();
                        while(rst.next()){
                        	ViewRefPay v = new ViewRefPay();
                        	v.setAlreadyPayAmt(rst.getDouble("alreadyPayAmt"));
                        	v.setBackAmount(rst.getDouble("backAmount"));
                        	v.setBillName(rst.getString("billName"));
                        	v.setBillNo(rst.getString("billNo"));
                        	v.setBillType(rst.getString("billType"));
                        	v.setBuyOrderId(rst.getString("buyOrderId"));
                        	v.setBuyOrderNo(rst.getString("buyOrderNo"));
                        	v.setComFullName(rst.getString("comFullName"));
                        	v.setCompanyCode(rst.getString("companyCode"));
                        	v.setDepartmentCode(rst.getString("departmentCode"));
                        	v.setDeptFullName(rst.getString("deptFullName"));
                        	v.setEmpFullName(rst.getString("empFullName"));
                        	v.setEmployeeId(rst.getString("employeeId"));
                        	v.setId(rst.getString("id"));
                        	v.setNeedPayAmt(rst.getDouble("needPayAmt"));
                        	v.setScompanyId(rst.getString("scompanyId"));
                        	v.setTotalTaxAmount(rst.getDouble("totalTaxAmount"));
                        	
                        	vs.add(v);
                        }
                        rs.setRetVal(vs);
                        
                    }
                });
                return rs.getRetCode();
            }
        });
		rs.setRetCode(retCode);           
		
		
		return rs;
	}
	
	public Result getRefReceive(final String CompanyCode,final String acceptTypeID,final String local){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        Statement s = conn.createStatement();
                        String sql = "select backAmount,"+local+" as billName,billNo,billType,comFullName,companycode,departmentCode"
                        +",deptfullname,empfullname,employeeId,ViewRefReceive.id,needReturnAmt,salesOrderId,salesOrderNo"
                        +",ViewRefReceive.scompanyId,totalAlrAccAmt,totalTaxAmount"
                        +" from ViewRefReceive  ,tblLanguage  where ViewRefReceive.billName=tblLanguage.id and CompanyCode = '"
        					+ CompanyCode
        					+ "' and NeedReturnAmt > 0 and ((ViewRefReceive.BillType=case '"
        					+ acceptTypeID + "'"
        					+ "when 'Receive' then 'tblSalesOutStock'"
        					+ "when 'PreReceive' then 'tblSalesOutStock'"
        					+ "when 'RetrunReceive' then 'tblSaleReceive' when 'Replace' then 'tblsubReceive' end ) or"
        					+ "(ViewRefReceive.BillType=case '" + acceptTypeID + "'"
        					+ "when 'Receive' then 'tblSalesRetailStock'"
        					+ "when 'PreReceive' then 'tblSalesOutStock'"
        					+ "when 'RetrunReceive' then 'tblSaleReceive' when 'Replace' then 'tblsubReceive' end ))"
        					+ " order by billdate";
                        
                        ResultSet rst = s.executeQuery(sql);
                        ArrayList<ViewRefReceive> vs = new ArrayList<ViewRefReceive>();
                        while(rst.next()){
                        	ViewRefReceive v = new ViewRefReceive();
                        	v.setBackAmount(rst.getDouble("backAmount"));
                        	v.setBillName(rst.getString("billName"));
                        	v.setBillNo(rst.getString("billNo"));
                        	v.setBillType(rst.getString("billType"));
                        	v.setComFullName(rst.getString("comFullName"));
                        	v.setCompanycode(rst.getString("companycode"));
                        	v.setDepartmentCode(rst.getString("departmentCode"));
                        	v.setDeptfullname(rst.getString("deptfullname"));
                        	v.setEmpfullname(rst.getString("empfullname"));
                        	v.setEmployeeId(rst.getString("employeeId"));
                        	v.setId(rst.getString("id"));
                        	v.setNeedReturnAmt(rst.getDouble("needReturnAmt"));
                        	v.setSalesOrderId(rst.getString("salesOrderId"));
                        	v.setSalesOrderNo(rst.getString("salesOrderNo"));
                        	v.setScompanyId(rst.getString("scompanyId"));
                        	v.setTotalAlrAccAmt(rst.getDouble("totalAlrAccAmt"));
                        	v.setTotalTaxAmount(rst.getDouble("totalTaxAmount"));
                        	vs.add(v);
                        }
                        rs.setRetVal(vs);
                        
                    }
                });
                return rs.getRetCode();
            }
        });
		rs.setRetCode(retCode);           
		
		
		return rs;
	}
	
	public Result getReceiveDet(final String BillNo){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        Statement s = conn.createStatement();
                        String sql = "select * from tblSaleReceiveDet where f_ref = (select id from tblSaleReceive where BillNo = '"+BillNo+"')";
                        ResultSet rst = s.executeQuery(sql);
                        ArrayList<SaleReceiveDet> vs = new ArrayList<SaleReceiveDet>();
                        while(rst.next()){
                        	SaleReceiveDet p = new SaleReceiveDet();
                        	
                        	p.setBackAmt(rst.getDouble("backAmt"));
                        	p.setBillAmt(rst.getDouble("billAmt"));
                        	p.setBillName(rst.getString("billName"));
                        	
                        	p.setClassCode(rst.getString("classCode"));
                        	p.setCurBackAmt(rst.getDouble("curBackAmt"));
                        	p.setCurBillAmt(rst.getDouble("curBillAmt"));
                        	p.setCurSettledAmt(rst.getDouble("curSettledAmt"));
                        	p.setCurWexeBalAmt(rst.getDouble("curWexeBalAmt"));
                        	
                        	p.setDetOrderNo(rst.getString("detOrderNo"));
                        	p.setExeBalAmt(rst.getDouble("exeBalAmt"));
                        	p.setExeBalFcAmt(rst.getDouble("exeBalFcAmt"));
                        	p.setRefbillID(rst.getString("RefbillID"));
                        	p.setF_ref(rst.getString("f_ref"));
                        	
                        	p.setId(rst.getString("id"));
                       
                        	p.setMoneyRate(rst.getString("moneyRate"));
                        	p.setMoneyID(rst.getString("moneyID"));
                        	p.setRefBillNo(rst.getString("refbillNo"));
                        	p.setRemark(rst.getString("remark"));
                        	p.setRowON(rst.getString("rowON"));
                        	p.setReceiveBillType(rst.getString("receiveBillType"));
                        	
                        	p.setSCompanyID(rst.getString("SCompanyID"));
                        	p.setSettledAmt(rst.getDouble("settledAmt"));
                        	p.setWexeBalAmt(rst.getDouble("wexeBalAmt"));
                        	p.setSaleOutBillID(rst.getString("saleOutBillID"));
                        	p.setSaleOutBillNo(rst.getString("saleOutBillNo"));
                        	p.setSalesOrderID(rst.getString("salesOrderID"));
                        	p.setSalesOrderNo(rst.getString("salesOrderNo"));
                        	p.setWorkFlowNode(rst.getString("workFlowNode"));
                        	p.setWorkFlowNodeName(rst.getString("workFlowNodeName"));
                        	
                        	
                        	vs.add(p);
                        }
                        rs.setRetVal(vs);
                        
                    }
                });
                return rs.getRetCode();
            }
        });
		rs.setRetCode(retCode);           
		
		
		return rs;
	}
	
	/**
	 * 查询供应商或客户的待结算金额
	 * @param BillNo
	 * @return
	 */
	public Result queryCompanyWexeBalAmt(final String companyCode,final String companyType){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                    	String sql = "select b.WexeBalAmt from tblCompanybeginning a left join tblCompanybeginningDet b on a.id=b.f_ref "
                    				+" where a.beginningType=? and  b.companyCode=? and b.WexeBalAmt>0" ;
                    	PreparedStatement pss = conn.prepareStatement(sql) ;
                    	pss.setString(1, companyType) ;
                    	pss.setString(2, companyCode) ;
                    	ResultSet rss = pss.executeQuery() ;
                    	if(rss.next()){
                    		rs.setRetVal(GlobalsTool.formatNumber(Double.valueOf(rss.getString("WexeBalAmt")), false, false, 
                        					GlobalsTool.getSysIntswitch(), "tblCompanybeginningDet", "WexeBalAmt", true)) ;
                    	}else{
                    		rs.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
                    	}
                    }
                });
                return rs.getRetCode();
            }
        });
		rs.setRetCode(retCode);           
		return rs;
	}
}
