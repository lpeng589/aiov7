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
 * <p>Company: ������</p>
 *
 * @author ������
 * @version 1.0
 */
public class SystemSetIfImpl extends BaseCustomFunction {


    /**
     * �޸�ǰҪ�������ModuleOperaton ��Ӧ��roleModule���󣬱�����Obj�У����޸ĺ�Ҫ���¸����н�ɫ���µ�ModuleOperationֵ
     * @param conn Connection ���ݿ�����,�û������ڽӿ����Լ���������
     * @param tableName String Ҫ�޸ĵ��Զ��幦�ܵı���
     * @param allTables Map ���б���Ϣ��key = tableName value = DBTableInfoBean
     * @param values HashMap �޸ĵ�ֵ��key = fieldName value = ֵ�� ������ӱ� key = "TABLENAME_tableName" value = List(�ӹ�ϣ��)
     * @param beforeObj Obj ��before�����в����Ķ�Ӧ���ڴ����á��ɽ����ڲ���ִ��ǰ�Ķ���
     * @return Result ���ؽ��
     */
    public Result onAfterUpdate(Connection conn, String tableName, Map allTables, HashMap values, final Obj beforeObj) {
    	Result rs=new Result();
    	
    	DDLOperation.updateRefreshTime("systemSet", conn);
    	//    	��������
    	//SystemState.instance.dogState = SystemState.DOG_RESTART;

        return rs;
    }

    /**
     * ��ɾ��֮ǰɾ��RoleModule����
     * @param conn Connection ���ݿ�����,�û������ڽӿ����Լ���������
     * @param tableName String Ҫɾ�����Զ��幦�ܵı���
     * @param allTables Hashtable ���б���Ϣ��key = tableName value = DBTableInfoBean
     * @param id String[] Ҫɾ���ļ�¼��������
     * @param beforeObj Obj ��before�����в����Ķ�Ӧ���ڴ����á��ɽ����ڲ���ִ��ǰ�Ķ���
     * @return Result ���ؽ��
     */
    public Result onAfterDelete(Connection conn, final String tableName, final Hashtable allTables,
                                final String id[], final Obj beforeObj) {
        Result rs = new Result();

        DDLOperation.updateRefreshTime("systemSet", conn);
//    	��������
    	//SystemState.instance.dogState = SystemState.DOG_RESTART;
        return rs;

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
                             final HashMap values, final String id, final Obj beforeObj) {
        DDLOperation.updateRefreshTime("systemSet", conn);
        
//    	��������
    	//SystemState.instance.dogState = SystemState.DOG_RESTART;
    	
        Result rs = new Result();

        return rs;
    }




}
