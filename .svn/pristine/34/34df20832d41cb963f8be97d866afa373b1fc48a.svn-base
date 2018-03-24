package com.menyi.web.util;

import java.sql.*;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.hibernate.proxy.*;
import org.hibernate.jmx.*;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileReader;

public class ConnectionEnv {
	public static String dataBaseName;
    public static Connection getConnection()throws Exception{
        Connection conn=null;
//        String path= Thread.currentThread().getContextClassLoader().getResource("").toString();
//        
//        path=java.net.URLDecoder.decode(path,"UTF-8");
//        int n = path.indexOf("website") ;
//        String oldPath=path;
//        File file;
//        if(n!=-1){
//        	path=oldPath.substring("file:/".length(),oldPath.indexOf("website"))+"apache-tomcat-6.0.16/conf/Catalina/localhost/ROOT.xml";
//        	file = new File(path);
//        	if(!file.exists()){
//        		path=oldPath.substring("file:/".length(),oldPath.indexOf("website"))+"AIOServer/conf/Catalina/localhost/ROOT.xml";
//        		file = new File(path);
//        		if(!file.exists()){
//        			path=oldPath.substring("file:/".length(),oldPath.indexOf("website"))+"AIOServer_x64/conf/Catalina/localhost/ROOT.xml";
//            		file = new File(path);
//        		}
//        	}
//        }else{
//        	path = path.substring("file:/".length(),path.indexOf("/lib")) +"/conf/Catalina/localhost/ROOT.xml";
//        	file = new File(path);
//        }
        File file = new File("../conf/Catalina/localhost/ROOT.xml");
        FileReader in = new FileReader(file);

        char [] cr=new char[1024];

        int count=0;
        String content="";
        while((count=in.read(cr))>-1)
        {
           content+=new String(cr,0,count);
        }
        String driver=null;
        String url=null;
        String userName=null;
        String passWord=null;

        String tempCotent="";
        int index=content.indexOf("driverClassName=\"")+"driverClassName=\"".length();
        tempCotent=content.substring(index);
        driver=tempCotent.substring(0,tempCotent.indexOf("\""));


        index=content.indexOf("url=\"")+"url=\"".length();
        tempCotent=content.substring(index);
        url=tempCotent.substring(0,tempCotent.indexOf("\""));


        index=content.indexOf("username=\"")+"username=\"".length();
        tempCotent=content.substring(index);
        userName=tempCotent.substring(0,tempCotent.indexOf("\""));

        index=content.indexOf("password=\"")+"password=\"".length();
        tempCotent=content.substring(index);
        passWord=tempCotent.substring(0,tempCotent.indexOf("\""));

        Class.forName(driver);
        conn=DriverManager.getConnection(url,userName,passWord);

        index=content.indexOf("DatabaseName=")+"DatabaseName=".length();
        tempCotent=content.substring(index);
        dataBaseName=tempCotent.substring(0,tempCotent.indexOf("\""));
        
        return conn;
    }
    
    public static void main(String[] args) {
		try {
			getConnection() ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
