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
	if($("#sworkNo").val()==""){
		alert("客服帐号不能为空");
		return;
	}
	if($("#spass").val()==""){
		alert("客服密码不能为空");
		return;
	}	
	document.form.submit();
}
</script>
</head>
<body class="html" >
<form  method="post" scope="request" id="form" name="form" action="/ServiceAction.do">
<input type="hidden" name="opType" value="login">
<div id="scroll-wrap">
	<div class="wrap">
    	<p id="pageTitle">科荣AIO客服服务平台</p>
		<div id="bodyUl">
			<ul>
				<li>
					<div class="itemName" style="width:350px;text-align:center;padding-left:180px;">
						<ul>
							<li style="color:red;font-size:18px;padding-bottom:15px"><label>请输入科荣授权客服信息</label></li>
							<li><label>客服帐号：</label><input type="text" name="sworkNo" id="sworkNo"/></li>
							<li><label>客服密码：</label><input type="password" name="spass" id= "spass"/></li>
							<li><span style="width: 350px; height: 30px; display: block; text-align: center;">
							<label onclick="doSubmit();" style="float:none"
								class="regbut btn btn-small btn-danger">确定登入</label></span> </li>
						</ul></div>
					<div class="d-left" style="padding-left:30px;border-left: 2px solid #817D81">
						<ul>
							<li style="color:red;font-size:18px">注意：本服务需要由科荣客服和用户联合授权</li>
							<li>1、客服正确输入客服帐号和密码</li>
							<li>2、发送服务请求</li>
							<li>3、用户管理员接收服务请求消息并确认、产生服务验证码</li>
							<li>4、客服在2分钟内输入验证码，进入客服平台</li>
						</ul>
					</div>
				</li>	
	      </ul>
		</div>
	</div>
</div>
</form>
</body>
</html>