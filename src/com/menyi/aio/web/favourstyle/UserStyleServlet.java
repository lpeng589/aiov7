package com.menyi.aio.web.favourstyle;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.dbfactory.Result;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.LRUMap;

/**<pre>
 * 对用户自定义样式进行操作的SERVLET 程序先对{@link #OPERATEKEY}的值进行判断
 * 只能是{@link #GETMETHOD}， {@link #SETMETHOD}，{@link #RESTOREMETHOD}
 *  {@link #GETMETHOD} 需要参数表单ID <b>formId</b> <b>property</b>(可选,无此参数则获取所有属性) 
 *  {@link #SETMETHOD} 需要参数表单ID <b>formId</b> <b>property</b> <b>value</b>(property value必须一样多，并且各自至少有一个) 
 *  {@link #RESTOREMETHOD} 需要参数表单ID <b>formId</b> <b>property</b>(可选,无此参数则获取所有属性) 返回为xml格式
 * </pre>
 * {@link MessageBean}
 */
public class UserStyleServlet extends HttpServlet {
	/**
	 * 操作回传参数
	 */
	private static final String OPERATEKEY = "OPERATE";
	/**
	 * 获取用户样式的操作参数
	 */
	private static final String GETMETHOD = "GET";
	/**
	 * 设置用户样式的操作参数
	 */
	private static final String SETMETHOD = "SET";
	/**
	 * 重置用户样式的操作参数
	 */
	private static final String RESTOREMETHOD = "RESTORE";

	/**
	 * 
	 */
	private static final long serialVersionUID = -7581517313477928547L;

	private static final LRUMap<String, String> map = new LRUMap<String, String>(500);
	
	@Override
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		doPost(arg0, arg1);
	}

	/**
	 * 
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		MessageBean mb = new MessageBean();
		TreeMap<String, String> tm = new TreeMap<String, String>();
		Object o = request.getSession().getAttribute("LoginBean");
		if (o != null && o instanceof LoginBean)// 如果用户已登录
		{
			String operation = request.getParameter(OPERATEKEY);
			if (operation == null) {
				mb.setCode(6);
				mb.setMessage("MSG0006");
				mb.setDescription("未设定操作类型");
			} else if (operation.equals(GETMETHOD)) {// 如果是执行获取属性操作

				tm = getFavourStyle(((LoginBean) o).getId(), request.getParameter("formId"), request.getParameter("property"));
				try {
					Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
					Element root = doc.createElement("root");
					Element data = doc.createElement("data");
					Set<String> keys = tm.keySet();
					for (String string : keys) {
						Element item = doc.createElement(string);
						item.appendChild(doc.createTextNode(tm.get(string)));
						data.appendChild(item);
					}
					doc.appendChild(root);
					root.appendChild(data);
					mb.setCode(0);
					mb.setMessage("MSG0000");
					mb.setDescription("操作成功");
					root.appendChild(mb.toXml(doc));
					TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(response.getOutputStream()));
				} catch (TransformerConfigurationException e) {
					e.printStackTrace();
				} catch (TransformerException e) {
					e.printStackTrace();
				} catch (TransformerFactoryConfigurationError e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				}
				return;
			} else if (operation.equals(SETMETHOD)) {// 如果是执行设置属性操作

				String[] keys = request.getParameterValues("property");
				String[] values = request.getParameterValues("value");
				if (keys == null || values == null || keys.length == 0 || values.length == 0 || keys.length != values.length)// 如果传值的参数有问题则返回错误信息
				{
					mb.setCode(2);
					mb.setMessage("MSG0002");
					mb.setDescription("参数不正确");
				} else {
					TreeMap<String, String> treeMap = new TreeMap<String, String>();
					for (int i = 0; i < keys.length; i++) {
						treeMap.put(keys[i], values[i]);
					}
					int result = setFavourStyle(((LoginBean) o).getId(), request.getParameter("formId"), treeMap);
					if (result < 0)// 设置失败
					{
						mb.setCode(3);
						mb.setMessage("MSG0003");
						mb.setDescription("设置自定义属性失败");
					} else {
						mb.setCode(0);
						mb.setMessage("MSG0000");
						mb.setDescription("操作成功");
					}
				}
			} else if (operation.equals(RESTOREMETHOD)) {
				int result = restoreFavourStyle(((LoginBean) o).getId(), request.getParameter("formId"), request.getParameterValues("property"));
				if (result < 0)// 设置失败
				{
					mb.setCode(5);
					mb.setMessage("MSG0005");
					mb.setDescription("恢复自定义属性失败");
				} else {
					mb.setCode(0);
					mb.setMessage("MSG0000");
					mb.setDescription("操作成功");
				}
			} else {
				mb.setCode(4);
				mb.setMessage("MSG0004");
				mb.setDescription("请求了一个不存在的操作");
			}
		} else // 用户未登录
		{
			mb.setCode(1);
			mb.setMessage("MSG0001");
			mb.setDescription("用户未登录");
		}
		try {
			Element msg = mb.toXml();
			Element root = msg.getOwnerDocument().createElement("root");
			root.appendChild(msg);
			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(root), new StreamResult(response.getOutputStream()));
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}
	}

	/**
	 * 恢复用户自定义属性的值，如果属性为null或长度为0，则删除掉用户对formID的所有设置
	 * @param userId 用户ID
	 * @param formId 表单ID
	 * @param properties 属性
	 * @return 反回删除的记录条数，如果为－1则表示有异常
	 */
	private int restoreFavourStyle(String userId, String formId, String[] properties) {
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			Context ct = new InitialContext();
			ct = (Context) ct.lookup("java:comp/env");
			DataSource ds = (DataSource) ct.lookup("jdbc/sqlserver");
			conn = ds.getConnection();
			String sql = "DELETE tblfacestyle WHERE employeeid=? and formid=? ";
			if (properties != null && properties.length > 0)
				sql += " and property=? ";
			pst = conn.prepareStatement(sql);
			pst.setString(1, userId);
			pst.setString(2, formId);
			if (properties != null && properties.length > 0) {
				for (int i = 0; i < properties.length; i++) {
					pst.setString(3, properties[i]);
					pst.addBatch();
					map.remove(userId+"_"+formId+"_"+properties[i]);
				}
			} else {
				pst.addBatch();
			}
			int[] count = pst.executeBatch();
			int ret = 0;
			for (int i = 0; i < count.length; i++) {
				ret += count[i];
			}
			pst.close();
			pst = null;
			conn.close();
			conn = null;
			return ret;
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pst != null)
					pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	/**
	 * 设置用户自定义的样式
	 * @param userId 用户编号
	 * @param formId 表单ID
	 * @param tm 属性表
	 * @return 返回处理信息,-1为失败,1为成功
	 */
	public static int setFavourStyle(String userId, String formId, TreeMap<String, String> tm) {
		Connection conn = null, conn2 = null;
		PreparedStatement pst = null, pst2 = null;
		try {
			Context ct = new InitialContext();
			ct = (Context) ct.lookup("java:comp/env");
			DataSource ds = (DataSource) ct.lookup("jdbc/sqlserver");
			conn = ds.getConnection();
			conn2 = ds.getConnection();
			pst = conn.prepareStatement("UPDATE tblfacestyle SET favour=? WHERE employeeid=? and formid=? and property = ?");
			pst2 = conn2.prepareStatement("INSERT INTO tblfacestyle (favour,employeeid,formid,property) values(?,?,?,?)");
			pst.setString(2, userId);
			pst.setString(3, formId);
			pst2.setString(2, userId);
			pst2.setString(3, formId);
			TreeMap<String, String> styleMap = getFavourStyle(userId, formId, null);// 获取原来定义的风格

			for (String key : tm.keySet()) {
				if (!tm.get(key).equals(styleMap.get(key)))// 如果风格同原风格不同，则更新用户风格
				{
					map.remove(userId+"_"+formId+"_"+String.valueOf(key));
					pst.setString(4, key);
					pst.setString(1, tm.get(key));
					int count = pst.executeUpdate();
					if (count == 0)// 如果没更新到记录，则原来无此自定义属性，需要新增一个属性
					{
						pst2.setString(4, key);
						pst2.setString(1, tm.get(key));
						pst2.executeUpdate();
					}
				}
			}
			pst.close();
			pst = null;
			pst2.close();
			pst2 = null;
			conn.close();
			conn = null;
			conn2.close();
			conn2 = null;
			return 1;
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pst != null)
					pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (pst2 != null)
					pst2.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (conn2 != null && !conn2.isClosed())
					conn2.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	/**<pre>
	 * 获取用户自定义的样式 样式获得方式：
	 * 先取得用户所使用的style对应的属性，
	 * 再获取用户自定义的属性值，
	 * 用户自定义属义如同使用的style重复的时候则使用自定久的属性
	 * </pre>
	 * @param userId 用户的ID编号
	 * @param formId 表单ID
	 * @param property 属性
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TreeMap<String, String> getFavourStyle(String userId, String formId, String property) {
		TreeMap<String, String> tm = new TreeMap<String, String>();
		if(property!=null && !property.equals(""))//如果是单取一个属性,先从缓存取，如果取不到再从数据库取
		{
			String value = map.get(userId+"_"+formId+"_"+property);
			if(value!=null)
			{
				tm.put(property, value);
				return tm;
			}
		}
		String sql = "SELECT property,favour FROM tblfacestyle where employeeid=? and formid=? ";
		ArrayList<String> al = new ArrayList<String>();
		al.add(userId);
		al.add(formId.trim());
		if (property != null && !property.equals("")) {
			sql += " and property=?";
			al.add(property);
		}
		Result rs = new AIODBManager().sqlList(sql, al);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<Object[]> objList = (List<Object[]>) rs.getRetVal();
			if(objList.size()==0 && (property==null || !property.equals("")))
			{
				map.put(userId+"_"+formId+"_"+property,null);
			}else
			for (Object[] objects : objList) {
				tm.put(String.valueOf(objects[0]), String.valueOf(objects[1]));
				map.put(userId+"_"+formId+"_"+String.valueOf(objects[0]),String.valueOf(objects[1]));//加入缓存
			}
		}
		return tm;
	}
}