package com.koron.oa.directorySeting;

import java.util.*;

import org.apache.commons.lang.StringUtils;

import com.dbfactory.Result;
import com.koron.oa.bean.DirectorySetting;
import com.koron.oa.util.StringArray;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;

/**
 * 
 * <p>
 * Title: 目录设置
 * </p>
 * 
 * @Copyright: 科荣软件
 * 
 * @author 毛晶
 * 
 */
public class DirectorySettingMgt extends AIODBManager {

	/**
	 * mj 查询所有的设置目录 方法一
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
	public Result query(int pageNo, int pageSize, Boolean isNeedPage, int isRoot) {
		// 创建参数
		List param = new ArrayList();
		param.add(isRoot);
		String hql = "select bean from DirectorySetting as bean where isRoot = ? order by lastUpdateTime desc";
		// 调用list返回结果
		if (isNeedPage) {
			return list(hql, param, pageNo, pageSize, true);
		} else {
			return list(hql, param);
		}
	}

	/**
	 * 初始化根节点
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
	public static Result getTreeRootData(LoginBean login, int isRoot) {

		AIODBManager aioMgt = new AIODBManager();
		List<Integer> param = new ArrayList<Integer>();
		System.out.println(isRoot);
		String hql = "select * from directorySetting where isRoot = ?";
		param.add(isRoot);
		Result rst = aioMgt.sqlList(hql, param);
		List list = (List) rst.getRetVal();
		List<DirectorySetting> directList = new ArrayList<DirectorySetting>();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			DirectorySetting bean = new DirectorySetting();
			if(bean == null){
				continue;
			}
			String ids = o[8].toString();
			String classCode = o[9].toString();
			String empGroup = o[10].toString();
			
			if (!isShowCurRoot(login, ids, classCode, empGroup)) {
				continue;
			}
			String id = o[0].toString();
			bean.setId(id);
			bean.setUserId(o[1].toString());
			bean.setPath(o[2].toString());
			bean.setName(o[3] == null ? null : o[3].toString());
			bean.setCreateTime(o[4].toString());
			bean.setLastUpdateTime(o[5].toString());
			bean.setTreeNo(Integer.parseInt(o[6].toString()));
			bean.setIsRoot(isRoot);
			bean.setShareuserId(ids);
			bean.setShareDeptOfClassCode(classCode);
			bean.setShareEmpGroup(empGroup);
			directList.add(bean);
		}

		rst.setRetVal(directList);
		return rst;
	}

	/**
	 * 判断是否有权限显示该根目录
	 * 
	 * @return
	 */
	public static boolean isShowCurRoot(LoginBean bean, String userId,
			String classCodes, String empGroup) {

		if ("admin".equalsIgnoreCase(bean.getName())) {
			return true;
		}
		if (StringUtils.isBlank(userId) && StringUtils.isBlank(classCodes)
				&& StringUtils.isBlank(empGroup)) {
			return true;// 所有人可见
		}

		String id = bean.getId();
		String departCode = bean.getDepartCode();
		String groupId = bean.getGroupId();
		
		if (StringUtils.isNotBlank(userId)) {
			String[] us = userId.split(",");
			for (int i = 0; i < us.length; i++) {
				String u = us[i];
				if (u.equals(id)) {
					return true;
				}
			}

		}
		if (StringUtils.isNotBlank(classCodes)) {
			String[] codes = classCodes.split(",");
			for (int i = 0; i < codes.length; i++) {
				String c = codes[i];
				if (departCode.contains(c)) {//子部门包含父部门或者相等
					return true;
				}
			}
		}

		if (StringUtils.isNotBlank(empGroup) && StringUtils.isNotBlank(groupId)) {
			String[] es = empGroup.split(",");
			String[] gs = groupId.split(";");
			String[] result_insect = StringArray.intersect(es, gs);
			if (result_insect.length > 0) {// 存在交集
				return true;
			}
		}

		return false;
	}

	
	/**
	 * 根据关联的列查询 对应的总数据大小 mj
	 * 
	 * @param tableName
	 * @param row
	 * @param rowValue
	 * @return
	 */
	public int getQueryCount(String tableName, String rowName1,
			String rowValue1, String rowName2, String rowValue2) {
		AIODBManager aioMgt = new AIODBManager();
		List<String> param = new ArrayList<String>();
		StringBuffer sql = new StringBuffer(
				"select count(*) from DirectorySetting as bean where "
						+ rowName1 + " = ? and " + rowName2 + " = ? ");
		param.add(rowValue1);
		param.add(rowValue2);
		System.out.println(sql);
		Result rst = aioMgt.sqlList(sql.toString(), param);
		List list = (List) rst.getRetVal();
		Object[] obj = (Object[]) list.get(0);
		int count = Integer.parseInt(obj[0].toString());
		return count;
	}

	/**
	 * 添加mj
	 * 
	 * @param id
	 *            long
	 * @param name
	 *            String
	 * @return Result
	 */
	public Result add(Object bean) {
		// 调用基类方法addBean执行插入操作
		return addBean(bean);
	}

	/**
	 * 修改bean mj
	 */
	public Result update(Object bean) {
		Result rs = new Result();
		rs = updateBean(bean);
		return rs;
	}

	/**
	 * 删除多条数据 mj
	 * 
	 * @param ids
	 *            long[]
	 * @return Result
	 */
	public Result delete(String[] ids, Class classBean, String delKey) {
		return deleteBean(ids, classBean, delKey);
	}

	public Result load(String setId, Class classBean) {
		Result rs = loadBean(setId, classBean);
		return rs;
	}

	public Result deleteBean(String id, Class classBean) {
		return deleteBean(id, classBean, "id");
	}
}
