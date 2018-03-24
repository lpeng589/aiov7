<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.import.advanced")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" >
function opFlag(flag){
	if(!isChecked('keyId')){
		alert("$text.get("common.msg.mustSelectOne")");
		return false;
	}
	document.getElementById("opType").value=flag;
	document.form.submit();
}
</script>
</head>

<body onLoad="showStatus();">
<form  method="post" scope="request" name="form" action="/importDataQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
  <input type="hidden" name="winCurIndex" value="$winCurIndex">
  <input type="hidden" name="dir" value="">
  <input type="hidden" name="opType" value="">
<div class="Heading" style="margin-bottom:10px;">
	<div class="HeadingTitle">$text.get("execl.import.export")</div>
	<div class="lf" style="margin-left:30px;">
		<input class="lf" name="name" type="text" value="$!importSearchForm.name" maxlength="50" 
		style="border:1px #bbb solid;border-radius:4px;width:120px;padding:0 5px;height:25px;" onkeydown="if(event.keyCode==13){forms[0].operation.value=$globals.getOP("OP_QUERY");forms[0].submit(); } "/>
		<button name="Submit" type="submit" onClick="forms[0].operation.value=$globals.getOP("OP_QUERY");" class="hBtns lf">$text.get("common.lb.query")</button>
	</div>
</div>
<div id="listRange_id">	
	<div class="scroll_function_small_a" id="conter">
<script type="text/javascript">
$("#conter").height(document.documentElement.clientHeight-85);
</script>
		<table border="0" width="750" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
		<thead>
			<tr>
				<td width="30"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
				<td width="150">$text.get("oa.import.model.name")</td>
				<td width="150">$text.get("import.importList.lb.table")</td>
				<!-- <td width="60">$text.get("excel.list.isUsed")</td>
				 <td width="60">$text.get("common.lb.update")</td>-->
				<td width="60">$text.get("excel.list.updata")</td>
				<td width="100">$text.get("excel.list.download")</td>
				<td width="100">$text.get("com.import.example")</td>
			</tr>			
		</thead>
		<tbody>
		#foreach ($row in $result )
			<tr>
				<td align="center"><input type="checkbox" name="keyId" value="$row.id"></td>
				<td align="left">$row.name</td>
				<td align="left">$row.targetTable</td>
				<!--<td align="center">#if($!row.flag == 1) $text.get("excel.list.yes")  #else $text.get("excel.list.no") #end</td>
				<td align="center"><a href='/importDataAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId=$row.id&winCurIndex=$!winCurIndex'>$text.get("common.lb.update")</a></td>
				-->
				<td align="center"><a href='/importDataAction.do?fromPage=importList&operation=$globals.getOP("OP_IMPORT_PREPARE")&tableName=$row.targetTable&parentTableName=$!row.parentTableName&moduleType=$!row.moduleType&winCurIndex=$!winCurIndex'>$text.get("excel.lb.upload")</a></td>
				<td align="center"><a href="javascript:mdiwin('/UserFunctionQueryAction.do?operation=25&tableName=$row.targetTable&ButtonType=billExport&parentTableName=$!row.parentTableName&moduleType=$!row.moduleType&example=true&noback=true','$text.get("excel.bill.import")')" >$text.get("excel.list.download")</a></td>
				
				<td align="center">#if($globals.existImportExample($row.targetTable))<a href="/ReadFile?tempFile=example&fileName=${row.targetTable}.xls">$text.get("com.import.example")</a>#end&nbsp;</td>
				
			</tr>
		#end
		</tbody>
		</table>
	</div>
	<div class="page-wp">
		<div class="listRange_pagebar"> $!pageBar </div>
	</div>
</div>
</form>

</body>
</html>
