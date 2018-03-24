package yqzl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import javax.servlet.http.HttpServletRequest;

import yqzl.DesUtil;

import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.billNumber.BillNo;
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
 * 建业银行银企直连
 * 
 * @author dzp
 * 
 */
public class JHYqzl {
	DynDBManager dbmgt = new DynDBManager();
	static DesUtil des = new DesUtil();

	/**
	 * 银企直联处理
	 * 
	 * @param conn
	 *            数据库连接
	 * @param host
	 *            前置机地址
	 * @param lgnnam
	 *            登录名
	 * @param accnbr
	 *            使用的账号
	 */
	public String deal(HttpServletRequest req) {
		String opType = req.getParameter("opType");
		if ("getAccount".equals(opType)) {
			// 查帐号
			String acc = getAccount();
			return acc;
		}

		// 获得今天天交易记录
		List list = new ArrayList();
		String ACCNO1 = req.getParameter("ACCNO1");
		String data = req.getParameter("data");
		
		try {
			// data = URLDecoder.decode(data,"UTF-8");
			data = des.decrypt(data, "Koron123");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (data != null && data.length() > 0) {
			data = data.trim();
			String ACC_NAME = getValue(data,"ACC_NAME");
			data = data.substring(data.indexOf("</ACC_NAME>")+("</ACC_NAME>").length());
			while (data.length()>0) {
				String one = data.substring(0,data.indexOf("</DETAILINFO>")+("</DETAILINFO>").length());
				data = data.substring(data.indexOf("</DETAILINFO>")+("</DETAILINFO>").length()).trim();
				one = "<ACCNO1>"+ACCNO1+"</ACCNO1><ACC_NAME>"+ACC_NAME+"</ACC_NAME>"+one;
				list.add(one);
			}
		}
		if (list.size() > 0)
			dealRecord(list, req);

		return "Received " + list.size() + " record";
	}
	
	private static String getValue(String str,String value){
		return str.substring(str.indexOf("<"+value+">")+("<"+value+">").length(),str.indexOf("</"+value+">"));
	}

	/**
	 * 银企直联交易记录处理
	 * 
	 * @param list
	 */
	private void dealRecord(List<String> list, HttpServletRequest req) {
		// 过滤掉已经处理过的
		List<String> listNew = new ArrayList<String>();
		if (list != null) {
			BaseEnv.log.debug("建行银企直联记录数（未过滤）：" + list.size() + " --" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			for (String one : list) {
				if (getRecordCount(getValue(one,"TRAN_FLOW")) == 0) {
					listNew.add(one);
				}
			}
		}
		BaseEnv.log.debug("建行银企直联记录数（已过滤）：" + listNew.size() + " --" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		// 处理新交易
		for ( String one : listNew) {
			try {
				addReceive(one, req);
			} catch (Exception e) {
				BaseEnv.log.debug("建行添加收款单出错：" + one.toString());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 添加收款记录
	 * 
	 * @param conn
	 * @param bean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addReceive(String one, HttpServletRequest req) {

		// 添加处理完的交易到历史记录
		BaseEnv.log.debug("建行开始添加收款记录(已过滤)：" + one + "插入原始记录。");
		addRecord(one);

		BaseEnv.log.debug("建行开始添加收款记录(已过滤)：" + one + "根据编号寻找店");
		// 根据编号找到店
		HashMap<String, String> company = getComapany(getValue(one,"ACCNO1"));
		if (company == null) {
			updateRecordStatus(getValue(one,"TRAN_FLOW"), "-1", "同步失败：" + "" + getValue(one,"ACCNO1") + "找不到店");
			BaseEnv.log.debug("建行开始添加收款记录(未过滤)：" + one + "找不到店");
			return;
		}
		BaseEnv.log.debug("建行开始添加收款记录：" + one);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dfShort = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		HashMap saveValues = new HashMap();
		saveValues.put("id", IDGenerater.getId());
		saveValues.put("BillDate", dfShort.format(now));
		saveValues.put("ExeBalAmt", getValue(one,"AMT"));
		saveValues.put("AcceptTypeId", "Receive");
		saveValues.put("AccDetStatus", "0");
		saveValues.put("CreateBy", "1");
		saveValues.put("lastUpdateBy", "1");
		saveValues.put("createTime", df.format(now));
		saveValues.put("lastUpdateTime", df.format(now));
		saveValues.put("statusId", "0");
		saveValues.put("Remark", "建行直连" + getValue(one,"ACC_NAME"));
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
		saveValues.put("FactIncome", getValue(one,"AMT"));
		saveValues.put("AccAmt", getValue(one,"AMT"));
		saveValues.put("DepartmentCode", "00101");
		saveValues.put("CompanyCode", company.get("classCode"));

		saveValues.put("CashUserID", "0");
		// saveValues.put("PeriodYear", "2015");
		// saveValues.put("PeriodMonth", "4");
		// saveValues.put("Period", "4");
		// saveValues.put("PeriodMonth", "4");
		saveValues.put("AutoBillMarker", "0");
		// saveValues.put("CertificateNo", "04d8da956_1504291429536500010");

		// 明细
		ArrayList<HashMap> details = new ArrayList<HashMap>();
		HashMap detValues = new HashMap();
		detValues.put("id", IDGenerater.getId());
		detValues.put("f_ref", saveValues.get("id"));
		detValues.put("SettleType", "1");
		String Account = req.getParameter("Account");
		if (Account == null || Account.equals("")) {
			detValues.put("Account", "100201");
		} else {
			detValues.put("Account", Account);
		}

		detValues.put("Amount", getValue(one,"AMT"));
		detValues.put("SCompanyID", "00001");
		detValues.put("Remark", "建行银企直连");
		detValues.put("ExeBalFcAmt", "0.00000000");
		details.add(detValues);
		saveValues.put("TABLENAME_tblReceiveAccountDet", details);
		Hashtable props = BaseEnv.propMap;
		Object ob = req.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
		MessageResources resources = null;
		if (ob instanceof MessageResources) {
			resources = (MessageResources) ob;
		}
		BaseEnv.log.debug("建行设置收款记录值：" + one.toString());
		// 保存
		Result rs = saveToDb(GlobalsTool.getTableInfoBean("tblSaleReceive"), saveValues, resources, props, req);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			BaseEnv.log.debug("建行添加收款单成功：" + one);
			updateRecordStatus(getValue(one,"TRAN_FLOW"), "1", "同步成功");
		} else {
			BaseEnv.log.debug("建行添加收款单失败：" + one);
			updateRecordStatus(getValue(one,"TRAN_FLOW"), "-1", "同步失败：" + rs.getRetVal());
		}

	}

	/**
	 * 添加处理记录
	 * 
	 * @param bean
	 * @return
	 */
	private Result addRecord(final String one) {
		final Result rs = new Result();
		DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) {
						try {
							String sql = "INSERT INTO [tbljhyqzl]([ACCNO1],[TRANDATE],[TRANTIME],[CRE_TYP],[CRE_NO],[MESSAGE],[AMT],[AMT1],[FLAG1],[ACCNO2],[ACC_NAME1],"
									+ "[FLAG2],[TRAN_FLOW],[BFLOW],[DET_NO],[DET],[REAL_TRANDATE],[RLTV_ACCNO],[status],[msg],createTime,ACC_NAME)"+ 
									" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setObject(1, getValue(one,"ACCNO1"));
							pss.setObject(2, getValue(one,"TRANDATE"));
							pss.setObject(3,getValue(one,"TRANTIME"));
							pss.setObject(4, getValue(one,"CRE_TYP"));
							pss.setObject(5,getValue(one,"CRE_NO"));
							pss.setObject(6, getValue(one,"MESSAGE"));
							pss.setObject(7, getValue(one,"AMT"));
							pss.setObject(8, getValue(one,"AMT1"));
							pss.setObject(9, getValue(one,"FLAG1"));
							pss.setObject(10, getValue(one,"ACCNO2"));
							pss.setObject(11, getValue(one,"ACC_NAME1"));
							pss.setObject(12, getValue(one,"FLAG2"));
							pss.setObject(13, getValue(one,"TRAN_FLOW"));
							pss.setObject(14, getValue(one,"BFLOW"));
							pss.setObject(15, getValue(one,"DET_NO"));
							pss.setObject(16, getValue(one,"DET"));
							pss.setObject(17, getValue(one,"REAL_TRANDATE"));
							pss.setObject(18, getValue(one,"RLTV_ACCNO"));
							pss.setObject(19, "0");
							pss.setObject(20, "");
							pss.setObject(21, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
							pss.setObject(22, getValue(one,"ACC_NAME"));
							
							pss.execute();
						} catch (Exception ex) {
							rs.retCode = ErrorCanst.DEFAULT_FAILURE;
							BaseEnv.log.error("Yqzl addRecord : ", ex);
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
	 * 
	 * @param trxnbr
	 * @return
	 */
	private int getRecordCount(final String TRAN_FLOW) {
		final Result rs = new Result();
		rs.setRealTotal(-1);
		DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) {
						try {
							String sql = "select count(*) from tbljhyqzl where TRAN_FLOW=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, TRAN_FLOW.trim());
							ResultSet rss = pss.executeQuery();
							if (rss.next()) {
								rs.setRealTotal(rss.getInt(1));
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.retCode = ErrorCanst.DEFAULT_FAILURE;
							BaseEnv.log.error("Yqzl getRecordCount : ", ex);
						}
					}
				});
				return rs.getRetCode();
			}
		});
		return rs.getRealTotal();
	}

	/**
	 * 修改记录状态
	 * 
	 * @param TrJrn
	 * @param TimeStab
	 * @return
	 */
	private int updateRecordStatus(final String TRAN_FLOW, final String status, final String msg) {
		final Result rs = new Result();
		rs.setRealTotal(-1);
		DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) {
						try {
							String sql = "update tbljhyqzl set status=?,msg=? where TRAN_FLOW=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, status);
							pss.setString(2, msg);
							pss.setString(3, TRAN_FLOW.trim());

							pss.execute();

						} catch (Exception ex) {
							ex.printStackTrace();
							rs.retCode = ErrorCanst.DEFAULT_FAILURE;
							BaseEnv.log.error("Yqzl getRecordCount : ", ex);
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
	 * 
	 * @param trxnbr
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getAccount() {
		final HashMap map = new HashMap();
		final Result rs = new Result();
		DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					@SuppressWarnings("unchecked")
					public void execute(Connection conn) {
						try {
							String sql = " select zlaccount from tblCompany where zlbank='jsyh' and len(isnull(zlaccount,''))>0 ";
							PreparedStatement pss = conn.prepareStatement(sql);
							ResultSet rss = pss.executeQuery();
							String zlaccount = "";
							while (rss.next()) {
								zlaccount += rss.getString(1) + ";";
							}
							rs.retVal = zlaccount;
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.retCode = ErrorCanst.DEFAULT_FAILURE;
							BaseEnv.log.error("JHYqzl getAccount : ", ex);
						}
					}
				});
				return rs.getRetCode();
			}
		});
		return (String) rs.getRetVal();
	}

	/**
	 * 查询门店
	 * 
	 * @param trxnbr
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private HashMap getComapany(final String yqzlNo) {
		final HashMap map = new HashMap();
		final Result rs = new Result();
		DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					@SuppressWarnings("unchecked")
					public void execute(Connection conn) {
						try {
							String sql = "select a.classCode" + " from tblCompany a" + "  where a.zlAccount=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, yqzlNo);
							ResultSet rss = pss.executeQuery();
							if (rss.next()) {
								map.put("classCode", rss.getString("classCode"));
								rs.setRetVal(map);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.retCode = ErrorCanst.DEFAULT_FAILURE;
							BaseEnv.log.error("Yqzl getRecordCount : ", ex);
						}
					}
				});
				return rs.getRetCode();
			}
		});
		return (HashMap) rs.getRetVal();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Result saveToDb(DBTableInfoBean tBean, HashMap saveValues, MessageResources resources, Hashtable props, HttpServletRequest req) {

		Hashtable<String, DBTableInfoBean> allTables = BaseEnv.tableInfos;
		LoginBean loginBean = new LoginBean();
		loginBean.setId("1");
		loginBean.setSunCmpClassCode("00001");

		Hashtable table = ((Hashtable) BaseEnv.sessionSet.get(loginBean.getId()));
		if (table == null) {
			table = new Hashtable();
			BaseEnv.sessionSet.put(loginBean.getId(), table);
		}

		BillNo billno = BillNoManager.find("tblSaleReceive_BillNo");
		if (billno != null) {
			// 启用单据编号连续后，从数据库读编号,或者单据编号为空

			String valStr = BillNoManager.find("tblSaleReceive_BillNo", saveValues, loginBean);
			if ("".equals(valStr)) {
				/* 单据编号无法生成 */
				Result rs = new Result();
				rs.retCode = ErrorCanst.DEFAULT_FAILURE;
				rs.retVal = "生成单据编号异常";
				return rs;
			} else {
				saveValues.put("BillNo", valStr);
			}
		}

		Locale locale = Locale.getDefault();
		String addMessage = "添加成功";
		/**
		 * 字段类型是单据编号重新设置单据生成
		 */
		String tableName = "";

		// 设置默认值
		UserFunctionMgt userMgt = new UserFunctionMgt();
		try {
			userMgt.setDefault(tBean, saveValues, loginBean.getId());
			// 明细表设置默认值
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
			Result rs = new Result();
			rs.retCode = ErrorCanst.DEFAULT_FAILURE;
			rs.retVal = "设置默认值失败";
			return rs;
		}

		/* 验证非空-----这里不再需要做验证的工作，因为在add方法中有更全面的较验了 */
		/********************************************
		 * 执行相关接口 saveValues 为保存数据的HashMap 传入此参数至相应接口完成数据插入
		 ********************************************/
		MOperation mop = new MOperation();
		mop.query = true;
		mop.add = true;
		mop.update = true;

		Result rs;
		try {
			rs = userMgt.add(tBean, saveValues, loginBean, "", allTables, "", "", locale, addMessage, resources, props, mop, "10");
		} catch (Exception e) {
			BaseEnv.log.error("ImportThread.importData add Error:", e);
			rs = new Result();
			rs.retCode = ErrorCanst.DEFAULT_FAILURE;
			rs.retVal = "执行自定义插入失败";
			return rs;
		}

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			return rs;
		} else {
			/* 删除单据编号 */
			SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, locale.toString(), "");
			rs.retVal = saveErrrorObj.getMsg();
			return rs;
		}
	}

	public static void main(String[] args) {
		// new Yqzl().deal("http://192.168.0.66:9999", "银企直连专用普通1",
		// "591902896910206");
		// new Yqzl(null,"").addReceive(null);
	}
}
