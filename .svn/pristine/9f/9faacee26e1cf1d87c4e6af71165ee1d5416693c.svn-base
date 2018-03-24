<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("property.title.list")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>


<script>
	function find()
	{	
	var name = document.getElementById("name").value;
	document.location="PropertyQueryAction.do?name="+name;
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
	
	form.action="/PropertyQueryAction.do?operation=$globals.getOP('OP_ADDPROP')";
	document.form.submit();
	}

</script>
</head>

<body onLoad="showtable('tblSort');showStatus(); ">
<form  method="post" scope="request" name="form" action="/PropertyQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
  <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("property.title.list")</div>
	<ul class="HeadingButton">
	<li><button type="button" onClick="sureProperty('keyId')" class="b4">$text.get("property.lb.addprop")</button></li>
	#if($globals.getMOperation().add())
		<li><button type="button" onClick="location.href='/PropertyAction.do?operation=$globals.getOP("OP_ADD_PREPARE")'" class="b2">$text.get("common.lb.add")</button></li>
	#end
		#if($globals.getMOperation().delete())
		<li><button type="button" onClick="javascript:var sd = sureDel('keyId'); if(sd) document.form.submit();" class="b2">$text.get("common.lb.del")</button></li>
	#end	
		
	
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
	</div>
    <div class="scroll_function_small_a" id="conter">
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
		<thead>
			<tr>
				<td width="30"></td>
				<td width="130">$text.get("property.lb.propName")</td>
				<td width="130">$text.get("property.lb.name")</td>
				<td >$text.get("property.lb.values")</td>
				#if($globals.getMOperation().update())
				<td width="50">$text.get("common.lb.update")</td>
				#end
			</tr>			
		</thead>
		<tbody>
		#foreach ($row in $result )
			#foreach($pp in $prop)
				#if($globals.get($row,0)==$pp)
			<tr>
				<td align="center"><input type="radio" id="keyId" name="keyId" value="$globals.get($row,0)"></td>
				<td align="left">$!globals.encodeHTMLLine($globals.get($row,1)) &nbsp;</td>
				<td align="left">$!globals.encodeHTMLLine($globals.get($row,2)) &nbsp;</td>
				<td align="left">$!globals.encodeHTMLLine($globals.get($row,3)) &nbsp;</td>
					#if($globals.getMOperation().update())
				<td align="center"><a href='/PropertyAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId=$globals.get($row,0)'>$text.get("common.lb.update")</a></td>
					#end
			</tr>
				#end
			#end
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
