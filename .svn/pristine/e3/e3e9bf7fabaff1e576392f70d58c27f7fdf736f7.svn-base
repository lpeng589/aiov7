package com.koron.oa.office.meeting;

import com.dbfactory.Result;
import com.menyi.web.util.AIODBManager;

public class OANoteMgt extends AIODBManager {
	/**
	 * 新增
	 * 
	 * @param    oanote
	 * @return
	 */
	public  Result addNote(OANoteBean bean){
		return addBean(bean);
	}
	
	/**
	 * 根据关系id 查询 oanote
	 *
	 * @param  String
	 * @return Result
	 */
	public Result loadNote(final String id) {
		return loadBean(id, OANoteBean.class);
	}

	/**
	 * 修改oanote
	 * @return Result
	 */
	public Result updateNote(final OANoteBean  bean) {
		return updateBean(bean);
	}
	
	
	/**
	 * 
	 * 删除与meetingId 相关的笔记
	 */
	public Result deleteNotes(final String meetingId) {
		return deleteBean(meetingId, OANoteBean.class, "meetingId");
	}
}
