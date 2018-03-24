package com.koron.oa.workflow;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.bsf.BSFException;
import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.ApproveBean;
import com.koron.oa.bean.ConditionBean;
import com.koron.oa.bean.ConditionsBean;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.MyWorkFlow;
import com.koron.oa.bean.OAMyWorkFlowDetBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.menyi.aio.bean.AuditeDeliveranceBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.BusinessException;
import com.menyi.web.util.ConfirmBean;
import com.menyi.web.util.DefineSQLBean;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.ErrorMessage;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.NotifyFashion;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

/**
 * 
 * Title: 我的工作流数据库操作类
 * Description: 主要功能工作流的查询，审核
 *
 * @Date:Dec 18, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class OAMyWorkFlowMgt extends AIODBManager {

	/**
	 * 添加单据的时候，向我的工作流插入数据
	 * @throws BSFException 
	 */
	public Result addOAMyWorkFlow(final String designId,final String tableName,final HashMap values,
			final LoginBean loginBean,final Locale locale,final MessageResources mr,
			final boolean isOAWorkFlow,final String saveType) throws BSFException{
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						try{
							addOAMyWorkFlow(designId, tableName, values, loginBean, locale,
									mr, conn) ;
						}catch(Exception ex){
							ex.printStackTrace();
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
	    }) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}
	
	/**
	 * 添加单据的时候，向我的工作流插入数据
	 * @throws BSFException 
	 */
	public Result addOAMyWorkFlow(final String designId,final String tableName,final HashMap values,
			final LoginBean loginBean,final Locale locale,final MessageResources mr,
			final Connection conn){
		
		Result rst = new Result() ;
		try{
			BaseEnv.log.debug("插入OAMyWorkFlow表开始 designId="+designId+";tableName="+tableName+";id="+values.get("id")+
					";loginBean="+(loginBean==null?"null":loginBean.getId())+";locale="+locale);
			String sql = "insert into OAMyWorkFlow(id,billNo,applyDate,applyBy,department,applyType,checkPerson,"
				+ "tableName,keyId,applyContent,createBy,currentNode,nextNodeIds,benchmarkTime,departmentCode,statusId,createTime,lastUpdateTime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" ;
			OAWorkFlowTemplate workFlow=BaseEnv.workFlowInfo.get(designId);
			String oaTitle="";
			if(tableName.equals("OAWorkFlowTable")){
				oaTitle=values.get("oaTitle").toString();
			}else{
				if(workFlow.getTitleTemp()!=null&&workFlow.getTitleTemp().length()>0){
					DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(tableName) ;
					oaTitle = replaceFieldNameByValue(tableInfo, values, workFlow.getTitleTemp(), locale.toString(),loginBean,conn) ;
				}
			}
			PreparedStatement pss = conn.prepareStatement(sql) ;
			pss.setString(1, values.get("id").toString()) ;
			pss.setString(2,values.get("id").toString()) ;
			pss.setString(3, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd)) ;
			pss.setString(4, loginBean.getEmpFullName()) ;
			pss.setString(5, loginBean.getDepartmentName()) ;
			pss.setString(6, designId) ;
			pss.setString(7, ";"+loginBean.getId()+";");
			pss.setString(8, tableName) ;
			pss.setString(9, values.get("id").toString()) ;
			pss.setString(10, oaTitle);
			pss.setString(11, loginBean.getId()) ;
			pss.setString(12, "0") ;
			pss.setString(13, "");
			pss.setDouble(14, 0);
			pss.setString(15, loginBean.getDepartCode());						
			pss.setInt(16, 0);
			pss.setString(17, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			pss.setString(18, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			int num = pss.executeUpdate() ;
						
			if(num>0){
				rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
			}else{
				rst.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
				return rst;
			}
			//插入OAMyWorkFlowPerson
			sql = " insert into OAMyWorkFlowPerson(keyId,tableName,checkperson,hadApprover,curApprover) values(?,?,?,0,1) ";
			pss = conn.prepareStatement(sql) ;
			pss.setString(1, values.get("id").toString()) ;
			pss.setString(2,tableName) ;
			pss.setString(3,loginBean.getId()) ;
			pss.execute();
			String insertTableName = new DynDBManager().getInsertTableName(tableName);//处理CRM多模板表名
			sql="update "+insertTableName+" set workFlowNode='0',checkPersons=';"+loginBean.getId()+";' where id='"+values.get("id")+"'";
			pss = conn.prepareStatement(sql) ;
			pss.execute();
			rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
		}catch (Exception e) {
			BaseEnv.log.error("OAMyWorkFlowMgt.addOAMyWorkFlow Error :",e);
			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
		}
		return rst ;
	}
	
	
	public Result getRelayWorkFlow(){
		final Result res = new Result();
				
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//找出
							String sql =  " select a.tableName,a.applyType,a.currentNode," +
									"isnull((select top 1 endTime+';'+cast(isnull(oaTimeLimitUnit,0) as varchar(2))+';'+cast(isnull(benchMarkTime,0) as varchar(20)) from OAMyWorkFlowDet c where c.f_ref=a.id and c.nodeId!=a.currentNode and c.statusId=0  and isnull(endTime,'')<> ''  order by c.sortOrder desc),'') lastEndTime," +
									"isnull(a.checkPerson,'') checkPerson,isnull(a.lastNoteTime,'') lastNoteTime,a.keyId,a.nextNodeIds,a.applyContent,a.lastNodeId,a.id from OAMyWorkFlow a where a.currentNode not in ('0','-1') ";
							PreparedStatement pss = conn.prepareStatement(sql);
							ResultSet rss = pss.executeQuery();
							while (rss.next()) {
								try{
									String tableName = rss.getString(1);
									String applayType = rss.getString(2);
									String currentNode = rss.getString(3);
									String lastEndTime = rss.getString(4);
									String checkPerson = rss.getString(5);		
									String lastNoteTime = rss.getString(6);	
									String billId = rss.getString(7);	
									String nextNodeIds = rss.getString(8);
									String title = rss.getString(9);
									String lastNodeId = rss.getString(10);
									String flowId = rss.getString(11);
									
									
									if(lastNoteTime == null || lastNoteTime.indexOf(":") == -1){
										lastNoteTime = "";
									}else{
										if(lastNoteTime.substring(0,lastNoteTime.indexOf(":")).equals(currentNode)){
											lastNoteTime = lastNoteTime.substring(lastNoteTime.indexOf(":")+1);
										}else{
											lastNoteTime = "";
										}
									}
									if(applayType == null || applayType.length() ==0 ){
										continue;
									}
									final OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(applayType);
									
									//未设置提醒方式，直接跳过
									if(template == null || template.getOverTimeWake() == null || template.getOverTimeWake().length() ==0) 
										continue;
									WorkFlowDesignBean  designBean=BaseEnv.workFlowDesignBeans.get(applayType);
									if(designBean == null) 
										continue;
									FlowNodeBean nodeB = designBean.getFlowNodeMap().get(currentNode);
									//未提醒过，且设置有提前提醒
									if(nodeB != null && (lastNoteTime == null || lastNoteTime.length() ==0 ) && nodeB.getNoteTime() != 0 ){//有提前提醒要求
										Date standDate = getStandDate(nodeB, lastEndTime,0,0);	//取标准的结束时间。
										Date finishDate=new Date();
										//计算提前天数
										Date noteDate = getStandDate(nodeB, lastEndTime,nodeB.getNoteTime(),nodeB.getNoteTimeUnit());	//取标准的提醒时间。
										if(noteDate !=null &&  noteDate.compareTo(new Date()) <0 && template.getOverTimeWake() != null){
											//提醒各用户
											hurryTrans("", applayType, checkPerson, billId, nextNodeIds, currentNode, "0", "0", 
													new Locale("zh", "CN"), "note", title, template.getOverTimeWake(), "noteTime", lastNodeId, null, "", null, conn);
											try{
												Thread.sleep(300);
											}catch(Exception e){}
											String tsql  = " update OAMyWorkFlow set lastNoteTime= ? where id = ?  ";
											PreparedStatement tpsmt = conn.prepareStatement(tsql);
											tpsmt.setString(1, currentNode +":"+ BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
											tpsmt.setString(2, flowId);
											tpsmt.execute();
										}									
									}
									if(nodeB != null && nodeB.getNoteRate() != 0 ){//有超时提醒要求
										Date standDate = getStandDate(nodeB, lastEndTime,0,0);	//取标准的结束时间。
										Date finishDate=new Date();
										//计算提前天数   
										if(standDate != null && standDate.compareTo(new Date()) < 0){ //当前时间已经超过标准时间
											String upEndTime = BaseDateFormat.format(standDate, BaseDateFormat.yyyyMMddHHmmss);
											if(lastNoteTime != null && lastNoteTime.length()  >0 ){ //说明已经提醒过。
												upEndTime = lastNoteTime;
											}
											
											String workTimeOfAM=BaseEnv.systemSet.get("WorkTimeOfAM").getSetting();//上午班 格式：9:00-12:00
											String workTimeOfPM=BaseEnv.systemSet.get("WorkTimeOfPM").getSetting();//下午班 格式：13:30-18:00
											
											int indexA=workTimeOfAM.indexOf("-");
											int indexB=workTimeOfPM.indexOf("-");
											Date ams=BaseDateFormat.parse("2000-01-01 "+workTimeOfAM.substring(0,indexA)+":00" , BaseDateFormat.yyyyMMddHHmmss);
											Date ame=BaseDateFormat.parse("2000-01-01 "+workTimeOfAM.substring(indexA+1)+":00" , BaseDateFormat.yyyyMMddHHmmss);
											Date pms=BaseDateFormat.parse("2000-01-01 "+workTimeOfPM.substring(0,indexB)+":00" , BaseDateFormat.yyyyMMddHHmmss);
											Date pme=BaseDateFormat.parse("2000-01-01 "+workTimeOfPM.substring(indexB+1)+":00" , BaseDateFormat.yyyyMMddHHmmss);
											
											long amtime=(ame.getTime()-ams.getTime());//上午的办理时间  
											long pmtime=(pme.getTime()-pms.getTime());//下午的办理时间  
											
											String temp=(nodeB.getNoteRate()*100)+"";
											long templ=Long.parseLong(temp.substring(0,temp.indexOf(".")));
											long setTimeS = 0;
											if(nodeB.getNoteRateUnit()==0){
												setTimeS=templ*(amtime+pmtime)/100;
											}else if(nodeB.getNoteRateUnit()==1){
												setTimeS=templ*60*60*1000/100;
											}else{
												setTimeS=templ*60*1000/100;
											}
											
											Date noteDate = getStandDate(upEndTime,setTimeS);	//取标准的提醒时间。
											
											if(noteDate.compareTo(new Date()) <0 && template.getOverTimeWake() != null){
												//提醒各用户
												hurryTrans("", applayType, checkPerson, billId, nextNodeIds, currentNode, "0", "0", 
														new Locale("zh", "CN"), "note", title, template.getOverTimeWake(), "noteRate", lastNodeId, null, "", null, conn);
												try{
													Thread.sleep(300);
												}catch(Exception e){}
												
												String tsql  = " update OAMyWorkFlow set lastNoteTime= ? where id = ?  ";
												PreparedStatement tpsmt = conn.prepareStatement(tsql);
												tpsmt.setString(1, currentNode +":"+ BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
												tpsmt.setString(2, flowId);
												tpsmt.execute();
											}	
										}
									}
								} catch (Exception ex) {
									res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
									BaseEnv.log.error("OAMyWorkFLowMgt.getRelayWorkFlow Error:",ex);
								}	
								
							}
						} catch (Exception ex) {
							res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("OAMyWorkFLowMgt.getRelayWorkFlow Error:",ex);
						}
					}
				});
				return res.getRetCode();
			}
		});
		res.setRetCode(retCode);
		return res;
	}
	
	/**
	 * 替换【】中字段值
	 * @param tableInfo
	 * @param values
	 * @param content
	 * @return
	 */
	public String replaceFieldNameByValue(DBTableInfoBean tableInfo,HashMap values,
			String content,String locale,LoginBean loginBean,Connection conn){
		if(locale.equalsIgnoreCase("zh_cn"))
			locale = "zh_CN";
		if(content==null || content.length()==0)return "" ;
		List<String> listField = getFieldName(content) ;
		for(String fieldName : listField){
			String strField = fieldName.substring(1,fieldName.length()).substring(0,fieldName.length()-2) ;
			if("登录人".equals(strField)){
				content = content.replace(fieldName, loginBean.getEmpFullName());
			}
			for(DBFieldInfoBean dbField : tableInfo.getFieldInfos()){
				if(!tableInfo.getTableName().startsWith("Flow_")){
					if(dbField.getDisplay()!=null && strField.equals(dbField.getDisplay().get(locale))){
						Object strValue = "" ;
						if(dbField.getInputType()==DBFieldInfoBean.INPUT_MAIN_TABLE||dbField.getInputTypeOld()==DBFieldInfoBean.INPUT_MAIN_TABLE){
							String varFieldName = "" ;
							for (int j = 0;j < dbField.getSelectBean().getViewFields().size(); j++) {
	                    		PopField posf = ((PopField) dbField.getSelectBean().getViewFields().get(j));
	                    		if(posf.getDisplay()!=null && posf.parentDisplay==true){
	                    			varFieldName = posf.getAsName().replace(".", "_") ;
	                    			break ;
	                    		}
							} 
							if(varFieldName != null && varFieldName.length() > 0){
								Result rss= getMainInputValue(dbField,(String)values.get(varFieldName), loginBean.getSunCmpClassCode(), values, loginBean.getId(),conn);
					            if(rss.retCode==ErrorCanst.DEFAULT_SUCCESS&&rss.retVal!=null){
					            	strValue =rss.retVal.toString() ;
					            }
							}
						}else if(dbField.getInputType()==DBFieldInfoBean.INPUT_ENUMERATE || dbField.getInputTypeOld()==DBFieldInfoBean.INPUT_ENUMERATE||
								dbField.getInputType()==DBFieldInfoBean.INPUT_RADIO || dbField.getInputTypeOld()==DBFieldInfoBean.INPUT_RADIO){
							strValue = GlobalsTool.getEnumerationItemsDisplay(dbField.getRefEnumerationName(), 
																	values.get(dbField.getFieldName())+"", locale) ;
						}else if(dbField.getInputType()==DBFieldInfoBean.INPUT_CHECKBOX || dbField.getInputTypeOld()==DBFieldInfoBean.INPUT_CHECKBOX){
							strValue = GlobalsTool.getCheckBoxDisplay(dbField.getRefEnumerationName(), 
																	values.get(dbField.getFieldName())+"", locale) ;
						}else if(dbField.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE){
							strValue = values.get(dbField.getFieldName());
							if(strValue!=null && strValue.toString().length()>0){
								strValue = GlobalsTool.round(Double.parseDouble(String.valueOf(strValue)), 2);
							}
						}else{
							strValue = values.get(dbField.getFieldName()) ;
						}
						if(strValue!=null && strValue.toString().length()>0){
							content = content.replace(fieldName, String.valueOf(strValue)) ;
						}else{
							content = content.replace(fieldName, "") ;
						}
						break ;
					}
				}else{ //个性化表单设计产生的表结构
					Object strValue = "" ;
					if(strField.equals(dbField.getLanguageId())){
						if("employee".equals(dbField.getDefaultValue()) || "dept".equals(dbField.getDefaultValue())
								|| "client".equals(dbField.getDefaultValue()) ){
							String pageValue=(String) values.get(dbField.getFieldName());
							if(pageValue!=null){
								String[] pageValList=pageValue.split(";");
								for(String pValue:pageValList){
									String temp="";
									if("employee".equals(dbField.getDefaultValue())){
										temp = GlobalsTool.getEmpFullNameByUserId(pValue);
									}else if("dept".equals(dbField.getDefaultValue())){
										temp =(String) BaseEnv.deptMap.get(pValue);
									}else if("client".equals(dbField.getDefaultValue())){
										temp = findClientNameById2(pValue,conn);
									}
									if(pageValList.length>1){
										strValue=strValue+temp+",";
									}else{
										strValue=temp;
									}
								}
							}
						}else if("popup".equals(dbField.getDefaultValue())){
							Result rs = getPopValue(values, dbField, loginBean);
							if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
								List  objList= (List) rs.retVal;
								Object[] obj = (Object[]) objList.get(0) ;
								strValue=obj[0].toString();
							}
							
						}else if(dbField.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE){
							strValue = GlobalsTool.round(Double.parseDouble(String.valueOf(strValue)), 2);
						}else{
							strValue = values.get(dbField.getFieldName()) ;
						}
						if(strValue!=null && strValue.toString().length()>0){
							content = content.replace(fieldName, String.valueOf(strValue)) ;
						}else{
							content = content.replace(fieldName, "") ;
						}
						break ;
					}
				}
			}
		}
		return content ;
	}
	
	/**
	 * 获取自定义弹出框显示内容
	 * @param tableMap
	 * @param fieldBean
	 * @param loginBean
	 * @return
	 */
	public Result getPopValue(final HashMap<String, String> tableMap, final DBFieldInfoBean fieldBean,final LoginBean loginBean) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String popuValue ="";
							for(String str : tableMap.get(fieldBean.getFieldName()).split(";")){
								String refsql = new DynDBManager().getRefSql(fieldBean,false,str, "00001", new Hashtable(),false,tableMap,loginBean.getId());
								if (refsql != null && fieldBean.getSelectBean()!=null) {
									ResultSet crset = conn.createStatement().executeQuery(refsql);
									if (crset.next()) {
										String temp=String.valueOf(crset.getObject(1));
										popuValue=popuValue+temp+",";
									}
								}
							}
							if(popuValue.length()>0){
								rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
								rst.setRetVal(popuValue);
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
	
	public String findClientNameById2(String keyId,Connection conn){
		String sql = "SELECT moduleId,clientName FROM CRMClientInfo WHERE id='"+keyId+"'";
		Statement stmt;
		String temp="";
		try {
			stmt = conn.createStatement();
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next()){
				temp = rs.getString("clientName");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
        return temp;
	}
	
	/**
     * 获取属性的值
     * @param attr 
     * @param str
     * @return
     */
    private List<String> getFieldName(String str) {
    	List<String> listAtt = new ArrayList<String>() ;
        Pattern pattern = Pattern.compile("(\\[[\\/\\.\\w\\:\\-\\u4e00-\\u9fa5]+\\])");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
        	listAtt.add(matcher.group(1));
        }
        return listAtt;
    }
	
	/**
	 * 根据用户设置的办理时限或者节点处设置的办理时限检查流程是否有超时
	 * @param bean
	 * @param endTime 实际结束时间
	 * @param lastEndTime 上次转交时间;当前节点指定时间单位;当前节点指定时间
	 * @return 
	 * @throws IllegalArgumentException
	 * @throws ParseException
	 */
	public int compareFinishDate(FlowNodeBean bean,String limitUnit,String benchTime,String endTime,String preEndTime) throws Exception{
		Date finishDate = BaseDateFormat.parse(endTime, BaseDateFormat.yyyyMMddHHmmss);
		Date standDate = this.getStandDate2(bean,limitUnit, benchTime,preEndTime);
		if(standDate!=null){
			return finishDate.compareTo(standDate);
		}else{
			return 0;
		}
	}
	
	public Date getStandDate2(FlowNodeBean bean,String limitUnit, String benchTime,String preEndTime) {
		try{
			long setTimeS=0;
			String temp="";
			long templ=0;
			Date finishDate = BaseDateFormat.parse(preEndTime, BaseDateFormat.yyyyMMddHHmmss);
			if(bean.isForwardTime()){//转交时设置办理时限
				if(benchTime == null || limitUnit== null){
					return new Date();
				}
				float setTime=Float.parseFloat(benchTime);//当前节点设置限期处理时间
				if(limitUnit.indexOf(".")>=0){
					limitUnit = limitUnit.substring(0,limitUnit.indexOf("."));
				}
				temp = (setTime*100)+"";
				templ  =Long.parseLong(temp.substring(0,temp.indexOf(".")));
				if("0".equals(limitUnit)){
					setTimeS=templ*24*60*60*1000/100;
				}else if("1".equals(limitUnit)){
					setTimeS=templ*60*60*1000/100;
				}else{
					setTimeS=templ*60*1000/100;
				}
			}else if(bean.getTimeLimit()>0){//节点处设置办理时限			
				
				temp=(bean.getTimeLimit()*100)+"";
				templ=Long.parseLong(temp.substring(0,temp.indexOf(".")));
				if(bean.getTimeLimitUnit()==0){
					setTimeS=templ*24*60*60*1000/100;
				}else if(bean.getTimeLimitUnit()==1){
					setTimeS=templ*60*60*1000/100;
				}else{
					setTimeS=templ*60*1000/100;
				}
			}else if(bean.getTimeLimit() == 0){
				return new Date();
			}
			
			long time = finishDate.getTime() + setTimeS;
			return new Date(time);
		}catch(Exception e){
			
		}
		return new Date();
	}
	
	public Date getStandDate(FlowNodeBean bean,String lastEndTime,float noteTime,float noteTimeUnit) throws IllegalArgumentException, ParseException{
		String[] lastEndTimes=lastEndTime.split(";");
		String upEndTime=lastEndTimes[0];//上个节点结束时间
		String setTimeUnit=lastEndTimes[1];//当前节点设置限期处理时间单位
		if(lastEndTimes[2]==null){
			return null;
		}
		float setTime=Float.parseFloat(lastEndTimes[2]);//当前节点设置限期处理时间
		
		Date upEndDate=BaseDateFormat.parse(upEndTime, BaseDateFormat.yyyyMMddHHmmss);
		String workTimeOfAM=BaseEnv.systemSet.get("WorkTimeOfAM").getSetting();//上午班 格式：9:00-12:00
		String workTimeOfPM=BaseEnv.systemSet.get("WorkTimeOfPM").getSetting();//下午班 格式：13:30-18:00
		
		int indexA=workTimeOfAM.indexOf("-");
		int indexB=workTimeOfPM.indexOf("-");
		Date ams=BaseDateFormat.parse("2000-01-01 "+workTimeOfAM.substring(0,indexA)+":00" , BaseDateFormat.yyyyMMddHHmmss);
		Date ame=BaseDateFormat.parse("2000-01-01 "+workTimeOfAM.substring(indexA+1)+":00" , BaseDateFormat.yyyyMMddHHmmss);
		Date pms=BaseDateFormat.parse("2000-01-01 "+workTimeOfPM.substring(0,indexB)+":00" , BaseDateFormat.yyyyMMddHHmmss);
		Date pme=BaseDateFormat.parse("2000-01-01 "+workTimeOfPM.substring(indexB+1)+":00" , BaseDateFormat.yyyyMMddHHmmss);
		
		long amtime=(ame.getTime()-ams.getTime());//上午的办理时间  
		long pmtime=(pme.getTime()-pms.getTime());//下午的办理时间  
		
		
		long setTimeS=0;
		String temp="";
		long templ=0;
		if(setTimeUnit.indexOf(".")>=0){
			setTimeUnit=setTimeUnit.substring(0,setTimeUnit.indexOf("."));
		}
		if(bean.isForwardTime()){//转交时设置办理时限
			temp=(setTime*100)+"";
			templ=Long.parseLong(temp.substring(0,temp.indexOf(".")));
			if(Integer.parseInt(setTimeUnit)==0){
				setTimeS=templ*(amtime+pmtime)/100;
			}else if(Integer.parseInt(setTimeUnit)==1){
				setTimeS=templ*60*60*1000/100;
			}else{
				setTimeS=templ*60*1000/100;
			}
		}else if(bean.getTimeLimit()>0){//节点处设置办理时限
			temp=(bean.getTimeLimit()*100)+"";
			templ=Long.parseLong(temp.substring(0,temp.indexOf(".")));
			if(bean.getTimeLimitUnit()==0){
				setTimeS=templ*(amtime+pmtime)/100;
			}else if(bean.getTimeLimitUnit()==1){
				setTimeS=templ*60*60*1000/100;
			}else{
				setTimeS=templ*60*1000/100;
			}
		}
		
		if(noteTime > 0){
			temp=(noteTime*100)+"";
			templ=Long.parseLong(temp.substring(0,temp.indexOf(".")));
			if(noteTimeUnit==0){
				setTimeS= setTimeS - templ*(amtime+pmtime)/100;
			}else if(noteTimeUnit==1){
				setTimeS=setTimeS - templ*60*60*1000/100;
			}else{
				setTimeS=setTimeS - templ*60*1000/100;
			}
		}
		
		return getStandDate(upEndTime, setTimeS);
	}
	public Date getStandDate(String upEndTime,long setTimeS)  throws IllegalArgumentException, ParseException{
		if(setTimeS>0){
			//分析当前节点标准完成时间
			ArrayList holidlist=BaseEnv.holidayInfo;
			long setTimeSecod=setTimeS;
			long standUse=0;
			Date standDate=null;
			String lastHTime=upEndTime;			
			
			Date upEndDate = BaseDateFormat.parse(upEndTime, BaseDateFormat.yyyyMMddHHmmss);
			Date lastHoliday=upEndDate;
			
			String workTimeOfAM=BaseEnv.systemSet.get("WorkTimeOfAM").getSetting();//上午班 格式：9:00-12:00
			String workTimeOfPM=BaseEnv.systemSet.get("WorkTimeOfPM").getSetting();//下午班 格式：13:30-18:00
			int indexA=workTimeOfAM.indexOf("-");
			int indexB=workTimeOfPM.indexOf("-");
			Date ams=BaseDateFormat.parse("2000-01-01 "+workTimeOfAM.substring(0,indexA)+":00" , BaseDateFormat.yyyyMMddHHmmss);
			Date ame=BaseDateFormat.parse("2000-01-01 "+workTimeOfAM.substring(indexA+1)+":00" , BaseDateFormat.yyyyMMddHHmmss);
			Date pms=BaseDateFormat.parse("2000-01-01 "+workTimeOfPM.substring(0,indexB)+":00" , BaseDateFormat.yyyyMMddHHmmss);
			Date pme=BaseDateFormat.parse("2000-01-01 "+workTimeOfPM.substring(indexB+1)+":00" , BaseDateFormat.yyyyMMddHHmmss);
			long amtime=(ame.getTime()-ams.getTime());//上午的办理时间  
			long pmtime=(pme.getTime()-pms.getTime());//下午的办理时间  
			
			boolean flag=true;
			while(flag){
				String ued=lastHTime.substring(0,11);
				if(!holidlist.contains(ued)){
					Date amst=BaseDateFormat.parse(ued+workTimeOfAM.substring(0,indexA)+":00" , BaseDateFormat.yyyyMMddHHmmss);
					Date amet=BaseDateFormat.parse(ued+workTimeOfAM.substring(indexA+1)+":00" , BaseDateFormat.yyyyMMddHHmmss);
					Date pmst=BaseDateFormat.parse(ued+workTimeOfPM.substring(0,indexB)+":00" , BaseDateFormat.yyyyMMddHHmmss);
					Date pmet=BaseDateFormat.parse(ued+workTimeOfPM.substring(indexB+1)+":00" , BaseDateFormat.yyyyMMddHHmmss);
					
					//解析当天转交时可办理的工作时间
					setTimeSecod=setTimeSecod-standUse;
					if(lastHoliday.compareTo(amst)<=0){//在上午班之前转交
						if(amtime>=setTimeSecod){
							standDate=amst;
							standDate.setTime(standDate.getTime()+setTimeSecod);
							break;
						}else if((amtime+pmtime)>=setTimeSecod){
							standDate=pmst;
							standDate.setTime(standDate.getTime()+(setTimeSecod-amtime));
							break;
						}else{
							standUse+=amtime+pmtime;
						}
					}else if(lastHoliday.compareTo(amst)>0&&lastHoliday.compareTo(amet)<=0){//在上午班时转交
						long amuse=amet.getTime()-lastHoliday.getTime();
						if(amuse>=setTimeSecod){
							standDate=lastHoliday;
							standDate.setTime(standDate.getTime()+setTimeSecod);
							break;
						}else if((amuse+pmtime)>=setTimeSecod){
							standDate=lastHoliday;
							standDate.setTime(standDate.getTime()+(setTimeSecod-amuse));
							break;
						}else{
							standUse+=amuse+pmtime;
						}
					}else if(lastHoliday.compareTo(pmst)<=0){//在下午班之前转交
						if(pmtime>=setTimeSecod){
							standDate=pmst;
							standDate.setTime(standDate.getTime()+setTimeSecod);
							break;
						}else{
							standUse+=pmtime;
						}
					}else if(lastHoliday.compareTo(pmst)>0&&lastHoliday.compareTo(pmet)<=0){//在下午班时转交
						long pmuse=pmet.getTime()-lastHoliday.getTime();
						if(pmuse>=setTimeSecod){
							standDate=lastHoliday;
							standDate.setTime(standDate.getTime()+setTimeSecod);
							break;
						}else{
							standUse+=pmuse;
						}
					}
				}
				lastHoliday=BaseDateFormat.parse(ued, BaseDateFormat.yyyyMMdd);
				lastHoliday.setTime(lastHoliday.getTime()+(24*60*60*1000));
				lastHTime=BaseDateFormat.format(lastHoliday, BaseDateFormat.yyyyMMddHHmmss); 
			}
			return standDate;
		}
		return null;
	}
	
	/**
	 * 用于列表显示流程描述
	 * @param designId
	 * @param billId
	 * @return
	 * @throws Exception 
	 */
	public String[] getFlowDepict(String designId,String billId,Connection conn,Locale locale) throws Exception{
		
		StringBuffer flowDepict = new StringBuffer();
		/*已经审核过的结点*/
		String sql = "select a.nodeID,a.checkPerson,a.endTime,a.oaTimeLimitUnit,a.benchmarkTime from OAMyWorkFlowDet a " 
				   + " where a.f_ref=? and a.statusId=0 and len(a.endTime)>0  order by a.sortOrder ";
		PreparedStatement pss = conn.prepareStatement(sql);
		pss.setString(1, billId);
		ResultSet rs = pss.executeQuery();
		WorkFlowDesignBean designBean=BaseEnv.workFlowDesignBeans.get(designId);
		String preEndTime = null;
		while(rs.next()){
			String nodeId = rs.getString(1);
			String checkPerson = rs.getString(2);
			String endTime = rs.getString(3);
			String limitUnit = rs.getString(4);
			String benchTime = rs.getString(5);
			FlowNodeBean nodeBean = designBean.getFlowNodeMap().get(nodeId);
			OnlineUser user = OnlineUserInfo.getUser(checkPerson);
			if(compareFinishDate(nodeBean, limitUnit, benchTime,endTime,preEndTime)>0){
				if(user != null){
					flowDepict.append("<font  class=\"fTimeout\">"+user.getName()+"("+nodeBean.getDisplay()+")-></font>");
				}
			}else{
				if(user != null){
					flowDepict.append("<font class=\"fComp\">"+user.getName()+"("+nodeBean.getDisplay()+")-></font>");
				}
			}		
			preEndTime = endTime;
		}
		/*当前结点*/
		sql = "select checkPerson,currentNode from OAMyWorkFlow where id=?";
		pss = conn.prepareStatement(sql);
		pss.setString(1, billId);
		rs = pss.executeQuery();
		String currentNode = "";
		String checkPerson = "";
		if(rs.next()){
			currentNode = rs.getString("currentNode");
			checkPerson = rs.getString("checkPerson");
		}
		if("-1".equals(currentNode)){
			flowDepict.append("<font class=\"fComp\">结束</font>");
		}else{
			FlowNodeBean nodeBean = designBean.getFlowNodeMap().get(currentNode);
			String[] checkPersons = checkPerson.split(";");
			for(String person : checkPersons){
				OnlineUser user = OnlineUserInfo.getUser(person);
				if(user!=null){
					flowDepict.append("<font class=\"fCur\">"+user.getName()+"("+nodeBean.getDisplay()+")</font>");
				}
			}
		}
		String [] flow=new String[]{flowDepict.toString(),""};
		return flow;
	}
	
	
	/*
	 * 拿当前单据的所有会签意见
	 */
	public Result getDeliverance(final String billId,final String type,final String tableName){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						
						String sql="select a.nodeId, b.id,b.deliverance,b.attTime,b.createBy,c.empFullName,isnull(a.endTime,''),b.affix from OAMyWorkFlowDet a,"
								 + " tblAuditeDeliverance b,tblEmployee c where b.createBy =c.id  and a.id=b.f_ref " ;
						if(tableName!=null && tableName.length()>0){
							sql += " and a.f_ref=(select id from OAMyWorkFlow where keyId=? and tableName=?)" ;
						}else{
							sql += " and a.f_ref=?" ;
						}
						if("affix".equals(type)){
							sql += " and b.affix is not null and len(b.affix)>0" ;
						}else{
							sql += " and b.deliverance is not null and len(b.deliverance)>0" ;
						}
						sql += " order by b.attTime" ;
						PreparedStatement pss= conn.prepareStatement(sql) ;
						pss.setString(1, billId) ;
						if(tableName!=null && tableName.length()>0){
							pss.setString(2, tableName) ;
						}
						ResultSet rs=pss.executeQuery(); 
						ArrayList list=new ArrayList();
						while(rs.next()){
							AuditeDeliveranceBean bean=new AuditeDeliveranceBean();
							bean.setNodeId(rs.getString(1));
							bean.setId(rs.getString(2));
							bean.setDeliverance(rs.getString(3));
							bean.setAttTime(rs.getString(4));
							bean.setCreateBy(rs.getString(5));
							bean.setEmpFullName(rs.getString(6));
							bean.setEndTime(rs.getString(7));
							bean.setAffix(rs.getString(8)) ;
							list.add(bean);
						}
						rst.setRetVal(list);
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}
	
	public Result updateFlowDepict(final String designId,final String id,final Locale locale){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {	
						updateFlowDepict(designId, id, locale,conn) ;
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}
	
	/**
	 * 更新流程描述信息
	 * @param designId
	 * @param id
	 * @param locale
	 * @param mr
	 * @param conn
	 * @return
	 */
	public Result updateFlowDepict(final String designId,final String id,final Locale locale,final Connection conn){
		Result result = new Result() ;
		try{
			String [] flowDepicts=getFlowDepict(designId,id,conn,locale);
			String sql="update OAMyWorkFlow set flowDepict=?,flowDepictTitle=? where id=?";
			PreparedStatement pss = conn.prepareStatement(sql) ;
			pss.setString(1, flowDepicts[0]) ;
			pss.setString(2, flowDepicts[1]) ;
			pss.setString(3,id) ;
			pss.executeUpdate();
		}catch(Exception ex){
			BaseEnv.log.error("OAMyWorkFlowMgt.updateFlowDepict ",ex);
			result.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
		}
		return result ;
	}
	
	/**
	 * 办理工作流时，确定工作流的下一节点
	 * @throws BSFException 
	 */
	public Result update(String tableName,final HashMap values,final LoginBean loginBean,
			final Locale locale,final Connection conn,final MessageResources mr){
		
		Result result = new Result() ;
		try{
			String designId = getDesignId(String.valueOf(values.get("id")), tableName,conn);
			OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(designId);
			if(designId==null || designId.length()==0){
				workFlow = BaseEnv.workFlowInfo.get(tableName);
				designId = workFlow.getId();
				addOAMyWorkFlow(designId, tableName, values, loginBean, locale, mr, conn);
			}
			String oaTitle="";
			if(workFlow.getTitleTemp()!=null&&workFlow.getTitleTemp().length()>0){
				DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(tableName) ;
				oaTitle = replaceFieldNameByValue(tableInfo, values, workFlow.getTitleTemp(), locale.toString(),loginBean,conn) ;
			}
			//修改当前单据的工作流信息
			String sql="update OAMyWorkFlow set applyContent=? where id=? " ;
			PreparedStatement pss= conn.prepareStatement(sql) ;
			pss.setString(1, oaTitle) ;
			pss.setString(2,values.get("id").toString()) ;
			pss.executeUpdate() ;
			
		}catch (Exception ex) {
			ex.printStackTrace() ;
			result.setRetCode(ErrorCanst.BILL_UPDATE_WORK_FLOW_ERROR) ;
		}
		return result ;
	}
	
	public void insertDeliverance(Connection conn,String flowId,String currNodeId,String deliverance,String loginId,String affix) throws SQLException{
		if((deliverance!=null&&deliverance.length()>0)||(affix!=null&&affix.length()>0)){
			//先查询出当前用户当前节点已经办理的工作流明细
			String sql="select top 1 id  from  OAMyWorkFlowDet where f_ref=? and nodeId=? and checkPerson=? order by sortOrder desc";
			PreparedStatement pss = conn.prepareStatement(sql) ;
			pss.setString(1,flowId) ;
			pss.setString(2,currNodeId);
			pss.setString(3,loginId) ;
			ResultSet rst = pss.executeQuery();
			if(rst.next()){
				String id=rst.getString(1);
				sql="insert into tblAuditeDeliverance (id,f_ref,deliverance,affix,attTime,createBy) values (?,?,?,?,?,?)";
				pss = conn.prepareStatement(sql) ;
				pss.setString(1, IDGenerater.getId()) ;
				pss.setString(2, id);
				pss.setString(3, deliverance==null?"":deliverance) ;
				pss.setString(4, affix==null?"":affix);
				pss.setString(5,BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)) ;
				pss.setString(6, loginId) ;
				pss.executeUpdate();
			}
		}
	}
	
	/**
	 * 添加附件
	 * @param flowId
	 * @param scompanyID
	 * @param currNodeId
	 * @param deliverance
	 * @param loginBean
	 * @param affix
	 * @return
	 */
	public Result addAffix(final String keyId,final String scompanyID,final LoginBean loginBean,
			final String affix,final String deliverance,final String tableName){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select id,applyType from OAMyWorkFlow where keyId=? and tableName=?";
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1,keyId) ;
							pss.setString(2, tableName) ;
							ResultSet rss= pss.executeQuery();
							if(rss.next()){
								String flowId = rss.getString("id");
								String designId = rss.getString("applyType");
								OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(designId);
								if(template.getLines()!=null){
									sql="insert into OAMyWorkFlowDet (id,f_ref,startTime,endTime,checkPerson,createBy,nodeType,approvalOpinions,affix,statusId) values (?,?,?,?,?,?,?,?,?,1)";
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, IDGenerater.getId()) ;
									pss.setString(2, keyId);
									pss.setString(3, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
									pss.setString(4, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)) ;
									pss.setString(5, loginBean.getId()) ;
									pss.setString(6, loginBean.getId()) ;
									pss.setString(7, "affix"); 
									pss.setString(8, deliverance);
									pss.setString(9, affix);
									pss.executeUpdate();
								}else{/*旧版流程*/
									sql="select top 1 id  from  OAMyWorkFlowDet where f_ref=? order by sortOrder desc";
									pss = conn.prepareStatement(sql) ;
									pss.setString(1,flowId) ;
									ResultSet rst = pss.executeQuery();
									String nodeId = "";
									if(rst.next()){
										nodeId=rst.getString(1);
										sql="insert into tblAuditeDeliverance (id,f_ref,deliverance,affix,attTime,createBy,SCompanyID,statusId) values (?,?,?,?,?,?,?,?)";
										pss = conn.prepareStatement(sql) ;
										pss.setString(1, IDGenerater.getId()) ;
										pss.setString(2, nodeId);
										pss.setString(3, deliverance) ;
										pss.setString(4, affix==null?"":affix);
										pss.setString(5,BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)) ;
										pss.setString(6, loginBean.getId()) ;
										pss.setString(7, scompanyID);
										pss.setInt(8, -1) ;
										pss.executeUpdate();
									}
								}
							}
						} catch (Exception e) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							e.printStackTrace();
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode);
		return rst ;
	}
	
	/**
	 * 删除附件
	 * @param flowId
	 * @param scompanyID
	 * @param currNodeId
	 * @param deliverance
	 * @param loginBean
	 * @param affix
	 * @return
	 */
	public Result deleteAffix(final String flowId,final String affix){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						try {
							String sql="select top 1 id  from  OAMyWorkFlowDet where f_ref=? order by sortOrder desc";
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1,flowId) ;
							ResultSet rst = pss.executeQuery();
							String nodeId = "";
							if(rst.next()){
								nodeId=rst.getString(1);
							}
							sql="delete from tblAuditeDeliverance where f_ref=? and affix=?";
							pss = conn.prepareStatement(sql) ;
							pss.setString(1,nodeId) ;
							pss.setString(2,affix) ;
							pss.executeUpdate() ;
						} catch (Exception e) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							e.printStackTrace();
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode);
		return rst ;
	}
	
	public Result getMainInputValue(final DBFieldInfoBean fi,final String code, final String sunCompany,final HashMap values,final String userId) throws BSFException{
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						String refsql;
						try {
							refsql = new DynDBManager().getRefSql(fi,false,values.get(fi.getFieldName()).toString(), 
									sunCompany, BaseEnv.tableInfos,false,values,userId);
						
							Statement st=conn.createStatement();
							ResultSet rs=st.executeQuery(refsql);
							if(rs.next()){
								for(int i=0;i<fi.getSelectBean().getViewFields().size();i++){
									PopField popField=(PopField)fi.getSelectBean().getViewFields().get(i);
									if(popField.getDisplay()!=null&&popField.getDisplay().length()>0){
										rst.setRetVal(rs.getString(i+1));
										break;
									}
								}
							}
						} catch (Exception e) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							e.printStackTrace();
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode);
		return rst ;
	}
	public Result getMainInputValue(final DBFieldInfoBean fi,final String code, final String sunCompany,final HashMap values,final String userId,Connection conn){
		Result rst=new Result();
		String refsql;
		try {
			refsql = new DynDBManager().getRefSql(fi,false,values.get(fi.getFieldName()).toString(), 
					sunCompany, BaseEnv.tableInfos,false,values,userId);
			
			refsql = refsql.replaceAll("(?i)\\bEMPRIGHT\\b", ""); //去掉语句中的部门临时标志
			refsql = refsql.replaceAll("(?i)\\bNORIGHT\\b", ""); //去掉语句中的部门临时标志
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery(refsql);
			if(rs.next()){
				for(int i=0;i<fi.getSelectBean().getViewFields().size();i++){
					PopField popField=(PopField)fi.getSelectBean().getViewFields().get(i);
					if(popField.getDisplay()!=null&&popField.getDisplay().length()>0){
						rst.setRetVal(rs.getString(i+1));
						break;
					}
				}
			}
		} catch (Exception e) {
			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			e.printStackTrace();
		} 
						
		return rst ;
	}
	/**
	 * 当用户点击办理按钮时，记录此用户开始办理此工作流的时间
	 * @throws BSFException 
	 */
	public Result transactStart(final String currNodeId,final String id,
			final LoginBean loginBean,final String tableName){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						//先查询当前用户是否已经办理过，如果没有办理则插入工作流明细
						String sql = "select id from OAMyWorkFlow where tableName='"+tableName+"' and keyId='"+id+"'" ;
						String flowId = id ;
						PreparedStatement pss = conn.prepareStatement(sql) ;
						ResultSet rss = pss.executeQuery() ;
						if(rss.next()){
							flowId = rss.getString("id") ;
						}
						sql="select id  from  OAMyWorkFlowDet where f_ref=? and nodeId=? and checkPerson=? " +
							 "and statusId=0 and (endTime is null or len(endTime)=0)";
						pss = conn.prepareStatement(sql) ;
						pss.setString(1,flowId) ;
						pss.setString(2,currNodeId);
						pss.setString(3,loginBean.getId()) ;
						ResultSet rst = pss.executeQuery();
						if(!rst.next()){
							sql="insert into OAMyWorkFlowDet (id,f_ref,nodeId,checkPerson,startTime,createBy,nodeType,statusId) values (?,?,?,?,?,?,?,?)";
							pss = conn.prepareStatement(sql) ;
							pss.setString(1, IDGenerater.getId()) ;
							pss.setString(2, flowId);
							pss.setString(3, currNodeId) ;
							pss.setString(4, loginBean.getId());
							pss.setString(5,BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)) ;
							pss.setString(6, loginBean.getId()) ;
							pss.setString(7, "transact"); 
							pss.setInt(8, 0);
							pss.executeUpdate();
						} 
						
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ; 
		rst.setRetCode(retCode) ;
		return rst ;
	}
	
	/**
	 * 得到当前单据的当前节点
	 * @param values
	 * @return
	 */
	public Result getCurrNodeId(final String id,final String tableName){
		final Result rs = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						String sql="select currentNode  from  OAMyWorkFlow where keyId=? and tableName=?";
						PreparedStatement pss = conn.prepareStatement(sql) ;
						pss.setString(1,id) ;
						pss.setString(2,tableName) ;
						ResultSet rst = pss.executeQuery();
						if(rst.next()){
							rs.setRetVal(rst.getString(1));
						}else{
							rs.setRetVal("0");
						}
						
					}
				}) ;
				return rs.getRetCode() ;
			}
		}) ; 
		rs.setRetCode(retCode) ;
		return rs ;
	}
	
	/**
	 * 查询 当前单据的流程id
	 * @param values
	 * @return
	 */
	public String getDesignIdByKeyId(final String keyId,final String tableName){
		final Result rs = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						String sql="select applyType  from  OAMyWorkFlow where keyId=? and tableName=?";
						PreparedStatement pss = conn.prepareStatement(sql) ;
						pss.setString(1,keyId) ;
						pss.setString(2,tableName) ;
						ResultSet rst = pss.executeQuery();
						if(rst.next()){
							rs.setRetVal(rst.getString("applyType"));
						}
					}
				}) ;
				return rs.getRetCode() ;
			}
		}) ; 
		if(retCode == ErrorCanst.DEFAULT_SUCCESS){
			return (String)rs.retVal ;
		}
		return "" ;
	}
	
	/**
	 * 得到当前单据的当前节点的审核人
	 * @param values
	 * @return
	 */
	public Result getCurrCheckPerson(final String id){
		final Result rs = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						String sql="select checkPerson  from  OAMyWorkFlow where id=? ";
						PreparedStatement pss = conn.prepareStatement(sql) ;
						pss.setString(1,id) ;
						ResultSet rst = pss.executeQuery();
						if(rst.next()){
							rs.setRetVal(rst.getString(1));
						} 
						
					}
				}) ;
				return rs.getRetCode() ;
			}
		}) ; 
		rs.setRetCode(retCode) ;
		return rs ;
	}

	/**
	 * 得到当前单据此用户的最后办理的节点
	 * @param values
	 * @return
	 */
	public Result getUserLastNode(final String id ,final String userId,final String tableName){
		final Result rs = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						String sql="select nodeID from OAMyWorkFlowDet where f_ref=(select id from OAMyWorkFlow where keyId=? and tableName=?) and checkPerson=? order by sortOrder desc";
						PreparedStatement pss = conn.prepareStatement(sql) ;
						pss.setString(1,id) ;
						pss.setString(2,tableName) ;
						pss.setString(3,userId) ;
						ResultSet rst = pss.executeQuery();
						if(rst.next()){
							rs.setRetVal(rst.getString(1));
						}else{
							rs.setRetVal("");
						} 
					}
				}) ;
				return rs.getRetCode() ;
			}
		}) ; 
		rs.setRetCode(retCode) ;
		return rs ;
	}
	
	/**
	 * 得到当前单据此用户的最后办理的节点
	 * @param values
	 * @return
	 */
	public Result getFlowCreateBy(final String id){
		final Result rs = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						String sql="select createBy from OAMyWorkFlow where keyId=?";
						PreparedStatement pss = conn.prepareStatement(sql) ;
						pss.setString(1,id) ;
						ResultSet rst = pss.executeQuery();
						if(rst.next()){
							rs.setRetVal(rst.getString(1));
						} 
						
					}
				}) ;
				return rs.getRetCode() ;
			}
		}) ; 
		rs.setRetCode(retCode) ;
		return rs ;
	}
	
	/**
	 * 得到当前节点转交时应该出现的节点，当前节点的下一节点如果可以略过，那么查询下下个节点
	 * 当前方法在新增保存或者用户办理流程保存时执行
	 * @param designId
	 * @param currNodeId
	 * @param values
	 * @param tableName
	 * @param conn
	 * @return
	 * @throws BSFException 
	 * @throws BSFException
	 */
	public String getNextNodeIds(String designId,String currNodeId,HashMap values,String tableName,Connection conn) throws Exception{
		ArrayList nextNodeIdList=new ArrayList();
		String nextNodeIds="";
		WorkFlowDesignBean designBean=BaseEnv.workFlowDesignBeans.get(designId);
		HashMap<String,FlowNodeBean> flowNodeMap=designBean.getFlowNodeMap();
		FlowNodeBean currNodeBean=flowNodeMap.get(currNodeId); 
		values.put("designId", designId);
		FlowNodeBean nextNodeBean=flowNodeMap.get(currNodeBean.getTo());
		this.recursFlowNode(flowNodeMap, nextNodeBean, nextNodeIdList, values,tableName,conn);
		for(int i=0;i<nextNodeIdList.size();i++){
			nextNodeIds+=nextNodeIdList.get(i)+";";
		}
		if(!nextNodeIds.contains("-1;")&&currNodeBean.isAllowStop()){
			nextNodeIds+="-1;";
		}
		return nextNodeIds;
	}
	
	/**
	 * 递归取出下级节点
	 * @param flowNodeMap
	 * @param currNodeBean
	 * @param nextNodeIds
	 * @param values
	 * @param tableName
	 * @param conn
	 * @throws BSFException
	 */
	@SuppressWarnings("unchecked")
	private void recursFlowNode(HashMap<String,FlowNodeBean> flowNodeMap,FlowNodeBean nextNodeBean,ArrayList nextNodeIds,
			HashMap values,String tableName,Connection conn) throws Exception{
		if("CHOICE".equals(nextNodeBean.getZAction())){
            for(ConditionsBean consBean : nextNodeBean.getConditionList()){
            	String conds = "";
            	/*条件分组*/
            	HashMap<String, List<ConditionBean>> condMap = new HashMap<String, List<ConditionBean>>();
            	for(ConditionBean conBean : consBean.getConditions()){
            		List<ConditionBean> condList = condMap.get(conBean.getGroupId());
            		if(condList==null){
            			condList = new ArrayList<ConditionBean>();
            			condList.add(conBean);
            			condMap.put(conBean.getGroupId(), condList);
            		}else{
            			condList.add(conBean);
            		}
            	}
            	for(String key : condMap.keySet()){
            		conds += "(";
	            	for(ConditionBean conBean : condMap.get(key)){
	            		String value = values.get(conBean.getFieldName())==null?"":String.valueOf(values.get(conBean.getFieldName()));
	            		if(conBean.getFieldName().endsWith("_duty") 
	            				|| conBean.getFieldName().endsWith("_post")
	            				|| conBean.getFieldName().endsWith("_dept")){
	            			String fieldName = conBean.getFieldName().substring(0, conBean.getFieldName().length()-5);
	            			value = String.valueOf(values.get(fieldName));
	            			if(value!=null && value.length()>0){
		            			OnlineUser lineUser = OnlineUserInfo.getUser(value);
		            			if(lineUser==null) continue;
		            			if(conBean.getFieldName().endsWith("_duty")){
			            			value = lineUser.getTitleID();
			            		}else if(conBean.getFieldName().endsWith("_post")){
			            			value = lineUser.getPost();
			            		}else if(conBean.getFieldName().endsWith("_dept")){
			            			value = lineUser.getDeptId();
			            		}
	            			}
	            		}else{
		            		DBFieldInfoBean fieldBean = DDLOperation.getFieldInfo(BaseEnv.tableInfos, tableName, conBean.getFieldName());
	            			if(fieldBean==null) {
	            				//这里找不到条件对应字段，即报 错
	            				throw new Exception("条件结点字段"+conBean.getDisplay()+":"+conBean.getFieldName()+"不存在");
	            				
	            			}
	            			//复选框由于数据库保存的值后带有,要加逗号来区分
	            			if(fieldBean.getInputType() == DBFieldInfoBean.INPUT_CHECKBOX || fieldBean.getInputTypeOld() == DBFieldInfoBean.INPUT_CHECKBOX  ){
	            				conBean.setValue(conBean.getValue().endsWith(",")?conBean.getValue():conBean.getValue()+",");
	            			}
	            			if("employee".equals(fieldBean.getFieldName()) || "EmployeeID".equals(fieldBean.getFieldName())){
	            				//如果是职员弹出窗，按这个规则走
	            				/*String empNames=showNames("employee",fieldBean.getFieldName(),values);
	        					if("null".equals(empNames)|| empNames==null){
	        						empNames="";
	        					}
	        					value=empNames; */
	            			}else if("DepartmentCode".equals(fieldBean.getFieldName()) || "DepartmentCode".equals(fieldBean.getFieldName())){
	            				//如果是职员弹出窗，按这个规则走
	            				/*String deptNames=showNames("dept",fieldBean.getFieldName(),values);
	        					if("null".equals(deptNames)|| deptNames==null){
	        						deptNames="";
	        					}
	        					value=deptNames;*/
	            			}else if(!fieldBean.getFieldName().contains("field_")){
			            		if(fieldBean!=null &&fieldBean.getInputType()==DBFieldInfoBean.INPUT_MAIN_TABLE){
			            			value = getMainTableValue(fieldBean, values, conBean, conn);
			            		}
		            		}else{
		            			String temp="";
		            			if("employee".equals(fieldBean.getDefaultValue())){
		        					String empNames=showNames("employee",fieldBean.getFieldName(),values);
		        					if("null".equals(empNames)|| empNames==null){
		        						empNames="";
		        					}
		        					temp=empNames;
		        				}else if("dept".equals(fieldBean.getDefaultValue())){
		        					String deptNames=showNames("dept",fieldBean.getFieldName(),values);
		        					if("null".equals(deptNames)|| deptNames==null){
		        						deptNames="";
		        					}
		        					temp=deptNames;
		        				}else if("client".equals(fieldBean.getDefaultValue())){
		        					String clientNames=showNames("client",fieldBean.getFieldName(),values);
		        					if("null".equals(clientNames)|| clientNames==null){
		        						clientNames="";
		        					}
		        					temp=clientNames;
		        				}else if("popup".equals(fieldBean.getDefaultValue())){
		        					LoginBean loginBean=new GlobalsTool().getLoginBean();
		        					String popName=getPop(values, fieldBean, loginBean);
		        					if("null".equals(popName)|| popName==null){
		        						popName="";
		        					}
		        					temp=popName;
		        				}else{
		        					temp=values.get(fieldBean.getFieldName()).toString();
		        				}	
		            			value=temp;
		            		}
	            		}
	            		OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(values.get("designId"));
	            		if(template!=null && template.getLines()!=null && template.getLines().length()>0){
	            			conds += getConditionNew(conBean,value);
	            		}else{
	            			conds += getCondition(conBean,value);
	            		}
	            	}
	            	if(condMap.get(key).size()>0){
	            		conds = conds.substring(0, conds.length()-3);
	            		ConditionBean condition = condMap.get(key).get(0);
	            		conds += ") " + condition.getGroupType();
	            	}
            	}
            	if(conds.length()>3){
            		conds = conds.substring(0, conds.length()-3);
            		BaseEnv.log.debug("审核流条件："+conds);
            		if((Boolean)BaseEnv.evalManager.eval("javascript", "XX", 0, 0, conds)){
            			FlowNodeBean choiceNode =  flowNodeMap.get(consBean.getTo());
            			if(choiceNode!=null && "CHOICE".equals(choiceNode.getZAction())){
            				this.recursFlowNode(flowNodeMap, choiceNode, nextNodeIds, values, tableName, conn);
            			}else{
	                		nextNodeIds.add(consBean.getTo());
	                		nextNodeBean=flowNodeMap.get(consBean.getTo());
	                		break;
            			}
                	}
            	}
            }
		}else{
			nextNodeIds.add(nextNodeBean.getKeyId());
		}
		//允许跳过
		if(nextNodeBean.isAllowJump()){
			nextNodeBean = flowNodeMap.get(nextNodeBean.getTo());
			this.recursFlowNode(flowNodeMap, nextNodeBean, nextNodeIds, values,tableName,conn);		
		}
	}
	
	/**
	 * 拼接条件 旧流程设计
	 * @param conBean
	 * @return
	 */
	public String getCondition(ConditionBean conBean,String value) throws Exception {
		StringBuffer conds = new StringBuffer();
		if(conBean.getRelation().contains(">") || conBean.getRelation().contains("<")){
			try{ 
				Double.parseDouble(value);
			}catch(Exception e){
				throw new Exception(conBean.getDisplay()+ "("+value+")不是数字类型不可以用><号做等式");
			}
			conds.append(" " + value + conBean.getRelation() + conBean.getValue() + " " + conBean.getAndOr());
		}else if(conBean.getRelation().indexOf("not like")>=0){
			conds.append(" '" + value + "'.indexOf('" + conBean.getValue() + "')<0 " + conBean.getAndOr());
		}else if(conBean.getRelation().indexOf("like")>=0){
			conds.append(" '" + value + "'.indexOf('"+conBean.getValue()+"')>=0 " + conBean.getAndOr());
		}else{
			conds.append(" '" + value + "'" + conBean.getRelation() + "'" + conBean.getValue() + "' " + conBean.getAndOr());
		}
		return conds.toString();
	}
	
	/**
	 * 拼接条件 新流程设计
	 * @param conBean
	 * @return
	 */
	public String getConditionNew(ConditionBean conBean,String value) throws Exception{
		StringBuffer conds = new StringBuffer();
		if(conBean.getRelation().contains(">") || conBean.getRelation().contains("<")){
			try{ 
				Double.parseDouble(value);
			}catch(Exception e){
				throw new Exception(conBean.getDisplay()+ "("+value+")不是数字类型不可以用><号做等式");
			}

			conds.append(" " + value + conBean.getRelation() + conBean.getValueDisplay() + " " + conBean.getAndOr());
		}else if(conBean.getRelation().indexOf("not like")>=0){
			conds.append(" '" + value + "'.indexOf('" + conBean.getValue() + "')<0 " + conBean.getAndOr());
		}else if(conBean.getRelation().indexOf("like")>=0){
			conds.append(" '" + value + "'.indexOf('"+conBean.getValue()+"')>=0 " + conBean.getAndOr());
		}else{
			if(conBean.getFieldName().endsWith("_dept") 
					&& ("==".equals(conBean.getRelation()) || "!=".equals(conBean.getRelation()))){
				if("==".equals(conBean.getRelation())){
					conds.append(" '" + value + "'.indexOf('" + conBean.getValue() + "')>=0 " + conBean.getAndOr());
				}else{
					conds.append(" '" + value + "'.indexOf('"+conBean.getValue()+"')<0 " + conBean.getAndOr());
				}
			}else{
				conds.append(" '" + value + "'" + conBean.getRelation() + "'" + conBean.getValue() + "' " + conBean.getAndOr());
			}
		}
		return conds.toString();
	}
	
	
	
	public String getMainTableValue(DBFieldInfoBean fieldBean,HashMap values,ConditionBean conBean,Connection conn) throws Exception{
		PopupSelectBean bean=fieldBean.getSelectBean();
		String disName="";
		for(int k=0;k<bean.getDisplayField().size();k++){
			PopField popf=(PopField)bean.getDisplayField().get(k);
			if(popf.getDisplay()!=null&&popf.getDisplay().length()>0){
				disName=popf.getAsName();
			}
		}
		if(values.get(disName)==null){
			Result rs=null;
			if(conn==null){
				rs=this.getMainInputValue(fieldBean,values.get(conBean.getFieldName()).toString(), values.get("SCompanyID").toString(), values, values.get("createBy").toString());
			}else{
				rs=this.getMainInputValue(fieldBean,values.get(conBean.getFieldName()).toString(), values.get("SCompanyID").toString(), values, values.get("createBy").toString(),conn);
			}
			
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS&&rs.retVal!=null){
				values.put(disName, rs.getRetVal().toString());
			}else{
				values.put(disName, "");
			}
		}
		return values.get(disName).toString();
		
	}
	
	/**
	 * 根据在流程节点处选择的人员和选择的选人过滤规则，自动选人规则得到本节点的审核人
	 * @param checkPersons
	 * @param nodeBean
	 * @param appDepartment
	 * @return 得到所有的审核人
	 */
	public ArrayList<String[]> getNodeCheckPerson(FlowNodeBean nodeBean,String appDepartment,
			String id,Connection conn,String tableName,String loginId) throws BusinessException{
		//ArrayList<String []> checkPersons = new ArrayList<String[]>();
		HashMap<String, String[]> personsMap = new HashMap<String, String[]>();
		if(nodeBean.getAutoSelectPeople()==1){	//加上自动选人规则的人员:流程发起人
			Result rs=null;
			if(conn==null){
				rs=this.getBillCreateBy(id,tableName);
			}else{
				rs=this.getBillCreateBy(id,conn,tableName);
			}
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				String userId =rs.getRetVal().toString();
				if(personsMap.get(userId)==null){
					String userName=OnlineUserInfo.getUser(userId).getName();
					personsMap.put(userId, new String[]{userId,userName});
				}
			}
		}else if(nodeBean.getAutoSelectPeople() == 2){//选择直属上司
			Result rs = null;
			if(conn==null){
				rs = getBoss(id,tableName);
			}else{
				rs = getBoss(id,tableName,conn);
			}
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS && rs.getRetVal()!=null){
				String userId =rs.getRetVal().toString();
				if(personsMap.get(userId)==null){
					String userName=OnlineUserInfo.getUser(userId).getName();
					personsMap.put(userId, new String[]{userId,userName});
				}
			}
		}else if(nodeBean.getAutoSelectPeople() == 3){//自定义选人
			Result rs = null;
			if(conn==null){
				rs = getDefineSelectPeople(nodeBean.getAutoSelectPeopleSQL(),id,tableName);
			}else{
				rs = getDefineSelectPeople(nodeBean.getAutoSelectPeopleSQL(),id,tableName,conn);
			}
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS && rs.getRetVal()!=null){
				ArrayList<String>  list =(ArrayList<String>)rs.getRetVal();
				for(String userId:list){
					String userName=OnlineUserInfo.getUser(userId).getName();
					personsMap.put(userId, new String[]{userId,userName});
				}
			}else{
				throw new BusinessException(String.valueOf(rs.retVal));
			}
		}else{
			//解析用户选择的经办人
			for(int k=0;nodeBean.getApprovers()!=null&&k<nodeBean.getApprovers().size();k++){
				ApproveBean appBean=(ApproveBean)nodeBean.getApprovers().get(k);
				if("employee".equals(appBean.getType())){//直接选择人员
					personsMap.put(appBean.getUser(), new String[]{appBean.getUser(),appBean.getUserName()});
				}else if("duty".equals(appBean.getType())){//选择职位
					ArrayList<OnlineUser> users=OnlineUserInfo.getTitleUsers(appBean.getUser());
					for(int j=0;j<users.size();j++){
						personsMap.put(users.get(j).getId(), new String[]{users.get(j).getId(),users.get(j).getName()});
					}
				}else if("dept".equals(appBean.getType())){//选择部门
					ArrayList<OnlineUser> users=OnlineUserInfo.getDeptUser(appBean.getUser());
					for(int j=0;j<users.size();j++){
						if("0".equals(users.get(j).getStatusId())){
							personsMap.put(users.get(j).getId(), new String[]{users.get(j).getId(),users.get(j).getName()});
						}
					}
				}else if("group".equals(appBean.getType())){
					List<String[]> users = new ArrayList<String[]>();
					if(conn==null){
						users = getEmpByGroup(appBean.getUser());
					}else{
						users = getEmpByGroup(appBean.getUser(),conn) ;
					}
					for(int j=0;users!=null && j<users.size();j++){
						//checkPersons.add(users.get(j));
						personsMap.put(users.get(j)[0], users.get(j));
					}
				}else if("post".equals(appBean.getType())){
					ArrayList<OnlineUser> users = OnlineUserInfo.getPostUsers(appBean.getUser());
					for(int j=0;j<users.size();j++){
						personsMap.put(users.get(j).getId(), new String[]{users.get(j).getId(),users.get(j).getName()});
					}
				}
			}
		}
		ArrayList<String []> cps=new ArrayList<String[]>();
		//根据选人过滤规则过滤人员
		if(nodeBean.getFilterSet()==nodeBean.FILTER_OWNERDEPT){//只选择本部门级下级部门审核人
			for(String key : personsMap.keySet()){
				String []checkPerson = personsMap.get(key);
				if(OnlineUserInfo.getUser(checkPerson[0]) != null && OnlineUserInfo.getUser(checkPerson[0]).getDeptId().indexOf(appDepartment)==0){
					cps.add(checkPerson);
				}
			}
		}else if(nodeBean.getFilterSet()==nodeBean.FILTER_NOOWNERDEPT){//不选择本部门及子部门审核人
			for(String key : personsMap.keySet()){
				String []checkPerson = personsMap.get(key);
				if(OnlineUserInfo.getUser(checkPerson[0]) != null && OnlineUserInfo.getUser(checkPerson[0]).getDeptId().indexOf(appDepartment)!=0){
					cps.add(checkPerson);
				}
			}
		}else if(nodeBean.getFilterSet()==nodeBean.FILTER_OWNERDEPTUPPERDEPT){//只选择本部门及上级部门审核人
			String parentDepartment=appDepartment.substring(0,appDepartment.length()-5);
			for(String key : personsMap.keySet()){
				String []checkPerson = personsMap.get(key);
				if(OnlineUserInfo.getUser(checkPerson[0]) != null && (OnlineUserInfo.getUser(checkPerson[0]).getDeptId().equals(appDepartment) ||
						OnlineUserInfo.getUser(checkPerson[0]).getDeptId().equals(parentDepartment))){
					cps.add(checkPerson);
				}
			}
		}else if(nodeBean.getFilterSet()==nodeBean.FILTER_NOCREATER){//不选择制单人
			Result rs=null;
			if(conn==null){
				rs=this.getBillCreateBy(id,tableName);
			}else{
				rs=this.getBillCreateBy(id,conn,tableName);
			}
			String userId="";
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				userId =rs.getRetVal().toString();
			}
			for(String key : personsMap.keySet()){
				String []checkPerson = personsMap.get(key);
				if(!checkPerson[0].equals(userId)){
					cps.add(checkPerson);
				}
			}
		}else if(nodeBean.getFilterSet()==nodeBean.FILTER_UPPERDEPT){//如果没有上级部门，不显示任何审核人
			appDepartment=appDepartment.substring(0,appDepartment.length()-5);
			for(String key : personsMap.keySet()){
				String []checkPerson = personsMap.get(key);
				if(OnlineUserInfo.getUser(checkPerson[0]) != null && OnlineUserInfo.getUser(checkPerson[0]).getDeptId().equals(appDepartment)){
					cps.add(checkPerson);
				}
			}
		}else if(nodeBean.getFilterSet()==nodeBean.FILTER_LOWERDEPT){//如果没有下级部门，不显示任何审核人
			for(String key : personsMap.keySet()){
				String []checkPerson = personsMap.get(key);
				String deptId=OnlineUserInfo.getUser(checkPerson[0]) != null ?OnlineUserInfo.getUser(checkPerson[0]).getDeptId():"";
				deptId=deptId.substring(0,deptId.length()-5);
				if(deptId.equals(appDepartment)){
					cps.add(checkPerson);
				}
			}
		}else if(nodeBean.getFilterSet()==nodeBean.FILTER_FIRSTDEPT){
			appDepartment=appDepartment.substring(0,5);
			for(String key : personsMap.keySet()){
				String []checkPerson = personsMap.get(key);
				if(OnlineUserInfo.getUser(checkPerson[0]) != null && OnlineUserInfo.getUser(checkPerson[0]).getDeptId().equals(appDepartment)){
					cps.add(checkPerson);
				}
			}
		}else if(nodeBean.getFilterSet()==nodeBean.FILTER_DEFINESQL){//自定义过滤规则
			Result rs = null;
			if(conn==null){
				rs = getDefineSelectPeople(nodeBean.getFilterSetSQL(),id,tableName);
			}else{
				rs = getDefineSelectPeople(nodeBean.getFilterSetSQL(),id,tableName,conn);
			}
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS && rs.getRetVal()!=null){
				ArrayList<String>  list =(ArrayList<String>)rs.getRetVal();
				for(String key : personsMap.keySet()){
					String []checkPerson = personsMap.get(key);
					boolean found = false;
					for(String userId:list){
						if(userId.equals(checkPerson[0])){
							found = true;
						}
					}	
					if(!found){
						cps.add(checkPerson);
					}
				}
			}else{
				throw new BusinessException(String.valueOf(rs.retVal));
			}
		}else if(nodeBean.getFilterSet()==nodeBean.FILTER_DEFINESQLSELONLY){//只选择自定义审核人
			Result rs = null;
			if(conn==null){
				rs = getDefineSelectPeople(nodeBean.getFilterSetSQL(),id,tableName);
			}else{
				rs = getDefineSelectPeople(nodeBean.getFilterSetSQL(),id,tableName,conn);
			}
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS && rs.getRetVal()!=null){
				ArrayList<String>  list =(ArrayList<String>)rs.getRetVal();
				for(String key : personsMap.keySet()){
					String []checkPerson = personsMap.get(key);
					for(String userId:list){
						if(userId.equals(checkPerson[0])){
							cps.add(checkPerson);
						}
					}	
				}
			}else{
				throw new BusinessException(String.valueOf(rs.retVal));
			}
		}else{
			for(String key : personsMap.keySet()){
				cps.add(personsMap.get(key));
			}
		}
		
		return cps;
	}
	
	/**
	 * 获取用户分组职员
	 * @param oanewsBean
	 * @return
	 */
	public List<String[]> getEmpByGroup(final String groupId){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn)  {
						rst.setRetVal(getEmpByGroup(groupId,conn));
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		if(rst.retCode == ErrorCanst.DEFAULT_SUCCESS){
			return (List<String[]>) rst.getRetVal();
		}
		return null ;
	}
	/**
	 * 获取用户分组职员
	 * @param oanewsBean
	 * @return
	 */
	public List<String[]> getEmpByGroup(String groupId,Connection conn){
		ArrayList<String[]> userList = new ArrayList<String[]>();
		try{
			String sql = "select userId,emp.empFullName from tblEmpGroupUser groups left join tblEmployee " +
					"emp on groups.userId=emp.id where f_ref=? ";
			PreparedStatement pss = conn.prepareStatement(sql);
			pss.setString(1, groupId);
			ResultSet rss = pss.executeQuery();
			while(rss.next()){
				String[] users = new String[2];
				users[0] = rss.getString("userId");
				users[1] = rss.getString("empFullName");
				userList.add(users);
			}
		}catch (Exception e) {
			BaseEnv.log.error("OAMyWorkFlowMgt getEmpByGroup", e);
			e.printStackTrace();
		}
		return userList;
	}
	
	public Result getBillCreateBy(final String keyId,final String tableName){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn)  {
						try{		
							Result rs = getBillCreateBy(keyId, conn, tableName);
							rst.setRetVal(rs.getRetVal());
							rst.setRetCode(rs.getRetCode());
						} catch (Exception e) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							e.printStackTrace();
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}
	
	/**
	 * 单据的创建人
	 * @param id
	 * @param conn
	 * @param tableName
	 * @return
	 */
	public Result getBillCreateBy(final String id,final Connection conn,String tableName){
		Result rst = new Result() ;
		String sql="";
		try{
			sql="select createBy from OAMyWorkFlow where keyId=? and tableName=? ";
			PreparedStatement pss=conn.prepareStatement(sql);
			pss.setString(1, id);
			pss.setString(2, tableName);
			ResultSet rs=pss.executeQuery();
			if(rs.next()){
				rst.setRetVal(rs.getString(1));
			}	
		}catch(Exception ex){
			BaseEnv.log.debug(sql);
			ex.printStackTrace();
		}
		return rst ;
	}
	
	/**
	 * 获取某人的直属上司
	 * @param id
	 * @param conn
	 * @param tableName
	 * @return
	 */
	public Result getBoss(final String keyId,final String tableName){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn)  {
						rst.retVal = getBoss(keyId, tableName, conn).retVal;
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}
	
	/**
	 * 获取某人的直属上司
	 * @param keyId
	 * @param conn
	 * @param tableName
	 * @return
	 */
	public Result getBoss(final String keyId,final String tableName,final Connection conn){
		final Result rst = new Result() ;
		try{		
			String sql="select directBoss from tblEmployee where id in (select createBy from OAMyWorkFlow where keyId=? and tableName=?)";
			PreparedStatement pss=conn.prepareStatement(sql);
			pss.setString(1, keyId);
			pss.setString(2, tableName);
			ResultSet rs=pss.executeQuery();
			if(rs.next()){
				rst.setRetVal(rs.getString(1));
			}
		} catch (Exception e) {
			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			e.printStackTrace();
		}
		return rst ;
	}
	
	
	/**
	 * 自定义选人
	 * @param id
	 * @param conn
	 * @param tableName
	 * @return
	 */
	public Result getDefineSelectPeople(final String sql,final String id,final String tableName){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn)  {
						Result rr = getDefineSelectPeople(sql,id,tableName, conn);
						rst.retCode =rr.retCode;
						rst.retVal = rr.retVal;
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}
	
	/**
	 * 自定义选人
	 * @param keyId
	 * @param conn
	 * @param tableName
	 * @return
	 */
	public Result getDefineSelectPeople(String sql,final String id,final String tableName,final Connection conn){ 
		final Result rst = new Result() ;
		
		//取sql中的@ValueofDB:这是从表单取值的信息
		Pattern pattern = Pattern.compile("@ValueofDB:([\\w]*)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sql);
		String fields = "";
		ArrayList<String> plist = new ArrayList<String>();
		while (matcher.find()) {
			fields +=","+matcher.group(1);
			plist.add(matcher.group(1));
		}
		if(fields.length() > 0){
			//有需取值
			fields = fields.substring(1);
			String querysql  = "select "+fields +" from "+tableName +" where id=?";
			try{	
				PreparedStatement pss=conn.prepareStatement(querysql);
				pss.setString(1, id);
				ResultSet rs=pss.executeQuery();
				if(rs.next()){
					for(String fd :plist){
						String obj = rs.getString(fd);
						sql = sql.replaceAll("@ValueofDB:"+fd, obj==null?"''":"'"+obj+"'");
					}
				}
			} catch (Exception e) {
				rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
				BaseEnv.log.error("OAMyWorkFlowMgt.getDefineSelectPeople 取自定义规则自定义参数 有错"+querysql,e);
				rst.retVal = "取自定义规则自定义参数 有错"+e.getMessage();
				return rst;
			}
		}
		
		
		try{		
			PreparedStatement pss=conn.prepareStatement(sql);
			ResultSet rs=pss.executeQuery();
			ArrayList list = new ArrayList();
			while(rs.next()){
				list.add(rs.getString(1));
			}
			rst.setRetVal(list);
		} catch (Exception e) {
			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			BaseEnv.log.error("OAMyWorkFlowMgt.getDefineSelectPeople 取自定义规则有错"+sql,e);
			rst.retVal = "取自定义规则有错"+e.getMessage();
			return rst;
		}
		return rst ;
	}
	
	/**
	 * 转交执行的操作，改变当前节点的值为用户在界面选择的下一步节点值，审核人为用户选择的审核人
	 * @param id
	 * @param nextNode
	 * @param checkPersons
	 * @param currNode
	 * @param loginBean
	 * @param designId
	 * @param appDepartment
	 * @param locale
	 * @param deliverance
	 * @param affix
	 * @return
	 * @throws BSFException
	 */
	public Result deliverTo(final String keyId,final String nextNode,final String[] checkPersons,final String currNode,
			final LoginBean loginBean,final String designId,final Locale locale,
			final String strDeliverance,final String affix,final String oaTimeLimit,final String oaTimeLimitUnit,
			final String appendWake,final MessageResources mr,final String userIds, final String deptIds,final String type) throws BSFException{
		final Result rst = new Result() ;
		
		String strCheckPersons="";	/*当前审批人*/
		String currentNode = "";	/*当前结点*/
		String department=""; /*当前部门*/
		final String tableName = BaseEnv.workFlowInfo.get(designId).getTemplateFile();
		
		/*根据当前单据ID查询当前工作流结点相关信息*/
		HashMap flowMap = getOAMyWorkFlowInfo(keyId,tableName);
    	if(flowMap!=null){
    		strCheckPersons = String.valueOf(flowMap.get("checkPerson"));
    		currentNode = String.valueOf(flowMap.get("currentNode"));
    		department = String.valueOf(flowMap.get("departmentCode"));
    		
    		//批量审核，要校验单据审核流版本和所先审核流是否一致
        	if(!designId.equals(flowMap.get("applyType"))){
        		rst.setRetVal("审核版本不一致。");
    			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
    			return rst;
        	}
    		
    		if(currNode != null && currNode.length() > 0 && !currNode.equals(currentNode)){
    			if("deliverToAll".equals(type)){
    				rst.setRetVal("当前审核结点不一致");
    			}else{
    				rst.setRetVal("当前结点不一致，操作不能继续,可能原因工作流已撤回或重置");
    			}
    			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
    			return rst;
    		}
    	}
    	
    	
    	/*查看当前审核人里有没有委托给我的*/
    	HashMap<String, String> consignMap = OAMyWorkFlowMgt.queryConsignation(loginBean.getId(), designId);
    	for(String person : strCheckPersons.split(";")){
			 if(consignMap.get(person)!=null){
				 strCheckPersons += loginBean.getId()+";";
			 }
		}
    	/*判断执行此操作的人是否当前审批结点的审核人 主要为了两个人同时审批或者一个人同时在(详情界面和列表界面同时打开)*/
        if("-1".equals(currentNode) || (strCheckPersons.length()>0 && !strCheckPersons.contains(";"+loginBean.getId()+";")
        		&& !"0".equals(currNode) && !"cancel".equals(nextNode))){
        	rst.setRetVal(mr.getMessage("common.msg.hasAudit"));
			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			return rst;
        }
        WorkFlowDesignBean designBean=BaseEnv.workFlowDesignBeans.get(designId);
        if(!nextNode.equals("-1")){
	        ArrayList<String []> OldcheckPersons=null;
	        if(nextNode != null && nextNode.contains("back")){
	        	String bNode = nextNode.substring(5);
	        	Result bnp =getBackNodePersion(keyId,bNode);
	        	if(bnp.retVal!= null){
	        		OldcheckPersons = (ArrayList<String []>)bnp.retVal;
	        	}
	        }else{        
		        //找出下一个结点的所有审核人，和所选单据进行比较，如果不一致，退出审核
		        FlowNodeBean nextnodeBean=designBean.getFlowNodeMap().get(nextNode);
		        OldcheckPersons=getNodeCheckPerson(nextnodeBean, department,keyId,null,tableName,loginBean.getId());
	        }
	        for(String cp : checkPersons){
	        	boolean found = false;
	        	for(String[] cpp : OldcheckPersons){
	        		if(cp.equals(cpp[0])){
	        			found=true;
	        			break;
	        		}
	        	}
	        	if(!found ){
	        		if("deliverToAll".equals(type)){
	        			rst.setRetVal("审核人不包括所选审核人。");
	        		}else{
	        			rst.setRetVal("审核人信息不一致。");
	        		}
	    			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
	    			return rst;
	        	}
	        }
        }
        
		
		final DynDBManager dyn = new DynDBManager();
		
		
		/*获取当前单据相关的流程信息*/
		final HashMap map = this.getOAMyWorkFlowInfo(keyId,tableName);
		final String billId = map.get("keyId").toString();
		final String id = map.get("id").toString();
		final OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(designId);
		/*查询单据的详细信息，并封装在HashMap中*/
		final Result billDetail = dyn.detail(tableName, BaseEnv.tableInfos, billId, loginBean.getSunCmpClassCode(),BaseEnv.propMap ,loginBean.getId(),true,"");
		final HashMap values = (HashMap) billDetail.getRetVal() ;
		
		if("deliverToAll".equals(type)){//批量转交,如果是批量转交，要先执行一个保存动作，看是否报错，原因为在转交时要调define做一些数据的验证
			//执行一个update动作
			if(values.get("BillDate")!=null && !values.get("BillDate").equals("")){
				int periodYear=Integer.parseInt(values.get("BillDate").toString().substring(0,4));
				int period=Integer.parseInt(values.get("BillDate").toString().substring(5,7));
				
				Hashtable ht = (Hashtable) BaseEnv.sessionSet.get(loginBean.getId());
				int curPeriodYear=Integer.parseInt(ht.get("NowYear").toString());
				int curPeriod=Integer.parseInt(ht.get("NowPeriod").toString());
				
				if(periodYear<curPeriodYear||(periodYear==curPeriodYear&&period<curPeriod)){
					rst.setRetVal(mr.getMessage("com.currentaccbefbill"));
					rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
					return rst;
				} 
			}
			
			if(template==null || template.getTemplateStatus() == 0){
				values.put("workFlowNodeName", "finish") ;
				values.put("workFlowNode", "-1");
        		values.put("checkPersons", "") ;
        		values.put("finishTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			}else if("draft".equals(values.get("workFlowNodeName"))){
				values.put("workFlowNodeName", "notApprove") ;
				values.put("workFlowNode", "0") ;
            	values.put("checkPersons", ";"+loginBean.getId()+";") ;
            	values.put("finishTime","");
			}
			values.put("lastUpdateTime",BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
			
			//要执行的define的信息
	        String defineInfo="";
	        DBTableInfoBean tb = BaseEnv.tableInfos.get(tableName);
	        String parentTableName = tb==null?"":tb.getPerantTableName();
	        String moduleType = values.get("moduleType")==null?"":values.get("moduleType")+"";
	        MOperation mop = GlobalsTool.getMOperationMap(parentTableName, tableName, moduleType, loginBean);
			Hashtable props = BaseEnv.propMap;
	        
	        Result updateRs = new DynDBManager().update(tableName, BaseEnv.tableInfos, values,loginBean.getId(), 
					defineInfo,mr,locale,"",loginBean,template,props);
	        if(updateRs.getRetCode()==ErrorCanst.RET_DEFINE_SQL_CONFIRM){
            	// 自定义配置需要用户确认
	        	rst.setRetVal("有弹出可选提示框，不支持批量审核");
    			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
    			return rst;
            }else  if (updateRs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
				return updateRs;
            }	 
		}
		
		DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn)  {
						String deliverance = strDeliverance ;
						String dispenseUser = "" ;
						try {
							String nodeType = "";			/*审批类型*/
							String nextNode2 = nextNode;	/*下个审批结点*/
							String strCheckPerson = ";";	/*当前结点审批人*/
							String insertTableName = new DynDBManager().getInsertTableName(tableName);//处理CRM多模板表名
							if(checkPersons!=null){
								for(String person : checkPersons){strCheckPerson += person+";";}
							}
							if(("-1").equals(nextNode)){
								nodeType = "stop";
							}else if(nextNode != null && nextNode.contains("back")){
								nodeType = "back";
								nextNode2 = nextNode.substring(5);
							}
							
							//如果当前结点的审核人(可能多个)不包含当前用户，说明当前用户是受托人，查询出当前结点的审核人
							String currentPerson = loginBean.getId();
							if(!String.valueOf(map.get("checkPerson")).contains(";"+loginBean.getId()+";")){
								currentPerson = queryUserIdByConsignUserId(loginBean.getId(), designId,String.valueOf(map.get("checkPerson")), conn);
								if(currentPerson!=null && currentPerson.length()>0){
									deliverance = loginBean.getEmpFullName()+"代"+OnlineUserInfo.getUser(currentPerson).getName()+"办理。\r\n " + deliverance;
								}
							}
							
							String sql = "update OAMyWorkFlowDet set endTime=?,workFlowNode=?,checkPersons=?,checkPerson=?,nodeType=case ? when 'back' then 'back' else nodeType end," 
								+ "oaTimeLimitUnit=?,benchMarkTime=?,approvalOpinions=?,affix=? where f_ref=? and nodeId=? and checkPerson=? and statusId=0" ;
							//修改工作流明细表中当前节点的转交时间，并记录当前登录用户所选择的下一个节点，和下一个节点的审核人
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)) ;
							pss.setString(2, nextNode2);
							pss.setString(3, strCheckPerson);
							pss.setString(4, currentPerson);
							pss.setString(5, nodeType);//假如当前操作是回退，就修改节点类型，否则还是原来的节点类型
							pss.setString(6, oaTimeLimitUnit) ;
							if(oaTimeLimit != null && oaTimeLimit.equals("")){
								pss.setString(7, null);
							}else{
								pss.setString(7, oaTimeLimit);
							}
							pss.setString(8, deliverance);
							pss.setString(9, affix) ;
							pss.setString(10, id) ;
							pss.setString(11, currNode) ;
							pss.setString(12, loginBean.getId());
							pss.executeUpdate() ;
							
							FlowNodeBean currBean = BaseEnv.workFlowDesignBeans.get(designId).getFlowNodeMap().get(currNode);
							updateMsgContent(billId, loginBean, conn,currBean.isUseAllApprove());
							//当启用全审且不是最后一人审核时
							if(currBean.isUseAllApprove() && !"back".equals(nodeType) && 
									!String.valueOf(map.get("checkPerson")).equals(";"+currentPerson+";")){
								//如果当前要转交的节点必须全审那么查询是否每个用户都已经审核
								String checkPerson = String.valueOf(map.get("checkPerson"));
								
								//去掉当前工作流审核人中的当前登录用户
								checkPerson = checkPerson.replaceAll(currentPerson+";", "");
								sql="update OAMyWorkFlow set checkPerson=? where id=?";
								pss = conn.prepareStatement(sql);
								pss.setString(1,checkPerson) ;
								pss.setString(2,id) ;
								pss.executeUpdate() ;
								
								//修改allApprovePerson全审时已审核过的人,修改allCheckPersons，记录所有审核过的人
								sql = "update OAMyWorkFlow set allApprovePerson=case isNull(allApprovePerson,'') when '' then ';'+?+';' else allApprovePerson+?+';'  end, " +
										" allCheckPersons=case isNull(allCheckPersons,'') when '' then ';'+?+';' else allCheckPersons+?+';'  end where id=? " ;
								if(tableName!=null && tableName.length()>0){
									sql += " and tableName='"+tableName+"'" ;
								}
								pss = conn.prepareStatement(sql);
								pss.setString(1,currentPerson) ;
								pss.setString(2,currentPerson) ;
								pss.setString(3,currentPerson) ;
								pss.setString(4,currentPerson) ;
								pss.setString(5,id) ;
								pss.executeUpdate() ;	
								
							 
								//把自己由当前审核人设为已审核人
								sql = "update OAMyWorkFlowPerson set hadApprover=1,curApprover=0 where keyId=? and tableName=? and checkperson=?";
								pss = conn.prepareStatement(sql);
								pss.setString(1, keyId);
								pss.setString(2, tableName);
								pss.setString(3, currentPerson);
								pss.execute();
								
										
								if(nodeType.length()==0 && currBean.getPassExec().length()>0){
									String defineName=currBean.getPassExec();
									DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(defineName);
									if (defineSqlBean != null) {
										HashMap values = new HashMap();
										values.put("id", keyId);
										values.put("tableName",tableName);
										Result rs1 = defineSqlBean.execute(conn, values,loginBean.getId(), null, null, "");
										if (rs1.retCode != ErrorCanst.DEFAULT_SUCCESS) {
											rst.setRetVal(rs1.retVal);
											rst.setRetCode(rs1.retCode);
											return;
										}
									}
								}

							}else{
								Result rstcd = checkDeliverTo(strCheckPerson, nextNode2, designId, conn, id,locale,nodeType,currNode,currentPerson,loginBean,appendWake,mr,tableName,
										deptIds,userIds,keyId,currBean,map,template,insertTableName,values);
								if(rstcd.retCode != ErrorCanst.DEFAULT_SUCCESS){
									rst.setRetVal(rstcd.retVal);
									rst.setRetCode(rstcd.retCode);
									return;
								}else if(rstcd!=null && rstcd.retVal!=null){
									if(rstcd.retVal.toString().length()>0){
										dispenseUser += "\r\n抄送人：" + rstcd.getRetVal() ;
									}
									String deptNames = new UserMgt().queryDeptNameById(deptIds, conn);
									if(deptNames!=null && deptNames.trim().length()>0){
										dispenseUser += "\r\n抄送部门：" + deptNames;
									}
								}
							}
							
							if(template!=null && template.getLines()==null){
								/*插入会签意见*/
								insertDeliverance(conn, id, currNode, deliverance+dispenseUser, currentPerson, affix);
							}


						} catch (Exception e) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("OAMyWorkFLowMgt.deliverTo Error:",e);
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		
		return rst ;
	}
	
	/**
	 * 所有的流程结束时执行审核define
	 * @param tableName
	 * @param keyId
	 * @param defineName
	 * @param lg
	 * @param conn
	 * @param resources
	 * @param locale
	 * @return
	 */
	public Result execCheckDefine(final String tableName,final String billId,final String designId,final LoginBean lg,final MessageResources resources,
		final Locale locale,final Connection conn,final Object retVal)throws Exception{
		Result rs=new Result();
		DynDBManager dyn=new DynDBManager();
		
		final HashMap values=(HashMap)retVal;
		//查询反审核人
		String sql;
		Statement st=conn.createStatement();
		String retCheck="";
		//保存时不记录反审核人信息
//		String sql="select departmentCode from tblEmployee where id='"+values.get("createBy").toString()+"'";
//		ResultSet rsst=st.executeQuery(sql);
//		if(rsst.next()){
//			retCheck=dyn.getRetCheckPer(BaseEnv.workFlowInfo.get(designId), rsst.getString(1));
//		}
		sql="update "+tableName+" set workFlowNodeName='finish',workFlowNode='-1',checkPersons='"+retCheck+"',finishTime='"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"' where id='"+billId+"'" ;
		st.execute(sql);
		
		if("CRMClientInfo".equals(tableName)){
			sql = "delete CRMClientInfoEmp where f_ref='"+billId+"' and empType=2 ";
			st = conn.createStatement();
			st.execute(sql);
			String checkp = retCheck;
			String[] checks =(checkp==null?"":checkp).split(";");
			for(String check :checks){
				if(check.length() > 0){
					sql = "insert into CRMClientInfoEmp(id,f_ref,employeeID,DepartmentCode,empType) values('"+IDGenerater.getId()+"','"+billId+"','"+ check+"','"+ ""+"',2)";
					st = conn.createStatement();
					st.execute(sql);
				}
			}
		}
		
		String defineName=BaseEnv.workFlowInfo.get(designId).getDefineName();
		if(defineName!=null&&defineName.length()>0){	
			DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(defineName);
			if(defineSqlBean!=null){
				//后台define代码需要用到最后审核时间
				values.put("tableName", tableName);
				values.put("finishTime", BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
				Result ret = defineSqlBean.execute(conn, values, lg.getId(),resources, locale, "");	
				rs.setRetCode(ret.getRetCode());
				rs.setRetVal(ret.getRetVal());
			}
		}					
		  
		return rs; 
	}
	
	/**
	 * 流程反审核时执行审核define
	 * @param tableName
	 * @param keyId
	 * @param defineName
	 * @param lg
	 * @param conn
	 * @param resources
	 * @param locale
	 * @return
	 * @throws BSFException 
	 */
	public Result execRetCheckDefine(final String tableName,final String keyId,final LoginBean lg,final MessageResources resources,final Locale locale){
		
		//得到当前要转交的单据的所有信息，以执行define
		DynDBManager dyn=new DynDBManager();
		Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(lg.getId()) ;
		boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
		final Result rs=dyn.detail(tableName, BaseEnv.tableInfos, keyId, lg.getSunCmpClassCode(),BaseEnv.propMap ,lg.getId(),isLastSunCompany,"");
		final String designId=BaseEnv.workFlowInfo.get(tableName).getId();
		final HashMap workValMap=this.getOAMyWorkFlowInfo(keyId,tableName);
		
		//准备好数据将当前单据标识成完成，并执行define
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			final HashMap values=(HashMap)rs.retVal;
			String nextIds="";
			if(workValMap==null){//如果没有审核记录，那么拿下节点ID，以便插入审核记录
				try{
					nextIds=this.getNextNodeIds(designId, "0", values,tableName,null);
					Result rs2=this.autoPass(designId, lg.getId(),keyId, "0", nextIds, lg.getDepartCode(),values,tableName);
					if(rs2.retCode==ErrorCanst.DEFAULT_SUCCESS&&rs2.retVal!=null){
						nextIds=rs2.retVal.toString();
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			final String nextNodeIds=nextIds;
			int retCode = DBUtil.execute(new IfDB(){
				public int exec(Session session){
					session.doWork(new Work(){
						public void execute(Connection conn)  {
							try{
								String tempTableName = new DynDBManager().getInsertTableName(tableName);
								//修改单据的审核状态
								String sql="update "+tempTableName+" set workFlowNodeName='notApprove',workFlowNode='0',checkPersons=';"+lg.getId()+";"+values.get("createBy")+";' where id='"+keyId+"'" ;
								Statement st=conn.createStatement();
								st.execute(sql);
								
								String id=IDGenerater.getId();
								PreparedStatement pss;
								if(workValMap==null){
									String lastDesignId = designId;
									sql = "select id as lastDesignId from OAWorkFlowTemplate where templateFile=? order by convert(int,isnull(version,0)) desc";
									pss = conn.prepareStatement(sql);
									pss.setString(1, tableName);
									ResultSet rss = pss.executeQuery();
									if(rss.next()){
										lastDesignId = rss.getString("lastDesignId");
									}
									sql = "insert into OAMyWorkFlow(id,billNo,applyDate,applyBy,department,applyType,checkPerson," +
									"tableName,keyId,applyContent,createBy,currentNode,nextNodeIds,departmentCode,statusId,createTime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" ;
									pss = conn.prepareStatement(sql) ;
									pss.setString(1,id) ;
									pss.setString(2,keyId) ;
									pss.setString(3, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd)) ;
									pss.setString(4, lg.getEmpFullName()) ;
									pss.setString(5, lg.getDepartmentName()) ;
									pss.setString(6, lastDesignId) ;
									pss.setString(7, ";"+lg.getId()+";");
									pss.setString(8, tableName) ;
									pss.setString(9, values.get("id").toString()) ;
									pss.setString(10, "");
									pss.setString(11, lg.getId()) ;
									pss.setString(12, "0") ;
									pss.setString(13, nextNodeIds);
									pss.setString(14, lg.getDepartCode());						
									pss.setInt(15, 0);
									pss.setString(16, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
									pss.executeUpdate() ;		
								}else{
									String lastDesignId = "";
									sql = "select id as lastDesignId from OAWorkFlowTemplate where templateFile=? order by convert(int,isnull(version,0)) desc";
									pss = conn.prepareStatement(sql);
									pss.setString(1, tableName);
									ResultSet rss = pss.executeQuery();
									if(rss.next()){
										lastDesignId = rss.getString("lastDesignId");
									}
									
									//修改工作流的信息
									sql="update OAMyWorkFlow set applyType='"+lastDesignId+"', currentNode='0',checkPerson=';"+lg.getId()+";"+values.get("createBy")+";' where tableName='"+tableName+"' and keyId='"+keyId+"'" ;
									st=conn.createStatement();
									st.execute(sql);
									
									//修改工作流明细信息
									sql="update OAMyWorkFlowDet set statusId=1 where f_ref=(select id from OAMyWorkFlow where tableName='"+tableName+"' and keyId='"+keyId+"')" ;
									st=conn.createStatement();
									st.execute(sql);
									
									id=workValMap.get("id").toString();
								}
								
								//新增反审核记录
								sql="insert into OAMyWorkFlowDet (id,f_ref,nodeId,checkPerson,startTime,createBy,nodeType,endTime,statusId) values (?,?,?,?,?,?,?,?,1)";
								pss = conn.prepareStatement(sql) ;
								pss.setString(1, IDGenerater.getId()) ;
								pss.setString(2, id);
								pss.setString(3, "-1") ;
								pss.setString(4, lg.getId());
								pss.setString(5,BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)) ;
								pss.setString(6, lg.getId()) ;
								pss.setString(7, "retAudit"); 
								pss.setString(8, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)); 
								pss.executeUpdate();
								
								//反审核后，重新增加开始节点
								sql="insert into OAMyWorkFlowDet (id,f_ref,nodeId,checkPerson,startTime,createBy,nodeType,endTime,workFlowNode,checkPersons) values (?,?,?,?,?,?,?,?,?,?)";
								pss = conn.prepareStatement(sql) ;
								pss.setString(1, IDGenerater.getId()) ;
								pss.setString(2, id);
								pss.setString(3, "0") ;
								pss.setString(4, lg.getId());
								pss.setString(5,BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)) ;
								pss.setString(6, lg.getId()) ;
								pss.setString(7, "transact"); 
								pss.setString(8,""); 
								pss.setString(9,""); 
								pss.setString(10,""); 
								pss.executeUpdate();
								
								addHasCheckPerson(tableName, keyId, null,String.valueOf(values.get("createBy"))+";"+lg.getId(), conn);
								
								String defineName=BaseEnv.workFlowInfo.get(designId).getRetdefineName();
								if(defineName!=null&&defineName.length()>0){
									values.put("tableName", tableName);
									DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(defineName);
									if(defineSqlBean != null){
										Result ret = defineSqlBean.execute(conn, values, lg.getId(),resources, locale, "");
										rs.setRetCode(ret.getRetCode());
										rs.setRetVal(ret.getRetVal());
										String reviewWake = BaseEnv.workFlowInfo.get(designId).getReviewWake() ;
										if(reviewWake!=null && reviewWake.trim().length()>0){
											String title = "";
											if(workValMap!=null && workValMap.get("applyContent")!=null){
												title = String.valueOf(workValMap.get("applyContent"));
											}
											hurryTrans("", designId, values.get("createBy").toString(), values.get("id").toString(), "", "0", "0","0",locale,"",title,reviewWake,"retAuditing","0",lg,"",resources,conn);
										}
									}
								}
							} catch (Exception e) {
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								e.printStackTrace();
							}
						}
					}) ;
					return rs.getRetCode() ;
				}
			}) ;
			rs.setRetCode(retCode) ;
		}
		  
		return rs; 
	}
	
	
	/**
	 * 得到当前节点之前所有审核过的节点
	 * @param id
	 * @param nextNode
	 * @param checkPersons
	 * @param currNode
	 * @param loginBean
	 * @param designId
	 * @param appDepartment
	 * @param locale
	 * @param deliverance
	 * @param affix
	 * @return
	 * @throws BSFException
	 */
	
	public Result getBackNodeBean(final String id,final FlowNodeBean currNB,final ArrayList<FlowNodeBean> nodeBeans,final WorkFlowDesignBean bean) throws BSFException{
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn)  {
						try{
							PreparedStatement pss;
							String lastNodeId="";
							
							String sql="select nodeID,checkPerson,MAX( sortOrder) as sortOrder from OAMyWorkFlowDet a where f_ref=? "+
									"and a.nodeID<>? and len(isNull(endTime,''))>0 and statusId=0 and nodeType ='transact' group by nodeID,checkPerson  order by sortOrder  ";
							pss=conn.prepareStatement(sql);
							pss.setString(1, id);
							pss.setString(2, currNB.getKeyId());
							ResultSet rs=pss.executeQuery();
							HashMap nodeChecks=new HashMap();
							ArrayList nodeIdSort=new ArrayList();
							String person="";
							while(rs.next()){
								lastNodeId = rs.getString(1);
								person = rs.getString(2);
								OnlineUser users=OnlineUserInfo.getUser(person);
								ArrayList<String []> checkPersons;
								if(nodeChecks.get(lastNodeId)==null){
									checkPersons=new ArrayList();	
									nodeChecks.put(lastNodeId, checkPersons);
									nodeIdSort.add(lastNodeId);
								}else{
									checkPersons=(ArrayList<String []>)nodeChecks.get(lastNodeId);
								}
								if(users!=null){
									checkPersons.add(new String[]{person,users.getName()});
								}
							}
							
							for(int i=0;i<nodeIdSort.size();i++){
								String nodeId=nodeIdSort.get(i).toString();
								ArrayList<String []> checkPersons=(ArrayList<String []>)nodeChecks.get(nodeId);
								FlowNodeBean nodeBean=bean.getFlowNodeMap().get(nodeId);
								FlowNodeBean nodeBeanNew=new FlowNodeBean();
								nodeBeanNew.setKeyId("back_"+nodeId);
								nodeBeanNew.setDisplay(nodeBean.getDisplay());
								nodeBeanNew.setApprovePeople(nodeBean.getApprovePeople());
								nodeBeanNew.setZAction(nodeBean.getZAction());
								nodeBeanNew.setCheckPeople(checkPersons);
								nodeBeanNew.setForwardTime(nodeBeanNew.isForwardTime());
								nodeBeans.add(nodeBeanNew);
							}
							
						} catch (Exception e) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}
	
	public Result getBackNodePersion(final String id,final String nodeId) throws BSFException{
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn)  {
						try{
							PreparedStatement pss;
							String lastNodeId="";
							
							String sql="select checkPerson from OAMyWorkFlowDet a where f_ref=? "+
									"and a.nodeID=? and len(isNull(endTime,''))>0 and statusId=0 and nodeType ='transact'  order by sortOrder ";
							pss=conn.prepareStatement(sql);
							pss.setString(1, id);
							pss.setString(2, nodeId);
							ResultSet rs=pss.executeQuery();
							ArrayList<String []> list = new ArrayList<String []>();
							while(rs.next()){
								list.add(new String[]{rs.getString(1)});
								lastNodeId = rs.getString(1);
							}
							rst.setRetVal(list);
						} catch (Exception e) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}
	
	

	/**
	 * 转交时根据当前节点的设置给相关人员发消息
	 * @param designId
	 * @param currNodeId
	 * @param billId
	 * @param conn
	 * @param locale
	 * @param operType
	 */
	public Result deliverWake(String designId,String currNodeId,String nextNodeId,String id,Connection conn,
			Locale locale,String operType,LoginBean login,String appendWake,MessageResources mr,
			String deptIds,String userIds){
		
		Result result = new Result();
		FlowNodeBean currBean=BaseEnv.workFlowDesignBeans.get(designId).getFlowNodeMap().get(currNodeId);
		String sql="select checkPerson,createBy,isnull(applyContent,''),applyBy,keyId,tableName from OAMyWorkFlow where id=?";
		try{
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs=ps.executeQuery();
			String checkPerson="";
			String createBy="";
			String applyContent="";
			String applyBy="";
			String billId="";
			String tableName="";
			if(rs.next()){
				checkPerson = rs.getString(1);
				createBy = rs.getString(2);
				applyContent = rs.getString(3);
				applyBy = rs.getString(4);
				if(applyBy==null||"".equals(applyBy)){
					applyBy=OnlineUserInfo.getUser(createBy).getName();
				}
				billId = rs.getString(5);
				tableName = rs.getString(6);
			}
			String nextWake = "";
			String startWake = "";
			String allWake = "";
			String setWake = "";
			String setWakeDept = "";
			String setWakePer = "";
			String setWakeGroup = "";
			String dispenseWake = "";
			
			if(currBean.isStandaloneNoteSet()){//如果当前节点设置了独立提醒方式
				if(currBean.isNextMobile()){//下一结点审核人提醒方式
					nextWake+="1,";			/*手机短信提醒*/
				}
				if(currBean.isNextMail()){
					nextWake+="2,";			/*邮件提醒*/
				}
				if(currBean.isNextSMS()){
					nextWake+="4,";			/*系统通知*/
				}
				
				if(currBean.isStartMobile()){ //发起人提醒方式
					startWake+="1,";
				}
				if(currBean.isStartMail()){	
					startWake+="2,";
				}
				if(currBean.isStartSMS()){
					startWake+="4,";
				}
				
				if(currBean.isAllMobile()){	//所有审核人提醒方式
					allWake+="1,";
				}
				if(currBean.isAllMail()){
					allWake+="2,";
				}
				if(currBean.isAllSMS()){
					allWake+="4,";
				}
				
				if(currBean.isSetMobile()){//指定人员提醒方式
					setWake+="1,";
				}
				if(currBean.isSetMail()){
					setWake+="2,";
				}
				if(currBean.isSetSMS()){
					setWake+="4,";
				}
				
				ArrayList<ApproveBean> notes = currBean.getNotePeople() ;
				for(int i=0;notes!=null && i<notes.size();i++){
					ApproveBean people=notes.get(i);
					if("employee".equals(people.getType())){
						setWakePer += people.getUser()+",";
					}else if("dept".equals(people.getType())){
						setWakeDept += people.getUser()+",";
					}
				}
			}else{//否则根据工作流设置中的进行提醒
				if(nextNodeId.equals("-1")){
					sql="select '',isnull(stopStartWake,''),isnull(stopSAllWake,''),isnull(stopSetWake,''),isnull(stopSetWakeDept,''),isnull(stopSetWakePer,''),isnull(stopSetWakeGroup,''),isnull(dispenseWake,'') from OAWorkFlowTemplate where id=?";					
				}else{
					sql="select isnull(nextWake,''),isnull(startWake,''),isnull(allWake,''),isnull(setWake,''),isnull(setWakeDept,''),isnull(setWakePer,''),isnull(setWakeGroup,''),isnull(dispenseWake,'') from OAWorkFlowTemplate where id=?";					
				}
				ps=conn.prepareStatement(sql);
				ps.setString(1, designId);
				rs=ps.executeQuery();
				
				if(rs.next()){
					nextWake = rs.getString(1);
					startWake = rs.getString(2);
					allWake = rs.getString(3);
					setWake = rs.getString(4);
					setWakeDept = rs.getString(5);
					setWakePer = rs.getString(6);
					setWakeGroup = rs.getString(7);
					dispenseWake = rs.getString(8);
				}
			}
			
			String hasSend=";";
			if(nextWake.length()>0 && !"-1".equals(nextNodeId)){
				hasSend=(checkPerson.indexOf(";"+login.getId()+";")>=0?";":"")+checkPerson/*.replaceAll(";"+login.getId()+";", "")*/;
				this.hurryTrans(applyBy, designId, checkPerson/*.replace(";"+login.getId()+";", "")*/ , billId, "", nextNodeId, "0","0",locale,operType,applyContent,nextWake,"next",currNodeId,login,appendWake,mr,conn);
			}
			
			if(startWake.length()>0&&!login.getId().equals(createBy) ){
				//假如创建人也在本次审核人中要将提醒消息改为下次审核的链接
				String next=checkPerson.indexOf(";"+createBy+";")>=0?"next":"";
				
				if(!hasSend.contains(";"+createBy+";")){
					if(nextNodeId.equals("-1")){
						this.tranEnd(applyBy, designId, createBy,nextNodeId,billId,locale,applyContent,startWake,login,conn);
					}else{
						this.hurryTrans(applyBy, designId, createBy, billId, "", nextNodeId, "0","0",locale,operType,applyContent,startWake,next,currNodeId,login,appendWake,mr,conn);
					}
					hasSend+=createBy+";";
				}
			}
			String checkPerson2="";
			if(allWake.length()>0){
				sql="select checkPerson from OAMyWorkFlowDet where f_ref=? group by checkPerson";
				ps=conn.prepareStatement(sql);
				ps.setString(1, billId);
				rs=ps.executeQuery();
				String allCheck="";
				while(rs.next()){
					String userId = rs.getString(1);
					if(!hasSend.contains(";"+userId+";")/*&&!login.getId().equals(userId)*/){
						hasSend+=userId+";";
						if(checkPerson.indexOf(";"+userId+";")>=0){
							checkPerson2+=userId+";";
						}else{
							allCheck+=userId+";";
						}
					}
				}
				allCheck=allCheck.length()>0?";"+allCheck:allCheck;
				checkPerson2=checkPerson2.length()>0?";"+checkPerson2:checkPerson2;
				
				if(nextNodeId.equals("-1")){
					this.tranEnd(applyBy, designId, allCheck,nextNodeId,billId,locale,applyContent,allWake,login,conn);
				}else{		
					if(allCheck.length()>0){
						this.hurryTrans(applyBy, designId, allCheck, billId, "", nextNodeId, "0","0",locale,operType,applyContent,allWake,"",currNodeId,login,appendWake,mr,conn);
					}
					if(checkPerson2.length()>0){
						this.hurryTrans(applyBy, designId, checkPerson2, billId, "", nextNodeId, "0","0",locale,operType,applyContent,allWake,"next",currNodeId,login,appendWake,mr,conn);
					}
				}
				
			}

			if(setWake.length()>0){ 
				checkPerson2="";
				
				String[] sets=setWakePer.split(",");
				for(int i=0;i<sets.length&&sets[i].length()>0;i++){
					if(hasSend.contains(";"+sets[i]+";")||checkPerson.indexOf(";"+sets[i]+";")>=0){
						setWakePer=setWakePer.replaceAll(sets[i]+",", "");
					}
					if(!hasSend.contains(";"+sets[i]+";")/*&&!login.getId().equals(sets[i])*/&&checkPerson.indexOf(";"+sets[i]+";")>=0){
						checkPerson2+=sets[i]+";"; //还未发送过，但包括在checkPerson中的数据要单据过滤出来，因为这部分数据提醒的内容不同是要求审核的，但setWakePer只是通知不包含审核
					}				
				}
				checkPerson2=";"+checkPerson2;
				if(setWakeDept.length()>0){
					String deptCodes="";
					String[]depts=setWakeDept.split(",");
					for(int i=0;i<depts.length&&depts[i].length()>0;i++){
						deptCodes+=" departmentCode like '"+depts[i]+"%' or";
					}
					if(deptCodes.length()>0){
						deptCodes=deptCodes.substring(0,deptCodes.length()-2);
						sql="select id from tblEmployee where openFlag=1 and statusId=0 and ("+deptCodes+")";
						ps=conn.prepareStatement(sql);
						rs=ps.executeQuery();
						while(rs.next()){
							String userId = rs.getString(1);
							if(!setWakePer.contains(","+userId+",")&&!hasSend.contains(";"+userId+";")&&checkPerson.indexOf(";"+userId+";")<0){
								setWakePer += rs.getString(1)+",";
								hasSend+=userId+";";
							}
							
							if(!hasSend.contains(";"+userId+";")/*&&!login.getId().equals(userId)*/&&checkPerson.indexOf(";"+userId+";")>=0&&checkPerson2.indexOf(";"+userId+";")<0){
								checkPerson2+=userId+";";
							}
						}
					}
				}
				
				if(setWakePer.length()>0){
					setWakePer=","+setWakePer;
					setWakePer = setWakePer.replace(',', ';'); 
					//这里检查是否有自定义过滤规则
					OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(designId) ; 
					if(workFlow.getWakeLimitSQL() != null && workFlow.getWakeLimitSQL().trim().length() > 0){
						//执行自定义sql过滤
						String newSetWakePer = ";";
						String wakeLimitSql = workFlow.getWakeLimitSQL();
						try{
							//取sql中的@ValueofDB:这是从表单取值的信息
							Pattern pattern = Pattern.compile("@ValueofDB:([\\w]*)", Pattern.CASE_INSENSITIVE);
							Matcher matcher = pattern.matcher(wakeLimitSql);
							String fields = "";
							ArrayList<String> plist = new ArrayList<String>();
							while (matcher.find()) {
								fields +=","+matcher.group(1);
								plist.add(matcher.group(1));
							}
							if(fields.length() > 0){
								//有需取值
								fields = fields.substring(1);
								String querysql  = "select "+fields +" from "+tableName +" where id=?";
								try{	
									PreparedStatement pss=conn.prepareStatement(querysql);
									pss.setString(1, id);
									rs=pss.executeQuery();
									if(rs.next()){
										for(String fd :plist){
											String obj = rs.getString(fd);
											wakeLimitSql = wakeLimitSql.replaceAll("@ValueofDB:"+fd, obj==null?"''":"'"+obj+"'");
										}
									}
								} catch (Exception e) {
									BaseEnv.log.error("OAMyWorkFlowMgt.deliverWake 执行指定人范围限定自定义sql自定义参数 有错"+querysql,e);
								}
							}
							
							ps=conn.prepareStatement(wakeLimitSql);
							rs=ps.executeQuery();
							while(rs.next()){
								String userId = rs.getString(1);														
								if(setWakePer.contains(";"+userId+";")){
									newSetWakePer+=userId+";";
								}
							}							
						}catch(Exception e){
							BaseEnv.log.error("OAMyWorkFlowMgt.deliverWake 执行指定人范围限定自定义sql出错 sql="+wakeLimitSql,e);
						}
						setWakePer = newSetWakePer;
					}
					if(setWakePer.length()>0&&!setWakePer.equals(";")){
						if(currNodeId.equals("-1")){
							this.tranEnd(applyBy, designId, setWakePer/*.replace(","+login.getId()+",", "")*/,nextNodeId,billId,locale,applyContent,setWake,login,conn);
						}else{
							this.hurryTrans(applyBy, designId, setWakePer/*.replace(","+login.getId()+",", "")*/, billId, "", nextNodeId, "0","0",locale,operType,applyContent,setWake,"",currNodeId,login,appendWake,mr,conn);
						}
					}
				}
				if(checkPerson2.length()>1){//在checkPerson中的人员，提醒内容是下一步经办人
					this.hurryTrans(applyBy, designId, checkPerson2, billId, "", nextNodeId, "0","0",locale,operType,applyContent ,setWake,"next",currNodeId,login,appendWake,mr,conn);
				}
			}
			
			String dispenseUser = "";
			String dispenseUserName = "";
			if(userIds!=null && userIds.trim().length()>0){ //用户指定人员
				for(String str : userIds.split(",")){
					if(!checkPerson2.contains(str+";") && !setWakePer.contains(str+",") && !dispenseUser.contains(str+",")){
						dispenseUser += str + ",";
					}
					dispenseUserName += OnlineUserInfo.getUser(str).getName()+",";
				}
			}
			if(deptIds!=null && deptIds.trim().length()>0){ //用户指定部门
				for(String str : deptIds.split(",")){
					ArrayList<OnlineUser> userList = OnlineUserInfo.getDeptUser(str) ;
					if(userList==null || userList.size()==0) continue;
					for(OnlineUser user : userList){
						if(!checkPerson2.contains(user.getId()+";") && !setWakePer.contains(user.getId()+",") && !dispenseUser.contains(user.getId()+",")){
							dispenseUser += user.getId() + ",";
						}
					}
					//dispenseUserName += OnlineUserInfo.getDept(str).getDepartmentName()+",";
				}
			}
			
			if(dispenseWake.length()>0&&dispenseUser.length()> 0){
				dispenseUser ="," + dispenseUser;
				if(currNodeId.equals("-1")){
					this.tranEnd(applyBy, designId, dispenseUser/*.replace(","+login.getId()+",", "")*/,nextNodeId,billId,locale,applyContent,setWake,login,conn);
				}else{
					this.hurryTrans(applyBy, designId, dispenseUser/*.replace(","+login.getId()+",", "")*/, billId, "", nextNodeId, "0","0",locale,operType,applyContent,dispenseWake,"",currNodeId,login,appendWake,mr,conn);
				}
				result.retVal = dispenseUserName;
			}
			
		}catch(Exception ex){
			BaseEnv.log.error("OAMyWorkFlowMgt.deliverWake ",ex);
		}
		return result;
	}
	
	public Result autoPass(final String designId,final String createBy,final String id,final String currNode,final String nextNodeIds,final String deptCode,final HashMap values,final String tableName){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn)  {
						try {
							OAMyWorkFlowMgt mgt=new OAMyWorkFlowMgt();
							Result rs=mgt.autoPass(designId, createBy, id, currNode, nextNodeIds, deptCode, values, tableName,conn);
							rst.setRetCode(rs.retCode);
							rst.setRetVal(rs.retVal);
						} catch (Exception e) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							e.printStackTrace();	
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}
	public Result autoPass(final String designId,final String createBy,final String id,final String currNode,
			final String nextNodeIds,final String deptCode,final HashMap values,final String tableName,Connection conn){
		final Result rst = new Result() ;

		try {
			String nextFirstNode=nextNodeIds.split(";")[0];						
			OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(designId);
			WorkFlowDesignBean bean=BaseEnv.workFlowDesignBeans.get(designId);
			FlowNodeBean nextNodeBean=bean.getFlowNodeMap().get(nextFirstNode);
			String nextCheckPerson="";
			OAMyWorkFlowMgt mgt=new OAMyWorkFlowMgt();
			Result rst2=mgt.getNodeCheckPByCongign(designId, nextFirstNode, id, deptCode, conn,tableName,createBy);
			if(rst2.retCode==ErrorCanst.DEFAULT_SUCCESS){
				nextCheckPerson=rst2.retVal.toString();
			}
			if("true".equals(workFlow.getAutoPass())&&currNode.equals("0") && nextCheckPerson.indexOf(";"+createBy+";") > -1){
				if(nextNodeBean.isUseAllApprove()&&nextNodeBean.getApprovePeople().equals("fix")
						&&!nextCheckPerson.equals(";"+createBy+";")){//下一结点必须要另一个人审核
					rst.setRetVal(nextNodeIds);
				}else{//否则列出下下节点
					String nextIds=mgt.getNextNodeIds(designId, nextFirstNode, values,tableName,conn);
					rst.setRetVal(nextIds);
				}
			}else{
				rst.setRetVal(nextNodeIds);
			}
		} catch (Exception e) {
			rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			e.printStackTrace();	
		}		
		return rst ;
	}
	
	/**
	 * 所有用户都已经审核，查询这些审核中靠前的审核节点作为当前节点，及用户选择的审核人作为当前审核人
	 * @param id
	 * @param currNode
	 * @param conn
	 * @throws SQLException 
	 */
	public Result checkDeliverTo(String nextCheckPerson,String nextNodeId,String designId,Connection conn,
			String flowId,Locale locale,String nodeType,String currNode,String currentPerson,LoginBean login,String appendWake,
			MessageResources mr,String tableName,String deptIds,String userIds,String keyId,FlowNodeBean currBean,HashMap map,
			OAWorkFlowTemplate template,String insertTableName,HashMap detailValues) throws Exception{
		
		String lastNodeId = currNode;
		String lastCheckPerson = currentPerson;
		if("back".equals(nodeType)){
			//如果是回退要将当前节点到所回退的节点都标识成无效，默认所有人都回退
			String sql = "update OAMyWorkFlowDet set statusId=1 where f_ref=? "+
				" and sortOrder>=(select top 1 sortOrder from OAMyWorkFlowDet b where b.f_ref=? and statusId=0 and nodeID=? order by sortOrder desc) " ;
			PreparedStatement pss = conn.prepareStatement(sql);
			pss.setString(1, flowId);
			pss.setString(2, flowId);
			pss.setString(3, nextNodeId);
			pss.executeUpdate();
			
			//查询上一个审核节点和审核人
			sql = "select top 1 checkPerson,nodeId from OAMyWorkFlowDet where f_ref=? and statusId=0 order by sortOrder desc";
			pss = conn.prepareStatement(sql);
			pss.setString(1, flowId);
			ResultSet rss = pss.executeQuery();
			lastNodeId = "";
			lastCheckPerson = "";
			if(rss.next()){
				lastNodeId = "'"+rss.getString("nodeId")+"'";
				lastCheckPerson = "'"+rss.getString("checkPerson")+"'";
			}
		}
		//修改当前节点的值,转交到下一个节点,清空全审时审核过的人
		String	sql = "update OAMyWorkFlow set allApprovePerson='',lastNodeId=?,lastCheckPerson=?,allCheckPersons=case isNull(allCheckPersons,'') when '' then ';'+?+';' else allCheckPersons+?+';' end,checkPerson=?,currentNode=?,lastUpdateTime=?,benchmarkTime=? where id=? " ;
		if(tableName!=null && tableName.length()>0){
			sql += " and tableName='"+tableName+"'" ;
		}
		PreparedStatement pss = conn.prepareStatement(sql);
		pss.setString(1,lastNodeId) ;
		pss.setString(2,lastCheckPerson) ;
		pss.setString(3,currentPerson) ;
		pss.setString(4,currentPerson) ;
		pss.setString(5,nextCheckPerson) ;
		pss.setString(6,nextNodeId) ;
		pss.setString(7,BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));		
		pss.setFloat(8,BaseEnv.workFlowDesignBeans.get(designId).getFlowNodeMap().get(nextNodeId).getLimitMinute());
		pss.setString(9,flowId) ;
		pss.executeUpdate() ;	
		
		/** 添加单据已经审批人的记录*/
		addHasCheckPerson(tableName, keyId, currentPerson,nextCheckPerson, conn);
		
		
		//修改单据中的状态						
		sql = "update bill set bill.workFlowNode=flow.currentNode,bill.checkPersons=flow.checkPerson "
			+ "from "+insertTableName+" bill join OAMyWorkFlow flow on bill.id=flow.keyId where bill.id='"+keyId+"'";
		Statement st=conn.createStatement();
		st.execute(sql);
		
		//CRM客户列表，修改CRMCLientInfoEmp中的数据，解决性能问题
		if("CRMClientInfo".equals(insertTableName)){
			sql = "delete CRMClientInfoEmp where f_ref='"+keyId+"' and empType=2 ";
			st = conn.createStatement();
			st.execute(sql);
			String checkp = nextCheckPerson;
			String[] checks =(checkp==null?"":checkp).split(";");
			for(String check :checks){
				if(check.length() > 0){
					sql = "insert into CRMClientInfoEmp(id,f_ref,employeeID,DepartmentCode,empType) values('"+IDGenerater.getId()+"','"+keyId+"','"+ check+"','"+ ""+"',2)";
					st = conn.createStatement();
					st.execute(sql);
				}
			}
		}
		
		//转交后执行的define操作 --这里必须在发送消息前执行，因为如果报错，就不能再发送消息
		String defineName="";
		if("back".equals(nodeType) && currBean.getBackExec().length()>0){
			defineName=currBean.getBackExec();
		}else if("stop".equals(nodeType) && currBean.getStopExec().length()>0){
			defineName=currBean.getStopExec();								
		}else if(nodeType.length()==0 && currBean.getPassExec().length()>0){
			defineName=currBean.getPassExec();
		}

		if(defineName!=null && defineName.length()>0){
			DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(defineName);
			if (defineSqlBean != null) {
				HashMap values = new HashMap();
				values.put("id", keyId);
				values.put("tableName",tableName);
				Result rs1 = defineSqlBean.execute(conn, values,login.getId(), null, null, "");
				if (rs1.retCode != ErrorCanst.DEFAULT_SUCCESS) {
					return rs1;
				}
			}
		} 
		if("-1".equals(nextNodeId) ){ // 执行审核结束define 
			if(!template.getTemplateClass().startsWith("00002")){
				Result rst2 = execCheckDefine(insertTableName,keyId, designId, login, mr, locale,conn,detailValues);
				if (rst2.retCode != ErrorCanst.DEFAULT_SUCCESS) {
					return rst2;
				}
			}else{
				st=conn.createStatement();
				sql="update "+insertTableName+" set workFlowNodeName='finish',workFlowNode='-1' where id='"+keyId+"'" ;
				st.execute(sql);
			}
		}
		
		return this.deliverWake(designId,currNode, nextNodeId, flowId, conn, locale, nodeType,login,appendWake,mr,deptIds,userIds);  
	}
	
	/**
	 * 添加单据已经审批人的记录
	 * @param tableName
	 * @param keyId
	 * @param checkperson 这是当前审核人自己，
	 * @param nextCheckPerson 下一结点的审核人
	 */
	public void addHasCheckPerson(String tableName,String keyId,String checkperson,String nextCheckPerson,
			Connection conn) throws Exception{
			String sql = "";
			PreparedStatement pss = null;    
			if(checkperson!=null){
				/*如果自己不在已审核人中，则加入自己*/
				sql = "select keyId from OAMyWorkFlowPerson where keyId=? and tableName=? and checkperson=? ";
				pss = conn.prepareStatement(sql);
				pss.setString(1, keyId); 
				pss.setString(2, tableName);
				pss.setString(3, checkperson);
				ResultSet rss = pss.executeQuery();
				if(!rss.next()){
					//不存在添加记录
					sql = "insert into OAMyWorkFlowPerson(keyId,tableName,checkperson,hadApprover,curApprover) values(?,?,?,?,?)";
					pss = conn.prepareStatement(sql);
					pss.setString(1, keyId);
					pss.setString(2, tableName);
					pss.setString(3, checkperson);
					pss.setInt(4, 1);
					pss.setInt(5, 0);
					pss.execute();
				}else{ //已经存在修改自己为已审核人
					sql = "update OAMyWorkFlowPerson set hadApprover = 1 where keyId=? and tableName=? and checkperson=? ";
					pss = conn.prepareStatement(sql);
					pss.setString(1, keyId);
					pss.setString(2, tableName);
					pss.setString(3, checkperson);
					pss.execute();
				}
			}
			/*删除当前单据的审批人，因为多为指定多人审核时，自己审了，其它人无需在审，则把所有其它已审核人都删除*/
			sql = "delete from OAMyWorkFlowPerson where keyId=? and tableName=? and hadApprover=0 and curApprover=1";
			pss = conn.prepareStatement(sql);
			pss.setString(1, keyId);
			pss.setString(2, tableName);
			pss.execute();
			
			/*如果当前单据单核人是已审核人，不能删除，只能修改*/
			sql = "update OAMyWorkFlowPerson set curApprover = 0  where keyId=? and tableName=? and hadApprover=1 and curApprover=1";
			pss = conn.prepareStatement(sql);
			pss.setString(1, keyId);
			pss.setString(2, tableName);
			pss.execute();
			
			String hasAddstr = "";
			for(String strCheck : nextCheckPerson.split(";")){ //把下一结点的审核人加为当前审核人
				if(strCheck.length()==0) continue;
				if(hasAddstr.indexOf(strCheck+";") == -1){
					hasAddstr +=strCheck+";";
					
					
					sql = "select keyId from OAMyWorkFlowPerson where keyId=? and tableName=? and checkperson=? ";
					pss = conn.prepareStatement(sql);
					pss.setString(1, keyId); 
					pss.setString(2, tableName);
					pss.setString(3, strCheck);
					ResultSet rss = pss.executeQuery();
					if(!rss.next()){
						//不存在添加记录
						sql = "insert into OAMyWorkFlowPerson(keyId,tableName,checkperson,hadApprover,curApprover) values(?,?,?,?,?)";
						pss = conn.prepareStatement(sql);
						pss.setString(1, keyId);
						pss.setString(2, tableName);
						pss.setString(3, strCheck);
						pss.setInt(4, 0);
						pss.setInt(5, 1); 
						pss.execute();
					}else{ //已经存在修改自己为已审核人
						sql = "update OAMyWorkFlowPerson set curApprover = 1 where keyId=? and tableName=? and checkperson=? ";
						pss = conn.prepareStatement(sql);
						pss.setString(1, keyId);
						pss.setString(2, tableName);
						pss.setString(3, strCheck);
						pss.execute();
					}
				}
			}
	}
	

	/*
	 * 撤回用户已经转交的节点
	 * @templateId 对应工作流模板ID
	 */
	public Result cancel(final String id,final String lastNodeId,final String currNode,final LoginBean loginBean,final String designIds) throws BSFException{
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn)  {
						try {
							String billId="";
							String sql="select checkPerson,keyId,tableName,applyType from OAMyWorkFlow where id=?";
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, id);
							ResultSet rst=pss.executeQuery();
							String tableName="";
							String designId = designIds ;
							if(rst.next()){
								billId=rst.getString(2);
								tableName=rst.getString(3);
								designId = rst.getString(4) ;
							}
							
							sql="insert into OAMyWorkFlowDet (id,f_ref,nodeId,checkPerson,startTime,createBy,nodeType,endTime) values (?,?,?,?,?,?,?,?)";
							pss = conn.prepareStatement(sql) ;
							pss.setString(1, IDGenerater.getId()) ;
							pss.setString(2, id);
							pss.setString(3, lastNodeId) ;
							pss.setString(4, loginBean.getId());
							pss.setString(5,BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)) ;
							pss.setString(6, loginBean.getId()) ;
							pss.setString(7, "cancel"); 
							pss.setString(8, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
							pss.executeUpdate();
							
							//将之前当前用户办理的节点的状态设置为无效状态
							sql="update OAMyWorkFlowDet set statusId=1 where f_ref=? and ((nodeId=? and checkPerson=?)) ";
							pss = conn.prepareStatement(sql) ;
							pss.setString(1, id);
							pss.setString(2, lastNodeId);
							pss.setString(3, loginBean.getId());
							pss.executeUpdate();
							//在用户点击撤消之前有可能下一节点的审核人已经打开单据查看，并生成了一条oamyworkflowdet的数据，所以这条数据必须删除
							sql="delete OAMyWorkFlowDet where f_ref=? and len(isnull(endTime,''))=0 ";
							pss = conn.prepareStatement(sql) ;
							pss.setString(1, id);
							pss.executeUpdate();
							
							if(lastNodeId.equals(currNode)){
								sql="update OAMyWorkFlow set checkPerson=checkPerson+?+';' where id=?";
								pss=conn.prepareStatement(sql);
								pss.setString(1, loginBean.getId());
								pss.setString(2, id);
								pss.execute();
								
								addHasCheckPerson(tableName, id, null, loginBean.getId(), conn);
							}else{
								//当撤回节点小于当前节点，那么要将撤回节点作为当前节点，并且要根据用户输入的数据查询下一个节点的值
								//查询上一个审核节点和审核人
								sql = "select top 1 checkPerson,nodeId from OAMyWorkFlowDet where f_ref=? and statusId=0 order by sortOrder desc";
								pss = conn.prepareStatement(sql);
								pss.setString(1, id);
								ResultSet rss = pss.executeQuery();
								String lastCheckNode = "";
								String lastCheckPerson = "";
								if(rss.next()){
									lastCheckNode = rss.getString("nodeId");
									lastCheckPerson = rss.getString("checkPerson");
								}
								sql="update OAMyWorkFlow set checkPerson=?,currentNode=?,lastNodeId=?,lastCheckPerson=? where id=?";
								pss = conn.prepareStatement(sql) ;
								pss.setString(1,";"+loginBean.getId()+";");
								pss.setString(2,lastNodeId);
								pss.setString(3, lastCheckNode);
								pss.setString(4, lastCheckPerson);
								pss.setString(5,id);
								pss.execute();		
								
								//把当前审核人重新改回自己
								addHasCheckPerson(tableName, id, null, loginBean.getId(), conn); 
							}
							//修改单据中的状态
							String tempTableName = new DynDBManager().getInsertTableName(tableName);
							tableName=BaseEnv.workFlowInfo.get(designId).getTemplateFile();
							sql="update "+tempTableName+" set workFlowNode=(select currentNode from OAMyWorkFlow where "+tempTableName+".id=OAMyWorkFlow.keyId and tableName='"+tableName+"' ),"+
								"checkPersons=(select checkPerson from OAMyWorkFlow where "+tempTableName+".id=OAMyWorkFlow.keyId and tableName='"+tableName+"' ) where id='"+billId+"'";
							Statement st=conn.createStatement();
							st.execute(sql);
							
							//CRM客户列表，修改CRMCLientInfoEmp中的数据，解决性能问题
							if("CRMClientInfo".equals(tempTableName)){
								sql = "delete CRMClientInfoEmp where f_ref='"+billId+"' and empType=2 ";
								st = conn.createStatement();
								st.execute(sql);
								//撤回后，checkPersion只有自己
								String checkp = loginBean.getId();
								String[] checks =(checkp==null?"":checkp).split(";");
								for(String check :checks){
									if(check.length() > 0){
										sql = "insert into CRMClientInfoEmp(id,f_ref,employeeID,DepartmentCode,empType) values('"+IDGenerater.getId()+"','"+billId+"','"+ check+"','"+ ""+"',2)";
										st = conn.createStatement();
										st.execute(sql);
									}
								}
							}
							
						} catch (Exception e) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							e.printStackTrace();
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}
	
	
	/**
	 * 批量转交
	 * @param ids
	 * @param loginBean
	 * @return
	 * @throws BSFException
	 *//*
	public Result deliverToAll(final String[] ids,final LoginBean loginBean,final Locale locale,final MessageResources mr) throws BSFException{
		final OAMyWorkFlowMgt mgt=new OAMyWorkFlowMgt();
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn)  {
						try {
							PreparedStatement pss;							
							String keyIds="";
							for(int i=0;i<ids.length;i++){
								keyIds+="'"+ids[i]+"',";
							}
					
							String sql = "select a.id,a.keyId,applyType,tableName,a.createBy,currentNode,nextNodeIds,departmentCode,b.startTime,b.endTime,a.checkPerson,a.applyContent from OAMyWorkFlow a left join OAMyWorkFlowDet b "+
									" on a.id=b.f_ref and a.currentNode=b.nodeId and b.checkPerson='"+loginBean.getId()+"' and b.statusId=0 where a.keyId in ("+keyIds.substring(0,keyIds.length()-1)+") and charIndex(';"+loginBean.getId()+";',a.checkPerson)>0";
							Statement st=conn.createStatement();
							ResultSet rs=st.executeQuery(sql);
							ArrayList list=new ArrayList();
							while(rs.next()){
								MyWorkFlow flow=new MyWorkFlow();
								flow.setId(rs.getString("id"));
								flow.setBillId(rs.getString("keyId"));
								flow.setApplyType(rs.getString("applyType"));
								flow.setTableName(rs.getString("tableName"));
								flow.setCreateBy(rs.getString("createBy"));
								flow.setCurrentNode(rs.getString("currentNode"));
								flow.setNextNodeIds(rs.getString("nextNodeIds"));
								flow.setDepartmentCode(rs.getString("departmentCode"));
								flow.setStartTime(rs.getString("startTime"));
								flow.setEndTime(rs.getString("endTime"));
								flow.setCheckPerson(rs.getString("checkPerson"));
								flow.setApplyContent(rs.getString("applyContent"));
								list.add(flow);
							}
							rst.setRetVal(list);
						} catch (Exception e) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							e.printStackTrace();
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		final List list=(ArrayList)rst.retVal;
		for(int i=0;i<list.size();i++){
			final MyWorkFlow flow=(MyWorkFlow)list.get(i);
			Result resVal=new DynDBManager().detail(flow.getTableName(), BaseEnv.tableInfos, flow.getBillId(), loginBean.getSunCompany(), BaseEnv.propMap, loginBean.getId(), true, new ArrayList(),"");
			if(resVal.retCode!=ErrorCanst.DEFAULT_SUCCESS){
				continue;
			}
			final HashMap values=(HashMap)resVal.retVal;
			final Result rstt = new Result() ;
			retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn)  {
						try {					
								String sql="";
								PreparedStatement pss;
								String nextNodeId="";
								WorkFlowDesignBean designBean=BaseEnv.workFlowDesignBeans.get(flow.getApplyType());
								if(flow.getNextNodeIds()!=null&&flow.getNextNodeIds().length()>0){
									nextNodeId=flow.getNextNodeIds().split(";")[0];
								}else{//如果没有下一节点，那么要看是否有条件，如果有条件要查询相关单据，如果没有条件直接查找当前节点的下一节点					
									nextNodeId=mgt.getNextNodeIdByVal(flow.getApplyType(), flow.getCurrentNode(), flow.getId(), conn,false,loginBean,values);
								}
								//查询下一节点的审核人
								String nextCheckPerson="";
								Result rst2=mgt.getNodeCheckPByCongign(flow.getApplyType(), nextNodeId, flow.getId(), flow.getDepartmentCode(),conn,flow.getTableName(),loginBean.getId());
								if(rst2.retCode!=ErrorCanst.DEFAULT_SUCCESS){
									rstt.setRetCode(rst2.getRetCode());
									return ;
								}else{
									nextCheckPerson=rst2.retVal.toString();
								}
								
								//插入工作流明细记录，自动插入查找到得下一节点和下一节点的审核人。 如果有开始时间，没有结束时间，直接修改结束时间，否则直接插入一条记录
								if(flow.getStartTime()!=null&&flow.getStartTime().length()>0){
									sql="update OAMyWorkFlowDet set endTime=?,workFlowNode=?,checkPersons=?,nodeType=case ? when 'back' then 'back' else nodeType end where f_ref=? and nodeId=? and checkPerson=? and statusId=0" ;										
									pss = conn.prepareStatement(sql);
									pss.setString(1,BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)) ;
									pss.setString(2,nextNodeId);
									pss.setString(3,nextCheckPerson);
									pss.setString(4,"transact");
									pss.setString(5,flow.getId()) ;
									pss.setString(6,flow.getCurrentNode()) ;
									pss.setString(7,loginBean.getId());
									pss.executeUpdate() ;
								}else{
									sql="insert into OAMyWorkFlowDet (id,f_ref,nodeId,checkPerson,startTime,endTime,createBy,nodeType,workFlowNode,checkPersons) values (?,?,?,?,?,?,?,?,?,?,?)";
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, IDGenerater.getId()) ;
									pss.setString(2, flow.getId());
									pss.setString(3, flow.getCurrentNode()) ;
									pss.setString(4, loginBean.getId());
									pss.setString(5,BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)) ;
									pss.setString(6,BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)) ;
									pss.setString(7, flow.getCreateBy()) ;
									pss.setString(8, "transact"); 
									pss.setString(9, nextNodeId);
									pss.setString(10, nextCheckPerson);
									pss.executeUpdate();
								}									
								
								//修改工作流主表数据
								boolean flag=false;
								if(designBean.getFlowNodeMap().get(flow.getCurrentNode()).isUseAllApprove()){
									if(flow.getCheckPerson().equals(";"+loginBean.getId()+";")){//如果是全审，并且最后一个用户已经审核完毕
										mgt.allCheckDeliverTo(flow.getApplyType(), flow.getId(), flow.getCurrentNode(), conn, flow.getApplyBy(), flow.getCreateBy(),locale,flow.getApplyContent(),loginBean,mr);
										flag=true;
									}else{//去掉当前工作流审核人中的当前登录用户										
										if(flow.getCheckPerson().indexOf(";"+loginBean.getId()+";")+(";"+loginBean.getId()+";").length()>=flow.getCheckPerson().length()){
												flow.setCheckPerson(flow.getCheckPerson().substring(0,flow.getCheckPerson().indexOf(";"+loginBean.getId()+";")+1));
										}else{
											flow.setCheckPerson(flow.getCheckPerson().substring(0,flow.getCheckPerson().indexOf(";"+loginBean.getId()+";")+1)+
													flow.getCheckPerson().substring(flow.getCheckPerson().indexOf(";"+loginBean.getId()+";")+(";"+loginBean.getId()+";").length()));
										}
										
										sql="update OAMyWorkFlow set checkPerson=? where id=?";
										pss = conn.prepareStatement(sql);
										
										pss.setString(1,flow.getCheckPerson()) ;
										pss.setString(2,flow.getId()) ;
										pss.executeUpdate() ;										
									}
								}else{
									mgt.checkDeliverTo(nextCheckPerson, nextNodeId, flow.getApplyType(), conn, flow.getId(), locale, "", flow.getCurrentNode(), loginBean, "", mr, flow.getTableName(), "", "");
									flag=true;
								}
								
								String []flowDepicts=mgt.getFlowDepict(flow.getApplyType(),flow.getId(), conn,locale,mr);								
								sql="update OAMyWorkFlow set flowDepict=?,flowDepictTitle=? where id=?";
								pss = conn.prepareStatement(sql) ;
								pss.setString(1, flowDepicts[0]) ;
								pss.setString(2, flowDepicts[1]) ;
								pss.setString(3,flow.getId()) ;
								pss.executeUpdate();
								
								mgt.updateMsgContent(flow.getBillId(),loginBean,conn,designBean.getFlowNodeMap().get(flow.getCurrentNode()).isUseAllApprove());
								
								//修改单据中的状态
								String tableName=BaseEnv.workFlowInfo.get(flow.getApplyType()).getTemplateFile();
								sql="update "+tableName+" set workFlowNode=(select currentNode from OAMyWorkFlow where "+tableName+".id=OAMyWorkFlow.keyId and tableName='"+tableName+"'),"+
									"checkPersons=(select checkPerson from OAMyWorkFlow where "+tableName+".id=OAMyWorkFlow.keyId and tableName='"+tableName+"') where id='"+flow.getBillId()+"'";
								pss=conn.prepareStatement(sql);
								pss.executeUpdate();
								
								if(nextNodeId.equals("-1")&&flag&&BaseEnv.workFlowInfo.get(flow.getApplyType()).getTemplateClass().equals("00001")){
									rst2= mgt.execCheckDefine(tableName,flow.getBillId(),flow.getApplyType(), loginBean, mr, locale,conn,values);
									rstt.setRetCode(rst2.retCode);
									rstt.setRetVal(rst2.retVal);
								}
						} catch (Exception e) {
							rstt.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							e.printStackTrace();
						}
					}
				}) ;
				return rstt.getRetCode() ;
			}
		}) ;
			if(rstt.retCode!=ErrorCanst.DEFAULT_SUCCESS){
				rst.setRetVal(rstt.getRetVal());
				break;
			}
		}
		//转交的单据中可能有需要重算成本的
    	if(rst.retCode==ErrorCanst.DEFAULT_SUCCESS&&BaseEnv.systemSet.get("SaveBillAutoRecalc")!=null&&BaseEnv.systemSet.get("SaveBillAutoRecalc").getSetting().equals("true")){
    		ReCalcucateMgt sysMgt=new ReCalcucateMgt();
    		AccPeriodBean accBean=BaseEnv.accPerios.get(loginBean.getSunCmpClassCode());
    		sysMgt.reCalcucateData(loginBean.getSunCmpClassCode(),accBean.getAccYear(),  accBean.getAccMonth(), loginBean.getId());
    	}
		rst.setRetCode(retCode) ;
		return rst ;
	}
    */
	
	/**
	 * 查询下一个审批人
	 * @param designId
	 * @param nodeId
	 * @param id
	 * @param deptCode
	 * @param conn
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Result getNodeCheckPByCongign(String designId, String nodeId,
			String id, String deptCode, Connection conn, String tableName,String userId) throws Exception {
		
		Result rst = new Result();
		WorkFlowDesignBean designBean = BaseEnv.workFlowDesignBeans.get(designId);
		String nextCheckPerson = "";
		if ("CHECK".equals(designBean.getFlowNodeMap().get(nodeId).getZAction())) {
			nextCheckPerson = ";";
			ArrayList<String[]> checkPersons = this.getNodeCheckPerson(designBean.getFlowNodeMap().get(nodeId), deptCode, id,conn, tableName,userId);
			if (checkPersons.size() > 0) {
				for (int j = 0; j < checkPersons.size(); j++) {
					nextCheckPerson += checkPersons.get(j)[0] + ";";
				}
			} else {
				rst.setRetCode(ErrorCanst.ER_NO_DATA);
				return rst;
			}
		}
		rst.setRetVal(nextCheckPerson);
		return rst;
	}
	
	public String getNextNodeIdByVal(String designId,String currNodeId,String id,Connection conn,
			boolean allNode,LoginBean loginBean,HashMap values)throws Exception{
		
		String tableName = BaseEnv.workFlowInfo.get(designId).getTemplateFile();
		String nextNodeId="";
		
		WorkFlowDesignBean designBean=BaseEnv.workFlowDesignBeans.get(designId);
		FlowNodeBean currBean=designBean.getFlowNodeMap().get(currNodeId);
		if("CHOICE".equals(designBean.getFlowNodeMap().get(currBean.getTo()).getZAction()) || allNode){	
			String nextNodeIds=this.getNextNodeIds(designId, currNodeId, values,tableName,conn);
			if(allNode){
				nextNodeId=nextNodeIds;
			}else{
				nextNodeId=nextNodeIds.split(";")[0];
			}
			if("0".equals(currNodeId)){//发起人和紧邻步骤的审批人相同时自动略过
				Result rs=this.autoPass(designId, loginBean.getId(),id, "0", nextNodeId, loginBean.getDepartCode(),values,tableName,conn);
				if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS&&rs.retVal!=null){
					nextNodeId=rs.retVal.toString();
				}
			}
		}else{
			nextNodeId=currBean.getTo();
		}
		return nextNodeId;
	}
	
	public HashMap getTableVal(String designId,String keyId,Connection conn,String tableName)throws Exception{
		HashMap values=new HashMap(); 
		
		String sql="select ";
		List<DBFieldInfoBean> fieldList=((DBTableInfoBean)BaseEnv.tableInfos.get(tableName)).getFieldInfos();
		for(int j=0;j<fieldList.size();j++){
			sql+=fieldList.get(j).getFieldName()+",";
		}
		sql=sql.substring(0,sql.length()-1);
		sql+=" from "+tableName+" where id='"+keyId+"'";
		
		Statement st = conn.createStatement() ;
		ResultSet rst=st.executeQuery(sql);
		if(rst.next()){
			for(int j=0;j<fieldList.size();j++){
				values.put(fieldList.get(j).getFieldName(), rst.getString(fieldList.get(j).getFieldName()));
			}
		}
		return values;
	}
	
	/**
	 * 如果工作流删除了，那么对应的通知消息和邮件也要删除
	 * @param billId
	 * @param receive
	 * @param conn
	 * @throws SQLException
	 */
	public void delMsgContent(String billId,Connection conn) throws SQLException{
		
		//将此工作流的所有邮件消息都删除掉
		String sql="delete from OAMailInfo where relationId=? ";
		PreparedStatement pss = conn.prepareStatement(sql) ;
		pss.setString(1,billId) ;
		pss.executeUpdate();
	}
	
	
	public void updateMsgContent(String billId,LoginBean receive,Connection conn,boolean isAllCheck) throws SQLException{
		//如果不是全审，删除其它用户的通知
		String sql="";
		if(!isAllCheck){
			sql="delete from OAMailInfo where relationId=? and mailTo!=? ";
			PreparedStatement pss = conn.prepareStatement(sql) ;
			pss.setString(1,billId) ;
			pss.setString(2,receive.getEmpFullName()) ;
			pss.executeUpdate();
		}
		
		sql="delete from OAMailInfo  where relationId=? and mailTo=? ";
		PreparedStatement pss = conn.prepareStatement(sql) ;
		pss.setString(1,billId) ;
		pss.setString(2,receive.getEmpFullName()) ;
		pss.executeUpdate();
	}
	
	
	/**
	 * 添加单据的时候，向我的工作流插入数据
	 */
	public Result addOAMyWorkFlow(final MyWorkFlow workFlow){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						String sql = "insert into OAMyWorkFlow(id,billNo,applyDate,applyBy,department,applyType,noteType,checkPerson," +
								"tableName,flowName,keyId,applyContent,createBy,workFlowNode,orderIndex) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" ;
						PreparedStatement pss = conn.prepareStatement(sql) ;
						pss.setString(1, IDGenerater.getId()) ;
						pss.setString(2, workFlow.getBillNo()) ;
						pss.setString(3, workFlow.getApplyDate()) ;
						pss.setString(4, workFlow.getApplyBy()) ;
						pss.setString(5, workFlow.getDepartment()) ;
						pss.setString(6, workFlow.getApplyType()) ;
						pss.setString(7, workFlow.getNoteType()) ;
						pss.setString(8, "userId:"+workFlow.getCheckPerson()+";");
						pss.setString(9, workFlow.getTableName()) ;
						pss.setString(10, workFlow.getFlowName()) ;
						pss.setString(11, workFlow.getBillId()) ;
						pss.setString(12, workFlow.getApplyContent()) ;
						pss.setString(13, workFlow.getCreateBy()) ;
						pss.setString(14, workFlow.getWorkFlowNode()) ;
						pss.setInt(15, workFlow.getOrderIndex()) ;
						int num = pss.executeUpdate() ;
						if(num>0){
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
						}else{
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}
	
	
	/**
	 * 删除我的工作流信息
	 */
	public Result deleteOAMyWorkFlow(final String keyId){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						String sql = "delete from OAMyWorkFlow where keyId in ("+keyId+")" ;
						PreparedStatement pss = conn.prepareStatement(sql) ;
						pss.executeUpdate() ;
						
						sql = "delete from tblAuditeDeliverance where f_ref in (select id from OAMyWorkFlowDet where f_ref in ("+keyId+")) " ;
						pss = conn.prepareStatement(sql) ;
						pss.executeUpdate() ;
						
						sql = "delete from OAMyWorkFlowDet where f_ref in ("+keyId+")" ;
						pss = conn.prepareStatement(sql) ;
						pss.executeUpdate() ;
						
						// Action里调用AdviceMgt
//						sql="delete from tblAdvice where relationId in ("+keyId+")";
//						pss = conn.prepareStatement(sql) ;
//						pss.executeUpdate();
						
						sql="delete from OAMailInfo  where relationId in ("+keyId+")";
						pss = conn.prepareStatement(sql) ;
						pss.executeUpdate();
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}
	
	
	
	/**
	 * 根据单据Id查询 工作流设计ID
	 */
	public Result queryTableNameById(final String[] keyId){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						String keyIds="";
						for(int i=0;i<keyId.length;i++){
							keyIds+="'"+keyId[i]+"',";
						}
						keyIds=keyIds.substring(0,keyIds.length()-1);
						String sql = "select tableName from OAMyWorkFlow where billNo in ("+keyIds+")" ;
						PreparedStatement pss = conn.prepareStatement(sql) ;
						ResultSet rss = pss.executeQuery() ;
						List<String> tableList=new ArrayList<String>();
						if(rss.next()){
							tableList.add(rss.getString("tableName"));
							rst.setRetVal(tableList) ;
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}

	/**
	 * 查询单据编号值
	 */
	public Result queryBillVal(final String keyId,final String tableName,final String fieldName){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						String sql = "select "+fieldName+" from "+tableName+" where id = '"+keyId+"' " ;
						PreparedStatement pss = conn.prepareStatement(sql) ;
						ResultSet rss = pss.executeQuery() ;
						if(rss.next()){
							rst.setRetVal(rss.getString(""+fieldName+"")) ;
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}
	
	/**
	 * 删除我的工作流信息
	 */
	public Result deleteTblMyWorkFlow(final String keyId){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						String sql = "delete from tblMyWorkFlow where keyId in ("+keyId+")" ;
						PreparedStatement pss = conn.prepareStatement(sql) ;
						int num = pss.executeUpdate() ;
						if(num>0){
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
						}else{
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}
	
	/**
	 * 修改OA我的工作流
	 */
	public Result updateOAMyWorkFlow(final MyWorkFlow workFlow){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						String sql = "update OAMyWorkFlow set billNo=?,applyDate=?,applyBy=?,department=?,applyType=?,noteType=?,checkPerson=?," +
								"tableName=?,flowName=?,applyContent=?,createBy=? where id=?" ;
						PreparedStatement pss = conn.prepareStatement(sql) ;
						pss.setString(1, workFlow.getBillNo()) ;
						pss.setString(2, workFlow.getApplyDate()) ;
						pss.setString(3, workFlow.getApplyBy()) ;
						pss.setString(4, workFlow.getDepartment()) ;
						pss.setString(5, workFlow.getApplyType()) ;
						pss.setString(6, workFlow.getNoteType()) ;
						pss.setString(7, ","+workFlow.getCheckPerson());
						pss.setString(8, workFlow.getTableName() ) ;
						pss.setString(9, workFlow.getFlowName()) ;
						pss.setString(10, workFlow.getApplyContent()) ;
						pss.setString(11, workFlow.getCreateBy()) ;
						pss.setString(12, workFlow.getId()) ;
						int num = pss.executeUpdate() ;
						if(num>0){
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
						}else{
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}
	
	
	/**
	 * 修改OA工作流的状态-1删除，0正常
	 */
	public Result updateStatus(final String statusId,final String[] keyId){		
		final Result rst = new Result() ;
		final ArrayList<String[]> list = new ArrayList<String[]>();
		
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) {
						try{
							long time=System.currentTimeMillis();
							for(int i=0;i<keyId.length;i++){
								String templateName="";
								String applyContent="";
								String templateClass="";
								String tableName = "";
								
								String sql = "select templateName,tableName,applyContent,templateClass from OAMyWorkFlow a join OAWorkFlowTemplate b on a.applyType=b.id where a.id = ?";
								PreparedStatement pss = conn.prepareStatement(sql) ;
								pss.setString(1, keyId[i]);
								ResultSet rset = pss.executeQuery();
								if(rset.next()){
									templateName = rset.getString("templateName");
									applyContent = rset.getString("applyContent");
									tableName = rset.getString("tableName");
									templateClass = rset.getString("templateClass");
									list.add(new String[]{keyId[i],templateName,applyContent,tableName});//用于记录日志 
									if(statusId.equals("-1") && !templateClass.startsWith("00002")){//如果工作流删除，不能是ERP
										rst.setRetCode(ErrorCanst.RET_FORBID_UPDATE_ERROR) ;
										rst.retVal = "非OA工作流["+templateName+"]不允许在工作流监控中删除";
										return;
									}
								}else{
									rst.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
									rst.retVal = "工作流不存在";
								}
								
								
								sql = "update OAMyWorkFlow set statusId="+statusId+" where id = ?" ;
								pss = conn.prepareStatement(sql) ;
								pss.setString(1, keyId[i]);
								int num = pss.executeUpdate() ;
								time=System.currentTimeMillis();
								if(num>0){
									if(statusId.equals("-1")){//如果工作流删除，那么同时发出的通知、邮件都要删除
										OAMyWorkFlowMgt oaflow=new OAMyWorkFlowMgt();
										oaflow.delMsgContent(keyId[i],conn);
									}									
									rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
								}else{
									rst.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
								}
							}
						}catch(Exception ex){
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
							BaseEnv.log.error("OAMyWorkFlowMgt.updateStatus ",ex);
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		if(retCode == ErrorCanst.DEFAULT_SUCCESS){
			rst.retVal = list;
		}
		
		return rst ;
	}
	/**
	 * 修改OA工作流的状态-1删除，0正常
	 */
	public Result delete(final String[] keyId){		
		final Result rst = new Result() ;
		final ArrayList<String[]> list = new ArrayList<String[]>();
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {						
						for(int i=0;i<keyId.length;i++){
							String templateName="";
							String applyContent="";
							String templateClass="";
							String tableName = "";
							String wkeyId = "";
							
							String sql = "select a.keyId,templateName,tableName,applyContent,templateClass from OAMyWorkFlow a join OAWorkFlowTemplate b on a.applyType=b.id where a.id = ?";
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, keyId[i]);
							ResultSet rset = pss.executeQuery();
							if(rset.next()){
								templateName = rset.getString("templateName");
								applyContent = rset.getString("applyContent");
								tableName = rset.getString("tableName");
								templateClass = rset.getString("templateClass");
								wkeyId = rset.getString("keyId");
								list.add(new String[]{keyId[i],templateName,applyContent,tableName});//用于记录日志 
								if(!templateClass.startsWith("00002")){//如果工作流删除，不能是ERP
									rst.setRetCode(ErrorCanst.RET_FORBID_UPDATE_ERROR) ;
									rst.retVal = "非OA工作流"+templateName+"["+applyContent+"]不允许在工作流监控中删除";
									return;
								}
							}else{
								rst.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
								rst.retVal = "工作流不存在";
							}
														
							pss = conn.prepareStatement("delete from "+tableName+" where id=?") ;
							pss.setString(1, wkeyId);
							pss.execute();

							pss=conn.prepareStatement("delete from  OAMyWorkFlow where id = ?");
							pss.setString(1, keyId[i]);
							pss.execute();
							pss=conn.prepareStatement("delete from OAMyWorkFlowDet where f_ref = ?");
							pss.setString(1, keyId[i]);
							pss.execute();
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		if(retCode == ErrorCanst.DEFAULT_SUCCESS){
			rst.retVal = list;
		}
		return rst ;
	}
	
	/**
	 * 删除时 判断要删除工作流结点是否是当前结点
	 */
	public Result startFlow(final String[] keyId,final String createBy){		
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						String keyIds="";
						for(int i=0;i<keyId.length;i++){
							keyIds+="'"+keyId[i]+"',";
						}
						keyIds=keyIds.substring(0,keyIds.length()-1);
						String sql="select keyId,tableName from oamyworkflow where id in ("+keyIds+") and (currentNode != '0' or createBy!='"+createBy+"')";
						Statement pss = conn.createStatement();
						ResultSet rs = pss.executeQuery(sql);
						if(rs.next()){
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}
	
	/**
	 * 获取流程审批记录
	 * @param designId
	 * @param keyId
	 * @param mr
	 * @param locale
	 * @param tableName
	 * @param applyBy
	 * @return
	 */
	public Result flowDepict(final String designId,final String keyId,final String tableName){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) {
						WorkFlowDesignBean  designBean = BaseEnv.workFlowDesignBeans.get(designId);
						String sql = "select nodeID,checkPerson,startTime,endTime,nodeType,approvalOpinions,affix,workFlowNode,checkPersons " +
								"from OAMyWorkFlowDet where f_ref=(select id from OAMyWorkFlow where keyId=? and tableName=?) and len(isNull(endTime,''))>0 order by endTime " ;//sortOrder  
						try {
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, keyId);
							pss.setString(2, tableName);
							ResultSet rss = pss.executeQuery();
							ArrayList<OAMyWorkFlowDetBean> logList = new ArrayList<OAMyWorkFlowDetBean>();
							while(rss.next()){
								OAMyWorkFlowDetBean flowDet = new OAMyWorkFlowDetBean();
								String checkPerson = rss.getString("checkPerson");
								OnlineUser approveUser = OnlineUserInfo.getUser(checkPerson);
								if(approveUser!=null){
									flowDet.setCheckPerson(approveUser.getName());
								}
								String nodeId = rss.getString("nodeId");
								FlowNodeBean nodeBean = designBean.getFlowNodeMap().get(nodeId);
								if(nodeBean!=null){
									flowDet.setNodeID(nodeBean.getDisplay());
								}
								flowDet.setStartTime(rss.getString("startTime"));
								flowDet.setEndTime(rss.getString("endTime"));
								String nodeType = rss.getString("nodeType");
								if("back".equals(nodeType)){
									nodeType = "回退";
								}else if("cancel".equals(nodeType)){
									nodeType = "撤回";
								}else if("retAudit".equals(nodeType)){
									nodeType = "反审核";
								}else if("reSet".equals(nodeType)){
									nodeType = "重置";
								}else if("affix".equals(nodeType)){
									nodeType = "补充意见";
									flowDet.setNodeID(nodeType);
								}else{
									nodeType = "转交";
								}
								flowDet.setNodeType(nodeType);
								String workflowNode = rss.getString("workFlowNode");
								FlowNodeBean nextNode = designBean.getFlowNodeMap().get(workflowNode);
								if(nextNode!=null){
									flowDet.setWorkFlowNode(nextNode.getDisplay());
								}
								String nextPerson = rss.getString("checkPersons");
								if(nextPerson!=null && nextPerson.indexOf(";")!=-1){
									for(String person : nextPerson.split(";")){
										OnlineUser nextUser = OnlineUserInfo.getUser(person);
										if(nextUser!=null){
											flowDet.setCheckPersons(nextUser.getName());
										}
									}
								}
								flowDet.setApprovalOpinions(rss.getString("approvalOpinions"));
								flowDet.setAffix(rss.getString("affix"));
								logList.add(flowDet);
							}
							rst.setRetVal(logList);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	}
	
	/**
	 * 
	 * @param designId
	 * @param keyId
	 * @param mr
	 * @param locale
	 * @param tableName
	 * @param applyBy
	 * @return
	 */
	public Result flowDepict2(final String designId,final String keyId,final MessageResources mr,
			final Locale locale,final String tableName,final String applyBy){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) {
						WorkFlowDesignBean  designBean=BaseEnv.workFlowDesignBeans.get(designId);
						try {
							String sql="select a.currentNode,b.nodeId,isnull(endTime,''),isnull((select top 1 endTime+';'+cast(isnull(oaTimeLimitUnit,0) as varchar(2))+';'+cast(isnull(benchMarkTime,0) as varchar(20)) from OAMyWorkFlowDet c where c.f_ref=a.id and c.nodeId!=b.nodeId and c.sortOrder<b.sortOrder and c.statusId=0 order by c.sortOrder desc),'')"+
							" lastEndTime,isnull(a.checkPerson,'') from OAMyWorkFlow a,OAMyWorkFlowDet b where a.id=b.f_ref and b.statusId=0 and b.nodeType!='affix' and b.sortOrder=(select max(sortOrder) from OAMyWorkFlowDet c where c.f_ref=b.f_ref and c.nodeID=b.nodeID) and a.keyId=? and a.tableName=?  order by nodeId";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, keyId) ;
							pss.setString(2, tableName);
							ResultSet rs = pss.executeQuery();
							ArrayList list2=new ArrayList();
							while(rs.next()){
								String[] str=new String[5];
								for(int i=0;i<str.length;i++){
									str[i] = rs.getString(i+1);
								}
								list2.add(str);
							}
							String node = "";
							String msg = "";
							//组织所有已经办结的节点
							for(int i=0;i<list2.size();i++){
								String[] str=(String[])list2.get(i);
								//当前节点不存在或者明细节点=当前节点
								if(designBean.getFlowNodeMap().get(str[1])==null||(str[0].equals(str[1])&&str[4].length()>0)){
									continue;
								}
								node+="<node id='"+str[1]+"' state='";
								if(str[0].equals(str[1])){
									node+="0";//当前节点
								}else{
									node+="1";//完成节点
								}
								node+="' doTime='"+str[2]+"' standardTime='";
								if(str[3].length()>0){
									Date standDate = getStandDate(designBean.getFlowNodeMap().get(str[1]), str[3],0,0);	
									Date finishDate=str[2].length()==0?new Date():BaseDateFormat.parse(str[2], BaseDateFormat.yyyyMMddHHmmss);
									node+=(standDate==null?"":BaseDateFormat.format(standDate, BaseDateFormat.yyyyMMddHHmmss))+"'";
									if(standDate!=null&&finishDate.compareTo(standDate)>0){
										node+=" delay='true'";
									}else{
										node+=" delay='false'";
									}								
								}else{
									node+="' delay='false'";
								}
								if(str[0].equals("-1")&&i==list2.size()-1){
									node+=" complete='true'";
									msg += mr.getMessage(locale,"oa.lb.stop");
								}
								
								node+="/>";
							}
							//组织当前节点及审核人
							if(list2.size()>0){
								String[] str=(String[])list2.get(list2.size()-1);
								if(!str[0].equals("-1")){
									String person="";
									if(str[4].length()>0){
										String[] strs=str[4].split(";");
										for(int j=0;j<strs.length;j++){
											if(strs[j].length()>=0&&OnlineUserInfo.getUser(strs[j])!=null){
												person+=OnlineUserInfo.getUser(strs[j]).getName()+",";
											}
										}
									}
									node+="<node id='"+str[0]+"' state='0' doTime='' standardTime='' delay='' person='"+(person!=null?person.substring(0,person.length()-1):"")+"'/>";
								}
							}
								
							String lastNodeId="0";
							while(true){
								if(designBean.getFlowNodeMap().get(lastNodeId).getTo().equals("-1")){
									break;
								}
								lastNodeId=designBean.getFlowNodeMap().get(lastNodeId).getTo();
							}
							
							if(msg.length()==0&&BaseEnv.workFlowInfo.get(designId).getTemplateClass().equals("00001")){//进销存中如果显示数据是启用审核流前数据，列出此记录
								sql="select workFlowNode,createBy,createTime from "+tableName+" where id=?";
								pss = conn.prepareStatement(sql);
								pss.setString(1, keyId) ;
								rs = pss.executeQuery();
								if(rs.next()){
									String workFlowNode = rs.getString(1);
									String createBy = rs.getString(2);
									String createTime = rs.getString(3);
									if(workFlowNode.equals("-1")){
										msg+=mr.getMessage(locale,"OAMyWorkFlow.msg.noAuditInfo");
									}else if(workFlowNode.equals("0")){
										msg+=OnlineUserInfo.getUser(createBy).getName()+" "+createTime+" "+mr.getMessage(locale,"oa.lb.apply")+"\n";
									}
								}
							}
								
							String nodes="<nodes detail='"+msg+"'>";
							nodes+=node;
							nodes+="</nodes>";
							rst.setRetVal(nodes);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	} 
	
	public Result flowDepict(final String designId,final String keyId,final MessageResources mr,
			final Locale locale,final String tableName,final String applyBy){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) {
						WorkFlowDesignBean  designBean=BaseEnv.workFlowDesignBeans.get(designId);
						String sql = "select nodeID,checkPerson,startTime,endTime,oaTimeLimitUnit,benchmarkTime,nodeType,a.f_ref,b.deliverance," +
								"b.createBy,b.attTime,b.statusId from OAMyWorkFlowDet a left join tblAuditeDeliverance b on a.id=b.f_ref " +
								"where a.f_ref=(select id from OAMyWorkFlow where keyId=? and tableName=?) order by sortOrder" ;
						PreparedStatement pss;
						try {
							OAMyWorkFlowMgt mgt=new OAMyWorkFlowMgt();
							pss = conn.prepareStatement(sql);						
							pss.setString(1, keyId) ;
							pss.setString(2, tableName);
							ResultSet rs = pss.executeQuery();
							ArrayList<OAMyWorkFlowDetBean> list=new ArrayList<OAMyWorkFlowDetBean>();
							while(rs.next()){
								OAMyWorkFlowDetBean bean=new OAMyWorkFlowDetBean();
								bean.setNodeID(rs.getString("nodeID"));
								bean.setCheckPerson(rs.getString("checkPerson"));
								bean.setStartTime(rs.getString("startTime"));
								bean.setEndTime(rs.getString("endTime"));
								bean.setOaTimeLimitUnit(rs.getString("oaTimeLimitUnit"));
								bean.setBenchmarkTime(rs.getString("benchmarkTime"));
								bean.setNodeType(rs.getString("nodeType"));
								bean.setF_ref(rs.getString("f_ref"));
								bean.setApprovalOpinions(rs.getString("deliverance"));
								bean.setAttTime(rs.getString("attTime")) ;
								bean.setCreateBy(rs.getString("createBy")) ;
								bean.setStatusId(rs.getString("statusId")) ;
								list.add(bean);
							}
							
							String msg="";
							int count=0;
							boolean flag=true;
							for(int i=0;i<list.size();i++){
								OAMyWorkFlowDetBean bean=list.get(i);
								if(designBean.getFlowNodeMap().get(bean.getNodeID())==null){
									continue;
								}
								if(bean.getNodeID().equals("-1")){
									if(i>0){
										msg+=mr.getMessage(locale,"oa.lb.stop")+"\n\n";
									}
									count=0;
									flag=false;
								}else if(i>0&&!list.get(i).getNodeID().equals(list.get(i-1).getNodeID())){										
									if(!list.get(i).getNodeID().equals("0")){
										count++;
										msg+=mr.getMessage(locale,"oa.lb.step",count)+"："+designBean.getFlowNodeMap().get(bean.getNodeID()).getDisplay()+"\n";
									}
								}
								String deliverStr = "" ;
								if(bean.getNodeType().equals("cancel")){
									deliverStr+=OnlineUserInfo.getUser(bean.getCheckPerson()).getName()+" "+bean.getStartTime()+" "+mr.getMessage(locale,"oa.lb.cancel")+"\n";
								}else if(flag&&bean.getNodeID().equals("0")){
									if(applyBy.equals(bean.getCheckPerson())){
										deliverStr+=OnlineUserInfo.getUser(bean.getCheckPerson()).getName()+" "+bean.getStartTime()+" "+mr.getMessage(locale,"oa.lb.apply")+"\n";
									}
								}else if(bean.getNodeID().equals("-1")){
									deliverStr+=OnlineUserInfo.getUser(bean.getCheckPerson()).getName()+" "+bean.getStartTime()+" "+mr.getMessage(locale,"define.button.reverse")+"\n";
								}else{
									if(!list.get(i).getNodeID().equals("0")){
										deliverStr+=OnlineUserInfo.getUser(bean.getCheckPerson()).getName()+" "+bean.getStartTime()+" "+mr.getMessage(locale,"oa.lb.startTrans")+"\n";
									}
								}
								if(bean.getEndTime()!=null&&bean.getEndTime().length()>0){
									if(!flag&&list.get(i).getNodeID().equals("0")){
										deliverStr+=mr.getMessage(locale,"oa.apply.by")+" "+OnlineUserInfo.getUser(bean.getCheckPerson()).getName()+"  ";
									}else{
										deliverStr+= OnlineUserInfo.getUser(bean.getCheckPerson()).getName() + " ";
									}
									deliverStr+=bean.getEndTime();
									if(bean.getNodeType().equals("back")){
										deliverStr+=" "+mr.getMessage(locale,"oa.lb.back")+"\n";
									}else{
										deliverStr+=" "+mr.getMessage(locale,"oa.lb.deliverTo")+"\n";
									}
								}
								if(!msg.contains(deliverStr)){
									msg += deliverStr ;
								}else{
									if(bean.getApprovalOpinions()!=null&&bean.getApprovalOpinions().length()>0 
											&& bean.getCheckPerson().equals(bean.getCreateBy())){
										msg+=OnlineUserInfo.getUser(bean.getCreateBy()).getName()+" "+bean.getAttTime()+" 添加意见\n" ;
									}
								}
								if(bean.getApprovalOpinions()!=null&&bean.getApprovalOpinions().length()>0 
										&& bean.getCheckPerson().equals(bean.getCreateBy())){
									msg+=mr.getMessage(locale,"oa.lb.deliverance")+"："+bean.getApprovalOpinions()+"\n";
									msg+="\r";
								}else{
									if(bean.getCreateBy()!=null && bean.getApprovalOpinions()!=null && bean.getApprovalOpinions().length()>0){
										msg+=OnlineUserInfo.getUser(bean.getCreateBy()).getName()+" "+bean.getAttTime()+" 添加意见\n" ;
										msg+=mr.getMessage(locale,"oa.lb.deliverance")+"："+bean.getApprovalOpinions()+"\n";
										msg+="\r";
									}
								}
							}
							sql="select a.currentNode,b.nodeId,isnull(endTime,''),isnull((select top 1 endTime+';'+cast(isnull(oaTimeLimitUnit,0) as varchar(2))+';'+cast(isnull(benchMarkTime,0) as varchar(20)) from OAMyWorkFlowDet c where c.f_ref=a.id and c.nodeId!=b.nodeId and c.sortOrder<b.sortOrder and c.statusId=0 order by c.sortOrder desc),'')"+
							" lastEndTime,isnull(a.checkPerson,'') from OAMyWorkFlow a,OAMyWorkFlowDet b where a.id=b.f_ref and b.statusId=0 and b.sortOrder=(select max(sortOrder) from OAMyWorkFlowDet c where c.f_ref=b.f_ref and c.nodeID=b.nodeID) and a.keyId=? and a.tableName=?    order by sortOrder "; 
							pss = conn.prepareStatement(sql);
							pss.setString(1, keyId) ;
							pss.setString(2, tableName);
							rs = pss.executeQuery();
							ArrayList list2=new ArrayList();
							while(rs.next()){
								String[] str=new String[5];
								for(int i=0;i<str.length;i++){
									str[i] = rs.getString(i+1);
								}
								list2.add(str);
							}
							String node="";
							//组织所有已经办结的节点
							for(int i=0;i<list2.size();i++){
								String[] str=(String[])list2.get(i);
								//当前节点不存在或者明细节点=当前节点
								if(designBean.getFlowNodeMap().get(str[1])==null||(str[0].equals(str[1])&&str[4].length()>0)){
									continue;
								}
								node+="<node id='"+str[1]+"' state='";
								if(str[0].equals(str[1])){
									node+="0";//当前节点
								}else{
									node+="1";//完成节点
								}
								node+="' doTime='"+str[2]+"' standardTime='";
								if(str[3].length()>0){
									Date standDate=mgt.getStandDate(designBean.getFlowNodeMap().get(str[1]), str[3],0,0);	
									Date finishDate=str[2].length()==0?new Date():BaseDateFormat.parse(str[2], BaseDateFormat.yyyyMMddHHmmss);
									node+=(standDate==null?"":BaseDateFormat.format(standDate, BaseDateFormat.yyyyMMddHHmmss))+"'";
									if(standDate!=null&&finishDate.compareTo(standDate)>0){
										node+=" delay='true'";
									}else{
										node+=" delay='false'";
									}								
								}else{
									node+="' delay='false'";
								}
								if(str[0].equals("-1")&&i==list2.size()-1){
									node+=" complete='true'";
									msg+=mr.getMessage(locale,"oa.lb.stop");
								}
								
								node+="/>";
							}
							//组织当前节点及审核人
							if(list2.size()>0){
								String[] str=(String[])list2.get(list2.size()-1);
								if(!str[0].equals("-1")){
									String person="";
									if(str[4].length()>0){
										String[] strs=str[4].split(";");
										for(int j=0;j<strs.length;j++){
											if(strs[j].length()>=0&&OnlineUserInfo.getUser(strs[j])!=null){
												person+=OnlineUserInfo.getUser(strs[j]).getName()+",";
											}
										}
									}
									node+="<node id='"+str[0]+"' state='0' doTime='' standardTime='' delay='' person='"+(person!=null?person.substring(0,person.length()-1):"")+"'/>";
								}
							}
							
							String lastNodeId="0";
							while(true){
								if(designBean.getFlowNodeMap().get(lastNodeId).getTo().equals("-1")){
									break;
								}
								lastNodeId=designBean.getFlowNodeMap().get(lastNodeId).getTo();
							}
							
							if(msg.length()==0&&BaseEnv.workFlowInfo.get(designId).getTemplateClass().equals("00001")){//进销存中如果显示数据是启用审核流前数据，列出此记录
								sql="select workFlowNode,createBy,createTime from "+tableName+" where id=?";
								pss = conn.prepareStatement(sql);
								pss.setString(1, keyId) ;
								rs = pss.executeQuery();
								if(rs.next()){
									String workFlowNode = rs.getString(1);
									String createBy = rs.getString(2);
									String createTime = rs.getString(3);
									if(workFlowNode.equals("-1")){
										msg+=mr.getMessage(locale,"OAMyWorkFlow.msg.noAuditInfo");
									}else if(workFlowNode.equals("0")){
										msg+=OnlineUserInfo.getUser(createBy).getName()+" "+createTime+" "+mr.getMessage(locale,"oa.lb.apply")+"\n";
									}
								}
							}
							
							String nodes="<nodes detail='"+msg+"'>";
							nodes+=node;
							nodes+="</nodes>";
							rst.setRetVal(nodes);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst ;
	} 
	public void hurryTrans(final String applyBy,final String applyType,final String checkPerson,final String billId,final String nextNodeIds,final String currNodeId
			,final String oaTimeLimit,final String oaTimeLimitUnit,final Locale locale,final String operType,final String title,final String wakeTypes
			,final String wakeScope,final String lastNodeId,final LoginBean lg,final String appendWake,final MessageResources mr){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) {
						hurryTrans( applyBy, applyType, checkPerson, billId, nextNodeIds, currNodeId
								, oaTimeLimit, oaTimeLimitUnit, locale, operType, title, wakeTypes
								, wakeScope, lastNodeId, lg, appendWake, mr, conn);
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
	}
	
	public void hurryTrans(String applyBy,String applyType,String checkPerson,String billId,String nextNodeIds,String currNodeId
			,String oaTimeLimit,String oaTimeLimitUnit,Locale locale,String operType,String title,String wakeTypes
			,String wakeScope,String lastNodeId,LoginBean lg,String appendWake,MessageResources mr,Connection conn){
		
		//所有消息都不要提醒自己
		if(lg != null){
			checkPerson =checkPerson.replaceAll(";"+lg.getId(), "");
		}
		if(checkPerson.length() ==0){
			return;
		}
		
		String modName = BaseEnv.workFlowInfo.get(applyType).getTemplateName();
		String msg="";
		String strType = "" ;
		int oper=7;
		FlowNodeBean bean=BaseEnv.workFlowDesignBeans.get(applyType).getFlowNodeMap().get(lastNodeId);
		if(wakeScope.equals("next")){
			msg=(operType.equals("back")?lg.getEmpFullName()+mr.getMessage(locale,"oa.workflow.awoke.msg1")+modName+":"+title:applyBy+mr.getMessage(locale,"oa.workflow.awoke.msg2")+modName+":"+title+mr.getMessage(locale,"oa.workflow.awoke.msg3")+"。")+" "+appendWake;
			strType = operType.equals("back")?"approve":"notApprove" ;
		}else if(wakeScope.equals("start")){
			msg=(mr.getMessage(locale,"oa.workflow.awoke.msg4")+modName+":"+title+" \""+bean.getDisplay()+"\"" +(operType.equals("back")?mr.getMessage(locale,"oa.workflow.awoke.hasBack"):mr.getMessage(locale,"oa.workflow.awoke.hasCheck")))+"。"+appendWake;
			oper=5;
			strType = "approve" ;
		}else if(wakeScope.equals("hurryTrans")){
			msg=mr.getMessage(locale,"oa.workflow.hurryTrans")+"！！！"+title;
			strType = "approve" ;
		}else if("retAuditing".equals(wakeScope)){
			msg = mr.getMessage(locale,"oa.workflow.awoke.msg4") + modName + ":" +title + "\" 已经反审核了。" + appendWake;
			strType = "approve" ;
		}else if("noteTime".equals(wakeScope)){
			msg = "即将超时！！！有"+modName+title+"需要审核,请查看";
			strType = "approve" ;
		}else if("noteRate".equals(wakeScope)){
			msg = "已经超时！！！有"+modName+title+"需要审核,请查看";
			strType = "approve" ;
		} else{
			if(lastNodeId.equals("0")){
				msg=applyBy+mr.getMessage(locale,"oa.workflow.awoke.msg2")+modName+":"+title+" "+mr.getMessage(locale,"oa.workflow.awoke.hasDeliver")+"。"+appendWake;
			}else{
				msg=(applyBy+mr.getMessage(locale,"oa.workflow.awoke.msg2")+modName+":"+title+" \""+bean.getDisplay()+"\"" +(operType.equals("back")?mr.getMessage(locale,"oa.workflow.awoke.hasBack"):mr.getMessage(locale,"oa.workflow.awoke.hasCheck")))+"。"+appendWake;
			}
			oper=5;
			strType = "approve" ;
		} 
		msg = "[consign_msg]" + msg;
		String link="";
		
		DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(BaseEnv.workFlowInfo.get(applyType).getTemplateFile()) ;
		MOperation mo = lg==null?null : (MOperation) lg.getOperationMap().get("/CRMBrotherAction.do?tableName="+tableInfo.getTableName());
		if(mo!=null && mo.query){
			link="<a href=\"javascript:mdiwin('/CRMBrotherAction.do?operation=5&type=deatilNew&keyId="+billId+"&tableName="+tableInfo.getTableName()+"','"+tableInfo.getDisplay().get(locale.toString())+"')\">"+msg+"</a> ";
		}else if(BaseEnv.workFlowInfo.get(applyType) !=null && BaseEnv.workFlowInfo.get(applyType).getTemplateFile().indexOf("CRMClientInfo")>-1){
			link="<a href=\"javascript:mdiwin('/CRMClientAction.do?operation=5&type=detailNew&keyId="+billId+"','客户详情')\">"+msg+"</a> ";
		}else if(BaseEnv.workFlowInfo.get(applyType).getTemplateType() == 1){
			String parentTableName = "" ;
			if(tableInfo!=null){
				parentTableName = tableInfo.getPerantTableName() ;
				if(parentTableName!=null&&parentTableName.endsWith(";")){
					parentTableName = parentTableName.substring(0,parentTableName.length()-1) ;
				}
				parentTableName = parentTableName==null?"":parentTableName;
			}
			String tableName=BaseEnv.workFlowInfo.get(applyType).getTemplateFile();
			/*判断是否是个性化表单设计产生的表单*/
			if(!tableName.startsWith("Flow_")){ 
				//查询该表的Id,查moduleType等，因moduleType,f_brother字段可能不存在，所以要做判断。
				String moduleType="";
				String f_brother="";
				String parentCode="";
				String tsql = "select ''";
				DBTableInfoBean dti =(DBTableInfoBean)BaseEnv.tableInfos.get(tableName);
				boolean iff_brother=false;
				boolean ifmoduleType=false;
				boolean if_classCode=false;
				for(DBFieldInfoBean dfi :dti.getFieldInfos()){
					if(dfi.getFieldName().equals("moduleType")){
						ifmoduleType = true;
						tsql += ",moduleType";
					}
					if(dfi.getFieldName().equals("f_brother")){
						iff_brother = true;
						tsql += ",f_brother";
					}
					if(dfi.getFieldName().equals("classCode")){
						if_classCode = true;
						tsql += ",classCode";
					}
				}
				
				tsql += " from "+dti.getTableName() +" where id='"+billId+"'";
				try{
					PreparedStatement tpss = conn.prepareStatement(tsql);											
					ResultSet trs=tpss.executeQuery();
					if(trs.next()){
						if(if_classCode){
							parentCode = trs.getString("classCode");
							if(parentCode != null && parentCode.length() >0){
								parentCode = parentCode.substring(0,parentCode.length() -5);
							}
							if(parentCode == null){
								parentCode = "";
							}
						}
						if(iff_brother){
							f_brother = trs.getString("f_brother");
						}
						if(ifmoduleType){
							moduleType = trs.getString("moduleType");
						}
					}
					if("0".equals(moduleType)){moduleType="";}
				}catch(Exception e){e.printStackTrace();}
				link="<a href=\"javascript:mdiwin('/UserFunctionAction.do?parentTableName="+parentTableName+"&tableName="+tableName
				+"&nextNodeIds="+nextNodeIds+"&currNodeId="+currNodeId+"&keyId="+billId+"&noback=true&operation="+oper+"&designId="+applyType+
				"&oaTimeLimit="+oaTimeLimit+"&oaTimeLimitUnit="+oaTimeLimitUnit+"&moduleType="+moduleType+"&f_brother="+f_brother+"&parentCode="+parentCode+"','"+tableInfo.getDisplay().get(locale.toString())+"')\">"+msg+"</a>";
			}else{
				link="<a href=\"javascript:mdiwin('/OAWorkFlowAction.do?parentTableName="+parentTableName+"&fromAdvice=true&tableName="+tableName
				+"&nextNodeIds="+nextNodeIds+"&currNodeId="+currNodeId+"&keyId="+billId+"&noback=true&operation="+oper+"&designId="+applyType+"&oaTimeLimit="+oaTimeLimit+"&oaTimeLimitUnit="+oaTimeLimitUnit+"','"+tableInfo.getDisplay().get(locale.toString())+"')\">"+msg+"</a>";
			}
		}
		if(wakeTypes.length()>0){
			//查询委托人
			HashMap<String, String> consignmap = queryConsignationByUserId(checkPerson.replaceAll(";", "','"), applyType,conn);
	        String[] wakeType = wakeTypes.split(",") ;
	        for(String type : wakeType){
	        	new NotifyFashion("1", msg.replace("[consign_msg]", ""), link.replace("[consign_msg]", ""), checkPerson.replaceAll(";", ","),Integer.parseInt(type) ,"workflow",billId,"","",strType).start() ;
	        }
	        for(String person : checkPerson.split(";")){
	        	OnlineUser online = OnlineUserInfo.getUser(person);//委托人
	        	if(consignmap.get(person)!=null && online!=null){
	        		msg = msg.replace("[consign_msg]", online.name+"-委托您：");
	        	    link = link.replace("[consign_msg]", online.name+"-委托您：");
	        		for(String type : wakeType){
	    	        	new NotifyFashion("1", msg, link, consignmap.get(person),Integer.parseInt(type) ,"workflow",billId,"","",strType).start() ;
	    	        }
	        	}
	        }
		}
	}

	/**
	 * 查询我的工作流
	 */
	public Result query(final OAMyWorkFlowForm flowForm,final String userId,final String approveStatus,final String local,final boolean myApprove) {
		Result rst = new Result();
		try{
			String sql = "select distinct a.id,a.keyId,t.designType,applyDate,applyBy,department,applyType,templateName,a.tableName,applyContent,currentNode,nextNodeIds,a.checkPerson,departmentCode,a.createBy,substring(a.createTime,12,5) as createTime,flowDepict,lastNodeId,lastCheckPerson "
			    + ",substring(a.lastUpdateTime,0,11) as lastUpdateTime,substring(a.lastUpdateTime,12,5) as times,a.flowDepictTitle,a.allApprovePerson,t.parentTableName,a.lastUpdateTime oldLastUpdateTime"
			    + " from OAMyWorkFlow a join OAWorkFlowTemplate t  on t.id=a.applyType" +
			    "  join OAMyWorkFlowPerson p on p.keyId=a.keyId and p.tableName=a.tableName  "  
			    + " where t.templateStatus=1 and a.statusId=0 ";	
			/*当前结点是否是需要是我审的*/
			if("consignation".equals(approveStatus)){ //委托
				sql += " and exists(select id from OAWorkConsignation where state=1 and CongignUserID='"+userId+"' and CHARINDEX(';'+userId+';',a.checkPerson)>0 and (CHARINDEX((select sameFlow+';' from OAWorkFlowTemplate where id=a.applyType),flowName)>0 or len(ISNULL(flowName,''))=0)" 
					+  " and substring(convert(varchar,GETDATE(),120),0,11)>=beginTime " 
					+  " and substring(convert(varchar,GETDATE(),120),0,11)<=endTime"
					+  " )";
			}else{
				if(myApprove){ //待审
					sql += " and ((a.createBy='"+userId+"' and a.currentNode='0') or (p.curApprover=1  and p.checkPerson='"+userId+"'  )) " ;
				}else{ //所有非待审条件都不包含当前我审的单据
					sql += " and (p.curApprover=0  and p.checkPerson='"+userId+"' ) " ;
				}
				if("self".equals(flowForm.getFlowBelong())){/*我发起的*/
					sql += " and (a.createBy='"+userId+"') ";
				}else if("handle".equals(flowForm.getFlowBelong())){/*我经办的*/
					sql += " and ( p.checkPerson='"+userId+"' ) ";
				}else{
					if(!myApprove){
						sql += " and ( p.hadApprover=1  and p.checkPerson='"+userId+"' ) ";
					}
				}
				if(!"".equals(approveStatus)){
					if("finish".equals(approveStatus)){
						sql += " and a.currentNode='-1'";
					}else{
						sql += " and a.currentNode!='-1'";
					}
				}
			}
			
			if(flowForm.getClassCode()!=null&&!"".equals(flowForm.getClassCode())){
				sql+=" and t.templateClass='"+flowForm.getClassCode()+"'";
			}
			if (flowForm.getBeginTime() != null && !"".equals(flowForm.getBeginTime())) {
				sql += " and a.applyDate>='" + flowForm.getBeginTime() + "'";
			}
			if (flowForm.getEndTime() != null && !"".equals(flowForm.getEndTime())) {
				sql += " and a.applyDate<='" + flowForm.getEndTime()+ " 23:59:59'";
			}
			if (flowForm.getEbeginTime() != null && !"".equals(flowForm.getEbeginTime())) {
				sql += " and a.lastUpdateTime>='" + flowForm.getEbeginTime() + "'";
			}
			if (flowForm.getEendTime() != null && !"".equals(flowForm.getEendTime())) {
				sql += " and a.lastUpdateTime<='" + flowForm.getEendTime()+ " 23:59:59'";
			}
			if (flowForm.getEmpFullName() != null && !"".equals(flowForm.getEmpFullName())) {
				sql += " and a.applyBy like '%"+ flowForm.getEmpFullName() + "%'";
			}
			if (flowForm.getDeptFullName() != null && !"".equals(flowForm.getDeptFullName())) {
				sql += " and a.department like '%"+ flowForm.getDeptFullName() + "%'";
			}
			if (flowForm.getTableFullName() != null && !"".equals(flowForm.getTableFullName())){
				sql += " and t.templateName like '%"+ flowForm.getTableFullName() +"%'" ;
			}
			if (flowForm.getApplyContent() != null && flowForm.getApplyContent().length()>0){
				sql += " and a.applyContent like '%"+flowForm.getApplyContent().trim()+"%'" ;
			}
			if (flowForm.getKeyWord() != null && flowForm.getKeyWord().length()>0){
				sql += "and (a.applyContent like '%"+flowForm.getKeyWord()+"%' or  a.applyBy like '%"+flowForm.getKeyWord()+"%' or a.department like '%"+flowForm.getKeyWord()+"%' or t.templateName like '%"+ flowForm.getKeyWord()+"%')" ;
			}
			sql = " select k.*,ROW_NUMBER() over(order by (case charindex(';"+userId+";',k.checkPerson) when 0 then 0 else 1 end) desc, k.oldLastUpdateTime desc) as row_id from ( "+sql+" ) k";
			rst = sqlListMaps(sql, new ArrayList(), flowForm.getPageNo(), flowForm.getPageSize());
			ArrayList<MyWorkFlow> flowList = new ArrayList<MyWorkFlow>();
			if(rst.retCode == ErrorCanst.DEFAULT_SUCCESS){
				ArrayList mapList = (ArrayList) rst.retVal;
				for(int i=0;i<mapList.size();i++){
					HashMap map = (HashMap) mapList.get(i);
					MyWorkFlow workFlow = new MyWorkFlow();
					workFlow.setId(String.valueOf(map.get("id")));
					workFlow.setBillId(String.valueOf(map.get("keyId")));
					workFlow.setApplyDate(String.valueOf(map.get("applyDate")));
					workFlow.setApplyBy(String.valueOf(map.get("applyBy")));
					workFlow.setTemplateName(String.valueOf(map.get("templateName")));
					workFlow.setDepartment(String.valueOf(map.get("department")));
					workFlow.setApplyType(String.valueOf(map.get("applyType")));
					workFlow.setApplyContent(String.valueOf(map.get("applyContent")));
					workFlow.setTableName(String.valueOf(map.get("tableName")));
					workFlow.setCheckPerson(String.valueOf(map.get("checkPerson"))) ;
					workFlow.setCurrentNode(String.valueOf(map.get("currentNode"))); 
					workFlow.setNextNodeIds(String.valueOf(map.get("nextNodeIds")));
					
					workFlow.setParentTableName(String.valueOf(map.get("parentTableName")));
					
					if("consignation".equals(approveStatus)){
						workFlow.setCheckPerson(";"+userId+";");
					}else{
						workFlow.setCheckPerson(String.valueOf(map.get("checkPerson")));
					}
					workFlow.setDepartmentCode(String.valueOf(map.get("departmentCode")));
					workFlow.setWorkFlowType(String.valueOf(map.get("designType")));
					workFlow.setCreateBy(String.valueOf(map.get("createBy")));
					workFlow.setCreateTime(String.valueOf(map.get("createTime")));
					workFlow.setFlowDepict(String.valueOf(map.get("flowDepict")));
					WorkFlowDesignBean flowDesign = BaseEnv.workFlowDesignBeans.get(workFlow.getApplyType()) ;
					if(flowDesign!=null){
						String lastCheckPerson = String.valueOf(map.get("lastCheckPerson"));
						String lastNodeId = String.valueOf(map.get("lastNodeId"));
						String allApprovePerson = String.valueOf(map.get("allApprovePerson"));
						if(lastCheckPerson!=null&&lastCheckPerson.length()>0){
							FlowNodeBean flowNode = flowDesign.getFlowNodeMap().get(lastNodeId);
							
							if(flowNode!=null){
								workFlow.setLastNodeName(flowNode.getDisplay());
								OnlineUser user = OnlineUserInfo.getUser(lastCheckPerson);
								if(user!=null){
									workFlow.setLastNodeCheckperson(user.getName());
								}
							}
						}
						if(workFlow.getCurrentNode()!=null){
							FlowNodeBean flowNode = flowDesign.getFlowNodeMap().get(workFlow.getCurrentNode()) ;
							
							//设置lastCheckPerson用于显示撤回按扭，由于启用全审未审完时当前结点还显示的上上一节点所以加上条件allApprovePerson
							if(flowNode!=null && flowNode.isAllowCancel() && (allApprovePerson== null || allApprovePerson.length()==0)){
								//当前结点允许撤回，则置setLastCheckPerson
								workFlow.setLastCheckPerson(lastCheckPerson);
								workFlow.setLastNodeId(lastNodeId);
							}
							
							if(flowNode!=null && flowNode.getApprovers()!=null){
								workFlow.setCurrNodeName(flowNode.getDisplay());
								String[] apprList = workFlow.getCheckPerson().split(";");
								String checkNames = "" ;
								for(String aapr : apprList){
									OnlineUser user = OnlineUserInfo.getUser(aapr);
									if(user!=null){
										checkNames+=user.getName()+",";
									}
									
								}
								if(checkNames.endsWith(",")){
									checkNames = checkNames.substring(0, checkNames.length()-1) ;
								}
								workFlow.setCheckPersonName(checkNames) ;
							}
						}
					}
					workFlow.setLastUpdateTime(String.valueOf(map.get("lastUpdateTime")));
					workFlow.setFinishTime(String.valueOf(map.get("times")));
					workFlow.setFlowDepictTitle(String.valueOf(map.get("flowDepictTitle")));
					flowList.add(workFlow);
				}
				rst.setRetVal(flowList);
				BaseEnv.log.debug(sql);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}	
		return rst;
	}
	
	
	/**
	 * 工作流监控
	 */
	public Result queryAll(final OAMyWorkFlowForm flowForm,final LoginBean login,final String type,final String local) {
		Result rst = new Result();
		String userId=login.getId();
		String dept=login.getDepartCode();
		String deptTemp=dept;
		String sql = "select a.id,a.keyId,t.designType,applyDate,applyBy,department,applyType,a.tableName,applyContent,currentNode,nextNodeIds,a.checkPerson,departmentCode,a.createBy,a.createTime,a.flowDepict ";
				sql += ",a.lastUpdateTime,ROW_NUMBER() over(order by a.applyDate desc,a.createTime desc) as row_id";
				sql += " from OAMyWorkFlow a  join OAWorkFlowTemplate t on t.id=a.applyType ";
						//" join OAMyWorkFlowPerson p on p.keyId=a.keyId and p.tableName=a.tableName  ";
		if("all".equals(type)){
			sql += "where a.statusId <>-1";
		}else{
			sql += "where a.statusId=-1 ";
		}	
		
		if(flowForm.getApproveStatus()!=null&&!"".equals(flowForm.getApproveStatus())){
			if("finish".equals(flowForm.getApproveStatus())){
				sql += " and a.currentNode='-1'";
			}else{
				sql += " and a.currentNode!='-1'";
			}
		}
		
		/*
		if("self".equals(flowForm.getFlowBelong())){
			sql+=" and (a.createBy in ('"+userId+"'))";
		}else if("handle".equals(flowForm.getFlowBelong())){
			sql+=" and p.checkPerson='"+userId+"' ";
		}else if("relation".equals(flowForm.getFlowBelong())){
			sql+=" and (a.createBy in('"+userId+"') or p.checkPerson='"+userId+"' )";
		}else{	 */
			if(!userId.equals("1")){
				//工作流监控，以监控权限为主，和工作流是否经我的手无关
				//sql+=" and (a.createBy in('"+userId+"') or p.checkPerson='"+userId+"' or";
				//显示我能监控的工作流
				sql+=" and (( " ;
					sql+="(charIndex('"+dept+",',depMonitor)>0"; //部分监控开始
					while(deptTemp.length()>5){
						deptTemp=deptTemp.substring(0,deptTemp.length()-5);
						sql+=" or charIndex('"+deptTemp+",',depMonitor)>0";
					}
					sql+=") " ; //部门监控结束
				sql+=	" or charIndex(',"+userId+",',','+perMonitor)>0 ) " +
						" and (case perMonitorScope when 0 then '"+dept+"' else a.departmentCode end ) like '"+dept+"%')";
				//sql+=")";
			}
		//}
		
		if (flowForm.getBeginTime() != null && !"".equals(flowForm.getBeginTime())) {
			sql += " and a.applyDate>='" + flowForm.getBeginTime() + "'";
		}
		if (flowForm.getEndTime() != null && !"".equals(flowForm.getEndTime())) {
			sql += " and a.applyDate<='" + flowForm.getEndTime()+ " 23:59:59'";
		}
		if (flowForm.getEbeginTime() != null && !"".equals(flowForm.getEbeginTime())) {
			sql += " and a.lastUpdateTime>='" + flowForm.getEbeginTime() + "'";
		}
		if (flowForm.getEendTime() != null && !"".equals(flowForm.getEendTime())) {
			sql += " and a.lastUpdateTime<='" + flowForm.getEendTime()+ " 23:59:59'";
		}
		if (flowForm.getEmpFullName() != null && !"".equals(flowForm.getEmpFullName())) {
			sql += " and a.applyBy like '%"+ flowForm.getEmpFullName() + "%'";
		}
		if (flowForm.getDeptFullName() != null && !"".equals(flowForm.getDeptFullName())) {
			sql += " and a.department like '%"+ flowForm.getDeptFullName() + "%'";
		}
		if (flowForm.getTableFullName() != null && !"".equals(flowForm.getTableFullName())){
			sql += " and t.templateName like '%"+ flowForm.getTableFullName() +"%'" ;
		}
		if (flowForm.getApplyContent() != null && flowForm.getApplyContent().length()>0){
			sql += " and a.applyContent like '%"+flowForm.getApplyContent()+"%'" ;
		}
		BaseEnv.log.debug("OAMyWorkFlowMgt.queryAll --"+sql);
		rst = sqlListMaps(sql, new ArrayList(), flowForm.getPageNo(), flowForm.getPageSize());
		ArrayList<MyWorkFlow> flowList = new ArrayList<MyWorkFlow>();
		if(rst.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList mapList = (ArrayList) rst.retVal;
			for(int i=0;i<mapList.size();i++){
				HashMap map = (HashMap) mapList.get(i);
				MyWorkFlow workFlow = new MyWorkFlow();
				workFlow.setId(String.valueOf(map.get("id")));
				workFlow.setBillId(String.valueOf(map.get("keyId")));
				workFlow.setApplyDate(String.valueOf(map.get("applyDate")));
				workFlow.setApplyBy(String.valueOf(map.get("applyBy")));
				workFlow.setDepartment(String.valueOf(map.get("department")));
				workFlow.setApplyType(String.valueOf(map.get("applyType")));
				workFlow.setApplyContent(String.valueOf(map.get("applyContent")));
				workFlow.setTableName(String.valueOf(map.get("tableName")));
				workFlow.setCheckPerson(String.valueOf(map.get("checkPerson"))) ;
				workFlow.setCurrentNode(String.valueOf(map.get("currentNode"))); 
				workFlow.setNextNodeIds(String.valueOf(map.get("nextNodeIds")));
				workFlow.setCheckPerson(String.valueOf(map.get("checkPerson")));
				workFlow.setDepartmentCode(String.valueOf(map.get("departmentCode")));
				workFlow.setWorkFlowType(String.valueOf(map.get("designType")));
				workFlow.setCreateBy(String.valueOf(map.get("createBy")));
				workFlow.setCreateTime(String.valueOf(map.get("createTime")));
				workFlow.setFlowDepict(String.valueOf(map.get("flowDepict")));
				workFlow.setLastUpdateTime(String.valueOf(map.get("lastUpdateTime")));
				flowList.add(workFlow);		
			}
		}
		rst.setRetVal(flowList);
		return rst;
	}
	
	public void tranEnd(String applyBy,String applyType,String applyById,String currNode,String keyId,Locale locale,String title,String wakeTypes,LoginBean lg,Connection conn){
		//所有消息都不要提醒自己
		applyById =applyById.replaceAll(";"+lg.getId(), "");
		if(applyById.length() ==0){
			return;
		}
		if(wakeTypes.length()>0){
			String modName = BaseEnv.workFlowInfo.get(applyType).getTemplateName();
			String msg=applyBy+"提交的"+modName+":"+title+"已审核完毕";
			String link="";
			OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(applyType);
			
			
			String tableName = new DynDBManager().getInsertTableName(template.getTemplateFile());//过滤表名
			String linkAddr = linkAddrQueryByTableName(tableName,conn);//获取linkAddr
			
			if("CRMClientInfo".equals(tableName)){
				//返回客户详情
				link="<a href=\"javascript:mdiwin('/CRMClientAction.do?operation=5&type=detailNew&keyId="+keyId+"','客户详情')\">"+msg+"</a> ";
			}else if(linkAddr.indexOf("/CRMBrotherAction")>-1){
				//表示用了新的客户邻居表路径
				DBTableInfoBean tableBean = GlobalsTool.getTableInfoBean(tableName);
				link="<a href=\"javascript:mdiwin('/CRMBrotherAction.do?tableName="+tableName+"&operation=5&keyId="+keyId+"','"+tableBean.getDisplay().get(locale.toString())+"详情')\">"+msg+"</a> ";
			}else if(template.getTemplateType() == 1){
				if("1".equals(template.getDesignType())){
					link="<a href=\"javascript:mdiwin('/OAWorkFlowAction.do?tableName="+template.getTemplateFile()+"&keyId="+keyId+"&noback=true&operation=5','"+template.getTemplateName()+"')\">"+msg+"</a>";
				}else{
					String parentTableName = "" ;
					DBTableInfoBean tableInfo = GlobalsTool.getTableInfoBean(template.getTemplateFile()) ;
					if(tableInfo!=null){
						parentTableName = tableInfo.getPerantTableName() ;
						if(parentTableName ==null){
							parentTableName = "";
						}
						if(parentTableName != null && parentTableName.endsWith(";")){
							parentTableName = parentTableName.substring(0,parentTableName.length()-1) ;
						}
					}
					link="<a href=\"javascript:mdiwin('/UserFunctionAction.do?parentTableName="+parentTableName+"&tableName="+BaseEnv.workFlowInfo.get(applyType).getTemplateFile()+"&currNodeId=-1&keyId="+keyId+"&noback=true&operation=5&designId="+applyType+"&approveStatus=finish','"+tableInfo.getDisplay().get(locale.toString())+"')\">"+msg+"</a>";
				}
			}
			
	        String[] wakeType = wakeTypes.split(",") ;
	        for(String type : wakeType){
	        	//new Thread(new NotifyFashion("1", msg, link, applyById.replaceAll(";", ","),Integer.parseInt(type) ,"workflow",keyId)).start() ;
	        	new Thread(new NotifyFashion("1", msg, link, applyById.replaceAll(";", ","),Integer.parseInt(type) ,"workflow",keyId,"","","approve")).start() ;
	        }
		}
	}
	
	
	
	/**
	 * 得到工作流的审核信息
	 * @param billId
	 * @return
	 */
	public Result getOAWFCheckInfo(final String billId){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){    
					public void execute(Connection conn) {
						String sql ="select isnull(b.nodeID,''),isnull(b.checkPerson,''),isnull(b.endTime,''),isnull(b.approvalOpinions,''),isnull(AutographPic,'') from oamyworkflow a left join  oamyworkflowDet b "+
									" on a.id=b.f_ref left join tblAutograph e on b.checkPerson=e.userName   "+
									" where a.keyId='"+billId+"' and isnull(b.endTime,'')<>'' and  b.statusId=0 order by sortOrder" ;
						PreparedStatement pss;
						try {
							pss = conn.prepareStatement(sql);
						
							ResultSet rs=pss.executeQuery();
							ArrayList list=new ArrayList();
							while(rs.next()){
								list.add(new String[]{rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)});
							}
							HashMap map=new HashMap();
							for(int i=0;i<list.size();i++){
								String [] info=(String[])list.get(i);
								
								ArrayList li;
								if(map.get(info[0])==null){
									li=new ArrayList();
								}else{
									li=(ArrayList)map.get(info[0]);
								}
								li.add(info);
								map.put(info[0], li);
							}
							
							rst.setRetVal(map);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							BaseEnv.log.debug("错误："+e.getMessage());
							e.printStackTrace();
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst;
	}
	public HashMap getOAMyWorkFlowInfo(String id){
		String sql="select a.createBy,applyType,a.checkPerson,nextNodeIds,currentNode,b.oaTimeLimitUnit,b.benchmarkTime,a.applyContent,a.departmentCode,a.tableName,a.keyId from OAMyWorkFlow a "+
			" left join OAMyWorkFlowDet b on a.id=b.f_ref where a.id='"+id+"'";
		AIODBManager aioMgt=new AIODBManager();
		Result rs=aioMgt.sqlListMap(sql, new ArrayList(), 0, 0);
		if(rs.retVal!=null&&((ArrayList)rs.retVal).size()>0){
			return (HashMap)((ArrayList)rs.retVal).get(0);
		}
		return null;
	}
	
	/**
	 * 获取当前单据相关的流程信息
	 * @param keyId
	 * @param tableName
	 * @return
	 */
	public HashMap getOAMyWorkFlowInfo(String keyId,String tableName){
		String sql="select a.createBy,applyType,a.checkPerson,nextNodeIds,currentNode,b.oaTimeLimitUnit,b.benchmarkTime,"
				+ "a.applyContent,a.departmentCode,a.tableName,a.keyId,a.id,a.lastNodeId,a.lastCheckPerson,a.applyContent,"
				+ "display as currentNodeName from OAMyWorkFlow a  "
				+ "left join OAMyWorkFlowDet b on a.id=b.f_ref "
				+ "left join OAWorkFlowNode c on c.flowId=a.applyType and c.keyId=a.currentNode  where a.keyId='"+keyId+"'";
		if(tableName!=null && tableName.length()>0){
			sql += " and  a.tableName='"+tableName+"'" ;
		}
		BaseEnv.log.debug("OAMyWorkFLowMgt.getOAMyWorkFlowInfo 取流程详情 sql="+sql);
		AIODBManager aioMgt=new AIODBManager();
		Result rs=aioMgt.sqlListMap(sql, new ArrayList(), 0, 0);
		if(rs.retVal!=null&&((ArrayList)rs.retVal).size()>0){
			return (HashMap)((ArrayList)rs.retVal).get(0);
		}
		return null;
	}
	
	/**
	 * 得到工作流的审核信息
	 * @param billId
	 * @return
	 */
	public Result getNextNodeIdByVal(final String designId,final String currNodeId,final String keyId,final boolean allNode,final LoginBean loginBean){
		Result resVal=new DynDBManager().detail(BaseEnv.workFlowInfo.get(designId).getTemplateFile(), BaseEnv.tableInfos, keyId, loginBean.getSunCompany(), BaseEnv.propMap, loginBean.getId(), true, "");
		if(resVal.retCode!=ErrorCanst.DEFAULT_SUCCESS){
			return resVal;
		}
		final HashMap values=(HashMap)resVal.retVal;
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) {
						try{
							rst.setRetVal(getNextNodeIdByVal(designId, currNodeId, keyId,conn,allNode,loginBean,values));
						} catch (Exception e) {
							BaseEnv.log.error("错误：",e);
							rst.retCode = ErrorCanst.DEFAULT_FAILURE;
							rst.retVal = e.getMessage();
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst;
	}
	
	/**
	 * 得到工作流的审核信息
	 * @param billId
	 * @return
	 */
	public Result getWorkFlowInfo(final String keyId){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) {
						try{
							String sql = "select department,applyType,currentNode from OAMyWorkFlow where id=?" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, keyId) ;
							ResultSet rss = pss.executeQuery() ;
							if(rss.next()){
								String[] flow = new String[3] ;
								flow[0] = rss.getString("department") ;
								flow[1] = rss.getString("applyType") ;
								flow[2] = rss.getString("currentNode") ;
								rst.setRetVal(flow) ;
							}
						} catch (Exception e) {
							BaseEnv.log.error("OAMyWorkFlowMgt getWorkFlowInfo method" ,e);
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode) ;
		return rst;
	}
	
	/**
	 * 得到工作流的审核信息
	 * @param billId
	 * @return
	 */
	public String getDesignId(String keyId,String tableName,Connection conn){
		String designId = "";
		try{
			String sql = "select applyType from OAMyWorkFlow where keyId=?" ;
			if(tableName!=null){
				sql += " and tableName='"+tableName+"'";
			}
			PreparedStatement pss = conn.prepareStatement(sql) ;
			pss.setString(1, keyId) ;
			ResultSet rss = pss.executeQuery() ;
			if(rss.next()){
				designId = rss.getString("applyType");
			}
		} catch (Exception e) {
			BaseEnv.log.error("OAMyWorkFlowMgt getWorkFlowInfo method" ,e);
			e.printStackTrace();
			return null;
		}
		return designId;
	}
	
	public Object[] getTableWorkFlowName(String tableName,String keyId){
		String sql="select workFlowNode,workFlowNodeName from "+tableName+" where id='"+keyId+"'";
		AIODBManager aioMgt=new AIODBManager();
		Result rs=aioMgt.sqlList(sql, new ArrayList());
		if(rs.retVal!=null&&((ArrayList)rs.retVal).size()>0){
			return ((Object[])((ArrayList)rs.retVal).get(0));
		}
		return new Object[]{"",""};
	}
	
	//工作流撤回时，删除上个节点转交的通知消息
	public Result queryDeleteAdvice(final String id,final String currNode){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						try {
							String currNodeStr="currNodeId="+currNode;
							String sql="select id from tbladvice where relationId=? AND CHARINDEX(?,content)>0 ";
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1,id) ;
							pss.setString(2,currNodeStr) ;
							ResultSet rss = pss.executeQuery() ;
							String ids = "";
							while (rss.next()){
								ids += rss.getString("id") + ",";
							}
							rst.setRetVal(ids);
						} catch (Exception e) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							e.printStackTrace();
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode);
		return rst ;
	}
	
	
	/**
	 * 工作流死结点，重置到开始结点
	 * @param id
	 * @param currNode
	 * @return
	 */
	public Result resetBegin(final String keyIds[],final LoginBean lg){
		final Result rst = new Result() ;
		int retCode = DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException {
						try {
							for(String keyId : keyIds){
								String sql = "select tableName,createBy,flowDepict,applyType from OAMyWorkFlow where id=?";
								PreparedStatement pss = conn.prepareStatement(sql) ;
								pss.setString(1, keyId);
								ResultSet rss = pss.executeQuery();
								if(rss.next()){
									String tableName = rss.getString("tableName");
									String createBy  = rss.getString("createBy");
									String flowDepict= rss.getString("flowDepict");
									String lastDesignId = rss.getString("applyType");
									
									sql = "select id as lastDesignId from OAWorkFlowTemplate where templateFile=? order by convert(int,isnull(version,0)) desc";
									pss = conn.prepareStatement(sql);
									pss.setString(1, tableName);
									rss = pss.executeQuery();
									if(rss.next()){
										lastDesignId = rss.getString("lastDesignId");
									}
									
									if(flowDepict!=null && flowDepict.indexOf("</font>")>0){
										flowDepict = flowDepict.substring(0,flowDepict.indexOf("</font>")+7);
									}
									sql = "update OAMyWorkFlow set checkPerson=?,currentNode='0',flowDepict=?,applyType=? where id=?";
									pss = conn.prepareStatement(sql);
									pss.setString(1,";"+createBy+";") ;
									pss.setString(2, flowDepict);
									pss.setString(3, lastDesignId);
									pss.setString(4, keyId);
									pss.executeUpdate() ;
									

									//新增重置核记录
									sql="insert into OAMyWorkFlowDet (id,f_ref,nodeId,checkPerson,startTime,createBy,nodeType,endTime,statusId) values (?,?,?,?,?,?,?,?,1)";
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, IDGenerater.getId()) ;
									pss.setString(2, keyId);
									pss.setString(3, "-1") ;
									pss.setString(4, lg.getId());
									pss.setString(5,BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)) ;
									pss.setString(6, lg.getId()) ;
									pss.setString(7, "reSet"); 
									pss.setString(8, BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)); 
									pss.executeUpdate();
									
									sql = " update OAMyWorkFlowDet set statusId= 1 where f_ref=?  ";
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, keyId) ;
									pss.executeUpdate();
									
									//反审核后，重新增加开始节点
									sql="insert into OAMyWorkFlowDet (id,f_ref,nodeId,checkPerson,startTime,createBy,nodeType,endTime,workFlowNode,checkPersons) values (?,?,?,?,?,?,?,?,?,?)";
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, IDGenerater.getId()) ;
									pss.setString(2, keyId);
									pss.setString(3, "0") ;
									pss.setString(4, lg.getId());
									pss.setString(5,BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)) ;
									pss.setString(6, lg.getId()) ;
									pss.setString(7, "transact"); 
									pss.setString(8,""); 
									pss.setString(9,""); 
									pss.setString(10,""); 
									pss.executeUpdate();
									
									
									sql = "update "+tableName+" set workFlowNode='0',workFlowNodeName='notApprove',checkPersons=? where id=?";
									pss = conn.prepareStatement(sql);
									pss.setString(1, ";"+createBy+";");
									pss.setString(2, keyId);
									pss.executeUpdate();
									
									sql = "delete from OAMyWorkFlowPerson where keyId=? ";
									pss = conn.prepareStatement(sql);
									pss.setString(1, keyId);
									pss.executeUpdate();
									
									sql = "insert into OAMyWorkFlowPerson(keyId,tableName,checkperson,hadApprover,curApprover) " +
											"values( ?,?,?,0,1 ) ";
									pss = conn.prepareStatement(sql);
									pss.setString(1, keyId);
									pss.setString(2, tableName);
									pss.setString(3, createBy);
									pss.executeUpdate();
								}
							}
						} catch (Exception e) {
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							e.printStackTrace();
						}
					}
				}) ;
				return rst.getRetCode() ;
			}
		}) ;
		rst.setRetCode(retCode);
		return rst ;
	}
	
	/**
	 * 根据数据库保存内容查出弹出框显示内容
	 * @param type
	 * @param fieldName
	 * @param map
	 * @return
	 */
	public String showNames(String type, String fieldName, HashMap<String, String> map) {
		String names="";
		boolean  flag=false;
		if(map.get(fieldName)==null) {
			return names;
		}
		if(map.get(fieldName).contains(";")){ //多选
			flag=true;
		}
		if(!flag){ //单选（暂时只支持）
			for(String Id:map.get(fieldName).split(";")){
				String temp="";
				if("employee".equals(type)){
					temp = GlobalsTool.getEmpFullNameByUserId(Id);
				}else if("dept".equals(type)){
					temp =(String) BaseEnv.deptMap.get(Id);
				}else if("client".equals(type)){
					temp =findClientNameById(Id);
				}
				names=temp;
			}
		}
		return names;
	}
	
	/**
	 * 根据客户Id获取客户名称
	 */
	public String findClientNameById(String keyId){
		List<String> param = new ArrayList<String>();
		String sql = "SELECT moduleId,clientName FROM CRMClientInfo WHERE id=?";
		param.add(""+keyId+"");
		Result rs = this.sqlList(sql, param);
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && rs.getRetVal() != null) {
			List obj=(List) rs.retVal;
			if(obj!=null && obj.size()>0){
				Object[] objList=(Object[]) obj.get(0);
				return objList[1].toString();
			}
		}
		return "";
	}
	
	/**
	 * 获得自定义弹出框显示内容
	 * @param tableMap
	 * @param fieldBean
	 * @param loginBean
	 * @return
	 */
	public String getPop(final HashMap<String, String> tableMap, final DBFieldInfoBean fieldBean,final LoginBean loginBean) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String popuValue ="";
							boolean  flag=false;
							if(tableMap.get(fieldBean.getFieldName()).contains(";")){ //多选
								flag=true;
							}
							if(!flag){
								for(String str : tableMap.get(fieldBean.getFieldName()).split(";")){
									String refsql = new DynDBManager().getRefSql(fieldBean,false,str, "00001", new Hashtable(),false,tableMap,loginBean.getId());
									if (refsql != null && fieldBean.getSelectBean()!=null) {
										ResultSet crset = conn.createStatement().executeQuery(refsql);
										if (crset.next()) {
											popuValue=String.valueOf(crset.getObject(1));
										}
									}
								}
							}
							rst.setRetVal(popuValue);
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
		return rst.retVal.toString();
	}
	
	/**
	 * 查询他人委托给我的工作流
	 * 
	 * @param tableName
	 * @param ids
	 * @param tblMap
	 * @param local
	 * @return
	 */
	public static HashMap<String, String> queryConsignation(final String userId, final String flowId) {
		final Result res = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							HashMap<String, String> consignMap = new HashMap<String, String>();
							String sql = "select UserID from OAWorkConsignation where CongignUserID=? "
									+  " and (flowName like (select '%'+sameFlow+';%' from OAWorkFlowTemplate where id=?) or len(isNull(flowName,''))=0) and State = 1"
									+  " and substring(convert(varchar,GETDATE(),120),0,11)>=beginTime " 
									+  " and substring(convert(varchar,GETDATE(),120),0,11)<=endTime" ;
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, userId);
							pss.setString(2, flowId);
							ResultSet rss = pss.executeQuery();
							while (rss.next()) {
								consignMap.put(rss.getString("UserID"), userId);
							}
							res.setRetVal(consignMap);
						} catch (Exception ex) {
							res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
						}
					}
				});
				return res.getRetCode();
			}
		});
		if (retCode == ErrorCanst.DEFAULT_SUCCESS) {
			return (HashMap<String, String>) res.retVal;
		}
		return new HashMap<String, String>();
	}
	
	/**
	 * 根据当前审批人查询被委托的人
	 * 
	 * @param tableName
	 * @param ids
	 * @param tblMap
	 * @param local
	 * @return
	 */
	public static HashMap<String,String> queryConsignationByUserId(final String userId,final String flowId,
			final Connection conn) {
		
		HashMap<String,String> consignMap = new HashMap<String,String>();
		try {
			String sql = "select UserID,CongignUserID from OAWorkConsignation where userId in ('"+userId+"') " 
				+  "and (flowName like (select '%'+sameFlow+';%' from OAWorkFlowTemplate where id=?) or len(isNull(flowName,''))=0)  and State = 1 "
				+  "and substring(convert(varchar,GETDATE(),120),0,11)>=beginTime " 
				+  "and substring(convert(varchar,GETDATE(),120),0,11)<=endTime";
			PreparedStatement pss = conn.prepareStatement(sql);
			pss.setString(1, flowId);
			ResultSet rss = pss.executeQuery();
			while (rss.next()) {
				consignMap.put(rss.getString("UserID"),rss.getString("CongignUserID")) ;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return consignMap;
	}
	
	/**
	 * 根据当前受托的人查询委托人
	 * 
	 * @param tableName
	 * @param ids
	 * @param tblMap
	 * @param local
	 * @return
	 */
	public static String queryUserIdByConsignUserId(final String userId,final String flowId,final String checkpersons,
			final Connection conn) {
		
		String strUserID = userId ;
		try {
			String sql = "select UserID,CongignUserID from OAWorkConsignation where CongignUserID = ? and CHARINDEX(UserID+';','"+checkpersons+"',0) >0 and (flowName like (select '%'+sameFlow+';%' from OAWorkFlowTemplate where id=?) or len(isNull(flowName,''))=0)  and State = 1";
			PreparedStatement pss = conn.prepareStatement(sql);
			pss.setString(1, userId);
			pss.setString(2, flowId);
			ResultSet rss = pss.executeQuery();
			if (rss.next()) {
				strUserID = rss.getString("UserID");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return strUserID;
	}
	
	/**
	 * 查询委托
	 * @param consignForm
	 * @return
	 */
	public Result existConsign(String loginId){
		
		ArrayList<String> param = new ArrayList<String>();
		String sql = "select * from OAWorkConsignation where congignuserid=? and state=1 and substring(convert(varchar,GETDATE(),120),0,11)>=beginTime  and substring(convert(varchar,GETDATE(),120),0,11)<=endTime";
		param.add(loginId);
		return sqlList(sql, param);
	}
	
	/**
	 * 根据表名获取linkAddress
	 * @param tableName
	 * @return
	 */
	public String linkAddrQueryByTableName(String tableName,Connection conn){
		String linkAddr = "";	
		String sql = "SELECT linkAddress FROM tblModules WHERE tblName='"+tableName+"'";
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next()){
				linkAddr=rs.getString("linkAddress");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
        return linkAddr;
	}
}
