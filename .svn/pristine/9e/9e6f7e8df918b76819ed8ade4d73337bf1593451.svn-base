<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("check.lb.view")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

</head>

<body onLoad="showtable('tblSort');showStatus(); ">

<form  method="post" scope="request" name="form" action="/UserFunctionQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_CHECK_LIST")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
 <input type="hidden" name="tableName" value="$tableName">
 <input type="hidden" name="parentCode" value="$!parentCode">
 <input type="hidden" name="keyId" value="$!keyId">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("check.lb.view")</div>
	<ul class="HeadingButton">
		<li><button type="button" onClick="location.href='/UserFunctionQueryAction.do?tableName=$tableName&parentCode=$globals.classCodeSubstring($!parentCode,5)&operation=$globals.getOP("OP_QUERY")'" class="b2">$text.get("common.lb.back")</button></li>
	</ul>
</div>
<div id="listRange_id">
    <div class="scroll_function_small_a" id="conter">
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
		<thead>
			<tr>
				<td align="center" width="100">$text.get("check.lb.user")</td>
				<td align="center" width="100">$text.get("check.lb.Level")</td>
				<td align="center" width="100">$text.get("check.lb.status")</td>
				<td align="center" width="200">$text.get("check.lb.Remark")</td>
			</tr>
		</thead>
		<tbody>
	
			#foreach ($row in $result.retVal)
			<tr>
				<td align="left">$globals.get($row,1) &nbsp;</td>
				<td align="left">$globals.get($row,2) &nbsp;</td>
				<td align="left">$globals.get($row,3) &nbsp;</td>
				<td align="left">$!globals.get($row,4) &nbsp;</td>
			</tr>
			#end
		</tbody>
		</table>
		<div class="listRange_pagebar"> $!pageBar </div>
	</div>	
</div>
<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-52;
oDiv.style.height=sHeight+"px";
//]]>
</script>
</form>
</body>
</html>
