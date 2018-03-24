package com.koron.hr.textUpload;


import com.dbfactory.Result;
import com.koron.hr.bean.HRCardRecordPositionBean;
import com.menyi.web.util.AIODBManager;

public class HRTextUploadMgt extends AIODBManager {

	/**
	 * 查找打卡记录卡号、日期、时间打卡位置，默认ID为1
	 * @param fileId
	 * @return
	 */
	public Result findHRCardRecordPosition(){
		return loadBean("1", HRCardRecordPositionBean.class);
	}
	
	
	/**
	 * 更新打卡记录卡号、日期、时间打卡位置
	 * @param hRCardRecordPositionBean
	 */
	public void updateHRCardRecordPosition(HRCardRecordPositionBean hRCardRecordPositionBean){
		
		this.updateBean(hRCardRecordPositionBean);
	}
}
