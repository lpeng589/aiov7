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
 * Title:���񱨱�Action
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-05-13 ���� 09:30
 * @Copyright: �������
 * @Author fjj
 * @preserve all
 */
public class FinanceReportAction extends MgtBaseAction{
	
	FinanceReportMgt mgt = new FinanceReportMgt();
	
	/**
	 * exe ��������ں���
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
		// ���ݲ�ͬ�������ͷ������ͬ��������
		int operation = getOperation(request);
		ActionForward forward = null;
		
		// ���ֱ���
		String optype = request.getParameter("optype");
		String type = request.getParameter("type");								//�����ť���ͣ�������ť����ӡ��ť��
		
		if(optype != null && "ReporttblAccBalance".equals(optype)){
			/* �ܷ����˱��� */
			forward = reportAccBalance(mapping, form, request, response);
			
		}else if(optype != null && "ReporttblAccDet".equals(optype)){
			
			/* ��ϸ�ܷ����� */
			if(type!=null && "detail".equals(type)){
				//ĳ����Ŀ����ϸ������
				forward = reportAccDetDetail(mapping, form, request, response);
			}else if(type != null && "loadIsItem".equals(type)){
				//���������м��غ�����
				forward = reportAccDetLoadIsItem(mapping, form, request, response);
			}else{
				//��Ŀ��ҳ
				forward = reportAccDetIndex(mapping, form, request, response);
			}
			
		}else if(optype != null && "ReporttblAccAllDet".equals(optype)){
			/* ����ʽ��ϸ�� */
			if(type != null && "designList".equals(type)){
				//����ʽ��ϸ�Զ����б�
				forward = reportDesignList(mapping, form, request, response);
			}else if(type!=null && "dealDesign".equals(type)){
				//����ʽ��ƽ���
				forward = reportDesign(mapping, form, request, response);
			}else if(type!=null && "delDesign".equals(type)){
				//ɾ�������������
				forward = delDesign(mapping, form, request, response);
			}else if(type!=null && "queryDesign".equals(type)){
				//��ѯ�����л�ȡ����ʽ��ϸ���û��Զ�������� ������
				forward = queryDesign(mapping, form, request, response);
			}else if(type!=null && "ajaxBearData".equals(type)){
				//ajax�������� ������ƿ�Ŀ��ȡ�������¼���Ŀ����
				forward = ajaxBearData(mapping, form, request, response);
			}else if(type!=null && "queryAccIsItem".equals(type)){
				forward = queryAccIsItem(mapping, form, request, response);
			}else{
				//��ѯ�б�
				forward = reportAccAllDetIndex(mapping, form, request, response);
			}
		}else if(optype != null && "ReporttblAccCalculate".equals(optype)){
			/* ������Ŀ�������� */
			if(type!=null && "detail".equals(type)){
				//���������Ŀʱ��ʾ����
				forward = reporttblAccCalculateDetail(mapping, form, request, response);
			}else{
				//��ҳ
				forward = reporttblAccCalculateIndex(mapping, form, request, response);
			}
		}else if(optype != null && "ReporttblAccCalculateDet".equals(optype)){
			/* ������Ŀ��ϸ�� */
			if(type!=null && "detail".equals(type)){
				//�����ƿ�Ŀ�����ѯ������Ŀ��ϸ����
				forward = reporttblAccCalculateDetDetail(mapping, form, request, response);
			}else{
				//��ҳ
				forward = reporttblAccCalculateDetIndex(mapping, form, request, response);
			}
		}else if(optype != null && "ReporttblTrialBalance".equals(optype)){
			/* ����ƽ�ⱨ�� */
			forward = reporttblTrialBalance(mapping, form, request, response);
			
		}else if(optype != null && "ReportAccTypeInfoBalance".equals(optype)){
			/* ��ƿ�Ŀ���� */
			forward = reportAccTypeInfoBalance(mapping, form, request, response);
			
		}else if(optype != null && "ReportAccTypeDayBalance".equals(optype)){
			/* ��Ŀ�ձ��� */
			forward = reportAccTypeDayBalance(mapping, form, request, response);
			
		}else if(optype != null && "ReportCertificateSum".equals(optype)){
			/* ƾ֤���ܱ� */
			forward = reportCertificateSum(mapping, form, request, response);
			
		}else if(optype != null && "ReportAccCalculateBalance".equals(optype)){
			/* ������Ŀ���� */
			forward = reportAccCalculateBalance(mapping, form, request, response);
			
		}
		return forward;
	}
	
	/**
	 * �ܷ����˱���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportAccBalance(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		/* ��ѯ���бұ����� */
		List currencyList = new ArrayList();
		Result result = mgt.queryCurrencyAll();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			currencyList = (ArrayList)result.retVal;
			request.setAttribute("currencyList", currencyList);
		}
		//ƾ֤����
		result = new VoucherMgt().queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		
		/* ����ڼ� */
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		
		//��ѯ�����ڼ䣬ҳ���������ڼ�ʱ�����жϴ���
		result = mgt.getCurrentlyPeriod();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("beginPeriod", result.retVal);
		}
		
		if(comeMode == null){
			/* ��һ�ν��� */
			return getForward(request, mapping, "reportAccBalanceList");
 		}
		
		//��������
		String periodYearStart = request.getParameter("periodYearStart");				//����ڼ俪ʼ��
		String periodStart = request.getParameter("periodStart");						//����ڼ俪ʼ
		String periodYearEnd = request.getParameter("periodYearEnd");					//����ڼ������
		String periodEnd = request.getParameter("periodEnd");							//����ڼ����
		String levelStart = request.getParameter("levelStart");							//��Ŀ����ʼ
		String levelEnd = request.getParameter("levelEnd");								//��Ŀ�������
		String acctypeCodeStart = request.getParameter("acctypeCodeStart");				//��Ŀ���뿪ʼ
		String acctypeCodeEnd = request.getParameter("acctypeCodeEnd");					//��Ŀ�������
		String currencyName = request.getParameter("currencyName");						//�ұ�
		String takeBrowNo = request.getParameter("takeBrowNo");							//�޷������ʾ
		String balanceAndTakeBrowNo = request.getParameter("balanceAndTakeBrowNo");		//�޷���������Ϊ��
		String binderNo = request.getParameter("binderNo");								//����δ����ƾ֤
		String showIsItem = request.getParameter("showIsItem");							//��ʾ������Ŀ��ϸ
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//��ʾ���ÿ�Ŀ
		
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
    	
		/* ��ѯ�ܷ����˱��� */
    	Result rs = mgt.queryAccBalanceData(periodYearStart, periodStart, periodYearEnd, periodEnd,
    				levelStart, levelEnd, acctypeCodeStart, acctypeCodeEnd, currencyName, 
    				takeBrowNo, balanceAndTakeBrowNo, binderNo, showIsItem, showBanAccTypeInfo,loginBean, pageSize, pageNo, mop);
		long time2 = System.currentTimeMillis();
		BaseEnv.log.info("�ܷ����˺�ִ̨��ʱ�䣺"+(time2-time1));
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			Object[] obj = (Object[])rs.retVal;
			
			//ѡ��ıұ�
			String currType = String.valueOf(obj[1]);
			//����Ĳ�ѯ������
			List list = (ArrayList)obj[0];
			//��ѯ�ɹ������б����
			request.setAttribute("accBalanceList", list);
			request.setAttribute("conMap", conMap);
			request.setAttribute("currType", currType);
			
			
			//������װ���ݸ�ʽ��list<ArrayList>����
			boolean flag = false;
			String periods = "";
			if(!periodYearStart.equals(periodYearEnd)){
				flag = true;
				periods = periodYearStart+"."+periodStart;
			}else{
				periods = periodStart;
			}
			//���ݴ���
			int lastY = 0;
			int lastX = 0;
			int lastX1 = 0;
			String currFlag = "";
			String title = "";
			String lineStr = "__AccNumber;__AccFullName;"+periods+";__credTypeidOrderNo;�ڳ����;";
			String printStr = "AccNumber;AccFullName;Period;credTypeidOrderNo;RecordComment;";
			if("".equals(currType) || "isBase".equals(currType)){
				//��λ�һ����ۺϱ�λ��
				currFlag = "isBase";
				if("".equals(currType)){
					title += "<�ۺϱ�λ��>";
				}else{
					result = mgt.queryCurrencyName(currencyName);
					title += result.retVal;
				}
				lastX1 = 1;
				lineStr += ";;__isIniflag;__PeriodIniBase";
				printStr += "PeriodDebitSumBase;PeriodCreditSumBase;isflag;PeriodDCBalaBase";
			}else if("all".equals(currType)){
				//���бұ����ʽ
				String[] fieldStr = new String[]{"PeriodDebitSumBase","PeriodCreditSumBase","isflag","PeriodDCBalaBase"};
				for(int k=0;k<fieldStr.length;k++){
					if(!"isIniflag".equals(fieldStr[k])){
						for(int l =0;l<currencyList.size();l++){
							String[] currStr = (String[])currencyList.get(l);
							String currency = currStr[0];
							if("true".equals(currStr[2])){
								//��λ��
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
				title += "<���бұ����ʽ>";
			}else{
				//���
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
					//���ڻ���ڼ�����
					periodList = (ArrayList)o;
				}
				//�޷������ʾ
				if(takeBrowNo !=null && "1".equals(takeBrowNo) && periodList.size()==0 
						&& "".equals(String.valueOf(oldmap.get("PeriodIniBase")))){
					continue;
				}
				//�����ڳ�����
				for(int k =0;k<strs.length;k++){
					String str = strs[k];
					if(str.length()>2 && "__".equals(str.substring(0,2))){
						//����[] Ҫ��Map��ȡ
						String newStr = str.substring(2);
						perList.add(oldmap.get(newStr));
					}else{
						perList.add(str);
					}
				}
				newList.add(perList);
				//���ڻ���ڼ�����
				for(int l =0;l<periodList.size();l++){
					HashMap periodMap = (HashMap)periodList.get(l);
					
					perList = new ArrayList();
					//���ںϼ�
					perList.add("");perList.add("");
					perList.add(flag==true?periodMap.get("Nyear")+"."+periodMap.get("Period"):periodMap.get("Period"));
					perList.add(periodMap.get("credTypeidOrderNo"));
					perList.add("���ںϼ�");
					String lineVal = "PeriodDebitSumBase;PeriodCreditSumBase;PeriodDCBalaBaseisflag;PeriodDCBalaBase";
					if("all".equals(currFlag)){
						String lines = "";
						for(String s : lineVal.split(";")){
							if(!"PeriodDCBalaBaseisflag".equals(s)){
								for(int k=0;k<currencyList.size();k++){
									String[] currs = (String[])currencyList.get(k);
									String curId = s.replace("Base", "")+"_";
									if(!"true".equals(currs[2])){
										//��λ��
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
					
					//�����ۼ�
					perList = new ArrayList();
					perList.add("");perList.add("");
					perList.add(flag==true?periodMap.get("Nyear")+"."+periodMap.get("Period"):periodMap.get("Period"));
					perList.add("");
					perList.add("�����ۼ�");
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
										//��λ��
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
			
			
			/* ҵ����(�����б�����) */
			String type = request.getParameter("type");
			if(type!= null && "exportList".equals(type)){
				//�����ܷ���������
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath()+"\\�ܷ�����.xls" ;
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				//���ñ���
				String titleName = "�ڼ䣺"+periodYearStart+"���"+periodStart+"����"+periodYearEnd+"���"+periodEnd+"��        �ұ�"+title;
				
				
				String locale =  this.getLocale(request).toString();
				//��strTitle��������ݵĸ�ʽΪ������������;��ʶ;�������;������У�
				List strTitle = new ArrayList();
				
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccNumber", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccName", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccMain.Period", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:ƾ֤�ֺ�;lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.RecordComment", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)+";lastX:"+lastX));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.LendAmount", locale)+";lastX:"+lastX));
				strTitle.add(setExportMap("name:���;lastX:"+lastX1));
				if("curr".equals(currFlag)){
					//���
					for(int l=0;l<3;l++){
						if(l==2){
							strTitle.add(setExportMap("name:"));
						}
						if(l == 0){
							strTitle.add(setExportMap("name:ԭ��;nextLine:1;nextX:5"));
						}else{
							strTitle.add(setExportMap("name:ԭ��"));
						}
						strTitle.add(setExportMap("name:��λ��"));
					}
				}else if("all".equals(currFlag)){
					//���бұ����ʽ
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
						strTitle.add(setExportMap("name:��λ��"));
					}
				}
				rs = mgt.exportBanlance(fos, "�ܷ�����",titleName, strTitle, newList);
				fos.close();
				if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("�ܷ�����.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
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
	 * ����values��װ����ͷ��������
	 * @param values               �������ݣ��磺   name:xxx;lastX:X;lastY:X��  ��xxx:xxx;��ʽ�����ƶ�Ӧ��ֵ
	 * @return
	 */
	public HashMap setExportMap(String values){
		HashMap setMap = new HashMap();
		if(values != null && !"".equals(values)){
			String[] value = values.split(";");			//�õ�xxx:xxx��ʽ������
			if(value.length>0){
				//������Ӧ�ļ�¼
				for(String s : value){
					if(s.indexOf(":")!=-1){
						//������Ӧ���͵�
						setMap.put(s.substring(0,s.indexOf(":")),s.substring(s.indexOf(":")+1));
					}else{
						//��ֻ��һ��ֵʱ��Ĭ�ϸ�����ֵ
						setMap.put(s,"");
					}
				}
			}
		}
		return setMap;
	}
	
	
	/**
	 * ����ɸѡ������ȡ��ʼ���ڣ���������
	 */
	protected Map reportDateInterval(Map<String, String> conMap,String dateType){
		Map m = new HashMap();
		/* ת����������  */
		String begD = "";
		String endD = "";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		if("1".equals(dateType)){
			 //�������
		    cal.set(Calendar.YEAR,Integer.parseInt(conMap.get("periodYearStart")));
		    //�����·�
		    cal.set(Calendar.MONTH, Integer.parseInt(conMap.get("periodStart"))-1);		    
		    //������
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
				//*****����******//
			} else if("halfyear".equals(momentType)){
				//*****�������*****//
			} else if("threemonth".equals(momentType)){
				//*****���������****//
			} else if("tomonth".equals(momentType)){
				//*****�ϸ���******//
			} else if("month".equals(momentType)){
				//******����******//
			} else if("week".equals(momentType)){
				//******����******//
			} else if("threeday".equals(momentType)){
				//******�������*****//
			} else if("day".equals(momentType)){
				//******����*****//
			} else if("yesterday".equals(momentType)){
				//******����****//
			}
		}
		/****end****/
		m.put("begD", begD);
		m.put("endD", endD);
		return m;
	}
	
	/**
	 * ��ϸ�ܷ�����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportAccDetIndex(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		/* �������� */
		String dateType = request.getParameter("dateType");								//ʱ�����ͣ����ڼ��ѯ�������ڲ�ѯ��
		String periodYearStart = request.getParameter("periodYearStart");				//����ڼ俪ʼ��
		String periodStart = request.getParameter("periodStart");						//����ڼ俪ʼ
		String periodYearEnd = request.getParameter("periodYearEnd");					//����ڼ������
		String periodEnd = request.getParameter("periodEnd");							//����ڼ����
		String dateStart = request.getParameter("dateStart");							//���ڿ�ʼ
		String dateEnd = request.getParameter("dateEnd");								//���ڽ���
		String momentType = request.getParameter("momentType");							//�׶Σ����꣬���µȣ�
		String currencyName = request.getParameter("currencyName");						//�ұ�
		String showInAccTypeInfo = request.getParameter("showInAccTypeInfo");			//��ʾ�Է���Ŀ
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//��ʾ���ÿ�Ŀ
		String binderNo = request.getParameter("binderNo");								//����δ����ƾ֤
		String takeBrowNo = request.getParameter("takeBrowNo");							//�޷������ʾ
		String balanceAndTakeBrowNo = request.getParameter("balanceAndTakeBrowNo");		//�޷���������Ϊ��
		String showIsItem = request.getParameter("showIsItem");							//ֻ��ʾ��ϸ��Ŀ
		String showIsAccType = request.getParameter("showIsAccType");					//��ʾ�Է���Ŀ������Ŀ
		String showTakePeriod = request.getParameter("showTakePeriod");					//��ʾ�޷�������ڼ�ϼ�
		String showAccType = request.getParameter("showAccType");						//���Է���Ŀ������ʾ
		
		/* �߼����� */
		String showDate = request.getParameter("showDate");								//��ʾҵ������
		String showMessage = request.getParameter("showMessage");						//��ʾƾ֤ҵ����Ϣ
		String showItemDetail = request.getParameter("showItemDetail");					//��ʾ������Ŀ��ϸ
		String gradeShow = request.getParameter("gradeShow");							//�ּ���ʾ
		String itemSort = request.getParameter("itemSort");								//��Ŀ���
		String itemLevel = request.getParameter("itemLevel");							//��Ŀ����
		String itemCodeStart = request.getParameter("itemCodeStart");					//��Ŀ���뿪ʼ
		String itemCodeEnd = request.getParameter("itemCodeEnd");						//��Ŀ�������
		
		String keyWord = request.getParameter("keyWord");								//�ؼ��ֲ�ѯ
		String oldClassCode = request.getParameter("oldClassCode");						//�ɵĻ�ƿ�Ŀ
		
		/* ��ѯ���бұ� */
		Result result = mgt.queryCurrencyAll();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("currencyList", result.retVal);
		}
		//ƾ֤����
		result = new VoucherMgt().queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		
		/* ��ѯ��С������ڼ�������ҳ������֤�ڼ� */
		result = mgt.queryPeriod();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("periodStr", result.retVal);
		}
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		/* ����ڼ� */
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		if(comeMode == null){
			/* ��һ�ν���ʱ�����в�ѯ�������������� */
			return getForward(request, mapping, "reportAccDetIndex");
 		}
		
		/* �������������浽Map�� */
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
				
		
		/* ��������ƴװ��String */
		String conditions = dealConditions(conMap);
		request.setAttribute("conditions", conditions);
		request.setAttribute("conMap", conMap);
		request.setAttribute("oldClassCode", oldClassCode);
		
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/FinanceReportAction.do?optype=ReporttblAccDet");
		
		long time1 = System.currentTimeMillis();
		/* ��ѯ��ƿ�ĿĿ¼ */
		result = mgt.queryAccTypeInfoAll(showBanAccTypeInfo,showIsItem,showItemDetail,
				gradeShow,itemSort,itemLevel,itemCodeStart,itemCodeEnd,keyWord,mop,lg);
		long time2 = System.currentTimeMillis();
		System.out.println("��ϸ�������������ƿ�Ŀ��ִ̨��ʱ�䣺"+(time2-time1));
		/* ��ѯ�ɹ� */
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			StringBuffer folderTree = new StringBuffer("[");
			ArrayList list = (ArrayList)result.retVal;
			
			/* �Ի�ƿ�Ŀ���д�����ҳ����ʾ�� */
			for (int i = 0; i < list.size(); i++) {
				HashMap map1 = (HashMap) list.get(i);
				String classCodes = String.valueOf(map1.get("asClassCode"));
				if(showIsItem == null || "".equals(showIsItem)){
					//����ʾ��ϸ��Ŀ
					if (folderTree.toString().indexOf("asClassCode:\"" + classCodes.substring(0, classCodes.length() - 5) + "\"") == -1) {
						folderTree.append(this.folderTree(list, String.valueOf(map1.get("AccNumber"))+" - "+replace(String.valueOf(map1.get("AccName"))), classCodes, Integer
								.valueOf(map1.get("isCatalog").toString()),String.valueOf(map1.get("classCode")),showItemDetail,String.valueOf(map1.get("isItem")),
								Integer.valueOf(map1.get("isCalculateParent").toString())));
					}
				}else{
					//��ʾ��ϸ��Ŀ
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
	 * ���Ŀ¼
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
			// �����¼�
			folderTree.append("{ asClassCode:\"" + asclassCode + "\",classCode:\""+classCode+"\",name:\"" + folderName	+ "\",nocheck:true,");
			if(falg){
				//ѡ������ʾ������Ŀ��ϸ
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
	 * ������
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
	 * ��ϸ�����˿�Ŀ����
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
			//�����ڻ�ƿ�Ŀ��
			request.setAttribute("comeMode", "");
			return getForward(request, mapping, "reportAccDetDetail");
		}
		/* �������� */
		String dateType = request.getParameter("dateType");								//ʱ�����ͣ����ڼ��ѯ�������ڲ�ѯ��
		String periodYearStart = request.getParameter("periodYearStart");				//����ڼ俪ʼ��
		String periodStart = request.getParameter("periodStart");						//����ڼ俪ʼ
		String periodYearEnd = request.getParameter("periodYearEnd");					//����ڼ������
		String periodEnd = request.getParameter("periodEnd");							//����ڼ����
		String dateStart = request.getParameter("dateStart");							//���ڿ�ʼ
		String dateEnd = request.getParameter("dateEnd");								//���ڽ���
		String momentType = request.getParameter("momentType");							//�׶Σ����꣬���µȣ�
		String currencyName = request.getParameter("currencyName");						//�ұ�(id-��ң���-�ۺϱ�λ�ң�all-���бұ����ʽ��currency-���бұ�)
		String showInAccTypeInfo = request.getParameter("showInAccTypeInfo");			//��ʾ�Է���Ŀ
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//��ʾ���ÿ�Ŀ
		String binderNo = request.getParameter("binderNo");								//����δ����ƾ֤
		String takeBrowNo = request.getParameter("takeBrowNo");							//�޷������ʾ
		String balanceAndTakeBrowNo = request.getParameter("balanceAndTakeBrowNo");		//�޷���������Ϊ��
		String showIsAccType = request.getParameter("showIsAccType");					//��ʾ�Է���Ŀ������Ŀ
		String showTakePeriod = request.getParameter("showTakePeriod");					//��ʾ�޷�������ڼ�ϼ�
		String showAccType = request.getParameter("showAccType");						//���Է���Ŀ������ʾ
		
		currencyName = currencyName==null?"":currencyName;
		/* �߼����� */
		String showDate = request.getParameter("showDate");								//��ʾҵ������
		String showMessage = request.getParameter("showMessage");						//��ʾƾ֤ҵ����Ϣ
		String showItemDetail = request.getParameter("showItemDetail");					//��ʾ������Ŀ��ϸ
		String gradeShow = request.getParameter("gradeShow");							//�ּ���ʾ
		String itemSort = request.getParameter("itemSort");								//��Ŀ���
		String itemLevel = request.getParameter("itemLevel");							//��Ŀ����
		String itemCodeStart = request.getParameter("itemCodeStart");					//��Ŀ���뿪ʼ
		String itemCodeEnd = request.getParameter("itemCodeEnd");						//��Ŀ�������
		
		String orderby = request.getParameter("orderby");								//����
		
		
		/* �������������浽Map�� */
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
		 * ��ѯ��ϸ��������ϸ
		 */
		long time1 = System.currentTimeMillis();
		Result rs = mgt.queryAccDetData((HashMap<String, String>)conMap,classCode);
		long time2 = System.currentTimeMillis();
		System.out.println("��ϸ�����������ִ̨��ʱ�䣺"+(time2-time1));
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			/* ��ѯ�ɹ� */
			Object[] obj = (Object[])rs.retVal;
			HashMap initMap = (HashMap)obj[0];					//�ڳ����
			List accDetList = (ArrayList)obj[1];				//��ϸ
			String currType = String.valueOf(obj[2]);			//��ұ�ʶ��all-���б��ֶ���ʽ����-�ۺϱ�λ�ң�isBase-��λ�ң�currency-������ң����Id-������ң�
			List currencyList = (ArrayList)obj[3];				//�������List
			request.setAttribute("initMap", initMap);
			request.setAttribute("accDetList", accDetList);
			request.setAttribute("currType", currType);
			request.setAttribute("currencyList", currencyList);
			
			
			//���ݴ���
			int lastY = 0;
			int lastX = 0;
			int lastX1 = 0;
			String currFlag = "";
			String title = "";
			String lineStrInit = "periodBegin;";
			String lineStrDet = "BillDate;";
			//��ʾҵ������
			if(showDate != null && "1".equals(showDate)){
				lineStrInit += ";";
				lineStrDet += "BillDate;";
			}
			lineStrInit += ";RecordComment;";
			lineStrDet += "CredTypeOrderNo;RecordComment;";
			//��ʾ�Է���Ŀ
			if(showInAccTypeInfo != null && "1".equals(showInAccTypeInfo)){
				lineStrInit += ";";
				lineStrDet += "detailacccode;";
			}
			//��ʾƾ֤ҵ����Ϣ
			if(showMessage != null && "1".equals(showMessage)){
				lineStrInit += ";;";
				lineStrDet += "RefModuleName;RefBillTypeName;";
			}
			String line =  "sumDebitAmountBase;sumLendAmountBase;isflag;sumBalanceAmountBase";
			if("".equals(currType) || "isBase".equals(currType)){
				//��λ�һ����ۺϱ�λ��
				currFlag = "isBase";
				if("".equals(currType)){
					title += "<�ۺϱ�λ��>";
				}else{
					rs = mgt.queryCurrencyName(currencyName);
					title += rs.retVal;
				}
				lastX1 = 1;
			}else if("all".equals(currType)){
				//���бұ����ʽ
				String newLine = "";
				String[] str = line.split(";");
				for(String s : str){
					if(!"isflag".equals(s)){
						for(int l =0;l<currencyList.size();l++){
							String[] currStr = (String[])currencyList.get(l);
							String currency = currStr[0];
							if("true".equals(currStr[2])){
								//��λ��
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
				title += "<���бұ����ʽ>";
			}else{
				//���
				lastY = 1;
				lastX = 1;
				lastX1 = 2;
				line = "sumDebitAmount;sumDebitAmountBase;sumLendAmount;sumLendAmountBase;isflag;sumBalanceAmount;sumBalanceAmountBase";
				currFlag = "curr";
				rs = mgt.queryCurrencyName(currencyName);
				title += rs.retVal;
			}
			
			List newList = new ArrayList();
			
			//����ڳ����
			List perList = new ArrayList();
			lineStrInit = lineStrInit+line;
			String[] fieldStr = lineStrInit.split(";");
			for(String s : fieldStr){
				if("RecordComment".equals(s)){
					perList.add("�ڳ����");
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
			
			//��ȡ��ϸ
			lineStrDet = lineStrDet+line;
			fieldStr = lineStrDet.split(";");
			for(int i = 0;i<accDetList.size(); i++){
				HashMap oldmap = (HashMap)accDetList.get(i);
				List detailList = (ArrayList)oldmap.get("detail");
				
				//��ȡ������Ŀ����ϸ����
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
				
				//���ںϼ�
				perList = new ArrayList();
				for(String s : fieldStr){
					if("RecordComment".equals(s)){
						perList.add("���ںϼ�");
					}else if("BillDate".equals(s)){
						perList.add(oldmap.get("periodEnd"));
					}else{
						perList.add(oldmap.get(s));
					}
				}
				newList.add(perList);
				
				//�����ۼ�
				perList = new ArrayList();
				for(String s : fieldStr){
					if("RecordComment".equals(s)){
						perList.add("�����ۼ�");
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
			 * �����б���
			 */
			String dealtype = request.getParameter("dealtype");
			if(dealtype != null && "exportList".equals(dealtype)){
				
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath()+"\\��ϸ������.xls" ;
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				//���ñ���
				String titleName = "";
				if(dateType!=null && "1".equals(dateType)){
					//ѡ�����ڼ�
					titleName = "�ڼ䣺"+periodYearStart+"���"+periodStart+"����"+periodYearEnd+"���"+periodEnd+"��";
				}else if(dateType!=null && "2".equals(dateType)){
					//ѡ������
					titleName = "���ڣ�"+dateStart+" �� "+dateEnd;
				}else if(dateType!=null && "3".equals(dateType)){
					//ѡ��׶�
					titleName = "�׶����";
					if("year".equals(momentType)){
						titleName += "����";
					}else if("halfyear".equals(momentType)){
						titleName += "�������";
					}else if("threemonth".equals(momentType)){
						titleName += "���������";
					}else if("tomonth".equals(momentType)){
						titleName += "��һ����";
					}else if("month".equals(momentType)){
						titleName += "����";
					}else if("week".equals(momentType)){
						titleName += "����";
					}else if("threeday".equals(momentType)){
						titleName += "�������";
					}else if("day".equals(momentType)){
						titleName += "����";
					}else if("yesterday".equals(momentType)){
						titleName += "����";
					}
				}
				
				int count = 3;
				String locale =  this.getLocale(request).toString();
				//��strTitle��������ݵĸ�ʽΪ������������;��ʶ;�������;������У�
				List strTitle = new ArrayList();
				strTitle.add(setExportMap("name:����;lastY:"+lastY));
				if(showDate != null && "1".equals(showDate)){
					strTitle.add(setExportMap("name:ҵ������;lastY:"+lastY));
					count++;
				}
				strTitle.add(setExportMap("name:ƾ֤�ֺ�;lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.RecordComment", locale)+";lastY:"+lastY));
				if(showInAccTypeInfo != null && "1".equals(showInAccTypeInfo)){
					//��ʾ�Է���Ŀ
					strTitle.add(setExportMap("name:�Է���Ŀ;lastY:"+lastY));
					count++;
				}
				if(showMessage != null && "1".equals(showMessage)){
					strTitle.add(setExportMap("name:ϵͳģ��;lastY:"+lastY));
					strTitle.add(setExportMap("name:ҵ������;lastY:"+lastY));
					count=count+2;
				}
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)+";lastX:"+lastX));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.LendAmount", locale)+";lastX:"+lastX));
				strTitle.add(setExportMap("name:���;lastX:"+lastX1));
				if("curr".equals(currFlag)){
					//���
					for(int l=0;l<3;l++){
						if(l==2){
							strTitle.add(setExportMap("name:"));
						}
						if(l == 0){
							strTitle.add(setExportMap("name:ԭ��;nextLine:1;nextX:"+count));
						}else{
							strTitle.add(setExportMap("name:ԭ��"));
						}
						strTitle.add(setExportMap("name:��λ��"));
					}
				}else if("all".equals(currFlag)){
					//���бұ����ʽ
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
						strTitle.add(setExportMap("name:��λ��"));
					}
				}
				
				rs = mgt.exportBanlance(fos, "��ϸ������",titleName, strTitle, newList);
				fos.close();
				if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("��ϸ������.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
							getMessage(request, "com.export.success.over")+fileName)
							.setBackUrl("/FinanceReportAction.do?optype=ReporttblAccDet&comeMode=1&"+conditions).setNotAutoBack()
							.setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
			}else{
				//�������ݽ��б��浽�ڴ��д�ӡ��ȡ����
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
			/* ��ѯʧ�� */
			EchoMessage.error().add(getMessage(
                    request, "sms.query.error")).
                    setBackUrl("/FinanceReportAction.do?optype=ReporttblAccDet&type=detail").
                    setAlertRequest(request);
		}
		request.setAttribute("comeMode", "comeMode");
		return getForward(request, mapping, "reportAccDetDetail");
	}
	
	/**
	 * �����ߵĻ�ƿ�Ŀ�Ǻ������Ŀʱ���������еĺ����Ŀ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportAccDetLoadIsItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String classCode = request.getParameter("classCode");							//��Ŀ
		String asClassCode = request.getParameter("asClassCode");
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//��ʾ���ÿ�Ŀ
		String showItemDetail = request.getParameter("showItemDetail");					//��ʾ������Ŀ��ϸ
		String gradeShow = request.getParameter("gradeShow");							//�ּ���ʾ
		String itemSort = request.getParameter("itemSort");								//��Ŀ���
		String itemLevel = request.getParameter("itemLevel");							//��Ŀ����
		String itemCodeStart = request.getParameter("itemCodeStart");					//��Ŀ���뿪ʼ
		String itemCodeEnd = request.getParameter("itemCodeEnd");						//��Ŀ�������
		
		/* ��ѯ����Ŀ�Ŀ���� */
		Result result = mgt.queryAccTypeIsItem(classCode,asClassCode, showBanAccTypeInfo, showItemDetail, gradeShow,
				itemSort, itemLevel, itemCodeStart, itemCodeEnd);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//��ѯ�ɹ�
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
	 * ����ʽ��ϸ���б�
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
		//ƾ֤����
		Result rs = new VoucherMgt().queryVoucherSetting();
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) rs.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		
		List currencyList = new ArrayList();
		/* ��ѯ���б������� */
		rs = mgt.queryCurrencyAll();
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			currencyList = (ArrayList)rs.retVal;
			request.setAttribute("currencyList", currencyList);
		}
		
		/* ����ڼ� */
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		
		//��ѯ�����ڼ䣬ҳ���������ڼ�ʱ�����жϴ���
		rs = mgt.getCurrentlyPeriod();
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("beginPeriod", rs.retVal);
		}

		if(comeMode == null){
			/* ��һ�ν��� */
			return getForward(request, mapping, "reportAccAllDetList");
 		}
		/* �������� */
		String accName = request.getParameter("accName");								//����������
		String periodYearStart = request.getParameter("periodYearStart");				//����ڼ俪ʼ��
		String periodStart = request.getParameter("periodStart");						//����ڼ俪ʼ
		String periodYearEnd = request.getParameter("periodYearEnd");					//����ڼ������
		String periodEnd = request.getParameter("periodEnd");							//����ڼ����
		String showFormCurrency = request.getParameter("showFormCurrency");				//��ʾԭ�ҽ��
		String showOperationBranch = request.getParameter("showOperationBranch");		//ҵ���¼������ʾ
		String binderNo = request.getParameter("binderNo");								//����δ����ƾ֤
		String showPeriodBalaBase = request.getParameter("showPeriodBalaBase");			//��ʾ��ϸ��Ŀ��ĩ���
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//��ʾ���ÿ�Ŀ
		String takeBrowNo = request.getParameter("takeBrowNo");							//�޷������ʾ
		String balanceAndTakeBrowNo = request.getParameter("balanceAndTakeBrowNo");		//���Ϊ�����޷������ʾ
		
		String itemSort = request.getParameter("itemSort");								//��Ŀ���
		String acctypeCodeStart = request.getParameter("acctypeCodeStart");				//��Ŀ���뿪ʼ
		String acctypeCodeEnd = request.getParameter("acctypeCodeEnd");					//��Ŀ�������
		
		/* �������������浽Map�� */
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
		
		/* ������Ƶ�id��ѯ�û��Զ�����Ƶļ�¼ */
		String[] designValue = null;
		rs = mgt.queryDesignById(accName);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			designValue = (String[])rs.retVal;
		}
		/**
		 * ��ѯ����ʽ��ϸ���б�
		 */
		long time1 = System.currentTimeMillis();
		rs = mgt.queryAllDetData((HashMap<String, String>)conMap,designValue);
		long time2 = System.currentTimeMillis();
		System.out.println("����ʽ��ϸ�˺�ִ̨��ʱ�䣺"+(time2-time1));
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			
			HashMap allDetDataMap = (HashMap)rs.retVal;
			request.setAttribute("allDetData", allDetDataMap);
			request.setAttribute("designBean", designValue);
			
			
			/* ҵ����(�����б�����) */
			String type = request.getParameter("type");
			if(type!= null && "exportList".equals(type)){
				
				//���в�ѯ��������
				HashMap initData = new HashMap();												//�ڳ�����Map
				List periodList = new ArrayList();												//��ѯ�����л���ڼ�
				if(allDetDataMap != null && allDetDataMap.size()>0){
					initData = (HashMap)allDetDataMap.get("initData");
					periodList = (ArrayList)allDetDataMap.get("periodList");
				}
				
				//�����ܷ���������
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath()+"\\����ʽ��ϸ��.xls" ;
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				//���ñ���
				String titleName = "�ڼ䣺"+periodYearStart+"���"+periodStart+"����"+periodYearEnd+"���"+periodEnd+"��";
				titleName += "  ����ʽ���ƣ�"+designValue[2];
				List newList = new ArrayList();
				
				/**
				 * ���������õĶ���ʽ��ʽ
				 */
				int debitCount = 0;										//�û����õĿ�Ŀ�跽����
				int creditCount = 0;									//�û����õĿ�Ŀ��������
				List setAccNumberList = new ArrayList();				//�û����õĿ�Ŀ��ż���
				List setNameDebitList = new ArrayList();				//�û����õĽ跽��Ŀ��������
				List setNameLendList = new ArrayList();					//�û����õĴ�����Ŀ��������
				String[] columndataStrs = designValue[3].split("//");
				for(String columndataStr : columndataStrs){
					String[] columndata = columndataStr.split(";");
					if("1".equals(columndata[0])){
						//�跽
						debitCount ++;
						setNameDebitList.add(columndata[2]);
					}else if("2".equals(columndata[0])){
						//����
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
				
				//�Ƿ���ʾԭ�ҽ��
				boolean showCurr = false;
				boolean isCurr = false;
				if(showFormCurrency != null && "1".equals(showFormCurrency)){
					showCurr = true;
				}
				List setCurrList = new ArrayList();
				if(currsStrs.length()>0){
					//����ѡ������
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
				
				//ͷ����ʾ��������ͷ
				String locale =  this.getLocale(request).toString();
				List strTitle = new ArrayList();
				strTitle.add(setExportMap("name:����;lastY:"+lastY));
				strTitle.add(setExportMap("name:ƾ֤�ֺ�;lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.RecordComment", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)+";lastY:"+lastY1+";lastX:"+currCount));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.LendAmount", locale)+";lastY:"+lastY1+";lastX:"+currCount));
				strTitle.add(setExportMap("name:���;lastX:2;lastY:"+lastY1));
				if(debitCount>0){
					strTitle.add(setExportMap("name:�跽;lastX:"+(debitCount*currCount-1)));					
				}
				if(creditCount>0){
					strTitle.add(setExportMap("name:����;lastX:"+(creditCount*currCount-1)));					
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
//						//�跽
//						strList.add("curr__"+setjdflagOrAccNumber[1]+"_sumDebitAmount");
//					}else{
//						//����
//						strList.add("curr__"+setjdflagOrAccNumber[1]+"_sumLendAmount");
//					}
//				}
				//�����ڳ�����
				List preList = new ArrayList();
				preList.add(allDetDataMap.get("AccDate"));
				preList.add("");
				preList.add("�ڳ����");

//				for(Object s : strList){
//					preList.add(initData.get(s));
//				}
				newList.add(preList);
				
				//ѭ���ڼ����������
				String[] str = null;
//				for(int i=0;i<periodList.size();i++){
//					HashMap periodMap = (HashMap)periodList.get(i);
//					
//					//�˻���ڼ��ƾ֤��ϸ
//					List dealList = (ArrayList)allDetDataMap.get("detail_"+periodMap.get("AccYear")+"_"+periodMap.get("AccPeriod"));
//					if(dealList != null && dealList.size()>0){
//						str = new String[]{"AccDate","credTypeIdOrderNo","RecordComment","DebitAmount","LendAmount","isflag","sumAmount"};
//						//������ϸ
//						for(HashMap detailMap : (ArrayList<HashMap>)dealList){
//							preList = new ArrayList();
//							for(String s : str){
//								preList.add(detailMap.get(s));
//							}
//							//�����������
//							for(int l =0;l<setAccNumberList.size();l++){
//								String setAccNumber = String.valueOf(setAccNumberList.get(l));
//								String[] setjdflagOrAccNumber = setAccNumber.split(";");
//								if("1".equals(setjdflagOrAccNumber[0])){
//									//�跽
//									preList.add(detailMap.get("curr__"+setjdflagOrAccNumber[1]+"_DebitAmount"));
//								}else{
//									//����
//									preList.add(detailMap.get("curr__"+setjdflagOrAccNumber[1]+"_LendAmount"));
//								}
//							}
//							newList.add(preList);
//						}
//					}
//					
//					//�����ںϼƽ��,�ͱ����ۼƽ��
//					String[] strName = new String[]{"period","CredYear"};
//					for(int k =0;k<strName.length;k++){
//						HashMap dealMap = (HashMap)allDetDataMap.get(strName[k]+"_"+periodMap.get("AccYear")+"_"+periodMap.get("AccPeriod"));
//						if(dealMap != null && dealMap.size()>0){
//							str = new String[]{"periodEnd","","RecordComment","totalsumDebitAmount","totalsumLendAmount","totalBalanceisflag","totalBalance"};
//							//������ϸ
//							preList = new ArrayList();
//							for(String s : str){
//								if("RecordComment".equals(s)){
//									if(k == 0){
//										preList.add("���ںϼ�");
//									}else{
//										preList.add("�����ۼ�");
//									}
//								}else{
//									preList.add(dealMap.get(s));
//								}
//							}
//							//�����������
//							for(int l =0;l<setAccNumberList.size();l++){
//								String setAccNumber = String.valueOf(setAccNumberList.get(l));
//								String[] setjdflagOrAccNumber = setAccNumber.split(";");
//								if("1".equals(setjdflagOrAccNumber[0])){
//									//�跽
//									preList.add(dealMap.get("curr__"+setjdflagOrAccNumber[1]+"_sumDebitAmount"));
//								}else{
//									//����
//									preList.add(dealMap.get("curr__"+setjdflagOrAccNumber[1]+"_sumLendAmount"));
//								}
//							}
//							newList.add(preList);
//						}
//					}
//				}
				
				rs = mgt.exportBanlance(fos, "����ʽ��ϸ��",titleName, strTitle, newList);
				fos.close();
				if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("����ʽ��ϸ��.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
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
	 * ����ʽ��ϸ������б�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportDesignList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		/* ��ѯ���ж���ʽ��ϸ���б����� */
		String keyWord = request.getParameter("keyWord");				//�ؼ�������
		Result rs = mgt.queryAccDesign(keyWord);
		request.setAttribute("keyWord", keyWord);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//��ѯ�ɹ�
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
	 * ����ʽ��ϸ����ƽ���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportDesign(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String dealType = request.getParameter("dealType");			//��ƽ��洦�����ͣ�updatePre �޸�ǰ��addPre ���ǰ��
		
		/* ��ѯ���б������� */
		Result rs = mgt.queryCurrencyAll();
		request.setAttribute("currencyList", rs.retVal);
		
		request.setAttribute("dealType", dealType);
		String id = request.getParameter("id");
		if(dealType != null && "addPre".equals(dealType)){
			//���ǰ
			return getForward(request, mapping, "reportDesign");
		}else if(dealType != null && "updatePre".equals(dealType)){
			//�޸�ǰ �Ȳ�ѯ����
			rs = mgt.queryDesignById(id);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				//��ѯ�ɹ�
				request.setAttribute("designBean", rs.retVal);
			}
			return getForward(request, mapping, "reportDesign");
		}
		
		//������������ӣ��޸ģ�
		String code = request.getParameter("accType");						//��ƿ�Ŀ���
		String name = request.getParameter("accName");						//����
		String[] jdflag = request.getParameterValues("td_jdflag");			//�����������
		String[] td_acctype = request.getParameterValues("td_acctype");		
		String[] td_accName = request.getParameterValues("td_accName");
		
		String[] currency_id = request.getParameterValues("currency_id");			//����
		String levelsetting = request.getParameter("levelsetting");					//���������ö�����
		String levelvalue = request.getParameter("levelvalue");						//����ֵ
		String columndata = "";
		String currencydata = "";
		if(td_acctype!=null){
			for(int i=0;i<td_acctype.length;i++){
				String acctypevalue = td_acctype[i];
				if(acctypevalue==null || "".equals(acctypevalue)){
					//�����ڱ��ʱɾ��������¼
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
			//���/�޸�ʧ��
			String msg = "common.msg.addFailture";
			if(id != null && !"".equals(id)){
				msg = "common.msg.updateErro";
			}
			EchoMessage.error().add(getMessage(request, msg)).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * ɾ������ʽ��ϸ����Ƽ�¼
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward delDesign(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String id = request.getParameter("id");						//��Ƽ�¼Id
		Result rs = mgt.delAccDesign(id);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//��ѯ�ɹ�
			if(Integer.valueOf(rs.retVal.toString())>0){
				request.setAttribute("msg", "ok");
			}
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * ��ѯ�û��Զ�����������
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
			//��ѯ�ɹ�
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
	 * ajax��������
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
			//��ȡ���¼���ƿ�Ŀ
			String accType = request.getParameter("accType");
			String level = request.getParameter("level");
			String itemSort = request.getParameter("itemSort");
			Result rs = mgt.queryAccTypeChild(accType,level,itemSort);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				//��ѯ�ɹ�
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
	 * ���ݻ�ƿ�ĿAccNumber��ѯ��Ŀ��Ϣ�������Ƿ���㲿�ţ�ְԱ���ֿ⣬�ͻ�����Ӧ�̵ȣ�
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
	 * ������Ŀ����������ҳ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reporttblAccCalculateIndex(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		/* �������� */
		String periodYearStart = request.getParameter("periodYearStart");				//����ڼ俪ʼ��
		String periodStart = request.getParameter("periodStart");						//����ڼ俪ʼ
		String periodYearEnd = request.getParameter("periodYearEnd");					//����ڼ������
		String periodEnd = request.getParameter("periodEnd");							//����ڼ����
		String itemSort = request.getParameter("itemSort");								//��Ŀ���
		String itemCodeStart = request.getParameter("itemCodeStart");					//��Ŀ���뿪ʼ
		String itemCodeEnd = request.getParameter("itemCodeEnd");						//��Ŀ�������
		String accCodeStart = request.getParameter("accCodeStart");						//��ƿ�Ŀ��ʼ
		String accCodeEnd = request.getParameter("accCodeEnd");							//��ƿ�Ŀ����
		String accCodeLevel = request.getParameter("accCodeLevel");						//��Ŀ����
		String currencyName = request.getParameter("currencyName");						//�ұ�
		String accTypeNo = request.getParameter("accTypeNo");							//��Ŀ�޷������ʾ
		String takeBrowNo = request.getParameter("takeBrowNo");							//�޷������ʾ
		String binderNo = request.getParameter("binderNo");								//����δ����ƾ֤
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//��ʾ���ÿ�Ŀ
		
		String keyWord = request.getParameter("keyWord");								//�ؼ��ֲ�ѯ
		String oldCode = request.getParameter("oldCode");								//�ɵı��
		
		
		/* ��ѯ���бұ� */
		Result result = mgt.queryCurrencyAll();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("currencyList", result.retVal);
		}

		//ƾ֤����
		result = new VoucherMgt().queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		
		/* ����ڼ� */
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		
		//��ѯ�����ڼ䣬ҳ���������ڼ�ʱ�����жϴ���
		result = mgt.getCurrentlyPeriod();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("beginPeriod", result.retVal);
		}
		
		if(comeMode == null){
			/* ��һ�ν���ʱ�����в�ѯ�������������� */
			return getForward(request, mapping, "reporttblAccCalculateIndex");
 		}
		
		/* �������������浽Map�� */
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
		/* ��������ƴװ��String */
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
		
		/* ��ѯ���������ĺ�����Ŀ */
		Result rs = mgt.quertItemDate((HashMap)conMap);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//��ѯ�ɹ�
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
	 * ������Ŀ�������˵��������Ŀ��������
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
			//�����ں�����Ŀ��
			request.setAttribute("comeMode", "");
			return getForward(request, mapping, "reporttblAccCalculateDetail");
		}
		
		/* �������� */
		String periodYearStart = request.getParameter("periodYearStart");				//����ڼ俪ʼ��
		String periodStart = request.getParameter("periodStart");						//����ڼ俪ʼ
		String periodYearEnd = request.getParameter("periodYearEnd");					//����ڼ������
		String periodEnd = request.getParameter("periodEnd");							//����ڼ����
		String itemSort = request.getParameter("itemSort");								//��Ŀ���
		String itemCodeStart = request.getParameter("itemCodeStart");					//��Ŀ���뿪ʼ
		String itemCodeEnd = request.getParameter("itemCodeEnd");						//��Ŀ�������
		String accCodeStart = request.getParameter("accCodeStart");						//��ƿ�Ŀ��ʼ
		String accCodeEnd = request.getParameter("accCodeEnd");							//��ƿ�Ŀ����
		String accCodeLevel = request.getParameter("accCodeLevel");						//��Ŀ����
		String currencyName = request.getParameter("currencyName");						//�ұ�
		String accTypeNo = request.getParameter("accTypeNo");							//��Ŀ�޷������ʾ
		String takeBrowNo = request.getParameter("takeBrowNo");							//�޷������ʾ
		String binderNo = request.getParameter("binderNo");								//����δ����ƾ֤
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//��ʾ���ÿ�Ŀ
		
		/* �������������浽Map�� */
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
		 * ��ѯ������Ŀ����������ϸ
		 */
		long time1 = System.currentTimeMillis();
		Result rs = mgt.queryAccCalculateDetail((HashMap<String, String>)conMap,classCode,mop,loginBean);
		long time2 = System.currentTimeMillis();
		System.out.println("������Ŀ�������������ִ̨��ʱ�䣺"+(time2-time1));
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			/* ��ѯ�ɹ� */
			Object[] obj = (Object[])rs.retVal;
			
			List accDetList = (ArrayList)obj[0];		//��ƿ�Ŀ�͸����ڼ�
			HashMap valuesMap = (HashMap)obj[1];		//���������
			String currType = String.valueOf(obj[2]);	//�������
			
			request.setAttribute("accDetList", accDetList);
			request.setAttribute("accDetHash", valuesMap);
			request.setAttribute("currType", currType);
			
			
			//������װ�õ����е�����
			List newList = new ArrayList();
			List perList = new ArrayList();
			/**
			 * ������װ
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
						
						//��ȡ������ݣ�key=classCode_year_period��
						HashMap values = (HashMap)valuesMap.get(accDetMap.get("classCode")+"_"+periodMap.get("AccYear")+"_"+periodMap.get("AccPeriod"));
						if(values == null){
							values = new HashMap();
						}
						//ѭ����������ݵ�list��
						for(String s : strs){
							perList.add(values.get(s));
						}
						newList.add(perList);
					}
				}
			}
			
			/**
			 * �����б���
			 */
			String dealtype = request.getParameter("dealtype");
			if(dealtype != null && "exportList".equals(dealtype)){
				
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath()+"\\������Ŀ��������.xls" ;
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				//���ñ���
				String titleName = "��Ŀ���";
				if("DepartmentCode".equals(itemSort)){
					titleName += "����";
				}else if("EmployeeID".equals(itemSort)){
					titleName += "ְԱ";
				}else if("StockCode".equals(itemSort)){
					titleName += "�ֿ�";
				}else if("ClientCode".equals(itemSort)){
					titleName += "�ͻ�";
				}else if("SuplierCode".equals(itemSort)){
					titleName += "��Ӧ��";
				}else if("ProjectCode".equals(itemSort)){
					titleName += "��Ŀ";
				}
				titleName = "   �ڼ䣺"+periodYearStart+"���"+periodStart+"����"+periodYearEnd+"���"+periodEnd+"��";
				
				String locale =  this.getLocale(request).toString();
				List strTitle = new ArrayList();
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccMain.Period", locale)+";lastY:1"));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccNumber", locale)+";lastY:1"));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccName", locale)+";lastY:1"));
				strTitle.add(setExportMap("name:�ڳ����;lastX:1"));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.LendAmount", locale)));
				strTitle.add(setExportMap("name:�����ۼƽ跽"));
				strTitle.add(setExportMap("name:�����ۼƴ���"));
				strTitle.add(setExportMap("name:��ĩ���;lastX:1"));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.JdFlag", locale)+";nextLine:1;nextX:3"));
				strTitle.add(setExportMap("name:���"));
				for(int i=0;i<4;i++){
					strTitle.add(setExportMap("name:���"));
				}
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.JdFlag", locale)));
				strTitle.add(setExportMap("name:���"));
				rs = mgt.exportBanlance(fos, "������Ŀ��������",titleName, strTitle, newList);
				fos.close();
				if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("������Ŀ��������.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
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
			/* ��ѯʧ�� */
			EchoMessage.error().add(getMessage(
                    request, "sms.query.error")).
                    setBackUrl("/FinanceReportAction.do?optype=ReporttblAccCalculate&type=detail").
                    setAlertRequest(request);
		}
		request.setAttribute("comeMode", "comeMode");
		return getForward(request, mapping, "reporttblAccCalculateDetail");
	}
	
	/**
	 * ������Ŀ��ϸ����ҳ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reporttblAccCalculateDetIndex(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		/* �������� */
		String dateType = request.getParameter("dateType");								//ʱ�����ͣ����ڼ��ѯ�������ڲ�ѯ��
		String periodYearStart = request.getParameter("periodYearStart");				//����ڼ俪ʼ��
		String periodStart = request.getParameter("periodStart");						//����ڼ俪ʼ
		String periodYearEnd = request.getParameter("periodYearEnd");					//����ڼ������
		String periodEnd = request.getParameter("periodEnd");							//����ڼ����
		String dateStart = request.getParameter("dateStart");							//���ڿ�ʼ
		String dateEnd = request.getParameter("dateEnd");								//���ڽ���
		String accCode = request.getParameter("accCode");								//��ƿ�Ŀ
		String currencyName = request.getParameter("currencyName");						//�ұ�
		String itemSort = request.getParameter("itemSort");								//��Ŀ���
		String itemCodeStart = request.getParameter("itemCodeStart");					//��Ŀ���뿪ʼ
		String itemCodeEnd = request.getParameter("itemCodeEnd");						//��Ŀ�������
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//��ʾ���ÿ�Ŀ
		String accTypeNo = request.getParameter("accTypeNo");							//��Ŀ�޷������ʾ
		String takeBrowNo = request.getParameter("takeBrowNo");							//�޷������ʾ
		String binderNo = request.getParameter("binderNo");								//����δ����ƾ֤
		
		String keyWord = request.getParameter("keyWord");								//�ؼ��ֲ�ѯ
		String oldCode = request.getParameter("oldCode");								//�ɵı��
		
		
		/* ��ѯ���бұ� */
		Result result = mgt.queryCurrencyAll();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("currencyList", result.retVal);
		}
		
		//ƾ֤����
		result = new VoucherMgt().queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		
		/* ����ڼ� */
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);

		if(comeMode == null){
			/* ��һ�ν���ʱ�����в�ѯ�������������� */
			return getForward(request, mapping, "reporttblAccCalculateDetIndex");
 		}
		
		/* �������������浽Map�� */
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
		/* ��������ƴװ��String */
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
		//������������
		request.setAttribute("conditions", conditions);
		
		/* ��ѯ���������ĺ�����Ŀ */
		Result rs = mgt.quertItemDate((HashMap<String,String>)conMap);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//��ѯ�ɹ�
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
	 * ������Ŀ��ϸ������
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
			//�����ں�����Ŀ��
			request.setAttribute("comeMode", "");
			return getForward(request, mapping, "reporttblAccCalculateDetDetail");
		}
		
		/* �������� */
		String dateType = request.getParameter("dateType");								//ʱ�����ͣ����ڼ��ѯ�������ڲ�ѯ��
		String periodYearStart = request.getParameter("periodYearStart");				//����ڼ俪ʼ��
		String periodStart = request.getParameter("periodStart");						//����ڼ俪ʼ
		String periodYearEnd = request.getParameter("periodYearEnd");					//����ڼ������
		String periodEnd = request.getParameter("periodEnd");							//����ڼ����
		String dateStart = request.getParameter("dateStart");							//���ڿ�ʼ
		String dateEnd = request.getParameter("dateEnd");								//���ڽ���
		String accCode = request.getParameter("accCode");								//��ƿ�Ŀ
		String currencyName = request.getParameter("currencyName");						//�ұ�
		String itemSort = request.getParameter("itemSort");								//��Ŀ���
		String itemCodeStart = request.getParameter("itemCodeStart");					//��Ŀ���뿪ʼ
		String itemCodeEnd = request.getParameter("itemCodeEnd");						//��Ŀ�������
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//��ʾ���ÿ�Ŀ
		String accTypeNo = request.getParameter("accTypeNo");							//��Ŀ�޷������ʾ
		String takeBrowNo = request.getParameter("takeBrowNo");							//�޷������ʾ
		String binderNo = request.getParameter("binderNo");								//����δ����ƾ֤
		
		/* �������������浽Map�� */
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
		 * ��ѯ������Ŀ��ϸ������
		 */
		long time1 = System.currentTimeMillis();
		Result rs = mgt.queryAccCalculateDetDetail((HashMap<String, String>)conMap,classCode,mop,loginBean);
		long time2 = System.currentTimeMillis();
		System.out.println("������Ŀ��ϸ�������ִ̨��ʱ�䣺"+(time2-time1));
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			/* ��ѯ�ɹ� */
			Object[] o = (Object[])rs.retVal;
			List periodList = (ArrayList)o[0];
			HashMap detailData = (HashMap)o[1];
			request.setAttribute("periodList", periodList);
			request.setAttribute("detailData", detailData);
			if(currencyName!=null && !"".equals(currencyName)){
				//��֤�Ƿ��Ǳ�λ��
				if("all".equals(currencyName)){
					/* ��ѯ���б��� */
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
			//��ѯ�����Ļ�ƿ�Ŀ
			rs = mgt.queryAccType(accCode);
			String[] acctypeStr = (String[])rs.retVal;
			
			List newList = new ArrayList();
			
			/**
			 * �������ݴ�������ݴ洢��newList��
			 */
			/**
			 * ��ȡ�ڳ����
			 */
			
			
			//�ڳ����
			HashMap initMap = (HashMap)detailData.get("initMap");
			List preList = new ArrayList();
			preList.add("");
			preList.add(initMap.get("accYear")+"."+initMap.get("accPeriod"));
			preList.add("");
			preList.add("�ڳ����");
			preList.add("");
			preList.add("");
			preList.add(initMap.get("isInitflag"));
			preList.add(initMap.get("sumAmount"));
			newList.add(preList);
			
			//������ϸ�ͱ��ںϼ� �����ۼ�
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
						newList.add(preList);							//�����ϸ
					}
				}
				
				//���ںϼ�
				preList = new ArrayList();
				preList.add("");
				preList.add(periodMap.get("AccYear")+"."+periodMap.get("AccPeriod"));
				preList.add("");preList.add("���ںϼ�");
				str = new String[]{"sumDebitAmount","sumLendAmount","isflag","sumDCBala"};
				for(String s : str){
					preList.add(curMap.get(s));
				}
				newList.add(preList);							//��ӱ��ںϼ�
				
				//�����ۼ�
				preList = new ArrayList();
				preList.add("");
				preList.add(periodMap.get("AccYear")+"."+periodMap.get("AccPeriod"));
				preList.add("");preList.add("�����ۼ�");
				str = new String[]{"yearsumDebitAmount","yearsumLendAmount","yearisflag","yearsumDCBala"};
				for(String s : str){
					preList.add(curMap.get(s));
				}
				newList.add(preList);							//��ӱ����ۼ�
			}
			
			/**
			 * ҵ����(�����б�����)
			 */
			String dealtype = request.getParameter("dealtype");
			if(dealtype!= null && "exportList".equals(dealtype)){
				//�����ܷ���������
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath()+"\\������Ŀ��ϸ��.xls" ;
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				//���ñ���
				String titleName = "��ƿ�Ŀ��"+acctypeStr[0]+" - "+acctypeStr[1]+"  ��Ŀ���";
				if("DepartmentCode".equals(itemSort)){
					titleName += "����";
				}else if("EmployeeID".equals(itemSort)){
					titleName += "ְԱ";
				}else if("StockCode".equals(itemSort)){
					titleName += "�ֿ�";
				}else if("ClientCode".equals(itemSort)){
					titleName += "�ͻ�";
				}else if("SuplierCode".equals(itemSort)){
					titleName += "��Ӧ��";
				}else if("ProjectCode".equals(itemSort)){
					titleName += "��Ŀ";
				}
				if(dateType != null && "1".equals(dateType)){
					//�ڼ��ѯ
					titleName += "   �ڼ䣺"+periodYearStart+"���"+periodStart+"����"+periodYearEnd+"���"+periodEnd+"��";
				}else if(dateType != null && "2".equals(dateType)){
					//���ڲ�ѯ
					titleName += "   ���ڣ�"+dateStart+"��"+dateEnd;
				}

				String locale =  this.getLocale(request).toString();
				
				//������ͷ
				List strTitle = new ArrayList();
				strTitle.add(setExportMap("name:����"));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccMain.Period", locale)));
				strTitle.add(setExportMap("name:ƾ֤�ֺ�"));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.RecordComment", locale)));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.LendAmount", locale)));
				strTitle.add(setExportMap("name:���;lastX:1"));
				rs = mgt.exportBanlance(fos, "������Ŀ��ϸ��",titleName, strTitle, newList);
				fos.close();
				if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("������Ŀ��ϸ��.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
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
			/* ��ѯʧ�� */
			EchoMessage.error().add(getMessage(
                    request, "sms.query.error")).
                    setBackUrl("/FinanceReportAction.do?optype=ReporttblAccCalculateDet&type=detail").
                    setAlertRequest(request);
		}
		request.setAttribute("comeMode", "comeMode");
		return getForward(request, mapping, "reporttblAccCalculateDetDetail");
	}
	
	/**
	 * ����ƽ����ѯ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reporttblTrialBalance(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		/* �������� */
		String periodYearStart = request.getParameter("periodYearStart");				//����ڼ���
		String periodStart = request.getParameter("periodStart");						//����ڼ�
		String levelStart = request.getParameter("levelStart");							//��Ŀ����
		String currencyName = request.getParameter("currencyName");						//�ұ�
		String showDetail = request.getParameter("showDetail");							//ֻ��ʾ��ϸ
		String showItemDetail = request.getParameter("showItemDetail");					//��ʾ������Ŀ��ϸ
		String binderNo = request.getParameter("binderNo");								//����δ����ƾ֤
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//��ʾ���ÿ�Ŀ
		String accTypeNoItem = request.getParameter("accTypeNoItem");					//��Ŀ����¼����ʾ
		
		/* ��ѯ���бұ� */
		List currencyList = new ArrayList();
		Result result = mgt.queryCurrencyAll();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			currencyList = (ArrayList)result.retVal;
			request.setAttribute("currencyList", currencyList);
		}

		//ƾ֤����
		result = new VoucherMgt().queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		
		
		/* �õ�����ڼ� */
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		
		//��ѯ�����ڼ䣬ҳ���������ڼ�ʱ�����жϴ���
		result = mgt.getCurrentlyPeriod();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("beginPeriod", result.retVal);
		}
		
		if(comeMode == null){
			/* ��һ�ν���ʱ�����в�ѯ�������������� */
			return getForward(request, mapping, "reporttblTrialBalance");
 		}
		
		/* �������������浽Map�� */
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
		
		/* ��ѯ���� */
		long time1 = System.currentTimeMillis();
		result = mgt.accTrialBalance((HashMap<String, String>)conMap,mop,lg);
		long time2 = System.currentTimeMillis();
		System.out.println("����ƽ����ѯ����ִ��ʱ�䣺"+(time2-time1));
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//��ѯ�ɹ�
			Object[] obj = (Object[])result.retVal;
			List list = (ArrayList)obj[0];
			HashMap totalMap = (HashMap)obj[1];
			String currType = String.valueOf(obj[2]);
			request.setAttribute("accTypeInfoList", list);
			request.setAttribute("totalData", obj[1]);
			request.setAttribute("currType", currType);
			
			//���ñ���
			String titleName = "�ڼ䣺"+periodYearStart+"���"+periodStart+"��";
			if("true".equals(totalMap.get("flag"))){
				titleName += "      ������ƽ��";
			}else{
				titleName += "      ��������ƽ��";
			}
			titleName += "          �ұ�";
			/**
			 * �����ݱ��浽newList��
			 */
			int lastY = 1;
			int lastX = 1;
			int lastX1 = 0;
			String currFlag = "";
			List newList = new ArrayList();
			String lineStr = "AccNumber;AccFullName;";
			String printStr = "No;AccNumber;AccFullName;";
			if("".equals(currType) || "isBase".equals(currType)){
				//��λ�һ����ۺϱ�λ��
				lineStr += "PeriodIniDebitBase;PeriodIniCreditBase;PeriodDebitSumBase;PeriodCreditSumBase;PeriodDebitBalaBase;PeriodCreditBalaBase";
				currFlag = "isBase";
				if("".equals(currType)){
					titleName += "<�ۺϱ�λ��>";
				}else{
					result = mgt.queryCurrencyName(currencyName);
					titleName += result.retVal;
				}
				printStr = "No;"+lineStr;
			}else if("all".equals(currType)){
				//���бұ����ʽ
				String[] cur1 = new String[]{"PeriodIniDebit","PeriodIniCredit","PeriodDebitSum","PeriodCreditSum","PeriodDebitBala","PeriodCreditBala"};
				String[] cur2 = new String[]{"PeriodIniDebitBase","PeriodIniCreditBase","PeriodDebitSumBase","PeriodCreditSumBase","PeriodDebitBalaBase","PeriodCreditBalaBase"};
				for(int k = 0;k<cur1.length;k++){
					for(int l =0;l<currencyList.size();l++){
						String[] currStr = (String[])currencyList.get(l);
						String currency = currStr[0];
						if("true".equals(currStr[2])){
							//��λ��
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
				titleName += "<���бұ����ʽ>";
			}else{
				//���
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
				//�����б�����
				perList.add(count);
				for(String s : strs){
					perList.add(oldmap.get(s));
				}
				newList.add(perList);
				count ++;
			}
			
			/**
			 * ��Ӻϼ�
			 */
			perList = new ArrayList();
			perList.add(count++);
			for(String s : strs){
				totalMap.put("AccFullName", "�ϼ�");
				perList.add(totalMap.get(s));
			}
			newList.add(perList);
			
			/* ҵ����(�����б�����) */
			String type = request.getParameter("type");
			if(type!= null && "exportList".equals(type)){
				//�����ܷ���������
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath()+"\\����ƽ���.xls" ;
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				
				
				
				String locale =  this.getLocale(request).toString();
				//��strTitle��������ݵĸ�ʽΪ����xxx:xxx��
				List strTitle = new ArrayList();
				
				strTitle.add(setExportMap("name:No.;lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccNumber", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccName", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:�ڳ����;lastX:"+lastX));
				strTitle.add(setExportMap("name:���ڷ���;lastX:"+lastX));
				strTitle.add(setExportMap("name:��ĩ���;lastX:"+lastX));
				for(int l=0;l<3;l++){
					if(l == 0){
						strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)+";nextLine:1;nextX:3;lastX:"+lastX1));
					}else{
						strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)+";lastX:"+lastX1));
					}
					strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.LendAmount", locale)+";lastX:"+lastX1));
				}
				if("curr".equals(currFlag)){
					//���
					for(int l=0;l<6;l++){
						if(l == 0){
							strTitle.add(setExportMap("name:ԭ��;nextLine:1;nextX:3"));
						}else{
							strTitle.add(setExportMap("name:ԭ��"));
						}
						strTitle.add(setExportMap("name:��λ��"));
					}
				}else if("all".equals(currFlag)){
					//���бұ����ʽ
					for(int l=0;l<6;l++){
						for(int j = 0;j<currencyList.size();j++){
							String[] curr = (String[])currencyList.get(j);
							if(l == 0 && j == 0){
								strTitle.add(setExportMap("name:"+curr[1]+";nextLine:1;nextX:3"));
							}else{
								strTitle.add(setExportMap("name:"+curr[1]));
							}
						}
						strTitle.add(setExportMap("name:��λ��"));
					}
				}
				result = mgt.exportBanlance(fos, "����ƽ���",titleName, strTitle, newList);
				fos.close();
				if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("����ƽ���.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
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
	 * ��Ŀ����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportAccTypeInfoBalance(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		/* ��ƿ�Ŀ�������� */
		String periodYearStart = request.getParameter("periodYearStart");				//����ڼ俪ʼ��
		String periodStart = request.getParameter("periodStart");						//����ڼ俪ʼ
		String periodYearEnd = request.getParameter("periodYearEnd");					//����ڼ������
		String periodEnd = request.getParameter("periodEnd");							//����ڼ����
		String acctypeCodeStart = request.getParameter("acctypeCodeStart");				//��ƿ�Ŀ��ʼ
		String acctypeCodeEnd = request.getParameter("acctypeCodeEnd");					//��ƿ�Ŀ����
		String levelStart = request.getParameter("levelStart");							//��Ŀ����
		String currencyName = request.getParameter("currencyName");						//�ұ�
		String showItemDetail = request.getParameter("showItemDetail");					//��ʾ������Ŀ��ϸ
		String binderNo = request.getParameter("binderNo");								//����δ����ƾ֤
		String accTypeNoOperation = request.getParameter("accTypeNoOperation");			//����û��ҵ�����Ŀ�Ŀ���ڳ��������ۼƣ�
		String accTypeNoPeriod = request.getParameter("accTypeNoPeriod");				//��������û�з�����Ŀ�Ŀ
		String accTypeNoYear = request.getParameter("accTypeNoYear");					//��������û�з�����Ŀ�Ŀ
		String takeBrowNo = request.getParameter("takeBrowNo");							//�����޷�����Ŀ�Ŀ
		String partitionLine = request.getParameter("partitionLine");					//������Ŀ�������߸���
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//��ʾ���ÿ�Ŀ
		
		/* ��ѯ���бұ� */
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
		
		//ƾ֤����
		result = new VoucherMgt().queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		
		//��ѯ�����ڼ䣬ҳ���������ڼ�ʱ�����жϴ���
		result = mgt.getCurrentlyPeriod();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("beginPeriod", result.retVal);
		}
		
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		LoginBean loginBean = (LoginBean) request.getSession().getAttribute("LoginBean");
		/* �õ�����ڼ� */
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		if(comeMode == null){
			/* ��һ�ν���ʱ�����в�ѯ�������������� */
			return getForward(request, mapping, "reportAccTypeInfoBalance");
 		}
		
		/* �������������浽Map�� */
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
		/* ��ѯ��Ŀ�������� */
		result = mgt.accTypeInfoBalance((HashMap<String,String>)conMap, loginBean, mop);
		long time2 = System.currentTimeMillis();
		BaseEnv.log.info("��Ŀ����ִ��ʱ�䣺"+(time2-time1));
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//��ѯ�ɹ�
			Object[] obj = (Object[])result.retVal;
			List list = (ArrayList)obj[0];
			HashMap totalMap = (HashMap)obj[1];
			
			//ѡ��ұ����ͣ�isBase-��λ�ң���-�ۺϱ�λ�ң�all-���бұ����id-��ң�
			String currType = String.valueOf(obj[2]);
			
			request.setAttribute("accTypeList", list);
			request.setAttribute("totalMap", totalMap);
			request.setAttribute("currType", currType);
			
			
			//���ݴ���
			int lastY = 1;
			int lastX = 1;
			int lastX1 = 0;
			String currFlag = "";
			String title = "";
			String lineStr = "AccNumber;AccFullName;";
			String printStr = "No;AccNumber;AccFullName;";
			if("".equals(currType) || "isBase".equals(currType)){
				//��λ�һ����ۺϱ�λ��
				lineStr += "PeriodIniDebitBase;PeriodIniCreditBase;PeriodDebitSumBase;PeriodCreditSumBase;CurrYIniDebitSumBase;CurrYIniCreditSumBase;PeriodDebitBalaBase;PeriodCreditBalaBase";
				currFlag = "isBase";
				if("".equals(currType)){
					title += "<�ۺϱ�λ��>";
				}else{
					result = mgt.queryCurrencyName(currencyName);
					title += result.retVal;
				}
				printStr = "No;"+lineStr;
			}else if("all".equals(currType)){
				//���бұ����ʽ
				String[] cur1 = new String[]{"PeriodIniDebit","PeriodIniCredit","PeriodDebitSum","PeriodCreditSum","CurrYIniDebitSum","CurrYIniCreditSum","PeriodDebitBala","PeriodCreditBala"};
				String[] cur2 = new String[]{"PeriodIniDebitBase","PeriodIniCreditBase","PeriodDebitSumBase","PeriodCreditSumBase","CurrYIniDebitSumBase","CurrYIniCreditSumBase","PeriodDebitBalaBase","PeriodCreditBalaBase"};
				for(int k = 0;k<cur1.length;k++){
					for(int l =0;l<currencyList.size();l++){
						String[] currStr = (String[])currencyList.get(l);
						String currency = currStr[0];
						if("true".equals(currStr[2])){
							//��λ��
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
				title += "<���бұ����ʽ>";
			}else{
				//���
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
			 * �����ϸ��Ϣ
			 */
			int count = 1;
			List perList = new ArrayList();
			for(int j = 0;j<list.size(); j++){
				HashMap oldmap = (HashMap)list.get(j);
				perList = new ArrayList();
				//�����б�����
				perList.add(count);
				for(String s : strs){
					perList.add(oldmap.get(s));
				}
				newList.add(perList);
				count ++;
			}
			
			/**
			 * ��Ӻϼ�
			 */
			perList = new ArrayList();
			perList.add(count++);
			for(String s : strs){
				totalMap.put("AccFullName", "�ϼ�");
				perList.add(totalMap.get(s));
			}
			newList.add(perList);
			
			/* ҵ����(�����б�����) */
			String type = request.getParameter("type");
			if(type!= null && "exportList".equals(type)){
				//�����ܷ���������
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath()+"\\��Ŀ����.xls" ;
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				//���ñ���
				String titleName = "�ڼ䣺"+periodYearStart+"���"+periodStart+"����"+periodYearEnd+"���"+periodEnd+"��        �ұ�"+title;
				
				String locale =  this.getLocale(request).toString();
				//��strTitle��������ݵĸ�ʽΪ����xxx:xxx��
				List strTitle = new ArrayList();
				
				strTitle.add(setExportMap("name:No.;lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccNumber", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccName", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:�ڳ����;lastX:"+lastX));
				strTitle.add(setExportMap("name:���ڷ���;lastX:"+lastX));
				strTitle.add(setExportMap("name:�����ۼ�;lastX:"+lastX));
				strTitle.add(setExportMap("name:��ĩ���;lastX:"+lastX));
				for(int l=0;l<4;l++){
					if(l == 0){
						strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)+";nextLine:1;nextX:3;lastX:"+lastX1));
					}else{
						strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)+";lastX:"+lastX1));
					}
					strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.LendAmount", locale)+";lastX:"+lastX1));
				}
				if("curr".equals(currFlag)){
					//���
					for(int l=0;l<8;l++){
						if(l == 0){
							strTitle.add(setExportMap("name:ԭ��;nextLine:1;nextX:3"));
						}else{
							strTitle.add(setExportMap("name:ԭ��"));
						}
						strTitle.add(setExportMap("name:��λ��"));
					}
				}else if("all".equals(currFlag)){
					//���бұ����ʽ
					for(int l=0;l<8;l++){
						for(int j = 0;j<currencyList.size();j++){
							String[] curr = (String[])currencyList.get(j);
							if(l == 0 && j == 0){
								strTitle.add(setExportMap("name:"+curr[1]+";nextLine:1;nextX:3"));
							}else{
								strTitle.add(setExportMap("name:"+curr[1]));
							}
						}
						strTitle.add(setExportMap("name:��λ��"));
					}
				}
				
				result = mgt.exportBanlance(fos, "��Ŀ����",titleName, strTitle, newList);
				fos.close();
				if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("��Ŀ����.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
							getMessage(request, "com.export.success.over")+fileName)
							.setBackUrl("/FinanceReportAction.do?optype=ReportAccTypeInfoBalance&comeMode=1&"+conditions).setNotAutoBack()
							.setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
			}else{
				//���ݴ���
				request.getSession().setAttribute("accBalanceData", dealNewList(printStr.split(";"), newList));
			}
		}
		return getForward(request, mapping, "reportAccTypeInfoBalance");
	}
	
	
	/**
	 * ��Ŀ�ձ���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportAccTypeDayBalance(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		/* ��ƿ�Ŀ�������� */
		String dateStart = request.getParameter("dateStart");							//���ڿ�ʼ
		String dateEnd = request.getParameter("dateEnd");								//���ڽ���
		String acctypeCodeStart = request.getParameter("acctypeCodeStart");				//��ƿ�Ŀ��ʼ
		String acctypeCodeEnd = request.getParameter("acctypeCodeEnd");					//��ƿ�Ŀ����
		String levelStart = request.getParameter("levelStart");							//��Ŀ����ʼ
		String levelEnd = request.getParameter("levelEnd");								//��Ŀ�������
		String currencyName = request.getParameter("currencyName");						//�ұ�
		String binderNo = request.getParameter("binderNo");								//����δ����ƾ֤
		String balanceZero = request.getParameter("balanceZero");						//���Ϊ�㲻��ʾ
		String takeBrowNo = request.getParameter("takeBrowNo");							//�޷������ʾ
		String showTotal = request.getParameter("showTotal");							//����ϼ� 
		String showItemDetail = request.getParameter("showItemDetail");					//����������Ŀ��ϸ
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//��ʾ���ÿ�Ŀ
		
		/* ��ѯ���бұ� */
		List currencyList = new ArrayList();
		Result result = mgt.queryCurrencyAll();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			currencyList = (ArrayList)result.retVal;
			request.setAttribute("currencyList", currencyList);
		}

		//ƾ֤����
		result = new VoucherMgt().queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		LoginBean loginBean = (LoginBean) request.getSession().getAttribute("LoginBean");
		/* �õ�����ڼ� */
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		if(comeMode == null){
			/* ��һ�ν���ʱ�����в�ѯ�������������� */
			return getForward(request, mapping, "reportAccTypeInfoDayBalance");
 		}
		
		/* �������������浽Map�� */
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
		
		/* ��ѯ��Ŀ�ձ�������*/
		result = mgt.accTypeInfoDay((HashMap<String, String>)conMap,mop,loginBean);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//��ѯ�ɹ�
			Object[] obj = (Object[])result.retVal;
			
			List list = (ArrayList)obj[0];								//��ѯ�Ļ�ƿ�Ŀ
			HashMap periodMap = (HashMap)obj[1];						//���ս�������
			HashMap preMap = (HashMap)obj[2];							//���ս�������
			HashMap countMap = (HashMap)obj[3];							//���������Map
			HashMap totalMap = (HashMap)obj[4];							//�ϼ�
			String currType = String.valueOf(obj[5]);
			request.setAttribute("accTypeInfoList", list);
			request.setAttribute("periodMap", periodMap);
			request.setAttribute("preMap", preMap);
			request.setAttribute("countMap", countMap);
			request.setAttribute("totalMap", totalMap);
			request.setAttribute("currType", currType);
			
			
			
			
			//�洢Ҫ���������
			List newList = new ArrayList();
			
			/**
			 * ѭ����ƿ�Ŀ
			 */
			int count = 1;
			List perList = new ArrayList();
			for(int j = 0;j<list.size(); j++){
				HashMap oldmap = (HashMap)list.get(j);
				String classCode = String.valueOf(oldmap.get("classCode"));				//��ƿ�ĿclassCode
				
				//��ƿ�Ŀ������
				String JdFlag = "��"; 
				if(Integer.parseInt(oldmap.get("JdFlag").toString()) == 1){
					JdFlag = "��";
				}
				Object preObj = preMap.get(classCode);
				Object periodObj = periodMap.get(classCode);
				String[] fieldStrPre = new String[]{"sumdisAmountBase"};
				String[] fieldStrPeriod = new String[]{"sumDebitAmountBase","sumLendAmountBase","JdFlag","totalsumdisAmountBase"};
				String lineStr = count+";"+oldmap.get("AccNumber")+";"+oldmap.get("AccFullName")+";"+JdFlag+";";
				if("".equals(currType) || "isBase".equals(currType)){
					/* ��λ�һ����ۺϱ�λ�� */
				}else if("all".equals(currType)){
					/* ���бұ����ʽ */
					String[] newFieldStrPre = new String[currencyList.size()+1];
					String[] newFieldStrPeriod = new String[(currencyList.size()+1)*3+1];
					//�������
					for(int l = 0 ;l<fieldStrPre.length;l++){
						for(int k = 0;k<currencyList.size();k++){
							String[] curStr = (String[]) currencyList.get(k);
							String curId = curStr[0];
							if("true".equals(curStr[2])){
								//��λ��
								curId = "";
							}
							newFieldStrPre[k] = fieldStrPre[l].replace("Base", "")+"_"+curId;
						}
						newFieldStrPre[currencyList.size()] = fieldStrPre[l];
					}
					fieldStrPre = newFieldStrPre;
					//���ս�������
					int counts = 0;
					for(int l = 0 ;l<fieldStrPeriod.length;l++){
						if(!"JdFlag".equals(fieldStrPeriod[l])){
							for(int k = 0;k<currencyList.size();k++){
								String[] curStr = (String[]) currencyList.get(k);
								String curId = curStr[0];
								if("true".equals(curStr[2])){
									//��λ��
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
					/* ��� */
					fieldStrPre = new String[]{"sumdisAmount","sumdisAmountBase"};
					fieldStrPeriod = new String[]{"sumDebitAmount","sumDebitAmountBase","sumLendAmount","sumLendAmountBase","JdFlag",
							"totalsumdisAmount","totalsumdisAmountBase"};
				}
				
				//�������
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
				//���ս跽���ͱ��մ������
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
				//���������
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
				//�����б�����
				count ++;
			}
			
			/**
			 * ��Ӻϼ�
			 */
			String[] fieldStrTotal = new String[]{"pre_sumdisAmountBase","period_sumDebitAmountBase","period_sumLendAmountBase",
					"lastJdFlag","period_totalsumdisAmountBase","debitCount","lendCount"};
			String lineStrTotal = ";;�ϼ�;;";
			String printStr = "No;AccNumber;AccFullName;JdFlag;";
			if("all".equals(currType)){
				//�ϼ�
				String[] newFieldStrTotal = new String[(currencyList.size()+1)*4+3];
				int counts = 0;
				for(int l = 0 ;l<fieldStrTotal.length;l++){
					if(!("lastJdFlag".equals(fieldStrTotal[l])) && !("debitCount".equals(fieldStrTotal[l])) && !("lendCount".equals(fieldStrTotal[l]))){
						for(int k = 0;k<currencyList.size();k++){
							String[] curStr = (String[]) currencyList.get(k);
							String curId = curStr[0];
							if("true".equals(curStr[2])){
								//��λ��
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
				//���
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
			
			/* ҵ����(�����б�����) */
			String type = request.getParameter("type");
			if(type!= null && "exportList".equals(type)){
				//������Ŀ�ձ�������
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath() + "\\��Ŀ�ձ���.xls";
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				//���ñ���
				String titleName = "�������䣺"+dateStart+" �� "+dateEnd+"        �ұ�";
				String currFlag = "";
				int lastY = 0;
				int lastX = 0;
				if("".equals(currType) || "isBase".equals(currType)){
					if("".equals(currType)){
						titleName += "<�ۺϱ�λ��>";
					}else{
						result = mgt.queryCurrencyName(currencyName);
						titleName += result.retVal;
					}
				}else if("all".equals(currType)){
					//���бұ����ʽ
					currFlag = "all";
					titleName += "<���бұ����ʽ>";
					lastY = 1;
					lastX = currencyList.size();
				}else{
					//���
					lastY = 1;
					lastX = 1;
					currFlag = "curr";
					result = mgt.queryCurrencyName(currencyName);
					titleName += result.retVal;
				}
				//��strTitle��������ݵĸ�ʽΪ����xxx:xxx��
				List strTitle = new ArrayList();
				String locale = this.getLocale(request).toString();
				strTitle.add(setExportMap("name:No.;lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccNumber", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccName", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.JdFlag", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:�������;lastX:"+lastX));
				strTitle.add(setExportMap("name:���ս跽������;lastX:"+lastX));
				strTitle.add(setExportMap("name:���մ���������;lastX:"+lastX));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.JdFlag", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:�������;lastX:"+lastX));
				strTitle.add(setExportMap("name:�跽����;lastY:"+lastY));
				strTitle.add(setExportMap("name:��������;lastY:"+lastY));
				if("curr".equals(currFlag)){
					//���
					for(int l=0;l<4;l++){
						if(l == 0){
							strTitle.add(setExportMap("name:ԭ��;nextLine:1;nextX:4"));
						}else if(l == 3){
							strTitle.add(setExportMap("name:ԭ��;nextX:11"));
						}else{
							strTitle.add(setExportMap("name:ԭ��"));
						}
						strTitle.add(setExportMap("name:��λ��"));
					}
				}else if("all".equals(currFlag)){
					//���бұ����ʽ
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
						strTitle.add(setExportMap("name:��λ��"));
					}
				}
				
				result = mgt.exportBanlance(fos, "��Ŀ�ձ���",titleName, strTitle, newList);
				fos.close();
				if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("��Ŀ�ձ���.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
							getMessage(request, "com.export.success.over")+fileName)
							.setBackUrl("/FinanceReportAction.do?optype=ReportAccTypeDayBalance&comeMode=1&"+conditions).setNotAutoBack()
							.setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
			}else{
				request.getSession().setAttribute("accBalanceData", dealNewList(printStr.split(";"), newList));
			}
			
			//���ݿ�Ŀ����������
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
	 * ƾ֤���ܱ�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportCertificateSum(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		/* ��ƿ�Ŀ�������� */
		String dateStart = request.getParameter("dateStart");							//���ڿ�ʼ
		String dateEnd = request.getParameter("dateEnd");								//���ڽ���
		String levelStart = request.getParameter("levelStart");							//��Ŀ����ʼ
		String levelEnd = request.getParameter("levelEnd");								//��Ŀ�������
		String currencyName = request.getParameter("currencyName");						//�ұ�
		String area = request.getParameter("area");										//��Χ��0 ����ƾ֤��1 δ����ƾ֤��2 �ѹ���ƾ֤��
		String showItemDetail = request.getParameter("showItemDetail");					//��ʾ������Ŀ��ϸ
		String takeBrowNo = request.getParameter("takeBrowNo");							//�޷������ʾ
		String showAll = request.getParameter("showAll");								//��������ƾ֤�ֺ�
		String credTypeStr = request.getParameter("credTypeStr");						//���������ƾ֤�ַ�Χ  (ƾ֤��;��Сֵ;���ֵ;|)
		
		/* ��ѯ���бұ� */
		List currencyList = new ArrayList();
		Result result = mgt.queryCurrencyAll();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			currencyList = (ArrayList)result.retVal;
			request.setAttribute("currencyList", currencyList);
		}

		/* ƾ֤���� */
		result = new VoucherMgt().queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		LoginBean loginBean = (LoginBean) request.getSession().getAttribute("LoginBean");
		/* �õ�����ڼ� */
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		if(comeMode == null){
			/* ��һ�ν���ʱ�����в�ѯ�������������� */
			return getForward(request, mapping, "reportCertificateSum");
 		}
		
		/* �������������浽Map�� */
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
		
		
		/* ��ѯƾ֤���ܱ�����*/
		result = mgt.accCertificateSum((HashMap<String, String>)conMap,mop,loginBean);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//��ѯ�ɹ�
			Object[] obj = (Object[])result.retVal;
			List list = (ArrayList)obj[0];
			HashMap totalMap = (HashMap)obj[1];
			String currType = String.valueOf(obj[2]);
			request.setAttribute("accList", list);
			request.setAttribute("totalMap", totalMap);
			request.setAttribute("currType", currType);
			
			String locale = this.getLocale(request).toString();
			
			/**
			 * �������ݵ�newList��
			 */
			int lastY = 0;
			int lastX = 0;
			int lastX1 = 0;
			String currFlag = "";
			String title = "�ұ�";
			String lineStr = "AccNumber;AccFullName;";
			if("".equals(currType) || "isBase".equals(currType)){
				//��λ�һ����ۺϱ�λ��
				currFlag = "isBase";
				lineStr += "sumDebitAmountBase;sumLendAmountBase;isflag;sumBalanceAmountBase";
				if("".equals(currType)){
					title += "<�ۺϱ�λ��>";
				}else{
					result = mgt.queryCurrencyName(currencyName);
					title += result.retVal;
				}
				lastX1 = 1;
			}else if("all".equals(currType)){
				//���бұ����ʽ
				String[] fieldStrPeriod = new String[]{"sumDebitAmountBase","sumLendAmountBase","isflag","sumBalanceAmountBase"};
				int counts = 0;
				for(int l = 0 ;l<fieldStrPeriod.length;l++){
					if(!"isflag".equals(fieldStrPeriod[l])){
						for(int k = 0;k<currencyList.size();k++){
							String[] curStr = (String[]) currencyList.get(k);
							String curId = curStr[0];
							if("true".equals(curStr[2])){
								//��λ��
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
				title += "<���бұ����ʽ>";
			}else{
				//���
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
			//��������ӵ�list��
			String[] perStr = lineStr.split(";");
			for(int j = 0;j<list.size(); j++){
				HashMap oldmap = (HashMap)list.get(j);
				perList = new ArrayList();
				//�����б�����
				perList.add(count);
				for(String s : perStr){
					perList.add(oldmap.get(s));
				}
				newList.add(perList);
				count ++;
			}
			
			//��Ӻϼ�
			perList = new ArrayList();
			perList.add(count++);
			for(String s : perStr){
				totalMap.put("AccFullName", "�ϼ�");
				perList.add(totalMap.get(s));
			}
			newList.add(perList);
			
			/* ҵ����(�����б�����) */
			String type = request.getParameter("type");
			if(type!= null && "exportList".equals(type)){
				//����ƾ֤���ܱ�����
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath()+"\\ƾ֤���ܱ�.xls" ;
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				//���ñ���
				String titleName = "�������䣺"+dateStart+" �� "+dateEnd+"  ��Χ:";
				if(area != null && "0".equals(area)){
					titleName += "����ƾ֤";
				}else if(showAll != null && "1".equals(area)){
					titleName += "δ����ƾ֤";
				}else if(showAll != null && "2".equals(area)){
					titleName += "�ѹ���ƾ֤";
				}
				titleName += " ƾ֤�֣�";
				if(showAll != null && "1".equals(showAll)){
					titleName += "ȫ��";
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
                    					titleName += "(ȫ��);";
                    				}else{
                    					titleName += "("+startCred+" - "+endCred+");";
                    				}
                    			}
                    		}
		               	}
		            }
				}
				
				titleName += "      "+title;
				//��strTitle��������ݵĸ�ʽΪ����xxx:xxx��
				List strTitle = new ArrayList();
				
				strTitle.add(setExportMap("name:No.;lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccNumber", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccName", locale)+";lastY:"+lastY));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)+";lastX:"+lastX));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.LendAmount", locale)+";lastX:"+lastX));
				strTitle.add(setExportMap("name:���;lastX:"+lastX1));
				if("curr".equals(currFlag)){
					//���
					for(int l=0;l<3;l++){
						if(l==2){
							strTitle.add(setExportMap("name:"));
						}
						if(l == 0){
							strTitle.add(setExportMap("name:ԭ��;nextLine:1;nextX:3"));
						}else{
							strTitle.add(setExportMap("name:ԭ��"));
						}
						strTitle.add(setExportMap("name:��λ��"));
					}
				}else if("all".equals(currFlag)){
					//���бұ����ʽ
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
						strTitle.add(setExportMap("name:��λ��"));
					}
				}
				result = mgt.exportBanlance(fos, "ƾ֤���ܱ�",titleName, strTitle, newList);
				fos.close();
				if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("ƾ֤���ܱ�.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
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
	 * ������Ŀ����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward reportAccCalculateBalance(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		/* �������� */
		String periodYearStart = request.getParameter("periodYearStart");				//����ڼ俪ʼ��
		String periodStart = request.getParameter("periodStart");						//����ڼ俪ʼ
		String periodYearEnd = request.getParameter("periodYearEnd");					//����ڼ������
		String periodEnd = request.getParameter("periodEnd");							//����ڼ����	
		String accCode = request.getParameter("accCode");								//��ƿ�Ŀ
		String currencyName = request.getParameter("currencyName");						//�ұ�
		String itemSort = request.getParameter("itemSort");								//��Ŀ���
		String itemCodeStart = request.getParameter("itemCodeStart");					//��Ŀ���뿪ʼ
		String itemCodeEnd = request.getParameter("itemCodeEnd");						//��Ŀ�������
		String levelStart = request.getParameter("levelStart");							//��Ŀ����ʼ
		String binderNo = request.getParameter("binderNo");								//����δ����ƾ֤
		String takeBrowNo = request.getParameter("takeBrowNo");							//���Ϊ�㲻��ʾ
		String balanceAndTakeBrowNo = request.getParameter("balanceAndTakeBrowNo");		//��ʾ������Ϊ��ļ�¼
		String showBanAccTypeInfo = request.getParameter("showBanAccTypeInfo");			//��ʾ���ÿ�Ŀ
		
		/* ��ѯ���бұ� */
		Result result = mgt.queryCurrencyAll();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("currencyList", result.retVal);
		}

		//ƾ֤����
		result = new VoucherMgt().queryVoucherSetting();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			AccMainSettingBean settingBean = (AccMainSettingBean) result.retVal;
			request.setAttribute("AccMainSetting", settingBean);
		}
		
		String comeMode = request.getParameter("comeMode");
		request.setAttribute("comeMode", comeMode);
		LoginBean loginBean = (LoginBean) request.getSession().getAttribute("LoginBean");
		
		/* �õ�����ڼ� */
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) sessionSet.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		
		//��ѯ�����ڼ䣬ҳ���������ڼ�ʱ�����жϴ���
		result = mgt.getCurrentlyPeriod();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("beginPeriod", result.retVal);
		}
		
		if(comeMode == null){
			/* ��һ�ν���ʱ�����в�ѯ�������������� */
			return getForward(request, mapping, "reportAccCalculateBalance");
 		}
		
		/* �������������浽Map�� */
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
		
		/* ��������ƴװ��String */
		String conditions = dealConditions(conMap);
		
		/* ��ѯ������Ŀ��������*/
		result = mgt.accCalculateBalance((HashMap<String, String>)conMap,mop,loginBean);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//��ѯ�ɹ�
			Object[] obj = (Object[])result.retVal;
			List accList = (ArrayList)obj[0];
			request.setAttribute("accList",accList);
			request.setAttribute("currType",obj[1]);
			
			//��ƿ�Ŀ����
			result = mgt.queryAccType(accCode);
			String[] acctypeData = (String[])result.retVal;
			request.setAttribute("acctypeData",acctypeData);
			
			/**
			 * ������װ
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
			 * �������ݽ��д���
			 */
			String type = request.getParameter("type");
			if(type != null && "exportList".equals(type)){
				//����������Ŀ��������
				File file = new File("../../AIOBillExport") ;
				if(!file.exists()){
					file.mkdirs() ;
				}
				String fileName = file.getCanonicalPath()+"\\������Ŀ����.xls" ;
				FileOutputStream fos = new FileOutputStream(fileName);
				Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
				
				//���ñ���
				String titleName = "�ڼ䣺"+periodYearStart+"���"+periodStart+"����"+periodYearEnd+"���"+periodEnd+"��   ��ƿ�Ŀ:"+acctypeData[0]+" - "+acctypeData[1]+"   ��Ŀ���";
				if("DepartmentCode".equals(itemSort)){
					titleName += "����";
				}else if("EmployeeID".equals(itemSort)){
					titleName += "ְԱ";
				}else if("StockCode".equals(itemSort)){
					titleName += "�ֿ�";
				}else if("ClientCode".equals(itemSort)){
					titleName += "�ͻ�";
				}else if("ProjectCode".equals(itemSort)){
					titleName += "��Ŀ";
				}else if("SuplierCode".equals(itemSort)){
					titleName += "��Ӧ��";
				}
				
				/**
				 * �е�ͷ������
				 */
				String locale = this.getLocale(request).toString();
				List strTitle = new ArrayList();
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccMain.Period", locale)+";lastY:1"));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccNumber", locale)+";lastY:1"));
				strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccTypeInfo.AccName", locale)+";lastY:1"));
				strTitle.add(setExportMap("name:�ڳ����;lastX:1"));
				strTitle.add(setExportMap("name:���ڷ���;lastX:1"));
				strTitle.add(setExportMap("name:�����ۼ�;lastX:1"));
				strTitle.add(setExportMap("name:��ĩ���;lastX:1"));
				for(int i=0;i<4;i++){
					if(i == 0){
						strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)+";nextLine:1;nextX:3"));
					}else{
						strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.DebitAmount", locale)));
					}
					strTitle.add(setExportMap("name:"+GlobalsTool.getFieldDisplay(map, "tblAccDetail.LendAmount", locale)));
				}
				result = mgt.exportBanlance(fos, "������Ŀ����",titleName, strTitle, newList);
				fos.close();
				if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "com.export.success")+"<a href='/ReadFile?tempFile=export&fileName="
							+GlobalsTool.encode("������Ŀ����.xls")+"'>"+getMessage(request, "com.export.success.download")+"</a>"+
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
	 * ��������������������Ϊname=value&��ʽ
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
	 * ��arrayList<arrayList>ת��ΪarrayList<HashMap>��ʽ
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
