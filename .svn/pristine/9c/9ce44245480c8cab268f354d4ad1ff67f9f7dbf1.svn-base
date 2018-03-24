<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("com.define.colconfig")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" / >
<script type="text/javascript" src="$globals.js("/js/Main.vjs","",$text)"></script>
<script type="text/javascript"> 
function ConfigType(name,display,width){
	this.name = name ;
	this.display = display;
	this.width = width ;
}
var configCount = new Array() ;
#foreach ($rowlist in $childTableList )  
 #if($allConfigList.size()>0)
	 	#foreach ($colConfig in $childTableConfigList.get($rowlist.tableName))
	 	  #set ($row="")
	 	  #set ($isExist="false")
	 	  #foreach ($config_row in $rowlist.fieldInfos )
	 		 #if($colConfig.colName==$config_row.fieldName)
	 		 	#set ($row=$config_row)
	 		 	#set ($isExist="true")
	 		 #end
	 	  #end
	 	  #if($isExist=="false")
				#foreach ($row in $rowlist.fieldInfos )
					#if("$row.inputType" == "2" || ("$row.inputType"=="6" && "$row.inputTypeOld"=="2"))
						#set($fname = $rowlist.tableName+"_"+$row.fieldName)
						var param4="";
						 #foreach($mainField in $row.getSelectBean().tableParams)
							#if($mainField.indexOf("@TABLENAME")>=0)
								#set($index=$mainField.indexOf("_")+1)
								#set($param=$rowlist.tableName+"_"+$mainField.substring($index))
							#else			
								#if(($mainField.indexOf($rowlist.tableName)==-1 &&$globals.getFieldBean($!tableName,$mainField).inputType!=100)||($mainField.indexOf($rowlist.tableName)>=0))
									#set($disP=$!tableName+"."+$mainField)
									param4=param4+":$globals.getFieldBean($!tableName,$mainField).isNull$globals.getFieldDisplay($disP)@$mainField";											
								#end
							#end
						 #end
					   	 #if("$row.inputType"=="6" && "$row.inputTypeOld"=="2")
					   	 	#set($inputType2=$row.inputTypeOld)
					   	 #else
					   	 	#set($inputType2=$row.inputType)
					   	 #end
					   	 
						 #if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
					  	 #foreach($srow in $row.getSelectBean().viewFields)
					  	    #if("$srow.asName"=="$colConfig.colName")
								 #if($srow.display.length()>0  && $srow.display.indexOf(".") >-1)
								 	#if($srow.display.indexOf("@TABLENAME.")>=0)				
										#set($fieldDisplay=$srow.display.replaceAll("@TABLENAME",$rowlist.tableName))
									#else
										#set($fieldDisplay=$srow.display)
									#end									
									#set($dis = $globals.getFieldDisplay($fieldDisplay))
								 #else
								 	#set($dis="")
								 #end		
								 
								 #if("$!srow.display" !="" && $!srow.display.indexOf(".")==-1)
									#set($dis = $srow.display)
								 #else
									 #if($dis == "") 
										#set($dis = $globals.getFieldDisplay($rowlist.tableName,$row.getSelectBean().name,$srow.fieldName)) 
									 #end
								 #end
								 
								 
								 #set($disField = $rowlist.tableName+"_"+$globals.getTableField($srow.asName))	
								 #set($disWidth = "")
								 #set($returnValue=$globals.getFieldWidth("$rowlist.tableName","$globals.getTableField($srow.asName)"))
								 #if($returnValue!=0)
								 	#set($disWidth=$returnValue) ;
								 #else
								 	#set($disWidth=$srow.width) ;
								 #end	        
								 configCount.push(new ConfigType('$disField','$dis','$disWidth')) ; 
							#end	
						 #end
						 #end
					   #end
				  #end
		  #else
		  	  #set($fname = $rowlist.tableName+"_"+$row.fieldName)
			  #if($globals.getScopeRight($rowlist.tableName,$row.fieldName,$scopeRight))
			  	#if("$row.inputType"=="6")
					 #set($inputType2=$row.inputTypeOld)
				#else
					 #set($inputType2=$row.inputType)
				#end
			    #if("$row.inputType" != "17" && "$row.inputType" != "100" && $row.fieldName != "id" && $row.fieldName != "f_ref" && $row.fieldName != "f_brother")					
					#if("$row.inputType" == "1" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "1"))
						 configCount.push(new ConfigType('$fname','$row.display.get("$globals.getLocale()")','$row.width')) ;
				    #elseif("$row.inputType"=="2" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "2"))
					 	#set($inputValue = "$rowlist.tableName:$row.fieldName")
						#set($inputValue2 = "$rowlist.tableName"+"_$row.fieldName")
						var param4="";
						#foreach($mainField in $row.getSelectBean().tableParams)
							#if($mainField.indexOf("@TABLENAME")>=0)
								#set($index=$mainField.indexOf("_")+1)
								#set($param=$rowlist.tableName+"_"+$mainField.substring($index))
							#else			
								#if(($mainField.indexOf($rowlist.tableName)==-1 &&$globals.getFieldBean($!tableName,$mainField).inputType!=100)||($mainField.indexOf($rowlist.tableName)>=0))
									#set($disP=$!tableName+"."+$mainField)
									param4=param4+":$globals.getFieldBean($!tableName,$mainField).isNull$globals.getFieldDisplay($disP)@$mainField";											
								#end
							#end
						#end
						#if($row.getSelectBean().relationKey.hidden)		
								configCount.push(new ConfigType('$fname','$row.display.get("$globals.getLocale()")','$row.width')) ;
						#else
								#if("$row.inputType" == "2" and "$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1")
									configCount.push(new ConfigType('$fname'+'_hid','$row.display.get("$globals.getLocale()")','$row.width')) ;
								#else		
					  				configCount.push(new ConfigType('$fname','$row.display.get("$globals.getLocale()")','$row.width')) ;
					  			#end
						#end
				    #elseif("$row.inputType" == "5" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "5"))
					      configCount.push(new ConfigType('$fname','$row.display.get("$globals.getLocale()")','$row.width')) ;
					#elseif("$row.inputType" == "4" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "4"))
						#set($fhname=$fname+"_lan")		
						configCount.push(new ConfigType('$fhname','$row.display.get("$globals.getLocale()")','$row.width')) ;
					#else	
						configCount.push(new ConfigType('$fname','$row.display.get("$globals.getLocale()")','$row.width')) ;
					#end
				#end
			  #end
		    #end
	    #end
 #else
       #foreach ($row in $rowlist.fieldInfos )
       	  #set($fname = $rowlist.tableName+"_"+$row.fieldName)
		  #if($globals.getScopeRight($rowlist.tableName,$row.fieldName,$scopeRight))
		    #if("$row.inputType" != "100" && "$row.inputType" != "17" && "$row.inputType" != "3" && "$row.inputType"!= "6" && $row.fieldName != "id" && $row.fieldName != "f_ref" && $row.fieldName != "f_brother")				  		
				  #if("$row.inputType" == "1" )
					 configCount.push(new ConfigType('$fname','$row.display.get("$globals.getLocale()")','$row.width')) ;
				  #elseif("$row.inputType" == "5")
				      configCount.push(new ConfigType('$fname','$row.display.get("$globals.getLocale()")','$row.width')) ;
				  #elseif("$row.inputType" == "2")					
					#if($row.getSelectBean().relationKey.hidden)
					     #if("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1")
							configCount.push(new ConfigType('$fname'+'_hid','$row.display.get("$globals.getLocale()")','$row.width')) ;
						 #end
					#else
						#if("$row.inputType" == "2" and "$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1")
							configCount.push(new ConfigType('$fname','$row.display.get("$globals.getLocale()")','$row.width')) ;
						#else		
							configCount.push(new ConfigType('$fname','$row.display.get("$globals.getLocale()")','$row.width')) ;
			  			#end
				    #end
				 	#if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
						#foreach($srow in $row.getSelectBean().viewFields)
							 #if($srow.display.length()>0 && $srow.display.indexOf(".") >-1)
							 	#if($srow.display.indexOf("@TABLENAME.")>=0)				
							 		#set($index=$srow.display.indexOf(".")+1)
							 		#set($tableField=$rowlist.tableName+"."+$srow.display.substring($index))
									#set($dis = $globals.getFieldDisplay($tableField))
								#else
									#set($dis=$globals.getFieldDisplay($srow.display))
								#end
							 #else
							 	#set($dis="")
							 #end	
							 #if("$!srow.display" !="" && $!srow.display.indexOf(".")==-1)
								#set($dis = $srow.display)
							 #else
								 #if($dis == "") 
									#set($dis = $globals.getFieldDisplay($rowlist.tableName,$row.getSelectBean().name,$srow.fieldName)) 
								 #end
							 #end
							 
							 #set($disField = $rowlist.tableName+"_"+$globals.getTableField($srow.asName))	
							 #set($returnValue=$globals.getFieldWidth("$rowlist.tableName","$globals.getTableField($srow.asName)"))
							 #if($returnValue!=0)
								#set($disWidth=$returnValue) 
							 #else
								#set($disWidth=$srow.width) 
							 #end
						    #if("$!srow.hiddenInput"!="true")			        
								configCount.push(new ConfigType('$disField','$dis','$disWidth')) ;
						    #end
						#end
			    	#end
				#elseif("$row.inputType" == "4")
					#set($fhname=$fname+"_lan")		
					configCount.push(new ConfigType('$fhname','$row.display.get("$globals.getLocale()")','$row.width')) ;
				#else	
					configCount.push(new ConfigType('$fname','$row.display.get("$globals.getLocale()")','$row.width')) ;
				#end
			#end	
		  #end
		#end
    #end
#end

function colWidthSet(){
    document.getElementById("colWidthSetting").style.display="block" ;	
    document.getElementById("colNameSetting").style.display="none" ;	
}

function returnColConfig(){
    document.getElementById("colWidthSetting").style.display="none" ;	
    document.getElementById("colNameSetting").style.display="block" ;	
}

function keyIsNumber()
{
	var keyCode = window.event.keyCode ;
	if(((keyCode>47)&&(keyCode<58))|| ((keyCode>95)&&(keyCode<106)) 
		|| (keyCode==8)||(keyCode==46)){
	}else if(keyCode==9 || keyCode==13){
		window.event.keyCode=9;
	}else{
		window.event.returnValue=false;
	}
}

function colNameSet(strUrl){
	//window.parent.fillColConfig("colNameSet@@"+strUrl);
	strUrl = strUrl+"&popWinName=customNamePopup";
	asyncbox.open({id:'customNamePopup',title:'列名配置',url:strUrl,width:700,height:430});	
	window.parent.jQuery.close('customPopup');
}
</script>
</head>

<body>
<div class="Heading">
	<div class="HeadingTitlesmall">$text.get("com.define.colconfig")</div>
	<ul class="HeadingButton">
		<li><span onClick="javascript:window.parent.jQuery.close('customPopup');" class="hBtns">$text.get("com.lb.close")</span></li>
	</ul>
	<br />
	<div align="center" style=" clear:both; margin-top:20px; ">
	<div id="DivCustomSetTable" align="center" style="width:480px;background: #f9f9f9 ;border: 1px solid #bbb">
    <div id="colNameSetting" style="display: block;">
    <table width="100%" align="center" border=0 id="CustomSetTable" cellpadding="0" cellspacing="0" class="BgTable">
	<tr style=" height:25px;background:#eee;background-image:-webkit-linear-gradient(top,#f9f9f9,#f2f2f2);background-image:linear-gradient(top,#f9f9f9,#e1e1e1);">
       <td style="border-bottom: 1px solid #bbb;" colspan="5" width="300">&nbsp;$text.get("com.define.colconfig")</td>
       <td style="border-bottom: 1px solid #bbb;" align="right" >&nbsp;
         
        </td>
      </tr>
	  <tr>
	  <td height="5"></td>
	  </tr>
      <tr align="center" valign="middle" class="BgRow">
        <td>
          <fieldset style="border:0;font-size:12px;">
            <legend>$text.get("com.define.canselect")</legend>   
			<select name='FieldsToSelectList' id='FieldsToSelectList' multiple="multiple" size="10" class="MultiSelect" style="width:130px" >

			#if($allConfigList.size()>0)    
			    #foreach($row in $mainTable.fieldInfos)    
			    	#if($row.inputType!="3" && $row.inputType!="17" && $row.inputType!="100" && $row.display!="")
			    		#if($row.inputType!="3" && $row.inputType!="100" && $row.display!="" && "$row.fieldName"!="id" && "$row.fieldName"!="f_ref" && "$row.fieldName"!="f_brother")
				    		#if(($row.inputType=="2" ||("$row.inputType"=="6" && "$row.inputTypeOld"=="2") ||("$row.inputType"=="8" && "$row.inputTypeOld"=="2")) 
				    														&& $row.getSelectBean().viewFields.size()>0)
							    #if("$row.fieldSysType"=="GoodsField" && $globals.getPropBean($row.fieldName).isSequence=="1")
									#set ($childField=$mainTable.tableName+":"+$row.fieldName+":"+$row.fieldName+":"+$row.fieldName+":0")
									#set ($found ="false")
									#foreach ($colConfig in $allConfigList)
							 			#set ($fieldStr="$colConfig.tableName"+":"+$colConfig.fieldName+":"+$colConfig.colName+":"+$colConfig.display+":$colConfig.isNull")
							 			#if( $fieldStr == $childField) #set ($found ="true") #end
							 		#end
									#if($found =="false") 
									##<option value="$childField">$row.display.get("$globals.getLocale()")</option>
									#end
								#else
									#foreach($field in $row.getSelectBean().viewFields)
										#if($field.parentDisplay==true && "$!field.display"=="")
											#set ($childField=$mainTable.tableName+":"+$row.fieldName+":"+$field.asName+":"+$field.asName+":0")
											#set ($found ="false")
											#foreach ($colConfig in $allConfigList)
									 			#set ($fieldStr="$colConfig.tableName"+":"+$colConfig.fieldName+":"+$colConfig.colName+":"+$colConfig.display+":$colConfig.isNull")
									 			#if( $fieldStr == $childField) #set ($found ="true") #end
									 		#end
											#if($found =="false") 
											<option value="$childField">$globals.getFieldDisplay($mainTable.tableName,$row.getSelectBean().name,$field.fieldName)</option>
											#end
										#elseif($field.parentDisplay==true && "$!field.display"!="")
											#set ($mainField=$mainTable.tableName+":"+$row.fieldName+":"+$field.asName+":"+$!field.display+":$row.isNull")
											#if($field.display.indexOf("@TABLENAME.")>=0)				
												#set($fieldDisplay=$field.display.replaceAll("@TABLENAME",$mainTable.tableName))
											#else
												#set($fieldDisplay=$field.display)
											#end
											#set ($found ="false")
											#foreach ($colConfig in $allConfigList)
									 			#set ($fieldStr="$colConfig.tableName"+":"+$colConfig.fieldName+":"+$colConfig.colName+":"+$colConfig.display+":$colConfig.isNull")
									 			#if( $fieldStr == $mainField) #set ($found ="true") #end
									 		#end
											#if($found =="false") 
											<option value='$mainField'>#if("$!field.display" !="" && $field.display.indexOf(".")==-1) $field.display #else  $globals.getFieldDisplay("$fieldDisplay") #end</option>
											#end
										#end
									#end	
								#end
							#else
								#set ($mainField=$mainTable.tableName+":"+$row.fieldName+":"+$row.fieldName+":"+$row.fieldName+":$row.isNull")
								#set ($found ="false")
								#foreach ($colConfig in $allConfigList)
						 			#set ($fieldStr="$colConfig.tableName"+":"+$colConfig.fieldName+":"+$colConfig.colName+":"+$colConfig.display+":$colConfig.isNull")
						 			#if( $fieldStr == $mainField) #set ($found ="true") #end
						 		#end
								#if($found =="false") 
								<option value='$mainField'>$row.display.get("$globals.getLocale()")</option>
								#end
							#end
						#end
					#end
				#end
				#foreach($rowlist2 in $childTableList2)
					#foreach ($row2 in $rowlist2.fieldInfos)
						#if($row2.inputType!="3" && $row2.inputType!="17" && $row2.inputType!="100" && $row2.display!="" && "$row2.fieldName"!="id" && "$row2.fieldName"!="f_ref" && "$row2.fieldName"!="f_brother")
							#if($row2.inputType=="2" ||("$row2.inputType"=="6" && "$row2.inputTypeOld"=="2")||("$row2.inputType"=="8" && "$row2.inputTypeOld"=="2"))
								#if("$row2.fieldSysType"=="GoodsField" && $globals.getPropBean($row2.fieldName).isSequence=="1")
									#set ($childField=$rowlist2.tableName+":"+$row2.fieldName+":"+$row2.fieldName+":"+$row2.fieldName+":0")
									#set ($found ="false")
									#foreach ($colConfig in $allConfigList)
							 			#set ($fieldStr="$colConfig.tableName"+":"+$colConfig.fieldName+":"+$colConfig.colName+":"+$colConfig.display+":$colConfig.isNull")
							 			#if( $fieldStr == $childField) #set ($found ="true") #end
							 		#end
									#if($found =="false")
									##<option value="$childField">$text.get("common.lb.detailList")-$row2.display.get("$globals.getLocale()")</option>
									#end
								#else
									#if("$row2.getSelectBean().relationKey.relationKey"=="true" && "$row2.getSelectBean().relationKey.hidden"=="false")
										#set ($childField=$rowlist2.tableName+":"+$row2.fieldName+":"+$row2.fieldName+":"+$row2.fieldName+":0")
										#set ($found ="false")
										#foreach ($colConfig in $allConfigList)
								 			#set ($fieldStr="$colConfig.tableName"+":"+$colConfig.fieldName+":"+$colConfig.colName+":"+$colConfig.display+":$colConfig.isNull")
								 			#if( $fieldStr == $childField) #set ($found ="true") #end
								 		#end
										#if($found =="false")
										<option value="$childField">$text.get("common.lb.detailList")-$row2.display.get("$globals.getLocale()")</option>
										#end
									#elseif($row2.getSelectBean().viewFields.size()==0)
										#set ($childField2=$rowlist2.tableName+":"+$row2.fieldName+":"+$row2.fieldName+":"+$row2.fieldName+":$row2.isNull")
										#set ($found ="false")
										#foreach ($colConfig in $allConfigList)
								 			#set ($fieldStr="$colConfig.tableName"+":"+$colConfig.fieldName+":"+$colConfig.colName+":"+$colConfig.display+":$colConfig.isNull")
								 			#if( $fieldStr == $childField2) #set ($found ="true") #end
								 		#end
										#if($found =="false")
										<option value="$childField2">$text.get("common.lb.detailList")-$row2.display.get("$globals.getLocale()")</option>
										#end
									#end
									#foreach($field in $row2.getSelectBean().viewFields)
										#if($field.parentDisplay==true && "$!field.display"=="")
											#set ($childField=$rowlist2.tableName+":"+$row2.fieldName+":"+$!field.asName+":"+$!field.asName+":0")
											#set ($found ="false")
											#foreach ($colConfig in $allConfigList)
									 			#set ($fieldStr="$colConfig.tableName"+":"+$colConfig.fieldName+":"+$colConfig.colName+":"+$colConfig.display+":$colConfig.isNull")
									 			#if( $fieldStr == $childField) #set ($found ="true") #end
									 		#end
											#if($found =="false")
											<option value="$childField">$text.get("common.lb.detailList")-$globals.getFieldDisplay($rowlist2.tableName,$row2.getSelectBean().name,$field.fieldName)</option>
											#end
										#elseif($field.parentDisplay==true && "$!field.display"!="")
											#set ($childField=$rowlist2.tableName+":"+$row2.fieldName+":"+$!field.asName+":"+$!field.display+":$row2.isNull")
											#if($field.display.indexOf("@TABLENAME.")>=0)				
												#set($fieldDisplay=$field.display.replaceAll("@TABLENAME",$rowlist2.tableName))
											#else
												#set($fieldDisplay=$field.display))
											#end		
											#set ($found ="false")
											#foreach ($colConfig in $allConfigList)
									 			#set ($fieldStr="$colConfig.tableName"+":"+$colConfig.fieldName+":"+$colConfig.colName+":"+$colConfig.display+":$colConfig.isNull")
									 			#if( $fieldStr == $childField) #set ($found ="true") #end
									 		#end
											#if($found =="false")
											<option value="$childField">$text.get("common.lb.detailList")- #if("$!field.display" !="" && $field.display.indexOf(".")==-1) $field.display #else  $globals.getFieldDisplay("$fieldDisplay") #end</option>
											#end
										#end
									#end
							  #end	
							#else
								#set ($childField2=$rowlist2.tableName+":"+$row2.fieldName+":"+$row2.fieldName+":"+$row2.fieldName+":$row2.isNull")
								#set ($found ="false")
								#foreach ($colConfig in $allConfigList)
						 			#set ($fieldStr="$colConfig.tableName"+":"+$colConfig.fieldName+":"+$colConfig.colName+":"+$colConfig.display+":$colConfig.isNull")
						 			#if( $fieldStr == $childField2) #set ($found ="true") #end
						 		#end
								#if($found =="false")
								<option value="$childField2">$text.get("common.lb.detailList")-$row2.display.get("$globals.getLocale()")</option>
								#end
							#end
						#end
					#end
				#end
			#else 
			    #foreach($row in $mainTable.fieldInfos)
		    		#if($row.inputType!="3" && $row.inputType!="17" && $row.inputType!="100" && $row.display!="" 
		    						&& "$row.fieldName"!="id" && "$row.fieldName"!="f_ref" && "$row.fieldName"!="f_brother")
			    		#if(($row.inputType=="2" ||("$row.inputType"=="6" && "$row.inputTypeOld"=="2")) 
			    														&& $row.getSelectBean().viewFields.size()>0)
						    #if("$row.fieldSysType"=="GoodsField" && $globals.getPropBean($row.fieldName).isSequence=="1")
							#else
								#foreach($field in $row.getSelectBean().viewFields)
									#if($field.parentDisplay==true && ("$field.hiddenInput"=="true" || "$row.inputType"=="6") && "$!field.display"=="")
										#set ($childField=$mainTable.tableName+":"+$row.fieldName+":"+$field.asName+":"+$field.asName+":0")
										<option value="$childField">$globals.getFieldDisplay($mainTable.tableName,$row.getSelectBean().name,$field.fieldName)</option>
									#elseif($field.parentDisplay==true && ("$field.hiddenInput"=="true" || "$row.inputType"=="6") && "$!field.display"!="")
										#set ($mainField=$mainTable.tableName+":"+$row.fieldName+":"+$field.asName+":"+$field.display+":$row.isNull")
										#if($field.display.indexOf("@TABLENAME.")>=0)				
											#set($fieldDisplay=$field.display.replaceAll("@TABLENAME",$mainTable.tableName))
										#else
											#set($fieldDisplay=$field.display)
										#end
										<option value='$mainField'>#if("$!field.display" !="" && $field.display.indexOf(".")==-1) $field.display #else  $globals.getFieldDisplay("$fieldDisplay") #end</option>
									#end
								#end	
							#end
						#elseif($row.inputType=="6" )
							#set ($mainField=$mainTable.tableName+":"+$row.fieldName+":"+$row.fieldName+":"+$row.fieldName+":$row.isNull")
							<option value='$mainField'>$row.display.get("$globals.getLocale()")</option>
						#end	
					#end
				#end
				#foreach($rowlist2 in $childTableList2)
					#foreach ($row2 in $rowlist2.fieldInfos)
						#if($row2.inputType!="3" && $row2.inputType!="17" && $row2.inputType!="100" && $row2.display!="" && "$row2.fieldName"!="id" && "$row2.fieldName"!="f_ref" && "$row2.fieldName"!="f_brother")
							#if($row2.inputType=="2" ||("$row2.inputType"=="6" && "$row2.inputTypeOld"=="2"))
								#if("$row2.fieldSysType"=="GoodsField" && $globals.getPropBean($row2.fieldName).isSequence=="1")
									
								#else									
									#foreach($field in $row2.getSelectBean().viewFields)
										#if($field.parentDisplay==true && ("$field.hiddenInput"=="true" || "$row2.inputType"=="6") && "$!field.display"=="")
											#set ($childField=$rowlist2.tableName+":"+$row2.fieldName+":"+$field.asName+":"+$field.asName+":0")
											<option value="$childField">$text.get("common.lb.detailList")-$globals.getFieldDisplay($rowlist2.tableName,$row2.getSelectBean().name,$field.fieldName)</option>
										#elseif($field.parentDisplay==true && ("$field.hiddenInput"=="true" || "$row2.inputType"=="6") && "$!field.display"!="")
											#set ($childField=$rowlist2.tableName+":"+$row2.fieldName+":"+$field.asName+":"+$field.display+":$row2.isNull")
											#if($field.display.indexOf("@TABLENAME.")>=0)				
												#set($fieldDisplay=$field.display.replaceAll("@TABLENAME",$rowlist2.tableName))
											#else
												#set($fieldDisplay=$field.display)
											#end		
											<option value="$childField">$text.get("common.lb.detailList")-#if("$!field.display" !="" && $field.display.indexOf(".")==-1) $field.display #else  $globals.getFieldDisplay("$fieldDisplay") #end</option>
										#end
									#end
									#if($row2.getSelectBean().viewFields.size()==0 && ("$row2.inputType"=="6" && "$row2.inputTypeOld"=="2"))
									#set ($childField2=$rowlist2.tableName+":"+$row2.fieldName+":"+$row2.fieldName+":"+$row2.fieldName+":$row2.isNull")
									<option value="$childField2">$text.get("common.lb.detailList")-$row2.display.get("$globals.getLocale()")</option>
									#end
							  #end
							#elseif($row2.inputType=="6")
								#set ($childField2=$rowlist2.tableName+":"+$row2.fieldName+":"+$row2.fieldName+":"+$row2.fieldName+":$row2.isNull")
								<option value="$childField2">$text.get("common.lb.detailList")-$row2.display.get("$globals.getLocale()")</option>
							#end  
						#end
					#end
				#end			
			#end	
			</select>
          </fieldset>
        </td>
        <td>
          #if($childTableList.size()>0)
          <span class="ActionButton hBtns" style="width:75px" onClick="colWidthSet();">$text.get("com.config.colwidth")</span><br><br>
          #end
          #if(!$moduleTable)
          <span class="ActionButton hBtns" title="$text.get('com.config.right.move')" style="width:75px" onClick="addItem('FieldsToSelectList','FieldsToShowList')">&gt;&gt;</span><br/><br/>
          <span class="ActionButton hBtns" title="$text.get('com.config.left.move')" style="width:75px" onClick="delItem('FieldsToShowList','FieldsToSelectList')">&lt;&lt;</span><br/><br/>
          #end
          #if("$!noback"=="false")
          <button type="button" class="ActionButton" style="width:75px" onClick="colNameSet('/ColDisplayAction.do?operation=7&keyId=$mainTable.tableName&fromConfig=config');">$text.get("com.colName.setting")</button>
          #end
        </td>
        <td>
          <fieldset style="border:0;font-size:12px;">
           <legend>$text.get("com.define.myselect")</legend>
		   <select name='FieldsToShowList' id='FieldsToShowList' multiple="multiple" size="10" class="MultiSelect" style="width:130px" >
 		#if($allConfigList.size()>0)
	 		#foreach ($colConfig in $allConfigList)
	 			#set ($fieldStr="$colConfig.tableName"+":"+$colConfig.fieldName+":"+$colConfig.colName+":"+$colConfig.display+":$colConfig.isNull")
	 			#if($colConfig.display.indexOf(".")>=0)
	 				#set ($display=$colConfig.display.replaceAll("@TABLENAME",$colConfig.tableName))
	 			#else
	 				#set ($display="$colConfig.tableName."+"$colConfig.display")
	 			#end 
	 			<option value="$fieldStr">#if("$colConfig.tableName" != "$mainTable.tableName")$text.get("common.lb.detailList")-#end  $globals.getFieldDisplay("$display")</option>
	 		#end
	 	#else
			    #foreach($row in $mainTable.fieldInfos)
			    	#if($row.inputType!="3" && $row.inputType!="17" && $row.inputType!="6" && $row.inputType!="100" && $row.display!="" && "$row.fieldName"!="id" && "$row.fieldName"!="f_brother")
			    		#if(($row.inputType=="2" || ($row.inputType=="8" && $row.inputTypeOld=="2")) && $row.getSelectBean().viewFields.size()>0)
						  	#if("$row.fieldSysType"=="GoodsField" && $globals.getPropBean($row.fieldName).isSequence=="1")
									#set ($childField=$mainTable.tableName+":"+$row.fieldName+":"+$row.fieldName+":"+$row.fieldName+":1")
									<option value="$childField">$row.display.get("$globals.getLocale()")</option>
							#else 
								#foreach($field in $row.getSelectBean().viewFields)
									#if($field.parentDisplay==true && "$field.hiddenInput"!="true" && "$!field.display"=="")
										#set ($childField=$mainTable.tableName+":"+$row.fieldName+":"+$field.asName+":"+$field.asName+":0")
										<option value="$childField">$globals.getFieldDisplay($mainTable.tableName,$row.getSelectBean().name,$field.fieldName)</option>
									#elseif($field.parentDisplay==true  && "$field.hiddenInput"!="6" && "$!field.display"!="")
										#set ($mainField=$mainTable.tableName+":"+$row.fieldName+":"+$field.asName+":"+$field.display+":$row.isNull")
										#if($field.display.indexOf("@TABLENAME.")>=0)				
											#set($fieldDisplay=$field.display.replaceAll("@TABLENAME",$mainTable.tableName))
										#else
											#set($fieldDisplay=$field.display)
										#end
										<option value='$mainField'>#if("$!field.display" !="" && $field.display.indexOf(".") == -1) $field.display #else $globals.getFieldDisplay("$fieldDisplay") #end</option>
									#end
								#end	
							#end	
						#else
				    		#set ($mainField=$mainTable.tableName+":"+$row.fieldName+":"+$row.fieldName+":"+$row.fieldName+":$row.isNull")
							<option value='$mainField'>$row.display.get("$globals.getLocale()") </option>
						#end
					#end
				#end
				#foreach($rowlist2 in $childTableList)
					#foreach ($row2 in $rowlist2.fieldInfos)
						#if($row2.inputType!="3" && $row2.inputType!="17" && $row2.inputType!="6" && $row2.inputType!="100" && $row2.display!=""  && "$row2.fieldName"!="id" && "$row2.fieldName"!="f_ref" && "$row2.fieldName"!="f_brother")
							#if(($row2.inputType=="2" || ($row2.inputType=="8" && $row2.inputTypeOld=="2")))
								#if("$row2.fieldSysType"=="GoodsField" && $globals.getPropBean($row2.fieldName).isSequence=="1")
									#set ($childField=$rowlist2.tableName+":"+$row2.fieldName+":"+$row2.fieldName+":"+$row2.fieldName+":1")
									<option value="$childField">$text.get("common.lb.detailList")-$row2.display.get("$globals.getLocale()")</option>
								#else
									#if("$row2.getSelectBean().relationKey.relationKey"=="true" && "$row2.getSelectBean().relationKey.hidden"=="false")
										#set ($childField=$rowlist2.tableName+":"+$row2.fieldName+":"+$row2.fieldName+":"+$row2.fieldName+":0")
										<option value="$childField">$text.get("common.lb.detailList")-$row2.display.get("$globals.getLocale()")</option>
									#elseif($row2.getSelectBean().viewFields.size()==0)
										#set ($childField2=$rowlist2.tableName+":"+$row2.fieldName+":"+$row2.fieldName+":"+$row2.fieldName+":$row2.isNull")
										<option value="$childField2">$text.get("common.lb.detailList")-$row2.display.get("$globals.getLocale()")</option>
									#end
									#foreach($field in $row2.getSelectBean().viewFields)
										#if($field.parentDisplay==true && "$field.hiddenInput"!="true" && "$!field.display"=="")
											#set ($childField=$rowlist2.tableName+":"+$row2.fieldName+":"+$field.asName+":"+$field.asName+":0")
											<option value="$childField">$text.get("common.lb.detailList")-$globals.getFieldDisplay($rowlist2.tableName,$row2.getSelectBean().name,$field.fieldName)</option>
										#elseif($field.parentDisplay==true && "$field.hiddenInput"!="true" && "$!field.display"!="")
											#set ($childField=$rowlist2.tableName+":"+$row2.fieldName+":"+$field.asName+":"+$field.display+":$row2.isNull")
											#if($field.display.indexOf("@TABLENAME.")>=0)				
												#set($fieldDisplay=$field.display.replaceAll("@TABLENAME",$rowlist2.tableName))
											#else
												#set($fieldDisplay=$field.display)
											#end		
											<option value="$childField">$text.get("common.lb.detailList")-  #if("$!field.display" !="" && $field.display.indexOf(".") == -1) $field.display #else $globals.getFieldDisplay("$fieldDisplay") #end</option>
										#end
									#end	
								#end
							#else
								#set ($childField2=$rowlist2.tableName+":"+$row2.fieldName+":"+$row2.fieldName+":"+$row2.fieldName+":$row2.isNull")
								<option value="$childField2">$text.get("common.lb.detailList")-$row2.display.get("$globals.getLocale()")</option>
							#end
						#end 
					#end
		
				#end
		 #end
		   </select>
         </fieldset>
        </td>
        <td width="8%" align="left">
          <span class="hBtns" title="$text.get('com.config.up.move')"  style="width:30px; text-align:center" onClick="JavaScript:upItem('FieldsToShowList')">&uarr;</span><br/><br/>
          <span class="hBtns" title="$text.get('com.config.down.move')" style="width:30px; text-align:center" onClick="JavaScript:downItem('FieldsToShowList')">&darr;</span>
        </td>
      </tr>
      <tr>
        <td colspan="5" align="center" style="padding:10px 0 6px">
          <span class="hBtns" class="ActionButton" style="width: 50px;" onClick="submitForm();">$text.get("mrp.lb.save")</span>
          <span class="hBtns" style="width: 50px;" onClick="defaultForm('defaultConfig');">
          	$text.get("com.define.defaultField")</span><br/>
        </td>
      </tr>
      <tr>
      	<td colspan="5" align="left">&nbsp;</td>
      </tr>
    </table>
    <input type="hidden" name="CustomDivIsFocus"  id="CustomDivIsFocus" value="" />
    <input type="hidden" name="FieldsToShow"  id="FieldsToShow" value="" />
    </div>
    <div id="colWidthSetting" style="display: none;">
  	 <table width="100%" align="center" border=0 id="CustomSetTable2" cellpadding="0" cellspacing="0"  class="BgTable">
     <tr style=" height:25px; background:#92D8FF url(/$globals.getStylePath()/images/aiobg.gif) repeat-x left -170px; border-bottom: 1px solid #F0FCFF">
        <td colspan="5" width="300"  align="left">&nbsp;$text.get("com.config.colwidth")</td>
        <td align="right" >&nbsp;
          
        </td>
      </tr>
            <tr>
      	<td colspan="6" align="left" >&nbsp;</td>
      </tr>
			<td colspan="6">
				<div id="userSettingWidth" style="float: left;width: 430px;margin-right: 20px;margin-bottom: 7px;">
					
				</div>
			</td>
	  <tr>
	  	<td colspan="6" align="center">
	  		 <span class="hBtns" onclick="beforeUpdate();" style="width: 50px;">$text.get("common.lb.ok")</span>
	  		 <span class="hBtns" onClick="returnColConfig();">$text.get("com.return.config")</span>
          	 <span class="hBtns" style="width: 50px;" onClick="defaultForm('defaultWidth');">$text.get("com.define.defaultField")</span>
	  	</td>
	  </tr>
      <tr>
      	<td colspan="6">&nbsp;</td>
      </tr>
    </table>
  </div>
  </div>
</div>
 <script type="text/javascript">
 	var indexCol = 1 ;
 	var str = "" ;
	for(var j=0;j<configCount.length;j++){
		var configType = configCount[j] ;
		if(configType.width>0){
			var fieldName= configType.name.replace("_",":") ;
			str += "<li style=\"float: left;width: 210px;height: 22px;  line-height: 22px;\">"+
						"<span style=\"float: left;text-align:right;width: 110px;\">"+configType.display+"：</span>"+
						"<input type=\"text\" id=\"tdWidth"+indexCol+"\" name=\""+fieldName+"\" onkeydown=\"keyIsNumber();\""+
						"style=\"width: 90px; border:0px;height: 18px; border: 1px solid #006699;\" value=\""+configType.width+"\"/></li>"
				
			indexCol=indexCol+1 ;		
		}
	}
	document.getElementById("userSettingWidth").innerHTML=str ;
	function beforeUpdate(){
		var colSelect = "colWidth@@" ;
		for(var i=1;i<indexCol;i++){
			var obj = document.getElementById("tdWidth"+i) ;
			var fieldName = obj.name ;
			var colWidth = obj.value ;
			if(colWidth<=0){
				alert("$text.get('com.config.width.zero')") ;
				return ;
			}
			colSelect += fieldName+":"+colWidth+";" ;
		}
		window.parent.fillColConfig(colSelect);
		window.parent.jQuery.close('customPopup');
	}
</script>
</body>
</html>
