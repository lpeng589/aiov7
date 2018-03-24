package com.menyi.aio.web.mrp;


import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.billNumber.BillNo;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConfirmBean;
import com.menyi.web.util.DefineSQLBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;


public class MrpMgt extends AIODBManager {
	DynDBManager dbmgt = new DynDBManager();

	UserFunctionMgt userFunMgt = new UserFunctionMgt();
	
	public Result toProduceRequire(ArrayList<HashMap> goodList,LoginBean lg,HttpServletRequest request){
		Result rs = new Result();
		//分为制造需求单和请购单
		ArrayList<HashMap> produceList = new ArrayList<HashMap>();
		ArrayList<HashMap> buyList = new ArrayList<HashMap>();
		for(HashMap map:goodList){
			String attrType= map.get("attrType")+"";
			if(Double.parseDouble(map.get("Qty")+"")<=0){
				continue;
			}
			if("0".equals(attrType)||"2".equals(attrType)){
				produceList.add(map);
			}else{
				buyList.add(map);
			}
		}
		if(produceList.size()==0&&buyList.size()==0){
			rs.retCode=ErrorCanst.DEFAULT_FAILURE;
			rs.retVal="补货量全部为0，无需处理";
			return rs;
		}
		
		HashMap retMap = new HashMap();
		
		if(buyList.size() > 0){//采购申请单
			HashMap values = new HashMap();
			values.put("BillDate", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
			values.put("DepartmentCode", lg.getDepartCode());
			values.put("EmployeeID", lg.getId());
			
			
			ArrayList clist=new ArrayList();
			values.put("TABLENAME_tblBuyApplicationDet", clist);
			clist.addAll(buyList);
	
			/*保存在内存中所有表结构的信息*/
			Hashtable map = BaseEnv.tableInfos;
			
			/*父类的classCode*/
			String parentCode = "";
			//要执行的define的信息
			String defineInfo = "";
			/*设置兄弟表的f_brother*/
			String tableName = "tblBuyApplication";
			String saveType = "saveDraft"; //保存类型 saveDraft 草稿
			
			
	
			/*表结构信息*/
			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, tableName);
	
			/*从内存中读取当前单据的工作流信息*/
			MOperation mop = (MOperation)lg.getOperationMap().get("/UserFunctionQueryAction.do?tableName=tblBuyApplication");
			if(mop==null || !mop.add){
				rs.retCode = ErrorCanst.DEFAULT_FAILURE;
				rs.retVal = "您没有添加请购单的权限";
				return rs;
			}
			OAWorkFlowTemplate workFlow = null;
	
			workFlow = BaseEnv.workFlowInfo.get(tableName);
			//System.out.println("*****workFlow********"+workFlow);
			/*初始化一些字段的基本信息*/
			Result rs_initDBField = userFunMgt.initDBFieldInfo(tableInfo, lg, values, parentCode, workFlow);
			/* fjj不成功 删除单据编号*/
			if (rs_initDBField.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				rs.retCode = ErrorCanst.DEFAULT_FAILURE;
				rs.retVal = "初始化字段错误";
				return rs;
			}
			if (rs_initDBField.retCode == ErrorCanst.CURRENT_ACC_BEFORE_BILL) {
				/*不能录入会计前数据*/
				rs.retCode = ErrorCanst.DEFAULT_FAILURE;
				rs.retVal = GlobalsTool.getMessage(request, "com.currentaccbefbill");
				return rs;
			} else if (rs_initDBField.retCode == ErrorCanst.BILL_DATE_NOT_EXIST_CURRENT_ACC) {
				/*单据日期的期间在会计期间中不存在*/
				rs.retCode = ErrorCanst.DEFAULT_FAILURE;
				rs.retVal = "单据日期的期间在会计期间中不存在";
				return rs;
			}
	
			//获取路径
			String path = request.getSession().getServletContext().getRealPath("DoSysModule.sql");
	
			Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
			MessageResources resources = null;
			if (ob instanceof MessageResources) {
				resources = (MessageResources) ob;
			}
			String deliverTo = request.getParameter("deliverTo");
			Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
	
			//zxy 将midCalculate 写入value中，方便 在define中以@valueOfDB:midCalculate=true来判断当前操作为中间运算
			boolean isMidCalculate = false;
			if ("midCalculate".equals(saveType)) {
				saveType = "saveAdd";
				isMidCalculate = true;
				values.put("midCalculate", "true");
			} 
			if("saveDraft".equals(saveType)){
		    	values.put("workFlowNodeName", "draft") ;
		    }else if("printSave".equals(saveType)){
		    	values.put("workFlowNodeName", "print") ;
		    	request.setAttribute("saveType", saveType);
		    }
			Locale loc = (Locale)request.getSession(true).getAttribute(org.apache.struts.Globals.LOCALE_KEY);
			rs = dbmgt.add(tableInfo.getTableName(), map, values, lg.getId(), path, defineInfo, resources, loc, saveType, lg, workFlow, deliverTo, props);
	
			String kid = values.get("id") == null ? "" : values.get("id").toString();
			request.setAttribute("BillId", kid);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS || rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
	
				String billTypeName = tableInfo.getDisplay().get(loc.toString());
				
				String[] returnValue = (String[]) rs.retVal;
				/*当前多语言种类*/
				String locale = loc.toString();
				String addMessage = GlobalsTool.getMessage(request, "common.lb.add");
				/*添加系统日志*/
				int operation = 0;
				if ("saveDraft".equals(saveType))
					operation = 5;
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					dbmgt.addLog(operation, values, null, tableInfo, locale.toString(), lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(), "", billTypeName);
				}
	
				String message = GlobalsTool.getMessage(request, "common.msg.addSuccess");
				if (returnValue[1] != null && returnValue.length > 0) {
					if (rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
						message = dbmgt.getDefSQLMsg(loc.toString(), returnValue[1]);
					} else {
						message += "<br>" + GlobalsTool.getMessage(request, "userfunction.msg.billNoExistNew") + ":" + returnValue[1];
					}
				}
				retMap.put("msg", message);
				retMap.put("BuykeyId", kid);
				rs.retVal=retMap;
			} else {
				/*失败清空单据编号*/
				if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
					// 自定义配置需要用户确认
					ConfirmBean confirm = (ConfirmBean) rs.getRetVal();
					String content = dbmgt.getDefSQLMsg(loc.toString(), confirm.getMsgContent());
					String jsConfirmYes = "";
					String jsConfirmNo = "";
					String saveAdd = request.getParameter("button");
	
					jsConfirmYes = confirm.getFormerDefine() + ":" + confirm.getYesDefine() + ";";
					if (confirm.getNoDefine().length() > 0) {
						jsConfirmNo = confirm.getFormerDefine() + ":" + confirm.getNoDefine() + ";";
					}
					rs.retVal="请购单不应该配置确认提示";
					return rs;
				} else {
					SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, loc.toString(), saveType);
					rs.retVal=saveErrrorObj.getMsg();
					return rs;
				}
			}
		}

		if(produceList.size() > 0){//制造需求单 
			HashMap values = new HashMap();
			values.put("BillDate", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
			values.put("DepartmentCode", lg.getDepartCode());
			values.put("EmployeeID", lg.getId());
			
			
			ArrayList clist=new ArrayList();
			values.put("TABLENAME_PDProduceRequireDet", clist);
			clist.addAll(produceList);
	
			/*保存在内存中所有表结构的信息*/
			Hashtable map = BaseEnv.tableInfos;
			
			/*父类的classCode*/
			String parentCode = "";
			//要执行的define的信息
			String defineInfo = "";
			/*设置兄弟表的f_brother*/
			String tableName = "PDProduceRequire";
			String saveType = "saveDraft"; //保存类型 saveDraft 草稿
			
			
	
			/*表结构信息*/
			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, tableName);
	
			/*从内存中读取当前单据的工作流信息*/
			MOperation mop = (MOperation)lg.getOperationMap().get("/UserFunctionQueryAction.do?tableName=PDProduceRequire");
			if(mop==null || !mop.add){
				rs.retCode = ErrorCanst.DEFAULT_FAILURE;
				rs.retVal = "您没有添加制造需求单的权限";
				return rs;
			}
			OAWorkFlowTemplate workFlow = null;
	
			workFlow = BaseEnv.workFlowInfo.get(tableName);
			//System.out.println("*****workFlow********"+workFlow);
			/*初始化一些字段的基本信息*/
			Result rs_initDBField = userFunMgt.initDBFieldInfo(tableInfo, lg, values, parentCode, workFlow);
			/* fjj不成功 删除单据编号*/
			if (rs_initDBField.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				rs.retCode = ErrorCanst.DEFAULT_FAILURE;
				rs.retVal = "初始化字段错误";
				return rs;
			}
			if (rs_initDBField.retCode == ErrorCanst.CURRENT_ACC_BEFORE_BILL) {
				/*不能录入会计前数据*/
				rs.retCode = ErrorCanst.DEFAULT_FAILURE;
				rs.retVal = GlobalsTool.getMessage(request, "com.currentaccbefbill");
				return rs;
			} else if (rs_initDBField.retCode == ErrorCanst.BILL_DATE_NOT_EXIST_CURRENT_ACC) {
				/*单据日期的期间在会计期间中不存在*/
				rs.retCode = ErrorCanst.DEFAULT_FAILURE;
				rs.retVal = "单据日期的期间在会计期间中不存在";
				return rs;
			}
	
			//获取路径
			String path = request.getSession().getServletContext().getRealPath("DoSysModule.sql");
	
			Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
			MessageResources resources = null;
			if (ob instanceof MessageResources) {
				resources = (MessageResources) ob;
			}
			String deliverTo = request.getParameter("deliverTo");
			Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
	
			//zxy 将midCalculate 写入value中，方便 在define中以@valueOfDB:midCalculate=true来判断当前操作为中间运算
			boolean isMidCalculate = false;
			if ("midCalculate".equals(saveType)) {
				saveType = "saveAdd";
				isMidCalculate = true;
				values.put("midCalculate", "true");
			} 
			if("saveDraft".equals(saveType)){
		    	values.put("workFlowNodeName", "draft") ;
		    }else if("printSave".equals(saveType)){
		    	values.put("workFlowNodeName", "print") ;
		    	request.setAttribute("saveType", saveType);
		    }
			Locale loc = (Locale)request.getSession(true).getAttribute(org.apache.struts.Globals.LOCALE_KEY);
			rs = dbmgt.add(tableInfo.getTableName(), map, values, lg.getId(), path, defineInfo, resources, loc, saveType, lg, workFlow, deliverTo, props);
	
			String kid = values.get("id") == null ? "" : values.get("id").toString();
			request.setAttribute("BillId", kid);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS || rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
	
				String billTypeName = tableInfo.getDisplay().get(loc.toString());
				
				String[] returnValue = (String[]) rs.retVal;
				/*当前多语言种类*/
				String locale = loc.toString();
				String addMessage = GlobalsTool.getMessage(request, "common.lb.add");
				/*添加系统日志*/
				int operation = 0;
				if ("saveDraft".equals(saveType))
					operation = 5;
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					dbmgt.addLog(operation, values, null, tableInfo, locale.toString(), lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(), "", billTypeName);
				}
	
				String message = GlobalsTool.getMessage(request, "common.msg.addSuccess");
				if (returnValue[1] != null && returnValue.length > 0) {
					if (rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
						message = dbmgt.getDefSQLMsg(loc.toString(), returnValue[1]);
					} else {
						message += "<br>" + GlobalsTool.getMessage(request, "userfunction.msg.billNoExistNew") + ":" + returnValue[1];
					}
				}
				retMap.put("msg", message);
				retMap.put("ProdkeyId", kid);
				rs.retVal=retMap;
			} else {
				/*失败清空单据编号*/
				if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
					// 自定义配置需要用户确认
					ConfirmBean confirm = (ConfirmBean) rs.getRetVal();
					String content = dbmgt.getDefSQLMsg(loc.toString(), confirm.getMsgContent());
					String jsConfirmYes = "";
					String jsConfirmNo = "";
					String saveAdd = request.getParameter("button");
	
					jsConfirmYes = confirm.getFormerDefine() + ":" + confirm.getYesDefine() + ";";
					if (confirm.getNoDefine().length() > 0) {
						jsConfirmNo = confirm.getFormerDefine() + ":" + confirm.getNoDefine() + ";";
					}
					rs.retVal="请购单不应该配置确认提示";
					return rs;
				} else {
					SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, loc.toString(), saveType);
					rs.retVal=saveErrrorObj.getMsg();
					return rs;
				}
			}
		}
		
		return rs;
	}
	
	/**
	 * 订单缺料选择订单
	 * @param GoodsFList
	 * @return
	 */
	public Result selSalesOrderForPD(String BillDate1,String BillDate2,String BillNo,String CompanyCode,ArrayList<String[]> ComFList){
		String ComF="";
		for(String[] ss :ComFList){
			ComF +=",tblCompany."+ss[0];
		}
		String sql="";
		
		sql =  " select a.id, a.BillDate,a.BillNo"+ComF+",c.EmpFullName,ReplyDate from tblSalesOrder a join tblCompany  on a.CompanyCode=tblCompany.classCode "
				+ "join tblEmployee c on a.CreateBy=c.id  "
				+ "where a.statusId=0 and a.workFlowNodeName='finish' ";
		ArrayList param = new ArrayList();
		if(BillDate1!=null && BillDate1.length() > 0){
			sql +=" and a.BillDate >=? ";
			param.add(BillDate1);
		}
		if(BillDate2!=null && BillDate2.length() > 0){
			sql +=" and a.BillDate <=? ";
			param.add(BillDate2);
		}
		
		if(BillNo!=null && BillNo.length() > 0){
			sql +=" and a.BillNo like '%'+?+'%' ";
			param.add(BillNo);
		}
		if(CompanyCode!=null && CompanyCode.length() > 0){
			sql +=" and tblCompany.classCode = ? ";
			param.add(CompanyCode.split("#;#")[0]);
		}
		sql += " order by ReplyDate ";
		return  sqlListMap(sql, param);
	}
	/**
	 * 计算当前订单的缺料情况。
	 * @param GoodsFList
	 * @return
	 */
	public Result showSalesOrderLack(String[] salesOrderIds,ArrayList<String[]> GoodsFList){
		String GoodsF = "";
		String ComF="";
		for(String[] ss :GoodsFList){
			GoodsF +=",tblGoods."+ss[0];
		}
		String sid="";
		for(String id:salesOrderIds){
			sid +=",'"+id+"'";
		}
		sid = sid.substring(1);
		
		String sql="";
		
		sql =  " select b.GoodsCode,b.BatchNo,yearNO,Inch,Hue, sum( b.NotOutQty) NotOutQty"+GoodsF+",attrType from tblSalesOrder a "
				+ "join tblSalesOrderDet b on a.id=b.f_ref "
				+ "join tblGoods on b.GoodsCode=tblGoods.classCode "
				+ "where attrType in (0,2) and a.id in ("+sid+") group by b.GoodsCode,b.BatchNo,yearNO,Inch,Hue,attrType "+GoodsF;
		ArrayList param = new ArrayList();
		Result rs = sqlListMap(sql, param);
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			return rs;
		}
		ArrayList<HashMap> mapList = (ArrayList<HashMap>)rs.retVal;
		ArrayList<HashMap> removeList= new ArrayList<HashMap>();
		for(HashMap map :mapList){
			String GoodsCode = map.get("GoodsCode")+"";
			String BatchNo = map.get("BatchNo")+"";
			String yearNO = map.get("yearNO")+"";
			String Inch = map.get("Inch")+"";
			String Hue = map.get("Hue")+"";
			
			//计算本商品的当前库存
			param.clear();
			sql = "select SUM( lastQty) lastQty from tblStocks where GoodsCode=?";
			param.add(GoodsCode);
			if(BatchNo != null && !BatchNo.equals("")){
				sql +=" and batchNo=?";
				param.add(BatchNo);
			}
			if(yearNO != null && !yearNO.equals("")){
				sql +=" and yearNO=?";
				param.add(yearNO);
			}
			if(Inch != null && !Inch.equals("")){
				sql +=" and Inch=?";
				param.add(Inch);
			}
			if(Hue != null && !Hue.equals("")){
				sql +=" and Hue=?";
				param.add(Hue);
			}
			rs =sqlListMap(sql, param);
			if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
				return rs;
			}
			ArrayList<HashMap> mapo = (ArrayList<HashMap>)rs.retVal;
			if(mapo.size() > 0){
				map.put("lastQty", mapo.get(0).get("lastQty"));
			}else{
				map.put("lastQty", 0);
			}
			if((map.get("attrType")+"").equals("0")||(map.get("attrType")+"").equals("2")){ //自制件或委外件
				//计算本商品的在制量
				param.clear();
				sql = "select sum(b.qty-b.PDInQty) prodQty from PDProduceRequire a "
						+ "join PDProduceRequireDet b on a.id=b.f_ref where a.statusId=0 and b.GoodsCode=? ";
				param.add(GoodsCode);
				if(BatchNo != null && !BatchNo.equals("")){
					sql +=" and batchNo=?";
					param.add(BatchNo);
				}
				if(yearNO != null && !yearNO.equals("")){
					sql +=" and yearNO=?";
					param.add(yearNO);
				}
				if(Inch != null && !Inch.equals("")){
					sql +=" and Inch=?";
					param.add(Inch);
				}
				if(Hue != null && !Hue.equals("")){
					sql +=" and Hue=?";
					param.add(Hue);
				}
				
			}else{//采购件等
				//计算本商品的采购在途
//				param.clear();
//				sql = "select sum(b.qty- b.InQty) prodQty from tblBuyApplication a "
//						+ "join tblBuyApplicationDet b on a.id=b.f_ref where a.statusId not in (1,2) and b.GoodsCode=? ";
//				param.add(GoodsCode);
//				if(BatchNo != null && !BatchNo.equals("")){
//					sql +=" and batchNo=?";
//					param.add(BatchNo);
//				}
//				if(yearNO != null && !yearNO.equals("")){
//					sql +=" and yearNO=?";
//					param.add(yearNO);
//				}
//				if(Inch != null && !Inch.equals("")){
//					sql +=" and Inch=?";
//					param.add(Inch);
//				}
//				if(Hue != null && !Hue.equals("")){
//					sql +=" and Hue=?";
//					param.add(Hue);
//				}
			}
			rs =sqlListMap(sql, param);
			if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
				return rs;
			}
			mapo = (ArrayList<HashMap>)rs.retVal;
			if(mapo.size() > 0){
				map.put("prodQty", mapo.get(0).get("prodQty"));
			}else{
				map.put("prodQty", 0);
			}
			
			Double NotOutQty = Double.parseDouble(map.get("NotOutQty")+"");
			Double lastQty = Double.parseDouble(map.get("lastQty")+"");
			Double prodQty = Double.parseDouble(map.get("prodQty")+"");
			Double lackQty =  NotOutQty-lastQty-prodQty;
			map.put("lackQty",lackQty<0?0:lackQty);
			if(lackQty<=0){
				removeList.add(map);
			}
		}
		//mapList.removeAll(removeList);
		
		Comparator comparator=new Comparator(){
			public int compare(Object arg0, Object arg1) {
				  HashMap m0=(HashMap)arg0;
				  HashMap m1=(HashMap)arg1;
 
				  Double d0=Double.parseDouble(m0.get("lackQty")+"");
				  Double d1=Double.parseDouble(m1.get("lackQty")+"");

				  int flag=d1.compareTo(d0);
				  return flag;
			}
		}; 
		Collections.sort(mapList, comparator);
		
		rs.retVal = mapList;
		return rs;
	}
	
	/**
	 * 可下工令
	 * @param keyId
	 * @return
	 */
	public Result showWorkOrder(String mrpId){
		String GoodsFields = "";
		for (String[] shows : BaseEnv.reportShowSet) { 
			if ("tblGoods".equals(shows[0]) && "1".equals(shows[2])) {
				GoodsFields +=",tblGoods."+shows[1];
			}
		}
		String sql = "select count(0) cout from PDMRPReqDet where f_ref=?";
		ArrayList param = new ArrayList();
		param.add(mrpId); 
		Result rs = sqlList(sql,param);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList)(rs.retVal)).size() > 0){
			Object[] os= ((ArrayList<Object[]>)(rs.retVal)).get(0);
			if(Integer.parseInt(os[0]+"")==0){
				rs.retVal = "请先进行物料需求运算";
				rs.retCode=ErrorCanst.DEFAULT_FAILURE;
				return rs;
			}
		}
		//是否下达请购,dtype 3,自制不合并，5委外不合并
		sql = "  select 1 orderno,CONVERT(varchar(50),id) id,GoodsCode,needWQty,ISNULL(workOrderQty,0)+isnull((select sum(qty) from PDWorkOrder where workflowNodeName !='finish' and MrpId=PDMRPBom.f_ref and mrpBomId=PDMRPBom.id),0) workOrderQty,needWQty-ISNULL(workOrderQty,0)-isnull((select sum(qty) from PDWorkOrder where workflowNodeName !='finish' and MrpId=PDMRPBom.f_ref and mrpBomId=PDMRPBom.id),0) Qty "
				+ " from PDMRPBom where dtype in ('3','5') and isOpen=1 and ISNULL(needWQty,0)>0 and f_ref=? "
				+ "union "
				+ "select 2 orderno, id,GoodsCode,needQty,ISNULL(workOrderQty,0) +isnull((select sum(qty) from PDWorkOrder where workflowNodeName !='finish' and MrpId=PDMRP.id  and isnull(mrpBomId,'')=''),0) workOrderQty,needQty-ISNULL(workOrderQty,0)-isnull((select sum(qty) from PDWorkOrder where workflowNodeName !='finish' and MrpId=PDMRP.id and isnull(mrpBomId,'')='' ),0) Qty "
				+ "from PDMRP where MRPType in (3,4) and id=?";
		sql = " select a.* "+GoodsFields+"  from ("+sql+")a join tblGoods  on a.GoodsCode=tblGoods.classCode order by orderno";
		param = new ArrayList();
		param.add(mrpId);   
		param.add(mrpId);
		return sqlListMap(sql,param);
		
	}
	/**
	 * 下达工令
	 * @param keyId
	 * @return
	 */
	public Result toWorkOrder(final String mrpId,final ArrayList<HashMap> list,final LoginBean lb){
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                            SQLException {
                    	CallableStatement cs=null;
                        try {
                        	for(HashMap map :list){
	                        	String sql = "{call proc_toPDWorkOrderMRP(?,?,?,?,?,?)}";
	                        	cs= conn.prepareCall(sql);
	                        	cs.setString(1, map.get("type")+"");
	                        	cs.setString(2, map.get("id")+"");
	                        	cs.setDouble(3, Double.parseDouble(map.get("Qty")+""));
	                        	cs.setString(4, lb.getId());
	                            cs.registerOutParameter(5, Types.INTEGER);
	                            cs.registerOutParameter(6, Types.VARCHAR, 50);
	                            cs.execute();
	                            rst.setRetCode(cs.getInt(5));
	                            rst.setRetVal(cs.getString(6));
	                            
	                            if(BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT){
	                	            SQLWarning warn = cs.getWarnings();
	                	            while(warn != null){  
	                	            	if(warn.getMessage() !=null && warn.getMessage().indexOf("正在直接执行 SQL；无游标") == -1){
	                	            		BaseEnv.log.debug("存储过程内部信息： "+warn.getMessage());
	                	            	}
	                	            	warn = warn.getNextWarning();
	                	            }
	                            }	
                        	}
                        	
                        }catch(SQLException ex){
                        	try{
                        		if(BaseEnv.log.getLevel().DEBUG_INT == Level.DEBUG.DEBUG_INT){
                    	            SQLWarning warn = cs.getWarnings();
                    	            while(warn != null){  
                    	            	if(warn.getMessage() !=null && warn.getMessage().indexOf("正在直接执行 SQL；无游标") == -1){
                    	            		BaseEnv.log.debug("存储过程内部信息： "+warn.getMessage());
                    	            	}
                    	            	warn = warn.getNextWarning();
                    	            }
                                }	
                        	}catch(SQLException ex2){
                        		BaseEnv.log.error("MrpMgt.toWorkOrder Error",ex);
                        	}
                        	BaseEnv.log.error("MrpMgt.toWorkOrder Error",ex);
                        	BaseEnv.log.error("MrpMgt.mrpStocks",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            rst.setRetVal(ex.getMessage());
                            return;
                        } catch (Exception ex) {
                            BaseEnv.log.error("MrpMgt.mrpStocks",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            rst.setRetVal(ex.getMessage());
                            return;
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
	 * 检查是否已下请购
	 * @param keyId
	 * @return
	 */
	public Result hasBuy(){
		//是否下达请购
		String sql = " select count(0) cs from tblBuyApplication where isnull(isMRP,'') ='yes' and workflowNodeName != 'finish' ";
		ArrayList param = new ArrayList();
		return sqlList(sql,param);
		
	}
	
	 /** 下达请购单
	 * @param keyId
	 * @param request
	 * @return
	 */
	public Result toBuyApplication(final String mrpId,final ArrayList<HashMap> goodsList,final LoginBean lg,HttpServletRequest request){
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                            SQLException {
                    	PreparedStatement cs=null;
                        try {
                        	//先把要请购的内容写入临时表
                        	String buyId=IDGenerater.getId();
                        	for(HashMap map :goodsList){
	                        	String sql = "insert into PDToBuyApplication(f_ref,GoodsCode,Qty) values(?,?,?)";
	                        	cs= conn.prepareStatement(sql);
	                        	cs.setString(1, buyId);
	                        	cs.setString(2, map.get("GoodsCode")+"");
	                        	cs.setString(3, map.get("Qty")+"");
	                            cs.execute();
                        	}
                        	//执行define
                        	DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("PDToBuyApplication");
							if (defineSqlBean != null) {
								HashMap map = new HashMap();
								map.put("buyId", buyId);
								map.put("mrpId", mrpId);
								Result rs3 = defineSqlBean.execute(conn, map,lg.getId(), null, null, "");
								if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
									BaseEnv.log.debug(rs3.retVal);
									rst.setRetVal(rs3.retVal);
									rst.setRetCode(rs3.retCode);
									return;
								}
							}
							String sql = "delete PDToBuyApplication where f_ref=?";
                        	cs= conn.prepareStatement(sql);
                        	cs.setString(1, buyId);
                            cs.execute();
                        } catch (Exception ex) {
                            BaseEnv.log.error("MrpMgt.toBuyApplication",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            rst.setRetVal(ex.getMessage());
                            return;
                        }
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        return rst;
		
		
//		Result rs = new Result();
//		
//		
//		HashMap values = new HashMap();
//		values.put("BillDate", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
//		values.put("DepartmentCode", lg.getDepartCode());
//		values.put("EmployeeID", lg.getId());
//		values.put("isMRP", "yes");
//		values.put("MRPSalesOrderId", mrpId);
//		
//		
//		ArrayList clist=new ArrayList();
//		values.put("TABLENAME_tblBuyApplicationDet", clist);
//		clist.addAll(goodsList);
//
//		/*保存在内存中所有表结构的信息*/
//		Hashtable map = BaseEnv.tableInfos;
//		
//		/*父类的classCode*/
//		String parentCode = "";
//		//要执行的define的信息
//		String defineInfo = "";
//		/*设置兄弟表的f_brother*/
//		String tableName = "tblBuyApplication";
//		String saveType = "saveDraft"; //保存类型 saveDraft 草稿
//		
//		
//
//		/*表结构信息*/
//		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, tableName);
//
//		/*从内存中读取当前单据的工作流信息*/
//		MOperation mop = (MOperation)lg.getOperationMap().get("/UserFunctionQueryAction.do?tableName=tblBuyApplication");
//		if(mop==null || !mop.add){
//			rs.retCode = ErrorCanst.DEFAULT_FAILURE;
//			rs.retVal = "您没有添加请购单的权限";
//			return rs;
//		}
//		OAWorkFlowTemplate workFlow = null;
//
//		workFlow = BaseEnv.workFlowInfo.get(tableName);
//		//System.out.println("*****workFlow********"+workFlow);
//		/*初始化一些字段的基本信息*/
//		Result rs_initDBField = userFunMgt.initDBFieldInfo(tableInfo, lg, values, parentCode, workFlow, request);
//		/* fjj不成功 删除单据编号*/
//		if (rs_initDBField.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//			rs.retCode = ErrorCanst.DEFAULT_FAILURE;
//			rs.retVal = "初始化字段错误";
//			return rs;
//		}
//		if (rs_initDBField.retCode == ErrorCanst.CURRENT_ACC_BEFORE_BILL) {
//			/*不能录入会计前数据*/
//			rs.retCode = ErrorCanst.DEFAULT_FAILURE;
//			rs.retVal = GlobalsTool.getMessage(request, "com.currentaccbefbill");
//			return rs;
//		} else if (rs_initDBField.retCode == ErrorCanst.BILL_DATE_NOT_EXIST_CURRENT_ACC) {
//			/*单据日期的期间在会计期间中不存在*/
//			rs.retCode = ErrorCanst.DEFAULT_FAILURE;
//			rs.retVal = "单据日期的期间在会计期间中不存在";
//			return rs;
//		}
//
//		//获取路径
//		String path = request.getSession().getServletContext().getRealPath("DoSysModule.sql");
//
//		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
//		MessageResources resources = null;
//		if (ob instanceof MessageResources) {
//			resources = (MessageResources) ob;
//		}
//		String deliverTo = request.getParameter("deliverTo");
//		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
//
//		//zxy 将midCalculate 写入value中，方便 在define中以@valueOfDB:midCalculate=true来判断当前操作为中间运算
//		boolean isMidCalculate = false;
//		if ("midCalculate".equals(saveType)) {
//			saveType = "saveAdd";
//			isMidCalculate = true;
//			values.put("midCalculate", "true");
//		} 
//		if("saveDraft".equals(saveType)){
//	    	values.put("workFlowNodeName", "draft") ;
//	    }else if("printSave".equals(saveType)){
//	    	values.put("workFlowNodeName", "print") ;
//	    	request.setAttribute("saveType", saveType);
//	    }
//		Locale loc = (Locale)request.getSession(true).getAttribute(org.apache.struts.Globals.LOCALE_KEY);
//		rs = dbmgt.add(tableInfo.getTableName(), map, values, lg.getId(), path, defineInfo, resources, loc, saveType, lg, workFlow, deliverTo, props, mop);
//
//		String kid = values.get("id") == null ? "" : values.get("id").toString();
//		request.setAttribute("BillId", kid);
//		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS || rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
//
//			String billTypeName = tableInfo.getDisplay().get(loc.toString());
//			
//			String[] returnValue = (String[]) rs.retVal;
//			/*当前多语言种类*/
//			String locale = loc.toString();
//			String addMessage = GlobalsTool.getMessage(request, "common.lb.add");
//			/*添加系统日志*/
//			int operation = 0;
//			if ("saveDraft".equals(saveType))
//				operation = 5;
//			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
//				dbmgt.addLog(operation, values, null, tableInfo, locale.toString(), lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(), "", billTypeName);
//			}
//
//			String message = GlobalsTool.getMessage(request, "common.msg.addSuccess");
//			if (returnValue[1] != null && returnValue.length > 0) {
//				if (rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
//					message = dbmgt.getDefSQLMsg(request, returnValue[1]);
//				} else {
//					message += "<br>" + GlobalsTool.getMessage(request, "userfunction.msg.billNoExistNew") + ":" + returnValue[1];
//				}
//			}
//			HashMap retMap = new HashMap();
//			retMap.put("msg", message);
//			retMap.put("keyId", kid);
//			rs.retVal=retMap;
//			return rs;
//		} else {
//			/*失败清空单据编号*/
//			if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
//				// 自定义配置需要用户确认
//				ConfirmBean confirm = (ConfirmBean) rs.getRetVal();
//				String content = dbmgt.getDefSQLMsg(request, confirm.getMsgContent());
//				String jsConfirmYes = "";
//				String jsConfirmNo = "";
//				String saveAdd = request.getParameter("button");
//
//				jsConfirmYes = confirm.getFormerDefine() + ":" + confirm.getYesDefine() + ";";
//				if (confirm.getNoDefine().length() > 0) {
//					jsConfirmNo = confirm.getFormerDefine() + ":" + confirm.getNoDefine() + ";";
//				}
//				rs.retVal="请购单不应该配置确认提示";
//				return rs;
//			} else {
//				SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, request, saveType);
//				rs.retVal=saveErrrorObj.getMsg();
//				return rs;
//			}
//		}
	}
	
//	/**
//	 * 把BOM结构复制到MRPBom表中，下工令的依据是这棵BOM树
//	 * @param mrpId
//	 * @param bomId
//	 * @param conn
//	 */
//	private void mrpBomTree(HashMap goodsDetMap,String mrpId,String bomId,Double needQty,String pDetCode,Double pQty,Double pLossRate,Connection conn) throws Exception{
//		String sql = " select PDBOMDet.*,attrType from PDBOMDet join tblGoods on PDBOMDet.GoodsCode=tblGoods.classCode where f_ref=?  ";
//		PreparedStatement pstmt = conn.prepareStatement(sql);
//		pstmt.setString(1, bomId);
//		ResultSet rset = pstmt.executeQuery();
//		int DetCodeCount =10000; //用于生成BOM树级别编号，方便展示BOM树
//		while(rset.next()){
//			String isOpen = "0";
//			String GoodsCode =  rset.getString("GoodsCode");
//			goodsDetMap.put(GoodsCode, 0);
//			Double baseQty = rset.getDouble("qty");//分步单位数量
//			Double baselossRate = rset.getDouble("lossRate");//分步单位损耗率
//			Double qty = pQty*baseQty; //在BOM树中单位用量必须乘上级BOM的单位用量
//			Double lossRate = pLossRate+baselossRate;//在BOM树中单位损耗必须加上级BOM的单位损耗
//			//物料来源
//			int attrType = rset.getInt("attrType");
//			String childBomId = "";
//			//如果物料来源码是0自制，2S委外 3X虚拟，则看有没有下级BOM
//			if(attrType == 0 ||attrType == 2 ||attrType == 3){
//				sql=" select id from PDBom where GoodsCode=? and isLast =1 ";
//            	PreparedStatement pstmt1 = conn.prepareStatement(sql);
//            	pstmt1.setString(1, GoodsCode);
//                ResultSet rset1 = pstmt1.executeQuery();
//                if(rset1.next()){
//                	childBomId= rset1.getString("id");
//                }
//			}
//			String alone = "yes";//默认都是下达独立工令
//			if(!childBomId.equals("")){
//				//有子BOM，则肯定不是采购件和杂项，置isOpen
//				isOpen = "1";
//				//有子BOM且虚拟件是合并工令单
//				if(attrType == 3)
//					alone = "no";
//			}
//			String detCode = pDetCode+(DetCodeCount);
//			DetCodeCount++;
//        	sql = "INSERT INTO [PDMRPBom]([f_ref],[GoodsCode],[BomDetId],[isOpen],[BomId],[childBomId],[qty],[lossRate],"
//        			+ "[totalQty],[totalLoss],[attrType],[alone],detCode,clientSupper,baselossRate,baseQty)";
//    		sql += " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//            BaseEnv.log.debug(sql);
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, mrpId); 
//            pstmt.setString(2, GoodsCode);
//            pstmt.setString(3, rset.getString("id"));
//            pstmt.setString(4, isOpen+"");
//            pstmt.setString(5, bomId+"");
//            pstmt.setString(6, childBomId);
//            pstmt.setString(7, qty+"");
//            pstmt.setString(8, lossRate+"");
//            pstmt.setDouble(9, needQty * qty);
//            pstmt.setDouble(10, needQty * qty * lossRate/100);
//            pstmt.setString(11, attrType+"");
//            pstmt.setString(12, alone);
//            pstmt.setString(13, detCode);
//            pstmt.setString(14, rset.getString("canSuper"));
//            pstmt.setDouble(15, baselossRate);
//            pstmt.setDouble(16, baseQty);
//            pstmt.execute();
//            if(isOpen.equals("1")){
//            	//需展开下级BOM
//            	mrpBomTree(goodsDetMap,mrpId, childBomId, needQty, detCode, qty, lossRate, conn);
//            }
//        }
//	}
	
	/**
	 * 运算各种库存量，并存在PDMRPStocks中，作为历史记录，也方便读取
	 * 计算方法：
	 * 1、当前库存 （当前实际库存部分）
	 * 2、当前欠料（当前实际库存部分）
	 * 3、在途量 =当前未结案请购单未入库部分
	 * 4、在制量 =当前未结案工令单
	 * 5、占用量（销售订单/制造需求单占用量+已请购量-已发料量）（当前未结案未终止的订单，如果已结案其占用的料自动释放为公共资源）
	 * a、在订单终止和自动结案时要终止其物料占用
	 * b、下请购单和订单要回填物料需求表相应字段
	 * c、发料，退料，超领，补料要回填物料需求表相应字段
	 * @param bomId
	 * @param mrpId
	 * @param parentCode
	 * @return
	 */
	public Result getStocks(final String GoodsCode,final String computeId){
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                        	getStocks(GoodsCode, computeId, connection);
                        } catch (Exception ex) {
                            BaseEnv.log.error("MrpMgt.mrpStocks",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        return rst;
	}
	
	public void getStocks(final String GoodsCode,final String computeId,Connection conn ) throws Exception{
		
        //当前库存
        String sql = " insert into PDMRPStocks(f_ref,parentCode,GoodsCode,type,remark,qty,billType,billId,createTime)   "+
				" select ?,?,tblStocks.GoodsCode,'curStocks','当前库存',isnull(tblstocks.lastQty,0) qty,'tblStocks',tblStocks.id,convert(varchar(19),getdate(),120) from "+
				"  tblStocks  "+
				"  join tblStock on tblStocks.StockCode=tblStock.classCode  where GoodsCode =? and isnull(tblstocks.lastQty,0) >0 and StockType='Main'";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, computeId);
        pstmt.setString(2, "");
        pstmt.setString(3, GoodsCode);
        pstmt.execute();
        //成品油,哈弗成品油
        sql = " insert into PDMRPStocks(f_ref,parentCode,GoodsCode,type,remark,qty,billType,billId,createTime)   "+
				" select ?,?,tblStocks.GoodsCode,'curStocks','当前库存',isnull(tblstocks.lastQty,0) qty,'tblStocks',tblStocks.id,convert(varchar(19),getdate(),120) from "+
				"  tblStocks  "+
				"  join tblStock on tblStocks.StockCode=tblStock.classCode  where GoodsCode =? and isnull(tblstocks.lastQty,0) >0 and StockType !='Main' and GoodsCode like '00002%'  ";

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, computeId);
        pstmt.setString(2, "");
        pstmt.setString(3, GoodsCode);
        pstmt.execute();
        //当前欠料
        sql = " insert into PDMRPStocks(f_ref,parentCode,GoodsCode,type,remark,qty,billType,billId,createTime)   "+
				" select ?,?,tblStocks.GoodsCode,'oweStocks','当前欠料',isnull(tblstocks.OweQty,0) qty,'tblStocks',tblStocks.id,convert(varchar(19),getdate(),120) from "+
				"  tblStocks"+
				"  join tblStock on tblStocks.StockCode=tblStock.classCode  where GoodsCode =? and isnull(tblstocks.OweQty,0) > 0 and StockType !='Main' and  StockType !='Client' ";

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, computeId);
        pstmt.setString(2, "");
        pstmt.setString(3, GoodsCode);
        pstmt.execute();

        //查在途量 =当前未结案请购单（请购数量-入库数量）,请购单状态1已结案（缴库完成会自动结案），2已终止,非MRP请购，即备料请购
        sql = " insert into PDMRPStocks(f_ref,parentCode,GoodsCode,type,remark,qty,billType,billId,createTime)   "+
        		" select ?,?,tblBuyApplicationDet.GoodsCode,'buyApplication','采购在途', ISNULL( tblBuyApplicationDet.NotInQty,0) Qty,'tblBuyApplication',tblBuyApplication.id,convert(varchar(19),getdate(),120) "
				+ " from tblBuyApplication join tblBuyApplicationDet on tblBuyApplication.id=tblBuyApplicationDet.f_ref "+ 
				" where  tblBuyApplication.statusId not in (1,2) and tblBuyApplication.workflowNodeName='finish' "
				+ "  and ISNULL( tblBuyApplicationDet.NotInQty,0) >0 and "
				+ "tblBuyApplicationDet.GoodsCode=?  ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, computeId);
        pstmt.setString(2, "");
        pstmt.setString(3, GoodsCode);
        pstmt.execute();
        
        //查在制量 =当前未结案工令单（未缴库数量）
        sql = " insert into PDMRPStocks(f_ref,parentCode,GoodsCode,type,remark,qty,billType,billId,createTime)   "+
        		" select ?,?,PDWorkOrder.GoodsCode,'produce','在制量', ISNULL( PDWorkOrder.Qty,0) - ISNULL( PDWorkOrder.InPayQty,0) Qty,'PDWorkOrder',PDWorkOrder.id,convert(varchar(19),getdate(),120)   "
				+ " from PDWorkOrder "
				+ "where PDWorkOrder.workFlowNodeName='finish' and PDWorkOrder.statusId not in (1,2) and ISNULL( PDWorkOrder.Qty,0) - ISNULL( PDWorkOrder.InPayQty,0) >0 "
				+ "and PDWorkOrder.GoodsCode=? ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, computeId);
        pstmt.setString(2, "");
        pstmt.setString(3, GoodsCode);
        pstmt.execute();
        //订单占用量=当前所有已下工令订单数量（针对所有成品的占用量）
        sql = " insert into PDMRPStocks(f_ref,parentCode,GoodsCode,type,remark,qty,billType,billId,createTime)   "+
        		" select ?,?,tblSalesOrderDet.GoodsCode,'salesProcess','订单占用量', ISNULL( WorkOrderQty,0) +ISNULL( PossesQty,0) - ISNULL( OutQty,0)   Qty,'tblSalesOrder',tblSalesOrder.id,convert(varchar(19),getdate(),120)   "
				+ " from tblSalesOrderDet join tblSalesOrder on tblSalesOrderDet.f_ref=tblSalesOrder.id "
				+ "where tblSalesOrder.statusId not in (1,2) and tblSalesOrder.workFlowNodeName='finish' and ISNULL( WorkOrderQty,0) +ISNULL( PossesQty,0) - ISNULL( OutQty,0)  >0 "
				+ "and tblSalesOrderDet.GoodsCode=? ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, computeId);
        pstmt.setString(2, "");
        pstmt.setString(3, GoodsCode);
        pstmt.execute();
        
        //占用量（销售订单/制造需求单占用量+已请购量-已发料量-补料-超领+退料）（当前未结案未终止的订单，如果已结案其占用的料自动释放为公共资源）
        sql = " insert into PDMRPStocks(f_ref,parentCode,GoodsCode,type,remark,qty,billType,billId,createTime)   "+
        		" select ?,?,PDMRPReqDet.GoodsCode,'process','占用量', (ISNULL( PDMRPReqDet.PossesQty,0) + ISNULL( PDMRPReqDet.BuyAppQty,0) -ISNULL( PDMRPReqDet.OutQty,0) -ISNULL( PDMRPReqDet.ReFetchQty,0) -ISNULL( PDMRPReqDet.ExceedQty,0) +ISNULL( PDMRPReqDet.ReturnQty,0)  ) Qty,'PDMRP',PDMRP.id,convert(varchar(19),getdate(),120) "
				+ " from PDMRP join PDMRPReqDet on PDMRP.id=PDMRPReqDet.f_ref "+ 
				" where  PDMRP.statusId not in (1)  and isnull(PDMRPReqDet.clientSupper,'') !='isClient' "
				+ " and  (ISNULL( PDMRPReqDet.PossesQty,0) + ISNULL( PDMRPReqDet.BuyAppQty,0) -ISNULL( PDMRPReqDet.OutQty,0) -ISNULL( PDMRPReqDet.ReFetchQty,0) -ISNULL( PDMRPReqDet.ExceedQty,0) +ISNULL( PDMRPReqDet.ReturnQty,0)  )  != 0 "
				+ " and PDMRPReqDet.GoodsCode=?  ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, computeId);
        pstmt.setString(2, "");
        pstmt.setString(3, GoodsCode);
        pstmt.execute();
	}
	
	private void saveMRPReqDet(String mrpId,String bomId,String GoodsCode,Double qty,Double possesQty,double lossQty,String Batch,
			String Inch,String Hue,String yearNO,String color,String ProDate,String Availably,String clientSupper,Connection conn) throws Exception{
		//查对象是否存在
		String sql = " select id from PDMRPReqDet where f_ref=? and GoodsCode=? and isnull([BatchNo],'')=? and "
				+ "isnull([Inch],'')=? and isnull([Hue],'')=? and isnull([yearNO],'')=? and isnull([color],'')=? and "
				+ "isnull([ProDate],'')=? and isnull([Availably],'')=? and clientSupper=?  ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, mrpId);  
        pstmt.setString(2, GoodsCode);
        pstmt.setString(3, Batch);  
        pstmt.setString(4, Inch);  
        pstmt.setString(5, Hue);  
        pstmt.setString(6, yearNO);  
        pstmt.setString(7, color);  
        pstmt.setString(8, ProDate);  
        pstmt.setString(9, Availably);  
        pstmt.setString(10, clientSupper); 
        ResultSet rset = pstmt.executeQuery();
        if(rset.next()){
        	String id = rset.getString("id");
        	sql = "update [PDMRPReqDet] set Qty=Qty+?,PossesQty=PossesQty+?,lackQty=lackQty+?,LossQty=LossQty+? where id=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, qty);
			pstmt.setDouble(2, possesQty);
			pstmt.setDouble(3, qty-possesQty);
			pstmt.setDouble(4, lossQty);
			pstmt.setString(5, id);  
			pstmt.execute();
	    }else{
	        
			sql = "INSERT INTO [PDMRPReqDet]([f_ref],[bomId],[GoodsCode],[Qty],[PossesQty],lackQty,[LossQty],[BuyAppQty],[OrderQty],[InQty],"
	        		+ "[OutQty],[ReFetchQty],[ExceedQty],[ReturnQty],[BatchNo],[Inch],[Hue],[yearNO],[color],[ProDate],[Availably],clientSupper)"
	        		+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, mrpId);  
	        pstmt.setString(2, bomId);
	        pstmt.setString(3, GoodsCode);
	        pstmt.setDouble(4, qty);
	        pstmt.setDouble(5, possesQty);  
	        pstmt.setDouble(6, qty-possesQty);  
	        pstmt.setDouble(7, lossQty);
	        pstmt.setString(8, "0");  
	        pstmt.setString(9, "0");  
	        pstmt.setString(10, "0");  
	        pstmt.setString(11, "0");  
	        pstmt.setString(12, "0");  
	        pstmt.setString(13, "0");  
	        pstmt.setString(14, "0");  
	        pstmt.setString(15, Batch);  
	        pstmt.setString(16, Inch);  
	        pstmt.setString(17, Hue);  
	        pstmt.setString(18, yearNO);  
	        pstmt.setString(19, color);  
	        pstmt.setString(20, ProDate);  
	        pstmt.setString(21, Availably);  
	        pstmt.setString(22, clientSupper); 
	        pstmt.execute();
	    }
	}
	
	public void recMrpReqDet(String mrpId,Double needQty,Double pLossRate,String detCode,HashMap goodsDetMap,Connection conn) throws Exception{
		String sql  = " select * from PDMRPBom where f_ref=? and detCode like ? and clientSupper !='isSupper' "; //非包料
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, mrpId);
        pstmt.setString(2, detCode+"_____");
        ResultSet rset = pstmt.executeQuery();
        while(rset.next()){
        	Double baseQty = rset.getDouble("baseQty");
        	Double baselossRate = rset.getDouble("baselossRate");
        	int isOpen = rset.getInt("isOpen"); //是否展开
        	String dtype = rset.getString("dtype");//是否独立工令
        	String bomId = rset.getString("bomId");
        	String GoodsCode = rset.getString("GoodsCode");
        	Double qty = baseQty*needQty;
        	double lossQty = baseQty*needQty*(baselossRate+pLossRate)/100;
        	String Batch = rset.getString("BatchNo");
        	String Inch = rset.getString("Inch");
        	String Hue = rset.getString("Hue");
        	String yearNO = rset.getString("yearNO");
        	String color = rset.getString("color");
        	String ProDate = rset.getString("ProDate");
        	String Availably = rset.getString("Availably");
        	Batch= Batch==null?"":Batch;
        	Inch= Inch==null?"":Inch;
        	Hue= Hue==null?"":Hue;
        	yearNO= yearNO==null?"":yearNO;
        	color= color==null?"":color;
        	ProDate= ProDate==null?"":ProDate;
        	Availably= Availably==null?"":Availably;
        	String clientSupper = rset.getString("clientSupper");
        	Double ProduceQty = rset.getDouble("ProduceQty");//独立工令时的子工令生产数量
        	String cDetCode = rset.getString("DetCode");
        	String mrpBomId=rset.getString("id");
        	
	        if(isOpen == 0){ //未展开
	        	//计算是否有占用量
	        	double goodsStock = goodsDetMap.get(GoodsCode)==null?0:Double.parseDouble(goodsDetMap.get(GoodsCode)+"") ;
	        	double prossesQty = 0;
	        	if(goodsStock>qty){
	        		prossesQty = qty;
	        		goodsDetMap.put(GoodsCode,goodsStock-qty);
	        	}else if(goodsStock>0){
	        		prossesQty = goodsStock;
	        		goodsDetMap.put(GoodsCode,0);
	        	}
	            this.saveMRPReqDet(mrpId, bomId, GoodsCode, qty,prossesQty, lossQty, Batch, Inch, Hue, yearNO, color, ProDate, Availably, clientSupper, conn);
	        }else if(dtype.equals("3")||dtype.equals("5")){//独立工令
	        	//查半成品是否有可用库存-----------------------------------------
	        	double goodsStock = goodsDetMap.get(GoodsCode)==null?0:Double.parseDouble(goodsDetMap.get(GoodsCode)+"") ;
	        	if(goodsStock>ProduceQty){
	        		//有可用库存，且并超过子工令的计划数量，不需生产 先占用半成品可用库存
	            	this.saveMRPReqDet(mrpId, bomId, GoodsCode, ProduceQty,ProduceQty, 0, Batch, Inch, Hue, yearNO, color, ProDate, Availably, clientSupper, conn);
	            	
	        		goodsDetMap.put(GoodsCode,goodsStock-ProduceQty);
	        		ProduceQty = 0d;
	        	}else {
	        		if(goodsStock>0){ //有部分可用库存，先占用，其它不足部分再安排生产
	            		this.saveMRPReqDet(mrpId, bomId, GoodsCode, goodsStock,goodsStock, 0, Batch, Inch, Hue, yearNO, color, ProDate, Availably, clientSupper, conn);
	            		//回填子生令需生产数量
	            	}
	            	ProduceQty = ProduceQty-goodsStock;//可用库存不足部分安排子工令生产
	            	recMrpReqDet(mrpId,ProduceQty,0d,cDetCode,goodsDetMap,conn);//以指定的生产数来计算物料
	        		goodsDetMap.put(GoodsCode,0);
	        	}
	            //回填子工令需生产数量
	            sql = "update PDMRPBom set needWQty=? where id=?";
	            PreparedStatement pstmt1 = conn.prepareStatement(sql);
	    		pstmt1.setDouble(1, ProduceQty);
	    		pstmt1.setString(2, mrpBomId);
	    		pstmt1.execute();
	        }else{//合并工令
	        	recMrpReqDet(mrpId,qty,baselossRate+pLossRate,cDetCode,goodsDetMap,conn);//以上级的生产数来计算物料
	        }
        }
	}
	
	/**
	 * 计算物料需求
	 * 1、先查所有所计算的物料规划表bom，找出所有原料，包括半成品。
	 * 2、计算出所有可用库存
	 * 3、分配处理
	 * @param list
	 * @param lb
	 * @return
	 */
	public Result computeMRP(final ArrayList list,final LoginBean lb){
		final String computeId = IDGenerater.getId();//本次计算的代号
		final Result rst = new Result();
		if(list.size()==0){
			rst.retCode = ErrorCanst.DEFAULT_FAILURE;
			rst.retVal="没有需计算的记录";
			return rst;
		}
		final HashMap goodsDetMap = new HashMap(); //用于存储所有原料商品明细和其可用库存
		String mrpIds = "";
		for(Object o :list){
			mrpIds += ",'"+o+"'";
		}
		mrpIds = mrpIds.substring(1);
		String sql  = " select GoodsCode from PDMRPBom where f_ref in ("+mrpIds+") group by GoodsCode ";
		BaseEnv.log.debug("开始读取所有需计算物料需求的商品 "+sql);
		Result rs = this.sqlListMap(sql, new ArrayList());
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			return rs;
		}
		long curTime = System.currentTimeMillis();
		BaseEnv.log.debug("开始计算物料需求 ，时间："+curTime);
		//逐个商品计算其可以用库存
		for(HashMap map :(ArrayList<HashMap>)(rs.retVal)){
			String GoodsCode = (String)map.get("GoodsCode");
			BaseEnv.log.debug("计算物料需求-商品："+GoodsCode+":"+System.currentTimeMillis());
			Result r1= getStocks(GoodsCode,computeId);
			if(r1.retCode != ErrorCanst.DEFAULT_SUCCESS){
				BaseEnv.log.error("计算物料需求报错， 商品"+GoodsCode+" ；；"+r1.retVal);
    			return r1;
    		}
    		Double validQty = 0d;
    		//查可用库存
			sql = " select SUM((case  when type='process' or type='salesProcess'  then -1 else 1 end )*Qty) lastQty from PDMRPStocks a where f_ref=? and GoodsCode=? "
					+ " ";
			ArrayList param = new ArrayList();
			param.add(computeId);
			param.add(GoodsCode);
			Result tRs = sqlListMap(sql,param);
			if(tRs.retCode != ErrorCanst.DEFAULT_SUCCESS){
				BaseEnv.log.error("读取物料需求报错 商品"+GoodsCode+" ；；"+r1.retVal);
				return tRs;
			}else{
				ArrayList<HashMap> stockQty = (ArrayList<HashMap>)tRs.retVal;
				if(stockQty.size() >0){
					validQty =Double.parseDouble(stockQty.get(0).get("lastQty")+"");
					goodsDetMap.put(GoodsCode, validQty);
				}
			}
		}
		BaseEnv.log.debug("结束计算物料需求 ，耗时："+(System.currentTimeMillis()-curTime));
		int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                            SQLException {
                        try {
                        	//记录录本次运算
                        	String sql  = "insert into PDMRPCompute(id,BillDate,createBy,createTime) values(?,?,?,?)";
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            
                            pstmt.setString(1, computeId);
                            pstmt.setString(2, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
                            pstmt.setString(3, lb.getId());
                            pstmt.setString(4, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
                            pstmt.execute();
                        	//循环处理每个需求规划单的物料表
                        	for(Object o :list){
	                        	String mrpId = o+""; //取要计算的mrpId
	                        	Double needQty = 0d;
	                            sql  = " select * from PDMRP where id=? ";
	                            pstmt = conn.prepareStatement(sql);
	                            pstmt.setString(1, mrpId);
	                            ResultSet rset = pstmt.executeQuery();
	                            if(rset.next()){
	                            	//根据生产规划单，准备料件存储表PDMRPReqDet
	                                needQty = rset.getDouble("needQty");
	                                //修改MRP运算的计算标识
	                                sql = " update PDMRP set ComputerId=? where id=? ";
	                                PreparedStatement pstmt1 = conn.prepareStatement(sql);
	                                pstmt1.setString(1, computeId);
	                                pstmt1.setString(2, mrpId);
	                                pstmt1.execute();
	                                
	                                recMrpReqDet(mrpId, needQty, 0d, "", goodsDetMap, conn);
	                            }
	                        }
                        	
                        	//处理客供料
                        	sql  = " select PDMRP.id mrpId,PDMRP.CompanyCode, PDMRPReqDet.GoodsCode, PDMRPReqDet.id,PDMRPReqDet.Qty "
                        			+ " from PDMRPReqDet join PDMRP on PDMRPReqDet.f_ref=PDMRP.id "
            	    				+ " join PDMRPCompute on PDMRP.ComputerId=PDMRPCompute.id "
            	    				+ " where PDMRPCompute.id=? and isnull(PDMRPReqDet.clientSupper,'') = 'isClient'  "
            	    				+ " order by PDMRP.BillDate ";
            	    		PreparedStatement pstmt2 = conn.prepareStatement(sql);
                        	pstmt2.setString(1, computeId);
                    		ResultSet rset2 = pstmt2.executeQuery();
                    		while(rset2.next()){
                    			//如果有客供料，找出其第个单据的客户
                    			String GoodsCode=rset2.getString("GoodsCode");
                    			String id=rset2.getString("id");
                    			Double Qty=rset2.getDouble("Qty");
                    			String CompanyCode=rset2.getString("CompanyCode");
                    			
                    			Double aQty = 0d;
                    			//查该客户，该仓库有没有库存
                    			sql = " select sum(lastQty) lastQty from tblStocks join tblStock on tblStocks.StockCode=tblStock.classCode and StockType='Client' "
                    					+ " where tblStock.CompanyCode=? and tblStocks.GoodsCode=?  ";
                    			PreparedStatement pstmt1 = conn.prepareStatement(sql);
                        		pstmt1.setString(1, CompanyCode);
                        		pstmt1.setString(2, GoodsCode);
                        		ResultSet rset1 = pstmt1.executeQuery();
                        		if(rset1.next()){
                        			aQty = rset1.getDouble("lastQty");
                        		}
                        		//如果存在客供库存，检查被占用量
                        		if(aQty > 0 ){
                        			sql  = " select sum(ISNULL( PDMRPReqDet.PossesQty,0) + ISNULL( PDMRPReqDet.InQty,0) -ISNULL( PDMRPReqDet.OutQty,0) -ISNULL( PDMRPReqDet.ReFetchQty,0) -ISNULL( PDMRPReqDet.ExceedQty,0) +ISNULL( PDMRPReqDet.ReturnQty,0)  ) lastQty "
                        					+ " from PDMRP join PDMRPReqDet on PDMRP.id=PDMRPReqDet.f_ref " 
                        					+ " where  PDMRP.statusId not in (1)  and isnull(PDMRPReqDet.clientSupper,'') = 'isClient' "
                        					+ " and PDMRP.CompanyCode=?  and PDMRPReqDet.GoodsCode=? " ;
                        			pstmt1 = conn.prepareStatement(sql);
                            		pstmt1.setString(1, CompanyCode);
                            		pstmt1.setString(2, GoodsCode);
                            		rset1 = pstmt1.executeQuery();
                            		if(rset1.next()){
                            			aQty =aQty - rset1.getDouble("lastQty");
                            		}
                        		}
                        		if(aQty > 0 ){
                        			if(aQty > Qty){
                        				aQty=Qty;
                        			}
                        			sql = " update PDMRPReqDet set PossesQty=?,lackQty=lackQty -? where id=? ";
                            		pstmt1 = conn.prepareStatement(sql);
                            		pstmt1.setDouble(1, aQty);
                            		pstmt1.setDouble(2, aQty);
                            		pstmt1.setString(3, id);
                            		pstmt1.execute();
                        		}
                    			
                    		}
                    		
                    		//处理发料取整
                    		sql  = " update PDMRPReqDet set qty=CEILING(qty),lackQty=CEILING(lackQty)  "
                					+ " from PDMRP join PDMRPReqDet on PDMRP.id=PDMRPReqDet.f_ref "
                					+ " join tblGoods on PDMRPReqDet.GoodsCode=tblGoods.classCode and tblGoods.sendRound=1 " 
                					+ " where  PDMRP.ComputerId=?   " ;
                    		PreparedStatement  pstmt1 = conn.prepareStatement(sql);
                    		pstmt1.setString(1, computeId);
                    		pstmt1.execute();
                    		
                        } catch (Exception ex) {
                            BaseEnv.log.error("MrpMgt.computeMRP",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
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
	 * 把BOM结构复制到MRPBom表中，下工令的依据是这棵BOM树
	 * @param mrpId
	 * @param bomId
	 * @param conn
	 */
	private void mrpBomTree(ArrayList bomList,String bomId,Double needQty,String pDetCode,Double pQty,Double pLossRate,Connection conn) throws Exception{
		String sql = " select PDBOMDet.*,attrType from PDBOMDet join tblGoods on PDBOMDet.GoodsCode=tblGoods.classCode where f_ref=?  ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, bomId);
		ResultSet rset = pstmt.executeQuery();
		int i=0;
		while(rset.next()){
			String newCode = pDetCode;
			for(int k=0;k<5 - (""+(i+1)).length();k++){
				newCode +='0';
			}
			newCode =newCode+(i+1);
			i++;
			
			int attrType = rset.getInt("attrType");
			String GoodsCode = rset.getString("GoodsCode");
			Double qty = rset.getDouble("qty")*pQty;
			Double lossRate=rset.getDouble("lossRate")+pLossRate;
			Double totalQty = qty*needQty;
			Double totalLoss = totalQty*lossRate/100;
			
			HashMap so = new HashMap();
			so.put("bomId", bomId);
			so.put("bomDetId", rset.getString("id"));
			so.put("detCode", newCode);
			so.put("qty", qty);
			so.put("lossRate", lossRate);
			so.put("totalQty", totalQty);
			so.put("totalLoss", totalLoss);
			so.put("ProduceQty", totalQty);
			so.put("attrType", attrType);
			so.put("dtype", "3");//自制不合并
			so.put("clientSupper", rset.getString("canSuper"));
			so.put("GoodsCode", GoodsCode);
			so.put("baseLossRate", rset.getDouble("LossRate"));
			so.put("baseQty", rset.getDouble("Qty"));
			
			String isOpen="0";
			so.put("isOpen", isOpen);
			so.put("childBomId", "");
			bomList.add(so);
			if(attrType == 0 || attrType ==2 || attrType==3){
				sql=" select id from PDBom where GoodsCode=? and isLast=1 ";
				PreparedStatement pstmt1 = conn.prepareStatement(sql);
				pstmt1.setString(1, GoodsCode);
				ResultSet rset1 =pstmt1.executeQuery();
    			if(rset1.next()){
    				so.put("childBomId", rset1.getString("id"));
    				isOpen="1";
    				so.put("isOpen", isOpen);
    				mrpBomTree(bomList, rset1.getString("id"), needQty, newCode, qty, lossRate, conn);
    			}
			}
       }
	}
	
	
	/**
	 * 计算物料需求,从制造需求单运算，先生成规划单，再运算
	 * 1、先查所有所计算的物料规划表bom，找出所有原料，包括半成品。
	 * 2、计算出所有可用库存
	 * 3、分配处理
	 * @param list
	 * @param lb
	 * @return
	 */
	public Result computeMRPPR(final ArrayList<String> list,final LoginBean lb){
		final Result rst = new Result();
		final ArrayList mrpList = new ArrayList();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                            SQLException {
                        try {
                    		//先生成规划单
                    		for(String id :list){
                    			HashMap values = new HashMap();
                    			String sql = " select tblGoods.GoodsNumber,tblGoods.GoodsFullName,GoodsCode,startDate,SendDate, "
                    					+ " PDProduceRequire.id billId,BillNo,BillDate,PDProduceRequire.DepartmentCode,"
                    					+ "PDProduceRequire.EmployeeId,'' CompanyCode,PDProduceRequire.createBy,"
                    					+ " Qty,MRPQty from PDProduceRequireDet "
                    					+ "join PDProduceRequire on PDProduceRequireDet.f_ref=PDProduceRequire.id "
                    					+ "join tblGoods on PDProduceRequireDet.GoodsCode=tblGoods.classCode  where PDProduceRequireDet.id=? ";
                    			PreparedStatement pstmt = conn.prepareStatement(sql);
                    			pstmt.setString(1, id);
                    			ResultSet rset =pstmt.executeQuery();
                    			if(rset.next()){
                    				String mrpId=IDGenerater.getId();
                    				mrpList.add(mrpId);
                    				String GoodsCode= rset.getString("GoodsCode");
                    				String GoodsNumber= rset.getString("GoodsNumber");
                    				String GoodsFullName= rset.getString("GoodsFullName");
                    				values.put("GoodsCode", GoodsCode);
                    				values.put("needQty", rset.getDouble("Qty")-rset.getDouble("MRPQty"));
                    				values.put("StockQty", 0d);
                    				values.put("OrderQty", 0d);
                    				values.put("ProduceQty", 0d);
                    				values.put("PossesQty", 0d);
                    				values.put("id", mrpId);
                    				values.put("MRPType", "3");
                    				values.put("startWork",rset.getString("startDate"));
                    				values.put("endWork", rset.getString("SendDate"));
                    				
                    				HashMap so = new HashMap();
                        			so.put("billId",rset.getString("billId"));
                        			so.put("BillType","PDProduceRequire");
                        			so.put("BillNo",rset.getString("BillNo"));
                        			so.put("BillDate",rset.getString("BillDate"));
                        			so.put("DepartmentCode",rset.getString("DepartmentCode"));
                        			so.put("EmployeeId",rset.getString("EmployeeId"));
                        			so.put("CompanyCode",rset.getString("CompanyCode"));
                        			so.put("createBy",rset.getString("createBy"));
                        			so.put("TotalQty",rset.getDouble("Qty"));
                        			so.put("MRPQty",rset.getDouble("MRPQty"));
                        			so.put("qty",rset.getDouble("Qty")-rset.getDouble("MRPQty"));
                        			so.put("Urgency","1");
                        			ArrayList salesList = new ArrayList();
                        			salesList.add(so);
                        			values.put("salesList", salesList);
                    				
                    				//查BOM
                    				sql=" select id from PDBom where GoodsCode=? and isLast=1 ";
                    				pstmt = conn.prepareStatement(sql);
                        			pstmt.setString(1, GoodsCode);
                    				rset =pstmt.executeQuery();
                        			if(rset.next()){
                        				values.put("bomId", rset.getString(1));
                        			}else{
                        				rst.retCode = ErrorCanst.DEFAULT_FAILURE;
                        				rst.retVal = "商品("+GoodsNumber+"  "+GoodsFullName+")当前没有可用BOM";
                    					return;
                    				}
                        			//查BOM树
                    				ArrayList bomList = new ArrayList();
                    				values.put("bomList", bomList);
                    				mrpBomTree(bomList, values.get("bomId")+"", (Double)so.get("qty"), "", 1d, 0d, conn);
                    				saveBom(values, lb, conn);
                    			}
                    			
                    		}

                        } catch (Exception ex) {
                            BaseEnv.log.error("MrpMgt.saveBom",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        if(retCode != ErrorCanst.DEFAULT_SUCCESS){
        	return rst;
        }
        return computeMRP(mrpList, lb);
	}
	
	/**
	 * 删除计算物料需求,从制造需求单运算，先生成规划单，再运算
	 * 1、先查所有所计算的物料规划表bom，找出所有原料，包括半成品。
	 * 2、计算出所有可用库存
	 * 3、分配处理
	 * @param list
	 * @param lb
	 * @return
	 */
	public Result deleteComputeMRPPR(final ArrayList<String> list,final LoginBean lb){
		final Result rst = new Result();
		final ArrayList mrpList = new ArrayList();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                            SQLException {
                        try {
                    		//先生成规划单
                    		for(String id :list){
                    			
                    			String sql =  "select isnull(PDMRP.WorkOrderQty,0)  WorkOrderQty "
                    					+ " from PDMRP where id= '"+id+"'";
                    			PreparedStatement stmt = conn.prepareStatement(sql);
                    			ResultSet rset = stmt.executeQuery();
                    			if(rset.next()){
                    				int workOrder = rset.getInt(1);
                    				if(workOrder>0){
                    					rst.setRetCode(-1000);
                    					rst.setRetVal("已下工令，不可以再重算");
                    					return;
                    				}
                    			}
                    			sql = " update PDProduceRequireDet set MRPQty = MRPQty - b.Qty from PDProduceRequireDet a "
                    					+ "join PDMRPAllot b on a.id=b.BillDetId and a.f_ref=b.BillId and b.BillType='PDProduceRequire' "
                    					+ "where b.f_ref='"+id+"'  ";
                    			stmt = conn.prepareStatement(sql);
                    			stmt.executeUpdate();
                    			sql = "delete from PDMRPAllot where f_ref ='"+id+"'";
                    			stmt = conn.prepareStatement(sql);
                    			stmt.executeUpdate();
                    			sql = "delete from PDMRPBom where f_ref ='"+id+"'";
                    			stmt = conn.prepareStatement(sql);
                    			stmt.executeUpdate();
                    			sql = "delete from PDMRPReqDet where f_ref ='"+id+"'";
                    			stmt = conn.prepareStatement(sql);
                    			stmt.executeUpdate();
                    			sql = "delete from PDMRP where id ='"+id+"'";
                    			stmt = conn.prepareStatement(sql);
                    			stmt.executeUpdate();
                    			sql = "delete from PDMRPBill where f_ref ='"+id+"'";
                    			stmt = conn.prepareStatement(sql);
                    			stmt.executeUpdate();
                    			sql = " update PDProduceRequireDet set MRPQty = MRPQty - b.Qty from PDProduceRequireDet a "
                    					+ "join PDMRPAllot b on a.id=b.BillDetId and a.f_ref=b.BillId and b.BillType='PDProduceRequire' "
                    					+ "where b.f_ref='"+id+"'  ";
                    			
                    		}

                        } catch (Exception ex) {
                            BaseEnv.log.error("MrpMgt.deleteComputeMRPPR",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
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
	 * 显示所有生产规划好的商品，用于计算物料需求
	 * @param BillDate1
	 * @param BillDate2
	 * @param BillType
	 * @param BillNo
	 * @param ComFullNamen
	 * @param EmpFullName
	 * @param DeptFullName
	 * @return
	 */

	public Result showMrp(){
		String sql=""; 
				
		sql =  "select tblGoods.GoodsNumber,tblGoods.GoodsFullName,tblGoods.GoodsSpec,tblGoods.BaseUnit,tblEmployee.EmpFullName, a.*,"
				+ "(select sum(PDMRPReqDet.lackQty)  from PDMRPReqDet where f_ref=a.id and isnull(PDMRPReqDet.clientSupper,'') !='isClient' ) lackQty  from PDMRP a "
				+ "join tblGoods  on a.GoodsCode=tblGoods.classCode "
				+ "join tblEmployee on a.createBy=tblEmployee.id " ;
		sql +=" where a.statusId=0 and a.MRPType not in (1,2)  order by a.startWork ";
		ArrayList param = new ArrayList();
		BaseEnv.log.debug("MrpMgt查当前完计算生产规划"+sql);
		
		return sqlListMap(sql, param);
	}
	/**
	 * 显示所有制造需求单，用于计算物料需求
	 * @return
	 */
	public Result showMRPPR(String BillNo,String GoodsNumber){
		String sql=""; 
				
		sql =  "select b.id,a.id keyId,PDMRP.id mrpId, tblGoods.GoodsNumber,tblGoods.GoodsFullName,tblGoods.GoodsSpec,tblGoods.BaseUnit,tblEmployee.EmpFullName, a.BillDate,a.BillNo,b.Qty,b.MRPQty,"
				+ "(select sum(PDMRPReqDet.lackQty)  from PDMRPReqDet  "
				+ "where PDMRPReqDet.f_ref=PDMRP.id and isnull(PDMRPReqDet.clientSupper,'') !='isClient' ) lackQty,isnull(PDMRP.WorkOrderQty,0)  WorkOrderQty "
				+ " from PDProduceRequire a join PDProduceRequireDet b on a.id=b.f_ref "
				+ "join tblGoods  on b.GoodsCode=tblGoods.classCode "
				+ "join tblEmployee on a.createBy=tblEmployee.id "
				+ " left join ( select PDMRP.id,PDMRP.GoodsCode,PDMRPBill.BillId,PDMRP.WorkOrderQty from PDMRPBill "
				+ "join PDMRP on PDMRPBill.f_ref=pdmrp.id ) PDMRP on b.GoodsCode=PDMRP.GoodsCode and a.id=PDMRP.BillId  " ;
		sql +=" where a.statusId=0 and a.workflowNodeName='finish'  ";
		if(BillNo != null && BillNo.length() > 0){
			sql +=" and a.BillNo='"+BillNo+"' ";
		}
		if(GoodsNumber != null && GoodsNumber.length() > 0){
			sql +=" and tblGoods.GoodsNumber='"+GoodsNumber+"' ";
		}
		sql +="order by a.BillDate ";
		ArrayList param = new ArrayList();
		BaseEnv.log.debug("MrpMgt查当前完计算生产规划"+sql);
		
		return sqlListMap(sql, param);
	}
	
	public Result showBuyApplication(String mrpId){
		//检查是滞有未审核的请购单
		String sql="";
		sql = "select count(0) from tblBuyApplication where workflowNodeName not in ('finish','print') and isMRP='yes' ";
		ArrayList param = new ArrayList();
//		Result rs = sqlList(sql,param);
//		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
//			return rs;
//		}
//		ArrayList<Object[]> oa = (ArrayList<Object[]>)rs.retVal;
//		if(oa.size() > 0 ){
//			int count = Integer.parseInt(oa.get(0)[0]+"");
//			if(count > 0){
//				rs.retCode=ErrorCanst.DEFAULT_FAILURE;
//				rs.retVal = "当前有未审核请购单，请先审核";
//				return rs;
//			}
//		}
		
		String GoodsFields = "";
		for (String[] shows : BaseEnv.reportShowSet) { 
			if ("tblGoods".equals(shows[0]) && "1".equals(shows[2])) {
				GoodsFields +=",tblGoods."+shows[1];
			}
		}

		sql =  "select PDMRPReqDet.GoodsCode"+GoodsFields+",tblGoods.reTimes,sum(PDMRPReqDet.Qty) Qty,sum(PDMRPReqDet.LossQty) LossQty,sum(PDMRPReqDet.PossesQty) PossesQty,"
				+ " sum(PDMRPReqDet.BuyAppQty) BuyAppQty,sum(PDMRPReqDet.lackQty) lackQty "
				+ " from PDMRPReqDet join PDMRP on PDMRPReqDet.f_ref=PDMRP.id "
				+ " join tblGoods on PDMRPReqDet.GoodsCode=tblGoods.classCode "
				+ " where PDMRP.statusId not in (1)  and isnull(PDMRPReqDet.clientSupper,'') !='isClient' ";
		param = new ArrayList();
		if(mrpId != null && !mrpId.equals("")){
			sql += " and PDMRP.id=? ";
			param.add(mrpId);
		}else{
			sql +=" and PDMRPReqDet.lackQty > 0 ";
		}
		sql +="group by PDMRPReqDet.GoodsCode "+GoodsFields+",tblGoods.reTimes";
		BaseEnv.log.debug("MrpMgt查当前所有待请购的缺料表"+sql);
		
		return sqlListMap(sql, param);
	}
	
	/**
	 * 生产规划
	 * 第一步：选择销售订单和制造需求单 
	 * @param BillDate1
	 * @param BillDate2
	 * @param BillType
	 * @param BillNo
	 * @param ComFullNamen
	 * @param EmpFullName
	 * @param DeptFullName
	 * @return
	 */

	public Result selSalesOrder(String BillDate1,String BillDate2 ,String BillType , String BillNo,String CompanyCode,String GoodsCode,
			ArrayList<String[]> GoodsFList,ArrayList<String[]> ComFList,String orderBy){
		String GoodsF = "";
		String ComF="";
		for(String[] ss :GoodsFList){
			GoodsF +=",tblGoods."+ss[0];
		}
		for(String[] ss :ComFList){
			ComF +=",tblCompany."+ss[0];
		}
		String sql="";
		if("true".equals(GlobalsTool.getSysSetting("productByOrder"))){
			sql=	"   select  tblSalesOrder.id,'tblSalesOrder' BillType,BillNo,BillDate,tblSalesOrder.CompanyCode,GoodsCode,Urgency,max(tblSalesOrderDet.id) SourceId,sum(tblSalesOrderDet.Qty) Qty,sum(isnull(tblSalesOrderDet.MRPQty,'0')) MRPQty "
					+ " from tblSalesOrder join tblSalesOrderDet on tblSalesOrder.id=tblSalesOrderDet.f_ref "
					+ " where tblSalesOrder.workFlowNodeName='finish' and tblSalesOrderDet.Qty- isnull(tblSalesOrderDet.MRPQty,'0')>0 and tblSalesOrder.statusId=0  "
					+ " group by tblSalesOrder.id,BillNo,BillDate,tblSalesOrder.CompanyCode,GoodsCode,Urgency"
					+ " union ";
		} 
		sql +=  " select  PDProduceRequire.id,'PDProduceRequire' BillType,BillNo,BillDate,'' CompanyCode,GoodsCode,1,max(convert(varchar(50), PDProduceRequireDet.id)) SourceId,sum(PDProduceRequireDet.Qty) Qty,sum(isnull(PDProduceRequireDet.MRPQty,'0')) MRPQty  "
				+ " from PDProduceRequire join PDProduceRequireDet on PDProduceRequire.id=PDProduceRequireDet.f_ref "
				+ " where PDProduceRequire.workFlowNodeName='finish' and PDProduceRequireDet.Qty- isnull(PDProduceRequireDet.MRPQty,'0')>0  and PDProduceRequire.statusId=0   "
				+ " group by PDProduceRequire.id,BillNo,BillDate,GoodsCode";
		
		sql =  "select a.id,BillType,BillNo,BillDate,a.SourceId,Qty,MRPQty,Qty-MRPQty NeedQty,GoodsCode,Urgency,a.CompanyCode "+GoodsF+ComF
				+ " from ("+sql+") a "
				+ " join tblGoods on a.GoodsCode = tblGoods.classCode "
				+ " left join tblCompany on a.CompanyCode = tblCompany.classCode ";
		sql +=" where 1=1 ";
		ArrayList param = new ArrayList();
		if(BillDate1!=null && BillDate1.length() > 0){
			sql +=" and a.BillDate >=? ";
			param.add(BillDate1);
		}
		if(BillDate2!=null && BillDate2.length() > 0){
			sql +=" and a.BillDate <=? ";
			param.add(BillDate2);
		}
		if(BillType!=null && BillType.length() > 0){
			sql +=" and a.BillType =? ";
			param.add(BillType);
		}
		if(BillNo!=null && BillNo.length() > 0){
			sql +=" and a.BillNo like '%'+?+'%' ";
			param.add(BillNo);
		}
		if(CompanyCode!=null && CompanyCode.length() > 0){
			sql +=" and tblCompany.classCode = ? ";
			param.add(CompanyCode.split("#;#")[0]);
		}
		if(GoodsCode!=null && GoodsCode.length() > 0){
			sql +=" and tblGoods.classCode =? ";
			param.add(GoodsCode.split("#;#")[0]);
		}
		
		sql += " order by "+orderBy;
		BaseEnv.log.debug("MrpMgt查当前订单"+sql);
		
		return sqlListMap(sql, param);
	}
	
	
	/**
	 * 第二步：选择商品
	 * @param keys
	 * @return
	 */
	public Result selGoods(HashMap<String,HashMap> goodsMaps){
		for(String GoodsCode:goodsMaps.keySet()){
			HashMap goodsItem = goodsMaps.get(GoodsCode);
			
			String sql=" select id,version,isLast from PDBom where GoodsCode=? and "
					+ " (case when (len(inValidate)=10 and inValidate<=CONVERT(varchar(10),getdate(),120)) or "
					+ "  (len(validate)=10 and validate>CONVERT(varchar(10),getdate(),120)) then 0 else 1 end) =1  order by isLast desc, version desc ";
			ArrayList param = new ArrayList();
			param.add(GoodsCode);
			Result bomRs = sqlListMap(sql, param);
			if(bomRs.retCode != ErrorCanst.DEFAULT_SUCCESS || !(bomRs.retVal  instanceof ArrayList) || ((ArrayList)bomRs.retVal).size()==0){
				//没有可用bom,界面不应该显示可以展开
			}else{
				goodsItem.put("bomList", bomRs.retVal);
			}
			
			String goodF = "";
			for (String[] shows : BaseEnv.reportShowSet) { 
				if ("tblGoods".equals(shows[0]) && "1".equals(shows[2])) {
					goodF +=","+shows[1];
				}
			}
			goodF = goodF.substring(1);
			sql = " select "+goodF+",attrType from tblGoods where classCode =? ";
			param = new ArrayList();
			param.add(GoodsCode);
			Result goodsRs = sqlListMap(sql,param);
			HashMap goodsMap = (HashMap)(((ArrayList)goodsRs.retVal).get(0));
			
			
			String mrpId = IDGenerater.getId();
			goodsItem.put("mrpId", mrpId);
			
//			//查当前成品的库存状况-----------------------------------------
//			Result stockRs = getStocks(GoodsCode, mrpId);
//			sql = " select type,SUM(Qty) lastQty from PDMRPStocks a where f_ref=? and GoodsCode=? and parentCode=?  group by type ";
//			BaseEnv.log.debug("查当前成品的库存状况"+sql);
//			param = new ArrayList();
//			param.add(mrpId);
//			param.add(GoodsCode);
//			param.add("");
//			Result tRs = sqlListMap(sql,param);
//			if(tRs.retCode != ErrorCanst.DEFAULT_SUCCESS){
//				return tRs;
//			}else{
//				ArrayList<HashMap> list = (ArrayList<HashMap>)tRs.retVal;
//				double stocks = 0d;
//				double buyApplication = 0d;
//				double produce = 0d;
//				double process = 0d;
//				for(HashMap map :list){
//					if("curStocks".equals(map.get("type")) || "oweStocks".equals(map.get("type"))){
//						stocks +=Double.parseDouble(map.get("lastQty")+"");
//					}else if("buyApplication".equals(map.get("type")) ){
//						buyApplication +=Double.parseDouble(map.get("lastQty")+"");
//					}else if("produce".equals(map.get("type")) ){
//						produce +=Double.parseDouble(map.get("lastQty")+"");
//					}else if("salesProcess".equals(map.get("type")) || "process".equals(map.get("type"))){
//						process +=Double.parseDouble(map.get("lastQty")+"");
//					}
//				}
//				goodsMap.put("StockQty", stocks);
//				goodsMap.put("OrderQty", buyApplication);
//				goodsMap.put("ProduceQty", produce);
//				goodsMap.put("PossesQty", process);
//				goodsMap.put("totalQty", stocks+buyApplication+produce-process);
//				
//			}
			goodsItem.putAll(goodsMap);
		}
		Result rs = new Result();
		return rs;
		
	}
	/**
	 * 第三步：选择BOM
	 * @param GoodsCode
	 * @return
	 */
	public Result selBom(String GoodsCode,String saleOrders,String mrpId){
		String sql=" select id,version,isLast from PDBom where GoodsCode=? and "
				+ " (case when (len(inValidate)=10 and inValidate<=CONVERT(varchar(10),getdate(),120)) or "
				+ "  (len(validate)=10 and validate>CONVERT(varchar(10),getdate(),120)) then 0 else 1 end) =1  order by isLast desc, version desc ";
		ArrayList param = new ArrayList();
		param.add(GoodsCode);
		Result bomRs = sqlListMap(sql, param);
		if(bomRs.retCode != ErrorCanst.DEFAULT_SUCCESS || !(bomRs.retVal  instanceof ArrayList) || ((ArrayList)bomRs.retVal).size()==0){
			bomRs.retCode = ErrorCanst.DEFAULT_FAILURE;
			bomRs.retVal = "本商品当前没有可用BOM";
			return bomRs;
		}
		
		String sales="";
		String require="";
		String keys[] = saleOrders.split(";");
		for(String key:keys){
			String type = key.split(":")[0];
			String val = key.split(":")[1];
			if(type.equals("tblSalesOrder")){
				sales +=",'"+val+"'";
			}else{
				require +=",'"+val+"'";
			}
		}
		if(sales.length() > 0) sales = sales.substring(1);
		else sales = "''";
		if(require.length() > 0) require = require.substring(1);
		else require="''";
		
		
		sql=	"   select tblSalesOrder.id,sum(isNull(qty,0)) TotalQty,sum(isNull(outqty,0)) outqty,sum(isNull(MRPQty,0)) MRPQty,'tblSalesOrder' BillType,BillNo,BillDate,tblSalesOrder.DepartmentCode,tblSalesOrder.EmployeeId,tblSalesOrder.CompanyCode,CreateBy "
				+ " from tblSalesOrder  join tblSalesOrderDet "
				+ " on tblSalesOrder.id=tblSalesOrderDet.f_ref and tblSalesOrderDet.GoodsCode='"+GoodsCode+"'    "
				+ " where tblSalesOrder.id in ("+sales+")  and tblSalesOrder.statusId=0  "
				+ " group by tblSalesOrder.id,BillNo,BillDate,tblSalesOrder.DepartmentCode,tblSalesOrder.EmployeeId,tblSalesOrder.CompanyCode,CreateBy"
				+ " union "
				+ " select PDProduceRequire.id,sum(isNull(qty,0)) TotalQty,0 outqty,sum(isNull(MRPQty,0)) MRPQty,'PDProduceRequire' BillType,BillNo,BillDate,PDProduceRequire.DepartmentCode,PDProduceRequire.EmployeeId,'',CreateBy "
				+ " from PDProduceRequire join PDProduceRequireDet "
				+ " on PDProduceRequire.id=PDProduceRequireDet.f_ref and PDProduceRequireDet.GoodsCode='"+GoodsCode+"'   "
				+ " where  PDProduceRequire.id in ("+require+")  and PDProduceRequire.statusId=0  "
				+ " group by PDProduceRequire.id,BillNo,BillDate,PDProduceRequire.DepartmentCode,PDProduceRequire.EmployeeId,CreateBy ";
		
		sql =  "select a.id,BillType,BillNo,BillDate,a.DepartmentCode,DeptFullName,a.EmployeeId,tblEmployee.EmpFullName,a.CompanyCode,ComName,ComNumber,a.createBy,cr.EmpFullName createByName,TotalQty,outqty,MRPQty,TotalQty-MRPQty qty "
				+ " from ("+sql+") a "
				+ " left join tblDepartment on a.DepartmentCode=tblDepartment.classCode "
				+ " join tblEmployee on a.EmployeeId=tblEmployee.id "
				+ " left join tblCompany on a.CompanyCode = tblCompany.classCode "
				+ " join tblEmployee cr on a.createBy=cr.id ";
		sql += " order by deptCode";
		BaseEnv.log.debug("MrpMgt查当前BOM 订单"+sql); 
		param = new ArrayList();
		Result salesRs = sqlListMap(sql,param);
		
		sql = " select * from tblGoods where classCode =? ";
		param = new ArrayList();
		param.add(GoodsCode);
		Result goodsRs = sqlListMap(sql,param);
		
		BigDecimal qty=new BigDecimal(0);
		for(HashMap map : (ArrayList<HashMap>)salesRs.retVal){
			qty =qty.add((BigDecimal)map.get("qty"));
		}
		HashMap goodsMap = (HashMap)(((ArrayList)goodsRs.retVal).get(0));
		//查当前成品的库存状况-----------------------------------------
		Result stockRs = getStocks(GoodsCode, mrpId);
		sql = " select type,SUM(Qty) lastQty from PDMRPStocks a where f_ref=? and GoodsCode=? and parentCode=?  group by type ";
		BaseEnv.log.debug("查当前成品的库存状况"+sql);
		param = new ArrayList();
		param.add(mrpId);
		param.add(GoodsCode);
		param.add("");
		Result tRs = sqlListMap(sql,param);
		if(tRs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			return tRs;
		}else{
			ArrayList<HashMap> list = (ArrayList<HashMap>)tRs.retVal;
			double stocks = 0d;
			double buyApplication = 0d;
			double produce = 0d;
			double process = 0d;
			for(HashMap map :list){
				if("curStocks".equals(map.get("type")) || "oweStocks".equals(map.get("type"))){
					stocks +=Double.parseDouble(map.get("lastQty")+"");
				}else if("buyApplication".equals(map.get("type")) ){
					buyApplication +=Double.parseDouble(map.get("lastQty")+"");
				}else if("produce".equals(map.get("type")) ){
					produce +=Double.parseDouble(map.get("lastQty")+"");
				}else if("salesProcess".equals(map.get("type")) || "process".equals(map.get("type"))){
					process +=Double.parseDouble(map.get("lastQty")+"");
				}
			}
			goodsMap.put("StockQty", stocks);
			goodsMap.put("OrderQty", buyApplication);
			goodsMap.put("ProduceQty", produce);
			goodsMap.put("PossesQty", process);
			goodsMap.put("totalQty", stocks+buyApplication+produce-process);
			
		}
		
		Result rs = new Result();
		rs.retVal = new Object[]{salesRs.retVal,goodsMap,bomRs.retVal,qty.doubleValue()};
		return rs;
	}
	/**
	 * 运算各种库存量，并存在PDMRPStocks中，作为历史记录，也方便读取
	 * 计算方法：
	 * 1、当前库存 （当前实际库存部分）
	 * 2、当前欠料（当前实际库存部分）
	 * 3、在途量 =当前未结案请购单未入库部分
	 * 4、在制量 =当前未结案工令单
	 * 5、占用量（销售订单/制造需求单占用量+已请购量-已发料量）（当前未结案未终止的订单，如果已结案其占用的料自动释放为公共资源）
	 * a、在订单终止和自动结案时要终止其物料占用
	 * b、下请购单和订单要回填物料需求表相应字段
	 * c、发料，退料，超领，补料要回填物料需求表相应字段
	 * @param bomId
	 * @param mrpId
	 * @param parentCode
	 * @return
	 */
	public Result mrpStocks(final String bomId,final String mrpId,final String parentCode){
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                            Connection conn = connection;
                            //当前库存
                            String sql = " insert into PDMRPStocks(f_ref,parentCode,GoodsCode,type,remark,qty,billType,billId,createTime)   "+
                					" select ?,?,PDBomdet.GoodsCode,'curStocks','当前库存',isnull(tblstocks.lastQty,0) qty,'tblStocks',tblStocks.id,convert(varchar(19),getdate(),120) from "+
                					"  PDBomdet join tblStocks  on PDBomDet.GoodsCode=tblStocks.GoodsCode "+
                					"  join tblStock on tblStocks.StockCode=tblStock.classCode  where f_ref =? and isnull(tblstocks.lastQty,0) >0 and StockType='Main'";

                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            pstmt.setString(1, mrpId);
                            pstmt.setString(2, parentCode);
                            pstmt.setString(3, bomId);
                            pstmt.execute();
                            //当前欠料
                            sql = " insert into PDMRPStocks(f_ref,parentCode,GoodsCode,type,remark,qty,billType,billId,createTime)   "+
                					" select ?,?,PDBomdet.GoodsCode,'oweStocks','当前欠料',isnull(tblstocks.OweQty,0) qty,'tblStocks',tblStocks.id,convert(varchar(19),getdate(),120) from "+
                					"  PDBomdet join tblStocks  on PDBomDet.GoodsCode=tblStocks.GoodsCode "+
                					"  join tblStock on tblStocks.StockCode=tblStock.classCode  where f_ref =? and isnull(tblstocks.OweQty,0) > 0 and StockType !='Main'";

                            pstmt = conn.prepareStatement(sql);
                            pstmt.setString(1, mrpId);
                            pstmt.setString(2, parentCode);
                            pstmt.setString(3, bomId);
                            pstmt.execute();

                            //查在途量 =当前未结案请购单（请购数量-入库数量）,请购单状态1已结案（缴库完成会自动结案），2已终止
                            sql = " insert into PDMRPStocks(f_ref,parentCode,GoodsCode,type,remark,qty,billType,billId,createTime)   "+
                            		" select ?,?,tblBuyApplicationDet.GoodsCode,'buyApplication','采购在途', ISNULL( tblBuyApplicationDet.NotInQty,0) Qty,'tblBuyApplication',tblBuyApplication.id,convert(varchar(19),getdate(),120) "
                					+ " from tblBuyApplication join tblBuyApplicationDet on tblBuyApplication.id=tblBuyApplicationDet.f_ref join PDBomdet on PDBomDet.GoodsCode=tblBuyApplicationDet.GoodsCode"+ 
                					" where  tblBuyApplication.statusId not in (1,2) and ISNULL( tblBuyApplicationDet.NotInQty,0) >0 and "
                					+ "PDBomDet.f_ref=?  ";
                            pstmt = conn.prepareStatement(sql);
                            pstmt.setString(1, mrpId);
                            pstmt.setString(2, parentCode);
                            pstmt.setString(3, bomId);
                            pstmt.execute();
                            
                            //查在制量 =当前未结案工令单（未缴库数量）
                            sql = " insert into PDMRPStocks(f_ref,parentCode,GoodsCode,type,remark,qty,billType,billId,createTime)   "+
                            		" select ?,?,PDWorkOrder.GoodsCode,'produce','在制量', ISNULL( PDWorkOrder.Qty,0) - ISNULL( PDWorkOrder.InPayQty,0) Qty,'PDWorkOrder',PDWorkOrder.id,convert(varchar(19),getdate(),120)   "
									+ " from PDWorkOrder join PDBomdet on PDBomDet.GoodsCode=PDWorkOrder.GoodsCode "
									+ "where PDWorkOrder.workFlowNodeName='finish' and PDWorkOrder.statusId not in (1,2) and ISNULL( PDWorkOrder.Qty,0) - ISNULL( PDWorkOrder.InPayQty,0) >0 "
									+ "and PDBomDet.f_ref=? ";
				            pstmt = conn.prepareStatement(sql);
                            pstmt.setString(1, mrpId);
                            pstmt.setString(2, parentCode);
                            pstmt.setString(3, bomId);
                            pstmt.execute();
                            
                            //占用量（销售订单/制造需求单占用量+已请购量-已发料量-补料-超领+退料）（当前未结案未终止的订单，如果已结案其占用的料自动释放为公共资源）
                            sql = " insert into PDMRPStocks(f_ref,parentCode,GoodsCode,type,remark,qty,billType,billId,createTime)   "+
                            		" select ?,?,PDMRPReqDet.GoodsCode,'process','占用量', (ISNULL( PDMRPReqDet.PossesQty,0) + ISNULL( PDMRPReqDet.BuyAppQty,0) -ISNULL( PDMRPReqDet.OutQty,0) -ISNULL( PDMRPReqDet.ReFetchQty,0) -ISNULL( PDMRPReqDet.ExceedQty,0) +ISNULL( PDMRPReqDet.ReturnQty,0)  ) Qty,'PDMRPReq',PDMRPReq.id,convert(varchar(19),getdate(),120) "
                					+ " from PDMRPReq join PDMRPReqDet on PDMRPReq.id=PDMRPReqDet.f_ref join PDBomdet on PDBomDet.GoodsCode=PDMRPReqDet.GoodsCode"+ 
                					" where  PDMRPReq.statusId not in (1) and (ISNULL( PDMRPReqDet.PossesQty,0) + ISNULL( PDMRPReqDet.BuyAppQty,0) -ISNULL( PDMRPReqDet.OutQty,0)  ) >0 and "
                					+ "PDBomDet.f_ref=?  ";
                            pstmt = conn.prepareStatement(sql);
                            pstmt.setString(1, mrpId);
                            pstmt.setString(2, parentCode);
                            pstmt.setString(3, bomId);
                            pstmt.execute();
                            
                            
                            
                            /**
                			 * 1、已进行MRP运算且下达采购申请未下工令或足料无需采购的全部计算为占用量，isBuy=0未采购，1已下采购单，2不需采购（原料充足）
                			 * 2、已下工令的所有未发料都算占用
                			 * 
                			 */
                            //未下工令已请购销售订单
//                			sql = " insert into PDMRPStocks(f_ref,parentCode,GoodsCode,type,remark,qty,billType,billId,createTime)   "+
//                            	"  select ?,?,c.GoodsCode,'noWorkSalesOrder','未下工令销售订单',isnull( a.NoWorkOrderQty*c.Qty,0) Qty,'tblSalesOrder',b.id,convert(varchar(19),getdate(),120) from tblSalesOrderDet a \n"+
//                				"join tblSalesOrder b on a.f_ref=b.id \n"+
//                				"join PDMRPGoods c on a.MRPId = c.f_ref and c.isOpen=0 \n"+
//                				"join PDMRP d on c.f_ref = d.id \n"+
//                				"join PDBomdet on PDBomDet.GoodsCode=c.GoodsCode\n"+
//                				"where d.isBuy<>0 and b.workFlowNodeName='finish' and b.statusId not in (1,2) and ISNULL(a.mrpid,'') <> '' and isnull( a.NoWorkOrderQty*c.Qty,0) > 0"
//                				+ " and PDBomDet.f_ref=?\n";
//                			
//                            pstmt = conn.prepareStatement(sql);
//                            pstmt.setString(1, mrpId);
//                            pstmt.setString(2, parentCode);
//                            pstmt.setString(3, bomId);
//                            pstmt.execute();
//                            //未下工令已请购制造需求单
//                            sql = " insert into PDMRPStocks(f_ref,parentCode,GoodsCode,type,remark,qty,billType,billId,createTime)   "+
//                                	"  select ?,?,c.GoodsCode,'noWorkRequire','未下工令制造需求单',isnull( a.NoWorkOrderQty*c.Qty,0) Qty,'PDProduceRequire',b.id,convert(varchar(19),getdate(),120) from PDProduceRequireDet a \n"+
//                    				"join PDProduceRequire b on a.f_ref=b.id \n"+
//                    				"join PDMRPGoods c on a.MRPId = c.f_ref and c.isOpen=0 \n"+
//                    				"join PDMRP d on c.f_ref = d.id \n"+
//                    				"join PDBomdet on PDBomDet.GoodsCode=c.GoodsCode\n"+
//                    				"where d.isBuy<>0 and b.workFlowNodeName='finish' and b.statusId not in (1,2) and ISNULL(a.mrpid,'') <> '' and isnull( a.NoWorkOrderQty*c.Qty,0) >0"
//                    				+ " and PDBomDet.f_ref=?\n";
//                            
//                            pstmt = conn.prepareStatement(sql);
//                            pstmt.setString(1, mrpId);
//                            pstmt.setString(2, parentCode);
//                            pstmt.setString(3, bomId);
//                            pstmt.execute();
                            
                            //已下工令未发料
//                            sql = " insert into PDMRPStocks(f_ref,parentCode,GoodsCode,type,remark,qty,billType,billId,createTime)   "+
//                                	" select ?,?,a.GoodsCode,'workOrderNoOut','已下工令未发料',a.SendQtyExt Qty,'PDWorkOrder',b.id,convert(varchar(19),getdate(),120)  from PDWorkOrderdet a \n"
//									+ "join PDWorkOrder b on b.id=a.f_ref \n"
//									+ "join PDBomdet on PDBomDet.GoodsCode=a.GoodsCode\n"
//									+ "where b.workFlowNodeName='finish' and b.statusId not in (1,2) and a.SendQtyExt > 0"
//									+ " and PDBomDet.f_ref=?  \n";
//                            
//                            pstmt = conn.prepareStatement(sql);
//                            pstmt.setString(1, mrpId);
//                            pstmt.setString(2, parentCode);
//                            pstmt.setString(3, bomId);
//                            pstmt.execute();
                            
                            
                        } catch (Exception ex) {
                            BaseEnv.log.error("MrpMgt.mrpStocks",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
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
	 * 第四步:ajax展开BOM树
	 * @param bomId
	 * @param mrpId
	 * @param parentCode
	 * @return
	 */
	public Result openBom(String bomId,String mrpId,final String parentCode){
		//查出Bom物料
		String sql = " select qty,lossRate,PDBomdet.id bomDetId,canSuper,isnull((select top 1 id from PDBom where GoodsCode=PDBomDet.GoodsCode and isLast=1 ),'') childBomId,tblGoods.* from "
				+ " PDBomdet join tblGoods on PDBomDet.GoodsCode=tblGoods.classCode "
				+ " where f_ref =? order by PDBomDet.id ";
		ArrayList param = new ArrayList();
		param.add(bomId);
		Result bomRs = sqlListMap(sql,param);
		if(bomRs.retCode == ErrorCanst.DEFAULT_SUCCESS){
		}
		return bomRs;
	}
	/**
	 * 运算保存后，展示MRP运算结果，
	 * @param keyId
	 * @return
	 */
	public Result showBom(String keyId){
		ArrayList param = new ArrayList();
		String sql =  "select a.billId,BillType,BillNo,BillDate,a.DepartmentCode,DeptFullName,a.EmployeeId,tblEmployee.EmpFullName,a.CompanyCode,ComName,ComNumber,a.Urgency,a.createBy,cr.EmpFullName createByName,qty,TotalQty,MRPQty "
				+ " from PDMRPBill a "
				+ " left join tblDepartment on a.DepartmentCode=tblDepartment.classCode "
				+ " left join tblEmployee on a.EmployeeId=tblEmployee.id "
				+ " left join tblCompany on a.CompanyCode = tblCompany.classCode "
				+ " left join tblEmployee cr on a.createBy=cr.id ";
		sql += " where a.f_ref=? ";
		param.add(keyId);
		sql += " order by a.id";
		BaseEnv.log.debug("MrpMgt.showBom 订单"+sql);
		Result salesRs = sqlListMap(sql,param);
		
		sql = " select bomId,needQty,PDMRP.id mrpId,PDBom.version,startWork,endWork  ,tblGoods.* from PDMRP join tblGoods on PDMRP.GoodsCode=tblGoods.classCode "
				+ " join PDBom on PDMRP.bomId=PDBom.id  where PDMRP.id =? ";
		param = new ArrayList();
		param.add(keyId);
		Result goodsRs = sqlListMap(sql,param);
		
		BigDecimal qty=new BigDecimal(0);
		for(HashMap map : (ArrayList<HashMap>)goodsRs.retVal){
			qty =qty.add((BigDecimal)map.get("needQty"));
		}
		
		sql = " select tblGoods.GoodsNumber,GoodsFullName,GoodsSpec,BaseUnit from  PDBom join tblGoods on PDBom.GoodsCode=tblGoods.classCode "
				+ " where PDBom.id =?  ";
		param = new ArrayList();
		param.add(keyId);
		Result bomRs = sqlListMap(sql,param);
		
		
		
		Result rs = new Result();
		rs.retVal = new Object[]{salesRs.retVal,goodsRs.retVal,bomRs,qty.doubleValue()};
		return rs;
	}

//	/**
//	 * 下达请购单
//	 * @param keyId
//	 * @param request
//	 * @return
//	 */
//	public Result toBuy(String keyId,HttpServletRequest request){
//		Result rs = new Result();
//		/*保存在内存中所有表结构的信息*/
//		Hashtable map = BaseEnv.tableInfos;
//
//		/*设置兄弟表的f_brother*/
//		String tableName = "tblBuyApplication";
//		/*父类的classCode*/
//		String parentCode = "";
//		//要执行的define的信息
//		String defineInfo = "";
//		
//		String saveType = "saveDraft"; //保存类型 saveDraft 草稿
//		
//		LoginBean lg = (LoginBean)request.getSession().getAttribute("LoginBean");
//		HashMap values = new HashMap();
//		values.put("BillDate", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
//		values.put("DepartmentCode", lg.getDepartCode());
//		values.put("EmployeeID", lg.getId());
//		values.put("PDMRPId", keyId);
//		ArrayList clist=new ArrayList();
//		values.put("TABLENAME_tblBuyApplicationDet", clist);
//		
//		ArrayList param = new ArrayList();
//		String sql = " select isOpen,max(PDMRPGoods.id) aid,f_ref,GoodsCode,BatchNo,Inch,Hue,yearNO,color,ProDate,Availably,sum(lackQty) lackQty,sum(totalLoss) totalLoss "
//				+ ",(select sum(Qty) from tblBuyApplicationDet a join tblBuyApplication b on a.f_ref=b.id where b.PDMRPId=PDMRPGoods.f_ref and a.GoodsCode=PDMRPGoods.GoodsCode ) totalQty  "
//				+ ",reTimes from  PDMRPGoods join tblGoods on PDMRPGoods.GoodsCode=tblGoods.classCode  "
//				+ " where f_ref =? and isOpen!=1 and clientSupper not in ('isClient','isSupper') group by isOpen,f_ref,GoodsCode,BatchNo,Inch,Hue,yearNO,color,ProDate,Availably,reTimes order by aid ";
//		param = new ArrayList();
//		param.add(keyId);
//		Result bomRs = sqlListMap(sql,param);
//		
//		if(bomRs.retCode != ErrorCanst.DEFAULT_SUCCESS){
//			rs.retCode = ErrorCanst.DEFAULT_FAILURE;
//			rs.retVal = "查询物料需求错误";
//			return rs;
//		}
//		boolean hasData = false ; //用来记录是否所有物料都已购齐
//		for(HashMap bmap : (ArrayList<HashMap>)bomRs.retVal){
//			HashMap val = new HashMap();
//			val.put("GoodsCode", bmap.get("GoodsCode"));
//			val.put("BatchNo",  bmap.get("BatchNo"));
//			val.put("Inch",  bmap.get("Inch"));
//			val.put("Hue",  bmap.get("Hue"));
//			val.put("yearNO", bmap.get("yearNO"));
//			val.put("color",  bmap.get("color"));
//			val.put("ProDate",  bmap.get("ProDate"));
//			val.put("Availably",  bmap.get("Availably"));
//			val.put("lackQty",  bmap.get("lackQty"));
//			val.put("totalLoss",  bmap.get("totalLoss"));
//			double lackQty = Double.parseDouble(bmap.get("lackQty")+"");
//			double totalQty = Double.parseDouble(bmap.get("totalQty")+"");
//			double qty = lackQty-totalQty;
//			if(qty>0){ //缺料大于0，才下请购单
//				double reTimes=1;
//				try{
//				reTimes = Double.parseDouble(bmap.get("reTimes")+"");
//				}catch(Exception ee){}
//				if(reTimes>1){ //补货陪量大于1时，要按补货陪量取整
//					//为防止补货偣量出现小数，因些都放大8个0取整
//					long breTimes = (long)(reTimes*1000000);
//					long bqty = (long)(qty*1000000);
//					bqty = ((bqty+breTimes-1)/breTimes) *breTimes;
//					qty = bqty/1000000.0;
//				}
//				val.put("hasQty", totalQty);
//				val.put("Qty",  qty);
//				clist.add(val);
//			}
//		}
//		if(clist.size()==0){
//			rs.retCode = ErrorCanst.DEFAULT_FAILURE;
//			rs.retVal = "本次运算不缺料，无需进行原材料采购";
//			return rs;
//		}
//		
//		
//
//		/*表结构信息*/
//		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, tableName);
//		
//		
//		
//
//		List fieldLists = tableInfo.getFieldInfos();
//		for (int i = 0; i < fieldLists.size(); i++) {
//			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) fieldLists.get(i);
//			if (fieldInfo.getFieldIdentityStr() != null && DBFieldInfoBean.FIELD_IDENTIFIER.equals(fieldInfo.getFieldIdentityStr())) {
//				// 单据编号
//				String key = "";
//				String defaultValue = fieldInfo.getDefaultValue();
//				if (defaultValue != null && !"".equals(defaultValue)) {
//					// 存在默认值
//					key = defaultValue;
//				} else {
//					key = tableInfo.getTableName() + "_" + fieldInfo.getFieldName();
//				}
//				BillNo billno = BillNoManager.find(key);
//				if (billno != null) {
//					if (!billno.isFillBack()) {
//						//未启用补号
//						if(fieldInfo.getDefaultValue()== null || fieldInfo.getDefaultValue().equals("")){
//							String defValue=getBillNoCode(tableName+"_"+fieldInfo.getFieldName(),lg);
//							values.put(fieldInfo.getFieldName(), defValue);
//						}else{
//							String defValue=getBillNoCode(fieldInfo.getDefaultValue(),lg);
//							values.put(fieldInfo.getFieldName(), defValue);
//						}
//					}
//				}
//			}
//		}
//
//		/*从内存中读取当前单据的工作流信息*/
//		MOperation mop = (MOperation)lg.getOperationMap().get("/UserFunctionQueryAction.do?tableName=tblBuyApplication");
//		if(mop==null || !mop.add){
//			rs.retCode = ErrorCanst.DEFAULT_FAILURE;
//			rs.retVal = "您没有添加采购订单的权限";
//		}
//		OAWorkFlowTemplate workFlow = null;
//
//		workFlow = BaseEnv.workFlowInfo.get(tableName);
//		//System.out.println("*****workFlow********"+workFlow);
//		/*初始化一些字段的基本信息*/
//		Result rs_initDBField = userFunMgt.initDBFieldInfo(tableInfo, lg, values, parentCode, workFlow, request);
//		/* fjj不成功 删除单据编号*/
//		if (rs_initDBField.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//			rs.retCode = ErrorCanst.DEFAULT_FAILURE;
//			rs.retVal = "初始化字段错误";
//			return rs;
//		}
//		if (rs_initDBField.retCode == ErrorCanst.CURRENT_ACC_BEFORE_BILL) {
//			/*不能录入会计前数据*/
//			rs.retCode = ErrorCanst.DEFAULT_FAILURE;
//			rs.retVal = GlobalsTool.getMessage(request, "com.currentaccbefbill");
//			return rs;
//		} else if (rs_initDBField.retCode == ErrorCanst.BILL_DATE_NOT_EXIST_CURRENT_ACC) {
//			/*单据日期的期间在会计期间中不存在*/
//			rs.retCode = ErrorCanst.DEFAULT_FAILURE;
//			rs.retVal = GlobalsTool.getMessage(request,"单据日期的期间在会计期间中不存在"); 
//			return rs;
//		}
//		
//		if("saveDraft".equals(saveType)){
//        	values.put("workFlowNodeName", "draft") ;
//        }else if("printSave".equals(saveType)){
//        	values.put("workFlowNodeName", "print") ;
//        	request.setAttribute("saveType", saveType);
//        }
//
//		//获取路径
//		String path = request.getSession().getServletContext().getRealPath("DoSysModule.sql");
//
//		Object ob = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
//		MessageResources resources = null;
//		if (ob instanceof MessageResources) {
//			resources = (MessageResources) ob;
//		}
//		String deliverTo = request.getParameter("deliverTo");
//		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
//
//		//zxy 将midCalculate 写入value中，方便 在define中以@valueOfDB:midCalculate=true来判断当前操作为中间运算
//		boolean isMidCalculate = false;
//		if ("midCalculate".equals(saveType)) {
//			saveType = "saveAdd";
//			isMidCalculate = true;
//			values.put("midCalculate", "true");
//		}
//		Locale loc = (Locale)request.getSession(true).getAttribute(org.apache.struts.Globals.LOCALE_KEY);
//		rs = dbmgt.add(tableInfo.getTableName(), map, values, lg.getId(), path, defineInfo, resources, loc, saveType, lg, workFlow, deliverTo, props, mop);
//
//		String kid = values.get("id") == null ? "" : values.get("id").toString();
//		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS || rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
//
//			String billTypeName = tableInfo.getDisplay().get(loc.toString());
//			
//			String[] returnValue = (String[]) rs.retVal;
//			/*当前多语言种类*/
//			String locale = loc.toString();
//			String addMessage = GlobalsTool.getMessage(request, "common.lb.add");
//			/*添加系统日志*/
//			int operation = 0;
//			if ("saveDraft".equals(saveType))
//				operation = 5;
//			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
//				dbmgt.addLog(operation, values, null, tableInfo, locale.toString(), lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(), "", billTypeName);
//			}
//
//			String message = GlobalsTool.getMessage(request, "common.msg.addSuccess");
//			if (returnValue[1] != null && returnValue.length > 0) {
//				if (rs.retCode == ErrorCanst.RET_DEFINE_SQL_ALERT) {
//					message = dbmgt.getDefSQLMsg(request, returnValue[1]);
//				} else {
//					message += "<br>" + GlobalsTool.getMessage(request, "userfunction.msg.billNoExistNew") + ":" + returnValue[1];
//				}
//			}
//			HashMap retMap = new HashMap();
//			retMap.put("msg", message);
//			retMap.put("keyId", kid);
//			rs.retVal=retMap;
//			return rs;
//		} else {
//			/*失败清空单据编号*/
//			if (rs.getRetCode() == ErrorCanst.RET_DEFINE_SQL_CONFIRM) {
//				// 自定义配置需要用户确认
//				ConfirmBean confirm = (ConfirmBean) rs.getRetVal();
//				String content = dbmgt.getDefSQLMsg(request, confirm.getMsgContent());
//				String jsConfirmYes = "";
//				String jsConfirmNo = "";
//				String saveAdd = request.getParameter("button");
//
//				jsConfirmYes = confirm.getFormerDefine() + ":" + confirm.getYesDefine() + ";";
//				if (confirm.getNoDefine().length() > 0) {
//					jsConfirmNo = confirm.getFormerDefine() + ":" + confirm.getNoDefine() + ";";
//				}
//				rs.retVal="请购单不应该配置确认提示";
//				return rs;
//			} else {
//				SaveErrorObj saveErrrorObj = dbmgt.saveError(rs, request, saveType);
//				rs.retVal=saveErrrorObj.getMsg();
//				return rs;
//			}
//		}
//		
//	}
	
//	public  String getBillNoCode(String key,LoginBean o){
//		String billStr = "";
//		BillNo billNo = new BillNoManager().find(key);		//加载Map并赋值
//		if(billNo == null){
//			billStr = "";
//		}else{
//			BillNoUnit unit = null;
//			if(billNo.isFillBack()){
//				unit = billNo.getInvers(new HashMap<String, String>(), o);
//			}else{
//				unit = billNo.getNumber(new HashMap<String, String>(), o);
//			}
//			if(unit != null){
//				billStr = unit.getValStr();
//			}
//		}
//		return billStr;
//	}
	
	
	public Result saveBom(final HashMap values,final LoginBean lb) {
        final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                        	saveBom(values, lb, connection);
                        } catch (Exception ex) {
                            BaseEnv.log.error("MrpMgt.saveBom",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
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
	 * 保存MRP运算结果
	 * @param values
	 * @param lb
	 * @return
	 */
	public Result saveBom(final HashMap values,final LoginBean lb,Connection connection) throws Exception{
        final Result rst = new Result();
        
        Connection conn = connection;
        String bNo = "";
        BillNo billno = BillNoManager.find("PDMRP_BillNo",conn);
		if (billno != null) {
				//启用单据编号连续后，从数据库读编号,或者单据编号为空
				String valStr = BillNoManager.find("PDMRP_BillNo", values, lb,conn);
				if ("".equals(valStr)) {
					/* 单据编号无法生成 */
					rst.retCode=ErrorCanst.DEFAULT_FAILURE;
            		rst.retVal="单据编号无法生成";
            		return rst;
				} else {
					bNo =valStr;
				}
		}else{
			rst.retCode=ErrorCanst.DEFAULT_FAILURE;
    		rst.retVal="请设置BillNo为单据编号类型";
    		return rst;
		}
        
        String sql = "INSERT INTO [PDMRP]([id],[isCatalog],[BillDate],[DepartmentCode],[EmployeeID],[Remark],"
        		+ "[createBy],[lastUpdateBy],[createTime],[lastUpdateTime],[statusId],[SCompanyID],[finishTime],"
        		+ "[printCount],[workFlowNode],[workFlowNodeName],[checkPersons],[GoodsCode],[BomId],needQty,"
        		+ "CompanyCode,BillNo,MRPType,startWork,endWork,merge)";
		sql += " values(?,0,?,?,?,?,?,?,?,?,0,'00001',?,0,'-1','finish','',?,?,?,?,?,?,?,?,?)";
        BaseEnv.log.debug(sql);
        String id = values.get("id")+"";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, id);
        pstmt.setString(2, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
        pstmt.setString(3, lb.getDepartCode());
        pstmt.setString(4, lb.getId());
        pstmt.setString(5, "");
        pstmt.setString(6, lb.getId());
        pstmt.setString(7, lb.getId());
        pstmt.setString(8, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
        pstmt.setString(9, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
        pstmt.setString(10, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
        pstmt.setString(11, values.get("GoodsCode")+"");
        pstmt.setString(12, values.get("bomId")+"");
        pstmt.setString(13, values.get("needQty")+"");
        //这里记录第一个单 据明细的往来代号到PDMRP表中，因为如果启用客供料，只允许同一客户的订单合并在一起规划，
        //相当于这个字段就是记录了这次规划的客户代号，有利于后面的算客供料和工令发料时选择客供料
        pstmt.setString(14, ((List<HashMap>)values.get("salesList")).get(0).get("CompanyCode")+"");
        pstmt.setString(15, bNo);
        pstmt.setString(16, values.get("MRPType")+"");
        pstmt.setString(17, values.get("startWork")+"");
        pstmt.setString(18, values.get("endWork")+"");
        pstmt.setString(19, values.get("merge")+"");
        pstmt.execute();
        
        boolean hasClientSupper = false;
        if(Double.parseDouble(values.get("MRPType")+"")==1){
        	//直接采购不用记录bom信息
        }else{
        	//这里只记录万能码商品的属性
        	if((List<HashMap>) values.get("goodsdeploy") != null){
				for (HashMap map : (List<HashMap>) values.get("goodsdeploy")) {
					sql = "INSERT INTO [PDMRPGoodsDeploy]([f_ref],[GoodsCode],dtype)";
					sql += " values(?,?,?)";
					BaseEnv.log.debug(sql);
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, id);
					pstmt.setString(2, map.get("GoodsCode") + "");
					pstmt.setString(3, map.get("dtype") + "");
					pstmt.execute();
				}
        	}
        	
        	
//            for(HashMap map:(List<HashMap>)values.get("bomList")){
//            	sql = "INSERT INTO [PDMRPBom]([f_ref],[GoodsCode],baseQty,[BomDetId],[isOpen],[BomId],[childBomId],[qty],[lossRate],"
//            			+ "[totalQty],[totalLoss],[attrType],[dtype],detCode,baseLossRate,clientSupper)";
//        		sql += " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//                BaseEnv.log.debug(sql);
//                pstmt = conn.prepareStatement(sql);
//                pstmt.setString(1, id);
//                pstmt.setString(2, map.get("GoodsCode")+"");
//                pstmt.setString(3, map.get("baseQty")+"");
//                pstmt.setString(4, map.get("bomDetId")+"");
//                pstmt.setString(5, map.get("isOpen")+"");
//                pstmt.setString(6, map.get("bomId")+"");
//                pstmt.setString(7, map.get("childBomId")+"");
//                pstmt.setString(8, map.get("qty")+"");
//                pstmt.setString(9, map.get("lossRate")+"");
//                pstmt.setString(10, map.get("totalQty")+"");
//                pstmt.setString(11, map.get("totalLoss")+"");
//                pstmt.setString(12, map.get("attrType")+"");
//                pstmt.setString(13, map.get("dtype")+"");
//                pstmt.setString(14, map.get("detCode")+"");
//                pstmt.setString(15, map.get("baseLossRate")+"");
//                pstmt.setString(16, map.get("clientSupper")+"");
//                pstmt.execute();
//                if("isClient".equals(map.get("clientSupper"))){
//                	hasClientSupper = true;//有客供料，必须是同一客户的订单
//                }
//            }
        }
        String firstCompanyCode = "";
        for(HashMap map:(List<HashMap>)values.get("salesList")){
        	sql = "INSERT INTO [PDMRPBill]([f_ref],[BillType],[BillId],Totalqty,[qty],[BillNo],[BillDate],[DepartmentCode],[EmployeeId],[CompanyCode],[createBy],MRPQty,Urgency)";
    		sql += " values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            BaseEnv.log.debug(sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, map.get("BillType")+"");
            pstmt.setString(3, map.get("billId")+"");
            pstmt.setString(4, map.get("TotalQty")+"");
            pstmt.setString(5, map.get("qty")+"");
            pstmt.setString(6, map.get("BillNo")+"");
            pstmt.setString(7, map.get("BillDate")+"");
            pstmt.setString(8, map.get("DepartmentCode")+"");
            pstmt.setString(9, map.get("EmployeeId")+"");
            pstmt.setString(10, map.get("CompanyCode")+"");
            pstmt.setString(11, map.get("createBy")+"");
            pstmt.setString(12, map.get("MRPQty")+"");
            pstmt.setString(13, map.get("Urgency")+"");
            pstmt.execute();
            
            if(map.get("BillType").equals("PDProduceRequire") && (Double.parseDouble(values.get("MRPType")+"")==1||Double.parseDouble(values.get("MRPType")+"")==2)){
            	rst.retCode=ErrorCanst.DEFAULT_FAILURE;
        		rst.retVal="制造需求单不能直接出库或直接采购，请返回上一步去掉制造需求单";
        		return rst;
            }
            
            if(hasClientSupper){ //有客供料，必须是同一客户的订单
            	if((map.get("CompanyCode")+"").equals("")){
            		rst.retCode=ErrorCanst.DEFAULT_FAILURE;
            		rst.retVal="制造需求单不可以使用客供料，请返回第一步去掉制造需求单";
            		return rst;
            	}else if(firstCompanyCode.equals("")){
            		firstCompanyCode=map.get("CompanyCode")+"";
            	}else if(!(map.get("CompanyCode")+"").equals(firstCompanyCode)){
            		rst.retCode=ErrorCanst.DEFAULT_FAILURE;
            		rst.retVal="使用客供料时，只能同时规划同一客户的订单，请返回第一步选择同一客户订单";
            		return rst;
            	}
            }
            
            if(Double.parseDouble(map.get("qty")+"")<=0){
            	continue; //数量小于不用分配订单量
            }
            
            //分配订单的已运算数量
            if(map.get("BillType").equals("tblSalesOrder")){
            	sql = " select id,Qty,isnull(MRPQty,0) MRPQty from tblSalesOrderDet where f_ref=? and GoodsCode=?";
            	BaseEnv.log.debug(sql);
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, map.get("billId")+"");
                pstmt.setString(2, values.get("GoodsCode")+"");
                ResultSet rset = pstmt.executeQuery();
                Double qty = Double.parseDouble(map.get("qty")+"");
                String firstId = null;
                while(rset.next()){
                	if(firstId==null){
                		firstId = rset.getString("id");
                	}
                	Double lQty = rset.getDouble("Qty")-rset.getDouble("MRPQty");
                	if(qty<=lQty){
                		sql = " update tblSalesOrderDet set MRPQty=? where id=? ";
                		PreparedStatement pstmt1 = conn.prepareStatement(sql);
                		pstmt1.setDouble(1, rset.getDouble("MRPQty") +qty);
                		pstmt1.setString(2, rset.getString("id"));
                		pstmt1.execute();
                		
                		//记录分配过程，方便，删除时扣减数量
                		sql = " insert into  PDMRPAllot(f_ref,BillId,BillDetId,BillType,Qty) values(?,?,?,?,?) ";
                		pstmt1 = conn.prepareStatement(sql);
                		pstmt1.setString(1, id);
                		pstmt1.setString(2, map.get("billId")+"");
                		pstmt1.setString(3, rset.getString("id"));
                		pstmt1.setString(4, "tblSalesOrder");
                		pstmt1.setDouble(5, qty);
                		pstmt1.execute();
                		qty = 0d;
                		break;
                	}else{
                		sql = " update tblSalesOrderDet set MRPQty=? where id=? ";
                		PreparedStatement pstmt1 = conn.prepareStatement(sql);
                		pstmt1.setDouble(1, rset.getDouble("Qty"));
                		pstmt1.setString(2, rset.getString("id"));
                		pstmt1.execute();
                		//记录分配过程，方便，删除时扣减数量
                		sql = " insert into  PDMRPAllot(f_ref,BillId,BillDetId,BillType,Qty) values(?,?,?,?,?) ";
                		pstmt1 = conn.prepareStatement(sql);
                		pstmt1.setString(1, id);
                		pstmt1.setString(2, map.get("billId")+"");
                		pstmt1.setString(3, rset.getString("id"));
                		pstmt1.setString(4, "tblSalesOrder");
                		pstmt1.setDouble(5, lQty);
                		pstmt1.execute();
                		qty = qty - lQty;
                	}
                }
            }else{
            	sql = " select id,Qty,isnull(MRPQty,0) MRPQty from PDProduceRequireDet where f_ref=? and GoodsCode=?";
            	BaseEnv.log.debug(sql);
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, map.get("billId")+"");
                pstmt.setString(2, values.get("GoodsCode")+"");
                ResultSet rset = pstmt.executeQuery();
                Double qty = Double.parseDouble(map.get("qty")+"");
                String firstId = null;
                while(rset.next()){
                	if(firstId==null){
                		firstId = rset.getString("id");
                	}
                	Double lQty = rset.getDouble("Qty")-rset.getDouble("MRPQty");
                	if(qty<=lQty){
                		sql = " update PDProduceRequireDet set MRPQty=? where id=? ";
                		PreparedStatement pstmt1 = conn.prepareStatement(sql);
                		pstmt1.setDouble(1, rset.getDouble("MRPQty") +qty);
                		pstmt1.setString(2, rset.getString("id"));
                		pstmt1.execute();
                		//记录分配过程，方便，删除时扣减数量
                		sql = " insert into  PDMRPAllot(f_ref,BillId,BillDetId,BillType,Qty) values(?,?,?,?,?) ";
                		pstmt1 = conn.prepareStatement(sql);
                		pstmt1.setString(1, id);
                		pstmt1.setString(2, map.get("billId")+"");
                		pstmt1.setString(3, rset.getString("id"));
                		pstmt1.setString(4, "PDProduceRequire");
                		pstmt1.setDouble(5, qty);
                		pstmt1.execute();
                		qty = 0d;
                		break;
                	}else{
                		sql = " update PDProduceRequireDet set MRPQty=? where id=? ";
                		PreparedStatement pstmt1 = conn.prepareStatement(sql);
                		pstmt1.setDouble(1, rset.getDouble("Qty"));
                		pstmt1.setString(2, rset.getString("id"));
                		pstmt1.execute();
                		//记录分配过程，方便，删除时扣减数量
                		sql = " insert into  PDMRPAllot(f_ref,BillId,BillDetId,BillType,Qty) values(?,?,?,?,?) ";
                		pstmt1 = conn.prepareStatement(sql);
                		pstmt1.setString(1, id);
                		pstmt1.setString(2, map.get("billId")+"");
                		pstmt1.setString(3, rset.getString("id"));
                		pstmt1.setString(4, "PDProduceRequire");
                		pstmt1.setDouble(5, lQty);
                		pstmt1.execute();
                		qty = qty - lQty;
                	}
                }
            }
        }
        
        rst.retVal=id;
                        
        return rst;

    }
	
	
}
