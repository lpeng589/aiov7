package com.menyi.aio.web.usermanage;

import java.text.SimpleDateFormat;
import java.util.*;

import com.dbfactory.hibernate.DBManager;
import com.dbfactory.Result;
import com.koron.oa.bean.Employee;
import com.menyi.web.util.ErrorCanst;
import com.menyi.aio.bean.EnumerateBean;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import java.io.Serializable;

import com.dbfactory.DBCanstant;

import org.hibernate.Query;

import com.dbfactory.hibernate.IfDB;
import com.dbfactory.hibernate.DBUtil;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.ModuleOperationBean;
import com.menyi.aio.bean.UserModuleBean;
import com.menyi.aio.bean.ModuleBean;
import com.menyi.aio.bean.EmployeeBean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import com.menyi.web.util.BaseEnv;

import java.sql.PreparedStatement;
import java.sql.Connection;

import org.hibernate.jdbc.Work;

import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.SystemState;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.dyndb.DDLOperation;

/**
 * Description: <br/>Copyright (C), 2008-2012, Justin.T.Wang <br/>This Program
 * is protected by copyright laws. <br/>Program Name: <br/>Date:
 *
 * @autor Justin.T.Wang yao.jun.wang@hotmail.com
 * @version 1.0
 */
public class UserMgt extends AIODBManager {
	
	public Result changeViewMenu(final String userId,final String type,final String value){
		Result rs = new Result();
		rs.retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                    	String sql = "";
                    	if(type.equals("viewTopMenu")){
                    		sql = "update tblemployee set viewTopMenu=? where id=?";
                    	}else{
                    		sql = "update tblemployee set viewLeftMenu=? where id=?";
                    	}                    	
                    	PreparedStatement stmt = connection.prepareStatement(sql);
                    	stmt.setString(1, value);
                    	stmt.setString(2, userId);
                    	stmt.executeUpdate();
                    	
                    }
                });
                return ErrorCanst.DEFAULT_SUCCESS;
            }
        });
		return rs;
    }
	
	public Result firstShowSave(final String userId,final String moduleNo){
		Result rs = new Result();
		rs.retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                    	String sql =  "update tblemployee set firstShow=case isnull(firstShow,'') when '' then ';'+?+';' else firstShow+?+';' end   where id=?";
                    	PreparedStatement stmt = connection.prepareStatement(sql);
                    	stmt.setString(1, moduleNo);
                    	stmt.setString(2, moduleNo);
                    	stmt.setString(3, userId);
                    	stmt.executeUpdate();
                    	
                    }
                });
                return ErrorCanst.DEFAULT_SUCCESS;
            }
        });
		return rs;
    }
	/**
	 * 复制权限
	 */ 
	public Result copyRight(final String sourceId,final ArrayList<String> dirIds){
		Result rs = new Result();
		rs.retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                    	String sql = "";

                    	for(String did:dirIds){
                        	//1、复制权限分组、
                    		sql = "update tblUserSunCompany set roleids=(select roleids from tblUserSunCompany where userid=?) where userid = ? ";
                        	PreparedStatement stmt = connection.prepareStatement(sql);
                        	stmt.setString(1, sourceId);
                        	stmt.setString(2, did);
                        	int count =stmt.executeUpdate();
                        	if(count==0){
                        		//说明记录不存在，插入记录
                        		sql = "insert into tblUserSunCompany(userid,sunCompanyid,roleids) select ?,sunCompanyid,roleidsfrom tblUserSunCompany where userid=?";
                        		stmt = connection.prepareStatement(sql);
                        		stmt.setString(1, did);
                        		stmt.setString(2, sourceId);                            	
                            	count =stmt.executeUpdate();
                        	}
                    	
	                    	//2、复制，隐藏成本价   隐藏销售价	  隐藏客户	  隐藏供应商   隐藏往来单位
	                    	sql = "update tblEmployee set HiddenField=(select HiddenField from tblEmployee where id=?) where id = ? ";
	                    	stmt = connection.prepareStatement(sql);
	                    	stmt.setString(1, sourceId);
                        	stmt.setString(2, did);
                        	count =stmt.executeUpdate();
	                    	
	                    	//3、销售出库允许低于成本价销售   销售出库允许低于限售价销售等等
                        	sql = "delete tblRightType where id = ? ";
                        	stmt = connection.prepareStatement(sql);
	                    	stmt.setString(1, did);
                        	count =stmt.executeUpdate();
                        	sql = "insert into tblRightType(id,rightType,hasRight) select ?,rightType,hasRight from tblRightType where id=?";
                        	stmt = connection.prepareStatement(sql);
	                    	stmt.setString(1, did);
                        	stmt.setString(2, sourceId);                        	
                        	count =stmt.executeUpdate();	                    	
	                    	//4、tblroleModule表
                        	sql = "delete tblroleModule where roleid = ? ";
                        	stmt = connection.prepareStatement(sql);
	                    	stmt.setString(1, did);
                        	count =stmt.executeUpdate();
                        	sql = "insert into tblroleModule(roleid,moduleOpId) select ?,moduleOpId from tblroleModule where roleid=?";
                        	stmt = connection.prepareStatement(sql);
	                    	stmt.setString(1, did);
                        	stmt.setString(2, sourceId);                        	
                        	count =stmt.executeUpdate();
	                    	//5、tblrolescope表和tblrolemodulescope
                        	sql = "delete tblrolemodulescope where scopeId in (select id from tblrolescope where roleId=?) ";
                        	stmt = connection.prepareStatement(sql);
	                    	stmt.setString(1, did);
                        	count =stmt.executeUpdate();
                        	
                        	sql = "delete tblrolescope where roleid = ? ";
                        	stmt = connection.prepareStatement(sql);
	                    	stmt.setString(1, did);
                        	count =stmt.executeUpdate();
                        	
                        	//循环查询所有rolescopde
                        	sql = " select id from tblrolescope where roleid=? ";
                        	stmt = connection.prepareStatement(sql);
	                    	stmt.setString(1, sourceId);
                        	ResultSet rset = stmt.executeQuery();
                        	while(rset.next()){
                        		String sourcescopeid = rset.getString(1);
                        		sql = "insert into tblrolescope(roleid,flag,tableName,fieldName,scopeValue,escopeValue,isAddLevel,isAllModules,rightUpdate,rightDelete,tableNameDisplay,fieldNameDisplay,scopeValueDisplay) " +
                    				" select ?,flag,tableName,fieldName,scopeValue,escopeValue,isAddLevel,isAllModules,rightUpdate,rightDelete,tableNameDisplay,fieldNameDisplay,scopeValueDisplay from tblrolescope where id=?";
                        		stmt = connection.prepareStatement(sql);
    	                    	stmt.setString(1, did);
		                    	stmt.setString(2, sourcescopeid);                        	
		                    	count =stmt.executeUpdate();
		                    	if(count ==0){
		                    		throw new RuntimeException("插入范围权限返回0行更新记录");
		                    	}
		                    	sql = " select @@identity ";
		                    	stmt = connection.prepareStatement(sql);
		                    	ResultSet rfid = stmt.executeQuery();
								String newId = "";
								if (rfid.next()) {
									newId = rfid.getString(1);
								} else {
									throw new RuntimeException("无法获取最后的标识ID值");
								}
		                    	
		                    	
		                    	sql = "insert into tblRoleModuleScope(scopeid,moduleOpId) select ?,moduleOpId from tblRoleModuleScope where scopeid=?";
		                    	stmt = connection.prepareStatement(sql);
		                    	stmt.setString(1, newId);
		                    	stmt.setString(2, sourcescopeid);                        	
		                    	count =stmt.executeUpdate();
                        	}                      	
                    	
                    	}
                    	
                    }
                });
                return ErrorCanst.DEFAULT_SUCCESS;
            }
        });
		return rs;
    }
	
	public Result getEmpGroup(String userId){
    	final Result result = new Result();
    	List paramList = new ArrayList() ;
    	final String sql = " select a.userID,c.empFullName,b.groupName,b.id from tblEmpGroupUser a join tblEmpGroup b on a.f_ref=b.id " +
    			"join tblemployee c on a.userID=c.id  where c.openFlag=1 and b.id in (select f_ref from tblEmpGroupUser where userId=?) order by c.empFullName";
    	paramList.add(userId) ;
    	return sqlList(sql,paramList);
    }
	
	/**
	 * 查询部门
	 * @return
	 */
	public Result queryDept(){
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        String sql = "select deptFullName,classCode from tblDepartment dept where statusId=0 "
                        		+ "and (select count(*) from tblEmployee emp where emp.departmentCode=dept.classCode and openFlag=1 and statusId=0)>0 order by deptFullName";
                        try {
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        	ResultSet rss = pss.executeQuery() ;
                        	ArrayList<String[]> deptList = new ArrayList<String[]>() ;
                        	while(rss.next()){
                        		String[] dept = new String[2] ;
                        		dept[0] = rss.getString("classCode") ;
                        		dept[1] = rss.getString("deptFullName") ;
                        		deptList.add(dept) ;
                        	}
                        	rs.setRetVal(deptList) ;
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error(" UserMgt.queryDept Query data Error :" , ex);
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.retCode = retCode ;
        return rs ;
	}
	
	/**
	 * 查询部门
	 * @return
	 */
	public Result queryDept2(){
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        String sql = " select deptFullName,classCode from tblDepartment dept where isnull(ispublic,0)<> 1 and statusId=0  "+
                        			 " and (select count(*) from tblEmployee emp where emp.departmentCode like dept.classCode+'%' and openFlag=1 and statusId=0)>0"+
                        				" ORDER BY deptCode";
                        try {
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        	ResultSet rss = pss.executeQuery() ;
                        	ArrayList<String[]> deptList = new ArrayList<String[]>() ;
                        	while(rss.next()){
                        		String[] dept = new String[2] ;
                        		dept[0] = rss.getString("classCode") ;
                        		dept[1] = rss.getString("deptFullName") ;
                        		deptList.add(dept) ;
                        	}
                        	rs.setRetVal(deptList) ;
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error(" UserMgt.queryDept Query data Error :" , ex);
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.retCode = retCode ;
        return rs ;
	}
	
	
    /**
     * 周新宇 填加一条系统用户记录
     *
     * @param id
     *            long
     * @param name
     *            String
     * @return Result
     */
    public Result add(EmployeeBean employeebean) {
        // 检查是否超过最大用户数限制
        if (employeebean.getCanJxc()==1 && getDBUserNum(1,employeebean.getId()) >= SystemState.instance.userNum) {
            Result rs = new Result();
            rs.retCode = ErrorCanst.RET_USER_LIMIT_ERROR;
            return rs;
        }
        if (employeebean.getCanOa()==1 && getDBUserNum(2,employeebean.getId()) >= SystemState.instance.oaUserNum) {
            Result rs = new Result();
            rs.retCode = ErrorCanst.RET_OAUSER_LIMIT_ERROR;
            return rs;
        }
        if (employeebean.getCanCrm()==1 && getDBUserNum(3,employeebean.getId()) >= SystemState.instance.crmUserNum) {
            Result rs = new Result();
            rs.retCode = ErrorCanst.RET_CRMUSER_LIMIT_ERROR;
            return rs;
        }
        if (employeebean.getCanHr()==1 && getDBUserNum(4,employeebean.getId()) >= SystemState.instance.hrUserNum) {
            Result rs = new Result();
            rs.retCode = ErrorCanst.RET_HRUSER_LIMIT_ERROR;
            return rs;
        }
        if (employeebean.getCanOrders()==1 && getDBUserNum(10,employeebean.getId()) >= SystemState.instance.ordersUserNum) {
            Result rs = new Result();
            rs.retCode = ErrorCanst.RET_ORDERSUSER_LIMIT_ERROR;
            return rs;
        }
        MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(employeebean.getPassword().trim().getBytes()) ;
			String md5Pwd = toHex(md.digest()) ;
			employeebean.setPassword(md5Pwd);
		} catch (NoSuchAlgorithmException e) {
			BaseEnv.log.error("UserMgt.add Password MD5 Error :",e);
			Result rs = new Result();
            rs.retCode = ErrorCanst.DEFAULT_FAILURE;
            return rs;
		}
		

        DBUtil.execute(new IfDB() {
            public int exec(Session session) {

                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        DDLOperation.updateRefreshTime("employee", connection);
                    }
                });
                return ErrorCanst.DEFAULT_SUCCESS;
            }
        });

        // 调用基类方法addBean执行插入操作
        return updateBean(employeebean);
    }
    
    public static String toHex(byte[] buffer) {
		StringBuffer sb = new StringBuffer(buffer.length);
		String temp;
		for (int i = 0; i < buffer.length; i++) {
			temp = Integer.toHexString(0xFF & buffer[i]);
			if (temp.length() < 2) {
				sb.append("0");
			}
			sb.append(temp.toUpperCase());
		}
		return sb.toString();
	}

    public Result updateUserModule(final String userId, final ArrayList list) {
        Result rs = new Result();
        rs.retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                // 删除所有数据
                String hql = "delete UserModuleBean where employeeBean.id =  :a";
                Query query = session.createQuery(hql);
                query.setParameter("a", userId);
                int r = query.executeUpdate();
                if (r < 0) {
                    return DBCanstant.ER_NO_DATA;
                }

                for (Object o : list) {
                    Result temp = addBean(o, session);
                    if (temp.retCode != ErrorCanst.DEFAULT_SUCCESS) {
                        return temp.retCode;
                    }
                }
                return ErrorCanst.DEFAULT_SUCCESS;
            }
        });
        return rs;
    }

    public Result getLoginPwd(){
    	final Result result = new Result();
    	final String sql = "select [sysName],[password] from tblEmployee where id = '1'";
    	DBUtil.execute(new IfDB(){
    		public int exec(Session session) {
    			session.doWork(new Work(){
    				public void execute(Connection con) throws SQLException {
    					Statement state = con.createStatement();
    					ResultSet rs = state.executeQuery(sql);
    					if(rs.next()){
    						result.setRetVal(new String[]{
    								rs.getString("sysName"),
    								rs.getString("password")
    						});
    					}
    				}
    			});
    			return 0;
    		}
    	});
    	return result;
    }

    /**
     * 递归查找菜单
     * @param type 类型 "0"表示公用模块，其余不明。
     * @param list 用来存当前模块的 {@link ModuleBean}
     * @param mb 父级moduleBean,除了根目录外，其余的list均可以通过{@link ModuleBean#getChildList()}获得
     * @param rs 返回值，用来判断事件是否处理成功
     */
    public static int moduleListCount = 0;
    public void getModuleList(String type, ArrayList list, ModuleBean mb,Result rs) {
    	moduleListCount++;
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            return;
        }
        Result temp = query(
            "select Bean from ModuleBean Bean where  Bean.isDisplay=0 and Bean.isUsed = 1 "
            + (type != null ? " and Bean.mainModule ='" + type
               + "' " : "")
            + " and length(Bean.classCode)=? and Bean.classCode like ?||'%'  order by Bean.orderBy ",
            mb == null ? 5 : mb.getClassCode().length() + 5,
            mb == null ? "" : mb.getClassCode());
        if (temp.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            rs.retCode = temp.retCode;
            return;
        } else {
            final ArrayList rstemp = (ArrayList)temp.getRetVal();
            int retCode = DBUtil.execute(new IfDB() {
                public int exec(Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws SQLException {
                            Connection conn = connection;
                            //查询多语言
                            KRLanguageQuery rlq = new KRLanguageQuery();
                            for (int i = 0; i < rstemp.size(); i++) {
                                ModuleBean tempmb = (ModuleBean) rstemp.get(i);
                                rlq.addLanguageId(tempmb.getModelId());
                            }
                            HashMap tempMap = rlq.query(conn);
                            for (int i = 0; i < rstemp.size(); i++) {
                                ModuleBean tempmb = (ModuleBean) rstemp.get(i);
                                KRLanguage language = (KRLanguage)tempMap.get(tempmb.getModelId()) ;
                                if(language==null){
                                	language = KRLanguageQuery.create(tempmb.getModelId(), tempmb.getModelId(), tempmb.getModelId()) ;
                                }
                                tempmb.setModelDisplay(language);
                            }

                        }
                    });
                    return ErrorCanst.DEFAULT_SUCCESS;
                }
            });
        }

        for (Object o : (ArrayList) temp.retVal) {
            ModuleBean mbtemp = (ModuleBean) o;

            mbtemp.setParentModuleBean(mb);
            ArrayList listtemp = new ArrayList();
            mbtemp.setChildList(listtemp);
            List<ModuleOperationBean> modOpList = mbtemp.getModuleoperationinfo() ;

            //设置admin帐号的权限,admin帐号不受任何限制
            String url = mbtemp.getLinkAddress();
            int lastIndex = 0 ;
            if(url!=null && url.contains("UserFunctionQueryAction.do")){
                if(url.indexOf("parentTableName=")!=-1){
                	lastIndex = url.indexOf("&",url.indexOf("&tableName=")+11) ;
                	if(lastIndex!=-1){
                		url = url.substring(0,lastIndex) ;
                	}
                }else if(url.indexOf("&moduleType=")!=-1){
                	lastIndex = url.indexOf("&", url.indexOf("&moduleType=")+12) ;
                	if(lastIndex!=-1){
                		url = url.substring(0,lastIndex) ;
                	}
                }else{
                	if(url.contains("&")){
                		url = url.substring(0,url.indexOf("&",url.indexOf("?tableName"))) ;
                	}
                }
            }
            MOperation mop = new MOperation();
            mop.moduleUrl = url;
            mop.moduleId = mbtemp.getId().trim();
            mop.isMobile= mbtemp.getIsMobile()==null?2:mbtemp.getIsMobile();
            mop.icon = mbtemp.getIcon();
            
            if(modOpList!=null){
                for(int j=0;j<modOpList.size();j++){
                	ModuleOperationBean opBean = modOpList.get(j) ;
                	mop.setOperation(opBean.getOperationID());
                }
            }
            BaseEnv.adminOperationMap.put(url, mop);
            BaseEnv.adminOoperationMapKeyId.put(mop.moduleId, mop); //用于弹出窗的鉴权

            list.add(mbtemp);
            getModuleList(null, listtemp, mbtemp, rs);
        }
    }
    
    /**
     * 查找系统模块
     * 模块的列表结构为：主模块列表(classcode长度为5),每个MessageBean下的子模块{@link ModuleBean#getChildList()}把子模块加入 
     * 各个ModuleBean的{@link ModuleBean#getParent()}也设置好，同时多语言也在函数内部设置了
     * 调用了 {@link #sortArrayList(ArrayList)}主法来实现按orderby进行排序。（collection.sort存在版本问题）
     * @return 模块列表
     * 
     */
	public HashMap<String,ArrayList> getModuleList2() {
		HashMap<String,ArrayList> map = new HashMap<String, ArrayList>();
		
		long t = System.currentTimeMillis();
		ArrayList<ModuleBean> list = new ArrayList<ModuleBean>();
		//按classcode进行排序
		java.util.TreeSet<ModuleBean> moduleSet= new TreeSet<ModuleBean>(new Comparator<ModuleBean>(){
			public int compare(ModuleBean o1, ModuleBean o2) {
				if(o1!=null && o2!=null && o1.getClassCode()!=null && o2.getClassCode()!=null)
				return o1.getClassCode().compareTo(o2.getClassCode());
				return 1;
			}
		});
		

		System.out.println("select Bean from ModuleBean Bean where  Bean.isDisplay=0 and Bean.isUsed = 1 "
				+ " order by Bean.classCode ");
		Result temp = query("select Bean from ModuleBean Bean where 1=? and Bean.isDisplay=0 and Bean.isUsed = 1 and not classcode is null "
				+ " order by Bean.classCode ",new Integer(1));//蛋疼的方法，不带参数就到另外一个方法中了，只能加了一个无意的参数
		
		System.out.println("searchDB"+(System.currentTimeMillis()-t));
		if (temp.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			return map;
		} else {
			final ArrayList rstemp = (ArrayList) temp.getRetVal();
			//把所有的模块的显示名采用多语言进行处理
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection connection) throws SQLException {
							Connection conn = connection;
							// 查询多语言
							KRLanguageQuery rlq = new KRLanguageQuery();
							for (int i = 0; i < rstemp.size(); i++) {
								ModuleBean tempmb = (ModuleBean) rstemp.get(i);
								rlq.addLanguageId(tempmb.getModelId());
							}
							HashMap tempMap = rlq.query(conn);
							for (int i = 0; i < rstemp.size(); i++) {
								ModuleBean tempmb = (ModuleBean) rstemp.get(i);
								KRLanguage language = (KRLanguage) tempMap.get(tempmb.getModelId());
								if (language == null) {
									language = KRLanguageQuery.create(tempmb.getModelId(), tempmb.getModelId(), tempmb.getModelId());
								}
								tempmb.setModelDisplay(language);
							}
						}
					});
					return ErrorCanst.DEFAULT_SUCCESS;
				}
			});
		}
		System.out.println("language"+(System.currentTimeMillis()-t));

		for (Object o : (ArrayList) temp.retVal) {
			ModuleBean mbtemp = (ModuleBean) o;
			moduleSet.add(mbtemp);

			List<ModuleOperationBean> modOpList = mbtemp.getModuleoperationinfo();

			// 设置admin帐号的权限,admin帐号不受任何限制
			String url = mbtemp.getLinkAddress();
			int lastIndex = 0;
			if (url != null && url.contains("UserFunctionQueryAction.do")) {
				if (url.indexOf("parentTableName=") != -1) {
					lastIndex = url.indexOf("&", url.indexOf("&tableName=") + 11);
					if (lastIndex != -1) {
						url = url.substring(0, lastIndex);
					}
				} else if (url.indexOf("&moduleType=") != -1) {
					lastIndex = url.indexOf("&", url.indexOf("&moduleType=") + 12);
					if (lastIndex != -1) {
						url = url.substring(0, lastIndex);
					}
				} else {
					if (url.contains("&")) {
						url = url.substring(0, url.indexOf("&", url.indexOf("?tableName")));
					}
				}
			}
			
			MOperation mop = new MOperation();
			mop.moduleUrl = url;
			mop.moduleId = mbtemp.getId().trim();
			if (modOpList != null) {
				for (int j = 0; j < modOpList.size(); j++) {
					ModuleOperationBean opBean = modOpList.get(j);
					mop.setOperation(opBean.getOperationID());
				}
			}
			BaseEnv.adminOperationMap.put(url, mop);
			BaseEnv.adminOoperationMapKeyId.put(mop.moduleId, mop); // 用于弹出窗的鉴权
		}
		System.out.println("premission===="+(System.currentTimeMillis()-t));

		//设置父子关系，同时子模块按orderby语句进行排序
		java.util.Stack<ModuleBean> stack = new Stack<ModuleBean>();
		
		for (ModuleBean moduleBean : moduleSet) {// 因为此集合已按classcode进行了排序，所以子节点一定是连续的几个
			while (!stack.isEmpty() && stack.lastElement().getClassCode() != null && !moduleBean.getClassCode().substring(0,moduleBean.getClassCode().length()-5).equals(stack.lastElement().getClassCode()))// 如果当前classcode不包含堆中的最后一个元素的classcode则表示这两个bean非父子关系
			{
				if (stack.lastElement().getChildList() != null)// 按orderBy属性把子列表进行排序进行排序
					sortArrayList(stack.lastElement().getChildList());
				stack.pop();
			}
			if (stack.isEmpty()) {
				if (moduleBean.getClassCode() != null && moduleBean.getClassCode().length() == 5)// 如果是根节点
				{
					stack.push(moduleBean);
					list.add(moduleBean);
				}
			} else {
				if (moduleBean.getClassCode().substring(0,moduleBean.getClassCode().length()-5).equals(stack.lastElement().getClassCode())) {
					moduleBean.setParentModuleBean(stack.lastElement());
					if (stack.lastElement().getChildList() == null)
						stack.lastElement().setChildList(new ArrayList<ModuleBean>());
					stack.lastElement().getChildList().add(moduleBean);
					stack.push(moduleBean);
				}
			}
		}
		System.out.println("relation" + (System.currentTimeMillis() - t));
		sortArrayList(list);
		for (ModuleBean moduleBean : list) {
			String mainModule = moduleBean.getMainModule();
			if (!map.containsKey(mainModule))
			{
				map.put(mainModule, new ArrayList());
			}
			map.get(mainModule).add(moduleBean);
		}
		map.get("1").addAll(map.get("0"));
		map.get("2").addAll(map.get("0"));
		map.get("3").addAll(map.get("0"));
		map.get("4").addAll(map.get("0"));
		
		// ====设置父子关系结束
		return map;
	}
    
    
    public Result update(EmployeeBean bean) {
        // 由于下面有子表，先删除子表数据，再添加
        // 这里重载doBeforeExec完成删除工作
        // 检查是否超过最大用户数限制
    	if(!bean.getId().equals("1")){
	        if (bean.getCanJxc()==1 && getDBUserNum(1,bean.getId()) >= SystemState.instance.userNum) {
	            Result rs = new Result();
	            rs.retCode = ErrorCanst.RET_USER_LIMIT_ERROR;
	            return rs;
	        }
	        if (bean.getCanOa()==1 && getDBUserNum(2,bean.getId()) >= SystemState.instance.oaUserNum) {
	            Result rs = new Result();
	            rs.retCode = ErrorCanst.RET_OAUSER_LIMIT_ERROR;
	            return rs;
	        }
	        if (bean.getCanCrm()==1 && getDBUserNum(3,bean.getId()) >= SystemState.instance.crmUserNum) {
	            Result rs = new Result();
	            rs.retCode = ErrorCanst.RET_CRMUSER_LIMIT_ERROR;
	            return rs;
	        }
	        if (bean.getCanHr()==1 && getDBUserNum(4,bean.getId()) >= SystemState.instance.hrUserNum) {
	            Result rs = new Result();
	            rs.retCode = ErrorCanst.RET_HRUSER_LIMIT_ERROR;
	            return rs;
	        }
	        if (bean.getCanOrders()==1 && getDBUserNum(10,bean.getId()) >= SystemState.instance.ordersUserNum) {
	            Result rs = new Result();
	            rs.retCode = ErrorCanst.RET_ORDERSUSER_LIMIT_ERROR;
	            return rs;
	        }
    	}
        

        DBUtil.execute(new IfDB() {
            public int exec(Session session) {

                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        DDLOperation.updateRefreshTime("employee", connection);
                    }
                });
                return ErrorCanst.DEFAULT_SUCCESS;
            }
        });
        Result rs = updateBean(bean);
        return rs;
    }
    public Result updatePwd(EmployeeBean bean) {
        Result rs = updateBean(bean);
        return rs;
    }

    /**
     * 删除多条系统用户记录
     *
     * @param ids
     *            long[]
     * @return Result
     */
    public Result delete(final String[] ids) {
        Result rs = new Result();
        final ArrayList list = new ArrayList();
        rs.retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                for (String id : ids) {
                    Result temp = loadBean(id, EmployeeBean.class, session);
                    if (temp.retCode != ErrorCanst.DEFAULT_SUCCESS) {
                        return temp.retCode;
                    }
                    EmployeeBean bean = (EmployeeBean) temp.retVal;
                    bean.setOpenFlag(0);
                    bean.setSysName("");
                    bean.setPassword("");
                    Result updateTemp = updateBean(bean, session);
                    if (updateTemp.retCode != ErrorCanst.DEFAULT_SUCCESS) {
                        return updateTemp.retCode;
                    }
                    list.add(bean);
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                                SQLException {
                            DDLOperation.updateRefreshTime("employee", connection);
                        }
                    });

                }
                return ErrorCanst.DEFAULT_SUCCESS;
            }
        });
        rs.retVal = list;
        return rs;

    }

    /**
     * 周新宇 查询所有User记录
     *
     * @param name
     *            String
     * @param pageNo
     *            int
     * @param pageSize
     *            int
     * @return Result
     */
    public Result query(String name, int pageNo, int pageSize, String SCompanyID, String sysName) {

        // 创建参数
        List param = new ArrayList();
        String hql = "select bean from EmployeeBean as bean where bean.openFlag = 1 and bean.SCompanyID like '"
                     + SCompanyID + "%' ";
        // 如标题不为空，则做标题模糊查询
        if (name != null && name.length() != 0) {
            hql += " and upper(bean.empFullName) like '%'||?||'%' ";
            param.add(name.trim().toUpperCase());
        }

        if (sysName != null && sysName.length() != 0) {
            hql += " and upper(bean.sysName) like '%'||?||'%' ";
            param.add(sysName.trim().toUpperCase());
        }

        // 调用list返回结果
        return list(hql, param, pageNo, pageSize, true);
    }
    public Result query(String name, int pageNo, int pageSize, String SCompanyID, String sysName,String depart,String titleId,String roleName) {

        // 创建参数
        List param = new ArrayList();
        String sql = "select a.id,empFullName,deptFullName,sysName,titleId,canJxc,canOa,canCrm,canHr,canOrders,defSys " +
        		" from tblEmployee a" +
        		" left join tblDepartMent b on a.departmentCode=b.classCode where isnull(a.ispublic,0) <>1 and a.openFlag = 1 and a.statusId=0 and a.SCompanyID like '"
                     + SCompanyID + "%' ";
        if (roleName != null &&roleName != "" && roleName.length() != 0) {
        	sql = "select  a.id,empFullName,deptFullName,sysName,titleId,canJxc,canOa,canCrm,canHr,canOrders,defSys " +
    		" from tblEmployee a " +
    		" left join tblDepartMent b on a.departmentCode=b.classCode " +
    		" join tblUserSunCompany c on c.userId=a.id " +
    		" join tblrole d on d.roleName like ? and   c.roleIds like '%'+d.id+';%' " +
    		" where a.openFlag = 1 and a.SCompanyID like '"
                 + SCompanyID + "%' ";
            param.add("%"+roleName+"%");
        }
        
        // 如标题不为空，则做标题模糊查询
        if (name != null && name.length() != 0) {
            sql += " and upper(a.empFullName) like ? ";
            param.add("%"+name.trim().toUpperCase()+"%");
        }

        if (sysName != null && sysName.length() != 0) {
            sql += " and upper(a.sysName) like ? ";
            param.add("%"+sysName.trim().toUpperCase()+"%");
        }
        if (depart != null && depart.length() != 0) {
        	String[] departs = depart.split(";");
        	String dh = "";
        	for(String dt:departs){
        		if(dt.length()>0){
		            dh += "or upper(b.deptFullName) like ? ";
		            param.add("%"+dt.toUpperCase()+"%");
        		}
	        }
        	if(dh.length() > 0){
        		dh = dh.substring(2);
        		sql += " and ("+dh+")";
        	}
        }
        if (titleId != null &&titleId != "" && titleId.length() != 0) {
            sql += " and a.titleId = ? ";
            param.add(titleId);
        }
        
        sql += " group by  a.id,empFullName,deptFullName,sysName,titleId,canJxc,canOa,canCrm,canHr,canOrders,defSys ";
        
        
        // 调用list返回结果
        return sqlList(sql, param, pageSize,pageNo,  true);
    }
    public Result querySelf(String userId) {
        // 创建参数
        List param = new ArrayList();
        String hql = "select bean from EmployeeBean as bean where bean.openFlag = 1 "
                     + "and bean.id like '%'||?||'%' ";
        param.add(userId);
        // 调用list返回结果
        return list(hql, param);
    }

    /**
     * 周新宇 查一条系统用户的详细信息
     *
     * @param notepadId
     *            long 代号
     * @return Result 返回结果
     */
    public Result queryEmp(String id) {
        // 创建参数
        List param = new ArrayList();
        String hql = "select bean from EmployeeBean as bean where  bean.id = ?";
        param.add(id);
        // 调用list返回结果
        return list(hql, param);
    }

    /**
     * 查询所有模块， 周新宇
     *
     * @param name
     *            String
     * @param pageNo
     *            int
     * @param pageSize
     *            int
     * @return Result
     */
    public Result queryAll() {

        // 创建参数
        List param = new ArrayList();
        String hql =
            "select bean from ModuleBean as bean where length(bean.classCode) =5  order by len(bean.classCode)desc,bean.orderBy asc ";
        Result rs = list(hql, param);
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            return rs;
        }
        ArrayList mbean = (ArrayList) rs.retVal;
        HashMap map = new HashMap();
        for (Object o : mbean) {
            ModuleBean bean = (ModuleBean) o;
            param.clear();
            param.add(bean.getClassCode());
            hql = "select bean from ModuleBean as bean where length(bean.classCode) >5 and (length(bean.tblName) >0 or length(bean.linkAddress) >0) and bean.classCode like ?||'%'    order by len(bean.classCode)desc,bean.orderBy asc ";
            Result temp = list(hql, param);
            if (temp.retCode != ErrorCanst.DEFAULT_SUCCESS) {
                return rs;
            }
            map.put(bean, temp.retVal);
        }
        rs.retVal = new Object[] {mbean, map}; // 返回模块的索引和结果

        // 调用list返回结果
        return rs;
    }

    /**
     *
     * @param id
     *            String
     * @return Result
     */
    public Result queryUserModuleByUserid(String id) {

        return query(
            "select Bean from UserModuleBean Bean where Bean.userBean.id=?",
            id);
    }

    /**
     * 周新宇 查一条系统用户的详细信息
     *
     * @param notepadId
     *            long 代号
     * @return Result 返回结果
     */
    public Result detail(String id) {
        // 装载着user表中的数据，userrole表中的数据，usermodule表中的数据
        return loadBean(id, EmployeeBean.class);

    }

    /**
     * 登陆 周新宇
     *
     * @param name
     *            String
     * @param pass
     *            String
     * @return Result
     */
    public Result login(String name, String pass) {

    	List list = new ArrayList();
    	list.add(name);
    	list.add(pass);
        Result rs = sqlList("select loginEndTime,loginStartTime,id,statusId from tblemployee as bean where bean.openFlag =1 and bean.sysName=? and bean.password=?", list);
        
        if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS &&  ((List)rs.retVal).size() > 0){
        	Object[] bean  = (Object[])(((List)rs.retVal).get(0));
        	if(bean[0] != null && bean[0].toString().length() > 0 && bean[1] != null && bean[1].toString().length() >0){
        		String curTime = new SimpleDateFormat("HH:mm").format(new Date());
        		if(curTime.compareTo(bean[1].toString())<0 || curTime.compareTo(bean[0].toString())>0){
        			rs.retCode = ErrorCanst.TIMER_COMPARE_ERROR;
        			rs.retVal = bean[1] +"-"+ bean[0];
        			return rs;
        		}
        	}
        	if(!bean[3].toString().equals("0")){
        		rs.retCode = ErrorCanst.USER_STOP;
        		return rs;
        	}
            return query("select bean from EmployeeBean as bean where bean.id = ?",String.valueOf(bean[2]));
        }
        rs.setRetCode(ErrorCanst.RET_NAME_PSW_ERROR);
        return rs;
    }
    /**
     * 登陆 周新宇
     *
     * @param name
     *            String
     * @param pass
     *            String
     * @return Result
     */
    public Result login(String userId) {

    	List list = new ArrayList();
    	list.add(userId);
    	list.add(userId);
        Result rs = sqlList("select loginEndTime,loginStartTime,id,statusId from tblemployee as bean where bean.openFlag =1 and ( bean.id =? or bean.openid =?)", list);
        
        if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS &&  ((List)rs.retVal).size() > 0){
        	Object[] bean  = (Object[])(((List)rs.retVal).get(0));
        	if(bean[0] != null && bean[0].toString().length() > 0 && bean[1] != null && bean[1].toString().length() >0){
        		String curTime = new SimpleDateFormat("HH:mm").format(new Date());
        		if(curTime.compareTo(bean[1].toString())<0 || curTime.compareTo(bean[0].toString())>0){
        			rs.retCode = ErrorCanst.TIMER_COMPARE_ERROR;
        			rs.retVal = bean[1] +"-"+ bean[0];
        			return rs;
        		}
        	}
        	if(!bean[3].toString().equals("0")){
        		rs.retCode = ErrorCanst.USER_STOP;
        		return rs;
        	}
            return query("select bean from EmployeeBean as bean where bean.id = ?",String.valueOf(bean[2]));
        }
        rs.setRetCode(ErrorCanst.RET_NAME_PSW_ERROR);
        return rs;
    }
    
    
    public Result loginNoMD5(String name, String pass) {

    	MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(pass.trim().getBytes()) ;
			String md5Pwd = toHex(md.digest()) ;
			pass =md5Pwd;
		} catch (NoSuchAlgorithmException e) {
			BaseEnv.log.error("UserMgt.add Password MD5 Error :",e);
			Result rs = new Result();
            rs.retCode = ErrorCanst.DEFAULT_FAILURE;
            return rs;
		}
    	
	  	List list = new ArrayList();
	  	list.add(name);
	  	list.add(pass);
	      Result rs = sqlList("select loginEndTime,loginStartTime,id,statusId from tblemployee as bean where bean.openFlag =1  and bean.sysName=? and bean.password=?", list);
	      
	      if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS &&  ((List)rs.retVal).size() > 0){
	      	Object[] bean  = (Object[])(((List)rs.retVal).get(0));
	      	if(bean[0] != null && bean[0].toString().length() > 0 && bean[1] != null && bean[1].toString().length() >0){
	      		String curTime = new SimpleDateFormat("HH:mm").format(new Date());
	      		if(curTime.compareTo(bean[1].toString())<0 || curTime.compareTo(bean[0].toString())>0){
	      			rs.retCode = ErrorCanst.TIMER_COMPARE_ERROR;
	      			rs.retVal = bean[1] +"-"+ bean[0];
	      			return rs;
	      		}
	      	}
	      	if(!bean[3].toString().equals("0")){
        		rs.retCode = ErrorCanst.USER_STOP;
        		return rs;
        	}
	          return query("select bean from EmployeeBean as bean where bean.id = ?",String.valueOf(bean[2]));
	      }
	      rs.setRetCode(ErrorCanst.RET_NAME_PSW_ERROR);
	      return rs;
	  }
    
    /**
     * 根据用户名查询用户
     *
     * @param name
     *            String
     * @param pass
     *            String
     * @return Result
     */
    public Result loginByName(String name) {

        Result rs = query(
            "select bean from EmployeeBean as bean where bean.openFlag =1 and bean.statusId ='0' and bean.sysName=?",name);
        if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS &&  ((List)rs.retVal).size() > 0){
        	EmployeeBean bean  = (EmployeeBean)(((List)rs.retVal).get(0));
        	if(bean.getLoginEndTime() != null && bean.getLoginEndTime().length() > 0 && bean.getLoginStartTime() != null && bean.getLoginStartTime().length() >0){
        		String curTime = new SimpleDateFormat("HH:mm").format(new Date());
        		if(curTime.compareTo(bean.getLoginStartTime())<0 || curTime.compareTo(bean.getLoginEndTime())>0){
        			rs.retCode = ErrorCanst.TIMER_COMPARE_ERROR;
        			rs.retVal = bean.getLoginStartTime() +"-"+ bean.getLoginEndTime();
        		}
        	}
        }
        return rs;
    }
    
    /**
     * 根据用户ID查询用户
     *
     * @param name
     *            String
     * @param pass
     *            String
     * @return Result
     */
    public Result loginById(String userId) {

        Result rs = query(
            "select bean from EmployeeBean as bean where bean.openFlag =1 and bean.statusId ='0' and bean.id=?",userId);
        if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS &&  ((List)rs.retVal).size() > 0){
        	EmployeeBean bean  = (EmployeeBean)(((List)rs.retVal).get(0));
        	if(bean.getLoginEndTime() != null && bean.getLoginEndTime().length() > 0 && bean.getLoginStartTime() != null && bean.getLoginStartTime().length() >0){
        		String curTime = new SimpleDateFormat("HH:mm").format(new Date());
        		if(curTime.compareTo(bean.getLoginStartTime())<0 || curTime.compareTo(bean.getLoginEndTime())>0){
        			rs.retCode = ErrorCanst.TIMER_COMPARE_ERROR;
        			rs.retVal = bean.getLoginStartTime() +"-"+ bean.getLoginEndTime();
        		}
        	}
        }
        return rs;
    }
    
    public Result getEmployee(String id) {
        return loadBean(id, EmployeeBean.class);
     }
    
    public HashMap<String, String> getEmployee2(final String userId){
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try {
							String sql = "select a.empFullName,a.StockCode,a.CompanyCode,b.StockFullName,c.ComFullName from tblEmployee a "
									+ "left join tblStock b on a.StockCode = b.classCode "
									+ "left join tblCompany c on a.Companycode = c.classCode "
									+ "where a.id=?";
                            PreparedStatement pss = conn.prepareStatement(sql);
                            pss.setString(1, userId);
                            ResultSet rss = pss.executeQuery();
                            if (rss.next()) {
                            	HashMap<String, String> empMap = new HashMap<String, String>();
                            	empMap.put("empFullName", rss.getString("empFullName"));
                            	empMap.put("StockCode", rss.getString("StockCode"));
                            	empMap.put("CompanyCode", rss.getString("CompanyCode"));
                            	empMap.put("StockFullName", rss.getString("StockFullName"));
                            	empMap.put("ComFullName", rss.getString("ComFullName"));
                            	rs.setRetVal(empMap);
                            }
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error(" UserMgt.getEmployee2() Query data Error :" , ex);
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
    	return rs.retVal!=null?(HashMap<String, String>)rs.retVal:null;
    }
    
    public Result changeWebNote(final String userId,final int type){
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try {
							String sql = "update tblemployee set showWebNote=? where id= ?";
                            PreparedStatement pss = conn.prepareStatement(sql);
                            pss.setInt(1, type);
                            pss.setString(2, userId);
                            pss.executeUpdate();
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error(" UserMgt.changeWebNote() Query data Error :" , ex);
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.retCode = retCode;
    	return rs;
    }
        
    public String getWisdom(){
    	//随机取某条名言
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        String sql = "select   top   1   wisdom   from   tblWisdom   order   by   newid() ";
                        //String sql = "select   top   1   wisdom   from   tblWisdom where id='ca9b21198_1105041710157650107'";
                                   
                        try {

                            Statement st = conn.createStatement();
                            ResultSet rss = st.executeQuery(sql);
                            List ls = new ArrayList();
                            if (rss.next()) {
                                rs.retVal = rss.getString(1);
                            }
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
                            BaseEnv.log.error(" UserMgt.getWisdom() Query data Error :" , ex);
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
    	return rs.retVal!=null?rs.retVal.toString():"";
    }

    /**
     * 查询系统用户数
     *
     * @return int
     */
    private int getDBUserNum(final int type,final String self) {
        final Result res = new Result();
        res.setRetVal(new ArrayList());
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws SQLException {
                        Connection conn = connection;
                        ArrayList list = new ArrayList();
                        // 查主表
                        String querysql = " select count(*) from tblEmployee where id <>'1' and openFlag = 1  and statusid=0 and id <>'"+self+"'  ";
                        if(1==type){
                        	querysql += " and canJxc=1 ";
                        }else if(2==type){
                        	querysql += " and canOa=1 ";
                        }else if(3==type){
                        	querysql += " and canCrm=1 ";
                        }else if(4==type){
                        	querysql += " and canHr=1 ";
                        }else if(10==type){
                        	querysql += " and canOrders=1 ";
                        }
                        try {
                            PreparedStatement cs = conn
                                .prepareStatement(querysql);
                            BaseEnv.log.debug(querysql);
                            ResultSet rset = cs.executeQuery();
                            int count = 0;
                            if (rset.next()) {
                                count = rset.getInt(1);
                            }
                            res.setRetVal(count + "");
                        } catch (Exception ex) {
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error(
                                "Query data Error InitMenDate.getDBUserNum :"
                                + querysql, ex);
                            return;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        if (res.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            return Integer.parseInt(res.getRetVal().toString());
        } else {
            return 0;
        }
    }

    private Result query(String hql, Object ...param) {
        ArrayList params = new ArrayList();
        for (Object obj : param) {
            params.add(obj);
        }
        return list(hql, params);
    }

    /**
     * 查找所有系统用户
     *
     * @param SCompanyID
     * @return
     */
    public Result query(String SCompanyID) {
        List param = new ArrayList();
        String hql = "select bean from EmployeeBean as bean where bean.openFlag = 1 and bean.sysName is not null and bean.password is not null and bean.SCompanyID like ? ";
        param.add(SCompanyID);
        // 调用list返回结果
        return this.list(hql, param);

    }

    /**
     * 根据ID查找系统用户
     *
     * @param userId
     * @return
     */
    public Result queryEmployee(String userId) {
        // 创建参数
        List param = new ArrayList();
        String hql =
            "select bean from EmployeeBean as bean where bean.openFlag = 1 and bean.sysName is not null and bean.password is not null and bean.id = ? ";
        param.add(userId);
        // 调用list返回结果
        return list(hql, param);
    }
    
    /**
     * 根据ID查找系统用户
     *
     * @param userId
     * @return
     */
    public EmployeeBean queryEmployee2(String userId) {
        // 创建参数
        List param = new ArrayList();
        String hql = "select bean from EmployeeBean as bean where bean.openFlag = 1 and bean.sysName is not null and bean.password is not null and bean.id = ? ";
        param.add(userId);
        // 调用list返回结果
        Result result = list(hql, param);
        if(result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retVal!=null && ((ArrayList)result.retVal).size()>0){
        	return ((ArrayList<EmployeeBean>)result.retVal).get(0);
        }else{
        	return null;
        }
    }

    /**
     * 查询所有用户
     * @return
     */
    public Result queryAllEmployee(){
    	String hql = "select bean from EmployeeBean as bean" ;
    	List param = new ArrayList() ;
    	return list(hql, param);
    }

    /**
     * 根据用户Id查询用户名
     * @param userIds
     * @param conn
     * @return
     */
    public String queryEmployeeNameById(String[] userIds,Connection conn){
    	
    	if(userIds==null || userIds.length==0) return "" ;
    	String strUserId = "" ;
    	for(String userId : userIds){
    		strUserId += "'"+userId+"'," ;
    	}
    	strUserId = strUserId.substring(0, strUserId.length()-1) ;
    	String sql = "select empFullName from tblEmployee where id in ("+strUserId+")" ;
    	String strUserName = "" ;
    	try {
			PreparedStatement pss = conn.prepareStatement(sql) ;
			ResultSet rss = pss.executeQuery() ;
			while(rss.next()){
				strUserName += rss.getString("empFullName")+"," ;
			}
			if(strUserName.indexOf(",")!=-1){
				strUserName = strUserName.substring(0,strUserName.length()-1) ;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			BaseEnv.log.error("UserMgt queryEmployeeNameById:",ex) ;
		}
		return strUserName ;
    }
    
    /**
     * 根据用户Id查询用户名
     * @param userIds
     * @param conn
     * @return
     */
    public String queryDeptNameById(String deptCode,Connection conn){
    	
    	if(deptCode==null || deptCode.length()==0) return "" ;
    	String sql = "select deptFullName from tblDepartment where ? like '%,'+ classCode +',%'";
    	String strDeptName = "" ;
    	try {
			PreparedStatement pss = conn.prepareStatement(sql) ;
			pss.setString(1, deptCode);
			ResultSet rss = pss.executeQuery() ;
			while(rss.next()){
				strDeptName += rss.getString("deptFullName")+"," ;
			}
			if(strDeptName.indexOf(",")!=-1){
				strDeptName = strDeptName.substring(0,strDeptName.length()-1) ;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			BaseEnv.log.error("UserMgt queryDeptNameById:",ex) ;
		}
		return strDeptName ;
    }
    
    /**
     * 根据系统用户名查看是否存在，如果不存在，则可以创建此用
     * @return
     */
    public Result queryEmployeeBySysName(String sysName){
    	//创建参数
    	List param = new ArrayList() ;
    	String hql = "select bean from EmployeeBean as bean where bean.sysName=?" ;
    	param.add(sysName) ;
    	return list(hql, param);
    }
    
    /**
	 * 根据系统用户名查询用户信息
	 * 
	 * @param userName
	 * @return
	 */
    public Result queryEmployeeByEmpName(String userName){
    	// 创建参数
    	List<String> param = new ArrayList<String>() ;
    	String hql = "from EmployeeBean as bean where bean.sysName=? and openFlag=1 and  statusId!=-1" ;
    	param.add(userName) ;
    	Result result = list(hql, param);
    	if(result.realTotal>0){
    		List<EmployeeBean> listEmp = (List<EmployeeBean>) result.retVal ;
    		result.setRetVal(listEmp.get(0)) ;
    		result.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
    	}else{
    		result.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
    	}
    	return result ;
    }
    
    /**
     * 根据系统用户是否存在，如果不存在，则可以创建此用户
     * @return
     */
    public Result queryEmployeeById(String empId){
    	//创建参数
    	List param = new ArrayList() ;
    	String hql = "select bean from EmployeeBean as bean where bean.id=? and openFlag=1 and  statusId!=-1" ;
    	param.add(empId) ;
    	return list(hql, param);
    }
    
    public Result queryUserSunCompanyRoles(){
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        String str_sel =
                                    "select userid,sunCompanyid,roleids from tblUserSunCompany" ;
                                   
                        try {

                            Statement st = conn.createStatement();
                            ResultSet rss = st.executeQuery(str_sel);
                            List ls = new ArrayList();
                            while (rss.next()) {
                                String[] o = new String[3];
                                o[0] = rss.getString("userid");
                                o[1] = rss.getString("sunCompanyid");
                                o[2] = rss.getString("roleids");
                                ls.add(o);
                            }
                            
                            str_sel ="select id,roleName from tblRole where id!='1'";
                            rss = st.executeQuery(str_sel);
                            List ls2 = new ArrayList();
                            while (rss.next()) {
                                String[] o = new String[2];
                                o[0] = rss.getString("id");
                                o[1] = rss.getString("roleName");
                                ls2.add(o);
                            }
                            
                            HashMap map=new HashMap();
                            for(int i=0;i<ls.size();i++){
                            	String []o=(String[])ls.get(i);
                            	String []role=new String[2];                            	
                            	map.put(o[0], role);
                            	role[0]=o[2];
                            	role[1]="";
                            	
                            	if(role[0]!=null){
	                            	for(int j=0;j<ls2.size();j++){
	                            		String []roleDis=(String [])ls2.get(j);
	                            		if(role[0].indexOf(roleDis[0])>=0){
	                            			role[1]+=roleDis[1]+";";
	                            		}
	                            	}
	                            	if(role[1].length()>0){
	                            		role[1]=role[1].substring(0,role[1].length()-1);
	                            	}
                            	}
                            }
                            
                            rs.retVal = map;
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
                            BaseEnv.log.error("Query data Error :" + str_sel, ex);
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });

        return rs;
    }
    
    public Result getDefPageName(final String url,final String local) {
        final Result res = new Result();
        res.setRetVal(new ArrayList());
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws SQLException {
                        Connection conn = connection;
                        ArrayList list = new ArrayList();
                        // 查主表
                        String querysql = " select "+local+" from tblModules a,tblLanguage  b  where a.modelName=b.id and a.linkAddress=?";
                        try {
                            PreparedStatement cs = conn
                                .prepareStatement(querysql);
                            BaseEnv.log.debug(querysql);
                            cs.setString(1, url);
                            ResultSet rset = cs.executeQuery();
                          
                            if (rset.next()) {
                                res.setRetVal(rset.getString(1));
                            }
                        } catch (Exception ex) {
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error(
                                "Query data Error InitMenDate.getDBUserNum :"
                                + querysql, ex);
                            return;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res;
    }
    
    public Result getDepartmentMap() {
        final Result res = new Result();
        res.setRetVal(new ArrayList());
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws SQLException {
                        Connection conn = connection;
                        ArrayList list = new ArrayList();
                        // 查主表
                        String querysql = " select classCode,deptFullName from tblDepartment ";
                        try {
                            PreparedStatement cs = conn
                                .prepareStatement(querysql);
                            ResultSet rset = cs.executeQuery();
                            HashMap map = new HashMap();
                            while (rset.next()) {
                                map.put(rset.getString(1), rset.getString(2));
                            }
                            res.retVal = map;
                        } catch (Exception ex) {
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error(
                                "Query data Error InitMenDate.getDBUserNum :"
                                + querysql, ex);
                            return;
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res;
    }
    
    public Result updateLoginStatus() {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						String sql="update tblSyssetting set Setting='1' where id='0dfba4892isdiplaylogin'";
						PreparedStatement pst =conn.prepareStatement(sql);
						pst.executeUpdate();
						long createTime=System.currentTimeMillis();
						String sqll="update tblInitTime set lastTime="+createTime+"where initName='systemSet'";
						PreparedStatement pstt =conn.prepareStatement(sqll);
						pstt.executeUpdate();	
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
    /**
	 * 查询所有部门 mj
	 * @return
	 */
	public Result queryAllDept(){
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        String sql = "select deptFullName,classCode from tblDepartment dept where statusId=0";
                        try {
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        	ResultSet rss = pss.executeQuery() ;
                        	ArrayList<String[]> deptList = new ArrayList<String[]>() ;
                        	while(rss.next()){
                        		String[] dept = new String[2] ;
                        		dept[0] = rss.getString("classCode") ;
                        		dept[1] = rss.getString("deptFullName") ;
                        		deptList.add(dept) ;
                        	}
                        	rs.setRetVal(deptList) ;
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error(" UserMgt.queryDept Query data Error :" , ex);
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.retCode = retCode ;
        return rs ;
	}
	
	/**
	 * 查询所有部门 mj
	 * @return
	 */
	public HashMap queryDeptByCode(final String classCode,final String type){
		final HashMap map=new HashMap();
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        String sql = "select * from tblDepartment dept ";
                        if("ID".equals(type)){
                        	sql += "where id = ?";
                        }else{
                        	sql += "where classCode = ?";
                        }
                        try {
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        	pss.setString(1, classCode);
                        	ResultSet rss = pss.executeQuery() ;
                        	if(rss.next()){
	                        	for(int i=1;i<=rss.getMetaData().getColumnCount();i++){
	                        		Object obj=rss.getObject(i);
	                        		if(obj==null){
	                        			if(rss.getMetaData().getColumnType(i)==Types.NUMERIC || rss.getMetaData().getColumnType(i)==Types.INTEGER){
	                        				map.put(rss.getMetaData().getColumnName(i), 0);
	                        			}else{
	                        				map.put(rss.getMetaData().getColumnName(i), "");
	                        			}
	                        		}else{
	                        			map.put(rss.getMetaData().getColumnName(i), obj);
	                        		}
	                        	}
                        	}else{
                        		BaseEnv.log.debug(" UserMgt.queryDeptByCode Query data 没有结果集 :"+sql +":参数="+classCode);
                        	}
                        	rs.setRetVal(map) ;
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error(" UserMgt.queryDeptByCode Query data Error :" , ex);
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        return map ;
	}
	
	/**
	 * 用来排序队列，只被获取模块列表使用。（原因：Collection的sort方法存在不兼容的问题）
	 * @param al 传入的ArrayList,传出返回的时候此队列是排序过的。
	 */
	private void sortArrayList(ArrayList al){ 
		java.util.TreeSet<ModuleBean> moduleSet= new TreeSet<ModuleBean>(new Comparator<ModuleBean>(){
			public int compare(ModuleBean o1, ModuleBean o2) {
				if(o1!=null && o2!=null)
				{
					if(o1.getOrderBy()==o2.getOrderBy()) return 1;
					if(o1.getOrderBy()>o2.getOrderBy())
						return 1;
					return -1;
				}
				return 1;
			}
		});
		moduleSet.addAll(al);
		al.clear();
		al.addAll(moduleSet);
	}
	
	/**
	 * 根据用户Id集合获取系统用户Id
	 * @param userIds
	 * @return
	 */
	public boolean getOpenFlagUserById(String userId){
		List<String> param=new ArrayList<String>();
		String strsql="select id from tblemployee where openflag='1' and  id =? ";
		param.add(""+userId+"");
		Result  rs= this.sqlList(strsql, param);
		boolean flag=false;
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			List objlist=(List) rs.retVal;
			if(objlist.size()>0){
				flag=true;
			}
		}
		return flag;
	}
	
	/**
	 * 根据部门Id获取部门classCode
	 * @param deptIds
	 * @return
	 */
	public String[] getDeptCodeById(String deptIds[]){
		List<String> param=new ArrayList<String>();
		String ids="";
		for(int i=0;i<deptIds.length;i++){
			if(i+1==deptIds.length){
				ids+="'"+deptIds[i]+"'";
			}else{
				ids+="'"+deptIds[i]+"'"+",";
			}
		}
		String strsql="select classCode from tblDepartment where id in ("+ids+")";
		
		Result rs=sqlList(strsql, param);
		List objList=(List) rs.retVal;
		String deptCode[]=new String[objList.size()];
		for(int i=0;i<objList.size();i++){
			Object[] obj=(Object[]) objList.get(i);
			deptCode[i]=(String)obj[0];
		}
		return deptCode;
	}
	
	/**
	 * 根据用户Id集合获取系统用户Id
	 * @param userIds
	 * @return
	 */
	public Result getOpenFlagUser(String userIds[]){
		List<String> param=new ArrayList<String>();
		String ids="";
		for(int i=0;i<userIds.length;i++){
			if(i+1==userIds.length){
				ids+="'"+userIds[i]+"'";
			}else{
				ids+="'"+userIds[i]+"'"+",";
			}
		}
		String strsql="select id from tblemployee where openflag='1' and  id in ("+ids+") ";
		return this.sqlList(strsql, param);
	}
	
	/**
	 * 根据用户id查询数据
	 * @param userId
	 * @param conn
	 * @return
	 */
	public Result getLoginBean(final String userId,final Connection conn){
		final Result rst = new Result();
		String strsql = "select e.id,e.EmpFullName,e.EmpName,e.DepartmentCode,dep.DepartmentName,dep.DeptFullName from dbo.tblEmployee e,dbo.tblDepartment dep where dep.classCode=e.DepartmentCode ";
		strsql += " and e.id=?";
		try {
			PreparedStatement ps = conn.prepareStatement(strsql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();
			LoginBean obj = null;
			if (rs.next()) {
				obj = new LoginBean();
				obj.setId(rs.getString("id"));
				obj.setEmpFullName(rs.getString("EmpFullName"));
				obj.setName(rs.getString("EmpName"));
				obj.setDepartCode(rs.getString("DepartmentCode"));
				obj.setDepartmentManager(rs.getString("DepartmentName"));
				obj.setDepartmentName(rs.getString("DeptFullName"));
			}
			rst.setRetVal(obj);
		} catch (Exception ex) {
			ex.printStackTrace();
			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			BaseEnv.log.error("Query data Error :" + strsql, ex);
		}
		return rst;
	}
	
	 /**
     * 根据分组 查询该分组下的所有职员
     */
	public List<String> queryAllEmployeeByGroup(final String group) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "select b.userID from tblEmpGroup a,tblEmpGroupUser b where a.id=b.f_ref and a.id=?";
						try {
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, group);
							ResultSet rs = pss.executeQuery();
							List<String> strList = new ArrayList<String>();
							while (rs.next()) {
								strList.add(rs.getString(1));
							}
							rst.setRetVal(strList);
						} catch (Exception ex) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		List<String> list = null;
		if(retCode==ErrorCanst.DEFAULT_SUCCESS){
			list = (List<String>)rst.getRetVal() ;
		}
		return list ;
	}
	
	/**
     * 初始化用户列表
     */
    public static Result initOnlineUser() {
        final HashMap oldOnlineUserMap = OnlineUserInfo.cloneMap();
        OnlineUserInfo.clearUser();
        final Result rs = new Result();
        int retVal = DBUtil.execute(new IfDB() {
            public int exec(final Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try {
                            String sql = "select a.id,empFullName,DeptFullName,DepartmentCode,TitleID,a.statusId,a.SysName,a.photo,a.empNumber,a.responsibility,a.gender, isnull(a.isPublic,0) isPublic from tblEmployee a "+
                                         "left join tblDepartment b on a.DepartmentCode= b.classCode ";
                            Statement stmt = conn.createStatement();
                            ResultSet rset = stmt.executeQuery(sql);
                            while (rset.next()) {
                                OnlineUser ou = (OnlineUser)oldOnlineUserMap.get(rset.getString("id"));
                                String osession ="";
                                Date odate = new Date();
                                odate.setTime(0l);
                                if(ou != null){
                                    osession = ou.session;
                                    odate = ou.getActiveDate();
                                }
                                String photo = rset.getString("photo");
                                if(photo!=null && photo.contains(":")){
                                	photo = photo.split(":")[0];
                                }
                                OnlineUserInfo.putUser(rset.getString("id"),rset.getString("empFullName"),
                                        rset.getString("DepartmentCode"),rset.getString("DeptFullName"),osession,odate,
                                        rset.getString("TitleID"),rset.getString("statusId"),rset.getString("sysName"),photo,
                                        rset.getString("empNumber"),rset.getString("responsibility"),rset.getString("gender"),rset.getInt("isPublic"));
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            rs.setRetCode(
                                    ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                    }
                });
                return ErrorCanst.DEFAULT_SUCCESS;
            }
        });
        rs.setRetCode(retVal);
        return rs;
    }
    
    /**
     * 设置桌面
     * @param mainModule			所属模块
     * @param url					url
     * @param title					标题
     * @return
     */
    public Result setDesk(final String loginId,final String desk) {
        final Result rs = new Result();
        int retVal = DBUtil.execute(new IfDB() {
            public int exec(final Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try {
                        	String sql = "update tblEmployee set defDesk='"+desk+"' where id='"+loginId+"'";
                        	Statement st = conn.createStatement();
                        	int count = st.executeUpdate(sql);
                        	rs.setRetVal(count);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            rs.setRetCode(
                                    ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                    }
                });
                return ErrorCanst.DEFAULT_SUCCESS;
            }
        });
        rs.setRetCode(retVal);
        return rs;
    }
    
    
    
	/**
	 * 绑定微信openid 
	 * @param userName 登陆名
	 * @param pass 密码
	 * @param openid 微信id
	 * @return
	 */
	public Result updateUserOpenid(final String userName, final String pass, final String openid) {
		final Result rs = new Result();	
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							// 先判断是否需要解绑
							String sql = "select id from tblEmployee where openid = ?";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, openid);
							ResultSet rset = ps.executeQuery();
							if (rset.next()) { 
								// 解绑
								sql = "update tblEmployee set openid = ? where id = ?";
								ps = conn.prepareStatement(sql);
								ps.setString(1, null);
								ps.setString(2, rset.getString("id"));
								ps.execute();
							}
							// 重新绑定微信
							sql = "update tblEmployee set openid = ? where sysName = ? and password = ?";
							ps = conn.prepareStatement(sql);
							ps.setString(1, openid);
							ps.setString(2, userName);
							ps.setString(3, pass);
							ps.execute();					
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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
