<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("unit.lb.unitAdmin")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

</head>
<body onLoad="showtable('tblSort');showStatus(); ">
<form  method="post" scope="request" name="form" action="/UnitQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("unit.lb.unitAdmin")</div>
	<ul class="HeadingButton">
		#if($globals.getMOperation().add())
		<li><button type="button" onClick="location.href='/UnitAction.do?operation=$globals.getOP("OP_ADD_PREPARE")'" class="b2">$text.get("common.lb.add")</button></li>
		#end
		#if($globals.getMOperation().delete())
		<li><button type="submit" onClick="javascript:return sureDel('keyId');" class="b2">$text.get("common.lb.del")</button></li>
		#end
	</ul>
</div>
<div id="listRange_id">
	<div class="listRange_1">
		<li><span>$text.get("unit.lb.name")：</span><input name="name" type="text" value="$!unitSearchForm.getName()" maxlength="50"></li>
		<li><button name="Submit" type="submit" onClick="forms[0].operation.value=$globals.getOP("OP_QUERY");return beforeBtnQuery();" class="b2">$text.get("common.lb.query")</button><button name="clear" type="botton" onClick="clearForm(form);" class="b2">$text.get("common.lb.clear")</button></li>
	</div>
    <div class="scroll_function_small_a" id="conter">
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list" name="table" id="tblSort">
		<thead>
			<tr>
				<td width="40"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
				<td width="150">$text.get("unit.lb.name")</td>
				<td >$text.get("unit.lb.remark")</td>
				#if($globals.getMOperation().update())
				<td width="50">$text.get("common.lb.update")</td>
				#end
			</tr>
		</thead>
		<tbody>
			#foreach ($row in $result )
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				<td align ="center"><input type="checkbox" name="keyId" value="$globals.get($row,0)"></td>
				<td align="left">$!globals.encodeHTMLLine($globals.get($row,1)) &nbsp;</td>
				<td align="left">$!globals.encodeHTMLLine($globals.get($row,2)) &nbsp;</td>
				#if($globals.getMOperation().update())
				<td align ="center"><a href='/UnitAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId=$globals.get($row,0)'>$text.get("common.lb.update")</a></td>
				#end
			</tr>
			#end
		</tbody>
		</table>
	</div>
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-92;
oDiv.style.height=sHeight+"px";
</script>
		<div class="listRange_pagebar"> $!pageBar </div>
</div>
</form>
</body>
</html>
