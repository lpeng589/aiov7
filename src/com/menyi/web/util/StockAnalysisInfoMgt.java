package com.menyi.web.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.StockAnalysisInfoBean;
import com.menyi.aio.bean.StockDet;
import com.menyi.aio.bean.UpperLowerLimitBean;
/**
 * 自动计算库存上下限
 * @author 徐磊
 *
 */
public class StockAnalysisInfoMgt {
	//在用户改变  库存分析基础信息 的数据  时更新下次执行时间
	public Result updateStockAnalysisInfo(){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
	            public int exec(final Session session) {
	                session.doWork(new Work() {
	                    public void execute(Connection connection) throws
	                            SQLException {
	                    	boolean error = false;//是否出现错误
	                    	try{
	                    		if(8!=BaseEnv.version){//GM才有这个功能
	                    			return;
	                    		}
		                        Connection conn = connection;
		                        Statement s = conn.createStatement();
		                        
		                        String sql3 = "select * from tblStockAnalysisInfo";
		                        ResultSet rt = s.executeQuery(sql3);
		                        StockAnalysisInfoBean bean = new StockAnalysisInfoBean();//得到改变后的数据
		                        if(rt.next()){
		                        	bean.setResort(rt.getInt("resort"));
		                        	bean.setFrequency(rt.getInt("frequency"));
		                        	bean.setLowerLimitProportion(rt.getDouble("lowerLimitProportion"));
		                        	bean.setUpperLimitProportion(rt.getDouble("upperLimitProportion"));
		                        	bean.setLastTime(rt.getString("lastTime"));
		                        }else{
//		                        	库存分析基础信息表中没有数据");
		                        	error = true;
		                        }
		                        if(rt.next()){
//		                        	"库存分析基础信息表中的数据大于一条");
		                        	error = true;
		                        }
//		                        yyyy-MM-dd HH:mm:ss格式
		                        SimpleDateFormat sdf = BaseDateFormat.sh;
		                        
		                        
		                        
		                        if(bean.getFrequency() == 0){//手工执行
		                        	bean.setNextTime("");    //没有下次执行时间
		                        }else{
		                        	if(bean.getLastTime()==null||"".equals(bean.getLastTime())){
		                        		return;//没有上次执行时间，直接返回
		                        	}
		                        	Date lastTime = sdf.parse(bean.getLastTime());//上次执行时间
		                        	long frequency = bean.getFrequency()*24*60*60*1000l; //到下次执行的间隔
		                        	Date nextTime = new Date(lastTime.getTime()+frequency);//下次执行时间
		                        	bean.setNextTime(sdf.format(nextTime));
		                        }
		                        //更新下次执行时间
		                        String updateBean = "update tblStockAnalysisInfo set nextTime='"+bean.getNextTime()+"'";
		                        int num = s.executeUpdate(updateBean);
		                        if(num!=1){
		                        	//更新出错，只能有一条记录
		                        	error = true;
                        		}
		                    }catch(Exception e){
		                    	
		                    	rs.setRetCode(ErrorCanst.NUMBER_COMPARE_ERROR);
	                        	return;
		                    }
		                    if(error){
		                    	rs.setRetCode(ErrorCanst.NUMBER_COMPARE_ERROR);
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
	 * 在服务启动 和 修改 库存分析基础信息 后调用
	 *
	 */
	public void serivce(){
		updateStockAnalysisInfo();
		Result rs = getStockAnalysisInfo();
		Object o = rs.getRetVal();
		if(o == null){
			return;
		}
		StockAnalysisInfoBean bean = (StockAnalysisInfoBean) o;
		SimpleDateFormat sdf = BaseDateFormat.sh;
		if(bean.getFrequency() == 0){//手工执行
			GlobalsTool.timer.cancel();//手工执行就取消时钟
			return;
		}
		if(bean.getNextTime()==null||"".equals(bean.getNextTime())){//没有下次执行时间，立即执行
			Result rt = doCount();
			if(rt.getRetCode() == ErrorCanst.NUMBER_COMPARE_ERROR){
				return;
			}
			Date nextTime = new Date(new Date().getTime() + bean.getFrequency()*24*60*60*1000l);
			GlobalsTool.timer.cancel();
			GlobalsTool.timer = new Timer();
			GlobalsTool.timer.schedule(new StockAnalysisInfo(),nextTime,bean.getFrequency()*24*60*60*1000l);
		}else{//有下次执行时间，启动时钟
			try {
				Date nextTime = sdf.parse(bean.getNextTime());
				GlobalsTool.timer.cancel();
				GlobalsTool.timer = new Timer();
				GlobalsTool.timer.schedule(new StockAnalysisInfo(),nextTime,bean.getFrequency()*24*60*60*1000l);
			} catch (ParseException e) {

			}
		}
	}

	/**
	 * 执行计算库存上下限
	 * 并在执行后更新库存分析基础信息的上次执行时间和下次执行时间
	 * @return
	 */
	public Result doCount(){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
	            public int exec(final Session session) {
	                session.doWork(new Work() {
	                    public void execute(Connection connection) throws
	                            SQLException {
	                    	boolean error=false;
	                    	try{
	                    		if(8!=BaseEnv.version){//GM才有这个功能
	                    			error=true;
	                    		}
		                        Connection conn = connection;
		                        Statement s = conn.createStatement();
		                        //每个仓库中的每个商品的第一次采购入库
		                        String sql = "select min(billDate) as firstDate,goodsCode,stockCode,BatchNo,Hue,yearNo,Inch from tblStockDet where billType='tblBuyInStock' and period!=-1 group by goodsCode,stockCode,BatchNo,Hue,yearNo,Inch";
		                        ResultSet rst = s.executeQuery(sql);
		                        ArrayList<StockDet> vs = new ArrayList<StockDet>();
		                        while(rst.next()){
		                        	StockDet sd = new StockDet();
		                        	sd.setFirstDate(rst.getString("firstDate"));
		                        	sd.setGoodsCode(rst.getString("goodsCode"));
		                        	sd.setStockCode(rst.getString("stockCode"));
		                        	sd.setBatchNo(rst.getString("batchNo"));
		                        	sd.setHue(rst.getString("hue"));
		                        	sd.setInch(rst.getString("inch"));
		                        	sd.setYearNo(rst.getString("yearNo"));
		                        	vs.add(sd);
		                        }
		                        
		                        String sql2 = "delete from tblUpperLowerLimit";
		                        s.executeUpdate(sql2);
		                        String sql3 = "select * from tblStockAnalysisInfo";
		                        ResultSet rt = s.executeQuery(sql3);
		                        StockAnalysisInfoBean bean = new StockAnalysisInfoBean();
		                        if(rt.next()){
		                        	bean.setResort(rt.getInt("resort"));
		                        	bean.setFrequency(rt.getInt("frequency"));
		                        	bean.setLowerLimitProportion(rt.getDouble("lowerLimitProportion"));
		                        	bean.setUpperLimitProportion(rt.getDouble("upperLimitProportion"));
		                        }else{
		                        	error=true;
		                        }
		                        if(rt.next()){
		                        	error=true;
		                        }
//		                        yyyy-MM-dd HH:mm:ss的时间格式
		                        SimpleDateFormat sdf = BaseDateFormat.sh;
		                        ////yyyy-MM-dd的时间格式
		                        SimpleDateFormat sdf2 = BaseDateFormat.sd;
//		                            现在时间
		                        Date d = new Date();
		                       
		                        for(StockDet sd:vs){
		                        	//第一次入库时间
		                        	Date sdDate = sdf2.parse(sd.getFirstDate());
		                        	//当天时间-第一次入库时间
	                        		int day = (int) ((d.getTime()-sdDate.getTime())/(24*60*60*1000l));
//	                        		大于上架期间
		                        	if(day>=bean.getResort()){  
//		                        		如果客户的上架期间设置为0，当天时间-第一次入库时间不满一天也算一天
		                        		day = day==0?1:day;
		                        		UpperLowerLimitBean ub = new UpperLowerLimitBean();
		                        		
		                        		//出库总数
		                        		double outStockNum = 0;
		                        		String outStock = "select sum(OutstoreQty) from tblStockDet where billType = 'tblSalesOutStock' and period!=-1 group by goodsCode,stockCode,BatchNo,Hue,yearNo,Inch having goodsCode='"+sd.getGoodsCode()+"' and stockCode='"+sd.getStockCode()+"'";
		                        		ResultSet out = s.executeQuery(outStock);
		                        		if(out.next()){
		                        			outStockNum = outStockNum + out.getDouble(1);
		                        		}
		                        		//退货总数
		                        		double returnStockNum = 0;
		                        		String returnStock = "select sum(InstoreQty) from tblStockDet where billType = 'tblSalesReturnStock' and period!=-1 group by goodsCode,stockCode,BatchNo,Hue,yearNo,Inch having goodsCode='"+sd.getGoodsCode()+"' and stockCode='"+sd.getStockCode()+"'";
		                        		ResultSet ret = s.executeQuery(returnStock);
		                        		if(ret.next()){
		                        			returnStockNum = returnStockNum + ret.getDouble(1);
		                        		}
		                        		double sumOutStockNum = outStockNum-returnStockNum;//销售总数=出库总数-退货总数
		                        		
		                        		//采购总数
		                        		double BuyInStockNum = 0;
		                        		String BuyInStock = "select sum(InstoreQty) from tblStockDet where billType = 'tblBuyInStock' and period!=-1 group by goodsCode,stockCode,BatchNo,Hue,yearNo,Inch having goodsCode='"+sd.getGoodsCode()+"' and stockCode='"+sd.getStockCode()+"'";
		                        		ResultSet retb = s.executeQuery(BuyInStock);
		                        		if(retb.next()){
		                        			BuyInStockNum = BuyInStockNum + retb.getDouble(1);
		                        		}
		                        		
		                        		double stockNum = BuyInStockNum-sumOutStockNum;//当前库存=采购总数-销售总数
		                        		
		                        		
		                        		//库存上限
		       
		                        		int upperLimit = (int) Math.round((sumOutStockNum/day)+(sumOutStockNum/day)*bean.getUpperLimitProportion()/100);
		                        		int lowerLimit = (int) Math.round((sumOutStockNum/day)+(sumOutStockNum/day)*bean.getLowerLimitProportion()/100);//下限
		                        		if(sumOutStockNum<0){//销售数为负数，以当前库存做为库存上下限
		                        			upperLimit = (int) stockNum;
		                        			lowerLimit = (int) stockNum;
		                        		}
		                        		ub.setUpperLimit(upperLimit);
		                        		ub.setLowerLimit(lowerLimit);
		                        		
		                        		String id = IDGenerater.getId(); // 自动生成一个ID
		                        		ub.setId(id);
		                        		
		                        		
		                        		String BatchNo = sd.getBatchNo()==null?"''":"'"+sd.getBatchNo()+"'";
		                        		String Hue= sd.getHue()==null?"''":"'"+sd.getHue()+"'";
		                        		String yearNo= sd.getYearNo()==null?"''":"'"+sd.getYearNo()+"'";
		                        		String Inch= sd.getInch()==null?"''":"'"+sd.getInch()+"'";

		                        		String insertUbSql = "insert into tblUpperLowerLimit (id,upperLimit,lowerLimit,stockCode,goodsCode,BatchNo,Hue,yearNo,Inch) values " +
		                        		"('"+ub.getId()+"','"+ub.getUpperLimit()+"','"+ub.getLowerLimit()+"','"+sd.getStockCode()+"','"+sd.getGoodsCode()+"',"+BatchNo+","+Hue+","+yearNo+","+Inch+")";
		                        		
		                        		int num = s.executeUpdate(insertUbSql);
		                        		if(num==0){
		                        			error=true;
		                        		}
		                        	}
		                        }
		                        bean.setLastTime(sdf.format(d));
//		                           如果是手动执行，去掉下次执行时间
		                        if(bean.getFrequency() == 0){
		                        	bean.setNextTime("");
		                        }else{
		                        	//根据执行频率来更新下次执行时间
		                        	bean.setNextTime(sdf.format(new Date(d.getTime() + (bean.getFrequency()*24*60*60*1000l))));
		                        }
		                       
		                        String updateBean = "update tblStockAnalysisInfo set lastTime='"+bean.getLastTime()+"',nextTime='"+bean.getNextTime()+"'";
		                        int num = s.executeUpdate(updateBean);
		                        if(num!=1){
		                        	error=true;
                        		}
		                    }catch(Exception e){
		                    	
		                    	rs.setRetCode(ErrorCanst.NUMBER_COMPARE_ERROR);
		                    	e.printStackTrace();
	                        	return;
		                    }
		                    if(error){
		                    	rs.setRetCode(ErrorCanst.NUMBER_COMPARE_ERROR);
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
	 * 得到库存分析基础信息表中的数据
	 * @return
	 */
	public Result getStockAnalysisInfo(){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
	            public int exec(final Session session) {
	                session.doWork(new Work() {
	                    public void execute(Connection connection) throws
	                            SQLException {
	                    	boolean error = false;
	                    	try{
	                    	
		                        Connection conn = connection;
		                        Statement s = conn.createStatement();
		                        
		                        String sql3 = "select * from tblStockAnalysisInfo";
		                        ResultSet rt = s.executeQuery(sql3);
		                        StockAnalysisInfoBean bean = new StockAnalysisInfoBean();
		                        if(rt.next()){
		                        	bean.setFrequency(rt.getInt("frequency"));
		                        	bean.setLowerLimitProportion(rt.getDouble("lowerLimitProportion"));
		                        	bean.setResort(rt.getInt("resort"));
		                        	bean.setUpperLimitProportion(rt.getDouble("upperLimitProportion"));
		                        	bean.setLastTime(rt.getString("lastTime"));
		                        	bean.setNextTime(rt.getString("nextTime"));
		                        }else{
//		                        	库存分析基础信息表中没有数据");
		                        	error = true;
		                        }
		                        if(rt.next()){
//		                        	"库存分析基础信息表中的数据大于一条");
		                        	error = true;
		                        }
		                        rs.setRetVal(bean);
		                    }catch(Exception e){
		                    	
		                    	rs.setRetCode(ErrorCanst.NUMBER_COMPARE_ERROR);
		                    	
	                        	return;
		                    }
		                    if(error){
		                    	rs.setRetCode(ErrorCanst.NUMBER_COMPARE_ERROR);
		                    	
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
	 * 时钟
	 * @author Administrator
	 *
	 */
	public class StockAnalysisInfo extends TimerTask {
//		时钟执行的方法
		@Override
		public void run() {
			// TODO Auto-generated method stub
			StockAnalysisInfoMgt mgt = new StockAnalysisInfoMgt();
			mgt.doCount();
		}

	}
	
	
}
