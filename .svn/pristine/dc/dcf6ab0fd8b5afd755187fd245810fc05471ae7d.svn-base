package com.koron.oa.workflow;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.TblBillNoBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.billNumber.BillFormat;
import com.menyi.aio.web.billNumber.BillNoMgt;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GenJS;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.InitMenData;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.OperationConst;

/**
 * <p>
 * Title:工作流表单设计
 * Description:
 * </p>
 * @Date:2013-04-9
 * @CopyRight:科荣软件
 * @Author:李文祥
 */
public class OAWorkFlowTableAction extends BaseAction {
	OAWorkFlowTableMgt mgt=new OAWorkFlowTableMgt();
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int operation = getOperation(request);
		String noback=request.getParameter("noback");//不能返回
		if(noback!=null&&noback.equals("true")){
			request.setAttribute("noback", true);
		}else{
			request.setAttribute("noback", false);
		}
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_ADD:
			String popupName=request.getParameter("isExistPopup");
			String selectType=request.getParameter("selectType");
			if("queryCase".equals(selectType)){
				//根据pattern转化格式
				forward = queryPattern(mapping, form, request, response);
			}else if("true".equals(popupName)){
				forward = isExistPopup(mapping,form,request,response);
			}else{
				forward = add(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_ADD_PREPARE:
			String view=request.getParameter("view");
			if("true".equals(view)){
				forward = view(mapping, form, request, response);
			}else{
				forward = addPrepare(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_UPDATE:
			forward = update(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			forward = updatePrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_IMPORT_PREPARE:
			forward = importFormPrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_IMPORT:
			String importType=request.getParameter("importType");
			if(!"img".equals(importType)){
				forward = importForm(mapping,form,request,response);
			}else{
				forward = importImg(mapping,form,request,response);
			}
			break;
		}
		return forward;
	}
	
	
	 /**
     * 判断物理表结构是否存在
     */
	 protected ActionForward isExistPopup(ActionMapping mapping,ActionForm form,
	           HttpServletRequest request, HttpServletResponse response) throws Exception {

		String popupName=request.getParameter("popupName");
		PopupSelectBean  popSelectBean = (PopupSelectBean) BaseEnv.popupSelectMap.get(popupName);
		String flag="false";
		if(popSelectBean!=null){
			flag="true|"+popSelectBean.getType();
		}
		request.setAttribute("msg", flag);
		return getForward(request, mapping, "blank");
	}
    
	 /**
		 * 根据pattern转化格式
		 * @param mapping
		 * @param form
		 * @param request
		 * @param response
		 * @return
		 */
	protected ActionForward queryPattern(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String patterns = request.getParameter("patterns");
		patterns = GlobalsTool.toChinseChar(patterns);
		LoginBean loginBean = this.getLoginBean(request);
		String result = new BillFormat(patterns).parseInt(000, new HashMap<String, String>(),loginBean,true);
		request.setAttribute("msg", result);
		return getForward(request, mapping, "blank");
	}
		/**
		 * 预览表单
		 * @param mapping
		 * @param form
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception
		 */
	protected ActionForward view(ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response) throws Exception {

		String tableName = getParameter("tableName", request);
		String tableCHName=getParameter("tableCHName", request);
		Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
	    DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);

		String keyId=request.getParameter("keyId");
		Result rs=mgt.getTableById(keyId);
		String layOutHtml="";
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			List obj=(List)rs.retVal;
			Object[] objList=(Object[]) obj.get(0);
			layOutHtml=(String) objList[0];
		}
		request.setAttribute("tableCHName", tableCHName);
		request.setAttribute("layOutHtml", layOutHtml);
		request.setAttribute("tableInfo", tableInfo);
		return getForward(request, mapping, "tableVesrion");
	}
		

	
	/**
	 * 导入表单准备
	 */
	protected ActionForward importFormPrepare(ActionMapping mapping,
				ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		return getForward(request, mapping, "importForm");
	}

	/**
	 * 导入表单
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward importForm(ActionMapping mapping,
             						ActionForm form,
             					HttpServletRequest request,
             					HttpServletResponse response) throws Exception {
		TableImportForm tableForm = (TableImportForm) form ;
	    FormFile file  = tableForm.getFormFile() ;
        String ext = file.getFileName().substring(file.getFileName().indexOf(".")+1) ;
        if(!("html".equalsIgnoreCase(ext) || "htm".equalsIgnoreCase(ext))){
        	EchoMessage.info().add(getMessage(request, "com.logo.type"))
			  				  .setAlertRequest(request);
        	return getForward(request, mapping, "alert") ;
        }
	    try {
	        String fileText = new String(file.getFileData(),"gb2312");
	        fileText=fileText.replace("field_", "");
	        request.setAttribute("msg", fileText);
	    } catch (Exception ex) {
	       ex.printStackTrace();
	    }
	    return getForward(request, mapping, "blank");
	}
	
	/**
	 * 上传图片
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward importImg(ActionMapping mapping,
									ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws Exception {
		TableImportForm tableForm = (TableImportForm) form ;
		FormFile file  = tableForm.getFormFile() ;
		String ext = file.getFileName().substring(file.getFileName().indexOf(".")+1) ;
		if(!("jpg".equalsIgnoreCase(ext) || "gif".equalsIgnoreCase(ext) || "jpeg".equalsIgnoreCase(ext) || "png".equalsIgnoreCase(ext) || "bmp".equalsIgnoreCase(ext) )){
			EchoMessage.info().add(getMessage(request, "com.logo.type"))
					  .setAlertRequest(request);
			return getForward(request, mapping, "alert") ;
		}
		
		// 获取图片存放路径
		String savePath = request.getSession().getServletContext().getRealPath("upload");
		String newFileName = file.getFileName();
	  	File imgFile = new File(savePath,newFileName) ;
	  	imgFile.createNewFile();
    	if(!imgFile.getParentFile().exists()){
    		imgFile.getParentFile().mkdirs() ;
    	}
    	FileOutputStream  fos = new FileOutputStream(imgFile);
        fos.write(file.getFileData());
        request.setAttribute("msg", newFileName);
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 打开添加表单页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward addPrepare(ActionMapping mapping,
             						ActionForm form,
             					HttpServletRequest request,
             					HttpServletResponse response) throws Exception {
		String tableName=request.getParameter("tableName");
		String tableCHName=request.getParameter("tableCHName");
		String designId=request.getParameter("designId");
		tableCHName=GlobalsTool.toChinseChar(tableCHName);
		request.setAttribute("tableName", tableName);
		request.setAttribute("tableCHName", tableCHName);
		request.setAttribute("designId", designId);
		
		return getForward(request, mapping, "tableDesign");
	}
	/**
	 * 添加表单
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward add(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws Exception {
		/*获取页面的表单标签*/
		String tableName=request.getParameter("tableName");
		String tableCHName=request.getParameter("tableCHName");
		
		String designId=request.getParameter("designId");
		request.setAttribute("designId", designId);
		String[] FieldList=request.getParameter("fieldStr").split("#");

		DBTableInfoBean tableInfo = new DBTableInfoBean();
		tableInfo.setDisplay(KRLanguageQuery.create(tableCHName,tableName,tableCHName));
		tableInfo.setLanguageId(tableInfo.getDisplay().getId());
        tableInfo.setId(IDGenerater.getId());
        tableInfo.setUpdateAble(DBTableInfoBean.CAN_UPDATE);
        tableInfo.setUdType(DBTableInfoBean.USER_TYPE);
    	tableInfo.setIsUsed(0);
    	tableInfo.setTableName(tableName);
        tableInfo.setCreateBy(getLoginBean(request).getId());
        tableInfo.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
        tableInfo.setLastUpdateBy(getLoginBean(request).getId());
        tableInfo.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
        tableInfo.setTableType(DBTableInfoBean.MAIN_TABLE);
        tableInfo.setFieldInfos(new ArrayList());
        DBFieldInfoBean fieldInfo = new DBFieldInfoBean();
         
        List<TblBillNoBean> billBeanList=new ArrayList<TblBillNoBean>();
        for(int i=0;i<FieldList.length;i++){ 
        	if(FieldList[i].trim().length()>0){
	        	String[] info=FieldList[i].split("\\|");
	        	DBFieldInfoBean fb = new DBFieldInfoBean();
	        	fb.setId(IDGenerater.getId());
	        	fb.setFieldName(info[0]);
	        	fb.setLanguageId(info[1]);
	        	if(!info[2].contains("BillNo")){
		        	if(info[2].contains("popup") || info[2].contains("count") ){// 1.定制弹出框口，需要保存弹出框值 2.计算控件，需要保存计算公式
		        		String strs[] =info[2].split("&&");
		        		fb.setInputValue(strs[1]);
		        		fb.setDefaultValue(strs[0]);
		        	}else{
		        		fb.setDefaultValue(info[2]); //将表单类型存入该字段，方便解析表单
		        	}
	        	}
	        	/*必填字段  start*/
	        	
	        	String isnull=info[3];
	        	if(!info[2].contains("BillNo")){ //单据编号
		        	if("true".equals(isnull)){ //必填
		        		fb.setIsNull(DBFieldInfoBean.IS_NULL); //所有字段改为可写，因为有可能必填字段必用户不小心删除后，字段仍然存在，保存失败
		        	}else{
		        		fb.setIsNull(DBFieldInfoBean.IS_NULL);
		        	}
	        	}
	        	if(info[2].contains("BillNo")){ //单据编号
	        		fb.setWidth(80);
	        		fb.setIsUnique((byte)1);
	        		fb.setFieldSysType("RowMarker");
	        		fb.setFieldIdentityStr("BillNo");
	        		fb.setIsCopy((byte)1);
	        		fb.setIsStat((byte)0);
	        		fb.setIsNull(DBFieldInfoBean.IS_NULL);
	        		fb.setStatusId(1);
	        		String strs[] =info[2].split("&&");
	        		String inputVal=strs[1];
        			String valList[]=inputVal.split("=");
        			int resetVal=5;
        			if("year".equals( valList[3])){
        				resetVal=1;
        			}else if("month".equals(valList[3])){
        				resetVal=2;
        			}
	        		
	        	 	TblBillNoBean billNoBean = new TblBillNoBean();
	            	billNoBean.setKey(tableInfo.getTableName()+"_"+fb.getFieldName());
	        		billNoBean.setReset(resetVal);
	        		billNoBean.setStart(Integer.parseInt(valList[1]));
	        		billNoBean.setStep(Integer.parseInt(valList[2]));
	        		billNoBean.setPattern(valList[0].toString());
	        		billNoBean.setBillNO(billNoBean.getStart()-billNoBean.getStep());
	        		billNoBean.setBillName(tableCHName+"_"+fb.getLanguageId());
	        		billNoBean.setLaststamp(System.currentTimeMillis());
	        		billNoBean.setIsAddbeform(true);
	        		if("true".equals(isnull)){ //必填
	        			billNoBean.setIsfillback(true);
	        		}else{
	        			billNoBean.setIsfillback(false);
	        		}
	        		billBeanList.add(billNoBean);
	        		
	        	}else{
		        	fb.setIsStat(DBFieldInfoBean.STAT_NO);
		        	fb.setIsUnique(DBFieldInfoBean.UNIQUE_NO);
		        	/*必填字段  结束*/
	        	}
	        	fb.setUdType(DBFieldInfoBean.USER_TYPE);
	        	fb.setListOrder((byte) 100);	
	        	fb.setInputType((byte)DBFieldInfoBean.INPUT_NORMAL);
	        	fb.setMaxLength(8001);
	        	fb.setFieldType((byte)DBFieldInfoBean.FIELD_ANY);
	        	fb.setTableBean(tableInfo);
	        	tableInfo.getFieldInfos().add(fb);
        	}
        }
       
        fieldInfo.setId(IDGenerater.getId());
        fieldInfo.setFieldName("createBy");
        fieldInfo.setFieldType(DBFieldInfoBean.FIELD_ANY);
        fieldInfo.setMaxLength(30);
        fieldInfo.setInputType(DBFieldInfoBean.INPUT_NO);
        fieldInfo.setIsNull(DBFieldInfoBean.NOT_NULL);
        fieldInfo.setListOrder((byte) 100);
        fieldInfo.setTableBean(tableInfo);
        fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
        fieldInfo.setDefaultValue("");
        fieldInfo.setLanguageId("建立人");
        tableInfo.getFieldInfos().add(fieldInfo);

        fieldInfo = new DBFieldInfoBean();
        fieldInfo.setId(IDGenerater.getId());
        fieldInfo.setFieldName("classCode");
        fieldInfo.setFieldType(DBFieldInfoBean.FIELD_ANY);
        fieldInfo.setMaxLength(500);
        fieldInfo.setInputType(DBFieldInfoBean.INPUT_NO);
        fieldInfo.setListOrder((byte) 0);
        fieldInfo.setDefaultValue("");
        fieldInfo.setTableBean(tableInfo);
        fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
        tableInfo.getFieldInfos().add(fieldInfo);
        
        fieldInfo = new DBFieldInfoBean();
        fieldInfo.setId(IDGenerater.getId());
        fieldInfo.setFieldName("workFlowNode");
        fieldInfo.setFieldType(DBFieldInfoBean.FIELD_ANY);
        fieldInfo.setMaxLength(30);
        fieldInfo.setDefaultValue("");
        fieldInfo.setInputType(DBFieldInfoBean.INPUT_NO);
        fieldInfo.setListOrder((byte) 0);
        fieldInfo.setTableBean(tableInfo);
        fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
        tableInfo.getFieldInfos().add(fieldInfo);

        fieldInfo = new DBFieldInfoBean();
        fieldInfo.setId(IDGenerater.getId());
        fieldInfo.setFieldName("workFlowNodeName");
        fieldInfo.setFieldType(DBFieldInfoBean.FIELD_ANY);
        fieldInfo.setMaxLength(30);
        fieldInfo.setDefaultValue("");
        fieldInfo.setInputType(DBFieldInfoBean.INPUT_NO);
        fieldInfo.setListOrder((byte) 0);
        fieldInfo.setTableBean(tableInfo);
        fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
        tableInfo.getFieldInfos().add(fieldInfo);
        
        fieldInfo = new DBFieldInfoBean();
        fieldInfo.setId(IDGenerater.getId());
        fieldInfo.setFieldName("checkPersons");
        fieldInfo.setFieldType(DBFieldInfoBean.FIELD_ANY);
        fieldInfo.setMaxLength(8000);
        fieldInfo.setDefaultValue("");
        fieldInfo.setInputType(DBFieldInfoBean.INPUT_NO);
        fieldInfo.setListOrder((byte) 0);
        fieldInfo.setTableBean(tableInfo);
        fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
        tableInfo.getFieldInfos().add(fieldInfo);

        fieldInfo = new DBFieldInfoBean();
        fieldInfo.setId(IDGenerater.getId());
        fieldInfo.setFieldName("lastUpdateBy");
        fieldInfo.setFieldType(DBFieldInfoBean.FIELD_ANY);
        fieldInfo.setMaxLength(30);
        fieldInfo.setInputType(DBFieldInfoBean.INPUT_NO);
        fieldInfo.setIsNull(DBFieldInfoBean.IS_NULL);
        fieldInfo.setListOrder((byte) 100);
        fieldInfo.setDefaultValue("");
        fieldInfo.setTableBean(tableInfo);
        fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
        fieldInfo.setLanguageId("最后修改人");
        tableInfo.getFieldInfos().add(fieldInfo);

        fieldInfo = new DBFieldInfoBean();
        fieldInfo.setId(IDGenerater.getId());
        fieldInfo.setFieldName("createTime");
        fieldInfo.setFieldType(DBFieldInfoBean.FIELD_DATE_LONG);
        fieldInfo.setInputType(DBFieldInfoBean.INPUT_NO);
        fieldInfo.setIsNull(DBFieldInfoBean.IS_NULL);
        fieldInfo.setMaxLength(19);
        fieldInfo.setDefaultValue("");
        fieldInfo.setListOrder((byte) 100);
        fieldInfo.setTableBean(tableInfo);
        fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
        fieldInfo.setLanguageId("建立时间");
        tableInfo.getFieldInfos().add(fieldInfo);

        fieldInfo = new DBFieldInfoBean();
        fieldInfo.setId(IDGenerater.getId());
        fieldInfo.setFieldName("lastUpdateTime");
        fieldInfo.setMaxLength(19);
        fieldInfo.setFieldType(DBFieldInfoBean.FIELD_DATE_LONG);
        fieldInfo.setInputType(DBFieldInfoBean.INPUT_NO);
        fieldInfo.setIsNull(DBFieldInfoBean.IS_NULL);
        fieldInfo.setListOrder((byte) 100);
        fieldInfo.setTableBean(tableInfo);
        fieldInfo.setDefaultValue("");
        fieldInfo.setUdType(DBFieldInfoBean.USER_TYPE);
        fieldInfo.setLanguageId("修改时间");
        tableInfo.getFieldInfos().add(fieldInfo);
        
        Object o = request.getSession().getServletContext().getAttribute(
                BaseEnv.TABLE_INFO);
        //获得data.txt路径
        String path=request.getSession().getServletContext().getRealPath("data.sql");
        DDLOperation ddlo = new DDLOperation();
        
        Result rs = ddlo.create((Hashtable) o, tableInfo,path);
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {//添加成功
        	BaseEnv.tableInfos.put(tableInfo.getTableName(), tableInfo);
        	String layOutHtml=request.getParameter("layOutHtml");
        	
        	if(billBeanList.size()>0){ //向tblBillNo表中添加单据编号规则
        		for(TblBillNoBean billBean:billBeanList){
        			new BillNoMgt().addBillNumber(billBean);
        		}
        	}
        	
        	LoginBean loginBean=this.getLoginBean(request);
        	String createtTime =BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
        	Result rsVer=mgt.addTableVersion(tableInfo.getId(), layOutHtml, loginBean.getId(), createtTime,"add",designId);
        	if(rsVer.retCode==ErrorCanst.DEFAULT_SUCCESS){
        		EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).
        		setBackUrl("/OAWorkFlowTableAction.do?operation=7&turnType=page&designId="+designId+"&tableName="+tableName+"&tableCHName="+tableCHName).setAlertRequest(request);
        	}else{
        		EchoMessage.error().add(
    					getMessage(request, "common.msg.addFailture")).setAlertRequest(request);
        	}
        	return getForward(request, mapping, "alert");
        }else{
        	
        	EchoMessage.error().add(
					getMessage(request, "common.msg.addFailture")).setAlertRequest(request);
        	return getForward(request, mapping, "alert");
        }
	}
	/**
	 *修改表单设计
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @desc  表单修改，需要表单的老数据和新数据进行比较，同时判断字段的增加、删除、修改的操作
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward update(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws Exception {
		String tableName=request.getParameter("tableName");
		String tableCHName=request.getParameter("tableCHName");
		request.setAttribute("tableName", tableName);
		request.setAttribute("tableCHName", tableCHName);
		String designId=request.getParameter("designId");
		request.setAttribute("designId", designId);
	    Object o = request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
	    
	    String type=request.getParameter("turnType"); 
		
		if("layoutHTML".equals(type)){ //个性化界面保存
			String layOutHtml=request.getParameter("layOutHtml");
			Result rs=mgt.layoutSave(tableName,layOutHtml);
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				//保存和预览的操作提示				
				String url="/OAWorkFlowTableAction.do?operation=7&turnType=layoutHTML&designId="+designId+"&tableName="+tableName+"&tableCHName="+GlobalsTool.urlEncode(tableCHName)+"&type=simple";
				EchoMessage.success().add("修改成功，并已启动个性化界面").setBackUrl(url).setAlertRequest(request);
			}else{
				EchoMessage.error().add("修改失败").setAlertRequest(request);
			}
			return getForward(request, mapping, "alert");
		}
	    
        //原数据
        DBTableInfoBean oldInfo = DDLOperation.getTableInfo((Hashtable) o,tableName);
        if(oldInfo==null){
        	return add(mapping, form, request, response);
        }
		//获取表单的老数据
		String textFieldStr=request.getParameter("fieldStr");
		//获取表单的新数据
		String newtextFieldStr=request.getParameter("newfieldStr");

		List<String> addFieldList=new ArrayList<String>(); 	//增加的字段集合
		String layOutHtml=request.getParameter("layOutHtml");
		
    	LoginBean loginBean=this.getLoginBean(request);
    	String  createtTime =BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
		
    	try{
	    	String saveType=request.getParameter("saveType");
	    	request.setAttribute("saveType", saveType); //判断是否是保存预览
			//判断表单标签是否发生变化
	    	Result rs=mgt.getNewTable(tableName,designId);
			String data="";
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				List obj=(List)rs.retVal;
				if(obj.size()>0){
					Object[] objlist=(Object[]) obj.get(0);
					data=objlist[2].toString();
				}
				if(!layOutHtml.equals(data)){
					String[] textList=textFieldStr.split("#");
					String[] newtextList=newtextFieldStr.split("#");
				    List<String> tempList = Arrays.asList(textList);
				    List<String> tempnewList = Arrays.asList(newtextList);
				    List<String> textChangeList=new ArrayList<String>();
			        for(int i=0;i<tempList.size();i++){ 
			        	if(!tempnewList.contains(tempList.get(i))){
			        		textChangeList.add(tempList.get(i));
			        	}
			        }
			        for(int k=0;k<tempnewList.size();k++){ 
			        	if(!tempList.contains(tempnewList.get(k))){
			        		textChangeList.add(tempnewList.get(k));
			        	}
			        }
			        for(int m=0;m<textChangeList.size();m++){
			        	String Id=textChangeList.get(m).split("\\|")[0];
			        	String textId=Id+"|";
			        	if(!textFieldStr.contains(textId) && newtextFieldStr.contains(textId)){ //增加的字段
		        			addFieldList.add(textChangeList.get(m));
			        	}
			        }
			     
			        rs=mgt.updateField(tempnewList,addFieldList,oldInfo);
			        if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			        	mgt.addTableVersion(oldInfo.getId(), layOutHtml, loginBean.getId(), createtTime,saveType,designId);
			        	new InitMenData().initDBInformation(request.getSession().getServletContext(),tableName);
			        	
			        	//单击保存按钮时，删除预览所产生的表单版本
				        if("save".equals(saveType)){
				        	mgt.deleteViewVersion(oldInfo.getId(), designId);
				        }
			        }
				}
			}
			GenJS.clearJS();
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				//保存和预览的操作提示
				String msg=getMessage(request, "crm.help.saveSucess");
			    if("view".equals(saveType)){
			    	msg="正在加载预览，请稍候...";
			    }
				String url="/OAWorkFlowTableAction.do?operation=7&turnType=page&saveType="+saveType+"&designId="+designId+"&tableName="+tableName+"&tableCHName="+GlobalsTool.urlEncode(tableCHName);
				EchoMessage.success().add(msg).setBackUrl(url).setAlertRequest(request);
			}else{
				String msg = getMessage(request, "common.msg.addFailture");
				if(rs.retVal!=null){
					msg = String.valueOf(rs.retVal);
				}
				EchoMessage.error().add(msg).setAlertRequest(request);
			}
			return getForward(request, mapping, "alert");
    	}catch (Exception e) {
    		e.printStackTrace();
    		EchoMessage.error().add(getMessage(request, "common.msg.addFailture")).setAlertRequest(request);
    		return getForward(request, mapping, "alert");
		}
	}
	
	
	/**
	 * 表单设计准备
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward updatePrepare(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws Exception {
		
		String tableName=request.getParameter("tableName");
		String tableCHName=request.getParameter("tableCHName");
		String saveType=request.getParameter("saveType"); //预览或导出表单参数
		tableCHName=GlobalsTool.toChinseChar(tableCHName);
		String type=request.getParameter("turnType"); 
		
		String designId=request.getParameter("designId");
		if("layoutHTML".equals(type)){ //进入个性化界面
			request.setAttribute("tableName", tableName);
			request.setAttribute("designId", designId);
			request.setAttribute("tableCHName", tableCHName);
			
			DBTableInfoBean dbt = BaseEnv.tableInfos.get(tableName);
			request.setAttribute("fields", dbt.getFieldInfos());
			request.setAttribute("childTables", DDLOperation.getChildTables(tableName, BaseEnv.tableInfos));
			request.setAttribute("turnType", type);
			return mapping.findForward("layoutHTML");
		}else if("layoutHTMLData".equals(type)){ //取个性化数据
			DBTableInfoBean dbt = BaseEnv.tableInfos.get(tableName);
			request.setAttribute("msg", dbt.getLayoutHTML());
			return mapping.findForward("blank");
		}else if("page".equals(type)){
			request.setAttribute("tableName", tableName);
			request.setAttribute("designId", designId);
			request.setAttribute("tableCHName", tableCHName);
			request.setAttribute("saveType", saveType);
			String maxValue=mgt.getMaxFiledVal(tableName);
			request.setAttribute("maxValue", maxValue);
			request.setAttribute("turnType", type);
			return mapping.findForward("tableDesign");
		}else{
			Result rs=mgt.getNewTable(tableName,designId);
			String data="";
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				List obj=(List)rs.retVal;
				if(obj.size()>0){
					Object[] objlist=(Object[]) obj.get(0);
					data=objlist[2].toString();
				}
			}else{
				data="failure";
			}
			request.setAttribute("msg", data);
			return mapping.findForward("blank");
		}
	}
	
	protected ActionForward doAuth(HttpServletRequest request, ActionMapping mapping) {
	/*	MOperation mop=(MOperation) this.getLoginBean(request).getOperationMap().get("/OAWorkFlowTempQueryAction.do");
		int op = this.getOperation(request);
		if(op == OperationConst.OP_ADD && mop.add){
			return null;
		}
		if(op == OperationConst.OP_UPDATE && mop.update){
			return null;
		}*/
		LoginBean loginBean = getLoginBean(request) ;
        if (loginBean == null) {
            BaseEnv.log.debug("MgtBaseAction.doAuth() ---------- loginBean is null");
            return getForward(request, mapping, "indexPage");
        }
		return null;
	}
}
