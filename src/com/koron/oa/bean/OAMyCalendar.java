package com.koron.oa.bean;
import java.util.*;
public class OAMyCalendar{
   private String id;
   private String classcode;
   private String calendardate;
   private String calendartype;
   private String calendartitle;
   private String calendarcontext;
   private String wakeupbegindate;
   private String nowwakeupbegindate;
   private int isperiodicity;
   private String periodicityunit;
   private int periodicity;
   private int nowwaleuptimes;
   private int setp;
   private String wakeuptype;
   private int waleuptimes;
   private String createby;
   private String lastupdateby;
   private String createtime;
   private String lastupdatetime;
   private int statusid;
   private String scompanyid;
   private int beforeday;
   private int beforehour;
   private int beforeminute;
   private String assClient ;
   private String assPeople ;
   
   public void setid(String id){
        this.id=id;
     }
   public String getid ( ){
      return id;
     }
   public void setclassCode(String classcode){
        this.classcode=classcode;
     }
   public String getclassCode ( ){
      return classcode;
     }
   public void setCalendarDate(String calendardate){
        this.calendardate=calendardate;
     }
   public String getCalendarDate ( ){
      return calendardate;
     }
   public void setCalendarType(String calendartype){
        this.calendartype=calendartype;
     }
   public String getCalendarType ( ){
      return calendartype;
     }
   public void setCalendarTitle(String calendartitle){
        this.calendartitle=calendartitle;
     }
   public String getCalendarTitle ( ){
      return calendartitle;
     }
   public void setCalendarContext(String calendarcontext){
        this.calendarcontext=calendarcontext;
     }
   public String getCalendarContext ( ){
      return calendarcontext;
     }
   public void setWakeUpBeginDate(String wakeupbegindate){
        this.wakeupbegindate=wakeupbegindate;
     }
   public String getWakeUpBeginDate ( ){
      return wakeupbegindate;
     }
   public void setNowWakeUpBeginDate(String nowwakeupbegindate){
        this.nowwakeupbegindate=nowwakeupbegindate;
     }
   public String getNowWakeUpBeginDate ( ){
      return nowwakeupbegindate;
     }
   public void setIsPeriodicity(int isperiodicity){
        this.isperiodicity=isperiodicity;
     }
   public int getIsPeriodicity ( ){
      return isperiodicity;
     }
   public void setPeriodicityUnit(String periodicityunit){
        this.periodicityunit=periodicityunit;
     }
   public String getPeriodicityUnit ( ){
      return periodicityunit;
     }
   public void setPeriodicity(int periodicity){
        this.periodicity=periodicity;
     }
   public int getPeriodicity ( ){
      return periodicity;
     }
   public void setNowWaleUpTimes(int nowwaleuptimes){
        this.nowwaleuptimes=nowwaleuptimes;
     }
   public int getNowWaleUpTimes ( ){
      return nowwaleuptimes;
     }
   public void setSetp(int setp){
        this.setp=setp;
     }
   public int getSetp ( ){
      return setp;
     }
   public void setWakeUpType(String wakeuptype){
        this.wakeuptype=wakeuptype;
     }
   public String getWakeUpType ( ){
      return wakeuptype;
     }
   public void setWaleUpTimes(int waleuptimes){
        this.waleuptimes=waleuptimes;
     }
   public int getWaleUpTimes ( ){
      return waleuptimes;
     }
   public void setcreateBy(String createby){
        this.createby=createby;
     }
   public String getcreateBy ( ){
      return createby;
     }
   public void setlastUpdateBy(String lastupdateby){
        this.lastupdateby=lastupdateby;
     }
   public String getlastUpdateBy ( ){
      return lastupdateby;
     }
   public void setcreateTime(String createtime){
        this.createtime=createtime;
     }
   public String getcreateTime ( ){
      return createtime;
     }
   public void setlastUpdateTime(String lastupdatetime){
        this.lastupdatetime=lastupdatetime;
     }
   public String getlastUpdateTime ( ){
      return lastupdatetime;
     }
   public void setstatusId(int statusid){
        this.statusid=statusid;
     }
   public int getstatusId ( ){
      return statusid;
     }
   public void setSCompanyID(String scompanyid){
        this.scompanyid=scompanyid;
     }
   public String getSCompanyID ( ){
      return scompanyid;
     }
   public void setBeforeDay(int beforeday){
        this.beforeday=beforeday;
     }
   public int getBeforeDay ( ){
      return beforeday;
     }
   public void setBeforeHour(int beforehour){
        this.beforehour=beforehour;
     }
   public int getBeforeHour ( ){
      return beforehour;
     }
   public void setBeforeMinute(int beforeminute){
        this.beforeminute=beforeminute;
     }
   public int getBeforeMinute ( ){
      return beforeminute;
     }
	public String getAssClient() {
		return assClient;
	}
	public void setAssClient(String assClient) {
		this.assClient = assClient;
	}
	public String getAssPeople() {
		return assPeople;
	}
	public void setAssPeople(String assPeople) {
		this.assPeople = assPeople;
	}
   
}

