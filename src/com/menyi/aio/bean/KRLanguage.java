package com.menyi.aio.bean;

import java.util.HashMap;
import java.io.Serializable;
import java.util.Iterator;

/**
 * �����洢������KEY��Ӧ�����ݣ��洢��ʽ���£� id:Ϊkeyֵ,�磺tblgoods.fullname map:�д洢�Ų�ͬ���Ե���ʾ����,keyΪ��ͬ�������ԣ�valueΪ��ʾֵ��ÿһ�����Զ�Ӧһ��keyvalue
 * 
 */
public class KRLanguage implements Serializable {
	private static final long serialVersionUID = 1L;
	public HashMap<String, String> map = new HashMap<String, String>();
	private String id = "";

	/**
	 * ���ö��������ID
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * ��������������һ�����ԣ���������ù���ͬ�����ԣ����Ժ����õ�Ϊ׼
	 * 
	 * @param key ���ԣ��磺zh_cn
	 * @param value ��ʾֵ���磺��Ʒ����
	 */
	public void putLanguage(String key, String value) {
		map.put(key, value);
	}

	/**
	 * ��ȡ���������ID
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * ȡ���������Ӧ�����Ե�ֵ
	 * 
	 * @param locale ����
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
