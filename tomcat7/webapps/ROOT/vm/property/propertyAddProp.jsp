<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("property.lb.set")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script>
	function find()
	{	
	var name = document.getElementById("name").value;
	document.location="PropertyQueryAction.do?operation=$globals.getOP('OP_QUERYTABLE')&name="+name;
	}
	function clearName()
	{
	document.getElementById("name").value="";
	}
	
	function sureProperty(itemName){
	if(delHasChild(itemName)){
		alert('$text.get("common.exist.childcategory")');
		return false;
	}
	if(!isChecked(itemName)){
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	
	form.action="/PropertyAction.do?operation=$globals.getOP('OP_PROPERTY')";
	document.form.submit();
	}
	function sureDelProperty(itemName){
	if(delHasChild(itemName)){
		alert('$text.get("common.exist.childcategory")');
		return false;
	}
	if(!isChecked(itemName)){
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	
	form.action="/PropertyAction.do?operation=$globals.getOP('OP_DELPROPERTY')";
	document.form.submit();
	}
</script>
</head>

<body onLoad="showtable('tblSort');showStatus(); ">
<form  method="post" scope="request" name="form" action="/PropertyQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERYTABLE")">
 <input type="hidden" name="propName" value="$propName">
  <input type="hidden" name="winCurIndex" value="$!winCurIndex">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("property.lb.set")</div>
	<ul class="HeadingButton">
		<li><button type="button" onClick="sureProperty('keyId')" class="b2">$text.get("property.lb.save")</button></li>
		<li><button type="button" onClick="sureDelProperty('keyId')" class="b4">$text.get("property.lb.delprop")</button></li>
		<li><button type="button" onClick="location.href='/PropertyQueryAction.do?operation=$globals.getOP("OP_QUERY")'" class="b2">$text.get("common.lb.back")</button></li>
	
	</ul>
</div>
<div id="listRange_id">	
	<div class="listRange_1">
		<li><span>$text.get("common.lb.query")：</span>
		<input type="text" id="name" name="name" value="$!search">	
		</li>
<li>
<button name="Submit" type="button" onClick="find()" class="b2">$text.get("common.lb.query")</button>
<button name="clear" type="botton" onClick="clearName()" class="b2">$text.get("common.lb.clear")</button>		
</li>
<li>
<button name="Submit" type="submit" onClick="forms[0].operation.value=$globals.getOP("OP_QUERYPROPTABLE");">$text.get("property.lb.addedprop")</button>
<button name="Submit" type="submit" onClick="forms[0].operation.value=$globals.getOP("OP_QUERYNOPROPTABLE");">$text.get("property.lb.noaddedprop")</button></li>

	</div>
    <div class="scroll_function_small_a" id="conter">
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
		<thead>
			<tr>
				<td width="30px"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
				<td width="130px">$text.get("property.set.tablename")</td>
				<td width="130px">$text.get("property.set.tabledisplay")</td>
			</tr>			
		</thead>
		<tbody>
		#foreach ($row in $result )		     
			<tr>
				<td align="center"><input type="checkbox" name="keyId"  value="$globals.get($row,2)"
				#foreach($pp in $tableProp)
				#if($globals.get($row,2)==$pp)
				checked="check" 
				#end
				#end
				></td>
				<td align="left">$!globals.encodeHTMLLine($globals.get($row,0)) &nbsp;</td>
				<td align="left">$!globals.encodeHTMLLine($globals.get($row,1)) &nbsp;</td>
			</tr>
		#end
		</tbody>
		</table>
		</div>
<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-92;
oDiv.style.height=sHeight+"px";
//]]>
</script>
		<div class="listRange_pagebar">$!pageBar </div>
</div>
</form>
</body>
</html>
