package com.menyi.aio.web.iniSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.menyi.aio.bean.UnitBean;
import com.menyi.web.util.*;

import org.apache.struts.action.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.dyndb.*;
import com.menyi.aio.web.report.ReportDataMgt;
import com.menyi.aio.web.sysAcc.*;
import com.dbfactory.hibernate.DBUtil;
import java.sql.Statement;
import java.sql.SQLException;
import org.hibernate.Session;
import com.dbfactory.hibernate.IfDB;
import java.sql.Connection;
import org.hibernate.jdbc.Work;

/**
 * <p>Title: 单位管理控制类</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class IniCompanyAction extends MgtBaseAction {

    IniCompanyMgt mgt = new IniCompanyMgt();
    DynDBManager dbmgt = new DynDBManager();
    SysAccMgt accmgt = new SysAccMgt();
    String sunClassCode=null;
    String OpenFCurrency="";
    String FixRate="";
    String OpenSunCompany="";
    String currPeriod="";

    public IniCompanyAction() {
    }

    /**
     * exe 控制器入口函数
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     * @todo Implement this com.huawei.sms.web.util.BaseAction method
     */
    protected ActionForward exe(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        Hashtable<String,SystemSettingBean> base=BaseEnv.systemSet;
        SystemSettingBean bean=base.get("SimpleAccFlag");
        String value=bean.getSetting();
        if(value==null)
        {
            value=bean.getDefaultValue();
        }
        request.setAttribute("SimpleAccFlag",value);

        bean=base.get("currency");
        OpenFCurrency=bean.getSetting();
        request.setAttribute("OpenFCurrency",OpenFCurrency);

        bean=base.get("FixRate");
        FixRate=bean.getSetting();
        request.setAttribute("FixRate",FixRate);

        OpenSunCompany=base.get("sunCompany").getSetting();


        //设置往来单位期初类型（应收，应付，预收，预付）
        int operation = getOperation(request);
        String type=request.getParameter("type");
        String iniTypeText="";
        request.setAttribute("type",type);
        if(type!=null)
        {
            if(type.equals("receive"))
                iniTypeText="iniCompany.lb.receive";
            else if(type.equals("pay"))
                iniTypeText="iniCompany.lb.pay";
            else if(type.equals("preReceive"))
                iniTypeText="iniCompany.lb.preReceive";
            else if(type.equals("prePay"))
                iniTypeText="iniCompany.lb.prePay";
            request.setAttribute("iniTypeText",iniTypeText);
        }
        MOperation mop = (MOperation)(getLoginBean(request).getOperationMap().get("/IniCompanyQueryAction.do?type="+type));
        request.setAttribute("MOID", mop.moduleId);
        //跟据不同操作类型分配给不同函数处理
        ActionForward forward = null;
        //得到分支机构信息
        LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
        sunClassCode = lg.getSunCmpClassCode();

        Hashtable sess=(Hashtable)BaseEnv.sessionSet.get(lg.getId());
        currPeriod=sess.get("NowPeriod").toString();
        switch (operation) {
        case OperationConst.OP_UPDATE_PREPARE:
            forward = editIniPrepare(mapping, form, request, response);
            break;
        case OperationConst.OP_UPDATE:
            if(request.getParameter("operType")!=null&&request.getParameter("operType").equals("edit")){
               forward = editIni(mapping, form, request, response);
            }else{
               forward = update(mapping, form, request, response);
            }
            break;
        case OperationConst.OP_QUERY:
            forward = query(mapping, form, request, response);
            break;
        default:
            forward = query(mapping, form, request, response);
        }
        return forward;
    }
    /**
     * 编辑期初列表保存
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward editIni(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
        Exception {

        ActionForward forward = getForward(request, mapping, "alert");
        String type = request.getParameter("type");
        if(!currPeriod.equals("-1")){
            //已经开账不能修改期初信息
            EchoMessage.error().add(getMessage(
                        request, "common.msg.RET_BEGINACC_END")).setBackUrl(
                        "/IniCompanyQueryAction.do?type="+type).setAlertRequest(
                                request);
            return forward;
        }

        if ((dbmgt.getChildCount("tblSunCompanys.classCode", sunClassCode).getRetVal().toString().
            equals("0")&&OpenSunCompany.equals("true"))||OpenSunCompany.equals("false")) {
            Result rs = null;
            boolean success = true;
            String[] keyId = request.getParameterValues("keyId");
            String[] currencyID = request.getParameterValues("currencyID");
            String[] currencyRate = request.getParameterValues("currencyRate");
            String[] year = request.getParameterValues("year");
            String[] borrow = request.getParameterValues("borrow");
            String[] lend = request.getParameterValues("lend");
            String[] balance = request.getParameterValues("balance");
            String[] curyear = request.getParameterValues("curyear");
            String[] curborrow = request.getParameterValues("curborrow");
            String[] curlend = request.getParameterValues("curlend");
            String[] curbalance = request.getParameterValues("curbalance");

            if (year == null ) { //启用简单财务
               year = new String[keyId.length];
               borrow = new String[keyId.length];
               lend = new String[keyId.length];
               curyear = new String[keyId.length];
               curborrow = new String[keyId.length];
               curlend = new String[keyId.length];

               for (int i = 0; i < keyId.length; i++) {
                   year[i] = balance[i];
                   borrow[i] = "0";
                   lend[i] = "0";                   
                   curyear[i] = curbalance==null?"":curbalance[i];
                   curborrow[i] = "0";
                   curlend[i] = "0";
               }
           }
           if(currencyID==null){//不启用外币
        	  currencyID = new String[keyId.length];
              currencyRate = new String[keyId.length];
        	  curyear = new String[keyId.length];
              curborrow = new String[keyId.length];
              curlend = new String[keyId.length];
              curbalance = new String[keyId.length];
              
              for(int i=0;i<keyId.length;i++){
            	  currencyID[i]="";
            	  currencyRate[i]="0";
            	  curyear[i] = "0";
                  curborrow[i] = "0";
                  curlend[i] = "0";
                  curbalance[i] = "0";
              }
           }


            for (int i = 0; i < keyId.length; i++) {
                //执行修改
                rs = mgt.update(sunClassCode, keyId[i], Double.valueOf(year[i]),
                                Double.valueOf(borrow[i]),
                                Double.valueOf(lend[i]),
                                Double.valueOf(balance[i]),
                                type, currencyID[i],
                                Double.valueOf(currencyRate[i]),
                                Double.valueOf(curyear[i])
                                , Double.valueOf(curborrow[i]),
                                Double.valueOf(curlend[i]),
                                Double.valueOf(curbalance[i]),this.getLoginBean(request).getId());
                if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
                    success = false;
                    break;
                }
            }
            if (success) {
                //修改成功
                EchoMessage.success().add(getMessage(
                        request, "common.msg.updateSuccess")).
                        setBackUrl("/IniCompanyQueryAction.do?type=" + type +
                                   "&companyCode=" +
                                   request.getParameter("classCode")+"&winCurIndex="+request.getParameter("winCurIndex")).
                        setAlertRequest(request);
            } else {
                //修改失败
                EchoMessage.error().add(getMessage(
                        request, "common.msg.updateErro")).setAlertRequest(
                                request);
            }
        }else{
            EchoMessage.error().add(getMessage(
                        request, "common.msg.notLastSunCompany")).setAlertRequest(
                                request);
        }

        return  forward;
    }

    /**
     * 编辑期初列表前的准备
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward editIniPrepare(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws
        Exception {
        ActionForward forward = null;
        request.setAttribute("companyCode",request.getParameter("companyCode"));       
        String type=request.getParameter("type");
        String[] classCode = request.getParameterValues("keyId") ;
       
        if (classCode != null && classCode.length!= 0) {
        	//执行查询前的加载
        	 Result rs = mgt.getEditList(classCode,type);
           
            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //加载成功
                List result=(List)rs.getRetVal();
                request.setAttribute("result", result);
                GlobalMgt glMgt=new GlobalMgt();
                rs=glMgt.getCurrency();
               
                if(rs.retCode== ErrorCanst.DEFAULT_SUCCESS){
                    try {
                        GlobalMgt gmgt = new GlobalMgt();
                        Object[] objr = gmgt.getBaseCurrency();
                        request.setAttribute("baseCur", objr[1]);
                    } catch (Exception ex) {
                        EchoMessage.error().add(ex.getMessage()).
                                setRequest(request);
                    }

                     request.setAttribute("curs",rs.getRetVal());
                     forward = getForward(request, mapping, "editIni");
                }else{
                    //加载失败
                    EchoMessage.error().add(rs.getRetVal().toString()).
                    setRequest(request);
                }
            }  else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
                //记录不存在错误
            	
                EchoMessage.error().add(getMessage(
                    request, "common.error.noeditdata")).setBackUrl(
    						"/IniCompanyQueryAction.do?type=receive&operation=4&companyCode="+request.getParameter("companyCode")+"&winCurIndex=" + request.getParameter("winCurIndex")).setRequest(request);
            } else if(rs.getRetCode()==ErrorCanst.RET_BEGINACC_END) {

            } else {
                //加载失败
                EchoMessage.error().add(rs.getRetVal().toString()).
                    setRequest(request);
            }
            if(forward==null)
            {
                 forward = getForward(request, mapping, "message");
            }
        }
        return forward;
    }

    /**
     * 修改前的准备
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward updatePrepare(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws
        Exception {
        ActionForward forward = null;
        String companyCode=request.getParameter("companyCode");
        if (companyCode != null && companyCode.length() != 0) {

            //执行查询前的加载
            Result rs = mgt.detail(sunClassCode,companyCode,request.getAttribute("type").toString());
            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //加载成功
                List list=(List)rs.retVal;
                Object []obj=(Object[])list.get(0);
                request.setAttribute("companyNo",obj[1]);
                request.setAttribute("companyName",obj[2]);
                request.setAttribute("rows", rs.retVal);
                request.setAttribute("companyCode",companyCode);
                GlobalMgt glMgt=new GlobalMgt();
                rs=glMgt.getCurrency();
                if(rs.retCode== ErrorCanst.DEFAULT_SUCCESS){
                    request.setAttribute("curs",rs.getRetVal());
                    GlobalMgt gmgt=new GlobalMgt();
                    try{
                        Object []objr=gmgt.getBaseCurrency();
                         request.setAttribute("baseCur",objr[1]);
                    }catch(Exception ex){
                         EchoMessage.error().add(ex.getMessage()).
                         setRequest(request);
                     }
                     forward = getForward(request, mapping, "update");
                }else{
                    //加载失败
                    EchoMessage.error().add(rs.getRetVal().toString()).
                    setRequest(request);
                }
            }  else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
                //记录不存在错误
                EchoMessage.error().add(getMessage(
                    request, "common.error.nodata")).setRequest(request);
            } else {
                //加载失败
                EchoMessage.error().add(getMessage(request, "common.msg.error")).
                    setRequest(request);
            }
            if(forward==null)
            {
                 forward = getForward(request, mapping, "message");
            }

        }
        return forward;
    }

    /**
     * 修改
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward update(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
            Exception {
        ActionForward forward = null;
        forward = getForward(request, mapping, "alert");
        if(!currPeriod.equals("-1")){
            //已经开账不能修改期初信息
            EchoMessage.error().add(getMessage(
                        request, "common.msg.RET_BEGINACC_END")).setBackUrl(
                        "/IniCompanyQueryAction.do?type="+request.getAttribute("type").toString()).setAlertRequest(
                                request);
            return forward;
        }
        LoginBean lg =(LoginBean) request.getSession().getAttribute("LoginBean");
        String companyCode = request.getParameter("companyCode");
        Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(lg.getId()) ;
        if ((Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString())&&OpenSunCompany.equals("true"))||OpenSunCompany.equals("false")) {

            String[] currencyID = request.getParameterValues("currencyID");
            String[] currencyRate = request.getParameterValues("currencyRate");
            String[] year = request.getParameterValues("year");
            String[] borrow = request.getParameterValues("borrow");
            String[] lend = request.getParameterValues("lend");
            String[] balance = request.getParameterValues("balance");
            String[] curyear = request.getParameterValues("curyear");
            String[] curborrow = request.getParameterValues("curborrow");
            String[] curlend = request.getParameterValues("curlend");
            String[] curbalance = request.getParameterValues("curbalance");
            String type=request.getParameter("type");
            String iniTypeText="";
            if(type!=null)
            {
                if(type.equals("receive"))
                    iniTypeText="iniCompany.lb.receive";
                else if(type.equals("pay"))
                    iniTypeText="iniCompany.lb.pay";
                else if(type.equals("preReceive"))
                    iniTypeText="iniCompany.lb.preReceive";
                else if(type.equals("prePay"))
                    iniTypeText="iniCompany.lb.prePay";
            }
            ArrayList delRowNums=new ArrayList();
            if(GlobalsTool.getSysSetting("currency").equals("true")&&currencyID!=null){//启用多币种时判断不能录入多条相同币种的数据
            	ArrayList currencyIds=new ArrayList();
            	for(int i=0;i<currencyID.length;i++){
            		if((year!=null&&Float.parseFloat(year[i]==""?"0.0":year[i])!=0.0)||(borrow!=null&&Float.parseFloat(borrow[i]==""?"0.0":borrow[i])!=0.0)
            			||(lend!=null&&Float.parseFloat(lend[i]==""?"0.0":lend[i])!=0.0)||(balance!=null&&Float.parseFloat(balance[i]==""?"0.0":balance[i])!=0.0)
            			||(curyear!=null&&Float.parseFloat(curyear[i]==""?"0.0":curyear[i])!=0.0)||(curborrow!=null&&Float.parseFloat(curborrow[i]==""?"0.0":curborrow[i])!=0.0)
            			||(curlend!=null&&Float.parseFloat(curlend[i]==""?"0.0":curlend[i])!=0.0)||(curbalance!=null&&Float.parseFloat(curbalance[i]==""?"0.0":curbalance[i])!=0.0)
            			||(currencyRate!=null&&Float.parseFloat(currencyRate[i]==""?"0.0":currencyRate[i])!=0.0)){
            			if(currencyIds.contains(currencyID[i])){
            			EchoMessage.error().add(this.getMessage(request, "iniCompany.moredata.error2",this.getMessage(request, "iniCompany.lb.head")+"_"+this.getMessage(request, iniTypeText))).setAlertRequest(request);
    					return forward;
            			}else{
            				currencyIds.add(currencyID[i]);
            			}
            		}else{
            			if(i!=0){
    						delRowNums.add(String.valueOf(i));
    					}
            		}
            	}
          
            	  String[] tempCurrencyID=currencyID;
                  String[] tempCurrencyRate=currencyRate;
                  String[] tempYear=year;
                  String[] tempBorrow=borrow;
                  String[] tempLend=lend;
                  String[] tempbalance=balance;
                  String[] tempCuryear=curyear;
                  String[] tempCurborrow=curborrow;
                  String[] tempCurlend=curlend;
                  String[] tempCurbalance=curbalance;
                  ArrayList addRowNums=new ArrayList();
                  for(int i=0;i<currencyID.length;i++){
                	  if(!delRowNums.contains(String.valueOf(i))){
                		  addRowNums.add(i);
                	  }
                  }
                  currencyID=new String[tempCurrencyID.length-delRowNums.size()];
                  currencyRate=new String[tempCurrencyID.length-delRowNums.size()];
                  year=new String[tempCurrencyID.length-delRowNums.size()];
                  borrow=new String[tempCurrencyID.length-delRowNums.size()];
                  lend=new String[tempCurrencyID.length-delRowNums.size()];
                  balance=new String[tempCurrencyID.length-delRowNums.size()];
                  curyear=new String[tempCurrencyID.length-delRowNums.size()];
                  curborrow=new String[tempCurrencyID.length-delRowNums.size()];
                  curlend=new String[tempCurrencyID.length-delRowNums.size()];
                  curbalance=new String[tempCurrencyID.length-delRowNums.size()];
                  for(int i=0;i<addRowNums.size();i++){
                	  int rowNum=Integer.parseInt(addRowNums.get(i).toString());
                	  currencyID[i]=tempCurrencyID[rowNum];
                      currencyRate[i]=tempCurrencyRate[rowNum];
                      if(tempYear==null){
                          year=tempYear;
                      }else{
                    	  year[i]=tempYear[rowNum];
                      }
                      if(tempBorrow==null){
                    	  borrow=tempBorrow;
                      }else{
                          borrow[i]=tempBorrow[rowNum];
                      }
                      if(tempLend==null){
                    	  lend=tempLend;
                      }else{
                       lend[i]=tempLend[rowNum];
                      }
                      if(tempbalance==null){
                    	  balance=tempbalance;
                      }else{
                        balance[i]=tempbalance[rowNum];
                      }
                      if(tempCuryear==null){
                    	  curyear=tempCuryear;
                      }else{
                        curyear[i]=tempCuryear[rowNum];
                      }
                      if(tempCurborrow==null){
                    	  curborrow=tempCurborrow;
                      }else{
                        curborrow[i]=tempCurborrow[rowNum];
                      }
                      if(tempCurlend==null){
                    	  curlend=tempCurlend;
                      }else{
                         curlend[i]=tempCurlend[rowNum];
                      }
                      if(tempCurbalance==null){
                    	  curbalance=tempCurbalance;
                      }else{
                         curbalance[i]=tempCurbalance[rowNum];
                      }
                  }
                  
            }
            
            if (year == null && currencyID == null) { //启用简单财务并且没有启用外币核算
                year = new String[1];
                year[0] = balance[0];
                borrow = new String[1];
                borrow[0] = "0";
                lend = new String[1];
                lend[0] = "0";
            } else if (year == null && currencyID != null) { //启用简单财务，启用外币核算
                year = new String[currencyID.length];
                borrow = new String[currencyID.length];
                lend = new String[currencyID.length];
                curyear = new String[currencyID.length];
                curborrow = new String[currencyID.length];
                curlend = new String[currencyID.length];

                for (int i = 0; i < currencyID.length; i++) {
                    year[i] = balance[i];
                    borrow[i] = "0";
                    lend[i] = "0";
                    curyear[i] = curbalance[i];
                    curborrow[i] = "0";
                    curlend[i] = "0";
                }
            }

            //执行修改
            Result rs = mgt.updateCompany(this.getLoginBean(request).getId(),
                                          sunClassCode, companyCode, year,
                                          borrow, lend, balance,
                                          request.getAttribute("type").toString(),
                                          currencyID, currencyRate,
                                          curyear, curborrow, curlend,
                                          curbalance);

            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //更新tblCompanyTotal往来汇总明细
                final String sunClassCode = lg.getSunCmpClassCode();
                final String cmpCode = companyCode;
                int retCode = DBUtil.execute(new IfDB() {
                    public int exec(Session session) {
                        session.doWork(new Work() {
                            public void execute(Connection connection) throws
                                    SQLException {
                                Statement cs = connection.createStatement();
                                int[] period = new int[4];
                                try {
                                    period = accmgt.getCurrAndNextPeriod(cs, sunClassCode);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                accmgt.updateCompanyTotal(cs, period[0], period[1], period[2],
                                                    period[3], sunClassCode,cmpCode);
                            }
                        });
                        return 0;
                    }
                });

                EchoMessage.success().add(getMessage(
                        request, "common.msg.updateSuccess")).
                        setBackUrl("/IniCompanyQueryAction.do?type=" +
                                   request.getAttribute("type").toString() +
                                   "&listType=back&companyCode=" +
                                   companyCode+"&winCurIndex="+request.getParameter("winCurIndex")+"&pageNo="+this.getParameterInt("pageNo", request)).
                        setAlertRequest(request);
            } else {
                //修改失败
                EchoMessage.error().add(getMessage(
                        request, "common.msg.updateErro")).setAlertRequest(
                                request);
            }
        } else {
            EchoMessage.error().add(getMessage(
                         request, "common.msg.notLastSunCompany")).setAlertRequest(
                                 request);
        }

        return forward;
    }


    /**
     * 查询
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		IniCompanySearchForm searchForm = (IniCompanySearchForm) form;
		if (searchForm != null) {
			//执行查询
			String listType = request.getParameter("listType");
			String companyCode = request.getParameter("companyCode");
			String type = request.getParameter("type");
			if (companyCode == null) {
				companyCode = "";
			}
			if (companyCode.length() > 0 && listType != null
					&& (listType.equals("update") || listType.equals("back"))) {
				companyCode = companyCode.substring(0, companyCode.length() - 5);
			}

			Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			int pageSize = getParameterInt("pageSize", request);
			int pageNo = getParameterInt("pageNo", request);
			if (pageNo == 0)
				pageNo = 1;
			if (pageSize == 0)
				pageSize = GlobalsTool.getPageSize();

			//得到分支机构信息
			request.setAttribute("pageNo", pageNo);
			LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
			String sunClassCode = lg.getSunCmpClassCode();

			Result rs = mgt.query(sunClassCode, listType, companyCode, map, searchForm.getCompanyNo(),
					searchForm.getCompanyName(), searchForm.getDimQuery(), type, pageNo, pageSize);

			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				if (rs.getRealTotal() == 0) {
					if (listType != null && listType.equals("next")) {
						return updatePrepare(mapping, form, request, response);
					}
				}
				ArrayList res = new ArrayList();
				res = (ArrayList) rs.getRetVal();
				ArrayList delList = new ArrayList();
				for (int i = 0; i < res.size(); i++) {
					String company_code = ((Object[]) res.get(i))[0].toString();
					if (company_code.length() > 5) {
						for (int j = 0; j < res.size(); j++) {
							if (j != i) {
								String temp_code = ((Object[]) res.get(j))[0].toString();
								if (temp_code.length() < company_code.length()) {
									if (company_code.substring(0, 5).equals(
											temp_code = temp_code.length() == 5 ? temp_code : temp_code.substring(0, 5))) {
										if (!delList.contains(i)) {
											delList.add(i);
										}
									}
								}
							}
						}
					}
				}
				if (delList.size() > 0) {
					ArrayList lastList = new ArrayList();
					for (int i = 0; i < res.size(); i++) {
						if (!delList.contains(i)) {
							lastList.add(res.get(i));
						}
					}
					res = lastList;
				}
				
				/**加上当前商品所在的当前位置**/
				DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, "tblCompany");
				String strCompanyCode = getParameter("companyCode", request)==null?"":getParameter("companyCode", request) ;
				if("back".equals(listType) && strCompanyCode.length()>=5){
					strCompanyCode = strCompanyCode.substring(0,strCompanyCode.length()-5) ;
				}
				Result rs3 = new ReportDataMgt().getParentName(strCompanyCode, tableInfo, String.valueOf(getLocale(request)));
				if (rs3.retCode == ErrorCanst.RET_NOTSETROWMARKER) {
					request.setAttribute("parentName", "");
				} else {
					ArrayList parentName = (ArrayList) rs3.retVal;
					String parentUrl = "";
					if (parentName.size() == 0) {
						parentUrl += GlobalsTool.getMessage(request, "com.acc.ini.root");
					} else {
						parentUrl += "<a href=\"/IniCompanyQueryAction.do?companyCode=&listType=back" 
								     + "&type="+type+ "&winCurIndex="
								     + request.getParameter("winCurIndex") + "\">"
								     + GlobalsTool.getMessage(request, "com.acc.ini.root") + "</a> >> ";
						for (int i = 0; i < parentName.size(); i++) {
							String[] nameClass = (String[]) parentName.get(i);
							if (nameClass[1].length() == (parentName.size()) * 5) {
								parentUrl += nameClass[0];
							} else {
								parentUrl += "<a href=\"/IniCompanyQueryAction.do?&companyCode=" + nameClass[1]+"00001" + "&listType=back"
										+ "&type="+type+"&winCurIndex="
										+ request.getParameter("winCurIndex") + "\">" + nameClass[0]
										+ "</a> >> ";
							}

						}
					}
					request.setAttribute("parentName", parentUrl);
				}
				
				request.setAttribute("companyCode", companyCode);
				request.setAttribute("result", res);
				request.setAttribute("pageBar", pageBar(rs, request));

			} else {
				//查询失败
				EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
				return getForward(request, mapping, "message");
			}
		}
		return getForward(request, mapping, "list");
	}

}
