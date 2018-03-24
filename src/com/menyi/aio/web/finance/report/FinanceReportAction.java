package com.menyi.aio.web.finance.report;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.bean.AccMainSettingBean;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.bean.EnumerateItemBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.web.finance.voucher.VoucherMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.MgtBaseAction;

/**
 * 
 * <p>
 * Title:财务报表Action
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-05-13 上午 09:30
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
public class FinanceReportAction extends MgtBaseAction{
	
	FinanceReportMgt mgt = new FinanceReportMgt();
	
	/**
	 * exe 控制器入口函数
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		
		// 区分报表
		String optype = request.getParameter("optype");
		String type = request.getParameter("type");								//点击按钮类型（导出按钮，打印按钮）
		
		if(optype != null && "ReporttblAccBalance".equals(optype)){
			/* 总分类账报表 */
			forward = reportAccBalance(mapping, form, request, response);
			
		}else if(optype != null && "ReporttblAccDet".equals(optype)){
			
			/* 明细总分类账 */
			if(type!=null && "detail".equals(type)){
				//某个科目的明细分类账
				forward = reportAccDetDetail(mapping, form, request, response);
			}else if(type != null && "loadIsItem".equals(type)){
				//点击左边树中加载核算项
				forward = reportAccDetLoadIsItem(mapping, form, request, response);
			}else{
				//科目首页
				forward = reportAccDetIndex(mapping, form, request, response);
			}
			
		}else if(optype != null && "ReporttblAccAllDet".equals(optype)){
			/* 多栏式明细账 */
			if(type != null && "designList".equals(type)){
				//多栏式明细自定义列表
				forward = reportDesignList(mapping, form, request, response);
			}else if(type!=null && "dealDesign".equals(type)){
				//多栏式设计界面
				forward = reportDesign(mapping, form, request, response);
			}else if(type!=null && "delDesign".equals(type)){
				//删除单个设计数据
				forward = delDesign(mapping, form, request, response);
			}else if(type!=null && "queryDesign".equals(type)){
				//查询条件中获取多栏式明细账用户自定义的数据 下拉框
				forward = queryDesign(mapping, form, request, response);
			}else if(type!=null && "ajaxBearData".equals(type)){
				//ajax调用数据 父级会计科目获取所有最下级科目数据
				forward = ajaxBearData(mapping, form, request, response);
			}else if(type!=null && "queryAccIsItem".equals(type)){
				forward = queryAccIsItem(mapping, form, request, response);
			}else{
				//查询列表
				forward = reportAccAllDetIndex(mapping, form, request, response);
			}
		}else if(optype != null && "ReporttblAccCalculate".equals(optype)){
			/* 核算项目分类总账 */
			if(type!=null && "detail".equals(type)){
				//点击核算项目时显示详情
				forward = reporttblAccCalculateDetail(mapping, form, request, response);
			}else{
				//首页
				forward = reporttblAccCalculateIndex(mapping, form, request, response);
			}
		}else if(optype != null && "ReporttblAccCalculateDet".equals(optype)){
			/* 核算项目明细账 */
			if(type!=null && "detail".equals(type)){
				//点击会计科目进入查询核算项目明细详情
				forward = reporttblAccCalculateDetDetail(mapping, form, request, response);
			}else{
				//首页
				forward = reporttblAccCalculateDetIndex(mapping, form, request, response);
			}
		}else if(optype != null && "ReporttblTrialBalance".equals(optype)){
			/* 试算平衡报表 */
			forward = reporttblTrialBalance(mapping, form, request, response);
			
		}else if(optype != null && "ReportAccTypeInfoBalance".equals(optype)){
			/* 会计科目余额表 */
			forward = reportAccTypeInfoBalance(mapping, form, request, response);
			
		}else if(optype != null && "ReportAccTypeDayBalance".equals(optype)){
			/* 科目日报表 */
			forward = reportAccTypeDayBalance(mapping, form, request, response);
			
		}else if(optype != null && "ReportCertificateSum".equals(optype)){
			/* 凭证汇总表 */
			forward = reportCertificateSum(mapping, form, request, response);
			
		}else if(optype != null && "ReportAccCalculateBalance".equals(optype)){
			/* 核算项目余额表 */
			forward = reportAccCalculateBalance(mapping, form, request, response);
			
		}
		return forward;
	}
	
	/**
	 * 总分类账报表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportAccBalance(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		/* 查询所有币别类型 */
		List currencyList = new ArrayList();
		Result result = mgt.queryCurrencyAll();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			currencyList = (ArrayList)result.retVal;
			request.setAttribute("currencyList", currencyList);
		}
		//凭证设置
		result = new VoucherMgt().queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		
		/* 会计期间 */
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		
		//查询开账期间，页面输入会计期间时进行判断处理
		result = mgt.getCurrentlyPeriod();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("beginPeriod", result.retVal);
		}
		
		if(comeMode == null){
			/* 第一次进入 */
			return getForward(request, mapping, "reportAccBalanceList");
 		}
		
		//搜索条件
		String periodYearStart = request.getParameter("periodYearStart");				//会计期间开始年
		String periodStart = request.getParameter("periodStart");						//会计期间开始
		String periodYearEnd = request.getParameter("periodYearEnd");					//会计期间结束年
		String periodEnd = request.getParameter("periodEnd");							//会计期间结束
		String levelStart = request.getParameter("levelStart");							//科目级别开始
		String levelEnd = request.getParameter("levelEnd");								//科目级别结束
		String acctypeCodeStart = request.getParameter("acctypeCodeStart");				//科目代码开始
		String acctypeCodeEnd = request.getParameter("acctypeCodeEnd");					//科目代码结束
		String currencyName = request.getParameter("currencyName");						//币别
		String takeBrowNo = request.getParameter("takeBrowNo");							//无发生额不显示
		String balanceAndTakeBrowNo = request.getParameter("balanceAndTakeBrowNo");		//无发生额并且余额为零
		String binderNo = request.getParameter("binderNo");								//包括未过账凭证
		String showIsItem = request.getParameter("showIsItem");							//显示核算项目明细
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//显示禁用科目
		
		currencyName = currencyName==null?"":currencyName;
		Map<String, String> conMap = new HashMap<String, String>();
		conMap.put("periodYearStart", periodYearStart);
		conMap.put("periodStart", periodStart);
		conMap.put("periodYearEnd", periodYearEnd);
		conMap.put("periodEnd", periodEnd);
		conMap.put("levelStart", levelStart);
		conMap.put("levelEnd", levelEnd);
		conMap.put("acctypeCodeStart", acctypeCodeStart);
		conMap.put("acctypeCodeEnd", acctypeCodeEnd);
		conMap.put("currencyName", currencyName);
		conMap.put("takeBrowNo", takeBrowNo);
		conMap.put("balanceAndTakeBrowNo", balanceAndTakeBrowNo);
		conMap.put("binderNo", binderNo);
		conMap.put("showIsItem", showIsItem);
		conMap.put("showBanAccTypeInfo", showBanAccTypeInfo);
		
		String conditions = dealConditions(conMap);
		
		int pageSize = getParameterInt("pageSize", request);
		int pageNo = getParameterInt("pageNo", request);
        if (pageNo == 0 ) {
        	pageNo = 1;
        }
    	if(pageSize ==0){
    		pageSize = 500;
    	}
    	long time1 = System.currentTimeMillis();
    	
    	LoginBean loginBean = this.getLoginBean(request);
    	MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/FinanceReportAction.do?optype=ReporttblAccBalance");
    	
		/* 查询总分类账报表 */
    	Result rs = mgt.queryAccBalanceData(periodYearStart, periodStart, periodYearEnd, periodEnd,
    				levelStart, levelEnd, acctypeCodeStart, acctypeCodeEnd, currencyName, 
    				takeBrowNo, balanceAndTakeBrowNo, binderNo, showIsItem, showBanAccTypeInfo,loginBean, pageSize, pageNo, mop);
		long time2 = System.currentTimeMillis();
		BaseEnv.log.info("总分类账后台执行时间："+(time2-time1));
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			Object[] obj = (Object[])rs.retVal;
			
			//选择的币别
			String currType = String.valueOf(obj[1]);
			//保存的查询的数据
			List list = (ArrayList)obj[0];
			//查询成功进入列表界面
			request.setAttribute("accBalanceList", list);
			request.setAttribute("conMap", conMap);
			request.setAttribute("currType", currType);
			
			
			//重新组装数据格式用list<ArrayList>保存
			boolean flag = false;
			String periods = "";
			if(!periodYearStart.equals(periodYearEnd)){
				flag = true;
				periods = periodYearStart+"."+periodStart;
			}else{
				periods = periodStart;
			}
			//数据处理
			int lastY = 0;
			int lastX = 0;
			int lastX1 = 0;
			String currFlag = "";
			String title = "";
			String lineStr = "__AccNumber;__AccFullName;"+periods+";__credTypeidOrderNo;期初余额;";
			String printStr = "AccNumber;AccFullName;Period;credTypeidOrderNo;RecordComment;";
			if("".equals(currType) || "isBase".equals(currType)){
				//本位币或者综合本位币
				currFlag = "isBase";
				if("".equals(currType)){
					title += "<综合本位币>";
				}else{
					result = mgt.queryCurrencyName(currencyName);
					title += result.retVal;
				}
				lastX1 = 1;
				lineStr += ";;__isIniflag;__PeriodIniBase";
				printStr += "PeriodDebitSumBase;PeriodCreditSumBase;isflag;PeriodDCBalaBase";
			}else if("all".equals(currType)){
				//所有币别多栏式
				String[] fieldStr = new String[]{"PeriodDebitSumBase","PeriodCreditSumBase","isflag","PeriodDCBalaBase"};
				for(int k=0;k<fieldStr.length;k++){
					if(!"isIniflag".equals(fieldStr[k])){
						for(int l =0;l<currencyList.size();l++){
							String[] currStr = (String[])currencyList.get(l);
							String currency = currStr[0];
							if("true".equals(currStr[2])){
								//本位币
								currency = "";
							}
							if(k == fieldStr.length-1){
								lineStr += "__PeriodIni_"+currency+";";
							}else{
								lineStr += ";";
							}
							printStr += fieldStr[k]+"_"+currStr[3]+";";
						}
					}
					if(k == fieldStr.length-1){
						lineStr += "__PeriodIniBase;";
					}else{
						lineStr += ";";
					}
					printStr += fieldStr[k]+";";
				}
				lastY = 1;
				lastX = currencyList.size();
				lastX1 = currencyList.size()+1;
				currFlag = "all";
				title += "<所有币别多栏式>";
			}else{
				//外币
				lastY = 1;
				lastX = 1;
				lastX1 = 2;
				lineStr += ";;;;__isIniflag;;__PeriodIniBase";
				printStr += "PeriodDebitSum;PeriodDebitSumBase;PeriodCreditSum;PeriodCreditSumBase;isflag;PeriodDCBala;PeriodDCBalaBase";
				currFlag = "curr";
				result = mgt.queryCurrencyName(currencyName);
				title += result.retVal;
			}
			String[] strs = lineStr.split(";");
			
			List newList = new ArrayList();
			for(int j = 0;j<list.size(); j++){
				HashMap oldmap = (HashMap)list.get(j);
				List perList = new ArrayList();
				List periodList = new ArrayList();
				Object o = oldmap.get("periodList");
				if(o != null){
					//存在会计期间数据
					periodList = (ArrayList)o;
				}
				//无发生额不显示
				if(takeBrowNo !=null && "1".equals(takeBrowNo) && periodList.size()==0 
						&& "".equals(String.valueOf(oldmap.get("PeriodIniBase")))){
					continue;
				}
				//处理期初数据
				for(int k =0;k<strs.length;k++){
					String str = strs[k];
					if(str.length()>2 && "__".equals(str.substring(0,2))){
						//存在[] 要从Map中取
						String newStr = str.substring(2);
						perList.add(oldmap.get(newStr));
					}else{
						perList.add(str);
					}
				}
				newList.add(perList);
				//存在会计期间数据
				for(int l =0;l<periodList.size();l++){
					HashMap periodMap = (HashMap)periodList.get(l);
					
					perList = new ArrayList();
					//本期合计
					perList.add("");perList.add("");
					perList.add(flag==true?periodMap.get("Nyear")+"."+periodMap.get("Period"):periodMap.get("Period"));
					perList.add(periodMap.get("credTypeidOrderNo"));
					perList.add("本期合计");
					String lineVal = "PeriodDebitSumBase;PeriodCreditSumBase;PeriodDCBalaBaseisflag;PeriodDCBalaBase";
					if("all".equals(currFlag)){
						String lines = "";
						for(String s : lineVal.split(";")){
							if(!"PeriodDCBalaBaseisflag".equals(s)){
								for(int k=0;k<currencyList.size();k++){
									String[] currs = (String[])currencyList.get(k);
									String curId = s.replace("Base", "")+"_";
									if(!"true".equals(currs[2])){
										//本位币
										curId += currs[0];
									}
									lines += curId+";";
								}
							}
							lines += s+";";
						}
						lineVal = lines;
					}else if("curr".equals(currFlag)){
						lineVal = "PeriodDebitSum;PeriodDebitSumBase;PeriodCreditSum;PeriodCreditSumBase;PeriodDCBalaBaseisflag;PeriodDCBala;PeriodDCBalaBase";
					}
					String[] fieldNames = lineVal.split(";");
					for(String s : fieldNames){
						perList.add(periodMap.get(s));
					}
					newList.add(perList);
					
					//本年累计
					perList = new ArrayList();
					perList.add("");perList.add("");
					perList.add(flag==true?periodMap.get("Nyear")+"."+periodMap.get("Period"):periodMap.get("Period"));
					perList.add("");
					perList.add("本年累计");
					lineVal = "CurrYIniDebitSumBase;CurrYIniCreditSumBase;CurrYIniAmountisflag;CurrYIniAmount";
					if("all".equals(currFlag)){
						String lines = "";
						for(String s : lineVal.split(";")){
							if(!"CurrYIniAmountisflag".equals(s)){
								for(int k=0;k<currencyList.size();k++){
									String[] currs = (String[])currencyList.get(k);
									String curId = "";
									if("CurrYIniAmount".equals(s)){
										curId = s.replace("Amount", "")+"_";
									}else{
										curId = s.replace("Base", "")+"_";
									}
									if(!"true".equals(currs[2])){
										//本位币
										curId += currs[0];
									}
									lines += curId+";";
								}
							}
							lines += s+";";
						}
						lineVal = lines;
					}else if("curr".equals(currFlag)){
						lineVal = "CurrYIniDebitSum;CurrYIniDebitSumBase;CurrYIniCreditSum;CurrYIniCreditSumBase;CurrYIniAmountisflag;CurrYIni;CurrYIniAmount";
					}
					fieldNames = lineVal.split(";");
					for(String s : fieldNames){
						perList.add(periodMap.get(s));
					}
					newList.add(perList);
				}
			}
			
			
			/* 业务处理(导出列表数据) */
			String type = request.getParameter("type");
			if(type!= null && "exportList".equals(type)){
				//导出总分类账数据
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath()+"\\总分类账.xls" ;
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				//设置标题
				String titleName = "期间："+periodYearStart+"年第"+periodStart+"期至"+periodYearEnd+"年第"+periodEnd+"期        币别："+title;
				
				
				String locale =  this.getLocale(request).toString();
				//往strTitle中添加数据的格式为：（中文名称;标识;跨多少列;跨多少行）
				List strTitle = new ArrayList();
				
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccNumber", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccName", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccMain.Period", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:凭证字号;lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.RecordComment", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)+";lastX:"+lastX));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.LendAmount", locale)+";lastX:"+lastX));
				strTitle.add(setExportMap("name:余额;lastX:"+lastX1));
				if("curr".equals(currFlag)){
					//外币
					for(int l=0;l<3;l++){
						if(l==2){
							strTitle.add(setExportMap("name:"));
						}
						if(l == 0){
							strTitle.add(setExportMap("name:原币;nextLine:1;nextX:5"));
						}else{
							strTitle.add(setExportMap("name:原币"));
						}
						strTitle.add(setExportMap("name:本位币"));
					}
				}else if("all".equals(currFlag)){
					//所有币别多栏式
					for(int l=0;l<3;l++){
						if(l==2){
							strTitle.add(setExportMap("name:"));
						}
						for(int j = 0;j<currencyList.size();j++){
							String[] curr = (String[])currencyList.get(j);
							if(l == 0 && j == 0){
								strTitle.add(setExportMap("name:"+curr[1]+";nextLine:1;nextX:5"));
							}else{
								strTitle.add(setExportMap("name:"+curr[1]));
							}
						}
						strTitle.add(setExportMap("name:本位币"));
					}
				}
				rs = mgt.exportBanlance(fos, "总分类账",titleName, strTitle, newList);
				fos.close();
				if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("总分类账.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
							getMessage(request, "com.export.success.over")+fileName)
							.setBackUrl("/FinanceReportAction.do?optype=ReporttblAccBalance&comeMode=1&"+conditions).setNotAutoBack()
							.setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
			}else{
				//String[] str = new String[]{"AccNumber","AccFullName","Period","credTypeidOrderNo","RecordComment","DebitAmount","LendAmount","isflag","PeriodDCBalaBase"};
				request.getSession().setAttribute("accBalanceData", dealNewList(printStr.split(";"), newList));
			}
		}else{
			EchoMessage.error().add(getMessage(
                    request, "sms.query.error")).
                    setBackUrl("/FinanceReportAction.do?optype=ReporttblAccBalance").
                    setAlertRequest(request);
		}
		return getForward(request, mapping, "reportAccBalanceList");
	}
	
	
	
	/**
	 * 根据values组装导出头部的数据
	 * @param values               传的数据（如：   name:xxx;lastX:X;lastY:X）  以xxx:xxx;格式的名称对应的值
	 * @return
	 */
	public HashMap setExportMap(String values){
		HashMap setMap = new HashMap();
		if(values != null && !"".equals(values)){
			String[] value = values.split(";");			//得到xxx:xxx格式的数据
			if(value.length>0){
				//存在相应的记录
				for(String s : value){
					if(s.indexOf(":")!=-1){
						//存在相应类型的
						setMap.put(s.substring(0,s.indexOf(":")),s.substring(s.indexOf(":")+1));
					}else{
						//当只有一个值时，默认给它空值
						setMap.put(s,"");
					}
				}
			}
		}
		return setMap;
	}
	
	
	/**
	 * 根据筛选条件获取开始日期，结束日期
	 */
	protected Map reportDateInterval(Map<String, String> conMap,String dateType){
		Map m = new HashMap();
		/* 转换数据类型  */
		String begD = "";
		String endD = "";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		if("1".equals(dateType)){
			 //设置年份
		    cal.set(Calendar.YEAR,Integer.parseInt(conMap.get("periodYearStart")));
		    //设置月份
		    cal.set(Calendar.MONTH, Integer.parseInt(conMap.get("periodStart"))-1);		    
		    //设置日
		    Date date = null;
		    
		    cal.set(Calendar.DAY_OF_MONTH, 1);
		    date=cal.getTime();
		    begD = df.format(date);
		    
		    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		    
		    date=cal.getTime();		   
		    endD = df.format(date);
		    
		} else if("2".equals(dateType)){			
			begD = conMap.get("dateStart");
			endD = conMap.get("dateEnd");
		} else if("3".equals(dateType)){
			String momentType = conMap.get("momentType");	
			if("year".equals(momentType)){
				//*****本年******//
			} else if("halfyear".equals(momentType)){
				//*****最近半年*****//
			} else if("threemonth".equals(momentType)){
				//*****最近三个月****//
			} else if("tomonth".equals(momentType)){
				//*****上个月******//
			} else if("month".equals(momentType)){
				//******本月******//
			} else if("week".equals(momentType)){
				//******本周******//
			} else if("threeday".equals(momentType)){
				//******最近三天*****//
			} else if("day".equals(momentType)){
				//******今天*****//
			} else if("yesterday".equals(momentType)){
				//******昨天****//
			}
		}
		/****end****/
		m.put("begD", begD);
		m.put("endD", endD);
		return m;
	}
	
	/**
	 * 明细总分类账
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportAccDetIndex(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		/* 条件搜索 */
		String dateType = request.getParameter("dateType");								//时间类型（按期间查询，按日期查询）
		String periodYearStart = request.getParameter("periodYearStart");				//会计期间开始年
		String periodStart = request.getParameter("periodStart");						//会计期间开始
		String periodYearEnd = request.getParameter("periodYearEnd");					//会计期间结束年
		String periodEnd = request.getParameter("periodEnd");							//会计期间结束
		String dateStart = request.getParameter("dateStart");							//日期开始
		String dateEnd = request.getParameter("dateEnd");								//日期结束
		String momentType = request.getParameter("momentType");							//阶段（本年，本月等）
		String currencyName = request.getParameter("currencyName");						//币别
		String showInAccTypeInfo = request.getParameter("showInAccTypeInfo");			//显示对方科目
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//显示禁用科目
		String binderNo = request.getParameter("binderNo");								//包括未过账凭证
		String takeBrowNo = request.getParameter("takeBrowNo");							//无发生额不显示
		String balanceAndTakeBrowNo = request.getParameter("balanceAndTakeBrowNo");		//无发生额并且余额为零
		String showIsItem = request.getParameter("showIsItem");							//只显示明细科目
		String showIsAccType = request.getParameter("showIsAccType");					//显示对方科目核算项目
		String showTakePeriod = request.getParameter("showTakePeriod");					//显示无发生额的期间合计
		String showAccType = request.getParameter("showAccType");						//按对方科目多条显示
		
		/* 高级搜索 */
		String showDate = request.getParameter("showDate");								//显示业务日期
		String showMessage = request.getParameter("showMessage");						//显示凭证业务信息
		String showItemDetail = request.getParameter("showItemDetail");					//显示核算项目明细
		String gradeShow = request.getParameter("gradeShow");							//分级显示
		String itemSort = request.getParameter("itemSort");								//项目类别
		String itemLevel = request.getParameter("itemLevel");							//项目级别
		String itemCodeStart = request.getParameter("itemCodeStart");					//项目代码开始
		String itemCodeEnd = request.getParameter("itemCodeEnd");						//项目代码结束
		
		String keyWord = request.getParameter("keyWord");								//关键字查询
		String oldClassCode = request.getParameter("oldClassCode");						//旧的会计科目
		
		/* 查询所有币别 */
		Result result = mgt.queryCurrencyAll();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("currencyList", result.retVal);
		}
		//凭证设置
		result = new VoucherMgt().queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		
		/* 查询最小和最大期间用于在页面上验证期间 */
		result = mgt.queryPeriod();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("periodStr", result.retVal);
		}
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		/* 会计期间 */
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		if(comeMode == null){
			/* 第一次进入时不进行查询，弹出搜索条件 */
			return getForward(request, mapping, "reportAccDetIndex");
 		}
		
		/* 把搜索条件保存到Map中 */
		Map<String, String> conMap = new HashMap<String, String>();
		conMap.put("dateType", dateType);
		conMap.put("periodYearStart", periodYearStart);
		conMap.put("periodStart", periodStart);
		conMap.put("periodYearEnd", periodYearEnd);
		conMap.put("periodEnd", periodEnd);
		conMap.put("dateStart", dateStart);
		conMap.put("dateEnd", dateEnd);
		conMap.put("momentType", momentType);
		conMap.put("currencyName", currencyName);
		conMap.put("showInAccTypeInfo", showInAccTypeInfo);
		conMap.put("showBanAccTypeInfo", showBanAccTypeInfo);
		conMap.put("binderNo", binderNo);
		conMap.put("takeBrowNo", takeBrowNo);
		conMap.put("balanceAndTakeBrowNo", balanceAndTakeBrowNo);
		conMap.put("showIsItem", showIsItem);
		conMap.put("showIsAccType", showIsAccType);
		conMap.put("showTakePeriod", showTakePeriod);
		conMap.put("showAccType", showAccType);
		conMap.put("showDate", showDate);
		conMap.put("showMessage", showMessage);
		conMap.put("showItemDetail", showItemDetail);
		conMap.put("gradeShow", gradeShow);
		conMap.put("itemSort", itemSort);
		conMap.put("itemLevel", itemLevel);
		conMap.put("itemCodeStart", itemCodeStart);
		conMap.put("itemCodeEnd", itemCodeEnd);
		conMap.put("keyWord", keyWord);
				
		
		/* 搜索条件拼装成String */
		String conditions = dealConditions(conMap);
		request.setAttribute("conditions", conditions);
		request.setAttribute("conMap", conMap);
		request.setAttribute("oldClassCode", oldClassCode);
		
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/FinanceReportAction.do?optype=ReporttblAccDet");
		
		long time1 = System.currentTimeMillis();
		/* 查询会计科目目录 */
		result = mgt.queryAccTypeInfoAll(showBanAccTypeInfo,showIsItem,showItemDetail,
				gradeShow,itemSort,itemLevel,itemCodeStart,itemCodeEnd,keyWord,mop,lg);
		long time2 = System.currentTimeMillis();
		System.out.println("明细分类账左边树会计科目后台执行时间："+(time2-time1));
		/* 查询成功 */
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			StringBuffer folderTree = new StringBuffer("[");
			ArrayList list = (ArrayList)result.retVal;
			
			/* 对会计科目进行处理，在页面显示树 */
			for (int i = 0; i < list.size(); i++) {
				HashMap map1 = (HashMap) list.get(i);
				String classCodes = String.valueOf(map1.get("asClassCode"));
				if(showIsItem == null || "".equals(showIsItem)){
					//不显示明细科目
					if (folderTree.toString().indexOf("asClassCode:\"" + classCodes.substring(0, classCodes.length() - 5) + "\"") == -1) {
						folderTree.append(this.folderTree(list, String.valueOf(map1.get("AccNumber"))+" - "+replace(String.valueOf(map1.get("AccName"))), classCodes, Integer
								.valueOf(map1.get("isCatalog").toString()),String.valueOf(map1.get("classCode")),showItemDetail,String.valueOf(map1.get("isItem")),
								Integer.valueOf(map1.get("isCalculateParent").toString())));
					}
				}else{
					//显示明细科目
					if(classCodes.length()==5 || !"".equals(keyWord)) {
						folderTree.append("{ asClassCode:\"" + classCodes + "\",classCode:\""+String.valueOf(map1.get("classCode"))+"\",nocheck:true,name:\"" + String.valueOf(map1.get("AccNumber"))+" - "+replace(String.valueOf(map1.get("AccName")))+"\",nocheck:true,nodes:[");
						for (int j = 0; j < list.size(); j++) {
							HashMap map2 = (HashMap) list.get(j);
							String classCode2 = String.valueOf(map2.get("asClassCode"));
							if(classCode2.length()>5 && classCode2.substring(0,5).equals(classCodes)){
								folderTree.append("{ asClassCode:\""+map2.get("asClassCode")+"\",classCode:\"" + map2.get("classCode") + "\",");
								Integer isCatalog = Integer.valueOf(map2.get("isCatalog").toString());
								Integer isCalculateParent = Integer.valueOf(map2.get("isCalculateParent").toString());
								if ((isCatalog != null && isCatalog > 0) || isCalculateParent == 1) {
									folderTree.append("isParent:true,isItem:\""+map2.get("isItem").toString()+"\",");
								}
								folderTree.append("nocheck:true,name:\"" + String.valueOf(map2.get("AccNumber"))+" - "+replace(String.valueOf(map2.get("AccName")))
										+ "\"}");
							}
						}
						folderTree.append("]}");
					}
				}
			}
			folderTree = new StringBuffer(folderTree.toString().replaceAll("\\}\\{", "},{"));
			folderTree.append("]");
			request.setAttribute("folderTree", folderTree.toString());
		}
		return getForward(request, mapping, "reportAccDetIndex");
	}
	
	private String replace( String name){
		return  name.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"");
		
	}
	
	/**
	 * 左边目录
	 * @param list
	 * @param folderName
	 * @param classCode
	 * @param isCatalog
	 * @return
	 */
	private String folderTree(ArrayList list,String folderName,String asclassCode,Integer isCatalog,
			String classCode,String showItemDetail,String isItem,Integer isCalculateParent){
		folderName = folderName.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"");
		StringBuffer folderTree = new StringBuffer("");
		boolean falg = false;
		if(showItemDetail!=null && !"".equals(showItemDetail)){
			falg = true;
		}
		if ((isCatalog != null && isCatalog > 0) || isCalculateParent == 1) {
			// 存在下级
			folderTree.append("{ asClassCode:\"" + asclassCode + "\",classCode:\""+classCode+"\",name:\"" + folderName	+ "\",nocheck:true,");
			if(falg){
				//选择了显示核算项目明细
				folderTree.append("isParent:true,isItem:\""+isItem+"\",");
			}
			folderTree.append("nodes:[");
			for (int i = 0; i < list.size(); i++) {
				HashMap map = (HashMap) list.get(i);
				String classCodes = String.valueOf(map.get("asClassCode"));
				if(map.get("isCalculateParent") == null){
					map.put("isCalculateParent", 0);
				}
				if(classCodes.substring(0, classCodes.length() - 5).equals(asclassCode)) {
					folderTree.append(this.folderTree(list, String.valueOf(map.get("AccNumber"))+" - "+replace(String.valueOf(map.get("AccName"))), classCodes, Integer
							.valueOf(map.get("isCatalog").toString()),String.valueOf(map.get("classCode")),showItemDetail,String.valueOf(map.get("isItem")),
							Integer.valueOf(map.get("isCalculateParent").toString())));
				}
			}
			folderTree.append("]}");
		} else {
			folderTree.append("{ classCode:\"" + classCode + "\",asClassCode:\"" + asclassCode + "\",nocheck:true,");
			if(falg){
				folderTree.append("isItem:\""+isItem+"\",");
			}
			folderTree.append("name:\"" + folderName + "\"}");
		}
    	return folderTree.toString();
    }
	
	
	/**
	 * 构建树
	 * @param grouplist
	 * @param list
	 * @param itemSort
	 * @return
	 */
	private String groupTree(ArrayList grouplist,ArrayList list,String itemSort){
		StringBuffer folderTree = new StringBuffer("");
		for(int i=0;i<grouplist.size();i++){
			String[] group = (String[])grouplist.get(i);
			folderTree.append("{ classCode:\"" + group[0] + "\",name:\"" + group[2]	+ "\",nocheck:true,nodes:[");
			for(int j=0;j<list.size();j++){
				HashMap map = (HashMap) list.get(j);
				if(group[0].equals(map.get(itemSort))){
					folderTree.append("{ classCode:\"" + String.valueOf(map.get("classCode")) + "\",nocheck:true,name:\"" + String.valueOf(map.get("AccNumber"))+" - "+replace(String.valueOf(map.get("AccName")))	+ "\"}");
				}
			}
			folderTree.append("]}");
		}
		return folderTree.toString();
	}
	
	/**
	 * 明细分类账科目详情
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportAccDetDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String classCode = request.getParameter("classCode");
		if(classCode == null || "".equals(classCode)){
			//不存在会计科目组
			request.setAttribute("comeMode", "");
			return getForward(request, mapping, "reportAccDetDetail");
		}
		/* 条件搜索 */
		String dateType = request.getParameter("dateType");								//时间类型（按期间查询，按日期查询）
		String periodYearStart = request.getParameter("periodYearStart");				//会计期间开始年
		String periodStart = request.getParameter("periodStart");						//会计期间开始
		String periodYearEnd = request.getParameter("periodYearEnd");					//会计期间结束年
		String periodEnd = request.getParameter("periodEnd");							//会计期间结束
		String dateStart = request.getParameter("dateStart");							//日期开始
		String dateEnd = request.getParameter("dateEnd");								//日期结束
		String momentType = request.getParameter("momentType");							//阶段（本年，本月等）
		String currencyName = request.getParameter("currencyName");						//币别(id-外币，空-综合本位币，all-所有币别多栏式，currency-所有币别)
		String showInAccTypeInfo = request.getParameter("showInAccTypeInfo");			//显示对方科目
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//显示禁用科目
		String binderNo = request.getParameter("binderNo");								//包括未过账凭证
		String takeBrowNo = request.getParameter("takeBrowNo");							//无发生额不显示
		String balanceAndTakeBrowNo = request.getParameter("balanceAndTakeBrowNo");		//无发生额并且余额为零
		String showIsAccType = request.getParameter("showIsAccType");					//显示对方科目核算项目
		String showTakePeriod = request.getParameter("showTakePeriod");					//显示无发生额的期间合计
		String showAccType = request.getParameter("showAccType");						//按对方科目多条显示
		
		currencyName = currencyName==null?"":currencyName;
		/* 高级搜索 */
		String showDate = request.getParameter("showDate");								//显示业务日期
		String showMessage = request.getParameter("showMessage");						//显示凭证业务信息
		String showItemDetail = request.getParameter("showItemDetail");					//显示核算项目明细
		String gradeShow = request.getParameter("gradeShow");							//分级显示
		String itemSort = request.getParameter("itemSort");								//项目类别
		String itemLevel = request.getParameter("itemLevel");							//项目级别
		String itemCodeStart = request.getParameter("itemCodeStart");					//项目代码开始
		String itemCodeEnd = request.getParameter("itemCodeEnd");						//项目代码结束
		
		String orderby = request.getParameter("orderby");								//排序
		
		
		/* 把搜索条件保存到Map中 */
		Map<String, String> conMap = new HashMap<String, String>();
		conMap.put("dateType", dateType);
		conMap.put("periodYearStart", periodYearStart);
		conMap.put("periodStart", periodStart);
		conMap.put("periodYearEnd", periodYearEnd);
		conMap.put("periodEnd", periodEnd);
		conMap.put("dateStart", dateStart);
		conMap.put("dateEnd", dateEnd);
		conMap.put("momentType", momentType);
		conMap.put("currencyName", currencyName);
		conMap.put("binderNo", binderNo);
		conMap.put("takeBrowNo", takeBrowNo);
		conMap.put("balanceAndTakeBrowNo", balanceAndTakeBrowNo);
		conMap.put("showInAccTypeInfo", showInAccTypeInfo);
		conMap.put("showBanAccTypeInfo", showBanAccTypeInfo);
		conMap.put("showIsAccType", showIsAccType);
		conMap.put("showTakePeriod", showTakePeriod);
		conMap.put("showAccType", showAccType);
		conMap.put("showDate", showDate);
		conMap.put("showMessage", showMessage);
		conMap.put("showItemDetail", showItemDetail);
		conMap.put("gradeShow", gradeShow);
		conMap.put("itemSort", itemSort);
		conMap.put("itemLevel", itemLevel);
		conMap.put("itemCodeStart", itemCodeStart);
		conMap.put("itemCodeEnd", itemCodeEnd);
		conMap.put("orderby", orderby);
		request.setAttribute("conMap", conMap);
		
		String conditions = dealConditions(conMap);
		/**
		 * 查询明细分类账明细
		 */
		long time1 = System.currentTimeMillis();
		Result rs = mgt.queryAccDetData((HashMap<String, String>)conMap,classCode);
		long time2 = System.currentTimeMillis();
		System.out.println("明细分类账详情后台执行时间："+(time2-time1));
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			/* 查询成功 */
			Object[] obj = (Object[])rs.retVal;
			HashMap initMap = (HashMap)obj[0];					//期初金额
			List accDetList = (ArrayList)obj[1];				//明细
			String currType = String.valueOf(obj[2]);			//外币标识（all-所有币种多栏式，空-综合本位币，isBase-本位币，currency-所有外币，外币Id-各种外币）
			List currencyList = (ArrayList)obj[3];				//所有外币List
			request.setAttribute("initMap", initMap);
			request.setAttribute("accDetList", accDetList);
			request.setAttribute("currType", currType);
			request.setAttribute("currencyList", currencyList);
			
			
			//数据处理
			int lastY = 0;
			int lastX = 0;
			int lastX1 = 0;
			String currFlag = "";
			String title = "";
			String lineStrInit = "periodBegin;";
			String lineStrDet = "BillDate;";
			//显示业务日期
			if(showDate != null && "1".equals(showDate)){
				lineStrInit += ";";
				lineStrDet += "BillDate;";
			}
			lineStrInit += ";RecordComment;";
			lineStrDet += "CredTypeOrderNo;RecordComment;";
			//显示对方科目
			if(showInAccTypeInfo != null && "1".equals(showInAccTypeInfo)){
				lineStrInit += ";";
				lineStrDet += "detailacccode;";
			}
			//显示凭证业务信息
			if(showMessage != null && "1".equals(showMessage)){
				lineStrInit += ";;";
				lineStrDet += "RefModuleName;RefBillTypeName;";
			}
			String line =  "sumDebitAmountBase;sumLendAmountBase;isflag;sumBalanceAmountBase";
			if("".equals(currType) || "isBase".equals(currType)){
				//本位币或者综合本位币
				currFlag = "isBase";
				if("".equals(currType)){
					title += "<综合本位币>";
				}else{
					rs = mgt.queryCurrencyName(currencyName);
					title += rs.retVal;
				}
				lastX1 = 1;
			}else if("all".equals(currType)){
				//所有币别多栏式
				String newLine = "";
				String[] str = line.split(";");
				for(String s : str){
					if(!"isflag".equals(s)){
						for(int l =0;l<currencyList.size();l++){
							String[] currStr = (String[])currencyList.get(l);
							String currency = currStr[0];
							if("true".equals(currStr[2])){
								//本位币
								currency = "";
							}
							newLine += s.replaceAll("Base", "")+"_"+currency+";";
						}
					}
					newLine += s+";";
				}
				line = newLine;
				lastY = 1;
				lastX = currencyList.size();
				lastX1 = currencyList.size()+1;
				currFlag = "all";
				title += "<所有币别多栏式>";
			}else{
				//外币
				lastY = 1;
				lastX = 1;
				lastX1 = 2;
				line = "sumDebitAmount;sumDebitAmountBase;sumLendAmount;sumLendAmountBase;isflag;sumBalanceAmount;sumBalanceAmountBase";
				currFlag = "curr";
				rs = mgt.queryCurrencyName(currencyName);
				title += rs.retVal;
			}
			
			List newList = new ArrayList();
			
			//填充期初金额
			List perList = new ArrayList();
			lineStrInit = lineStrInit+line;
			String[] fieldStr = lineStrInit.split(";");
			for(String s : fieldStr){
				if("RecordComment".equals(s)){
					perList.add("期初余额");
				}else if("isflag".equals(s)){
					perList.add(initMap.get("isIniflag"));
				}else if("sumDebitAmountBase".equals(s) || "sumLendAmountBase".equals(s)){
					perList.add("");
				}else{
					perList.add(initMap.get(s));
				}
			}
			newList.add(perList);
			perList = new ArrayList();
			
			//获取明细
			lineStrDet = lineStrDet+line;
			fieldStr = lineStrDet.split(";");
			for(int i = 0;i<accDetList.size(); i++){
				HashMap oldmap = (HashMap)accDetList.get(i);
				List detailList = (ArrayList)oldmap.get("detail");
				
				//获取单个科目的明细数据
				for(int j =0;j<detailList.size();j++){
					perList = new ArrayList();
					HashMap detailMap = (HashMap)detailList.get(j);
					if(detailMap != null){
						for(String s : fieldStr){
							perList.add(detailMap.get(s));
							if("detailacccode".equals(s)){
//								perList.add(detailMap.get("detailacccode")+" "+detailMap.get("accname"));
							}
						}
					}
					newList.add(perList);
				}
				
				//本期合计
				perList = new ArrayList();
				for(String s : fieldStr){
					if("RecordComment".equals(s)){
						perList.add("本期合计");
					}else if("BillDate".equals(s)){
						perList.add(oldmap.get("periodEnd"));
					}else{
						perList.add(oldmap.get(s));
					}
				}
				newList.add(perList);
				
				//本年累计
				perList = new ArrayList();
				for(String s : fieldStr){
					if("RecordComment".equals(s)){
						perList.add("本年累计");
					}else if("BillDate".equals(s)){
						perList.add(oldmap.get("periodEnd"));
					}else{
						if("isflag".equals(s)){
							s = "isYearflag";
						}else{
							s = "year_"+s;
						}
						perList.add(oldmap.get(s));
					}
				}
				newList.add(perList);
			}
			
			/**
			 * 导出列表处理
			 */
			String dealtype = request.getParameter("dealtype");
			if(dealtype != null && "exportList".equals(dealtype)){
				
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath()+"\\明细分类账.xls" ;
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				//设置标题
				String titleName = "";
				if(dateType!=null && "1".equals(dateType)){
					//选择会计期间
					titleName = "期间："+periodYearStart+"年第"+periodStart+"期至"+periodYearEnd+"年第"+periodEnd+"期";
				}else if(dateType!=null && "2".equals(dateType)){
					//选择日期
					titleName = "日期："+dateStart+" 至 "+dateEnd;
				}else if(dateType!=null && "3".equals(dateType)){
					//选择阶段
					titleName = "阶段类别：";
					if("year".equals(momentType)){
						titleName += "本年";
					}else if("halfyear".equals(momentType)){
						titleName += "最近半年";
					}else if("threemonth".equals(momentType)){
						titleName += "最近三个月";
					}else if("tomonth".equals(momentType)){
						titleName += "上一个月";
					}else if("month".equals(momentType)){
						titleName += "本月";
					}else if("week".equals(momentType)){
						titleName += "本周";
					}else if("threeday".equals(momentType)){
						titleName += "最近三天";
					}else if("day".equals(momentType)){
						titleName += "今天";
					}else if("yesterday".equals(momentType)){
						titleName += "昨天";
					}
				}
				
				int count = 3;
				String locale =  this.getLocale(request).toString();
				//往strTitle中添加数据的格式为：（中文名称;标识;跨多少列;跨多少行）
				List strTitle = new ArrayList();
				strTitle.add(setExportMap("name:日期;lastY:"+lastY));
				if(showDate != null && "1".equals(showDate)){
					strTitle.add(setExportMap("name:业务日期;lastY:"+lastY));
					count++;
				}
				strTitle.add(setExportMap("name:凭证字号;lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.RecordComment", locale)+";lastY:"+lastY));
				if(showInAccTypeInfo != null && "1".equals(showInAccTypeInfo)){
					//显示对方科目
					strTitle.add(setExportMap("name:对方科目;lastY:"+lastY));
					count++;
				}
				if(showMessage != null && "1".equals(showMessage)){
					strTitle.add(setExportMap("name:系统模块;lastY:"+lastY));
					strTitle.add(setExportMap("name:业务描述;lastY:"+lastY));
					count=count+2;
				}
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)+";lastX:"+lastX));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.LendAmount", locale)+";lastX:"+lastX));
				strTitle.add(setExportMap("name:余额;lastX:"+lastX1));
				if("curr".equals(currFlag)){
					//外币
					for(int l=0;l<3;l++){
						if(l==2){
							strTitle.add(setExportMap("name:"));
						}
						if(l == 0){
							strTitle.add(setExportMap("name:原币;nextLine:1;nextX:"+count));
						}else{
							strTitle.add(setExportMap("name:原币"));
						}
						strTitle.add(setExportMap("name:本位币"));
					}
				}else if("all".equals(currFlag)){
					//所有币别多栏式
					for(int l=0;l<3;l++){
						if(l==2){
							strTitle.add(setExportMap("name:"));
						}
						for(int j = 0;j<currencyList.size();j++){
							String[] curr = (String[])currencyList.get(j);
							if(l == 0 && j == 0){
								strTitle.add(setExportMap("name:"+curr[1]+";nextLine:1;nextX:"+count));
							}else{
								strTitle.add(setExportMap("name:"+curr[1]));
							}
						}
						strTitle.add(setExportMap("name:本位币"));
					}
				}
				
				rs = mgt.exportBanlance(fos, "明细分类账",titleName, strTitle, newList);
				fos.close();
				if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("明细分类账.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
							getMessage(request, "com.export.success.over")+fileName)
							.setBackUrl("/FinanceReportAction.do?optype=ReporttblAccDet&comeMode=1&"+conditions).setNotAutoBack()
							.setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
			}else{
				//处理数据进行保存到内存中打印好取数据
				String srtLine = "BillDate";
				if(showDate != null && "1".equals(showDate)){
					srtLine += ";BillDates";
				}
				srtLine += ";credTypeidOrderNo;RecordComment";
				if("all".equals(currType)||"currency".equals(currType)){
					srtLine += ";sumAmount";
				}
				if(showInAccTypeInfo != null && "1".equals(showInAccTypeInfo)){
					srtLine += ";detailacccode";
				}
				
				if(showMessage != null && "1".equals(showMessage)){
					srtLine += ";RefModuleName";
					srtLine += ";RefBillTypeName";
				}
				
				srtLine += ";sumDebitAmountBase;sumLendAmountBase";
				if("all".equals(currType)||"currency".equals(currType)){
					srtLine += ";Currency";
				}
				srtLine += ";isflag";
				if("all".equals(currType)||"currency".equals(currType)){
					srtLine += ";CurrencyRate";
				}
				srtLine+=";sumBalanceAmountBase";
				request.getSession().setAttribute("accBalanceData", dealNewList(srtLine.split(";"), newList));
			}
		}else{
			/* 查询失败 */
			EchoMessage.error().add(getMessage(
                    request, "sms.query.error")).
                    setBackUrl("/FinanceReportAction.do?optype=ReporttblAccDet&type=detail").
                    setAlertRequest(request);
		}
		request.setAttribute("comeMode", "comeMode");
		return getForward(request, mapping, "reportAccDetDetail");
	}
	
	/**
	 * 点击左边的会计科目是核算类科目时加载子项中的核算科目
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportAccDetLoadIsItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String classCode = request.getParameter("classCode");							//科目
		String asClassCode = request.getParameter("asClassCode");
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//显示禁用科目
		String showItemDetail = request.getParameter("showItemDetail");					//显示核算项目明细
		String gradeShow = request.getParameter("gradeShow");							//分级显示
		String itemSort = request.getParameter("itemSort");								//项目类别
		String itemLevel = request.getParameter("itemLevel");							//项目级别
		String itemCodeStart = request.getParameter("itemCodeStart");					//项目代码开始
		String itemCodeEnd = request.getParameter("itemCodeEnd");						//项目代码结束
		
		/* 查询核算的科目数据 */
		Result result = mgt.queryAccTypeIsItem(classCode,asClassCode, showBanAccTypeInfo, showItemDetail, gradeShow,
				itemSort, itemLevel, itemCodeStart, itemCodeEnd);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			StringBuffer folderTree = new StringBuffer("");
			ArrayList list = (ArrayList)result.retVal;
			for (int i = 0; i < list.size(); i++) {
				HashMap map = (HashMap) list.get(i);
				String classCodes = String.valueOf(map.get("asClassCode"));
				folderTree.append("[{ \"asClassCode\":\"" + classCodes + "\",\"classCode\":\""+String.valueOf(map.get("classCode"))+"\",\"name\":\"" + String.valueOf(map.get("AccNumber"))+" - "+replace(String.valueOf(map.get("AccName")))+"\",\"nocheck\":true}];");
			}
			request.setAttribute("msg", folderTree.toString());
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 多栏式明细账列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportAccAllDetIndex(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		//凭证设置
		Result rs = new VoucherMgt().queryVoucherSetting();
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) rs.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		
		List currencyList = new ArrayList();
		/* 查询所有币种数据 */
		rs = mgt.queryCurrencyAll();
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			currencyList = (ArrayList)rs.retVal;
			request.setAttribute("currencyList", currencyList);
		}
		
		/* 会计期间 */
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		
		//查询开账期间，页面输入会计期间时进行判断处理
		rs = mgt.getCurrentlyPeriod();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("beginPeriod", rs.retVal);
		}

		if(comeMode == null){
			/* 第一次进入 */
			return getForward(request, mapping, "reportAccAllDetList");
 		}
		/* 条件搜索 */
		String accName = request.getParameter("accName");								//多栏账名称
		String periodYearStart = request.getParameter("periodYearStart");				//会计期间开始年
		String periodStart = request.getParameter("periodStart");						//会计期间开始
		String periodYearEnd = request.getParameter("periodYearEnd");					//会计期间结束年
		String periodEnd = request.getParameter("periodEnd");							//会计期间结束
		String showFormCurrency = request.getParameter("showFormCurrency");				//显示原币金额
		String showOperationBranch = request.getParameter("showOperationBranch");		//业务记录分行显示
		String binderNo = request.getParameter("binderNo");								//包括未过账凭证
		String showPeriodBalaBase = request.getParameter("showPeriodBalaBase");			//显示明细项目期末余额
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//显示禁用科目
		String takeBrowNo = request.getParameter("takeBrowNo");							//无发生额不显示
		String balanceAndTakeBrowNo = request.getParameter("balanceAndTakeBrowNo");		//余额为零且无发生额不显示
		
		String itemSort = request.getParameter("itemSort");								//项目类别
		String acctypeCodeStart = request.getParameter("acctypeCodeStart");				//项目代码开始
		String acctypeCodeEnd = request.getParameter("acctypeCodeEnd");					//项目代码结束
		
		/* 把搜索条件保存到Map中 */
		Map<String, String> conMap = new HashMap<String, String>();
		conMap.put("accName", accName);
		conMap.put("periodYearStart", periodYearStart);
		conMap.put("periodStart", periodStart);
		conMap.put("periodYearEnd", periodYearEnd);
		conMap.put("periodEnd", periodEnd);
		conMap.put("showFormCurrency", showFormCurrency);
		conMap.put("showOperationBranch", showOperationBranch);
		conMap.put("binderNo", binderNo);
		conMap.put("showPeriodBalaBase", showPeriodBalaBase);
		conMap.put("showBanAccTypeInfo", showBanAccTypeInfo);
		conMap.put("takeBrowNo", takeBrowNo);
		conMap.put("balanceAndTakeBrowNo", balanceAndTakeBrowNo);
		conMap.put("itemSort", itemSort);
		conMap.put("acctypeCodeStart", acctypeCodeStart);
		conMap.put("acctypeCodeEnd", acctypeCodeEnd);
		request.setAttribute("conMap", conMap);
		
		String conditions = dealConditions(conMap);
		
		/* 根据设计的id查询用户自定义设计的记录 */
		String[] designValue = null;
		rs = mgt.queryDesignById(accName);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			designValue = (String[])rs.retVal;
		}
		/**
		 * 查询多栏式明细账列表
		 */
		long time1 = System.currentTimeMillis();
		rs = mgt.queryAllDetData((HashMap<String, String>)conMap,designValue);
		long time2 = System.currentTimeMillis();
		System.out.println("多栏式明细账后台执行时间："+(time2-time1));
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			
			HashMap allDetDataMap = (HashMap)rs.retVal;
			request.setAttribute("allDetData", allDetDataMap);
			request.setAttribute("designBean", designValue);
			
			
			/* 业务处理(导出列表数据) */
			String type = request.getParameter("type");
			if(type!= null && "exportList".equals(type)){
				
				//所有查询到的数据
				HashMap initData = new HashMap();												//期初金额的Map
				List periodList = new ArrayList();												//查询的所有会计期间
				if(allDetDataMap != null && allDetDataMap.size()>0){
					initData = (HashMap)allDetDataMap.get("initData");
					periodList = (ArrayList)allDetDataMap.get("periodList");
				}
				
				//导出总分类账数据
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath()+"\\多栏式明细账.xls" ;
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				//设置标题
				String titleName = "期间："+periodYearStart+"年第"+periodStart+"期至"+periodYearEnd+"年第"+periodEnd+"期";
				titleName += "  多栏式名称："+designValue[2];
				List newList = new ArrayList();
				
				/**
				 * 处理用设置的多栏式格式
				 */
				int debitCount = 0;										//用户设置的科目借方总数
				int creditCount = 0;									//用户设置的科目贷方总数
				List setAccNumberList = new ArrayList();				//用户设置的科目编号集合
				List setNameDebitList = new ArrayList();				//用户设置的借方科目中文名称
				List setNameLendList = new ArrayList();					//用户设置的贷方科目中文名称
				String[] columndataStrs = designValue[3].split("//");
				for(String columndataStr : columndataStrs){
					String[] columndata = columndataStr.split(";");
					if("1".equals(columndata[0])){
						//借方
						debitCount ++;
						setNameDebitList.add(columndata[2]);
					}else if("2".equals(columndata[0])){
						//贷方
						creditCount ++;
						setNameLendList.add(columndata[2]);
					}
					setAccNumberList.add(columndata[0]+";"+columndata[1]);
				}
				String currsStrs = designValue[4];
				int currCount = 0;
				int lastY = 1;
				int lastX = 1;
				int lastY1 = 1;
				
				//是否显示原币金额
				boolean showCurr = false;
				boolean isCurr = false;
				if(showFormCurrency != null && "1".equals(showFormCurrency)){
					showCurr = true;
				}
				List setCurrList = new ArrayList();
				if(currsStrs.length()>0){
					//存在选择的外币
					isCurr = true;
					currCount = currsStrs.split(",").length;
					if(showCurr){
						currCount = currCount*2;
					}
					for(String s : currsStrs.split(",")){
						rs = mgt.queryCurrencyName(s);
						setCurrList.add(rs.retVal);
					}
				}
				if(isCurr){
					lastY = 2;
					lastY1 = 1;
				}
				
				//头部显示的数据列头
				String locale =  this.getLocale(request).toString();
				List strTitle = new ArrayList();
				strTitle.add(setExportMap("name:日期;lastY:"+lastY));
				strTitle.add(setExportMap("name:凭证字号;lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.RecordComment", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)+";lastY:"+lastY1+";lastX:"+currCount));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.LendAmount", locale)+";lastY:"+lastY1+";lastX:"+currCount));
				strTitle.add(setExportMap("name:余额;lastX:2;lastY:"+lastY1));
				if(debitCount>0){
					strTitle.add(setExportMap("name:借方;lastX:"+(debitCount*currCount-1)));					
				}
				if(creditCount>0){
					strTitle.add(setExportMap("name:贷方;lastX:"+(creditCount*currCount-1)));					
				}
				
				//strTitle.add(setExportMap("name:"));
				for(int i = 0;i<setNameDebitList.size();i++){
					if(i == 0){
						strTitle.add(setExportMap("name:"+setNameDebitList.get(i)+";nextLine:1;lastX:"+(currCount-1)));
					}else{
						strTitle.add(setExportMap("name:"+setNameDebitList.get(i)+";lastX:"+(currCount-1)));
					}
				}
				for(int i = 0;i<setNameLendList.size();i++){
					if(i==0 && setNameDebitList.size()==0){
						strTitle.add(setExportMap("name:"+setNameLendList.get(i)+";nextLine:1;lastX:"+(currCount-1)));
					}else{
						strTitle.add(setExportMap("name:"+setNameLendList.get(i)+";lastX:"+(currCount-1)));
					}
				}
				for(int i=0;i<2;i++){
					for(int j =0;j<setCurrList.size();j++){
						if(i == 0){
							strTitle.add(setExportMap("name:"+setCurrList.get(j)+";nextLine:1;"+(currCount-1)));
						}else{
							strTitle.add(setExportMap("name:"+setCurrList.get(j)));
						}
					}
				}
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.JdFlag", locale)));
				strTitle.add(setExportMap("name:"));
				for(int i=0;i<columndataStrs.length;i++){
					for(int j =0;j<setCurrList.size();j++){
						strTitle.add(setExportMap("name:"+setCurrList.get(j)));
					}
				}
				
//				List strList = new ArrayList();
//				strList.add("totalsumDebitAmount");
//				strList.add("totalsumLendAmount");
//				strList.add("totalBalanceisflag");
//				strList.add("totalBalance");
//				for(int i =0;i<setAccNumberList.size();i++){
//					String setAccNumber = String.valueOf(setAccNumberList.get(i));
//					String[] setjdflagOrAccNumber = setAccNumber.split(";");
//					if("1".equals(setjdflagOrAccNumber[0])){
//						//借方
//						strList.add("curr__"+setjdflagOrAccNumber[1]+"_sumDebitAmount");
//					}else{
//						//贷方
//						strList.add("curr__"+setjdflagOrAccNumber[1]+"_sumLendAmount");
//					}
//				}
				//处理期初数据
				List preList = new ArrayList();
				preList.add(allDetDataMap.get("AccDate"));
				preList.add("");
				preList.add("期初余额");

//				for(Object s : strList){
//					preList.add(initData.get(s));
//				}
				newList.add(preList);
				
				//循环期间再组合数据
				String[] str = null;
//				for(int i=0;i<periodList.size();i++){
//					HashMap periodMap = (HashMap)periodList.get(i);
//					
//					//此会计期间的凭证明细
//					List dealList = (ArrayList)allDetDataMap.get("detail_"+periodMap.get("AccYear")+"_"+periodMap.get("AccPeriod"));
//					if(dealList != null && dealList.size()>0){
//						str = new String[]{"AccDate","credTypeIdOrderNo","RecordComment","DebitAmount","LendAmount","isflag","sumAmount"};
//						//存在明细
//						for(HashMap detailMap : (ArrayList<HashMap>)dealList){
//							preList = new ArrayList();
//							for(String s : str){
//								preList.add(detailMap.get(s));
//							}
//							//处理借贷方金额
//							for(int l =0;l<setAccNumberList.size();l++){
//								String setAccNumber = String.valueOf(setAccNumberList.get(l));
//								String[] setjdflagOrAccNumber = setAccNumber.split(";");
//								if("1".equals(setjdflagOrAccNumber[0])){
//									//借方
//									preList.add(detailMap.get("curr__"+setjdflagOrAccNumber[1]+"_DebitAmount"));
//								}else{
//									//贷方
//									preList.add(detailMap.get("curr__"+setjdflagOrAccNumber[1]+"_LendAmount"));
//								}
//							}
//							newList.add(preList);
//						}
//					}
//					
//					//处理本期合计金额,和本年累计金额
//					String[] strName = new String[]{"period","CredYear"};
//					for(int k =0;k<strName.length;k++){
//						HashMap dealMap = (HashMap)allDetDataMap.get(strName[k]+"_"+periodMap.get("AccYear")+"_"+periodMap.get("AccPeriod"));
//						if(dealMap != null && dealMap.size()>0){
//							str = new String[]{"periodEnd","","RecordComment","totalsumDebitAmount","totalsumLendAmount","totalBalanceisflag","totalBalance"};
//							//存在明细
//							preList = new ArrayList();
//							for(String s : str){
//								if("RecordComment".equals(s)){
//									if(k == 0){
//										preList.add("本期合计");
//									}else{
//										preList.add("本年累计");
//									}
//								}else{
//									preList.add(dealMap.get(s));
//								}
//							}
//							//处理借贷方金额
//							for(int l =0;l<setAccNumberList.size();l++){
//								String setAccNumber = String.valueOf(setAccNumberList.get(l));
//								String[] setjdflagOrAccNumber = setAccNumber.split(";");
//								if("1".equals(setjdflagOrAccNumber[0])){
//									//借方
//									preList.add(dealMap.get("curr__"+setjdflagOrAccNumber[1]+"_sumDebitAmount"));
//								}else{
//									//贷方
//									preList.add(dealMap.get("curr__"+setjdflagOrAccNumber[1]+"_sumLendAmount"));
//								}
//							}
//							newList.add(preList);
//						}
//					}
//				}
				
				rs = mgt.exportBanlance(fos, "多栏式明细账",titleName, strTitle, newList);
				fos.close();
				if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("多栏式明细账.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
							getMessage(request, "com.export.success.over")+fileName)
							.setBackUrl("/FinanceReportAction.do?optype=ReporttblAccAllDet&comeMode=1&"+conditions).setNotAutoBack()
							.setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
			}
			
		}
		return getForward(request, mapping, "reportAccAllDetList");
	}
	
	
	/**
	 * 多栏式明细账设计列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportDesignList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		/* 查询所有多栏式明细账列表数据 */
		String keyWord = request.getParameter("keyWord");				//关键字搜索
		Result rs = mgt.queryAccDesign(keyWord);
		request.setAttribute("keyWord", keyWord);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			String folderTree = "[";
			ArrayList list = (ArrayList)rs.retVal;
			for(int i=0;i<list.size();i++){
				Object[] o = (Object[])list.get(i);
				folderTree += "{ id:\""+o[0]+"\", pId:0, name:\""+o[1]+"\"}";
				if(i<list.size()-1){
					folderTree+=",";
				}
			}
			folderTree += "]";
			request.setAttribute("data", folderTree);
		}
		return getForward(request, mapping, "reportDesignList");
	}
	
	
	/**
	 * 多栏式明细账设计界面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportDesign(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String dealType = request.getParameter("dealType");			//设计界面处理类型（updatePre 修改前，addPre 添加前）
		
		/* 查询所有币种数据 */
		Result rs = mgt.queryCurrencyAll();
		request.setAttribute("currencyList", rs.retVal);
		
		request.setAttribute("dealType", dealType);
		String id = request.getParameter("id");
		if(dealType != null && "addPre".equals(dealType)){
			//添加前
			return getForward(request, mapping, "reportDesign");
		}else if(dealType != null && "updatePre".equals(dealType)){
			//修改前 先查询数据
			rs = mgt.queryDesignById(id);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				//查询成功
				request.setAttribute("designBean", rs.retVal);
			}
			return getForward(request, mapping, "reportDesign");
		}
		
		//其它操作（添加，修改）
		String code = request.getParameter("accType");						//会计科目编号
		String name = request.getParameter("accName");						//名称
		String[] jdflag = request.getParameterValues("td_jdflag");			//借贷方向数组
		String[] td_acctype = request.getParameterValues("td_acctype");		
		String[] td_accName = request.getParameterValues("td_accName");
		
		String[] currency_id = request.getParameterValues("currency_id");			//币种
		String levelsetting = request.getParameter("levelsetting");					//按级别设置多栏账
		String levelvalue = request.getParameter("levelvalue");						//级别值
		String columndata = "";
		String currencydata = "";
		if(td_acctype!=null){
			for(int i=0;i<td_acctype.length;i++){
				String acctypevalue = td_acctype[i];
				if(acctypevalue==null || "".equals(acctypevalue)){
					//不存在编号时删除该条记录
					continue;
				}
				columndata = columndata+jdflag[i]+";"+acctypevalue+";"+td_accName[i]+";//";
			}
		}
		if(currency_id!=null){
			for(int i=0;i<currency_id.length;i++){
				currencydata += currency_id[i];
				if(i<currency_id.length-1){
					currencydata += ",";
				}
			}
		}
		if(levelsetting==null){
			levelsetting = "0";
		}
		rs = mgt.addOrUpdateDesign(id, code, name, columndata, currencydata, levelsetting, levelvalue);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("dealAsyn", "true");
			String msg = "common.msg.addSuccess";
			if(id != null && !"".equals(id)){
				msg = "common.msg.updateSuccess";
			}
			EchoMessage.success().add(getMessage(
                    request, msg)).setBackUrl("/FinanceReportAction.do?optype=ReporttblAccAllDet&type=designList").
                    setAlertRequest(request);
		}else{
			//添加/修改失败
			String msg = "common.msg.addFailture";
			if(id != null && !"".equals(id)){
				msg = "common.msg.updateErro";
			}
			EchoMessage.error().add(getMessage(request, msg)).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * 删除多栏式明细账设计记录
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward delDesign(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String id = request.getParameter("id");						//设计记录Id
		Result rs = mgt.delAccDesign(id);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			if(Integer.valueOf(rs.retVal.toString())>0){
				request.setAttribute("msg", "ok");
			}
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 查询用户自定义的设计数据
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryDesign(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		Result rs = mgt.queryAccDesign("");
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			List list = (ArrayList)rs.retVal;
			String msg = "";
			for(int i=0;i<list.size();i++){
				String[] str = (String[])list.get(i);
				msg += "<option value='"+str[0]+"'>"+str[1]+"</option>";
			}
			request.setAttribute("msg", msg);
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * ajax调用数据
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward ajaxBearData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String dealType = request.getParameter("dealType");
		if(dealType != null && "childAccType".equals(dealType)){
			//获取最下级会计科目
			String accType = request.getParameter("accType");
			String level = request.getParameter("level");
			String itemSort = request.getParameter("itemSort");
			Result rs = mgt.queryAccTypeChild(accType,level,itemSort);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				//查询成功
				List list = (ArrayList)rs.retVal;
				String strJson = "{\"accData\":[";
				for(int i=0;i<list.size();i++){
					String[] str = (String[])list.get(i);
					strJson += "{\"AccNumber\":\""+str[0]+"\",";
					strJson += "\"AccName\":\""+str[1]+"\",";
					strJson += "\"jdFlag\":\""+str[2]+"\"}";
					if(i<list.size()-1){
						strJson += ",";
					}
				}
				strJson += "]}";
				request.setAttribute("msg", strJson);
			}
		}
		return getForward(request, mapping, "blank");
	}
	
	
	/**
	 * 根据会计科目AccNumber查询科目信息（包括是否核算部门，职员，仓库，客户，供应商等）
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryAccIsItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String accNumber = request.getParameter("accNumber");
		Result rs = mgt.queryAccTypeInfoItem(accNumber);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			HashMap map = (HashMap)rs.getRetVal();
			String strJson = "{\"IsDept\":\""+map.get("IsDept")+"\",";
			strJson += "\"IsPersonal\":\""+map.get("IsPersonal")+"\",";
			strJson += "\"IsClient\":\""+map.get("IsClient")+"\",";
			strJson += "\"IsProject\":\""+map.get("IsProject")+"\",";
			strJson += "\"IsProvider\":\""+map.get("IsProvider")+"\",";
			strJson += "\"isStock\":\""+map.get("isStock")+"\"}";
			request.setAttribute("msg", strJson);
		}
		return getForward(request, mapping, "blank");
	}
	
	
	/**
	 * 核算项目分类总账首页
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reporttblAccCalculateIndex(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		/* 条件搜索 */
		String periodYearStart = request.getParameter("periodYearStart");				//会计期间开始年
		String periodStart = request.getParameter("periodStart");						//会计期间开始
		String periodYearEnd = request.getParameter("periodYearEnd");					//会计期间结束年
		String periodEnd = request.getParameter("periodEnd");							//会计期间结束
		String itemSort = request.getParameter("itemSort");								//项目类别
		String itemCodeStart = request.getParameter("itemCodeStart");					//项目代码开始
		String itemCodeEnd = request.getParameter("itemCodeEnd");						//项目代码结束
		String accCodeStart = request.getParameter("accCodeStart");						//会计科目开始
		String accCodeEnd = request.getParameter("accCodeEnd");							//会计科目结束
		String accCodeLevel = request.getParameter("accCodeLevel");						//科目级次
		String currencyName = request.getParameter("currencyName");						//币别
		String accTypeNo = request.getParameter("accTypeNo");							//科目无发生额不显示
		String takeBrowNo = request.getParameter("takeBrowNo");							//无发生额不显示
		String binderNo = request.getParameter("binderNo");								//包括未过账凭证
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//显示禁用科目
		
		String keyWord = request.getParameter("keyWord");								//关键字查询
		String oldCode = request.getParameter("oldCode");								//旧的编号
		
		
		/* 查询所有币别 */
		Result result = mgt.queryCurrencyAll();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("currencyList", result.retVal);
		}

		//凭证设置
		result = new VoucherMgt().queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		
		/* 会计期间 */
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		
		//查询开账期间，页面输入会计期间时进行判断处理
		result = mgt.getCurrentlyPeriod();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("beginPeriod", result.retVal);
		}
		
		if(comeMode == null){
			/* 第一次进入时不进行查询，弹出搜索条件 */
			return getForward(request, mapping, "reporttblAccCalculateIndex");
 		}
		
		/* 把搜索条件保存到Map中 */
		Map<String, String> conMap = new HashMap<String, String>();
		conMap.put("periodYearStart", periodYearStart);
		conMap.put("periodStart", periodStart);
		conMap.put("periodYearEnd", periodYearEnd);
		conMap.put("periodEnd", periodEnd);
		conMap.put("itemSort", itemSort);
		conMap.put("itemCodeStart", itemCodeStart);
		conMap.put("itemCodeEnd", itemCodeEnd);
		conMap.put("accCodeStart", accCodeStart);
		conMap.put("accCodeEnd", accCodeEnd);
		conMap.put("accCodeLevel", accCodeLevel);
		conMap.put("currencyName", currencyName);
		conMap.put("accTypeNo", accTypeNo);
		conMap.put("takeBrowNo", takeBrowNo);
		conMap.put("binderNo", binderNo);
		conMap.put("showBanAccTypeInfo", showBanAccTypeInfo);
		conMap.put("keyWord", keyWord);
		conMap.put("oldCode", oldCode);

		request.setAttribute("conMap", conMap);
		/* 搜索条件拼装成String */
		String conditions = "";
		Iterator iterator = conMap.keySet().iterator();
		while(iterator.hasNext()) {
			Object key = iterator.next();
			if(conMap.get(key) != null){
				conditions += key+"="+conMap.get(key)+"&";
			}
		}
		if(conditions.length()>0){
			conditions.substring(0,conditions.length()-1);
		}
		request.setAttribute("conditions", conditions);
		
		/* 查询满足条件的核算项目 */
		Result rs = mgt.quertItemDate((HashMap)conMap);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			List list = (ArrayList)rs.retVal;
			StringBuffer folderTree = new StringBuffer("[");
			for(int i=0;i<list.size();i++){
				HashMap map = (HashMap)list.get(i);
				String classCode = String.valueOf(map.get("classCode"));
				folderTree.append("{nocheck:true,classCode:\"" + classCode +"\",name:\"" + String.valueOf(map.get("AccNumber"))+" - "+replace(String.valueOf(map.get("AccName")))+"\"");
				if(classCode.length()>5 && !"EmployeeID".equals(itemSort)){
					folderTree.append(",pClassCode:\"" + classCode.substring(0,classCode.length()-5)+"\"");
				}
				folderTree.append("}");
				if(i<list.size()-1){
					folderTree.append(",");
				}
			}
			folderTree.append("]");
			request.setAttribute("folderTree", folderTree.toString());
		}
		return getForward(request, mapping, "reporttblAccCalculateIndex");
	}
	
	/**
	 * 核算项目分类总账点击核算项目加载详情
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reporttblAccCalculateDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String classCode = request.getParameter("classCode");
		if(classCode == null || "".equals(classCode)){
			//不存在核算项目组
			request.setAttribute("comeMode", "");
			return getForward(request, mapping, "reporttblAccCalculateDetail");
		}
		
		/* 条件搜索 */
		String periodYearStart = request.getParameter("periodYearStart");				//会计期间开始年
		String periodStart = request.getParameter("periodStart");						//会计期间开始
		String periodYearEnd = request.getParameter("periodYearEnd");					//会计期间结束年
		String periodEnd = request.getParameter("periodEnd");							//会计期间结束
		String itemSort = request.getParameter("itemSort");								//项目类别
		String itemCodeStart = request.getParameter("itemCodeStart");					//项目代码开始
		String itemCodeEnd = request.getParameter("itemCodeEnd");						//项目代码结束
		String accCodeStart = request.getParameter("accCodeStart");						//会计科目开始
		String accCodeEnd = request.getParameter("accCodeEnd");							//会计科目结束
		String accCodeLevel = request.getParameter("accCodeLevel");						//科目级次
		String currencyName = request.getParameter("currencyName");						//币别
		String accTypeNo = request.getParameter("accTypeNo");							//科目无发生额不显示
		String takeBrowNo = request.getParameter("takeBrowNo");							//无发生额不显示
		String binderNo = request.getParameter("binderNo");								//包括未过账凭证
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//显示禁用科目
		
		/* 把搜索条件保存到Map中 */
		Map<String, String> conMap = new HashMap<String, String>();
		conMap.put("periodYearStart", periodYearStart);
		conMap.put("periodStart", periodStart);
		conMap.put("periodYearEnd", periodYearEnd);
		conMap.put("periodEnd", periodEnd);
		conMap.put("itemSort", itemSort);
		conMap.put("itemCodeStart", itemCodeStart);
		conMap.put("itemCodeEnd", itemCodeEnd);
		conMap.put("accCodeStart", accCodeStart);
		conMap.put("accCodeEnd", accCodeEnd);
		conMap.put("accCodeLevel", accCodeLevel);
		conMap.put("currencyName", currencyName);
		conMap.put("accTypeNo", accTypeNo);
		conMap.put("takeBrowNo", takeBrowNo);
		conMap.put("binderNo", binderNo);
		conMap.put("showBanAccTypeInfo", showBanAccTypeInfo);
		request.setAttribute("conMap", conMap);
		
		String conditions = dealConditions(conMap);
		
		LoginBean loginBean = this.getLoginBean(request);
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/FinanceReportAction.do?optype=ReporttblAccCalculate");
		
		/**
		 * 查询核算项目分类总账明细
		 */
		long time1 = System.currentTimeMillis();
		Result rs = mgt.queryAccCalculateDetail((HashMap<String, String>)conMap,classCode,mop,loginBean);
		long time2 = System.currentTimeMillis();
		System.out.println("核算项目分类总账详情后台执行时间："+(time2-time1));
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			/* 查询成功 */
			Object[] obj = (Object[])rs.retVal;
			
			List accDetList = (ArrayList)obj[0];		//会计科目和各种期间
			HashMap valuesMap = (HashMap)obj[1];		//保存的数据
			String currType = String.valueOf(obj[2]);	//外币类型
			
			request.setAttribute("accDetList", accDetList);
			request.setAttribute("accDetHash", valuesMap);
			request.setAttribute("currType", currType);
			
			
			//保存组装好的所有的数据
			List newList = new ArrayList();
			List perList = new ArrayList();
			/**
			 * 数据组装
			 */
			String[] strs = new String[]{"isInitflag","initAmount","sumDebitAmount","sumLendAmount","sumYearDebitAmount","sumYearLendAmount","isflag","PeriodBalaBase"};
			for(int i=0;i<accDetList.size();i++){
				HashMap accDetMap = (HashMap)accDetList.get(i);
				List periodList = (ArrayList)accDetMap.get("period");
				if(periodList != null && periodList.size()>0){
					for(int j=0;j<periodList.size();j++){
						HashMap periodMap = (HashMap)periodList.get(j);
						perList = new ArrayList();
						perList.add(periodMap.get("AccYear")+"."+periodMap.get("AccPeriod"));
						if(j == 0){
							perList.add(accDetMap.get("AccNumber"));
							perList.add(accDetMap.get("AccFullName"));
						}else{
							perList.add("");perList.add("");
						}
						
						//获取金额数据（key=classCode_year_period）
						HashMap values = (HashMap)valuesMap.get(accDetMap.get("classCode")+"_"+periodMap.get("AccYear")+"_"+periodMap.get("AccPeriod"));
						if(values == null){
							values = new HashMap();
						}
						//循环附金额数据到list中
						for(String s : strs){
							perList.add(values.get(s));
						}
						newList.add(perList);
					}
				}
			}
			
			/**
			 * 导出列表处理
			 */
			String dealtype = request.getParameter("dealtype");
			if(dealtype != null && "exportList".equals(dealtype)){
				
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath()+"\\核算项目分类总账.xls" ;
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				//设置标题
				String titleName = "项目类别：";
				if("DepartmentCode".equals(itemSort)){
					titleName += "部门";
				}else if("EmployeeID".equals(itemSort)){
					titleName += "职员";
				}else if("StockCode".equals(itemSort)){
					titleName += "仓库";
				}else if("ClientCode".equals(itemSort)){
					titleName += "客户";
				}else if("SuplierCode".equals(itemSort)){
					titleName += "供应商";
				}else if("ProjectCode".equals(itemSort)){
					titleName += "项目";
				}
				titleName = "   期间："+periodYearStart+"年第"+periodStart+"期至"+periodYearEnd+"年第"+periodEnd+"期";
				
				String locale =  this.getLocale(request).toString();
				List strTitle = new ArrayList();
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccMain.Period", locale)+";lastY:1"));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccNumber", locale)+";lastY:1"));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccName", locale)+";lastY:1"));
				strTitle.add(setExportMap("name:期初余额;lastX:1"));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.LendAmount", locale)));
				strTitle.add(setExportMap("name:本年累计借方"));
				strTitle.add(setExportMap("name:本年累计贷方"));
				strTitle.add(setExportMap("name:期末余额;lastX:1"));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.JdFlag", locale)+";nextLine:1;nextX:3"));
				strTitle.add(setExportMap("name:余额"));
				for(int i=0;i<4;i++){
					strTitle.add(setExportMap("name:金额"));
				}
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.JdFlag", locale)));
				strTitle.add(setExportMap("name:金额"));
				rs = mgt.exportBanlance(fos, "核算项目分类总账",titleName, strTitle, newList);
				fos.close();
				if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("核算项目分类总账.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
							getMessage(request, "com.export.success.over")+fileName)
							.setBackUrl("/FinanceReportAction.do?optype=ReporttblAccCalculate&comeMode=1&"+conditions).setNotAutoBack()
							.setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
			}else{
				String[] str = new String[]{"Period","AccNumber","AccFullName","initIsFlag","initAmount","DebitAmount","LendAmount","yearDebitAmount","yearLendAmount","isflag","PeriodDCBalaBase"};
				request.getSession().setAttribute("accBalanceData", dealNewList(str, newList));
			}
		}else{
			/* 查询失败 */
			EchoMessage.error().add(getMessage(
                    request, "sms.query.error")).
                    setBackUrl("/FinanceReportAction.do?optype=ReporttblAccCalculate&type=detail").
                    setAlertRequest(request);
		}
		request.setAttribute("comeMode", "comeMode");
		return getForward(request, mapping, "reporttblAccCalculateDetail");
	}
	
	/**
	 * 核算项目明细账首页
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reporttblAccCalculateDetIndex(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		/* 条件搜索 */
		String dateType = request.getParameter("dateType");								//时间类型（按期间查询，按日期查询）
		String periodYearStart = request.getParameter("periodYearStart");				//会计期间开始年
		String periodStart = request.getParameter("periodStart");						//会计期间开始
		String periodYearEnd = request.getParameter("periodYearEnd");					//会计期间结束年
		String periodEnd = request.getParameter("periodEnd");							//会计期间结束
		String dateStart = request.getParameter("dateStart");							//日期开始
		String dateEnd = request.getParameter("dateEnd");								//日期结束
		String accCode = request.getParameter("accCode");								//会计科目
		String currencyName = request.getParameter("currencyName");						//币别
		String itemSort = request.getParameter("itemSort");								//项目类别
		String itemCodeStart = request.getParameter("itemCodeStart");					//项目代码开始
		String itemCodeEnd = request.getParameter("itemCodeEnd");						//项目代码结束
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//显示禁用科目
		String accTypeNo = request.getParameter("accTypeNo");							//科目无发生额不显示
		String takeBrowNo = request.getParameter("takeBrowNo");							//无发生额不显示
		String binderNo = request.getParameter("binderNo");								//包括未过账凭证
		
		String keyWord = request.getParameter("keyWord");								//关键字查询
		String oldCode = request.getParameter("oldCode");								//旧的编号
		
		
		/* 查询所有币别 */
		Result result = mgt.queryCurrencyAll();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("currencyList", result.retVal);
		}
		
		//凭证设置
		result = new VoucherMgt().queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		
		/* 会计期间 */
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);

		if(comeMode == null){
			/* 第一次进入时不进行查询，弹出搜索条件 */
			return getForward(request, mapping, "reporttblAccCalculateDetIndex");
 		}
		
		/* 把搜索条件保存到Map中 */
		Map<String, String> conMap = new HashMap<String, String>();
		conMap.put("dateType", dateType);
		conMap.put("periodYearStart", periodYearStart);
		conMap.put("periodStart", periodStart);
		conMap.put("periodYearEnd", periodYearEnd);
		conMap.put("periodEnd", periodEnd);
		conMap.put("dateStart", dateStart);
		conMap.put("dateEnd", dateEnd);
		conMap.put("accCode", accCode);
		conMap.put("currencyName", currencyName);
		conMap.put("itemSort", itemSort);
		conMap.put("itemCodeStart", itemCodeStart);
		conMap.put("itemCodeEnd", itemCodeEnd);
		conMap.put("showBanAccTypeInfo", showBanAccTypeInfo);
		conMap.put("accTypeNo", accTypeNo);
		conMap.put("takeBrowNo", takeBrowNo);
		conMap.put("binderNo", binderNo);
		conMap.put("keyWord", keyWord);
		conMap.put("oldCode", oldCode);

		request.setAttribute("conMap", conMap);
		/* 搜索条件拼装成String */
		String conditions = "";
		Iterator iterator = conMap.keySet().iterator();
		while(iterator.hasNext()) {
			Object key = iterator.next();
			if(conMap.get(key) != null){
				conditions += key+"="+conMap.get(key)+"&";
			}
		}
		if(conditions.length()>0){
			conditions.substring(0,conditions.length()-1);
		}
		//保存搜索条件
		request.setAttribute("conditions", conditions);
		
		/* 查询满足条件的核算项目 */
		Result rs = mgt.quertItemDate((HashMap<String,String>)conMap);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			List list = (ArrayList)rs.retVal;
			StringBuffer folderTree = new StringBuffer("[");
			for(int i=0;i<list.size();i++){
				HashMap map = (HashMap)list.get(i);
				String classCode = String.valueOf(map.get("classCode"));
				folderTree.append("{nocheck:true,classCode:\"" + classCode +"\",name:\"" + String.valueOf(map.get("AccNumber"))+" - "+replace(String.valueOf(map.get("AccName")))+"\"");
				if(classCode.length()>5 && !"EmployeeID".equals(itemSort)){
					folderTree.append(",pClassCode:\"" + classCode.substring(0,classCode.length()-5)+"\"");
				}
				folderTree.append("}");
				if(i<list.size()-1){
					folderTree.append(",");
				}
			}
			folderTree.append("]");
			request.setAttribute("folderTree", folderTree.toString());
			
			rs = mgt.queryAccType(accCode);
			request.setAttribute("acctype", rs.retVal);
		}
		return getForward(request, mapping, "reporttblAccCalculateDetIndex");
	}
	
	/**
	 * 核算项目明细账详情
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reporttblAccCalculateDetDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String classCode = request.getParameter("classCode");
		if(classCode == null || "".equals(classCode)){
			//不存在核算项目组
			request.setAttribute("comeMode", "");
			return getForward(request, mapping, "reporttblAccCalculateDetDetail");
		}
		
		/* 条件搜索 */
		String dateType = request.getParameter("dateType");								//时间类型（按期间查询，按日期查询）
		String periodYearStart = request.getParameter("periodYearStart");				//会计期间开始年
		String periodStart = request.getParameter("periodStart");						//会计期间开始
		String periodYearEnd = request.getParameter("periodYearEnd");					//会计期间结束年
		String periodEnd = request.getParameter("periodEnd");							//会计期间结束
		String dateStart = request.getParameter("dateStart");							//日期开始
		String dateEnd = request.getParameter("dateEnd");								//日期结束
		String accCode = request.getParameter("accCode");								//会计科目
		String currencyName = request.getParameter("currencyName");						//币别
		String itemSort = request.getParameter("itemSort");								//项目类别
		String itemCodeStart = request.getParameter("itemCodeStart");					//项目代码开始
		String itemCodeEnd = request.getParameter("itemCodeEnd");						//项目代码结束
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//显示禁用科目
		String accTypeNo = request.getParameter("accTypeNo");							//科目无发生额不显示
		String takeBrowNo = request.getParameter("takeBrowNo");							//无发生额不显示
		String binderNo = request.getParameter("binderNo");								//包括未过账凭证
		
		/* 把搜索条件保存到Map中 */
		Map<String, String> conMap = new HashMap<String, String>();
		conMap.put("dateType", dateType);
		conMap.put("periodYearStart", periodYearStart);
		conMap.put("periodStart", periodStart);
		conMap.put("periodYearEnd", periodYearEnd);
		conMap.put("periodEnd", periodEnd);
		conMap.put("dateStart", dateStart);
		conMap.put("dateEnd", dateEnd);
		conMap.put("accCode", accCode);
		conMap.put("currencyName", currencyName);
		conMap.put("itemSort", itemSort);
		conMap.put("itemCodeStart", itemCodeStart);
		conMap.put("itemCodeEnd", itemCodeEnd);
		conMap.put("showBanAccTypeInfo", showBanAccTypeInfo);
		conMap.put("accTypeNo", accTypeNo);
		conMap.put("takeBrowNo", takeBrowNo);
		conMap.put("binderNo", binderNo);
		request.setAttribute("conMap", conMap);
		
		String conditions = dealConditions(conMap);
		
		LoginBean loginBean = this.getLoginBean(request);
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/FinanceReportAction.do?optype=ReporttblAccCalculateDet");
		
		/**
		 * 查询核算项目明细账详情
		 */
		long time1 = System.currentTimeMillis();
		Result rs = mgt.queryAccCalculateDetDetail((HashMap<String, String>)conMap,classCode,mop,loginBean);
		long time2 = System.currentTimeMillis();
		System.out.println("核算项目明细账详情后台执行时间："+(time2-time1));
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			/* 查询成功 */
			Object[] o = (Object[])rs.retVal;
			List periodList = (ArrayList)o[0];
			HashMap detailData = (HashMap)o[1];
			request.setAttribute("periodList", periodList);
			request.setAttribute("detailData", detailData);
			if(currencyName!=null && !"".equals(currencyName)){
				//验证是否是本位币
				if("all".equals(currencyName)){
					/* 查询所有币种 */
					rs = mgt.queryCurrencyAll();
					if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
						request.setAttribute("currencyList", rs.retVal);
					}
					request.setAttribute("isBase", "all");
				}else{
					rs = mgt.queryIsBaseCurrency(currencyName);
					request.setAttribute("isBase", rs.retVal);
				}
			}
			//查询搜索的会计科目
			rs = mgt.queryAccType(accCode);
			String[] acctypeStr = (String[])rs.retVal;
			
			List newList = new ArrayList();
			
			/**
			 * 进行数据处理把数据存储到newList中
			 */
			/**
			 * 获取期初金额
			 */
			
			
			//期初金额
			HashMap initMap = (HashMap)detailData.get("initMap");
			List preList = new ArrayList();
			preList.add("");
			preList.add(initMap.get("accYear")+"."+initMap.get("accPeriod"));
			preList.add("");
			preList.add("期初余额");
			preList.add("");
			preList.add("");
			preList.add(initMap.get("isInitflag"));
			preList.add(initMap.get("sumAmount"));
			newList.add(preList);
			
			//处理明细和本期合计 本年累计
			for(int i=0;i<periodList.size();i++){
				HashMap periodMap = (HashMap)periodList.get(i);
				String[] str = new String[]{"AccDate","period","credTypeIdOrderNo","RecordComment","DebitAmount","LendAmount","isflag","sumAmount"};
				HashMap curMap = (HashMap)detailData.get(periodMap.get("AccYear")+"_"+periodMap.get("AccPeriod"));
				List detailMain = (ArrayList)curMap.get("accMainList");
				preList = new ArrayList();
				if(detailMain != null && detailMain.size()>0){
					for(int j =0;j<detailMain.size();j++){
						HashMap detail = (HashMap)detailMain.get(j);
						preList = new ArrayList();
						for(String s : str){
							if("period".equals(s)){
								preList.add(detail.get("CredYear")+"."+detail.get("Period"));
							}else{
								preList.add(detail.get(s));
							}
						}
						newList.add(preList);							//添加明细
					}
				}
				
				//本期合计
				preList = new ArrayList();
				preList.add("");
				preList.add(periodMap.get("AccYear")+"."+periodMap.get("AccPeriod"));
				preList.add("");preList.add("本期合计");
				str = new String[]{"sumDebitAmount","sumLendAmount","isflag","sumDCBala"};
				for(String s : str){
					preList.add(curMap.get(s));
				}
				newList.add(preList);							//添加本期合计
				
				//本年累计
				preList = new ArrayList();
				preList.add("");
				preList.add(periodMap.get("AccYear")+"."+periodMap.get("AccPeriod"));
				preList.add("");preList.add("本年累计");
				str = new String[]{"yearsumDebitAmount","yearsumLendAmount","yearisflag","yearsumDCBala"};
				for(String s : str){
					preList.add(curMap.get(s));
				}
				newList.add(preList);							//添加本年累计
			}
			
			/**
			 * 业务处理(导出列表数据)
			 */
			String dealtype = request.getParameter("dealtype");
			if(dealtype!= null && "exportList".equals(dealtype)){
				//导出总分类账数据
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath()+"\\核算项目明细账.xls" ;
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				//设置标题
				String titleName = "会计科目："+acctypeStr[0]+" - "+acctypeStr[1]+"  项目类别：";
				if("DepartmentCode".equals(itemSort)){
					titleName += "部门";
				}else if("EmployeeID".equals(itemSort)){
					titleName += "职员";
				}else if("StockCode".equals(itemSort)){
					titleName += "仓库";
				}else if("ClientCode".equals(itemSort)){
					titleName += "客户";
				}else if("SuplierCode".equals(itemSort)){
					titleName += "供应商";
				}else if("ProjectCode".equals(itemSort)){
					titleName += "项目";
				}
				if(dateType != null && "1".equals(dateType)){
					//期间查询
					titleName += "   期间："+periodYearStart+"年第"+periodStart+"期至"+periodYearEnd+"年第"+periodEnd+"期";
				}else if(dateType != null && "2".equals(dateType)){
					//日期查询
					titleName += "   日期："+dateStart+"至"+dateEnd;
				}

				String locale =  this.getLocale(request).toString();
				
				//设置列头
				List strTitle = new ArrayList();
				strTitle.add(setExportMap("name:日期"));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccMain.Period", locale)));
				strTitle.add(setExportMap("name:凭证字号"));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.RecordComment", locale)));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.LendAmount", locale)));
				strTitle.add(setExportMap("name:余额;lastX:1"));
				rs = mgt.exportBanlance(fos, "核算项目明细账",titleName, strTitle, newList);
				fos.close();
				if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("核算项目明细账.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
							getMessage(request, "com.export.success.over")+fileName)
							.setBackUrl("/FinanceReportAction.do?optype=ReporttblAccCalculateDet&comeMode=1&"+conditions).setNotAutoBack()
							.setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
			}else{
				String[] str = new String[]{"BillDate","Period","credTypeidOrderNo","RecordComment","DebitAmount","LendAmount","isflag","PeriodDCBalaBase"};
				request.getSession().setAttribute("accBalanceData", dealNewList(str, newList));
			}
		}else{
			/* 查询失败 */
			EchoMessage.error().add(getMessage(
                    request, "sms.query.error")).
                    setBackUrl("/FinanceReportAction.do?optype=ReporttblAccCalculateDet&type=detail").
                    setAlertRequest(request);
		}
		request.setAttribute("comeMode", "comeMode");
		return getForward(request, mapping, "reporttblAccCalculateDetDetail");
	}
	
	/**
	 * 试算平衡表查询
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reporttblTrialBalance(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		/* 条件搜索 */
		String periodYearStart = request.getParameter("periodYearStart");				//会计期间年
		String periodStart = request.getParameter("periodStart");						//会计期间
		String levelStart = request.getParameter("levelStart");							//科目级次
		String currencyName = request.getParameter("currencyName");						//币别
		String showDetail = request.getParameter("showDetail");							//只显示明细
		String showItemDetail = request.getParameter("showItemDetail");					//显示核算项目明细
		String binderNo = request.getParameter("binderNo");								//包括未过账凭证
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//显示禁用科目
		String accTypeNoItem = request.getParameter("accTypeNoItem");					//科目不记录不显示
		
		/* 查询所有币别 */
		List currencyList = new ArrayList();
		Result result = mgt.queryCurrencyAll();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			currencyList = (ArrayList)result.retVal;
			request.setAttribute("currencyList", currencyList);
		}

		//凭证设置
		result = new VoucherMgt().queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		
		
		/* 得到会计期间 */
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		
		//查询开账期间，页面输入会计期间时进行判断处理
		result = mgt.getCurrentlyPeriod();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("beginPeriod", result.retVal);
		}
		
		if(comeMode == null){
			/* 第一次进入时不进行查询，弹出搜索条件 */
			return getForward(request, mapping, "reporttblTrialBalance");
 		}
		
		/* 把搜索条件保存到Map中 */
		Map<String, String> conMap = new HashMap<String, String>();
		conMap.put("periodYearStart", periodYearStart);
		conMap.put("periodStart", periodStart);
		conMap.put("levelStart", levelStart);
		conMap.put("currencyName", currencyName);
		conMap.put("showDetail", showDetail);
		conMap.put("showItemDetail", showItemDetail);
		conMap.put("binderNo", binderNo);
		conMap.put("showBanAccTypeInfo", showBanAccTypeInfo);
		conMap.put("accTypeNoItem", accTypeNoItem);
		
		request.setAttribute("conMap", conMap);
		
		String conditions = dealConditions(conMap);
		
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/FinanceReportAction.do?optype=ReporttblTrialBalance");
		
		/* 查询数据 */
		long time1 = System.currentTimeMillis();
		result = mgt.accTrialBalance((HashMap<String, String>)conMap,mop,lg);
		long time2 = System.currentTimeMillis();
		System.out.println("试算平衡表查询数据执行时间："+(time2-time1));
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			Object[] obj = (Object[])result.retVal;
			List list = (ArrayList)obj[0];
			HashMap totalMap = (HashMap)obj[1];
			String currType = String.valueOf(obj[2]);
			request.setAttribute("accTypeInfoList", list);
			request.setAttribute("totalData", obj[1]);
			request.setAttribute("currType", currType);
			
			//设置标题
			String titleName = "期间："+periodYearStart+"年第"+periodStart+"期";
			if("true".equals(totalMap.get("flag"))){
				titleName += "      试算结果平衡";
			}else{
				titleName += "      试算结果不平衡";
			}
			titleName += "          币别：";
			/**
			 * 把数据保存到newList中
			 */
			int lastY = 1;
			int lastX = 1;
			int lastX1 = 0;
			String currFlag = "";
			List newList = new ArrayList();
			String lineStr = "AccNumber;AccFullName;";
			String printStr = "No;AccNumber;AccFullName;";
			if("".equals(currType) || "isBase".equals(currType)){
				//本位币或者综合本位币
				lineStr += "PeriodIniDebitBase;PeriodIniCreditBase;PeriodDebitSumBase;PeriodCreditSumBase;PeriodDebitBalaBase;PeriodCreditBalaBase";
				currFlag = "isBase";
				if("".equals(currType)){
					titleName += "<综合本位币>";
				}else{
					result = mgt.queryCurrencyName(currencyName);
					titleName += result.retVal;
				}
				printStr = "No;"+lineStr;
			}else if("all".equals(currType)){
				//所有币别多栏式
				String[] cur1 = new String[]{"PeriodIniDebit","PeriodIniCredit","PeriodDebitSum","PeriodCreditSum","PeriodDebitBala","PeriodCreditBala"};
				String[] cur2 = new String[]{"PeriodIniDebitBase","PeriodIniCreditBase","PeriodDebitSumBase","PeriodCreditSumBase","PeriodDebitBalaBase","PeriodCreditBalaBase"};
				for(int k = 0;k<cur1.length;k++){
					for(int l =0;l<currencyList.size();l++){
						String[] currStr = (String[])currencyList.get(l);
						String currency = currStr[0];
						if("true".equals(currStr[2])){
							//本位币
							currency = "";
						}
						lineStr += cur1[k]+"_"+currency+";";
						printStr += cur1[k]+"_"+currStr[3]+";";
					}
					lineStr += cur2[k]+";";
					printStr += cur2[k]+";";
				}
				lastY = 2;
				lastX = 2*(currencyList.size())+1;
				lastX1 = currencyList.size();
				currFlag = "all";
				titleName += "<所有币别多栏式>";
			}else{
				//外币
				lineStr += "PeriodIniDebit;PeriodIniDebitBase;PeriodIniCredit;PeriodIniCreditBase;PeriodDebitSum;PeriodDebitSumBase;PeriodCreditSum;PeriodCreditSumBase;PeriodDebitBala;PeriodDebitBalaBase;PeriodCreditBala;PeriodCreditBalaBase";
				lastY = 2;
				lastX = 3;
				lastX1 = 1;
				currFlag = "curr";
				result = mgt.queryCurrencyName(currencyName);
				titleName += result.retVal;
				printStr = "No;"+lineStr;
			}
			String[] strs = lineStr.split(";");
			
			int count = 1;
			List perList = new ArrayList();
			for(int j = 0;j<list.size(); j++){
				HashMap oldmap = (HashMap)list.get(j);
				perList = new ArrayList();
				//处理列表数据
				perList.add(count);
				for(String s : strs){
					perList.add(oldmap.get(s));
				}
				newList.add(perList);
				count ++;
			}
			
			/**
			 * 添加合计
			 */
			perList = new ArrayList();
			perList.add(count++);
			for(String s : strs){
				totalMap.put("AccFullName", "合计");
				perList.add(totalMap.get(s));
			}
			newList.add(perList);
			
			/* 业务处理(导出列表数据) */
			String type = request.getParameter("type");
			if(type!= null && "exportList".equals(type)){
				//导出总分类账数据
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath()+"\\试算平衡表.xls" ;
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				
				
				
				String locale =  this.getLocale(request).toString();
				//往strTitle中添加数据的格式为：（xxx:xxx）
				List strTitle = new ArrayList();
				
				strTitle.add(setExportMap("name:No.;lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccNumber", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccName", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:期初余额;lastX:"+lastX));
				strTitle.add(setExportMap("name:本期发生;lastX:"+lastX));
				strTitle.add(setExportMap("name:期末余额;lastX:"+lastX));
				for(int l=0;l<3;l++){
					if(l == 0){
						strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)+";nextLine:1;nextX:3;lastX:"+lastX1));
					}else{
						strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)+";lastX:"+lastX1));
					}
					strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.LendAmount", locale)+";lastX:"+lastX1));
				}
				if("curr".equals(currFlag)){
					//外币
					for(int l=0;l<6;l++){
						if(l == 0){
							strTitle.add(setExportMap("name:原币;nextLine:1;nextX:3"));
						}else{
							strTitle.add(setExportMap("name:原币"));
						}
						strTitle.add(setExportMap("name:本位币"));
					}
				}else if("all".equals(currFlag)){
					//所有币别多栏式
					for(int l=0;l<6;l++){
						for(int j = 0;j<currencyList.size();j++){
							String[] curr = (String[])currencyList.get(j);
							if(l == 0 && j == 0){
								strTitle.add(setExportMap("name:"+curr[1]+";nextLine:1;nextX:3"));
							}else{
								strTitle.add(setExportMap("name:"+curr[1]));
							}
						}
						strTitle.add(setExportMap("name:本位币"));
					}
				}
				result = mgt.exportBanlance(fos, "试算平衡表",titleName, strTitle, newList);
				fos.close();
				if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("试算平衡表.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
							getMessage(request, "com.export.success.over")+fileName)
							.setBackUrl("/FinanceReportAction.do?optype=ReporttblTrialBalance&comeMode=1&"+conditions).setNotAutoBack()
							.setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
			}else{
				request.getSession().setAttribute("accBalanceData", dealNewList(printStr.split(";"), newList));
			}
		}
		
		return getForward(request, mapping, "reporttblTrialBalance");
	}
	
	/**
	 * 科目余额表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportAccTypeInfoBalance(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		/* 会计科目搜索条件 */
		String periodYearStart = request.getParameter("periodYearStart");				//会计期间开始年
		String periodStart = request.getParameter("periodStart");						//会计期间开始
		String periodYearEnd = request.getParameter("periodYearEnd");					//会计期间结束年
		String periodEnd = request.getParameter("periodEnd");							//会计期间结束
		String acctypeCodeStart = request.getParameter("acctypeCodeStart");				//会计科目开始
		String acctypeCodeEnd = request.getParameter("acctypeCodeEnd");					//会计科目结束
		String levelStart = request.getParameter("levelStart");							//科目级别
		String currencyName = request.getParameter("currencyName");						//币别
		String showItemDetail = request.getParameter("showItemDetail");					//显示核算项目明细
		String binderNo = request.getParameter("binderNo");								//包括未过账凭证
		String accTypeNoOperation = request.getParameter("accTypeNoOperation");			//包括没有业务发生的科目（期初，本年累计）
		String accTypeNoPeriod = request.getParameter("accTypeNoPeriod");				//包括本期没有发生额的科目
		String accTypeNoYear = request.getParameter("accTypeNoYear");					//包括本年没有发生额的科目
		String takeBrowNo = request.getParameter("takeBrowNo");							//包括无发生额的科目
		String partitionLine = request.getParameter("partitionLine");					//核算项目用网络线隔开
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//显示禁用科目
		
		/* 查询所有币别 */
		List currencyList = new ArrayList();
		Result result = mgt.queryCurrencyAll();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			currencyList = (ArrayList)result.retVal;
			List<String[]> currList = currencyList;
			for(String[] ss:currList){
				if(ss[2].equals("true")){
					currList.remove(ss);
					break;
				}
			}
			request.setAttribute("currencyList", currencyList);
		}
		
		//凭证设置
		result = new VoucherMgt().queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		
		//查询开账期间，页面输入会计期间时进行判断处理
		result = mgt.getCurrentlyPeriod();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("beginPeriod", result.retVal);
		}
		
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		LoginBean loginBean = (LoginBean) request.getSession().getAttribute("LoginBean");
		/* 得到会计期间 */
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		if(comeMode == null){
			/* 第一次进入时不进行查询，弹出搜索条件 */
			return getForward(request, mapping, "reportAccTypeInfoBalance");
 		}
		
		/* 把搜索条件保存到Map中 */
		Map<String, String> conMap = new HashMap<String, String>();
		conMap.put("periodYearStart", periodYearStart);
		conMap.put("periodStart", periodStart);
		conMap.put("periodYearEnd", periodYearEnd);
		conMap.put("periodEnd", periodEnd);
		conMap.put("acctypeCodeStart", acctypeCodeStart);
		conMap.put("acctypeCodeEnd", acctypeCodeEnd);
		conMap.put("levelStart", levelStart);
		conMap.put("currencyName", currencyName);
		conMap.put("showItemDetail", showItemDetail);
		conMap.put("binderNo", binderNo);
		conMap.put("accTypeNoOperation", accTypeNoOperation);
		conMap.put("accTypeNoPeriod", accTypeNoPeriod);
		conMap.put("accTypeNoYear", accTypeNoYear);
		conMap.put("takeBrowNo", takeBrowNo);
		conMap.put("partitionLine", partitionLine);
		conMap.put("showBanAccTypeInfo", showBanAccTypeInfo);
		
		request.setAttribute("conMap", conMap);
		String conditions = dealConditions(conMap);
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/FinanceReportAction.do?optype=ReportAccTypeInfoBalance");

		long time1 = System.currentTimeMillis();
		/* 查询科目余额表数据 */
		result = mgt.accTypeInfoBalance((HashMap<String,String>)conMap, loginBean, mop);
		long time2 = System.currentTimeMillis();
		BaseEnv.log.info("科目余额表执行时间："+(time2-time1));
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			Object[] obj = (Object[])result.retVal;
			List list = (ArrayList)obj[0];
			HashMap totalMap = (HashMap)obj[1];
			
			//选择币别类型（isBase-本位币，空-综合本位币，all-所有币别，外币id-外币）
			String currType = String.valueOf(obj[2]);
			
			request.setAttribute("accTypeList", list);
			request.setAttribute("totalMap", totalMap);
			request.setAttribute("currType", currType);
			
			
			//数据处理
			int lastY = 1;
			int lastX = 1;
			int lastX1 = 0;
			String currFlag = "";
			String title = "";
			String lineStr = "AccNumber;AccFullName;";
			String printStr = "No;AccNumber;AccFullName;";
			if("".equals(currType) || "isBase".equals(currType)){
				//本位币或者综合本位币
				lineStr += "PeriodIniDebitBase;PeriodIniCreditBase;PeriodDebitSumBase;PeriodCreditSumBase;CurrYIniDebitSumBase;CurrYIniCreditSumBase;PeriodDebitBalaBase;PeriodCreditBalaBase";
				currFlag = "isBase";
				if("".equals(currType)){
					title += "<综合本位币>";
				}else{
					result = mgt.queryCurrencyName(currencyName);
					title += result.retVal;
				}
				printStr = "No;"+lineStr;
			}else if("all".equals(currType)){
				//所有币别多栏式
				String[] cur1 = new String[]{"PeriodIniDebit","PeriodIniCredit","PeriodDebitSum","PeriodCreditSum","CurrYIniDebitSum","CurrYIniCreditSum","PeriodDebitBala","PeriodCreditBala"};
				String[] cur2 = new String[]{"PeriodIniDebitBase","PeriodIniCreditBase","PeriodDebitSumBase","PeriodCreditSumBase","CurrYIniDebitSumBase","CurrYIniCreditSumBase","PeriodDebitBalaBase","PeriodCreditBalaBase"};
				for(int k = 0;k<cur1.length;k++){
					for(int l =0;l<currencyList.size();l++){
						String[] currStr = (String[])currencyList.get(l);
						String currency = currStr[0];
						if("true".equals(currStr[2])){
							//本位币
							currency = "";
						}
						lineStr += cur1[k]+"_"+currency+";";
						printStr += cur1[k]+"_"+currStr[3]+";";
					}
					lineStr += cur2[k]+";";
					printStr += cur2[k]+";";
				}
				lastY = 2;
				lastX = 2*(currencyList.size())+1;
				lastX1 = currencyList.size();
				currFlag = "all";
				title += "<所有币别多栏式>";
			}else{
				//外币
				lineStr += "PeriodIniDebit;PeriodIniDebitBase;PeriodIniCredit;PeriodIniCreditBase;PeriodDebitSum;PeriodDebitSumBase;PeriodCreditSum;PeriodCreditSumBase;CurrYIniDebitSum;CurrYIniDebitSumBase;CurrYIniCreditSum;CurrYIniCreditSumBase;PeriodDebitBala;PeriodDebitBalaBase;PeriodCreditBala;PeriodCreditBalaBase";
				lastY = 2;
				lastX = 3;
				lastX1 = 1;
				currFlag = "curr";
				result = mgt.queryCurrencyName(currencyName);
				title += result.retVal;
				printStr = "No;"+lineStr;
			}
			String[] strs = lineStr.split(";");
			List newList = new ArrayList();
			
			/**
			 * 添加详细信息
			 */
			int count = 1;
			List perList = new ArrayList();
			for(int j = 0;j<list.size(); j++){
				HashMap oldmap = (HashMap)list.get(j);
				perList = new ArrayList();
				//处理列表数据
				perList.add(count);
				for(String s : strs){
					perList.add(oldmap.get(s));
				}
				newList.add(perList);
				count ++;
			}
			
			/**
			 * 添加合计
			 */
			perList = new ArrayList();
			perList.add(count++);
			for(String s : strs){
				totalMap.put("AccFullName", "合计");
				perList.add(totalMap.get(s));
			}
			newList.add(perList);
			
			/* 业务处理(导出列表数据) */
			String type = request.getParameter("type");
			if(type!= null && "exportList".equals(type)){
				//导出总分类账数据
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath()+"\\科目余额表.xls" ;
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				//设置标题
				String titleName = "期间："+periodYearStart+"年第"+periodStart+"期至"+periodYearEnd+"年第"+periodEnd+"期        币别："+title;
				
				String locale =  this.getLocale(request).toString();
				//往strTitle中添加数据的格式为：（xxx:xxx）
				List strTitle = new ArrayList();
				
				strTitle.add(setExportMap("name:No.;lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccNumber", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccName", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:期初余额;lastX:"+lastX));
				strTitle.add(setExportMap("name:本期发生;lastX:"+lastX));
				strTitle.add(setExportMap("name:本年累计;lastX:"+lastX));
				strTitle.add(setExportMap("name:期末余额;lastX:"+lastX));
				for(int l=0;l<4;l++){
					if(l == 0){
						strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)+";nextLine:1;nextX:3;lastX:"+lastX1));
					}else{
						strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)+";lastX:"+lastX1));
					}
					strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.LendAmount", locale)+";lastX:"+lastX1));
				}
				if("curr".equals(currFlag)){
					//外币
					for(int l=0;l<8;l++){
						if(l == 0){
							strTitle.add(setExportMap("name:原币;nextLine:1;nextX:3"));
						}else{
							strTitle.add(setExportMap("name:原币"));
						}
						strTitle.add(setExportMap("name:本位币"));
					}
				}else if("all".equals(currFlag)){
					//所有币别多栏式
					for(int l=0;l<8;l++){
						for(int j = 0;j<currencyList.size();j++){
							String[] curr = (String[])currencyList.get(j);
							if(l == 0 && j == 0){
								strTitle.add(setExportMap("name:"+curr[1]+";nextLine:1;nextX:3"));
							}else{
								strTitle.add(setExportMap("name:"+curr[1]));
							}
						}
						strTitle.add(setExportMap("name:本位币"));
					}
				}
				
				result = mgt.exportBanlance(fos, "科目余额表",titleName, strTitle, newList);
				fos.close();
				if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("科目余额表.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
							getMessage(request, "com.export.success.over")+fileName)
							.setBackUrl("/FinanceReportAction.do?optype=ReportAccTypeInfoBalance&comeMode=1&"+conditions).setNotAutoBack()
							.setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
			}else{
				//数据处理
				request.getSession().setAttribute("accBalanceData", dealNewList(printStr.split(";"), newList));
			}
		}
		return getForward(request, mapping, "reportAccTypeInfoBalance");
	}
	
	
	/**
	 * 科目日报表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportAccTypeDayBalance(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		/* 会计科目搜索条件 */
		String dateStart = request.getParameter("dateStart");							//日期开始
		String dateEnd = request.getParameter("dateEnd");								//日期结束
		String acctypeCodeStart = request.getParameter("acctypeCodeStart");				//会计科目开始
		String acctypeCodeEnd = request.getParameter("acctypeCodeEnd");					//会计科目结束
		String levelStart = request.getParameter("levelStart");							//科目级别开始
		String levelEnd = request.getParameter("levelEnd");								//科目级别结束
		String currencyName = request.getParameter("currencyName");						//币别
		String binderNo = request.getParameter("binderNo");								//包括未过账凭证
		String balanceZero = request.getParameter("balanceZero");						//余额为零不显示
		String takeBrowNo = request.getParameter("takeBrowNo");							//无发生额不显示
		String showTotal = request.getParameter("showTotal");							//输出合计 
		String showItemDetail = request.getParameter("showItemDetail");					//包括核算项目明细
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//显示禁用科目
		
		/* 查询所有币别 */
		List currencyList = new ArrayList();
		Result result = mgt.queryCurrencyAll();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			currencyList = (ArrayList)result.retVal;
			request.setAttribute("currencyList", currencyList);
		}

		//凭证设置
		result = new VoucherMgt().queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		LoginBean loginBean = (LoginBean) request.getSession().getAttribute("LoginBean");
		/* 得到会计期间 */
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		if(comeMode == null){
			/* 第一次进入时不进行查询，弹出搜索条件 */
			return getForward(request, mapping, "reportAccTypeInfoDayBalance");
 		}
		
		/* 把搜索条件保存到Map中 */
		Map<String, String> conMap = new HashMap<String, String>();
		conMap.put("dateStart", dateStart);
		conMap.put("dateEnd", dateEnd);
		conMap.put("acctypeCodeStart", acctypeCodeStart);
		conMap.put("acctypeCodeEnd", acctypeCodeEnd);
		conMap.put("levelStart", levelStart);
		conMap.put("levelEnd", levelEnd);
		conMap.put("currencyName", currencyName);
		conMap.put("binderNo", binderNo);
		conMap.put("showItemDetail", showItemDetail);
		conMap.put("takeBrowNo", takeBrowNo);
		conMap.put("balanceZero", balanceZero);
		conMap.put("showTotal", showTotal);
		conMap.put("showBanAccTypeInfo", showBanAccTypeInfo);
		
		request.setAttribute("conMap", conMap);
		String conditions = dealConditions(conMap);
		
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/FinanceReportAction.do?optype=ReportAccTypeDayBalance");
		
		/* 查询科目日报表数据*/
		result = mgt.accTypeInfoDay((HashMap<String, String>)conMap,mop,loginBean);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			Object[] obj = (Object[])result.retVal;
			
			List list = (ArrayList)obj[0];								//查询的会计科目
			HashMap periodMap = (HashMap)obj[1];						//本日借贷方金额
			HashMap preMap = (HashMap)obj[2];							//上日借贷方金额
			HashMap countMap = (HashMap)obj[3];							//借贷方笔数Map
			HashMap totalMap = (HashMap)obj[4];							//合计
			String currType = String.valueOf(obj[5]);
			request.setAttribute("accTypeInfoList", list);
			request.setAttribute("periodMap", periodMap);
			request.setAttribute("preMap", preMap);
			request.setAttribute("countMap", countMap);
			request.setAttribute("totalMap", totalMap);
			request.setAttribute("currType", currType);
			
			
			
			
			//存储要保存的数据
			List newList = new ArrayList();
			
			/**
			 * 循环会计科目
			 */
			int count = 1;
			List perList = new ArrayList();
			for(int j = 0;j<list.size(); j++){
				HashMap oldmap = (HashMap)list.get(j);
				String classCode = String.valueOf(oldmap.get("classCode"));				//会计科目classCode
				
				//会计科目的数据
				String JdFlag = "贷"; 
				if(Integer.parseInt(oldmap.get("JdFlag").toString()) == 1){
					JdFlag = "借";
				}
				Object preObj = preMap.get(classCode);
				Object periodObj = periodMap.get(classCode);
				String[] fieldStrPre = new String[]{"sumdisAmountBase"};
				String[] fieldStrPeriod = new String[]{"sumDebitAmountBase","sumLendAmountBase","JdFlag","totalsumdisAmountBase"};
				String lineStr = count+";"+oldmap.get("AccNumber")+";"+oldmap.get("AccFullName")+";"+JdFlag+";";
				if("".equals(currType) || "isBase".equals(currType)){
					/* 本位币或者综合本位币 */
				}else if("all".equals(currType)){
					/* 所有币别多栏式 */
					String[] newFieldStrPre = new String[currencyList.size()+1];
					String[] newFieldStrPeriod = new String[(currencyList.size()+1)*3+1];
					//上日余额
					for(int l = 0 ;l<fieldStrPre.length;l++){
						for(int k = 0;k<currencyList.size();k++){
							String[] curStr = (String[]) currencyList.get(k);
							String curId = curStr[0];
							if("true".equals(curStr[2])){
								//本位币
								curId = "";
							}
							newFieldStrPre[k] = fieldStrPre[l].replace("Base", "")+"_"+curId;
						}
						newFieldStrPre[currencyList.size()] = fieldStrPre[l];
					}
					fieldStrPre = newFieldStrPre;
					//本日借贷方金额
					int counts = 0;
					for(int l = 0 ;l<fieldStrPeriod.length;l++){
						if(!"JdFlag".equals(fieldStrPeriod[l])){
							for(int k = 0;k<currencyList.size();k++){
								String[] curStr = (String[]) currencyList.get(k);
								String curId = curStr[0];
								if("true".equals(curStr[2])){
									//本位币
									curId = "";
								}
								newFieldStrPeriod[counts] = fieldStrPeriod[l].replace("Base", "")+"_"+curId;
								counts ++;
							}	
						}
						newFieldStrPeriod[counts] = fieldStrPeriod[l];
						counts ++;
					}
					fieldStrPeriod = newFieldStrPeriod;
				}else{
					/* 外币 */
					fieldStrPre = new String[]{"sumdisAmount","sumdisAmountBase"};
					fieldStrPeriod = new String[]{"sumDebitAmount","sumDebitAmountBase","sumLendAmount","sumLendAmountBase","JdFlag",
							"totalsumdisAmount","totalsumdisAmountBase"};
				}
				
				//上日余额
				for(int k =0;k<fieldStrPre.length;k++){
					if(preObj != null){
						HashMap pre = (HashMap)preObj;
						String value = "";
						if(pre.get(fieldStrPre[k]) != null){
							value = String.valueOf(pre.get(fieldStrPre[k]));
						}
						lineStr += value+";";
					}else{
						lineStr += " ;";
					}
				}
				//本日借方金额和本日贷方金额
				for(int k=0;k<fieldStrPeriod.length;k++){
					if(periodObj != null){
						HashMap period = (HashMap)periodObj;
						String value = "";
						if("JdFlag".equals(fieldStrPeriod[k])){
							value = JdFlag;
						}else{
							if(period.get(fieldStrPeriod[k]) != null){
								value = String.valueOf(period.get(fieldStrPeriod[k]));
							}
						}
						lineStr += value+";";
					}else{
						lineStr += " ;";
					}
				}
				//借贷方笔数
				String[] countStr = new String[]{"_debit","_lend"};
				for(String s : countStr){
					Object o = countMap.get(classCode+s);
					if(o != null){
						HashMap countMaps = (HashMap)o ;
						String value = "";
						if(countMaps.get("count") != null){
							value = String.valueOf(countMaps.get("count"));
						}
						lineStr += value+";";
					}else{
						lineStr += " ;";
					}
				}
				perList = new ArrayList();
				String[] perStr = lineStr.split(";");
				for(String s : perStr){
					perList.add(s);
				}
				newList.add(perList);
				//处理列表数据
				count ++;
			}
			
			/**
			 * 添加合计
			 */
			String[] fieldStrTotal = new String[]{"pre_sumdisAmountBase","period_sumDebitAmountBase","period_sumLendAmountBase",
					"lastJdFlag","period_totalsumdisAmountBase","debitCount","lendCount"};
			String lineStrTotal = ";;合计;;";
			String printStr = "No;AccNumber;AccFullName;JdFlag;";
			if("all".equals(currType)){
				//合计
				String[] newFieldStrTotal = new String[(currencyList.size()+1)*4+3];
				int counts = 0;
				for(int l = 0 ;l<fieldStrTotal.length;l++){
					if(!("lastJdFlag".equals(fieldStrTotal[l])) && !("debitCount".equals(fieldStrTotal[l])) && !("lendCount".equals(fieldStrTotal[l]))){
						for(int k = 0;k<currencyList.size();k++){
							String[] curStr = (String[]) currencyList.get(k);
							String curId = curStr[0];
							if("true".equals(curStr[2])){
								//本位币
								curId = "";
							}
							newFieldStrTotal[counts] = fieldStrTotal[l].replace("Base", "")+"_"+curId;
							printStr += fieldStrTotal[l].replace("Base", "").replace("pre_", "").replace("period_", "")+"_"+curStr[3]+";";
							counts ++;
						}	
					}
					newFieldStrTotal[counts] = fieldStrTotal[l];
					printStr += fieldStrTotal[l].replace("pre_", "").replace("period_", "")+";";
					counts ++;
				}
				fieldStrTotal = newFieldStrTotal;
			}else if(!"".equals(currType) && !"isBase".equals(currType)){
				//外币
				fieldStrTotal = new String[]{"pre_sumdisAmount","pre_sumdisAmountBase","period_sumDebitAmount","period_sumDebitAmountBase",
						"period_sumLendAmount","period_sumLendAmountBase","JdFlag","period_totalsumdisAmount","period_totalsumdisAmountBase","debitCount","lendCount"};
				printStr += "sumdisAmount;sumdisAmountBase;sumDebitAmount;sumDebitAmountBase;sumLendAmount;sumLendAmountBase;JdFlag;totalsumdisAmount;totalsumdisAmountBase;debitCount;lendCount";
			}else{
				printStr += "sumdisAmountBase;sumDebitAmountBase;sumLendAmountBase;lastJdFlag;totalsumdisAmountBase;debitCount;lendCount";
			}
			for(int k=0;k<fieldStrTotal.length;k++){
				String value = "";
				if(totalMap != null){
					HashMap totalPeriodMap = (HashMap)totalMap;
					if(totalPeriodMap.get(fieldStrTotal[k]) != null){
						value = String.valueOf(totalPeriodMap.get(fieldStrTotal[k]));
					}
				}
				lineStrTotal += value+" ;";
			}
			perList = new ArrayList();
			String[] perStr = lineStrTotal.split(";");
			for(String s : perStr){
				perList.add(s);
			}
			newList.add(perList);
			
			/* 业务处理(导出列表数据) */
			String type = request.getParameter("type");
			if(type!= null && "exportList".equals(type)){
				//导出科目日报表数据
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath() + "\\科目日报表.xls";
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				//设置标题
				String titleName = "日期区间："+dateStart+" 至 "+dateEnd+"        币别：";
				String currFlag = "";
				int lastY = 0;
				int lastX = 0;
				if("".equals(currType) || "isBase".equals(currType)){
					if("".equals(currType)){
						titleName += "<综合本位币>";
					}else{
						result = mgt.queryCurrencyName(currencyName);
						titleName += result.retVal;
					}
				}else if("all".equals(currType)){
					//所有币别多栏式
					currFlag = "all";
					titleName += "<所有币别多栏式>";
					lastY = 1;
					lastX = currencyList.size();
				}else{
					//外币
					lastY = 1;
					lastX = 1;
					currFlag = "curr";
					result = mgt.queryCurrencyName(currencyName);
					titleName += result.retVal;
				}
				//往strTitle中添加数据的格式为：（xxx:xxx）
				List strTitle = new ArrayList();
				String locale = this.getLocale(request).toString();
				strTitle.add(setExportMap("name:No.;lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccNumber", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccName", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.JdFlag", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:上日余额;lastX:"+lastX));
				strTitle.add(setExportMap("name:本日借方发生额;lastX:"+lastX));
				strTitle.add(setExportMap("name:本日贷方发生额;lastX:"+lastX));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.JdFlag", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:本日余额;lastX:"+lastX));
				strTitle.add(setExportMap("name:借方笔数;lastY:"+lastY));
				strTitle.add(setExportMap("name:贷方笔数;lastY:"+lastY));
				if("curr".equals(currFlag)){
					//外币
					for(int l=0;l<4;l++){
						if(l == 0){
							strTitle.add(setExportMap("name:原币;nextLine:1;nextX:4"));
						}else if(l == 3){
							strTitle.add(setExportMap("name:原币;nextX:11"));
						}else{
							strTitle.add(setExportMap("name:原币"));
						}
						strTitle.add(setExportMap("name:本位币"));
					}
				}else if("all".equals(currFlag)){
					//所有币别多栏式
					for(int l=0;l<4;l++){
						for(int j = 0;j<currencyList.size();j++){
							String[] curr = (String[])currencyList.get(j);
							if(l == 0 && j == 0){
								strTitle.add(setExportMap("name:"+curr[1]+";nextLine:1;nextX:4"));
							}else if(l == 3 && j == 0){
								strTitle.add(setExportMap("name:"+curr[1]+";nextX:"+(5+((currencyList.size()+1)*3))));
							}else{
								strTitle.add(setExportMap("name:"+curr[1]));
							}
						}
						strTitle.add(setExportMap("name:本位币"));
					}
				}
				
				result = mgt.exportBanlance(fos, "科目日报表",titleName, strTitle, newList);
				fos.close();
				if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("科目日报表.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
							getMessage(request, "com.export.success.over")+fileName)
							.setBackUrl("/FinanceReportAction.do?optype=ReportAccTypeDayBalance&comeMode=1&"+conditions).setNotAutoBack()
							.setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
			}else{
				request.getSession().setAttribute("accBalanceData", dealNewList(printStr.split(";"), newList));
			}
			
			//根据科目来决定余额方向
			Iterator iter = preMap.entrySet().iterator();
			while(iter.hasNext()){
				Entry entry = (Entry)iter.next();
				HashMap tMap = (HashMap)entry.getValue();
				if(tMap.get("JdFlag")!=null && "2".equals(tMap.get("JdFlag").toString())){
					//
					Object o = tMap.get("sumdisAmountBase");
					if(o != null && !"".equals(o)){
						tMap.put("sumdisAmountBase",new BigDecimal(o.toString()).negate());
					}
				}
			}
			iter = periodMap.entrySet().iterator();
			while(iter.hasNext()){
				Entry entry = (Entry)iter.next();
				HashMap tMap = (HashMap)entry.getValue();
				if(tMap.get("JdFlag")!=null && "2".equals(tMap.get("JdFlag").toString())){
					//
					Object o = tMap.get("totalsumdisAmountBase");
					if(o != null && !"".equals(o)){
						tMap.put("totalsumdisAmountBase",new BigDecimal(o.toString()).negate());
					}
				}
			}
		}
		return getForward(request, mapping, "reportAccTypeInfoDayBalance");
	}
	
	/**
	 * 凭证汇总表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportCertificateSum(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		/* 会计科目搜索条件 */
		String dateStart = request.getParameter("dateStart");							//日期开始
		String dateEnd = request.getParameter("dateEnd");								//日期结束
		String levelStart = request.getParameter("levelStart");							//科目级别开始
		String levelEnd = request.getParameter("levelEnd");								//科目级别结束
		String currencyName = request.getParameter("currencyName");						//币别
		String area = request.getParameter("area");										//范围（0 所有凭证，1 未过账凭证，2 已过账凭证）
		String showItemDetail = request.getParameter("showItemDetail");					//显示核算项目明细
		String takeBrowNo = request.getParameter("takeBrowNo");							//无发生额不显示
		String showAll = request.getParameter("showAll");								//包含所有凭证字号
		String credTypeStr = request.getParameter("credTypeStr");						//保存输入的凭证字范围  (凭证字;最小值;最大值;|)
		
		/* 查询所有币别 */
		List currencyList = new ArrayList();
		Result result = mgt.queryCurrencyAll();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			currencyList = (ArrayList)result.retVal;
			request.setAttribute("currencyList", currencyList);
		}

		/* 凭证设置 */
		result = new VoucherMgt().queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		LoginBean loginBean = (LoginBean) request.getSession().getAttribute("LoginBean");
		/* 得到会计期间 */
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		if(comeMode == null){
			/* 第一次进入时不进行查询，弹出搜索条件 */
			return getForward(request, mapping, "reportCertificateSum");
 		}
		
		/* 把搜索条件保存到Map中 */
		Map<String, String> conMap = new HashMap<String, String>();
		conMap.put("dateStart", dateStart);
		conMap.put("dateEnd", dateEnd);
		conMap.put("levelStart", levelStart);
		conMap.put("levelEnd", levelEnd);
		conMap.put("currencyName", currencyName);
		conMap.put("area", area);
		conMap.put("showAll", showAll);
		conMap.put("credTypeStr", credTypeStr);
		conMap.put("showItemDetail", showItemDetail);
		conMap.put("takeBrowNo", takeBrowNo);
		request.setAttribute("conMap", conMap);
		String conditions = dealConditions(conMap);
		
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/FinanceReportAction.do?optype=ReportCertificateSum");
		
		
		/* 查询凭证汇总表数据*/
		result = mgt.accCertificateSum((HashMap<String, String>)conMap,mop,loginBean);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			Object[] obj = (Object[])result.retVal;
			List list = (ArrayList)obj[0];
			HashMap totalMap = (HashMap)obj[1];
			String currType = String.valueOf(obj[2]);
			request.setAttribute("accList", list);
			request.setAttribute("totalMap", totalMap);
			request.setAttribute("currType", currType);
			
			String locale = this.getLocale(request).toString();
			
			/**
			 * 保存数据到newList中
			 */
			int lastY = 0;
			int lastX = 0;
			int lastX1 = 0;
			String currFlag = "";
			String title = "币别：";
			String lineStr = "AccNumber;AccFullName;";
			if("".equals(currType) || "isBase".equals(currType)){
				//本位币或者综合本位币
				currFlag = "isBase";
				lineStr += "sumDebitAmountBase;sumLendAmountBase;isflag;sumBalanceAmountBase";
				if("".equals(currType)){
					title += "<综合本位币>";
				}else{
					result = mgt.queryCurrencyName(currencyName);
					title += result.retVal;
				}
				lastX1 = 1;
			}else if("all".equals(currType)){
				//所有币别多栏式
				String[] fieldStrPeriod = new String[]{"sumDebitAmountBase","sumLendAmountBase","isflag","sumBalanceAmountBase"};
				int counts = 0;
				for(int l = 0 ;l<fieldStrPeriod.length;l++){
					if(!"isflag".equals(fieldStrPeriod[l])){
						for(int k = 0;k<currencyList.size();k++){
							String[] curStr = (String[]) currencyList.get(k);
							String curId = curStr[0];
							if("true".equals(curStr[2])){
								//本位币
								curId = "";
							}
							lineStr += fieldStrPeriod[l].replace("Base", "")+"_"+curId+";";
							counts ++;
						}	
					}
					lineStr += fieldStrPeriod[l]+";";
					counts ++;
				}
				lastY = 1;
				lastX = currencyList.size();
				lastX1 = currencyList.size()+1;
				currFlag = "all";
				title += "<所有币别多栏式>";
			}else{
				//外币
				lineStr += "sumDebitAmount;sumDebitAmountBase;sumLendAmount;sumLendAmountBase;isflag;sumBalanceAmount;sumBalanceAmountBase";
				lastY = 1;
				lastX = 1;
				lastX1 = 2;
				currFlag = "curr";
				result = mgt.queryCurrencyName(currencyName);
				title += result.retVal;
			}
			List newList = new ArrayList();
			List perList = new ArrayList();
			int count = 1;
			//把数据添加到list中
			String[] perStr = lineStr.split(";");
			for(int j = 0;j<list.size(); j++){
				HashMap oldmap = (HashMap)list.get(j);
				perList = new ArrayList();
				//处理列表数据
				perList.add(count);
				for(String s : perStr){
					perList.add(oldmap.get(s));
				}
				newList.add(perList);
				count ++;
			}
			
			//添加合计
			perList = new ArrayList();
			perList.add(count++);
			for(String s : perStr){
				totalMap.put("AccFullName", "合计");
				perList.add(totalMap.get(s));
			}
			newList.add(perList);
			
			/* 业务处理(导出列表数据) */
			String type = request.getParameter("type");
			if(type!= null && "exportList".equals(type)){
				//导出凭证汇总表数据
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath()+"\\凭证汇总表.xls" ;
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				//设置标题
				String titleName = "日期区间："+dateStart+" 至 "+dateEnd+"  范围:";
				if(area != null && "0".equals(area)){
					titleName += "所有凭证";
				}else if(showAll != null && "1".equals(area)){
					titleName += "未过账凭证";
				}else if(showAll != null && "2".equals(area)){
					titleName += "已过账凭证";
				}
				titleName += " 凭证字：";
				if(showAll != null && "1".equals(showAll)){
					titleName += "全部";
				}else{
					List sysTypeList = ((EnumerateBean)BaseEnv.enumerationMap.get("CredTypeID")).getEnumItem() ;
					for(Object o: sysTypeList){
		               	 EnumerateItemBean item = (EnumerateItemBean)o;
		               	if(credTypeStr!=null && !"".equals(credTypeStr)){
                    		String[] credStr = credTypeStr.split(";]");
                    		for(String s : credStr){
                    			String[] values = s.split(";");
                    			if(values[0].equals(item.getEnumValue())){
                    				KRLanguage language = item.getDisplay();
                    				titleName += language.get(locale);
                    				String startCred = "";
                    				String endCred = "";
                    				if(values.length>1){
                    					startCred = values[1];
                    					if(values.length>2){
                    						endCred = values[2];
                    					}
                    				}
                    				if("".equals(startCred) && "".equals(endCred)){
                    					titleName += "(全部);";
                    				}else{
                    					titleName += "("+startCred+" - "+endCred+");";
                    				}
                    			}
                    		}
		               	}
		            }
				}
				
				titleName += "      "+title;
				//往strTitle中添加数据的格式为：（xxx:xxx）
				List strTitle = new ArrayList();
				
				strTitle.add(setExportMap("name:No.;lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccNumber", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccName", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)+";lastX:"+lastX));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.LendAmount", locale)+";lastX:"+lastX));
				strTitle.add(setExportMap("name:余额;lastX:"+lastX1));
				if("curr".equals(currFlag)){
					//外币
					for(int l=0;l<3;l++){
						if(l==2){
							strTitle.add(setExportMap("name:"));
						}
						if(l == 0){
							strTitle.add(setExportMap("name:原币;nextLine:1;nextX:3"));
						}else{
							strTitle.add(setExportMap("name:原币"));
						}
						strTitle.add(setExportMap("name:本位币"));
					}
				}else if("all".equals(currFlag)){
					//所有币别多栏式
					for(int l=0;l<3;l++){
						if(l==2){
							strTitle.add(setExportMap("name:"));
						}
						for(int j = 0;j<currencyList.size();j++){
							String[] curr = (String[])currencyList.get(j);
							if(l == 0 && j == 0){
								strTitle.add(setExportMap("name:"+curr[1]+";nextLine:1;nextX:3"));
							}else{
								strTitle.add(setExportMap("name:"+curr[1]));
							}
						}
						strTitle.add(setExportMap("name:本位币"));
					}
				}
				result = mgt.exportBanlance(fos, "凭证汇总表",titleName, strTitle, newList);
				fos.close();
				if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("凭证汇总表.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
							getMessage(request, "com.export.success.over")+fileName)
							.setBackUrl("/FinanceReportAction.do?optype=ReportCertificateSum&comeMode=1&"+conditions).setNotAutoBack()
							.setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
			}else{
				lineStr  = "NO;"+lineStr;
				String[] str = lineStr.split(";");
				request.getSession().setAttribute("accBalanceData", dealNewList(str, newList));
			}
		}
		return getForward(request, mapping, "reportCertificateSum");
	}
	
	
	/**
	 * 核算项目余额表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportAccCalculateBalance(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		/* 搜索条件 */
		String periodYearStart = request.getParameter("periodYearStart");				//会计期间开始年
		String periodStart = request.getParameter("periodStart");						//会计期间开始
		String periodYearEnd = request.getParameter("periodYearEnd");					//会计期间结束年
		String periodEnd = request.getParameter("periodEnd");							//会计期间结束	
		String accCode = request.getParameter("accCode");								//会计科目
		String currencyName = request.getParameter("currencyName");						//币别
		String itemSort = request.getParameter("itemSort");								//项目类别
		String itemCodeStart = request.getParameter("itemCodeStart");					//项目代码开始
		String itemCodeEnd = request.getParameter("itemCodeEnd");						//项目代码结束
		String levelStart = request.getParameter("levelStart");							//科目级别开始
		String binderNo = request.getParameter("binderNo");								//包括未过账凭证
		String takeBrowNo = request.getParameter("takeBrowNo");							//余额为零不显示
		String balanceAndTakeBrowNo = request.getParameter("balanceAndTakeBrowNo");		//显示发生额为零的记录
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//显示禁用科目
		
		/* 查询所有币别 */
		Result result = mgt.queryCurrencyAll();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("currencyList", result.retVal);
		}

		//凭证设置
		result = new VoucherMgt().queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		LoginBean loginBean = (LoginBean) request.getSession().getAttribute("LoginBean");
		
		/* 得到会计期间 */
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		
		//查询开账期间，页面输入会计期间时进行判断处理
		result = mgt.getCurrentlyPeriod();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("beginPeriod", result.retVal);
		}
		
		if(comeMode == null){
			/* 第一次进入时不进行查询，弹出搜索条件 */
			return getForward(request, mapping, "reportAccCalculateBalance");
 		}
		
		/* 把搜索条件保存到Map中 */
		Map<String, String> conMap = new HashMap<String, String>();
		conMap.put("periodYearStart", periodYearStart);
		conMap.put("periodStart", periodStart);
		conMap.put("periodYearEnd", periodYearEnd);
		conMap.put("periodEnd", periodEnd);
		conMap.put("accCode", accCode);
		conMap.put("currencyName", currencyName);
		conMap.put("itemSort", itemSort);
		conMap.put("itemCodeStart", itemCodeStart);
		conMap.put("itemCodeEnd", itemCodeEnd);
		conMap.put("levelStart", levelStart);
		conMap.put("binderNo", binderNo);
		conMap.put("takeBrowNo", takeBrowNo);
		conMap.put("balanceAndTakeBrowNo", balanceAndTakeBrowNo);
		conMap.put("showBanAccTypeInfo", showBanAccTypeInfo);
		request.setAttribute("conMap", conMap);
		
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/FinanceReportAction.do?optype=ReportAccCalculateBalance");
		
		/* 搜索条件拼装成String */
		String conditions = dealConditions(conMap);
		
		/* 查询核算项目余额表数据*/
		result = mgt.accCalculateBalance((HashMap<String, String>)conMap,mop,loginBean);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			Object[] obj = (Object[])result.retVal;
			List accList = (ArrayList)obj[0];
			request.setAttribute("accList",accList);
			request.setAttribute("currType",obj[1]);
			
			//会计科目数据
			result = mgt.queryAccType(accCode);
			String[] acctypeData = (String[])result.retVal;
			request.setAttribute("acctypeData",acctypeData);
			
			/**
			 * 数据组装
			 */
			List newList = new ArrayList();
			List preList = new ArrayList();
			String[] strs = new String[]{"AccCode","AccFullName","init_sumDebitAmountBase","init_sumLendAmountBase","sumDebitAmountBase","sumLendAmountBase","year_sumDebitAmountBase","year_sumLendAmountBase","end_sumDebitAmountBase","end_sumLendAmountBase"};
			for(int i=0;i<accList.size();i++){
				HashMap accMap = (HashMap)accList.get(i);
				Iterator iter = accMap.entrySet().iterator();
				while(iter.hasNext()){
					Entry entry = (Entry)iter.next();
					HashMap accEntryMap = (HashMap)entry.getValue();
					preList = new ArrayList();
					preList.add(accEntryMap.get("AccYear")+"."+accEntryMap.get("AccPeriod"));
					for(String s : strs){
						Object o = accEntryMap.get(s);
						preList.add(o);
					}
					newList.add(preList);
				}
			}
			
			/**
			 * 导出数据进行处理
			 */
			String type = request.getParameter("type");
			if(type != null && "exportList".equals(type)){
				//导出核算项目余额表数据
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath()+"\\核算项目余额表.xls" ;
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				//设置标题
				String titleName = "期间："+periodYearStart+"年第"+periodStart+"期至"+periodYearEnd+"年第"+periodEnd+"期   会计科目:"+acctypeData[0]+" - "+acctypeData[1]+"   项目类别：";
				if("DepartmentCode".equals(itemSort)){
					titleName += "部门";
				}else if("EmployeeID".equals(itemSort)){
					titleName += "职员";
				}else if("StockCode".equals(itemSort)){
					titleName += "仓库";
				}else if("ClientCode".equals(itemSort)){
					titleName += "客户";
				}else if("ProjectCode".equals(itemSort)){
					titleName += "项目";
				}else if("SuplierCode".equals(itemSort)){
					titleName += "供应商";
				}
				
				/**
				 * 列的头部标题
				 */
				String locale = this.getLocale(request).toString();
				List strTitle = new ArrayList();
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccMain.Period", locale)+";lastY:1"));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccNumber", locale)+";lastY:1"));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccName", locale)+";lastY:1"));
				strTitle.add(setExportMap("name:期初余额;lastX:1"));
				strTitle.add(setExportMap("name:本期发生;lastX:1"));
				strTitle.add(setExportMap("name:本年累计;lastX:1"));
				strTitle.add(setExportMap("name:期末余额;lastX:1"));
				for(int i=0;i<4;i++){
					if(i == 0){
						strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)+";nextLine:1;nextX:3"));
					}else{
						strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)));
					}
					strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.LendAmount", locale)));
				}
				result = mgt.exportBanlance(fos, "核算项目余额表",titleName, strTitle, newList);
				fos.close();
				if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("核算项目余额表.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
							getMessage(request, "com.export.success.over")+fileName)
							.setBackUrl("/FinanceReportAction.do?optype=ReportAccCalculateBalance&comeMode=1&"+conditions).setNotAutoBack()
							.setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
			}else{
				String[] str = new String[]{"Period","AccNumber","AccFullName","InitDebit","InitLend","PeriodDebit","PeriodLend","YearDebit","YearLend","PeriodDCDebit","PeriodDCLend"};
				request.getSession().setAttribute("accBalanceData", dealNewList(str, newList));
			}
		}
		return getForward(request, mapping, "reportAccCalculateBalance");
	}
	
	/**
	 * 处理搜索条件把条件改为name=value&形式
	 * @param conMap
	 * @return
	 */
	public String dealConditions(Map<String, String> conMap){
		String conditions = "";
		Iterator iterator = conMap.keySet().iterator();
		while(iterator.hasNext()) {
			Object key = iterator.next();
			if(conMap.get(key) != null){
				conditions += key+"="+conMap.get(key)+"&";
			}
		}
		if(conditions.length()>0){
			conditions.substring(0,conditions.length()-1);
		}
		return conditions;
	}
	
	/**
	 * 把arrayList<arrayList>转换为arrayList<HashMap>格式
	 * @param str
	 * @param newList
	 * @return
	 */
	public List dealNewList(String[] str,List newList){
		List printList = new ArrayList();
		for(int i=0;i<newList.size();i++){
			List childList = (ArrayList)newList.get(i);
			HashMap map = new HashMap();
			if(str.length<childList.size()){
				for(int j=0;j<str.length;j++){
					map.put(str[j], childList.get(j));
				}
			}else{
				for(int j=0;j<childList.size();j++){
					map.put(str[j], childList.get(j));
				}
			}
			printList.add(map);
		}
		return printList;
	}
	
}
