package com.menyi.web.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class zh_CN_TO_zh_TW {

	public static void main(String[] args) {
		try {
			/*
			 * 把数据库中表tblLanguage中zh_CN列全选中，复制到word 2003中转换成繁体
			 * 保存名为test.txt，放到D盘。
			 * */
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream("D:\\zh.txt")));
			String str = null ;
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver") ;
			Connection conn = DriverManager.getConnection(
							"jdbc:sqlserver://192.168.2.1:5303;SelectMethod=Cursor;DatabaseName=aio_sy","sa","sql2005") ;
			String sql = "select * from tblLanguage" ;
			Statement state = conn.createStatement() ;
			ResultSet rs = state.executeQuery(sql) ;
			int m = 0 ;
			while(rs.next()){
				str= br.readLine() ;
				String updateSql = "update tblLanguage set zh_TW = '"+str+"'"+"where id ='" + rs.getString(1)+"'" ;
				Statement state2 = conn.createStatement() ;
				int n = state2.executeUpdate(updateSql) ;
				if(n>0)
					m++ ;
				
			}
			System.out.println(m+"行已更新");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
