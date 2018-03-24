package com.koron.oa.publicMsg.newsInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;

import com.koron.oa.bean.Employee;
import com.koron.oa.bean.OANewsBean;
import com.koron.oa.bean.OANewsInfoReplyBean;
import com.koron.oa.util.EmployeeMgt;
import com.menyi.aio.bean.EmployeeBean;

import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
/**
 * 
 * <p>
 * Title:新闻中心数据库操作类
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2012-6-5
 * @Copyright: 科荣软件
 * @Author 李文祥
 */
public class OANewsMgt extends AIODBManager {
	
	EmployeeMgt EmpMgt=new EmployeeMgt();

	/**
	 * 新增新闻
	 * 
	 * @param oanews
	 * @return
	 */
	public Result addNews(OANewsBean oaNews) {
		return addBean(oaNews);
	}

	/**
	 * 删除新闻
	 * 
	 * @return
	 */
	public Result deleteNews(String[] keyIds) {
		return deleteBean(keyIds, OANewsBean.class, "id");
	}
	
	/**
	 * 删除新闻
	 * 
	 * @return
	 */
	public Result deleteNews(String keyId) {
		return deleteBean(keyId, OANewsBean.class, "id");
	}

	/**
	 * 修改新闻
	 * 
	 * @param keyids
	 * @return
	 */
	public Result updateNews(final OANewsBean oaNews) {
		return updateBean(oaNews);
	}

	/**
	 * 根据新闻ID查询新闻
	 * 
	 * @param oanews
	 * @return
	 */
	public Result loadNews(final String newsId) {
		return loadBean(newsId, OANewsBean.class);
	}

	/**
	 * 查询新闻(包含按新闻类型、关键检索、关键字、多条件查询、查询上一条、查询下一条)
	 * @param form
	 * @param userId
	 * @return
	 */
	public Result queryNews(final OANewsSearchForm form,final String createTime,final String userId,final String groups,final String depts,final String querytype){
		//创建参数
		List<String> param = new ArrayList<String>();
		String type=form.getSelectType();
		String hql="from OANewsBean bean where 1=1";
		if("detailPre".equals(querytype)){ //详细页面上一条
			 hql +=" and bean.lastupDateTime>? ";
			 param.add(createTime);
		}
		else if("detailNext".equals(querytype)){ //详细页面上一条
			 hql +=" and bean.lastupDateTime<? ";
			 param.add(createTime);
		}
		
		if("type".equals(type)){   //按新闻类型查询
			hql += " and bean.newsType = ? ";
			param.add(form.getSelectId());
		}
		if("time".equals(type)){   //按关键检索查询
			int timeId=Integer.parseInt(form.getSelectId());
			switch (timeId) {
			case 1: // 一天以内
				hql += " and DateDiff(day,bean.createTime,getdate())=0 ";
				break;
			case 2: // 一周以内
				hql += " and DateDiff(day,bean.createTime,getdate())<=6 ";
				break;
			case 3: // 一个月以内
				hql += " and DateDiff(day,bean.createTime,getdate())<=30 ";
				break;
			case 4: // 三个月以内
				hql += " and DateDiff(month,bean.createTime,getdate())<=3";
				break;
			case 5: // 三个月以外
				hql += " and DateDiff(month,bean.createTime,getdate())>3 ";
				break;
			}			
		}
		if("gaoji".equals(type)){  //多条件查询
			if (form.getNewsType() != null && form.getNewsType().length() > 0) {
				hql += " and bean.newsType=? ";
				param.add(form.getNewsType());
			}
			if (form.getNewsTitle() != null && form.getNewsTitle().length() > 0) {
				hql += " and bean.newsTitle like ? ";
				param.add("%" + form.getNewsTitle() + "%");
			}
			if (form.getCreateBy() != null
					&& form.getCreateBy().trim().length() > 0) {
				hql += " and bean.createBy=? ";
				param.add(form.getCreateBy());
			}
		
			if(!"".equals(form.getBeginTime())  && !"".equals(form.getBeginTime()) ){				
				if (form.getBeginTime().trim().length() > 0) {
					hql += " and bean.releaseTime >?";
					param.add(form.getBeginTime());
				}
				if (form.getEndTime().trim().length() > 0) {
					hql += " and bean.releaseTime < ?";
					param.add(form.getEndTime()+ " 23:59:59");
				}		
		   }  
			
		}
		if("keyword".equals(type)){ //关键字查询
			
			if(form.getKeyWord()!=null && form.getKeyWord().length()>0){			 
				
				hql +=" and( bean.newsTitle like '%'+?+'%' or bean.newsContext like '%'+?+'%') ";
				param.add(form.getKeyWord());
				param.add(form.getKeyWord());
			}
		}
		
		if (!"1".equals(userId)) {
			hql += " and ((bean.createBy =?) or ( bean.statusId=0 and ( bean.isAlonePopedmon='0' or  bean.popedomUserIds like ?  ";
			param.add(userId);
			param.add("%" +userId+ "%");
			
			if (depts.length()>=5){
				for(int i=0;i<depts.length()/5;i++){
					String d=depts.substring(0,depts.length()-5*i);
					hql += " or bean.popedomDeptIds like ? ";
					param.add("%"+ d +",%");
				}
			}	
			
			if(groups.length()>25){
				 String[] grs=groups.split(";");
			        for(String g : grs){
			        	hql +=" or bean.popedomEmpGroupIds like ?";
			        	param.add("%" +g+ ",%");
			        }			        	
			}
			hql +=" )))";
		}
		
		hql += " order by bean.statusId desc , bean.lastupDateTime desc";
		return list(hql, param, form.getPageNo(), form.getPageSize(), true);
	}
	
	
	/**
	 * 新增评论
	 * 
	 * @param forumBean
	 * @return
	 */
	public Result replyNews(OANewsInfoReplyBean replyBean) {
		Result result = addBean(replyBean);
		return result;
	}

	/**
	 * 查询新闻的Id查询评论
	 * 
	 * @param newsId
	 */
	public Result queryReplys(String newsId, int pageNo, int pageSize) {
		List<String> param = new ArrayList<String>();
		String hql = "select bean from OANewsInfoReplyBean bean where bean.newsId=? ";
		param.add(newsId);
		hql += " order by bean.createTime desc";
		return list(hql, param, pageNo, pageSize, true);
	}
	
	/**
	 * 获取所有评论的图片
	 * @param newsId
	 * @return
	 */
	public Result queryAllPhoto (final String newsId){
		final List<EmployeeBean> lis = new ArrayList();	
		final HashMap<String, EmployeeBean> hamap = new HashMap<String, EmployeeBean>();
		final Result result = new Result();
 		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select id,EmpFullName,photo from tblEmployee where id in (select createBy from OANewsInfoReply where newsId=" + "'" + newsId + "'" + ")";
							PreparedStatement pss = conn.prepareStatement(sql) ;
							ResultSet rss = pss.executeQuery() ;
							EmployeeBean empBean = null;
							while(rss.next()){
								empBean = new EmployeeBean();
								empBean.setId(rss.getString("id"));
								empBean.setEmpFullName(rss.getString("EmpFullName"));
								String str = rss.getString("photo");
								if (str != null && str != "" && str.indexOf(":") > 0) {
									str = str.substring(0, str.indexOf(":"));
								}
								if (str==null){
									str="";
								}
								empBean.setPhoto(str);
								hamap.put(empBean.getId(), empBean);
							}
							result.setRetVal(hamap);
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.
                                    DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OANewsMgt queryAllPhoto : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
				}
			});
			result.retCode = retCode;
			return result ;
		}
	
	/**
	 * 删除评论
	 * 
	 * @return
	 */
	public Result deleteReply(String keyId) {
		return deleteBean(keyId, OANewsInfoReplyBean.class, "id");
	}
    /**
     * 批量删除评论
     */
	public Result deleteReplys(String[] keyIds){	
		return deleteBean(keyIds, OANewsInfoReplyBean.class, "id");
	}
	/**
	 * 添加评论
	 * 
	 * @param forumBean
	 * @return
	 */
	public Result addreply(OANewsInfoReplyBean replyBean) {
		return addBean(replyBean);

	}
	
	/**
	 * 获取通知对象
	 */
	public String getPopedomUser(OANewsBean oaNews,String userId) {
		String popedomUserIds = ""; // 通知对象
		String popedomUserId = oaNews.getPopedomUserIds();
		if ("0".equals(oaNews.getIsAlonePopedmon())) { // 判断是否单独授权
			// 设置通知对象为所有人
			List listEmp = (List) EmpMgt.sel_allEmployee().getRetVal();
			for (int i = 0; i < listEmp.size() ; i++) { // 循环发送		
					popedomUserIds += String.valueOf(listEmp.get(i)) + ",";
			}
		} else {
			// 选择个人
			if (oaNews.getPopedomUserIds() != null && oaNews.getPopedomUserIds().length() > 0) {
				String [] popeUser=popedomUserId.split(",");
				for(int i=0;i<popeUser.length;i++)			
					popedomUserIds += popeUser[i]+",";
			}

			// 选择部门
			if (null != oaNews.getPopedomDeptIds()
					&& !"".equals(oaNews.getPopedomDeptIds())) {
				String[] deptIds = oaNews.getPopedomDeptIds().split(","); // 根据部门编号查找部门人员
				for (String departId : deptIds) {
					List<Employee> list_emp = EmpMgt.queryAllEmployeeByClassCode(departId);
					for (Employee emp : list_emp) {
						if (!popedomUserId.contains(emp.getid())) {// 判断是否已经单独授权
								popedomUserIds += emp.getid() + ",";
						}
					}
				}
			}
			// 选择职员分组
			if (null != oaNews.getPopedomEmpGroupIds() && !"".equals(oaNews.getPopedomEmpGroupIds())) {
				String[] empGroupIds = oaNews.getPopedomEmpGroupIds()
						.split(","); // 根据分组查找分组人员
				for (String empGroup : empGroupIds) {
					List list = EmpMgt.queryAllEmployeeByGroup(empGroup);
					for (int i = 0; i < list.size(); i++) {
						if (!popedomUserIds.contains(list.get(i).toString())) {// 判断是否已经单独授权
							popedomUserIds += list.get(i).toString() + ",";
						}
					}
				}
			}
		}
		String popeUser="";
		//判断通知对象是否包含当前用户，不需要向自己发送通知消息
		if(popedomUserIds!=null && popedomUserIds.length()>0){
			String [] popedomUser=popedomUserIds.split(",");
			for(String pope:popedomUser){
				if(!pope.equals(userId))
					popeUser +=pope+",";
			}
			
		}
		return popeUser;
	}
	/**
	 * 获取通知用户
	 * @param oanewsBean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EmployeeBean> getEmployee(String popedomUserId){
	    ArrayList<String> param = new ArrayList<String>() ;
        popedomUserId=","+popedomUserId;
		String hql = "select bean from EmployeeBean  bean where ? like '%,' + bean.id+',%'";
		param.add(popedomUserId) ;
		Result result = list(hql, param);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			return (List<EmployeeBean>) result.getRetVal();
		}else{
			return null ;
		}
	}
	/**
	 * 获取通知部门
	 * @param oanewsBean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getDepartment(String popedomDeptId){
		ArrayList<String> param = new ArrayList<String>();
	 	popedomDeptId=","+popedomDeptId;
		String hql = "select * from tblDepartment where ? like '%,'+ classCode +',%'"; 
		param.add(popedomDeptId);
		Result result = sqlList(hql, param);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			return (List<Object>) result.getRetVal();
		}else{
			return null ;
		}
	}
	/**
	 * 获取职员分组
	 * @param oanewsBean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getEmpGroup(String popedomEmpGroupId){
		ArrayList<String> param = new ArrayList<String>() ;
		popedomEmpGroupId+=","+popedomEmpGroupId;
		String hql = "select * from tblEmpGroup where  ? like '%,'+ id +',%'";
		param.add(popedomEmpGroupId) ;
		Result result = sqlList(hql, param);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			return (List<Object>) result.getRetVal() ;
		}else{
			return null ;
		}
	}
	
	/**
	 * 详情页面添加
	 * @param newsId
	 * @return 
	 */
	public Result detailSave(final String newsId,final String releaseTime){
		final Result result = new Result();
 		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "UPDATE OACompanyNewsInfo SET statusId =0,ReleaseTime=? WHERE id=?";
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, releaseTime);
							pss.setString(2, newsId);
							int  rss = pss.executeUpdate();
							
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.
                                    DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OANewsMgt queryAllPhoto : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
				}
			});
			result.retCode = retCode;
			return result ;
		}
}
