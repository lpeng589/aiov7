<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.common.myWaitAuditing ")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/date.vjs"></script>
<script language="javascript">
	function openInputDate(obj)
	{
		c.showMoreDay = false;
		c.show(obj);
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
</script>
<script type="text/javascript">
function selectEmployee()
{
	var urlstr = '/UserFunctionAction.do?operation=22&tableName=tblEmployee&selectName=SelectAllEmployee' ;
	var str = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=query","","dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)=="undefined")return ;
	var content = str.split(";") ;
	document.getElementById("name").value=content[0] ;
}
function openWorkFlowList()
{
	var urlStr="/MyWorkFlowAction.do?flowType=openWindow";
	var str=window.showModalDialog(urlStr);
	if(typeof(str)=="undefined")return ;
	var content=str.split(",");
	document.getElementById("tableFullName").value=content[0];
	document.getElementById("tableName").value=content[1];
}
</script>
</head>

<body>
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/OAMyWorkFlow.do">
 <input type="hidden" name="operation" value="4">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value=""> 
 <input type="hidden" name="loginId" value="">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("oa.my.workflow")</div>
	<ul class="HeadingButton">
	<li>
		<button name="Submits" type="submit" class="b2">$text.get("common.lb.query")</button>
	</li>
	</ul>
</div>
<div id="listRange_id">
		<div class="listRange_1">	
					<li><span>$text.get("oa.apply.by")：	</span>
						<input id="name" name="empFullName" value="$!workFlowForm.empFullName"
							onKeyDown="selectEmployee()"
							ondblclick="selectEmployee()"/><img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="selectEmployee()">
					</li>
					<li>
						<span>$text.get("oa.common.department")：</span>
						<select name="deptFullName">
							<option value="">&nbsp;</option>
							#foreach($depar in $deparList)
								<option value="$!depar" 
								#if($!depar==$!workFlowForm.deptFullName)
									selected
								#end
								>$!depar</option>
							#end
						</select>
						&nbsp;
					</li>
					<li>
						<span>$text.get("oa.apply.type")：</span>
						<input id="tableFullName" onDblClick="openWorkFlowList()" name="tableFullName" value="$!workFlowForm.tableFullName" type="text" /><img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="openWorkFlowList()"/></li>
					<li>
						<span>$text.get("common.msg.auditStatus"):</span>
						<select name="approveStatus">
							<option value=""></option>
							<option value="notApprove" #if("notApprove"=="$workFlowForm.approveStatus") selected #end)>$text.get("oa.mydesk.notApprove")</option>
							<option value="finish" #if("finish"=="$workFlowForm.approveStatus") selected #end>$text.get("oa.workflow.finish")</option>
							<option value="approved" #if("approved"=="$workFlowForm.approveStatus") selected #end>$text.get("common.Audited")</option>
							<option value="notPass" #if("notPass"=="$workFlowForm.approveStatus") selected #end>$text.get("com.not.pass")</option>
						</select>
					</li>
					<li>
						<span>$text.get("oa.apply.reason")：</span>
						<input id="applyContent" width="700px;" name="applyContent" value="$!workFlowForm.applyContent" type="text" />
						<input id="tableName" name="tableName" value="$!workFlowForm.tableName" type="hidden"/>
					</li>
					<li style="width: 340px;">
						<span>$text.get("oa.myTransaction.transactionTime")：</span>$text.get("oa.common.from")
						<input style="width:70px;" name="beginTime" value="$!workFlowForm.beginTime"
							onKeyDown="if(event.keyCode==13) openInputDate(this);"
							onClick="openInputDate(this);"/>
						$text.get("oa.common.to")
						<input style="width:70px;" name="endTime" value="$!workFlowForm.endTime"
							onKeyDown="if(event.keyCode==13) openInputDate(this);"
							onClick="openInputDate(this);"/>
						&nbsp;
					</li>
					
	</div>
<div class="scroll_function_small_a" id="conter">
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-118;
oDiv.style.height=sHeight+"px";
</script>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" id="tblSort" name="table">
		<thead>
			<tr>
				<td width="200" class="oabbs_tl">$text.get("oa.apply.reason")</td>
				<td width="120" class="oabbs_tc">$text.get("oa.application.date")</td>
			    <td width="80" class="oabbs_tc">$text.get("oa.apply.by")</td>
			    <td width="100" class="oabbs_tc">$text.get("oa.dept.name")</td>
	     	    <td width="100" class="oabbs_tc">$text.get("oa.apply.type")</td>
		        <td width="80" class="oabbs_t1">$text.get("common.msg.auditStatus")</td>
		        <td width="60" class="oabbs_tc">$text.get("common.lb.operation")</td>
			</tr>
		</thead>
		<tbody>
		#foreach($flow in $list_flow)
			#if("$!flow.workFlowNodeName"!="")
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
			  <td width="*" class="oabbs_tl">
			  	#if("$flow.flowType"=="data")
			  	<a href="/UserFunctionAction.do?tableName=$!flow.tbleName&keyId=$!flow.keyId&operation=5&isMyFlow=yes&LinkType=@URL:" target="_blank">$!flow.applyContent&nbsp;</a></td>
			  	#else
			  	<a href="/OAFileWorkFlowAction.do?operation=5&keyId=$!flow.keyId&winCurIndex=$!winCurIndex&isMyFlow=yes">$!flow.applyContent&nbsp;</a>
			  	#end
			  <td width="200" class="oabbs_tc">$!flow.applyDate&nbsp;</td>
			  <td width="100" class="oabbs_tc">$!flow.applyBy&nbsp;</td>
			  <td width="100" class="oabbs_tc">$!flow.department&nbsp;</td> 
			  <td width="100" class="oabbs_tc">$!flow.applyType&nbsp;</td> 
		      <td width="80" class="oabbs_t1" align="center">
			      	#foreach($erow in $globals.getEnumerationItems("WFQueyCondition"))
						#if($erow.value==$!flow.workFlowNodeName)
							$erow.name
						#end
					#end
		      </td>
		      <td width="60" class="oabbs_tc">
		      		#if("file"=="$!flow.flowType")
		      			#if($flow.flowType=="file" && "$!flow.checkPerson"=="$LoginBean.id" && "notPass"!="$!flow.workFlowNodeName")
		      				<a href="/OAFileWorkFlowAction.do?operation=5&keyId=$!flow.keyId&isMyFlow=yes&winCurIndex=$!winCurIndex">$text.get("common.lb.check")</a>
		      			#else
			      		&nbsp;
			      		#end
			      	#else
			      		#if($!flow.checkPerson.indexOf("userId:${LoginBean.id};")!=-1)
			      			<a href='/UserFunctionAction.do?tableName=$!flow.tbleName&keyId=$!flow.keyId&isMyFlow=yes&operation=5'>$text.get("common.lb.check")</a>&nbsp;		      		
			      			&nbsp;
			      		#else
			      			&nbsp;
			      		#end
			      	#end
		      	&nbsp;
		      	</td>
			</tr>
			#end
		#end
		  </tbody>
		</table>
	</div>
		<div class="listRange_pagebar"> $!pageBar </div>
</div>
</form>
</body>
</html>
