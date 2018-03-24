package com.koron.oa.oaWorkLogTemplate;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.OATaskBean;
import com.koron.oa.bean.OAWorkLogTemplateBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;

/**
 * 
 * <p>Title:通用评论数据库操作类</p> 
 * <p>Description: </p>
 *
 * @Date:2013/11/5
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 徐杰俊
 */
public class OAWorkLogTemplateMgt extends AIODBManager{

	/**
	 * 公共sql查询
	 * @param sql
	 * @param param
	 * @return
	*/
	public Result publicSqlQuery(String sql,ArrayList param){
		return sqlList(sql,param);
	}
	
	/**
	 * 模板查询
	 * @param loginBean
	 * @param workLogType
	 * @return
	 */
	public Result workLogTemplateQuery(LoginBean loginBean){
		String hql = "FROM OAWorkLogTemplateBean WHERE 1=1 ";
		if(!"1".equals(loginBean.getId())){
			hql +=" and ( ','+deptIds like '%,"+loginBean.getDepartCode()+",%' or ','+userIds like '%,"+loginBean.getId()+",%')";
		}
		hql +=" ORDER BY statusId DESC,lastUpdateTime DESC";
		return list(hql,new ArrayList());
	}
	
	/**
	 * 加载Bean
	 * @param templateId
	 * @return
	 */
	public OAWorkLogTemplateBean loadTemplateBean(String templateId){
		OAWorkLogTemplateBean bean = null;
		Result rs = loadBean(templateId,OAWorkLogTemplateBean.class);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			bean = (OAWorkLogTemplateBean)rs.retVal;
		}
		return bean;
	}
	
	/**
	 * 添加bean
	 * @param bean
	 * @return
	 */
	public Result addTemplateBean(OAWorkLogTemplateBean bean){
		return addBean(bean);
	}
	
	/**
	 * 修改bean
	 * @param bean
	 * @return
	 */
	public Result updateTemplateBean(OAWorkLogTemplateBean bean){
		return updateBean(bean);
	}
	
	/**
	 * 删除bean
	 * @param bean
	 * @return
	 */
	public Result delTemplateBean(String templateId){
		return deleteBean(templateId, OAWorkLogTemplateBean.class, "id");
	}
	
	/**
	 * 根据当前用户获取模板内容
	 * @param loginBean
	 * @return
	 */
	public String getPlanTemplateContent(LoginBean loginBean,String workLogType){
		String retContent = "";
		String sql = "select top 1 content from tblPlanTemplate WHERE (','+userIds like ? or  ','+deptIds like ?) and toplanType like ? and isNull(content,'') <> '' and statusId ='0'" ;
		ArrayList param = new ArrayList();
		param.add("%,"+loginBean.getId()+",%");
		param.add("%,"+loginBean.getDepartCode()+",%");
		param.add("%"+workLogType+"%");
		Result rs = sqlList(sql, param);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList list = (ArrayList)rs.retVal;
			if(list!=null && list.size()>0){
				retContent = String.valueOf(GlobalsTool.get(list.get(0),0));
			}
		}
		return retContent;
	}
}


