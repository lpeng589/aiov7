<html> 
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>科荣AIO客服服务平台</title>
<link type="text/css" rel="stylesheet" href="/style/css/about.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
$(function(){
	$("#scroll-wrap").height(document.body.clientHeight-18);
});


function doSubmit(){
	if($("#valNo").val()==""){
		alert("验证码不能为空");
		return;
	}	
	document.form.submit();
}
#if("$!msg" != "")
	alert("$!msg");
#end


</script>
</head>
<body class="html" >
<form  method="post" scope="request" id="form" name="form" action="/ServiceAction.do"  >
<input type="hidden" name="opType" value="valator">
<div id="scroll-wrap">
	<div class="wrap">
    	<p id="pageTitle">科荣AIO客服服务平台</p>
		<div id="bodyUl">
			<ul>
				<li style="text-align:center">
					<div class="d-left" style="color:red;width:400px;float: none;text-align:left">
						系统已经发送授权请求消息给admin帐号，请等待客户授权
					</div>
				</li>
				<li style="text-align:center">
					<div class="d-left"  style="width:400px;float: none;text-align:left"> 授权验证码：
						<input type="text" name="valNo" id="valNo"/>
					</div>
				</li>
				
				
	      <li style="text-align:center">
	        <div class="d-left"  style="width:400px;float: none;text-align:left">
	        <label onclick="doSubmit();" style="float:none;margin-left:120px"
								class="regbut btn btn-small btn-danger">确定</label>
	        </div>
	      </li>
	      </ul>
		</div>
	</div>
</div>
</form>
</body>
</html>