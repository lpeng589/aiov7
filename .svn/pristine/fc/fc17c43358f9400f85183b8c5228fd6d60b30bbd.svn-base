package com.koron.oa.publicMsg.newadvice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.Employee;
import com.koron.oa.bean.OAAdviceBean;
import com.koron.oa.bean.OANewsInfoReplyBean;
import com.koron.oa.util.EmployeeMgt;

import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;

/**
 * 
 * <p>
 * Title:通知通告数据库操作类
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2012-6-5
 * @Copyright: 科荣软件
 * @Author 李文祥
 */
public class OAAdviceMgt extends AIODBManager {
	
	EmployeeMgt EmpMgt=new EmployeeMgt();
	

	/**
	 * 新增通知通告
	 * 
	 * @param oanews
	 * @return
	 */

	
	public Result addAdvice(final OAAdviceBean oaAdvice) {
		return addBean(oaAdvice);
	}

	/**
	 * 删除通知通告
	 * 
	 * @return
	 */
	public Result deleteAdvice(String[] keyIds) {
		return deleteBean(keyIds, OAAdviceBean.class, "id");
	}

	/**
	 * 修改通知通告
	 * 
	 * @param keyids
	 * @return
	 */
	public Result updateAdvice(final OAAdviceBean oaAdvice) {
		return updateBean(oaAdvice);
	}

	/**
	 * 根据通知通告ID查询通知通告
	 * 
	 * @param oanews
	 * @return
	 */
	public Result loadAdvice(final String adviceId) {
		return loadBean(adviceId, OAAdviceBean.class);
	}

	
	public Result queryAdvice(final OAAdviceSearchForm form,final String createTime ,final String userId,final String groups,final String depts,final String querytype){
		//创建参数
		List<String> param = new ArrayList<String>();
		String type=form.getSelectType(); //按通知通告类型查询
		String hql = "from OAAdviceBean bean where 1=1 ";
		if("detailPre".equals(querytype)){ //详细页面上一条
			 hql +=" and bean.lastupDateTime>? ";
			 param.add(createTime);
		}
		else if("detailNext".equals(querytype)){ //详细页面上一条
			 hql +=" and bean.lastupDateTime<? ";
			 param.add(createTime);
		}
		if("type".equals(type)){
			hql += "and bean.adviceType = ? ";
			param.add(form.getSelectId());
		}
		if("time".equals(type)){  //按关键检索查询
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
		if("gaoji".equals(type)){   //多条件查询
			if (form.getAdviceType() != null && form.getAdviceType().length() > 0) {
				hql += " and bean.adviceType=? ";
				param.add(form.getAdviceType());
			}
			if (form.getAdviceTitle() != null && form.getAdviceTitle().length() > 0) {
				hql += " and bean.adviceTitle like ? ";
				param.add("%" + form.getAdviceTitle() + "%");
			}
			if (form.getCreateBy() != null
					&& form.getCreateBy().trim().length() > 0) {
				hql += " and bean.pulisher=? ";
				param.add(form.getCreateBy());
			}
			if( !"".equals(form.getBeginTime())  && !"".equals(form.getBeginTime()) ){
				if (form.getBeginTime().trim().length() > 0) {
					hql += " and bean.pulishDate > ?";
					param.add(form.getBeginTime());
				}
				if (form.getEndTime().trim().length() > 0) {
					hql += " and bean.pulishDate < ?";
					param.add(form.getEndTime()+ " 23:59:59");
				}
			
		    }  
			
		}
		
		if("keyword".equals(type)){ //关键字查询
			if(form.getKeyWord()!=null && form.getKeyWord().length()>0){			 
				hql +=" and( bean.adviceTitle like ? or bean.adviceContext like ? ) ";
				param.add("%" + form.getKeyWord() + "%");
				param.add("%" + form.getKeyWord() + "%");
			}
		}
	
		
		if (!"1".equals(userId)) {
			hql += " and ((bean.createBy =?) or ( bean.statusId=0 and ( bean.isAlonePopedmon='0' or  bean.accepter like ? ";
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
		
		hql += " order by bean.statusId desc ,bean.lastupDateTime desc";
		BaseEnv.log.debug("OAAdviceMgt.query sql="+hql);
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
	 * 查询通知通告的Id查询评论
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
	 * 删除评论
	 * 
	 * @return
	 */
	public Result deleteReply(String keyId) {
		return deleteBean(keyId, OANewsInfoReplyBean.class, "id");
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
	public String getPopedomUser(OAAdviceBean oaAdvice,String userId) {
		String popedomUserIds = ""; // 通知对象
		if ("0".equals(oaAdvice.getIsAlonePopedmon())) { // 判断是否单独授权
			// 设置通知对象为所有人
			List listEmp = (List) EmpMgt.sel_allEmployee().getRetVal();
			for (int i = 0; i < listEmp.size(); i++) { // 循环发送
				popedomUserIds += String.valueOf(listEmp.get(i)) + ",";
			}
		} else {
			// 选择个人
			if (oaAdvice.getAccepter() != null
					&& oaAdvice.getAccepter().length() > 0) {
				popedomUserIds += oaAdvice.getAccepter();
			}

			// 选择部门
			if (null != oaAdvice.getPopedomDeptIds()
					&& !"".equals(oaAdvice.getPopedomDeptIds())) {
				String[] deptIds = oaAdvice.getPopedomDeptIds().split(","); // 根据部门编号查找部门人员
				for (String departId : deptIds) {
					List<Employee> list_emp = EmpMgt
							.queryAllEmployeeByClassCode(departId);
					for (Employee emp : list_emp) {
						if (!popedomUserIds.contains(emp.getid())) {// 判断是否已经单独授权
							popedomUserIds += emp.getid() + ",";
						}
					}
				}
			}
			// 选择职员分组
			if (null != oaAdvice.getPopedomEmpGroupIds() && !"".equals(oaAdvice.getPopedomEmpGroupIds())) {
				String[] empGroupIds = oaAdvice.getPopedomEmpGroupIds()
						.split(","); // 根据分组查找分组人员
				for (String empGroup : empGroupIds) {
					List list = EmpMgt
							.queryAllEmployeeByGroup(empGroup);
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
							String sql = "UPDATE OAAdviceInfo SET statusId =0,PulishDate=? WHERE id=?";
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, releaseTime);
							pss.setString(2, newsId);
							int  rss = pss.executeUpdate();
							
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.
                                    DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OAAdviceMgt  etailSave : ", ex) ;
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
