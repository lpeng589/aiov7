package com.koron.oa.office.meeting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbfactory.Result;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;

public class OABoardroomMgt extends AIODBManager{
	public Result getBoardroomByMeeting(String[] boardroomIds){
		List param = new ArrayList();
		String sql="select id,sponsor,b.boardroomId from ( ";
		sql += " select * from oameeting   where  regularMeeting=0 and endTime >= getDate() ";
		sql += "  union  ";
		sql += " select * from oameeting   where regularMeeting<>0 ";
		sql += " )  b  where status is  null and boardroomId  in( ";
		for(int i=0;i<boardroomIds.length;i++){
			sql += "?";
			param.add(boardroomIds[i]);
			if(i!=boardroomIds.length-1){
				sql += ",";
			}
		}
		sql += " ) ";
		return sqlList(sql, param,  10000,1, true);
	}

	
	public Map<String, OABoardroomBean> getBoardroomMap() {
		Map boardroomMap =new HashMap();
		Result result=queryAll();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<OABoardroomBean>  beans=(List<OABoardroomBean>)result.getRetVal();
			for(OABoardroomBean bean : beans){
				boardroomMap.put(bean.getBoardroomId(),bean);
			}
		}
		return boardroomMap;
	}

	

	/**
	 * 新增会议室
	 * 
	 * @param OABoardroomBean
	 * @return
	 */
	public  Result addBoardroom(OABoardroomBean bean){		
		return addBean(bean);
	}
	
	/**
	 * 根据id 查询 oaboardroom
	 *
	 * @param  String
	 * @return Result
	 */
	public Result loadBoardroom(final String id) {
		Result result= loadBean(id, OABoardroomBean.class);
		return result;
	}

	/**
	 * 修改oaboardroom
	 * @return Result
	 */
	public Result updateBoardroom(final OABoardroomBean  bean) {		
		Result result= updateBean(bean);
		return result;
	}
	
	
	/**
	 * 删除 oaboardroom
	 * @return Result
	 */
	public Result deleteBoardroom(final String id ){
		Result result= deleteBean(id, OABoardroomBean.class,"boardroomId");
		return result;
	}
	/**
	 * 删除 oaboardroom多个
	 * @return Result
	 */
	public Result deleteBoardroom(final String[] ids ){
		Result result= deleteBean(ids, OABoardroomBean.class,"boardroomId");
		return result;
	}
	
	
	/**
	 * 查询所有
	 * */
	public Result queryAll() {
		//创建参数
		List<String> param = new ArrayList<String>();
		String hql = "from OABoardroomBean bean where 1=1 ";
		//调用list返回结果
		return list(hql, param, 1, 100, true);
	}
	
	
	
	
}
