package com.menyi.email;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.AuthenticationFailedException;
import javax.mail.Flags;
import javax.mail.MessagingException;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.JsonObject;
import com.koron.mobile.bean.MobileCount;
import com.koron.oa.bean.MailinfoSettingBean;
import com.koron.oa.bean.OAMailGroupBean;
import com.koron.oa.bean.OAMailInfoBean;
import com.menyi.aio.bean.AlertBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.web.customize.CustomizePYM;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.email.util.AIOEMail;
import com.menyi.email.util.EMailMessage;
import com.menyi.web.util.*;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

public class EMailMgt extends AIODBManager {

	private static HashMap<String, Date> accountMap = new HashMap();
	private PublicMgt pubMgt = new PublicMgt();
	
	/**
	 * 增加邮件，注意更新个人当前邮件大小限制
	 * 
	 * @param bean
	 * @return
	 */
	public Result addMail(final OAMailInfoBean bean) {
		if (bean.getMailTo() != null && bean.getMailTo().length() > 4000) {
			bean.setMailTo(bean.getMailTo().substring(0, 4000));
		}
		if (bean.getMailCc() != null && bean.getMailCc().length() > 4000) {
			bean.setMailCc(bean.getMailCc().substring(0, 4000));
		}
		if (bean.getMailBCc() != null && bean.getMailBCc().length() > 4000) {
			bean.setMailBCc(bean.getMailBCc().substring(0, 4000));
		}
		if (bean.getMailUID() == null || bean.getMailUID().length() == 0) {
			bean.setMailUID(bean.getId());
		}
		Result rs = addBean(bean);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 更新邮件大小
			updateCurMailSize(bean.getEmailType(), bean.getAccount(), bean
					.getUserId(), bean.getMailSize());
			//重算手机未读邮件数
			notifyMobile(bean.getUserId());
		} else {
			System.out.println("EMailMgt.addMail Error " + rs.retCode);
		}
		return rs;
	}

	public Result updateCurMailSize(final int emailType, final String account,
			final String userid, final int mailSize) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {

						Connection conn = connection;
						String sql = "";
						if (emailType == 1) {
							sql = "update tblEmployee set curMailSize= curMailSize + ? where id = (select createBy from MailinfoSetting where id= ?) ";
						} else {
							sql = "update tblEmployee set curMailSize= curMailSize + ? where id = ? ";
						}
						PreparedStatement s = conn.prepareStatement(sql);
						s.setLong(1, mailSize);
						if (emailType == 1) {
							s.setString(2, account);
						} else {
							s.setString(2, userid);
						}
						s.executeUpdate();
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		if (retCode != ErrorCanst.DEFAULT_SUCCESS) {
			AIOEMail.emailLog.error("EMailMgt.updateCurMailSize() Error ");
			rs.retCode = retCode;
			return rs;
		}
		return rs;
	}

	public Result addMailCRM(final String clientId, final String linkman,
			final String email, final String scompanyId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {

						Connection conn = connection;
						String sql = "insert into CRMClientInfoDet(id,f_ref,UserName,Gender,CLientEmail,SCompanyID,mainUser,dateType,contactNo ) values(?,?,?,-100000,?,?,1,1,?) ";

						PreparedStatement s = conn.prepareStatement(sql);
						s.setString(1, IDGenerater.getId());
						s.setString(2, clientId);
						s.setString(3, linkman);
						s.setString(4, email);
						s.setString(5, scompanyId);
						s.setString(6, IDGenerater.getId());
						s.executeUpdate();
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		if (retCode != ErrorCanst.DEFAULT_SUCCESS) {
			AIOEMail.emailLog.error("EMailMgt.addMailCRM() Error ");
			rs.retCode = retCode;
			return rs;
		}
		return rs;
	}

	public Result delMail(String keyId[]) {
		// 附件删除处理
		if (keyId == null || keyId.length == 0)
			return new Result();
		List param = new ArrayList();
		String keys = "";
		for (String s : keyId) {
			keys = keys + ",'" + s + "'";
		}
		String hql = "select bean from OAMailInfoBean bean  where  id in ("
				+ keys.substring(1) + ") ";

		// param.add(keys.substring(1));
		Result rs = list(hql, param);
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return rs;
		} else {
			for (Object o : (ArrayList) rs.retVal) {
				OAMailInfoBean bean = (OAMailInfoBean) o;

				rs = this.deleteBean(bean.getId(), OAMailInfoBean.class, "id");
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					//重算手机未读邮件数
					notifyMobile(bean.getUserId());
					//删除提醒
					deleteBean(bean.getId(), AlertBean.class, "relationId");
					
					// 减少邮箱大小
					updateCurMailSize(bean.getEmailType(), bean.getAccount(),
							bean.getUserId(), -bean.getMailSize());

					String attr = bean.getMailAttaches() + ";"
							+ bean.getInnerAttaches();
					String[] atts = attr.split(";");
					for (String at : atts) {
						if (at != null && at.length() > 0) {
							String attachPath = bean.getAccount();
							if (bean.getEmailType() == 0) {
								attachPath = "inner" + bean.getUserId();
							}
							File aFile = new File(BaseEnv.FILESERVERPATH
									+ "/email/" + attachPath + "/" + at);
							aFile.delete();
						}
					}
					// 删除嵌入式附件
					Pattern rfp = Pattern.compile("(/ReadFile[^\\s\\\"]*)",
							Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
					if (bean.getMailContent() != null
							&& bean.getMailContent().length() > 0) {
						Matcher mat = rfp.matcher(bean.getMailContent());
						while (mat.find()) {
							try {
								String readFile = mat.group(1);
								File f = null;
								if (readFile.indexOf("email") > -1) {
									String fileName = readFile
											.substring(readFile
													.indexOf("fileName=") + 9);
									fileName = java.net.URLDecoder.decode(
											fileName, "UTF-8");
									f = new File(BaseEnv.FILESERVERPATH
											+ "/email/" + fileName);
									f.delete();
								}
							} catch (Exception e1) {

							}
						}
						// 删除编辑器上的图片
						rfp = Pattern.compile("src=\\\"(/upload/[^\\s\\\"]*)",
								Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
						mat = rfp.matcher(bean.getMailContent());
						while (mat.find()) {
							String readFile = mat.group(1);
							File f = new File(BaseEnv.systemRealPath + readFile);
							f.delete();
						}
					}
				}
			}
		}
		return new Result();
	}

	public Result updateMail(OAMailInfoBean bean) {
		Result rs = this.updateBean(bean);
		// 重算手机未读邮件数
		notifyMobile(bean.getUserId());
		return rs;
	}
//
//	/**
//	 * 如果对应的通知还未阅读，则删除
//	 * 
//	 * @param planId
//	 * @param assId
//	 * @param keyId
//	 * @return
//	 */
//	public Result delAdvice(final OAMailInfoBean bean) {
//		if (bean.getToUserId() == null || bean.getToUserId().equals("")) {
//			return new Result();
//		}
//		final Result result = new Result();
//		int retCode = DBUtil.execute(new IfDB() {
//			public int exec(Session session) {
//				session.doWork(new Work() {
//					public void execute(Connection conn) throws SQLException {
//						try {
//							String sql = " delete tbladvice where status='noRead' and receive=? and relationId=? ";
//							PreparedStatement pss = conn.prepareStatement(sql);
//							pss.setString(1, bean.getToUserId());
//							if (bean.getEmailType() == 0) {
//								// 内部邮件，是一封都提示的
//								pss.setString(2, bean.getId());
//							} else {
//								// 外部邮件，是接收一次提示一次
//								pss.setString(2, bean.getAccount());
//							}
//							pss.executeUpdate();
//						} catch (SQLException ex) {
//							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//							ex.printStackTrace();
//							AIOEMail.emailLog.error("EMailMgt.delAdvice : ", ex);
//							return;
//						}
//					}
//				});
//				return result.getRetCode();
//			}
//		});
//		result.retCode = retCode;
//		return result;
//	}

	/*
	 * 采用一个新的独立线程来发送邮件
	 * 
	 */
	public void sendByThread(final EMailMessage smes, final String account,
			final String mailId, final String userId) {

		// 查询帐号信息
		Result rs = loadAccount(account);
		if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
			return;
		}
		MailinfoSettingBean setting = (MailinfoSettingBean) rs.getRetVal();

		sendByThread(smes, setting, mailId, userId);
	}

	/*
	 * 采用一个新的独立线程来发送邮件
	 * 
	 */
	public void sendByThread(final EMailMessage smes,
			final MailinfoSettingBean setting, final String mailId,
			final String userId) {
		Thread thread = new Thread() {
			public void run() {
				try {

					AIOEMail sm = new AIOEMail();
					boolean smtpauth = setting.getSmtpAuth() == 1 ? true
							: false;
					// 指定要使用的邮件服务器
					sm.setAccount(setting.getMailaddress(), setting
							.getPop3server(), setting.getPop3username(),
							setting.getPop3userpassword(), smtpauth, setting
									.getSmtpserver(),
							setting.getSmtpusername(), setting
									.getSmtpuserpassword(), setting
									.getPop3Port(), setting.getSmtpPort(),
							setting.getDisplayName(), setting.getId(), setting
									.getCreateby(),
							setting.getPopssl() == 1 ? true : false, setting
									.getSmtpssl() == 1 ? true : false, setting
									.getAutoAssign() == 1 ? true : false);
					// 调用发送邮件接口
					sm.send(smes,userId);

				} catch (AuthenticationFailedException ex) {
					AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
					if (mailId != null && mailId.length() > 0) {
						Result r = loadMail(mailId);
						if (r.retCode == ErrorCanst.DEFAULT_SUCCESS
								&& r.retVal != null) {
							OAMailInfoBean mb = (OAMailInfoBean) r.retVal;
							mb.setGroupId("2");// 改为草稿
							updateMail(mb);
						}
					}
					NotifyFashion nf = new NotifyFashion("1",
							"RES<common.msg.mailsendError>-RES<oa.mail.send.AuthenticationFailture>:"
									+ smes.getSubject(),
							"RES<common.msg.mailsendError>-RES<oa.mail.send.AuthenticationFailture>"
									+ smes.getSubject(), userId, 4, "no",
							mailId);
					nf.start();
				} catch (MessagingException ex) {
					AIOEMail.emailLog.error("EMailAction.receiveMail Error ", ex);
					if (mailId != null && mailId.length() > 0) {
						Result r = loadMail(mailId);
						if (r.retCode == ErrorCanst.DEFAULT_SUCCESS
								&& r.retVal != null) {
							OAMailInfoBean mb = (OAMailInfoBean) r.retVal;
							mb.setGroupId("2");// 改为草稿
							updateMail(mb);
						}
					}
					NotifyFashion nf = new NotifyFashion("1",
							"RES<common.msg.mailsendError>-RES<oa.mail.receive.ConnFailture>:"
									+ smes.getSubject(),
							"RES<common.msg.mailsendError>-RES<oa.mail.receive.ConnFailture>"
									+ smes.getSubject(), userId, 4, "no",
							mailId);
					nf.start();
				} catch (Exception ex) {
					AIOEMail.emailLog.error("EMailAction.add SendMail Error ", ex);
					if (mailId != null && mailId.length() > 0) {
						Result r = loadMail(mailId);
						if (r.retCode == ErrorCanst.DEFAULT_SUCCESS
								&& r.retVal != null) {
							OAMailInfoBean mb = (OAMailInfoBean) r.retVal;
							mb.setGroupId("2");// 改为草稿
							updateMail(mb);
						}
					}
					NotifyFashion nf = new NotifyFashion(
							"1",
							"RES<common.msg.mailsendError>:"
									+ smes.getSubject(),
							"RES<common.msg.mailsendError>" + smes.getSubject(),
							userId, 4, "no", mailId);
					nf.start();
				}
			}
		};
		thread.start();

	}

	public Result addMailinfoSetting(MailinfoSettingBean oldBean) {
		// 重名判断
		String hql = "select count(*) from MailinfoSettingBean bean where bean.mailaddress=?";
		ArrayList param = new ArrayList();
		param.add(oldBean.getMailaddress());
		Result rs = this.list(hql, param);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			Object o = ((List) rs.retVal).get(0);
			int count = Integer.parseInt(o.toString());
			if (count > 0) {
				rs.retCode = ErrorCanst.MULTI_VALUE_ERROR;
				return rs;
			}
		}

		if (oldBean.getSmtpsamepop() == 1) {
			oldBean.setSmtpusername(oldBean.getPop3username());
			oldBean.setSmtpuserpassword(oldBean.getPop3userpassword());
		}
		return this.addBean(oldBean);
	}

	/**
	 * mj 根据该邮件id查询对应的收件人的详细信息
	 * 
	 * @param pId
	 *            该邮件id 即收件人的父Id
	 * @param isNeedTop
	 *            是否需要top分页显示
	 * 
	 * @return
	 */
	public Result queryReceives(String pId, String topCount, String empName) {
		StringBuilder sb = new StringBuilder("select ");
		if (StringUtils.isNotBlank(topCount)) {
			sb.append("top 5 ");
		}
		ArrayList param = new ArrayList();
		sb
				.append("empfullname,deptfullname,replaydate,state,(select deptfullname from tblDepartment dt where dt.classCode = substring(dept.classcode,1,len(dept.classCode)-5)) as pDeptName ,main.id ,emp.id from OAMailInfo main inner join tblEmployee emp on main.touserId = emp.id inner join tblDepartment dept on emp.departmentcode = dept.classcode where main.pId = ?");
		param.add(pId);

		if (StringUtils.isNotBlank(empName)) {
			sb.append(" and empfullname = ?");
			param.add(empName);
		}
		return this.sqlList(sb.toString(), param);
	}

	public Result updateMailinfoSetting(MailinfoSettingBean bean) {
		// 重名判断
		String hql = "select count(*) from MailinfoSettingBean bean where bean.mailaddress=? and bean.id <> ?";
		ArrayList param = new ArrayList();
		param.add(bean.getMailaddress());
		param.add(bean.getId());
		Result rs = this.list(hql, param);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			Object o = ((List) rs.retVal).get(0);
			int count = Integer.parseInt(o.toString());
			if (count > 0) {
				rs.retCode = ErrorCanst.MULTI_VALUE_ERROR;
				return rs;
			}
		}

		rs = this.loadBean(bean.getId(), MailinfoSettingBean.class);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			MailinfoSettingBean oldBean = (MailinfoSettingBean) rs.retVal;

			oldBean.setAccount(bean.getAccount());
			oldBean.setDefaultUser(bean.getDefaultUser());
			oldBean.setDisplayName(bean.getDisplayName());
			oldBean.setMailaddress(bean.getMailaddress());
			oldBean.setPop3Port(bean.getPop3Port());
			oldBean.setPop3server(bean.getPop3server());
			oldBean.setPop3username(bean.getPop3username());
			oldBean.setPop3userpassword(bean.getPop3userpassword());
			oldBean.setPopssl(bean.getPopssl());
			oldBean.setSmtpAuth(bean.getSmtpAuth());
			oldBean.setSmtpPort(bean.getSmtpPort());
			oldBean.setSmtpsamepop(bean.getSmtpsamepop());
			oldBean.setSmtpserver(bean.getSmtpserver());
			oldBean.setSmtpssl(bean.getSmtpssl());
			oldBean.setSmtpusername(bean.getSmtpusername());
			oldBean.setSmtpuserpassword(bean.getSmtpuserpassword());

			oldBean.setLastupdateby(bean.getLastupdateby());
			oldBean.setLastupdatetime(bean.getLastupdatetime());
			oldBean.setAutoAssign(bean.getAutoAssign());
			oldBean.setMainAccount(bean.getMainAccount());
			if (oldBean.getSmtpsamepop() == 1) {
				oldBean.setSmtpusername(oldBean.getPop3username());
				oldBean.setSmtpuserpassword(oldBean.getPop3userpassword());
			}
			oldBean.setAutoReceive(bean.getAutoReceive());
			oldBean.setRetentDay(bean.getRetentDay());
			return this.updateBean(oldBean);
		}
		return rs;
	}

	public int moveMail(String keyId[], String groupId) {
		int c = 0;
		String et = null;
		if (groupId.indexOf(":") > 0) {
			// 移动到自定义文件夹，要比较一下邮箱帐号，因为自定义文件夹都隶属于某个邮箱，不能混移
			et = groupId.substring(groupId.indexOf(":") + 1);
			groupId = groupId.substring(0, groupId.indexOf(":"));
		}

		for (String key : keyId) {
			Result rs = loadBean(key, OAMailInfoBean.class);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				OAMailInfoBean bean = (OAMailInfoBean) rs.retVal;
				if (et != null) {
					if (bean.getAccount().equals(et)) {
						bean.setGroupId(groupId);
						rs = this.updateBean(bean);
					} else { // 邮箱不一致
						c++;
					}
				} else {
					if("4".equals(groupId)||"5".equals(groupId)){
						//若是移动到垃圾箱或废件箱不收藏
						bean.setCollectionType("0");
					}
					bean.setGroupId(groupId);
					rs = this.updateBean(bean);
				}
				
				//删除提醒
				deleteBean(key, AlertBean.class, "relationId");
				
				//重算手机未读邮件数
				notifyMobile(bean.getUserId());
			}
		}
		return c;
	}

	public Result mailAssign(final String keyId[], final String account,
			final String userId, final String assignusers) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {

						Connection conn = connection;
						String sql = "insert into tblMailAssign(id,account,mailId,toUserId,createBy,createTime) values(?,?,?,?,?,?) ";

						String csql = " select count(*) from tblMailAssign where account=? and mailId=? and toUserId=? ";
						PreparedStatement cs = null;

						PreparedStatement s = conn.prepareStatement(sql);
						for (String key : keyId) {
							for (String u : assignusers.split(",")) {
								if (u.length() > 0) {
									cs = conn.prepareStatement(csql);
									cs.setString(1, account);
									cs.setString(2, key);
									cs.setString(3, u);
									ResultSet cr = cs.executeQuery();
									if (cr.next()) {
										if (cr.getInt(1) > 0)
											continue;
									}

									s.setString(1, IDGenerater.getId());
									s.setString(2, account);
									s.setString(3, key);
									s.setString(4, u);
									s.setString(5, userId);
									s.setString(6, BaseDateFormat.format(
											new Date(),
											BaseDateFormat.yyyyMMddHHmmss));
									s.addBatch();
								}
							}
						}
						s.executeBatch();
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		if (retCode != ErrorCanst.DEFAULT_SUCCESS) {
			AIOEMail.emailLog.error("EMailMgt.addMailCRM() Error ");
			rs.retCode = retCode;
			return rs;
		}
		return rs;
	}

	public void signMail(String keyId[], int sign) {
		for (String key : keyId) {
			Result rs = loadBean(key, OAMailInfoBean.class);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				OAMailInfoBean bean = (OAMailInfoBean) rs.retVal;
				bean.setState(sign);
				rs = this.updateBean(bean);
				//重算手机未读邮件数
				notifyMobile(bean.getUserId());
			}
		}
	}

	public Result queryMail(String userId, String account, String groupId,
			String keyword, String email, int pageNo, int pageSize) {
		List param = new ArrayList();
		String hql = "";
		if (account.equals("")) {// 如果是内部邮箱的话要查询该用户的邮件信息
			hql = "select bean from OAMailInfoBean bean  where  bean.userId=? and bean.account=? and bean.groupId=? ";
			param.add(userId);
		} else {// 如果是外部邮箱的话,要查询该邮箱所有的邮件信息
			hql = "select bean from OAMailInfoBean bean  where  bean.account in "
					+ "(select bean1.id from MailinfoSettingBean bean1 where bean1.mailaddress=(select bean2.mailaddress from MailinfoSettingBean bean2 where bean2.id=?)) and bean.groupId=? ";
		}
		param.add(account);
		param.add(groupId);
		if (email != null && email.length() > 0) {
			if (groupId.equals("2") || groupId.equals("3")) {
				hql += " and upper(bean.mailTo + bean.mailCc +bean.mailBCc) like '%'||?||'%'";
			} else {
				hql += " and upper(bean.mailFrom) like '%'||?||'%'";
			}
			param.add(email);
		}
		if (keyword != null && keyword.length() > 0) {
			hql += " and upper(bean.mailTitle + bean.mailContent) like '%'||?||'%'";
			param.add(keyword);
		}
		hql += " order by bean.mailTime desc ";

		return list(hql, param, pageNo, pageSize, true);

	}

	/**
	 * 查询最近联系人 邮件
	 * 
	 * @param ids
	 * @return
	 */
	public Result hisEmpOfMail(final String userId, final String groupId,String account) {
		String sql = "select  DISTINCT(A.mailFrom) FROM (select top 100 mailFrom from OAMailInfo bean with(index(InxOAMailInfo_account_groupid)) where  bean.userId = ? and bean.account='' and bean.groupId = ? order by bean.mailTime desc) as A";
		//b74a05c4_1206091957402879348 47e437ca_1104150929403784124
		List param = new ArrayList();
		if (StringUtils.isNotBlank(account)) {
			sql = "select  DISTINCT(A.mailFrom) FROM (select top 100 mailFrom  from OAMailInfo bean with(index(InxOAMailInfo_account_groupid))   where  (bean.account =? or bean.account in ( select id from mailinfosetting where mainaccount=? and statusid=1))  and bean.groupId=? ) as A ";
			param.add(account);
			param.add(account);
			param.add(groupId);
		} else {
			param.add(userId);
			param.add(groupId);
		}
		return sqlList(sql, param);

	}

	public Result queryMail(String userId, String account, String groupId,
			String keyword, String email, String view, String orderby,
			String isdesc, String labelId, int pageNo, int pageSize,
			String beginTime, String endTime,String searchFlag) {
		List param = new ArrayList();
		String sql = "select bean.id,bean.account,bean.emailType,bean.fromUserId,bean.groupId,bean.labelId,bean.mailAttaches,bean.mailBCc,bean.mailCc,bean.mailFrom,bean.mailSize,bean.mailTime,bean.mailTitle,bean.mailTo,bean.replayDate,bean.revolveDate,bean.sendeMailType,bean.state,bean.toUserId,bean.userId,bean.createTime,bean.remark,bean.mailContent ";
		if ("".equals(account)) {// 如果是内部邮箱的话要查询该用户的邮件信息
			sql += " from OAMailInfo bean with(index(InxOAMailInfo_account_groupid)) where  bean.userId=? and bean.account='' and bean.groupId=? ";
			param.add(userId);
			param.add(groupId);
		} else if ("assign".equals(account)) {// 如果是分配给我的邮箱
			sql += " from OAMailInfo bean join tblmailassign a on a.mailid=bean.id  where  a.touserId=? and groupid = '1' ";
			param.add(userId);
		} else if ("alert".equals(account)) {
			sql += " from OAMailInfo bean,tblAlert bean2 where bean.id=bean2.relationId and bean2.createBy=?";
			param.add(userId);
		} else if ("collection".equals(account)) {
			Result result = selectAccountByUser(userId);
			sql += " from OAMailInfo bean where bean.userId in (?";
			param.add(userId);
			//keke： 此处修改代码 ，是为了解决收藏夹不出现外部邮件的问题
			List<MailinfoSettingBean>  outterMails=(List<MailinfoSettingBean>)result.retVal;
			for(MailinfoSettingBean outter : outterMails){
				sql += ",?";
				param.add(outter.getLastupdateby());
			}
			sql += " ) and bean.collectionType = '1'";
			
		}else if ("all".equals(account)) {// 如果outlook风格查看所有邮箱,我的和分配给我的邮箱
			sql += " from OAMailInfo bean with(index(InxOAMailInfo_account_groupid)) join mailinfosetting a on bean.account=a.id where bean.groupId=? and a.statusid=1 and  ( a.id in  "
					+ "( select aa.id from mailinfosetting aa where aa.createBy=? or aa.id in (select f_ref from MailinfoSettingUser b where b.userId=? )) or "
					+ " a.id in ( select id from mailinfosetting where mainAccount in(   select aa.id from mailinfosetting aa where aa.createBy=? or aa.id in (select f_ref from MailinfoSettingUser b where b.userId=? )) )  )";
			param.add(groupId);
			param.add(userId);
			param.add(userId);
			param.add(userId);
			param.add(userId);
		} else {// 如果是外部邮箱的话,要查询该邮箱所有的邮件信息
			sql += " from OAMailInfo bean with(index(InxOAMailInfo_account_groupid))   where  (bean.account =? or bean.account in ( select id from mailinfosetting where mainaccount=? and statusid=1))  and bean.groupId=? ";
			param.add(account);
			param.add(account);
			param.add(groupId);
		}

		if (email != null && email.length() > 0) {
			if (groupId.equals("2") || groupId.equals("3")) {
				sql += " and upper(bean.mailTo + bean.mailCc +bean.mailBCc) like ? ";
			} else {
				sql += " and upper(bean.mailFrom) like ? ";
			}
			param.add("%" + email + "%");
		}
		if (keyword != null && !"".equals(keyword) && !"关键字搜索".equals(keyword) && !"标题查询".equals(keyword)) {
			//sql += " and (upper(bean.mailTitle) like ? or bean.mailContent like ?  )";
			sql += " and (upper(bean.mailTitle) like ? ";
			if(searchFlag !=null && "true".equals(searchFlag)){
				sql += " or bean.mailContent like ? ";
				param.add("%" + keyword + "%");
			}
			sql +=" )";
			param.add("%" + keyword + "%");
		}
		if (beginTime != null && !"".equals(beginTime)) {
			sql += " and substring(bean.mailTime,1,10) >= ?";
			param.add(beginTime);
		}
		if (endTime != null && !"".equals(endTime)) {
			sql += " and substring(bean.mailTime,1,10) <= ?";
			param.add(endTime);
		}

		if (view != null && "1".equals(view)) {
			sql += " and bean.state = ?";
			param.add(0);
		} else if (view != null && "2".equals(view)) {
			sql += " and bean.state = ?";
			param.add(1);
		} else if (view != null && "3".equals(view)) {
			sql += " and len(bean.replayDate) >0 ";
		} else if (view != null && "4".equals(view)) {
			sql += " and len(bean.revolveDate) > 0 ";
		} else if (view != null && "5".equals(view)) {
			sql += " and exists(select id from tblmailassign a where a.mailid=bean.id) ";
		}

		if (labelId != null && labelId.length() > 0) {
			sql += " and bean.labelId like '%'+?+'%'";
			param.add(labelId);
		}

		sql +=" order by ";
		if ("1".equals(orderby)) {
			sql += " bean.mailFrom  ";
		} else if ("2".equals(orderby)) {
			sql += " bean.mailTitle  ";
		} else if ("3".equals(orderby)) {
			sql += " bean.mailSize  ";
		} else {
			sql += " bean.mailTime  ";
		}
		if ("false".equals(isdesc)) {
			sql += " asc ";
		} else {
			sql += " desc ";
		}

		Result rs = sqlList(sql, param, pageSize, pageNo, true);
		/**
		 * 保存各时间的数据
		 */
		LinkedHashMap<Integer,ArrayList> map = new LinkedHashMap<Integer,ArrayList>();
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
	        
			for (Object o : (ArrayList) rs.retVal) {
				Object os[] = (Object[]) o;
				
				String mailTime = String.valueOf(os[11]);
				
				OAMailInfoBean bean = new OAMailInfoBean();
				bean.setId((String) os[0]);
				bean.setEmailType((Integer) os[2]);
				bean.setFromUserId((String) os[3]);
				bean.setGroupId((String) os[4]);
				bean.setLabelId((String) os[5]);
				bean.setMailAttaches((String) os[6]);
				bean.setMailBCc((String) os[7]);
				bean.setMailCc((String) os[8]);
				bean.setMailFrom((String) os[9]);
				bean.setMailSize(Integer.parseInt(os[10].toString()) / 1024);
				bean.setMailTime(mailTime);
				bean.setMailTitle((String) os[12]);
				bean.setMailTo((String) os[13]);
				bean.setReplayDate((String) os[14]);
				bean.setRevolveDate((String) os[15]);
				bean.setSendeMailType((String) os[16]);
				bean.setState((Integer) os[17]);
				bean.setToUserId((String) os[18]);
				bean.setUserId((String) os[19]);
				bean.setCreateTime((String) os[20]);
				bean.setRemark((String)os[21]);
				String content = GlobalsTool.replaceHTMLMail(String.valueOf(os[22]));
				if(content.length()>100){
					content = content.substring(0,100)+"...";
				}
				bean.setMailContent(content);
				
				bean.setMailTitle(bean.getMailTitle().replaceAll("'|\\r|\\n", ""));
				
				int count = getTimeSection(mailTime);
				ArrayList list = map.get(count);
				if(list == null){
					list = new ArrayList();
				}
				list.add(bean);
				map.put(count, list);
			}
		}
		
		//查询未读邮件的条数
		String count = "";
		if(groupId != null && "1".equals(groupId)){
			Result rslt = noReadMail(sql, param);
			count = String.valueOf(rslt.getRetVal());
		}
		Object[] obj = new Object[]{count, map, sql, param};
		rs.retVal = obj;
		return rs;
	}
	
	/**
	 * 对邮件进行分类（今天，昨天，前天，上周，本周，本月，更多）
	 * @param time
	 * @return
	 */
	public Integer getTimeSection(String time){
		try {
			SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
			Date times = simple.parse(time);
			Date date = new Date();
			String todayStr = simple.format(date);   
			Date today = simple.parse(todayStr);   

			long l = 24*60*60*1000;
			if(today.getTime()-times.getTime()==0){
				//今天
				return 1;
			}
			if((today.getTime()-times.getTime())>0 && (today.getTime()-times.getTime())<=l){
				//昨天
				return 2;
			}
			if((today.getTime()-times.getTime())>l && (today.getTime()-times.getTime())<=2*l){
				//前天
				return 4;
			}
			Date firstDate = getFirstDayOfWeek(today);
			if(firstDate.getTime()-times.getTime()<=0){
				//本周
				return 8;
			}
			Calendar gc = Calendar.getInstance();
			gc.setTime(firstDate);
			gc.set(gc.DATE, (gc.get(gc.DATE))-7);
			if(gc.getTime().getTime()-times.getTime()<=0){
				//上周
				return 16;
			}
			gc = Calendar.getInstance();
			gc.setTime(today);
			gc.set(Calendar.DAY_OF_MONTH,1);
			if(gc.getTime().getTime()-times.getTime()<=0){
				//本月
				return 64;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 256;
	}

	/**
	 * 本周
	 * @param date
	 * @return
	 */
	public static synchronized Date getFirstDayOfWeek(Date date){
		Calendar gc = Calendar.getInstance();
		gc.setTime(date);
		  switch ( gc.get( Calendar.DAY_OF_WEEK ) ){
		   case ( Calendar.SUNDAY  ):
		    gc.add( Calendar.DATE, -6 );
		    break;
		   case ( Calendar.MONDAY  ):
		    gc.add( Calendar.DATE, 0 );
		    break;
		   case ( Calendar.TUESDAY  ):
		    gc.add( Calendar.DATE, -1 );
		    break;
		   case ( Calendar.WEDNESDAY  ):
		    gc.add( Calendar.DATE, -2 );
		    break;
		   case ( Calendar.THURSDAY  ):
		    gc.add( Calendar.DATE, -3 );
		    break;
		   case ( Calendar.FRIDAY  ):
		    gc.add( Calendar.DATE, -4 );
		    break;
		   case ( Calendar.SATURDAY  ):
		    gc.add( Calendar.DATE, -5 );
		    break;
		  }
		  return gc.getTime();
	}

	/**
	 * 根据时间段查询统计
	 * @param count
	 * @param sql
	 * @return
	 */
	public Result countMail(final int count, final String sql, final List param){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn)
							throws SQLException {
						String sqls = "select count(0) as count " + sql.substring(sql.indexOf("from OAMailInfo"),sql.lastIndexOf("order by"));
						if(count == 1){
							//今天
							sqls += " and datediff(day,bean.mailTime,getdate())=0 ";
						}else if(count == 2){
							//昨天
							sqls += " and datediff(day,bean.mailTime,getdate())=1 ";
						}else if(count == 4){
							//前天
							sqls += " and datediff(day,bean.mailTime,getdate())=2 ";
						}else if(count == 8){
							//本周
							sqls += " and datediff(wk,bean.mailTime,getdate())=0 and datediff(day,bean.mailTime,getdate()) not in (0,1,2)";
						}else if(count == 16){
							//上周
							sqls += " and datediff(wk,bean.mailTime,getdate())=1";
						}else if(count == 64){
							//本月
							sqls += " and datediff(mm,bean.mailTime,getdate())=0 and datediff(wk,bean.mailTime,getdate()) not in (0,1)";
						}else if(count == 256){
							//更多
							sqls += " and datediff(mm,bean.mailTime,getdate())>0";
						}
						PreparedStatement ps = conn.prepareStatement(sqls);
						for(int i=0;i<param.size();i++){
							ps.setObject(i+1, (Object)param.get(i));
						}
						ResultSet rset = ps.executeQuery();
						int counts = 0;
						if(rset.next()){
							counts = rset.getInt("count");
						}
						rs.retVal = counts;
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	/**
	 * 查询未读邮件的数量
	 * @param sql
	 * @param param
	 * @return
	 */
	public Result noReadMail(final String sql, final List param){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn)
							throws SQLException {
						String sqls = "select count(0) as count " + sql.substring(sql.indexOf("from OAMailInfo"),sql.lastIndexOf("order by"));
						sqls += " and bean.state=0";
						PreparedStatement ps = conn.prepareStatement(sqls);
						for(int i=0;i<param.size();i++){
							ps.setObject(i+1, (Object)param.get(i));
						}
						ResultSet rset = ps.executeQuery();
						int counts = 0;
						if(rset.next()){
							counts = rset.getInt("count");
						}
						rs.retVal = counts;
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	
	
	public Result queryLastDetail(String userId, String account,
			String groupId, String time) {
		List param = new ArrayList();
		/*
		 * if("1".equals(userId)){ String hql = "select top 1 id,mailTitle from
		 * OAMailInfo bean where bean.account=? and bean.groupId=? and
		 * bean.mailTime<? order by bean.mailTime desc"; param.add(account);
		 * param.add(groupId); param.add(time);
		 * 
		 * return sqlList(hql, param); }else{
		 */
		String hql = "select top 1 id,mailTitle from OAMailInfo bean  where  bean.userId=? and bean.account=? and bean.groupId=? and bean.mailTime<? order by bean.mailTime desc";
		param.add(userId);
		param.add(account);
		param.add(groupId);
		param.add(time);

		return sqlList(hql, param);
		// }

	}

	public Result queryNextDetail(String userId, String account,
			String groupId, String time) {
		List param = new ArrayList();
		/*
		 * if("1".equals(userId)){ String hql = "select top 1 id,mailTitle from
		 * OAMailInfo bean where bean.account=? and bean.groupId=? and
		 * bean.mailTime>? order by bean.mailTime "; param.add(account);
		 * param.add(groupId); param.add(time);
		 * 
		 * return sqlList(hql, param); }else{
		 */
		String hql = "select top 1 id,mailTitle from OAMailInfo bean  where  bean.userId=? and bean.account=? and bean.groupId=? and bean.mailTime>? order by bean.mailTime ";
		param.add(userId);
		param.add(account);
		param.add(groupId);
		param.add(time);

		return sqlList(hql, param);
		// }

	}

	public Result loadMail(String id) {
		return loadBean(id, OAMailInfoBean.class);
	}

	/**
	 * 删除邮件草稿，不能删除附件，但必须减少邮箱大小
	 * 
	 * @param id
	 * @return
	 */
	public Result delDrafMail(String id) {
		Result rs = this.loadBean(id, OAMailInfoBean.class);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			deleteBean(id, OAMailInfoBean.class, "id");
			OAMailInfoBean bean = (OAMailInfoBean) rs.retVal;
			// 更新邮件大小
			updateCurMailSize(bean.getEmailType(), bean.getAccount(), bean
					.getUserId(), -bean.getMailSize());
			//重算手机未读邮件数
			notifyMobile(bean.getUserId());
		}
		return rs;
	}

	public Result delAccount(String[] ids) {
		if (ids == null || ids.length == 0) {
			return new Result();
		}
		// 有邮件不能删除
		List param = new ArrayList();
		String istr = "";
		for (String id : ids) {
			istr += "?,";
			param.add(id);
		}
		istr = istr.substring(0, istr.length() - 1);

		String hql = "select count(*) from OAMailInfoBean bean where bean.account in ("
				+ istr + ")";

		Result rs = this.list(hql, param);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			Object o = ((List) rs.retVal).get(0);
			int count = Integer.parseInt(o.toString());
			if (count > 0) {
				rs.retCode = ErrorCanst.DATA_ALREADY_USED;
				rs.retVal = "MailUse";
				return rs;
			}
		}
		// //被作为主帐户引用不能删除
		// hql = "select count(*) from MailinfoSettingBean bean where
		// bean.mainAccount = ?";
		// rs = this.list(hql, param);
		// if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
		// Object o = ((List)rs.retVal).get(0);
		// int count = Integer.parseInt(o.toString());
		// if(count >0){
		// rs.retCode = ErrorCanst.DATA_ALREADY_USED;
		// rs.retVal="mainAccountUse";
		// return rs;
		// }
		// }
		rs = deleteBean(ids, MailinfoSettingBean.class, "id");
		for (String id : ids) {
			deleteGuidByAccount(id); // 删除该帐号的邮件痕迹
		}
		return rs;
	}

	
	/**
	 * 删除有邮件的邮箱；并确定删除该邮箱的所有邮件
	 * @param ids
	 * @return
	 */
	public Result delSureAccount(final String[] ids) {
		// 这里用了事务，所以得在调用方那里执行notifyMobile
		if (ids == null || ids.length == 0) {
			return new Result();
		}
		
		final Result result= new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				
				Result rs =null;
						try {
						 rs = new Result();
						 for(String id : ids){
						 rs = deleteBean(id, OAMailInfoBean.class, "account",session);
						 rs = deleteBean(id, MailinfoSettingBean.class, "id",session);
						 deleteGuidByAccount(id); // 删除该帐号的邮件痕迹
						 }
						result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							AIOEMail.emailLog.error("OAMyLogMgt queryMyFriends", ex);
							
						}
						return result.getRetCode();	
			
				
			}
		});
	
		return result;
	
	}
	
	
	
	public Result selectConnector(String name) {
		List param = new ArrayList();
		String sql = "select bean.email, bean.empFullName from tblEmployee bean where bean.empFullName=?";
		param.add(name);
		return sqlList(sql, param);
	}

	public Result selectClientMan(String name) {
		List param = new ArrayList();
		String sql = "select bean.ClientEmail,bean.UserName from CRMClientInfoDet bean where bean.UserName=?";
		param.add(name);
		return sqlList(sql, param);

	}

	public Result selectConnectorName(String email) {
		List param = new ArrayList();
		String sql = "select bean.empFullName from tblEmployee bean where bean.email=?";
		param.add(email);
		return sqlList(sql, param);
	}

	public Result selectDeptUser(String name) {
		List param = new ArrayList();
		// 查询最子级的数据
		String sql = "select classCode from tblDepartment where classCode like (select classCode from tblDepartment bean where bean.deptFullName=?)+'%' ";
		param.add(name);
		Result rs = sqlList(sql, param);
		if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS
				|| ((List) rs.retVal).size() == 0) {
			rs.setRetCode(ErrorCanst.NULL_OBJECT_ERROR);
			return rs;
		}
		List classCode = (List) rs.retVal;
		String deptCodes = "";
		for (int i = 0; i < classCode.size(); i++) {
			Object[] obj = (Object[]) classCode.get(i);
			deptCodes += "'" + obj[0].toString() + "',";
		}
		if (deptCodes.length() > 0) {
			deptCodes = deptCodes.substring(0, deptCodes.length() - 1);
		}
		param.clear();
		sql = " select id from tblEmployee bean where openFlag =1 and statusId = 0 and  bean.departmentCode in ("
				+ deptCodes + ") ";

		return sqlList(sql, param);
	}

	public Result selectUser(String name) {
		List param = new ArrayList();
		String sql = " select id from tblEmployee bean where openFlag =1 and  bean.empFullName= ? ";
		param.add(name);
		return sqlList(sql, param);
	}

	public Result selectUserName(String id) {
		List param = new ArrayList();
		String sql = " select empFullName from tblEmployee bean where openFlag =1 and  bean.id= ? ";
		param.add(id);
		return sqlList(sql, param);
	}

	// 查询内部邮件用户名
	public Result selectUserNames(final String ids) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							String strsql = " select id,empFullName from tblEmployee bean where openFlag =1 and  bean.id in ("
									+ ids + ") ";
							PreparedStatement ps = conn
									.prepareStatement(strsql);
							ResultSet rs = ps.executeQuery();
							HashMap<String, String> ns = new HashMap<String, String>();
							while (rs.next()) {
								ns.put(rs.getString("id"), rs
										.getString("empFullName"));// 联系人为键，地址为值
							}
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							rst.setRetVal(ns);
						} catch (Exception e) {
							AIOEMail.emailLog.error(
									"EMailMgt.selectUserNames Error ", e);
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);

		return rst;

	}

	// 查询外部邮件账户名
	public Result selectAccountNames(String ids) {
		List param = new ArrayList();
		String sql = " select Email,EmpFullName from tblEmployee bean where   bean.Email in ("
				+ ids + ")";
		return sqlList(sql, param);
	}

	/**
	 * 查询出当前用户的默认账户,包括所有自己创建的，和共享给自己的
	 * 
	 * @param userId
	 *            String
	 * @return Result
	 */
	public Result selectAccountByUser(final String userId) {
		List param = new ArrayList();
		String sql = "select f_ref from MailinfoSettingUser where userId=?";
		param.add(userId);
		Result rs = sqlList(sql, param);

		String ids = "";
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			for (Object o : (List) rs.retVal) {
				Object[] os = (Object[]) o;
				ids += "'" + os[0] + "',";
			}
		}

		param.clear();
		String hql = "";
		if (ids.length() == 0) {
			hql = "select bean from MailinfoSettingBean bean where bean.createby=? and bean.statusid = 1 and (bean.mainAccount = '' or bean.mainAccount is null) ";
		} else {
			hql = "select bean from MailinfoSettingBean bean where bean.statusid = 1  and (bean.mainAccount = '' or bean.mainAccount is null)  and( bean.createby=?   or bean.id in ("
					+ ids.substring(0, ids.length() - 1) + "))";
		}
		param.add(userId);
		return list(hql, param);
	}

	public Result selectAllAccount() {
		List param = new ArrayList();
		String hql = "select a from MailinfoSettingBean a where a.id=(select min(b.id) from MailinfoSettingBean b where a.mailaddress=b.mailaddress)";
		return list(hql, param);
	}

	/**
	 * 查询出邮件账户根据邮件地址
	 * 
	 * @param userId
	 *            String
	 * @return Result
	 */
	public Result selectAccountByEmail(final String email) {
		List param = new ArrayList();
		String hql = "select bean from MailinfoSettingBean bean where bean.mailaddress=?";
		param.add(email);
		return list(hql, param);
	}

	/**
	 * 查询出当前用户的默认账户
	 * 
	 * @param userId
	 *            String
	 * @return Result
	 */
	public Result loadAccount(final String id) {
		List param = new ArrayList();
		return loadBean(id, MailinfoSettingBean.class);
	}

	/**
	 * 查询出当前用户的默认账户
	 * 
	 * @param userId
	 *            String
	 * @return Result
	 */
	public Result loadShareUser(final String id) {
		List param = new ArrayList();
		String sql = " select a.userId,b.empFullName from MailinfoSettingUser a left join tblEmployee b on a.userId=b.id where f_ref=?";
		param.add(id);
		return sqlList(sql, param);
	}

	public Result updateSare(final String id, final String userIds[],
			final String sCompanyID) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {

						Connection conn = connection;
						String sql = "";

						sql = " delete MailinfoSettingUser where f_ref=?";
						PreparedStatement s = conn.prepareStatement(sql);
						s.setString(1, id);
						s.executeUpdate();
						for (String user : userIds) {
							if (user == null || user.length() == 0  || "".equals(user))
								continue;
							sql = " insert MailinfoSettingUser(id,f_ref,userId,SCompanyID) values(?,?,?,?)";
							s = conn.prepareStatement(sql);
							s.setString(1, IDGenerater.getId());
							s.setString(2, id);
							s.setString(3, user);
							s.setString(4, sCompanyID);
							s.executeUpdate();
						}

					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		if (retCode != ErrorCanst.DEFAULT_SUCCESS) {
			AIOEMail.emailLog.error("EMailMgt.updateSare() Error ");
			rs.retCode = retCode;
			return rs;
		}
		return rs;
	}

	public boolean filterMailFrom(final OAMailInfoBean bean) {
		if (bean.getMailFrom() == null
				|| bean.getMailFrom().trim().length() == 0) {
			return true;
		}
		HashMap<String, String> paramMap = EMailSettingMgt.listParamMap();
		// 日期过滤
		String filterDate = paramMap.get("filterDate");
		if (bean.getMailTime() != null && filterDate != null
				&& filterDate.length() > 0 && !filterDate.equals("0")) {
			try {
				long time = BaseDateFormat.parse(bean.getMailTime(),
						BaseDateFormat.yyyyMMddHHmmss).getTime();
				if ((time - new Date().getTime()) > Integer
						.parseInt(filterDate) * 24 * 60 * 60000) {
					return false;
				}
			} catch (Exception e) {

			}
		}
		String email = bean.getMailFrom().trim();
		String display = "";
		if (email.indexOf("<") > 0 && email.indexOf(">") == email.length() - 1) {
			display = email.substring(0, email.indexOf("<"));
			email = email.substring(email.indexOf("<") + 1, email.length() - 1);
		}
		final String theemail = email;
		// 域名过滤
		String filterDomain = paramMap.get("filterDomain");
		if (filterDomain != null && filterDomain.length() > 0
				&& theemail.indexOf("@") > 0) {
			String mailddomain = theemail.substring(theemail.indexOf("@") + 1);
			if ((filterDomain + "  ").matches(".*" + mailddomain
					+ "[;|,|\\s]{1}.*")) {
				return false;
			}
		}

		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						String sql = "";
						sql = " select count(*) from tblMailBlack where email=? ";
						PreparedStatement s = conn.prepareStatement(sql);
						s.setString(1, theemail);
						ResultSet rset = s.executeQuery();
						if (rset.next()) {
							int count = rset.getInt(1);
							if (count > 0) {
								rs.retCode = ErrorCanst.MULTI_VALUE_ERROR;
								return;
							}
						}
					}
				});
				return rs.retCode;
			}
		});
		if (retCode == ErrorCanst.MULTI_VALUE_ERROR) {
			return false; // 被过滤
		}
		return true;
	}

	/**
	 * 查询出当前用户的自定义文件夹
	 * 
	 * @param userId
	 *            String
	 * @return Result
	 */
	public Result selectGroupByUser(final String userId) {
		List param = new ArrayList();
		String sql = "select f_ref from MailinfoSettingUser where userId=?";
		param.add(userId);
		Result rs = sqlList(sql, param);

		String ids = "";
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			for (Object o : (List) rs.retVal) {
				Object[] os = (Object[]) o;
				ids += "'" + os[0] + "',";
			}
		}

		param.clear();
		sql = "";
		if (ids.length() == 0) {
			sql = "select bean.id,bean.groupName,bean.account,mis.account from OAMailGroup bean left join MailinfoSetting mis on  bean.account = mis.id where mis.statusid = 1 and  mis.createby=? "
					+ " union select bean.id,bean.groupName,'','' from OAMailGroup bean where bean.UserId=? and (bean.account is null or bean.account ='') ";

		} else {
			sql = "select bean.id,bean.groupName,bean.account,mis.account from OAMailGroup bean left join MailinfoSetting mis on  bean.account = mis.id where mis.statusid = 1 and( mis.createby=?   or mis.id in ("
					+ ids.substring(0, ids.length() - 1)
					+ "))"
					+ " union select bean.id,bean.groupName,'','' from OAMailGroup bean where bean.UserId=? and (bean.account is null or bean.account ='') ";
		}
		param.add(userId);
		param.add(userId);
		return sqlList(sql, param);
	}

	/**
	 * 查询出当前用户的个人邮箱
	 * 
	 * @param userId
	 *            String
	 * @return Result
	 */
	public Result selectMailAccountByUser(final String userId,final EMailAccountSearchForm searchForm) {
		List param = new ArrayList();
		String sql = "select a.id,a.account,a.mailaddress,b.account mainaccount,a.defaultUser,c.empFullName,a.statusid "
				+ "from MailinfoSetting a left join MailinfoSetting b on b.id=a.mainaccount left join tblemployee c on a.createBy=c.id ";

		if (searchForm.getMainAccount() != null && !"".equals(searchForm.getMainAccount())){
			sql += " where a.mainaccount=?";
			param.add(searchForm.getMainAccount());
		} else {
			sql += "  where (a.mainaccount = '' or a.mainaccount is null) ";
		}
		if (!("1").equals(userId)) {
			sql += " and a.createBy=?";
			param.add(userId);
		}
		if(searchForm.getKeyWord() != null && !"".equals(searchForm.getKeyWord())){
			sql += " and (a.account like '%"+searchForm.getKeyWord()+"%' or a.mailaddress like '%"+searchForm.getKeyWord()+"%' or c.empFullName like '%"+searchForm.getKeyWord()+"%')";
		}
		if(searchForm.getEmailStatus() != null && !"".equals(searchForm.getEmailStatus())){
			sql += " and a.statusid="+searchForm.getEmailStatus();
		}
		return this.sqlList(sql, param, searchForm.getPageSize(), searchForm.getPageNo(), true);
	}

	/**
	 * 查询出当前用户的自定义文件夹
	 * 
	 * @param userId
	 *            String
	 * @return Result
	 */
	public Result statMailByGroup(final String statId, final String userId) {
		Result rs = new Result();
		ArrayList list = new ArrayList();
		List param = new ArrayList();
		// 统计内部
		String sql = "select groupId,count(*) from OAMailInfo where state=0 and account = '' and userId = ? group by groupId ";
		param.add(userId);
		Result rss = sqlList(sql, param);
		if (rss.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<Object[]> ls = (List<Object[]>) rss.retVal;
			for (Object[] os : ls) {
				list.add(new Object[] { "", os[0], os[1] });
			}
		}
		param.clear();
		// 统计外部
		if ("all".equals(statId)) {
			sql = " select groupId,count(*) from OAMailInfo bean join mailinfosetting a on bean.account=a.id where bean.state=0 and a.statusid=1 and  ( a.createBy=? or a.id in (select f_ref from MailinfoSettingUser b where b.userId=? )) group by groupId ";
			param.add(userId);
			param.add(userId);
			rss = sqlList(sql, param);
			if (rss.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				List<Object[]> ls = (List<Object[]>) rss.retVal;
				for (Object[] os : ls) {
					list.add(new Object[] { "all", os[0], os[1] });
				}
			}
		} else {
			String[] stats = statId.split(":");
			for (String stat : stats) {
				if (stat.length() > 0) {
					param.clear();
					sql = "select groupId,count(*) from OAMailInfo where state=0 and account = ? group by groupId ";
					param.add(stat);
					rss = sqlList(sql, param);
					if (rss.retCode == ErrorCanst.DEFAULT_SUCCESS) {
						List<Object[]> ls = (List<Object[]>) rss.retVal;
						for (Object[] os : ls) {
							list.add(new Object[] { stat, os[0], os[1] });
						}
					}
				}
			}
		}
		rs.retVal = list;
		return rs;
	}

	/**
	 * 查询出当前帐号的自定义文件夹
	 * 
	 * @author zxy ,modity by mj
	 * @param userId
	 *            String
	 * @return Result
	 */
	public Result selectGroupByAccount(final String userId, String account) {

		if (!"all".equals(account)) {
			List param = new ArrayList();
			String sql = null;
			if (StringUtils.isBlank(account)) {// 如果是内部邮件
				sql = "select bean.id,bean.groupName,bean.account from OAMailGroup bean left join MailinfoSetting mis on bean.account = mis.id where bean.userId= ? and bean.account = ''";
				param.add(userId);
			} else {
				sql = "select bean.id,bean.groupName,bean.account,mis.account from OAMailGroup bean join MailinfoSetting mis on  bean.account = mis.id where bean.account = ?";
				param.add(account);
			}
			return sqlList(sql, param);
		} else {
			return this.selectGroupByUser(userId);
		}
	}

	/**
	 * 查询出当前用户的自定义文件夹
	 * 
	 * @param userId
	 *            String
	 * @return Result
	 */
	public Result delGroup(final String groupId) {
		// 检查文件夹下是否有文件
		List param = new ArrayList();
		String hql = "select count(bean) from OAMailInfoBean bean where bean.groupId=?";
		param.add(groupId);
		Result rs = list(hql, param);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			Long os = (Long) ((List) rs.getRetVal()).get(0);
			int count = os.intValue();
			if (count == 0) {
				return deleteBean(groupId, OAMailGroupBean.class, "id");
			} else {
				rs.setRetCode(ErrorCanst.DATA_ALREADY_USED);
				return rs;
			}
		} else {
			return rs;
		}
	}

	public Result addGroup(String id, String groupName, String account,
			String userId) {
		OAMailGroupBean bean = new OAMailGroupBean();
		bean.setId(id);
		bean.setAccount(account);
		bean.setGroupName(groupName);
		bean.setUserId(userId);
		return addBean(bean);
	}

	public Result updateGroup(String id, String groupName, String account,
			String userId) {
		OAMailGroupBean bean = new OAMailGroupBean();
		bean.setId(id);
		bean.setAccount(account);
		bean.setGroupName(groupName);
		bean.setUserId(userId);
		//重算手机未读邮件数
		notifyMobile(bean.getUserId());
		return updateBean(bean);
	}

	public Result loadGroup(String id) {
		return loadBean(id, OAMailGroupBean.class);
	}

	public Result sendOuterMailInfter(String userId, String to, String subject,
			String content, Connection conn) {
		Result rs = new Result();
		if (conn != null) {
			rs = this.selectAccountByUser(userId, conn);
		} else {
			rs = this.selectAccountByUser(userId);
		}

		MailinfoSettingBean obj = null;
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			ArrayList list = (ArrayList) rs.retVal;
			for (int i = 0; i < list.size(); i++) {
				MailinfoSettingBean a = (MailinfoSettingBean) list.get(i);
				if (a.getDefaultUser().equals("1")) {
					obj = a;
					break;
				}
			}
		}
		if (null != obj) {
			try {
				AIOEMail sm = new AIOEMail();
				boolean smtpauth = obj.getSmtpAuth() == 1 ? true : false;
				// 指定要使用的邮件服务器
				sm.setAccount(obj.getMailaddress(), obj.getPop3server(), obj
						.getPop3username(), obj.getPop3userpassword(),
						smtpauth, obj.getSmtpserver(), obj.getSmtpusername(),
						obj.getSmtpuserpassword(), obj.getPop3Port(), obj
								.getSmtpPort(), obj.getDisplayName(), obj
								.getId(), obj.getCreateby(),
						obj.getPopssl() == 1 ? true : false,
						obj.getSmtpssl() == 1 ? true : false, obj
								.getAutoAssign() == 1 ? true : false);
				// 指定帐号和密码
				EMailMessage smes = new EMailMessage();
				smes.addRecipient(EMailMessage.TO, to, "");
				smes.setSubject(subject);
				smes.setContent(true, content);
				sm.send(smes);
				String createTime = BaseDateFormat.format(new Date(),
						BaseDateFormat.yyyyMMddHHmmss);
				// 将发送的数据保存至数据库
				Result rest;
				if (conn != null) {
					rest = this.addMail(userId, to, subject, createTime,
							content, 1, "", userId, obj.getId());
				} else {
					rest = this.addMail(userId, to, subject, createTime,
							content, 1, "", userId, obj.getId());
				}
				rs.setRetCode(rest.getRetCode());
			} catch (Exception ex) {
				ex.printStackTrace();
				rs.setRetCode(ErrorCanst.SEND_EMAIL_ERROR);
				rs.setRetVal("common.msg.sendError");
				return rs;

			}
		} else {
			rs.setRetCode(ErrorCanst.NOT_SET_EMAIL_ACCOUNT);
			rs.setRetVal("com.oa.notsetMail");
			return rs;

		}
		return rs;
	}

	/**
	 * 查询出当前用户的默认账户
	 * 
	 * @param userId
	 *            String
	 * @return Result
	 */
	public Result selectAccountByUser(final String userId, Connection conn) {
		Result rst = new Result();
		try {
			PreparedStatement pstmt = conn
					.prepareStatement("select * from MailinfoSetting where 1=1 and createBy=?");
			pstmt.setString(1, userId);
			ResultSet rs = pstmt.executeQuery();
			ArrayList list = new ArrayList();
			while (rs.next()) {
				MailinfoSettingBean obj = new MailinfoSettingBean();
				obj.setId(rs.getString("id"));
				obj.setMailaddress(rs.getString("MailAddress"));
				obj.setPop3server(rs.getString("Pop3server"));
				obj.setPop3username(rs.getString("Pop3UserName"));
				obj.setPop3userpassword(rs.getString("Pop3UserPassWord"));
				obj.setSmtpserver(rs.getString("Smtpserver"));
				obj.setSmtpusername(rs.getString("SmtpUserName"));
				obj.setSmtpuserpassword(rs.getString("SmtpUserPassWord"));
				obj.setCreateby(rs.getString("createBy"));
				obj.setLastupdateby(rs.getString("lastUpdateBy"));
				obj.setCreatetime(rs.getString("createTime"));
				obj.setLastupdatetime(rs.getString("lastUpdateTime"));
				obj.setScompanyid(rs.getString("SCompanyID"));
				obj.setSmtpAuth(rs.getInt("SmtpAuth"));
				obj.setPop3Port(rs.getInt("pop3Port"));
				obj.setSmtpPort(rs.getInt("smtpPort"));
				list.add(obj);
			}
			rst.setRetVal(list);
		} catch (Exception ex) {
			ex.printStackTrace();
			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		}
		return rst;
	}

	public Result addMail(String fromUserId, String mailTo, String mailTitle,
			String createTime, String mailContent, int emailType,
			String attaches, String userId, String account) {
		// 新增邮件信息
		OAMailInfoBean info = new OAMailInfoBean();
		String id = IDGenerater.getId();
		info.setId(id);
		info.setFromUserId(fromUserId);
		info.setCreateTime(createTime);
		info.setMailTitle(mailTitle);
		info.setMailTime(createTime);
		info.setMailContent(mailContent);
		info.setMailAttaches(attaches);
		info.setEmailType(emailType);
		info.setGroupId("3");
		info.setMailTo(mailTo);
		info.setState(1);
		info.setUserId(userId);
		info.setAccount(account);

		Result rs = this.addMail(info);
		return rs;
	}

//	 这方法没人用，如果要启用的话，自己想办法调用notifyMobile来通知手机
//	public Result addMail(String fromUserId, String mailTo, String mailTitle,
//			String createTime, String mailContent, int emailType,
//			String attaches, String userId, String account, Connection conn) {
//		Result rs = new Result();
//		try {
//			// 新增邮件信息
//
//			String id = IDGenerater.getId();
//			PreparedStatement pstmt = conn
//					.prepareStatement("insert into OAMailInfo (id,fromUserId,createTime,mailTitle,"
//							+ "mailContent,mailAttaches,emailType,groupId,mailTo,state,userId,account,mailuid) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
//			pstmt.setString(1, id);
//			pstmt.setString(2, fromUserId);
//			pstmt.setString(3, createTime);
//			pstmt.setString(4, mailTitle);
//			pstmt.setString(5, mailContent);
//			pstmt.setString(6, attaches);
//			pstmt.setInt(7, emailType);
//			pstmt.setInt(8, 3);
//			pstmt.setString(9, mailTo);
//			pstmt.setInt(10, 1);
//			pstmt.setString(11, userId);
//			pstmt.setString(12, account);
//			pstmt.setString(13, id);
//			pstmt.executeUpdate();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//		}
//
//		return rs;
//	}

	/**
	 * 导出
	 * 
	 * @param keyId
	 * @return
	 */
	public Result export(String keyId[], String userId, String str) {
		String path = "/email/export/"
				+ "EMAIL_"
				+ BaseDateFormat.format(new Date(),
						BaseDateFormat.yyyyMMddHHmmss2) + ".zip";
		if (keyId == null || keyId.length == 0)
			return new Result();

		List param = new ArrayList();
		String keys = "";
		for (String s : keyId) {
			keys = keys + ",'" + s + "'";
		}
		String hql = "select bean from OAMailInfoBean bean  where  id in ("
				+ keys.substring(1) + ") ";

		// param.add(keys.substring(1));
		Result rs = list(hql, param);
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return rs;
		} else {

			File f = new File(BaseEnv.FILESERVERPATH + path);
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			FileOutputStream dest = null;
			try {
				dest = new FileOutputStream(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					dest));
			out.setEncoding("GBK");
			AIOEMail aioEmail = new AIOEMail();

			for (Object o : (ArrayList) rs.retVal) {
				OAMailInfoBean bean = (OAMailInfoBean) o;

				EMailMessage email = new EMailMessage();
				email.setSubject(bean.getMailTitle().length() > 15 ? bean
						.getMailTitle().substring(0, 14) : bean.getMailTitle()); // 标题
				email.setContent(bean.getMailContent());// 正文
				email.setMessageId(bean.getMailUID());
				email.setIsContentHtml(true);
				if (bean.getAccount() != null && !"".equals(bean.getAccount())) {
					Result a = this.selectMailAddressById(bean.getAccount());
					ArrayList l = (ArrayList) a.retVal;
					String fromEmail = ((MailinfoSettingBean) l.get(0))
							.getMailaddress();
					aioEmail.setEmailAddress(fromEmail);
				} else {
					ArrayList login = (ArrayList) this.selectLoginNameById(bean
							.getFromUserId()).retVal;
					String loginName = ((EmployeeBean) login.get(0))
							.getSysName();
					aioEmail.setEmailAddress(loginName);
				}
				if (bean.getMailTo() != null) {
					String[] emailTo = bean.getMailTo().split(";");

					for (int i = 0; i < emailTo.length; i++) {
						if (emailTo[i].indexOf('<') == -1) {
							email.addRecipient(0, "", emailTo[i]);
						} else {
							String emailToName = emailTo[i].substring(0,
									emailTo[i].indexOf('<'));
							String emailAddress = emailTo[i].substring(
									emailTo[i].indexOf('<') + 1, emailTo[i]
											.length() - 1);
							email.addRecipient(0, emailAddress, emailToName);
						}
					}
				}

				if (bean.getMailBCc() != null) {
					String[] emailBcc = bean.getMailBCc().split(";");

					for (int i = 0; i < emailBcc.length; i++) {
						if (emailBcc[i].indexOf('<') == -1) {
							email.addRecipient(2, "", emailBcc[i]);

						} else {
							String emailToName = emailBcc[i].substring(0,
									emailBcc[i].indexOf('<'));
							String emailAddress = emailBcc[i].substring(
									emailBcc[i].indexOf('<') + 1, emailBcc[i]
											.length() - 1);
							email.addRecipient(2, emailAddress, emailToName);
						}
					}
				}

				if (bean.getMailCc() != null) {
					String[] emailcc = bean.getMailCc().split(";");

					for (int i = 0; i < emailcc.length; i++) {
						if (emailcc[i].indexOf('<') == -1) {
							email.addRecipient(1, "", emailcc[i]);

						} else {
							String emailToName = emailcc[i].substring(0,
									emailcc[i].indexOf('<'));
							String emailAddress = emailcc[i].substring(
									emailcc[i].indexOf('<') + 1, emailcc[i]
											.length() - 1);
							email.addRecipient(1, emailAddress, emailToName);
						}
					}
				}

				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				try {
					email.setDate(df.parse(bean.getMailTime()));
				} catch (ParseException e1) {
					// TODO Auto-generated catch blockg
					AIOEMail.emailLog.error(str);
				}

				String attr = bean.getMailAttaches() == null ? "" : bean
						.getMailAttaches();
				String[] atts = attr.split(";");
				for (String at : atts) {
					if (at != null && at.length() > 0) {
						String attachPath = bean.getAccount();
						if (bean.getEmailType() == 0) {
							attachPath = "inner" + bean.getUserId();
						}
						File aFile = new File(BaseEnv.FILESERVERPATH
								+ "/email/" + attachPath + "/" + at);
						email.putFile(at, aFile);
					}
				}

				ZipEntry entry = new ZipEntry(email.getSubject() + ".eml");

				try {
					out.putNextEntry(entry);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					aioEmail.saveFile(email, out);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		rs.retVal = path;
		return rs;
	}

	public int[] importFromAIO(ImportForm form, final String account,
			final String group, final String userId) {
		Result rs = new Result();

		final ArrayList<OAMailInfoBean> beanList = new ArrayList<OAMailInfoBean>();
		final HashMap<String, String> updateAttach = new HashMap<String, String>();// 用于存储改名的附件
		final ArrayList<File> saveFile = new ArrayList<File>();// 用于存储已经保存的文件
		int dubleMail = 0;
		int rightMail = 0;
		int failMail = 0;

		String aPath = BaseEnv.FILESERVERPATH + "/email/" + account + "/";

		// 由于apache的解压缩工具没有ZipInputStream类，只有ZipFile所以这里先把流写入文件，然后再由ZipFile读取
		// java提供的ZipImputStream类不支持中文。
		String cfileName = BaseEnv.FILESERVERPATH + "/email/temp/"
				+ System.currentTimeMillis() + ".zip";
		File cfile = new File(cfileName);
		if (form.getFileType().equals("zip")) {
			try {
				if (!cfile.getParentFile().exists()) {
					cfile.getParentFile().mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(cfile);
				fos.write(form.getFile().getFileData());
				fos.flush();
				fos.close();

				File ap = new File(aPath);
				if (!ap.exists()) {
					ap.mkdirs();
				}
			} catch (Exception es) {

			}
		} else {
			cfileName = form.getPath();
		}

		try {
			ZipFile zf = new ZipFile(cfileName, "GBK");// 支持中文
			Enumeration e = zf.getEntries();

			while (e.hasMoreElements()) {
				try {
					ZipEntry ze2 = (ZipEntry) e.nextElement();
					String entryName = ze2.getName();
					if (!ze2.isDirectory()) {
						entryName = java.net.URLDecoder.decode(entryName);
						if (entryName.endsWith(".eml")
								|| entryName.endsWith(".aioeml")) {
							BufferedInputStream bi = new BufferedInputStream(zf
									.getInputStream(ze2));
							byte[] readContent = new byte[1024];
							int readCount = 0;
							byte[] bs = new byte[0];
							while ((readCount = bi.read(readContent)) > -1) {
								byte[] temp = bs;
								bs = new byte[temp.length + readCount];
								System.arraycopy(temp, 0, bs, 0, temp.length);
								System.arraycopy(readContent, 0, bs,
										temp.length, readCount);
							}
							String str = new String(bs, "UTF-8");
							OAMailInfoBean bean = OAMailInfoBean
									.fromString(str);
							if(StringUtils.isNotBlank(bean.getId())){//mj
							
								beanList.add(bean);
							}
						} else if (entryName.startsWith("attach")) {
							// fileName前加时间
							String afileName = entryName.substring(entryName
									.indexOf("/") + 1);
							String oldafileName = afileName;
							File afile = new File(aPath, afileName);
							if (!afile.getParentFile().exists()) {
								afile.getParentFile().mkdirs();
							}
							for (int i = 1; afile.exists() && i < 1000; i++) {
								// 文件名存在，改名
								if (oldafileName.indexOf(".") > -1) {
									afileName = oldafileName.substring(0,
											oldafileName.lastIndexOf("."))
											+ "("
											+ i
											+ ")"
											+ oldafileName
													.substring(oldafileName
															.lastIndexOf("."));
								} else {
									afileName = oldafileName + "(" + i + ")";
								}
								afile = new File(aPath, afileName);
							}
							if (!oldafileName.equals(afileName)) {
								// 文件被改名
								updateAttach.put(oldafileName, afileName);
							}

							FileOutputStream writer = new FileOutputStream(
									afile);
							BufferedInputStream bi = new BufferedInputStream(zf
									.getInputStream(ze2));
							byte[] readContent = new byte[1024];
							int readCount = bi.read(readContent);
							while (readCount != -1) {
								writer.write(readContent, 0, readCount);
								readCount = bi.read(readContent);
							}
							writer.close();
							saveFile.add(afile);
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			zf.close();
		} catch (Exception e) {
			rs.retCode = ErrorCanst.DEFAULT_FAILURE;
			e.printStackTrace();
		}
		if (cfile.exists()) { // 删除临时文件
			cfile.delete();
		}

		for (final OAMailInfoBean bean : beanList) {
			boolean isRight = false;
			boolean isEx = this.isExist(account, bean.getMailUID());
			bean.setPId("1");
			bean.setRelationId("2");
			
			if (isEx) {
				dubleMail++;
			} else {
				int retCode = DBUtil.execute(new IfDB() {
					public int exec(Session session) {
						// 开始做数据存储工作
						// 检查bean中是否有附件改名
						for (String str : updateAttach.keySet()) {
							if (bean.getMailAttaches() != null
									&& bean.getMailAttaches().indexOf(str) > -1) {
								bean.setMailAttaches(bean.getMailAttaches()
										.replaceAll(str + ";",
												updateAttach.get(str) + ";"));
							}
							if (bean.getInnerAttaches() != null
									&& bean.getInnerAttaches().indexOf(str) > -1) {
								bean.setInnerAttaches(bean.getInnerAttaches()
										.replaceAll(str + ";",
												updateAttach.get(str) + ";"));
							}
							if (bean.getMailContent() != null
									&& bean.getMailContent().indexOf(str) > -1) {
								bean
										.setMailContent(bean.getMailContent()
												.replaceAll(str,
														updateAttach.get(str)));
							}
						}
						bean.setMailContent(bean.getMailContent().replaceAll(
								"fileName=" + bean.getAccount(),
								"fileName=" + account));
						bean.setAccount(account);
						bean.setGroupId(group);
						bean.setUserId(userId);
						bean.setId(IDGenerater.getId());
						addBean(bean, session);

						return ErrorCanst.DEFAULT_SUCCESS;
					}
				});
				if (retCode != ErrorCanst.DEFAULT_SUCCESS) {
					failMail++;
				} else {
					rightMail++;
					isRight = true;
				}
			}
			if (!isRight) {
				// 导常时，删除有附件
				String att = bean.getMailAttaches();
				if (att != null && att.length() > 0) {
					String af[] = att.split(";");
					for (String fn : af) {
						if (fn != null && fn.length() > 0) {
							File ff = new File(aPath, fn);
							ff.delete();
						}
					}
				}
				att = bean.getInnerAttaches();
				if (att != null && att.length() > 0) {
					String af[] = att.split(";");
					for (String fn : af) {
						if (fn != null && fn.length() > 0) {
							File ff = new File(aPath, fn);
							ff.delete();
						}
					}
				}
			}
		}
		
		//重算手机未读邮件数
		notifyMobile(userId);
		return new int[] { rightMail, dubleMail, failMail };
	}

	public int[] importStandand(ImportForm form, final String account,
			final String group, final String userId) {
		Result rs = new Result();

		final ArrayList<OAMailInfoBean> beanList = new ArrayList<OAMailInfoBean>();
		final HashMap<String, String> updateAttach = new HashMap<String, String>();// 用于存储改名的附件
		final ArrayList<File> saveFile = new ArrayList<File>();// 用于存储已经保存的文件

		int dubleMail = 0;
		int rightMail = 0;
		int failMail = 0;

		if (form.getFileType().equals("zip")) {
			// 由于apache的解压缩工具没有ZipInputStream类，只有ZipFile所以这里先把流写入文件，然后再由ZipFile读取
			// java提供的ZipImputStream类不支持中文。
			String cfileName = BaseEnv.FILESERVERPATH + "/email/temp/"
					+ System.currentTimeMillis() + ".zip";
			File cfile = new File(cfileName);
			try {

				if (!cfile.getParentFile().exists()) {
					cfile.getParentFile().mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(cfile);
				fos.write(form.getFile().getFileData());
				fos.flush();
				fos.close();

				String aPath = BaseEnv.FILESERVERPATH + "/email/" + account
						+ "/";
				File ap = new File(aPath);
				if (!ap.exists()) {
					ap.mkdirs();
				}

				ZipFile zf = new ZipFile(cfileName, "GBK");// 支持中文
				Enumeration e = zf.getEntries();
				while (e.hasMoreElements()) {
					try {
						ZipEntry ze2 = (ZipEntry) e.nextElement();
						String entryName = ze2.getName();
						if (entryName.endsWith(".eml")) {
							BufferedInputStream bi = new BufferedInputStream(zf
									.getInputStream(ze2));

							try {
								AIOEMail ae = new AIOEMail();
								ae.setAccount("", "", "", "", true, "", "", "",
										0, 0, "", account, "", true, true,
										false);
								String messageId = ae.readFileHeader(bi);//为“” 这个感觉有问题。
								// 检查邮件是否存在.
								boolean isEx = this.isExist(account, messageId);
								if (isEx) {
									dubleMail++;
								} else {
									EMailMessage em = ae.readFileBody();
									boolean bol = convertMail(userId, account,
											em,group);//mj 
									if (bol) {
										rightMail++;
									} else {
										failMail++;
									}
								}
							} catch (Exception e1) {
								e1.printStackTrace();
								failMail++;
							}
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				zf.close();
			} catch (Exception e) {
				rs.retCode = ErrorCanst.DEFAULT_FAILURE;
				e.printStackTrace();
			}
			if (cfile.exists()) { // 删除临时文件
				// cfile.delete();
			}
		} else if (form.getFileType().equals("eml")) {
			try {
				AIOEMail ae = new AIOEMail();
				ae.setAccount("", "", "", "", true, "", "", "", 0, 0, "",
						account, "", true, true, false);
				String messageId = ae.readFileHeader(form.getFile()
						.getInputStream());
				// 检查邮件是否存在.
				boolean isEx = this.isExist(account, messageId);
				if (isEx) {
					dubleMail++;
				} else {
					EMailMessage em = ae.readFileBody();
					String fileName = form.getFile().getFileName();
					em.setFileName(fileName);
					boolean bol = convertMail(userId, account, em,group);
					if (bol) {
						//保存 eml文件
						//mj beg
						String emlFile = fileName;
						String path  = BaseEnv.FILESERVERPATH+"/email/"+account+"/eml/";		  
	                    File cfile = new File(path, emlFile);  
	        			try {
	        				if (!cfile.getParentFile().exists()) {
	        					cfile.getParentFile().mkdirs();
	        				}
	        				FileOutputStream fos = new FileOutputStream(cfile);
	        				//fos.write(form.getFile().getFileData());
	                        InputStream is = form.getFile().getInputStream();
	                        byte[] buffer = new byte[1*1024*1024];
	                        int length = 0;
	                        while ((length = is.read(buffer)) > 0) {
	                        	fos.write(buffer, 0, length);
	                        }
	                        fos.close();
	                        is.close();
                        }catch (Exception e) {
							// TODO: handle exception
                        	e.printStackTrace();
						}
                        //mj end
						rightMail++;
					} else {
						failMail++;
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				failMail++;
			}
		} else {
			AIOEMail ae = new AIOEMail();
			ae.setAccount("", "", "", "", true, "", "", "", 0, 0, "", account,
					"", true, true, false);

			File path = new File(form.getPath());
			File[] fileList = path.listFiles();
			for (int i = 0; fileList != null && i < fileList.length; i++) {
				if (!fileList[i].getName().endsWith("eml"))
					continue;
				try {
					String messageId = ae.readFileHeader(new FileInputStream(
							fileList[i]));
					// 检查邮件是否存在.
					boolean isEx = this.isExist(account, messageId);
					if (isEx) {
						dubleMail++;
					} else {
						EMailMessage em = ae.readFileBody();
						boolean bol = convertMail(userId, account, em ,group);
						if (bol) {
							rightMail++;
						} else {
							failMail++;
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					failMail++;
				}
			}
		}

		return new int[] { rightMail, dubleMail, failMail };
	}

	public static boolean convertMail(String userId, String emailType,
			EMailMessage em ,String groupId) {
		try {
			// 保存邮件
			OAMailInfoBean mailInfo = new OAMailInfoBean();
			mailInfo.setBegReplay(em.isBegReplay() ? "1" : "0");
			mailInfo.setId(IDGenerater.getId());
			mailInfo.setCreateTime(BaseDateFormat.format(new Date(),
					BaseDateFormat.yyyyMMddHHmmss));
			mailInfo.setEmailType(1);
			// mailInfo.setFromUserId();
			// 附件
			String mailAttaches = "";
			Map map_file = em.getFiles();
			if (map_file != null) {
				Set set = map_file.keySet();
				Iterator iterator = set.iterator();
				while (iterator.hasNext()) {
					String fileName = iterator.next().toString();
					mailAttaches += fileName + ";";
				}
			}
			
			mailInfo.setMailAttaches(mailAttaches);

			String innerAttaches = "";
			if (em.getContentIdList() != null) {
				for (Object o : em.getContentIdList()) {
					String[] strs = (String[]) o;
					innerAttaches += strs[1];
				}
			}
			mailInfo.setInnerAttaches(innerAttaches);

			// 接收人
			String mto = "";
			String mcc = "";
			String mbcc = "";
			for (EMailMessage.Recipienter rc : em.getRecipientList()) {
				if (rc.type == EMailMessage.TO) {
					mto += rc.toString() + ";";
				} else if (rc.type == EMailMessage.CC) {
					mcc += rc.toString() + ";";
				} else if (rc.type == EMailMessage.BCC) {
					mbcc += rc.toString() + ";";
				}
			}
			mailInfo.setGroupId(groupId);
			mailInfo.setMailCc(mcc);
			mailInfo.setMailTo(mto);
			mailInfo.setMailBCc(mbcc);
			mailInfo.setEmlfile(em.getFileName());//mj 将文件名保存
			mailInfo.setMailContent(em.getContent());
			mailInfo.setMailFrom(em.getSender() != null ?em.getSender().toString():null);
			mailInfo.setMailTime(BaseDateFormat.format(
					em.getDate() == null ? new Date() : em.getDate(),
					BaseDateFormat.yyyyMMddHHmmss));
			mailInfo.setMailTitle(em.getSubject());
			mailInfo.setState(0); // 新邮件
			mailInfo.setToUserId(userId); // 发送给自已的邮件
			mailInfo.setUserId(userId);
			mailInfo.setAccount(emailType);
			mailInfo.setMailSize(em.getMailSize());
			mailInfo.setMailUID(em.getMessageId());
		
			// 保存到数据库中去
			EMailMgt mgt = new EMailMgt();

			mailInfo.setGroupId(mgt.filterMailFrom(mailInfo) ? "1" : "4"); // 进行规则过滤

			Result rs = mgt.addMail(mailInfo);

			return rs.retCode == ErrorCanst.DEFAULT_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			// AIOEMail.emailLog.error("MailAutoReceive.receiveContent Error", e);
			return false;
		}

	}

	public boolean isExist(String account, String uid) {
		ArrayList param = new ArrayList();
		String sql = "select count(*) from oamailinfo where  account=? and mailuid =?";
		param.add(account);
		param.add(uid);
		Result rs = sqlList(sql, param);

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if (((Integer) ((Object[]) (((ArrayList) rs.retVal).get(0)))[0]) > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 导出
	 * 
	 * @param keyId
	 * @return
	 */
	public Result exportToAIO(String keyId[], String userId) {
		String path = "/email/export/"
				+ "EMAIL_"
				+ BaseDateFormat.format(new Date(),
						BaseDateFormat.yyyyMMddHHmmss2) + ".zip";
		if (keyId == null || keyId.length == 0)
			return new Result();

		Result rs = new Result();

		File f = new File(BaseEnv.FILESERVERPATH + path);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		FileOutputStream dest = null;
		try {
			dest = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ZipOutputStream out = new ZipOutputStream(
				new BufferedOutputStream(dest));
		out.setEncoding("GBK");

		while (keyId.length > 0) {
			List param = new ArrayList();
			String keys = "";
			for (int i = 0; i < keyId.length && i < 100; i++) {
				keys = keys + ",'" + keyId[i] + "'";
			}
			if (keyId.length > 100) {
				String[] temp = keyId;
				keyId = new String[temp.length - 100];
				System.arraycopy(temp, 100, keyId, 0, temp.length - 100);
			} else {
				keyId = new String[0];
			}
			String hql = "select bean from OAMailInfoBean bean  where  id in ("
					+ keys.substring(1) + ") ";
			rs = list(hql, param);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				for (Object o : (ArrayList) rs.retVal) {
					OAMailInfoBean bean = (OAMailInfoBean) o;
					try {
						// 嵌入式图片
						// 邮件转发中的图片
						Pattern rfp = Pattern.compile("(/ReadFile[^\\s\\\"]*)",
								Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
						Matcher mat = rfp.matcher(bean.getMailContent());
						int i = 0;
						while (mat.find()) {
							String readFile = mat.group(1);
							File aFile = null;
							if (readFile.indexOf("email") > -1) {
								String fileName = readFile.substring(readFile
										.indexOf("fileName=") + 9);
								fileName = java.net.URLDecoder.decode(fileName,
										"UTF-8");
								aFile = new File(BaseEnv.FILESERVERPATH
										+ "/email/" + fileName);
							}
							ZipEntry entry = new ZipEntry("attach/"
									+ readFile.substring(readFile
											.lastIndexOf("/") + 1));
							out.putNextEntry(entry);
							copyFile(aFile, out);
						}

						// 查有没有编辑器上传的附件
						rfp = Pattern.compile("src=\\\"(/upload/[^\\s\\\"]*)",
								Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
						mat = rfp.matcher(bean.getMailContent());
						while (mat.find()) {
							String readFile = mat.group(1);
							bean.setMailContent(bean.getMailContent()
									.substring(
											0,
											bean.getMailContent().indexOf(
													"src=\"" + readFile) + 5)
									+ "/ReadFile?tempFile=email&amp;fileName="
									+ bean.getAccount()
									+ "/"
									+ readFile.substring("/upload/".length())
									+ bean.getMailContent().substring(
											bean.getMailContent().indexOf(
													"src=\"" + readFile)
													+ ("src=\"" + readFile)
															.length()));
							// 附件

							File aFile = new File(BaseEnv.systemRealPath,
									readFile);
							ZipEntry entry = new ZipEntry("attach/"
									+ readFile.substring("/upload/".length()));
							out.putNextEntry(entry);
							copyFile(aFile, out);
						}

						ZipEntry entry = new ZipEntry(bean.getMailTitle()
								+ ".aioeml");
						out.putNextEntry(entry);
						out.write(bean.toString().getBytes("UTF-8"));

						// 附件
						String attr = bean.getMailAttaches() == null ? ""
								: bean.getMailAttaches();
						String[] atts = attr.split(";");
						for (String at : atts) {
							if (at != null && at.length() > 0) {
								String attachPath = bean.getAccount();
								if (bean.getEmailType() == 0) {
									attachPath = "inner" + bean.getUserId();
								}
								File aFile = new File(BaseEnv.FILESERVERPATH
										+ "/email/" + attachPath + "/" + at);
								entry = new ZipEntry("attach/" + at);
								out.putNextEntry(entry);
								copyFile(aFile, out);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

		}
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		rs.retVal = path;
		return rs;
	}

	/**
	 * 导出
	 * 
	 * @param keyId
	 * @return
	 */
	public Result exportToEml(String keyId[], String userId) {
		String path = "/email/export/"
				+ "EMAIL_"
				+ BaseDateFormat.format(new Date(),
						BaseDateFormat.yyyyMMddHHmmss2) + ".zip";
		if (keyId == null || keyId.length == 0)
			return new Result();

		Result rs = new Result();

		File f = new File(BaseEnv.FILESERVERPATH + path);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		FileOutputStream dest = null;
		try {
			dest = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ZipOutputStream out = new ZipOutputStream(
				new BufferedOutputStream(dest));
		out.setEncoding("GBK");

		while (keyId.length > 0) {
			try {
				List param = new ArrayList();
				String keys = "";
				for (int i = 0; i < keyId.length && i < 100; i++) {
					keys = keys + ",'" + keyId[i] + "'";
				}
				if (keyId.length > 100) {
					String[] temp = keyId;
					keyId = new String[temp.length - 100];
					System.arraycopy(temp, 100, keyId, 0, temp.length - 100);
				} else {
					keyId = new String[0];
				}
				String hql = "select bean from OAMailInfoBean bean  where  id in ("
						+ keys.substring(1) + ") ";
				rs = list(hql, param);
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					for (Object o : (ArrayList) rs.retVal) {
						try {
							OAMailInfoBean bean = (OAMailInfoBean) o;
							AIOEMail em = new AIOEMail();

							ZipEntry entry = new ZipEntry(bean.getMailTitle()
									+ ".eml");
							out.putNextEntry(entry);
							if (bean.getEmlfile() != null
									&& !bean.getEmlfile().equals("")) {
								File of = new File(BaseEnv.FILESERVERPATH
										+ "/email/" + bean.getAccount()
										+ "/eml/" + bean.getEmlfile());
								FileInputStream fs = new FileInputStream(of);
								byte[] bs = new byte[1024];
								int count = 0;
								while ((count = fs.read(bs)) > -1) {
									out.write(bs, 0, count);
								}
								fs.close();
							} else {
								em.saveFile(convertEMailMessage(bean), out);
							}
							System.out.println(bean.getMailTitle());
						} catch (Exception ex) {
						}
					}

				}
			} catch (Exception ex) {

			}

		}
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		rs.retVal = path;
		return rs;
	}

	public static EMailMessage convertEMailMessage(OAMailInfoBean em) {
		try {
			String path = "/email/" + em.getAccount() + "/";
			// 保存邮件
			EMailMessage mailInfo = new EMailMessage();
			mailInfo.setBegReplay(em.getBegReplay() == "1" ? true : false);
			// 附件
			if (em.getMailAttaches() != null
					&& em.getMailAttaches().length() > 0) {
				String ma[] = em.getMailAttaches().split(";");
				for (String mstr : ma) {
					mailInfo.getFiles().put(mstr,
							new File(BaseEnv.FILESERVERPATH + path + mstr));
				}
			}

			if (em.getMailCc() != null && em.getMailCc().length() > 0) {
				String[] mms = em.getMailCc().split(";");
				for (String mm : mms) {
					String emailAddress = mm;
					String display = "";
					if (emailAddress.indexOf("<") > 0) {
						display = emailAddress.substring(0, emailAddress
								.indexOf("<"));
						emailAddress = emailAddress.substring(emailAddress
								.indexOf("<") + 1, emailAddress.indexOf(">"));
					}
					mailInfo.addRecipient(EMailMessage.CC, emailAddress,
							display);
				}
			}
			if (em.getMailBCc() != null && em.getMailBCc().length() > 0) {
				String[] mms = em.getMailBCc().split(";");
				for (String mm : mms) {
					String emailAddress = mm;
					String display = "";
					if (emailAddress.indexOf("<") > 0) {
						display = emailAddress.substring(0, emailAddress
								.indexOf("<"));
						emailAddress = emailAddress.substring(emailAddress
								.indexOf("<") + 1, emailAddress.indexOf(">"));
					}
					mailInfo.addRecipient(EMailMessage.BCC, emailAddress,
							display);
				}
			}
			if (em.getMailTo() != null && em.getMailTo().length() > 0) {
				String[] mms = em.getMailTo().split(";");
				for (String mm : mms) {
					String emailAddress = mm;
					String display = "";
					if (emailAddress.indexOf("<") > 0) {
						display = emailAddress.substring(0, emailAddress
								.indexOf("<"));
						emailAddress = emailAddress.substring(emailAddress
								.indexOf("<") + 1, emailAddress.indexOf(">"));
					}
					mailInfo.addRecipient(EMailMessage.TO, emailAddress,
							display);
				}
			}

			mailInfo.setContent(true, em.getMailContent());
			String emailAddress = em.getMailFrom();
			String display = "";
			if (emailAddress.indexOf("<") > 0) {
				display = emailAddress.substring(0, emailAddress.indexOf("<"));
				emailAddress = emailAddress.substring(
						emailAddress.indexOf("<") + 1, emailAddress
								.indexOf(">"));
			}
			mailInfo.setSender(mailInfo.newRecipienter(EMailMessage.TO,
					emailAddress, display));
			mailInfo.setDate(BaseDateFormat.parse(em.getMailTime(),
					BaseDateFormat.yyyyMMddHHmmss));
			mailInfo.setSubject(em.getMailTitle());
			return mailInfo;
		} catch (Exception e) {
			// AIOEMail.emailLog.error("MailAutoReceive.receiveContent Error", e);
			return null;
		}

	}

	private void copyFile(File src, OutputStream os) throws Exception {
		FileInputStream fis = new FileInputStream(src);
		byte[] bs = new byte[1024];
		int count = 0;
		while ((count = fis.read(bs)) > -1) {
			os.write(bs, 0, count);
		}
		fis.close();
	}

	/**
	 * 根据 MailinfoSetting id得到地址
	 * 
	 * @return
	 */
	public Result selectMailAddressById(String id) {
		List param = new ArrayList();
		String hql = "select bean from MailinfoSettingBean bean  where  bean.id = ?";
		param.add(id);

		return list(hql, param);
	}

	public Result selectLoginNameById(String id) {
		List param = new ArrayList();
		String hql = "select bean from EmployeeBean bean  where  bean.id = ?";
		param.add(id);

		return list(hql, param);
	}

	public Result getMailReplyAccount(final String AccountID) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {

						Connection conn = connection;
						String sql = "select id from tblMailReplyAccount where AccountID='"
								+ AccountID + "'";
						Statement s = conn.createStatement();
						ResultSet rst = s.executeQuery(sql);
						if (rst.next())
							rs.setRetVal(rst.getString(1));

					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	public Result getMailReplyAccountDet(final String MailReplyAccountId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {

						Connection conn = connection;
						// 要除去，自己无权限，或无共享的帐户
						String sql = "select MailinfoSetting.id ,MailinfoSetting.account from tblMailReplyAccountDet,MailinfoSetting "
								+ "where tblMailReplyAccountDet.ReplyAccount=MailinfoSetting.id and tblMailReplyAccountDet.f_ref='"
								+ MailReplyAccountId + "'";
						Statement s = conn.createStatement();
						ResultSet rst = s.executeQuery(sql);
						ArrayList list = new ArrayList();
						while (rst.next()) {
							String[] str = new String[2];
							str[0] = rst.getString(1);
							str[1] = rst.getString(2);
							list.add(str);
						}
						rs.setRetVal(list);
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);

		return rs;
	}

	public Result mailMatch(final MailinfoSettingBean bean) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						// 取邮件域名
						String area = bean.getMailaddress();
						area = area.substring(area.indexOf("@") + 1);
						String sql = "select * from MailSettingTemp where baseAddress='"
								+ area + "'";
						Statement s = conn.createStatement();
						ResultSet rst = s.executeQuery(sql);
						ArrayList list = new ArrayList();
						if (rst.next()) {
							bean.setPop3server(rst.getString("Pop3server"));
							// Pop3UserName 为2时表示帐户名带有@域名，为1表不不带域名
							if ("2".equals(rst.getString("Pop3UserName"))) {
								bean.setPop3username(bean.getMailaddress()
										.substring(
												0,
												bean.getMailaddress().indexOf(
														"@")));
							} else {
								bean.setPop3username(bean.getMailaddress());
							}
							bean.setSmtpserver(rst.getString("Smtpserver"));
							if ("2".equals(rst.getString("SmtpUserName"))) {
								bean.setSmtpusername(bean.getMailaddress()
										.substring(
												0,
												bean.getMailaddress().indexOf(
														"@")));
							} else {
								bean.setSmtpusername(bean.getMailaddress());
							}
							bean.setSmtpAuth(rst.getInt("SmtpAuth"));
							bean.setPop3Port(rst.getInt("Pop3Port"));
							bean.setSmtpPort(rst.getInt("SmtpPort"));
							bean.setPopssl(rst.getInt("popssl"));
							bean.setSmtpssl(rst.getInt("smtpssl"));
							bean.setSmtpsamepop(rst.getInt("smtpsamepop"));
						}
						rs.setRetVal(list);
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);

		return rs;
	}

	public Result getALLMailinfoSetting(final String userId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {

						String sql = "select id,account,defaultUser from MailinfoSetting where statusid=1 and (mainaccount = '' or mainaccount is null) and ( createby=? or id in (select f_ref from MailinfoSettingUser where userId=? ))";

						PreparedStatement s = connection.prepareStatement(sql);
						s.setString(1, userId);
						s.setString(2, userId);
						ResultSet rst = s.executeQuery();
						ArrayList list = new ArrayList();
						while (rst.next()) {
							String[] str = new String[3];
							str[0] = rst.getString(1);
							str[1] = rst.getString(2);
							str[2] = rst.getString(3);
							list.add(str);
						}
						rs.setRetVal(list);

					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	public Result getALLMailinfoSetting() {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {

						String sql = "select id,account,isCorporationEmail from MailinfoSetting";

						PreparedStatement s = connection.prepareStatement(sql);

						ResultSet rst = s.executeQuery();
						ArrayList list = new ArrayList();
						while (rst.next()) {
							String[] str = new String[3];
							str[0] = rst.getString(1);
							str[1] = rst.getString(2);
							str[2] = rst.getString(3);
							list.add(str);
						}
						rs.setRetVal(list);

					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	public Result setCorporationEmail(final String id) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							Statement stat = connection.createStatement();

							String sql = "update MailinfoSetting set isCorporationEmail=0 where isCorporationEmail=1";
							stat.addBatch(sql);
							sql = "update MailinfoSetting set isCorporationEmail=1 where id='"
									+ id + "'";
							stat.addBatch(sql);
							stat.executeBatch();
						} catch (Exception e) {
							AIOEMail.emailLog.error(
									"EMailMgt.setCorporationEmail Error ", e);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 通讯录联系人和地址
	 * 
	 * @param userId
	 * @return
	 */
	public Result getNoteNameAndEmail(final String userId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							String strsql = "select a.empFullName,a.email from tblEmployee a where ( b.ispublic =2 or b.createBy=? )";
							PreparedStatement ps = conn
									.prepareStatement(strsql);
							ps.setString(1, userId);
							ResultSet rs = ps.executeQuery();
							HashMap<String, String> ns = new HashMap<String, String>();
							while (rs.next()) {
								ns.put(rs.getString("email"), rs
										.getString("name"));// 联系人为键，地址为值
							}
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							rst.setRetVal(ns);
						} catch (Exception e) {
							AIOEMail.emailLog.error(
									"EMailMgt.getNoteNameAndEmail Error ", e);
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);

		return rst;

	}

	/**
	 * 通讯录联系人和地址
	 * 
	 * @param userId
	 * @return
	 */
	public Result getNoteNameByEmail(final String userId, final String email) {
		final Result rst = new Result();
		if (email == null || email.length() == 0)
			return rst;
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							String strsql = "select a.empFullName,a.email,a.id from tblEmployee a where a.statusId!='-1' and a.email in ("+email+")";
							PreparedStatement ps = conn.prepareStatement(strsql);
							ResultSet rs = ps.executeQuery();
							HashMap<String, String[]> ns = new HashMap<String, String[]>();
							while (rs.next()) {
								ns.put(rs.getString("email"),new String[]{rs.getString("empFullName"),rs.getString("id")});// 联系人为键，地址为值
							}
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							rst.setRetVal(ns);
						} catch (Exception e) {
							AIOEMail.emailLog.error("EMailMgt.getNoteNameAndEmail Error ", e);
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;

	}

	public Result getClientOwnerByEmail(final String email) {
		final Result rst = new Result();
		if (email == null || email.length() == 0)
			return rst;
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							String strsql = "select a.EmployeeID from CRMClientInfoEmp a , CRMClientInfoDet b where a.f_ref=b.f_ref and ClientEmail in ("
									+ email + ")";
							PreparedStatement ps = conn
									.prepareStatement(strsql);
							ResultSet rs = ps.executeQuery();
							ArrayList<String> ns = new ArrayList<String>();
							while (rs.next()) {
								ns.add(rs.getString(1));// 联系人为键，地址为值
							}
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							rst.setRetVal(ns);
						} catch (Exception e) {
							AIOEMail.emailLog.error(
									"EMailMgt.getNoteNameAndEmail Error ", e);
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);

		return rst;

	}

	public Result getClientByEmail(final String email) {
		final Result rst = new Result();
		if (email == null || email.length() == 0)
			return rst;
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							String strsql = "select a.clientName,b.userName,b.clientEmail,a.id from CRMClientInfo a , CRMClientInfoDet b where a.id=b.f_ref and ClientEmail in ("
									+ email + ")";
							PreparedStatement ps = conn
									.prepareStatement(strsql);
							ResultSet rs = ps.executeQuery();
							HashMap<String, String[]> ns = new HashMap<String, String[]>();
							while (rs.next()) {
								ns.put(rs.getString("clientEmail"),
										new String[] {
												rs.getString("clientName"),
												rs.getString("userName"),
												rs.getString("id") });// 联系人为键，地址为值
							}
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							rst.setRetVal(ns);
						} catch (Exception e) {
							AIOEMail.emailLog.error(
									"EMailMgt.getNoteNameAndEmail Error ", e);
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);

		return rst;

	}

	public Result getAssignById(final String mailId) {
		final Result rst = new Result();
		if (mailId == null || mailId.length() == 0)
			return rst;
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							String strsql = "select a.mailId,a.toUserId,b.EmpfullName,a.createTime,a.createBy,c.EmpfullName fname from tblMailAssign a , tblEmployee b,tblEmployee c where a.toUserId=b.id and a.createBy=c.id and mailId in ("
									+ mailId + ")";
							PreparedStatement ps = conn
									.prepareStatement(strsql);
							ResultSet rs = ps.executeQuery();
							HashMap<String, ArrayList> ns = new HashMap<String, ArrayList>();
							while (rs.next()) {
								String mailId = rs.getString("mailId");
								ArrayList list;
								if (ns.get(mailId) != null) {
									list = ns.get(mailId);
								} else {
									list = new ArrayList();
									ns.put(mailId, list);
								}
								list.add(new String[] {
										rs.getString("toUserId"),
										rs.getString("EmpfullName"),
										rs.getString("createTime"),
										rs.getString("fname") });// 联系人为键，地址为值
							}
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							rst.setRetVal(ns);
						} catch (Exception e) {
							AIOEMail.emailLog.error(
									"EMailMgt.getNoteNameAndEmail Error ", e);
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);

		return rst;

	}

	/**
	 * 设置默认帐户
	 * 
	 * @param userId
	 * @return
	 */
	public Result defaultAccount(final String id, final String userId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							String strsql = "update MailinfoSetting set defaultUser = 2 where createBy=?";
							PreparedStatement ps = conn
									.prepareStatement(strsql);
							ps.setString(1, userId);
							ps.executeUpdate();
							strsql = "update MailinfoSetting set defaultUser = 1 where createBy=? and id = ?";
							ps = conn.prepareStatement(strsql);
							ps.setString(1, userId);
							ps.setString(2, id);
							ps.executeUpdate();

							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception e) {
							AIOEMail.emailLog.error(
									"EMailMgt.getNoteNameAndEmail Error ", e);
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);

		return rst;

	}

	public Result queryMailLabel() {
		String hql = "select bean from MailLabelBean bean";
		return this.list(hql, new ArrayList());
	}

	/**
	 * 设置某邮件的标签
	 * 
	 * @param id
	 * @param label
	 * @return
	 */
	public Result setMailLabel(String id, String label) {
		Result rs = this.loadBean(id, OAMailInfoBean.class);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			OAMailInfoBean bean = (OAMailInfoBean) rs.retVal;
			if (label == null || label.length() == 0) {
				bean.setLabelId("");
				rs = this.updateBean(bean);
			} else if (bean.getLabelId() == null
					|| bean.getLabelId().indexOf(label) == -1) {
				if (bean.getLabelId() == null
						|| bean.getLabelId().length() == 0) {
					bean.setLabelId(label);
				} else {
					bean.setLabelId(bean.getLabelId() + ";" + label);
				}
				rs = this.updateBean(bean);
			}
		}
		return rs;
	}

	/**
	 * 设置某邮件的标签
	 * 
	 * @param id
	 * @param label
	 * @return
	 */
	public Result delMailLabel(String id, String label) {
		if (label == null || label.length() == 0) {
			return new Result();
		}
		Result rs = this.loadBean(id, OAMailInfoBean.class);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			OAMailInfoBean bean = (OAMailInfoBean) rs.retVal;

			if (bean.getLabelId() != null
					&& bean.getLabelId().indexOf(label) > -1) {
				if (bean.getLabelId().indexOf(label + ";") > -1) {
					bean.setLabelId(bean.getLabelId().replaceAll(label + ";",
							""));
				} else if (bean.getLabelId().indexOf(";" + label) > -1) {
					bean.setLabelId(bean.getLabelId().replaceAll(";" + label,
							""));
				} else {
					bean.setLabelId(bean.getLabelId().replaceAll(label, ""));
				}
				rs = this.updateBean(bean);
			}
		}
		return rs;
	}

	/**
	 * 回执
	 * @param id
	 * @return
	 */
	public Result setMailNoBegReplay(String id) {
		Result rs = this.loadBean(id, OAMailInfoBean.class);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			OAMailInfoBean bean = (OAMailInfoBean) rs.retVal;
			bean.setBegReplay("3");
			rs = this.updateBean(bean);
		}
		return rs;
	}

	
	/**
	 * 回执
	 * @param id
	 * @param userId
	 * @param userFullName
	 * @param content
	 * @return
	 */
	public Result setMailBegReplay(String id, String userId,
			String userFullName, String content) {
		Result rs = this.loadBean(id, OAMailInfoBean.class);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			OAMailInfoBean bean = (OAMailInfoBean) rs.retVal;
			bean.setBegReplay("2");
			// 发送回执
			OAMailInfoBean mailInfo = new OAMailInfoBean();
			mailInfo.setId(IDGenerater.getId());
			mailInfo.setCreateTime(BaseDateFormat.format(new Date(),
					BaseDateFormat.yyyyMMddHHmmss));
			mailInfo.setEmailType(bean.getEmailType());
			mailInfo.setSendeMailType(bean.getAccount());
			mailInfo.setFromUserId(userId); // 发件人代号
			mailInfo.setGroupId("3"); // 如果是保存为草稿，则存入草稿箱，否则为发件箱
			mailInfo.setMailFrom(userFullName);
			mailInfo.setMailTime(BaseDateFormat.format(new Date(),
					BaseDateFormat.yyyyMMddHHmmss));

			// 接收人
			mailInfo.setMailTo(bean.getMailFrom());

			content = content.replaceAll("bean.getMailTo", GlobalsTool
					.encodeHTML(bean.getMailTo()));
			content = content.replaceAll("bean.getMailTitle", bean
					.getMailTitle());
			content = content.replaceAll("mailInfo.getMailTime", mailInfo
					.getMailTime());

			mailInfo.setMailContent(content);

			mailInfo.setMailTitle("Read:" + bean.getMailTitle());
			mailInfo.setState(1); // 发件时不存在新邮件
			mailInfo.setUserId(userId);
			mailInfo.setAccount(bean.getAccount());

			mailInfo.setMailSize(mailInfo.getMailContent() == null ? 0
					: mailInfo.getMailContent().length());

			// 保存到数据库中去
			rs = addMail(mailInfo);
			// 内部邮件回执不用真发送
			if (bean.getEmailType() == 1) {
				try {

					// 指定帐号和密码
					EMailMessage smes = new EMailMessage();
					smes.setBegReplay(false);
					// 增加收件人
					if (null != mailInfo.getMailTo()
							&& !"".equals(mailInfo.getMailTo())) {
						String email = mailInfo.getMailTo().trim();
						String display = "";
						if (email.indexOf("<") > 0
								&& email.indexOf(">") == email.length() - 1) {
							display = email.substring(0, email.indexOf("<"));
							email = email.substring(email.indexOf("<") + 1,
									email.length() - 1);
						}
						smes.addRecipient(EMailMessage.TO, email, display);
					}

					smes.setSubject(mailInfo.getMailTitle());
					smes.setContent(true, mailInfo.getMailContent());
					smes.setDate(new Date());

					// 查询帐号信息
					rs = loadAccount(bean.getAccount());
					if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
						MailinfoSettingBean setting = (MailinfoSettingBean) rs
								.getRetVal();

						AIOEMail sm = new AIOEMail();
						boolean smtpauth = setting.getSmtpAuth() == 1 ? true
								: false;
						String attachPath = bean.getAccount();// 以邮箱代号为路径
						// 指定要使用的邮件服务器
						sm.setAccount(setting.getMailaddress(), setting
								.getPop3server(), setting.getPop3username(),
								setting.getPop3userpassword(), smtpauth,
								setting.getSmtpserver(), setting
										.getSmtpusername(), setting
										.getSmtpuserpassword(), setting
										.getPop3Port(), setting.getSmtpPort(),
								setting.getDisplayName(), attachPath, setting
										.getCreateby(),
								setting.getPopssl() == 1 ? true : false,
								setting.getSmtpssl() == 1 ? true : false,
								setting.getAutoAssign() == 1 ? true : false);
						// 调用发送邮件接口
						sm.send(smes);
					}

				} catch (AuthenticationFailedException ex) {
					AIOEMail.emailLog.error("EMailMgt.setMailBegReplay Error ", ex);
					mailInfo.setGroupId("2");// 改为草稿
					updateMail(mailInfo);
					rs.retCode = ErrorCanst.RET_NO_RIGHT_ERROR;
				} catch (MessagingException ex) {
					AIOEMail.emailLog.error("EMailMgt.setMailBegReplay Error ", ex);
					mailInfo.setGroupId("2");// 改为草稿
					updateMail(mailInfo);
					rs.retCode = ErrorCanst.DEFAULT_FAILURE;
				} catch (Exception ex) {
					AIOEMail.emailLog.error("EMailMgt.setMailBegReplay Error ", ex);
					mailInfo.setGroupId("2");// 改为草稿
					updateMail(mailInfo);
					rs.retCode = ErrorCanst.DEFAULT_FAILURE;
				}
			} else {
				mailInfo.setId(IDGenerater.getId());
				mailInfo.setUserId(bean.getFromUserId());
				mailInfo.setToUserId(bean.getFromUserId());
				mailInfo.setMailUID(mailInfo.getId());
				mailInfo.setGroupId("1");
				this.addBean(mailInfo);
			}

			this.updateBean(bean);
		}
		return rs;
	}

	public Result setMailSatus(String id, int status) {
		Result rs = this.loadBean(id, MailinfoSettingBean.class);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			MailinfoSettingBean bean = (MailinfoSettingBean) rs.retVal;
			bean.setStatusid(status);
			rs = this.updateBean(bean);
		}
		return rs;
	}

	/**
	 * 一次查询多个帐号地址
	 * 
	 * @param ids
	 * @return
	 */
	public Result getMailAddress(String[] ids) {
		String idstr = "";
		for (String id : ids) {
			if (id != null && id.length() > 0) {
				idstr += "'" + id + "',";
			}
		}
		if (idstr.length() == 0) {
			return null;
		} else {
			idstr = idstr.substring(0, idstr.length() - 1);
		}

		String hql = "select bean from MailinfoSettingBean bean where id in ("
				+ idstr + ")";
		return this.list(hql, new ArrayList());
	}

	/**
	 * 查询子帐户
	 * 
	 * @param ids
	 * @return
	 */
	public Result getMailChildMail(ArrayList<MailinfoSettingBean> beans) {
		Result rs = new Result();
		rs.retVal = beans;
		String idstr = "";
		for (MailinfoSettingBean bean : beans) {
			if (bean.getId() != null && bean.getId().length() > 0) {
				idstr += "'" + bean.getId() + "',";
			}
		}
		if (idstr.length() == 0) {
			return rs;
		} else {
			idstr = idstr.substring(0, idstr.length() - 1);
		}

		String hql = "select bean from MailinfoSettingBean bean where mainAccount in ("
				+ idstr + ")";
		Result rr = this.list(hql, new ArrayList());
		if (rr.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			ArrayList<MailinfoSettingBean> list = (ArrayList) rr.retVal;
			for (int i = 0; rr.retVal != null && i < list.size(); i++) {
				MailinfoSettingBean b = list.get(i);
				int count = beans.size();
				for (int j = 0; j < count; j++) {
					MailinfoSettingBean bean = beans.get(j);
					if (bean.getId().equals(b.getMainAccount())) {
						beans.add(j + 1, b);
					}
				}
			}
		}

		return rs;

	}

	/**
	 * 当回复时，需计录回复的时间
	 * 
	 * @param id
	 * @return
	 */
	public Result updateReplayDate(final String id) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {

						Connection conn = connection;
						String sql = "update oamailinfo set replayDate = ? where id = ? ";
						PreparedStatement s = conn.prepareStatement(sql);
						s.setString(1, BaseDateFormat.format(new Date(),
								BaseDateFormat.yyyyMMddHHmmss));
						s.setString(2, id);
						s.executeUpdate();
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 当回复时，需计录回复的时间
	 * 
	 * @param id
	 * @return
	 */
	public Result updateRevolveIdDate(final String id) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {

						Connection conn = connection;
						String sql = "update oamailinfo set revolveDate = ? where id = ? ";
						PreparedStatement s = conn.prepareStatement(sql);
						s.setString(1, BaseDateFormat.format(new Date(),
								BaseDateFormat.yyyyMMddHHmmss));
						s.setString(2, id);
						s.executeUpdate();
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 修改签名
	 * 
	 * @param id
	 * @param signature
	 * @return
	 */
	public Result updateSign(final String id, final String signature) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {

						Connection conn = connection;
						String sql = "update MailinfoSetting set signature = ? where id = ? ";
						PreparedStatement s = conn.prepareStatement(sql);
						s.setString(1, signature);
						s.setString(2, id);
						s.executeUpdate();
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 查询出所有需自动接收的帐户
	 * 
	 * @param userId
	 *            String
	 * @return Result
	 */
	public Result selectAutoReplayAccount() {
		List param = new ArrayList();
		String hql = "select bean from MailinfoSettingBean bean where bean.autoReceive > 0 and bean.statusid=1 ";
		return list(hql, param);
	}

	public Result handNewMail1000(String[] uids, AIOEMail sm,
			final String account, int retentDay) {
		/**
		 * 这里计算新邮件，并删除过期的老邮件 思路为，每封邮件都有GUID唯一编号,将帐户ID和邮件的GUID保留在表
		 * tblMailGUID中。并建立 帐号与guid的联合索引
		 * 每次接收邮件后，将所有邮件的GUID组合成一个查询语句，查询出哪些邮件已经接收过，过滤出接收过的邮件不再接收，
		 * 并运算已接收过的邮件是否超过保留时间，如果超过，则置删除标志。
		 * 这里还需考虑异常情况下tblMailGUID中数据不能正常删除，导致垃圾数据变多性能下降，这里还需一个机制同步删除服务器上已经不存在的GUID,
		 * 但这影响性能，每天做一次这种工作就行，所以记录每个帐号做这工作的时间。
		 */
		Result rs = new Result();
		if (uids == null || uids.length == 0) {
			return rs;
		}
		// 组合查询所有GUID
		String guids = "";
		final ArrayList<String> param = new ArrayList();
		for (String uid : uids) {
			if (uid == null || uid.length() == 0) {
				continue;
			}
			param.add(uid);
			guids += "?,";
		}
		guids = guids.substring(0, guids.length() - 1);
		param.add(account);

		// -----------如果邮件服务器上邮件超过2100封，此方法会因参数过多而报错，所以必须每次只能处理1千条数据，这样，如果执行下面的这个差异比较，会造成
		// -----------错删数据，所以必须屏蔽，但这样会造成垃圾数据，如果通过其它工具删除了邮件服务器上的邮件，---
		// 这里每天做一次差异比较
		// if(accountMap.get(account) == null || (System.currentTimeMillis() -
		// accountMap.get(account).getTime())/(24*60*60000) > 1){
		// final String delgrui = guids;
		// int retCode = DBUtil.execute(new IfDB() {
		// public int exec(Session session) {
		// session.doWork(new Work() {
		// public void execute(Connection connection) throws
		// SQLException {
		// Connection conn = connection;
		// String sql = "delete tblMailGUID where guid not in("+delgrui+") and
		// account=?";
		// PreparedStatement s = conn.prepareStatement(sql);
		// for(int i=1;i<=param.size();i++){
		// s.setString(i,param.get(i-1));
		// }
		// s.executeUpdate();
		// }
		// });
		// return ErrorCanst.DEFAULT_SUCCESS;
		// }
		// });
		// AIOEMail.emailLog.info("---account "+account +" run compare for server and
		// client");
		// accountMap.put(account, new Date());
		// }

		String sql = "select guid,maildate from tblMailGUID where guid in("
				+ guids + ") and account=?";
		rs = sqlList(sql, param);
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			AIOEMail.emailLog.error("EMailMgt.handNewMail() get db guid Error ");
			return rs;
		}

		ArrayList<Object[]> dbMail = (ArrayList<Object[]>) rs.retVal;

		final ArrayList<String> delMsgList = new ArrayList();// 用于保存本次需删除的邮件标记
		// 过婆旧邮件 ，并查看旧邮件是否过期
		for (Object[] os : dbMail) {
			for (String uid : uids) {
				if (os[0].equals(uid)) {
					// 过滤

					sm.newMsgList.remove(sm.msgMap.get(uid));
					// 查看旧邮件是否过期
					try {
						if (retentDay >= 0
								&& (os[1] == null || (System
										.currentTimeMillis() - BaseDateFormat
										.parse(os[1].toString(),
												BaseDateFormat.yyyyMMddHHmmss)
										.getTime())
										/ (24 * 60 * 60000) >= retentDay)) {
							// 删除服务器上邮件
							try {
								sm.msgMap.get(uid).setFlag(Flags.Flag.DELETED,
										true);
							} catch (Exception e) {
							}
							delMsgList.add(uid);
						}
					} catch (Exception e2) {
					}

					break;
				}
			}
		}
		// 删除数据库中旧邮件标记, 因为有些服务器设置删除标识后，不会真的删除,所以这里也不能删除guid,否则会重复接收
//		if (delMsgList.size() > 0) {
//			int retCode = DBUtil.execute(new IfDB() {
//				public int exec(Session session) {
//					session.doWork(new Work() {
//						public void execute(Connection connection)
//								throws SQLException {
//
//							Connection conn = connection;
//							String sql = "delete tblMailGUID where  guid= ? and account=? ";
//							PreparedStatement s = conn.prepareStatement(sql);
//							for (String mid : delMsgList) {
//								s.setString(1, mid);
//								s.setString(2, account);
//								s.addBatch();
//							}
//							s.executeBatch();
//						}
//					});
//					return ErrorCanst.DEFAULT_SUCCESS;
//				}
//			});
//			if (retCode != ErrorCanst.DEFAULT_SUCCESS) {
//				AIOEMail.emailLog.error("EMailMgt.handNewMail() del Old GUID Error ");
//				rs.retCode = retCode;
//				return rs;
//			}
//		}
		return rs;
	}

	public Result deleteGuidByAccount(final String account) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {

						Connection conn = connection;
						String sql = "delete  tblMailGUID where account=? ";
						PreparedStatement s = conn.prepareStatement(sql);
						s.setString(1, account);
						s.executeUpdate();
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		if (retCode != ErrorCanst.DEFAULT_SUCCESS) {
			AIOEMail.emailLog.error("EMailMgt.addGuid() Error ");
			rs.retCode = retCode;
			return rs;
		}
		return rs;
	}

	public Result addGuid(final String account, final String guid,
			final String maildate) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {

						Connection conn = connection;
						String sql = "insert into tblMailGUID(guid,account,maildate) values(?,?,?) ";
						PreparedStatement s = conn.prepareStatement(sql);
						s.setString(1, guid);
						s.setString(2, account);
						s.setString(3, maildate);
						s.executeUpdate();
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		if (retCode != ErrorCanst.DEFAULT_SUCCESS) {
			AIOEMail.emailLog.error("EMailMgt.addGuid() Error ");
			rs.retCode = retCode;
			return rs;
		}
		return rs;
	}

	public Result getMailSize(String userId) {
		ArrayList param = new ArrayList();
		String sql = "select a.mailSize+'',a.curMailSize+''  from tblEmployee a where a.id= ? ";
		param.add(userId);
		Result result = this.sqlList(sql, param);

		Result rs = new Result();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS
				&& ((ArrayList) result.retVal).size() > 0) {
			String mailSizes = ((Object[]) ((ArrayList) result.retVal).get(0))[0]
					.toString();
			String curmailSizes = ((Object[]) ((ArrayList) result.retVal)
					.get(0))[1].toString();
			long mailSize = Long.parseLong(mailSizes);
			long curmailSize = Long.parseLong(curmailSizes);
			if (mailSize == 0) {
				mailSize = Long.parseLong(BaseEnv.systemSet.get(
						"defaultMailBoxSize").getSetting());
			}
			rs.retVal = new Long[] { mailSize,
					curmailSize / (1024 * 1024) };
		}
		return rs;
	}

	public boolean checkMailSizeList(final int emailType, final String account,
			final String userId) {
		String sql = "";
		if (1 == emailType) {
			sql = "select a.mailSize,a.curMailSize  from tblEmployee a join MailinfoSetting b on a.id=b.createBy where b.id= ? ";
		} else {
			sql = "select a.mailSize,a.curMailSize  from tblEmployee a where a.id= ? ";
		}
		ArrayList param = new ArrayList();
		if (1 == emailType) {
			param.add(account);
		} else {
			param.add(userId);
		}
		Result rs = this.sqlList(sql, param);

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS
				&& ((List) rs.retVal).size() > 0) {
			Object[] os = (Object[]) (((List) rs.retVal).get(0));
			long mailSize = Long.parseLong(os[0].toString()) * 1024 * 1024;
			long curMailSize = Long.parseLong(os[1].toString());
			if (mailSize == 0) {
				mailSize = Long.parseLong(BaseEnv.systemSet.get(
						"defaultMailBoxSize").getSetting()) * 1024 * 1024;
			}
			if (curMailSize > mailSize) {
				return false;
			}
		}
		// 如出现异常请况不做限制
		return true;
	}

	/**
	 * 添加 提醒
	 * 
	 * @param bean
	 * @return
	 */
	public Result addAlert(final AlertBean bean) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				deleteBean(bean.getRelationId(), AlertBean.class, "relationId",
						session);
				Result result = addBean(bean, session);
				return result.retCode;
			}
		});
		return rs;
	}

	/**
	 * 删除提醒设置
	 * 
	 * @param bean
	 * @return
	 */
	public Result deleteAlert(String alertId) {
		return deleteBean(alertId, AlertBean.class, "id");
	}

	/**
	 * 加载提醒设置
	 * 
	 * @param bean
	 * @return
	 */
	public Result loadAlert(String alertId, Session session) {
		return loadBean(alertId, AlertBean.class, session);
	}

	/**
	 * 查看是否 存在提醒设置
	 * 
	 * @param emailId
	 * @return
	 */
	public Result loadAlertByEamilId(final String emailId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "select id from tblAlert where relationId=?";
						PreparedStatement pss = conn.prepareStatement(sql);
						pss.setString(1, emailId);
						ResultSet rss = pss.executeQuery();
						if (rss.next()) {
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							Result alert = loadAlert(rss.getString("id"),
									session);
							rs.setRetVal(alert.retVal);
						} else {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		return rs;
	}

	public Result deleteAlerts(final String relationId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement state = conn.createStatement();
//							String sql = "delete tblAdvice where relationId='"
//									+ relationId + "'";
//							state.addBatch(sql);
							String sql = "delete tblAlert where relationId='"
									+ relationId + "'";
							state.addBatch(sql);
							state.executeBatch();
						} catch (Exception e) {
							e.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		return rs;
	}
	/**
	 * 根据邮件的id得到当前收邮件的人名
	 */
	public Result getFullNameByEmailId(final String id){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "select empFullName from tblEmployee bean inner join oamailinfo mail on mail.userId = bean.id  where mail.id = ?";
						PreparedStatement pss = conn.prepareStatement(sql);
						pss.setString(1, id);
						ResultSet rss = pss.executeQuery();
						if (rss.next()) {
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							rs.setRetVal(rss.getString("empFullName"));
						} else {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		return rs;
	}
	
	/**
	 * 发送外部邮件给客户后修改客户最后更新时间与日志记录
	 * @param id
	 * @return
	 */
	public Result emailRelateClient(final ArrayList list,final String userId){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						OnlineUser user = OnlineUserInfo.getUser(userId);
						
						String emailCondition = "";//拼接查询的邮箱条件
						String clientIds = "";//客户Ids
						
						//处理邮箱地址拼接查询条件语句
						for (Object o : list) {
							EMailMessage.Recipienter rc = (EMailMessage.Recipienter) o;
							if(rc.emailAddress !=null && !"".equals(rc.emailAddress)){
								emailCondition += "'"+rc.emailAddress+"',";
							}
						}
						if(emailCondition.endsWith(",")){
							emailCondition = emailCondition.substring(0,emailCondition.length()-1);
						}
						
						
						if(!"".equals(emailCondition)){
							String sql = "select distinct f_ref from CRMClientInfoDet where ClientEmail in ("+emailCondition+")";
							PreparedStatement pss = conn.prepareStatement(sql);
							ResultSet rss = pss.executeQuery();
							while(rss.next()){
								clientIds += rss.getString("f_ref")+",";//客户Ids
							}
							//如果clientIds不为空,对客户进行操作
							if(!"".equals(clientIds)){
								String clientIdSql =""; //拼接客户ID语句
								for(String str : clientIds.split(",")){
									clientIdSql += "'"+str +"',";
									String context = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd) + " " +user.name+ "发送了一封邮件。" ;
									pubMgt.insertCRMCLientInfoLog(str, "history", context, user.getId(), conn) ;//插入客户日志
								}
								if(clientIdSql.endsWith(",")){
									clientIdSql = clientIdSql.substring(0,clientIdSql.length()-1);
								}
								
								//更新客户的最后联系时间
								if(!"".equals(clientIdSql)){
									String updateSql = "UPDATE CRMClientInfo SET LastContractTime = '"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"' WHERE id in ("+clientIdSql+")";
									PreparedStatement pss1 = conn.prepareStatement(updateSql);
									pss1.executeUpdate();
								}
							}
							
						}
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		return rs;
	}
	
	
	/**
	 * 处理邮件发送人的历史记录
	 * @param mailInfo
	 * @return
	 */
	public Result emailSendHistory(final OAMailInfoBean mailInfo){
		Result rs = dealReceivePerson(mailInfo);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			List list = (ArrayList)rs.getRetVal();
			rs = dealEmailSendHistory(list);
		}
		return rs;
	}
	
	
	
	/**
	 * 处理收件人数据，返回需要保存的数据
	 * @param mailInfo
	 * @return
	 */
	public Result dealReceivePerson(final OAMailInfoBean mailInfo){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							boolean sendeMailType = false;
							if(mailInfo.getSendeMailType() != null && !"".equals(mailInfo.getSendeMailType())){
								//外币邮箱
								sendeMailType = true;
							}
							List list = new ArrayList();
							Statement st = conn.createStatement();
							ResultSet rset = null;
							String str = mailInfo.getMailTo() + ";" + mailInfo.getMailCc() + ";" + mailInfo.getMailBCc();
							String emails[] = str.split(",|;");
							String error = "";
							for (String email : emails) {
								if (email != null && email.length() > 0&& !"null".equals(email)) {
									// 从部门表中查询
									boolean falg = false;
									String emailStr = "";
									String display = "";
									String sql = "select id from tblEmployee where openFlag=1 and empFullName='"+email+"'";
									if(sendeMailType){
										//外币邮箱进行查询客户联系人
										if(email.indexOf("<") > -1 && email.indexOf(">") == email.length() - 1) {
											display = email.substring(0, email.indexOf("<"));
											emailStr = email.substring(email.indexOf("<") + 1,email.length() - 1);
										}
										sql = "select id from tblEmployee where openFlag=1 and (empFullName='"+display+"' or email='"+emailStr+"')";
									}
									//查询职员
									rset = st.executeQuery(sql);
									while(rset.next()){
										falg = true;
										HashMap map = sendMap(mailInfo.getFromUserId(), rset.getString("id"), "1", mailInfo.getSendeMailType());
										list.add(map);
									}
									String emailType = "";
									if(!falg){
										if(sendeMailType){
											sql = "select id from CRMClientInfoDet where (UserName='"+display+"' or clientEmail='"+emailStr+"')";
											emailType = "3";
										}else{
											//内部邮箱部门
											sql = "select classCode as id from tblDepartment where classCode like (select classCode from tblDepartment bean where bean.deptFullName='"+email+"')";
											emailType = "2";
										}
										rset = st.executeQuery(sql);
										while(rset.next()){
											falg = true;
											HashMap map = sendMap(mailInfo.getFromUserId(), rset.getString("id"), emailType, mailInfo.getSendeMailType());
											list.add(map);
										}
									}
									if(!falg){
										HashMap map = sendMap(mailInfo.getFromUserId(), email, emailType, mailInfo.getSendeMailType());
										list.add(map);
									}
								}
							}
							rs.setRetVal(list);
						} catch (Exception e) {
							e.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		return rs;
	}
	
	/**
	 * 封装map数据
	 * @param sendId
	 * @param receiveId
	 * @param emailType
	 * @param emailSetting
	 * @return
	 */
	public HashMap sendMap(String sendId,String receiveId,String emailType,String emailSetting){
		HashMap map = new HashMap();
		map.put("sendId", sendId);										//发送者
		map.put("receiveId", receiveId);								//收件者
		map.put("emailType", emailType);								//邮箱类型
		map.put("emailSetting",emailSetting==null?"":emailSetting);
		return map;
	}
	
	
	/**
	 * 计算未读邮件
	 * @param userId
	 * @return
	 */
	public Result calcNoRead(final String userId) {
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select count(*) as count from OAMailInfo a "
									+ "where a.userId = ? and a.state=0 and a.groupId='1' and "
									+ "((EmailType = 0) or (EmailType = 1 and a.account in (select b.id from MailInfoSetting b where b.createBy = ? and b.statusId = 1))) ";
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, userId) ;
							pss.setString(2, userId) ;
							ResultSet rss = pss.executeQuery() ;
							int count = 0;
							if(rss.next()){
								count = rss.getInt("count") ;
								result.setRetVal(count) ;
							} else {
								result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							}
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							AIOEMail.emailLog.error("MOfficeMgt queryMessageByUserId mehtod",ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
    }
	

	/**
	 * 把现有的未读邮件数推送给手机
	 * @return
	 */
	public void notifyMobile(String userId) {
//		try {
//			Result rs = calcNoRead(userId);
//			if (ErrorCanst.DEFAULT_SUCCESS != rs.getRetCode()) {
//				return;
//			}
//			int count = (Integer) rs.getRetVal();
//			AIOEMail.emailLog.debug("EMailMag---notifyMobile----重算邮件数量，userId：" + userId + "，数量：" + count);
//			
//			HashMap<String, ArrayList<String[]>> tokenMap = null;
//			// 推送手机
//			if (null == tokenMap) {
//				Result result = new PublicMgt().queryTokenByUserIds(userId);
//				if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
//					tokenMap = (HashMap<String, ArrayList<String[]>>) result.retVal;
//				}
//			}
//			if (null != tokenMap) {
//				ArrayList<String[]> tokenList = tokenMap.get(userId);
//				if (null != tokenList) {
//					for (String[] str : tokenList) { // 一个人有可能有多个手机
//
//						JsonObject json = new JsonObject();
//						json.addProperty("type", "mail");
//						json.addProperty("count", count);
//						new AppleApnsSend(
//								String.valueOf(SystemState.instance.dogId),
//								str[0], "", json.toString(), str[1]).start();
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			AIOEMail.emailLog.error("EMailMag---notifyMobile---" + e);
//		}
	}
	
	
	/**
	 * 处理发送邮件历史记录
	 * @param sendId            发送者id
	 * @param receiveId			收件人id
	 * @param emailType			邮件方式（1职员，2部门，3客户）
	 * @param emailSetting      邮件类型（空代表内部邮箱，有值对应外部邮箱设置的表MailinfoSetting的id）
	 * @return
	 */
	public Result dealEmailSendHistory(final List list){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							for(int i=0;i<list.size();i++){
								HashMap map = (HashMap)list.get(i);
								StringBuffer sql = new StringBuffer("select count(0) as count from tblEmailSendHistory where ");
								sql.append("sendId='"+map.get("sendId")+"' and receiveId='"+map.get("receiveId")+"' and ");
								sql.append("emailType='"+map.get("emailType")+"' and emailSetting='"+map.get("emailSetting")+"'");
								Statement st = conn.createStatement();
								ResultSet rset = st.executeQuery(sql.toString());
								int count = 0;
								if(rset.next()){
									count = rset.getInt("count");
								}
								if(count == 0){
									//不存在发送的历史记录时新增记录
									sql = new StringBuffer("insert into tblEmailSendHistory(sendTime,sendId,receiveId,emailType,emailSetting) values(?,?,?,?,?)");
								}else{
									sql = new StringBuffer("update tblEmailSendHistory set sendTime=? where sendId=? and receiveId=? and emailType=? and emailSetting=?");
								}
								PreparedStatement ps = conn.prepareStatement(sql.toString());
								ps.setString(1, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
								ps.setString(2, String.valueOf(map.get("sendId")));
								ps.setString(3, String.valueOf(map.get("receiveId")));
								ps.setString(4, String.valueOf(map.get("emailType")));
								ps.setString(5, String.valueOf(map.get("emailSetting")));
								ps.executeUpdate();
							}
						} catch (Exception e) {
							e.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		return rs;
	}
	
	
	/**
	 * 查询用户发送的历史记录
	 * @param loginId           用户Id
	 * @param emailType			邮箱类型（内部邮箱，外部邮箱）
	 * @param keyWord			关键字搜索
	 * @return
	 */
	public Result queryEmailSendHistory(final String loginId, final String emailType, final String keyWord, final String pageNo){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("select id,sendId,receiveId,sendTime,emailType,isnull(emailSetting,'') as emailSetting,");
							sql.append(" ROW_NUMBER() over(order by sendTime desc) as row_id from tblEmailSendHistory where sendId='"+loginId+"' ");
							if(emailType == null || "".equals(emailType)){
								//内部邮箱取部门和职员
								sql.append(" and (emailType='1' or emailType='2') and isnull(emailSetting,'') = '' ");
							}else{
								sql.append(" and (emailType='1' or emailType='3') and emailSetting is not null and emailSetting !='' ");
							}
							String sqlCount = sql.toString();
							String sqls = "select * from ("+sqlCount+") as a where 1=1 ";
							if(keyWord == null || "".equals(keyWord)){
								 if(pageNo != null && !"".equals(pageNo)){
									 sqls += " and row_id between "+(20*Integer.parseInt(pageNo)+1)+" and "+(20*(Integer.parseInt(pageNo)+1));
								 }else{
									 sqls += " and row_id between 1 and 20";
								 }
							}
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sqls);
							List list = new ArrayList();
							while(rset.next()){
								HashMap map = new HashMap();
								Integer type = Integer.valueOf(rset.getString("emailType"));
								String receiveId = rset.getString("receiveId");
								map.put("id", rset.getString("id"));
								map.put("sendType", type);
								map.put("receiveId", receiveId);
								if(type == 1){
									//从职员表中查询数据
									sqls = "select EmpFullName as name,email from tblEmployee where id='"+receiveId+"'";
								}else if(type == 2){
									//从部门表中查询数据
									sqls = "select DeptFullName as name,'' as email from tblDepartment where classCode='"+receiveId+"'";
								}else if(type == 3){
									//从客户表中查询数据
									sqls = "select UserName as name,ClientEmail as email from CRMClientInfoDet where id='"+receiveId+"'";
								}
								PreparedStatement ps = conn.prepareStatement(sqls);
								ResultSet resultSet = ps.executeQuery();
								if(resultSet.next()){
									map.put("name", resultSet.getString("name"));
									map.put("email", resultSet.getString("email"));
								}
								//关键字查询
								if(keyWord != null && !"".equals(keyWord)){
									if(receiveId.indexOf(keyWord)>=0 || (map.get("name")!=null && String.valueOf(map.get("name")).indexOf(keyWord)>=0) 
											|| (map.get("email")!=null && String.valueOf(map.get("email")).indexOf(keyWord)>=0)
											|| CustomizePYM.getFirstLetter(map.get("name").toString()).indexOf(keyWord)>=0 
											|| ChineseSpelling.getSelling(map.get("name").toString()).indexOf(keyWord)>=0 ){
										list.add(map);
									}
								}else{
									list.add(map);									
								}
							}
							
							Integer count = 0;
							if((keyWord == null || "".equals(keyWord))){
								sqls = "select count(0) as count from ("+sqlCount+") as a";
								rset = st.executeQuery(sqls);
								if(rset.next()){
									count = rset.getInt("count");		
								}
							}
							Object[] obj = new Object[]{count,list};
							rst.setRetVal(obj);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		return rst;
	}
	
	/**
	 * 查询组织架构
	 * @return
	 */
	public Result queryDeptOrEmp(final String keyword){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("select id,deptFullName as name,classCode,isCatalog from tblDepartment where classCode is not null and statusId=0 ");
							if(keyword != null && !"".equals(keyword)){
								sql.append(" and (deptCode like '%"+keyword+"%' or deptFullName like '%"+keyword+"%' or remark like '%"+keyword+"%' or deptFullNamePYM like '%"+keyword+"%'  )");
							}
							sql.append(" order by deptCode");
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ResultSet rs = ps.executeQuery();
							ArrayList deptList=new ArrayList();
							while(rs.next()){
								HashMap map=new HashMap();
	                        	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
	                        		Object obj=rs.getObject(i);
	                        		if(obj==null){
	                        			map.put(rs.getMetaData().getColumnName(i), "");
	                        		}else{
	                        			map.put(rs.getMetaData().getColumnName(i), obj);
	                        		}
	                        	}
	                        	deptList.add(map);
							}
							sql = new StringBuffer("select id,empFullName as name,departmentCode as classCode from tblEmployee where statusId=0");
							if(keyword != null && !"".equals(keyword)){
								sql.append(" and (empFullName like '%"+keyword+"%' or sysName like '%"+keyword+"%' or empFullNamePYM like '%"+keyword+"%'  )");
							}
							Statement st = conn.createStatement();
							ResultSet reset = st.executeQuery(sql.toString());
							ArrayList empList=new ArrayList();
							while(reset.next()){
								HashMap empMap=new HashMap();
								for(int j=1;j<=reset.getMetaData().getColumnCount();j++){
									Object obj=reset.getObject(j);
									if(obj==null){
										empMap.put(reset.getMetaData().getColumnName(j), "");
									}else{
										empMap.put(reset.getMetaData().getColumnName(j), obj);
									}
								}
								empList.add(empMap);
							}
							Object[] obj = new Object[]{deptList, empList};
							rst.setRetVal(obj);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		return rst;
	}
	
	
	/**
	 * 查询通讯录
	 * @param keyWord
	 * @return
	 */
	public Result queryAddressList(final String keyWord){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							
							/* 查询通讯录数据 */
							StringBuffer sql = new StringBuffer("select id,empFullName as name,email from tblemployee where statusId!='-1' and SCompanyID='00001' ");
							sql.append("and isnull(email,'')!='' ");
							if(keyWord != null && !"".equals(keyWord)){
								sql.append(" and (empFullName like '%"+keyWord+"%' or sysName like '%"+keyWord+"%' or empFullNamePYM like '%"+keyWord+"%' or email like '%"+keyWord+"%')");
							}
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sql.toString());
							ArrayList empList=new ArrayList();
							while(rset.next()){
								HashMap map=new HashMap();
	                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                        		Object obj=rset.getObject(i);
	                        		if(obj==null){
	                        			map.put(rset.getMetaData().getColumnName(i), "");
	                        		}else{
	                        			map.put(rset.getMetaData().getColumnName(i), obj);
	                        		}
	                        	}
	                        	empList.add(map);
							}
							rst.setRetVal(empList);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		return rst;
	}
	
	/**
	 * 查询客户联系人（客户联系人进行支持更多查询）
	 * @param keyWord
	 * @return
	 */
	public Result queryClientData(final String keyWord,final LoginBean lg,final String pageNo){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							
							/* 查询客户联系人 */
							String sql = "select CRMClientInfoDet.id,CRMClientInfo.clientName,CRMClientInfoDet.userName as name,CRMClientInfoDet.clientEmail as email,ROW_NUMBER() over(order by CRMClientInfoDet.id desc) as row_id " +
							"from CRMClientInfoDet,CRMClientInfo where CRMClientInfoDet.f_ref=CRMClientInfo.id and isnull(CRMClientInfoDet.clientEmail,'') != ''  and f_ref in(SELECT id from CRMClientInfo where 1=1";
							sql+=new PublicMgt().getClientScope(lg)+")";
							
							if(keyWord != null && !"".equals(keyWord)){
								sql += " and (CRMClientInfo.clientName like '%"+keyWord+"%'";
								sql += " or CRMClientInfoDet.userName like '%"+keyWord+"%'";
								sql += " or CRMClientInfoDet.clientEmail like '%"+keyWord+"%')";
							}
							String sqlCount = sql;
							String sqls = "select * from ("+sqlCount+") as a where 1=1 ";
							
							//非关键字查询
							if(pageNo != null && !"".equals(pageNo)){
								sqls += " and row_id between "+(20*Integer.parseInt(pageNo)+1)+" and "+(20*(Integer.parseInt(pageNo)+1));
							}else{
								sqls += " and row_id between 1 and 20";
							}
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sqls);
							ArrayList clientList=new ArrayList();
							while(rset.next()){
								HashMap map=new HashMap();
	                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                        		Object obj=rset.getObject(i);
	                        		if(obj==null){
	                        			map.put(rset.getMetaData().getColumnName(i), "");
	                        		}else{
	                        			map.put(rset.getMetaData().getColumnName(i), obj);
	                        		}
	                        	}
	                        	clientList.add(map);
							}
							Integer count = 0;
							sqls = "select count(0) as count from ("+sqlCount+") as a";
							rset = st.executeQuery(sqls);
							if(rset.next()){
								count = rset.getInt("count");		
							}
							Object[] obj = new Object[]{count,clientList};
							rst.setRetVal(obj);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		return rst;
	}
	
	/**
	 * 修改备注
	 * @param mailId
	 * @param content
	 * @return
	 */
	public Result dealRemark(final String mailId,final String content){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "update OAMailInfo set remark='"+content+"' where id='"+mailId+"'";
							Statement st = conn.createStatement();
							int count = st.executeUpdate(sql);
							rst.setRetVal(count);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		return rst;
	}
	
	/**
	 * 删除邮件发送的人员历史记录
	 * @param id
	 * @return
	 */
	public Result delHistory(final String id,final String loginId){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "delete tblEmailSendHistory where sendId='"+loginId+"'";
							if(!"all".equals(id)){
								sql += " and receiveId='"+id+"'";
							}
							Statement st = conn.createStatement();
							st.executeUpdate(sql);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		return rst;
	}
	
	/**
	 * 查询E-mail记录的上一条或者下一条
	 * @param id
	 * @param loginId
	 * @return
	 */
	public Result queryPreNextData(final String sql, final List param, final String itemType, final String createTime){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sqls = "select top 1 "+sql.substring("select".length(),sql.indexOf("order by"));
							if("pre".equals(itemType)){
								//上一条
								sqls += " and bean.createTime >'"+createTime+"' order by bean.createTime asc";
							}else if("next".equals(itemType)){
								//下一条
								sqls += " and bean.createTime <'"+createTime+"' order by bean.createTime desc";
							}
							PreparedStatement ps = conn.prepareStatement(sqls);
							for(int i=0;i<param.size();i++){
								ps.setObject(i+1, param.get(i));
							}
							ResultSet rset = ps.executeQuery();
							ArrayList emailList=new ArrayList();
							while(rset.next()){
								HashMap map=new HashMap();
	                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                        		Object obj=rset.getObject(i);
	                        		if(obj==null){
	                        			map.put(rset.getMetaData().getColumnName(i), "");
	                        		}else{
	                        			map.put(rset.getMetaData().getColumnName(i), obj);
	                        		}
	                        	}
	                        	emailList.add(map);
							}
							rst.setRetVal(emailList);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		return rst;
	}
}
