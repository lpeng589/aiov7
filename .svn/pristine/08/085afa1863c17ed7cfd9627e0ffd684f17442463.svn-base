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
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">

function selectKeyId(obj){
	$(obj).find("input").attr("checked","checked");
}
function returnVal(obj){
	parent.seachAsyn($(obj).find("input").val());
}

</script>
</head>

<body onLoad="showStatus();">
<form  method="post" scope="request" name="form" action="/EnumerationQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
 <input type="hidden" name="type" value="crm">

<div id="listRange_id">
	<div class="listRange_1 listRange_1_input2">
	#if($LoginBean.operationMap.get("/EnumerationQueryAction.do").query() || $!LoginBean.id=="1")
		<li><span>$text.get("enumeration.lb.search")：</span>
		<input type="text" id="name" name="name" value="$!search">	
		</li>
		
	#if($LoginBean.operationMap.get("/EnumerationQueryAction.do").query() || $!LoginBean.id=="1")
	<li><button name="Btnquery" type="submit" onClick="javascript:document.getElementById('pageNo').value='1';" class="hBtns" >$text.get("common.lb.query")</button></li>
	#end
#end

	</div>
    <div class="scroll_function_small_a" id="conter">
		<table border="0" style="width:100%;table-layout:fixed;" cellpadding="0" cellspacing="0" class="listRange_list" name="table" id="tblSort">
		<thead>
			<tr>
			  <td width="30" align="center"></td>
				<td width="150" align="center">$text.get("enumeration.lb.enumName")</td>
				<td width="150" align="center">$text.get("enumeration.lb.name")</td>
				<td width="300" align="center">$text.get("enumeration.lb.value")</td>				
			</tr>			
		</thead>
		<tbody>
		#foreach ($row in $result )
			<tr onclick="selectKeyId(this)" ondblclick="returnVal(this)">
				<td align="center"><input type="radio" name="keyId" value="$globals.get($row,1)"></td>
				<td align="left">$!globals.encodeHTMLLine($globals.get($row,1)) &nbsp;</td>
				<td align="left">$!globals.encodeHTMLLine($globals.get($row,2)) &nbsp;</td>
				<td align="left" title='$!globals.encodeHTMLLine($globals.get($row,3))'>$!globals.encodeHTMLLine($globals.get($row,3)) &nbsp;</td>				
			</tr>
		#end
		</tbody>
	  </table>
	  </div>
		<div class="listRange_pagebar">$!pageBar </div>
<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-65;
oDiv.style.height=sHeight+"px";
//]]>
</script>
</div>
</form>
</body>
</html>
