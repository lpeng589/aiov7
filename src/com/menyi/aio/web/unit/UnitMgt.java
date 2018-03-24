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
 * <p>Description: ��λ����Ľӿ���</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: ������</p>
 *
 * @author ������
 * @version 1.0
 */
public class UnitMgt extends DBManager  {

    /**
     * ���һ����λ��¼
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
        //���û��෽��addBeanִ�в������
        return addBean(bean);
    }

    /**
     * �޸�һ����λ��¼
     * @param id long
     * @param name String
     * @return Result
     */
    public Result update(long id,
                         String unitName,
                         String remark,
                         Integer lstUpdateBy ) {
        //�Ȳ����Ӧ�ĵ�λ��¼
        Result rs = loadBean(new Long(id), UnitBean.class);
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            UnitBean bean = (UnitBean) rs.retVal;
            bean.setUnitName(unitName);
            bean.setRemark(remark);
              //�޸ļ�¼
            rs = updateBean(bean);
        }
        return rs;
    }

    /**
     * ɾ��������λ��¼
     * @param ids long[]
     * @return Result
     */
    public Result delete(long[] ids) {
        return deleteBean(ids, UnitBean.class, "id");
    }

    /**
     * ��ѯ��λ��¼
     * @param name String
     * @param pageNo int
     * @param pageSize int
     * @return Result
     */
    public Result query(String name, int pageNo, int pageSize) {

        //��������
        List param = new ArrayList();
        String hql = "select bean.id,bean.unitName ,bean.remark from UnitBean as bean ";
        //����ⲻΪ�գ���������ģ����ѯ
        if (name != null && name.length() != 0) {
            hql += " where upper(bean.unitName) like '%'||?||'%' ";
            param.add(name.trim().toUpperCase());
        }
        //����list���ؽ��
        return list(hql, param, pageNo, pageSize,true);
    }

    /**
     * ��һ����λ����ϸ��Ϣ
     * @param notepadId long ����
     * @return Result ���ؽ��
     */
    public Result detail(long id) {
        return loadBean(new Long(id), UnitBean.class);
    }
}
