package com.menyi.aio.web.bom;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.BomBean;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.importData.BSFExec;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.DefineSQLBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
/**
 * 
 * <p>Title:BOM单数据库操作类</p> 
 * <p>Description: </p>
 *
 * @Date:2012-5-15
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class BomMgt extends AIODBManager {

	public Result convert(final String goodsCode){
		Result rs = new Result();
		rs.retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                    	ArrayList<Integer> count = new ArrayList<Integer>();
        				count.add(1);
                    	recConvert(connection, goodsCode,count);
                    }
                });
                return ErrorCanst.DEFAULT_SUCCESS;
            }
        });
		return rs;
	}
	private void recConvert(Connection conn,String goodsCode,ArrayList<Integer> count) throws SQLException{
		if(count.get(0) > 1000){
			return;
		}
		count.add(count.get(0)+1);
		count.remove(0);
		String sql  = " Update PDBom set moduleType='product' where goodsCode= ?  ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, goodsCode);
		pstmt.execute();
		sql = "select pdBomDet.GoodsCode  from pdBomDet join pdBom on pdBomDet.f_ref=pdBom.id  where pdBom.goodsCode= ?  ";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, goodsCode);
		ResultSet rset = pstmt.executeQuery();
		while(rset.next()){
			String cgd = rset.getString(1);
			recConvert(conn,cgd,count);
		}
	}
	
	public Result getReplace(String detBomId){
		HashMap map = new HashMap();
		//找到弹出窗要反回的字段
		String sql = " select PDBom.GoodsCode pGoodsCode,PDBomDet.GoodsCode, PDGoodsReplace.id from PDBomDet join PDBom on PDBomDet.f_ref=PDBom.id left join  PDGoodsReplace  on PDGoodsReplace.GoodsCode=PDBomDet.GoodsCode "
				+ " and PDGoodsReplace.PGoodsCode=pdbom.GoodsCode where PDBomDet.id=?  ";
		ArrayList param = new ArrayList();
		param.add(detBomId);
		return sqlListMap(sql, param);
	}
	
	public HashMap getChild(String[] goods){
		HashMap map = new HashMap();
		//找到弹出窗要反回的字段
		DBFieldInfoBean fBean = GlobalsTool.getFieldBean("PDBomDet", "GoodsCode");
		String popFName = "";
		for(int i=1;i<fBean.getSelectBean().getReturnFields().size();i++){
			PopField pf =fBean.getSelectBean().getReturnFields().get(i);
			popFName +=pf.getAsName()+",";
		}
		for(String good :goods){
			if(good.length() > 0){
				ArrayList list= new ArrayList();
				ArrayList<Integer> count = new ArrayList<Integer>();
				count.add(1);
				recGetChild(good,list,popFName,count);
				map.put(good, list);
			}
		}
		return map;
	}
	private void recGetChild(String good,ArrayList list,String popFName,ArrayList<Integer> count){
		if(count.get(0) > 1000){
			return;
		}
		count.add(count.get(0)+1);
		count.remove(0);
		String sql  = " select PDBomDet.GoodsCode,"+popFName+"  PDBomDet.qty,PDBomDet.lossRate,PDBomDet.canSuper,PDBomDet.remark,PDBomDet.id bomDetId "
				+ "from PDBomDet join pdBom on pdBomDet.f_ref=pdBom.id join tblGoods on PDBomDet.GoodsCode=tblGoods.classCode  "
				+ " join tblGoods mg on PDBom.GoodsCode=mg.classCode "
				+ "where PDBom.GoodsCode=? and isLast=1 and mg.attrType not in ('1') ";
		ArrayList param = new ArrayList();
		param.add(good);
		BaseEnv.log.debug("BomMgt.recGetChild 取子bom sql="+sql);
		Result rs = sqlListMap(sql, param);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			if(rs.retVal != null && rs.retVal instanceof ArrayList){
				for(HashMap map :(ArrayList<HashMap>)rs.retVal){
					list.add(map);
					ArrayList clist = new ArrayList();
					map.put("childs", clist);
					recGetChild(map.get("GoodsCode")+"",clist,popFName,count);
				}
			}
		}
	}
	
	
	
}
