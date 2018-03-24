package com.menyi.aio.bean;

public class PayDet {

	private String id;
	private String f_ref;
	private double BillAmt;
	private String Remark;
	private double CurBillAmt;
	private double ExeBalAmt;
	private double ExeBalFcAmt;
	private String MoneyRate;
	private double SettledAmt;
	private double WexeBalAmt;
	private double CurSettledAmt;
	private double CurWexeBalAmt;
	private String RefBillNo;
	private String RefInvoiceID;
	private String BuyOrderNo;
	private String BuyOrderID;
	private String RefbillID;
	private double BackAmt;
	private String SCompanyID;
	private double CurBackAmt;
	private String classCode;
	private String RowON;
	private String MoneyID;
	private String workFlowNodeName;
	private String workFlowNode;
	private String PayBillType;
	private String BillName ;
	private String detOrderNo;
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
	public String getBillName() {
		return BillName;
	}
	public void setBillName(String billName) {
		BillName = billName;
	}
	public String getBuyOrderID() {
		return BuyOrderID;
	}
	public void setBuyOrderID(String buyOrderID) {
		BuyOrderID = buyOrderID==null?"":buyOrderID;
	}
	public String getBuyOrderNo() {
		return BuyOrderNo;
	}
	public void setBuyOrderNo(String buyOrderNo) {
		BuyOrderNo = buyOrderNo==null?"":buyOrderNo;
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
	public String getDetOrderNo() {
		return detOrderNo;
	}
	public void setDetOrderNo(String detOrderNo) {
		this.detOrderNo = detOrderNo;
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
	public String getMoneyID() {
		return MoneyID;
	}
	public void setMoneyID(String moneyID) {
		MoneyID = moneyID;
	}
	public String getMoneyRate() {
		return MoneyRate;
	}
	public void setMoneyRate(String moneyRate) {
		MoneyRate = moneyRate;
	}
	public String getPayBillType() {
		return PayBillType;
	}
	public void setPayBillType(String payBillType) {
		PayBillType = payBillType;
	}
	public String getRefbillID() {
		return RefbillID;
	}
	public void setRefbillID(String refbillID) {
		RefbillID = refbillID;
	}
	public String getRefBillNo() {
		return RefBillNo;
	}
	public void setRefBillNo(String refBillNo) {
		RefBillNo = refBillNo;
	}
	public String getRefInvoiceID() {
		return RefInvoiceID;
	}
	public void setRefInvoiceID(String refInvoiceID) {
		RefInvoiceID = refInvoiceID;
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
	
}
