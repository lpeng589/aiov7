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
	 * ͬ������ͨѶ¼���ţ����ӿڲ����⿪�ţ�
	 * @return
	 */
	public static List<SyncResultBean> syncDepartment(List<SyncDepartmentBean> departmentList,
			Map<String, String> deptMap, String keyName) throws Exception{
		//��������
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
						//�ҵ���id
						String parentid = "1";
						if (count != 5) {
							String code = bean.getClasscode();
							String parentCode = code.substring(0, code.length() - 5);
							parentid = deptMap.get(parentCode);
						}
						if (oldDepartmentList != null) {
							for (WXWorkDepartmentBean departmentBean : oldDepartmentList) {
								if (bean.getDeptFullName().equals(departmentBean.getName()) &&  departmentBean.getParentid().equals(Integer.valueOf(parentid))) { 
									//����Ҫƥ�仹Ҫƥ�丸������
									creatFlag = false;
									//�����Ӧ��ϵ
									deptMap.put(bean.getClasscode(), String.valueOf(departmentBean.getId()));
									deptMap.put(String.valueOf(departmentBean.getId()), bean.getClasscode());
									oldDepartmentList.remove(departmentBean);
									break;
								}
							}
						}
						if (creatFlag) {						
							// ���ɲ���
							WXWorkDepartmentBean departmentBean = new WXWorkDepartmentBean();
							departmentBean.setName(bean.getDeptFullName());
							departmentBean.setParentid(Integer.valueOf(parentid));
							WXWorkDepartmentResultBean result = wxWorkDepartment.create(departmentBean);
							if (result != null && result.getErrcode().equals("0")) {
								//�����Ӧ��ϵ
								deptMap.put(bean.getClasscode(), result.getId());
								deptMap.put(result.getId(), bean.getClasscode());
								BaseEnv.log.debug("ͬ�����Ž�� --�ɹ� "+bean.getDeptFullName());
							} else {
								SyncResultBean syncResultBean = gson.fromJson(gson.toJson(result), SyncResultBean.class);
								syncResultBean.setName(bean.getDeptFullName());
								syncResultList.add(syncResultBean);
								BaseEnv.log.debug("ͬ�����Ž��" +bean.getClasscode()+" " + syncResultBean.getName() + " " + syncResultBean.getErrcode() + " " + syncResultBean.getErrmsg());
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
			//ɾ����Ч�Ĳ���
			if (result != null && result.getErrcode().equals("0")) {
				//�����Ӧ��ϵ
				BaseEnv.log.debug("ͬ������ɾ�������ڲ��Ž�� --�ɹ� "+departmentBean.getName());
			} else {
				SyncResultBean syncResultBean = gson.fromJson(gson.toJson(result), SyncResultBean.class);
				syncResultBean.setName(departmentBean.getName());
				syncResultList.add(syncResultBean);
				BaseEnv.log.debug("ͬ������ɾ�������ڲ��Ž��" + syncResultBean.getName() + " " + syncResultBean.getErrcode() + " " + syncResultBean.getErrmsg());
			}
		}
		
		return syncResultList;
	}

	/**
	 * ͬ������ͨѶ¼ְԱ
	 * @return
	 */
	public static List<SyncResultBean> syncEmployee(List<SyncEmployeeBean> list,HashMap<String,String> deptMap, String keyName)  throws Exception{
		if (list != null) {
			BaseEnv.log.debug("ͬ��ְԱ����" + list.size());
		}
		Gson gson = new Gson();
		List<SyncResultBean> syncResultList = new ArrayList<SyncResultBean>();
		
		WXWorkUser wxWorkUser = new WXWorkUser(keyName);
		WXWorkUserResultBean oldResult = wxWorkUser.simpleList("1", "1");
		if (oldResult != null && !oldResult.getErrcode().equals("0")) {
			SyncResultBean syncResultBean = gson.fromJson(gson.toJson(oldResult), SyncResultBean.class);			
			BaseEnv.log.error("ͬ��ְԱʧ�ܣ�ȡ��ҵ΢��ְԱ�б��ɹ�"+syncResultBean.getErrcode() + " " + syncResultBean.getErrmsg());			
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
						syncResultBean.setName("ְԱ :" + "  " + bean.getName());
						syncResultList.add(syncResultBean);
						BaseEnv.log.debug("ͬ��ְԱ���:���" + bean.getName() + " " + syncResultBean.getErrcode() + " " + syncResultBean.getErrmsg());
					}
				}else{
					WXWorkUserResultBean result = wxWorkUser.update(bean);
					if (result != null && !result.getErrcode().equals("0")) {
						SyncResultBean syncResultBean = gson.fromJson(gson.toJson(result), SyncResultBean.class);
						syncResultBean.setName("ְԱ :" + "  " + bean.getName());
						syncResultList.add(syncResultBean);
						BaseEnv.log.debug("ͬ��ְԱ������޸�" + bean.getName() + " " + syncResultBean.getErrcode() + " " + syncResultBean.getErrmsg());
					}
				}
			}
			for(WXWorkUserBean ob :oldUser){
				WXWorkUserResultBean result = wxWorkUser.delete(ob.getUserid());
				if (result != null && !result.getErrcode().equals("0")) {
					SyncResultBean syncResultBean = gson.fromJson(gson.toJson(result), SyncResultBean.class);
					syncResultBean.setName("ְԱ :" + "  " + ob.getName());
					syncResultList.add(syncResultBean);
					BaseEnv.log.debug("ͬ��ְԱ���:ɾ��ʧ��" + ob.getName() + " " + syncResultBean.getErrcode() + " " + syncResultBean.getErrmsg());
				}else{
					BaseEnv.log.debug("ͬ��ְԱ�����ɾ���ɹ�" + ob.getName() );
				}
			}
		}
		return syncResultList;
	}
}
