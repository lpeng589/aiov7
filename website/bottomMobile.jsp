<html> 
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$globals.getCompanyName('')</title>
	
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>

<script type="text/javascript" src="$globals.js("/js/mainBody.vjs","",$text)"></script>

<link rel="stylesheet" href="/style/css/homeMobile.css" type="text/css" />
<script language="JavaScript"> 
	

function backHome(){
	title = "";
	url = "/moduleFlow.do?operation=my_dest";
	top.showreModule(title,url);	
}

function sureLoginOut(){
	if(confirm('$text.get("login.lb.sureLoginOutSystem")','')) {
		window.location='/loginAction.do?operation=$globals.getOP("OP_LOGOUT")';
	}
}
function organization(){
	top.organization();
}

</script>
</head>
<body onUnload="RunOnUnload();">
<div class="bottom">
	<div>
		<span class="organization"><a href="javascript:organization();">&nbsp;&nbsp;</a></span>		
		<span class="Back"><a href="javascript:backHome();">&nbsp;&nbsp;</a></span>		
		<span class="logout" > <a href="javascript:sureLoginOut();">&nbsp;&nbsp; </a></span>
	</div>
		
</div>
</body>
</html>