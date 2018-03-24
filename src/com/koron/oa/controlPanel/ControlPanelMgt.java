package com.koron.oa.controlPanel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.Department;
import com.koron.oa.bean.OABBSUserBean;
import com.koron.oa.bean.OAKnowFileBean;
import com.koron.oa.bean.OAKnowFolderBean;
import com.koron.oa.publicMsg.newordain.OAOrdainMgt;
import com.menyi.aio.bean.CompanyBean;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;


/**
 * 
 * 
 * <p>Title:֪ʶ����Mgt</p> 
 * <p>Description: </p>
 *
 * @Date:2012-6-13
 * @Copyright: �������
 * @Author ���ҿ�
 */
public class ControlPanelMgt extends AIODBManager{
	
	/**
	 * ����id������Ϣ
	 * @param fileId
	 * @return
	 */
	public Result loadEmployee(String employeeId){
		return loadBean(employeeId, EmployeeBean.class);
	}
	
	/**
	 * ����ְԱ
	 * @param employee
	 */
	public void update(EmployeeBean employee){
		this.updateBean(employee);
	}
	
	/**
	 * ����EmployeeId����OABBSUser
	 * @param fileId
	 * @return
	 */
	public Result findOABBSUserByEmployeeId(String employeeId){
		List list = new ArrayList();
		String sql = "from OABBSUserBean where userID = '" + employeeId + "'" ;
		return this.list(sql, list);
		
	}
	
	
	
	
}
