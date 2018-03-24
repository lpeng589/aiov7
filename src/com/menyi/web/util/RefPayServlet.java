package com.menyi.web.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.menyi.aio.bean.PayDet;
import com.menyi.aio.bean.SaleReceiveDet;
import com.menyi.aio.bean.ViewRefPay;
import com.menyi.aio.bean.ViewRefReceive;

/**
 * �Զ�����Servlet
 * �����ɹ�����������տ���Զ�����
 * @author ����
 *
 */
public class RefPayServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public RefPayServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String expectStart = GlobalsTool.getMessage(request, "web.util.lb.expectStart");
		String type =  request.getParameter("type");//�Զ���������
		StringBuffer returnString = new StringBuffer("");
		if("pay".equals(type)){//�ɹ����
			//��Ӧ������
			String viewCompanyTotal_ComFullName = request.getParameter("ViewCompanyTotal_ComFullName");
			String companyCode = request.getParameter("companyCode") ;
			viewCompanyTotal_ComFullName = new String(viewCompanyTotal_ComFullName.getBytes("ISO-8859-1"), "GBK");
			String paytypeID = request.getParameter("PaytypeID");
			String money = request.getParameter("money");
			double balance = Double.parseDouble(money);//���
			
			String billNo = request.getParameter("billNo");//�ɹ����ID
			RefPayMgt fq = new RefPayMgt();
			/*��ѯ��Ӧ��Ӧ�̵� ��������*/
			Result result = fq.queryCompanyWexeBalAmt(companyCode, "1") ;
			if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
				double balAmt = Double.parseDouble(result.retVal.toString()) ;
				if(balance>balAmt){
					balance = getDoubleSubtarct(balance,balAmt) ;
					returnString.append(",,,,"+balAmt+",,,,,,"+balAmt+","+expectStart);
				}else{
					returnString.append(",,,,"+balAmt+",,,,,,"+balance+","+expectStart);
					balance = 0 ;
				}
			}
			ArrayList<String> ps = new ArrayList<String>();//�޸�ʱ������ĵ���
			if(billNo!=null&&!"".equals(billNo)){//�޸Ĳɹ����
				Result rs = fq.getPayDet(billNo);
				ArrayList<PayDet> vs = (ArrayList<PayDet>) rs.getRetVal();
				StringBuffer allBillType = new StringBuffer("");//���еĵ�������
				for (PayDet v : vs) {
					if(!"".equals(allBillType.toString())){
						allBillType.append(",");
					}
					allBillType.append("'"+v.getPayBillType()+"'");
				}
				Locale locale = (Locale) request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY);
				//��������
				HashMap<String, String> allBillName =(HashMap<String, String>) fq.getBillName(locale.toString(), allBillType.toString()).getRetVal();
				for (PayDet v : vs) {
					
					v.setBillName(allBillName.get(v.getPayBillType()));
					if (balance > v.getWexeBalAmt()) {
						if(returnString.toString().length() != 0){
							returnString.append("|");
						}
						
						returnString.append(
								v.getRefBillNo() + "," + v.getBillAmt() + ","
								+ v.getSettledAmt() + "," + v.getBackAmt()+","+
								v.getWexeBalAmt()+","+v.getRefbillID()+","+v.getBuyOrderNo()+","+
								v.getPayBillType()+","+v.getBillName()+","+v.getBuyOrderID()+","+
								v.getWexeBalAmt()+",");
						
						balance = getDoubleSubtarct(balance, v.getWexeBalAmt());
						ps.add(v.getRefBillNo());
					} else if (balance <= v.getWexeBalAmt() && balance > 0) {
						if(returnString.toString().length() != 0){
							returnString.append("|");
						}
						returnString.append(
								v.getRefBillNo() + "," + v.getBillAmt() + ","
								+ v.getSettledAmt() + "," + v.getBackAmt() +","+
								v.getWexeBalAmt()+","+v.getRefbillID()+","+v.getBuyOrderNo()+","+
								v.getPayBillType()+","+v.getBillName()+","+v.getBuyOrderID()+","+
								balance+",");
						
						balance = 0;
						break;
					} else {
						break;
					}
				}
				
			}
			
			Result rs = fq.getRefPay(viewCompanyTotal_ComFullName,paytypeID);//������ص���
			ArrayList<ViewRefPay> vs = (ArrayList<ViewRefPay>) rs.getRetVal();
			StringBuffer allBillType = new StringBuffer("");//���еĵ�������
			for (ViewRefPay v : vs) {
				if(!"".equals(allBillType.toString())){
					allBillType.append(",");
				}
				allBillType.append("'"+v.getBillType()+"'");
			}
			Locale locale = (Locale) request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY);
			HashMap<String, String> allBillName =(HashMap<String, String>) fq.getBillName(locale.toString(), allBillType.toString()).getRetVal();
			for (ViewRefPay v : vs) {
				boolean same = false;
				for(String oneBillNo:ps){
					if(v.getBillNo().equals(oneBillNo)){
						same = true;//���ŵ����޸�ʱ��������
						break;
					}
				}
				if(same){
					continue;
				}
				
				v.setBillName(allBillName.get(v.getBillType()));
				if (balance > v.getNeedPayAmt()) {
					if(returnString.toString().length() != 0){
						returnString.append("|");
					}
					
					returnString.append(
							v.getBillNo() + "," + v.getTotalTaxAmount() + ","
							+ v.getAlreadyPayAmt() + "," + v.getBackAmount()+","+
							v.getNeedPayAmt()+","+v.getId()+","+v.getBuyOrderNo()+","+
							v.getBillType()+","+v.getBillName()+","+v.getBuyOrderId()+","+
							v.getNeedPayAmt()+",");
					
					balance = getDoubleSubtarct(balance, v.getNeedPayAmt());
				} else if (balance <= v.getNeedPayAmt() && balance > 0) {
					if(returnString.toString().length() != 0){
						returnString.append("|");
					}
					returnString.append(
							v.getBillNo() + "," + v.getTotalTaxAmount() + ","
							+ v.getAlreadyPayAmt() + "," + v.getBackAmount()+","+
							v.getNeedPayAmt()+","+v.getId()+","+v.getBuyOrderNo()+","+
							v.getBillType()+","+v.getBillName()+","+v.getBuyOrderId()+","+
							balance+",");
					
					balance = 0;
					break;
				} else {
					break;
				}
			}
		} else {//���۳���
			String companyCode = request
					.getParameter("CompanyCode");//�ͻ�����;
			String money = request.getParameter("money");
			double balance = Double.parseDouble(money);// ���
			String acceptTypeID = request.getParameter("AcceptTypeID");

			if (companyCode == null || "".equals(companyCode)) {
				return;
			}
			String billNo = request.getParameter("billNo");//�����տID
			RefPayMgt fq = new RefPayMgt();
			/*��ѯ��Ӧ�ͻ��� ��������*/
			Result result = fq.queryCompanyWexeBalAmt(companyCode, "2") ;
			if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
				double balAmt = Double.parseDouble(result.retVal.toString()) ;
				if(balance>balAmt){
					balance = getDoubleSubtarct(balance,balAmt) ;
					returnString.append(",,,,"+balAmt+",,,,,,"+balAmt+","+expectStart);
				}else{
					returnString.append(",,,,"+balAmt+",,,,,,"+balance+","+expectStart);
					balance = 0 ;
				}
			}
			ArrayList<String> ps = new ArrayList<String>();
			if (billNo != null && !"".equals(billNo)) {
				Result rs = fq.getReceiveDet(billNo);
				ArrayList<SaleReceiveDet> vs = (ArrayList<SaleReceiveDet>) rs
						.getRetVal();
				StringBuffer allBillType = new StringBuffer("");//���еĵ�������
				for (SaleReceiveDet v : vs) {
					if(!"".equals(allBillType.toString())){
						allBillType.append(",");
					}
					allBillType.append("'"+v.getReceiveBillType()+"'");
				}
				Locale locale = (Locale) request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY);
				//��������
				//HashMap<String, String> allBillName =(HashMap<String, String>) fq.getBillName(locale.toString(), allBillType.toString()).getRetVal();
				for (SaleReceiveDet v : vs) {
					
					//v.setBillName(allBillName.get(v.getReceiveBillType()));
					if (balance > v.getWexeBalAmt()) {
						if (returnString.toString().length() != 0) {
							returnString.append("|");
						}

						returnString.append(v.getRefBillNo() + ","
								+ v.getBillAmt() + "," + v.getSettledAmt()
								+ "," + v.getBackAmt() + ","
								+ v.getWexeBalAmt() + "," + v.getRefbillID()
								+ "," + v.getSalesOrderNo() + ","
								+ v.getReceiveBillType() + ","
								+ v.getBillName() + "," + v.getSalesOrderID()
								+ "," + v.getWexeBalAmt()+",");

						balance = getDoubleSubtarct(balance, v.getWexeBalAmt());
						ps.add(v.getRefBillNo());
					} else if (balance <= v.getWexeBalAmt() && balance > 0) {
						if (returnString.toString().length() != 0) {
							returnString.append("|");
						}
						returnString.append(v.getRefBillNo() + ","
								+ v.getBillAmt() + "," + v.getSettledAmt()
								+ "," + v.getBackAmt() + ","
								+ v.getWexeBalAmt() + "," + v.getRefbillID()
								+ "," + v.getSalesOrderNo() + ","
								+ v.getReceiveBillType() + ","
								+ v.getBillName() + "," + v.getSalesOrderID()
								+ "," + balance+",");

						balance = 0;
						break;
					} else {
						break;
					}
				}

			}

			
			RefPayMgt sqm = new RefPayMgt();
			Result rs = sqm.getRefReceive(companyCode,acceptTypeID,GlobalsTool.getLocale(request).toString());
			ArrayList<ViewRefReceive> vs = (ArrayList<ViewRefReceive>) rs
					.getRetVal();//���е��޸ĵ���

			StringBuffer allBillType = new StringBuffer("");//���еĵ�������
			for (ViewRefReceive v : vs) {
				if(!"".equals(allBillType.toString())){
					allBillType.append(",");
				}
				allBillType.append("'"+v.getBillType()+"'");
			}
			
			for (ViewRefReceive v : vs) {
				boolean same = false;
				for (String oneBillNo : ps) {
					if (v.getBillNo().equals(oneBillNo)) {
						same = true;//���ŵ����޸�ʱ��������
						break;
					}
				}
				if (same) {
					continue;
				}
			
				if (balance > v.getNeedReturnAmt()) {
					if (returnString.toString().length() != 0) {
						returnString.append("|");
					}
					returnString.append(v.getBillNo() + ","
							+ v.getTotalTaxAmount() + ","
							+ v.getTotalAlrAccAmt() + "," + v.getBackAmount()
							+ "," + v.getNeedReturnAmt() + "," + v.getId()
							+ "," + v.getSalesOrderNo() + "," + v.getBillType()
							+ "," + v.getBillName() + "," + v.getSalesOrderId()
							+ "," + v.getNeedReturnAmt()+",");

					balance = getDoubleSubtarct(balance, v.getNeedReturnAmt());
				} else if (balance <= v.getNeedReturnAmt() && balance > 0) {
					if (returnString.toString().length() != 0) {
						returnString.append("|");
					}
					returnString.append(v.getBillNo() + ","
							+ v.getTotalTaxAmount() + ","
							+ v.getTotalAlrAccAmt() + "," + v.getBackAmount()
							+ "," + v.getNeedReturnAmt() + "," + v.getId()
							+ "," + v.getSalesOrderNo() + "," + v.getBillType()
							+ "," + v.getBillName() + "," + v.getSalesOrderId()
							+ "," + balance+",");

					balance = 0;
					break;
				} else {
					break;
				}
			}
		}
		String retVar = returnString.toString();

		response.setContentType("text/html; charset=GBK");
		PrintWriter out = response.getWriter();
		out.print(retVar);
		out.flush();
		out.close();
		
		
			
	}

	public void init() throws ServletException {
		// Put your code here
	}
	
	public Double getDoubleSubtarct(Double a,Double b){
		BigDecimal ba = new BigDecimal(a.toString());
		BigDecimal bb = new BigDecimal(b.toString());
		
		return ba.subtract(bb).doubleValue();
	}

}
