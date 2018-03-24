<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script src="/js/jquery.js" type="text/javascript"></script>

<script language="javascript" src="$globals.js("/js/date.vjs","",$text)"></script>
<script type="Text/Javascript" src="/js/MSIE.PNG.js"></script>
<style type="text/css">
.inputDiv_bt{
	height:28px;
	line-height:28px;
	vertical-align:middle;
	padding:5px;
	text-align:center;
	background-color:#acd3fe;
}
.inputDiv_bt button{
	margin:0px;
	border:0;
	border:1px solid #1CA4FC;
	text-align:center;
	margin-right:5px;
	height:22px;
	line-height:16px;
	vertical-align:middle;
	font-size:12px;
	cursor:pointer;
	color:#02428A;
	width:0;
	width:auto;
	padding:2px 3px 0 3px;
	overflow:visible;
	background:url(../images/aiobg.gif) repeat-x left top;
}
.inputDiv{
	cursor:default; 
	position:absolute; 
	width:150px;
	height:50px; 
	border:1px solid #b6cadf;
} 
.inputDiv{
	height:28px;
	line-height:28px;
	vertical-align:middle;
	border-top:1px solid #fff;
	border-left:1px solid #fff;
	border-right:1px solid #fff;
	border-bottom:1px solid #57a7ff;
	background-color:#acd3fe;
}
</style>
<script language="javascript">  



function addClass(){
		curUpdateId = "";
		$('#inputDiv').show();
		$('#inputDiv').css("left",350);
		$('#inputDiv').css("top",80);		
}

function doInputCancel(){
	$('#inputDiv').hide();
}
function doInputOk(){
	if($("#inputObj").val() == ""){
		alert("输入值不能为空");
		return;
	}
	$("#inputDiv").hide();
	if(curUpdateId == ""){
	jQuery.get("/moduleFlow.do?operation=addClass&defaultSet=$defaultSet&className="+encodeURIComponent($("#inputObj").val()),function(data){
		alert(data);
		window.location.reload();
	});
	}else{
	jQuery.get("/moduleFlow.do?operation=updateClass&defaultSet=$defaultSet&id="+curUpdateId+"&className="+encodeURIComponent($("#inputObj").val()),function(data){
		alert(data);
		window.location.reload();
	});
	}
}
var curUpdateId="";
function updateClass(id,name){
		curUpdateId = id;
		$('#inputDiv').show();
		$('#inputDiv').css("left",350);
		$('#inputDiv').css("top",80);	
		$("#inputObj").val(name);
}



</script>
</head>

<body onLoad="showtable('tblSort');showStatus(); ">
<div id=inputDiv class="inputDiv" style="display:none" >
	<div class="inputDiv_title">
		<span id="inputDiv_title" style="margin-left:10px;width:120px;">$text.get("please.input.category") </span>
	</div>
	<div style="text-align:center">
		<input type="text" style="width:95%" name="inputObj" id="inputObj"> 
	</div>
	<div  class="inputDiv_bt">
		<button type="button" onclick="doInputOk()"  style="width:50px;margin:2px 4px 2px 4px;">$text.get("catagory.save")</button>
		<button type="button" onclick="doInputCancel()"  style="width:50px;margin:2px 4px 2px 4px;">$text.get("catagory.cancel")</button>
	</div>
</div>
<form style="margin:0px;"  method="post" scope="request" name="form" action="/moduleFlow.do?operation=destSet">
 <input type="hidden" name="operation" value="$globals.getOP('OP_QUERY')">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
  <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("navigate.category")</div>
	<ul class="HeadingButton">
		<li><button type="button"" class="b2" onClick="addClass();">$text.get("common.lb.add")</button></li>
		#if("$defaultSet" == "true") 
		<li><button type="button" onClick="window.location.href='/moduleFlow.do?operation=destDefaultSet';" class="b2">$text.get("common.lb.back")</button></li>
		#else 
		<li><button type="button" onClick="window.location.href='/moduleFlow.do?operation=destSet';" class="b2">$text.get("common.lb.back")</button></li>
		#end
		
	</ul>
</div>
<div id="listRange_id">
		<div class="scroll_function_small_a" id="conter">
			<script type="text/javascript">
				var oDiv=document.getElementById("conter");
				var sHeight=document.body.clientHeight-115;
				oDiv.style.height=sHeight+"px";
				</script>
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
			<thead>
				<tr>
				    
				    <td width="200" align="center" >$text.get("navigate.category")</td>
					<td width="90" align="center">$text.get("common.lb.operation")</td>				
				</tr>			
			</thead>
			<tbody>
				#foreach ($row in $result )
				<tr height=60>
					<td align="left" >$globals.get($row,1)</td>
					<td align="CENTER">
					<a href="javascript:updateClass('$globals.get($row,0)','$globals.get($row,1)');"> $text.get("common.lb.update") </a>
					<a href="/moduleFlow.do?operation=delClass&defaultSet=$defaultSet&id=$globals.get($row,0)"> $text.get("common.lb.del") </a></td>					
				</tr>
				#end		
			</tbody>
		  </table>

		</div>	
	<div class="listRange_pagebar"></div>
	</div>
	</form>
	
</body>
</html>
