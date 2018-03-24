package meizhi;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.AppleApnsSend;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.NotifyFashion;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.SystemState;


public class RelayWorkFlow extends AIODBManager{
	private static Gson gson;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}
	
	/**
	 * 7天未入库，提醒杨总,独立事物，每天提醒一次
	 * @param toUserId
	 * @param conn
	 * @return
	 */
	public Result sendMsg7(String userIds,Connection conn) {
		final Result res = new Result();
		//入库延时7天的 
		try{   
			String sql = "select id,Description,delayTime,Manager1,Manager1LastNote,Manager2 from tblProjectTimeDelay " +
					"where closeStatus=0 and delayTime > 60   "; //and needNotice = 1  and delayType in (8,9)   所有超过24小时的延时消息都要通知杨总
			PreparedStatement pss = conn.prepareStatement(sql);
			ResultSet rss = pss.executeQuery();
			while (rss.next()) {
				String id = rss.getString("id");
				String Description = rss.getString("Description");
				long mu = rss.getLong("delayTime");
				//String Manager1 = rss.getString("Manager1");
				//Manager1 = Manager1==null?"":Manager1;
				//String Manager1LastNote = rss.getString("Manager1LastNote");
				//String Manager2 = rss.getString("Manager2");
				//Manager2 = Manager2==null?"":Manager2;
				String relayTimeStr = "";
				if(mu<60){
					relayTimeStr = ((long)mu)+"分钟";
				}else if(mu<24 * 60){
					relayTimeStr = (((long)(100*mu/60))/100)+"小时";
				}else{
					relayTimeStr = (((long)(100*mu/(24*60)))/100)+"天";
				}
				String notetitle = Description + ",延时" + relayTimeStr;
				for(String userId:userIds.split(";")){
					if(userId != null && userId.length() > 0){
						BaseEnv.log.debug("延时7天给("+userId+")发送消息"+notetitle + ",时间"+ BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
						//new Thread(new NotifyFashion("1", notetitle, notetitle, toUserId, 4, "workflow", billId, "", "", "approve")).start();
						JsonObject json = new JsonObject();
						json.addProperty("op", "add");
						json.addProperty("type", "note");
						json.addProperty("userId", userId);
						json.addProperty("noteType", "approve");
						json.addProperty("id", IDGenerater.getId());
						json.addProperty("refid", id);
						json.addProperty("title", notetitle);
						json.addProperty("time", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
						json.addProperty("content", notetitle);
						new AppleApnsSend(
							String.valueOf(SystemState.instance.dogId), userId,
								notetitle, json.toString(), "").start();
					}
				}
				
				
			}
			
		} catch (Exception ex) {
			BaseEnv.log.error("延时7天给杨总发送延期消息",ex);
            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		}
		return res;
	}
	
	/**
	 * 普通微信提醒，提醒责任人和管理层，这是每10分钟执行一次
	 * @param toUserId
	 * @param conn
	 * @return
	 */
	public Result sendMsg(Connection conn) {
		final Result res = new Result();

		try{		
			//此为所有要通知责任主管不通知责任人的信息
			String sql = "select id,Description,delayTime,Manager1,Manager1LastNote,Manager2 from tblProjectTimeDelay " +
					"where closeStatus=0 and needNotice = 0  ";
			PreparedStatement pss = conn.prepareStatement(sql);
			ResultSet rss = pss.executeQuery();
			while (rss.next()) {
				String id = rss.getString("id");
				String Description = rss.getString("Description");
				long mu = rss.getLong("delayTime");
				String Manager1 = rss.getString("Manager1");
				Manager1 = Manager1==null?"":Manager1;
				String Manager1LastNote = rss.getString("Manager1LastNote");
				String Manager2 = rss.getString("Manager2");
				Manager2 = Manager2==null?"":Manager2;
				String relayTimeStr = "";
				if(mu<60){
					relayTimeStr = ((long)mu)+"分钟";
				}else if(mu<24 * 60){
					relayTimeStr = (((long)(100*mu/60))/100)+"小时";
				}else{
					relayTimeStr = (((long)(100*mu/(24*60)))/100)+"天";
				}
				String notetitle = Description + ",延时" + relayTimeStr;
				long time = 0;
				if((Manager1LastNote!=null && Manager1LastNote.length() >0)){
					time = BaseDateFormat.parse(Manager1LastNote, BaseDateFormat.yyyyMMddHHmmss).getTime();
				}
				if(new Date().getTime()-time > 60* 60000){
					if(Manager1 != null && Manager1.length() > 0 ){
						BaseEnv.log.debug("审核延时给责任人1("+Manager1+")发送消息"+notetitle + ",时间"+ BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
						//new Thread(new NotifyFashion("1", notetitle, notetitle, toUserId, 4, "workflow", billId, "", "", "approve")).start();
						JsonObject json = new JsonObject();
						json.addProperty("op", "add");
						json.addProperty("type", "note");
						json.addProperty("userId", Manager1);
						json.addProperty("noteType", "approve");
						json.addProperty("id", IDGenerater.getId());
						json.addProperty("refid", id);
						json.addProperty("title", notetitle);
						json.addProperty("time", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
						json.addProperty("content", notetitle);
						new AppleApnsSend(
							String.valueOf(SystemState.instance.dogId), Manager1,
								notetitle, json.toString(), "").start();
					}
					if(Manager2 != null && Manager2.length() > 0 ){
						BaseEnv.log.debug("审核延时给责任人2("+Manager2+")发送消息"+notetitle + ",时间"+ BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
						//new Thread(new NotifyFashion("1", notetitle, notetitle, toUserId, 4, "workflow", billId, "", "", "approve")).start();
						JsonObject json = new JsonObject();
						json.addProperty("op", "add");
						json.addProperty("type", "note");
						json.addProperty("userId", Manager2);
						json.addProperty("noteType", "approve");
						json.addProperty("id", IDGenerater.getId());
						json.addProperty("refid", id);
						json.addProperty("title", notetitle);
						json.addProperty("time", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
						json.addProperty("content", notetitle);
						new AppleApnsSend(
							String.valueOf(SystemState.instance.dogId), Manager2,
								notetitle, json.toString(), "").start();
					}
					//修改最后更新时间
					if ((Manager1 != null && Manager1.length() > 0) || (Manager2 != null && Manager2.length() > 0)){
						sql  = "update tblProjectTimeDelay set Manager1LastNote= convert(varchar(19),getdate(),120)  where id=?";
						PreparedStatement pss2 = conn.prepareStatement(sql);
						pss2.setString(1, id);
						pss2.execute();
					}
				}
			}
			//此为所有要通知责任主管不通知责任人的信息
			sql = "select id,Description,delayTime,Manager1LastNote,isnull(Manager1,'')+','+isnull(Manager2,'')+','+isnull(N.CheckPersion,'') checkPersion " +
					"from tblProjectTimeDelay a OUTER APPLY(SELECT  CheckPersion= STUFF(REPLACE(REPLACE((" +
					"      SELECT CheckPersion FROM tblProjectTimeDelayDet k WHERE f_ref = A.id  FOR XML AUTO" +
					"                                   ), '<k CheckPersion=\"', ','), '\"/>', ''), 1, 1, '')" +
					"                       )N where closeStatus=0 and needNotice = 1   and delayType not in (8,9)";
			pss = conn.prepareStatement(sql);
			rss = pss.executeQuery();
			while (rss.next()) {
				String id = rss.getString("id");
				String Description = rss.getString("Description");
				long mu = rss.getLong("delayTime");
				String Manager1LastNote = rss.getString("Manager1LastNote");
				String checkPersion = rss.getString("checkPersion");
				String relayTimeStr = "";
				if(mu<60){
					relayTimeStr = ((long)mu)+"分钟";
				}else if(mu<24 * 60){
					relayTimeStr = (((long)(100*mu/60))/100)+"小时";
				}else{
					relayTimeStr = (((long)(100*mu/(24*60)))/100)+"天";
				}
				String notetitle = Description + "延时" + relayTimeStr;
				long time = 0;
				if((Manager1LastNote!=null && Manager1LastNote.length() >0)){
					time = BaseDateFormat.parse(Manager1LastNote, BaseDateFormat.yyyyMMddHHmmss).getTime();
				}
				if(new Date().getTime()-time > 60* 60000 && checkPersion != null && checkPersion.length() > 0){
					String cps[] = checkPersion.split(",");
					boolean hasSend = false;
					for(String cp :cps){
						if(cp != null && cp.length() > 0 ){
							hasSend = true;
							BaseEnv.log.error("审核延时给责任人("+cp+")发送消息"+notetitle + ",时间"+ BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
							//new Thread(new NotifyFashion("1", notetitle, notetitle, toUserId, 4, "workflow", billId, "", "", "approve")).start();
							JsonObject json = new JsonObject();
							json.addProperty("op", "add");
							json.addProperty("type", "note");
							json.addProperty("userId", cp);
							json.addProperty("noteType", "approve");
							json.addProperty("id", IDGenerater.getId());
							json.addProperty("refid", id);
							json.addProperty("title", notetitle);
							json.addProperty("time", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
							json.addProperty("content", notetitle);
							new AppleApnsSend(
								String.valueOf(SystemState.instance.dogId), cp,
									notetitle, json.toString(), "").start();
						}
					}
					//修改最后更新时间
					if (hasSend){
						sql  = "update tblProjectTimeDelay set Manager1LastNote= convert(varchar(19),getdate(),120)  where id=?";
						PreparedStatement pss2 = conn.prepareStatement(sql);
						pss2.setString(1, id);
						pss2.execute();
					}
				}
			}
		} catch (Exception ex) {
			BaseEnv.log.error("审核延时给杨总发送延期消息",ex);
            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		}
		return res;
	}
	
	public Result doRelay(final String toUserId, Connection conn) {
		final Result res = new Result();
		try {     
			//找出
			String sql = " select a.tableName,a.applyType,a.currentNode,"
					+ "isnull((select top 1 endTime+';'+cast(isnull(oaTimeLimitUnit,0) as varchar(2))+';'+cast(isnull(benchMarkTime,0) as varchar(20))+';'+c.id from OAMyWorkFlowDet c where c.f_ref=a.id and c.nodeId!=a.currentNode and c.statusId=0 and isnull(endTime,'')<> '' order by c.sortOrder desc),'') lastEndTime,"
					+ "isnull(a.checkPerson,''),isnull(a.lastNoteTime,''),a.keyId,a.nextNodeIds,a.applyContent,a.lastNodeId,a.id from OAMyWorkFlow a where a.currentNode not in ('0','-1') ";
			PreparedStatement pss = conn.prepareStatement(sql);
			ResultSet rss = pss.executeQuery();
			while (rss.next()) {
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

				if (lastNoteTime == null || lastNoteTime.indexOf(":") == -1) {
					lastNoteTime = "";
				} else {
					if (lastNoteTime.substring(0, lastNoteTime.indexOf(":")).equals(currentNode)) {
						lastNoteTime = lastNoteTime.substring(lastNoteTime.indexOf(":") + 1);
					} else {
						lastNoteTime = "";
					}
				}
				if (applayType == null || applayType.length() == 0) {
					continue;
				}
				final OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(applayType);

				WorkFlowDesignBean designBean = BaseEnv.workFlowDesignBeans.get(applayType);
				if (designBean == null)
					continue;
				FlowNodeBean nodeB = designBean.getFlowNodeMap().get(currentNode);
				//未提醒过，且设置有提前提醒
				if (nodeB != null) {//有提前提醒要求
					if (lastEndTime == null || lastEndTime.length() < 4){
						BaseEnv.log.debug("计算延期审批 Error lastEndTime="+lastEndTime);
						continue;
					}
					Date standDate = new OAMyWorkFlowMgt().getStandDate(nodeB, lastEndTime, 0, 0); //取标准的结束时间。
					
					String detId = lastEndTime.split(";")[3];
					if (standDate != null && (new Date().getTime()-standDate.getTime()) >  10 * 60000) { //超过10分钟开始记录
						//超时，插入超时表中
						//查询是否已经有超时记录
						BaseEnv.log.debug("美芝：找到延时单据"+detId+":"+standDate);
						sql = " select id,lastNodeTime,workflowNodeName,ProjectNo from mzrelayworkflow where myWorkDetId= ? and refBillId=? ";
						pss = conn.prepareStatement(sql);
						pss.setString(1, detId);
						pss.setString(2, billId);
						ResultSet rsold = pss.executeQuery();
						String relayId = "";
						if (rsold.next()) {
							relayId = rsold.getString(1);
							String lastNodeTime = rsold.getString("lastNodeTime");
							String workflowNodeName = rsold.getString("workflowNodeName");
							String ProjectNo = rsold.getString("ProjectNo");
							sql = "update mzrelayworkflow  set relayTime = ?,lastNodeTime=? where id=?";
							pss = conn.prepareStatement(sql);
							long realy = new Date().getTime() - standDate.getTime();
							pss.setLong(1, realy / 60000);
							//每个小时通知一次给杨总
							Date lastNodeTimeDate = new Date();
							try {
								lastNodeTimeDate = BaseDateFormat.parse(lastNodeTime, BaseDateFormat.yyyyMMddHHmmss);
							} catch (Exception e) {
							}
							BaseEnv.log.debug("美芝：修改延时单据时间"+detId+":"+standDate+";"+realy+":最后通知时间"+lastNodeTime);
							if (lastNodeTime == null || lastNodeTime.length() == 0 || new Date().getTime() - lastNodeTimeDate.getTime() > 60 * 60000) {
								
								long mu = (new Date().getTime() - standDate.getTime()) /  60000;
								String cpName = "";
								for (String ck : checkPerson.split(";")) {
									if (ck.trim().length() > 0) {
										cpName += OnlineUserInfo.getUser(ck).getName() + ",";
									}
								}
								String relayTimeStr = "";
								if(mu<60){
                					relayTimeStr = ((long)mu)+"分钟";
                				}else if(mu<24 * 60){
                					relayTimeStr = (((long)(100*mu/60))/100)+"小时";
                				}else{
                					relayTimeStr = (((long)(100*mu/(24*60)))/100)+"天";
                				}
								String notetitle = cpName + title + workflowNodeName + "延时" + relayTimeStr;
								BaseEnv.log.debug("美芝：通知杨总"+detId+":toUserId"+toUserId+":"+notetitle);
								
								//new Thread(new NotifyFashion("1", notetitle, notetitle, toUserId, 4, "workflow", billId, "", "", "approve")).start();
								JsonObject json = new JsonObject();
								json.addProperty("op", "add");
								json.addProperty("type", "note");
								json.addProperty("userId", toUserId);
								json.addProperty("noteType", "approve");
								json.addProperty("id", IDGenerater.getId());
								json.addProperty("refid", billId);
								json.addProperty("title", notetitle);
								json.addProperty("time", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
								json.addProperty("content", notetitle);
								new AppleApnsSend(
									String.valueOf(SystemState.instance.dogId), toUserId,
										notetitle, json.toString(), "").start();
								
								lastNodeTime = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
							}
							pss.setString(2, lastNodeTime);
							pss.setString(3, relayId);    
							pss.executeUpdate();
						} else {
							BaseEnv.log.debug("美芝：插入延时单据时间"+detId+":"+standDate);
							relayId = IDGenerater.getId();
							sql = "INSERT INTO [mzRelayWorkFlow]([refBillType],[refBillTypeName],[refBillId],"
									+ "[workflowNodeId],[workflowNodeName],[myWorkDetId],[ProjectNo],[lastNodeTime],[standEndTime],[relayTime],id) " + "values(?,?,?,?,?,?,?,?,?,?,?)";
							pss = conn.prepareStatement(sql);
							pss.setString(1, tableName);
							pss.setString(2, template.getTemplateName());
							pss.setString(3, billId);
							pss.setString(4, nodeB.getKeyId());
							pss.setString(5, nodeB.getDisplay());
							pss.setString(6, detId);
							String projectNo = "";
							DBFieldInfoBean dfb = GlobalsTool.getFieldBean(tableName, "ProjectNo");
							if (dfb != null) {
								//存在项目字段插入项目号
								String sqlpj = " select ProjectNo from  " + tableName + " where id=?";
								PreparedStatement psspj = conn.prepareStatement(sqlpj);
								psspj.setString(1, billId);
								ResultSet rspj = psspj.executeQuery();
								if (rspj.next()) {
									projectNo = rspj.getString(1);
								}
							}
							pss.setString(7, projectNo);
							pss.setString(8, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
							pss.setString(9, BaseDateFormat.format(standDate, BaseDateFormat.yyyyMMddHHmmss));
							long realy = new Date().getTime() - standDate.getTime();
							pss.setLong(10, realy / 60000);
							pss.setString(11, relayId);
							pss.execute();
							sql = " insert into mzRelayWorkFlowDet(f_ref,checkPersion) values(?,?)";
							String checkPersons[] = checkPerson.split(";");
							for (String ck : checkPersons) {
								if (ck.trim().length() > 0) {
									pss = conn.prepareStatement(sql);
									pss.setString(1, relayId);
									pss.setString(2, ck);
									pss.execute();
								}
							}
						}
					} 
				}
			}
		} catch (Exception ex) {
			BaseEnv.log.error("给杨总发送延期消息",ex);
            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		}
		return res;
	}
	
	public String getKB(String projectNo){
		String ret = "";
		final HashMap map = new HashMap();
		Result rs = sqlList("select billNo,name from tblProject where workFlowNodeName='finish' and status <> 3", new ArrayList());
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS && ((ArrayList)rs.retVal).size()==0){
			BaseEnv.log.error(" 看板取项目异常 ");
			return "";
		}
		if(projectNo == null || projectNo.length() ==0){
			projectNo = ((ArrayList<Object[]>)rs.retVal).get(0)[0].toString();
		}
		final String fprojectNo = projectNo;
		map.put("projects", rs.retVal);
		map.put("curProjectNo", projectNo);
		
		for(Object[] ss: (ArrayList<Object[]>)rs.retVal){
			if(ss[0].equals(projectNo)){
				map.put("curProject", ss[1]);
			}
		}
		
		
		final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                            SQLException {
                    	//取具体一个项目的金额数据
                    	try {  
	                    	CallableStatement cstmt = conn.prepareCall("{call report_GetProjectView(?)}");
	            			cstmt.setString(1, fprojectNo);                    	
	                    	ResultSet rss = cstmt.executeQuery();
							SQLWarning warn = rss.getWarnings();
							while (warn != null) {
								BaseEnv.log.debug("看板 warn " + warn.getMessage());
								warn = warn.getNextWarning();
							}
							HashMap pl = new HashMap();
							while(rss.next()){
								HashMap m1= new HashMap();
								m1.put("planAmount", rss.getDouble("planAmount"));
								m1.put("FactAmount", rss.getDouble("FactAmount"));
								m1.put("inAmount", rss.getDouble("inAmount"));
								m1.put("returnAmount", rss.getDouble("returnAmount"));
								m1.put("stockAmount", rss.getDouble("stockAmount"));
								m1.put("allotAmount", rss.getDouble("allotAmount"));
								pl.put(rss.getString("mType") , m1);
							}
							map.put("amount", pl);
                    	} catch (Exception ex) {
                            BaseEnv.log.error("看板取项目金额",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                    	//取项目的延时数据
                    	PreparedStatement pstmt ;
                        ResultSet rs;
                        String sqlStr="" +
                        		" SELECT * FROM(SELECT * FROM mzRelayWorkFlow)A" +
                    	" OUTER APPLY(SELECT " +
                    	"        empFullName= STUFF(REPLACE(REPLACE(" +
                    	"             (" +
                    	"                 SELECT EmpFullName FROM mzRelayWorkFlowDet  N join tblEmployee k on n.checkPersion=k.id" +
                    	"                 WHERE f_ref = A.id" +
                    	"                 FOR XML AUTO" +
                    	"             ), '<k EmpFullName=\"', ','), '\"/>', ''), 1, 1, '')" +
                    	" )N where ProjectNo=? ";
                        try {  
                            pstmt = conn.prepareStatement(sqlStr);
                            pstmt.setString(1, fprojectNo);
                            List list = new ArrayList();
                            rs = pstmt.executeQuery();
                            while (rs.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
                            		Object obj=rs.getObject(i);
                            		if(obj==null){
                            			if(rs.getMetaData().getColumnType(i)==Types.NUMERIC||rs.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rs.getMetaData().getColumnName(i), 0);
                            			}else{
                            				map.put(rs.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			if(rs.getMetaData().getColumnName(i).equals("relayTime")){
                            				double relayTime = rs.getDouble("relayTime");
                            				String relayTimeStr="";
                            				if(relayTime<60){
                            					relayTimeStr = ((int)relayTime)+"分钟";
                            				}else if(relayTime<24 * 60){
                            					relayTimeStr = (((int)(100*relayTime/60))/100)+"小时";
                            				}else{
                            					relayTimeStr = (((int)(100*relayTime/(24*60)))/100)+"天";
                            				}
                            				map.put("relayTime", relayTimeStr);
                            			}else {
	                            			map.put(rs.getMetaData().getColumnName(i), obj);
	                            		}
                            		}
                            	}
                            	list.add(map);
                            }
                            map.put("relay", list);                            
                        } catch (Exception ex) {
                        	BaseEnv.log.error("看板取项目审核延迟",ex);
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                        
                        sqlStr="" +
                		" select BillDate,Question,EmpFullName from tblQuestionFeedback a join tblEmployee b on a.createBy=b.id" +
                		" where Flag=2 and  ProjectNo=? ";
		                try {  
		                    pstmt = conn.prepareStatement(sqlStr);
		                    pstmt.setString(1, fprojectNo);
		                    List list = new ArrayList();
		                    rs = pstmt.executeQuery();
		                    while (rs.next()) {
		                    	HashMap map=new HashMap();
		                    	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
		                    		Object obj=rs.getObject(i);
		                    		if(obj==null){
		                    			if(rs.getMetaData().getColumnType(i)==Types.NUMERIC||rs.getMetaData().getColumnType(i)==Types.INTEGER){
		                    				map.put(rs.getMetaData().getColumnName(i), 0);
		                    			}else{
		                    				map.put(rs.getMetaData().getColumnName(i), "");
		                    			}
		                    		}else{
		                    			map.put(rs.getMetaData().getColumnName(i), obj);
		                    		}
		                    	}
		                    	list.add(map);
		                    }
		                    map.put("question", list);
		                    
		                } catch (Exception ex) {
		                	BaseEnv.log.error("看板取项目问题",ex);
		                    rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		                    return;
		                }
                        
                    }
                });
                return rst.getRetCode();
            }
        });
        rst.setRetCode(retCode);
		
        ret = gson.toJson(map);
		
		return ret;
	}
	
}
