<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$globals.getCompanyName('') </title>
<link rel="stylesheet" href="style/css/loginMobile.css" type="text/css" />
<script language="javascript" src="$globals.js("/js/login.vjs","",$text)"></script>

#if($globals.getSystemState().lastErrorCode != 0 || ($globals.getSystemState().dogState != 0 && $globals.getSystemState().dogState != 1 ))
	<script language="javascript">
	window.location.href = 'common/fatalMessage.jsp?from=index';	
	 </script>
#end

</head>
<body onload="bodyloadMobile()">
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" action="/loginAction.do" name="LoginForm" onKeyDown="keyDownOp()" target="formFrame">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="NOTTOKEN" value="true">
<input type="hidden" name="operation" value="$globals.getOP("OP_LOGIN")"/>
<input type="hidden" id="noSuchUser" value="$text.get("login.lb.noSuchUser")"/>
<input type="hidden" name="ukId" value="">
<input type="hidden" name="strM1" value="">
<input type="hidden" name="strM2" value="">
<input type="hidden" name="clientKey" value="">
<input type="hidden" name="random" value="">
	<div class="Login_win">
		<div class="Login_Ad"></div>
		<div class="Login_body">
			<ul>
				<li><input name="name" tabindex="1" type="text" value="请输入用户名"  onfocus="if(this.value=='请输入用户名')this.value='';" onblur="if(this.value.length==0)this.value=defaultValue;  setLocStyle();" value="$!uname"/></li>
				<li id="pwdLi">
 					<input tabindex="2" id="password" name="password" type="password" value="" onblur="pwdBlur()" style="display:none"/>
 					<input tabindex="3" id="password2" name="password2" type="text" value="请输入密码" onfocus="pwdFocus()" style="display:inline"/>
 				</li>
				<li>
				<select name="loc" tabindex="3" onChange = "changeLoc(); ">
					#foreach($ks  in $LocaleTable.keys())
					<option value = '$ks' #if("$globals.getLocale()" == "$ks") selected #end>$globals.getLanguage($ks)   </option>
					#end
				</select>
				</li>
			</ul>
			<div>
			<table border="0" cellpadding="0" cellspacing="0" align="center">
				<tr >
			#if($globals.getSysSetting("MemoryPassword")=="true")
			<td align="right" style="padding-top:2px;"><input type="checkbox" id="remainpass" name="remainpass"/></td>
			<td align="left" onclick="changeremainpass()">&nbsp;$text.get("login.lb.MemoryPassword")</td>
			#else
			<td align="right" style="padding-top:2px;">&nbsp;</td>
			<td align="left">&nbsp;</td>
			#end
			
			<input type="hidden" name="style" value="style1"/>
					
				</tr>
			</table>
			</div>
			<div id="sunCom" style="color:red;"></div>
			<div><button type="button" id="submitButton" name="submitButton" onClick="mobiteSubmit()" tabindex="5">$text.get("login.lb.login")</button></div>
		</div>
	</div>
</form>	
</body>
</html>

