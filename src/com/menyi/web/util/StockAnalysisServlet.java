package com.menyi.web.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.StockAnalysisInfoBean;
/**
 * 自动计算库存上下限
 * @author 徐磊
 *
 */
public class StockAnalysisServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public StockAnalysisServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	//点击自动执行来执行计算库存上下限
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=GBK");
		PrintWriter out = response.getWriter();
		StockAnalysisInfoMgt mgt = new StockAnalysisInfoMgt();
		Result rs = mgt.getStockAnalysisInfo();
		Object o = rs.getRetVal();
		//没有找到库存分析基础信息
		if(o == null){
			out.print(GlobalsTool.getMessage(request, "com.auto.error"));
			return;
		}
		StockAnalysisInfoBean bean = (StockAnalysisInfoBean) o;
		// yyyy-MM-dd HH:mm:ss的时间格式
		SimpleDateFormat sdf =BaseDateFormat.sh;

		if(bean.getFrequency() == 0){//手工执行
			Result re = mgt.doCount();
			if(re.getRetCode() == ErrorCanst.NUMBER_COMPARE_ERROR){//执行出错
				out.print(GlobalsTool.getMessage(request, "com.auto.error"));
				return;
			}
			GlobalsTool.timer.cancel();//取消时钟
			out.print(GlobalsTool.getMessage(request, "com.auto.succeed"));
		}else{
			try {
				Result re = mgt.doCount();
				if(re.getRetCode() == ErrorCanst.NUMBER_COMPARE_ERROR){
					out.print(GlobalsTool.getMessage(request, "com.auto.error"));
					return;
				}
				Result rs2 = mgt.getStockAnalysisInfo();
				Object o2 = rs2.getRetVal();
				StockAnalysisInfoBean bean2 = (StockAnalysisInfoBean) o2;
				Date nextTime = sdf.parse(bean2.getNextTime());
				GlobalsTool.timer.cancel();
				GlobalsTool.timer = new Timer();
				GlobalsTool.timer.schedule(new StockAnalysisInfo(),nextTime,bean.getFrequency()*24*60*60*1000l);//重新设置时钟
				out.print(GlobalsTool.getMessage(request, "com.auto.succeed"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				out.print(GlobalsTool.getMessage(request, "com.auto.error"));
			}
		}
	    
	    
	}

	/**
	 * 
	 * 在系统启动时调用,根据库存分析基础信息来执行库存上下限
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		if(8==BaseEnv.version){//GM才有这个功能
			StockAnalysisInfoMgt mgt = new StockAnalysisInfoMgt();
			mgt.serivce();
		}
	}
	/**
	 * 时钟
	 * @author Administrator
	 *
	 */
	public class StockAnalysisInfo extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			StockAnalysisInfoMgt mgt = new StockAnalysisInfoMgt();
			mgt.doCount();
		}

	}

}
