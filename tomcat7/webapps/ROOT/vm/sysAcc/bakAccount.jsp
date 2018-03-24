<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("sysAcc.lb.yearCloseAcc")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<script type="text/javascript">
	function bakAccount(){
		if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
		window.location.href='SysAccAction.do?exe=exe&type=bakAccount' ;
	}
</script>
</head>
<body>

<div class="Heading">
	<div class="HeadingTitle">$text.get("com.sys.bakAccount")</div>
	<ul class="HeadingButton">
		<li><button type="button" id="submitID"  onClick="bakAccount();" class="hBtns">$text.get("com.sys.bakAccount")</button></li>
	</ul>
</div>


<div id="listRange_id">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
	<div class="listRange_sys">
			<div class="sysAcctite">$text.get("com.sys.bak.intro")</div>
			<div class="sysAccconter"><span></span>$text.get("com.sys.bak.remark")<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<ul>
		</ul>
	</div>
</div>
</body>
</html>
