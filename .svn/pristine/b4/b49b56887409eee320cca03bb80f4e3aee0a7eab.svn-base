<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>	$text.get("accperiods.Intercalate")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript">

function beforSubmit(){
	if(form.accPeriodDate.value == ""){
		alert("$text.get("accPeriod.msg.set")");
		return false;
	}
  var urls="/UtilServlet?operation=getCurPeriod";
		 AjaxRequest(urls) ;
		if(response!="-1")
		{
		 alert("$text.get('tblSysSetting.statusid.error')");
		 return false;
		}
	
	
	return true;
}	

function openInputDate(obj)
{
	WdatePicker({lang:'$globals.getLocale()'});
}

function $(obj){
	return document.getElementById(obj);
}
</script>
</head>

<body>

<form  method="post" scope="request" name="form" action="/AccPeriodIntercalateAction.do" >
<input type="hidden" name="operation" value="1">

<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">
			$text.get("accperiods.Intercalate") 
		</div>
</div>



<div class="listRange" align="center">
	<div class="listRange_div_jag_hint" align="center">
		<div class="listRange_div_jag_div"><span style="float: right;">&nbsp;</span><span>$text.get("set.accounting.period")：</span></div>
		<div>
			<ul class="listRange_Prongix">
				<li>
				<span>$text.get("present.accounting.time")：$!currentlyPeriod
				<br>
				<br>$text.get("choose.accounting.time")：<input name="accPeriodDate" class="text" onClick="openInputDate(this)" readonly="readonly" value="$!currentlyPeriod" id="accPeriodDate" type="text" />
				&nbsp;
				<button type="submit" name="Submit" onClick="return beforSubmit();" style="padding-top: 3px;">$text.get("mrp.lb.save") </button></span>
				</li>
			</ul>
			
		</div>
		
	</div>
</div>
</form> 
</body>
</html>

