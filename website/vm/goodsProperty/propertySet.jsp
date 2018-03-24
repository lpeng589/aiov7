<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("property.title.propset")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script>
function find()
	{	
	document.getElementById("operation").value="$globals.getOP('OP_ADD_PREPARE')";
	document.getElementById("type").value="propSetQuery";
	//window.location="/PropAction.do?operation=$globals.getOP('OP_ADD_PREPARE')&type=propSetQuery&keyId="+id+"&search="+name+"&condition="+condition;
	document.form.target="";
	document.form.submit();
	}
	function clearName()
	{
	document.getElementById("name").value="";
	}
	function beforeAdd(name)
	{
	if(!isChecked(name))
	  {
	    alert('$text.get("common.msg.mustSelectOne")');
		return false;
	  }
	  document.form.target="";
	  document.form.submit();
	}
	function beforeDel(name)
	{
	if(!isChecked(name))
	  {
	    alert('$text.get("common.msg.mustSelectOne")');
		return false;
	  }
	  document.getElementById("operation").value="$globals.getOP('OP_DELETE')";
	  document.getElementById("type").value="propSetDel";
      document.form.target="";
	  document.form.submit();
	}
	function beforeStart(name)
	{
	if(!isChecked(name))
	  {
	    alert('$text.get("common.msg.mustSelectOne")');
		return false;
	  }
	  document.getElementById("operation").value="$globals.getOP('OP_UPDATE')";
	  document.getElementById("type").value="propSetStart";
      document.form.target="";
	  document.form.submit();
	}
	function beforeStop(name)
    {
	if(!isChecked(name))
	  {
	    alert('$text.get("common.msg.mustSelectOne")');
		return false;
	  }
	  document.getElementById("operation").value="$globals.getOP('OP_UPDATE')";
	  document.getElementById("type").value="propSetStop";
      document.form.target="";
	  document.form.submit();
	}
</script>
</head>

<body onLoad="showtable('tblSort');showStatus(); ">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/PropAction.do" target="formFrame">
 <input type="hidden" id="operation" name="operation" value="$globals.getOP('OP_ADD')">
 <input type="hidden" id="type" name="type" value="propSetAdd">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
 <input type="hidden" id="propId" name="propId" value="$!keyId">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("property.title.propset")</div>
	<ul class="HeadingButton">
	#if($globals.getMOperation().add())
		<li><button type="button" onClick="beforeAdd('keyId');" class="b2">$text.get("common.lb.add")</button></li>
	#end
	#if($globals.getMOperation().delete())
		<li><button type="button" onClick="beforeDel('keyId')" class="b2">$text.get("common.lb.del")</button></li>
	#end	
		<li><button type="button" onClick="beforeStart('keyId');" class="b4">$text.get("property.button.start")</button></li>
	<li><button type="button" onClick="beforeStop('keyId');" class="b4">$text.get("property.button.stop")</button></li>
	#if($globals.getMOperation().query())
	<li><button type="button" onClick="location.href='/PropQueryAction.do?operation=$globals.getOP('OP_QUERY')&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button></li>
	#end
	</ul>
</div>
<div id="listRange_id">
	<div class="listRange_1">
	    <li><span>$text.get("property.lb.propDisplay")：</span>
			  <input  type="text"  name="propDisplay" id="propDisplay" value="$!display"  readonly="readonly">
       </li>
	   <li><span>$text.get("property.query.choose")：</span>
			<select id="condition" name="condition" id="select"> 
			    <option value="all" #if($condition=="all") selected #end>$text.get("property.choose.item1")</option>
				<option value="have" #if($condition=="have") selected #end>$text.get("property.choose.item2")</option>
				<option value="no" #if($condition=="no") selected #end>$text.get("property.choose.item3")</option>
		      </select>
			</li>
		<li><span>$text.get("property.propSet.prop")：</span>
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
			  <td width="30" align="center"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
				<td width="200" align="center">$text.get("property.set.tablename")</td>
				<td width="200" align="center">$text.get("property.set.tabledisplay")</td>
				<td width="200" align="center">$text.get("property.set.ishave")</td>
				<td width="200" align="center">$text.get("property.lb.propUsed")</td>
			</tr>			
		</thead>
		<tbody>
		#foreach ($row in $result )
		   
			<tr>
				<td align="center"><input type="checkbox" name="keyId" value="$globals.get($row,0)@$globals.get($row,1)"></td>
				<td align="left">$!globals.encodeHTMLLine($globals.get($row,1)) &nbsp;</td>
				<td align="left">$!globals.encodeHTMLLine($globals.get($row,2)) &nbsp;</td>
				<td align="center">#if($!globals.encodeHTMLLine($globals.get($row,3))==1) $text.get("excel.list.yes") #else $text.get("excel.list.no") #end</td>
				<td align="center">#if($!globals.encodeHTMLLine($globals.get($row,4))==1) $text.get("excel.list.yes") #else $text.get("excel.list.no") #end</td>
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
</div>
</form>
</body>
</html>
