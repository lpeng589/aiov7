package com.menyi.aio.web.userFunction;

import com.dbfactory.hibernate.DBManager;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import java.sql.Statement;
import java.sql.SQLException;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.bean.KRLanguage;

import org.hibernate.Session;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.aio.bean.GoodsPropEnumItemBean;
import com.menyi.web.util.BaseEnv;
import com.dbfactory.hibernate.IfDB;
import java.sql.Connection;
import org.hibernate.jdbc.Work;
import java.sql.ResultSet;

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
public class PropMgt extends DBManager {
    /**
     * 根据字段名查询属性表
     * @param name String
     * @return Result
     */
    public Result query(String name,Session session,Connection conn) {
     //创建参数
     List param = new ArrayList();
     String hql = "select bean from GoodsPropInfoBean as bean  where bean.propName=?";
     param.add(name.trim());
     //调用list返回结果
      Result rs = list(hql, param,session);
     if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList) rs.getRetVal()).size() > 0) {
                     try {
                         KRLanguageQuery query = new KRLanguageQuery();
                         for (int i = 0; i < ((ArrayList) rs.getRetVal()).size(); i++) {
                             GoodsPropInfoBean gib = (GoodsPropInfoBean) ((ArrayList) rs.getRetVal()).get(i);
                             query.addLanguageId(gib.getLanguageId());
                             for (int j = 0; j < gib.getEnumItem().size(); j++) {
                                 GoodsPropEnumItemBean geb = (GoodsPropEnumItemBean) gib.getEnumItem().get(j);
                                 query.addLanguageId(geb.getLanguageId());
                             }
                         }
                         HashMap map = query.query(conn);
                         for (int i = 0; i < ((ArrayList) rs.getRetVal()).size(); i++) {
                             GoodsPropInfoBean gib = (GoodsPropInfoBean) ((ArrayList) rs.getRetVal()).get(i);
                             gib.setDisplay((KRLanguage) map.get(gib.getLanguageId()));
                             for (int j = 0; j < gib.getEnumItem().size(); j++) {
                                 GoodsPropEnumItemBean geb = (GoodsPropEnumItemBean) gib.getEnumItem().get(j);
                                 geb.setDisplay((KRLanguage) map.get(geb.getLanguageId()));
                             }
                         }
                     } catch (Exception ex) {
                         ex.printStackTrace();
                         rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                     }
 }
     return rs;
 }
 public Result queryProp(String name) {
    //创建参数
    List param = new ArrayList();
    String hql = "select bean from GoodsPropInfoBean as bean  where bean.propName=? or bean.propName+'Name'=?";
    param.add(name.trim());
    param.add(name.trim());
    //调用list返回结果
    final Result rs = list(hql, param);
    if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList) rs.getRetVal()).size() > 0) {
        int retVal = DBUtil.execute(new IfDB() {
            public int exec(final Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        try {
                            KRLanguageQuery query = new KRLanguageQuery();
                            for (int i = 0; i < ((ArrayList) rs.getRetVal()).size(); i++) {
                                GoodsPropInfoBean gib = (GoodsPropInfoBean) ((ArrayList) rs.getRetVal()).get(i);
                                query.addLanguageId(gib.getLanguageId());
                                for (int j = 0; j < gib.getEnumItem().size(); j++) {
                                    GoodsPropEnumItemBean geb = (GoodsPropEnumItemBean) gib.getEnumItem().get(j);
                                    query.addLanguageId(geb.getLanguageId());
                                }
                            }
                            HashMap map = query.query(connection);
                            for (int i = 0; i < ((ArrayList) rs.getRetVal()).size(); i++) {
                                GoodsPropInfoBean gib = (GoodsPropInfoBean) ((ArrayList) rs.getRetVal()).get(i);
                                gib.setDisplay((KRLanguage) map.get(gib.getLanguageId()));
                                for (int j = 0; j < gib.getEnumItem().size(); j++) {
                                    GoodsPropEnumItemBean geb = (GoodsPropEnumItemBean) gib.getEnumItem().get(j);
                                    geb.setDisplay((KRLanguage) map.get(geb.getLanguageId()));
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retVal);
    }
    return rs;
}
}
