<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("com.module.popup")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript">
function closeSubmit(){
	var items = document.getElementsByName("keyIds");
	var ids="";
	for(var i=0;i<items.length;i++){
	   if(items[i].checked){
	       ids=ids+items[i].value+"|";
	   }	    
	}
	if(ids==""){
		alert("$text.get("oa.popup.select.one")") ;
		return false ;
	}
	window.parent.returnValue = ids;
	window.close();
}
</script>
</head>
<body onLoad="showStatus();" scroll="no">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitlesmall">$text.get("com.module.popup")</div>
	<ul class="HeadingButton_Pop-up">
		<li><button type="button" onClick="closeSubmit()">$text.get("common.lb.ok")</button></li>
		<li><button type="button" onClick="window.close();">$text.get("common.lb.close")</button></li>	
	</ul>	
</div>
<div class="listRange_Pop-up_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list" name="table" style="text-align: center;">
		<thead>
			<tr>				
				<td width="40"><input type="checkbox" name="selAll" onClick="checkAll('keyIds')"></td>
				<td width="120">$text.get("tableInfo.lb.tabeDisplayName")</td>
				<td width="120">$text.get("scope.lb.fieldName")</td>
				<td width="120">$text.get("com.popup.fields")</td>
				<td width="100">$text.get("com.popup.type")</td>
			</tr>			
		</thead>
		<tbody>
		#set($reportBean=$globals.getReportFieldDisplay($request.getParameter("tableName")))
		#foreach($reportField in $reportBean.disFields2)
			#if($reportField.inputType==2)
				#foreach($srow in $globals.getPopupFieldDisplay($reportField.popUpName))
					#if($srow.display.length()>0)
						#set($index=$srow.display.indexOf(".")+1)
						#set($tableField=$rowlist.tableName+"."+$srow.display.substring($index))
						#set($dis = $globals.getFieldDisplay($tableField))
						#set($fieldName=$tableField)
					#else
						#set($dis="")
					#end				
					#if($dis == "") 
						#set($dis = $globals.getFieldDisplay($srow.fieldName)) 
						#set($fieldName=$srow.fieldName)
					#end
				<tr>
					<td align="left"><input type="checkbox" name="keyIds" value="$request.getParameter('tableName');$reportBean.reportName;${reportField.fieldName}-${fieldName}-${reportField.popUpName}-list;${reportField.display}-$dis;list">&nbsp;</td>
					<td align="left"> $reportBean.reportName&nbsp;</td>
					<td align="left"> #if($velocityCount==1)$reportField.display #end&nbsp;</td>
					<td align="left"> $dis&nbsp;</td>
					<td align="left"> $text.get("com.popup.report")&nbsp;</td>			
				</tr>
				#end
			#end
		#end
		#foreach($field in $globals.getAllFieldList($request.getParameter("tableName")))
			#if($globals.canImportField($field) && $field.inputType!=3)
				#if($field.inputType==2)
					#foreach($srow in $field.getSelectBean().displayField2)
						#if($srow.display.length()>0)
						 	#set($index=$srow.display.indexOf(".")+1)
							#set($tableField=$rowlist.tableName+"."+$srow.display.substring($index))
							#set($dis = $globals.getFieldDisplay($tableField))
							#set($fieldName=$tableField)
						 #else
						 	#set($dis="")
						 #end				
						 #if($dis == "") 
							#set($dis = $globals.getFieldDisplay($srow.fieldName)) 
							#set($fieldName=$srow.fieldName)
						 #end
					<tr>
						<td align="left"><input type="checkbox" name="keyIds" value="$field.tableBean.tableName;$field.tableBean.display.get("$globals.getLocale()");${field.fieldName}-${fieldName}-${field.getSelectBean().name}-bill;$field.display.get("$globals.getLocale()")-$dis;bill">&nbsp;</td>
						<td align="left"> $field.tableBean.display.get("$globals.getLocale()")&nbsp;</td>
						<td align="left"> #if($velocityCount==1)$field.display.get("$globals.getLocale()")#end&nbsp;</td>
						<td align="left"> $dis&nbsp;</td>	
						<td align="left"> $text.get("com.popup.bill")&nbsp;</td>		
					</tr>
					#end
				#end
			#end
		#end
		</tbody>
		</table>
</div>
</div>
</form>
</body>
</html>
