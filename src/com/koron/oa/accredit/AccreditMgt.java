package com.koron.oa.accredit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.web.login.*;
import com.menyi.web.util.*;

/**
 * 
 * 
 * <p>Title:��Ȩmgt</p> 
 * <p>Description: </p>
 *
 * @Date:2012-8-27
 * @Copyright: �������
 * @Author ���ҿ�
 */
public class AccreditMgt extends AIODBManager{
	
	/**
	 * ��ȡ��������
	 * @return
	 */
	
	public Result queryData(AccreditSearchForm accForm, Integer types){

		List<String> param = new ArrayList<String>();
		String sql = "select id,deptCode,deptFullName,classCode,isCatalog from tblDepartment where statusid!='-1' and isnull(isPublic,0) <> 1 ";
		if(!"".equals(accForm.getValue()) && accForm.getValue() != null && !"null".equals(accForm.getValue())){
			sql += " and (classCode like '%"+accForm.getValue()+"%'"+" or deptCode like '%"+accForm.getValue()+"%'"+
			" or deptFullName like '%"+accForm.getValue()+"%'"+" or deptFullNamePYM like '%"+accForm.getValue()+"%')";
		}
		String deptCode2 = "";
		if(types == 2 && !"".equals(accForm.getUserCode())){
			Result result = queryDeptCode(accForm.getUserCode());
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				deptCode2 += (String)result.retVal;
			}
		}
		if(!"".equals(accForm.getParameterCode()) && accForm.getParameterCode() != null && !"null".equals(accForm.getParameterCode())){
			String codestr = deptCode2+accForm.getParameterCode();
			String[] codes = codestr.split(",");
			if(codes.length>0){
				sql += " and (";
				for(int i=0;i<codes.length;i++){
					sql += " classCode like '"+codes[i]+"%'";
					if(i<codes.length-1){
						sql += " or ";
					}
				}
				sql += ")";
			}
		}
		sql += " order by deptCode";
		return sqlList(sql, param);
	}
	
	
	/**
	 * ����������ѯ����
	 * @param type
	 * @param values
	 * @return
	 */
	public Result queryDeptData(AccreditSearchForm accForm){
		
		ArrayList<String> param = new ArrayList<String>();
		String sql = "select id,deptCode,deptFullName,classCode,isCatalog,ROW_NUMBER() over(order by classCode desc)" +
				" as row_id from tblDepartment where statusid!='-1'  and isnull(isPublic,0) <> 1  ";
		if(accForm.getKeyType() != null && "group".equals(accForm.getKeyType())){
			//�������ѯ��Ϣ
			sql += " and classCode='"+accForm.getValue()+"'";
		}else if(accForm.getKeyType() != null && "keyWord".equals(accForm.getKeyType())){
			//�ؼ��ֲ�ѯ
			sql += " and (classCode like '%"+accForm.getValue()+"%'"+" or deptCode like '%"+accForm.getValue()+"%'"
			+" or deptFullName like '%"+accForm.getValue()+"%'"+" or deptFullNamePYM like '%"+accForm.getValue()+"%')";
		}else if(accForm.getKeyType() != null && "letter".equals(accForm.getKeyType())){
			//������ĸ��ѯ����
			
		}else if(accForm.getKeyType() != null && "choose".equals(accForm.getKeyType())){
			//��ѡ����
			if(accForm.getValue()!=null && !"".equals(accForm.getValue())){
				String[] s = accForm.getValue().split(",");
				sql += "and ( classCode like '"+s[0]+"%'";
				for(int i=1;i<s.length;i++){
					sql += " or classCode like '"+s[i]+"%'";
				}
				sql+=")";
			}else{
				sql += " and 1=2";
			}
		}
		if(accForm.getParameterCode()!=null && !"null".equals(accForm.getParameterCode()) && !"".equals(accForm.getParameterCode())){
			String[] codes = accForm.getParameterCode().split(",");
			
			sql += "and ( classCode like '"+codes[0]+"%'";
			for(int i=1;i<codes.length;i++){
				sql += " or classCode like '"+codes[i]+"%'";
			}
			sql+=")";
		}
		return sqlList(sql, param);
	}
	
	
	/**
	 * ��ѯ��Ա��Ϣ
	 * @param accForm
	 * @param local
	 * @return
	 */
	public Result queryUserData(AccreditSearchForm accForm,String local){
		
		String sql = "select a.id,a.empNumber,a.empFullName,d.deptFullName,a.email,d.classCode,l."+local+",";
		sql += "ROW_NUMBER() over(order by a.empNumber) as row_id from tblemployee a left join ";
		sql += "(select enumitem.enumValue,enumitem.languageId from tblDBEnumerationItem enumitem,";
		sql += "tblDBEnumeration enum where enumitem.enumId=enum.id and enum.enumName='duty') as ss on ss.enumValue=a.titleId";
		sql += " left join tblLanguage as l on l.id=ss.languageId left join ";
		sql += "tblDepartment d on a.departmentCode=d.classCode where a.statusId!='-1'  and isnull(a.isPublic,0) <> 1  and a.SCompanyID='00001' ";
		
		if("noAdmin".equals(accForm.getCondition())){
			// zxy ,��ӦȨ�޲���ѡ��admin
			sql += " and a.id <>'1' ";
		}else if("openFlag".equals(accForm.getCondition())){
			//ֻ��ʾ���Խ���ϵͳ���û�
			sql += " and a.openFlag=1 "; 
		}else if("communicationNote".equals(accForm.getCondition())){
			//ͨѶ¼����
			sql += " and isnull(a.email,'')!='' ";
		}
		
		if(accForm.getKeyType() != null && "group".equals(accForm.getKeyType())){
			//���ݲ������ѯ����
			sql += " and a.departmentCode like '"+accForm.getValue()+"%'";
		}else if(accForm.getKeyType() != null && "manygroup".equals(accForm.getKeyType())){
			//ѡ������
			if(!"".equals(accForm.getValue())){
				sql += " and a.departmentCode in (";
				String[] s = accForm.getValue().split(",");
				for(int i = 0;i<s.length;i++){
					sql += "'"+s[i]+"'";
					if(i<s.length-1){
						sql += ",";
					}
				}
				sql += ")";
			}else{
				sql += " and 1=2";
			}
			
		}else if(accForm.getKeyType() != null && "keyWord".equals(accForm.getKeyType())){
			//�ؼ��ֲ�ѯ
			sql += " and (a.empNumber like '%"+accForm.getValue()+"%'";
			sql += " or a.empFullName like '%"+accForm.getValue()+"%'";
			sql += " or a.empFullNamePYM like '%"+accForm.getValue()+"%'";
			
			if("communicationNote".equals(accForm.getCondition())){
				sql += " or a.email like '%"+accForm.getValue()+"%'";
			}
			
			sql += " or d.deptFullName like '%"+accForm.getValue()+"%')";
		}else if(accForm.getKeyType() != null && "letter".equals(accForm.getKeyType())){
			//���պ��ֵ���ĸ��ѯ
			if(!"0".equals(accForm.getValue())){
				sql += " and (a.empFullName "+ edhSql(accForm.getValue(), "tblEmployee", "empFullName")+" or a.empFullName like '"+accForm.getValue()+"%')";
			}
			
		}else if(accForm.getKeyType() != null && "choose".equals(accForm.getKeyType())){
			//��ѡ�������
			if(accForm.getValue() != null && !"".equals(accForm.getValue())){
				sql += " and a.id in (";
				String[] s = accForm.getValue().split(",");
				for(int i = 0;i<s.length;i++){
					sql += "'"+s[i]+"'";
					if(i<s.length-1){
						sql += ",";
					}
				}
				sql += ")";
			}else{
				sql += " and 1=2";
			}
		}
		sql += " and (";
		if(accForm.getUserCode() != null && !"".equals(accForm.getUserCode())){
			sql += " a.id in (";
			String[] s = accForm.getUserCode().split(",");
			for(int i = 0;i<s.length;i++){
				sql += "'"+s[i]+"'";
				if(i<s.length-1){
					sql += ",";
				}
			}
			sql += ")";
		}
		
		if(!"".equals(accForm.getParameterCode()) && accForm.getParameterCode() != null){
			String[] codes = accForm.getParameterCode().split(",");
			if(accForm.getUserCode() != null && !"".equals(accForm.getUserCode())){
				sql += " or ";
			}
			for(int i=0;i<codes.length;i++){
				sql += " d.classCode like '"+codes[i]+"%'";
				if(i<codes.length-1){
					sql += " or ";
				}
			}
		}
		if(!(accForm.getUserCode() != null && !"".equals(accForm.getUserCode())) && !(!"".equals(accForm.getParameterCode()) && accForm.getParameterCode() != null)){
			sql += " 1=1";
		}
		sql += ")";
		return sqlListMaps(sql, null, accForm.getPageNo(), accForm.getPageSize());
	}
	
	
	
	/**
	 * ��ĸ��ѯ���÷���
	 * @param value		ֵ
	 * @param tableName		����
	 * @param tableField	�����ĸ��ֶ�����ѯ
	 * @return
	 */
	public String edhSql(String value, String tableName, String tableField){
		String sql = " in (select e."+tableField+" from "+tableName+" e," +
				"(select   '߹ '   chr, 'A '   letter   union   all   select   '�� ', 'B '   union   all"+
		        " select   '�� ', 'C '   union   all   select   '�� ', 'D '   union   all "+
		        " select   '�� ', 'E '   union   all   select   '�� ', 'F '   union   all "+
		        " select   '� ', 'G '   union   all   select   '�� ', 'H '   union   all "+
		        " select   'آ ', 'J '   union   all   select   '�� ', 'K '   union   all "+
		        " select   '�� ', 'L '   union   all   select   '�` ', 'M '   union   all "+
		        " select   '�� ', 'N '   union   all   select   '�� ', 'O '   union   all "+
		        " select   '�r ', 'P '   union   all   select   '�� ', 'Q '   union   all "+
		        " select   '�� ', 'R '   union   all   select   '�� ', 'S '   union   all "+
		        " select   '�� ', 'T '   union   all   select   '�� ', 'W '   union   all "+
		        " select   'Ϧ ', 'X '   union   all   select   'Ѿ ', 'Y '   union   all "+
		        " select   '�� ', 'Z ')   b where b.chr <=left(e."+tableField+",1) group by e."+tableField + 
		        " having   max(letter)= '"+value+"')";
		return sql;
	}
	
	
	
	/**
	 * ��ĸ��ѯ   ר�Ž���ͻ���ϵ   ���պ��ֵ���ĸ��ѯ     �Ľ����棺sql�Ż�
	 * @param value		ֵ
	 * @param tableName		����
	 * @param tableField	�����ĸ��ֶ�����ѯ
	 * @return
	 */
	public String optimizationSql(String value){
		String sql = " in (  select LEFT(e.userName,1) from CRMClientInfoDet e," +
				"(select   '߹ '   chr, 'A '   letter   union   all   select   '�� ', 'B '   union   all"+
		        " select   '�� ', 'C '   union   all   select   '�� ', 'D '   union   all "+
		        " select   '�� ', 'E '   union   all   select   '�� ', 'F '   union   all "+
		        " select   '� ', 'G '   union   all   select   '�� ', 'H '   union   all "+
		        " select   'آ ', 'J '   union   all   select   '�� ', 'K '   union   all "+
		        " select   '�� ', 'L '   union   all   select   '�` ', 'M '   union   all "+
		        " select   '�� ', 'N '   union   all   select   '�� ', 'O '   union   all "+
		        " select   '�r ', 'P '   union   all   select   '�� ', 'Q '   union   all "+
		        " select   '�� ', 'R '   union   all   select   '�� ', 'S '   union   all "+
		        " select   '�� ', 'T '   union   all   select   '�� ', 'W '   union   all "+
		        " select   'Ϧ ', 'X '   union   all   select   'Ѿ ', 'Y '   union   all "+
		        " select   '�� ', 'Z ')   b where b.chr <=left(e.userName,1) group by LEFT(e.userName,1) having   max(letter)= '"+value+"'    ";
		sql += "UNION  SELECT  '"+value+"'   )   ";
		
		return sql;
	}
	
	
	/**
	 * ��ѯְԱ��
	 * @param accForm
	 * @return
	 */
	public Result queryEmpData(AccreditSearchForm accForm){
		
		List<String> param = new ArrayList<String>();
		String sql = "select id,groupName,groupRemark from tblEmpGroup where 1=1";
		if(!"".equals(accForm.getValue()) && accForm.getValue() != null && !"null".equals(accForm.getValue())){
			sql += " and (id like '%"+accForm.getValue()+"%'"+" or groupName like '%"+accForm.getValue()+"%'"+" or groupRemark like '%"+accForm.getValue()+"%')";
		}
		
		if(!"".equals(accForm.getParameterCode()) && accForm.getParameterCode() != null && !"null".equals(accForm.getParameterCode())){
			String[] ids = accForm.getParameterCode().split(",");
			sql += " and id in (";
			for(int i=0;i<ids.length;i++){
				sql += "'"+ids[i]+"'";
				if(i<ids.length-1){
					sql += ",";
				}
			}
			sql += ")";
		}
		return sqlList(sql, param);
	}
	
	
	/**
	 * ��ѯ������
	 * @param accForm
	 * @return
	 */
	public Result queryEmpGroupData(AccreditSearchForm accForm){
		List<String> param = new ArrayList<String>();
		String sql = "select id,groupName,groupRemark from tblEmpGroup where 1=1";
		if(accForm.getKeyType() != null && "group".equals(accForm.getKeyType())){
			//�������ѯ��Ϣ
			sql += " and id='"+accForm.getValue()+"'";
		}else if(accForm.getKeyType() != null && "keyWord".equals(accForm.getKeyType())){
			//�ؼ��ֲ�ѯ
			sql += " and (id like '%"+accForm.getValue()+"%'"+" or groupName like '%"+accForm.getValue()+"%'"+" or groupRemark like '%"+accForm.getValue()+"%')";
		}else if(accForm.getKeyType() != null && "choose".equals(accForm.getKeyType())){
			//��ѡ����
			if(accForm.getValue()!=null && !"".equals(accForm.getValue())){
				String[] s = accForm.getValue().split(",");
				sql += " and id in (";
				for(int i=0;i<s.length;i++){
					sql += "'"+s[i]+"'";
					if(i<s.length-1){
						sql += ",";
					}
				}
				sql += " )";
			}else{
				sql += " and 1=2";
			}
		}
		if(accForm.getParameterCode()!=null && !"null".equals(accForm.getParameterCode()) && !"".equals(accForm.getParameterCode())){
			String[] codes = accForm.getParameterCode().split(",");
			sql += " and id in (";
			for(int i=0;i<codes.length;i++){
				sql += "'"+codes[i]+"'";
				if(i<codes.length-1){
					sql += ",";
				}
			}
			sql += ")";
		}
		return sqlList(sql, param);
	}
	
	/**
	 * ͨѶ¼��
	 * @param accForm
	 * @return
	 */
//	public Result queryCommunicationGroup(AccreditSearchForm accForm,String userId){
		
//		List<String> param = new ArrayList<String>();
//		String sql = "select bean.id,bean.classCode,bean.groupName,bean.description from OACommunicationNoteGroup bean,OACommunicationNoteInfo info where 1=1";
//		sql += " and (bean.createBy='"+userId+"'"+" or bean.IsPublic=2) and info.groupId=bean.id and isnull(info.email,'') != ''";
//		sql += " group by bean.id,bean.classCode,bean.groupName,bean.description";
//		return sqlList(sql, param);
//	}
	
	/**
	 * ͨѶ¼��Ա
	 * @param accForm
	 * @return
	 */
//	public Result queryCommunication(AccreditSearchForm accForm,String userId){
//		
//		String sql = "select groups.groupName,info.id,info.groupId,info.name,info.unitName,info.unitAddress,info.email," +
//				"ROW_NUMBER() over(order by info.id desc) as row_id from OACommunicationNoteInfo info," +
//				"OACommunicationNoteGroup groups where info.groupId=groups.id and ";
//		sql += "(info.createBy='"+userId+"' or groups.isPublic=2) and isnull(info.email,'') != ''";
//		
//		if(accForm.getKeyType() != null && "group".equals(accForm.getKeyType())){
//			//����ͨѶ¼���ѯ����
//			sql += " and groups.classCode like '"+accForm.getValue()+"%'";
//		}else if(accForm.getKeyType() != null && "manygroup".equals(accForm.getKeyType())){
//			//ѡ������
//			if(!"".equals(accForm.getValue())){
//				sql += " and groups.classCode in (";
//				String[] s = accForm.getValue().split(",");
//				for(int i = 0;i<s.length;i++){
//					sql += "'"+s[i]+"'";
//					if(i<s.length-1){
//						sql += ",";
//					}
//				}
//				sql += ")";
//			}else{
//				sql += " and 1=2";
//			}
//			
//		}else if(accForm.getKeyType() != null && "keyWord".equals(accForm.getKeyType())){
//			//�ؼ��ֲ�ѯ
//			sql += " and (groups.groupName like '%"+accForm.getValue()+"%'";
//			sql += " or info.name like '%"+accForm.getValue()+"%'";
//			sql += " or info.unitAddress like '%"+accForm.getValue()+"%'";
//			sql += " or info.email like '%"+accForm.getValue()+"%'";
//			sql += " or info.unitName like '%"+accForm.getValue()+"%')";
//		}else if(accForm.getKeyType() != null && "letter".equals(accForm.getKeyType())){
//			//���պ��ֵ���ĸ��ѯ
//			if(!"0".equals(accForm.getValue())){
//				sql += " and (info.name "+ edhSql(accForm.getValue(), "OACommunicationNoteInfo", "name")+" or info.name like '"+accForm.getValue()+"%')";
//			}
//			
//		}else if(accForm.getKeyType() != null && "choose".equals(accForm.getKeyType())){
//			//��ѡ�������
//			if(accForm.getValue() != null && !"".equals(accForm.getValue())){
//				sql += " and info.id in (";
//				String[] s = accForm.getValue().split(",");
//				for(int i = 0;i<s.length;i++){
//					sql += "'"+s[i]+"'";
//					if(i<s.length-1){
//						sql += ",";
//					}
//				}
//				sql += ")";
//			}else{
//				sql += " and 1=2";
//			}
//		}
//		if(accForm.getParameterCode() != null && !"".equals(accForm.getParameterCode())){
//			sql += " and info.id in (";
//			String[] s = accForm.getParameterCode().split(",");
//			for(int i = 0;i<s.length;i++){
//				sql += "'"+s[i]+"'";
//				if(i<s.length-1){
//					sql += ",";
//				}
//			}
//			sql += ")";
//		}
//		return sqlListMaps(sql, null, accForm.getPageNo(), accForm.getPageSize());
//	}
	
	/**
	 * �ͻ���ϵ����
	 * @param accForm
	 * @param userId
	 * @return
	 */
	public Result queryClientGroup(AccreditSearchForm accForm, String userId){
		
		List<String> param = new ArrayList<String>();
		String sql = "select CRMClientInfo.id,CRMClientInfo.ClientName,CRMClientInfoDet.UserName,CRMClientInfoDet.ClientEmail from CRMClientInfoDet,CRMClientInfo,CRMClientInfoEmp";
		sql += " where CRMClientInfoDet.f_ref=CRMClientInfo.id and CRMClientInfo.id=CRMClientInfoEmp.f_ref";
		sql += " and isnull(CRMClientInfoDet.UserName,'') != '' and isnull(CRMClientInfoDet.ClientEmail,'') != '' and " +
				"((exists(select * from tblEmpDistrict a left join tblEmpDistrictDet b on b.f_ref=a.id where a.EmployeeID='"+userId+"' and b.BusinessDistrict=CRMClientInfo.BusinessDistrict)" +
						" or '"+userId+"'='1' or (select setting from tblSysDeploy where sysCode='ManageDistrict')='false')) and (CRMClientInfoEmp.EmployeeID='"+userId+"' or '"+userId+"'='1')";
		if(!"".equals(accForm.getParameterCode()) && accForm.getParameterCode() != null && !"null".equals(accForm.getParameterCode())){
			String[] ids = accForm.getParameterCode().split(",");
			sql += " and CRMClientInfo.id in (";
			for(int i=0;i<ids.length;i++){
				sql += "'"+ids[i]+"'";
				if(i<ids.length-1){
					sql += ",";
				}
			}
			sql += ")";
		}
		return sqlList(sql, param);
	}
	
	/**
	 * �ͻ���ϵ��
	 * @param accForm
	 * @param userId
	 * @return
	 */
	public Result queryClientData(AccreditSearchForm accForm, LoginBean loginBean){
		
		
		String sql = "";
		
		if(accForm.getKeyType() != null && "letter".equals(accForm.getKeyType())){
			//��ĸ��ѯ��union all ��ѯ
			sql = "select distinct CRMClientInfoDet.f_ref as id,CRMClientInfoDet.id as detid,CRMClientInfo.clientName,CRMClientInfoDet.userName," +
					"CRMClientInfoDet.clientEmail,ROW_NUMBER() over(order by CRMClientInfoDet.id desc) as row_id from(select CRMClientInfoDet.id," +
					"CRMClientInfoDet.f_ref,CRMClientInfoDet.userName,CRMClientInfoDet.clientEmail from CRMClientInfoDet where " +
					"isnull(CRMClientInfoDet.clientEmail,'') != '' and (CRMClientInfoDet.userName "+edhSql(accForm.getValue(), "CRMClientInfoDet", "userName") +") " +
					"union all select CRMClientInfoDet.id,CRMClientInfoDet.f_ref,CRMClientInfoDet.userName,CRMClientInfoDet.clientEmail " +
					"from CRMClientInfoDet where isnull(CRMClientInfoDet.clientEmail,'') != '' " +
					"and CRMClientInfoDet.userName like '"+accForm.getValue()+"%') " +
					"CRMClientInfoDet left join CRMClientInfo on CRMClientInfoDet.f_ref=CRMClientInfo.id where " +
					"f_ref in (SELECT id from CRMClientInfo where 1=1"+new PublicMgt().getClientScope(loginBean)+")";
		}else{
			sql = "select CRMClientInfo.id,CRMClientInfoDet.id as detid,CRMClientInfo.clientName,CRMClientInfoDet.userName," +
			"CRMClientInfoDet.clientEmail,ROW_NUMBER() over(order by CRMClientInfoDet.id desc) as row_id " +
			"from CRMClientInfoDet,CRMClientInfo where CRMClientInfoDet.f_ref=CRMClientInfo.id and isnull(CRMClientInfoDet.clientEmail,'') != ''  and f_ref in(SELECT id from CRMClientInfo where 1=1";
			sql+=new PublicMgt().getClientScope(loginBean)+")";
		}
		
		if(accForm.getKeyType() != null && "keyWord".equals(accForm.getKeyType())){
			//�ؼ��ֲ�ѯ
			sql += " and (CRMClientInfo.clientName like '%"+accForm.getValue()+"%'";
			sql += " or CRMClientInfoDet.userName like '%"+accForm.getValue()+"%'";
			sql += " or CRMClientInfoDet.clientEmail like '%"+accForm.getValue()+"%')";
		}
		
		if(accForm.getKeyType() != null && "choose".equals(accForm.getKeyType())){
			//��ѡ�������
			if(accForm.getValue() != null && !"".equals(accForm.getValue())){
				sql += " and CRMClientInfoDet.clientEmail in (";
				String[] s = accForm.getValue().split(",");
				for(int i = 0;i<s.length;i++){
					sql += "'"+s[i]+"'";
					if(i<s.length-1){
						sql += ",";
					}
				}
				sql += ")";
			}else{
				sql += " and 1=2";
			}
		}
		
		return sqlListMaps(sql.toString(), null, accForm.getPageNo(), accForm.getPageSize());
	}
	
	/**
	 * �ͻ�������
	 * @param accForm
	 * @param userId
	 * @return
	 */
	public Result queryCRMClientData(AccreditSearchForm accForm,
			HttpServletRequest request, LoginBean loginBean){
		
		String sql = "select id,clientNo,clientName,row_number() over(order by lastContractTime desc) row_id from CRMClientInfo where 1=1 ";
		
		StringBuffer condition = new StringBuffer();
		
		String fieldValueSQL = "" ;
		
		MOperation mop = (MOperation) loginBean.getOperationMap().get("/UserFunctionQueryAction.do?tableName=CRMClientInfo");
		ArrayList scopeRight = getAllMopByType(mop,loginBean, MOperation.M_QUERY);
		String allDeptScopeStr = checkExistAllDeptScope(scopeRight);
		
		//�����Ƿ������������
		String userId = loginBean.getId();
		/*���÷�ΧȨ��*/
		if(!"1".equals(userId) && !"ALL".equals(allDeptScopeStr)){
			/*�鿴ĳ�ֶ�ֵ����*/
			
			condition.append(" and  (CRMClientInfo.createBy ='").append(userId).append("' or CRMClientInfo.id in(select f_ref from CRMClientInfoEmp " +
					"where employeeId ='").append(userId).append("') ") ;
			
			if(mop!=null){
				/*
				�°��ȡȨ������
				ArrayList scopeRight = new ArrayList();
				scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
				scopeRight.addAll(this.getLoginBean(request).getAllScopeRight());
				*/
				if (scopeRight != null && scopeRight.size()>0) {
					for (Object o : scopeRight) {
						String strUserIds = "" ;
						String strDeptIds = "" ;
						LoginScopeBean lsb = (LoginScopeBean) o;
						if(lsb!=null && "1".equals(lsb.getFlag())){
							for(String strId : lsb.getScopeValue().split(";")){
								strUserIds += "'"+strId+"'," ;
							}
							strUserIds = strUserIds.substring(0, strUserIds.length()-1) ;
							condition.append(" or CRMClientInfo.createBy in (").append(strUserIds).append(")");
							
						}
						if(lsb!=null && "5".equals(lsb.getFlag())){
							for(String strId : lsb.getScopeValue().split(";")){
								strDeptIds += "or departmentCode like '" +strId + "%' ";
								//strDeptIds += "'"+strId+"'," ;
								//condition.append(" or departmentCode like '").append(strId).append("%' ") ;
							}
							if(!"".equals(strDeptIds)){
								condition.append(" or (").append(strDeptIds.substring(2)).append(")");
							}
							
						}
						if(lsb!=null && "6".equals(lsb.getFlag()) && "CRMClientInfo".equals(lsb.getTableName())){
							if (lsb.getScopeValue() != null && lsb.getScopeValue().length() > 0) {
								if(lsb.getScopeValue().contains(";")){
									String[] scopes = lsb.getScopeValue().split(";") ;
									String scopeSQL = "(" ;
									for(String str : scopes){
										scopeSQL += "'" + str + "'," ; 
									}
									scopeSQL = scopeSQL.substring(0, scopeSQL.length()-1) ;
									scopeSQL += ")" ;
									fieldValueSQL = lsb.getTableName() + "." + lsb.getFieldName() + " in " +scopeSQL ;
								}
				            }
						}
					}
					
				}
			}
			condition.append(")") ;
			
			if(fieldValueSQL.length()>0){
				condition.append(" and (").append(fieldValueSQL).append(")") ;
			}
			
			//��ȡ�鿴�����޸�Ȩ�޵�����ְԱID,�����޸�Ȩ��
			//this.getAllowUpdateUserIds(mop, request);
		}
		
		//condition.append(" and status != '1' ") ;
		
		sql += condition.toString();
		if(accForm.getKeyType() != null && "keyWord".equals(accForm.getKeyType())){
			//�ؼ��ֲ�ѯ
			sql += " and CRMClientInfo.clientName like '%"+accForm.getValue()+"%'";
		}else if(accForm.getKeyType() != null && "letter".equals(accForm.getKeyType())){
			//���պ��ֵ���ĸ��ѯ
			if(!"0".equals(accForm.getValue())){
				sql += " and (CRMClientInfo.clientName "+ edhSql(accForm.getValue(), "CRMClientInfo", "clientName")+" or CRMClientInfo.clientName like '"+accForm.getValue()+"%')";
			}
			
		}else if(accForm.getKeyType() != null && "choose".equals(accForm.getKeyType())){
			//��ѡ�������
			if(accForm.getValue() != null && !"".equals(accForm.getValue())){
				sql += " and CRMClientInfo.id in (";
				String[] s = accForm.getValue().split(",");
				for(int i = 0;i<s.length;i++){
					sql += "'"+s[i]+"'";
					if(i<s.length-1){
						sql += ",";
					}
				}
				sql += ")";
			}else{
				sql += " and 1=2";
			}
		}
		if(!"".equals(accForm.getParameterCode()) && accForm.getParameterCode() != null && !"null".equals(accForm.getParameterCode())){
			String[] ids = accForm.getParameterCode().split(",");
			sql += " and CRMClientInfo.id in (";
			for(int i=0;i<ids.length;i++){
				sql += "'"+ids[i]+"'";
				if(i<ids.length-1){
					sql += ",";
				}
			}
			sql += ")";
		}
		BaseEnv.log.debug("AccreditMgt.queryCRMClientData:"+sql);
		return sqlListMaps(sql, null, accForm.getPageNo(), accForm.getPageSize());
	}
	
	
	/**
	 * �����û����Ʋ�ѯ����
	 * @param userId
	 * @return
	 */
	public Result queryDeptCode(final String userIds) {
        final Result rst = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        try {
                            Connection conn = connection;
                            String sql = "select departmentCode from tblEmployee where id in (";
                    		String[] userId = userIds.split(",");
                    		for(int i =0;i<userId.length;i++){
                    			sql += "'"+userId[i]+"'";
                    			if(i<userId.length-1){
                    				sql += ",";
                    			}
                    		}
                    		sql += ")";
                            BaseEnv.log.debug(sql);
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            ResultSet rs = pstmt.executeQuery();
                            String deptCodes = "";
                            while (rs.next()) {
                            	deptCodes += rs.getString("departmentCode")+",";
                            }
                            rst.setRetVal(deptCodes);
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
	
	///wyy
	/**
	 * 
	 * 
	 * <p>Title:��Ȩmgt</p> 
	 * <p>Description: </p>
	 *
	 * @Date:2012-8-27
	 * @Copyright: �������
	 * @Author wyy
	 */
	
	/**
	 * ��ȡְԱ��������
	 * @return
	 */
	
	public String queryGroupData(String id){

		List<String> param = new ArrayList<String>();
		String sql = "select u.f_ref,g.groupName " +
				"from tblEmpGroupUser u ,tblEmpGroup g where g.id = u.f_ref and userID = '"+id+"'";
		Result rs = sqlList(sql, param);
		String str = "";
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS && rs!=null){
			ArrayList rsRs = (ArrayList)rs.retVal;
			for (Object obj : rsRs) {
				str += GlobalsTool.get(obj,0)+","+GlobalsTool.get(obj,1);
			}
		}
		return str;
	}
	
	public Result queryStaff(String Id){
		String sql = "select id,userID from  tblEmpGroupUser where f_ref = '"+Id+"'";
		sql +=" and 1=? order by detOrderNo";
		ArrayList param = new ArrayList();
		param.add("1");
		return sqlList(sql, param);
	}
	
	
	
	
	/**
	 * ��ȡ�Զ����������
	 * @return
	 */
	
	public Result queryDefineClassData(AccreditSearchForm accForm, String tableName,String nameField,String codeField){

		List<String> param = new ArrayList<String>();
		String cataName = accForm.getCondition(); //��conditon�������������
		
		
		String sql = "select id,"+codeField+","+nameField+",classCode,isCatalog from "+tableName+" a where statusid!='-1' ";
		if("tblCompany1".equals(cataName)){
			//��Ӧ��
			sql +=" and moduleType in ('1','3') ";
		}else if("tblCompany2".equals(cataName)){
			//�ͻ�
			sql +=" and moduleType in ('2','3') ";
		}else if("tblAccTypeInfo".equals(cataName)){
			//��Ŀ��������
			sql = "select a.id,"+codeField+",zh_CN,classCode,isCatalog from "+tableName+" a join tblLanguage b on a."+nameField+"=b.id  where statusid!='-1' ";
		}else if("tblCompany".equals(cataName)){
			//�ͻ�
			sql = "select id,"+codeField+","+nameField+",classCode,isCatalog,moduleType from "+tableName+" a where statusid!='-1' ";
		}
		
		sql +=" and  a.isCatalog=1 ";
		
		if(!"".equals(accForm.getValue()) && accForm.getValue() != null && !"null".equals(accForm.getValue())){
			sql += " and (classCode like '%"+accForm.getValue()+"%'"+" or "+nameField+" like '%"+accForm.getValue()+"%'"+
			" or "+codeField+" like '%"+accForm.getValue()+"%'"+" )";
		}
		
		sql += " order by "+codeField+"";
		return sqlList(sql, param);
	}
	
	/**
	 * ��ѯ��Ա��Ϣ
	 * @param accForm
	 * @param local
	 * @return
	 */
	public Result queryDefineClassChildData(AccreditSearchForm accForm, String tableName,String nameField,String codeField){
		
		ArrayList<String> param = new ArrayList<String>();
		String cataName = accForm.getCondition(); //��conditon�������������		
		
		String sql = "select id,"+codeField+","+nameField+",classCode,isCatalog from "+tableName+" a where statusid!='-1' ";
		if("tblCompany1".equals(cataName)){
			//��Ӧ��
			sql +=" and moduleType in ('1','3') ";
		}else if("tblCompany2".equals(cataName)){
			//�ͻ�
			sql +=" and moduleType in ('2','3') ";
		}else if("tblAccTypeInfo".equals(cataName)){
			//��Ŀ��������
			sql = "select a.id,"+codeField+",zh_CN,classCode,isCatalog from "+tableName+" a join tblLanguage b on a."+nameField+"=b.id  where statusid!='-1' ";
		}
				
		if(accForm.getKeyType() != null && "keyWord".equals(accForm.getKeyType())){
			//�ؼ��ֲ�ѯ
			sql += " and ( "+codeField+" like '%"+accForm.getValue()+"%'"
			+" or "+nameField+" like '%"+accForm.getValue()+"%'"+" )";
		}else if(accForm.getKeyType() != null && "group".equals(accForm.getKeyType())){
			if("tblCompany".equals(cataName) && accForm.getValue().length()==1){
				sql += " and len(classCode) =5 and moduleType='"+accForm.getValue()+"' ";
			}else{
				//�������,ֻ�ܲ��¼����������Ӽ�
				sql += " and classCode like '"+accForm.getValue()+"%' and len(classCode) = "+accForm.getValue().length()+"+5 ";
			}
		}else if(accForm.getKeyType() != null && "letter".equals(accForm.getKeyType())){
			//������ĸ��ѯ����----��֧��
			
		}else if(accForm.getKeyType() != null && "choose".equals(accForm.getKeyType())){
			//��ѡ����
			if(accForm.getValue()!=null && !"".equals(accForm.getValue())){
				String[] s = accForm.getValue().split(",");
				sql += "and classCode in ('"+s[0]+"'";
				for(int i=1;i<s.length;i++){
					sql += " , '"+s[i]+"'";
				}
				sql+=")";
			}else{
				sql += " and 1=2";
			}
		}else if(accForm.getKeyType() == null||accForm.getKeyType().length() ==0){
			//��һ�ν�����û����ʱ��ֻ���һ��
			sql += " and len(classCode) = 5 ";
		}
		sql += " order by "+codeField+"";
		return sqlList(sql, param,accForm.getPageSize(), accForm.getPageNo(), true);
	}
	
	/**
	 * ����Ƿ��й�Ͻȫ��˾Ȩ��
	 * @param scopeRight
	 * @return
	 */
	public String checkExistAllDeptScope(ArrayList scopeRight){
		String retStr = "";
		if (scopeRight != null && scopeRight.size()>0) {
			for (Object o : scopeRight) {
				LoginScopeBean lsb = (LoginScopeBean) o;
				if(lsb!=null && "5".equals(lsb.getFlag())){
					for(String strId : lsb.getScopeValue().split(";")){
						if("ALL".equals(strId)){
							retStr = "ALL";
							break;
						}
					}
					if("ALL".equals(retStr)){
						break;
					}
				}
			}
		}
		return retStr;
	}
	
	/**
	 * ����mopType��ȡ��ǰ�û�������Ȩ��
	 * @param mop
	 * @param loginBean
	 * @param mopType  Ȩ������(MOperation.M_QUERY:��ѯ)
	 * @return
	 */
	public ArrayList getAllMopByType(MOperation mop,LoginBean loginBean,int mopType){
		ArrayList scopeRight = new ArrayList();
		scopeRight.addAll(mop.getScope(mopType));
		scopeRight.addAll(loginBean.getAllScopeRight());
		return scopeRight;
	}
}
