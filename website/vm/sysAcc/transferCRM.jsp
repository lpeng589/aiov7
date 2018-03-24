<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("crm.msg.transferTitle")</title>

<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<!--$text.get("sysAcc.msg.openAcc")
<input type="button"  onClick="openAcc()" value="$text.get("sysAcc.lb.openAcc")">-->
<script language="JavaScript">
function transfer()
{
	if(confirm("$text.get("crm.msg.crmmsgtext")"))
	{
		document.getElementById("submitID").disabled=true;
		location.href="/TransferCRMAction.do?operation=$globals.getOP("OP_UPDATE")";	
	}
}
</script> 
</head>
<body>

<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("crm.msg.transferTitle")</div>
	<ul class="HeadingButton">
		<li><button type="button" id="submitID" onClick="transfer()">$text.get("crm.msg.transferTitle")</button></li>
	</ul>
</div>
<div id="listRange_id" align="center">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
	<div class="listRange_sys">
		<ul class="listRange_1">
			<li class="sysAcctite">$text.get("crm.msg.transfer")：</li>
			<li class="sysAccconter">$text.get("crm.msg.transferInfo")</li>		
		</ul>
		<div id="showList"></div>
	</div>
</div>
</body>
</html>
