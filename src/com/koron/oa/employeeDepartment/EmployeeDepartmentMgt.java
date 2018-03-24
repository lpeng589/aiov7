package com.koron.oa.employeeDepartment;

import java.sql.*;
import java.util.*;
import java.util.Date;

import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.CustomizePYM;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.msgcenter.msgif.DepartmentItem;
import com.menyi.msgcenter.msgif.EmployeeItem;
import com.menyi.msgcenter.msgif.MsgHeader;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.*;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;


public class EmployeeDepartmentMgt extends AIODBManager{
	


	/**
	 * 查询所有部门
	 * @return
	 */
	public Result loadDepartment(final String status){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "select id,classCode,deptFullName,isCatalog,deptCode,statusId from tblDepartment where isnull(isPublic,0) != 1 and classCode is not null ";
						sql += " and statusId="+status+" order by deptCode";
						try {
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ResultSet rs = ps.executeQuery();
							ArrayList list=new ArrayList();
							while(rs.next()){
								HashMap map=new HashMap();
	                        	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
	                        		Object obj=rs.getObject(i);
	                        		if(obj==null){
	                        			map.put(rs.getMetaData().getColumnName(i), "");
	                        		}else{
	                        			map.put(rs.getMetaData().getColumnName(i), obj);
	                        		}
	                        	}
	                        	list.add(map);
							}
							rst.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + sql, ex);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst ;
	}
	
	/**
	 * 查询职员
	 * @param employeeType		职员类别（在职员工，离职员工）
	 * @param keyword			关键字搜索
	 * @param departmentCode	部门编号
	 * @param pageNo			第几页
	 * @param pageSize			一页多少行记录
	 * @return
	 */
	public Result loadEmployeeData(final String employeeType,final String keyword,final String departmentCode,final String local,
			final int pageNo, final int pageSize){
		
		StringBuffer sql = new StringBuffer("select tblEmployee.id,tblEmployee.empNumber,tblEmployee.empFullName,tblEmployee.openFlag,tblEmployee.statusId,l."+local+" as titleName,tblEmployee.sysName,");
		sql.append("tblEmployee.Email,tblEmployee.EmployDate,tblEmployee.LeaveDate,tblEmployee.FamilyAddress,tblEmployee.QQ,tblEmployee.EmpEnglishName,tblEmployee.Remark,tblEmployee.Tel,");
		sql.append("tblEmployee.directBoss,tblDepartment.deptFullName,tblEmployee.mobile,tblEmployee.photo,tblEmployee.bornDate,row_number() over(order by tblEmployee.statusId desc,tblEmployee.EmpNumber) as row_id ");
		sql.append("from tblEmployee left join tblDepartment on tblEmployee.departmentCode=tblDepartment.classCode ");
		sql.append("left join (select enumitem.enumValue,enumitem.languageId from tblDBEnumerationItem enumitem,tblDBEnumeration enum where enumitem.enumId=enum.id and enum.enumName='duty') as ss ");
		sql.append("on ss.enumValue=tblEmployee.titleId left join tblLanguage as l on l.id=ss.languageId  ");
		sql.append("where isnull(tblEmployee.ispublic,0) <>1 ");
		
		/* 员工类型（在职，离职） */
		if(employeeType != null && "leave".equals(employeeType)){
			//离职员工
			sql.append(" and tblEmployee.statusId=-1");
		}else{
			//默认现在在职员工
			sql.append(" and tblEmployee.statusId=0");			
		}
		
		/* 关键字搜索 */
		if(keyword != null && !"".equals(keyword)){
			sql.append(" and (tblEmployee.empNumber like '%"+keyword+"%' or tblEmployee.empFullName like '%"+keyword+"%'");
			sql.append(" or tblEmployee.empFullNamePYM like '%"+keyword+"%' or tblDepartment.deptFullName like '%"+keyword+"%'");
			sql.append(" or tblEmployee.Email like '%"+keyword+"%' or tblEmployee.Mobile like '%"+keyword+"%'");
			sql.append(" or tblEmployee.FamilyAddress like '%"+keyword+"'");
			sql.append(")");
		}
		
		/* 部门进行过滤 */
		if(departmentCode != null && !"".equals(departmentCode)){
			sql.append(" and tblEmployee.departmentCode like '"+departmentCode+"%'");
		}
		List paramList = new ArrayList();
		Result rs = sqlListMaps(sql.toString(), paramList, pageNo, pageSize);
		Object[] obj = new Object[]{sql.toString(),rs.retVal};
		rs.setRetVal(obj);
		return rs;
	}
	
	/**
	 * 添加或者修改职员
	 * @param form
	 * @return
	 */
	public Result dealEmployeeData(final EmployeeForm form,final LoginBean lg, final MessageResources resources,
			final Locale locale, final Hashtable allTables){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String id = form.getId();
							StringBuffer sql = new StringBuffer("");
							String str = "EmpNumber,DepartmentCode,EmpFullName,Gender,TitleID,EmpEnglishName,";
							str += "DirectBoss,EmployDate,Mobile,QQ,Tel,Email,FamilyAddress,BornDate,LeaveDate,Remark,";
							str += "StockCode,CompanyCode,";
							if(BaseEnv.version == 12 || BaseEnv.version == 8){
								str += "Account";
							}else{
								str += "payAccCode";
							}
							str += ",Define1,Define2,Define3,Define4,Define5,mailSize,lastUpdateBy,lastUpdateTime,empFullNamePYM,responsibility,SCompanyID,id";
							if(id == null || "".equals(id)){
								//当Id不存在时添加数据
								sql.append("insert into tblEmployee(");
								sql.append(str+",createBy,createTime,workFlowNodeName) values(");
								for(int i=0;i<str.split(",").length;i++){
									sql.append("?");
									if(i != str.split(",").length-1){
										sql.append(",");
									}
								}
								sql.append(",'"+lg.getId()+"','"+BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)+"','finish'");
								sql.append(")");
								form.setId(IDGenerater.getId());
							}else{
								//修改数据
								sql.append("update tblEmployee set ");
								for(int i=0;i<str.split(",").length-1;i++){
									sql.append(str.split(",")[i]+"=?");
									if(i != str.split(",").length-2){
										sql.append(",");
									}
								}
								sql.append(" where id=?");
							}
							
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ps.setString(1, form.getEmpNumber());
							ps.setString(2, form.getDepartmentCode());
							ps.setString(3, form.getEmpFullName());
							ps.setString(4, form.getGender());
							ps.setString(5, form.getTitleID());
							ps.setString(6, form.getEmpEnglishName());
							ps.setString(7, form.getDirectBoss());
							ps.setString(8, form.getEmployDate());
							ps.setString(9, form.getMobile());
							ps.setString(10, form.getQq());
							ps.setString(11, form.getTel());
							ps.setString(12, form.getEmail());
							ps.setString(13, form.getFamilyAddress());
							ps.setString(14, form.getBornDate());
							ps.setString(15, form.getLeaveDate());
							ps.setString(16, form.getRemark());
							ps.setString(17, form.getStockCode());
							ps.setString(18, form.getCompanyCode());
							ps.setString(19, form.getAccount());
							ps.setString(20, form.getDefine1());
							ps.setString(21, form.getDefine2());
							ps.setString(22, form.getDefine3());
							ps.setString(23, form.getDefine4());
							ps.setString(24, form.getDefine5());
							ps.setInt(25, form.getMailSize());
							ps.setString(26, lg.getId());
							ps.setString(27, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
							ps.setString(28, CustomizePYM.getFirstLetter(form.getEmpFullName()));
							ps.setString(29, form.getResponsibility());
							ps.setString(30, lg.getSunCmpClassCode());
							ps.setString(31, form.getId());
							int count = ps.executeUpdate();
							rst.setRetVal(count);
							
							String defineType = "update";
							if(id == null || "".equals(id)){
								sql = new StringBuffer("update tblEmployee set id=orderid where id='"+form.getId()+"'");
								ps = conn.prepareStatement(sql.toString());
								ps.executeUpdate();
								defineType = "add";
							}
							//添加数据到map中为
							HashMap<String,String> hashmap = new HashMap<String,String>();
							hashmap.put("tblEmployee_EmpNumber", form.getEmpNumber());
							hashmap.put("tblEmployee_id", form.getId());
							
							//职员添加执行define
							Result rs = new DynDBManager().defineSql(conn, defineType, true, "tblEmployee",(Hashtable) allTables, hashmap, form.getId(), lg.getId(),new String(),resources,locale);
				            if (rs.getRetCode() < ErrorCanst.DEFAULT_SUCCESS) {
				            	rst.setRetCode(rs.getRetCode());
				            	rst.setRetVal(rs.getRetVal());
				                BaseEnv.log.error("EmployeeDepartment After tblEmployee defineSql Error code = " + rst.getRetCode() + " Msg=" + rst.getRetVal());
				                return;
				            }
				            
							
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		/* 更新数据缓存 */
        MSGConnectCenter.refreshEmpInfo(new String[]{form.getId()});
		return rst ;
	}
	

	
	
	/**
	 * 设置用户状态（-1离职，0在职）
	 * @param id
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public Result setStatus(final String id, final String status) throws Exception{
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String time = "";
							if("-1".equals(status)){
								time = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
							}
							StringBuffer sql = new StringBuffer("update tblEmployee set statusId='"+status+"',leaveDate='"+time+"' where id='"+id+"'");
							Statement st = conn.createStatement();
							int count = st.executeUpdate(sql.toString());
							rst.setRetVal(count);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		if("-1".equals(status)){
			MSGConnectCenter.deleteObj(new String[]{id},"employee");
		}else{			
			MSGConnectCenter.refreshEmpInfo(new String[]{id});
		}
		
		return rst ;
	}
	
	/**
	 * 启用停用部门
	 * @param id
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public Result setDeptStatus(final String classCode, final String status) throws Exception{
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String time = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
							//检查下级部门是否有全停用.
							if(status.equals("-1")){
								String sql  =  " select count(0) from tblDepartment where statusId = 0  and classCode <> '"+classCode+"' and classCode like '"+classCode+"%' ";
								Statement st = conn.createStatement();
								ResultSet rset = st.executeQuery(sql);
								if(rset.next()){
									int count =rset.getInt(1);
									if(count > 0){
										rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
										rst.setRetVal("有启用状态的子级部门，请先停用所有子级部门");
										return;
									}
									
								}
							}
							
							StringBuffer sql = new StringBuffer("update tblDepartment set statusId='"+status+"',lastUpdateTime='"+time+"' where classCode='"+classCode+"'");
							Statement st = conn.createStatement();
							int count = st.executeUpdate(sql.toString());
							rst.setRetVal(count);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		MSGConnectCenter.refreshDeptInfo(classCode);
		return rst ;
	}
	
	/**
	 * 处理部门
	 * @param type
	 * @param deptCode
	 * @param deptName
	 * @param topClassCode
	 * @return
	 * @throws Exception
	 */
	public Result dealDepartment(final String type,final String deptCode,final String deptName,
			final String oldClassCode,final LoginBean lg) throws Exception{
		final String classCode = oldClassCode;
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("");
							PreparedStatement ps = null;
							String time = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
							if("add".equals(type)){
								//当Id不存在时添加数据
								sql.append("insert into tblDepartment(deptCode,deptFullName,classCode,id,createBy,SCompanyId,workFlowNodeName,lastUpdateBy,createTime,lastUpdateTime,DeptFullNamePYM) values(?,?,?,?,?,?,?,?,?,?,?)");
								ps = conn.prepareStatement(sql.toString());
								ps.setString(1, deptCode);
								ps.setString(2, deptName);
								ps.setString(3, classCode);
								ps.setString(4, IDGenerater.getId());
								ps.setString(5, lg.getId());
								ps.setString(6, "00001");
								ps.setString(7, "finish");
								ps.setString(8, lg.getId());
								ps.setString(9, time);
								ps.setString(10, time);
								ps.setString(11, CustomizePYM.getFirstLetter(deptName));
								int count = ps.executeUpdate();
								rst.setRetVal(count);
								sql = new StringBuffer("update tblDepartment set isCatalog=1 where classCode='"+oldClassCode.substring(0,oldClassCode.length()-5)+"'");
								ps = conn.prepareStatement(sql.toString());
								ps.executeUpdate();
							}else{
								//修改数据
								sql.append("update tblDepartment set deptCode=?,deptFullName=?,lastUpdateBy=?,createTime=?,lastUpdateTime=?,deptFullNamePYM=? where classCode=?");
								ps = conn.prepareStatement(sql.toString());
								ps.setString(1, deptCode);
								ps.setString(2, deptName);
								ps.setString(3, lg.getId());
								ps.setString(4, time);
								ps.setString(5, time);
								ps.setString(6, CustomizePYM.getFirstLetter(deptName));
								ps.setString(7, classCode);
								int count = ps.executeUpdate();
								rst.setRetVal(count);
								
								CallableStatement cbs = conn.prepareCall("{call UpdateAccName('DepartmentCode',?,?,?)}");
						        cbs.setString(1, classCode);
						        cbs.registerOutParameter(2, java.sql.Types.INTEGER);
						        cbs.registerOutParameter(3, java.sql.Types.VARCHAR);
						        cbs.execute();
						        if(cbs.getInt(2) !=0){
						        	rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						        	rst.setRetVal(cbs.getString(3));
									return;
						        }
							}
						} catch (Exception ex) {
							BaseEnv.log.error("EmployeeDepartmentMgt.dealDepartment Error",ex);
							rst.setRetVal(ex.getMessage());
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		MSGConnectCenter.refreshDeptInfo(classCode);		
		return rst ;
	}
	
	/**
	 * 根据Id查询职员数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Result queryEmployee(final String id,final String locale) throws Exception{
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("select emp.empNumber,emp.departmentCode,emp.empFullName,");
							sql.append("emp.gender,emp.titleID,emp.empEnglishName,");
							sql.append("emp.directBoss,emp.employDate,emp.mobile,emp.qq,emp.tel,emp.email,emp.familyAddress,emp.photo,emp.mailSize,");
							sql.append("emp.bornDate,emp.leaveDate,emp.remark,emp.responsibility,tblStock.stockFullName as stockCodeName,");
							sql.append("emp.stockCode,tblCompany.comFullName as companyCodeName,emp.companyCode,tblLanguage."+locale+" as accountName,");
							if(BaseEnv.version == 12 || BaseEnv.version == 8){
								sql.append("emp.account");
							}else{
								sql.append("emp.payAccCode as account");
							}
							sql.append(",emp.define1,emp.define2,emp.define3,emp.define4,emp.define5,emp.id,dept.deptFullName as departmentCodeName,e.EmpFullName as directBossName ");
							sql.append(" from tblEmployee emp left join tblDepartment dept on emp.departmentCode=dept.classCode ");
							sql.append(" left join tblStock on tblStock.classCode=emp.stockCode ");
							sql.append(" left join tblCompany on tblCompany.classCode=emp.companyCode");
							sql.append(" left join tblAccTypeInfo on ");
							if(BaseEnv.version == 12 || BaseEnv.version == 8){
								sql.append("tblAccTypeInfo.accNumber=emp.account");
							}else{
								sql.append("tblAccTypeInfo.accNumber=emp.payAccCode");
							}
							sql.append(" left join tblLanguage on tblLanguage.id=tblAccTypeInfo.AccFullName");
							sql.append(" left join tblEmployee e on e.id=emp.DirectBoss ");
							sql.append(" where emp.id='"+id+"'");
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sql.toString());
							HashMap map=new HashMap();
							if(rset.next()){
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			map.put(rset.getMetaData().getColumnName(i), "");
                            		}else{
                            			map.put(rset.getMetaData().getColumnName(i), obj);
                            		}
                            	}
							}
							rst.setRetVal(map);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst ;
	}
	
	
	/**
	 * 查询部门
	 * @param classCode
	 * @return
	 * @throws Exception
	 */
	public Result queryDept(final String classCode) throws Exception{
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("select dept.id,dept.classCode,dept.deptCode,dept.deptFullName");
							sql.append(" from tblDepartment dept where classCode='"+classCode+"'");
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sql.toString());
							HashMap map=new HashMap();
							if(rset.next()){
                            	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                            		Object obj=rset.getObject(i);
                            		if(obj==null){
                            			map.put(rset.getMetaData().getColumnName(i), "");
                            		}else{
                            			map.put(rset.getMetaData().getColumnName(i), obj);
                            		}
                            	}
							}
							rst.setRetVal(map);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst ;
	}
	
	/**
	 * 删除职员
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Result deleteEmployee(final String id) throws Exception{
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("delete from tblEmployee where id='"+id+"'");
							Statement st = conn.createStatement();
							st.executeUpdate(sql.toString());
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		MSGConnectCenter.deleteObj(new String[]{id}, "employee");
		return rst ;
	}
	
	
	/**
	 * 查询部门下的职员
	 * @param classCode
	 * @return
	 * @throws Exception
	 */
	public Result queryDeptIsEmp(final String classCode, final String type) throws Exception{
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("select count(0) as count from tblEmployee where 1=1 ");
							if("exact".equals(type)){
								sql.append(" and departmentCode='"+classCode+"'");
							}else{
								sql.append(" and departmentCode like '"+classCode+"%'");
							}
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sql.toString());
							int count =0;
							if(rset.next()){
								count = rset.getInt("count");
							}
							rst.setRetVal(count);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst ;
	}
	
	/**
	 * 查询部门的下级部门
	 * @param classCode
	 * @return
	 * @throws Exception
	 */
	public Result queryIsNextDept(final String classCode) throws Exception{
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("select count(0) as count from tblDepartment where ");
							sql.append(" classCode like '"+classCode+"%' and classCode != '"+classCode+"'");
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sql.toString());
							int count =0;
							if(rset.next()){
								count = rset.getInt("count");
							}
							rst.setRetVal(count);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst ;
	}
	
	
	/**
	 * 删除部门
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Result deleteDept(final String classCode) throws Exception{
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("delete from tblDepartment where classCode like '"+classCode+"%'");
							Statement st = conn.createStatement();
							st.executeUpdate(sql.toString());
							//修改isCatalog
							sql = new StringBuffer("update tblDepartment set isCatalog=0 where classCode in (select classCode from tblDepartment as dept where (select COUNT(0) from tblDepartment where classCode like dept.classCode+'%' and dept.classCode!=classCode)=0)");
							st.executeUpdate(sql.toString());
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		TreeMap<String, DepartmentItem> deptMap = MSGConnectCenter.departmentMap2;
		DepartmentItem dept = deptMap.get(classCode);
		MSGConnectCenter.deleteObj(new String[]{dept.deptId}, "dept");
		return rst ;
	}
	
	
	/**
	 * 查询部门Id或者职员Id
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Result queryIds(final String tableName) throws Exception{
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("select id from "+tableName);
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sql.toString());
							String[] str = null;
							List list = new ArrayList();
							while(rset.next()){
								list.add(rset.getString("id"));
							}
							if(list.size()>0){
								str = new String[list.size()];
							}
							for(int i=0;i<list.size();i++){
								str[i] = String.valueOf(list.get(i));
							}
							rst.setRetVal(str);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst ;
	}
	
	
	/**
	 * 处理职员和部门是否可以修改   执行存储过程（主要是ERP中如果有引用部门和职员就不让修改编号和名称）
	 * @param fieldName
	 * @param identitys
	 * @param local
	 * @return
	 */
	public Result isDealDeptOrEmployee(final String fieldName,final String identitys,final String local){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							/* 执行存储过程 */
							StringBuffer procstr = new StringBuffer("{call proc_BaseInfoUse(@fieldName=?,@code=?,@local=?,@retCode=?,@retVal=?)}");
							CallableStatement cs = conn.prepareCall(procstr.toString()) ;
							cs.setString(1, fieldName);
							cs.setString(2, identitys);
							cs.setString(3, local);
							cs.registerOutParameter(4, Types.INTEGER);
							cs.registerOutParameter(5, Types.VARCHAR);
							cs.execute();
							rs.setRetCode(cs.getInt(4));
							rs.setRetVal(cs.getString(5));
							if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && rs.getRetCode() != 2601) {  
								BaseEnv.log.debug("EmployeeDepartment isDealDeptOrEmployee Info: " + cs.getString(5));
				            }
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			                rs.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
}
