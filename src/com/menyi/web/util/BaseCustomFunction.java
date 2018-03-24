package com.menyi.web.util;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable;
import java.sql.Connection;
import java.util.ArrayList;
import com.menyi.aio.web.login.LoginBean;
import com.dbfactory.Result;

public class BaseCustomFunction {

    /**
     * ��ҳ������Զ��尴Ť
     * @param request HttpServletRequest
     * @param buttonStr String
     * @param type int TYPE_QUERY TYPE_ADD_PREPARE TYPE_UPDATE_RREPARE
     * @param userId int �û�ID�����ڼ�Ȩ
     */
    public void setButton(HttpServletRequest request,ArrayList buttonList,String userId) {
        request.setAttribute("customButton",buttonList);
    }
    /**
     * ��ѯ
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param tableName String ��ѯ�ı���
     * @param allTable Hashtable ���б���Ϣ��key = tableName value = DBTableInfoBean
     * @param tableView Hashtable ���б��DBTableViewBean ��Ϣ
     */
    public void onQuery(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request,
                        HttpServletResponse response,LoginBean loginBean,String tableName, Hashtable allTable,Hashtable tableView) {

    }
    /**
     * ���ǰ��׼��ҳ
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param tableName String ��ӵı���
     * @param allTable Hashtable ���б���Ϣ��key = tableName value = DBTableInfoBean
     */
    public void onAddPrepare(ActionMapping mapping, ActionForm form,
                             HttpServletRequest request,
                             HttpServletResponse response,LoginBean loginBean,
                             String tableName, Hashtable allTable) {

    }
    /**
     * �޸�ǰ��׼��ҳ
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param keyId int �޸ĵ�����ID��
     * @param tableName String �޸ĵı���
     * @param table Hashtable ���б���Ϣ��key = tableName value = DBTableInfoBean
     * @param values HashMap ֵ��key = fieldName value = ֵ�� ������ӱ� key = "TABLENAME_tableName" value = List(�ӹ�ϣ��)
     */
    public void onUpdatePrepare(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response,LoginBean loginBean, String keyId,
                                String tableName, Hashtable allTable,
                                HashMap values) {

    }

    /**
     * ���֮���Ҫ�������ݿ⶯�������ش˷���
     * @param conn Connection ���ݿ�����,�û������ڽӿ����Լ���������
     * @param tableName String Ҫ��ӵ��Զ��幦�ܵı���
     * @param allTables Map ���б���Ϣ��key = tableName value = DBTableInfoBean
     * @param values HashMap ֵ��key = fieldName value = ֵ�� ������ӱ� key = "TABLENAME_tableName" value = List(�ӹ�ϣ��)
     * @param retObj ��ֹbefore��������һ��ֵ������after�������е���
     * @return Result ���ؽ��
     */
    public Result onBeforeAdd(Connection conn, final String tableName,final Map allTables,
                           final HashMap values,Obj retObj) {
        return new Result();
    }
    /**
     * ���֮���Ҫ�������ݿ⶯�������ش˷���
     * @param conn Connection ���ݿ�����,�û������ڽӿ����Լ���������
     * @param tableName String Ҫ��ӵ��Զ��幦�ܵı���
     * @param allTables Map ���б���Ϣ��key = tableName value = DBTableInfoBean
     * @param values HashMap ֵ��key = fieldName value = ֵ�� ������ӱ� key = "TABLENAME_tableName" value = List(�ӹ�ϣ��)
     * @param id int ��Ӻ��²���������ֵ
     * @param beforeObj Obj ��before�����в����Ķ�Ӧ���ڴ����á��ɽ����ڲ���ִ��ǰ�Ķ���
     * @return Result ���ؽ��
     */
    public Result onAfterAdd(Connection conn, final String tableName, final Map allTables,
                          final HashMap values, final String id,final Obj beforeObj) {
        return new Result();
    }
    /**
     * ���޸�֮ǰҪ�������ݿ⶯�������ش˷���
     * @param conn Connection ���ݿ�����,�û������ڽӿ����Լ���������
     * @param tableName String Ҫ�޸ĵ��Զ��幦�ܵı���
     * @param allTables Map ���б���Ϣ��key = tableName value = DBTableInfoBean
     * @param values HashMap �޸ĵ�ֵ��key = fieldName value = ֵ�� ������ӱ� key = "TABLENAME_tableName" value = List(�ӹ�ϣ��)
     * @param retObj ��ֹbefore��������һ��ֵ������after�������е���
     * @return Result ���ؽ��
     */
    public Result onBeforUpdate(Connection conn, String tableName, Map allTables, HashMap values,Obj retObj) {
        return new Result();
    }
    /**
     * ���޸�֮��Ҫ�������ݿ⶯�������ش˷���
     * @param conn Connection ���ݿ�����,�û������ڽӿ����Լ���������
     * @param tableName String Ҫ�޸ĵ��Զ��幦�ܵı���
     * @param allTables Map ���б���Ϣ��key = tableName value = DBTableInfoBean
     * @param values HashMap �޸ĵ�ֵ��key = fieldName value = ֵ�� ������ӱ� key = "TABLENAME_tableName" value = List(�ӹ�ϣ��)
     * @param beforeObj Obj ��before�����в����Ķ�Ӧ���ڴ����á��ɽ����ڲ���ִ��ǰ�Ķ���
     * @return Result ���ؽ��
     */
    public Result onAfterUpdate(Connection conn, String tableName, Map allTables, HashMap values,final Obj beforeObj) {
        return new Result();
    }
    /**
     * ��ɾ��֮ǰҪ�������ݿ⶯�������ش˷���
     * @param conn Connection ���ݿ�����,�û������ڽӿ����Լ���������
     * @param tableName String Ҫɾ�����Զ��幦�ܵı���
     * @param allTables Hashtable ���б���Ϣ��key = tableName value = DBTableInfoBean
     * @param id String[] Ҫɾ���ļ�¼��������
     * @param retObj ��ֹbefore��������һ��ֵ������after�������е���
     * @return Result ���ؽ��
     */
    public Result onBeforDelete(Connection conn, final String tableName, final Hashtable allTables,
                             final String id[],Obj retObj) {
        return new Result();
    }
    /**
     * ��ɾ��֮��Ҫ�������ݿ⶯�������ش˷���
     * @param conn Connection ���ݿ�����,�û������ڽӿ����Լ���������
     * @param tableName String Ҫɾ�����Զ��幦�ܵı���
     * @param allTables Hashtable ���б���Ϣ��key = tableName value = DBTableInfoBean
     * @param id String[] Ҫɾ���ļ�¼��������
     * @param beforeObj Obj ��before�����в����Ķ�Ӧ���ڴ����á��ɽ����ڲ���ִ��ǰ�Ķ���
     * @return Result ���ؽ��
     */
    public Result onAfterDelete(Connection conn, final String tableName, final Hashtable allTables,
                             final String id[],final Obj beforeObj) {
        return new Result();
    }

}
