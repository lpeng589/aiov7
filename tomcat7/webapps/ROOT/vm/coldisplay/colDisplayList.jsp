<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("com.colName.setting")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script language="javascript">
function copyOperation()
{
 	document.form.operation.value=$globals.getOP("OP_COPY_PREPARE");
	document.form.action="/CustomAction.do";
	if(!isChecked("keyId")){		
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	document.form.submit();
}
</script>
</head>

<body onLoad="showtable('tblSort');showStatus(); ">

<form  method="post" scope="request" name="form" action="/ColDisplayAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
 <input type="hidden" name="winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("com.colName.setting")</div>
	<ul class="HeadingButton">
	</ul>	
</div>
<div id="listRange_id">
	<div class="listRange_1">
		<li><span>$text.get("common.lb.query")：</span>
		<input type="text" id="abcd" name="search" value="$!search">	
		</li>
		<li>
<button name="Submit" type="submit" onClick="forms[0].operation.value=$globals.getOP("OP_QUERY");" class="b2">$text.get("common.lb.query")</button>
	</li>
	</div>
	<div class="scroll_function_small_a" id="conter">
		<table border="0" style="width:400px;" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
		<thead>
			<tr>
			  <td width="40" align="center"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
				<td width="200" align="center">$text.get("tableInfo.lb.tabeDisplayName")</td>
				<td width="60" align="center">$text.get("common.lb.update")</td>
			</tr>			
		</thead>
		<tbody>
		#foreach ($row in $result )
			<tr>
				<td align="center" class="listheadonerow"><input type="checkbox" name="keyId" value="$globals.get($row,0)"></td>
				<td align="left">$!globals.get($row,1)</td>
				<td align="center">
				<a href='/ColDisplayAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId=$globals.get($row,0)&search=$!search&winCurIndex=$!winCurIndex'>$text.get("common.lb.update")</a> &nbsp;</td>
			</tr>
		#end
		</tbody>
		</table>
	  </div>
		<div class="listRange_pagebar"> $!pageBar </div>
</div>
</form>
<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-92;
oDiv.style.height=sHeight+"px";
//]]>
</script>
</body>
</html>
