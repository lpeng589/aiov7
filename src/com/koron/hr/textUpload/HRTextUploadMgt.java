package com.koron.hr.textUpload;


import com.dbfactory.Result;
import com.koron.hr.bean.HRCardRecordPositionBean;
import com.menyi.web.util.AIODBManager;

public class HRTextUploadMgt extends AIODBManager {

	/**
	 * ���Ҵ򿨼�¼���š����ڡ�ʱ���λ�ã�Ĭ��IDΪ1
	 * @param fileId
	 * @return
	 */
	public Result findHRCardRecordPosition(){
		return loadBean("1", HRCardRecordPositionBean.class);
	}
	
	
	/**
	 * ���´򿨼�¼���š����ڡ�ʱ���λ��
	 * @param hRCardRecordPositionBean
	 */
	public void updateHRCardRecordPosition(HRCardRecordPositionBean hRCardRecordPositionBean){
		
		this.updateBean(hRCardRecordPositionBean);
	}
}
