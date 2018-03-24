<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script type="text/javascript">
var style="$globals.getStylePath()";
function fillData(datas){
	newOpenSelectSearch(datas,fieldNames,fieldNIds);
	jQuery.close('Popdiv');
}
</script>
</head>
<body>
<form  method="post" scope="request" name="form" action="/OAWorkPlanQueryAction.do?opType=eventWorkList">
<input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
<input type="hidden" name="planType" value="event"> 
<input type="hidden" name="employee" id="employee" value="$!OADayWorkPlanSearchForm.employee" /> 
<input type="hidden" name="department" id="department" value="$!OADayWorkPlanSearchForm.department" /> 
<input type="hidden" name="winCurIndex" value="$!winCurIndex"> 
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("crm.event.plan")</div>
	<ul class="HeadingButton">
		<li><button name="Submit" type="submit" onClick="forms[0].operation.value=$globals.getOP("OP_QUERY");return beforeBtnQuery();" class="b2">$text.get("common.lb.query")</button></li>
		#if($globals.getMOperation().add())
		<li><button type="button" onClick="location.href='/OAWorkPlanAction.do?operation=6&planType=event&winCurIndex=$winCurIndex'" class="b2">$text.get("common.lb.add")</button></li>
		#end
		#if($globals.getMOperation().delete())
		<li><button type="submit" onClick="return sureDel('keyId');" class="b2">$text.get("common.lb.del")</button></li>
		#end
	</ul>
</div>
<div id="listRange_id">
	<div class="listRange_1">
		<li><span>$text.get("oa.subjects")：</span><input name="title" type="text" value="$!OADayWorkPlanSearchForm.title" maxlength="50"></li>
		<li><span>$text.get("customTable.lb.tableType")：</span><select name=typeId>
					<option value="" >$text.get("com.aioselect.all")</option>
					<option value="myPlan" #if($!OADayWorkPlanSearchForm.typeId == "myPlan") selected #end>$text.get("workplan.lb.myplan")</option>
			#foreach($row in $associate)	
				#if( "$!row.isEmployee" == "1")
					<option value="$!row.id" #if($!OADayWorkPlanSearchForm.typeId == "$!row.id") selected #end>${row.name}$text.get("oa.bbs.plan")</option>					
				#end
			#end
					
					</select></li>
		<li><span>$text.get("stat.statusId")：</span><select name=statusId>
					<option value="" ></option>
					<option value="0" #if($!OADayWorkPlanSearchForm.statusId == "0") selected #end>$text.get("oa.bbs.notFinish")</option>
					<option value="1" #if($!OADayWorkPlanSearchForm.statusId == "1") selected #end>$text.get("oa.bbs.finished")</option>
					</select></li>
		<li> 
			<span>$text.get("oa.common.department")：</span><input name="deptName"  id="deptName" type="text" value="$!deptMap.get($!OADayWorkPlanSearchForm.department)" maxlength="50" onclick="deptPopForAccount('deptGroup','deptName','department');" ondblclick="deptPopForAccount('deptGroup','deptName','department');"><img style="cursor:pointer;" src="/style1/images/St.gif" onClick="deptPopForAccount('deptGroup','deptName','department');" >	
		</li>
		<li>
			<span>$text.get("oa.common.employee")：</span><input name="empName" id="empName" type="text" value="$!globals.getEmpFullNameByUserId($!OADayWorkPlanSearchForm.employee)" maxlength="50" onclick="deptPopForAccount('userGroup','empName','employee');" ondblclick="deptPopForAccount('userGroup','empName','employee');"><img style="cursor:pointer;" src="/style1/images/St.gif" onClick="deptPopForAccount('userGroup','empName','employee');" >
		</li>
		<li><span>$text.get("scope.lb.tsscopeValue")≥：</span><input type="text" name="beginDate" value="$!OADayWorkPlanSearchForm.beginDate" maxlength="50" onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onClick="WdatePicker({lang:'zh_CN'});"></li>
		<li><span>$text.get("scope.lb.tescopeValue")≤：</span><input type="text" name="endDate" value="$!OADayWorkPlanSearchForm.endDate" maxlength="50" onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onClick="WdatePicker({lang:'zh_CN'});"></li>
		
	</div>
    <div class="scroll_function_small_a" id="conter">
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort" style="width:1024px;">
		<thead>
			<tr>
				<td width="30" align="center">&nbsp;</td>
				<td align ="center" width="25"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
				<td align ="center" width="120">$text.get("oa.common.employee")</td>							
				<td align ="center" width="100">$text.get("oa.common.department")</td>
				<td align ="center" width="*">$text.get("oa.subjects")</td>				
				<td align ="center" width="140">$text.get("scope.lb.tsscopeValue")</td>
				<td align ="center" width="140">$text.get("scope.lb.tescopeValue")</td>
				<td align ="center" width="80">$text.get("stat.statusId")</td>
				<td align ="center" width="80">$text.get("common.lb.detail")</td>
			</tr>
		</thead>
		<tbody>
			#set($num=1)
			#foreach ($row in $result )
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				<td align ="center">$num</td>
				<td align ="center"><input type="checkbox" name="keyId" value="$globals.get($row,0)"></td>
				<td align="left">$!globals.getEmpFullNameByUserId($globals.get($row,3)) &nbsp;</td>
				<td align="left">$!deptMap.get($globals.get($row,2)) &nbsp;</td>			
				<td align="left"><a href="/OAWorkPlanAction.do?operation=4&planType=event&keyId=$globals.get($row,0)&winCurIndex=$winCurIndex"> $!globals.encodeHTMLLine($globals.get($row,1)) </a> &nbsp;</td>
				<td align="left">$!globals.encodeHTMLLine($globals.get($row,4)) &nbsp;</td>
				<td align="left">$!globals.encodeHTMLLine($globals.get($row,5)) &nbsp;</td>
				<td align="left">#if($globals.get($row,6)=="0") $text.get("oa.bbs.notFinish") #else $text.get("oa.bbs.finished") #end &nbsp;</td>
				<td align="center"><a href="/OAWorkPlanAction.do?operation=4&planType=event&keyId=$globals.get($row,0)&winCurIndex=$winCurIndex"> $text.get("common.lb.detail") </a> &nbsp;</td>				
			</tr>
			#set($num=$num+1)
			#end
		</tbody>
		</table>
	</div>
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.documentElement.clientHeight-130;
oDiv.style.height=sHeight+"px";
</script>
		<div class="listRange_pagebar"> $!pageBar </div>
</div>
</form>
</body>
</html>
