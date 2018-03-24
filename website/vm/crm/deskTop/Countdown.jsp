<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("countDown.lb.update")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript">

putValidateItem("countdownTitle","$text.get('countDown.lb.title')","any","",false,0,100);
putValidateItem("countdownDate","$text.get('countDown.lb.date')","date","",false,0,10);

function beforSubmit(form){   
	if(document.form.countdownType.value=="3"){
		if(!validate(form))return false;
	}
	disableForm(form);
	return true;
}	
function changetype(){
	if(document.form.countdownType.value=="3"){
		document.getElementById("mdiv").style.display="block";
	}else{
		document.getElementById("mdiv").style.display="none";
	}
}
</script>
</head>

<body onLoad="showStatus();" scroll="no" style="overflow:hidden;">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/CrmDeskTopAction.do" enctype="multipart/form-data" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="type" value="Countd">
<input type="hidden" name="add" value="true">
<input type="hidden" name="NOTTOKEN" value="NOTTOKEN">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("countDown.lb.update")</div>
</div>
<div style="width:100%;padding-top:5px; height:auto;float:left; border-top:1px solid #CCCCCC; background:#fff url(/$globals.getStylePath()/images/aiobg.gif) repeat-x left -221px;">

		<div style="padding-left: 50px;padding-top:20px;">
			$text.get("countDown.lb.type")： <select  name="countdownType" onclick="changetype()">
						<option value="1" #if($globals.get($countDown,0)=="1") selected #end>$text.get("countDown.lb.mouth")</option>
						<option value="2" #if($globals.get($countDown,0)=="2") selected #end>$text.get("countDown.lb.year")</option>
						<option value="3" #if($globals.get($countDown,0)=="3") selected #end>$text.get("countDown.lb.fix")</option>
						</select>
		</div>
		<div id="mdiv" #if($globals.get($countDown,0)!="3") style="display:none" #end>
		<div style="padding-left: 50px;padding-top:20px;">
			$text.get("countDown.lb.date")：<input name="countdownDate" class="text" type="text" style="width:100px"  
			onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onClick="WdatePicker({lang:'zh_CN'});" value="$globals.get($countDown,2)">
		</div>
		<div style="padding-left: 50px;padding-top:20px;">
			$text.get("countDown.lb.title")：<input name="countdownTitle" class="text" type="text" style="width:300px"  value="$globals.get($countDown,1)">
		</div>
		</div>
		<div style="padding-left: 100px;padding-top: 20px;">
			<button type="submit" style="width:80px;">$text.get("common.lb.ok")</button>
			<button type="button" style="width:80px;"onClick="window.close();">$text.get("common.lb.close")</button>
		</div>
		
</div>
</form> 
</body>
</html>
