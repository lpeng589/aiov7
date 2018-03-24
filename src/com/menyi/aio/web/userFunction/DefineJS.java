package com.menyi.aio.web.userFunction;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.menyi.aio.bean.ColConfigBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.PublicMgt;

/**
 * 为了把velocity复杂逻辑转移到java中来，增加代码的可阅读性，建立此类，可把部分复杂的生成js代码的逻辑转移到这里
 * @author 周新宇
 *
 */
public class DefineJS {
	
	public static DefineJS instance= new DefineJS();

	public static String initChildDownSelectNoCond(String tableName,String fieldName){
		DBFieldInfoBean fieldBean = GlobalsTool.getFieldBean(tableName+"."+fieldName);
		String sql = fieldBean==null?"":fieldBean.getInputValue();
		if(sql==null || sql.length()==0 ){
			//如果是空，或有条件，则不直接查询数据库。
			return "";
		}else if(sql.indexOf("@ValueOfDB") >-1){
			return "0:明细表不支持带条件下拉框;";
		}else {			
			if(sql.indexOf("@ValueOfConst") >-1){
				Pattern pattern = Pattern.compile("@ValueOfConst:([\\w]+)");
				Matcher matcher = pattern.matcher(sql);	
				String con="";
				String queryStr="";
				while(matcher.find()){
					sql = sql.replaceAll("@ValueOfConst:"+matcher.group(1), "'"+matcher.group(1)+"'");
				}
			}
			//执行数据库查询
			PublicMgt pmgt= new PublicMgt();
			BaseEnv.log.debug("DefineJS.initChildDownSelectNoCond "+tableName+"."+fieldName+":"+sql);
			return pmgt.downSelect(sql);
		}
	}
	/**
	 * 下拉选择框初始化时，进行ajax调用选项数据，并要跟据语句来决定onchange事件
	 * @return
	 */
	public static String initDownSelect(String mainTable){
		StringBuffer sb = new StringBuffer();
		//
		Hashtable allTables =BaseEnv.tableInfos;
		DBTableInfoBean bean =DDLOperation.getTableInfo(allTables, mainTable);
		if(bean == null){
			return "";
		}
		
		//主表下拉字段
		String changeStr="";
		for(DBFieldInfoBean row: bean.getFieldInfos()){
			if(row.getInputType() == DBFieldInfoBean.INPUT_DOWNLOAD_SELECT ) {
				
				/**
				 * 参数：@ValueOfDBMust: 必选条件 如果条件没有值，则不执行查询操作
				 *      @ValueOfDB: 可选条件
				 */
				Pattern pattern = Pattern.compile("@ValueOfDBMust:([\\w]+)");
				Matcher matcher = pattern.matcher(row.getInputValue());	
				String con="";
				String queryStr="";
				while(matcher.find()){
					  con += " m('"+matcher.group(1)+"').value !='' &&";
					  queryStr +="+'&"+matcher.group(1)+"='+"+"encodeURIComponent( m('"+matcher.group(1)+"').value)";
				}
				pattern = Pattern.compile("@ValueOfDB:([\\w]+)");
				matcher = pattern.matcher(row.getInputValue());	
				while(matcher.find()){
					queryStr +="+'&"+matcher.group(1)+"='+"+" encodeURIComponent( m('"+matcher.group(1)+"').value)";
				}
				queryStr ="    jQuery.get('/UtilServlet?operation=downSelect&field="+bean.getTableName()+"."+row.getFieldName()+"'"+queryStr+",\n" +
						"        function(data){\n " +
						"            if(data.indexOf('ERROR:')>-1){alert(data.substring(6));return;}\n" +
						"            var objSelect = m('"+row.getFieldName()+"');\n" +
						"            objSelect.options.length=0;\n" +
			            "            "+"objSelect.options.add(new Option('', ''));"+
						"            var ds = data.split('#;#');\n" +
						"            for(i=0;i<ds.length;i++){\n" +
						"            	if(ds[i] !=''){\n" +
						"            		var itemDS = ds[i].split('#:#');  \n" +
						"            		var varItem = new Option(itemDS[1], itemDS[0]);  \n" +
						"            		if(objSelect.getAttribute('vl')==itemDS[0]){varItem.selected=true};\n" +
						"            		objSelect.options.add(varItem);\n" +
						"            	}\n" +
						"            }\n" +
						"             $('#"+row.getFieldName()+"').change();\n" +
						"        });  ";
				//有必选条件，要判断一下这个值是否为空，明细表的值必须是“表名_字段名”				
				String oneAjax = "";
				if(con.length() >0){
					oneAjax +="    if("+con.substring(0,con.length() -2)+"){\n";
					oneAjax +="    "+queryStr+"\n";
					oneAjax +="    }\n";
				}else{
					oneAjax +=queryStr+"\n";
				}
				
				//如果没条件的，在初始化时直接调用，有条件的，执行其中一个条件的change事件
				pattern = Pattern.compile("(@ValueOfDBMust|@ValueOfDB):([\\w]+)");
				matcher = pattern.matcher(row.getInputValue());	
				String evalStr="";//用加增中相应字段的onchange事件
				String oneCond="";
				while(matcher.find()){
					  evalStr += "$('#"+matcher.group(2)+"').change( function() {\n" +
					  	oneAjax+
					  		" }); \n ";
						//有条件的添加change事件的调用
					  sb.append(evalStr);
					  oneCond = matcher.group(2);
				}
				if(oneCond.length() > 0){
					//执行一个条件的change事件,如果事件已经存在，或都已经存在于sb中，即不是下拉选择字段，则产生初始调用
					if(changeStr.indexOf("$('#"+oneCond+"')")== -1 && sb.indexOf("$('#"+oneCond+"')") ==-1){						
						changeStr +="$('#"+oneCond+"').change(); \n";
					}
				}else{
					//初始化时的调用
					sb.append(oneAjax);
				}
				
			}
				
		}
		sb.append(changeStr);
		return sb.toString();
	}
	
	/**
	 * 查询和报表界面的下拉弹出选择
	 * @return
	 */
	public static String initQueryDownSelect(ArrayList<String[]> conditions){
		StringBuffer sb = new StringBuffer();
		//
		
		
		//主表下拉字段
		String changeStr="";
		for(String[] row: conditions){
			if(row[3].equals(DBFieldInfoBean.INPUT_DOWNLOAD_SELECT+"") ) {
				
				/**
				 * 参数：@ValueOfDBMust: 必选条件 如果条件没有值，则不执行查询操作
				 *      @ValueOfDB: 可选条件
				 */
				String osql = row[12];
				if(osql==null || osql.length() ==0){
					continue;
				}
				Pattern pattern = Pattern.compile("@ValueOfDBMust:([\\w]+)");
				Matcher matcher = pattern.matcher(osql);	
				String con="";
				String queryStr="";
				while(matcher.find()){
					  con += " m('"+matcher.group(1)+"').value !='' &&";
					  queryStr +="+'&"+matcher.group(1)+"='+"+"encodeURIComponent( m('"+matcher.group(1)+"').value)";
				}
				pattern = Pattern.compile("@ValueOfDB:([\\w]+)");
				matcher = pattern.matcher(osql);	
				while(matcher.find()){
					queryStr +="+'&"+matcher.group(1)+"='+"+" encodeURIComponent( m('"+matcher.group(1)+"').value)";
				} //" encodeURIComponent( '"+osql+"')"
				String encodesql=osql;
				try {
					encodesql = URLEncoder.encode(osql,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				queryStr ="    jQuery.get('/UtilServlet?operation=downSelect&sql="+encodesql+"'"+queryStr+",\n" +
						"        function(data){\n " +
						"            if(data.indexOf('ERROR:')>-1){alert(data.substring(6));return;}\n" +
						"            var objSelect = m('"+row[1]+"');\n" +
						"            objSelect.options.length=0;\n" +
			            "            "+("1".equals(row[7])?"":"objSelect.options.add(new Option('', ''));")+
						"            var ds = data.split('#;#');\n" +
						"            for(i=0;i<ds.length;i++){\n" +
						"            	if(ds[i] !=''){\n" +
						"            		var itemDS = ds[i].split('#:#');  \n" +
						"            		var varItem = new Option(itemDS[1], itemDS[0]);  \n" +
						"            		if(objSelect.getAttribute('vl')==itemDS[0]){varItem.selected=true};\n" +
						"            		objSelect.options.add(varItem);\n" +
						"            	}\n" +
						"            }\n" +
						"             $('#"+row[1]+"').change();\n" +
						"        });  ";
				//有必选条件，要判断一下这个值是否为空，明细表的值必须是“表名_字段名”				
				String oneAjax = "";
				if(con.length() >0){
					oneAjax +="    if("+con.substring(0,con.length() -2)+"){\n";
					oneAjax +="    "+queryStr+"\n";
					oneAjax +="    }\n";
				}else{
					oneAjax +=queryStr+"\n";
				}
				
				//如果没条件的，在初始化时直接调用，有条件的，执行其中一个条件的change事件
				pattern = Pattern.compile("(@ValueOfDBMust|@ValueOfDB):([\\w]+)");
				matcher = pattern.matcher(osql);	
				String evalStr="";//用加增中相应字段的onchange事件
				String oneCond="";
				while(matcher.find()){
					  evalStr += "$('#"+matcher.group(2)+"').change( function() {\n" +
					  	oneAjax+
					  		" }); \n ";
						//有条件的添加change事件的调用
					  sb.append(evalStr);
					  oneCond = matcher.group(2);
				}
				if(oneCond.length() > 0){
					//执行一个条件的change事件,如果事件已经存在，或都已经存在于sb中，即不是下拉选择字段，则产生初始调用
					if(changeStr.indexOf("$('#"+oneCond+"')")== -1 && sb.indexOf("$('#"+oneCond+"')") ==-1){						
						changeStr +="$('#"+oneCond+"').change(); \n";
					}
				}else{
					//初始化时的调用
					sb.append(oneAjax);
				}
				
			}
				
		}
		sb.append(changeStr);
		return sb.toString();
	}
	
//	public String initChildTable(ArrayList<DBTableInfoBean> $childTableList,Hashtable<String, ArrayList<ColConfigBean>> childTableConfigList){
//		StringBuffer sb = new StringBuffer();
//		String $allChildName="";
//		for (DBTableInfoBean $rowlist : $childTableList ) {
//			sb.append("childData[childCount]='"+$rowlist.getTableName()+"' \n");
//			sb.append("tabIsNulls[childCount]='"+$rowlist.isNull+"' \n");
//			sb.append("tabDiss[childCount]='"+$rowlist.display.get($globals.getLocale())+"' \n");
//			sb.append("childCount+++ \n");
//			
//			$allChildName=$allChildName+$rowlist.tableName+",";
//			String $griddata =$rowlist.tableName+"Data";
//			sb.append("var "+$griddata+" = new gridData() \n");
//			sb.append("gridDatas[gridDataNum] = "+$griddata +"\n");
//			sb.append("gridDataNum++"+"\n");
//			sb.append("var "+$rowlist.tableName+"="+$rowlist.defRowCount+"\n");
//		 if($allConfigList.size()>0){
//			 for (ArrayList<ColConfigBean> $colConfig : $childTableConfigList.get($rowlist.tableName)){
//				 DBFieldInfoBean $row=null;
//			 	  boolean $isExist=false;
//			 	  for (DBFieldInfoBean $config_row : $rowlist.fieldInfos ){
//			 		 if($colConfig.colName.equals($config_row.fieldName))
//			 		 	$row=$config_row;
//			 		 	$isExist=true;
//			 		 }
//			 	  }
//			 	  if( !$isExist){
//						for (DBFieldInfoBean $row : $rowlist.fieldInfos ){
//							
//							if($row.inputType == 2 || ($row.inputType==6 && $row.inputTypeOld==2)){
//								//设置自动插入表的字段值
//								String $ajaxStr=":";
//								if($row.insertTable.length()>0){
//									for(PopField $srow : $row.getSelectBean().viewFields){
//										$ajaxStr=$ajaxStr+$rowlist.tableName+"_"+$globals.getTableField($srow.asName)+";";
//									}
//								}
//								
//								
//								String $fname = $rowlist.tableName+"_"+$row.fieldName;
//					    		boolean $nullable = false;
//								if($row.isNull == 0){
//						    		$nullable = true;
//								}
//								String $inputValue = "";
//								sb.append(" var param4=\"\" \n");
//								 for(String $mainField : $row.getSelectBean().tableParams){
//									if($mainField.indexOf("@TABLENAME")>=0){
//										int $index=$mainField.indexOf("_")+1;
//										String $param=$rowlist.tableName+"_"+$mainField.substring($index);
//										String $disP=$rowlist.tableName+"."+$mainField.substring($index);
//										sb.append("param4=param4+\":"+$!globals.getFieldBean($rowlist.tableName,$mainField.substring($index)).isNull+$globals.getFieldDisplay($disP)+"@"+$param+"\n");
//									}else{			
//										if(($mainField.indexOf($rowlist.tableName)==-1 &&$globals.getFieldBean($!tableName,$mainField).inputType!=100)||($mainField.indexOf($rowlist.tableName)>=0)){
//											String $disP=$!tableName+"."+$mainField;
//											sb.append("param4=param4+\":"+$globals.getFieldBean($!tableName,$!mainField).isNull$globals.getFieldDisplay($disP)+"@"+$mainField+"\n");											
//										}
//									}
//								 }
//							   	 $inputValue = $rowlist.tableName+":"+$row.fieldName
//							   	 String $inputValue2 = $rowlist.tableName+"_"+$row.fieldName;
//							   	 if($row.inputType==6 && $row.inputTypeOld==2){
//							   	 	$inputType2=$row.inputTypeOld;
//							   	 }else{
//							   	 	$inputType2=$row.inputType;
//							   	 }
//							   	 
//								 if(!($row.fieldSysType.equals("GoodsField") and $globals.getPropBean($row.fieldName).isSequence==1)){
//							  	 for(PopField $srow : $row.getSelectBean().viewFields){
//							  	    if((($srow.display==null ||$srow.display.length()==0) && $srow.fieldName.equals($colConfig.colName)) 
//							  	    								|| (($srow.display!=null && $srow.display.length()>0) && $srow.display.equals($colConfig.colName))){
//										 if($srow.display.length()>0){
//										 	int $index=$srow.display.indexOf(".")+1;
//											String $tableField=$rowlist.tableName+"."+$srow.display.substring($index);
//											String $dis = $globals.getFieldDisplay($srow.display);
//											if($dis== null || $dis.equals("")||$dis.indexOf("not Exist")>=0){
//												$dis = $globals.getFieldDisplay($tableField);
//											}
//										 }else{
//										 	$dis="";
//										 }				
//										 if($dis==null || $dis.equals("")) {
//											$dis = $globals.getFieldDisplay($rowlist.tableName,$row.getSelectBean().name,$srow.fieldName); 
//										 }
//										 $disField = $rowlist.tableName+"_"+$globals.getTableField($srow.asName);
//										 $disWidth = "";
//										 $returnValue=$globals.getFieldWidth($rowlist.tableName,$globals.getTableField($srow.asName));
//										 if($returnValue!=0){
//										 	$disWidth=$returnValue;
//										 }else{
//										 	$disWidth=$srow.width;
//										 } 
//										 sp.append("addCols("+$griddata+",'"+$disField+"','"+$dis+"','"+$disWidth+"',2,true,'',1000000,"+$inputType2+",'"+$inputValue+":"+$disField+$ajaxStr+"'+param4,\""+$row.popupType+"\",\""+$row.fieldIdentityStr+"\",\""+$row.copyType+"\",\""+$row.listOrder+"\",\"\" \n");
//									}	
//								 }
//								 }
//							   }
//						  }
//						  
//				  }else{
//				  	  #set($fname = $rowlist.tableName+"_"+$row.fieldName)
//					  	if("$row.inputType"=="6"){
//							 #set($inputType2=$row.inputTypeOld)
//						}else{
//							 #set($inputType2=$row.inputType)
//						}
//					    if("$row.inputType" != "100" && $row.fieldName != "id" && $row.fieldName != "f_ref" && $row.fieldName != "f_brother"){					
//							#set($defValue="")
//							if($values.get($fname).length()>0||$row.getDefValue().indexOf("@Sess:")>=0){
//								#set($defValue=$values.get($fname))
//							}else if($globals.getUrlBillDef($fname).length()>0){
//						  		#set($defValue=$globals.getUrlBillDef($fname))
//						  	}else if($globals.getUrlBillDef($fname).length()==0){
//								#set($defValue=$row.getDefValue())
//						 	}
//						    #set($nullable = "false")
//							if("$row.isNull" == "0"){
//							    #set($nullable = "true")
//							}
//							#set($inputValue = "")
//							if("$row.inputType" == "1" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "1")){
//								  for($erow in $globals.getEnumerationItems($row.refEnumerationName)){
//									  #set($inputValue = "$inputValue;$erow.value:$erow.name")
//								  }
//								 addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,$inputType2,'$inputValue','',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');
//								 putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue',true);				  
//						    }else if("$row.inputType" == "16" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "16")){
//								 addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,$inputType2,inputType_$row.fieldName,'',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');
//								 putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue',true);				  
//						    }else if("$row.inputType"=="2" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "2")){
//							 	#set($inputValue = "$rowlist.tableName:$row.fieldName")
//								#set($inputValue2 = "$rowlist.tableName"+"_$row.fieldName")
//								var param4="";
//								for($mainField in $row.getSelectBean().tableParams){
//									if($mainField.indexOf("@TABLENAME")>=0){
//										#set($index=$mainField.indexOf("_")+1)
//										#set($param=$rowlist.tableName+"_"+$mainField.substring($index))
//										#set($disP=$rowlist.tableName+"."+$mainField.substring($index))
//										param4=param4+":$globals.getFieldBean($rowlist.tableName,$mainField.substring($index)).isNull$globals.getFieldDisplay($disP)@$param";
//									}else{			
//										if(($mainField.indexOf($rowlist.tableName)==-1 &&$globals.getFieldBean($tableName,$mainField).inputType!=100)||($mainField.indexOf($rowlist.tableName)>=0)){
//											#set($disP=$tableName+"."+$mainField)
//											param4=param4+":$globals.getFieldBean($tableName,$mainField).isNull$globals.getFieldDisplay($disP)@$mainField";											
//										}
//									}
//								}
//								if($row.getSelectBean().relationKey.hidden){
//										addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,-2,'','',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');
//										putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue');
//								}else{
//										if("$row.inputType" == "2" and "$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"){ //<!--序列号字段-->
//										addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,-2,'','',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');
//											addCols($griddata,'$fname'+'_hid','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,$row.inputType,'@SEQ$row.logicValidate',"$row.popupType","$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');
//											putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue');
//										}else{		
//											addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,$inputType2,'$inputValue:$inputValue2:'+param4,"$row.popupType","$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');
//											putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue',true);
//							  			}
//								}
//						    }else if("$row.inputType" == "5" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "5")){
//							  	  for($erow in $globals.getEnumerationItems($row.refEnumerationName)){
//									  #set($inputValue = "$inputValue;$erow.value:$erow.name")
//								  }
//							      addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,$inputType2,'$inputValue','',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');						  
//							}else if("$row.inputType" == "4" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "4")){
//								#set($fhname=$fname+"_lan")		
//								addCols($griddata,'$fhname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,$inputType2,'$inputValue','',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');	
//								putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue');	
//							
//								addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,-2,'$inputValue','',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');		
//								putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue');		
//							}else if("$row.inputType" == "3"){
//								addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,-2,'$inputValue','',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');	
//								putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue');	
//							}else{	
//								addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,$inputType2,'$inputValue','',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');	
//								putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue',true);	
//							}
//						}
//				    }
//			    }
//			    for ($config_row in $rowlist.fieldInfos ){
//			    	 #set($fname = $rowlist.tableName+"_"+$config_row.fieldName)
//			    	 #set($defValue="")
//					 if($values.get($fname).length()>0||$row.getDefValue().indexOf("@Sess:")>=0){
//						 #set($defValue=$values.get($fname))
//					 }else if($globals.getUrlBillDef($fname).length()>0){
//						 #set($defValue=$globals.getUrlBillDef($fname))
//					 }else if($globals.getUrlBillDef($fname).length()==0){
//						 #set($defValue=$config_row.getDefValue())
//					 }
//					 #set($nullable = "false")
//					 if("$config_row.isNull" == "0"){
//						 #set($nullable = "true")
//					 }
//					 #set($inputValue = "")
//			 	     if("$config_row.inputType"=="3"){
//			 	     	addCols($griddata,'$fname','$config_row.display.get("$globals.getLocale()")','$config_row.width',$config_row.fieldType,$nullable,'$defValue',$config_row.maxLength,-2,'$inputValue','','',"$config_row.copyType",'','$config_row.groupDisplay.get("$globals.getLocale()")');	
//						putChildValidateItem("$fname","$config_row.display.get("$globals.getLocale()")","$config_row.getStringType()","$row.fieldSysType",$config_row.getSringNull(),minValue,$config_row.getStringLength(),$config_row.getSringDefault(),'$defValue');	
//			 	     }else if("$config_row.inputType"=="2" && $config_row.getSelectBean().viewFields.size()>0){
//						var param4="";
//						if($config_row.getSelectBean().relationKey.hidden)	{
//							addCols($griddata,'$fname','$config_row.display.get("$globals.getLocale()")','$config_row.width',$config_row.fieldType,$nullable,'$defValue',$config_row.maxLength,-2,'','','',"$config_row.copyType",'','$config_row.groupDisplay.get("$globals.getLocale()")');
//							putChildValidateItem("$fname","$config_row.display.get("$globals.getLocale()")","$config_row.getStringType()","$row.fieldSysType",$config_row.getSringNull(),minValue,$config_row.getStringLength(),$config_row.getSringDefault(),'$defValue',true);
//						}else{
//							#set ($isExist="false")
//							for ($colConfig in $childTableConfigList.get($rowlist.tableName)){
//			 	     			if("$config_row.fieldName"=="$colConfig.colName"){
//			 	     				#set ($isExist="true")
//			 	     			}
//			 	     		}
//			 	     		if("$isExist"=="false"){
//							addCols($griddata,'$fname','$config_row.display.get("$globals.getLocale()")','$config_row.width',$config_row.fieldType,$nullable,'$defValue',$config_row.maxLength,-2,'$inputValue:$inputValue2:'+param4,'',"$config_row.fieldIdentityStr","$config_row.copyType",'','$config_row.groupDisplay.get("$globals.getLocale()")');
//							}
//						}
//						if(!("$config_row.fieldSysType"=="GoodsField" and $globals.getPropBean($config_row.fieldName).isSequence=="1")){
//			 	     	for($srow in $config_row.getSelectBean().viewFields){
//			 	     		#set ($isExist="false")
//			 	     		for ($colConfig in $childTableConfigList.get($rowlist.tableName)){
//			 	     			if(("$srow.display"=="" && "$srow.fieldName"=="$colConfig.colName") 
//			 	     						|| ("$srow.display"!="" && "$srow.display"=="$colConfig.colName")){
//			 	     				#set ($isExist="true")
//			 	     			}
//			 	     		}
//			 	     		if("$isExist"=="false"){
//			 	     			if($srow.display.length()>0){
//									#set($index=$srow.display.indexOf(".")+1)
//									#set($tableField=$rowlist.tableName+"."+$srow.display.substring($index))
//									#set($dis = $globals.getFieldDisplay($srow.display))
//									if($dis==""||$dis.indexOf("not Exist")>=0){
//									#set($dis = $globals.getFieldDisplay($tableField))
//									}
//								}else{
//									#set($dis="")
//								}				
//								if($dis == "") 
//									#set($dis = $globals.getFieldDisplay($rowlist.tableName,$row.getSelectBean().name,$srow.fieldName)) 
//								}
//								#set($disField = $rowlist.tableName+"_"+$globals.getTableField($srow.asName))
//								#set($returnValue=$globals.getFieldWidth("$rowlist.tableName","$globals.getTableField($config_row.asName)"))			        
//								if($returnValue!=0){
//										 #set($disWidth=$returnValue) 
//								}else{
//										#set($disWidth=$config_row.width) 
//								}
//								if($values.get($disField).length()>0)#set($defValue=$values.get($disField))}
//								addCols($griddata,'$disField','$dis','$disWidth',$config_row.fieldType,$nullable,'$defValue',$config_row.maxLength,-2,'');
//						    }
//						 }
//			 	     	 }
//			 	     }else if("$config_row.inputType" != "100"  && $config_row.fieldName != "id" && $config_row.fieldName != "f_ref" && $config_row.fieldName != "f_brother"){
//			 	     	#set ($isExist="false")
//			 	     	for ($colConfig in $childTableConfigList.get($rowlist.tableName)){
//			 	     		if("$config_row.fieldName"=="$colConfig.colName")
//			 	     			#set ($isExist="true")
//			 	     		}
//			 	     	}
//			 	     	if("$isExist"=="false" && "$config_row.display"!=""){
//			 	     		addCols($griddata,'$fname','$config_row.display.get("$globals.getLocale()")','$config_row.width',$config_row.fieldType,$nullable,'$defValue',$config_row.maxLength,-2,'$inputValue','',"$config_row.fieldIdentityStr","$config_row.copyType","$config_row.listOrder",'$config_row.groupDisplay.get("$globals.getLocale()")');	
//							putChildValidateItem("$fname","$config_row.display.get("$globals.getLocale()")","$config_row.getStringType()","$row.fieldSysType",$config_row.getSringNull(),minValue,$config_row.getStringLength(),$config_row.getSringDefault(),'$defValue');	
//			 	     	}
//			 	     }
//			   }
//		    }else{
//		       for ($row in $rowlist.fieldInfos ){
//		       	  #set($fname = $rowlist.tableName+"_"+$row.fieldName)
//				    if("$row.inputType" != "100" && $row.fieldName != "id" && $row.fieldName != "f_ref" && $row.fieldName != "f_brother")	{			  
//						  #set($defValue="")
//						  if($values.get($fname).length()>0||$row.getDefValue().indexOf("@Sess:")>=0){
//							#set($defValue=$values.get($fname)) 
//						  }else if($globals.getUrlBillDef($fname).length()>0){
//						  	#set($defValue=$globals.getUrlBillDef($fname))
//						  }else if($globals.getUrlBillDef($fname).length()==0){
//							#set($defValue=$row.getDefValue())
//						  }
//					      #set($nullable = "false")
//						  if("$row.isNull" == "0"){
//						    #set($nullable = "true")
//						  }
//						
//						  #set($inputValue = "") 
//						  if("$row.inputType" == "1" ){
//							  for($erow in $globals.getEnumerationItems($row.refEnumerationName)){
//								  #set($inputValue = "$inputValue;$erow.value:$erow.name")
//							  }
//							 addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,$row.inputType,'$inputValue','',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');
//							 putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue',true);				
//						  }else if("$row.inputType" == "16" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "16")){
//							 addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,$row.inputType,inputType_$row.fieldName,'',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');
//							 putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue',true);				
//						  }else if("$row.inputType" == "5"){
//						  	  for($erow in $globals.getEnumerationItems($row.refEnumerationName)){
//								  #set($inputValue = "$inputValue;$erow.value:$erow.name")
//							  }
//						      addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,$row.inputType,'$inputValue','',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');						  
//						  }else if("$row.inputType" == "2"){
//						  	  //设置自动插入表的字段值
//							#set($ajaxStr=":")
//							if($row.insertTable.length()>0){
//							for($srow in $row.getSelectBean().viewFields){
//								#set($ajaxStr=$ajaxStr+$rowlist.tableName+"_"+$globals.getTableField($srow.asName)+";")
//							}
//							}
//								
//							  #set($inputValue = "$rowlist.tableName:$row.fieldName")
//							  #set($inputValue2 = "$rowlist.tableName"+"_$row.fieldName")				
//							  var param4="";
//							for($mainField in $row.getSelectBean().tableParams){
//								if($mainField.indexOf("@TABLENAME")>=0){
//									#set($index=$mainField.indexOf("_")+1)
//									#set($param=$rowlist.tableName+"_"+$mainField.substring($index))
//									#set($disP=$rowlist.tableName+"."+$mainField.substring($index))
//									param4=param4+":$globals.getFieldBean($rowlist.tableName,$mainField.substring($index)).isNull$globals.getFieldDisplay($disP)@$param";
//								}else{ 
//									if(($mainField.indexOf($rowlist.tableName)==-1 &&$globals.getFieldBean($tableName,$mainField).inputType!=100)||($mainField.indexOf($rowlist.tableName)>=0)){
//										#set($disP=$tableName+"."+$mainField) param4=param4+":$globals.getFieldBean($tableName,$mainField).isNull$globals.getFieldDisplay($disP)@$mainField";											
//									}
//								}
//							}
//							
//							if($row.getSelectBean().relationKey.hidden){
//							     if("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"){
//								 	addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,-2,'','',"$row.fieldIdentityStr","$row.copyType","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');
//									addCols($griddata,'$fname'+'_hid','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,$row.inputType,'@SEQ$row.logicValidate',"$row.popupType","$row.fieldIdentityStr","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');
//									putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue',true);
//								 }else{
//									addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,-2,'','',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');
//									putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue');
//								 }
//							}else{
//								if("$row.inputType" == "2" and "$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"){
//									addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,-2,'','',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');
//									addCols($griddata,'$fname'+'_hid','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,$row.inputType,'@SEQ$row.logicValidate',"$row.popupType","$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');
//									putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue',true);
//								}else{		
//									addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,$row.inputType,'$inputValue:$inputValue2:'+param4,"$row.popupType","$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');
//									putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue',true);
//					  			}
//						    }
//						 if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1")){
//						 	
//							for($srow in $row.getSelectBean().viewFields){
//								 if($srow.display.length()>0){
//								 	#set($index=$srow.display.indexOf(".")+1)
//									#set($tableField=$rowlist.tableName+"."+$srow.display.substring($index))
//									#set($dis = $globals.getFieldDisplay2($srow.display,$moduleUrl))
//									if($dis==""||$dis.indexOf("not Exist")>=0){
//										#set($dis = $globals.getFieldDisplay2($tableField,$moduleUrl))
//									}
//								 }else{
//								 	#set($dis="")
//								 }				
//								 if($dis == "") {
//									#set($dis = $globals.getFieldDisplay($rowlist.tableName,$row.getSelectBean().name,$srow.fieldName,$moduleUrl)) 
//								 }
//								 #set($disField = $rowlist.tableName+"_"+$globals.getTableField($srow.asName))	
//								 #set($returnValue=$globals.getFieldWidth("$rowlist.tableName","$globals.getTableField($srow.asName)"))
//								 if($returnValue!=0){
//									#set($disWidth=$returnValue) 
//								 }else{
//									#set($disWidth=$srow.width) 
//								 }
//								if($values.get("$disField").length()>0){#set($defValue=$values.get($disField))}
//							    if("$srow.hiddenInput"!="true")	{		        
//									addCols($griddata,'$disField','$dis','$disWidth',2,true,'$defValue',1000000,$row.inputType,'$inputValue:$disField$ajaxStr'+param4,'',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');	
//							 	}else{
//							 		addCols($griddata,'$disField','$dis','$disWidth',$row.fieldType,$nullable,'$defValue',$row.maxLength,-2,'$inputValue','',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');	
//							    }
//							}
//					     }
//						}else if("$row.inputType" == "3" || "$row.inputType" == "6"){
//							addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,-2,'$inputValue','',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');	
//							putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue');		
//						}else if("$row.inputType" == "4"){
//							#set($fhname=$fname+"_lan")		
//							addCols($griddata,'$fhname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,$row.inputType,'$inputValue','',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');	
//							putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue');	
//				
//							addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,-2,'$inputValue','',"$row.fieldIdentityStr","$row.listOrder","$row.copyType",'$row.groupDisplay.get("$globals.getLocale()")');		
//							putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue');		
//						}else{	
//							addCols($griddata,'$fname','$row.display.get("$globals.getLocale()")','$row.width',$row.fieldType,$nullable,'$defValue',$row.maxLength,$row.inputType,'$inputValue','',"$row.fieldIdentityStr","$row.copyType","$row.listOrder",'$row.groupDisplay.get("$globals.getLocale()")');	
//							putChildValidateItem("$fname","$row.display.get("$globals.getLocale()")","$row.getStringType()","$row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),$row.getSringDefault(),'$defValue',true);	
//						}
//					}	
//
//				}
//		    }
//		}
//		return sb.toString();
//	}
}
