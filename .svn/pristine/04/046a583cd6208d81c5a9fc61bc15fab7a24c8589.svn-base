package com.menyi.aio.web.mrp;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.GoodsAttributeBean;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;

/**
 * 
 * @author 刘志高
 *
 */
public class MrpBPMgt extends DBManager {

	/**
	 * 执行批处理语句
	 * @param sql
	 * @return
	 */
	public boolean execBath(final String[] sql){	

        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
             public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try
                        {
                          Statement st=conn.createStatement();	 
                          for(int i=0;i<sql.length;i++){
                        	  BaseEnv.log.debug(sql[i]);
                        	  st.addBatch(sql[i]);
                          }
                           
                           // 执行数据更新
                          st.executeBatch();
                           // retVal的值为空，说明更新正常
                           rs.setRetVal(null);
                           
                        }catch(Exception ex)
                        {
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            rs.setRetVal(ex.getMessage());
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
    
		return rs.getRetVal()==null?true:false;
	}
	/**
	 * 执行单条sql语句,非select
	 * @param sql
	 * @return
	 */
	public Result execSql(final String sql) {

		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement();
							// 执行数据更新
							int rowNum = st.executeUpdate(sql);
							// retVal的值为空，说明更新正常
							rs.setRetVal(true);

						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.MRP_ERROR_CODE);
							rs.setRetVal(false);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		return rs;
	}
	/**
	 * 调用存储过程
	 * @param call
	 * @return
	 */
	public boolean execOrderQuantumProc(final String salesOrderID,final String goodsCode,final double outQty){	
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
             public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try
                        {
                           CallableStatement cst = conn.prepareCall("{call proc_updateSalesOrderQuantum(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
	       					cst.setString(1, salesOrderID);
	       					cst.setString(2, goodsCode);
	       					cst.setString(3,"add");
	       					cst.setString(4,"tblSalesOutStock");
	       					cst.setDouble(5, 0d);
	       					cst.setDouble(6, 0d);
	       					cst.setDouble(7, 0d);
	       					cst.setDouble(8, 0d);
	       					cst.setDouble(9, outQty);
	       					cst.setString(10,"");
	       					cst.setString(11,"");
	       					cst.setString(12,"");
	       					cst.registerOutParameter(13, Types.INTEGER);
	       					cst.registerOutParameter(14, Types.VARCHAR, 500);
	       					cst.execute();	
                        }catch(Exception ex){
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            rs.setRetVal(ex.getMessage());
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
    
		return rs.getRetVal()==null?true:false;
	}
	/**
	 * 
	 * @param productID
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	
	public Result querySql(String sql,int pageNo,int pageSize){
		
		int start = (pageNo-1)*pageSize+1;
		int end = pageNo*pageSize;
		sql = "select * from("+sql+") as list where rowrow between "+start+" and "+end;
		return querySql(sql);
	}
	/**
	 * 查询
	 * @param sql要执行的查询字符串
	 * @return 返回查询的全部结果
	 */
	
	public Result querySql(final String sql)
	    {
	        final Result rs=new Result();
	        int retCode = DBUtil.execute(new IfDB() {
	             public int exec(Session session) {
	                session.doWork(new Work() {
	                    public void execute(Connection connection) throws
	                            SQLException {
	                        Connection conn = connection;
	                        try
	                        {
	                           Statement st=conn.createStatement();	 
	                           
	                           ResultSet rss=st.executeQuery(sql);
	                           ArrayList<String[]> formatList=new ArrayList<String[]>();
	                           while(rss.next())
	                           {
	                        	   int col = rss.getMetaData().getColumnCount();
	                        	   
	                               String []format=new String[col] ;
	                               String typeName = null;// 记录查询字段类型
	                               for(int i=0;i<col;i++){
	                            	   typeName = rss.getMetaData().getColumnTypeName(i+1);//
	                            	  
	                            	   format[i] = rss.getString(i+1);	    
	                            	   if("numeric".equals(typeName)){// 如果字段是numeric类型的则格式化它，保留2位小数
	                            		   if(format[i]!=null)
	                            			   format[i]= GlobalsTool.formatNumberS((new Double(format[i])), false, false, "Qty", "");
	                            		   else
	                            			   format[i]="0";
	                            	   }
	                               }
	                               formatList.add(format);	                               
	                           }
	                           rs.setRetVal(formatList);
	                        }catch(Exception ex)
	                        {
	                            ex.printStackTrace();
	                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
	                            rs.setRetVal(ex.getMessage());
	                            return;
	                        }
	                    }
	                });
	                return rs.getRetCode();
	            }
	        });
	       return rs;
	    }
	
	/**
	 * 查询是否生产计划单
	 * @param id
	 * @return
	 */
	public Result queryProductPalnSql(final String id)
    {
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
             public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try
                        {
                           Statement st=conn.createStatement();	 
                           String sql="select goodscode,qty,RefBillNo,* from tblPlan where id='"+id+"'";
                           ResultSet rss=st.executeQuery(sql);
                           String[] strs=new String[3];
                           if(rss.next()){
                        	   strs[0]=rss.getString(1);
                        	   strs[1]=rss.getString(2);
                        	   strs[2]=rss.getString(3);
                        	   rs.setRetVal(strs);
                           }else{
                        	   rs.setRetVal(null);
                           }
                           
                        }catch(Exception ex)
                        {
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            rs.setRetVal(ex.getMessage());
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
       return rs;
    }
	/**
	 * 查询相关销售订单的订单数量
	 * @param id
	 * @return
	 */
	public Result queryRelSalesOrderQtySql(final String billNo,final String goodsCode)
    {
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
             public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try
                        {
                           Statement st=conn.createStatement();	 
                           String sql="select sum(qty) from tblSalesOrderdet where f_ref=(select id from tblSalesOrder where billno='"+billNo+"') and goodscode='"+goodsCode+"' ";
                           ResultSet rss=st.executeQuery(sql);
                           if(rss.next()){
                        	   rs.setRetVal(rss.getInt(1));
                           }else{
                        	   rs.setRetVal("0");
                           }
                           
                        }catch(Exception ex)
                        {
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            rs.setRetVal(ex.getMessage());
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
       return rs;
    }
	/**
	 * 分页查询表信息，如果pageSize=0,则查询所有
	 * 
	 * @param sql
	 * @param pageNo
	 * @param pageSize
	 * @return
	
	public Result queryTable(final String sql,final int pageNo,final int pageSize){
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
             public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try{
                        	ResultSet resultSet = null;
                        	ResultSetMetaData rsm = null;
                        	int rsLen = 0;
                        	String[] titleNames = null;
                        	String[] typeNames = null;
                        	String[] record = null;// 一条结果
                        	List records = new ArrayList();
                        	int start = (pageNo-1)*pageSize+1;
							int end = pageNo*pageSize;
							Object tmp = null;
							
							PreparedStatement ps = conn.prepareStatement(sql);							
							resultSet = ps.executeQuery();
							rsm = resultSet.getMetaData();
							rsLen = rsm.getColumnCount();
							
							titleNames = new String[rsLen];//表字段名
							typeNames = new String[rsLen];//字段类型
							
							for(int i=0; i<rsLen;i++){
								titleNames[i] = rsm.getColumnName(i+1);
								typeNames[i] = rsm.getColumnTypeName(i+1);
							}
							
							int j=1;//行从1开始
							for(;resultSet.next();j++){								
								if(j<start||j>end)continue;								
								record = new String[rsLen];
								for (int i = 0; i < rsLen; i++) {
									tmp = resultSet.getObject(i+1);
									if(tmp!=null){
										record[i] = tmp.toString();
									}
								}
								records.add(record);
							}
							rs.setRealTotal(j-1);
							rs.setRetVal(new Table(titleNames,typeNames,records));
							
                        }catch(Exception ex){
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            rs.setRetVal(ex.getMessage());
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
       return rs;
    }
    
     */
	
	/**
	 * 通用商品编号得到该商品的库存已分配量
	 * @param String goodscode
	 */
	public HashMap getStockUsed(String billType,String detIds){		
		//得到已经启用的商品属性信息
		String bomCond="";
		ArrayList list=BaseEnv.propList;
		for(int i=0;i<list.size();i++){
			GoodsPropInfoBean propBean=(GoodsPropInfoBean)list.get(i);
			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
				bomCond+=" and isnull(a."+propBean.getPropName()+",'')=isnull(b."+propBean.getPropName()+",'')";
			}
		}
		
		StringBuffer sql=new StringBuffer("");		
		sql.append("select b.id as detId,sum(stockQuantum)+sum(orderQuantum) from tblStocks a,"+billType+" b where b.id in ("+detIds+") and a.goodsCode=b.goodsCode "+bomCond);

		sql.append(" and a.stockCode in (select classCode from tblStock where IsMrpOper=1) group by b.id");

		
		try {
			HashMap StockMap=new HashMap();
			ArrayList li=(ArrayList) new AIODBManager().sqlList(sql.toString(), new ArrayList()).retVal;
			for(int i=0;li!=null&&i<li.size();i++){
				Object[] sf = ((Object[])li.get(i));
				String qty="0";
				if(sf!=null&&sf[1]!=null){
					qty=GlobalsTool.formatNumber(sf[1], false, false, true, "", "", true);
				}
				StockMap.put(sf[0],qty);
			}
			return StockMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap();
		
	}
	/**
	 * 查询商品的可用库存
	 * @param billType
	 * @param detIds
	 * @return
	 */
	public HashMap getUseStock(String StockCodes){
		StringBuffer sql=new StringBuffer("");		
		sql.append("select cast(Hashbytes('md5',Lower(GoodsCode+'GoodsCode'+color+'color')) as bigint) as goodPropHash,sum(TotalQty) from tblStocks a");
		sql.append(" where a.stockCode in (select classCode from tblStock where IsMrpOper=1) ");
		if(StockCodes.length()>0){
			sql.append(" and a.StockCode in ("+("'"+StockCodes.replaceAll(";", "','")+"'")+")");
		}
		sql.append("group by a.GoodsCode,a.color");
		
		try {
			HashMap StockMap=new HashMap();
			ArrayList li=(ArrayList) new AIODBManager().sqlList(sql.toString(), new ArrayList()).retVal;
			for(int i=0;li!=null&&i<li.size();i++){
				Object[] sf = ((Object[])li.get(i));
				String qty="0";
				if(sf!=null&&sf[1]!=null){
					qty=GlobalsTool.formatNumber(sf[1], false, false, true, "", "", true);
				}
				StockMap.put(sf[0].toString(), sf[1]);
			}
			return StockMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap();
		
	}
	/**
	 * 得到商品的价格：先取价格跟踪，再去预设进价
	 * @param String goodscode
	 */
	public BigDecimal getGoodsPrice(String companyCode,String GoodsCode){		
		
		StringBuffer sql=new StringBuffer("");		
		sql.append("select isnull((case isnull(b.BuyPrice,0) when 0 then a.PreBuyPrice else b.BuyPrice end),0) as price from tblGoods a ");
		sql.append("left join tblCustomerPriceDet b on a.classCode=b.GoodsCode and b.f_ref=(select id from tblCustomerPrice where companyCode='"+companyCode+"') where a.classCode='"+GoodsCode+"'");
		
		BigDecimal bd=new BigDecimal("0");
		try {
			ArrayList li=(ArrayList) new AIODBManager().sqlList(sql.toString(), new ArrayList()).retVal;
			if(li.size()>0){
				bd=(BigDecimal)((Object[])li.get(0))[0];
			}
			return bd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bd;
		
	}
	
	/**
	 * 通用单据编号得到该商品本张订单的在途量,在产量，库存已分配量,订单已分配量
	 * @param String billNo
	 */
	public Object[] getQuantumQtys(String trackNo,String goodscode,String mrpFrom,String bomDetId,String orderDetId){
		String sql="";
		if(mrpFrom.equals("2")){//销售订单
			sql="select buyIngNum,proingNum,stockQuantum,orderQuantum from tblSalesOrderQuantum where orderDetId='"+orderDetId+"' and salesOrderId='"+trackNo+"' and goodscode='" + goodscode + "' and bomdetId='"+bomDetId+"'";
		}else{//生产计划单
			sql="select buyIngNum,proingNum,stockQuantum,orderQuantum from tblSalesOrderQuantum where salesOrderId='"+trackNo+"' and goodscode='" + goodscode + "' and bomdetId='"+bomDetId+"'";
		}
		ArrayList li=(ArrayList) new AIODBManager().sqlList(sql.toString(), new ArrayList()).retVal;
		Object[] sf=new String[]{"0","0","0","0"};
		if(li.size()>0){
			sf =(Object[])li.get(0);		
		}		
        return sf;
	}
	
	public Result getOperBil(String where,String operStatus) {
		StringBuffer sql = null;// 要查询的sql语句
		
		//得到已经启用的商品属性信息
		String propStr="";
		ArrayList list=BaseEnv.attList;
		for(int i=0;i<list.size();i++){
			GoodsAttributeBean propBean=(GoodsAttributeBean)list.get(i);
			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&!propBean.getPropName().equals("ProDate")&&!propBean.getPropName().equals("Availably")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblSalesOrderDet", propBean.getPropName())!=null){//启用的商品属性
				if(propBean.getPropName().equals("Hue")||propBean.getPropName().equals("yearNO")||propBean.getPropName().equals("User1")||propBean.getPropName().equals("User2")
						||propBean.getPropName().equals("Design")||propBean.getPropName().equals("color")){
					if(BaseEnv.version==8){//工贸版
						propStr+=",isnull((select propItemName from tblGoodsPropItem where propItemID=@TABLENAME."+propBean.getPropName()+" and propName='"+propBean.getPropName()+"'),'') as "+propBean.getPropName();
					}else{//布匹版
						if(propBean.getPropName().equals("color")&&BaseEnv.systemSet.get("Usepubliccolor").getSetting().equals("false")){//如果颜色值设置不是公共颜色
							propStr+=",(select propItemName from tblGoodsOfProp ,tblGoodsOfPropDet  where  tblGoodsOfProp.id=tblGoodsOfPropDet.f_ref and tblGoodsOfProp.goodsCode=@TABLENAME.goodsCode and tblGoodsOfPropDet.propItemID=@TABLENAME."+propBean.getPropName()+" and tblGoodsOfPropDet.PropID='"+propBean.getPropName()+"') as "+propBean.getPropName();
						}else{
							propStr+=",(select propItemName from tblGoodsOfPropDet where propItemID=@TABLENAME."+propBean.getPropName()+" and PropID='"+propBean.getPropName()+"') as "+propBean.getPropName();
						}
					}
				}else{
					propStr+=",@TABLENAME."+propBean.getPropName();
				}
			}
		}
		/*
		sql = new StringBuffer("select ");
		sql.append("'1' as mrpFrom,b.trackNo,a.id as detId,b.goodsCode,c.goodsnumber,c.goodsFullName,c.GoodsSpec");
		sql.append(",b.NotProduceInQty as produceQty,a.finishDate as sendDate,d.EmpFullName,e.DeptFullName");
		sql.append(",row_number() over(order by a.finishDate,a.createTime desc) as row_id");
		sql.append(",isnull((select top 1 BillNo from tblBOM k where k.statusId=0 and k.goodsCode=b.goodsCode and isnull(k.BatchNo,'')=isnull(b.BatchNo,'') and isnull(k.Inch,'')=isnull(b.Inch,'') and isnull(k.Hue,'')=isnull(b.Hue,'') and isnull(k.yearNo,'')=isnull(b.yearNo,'') order by versionNo desc),'') as bomNo");
		sql.append(propStr.replaceAll("@TABLENAME", "b"));
		sql.append(" from tblPlan a");
		sql.append(" left join tblPlanDet b on a.id=b.f_ref");
		sql.append(" left join tblGoods c on b.goodsCode=c.classCode");
		sql.append(" left join tblEmployee d on a.EmployeeID=d.id");
		sql.append(" left join tblDepartment e on a.DepartmentCode=e.classcode ");
		sql.append(where+"  and b.NotProduceInQty>0 and a.InStatusId=0 and a.attachMrpOper=1 and a.workFlowNodeName='finish'");
		
		if("0".equals(operStatus)||operStatus==null){//未运算
			sql.append(" and b.trackNo not in (select orderTrackNo from tblProductMRP)");
		}else if("1".equals(operStatus)){//已运算
			sql.append(" and b.trackNo in (select orderTrackNo from tblProductMRP)");
		}
		
		sql.append(" union all "); */
		
		sql = new StringBuffer("");
		sql.append("select '2' as mrpFrom,b.trackNo,a.id as detId,b.goodsCode,c.goodsnumber,c.goodsFullName,c.GoodsSpec");
		sql.append(",b.NotOutQty as produceQty,(case when len(isnull(b.sendDate,''))=0 then a.sendDate else b.sendDate end) as sendDate,d.EmpFullName,e.DeptFullName");
		sql.append(",row_number() over(order by case when len(isnull(b.sendDate,''))=0 then a.sendDate else b.sendDate end desc,a.createTime desc) as row_id");
		//sql.append(",isnull((select top 1 BillNo from tblBOM k where k.goodsCode=b.goodsCode and isnull(k.BatchNo,'')=isnull(b.BatchNo,'') and isnull(k.Inch,'')=isnull(b.Inch,'') and isnull(k.Hue,'')=isnull(b.Hue,'') and isnull(k.yearNo,'')=isnull(b.yearNo,'') order by versionNo desc),'') as bomNo");
		//bom与销售订单明细的属性需要建一个表存储需要匹配的属性。
		sql.append(",isnull((select top 1 BillNo from tblBOM k where k.goodsCode=b.goodsCode  order by versionNo desc),'') as bomNo");
		sql.append(propStr.replaceAll("@TABLENAME", "b"));
		sql.append(" from tblSalesOrder a"); 
		sql.append(" left join tblSalesOrderDet b on a.id=b.f_ref");
		sql.append(" left join tblGoods c on b.goodsCode=c.classCode");
		sql.append(" left join tblEmployee d on a.EmployeeID=d.id");
		sql.append(" left join tblDepartment e on a.DepartmentCode=e.classcode ");
		//sql.append(where+" and b.NotOutQty>0 and a.statusId=0 and a.attachMrpOper=1 and a.workFlowNodeName='finish'");
		//布匹去掉and a.attachMrpOper=1 
		sql.append(where+" and b.NotOutQty>0 and a.statusId=0  and a.workFlowNodeName='finish'");	
		
		if("0".equals(operStatus)||operStatus==null){//未运算
			sql.append(" and b.trackNo not in (select orderTrackNo from tblProductMRP)  order by b.trackNO desc");
		}else if("1".equals(operStatus)){//已运算
			sql.append(" and b.trackNo in (select orderTrackNo from tblProductMRP) order by b.trackNO desc ");
		}
        //ssss
		BaseEnv.log.debug("order query:"+sql.toString());
		Result rs = new AIODBManager().sqlListMap(sql.toString(), new ArrayList(), 0, 0);

		return rs;
	}
	public HashMap getAllBom(){
		//得到已经启用的商品属性信息
		String bomField="";
		ArrayList proplist=BaseEnv.propList;
		for(int i=0;i<proplist.size();i++){
			GoodsPropInfoBean propBean=(GoodsPropInfoBean)proplist.get(i);
			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){
				if(propBean.getPropName().equals("Hue")||propBean.getPropName().equals("yearNO")||propBean.getPropName().equals("Design")
						||propBean.getPropName().equals("color")||propBean.getPropName().equals("User1")||propBean.getPropName().equals("User2")){
					if(BaseEnv.version==8){//工贸版
						bomField+=",isnull((select propItemName from tblGoodsPropItem where propItemID=tblBom."+propBean.getPropName()+" and propName='"+propBean.getPropName()+"'),'') as "+propBean.getPropName();
					}else{//布匹版
						if(propBean.getPropName().equals("color")&&BaseEnv.systemSet.get("Usepubliccolor").getSetting().equals("false")){//如果颜色值设置不是公共颜色
							bomField+=",(select propItemName from tblGoodsOfProp a,tblGoodsOfPropDet b where  a.id=b.f_ref and a.goodsCode=tblBom.goodsCode and b.propItemID=tblBom."+propBean.getPropName()+" and b.PropID='"+propBean.getPropName()+"') as "+propBean.getPropName();
						}else{
							bomField+=",(select propItemName from tblGoodsOfPropDet where propItemID=tblBom."+propBean.getPropName()+" and PropID='"+propBean.getPropName()+"') as "+propBean.getPropName();
						}
					}
				}else{
					bomField+=","+propBean.getPropName();
				}
			}
		}
		String sql="select billNo,goodsCode"+bomField+" from tblBom where workFlowNodeName='finish'  and statusId=0 and (select count(0) from tblBomDet where f_ref=tblBOM.id)>0 order by VersionNO desc";
		ArrayList li=(ArrayList)new AIODBManager().sqlListMap(sql, new ArrayList(),0,0).retVal;
		
		HashMap map=new HashMap();
		for(int i=0;i<li.size();i++){
			HashMap tempMap=(HashMap)li.get(i);
			String keyId=tempMap.get("goodsCode").toString();
			/*  布匹订单匹配bom只匹配商品的classcode
			for(int j=0;j<proplist.size();j++){
				GoodsPropInfoBean propBean=(GoodsPropInfoBean)proplist.get(j);
				if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){
					keyId+=":"+tempMap.get(propBean.getPropName());
				}
			}*/
			ArrayList billNos=null;
			if(map.get(keyId)==null){
				billNos=new ArrayList();
				map.put(keyId,billNos);
			}else{
				billNos=(ArrayList)map.get(keyId);
			}
			billNos.add(tempMap.get("billNo"));
		}
        return map;
	}
	
	//20091119查询未出库数量
	public Result getNotOutQty(final String tblName,final String goodscode){
		
		String sql = null;
		if("tblSalesOutStockDet".equals(tblName)){//销售出库单未出库,未审核
			sql = "select sum(Qty) from tblSalesOutStockDet where goodscode='"+goodscode+"'"
				+" and f_ref in (select id from tblSalesOutStock" 
				+" where SalesOrderID<>0 or SalesOrderID is not null) and workFlowNodeName='notApprove'";
		}else if("tblOtherOutDet".equals(tblName)){//其他出库单未出库，未审核
			sql = "select sum(Qty) from tblOtherOutDet where goodscode='"+goodscode+"' and workFlowNodeName='notApprove'";
		}else if("tblBuyOutStockDet".equals(tblName)){//采购退货单未出库，未审核
			sql = "select sum(Qty) from tblBuyOutStockDet where goodscode='"+goodscode+"' and workFlowNodeName='notApprove'";
		}else{
			//写给程序员看的
			throw new RuntimeException("this method:MrpMgt.getNotOutQty(...) not have this operation of the table:"+tblName+"!");
		}
		if(sql!=null){
			return querySql(sql);//执行查询
		}else{
			return null;
		}
	}
	
	
	public Result insertOrderQuantum(final ArrayList trackNoGood,final String createBy,final String billType){		
		final Result rs=new Result();
		int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) throws
	                           SQLException {
	                	   try{
		                       CallableStatement cst;
		               		   for(int i=0;i<trackNoGood.size();i++){//所选的订单的成品对应的BOM单，在此先将所运算的订单的成品的物料保存到“销售订单分配量”
			               			String []orderBom=(String[])trackNoGood.get(i);
		               				String trackNo=orderBom[0];
		               				String goodsCode=orderBom[4];
		               				cst = conn.prepareCall("{call proc_insertOrderQuantum(?,?,?,?,?,?)}");
		               				cst.setString(1, createBy);
		               				cst.setString(2, trackNo);
		               				cst.setString(3, billType);
		               				cst.setString(4, orderBom[2]);
		               				cst.setString(5, orderBom[3]);
		               				cst.registerOutParameter(6, Types.INTEGER);
		               				cst.execute();
		               		  }		               		   
	                	   }catch(Exception ex){
	                		   ex.printStackTrace();
	                	   }
	                   }
	               });
	               return rs.getRetCode();
	           }
	       });
	       rs.setRetCode(retCode);
		return rs;
	}
	public Result getSonMap(){		
		final Result rs=new Result();
		int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) throws
	                           SQLException {
	                	   try{
	                		   String sql="select ch,l,bomdetId,goodsCode,unitNum,qty,price,MaterielType,BOMId,mainPath,path,startDate,submitDate,isnull(color,'') as color,trackNo,cast(Hashbytes('md5',Lower(GoodsCode+'GoodsCode'+isnull(color,'')+'color')) as bigint) as goodPropHash,replace from bomtmp as bd order by path";
	                		  // BaseEnv.log.debug("查询当前成品所有子级物料："+sql);
	                		   Statement st=conn.createStatement();
	                		   ResultSet rst=st.executeQuery(sql);
	                		   
	                		   ArrayList list=new ArrayList();
	                		   HashMap map=new HashMap();
	                		   while(rst.next()){
	                			   HashMap itemMap=new HashMap();
	                			   String path=rst.getString("path");
	                			   list.add(path);
	                			   map.put(path, itemMap);
	                			   itemMap.put("path", path);
	                			   itemMap.put("ch", rst.getString("ch"));
	                			   itemMap.put("l", rst.getString("l"));
	                			   itemMap.put("bomdetId", rst.getString("bomdetId"));
	                			   itemMap.put("goodsCode", rst.getString("goodsCode"));
	                			   itemMap.put("unitNum", rst.getDouble("unitNum"));
	                			   itemMap.put("qty", rst.getDouble("qty"));
	                			   itemMap.put("price", rst.getDouble("price"));
	                			   itemMap.put("MaterielType", rst.getString("MaterielType"));
	                			   itemMap.put("BOMId", rst.getString("BOMId"));
	                			   itemMap.put("mainPath", rst.getString("mainPath"));
	                			   itemMap.put("startDate", rst.getString("startDate"));
	                			   itemMap.put("submitDate", rst.getString("submitDate"));
	                			   itemMap.put("color",rst.getString("color"));
	                			   //itemMap.put("BatchNo", rst.getString("BatchNo"));
	                			   //itemMap.put("Inch", rst.getString("Inch"));
	                			   //itemMap.put("Hue", rst.getString("Hue"));
	                			   //itemMap.put("yearNo", rst.getString("yearNo"));
	                			   itemMap.put("trackNo", rst.getString("trackNo"));
	                			   itemMap.put("goodPropHash", rst.getString("goodPropHash"));
	                			   itemMap.put("replace", rst.getString("replace"));
	                		   }
	                		   
	                		   Object[] obj=new Object[]{list,map};
	                		   rs.setRetVal(obj);
	                	   }catch(Exception ex){
	                		   ex.printStackTrace();
	                		   rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
	                	   }
	                   }
	               });
	               return rs.getRetCode();
	           }
	       });
	       rs.setRetCode(retCode);
		return rs;
	}
	public Result getGoodsInfo(final String goodsCodes){		
		final Result rs=new Result();
		int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) throws
	                           SQLException {
	                	   try{
	                		   String sql="select a.classCode,goodsNumber,goodsFullName,goodsSpec,isnull(b.unitName,'') as unitName,MaterielAttribute from tblGoods a left join tblUnit b on a.BaseUnit=b.id where a.classCode in ("+goodsCodes+")";
	                		   Statement st=conn.createStatement();
	                		   ResultSet rst=st.executeQuery(sql);
	                		   
	                		   HashMap map=new HashMap();
	                		   while(rst.next()){
	                			   HashMap itemMap=new HashMap();
	                			   String goodsCode=rst.getString("classCode");
	                			   map.put(goodsCode, itemMap);
	                			   itemMap.put("classCode", goodsCode);
	                			   itemMap.put("goodsNumber", rst.getString("goodsNumber"));
	                			   itemMap.put("goodsFullName", rst.getString("goodsFullName"));
	                			   itemMap.put("goodsSpec", rst.getString("goodsSpec"));
	                			   itemMap.put("unitName", rst.getString("unitName"));
	                			   itemMap.put("MaterielAttribute", rst.getString("MaterielAttribute"));
	                		   }
	                		   
	                		   rs.setRetVal(map);
	                	   }catch(Exception ex){
	                		   ex.printStackTrace();
	                		   rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
	                	   }
	                   }
	               });
	               return rs.getRetCode();
	           }
	       });
	       rs.setRetCode(retCode);
		return rs;
	}
	public Result updateOrderQuantum(final ArrayList trackNos,final String createBy,final HashMap<String,ArrayList> orderMap){		
		final Result rs=new Result();
		int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) throws
	                           SQLException {
	                	   try{		                       
		                       CallableStatement cst;
		               		   for(int i=0;i<trackNos.size();i++){//所选的订单的成品对应的BOM单，在此先将所运算的订单的成品的物料保存到“销售订单分配量”
			               			String trackNo=trackNos.get(i).toString();            				
		               				ArrayList bomDetList=orderMap.get(trackNo);
		               				for(int j=0;j<bomDetList.size();j++){
		               					HashMap detMap=(HashMap)bomDetList.get(j);
		               					cst = conn.prepareCall("{call proc_updateSalesOrderQuantum(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		               					cst.setString(1, detMap.get("TrackNo").toString());
		               					cst.setString(2, detMap.get("GoodsCode").toString());
		               					cst.setString(3,detMap.get("oper").toString());
		               					cst.setString(4,"");
		               					cst.setDouble(5, Double.parseDouble(detMap.get("stockQuantum").toString()));
		               					cst.setDouble(6, 0d);
		               					cst.setDouble(7, Double.parseDouble(detMap.get("buyingNum").toString()));
		               					cst.setDouble(8, Double.parseDouble(detMap.get("proingNum").toString()));
		               					cst.setDouble(9, 0d);
		               					cst.setString(10,detMap.get("levelCount").toString());
		               					cst.setString(11,detMap.get("orderDetId").toString());
		               					cst.setString(12,detMap.get("bomdetId").toString());
		               					cst.registerOutParameter(13, Types.INTEGER);
		               					cst.registerOutParameter(14, Types.VARCHAR, 500);
		               					cst.execute(); 
		               				}
		               		  }		               		   
	                	   }catch(Exception ex){
	                		   ex.printStackTrace();
	                	   }
	                   }
	               });
	               return rs.getRetCode();
	           }
	       });
	       rs.setRetCode(retCode);
		return rs;
	}
	

	public Result updateBuyOrderTrackNos(final ArrayList goods, final HashMap<String,String[]> goodMap){
        final Result rs=new Result();
		int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) throws
	                           SQLException {
	                	   try{		                       
		                       CallableStatement cst;
		               		   for(int i=0;i<goods.size();i++){//所选的订单的成品对应的BOM单，在此先将所运算的订单的成品的物料保存到“销售订单分配量”
			               			String goodCode=goods.get(i).toString();            				
		               				String[] ts=goodMap.get(goodCode);
	               					cst = conn.prepareCall("{call proc_updateBuyOrderTrackNo(?,?,?)}");
	               					cst.setString(1, ts[0]);
	               					cst.setString(2, goodCode);
	               					cst.setDouble(3, Double.parseDouble(ts[1]));
	               					cst.execute(); 
		               		  }		               		   
	                	   }catch(Exception ex){
	                		   ex.printStackTrace();
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
	 * 查询商品的最低采购量
	 * @param classCode
	 * @return
	 */
	public Result queryGoodLeastQty(final String classCode){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="select isnull(leastQty,0) as leastQty1,CompanyCode,ComFullName,goodsNumber,goodsFullName,goodsSpec,unitName,isnull(leastOrder,0) as leastOrder ,isnull(g.StockCode,'') as StockCode,isnull(k.StockFullName,'') as StockFullName,g.MaterielAttribute from tblGoods g left join tblCompany "+
                        		"on g.CompanyCode=tblCompany.classCode left join tblUnit on g.BaseUnit=tblUnit.id left join tblStock k on g.StockCode=k.classCode" +
                        			" where g.classCode='" + classCode +"'";
                        	Statement st = conn.createStatement();
                        	ResultSet rst = st.executeQuery(sql);
                        	String[] values=new String[11];
                        	if (rst.next()) {
								values[0] = rst.getString("leastQty1");
								values[1] = rst.getString("CompanyCode");
								values[2] = rst.getString("ComFullName");
								values[3] = rst.getString("goodsNumber");
								values[4] = rst.getString("goodsFullName");
								values[5] = rst.getString("goodsSpec");
								values[6] = rst.getString("unitName");
								values[7] = rst.getString("leastOrder");
								values[8] = rst.getString("StockCode");
								values[9] = rst.getString("StockFullName");
								values[10] = "WG".toString() ;  //rst.getString("MaterielAttribute");
								
							}
                        	rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
                        	rs.setRetVal(values) ;
                        } catch (Exception ex) {
                        	ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
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
	 * 判断是否启用按订单生产
	 * @param classCode
	 * @return
	 */
	public Result isUseProduce(){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="select Setting from tblSysSetting where sysCode='ProduceBySalesorder'";
                        	Statement st = conn.createStatement();
                        	ResultSet rst = st.executeQuery(sql);
                        	String value="";
                        	if (rst.next()) {
								value = rst.getString("Setting");
							}
                        	rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
                        	rs.setRetVal(value) ;
                        } catch (Exception ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
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
	public String getGoodsHash(boolean isFirst,String bomdetId,boolean isPlace){
		String propStr="";
		
		if(BaseEnv.version==8){//工贸版
			propStr="cast(Hashbytes('md5',goodsCode+'GoodsCode'+BatchNo+'BatchNo'+color+'color'+Hue+'Hue'+Inch+'Inch'+yearNO+'yearNO') as bigint)";
		}else{//布匹版
			propStr="cast(Hashbytes('md5',GoodsCode+'GoodsCode'+isnul(color,'')+'color') as bigint)";
		}
		String sql="";
		if(isFirst){
			sql="select "+propStr+" as hash from tblBom where id='"+bomdetId+"'";
		}else{
			if(isPlace){
				sql="select "+propStr+" as hash from tblBomDetail where id='"+bomdetId+"'";
			}else{
				sql="select "+propStr+" as hash from tblBomDet where id='"+bomdetId+"'";
			}
		}
		Result rs = new AIODBManager().sqlListMap(sql, new ArrayList(), 0, 0);
		String goodsHash=((HashMap)((ArrayList)rs.retVal).get(0)).get("hash") .toString();
		return goodsHash;
	}
	
	/**
	 * 更新分仓库存的库存已分配量
	 * @param strs
	 * @return
	 */
	public void updateStockQuantum(double num,String bomdetId,boolean isFirst,String stockCode,String trackNo,String isPlace){
		String sql="";
		//查询这个销售订单之前是否已经更新了库存已分配量
		sql="select netReqQty from tblSalesOrderQuantum where salesOrderID='"+trackNo+"' and bomdetId='"+bomdetId+"'";
		Result  rs=new AIODBManager().sqlList(sql, new ArrayList());
		ArrayList li=(ArrayList)rs.retVal;
		if(li.size()>0){
			Object obj=((Object[])li.get(0))[0];
			double d=Double.parseDouble(obj.toString());
			if(d==0){
				return;
			}else{
				if(d<num){
					num=d;
				}
			}
		}else{
			return ;
		}
		
		
		String propStr="";
		ArrayList list=BaseEnv.propList;
		for(int i=0;i<list.size();i++){
			GoodsPropInfoBean propBean=(GoodsPropInfoBean)list.get(i);
			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
				propStr+=","+propBean.getPropName();
			}
		}	
		
		if(isFirst){
			sql="select goodsCode"+propStr+" from tblBom where id='"+bomdetId+"'";
		}else{
			if("true".equals(isPlace)){
				sql="select goodsCode"+propStr+" from tblBomDetail where id='"+bomdetId+"'";
			}else{
				sql="select goodsCode"+propStr+" from tblBomDet where id='"+bomdetId+"'";
			}
		}		
		rs = new AIODBManager().sqlListMap(sql, new ArrayList(), 0, 0);
		
		//设置更改分仓库存表的条件
		String sqlCond="";
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList listMap=(ArrayList)rs.retVal;
			if(listMap.size()>0){
				HashMap map=(HashMap)listMap.get(0);		
				sqlCond=" goodsCode='"+map.get("goodsCode").toString()+"'";
				for(int i=0;i<list.size();i++){
					GoodsPropInfoBean propBean=(GoodsPropInfoBean)list.get(i);
					if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
						sqlCond+=" and "+propBean.getPropName()+"='"+map.get(propBean.getPropName())+"'";
					}
				}		
			}
		}
		if(stockCode.length()>0){
			sqlCond+=" and stockCode in ('"+(stockCode.substring(0,stockCode.length()-1).replaceAll(";", "','"))+"')";
		}
		if(sqlCond.length()>0)sqlCond=" where "+sqlCond;
		String sqlquery="select id,lastQty-orderQuantum-stockQuantum from tblStocks "+sqlCond+" order by lastQty desc";
		ArrayList sqlList=new ArrayList();
		BaseEnv.log.debug(sqlquery);
		Result rst=this.querySql(sqlquery);
		if(rst.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList listStocks=(ArrayList)rst.retVal;
			String id="";
			double qty=0;
			double useqty=0;
			for(int i=0;i<listStocks.size();i++){
				String[] str=(String[])listStocks.get(i);
				id=str[0];
				qty=Double.parseDouble(str[1].replace(",", ""));
				if(qty-num>=0){
					useqty=num;
					num=0;
				}else{ 
					useqty=qty;
					num=num-qty;
				}
				
				if(useqty>0){
					sqlList.add("update tblStocks set stockQuantum=stockQuantum+"+useqty+" where id='"+id+"'");
				}
				
				if(num==0)break;
			}
		}
		String []sqlBath=new String[sqlList.size()];
		for(int i=0;i<sqlList.size();i++){
			sqlBath[i]=sqlList.get(i).toString();
		}
		this.execBath(sqlBath);
        return ;
	}
	
	/**
	 * 减去分仓库存中的订单已分配量，库存已分配量。
	 * @param trackNo
	 */
	public void updateStock(String trackNo){
		AIODBManager aioMgt=new AIODBManager();
		String sql="select goodPropHash,sum(stockQuantum),sum(orderQuantum) from tblSalesOrderQuantum where salesOrderID='"+trackNo+"' and (stockQuantum>0 or orderQuantum>0) group by goodPropHash";
		Result rs=aioMgt.sqlList(sql, new ArrayList());
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS&&rs.retVal!=null){
			ArrayList quanList=new ArrayList();
			
			ArrayList list=(ArrayList)rs.retVal;
			for(int i=0;i<list.size();i++){//查询所有商品的库存已分配量，订单已分配量
				Object[] obj=(Object[])list.get(i);
				String goodPropHash=obj[0].toString();
				double stockQuantum=Double.parseDouble(obj[1].toString());
				double orderQuantum=Double.parseDouble(obj[2].toString());
				ArrayList quanSignList=this.updateSignStock(goodPropHash, stockQuantum, orderQuantum);
				quanList.addAll(quanSignList);				
			}
			//分仓库存表减去库存已分配量，订单已分配量
			String []quans=new String[quanList.size()];
			for(int i=0;i<quanList.size();i++){
				quans[i]=quanList.get(i).toString();
			}
			this.execBath(quans);
		}
	}
	
	public double getQuantum(String salesOrderID,String goodsCode,String bomdetId){
		String sql="select isnull(stockQuantum,0)+isnull(orderQuantum,0)+isnull(buyIngNum,0)+isnull(proingNum,0) from tblSalesOrderQuantum where  salesOrderID=? and goodsCode=? and bomdetId=?";
		ArrayList list=new ArrayList();
		list.add(salesOrderID);
		list.add(goodsCode);
		list.add(bomdetId);
		AIODBManager aioMgt=new AIODBManager();
		Result rs=aioMgt.sqlList(sql, list);
		Double d=0d;
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS&&rs.retVal!=null){
			ArrayList qtyList=(ArrayList)rs.retVal;
			for(int j=0;j<qtyList.size();j++){
				d=Double.parseDouble(((Object[])qtyList.get(j))[j].toString());
			}
		}
		return d;
	}
	
	public ArrayList updateSignStock(String goodPropHash,double stockQuantum,double orderQuantum){
		AIODBManager aioMgt=new AIODBManager();
		String sql="";
		//减去分仓库存表中当前商品的库存已分配量，订单已分配量
		if(BaseEnv.version==8){//工贸版
			sql="select id,isnull(stockQuantum,0),isnull(orderQuantum,0) from tblStocks where cast(Hashbytes('md5',isnull(goodsCode,'')+'GoodsCode'+isnull(BatchNo,'')+'BatchNo'+isnull(color,'')+'color'+isnull(Hue,'')+'Hue'+isnull(Inch,'')+'Inch'+isnull(yearNO,'')+'yearNO') as bigint)"+
				"="+goodPropHash+" order by isnull(stockQuantum,0)+isnull(orderQuantum,0)";
		}else{//布匹版
			sql="select id,isnull(stockQuantum,0),isnull(orderQuantum,0) from tblStocks where cast(Hashbytes('md5',GoodsCode+'GoodsCode'+isnull(color,'')+'color') as bigint)"+
			"="+goodPropHash+" order by isnull(stockQuantum,0)+isnull(orderQuantum,0)";
		}
		Result rs=aioMgt.sqlList(sql, new ArrayList());
		ArrayList quanList=new ArrayList();
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS&&rs.retVal!=null){
			ArrayList qtyList=(ArrayList)rs.retVal;
			for(int j=0;j<qtyList.size();j++){
				Object[] qty=(Object[])qtyList.get(j);
				double stockQuantum2=Double.parseDouble(qty[1].toString());
				double orderQuantum2=Double.parseDouble(qty[2].toString());
				
				if(stockQuantum<stockQuantum2){
					stockQuantum2=stockQuantum;
					stockQuantum=0;
				}else{
					stockQuantum=stockQuantum-stockQuantum2;
				}
				
				if(orderQuantum<orderQuantum2){
					orderQuantum2=orderQuantum;
					orderQuantum=0;
				}else{
					orderQuantum=orderQuantum-orderQuantum2;
				}
				
				sql="update tblStocks set stockQuantum=stockQuantum-"+stockQuantum2+",orderQuantum=orderQuantum-"+orderQuantum2+" where id='"+qty[0]+"'";
				quanList.add(sql);
				if(stockQuantum==0&&orderQuantum==0){
					continue;
				}
			}
		}
		return quanList;
	}
	
	public void updateBill(String trackNo){
		String[]tables=new String[]{"tblBuyOrder","tblBuyInStock","tblBuyOutStock","tblSalesOutStock","tblSalesReturnStock",
				"tblAllot","tblAllotChange","tblOtherOut","tblOtherIn","tblOutMaterials","tblInProducts",
				"tblRtnMaterials","tblTransferMaterial","tblProductsLose","tblEntrustOutGoods"};
		ArrayList list=new ArrayList();
		for(int i=0;i<tables.length;i++){
			if(DDLOperation.getFieldInfo(BaseEnv.tableInfos, tables[i], "TrackNo")!=null){
				list.add(tables[i]);
			}
		}
		String[] sqlList=new String[list.size()];
		for(int i=0;i<list.size();i++){
			String sql="update "+list.get(i)+" set trackNo=replace(trackNo,'"+trackNo+"','') where charIndex('"+trackNo+"',trackNo)>0";
			sqlList[i]=sql;
			BaseEnv.log.debug(sql);
		}
		this.execBath(sqlList);
	}
	
	public void delQuantum(String trackNo){
		String sql="delete from tblSalesOrderQuantum where salesOrderID='"+trackNo+"'";
		this.execSql(sql);
	}
	
	/**
	 * 更新分仓库存的库存已分配量,减去已经出库的部分
	 * @param strs
	 * @return
	 */
	public void upStockQuantum(double num,boolean isFirst,String bomdetId){
		String sql="";
		double num2=num;
		String propStr="";
		ArrayList list=BaseEnv.propList;
		for(int i=0;i<list.size();i++){
			GoodsPropInfoBean propBean=(GoodsPropInfoBean)list.get(i);
			if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
				propStr+=","+propBean.getPropName();
			}
		}	
		
		if(isFirst){
			sql="select goodsCode"+propStr+" from tblBom where id='"+bomdetId+"'";
		}else{
			sql="select goodsCode"+propStr+" from tblBomDet where id='"+bomdetId+"'";
		}		
		Result rs = new AIODBManager().sqlListMap(sql, new ArrayList(), 0, 0);
		
		//设置更改分仓库存表的条件
		String sqlCond="";
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList listMap=(ArrayList)rs.retVal;
			if(listMap.size()>0){
				HashMap map=(HashMap)listMap.get(0);		
				sqlCond=" goodsCode='"+map.get("goodsCode").toString()+"'";
				for(int i=0;i<list.size();i++){
					GoodsPropInfoBean propBean=(GoodsPropInfoBean)list.get(i);
					if(propBean.getIsUsed()==1&&!propBean.getPropName().equals("Seq")&&DDLOperation.getFieldInfo(BaseEnv.tableInfos, "tblBOM", propBean.getPropName())!=null){//启用的商品属性
						sqlCond+=" and "+propBean.getPropName()+"='"+map.get(propBean.getPropName())+"'";
					}
				}		
			}
		}
		
		/*************************更新分仓库存表********************************/
		String sqlquery="select id,orderQuantum,stockQuantum from tblStocks where "+sqlCond+" and (orderQuantum+stockQuantum)>0 order by orderQuantum+stockQuantum";
		ArrayList sqlList=new ArrayList();
		Result rst=this.querySql(sqlquery);
		if(rst.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList listStocks=(ArrayList)rst.retVal;
			String id="";
			double orderQuantum=0;
			double stockQuantum=0;
			for(int i=0;i<listStocks.size();i++){
				String[] str=(String[])listStocks.get(i);
				id=str[0];
				orderQuantum=Double.parseDouble(str[1].replace(",", ""));
				stockQuantum=Double.parseDouble(str[2].replace(",", ""));
				if(num-(orderQuantum+stockQuantum)<=0){
					if(num-stockQuantum<=0){//库存已分配量足够
						stockQuantum=num;
						orderQuantum=0;
					}else{//库存已分配量不够，需要减去订单已分配量
						orderQuantum=num-stockQuantum;
					}
					num=0;
				}else{
					num=num-(orderQuantum+stockQuantum);
				}
				sqlList.add("update tblStocks set stockQuantum=stockQuantum-"+stockQuantum+",orderQuantum=orderQuantum-"+orderQuantum+" where id='"+id+"'");				
				if(num==0)break;
			}
		}
		String []sqlBath=new String[sqlList.size()];
		for(int i=0;i<sqlList.size();i++){
			sqlBath[i]=sqlList.get(i).toString();
		}
		this.execBath(sqlBath);
		
		/**************************更新订单已分配量表中订单已分配量*********************************************/
		sqlquery="select salesOrderID,goodsCode,orderQuantum+stockQuantum from tblSalesOrderQuantum where "+sqlCond+" and (stockQuantum+orderQuantum)>0 order by createTime desc";
		rst=this.querySql(sqlquery);
		if(rst.retCode==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList listStocks=(ArrayList)rst.retVal;
			double stockQuantum=0;
			for(int i=0;i<listStocks.size();i++){
				String[] str=(String[])listStocks.get(i);
				stockQuantum=Double.parseDouble(str[2].replace(",", ""));
				if(num2-stockQuantum<=0){
					stockQuantum=num2;
					num2=0;
				}
				
				this.execOrderQuantumProc(str[0], str[1], stockQuantum);	
				if(num2==0)break;
			}
		}
		
        return ;
	}
	
	/**
	 * 查找商品表所有兄弟表和生产任务单所有兄弟表映射关系
	 * @return 返回hashMap 键为表名，值为list保存表所有对应字段
	 */
	public Result queryGoodsReProduce(){	
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
             public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                            SQLException {
                        try
                        {
                        	Statement st=conn.createStatement();
                        	ResultSet rst=null;
                        	ArrayList<DBTableInfoBean> goodsList=DDLOperation.getBrotherTables("tblGoods", BaseEnv.tableInfos);
                        	String goodsTables="";
                        	for(int i=0;i<goodsList.size();i++){
                        		goodsTables+="'"+goodsList.get(i).getTableName()+"',";
                        	}
                        	HashMap relationMap=new HashMap();
                        	//如果商品有兄弟表，逐个查询生产任务单的兄弟表是否有在这些表中存在映射关系
                        	if(goodsTables.length()>0){
                        		String sql="";                        		
                        		String childTable="";
                        		String mostlyTable="";
                        		String childTableField="";
                        		String mostlyTableField="";
                        		
                        		goodsTables=goodsTables.substring(0,goodsTables.length()-1);
                        		ArrayList proList=DDLOperation.getBrotherTables("tblProduce", BaseEnv.tableInfos);
                            	for(int i=0;i<proList.size();i++){
                            		//用relationMap的键保存所有的生产兄弟表映射到的商品兄弟表，值为ArrayList
                            		DBTableInfoBean tableBean=(DBTableInfoBean)proList.get(i);
                            		sql="select childTable,mostlyTable,childTableField,mostlyTableField from tblTableMapped where childTable='"+tableBean.getTableName()+"' and mostlyTable in ("+goodsTables+")";
                            		rst=st.executeQuery(sql);
                            		while(rst.next()){
                            			childTable=rst.getString(1);
                            			mostlyTable=rst.getString(2);
                            			childTableField=rst.getString(3);
                            			mostlyTableField=rst.getString(4);
                            			
                            			ArrayList fieldList;
                            			if(!relationMap.containsKey(childTable+"~"+mostlyTable)){
                            				fieldList=new ArrayList();
                            				relationMap.put(childTable+"~"+mostlyTable, fieldList);                            				
                            			}else{
                            				fieldList=(ArrayList)relationMap.get(childTable+"~"+mostlyTable);
                            			}
                            			fieldList.add(new String[]{childTableField,mostlyTableField});
                            		}  
                            	}
                        	} 
                        	rs.setRetVal(relationMap);
                        }catch(Exception ex){
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            rs.setRetVal(ex.getMessage());
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
	 * 根据生产任务单与商品表的映射关系，插入数据到任务单表
	 * @param relationMap
	 * @param goodsCode
	 * @param produceID
	 * @return
	 */
	public Result execGoodsReProduce(final HashMap relationMap,final String goodsCode,final String produceID){	
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
             public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                            SQLException {
                        try
                        {
                        	Statement st=conn.createStatement();
                        	Iterator it=relationMap.keySet().iterator();
                        	String sqlInsert="";
                        	String sqlVal="";
                        	String sql="";
                        	while(it.hasNext()){
                        		String tableName=it.next().toString();
                        		ArrayList fieldList=(ArrayList)relationMap.get(tableName);
                        		String childTable=tableName.substring(0,tableName.indexOf("~"));
                        		String mostlyTable=tableName.substring(tableName.indexOf("~")+1);
                        		
                        		//组织SQL语句
                        		
                        		sqlInsert="insert into "+childTable+"(id,f_brother";
                        		sqlVal="select '"+IDGenerater.getId() +"','"+produceID+"'";
                        		for(int i=0;i<fieldList.size();i++){
                        			String []str=(String[])fieldList.get(i);
                        			sqlInsert+=","+str[0];
                        			sqlVal+=","+str[1];
                        		}
                        		sql=sqlInsert+") "+sqlVal+" from "+mostlyTable+" where f_brother=(select top 1 id from tblGoods where classCode='"+goodsCode+"')";
                        		
                        		st.addBatch(sql);
                        	}
                        	if(sql.length()>0){
                        		st.executeBatch();
                        	}
                        	
                        }catch(Exception ex){
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            rs.setRetVal(ex.getMessage());
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
	 * 查询MRP的值
	 * @param call
	 * @return
	 */
	public Result selectProductMRP(final String ProductMRPs){	
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
             public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try
                        {
                            String sql="select relationNo,bomNo,MRPFrom,ProductQty  from tblProductMRP where id in ('"+ProductMRPs.replaceAll(",", "','")+"')";
                            Statement st=conn.createStatement();
                            ResultSet rst=st.executeQuery(sql);
                            ArrayList list=new ArrayList();
                            while(rst.next()){
                            	list.add(new String[]{rst.getString(1),rst.getString(2),rst.getString(3),rst.getString(4)});
                            }     
                            rs.setRetVal(list);
                        }catch(Exception ex){
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            rs.setRetVal(ex.getMessage());
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });        
       
		return rs;
	}
}
