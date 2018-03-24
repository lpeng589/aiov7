package com.menyi.web.util;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class GenJS {

	private static Hashtable<String,Long> jsMap = new Hashtable<String,Long>(); //用于保存更新过的js文件
	private static long jsTime = System.currentTimeMillis();
	
	public static void clearJS(){
		jsTime = System.currentTimeMillis();
		jsMap.clear();
	}
	public static void  putJS(String file,Long time){
		jsMap.put(file, time);
	}
	
	public static Long  getJS(String file){
		return jsMap.get(file);
	}
	public static String djs(String js){
		return js+"?"+jsTime;
	}
	
	
	
	public static String js(String path,String templete, HashMap map) {
		String retStr = "";
		try {
			VelocityEngine ve = new VelocityEngine();
			Properties properties = new Properties(); 
			//设置模板的路径 
			properties.setProperty(ve.FILE_RESOURCE_LOADER_PATH,path);   
			//初始花velocity 让设置的路径生效 
			ve.init(properties);  
			
			Template t = ve.getTemplate(templete,"UTF-8"); 
			VelocityContext context = new VelocityContext();
			Iterator it = map.keySet().iterator();
			while(it.hasNext()){
				String str = it.next().toString();
				context.put(str, map.get(str));
			}		
			
			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			/* show the World */
			return writer.toString();
		} catch (Exception e) {
			BaseEnv.log.error("GenJS.js() Error:",e);
		}

		return retStr;
	}
}
