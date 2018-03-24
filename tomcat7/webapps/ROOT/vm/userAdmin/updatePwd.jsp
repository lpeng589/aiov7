<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("user.lb.modifyPass")</title>
<link rel="stylesheet" href=" /$globals.getStylePath()/css/classFunction.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript">
function beforeSubmit(){
	var oldpassword = document.form.oldpassword ;
	var newpassword = document.form.newpassword;
	var confirmpassword = document.form.confirmpassword;

    if(!isNull(newpassword.value,'$text.get("sms.modpass.newpass")')){
    	newpassword.focus() ;
		return false;
	}else if(!isNull(confirmpassword.value,'$text.get("userManager.lb.confirmpassword")')){
		confirmpassword.focus() ;
		return false;
	}else if(trimStr(form.newpassword.value) != trimStr(form.confirmpassword.value)) {
		alert("$text.get("userManager.msg.confirmPassword")");
		form.newpassword.value = "";
		form.confirmpassword.value = "";
		form.newpassword.focus();
		return false;
	}
	if(form.newpassword.value.length>0 && trimStr(form.newpassword.value)==""){
		alert("密码不能为空.");
		form.newpassword.value = "";
		form.confirmpassword.value = "";
		form.newpassword.focus();
		return false;
	}
	return true ;
}

function isNull(variable,message){
	if(variable=="" || variable==null){
		alert(message+" "+"$text.get('common.validate.error.null')");
		return false;
	}else{
		return true;
	}	
}
</script>
<style type="text/css">
button.hBtns{font-family:microsoft yahei;height:27px;line-height:21px;}
button.hBtns:hover{background:#ccc;}
.text{float:left;height:25px;width:300px;}
</style>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form action="/UserManageAction.do?operation=$globals.getOP("OP_UPDATE_PWD")" onsubmit="return beforeSubmit();" name="form" method="post" target="formFrame">
<div class="Heading">
	<div class="HeadingTitle">$text.get("user.lb.modifyPass")</div>

</div>
<div id="listRange_id" align="center">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
	<div class="scroll_function_small_a">
				<div class="listRange_div_jag_hint" align="center" style="height:230px;">
					<div class="listRange_div_jag_div"><span>$text.get("user.lb.modifyPass")</span></div>					
						<ul class="listRange_woke" style="overflow:hidden;">
							<li>
								<span>$text.get("sms.modpass.oldpass")：</span>
								<input class="text" type="password" name="oldpassword" onKeyDown="if(event.keyCode==13)event.keyCode=9;">
							</li>
							<li>
								<span>$text.get("sms.modpass.newpass")：</span>
								<input class="text" type="password" name="newpassword" onKeyDown="if(event.keyCode==13)event.keyCode=9;"  />
							</li>	
							<li>
								<span>$text.get("userManager.lb.confirmpassword")：</span>
								<input class="text" type="password" name="confirmpassword" onKeyDown="if(event.keyCode==13)event.keyCode=9;"  />
							</li>								
							<li style="text-align: center;">
								<button type="submit" name="Submit" class="hBtns" >$text.get("common.lb.save")</button>
								<button type="button" onClick="closeWin();" class="hBtns" >$text.get("com.lb.close")</button>
							</li>													
						</ul>
				</div>																		
	</div>			
</div>
</form>
</body>
</html>
