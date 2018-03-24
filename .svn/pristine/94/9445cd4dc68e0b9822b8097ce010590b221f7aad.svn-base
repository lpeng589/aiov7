<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("enumeration.title.list")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css"/>
<style type="text/css">
.listrange_list tbody td{overflow:hidden;text-overflow:ellipsis;}
</style>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript">

function sureDel(itemName){
	if(delHasChild(itemName)){
		alert('$text.get("common.exist.childcategory")');
		return false;
	}
	if(!isChecked(itemName)){
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	form.operation.value=$globals.getOP("OP_DELETE");
	if(!confirm('$text.get("common.msg.confirmDel")')){
		form.operation.value = $globals.getOP("OP_QUERY");
		cancelSelected("input");
		return false;
	}else{
		return true;
	}
}
</script>
</head>

<body onLoad="showStatus();">
<form  method="post" scope="request" name="form" action="/EnumerationQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingTitle">$text.get("enumeration.title.list")</div>
	<ul class="HeadingButton">
	#if($LoginBean.operationMap.get("/EnumerationQueryAction.do").query() || $!LoginBean.id=="1")
	<li><button name="Btnquery" type="submit" onClick="javascript:document.getElementById('pageNo').value='1';" class="hBtns" >$text.get("common.lb.query")</button></li>
	#end
	#if($LoginBean.operationMap.get("/EnumerationQueryAction.do").add() || $!LoginBean.id=="1")
		<li><button type="button" onClick="location.href='/EnumerationAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&winCurIndex=$!winCurIndex'" class="hBtns">$text.get("common.lb.add")</button></li>
	#end
	#if($LoginBean.operationMap.get("/EnumerationQueryAction.do").delete() || $!LoginBean.id=="1")
		<li><button type="button" onClick="javascript:var sd = sureDel('keyId'); if(sd) {document.form.submit();}" class="hBtns">$text.get("common.lb.del")</button></li>
	#end
	</ul>
</div>
<div id="listRange_id">
	<div class="listRange_1 listRange_1_input2">
	#if($LoginBean.operationMap.get("/EnumerationQueryAction.do").query() || $!LoginBean.id=="1")
		<li><span>$text.get("enumeration.lb.search")：</span>
		<input type="text" id="name" name="name" value="$!search">	
		</li>
		<li><span>$text.get("common.lb.module")：</span>
		<select  name="mainModule"  >
			<option value="-1" #if($erow.value == $!mainModule) selected #end>$text.get("property.choose.item1")</option>
			#foreach($erow in $globals.getEnumerationItems("MainModule"))
				<option   value="$erow.value" #if($erow.value == $!mainModule) selected #end>$erow.name</option>
			#end
		</select>	
		</li>
		<li>	
</li>
#end

	</div>
    <div class="scroll_function_small_a" id="conter">
		<table border="0" style="width:100%;table-layout:fixed;" cellpadding="0" cellspacing="0" class="listRange_list" name="table" id="tblSort">
		<thead>
			<tr>
			  <td width="30" align="center"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
				<td width="150" align="center">$text.get("enumeration.lb.enumName")</td>
				<td width="150" align="center">$text.get("enumeration.lb.name")</td>
				<td width="300" align="center">$text.get("enumeration.lb.value")</td>
				#if($LoginBean.operationMap.get("/EnumerationQueryAction.do").update() || $!LoginBean.id=="1")
				<td width="50" align="center">$text.get("common.lb.update")</td>
				#end
			</tr>			
		</thead>
		<tbody>
		#foreach ($row in $result )
			<tr>
				<td align="center"><input type="checkbox" name="keyId" value="$globals.get($row,0)"></td>
				<td align="left">$!globals.encodeHTMLLine($globals.get($row,1)) &nbsp;</td>
				<td align="left">$!globals.encodeHTMLLine($globals.get($row,2)) &nbsp;</td>
				<td align="left" title='$!globals.encodeHTMLLine($globals.get($row,3))'>$!globals.encodeHTMLLine($globals.get($row,3)) &nbsp;</td>
				#if($LoginBean.operationMap.get("/EnumerationQueryAction.do").update() || $!LoginBean.id=="1")
				<td align="center"><a href='/EnumerationAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId=$globals.get($row,0)&winCurIndex=$!winCurIndex'>$text.get("common.lb.update")</a></td>
				#end
			</tr>
		#end
		</tbody>
	  </table>
	  </div>
		<div class="listRange_pagebar">$!pageBar </div>
<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-105;
oDiv.style.height=sHeight+"px";
//]]>
</script>
</div>
</form>
</body>
</html>
