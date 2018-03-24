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
 * ���û��Զ�����ʽ���в�����SERVLET �����ȶ�{@link #OPERATEKEY}��ֵ�����ж�
 * ֻ����{@link #GETMETHOD}�� {@link #SETMETHOD}��{@link #RESTOREMETHOD}
 *  {@link #GETMETHOD} ��Ҫ������ID <b>formId</b> <b>property</b>(��ѡ,�޴˲������ȡ��������) 
 *  {@link #SETMETHOD} ��Ҫ������ID <b>formId</b> <b>property</b> <b>value</b>(property value����һ���࣬���Ҹ���������һ��) 
 *  {@link #RESTOREMETHOD} ��Ҫ������ID <b>formId</b> <b>property</b>(��ѡ,�޴˲������ȡ��������) ����Ϊxml��ʽ
 * </pre>
 * {@link MessageBean}
 */
public class UserStyleServlet extends HttpServlet {
	/**
	 * �����ش�����
	 */
	private static final String OPERATEKEY = "OPERATE";
	/**
	 * ��ȡ�û���ʽ�Ĳ�������
	 */
	private static final String GETMETHOD = "GET";
	/**
	 * �����û���ʽ�Ĳ�������
	 */
	private static final String SETMETHOD = "SET";
	/**
	 * �����û���ʽ�Ĳ�������
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
		if (o != null && o instanceof LoginBean)// ����û��ѵ�¼
		{
			String operation = request.getParameter(OPERATEKEY);
			if (operation == null) {
				mb.setCode(6);
				mb.setMessage("MSG0006");
				mb.setDescription("δ�趨��������");
			} else if (operation.equals(GETMETHOD)) {// �����ִ�л�ȡ���Բ���

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
					mb.setDescription("�����ɹ�");
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
			} else if (operation.equals(SETMETHOD)) {// �����ִ���������Բ���

				String[] keys = request.getParameterValues("property");
				String[] values = request.getParameterValues("value");
				if (keys == null || values == null || keys.length == 0 || values.length == 0 || keys.length != values.length)// �����ֵ�Ĳ����������򷵻ش�����Ϣ
				{
					mb.setCode(2);
					mb.setMessage("MSG0002");
					mb.setDescription("��������ȷ");
				} else {
					TreeMap<String, String> treeMap = new TreeMap<String, String>();
					for (int i = 0; i < keys.length; i++) {
						treeMap.put(keys[i], values[i]);
					}
					int result = setFavourStyle(((LoginBean) o).getId(), request.getParameter("formId"), treeMap);
					if (result < 0)// ����ʧ��
					{
						mb.setCode(3);
						mb.setMessage("MSG0003");
						mb.setDescription("�����Զ�������ʧ��");
					} else {
						mb.setCode(0);
						mb.setMessage("MSG0000");
						mb.setDescription("�����ɹ�");
					}
				}
			} else if (operation.equals(RESTOREMETHOD)) {
				int result = restoreFavourStyle(((LoginBean) o).getId(), request.getParameter("formId"), request.getParameterValues("property"));
				if (result < 0)// ����ʧ��
				{
					mb.setCode(5);
					mb.setMessage("MSG0005");
					mb.setDescription("�ָ��Զ�������ʧ��");
				} else {
					mb.setCode(0);
					mb.setMessage("MSG0000");
					mb.setDescription("�����ɹ�");
				}
			} else {
				mb.setCode(4);
				mb.setMessage("MSG0004");
				mb.setDescription("������һ�������ڵĲ���");
			}
		} else // �û�δ��¼
		{
			mb.setCode(1);
			mb.setMessage("MSG0001");
			mb.setDescription("�û�δ��¼");
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
	 * �ָ��û��Զ������Ե�ֵ���������Ϊnull�򳤶�Ϊ0����ɾ�����û���formID����������
	 * @param userId �û�ID
	 * @param formId ��ID
	 * @param properties ����
	 * @return ����ɾ���ļ�¼���������Ϊ��1���ʾ���쳣
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
	 * �����û��Զ������ʽ
	 * @param userId �û����
	 * @param formId ��ID
	 * @param tm ���Ա�
	 * @return ���ش�����Ϣ,-1Ϊʧ��,1Ϊ�ɹ�
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
			TreeMap<String, String> styleMap = getFavourStyle(userId, formId, null);// ��ȡԭ������ķ��

			for (String key : tm.keySet()) {
				if (!tm.get(key).equals(styleMap.get(key)))// ������ͬԭ���ͬ��������û����
				{
					map.remove(userId+"_"+formId+"_"+String.valueOf(key));
					pst.setString(4, key);
					pst.setString(1, tm.get(key));
					int count = pst.executeUpdate();
					if (count == 0)// ���û���µ���¼����ԭ���޴��Զ������ԣ���Ҫ����һ������
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
	 * ��ȡ�û��Զ������ʽ ��ʽ��÷�ʽ��
	 * ��ȡ���û���ʹ�õ�style��Ӧ�����ԣ�
	 * �ٻ�ȡ�û��Զ��������ֵ��
	 * �û��Զ���������ͬʹ�õ�style�ظ���ʱ����ʹ���Զ��õ�����
	 * </pre>
	 * @param userId �û���ID���
	 * @param formId ��ID
	 * @param property ����
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TreeMap<String, String> getFavourStyle(String userId, String formId, String property) {
		TreeMap<String, String> tm = new TreeMap<String, String>();
		if(property!=null && !property.equals(""))//����ǵ�ȡһ������,�ȴӻ���ȡ�����ȡ�����ٴ����ݿ�ȡ
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
				map.put(userId+"_"+formId+"_"+String.valueOf(objects[0]),String.valueOf(objects[1]));//���뻺��
			}
		}
		return tm;
	}
}