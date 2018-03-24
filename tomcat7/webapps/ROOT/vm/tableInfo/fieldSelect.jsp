<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("tableInfo.title")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script language="javascript">
function disclose(tableName){
	var pt = '$!request.getParameter("parentTableName")';
	if(pt != ""){
	window.parent.returnValue = pt+":"+tableName;
	}else{
    window.parent.returnValue = tableName;
	}
    window.close();
}
</script>
</head>

<body onLoad="showtable('tblSort');showStatus(); ">

<form  method="post" scope="request" name="form" action="/CustomQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
  <input type="hidden" name="winCurIndex" value="$!winCurIndex">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("tableInfo.title")</div>
	<ul class="HeadingButton">
		
	</ul>
</div>
<div id="listRange_id">
    <div class="scroll_function_small_a" id="conter">
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list" name="table" id="tblSort">
		<thead>
			<tr >
				<td align="left" colspan="2">$text.get("customTable.lb.saveField")</td>	
			</tr>			
			<tr>
				<td width="150px">$text.get("customTable.lb.fieldName")</td>
				<td width="150px">$text.get("customTable.lb.moreLan")</td>
			</tr>			
		</thead>
		<tbody>
		
		<tr onDblClick="disclose('$request.getParameter("tableName"):id')">
				<td align="left">id &nbsp;</td>
				<td align="left">id &nbsp;</td>			
		</tr>
		#foreach ($row in $TABLE_INFO.get($request.getParameter("tableName")).fieldInfos )
			
		#if($row.fieldName != "id" && $row.fieldName != "f_ref")
			<tr onDblClick="disclose('$request.getParameter("tableName"):$row.fieldName')">
				<td align="left">$row.fieldName &nbsp;</td>
				<td align="left">$row.display.get($globals.getLocale()) &nbsp;</td>			
			</tr>
		#end
		#end

		</tbody>	
		<thead>
			<tr >
				<td align="left" colspan="3">$text.get("customTable.lb.displayField")</td>	
			</tr>			
			<tr>
				<td width="150px"><input type="checkbox" name="" value=""></td>
				<td width="150px">$text.get("customTable.lb.fieldName")</td>
				<td width="150px">$text.get("customTable.lb.moreLan")</td>
			</tr>			
		</thead>
		<tbody>				
		#foreach ($row in $TABLE_INFO.get($request.getParameter("tableName")).fieldInfos )
			
		#if($row.fieldName != "id" && $row.fieldName != "f_ref")
			<tr onDblClick="disclose('$request.getParameter("tableName"):$row.fieldName')">
				<td align="left"><input type="checkbox" name="" value=""></td>	
				<td align="left">$row.fieldName &nbsp;</td>
				<td align="left">$row.display.get($globals.getLocale()) &nbsp;</td>							
			</tr>
		#end
		#end		
		
		</tbody>
		</table>
	</div>	
<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-52;
oDiv.style.height=sHeight+"px";
//]]>
</script>
		<div class="listRange_pagebar"> $!pageBar </div>
</div>
</form>
</body>
</html>
