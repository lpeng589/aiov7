package com.menyi.aio.web.advice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import bsh.EvalError;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.JsonObject;
import com.koron.oa.bean.MessageBean;
import com.menyi.aio.bean.AdviceBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.msgcenter.msgif.CancelMsgReq;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.AppleApnsSend;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.PublicMgt;
import com.menyi.web.util.SystemState;

/**
 * <p>Title: </p>
 *
 * <p>Description: 单位管理的接口类</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class AdviceMgt extends DBManager {
	//
	//	protected Result add(AdviceForm myForm) {
	//    	Result rs=null;
	//    	if(myForm.getReceive().length()==0){
	//    		AdviceBean bean = new AdviceBean();
	//	        bean.setId(myForm.getId());
	//	        bean.setSend(myForm.getSend());
	//	        bean.setTitle(myForm.getTitle());
	//	        bean.setReceive(myForm.getReceive());
	//	        bean.setContent(myForm.getContent());
	//	        bean.setRelatMsgId(myForm.getRelatMsgId());
	//	        bean.setReceiveName(myForm.getReceiveName());
	//	        bean.setSendName(myForm.getSendName());
	//	        bean.setCreateBy(myForm.getSend());
	//	        bean.setLastUpdateBy(myForm.getSend());
	//	        bean.setCreateTime(BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss));
	//	        bean.setLastUpdateTime(BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss));
	//	        bean.setStatus("noRead");
	//	        bean.setExist("all");
	//	        bean.setType(myForm.getType()) ;
	//	        bean.setRelationId(myForm.getRelationId()) ;
	//	        if(!this.isExists(bean.getReceive(), bean.getTitle(), bean.getContent())){
	//	        	rs=addBean(bean);
	//	        }
	//	        
	//    	}else{
	//    		String receive[]=myForm.getReceive().split(";");
	//    		String receiveName[]=myForm.getReceiveName().split(";");
	//    		for(int i=0;i<receive.length;i++){
	//    			AdviceBean bean = new AdviceBean();
	//    			String id = IDGenerater.getId();
	//    		    myForm.setId(id);
	//    	        bean.setId(myForm.getId());
	//    	        bean.setSend(myForm.getSend());
	//    	        bean.setTitle(myForm.getTitle());
	//    	        bean.setReceive(receive[i]);
	//    	        bean.setContent(myForm.getContent());
	//    	        bean.setReceiveName(receiveName[i]);
	//    	        bean.setSendName(myForm.getSendName());
	//    	        bean.setRelatMsgId(myForm.getRelatMsgId());
	//    	        bean.setCreateBy(myForm.getSend());
	//    	        bean.setLastUpdateBy(myForm.getSend());
	//    	        bean.setCreateTime(BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss));
	//    	        bean.setLastUpdateTime(BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss));
	//    	        bean.setStatus("noRead");
	//    	        bean.setExist("all");
	//    	        bean.setType(myForm.getType()) ;
	//    	        bean.setRelationId(myForm.getRelationId()) ;
	//    	        if(!this.isExists(bean.getReceive(), bean.getTitle(), bean.getContent())){
	//    	        	rs=addBean(bean);    	        
	//	    	        if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
	//	    	        	return rs;
	//	    	        }
	//    	        }
	//    	        // 给客户端用户发送通知消息
	//				MSGConnectCenter.sendAdvice(bean.getReceive(),bean.getId(),bean.getTitle());
	//    		}
	//    	}
	//        return rs;
	//    }

	/**
	 * 查询通知表中是否已经存在相同的接受人，相同的内容未读
	 * @param receive
	 * @param content
	 * @return
	 */
	public boolean isExists(String receive, String title, String content, Connection conn) throws Exception {
		List param = new ArrayList();
		String hql = "select id from tblAdvice as bean where 1=1 ";
		hql += " and bean.Receive=?";

		hql += " and bean.Title=?";

		hql += " and bean.Content=?";

		hql += " and bean.Status='noRead'";

		PreparedStatement pstmt = conn.prepareStatement(hql);
		pstmt.setString(1, receive);
		pstmt.setString(2, title);
		pstmt.setString(3, content);

		ResultSet rset = pstmt.executeQuery();
		if (rset.next()) {
			return true;
		} else {
			return false;
		}
	}

	//
	//    /**
	//     * 修改一条单位记录
	//     * @param id long
	//     * @param name String
	//     * @return Result
	//     */
	//    protected Result update(String id,
	//                         String status,
	//                         String exist
	//                        ) {
	//        //先查出相应的单位记录
	//        Result rs = loadBean(id, AdviceBean.class);
	//        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
	//        	AdviceBean bean = (AdviceBean) rs.retVal;
	//            if(status.length()!=0){
	//              bean.setStatus(status);
	//
	//              if ("read".equals(status)){
	//            	  MSGConnectCenter.CancelMsg(CancelMsgReq.TYPE_SYS, bean.getReceive(), bean.getId());
	//              }
	//            }
	//            if(exist.length()!=0){
	//              bean.setExist(exist);
	//            }
	//            //修改记录
	//            rs = updateBean(bean);
	//        }
	//        return rs;
	//    }

	/**
	 * 查询单位记录
	 * @param name String
	 * @param pageNo int
	 * @param pageSize int
	 * @return Result
	 */
	public Result query(String loginId, String content, String type, String startDate, String endDate, String status, int pageNo, int pageSize, String title) {

		//创建参数
		List param = new ArrayList();
		String hql = "select bean from AdviceBean as bean where 1=1 ";

		hql += " and   bean.Receive=? ";
		param.add(loginId);

		if (content != null && content.length() != 0) {
			hql += " and bean.Content like '%'||?||'%'";
			param.add(content.trim());
		}

		if (title != null && title.length() != 0) {
			hql += " and bean.Title like '%'|| ? ||'%' ";
			param.add(title.trim());
		}

		if (startDate != null && startDate.length() != 0) {
			hql += " and bean.createTime>=?";
			param.add(startDate.trim() + " 00:00:00");
		}
		if (endDate != null && endDate.length() != 0) {
			hql += " and bean.createTime<=?";
			param.add(endDate.trim() + " 23:59:59");
		}

		if (status != null && status.length() != 0) {
			hql += " and bean.Status=?";
			param.add(status);
		}

		if (type != null && type.length() != 0) {
			hql += " and bean.type=? ";
			param.add(type);
		}
		hql = hql + "order by bean.Status,bean.createTime desc";

		//调用list返回结果
		return list(hql, param, pageNo, pageSize, true);
	}

	public Result queryByCondition(final String createId, final String msgType, final String title, final String content) {

		final Result rs = new Result();

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							Statement st = conn.createStatement();
							String hql = "select * from OAMessage as bean ";
							//如标题不为空，则做标题模糊查询
							hql += " where bean.Send='" + createId + "'";
							if (msgType != null && msgType.length() != 0) {
								hql += " and bean.OperType='" + msgType + "'";
							}
							if (title != null && title.length() != 0) {
								hql += " and bean.Title like '%" + title + "%' ";
							}
							if (content != null && content.length() != 0) {
								hql += " and bean.Content like '%" + content + "%' ";
							}

							hql = hql + "order by bean.createTime desc";
							ResultSet rss = st.executeQuery(hql);
							ArrayList<MessageBean> listMes = new ArrayList<MessageBean>();
							while (rss.next()) {
								MessageBean m = new MessageBean();
								m.setId(rss.getString("id"));
								m.setContent(rss.getString("Content"));
								m.setCreateBy(rss.getString("createBy"));
								m.setCreateTime(rss.getString("createTime"));
								m.setExist(rss.getString("exist"));
								m.setLastUpdateBy(rss.getString("lastUpdateBy"));
								m.setLastUpdateTime(rss.getString("lastUpdateTime"));
								m.setOperType(rss.getString("OperType"));
								m.setSend(rss.getString("Send"));
								m.setStatus(rss.getString("Status"));
								listMes.add(m);
							}
							rs.setRetVal(listMes);
							rs.setRealTotal(listMes.size());
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("queryByCondition Error ", ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		return rs;
	}

	/**
	 * 查一条单位的详细信息
	 * @param notepadId long 代号
	 * @return Result 返回结果
	 */
	public Result detail(String id) {
		return loadBean(id, AdviceBean.class);
	}

	/**
	 * 查一条单位的详细信息
	 * @param notepadId long 代号
	 * @return Result 返回结果
	 */
	public Result EmpleoyeeDetail(String id) {
		return loadBean(id, EmployeeBean.class);
	}

	public Result getEmployee(String id) {
		return loadBean(id, EmployeeBean.class);
	}

	/**
	 * 得到登陆者未查看过的消息
	 * @param loginId String
	 * @return Result
	 */
	public Result getCurrLoginMsgCount(String loginId) {

		String sql = "select count(bean)  from AdviceBean bean where bean.Receive=? and bean.Status=? ";
		ArrayList param = new ArrayList();
		param.add(loginId);
		param.add("noRead");
		Result rs = this.list(sql, param);
		return rs;
	}

	/**
	 * 最新7条消息
	 * @param loginId String
	 * @return Result
	 */
	public Result getAdvices(String loginId) {
		String sql = "select bean  from AdviceBean bean where bean.Receive=? and bean.Status=? order by bean.createTime desc";
		ArrayList param = new ArrayList();
		param.add(loginId);
		param.add("noRead");
		Result rs = this.list(sql, param, 1, 7, true);
		return rs;
	}

	/**
	 * 根据关联表的id查询该主键 mj
	 */
	public Result getBeanByReId(String relationId) {

		String sql = "select bean  from AdviceBean bean where bean.relationId = ?";
		ArrayList param = new ArrayList();
		param.add(relationId);
		Result rs = this.list(sql, param);
		return rs;

	}

	/**
	 * 修改是否立即发布（针对 新闻中心和通知通告而书写的方法）
	 * @param ids
	 * @return
	 */
	public Result updateIsUsedById(final String id, final String exist) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer ids = new StringBuffer("");
							int i = 0;
							Statement st = conn.createStatement();
							String strsql = "update tblAdvice set exist = '" + exist + "' where id = '" + id + "'";
							st.executeUpdate(strsql);
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("UpdateIsUsedById Error ", ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 得到登陆者未查看过的消息
	 * @param loginId String
	 * @return Result
	 */
	public Result getCurrLoginMsg(String loginId) {
		String sql = "select bean.id,bean.Title,bean.createTime,bean.Content  from AdviceBean bean where bean.Receive=? and bean.Status=? order by bean.createTime desc";
		ArrayList param = new ArrayList();
		param.add(loginId);
		param.add("noRead");
		Result rs = this.list(sql, param);
		return rs;
	}

	/**
	 * 根据通知消息的相关ID字段查询出相应的通知消息
	 * @param Id
	 * @return
	 */
	public Result queryallMessage(final String Id) {
		ArrayList paramList = new ArrayList();
		String hql = "select bean from AdviceBean bean where ? like '%,' + bean.relationId+',%'";
		paramList.add(Id);
		return list(hql, paramList);
	}

	public Result querySingMsg(final String realtionId, final String receiveId) {

		ArrayList paramList = new ArrayList();
		String hql = "select bean from AdviceBean bean where bean.relationId=?  and  Receive=?";
		paramList.add(realtionId);
		paramList.add(receiveId);
		return list(hql, paramList);
	}

	protected boolean add(AdviceBean bean, HashMap<String, ArrayList<String[]>> tokenMap, Connection conn) {
		try {

			if (!this.isExists(bean.getReceive(), bean.getTitle(), bean.getContent(), conn)) {

				String sql = " INSERT INTO [tblAdvice]([id],[Send],[Title],[Content],[Receive],[relationId],[type],[createTime],[lastUpdateTime],[Status],[createBy])"
						+ " values(?,?,?,?,?,?,?,?,?,?,?);  ";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, bean.getId());
				pstmt.setString(2, bean.getSend());
				pstmt.setString(3, bean.getTitle());
				pstmt.setString(4, bean.getContent());
				pstmt.setString(5, bean.getReceive());
				pstmt.setString(6, bean.getRelationId());
				pstmt.setString(7, bean.getType());
				pstmt.setString(8, bean.getCreateTime());
				pstmt.setString(9, bean.getLastUpdateTime());
				pstmt.setString(10, bean.getStatus());
				pstmt.setString(11, bean.getCreateBy());
				pstmt.execute();

				//通知KK
				MSGConnectCenter.sendAdvice(bean.getReceive(), bean.getId(), bean.getTitle());

				// 推送手机
				JsonObject json = new JsonObject();
				json.addProperty("op", "add");
				json.addProperty("type", "note");
				json.addProperty("userId", bean.getReceive());
				json.addProperty("noteType", bean.getType());
				json.addProperty("id", bean.getId());
				json.addProperty("refid", bean.getRelationId());
				String advice = bean.getTitle();
				advice = advice.replaceAll("RES<oa.mail.msg.newMail>", "您有新的邮件");
				json.addProperty("title", advice);
				json.addProperty("time", bean.getCreateTime());
				advice = bean.getContent();
				advice = advice.replaceAll("RES<oa.mail.msg.newMail>", "您有新的邮件");
				json.addProperty("content", advice);
				new AppleApnsSend(String.valueOf(SystemState.instance.dogId), bean.getReceive(), bean.getTitle(), json.toString(), "").start();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public boolean add(final String userId, final String title, final String content, final String receiveIds, final String relationId, final String type) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							boolean bol = add(userId, title, content, receiveIds, relationId, type, conn);

							rs.setRetCode(bol ? ErrorCanst.DEFAULT_SUCCESS : ErrorCanst.DEFAULT_FAILURE);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("AdviceMgt.add Error", ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return retCode == ErrorCanst.DEFAULT_SUCCESS;
	}

	/**
	 * 这是外部添加通知消息的唯一接口，谁不调，灭了谁，PWY
	 * @param userId 创建者ID
	 * @param title 标题
	 * @param content 内容
	 * @param receiveIds 通知对象
	 * @param relationId 详情ID
	 * @param type 类型
	 * @return
	 */
	public boolean add(String userId, String title, String content, String receiveIds, String relationId, String type, Connection conn) {
		if (null == userId || null == title || null == content || null == receiveIds || null == relationId) {
			return false;
		}
		receiveIds = receiveIds.replaceAll(";", ",").replaceAll("'", "");
		if (type == null || type.length() == 0) {
			type = "other";
		}
		HashMap<String, ArrayList<String[]>> tokenMap = null;
		Result result = new PublicMgt().queryTokenByUserIds(receiveIds,conn);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			tokenMap = (HashMap<String, ArrayList<String[]>>) result.retVal;
		}

		String ids[] = receiveIds.split(",");
		for (int i = 0; i < ids.length; i++) {
			if (ids[i].length() > 0) {
				AdviceBean bean = new AdviceBean();
				bean.setId(IDGenerater.getId());
				bean.setSend(userId);
				bean.setTitle(title);
				bean.setContent(content);
				bean.setReceive(ids[i]);
				bean.setRelationId(relationId);
				bean.setType(type);
				bean.setCreateTime(BaseDateFormat.format(new java.util.Date(), BaseDateFormat.yyyyMMddHHmmss));
				bean.setLastUpdateTime(BaseDateFormat.format(new java.util.Date(), BaseDateFormat.yyyyMMddHHmmss));
				bean.setStatus("noRead");
				bean.setCreateBy(userId);
				this.add(bean, tokenMap, conn);
			}
		}

		return true;
	}

	protected boolean readOver(AdviceBean bean) {
		bean.setStatus("read");
		bean.setLastUpdateTime(BaseDateFormat.format(new java.util.Date(), BaseDateFormat.yyyyMMddHHmmss));
		Result rs = updateBean(bean);
		if (ErrorCanst.DEFAULT_SUCCESS == rs.getRetCode()) {
			// 通知KK
			MSGConnectCenter.cancelMsg(CancelMsgReq.TYPE_SYS, bean.getReceive(), bean.getId());

			//			// 推送手机
			//			HashMap<String, ArrayList<String[]>> tokenMap = null;
			//			Result result = new PublicMgt().queryTokenByUserIds(bean
			//					.getReceive());
			//			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//				tokenMap = (HashMap<String, ArrayList<String[]>>) result.retVal;
			//			}
			//			if (null != tokenMap) {
			//				ArrayList<String[]> tokenList = tokenMap.get(bean.getReceive());
			//				if (null != tokenList) {
			//					for (String[] str : tokenList) {	// 一个人有可能有多个手机
			//
			//						JsonObject json = new JsonObject();
			//						json.addProperty("op", "read");
			//						json.addProperty("type", "note");
			//						json.addProperty("userId", bean.getReceive());
			////						json.addProperty("noteType", bean.getType());
			//						json.addProperty("id", bean.getId());
			////						json.addProperty("refid", bean.getRelationId());
			//						new AppleApnsSend(
			//								String.valueOf(SystemState.instance.dogId), str[0],
			//								bean.getTitle(), json.toString(), str[1]).start();
			//					}
			//					
			//				}
			//			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据详情ID和接收者标为已读
	 * @param userId
	 * @param relationId
	 * @return
	 */
	public boolean readOverByRelationId(String relationIds, String userIds) {
		if (null == relationIds || null == userIds) {
			return false;
		}
		userIds = userIds.replaceAll(";", ",").replaceAll("'", "");
		relationIds = relationIds.replaceAll(";", ",").replaceAll("'", "");
		String[] ids = relationIds.split(",");
		String str = "";
		for (String id : ids) {
			str += "'" + id + "',";
		}
		str = str.substring(0, str.length() - 1);
		String sql = "select bean from AdviceBean bean where bean.relationId in (" + str + ") ";
		ArrayList param = new ArrayList();
		if (null != userIds && userIds.length() > 0) {
			ids = userIds.split(",");
			str = "";
			for (String id : ids) {
				str += "'" + id + "',";
			}
			str = str.substring(0, str.length() - 1);
			sql += " and bean.Receive in (" + str + ") ";
		}
		Result rs = this.list(sql, param);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<AdviceBean> listAdvice = (List<AdviceBean>) rs.getRetVal();
			boolean result = false;
			for (AdviceBean bean : listAdvice) {
				result |= readOver(bean);
			}
			return result;
		} else {
			return false;
		}
	}

	/**
	 * 根据ID标为已读
	 * @param keyIds
	 * @return
	 */
	public boolean readOverById(String keyIds) {
		if (null == keyIds) {
			return false;
		}
		keyIds = keyIds.replaceAll(";", ",").replaceAll("'", "");
		if (null == keyIds) {
			return false;
		}
		String[] ids = keyIds.split(",");
		String str = "";
		for (String id : ids) {
			str += "'" + id + "',";
		}
		str = str.substring(0, str.length() - 1);
		String sql = "select bean from AdviceBean bean where bean.id in (" + str + ") ";
		Result rs = this.list(sql, new ArrayList());
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<AdviceBean> listAdvice = (List<AdviceBean>) rs.getRetVal();
			boolean result = false;
			for (AdviceBean bean : listAdvice) {
				result |= readOver(bean);
				if (bean.getRelationId() != null && !bean.getRelationId().equals("")) {
					readOverByRelationId(bean.getRelationId(), bean.getReceive());
				}
			}
			return result;
		} else {
			return false;
		}
	}

	protected boolean delete(AdviceBean bean) {
		Result rs = deleteBean(bean.getId(), AdviceBean.class, "id");
		if (ErrorCanst.DEFAULT_SUCCESS == rs.getRetCode()) {
			// 通知KK
			MSGConnectCenter.cancelMsg(CancelMsgReq.TYPE_SYS, bean.getReceive(), bean.getId());

			//			// 推送手机
			//			HashMap<String, ArrayList<String[]>> tokenMap = null;
			//			Result result = new PublicMgt().queryTokenByUserIds(bean
			//					.getReceive());
			//			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//				tokenMap = (HashMap<String, ArrayList<String[]>>) result.retVal;
			//			}
			//			if (null != tokenMap) {
			//				ArrayList<String[]> tokenList = tokenMap.get(bean.getReceive());
			//				if (null != tokenList) {
			//					for (String[] str : tokenList) {	// 一个人有可能有多个手机
			//
			//						JsonObject json = new JsonObject();
			//						json.addProperty("op", "del");
			//						json.addProperty("type", "note");
			//						json.addProperty("userId", bean.getReceive());
			////						json.addProperty("noteType", bean.getType());
			//						json.addProperty("id", bean.getId());
			////						json.addProperty("refid", bean.getRelationId());
			//						new AppleApnsSend(
			//								String.valueOf(SystemState.instance.dogId), str[0],
			//								bean.getTitle(), json.toString(), str[1]).start();
			//					}
			//				}
			//			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据详情ID和接收者删除
	 * @param userId
	 * @param relationId
	 * @return
	 */
	public boolean deleteByRelationId(String relationIds, String userIds) {
		if (null == relationIds || null == userIds) {
			return false;
		}
		userIds = userIds.replaceAll(";", ",").replaceAll("'", "");
		relationIds = relationIds.replaceAll(";", ",").replaceAll("'", "");
		String[] ids = relationIds.split(",");
		String str = "";
		for (String id : ids) {
			str += "'" + id + "',";
		}
		str = str.substring(0, str.length() - 1);
		String sql = "select bean from AdviceBean bean where bean.relationId in (" + str + ") ";
		ArrayList param = new ArrayList();
		if (null != userIds && userIds.length() > 0) {
			ids = userIds.split(",");
			str = "";
			for (String id : ids) {
				str += "'" + id + "',";
			}
			str = str.substring(0, str.length() - 1);
			sql += " and bean.Receive in (" + str + ") ";
		}
		Result rs = this.list(sql, param);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<AdviceBean> listAdvice = (List<AdviceBean>) rs.getRetVal();
			boolean result = listAdvice.size() == 0; // 本来就没有，不用删了，直接返回true
			for (AdviceBean bean : listAdvice) {
				result |= delete(bean); // 只要有一条成功，就当作成功了
			}
			return result;
		} else {
			return false;
		}
	}

	/**
	 * 根据ID删除
	 * @param keyIds
	 * @return
	 */
	public boolean deleteById(String keyIds) {
		if (null == keyIds) {
			return false;
		}
		keyIds = keyIds.replaceAll(";", ",").replaceAll("'", "");
		if (null == keyIds) {
			return false;
		}
		String[] ids = keyIds.split(",");
		String str = "";
		for (String id : ids) {
			str += "'" + id + "',";
		}
		str = str.substring(0, str.length() - 1);
		String sql = "select bean from AdviceBean bean where bean.id in (" + str + ") ";
		Result rs = this.list(sql, new ArrayList());
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<AdviceBean> listAdvice = (List<AdviceBean>) rs.getRetVal();
			boolean result = false;
			for (AdviceBean bean : listAdvice) {
				result |= delete(bean);
			}
			return result;
		} else {
			return false;
		}
	}
	//    /**
	//     * 删除多条单位记录
	//     * @param ids long[]
	//     * @return Result
	//     */
	//    protected Result delete(String[] ids) {
	//    	return deleteBean(ids, AdviceBean.class, "id");
	//    }
	//	/**
	//	 * 删除相应的通知消息
	//	 * 
	//	 * @param oid
	//	 */
	//	protected void deleteMessage(String oid) {
	//		Result deleters = queryallMessage(oid);
	//		List<AdviceBean> listAdvice = (List<AdviceBean>) deleters.getRetVal();
	//		for (int i = 0; i < listAdvice.size(); i++) {
	//			AdviceBean adviceBean = listAdvice.get(i);
	//			delete(adviceBean);
	//		}
	//	}
}