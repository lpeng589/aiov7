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
     * 向页面添加自定义按扭
     * @param request HttpServletRequest
     * @param buttonStr String
     * @param type int TYPE_QUERY TYPE_ADD_PREPARE TYPE_UPDATE_RREPARE
     * @param userId int 用户ID，用于鉴权
     */
    public void setButton(HttpServletRequest request,ArrayList buttonList,String userId) {
        request.setAttribute("customButton",buttonList);
    }
    /**
     * 查询
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param tableName String 查询的表名
     * @param allTable Hashtable 所有表信息，key = tableName value = DBTableInfoBean
     * @param tableView Hashtable 所有表的DBTableViewBean 信息
     */
    public void onQuery(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request,
                        HttpServletResponse response,LoginBean loginBean,String tableName, Hashtable allTable,Hashtable tableView) {

    }
    /**
     * 添加前的准备页
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param tableName String 添加的表名
     * @param allTable Hashtable 所有表信息，key = tableName value = DBTableInfoBean
     */
    public void onAddPrepare(ActionMapping mapping, ActionForm form,
                             HttpServletRequest request,
                             HttpServletResponse response,LoginBean loginBean,
                             String tableName, Hashtable allTable) {

    }
    /**
     * 修改前的准备页
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param keyId int 修改的数据ID号
     * @param tableName String 修改的表名
     * @param table Hashtable 所有表信息，key = tableName value = DBTableInfoBean
     * @param values HashMap 值表，key = fieldName value = 值， 如果是子表 key = "TABLENAME_tableName" value = List(子哈希表)
     */
    public void onUpdatePrepare(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response,LoginBean loginBean, String keyId,
                                String tableName, Hashtable allTable,
                                HashMap values) {

    }

    /**
     * 添加之后的要做的数据库动作可重载此方法
     * @param conn Connection 数据库连接,用户不能在接口中自己产生连接
     * @param tableName String 要添加的自定义功能的表名
     * @param allTables Map 所有表信息，key = tableName value = DBTableInfoBean
     * @param values HashMap 值表，key = fieldName value = 值， 如果是子表 key = "TABLENAME_tableName" value = List(子哈希表)
     * @param retObj 在止before函数返回一个值，供在after函数的中调用
     * @return Result 返回结果
     */
    public Result onBeforeAdd(Connection conn, final String tableName,final Map allTables,
                           final HashMap values,Obj retObj) {
        return new Result();
    }
    /**
     * 添加之后的要做的数据库动作可重载此方法
     * @param conn Connection 数据库连接,用户不能在接口中自己产生连接
     * @param tableName String 要添加的自定义功能的表名
     * @param allTables Map 所有表信息，key = tableName value = DBTableInfoBean
     * @param values HashMap 值表，key = fieldName value = 值， 如果是子表 key = "TABLENAME_tableName" value = List(子哈希表)
     * @param id int 添加后新产生的主键值
     * @param beforeObj Obj 在before函数中产生的对应，在此引用。可接收在操作执行前的对象
     * @return Result 返回结果
     */
    public Result onAfterAdd(Connection conn, final String tableName, final Map allTables,
                          final HashMap values, final String id,final Obj beforeObj) {
        return new Result();
    }
    /**
     * 在修改之前要做的数据库动作可重载此方法
     * @param conn Connection 数据库连接,用户不能在接口中自己产生连接
     * @param tableName String 要修改的自定义功能的表名
     * @param allTables Map 所有表信息，key = tableName value = DBTableInfoBean
     * @param values HashMap 修改的值表，key = fieldName value = 值， 如果是子表 key = "TABLENAME_tableName" value = List(子哈希表)
     * @param retObj 在止before函数返回一个值，供在after函数的中调用
     * @return Result 返回结果
     */
    public Result onBeforUpdate(Connection conn, String tableName, Map allTables, HashMap values,Obj retObj) {
        return new Result();
    }
    /**
     * 在修改之后要做的数据库动作可重载此方法
     * @param conn Connection 数据库连接,用户不能在接口中自己产生连接
     * @param tableName String 要修改的自定义功能的表名
     * @param allTables Map 所有表信息，key = tableName value = DBTableInfoBean
     * @param values HashMap 修改的值表，key = fieldName value = 值， 如果是子表 key = "TABLENAME_tableName" value = List(子哈希表)
     * @param beforeObj Obj 在before函数中产生的对应，在此引用。可接收在操作执行前的对象
     * @return Result 返回结果
     */
    public Result onAfterUpdate(Connection conn, String tableName, Map allTables, HashMap values,final Obj beforeObj) {
        return new Result();
    }
    /**
     * 在删除之前要做的数据库动作可重载此方法
     * @param conn Connection 数据库连接,用户不能在接口中自己产生连接
     * @param tableName String 要删除的自定义功能的表名
     * @param allTables Hashtable 所有表信息，key = tableName value = DBTableInfoBean
     * @param id String[] 要删除的记录主键数组
     * @param retObj 在止before函数返回一个值，供在after函数的中调用
     * @return Result 返回结果
     */
    public Result onBeforDelete(Connection conn, final String tableName, final Hashtable allTables,
                             final String id[],Obj retObj) {
        return new Result();
    }
    /**
     * 在删除之后要做的数据库动作可重载此方法
     * @param conn Connection 数据库连接,用户不能在接口中自己产生连接
     * @param tableName String 要删除的自定义功能的表名
     * @param allTables Hashtable 所有表信息，key = tableName value = DBTableInfoBean
     * @param id String[] 要删除的记录主键数组
     * @param beforeObj Obj 在before函数中产生的对应，在此引用。可接收在操作执行前的对象
     * @return Result 返回结果
     */
    public Result onAfterDelete(Connection conn, final String tableName, final Hashtable allTables,
                             final String id[],final Obj beforeObj) {
        return new Result();
    }

}
