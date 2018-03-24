package com.koron.oa.office.goods.applyUse;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.office.goods.GoodsSearchForm;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.GlobalsTool;

public class ApplyUseMgt extends AIODBManager{
 
	/**
	 * 查询领用明细
	 * @return
	 */ 
	public Result queryApply(GoodsSearchForm lvForm){
		List param = new ArrayList();
		String hql = "from OAApplyGoodsDetBean as bean where '1' = ?";
		param.add("1");
		hql+=" order by bean.id desc";		
		return list(hql, param,lvForm.getPageNo(),lvForm.getPageSize(),true);	
	}
	/**
	 * 查询领用记录
	 * @param lvForm
	 * @return
	 */
	public Result getByApply(GoodsSearchForm lvForm){
		List param = new ArrayList();
		String hql = "from OAApplyGoodsBean as bean where '1' = ?";
		param.add("1");
		hql+=" order by bean.applyNO asc";		
		return list(hql, param,lvForm.getPageNo(),lvForm.getPageSize(),true);	
	}
	/**
	 * 根据ID查询明细
	 * @param keyId 
	 * @return
	 */
	public Result getDetById(GoodsSearchForm lvForm,String keyId){
		List param = new ArrayList();
		String hql = "from OAApplyGoodsDetBean as bean where '1' = ? and bean.id in ("+keyId+")";
		param.add("1");
		hql+=" order by bean.id asc";		
		return list(hql, param,lvForm.getPageNo(),lvForm.getPageSize(),true);	
	}
	/**
	 * 领用基本表
	 * @param lvForm
	 * @return
	 */
	public Result qyOffice(GoodsSearchForm lvForm){
		List<String> param = new ArrayList<String>();
		String hql = "from OABasicGoodsBean as bean where 1=1";
		if(!"".equals(lvForm.getGoodsName()) && lvForm.getGoodsName()!=null){
			hql+=(" and bean.goodsName like ? ");
			param.add("%"+lvForm.getGoodsName()+"%");
		}
		if(!"".equals(lvForm.getType()) && lvForm.getType()!=null){
			hql+=(" and bean.type = ? ");
			param.add(lvForm.getType());
		}
		hql+=" and bean.qty > 0 order by bean.id asc";		
		return list(hql, param,lvForm.getPageNo(),lvForm.getPageSize(),true);	
	}
	 /**
	  * 根据外键查询明细表id和用品id
	  * @return
	  */
	public Result getByApplyID(String key){
		List param = new ArrayList();
		String sql = "select id,applyQty,back_sign from OAApplyGoodsInfoDet WHERE  a_ref = '"+key+"'";	
		sql+=" and 1 = ? order by id asc";		
		param.add("1");
		return sqlList(sql, param);	
	}
	/**
	 * 根据明细表id获取用品id
	 * @param key
	 * @return
	 */
	public Result getIDByID(String key){
		List param = new ArrayList();
		String sql = "select a_ref from OAApplyGoodsInfoDet WHERE  id = '"+key+"'";	
		sql+=" and 1 = ? order by id asc";		
		param.add("1");
		return sqlList(sql, param);	
	}
	/**
	 * 条件查询领用记录
	 * @param lvForm
	 * @return
	 */
	public Result getApplyBy(GoodsSearchForm lvForm,String title){
		List param = new ArrayList();
		String sql = " from OAApplyGoodsBean G  WHERE 1=1 ";	
		if(title!=null && !"".equals(title)){
			sql+=" and G.id in ("+title+")";
		}
		if(lvForm.getApplyRole()!=null && !"".equals(lvForm.getApplyRole())){
			sql+=" and G.applyRole like '%"+lvForm.getApplyRole()+"%'";
		}
		if(lvForm.getBeginTime()!=null && !"".equals(lvForm.getBeginTime())){
			sql+=" and convert(varchar,G.applyDate,102) >= '"+lvForm.getBeginTime()+"'";
		}
		if(lvForm.getEndTime()!=null && !"".equals(lvForm.getEndTime())){
			sql+=" and convert(varchar,G.applyDate,102) <= '"+lvForm.getEndTime()+"'";
		}		
		sql+=" and '2' = ? order by applyNO asc";		
		param.add("2");
		return list(sql, param,lvForm.getPageNo(),lvForm.getPageSize(),true);	
	}
	/**
	 * 根据信息表名称获取领用表id
	 * @param title
	 * @return
	 */
	public Result turnId(String title){	
		List param = new ArrayList();
		String sql = "select a_ref from oaapplygoodsinfodet where goodsname like '%"+title+"%'";
		sql+=" and '2' = ? ";		
		param.add("2");
		return sqlList(sql, param);
	}

	/**
	 * 修改删除
	 * @return
	 */
	public Result delUpApply(final String ID,final String[] detID,
			final OAApplyGoodsBean bean){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {				
				try {	
					if(detID.length >0){
						for(int i=0;i<detID.length;i++){	
							//删除明细表
							deleteBean(detID[i], OAApplyGoodsDetBean.class, "id", session);
						}	
					}												
					deleteBean(ID, OAApplyGoodsBean.class, "id",session);	
					bean.setId(ID);
					addBean(bean,session);
				} catch (Exception ex) {
					ex.printStackTrace();
					BaseEnv.log.error("ApplyUseMgt delUpApply : ", ex) ;
				}
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;	
	}
	
	/**
	 * 删除领用记录
	 * @param ID
	 * @return
	 */
	public Result deleteApply(final String ID,final String[] arrDetId){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {				
				try {	
					if(arrDetId.length >0){
						for(int i=0;i<arrDetId.length;i++){																		
							//删除明细表
							deleteBean(arrDetId[i], OAApplyGoodsDetBean.class, "id", session);
						}	
					}												
					deleteBean(ID, OAApplyGoodsBean.class, "id",session);
				} catch (Exception ex) {
					ex.printStackTrace();
					BaseEnv.log.error("ApplyUseMgt deleteApply : ", ex) ;
				}
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;	
	} 
	/**
	 * 根据applyNO获取领用记录id
	 * @param ID
	 * @return
	 */
	public Result getApplyID(String ID){
		String sql = "select id from OAApplyGoodsInfo where 1=? and applyNO = '"+ID+"'";
		List param = new ArrayList();
		param.add("1");
		return sqlList(sql, param);
	} 
	/**
	 * 保存订单
	 * @param bean
	 * @return
	 */
	public Result saveApply(final OAApplyGoodsBean bean){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {				
				try {														
					addBean(bean,session);
				} catch (Exception ex) {
					ex.printStackTrace();
					BaseEnv.log.error("ApplyUseMgt saveApply : ", ex) ;
				}
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;
	}
	/**
	 * 加载领用明细表
	 * @param id
	 * @return
	 */
	public Result loadApplyDet(String id){
		return loadBean(id, OAApplyGoodsDetBean.class);
	}
	/**
	 *更新领用明细
	 * @param bean
	 * @return
	 */
	public Result upApplyDet(OAApplyGoodsDetBean bean){
		return updateBean(bean);
	}
	
	/**
	 * 加载领用
	 * @param id
	 * @return
	 */
	public Result loadApply(String id){
		return loadBean(id, OAApplyGoodsBean.class);
	}
	
	public Result updateApply(OAApplyGoodsBean bean){
		return updateBean(bean);
	}
	
	/**
	 * 删除明细
	 * @param id
	 * @return
	 */
	public Result delDet(final String[] id,final String ID){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {				
				try {						
					
					if(id.length >0){						
						for(int i=0;i<id.length;i++){
							//删除
							deleteBean(id[i], OAApplyGoodsDetBean.class, "id", session);
						}
					}				
				} catch (Exception ex) {
					ex.printStackTrace();
					BaseEnv.log.error("ApplyUsesMgt delDet : ", ex) ;
				}
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;
	}
	/**
	 * 删除空单号
	 * @param id
	 * @return
	 */
	public Result delApply(String[] id){
		return deleteBean(id, OAApplyGoodsBean.class,"id");
	}
	
	/**
	 * 获取明细id,back_sign,applyQty,apply_total
	 * @param ID
	 * @return
	 */
	public Result getByApplyArr(String[] applyID){
		List param = new ArrayList();//
		String applyIDs = "";
		for (int i = 0; i < applyID.length; i++) {
			applyIDs += ",'"+applyID[i]+"'";
		}
		String sql = "select id,back_sign,applyQty from OAApplyGoodsInfoDet where a_ref in ("+applyIDs.substring(1)+")";				
		sql+=" and 1 = ? order by id desc";		
		param.add("1");
		return sqlList(sql, param);	
	} 
	
	/**
	 * 删除明细
	 * @param idDet
	 * @return
	 */
	public Result delApplyDet(final String[] idDet){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {				
				try {	
					if(idDet.length >0){
						for(int i=0;i<idDet.length;i++){
							//删除
							deleteBean(idDet[i], OAApplyGoodsDetBean.class, "id", session);
						}	
					}												
				} catch (Exception ex) {
					ex.printStackTrace();
					BaseEnv.log.error("BuyGoodsMgt delGoodsDet : ", ex) ;
				}
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;	
	} 
	
}
