package com.koron.openplatform.aio;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import com.koron.openplatform.Authentication;
import com.koron.openplatform.MessageBean;
import com.koron.openplatform.jd.JdOpenPlatform;
import com.koron.openplatform.taobao.TaobaoOpenPlatform;
import com.koron.openplatform.vos.OrderProduct;
import com.koron.openplatform.vos.TblOrder;
import com.koron.openplatform.vos.TblProduct;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;

public class EBListener extends TimerTask implements ServletContextListener {
	public static Connection getConnection() {
		try {
			Context ct = new InitialContext();
			ct = (Context) ct.lookup("java:comp/env");
			DataSource ds = (DataSource) ct.lookup("jdbc/sqlserver");
			return ds.getConnection();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
//		try {
//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//			try {
//				return DriverManager.getConnection(
//						"jdbc:sqlserver://localhost:1433;SelectMethod=Cursor;DatabaseName=GM", "sa", "koron.aio");
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		return null;
	}

	private List<Authentication> getAll(boolean onlyAuto) {
		String sql = "select cerid,appid,appkey,token,EBStyle from tblebcertificate";
		if (onlyAuto)
			sql += " where insign=0";
		Connection conn = getConnection();
		if (conn != null) {
			try {
				List<Authentication> list = new ArrayList<Authentication>();
				ResultSet rs = conn.createStatement().executeQuery(sql);
				while (rs.next()) {
					Authentication auth = new Authentication();
					auth.setAlias(rs.getString("cerid"));
					auth.setProvider(rs.getString("EBStyle").trim().equals("2") ? "jd" : "taobao");
					if (auth.getProvider().equals("jd")) {
						auth.put(JdOpenPlatform.ACCESSTOKEN, rs.getString("token"));
						auth.put(JdOpenPlatform.APPKEY, rs.getString("appid"));
						auth.put(JdOpenPlatform.APPSECRET, rs.getString("appkey"));
					} else {
						auth.put(TaobaoOpenPlatform.SESSIONKEY, rs.getString("token"));
						auth.put(TaobaoOpenPlatform.APPKEY, rs.getString("appid"));
						auth.put(TaobaoOpenPlatform.APPSECRET, rs.getString("appkey"));
					}
					list.add(auth);
				}
				rs.close();
				return list;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private String getStatus(Authentication auth) {
		Connection conn = getConnection();
		if (conn != null) {
			try {
				ResultSet rs = conn.createStatement().executeQuery(
						"select OrderStaID from tblEBCerDet where cerid='" + auth.getAlias() + "'");
				String ret = "";
				while (rs.next()) {
					ret += rs.getString("OrderStaID") + ",";
				}
				rs.close();
				if (ret.endsWith(","))
					ret = ret.substring(0, ret.length() - 1);
				return ret;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private void save(TblOrder order, Authentication auth) {
		Connection conn = getConnection();
		if (conn != null) {
			try {
				String sql = "insert into tblEBOrder (id,OrderID,OrderTime,PayMethod,DeliverMethod,"
						+ "AmountMoney,Pay,DeliverPay,FavourableMoney,"
						+ "NickName,LeaveWord,CerCode,InSign,HideSign,"
						+ "Remark,createTime,lastUpdateTime,MobilePhone,Phone," 
						+ "Address,receiver,createBy,opeople,ophone," +
						"omobile,oaddress,province,city,section,post,orderstaid) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement pst = conn.prepareStatement("select id from tbleborder where orderid=?");
				pst.setString(1, order.getOrderID());
				ResultSet rs = pst.executeQuery();
				if (!rs.next()) {// 如果没有订单，则重新导入
					rs.close();
					pst.close();
					conn.setAutoCommit(false);
					pst = conn.prepareStatement(sql);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String orderKey = IDGenerater.getId();
					pst.setString(1, orderKey);
					pst.setString(2, order.getOrderID());
					pst.setString(3, order.getOrderTime().substring(0, 10));
					pst.setString(4, order.getPayMethod());
					pst.setString(5, order.getDeliverMethod());
					pst.setString(6, order.getAmountMoney());
					pst.setString(7, order.getPay());
					pst.setString(8, order.getDeliverPay());
					pst.setString(9, order.getFavourableMoney());
					pst.setString(10, order.getNickName());
					pst.setString(11, order.getLeaveWord());
					pst.setString(12, order.getCerCode());
					pst.setString(13, order.getInSign().toLowerCase().equals("true") ? "0" : "1");
					pst.setString(14, order.getHideSign().toLowerCase().equals("true") ? "0" : "1");
					pst.setString(15, order.getRemark());
					pst.setString(16, sdf.format(new Date()));
					pst.setString(17, sdf.format(new Date()));
					pst.setString(18, order.getMobile());
					pst.setString(19, order.getPhoneNum());
					pst.setString(20, order.getAddress());
					pst.setString(21, order.getReceiver());
					pst.setString(22, "");
					pst.setString(23, order.getoPeople());
					pst.setString(24, order.getoPhone());
					pst.setString(25, order.getoMobile());
					pst.setString(26, order.getoAddress());
					pst.setString(27, order.getProvince());
					pst.setString(28, order.getCity());
					pst.setString(29, order.getSection());
					pst.setString(30, order.getPost());
					pst.setString(31, order.getOrderStatus());
					pst.executeUpdate();
					String[] pIds = new String[order.getProducts().size()];
					for (int i = 0; i < order.getProducts().size(); i++) {
						pIds[i] = order.getProducts().get(i).getPid();
					}
					MessageBean msg = auth.invoke("getproductdetail", pIds);
					if (msg.getCode() == 1) {
						List<TblProduct> products = (List<TblProduct>) msg.getData();
						for (TblProduct tblProduct : products) {
							save(tblProduct, auth);
						}
					}

					pst = conn
							.prepareStatement("insert into tbleborderdet (id,f_ref,num,price,sku,amount) values(?,?,?,?,?,?)");
					for (OrderProduct product : order.getProducts()) {
						pst.setString(1, IDGenerater.getId());
						pst.setString(2, orderKey);
						pst.setString(3, product.getTotal());
						pst.setString(4, product.getPrice());
						pst.setString(5, product.getProductId());
						pst.setString(
								6,
								String.valueOf(Double.parseDouble(product.getTotal())
										* Double.parseDouble(product.getPrice())));
						pst.addBatch();
					}
					pst.executeBatch();
					pst.close();
					if (BaseEnv.systemSet.get("EBAUTOIMPORT").getSetting().toLowerCase().endsWith("true")) {
						CallableStatement cst = conn.prepareCall("{call proc_EBOrderInsertOrder (@Order_id=?,@retCode=?,@retVal=?)}");
						cst.setString(1, orderKey);
						cst.registerOutParameter(2, Types.INTEGER);
						cst.registerOutParameter(3, Types.VARCHAR);
						cst.executeUpdate();
						cst.close();
					}
					conn.commit();
					conn.setAutoCommit(true);
					
//					EBTOSALESORDER

				} else// 如果已有订单则不重新导入
				{
					rs.close();
					pst.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void save(TblProduct product, Authentication auth) {
		Connection conn = getConnection();
		if (conn != null) {
			try {
				String sql = "insert into tblebgoods (id,goodsid,CerCode,goodsname,sku,"
						+ "weight,size,price,buyprice,marketprice"
						+ ",picture,createBy) values (?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement pst = conn.prepareStatement("select id from tblebgoods where goodsid=?");
				pst.setString(1, product.getGoodsID());
				ResultSet rs = pst.executeQuery();
				if (!rs.next()) {// 如果没有商品，则重新导入
					rs.close();
					pst.close();
					pst = conn.prepareStatement(sql);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String productKey = IDGenerater.getId();
					pst.setString(1, productKey);
					pst.setString(2, product.getGoodsID());
					pst.setString(3, product.getCerCode());
					pst.setString(4, subStr(product.getTittle(), 200));
					pst.setString(5, subStr(product.getSkus(), 100));
					pst.setString(6, subStr(product.getWeight(), 100));
					pst.setString(7, subStr(product.getSize(), 100));
					pst.setString(8, product.getPrice());
					pst.setString(9, product.getBuyPrice());
					pst.setString(10, product.getMarketPrice());
					pst.setString(11, subStr(product.getPicture(), 500));
					pst.setString(12, "");
					pst.executeUpdate();
				} else// 如果已有订单则不重新导入
				{
					rs.close();
					pst.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void run() {
		if(BaseEnv.systemSet.get("EBAUTOIMPORT")==null || BaseEnv.systemSet.get("EBAUTOIMPORT").getSetting()==null || !BaseEnv.systemSet.get("EBAUTOIMPORT").getSetting().toLowerCase().endsWith("true"))
		{
			return ;
		}
		
		List<Authentication> list = getAll(true);// 获取所有需要自动导入的证书
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -2);
		for (Authentication auth : list) {
			String s = getStatus(auth);
			if (s != null && !s.equals("")) {
				System.out.println(sdf.format(c.getTime()));
				MessageBean<? extends Object> a = auth.invoke("getorderids", sdf.format(c.getTime()), s);
				if (a.getCode() == 1) {
					s = String.valueOf(a.getData()); // 订单号，用逗号分隔
					System.out.println(s);
					if (!s.equals("")) {
						String[] orders = s.split(",");
						List<TblOrder> orderList = (List<TblOrder>) auth.invoke("getorder", orders).getData();
						if (orderList != null) {
							System.out.println("start====");
							for (TblOrder tblOrder : orderList) {
								save(tblOrder, auth);
							}
						}
					}
				}
			}
		}

	}

	private String subStr(String str, int len) {
		if (str != null && str.length() > len) {
			System.out.println(str);
			return str.substring(0, len);
		}
		return str;
	}

	public static void main(String[] args) {
		new EBListener().run();

	}

	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("enter eb listener");
		Timer t = new Timer();
		long period = 200000;
		String periodStr = arg0.getServletContext().getInitParameter("period");
		if(periodStr!=null)
		{
			try {
				period = Long.parseLong(periodStr);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		t.schedule(this, 60*1000,period);
	}
}
