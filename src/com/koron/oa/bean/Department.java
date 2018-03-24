package com.koron.oa.bean;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: ¿ÆÈÙÈí¼þ</p>
 *
 * @author À×ÓÀ»Ô
 * @version 1.0
 */
public class Department {
    private String id;
      private String deptcode;
      private String deptfullname;
      private String departmentname;
      private String manageritel;
      private String contactor;
      private String contacttel;
      private String remark;
      private float topcost;
      private float topcredit;
      private String createby;
      private String lastupdateby;
      private String createtime;
      private String lastupdatetime;
      private String classcode;
      private String departmentmanager;
      private String attachment;
      private String scompanyid;
      private String rowon;
      private int    departmentweave;
      public void setid(String id){
           this.id=id;
        }
      public String getid ( ){
         return id;
        }
      public void setDeptCode(String deptcode){
           this.deptcode=deptcode;
        }
      public String getDeptCode ( ){
         return deptcode;
        }
      public void setDeptFullName(String deptfullname){
           this.deptfullname=deptfullname;
        }
      public String getDeptFullName ( ){
         return deptfullname;
        }
      public void setDepartmentName(String departmentname){
           this.departmentname=departmentname;
        }
      public String getDepartmentName ( ){
         return departmentname;
        }
      public void setManagerITel(String manageritel){
           this.manageritel=manageritel;
        }
      public String getManagerITel ( ){
         return manageritel;
        }
      public void setContactor(String contactor){
           this.contactor=contactor;
        }
      public String getContactor ( ){
         return contactor;
        }
      public void setContactTel(String contacttel){
           this.contacttel=contacttel;
        }
      public String getContactTel ( ){
         return contacttel;
        }
      public void setRemark(String remark){
           this.remark=remark;
        }
      public String getRemark ( ){
         return remark;
        }
      public void setTopCost(float topcost){
           this.topcost=topcost;
        }
      public float getTopCost ( ){
         return topcost;
        }
      public void setTopCredit(float topcredit){
           this.topcredit=topcredit;
        }
      public float getTopCredit ( ){
         return topcredit;
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
      public void setclassCode(String classcode){
           this.classcode=classcode;
        }
      public String getclassCode ( ){
         return classcode;
        }
      public void setdepartmentManager(String departmentmanager){
           this.departmentmanager=departmentmanager;
        }
      public String getdepartmentManager ( ){
         return departmentmanager;
        }
      public void setAttachment(String attachment){
           this.attachment=attachment;
        }
      public String getAttachment ( ){
         return attachment;
        }
      public void setSCompanyID(String scompanyid){
           this.scompanyid=scompanyid;
        }
      public String getSCompanyID ( ){
         return scompanyid;
        }
      public void setRowON(String rowon){
           this.rowon=rowon;
        }
      public String getRowON ( ){
         return rowon;
     }
	public int getDepartmentweave() {
		return departmentweave;
	}
	public void setDepartmentweave(int departmentweave) {
		this.departmentweave = departmentweave;
	}
}
