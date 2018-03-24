package com.menyi.aio.web.mobile;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.menyi.web.util.AppleApnsSend;
import com.menyi.web.util.SystemState;

/**
 * ����ǩ��ǩ��֪ͨ�����˺��ϼ�
 * @author Sanhong
 *
 */
public class AttendanceNotice {
	private static Gson gson = new Gson();
	
	/**
	 * 
	 * @param directorBoss �ϼ�id
	 * @param noticePersons ������id,���ʹ��;�ָ�
	 * @param createBy ������Ա������
	 * @param content ֪ͨ����
	 * @param id
	 */
	public  void signNotice(String directorBoss, String noticePersons, String createBy,  String id) {
		if(noticePersons.indexOf(directorBoss) == -1) {
			noticePersons += ";" + directorBoss;
		}
		String[] persons = noticePersons.split(";");
		String content = createBy;
		Map<String, Object> map = new HashMap<String, Object>();
		for (String person : persons) {
			map.put("userId", person);
			map.put("content", "mobilevue/attendance/det/" + id);
			new AppleApnsSend(String.valueOf(SystemState.instance.dogId), "", content, gson.toJson(map), "").start();			
		}
	}

}
