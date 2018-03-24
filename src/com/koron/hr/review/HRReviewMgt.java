package com.koron.hr.review;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.bsf.BSFException;
import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.OnlineUserInfo;
/**
 * 
 * <p>Title:</p> 
 * <p>Description: 表现评估</p>
 *
 * @Date:Oct 10, 2011
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class HRReviewMgt extends AIODBManager{

	/**
	 * 查询表现评估
	 * @return
	 */
	public Result queryReview(HRReviewSearchForm form){
		ArrayList param = new ArrayList() ;
		String sql = "select HRReview.id,PeriodBegin,PeriodEnd,EmpFullName,DeptFullName,MyGoal,Competency," +
					 "applyType,currentNode,NextNodeIds,OAMyWorkFlow.departmentCode,checkPerson from HRReview " +
					 "left join OAMyWorkFlow on OAMyWorkFlow.id=HRReview.id " +
					 "left join tblEmployee on tblEmployee.id=HRReview.EmployeeID " +
					 "left join tblDepartment on tblDepartment.classCode=HRReview.DepartmentCode " +
					 "where (HRReview.createBy=? or OAMyWorkFlow.checkPerson like ? " +
					 " or (HRReview.id in (select f_ref from OAMyWorkFlowDet where checkPerson =?))) " ;
		param.add(form.getUserId()) ;
		param.add("%;"+form.getUserId()+";%") ;
		param.add(form.getUserId()) ;
		if(form.getBeginTime()!=null && form.getBeginTime().length()>0){
			sql += " and PeriodBegin>=?" ;
			param.add(form.getBeginTime()) ;
		}
		if(form.getEndTime()!=null && form.getEndTime().length()>0){
			sql += " and PeriodEnd<=?" ;
			param.add(form.getEndTime()) ;
		}
		if(form.getEmpFullName()!=null && form.getEmpFullName().length()>0){
			sql += " and EmpFullName like ?" ;
			param.add("%"+form.getEmpFullName()+"%") ;
		}
		sql += " order by HRReview.createTime desc " ;
		return sqlList(sql, param) ;
	}
	
	
	/**
	 * 根据表名查询流设计ID
	 * @param tableName
	 * @return
	 */
	public Result getWorkFlowDesing(final String tableName){
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try {
                        	Statement st=conn.createStatement(); 
                        	String sql ="select id from OAWorkFlowTemplate where templateFile='"+tableName+"' and fileFinish=1 and templateStatus=1";                        	
                        	ResultSet rst=st.executeQuery(sql);
                        	if(rst.next()){
                        		rs.setRetVal(rst.getString(1)) ;
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
        return rs;
    }
	
	/**
	 * 查询工作流模板 是否过时
	 * @param tableName
	 * @return
	 */
	public Result getFlowTemplate(final String designId){
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try {
                        	Statement st = conn.createStatement();
            				ResultSet rss = st.executeQuery("select allowAdd,usefulLifeS,usefulLifeE from OAWorkFlowTemplate where id='"+designId+"'");
            				String usefulLifeE="";
            				int allowAdd=1;
            				if(rss.next()){
            					allowAdd=rss.getInt(1);
            					usefulLifeE=rss.getString(3);
            				}
            				Date endTime = new Date();
            				if(allowAdd==0 && usefulLifeE!=null&&usefulLifeE.length()>0){
            					Date lifeED=BaseDateFormat.parse(usefulLifeE,BaseDateFormat.yyyyMMdd);
            					if(endTime.compareTo(lifeED)>0){
            						rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
            					}else{
            						rs.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
            					}
            				}else{
        						rs.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
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
        rs.setRetCode(retCode) ;
        return rs;
    }
	
	/**
	 * 根据表名查询流设计ID
	 * @param tableName
	 * @return
	 */
	public Result getNextNodeId(final String tableName,final String designId,
			final String currentId,final LoginBean login){
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                    	OAMyWorkFlowMgt flowMgt = new OAMyWorkFlowMgt() ;
            			try {
            				HashMap values = new HashMap() ;
            				values.put("tblEmployee.EmpFullName", login.getEmpFullName()) ;
							String nextNodeId = flowMgt.getNextNodeIds(designId, currentId, values, "HRReview", conn) ;
							rs.setRetVal(nextNodeId) ;
						} catch (Exception e) {
							e.printStackTrace();
						}
                    }
                });
                return rs.getRetCode();
            }
        });
        return rs;
    }
	
	/**
	 * 工作流 流程描述
	 * @return
	 */
	public String getFlowDepict(final String designId,final String billId,
			final Locale locale,final MessageResources mr) throws SQLException{
		final StringBuilder sbb = new StringBuilder() ;
		/*sbb.append("<table cellpadding=\"0\" cellspacing=\"0\" style=\"margin-left: 100px;\">") ;
		sbb.append("<tr style=\"height: 58px;\">") ;
		final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                    	OAMyWorkFlowMgt flowMgt = new OAMyWorkFlowMgt() ;
            			try {
            				*//****************************查询这个流程是否是否是过时添加的***************************************//*
            				String outTime = "" ;
            				Statement st=conn.createStatement();
            				ResultSet rs=st.executeQuery("select allowAdd,usefulLifeS,usefulLifeE from OAWorkFlowTemplate where id='"+designId+"'");
            				String usefulLifeE="";
            				int allowAdd=1;
            				if(rs.next()){
            					allowAdd=rs.getInt(1);
            					usefulLifeE=rs.getString(3);
            				}
            				Date endTime=null;
            				rs=st.executeQuery("select endTime from oamyworkflow a,oamyworkflowDet b where a.id=b.f_ref and a.id='"+billId+"' and nodeId='0'");
            				if(rs.next()){
            					String temp=rs.getString(1);
            					if(temp==null||(temp!=null&&temp.length()==0)){
            						endTime=new Date();
            					}else{
            						endTime=BaseDateFormat.parse(temp, BaseDateFormat.yyyyMMdd);
            					}
            				}
            				
            				if(usefulLifeE!=null&&usefulLifeE.length()>0){
            					Date lifeED=BaseDateFormat.parse(usefulLifeE,BaseDateFormat.yyyyMMdd);
            					if(endTime.compareTo(lifeED)>0){
            						outTime ="<font style=\"font-weight: bold;color: red;\">("+mr.getMessage(locale,"oa.workflow.msg.outdated")+")</font>&nbsp;&nbsp;";
            					}
            				}
            				WorkFlowDesignBean designBean=BaseEnv.workFlowDesignBeans.get(designId);
            				String flowDepict="";
            				String sql="select a.nodeID,a.checkPerson,a.endTime,isnull((select top 1 endTime+';'+cast(isnull(oaTimeLimitUnit,0) as varchar(2))+';'+cast(isnull(benchMarkTime,0) as varchar(20)) from OAMyWorkFlowDet c where c.f_ref=b.id and c.nodeId!=a.nodeId and c.sortOrder<a.sortOrder and c.statusId=0 order by c.sortOrder desc),'')"+
            					",b.departmentCode,b.checkPerson,b.currentNode,b.lastUpdateTime from OAMyWorkFlowDet a,OAMyWorkFlow b where a.f_ref=b.id and a.statusId=0 and len(a.endTime)>0 and a.f_ref='"+billId+"' order by a.sortOrder";
            				st=conn.createStatement();
            				rs=st.executeQuery(sql);
            				ArrayList dets=new ArrayList();
            				String department="";
            				String currCP="";
            				String currNode="";
            				String lastUpdateTime="";
            				while(rs.next()){
            					String []str=new String[4];
            					str[0]=rs.getString(1);
            					str[1]=rs.getString(2);
            					str[2]=rs.getString(3);
            					str[3]=rs.getString(4);
            					department=rs.getString(5);
            					currCP=rs.getString(6);
            					currNode=rs.getString(7);
            					lastUpdateTime=rs.getString(8);
            					dets.add(str);
            				}
            				for(int i=0;i<dets.size();i++){
            					String []str=(String[])dets.get(i);
            					FlowNodeBean bean=designBean.getFlowNodeMap().get(str[0]);
            					if(bean==null)continue;
            					String display=str[0].equals("0")?"":"("+bean.getDisplay()+")";
            					if(!str[0].equals(currNode)){//非当前节点，就是已办结的节点
            						if(str[3].length()>0&&flowMgt.compareFinishDate(bean, str[2], str[3])>0){
            							sbb.append("<td style=\"width: 70px;background:#ff0000;text-align: center;\">"+bean.getDisplay()+"</td>");
        								sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 194px;\"></td>") ;
            						}else{
            							sbb.append("<td style=\"width: 70px;background:#22c907;text-align: center;\">"+((outTime.length()>0 && "0".equals(str[0]))?outTime:"")+bean.getDisplay()+"</td>");
        								sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 262px;\"></td>") ;
            						}
            						while(i+1<dets.size()){
            							String []nextStr=(String[])dets.get(i+1);
            							if(str[0].equals(nextStr[0])){
            								if(str[3].length()>0&&flowMgt.compareFinishDate(bean, nextStr[2], nextStr[3])>0){
            									sbb.append("<td style=\"width: 70px;background:#ff0000;text-align: center;\">"+bean.getDisplay()+"</td>");
                								sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 194px;\"></td>") ;
            								}else{
            									sbb.append("<td style=\"width: 70px;background:#22c907;text-align: center;\">"+bean.getDisplay()+"</td>");
            									sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 262px;\"></td>") ;
            								}
            								str=nextStr;
            								i++;
            							}else{
            								break;
            							}
            						}
            						//flowDepict+="<font color=\"pink\">"+display+"-></font>";
            					}else{//当前节点
            						currCP=currCP.replace(str[1]+";", "");
            						if(str[2]==null||"".equals(str[2])){
            							if(str[3].length()>0&&flowMgt.compareFinishDate(bean, "", str[3])>0){
            								sbb.append("<td style=\"width: 70px;background:#ff0000;text-align: center;\">"+bean.getDisplay()+"</td>");
            								sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 194px;\"></td>") ;
            							}else{
            								sbb.append("<td style=\"width: 70px;background:#22c907;text-align: center;\">"+bean.getDisplay()+"</td>");
            								sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 262px;\"></td>") ;
            							}
            						}else{
            							if(str[3].length()>0&&flowMgt.compareFinishDate(bean, str[2], str[3])>0){
            								sbb.append("<td style=\"width: 70px;background:#ff0000;text-align: center;\">"+bean.getDisplay()+"</td>");
            								sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 194px;\"></td>") ;
            							}else{
            								sbb.append("<td style=\"width: 70px;background:#ff9c00;text-align: center;\">"+bean.getDisplay()+"</td>");
            								sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 194px;\"></td>") ;
            							}
            						}
            						while(i+1<dets.size()){
            							String []nextStr=(String[])dets.get(i+1);
            							if(str[0].equals(nextStr[0])){
            								currCP=currCP.replace(nextStr[1]+";", "");
            								if(nextStr[2]==null||"".equals(nextStr[2])){
            									if(str[3].length()>0&&flowMgt.compareFinishDate(bean, "", nextStr[3])>0){
            										sbb.append("<td style=\"width: 70px;background:#ff0000;text-align: center;\">"+bean.getDisplay()+"</td>");
                    								sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 194px;\"></td>") ;
            									}else{
            										sbb.append("<td style=\"width: 70px;background:#22c907;text-align: center;\">"+bean.getDisplay()+"</td>");
                    								sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 262px;\"></td>") ;
            									}
            								}else{
            									if(str[3].length()>0&&flowMgt.compareFinishDate(bean, nextStr[2], nextStr[3])>0){
            										sbb.append("<td style=\"width: 70px;background:#ff0000;text-align: center;\">"+bean.getDisplay()+"</td>");
                    								sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 194px;\"></td>") ;
            									}else{
            										sbb.append("<td style=\"width: 70px;background:#ff9c00;text-align: center;\">"+bean.getDisplay()+"</td>");
                    								sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 194px;\"></td>") ;
            									}
            								}
            								str=nextStr;
            								i++;
            							}else{
            								break;
            							}
            						}
            						String []currs=currCP.split(";");
            						for(int j=0;j<currs.length;j++){
            							if(currs[j].length()>0){
            								if(str[3].length()>0&&flowMgt.compareFinishDate(bean, "", str[3])>0){
            									sbb.append("<td style=\"width: 70px;background:#ff0000;text-align: center;\">"+bean.getDisplay()+"</td>");
                								sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 194px;\"></td>") ;
            								}else{
            									sbb.append("<td style=\"width: 70px;background:#22c907;text-align: center;\">"+bean.getDisplay()+"</td>");
                								sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 262px;\"></td>") ;
            								}
            							}
            						}
            							//flowDepict+="<font color=\"gree\">"+display+"</font><font color=\"black\">-></font>";
            					}
            				}
            						
            				*//***************************************未办理的节点***************************************//*
            				String[] lastStr=null;
            				boolean flag=false;
            				if(dets.size()>0){
            					lastStr=(String[])dets.get(dets.size()-1);
            				}else{
            					lastStr=new String[4];
            					currNode="0";
            					lastStr[0]="0";
            					FlowNodeBean bean=designBean.getFlowNodeMap().get(String.valueOf("0"));
            					if(!"0".equals(currNode)){//非当前节点，就是已办结的节点
        							sbb.append("<td style=\"width: 70px;background:#e3e3e3;text-align: center;\">"+bean.getDisplay()+"</td>");
                					sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 330px;\"></td>") ;
        						}else{
        							sbb.append("<td style=\"width: 70px;background:#ff9c00;text-align: center;\">"+outTime+bean.getDisplay()+"</td>");
                					sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 126px;\"></td>") ;
        						}
            				}
            					
            				Long nextNodeId=Long.parseLong(lastStr[0]);
            				HashMap values=new HashMap(); 
            				String tableName="";	
            				if(nextNodeId!=-1){
            					if(BaseEnv.workFlowInfo.get(designId).getTemplateType() == 1){
            						tableName=BaseEnv.workFlowInfo.get(designId).getTemplateFile();
            					}else{
            						tableName="OAWorkFlowTable";
            					}
            					sql="select createBy,";
            					List<DBFieldInfoBean> fieldList=((DBTableInfoBean)BaseEnv.tableInfos.get(tableName)).getFieldInfos();
            					for(int j=0;j<fieldList.size();j++){
            						sql+=fieldList.get(j).getFieldName()+",";
            					}
            					sql=sql.substring(0,sql.length()-1);
            					sql+=" from "+tableName+" where id='"+billId+"'";
            						
            					st = conn.createStatement() ;
            					ResultSet rst=st.executeQuery(sql);
            					if(rst.next()){
            						for(int j=0;j<fieldList.size();j++){
            							values.put(fieldList.get(j).getFieldName(), rst.getString(fieldList.get(j).getFieldName()));
            						}
            					} 	
            				 }
            						    
            				 while(nextNodeId!=-1){
            					nextNodeId=Long.parseLong(flowMgt.getNextNodeIds(designId, String.valueOf(nextNodeId), values,tableName,conn).split(";")[0]);
            					FlowNodeBean bean=designBean.getFlowNodeMap().get(String.valueOf(nextNodeId));
            					if(nextNodeId!=-1){
            						if(!String.valueOf(nextNodeId).equals(currNode)){//非当前节点，就是已办结的节点
            							sbb.append("<td style=\"width: 70px;background:#e3e3e3;text-align: center;\">"+bean.getDisplay()+"</td>");
                    					sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 330px;\"></td>") ;
            						}else{
            							sbb.append("<td style=\"width: 70px;background:#ff9c00;text-align: center;\">"+bean.getDisplay()+"</td>");
                    					sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 126px;\"></td>") ;
            						}
            						
            					}else{
            						if(currNode.equals("-1")){
            							sbb.append("<td style=\"width: 70px;background:#22c907;text-align: center;\">"+bean.getDisplay()+"</td>");
                						sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 262px;\"></td>") ;
            						}else{
            							sbb.append("<td style=\"width: 70px;background:#e3e3e3;text-align: center;\">"+bean.getDisplay()+"</td>");
                						sbb.append("<td style=\"width: 32px;background:url('/style/images/Direction_Icons.gif');background-position: 209px 330px;\"></td>") ;
            						}
            					}
            				}
            			} catch (Exception e) {
							e.printStackTrace();
						}
                    }
                });
                return rs.getRetCode();
            }
        });
        sbb.append("</tr>") ;
		sbb.append("</table>") ;*/
		return sbb.toString() ;
	}
	
}
