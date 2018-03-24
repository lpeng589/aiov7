
<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>345</title>
		<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
	</head>
	<script language="javascript" src="/js/date.vjs"></script>
	<script type="text/javascript">
  
  function openSelect(urlstr,obj){ 
	
	var str  = window.showModalDialog(urlstr+"&MOID=0809fc77_0909100911555462068&MOOP=add","","dialogWidth=730px;dialogHeight=450px"); 

	fs=str.split(";");   	
	if(obj == "employeeFullname"){
		$("employeeNo").value = fs[0];
		$(obj).value = fs[1];
	}
	else if(obj == "departmentName"){
		$("departmentNo").value = fs[0];
		$(obj).value = fs[1];
	}
	else if(obj == "squadEnactmentNname"){
		$("squadEnactmentNo").value = fs[0];
		$(obj).value = fs[1];
	}
	
	getFristPage();  		
	return 0;
}

function openInputDate(obj)
{
	c.showMoreDay = false;
	c.show(obj);
}

function $(obj){
	return document.getElementById(obj);
}

function addPeriods(){
	window.location.href = "/vm/checkOnWorkAttendance/periodsAdd.jsp";
	
}

function clearForms(periods){
	for(var i=0;i<periods.elements.length;i++){

		if(periods.elements[i].type=="text" || periods.elements[i].type=="hidden") {
			if(periods.elements[i].name != "pageNo" && periods.elements[i].name != "pageSize"){
				periods.elements[i].value="";
			}
		}
	}
}

function formatTime(time1,time2){
	var times1 = time1.split("-");
	var times2 = time2.split("-");
	var date1 = new Date(times1[0],times1[1],times1[2]);
	var date2 = new Date(times2[0],times2[1],times2[2]);
	
	if(date1>date2){
		alert("$text.get('oa.msg.endDate.afterStartDate')");
		return false;
	}
	return true;
}

function statVal(){

	if("" == $("startDate").value){
		alert("$text.get('oa.msg.startDateNotNull')");
		return false;
	}
	if ($("endDare").value == ""){
		alert("$text.get('oa.msg.endDateNotNull')");
		return false;
	}
	if(!formatTime($("startDate").value,$("endDare").value)){
		return false;
	}
	
	return true;
}

function showDiv(){
	var periodsType = $("periodsType").value;
	if(periodsType == "0"){
		$("emp").style.display = "block";
		$("dep").style.display = "none";
		$("departmentNo").value = "";
		$("departmentName").value = "";
	}else if(periodsType == "1"){
		$("emp").style.display = "none";
		$("dep").style.display = "block";
		$("employeeNo").value = "";
		$("employeeFullname").value = "";
	}
}
  </script>

	<body>
		<form action="/DutyPeriodsAction.do?type=2" name="DutyPeriodsForm"
			method="post">
			<div class="Heading">
				<div class="HeadingIcon">
					<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
				</div>
				<div class="HeadingTitle">
					$text.get("dutyPeriods.search")
				</div>
				<ul class="HeadingButton">
					<li>
						<button name="add" type="button" onClick="addPeriods()" class="b2">$text.get("common.lb.add")
						</button>
					</li>
				</ul>
			</div>

			<div id="listRange_id">
				<div class="scroll_function_big">
					<ul class="listRange_1">
						<div id="emp">
						<li>
							<span>$text.get("oa.common.employee")：</span>
							<input name="employeeNo" id="employeeNo" value="$!DutyPeriodsForm.employeeNo" type="hidden"/>
							<input name="employeeFullname" id="employeeFullname" type="text"
								onDblClick="openSelect('/UserFunctionAction.do?tableName=tblArrangeSquad&selectName=empPeriods&operation=22','employeeFullname')"
								value="$!DutyPeriodsForm.employeeFullname" size="20"
								onkeypress="if(event.keyCode ==13){openSelect('/UserFunctionAction.do?tableName=tblArrangeSquad&selectName=empPeriods&operation=22','employeeFullname')}"
							/><img src="/$globals.getStylePath()/images/St.gif"
								onClick="openSelect('/UserFunctionAction.do?tableName=tblArrangeSquad&selectName=empPeriods&operation=22','employeeFullname')">
						</li></div>
						<div id="dep" style="display: none;">
						<li>
							<span>$text.get("oa.common.department")：</span>
							<input name="departmentNo" id="departmentNo" value="$!DutyPeriodsForm.departmentNo" type="hidden"/>
							<input name="departmentName" id="departmentName" type="text"
								onDblClick="openSelect('/UserFunctionAction.do?tableName=tblArrangeSquad&selectName=depPeriods&operation=22','departmentName')"
								value="$!DutyPeriodsForm.departmentName" size="20"
								onKeyPress="if(event.keyCode ==13){ openSelect('/UserFunctionAction.do?tableName=tblArrangeSquad&selectName=depPeriods&operation=22','departmentName')}" 
							/><img src="/$globals.getStylePath()/images/St.gif"
								onClick="openSelect('/UserFunctionAction.do?tableName=tblArrangeSquad&selectName=depPeriods&operation=22','departmentName')">
						</li></div>
						<li>
							<span>$text.get("dutyPeriods.squad.No")$text.get("unit.lb.name")：</span>
							<input name="squadEnactmentNo" id="squadEnactmentNo" value="$!DutyPeriodsForm.squadEnactmentNo" type="hidden"/>
							<input name="squadEnactmentNname" id="squadEnactmentNname" type="text"
								onDblClick="openSelect('/UserFunctionAction.do?tableName=tblDutyPeriods&selectName=squadPeriods&operation=22','squadEnactmentNname')"
								value="$!DutyPeriodsForm.squadEnactmentNname" size="20"
								onKeyPress="if(event.keyCode ==13){ openSelect('/UserFunctionAction.do?tableName=tblDutyPeriods&selectName=squadPeriods&operation=22','squadEnactmentNname')}"
							/><img src="/$globals.getStylePath()/images/St.gif"
								onClick="openSelect('/UserFunctionAction.do?tableName=tblDutyPeriods&selectName=squadPeriods&operation=22','squadEnactmentNname')">
						</li>
						<li>
							<span>$text.get("common.lb.startDate")：</span><font color="red">*</font>
							<input type="text" name="startDutyDate" id="startDutyDate"
								onchange="getFristPage()" onClick="openInputDate(this)"
								value="$!DutyPeriodsForm.startDutyDate" />
						</li>
						<li>
							<span>$text.get("common.lb.endDate")：</span><font color="red">*</font>
							<input type="text" name="endDutyDate" id="endDutyDate"
								onchange="getFristPage()" onClick="openInputDate(this)"
								value="$!DutyPeriodsForm.endDutyDate" />
						</li>
						<li>
							<span>$text.get("common.lb.queryAssort")：</span>
							<select  name="periodsType" id="periodsType" onChange="showDiv()">
								#if($!DutyPeriodsForm.periodsType == "0")
									<option value="0" selected="selected">$text.get("oa.common.employee")</option>
									<option value="1">$text.get("oa.common.department")</option>
								#elseif($!DutyPeriodsForm.periodsType == "1")
									<option value="0">$text.get("oa.common.employee")</option>
									<option value="1" selected="selected">$text.get("oa.common.department")</option>
								#end
							</select>
						</li>
					</ul>
					<ul style="float: right;">
						<li>
							<button name="search" id="search" type="submit" onClick="return statVal()" class="b2">
								$text.get("common.lb.query")
							</button>
							<button name="clear" type="botton" onClick="clearForms(DutyPeriodsForm)" class="b2">$text.get("common.lb.clear")
							</button>
						</li>
					</ul>
				</div>
				<div class="scroll_function_small_a" id="conter">
					<table border="0" cellpadding="0" cellspacing="0"
						class="listRange_list_function" id="tblSort" name="table"
						width="530">
						<THead>
							<tr class="listhead">
								<td class="listheade" width="30" align="center">
									<span style="vertical-align:middle;"><IMG
											src="/$globals.getStylePath()/images/down.jpg" border=0 /> </span>
								</td>
								#if ($!DutyPeriodsForm.periodsType == "0")
								<td width="100" align="center">
									$text.get("user.lb.employeeId")
								</td>
								<td width="100" align="center">
									$text.get("oa.oamessage.usergroup")
								</td>
								<td width="100" align="center">
									$text.get("oa.dept.name")
								</td>
								#elseif ($!DutyPeriodsForm.periodsType == "1")
								<td width="100" align="center">
									$text.get("oa.common.Department No.")
								</td>
								<td width="100" align="center">
									$text.get("oa.dept.name")
								</td>
								#end
								<td width="100" align="center">
									$text.get("com.checkOnWorkAttendance.periodsAdd.cycleName")
								</td>
								<td width="100" align="center">
									$text.get("squad.squadEnactmentName")
								</td>
								<td width="100" align="center">
									$text.get("oa.common.workingDay")




								</td>
								
							</tr>
						</THead>
						<TBody>
							#if ($!DutyPeriodsForm.periodsType == "0")
								#foreach($!periods in $!dutyPeriodses)
								<tr>
									<td class="listheadonerow" align="center" width="30">
										$!velocityCount
									</td>
									<td width="100" align="center">
									#if($!periods.employeeNo != "")
										$!periods.employeeNo #else &nbsp; #end
									</td>
									<td width="100" align="center">
									#if($!periods.employeeFullname != "")
										$!periods.employeeFullname #else &nbsp; #end
									</td>
									<td width="100" align="center">
									#if($!periods.departmentName != "")
										$!periods.departmentName #else &nbsp; #end
									</td>
									<td width="100" align="center">
									#if($!periods.periodsName != "")
										$!periods.periodsName #else &nbsp; #end
									</td>
									<td width="100" align="center">
									#if($!periods.squadEnactmentNname != "")
										$!periods.squadEnactmentNname #else &nbsp; #end
									</td>
									<td width="100" align="center">
									#if($!periods.date != "")
										$!periods.date #else &nbsp; #end
									</td>
								</tr>
								#end
							#elseif ($!DutyPeriodsForm.periodsType == "1")
								#foreach($!periods in $!dutyPeriodses)
								<tr>
									<td class="listheadonerow" align="center" width="30">
										$!velocityCount
									</td>
									<td width="100" align="center">
									#if($!periods.departmentNo != "")
										$!periods.departmentNo #else &nbsp; #end
									</td>
									<td width="100" align="center">
									#if($!periods.departmentName != "")
										$!periods.departmentName #else &nbsp; #end
									</td>
									<td width="100" align="center">
									#if($!periods.periodsName != "")
										$!periods.periodsName #else &nbsp; #end
									</td>
									<td width="100" align="center">
									#if($!periods.squadEnactmentNname != "")
										$!periods.squadEnactmentNname #else &nbsp; #end
									</td>
									<td width="100" align="center">
									#if($!periods.date != "")
										$!periods.date #else &nbsp; #end
									</td>
								</tr>
								#end
							#end
						</TBody>
					</table>
				</div>


				<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-140;
oDiv.style.height=sHeight+"px";
//]]>
</script>
				#if($enable == "true")
				<div class="listRange_pagebar" style="position:relative">
					$text.get("common.theNo")$pageNo$text.get("ao.common.page")&nbsp;&nbsp;
					<a href="javascript:document.DutyPeriodsForm.submit();"
						onclick="getFristPage()">$text.get("common.firstPage")</a>&nbsp;&nbsp;#if($pageNo!=1)
					<a href="javascript:document.DutyPeriodsForm.submit();" onClick="prePage()">$text.get("common.prePage")</a>#end&nbsp;&nbsp;
					#if($pageNo!=$pageSumList.size())
					<a href="javascript:document.DutyPeriodsForm.submit();"
						onclick="nextPage()">$text.get("common.nextPage")</a>#end&nbsp;&nbsp;&nbsp;
					<select name="pageNo" id="pageNo">
						#foreach($pageNumber in $pageSumList) #if($pageNo == $pageNumber)
						<option value="$pageNumber" selected="selected">
							$pageNumber
						</option>
						#else
						<option value="$pageNumber">
							$pageNumber
						</option>
						#end #end
					</select>
					<input type="hidden" id="pageSize" name="pageSize"
						value="$pageSize">
					<button type="submit" name="ppbutton">
						go
					</button>
					&nbsp;
				</div>
				#end
		</form>
	</body>
	<script type="text/javascript">
		function nextPage(){
			$("pageNo").value = Number($("pageNo").value) + 1;
		}
		
		function prePage(){
			$("pageNo").value = Number($("pageNo").value) - 1 < 1?1:Number($("pageNo").value)-1;
		}
		
		function getFristPage(){
			if($("pageNo") != null){
				$("pageNo").value = 1;
			}
			
		}
	</script>
</html>
