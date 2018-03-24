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
 * Title:֪ͨͨ�����ݿ������
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2012-6-5
 * @Copyright: �������
 * @Author ������
 */
public class OAAdviceMgt extends AIODBManager {
	
	EmployeeMgt EmpMgt=new EmployeeMgt();
	

	/**
	 * ����֪ͨͨ��
	 * 
	 * @param oanews
	 * @return
	 */

	
	public Result addAdvice(final OAAdviceBean oaAdvice) {
		return addBean(oaAdvice);
	}

	/**
	 * ɾ��֪ͨͨ��
	 * 
	 * @return
	 */
	public Result deleteAdvice(String[] keyIds) {
		return deleteBean(keyIds, OAAdviceBean.class, "id");
	}

	/**
	 * �޸�֪ͨͨ��
	 * 
	 * @param keyids
	 * @return
	 */
	public Result updateAdvice(final OAAdviceBean oaAdvice) {
		return updateBean(oaAdvice);
	}

	/**
	 * ����֪ͨͨ��ID��ѯ֪ͨͨ��
	 * 
	 * @param oanews
	 * @return
	 */
	public Result loadAdvice(final String adviceId) {
		return loadBean(adviceId, OAAdviceBean.class);
	}

	
	public Result queryAdvice(final OAAdviceSearchForm form,final String createTime ,final String userId,final String groups,final String depts,final String querytype){
		//��������
		List<String> param = new ArrayList<String>();
		String type=form.getSelectType(); //��֪ͨͨ�����Ͳ�ѯ
		String hql = "from OAAdviceBean bean where 1=1 ";
		if("detailPre".equals(querytype)){ //��ϸҳ����һ��
			 hql +=" and bean.lastupDateTime>? ";
			 param.add(createTime);
		}
		else if("detailNext".equals(querytype)){ //��ϸҳ����һ��
			 hql +=" and bean.lastupDateTime<? ";
			 param.add(createTime);
		}
		if("type".equals(type)){
			hql += "and bean.adviceType = ? ";
			param.add(form.getSelectId());
		}
		if("time".equals(type)){  //���ؼ�������ѯ
			int timeId=Integer.parseInt(form.getSelectId());
			switch (timeId) {
			case 1: // һ������
				hql += " and DateDiff(day,bean.createTime,getdate())=0 ";
				break;
			case 2: // һ������
				hql += " and DateDiff(day,bean.createTime,getdate())<=6 ";
				break;
			case 3: // һ��������
				hql += " and DateDiff(day,bean.createTime,getdate())<=30 ";
				break;
			case 4: // ����������
				hql += " and DateDiff(month,bean.createTime,getdate())<=3";
				break;
			case 5: // ����������
				hql += " and DateDiff(month,bean.createTime,getdate())>3 ";
				break;
			}
			
		}
		if("gaoji".equals(type)){   //��������ѯ
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
		
		if("keyword".equals(type)){ //�ؼ��ֲ�ѯ
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
	 * ��������
	 * 
	 * @param forumBean
	 * @return
	 */
	public Result replyNews(OANewsInfoReplyBean replyBean) {
		Result result = addBean(replyBean);
		return result;
	}

	/**
	 * ��ѯ֪ͨͨ���Id��ѯ����
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
	 * ɾ������
	 * 
	 * @return
	 */
	public Result deleteReply(String keyId) {
		return deleteBean(keyId, OANewsInfoReplyBean.class, "id");
	}

	/**
	 * �������
	 * 
	 * @param forumBean
	 * @return
	 */
	public Result addreply(OANewsInfoReplyBean replyBean) {
		return addBean(replyBean);

	}


	/**
	 * ��ȡ֪ͨ����
	 */
	public String getPopedomUser(OAAdviceBean oaAdvice,String userId) {
		String popedomUserIds = ""; // ֪ͨ����
		if ("0".equals(oaAdvice.getIsAlonePopedmon())) { // �ж��Ƿ񵥶���Ȩ
			// ����֪ͨ����Ϊ������
			List listEmp = (List) EmpMgt.sel_allEmployee().getRetVal();
			for (int i = 0; i < listEmp.size(); i++) { // ѭ������
				popedomUserIds += String.valueOf(listEmp.get(i)) + ",";
			}
		} else {
			// ѡ�����
			if (oaAdvice.getAccepter() != null
					&& oaAdvice.getAccepter().length() > 0) {
				popedomUserIds += oaAdvice.getAccepter();
			}

			// ѡ����
			if (null != oaAdvice.getPopedomDeptIds()
					&& !"".equals(oaAdvice.getPopedomDeptIds())) {
				String[] deptIds = oaAdvice.getPopedomDeptIds().split(","); // ���ݲ��ű�Ų��Ҳ�����Ա
				for (String departId : deptIds) {
					List<Employee> list_emp = EmpMgt
							.queryAllEmployeeByClassCode(departId);
					for (Employee emp : list_emp) {
						if (!popedomUserIds.contains(emp.getid())) {// �ж��Ƿ��Ѿ�������Ȩ
							popedomUserIds += emp.getid() + ",";
						}
					}
				}
			}
			// ѡ��ְԱ����
			if (null != oaAdvice.getPopedomEmpGroupIds() && !"".equals(oaAdvice.getPopedomEmpGroupIds())) {
				String[] empGroupIds = oaAdvice.getPopedomEmpGroupIds()
						.split(","); // ���ݷ�����ҷ�����Ա
				for (String empGroup : empGroupIds) {
					List list = EmpMgt
							.queryAllEmployeeByGroup(empGroup);
					for (int i = 0; i < list.size(); i++) {
						if (!popedomUserIds.contains(list.get(i).toString())) {// �ж��Ƿ��Ѿ�������Ȩ
							popedomUserIds += list.get(i).toString() + ",";
						}
					}
				}
			}
		
		}
		String popeUser="";
		//�ж�֪ͨ�����Ƿ������ǰ�û�������Ҫ���Լ�����֪ͨ��Ϣ
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
	 * ����ҳ�����
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
