
<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>345</title>
		<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
	</head>
	<script language="javascript" src="/js/date.vjs"></script>
	<script type="text/javascript">
	
  function openSelect(urlstr,obj,field){ 
	
	var str  = window.showModalDialog(urlstr+"&MOID=0809fc77_0909100911555462068&MOOP=add","","dialogWidth=730px;dialogHeight=450px"); 

	fs=str.split(";");   	
	$("employeeNo").value = fs[0];
	$("tblEmployee_empFullName").value = fs[1];
	getFristPage();
}	

function openInputDate(obj)
{
	c.showMoreDay = false;
	c.show(obj);
}

function $(obj){
	return document.getElementById(obj);
}

function formatTime(time1,time2){
	var times1 = time1.split("-");
	var times2 = time2.split("-");
	var date1 = new Date(times1[0],times1[1],times1[2]);
	var date2 = new Date(times2[0],times2[1],times2[2]);
	
	if(date1>=date2){
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
	if ($("endDate").value == ""){
		alert("$text.get('oa.msg.endDateNotNull')");
		return false;
	}
	if(!formatTime($("startDate").value,$("endDate").value)){
		return false;
	}
	
	return true;
}
function empIdClear(){
	if($("tblEmployee_empFullName").value == ""){
		$("employeeNo").value = "";
	}
}
  </script>

	<body>
		<form action="/WorkRuleReportAction.do?type=2" name="statReport"
			method="post">
			<div class="Heading">
				<div class="HeadingIcon">
					<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
				</div>
				<div class="HeadingTitle">
					$text.get("stat.sum.report")

				</div>
				<ul style="float: right;">
					<li>
						<button type="button"
							onClick='window.showModalDialog("/UserFunctionQueryAction.do?tableName=HRPay&reportNumber=HRPay&operation=18&parentTableName=HRPayFrame&winCurIndex=1","","dialogWidth=208px;dialogHeight=285px,scroll=no;")'>
							<img src="/$globals.getStylePath()/images/print.gif">
							$text.get("common.lb.print")
						</button>
					</li>
				</ul>
			</div>
			<input type="hidden" name="operation" id="operation" value="1">
			<div id="listRange_id">
				<div class="scroll_function_big">
					<ul class="listRange_1">
						<li>
							<span>$text.get("stat.empFullName")：</span>
							<input name="employeeNo" id="employeeNo" type="hidden"  value="$!employeeNo" />
							<input name="tblEmployee_empFullName"
								id="tblEmployee_empFullName"
								onKeyDown="if(event.keyCode==13&&this.value.length>0) {event.keyCode=9;mainSelect('/UtilServlet?operation=Ajax&tableName=tblWorkRuleListReport&fieldName=employeeNo' ,'/UserFunctionAction.do?operation=22&tableName=tblWorkRuleListReport&selectName=employeeLR' ,this,'employeeNo')}"
								type="text"
								onDblClick="openSelect('/UserFunctionAction.do?tableName=tblWorkRuleListReport&&selectName=employeeLR&operation=22','tblEmployee_empFullName','employeeNo')"
								value="$!tblEmployee_empFullName" size="20" onChange="empIdClear()"
								onKeyPress="if(event.keyCode ==13){ openSelect('/UserFunctionAction.do?tableName=tblWorkRuleListReport&selectName=employeeLR&operation=22','tblEmployee_empFullName','employeeNo')}" /><img src="/$globals.getStylePath()/images/St.gif"
								onClick="openSelect('/UserFunctionAction.do?tableName=tblWorkRuleListReport&selectName=employeeLR&operation=22','tblEmployee_empFullName','employeeNo')">
						</li>
						<li>
							<span>$text.get("stat.sum.start.date")：</span><font color="red">*</font>
							<input type="text" name="startDate" id="startDate"
								onclick="openInputDate(this)" onChange="getFristPage()" value="$!startDate"/>
						</li>
						<li>
							<span>$text.get("stat.sum.end.date")：</span><font color="red">*</font>
							<input name="endDate" id="endDate" type="text"
								onclick="openInputDate(this)"  onchange="getFristPage()" value="$!endDate"/>
						</li>
					</ul>
					<ul style="float: right;">
						<li>
							<button style="width: 120px;" type="submit" title="$text.get('common.hr.sum.reportInfo')"
								onclick="return statVal()">
								$text.get("stat.sum.reportDet")$text.get("stat.list")
							</button>
						</li>
					</ul>
				</div>
		<div class="scroll_function_small_a" id="conter">
		<table border="0" cellpadding="0" cellspacing="0"
			class="listRange_list_function" id="tblSort" >
			<THead>
				<tr class="listhead">
					<td class="listheade" width="30" align="center">
						<span style="vertical-align:middle;"><IMG
								src="/$globals.getStylePath()/images/down.jpg" border=0 /> </span>
					</td>
					<td width="80" align="center">
						$text.get("stat.empFullName")
					</td>
					<td width="80" align="center">
						$text.get("stat.sum.report.late")
					</td>
					<td width="120" align="center">
						$text.get("stat.sum.report.lateMinute")
					</td>
					<td width="120" align="center">
						$text.get("stat.penalty.sum.late")
					</td>
					<td width="80" align="center">
						$text.get("stat.sum.report.leaveEarly")
					</td>
					<td width="120" align="center">
						$text.get("stat.sum.report.leaveEarlyMinute")
					</td>
					<td width="120" align="center">
						$text.get("stat.penalty.sum.leaveEarly")
					</td>
					<td width="80" align="center">
						$text.get("stat.sum.report.absent")
					</td>
					<td width="100" align="center">
						$text.get("stat.sum.report.absent.day")
					</td>
					<td width="100" align="center">
						$text.get("stat.penalty.sum.absenteeism")
					</td>
					<td width="80" align="center">
						$text.get("stat.sum.report.leave")
					</td>
					<td width="100" align="center">
						$text.get("stat.sum.report.leaveDay")
					</td>
					<td width="80" align="center">
						$text.get("stat.sum.report.evection")
					</td>
					<td width="100" align="center">
						$text.get("stat.sum.report.evectionDay")
					</td>
				</tr>
			</THead>
			<TBody>
				#foreach($!statReport in $!stats)
				<tr>
					<td class="listheadonerow" align="center" width="30">
						$!velocityCount
					</td>
					<td align="center">
					#if($!statReport.empFullName != "")
						$!statReport.empFullName #else &nbsp; #end
					</td>
					<td align="center">
					#if($!statReport.lateSum != "")
						$!statReport.lateSum #else &nbsp; #end
					</td>
					<td align="center">
					#if($!statReport.lateMinuteSum != "")
						$!statReport.lateMinuteSum #else &nbsp; #end
					</td>
					<td align="center">
					#if($!statReport.lateAmerce != "")
						$!statReport.lateAmerce #else &nbsp; #end
					</td>
					<td align="center">
					#if($!statReport.leaveEarlySum != "")
						$!statReport.leaveEarlySum #else &nbsp; #end
					</td>
					<td align="center">
					#if($!statReport.leaveEarlyMinuteSum != "")
						$!statReport.leaveEarlyMinuteSum #else &nbsp; #end
					</td>
					<td align="center">
					#if($!statReport.leaveEarlyAmerce != "")
						$!statReport.leaveEarlyAmerce #else &nbsp; #end
					</td>
					<td align="center">
					#if($!statReport.absentWorkSum != "")
						$!statReport.absentWorkSum #else &nbsp; #end
					</td>
					<td align="center">
					#if($!statReport.absentWorkDaySum != "")
						$!statReport.absentWorkDaySum #else &nbsp; #end
					</td>
					<td align="center">
					#if($!statReport.absentAmerce != "")
						$!statReport.absentAmerce #else &nbsp; #end
					</td>
					<td align="center">
					#if($!statReport.leaveSum != "")
						$!statReport.leaveSum #else &nbsp; #end
					</td>
					<td align="center">
					#if($!statReport.leaveDaySum != "")
						$!statReport.leaveDaySum #else &nbsp; #end
					</td>
					<td align="center">
					#if($!statReport.evectionSum != "")
						$!statReport.evectionSum #else &nbsp; #end
					</td>
					<td align="center">
					#if($!statReport.evectionDaySum != "")
						$!statReport.evectionDaySum #else &nbsp; #end
					</td>
				</tr>
				#end
			</TBody>
		</table>
		</div>
		
				<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-130;
oDiv.style.height=sHeight+"px";
//]]>
</script>
#if($!enable == "true")
<div class="listRange_pagebar" style="position:relative"> 
		$text.get("common.theNo")$!pageNo$text.get("ao.common.page")&nbsp;&nbsp;<a href="javascript:document.statReport.submit();" onClick="getFristPage()">$text.get("ao.common.page")</a>&nbsp;&nbsp;#if($!pageNo!=1)<a href="javascript:document.statReport.submit();" onClick="prePage()">$text.get("common.prePage")</a>#end&nbsp;&nbsp;
		#if($!pageNo!=$!pageSumList.size())<a href="javascript:document.statReport.submit();" onClick="nextPage()">$text.get("common.nextPage")</a>#end&nbsp;&nbsp;&nbsp;
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
	</body>
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
</html>
