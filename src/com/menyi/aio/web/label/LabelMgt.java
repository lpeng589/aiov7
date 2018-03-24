package com.menyi.aio.web.label;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.LabelBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
/**
 * 
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:2012-7-5
 * @Copyright: 科荣软件
 * @Author 方家俊
 */
public class LabelMgt extends AIODBManager{
	
	

	
	public Result updateSysDeploy(final String sysCode,final String value){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
		
			@Override
			public int exec(Session session) {
				session.doWork(new Work() {				
					public void execute(Connection conn) throws SQLException {
						try{
							String sql = "update tblSysDeploy set Setting=? where SysCode=?";
							PreparedStatement statement = conn.prepareStatement(sql);
							statement.setString(1, value);
							statement.setString(2, sysCode);
							statement.executeUpdate();
							sql ="update tblSysSetting set Setting=? where SysCode=?";		
							statement = conn.prepareStatement(sql);
							statement.setString(1, value);
							statement.setString(2, sysCode);
							statement.executeUpdate();						
						}catch(Exception ex){
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		if(retCode== ErrorCanst.DEFAULT_SUCCESS){
			BaseEnv.systemSet.get(sysCode).setSetting(value);
		}
		return result;
	}
	
	/**
	 * 添加
	 * @param bean
	 * @return
	 */
	public Result addLabel(LabelBean bean){
		return addBean(bean);
	}
	
	public Result delLabel(String id){
		return deleteBean(id, LabelBean.class,"id");
	}
	/**
	 * 查询颜色和花号，缸号是否启用
	 * @return
	 */
	public Result getGoodsAttribute() {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select t.propName,l.zh_CN from tblGoodsAttribute t left join tblLanguage l on t.languageId=l.id " +
									"where t.propName not in ('Seq') and t.isUsed=1 order by zh_CN asc";
							PreparedStatement pss = conn.prepareStatement(sql) ;
							ResultSet prs = pss.executeQuery();
							ArrayList list = new ArrayList();
							while(prs.next()){
								String[] p = new String[2];
								p[0] = prs.getString("propName");
								p[1] = prs.getString("zh_CN");
								list.add(p);
							}
							result.retVal = list;
							result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("CrmDeskTopMgt getAttention:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	/**
	 * 加载
	 * @param id
	 * @return
	 */
	public Result loadlabel(String id){
		return loadBean(id,LabelBean.class);
	}
	
	
	/**
     * 
     * @return
     */
    public Result sysnSeq() {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;  
                        try {
                            CallableStatement cs = conn.prepareCall("{call getSeq(?)}") ;
                            cs.registerOutParameter(1, Types.VARCHAR);
                            cs.execute();
                            rs.setRetVal(cs.getString(1));
                        } catch (SQLException ex) {   
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace() ;
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);
        return rs;
    }
    
    /**
	 * tblStockDet表中校验不能重复
	 * @return
	 */
	public Result getSeqEcho(final String seq) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select seq from tblStockDet where seq=?";
							
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, seq);
							ResultSet prs = pss.executeQuery();
							String sysused="";
							while(prs.next()){
								sysused = prs.getString("seq");
							}
							result.retVal = sysused;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("CrmDeskTopMgt getAttention:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	/**
	 * 
	 * @return
	 */
	public Result getSeq(final String seq,final String id) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select seq from tbllabel where seq=?";
							if(id != null && !"".equals(id)){
								sql += " and id != '"+id+"'";
							}
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, seq);
							ResultSet prs = pss.executeQuery();
							String sysused="";
							while(prs.next()){
								sysused = prs.getString("seq");
							}
							result.retVal = sysused;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("CrmDeskTopMgt getAttention:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	public Result getReport(final String name) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select id from tblReports where SQLfileName=?";
							
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, name+"SQL.xml");
							ResultSet prs = pss.executeQuery();
							String sysused="";
							while(prs.next()){
								sysused = prs.getString("id");
							}
							result.retVal = sysused;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("CrmDeskTopMgt getAttention:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	/**
	 * 查询数量控制的位数
	 * @param name
	 * @return
	 */
	public Result getNum() {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select setting from tblSysParameter where sysCode='DigitsQty'";
							
							PreparedStatement pss = conn.prepareStatement(sql) ;
							ResultSet prs = pss.executeQuery();
							String sysused="";
							while(prs.next()){
								sysused = prs.getString("setting");
							}
							result.retVal = sysused;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("CrmDeskTopMgt getAttention:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}

	/**
	 * 查询数据
	 * @return
	 */
	public Result getQuery(final String keyword,final Integer pageNo, final Integer pageSize) {
		
		StringBuffer sql = new StringBuffer("select t.id,t.goodsCode,t.unit,t.seq,t.batchNo,");
		sql.append("design=(select PropItemName from tblGoodsOfPropDet where PropItemID=t.design and PropID='Design')");
		sql.append(",t.color,t.colorName,t.colorBit,t.coil,t.meter");
		sql.append(",t.qty,t.createBy,t.createTime,t.gram,t.breadth,t.density,user1=(select PropItemName from tblGoodsOfPropDet where PropItemID=t.user1 and PropID='User1'),");
		sql.append("user2=(select PropItemName from tblGoodsOfPropDet where PropItemID=t.user2 and PropID='User2'),g.goodsFullName,g.goodsNumber,t.trackNo,t.procedures,ROW_NUMBER() over(order by t.seq desc) as row_id");
		sql.append(" from tblLabel t left join tblGoods g on t.goodsCode=g.classCode where 1=1");
		if(keyword !=null && !"".equals(keyword)){
			sql.append(" and (g.goodsFullName like '%"+keyword+"%' or g.goodsNumber like '%"+keyword+"' or t.goodsCode like '%"+keyword+"%' or t.seq like '%"+keyword+"%' or t.qty like '%"+keyword+"%')");
		}
		return sqlListMaps(sql.toString(), null, pageNo, pageSize);
	}
	
	/**
	 * 查询现有标签
	 * @param name
	 * @return
	 */
	public Result findAll(){
		List list = new ArrayList();
		String sql = "from LabelBean";
		return this.list(sql, list);
	}
	
	
	/**
	 * 根据id查询数据
	 * @return
	 */
	public Result getQueryId(final String id,final Integer type) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("select t.id,t.goodsCode,t.unit,t.seq,t.batchNo,t.design,t.color,t.colorBit,t.coil,t.meter");
							sql.append(",t.createBy,t.createTime,t.qty,t.colorName,t.gram,t.breadth,t.density,t.user1,t.user2,g.goodsFullName,t.trackNo,t.procedures,g.goodsNumber");
							sql.append(" from tblLabel t left join tblGoods g on t.goodsCode=g.classCode");
							if(type != null && !"".equals(type)){
								sql.append(" where t.id='"+id+"'");
							}
							sql.append(" order by seq desc");
							PreparedStatement pss = conn.prepareStatement(sql.toString()) ;
							ResultSet rs = pss.executeQuery();
							ArrayList<String[]> list = new ArrayList<String[]>();
							while(rs.next()){
	                    		String[] label = new String[26];
	                    		label[0] = rs.getString("id");
	                    		label[1] = rs.getString("goodsCode");
	                    		label[2] = rs.getString("unit");
	                    		label[3] = rs.getString("seq");
	                    		label[4] = rs.getString("batchNo");
	                    		label[5] = rs.getString("design");
	                    		label[6] = rs.getString("color");
	                    		label[7] = rs.getString("colorName");
	                    		label[8] = rs.getString("colorBit");
	                    		String coil ="";
	                    		if(!"0E-8".equals(rs.getString("coil"))){
	                    			coil = rs.getString("coil");
	                    		}
	                    		String meter ="";
	                    		if(!"0E-8".equals(rs.getString("meter"))){
	                    			meter = rs.getString("meter");
	                    		}
	                    		label[9] = coil;
	                    		label[10] = meter;
	                    		label[11] = Double.toString(rs.getDouble("qty"));
	                    		label[12] = rs.getString("createBy");
	                    		label[13] = rs.getString("createTime");
	                    		label[14] = rs.getString("gram");
	                    		label[15] = rs.getString("breadth");
	                    		label[16] = rs.getString("density");
	                    		label[17] = rs.getString("user1");
	                    		label[18] = rs.getString("user2");
	                    		label[19] = rs.getString("goodsFullName");
	                    		label[20] = rs.getString("goodsNumber");
	                    		label[21] = rs.getString("trackNo");
	                    		label[22] = rs.getString("procedures");
	                    		label[23] = rs.getString("design");
	                    		label[24] = rs.getString("user1");
	                    		label[25] = rs.getString("user2");
	                    		list.add(label);
							}
							result.retVal = list;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log
									.error("CrmDeskTopMgt getAttention:", ex);
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	/**
	 * 查询属性值，名
	 * @param id
	 * @param type
	 * @return
	 */
	public Result getQueryProp(final String PropItemID,final String names) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select PropItemName from tblGoodsOfPropDet where PropItemID='"+PropItemID+"' and PropID='"+names+"'";
							
							PreparedStatement pss = conn.prepareStatement(sql) ;
							ResultSet prs = pss.executeQuery();
							String PropItemName="";
							while(prs.next()){
								PropItemName = prs.getString("PropItemName");
							}
							result.retVal = PropItemName;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("LabelMgt getAttention:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}

}
