package com.koron.oa.publicMsg.knowledgeCenter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.Department;
import com.koron.oa.bean.OAKnowFileBean;
import com.koron.oa.bean.OAKnowFolderBean;
import com.koron.oa.bean.OAKnowledgeCenterFile;
import com.koron.oa.publicMsg.newordain.OAOrdainMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;


/**
 * 
 * 
 * <p>Title:知识中心Mgt</p> 
 * <p>Description: </p>
 *
 * @Date:2012-6-13
 * @Copyright: 科荣软件
 * @Author 方家俊
 */
public class OAKnowCenterMgt extends AIODBManager{
	
	OAOrdainMgt omgt=new OAOrdainMgt();
	/**
	 * 查询所有的组
	 * @return
	 */
	public Result queryGroup(){
		List param = new ArrayList();
		String hql = "from OAKnowFolderBean bean";
		return list(hql, param);
	}
	
	/**
	 * 添加
	 * @param bean
	 * @return
	 */
	public Result addFile(OAKnowFileBean bean){
		return addBean(bean);
	}
	
	/**
	 * 根据id加载信息
	 * @param fileId
	 * @return
	 */
	public Result loadFile(String fileId){
		return loadBean(fileId, OAKnowFileBean.class);
	}
	
	/**
	 * 修改
	 * @param bean
	 * @return
	 */
	public Result updateFile(OAKnowFileBean bean){
		return updateBean(bean);
	}
	
	/**
	 * 删除
	 * @param fileId
	 * @return
	 */
	public Result deleteFile(String[] fileId){
		return deleteBean(fileId, OAKnowFileBean.class, "id");
	}
	
	
	/**
	 * 查询知识中心的列表
	 * @param folderCode
	 * @param userId
	 * @param deptCode  部门ID
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Result queryKnowFile(final String folderCode, final String userId, final String deptCode, final String keyWord, final String queryType, final int term,
				final String fileTitleSearch, final String createBySearch, final String propUserName, final String beginTimeSearch, final String endTimeSearch,
				final String groupIdSearch, final String groupName, final int pageNo,final int pageSize,final String isEtypq,final String lastTime){
		String sql="select  a.*,emp.EmpFullName,b.folderName,ROW_NUMBER() over(order by a.lastUpdateTime desc) " +
		"as row_id from OAKnowledgeCenterFile a left join OAKnowledgeCenterFolder b on " +
		"a.folderId = b.classCode left join tblEmployee emp on emp.id=a.createBy where 1=1";

		//整个目录是否有权限查看
		String deptCodes = deptCode;
		sql += " and ('1'='"+userId+"' or (b.popedomUserIds='' and b.popedomDeptIds='')  or b.createBy='"+userId+"' or b.popedomUserIds like '%"+userId+"%' ";
		sql += " or (" ;
		while(deptCodes.length()>0){
			sql += "','+"+"b.popedomDeptIds"+"+',' like '%,"+deptCodes+",%'";
			deptCodes = deptCodes.substring(0,deptCodes.length()-5);
			if(deptCodes.length()!=0){
				sql+=" or ";
			}
		}
		deptCodes = deptCode;
		sql += ")) and ('1'='"+userId+"' or a.createBy='"+userId+"' or a.isAlonePopedom = '0' or a.popedomUserIds like '%"+userId+"%' ";
		sql += " or (" ;
		while(deptCodes.length()>0){
			sql += "','+"+"a.popedomDeptIds"+"+',' like '%,"+deptCodes+",%'";
			deptCodes = deptCodes.substring(0,deptCodes.length()-5);
			if(deptCodes.length()!=0){
				sql+=" or ";
			}
		}
		sql += ")) ";
		if(queryType !=null && "number".equals(queryType)){
			//选择单个组或者单个有下级的组
			if(null!=folderCode && folderCode.length()>0){
		         sql += " and a.folderId like '"+folderCode+"%'";
		    }
		}else if(queryType !=null && "time".equals(queryType)){
			//关键检索
			switch (term) {
			case 1: // 一天以内
				sql += " and DateDiff(day,a.createTime,getdate())=0 ";
				break;
			case 2: // 一周以内
				sql += " and DateDiff(day,a.createTime,getdate())<=6 ";
				break;
			case 3: // 一个月以内
				sql += " and DateDiff(day,a.createTime,getdate())<=30 ";
				break;
			case 4: // 三个月以内
				sql += " and DateDiff(month,a.createTime,getdate())<=3";
				break;
			case 5: // 三个月以外
				sql += " and DateDiff(month,a.createTime,getdate())>3 ";
			}
		}else if(queryType != null && "keyword".equals(queryType)){
			
			//条件检索
			if( !"".equals(beginTimeSearch) && beginTimeSearch != null){
				if(beginTimeSearch.equals(endTimeSearch)){
					sql += " and a.createTime like '"+beginTimeSearch+"%'";
				}else{
					if(beginTimeSearch.trim().length() > 0) {
						sql += " and a.createTime > '"+beginTimeSearch+"'";
					}
					if(endTimeSearch.trim().length() > 0) {
						sql += " and a.createTime < '"+endTimeSearch+" 23:59:59'";
					}
				}
		    }
			if(!"".equals(fileTitleSearch) && fileTitleSearch != null){
				sql += " and a.fileTitle like '%" +fileTitleSearch+"%'";
			}
			if(createBySearch.trim().length() > 0 && createBySearch != null){
				sql += " and a.createBy='"+createBySearch+"'";
			}
			if(propUserName.trim().length() > 0 && propUserName != null ){
				sql += " and (emp.empFullName like '%"+propUserName+"%'";
				sql += " or emp.empFullNamePYM like '%"+propUserName+"%')";
			}
			if(groupIdSearch != null && groupIdSearch.trim().length()>0){
				sql += " and a.folderId='"+groupIdSearch+"'";
			}
			if(groupName != null && groupName.trim().length()>0){
				sql += " and b.folderName like '%"+groupName+"%'";
			}
			
		}else if(queryType != null && "key".equals(queryType)){
			//关键字检索
			if(keyWord != null && keyWord.length()>0){			 			
				sql +=" and (a.fileTitle like '%" +keyWord+ "%' or a.fileName like '%" +keyWord+ "%' or a.description like '%" +keyWord+ "%' or a.createBy like '%" +keyWord+ "%')";
			}
		}
		if("detailPre".equals(isEtypq)){ //详细页面上一条
			 sql +=" and a.lastUpdateTime> '"+lastTime+"'";
		}
		else if("detailNext".equals(isEtypq)){ //详细页面上一条
			 sql +=" and a.lastUpdateTime< '"+lastTime+"'";
		}
	
		return sqlListMaps(sql, null, pageNo, pageSize);
	}
	
	/**
	 * 详情
	 * @param fileId
	 * @return
	 */
	public Result detailFile(String fileId){
		return loadBean(fileId, OAKnowFileBean.class);
	}
	
	/**
	 * 查询自己有权限查看的组
	 * @param loginBean
	 * @return
	 */
	public Result queryFolderUser(LoginBean loginBean){
 		List param = new ArrayList();
		String hql = "from OAKnowFolderBean bean where bean.classCode is not null and" +
				" ('1'='"+loginBean.getId() +"' or (bean.popedomUserIds='' and bean.popedomDeptIds='') or bean.createBy='"+loginBean.getId()+"' or (bean.popedomUserIds like '%"+loginBean.getId()+"%' )";
		String deptCode=loginBean.getDepartCode();
		hql += " or (";
		while(deptCode.length()>0){
			hql += "','+"+"bean.popedomDeptIds"+"+',' like '%,"+deptCode+",%'";
			deptCode = deptCode.substring(0,deptCode.length()-5);
			if(deptCode.length()!=0){
				hql+=" or ";
			}
		}
		hql += ")) order by bean.listOrder asc";
		return list(hql, param);
	}
	
	/**
	 * 根据classCode查询
	 * @param folderId
	 * @return
	 */
	public Result quertFolderCode(String folderId){
		List param = new ArrayList();
		String hql = "from OAKnowFolderBean bean where bean.classCode=?";
		param.add(folderId);
		return list(hql, param);
	}

	
	/**
	 * 查询目录
	 * @param folderId
	 * @return
	 */
	public Result quertFolder(String folderId,String type){
		List param = new ArrayList();
		String hql = "from OAKnowFolderBean bean where 1=1";
		if("1".equals(type) && type != null){
			hql += " and len(bean.classCode)=5";
		}else if("delete".equals(type) && type != null){
			hql += " and bean.classCode like '"+folderId+"%'";
		}else{
			hql += " and bean.classCode like '"+folderId+"_____'";
		}
		hql += " order by bean.listOrder asc";
		return list(hql, param);
	}
	
	/**
	 * 根据组ID查询知识中心信息
	 * @param groupId
	 * @return
	 */
	public Result queryKnowBygroup(final String classCode){
		List<String> param=new ArrayList<String>();
		String hql = "from OAKnowFileBean bean where bean.folderId like ?";
		param.add(""+classCode+"%");
		return this.list(hql, param);
	}
	
	/**
	 * 加载组
	 * @param id
	 * @return
	 */
	public Result loadGroup(String id){
		return loadBean(id, OAKnowFolderBean.class);
	}
	
	/**
	 * 添加组
	 * @param bean
	 * @return
	 */
	public Result addGroup(OAKnowFolderBean bean){
		return addBean(bean);
	}
	
	
	/**
	 * 获取通知部门
	 * @param oanewsBean
	 * @return
	 */
//	 根据ID查询职员信息
	public List getDepartmentByClassCode(final String classCode) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						String sql = "select id,DeptFullName,DeptCode,classCode from tblDepartment where classCode like ? ";
						try {
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, classCode+"%");
							List list = new ArrayList();
							ResultSet rs = ps.executeQuery();
							while (rs.next()) {
								Department dept = new Department();
								dept.setid(rs.getString("id"));
								dept.setDeptFullName(rs.getString("DeptFullName"));
								dept.setDeptCode(rs.getString("DeptCode"));
								dept.setclassCode(rs.getString("classCode")) ;
								rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
								list.add(dept);
							}
							rst.setRetVal(list);
						} catch (Exception ex) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + sql, ex);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		List lists = null;
		if (retCode == ErrorCanst.DEFAULT_SUCCESS) {
			lists = (ArrayList) rst.getRetVal();
		}
		return lists;
	}
	
	/**
	 * 修改组是否存在下级
	 * @param classCode
	 * @return
	 */
	public Result updateIsCatalog(final String classCode,final Integer type) {
	       final Result rst = new Result();
	       int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) throws
	                           SQLException {
	                       try {
	                           PreparedStatement pstmt = null;
	                           if(type == 1){
	                        	   pstmt= conn.prepareStatement("update OAKnowledgeCenterFolder set isCatalog=1 where classCode=?"); 
	                           }else if(type == 2){
	                        	   pstmt= conn.prepareStatement("update OAKnowledgeCenterFolder set isCatalog=0 where classCode=?"); 
	                           }
	                           
	                           pstmt.setString(1, classCode);
	                           int row = pstmt.executeUpdate();
	                           if (row > 0) {
	                              rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
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
	      return rst;
	 }
	/**
	 * 修改组
	 * @param bean
	 * @return
	 */
	public Result updateGroup(OAKnowFolderBean bean){
		return updateBean(bean);
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public Result delGroup(String[] ids){
		return deleteBean(ids, OAKnowFolderBean.class, "classCode");
	}
	
	
	/**
	 * 根据folderId清空所有的File
	 * @param classCode
	 * @return
	 */
	public Result deleteFiles(final String[] classCode) {
	       final Result rst = new Result();
	       int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) throws
	                           SQLException {
	                       try {
	                    	   Statement state = conn.createStatement() ;
	                    	   for(String id : classCode){
	                    		   String sql = "delete OAKnowledgeCenterFile where folderId like '"+id+"%'"; 
		                           state.addBatch(sql) ;
		                           sql = "delete OAKnowledgeCenterFolder where classCode like '"+id+"%' and classCode!='"+id+"'";
		                           state.addBatch(sql);
		                           sql = "update OAKnowledgeCenterFolder set isCatalog=0 where classCode='"+id+"'";
		                           state.addBatch(sql);
	                    	   }
	                           state.executeBatch() ;
	                      }catch (Exception ex) {
								rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								ex.printStackTrace();
								BaseEnv.log.error("OABBSForumMgt moveForum : ", ex) ;
								return;
	                      }
	                  }
	              });
	              return rst.getRetCode();
	          }
	      });
	      rst.setRetCode(retCode);
	      return rst;
	 }
	
	/**
	 * 根据folderId清空所有的目录
	 * @param classCode
	 * @return
	 */
	public Result deleteFolder(final String classCode) {
	       final Result rst = new Result();
	       int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) throws
	                           SQLException {
	                       try {
	                           PreparedStatement pstmt= conn.prepareStatement("delete OAKnowledgeCenterFolder where classCode like ?"); 
	                           pstmt.setString(1, classCode+"%");
	                           int row = pstmt.executeUpdate();
	                           if (row >= 0) {
	                              rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
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
	      return rst;
	 }
	
	/**
	 * 获取授权部门和个人
	 * @param deptStr
	 * @param userStr
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unchecked" })
	public String getShouquan(String deptStr,String userStr){
		String str="";
		return str=omgt.getShouquan(deptStr, userStr);
	}
	
	/**
	 * 根据部门classCode获取子级部门
	 * @param deptCode
	 * @return
	 */
	public Result getDept(final String deptCode){
		String[] deptlist=deptCode.split(",");
		List<String> param=new ArrayList<String>();
		String sql="select * from tbldepartment where classCode like ? ";
		param.add(""+deptlist[0]+"%");
		for(int i=1;i<deptlist.length;i++){
			sql+= " or classCode like ? ";
			param.add(""+deptlist[i]+"%");
		}
		
		return this.sqlList(sql, param);
	}
	
	/**
	 * 根据部门classCode获取部门信息
	 * @param deptCode
	 * @return
	 */
	public Result getDeptInfo(final String deptCode){
		String[] deptlist=deptCode.split(",");
		List<String> param=new ArrayList<String>();
		String sql="select * from tbldepartment where classCode =? ";
		param.add(""+deptlist[0]+"");
		for(int i=1;i<deptlist.length;i++){
			sql+= " or classCode = ? ";
			param.add(""+deptlist[i]+"");
		}
		
		return this.sqlList(sql, param);
	}
	
	/**
	  * 根据部门ClassCode查询部门下的所有职员
	  * @param deptCode
	  * @return
	  */
	public Result queryEmpByDeptCode(String deptCode){			
		String hql="from EmployeeBean bean where bean.DepartmentCode in ("+ deptCode +")";			
		return list(hql, null);
	}
	
	/**
	 * 知识中心查询
	 * @param folderCode
	 * @param userId
	 * @param deptCode
	 * @param empGroupId
	 * @param keyWord
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Result query(final String folderCode,final String userId,final String deptCode,final String empGroupId,final String keyWord,final int pageNo,final int pageSize) {
   	 	String temDeptCode=deptCode;
        StringBuffer sql = new StringBuffer("select  a.id,a.folderId,a.fileName,a.FilePath,a.Description,emp.EmpFullName,a.createTime,b.folderName,a.createBy,a.fileTitle,a.IsAlonePopedom,a.PopedomUserIds,a.PopedomDeptIds,a.popedomEmpGroupIds,a.isSaveReading,ROW_NUMBER() over(order by a.lastUpdateTime desc) as row_id from OAKnowledgeCenterFile a ");
        sql.append("left join OAKnowledgeCenterFolder b on a.folderId = b.classCode left join tblEmployee emp on emp.id=a.createBy where 1=1");
        
        if(null!=folderCode && folderCode.length()>0){
        	sql.append("and a.folderId like '"+folderCode+"%'");
         }
        sql.append("and ('1'='"+userId+"' or a.createBy='"+userId+"' or (");
        //目录权限
        sql.append("(b.popedomUserIds like '%"+userId+"%' or ','+b.popedomDeptIds+',' like '%,"+temDeptCode+",%'");
        while(temDeptCode.length()>5){
        	temDeptCode=temDeptCode.substring(0,temDeptCode.length()-5);
        	sql.append(" or ','+b.popedomDeptIds+',' like '%,"+temDeptCode+",%'");
        }
        temDeptCode=deptCode;
        //文件独立授权
        sql.append(") and (a.isAlonePopedom = '0' or a.popedomUserIds like '%"+userId+"%' or ','+a.popedomDeptIds+',' like '%,"+temDeptCode+",%'");
        while(temDeptCode.length()>5){
        	temDeptCode=temDeptCode.substring(0,temDeptCode.length()-5);
        	sql.append(" or ','+a.popedomDeptIds+',' like '%,"+temDeptCode+",%'");
        }
        if(empGroupId.length()>0){
        	String []groups=empGroupId.split(";");
        	for(int i=0;i<groups.length;i++){
        		sql.append(" or a.popedomEmpGroupIds like '%"+groups[i]+"%'");
        	}
        }
        sql.append(")))");
        
        if(keyWord!=null&&!"".equals(keyWord)){
            sql.append(" and (a.fileTitle like '%"+keyWord+"%' or a.Description like'%"+keyWord+"%') ") ;
        }
        AIODBManager aioMgt=new AIODBManager();
        Result rs=aioMgt.sqlListMap(sql.toString(), null, pageNo, pageSize);
        List listMap=(List)rs.retVal;
        List<OAKnowledgeCenterFile> ls = new ArrayList<OAKnowledgeCenterFile>();
        int index = 0 ;
        for(int i=0;i<listMap.size();i++){
       	 HashMap map=(HashMap)listMap.get(i);
       	 OAKnowledgeCenterFile file=new OAKnowledgeCenterFile();
       	 file.setid(map.get("id").toString());
       	 file.setFolderID(map.get("folderId").toString());
       	 file.setDescription(map.get("Description").toString());
       	 file.setFileName(map.get("fileName").toString());
       	 file.setFilePath(map.get("FilePath").toString());
       	 file.setCreateByName(map.get("EmpFullName").toString());
       	 file.setcreateTime(map.get("createTime").toString());
       	 file.setFolderName(map.get("folderName").toString()) ;
       	 file.setFileTitle(map.get("fileTitle").toString());
       	 file.setIsAlonePopedom(Integer.parseInt(map.get("IsAlonePopedom").toString()));
       	 file.setcreateBy(map.get("createBy").toString());
       	 file.setPopedomUserIds(map.get("PopedomUserIds").toString());
       	 file.setPopedomdeptids(map.get("PopedomDeptIds").toString()) ;
       	 file.setPopedomEmpGroupIds(map.get("popedomEmpGroupIds").toString()) ;
       	 file.setIsSaveReading(map.get("isSaveReading").toString()) ;
       	 file.setIndex(index) ;
       	 ls.add(file) ;
            index++ ;         	 
        }
        rs.setRetVal(ls);
        return rs;
    }
	
	public Result getFile(final String id) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							PreparedStatement pstmt = conn.prepareStatement("select * from OAKnowledgeCenterFile where 1=1 and id=?");
							OAKnowledgeCenterFile obj = new OAKnowledgeCenterFile();
							pstmt.setString(1, id);
							ResultSet rs = pstmt.executeQuery();
							if (rs.next()) {
								obj.setid(rs.getString("id"));
								obj.setclassCode(rs.getString("classCode"));
								obj.setFolderID(rs.getString("FolderID"));
								obj.setDescription(rs.getString("Description"));
								obj.setFileName(rs.getString("FileName"));
								obj.setFilePath(rs.getString("FilePath"));
								obj.setIsAlonePopedom(rs.getInt("IsAlonePopedom"));
								obj.setcreateBy(rs.getString("createBy"));
								obj.setlastUpdateBy(rs.getString("lastUpdateBy"));
								obj.setcreateTime(rs.getString("createTime"));
								obj.setlastUpdateTime(rs.getString("lastUpdateTime"));
								obj.setSCompanyID(rs.getString("SCompanyID"));
								obj.setPopedomUserIds(rs.getString("PopedomUserIds"));
								obj.setPopedomdeptids(rs.getString("PopedomDeptIds")) ;
								obj.setPopedomEmpGroupIds(rs.getString("popedomEmpGroupIds")) ;
								obj.setIsSaveReading(rs.getString("isSaveReading")) ;
								obj.setFileTitle(rs.getString("fileTitle")) ;
							}
							rst.setRetVal(obj);
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
		return rst;
	}
}
