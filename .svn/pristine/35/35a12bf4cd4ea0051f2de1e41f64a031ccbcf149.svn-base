<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 <link rel="stylesheet" href="/style/css/controlPanel.css" type="text/css"/>
<script language="javascript" src="/js/function.js"></script>
<style type="text/css">

.column{

}
.column li {
	margin-left: 10%;
	
}

.column input{
	border:1px solid #42789C;
	width:130px;
	text-align:left;
}

.column span{
	color:#666666;
	font-weight:bold;
	float:left;	
	text-align:right;
	width:120px;
	text-indent:3px;
	height:30px;
		
}

.column span{
	float:left;
	margin-top:7px;
	color:#666666;
	letter-spacing:1px;
}

.column li {
	vertical-align:bottom;
	float:left;
	text-align:left;
	width:340px;
	height:30px;
	line-height:18px;
	color:#003E7B;
}

</style>


<script type="text/javascript">
function beforeSubmit(){
	var oldpassword = document.form.oldpassword ;
	var newpassword = document.form.newpassword;
	var confirmpassword = document.form.confirmpassword;
    if(!isNull(oldpassword.value,'$text.get("sms.modpass.oldpass")')){
    	oldpassword.focus() ;
		return false;
	}
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


function closeWin(){
	window.parent.location.href = "/ControlPanelAction.do?src=menu";
}
</script>
</head>
<body>

<iframe name="formFrame" style="display:none"></iframe>
<form action="/UserManageAction.do?operation=$globals.getOP("OP_UPDATE_PWD")&controlPanelUpdate=1" onsubmit="return beforeSubmit();" name="form" method="post" target="formFrame">
	
	<table cellpadding="0" cellspacing="0" class="framework">
		<tr>
			<!--右边列表开始-->
			<td>
				<div class="data" style="margin-top:25px;height: 405px;">
					<div class="data_title"><div class="control_list">$text.get("user.lb.modifyPass")</div></div>
					<div class="scroll_function_small_a">
								<div style="height: 200px;margin: 50px auto;width: 450px;border-bottom: #b4b4b4 1px solid;border-left: #b4b4b4 1px solid;border-right: #b4b4b4 1px solid;margin-top: 100px;">
								<div class="column_title" ></div>
								<ul class="column" style="width: 100%; height: 130px;" >
									<li>
										<span>$text.get("sms.modpass.oldpass")：</span>
										<input class="text" type="password" name="oldpassword" onKeyDown="if(event.keyCode==13)event.keyCode=9;"/>
									</li>
									<li>
										<span>$text.get("sms.modpass.newpass")：</span>
										<input class="text" type="password" name="newpassword" onKeyDown="if(event.keyCode==13)event.keyCode=9;"  />
									</li>	
									<li>
										<span>$text.get("userManager.lb.confirmpassword")：</span>
										<input class="text" type="password" name="confirmpassword" onKeyDown="if(event.keyCode==13)event.keyCode=9;"  />
									</li>								
									<li style="text-align: center;margin-top: 5px;">
										<button type="submit" name="Submit"  style="width: 80px;">$text.get("common.lb.save")</button>
										<button type="button" onclick="closeWin();" style="width: 80px;">$text.get("com.lb.close")</button>
									</li>	
								</ul>
							</div>																
					</div>	

				</div>
			</td><!--右边列表结束-->
		</tr>
	</table>
</body>
</html>
