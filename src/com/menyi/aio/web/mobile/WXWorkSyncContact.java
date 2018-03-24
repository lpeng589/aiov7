package com.menyi.aio.web.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.koron.wechat.wxwork.department.WXWorkDepartment;
import com.koron.wechat.wxwork.department.WXWorkDepartmentBean;
import com.koron.wechat.wxwork.department.WXWorkDepartmentResultBean;
import com.koron.wechat.wxwork.user.WXWorkUser;
import com.koron.wechat.wxwork.user.WXWorkUserBean;
import com.koron.wechat.wxwork.user.WXWorkUserResultBean;
import com.menyi.web.util.BaseEnv;

public class WXWorkSyncContact {
	/**
	 * 同步所有通讯录部门（本接口不对外开放）
	 * @return
	 */
	public static List<SyncResultBean> syncDepartment(List<SyncDepartmentBean> departmentList,
			Map<String, String> deptMap, String keyName) throws Exception{
		//父级部门
		List<SyncResultBean> syncResultList = new ArrayList<SyncResultBean>();
		Gson gson = new Gson();
		WXWorkDepartment wxWorkDepartment = new WXWorkDepartment(keyName);
		List<WXWorkDepartmentBean> oldDepartmentList = wxWorkDepartment.list().getDepartment();
		if (departmentList != null) {
			int count = 5;
			while (departmentList.size() > 0 && count <= 50) {
				for (int i = 0; i < departmentList.size(); i++) {
					SyncDepartmentBean bean = departmentList.get(i);
					if (bean.getClasscode().length() == count) {
						boolean creatFlag = true;
						//找到父id
						String parentid = "1";
						if (count != 5) {
							String code = bean.getClasscode();
							String parentCode = code.substring(0, code.length() - 5);
							parentid = deptMap.get(parentCode);
						}
						if (oldDepartmentList != null) {
							for (WXWorkDepartmentBean departmentBean : oldDepartmentList) {
								if (bean.getDeptFullName().equals(departmentBean.getName()) &&  departmentBean.getParentid().equals(Integer.valueOf(parentid))) { 
									//名字要匹配还要匹配父级部门
									creatFlag = false;
									//保存对应关系
									deptMap.put(bean.getClasscode(), String.valueOf(departmentBean.getId()));
									deptMap.put(String.valueOf(departmentBean.getId()), bean.getClasscode());
									oldDepartmentList.remove(departmentBean);
									break;
								}
							}
						}
						if (creatFlag) {						
							// 生成部门
							WXWorkDepartmentBean departmentBean = new WXWorkDepartmentBean();
							departmentBean.setName(bean.getDeptFullName());
							departmentBean.setParentid(Integer.valueOf(parentid));
							WXWorkDepartmentResultBean result = wxWorkDepartment.create(departmentBean);
							if (result != null && result.getErrcode().equals("0")) {
								//保存对应关系
								deptMap.put(bean.getClasscode(), result.getId());
								deptMap.put(result.getId(), bean.getClasscode());
								BaseEnv.log.debug("同步部门结果 --成功 "+bean.getDeptFullName());
							} else {
								SyncResultBean syncResultBean = gson.fromJson(gson.toJson(result), SyncResultBean.class);
								syncResultBean.setName(bean.getDeptFullName());
								syncResultList.add(syncResultBean);
								BaseEnv.log.debug("同步部门结果" +bean.getClasscode()+" " + syncResultBean.getName() + " " + syncResultBean.getErrcode() + " " + syncResultBean.getErrmsg());
							}
						}
					}
				}
				count += 5;
			}
		}
		
		for (int i=oldDepartmentList.size()-1;i>=0;i--) {
			WXWorkDepartmentBean departmentBean = oldDepartmentList.get(i);
			if(departmentBean.getId().equals(1)) continue;
			WXWorkDepartmentResultBean result = wxWorkDepartment.delete(departmentBean.getId());
			//删除无效的部门
			if (result != null && result.getErrcode().equals("0")) {
				//保存对应关系
				BaseEnv.log.debug("同步部门删除不存在部门结果 --成功 "+departmentBean.getName());
			} else {
				SyncResultBean syncResultBean = gson.fromJson(gson.toJson(result), SyncResultBean.class);
				syncResultBean.setName(departmentBean.getName());
				syncResultList.add(syncResultBean);
				BaseEnv.log.debug("同步部门删除不存在部门结果" + syncResultBean.getName() + " " + syncResultBean.getErrcode() + " " + syncResultBean.getErrmsg());
			}
		}
		
		return syncResultList;
	}

	/**
	 * 同步所有通讯录职员
	 * @return
	 */
	public static List<SyncResultBean> syncEmployee(List<SyncEmployeeBean> list,HashMap<String,String> deptMap, String keyName)  throws Exception{
		if (list != null) {
			BaseEnv.log.debug("同步职员数量" + list.size());
		}
		Gson gson = new Gson();
		List<SyncResultBean> syncResultList = new ArrayList<SyncResultBean>();
		
		WXWorkUser wxWorkUser = new WXWorkUser(keyName);
		WXWorkUserResultBean oldResult = wxWorkUser.simpleList("1", "1");
		if (oldResult != null && !oldResult.getErrcode().equals("0")) {
			SyncResultBean syncResultBean = gson.fromJson(gson.toJson(oldResult), SyncResultBean.class);			
			BaseEnv.log.error("同步职员失败，取企业微信职员列表不成功"+syncResultBean.getErrcode() + " " + syncResultBean.getErrmsg());			
			return syncResultList;
		}
		List<WXWorkUserBean> oldUser= oldResult.getUserlist();		
		if (list != null) {
			for (SyncEmployeeBean employeeBean : list) {
				WXWorkUserBean bean = new WXWorkUserBean();
				bean.setUserid(employeeBean.getId());
				bean.setName(employeeBean.getEmpFullName());
				bean.setPosition(employeeBean.getPosition());
				bean.setMobile(employeeBean.getMobile());
				bean.setEmail(employeeBean.getEmail());
				bean.setTelephone(employeeBean.getTel());
				String dept = deptMap.get(employeeBean.getDepartmentCode());
				if (dept == null || dept.length() ==0) {
					bean.setDepartment(new Integer[] { 1 });
				} else {
					bean.setDepartment(new Integer[] {Integer.valueOf(dept)});
				}
				WXWorkUserBean oldBean = null;
				for(WXWorkUserBean ob :oldUser){
					if(ob.getUserid().equals(bean.getUserid())){
						oldBean = ob;
						oldUser.remove(ob);
						break;
					}
				}
				if(oldBean ==null){				
					WXWorkUserResultBean result = wxWorkUser.create(bean);
					if (result != null && !result.getErrcode().equals("0")) {
						SyncResultBean syncResultBean = gson.fromJson(gson.toJson(result), SyncResultBean.class);
						syncResultBean.setName("职员 :" + "  " + bean.getName());
						syncResultList.add(syncResultBean);
						BaseEnv.log.debug("同步职员结果:添加" + bean.getName() + " " + syncResultBean.getErrcode() + " " + syncResultBean.getErrmsg());
					}
				}else{
					WXWorkUserResultBean result = wxWorkUser.update(bean);
					if (result != null && !result.getErrcode().equals("0")) {
						SyncResultBean syncResultBean = gson.fromJson(gson.toJson(result), SyncResultBean.class);
						syncResultBean.setName("职员 :" + "  " + bean.getName());
						syncResultList.add(syncResultBean);
						BaseEnv.log.debug("同步职员结果：修改" + bean.getName() + " " + syncResultBean.getErrcode() + " " + syncResultBean.getErrmsg());
					}
				}
			}
			for(WXWorkUserBean ob :oldUser){
				WXWorkUserResultBean result = wxWorkUser.delete(ob.getUserid());
				if (result != null && !result.getErrcode().equals("0")) {
					SyncResultBean syncResultBean = gson.fromJson(gson.toJson(result), SyncResultBean.class);
					syncResultBean.setName("职员 :" + "  " + ob.getName());
					syncResultList.add(syncResultBean);
					BaseEnv.log.debug("同步职员结果:删除失败" + ob.getName() + " " + syncResultBean.getErrcode() + " " + syncResultBean.getErrmsg());
				}else{
					BaseEnv.log.debug("同步职员结果：删除成功" + ob.getName() );
				}
			}
		}
		return syncResultList;
	}
}
