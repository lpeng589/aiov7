package com.koron.oa.workflow.consignation;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.OAWorkConsignBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;


/**
 * 
 * <p>Title:工作流委托的数据库操作类</p> 
 * <p>Description: </p>
 *
 * @Date:Sep 2, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class OAConsignationMgt extends AIODBManager {

	/**
	 * 添加委托
	 * @param workConsign
	 * @return
	 */
	public Result addConsign(OAWorkConsignBean workConsign) {
		//判断流程是否已经委托给其它人了 ;str1 = aaaaaaaa;bbbbbbbbbbbb;cccccccccccc;| str2 =;111111111111;bbbbbbbbbbbbbb;cccccccc;
		//判断字符串str1中的bbbbbbbbbbbbbb已经在str2中
		String sql = "select flowName from OAWorkConsignation where userId=? and state=1";
		ArrayList<String> param = new ArrayList<String>();
		param.add(workConsign.getUserid());
		Result result = sqlList(sql, param);
		boolean noConsign = true;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList)result.retVal).size()>0){
			//委托全部流程
			if(workConsign.getFlowName()==null || workConsign.getFlowName().length()==0){
				result.retCode = ErrorCanst.MULTI_VALUE_ERROR;
				noConsign = false;
			}else{
				ArrayList objList = (ArrayList) result.retVal;
				String flowNames = "";
				for(int i=0;i<objList.size();i++){
					flowNames += String.valueOf(((Object[])objList.get(i))[0]);
				}
				//委托某些流程
				for(String flow :workConsign.getFlowName().split(";")){
					if(flowNames.contains(flow+";") || flowNames==null || flowNames.length()==0){
						result.retCode = ErrorCanst.MULTI_VALUE_ERROR;
						noConsign = false;
						break;
					}
				}
			}
		}
		if(noConsign){
			result = addBean(workConsign);
		}
		return result; 
	}
	
	/**
	 * 加载委托
	 * @param keyId
	 * @return
	 */
	public OAWorkConsignBean loadConsign(String keyId){
		Result result = loadBean(keyId, OAWorkConsignBean.class);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			return (OAWorkConsignBean)result.retVal;
		}
		return null;
	}

	/**
	 * 修改委托
	 * @param workConsign
	 * @return
	 */
	public Result updateConsign(OAWorkConsignBean workConsign){
		return updateBean(workConsign);
	}
	
	
	/**
	 * 查询委托
	 * @param consignForm
	 * @return
	 */
	public Result queryConsign(OAConsignSearchForm consignForm,String loginId){
		
		ArrayList<String> param = new ArrayList<String>();
		String hql = "from OAWorkConsignBean bean where 1=1 ";
		if(!"1".equals(loginId) || !"all".equals(consignForm.getQueryType())){
			if("myself".equals(consignForm.getQueryType())){
				hql += " and bean.congignuserid=?";
				param.add(loginId);
			}else if("others".equals(consignForm.getQueryType())){
				hql += " and bean.userid=?";
				param.add(loginId);
			}else if("day".equals(consignForm.getQueryType())){
				hql += "";
			}else if("week".equals(consignForm.getQueryType())){
				hql += "";
			}else if("month".equals(consignForm.getQueryType())){
				hql += "";
			}else if("more".equals(consignForm.getQueryType())){
				hql += "";
			}else{
				hql += " and (bean.congignuserid=? or bean.userid=?)";
				param.add(loginId);
				param.add(loginId);
			}
		}
		hql += " order by bean.createtime desc";
		return list(hql, param, consignForm.getPageNo(), consignForm.getPageSize(), true);
	}
	
	/**
	 * 更新过期委托
	 * @param consignForm
	 * @return
	 */
	public Result updateExpireConsign(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "update OAWorkConsignation set state=0 where state=1 and substring(convert(varchar,GETDATE(),120),0,11)>endTime";
						Statement state = conn.createStatement();
						state.execute(sql);
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;
	}
	
	/**
	 * 注销委托
	 * @param consignForm
	 * @return
	 */
	public Result cancelConsign(final String[] keyIds){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String strKeyIds = "";
						for(String str : keyIds){
							strKeyIds += "'"+str+"',";
						}
						if(strKeyIds.endsWith(",")){
							strKeyIds = strKeyIds.substring(0, strKeyIds.length()-1);
						}
						String sql = "update OAWorkConsignation set state=-1 where state=1 and id in ("+strKeyIds+")";
						Statement state = conn.createStatement();
						state.execute(sql);
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;
	}
	
	/**
	 * 查询工作流类别
	 * @return
	 */
	public Result queryFlowClass(){
		
		ArrayList<String> param = new ArrayList<String>();
		String sql = "select flowType.classCode,lan.zh_CN from tblWorkFlowType flowType,tblLanguage lan "
				   + " where flowType.ModuleName=lan.id order by flowType.classCode " ;
		return sqlList(sql, param);
	}
	
	/**
	 * 查询工作流类型
	 * @return
	 */
	public Result queryFlowType(LoginBean login){
		
		ArrayList<String> param = new ArrayList<String>();
		String sql = "select isNull(lan.zh_CN,template.templateName) as name,template.sameFlow as keyId,template.sameFlow as sameFlow,template.templateClass as tclass,ROW_NUMBER() over(order by template.createTime desc) as row_id " 
				   + " from OAWorkFlowTemplate template left join tblLanguage lan on template.templateName=lan.id " 
				   + " where fileFinish='1' and  statusId=0 and templateStatus=1 " ;
		if(!"1".equals(login.getId())){
			sql += "and (charIndex(',"+login.getId()+",',allowVisitor)>0 or charIndex(',"+login.getDepartCode()+",',allowVisitor)>0 " ;
			String departCode=login.getDepartCode();
			while(departCode.length()>5){
				departCode=departCode.substring(0,departCode.length()-5);
				sql+=" or charIndex(',"+departCode+",',allowVisitor)>0";
			}
			if(login.getGroupId().length()>0){
				String []groups=login.getGroupId().split(";");
				for(int i=0;i<groups.length;i++){
					sql+=" or charIndex(',"+groups[i]+",',allowVisitor)>0";
				}
			}
			sql += " or charIndex(template.templateClass,'00002',0)=0)";
		}
		return sqlListMap(sql, param, 1, 10000);
	}

}
