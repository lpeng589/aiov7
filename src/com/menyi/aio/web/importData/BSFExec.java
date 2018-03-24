package com.menyi.aio.web.importData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.bsf.BSFManager;

import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConfigParse;
import com.menyi.web.util.GlobalsTool;

public class BSFExec {

	public static void calculate(HashMap values ,Hashtable allTable,DBTableInfoBean tableInfo,HashMap<String,ExcelFieldInfoBean> impFields,Locale locale) throws Exception{
		try{
			BSFExecBean bsfBean = new BSFExecBean(values,allTable,tableInfo,impFields,locale);
			//��ִ����ϸ��Ĺ�ʽ����ִ������Ĺ�ʽ
			ArrayList<DBTableInfoBean> tableList = DDLOperation.getChildTables(tableInfo.getTableName(), allTable);
			HashMap tblChildMap=new HashMap();
            for(int i=0;i<tableList.size();i++){
            	tblChildMap.put(((DBTableInfoBean)tableList.get(i)).getTableName(),String.valueOf(i));
            	tblChildMap.put(String.valueOf(i),((DBTableInfoBean)tableList.get(i)).getTableName());
            }
			tableList.add(tableInfo);
			for(DBTableInfoBean childTable:tableList){
				for(DBFieldInfoBean dbfields:childTable.getFieldInfos()){
					if(dbfields.getCalculate() != null && dbfields.getCalculate().length() > 0 && 
							impFields.get(childTable.getTableName()+"_"+dbfields.getFieldName())!=null){					
						//ֻ��excel�д��ڵ��ֶν��й�ʽ���㣬����һЩ�������Ȳ��ֶ��Ͻ��й�˾���㣬���ܵ��µĸ���ֵ���ϱ仯������С��������
						//System.out.println(dbfields.getFieldName() +"----"+dbfields.getCalculate());
						//�м��㹫ʽ
						int lines = 1;
						boolean isMain= true;
						if(!childTable.getTableName().equals(tableInfo.getTableName())){
							ArrayList cl = (ArrayList)values.get("TABLENAME_"+childTable.getTableName());
							if(cl != null){
								lines = cl.size();								
							}else{
								lines = 0;
							}
							isMain=false;
						}						
						
						for(int i=0;i<lines;i++){
							//����ֶ�ֵ����Ϊ�գ���Ҫִ�й�ʽ
							String v;
							String ConversionRate="";
							if(isMain){
								v =values.get(dbfields.getFieldName())==null?"":values.get(dbfields.getFieldName()).toString();
								ConversionRate = values.get("ConversionRate")==null?"":values.get("ConversionRate").toString();
							}else{
								ArrayList cl = (ArrayList)values.get("TABLENAME_"+childTable.getTableName());
								HashMap hm = (HashMap)cl.get(i);
								v = hm.get(dbfields.getFieldName())==null?"":hm.get(dbfields.getFieldName()).toString();	
								ConversionRate = hm.get("ConversionRate")==null?"":hm.get("ConversionRate").toString();
							}
							if(v==null||v.length()==0){
								continue;
							}
							//������Щ������λ�Ĺ�ʽд�Ĳ���ȷ�����������ڲ�������ʽ���Բ������������������ǻ�����Ϊ0�ģ���ִ�и�����λ��ʽ
							if(ConversionRate==null||ConversionRate.length()==0 || ConversionRate.equals("0")){
								if(dbfields.getFieldSysType().equals("AssUnit")||dbfields.getFieldSysType().equals("unitFloat")||dbfields.getFieldSysType().equals("unitInt")){
									continue;
								}
							}
							String newCals = dbfields.getCalculate();							
							if(isMain){
								//�滻��ʽ�й�����ʽ
								newCals = GlobalsTool.replacePubCaculate(null,tableInfo.getTableName(),tblChildMap,newCals);
								//�滻��ʽ�����б���
								ArrayList childTableList = DDLOperation.getChildTables(tableInfo.getTableName(), BaseEnv.tableInfos);
								for(int k=0;k<childTableList.size();k++){
									newCals = newCals.replaceAll("@CHILDTABLENAME"+k, (String)tblChildMap.get(""+k));
								}								
								// �ֽ�����
								newCals = newCals.replaceAll("\\}\\{", "};{");
							}else{
								//�滻��ʽ�й�����ʽ
								newCals = GlobalsTool.replacePubCaculate(dbfields.getTableBean().getTableName(),tableInfo.getTableName(),tblChildMap,newCals);
								//�滻��ʽ�����б���
								
								newCals = newCals.replaceAll("@TABLENAME", dbfields.getTableBean().getTableName());
								
								ArrayList childTableList = DDLOperation.getChildTables(tableInfo.getTableName(), BaseEnv.tableInfos);
								for(int k=0;k<childTableList.size();k++){
									newCals = newCals.replaceAll("@CHILDTABLENAME"+k, (String)tblChildMap.get(""+k));
								}
								newCals = newCals.replaceAll("\\}\\{", "};{");
							}							
							newCals = replaceMEM(newCals);
							
							//BaseEnv.log.debug("BSFExec.calculate "+dbfields.getTableBean().getTableName()+"."+dbfields.getFieldName()+"    newCals="+newCals);
							
							String matStr = newCals;
							try{
								Pattern pattern=Pattern.compile("\\{([^\\{\\}]*)\\}[\\s]*=([^;=]+)");   
						        Matcher matcher=pattern.matcher(matStr);   
						        while (matcher.find())
						        {   
						        	String str = matcher.group();
						        	String cl = matcher.group(1);
						        	String cr = matcher.group(2);
						       	 	String newStr = "bsf.setValue('"+childTable.getTableName()+"_"+dbfields.getFieldName()+"','"+cl+"',"+cr+","+(isMain?-1:i)+")";	
							       	newCals = newCals.substring(0,newCals.indexOf(str))+newStr+newCals.substring(newCals.indexOf(str)+str.length());
						        } 						
						        
						       
						        newCals = newCals.replaceAll("sum\\{", "bsf.getSum('");								
						        newCals = newCals.replaceAll("\\{", "bsf.getValue('");
						        newCals = newCals.replaceAll("\\}", "',"+i+")");
						        
						     
						        //******�����ֶ�ֵ���͵����ض���������ֵ********//
						        /*
						        newCals = newCals.replaceAll("sum\\{", "bsf.getSum('");	
						        //Pattern pattern2=Pattern.compile("\\{([a-zA-Z_0-9]+?)\\}");   						        
						        Pattern pattern2=Pattern.compile("\\{(.*?)\\}");
						        Matcher matcher2=pattern2.matcher(newCals);   
						        while (matcher2.find())
						        {   
						        	//String v1 = matcher2.group(1);
						        	String vo = matcher2.group();
						        	String val = matcher2.group(1);						        
						        	boolean flag = false;
						        	for(DBFieldInfoBean item : tableInfo.getFieldInfos()){
						        		if(val.equalsIgnoreCase(item.getFieldName())){
						        			if("1".equals(Byte.toString(item.getInputType()))){
						        				//newCals = newCals.replaceAll(val,"bsf.getStrVal('"+val+"',"+i+")");
							        			newCals = newCals.substring(0,newCals.indexOf(vo))+"bsf.getStrVal('"+val+newCals.substring(newCals.indexOf(vo)+vo.length()-1);
						        			}else{
							        			 newCals = newCals.substring(0,newCals.indexOf(vo))+"bsf.getValue('"+val+newCals.substring(newCals.indexOf(vo)+vo.length()-1);
							        		}
						        			flag = true;
						        		} 
						        	}
						        	if(!flag){
						        		 newCals = newCals.substring(0,newCals.indexOf(vo))+"bsf.getValue('"+val+newCals.substring(newCals.indexOf(vo)+vo.length()-1);
						        	}
						        							       	 	
						        }
						        newCals = newCals.replaceAll("sum\\{", "bsf.getSum('");
						        newCals = newCals.replaceAll("\\{", "");
						        newCals = newCals.replaceAll("\\}", "',"+i+")");
						        */
						        //***************end*****************//
						        
						        //BaseEnv.log.debug("BSFExec.calculate "+dbfields.getTableBean().getTableName()+"."+dbfields.getFieldName()+"    newCals="+newCals);
						        BSFManager  bsf = new BSFManager();//�����ַ���
								bsf.declareBean("bsf", bsfBean, bsfBean.getClass());
								bsf.exec("javascript", "XX", 0, 0, newCals);
								
							}catch(Exception e){
								BaseEnv.log.error("BSFExec.calculate Error matStr: "+matStr);
								BaseEnv.log.error("BSFExec.calculate Error newCals: "+newCals);		
								BaseEnv.log.error("BSFExec.calculate Error  ",e);		
								throw e;
							}
						}
						
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}

	
	private static String replaceMEM(String str){
		if (str.contains("@MEM:")) {
			// �������õ�����Щϵͳ����
			ArrayList sysParams = new ArrayList();
			ConfigParse.parseSentenceGetParam(str, new ArrayList(),
					sysParams, new ArrayList(), new ArrayList(),
					new ArrayList(),null);
			for (int j = 0; j < sysParams.size(); j++) {
				str = str.replaceAll("@MEM:" + sysParams.get(j),
						BaseEnv.systemSet.get(sysParams.get(j))
						.getSetting());
			}
		}
		return str;
	}
	public static void main(String[] args){
		
		try{
			BSFExecBean bsfBean = new BSFExecBean(null,null,null,null,null);
	        BSFManager  bsf = new BSFManager();//�����ַ���
			bsf.declareBean("bsf", bsfBean, bsfBean.getClass());
			bsf.exec("javascript", "XX", 0, 0, "bsf.setValue('','',0*\"NaN\" ,0)");
			
//			HashMap values = new HashMap();
//			
//			ArrayList al = new ArrayList();
//			HashMap cv = new HashMap();
//			cv.put("SecUnitQty", "0");
//			
//			al.add(cv);
//			values.put("TABLENAME_tblCheckMoreDet", al);
//			BSFExecBean bsfBean = new BSFExecBean(values,null,null,null,null);
//			
//			String newCals = " bsf.setValue('tblCheckMoreDet_SecUnitQty','tblCheckMoreDet_OQty',bsf.getValue('tblCheckMoreDet_SecUnitQty',0)*bsf.getValue('tblCheckMoreDet_ConversionRate',0),0);";
//	        
//	        BSFManager  bsf = new BSFManager();//�����ַ���
//			bsf.declareBean("bsf", bsfBean, bsfBean.getClass());
//			bsf.exec("javascript", "XX", 0, 0, newCals);
		}catch(Exception e){
			e.printStackTrace();
			
		}
		
	}
}
