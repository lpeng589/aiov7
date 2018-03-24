<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
	function beforeSubmit(){
		if(jQuery("#languageVal").val()==""){
			alert("选项值不能为空，请重新填写");
			jQuery("#languageVal").focus();
			return "false";
		}
		document.form.submit();
	}	

	function checkEnumerVal(obj){
		jQuery.ajax({
	 	  type: "POST",
	 	  url: "/CRMClientAction.do?operation=4&type=enumerVerify&enumerId="+jQuery("#enumerId").val()+"&enumerVal="+obj.value,
	 	  success: function(msg){
	 	  	if(msg == "yes"){
	 	  		alert("选项值已存在,请重新输入");
	 	  		obj.select();
	 	  		if(jQuery("#exist").length == 0){
		 	  		jQuery(obj).after('<font style="color: red;" id="exist">已存在</font>');
	 	  		}
	 	  		return false;
	 	  	}else{
	 	  		if(jQuery("#exist").length == 1){
	 	  			jQuery("#exist").remove();
	 	  		}
	 	  	}
	 	  }
		});
	}
</script>

</head>
<body>
<form name="form" action="/CRMClientAction.do" method="post" >
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_ADD')"/>
<input type="hidden" name="type" id="type" value="enumer"/>
<input type="hidden" name="enumerId" id="enumerId" value="$enumerId"/>
<input name="enumerVal" type="hidden" class="inp_w" value="$enumerVal"/>
<input name="sort" type="hidden" class="inp_w" value="100"/>
<div class="boxbg2 subbox_w700" style="width:400px;">
<div class="subbox cf" style="height: 30px;border: 0;">
  <div class="bd" style="height: 20px;">
      <div class="inputbox" style="margin-top: 10px;">
        <ul>
          <li><span>选项值：</span><input name="languageVal" id="languageVal" type="text" class="inp_w" onKeyDown="if(event.keyCode==13) return false;"/></li>
         	 
        </ul>
	         
      </div>
     
  </div>
</div>
</div>
</form>
</body>
</html>
