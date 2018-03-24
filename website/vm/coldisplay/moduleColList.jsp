<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("set.popup.field")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

</head>

<body onLoad="showStatus();">

<form  method="post" scope="request" name="form" action="/ModuleColAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("set.popup.field")</div>
	<ul class="HeadingButton">
	#if($globals.getMOperation().add())
		<li><button type="button" onClick="location.href='/ModuleColAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.add")</button></li>
		#end
	#if($globals.getMOperation().delete())
		<li><button type="button" onClick="javascript:var sd = sureDel('keyId'); if(sd) document.form.submit();" class="b2">$text.get("common.lb.del")</button></li>
		#end
	
		#if($globals.getMOperation().query())
		<li><button name="Submit" type="submit" onClick="forms[0].operation.value=$globals.getOP("OP_QUERY");return beforeBtnQuery();" class="b2">$text.get("common.lb.query")</button></li>
		#end
	</ul>
</div>
<div id="listRange_id">
		<div class="listRange_1">
			<li><span>$text.get("wokeflow.module.name")：</span><input name="moduleName" type="text" value="$!moduleName" maxlength="50"></li>	
		</div>
		<div class="scroll_function_small_a" id="conter">
		<script type="text/javascript">
			var oDiv=document.getElementById("conter");
			var sHeight=document.body.clientHeight-92;
			oDiv.style.height=sHeight+"px";
		</script>
			<table border="0" width="210" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
			<thead>
				<tr>
					<td width="40"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
					<td width="120">$text.get("wokeflow.module.name")</td>
					#if($globals.getMOperation().update())
					<td width="50">$text.get("mrp.lb.update")</td>
					#end
				</tr>
			</thead>
			<tbody>
				#foreach ($moduleName in $moduleList )
				<tr>
					<td align ="center"><input type="checkbox" name="keyId" value="$globals.get($moduleName,0)"></td>
					<td align="left">$globals.get($moduleName,1)&nbsp;</td>	
					#if($globals.getMOperation().update())
					<td align="center"><a href="/ModuleColAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&moduleId=$globals.get($moduleName,0)&winCurIndex=$!winCurIndex">$text.get("mrp.lb.update")</a></td>	
					#end
				</tr>
				#end
			</tbody>
			</table>
		</div>
			<div class="listRange_pagebar"> $!pageBar </div>
	</div>
</div>
</form>
</body>
</html>
