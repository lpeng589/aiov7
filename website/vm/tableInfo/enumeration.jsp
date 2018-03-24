<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("enumeration.title.list")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script language="javascript">
function disclose(str){
	
    window.parent.returnValue = str;
    window.close();
}

</script>
</head>

<body onLoad="showtable('tblSort');showStatus(); ">

<form  method="post" scope="request" name="form" action="/EnumerationQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
  <input type="hidden" name="winCurIndex" value="$!winCurIndex">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("enumeration.title.list")</div>
	<ul class="HeadingButton">
		
	</ul>
</div>
<div id="listRange_id">
    <div class="scroll_function_small_a" id="conter">
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list" name="table" id="tblSort">
		<thead>
			<tr>
				<td width="150px">$text.get("enumeration.lb.enumName")</td>
				<td width="150px">$text.get("enumeration.lb.name")</td>
			</tr>			
		</thead>
		<tbody>
		#foreach ($row in $globals.getEnumeration() )
			<tr onDblClick="disclose('$row.value')">
				<td align="left">$row.value &nbsp;</td>
				<td align="left">$row.name &nbsp;</td>			
			</tr>
		#end
		</tbody>
		</table>
	</div>
<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-52;
oDiv.style.height=sHeight+"px";
//]]>
</script>
		<div class="listRange_pagebar"> $!pageBar </div>
</div>
</form>
</body>
</html>
