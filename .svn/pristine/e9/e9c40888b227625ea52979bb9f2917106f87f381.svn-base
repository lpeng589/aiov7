package yqzl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletContext;

import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;

import yqzl.Jzb;
import yqzl.bean.NtdmtBean;

/**
 * 银企直连
 * @author dzp
 *
 */
public class Yqzl {
	private ServletContext context;
	private String host;
	private String lgnnam;
	public Yqzl(ServletContext context,String host,String lgnnam){
		this.context=context;
		this.host=host;
		this.lgnnam=lgnnam;
	}
	/**
	 * 银企直联处理
	 * @param conn  数据库连接
	 * @param host  前置机地址
	 * @param lgnnam  登录名
	 * @param accnbr  使用的账号
	 */
	public void deal(String accnbr){
		//获得今天天交易记录
		Jzb jzb = new Jzb(host, lgnnam);
		List<NtdmtBean> listNow=jzb.getNow(accnbr);
		dealRecord(listNow);
		//获得昨天交易记录
		SimpleDateFormat df= new SimpleDateFormat("yyyyMMdd");
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, -1);
		String begdat=df.format(now.getTime());
		now.add(Calendar.DATE, -1);
		String enddat=df.format(now.getTime());
		List<NtdmtBean> listHistory=jzb.getHistory(accnbr, begdat, enddat);
		dealRecord(listHistory);
	}
	/**
	 * 银企直联交易记录处理
	 * @param list
	 */
	private void dealRecord(List<NtdmtBean> list){
		//过滤掉已经处理过的
		List<NtdmtBean> listNew=new ArrayList<NtdmtBean>();
		if(list!=null){
			System.out.println("银企直联记录数（未过滤）："+list.size()+" --"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			for (NtdmtBean ntdmtBean : list) {
				if(getRecordCount(ntdmtBean.getTrxnbr())==0){
					listNew.add(ntdmtBean);
				}
			}
		}
		System.out.println("银企直联记录数（已过滤）："+list.size()+" --"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		//处理新交易
		for (NtdmtBean ntdmtBean : listNew) {
			try {
				addReceive(ntdmtBean);
			} catch (Exception e) {
				System.out.println("添加收款单出错："+ntdmtBean.getDmanbr());
				e.printStackTrace();
			}
		}
	}
	/**
	 * 添加收款记录
	 * @param conn
	 * @param bean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addReceive(NtdmtBean bean){
		System.out.println("开始添加收款记录(未过滤)："+bean.getDmanbr());
		//过滤无效单据
		if(bean.getTrxamt().startsWith("-")||!bean.getTrxdir().equals("C")){
			System.out.println("开始添加收款记录(未过滤)："+bean.getDmanbr()+"无效单据");
			return;
		}
		System.out.println("开始添加收款记录(未过滤)："+bean.getDmanbr()+"根据编号寻找店");
		//根据编号找到店
		HashMap<String, String> company=getComapany(bean.getDmanbr());
		if(company==null){
			System.out.println("开始添加收款记录(未过滤)："+bean.getDmanbr()+"找不到店");
			return;
		}
		System.out.println("开始添加收款记录："+bean.getDmanbr());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dfShort = new SimpleDateFormat("yyyy-MM-dd");
		Date now =new Date();
		HashMap saveValues = new HashMap();
		saveValues.put("id", IDGenerater.getId());
		saveValues.put("BillDate", dfShort.format(now));
		saveValues.put("ExeBalAmt", bean.getTrxamt());
		saveValues.put("AcceptTypeId", "Receive");
		saveValues.put("AccDetStatus", "0");
		saveValues.put("CreateBy", "1");
		saveValues.put("lastUpdateBy", "1");
		saveValues.put("createTime", df.format(now));
		saveValues.put("lastUpdateTime", df.format(now));
		saveValues.put("statusId", "0");
		saveValues.put("Remark", bean.getTrxtxt());
		saveValues.put("SettleType", "3");
		saveValues.put("SCompanyID", "00001");
		saveValues.put("workFlowName", "finish");
		saveValues.put("WorkFlowNode", "-1");
		saveValues.put("currentlyreceive", "0.00000000");
		saveValues.put("FCcurrentlyreceive", "0.00000000");
		saveValues.put("ReturnAmt", "0.00000000");
		saveValues.put("EmployeeID", "1");
		saveValues.put("BillFcAmt", "0.00000000");
		saveValues.put("ContractAmt", "0.00000000");
		saveValues.put("prntCount", "0");
		saveValues.put("CurrencyRate", "1.00000000");
		saveValues.put("finishTime", df.format(now));
		saveValues.put("FactIncome", bean.getTrxamt());
		saveValues.put("AccAmt", bean.getTrxamt());
		saveValues.put("DepartmentCode", "00101");
		saveValues.put("CompanyCode", company.get("classCode"));
		
		saveValues.put("CashUserID", "0");
		//saveValues.put("PeriodYear", "2015");
		//saveValues.put("PeriodMonth", "4");
		//saveValues.put("Period", "4");	
		//saveValues.put("PeriodMonth", "4");
		saveValues.put("AutoBillMarker", "0");
		//saveValues.put("CertificateNo", "04d8da956_1504291429536500010");
		
	
		
	
		
		
		
		//明细
		ArrayList<HashMap> details= new ArrayList<HashMap>();
		HashMap detValues= new HashMap();
		detValues.put("id", IDGenerater.getId());
		detValues.put("f_ref", saveValues.get("id"));
		detValues.put("SettleType", "1");
		detValues.put("Account", "100205");
		detValues.put("Amount", bean.getTrxamt());
		detValues.put("SCompanyID", "00001");
		detValues.put("Remark", "银企直连");
		detValues.put("ExeBalFcAmt", "0.00000000");
		details.add(detValues);
		saveValues.put("TABLENAME_tblReceiveAccountDet", details);
		Hashtable props =BaseEnv.propMap;
		Object ob = context.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		MessageResources resources = null;
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		System.out.println("设置收款记录值："+bean.getDmanbr());
		//保存
		if(saveToDb(GlobalsTool.getTableInfoBean("tblSaleReceive"),saveValues, resources,props)){
			System.out.println("添加收款单成功："+bean.getDmanbr());
			//添加处理完的交易到历史记录
			addRecord(bean);
			System.out.println("添加历史交易记录成功："+bean.getDmanbr());
		}
		
	}
	/**
	 * 添加处理记录
	 * @param bean
	 * @return
	 */
	private Result addRecord(final NtdmtBean bean){
		final Result rs = new Result(); 
		DBUtil.execute(new IfDB() {
		     public int exec(Session session) {
		         session.doWork(new Work() {
		             public void execute(Connection conn) {
		          	   try{
				          	String sql = "insert into TblYqzlRecord(trxnbr,dmanbr,dmanam,ccynbr,trxamt,trxdir,trxtim,rpyacc,rpynam,trxtxt,narinn,createtime) values(?,?,?,?,?,?,?,?,?,?,?,?)";
				         	PreparedStatement pss = conn.prepareStatement(sql) ;
				         	pss.setObject(1, bean.getTrxnbr()) ;
				         	pss.setObject(2, bean.getDmanbr()) ;
				            pss.setObject(3, bean.getDmanam()) ;
				            pss.setObject(4, bean.getCcynbr()) ;
				            pss.setObject(5, bean.getTrxamt()) ;
				            pss.setObject(6, bean.getTrxdir()) ;
				            pss.setObject(7, bean.getTrxtim()) ;
				            pss.setObject(8, bean.getRpyacc()) ;
				            pss.setObject(9, bean.getRpynam()) ;
				            pss.setObject(10,bean.getTrxtxt()) ;
				            pss.setObject(11,bean.getNarinn()) ;
				            pss.setObject(12,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())) ;
				            pss.execute();
		          	   }catch(Exception ex){
		          		   ex.printStackTrace();
		          		   rs.retCode = ErrorCanst.DEFAULT_FAILURE ;
		          		   BaseEnv.log.error("Yqzl addRecord : ",ex);
		          	   }
		             }
		         });
		         return rs.getRetCode();
		     }
	       });
	       return rs;
	}
	/**
	 * 查询数量
	 * @param trxnbr
	 * @return
	 */
	private int getRecordCount(final String trxnbr){
		final Result rs = new Result(); 
		rs.setRealTotal(-1);
		DBUtil.execute(new IfDB() {
		     public int exec(Session session) {
		         session.doWork(new Work() {
		             public void execute(Connection conn) {
		          	   try{
		          		 String sql = "select count(*) from TblYqzlRecord where trxnbr=?" ;
		     		    PreparedStatement pss = conn.prepareStatement(sql) ;
		     		    pss.setString(1, trxnbr) ;
		     		    ResultSet rss = pss.executeQuery() ;
		     		    if(rss.next()){
		     		    	rs.setRealTotal(rss.getInt(1));
		     		    }     
		          	   }catch(Exception ex){
		          		   ex.printStackTrace();
		          		   rs.retCode = ErrorCanst.DEFAULT_FAILURE ;
		          		 BaseEnv.log.error("Yqzl getRecordCount : ",ex);
		          	   }
		             }
		         });
		         return rs.getRetCode();
		     }
	       });
	       return rs.getRealTotal();
	}
	/**
	 * 查询门店
	 * @param trxnbr
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private HashMap getComapany(final String yqzlNo){
		final HashMap map = new HashMap();
		final Result rs = new Result(); 
		DBUtil.execute(new IfDB() {
		     public int exec(Session session) {
		         session.doWork(new Work() {
		             @SuppressWarnings("unchecked")
					public void execute(Connection conn) {
		          	   try{
		          		 String sql = "select a.classCode"
		          				 	+" from tblCompany a"
		          				 	+"  where a.yqzlNo=?";
		     		    PreparedStatement pss = conn.prepareStatement(sql) ;
		     		    pss.setString(1, yqzlNo) ;
		     		    ResultSet rss = pss.executeQuery() ;
		     		    if(rss.next()){
		     		    	map.put("classCode", rss.getString("classCode"));
		     		    	rs.setRetVal(map);
		     		    }     
		          	   }catch(Exception ex){
		          		   ex.printStackTrace();
		          		   rs.retCode = ErrorCanst.DEFAULT_FAILURE ;
		          		 BaseEnv.log.error("Yqzl getRecordCount : ",ex);
		          	   }
		             }
		         });
		         return rs.getRetCode();
		     }
	       });
	       return (HashMap) rs.getRetVal();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean saveToDb(DBTableInfoBean tBean,HashMap saveValues,MessageResources resources,Hashtable props){
		
		
		
		Hashtable<String,DBTableInfoBean> allTables = BaseEnv.tableInfos;
		LoginBean loginBean = new LoginBean();
		loginBean.setId("1");
		loginBean.setSunCmpClassCode("00001");
		
		Hashtable table = ((Hashtable) BaseEnv.sessionSet.get(loginBean.getId()));
		if(table == null){
			table = new Hashtable();
			BaseEnv.sessionSet.put(loginBean.getId(), table);
		}
		
		Locale locale = Locale.getDefault();
		String addMessage = "添加成功";
		/**
		 * 字段类型是单据编号重新设置单据生成
		 */
		String tableName = "";
		
		//设置默认值
		UserFunctionMgt userMgt = new UserFunctionMgt();
		try {							
			userMgt.setDefault(tBean, saveValues, loginBean.getId());
			//明细表设置默认值
			ArrayList<DBTableInfoBean> ct = DDLOperation.getChildTables(tBean.getTableName(), allTables);
			for (int j = 0; ct != null && j < ct.size(); j++) {
				DBTableInfoBean cbean = ct.get(j);
				ArrayList clist = (ArrayList) saveValues.get("TABLENAME_" + cbean.getTableName());
				for (int k = 0; clist != null && k < clist.size(); k++) {
					HashMap cmap = (HashMap) clist.get(k);
					userMgt.setDefault(cbean, cmap, loginBean.getId());
				}
			}
		} catch (Exception e) {
			return false ;
		}
				

		/*验证非空-----这里不再需要做验证的工作，因为在add方法中有更全面的较验了*/ 
		/********************************************
		 执行相关接口  saveValues 为保存数据的HashMap 传入此参数至相应接口完成数据插入
		 ********************************************/
		MOperation mop = new MOperation();
		mop.query = true;
		mop.add = true;
		mop.update = true;
		
		Result rs;
		try {
			rs = userMgt.add(tBean, saveValues, loginBean, "", allTables, "", "", locale, addMessage, resources, props, mop, "10");
		} catch (Exception e) {
			BaseEnv.log.error("ImportThread.importData add Error:",e);
			return false;
		}
		
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			
			return true;
		} else {
			return false;
		}
	}
	public static void main(String[] args) {
		//new Yqzl().deal("http://192.168.0.66:9999", "银企直连专用普通1", "591902896910206");
		//new Yqzl(null,"").addReceive(null);
	}
}
