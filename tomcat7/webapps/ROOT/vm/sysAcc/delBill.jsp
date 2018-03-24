<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("sysAcc.lb.reOpenAcc")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="JavaScript">
function submitSys()
{
	var periodYear=form.periodYear.value;
	var period=form.period.value;
	if(period.length==0||periodYear.length==0){
		alert("$text.get("Accounting.delBill.inputYearPeriod")");
		return ;
	}else{
		if(!isInt(periodYear)||!isInt(period)||(isInt(periodYear)&&(periodYear<1700||periodYear>9999))||(isInt(period)&&(period<1||period>12))){
			alert("$text.get("reCalculate.periodInt.error")");
			return;
		}
	}
	
	if(confirm("$msgtext"))
	{
		 document.getElementById("submitID").disabled=true;
		 form.submit();
		 if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
	}
}
</script> 
</head>
<body>

<form name="form" action="/SysAccAction.do?exe=exe&type=$type&winCurIndex=$winCurIndex" method="post"><div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("Accounting.delBill.title")</div>
	<ul class="HeadingButton">
		<li><button type="button" id="submitID"  onClick="submitSys()" class="b2">$text.get("common.lb.del")</button></li>
	</ul>
</div>

<div id="listRange_id">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
	<div class="listRange_sys">
			<div class="sysAcctite">$text.get("Accounting.delBill.synopsis")</div>
			<div class="sysAccconter">$text.get("Accounting.delBill.synopsisContent")</div>
		
			<div style="width:600px;float:right">
		<div style="margin:5px 0px 0 20px;padding-bottom:0px;float:right;width:99%;text-align:right;">
			<span style="float:left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;服务器IP：<input type="text" name="SERVER" class="text"  value=""></span>
		</div>
		<div style="margin:5px 0px 0 20px;padding-bottom:0px;float:right;width:99%;text-align:right;">
			<span style="float:left">数据库用户名：<input type="text" class="text" name="UID"  value=""></span>
		</div>
		<div style="margin:5px 0px 0 20px;padding-bottom:0px;float:right;width:99%;text-align:right;">
			<span style="float:left">&nbsp;&nbsp;&nbsp;数据库密码：<input type="password" class="text" name="PWD"  value=""></span>
		</div>
		<div style="margin:5px 0px 0 20px;padding-bottom:0px;float:right;width:99%;text-align:right;">
			<span style="float:left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数据库名：<input type="text" class="text" name="DataBase"  value=""></span>
		</div>
		<div style="margin:5px 0px 0 20px;padding-bottom:0px;float:right;width:99%;text-align:right;">
			<span style="float:left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$text.get("common.lb.periodYear")：<input type="text" class="text" name="periodYear"  value=""></span>
		</div>		
		<div style="margin:5px 0px 0 20px;padding-bottom:0px;float:right;width:99%;text-align:right;">
			<span style="float:left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$text.get("common.lb.period")：<input type="text" name="period" class="text"  value=""></span>
		</div>
	</div>
</div>
</form>
</body>
</html>

