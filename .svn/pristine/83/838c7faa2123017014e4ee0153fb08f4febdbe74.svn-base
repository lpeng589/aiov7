package com.menyi.aio.web.mobile;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.menyi.web.util.AppleApnsSend;
import com.menyi.web.util.SystemState;

/**
 * 考勤签入签出通知抄送人和上级
 * @author Sanhong
 *
 */
public class AttendanceNotice {
	private static Gson gson = new Gson();
	
	/**
	 * 
	 * @param directorBoss 上级id
	 * @param noticePersons 抄送人id,多个使用;分隔
	 * @param createBy 外勤人员的名字
	 * @param content 通知内容
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
