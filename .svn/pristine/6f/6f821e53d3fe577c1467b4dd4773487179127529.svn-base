package com.menyi.aio.defineIf;

import com.menyi.web.util.BaseCustomFunction;
import com.dbfactory.Result;
import com.menyi.web.util.Obj;
import java.util.Map;
import java.sql.Connection;
import java.util.HashMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.*;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import java.util.List;
import java.util.Hashtable;
import com.menyi.aio.dyndb.DDLOperation;
import com.dbfactory.hibernate.DBUtil;
import org.hibernate.Session;
import com.dbfactory.hibernate.IfDB;
import org.hibernate.jdbc.Work;
import com.menyi.web.util.SecurityLock;
import com.menyi.web.util.SystemState;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class ModuleIfImpl extends BaseCustomFunction {
    /**
     * 修改前要查出所有ModuleOperaton 对应的roleModule对象，保存在Obj中，在修改后，要重新给所有角色赋新的ModuleOperation值
     * @param conn Connection 数据库连接,用户不能在接口中自己产生连接
     * @param tableName String 要修改的自定义功能的表名
     * @param allTables Map 所有表信息，key = tableName value = DBTableInfoBean
     * @param values HashMap 修改的值表，key = fieldName value = 值， 如果是子表 key = "TABLENAME_tableName" value = List(子哈希表)
     * @param retObj 在止before函数返回一个值，供在after函数的中调用
     * @return Result 返回结果
     */
    public Result onBeforUpdate(Connection conn, String tableName, Map allTables, HashMap values, Obj retObj) {
        Result rs = new Result();
        return rs;
    }

    /**
     * 修改前要查出所有ModuleOperaton 对应的roleModule对象，保存在Obj中，在修改后，要重新给所有角色赋新的ModuleOperation值
     * @param conn Connection 数据库连接,用户不能在接口中自己产生连接
     * @param tableName String 要修改的自定义功能的表名
     * @param allTables Map 所有表信息，key = tableName value = DBTableInfoBean
     * @param values HashMap 修改的值表，key = fieldName value = 值， 如果是子表 key = "TABLENAME_tableName" value = List(子哈希表)
     * @param beforeObj Obj 在before函数中产生的对应，在此引用。可接收在操作执行前的对象
     * @return Result 返回结果
     */
    public Result onAfterUpdate(Connection conn, String tableName, Map allTables, HashMap values, final Obj beforeObj) {
        Result rs = new Result();
        /**
         * 根据修改的moduleOperation的代号，和修改之前反回的值修改RoleModule
         */
        DDLOperation.updateRefreshTime("ModuleList", conn);
        return rs;
    }

    /**
     * 在删除之前删除RoleModule数据
     * @param conn Connection 数据库连接,用户不能在接口中自己产生连接
     * @param tableName String 要删除的自定义功能的表名
     * @param allTables Hashtable 所有表信息，key = tableName value = DBTableInfoBean
     * @param id String[] 要删除的记录主键数组
     * @param beforeObj Obj 在before函数中产生的对应，在此引用。可接收在操作执行前的对象
     * @return Result 返回结果
     */
    public Result onBeforDelete(Connection conn, final String tableName, final Hashtable allTables,
                                final String id[], final Obj beforeObj) {
        Result rs = new Result();
        DDLOperation.updateRefreshTime("ModuleList", conn);
        return rs;

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
                             final HashMap values, final String id, final Obj beforeObj) {
        DDLOperation.updateRefreshTime("ModuleList", conn);
        Result rs = new Result();

        return rs;
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
    public Result onBeforeAdd(Connection conn, final String tableName, final Map allTables,
                              final HashMap values, Obj retObj) {
        Result rs = new Result();
        //检查自定义模块是否被非法撰改，检查数据库中模块数是否超过标准,无限制不用比较
        if (!SystemState.instance.funUserDefine) {
            rs.setRetCode(ErrorCanst.RET_FUNCTION_LIMIT_ERROR);
        }
        return rs;
    }

    


}
