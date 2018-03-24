<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js"></script>

<script>
function beforeDelete(){
	var keyIds= document.getElementsByName("keyId")
	for(var i=0;i<keyIds.length;i++){
		if(keyIds[i].checked&&keyIds[i].statusId=="100"){
			alert("$text.get("workflow.msg.SysWorkflowNotDel")");
			return false;
		}
	}
			
	if(sureDel('keyId')){
		form.submit();
	}
}

function beforeOp(){
	var len=$("input[name='keyId']:checked").size();
	if (len == 0){
		alert ("$text.get("common.msg.selectone")");
		return false;
	}
	var sendOpkid = "";
	$("input[name='keyId']:checked").each(function(){
		sendOpkid += $(this).val() + ",";
	})
	document.getElementsByName("sendOpKeyId")[0].value = sendOpkid;
	document.form.submit();
}
//TODO Crokery 工作流

function beforeEnOp(){
	var len=$("input[name='keyId']:checked").size();
	if (len == 0){
		alert ("$text.get("common.msg.selectone")");
		return false;
	}
	var sendEnkid = "";
	$("input[name='keyId']:checked").each(function(){
		sendEnkid += $(this).val() + ",";
	})
	document.getElementsByName("sendEnKeyId")[0].value = sendEnkid;
	document.form.submit();
}
function openFlowSet(){
	var width=screen.width ;
	var height=screen.height;

	var objs=document.getElementsByName("keyId");
	var count=0;
	var objKey=null;
	for(var i=0;i<objs.length;i++){
		if(objs[i].checked){
			count++;
			objKey=objs[i];
		}
	} 
	if(count>1||count==0){
		alert("$text.get("workflow.lb.designone")");
		return false;
	}
	
	
	var str="/UtilServlet?operation=BillNotFinish&keyId="+objKey.value;
 	AjaxRequest(str);
    var value = response;    	
    if(value!="no response text" && value.length>0){
    	alert("$text.get("oaworkFlow.msg.noUpdate")");
    	return false;
    }
    
	window.open("/vm/oa/workflow/workFlowDesign.jsp?ip=$host&workFileName="+objKey.value+".xml&tableName="+objKey.tableName,"","width="+width+";height="+height);

}
</script>
</head>
<body onLoad="showtable('tblSort');showStatus();">
<form  method="post" scope="request" name="form" action="/ERPWorkFlowTempSearchAction.do?comeType=erp">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
 <input type="hidden" name="sendOpKeyId" value="">
 <input type="hidden" name="sendEnKeyId" value="">
 
 
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">
		$text.get("oa.workflow.title.workflowmanager")
	</div>
	
	<ul class="HeadingButton">
		#if($globals.getMOperation().query())
		<li><button name="Submit" type="submit" onClick="forms[0].winCurIndex.value='$!winCurIndex'; forms[0].operation.value=$globals.getOP("OP_QUERY");return beforeBtnQuery();" class="b2">$text.get("common.lb.query")</button></li>
		#end
		#if($globals.getMOperation().update())
			<li><button type="button" onclick="return beforeOp()" class="b2">$text.get("wokeflow.lb.enable")</button></li>
			<li><button type="button" onclick="return beforeEnOp()" class="b2">$text.get("wokeflow.lb.noEnable")</button></li>	
			<li><button type="button"  onClick="openFlowSet()" >$text.get("oa.workflow.lb.workflowSet")</button></li>
		#end
	</ul>
</div>
<div id="listRange_id">
		<div class="listRange_1">
			<li><span>$text.get("customTable.lb.tableName"):</span><input name="tableName" type="text" value="$!OAWorkFlowTempSearchForm.tableName" maxlength="50"></li>
			<li><span>$text.get("oa.workflow.lb.tempName"):</span><input name="templateName" type="text" value="$!OAWorkFlowTempSearchForm.templateName" maxlength="50"></li>
			<li><span>$text.get("oa.workflow.lb.tempStatus"):</span>
			<select name="templateStatus">
				<option value="-1">&nbsp;</option>							
				<option value="1" #if("$!OAWorkFlowTempSearchForm.templateStatus"=="1")	selected #end>$text.get("wokeflow.lb.enable")</option>
				<option value="0" #if("$!OAWorkFlowTempSearchForm.templateStatus"=="0")	selected #end>$text.get("wokeflow.lb.noEnable")</option>
			</select>
			</li>
		</div>
		<div class="scroll_function_small_a" id="conter">
	<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-105;
oDiv.style.height=sHeight+"px";
</script>
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort" style="width:700px;">
			<thead>
				<tr>
					<td width="30" align="center"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
					<td width="30" align="center">&nbsp;</td>
					<td width="150" align="center">$text.get("customTable.lb.tableName")</td>
					<td width="150" align="center">$text.get("oa.workflow.lb.tempName")</td>
					<td width="100" class="oabbs_tc">$text.get("common.lb.status")</td>
					<td width="100" class="oabbs_tc">$text.get("wokeflow.list.isDesigned")</td>
					#if($globals.getMOperation().update())
					<td width="60" align="center">$text.get("common.lb.update")</td>
					#end
				</tr>
			</thead>
			<tbody>
				#foreach ($row in $result )
				<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
					<td align="center">
						<input type="checkbox" name="keyId" tableName="$row.get("templateFile")"  value="$!row.get("id")">  </td>
						#set($count=$!fromCount+$velocityCount)
						<td width="30" align="center">$count</td>
						<td align="left">$!row.get("templateFile")</td>
						<td align="left">$!row.get("templateName")</td>
						<td align="left">
						#if($!row.get("templateStatus") == "1")
						$text.get("wokeflow.lb.enable")
						#elseif($!row.get("templateStatus") == "0")
						$text.get("wokeflow.lb.noEnable")
						#end</td>
						<td align="center">
						#if($!row.get("fileFinish") == "1")
						$text.get("wokeflow.lb.isDesign")
						#elseif($!row.get("fileFinish") == "0")
						$text.get("wokeflow.lb.noDesign")
						#end
						</td>
					#if($globals.getMOperation().update())					
						<td align="center"><a href='/ERPWorkFlowTempAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId=$!row.get("id")&winCurIndex=$!winCurIndex'>$text.get("common.lb.update")</a></td>
					#end
				</tr>
				#end	
			</tbody>
		  </table>
		  	
		</div>
		<div class="listRange_pagebar"> $!pageBar </div>
</div>
</form>
</body>
</html>
