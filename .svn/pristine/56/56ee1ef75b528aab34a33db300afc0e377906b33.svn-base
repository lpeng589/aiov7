package com.koron.robot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

public class InfoItem {
	private String name;
	private String site;
	private Vector<String> phone = new Vector<String>();
	private Vector<String> mobile= new Vector<String>();
	private Vector<String> email= new Vector<String>();
	private Vector<String> fax= new Vector<String>();
	
	private boolean valid=false;
	
	
	public static String getString(Vector<String> set){
		if(set == null || set.size() ==0) return "";
		if(set.size() == 1) return set.get(0);
		
		StringBuffer buf = new StringBuffer();
		for(String s:set){
			buf.append(s+",");
		}	
		String ret = buf.toString();
		return ret.substring(0,ret.length() -1);
	}

	public InfoItem(String name, String site) {
		super();
		this.name = name;
		this.site = site;		
	}
	
	

	public boolean isValid() {
		return valid;
	}



	public void setValid(boolean valid) {
		this.valid = valid;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String toString() {
		String ret = name + ":" + site + "\n邮件";
		for (String mail : email) {
			ret += mail + "\t";
		}
		ret += "\n手机";
		for (String m : mobile) {
			ret += m + "\t";
		}
		ret += "\n电话";
		for (String m : phone) {
			ret += m + "\t";
		}
		ret += "\n传真";
		for (String m : fax) {
			ret += m + "\t";
		}
		return ret;
	}

	public Vector<String> getPhone() {
		return phone;
	}

	public void setPhone(Vector<String> phone) {
		this.phone = phone;
	}

	public Vector<String> getMobile() {

		return mobile;
	}

	public void setMobile(Vector<String> mobile) {
		this.mobile = mobile;
	}

	public Vector<String> getEmail() {
		return email;
	}

	public void setEmail(Vector<String> email) {
		this.email = email;
	}

	public Vector<String> getFax() {
		return fax;
	}

	public void setFax(Vector<String> fax) {
		this.fax = fax;
	}
}
