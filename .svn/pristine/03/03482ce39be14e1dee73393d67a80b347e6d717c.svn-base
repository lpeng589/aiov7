package com.koron.hr.workRule;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.hr.bean.DutyPeriodsBean;
import com.koron.hr.bean.EmployeeWorkRuleInfoBean;
import com.koron.hr.bean.WorkRuleBean;
import com.koron.hr.bean.WorkRuleListReportBean;
import com.menyi.aio.bean.BrushCardAnnalBean;
import com.menyi.aio.bean.EvectionBean;
import com.menyi.aio.bean.LeaveBean;
import com.menyi.aio.bean.SquadSect;
import com.menyi.aio.bean.StatWorkRukeListReportBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;

public class WorkRuleReportMgt extends DBManager {


	public Result getWorkRuleByEmpNo(final String employeeNo) {
		final Result empResult = new Result();
		final StringBuffer sqlBuffer = new StringBuffer();

		sqlBuffer.append("{call proc_workRule (?)}");
		int retCode = DBUtil.execute(new IfDB() {
			@Override
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection con) throws SQLException {
						try {
							CallableStatement cs = con.prepareCall(sqlBuffer
									.toString());
							cs.setString(1, employeeNo);
							ResultSet rs = cs.executeQuery();
							while (rs.next()) {
								empResult.setRetVal(new WorkRuleBean(rs
										.getString("ruleNo"), rs
										.getInt("overdueMinute"), rs
										.getDouble("overdueAmerce"), rs
										.getInt("leaveEarlyMinute"), rs
										.getDouble("leaveEarlyAmerce"), rs
										.getInt("absentWorkMinute"), rs
										.getDouble("absentWorkAmerce"), rs
										.getInt("statusId")));
							}
						} catch (Exception e) {
							empResult.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							empResult.setRetVal(e.getMessage());
						}

					}
				});
				return empResult.getRetCode();
			}
		});
		empResult.setRetCode(retCode);
		return empResult;
	}


	public Result getPeriodsEmpNo(final String startDate,final String employeeNo) {
		final Result result = new Result();
		final String sqlCall = "{call proc_EmpArrangSquad (?)}";
		final StringBuffer sqlPre = new StringBuffer();
		sqlPre.append("select periodsNo,squadEnactmentNo,convert(varchar(10),date,120) as date from tblDutyPeriodsByDate where date < convert(varchar(10),getdate(),120) ");
		sqlPre.append(" and periodsNo = ?");
		if(!"".equals(startDate)){
			sqlPre.append(" and date > '" + startDate+"'");
		}

		int retCode = DBUtil.execute(new IfDB() {
			@Override
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection con) throws SQLException {
						try {
							String periodsNo = "";
							CallableStatement cs = con.prepareCall(sqlCall);
							cs.setString(1, employeeNo);
							ResultSet rsPeriodsNo = cs.executeQuery();
							while (rsPeriodsNo.next()) {
								periodsNo = rsPeriodsNo.getString("periodsNo");
								break;
							}

							PreparedStatement ps = con.prepareStatement(sqlPre
									.toString());
							ps.setString(1, periodsNo);
							ResultSet rsPeriods = ps.executeQuery();
							List<DutyPeriodsBean> periods = new ArrayList<DutyPeriodsBean>();
							while (rsPeriods.next()) {
								periods.add(new DutyPeriodsBean(
										periodsNo,
										getSquadSectByEmpNo(con, rsPeriods.getString("squadEnactmentNo")),
										rsPeriods.getString("date")
										));
							}
							result.setRetVal(periods);
						} catch (Exception e) {
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(e.getMessage());
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}


	public List<SquadSect> getSquadSectByEmpNo(Connection con,final String squadEnactmentNo) throws SQLException {

		final String sql = "select squadEnactmentNo,onDutyTime,offDutyTime,onDutyAvailabilityTime," +
				"offDutyAvailabilityTime,squadSectType from view_SquadEnactment where squadEnactmentNo =  ? order by onDutyTime";

		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, squadEnactmentNo);
		ResultSet rs = ps.executeQuery();
		List<SquadSect> squadSects = new ArrayList<SquadSect>();
		while (rs.next()) {
			squadSects.add(new SquadSect(rs
					.getString("squadEnactmentNo"), rs
					.getInt("onDutyAvailabilityTime"), rs
					.getInt("offDutyAvailabilityTime"), rs
					.getString("onDutyTime"), rs
					.getString("offDutyTime"), rs
					.getString("squadSectType")));
		}
					
		return squadSects;
	}


	public Result getCardAnnalByEmpNo(final String startDate,final String employeeNo) {
		final Result result = new Result();
		final String sql = "{call proc_BrushCardAnnal (?,?)}";

		int retCode = DBUtil.execute(new IfDB() {
			@Override
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection con) throws SQLException {
						try {
							CallableStatement cs = con.prepareCall(sql);
							cs.setString(1, startDate);
							cs.setString(2, employeeNo);
							ResultSet rs = cs.executeQuery();
							List<BrushCardAnnalBean> annals = new ArrayList<BrushCardAnnalBean>();
							while (rs.next()) {
								annals.add(new BrushCardAnnalBean(
										rs.getString("dutyCardTime"),
										rs.getString("dutyDate")
								));
							}
							result.setRetVal(annals);
						} catch (Exception e) {
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(e.getMessage());
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}


	public Result getLeaveInfoByEmpNo(final String employeeNo) {
		final Result result = new Result();
		final String sql = "select BillNo,EmployeeCode,CreateDate,LeaveStartDate,"
			+ "LeaveReturnDate,LeavebillType from OALeaveBill where EmployeeCode = ?";
		int retCode = DBUtil.execute(new IfDB() {
			@Override
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection con) throws SQLException {
						try {
							PreparedStatement ps = con.prepareStatement(sql);
							ps.setString(1, employeeNo);
							ResultSet rs = ps.executeQuery();
							List<LeaveBean> leaves = new ArrayList<LeaveBean>();
							while (rs.next()) {
								leaves.add(new LeaveBean(
										rs.getString("BillNo"), rs
										.getString("EmployeeCode"), rs
										.getString("CreateDate"), rs
										.getString("LeaveStartDate"),
										rs.getString("LeaveReturnDate"), rs
										.getString("LeavebillType")));
							}
							result.setRetVal(leaves);
						} catch (Exception e) {
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(e.getMessage());
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}

	
	public Result getEvectionInfoByEmpNo(final String employeeNo) {
		final Result result = new Result();
		final String sql = "select BillNo,EmployeeCode,BillDate,EvectionStartDate,"
			+ "EvectionReturnDate from OAEvectionBill where EmployeeCode = ?";
		int retCode = DBUtil.execute(new IfDB() {
			@Override
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection con) throws SQLException {
						try {
							PreparedStatement ps = con.prepareStatement(sql);
							ps.setString(1, employeeNo);
							ResultSet rs = ps.executeQuery();
							List<EvectionBean> evections = new ArrayList<EvectionBean>();
							while (rs.next()) {
								evections.add(new EvectionBean(rs
										.getString("BillNo"), rs
										.getString("EmployeeCode"), rs
										.getString("BillDate"), rs
										.getString("EvectionStartDate"), rs
										.getString("EvectionReturnDate")));
							}
							result.setRetVal(evections);
						} catch (Exception e) {
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(e.getMessage());
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}


	public Result statCardAnnalByEmpNo(final String employeeNo,String id){
		Result result = new Result();
		String startDate = getStartDate(employeeNo).getRetVal().toString();
		EmployeeWorkRuleInfoBean ruleInfoBean = new EmployeeWorkRuleInfoBean(
				employeeNo,
				(WorkRuleBean)getWorkRuleByEmpNo(employeeNo).getRetVal(),
				(List<DutyPeriodsBean>)getPeriodsEmpNo(startDate,employeeNo).getRetVal(),
				(List<BrushCardAnnalBean>)getCardAnnalByEmpNo(startDate,employeeNo).getRetVal(),
				(List<LeaveBean>)getLeaveInfoByEmpNo(employeeNo).getRetVal(),
				(List<EvectionBean>)getEvectionInfoByEmpNo(employeeNo).getRetVal()
		);
		List<DutyPeriodsBean> periods = ruleInfoBean.getPeriodsBean();
		List<BrushCardAnnalBean> annals = ruleInfoBean.getAnnals();
		for(int i = 0;i < periods.size();i++){
			List annalByDay = new ArrayList();
			List<LeaveBean> leaveDay = new ArrayList<LeaveBean>();
			List<EvectionBean> evectionDay = new ArrayList<EvectionBean>();  
			String referrenceDate = periods.get(i).getDutyDate();
			
			for (LeaveBean leave : ruleInfoBean.getLeaves()) {
				if(referrenceDate.substring(0, 10).equals(leave.getLeaveStartDate().substring(0, 10)) ||
						referrenceDate.substring(0, 10).equals(leave.getLeaveReturnDate().substring(0, 10))){
					leaveDay.add(leave);
				}
			}
			for (EvectionBean evection : ruleInfoBean.getEvections()) {
				if(referrenceDate.substring(0, 10).equals(evection.getEvectionStartDate().substring(0, 10))||
						referrenceDate.substring(0, 10).equals(evection.getEvectionReturnDate().substring(0, 10))){
					evectionDay.add(evection);
				}
			}
			for (int j = 0; j < annals.size();) {
				if(!referrenceDate.equals(annals.get(j).getDutyDate())){
					break;
				}
				annalByDay.add(annals.get(j).getCardAnnalTime().substring(11));
				annals.remove(j);
			}
			WorkRuleListReportBean reportBean = statCardAnnalByDay(annalByDay, leaveDay, evectionDay, ruleInfoBean,id,periods.get(i));
			reportBean.setLateAmerce(
					reportBean.getLateCount()*ruleInfoBean.getWorkRuleBean().getOverdueAmerce());
			reportBean.setLeaveEarlyAmerce(
					reportBean.getLeaveEarlyCount()*ruleInfoBean.getWorkRuleBean().getLeaveEarlyAmerce());
			reportBean.setAbsentAmerce(
					reportBean.getAbsentWorkCount()*ruleInfoBean.getWorkRuleBean().getAbsentWorkAmerce());
			
			result = addWorkRuleReportByEmpByDay(reportBean);
		}
		return result;
	}


	public WorkRuleListReportBean statCardAnnalByDay(final List annalDay,List<LeaveBean> leaveDay,
			List<EvectionBean> evectionDay,EmployeeWorkRuleInfoBean infoBean,final String id,
			DutyPeriodsBean periods){
		WorkRuleListReportBean reportBean = new WorkRuleListReportBean();
		reportBean.setReportNo(getClassCode(IDGenerater.getId()));
		reportBean.setEmployeeNo(infoBean.getEmployeeNo());
		reportBean.setWorkDate(periods.getDutyDate());
		reportBean.setCreateBy(id);
		
		
		if(annalDay.size() < 1){
			reportBean.setAbsentWorkCount(1);
			reportBean.setAbsentWorkDay(1);
			return reportBean;
		}
		String format = "yyyy-MM-dd HH:mm:ss";
		int count = 0;
		List<SquadSect> squadSects = periods.getSquadSects();
		int dutyHour = 0;
		for (SquadSect sect : squadSects) {
			dutyHour += verdictStrTime(sect.getOffDutyTime(), sect.getOnDutyTime(), "HH:mm:ss", "hour");
		}
		
		for (LeaveBean leave : leaveDay) {
			for (SquadSect sqSect : squadSects) {
				String onDutyTime = reportBean.getWorkDate()+" "+sqSect.getOnDutyTime();
				String offDutyTime = reportBean.getWorkDate()+" "+sqSect.getOffDutyTime();
				if((onDutyTime.equals(leave.getLeaveStartDate()) || 
						verdictStrDate(leave.getLeaveStartDate(),onDutyTime,format)) && 
						(offDutyTime.equals(leave.getLeaveReturnDate()) || 
						verdictStrDate(offDutyTime, leave.getLeaveReturnDate(), format))){
					sqSect.setStatusId(1);
					double leaveDayTemp = reportBean.getLeaveDay() + verdictStrTime(leave.getLeaveReturnDate(), leave.getLeaveStartDate(), format, "hour");
					reportBean.setLeaveDay(leaveDayTemp > dutyHour?dutyHour:leaveDayTemp);
				}
				else if(verdictStrDate(onDutyTime, leave.getLeaveStartDate(), format) && 
						verdictStrDate(leave.getLeaveStartDate(),offDutyTime, format)){
					sqSect.setOffDutyTime(leave.getLeaveStartDate().substring(11));
					double leaveDayTemp = reportBean.getLeaveDay() + verdictStrTime(leave.getLeaveReturnDate(), leave.getLeaveStartDate(), format, "hour");
					reportBean.setLeaveDay(leaveDayTemp > dutyHour?dutyHour:leaveDayTemp);
				}
				else if(verdictStrDate(onDutyTime, leave.getLeaveReturnDate(), format) &&
						verdictStrDate(leave.getLeaveReturnDate(), offDutyTime, format)){
					sqSect.setOnDutyTime(leave.getLeaveReturnDate().substring(11));
					double leaveDayTemp = reportBean.getLeaveDay() + verdictStrTime(leave.getLeaveReturnDate(), leave.getLeaveStartDate(), format, "hour");
					reportBean.setLeaveDay(leaveDayTemp > dutyHour?dutyHour:leaveDayTemp);
				}
			}
			reportBean.setLeaveCount(++count);
		}
		count = 0;
		
		for (EvectionBean evection : evectionDay) {
			for (SquadSect sect : squadSects) {
				String onDutyTime = reportBean.getWorkDate()+" "+sect.getOnDutyTime();
				String offDutyTime = reportBean.getWorkDate()+" "+sect.getOffDutyTime();
				if((onDutyTime.equals(evection.getEvectionStartDate()) || 
						verdictStrDate(evection.getEvectionStartDate(), onDutyTime, format)) && 
						(offDutyTime.equals(evection.getEvectionReturnDate()) || 
						verdictStrDate(offDutyTime, evection.getEvectionReturnDate(), format))){
					sect.setStatusId(1);
					double evectionDayTemp = reportBean.getEvectionDay() + 
						verdictStrTime(evection.getEvectionReturnDate(), evection.getEvectionStartDate(), format, "hour");
					reportBean.setEvectionDay(evectionDayTemp > dutyHour?dutyHour:evectionDayTemp);
				}
				else if(verdictStrDate(onDutyTime, evection.getEvectionStartDate(), format) &&
						verdictStrDate(evection.getEvectionStartDate(), offDutyTime, format)){
					sect.setOffDutyTime(evection.getEvectionStartDate().substring(11));
					double evectionDayTemp = reportBean.getEvectionDay() + 
						verdictStrTime(evection.getEvectionReturnDate(), evection.getEvectionStartDate(), format, "hour");
					reportBean.setEvectionDay(evectionDayTemp > dutyHour?dutyHour:evectionDayTemp);
				}
				else if(verdictStrDate(onDutyTime, evection.getEvectionReturnDate(), format) && 
						verdictStrDate(evection.getEvectionReturnDate(), offDutyTime, format)){
					sect.setOnDutyTime(evection.getEvectionReturnDate().substring(11));
					double evectionDayTemp = reportBean.getEvectionDay() + 
						verdictStrTime(evection.getEvectionReturnDate(), evection.getEvectionStartDate(), format, "hour");
					reportBean.setEvectionDay(evectionDayTemp > dutyHour?dutyHour:evectionDayTemp);
				}
			}
			reportBean.setEvectionCount(++count);
		}

		WorkRuleBean ruleBean = infoBean.getWorkRuleBean();
		
		
		for (int i = 0; i < squadSects.size();i++) {
			int lateCount = 0;
			int lateMinute = 0;
			int leaveEarlyCount = 0;
			int leaveEarlyMinute = 0;
			int absentWorkCount = 0;
			double absentWorkDay = 0;
			SquadSect squadSect = squadSects.get(i);
			if(1 == squadSect.getStatusId() || "2" == squadSect.getSquadSectType()){
				continue;
			}
			if(2 <= annalDay.size()){
				
				Boolean isOnDutyInGear = true,isOffDutyInGear = true;
				for (int j = 0; j < annalDay.size(); j++) {
					String annalSect = annalDay.get(j).toString();
					
					if(0 != squadSect.getOnDutyAvailabilityTime()){
						
						if(verdictStrDate(annalSect, returnDate(squadSect.getOnDutyTime(), 
								ruleBean.getOverdueMinute(), "add"),format) && verdictStrDate(returnDate(squadSect.getOnDutyTime(), 
										squadSect.getOnDutyAvailabilityTime(), "minus"), annalSect,format)){
							annalDay.remove(annalSect);
							isOnDutyInGear = false;
							break;
						}
					}else{
						if(verdictStrDate(annalSect, returnDate(squadSect.getOnDutyTime(), 
								ruleBean.getOverdueMinute(), "add"),format)){
							annalDay.remove(annalSect);
							isOnDutyInGear = false;
							break;
						}
					}
				}
				if(isOnDutyInGear){
					for (int j = 0; j < annalDay.size(); j++) {
						String annalSect = annalDay.get(j).toString();
	  
						if(verdictStrDate(annalSect, returnDate(squadSect.getOnDutyTime(),
								ruleBean.getAbsentWorkMinute(), "add"),format) && verdictStrDate(returnDate(squadSect.getOnDutyTime(),
										ruleBean.getOverdueMinute(), "add"), annalSect, format)){
							++lateCount;
							annalDay.remove(annalSect);
							lateMinute = (int)verdictStrTime(annalSect, returnDate(squadSect.getOnDutyTime(), ruleBean.getOverdueMinute(), "add"), format, "minute");
							isOnDutyInGear = false;
							break;
						}
					}
				}
				
				for (int j = 0; j < annalDay.size(); j++) {
					String annalSect = annalDay.get(j).toString();
					
					if(0 != squadSect.getOffDutyAvailabilityTime()){
						
						
						if(verdictStrDate(returnDate(squadSect.getOffDutyTime(), ruleBean.getLeaveEarlyMinute(), "minus"),
								annalSect, format) && verdictStrDate(annalSect, 
										returnDate(squadSect.getOffDutyTime(), squadSect.getOffDutyAvailabilityTime(), "add"), format)){
							annalDay.remove(annalSect);
							isOffDutyInGear = false;
							break;
						}
					}else{
						
						if(verdictStrDate(returnDate(squadSect.getOffDutyTime(), ruleBean.getLeaveEarlyMinute(), "minus"),
								annalSect, format) ){
							annalDay.remove(annalSect);
							isOffDutyInGear = false;
							break;
						}
					}
					
				}
				
				if(isOffDutyInGear){
					for (int j = 0; j < annalDay.size(); j++) {
						String annalSect = annalDay.get(j).toString();
	
						if(verdictStrDate(returnDate(squadSect.getOffDutyTime(), ruleBean.getAbsentWorkMinute(), "minus"), 
								annalSect, format) && verdictStrDate(annalSect, returnDate
										(squadSect.getOffDutyTime(), ruleBean.getLeaveEarlyMinute(), "minus"), format)){
							++leaveEarlyCount;
							leaveEarlyMinute = (int)verdictStrTime(returnDate(squadSect.getOffDutyTime(), 
									ruleBean.getLeaveEarlyMinute(), "minus"), annalSect, format, "minute");
							annalDay.remove(annalSect);
							isOffDutyInGear = false;
							break;
						}
					}
				}
				if(isOffDutyInGear || isOnDutyInGear){
					for (int j = 0; j < annalDay.size(); j++) {
						String annalSect = annalDay.get(j).toString();
						 
						if(verdictStrDate(returnDate(squadSect.getOnDutyTime(), ruleBean.getAbsentWorkMinute(), "add"),
								annalSect, format) || verdictStrDate(annalSect, returnDate
										(squadSect.getOffDutyTime(), ruleBean.getAbsentWorkMinute(), "minus"), format) || 
										verdictStrDate(annalSect, returnDate(squadSect.getOnDutyTime(), squadSect.getOnDutyAvailabilityTime(), "minus"), format) || 
										verdictStrDate(returnDate(squadSect.getOffDutyTime(), squadSect.getOffDutyAvailabilityTime(), "add"), annalSect, format)){
							++absentWorkCount;
							absentWorkDay += 0.5;
							lateCount = 0;
							leaveEarlyCount = 0;
							lateMinute = 0;
							leaveEarlyMinute = 0;
							annalDay.remove(annalSect);
							break;
						}
					}
				}
			}else{
				++absentWorkCount; 
				absentWorkDay += 0.5;
			}
			absentWorkCount = reportBean.getAbsentWorkCount() + absentWorkCount > 1?
					1: reportBean.getAbsentWorkCount() + absentWorkCount;
			absentWorkDay = reportBean.getAbsentWorkDay() + absentWorkDay > 1?
					1:reportBean.getAbsentWorkDay() + absentWorkDay;
			reportBean.setLateCount(reportBean.getLateCount() + lateCount);
			reportBean.setLateMinute(reportBean.getLateMinute() + lateMinute);
			reportBean.setLeaveEarlyCount(reportBean.getLeaveEarlyCount() + leaveEarlyCount);
			reportBean.setLeaveEarlyMinute(reportBean.getLeaveEarlyMinute() + leaveEarlyMinute);
			reportBean.setAbsentWorkCount(absentWorkCount);
			reportBean.setAbsentWorkDay(absentWorkDay);
		}
		return reportBean;
		
	}
	

	public double verdictStrTime(String str1,String str2,String format,String timeType){
		double result = 0;
		SimpleDateFormat simFormat = new SimpleDateFormat(format);
		try{
			Date date1 = simFormat.parse(str1);
			Date date2 = simFormat.parse(str2);
			if("minute".equals(timeType)){
				result = Math.abs((double)(date1.getTime() - date2.getTime())/(1000*60));
			}
			else if("hour".equals(timeType)){
				result = Math.abs((double)(date1.getTime() - date2.getTime())/(1000*60*60));
			}
			else if("day".equals(timeType)){
				result = Math.abs((double)(date1.getTime() - date2.getTime())/(1000*60*60*24));
			
				String tempNumber = String.valueOf(result);
				int index = tempNumber.indexOf(".");
				Double douHead = Double.parseDouble(tempNumber.substring(0, index));
				Double douTemp = Double.parseDouble("0"+tempNumber.substring(index,index+2));
			
				if(Double.parseDouble("0.5") >= douTemp && Double.parseDouble("0.0") < douTemp){
					result = douHead + 0.5;
				}
				else if(Double.parseDouble("0.5") <= douTemp){
					result = douHead + 1;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Boolean verdictStrDate(String str1,String str2,String format){
		SimpleDateFormat simFormat = new SimpleDateFormat(format);
		try {
			Date date1 = simFormat.parse(str1);
			Date date2 = simFormat.parse(str2);
			if(date1.before(date2)){
				return true;
			}
		} catch (Exception e) {
		}

		return false;
	}


	public String returnDate(String date,int availabilityTime,String equType){
		Date returnDate = null;
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		try{
			returnDate = format.parse(date);
			int minutes = returnDate.getMinutes();
			if("minus".equals(equType)){
				returnDate.setMinutes(minutes-availabilityTime);
			}else{
				returnDate.setMinutes(minutes+availabilityTime);
			}
		}catch (Exception e) {
		}
		return format.format(returnDate);
	}


	public String getClassCode(String id){
		StringBuffer classCode = new StringBuffer("LR");
		classCode.append(new SimpleDateFormat("yyMMdd").format(new Date()));
		classCode.append(id.substring(id.length()-4));
		return classCode.toString();

	}

	
	public Result addWorkRuleReportByEmpByDay(final WorkRuleListReportBean reportBean){
		final StringBuffer sql = new StringBuffer();
		final Result result = new Result();
		sql.append("insert into tblWorkRuleListReport (id,workRuleListReportNo,employeeNo,lateCount,lateTime,LeaveEarlyCount,LeaveEarlyTime, ");
		sql.append("AbsentWorkCount,AbsentWorkDay,LeaveCount,LeaveDay,EvectionCount,EvectionDay,workDate,createBy,lateAmerce,leaveEarlyAmerce,absentAmerce) ");
		sql.append("values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		int retCode =DBUtil.execute(new IfDB(){
			@Override
			public int exec(Session session) {
				session.doWork(new Work(){
					public void execute(Connection con) throws SQLException {
						try {
							PreparedStatement ps = con.prepareStatement(sql.toString());
							ps.setString(1, IDGenerater.getId());
							ps.setString(2, reportBean.getReportNo());
							ps.setString(3, reportBean.getEmployeeNo());
							ps.setInt(4, reportBean.getLateCount());
							ps.setInt(5, reportBean.getLateMinute());
							ps.setInt(6, reportBean.getLeaveEarlyCount());
							ps.setInt(7, reportBean.getLeaveEarlyMinute());
							ps.setInt(8, reportBean.getAbsentWorkCount());
							ps.setDouble(9, reportBean.getAbsentWorkDay());
							ps.setInt(10, reportBean.getLeaveCount());
							ps.setDouble(11, reportBean.getLeaveDay());
							ps.setInt(12, reportBean.getEvectionCount());
							ps.setDouble(13, reportBean.getEvectionDay());
							ps.setString(14, reportBean.getWorkDate());
							ps.setString(15, reportBean.getCreateBy());
							ps.setDouble(16, reportBean.getLateAmerce());
							ps.setDouble(17, reportBean.getLeaveEarlyAmerce());
							ps.setDouble(18, reportBean.getAbsentAmerce());
							result.setRealTotal(ps.executeUpdate()+result.getRealTotal()) ;
						} catch (Exception e) {
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(e.getMessage());
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}


	public Result findEmployeeAll(String id){
		final String sql = "select id from tblEmployee";
		final Result result = new Result();
		final List employeeNos = new ArrayList();
		int retCode = DBUtil.execute(new IfDB(){
			@Override
			public int exec(Session session) {
				session.doWork(new Work(){
					public void execute(Connection con) throws SQLException {
						try {
							Statement state = con.createStatement();
							ResultSet rs = state.executeQuery(sql);
							
							while(rs.next()){
								employeeNos.add(rs.getString("id"));
							}
						} catch (Exception e) {
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(e.getMessage());
						}
					}
				});
				return result.getRetCode();
			}
		});
		for (Object object : employeeNos) {
			result.setRealTotal(result.getRealTotal()+statCardAnnalByEmpNo(object.toString(),id).getRealTotal());
		}
		result.setRetCode(retCode);
		return result;
	}
	

	public Result searchListReport(WorkRuleListReportForm reportBean,int startNo,int endNo){
		final Result result = new Result();
		final StringBuffer sql = new StringBuffer();
		final StringBuffer sqlCount = new StringBuffer();
		sqlCount.append("select count(1) from tblWorkRuleListReport where 1=1 ");
		sql.append("select * from (");
		sql.append("select row_number() over(order by workDate desc) as row_No,workRuleListReportNo,emp.empFullName,lateCount,lateTime,lateAmerce,LeaveEarlyCount,leaveEarlyTime,leaveEarlyAmerce,");
		sql.append("AbsentWorkCount,AbsentWorkDay,absentAmerce,LeaveCount,leaveDay,EvectionCount,evectionDay,workDate ");
		sql.append("from tblWorkRuleListReport as lr ");
		sql.append("left join tblEmployee as emp on emp.id = lr.employeeNo " );
		sql.append("where 1=1 ");
		if(!"".equals(reportBean.getEmployeeNo()) && null != reportBean.getEmployeeNo()){
			sqlCount.append(" and employeeNo = '"+reportBean.getEmployeeNo()+"' ");
			sql.append(" and employeeNo = '"+reportBean.getEmployeeNo()+"' ");
		}
		if(!"".equals(reportBean.getLateCount()) && null != reportBean.getLateCount()){
			sqlCount.append(" and lateCount = " + getIntByString(reportBean.getLateCount())+" ");
			sql.append(" and lateCount = " + getIntByString(reportBean.getLateCount())+" ");
		}
		if(!"".equals(reportBean.getLateMinute()) && null != reportBean.getLateMinute()){ 
			sqlCount.append(" and lateTime = " + getIntByString(reportBean.getLateMinute())+" ");
			sql.append(" and lateTime = " + getIntByString(reportBean.getLateMinute())+" ");
		}
		if(!"".equals(reportBean.getLateAmerce()) && null != reportBean.getLateAmerce()){ 
			sqlCount.append(" and lateAmerce = " + getIntByString(reportBean.getLateAmerce())+" ");
			sql.append(" and lateAmerce = " + getIntByString(reportBean.getLateAmerce())+" ");
		}
		if(!"".equals(reportBean.getLeaveEarlyCount()) && null != reportBean.getLeaveEarlyCount()){
			sqlCount.append(" and LeaveEarlyCount = " + getIntByString(reportBean.getLeaveEarlyCount())+" ");
			sql.append(" and LeaveEarlyCount = " + getIntByString(reportBean.getLeaveEarlyCount())+" ");
		}
		if(!"".equals(reportBean.getLeaveEarlyMinute()) && null != reportBean.getLeaveEarlyMinute()){
			sqlCount.append(" and leaveEarlyTime = " + getIntByString(reportBean.getLeaveEarlyMinute())+" ");
			sql.append(" and leaveEarlyTime = " + getIntByString(reportBean.getLeaveEarlyMinute())+" ");
		}
		if(!"".equals(reportBean.getLeaveEarlyAmerce()) && null != reportBean.getLeaveEarlyAmerce()){
			sqlCount.append(" and leaveEarlyAmerce = " + getIntByString(reportBean.getLeaveEarlyAmerce())+" ");
			sql.append(" and leaveEarlyAmerce = " + getIntByString(reportBean.getLeaveEarlyAmerce())+" ");
		}
		if(!"".equals(reportBean.getAbsentWorkCount()) && null != reportBean.getAbsentWorkCount()){
			sqlCount.append(" and AbsentWorkCount = " + getIntByString(reportBean.getAbsentWorkCount())+" ");
			sql.append(" and AbsentWorkCount = " + getIntByString(reportBean.getAbsentWorkCount())+" ");
		}
		if(!"".equals(reportBean.getAbsentWorkDay()) && null != reportBean.getAbsentWorkDay()){
			sqlCount.append(" and AbsentWorkDay = " + Double.parseDouble(reportBean.getAbsentWorkDay()) +" ");
			sql.append(" and AbsentWorkDay = " + Double.parseDouble(reportBean.getAbsentWorkDay()) +" ");
		}
		if(!"".equals(reportBean.getAbsentAmerce()) && null != reportBean.getAbsentAmerce()){
			sqlCount.append(" and absentAmerce = " + Double.parseDouble(reportBean.getAbsentAmerce()) +" ");
			sql.append(" and absentAmerce = " + Double.parseDouble(reportBean.getAbsentAmerce()) +" ");
		}
		if(!"".equals(reportBean.getLeaveCount()) && null != reportBean.getLeaveCount()){
			sqlCount.append(" and LeaveCount = " + getIntByString(reportBean.getLeaveCount())+" ");
			sql.append(" and LeaveCount = " + getIntByString(reportBean.getLeaveCount())+" ");
		}
		if(!"".equals(reportBean.getLeaveDay()) && null != reportBean.getLeaveDay()){
			sqlCount.append(" and leaveDay = " + Double.parseDouble(reportBean.getLeaveDay())+" ");
			sql.append(" and leaveDay = " + Double.parseDouble(reportBean.getLeaveDay())+" ");
		}
		if(!"".equals(reportBean.getEvectionCount()) && null != reportBean.getEvectionCount()){
			sqlCount.append(" and EvectionCount = " + getIntByString(reportBean.getEvectionCount())+" ");
			sql.append(" and EvectionCount = " + getIntByString(reportBean.getEvectionCount())+" ");
		}
		if(!"".equals(reportBean.getEvectionDay()) && null != reportBean.getEvectionDay()){
			sqlCount.append(" and evectionDay = " + Double.parseDouble(reportBean.getEvectionDay())+" ");
			sql.append(" and evectionDay = " + Double.parseDouble(reportBean.getEvectionDay())+" ");
		}
		
		sqlCount.append(" and workDate >= '" + reportBean.getStartDate() + "' and workDate <= '"+reportBean.getEndDare()+"' " );
		sql.append(" and workDate >= '" + reportBean.getStartDate() + "' and workDate <= '"+reportBean.getEndDare()+"' " );
		
		sql.append(" ) as temp ");
		sql.append(" where row_No between "+startNo+" and "+endNo);
		
		int retCode = DBUtil.execute(new IfDB(){
			@Override
			public int exec(Session session) {
				session.doWork(new Work(){
					public void execute(Connection con) throws SQLException {
						try {
							Statement statement = con.createStatement();
							ResultSet rs = statement.executeQuery(sql.toString());
							List<WorkRuleListReportForm> listReports = new ArrayList<WorkRuleListReportForm>();
							while(rs.next()){
								listReports.add(new WorkRuleListReportForm(
										rs.getString("workRuleListReportNo"),
										rs.getString("empFullName"),
										rs.getString("lateCount"),
										rs.getString("lateTime"),
										String.valueOf(rs.getDouble("lateAmerce")),
										rs.getString("LeaveEarlyCount"),
										rs.getString("leaveEarlyTime"),
										String.valueOf(rs.getDouble("leaveEarlyAmerce")),
										rs.getString("AbsentWorkCount"),
										String.valueOf(rs.getDouble("AbsentWorkDay")),
										String.valueOf(rs.getDouble("absentAmerce")),
										rs.getString("LeaveCount"),
										String.valueOf(rs.getDouble("leaveDay")),
										rs.getString("EvectionCount"),
										String.valueOf(rs.getDouble("evectionDay")),
										rs.getString("workDate")
										));
							}
							result.setRetVal(listReports);
							Statement state = con.createStatement();
							rs = state.executeQuery(sqlCount.toString());
							if(rs.next()){
								result.setTotalPage(Integer.parseInt(rs.getString(1)));
							}
							
						} catch (Exception e) {
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(e.getMessage());
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	

	public Result statDutyAnnalByDate(final String startDate,final String endDate,final String employeeNo,final int startNo,final int endNo){
		final Result result = new Result();
		final String sql = "{call proc_statDutyAnnal (?,?,?,?,?,?)}";
		int retCode = DBUtil.execute(new IfDB(){
			@Override
			public int exec(Session session) {
				session.doWork(new Work(){
					public void execute(Connection con){
						try{
							
							CallableStatement cs = con.prepareCall(sql);
							cs.setString(1, startDate);
							cs.setString(2, endDate);
							cs.setString(3, employeeNo);
							cs.setString(4, String.valueOf(startNo));
							cs.setString(5, String.valueOf(endNo));
							cs.registerOutParameter(6, java.sql.Types.INTEGER);
							ResultSet rs = cs.executeQuery();
							
							List<StatWorkRukeListReportBean> stats = 
								new ArrayList<StatWorkRukeListReportBean>();
							while (rs.next()){
								stats.add(new StatWorkRukeListReportBean(
										rs.getString("employeeNo"),
										rs.getString("EmpFullName"),
										rs.getInt("lateCount"),
										rs.getDouble("lateTime"),
										rs.getDouble("lateAmerce"),
										rs.getInt("LeaveEarlyCount"),
										rs.getDouble("leaveEarlyTime"),
										rs.getDouble("leaveEarlyAmerce"),
										rs.getInt("AbsentWorkCount"),
										rs.getDouble("AbsentWorkDay"),
										rs.getDouble("absentAmerce"),
										rs.getInt("LeaveCount"),
										rs.getDouble("leaveDay"),
										rs.getInt("EvectionCount"),
										rs.getDouble("evectionDay")
										));
							}
							result.setTotalPage(cs.getInt(6));
							result.setRetVal(stats);
						}catch (Exception e) {
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(e.getMessage());
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	public Result getStartDate(final String employeeNo){
		final String sql ="select isnull(max(workDate),'') as workDate from tblWorkRuleListReport where employeeNo = ?";
		final Result result = new Result();
		DBUtil.execute(new IfDB(){
			@Override
			public int exec(Session session) {
				session.doWork(new Work(){
					public void execute(Connection con) {
						try {
							PreparedStatement ps = con.prepareStatement(sql);
							ps.setString(1, employeeNo);
							ResultSet rs = ps.executeQuery();
							if(rs.next()){
								result.setRetVal(rs.getString("workDate"));
							}
						} catch (Exception e) {
							BaseEnv.log.debug(e.getMessage());
						}
					}
				});
				return 0;
			}
		});
		return result;
	}
	
	public List getSumPageList(int pageSum,int pageSize){
		List pageSumList = new ArrayList();
		int sumPage = pageSum%pageSize == 0?pageSum/pageSize:pageSum/pageSize+1;
		for(int i = 1;i <= sumPage;i++){
			pageSumList.add(i);
		}
		
		return pageSumList;
	}
	
	public Result insertCardAnnals(final List<BrushCardAnnalBean> annals,final String createBy){
		final Result result = new Result();
		
		final String sql = "insert into tblBrushCardAnnal (id,dutyCardTime,employeeNo,createBy,createTime) values (?,?,?,?,convert(varchar(19),getdate(),120))";
		
		int retCode = DBUtil.execute(new IfDB(){
			@Override
			public int exec(Session session) {
				session.doWork(new Work(){
					public void execute(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(sql);
						try {
							for (BrushCardAnnalBean bean : annals) {
								ps.setString(1, IDGenerater.getId());
								ps.setString(2, bean.getCardAnnalTime());
								ps.setString(3, bean.getEmployeeNo());
								ps.setString(4, createBy);
								ps.executeUpdate();
							}
							
						} catch (Exception e) {
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(e.getMessage());
						}
						
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	
	public Result validateEmployeeNo(final List<BrushCardAnnalBean> annals){
		final Result result = new Result();
		final String sql = "select count(0) from tblEmployee where EmpNumber = ?";
		
		DBUtil.execute(new IfDB(){
			@Override
			public int exec(Session session) {
				session.doWork(new Work(){
					public void execute(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(sql);
						Set<String> employeeNos = new HashSet<String>();
						for (BrushCardAnnalBean bean : annals) {
							ps.setString(1, bean.getEmployeeNo());
							ResultSet rs = ps.executeQuery();
							int ret = 0;
							if(rs.next()){
								ret = Integer.parseInt(rs.getString(1));
							}
							if(ret == 0){
								employeeNos.add(bean.getEmployeeNo());
							}
						}
						result.setRetVal(employeeNos);
					}
				});
				return 0;
			}
		});
		return result;
	}
	
	public int getIntByString(String str){
		
		int temp = -1;
		try {
			temp = Integer.parseInt(str);
		} catch (Exception e) {
		}
		
		return temp;
	}
}
