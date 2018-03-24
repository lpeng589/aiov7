package com.koron.oa.publicMsg.newordain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.Employee;
import com.koron.oa.bean.OAOrdainBean;
import com.koron.oa.bean.OAOrdainGroupBean;
import com.koron.oa.util.EmployeeMgt;
import com.menyi.aio.bean.EmployeeBean;

import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;

/**
 * 
 * <p>
 * Title:规章制度数据库操作类
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2012-6-5
 * @Copyright: 科荣软件
 * @Author 李文祥
 */
public class OAOrdainMgt extends AIODBManager {

	GlobalsTool gt=new GlobalsTool();
	EmployeeMgt EmpMgt=new EmployeeMgt();
	
	/**
	 * 新增规章制度
	 * 
	 * @param oanews
	 * @return
	 */
	public Result addOrdain(final OAOrdainBean oaOrdain) {
		return addBean(oaOrdain);
	}

	/**
	 * 删除规章制度
	 * 
	 * @return
	 */
	public Result deleteOrdain(String[] keyIds) {
		return deleteBean(keyIds, OAOrdainBean.class, "id");
	}

	/**
	 * 修改规章制度
	 * 
	 * @param keyids
	 * @return
	 */
	public Result updateOrdain(final OAOrdainBean oaOrdain) {
		return updateBean(oaOrdain);
	}

	/**
	 * 根据规章制度ID查询规章制度
	 * 
	 * @param oanews
	 * @return
	 */
	public Result loadOrdain(final String newsId) {
		return loadBean(newsId, OAOrdainBean.class);
	}
    
	/**
	 * 根据组ID查询规章制度信息
	 * @param groupId
	 * @return
	 */
	public Result queryOrdainBygroup(final String groupId){
		List<String> param=new ArrayList<String>();
		String hql = "from OAOrdainBean bean where bean.groupId like ?";
		param.add(""+groupId+"%");
		return this.list(hql, param);
	}
	/**
	 * 根据规章制度组ID查询规章组
	 * 
	 * @param oanews
	 * @return
	 */
	public Result loadOrdainGroup(final String groupId) {
		return loadBean(groupId, OAOrdainGroupBean.class);
	}
	/**
	 * 查询规章制度(包含按规章制度组、关键检索、关键字、多条件查询、上一条、下一条)
	 * @param form
	 * @param userId
	 * @return
	 */
	public Result queryOrdain(final OAOrdainSearchForm form,final String createTime ,final String userId,final String depts ,final String querytype){
		//创建参数
		List<String> param = new ArrayList<String>();
		String type=form.getSelectType();
		String hql = "from OAOrdainBean bean where 1=1 ";
		if("detailPre".equals(querytype)){ //详细页面上一条
			 hql +=" and bean.lastupDateTime>? ";
			 param.add(createTime);
		}
		else if("detailNext".equals(querytype)){ //详细页面上一条
			 hql +=" and bean.lastupDateTime<? ";
			 param.add(createTime);
		}
		
		if("type".equals(type)){   //按规章制度类型查询
			String typeId=form.getSelectId();
			hql += "and bean.groupId like ? ";
			param.add(""+typeId+"%");
		
		}
		if("time".equals(type)){   //按关键检索查询
			int timeId=Integer.parseInt(form.getSelectId());
			switch (timeId) {
			case 1: // 一天以内
				hql += " and DateDiff(day,bean.createTime,getdate())=0 ";
				break;
			case 2: // 一周以内
				hql += " and DateDiff(day,bean.createTime,getdate())<=6 ";
				break;
			case 3: // 一个月以内
				hql += " and DateDiff(day,bean.createTime,getdate())<=30 ";
				break;
			case 4: // 三个月以内
				hql += " and DateDiff(month,bean.createTime,getdate())<=3";
				break;
			case 5: // 三个月以外
				hql += " and DateDiff(month,bean.createTime,getdate())>3 ";
				break;
			}			
		}
		if("gaoji".equals(type)){  //多条件查询
			if (form.getGroupId() != null && form.getGroupId().length() > 0) {
				hql += " and bean.groupId=? ";
				param.add(form.getGroupId());
			}
			if (form.getOrdainTitle() != null && form.getOrdainTitle().length() > 0) {
				hql += " and bean.ordainTitle like ? ";
				param.add("%" + form.getOrdainTitle() + "%");
			}
			if (form.getCreateBy() != null
					&& form.getCreateBy().trim().length() > 0) {
				hql += " and bean.createBy=? ";
				param.add(form.getCreateBy());
			}
			if( !"".equals(form.getBeginTime())  && !"".equals(form.getBeginTime()) ){
				if(form.getBeginTime().equals(form.getEndTime())){
					hql += " and bean.createTime like ?";
					param.add(""+form.getBeginTime()+"%");
				}
				else{
					if (form.getBeginTime().trim().length() > 0) {
						hql += " and bean.createTime > ?";
						param.add(form.getBeginTime());
					}
					if (form.getEndTime().trim().length() > 0) {
						hql += " and bean.createTime < ?";
						param.add(form.getEndTime()+ " 23:59:59");
					}
				}
		    }  
		}
		if("keyword".equals(type)){ //关键字查询			
			if(form.getKeyWord()!=null && form.getKeyWord().length()>0){			 			
				hql +=" and( bean.ordainTitle like '%'+?+'%' or bean.groupId like '%'+?+'%' or bean.content like '%'+?+'%') ";
				param.add(form.getKeyWord());
				param.add(form.getKeyWord());
				param.add(form.getKeyWord());
			}
		}
		
		if (!"1".equals(userId)) { //登陆用户权限判断
			BaseEnv.log.error("此时的dept为:"+depts);
			List list = (List)this.queryFolderByUserId(userId, depts).getRetVal();
			String folderTree="";
			for(int i=0;i<list.size();i++){
				String []obj=(String[])list.get(i);   				
					folderTree+=obj[1]+",";
			}
			String m="";
			if(folderTree!=null )
				m=this.dealString(folderTree);  //获取登陆用户可以查看的规章制度组的组Id
			if("".equals(m))     //如果登陆用户没有可以查看的规章制度组的组,则证明登陆用户不能查看任何的规章制度信息
				m="'123'";
			hql += " and bean.groupId in ("+ m +") and ( bean.createBy=? or ( bean.isAlonePopedmon='0' or bean.popedomUserIds like ?   ";

			param.add(userId);
			param.add("%" +userId+ "%");
			if (depts.length()>=5){
				for(int i=0;i<depts.length()/5;i++){
					String d=depts.substring(0,depts.length()-5*i);
					hql += " or bean.popedomDeptIds like ? ";
					param.add("%"+ d +",%");
				}
			}
			hql +=" ))";
		}
		hql += " order by bean.lastupDateTime desc";
		return list(hql, param, form.getPageNo(), form.getPageSize(), true);
	}

	/**
	 * 处理字符串，例如:将"1;2;3"处理成"'1','2','3'"
	 * @param tree
	 * @return
	 */
	public String dealString(String tree){
		String[] list = tree.split(",");
		List<String> newlist=new ArrayList<String>();
		for (int i=0; i < list.length; i++) {
			if(list[i].length()==5){
				newlist.add(list[i]);
			}
			for(int j=0;j<newlist.size();j++){
				for (int m=0; m < list.length; m++) {
					if((!newlist.contains(list[m]) && newlist.get(j).length() + 5 == list[m].length() && list[m].substring(0,list[m].length()-5).equals(newlist.get(j)))) {
						newlist.add(list[m]);
						break;
					}
				}
			}
			
		}
		String mm="";
		for(int k=0;k<newlist.size();k++)
			if(k==newlist.size()-1)
				mm+="'"+newlist.get(k)+"'";
			else
				mm+="'"+newlist.get(k)+"'"+",";
		return mm;
	}
	
	/**
	 * 获取通知对象
	 */
	public String getPopedomUser(OAOrdainBean oaOrdain,String userId) {
		String popedomUserIds = ""; // 通知对象
		String popedomUserId = oaOrdain.getPopedomUserIds();
		if ("0".equals(oaOrdain.getIsAlonePopedmon())) { // 判断是否单独授权
			// 设置通知对象为所有人
			List listEmp = (List) EmpMgt.sel_allEmployee().getRetVal();
			for (int i = 0; i < listEmp.size() ; i++) { // 循环发送		
					popedomUserIds += String.valueOf(listEmp.get(i)) + ",";
			}
		} else {
			// 选择个人
			if (oaOrdain.getPopedomUserIds() != null && oaOrdain.getPopedomUserIds().length() > 0) {
				String [] popeUser=popedomUserId.split(",");
				for(int i=0;i<popeUser.length;i++)			
					popedomUserIds += popeUser[i]+",";
			}

			// 选择部门
			if (null != oaOrdain.getPopedomDeptIds()
					&& !"".equals(oaOrdain.getPopedomDeptIds())) {
				String[] deptIds = oaOrdain.getPopedomDeptIds().split(","); // 根据部门编号查找部门人员
				for (String departId : deptIds) {
					List<Employee> list_emp = EmpMgt.queryAllEmployeeByClassCode(departId);
					for (Employee emp : list_emp) {
						if (!popedomUserId.contains(emp.getid())) {// 判断是否已经单独授权
								popedomUserIds += emp.getid() + ",";
						}
					}
				}
			}
			// 选择职员分组
			if (null != oaOrdain.getPopedomEmpGroupIds() && !"".equals(oaOrdain.getPopedomEmpGroupIds())) {
				String[] empGroupIds = oaOrdain.getPopedomEmpGroupIds()
						.split(","); // 根据分组查找分组人员
				for (String empGroup : empGroupIds) {
					List list = EmpMgt.queryAllEmployeeByGroup(empGroup);
					for (int i = 0; i < list.size(); i++) {
						if (!popedomUserIds.contains(list.get(i).toString())) {// 判断是否已经单独授权
							popedomUserIds += list.get(i).toString() + ",";
						}
					}
				}
			}
		}
		String popeUser="";
		//判断通知对象是否包含当前用户，不需要向自己发送通知消息
		if(popedomUserIds!=null && popedomUserIds.length()>0){
			String [] popedomUser=popedomUserIds.split(",");
			for(String pope:popedomUser){
				if(!pope.equals(userId))
					popeUser +=pope+",";
			}
			
		}
		return popeUser;
	}

	/**
	 * 根据登陆用户查询所属组
	 * @param loginBean
	 * @return
	 */
	public Result queryFolderByUserId(final String userId, final String depts) {
			final Result rst = new Result();
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection connection)
								throws SQLException {
							Connection conn = connection;
							String sql = "select b.id,b.classCode,b.GroupName,b.Description,b.isCatalog from OAOrdainGroup b left join tblEmployee emp on emp.id=b.createBy " +
									"where b.classCode is not null and ";
							
							String deptCodes = depts;
							sql += " ('1'='"+userId+"' or (b.popedomUserIds='' and b.popedomDeptIds='') or b.createBy='"+userId+"' or b.popedomUserIds like '%"+userId+"%' ";
							if(deptCodes.length()>0){
								sql += " or (";
									while(deptCodes.length()>0){
										
										sql += "','+"+"b.popedomDeptIds"+"+',' like '%,"+deptCodes+",%'";
										deptCodes = deptCodes.substring(0,deptCodes.length()-5);
										if(deptCodes.length()!=0){
											sql+=" or ";
										}
									}	
								sql+=" )";
							}
							sql+="  )order by b.classCode";
							
							try {
								PreparedStatement ps = conn.prepareStatement(sql) ;
								ResultSet rs = ps.executeQuery();
								ArrayList list=new ArrayList();
								while(rs.next()){
									String []str=new String[5];
									str[0]=rs.getString(1);
									str[1]=rs.getString(2);
									str[2]=rs.getString(3);
									str[3]=rs.getString(4);
									str[4]=rs.getString(5);
									list.add(str);
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
		 * 根据classCode查询
		 * @param folderId
		 * @return
		 */
		public Result queryFolderByCode(String folderId){
			List<String> param = new ArrayList<String>();
			String hql = "from OAOrdainGroupBean bean where bean.classCode=?";
			param.add(folderId);
			return list(hql, param);
		}
		
	 /**
		 * 查询目录
		 * @param folderId
		 * @return
		 */
		public Result queryFolder(String folderId,String type,String operation){
			List<String> param = new ArrayList<String>();
			String hql = "from OAOrdainGroupBean bean where 1=1";
		
			if("enter".equals(operation)){ 
				if("1".equals(type) && type != null){
					hql += " and len(bean.classCode)=5";
				}else{
					hql += " and bean.classCode like '"+folderId+"_____'";
				}
			}
			else {
				hql += " and bean.classCode like ? ";
				param.add("" + folderId + "%");
				
			}
			
			hql += " order by bean.listOrder asc";
			return list(hql, param);
		}
		/**
		 * 更新下级目录的权限
		 * @param ClassCode
		 * @param oabean
		 * @return
		 */
		public Result updateGroupByClassCode(final String ClassCode,final String dept,final String user,final Boolean changedept,final Boolean changeuser) {
		       final Result rst = new Result();
		       int retCode = DBUtil.execute(new IfDB() {
		           public int exec(Session session) {
		               session.doWork(new Work() {
		                   public void execute(Connection conn) throws
		                           SQLException {
		                       try {
		                           PreparedStatement pstmt = null;  
		                           String strsql="update OAOrdainGroup set ";
		                           if(changedept==true && changeuser==false){
		                        	   strsql += " popedomDeptIds='"+dept+"' ";
		                           }
		                           if(changeuser==true && changedept==false){
		                        	   strsql += " PopedomUserIds='"+user+"' ";
		                           }
		                           if(changeuser==true && changedept==true){
		                        	   strsql+= " opedomUserIds='"+dept+"',popedomDeptIds= '"+user+"'";
		                           }
		                           strsql+= "where classCode in ("+ClassCode+")";
		                           
			                       pstmt= conn.prepareStatement(strsql); 
			                      
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
		 * 加载组
		 * @param id
		 * @return
		 */
		public Result loadGroup(String id){
			return loadBean(id, OAOrdainGroupBean.class);
		}
		
		/**
		 * 根据classCode查询组
		 * @param classCode
		 * @return
		 */
		public Result loadGroupByClassCode(String classCode){
			List<String> param=new ArrayList<String>();
			String hql="from OAOrdainGroupBean bean where bean.classCode= ?";
			param.add(classCode);
			return list(hql,param);
		}
		/**
		 * 添加组
		 * @param bean
		 * @return
		 */
		public Result addGroup(OAOrdainGroupBean bean){
			return addBean(bean);
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
		 * 获取授权部门和个人
		 * @param deptStr
		 * @param userStr
		 * @return
		 */
		@SuppressWarnings({ "unchecked", "unchecked" })
		public String getShouquan(String deptStr,String userStr){
		
			/*获取上级目录授权的部门*/
			String deptlist="";
			/*获取上级目录授权的职员*/
			String Userlist="";
			
			String deptNames="";
			
			List<Object[]> Deptlist=null;
			String[] User=userStr.split(",");
			
			if(!"".equals(deptStr)){
				
				/*获取上级目录授权的部门的名称*/
				Result deptInfo=this.getDeptInfo(deptStr);
				if(deptInfo.retCode==ErrorCanst.DEFAULT_SUCCESS){
					Deptlist=(List<Object[]>)deptInfo.retVal;
					for(int k=0;k<Deptlist.size();k++){
						Object[] depts=Deptlist.get(k);
						deptNames+=depts[2]+",";
						
					}
				}
					
			//	获取上级目录授权的部门及下级部门
				Result allDept=this.getDept(deptStr);
				if(allDept.retCode==ErrorCanst.DEFAULT_SUCCESS){
					Deptlist=(List<Object[]>)allDept.retVal;
					for(int k=0;k<Deptlist.size();k++){
						Object[] depts=Deptlist.get(k);
						if(k==Deptlist.size()-1 )
							deptlist+="'"+depts[14]+"'";
						else
							deptlist+="'"+depts[14]+"'"+",";
						
					}
				}
			/*	
				//获取上级目录授权的部门的所有职员				
				List<EmployeeBean> Emplist=null;
				Result allEmp=queryEmpByDeptCode(deptlist);
				if(allEmp.retCode==ErrorCanst.DEFAULT_SUCCESS){
					Emplist=(List<EmployeeBean>) allEmp.retVal;				
					for(int k=0;k<Emplist.size();k++)					
						if(k==Emplist.size()-1 && User.length<1)
							Userlist+="'"+Emplist.get(k).getId()+"'";
						else
							Userlist+="'"+Emplist.get(k).getId()+"'"+",";
				}
			*/	
			}
			/*
			String persons = "";
	     	//获取父级目录选择的授权个人
			int strlen=User.length;
		    for(int k=0;k<User.length;k++){
		    	if((Userlist.length()>0 && Userlist.indexOf("'"+User[k]+"'") < 0 ) || Userlist.length()==0 ){ //如果所选部门中不包含授权个人，则添加到字符串中	
			    	strlen--;
		    		if(k==User.length-1) {
						Userlist+="'"+User[k]+"'";
					} else
						Userlist+="'"+User[k]+"'"+",";
		    	}
		    	
		    }
		    
		
		    if(Userlist.length()>0){		    	
		    	if(Userlist.lastIndexOf(",")+1==Userlist.length()){      //判断选择的部门是否包含所有选择的个人
		    		Userlist = Userlist.substring(0,Userlist.length()-1);
		    	}
		    }
		*/
			
			//获得上下级部门的所有人数
			List<EmployeeBean> Emplist=null;
			if(deptlist != null && !deptlist.equals("")){
				Result allEmp=queryEmpByDeptCode(deptlist);
				Emplist=(List<EmployeeBean>) allEmp.retVal;
				
				int temp = 0;//用于标识传递的用户ID参数是否与部门里面的用户ID,默认0表示不等
				for(int i =0;i<User.length;i++){
					for(int j=0;j<Emplist.size();j++){
						if(User[i].equals(Emplist.get(j).getId())){
							temp = 1;//相等时标识设为1
						}
						//标识等于1时跳出循环
						if(temp == 1){
							break;
						}
					}
					//temp=0标识用户ID不再部门的用户ID里
					if(temp == 0) {
						Userlist += User[i] + ",";
					}
					temp = 0;//最后把temp设为0
				}
				
			}else{
				for(int i=0;i<User.length;i++){
					Userlist += User[i] + ",";
				}
			}
			
			
		    if("".equals(deptStr)) { //判断部门是否为空
		    		deptlist="''"; 
		    	//	deptNames="null";
		    		deptNames="''";
		    }
		    if("".equals(Userlist)) //判断个人是否为空
	    			Userlist="''";
		   
			return deptlist+"%%"+Userlist+"%%"+deptNames;
		}
		
		
		/**
		 * 根据部门Id获取子级部门
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
		 * 修改组是否存在下级
		 * @param classCode
		 * @return
		 */
		public Result updateIsCatalog(final String classCode,final String type) {
		       final Result rst = new Result();
		       int retCode = DBUtil.execute(new IfDB() {
		           public int exec(Session session) {
		               session.doWork(new Work() {
		                   public void execute(Connection conn) throws
		                           SQLException {
		                       try {
		                           PreparedStatement pstmt = null;
		                           if("add".equals(type)) //增加目录时修改IsCatalog值
			                           pstmt= conn.prepareStatement("update OAOrdainGroup set isCatalog=1 where classCode=?"); 
		                           else  //删除目录时修改IsCatalog值            
		                        	   pstmt= conn.prepareStatement("update OAOrdainGroup set isCatalog=0 where classCode=?"); 
		                           
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
		public Result updateGroup(OAOrdainGroupBean bean){
			return updateBean(bean);
		}
		
		/**
		 * 删除组
		 * @param id
		 * @return
		 */
		public Result delGroup(String[] keyId){
			return deleteBean(keyId, OAOrdainGroupBean.class, "id");
		}
		

		/**
		 * 根据folderId清空所有的File
		 * @param classCode
		 * @return
		 */
		public Result deleteFiles(final String classCode) {
		       final Result rst = new Result();
		       int retCode = DBUtil.execute(new IfDB() {
		           public int exec(Session session) {
		               session.doWork(new Work() {
		                   public void execute(Connection conn) throws
		                           SQLException {
		                       try {
		                           PreparedStatement pstmt= conn.prepareStatement("delete OAOrdainGroup where classCode like ? and len(classCode) > len(?)"); 
		                           pstmt.setString(1, classCode+"%");
		                           pstmt.setString(2, classCode);
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
		

}
