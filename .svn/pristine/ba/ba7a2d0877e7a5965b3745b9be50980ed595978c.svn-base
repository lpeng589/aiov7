<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("com.define.colconfig")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="$globals.js("/js/Main.vjs","",$text)"></script>
<script language="javascript"> 
function colNameSet(strUrl){
	window.parent.returnValue = "colNameSet@@"+strUrl;
	window.close() ;
}
</script>
</head>

<body>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitlesmall">$text.get("com.define.colconfig")</div>
	<ul class="HeadingButton">
		<li><button type="button" onClick="javascript:window.close();" class="b2">$text.get("com.lb.close")</button></li>
	</ul>
	<br />
	<div align="center" style=" clear:both; margin-top: 50px; ">
	<div id="DivCustomSetTable" align="center" style="width:480px;height:250px; background: #F0FCFF ;border: 1px solid #0099CC">
    <div id="colNameSetting" style="display: block;">
    <table width="100%" align="center" border=0 id="CustomSetTable" cellpadding="0" cellspacing="0" class="BgTable">
	<tr style=" height:25px; background:#92D8FF url(/$globals.getStylePath()/images/aiobg.gif) repeat-x left -170px; border-bottom: 1px solid #F0FCFF">
       <td colspan="5" width="300">&nbsp;$text.get("com.define.colconfig")</td>
       <td align="right" >&nbsp;
         
        </td>
      </tr>
	  <tr>
	  <td height="4"></td>
	  </tr>
      <tr align="center" valign="middle" class="BgRow">
        <td>
          <fieldset style="border:0;font-size:12px;">
            <legend>$text.get("com.define.canselect")</legend>   
			<select name='FieldsToSelectList' id='FieldsToSelectList' multiple="multiple" size="10" class="MultiSelect" style="width:130px" >	
			<!-- 客户资料 -->
			#if($!globals.queryConfig("detailCRMClientInfo").size()>0)
 				#foreach ($row in $!globals.getTableInfoBean("CRMClientInfo").fieldInfos)
			 		#if($row.inputType!="3" && $row.inputType!="100" && $row.display!="" && "$row.fieldName"!="f_brother")
				    	#if(!$globals.existColConfig("detailCRMClientInfo",$row.fieldName))
				    		#if(($row.inputType=="2" || ($row.inputType!="6" && $row.inputTypeOld!="6")) && $row.getSelectBean().viewFields.size()>0)
								#foreach($field in $row.getSelectBean().displayField2)
									#if($field.parentDisplay==true && "$field.hiddenInput"!="true" && "$!field.display"=="")
										#set ($childField='detailCRMClientInfo'+":"+$field.fieldName+":0")
										<option value="$childField">$globals.getFieldDisplay('CRMClientInfo',$row.getSelectBean().name,$field.fieldName)</option>
									#elseif($field.parentDisplay==true  && "$field.hiddenInput"!="6" && "$!field.display"!="")
										#set ($mainField='detailCRMClientInfo'+":"+$field.display+":0")
										#if($field.display.indexOf("@TABLENAME.")>=0)				
											#set($fieldDisplay=$field.display.replaceAll("@TABLENAME",'CRMClientInfo'))
										#else
											#set($fieldDisplay=$field.display)
										#end
										<option value='$mainField'>$globals.getFieldDisplay("$fieldDisplay")</option>
									#end
								#end	
							#else
					    		#set ($mainField='detailCRMClientInfo'+":"+$row.fieldName+":0")
								<option value='$mainField'>$row.display.get("$globals.getLocale()")</option>
							#end
						#end
		 			#end
	 			#end
 			#else
				#foreach($row in $!globals.getTableInfoBean("CRMClientInfo").fieldInfos)
 					#if($row.inputType!="3" && $row.inputType!="100" && $row.display!="" && "$row.fieldName"!="f_brother")
			    		#if(($row.inputType=="2" ||("$row.inputType"=="6" && "$row.inputTypeOld"=="2")) 
			    														&& $row.getSelectBean().viewFields.size()>0)
							#foreach($field in $row.getSelectBean().displayField2)
								#if($field.parentDisplay==true && ("$field.hiddenInput"=="true" || "$row.inputType"=="6") && "$!field.display"=="")
									#set ($childField='detailCRMClientInfo'+":"+$field.fieldName+":0")
									<option value="$childField">$globals.getFieldDisplay('CRMClientInfo',$row.getSelectBean().name,$field.fieldName)</option>
								#elseif($field.parentDisplay==true && ("$field.hiddenInput"=="true" || "$row.inputType"=="6") && "$!field.display"!="")
									#set ($mainField='detailCRMClientInfo'+":"+$field.display+":0")
									#if($field.display.indexOf("@TABLENAME.")>=0)				
										#set($fieldDisplay=$field.display.replaceAll("@TABLENAME",'CRMClientInfo'))
									#else
										#set($fieldDisplay=$field.display)
									#end
									<option value='$mainField'>$globals.getFieldDisplay("$fieldDisplay")</option>
								#end
							#end	
						#elseif($row.inputType=="6" )
							#set ($mainField='detailCRMClientInfo'+":"+$row.fieldName+":0")
							<option value='$mainField'>$row.display.get("$globals.getLocale()")</option>
						#end	
					#end
				#end
			#end
			<!-- 联系人 -->
			#if($!globals.queryConfig("detailCRMClientInfoDet").size()>0)
 				#foreach ($row in $!globals.getTableInfoBean("CRMClientInfoDet").fieldInfos)
			 		#if($row.inputType!="3" && $row.inputType!="6" && $row.inputType!="100" && $row.display!="" && "$row.fieldName"!="f_brother")
				    	#if(!$globals.existColConfig("detailCRMClientInfoDet",$row.fieldName))
				    		#if($row.inputType=="2" && $row.getSelectBean().viewFields.size()>0)
								#foreach($field in $row.getSelectBean().displayField2)
									#if($field.parentDisplay==true && "$field.hiddenInput"!="true" && "$!field.display"=="")
										#set ($childField='detailCRMClientInfoDet'+":"+$field.fieldName+":0")
										<option value="$childField">$text.get("common.lb.detailList")-$globals.getFieldDisplay('CRMClientInfoDet',$row.getSelectBean().name,$field.fieldName)</option>
									#elseif($field.parentDisplay==true  && "$field.hiddenInput"!="6" && "$!field.display"!="")
										#set ($mainField='detailCRMClientInfoDet'+":"+$field.display+":0")
										#if($field.display.indexOf("@TABLENAME.")>=0)				
											#set($fieldDisplay=$field.display.replaceAll("@TABLENAME",'CRMClientInfoDet'))
										#else
											#set($fieldDisplay=$field.display)
										#end
										<option value='$mainField'>$text.get("common.lb.detailList")-$globals.getFieldDisplay("$fieldDisplay")</option>
									#end
								#end	
							#else
					    		#set ($mainField='detailCRMClientInfoDet'+":"+$row.fieldName+":0")
								<option value='$mainField'>$text.get("common.lb.detailList")-$row.display.get("$globals.getLocale()")</option>
							#end
						#end
		 			#end
	 			#end
 			#else
				#foreach($row in $!globals.getTableInfoBean("CRMClientInfoDet").fieldInfos)
 					#if($row.inputType!="3" && $row.inputType!="100" && $row.display!="" && "$row.fieldName"!="f_brother")
			    		#if(($row.inputType=="2" ||("$row.inputType"=="6" && "$row.inputTypeOld"=="2")) 
			    														&& $row.getSelectBean().viewFields.size()>0)
							#foreach($field in $row.getSelectBean().displayField2)
								#if($field.parentDisplay==true && ("$field.hiddenInput"=="true" || "$row.inputType"=="6") && "$!field.display"=="")
									#set ($childField='detailCRMClientInfoDet'+":"+$field.fieldName+":0")
									<option value="$childField">$globals.getFieldDisplay('CRMClientInfoDet',$row.getSelectBean().name,$field.fieldName)</option>
								#elseif($field.parentDisplay==true && ("$field.hiddenInput"=="true" || "$row.inputType"=="6") && "$!field.display"!="")
									#set ($mainField='detailCRMClientInfoDet'+":"+$field.display+":0")
									#if($field.display.indexOf("@TABLENAME.")>=0)				
										#set($fieldDisplay=$field.display.replaceAll("@TABLENAME",'CRMClientInfoDet'))
									#else
										#set($fieldDisplay=$field.display)
									#end
									<option value='$mainField'>$text.get("common.lb.detailList")-$globals.getFieldDisplay("$fieldDisplay")</option>
								#end
							#end	
						#elseif($row.inputType=="6" )
							#set ($mainField='detailCRMClientInfoDet'+":"+$row.fieldName+":0")
							<option value='$mainField'>$text.get("common.lb.detailList")-$row.display.get("$globals.getLocale()")</option>
						#end	
					#end
				#end
			#end
			</select>
          </fieldset>
        </td>
        <td>
          <button type="button" class="ActionButton" title="$text.get('com.config.right.move')" style="width:75px" onClick="addItem('FieldsToSelectList','FieldsToShowList')">&gt;&gt;</button><br/><br/>
          <button type="button" class="ActionButton" title="$text.get('com.config.left.move')" style="width:75px" onClick="delItem('FieldsToShowList','FieldsToSelectList')">&lt;&lt;</button><br/><br/>
        </td>
        <td>
          <fieldset style="border:0;font-size:12px;">
           <legend>$text.get("com.define.myselect")</legend>
		   <select name='FieldsToShowList' id='FieldsToShowList' multiple="multiple" size="10" class="MultiSelect" style="width:130px" >
		   <!-- 客户资料 -->
		   	#if($!globals.queryConfig("detailCRMClientInfo").size()>0)
 				#foreach ($colConfig in $!globals.queryConfig("detailCRMClientInfo"))
		 			#set ($fieldStr="$colConfig.tableName"+":"+$colConfig.colName+":0")
		 			#if($colConfig.colName.indexOf(".")>=0)
		 				#set ($display=$colConfig.colName.replaceAll("@TABLENAME","CRMClientInfo"))
		 			#else
		 				#set ($display="CRMClientInfo."+"$colConfig.colName")
		 			#end
		 			<option value="$fieldStr">$globals.getFieldDisplay("$display")</option>
	 			#end
 			#else
 				#foreach($row in $!globals.getTableInfoBean("CRMClientInfo").fieldInfos)
					#if($row.inputType!="3" && $row.inputType!="6" && $row.inputType!="100" && $row.display!="" && "$row.fieldName"!="f_brother")
			    		#if($row.inputType=="2" && $row.getSelectBean().viewFields.size()>0)
							#foreach($field in $row.getSelectBean().displayField2)
								#if($field.parentDisplay==true && "$field.hiddenInput"!="true" && "$!field.display"=="")
									#set ($childField='detailCRMClientInfo'+":"+$field.fieldName+":0")
									<option value="$childField">$globals.getFieldDisplay('CRMClientInfo',$row.getSelectBean().name,$field.fieldName)</option>
								#elseif($field.parentDisplay==true  && "$field.hiddenInput"!="6" && "$!field.display"!="")
									#set ($mainField='detailCRMClientInfo'+":"+$field.display+":0")
									#if($field.display.indexOf("@TABLENAME.")>=0)				
										#set($fieldDisplay=$field.display.replaceAll("@TABLENAME",'CRMClientInfo'))
									#else
										#set($fieldDisplay=$field.display)
									#end
									<option value='$mainField'>$globals.getFieldDisplay("$fieldDisplay")</option>
								#end
							#end	
						#else
				    		#set ($mainField='detailCRMClientInfo'+":"+$row.fieldName+":0")
							<option value='$mainField'>$row.display.get("$globals.getLocale()")</option>
						#end
					#end
 				#end
 			#end
 			<!-- 联系人 -->
 			#if($!globals.queryConfig("detailCRMClientInfoDet").size()>0)
 				#foreach ($colConfig in $!globals.queryConfig("detailCRMClientInfoDet"))
		 			#set ($fieldStr="$colConfig.tableName"+":"+$colConfig.colName+":$colConfig.isNull")
		 			#if($colConfig.colName.indexOf(".")>=0)
		 				#set ($display=$colConfig.colName.replaceAll("@TABLENAME","CRMClientInfoDet"))
		 			#else
		 				#set ($display="CRMClientInfoDet."+"$colConfig.colName")
		 			#end ==$fieldStr
		 			<option value="$fieldStr">$text.get("common.lb.detailList")-$globals.getFieldDisplay("$display")</option>
	 			#end
 			#else
 				#foreach($row in $!globals.getTableInfoBean("CRMClientInfoDet").fieldInfos)
					#if($row.inputType!="3" && $row.inputType!="6" && $row.inputType!="100" && $row.display!="" && "$row.fieldName"!="f_brother")
			    		#if($row.inputType=="2" && $row.getSelectBean().viewFields.size()>0)
							#foreach($field in $row.getSelectBean().displayField2)
								#if($field.parentDisplay==true && "$field.hiddenInput"!="true" && "$!field.display"=="")
									#set ($childField='detailCRMClientInfo'+":"+$field.fieldName+":0")
									<option value="$childField">$text.get("common.lb.detailList")-$globals.getFieldDisplay('CRMClientInfoDet',$row.getSelectBean().name,$field.fieldName)</option>
								#elseif($field.parentDisplay==true  && "$field.hiddenInput"!="6" && "$!field.display"!="")
									#set ($mainField='detailCRMClientInfoDet'+":"+$field.display+":0")
									#if($field.display.indexOf("@TABLENAME.")>=0)				
										#set($fieldDisplay=$field.display.replaceAll("@TABLENAME",'CRMClientInfoDet'))
									#else
										#set($fieldDisplay=$field.display)
									#end
									<option value='$mainField'>$text.get("common.lb.detailList")-$globals.getFieldDisplay("$fieldDisplay")</option>
								#end
							#end	
						#else
				    		#set ($mainField='detailCRMClientInfoDet'+":"+$row.fieldName+":0")
							<option value='$mainField'>$text.get("common.lb.detailList")-$row.display.get("$globals.getLocale()")</option>
						#end
					#end
 				#end
 			#end
		   </select>
         </fieldset>
        </td>
        <td width="8%" align="left">
          <button type="button" title="$text.get('com.config.up.move')"  style="width:30px; text-align:center" onClick="JavaScript:upItem('FieldsToShowList')">&uarr;</button><br/><br/>
          <button type="button" title="$text.get('com.config.down.move')" style="width:30px; text-align:center" onClick="JavaScript:downItem('FieldsToShowList')">&darr;</button>
        </td>
      </tr>
      <tr>
        <td colspan="4" align="center" style="padding:10px 0 6px">
          <button type="button" class="ActionButton" style="width: 50px;" onClick="submitForm();">$text.get("mrp.lb.save")</button>
          <!-- 
          <button type="button" class="ActionButton" style="width: 50px;" onClick="defaultForm('defaultConfig');"/>$text.get("com.define.defaultField")<br/>
           -->
        </td>
      </tr>
      <tr>
      	<td colspan="4" align="left">&nbsp;</td>
      </tr>
    </table>
  </div>
</div>
</body>
</html>
