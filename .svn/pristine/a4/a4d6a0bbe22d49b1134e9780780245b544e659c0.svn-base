package com.menyi.aio.web.finance.formulaEdit;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.koron.oa.bean.MessageBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.MgtBaseAction;

public class FormulaAction extends MgtBaseAction{

	
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 根据不同操作类型分配给不同函数处理
		String operation = request.getParameter("operation");
		ActionForward forward = null;
		if("save".equals(operation)){
			return saveFormula(mapping,request);
		}  else if("edit".equals(operation)){			
			/*获取公式数据*/
			return editFormula(mapping,request);
			/***end***/			
		} else{
			return editFormula(mapping,request);
		}				
	}
	
	private ActionForward editFormula(ActionMapping mapping,HttpServletRequest request){
		final Map data = new HashMap(); 
		final Result rs = new Result();				
		//在配置文件中查找此单据中弹出框显示语句
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection connection) throws SQLException{
						
						Connection conn = connection;
						String sql = "select a.id,a.rows,a.cols,b.cell,b.fun,b.value from AssetFormula a join AssetFormulaDet b on a.id = b.f_ref";
						try{
							PreparedStatement ps = conn.prepareStatement(sql);
							ResultSet rss = ps.executeQuery();						
							Map cells = new HashMap();
	 						while(rss.next()){
								Map m = new HashMap();
								data.put("id", rss.getString("id"));
								data.put("rows", rss.getString("rows"));
								data.put("cols", rss.getString("cols"));					
								m.put("cell", rss.getString("cell"));
								m.put("fun",rss.getString("fun"));
								m.put("value", rss.getString("value"));
								cells.put(rss.getString("cell"), m);							
							}
	 						data.put("cells", cells);
	 						rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						}catch(Exception e){
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("select formula Error :" , e);
							return;
						}
					}
				});
				return rs.getRetCode();
			}		
		});
		Map msg = new HashMap();
		if(retCode == ErrorCanst.DEFAULT_SUCCESS){
			msg.put("code","OK");
			if(data.get("id") == null){
				request.setAttribute("id", "");
			} else{
				request.setAttribute("id", data.get("id"));
			}
			if(data.get("rows")== null){
				request.setAttribute("rows",1);
			} else{
				request.setAttribute("rows",Integer.parseInt((String)data.get("rows")));
			}
			if(data.get("cols") == null){
				request.setAttribute("cols", 8);
			} else{
				request.setAttribute("cols", Integer.parseInt((String)data.get("cols")));
			}
			
			request.setAttribute("cells", data.get("cells"));
		} else{
			msg.put("code","Error");
		}
		request.setAttribute("data",msg);
		
		return getForward(request, mapping, "FormulaEdit");
	}
	
	private ActionForward saveFormula(ActionMapping mapping,HttpServletRequest request){
		final String cells = request.getParameter("cells");
		final String rows = request.getParameter("rows");
		final String cols = request.getParameter("cols");
		final String id = request.getParameter("id");
		final List<HashMap> list = new Gson().fromJson(cells, List.class);
		final Result rs = new Result();				
		//在配置文件中查找此单据中弹出框显示语句
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						String querysql = "";
						
						try {
							String delSql = "delete from AssetFormulaDet";
							PreparedStatement cs = connection.prepareStatement(delSql);						
							cs.executeUpdate();
							String mainId = id;
							//*****判断若未编辑过公式******//
							if(id == null || "".equals(id)){								
								mainId = "00001";
								String addSql = "insert into AssetFormula(id,rows,cols,createBy) values(?,?,?,?)";
								PreparedStatement as = connection.prepareStatement(addSql);
								as.setString(1, mainId);
								as.setInt(2, Integer.parseInt(rows));
								as.setInt(3, Integer.parseInt(cols));
								as.setString(4, "1");
								as.executeUpdate();
							}
													
							for(HashMap item : list) {
								String sql = " INSERT INTO  AssetFormulaDet(f_ref,cell,value,fun) values(?,?,?,?)";
								
								PreparedStatement is = conn.prepareStatement(sql);
								is.setString(1, mainId);
								is.setString(2, (String)item.get("name"));
								is.setString(3, (String)item.get("value"));	
								is.setString(4, (String)item.get("fun"));
								
								is.execute();
								
							}
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Save formula Error :" , ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});		
		//*****end*****//
		Map msg = new HashMap();
		if(retCode == ErrorCanst.DEFAULT_SUCCESS){
			msg.put("code","OK");
		} else{
			msg.put("code","Error");
		}
		request.setAttribute("data",new Gson().toJson(msg));
		return getForward(request, mapping, "FormulaMsg");
	}

}

