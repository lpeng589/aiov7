package com.menyi.web.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dbfactory.Result;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.ReatailAccountBean;
import com.menyi.aio.bean.ReatailBean;
import com.menyi.aio.bean.ReatailDetBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.billNumber.BillNo;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;
/**
 * 解析客户端上传的零售单xml文件
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Dec 4, 2010
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class ParseReatail {

	/**
	 * 解析客户端上传的零售单xml文件
	 * @param document
	 * @return
	 */
	public static List<ReatailBean> parse(Document document){
		Element root = document.getDocumentElement();
		int nodeLength = root.getChildNodes().getLength() ;
		ArrayList<ReatailBean> listReatail = new ArrayList<ReatailBean>() ;
		for(int i = 0; i < nodeLength; i++){
			Node node = root.getChildNodes().item(i);
			if(null !=node.getAttributes() && null != node.getAttributes().getNamedItem("id")){
				ReatailBean bean = new ReatailBean() ;
				bean.setId(node.getAttributes().getNamedItem("id").getNodeValue()) ;
				bean.setCardNO(node.getAttributes().getNamedItem("cardNO").getNodeValue()) ;
				bean.setCardType(node.getAttributes().getNamedItem("Cardtype").getNodeValue()) ;
				bean.setMoney(node.getAttributes().getNamedItem("money").getNodeValue()) ;
				bean.setDiscountMoney(node.getAttributes().getNamedItem("discountMoney").getNodeValue()) ;
				bean.setAccountMoney(node.getAttributes().getNamedItem("AccountMoney").getNodeValue()) ;
				bean.setConsumeIntegral(node.getAttributes().getNamedItem("ConsumeIntegral").getNodeValue()) ;
				bean.setDiscountAmount(node.getAttributes().getNamedItem("preferentialMoney").getNodeValue()) ;
				bean.setPlayMoney(node.getAttributes().getNamedItem("playMoney").getNodeValue()) ;
				bean.setOutMoney(node.getAttributes().getNamedItem("outMoney").getNodeValue()) ;
				bean.setEmployeeId(node.getAttributes().getNamedItem("employeeId").getNodeValue()) ;
				bean.setShopId(node.getAttributes().getNamedItem("shopId").getNodeValue()) ;
				bean.setCreateDate(node.getAttributes().getNamedItem("createDate").getNodeValue()) ;
				bean.setSendFlag(node.getAttributes().getNamedItem("sendFlag").getNodeValue()) ;
				bean.setTax(node.getAttributes().getNamedItem("tax").getNodeValue()) ;
				bean.setTaxMoney(node.getAttributes().getNamedItem("taxMoney").getNodeValue()) ;
				bean.setSalesperson(node.getAttributes().getNamedItem("salesperson").getNodeValue()) ;
				bean.setCompanyCode(node.getAttributes().getNamedItem("companyCode").getNodeValue()) ;
				bean.setStockCode(node.getAttributes().getNamedItem("stockCode").getNodeValue()) ;
				bean.setTerminal(node.getAttributes().getNamedItem("terminal").getNodeValue()); 
				bean.setRemark(node.getAttributes().getNamedItem("remark").getNodeValue());
		        NodeList nodeList = node.getChildNodes();
		        if (null != nodeList && nodeList.getLength() > 0) {
		            int lenght = nodeList.getLength();
		            ArrayList<ReatailDetBean> detList = new ArrayList<ReatailDetBean>() ;
		            ArrayList<ReatailAccountBean> accountList = new ArrayList<ReatailAccountBean>() ;
		            for (int j = 0; j < lenght; j++) {
		                Node cnode = nodeList.item(j);
		                if(null != cnode && "det".equals(cnode.getNodeName())){
			                ReatailDetBean detBean = new ReatailDetBean() ;
			                detBean.setId(cnode.getAttributes().getNamedItem("id").getNodeValue()) ;
			                detBean.setOrderId(cnode.getAttributes().getNamedItem("orderId").getNodeValue()) ;
			                detBean.setGoodsCode(cnode.getAttributes().getNamedItem("goodsCode").getNodeValue()) ;
			                detBean.setQty(cnode.getAttributes().getNamedItem("qty").getNodeValue()) ;
			                detBean.setPrice(cnode.getAttributes().getNamedItem("price").getNodeValue()) ;
			                detBean.setMoney(cnode.getAttributes().getNamedItem("money").getNodeValue()) ;
			                detBean.setDiscount(cnode.getAttributes().getNamedItem("discount").getNodeValue()) ;
			                detBean.setDiscountPrice(cnode.getAttributes().getNamedItem("discountPrice").getNodeValue()) ;
			                detBean.setDiscountMoney(cnode.getAttributes().getNamedItem("discountMoney").getNodeValue()) ;
			                detBean.setTaxPrice(cnode.getAttributes().getNamedItem("taxPrice").getNodeValue()) ;
			                detBean.setTaxMoney(cnode.getAttributes().getNamedItem("taxMoney").getNodeValue()) ;
			                detBean.setCreateDate(cnode.getAttributes().getNamedItem("createDate").getNodeValue()) ;
			                detBean.setBatchNo(cnode.getAttributes().getNamedItem("batchNo").getNodeValue()) ;
			                detBean.setYearNO(cnode.getAttributes().getNamedItem("yearNOValue").getNodeValue()) ;
			                detBean.setColor(cnode.getAttributes().getNamedItem("color").getNodeValue()) ;
			                detBean.setSeq(cnode.getAttributes().getNamedItem("seq").getNodeValue()) ;
			                detBean.setInch(cnode.getAttributes().getNamedItem("inch").getNodeValue()) ;
			                detBean.setHue(cnode.getAttributes().getNamedItem("hueValue").getNodeValue()) ;
			                detBean.setAvailably(cnode.getAttributes().getNamedItem("Availably").getNodeValue()) ;
			                detBean.setProDate(cnode.getAttributes().getNamedItem("proDate").getNodeValue()) ;
			                detList.add(detBean) ;
		                }
		                if(null != cnode && "accounts".equals(cnode.getNodeName())){
		                	NodeList accounts=cnode.getChildNodes();
		                	for(int m=0;m<accounts.getLength();m++){
		                        Node anode=accounts.item(m);
		                        if(null !=anode.getAttributes() && null != anode.getAttributes().getNamedItem("id")){
				                	ReatailAccountBean account = new ReatailAccountBean() ;
				                	account.setId(anode.getAttributes().getNamedItem("id").getNodeValue()) ;
				                	account.setSubjectCode(anode.getAttributes().getNamedItem("subjectCode").getNodeValue()) ;
				                	account.setMoney(anode.getAttributes().getNamedItem("money").getNodeValue()) ;
				                	accountList.add(account) ;
		                        }
		                	}
		                }
		            }
		            bean.setListDet(detList) ;
		            bean.setAccountList(accountList) ;
		        }
		        listReatail.add(bean) ;
			}
		}
        return listReatail ;
	}
	
	/**
	 * 把零售单转换成销售出库对应的数据
	 * @param listBean
	 * @return
	 */
	public static HashMap convertSalesOutStockData(ReatailBean bean,Hashtable allTables,
			String[] shop,LoginBean login){
		HashMap values = new HashMap() ;
		DBFieldInfoBean fieldBean = DDLOperation.getFieldInfo(allTables, "tblSalesOutStock", "BillNo") ;
		values.put("BillNo", BillNoManager.find("tblSalesOutStock_BillNo")) ;
		values.put("BillDate", bean.getCreateDate().substring(0, 10)) ;
		values.put("CompanyCode", bean.getCompanyCode()) ;	/*门店默认往来单位*/
		values.put("EmployeeID", bean.getSalesperson()) ;
		OnlineUser user = OnlineUserInfo.getUser(bean.getSalesperson()) ;
		if(user!=null){
			values.put("DepartmentCode", user.getDeptId()) ;
		}else{
			Result result = new UserMgt().queryEmp(bean.getSalesperson()) ;
			if(result.retCode==ErrorCanst.DEFAULT_SUCCESS && result.retVal!=null){
				EmployeeBean emp = (EmployeeBean) ((ArrayList)result.getRetVal()).get(0) ;
				values.put("DepartmentCode", emp!=null?emp.getDepartmentCode():"") ;
			}
		}
		values.put("StockCode", bean.getStockCode()) ;		/*门店默认仓库*/
		values.put("AcceptDate", bean.getCreateDate().substring(0, 10)) ;
		values.put("CardNO", bean.getCardNO()) ;
		values.put("AlrAccAmt", bean.getTaxMoney()) ;
		values.put("AccAmt", bean.getTaxMoney()) ;
		values.put("FactIncome", bean.getTaxMoney()) ;
		values.put("DiscountAmount", bean.getDiscountAmount()) ;
		values.put("InVoiceType", "1") ;
		values.put("Terminal", bean.getTerminal()) ;
		double totalTaxAmount = GlobalsTool.round(Double.parseDouble(bean.getTaxMoney()), GlobalsTool.getDigitsOrSysSetting("tblSalesOutStock","TotalTaxAmount")) ;
		values.put("TotalTaxAmount", totalTaxAmount) ;
		values.put("shopId", bean.getShopId()) ;
		values.put("billType", "reatail") ;		
		values.put("reatailId", bean.getId()) ;
		values.put("SCompanyID", login.getSunCmpClassCode()) ;
		values.put("Remark", bean.getRemark());
		ArrayList childList = new ArrayList();
		values.put("TABLENAME_tblSalesOutStockDet", childList);
		for(ReatailDetBean detBean : bean.getListDet()){
			HashMap hm = new HashMap() ;
			hm.put("GoodsCode", detBean.getGoodsCode()) ;
			hm.put("Qty", detBean.getQty()) ;
			hm.put("Price", detBean.getPrice()) ;	
			double disAmount = GlobalsTool.round(Double.parseDouble(detBean.getDiscountMoney()), GlobalsTool.getDigitsOrSysSetting("tblSalesOutStockDet","DisAmount")) ;
			double amount = GlobalsTool.round(Double.parseDouble(detBean.getMoney()), GlobalsTool.getDigitsOrSysSetting("tblSalesOutStockDet","Amount")) ;
			hm.put("Amount", detBean.getMoney()) ;	
			hm.put("Tax", bean.getTax()) ;
			hm.put("TaxPrice", detBean.getTaxPrice()) ;
			hm.put("TaxAmount", detBean.getTaxMoney()) ;
			hm.put("Discount", detBean.getDiscount()) ;
			hm.put("DisPrice", detBean.getDiscountPrice()) ;
			hm.put("DisAmount", amount-disAmount) ;
			hm.put("DisBackAmt", detBean.getDiscountMoney()) ;
			hm.put("Seq", detBean.getSeq().length()>0?detBean.getSeq()+"~":"") ;
			hm.put("BatchNo", detBean.getBatchNo()) ;
			hm.put("Inch", detBean.getInch()) ;
			hm.put("color", detBean.getColor()) ;
			hm.put("yearNO", detBean.getYearNO()) ;
			hm.put("Hue", detBean.getHue()) ;
			hm.put("Availably", detBean.getAvailably()) ;
			hm.put("ProDate", detBean.getProDate()) ;
			if("true".equals(GlobalsTool.getSysSetting("ManyStockSales"))){
				hm.put("StockCode", bean.getStockCode()) ;
			}
			childList.add(hm) ;
		}
		ArrayList accountList = new ArrayList() ;
		values.put("TABLENAME_tblSalesRecAccount", accountList) ;
		for(ReatailAccountBean account : bean.getAccountList()){
			HashMap hm = new HashMap() ;
			DBFieldInfoBean fieldBean2 = DDLOperation.getFieldInfo(allTables, "tblSalesRecAccount", "SettleType") ;
			hm.put("SettleType", fieldBean2.getDefaultValue()) ;
			hm.put("Account", account.getSubjectCode()) ;
			hm.put("Amount", account.getMoney()) ;
			accountList.add(hm) ;
		}
		return values ;
	}
	
	/**
	 * 测试
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		File file = new File("C:\\demo.xml") ;
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file) ;
		List<ReatailBean> listReatail = ParseReatail.parse(document) ;
		System.out.println(listReatail.size());
	}
}
