<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="/style1/css/workflow2.css" />
<link rel="stylesheet" type="text/css" href="/style/css/clientModule.css" />
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script language="javascript" src="/js/public_utils.js"></script>
<style type="text/css">
a{color:#000000;}
a:link{color:#000000;}
a:visited{color:#000000;}
a:hover{color:#000000;}
</style>
<script type="text/javascript">

function subForm(){
	if($("#show").html()!=""){
		$("input[name='moduleName']").val("");
		$("#show").html("");
		$("input[name='moduleName']").focus();
		return false;
	}

	var moduleName=$("input[name='moduleName']").val();
	if(moduleName==null  || moduleName==""){
		alert("模板名称不能为空，请重新填写!");
		return false;
	}
	
	form.submit();
}

function checkModueName(){
	var name=$("input[name='moduleName']").val();
	if( ("$type"!="copy" && "$!clientModule.moduleName"!=name) || "$type"=="copy" ){
		jQuery.ajax({
		   type: "get",
		   url: "/ClientSettingAction.do",
		   cache: false,
		   data: "operation=1&addType=checkName&moduleName="+name+"",
		   success: function(msg){
		   		if(msg=="exist"){
				     $("#show").html("模版名称已存在，请重新输入");
				     $("input[name='moduleName']").focus();
			    }else{
			    	$("#show").html("");
			    }
		    }
		});
	}
}

function delCheckBox(obj,boxName){
	if($(obj).attr("checked") == "checked"){
		$("#"+boxName).val("1");
	}else{
		$("#"+boxName).val("0");	
	}

}

function openFlowSet(){
	var workflowFile="CRMClientInfo";
	var newCreate="false";
	if(workflowFile=="" && globalFileName==""){ //如果没有流程设计文件（xml文件），则为新建
		newCreate="true";
	}
	var str="/UtilServlet?operation=BillNotFinish&keyId=$!workFlow.id";
 	AjaxRequest(str);
    var value = response;    	
    if(value!="no response text" && value.length>0){
    	if(confirm("存在未审核的单据，不能修改流程步骤！\n\n是否创建新的流程版本？")){
    		location.href="/ERPWorkFlowTempAction.do?operation=1&flowType=erp&type=copy&winCurIndex=$!winCurIndex&keyId=$!globals.get($result,0)" ;
    		return ;
    	}else{
    		return false ;
    	}
    }
    
	var width=screen.width ;
	var height=screen.height;
	var url="/vm/oa/workflow/workFlowDesign.jsp?ip=$!IP:$!port&workFileName=${workFlow.id}.xml&tableName=CRMClientInfo&newCreate="+newCreate;
	window.open(url);
}

function keyWorkStatus(obj){
	if($(obj).attr("checked") == "checked"){
		$(obj).val("1");
	}else{
		$(obj).val("0");
	}
}


document.onkeydown = keyDown; 
</script>
</head>
<body >
	<form method="post" scope="request" name="form"
		action="/ClientSettingAction.do?operation=$globals.getOP('OP_UPDATE')" class="mytable" >
		<input type="hidden" name="moduleId" id="moduleId" value="$!clientModule.id"/>
		<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value="$!clientModule.popedomDeptIds" />
		<input type="hidden" name="popedomUserIds" id="popedomUserIds" value="$!clientModule.popedomUserIds" />
		<input type="hidden" name="popedomEmpGroupIds" id="popedomEmpGroupIds" value="$!clientModule.popedomEmpGroupIds" />
		<input type="hidden" name="keyFields" id="keyFields" value="" />
		<input type="hidden" name="searchFields" id="searchFields" value="" />
		<input type="hidden" name="listFields" id="listFields" value="" />
		<input type="hidden" name="pageFields" id="pageFields" value="" />
		<input type="hidden" name="detailFields" id="detailFields" value="" />
		<input type="hidden" name="mustSelect" id="mustSelect" value="" />
		<input type="hidden" name="mustNames" id="mustNames" value="" />
		<input type="hidden" name="groupNames" id="groupNames" value="" />
		<input type="hidden" name="brotherTables" id="brotherTables" value="" />
		<input type="hidden" name="dbData" id="dbData" value="" onpropertyChange="javascript:if(this.value!=null)evaluate()"/>
		<input type="hidden" name="type" value="$type"/>
		<input type="hidden" name="moduleId" id="moduleId" value="$!moduleId" />
		<!-- 
		#if("$!clientModule.id"=="1" &&  "$type"!="copy" )
			<input type="hidden" name="moduleName" id="moduleName" value="$!clientModule.moduleName" />
			<input type="hidden" name="moduleDesc" id="moduleDesc" value="$!clientModule.moduleDesc"/>
		#end
		 -->
		<table cellpadding="0" cellspacing="0" class="framework">
			<tr>
				<td>
					<div class="TopTitle">
						<span>当前位置:模板显示设置</span>
						<div>
							<!-- 
							#if("$clientModule.id"=="1" && "$type"!="copy")
								<input type="button" class="bu_bg"  onclick="javascript:if(confirm('还原默认模版将会丢失原有设置，确定继续?')){defModule();}" value="还原模版" />
							#end
							 -->
							<input type="button" class="bu_02" onclick="subForm();" value="保存" />
							<!-- <input type="button" class="bu_02" onclick="javascript:window.location.href='/ClientSettingAction.do?operation=4'" value="返回" /> -->
						</div>
					</div>
					<div id="cc" class="data"
						style="overflow:hidden;overflow-y:auto;width:100%;">
						<script type="text/javascript">
							var oDiv=document.getElementById("cc");
							var sHeight=document.documentElement.clientHeight-55;
							oDiv.style.height=sHeight+"px";
						</script>
						<div class="boxbg2 subbox_w700">
							<div class="subbox cf">
								<div class="inputbox">
									<h4 style="margin-top: 1px;">
										<img src="/style/images/plan/column_a.gif" style="float: left;margin-right: 5px;margin-top: -2px;"/>基本信息<span style="float: right;">标<font color="red" size="0.1"> * </font>部分为必填内容</span>
									</h4>
									<ul>
										<li style="width: 100%;">
											<span>模版名称：</span>
											<input name="moduleName" type="text" class="inp_w"  #if("$!type"!="copy") value="$!clientModule.moduleName" #else value="" #end
											 onblur="checkModueName();"	style="width: 300px;" /><font color="red" style="float: left;">&nbsp;*</font><font id="show" style="width: 20%; float: left;"></font>
										</li>
										<li style="width: 100%;">
											<span>模板描述：</span>
											<input name="moduleDesc" type="text" class="inp_w2" #if("$!type"!="copy") value="$!clientModule.moduleDesc" #else value=""  #end />
										</li>
										<li style="width: 100%;">
											<span>启用关键字设置：</span>
											<input type="checkbox" id="keyWordStatus" name="keyWordStatus" style="margin-top: 6px;" onchange="keyWorkStatus(this)" #if("$!clientModule.keyWordStatus" == "1") value="1" checked="checked" #else value="0" #end/>
											<font>&nbsp;&nbsp;&nbsp;(启用关键字控制：(此功能主要是建立防撞单的机制。注意：启用关键字设置表示添加、修改操作时，客户名称不能包含此模板客户资料中"关键字"字段的内容))</font>
										</li>
										<li style="width: 100%;">
											<span>启用SWOT分析：</span>
											<input type="checkbox" id="swotStatus" name="swotStatus" style="margin-top: 6px;" onchange="keyWorkStatus(this)" #if("$!clientModule.swotStatus" == "1") value="1" checked="checked" #else value="0" #end/>
											<font>&nbsp;&nbsp;&nbsp;(启用SWOT分析：在客户资料详情页面可对客户进行强弱危机进行分析)</font>
										</li>
										<!--
										<li style="width: 100%;">
											<span>列表客户数：</span>
											<select name="defPageSize" id="defPageSize" >
												<option value="15" #if("$!clientModule.defPageSize"=="15") selected="selected"  #end>15</option>
												<option value="30" #if("$!clientModule.defPageSize"=="30") selected="selected"  #end>30</option>
												<option value="50" #if("$!clientModule.defPageSize"=="50") selected="selected"  #end>50</option>
												<option value="100" #if("$!clientModule.defPageSize"=="100") selected="selected"  #end>100</option>
												<option value="500" #if("$!clientModule.defPageSize"=="500") selected="selected"  #end>500</option>
												<option value="1000" #if("$!clientModule.defPageSize"=="1000") selected="selected"  #end>1000</option>
											</select>
											<font>（注：客户列表每页显示客户的数量）</font>
										</li>
										 
										<li style="width: 100%;">
											<span>启用审核流：</span>
											<input type="hidden" name="workflow" id="workflow" value="$clientModule.workflow"/>
											<input type="checkbox" name="workflow2" id="workflow2" style="border: 0;float: left;margin-top: 2px;" onclick="delCheckBox(this,'workflow')" #if($!clientModule.workflow == 1) checked="checked" #end />
											<span style="margin-left:20px;width:50px;">
											<button type="button" class="bu_02" onclick="openFlowSet();">流程设计</button>
											</span>
										</li>
										 -->
										 <!--
										<li style="width: 100%;">
											<span>启用客户标签：</span>
											<input type="hidden" name="isUserLabel" id="isUserLabel" value="$clientModule.isUserLabel"/>
											<input type="checkbox" name="labelFlag" id="labelFlag" style="border: 0;float: left;margin-top: 2px;" onclick="delCheckBox(this,'isUserLabel')" #if($!clientModule.isUserLabel == 1) checked="checked" #end />
										</li>
										 
											#if("$!loginName" == "admin")
											<li style="width: 100%;">
												<span>启用移交接收：</span>
												<input type="hidden" name="isUserTransfer" id="isUserTransfer" value="$!clientModule.isUserTransfer"/>
												<input type="checkbox" name="TransferFlag" id="TransferFlag" style="border: 0;float: left;margin-top: 2px;" onclick="delCheckBox(this,'isUserTransfer')" #if($!clientModule.isUserTransfer == 1) checked="checked" #end />
											</li>
											#end
										 -->
									</ul>
									<!-- 
									<h4>
										<img src="/style/images/plan/column_a.gif" style="float: left;margin-right: 5px;margin-top: -2px;"/>客户页面显示
									</h4>
									 -->
									<br />
								</div>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
