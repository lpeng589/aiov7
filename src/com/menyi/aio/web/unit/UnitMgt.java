package com.menyi.aio.web.unit;

import java.util.*;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.UnitBean;
import com.menyi.web.util.ErrorCanst;

/**
 * <p>Title: </p>
 *
 * <p>Description: 单位管理的接口类</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class UnitMgt extends DBManager  {

    /**
     * 填加一条单位记录
     * @param id long
     * @param name String
     * @return Result
     */
    public Result add(long id,
                      String unitName ,
                      String remark ,
                      Integer createBy
                        ) {
        UnitBean bean = new UnitBean();
        bean.setId(new Long(id));
        bean.setUnitName(unitName);
        bean.setRemark(remark);
        //调用基类方法addBean执行插入操作
        return addBean(bean);
    }

    /**
     * 修改一条单位记录
     * @param id long
     * @param name String
     * @return Result
     */
    public Result update(long id,
                         String unitName,
                         String remark,
                         Integer lstUpdateBy ) {
        //先查出相应的单位记录
        Result rs = loadBean(new Long(id), UnitBean.class);
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            UnitBean bean = (UnitBean) rs.retVal;
            bean.setUnitName(unitName);
            bean.setRemark(remark);
              //修改记录
            rs = updateBean(bean);
        }
        return rs;
    }

    /**
     * 删除多条单位记录
     * @param ids long[]
     * @return Result
     */
    public Result delete(long[] ids) {
        return deleteBean(ids, UnitBean.class, "id");
    }

    /**
     * 查询单位记录
     * @param name String
     * @param pageNo int
     * @param pageSize int
     * @return Result
     */
    public Result query(String name, int pageNo, int pageSize) {

        //创建参数
        List param = new ArrayList();
        String hql = "select bean.id,bean.unitName ,bean.remark from UnitBean as bean ";
        //如标题不为空，则做标题模糊查询
        if (name != null && name.length() != 0) {
            hql += " where upper(bean.unitName) like '%'||?||'%' ";
            param.add(name.trim().toUpperCase());
        }
        //调用list返回结果
        return list(hql, param, pageNo, pageSize,true);
    }

    /**
     * 查一条单位的详细信息
     * @param notepadId long 代号
     * @return Result 返回结果
     */
    public Result detail(long id) {
        return loadBean(new Long(id), UnitBean.class);
    }
}
