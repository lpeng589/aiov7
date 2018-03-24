package com.koron.crm.openSelect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.crm.bean.WorkProfessionBean;
import com.koron.crm.bean.WorkTradeBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;


/**
 * 
 * 
 * <p>Title:行业,地区查询Mgt</p> 
 * <p>Description: </p>
 *
 * @Date:2012-6-13
 * @Copyright: 科荣软件
 * @Author 徐洁俊
 */
public class CRMopenSelectMgt extends AIODBManager{
	
	
	/**
	 * 查找所有的行业
	 * @param fileId
	 * @return
	 */
	public Result findWordTrades(){
		List list = new ArrayList();
		String sql = "from WorkTradeBean order by createTime DESC";
		return this.list(sql, list);
		
	}
	
	
	public Result findDistricts(String province,String city){
		List list = new ArrayList();
		String sql = "select distinct";
		if(city == null || "".equals(city)){
			if(province == null){
				sql = "select * from (select ViewDistrict.Province province ,ViewDistrict.classCode classCode,ROW_NUMBER() over( order by ViewDistrict.classCode) as row_id from ViewDistrict left join CRMBusinessDistrictDet on ViewDistrict.classCode=CRMBusinessDistrictDet.District where 1=1  and ViewDistrict.classCode like '00001_____') a where  row_id between 1 and 100";
			}else{
				sql += " city,classCode FROM tblDistrict WHERE province ='" + province + "' and len(classCode)=15" ;
			}
		}else{
			sql += " area,DistrictFullName,classCode FROM tblDistrict WHERE province = '" + province + "' and city ='" + city + "' and len(classCode)=20";
		}
		return this.sqlList(sql, list);
	}
	
	public Result findForegins(){
		String sql = "select classCode,country from tbldistrict where classCode like '00002%' and classCode != '00002'";
		List list = new ArrayList();
		return this.sqlList(sql, list);
	}
	
	public Result findDistrictByFullName(String fucllName){
		String sql = "select classCode from tblDistrict where DistrictFullName = '" + fucllName + "'";
		List list = new ArrayList();
		return this.sqlList(sql, list);
	}
	
	public Result findTradeByFullName(String fucllName){
		String hql = "from WorkProfessionBean  where name  = '" + fucllName + "'";
		List list = new ArrayList();
		return this.list(hql, list);
	}
	
	public Result findDis(String range,String keyWord,String selOption){
		String sql = "";
		if("province".equals(range)){
			sql = "select * from (select ViewDistrict.Province province ,ViewDistrict.classCode classCode,districtFullName,ROW_NUMBER() over( order by ViewDistrict.classCode) as row_id from ViewDistrict where 1=1  and ViewDistrict.classCode like '00001_____') a where  row_id between 1 and 100";
			if(keyWord != null && !"关键字搜索".equals(keyWord)){
				if("province".equals(selOption) || "all".equals(selOption)){
					sql +=" and province like '%"+keyWord+"%'";
				}
			}
		}else if("city".equals(range)){
			sql = "select city,classCode,districtFullName FROM tblDistrict WHERE len(classCode)=15";
			if(keyWord != null && !"关键字搜索".equals(keyWord)){
				if("city".equals(selOption)|| "all".equals(selOption)){
					sql +=" and city like '%"+keyWord+"%'";
				}
			}
		}else{
			if(keyWord != null && "关键字搜索".equals(keyWord)){
				keyWord = "";
			}
			sql = "select area,classCode,districtFullName FROM tblDistrict WHERE len(classCode)=20 and area like '%"+keyWord+"%' ";
		}
		List list = new ArrayList();
		return this.sqlList(sql, list);
	}
	
	public Result findareas(String cityId){
		String sql ="select area,classCode,districtFullName from tbldistrict where classcode like '"+cityId+"%' and len(classCode)=20";
		return sqlList(sql, new ArrayList());
	}
	
	/**
	 * 添加行业
	 * @param bean
	 * @return
	 */
	public Result addProfession(WorkProfessionBean bean){
		return this.addBean(bean);
	}
	
	public Result loadWorkTrade(String workTradeId){
		return this.loadBean(workTradeId, WorkTradeBean.class);
	}
	
	public Result delProfession(String professionId){
		return deleteBean(professionId, WorkProfessionBean.class, "id");
	}
	
	/**
	 * 删除行业总类
	 * @param moduleId
	 * @return
	 */
	public Result delWorkTrade(final String workTradeId){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							Statement stmt = connection.createStatement();
							stmt.addBatch("DELETE FROM CRMWorkProfession WHERE workTradeId='"+workTradeId+"'");
							stmt.addBatch("DELETE FROM CRMWorkTrade WHERE id='"+workTradeId+"'");
							stmt.executeBatch();
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
	
	/**
	 * 添加总称
	 * @param moduleId
	 * @return
	 */
	public Result addWorkTrade(final List<String> sqlList){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							Statement stmt = connection.createStatement();
							for(String sql : sqlList){
								stmt.addBatch(sql);
							}
							stmt.executeBatch();
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
	
	/**
	 * 查看客户使用行业个数
	 * @param moduleId
	 * @return
	 */
	public Result checkTradeCount(final String tradeId,final String checkFlag){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql ="";
							if("profession".equals(checkFlag)){
								sql = "SELECT count(*) as tradeCount FROM CRMClientInfo WHERE trade=?";
							}else{
								sql = "SELECT count(*) as tradeCount FROM CRMClientInfo WHERE trade in (select id from CRMWorkProfession where workTradeId =?)";
								
							}
							PreparedStatement ps = connection.prepareStatement(sql);
							ps.setString(1, tradeId);
							ResultSet rss = ps.executeQuery();
							int count =0;
							while(rss.next()){
								count = rss.getInt("tradeCount");
							}
							rst.setRetVal(count);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
}
