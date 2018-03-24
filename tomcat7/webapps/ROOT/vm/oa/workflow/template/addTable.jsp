<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<style type="text/css">
.inputbox ul li{
	width: 380px;	
}
</style>
<script type="text/javascript">
	var moduleType = "$!moduleType";
	$(document).ready(function(){
		$("#flowName",document).focus();
	});
	function beforeSubmit(){
		var flowName=$("#flowName",document).val();
		flowName=jQuery.trim(flowName);
		if(flowName.length==0){
			alert("流程名称不能为空，请重新填写!");
			$("#flowName",document).focus();
			return false;
		}
		
		if(!containSC2(flowName)){
			alert("流程名称只允许为英文,数字和汉字的混合，请重新输入!");
			$("#flowName",document).select();
			$("#flowName",document).focus();
			return false;
		}
		var design=$("input[name='addType']:checked",document).val();
		$("#designType",document).val(design);
		return true;
	}
	
	
/* function submitForm(){
	var flowName = jQuery.trim($("#flowName",document).val());
	var fromTemplate = $("#fromTemplate option:selected",document).val();//表单模板
	var templateFile = flowName;			
	var designType=myform.designType.value;
	var tempClass=$("#templateClass option:selected",document).val();
	jQuery.ajax({type: "POST",async:false,
	     url: "/OAWorkFlowTempAction.do",
	     data: "operation=1&type=addTable&templateFile="+templateFile+"&designType="+designType+"&tempClass="+tempClass+"&moduleType="+moduleType+"&fromTemplate="+fromTemplate,
	     success: function(data){
		     if(data!="failure"){
				alert("工作流流程添加成功!");
				window.parent.location="/OAWorkFlowTempAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&type=updateNew&keyId="+data+"&moduleType="+moduleType;
			 }else{
				alert("工作流流程添加失败!");
				return false;
			 }
	     }
	},"text");
} */
 function submitForm(){
	var flowName = jQuery.trim($("#flowName",document).val());
	var fromTemplate = $("#fromTemplate option:selected",document).val();//表单模板ID OAWorkFlowTemplate
	var templateFile = flowName;			
	var designType=myform.designType.value;
	var tempClass=$("#templateClass option:selected",document).val();
	var paramer = "operation=1&type=addTable&templateFile="+templateFile+"&designType="+designType+"&tempClass="+tempClass+"&moduleType="+moduleType+"&fromTemplate="+fromTemplate;
	jQuery.ajax({type: "POST",async:false,
	     url: "/OAWorkFlowTempAction.do",
	     data: paramer,
	     success: function(data){
		     if(data!="failure"){
				alert("工作流流程添加成功!");
				window.parent.location="/OAWorkFlowTempAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&type=updateNew&keyId="+data+"&moduleType="+moduleType;
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

/**
*  改变类型
*/
function changeType(){ 	return;
	var tclass = $("#templateClass option:selected",document).val();
	if(tclass.indexOf("00002")!=-1){
		$(".flowType",document).show();
		$("field_2",document).attr("checked",true);	
	}else{
		$(".flowType",document).hide();
		$("field_1",document).attr("checked",true);	
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
<input type="hidden" id="moduleType" name="moduleType" value="$!moduleType"/>
<div class="boxbg2 subbox_w700" style="width:350px;"> 
<div class="subbox cf" style="height: 30px;border: 0;">
  <div class="bd" style="height: 20px;">
      <div class="inputbox" style="margin-top: 10px;">
        <ul>
        	<li style="list-style: square"><span>流程名称：<font color="red">*</font></span>
          		<input  class="inp_w" style="width: 190px;" name="flowName" id="flowName" type="text"  onKeyDown="flowKeyDown();" maxlength="12"/>
        	</li>
        	<li style="margin-top: 7px;"><span>$text.get("oa.workflow.lb.tempClass")：<font color="red">*</font></span>
				<select  id="templateClass" name="templateClass" style="width:135px"  onchange="changeType();"  onKeyDown="if(event.keyCode==13) event.keyCode=9">
					#if($selectClass != "00001" && $selectClass != "00003") #set($selectClass="00002")  #end
					#foreach($row in $typeListbycata)
						#if($globals.get($row,1) != "00001" && $globals.get($row,1) != "00003")
						#if("$!selectClass"=="")
							#set($selectClass=$!globals.get($row,2))
						#end
						<option value="$globals.get($row,1)" #if($!selectClass.indexOf("$!globals.get($row,2)")!=-1) selected #end >$globals.get($row,2)</option>
						#end
					#end
				</select>
			</li>
        	<li style="margin-top: 7px;"><span>表单模板：</span>
				<select  id="fromTemplate" name="fromTemplate" style="width:135px"  onchange="changeFromTemp();"  >
							<option value="" >--请选择--</option>
					#if($fromTempMap)	
						#foreach($template in $fromTempMap.entrySet())
							<option value="$template.value" >$template.key</option>
						#end
					#end
				</select>
			</li>
        	<li class="flowType" style="margin-top:7px;#if(true || $!selectClass.indexOf("00002")==-1)display:none;#end" ><span>选择：</span>
	    		<span style="width:80px;"><input id="field_2"  type="radio" name="addType" value="1" style="border: 0px; margin-top:4px;" /><label for="field_2">个性化设计</label></span>
	        	<span style="width:80px;"><input id="field_1"  type="radio" checked="checked"  style="border: 0px; margin-top:4px;" name="addType" value="2" /><label for="field_1">自定义设计</label></span> 
	    	</li>
        </ul>
      </div>
  </div>
</div>
</div>
</form>
</body>
</html>
