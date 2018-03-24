package com.menyi.aio.web.iniSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.menyi.aio.bean.UnitBean;
import com.menyi.web.util.*;

import org.apache.struts.action.*;
import java.util.List;
import java.util.Hashtable;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.login.MOperation;
import java.util.ArrayList;
import com.menyi.aio.bean.ReportsDetBean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;
import com.menyi.aio.bean.ReportsBean;
import com.menyi.aio.web.report.ReportDataMgt;
import com.menyi.aio.web.report.ReportSetMgt;
import java.util.Map;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.web.sysAcc.SysAccMgt;
import com.dbfactory.hibernate.DBUtil;

import java.net.URLEncoder;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.Session;
import com.dbfactory.hibernate.IfDB;
import java.sql.Connection;
import org.hibernate.jdbc.Work;

import sun.security.timestamp.TSRequest;

import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.goodsProp.GoodsPropMgt;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.bean.GoodsPropEnumItemBean;
import com.menyi.aio.bean.StockBean;

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
public class IniGoodsAction extends MgtBaseAction {

    IniGoodsMgt mgt = new IniGoodsMgt();
    GoodsPropMgt propMgt=new GoodsPropMgt();
    DynDBManager dbmgt = new DynDBManager();
    SysAccMgt accmgt = new SysAccMgt();
    String sunClassCode=null;
    String OpenSunCompany="";
    String currPeriod="";

    public IniGoodsAction() {
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
    protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int operation = getOperation(request);
		// 得到分支机构信息
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		sunClassCode = lg.getSunCmpClassCode();
		Hashtable<String, SystemSettingBean> systemSet = BaseEnv.systemSet;
		OpenSunCompany = systemSet.get("sunCompany").getSetting();

		Hashtable sess = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		currPeriod = sess.get("NowPeriod").toString();

		// 跟据不同操作类型分配给不同函数处理
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_UPDATE_PREPARE:
			forward = editIniPrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			if (request.getParameter("type") != null && request.getParameter("type").equals("edit")) {
				forward = editIni(mapping, form, request, response);
			} else {
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
	 * 编辑期初列表前的准备
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
	protected ActionForward editIniPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		ActionForward forward = null;
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(
				BaseEnv.TABLE_INFO);
		String goodsCode = request.getParameter("goodsCode");
		String stockName = request.getParameter("stockName");
		request.setAttribute("stockName", stockName);
		request.setAttribute("goodsCode", goodsCode);
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		String[] id = request.getParameterValues("keyId");
		if (id != null && id.length != 0) {
			// 显示属性
			IniGoodsMgt iniGoodsMgt = new IniGoodsMgt();
			GoodsPropMgt propMgt = new GoodsPropMgt();
			List fieldsName = iniGoodsMgt.queryStocksFieldsName("tblStockDet", allTables);
			List existPropNames = new ArrayList();
			boolean existsTowUnit = false;
			Result rss = null;
			for (int i = 0; i < fieldsName.size(); i++) {
				rss = propMgt.queryPropName(fieldsName.get(i).toString());
				if (rss.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && rss.getRetVal() != null
						&& ((ArrayList) rss.getRetVal()).size() > 0) {
					GoodsPropInfoBean gpInfo = (GoodsPropInfoBean) ((ArrayList) rss.getRetVal()).get(0);
					DBFieldInfoBean temp = GlobalsTool.getFieldBean("tblStockDet", gpInfo.getPropName());
					if (gpInfo.getIsUsed() == 1 && temp.getInputType() != DBFieldInfoBean.INPUT_HIDDEN)// 如果该属性启用并显示
					{
						String[] values = new String[4];
						values[0] = gpInfo.getPropName();// 属性名称
						values[1] = gpInfo.getDisplay().get(getLocale(request).toString()).toString();// 属性值
						values[2] = String.valueOf(gpInfo.getInputBill());// 是否单据录入
						values[3] = String.valueOf(gpInfo.getTwoUnit());
						existPropNames.add(values);
						if (gpInfo.getTwoUnit() == 1) {
							request.setAttribute("twoUnitPro", gpInfo.getPropName());
							request.setAttribute("twoUnitBillInput", values[2]);
						}
					}
					if (gpInfo.getIsSequence() == 1) {// 保存序列号属性名称
						request.setAttribute("seqPropfName", gpInfo.getPropName());
					}
				}
			}
			if (BaseEnv.systemSet.get("TwoUnit").getSetting().equals("true")) {
				existsTowUnit = true;
			}
			request.setAttribute("existsTowUnit", existsTowUnit);
			List existNVPropDis = new ArrayList();
			for (int i = 0; i < existPropNames.size(); i++) {
				GoodsPropInfoBean tempProp = GlobalsTool.getPropBean(((String[]) existPropNames.get(i))[0]);
				if (tempProp.getNameAndValue() == 1) {// 支持名称&值属性
					DBFieldInfoBean nvField = GlobalsTool.getFieldBean("tblStockDet", tempProp.getPropName()
							+ "NV");
					String[] values = new String[4];
					values[0] = tempProp.getPropName();
					values[1] = nvField.getFieldName();
					values[2] = nvField.getDisplay().get(getLocale(request).toString());
					values[3] = "";
					existNVPropDis.add(values);
				}
			}
			// 执行查询前的加载
			Result rs = mgt.getEditList(id, existPropNames, existsTowUnit);
			List propValues = new ArrayList();
			List nvPropDis = new ArrayList();
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				propValues = (ArrayList) rs.getRetVal();
				for (int k = 0; k < propValues.size(); k++) {
					Object[] row = (Object[]) propValues.get(k);
					ArrayList nvRow = new ArrayList();
					for (int p = 0; p < existPropNames.size(); p++) {
						rs = propMgt.queryPropName(((String[]) existPropNames.get(p))[0]);
						String[] col = new String[2];
						if (rs.getRetCode() != ErrorCanst.DEFAULT_FAILURE
								&& ((ArrayList) rs.getRetVal()).size() > 0) {
							
							GoodsPropInfoBean gpBean = (GoodsPropInfoBean) ((ArrayList) rs.getRetVal()).get(0);
							String val = row[11 + p] == null ? "" : row[11 + p].toString();
							rs = propMgt.queryPropValue(gpBean.getId(), val, row[1].toString(), getLocale(request).toString());
							
							String tempEnumValue = "";
							if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && rs.getRetVal() != null) {
								tempEnumValue = val+"@#"+rs.getRetVal().toString();
							} else {
								if(gpBean.getIsSequence()!=1){
									tempEnumValue = val+"@#"+val;
								}else{
									tempEnumValue = val;
								}
							}
							
							if (gpBean.getNameAndValue() == 1) {
								col[0] = gpBean.getPropName();
								col[1] = tempEnumValue;
								nvRow.add(col);
							}
							row[11 + p] = tempEnumValue;
							
						}
					}
					nvPropDis.add(nvRow);
				}
				request.setAttribute("existsPropNames", existPropNames);
				request.setAttribute("existNVPropDis", existNVPropDis);
				request.setAttribute("props", propValues);
				request.setAttribute("nvPropDis", nvPropDis);
				request.setAttribute("colLength", ((Object[]) propValues.get(0)).length);
				String[] seqFlag = new String[propValues.size()];
				for (int i = 0; i < propValues.size(); i++) {
					Object[] obj = (Object[]) propValues.get(i);
					rs = mgt.queryGoodsSet(obj[1].toString());
					if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
						boolean startSeq = (Boolean) rs.getRetVal();
						if (startSeq) {
							seqFlag[i] = "0";
						}
					}
				}
				DBFieldInfoBean df = GlobalsTool.getFieldBean("tblIniStockDet", "InstoreQty");
				if (df.getFieldSysType().equals("SeqQty")) {// 如果是序列号数量字段
					request.setAttribute("SeqQtyfName", "InstoreQty");
				}
				if (GlobalsTool.hasUsedSeq("tblStockDet")) {// 如果启用序列号，显示往来单位弹出框
					rs = mgt.getProvidors(id);
					if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
						EchoMessage.error().add(rs.getRetVal().toString()).setRequest(request);
					}
					request.setAttribute("providorList", rs.getRetVal());
				}
				request.setAttribute("seqFlag", seqFlag);
				forward = getForward(request, mapping, "editIni");
			} else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
				// 记录不存在错误
				EchoMessage.error().add(getMessage(request, "common.error.noeditdata")).setBackUrl(
						"/IniGoodsQueryAction.do?goodsCode="+goodsCode+"&stockName="+stockName+"&winCurIndex=" + request.getParameter("winCurIndex"))
						.setRequest(request);
			} else {
				// 加载失败
				EchoMessage.error().add(rs.getRetVal().toString()).setRequest(request);
			}
			if (forward == null) {
				forward = getForward(request, mapping, "message");
			}
		}
		return forward;
	}
	
    /**
	 * 编辑期初列表保存
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
    protected ActionForward editIni(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
        Exception {
        ActionForward forward = getForward(request, mapping, "alert");
        Hashtable allTables = (Hashtable) request.getSession().
        getServletContext().getAttribute(BaseEnv.TABLE_INFO);
        LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
        if(!currPeriod.equals("-1")){
            // 已经开账不能修改期初信息
            EchoMessage.error().add(getMessage(
                    request, "common.msg.RET_BEGINACC_END")).setBackUrl(
                            "/IniGoodsQueryAction.do").setAlertRequest(
                                    request);

            return forward;
        }

        // 是否启用负库存验证
        boolean negativeStock = Boolean.parseBoolean(BaseEnv.systemSet.get("NegativeStock").getSetting());
        Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(lg.getId()) ;
        if((Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString())&&OpenSunCompany.equals("true"))||
                OpenSunCompany.equals("false")){
            Result rs = null;
            boolean success = true;
//          获取属性字段内容
        	IniGoodsMgt iniGoodsMgt=new IniGoodsMgt();
            GoodsPropMgt propMgt=new GoodsPropMgt();
            List existPropNames=new ArrayList();
            List existNVNames=new ArrayList();//支持属性&值的属性名称
            List propValues=new ArrayList();
            List nvValues=new ArrayList();//支持属性&值的属性值
            Result rss=null;
            List fieldsName= iniGoodsMgt.queryStocksFieldsName("tblStockDet",allTables);
               for(int i=0;i<fieldsName.size();i++)
               {
            	  rss= propMgt.queryPropName(fieldsName.get(i).toString());
            	  if(rss.getRetCode()==ErrorCanst.DEFAULT_SUCCESS&&rss.getRetVal()!=null&&((ArrayList)rss.getRetVal()).size()>0)
            	  {
            		  GoodsPropInfoBean gpInfo=(GoodsPropInfoBean)((ArrayList)rss.getRetVal()).get(0);
            		  String[] values=new String[3];
            		  values[0]=gpInfo.getPropName();
            		  values[1]=gpInfo.getDisplay().get(getLocale(request).
                              toString()).toString();
            		  values[2]=String.valueOf(gpInfo.getIsCalculate());
            		  existPropNames.add(values);
            		  if(gpInfo.getNameAndValue()==1){
            			  existNVNames.add(gpInfo.getPropName()+"NV");
            		  }
            	  }
               }
            if(existPropNames.size()!=0)
            {
            	for(int i=0;i<existPropNames.size();i++)
            	{
            		String []propName=(String [])existPropNames.get(i);
            		String[]values=request.getParameterValues("tblGoodsOfProp_"+propName[0]);
            		propValues.add(values);
            		
            	}
            }
            for(int i=0;i<existNVNames.size();i++){
            	String[]values=request.getParameterValues(existNVNames.get(i).toString());
        		nvValues.add(values);
            }

            
            String []IniTwoQty=request.getParameterValues("IniTwoQty");
            String[] keyId = request.getParameterValues("keyId");
            String[] goodsCode = request.getParameterValues("goodsCode");
            String[] stockCode = request.getParameterValues("stockCode");
            String[] iniQty = request.getParameterValues("InstoreQty");
            String[] iniPrice = request.getParameterValues("InstorePrice");
            String[] iniAmount = request.getParameterValues("InstoreAmount");
            String[] StockLocation=request.getParameterValues("StockLocation");
            String[]hidSecUnit=request.getParameterValues("hidSecUnit");
            String[]conversionRate=request.getParameterValues("conversionRate");
            String[]secUnitQty=request.getParameterValues("secUnitQty");
            String[]secUnitPrice=request.getParameterValues("secUnitPrice");
            String[]hidProvider=request.getParameterValues("hidProvider");
            String  seqPropfName=request.getParameter("seqPropfName");
            ArrayList notUsedPropNames=iniGoodsMgt.queryNotUsedPropNameInTable("tblStockDet",allTables);
            rs = mgt.updateMultiData(sunClassCode,existPropNames,propValues,keyId,goodsCode,stockCode,IniTwoQty,iniQty,iniPrice,iniAmount,negativeStock,lg.getId(),notUsedPropNames,StockLocation,seqPropfName,hidSecUnit,conversionRate,secUnitQty,secUnitPrice,getLocale(request).toString(),hidProvider,nvValues,existNVNames);

            if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
                //修改成功
                EchoMessage.success().add(getMessage(
                        request, "common.msg.updateSuccess")).
                        setBackUrl("/IniGoodsQueryAction.do?type=back&goodsCode=" +
                                   request.getParameter("classCode")+"00001"+"&stockName="+URLEncoder.encode(request.getParameter("stockName"), "UTF-8")+"&type=next"+"&winCurIndex="+request.getParameter("winCurIndex")).
                        setAlertRequest(request);
            } else {
                //修改失败
                EchoMessage.error().add(getMessage(
                        request, "common.msg.updateErro")).setAlertRequest(request);
            }
        }else{
           EchoMessage.error().add(getMessage(
                        request, "common.msg.notLastSunCompany")).setAlertRequest(
                                request);
        }
        return  forward;
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
    protected ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(
				BaseEnv.TABLE_INFO);
		String goodsCode = request.getParameter("goodsCode");
		String stockCode = request.getParameter("stockCode");
		if (goodsCode != null && goodsCode.length() != 0) {

			// 执行查询前的加载
			Result rs = mgt.detail(goodsCode, stockCode, BaseEnv.systemSet.get("AssUnit").getSetting());
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				// 加载成功
				Object[] detail = (Object[]) rs.getRetVal();
				request.setAttribute("detail", detail);
				// 显示属性
				IniGoodsMgt iniGoodsMgt = new IniGoodsMgt();
				GoodsPropMgt propMgt = new GoodsPropMgt();
				Result rss = null;
				List existPropNames = new ArrayList();
				boolean existsTowUnit = false;
				List fieldsName = iniGoodsMgt.queryStocksFieldsName("tblStockDet", allTables);
				boolean goodsUseSeq = false;
				for (int i = 0; i < fieldsName.size(); i++) {
					rss = propMgt.queryPropName(fieldsName.get(i).toString());
					if (rss.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && rss.getRetVal() != null
							&& ((ArrayList) rss.getRetVal()).size() > 0) {

						GoodsPropInfoBean gpInfo = (GoodsPropInfoBean) ((ArrayList) rss.getRetVal()).get(0);
						DBFieldInfoBean temp = GlobalsTool.getFieldBean("tblStockDet", gpInfo.getPropName());
						if (gpInfo.getIsUsed() == 1 && temp.getInputType() != DBFieldInfoBean.INPUT_HIDDEN)// 如果该属性启用并显示
						{
							if (gpInfo.getIsSequence() == 1) {// 如果是序列号，验证该商品是否启用了序列号
								rss = mgt.queryGoodsSet(goodsCode);
								if (rss.getRetCode() == ErrorCanst.DEFAULT_SUCCESS
										&& (Boolean) rss.getRetVal()) {
									goodsUseSeq = true;
									String[] values = new String[3];
									values[0] = gpInfo.getPropName();// 属性名称
									if (gpInfo.getDisplay() == null) {
										values[1] = values[0];
									} else {
										values[1] = gpInfo.getDisplay().get(getLocale(request).toString()).toString();// 属性值
									}
									values[2] = String.valueOf(gpInfo.getInputBill());// 是否单据录入
									existPropNames.add(values);
									if (gpInfo.getTwoUnit() == 1) {
										request.setAttribute("twoUnitPro", gpInfo.getPropName());
										request.setAttribute("twoUnitBillInput", values[2]);
									}
								}
							} else {
								String[] values = new String[3];
								values[0] = gpInfo.getPropName();// 属性名称
								if (gpInfo.getDisplay() == null) {
									values[1] = values[0];
								} else {
									values[1] = gpInfo.getDisplay().get(getLocale(request).toString()).toString();// 属性值
								}
								values[2] = String.valueOf(gpInfo.getInputBill());// 是否单据录入
								existPropNames.add(values);
								if (gpInfo.getTwoUnit() == 1) {
									request.setAttribute("twoUnitPro", gpInfo.getPropName());
									request.setAttribute("twoUnitBillInput", values[2]);
								}
							}

						}
					}
				}
				if (BaseEnv.systemSet.get("TwoUnit").getSetting().equals("true")) {
					existsTowUnit = true;
					request.setAttribute("existsTowUnit", existsTowUnit);
				}
				List existNVPropDis = new ArrayList();
				for (int i = 0; i < existPropNames.size(); i++) {
					GoodsPropInfoBean tempProp = GlobalsTool
							.getPropBean(((String[]) existPropNames.get(i))[0]);
					if (tempProp.getNameAndValue() == 1) {// 支持名称&值属性
						DBFieldInfoBean nvField = GlobalsTool.getFieldBean("tblStockDet", tempProp.getPropName()+ "NV");
						String[] values = new String[4];
						values[0] = tempProp.getPropName();
						values[1] = nvField.getFieldName();
						values[2] = nvField.getDisplay().get(getLocale(request).toString());
						values[3] = "";
						existNVPropDis.add(values);
					}
				}
				int costMethod = Integer.parseInt(BaseEnv.systemSet.get("GoodsCostMethod").getSetting());
				rss = iniGoodsMgt.queryGoodsPropValues(goodsCode, stockCode, existPropNames, existsTowUnit,
						existNVPropDis, goodsUseSeq, costMethod);
				List propValues = new ArrayList();
				if (rss.getRetCode() != ErrorCanst.DEFAULT_FAILURE) {
					propValues = (ArrayList) ((Object[]) rss.getRetVal())[0];
					if (Boolean.parseBoolean(((Object[]) rss.getRetVal())[1].toString())) {
						request.setAttribute("inputType", "Total");
					}
					for (int k = 0; k < propValues.size(); k++) {
						List row = (ArrayList) propValues.get(k);
						int index = 0;
						for (int p = 0; p < existPropNames.size(); p++) {
							rs = propMgt.queryPropName(((String[]) existPropNames.get(p))[0]);
							if (rs.getRetCode() != ErrorCanst.DEFAULT_FAILURE
									&& ((ArrayList) rs.getRetVal()).size() > 0) {
								GoodsPropInfoBean gpBean = (GoodsPropInfoBean) ((ArrayList) rs.getRetVal()).get(0);

								if (gpBean.getNameAndValue() != 1) {
									rs = propMgt.queryPropValue(gpBean.getId()
											, ((ArrayList) row.get(index)).get(0).toString()
											, goodsCode, getLocale(request).toString());
									
									String tempEnumValue = "";
									if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS
																	&& rs.getRetVal() != null) {
										tempEnumValue = rs.getRetVal().toString();
									} else {
										tempEnumValue = ((ArrayList) row.get(index)).get(1).toString();
									}
									if (gpBean.getIsSequence() == 1) {// 如果是序列号属性
										String[] seqList = tempEnumValue.split("~");
										tempEnumValue = seqList[seqList.length - 1];
									}
									((ArrayList) row.get(index)).remove(1);
									((ArrayList) row.get(index)).add(tempEnumValue);
									index += 1;
								} else {
									index += 2;
								}
							}
						}
					}
				}

				request.setAttribute("existsPropNames", existPropNames);
				request.setAttribute("props", propValues);
				request.setAttribute("nvPropNames", existNVPropDis);

				request.setAttribute("goodsCode", request.getParameter("goodsCode"));
				request.setAttribute("stockCode", request.getParameter("stockCode"));
				MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap()
																		.get("/IniGoodsQueryAction.do");
				request.setAttribute("MOID", mop.moduleId);
				rs = mgt.queryGoodsSet(goodsCode);
				boolean startSeq = false;
				if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
					startSeq = (Boolean) rs.getRetVal();
					if (startSeq) {
						DBFieldInfoBean df = GlobalsTool.getFieldBean("tblIniStockDet", "InstoreQty");
						if (df.getFieldSysType().equals("SeqQty")) {// 如果是序列号数量字段
							request.setAttribute("SeqQtyfName", "iniQty");
						}
					}
				}
				request.setAttribute("startSeq", startSeq);
				forward = getForward(request, mapping, "update");

			} else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
				// 记录不存在错误
				EchoMessage.error().add(getMessage(request, "common.error.nodata")).setRequest(request);
			} else {
				// 加载失败
				EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
			}
			if (forward == null) {
				forward = getForward(request, mapping, "message");
			}
		}
		return forward;
	}

    /**
	 * 修改
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
    protected ActionForward update(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
        Exception {
    	String goodsCode = request.getParameter("GoodsCode");
        String stockCode=request.getParameter("StockCode");
        String inputType=request.getParameter("inputType")==null?"":request.getParameter("inputType");
        Hashtable allTables = (Hashtable) request.getSession().
        getServletContext().getAttribute(BaseEnv.TABLE_INFO);
        ActionForward forward = null;
        forward = getForward(request, mapping, "alert");
        if(!currPeriod.equals("-1")){
            // 已经开账不能修改期初信息
            EchoMessage.error().add(getMessage(
                    request, "common.msg.RET_BEGINACC_END")).setBackUrl(
                            "/IniGoodsQueryAction.do").setAlertRequest(
                                    request);

            return forward;
        }
       
        // 是否启用负库存验证
        boolean negativeStock = Boolean.parseBoolean(BaseEnv.systemSet.get("NegativeStock").getSetting());
        LoginBean lg =(LoginBean) request.getSession().getAttribute("LoginBean");
        Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(lg.getId()) ;
        if ((Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString())&&OpenSunCompany.equals("true"))||OpenSunCompany.equals("false")) {
        	// 获取属性字段内容
        	IniGoodsMgt iniGoodsMgt=new IniGoodsMgt();
            GoodsPropMgt propMgt=new GoodsPropMgt();
            Result rss=null;
            List existPropNames=new ArrayList();
            List propValues=new ArrayList();
            List existNVNames=new ArrayList();
            List nvValues=new ArrayList();
               List fieldsName=iniGoodsMgt.queryStocksFieldsName("tblStockDet",allTables);
               for(int i=0;i<fieldsName.size();i++)
               {
            	  rss= propMgt.queryPropName(fieldsName.get(i).toString());
            	  if(rss.getRetCode()==ErrorCanst.DEFAULT_SUCCESS&&rss.getRetVal()!=null&&((ArrayList)rss.getRetVal()).size()>0)
            	  {
            		  GoodsPropInfoBean gpInfo=(GoodsPropInfoBean)((ArrayList)rss.getRetVal()).get(0);
            		  String[] values=new String[3];
            		  values[0]=gpInfo.getPropName();
            		  values[1]=gpInfo.getDisplay().get(getLocale(request).
                              toString()).toString();
            		  values[2]=String.valueOf(gpInfo.getIsCalculate());
            		  existPropNames.add(values);
            		  
            		  if(gpInfo.getNameAndValue()==1){//支持名称&值显示字段
            			  DBFieldInfoBean nvField=GlobalsTool.getFieldBean("tblStockDet",gpInfo.getPropName()+"NV");
            			  if(nvField!=null){
            				  existNVNames.add(nvField.getFieldName());
            			  }
            		  }
            	  }
               }
            if(existPropNames.size()!=0)
            {
            	for(int i=0;i<existPropNames.size();i++)
            	{
            		String []propName=(String [])existPropNames.get(i);
            		String[]values=request.getParameterValues("tblGoodsOfProp_"+propName[0]);
            		propValues.add(values);
            	}
            }
            if(existNVNames.size()!=0){
            	for(int i=0;i<existNVNames.size();i++){
            		String[] values=request.getParameterValues(existNVNames.get(i).toString());
            		nvValues.add(values);
            	}
            }

            String []IniTwoQty=request.getParameterValues("IniTwoQty");
            String []iniQty=request.getParameterValues("iniQty");
            String[]iniPrice=request.getParameterValues("iniPrice");
            String[]iniAmount=request.getParameterValues("iniAmount");
            if(iniAmount!=null){
	            for(int i=0;i<iniAmount.length;i++){
	            	String amount=iniAmount[i];
	            	if(iniAmount[i].indexOf(".")!=-1){
	            		amount=iniAmount[i].substring(0,iniAmount[i].indexOf("."));
	            	}
	            	if(amount.length()>10){
	           			EchoMessage.error().add(getMessage(
                                request, "goodsini.iniAmount.maxerror")).setBackUrl("/IniGoodsQueryAction.do?type=update&goodsCode=" +
                                		goodsCode+"&goodsNumber"+request.getParameter("goodsNumber")+"&goodsFullName="+java.net.URLEncoder.encode(request.getParameter("goodsFullName"), "UTF-8")+
                                		"&stockName="+java.net.URLEncoder.encode(request.getParameter("stockName"), "UTF-8") +"&dimQuery="+java.net.URLEncoder.encode(request.getParameter("dimQuery"), "UTF-8")+"&pageNo="+request.getParameter("pageNo")+"&winCurIndex="+request.getParameter("winCurIndex")).
                                setAlertRequest(request);
                        return forward;
	            	}
	            }
            }
            String[]StockLocation=request.getParameterValues("StockLocation");
            String[]hidSecUnit=request.getParameterValues("hidSecUnit");
            String[]conversionRate=request.getParameterValues("conversionRate");
            String[]secUnitQty=request.getParameterValues("secUnitQty");
            String[]secUnitPrice=request.getParameterValues("secUnitPrice");
            String[]hidProvider=request.getParameterValues("hidProvider");
            ArrayList notUsedPropNames=iniGoodsMgt.queryNotUsedPropNameInTable("tblStockDet",allTables);
            String seqPropfName=request.getParameter("SeqPropfName");
           
            if(BaseEnv.systemSet.get("TwoUnit").getSetting().equals("true")){//检查双单位
            	 for(int i=0;i<IniTwoQty.length;i++){
                 	if(IniTwoQty[i].replaceAll(" ","").equals("")){
                 		IniTwoQty[i]="0";
//                        EchoMessage.error().add(getMessage(
//                                request, "ini.input.twouint")).setBackUrl("/IniGoodsQueryAction.do?type=update&goodsCode=" +
//                                		goodsCode+"&goodsNumber"+request.getParameter("goodsNumber")+"&goodsFullName="+java.net.URLEncoder.encode(request.getParameter("goodsFullName"), "UTF-8")+
//                                		"&stockName="+java.net.URLEncoder.encode(request.getParameter("stockName"), "UTF-8") +"&dimQuery="+java.net.URLEncoder.encode(request.getParameter("dimQuery"), "UTF-8")+"&pageNo="+request.getParameter("pageNo")+"&winCurIndex="+request.getParameter("winCurIndex")).
//                                setAlertRequest(request);
//                        return forward;
                 	}else{
                 		try{
                 		Float.parseFloat(IniTwoQty[i].replaceAll(" ",""));
                 		}catch(Exception e){
                 			EchoMessage.error().add(getMessage(
                                    request, "ini.input.twouintformat")).setBackUrl("/IniGoodsQueryAction.do?type=update&goodsCode=" +
                                    		goodsCode+"&goodsNumber"+request.getParameter("goodsNumber")+"&goodsFullName="+java.net.URLEncoder.encode(request.getParameter("goodsFullName"), "UTF-8")+
                                    		"&stockName="+java.net.URLEncoder.encode(request.getParameter("stockName"), "UTF-8") +"&dimQuery="+java.net.URLEncoder.encode(request.getParameter("dimQuery"), "UTF-8")+"&pageNo="+request.getParameter("pageNo")+"&winCurIndex="+request.getParameter("winCurIndex")).
                                    setAlertRequest(request);
                            return forward;
                 		}
                 	}
                 }
            }
            String[]propStrArr=new String[iniQty.length];
            for(int i=0;i<iniQty.length;i++){
            	String propStr="";
				for (int j = 0; j < existPropNames.size(); j++) {
					String value="";
					if (propValues.get(j) != null) {
						value = ((String[]) propValues.get(j))[i];
					} 
					propStr+=value+"|";
				}
				propStrArr[i]=propStr;
            }
        	//执行修改
            Result rs = mgt.update(sunClassCode,StockLocation,goodsCode, stockCode,inputType,iniQty,iniPrice,iniAmount,IniTwoQty,negativeStock,existPropNames,propValues,lg.getId(),notUsedPropNames,seqPropfName,hidSecUnit,conversionRate,secUnitQty,secUnitPrice,hidProvider,existNVNames,nvValues,getLocale(request).toString(),propStrArr);

            if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {//修改成功
                EchoMessage.success().add(getMessage(
                        request, "common.msg.updateSuccess")).
                        setBackUrl("/IniGoodsQueryAction.do?type=back&goodsCode=" +
                        		goodsCode+"&goodsNumber"+request.getParameter("goodsNumber")+"&goodsFullName="+java.net.URLEncoder.encode(request.getParameter("goodsFullName"), "UTF-8")+
                        		"&stockName="+java.net.URLEncoder.encode(request.getParameter("stockName"), "UTF-8") +"&dimQuery="+java.net.URLEncoder.encode(request.getParameter("dimQuery"), "UTF-8")+"&pageNo="+request.getParameter("pageNo")+"&winCurIndex="+request.getParameter("winCurIndex")).
                        setAlertRequest(request);
            } else if(rs.getRetCode() == ErrorCanst.PROC_NEGATIVE_ERROR){
                //存储过程返回负库存错误
            	String[] str=rs.getRetVal().toString().split(";");String msg="";
            	if(str.length==1){
                     msg = this.getMessage(request,"common.error.negative2",str[0]);
            	}else{
            		 msg = this.getMessage(request,"common.error.negative",str[0],str[1]);
            	}
                EchoMessage.error().add(msg).setAlertRequest(request);
            } else if(rs.getRetCode()==ErrorCanst.GOODS_INI_MULTI){//同一商品相同属性，同一属性值对应多个属性名称
            	String[]str=(String[])rs.getRetVal();
            	String msg=this.getMessage(request, "goodsIni.multi.error", str[0], str[1],str[2]);
            	EchoMessage.error().add(msg).setAlertRequest(request);
            } else if(rs.getRetCode()==ErrorCanst.MULTI_VALUE_ERROR){//不存在属性时，不能录入多条数据
            	EchoMessage.error().add(this.getMessage(request, "goodsIni.data.more")).setAlertRequest(request);
            } else if(rs.getRetCode()==ErrorCanst.GOODS_INI_PROPDISPLAY){//录入属性值时，没录入属性值显示名
            	EchoMessage.error().add(this.getMessage(request, "goodsIni.data.propDisplay",rs.getRetVal().toString())).setAlertRequest(request);
            } else if(rs.getRetCode()==ErrorCanst.GOODS_INI_MULTI_VAL){//同一商品相同属性，同一属性名称对应多个属性值
            	String[]str=(String[])rs.getRetVal();
            	String msg=this.getMessage(request, "goodsIni.multival.error", str[0], str[1],str[2]);
            	EchoMessage.error().add(msg).setAlertRequest(request);
            }else{
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

        return  forward;
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
    protected ActionForward query(ActionMapping mapping, ActionForm form, 
    								HttpServletRequest request,
									HttpServletResponse response) throws Exception {
		IniGoodsForm thisForm = (IniGoodsForm) form;
		String stockName = getParameter("stockName", request);

		if (stockName != null && stockName.length() > 0) {
			thisForm.setStockName(stockName);
		}
		if (thisForm.getStockName() == null) {
			thisForm.setStockName("default");
		}
		//执行查询

		String type = request.getParameter("type");
		String goodsCode = request.getParameter("goodsCode") == null ? "" : request.getParameter("goodsCode");
		if (goodsCode.length() > 0 && type != null && ("uptate".equals(type) || "back".equals(type))) {
			goodsCode = goodsCode.substring(0, goodsCode.length() - 5);
		}

		if (type != null && !type.equals("query")) {
			if (thisForm.getGoodsFullName() != null)
				thisForm.setGoodsFullName(new String(thisForm.getGoodsFullName().getBytes("iso-8859-1"),"UTF-8"));
			if (thisForm.getDimQuery() != null)
				thisForm.setDimQuery(new String(thisForm.getDimQuery().getBytes("iso-8859-1"), "UTF-8"));
		}

		Hashtable map = (Hashtable) request.getSession().getServletContext()
				.getAttribute(BaseEnv.TABLE_INFO);

		int pageSize = getParameterInt("pageSize", request);
		int pageNo = getParameterInt("pageNo", request);
		if (pageNo == 0)
			pageNo = 1;
		if (pageSize == 0)
			pageSize = GlobalsTool.getPageSize();

		boolean existsTowUnit = false;
		if (BaseEnv.systemSet.get("TwoUnit").getSetting().equals("true")) {
			existsTowUnit = true;
		}
		request.setAttribute("existsTowUnit", existsTowUnit);

		request.setAttribute("pageNo", pageNo);
		
		Result rs=null;
		
		if("default".equals(thisForm.getStockName())){
			rs=new Result();
		}else{
			rs = mgt.query(sunClassCode, goodsCode, type, map, thisForm.getGoodsNumber(), thisForm
					.getGoodsFullName(), thisForm.getStockName(), thisForm.getStoreStateID(), thisForm
					.getDimQuery(), pageNo, pageSize, existsTowUnit);
		}

		ReportSetMgt mgt = new ReportSetMgt();
		String locale = GlobalsTool.getLocale(request).toString();
		Result rsPrint = mgt.getReportSetInfo("IniReportGoods", locale);
		if (rsPrint.getRealTotal() > 0) {
			ReportsBean report = (ReportsBean) rsPrint.getRetVal();
			Map det = report.getReportDetBean();
			Collection con = det.values();
			Iterator iter = con.iterator();
			while (iter.hasNext()) {
				ReportsDetBean detBean = (ReportsDetBean) iter.next();
				if (detBean.getNewFlag().equals("OLD")) {
					request.setAttribute("print", true);
					break;
				}
			}
		}

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//查询成功
			if (rs.getRealTotal() == 0) {
				if (type != null && type.equals("next")) {
					return updatePrepare(mapping, form, request, response);
				}
			}

			if (type != null && !type.equals("query") && !type.equals("update")) {
				thisForm.setGoodsFullName("");
				thisForm.setDimQuery("");
				thisForm.setGoodsNumber("");
			}
			IniGoodsMgt iniGoodsMgt = new IniGoodsMgt();
			GoodsPropMgt propMgt = new GoodsPropMgt();
			Result rss = null;
			List existPropNames = new ArrayList();
			List fieldsName = iniGoodsMgt.queryStocksFieldsName("tblStocks", map);
			for (int i = 0; i < fieldsName.size(); i++) {
				rss = propMgt.queryPropName(fieldsName.get(i).toString());
				if (rss.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && rss.getRetVal() != null
						&& ((ArrayList) rss.getRetVal()).size() > 0) {
					GoodsPropInfoBean gpInfo = (GoodsPropInfoBean) ((ArrayList) rss.getRetVal()).get(0);
					if (gpInfo.getIsUsed() == 1)// 如果该属性启用
					{
						String[] values = new String[3];
						values[0] = gpInfo.getPropName();// 属性名称
						if (gpInfo.getDisplay() != null) {
							values[1] = gpInfo.getDisplay().get(getLocale(request).toString()).toString();// 属性值
						} else {
							values[1] = values[0];// 属性值
						}
						values[2] = String.valueOf(gpInfo.getInputBill());// 是否单据录入
						existPropNames.add(values);
					}
				}
			}
			
			/**加上当前商品所在的当前位置**/
			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, "tblGoods");
			String strGoodsCode = getParameter("goodsCode", request)==null?"":getParameter("goodsCode", request) ;
			if("back".equals(type) && strGoodsCode.length()>=5){
				strGoodsCode = strGoodsCode.substring(0,strGoodsCode.length()-5) ;
			}
			Result rs3 = new ReportDataMgt().getParentName(strGoodsCode, tableInfo, locale);
			if (rs3.retCode == ErrorCanst.RET_NOTSETROWMARKER) {
				request.setAttribute("parentName", "");
			} else {
				ArrayList parentName = (ArrayList) rs3.retVal;
				String parentUrl = "";
				if (parentName.size() == 0) {
					parentUrl += GlobalsTool.getMessage(request, "com.acc.ini.root");
				} else {
					parentUrl += "<a href=\"/IniGoodsQueryAction.do?goodsCode=&type=back" 
							     +"&stockName="+stockName + "&winCurIndex="
							     + request.getParameter("winCurIndex") + "\">"
							     + GlobalsTool.getMessage(request, "com.acc.ini.root") + "</a> >> ";
					for (int i = 0; i < parentName.size(); i++) {
						String[] nameClass = (String[]) parentName.get(i);
						if (nameClass[1].length() == (parentName.size()) * 5) {
							parentUrl += nameClass[0];
						} else {
							parentUrl += "<a href=\"/IniGoodsQueryAction.do?&goodsCode=" + nameClass[1]+"00001" + "&type=back"
									+"&stockName="+stockName+ "&winCurIndex="
									+ request.getParameter("winCurIndex") + "\">" + nameClass[0]
									+ "</a> >> ";
						}

					}
				}
				request.setAttribute("parentName", parentUrl);
			}

			Result stock_rs = iniGoodsMgt.queryAllStocks();
			request.setAttribute("stockList", (ArrayList<StockBean>) stock_rs.getRetVal());
			request.setAttribute("existPropNames", existPropNames);
			request.setAttribute("goodsCode", goodsCode);
			request.setAttribute("result", rs.getRetVal());
			request.setAttribute("pageBar", pageBar(rs, request));
			request.setAttribute("currPeriod", currPeriod);
		} else {
			//查询失败
			EchoMessage.error().add(getMessage(request, "common.msg.error")).setRequest(request);
			return getForward(request, mapping, "message");
		}

		return getForward(request, mapping, "list");
	}

}
