package com.menyi.aio.web.userFunction;

import java.util.ArrayList;

/**
*
* <p>Title: </p>
*
* <p>Description: </p>
*
* <p>Copyright: Copyright (c) 2008</p>
*
* <p>Company: 周新宇</p>
*
* @author 周新宇
* @version 1.0
* @preserve all
*/
public class TreeItem {
	public String classCode;
	public String name;
	public String number;
	public String id;
	public boolean nocheck=true;
	
	public TreeItem(String classCode,String name,String number,boolean nocheck,String id){
		this.classCode = classCode;
		this.number = number;
		this.name = name;
		this.nocheck = nocheck;	
		this.id=id;
	}
	
	
	private ArrayList<TreeItem> nodes= new ArrayList();

	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isNocheck() {
		return nocheck;
	}

	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}

	public ArrayList<TreeItem> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<TreeItem> nodes) {
		this.nodes = nodes;
	}
}
