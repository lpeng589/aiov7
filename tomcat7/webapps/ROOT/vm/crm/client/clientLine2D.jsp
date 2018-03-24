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
		var chart = new FusionCharts("flash/FCF_Line.swf", "ChartId", "700", "300");	
	 	chart.setDataXML("$!dataXML");
		chart.render("line2D");
	}
}

function changeType(obj){
	
	var varType = document.getElementsByName("lineType") ;
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
<input type="hidden" name="type" value="line2D"/>
<input type="hidden" name="SQLSave2" value="$!SQLSave2">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="oaHeadingTitle">$text.get("crm.client.line2d")</div>
	<ul class="HeadingButton">
		<li><button type="button" onClick="javascript:window.close()" class="b2">$text.get("common.lb.close")</button></li>
	</ul>
</div>
<div>
	<div class="showClass">
		<ul>
			<li><input id="dayId" type="checkbox" name="lineType" #if("$!strType"=="day")checked #end value="day" onclick="changeType(this)"/><label for="dayId">$text.get("crm.line2d.day")</label></li>
			<li><input id="monthId" type="checkbox" name="lineType" #if("$!strType"=="month")checked #end value="month"  onclick="changeType(this)"/><label for="monthId">$text.get("crm.line2d.month")</label></li>
			<li><input id="yearId" type="checkbox" name="lineType" #if("$!strType"=="year")checked #end  value="year" onclick="changeType(this)"/><label for="yearId">$text.get("crm.line2d.year")</label></li>
		</ul>
	</div>
	<div id="line2D">
		
	</div>
</div>
	
</body>

</html>
