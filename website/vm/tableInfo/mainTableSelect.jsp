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

#if($request.getParameter("type")==2)
document.form.action = "/vm/tableInfo/fieldSelect.jsp?tableName="+tableName;
#else
document.form.action = "/vm/tableInfo/childTableSelect.jsp?tableName="+tableName;
#end
document.form.submit();
}
</script>

</head>

<body onLoad="showtable('tblSort');showStatus(); ">

<form  method="post" scope="request" name="form" action="/CustomQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
  <input type="hidden" name="winCurIndex" value="$!winCurIndex">
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
			<tr>
				<td width="150px">$text.get("tableInfo.lb.tableName")</td>
				<td width="150px">$text.get("tableInfo.lb.tabeDisplayName")</td>
			</tr>			
		</thead>
		<tbody>
		#foreach ($row in $TABLE_INFO.values() )
		#if($row.tableType == 0)
			<tr onDblClick="disclose('$row.tableName')">
				<td align="left">$row.tableName &nbsp;</td>
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
