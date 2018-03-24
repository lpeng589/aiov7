package com.menyi.aio.bean;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


public class RoleModuleScopeBean implements Serializable {
	public int moduleOpId;
	public int scopeId;
	public int getModuleOpId() {
		return moduleOpId;
	}
	public void setModuleOpId(int moduleOpId) {
		this.moduleOpId = moduleOpId;
	}
	public int getScopeId() {
		return scopeId;
	}
	public void setScopeId(int scopeId) {
		this.scopeId = scopeId;
	}
	
	

}

