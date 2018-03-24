package com.menyi.email.emailFilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.koron.oa.bean.EmailFilter;
import com.koron.oa.bean.MailinfoSettingBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.OperationConst;

/**
 * 邮件过滤Manager by mj Description: <br/>Copyright (C), 2008-2012, Justin.T.Wang
 * <br/>This Program is protected by copyright laws. <br/>Program Name:
 * <br/>Date:
 * 
 * @autor Justin.T.Wang yao.jun.wang@hotmail.com
 * @version 1.0
 */
public class EmailFilterMgt extends AIODBManager {

	/**
	 * mj 模糊查询
	 * 
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @param SCompanyID
	 * @param sysName
	 * @param epat
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public Result query(int pageNo, int pageSize, String oaMailinfoSetting,
			String folder, String filterCondition) {

		// 创建参数
		List param = new ArrayList();
		String hql = "select bean from EmailFilter as bean where 1=1";
		// 如标题不为空，则做标题模糊查询
		if (StringUtils.isNotBlank(filterCondition)) {
			hql += " and upper(bean.empFullName) like '%'||?||'%' ";
			param.add(filterCondition.trim().toUpperCase());// 此处也许不需要toUpperCase小写
			// 有待测试
		}
		// 目标文件夹
		if (StringUtils.isNotBlank(folder)) {
			hql += " and upper(bean.sysName) like '%'||?||'%' ";
			param.add(folder.trim().toUpperCase());
		}
		// 邮箱账户
		if (StringUtils.isNotBlank(folder)) {
			hql += " and upper(bean.sysName) like '%'||?||'%' ";
			param.add(folder.trim().toUpperCase());
		}
		// 调用list返回结果
		return list(hql, param, pageNo, pageSize, true);
	}

	public Result queryAllInfoByKeyWord(String userId,String oaMailinfoSetting,
			String folder, String filterCondition, String msId, int pageNo,
			int pageSize) {
		AIODBManager aioMgt = new AIODBManager();
		List<String> param = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(
				"select emp.id as eId, ms.id as msId,filterCondition,refOaFolderId,mailAddress from emailfilter emp  join mailinfosetting ms on emp.refOamailinfosettingid = ms.id where 1=1 ");


		if (StringUtils.isNotBlank(userId)) {
			sql.append(" and emp.userId=?");
			param.add(userId);
		}
		
		if (StringUtils.isNotBlank(filterCondition)) {
			sql.append(" and filterCondition like '%' +?+'%'");
			param.add(filterCondition);
		}

		if (StringUtils.isNotBlank(folder)) {
			sql.append(" and folderName like '%'+?+'%'");
			param.add(folder);
		}
		if (StringUtils.isNotBlank(oaMailinfoSetting)) {
			sql.append(" and mailAddress like '%' + ? + '%'");
			param.add(oaMailinfoSetting);
		}
		if (StringUtils.isNotBlank(msId)) {
			sql.append(" and ms.id = '?'");
			param.add(msId);
		}

		sql.append(" order by emp.lastUpdateTime desc");
		Result rst = aioMgt.sqlList(sql.toString(), param, pageSize, pageNo,
				true);
		List list = (List) rst.getRetVal();
		List<EmailFilter> listFilter = new ArrayList<EmailFilter>();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			EmailFilter obj = new EmailFilter();

			System.out.println(o[3].toString());
			obj.setId(o[0].toString());
			obj.setRefOaFolderId(o[3].toString());
			obj.setRefOaMailinfoSettingId(o[1].toString());
			obj.setFilterCondition(o[2].toString());
			String gId = o[3].toString();
			String fName = this.getFoldeNameByMap(gId);
			if (StringUtils.isNotBlank(fName)){
				obj.setFolderName(fName);
			} else {
				Result rs = getGroupById(gId);
				String[] v = (String[])rs.getRetVal();
				obj.setFolderName(v[0]);
			}
			
			obj.setAddressName(o[4].toString());
			obj.setCreateTime(BaseDateFormat.format(new Date(),
					BaseDateFormat.yyyyMMddHHmmss));
			obj.setLastUpdateTime(BaseDateFormat.format(new Date(),
					BaseDateFormat.yyyyMMddHHmmss));
			listFilter.add(obj);
		}
		rst.setRetVal(listFilter);
		return rst;
	}
	
	
	  public Result getGroupById(String id) {

		final String groupId = id; // group ID

		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							String sql = "select GroupName from dbo.oamailGroup where 1=1 and id =?";
							System.out.println(sql);

							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, groupId);
							ResultSet rss = ps.executeQuery();
							String value[] = new String[1];
							if (rss.next()) {
								value[0] = rss.getString("GroupName"); // 将name保存
							}
							rs.setRetVal(value);
							rs.setRealTotal(value.length);
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							return;
						}
					}
				});

				return rs.getRetCode();
			}
		});
		rs.retCode = retCode;
		return rs;
	}

	
	//mj
	@SuppressWarnings("unchecked")
	public Result queryInfoById(String id) {
		AIODBManager aioMgt = new AIODBManager();
		StringBuffer sql = new StringBuffer(
				"select emp.id as eId, ms.id as msId,filterCondition,refOaFolderId,mailAddress from emailfilter emp  inner join mailinfosetting ms on emp.refOamailinfosettingid = ms.id where emp.id = ?");
		List paramList = new ArrayList();
		paramList.add(id);
		Result rst = aioMgt.sqlList(sql.toString(), paramList);
		List list = (List) rst.getRetVal();
		List<EmailFilter> listFilter = new ArrayList<EmailFilter>();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			EmailFilter obj = new EmailFilter();
			obj.setId(o[0].toString());
			obj.setRefOaFolderId(o[3].toString());
			obj.setRefOaMailinfoSettingId(o[1].toString());
			obj.setFilterCondition(o[2].toString());
			
			String gId = o[3].toString();
			String fName = this.getFoldeNameByMap(gId);
			if (StringUtils.isNotBlank(fName)){
				obj.setFolderName(fName);
			} else {
				Result rs = getGroupById(gId);
				String[] v = (String[])rs.getRetVal();
				obj.setFolderName(v[0]);
			}
			
			obj.setAddressName(o[4].toString());
			obj.setCreateTime(BaseDateFormat.format(new Date(),
					BaseDateFormat.yyyyMMddHHmmss));
			obj.setLastUpdateTime(BaseDateFormat.format(new Date(),
					BaseDateFormat.yyyyMMddHHmmss));
			listFilter.add(obj);
		}
		rst.setRetVal(listFilter);
		return rst;
	}

	/**
	 * maojing 添加一条邮件过滤数据
	 * 
	 * @param id
	 *            long
	 * @param name
	 *            String
	 * @return Result
	 */
	public Result add(final EmailFilter emailFilter) {
		// 调用基类方法addBean执行插入操作
		return addBean(emailFilter);
	}

	/**
	 * 修改bean by mj
	 */
	public Result update(EmailFilter filter) {
		Result rs = new Result();
		rs = updateBean(filter);
		return rs;
	}

	/**
	 * 根据id查询对应的bean
	 * 
	 * @param userId
	 * @param type
	 * @param value
	 * @return
	 */
	public Result getEmailFilterById(String id) {
		Result rs = new Result();
		rs = loadBean(id, EmailFilter.class);
		return rs;
	}

	public Result getMailinfoSettingBeanById(String id) {
		Result rs = new Result();
		rs = loadBean(id, MailinfoSettingBean.class);
		return rs;
	}

	/**
	 * 查询出当前用户的个人邮箱
	 * 
	 * @param userId
	 *            String
	 * @return Result
	 */
	@SuppressWarnings("unchecked")
	public Result selectMailAccountByUser(final String userId,
			final String mainAccount) {
		List param = new ArrayList();
		String sql = "select a.id,a.account,a.mailaddress,b.account mainaccount,a.defaultUser,c.empFullName,a.statusid "
				+ "from MailinfoSetting a left join MailinfoSetting b on b.id=a.mainaccount   left join tblemployee c on a.createBy=c.id ";

		if (mainAccount != null && !mainAccount.equals("")) {
			sql += " where a.mainaccount=?";
			param.add(mainAccount);
		} else {
			sql += "  where (a.mainaccount = '' or a.mainaccount is null) ";
		}
		if (!userId.equals("1")) {
			sql += " and a.createBy=?";
			param.add(userId);
		}
		return this.sqlList(sql, param);
	}

	/**
	 * 删除多条过滤数据
	 * 
	 * @param ids
	 *            long[]
	 * @return Result
	 */
	public Result delete(String[] ids) {
		return deleteBean(ids, EmailFilter.class, "id");
	}

	/**
	 * 根据邮箱账号查询对应的规则详细信息 mj
	 * 
	 * @param msId
	 * @return
	 */
	public Result getFilterInfoByMsId(String msId) {
		List<String> param = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(
				"select emp.id as eId, ms.id as msId,filterCondition,refOaFolderId,mailAddress from emailfilter emp  join mailinfosetting ms on emp.refOamailinfosettingid = ms.id where 1=1 ");
		if (StringUtils.isNotBlank(msId)) {
			sql.append(" and ms.id = ?");
			param.add(msId);
		}
		sql.append(" order by emp.lastUpdateTime desc");
		Result rst = sqlList(sql.toString(), param);
		List list = (List) rst.getRetVal();
		List<EmailFilter> listFilter = new ArrayList<EmailFilter>();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			EmailFilter obj = new EmailFilter();
			obj.setId(o[0].toString());
			obj.setRefOaFolderId(o[3].toString());
			obj.setRefOaMailinfoSettingId(o[1].toString());
			obj.setFilterCondition(o[2].toString());
			obj.setFolderName(this.getFoldeNameByMap(o[3].toString()));
			obj.setAddressName(o[4].toString());
			obj.setCreateTime(BaseDateFormat.format(new Date(),
					BaseDateFormat.yyyyMMddHHmmss));
			obj.setLastUpdateTime(BaseDateFormat.format(new Date(),
					BaseDateFormat.yyyyMMddHHmmss));
			listFilter.add(obj);
		}
		rst.setRetVal(listFilter);
		return rst;
	}

	/**
	 * mj 将字符串以，分隔成数组
	 */
	public Result getArrayByStrSplit(String str) {
		Result rs = new Result();
		String[] arr = StringUtils.split(str, ',');
		rs.setRetVal(arr);
		return rs;
	}

	/**
	 * mj 将对应的文件夹名获得对应的group ID 当前文件夹名是否属于五个常见的文件夹 如收件箱 发现箱
	 * 草稿箱（不需要管前面三个，会自动保存到对应的目录） 垃圾邮件箱 废件箱
	 */
	public Result getGroupIdMapByFolder(String folder, String userId,
			String account) {

		Result rs = new Result();
		String groupId = this.getFolderIdByMap(folder);
		if (StringUtils.isNotBlank(groupId)) {
			rs.setRetVal(groupId);
			return rs;
		} else {
			// 查询该文件夹对应的groupId
			rs = queryGroupDetail(userId, account, folder);
			Object[] obj = (Object[]) rs.getRetVal();
			if (obj != null) {
				groupId = obj[0].toString();
				rs.setRetVal(groupId);
				return rs;
			}
			return null;
		}

	}

	/**
	 * mj map 存放对应的groupid
	 */
	public String getFoldeNameByMap(String folderId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(OperationConst.OP_GROUPID_ONE_NUM,
				OperationConst.OP_GROUPID_ONE_STR);
		map.put(OperationConst.OP_GROUPID_TWO_NUM,
				OperationConst.OP_GROUPID_TWO_STR);
		map.put(OperationConst.OP_GROUPID_THREE_NUM,
				OperationConst.OP_GROUPID_THREE_STR);
		map.put(OperationConst.OP_GROUPID_FOUR_NUM,
				OperationConst.OP_GROUPID_FOUR_STR);
		map.put(OperationConst.OP_GROUPID_FIVE_NUM,
				OperationConst.OP_GROUPID_FIVE_STR);
		return map.get(folderId);
	}

	public String getFolderIdByMap(String folder) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(OperationConst.OP_GROUPID_ONE_STR,
				OperationConst.OP_GROUPID_ONE_NUM);
		map.put(OperationConst.OP_GROUPID_TWO_STR,
				OperationConst.OP_GROUPID_TWO_NUM);
		map.put(OperationConst.OP_GROUPID_THREE_STR,
				OperationConst.OP_GROUPID_THREE_NUM);
		map.put(OperationConst.OP_GROUPID_FOUR_STR,
				OperationConst.OP_GROUPID_FOUR_NUM);
		map.put(OperationConst.OP_GROUPID_FIVE_STR,
				OperationConst.OP_GROUPID_FIVE_NUM);
		return map.get(folder);
	}

	@SuppressWarnings("unchecked")
	public Result queryGroupDetail(String userId, String account,
			String groupName) {
		List param = new ArrayList();
		String hql = "select g.id from OAMailGroup g join mailinfosetting ms on ms.id = g.account  where g.account = ? and g.userID = ? and g.groupName = ?";
		param.add(account);
		param.add(userId);
		param.add(groupName);
		return sqlList(hql, param);
	}

	public Result load(String setId) {
		Result rs = loadBean(setId, MailinfoSettingBean.class);
		return rs;
	}

	public String getFilterRule(String str) {
		int i = str.indexOf(".");
		int j = str.indexOf(".", i + 1);
		int p = str.indexOf("@");
		String filter = "";
		// www.baidu@qq.com
		if (p >= 0) {
			// 存在
			// www.baidu@aa.com baidu@qq.com
			System.out.println(str);
			if (i < 0) {
				// 没有.那也就是 如baidu等
				filter = str;
			} else if (i >= 0) {
				if( j>0 ) {// www.baidu@qq.com
					if( p<j ) {//只有这种可能 if可以去掉
						filter = str.substring(i+1,p);
					}
				} else {// baidu@qq.com等
					if( p>j ){//只有这种可能 if可以去掉
						filter = str.substring(0,p);
					}
				}
			}
		} else {// 不存在
			if (i < 0) {
				// 没有.那也就是 如baidu等
				filter = str;
			} else if (i >= 0) {
				if (j > 0) {// 存在 www.fsdfsd.fsdf这样的
					filter = str.substring(i+1, j);
					System.out.println(filter);
				} else {
					// 只有www.baiducom 没结尾的点 mao.com
					if (i == 0) {
						System.out.println(str);
						filter = str.substring(1);
						System.out.println("ss"+filter+"ss");
					} else {
						System.out.println(str);
						filter = str.substring(0,i);
						System.out.println("ss"+filter+"ss");
					}
				}

			} 
		}
		return filter;
	}
}
