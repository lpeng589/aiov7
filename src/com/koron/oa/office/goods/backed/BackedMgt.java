package com.koron.oa.office.goods.backed;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.office.goods.GoodsSearchForm;
import com.koron.oa.office.goods.applyUse.ApplyUseMgt;
import com.koron.oa.office.goods.applyUse.OAApplyGoodsBean;
import com.koron.oa.office.goods.applyUse.OAApplyGoodsDetBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.GlobalsTool;

public class BackedMgt extends AIODBManager{
	ApplyUseMgt aioApply = new ApplyUseMgt();
	/**
	 * ��ѯ���й黹��¼
	 * @param lvForm
	 * @return
	 */
	public Result getBack(GoodsSearchForm lvForm){
		List param = new ArrayList();
		String hql = "from OABackedGoodsBean as bean where '1' = ?";
		param.add("1");
		hql+=" order by bean.backNO asc";		
		return list(hql, param,lvForm.getPageNo(),lvForm.getPageSize(),true);	
	}
	/**
	 * ����ID��ѯ��ϸ
	 * @param keyId 
	 * @return
	 */
	public Result getDetById(GoodsSearchForm lvForm,String keyId){
		List param = new ArrayList();
		String hql = "from OABackedGoodsDetBean as bean where '1' = ? and bean.id in ("+keyId+")";
		param.add("1");
		hql+=" order by bean.id asc";		
		return list(hql, param, lvForm.getPageNo(), lvForm.getPageSize(),true);	
	}
	 /**
	  * ���������ѯ��ϸ��id����Ʒid
	  * @return
	  */
	public Result getByBackID(String key){
		List param = new ArrayList();
		String sql = "select id,backedQty from OABackedGoodsInfoDet WHERE  b_ref = '"+key+"'";	
		sql+=" and 1 = ? order by id desc";		
		param.add("1");
		return sqlList(sql, param);	
	}
	/**
	 * ������ѯ�黹��¼
	 * @param lvForm
	 * @return
	 */
	public Result getBackBy(GoodsSearchForm lvForm,String title){
		List param = new ArrayList();
		String sql = " from OABackedGoodsBean G  WHERE 1=1 ";	
		if(title!=null && !"".equals(title)){
			sql+=" and G.id in ("+title+")";
		}
		if(lvForm.getApplyRole()!=null && !"".equals(lvForm.getApplyRole())){
			sql+=" and G.backedRole like '%"+lvForm.getApplyRole()+"%'";
		}
		if(lvForm.getBeginTime()!=null && !"".equals(lvForm.getBeginTime())){
			sql+=" and convert(varchar,G.backDate,102) >= '"+lvForm.getBeginTime()+"'";
		}
		if(lvForm.getEndTime()!=null && !"".equals(lvForm.getEndTime())){
			sql+=" and convert(varchar,G.backDate,102) <= '"+lvForm.getEndTime()+"'";
		}		
		sql+=" and '2' = ? order by G.id asc";		
		param.add("2");
		return list(sql, param,lvForm.getPageNo(),lvForm.getPageSize(),true);	
	}
	
	/**
	 * ������Ϣ�����ƻ�ȡ�黹��id
	 * @param title
	 * @return
	 */
	public Result turnId(String title){	
		List param = new ArrayList();
		String sql = "select D.b_ref from OABackedGoodsInfoDet D left join  oaapplygoodsinfodet A on D.backId = A.id where A.goodsname like '%"+title+"%'";
		sql+=" and '2' = ? ";		
		param.add("2");
		return sqlList(sql, param);
	}

	/**
	 * �޸�ʱɾ��
	 * @return
	 */
	public Result delUpBack(final String ID,final String[] detID,final OABackedGoodsBean bean,
			final String[] backedQty,final String[] id){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {				
				try {	
					if(detID.length >0){
						for(int i=0;i<detID.length;i++){																		
							//ɾ����ϸ��
							deleteBean(detID[i], OABackedGoodsDetBean.class, "id", session);
						}	
					}												
					deleteBean(ID, OABackedGoodsBean.class, "id",session);
					for(int i=0;i<id.length;i++){					
						OAApplyGoodsDetBean applyDetbean = (OAApplyGoodsDetBean)loadBean(id[i], OAApplyGoodsDetBean.class,session).retVal;
						//���������ϸ�й黹����						
						applyDetbean.setBack_sign(GlobalsTool.round((Float.parseFloat(backedQty[i]==""?"0":backedQty[i])),GlobalsTool.getDigits()));
						updateBean(applyDetbean,session);
						
					}									
					addBean(bean,session);
				} catch (Exception ex) {
					ex.printStackTrace();
					BaseEnv.log.error("BackedMgt delUpBack : ", ex) ;
				}
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;		
	} 
	
	/**
	 * ɾ���黹��¼,��������
	 * @param ID
	 * @return
	 */
	public Result deleteBack(final String[] ID,final String[] arrDetId){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {				
				try {						
					if(arrDetId.length >0){
						for(int i=0;i<arrDetId.length;i++){	
							//������ϸ
							OABackedGoodsDetBean backedbean = (OABackedGoodsDetBean)loadBean(arrDetId[i], OABackedGoodsDetBean.class, session).retVal;							
							OAApplyGoodsDetBean applybean = (OAApplyGoodsDetBean)loadBean(backedbean.getBackId(), OAApplyGoodsDetBean.class, session).retVal;
							
							//���ٱ��
							applybean.setBack_sign(GlobalsTool.round((applybean.getBack_sign())-(backedbean.getBackedQty()),GlobalsTool.getDigits()));			
							updateBean(applybean, session);
							//ɾ����ϸ��
							deleteBean(arrDetId[i], OABackedGoodsDetBean.class, "id", session);
						}	
					}	
					if(ID !=null){
						for (int i = 0; i < ID.length; i++) {
							deleteBean(ID[i], OABackedGoodsBean.class, "id",session);
						}	
					}								
				} catch (Exception ex) {
					ex.printStackTrace();
					BaseEnv.log.error("BackedMgt deleteBack : ", ex) ;
				}
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;		
	} 

	
	/**
	 * ɾ�������黹����
	 * @param idDet
	 * @return
	 */
	public Result delBack(String[] idDet){
		return deleteBean(idDet, OABackedGoodsBean.class, "id");
	} 
	/**
	 * ɾ�������黹��¼
	 * @param ID
	 * @return
	 */
	public Result getByBackArr(String[] BackID){
		List param = new ArrayList();//
		String BackIDs = "";
		for (int i = 0; i < BackID.length; i++) {
			BackIDs += ",'"+BackID[i]+"'";
		}
		String sql = "select id,backedQty from OABackedGoodsInfoDet where b_ref in ("+BackIDs.substring(1)+")";				
		sql+=" and 1 = ? order by id desc";		
		param.add("1");
		return sqlList(sql, param);	
	} 
	
	/**
	 * ���ݵ��Ż�ȡ���ü�¼id
	 * @param ID
	 * @return
	 */
	public Result getBackID(String ID){
		String sql = "select id from OABackedGoodsInfo where 1=1 and backNO = ?";
		List param = new ArrayList();
		param.add(ID);
		return sqlList(sql, param);
	} 
	/**
	 * ����黹����
	 * @param bean
	 * @return
	 */
	public Result saveBack(final OABackedGoodsBean bean,final String[] backedQty,final String[] id){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {				
				try {					
					for(int i=0;i<id.length;i++){					
						OAApplyGoodsDetBean applyDetbean = (OAApplyGoodsDetBean)loadBean(id[i], OAApplyGoodsDetBean.class,session).retVal;			
						//���������ϸ�й黹����
						
						applyDetbean.setBack_sign(GlobalsTool.round((Float.parseFloat(backedQty[i])+(applyDetbean.getBack_sign())),GlobalsTool.getDigits()));											
						updateBean(applyDetbean,session);
						
					}									
					addBean(bean,session);
				} catch (Exception ex) {
					ex.printStackTrace();
					BaseEnv.log.error("BackedMgt saveBack : ", ex) ;
				}
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;
	}
	/**
	 * �õ�������Ϣ
	 * @param role
	 * @return
	 */
	public Result getApplyDet(GoodsSearchForm lvForm,String role,String goods,String time){
		String sql = "from OAApplyGoodsDetBean " +
				"where back_sign < applyQty ";
		if(role !=null && !"".equals(role) ){
			sql += " and applyGoodsBean.applyRole like '%"+role+"%'";
		};
		if(time !=null && !"".equals(time) ){
			sql += " and applyGoodsBean.applyDate = '"+time+"'";
		};
		if(goods !=null && !"".equals(goods) ){
			sql += " and goodsName like '%"+goods+"%'";
		};
		sql +=" and '1' = ? order by id asc";
		List param = new ArrayList();
		param.add("1");
		return list(sql, param, lvForm.getPageNo(), lvForm.getPageSize(), true);
	}
	
	public Result loadDet(String id){
		return loadBean(id, OABackedGoodsDetBean.class);
	}
	
	public Result loadBack(String id){
		return loadBean(id, OABackedGoodsBean.class);
	}
	
	public Result updateBack(OABackedGoodsBean bean){
		return updateBean(bean);
	}
}
