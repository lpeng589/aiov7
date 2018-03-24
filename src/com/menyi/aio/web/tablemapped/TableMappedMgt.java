package com.menyi.aio.web.tablemapped;

import com.dbfactory.hibernate.DBManager;
import com.menyi.aio.bean.*;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.dbfactory.*;
import java.util.*;

/**
 * Description:
 * <br/>Copyright (C), 2008-2012, Justin.T.Wang
 * <br/>This Program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @autor Justin.T.Wang  yao.jun.wang@hotmail.com
 * @version 1.0
 */
public class TableMappedMgt  extends DBManager{

    public TableMappedMgt() {
    }

    public Result add(TableMappedBean tableMapped){
        return addBean(tableMapped);
    }

    public Result query(String name,String targetName, int pageNo, int pageSize) {

        //创建参数
        List param = new ArrayList();

        String hql = "select distinct bean.mostlyTable,bean.childTable from TableMappedBean as bean where 1=1 ";
        //如标题不为空，则做标题模糊查询
        if ((name != null && name.length() != 0)) {

              hql += " and upper(bean.mostlyTable) like '%'||?||'%' ";
              param.add(name.trim().toUpperCase());
        }
        if ((targetName != null && targetName.length() != 0)) {

            hql += " and upper(bean.childTable) like '%'||?||'%' ";
            param.add(targetName.trim().toUpperCase());
      }

        return list(hql, param, pageNo, pageSize, true);
    }
    // 根据Id数组删除Bean
    public Result delete(String[] ids){
        return deleteBean(ids, TableMappedBean.class, "id");
    }
    // 删除一个或多个Bean
    public Result delete(TableMappedBean...tableMappeds){
        String[] ids = new String[tableMappeds.length];
        for(int i = 0 ; i < tableMappeds.length ; i ++){
            ids[i] = tableMappeds[i].getId();
        }
        return deleteBean(ids, TableMappedBean.class, "id");
    }

    public Result update(TableMappedBean tableMapped){
        return updateBean(tableMapped);
    }

    public Result queryMostAndChild(){

        return query("select bean.mostlyTable,bean.childTable from TableMappedBean as bean");
    }

    public Result queryByMostlyTable(String mostlyTableName){

        return query("select bean from TableMappedBean as bean where bean.mostlyTable=?",mostlyTableName);
    }
    public Result queryChildTableName(String mostlyTableName){

        return query("select distinct bean.childTable from TableMappedBean as bean where bean.mostlyTable=?",mostlyTableName);
    }

    public Result queryByChildTable(String childTable){
        return query("select bean from TableMappedBean as bean where bean.childTable=?",childTable);
    }

    public Result queryByMostlyAndChild(String mostlyTable, String childTable){

        return query("select bean from TableMappedBean as bean where bean.mostlyTable=? and bean.childTable=?", mostlyTable, childTable);
    }
    /**
     * 跟据主表和明细表查询有映射关系的明细表
     * @param mostlyTable
     * @param childTable
     * @return
     */
    public String queryChildByMostlyAndChild(String mostlyTable, String childTable){
    	ArrayList<DBTableInfoBean> mtbs = DDLOperation.getChildTables(mostlyTable, BaseEnv.tableInfos);    	
		ArrayList<DBTableInfoBean> ctbs = DDLOperation.getChildTables(childTable, BaseEnv.tableInfos);
		String mchild="";
		for(DBTableInfoBean tb:mtbs){
			mchild += ",'"+tb.getTableName()+"'";
		}
		String cchild="";
		for(DBTableInfoBean tb:ctbs){
			cchild += ",'"+tb.getTableName()+"'";
		}
		if(mchild.length()==0|| cchild.length()==0){
			return "";
		}
		mchild = mchild.substring(1);
		cchild = cchild.substring(1);
        Result rs = query("select bean from TableMappedBean as bean where bean.mostlyTable in ("+mchild+") and bean.childTable in("+cchild+")" );
        if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS &&rs.retVal != null && ((ArrayList<TableMappedBean>)rs.retVal).size() >0){
        	TableMappedBean tmb = ((ArrayList<TableMappedBean>)rs.retVal).get(0);
        	return tmb.getMostlyTable();
        }
        return "";
    }

    private Result query(String hql,Object...params){

        ArrayList param = new ArrayList();
        for(Object obj : params){
            param.add(obj);
        }
        return list(hql,param);
    }
}
