<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<script type="text/javascript" src="/js/jquery.js"></script>

<style type="text/css">
.inputbox ul li{
	width: 380px;	
}
</style>


<script type="text/javascript">
	$(document).ready(function(){
		$("#moduleName").focus();
		
	});
	
	function beforeSubmit(){
		var moduleName = jQuery("#moduleName").val();
		if(moduleName==""){
			alert("模板名称不能为空，请重新填写");
			jQuery("#moduleName").focus();
			return "false";
		}
		
		jQuery.ajax({
			type: "POST",
			url: "/ClientSettingAction.do",
			data: "operation=1&addType=checkName&moduleName="+encodeURIComponent(encodeURIComponent(moduleName)),
			success: function(msg){
				if(msg=="exist"){
					alert("模板名称已存在,请修改");
					jQuery("#moduleName").focus();
					return "false";
				}else{
					if(typeof(top.jblockUI)!="undefined"){
						top.jblockUI({ message: " <img src='/style/images/load.gif'/>",css:{ background: 'transparent'}}); 
					}
					document.form.submit();
				}
			}
		});	
		
		
	}	
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form name="form" action="/ClientSettingAction.do" method="post" target="formFrame" >
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_ADD')"/>
<div class="boxbg2 subbox_w700" style="width:350px;">
<div class="subbox cf" style="height: 30px;border: 0;">
  <div class="bd" style="height: 20px;">
      <div class="inputbox" style="margin-top: 10px;">
        <ul>
          <li><span>模板名称：</span><input name="moduleName" id="moduleName" type="text" class="inp_w" onKeyDown="if(event.keyCode==13) return beforeSubmit();"/></li>
          <li><span>描述：</span><input name="moduleDesc" id="moduleDesc" type="text" class="inp_w" onKeyDown="if(event.keyCode==13) return beforeSubmit();" /></li>
        </ul>
      </div>
     
  </div>
</div>
</div>
</form>
</body>
</html>
