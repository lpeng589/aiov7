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
		$("#viewName").focus();
		
	});
	function beforeSubmit(){
		if(jQuery("#viewName").val()==""){
			alert("视图名称不能为空，请重新填写");
			jQuery("#viewName").focus();
			return "false";
		}
		document.form.submit();
	}	
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form name="form" action="/ClientSettingAction.do" method="post" target="formFrame" >
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_ADD')"/>
<input type="hidden" name="addType" id="addType" value="moduleView"/>
<input type="hidden" name="moduleId" id="moduleId" value='$request.getParameter("moduleId")'/>
<div class="boxbg2 subbox_w700" style="width:350px;">
<div class="subbox cf" style="height: 30px;border: 0;">
  <div class="bd" style="height: 20px;">
      <div class="inputbox" style="margin-top: 10px;">
        <ul>
          <li><span>视图名称：</span><input name="viewName" id="viewName" type="text" class="inp_w" onKeyDown="if(event.keyCode==13) return beforeSubmit();"/></li>
          <li><span>描述：</span><input name="viewDesc" id="viewDesc" type="text" class="inp_w" onKeyDown="if(event.keyCode==13) return beforeSubmit();" /></li>
        </ul>
      </div>
     
  </div>
</div>
</div>
</form>
</body>
</html>
