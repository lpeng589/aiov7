<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("sysAcc.lb.closeAcc")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="JavaScript">

function process(proType)
{
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	location.href="/ProcessOldData.do?operation=$globals.getOP("OP_UPDATE")&proType="+proType+"&winCurIndex=$winCurIndex";	
}
</script>

<style>
.acc {
	border-top:1px solid #1F8BE2;
	border-right:1px solid #1F8BE2;
}
.acctr {
	height:22px;
	background:url(/style/images/aiobg.gif) repeat-x left -21px;
}
.acc td {
	border-left:1px solid #1F8BE2;
	border-bottom:1px solid #1F8BE2; text-align:center; height:22px;
}
.td1 {
	background:#fff;
}
</style> 
</head>
<body>

<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("sysAcc.lb.closeAcc")</div>
	<ul class="HeadingButton">
		<li><button type="button" id="submitID"  onClick="process('affix')" class="b2">$text.get("common.lb.DisposalAccessoriesFormat")</button></li>
	</ul>
</div>
<div id="listRange_id">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
	<div class="listRange_sys">
			<div class="sysAcctite">$text.get("sysAcc.msg.closeAcctite")：</div>
			<div class="sysAccconter"><span></span>$text.get("sysAcc.msg.closeAccDet")</div>	
			
			<div class="scroll_function_small_a" align="center">		
		<br/><br/>
	</div>
	
		
		<div id="showList"></div>
		<div id="tip" align="center" style="color: red;font-size: 14px;font-weight:bold;"></div>
		<div id="course" align="center" style="color: red;font-size: 14px;font-weight:bold;"></div>
		
		<br>
		<div class="scroll_function_small_a">
		
		
	</div>
</div>
</body>
</html>
