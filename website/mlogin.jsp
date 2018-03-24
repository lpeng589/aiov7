<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<title>$globals.getCompanyName('') </title>
<link rel="stylesheet" href="style/css/mlogin.css" type="text/css" />
<script type="text/javascript" src="/mobile/js/jquery.min.js"></script> 
<script type="text/javascript" src="/mobile/js/mlogin.min.js"></script> 

</head>
<body onload="loginInp();">
#if($globals.getSystemState().lastErrorCode != 0 || ($globals.getSystemState().dogState != 0 && $globals.getSystemState().dogState != 1 ))
	<span style="padding: 40px 5px;display: block;font-size: 25px;">系统启动状态异常!<br/>请从电脑端登入查看错误</span>
#else
<form >
	<div class="Login_win">
		<div class="Login_Ad"></div>
		<div class="Login_body">
			<ul>
				<li>
					<span>帐号</span><input id="name" type="text" value="" /></li>
				<li>
 					<span>密码</span><input id="password"  type="password" value=""/>
 				</li>
			</ul>
			<div>
			<table border="0" cellpadding="0" cellspacing="0" align="center">
				<tr >
					<td align="right" style="padding-top:2px;"><input type="checkbox" id="remainpass"/></td>
					<td align="left" onclick="changeremainpass()">&nbsp;<label for="remainpass">记住密码</label></td>					
				</tr>
			</table>
			</div>
			<div><button type="button" id="submitButton" onClick="formSubmit()">登录</button></div>
		</div>
	</div>
#end	
</form>	
</body>
</html>

