<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<style type="text/css">
.inputbox ul li{
	width: 380px;	
}
</style>
<script type="text/javascript">
	$(document).ready(function(){
		$("#flowName").focus();
	});
	function beforeSubmit(){
		var flowName=jQuery("#flowName").val();
		flowName=jQuery.trim(flowName);
		if(flowName.length==0){
			alert("流程名称不能为空，请重新填写!");
			jQuery("#flowName").focus();
			return false;
		}
		
		if(!isHaveSpecilizeChar2(flowName)){
			alert("流程名称不能包含特殊字符，请重新输入!");
			jQuery("#flowName").select();
			jQuery("#flowName").focus();
			return false;
		}
		var design=jQuery("input[name='addType']:checked").val();
		jQuery("#designType").val(design);
		return true;
	}	
	
function submitForm(){
	var flowName=jQuery("#flowName").val();
	flowName=jQuery.trim(flowName);
	var templateFile=flowName;
	var designType=myform.designType.value;
	var tempClass=jQuery("#templateClass option:selected").val();
	jQuery.ajax({
	     type: "POST",
	     url: "/OAWorkFlowTempAction.do",
	     async:false,
	     data: "operation=1&type=addTable&templateFile="+templateFile+"&designType="+designType+"&tempClass="+tempClass,
	     success: function(data){
		     if(data!="failure"){
				alert("工作流流程添加成功!");
				window.parent.location="/OAWorkFlowTempAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&type=updateNew&keyId="+data;
			 }else{
				alert("工作流流程添加失败!");
				return false;
			 }
	     }
	},"text");
}
	
	
function flowKeyDown(){
	if(event.keyCode==13){
		var flag=beforeSubmit();
		if(flag==true){
			submitForm();
		}
	}
}
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form method="post"  scope="request" name="myform" class="mytable"  target="formFrame" >
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")" />
<input type="hidden" name="designType" id="designType" value="" />
<input type="hidden" id="win" name="winCurIndex" value="$!winCurIndex"/>
<div class="boxbg2 subbox_w700" style="width:350px;"> 
<div class="subbox cf" style="height: 30px;border: 0;">
  <div class="bd" style="height: 20px;">
      <div class="inputbox" style="margin-top: 10px;">
        <ul>
        	<li style="list-style: square"><span>流程名称：<font color="red">*</font></span>
          		<input  class="inp_w" style="width: 190px;" name="flowName" id="flowName" type="text"  onKeyDown="flowKeyDown();" maxlength="12"/>
        	</li>
        	<li style="margin-top: 7px;"><span>$text.get("oa.workflow.lb.tempClass")：<font color="red">*</font></span>
				<select  id="templateClass" name="templateClass" style="width:135px"  onKeyDown="if(event.keyCode==13) event.keyCode=9">
					#set($num=1)
					#foreach($row in $typeListbycata)
						<option  value="$globals.get($row,1)" 
						#if("$!selectClass"=="")
							#if("$num"=="1")
								selected 
							#end 
						#elseif("$!selectClass"!="" && "$!selectClass"=="$!globals.get($row,2)")
							selected
						#end
						>$globals.get($row,2)</option>
						#set($num=$num+1)
					#end
				</select>
			</li>
        	<li style="margin-top:7px;"><span>选择：</span>
	    		<span style="width:80px;"><input id="field_2"  checked="checked" type="radio" name="addType" value="1" style="border: 0px; margin-top:4px;" /><label for="field_2">个性化设计</label></span>
	        	<span style="width:80px;"><input id="field_1"  type="radio" style="border: 0px; margin-top:4px;" name="addType" value="2" /><label for="field_1">自定义设计</label></span> 
	    	</li>
        </ul>
      </div>
  </div>
</div>
</div>
</form>
</body>
</html>
