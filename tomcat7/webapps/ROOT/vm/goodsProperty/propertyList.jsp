<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("property.lb.title")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/DragTable.js"></script>
<script language="javascript" src="/js/SortTable.js"></script>

<script>
function find()
{	
	var name = document.getElementById("name").value;
	document.location="PropQueryAction.do?name="+name+"&winCurIndex=$!winCurIndex";
}
	
function clearName()
{
	document.getElementById("name").value="";
}
function propSet(name)
{
	  if(!isChecked(name))
	  {
	    alert('$text.get("property.alert.selectone")');
		return false;
	  }
	  var id;
	  items = document.getElementsByName(name);
	  var count=0;
	for(var i=0;i<items.length;i++){
	    if(items[i].checked)
		{
		   count=count+1;
		   id=items[i].value;
		}
	}
	if(count>1)
	{
	alert('$text.get("property.alert.onlyselectone")');
	return false;
	}
	#if($globals.getMOperation().add())
	window.location="/PropAction.do?operation=$globals.getOP('OP_ADD_PREPARE')&type=propSetPre&keyId="+id+"&winCurIndex=$!winCurIndex";
	#end
	  
	}
	function beforeDel()
	{
	var sd = sureDel('keyId');
	 if(sd)
	 {
	  document.form.target="";
	  document.form.submit();
	  }
	}
</script>
</head>

<body onLoad="showtable('tblSort');showStatus(); ">
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/PropQueryAction.do" target="formFrame">
 <input type="hidden" name="operation" value="$globals.getOP('OP_QUERY')">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("property.lb.title")</div>
	<ul class="HeadingButton">
		<li><button type="button" onClick="propSet('keyId')" class="b4">$text.get("property.lb.propSet")</button></li>
		#if($globals.getMOperation().add())
		<li><button type="button" onClick="location.href='/PropAction.do?operation=$globals.getOP('OP_ADD_PREPARE')&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.add")</button></li>
	#end
	#if($globals.getMOperation().delete())
		<li><button type="button" onClick="beforeDel()" class="b2">$text.get("common.lb.del")</button></li>
	#end
	</ul>
</div>
<div id="listRange_id">	
	<div class="listRange_1">
		<li><span>$text.get("common.lb.query")：</span>
		<input type="text" id="name" name="name" value="$!search" onKeyPress="if(event.keyCode == 13){ find();  }">	
		</li>
		<li>
<button name="Submit" type="button" onClick="find()" class="b2">$text.get("common.lb.query")</button>

<button name="clear" type="botton" onClick="clearName()" class="b2">$text.get("common.lb.clear")</button>		
</li>
	</div>
    <div class="scroll_function_small_a" id="conter">
		<table border="0" width="100%" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
		<thead>
			<tr>
				<td width="30" align="center"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
				<td align="center" width="50">$text.get("property.lb.propName")</td>
				<td align="center" width="80">$text.get("property.lb.propDisplay")</td>
				<td align="center" width="150">$text.get("property.lb.enum")</td>
				<td align="center" width="53">$text.get("excel.list.isUsed")</td>
				<td align="center" width="80">$text.get("property.lb.isCalculate")</td>
				<td align="center" width="66">$text.get("property.lb.jointable")</td>
				<td align="center" width="55">$text.get("property.lb.inputbill")</td>
				<td align="center" width="70">$text.get("property.lb.twoUnit")</td>
				<td align="center" width="66">$text.get("property.lb.sequence")</td>
				<td align="center" width="80">$text.get("property.lb.sepAppoint")</td>
				<td align="center" width="40">$text.get("property.lb.groupOrNot")</td>
				<td align="center" width="80">$text.get("property.lb.nameAndValue")</td>
				#if($globals.getMOperation().update())
				<td width="40">$text.get("common.lb.update")</td>
				#end
			</tr>			
		</thead>
		<tbody>
		#foreach ($row in $result )
			<tr>
				<td align="center"><input type="checkbox" name="keyId" value="$globals.get($row,0)"></td>
				<td align="center">$!globals.encodeHTMLLine($globals.get($row,1)) &nbsp;</td>
				<td align="center">$!globals.encodeHTMLLine($globals.get($row,2)) &nbsp;</td>
				<td align="left" width="200">$!globals.encodeHTMLLine($globals.get($row,3)) &nbsp;</td>
				<td align="center">#if($!globals.encodeHTMLLine($globals.get($row,4))==1) $text.get("excel.list.yes") #else $text.get("excel.list.no") #end</td>
				<td align="center">#if($!globals.encodeHTMLLine($globals.get($row,5))==1) $text.get("excel.list.yes") #else $text.get("excel.list.no") #end</td>
				<td align="center">#if($!globals.encodeHTMLLine($globals.get($row,6))==1) $text.get("excel.list.yes") #else $text.get("excel.list.no") #end</td>
				<td align="center">#if($!globals.encodeHTMLLine($globals.get($row,7))==1) $text.get("excel.list.yes") #else $text.get("excel.list.no") #end</td>
				<td align="center">#if($!globals.encodeHTMLLine($globals.get($row,8))==1) $text.get("excel.list.yes") #else $text.get("excel.list.no") #end</td>
				<td align="center">#if($!globals.encodeHTMLLine($globals.get($row,9))==1) $text.get("excel.list.yes") #else $text.get("excel.list.no") #end</td>
				<td align="center">#if($!globals.encodeHTMLLine($globals.get($row,10))==1) $text.get("excel.list.yes") #else $text.get("excel.list.no") #end</td>
				<td align="center">#if($!globals.encodeHTMLLine($globals.get($row,11))==1) $text.get("excel.list.yes") #else $text.get("excel.list.no") #end</td>
				<td align="center">#if($!globals.encodeHTMLLine($globals.get($row,12))==1) $text.get("excel.list.yes") #else $text.get("excel.list.no") #end</td>
				#if($globals.getMOperation().update())
				<td align="center"><a href="/PropAction.do?operation=$globals.getOP('OP_UPDATE_PREPARE')&keyId=$globals.get($row,0)&winCurIndex=$!winCurIndex">$text.get("common.lb.update")</a></td>
				#end
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
