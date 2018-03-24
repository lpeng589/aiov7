package com.menyi.aio.web.goodsProp;

import com.menyi.web.util.MgtBaseAction;


import com.dbfactory.Result;
import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.SystemState;

import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;

import java.util.ArrayList;
import com.menyi.web.util.BaseEnv;
import java.util.HashMap;
import java.util.Iterator;

import java.util.Hashtable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.dyndb.TableNameGenerate;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.web.util.IDGenerater;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.report.*;
import java.util.*;
import com.menyi.aio.bean.*;
import com.menyi.aio.web.alert.AlertCenterMgt;
import com.menyi.aio.web.usermanage.UserManageAction;
import com.menyi.aio.web.customize.CustomizeMgt;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.web.util.InitMenData;
import com.menyi.aio.web.enumeration.EnumerationSearchForm;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.SortUtil;


/**
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: �����</p>
 *
 * @author �����
 * @version 1.0
 */
public class PropAction extends MgtBaseAction {


    GoodsPropMgt mgt = new GoodsPropMgt();
    /**
     * exe ��������ں���
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     * @todo Implement this com.huawei.sms.web.util.BaseAction
     method
     */
    protected ActionForward exe(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

        //���ݲ�ͬ�������ͷ������ͬ��������
        int operation = getOperation(request);
        String type = request.getParameter("type");
        ActionForward forward = null;
        switch (operation) {
        case OperationConst.OP_ADD_PREPARE:
            if (type != null && type.equals("propSetPre")) {
                forward = propSetPre(mapping, form, request, response);
            } else if (type != null && type.equals("propSetQuery")) {
                forward = propSetQuery(mapping, form, request, response);
            } else {
                forward = addPrepare(mapping, form, request, response);
            }
            break;
        case OperationConst.OP_ADD:
            if (type != null && type.equals("propSetAdd")) {
                forward = propSetAdd(mapping, form, request, response);
            } else {
                forward = add(mapping, form, request, response);
            }            
            break;
        case OperationConst.OP_COPY:
            forward = add(mapping, form, request, response);
            break;
        case OperationConst.OP_UPDATE_PREPARE:
        case OperationConst.OP_COPY_PREPARE:
            forward = updatePrepare(mapping, form, request, response);
            break;
        case OperationConst.OP_UPDATE:
            if (type != null && type.equals("propSetStart")) {
                forward = propSetStart(mapping, form, request, response);
            } else if (type != null && type.equals("propSetStop")) {
                forward = propSetStop(mapping, form, request, response);
            } else {
                forward = update(mapping, form, request, response);
            }
            break;
        case OperationConst.OP_DELETE:
            if (type != null && type.equals("propSetDel")) {
                forward = propSetDel(mapping, form, request, response);
            } else {
                forward = delete(mapping, form, request, response);
            }            
            break;
        case OperationConst.OP_QUERY:
            forward = query(mapping, form, request, response);
            break;
        case OperationConst.OP_DETAIL:
            forward = detail(mapping, form, request, response);
            break;
        default:
            forward = query(mapping, form, request, response);
        }
        return forward;
    }

    /**
     * ͣ������
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward propSetStop(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws
        Exception {
        ActionForward forward = null;
        Result rs = null;
        String[] keyIds = request.getParameterValues("keyId");
        String propId = request.getParameter("propId");
        GoodsPropInfoBean gpBean = (GoodsPropInfoBean) mgt.detail(propId).getRetVal();
        Hashtable map = (Hashtable) request.getSession().getServletContext().
                        getAttribute(BaseEnv.TABLE_INFO);
        boolean isExist = false;
        for (int i = 0; i < keyIds.length; i++) {
            DBTableInfoBean tableInfo = (DBTableInfoBean) map.get(keyIds[i].split("@")[1]);
            List<DBFieldInfoBean> fieldsInfo = tableInfo.getFieldInfos();
            for (int j = 0; j < fieldsInfo.size(); j++) {
                if (fieldsInfo.get(j).getFieldName().equals(gpBean.getPropName())) {
                    isExist = true;
                }
            }
            if (!isExist) {
                EchoMessage.error().add(getMessage(
                    request, "property.set.delerror")).setBackUrl("/PropAction.do?operation=" + OperationConst.OP_ADD_PREPARE +
                    "&type=propSetPre&keyId=" + propId + "&winCurIndex=" + request.getParameter("winCurIndex")).
                    setAlertRequest(request);
                return getForward(request, mapping, "message");

            }

        }
        //ͣ������ʱ���жϸ������ڷֲֿ������Ƿ���ڲ��п�棬������ͣ��
        List<DBTableInfoBean> existTables = new ArrayList<DBTableInfoBean>();
        boolean existInStocks = false;
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            DBTableInfoBean tableInfo = (DBTableInfoBean) map.get(it.next());
            List<DBFieldInfoBean> fieldsInfo = tableInfo.getFieldInfos();
            for (int j = 0; j < fieldsInfo.size(); j++) {
                if (fieldsInfo.get(j).getFieldName().equals(gpBean.
                    getPropName())) {
                    existTables.add(tableInfo);

                }
            }
            
        }
        for(int i=0;i<existTables.size();i++){
        	DBTableInfoBean dbi=existTables.get(i);
        	if (dbi.getTableName().equalsIgnoreCase("tblStocks")) {
                existInStocks = true;
            }
        }
        if (existInStocks) {
            Result result = mgt.checkStopUse(gpBean.getPropName());
            if (result.getRetVal().equals("true")) {
                EchoMessage.error().add(getMessage(
                    request, "Property.stop.error")).setBackUrl("/PropAction.do?operation=" + OperationConst.OP_ADD_PREPARE +
                    "&type=propSetPre&keyId=" + propId + "&winCurIndex=" + request.getParameter("winCurIndex")).
                    setAlertRequest(request);
                return getForward(request, mapping, "message");
//    		EchoMessage.error().add(getMessage(
//                    request, "Property.stop.error")).setAlertRequest(
//                            request);
//            return getForward(request, mapping, "alert");
            }

        }
        //
        List<DBTableInfoBean> list = new ArrayList<DBTableInfoBean>();
        for (int i = 0; i < keyIds.length; i++) {
            DBTableInfoBean tableInfo = (DBTableInfoBean) map.get(keyIds[i].split("@")[1]);
            list.add(tableInfo);
        }
        //���ж�
        String path=request.getSession().getServletContext().getRealPath("GoodsProp.sql").toString();
        rs = mgt.propStop(gpBean, list,path);
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            //����ͣ�óɹ�
            EchoMessage.success().add(getMessage(
                request, "property.stop.success"))
                .setBackUrl("/PropAction.do?operation=" + OperationConst.OP_ADD_PREPARE + "&type=propSetPre&keyId=" + propId +
                            "&winCurIndex=" + request.getParameter("winCurIndex")).
                setAlertRequest(request);
            forward = getForward(request, mapping, "message");
            //��������
        	SystemState.instance.dogState = SystemState.DOG_RESTART;
        } else {
            //����ͣ��ʧ��
            EchoMessage.error().add(getMessage(request,
                                               "property.stop.error")).
                setRequest(request);
            forward = getForward(request, mapping, "message");
        }

        return forward;
    }

    /**
     * ��������
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward propSetStart(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws
        Exception {
        ActionForward forward = null;
        Result rs = null;
        String[] keyIds = request.getParameterValues("keyId");
        String propId = request.getParameter("propId");
        GoodsPropInfoBean gpBean = (GoodsPropInfoBean) mgt.detail(propId).getRetVal();
        Hashtable map = (Hashtable) request.getSession().getServletContext().
                        getAttribute(BaseEnv.TABLE_INFO);
        boolean isExist = false;
        for (int i = 0; i < keyIds.length; i++) {
            DBTableInfoBean tableInfo = (DBTableInfoBean) map.get(keyIds[i].split(
                "@")[1]);
            List<DBFieldInfoBean> fieldsInfo = tableInfo.getFieldInfos();
            for (int j = 0; j < fieldsInfo.size(); j++) {
                if (fieldsInfo.get(j).getFieldName().equals(gpBean.getPropName())) {
                    isExist = true;
                }
            }
            if (!isExist) {
                EchoMessage.error().add(getMessage(
                    request, "property.set.delerror")).setBackUrl(
                        "/PropAction.do?operation=" + OperationConst.OP_ADD_PREPARE +
                        "&type=propSetPre&keyId=" + propId + "&winCurIndex=" + request.getParameter("winCurIndex")).
                    setAlertRequest(request);
                return getForward(request, mapping, "message");

            }

        }
        List<DBTableInfoBean> list = new ArrayList<DBTableInfoBean>();
        for (int i = 0; i < keyIds.length; i++) {
            DBTableInfoBean tableInfo = (DBTableInfoBean) map.get(keyIds[i].split("@")[1]);
            list.add(tableInfo);
        }
        //�ɹ��󱣴�ű�
    	String path=request.getSession().getServletContext().getRealPath("GoodsProp.sql").toString();
        rs = mgt.propStart(gpBean, list,path);
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            //�����ڴ�����
            InitMenData imd = new InitMenData();
            imd.initDBInformation(request.getSession().getServletContext(),null);

            //��Ʒ�������óɹ�
            EchoMessage.success().add(getMessage(
                request, "property.start.success"))
                .setBackUrl("/PropAction.do?operation=" + OperationConst.OP_ADD_PREPARE + "&type=propSetPre&keyId=" + propId +
                            "&winCurIndex=" + request.getParameter("winCurIndex")).
                setAlertRequest(request);
            forward = getForward(request, mapping, "message");

        } else {
            //��Ʒ��������ʧ��
            EchoMessage.error().add(getMessage(request,
                                               "property.start.error")).
                setRequest(request);
            forward = getForward(request, mapping, "message");
        }

        return forward;
    }

    /**
     * ɾ����������
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward propSetDel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                       HttpServletResponse response) throws
        Exception {
        ActionForward forward = null;
        Result rs = null;
        String[] keyIds = request.getParameterValues("keyId");
        String propId = request.getParameter("propId");
        GoodsPropInfoBean gpBean = (GoodsPropInfoBean) mgt.detail(propId).getRetVal();
        Hashtable map = (Hashtable) request.getSession().getServletContext().
                        getAttribute(BaseEnv.TABLE_INFO);
        boolean isExist = false;
        for (int i = 0; i < keyIds.length; i++) {
            DBTableInfoBean tableInfo = (DBTableInfoBean) map.get(keyIds[i].split("@")[1]);
            List<DBFieldInfoBean> fieldsInfo = tableInfo.getFieldInfos();
            for (int j = 0; j < fieldsInfo.size(); j++) {
                if (fieldsInfo.get(j).getFieldName().equals(gpBean.getPropName())) {
                    isExist = true;
                }
            }
            if (!isExist) {
                EchoMessage.error().add(getMessage(
                    request, "property.set.delerror")).setBackUrl("/PropAction.do?operation=" + OperationConst.OP_ADD_PREPARE +
                    "&type=propSetPre&keyId=" + propId + "&winCurIndex=" + request.getParameter("winCurIndex")).
                    setAlertRequest(request);
                return getForward(request, mapping, "message");

            }

        }
        List<DBTableInfoBean> list = new ArrayList<DBTableInfoBean>();
        for (int i = 0; i < keyIds.length; i++) {
            DBTableInfoBean tableInfo = (DBTableInfoBean) map.get(keyIds[i].split("@")[1]);
            list.add(tableInfo);
        }
        //���ж�
        //ɾ���ɹ��󱣴�ű�
     	 String path=request.getSession().getServletContext().getRealPath("GoodsProp.sql").toString();
        rs = mgt.propSetDeL(gpBean, list,path);
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            //ɾ���ɹ�
            EchoMessage.success().add(getMessage(
                request, "common.msg.delSuccess"))
                .setBackUrl("/PropAction.do?operation=" + OperationConst.OP_ADD_PREPARE + "&type=propSetPre&keyId=" + propId +
                            "&winCurIndex=" + request.getParameter("winCurIndex")).
                setAlertRequest(request);
            forward = getForward(request, mapping, "message");
            //��������
        	SystemState.instance.dogState = SystemState.DOG_RESTART;
        } else {
            //ɾ��ʧ��
            EchoMessage.error().add(getMessage(request,
                                               "common.msg.delError")).
                setRequest(request);
            forward = getForward(request, mapping, "message");
        }

        return forward;
    }

    /**
     * �����������
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward propSetAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                       HttpServletResponse response) throws
        Exception {
        Result rs = null;
        ActionForward forward = getForward(request, mapping, "alert");
        String[] keyIds = request.getParameterValues("keyId");
        String propId = request.getParameter("propId");
        GoodsPropInfoBean gpBean = (GoodsPropInfoBean) mgt.detail(propId).getRetVal();
        Hashtable map = (Hashtable) request.getSession().getServletContext().
                        getAttribute(BaseEnv.TABLE_INFO);
        if (gpBean.getIsUsed() == 2) { //�����Ʒ����δ����
            EchoMessage.error().add(getMessage(
                request, "Property.set.notUsed")).setBackUrl("/PropAction.do?operation=" + OperationConst.OP_ADD_PREPARE +
                "&type=propSetPre&keyId=" + propId + "&winCurIndex=" + request.getParameter("winCurIndex")).
                setAlertRequest(request);
            return getForward(request, mapping, "message");

        }
        boolean isExist = false;
        for (int i = 0; i < keyIds.length; i++) {
            DBTableInfoBean tableInfo = (DBTableInfoBean) map.get(keyIds[i].split("@")[1]);
            List<DBFieldInfoBean> fieldsInfo = tableInfo.getFieldInfos();
            for (int j = 0; j < fieldsInfo.size(); j++) {
                if (fieldsInfo.get(j).getFieldName().equals(gpBean.getPropName())) {
                    isExist = true;
                }
            }
            if (isExist) {

                EchoMessage.error().add(getMessage(
                    request, "property.set.adderror")).setBackUrl("/PropAction.do?operation=" + OperationConst.OP_ADD_PREPARE +
                    "&type=propSetPre&keyId=" + propId + "&winCurIndex=" + request.getParameter("winCurIndex")).
                    setAlertRequest(request);
                return getForward(request, mapping, "message");

            }

        }

        Map<String, DBFieldInfoBean> fields = new HashMap<String, DBFieldInfoBean>();
        for (int i = 0; i < keyIds.length; i++) {
            String tableId = keyIds[i].split("@")[0];
            String tableName = keyIds[i].split("@")[1];
            DBFieldInfoBean fieldInfo = new DBFieldInfoBean();
            fieldInfo.setId(IDGenerater.getId());
            fieldInfo.setFieldName(gpBean.getPropName());
            fieldInfo.setFieldType(DBFieldInfoBean.FIELD_ANY);
            if (gpBean.getIsUsed() == 1) {
                fieldInfo.setInputType(DBFieldInfoBean.INPUT_MAIN_TABLE);
            } else {
                fieldInfo.setInputType(DBFieldInfoBean.INPUT_HIDDEN);
            }
            fieldInfo.setIsNull(DBFieldInfoBean.IS_NULL);
            fieldInfo.setListOrder((byte) 100);
            fieldInfo.setMaxLength(5000);
            fieldInfo.setUdType((byte) 0);
            DBTableInfoBean tableInfo = new DBTableInfoBean();
            tableInfo.setId(tableId);
            tableInfo.setTableName(tableName);
            fieldInfo.setTableBean(tableInfo);
            fieldInfo.setIsUnique(DBFieldInfoBean.UNIQUE_NO);
            fieldInfo.setIsStat(DBFieldInfoBean.STAT_NO);
            fieldInfo.setInputValue(gpBean.getAlertName());
            KRLanguage krlan = new KRLanguage();
            krlan.setId(IDGenerater.getId());
            krlan.map = gpBean.getDisplay().map;
            fieldInfo.setDisplay(krlan);

            fields.put(tableId, fieldInfo);
        }
        Map<String, DBFieldInfoBean> vfields = new HashMap<String, DBFieldInfoBean>();
        if(gpBean.getNameAndValue()==1&&gpBean.getIsUsed() == 1){//֧������&ֵ
        	 for (int i = 0; i < keyIds.length; i++) {
                 String tableId = keyIds[i].split("@")[0];
                 String tableName = keyIds[i].split("@")[1];
                 DBFieldInfoBean fieldInfo = new DBFieldInfoBean();
                 fieldInfo.setId(IDGenerater.getId());
                 fieldInfo.setFieldName(gpBean.getPropName()+"NV");
                 fieldInfo.setFieldType(DBFieldInfoBean.FIELD_ANY);
                 fieldInfo.setInputType(DBFieldInfoBean.INPUT_NORMAL);
                 fieldInfo.setIsNull(DBFieldInfoBean.IS_NULL);
                 fieldInfo.setListOrder((byte) 100);
                 fieldInfo.setMaxLength(5000);
                 fieldInfo.setUdType((byte) 0);
                 DBTableInfoBean tableInfo = new DBTableInfoBean();
                 tableInfo.setId(tableId);
                 tableInfo.setTableName(tableName);
                 fieldInfo.setTableBean(tableInfo);
                 fieldInfo.setIsUnique(DBFieldInfoBean.UNIQUE_NO);
                 fieldInfo.setIsStat(DBFieldInfoBean.STAT_NO);
                 fieldInfo.setInputValue("");
                 KRLanguage krlan = new KRLanguage();
                 krlan.setId(IDGenerater.getId());
                 krlan.map.put("zh_CN", gpBean.getDisplay().map.get("zh_CN")+"��ʾ��");
                 krlan.map.put("en", gpBean.getDisplay().map.get("en")+"DisplayName");
                 krlan.map.put("zh_HK", gpBean.getDisplay().map.get("zh_HK")+"�@ʾ��");
                 krlan.map.put("zh_TW", gpBean.getDisplay().map.get("zh_TW")+"�@ʾ��");
                 fieldInfo.setDisplay(krlan);
                 vfields.put(tableId, fieldInfo);
             }
        }
        //��ӳɹ��󱣴�ű�
      	 String path=request.getSession().getServletContext().getRealPath("GoodsProp.sql").toString();
        rs = mgt.propSetAdd(fields,vfields,path);
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            //��ӳɹ�
            EchoMessage.success().add(getMessage(
                request, "common.msg.addSuccess"))
                .setBackUrl("/PropAction.do?operation=" + OperationConst.OP_ADD_PREPARE + "&type=propSetPre&keyId=" + propId +
                            "&winCurIndex=" + request.getParameter("winCurIndex")).
                setAlertRequest(request);
            forward = getForward(request, mapping, "message");
            //��������
        	SystemState.instance.dogState = SystemState.DOG_RESTART;
        } else {
            //���ʧ��
            EchoMessage.error().add(getMessage(
                request, "common.msg.addFailture")).
                setAlertRequest(request);
            forward = getForward(request, mapping, "message");
        }
        return forward;

    }

    /**
     * ��Ʒ�������ò�ѯ
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward propSetQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                         HttpServletResponse response) throws
        Exception {
        String keyId = request.getParameter("propId");
        GoodsPropInfoBean gpBean = (GoodsPropInfoBean) mgt.detail(keyId).getRetVal();
        String display =gpBean.getDisplay().toString();
        request.setAttribute("keyId", keyId);
        request.setAttribute("display", display);
        String condition = request.getParameter("condition");
        ArrayList list = new ArrayList();
       
        Hashtable map = (Hashtable) request.getSession().getServletContext().
        getAttribute(BaseEnv.TABLE_INFO);
        String search = request.getParameter("name") == null ? "" :
                        request.getParameter("name");
        // search=GlobalsTool.toChinseChar(search);
        request.setAttribute("condition", condition);
        request.setAttribute("search", search);

        if (!"".equals(search)) {
            Iterator it = map.values().iterator();
            while (it.hasNext()) {
                DBTableInfoBean tableInfo = (DBTableInfoBean) it.next();
                List<DBFieldInfoBean> fieldsInfo = tableInfo.getFieldInfos();
                boolean isHave = false;
                boolean isUsed = false;
                for (int i = 0; i < fieldsInfo.size(); i++) {
                    if (fieldsInfo.get(i).getFieldName().equals(gpBean.getPropName())) {
                        isHave = true;
                        if (fieldsInfo.get(i).getInputType()!= DBFieldInfoBean.INPUT_HIDDEN) {
                            isUsed = true;
                        }
                    }
                }
                String isExist = "0";
                if (isHave) {
                    isExist = "1";
                }
                String used = "0";
                if (isUsed) {
                    used = "1";
                }
                Object[] os = new Object[] {tableInfo.getId(),
                              tableInfo.getTableName(),
                              tableInfo.getDisplay()==null?"":tableInfo.getDisplay().get(getLocale(request).toString()),
                              isExist, used};

                if (condition.equals("all")) {
                    if (tableInfo.getTableName().toUpperCase().
                        contains(search.
                                 toUpperCase()) ||
                        (tableInfo.getDisplay().get(getLocale(request).
                        toString()) != null &&
                         tableInfo.
                         getDisplay().get(getLocale(request).toString()).
                         toString().
                         toUpperCase().contains(search.toUpperCase()))) {
                        list.add(os);
                    }

                } else if (condition.equals("have")) {

                    if ((tableInfo.getTableName().toUpperCase().
                         contains(search.
                                  toUpperCase()) ||
                         (tableInfo.getDisplay().get(getLocale(request).
                        toString()) != null &&
                          tableInfo.
                          getDisplay().get(getLocale(request).toString()).
                          toString().
                          toUpperCase().contains(search.toUpperCase()))) && isHave) {
                        list.add(os);
                    }
                } else {
                    if ((tableInfo.getTableName().toUpperCase().
                         contains(search.
                                  toUpperCase()) ||
                         (tableInfo.getDisplay().get(getLocale(request).
                        toString()) != null &&
                          tableInfo.
                          getDisplay().get(getLocale(request).toString()).
                          toString().
                          toUpperCase().contains(search.toUpperCase()))) &&
                        !isHave) {
                        list.add(os);
                    }

                }
            }

        } else {
            Iterator it = map.values().iterator();
            while (it.hasNext()) {
                DBTableInfoBean tableInfo = (DBTableInfoBean) it.next();
                List<DBFieldInfoBean> fieldsInfo = tableInfo.getFieldInfos();
                boolean isHave = false;
                boolean isUsed = false;
                for (int i = 0; i < fieldsInfo.size(); i++) {
                    if (fieldsInfo.get(i).getFieldName().equals(gpBean.getPropName())) {
                        isHave = true;
                        if (fieldsInfo.get(i).getInputType() == DBFieldInfoBean.INPUT_MAIN_TABLE) {
                            isUsed = true;
                        }
                    }
                }
                String isExist = "0";
                if (isHave) {
                    isExist = "1";
                }
                String used = "0";
                if (isUsed) {
                    used = "1";
                }
                Object[] os = new Object[] {tableInfo.getId(),
                              tableInfo.getTableName(),
                              tableInfo.getDisplay()==null?"":tableInfo.getDisplay().get(getLocale(request).toString()),
                              isExist, used};
                if (condition.equals("all")) {
                    list.add(os);
                } else if (condition.equals("have")) {
                    if (isHave) {
                        list.add(os);
                    }
                } else {
                    if (!isHave) {
                        list.add(os);
                    }

                }
            }
        }
        List sortList = SortUtil.getPropSortList(list);
        //��ѯ�ɹ�
        request.setAttribute("result", sortList);

        return getForward(request, mapping, "propSet");
    }

    /**
     * ��Ʒ��������ǰ��׼��
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward propSetPre(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                       HttpServletResponse response) throws
        Exception {
        String keyId = request.getParameter("keyId");
        GoodsPropInfoBean gpBean = (GoodsPropInfoBean) mgt.detail(keyId).getRetVal();
        String display=gpBean.getDisplay().toString();
        request.setAttribute("display", display);
        request.setAttribute("keyId", keyId);
        //Ĭ����ʾȫ��
        ArrayList list = new ArrayList();
        
        Hashtable map = (Hashtable) request.getSession().getServletContext().
                        getAttribute(BaseEnv.TABLE_INFO);
        Iterator it = map.values().iterator();
        while (it.hasNext()) {
            DBTableInfoBean tableInfo = (DBTableInfoBean) it.next();
            List<DBFieldInfoBean> fieldsInfo = tableInfo.getFieldInfos();
            boolean isHave = false;
            boolean isUsed = false;
            for (int i = 0; i < fieldsInfo.size(); i++) {
                if (fieldsInfo.get(i).getFieldName().equals(gpBean.getPropName())) {
                    isHave = true;
                    if (fieldsInfo.get(i).getInputType() != DBFieldInfoBean.INPUT_HIDDEN) {
                        isUsed = true;
                    }

                }
            }
            String isExist = "2";
            if (isHave) {
                isExist = "1";
            }
            String used = "0";
            if (isUsed) {
                used = "1";
            }
            if(tableInfo.getDisplay()!=null){
            	Object[] os = new Object[] {tableInfo.getId(), tableInfo.getTableName(),
                          tableInfo.getDisplay().get(getLocale(request).toString()), isExist, used};
            	list.add(os);
            }

        }
        List sortList = SortUtil.getPropSortList(list);
        //��ѯ�ɹ�
        request.setAttribute("result", sortList);

        return getForward(request, mapping, "propSet");
    }


    /**
     * ���ǰ��׼��
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward addPrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                       HttpServletResponse response) throws
        Exception {
        request.removeAttribute("operation");

        return getForward(request, mapping, "propAdd");
    }

    /**
     * �������
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward add(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

        String propName = request.getParameter("propName");
        String propDisplay = request.getParameter("propDisplay");
        String joinTable = request.getParameter("joinTable");//������Ʒ��
        String selectAlert = request.getParameter("selectAlert");//������
        String inputBill = request.getParameter("inputBill");//����¼��
        String twoUnit = request.getParameter("twoUnit");//˫��λ
        String isSequence=request.getParameter("sequence");//���к�
        Result rs1 = mgt.queryPropName(propName);//�鿴�����Ƿ����
        if (rs1.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
            EchoMessage.error().add(getMessage(
                request, "common.msg.addFailture")).
                setAlertRequest(request);

        }else if((((ArrayList) rs1.getRetVal()).size()) > 0){
	        	EchoMessage.error().add(getMessage(request,
	            "property.porpName.exist")).setAlertRequest(request);
	            return getForward(request, mapping, "alert");
        }
        LoginBean lg = new LoginBean();
        lg = (LoginBean) request.getSession().getAttribute("LoginBean");
        String isUsed = request.getParameter("isUsed");
        String isCalculate = request.getParameter("isCalculate");//���гɱ�����
        String groupOrNot=request.getParameter("groupOrNot");//����
        String nameAndValue=request.getParameter("nameAndValue");//֧������&ֵ
        GoodsPropInfoBean gpBean = new GoodsPropInfoBean();
        gpBean.setId(IDGenerater.getId());
        gpBean.setPropName(propName);
        gpBean.setDisplay(KRLanguageQuery.create((Hashtable) request.getSession().getServletContext().getAttribute("LocaleTable"),
                                                 (Locale) request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY),propDisplay));
        gpBean.setLanguageId(gpBean.getDisplay().getId());
        gpBean.setIsUsed(Integer.parseInt(isUsed));
        gpBean.setIsCalculate(Integer.parseInt(isCalculate));
        gpBean.setJoinTable(Integer.parseInt(joinTable));
        gpBean.setIsSequence(Integer.parseInt(isSequence));
        gpBean.setSepAppoint(Integer.parseInt(request.getParameter("sepAppoint")));
        gpBean.setGroupOrNot(Integer.parseInt(groupOrNot));
        gpBean.setNameAndValue(Integer.parseInt(nameAndValue));
        gpBean.setAlertName(selectAlert);
        gpBean.setInputBill(Integer.parseInt(inputBill));
        gpBean.setCreateBy(getLoginBean(request).getId());
        gpBean.setLastUpdateBy(getLoginBean(request).getId());
        gpBean.setCreateTime(BaseDateFormat.format(new Date(),
            BaseDateFormat.yyyyMMddHHmmss));
        gpBean.setLastUpdateTime(BaseDateFormat.format(new Date(),
            BaseDateFormat.yyyyMMddHHmmss));
        gpBean.setTwoUnit(Integer.parseInt(twoUnit));
        gpBean.setSCompanyID(lg.getSunCmpClassCode());
        ArrayList eItems = new ArrayList();
        gpBean.setEnumItem(eItems);
        String[] enumNames = this.getParameters("enumName", request);
        String[] enumValues = this.getParameters("enumValue", request);
        String[] groupNames=this.getParameters("goupName", request);
        String[] enumStatus = this.getParameters("enumStatu", request);
       if(!(enumValues[0].equals("")&&groupNames[0].equals(""))){
        if (enumValues != null && enumValues.length > 0) {
            for (int i = 0; i < enumValues.length; i++) {
                if (enumValues[i] != null &&
                		enumValues[i].trim().length() > 0) {
                    GoodsPropEnumItemBean gpItem = new GoodsPropEnumItemBean();
                    gpItem.setId(IDGenerater.getId());
                    String display = enumNames[i].trim();
                    gpItem.setDisplay(KRLanguageQuery.create((Hashtable) request.getSession().getServletContext().getAttribute("LocaleTable"),
                                                 (Locale) request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY),display));
                    gpItem.setLanguageId(gpItem.getDisplay().getId());
                    gpItem.setEnumValue(enumValues[i]);
                    gpItem.setGroupName(groupNames[i]);
                    gpItem.setIsUsed(Integer.parseInt(enumStatus[i]));
                    gpItem.setPropBean(gpBean);
                    gpItem.setSCompanyID(lg.getSunCmpClassCode());
                    eItems.add(gpItem);
                }
            }
        }
        //�������֧�ַ��飬������ֵ������������
        if(groupOrNot.equals("1")&&groupNames!=null&&groupNames.length>0){
        	for(int i=0;i<groupNames.length;i++){
        		String groupName=groupNames[i].replaceAll(" ","");
        		if(groupName.length()==0){
        			 EchoMessage.error().add(getMessage(
        		                request, "property.add.groupOrNotError")).
        		                setAlertRequest(request);
        			 return getForward(request, mapping, "alert");
        		}
        	}
        }
        //������Բ�֧�ַ��飬������ֵ������������
        if(groupOrNot.equals("2")&&groupNames!=null&&groupNames.length>0){
        	for(int i=0;i<groupNames.length;i++){
        		if(groupNames[i].length()>0){
        			 EchoMessage.error().add(getMessage(
        		                request, "property.add.groupOrNotError2")).
        		                setAlertRequest(request);
        			 return getForward(request, mapping, "alert");
        		}
        	}
        }
        if(groupOrNot.equals("1")){//������÷���,ͬһ�������ֵ��������ͬ
	        int currIndex=0;
	        for(int i=0;i<groupNames.length;i++){
	        	currIndex=i;
	        	for(int j=0;j<groupNames.length;j++){
	        		if(j!=currIndex&&groupNames[j].equals(groupNames[currIndex])){
	        			if(enumValues[j].equals(enumValues[currIndex])){
	        				EchoMessage.error().add(getMessage(
	        		                request, "property.add.valueEqGroupEq")).
	        		                setAlertRequest(request);
	        			 return getForward(request, mapping, "alert");
	        			}
	        		}
	        	}
	        }
        }else{//�����÷��飬����ֵ������ͬ
        	for(int i=0;i<enumValues.length-1;i++){
        		for(int j=i+1;j<=enumValues.length-1;j++){
        			if(enumValues[i].equals(enumValues[j])){
        				EchoMessage.error().add(getMessage(
        		                request, "Property.enumvalue.eq")).
        		                setAlertRequest(request);
        			 return getForward(request, mapping, "alert");
        			}
        		}
        		
        	}
        }
       }
        //��ӳɹ��󱣴�ű�
   	 String path=request.getSession().getServletContext().getRealPath("GoodsProp.sql").toString();
        Result rs = mgt.add(gpBean, getDefaultLocale(request).toString(),path);
        ActionForward forward = getForward(request, mapping, "alert");

        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            //��ӳɹ�
            EchoMessage.success().add(getMessage(
                request, "common.msg.addSuccess"))
                .setBackUrl("/PropQueryAction.do?winCurIndex=" + request.getParameter("winCurIndex")).
                setAlertRequest(request);
            //��������
        	SystemState.instance.dogState = SystemState.DOG_RESTART;
        } else {
            //���ʧ��
            EchoMessage.error().add(getMessage(
                request, "common.msg.addFailture")).
                setAlertRequest(request);
        }
        return forward;

    }

    /**
     * �޸�ǰ��׼��
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
        String nstr = request.getParameter("keyId");
        if (nstr != null && nstr.length() != 0) {
            //ִ�в�ѯǰ�ļ���
            Result rs = mgt.detail(nstr);
            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //���سɹ�
                request.setAttribute("result", rs.retVal);
                forward = getForward(request, mapping, "propUpdate");
            } else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
                //��¼�����ڴ���
                EchoMessage.error().add(getMessage(
                    request, "common.error.nodata")).setRequest(request);
            } else {
                //����ʧ��
                EchoMessage.error().add(getMessage(request, "common.msg.error")).
                    setRequest(request);
                forward = getForward(request, mapping, "message");
            }
        }
        return forward;

    }


    /**
     * ��ʾ�����޸�ǰ��׼��
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward tableViewPrepare(ActionMapping mapping,
                                             ActionForm form,
                                             HttpServletRequest request, HttpServletResponse response) throws
        Exception {
        ActionForward forward = null;
        return forward;
    }

    /**
     * �޸�
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
        LoginBean lg = new LoginBean();
        lg = (LoginBean) request.getSession().getAttribute("LoginBean");
        //ִ���޸Ĳ���
        Hashtable map = (Hashtable) request.getSession().getServletContext().
                        getAttribute(BaseEnv.TABLE_INFO);
        GoodsPropInfoBean bean = new GoodsPropInfoBean();
        bean.setId(request.getParameter("id"));
        Result rs = mgt.detail(bean.getId());
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            //����ʧ��
            EchoMessage.error().add(getMessage(
                request, "common.msg.updateErro")).setAlertRequest(
                    request);
            return getForward(request, mapping, "alert");
        }

        GoodsPropInfoBean oldbean = (GoodsPropInfoBean) rs.retVal;
        List<DBTableInfoBean> existTables = new ArrayList<DBTableInfoBean>();
        boolean isExist = false;
        boolean existInStocks = false;
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            DBTableInfoBean tableInfo = (DBTableInfoBean) map.get(it.next());
            List<DBFieldInfoBean> fieldsInfo = tableInfo.getFieldInfos();
            for (int j = 0; j < fieldsInfo.size(); j++) {
                if (fieldsInfo.get(j).getFieldName().equals(oldbean.
                    getPropName())) {
                    isExist = true;
                    existTables.add(tableInfo);

                }
            }
            if (tableInfo.getTableName().equalsIgnoreCase("tblStocks")) {
                existInStocks = true;
            }
        }
        String oldCalculate = request.getParameter("oldCalculate");
        if (Integer.parseInt(request.getParameter("isUsed")) == 2 || !oldCalculate.equals(request.getParameter("isCalculate"))) {
            if (existInStocks) {
                Result result = mgt.checkStopUse(oldbean.getPropName());
                if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && result.getRetVal().equals("true")) {
                    EchoMessage.error().add(getMessage(
                        request, "Property.stop.error")).setAlertRequest(
                            request);
                    return getForward(request, mapping, "alert");
                }

            }
        }
        if(oldbean.getNameAndValue()!=Integer.parseInt(request.getParameter("nameAndValue"))){//�����������֧������&ֵ�����ã�Ӧ��ɾ�����б��������˸����Ե�����
			Iterator nit = map.values().iterator();
			while (nit.hasNext()) {
			DBTableInfoBean tableInfo = (DBTableInfoBean) nit.next();
				if(GlobalsTool.getFieldBean(tableInfo.getTableName(),oldbean.getPropName())!=null){
					EchoMessage.error().add(getMessage(
	                        request, "property.upnv.error")).setAlertRequest(
	                            request);
	                return getForward(request, mapping, "alert");
				}
			}
        }
        bean.setId(oldbean.getId());
        bean.setCreateBy(oldbean.getCreateBy());
        bean.setCreateTime(oldbean.getCreateTime());
        bean.setLastUpdateBy(getLoginBean(request).getId());
        bean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
        bean.setPropName(oldbean.getPropName());
        String propDisplay = request.getParameter("propDisplay");
        bean.setDisplay(KRLanguageQuery.create((Hashtable) request.getSession().getServletContext().getAttribute("LocaleTable"),
                                                 (Locale) request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY),propDisplay));
        bean.setLanguageId(bean.getDisplay().getId());
        bean.setIsUsed(Integer.parseInt(request.getParameter("isUsed")));
        bean.setIsCalculate(Integer.parseInt(request.getParameter("isCalculate")));
        String joinTable = request.getParameter("joinTable");
        String selectAlert = request.getParameter("selectAlert");
        String inputBill = request.getParameter("inputBill");
        bean.setJoinTable(Integer.parseInt(joinTable));
        bean.setAlertName(selectAlert);
        bean.setInputBill(Integer.parseInt(inputBill));
        bean.setTwoUnit(Integer.parseInt(request.getParameter("twoUnit")));
        bean.setIsSequence(Integer.parseInt(request.getParameter("sequence")));
        bean.setSepAppoint(Integer.parseInt(request.getParameter("sepAppoint")));
        String oldPropDisplay = request.getParameter("oldPropDisplay");
        bean.setSCompanyID(lg.getSunCmpClassCode());
        String groupOrNot=request.getParameter("groupOrNot");
        bean.setGroupOrNot(Integer.parseInt(groupOrNot));
        bean.setNameAndValue(Integer.parseInt(request.getParameter("nameAndValue")));
//        if (!oldPropDisplay.equals(propDisplay)) {
//            Result rs2 = mgt.queryPropDisplay(propDisplay,getLocale(request).toString());
//            if ((Boolean)rs2.getRetVal()==true) {
//                EchoMessage.error().add(getMessage(request,
//                    "property.display.exist")).
//                    setAlertRequest(request);
//                return getForward(request, mapping, "alert");
//
//            }
//        }
        ArrayList eItems = new ArrayList();
        bean.setEnumItem(eItems);
        String[] fieldIds = this.getParameters("fieldId", request);
        String[] enumNames = this.getParameters("enumName", request);
        String[] enumValues = this.getParameters("enumValue", request);
        String[] groupNames=this.getParameters("goupName",request);
        String[] enumStatus = this.getParameters("enumStatu", request);
        String[] lgIds=this.getParameters("lgId", request);
        //����ֵ�������ӵ���Ʒ������ɾ��
        ArrayList noExistEnumValues=new ArrayList();
        for(int i=0;i<oldbean.getEnumItem().size();i++){
        	GoodsPropEnumItemBean gpItem =(GoodsPropEnumItemBean)oldbean.getEnumItem().get(i);
        	boolean isIn=false;
        	for(int j=0;enumValues!=null&&j<enumValues.length;j++){
        		if(enumValues[j].equals(gpItem.getEnumValue())){
        			isIn=true;
        		}
        	}
        	if(!isIn){
        		noExistEnumValues.add(gpItem.getEnumValue());
        	}
        }
        Result rs3=mgt.queryPropValuesInTblGoodsOfprop(oldbean.getId());
        if(rs3.getRetCode()==ErrorCanst.DEFAULT_SUCCESS&&((ArrayList)rs3.getRetVal()).size()>0){
        	ArrayList list=(ArrayList)rs3.getRetVal();
        	
        	for(int j=0;j<noExistEnumValues.size();j++){
        		boolean isIn=false;
	        	for(int i=0;i<list.size();i++){
	        		if(list.get(i).equals(noExistEnumValues.get(j))){
	        			isIn=true;
	        		}
	        	}
	        	if(isIn){
	        		 EchoMessage.error().add(getMessage(request,
	                    "property.enumValue.exist")).
	                    setAlertRequest(request);
	                return getForward(request, mapping, "alert");
	        	}
        	}
        }
        if(Integer.parseInt(groupOrNot)==1){//������÷���������ɾ�����޸��ӱ�����ֵ����
        	if(fieldIds.length!=oldbean.getEnumItem().size()&&oldbean.getEnumItem().size()!=0){
        		 EchoMessage.error().add(getMessage(request,
                 "prop.group.erorr")).
                 setAlertRequest(request);
                 return getForward(request, mapping, "alert");
        	}else{
        		for(int i=0;i<fieldIds.length;i++){
        			for(int j=0;j<oldbean.getEnumItem().size();j++){
        				GoodsPropEnumItemBean item=(GoodsPropEnumItemBean)oldbean.getEnumItem().get(j);
        				if(item.getId().equals(fieldIds[i])){
        					if(!item.getEnumValue().equals(enumValues[i])||!item.getGroupName().equals(groupNames[i])||item.getIsUsed()!=Integer.parseInt(enumStatus[i])||!item.getDisplay().toString().equals(enumNames[i])){
        						 EchoMessage.error().add(getMessage(request,
        		                 "prop.group.erorr")).
        		                 setAlertRequest(request);
        		                 return getForward(request, mapping, "alert");
        					}
        					break;
        				}
        			}
        		}
        		
        	}
        }
        
      //  if(!(enumValues[0].equals("")&&groupNames[0].equals(""))){
        //�������֧�ַ��飬������ֵ������������
        if(groupOrNot.equals("1")&&groupNames!=null&&groupNames.length>0){
        	for(int i=0;i<groupNames.length;i++){
        		String groupName=groupNames[i].replaceAll(" ","");
        		if(groupName.length()==0){
        			 EchoMessage.error().add(getMessage(
        		                request, "property.add.groupOrNotError")).
        		                setAlertRequest(request);
        			 return getForward(request, mapping, "alert");
        		}
        	}
        }
        //������Բ�֧�ַ��飬������ֵ������������
        if(groupOrNot.equals("2")&&groupNames!=null&&groupNames.length>0){
        	for(int i=0;i<groupNames.length;i++){
        		if(groupNames[i].length()>0){
        			 EchoMessage.error().add(getMessage(
        		                request, "property.add.groupOrNotError2")).
        		                setAlertRequest(request);
        			 return getForward(request, mapping, "alert");
        		}
        	}
        }
        
        if(groupOrNot.equals("1")){//������÷���,ͬһ�������ֵ��������ͬ
	        int currIndex=0;
	        for(int i=0;i<groupNames.length;i++){
	        	currIndex=i;
	        	for(int j=0;j<groupNames.length;j++){
	        		if(j!=currIndex&&groupNames[j].equals(groupNames[currIndex])){
	        			if(enumValues[j].equals(enumValues[currIndex])){
	        				EchoMessage.error().add(getMessage(
	        		                request, "property.add.valueEqGroupEq")).
	        		                setAlertRequest(request);
	        			 return getForward(request, mapping, "alert");
	        			}
	        		}
	        	}
	        }
        }else{//�����÷��飬����ֵ������ͬ
        	for(int i=0;enumValues!=null&&i<enumValues.length-1;i++){
        		for(int j=i+1;j<=enumValues.length-1;j++){
        			if(enumValues[i].equals(enumValues[j])){
        				EchoMessage.error().add(getMessage(
        		                request, "Property.enumvalue.eq")).
        		                setAlertRequest(request);
        			 return getForward(request, mapping, "alert");
        			}
        		}
        		
        	}
        }
        HashMap lgMap=new HashMap();
        if (enumValues != null && enumValues.length > 0) {
            for (int i = 0; i < enumValues.length; i++) {
                if (enumValues[i] != null &&
                		enumValues[i].trim().length() > 0) {
                    GoodsPropEnumItemBean gpItem = new GoodsPropEnumItemBean();
                    if (fieldIds[i] != null && fieldIds[i].length() != 0) {
                        gpItem.setId(fieldIds[i]);
                    } else {
                        gpItem.setId(IDGenerater.getId());
                    }
                    String display = enumNames[i].trim();
                    gpItem.setDisplay(KRLanguageQuery.create((Hashtable) request.getSession().getServletContext().getAttribute("LocaleTable"),
                                                 (Locale) request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY),display));
                    gpItem.setLanguageId(gpItem.getDisplay().getId());
                    if(fieldIds[i].equals(gpItem.getId())){
                    	if(lgIds[i]!=null&&lgIds[i].length()>0){
                    	lgMap.put(lgIds[i],gpItem.getLanguageId());
                    	}
                    }
                    gpItem.setEnumValue(enumValues[i]);
                    gpItem.setGroupName(groupNames[i]);
                    gpItem.setIsUsed(Integer.parseInt(enumStatus[i]));
                    gpItem.setPropBean(bean);
                    gpItem.setSCompanyID(lg.getSunCmpClassCode());
                    eItems.add(gpItem);
                }
            }
        }

        ActionForward forward = null;
        //ִ���޸�
        //mgt.execUpdateBeforDelOld(rs, oldbean);
        forward = getForward(request, mapping, "alert");
//        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//            //�޸�ʧ��
//            EchoMessage.error().add(getMessage(
//                request, "common.msg.updateErro")).setAlertRequest(
//                    request);
//            return forward;
//
//        }
        //�ɹ��󱣴�ű�
     	 String path=request.getSession().getServletContext().getRealPath("GoodsProp.sql").toString();
       rs = mgt.update(oldbean,bean, existTables, map,lgMap);

        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            if (oldbean.getIsUsed() != Integer.parseInt(request.getParameter("isUsed"))) {
                if (Integer.parseInt(request.getParameter("isUsed")) == 2) {
                    Result rsss = mgt.propStop(oldbean, existTables,path);
                } else {
                    Result rsss = mgt.propStart(oldbean, existTables,path);
                }
            }
            //�޸ĳɹ�
            EchoMessage.success().add(getMessage(
            						request, "common.msg.updateSuccess")+getMessage(request, "com.prop.start.service"))
            					 .setBackUrl("/PropQueryAction.do?winCurIndex=" + request.getParameter("winCurIndex"))
            					 .setNotAutoBack()
            					 .setAlertRequest(request);
            //�޸ĳɹ������������
        	SystemState.instance.dogState = SystemState.DOG_RESTART;
        } else {
            //�޸�ʧ��
            EchoMessage.error().add(getMessage(
                request, "common.msg.updateErro")).setAlertRequest(
                    request);
        }
        return forward;

    }

    /**
     * ����ɾ��
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward delete(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
        Exception {
        ActionForward forward = null;
        Hashtable map = (Hashtable) request.getSession().getServletContext().
                        getAttribute(BaseEnv.TABLE_INFO);
        String nstr[] = request.getParameterValues("keyId");
        boolean isExist = false;
        if (nstr != null && nstr.length != 0) {
            Result rs = null;
            List<GoodsPropInfoBean> list = new ArrayList<GoodsPropInfoBean>();
            for (int i = 0; i < nstr.length; i++) {
                GoodsPropInfoBean gpBean = (GoodsPropInfoBean) mgt.detail(nstr[i]).getRetVal();
                list.add(gpBean);
                Iterator it = map.values().iterator();
                while (it.hasNext()) {
                    DBTableInfoBean tableInfo = (DBTableInfoBean) it.next();
                    List<DBFieldInfoBean> fieldsInfo = tableInfo.getFieldInfos();
                    for (int j = 0; j < fieldsInfo.size(); j++) {
                    	DBFieldInfoBean fieldInfo=fieldsInfo.get(j);
                        if (fieldInfo!=null&&fieldInfo.getFieldSysType()!=null&&fieldInfo.getFieldName().equals(gpBean.getPropName())&&fieldInfo.getFieldSysType().equals("GoodsField")) {
                            isExist = true;
                        }
                    }
                }
            }
            if (isExist) {

                EchoMessage.error().add(getMessage(request, "property.del.not")).setBackUrl("/PropQueryAction.do?winCurIndex=" + request.getParameter("winCurIndex")).setAlertRequest(request);
                return getForward(request, mapping, "message");

            }
            //ɾ���ɹ��󱣴�ű�
          	 String path=request.getSession().getServletContext().getRealPath("GoodsProp.sql").toString();
            rs = mgt.delete(list, map,path);
            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //ɾ���ɹ�
                EchoMessage.success().add(getMessage(
                    request, "common.msg.delSuccess")).setBackUrl("/PropQueryAction.do?winCurIndex=" + request.getParameter("winCurIndex")).
                    setAlertRequest(request);
                //��������
            	SystemState.instance.dogState = SystemState.DOG_RESTART;
                return getForward(request, mapping, "message");
                
            } else {
                //ɾ��ʧ��
                EchoMessage.error().add(getMessage(
                    request, "common.msg.delFailture")).
                    setAlertRequest(request);
                forward = getForward(request, mapping, "message");
            }

        }
        return forward;

    }

    /**
     * ��ѯ
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward query(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws
        Exception {
        GoodsPropSearchForm searchForm = (GoodsPropSearchForm) form;
        if (searchForm != null) {
            //ִ�в�ѯ
            String name = request.getParameter("name");
            if(name!=null && name.length()>0){
            	name = GlobalsTool.toChinseChar(name) ;
            }
            Result rs = mgt.query(name, searchForm.getPageNo(), searchForm.getPageSize());
            ArrayList list = new ArrayList();
            for (int i = 0; i < ((ArrayList) (rs.retVal)).size(); i++) {
                GoodsPropInfoBean gpBean = (GoodsPropInfoBean) ((ArrayList) (rs.retVal)).get(i);
                String str = "";
                for (int j = 0; j < gpBean.getEnumItem().size(); j++) {
                    GoodsPropEnumItemBean gpItem = (GoodsPropEnumItemBean) gpBean.getEnumItem().
                        get(j);
                    String lo = gpItem.getDisplay() == null ? "" : gpItem.getDisplay().get(getLocale(
                        request).toString());
                    lo = lo == null ? "" : lo;

                    str += lo + ":" + gpItem.getEnumValue() + ";";
                }
                if (str.length() > 0) {
                    str = str.substring(0, str.length() - 1);
                }
                String lo = gpBean.getDisplay() == null ? "" : gpBean.getDisplay().get(getLocale(request).toString());
                lo = lo == null ? "" : lo;
                list.add(new String[] {gpBean.getId().toString(), gpBean.getPropName(), lo, str, String.valueOf(gpBean.getIsUsed()),
                         String.valueOf(gpBean.getIsCalculate()), String.valueOf(gpBean.getJoinTable()), String.valueOf(gpBean.getInputBill()),
                         String.valueOf(gpBean.getTwoUnit()),String.valueOf(gpBean.getIsSequence()),String.valueOf(gpBean.getSepAppoint()),String.valueOf(gpBean.getGroupOrNot()),String.valueOf(gpBean.getNameAndValue())});
            }
            request.setAttribute("condition", "all");
            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //��ѯ�ɹ�
                request.setAttribute("search", name);
                request.setAttribute("result", list);
                request.setAttribute("pageBar", pageBar(rs, request));
            } else {
                //��ѯʧ��
                EchoMessage.error().add(getMessage(request, "common.msg.error")).
                    setRequest(request);
                return getForward(request, mapping, "message");
            }
        }
        return getForward(request, mapping, "propList");

    }


    /**
     * ��ϸ
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward detail(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
        Exception {

        String nstr = request.getParameter("keyId");

        return getForward(request, mapping, "tableDetail");
    }

}
