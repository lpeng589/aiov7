package com.menyi.aio.bean;

public class SaleReceiveDet {

	private String id;
	private String f_ref;
	private double BillAmt;
	private double CurBillAmt;
	private double ExeBalAmt;
	private double ExeBalFcAmt;
	private double SettledAmt;
	private double WexeBalAmt;
	private double CurSettledAmt;
	private double CurWexeBalAmt;
	private String MoneyRate;
	private String Remark;
	private String SalesOrderNo;
	private String SalesOrderID;
	private String RefBillNo;
	private String RefbillID;
	private double BackAmt;
	private String SCompanyID;
	private double CurBackAmt;
	private String classCode;
	private String RowON;
	private String MoneyID;
	private String SaleOutBillNo;
	private String SaleOutBillID;
	private String workFlowNodeName;
	private String workFlowNode;
	private String ReceiveBillType;
	private String BillName;
	private String detOrderNo;
	public String getBillName() {
		return BillName;
	}
	public void setBillName(String billName) {
		BillName = billName;
	}
	public String getDetOrderNo() {
		return detOrderNo;
	}
	public void setDetOrderNo(String detOrderNo) {
		this.detOrderNo = detOrderNo;
	}
	public String getMoneyID() {
		return MoneyID;
	}
	public void setMoneyID(String moneyID) {
		MoneyID = moneyID;
	}
	public String getReceiveBillType() {
		return ReceiveBillType;
	}
	public void setReceiveBillType(String receiveBillType) {
		ReceiveBillType = receiveBillType;
	}
	public String getSaleOutBillID() {
		return SaleOutBillID;
	}
	public void setSaleOutBillID(String saleOutBillID) {
		SaleOutBillID = saleOutBillID==null?"":saleOutBillID;
	}
	public String getSaleOutBillNo() {
		return SaleOutBillNo;
	}
	public void setSaleOutBillNo(String saleOutBillNo) {
		SaleOutBillNo = saleOutBillNo==null?"":saleOutBillNo;
	}
	public String getWorkFlowNode() {
		return workFlowNode;
	}
	public void setWorkFlowNode(String workFlowNode) {
		this.workFlowNode = workFlowNode;
	}
	public String getWorkFlowNodeName() {
		return workFlowNodeName;
	}
	public void setWorkFlowNodeName(String workFlowNodeName) {
		this.workFlowNodeName = workFlowNodeName;
	}
	public double getBackAmt() {
		return BackAmt;
	}
	public void setBackAmt(double backAmt) {
		BackAmt = backAmt;
	}
	public double getBillAmt() {
		return BillAmt;
	}
	public void setBillAmt(double billAmt) {
		BillAmt = billAmt;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public double getCurBackAmt() {
		return CurBackAmt;
	}
	public void setCurBackAmt(double curBackAmt) {
		CurBackAmt = curBackAmt;
	}
	public double getCurBillAmt() {
		return CurBillAmt;
	}
	public void setCurBillAmt(double curBillAmt) {
		CurBillAmt = curBillAmt;
	}
	public double getCurSettledAmt() {
		return CurSettledAmt;
	}
	public void setCurSettledAmt(double curSettledAmt) {
		CurSettledAmt = curSettledAmt;
	}
	public double getCurWexeBalAmt() {
		return CurWexeBalAmt;
	}
	public void setCurWexeBalAmt(double curWexeBalAmt) {
		CurWexeBalAmt = curWexeBalAmt;
	}
	
	public String getRefbillID() {
		return RefbillID;
	}
	public void setRefbillID(String refbillID) {
		RefbillID = refbillID;
	}
	public double getExeBalAmt() {
		return ExeBalAmt;
	}
	public void setExeBalAmt(double exeBalAmt) {
		ExeBalAmt = exeBalAmt;
	}
	public double getExeBalFcAmt() {
		return ExeBalFcAmt;
	}
	public void setExeBalFcAmt(double exeBalFcAmt) {
		ExeBalFcAmt = exeBalFcAmt;
	}
	public String getF_ref() {
		return f_ref;
	}
	public void setF_ref(String f_ref) {
		this.f_ref = f_ref;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMoneyRate() {
		return MoneyRate;
	}
	public void setMoneyRate(String moneyRate) {
		MoneyRate = moneyRate;
	}
	public String getRefBillNo() {
		return RefBillNo;
	}
	public void setRefBillNo(String refbillNo) {
		RefBillNo = refbillNo;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getRowON() {
		return RowON;
	}
	public void setRowON(String rowON) {
		RowON = rowON;
	}
	public String getSalesOrderID() {
		return SalesOrderID;
	}
	public void setSalesOrderID(String salesOrderID) {
		SalesOrderID = salesOrderID==null?"":salesOrderID;
	}
	public String getSalesOrderNo() {
		return SalesOrderNo;
	}
	public void setSalesOrderNo(String salesOrderNo) {
		SalesOrderNo = salesOrderNo==null?"":salesOrderNo;
	}
	public String getSCompanyID() {
		return SCompanyID;
	}
	public void setSCompanyID(String companyID) {
		SCompanyID = companyID;
	}
	public double getSettledAmt() {
		return SettledAmt;
	}
	public void setSettledAmt(double settledAmt) {
		SettledAmt = settledAmt;
	}
	public double getWexeBalAmt() {
		return WexeBalAmt;
	}
	public void setWexeBalAmt(double wexeBalAmt) {
		WexeBalAmt = wexeBalAmt;
	}
}
