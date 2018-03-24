package com.menyi.aio.bean;

import java.util.HashMap;
import java.io.Serializable;
import java.util.Iterator;

/**
 * 用来存储多语言KEY对应的内容，存储格式如下： id:为key值,如：tblgoods.fullname map:中存储着不同语言的显示内容,key为不同区域语言，value为显示值，每一种语言对应一个keyvalue
 * 
 */
public class KRLanguage implements Serializable {
	private static final long serialVersionUID = 1L;
	public HashMap<String, String> map = new HashMap<String, String>();
	private String id = "";

	/**
	 * 设置多语言项的ID
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 给多语言项增加一种语言，如果已设置过相同的语言，则以后设置的为准
	 * 
	 * @param key 语言，如：zh_cn
	 * @param value 显示值，如：商品名称
	 */
	public void putLanguage(String key, String value) {
		map.put(key, value);
	}

	/**
	 * 获取此语言项的ID
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 取多语言项对应的语言的值
	 * 
	 * @param locale 语言
	 * @return
	 */
	public String get(String locale) {
		String o = map.get(locale);
		o = o == null ? "" : o;
		return o;
	}
	@Override
	public String toString() {
		Iterator<String> it = map.keySet().iterator();
		String str = "";
		while (it.hasNext()) {
			String key = it.next();
			str += key + ":" + map.get(key) + ";";
		}
		return str;
	} 
	public String toAddSQL() {
		Iterator<String> it = map.keySet().iterator();
		if(map.size() ==0){
			return "";
		}
		String fl = "";
		String vl = "";
		while (it.hasNext()) {
			String key = it.next();
			fl +=","+key;
			vl +=",'"+map.get(key)+"'";
		}
		return "insert into tblLanguage("+(fl.substring(1))+",id) values("+(vl.substring(1))+",'"+id+"')";
	}
	
}
