<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("sysAcc.msg.openAcc")</title>

<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<!--$text.get("sysAcc.msg.openAcc")
<input type="button"  onClick="openAcc()" value="$text.get("sysAcc.lb.openAcc")">-->
<script language="JavaScript">
function openAcc()
{
	if(confirm("$msgtext"))
	{
		if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
		location.href="/SysAccAction.do?exe=exe&type=$type&winCurIndex=$winCurIndex";
	}
}
</script> 
</head>
<body>

<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("sysAcc.lb.adjustExchange")</div>
	<ul class="HeadingButton">
		<li><button type="button" id="submitID"  onClick="openAcc()" class="b2">$text.get("sysAcc.lb.adjustExchange")</button></li>
	</ul>
</div>
<div id="listRange_id">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
	<div class="listRange_sys">
			<div class="sysAcctite">$text.get("sysAcc.msg.adjustExchangetite")：</div>
			<div class="sysAccconter"><span></span>$text.get("sysAcc.msg.adjustExchange")</div>
	</div>
</div>


</body>
</html>
