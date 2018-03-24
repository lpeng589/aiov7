package com.menyi.aio.defineIf;

import com.menyi.web.util.BaseCustomFunction;
import com.dbfactory.Result;
import com.menyi.web.util.Obj;
import java.util.Map;
import java.sql.Connection;
import java.util.HashMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.InitMenData;

import java.util.List;
import java.util.Hashtable;

import com.menyi.aio.bean.SystemSettingBean;
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
public class SystemSetIfImpl extends BaseCustomFunction {


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
    	Result rs=new Result();
    	
    	DDLOperation.updateRefreshTime("systemSet", conn);
    	//    	重启服务
    	//SystemState.instance.dogState = SystemState.DOG_RESTART;

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
    public Result onAfterDelete(Connection conn, final String tableName, final Hashtable allTables,
                                final String id[], final Obj beforeObj) {
        Result rs = new Result();

        DDLOperation.updateRefreshTime("systemSet", conn);
//    	重启服务
    	//SystemState.instance.dogState = SystemState.DOG_RESTART;
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
        DDLOperation.updateRefreshTime("systemSet", conn);
        
//    	重启服务
    	//SystemState.instance.dogState = SystemState.DOG_RESTART;
    	
        Result rs = new Result();

        return rs;
    }




}
