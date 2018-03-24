package com.koron.crm.state;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.ErrorCanst;

public class StateConfig extends DBManager{

	/**
	 * 解析统计XML文件
	 * @param file
	 * @return
	 */
	public ArrayList<String[]> parse(File file){
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
			Element root = document.getDocumentElement();
			int nodeLength = root.getChildNodes().getLength() ;
			ArrayList<String[]> listState = new ArrayList<String[]>() ;
			for(int i = 0; i < nodeLength; i++){
				Node node = root.getChildNodes().item(i);
				if(null !=node.getAttributes() && null != node.getAttributes().getNamedItem("name")){
					String[] strState = new String[2] ;
					strState[0] = node.getAttributes().getNamedItem("name").getNodeValue() ;
					strState[1] = node.getTextContent() ;
					listState.add(strState) ;
				}
			}
			return listState ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}
	
	/**
	 * 查询统计
	 * @return
	 */
	public HashMap<String, String> queryState(final ArrayList<String[]> listSql,
			final String clientId) {
		final HashMap<String, String> stateMap = new HashMap<String, String>();
		if (listSql != null) {
			final Result rs = new Result();
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection conn) throws SQLException {
							try {
								for(String[] str : listSql){
									String sql = str[1] ;
									sql = sql.replace("@ClientId", "'"+clientId+"'") ;
									PreparedStatement pss = conn.prepareStatement(sql);
									ResultSet rss = pss.executeQuery() ;
									if(rss.next()){
										stateMap.put(str[0], String.valueOf(rss.getDouble(1))) ;
									}
								}
							} catch (Exception ex) {
								ex.printStackTrace();
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								return;
							}
						}
					});
					return rs.getRetCode();
				}
			});
			rs.setRetCode(retCode);
		}
		return stateMap;
	}
	
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
		/*File file = new File("D:\\workspace\\AIO\\config_sy\\StateConfig.xml") ;
		ArrayList<String[]> listState = new StateConfig().parse(file) ;
		for(String[] str : listState){
			System.out.println(str[0]+"---------"+str[1]);
		}*/
		Calendar calendar2 = Calendar.getInstance() ;
		calendar2.set(Calendar.DAY_OF_YEAR,calendar2.get(Calendar.DAY_OF_YEAR) + 6);
		System.out.println(BaseDateFormat.format(calendar2.getTime(), BaseDateFormat.yyyyMMdd));
	}
}
