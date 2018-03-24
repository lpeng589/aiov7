<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>345</title>
		<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
	</head>
	<script language="javascript" src="/js/date.vjs"></script>
	<script type="text/javascript">
	function openInputDate(obj)
{
	c.showMoreDay = false;
	c.show(obj);
}

  function openSelect(urlstr){ 
	
	var str  = window.showModalDialog(urlstr+"&MOID=0809fc77_0909100911555462068&MOOP=add","","dialogWidth=730px;dialogHeight=450px"); 

	fs=str.split(";");   	
	$("employeeNo").value = fs[0];
	$("employeeName").value = fs[1];
	getFristPage();
}

function $(obj){
	return document.getElementById(obj);
}

function stat(){
	$("operation").value = 1;
	document.WorkRuleListReportForm.submit();
}

function empIdClear(){
	if($("employeeName").value == ""){
		$("employeeNo").value = "";
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
function clearForms(periods){
	for(var i=0;i<periods.elements.length;i++){

		if(periods.elements[i].type=="text" || periods.elements[i].type=="hidden") {
			if(periods.elements[i].name != "pageNo" && periods.elements[i].name != "pageSize"){
				periods.elements[i].value="";
			}
		}
	}
}


  </script>

	<body>
	<form action="/WorkRuleReportAction.do?type=1" id="WorkRuleListReportForm" name="WorkRuleListReportForm" method="post">
		<div class="Heading">
			<div class="HeadingIcon">
				<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
			</div>
			<div class="HeadingTitle">
				$text.get("stat.list.report")
			</div>
			<ul class="HeadingButton">
				<li>
					<button type="button" title="$text.get('common.hr.afreshCountReport')" onClick="stat()" class="b4">$text.get("stat.analyse.report")
					</button>
				</li>
			</ul>
		</div>
		
		<input type="hidden" name="operation" id="operation" value="4">
		<div id="listRange_id">
				<div class="listRange_1">
					<li>
						<span>$text.get("stat.empFullName")：</span>
						<input name="employeeNo" id="employeeNo" type="hidden" value="$!WorkRuleListReportForm.employeeNo">
						<input name="employeeName" id="employeeName" type="text"
							onDblClick="openSelect('/UserFunctionAction.do?tableName=tblWorkRuleListReport&&selectName=employeeLR&operation=22')"
							value="$!WorkRuleListReportForm.employeeName" size="20"
							onchange="empIdClear()"
							onKeyPress="if(event.keyCode ==13){ openSelect('/UserFunctionAction.do?tableName=tblWorkRuleListReport&selectName=employeeLR&operation=22')}"
							/><img src="/$globals.getStylePath()/images/St.gif"
							onClick="openSelect('/UserFunctionAction.do?tableName=tblWorkRuleListReport&selectName=employeeLR&operation=22')">
					</li>
					<li>
						<span>$text.get("stat.lateCount")：</span>
						<input type="text" name="lateCount" id="lateCount" onChange="getFristPage()" value="$!WorkRuleListReportForm.lateCount"/>
					</li>
					<li>
						<span>$text.get("stat.lateMinute")：</span>
						<input type="text" name="lateMinute" id="lateMinute"  onchange="getFristPage()" value="$!WorkRuleListReportForm.lateMinute"/>
					</li>
					<li>
						<span>$text.get("stat.LeaveEarlyCount")：</span>
						<input type="text" name="leaveEarlyCount" id="leaveEarlyCount" onChange="getFristPage()" value="$!WorkRuleListReportForm.leaveEarlyCount"/>
					</li>
					<li>
						<span>$text.get("stat.LeaveEarlyMinute")：</span>
						<input type="text" name="leaveEarlyMinute" id="leaveEarlyMinute" onChange="getFristPage()" value="$!WorkRuleListReportForm.leaveEarlyMinute"/>
					</li>
					<li>
						<span>$text.get("stat.AbsentWorkCount")：</span>
						<input type="text" name="absentWorkCount" id="absentWorkCount" onChange="getFristPage()" value="$!WorkRuleListReportForm.absentWorkCount"/>
					</li>
					<li>
						<span>$text.get("stat.AbsentWorkDay")：</span>
						<input type="text" name="absentWorkDay" id="absentWorkDay" onChange="getFristPage()" value="$!WorkRuleListReportForm.absentWorkDay"/>
					</li>
					<li>
						<span>$text.get("stat.LeaveCount")：</span>
						<input type="text" name="leaveCount" id="leaveCount" onChange="getFristPage()" value="$!WorkRuleListReportForm.leaveCount"/>
					</li>
					<li>
						<span>$text.get("stat.LeaveDay")：</span>
						<input type="text" name="leaveDay" id="leaveDay" onChange="getFristPage()" value="$!WorkRuleListReportForm.leaveDay"/>
					</li>
					<li>
						<span>$text.get("stat.EvectionCount")：</span>
						<input type="text" name="evectionCount" id="evectionCount" onChange="getFristPage()" value="$!WorkRuleListReportForm.evectionCount"/>
					</li>
					<li>
						<span>$text.get("stat.EvectionDay")：</span>
						<input type="text" name="evectionDay" id="evectionDay" onChange="getFristPage()" value="$!WorkRuleListReportForm.evectionDay"/>
					</li>
					<li>
						<span>$text.get("common.lb.startDate")：</span><font color="red">*</font>
						<input type="text" name="startDate" id="startDate" onClick="openInputDate(this)" onChange="getFristPage()" value="$!WorkRuleListReportForm.startDate"/>
					</li>
					<li>
						<span>$text.get("common.lb.endDate")：</span><font color="red">*</font>
						<input type="text" name="endDare" id="endDare" onClick="openInputDate(this)" onChange="getFristPage()" value="$!WorkRuleListReportForm.endDare"/>
					</li>
			</div>
				<div class="scroll_function_big">
					<button name="search" title="$text.get('common.hr.queryAnalysedReport')" onClick="return statVal()" id="search" type="submit" class="b4">$text.get("stat.query.report")</button>
					<button name="clear" type="botton" onClick="clearForms(WorkRuleListReportForm)" class="b2">
								$text.get("common.lb.clear")
							</button>
				</div>
			<div class="scroll_function_small_a" id="conter">
				<table border="0" cellpadding="0" cellspacing="0"
					class="listRange_list_function" id="tblSort" 
					>
					<THead>
						<tr class="listhead">
							<td class="listheade" width="30" align="center">
								<span style="vertical-align:middle;"><IMG
										src="/$globals.getStylePath()/images/down.jpg" border=0 />
								</span>
							</td>
							<td width="90" align="center">
								$text.get("stat.reportNo")
							</td>
							<td width="60" align="center">
								$text.get("stat.empFullName")
							</td>
							<td width="60" align="center">
								$text.get("stat.lateCount")
							</td>
							<td width="85" align="center">
								$text.get("stat.lateMinute")
							</td>
							<td width="85" align="center">
								$text.get("stat.penalty.late")
							</td>
							<td width="60" align="center">
								$text.get("stat.LeaveEarlyCount")
							</td>
							<td width="85" align="center">
								$text.get("stat.LeaveEarlyMinute")
							</td>
							<td width="85" align="center">
								$text.get("stat.penalty.leaveEarly")
							</td>
							<td width="60" align="center">
								$text.get("stat.AbsentWorkCount")
							</td>
							<td width="60" align="center">
								$text.get("stat.AbsentWorkDay")
							</td>
							<td width="85" align="center">
								$text.get("stat.penalty.absenteeism")
							</td>
							<td width="60" align="center">
								$text.get("stat.LeaveCount")
							</td>
							<td width="60" align="center">
								$text.get("stat.LeaveDay")
							</td>
							<td width="60" align="center">
								$text.get("stat.EvectionCount")
							</td>
							<td width="60" align="center">
								$text.get("stat.EvectionDay")
							</td>
							<td width="80" align="center">
								$text.get("stat.workDate")
							</td>
						</tr>
					</THead>
					<TBody>
						#foreach($!WorkRuleListReportForm in $!WorkRuleListReportForms)
						<tr>
							<td class="listheadonerow" align="center" width="30">
								$!velocityCount
							</td>
							<td  align="center">
							#if($!WorkRuleListReportForm.reportNo != "")
								$!WorkRuleListReportForm.reportNo #else &nbsp; #end
							</td>
							<td align="center">
							#if($!WorkRuleListReportForm.employeeName != "")
								$!WorkRuleListReportForm.employeeName  #else &nbsp; #end
							</td>
							<td align="center">
							#if($!WorkRuleListReportForm.lateCount != "")
								$!WorkRuleListReportForm.lateCount #else &nbsp; #end
							</td>
							<td align="center">
							#if($!WorkRuleListReportForm.lateMinute != "")
								$!WorkRuleListReportForm.lateMinute #else &nbsp; #end
							</td>
							<td align="center">
							#if($!WorkRuleListReportForm.lateAmerce != "")
								$!WorkRuleListReportForm.lateAmerce #else &nbsp; #end
							</td>
							<td align="center">
							#if($!WorkRuleListReportForm.leaveEarlyCount != "")
								$!WorkRuleListReportForm.leaveEarlyCount #else &nbsp; #end
							</td>
							<td align="center">
							#if($!WorkRuleListReportForm.leaveEarlyMinute != "")
								$!WorkRuleListReportForm.leaveEarlyMinute #else &nbsp; #end
							</td>
							<td align="center">
							#if($!WorkRuleListReportForm.leaveEarlyAmerce != "")
								$!WorkRuleListReportForm.leaveEarlyAmerce #else &nbsp; #end
							</td>
							<td align="center">
							#if($!WorkRuleListReportForm.absentWorkCount != "")
								$!WorkRuleListReportForm.absentWorkCount #else &nbsp; #end
							</td>
							<td align="center">
							#if($!WorkRuleListReportForm.absentWorkDay != "")
								$!WorkRuleListReportForm.absentWorkDay #else &nbsp; #end
							</td>
							<td align="center">
							#if($!WorkRuleListReportForm.absentAmerce != "")
								$!WorkRuleListReportForm.absentAmerce #else &nbsp; #end
							</td>
							<td align="center">
							#if($!WorkRuleListReportForm.leaveCount != "")
								$!WorkRuleListReportForm.leaveCount #else &nbsp; #end
							</td>
							<td align="center">
							#if($!WorkRuleListReportForm.leaveDay != "")
								$!WorkRuleListReportForm.leaveDay #else &nbsp; #end
							</td>
							<td align="center">
							#if($!WorkRuleListReportForm.evectionCount != "")
								$!WorkRuleListReportForm.evectionCount #else &nbsp; #end
							</td>
							<td align="center">
							#if($!WorkRuleListReportForm.evectionDay != "")
								$!WorkRuleListReportForm.evectionDay #else &nbsp; #end
							</td>
							<td align="center">
							#if($!WorkRuleListReportForm.workDate != "")
								$!WorkRuleListReportForm.workDate #else &nbsp; #end
							</td>
						</tr>
						#end
					</TBody>
					
				</table>
			</div>
<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-168;
oDiv.style.height=sHeight+"px";
//]]>
</script>
			#if($enable=="true")
		<div class="listRange_pagebar" > 
		$text.get("common.theNo")$!pageNo$text.get("ao.common.page")&nbsp;&nbsp;<a href="javascript:document.WorkRuleListReportForm.submit();" onClick="getFristPage()">$text.get("common.firstPage")</a>&nbsp;&nbsp;#if($!pageNo!=1)<a href="javascript:document.WorkRuleListReportForm.submit();" onClick="prePage()">$text.get("common.prePage")</a>#end&nbsp;&nbsp;
		#if($!pageNo!=$!pageSumList.size())<a href="javascript:document.WorkRuleListReportForm.submit();" onClick="nextPage()">$text.get("common.nextPage")</a>#end&nbsp;&nbsp;&nbsp;
		<select name="pageNo" id="pageNo">
		#foreach($!pageNumber in $!pageSumList)
		#if($!pageNo == $!pageNumber)
			<option value="$!pageNumber" selected="selected">$!pageNumber</option>
		#else
			<option value="$!pageNumber">$!pageNumber</option>
			#end
		#end
		</select>
		<input type="hidden" id="pageSize" name="pageSize" value="$!pageSize">
		<button type="submit" name="ppbutton">go</button>&nbsp; 
		</div>#end
	</form>
	<script type="text/javascript">
		function nextPage(){
			$("pageNo").value = Number($("pageNo").value) + 1;
		}
		
		function prePage(){
			$("pageNo").value = Number($("pageNo").value) - 1 < 1?1:Number($("pageNo").value)-1;
			
		}
		
		function getFristPage(){
			$("pageNo").value = 1;
		}

	</script>

	</body>
</html>
