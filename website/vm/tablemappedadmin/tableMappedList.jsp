<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("tableMap.lb.title")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<script type="text/javascript" src="/js/function.js"></script>
</head>

<body onLoad="showtable('tblSort');showStatus(); ">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/tableMappedQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingTitle">$text.get("tableMap.lb.title")</div>
	<ul class="HeadingButton">
		#if($LoginBean.operationMap.get("/tableMappedQueryAction.do").add() || $!LoginBean.id=="1")
		<li><button type="button" onClick="location.href='/tableMappedAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&winCurIndex=$!winCurIndex'" class="hBtns">$text.get("common.lb.add")</button></li>
		#end
		#if($LoginBean.operationMap.get("/tableMappedQueryAction.do").delete() || $!LoginBean.id=="1")
		<li><button type="button" onClick="javascript:var sd = sureDel('keyId'); if(sd) document.form.submit();" class="hBtns">$text.get("common.lb.del")</button></li>
		#end
		<li><button name="Submit" type="submit" onClick="forms[0].operation.value=$globals.getOP("OP_QUERY");return beforeBtnQuery();" class="hBtns">$text.get("common.lb.query")</button></li>
	</ul>
</div>
<div id="listRange_id">
		<div class="listRange_1">
			<li><span>$text.get("tableMap.lb.source")：</span><input name="name" type="text" value="$!TableMappedSearchForm.getName()" maxlength="50"></li>
			<li><span>目标表：</span><input name="targetName" type="text" value="$!TableMappedSearchForm.getTargetName()" maxlength="50"></li>	
		</div>
		<div class="scroll_function_small_a" id="conter">
		<script type="text/javascript">
			var oDiv=document.getElementById("conter");
			var sHeight=document.body.clientHeight-105;
			oDiv.style.height=sHeight+"px";
		</script>
			<table border="0" width="100%" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
			<thead>
				<tr>
					<td width="40"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
					<td >$text.get("tableMap.lb.source")</td>
					<td >$text.get("tableMap.lb.target")</td>
					#if($globals.getMOperation().update() || $!LoginBean.id=="1")
					<td width="50">$text.get("common.lb.update")</td>
					#end
				</tr>
			</thead>
			<tbody>
				#foreach ($row in $result )
				<tr>
					<td align ="center"><input type="checkbox" name="keyId" value="$globals.get($row,0)@$globals.get($row,1)"></td>
					<td align="left">$!globals.get($row,0) &nbsp;</td>
					<td align="left">$!globals.encodeHTMLLine($globals.get($row,1)) &nbsp;</td>
					#if($globals.getMOperation().update() || $!LoginBean.id=="1")
					<td align ="center"><a href='/tableMappedAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId=$globals.get($row,0)@$globals.get($row,1)&winCurIndex=$!winCurIndex'>$text.get("common.lb.update")</a></td>	
					#end			
				</tr>
				#end
			</tbody>
			</table>
		</div>
			<div class="page-wp">
				<div class="listRange_pagebar"> $!pageBar </div>
			</div>
	</div>
</div>
</form>
</body>
</html>
