<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.common.myWaitAuditing ")</title>

<link rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" type="text/css"/>
<link type="text/css" rel="stylesheet" href="/style/css/workflow_list.css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript">
if(typeof(top.junblockUI)!="undefined"){
	top.junblockUI();
}

function showreModule(title,href){		
	top.showreModule(title,url);	
}
 
function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}
	
function clearForms(){
	for(var i=0;i<form.elements.length;i++){
		if(form.elements[i].type=="text") {
			if(form.elements[i].name != "pageNo"){
				form.elements[i].value="";
			}
		}
	}
	form.tableName.value="" ;
}

function flowDepict(applyType,keyId){ 
	window.open("/OAMyWorkFlow.do?keyId="+keyId+"&operation=$globals.getOP("OP_DETAIL")&applyType="+applyType);
}

function sureDel(statusId){ 
	if(!isChecked("keyId")){
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	
	form.operation.value=$globals.getOP("OP_DELETE");
	var msg="";
	if(statusId=='-1'){
		msg="$text.get("common.msg.confirmDel")";
	}else if(statusId=='delete'){
		msg="$text.get("common.msg.DestroyNotRevert")";
	}else{
		msg="$text.get("common.msg.RevertApproval")";
	}
	if(!confirm(msg)){
		form.operation.value = $globals.getOP("OP_QUERY");
		cancelSelected("input");
		return false;
	}
	form.statusId.value=statusId;
	form.submit();
}

function pageSubmit(pageNo){
	$("#pageNo").val(pageNo);
	jblockUI();
	form.submit();
}

function submitQuery(){
	jblockUI();
}

function jblockUI(){
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({message: "<img src='/style/images/load.gif'/>",css:{ background: 'transparent'}}); 
	}
}

function windowOpen(url){
	window.open(url);
}

function showType(){
	var displayName = "$text.get('oa.apply.type')"; 
	var urlstr = "/UserFunctionAction.do?selectName=OAMyWorkFlowType&operation=22&popupWin=Popdiv&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName;
	asyncbox.open({id:'Popdiv',title:displayName,url:urlstr,width:750,height:470});
}

function exePopdiv(str){
	if(typeof(str)=="undefined") return ;
	var note = str.split("#;#") ;
	$("#tableFullName").val(note[0]);
}

//处理双击弹出框



function fillData(datas){
	if(typeof(datas)=="undefined") return ;
	var note = datas.split(";") ;
	$("#empFullName").val(note[1]);
	jQuery.close('Popdiv');
}

function resetBegin(){
	if(!isChecked("keyId")){
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	form.operation.value="3";
	form.reset.value="reset";
	form.submit();
}
</script>
</head>

<body>
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/OAMyWorkFlowQuery.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value=""> 
 <input type="hidden" name="loginId" value="">
 <input type="hidden" name="statusId" value="">
 <input type="hidden" name="type" value="$!type">
 <input type="hidden" name="reset" value="">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingTitle">#if($!type.equals("all"))$text.get("oa.workflow.workflowQuery")#else$text.get("oa.workflow.RecycleBin")#end</div>
	<ul class="HeadingButton">
	<li><button name="Submits" type="submit" class="hBtns" onclick="submitQuery();">$text.get("common.lb.query")</button></li>
	#if($!type.equals("all"))
		#if($LoginBean.operationMap.get("/OAMyWorkFlowQuery.do?type=all&operation=4").delete())
		<li><button name="update" type="button" onClick="sureDel('-1')"  class="hBtns">$text.get("common.lb.del")</button></li>
		#end
		#if($LoginBean.operationMap.get("/OAMyWorkFlowQuery.do?type=all&operation=4").delete() || $LoginBean.operationMap.get("/OAMyWorkFlowQuery.do?type=all&operation=4&f=s").delete())
		<li><button name="update" type="button" onClick="resetBegin();"  class="hBtns" title="将结点重置到开始结点">重置</button></li>
		#end
	#else
	<li><button name="delete" type="button" onClick="sureDel('delete')"  class="hBtns">$text.get("common.lb.destroy")</button></li>
	<li><button name="update" type="button" onClick="sureDel('0')" class="hBtns">$text.get("crm.tab.rev")</button></li>
	#end
	</ul>
</div>
<div id="listRange_id">
		<div class="listRange_1" style="max-width:940px;float:left">	
					<li style="width: 420px;">
						<span>$text.get("oa.application.date")：</span>
						<input style="width:70px;" name="beginTime" value="$!workFlowForm.beginTime"
							onKeyDown="if(event.keyCode==13) openInputDate(this);"
							onClick="openInputDate(this);"/>
						-
						<input style="width:70px;" name="endTime" value="$!workFlowForm.endTime"
							onKeyDown="if(event.keyCode==13) openInputDate(this);"
							onClick="openInputDate(this);"/>
						&nbsp;
					</li>
					<li style="width: 400px;">
						<span>更新时间：</span>
						<input style="width:70px;" name="ebeginTime" value="$!workFlowForm.ebeginTime"
							onKeyDown="if(event.keyCode==13) openInputDate(this);"
							onClick="openInputDate(this);"/>
						-
						<input style="width:70px;" name="eendTime" value="$!workFlowForm.eendTime"
							onKeyDown="if(event.keyCode==13) openInputDate(this);"
							onClick="openInputDate(this);"/>
						&nbsp;
					</li>
					<li style="width: 210px;">
						<span>$text.get("oa.apply.type")：</span>
						<input id="tableFullName" onDblClick="showType()" name="tableFullName" value="$!workFlowForm.tableFullName" type="text" /><img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="showType()"/></li>
					<li style="width: 210px;">
						<span>$text.get("oa.apply.by")：</span>
						<input id="empFullName" onDblClick="deptPopForAccount('userGroup','empFullName','');" name="empFullName" value="$!workFlowForm.empFullName" type="text" /><img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForAccount('userGroup','empFullName','');"/>
					</li>
					<li style="width: 210px;">
						<span>$text.get("oa.workflow.lb.tempStatus")：</span>
						<select name="approveStatus">
							<option value=""></option>
							<option value="transing" #if($!workFlowForm.approveStatus=="transing") selected #end)>$text.get("common.lb.transacting")</option>
							<option value="finish" #if($!workFlowForm.approveStatus=="finish") selected #end>$text.get("common.lb.transacted")</option>
						</select>
					</li>
					<!--<li>
						<span>$text.get("common.msg.FlowBelongTo")</span>
						<select name="flowBelong">
							<option value="relation" #if($!workFlowForm.flowBelong=="relation") selected #end>$text.get("common.msg.CorrelationMe")</option>			
							<option value="self" #if($!workFlowForm.flowBelong=="self") selected #end)>$text.get("oa.workFlow.MeOriginate")</option>
							<option value="handle" #if($!workFlowForm.flowBelong=="handle") selected #end>$text.get("oa.workFlow.MeTransactor")</option>							
							<option value="all"  #if($!workFlowForm.flowBelong=="all") selected #end>$text.get("property.choose.item1")</option>				
						</select>
					</li> -->
					<li style="width: 210px;">
						<span>$text.get("oa.apply.reason")：</span>
						<input id="applyContent" width="700px;" name="applyContent" value="$!workFlowForm.applyContent" type="text" />
						<input id="tableName" name="tableName" value="$!workFlowForm.tableName" type="hidden"/>
					</li>
					
					
	</div>
<div class="scroll_function_small_a" id="conter">
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.documentElement.clientHeight-135;
oDiv.style.height=sHeight+"px";
</script>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" id="tblSort" name="table">
		<thead>
			<tr>
			    <td width="15" class="oabbs_tc" align="center" >&nbsp;</td>
				<td width="15" align="center" ><input type="checkbox" name="selAll"  onClick="checkAll('keyId')"></td>
				<td width="75">$text.get("oa.application.date")</td>
				<td width="40" class="oabbs_tc">$text.get("oa.apply.by")</td>
				<td width="50" class="oabbs_tc">$text.get("oa.dept.name")</td>
				<td width="80" class="oabbs_tc">$text.get("oa.apply.type")</td>
				<td width="120" class="oabbs_tc">$text.get("oa.apply.reason")</td>	
		        <td width="200" class="oabbs_tc">$text.get("oa.workFlow.StepAndFlowchart")</td>
		        <td width="80" class="oabbs_tc">更新时间
		        <!-- 
		        <td width="80" class="oabbs_tc">$text.get("oa.common.lastUpdateTime") -->
		        <td width="60" class="oabbs_tc">$text.get("common.lb.operation")</td>
			</tr>
		</thead>
		<tbody>
		#foreach($flow in $list)     
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);" >
			    <td width="15" class="oabbs_tc">$velocityCount</td>
				<td align="center" ><input type="checkbox" name="keyId" value="$!flow.id" styleFile="$!styleFiles.get($!flow.tableName)"  billTable="$!flow.tableName" ></td>
			  	<td class="oabbs_tc">$!flow.createTime&nbsp;</td>
			  	<td class="oabbs_tl">$!flow.applyBy&nbsp;</td>
			  	<td class="oabbs_tl" title="$!flow.department">$!flow.department&nbsp;</td> 
			  	<td title="$!flowTitle">$workFlowInfo.get("$!flow.applyType").getTemplateName()&nbsp;</td> 
			  	<td  style="word-wrap: break-word;word-break: normal;white-space:normal;height:100%">
		     	#if("$!flow.workFlowType"!="1")
			  	    <a href="javascript:mdiwin('/UserFunctionAction.do?tableName=$!flow.tableName&noback=true&currNodeId=$!flow.currentNode&keyId=$!flow.billId&operation=$globals.getOP("OP_DETAIL")&winCurIndex=$!winCurIndex&designId=$!flow.applyType&approveStatus=$!workFlowForm.approveStatus&OAFlowQueryType=$!type','#if($!type.equals("all"))$text.get("oa.workflow.workflowQuery")#else$text.get("oa.workflow.RecycleBin")#end')">$!flow.applyContent</a>&nbsp;
		 		#else
	    	  	    <a href="javascript:mdiwin('/OAWorkFlowAction.do?tableName=$!flow.tableName&noback=true&currNodeId=$!flow.currentNode&keyId=$!flow.billId&operation=$globals.getOP("OP_DETAIL")&winCurIndex=$!winCurIndex&designId=$!flow.applyType&approveStatus=$!workFlowForm.approveStatus&OAFlowQueryType=$!type','#if($!type.equals("all"))$text.get("oa.workflow.workflowQuery")#else$text.get("oa.workflow.RecycleBin")#end')">$!flow.applyContent</a>&nbsp;
	    	  	#end
			 	</td>
			  
		      <td width="80"  style="width:323px;word-wrap: break-word;word-break: normal;white-space:normal;height:100%"><a href="javascript:flowDepict('$!flow.applyType','$!flow.billId');">$!flow.flowDepict&nbsp;</a></td>
		      <td>$!flow.lastUpdateTime&nbsp;</td> 
		      <!-- 
		      <td>#if($!flow.lastUpdateTime)$!flow.lastUpdateTime#else$!flow.createTime#end&nbsp;</td>--> 
		      <td width="60" class="oabbs_tc"> 
			  #if("$!flow.workFlowType"!="1")
			 	<a href="javascript:mdiwin('/UserFunctionAction.do?tableName=$!flow.tableName&currNodeId=$!flow.currentNode&keyId=$!flow.billId&operation=$globals.getOP("OP_DETAIL")&winCurIndex=$!winCurIndex&designId=$!flow.applyType&approveStatus=$!workFlowForm.approveStatus&OAFlowQueryType=$!type','$!flowTitle')">$text.get("common.lb.detail")</a>&nbsp;
			  #else
			  	<a href="javascript:mdiwin('/OAWorkFlowAction.do?tableName=$!flow.tableName&currNodeId=$!flow.currentNode&keyId=$!flow.billId&operation=$globals.getOP("OP_DETAIL")&winCurIndex=$!winCurIndex&designId=$!flow.applyType&approveStatus=$!workFlowForm.approveStatus&OAFlowQueryType=$!type','$!flowTitle')">$text.get("common.lb.detail")</a>&nbsp;
			  #end
		      </td>
			</tr>
		#end
		  </tbody>
		</table>
	</div>
		<div class="page-wp">
			<div class="listRange_pagebar"> $!pageBar </div>
		</div>
</div>
</form>
</body>
</html>
