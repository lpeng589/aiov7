<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("crm.client.list")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="JavaScript" src="/js/FusionCharts.js"></script>
<script type="text/javascript">
function showPie3D(){
	if("$!dataXML"==""){
		document.getElementById("SQLSave2").value = window.dialogArguments.document.getElementById("SQLSave").value ;
		form.submit() ;
	}else{
		var chart = new FusionCharts("flash/FCF_Pie3D.swf", "ChartId", "500", "250");	
	 	chart.setDataXML("$!dataXML");
		chart.render("pie3D");
	}
}

function changeType(obj){
	
	var varType = document.getElementsByName("pieType") ;
	for(var i=0;i<varType.length;i++){
		if(varType[i].value == obj.value){
			varType[i].checked = true ;
		}else{
			varType[i].checked = false ;
		}
	}
	form.strType.value = obj.value ;
	form.submit() ;
}
</script>
<style>
.showClass{
	width: 18%;
	height:300px;
	border-right:1px solid #AED3F1;
	border-top:1px solid #AED3F1;
	margin-top:5px; 
	padding-top:10px;
	float: left;
}
</style>
</head>

<body onload="showPie3D();">
<form method="post" name="form" action="/ClientAction.do">
<input type="hidden" name="strType" value=""/>
<input type="hidden" name="type" value="pie3D"/>
<input type="hidden" name="SQLSave2" value="$!SQLSave2">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="oaHeadingTitle">$text.get("crm.client.pie3d")</div>
	<ul class="HeadingButton">
		<li><button type="button" onClick="javascript:window.close()" class="b2">$text.get("common.lb.close")</button></li>
	</ul>
</div>
<div>
	<div class="showClass">
		<ul>
			<li><input id="statusId" type="checkbox" name="pieType" #if("$!strType"=="Status")checked #end value="Status" onclick="changeType(this)"/><label for="statusId">$text.get("crm.pie3d.life.circle")</label></li>
			<li><input id="districtId" type="checkbox" name="pieType" #if("$!strType"=="CRMBusinessDistrict.BusinessDistrict")checked #end value="CRMBusinessDistrict.BusinessDistrict"  onclick="changeType(this)"/><label for="districtId">$text.get("crm.pie3d.bus.district")</label></li>
			<li><input id="sourceInfoId" type="checkbox" name="pieType" #if("$!strType"=="CRMClientInfo.SourceInfo")checked #end value="CRMClientInfo.SourceInfo"  onclick="changeType(this)"/><label for="sourceInfoId">$text.get("crm.pie3d.source.info")</label></li>
			<li><input id="clientTypeId" type="checkbox" name="pieType" #if("$!strType"=="ClientType")checked #end value="ClientType" onclick="changeType(this)"/><label for="clientTypeId">$text.get("crm.pie3d.custoer.type")</label></li>
			<li><input id="clientLabelId" type="checkbox" name="pieType" #if("$!strType"=="ClientLabel")checked #end value="ClientLabel" onclick="changeType(this)"/><label for="clientLabelId">$text.get("crm.pie3d.client.label")</label></li>
			<li><input id="tradeId" type="checkbox" name="pieType" #if("$!strType"=="Trade")checked #end value="Trade" value="Trade" onclick="changeType(this)"/><label for="tradeId">$text.get("crm.pie3d.Industry")</label></li>
			<!-- <li><input type="checkbox" name="pieType" #if("$!strType"=="DeptCode")checked #end value="DeptCode" onclick="changeType(this)"/>部门</li> -->
			<li><input id="createById" type="checkbox" name="pieType" #if("$!strType"=="CRMClientInfo.CreateBy")checked #end value="CRMClientInfo.CreateBy"  onclick="changeType(this)"/><label for="createById">$text.get("crm.pie3d.createBy")</label></li>
			<li><input id="scaleId" type="checkbox" name="pieType" #if("$!strType"=="Scale")checked #end value="Scale" onclick="changeType(this)"/><label for="scaleId">$text.get("crm.pie3d.scale")</label></li>
			<li><input id="levelId" type="checkbox" name="pieType" #if("$!strType"=="Level")checked #end value="Level" onclick="changeType(this)"/><label for="levelId">$text.get("crm.pie3d.level")</label></li>
		</ul>
	</div>
	<div id="pie3D">
		
	</div>
</div>
	
</body>

</html>
