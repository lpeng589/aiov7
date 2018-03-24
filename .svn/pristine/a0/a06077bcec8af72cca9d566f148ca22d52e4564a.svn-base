package com.koron.oa.workflow.template;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.ApproveBean;
import com.koron.oa.bean.ConditionBean;
import com.koron.oa.bean.ConditionsBean;
import com.koron.oa.bean.FieldBean;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.NotePeopleBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.koron.oa.workflow.ReadXML;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.PublicMgt;
import com.menyi.web.util.SystemState;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

/**
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Nov 7, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class OAWorkFlowTempMgt extends AIODBManager {

    public Result update(final OAWorkFlowDesignForm myForm,final String allowVisitor,final String wakeUp) {
        final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try {
                        	OAWorkFlowTempMgt mgt=new OAWorkFlowTempMgt();
                            String esql = "update OAWorkFlowTemplate set templateName=?,templateClass=? ,allowVisitor=? ," +
                            				"templateStatus=? ,wakeUp=?,flowOrder=?,readLimit=?,detail=?,affix=?,templateFile=?,"+
                            				"depMonitorScope=?,perMonitorScope=?,depMonitor=?,perMonitor=?,titleTemp=?,"+
                            				"nextWake=?,startWake=?,allWake=?,setWake=?,setWakeDept=?,setWakePer=?,setWakeGroup=?,stopStartWake=?"+
                            				",stopSAllWake=?,stopSetWake=?,stopSetWakeDept=?,stopSetWakePer=?,stopSetWakeGroup=?,autoPass=?,usefulLifeS=?,"+
                            				"usefulLifeE=?,allowAdd=?,retCheckPerRule=?,timeType=?,timeVal=?,fileContent=? ,updateBy=?,dispenseWake=?,reviewWake=?," +
                            				"defStatus=?,overTimeWake=?,wakeLimitSQL=? where id=?";
                            PreparedStatement pstmt = conn.prepareStatement(esql);
                            pstmt.setString(1,myForm.getTemplateName());
                            pstmt.setString(2,myForm.getTemplateClass());
                            pstmt.setString(3,allowVisitor);
                            pstmt.setInt(4,myForm.getTemplateStatus());
                            pstmt.setString(5, wakeUp) ;
                            pstmt.setInt(6, myForm.getFlowOrder()) ;
                            pstmt.setInt(7,myForm.getReadLimit());
                            pstmt.setString(8, myForm.getDetail());
                            pstmt.setString(9, "");
                            pstmt.setString(10, myForm.getTemplateFile());
                            pstmt.setString(11, myForm.getDepMonitorScope()) ;
                            pstmt.setString(12,myForm.getPerMonitorScope());
                            pstmt.setString(13, myForm.getMonitorDeptIds());
                            pstmt.setString(14, myForm.getMonitorUserIds());
                            pstmt.setString(15, myForm.getTitleTemp());
                            pstmt.setString(16,mgt.linkStr(myForm.getNextWake(), ",")) ;
                            pstmt.setString(17, mgt.linkStr(myForm.getStartWake(), ",")) ;
                            pstmt.setString(18, mgt.linkStr(myForm.getAllWake(), ","));
                            pstmt.setString(19, mgt.linkStr(myForm.getSetWake(), ",")) ;
                            pstmt.setString(20, myForm.getSetWakeDept()) ;
                            pstmt.setString(21,myForm.getSetWakePer()) ;
                            pstmt.setString(22,myForm.getSetWakeGroup()) ;
                            pstmt.setString(23,mgt.linkStr(myForm.getStopStartWake(), ","));
                            pstmt.setString(24,mgt.linkStr( myForm.getStopSAllWake(), ","));
                            pstmt.setString(25,mgt.linkStr( myForm.getStopSetWake(), ",")) ;
                            pstmt.setString(26, myForm.getStopSetWakeDept()) ;
                            pstmt.setString(27,myForm.getStopSetWakePer()) ;
                            pstmt.setString(28,myForm.getStopSetWakeGroup()) ;
                            pstmt.setString(29,myForm.getAutoPass());
                            pstmt.setString(30,myForm.getUsefulLifeS()) ;
                            pstmt.setString(31,myForm.getUsefulLifeE());
                            pstmt.setString(32,myForm.getAllowAdd());
                            pstmt.setString(33,myForm.getRetCheckPerRule());
                            pstmt.setString(34,myForm.getTimeType());
                            pstmt.setString(35,myForm.getTimeVal());
                            pstmt.setString(36, myForm.getFileContent()) ;
                            pstmt.setString(37, myForm.getUpdateBy());
                            pstmt.setString(38, mgt.linkStr(myForm.getDispenseWake(), ","));
                            pstmt.setString(39, mgt.linkStr(myForm.getReviewWake(), ","));
                            pstmt.setString(40, myForm.getDefStatus());
                            pstmt.setString(41, mgt.linkStr(myForm.getOverTimeWake(), ","));
                            pstmt.setString(42, myForm.getWakeLimitSQL());
                            pstmt.setString(43, myForm.getId());
                            
                            pstmt.execute();
                            
                            //修改了监控范围后，必须把老版本的监控范围也一起修改一下，否则新增的监控人员看不到老版本工作流数据.
                            esql = "update OAWorkFlowTemplate set  depMonitorScope=?,perMonitorScope=?,depMonitor=?,perMonitor=? where templateFile=?";
                            pstmt = conn.prepareStatement(esql);
                            pstmt.setString(1, myForm.getDepMonitorScope());
                            pstmt.setString(2, myForm.getPerMonitorScope());
                            pstmt.setString(3, myForm.getMonitorDeptIds());
                            pstmt.setString(4, myForm.getMonitorUserIds());
                            pstmt.setString(5, myForm.getTemplateFile());
                            pstmt.execute();
                            
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
    
    public String linkStr(String [] array,String sign){
    	String str="";
    	for(int i=0;array!=null&&i<array.length;i++){
    		str+=array[i]+sign;
    	}
    	if(str.length()>0){
    		str=str.substring(0,str.length()-1);
    	}
    	return str;
    }
    
    public Result add(final String id,final OAWorkFlowDesignForm form,
                      final String allowVisitor , final String wakeUp) {
        final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try {
                        	OAWorkFlowTempMgt mgt=new OAWorkFlowTempMgt();
                            String esql = "insert into OAWorkFlowTemplate(id,templateName,templateType ,templateClass  ," +
                            			   "allowVisitor ,templateStatus,wakeUp,flowOrder,readLimit,detail,affix,templateFile,"+
                            			   "depMonitorScope,perMonitorScope,depMonitor,perMonitor,titleTemp,nextWake,startWake,allWake,setWake,setWakeDept,"+
                            			   "setWakePer,setWakeGroup,stopStartWake,stopSAllWake,stopSetWake,stopSetWakeDept,stopSetWakePer,stopSetWakeGroup,autoPass"+
                            			   ",usefulLifeS,usefulLifeE,allowAdd,timeType,timeVal,fileContent,statusId,sameFlow,createTime,dispenseWake,designType,overTimeWake,wakeLimitSQL) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                            			   +",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                            PreparedStatement pstmt = conn.prepareStatement(esql);
                            pstmt.setString(1, id);
                            pstmt.setString(2, form.getTemplateName());
                            pstmt.setInt(3, form.getTemplateType());
                            pstmt.setString(4,form.getTemplateClass());
                            pstmt.setString(5, allowVisitor);
                            pstmt.setInt(6, form.getTemplateStatus());
                            pstmt.setString(7, wakeUp) ;
                            pstmt.setInt(8, form.getFlowOrder()) ;
                            pstmt.setInt(9, form.getReadLimit()) ;
                            pstmt.setString(10, form.getDetail()) ;
                            pstmt.setString(11, "") ;
                            pstmt.setString(12, form.getTemplateFile());
                            pstmt.setString(13, form.getDepMonitorScope()) ;
                            pstmt.setString(14, form.getPerMonitorScope()) ;
                            pstmt.setString(15,form.getMonitorDeptIds()) ;
                            pstmt.setString(16,form.getMonitorUserIds()) ;
                            pstmt.setString(17,form.getTitleTemp());
                            pstmt.setString(18,mgt.linkStr(form.getNextWake(),",")) ;
                            pstmt.setString(19,mgt.linkStr(form.getStartWake(),",")) ;
                            pstmt.setString(20,mgt.linkStr(form.getAllWake(),","));
                            pstmt.setString(21,mgt.linkStr(form.getSetWake(),",")) ;
                            pstmt.setString(22, form.getSetWakeDept()) ;
                            pstmt.setString(23,form.getSetWakePer()) ;
                            pstmt.setString(24,form.getSetWakeGroup()) ;
                            pstmt.setString(25,mgt.linkStr(form.getStopStartWake(),","));
                            pstmt.setString(26,mgt.linkStr(form.getStopSAllWake(),","));
                            pstmt.setString(27,mgt.linkStr(form.getStopSetWake(),",")) ;
                            pstmt.setString(28, form.getStopSetWakeDept()) ;
                            pstmt.setString(29,form.getStopSetWakePer()) ;
                            pstmt.setString(30,form.getStopSetWakeGroup()) ;
                            pstmt.setString(31,form.getAutoPass());
                            pstmt.setString(32,form.getUsefulLifeS());
                            pstmt.setString(33,form.getUsefulLifeE());
                            pstmt.setString(34,form.getAllowAdd());
                            pstmt.setString(35,form.getTimeType());
                            pstmt.setString(36,form.getTimeVal());
                            pstmt.setString(37,form.getFileContent()) ;
                            pstmt.setInt(38, 0) ;
                            pstmt.setString(39,id) ;
                            pstmt.setString(40, form.getCreateTime());
                            pstmt.setString(41, mgt.linkStr(form.getDispenseWake(), ","));
                            pstmt.setString(42, form.getDesignType());
                            pstmt.setString(43, mgt.linkStr(form.getOverTimeWake(), ","));
                            pstmt.setString(44, form.getWakeLimitSQL());
                            pstmt.execute();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                    }
                });
                OAWorkFlowTemplate workFlow =(OAWorkFlowTemplate) loadBean(id, OAWorkFlowTemplate.class,session).retVal;
                BaseEnv.workFlowInfo.put(workFlow.getId(), workFlow) ;
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
        return rst;
    }

   
    public Result load(String id){
        List param = new ArrayList();
        String sql ="select work.id,templateName ,templateType,templateClass,templateFile,workflowFile,allowVisitor,templateStatus,wakeUp,flowOrder,readLimit,detail,affix,depMonitorScope,perMonitorScope,depMonitor,perMonitor,titleTemp,"+
        "nextWake,startWake,allWake,setWake,setWakeDept,setWakePer,setWakeGroup,stopStartWake,stopSAllWake,stopSetWake,stopSetWakeDept,stopSetWakePer,stopSetWakeGroup,autoPass,applyLookFieldDisplay,usefulLifeS,usefulLifeE,allowAdd," +
        "retCheckPerRule,timeType,timeVal,sameFlow,fileContent,dispenseWake,reviewWake,designType,defStatus,overTimeWake,FileFinish,wakeLimitSQL from OAWorkFlowTemplate work where id=? ";
       System.out.println("sql："+sql);
        param.add(id);
        final Result rs=sqlList(sql,param);
       
        return rs;
    }
    
    @SuppressWarnings("unchecked")
	public Result loadNew(String id){
        List param = new ArrayList();
        String sql ="select work.id,templateName ,templateType,templateClass,templateFile,workflowFile,allowVisitor,templateStatus,wakeUp,flowOrder,readLimit,detail,affix,depMonitorScope,perMonitorScope,depMonitor,perMonitor,titleTemp,"+
        "nextWake,startWake,allWake,setWake,setWakeDept,setWakePer,setWakeGroup,stopStartWake,stopSAllWake,stopSetWake,stopSetWakeDept,stopSetWakePer,stopSetWakeGroup,autoPass,applyLookFieldDisplay,usefulLifeS,usefulLifeE,allowAdd,retCheckPerRule,timeType,timeVal,sameFlow,fileContent,dispenseWake,reviewWake,designType,defStatus,overTimeWake from OAWorkFlowTemplate work where id=? ";
        param.add(id);
        return sqlList(sql,param);
    }
    
    public boolean isFinish(String tempId){    	
    	String sql="select count(0) from OAWorkFlowTemplate where id='"+tempId+"' and fileFinish='1'";
    	List param=new ArrayList();
    	final Result rs=sqlList(sql, param);
    	final List list=(List)rs.getRetVal();
    	Object obj=((Object[])list.get(0))[0];
    	if(Integer.parseInt(obj.toString())==1){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public int isAbleUsed(String tempId){    	
    	String sql="select fileFinish,allowVisitor from OAWorkFlowTemplate where id='"+tempId+"' ";
    	List param=new ArrayList();
    	final Result rs=sqlList(sql, param);
    	final List list=(List)rs.getRetVal();
    	Object[] obj=((Object[])list.get(0));
    	if(obj[1]!=null){
    		boolean flag=false;
    		if(obj[1].toString().replaceAll(",", "").replaceAll("\\|", "").length()>0){
    			flag=true;
    		}
	    	
	    	if(obj[0]!=null&&obj[0].equals("0")||!flag){
	    		return 1;
	    	}else{
	    		return 0;
	    	}
    	}else{
    		return 2;
    	}
    }
    
    public Result queryFirst(final String locale,LoginBean loginBean) {
		//创建参数
	List param = new ArrayList();
	String sql ="select top 1 work.id,templateName ,templateType," + locale + ",templateStatus,work.templateClass,work.statusId,work.flowOrder,templateFile from OAWorkFlowTemplate work, tblWorkFlowType a,tbllanguage b where work.templateClass=a.classCode and a.ModuleName=b.id  ";
	sql+=" and fileFinish='1' and templateStatus=1 and work.statusId=0 and  (charIndex(',"+loginBean.getId()+",',allowVisitor)>0 or charIndex(',"+loginBean.getDepartCode()+",',allowVisitor)>0 ";
	String departCode=loginBean.getDepartCode();
	while(departCode.length()>5){
		departCode=departCode.substring(0,departCode.length()-5);
		sql+=" or charIndex(',"+departCode+",',allowVisitor)>0";
	}
	if(loginBean.getGroupId().length()>0){
		String []groups=loginBean.getGroupId().split(";");
		for(int i=0;i<groups.length;i++){
			sql+=" or charIndex(',"+groups[i]+",',allowVisitor)>0";
		}
	}
	sql+=") order by work.flowOrder";
	
	//调用list返回结果        
    final KRLanguageQuery krQuery = new KRLanguageQuery();
    final Result rs=sqlList(sql, param);
    final List list=(List)rs.getRetVal();
    for(int i=0;i<list.size();i++){
    	krQuery.addLanguageId((((Object[])list.get(i))[1]).toString());
    }
    
    int retCode = DBUtil.execute(new IfDB() {
        public int exec(Session session) {
            session.doWork(new Work() {
                public void execute(Connection conn) throws  SQLException {
                    try {
                    	HashMap krLanguageMap = krQuery.query(conn);
                    	for(int i=0;i<list.size();i++){                    		
                    		String tempName=((Object[])list.get(i))[1].toString();
                    		if((KRLanguage) krLanguageMap.get(tempName)!=null){
                    			((Object[])list.get(i))[1]=((KRLanguage) krLanguageMap.get(tempName)).get(locale);
                    		}
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

    public Result query(final String locale, String templateName, int templateType,
                        String templateClass, int templateStatus, int pageNo, int pageSize) {
        //创建参数
        List param = new ArrayList();
        String sql = "select work.id,work.templateName,templateType,a.moduleName,templateStatus,work.templateClass,work.statusId,work.flowOrder,templateFile,work.designType,work.workflowFile,work.fileFinish from"+
                " OAWorkFlowTemplate work left join  tblWorkFlowType a  on work.templateClass=a.classCode  where  work.statusId=0";
        //如标题不为空，则做标题模糊查询
        if (templateName != null && templateName.length() != 0) {
            sql += " and (upper(templateName) like '%"+templateName.trim().toUpperCase()+"%' or upper(templateName) like '%"+templateName.trim().toUpperCase()+"%')";
        }
        if (templateType != -1) {
            sql += " and templateType = ? ";
            param.add(templateType);
        }
        if (templateClass != null && templateClass.length() != 0) {
            sql += " and templateClass like ? ";
            param.add(""+templateClass+"%");
        }

        if (templateStatus != -1) {
            sql += " and templateStatus = ? ";
            param.add(templateStatus);
        }
        //跟据加密狗控制，查询的类型
        String modStr = "";
		for(String ml:(ArrayList<String>)SystemState.instance.moduleList){
			if("1".equals(ml)){
				modStr += " OR a.classCode like '00001%'  ";
			}else if("2".equals(ml)){
				modStr += " OR a.classCode like '00002%'  ";
			}else if("3".equals(ml)){
				modStr += " OR a.classCode like '00003%'  ";
			}
		}
		if(modStr.length() > 0){
			modStr = modStr.substring(3);
		}
		sql = sql +" and ("+modStr+")";
        
        
        sql+=" order by work.templateStatus desc";
        BaseEnv.log.debug("OAWorkFlowTempMgt.query 查询工作流sql ="+sql);
        return sqlList(sql, param, pageSize, pageNo, true);
    }
    
    public Result queryErp(final String locale, String templateName, int templateStatus, int pageNo,int pageSize,String tableName) {
    	List param = new ArrayList();
    	String sql ="select work.id,work.templateName,templateStatus,templateFile,workFlowFile,fileFinish,row_number() over (order by templateStatus desc,fileFinish desc) as row_id from"+
	    " OAWorkFlowTemplate work  where work.statusId=0 and templateClass='00001'";
    	//如标题不为空，则做标题模糊查询
    	if (templateName != null && templateName.length() != 0) {
    		sql += " and upper(work.templateName) like '%"+templateName.trim().toUpperCase()+"%'";
    	}
    	if (tableName != null && tableName.length() != 0) {
    		sql += " and upper(work.templateFile) like '%"+tableName.trim().toUpperCase()+"%'";
    	}
    	
		if (templateStatus != -1) {
			sql += " and templateStatus = ? ";
			param.add(templateStatus);
		}
	
		//调用list返回结果        
		final Result rs=this.sqlListMap(sql, param, pageNo, pageSize);
		return rs;
	}
    
    public Result query(String templateName,String templateClass,int templateStatus){
    	//创建参数
        List param = new ArrayList();
        String sql =
                "select id,templateName ,templateType,workFlowFile,templateClass,allowVisitor from OAWorkFlowTemplate where 1=1 ";
        //如标题不为空，则做标题模糊查询
        if (templateName != null && templateName.length() != 0) {
            sql += " and upper(templateName) like '%"+templateName.trim().toUpperCase()+"%' ";
        }
        if (templateClass != null && templateClass.length() != 0) {
            sql += " and templateClass like ? ";
            param.add(templateClass+"%");
        }
        if (templateStatus!=-1){
        	sql += " and templateStatus=? " ;
        	param.add(templateStatus) ;
        }
        //调用list返回结果
        return sqlList(sql, param);
    }
    /**
     * 查询模板类型
     * @return Result
     */
    public Result queryType(final String type) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select id,classCode,ModuleName,isCatalog from tblWorkFlowType where isCatalog=0 ";
							if("all".equals(type)){
							  sql =  "select id,classCode,ModuleName,isCatalog from tblWorkFlowType where 1=1 ";
							}
							String modStr = "";
							for(String ml:(ArrayList<String>)SystemState.instance.moduleList){
								if("1".equals(ml)){
									modStr += " OR classCode like '00001%'  ";
								}else if("2".equals(ml)){
									modStr += " OR classCode like '00002%'  ";
								}else if("3".equals(ml)){
									modStr += " OR classCode like '00003%'  ";
								}
							}
							if(modStr.length() > 0){
								modStr = modStr.substring(3);
							}
							sql = sql +" and ("+modStr+")";
							
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ResultSet rs = ps.executeQuery();
							ArrayList<String []> list=new ArrayList<String []>();
							while(rs.next()){
								String []str=new String[4];
								str[0]=rs.getString(1);
								str[1]=rs.getString(2);
								str[2]=rs.getString(3);
								str[3]=rs.getString(4);
								list.add(str);
							}
							rst.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("OAWorkFlowTempMgt queryType data Error.", ex);
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
    
    public Result queryNo() {
    	final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try {
                        	String sql = "select max(floworder) from OAWorkFlowTemplate";
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                    		ResultSet rss = pss.executeQuery() ;
                    		if(rss.next()){
                    			int count = rss.getInt(1) ;
                    			rst.setRetVal(count);
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
     * 跟据用户查找工作流
     * @return
     */
    public Result queryFlowIdByAllowUser(final String tableName,final String userId) {
    	final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try {
                        	String sql = "select id from OAWorkFlowTemplate where templateFile=? and allowVisitor like ?";
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        	pss.setString(1, tableName) ;
                        	pss.setString(2, "%,"+userId+",%") ;
                    		ResultSet rss = pss.executeQuery() ;
                    		if(rss.next()){
                    			rst.setRetVal(rss.getString(1));
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
     * 查询模板类型
     * @return Result
     */
    public Result deleteWorkFlow(final String[] keyIds) {
    	final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try {
                        	for(String keyId : keyIds){
                        		String sql = "select count(*) from OAWorkFlowTable where templateCode=?" ;
                        		PreparedStatement pss = conn.prepareStatement(sql) ;
                        		pss.setString(1, keyId) ;
                        		ResultSet rss = pss.executeQuery() ;
                        		if(rss.next()){
                        			int count = rss.getInt(1) ;
                        			if(count>0){
                        				rst.setRetCode(ErrorCanst.DATA_ALREADY_USED) ;
                        				break ;
                        			}
                        		}
                        		sql = "delete from OAWorkFlowTemplate where id=?" ;
                        		pss = conn.prepareStatement(sql) ;
                        		pss.setString(1, keyId) ;
                        		int count = pss.executeUpdate() ;
                        		if(count>0){
                        			BaseEnv.workFlowDesignBeans.remove(keyId);
                        		}
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
    
    public void changeState(final String[] keyIds, final int bln) {
    	final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try {
                        	for(String keyId : keyIds){
                        		String sql = "update OAWorkFlowTemplate set templateStatus='" + bln + "' where id='" + keyId + "'" ;
                        		PreparedStatement pss = conn.prepareStatement(sql) ;
                        		pss.executeUpdate() ;
                        		sql = "select templateFile from OAWorkFlowTemplate where id='" + keyId + "'" ;
                        		pss = conn.prepareStatement(sql) ;
                        		ResultSet rss = pss.executeQuery() ;
                        		if(rss.next()){
                        			String templateFile = rss.getString("templateFile") ;
                        			if(BaseEnv.workFlowInfo.get(templateFile) != null){
                        				BaseEnv.workFlowInfo.get(templateFile).setTemplateStatus(bln);
                        			}
                        		}
                        		
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
	}
    
    public WorkFlowDesignBean getWorkFlowDesing(final String tableName){
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try {
                        	Statement st=conn.createStatement(); 
                        	String sql ="select id from OAWorkFlowTemplate where templateFile='"+tableName+"' and fileFinish=1 and templateStatus=1 order by convert(int,version) desc";                        	
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
        WorkFlowDesignBean bean=null;
        if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS&&rs.retVal!=null){
        	String key=rs.retVal.toString();
        	bean=BaseEnv.workFlowDesignBeans.get(key);
        }
        return bean;
    }
    
    public WorkFlowDesignBean getWorkFlowDesing2(final String billID,final String tableName){
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try {
                        	String sql ="select applyType from OAMyWorkFlow where keyId=? and tableName=?";
                        	PreparedStatement pss = conn.prepareStatement(sql);
                        	pss.setString(1, billID);
                        	pss.setString(2, tableName);
                        	ResultSet rst=pss.executeQuery();
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
        WorkFlowDesignBean bean=null;
        if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS&&rs.retVal!=null){
        	String key=rs.retVal.toString();
        	bean=BaseEnv.workFlowDesignBeans.get(key);
        }
        return bean;
    }
    
    /**
     * 查询该表是否还未审的单据
     * @param tableName 表名
     * @param type 判断是否是查询存在未审核的单据
     * @return
     */
    public String selectExistBill(String[] keyId,String locale,String type) {
    	String keyIds="";
    	for(int i=0;i<keyId.length;i++){
    		keyIds+="'"+keyId[i]+"',";
    	}    	
    	String sql="select tableName from OAMyWorkFlow where currentNode!='-1' and applyType in (select id from OAWorkFlowTemplate where templateFile in (select templateFile from OAWorkFlowTemplate where id in ("+keyIds.substring(0,keyIds.length()-1)+"))) group by tableName";
    	if("all".equals(type)){
    		sql="select tableName from OAMyWorkFlow where applyType in (select id from OAWorkFlowTemplate where templateFile in (select templateFile from OAWorkFlowTemplate where id in ("+keyIds.substring(0,keyIds.length()-1)+"))) group by tableName";
    	}

    	Result rs=sqlList(sql, new ArrayList());
    	String tableName="";
    	List list=(List)rs.retVal;
    	for(int i=0;i<list.size();i++){
    		DBTableInfoBean tableInfo= (DBTableInfoBean)BaseEnv.tableInfos.get(((Object[])list.get(i))[0]);
    		tableName+=tableInfo.getDisplay().get(locale)+",";
    	}
    	
    	if(tableName.length()>0){
    		tableName=tableName.substring(0,tableName.length()-1);
    	}
    	return tableName;
	}
    
    /**
     * 查询该表是否还未审的单据
     * @param tableName 表名
     * @return
     */
    public String selectExistBillNotFinish2(String[] keyId,String locale) {
    	String keyIds="";
    	for(int i=0;i<keyId.length;i++){
    		keyIds+="'"+keyId[i]+"',";
    	}    	
    	String sql="select tableName from OAMyWorkFlow where currentNode!='-1' and applyType in ("+keyIds.substring(0,keyIds.length()-1)+")  group by tableName";

    	Result rs=sqlList(sql, new ArrayList());
    	String tableName="";
    	List list=(List)rs.retVal;
    	for(int i=0;list!=null && i<list.size();i++){
    		DBTableInfoBean tableInfo= (DBTableInfoBean)BaseEnv.tableInfos.get(((Object[])list.get(i))[0]);
    		tableName+=tableInfo.getDisplay().get(locale)+",";
    	}
    	
    	if(tableName.length()>0){
    		tableName=tableName.substring(0,tableName.length()-1);
    	}
    	return tableName;
	}
    
    /**
     * 流程版本
     * @param tableName
     * @return
     */
    public Result queryFlowVersion(final String tableName) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select temp.version,temp.createTime,emp.empFullName,temp.id from OAWorkFlowTemplate  temp " +
									"left join tblEmployee emp on temp.updateBy=emp.id where temp.templateFile=? and len(isNull(temp.createTime,''))>0 order by temp.createTime desc" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, tableName) ;
							ResultSet rss = pss.executeQuery() ;
							List<String[]> listVersion = new ArrayList<String[]>() ;
							while(rss.next()){
								String[] versions = new String[4] ;
								versions[0] = rss.getString("version") ;
								versions[1] = rss.getString("createTime") ;
								versions[2] = rss.getString("empFullName") ;
								versions[3] = rss.getString("id") ;
								listVersion.add(versions) ;
							}
							rst.setRetVal(listVersion) ;
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
     * 查询表单版本
     * @param tableId
     * @param designId
     * @return
     */
    public Result queryTableVersion(final String tableId,final String designId){
    	final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql =" SELECT o.id,o.createTime,t.empfullName FROM OAWorkFlowTableVersion o LEFT JOIN tblemployee t ON o.createBy=t.id "
									  + "WHERE o.tableId= ? AND o.designId=?  and o.saveType!='view' order by o.createTime desc" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, tableId) ;
							pss.setString(2, designId);
							ResultSet rss = pss.executeQuery() ;
							List<String[]> listVersion = new ArrayList<String[]>() ;
							while(rss.next()){
								String[] versions = new String[4] ;
								versions[0] = rss.getString("createTime") ;
								versions[1] = rss.getString("empFullName") ;
								versions[2] = rss.getString("id") ;
								listVersion.add(versions) ;
							}
							rst.setRetVal(listVersion) ;
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
	 * 复制工作流模板
	 * 
	 * @param flowName
	 * @param fileFinish
	 * @return
	 */
	public Result copyFlowTemplate(final String keyId, final String newFlowName,final String userId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				Result result = loadBean(keyId, OAWorkFlowTemplate.class, session);
				final OAWorkFlowTemplate template = (OAWorkFlowTemplate) result.retVal;
				final OAWorkFlowTemplate nTemplate = new OAWorkFlowTemplate();
				try {
					PropertyUtils.copyProperties(nTemplate,template);
					nTemplate.setId(newFlowName.substring(0, newFlowName.indexOf(".")));
					nTemplate.setWorkFlowFile(newFlowName);
					nTemplate.setVersion(String.valueOf(Integer.parseInt(template.getVersion()==null?"0":template.getVersion())+1));
					nTemplate.setStatusId(0);
					nTemplate.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
					nTemplate.setUpdateBy(userId);
					//插入新版本流程
					Result rs = addBean(nTemplate, session);
					if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
						rst.retCode = rs.retCode;
						rst.retVal = rs.retVal;
						return rs.retCode;
					}
					session.doWork(new Work() {
						public void execute(Connection conn) throws SQLException {
							/*更新上一个流程为旧版本*/
							String sql=" update OAWorkFlowTemplate set StatusId = -1 where id = ? ";
							PreparedStatement pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, keyId);
							pstmt.execute();
							/*读结点*/							
							sql=" select * from OAWorkFlowNode where flowId=? ";
							pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, keyId);
							ResultSet rss3 =pstmt.executeQuery();
							while(rss3.next()){
								String flowNodeId= IDGenerater.getId();
								String oldflowNodeId = rss3.getString(1);
								
								String fields = "";
								String values = "";
								ArrayList<String> param = new ArrayList();
								for(int i = 0 ;i< rss3.getMetaData().getColumnCount();i++){
									String name = rss3.getMetaData().getColumnName(i+1);
									if("id".equals(name)){
										fields +=",id";
										values +=",?";
										param.add(flowNodeId);
										
										oldflowNodeId = rss3.getString(name);
									}else if("flowId".equals(name)){
										fields +=",flowId";
										values +=",?";
										param.add(nTemplate.getId());
									}else if("to".equals(name)){
										fields +=",[to]";
										values +=",?";
										param.add(rss3.getString(name));
									}else{
										fields +=","+name;
										values +=",?";
										param.add(rss3.getString(name));
									}
								}
								fields = fields.substring(1);
								values = values.substring(1);
								sql = "insert into OAWorkFlowNode("+fields+") values("+values+")";
								PreparedStatement pstmt2 = conn.prepareStatement(sql);
								for(int i = 0 ;i< param.size();i++){
									pstmt2.setString(i+1, param.get(i));
								}
								/*复制结点*/
								pstmt2.executeUpdate();
								
								/*复制审批人*/
								sql=" insert into OAWorkFlowNodeApprover(id,flowNodeId,userName,[user],typeName,type,checkType) " +
										"select substring(replace(newid(),'-',''),1,30),?,userName,[user],typeName,type,checkType from OAWorkFlowNodeApprover where flowNodeId=?";
								pstmt2 = conn.prepareStatement(sql);
								pstmt2.setString(1, flowNodeId);
								pstmt2.setString(2, oldflowNodeId);
								pstmt2.execute();
								/*复制字段*/
								sql=" insert into OAWorkFlowNodeField(id,flowNodeId,fieldName,inputType,isNotNull,fieldType) " +
										"select substring(replace(newid(),'-',''),1,30),?,fieldName,inputType,isNotNull,fieldType from OAWorkFlowNodeField where flowNodeId=?";
								pstmt2 = conn.prepareStatement(sql);
								pstmt2.setString(1, flowNodeId);
								pstmt2.setString(2, oldflowNodeId);
								pstmt2.execute();
								
								/*复制条件*/
								sql=" select * from OAWorkFlowNodeCondition where flowNodeId=?";
								PreparedStatement pstmt3 = conn.prepareStatement(sql);
								pstmt3.setString(1, oldflowNodeId);
								ResultSet rss4 =pstmt3.executeQuery();
								while(rss4.next()){
									String oldcondId = "";
									String condId = IDGenerater.getId();
									fields = "";
									values = "";
									param = new ArrayList();
									for(int i = 0 ;i< rss4.getMetaData().getColumnCount();i++){
										String name = rss4.getMetaData().getColumnName(i+1);
										if("id".equals(name)){
											fields +=",id";
											values +=",?";
											param.add(condId);
											
											oldcondId = rss4.getString(name);											
										}else if("flowNodeId".equals(name)){
											fields +=",flowNodeId";
											values +=",?";
											param.add(flowNodeId);
											
											oldflowNodeId = rss4.getString(name);
										}else if("to".equals(name)){
											fields +=",[to]";
											values +=",?";
											param.add(rss4.getString(name));
										}else{
											fields +=","+name;
											values +=",?";
											param.add(rss4.getString(name));
										}
									}
									fields = fields.substring(1);
									values = values.substring(1);
									sql = "insert into OAWorkFlowNodeCondition("+fields+") values("+values+")";
									PreparedStatement pstmt4 = conn.prepareStatement(sql);
									for(int i = 0 ;i< param.size();i++){
										pstmt4.setString(i+1, param.get(i));
									}
									/*复制条件*/
									pstmt4.executeUpdate();
									/*复制条件字段*/
									sql=" insert into OAWorkFlowNodeConditionDet(id,conditionId,fieldName,display,andOrDisplay,andOr,valueDisplay,value,relationDisplay,relation,groupId,groupType) " +
											"select substring(replace(newid(),'-',''),1,30),?,fieldName,display,andOrDisplay,andOr,valueDisplay,value,relationDisplay,relation,groupId,groupType from OAWorkFlowNodeConditionDet where conditionId=?";
									pstmt4 = conn.prepareStatement(sql);
									pstmt4.setString(1, condId);
									pstmt4.setString(2, oldcondId);
									pstmt4.execute();									
								}
								
							}							
							
							if("1".equals(template.getDesignType())){ //如果是个性化工作流生成的表单，则需要向OAWorkFlowTableVersion中插入一条数据
								String tableName = template.getTemplateFile();
								sql="select top 1 * from  OAWorkFlowTableVersion where  designId='"+keyId+"' and tableId=(select id from tbldbtableInfo where tableName='"+tableName+"' ) order by createTime desc";
								pstmt = conn.prepareStatement(sql);
								rss3 = pstmt.executeQuery();
								if (rss3.next()) {
									String tableId=rss3.getString("tableId");
									String layOutHtml=rss3.getString("layOutHtml");
									sql="insert into OAWorkFlowTableVersion (id,tableId,layOutHtml,createBy,createTime,saveType,designId) values(?,?,?,?,?,?,?) ";
									pstmt = conn.prepareStatement(sql);
									pstmt.setString(1, IDGenerater.getId());
									pstmt.setString(2, tableId);
									pstmt.setString(3, layOutHtml);
									pstmt.setString(4, userId);
									pstmt.setString(5, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
									pstmt.setString(6, "add");
									pstmt.setString(7, newFlowName.substring(0, newFlowName.indexOf(".")));
									pstmt.execute();
								}
							}
						}
					});
	                BaseEnv.workFlowInfo.put(nTemplate.getId(), nTemplate) ;
	                BaseEnv.workFlowInfo.put(nTemplate.getTemplateFile(), nTemplate) ;
	                if(nTemplate.getLines()!=null && nTemplate.getLines().length()>0){
	                	PublicMgt pMgt = new PublicMgt();
	                	BaseEnv.workFlowDesignBeans.put(nTemplate.getId(), pMgt.getWorkFlowDesign(nTemplate.getId(),session));
	                }else{
		                String fileName = keyId + ".xml";
		    			// 复制工作流文件
		    			File newFile = null;
		    			File oldFile = null;
		    			if (SystemState.instance.dogState == SystemState.DOG_FORMAL
		    					&& "0".equals(SystemState.instance.dogId)) {
		    				oldFile = new File(getWokeFlowPath(fileName));
		    				newFile = new File(getWokeFlowPath(nTemplate.getId() + ".xml"));
		    			} else {
		    				oldFile = new File(getUserWokeFlowPath(fileName));
		    				newFile = new File(getUserWokeFlowPath(nTemplate.getId() + ".xml"));
		    			}
		    			copyFile(oldFile, newFile);
		    			// 加载OA的工作流设计文件
		    			BaseEnv.workFlowDesignBeans.put(nTemplate.getId(), ReadXML.parse(newFile.getName()));
	                }
				} catch (Exception e) {
					e.printStackTrace();
					rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
				}
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
	
	public String getWokeFlowPath(String fileName) {
		String path = BaseEnv.FILESERVERPATH;
		if (!path.trim().endsWith("/")) {
			path = path.trim() + "/";
		}
		String dir = path += "wokeflow/";
		File file = new File(dir);
		if (!file.exists()) {
			try {
				file.mkdirs();
			} catch (Exception ex) {
			}
		}
		path = dir + fileName;
		return path;
	}

	public String getUserWokeFlowPath(String fileName) {
		String path = BaseEnv.FILESERVERPATH;
		if (!path.trim().endsWith("/")) {
			path = path.trim() + "/";
		}
		String dir = path += "userWokeflow/";
		File file = new File(dir);
		if (!file.exists()) {
			try {
				file.mkdirs();
			} catch (Exception ex) {
			}
		}
		path = dir + fileName;
		return path;
	}
	
	/**
	 * 复制文件
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	public static void copyFile(File oldFile, File newFile) {
		try {
			int bytesum = 0;
			int byteread = 0;
			if (oldFile.exists()) {
				newFile.createNewFile();
				InputStream inStream = new FileInputStream(oldFile);
				FileOutputStream fs = new FileOutputStream(newFile);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    /**
     * 获取模板数据
     * @param keyId
     * @return
     */
    public String getWorkFlowDesingById(final String keyId){
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try {
                        	Statement st=conn.createStatement(); 
                        	String sql ="select * from OAWorkFlowTemplate where id='"+keyId+"' ";                        	
                        	ResultSet rst=st.executeQuery(sql);
                        	if(rst.next()){
                        		String tableName = "" ;
                        		String flowFile = "" ;
                        		ResultSetMetaData metaData = rst.getMetaData() ;
                        		StringBuilder insertSQL = new StringBuilder() ;
                        		insertSQL.append("insert into OAWorkFlowTemplate(") ;
                        		for(int i=1;i<=metaData.getColumnCount();i++){
                        			String columnName = metaData.getColumnName(i) ;
                        			insertSQL.append(columnName+",") ;
                        		}
                        		insertSQL.deleteCharAt(insertSQL.length()-1) ;
                        		insertSQL.append(") values(") ;
                        		for(int i=1;i<=metaData.getColumnCount();i++){
                        			String columnName = metaData.getColumnName(i) ;
                        			String value = rst.getString(columnName) ;
                        			if("id".equals(columnName)){
                        				value = IDGenerater.getId() ;
                        			}
                        			if("templateFile".equals(columnName)){
                        				tableName = value ;
                        			}
                        			if("workFlowFile".equals(columnName)){
                        				flowFile = value ;
                        			}
                        			if(value==null){
                        				insertSQL.append(value+",") ;
                        			}else{
                        				insertSQL.append("'"+value+"',") ;
                        			}
                        			
                        		}
                        		insertSQL.deleteCharAt(insertSQL.length()-1) ;
                        		insertSQL.append(");") ;
                        		insertSQL.append("@koron@"+tableName) ;
                        		insertSQL.append("@koron@"+flowFile) ;
                        		rs.setRetVal(insertSQL.toString()) ;
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
        String sql = "" ;
        if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS&&rs.retVal!=null){
        	sql = rs.retVal.toString();
        }
        return sql;
    }
 
    /**
     * 判断工作流程表中是否存在该表名的工作流
     * @param templateName
     * @return
     */
    public String isExistWorkFlow(String templateName){
    	String num="";
    	List<String> param=new ArrayList<String>();
    	String sql="SELECT COUNT(*) as num FROM OAWorkFlowTemplate OFt WHERE OFt.templateFile=? ";
    	param.add(templateName);
    	Result rs=this.sqlList(sql, param);
    	if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
    		List obj=(List) rs.retVal;
    		Object[] objList=(Object[]) obj.get(0);
    		num=objList[0].toString();
    	}
    	return num;
    	
    }
    
    /**
     * 执行SQL语句
     * @param keyId
     * @return
     */
    public Result executeSQL(final String sql){
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try { 
                        	Statement state = conn.createStatement() ;
                        	state.execute(sql) ;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.retCode = retCode ;
        return rs;
    }
    
    /**
     * 添加表单流程
     * @param gItem
     * @return
     */
	public Result addWorkFlow(OAWorkFlowDesignForm oaFlowForm){
		String createTime = BaseDateFormat.format(new java.util.Date(), BaseDateFormat.yyyyMMddHHmmss);
		List<String> param=new ArrayList<String>();
		String sql="insert into OAWorkFlowTemplate(id,templateFile,templateClass,templateName,createTime,statusId,templateStatus,designType,templateType)values(?,?,?,?,?,?,?,?,?)";
		param.add(""+oaFlowForm.getId()+"");
		param.add(""+oaFlowForm.getTemplateFile()+"");
		param.add(""+oaFlowForm.getTemplateClass()+"");
		param.add(""+oaFlowForm.getTemplateName()+"");
		param.add(""+createTime+"");
		param.add(""+oaFlowForm.getStatusId()+"");
		param.add(""+oaFlowForm.getTemplateStatus()+"");
		param.add(""+oaFlowForm.getDesignType()+"");
		param.add(""+oaFlowForm.getTemplateType()+"");
		return this.msgSql(sql, param);
	}
	
    /**
	 * jdbc调用公共方法
	 * @param sql
	 * @param param
	 * @return
	 */
	public Result msgSql(final String sql, final List param) {
       final Result rst = new Result();
       int retCode = DBUtil.execute(new IfDB() {
           public int exec(Session session) {
               session.doWork(new Work() {
                   public void execute(Connection conn) throws
                           SQLException {
                       try {
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            for(int i = 1;i<=param.size();i++){
                                pstmt.setObject(i,param.get(i-1));
                            }
                           int row = pstmt.executeUpdate();
                           if (row > 0) {
                              rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                              rst.setRealTotal(row);
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
	 * 
	 * 查询所有的职员分组
	 * @param keyIds 客户的Id
	 * @return
	 */
	public HashMap<String, String> selectEmpGroupMap() {
		Result rs = new Result();
		HashMap<String, String> groupMap = new HashMap<String, String>();
		try {
			String sql = "select id from tblEmpGroup where statusId=0 ";
			Result result = sqlList(sql, new ArrayList<String>());
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				List objList = (List) result.retVal;
				for(int i=0;i<objList.size();i++){
					String groupId = String.valueOf(((Object[])objList.get(i))[0]) ;
					groupMap.put(groupId, groupId);
				}
			}
		} catch (Exception ex) {
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			BaseEnv.log.error("OAWorkFLowTempMgt---moduleQueryByName method :" + ex);
		}
		return groupMap;
	}
	
	
	/**
	 * 根据模板名称查找模板id,处理启用审核流UPDATE语句
	 * 
	 * @param keyIds 客户的Id
	 * @return
	 */
	public String moduleQueryByName(String tableName,Connection conn) {
		Result rs = new Result();
		String moduleId = "";
		try {
			String sql = "select id from CRMClientModule WHERE tableInfo like '"+tableName+"%'";
			System.out.println(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rset = ps.executeQuery();
    		if(rset.next()){
    			if("".equals(moduleId)){
    				moduleId = rset.getString("id"); 
    			}
    		}
		} catch (SQLException ex) {
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			ex.printStackTrace();
			BaseEnv.log.error("CRMClientMgt---insertCRMCLientInfoLog method :" + ex);
		}
		return moduleId;
	}
	
	/**
	 * 添加流程文件 到 数据库中
	 * @return
	 */
	public Result addFlowData(final HashMap<String, FlowNodeBean> flowMap,final String flowxml,final String flowId){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	        	   /*更新流程图*/
	        	   Result rs = loadBean(flowId, OAWorkFlowTemplate.class, session);	   
	        	   if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
	        		   result.retCode = rs.retCode;
	        		   result.retVal = rs.retVal;
	        		   return rs.retCode;
	        	   }
	        	   OAWorkFlowTemplate template = (OAWorkFlowTemplate) rs.retVal;
	        	   template.setLines(flowxml);
	        	   template.setFileFinish("1");
	        	   rs = updateBean(template, session);
	        	   if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
	        		   result.retCode = rs.retCode;
	        		   result.retVal = rs.retVal;
	        		   return rs.retCode;
	        	   }
	        	   //先删除 后插入
        		   rs = deleteBean(flowId, FlowNodeBean.class,"flowId",session);
        		   if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS && rs.retCode != ErrorCanst.ER_NO_DATA){
	        		   result.retCode = rs.retCode;
	        		   result.retVal = rs.retVal;
	        		   return rs.retCode;
	        	   }
        		   //由于目前出现多次条件无故重复多次的问题，所以这里做一下校验
	        	   for(FlowNodeBean flowNode : flowMap.values()){
	        		   //检查结点审核人是否重复
	        		   HashMap map  = new HashMap();
	        		   for(ApproveBean appB:flowNode.getApproveSet()){
	        			   String tempStr = appB.getFlowNode2().getId()+":"+appB.getUser()+":"+appB.getType();
	        			   if(map.get(tempStr) == null){
	        				   map.put(tempStr,tempStr);
	        			   }else{
	        				   result.retVal = "结点"+flowNode.getDisplay()+"审核人"+appB.getUserName()+"重复";
	        				   return ErrorCanst.DEFAULT_FAILURE;
	        			   }
	        		   }
	        		   //检查结点审核人是否重复
	        		   map  = new HashMap();
	        		   for(ConditionsBean appB:flowNode.getConditionSet()){
	        			   String tempStr = appB.getFlowNode3().getId()+":"+appB.getTo();
	        			   if(map.get(tempStr) == null){
	        				   map.put(tempStr,tempStr);
	        				   //检查条件结点中的条件项是否重复
	        				   for(ConditionBean cB:appB.getConditions()){
	        					   String cs = "DET:"+cB.getConditionsBean().getId()+":"+cB.getFieldName()+":"+cB.getAndOr()+":"+cB.getValue()+":"+cB.getRelation()+":"+cB.getGroupId()+":"+cB.getGroupType();
	        					   if(map.get(cs) == null){
	    	        				   map.put(cs,cs);	    	        				   
	    	        			   }else{
	    	        				   result.retVal = "结点"+flowNode.getDisplay()+"条件"+appB.getDisplay()+"字段"+cB.getDisplay()+"重复";
	    	        				   return ErrorCanst.DEFAULT_FAILURE;
	    	        			   }
	        				   }
	        			   }else{
	        				   result.retVal = "结点"+flowNode.getDisplay()+"条件"+appB.getDisplay()+"重复";
	        				   return ErrorCanst.DEFAULT_FAILURE;
	        			   }
	        		   }
	        		   
	        		   //检查结点字段是否重复
	        		   map  = new HashMap();
	        		   for(FieldBean appB:flowNode.getFieldSet()){
	        			   String tempStr = appB.getFlowNode().getId() + ":" + appB.getFieldName()+":"+appB.getInputType()+":"+appB.isNotNull()+":"+appB.getFieldType();
	        			   if(map.get(tempStr) == null){
	        				   map.put(tempStr,tempStr);
	        			   }else{
	        				   result.retVal = "结点"+flowNode.getDisplay()+"字段"+appB.getFieldName()+"重复";
	        				   return ErrorCanst.DEFAULT_FAILURE;
	        			   }
	        		   }
	        		   rs =addBean(flowNode,session);
	        		   if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
		        		   result.retCode = rs.retCode;
		        		   result.retVal = rs.retVal;
		        		   return rs.retCode;
		        	   }
	        	   }
	        	   
	        	   
	        	   /*删除旧的流程文件*/
	        	   File flowFile =null;
	           	   if (SystemState.instance.dogState == SystemState.DOG_FORMAL
	       				&& "0".equals(SystemState.instance.dogId)) {
	           		   flowFile = new File(ReadXML.getWokeFlowPath(flowId+".xml")) ;
	           	   }else{
	           		   flowFile = new File(ReadXML.getUserWokeFlowPath(flowId+".xml")) ;
	           	   }
	           	   if(flowFile.exists()){
	           		   //flowFile.delete();
	           	   }
	           	   //从新加载到内在中
	           	   WorkFlowDesignBean design = new WorkFlowDesignBean();
	           	   design.setFlowNodeMap(flowMap);
	           	   BaseEnv.workFlowDesignBeans.put(flowId, design);
	           	   BaseEnv.workFlowInfo.put(flowId, template);
	           	   if(template.getStatusId()==0){//只有当前版本修改时才更新，否则修改老版的工作流会覆盖新版工作流
	           		   BaseEnv.workFlowInfo.put(template.getTemplateFile(), template);
	           	   }
	        	   return 0;
	           }
	    });
		result.retCode = retCode;
		return result ;
	}
	
	/**
	 * 查询办公自动化的流程	 
	 * @param sqlList
	 * @return
	 */
	public Result queryFlowSetting(String sqlList){
		ArrayList param = new ArrayList();
		String sql = "select F.id,F.createBy,E.DepartmentCode,E.id,F.classCode,F.isCatalog,F.number,F.moduleName ";
		/*if(!"0".equals(flowShow)){
			sql += " ,(select count(F.moduleName) from tblWorkFlowType F left join tblEmployee E on F.createBy= E.id  where F.isCatalog <> '1' and F.classCode like '00002%') as chlidCount ";
		}	*/		
		sql +=" from tblWorkFlowType F left join tblEmployee E on F.createBy= E.id ";
		if(sqlList !=null && !"".equals(sqlList)){
			sql += sqlList;
		}
		System.out.println(sql);
		return sqlList(sql, param);	
	}
	
	/**
	 * 更新工作流设置
	 * @param oldValues
	 * @param newValues
	 * @param loginId
	 * @return
	 */
	public Result updateFlowSetting(final ArrayList<String> newValues,final String loginId){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {   
                    		                		                    		
                    		//查询出以前最大classCode
                    		String maxsql = "select top 1 classCode from tblWorkFlowType where classCode like '00002%' and isCatalog <> '1' order by classCode desc";
                    		PreparedStatement pes = conn.prepareStatement(maxsql);
                    		ResultSet rs = pes.executeQuery();
                    		String classCode = "0000200001";
                    		if(rs.next()){
                    			classCode = rs.getString("classCode");
                    		}
                    		
                    		//删除所有00002——下面的子级
                    		String sql = "delete tblWorkFlowType where classCode like '00002%' and isCatalog <> '1'";
                    		PreparedStatement ps = conn.prepareStatement(sql);
              		  		ps.executeUpdate();               		  		                               		
                    		//进行新增 
                    		String hql = "insert into tblWorkFlowType(id,classCode,isCatalog,workFlowNode,workFlowNodeName,number,moduleName,createBy,lastUpdateBy,createTime,lastUpdateTime,statusId,SCompanyID,checkPersons,finishTime,checkPersont)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";             
              		  		PreparedStatement pss = conn.prepareStatement(hql); 
              		  		
              		  		int classkey =Integer.parseInt(classCode);             		  		
                    		for (String newKey : newValues) {                     			
                    			if("0".equals(newKey.split(";")[2])){
                    				classkey = classkey+1;
                    				classCode = "0000"+classkey;
                    				pss.setString(2, classCode);
                    			}else if(newKey.split(";")[2].indexOf("0000200000") != -1){
                    				//兼容老数据   
                    				pss.setString(2, "000020000"+newKey.split(";")[2].substring(10));
                    			}else{
                    				pss.setString(2, newKey.split(";")[2]);
                    			}                       		
                        		pss.setString(1, IDGenerater.getId());                      		
                        		pss.setString(3, "0");
                        		pss.setString(4, "-1");
                        		pss.setString(5, "finish");             		
                        		pss.setString(6, newKey.split(";")[0]);
                        		pss.setString(7, newKey.split(";")[1]);             		
                        		pss.setString(8, loginId);
                        		pss.setString(9, loginId);
                        		pss.setString(10, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
                        		pss.setString(11, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));           		
                        		pss.setString(12, "0");
                        		pss.setString(13, "00001");
                        		pss.setString(14, "");
                        		pss.setString(15, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
                        		pss.setString(16, "");
                        		pss.executeUpdate();                         		
							}                  		              		  		            		  		             		  		
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					BaseEnv.log.error("OAWorkFlowSettingMgt updateFlowSetting : ", ex) ;
        					result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
        				}
                    }
                });				
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;	
	}
	
	/***
	 * 获取表单设计模板
	 * @return
	 */
	/*	Map<String,String> fromMap = new HashMap<String,String>();
			fromMap.put("调薪申请", "tiaoxinshenqing");	*/
    public Result queryFromTemp() {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select templateName,templateFile from OAWorkFlowTemplate where templateStatus = 1";
					
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ResultSet rs = ps.executeQuery();
							Map<String,String> fromMap = new HashMap<String,String>();
							while(rs.next()){
								fromMap.put(rs.getString(1), rs.getString(2));
							}
							rst.setRetVal(fromMap);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("OAWorkFlowTempMgt queryFromTemplate data Error.", ex);
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
}
