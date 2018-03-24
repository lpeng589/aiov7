package com.koron.crm.qa;

import java.util.ArrayList;
import java.util.List;

import com.dbfactory.Result;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.MgtBaseAction;

public class CRMQAMgt  extends AIODBManager{
	/**
	 * 查询
	 * */
	public Result query(CRMQASearchForm form) {
		//创建参数
		List<String> param = new ArrayList<String>();
		String hql = "from CRMQABean bean where 1=1 ";
		if(form != null){
			if(form.getRef_id() !=null && !form.getRef_id().trim().equals("")){
				hql += "  and  ref_id  like ? ";
				param.add("%"+form.getRef_id().trim()+"%");
			}
			
			if(form.getAnswer() != null && !form.getAnswer().trim().equals("")){
				hql += "  and  answer  like ? ";
				param.add("%"+form.getAnswer().trim()+"%");
			}
			
			if(form.getCreateStartTime() != null && !form.getCreateStartTime().trim().equals("")){
				if(form.getCreateEndTime() != null && !form.getCreateEndTime().trim().equals("")){
					hql += "  and ( createTime  between ? and ?  or createTime  like ? )  ";
					param.add(form.getCreateStartTime().trim());
					param.add(form.getCreateEndTime().trim());
					param.add("%"+form.getCreateEndTime().trim()+"%");
				}else{
					hql += "  and  createTime  like ? ";
					param.add("%"+form.getCreateStartTime().trim()+"%");
				}
			}
			
		}
		hql += "  order by  createTime desc ";
		//调用list返回结果
		return list(hql, param, form.getPageNo(), form.getPageSize(), true);
	}
	
	public Result add(CRMQABean bean){
		return addBean(bean);
	}
	
	public Result update(CRMQABean bean){
		return updateBean(bean);
	}
	public Result deleteById(String[] ids){
		return deleteBean(ids, CRMQABean.class, "id");
	}
	
	public Result detail(String id){
		return loadBean(id, CRMQABean.class);
	}
}
