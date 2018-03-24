<html> 
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>授权请求</title>
<link type="text/css" rel="stylesheet" href="/style/css/about.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
$(function(){
	$("#scroll-wrap").height(document.body.clientHeight-18);
});

function doReject(){
	top.jQuery.fn.closeActiveTab();
}


function doPass(){
	document.form.submit();
}


</script>
</head>
<body class="html" >
<form  method="post" scope="request" id="form" name="form" action="/ServiceAction.do">
<input type="hidden" name="opType" value="auth">
<input type="hidden" name="exec" value="true">
<div id="scroll-wrap">
	<div class="wrap">
    	<p id="pageTitle"></p>
		<div id="bodyUl">
			<ul>				
				<li style="text-align:center">
					<div class="d-left" style="color:red;width:600px;float: none;text-align:left">
						操作有风险，授权需谨慎！务必确认客服人员身份。<br/>请将以下验证码告知客服，此验证码将在2分钟后失效。
					</div>
				</li>	
				<li style="text-align:center">
					<div class="d-left" style="width:600px;float: none;text-align:left">
						授权验证码：<font style="font-size:25px;color:red;float:none;font-weight: bold;">$!valNo</font>
					</div>
				</li>
	      </ul>
		</div>
	</div>
</div>
</form>
</body>
</html>