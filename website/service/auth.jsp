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
<input type="hidden" name="company" value="${company}">
<input type="hidden" name="name" value="$name">
<div id="scroll-wrap">
	<div class="wrap">
    	<p id="pageTitle"></p>
		<div id="bodyUl">
			<ul>
				<li style="text-align:center">
					<div class="d-left" style="color:red;width:600px;float: none;text-align:left">
						${company}客服人员[$name]请求授予客户服务的权限
					</div>
				</li>
				<li style="text-align:center">
					<div class="d-left" style="color:red;width:600px;float: none;text-align:left">
						操作有风险，授权需谨慎！务必确认客服人员身份！
					</div>
				</li>				
	      <li style="text-align:center">
	        <div class="d-left"  style="width:600px;float: none;text-align:left">
	        <label onclick="doPass();" style="float:none;margin-left:120px"
								class="regbut btn btn-small btn-danger">&nbsp;接&nbsp;受&nbsp;</label>
	        <label onclick="doReject();" style="float:none;margin-left:120px"
								class="regbut btn btn-small btn-danger">&nbsp;拒&nbsp;绝&nbsp;</label>
	        </div>
	      </li>
	      </ul>
		</div>
	</div>
</div>
</form>
</body>
</html>