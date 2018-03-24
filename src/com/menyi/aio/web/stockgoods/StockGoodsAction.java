package com.menyi.aio.web.stockgoods;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;

import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.StockGoodsModuleBean;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.*;

/**
 * 
 * <p>
 * Title:�����ƷͼƬAction
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-08-08 ���� 10:15
 * @Copyright: �������
 * @Author fjj
 * @preserve all
 */
public class StockGoodsAction extends MgtBaseAction{
	
	StockGoodsMgt mgt = new StockGoodsMgt();
	
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
		
		/* ��ͬ���� */
		String optype = request.getParameter("optype");
		
		switch (operation) {
		case OperationConst.OP_ADD_PREPARE:
			/* ���ģ��ǰ */
			forward = addModulePre(mapping, form, request, response);
			break;
		case OperationConst.OP_ADD:
			/* ���ģ�� */
			forward = addModule(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			/* �޸�ģ��ǰ */
			forward = updateModulePre(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			/* �޸�ģ�� */
			forward = updateModule(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:
			/* ɾ��ģ�� */
			forward = deleteModule(mapping, form, request, response);
			break;
		case OperationConst.OP_QUERY:
			/* ��ѯ�����ƷͼƬ */
			forward = queryStockGoods(mapping, form, request, response);
			break;
		default:
			if(optype != null && "queryModule".equals(optype)){
				/* ��ѯ�����ģ�� */
				forward = queryModule(mapping, form, request, response);
			}else{
				/* ��ѯ�����ƷͼƬ */
				forward = queryStockGoods(mapping, form, request, response);
			}
		}
		return forward;
	}
	
	/**
	 * ��ѯ�����ƷͼƬ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryStockGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String moduleId = request.getParameter("moduleId");					//ģ��Id
		
		/* ��ѯ����ģ�� */
		Result rs = mgt.queryModule();
		StockGoodsModuleBean moduleBean = null;
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			List list = (ArrayList)rs.retVal;
			if(list != null && list.size()>0){
				if(moduleId == null || "".equals(moduleId)){
					moduleBean = (StockGoodsModuleBean)list.get(0);				//ȡĬ��ģ��
				}else{
					moduleBean = (StockGoodsModuleBean)mgt.loadModule(moduleId).retVal;
				}
			}
			request.setAttribute("moduleList", list);
		}
		
		request.setAttribute("moduleBean", moduleBean);
		/* ����ģ��ȡ�û����õ����������ʾ�� */
		
		HashMap searchMap = new HashMap();									//��������
		
		List searchList = new ArrayList();									//�����������ֶ�bean
		
		List showList = new ArrayList();									//Ҫ��ʾ���ֶ�����
		
		//������
		if(moduleBean.getSearchFields() != null){
			for(String searchField : moduleBean.getSearchFields().split(",")){
				//����ֵ��ȡ�ֶ���Ϣ��tblname.field��
				DBFieldInfoBean dbField = GlobalsTool.getFieldBean(searchField);
				if(dbField != null){
					//��ȡ����
					String parameValue = getParameter(dbField.getFieldName(), request) ;
					searchMap.put(dbField.getFieldName(), parameValue);
					if(dbField.getInputType() == 2){
						searchMap.put(dbField.getFieldName()+"_name", getParameter(dbField.getFieldName()+"_name", request));
					}
					searchList.add(dbField);
				}
			}
		}
		request.setAttribute("searchList", searchList);
		request.setAttribute("searchMap", searchMap);
		
		String fields = "";										//��Ʒ����ֶ�
		String otherField = "";
		//��ʾ��
		if(moduleBean.getShowFields() != null){
			for(String showField : moduleBean.getShowFields().split(",")){
				if(!"".equals(showField)){
					DBFieldInfoBean dbField = GlobalsTool.getFieldBean(showField);
					if(dbField != null){
						if(dbField.getInputType() == 2){
							//������
							if("tblGoods.BaseUnit".equals(showField)){
								//��λ
								fields += "(select unitName from tblUnit where tblUnit.id=tblGoods.BaseUnit) as BaseUnit,";
							}else if("tblGoods.CompanyCode".equals(showField)){
								//��Ӧ��
								fields += "(select ComFullName from tblCompany where tblCompany.classCode=tblGoods.CompanyCode) as CompanyCode,";
							}
						}else{
							fields += showField+",";
						}
					}else{
						dbField = new DBFieldInfoBean();
						//�������,���ƥ��,��浥��,����
						if("LastQty".equals(showField) || "LastTwoQty".equals(showField) ||
								"lastAmount".equals(showField)){
							otherField += "isnull(sum("+showField+"),0) as "+showField+",";
						}else if("lastPrice".equals(showField)){
							otherField += "(CASE isnull(sum(LastQty),0) WHEN 0 THEN 0 ELSE isnull(sum(lastAmount),0)/isnull(sum(LastQty),0) END) as "+showField+",";
						}
						dbField.setFieldName(showField);
					}
					showList.add(dbField);
				}
			}
		}
		request.setAttribute("showList", showList);
		
		//����
		String orders = request.getParameter("");
		int pageSize = getParameterInt("pageSize", request);
		int pageNo = getParameterInt("pageNo", request);
        if (pageNo == 0 ) {
        	pageNo = 1;
        }
        if (pageSize == 0) {
        	pageSize = 30;
        }
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("pageNo", pageNo);
		//��ѯ����
		Result result = mgt.queryStockGoods(searchMap,fields,otherField,orders,pageNo,pageSize);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//��ѯ�ɹ�
			List list = (ArrayList)result.retVal;
			request.setAttribute("goodsList", list) ;
			request.setAttribute("pageBar",pageBar2(result, request)) ;
		}
		return getForward(request, mapping, "stockgoodsList");
	}
	
	
	/**
	 * ��ѯ���õ���ʾģ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward queryModule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Result rs = mgt.queryModule();
		/* ��ѯ�ɹ� */
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			List list = (ArrayList)rs.retVal;
			request.setAttribute("list", list);
		}
		return getForward(request, mapping, "moduleList");
	}
	
	/**
	 * ���ģ��ǰ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addModulePre(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		/* ȡ��Ʒ����ֶ����� */
		String tableName = "tblGoods";
    	Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
    	DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
    	List fields = tableInfo.getFieldInfos();
    	List searchList = new ArrayList();									//������list
    	List showList = new ArrayList();									//��ʾ��list
    	HashMap map = new HashMap();
    	HashMap showMap = new HashMap();
    	for(int i=0;i<fields.size();i++){
    		DBFieldInfoBean fieldBean = (DBFieldInfoBean)fields.get(i);
    		map = new HashMap();
    		showMap = new HashMap();
    		if(fieldBean.getInputType() == 0 || fieldBean.getInputType() == 2 || fieldBean.getInputType() == 7){
    			//�����ֶ�����Ϊ���������
    			map.put("name", fieldBean.getDisplay().get(getLocale(request).toString()));
    			map.put("value", tableName+"."+fieldBean.getFieldName());
    			map.put("isnull", fieldBean.getIsNull());
    			searchList.add(map);
    			showMap.put("name", fieldBean.getDisplay().get(getLocale(request).toString()));
    			showMap.put("value", tableName+"."+fieldBean.getFieldName());
    			//������ʾͼƬ
    			if("tblGoods.Picture".equals(tableName+"."+fieldBean.getFieldName())){
    				showMap.put("isnull", 1);
    			}else{
    				showMap.put("isnull", fieldBean.getIsNull());
    			}
    			showList.add(showMap);
    		}
    	}
    	
    	//��ʾ��������ͽ��
    	String[] strName = new String[]{"�������","���ƥ��","��浥��","���"};
    	String[] strValue = new String[]{"LastQty","LastTwoQty","lastPrice","lastAmount"};
    	for(int i =0;i<strName.length;i++){
    		map = new HashMap();
    		map.put("name", strName[i]);
    		map.put("value", strValue[i]);
    		map.put("isnull", 0);
    		showList.add(map);
    	}
    	request.setAttribute("searchList", searchList);
    	request.setAttribute("showList", showList);
		
		return getForward(request, mapping, "dealModule");
	}
	
	
	/**
	 * ���ģ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addModule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String moduleName = request.getParameter("moduleName");						//ģ������
		String moduleDesc = request.getParameter("moduleDesc");						//ģ������
		String searchFields = request.getParameter("searchFields");					//�����ֶ�
		String showFields = request.getParameter("showFields");						//��ʾ�ֶ�
		
		StockGoodsModuleBean bean = new StockGoodsModuleBean();						//����bean
		
		LoginBean lg = this.getLoginBean(request);									//��ȡ�������ڴ��еĵ�½�û���Ϣ
		
		/**
		 * ��bean�и�ֵ
		 */
		bean.setCreateBy(lg.getId());
		bean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		bean.setId(IDGenerater.getId());
		bean.setModuleDesc(moduleDesc);
		bean.setModuleName(moduleName);
		bean.setSearchFields(searchFields);
		bean.setShowFields(showFields);
		
		
		/* �������� */
		Result rs = mgt.addModule(bean);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//��ӳɹ�
			request.setAttribute("dealAsyn", "true");
			String msg = "common.msg.addSuccess";
			EchoMessage.success().add(getMessage(
                    request, msg)).setBackUrl("/StockGoodsAction.do?optype=queryModule").
                    setAlertRequest(request);
		}else{
			//���ʧ��
			String msg = "common.msg.addFailture";
			EchoMessage.error().add(getMessage(request, msg)).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * ģ���޸�ǰ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward updateModulePre(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		/* ȡ��Ʒ����ֶ����� */
		String tableName = "tblGoods";
    	Hashtable allTables = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
    	DBTableInfoBean tableInfo = (DBTableInfoBean) allTables.get(tableName);
    	List fields = tableInfo.getFieldInfos();
    	List searchList = new ArrayList();
    	List showList = new ArrayList();									//��ʾ��list
    	HashMap map = new HashMap();
    	HashMap showMap = new HashMap();
    	for(int i=0;i<fields.size();i++){
    		DBFieldInfoBean fieldBean = (DBFieldInfoBean)fields.get(i);
    		map = new HashMap();
        	showMap = new HashMap();
    		if(fieldBean.getInputType() == 0 || fieldBean.getInputType() == 2 || fieldBean.getInputType() == 7){
    			//�����ֶ�����Ϊ���������
    			map.put("name", fieldBean.getDisplay().get(getLocale(request).toString()));
    			map.put("value", tableName+"."+fieldBean.getFieldName());
    			map.put("isnull", fieldBean.getIsNull());
    			searchList.add(map);
    			showMap.put("name", fieldBean.getDisplay().get(getLocale(request).toString()));
    			showMap.put("value", tableName+"."+fieldBean.getFieldName());
    			//������ʾͼƬ
    			if("tblGoods.Picture".equals(tableName+"."+fieldBean.getFieldName())){
    				showMap.put("isnull", 1);
    			}else{
    				showMap.put("isnull", fieldBean.getIsNull());
    			}
    			showList.add(showMap);
    		}
    	}
    	
    	//��ʾ��������ͽ��
    	String[] strName = new String[]{"�������","���ƥ��","��浥��","���"};
    	String[] strValue = new String[]{"LastQty","LastTwoQty","lastPrice","lastAmount"};
    	for(int i =0;i<strName.length;i++){
    		map = new HashMap();
    		map.put("name", strName[i]);
    		map.put("value", strValue[i]);
    		map.put("isnull", 0);
    		showList.add(map);
    	}
		
    	request.setAttribute("searchList", searchList);
    	request.setAttribute("showList", showList);
    	
    	/* ����ģ��Idȡģ������ */
    	String id = request.getParameter("id");
    	Result rs = mgt.loadModule(id);
    	if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		//���سɹ�
    		request.setAttribute("moduleBean", rs.retVal);
    	}
		return getForward(request, mapping, "dealModule");
	}
	
	
	/**
	 * �޸�ģ������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward updateModule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String id = request.getParameter("id");										//id
		String moduleName = request.getParameter("moduleName");						//ģ������
		String moduleDesc = request.getParameter("moduleDesc");						//ģ������
		String searchFields = request.getParameter("searchFields");					//�����ֶ�
		String showFields = request.getParameter("showFields");						//��ʾ�ֶ�
		
		StockGoodsModuleBean bean = (StockGoodsModuleBean)mgt.loadModule(id).retVal;//����id��ȡ��Ϣ
		
		/**
		 * ��bean�и�ֵ
		 */
		bean.setModuleDesc(moduleDesc);
		bean.setModuleName(moduleName);
		bean.setSearchFields(searchFields);
		bean.setShowFields(showFields);
		
		
		/* �������� */
		Result rs = mgt.updateModule(bean);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//�޸ĳɹ�
			request.setAttribute("dealAsyn", "true");
			String msg = "common.msg.updateSuccess";
			EchoMessage.success().add(getMessage(
                    request, msg)).setBackUrl("/StockGoodsAction.do?optype=queryModule").
                    setAlertRequest(request);
		}else{
			//�޸�ʧ��
			String msg = "common.msg.updateFailture";
			EchoMessage.error().add(getMessage(request, msg)).setAlertRequest(request);
		}
		
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * ɾ��ģ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward deleteModule(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String id = request.getParameter("id");
		Result rs = mgt.deleteModule(id);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//��ѯ�ɹ�
			if(Integer.valueOf(rs.retVal.toString())>0){
				request.setAttribute("msg", "ok");
			}
		}
		return getForward(request, mapping, "blank");
	}
}
