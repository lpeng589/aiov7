
<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>$text.get("dutytime.config") </title>
		<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
	</head>

	<script type="text/javascript">

  function openSelect(urlstr,obj,field){ 
	
	var str  = window.showModalDialog(urlstr+"&MOID=0809fc77_0909100911555462068&MOOP=add","","dialogWidth=730px;dialogHeight=450px"); 

	fs=str.split(";");   	
	$("squadEnactmentNo").value = fs[0];
	getFristPage();
}

function $(obj){
	return document.getElementById(obj);
}

function stat(){
	window.location.href = "/vm/checkOnWorkAttendance/squadAdd.jsp";
}

  </script>

	<body>
		<form action="/DutyPeriodsAction.do?type=1" name="listReport"
			method="post">
			<div class="Heading">
				<div class="HeadingIcon">
					<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
				</div>
				<div class="HeadingTitle">
					$text.get("dutytime.config")
				</div>
				<ul class="HeadingButton">
					<li>
						<button name="add" type="button" onClick="stat()" class="b2">$text.get("common.lb.add")
						</button>
					</li>
				</ul>
			</div>

			<div id="listRange_id">
					<div class="listRange_1">
						<li>
							<span>$text.get("squad.squadEnactmentName")：</span>
							<input type="text" name="squadEnactmentName"
								id="squadEnactmentName" onChange="getFristPage()"
								value="$!squadEnactmentName" />
						</li>
						<li>
							<span>$text.get("com.checkOnWorkAttendance.squadNo")：</span>
							<input name="squadEnactmentNo" id="squadEnactmentNo"
								onKeyDown="if(event.keyCode==13&&this.value.length>0) {event.keyCode=9;mainSelect('/UtilServlet?operation=Ajax&tableName=tblDutyPeriods&selectName=squadEnactmentPeriods' ,'/UserFunctionAction.do?operation=22&tableName=tblDutyPeriods&selectName=squadEnactmentPeriods' ,this,'squadEnactmentNo')}"
								type="text"
								onDblClick="openSelect('/UserFunctionAction.do?tableName=tblDutyPeriods&selectName=squadEnactmentPeriods&operation=22',this,'squadEnactmentNo')"
								value="$!squadEnactmentNo" size="20"
								onKeyPress="if(event.keyCode ==13){ openSelect('/UserFunctionAction.do?tableName=tblDutyPeriods&selectName=squadEnactmentPeriods&operation=22',this,'squadEnactmentNo')}" 
								/><img src="/$globals.getStylePath()/images/St.gif"
								onClick="openSelect('/UserFunctionAction.do?tableName=tblDutyPeriods&selectName=squadEnactmentPeriods&operation=22',this,'squadEnactmentNo')">
						</li>
					</div>
					<div class="scroll_function_big">
							<button name="search" id="search" type="submit" class="b2">
								$text.get("common.lb.query")
							</button>
						</div>
				</div>
<div class="scroll_function_small_a" id="conter">
				<table border="0" cellpadding="0" cellspacing="0"
					class="listRange_list_function" id="tblSort" width="880">
					<THead>
						<tr class="listhead">
							<td class="listheade" width="30" align="center">
								<span style="vertical-align:middle;"><IMG
										src="/$globals.getStylePath()/images/down.jpg" border=0 /> </span>
							</td>
							<td width="100" align="center">
								$text.get("com.checkOnWorkAttendance.squadNo")
							</td>
							<td width="100" align="center">
								$text.get("squad.squadEnactmentName")
							</td>
							<td width="100" align="center">
								$text.get("common.msg.dutyTime")
							</td>
							<td width="100" align="center">
								$text.get("common.msg.afterDutyTime")
							</td>
							<td width="150" align="center">
								$text.get("squadSect.onDutyAvailabilityTime")
							</td>
							<td width="150" align="center">
								$text.get("squadSect.offDutyAvailabilityTime")
							</td>
							<td width="100" align="center">
								$text.get("squadSect.squadSectType")
							</td>
						</tr>
					</THead>
					<TBody>
						#foreach($!squadInfo in $!squads)
						<tr>
							<td class="listheadonerow" align="center" width="30">
								$velocityCount
							</td>
							<td width="100" align="center">
								#if($!squadInfo.squadEnactmentNo != "")
								$!squadInfo.squadEnactmentNo #else &nbsp; #end
							</td>
							<td width="100" align="center">
								#if($!squadInfo.squadEnactmentName != "")
								$!squadInfo.squadEnactmentName #else &nbsp; #end
							</td>
							<td width="100" align="center">
							#if($!squadInfo.sect.onDutyTime != "")
								$!squadInfo.sect.onDutyTime #else &nbsp; #end
							</td>
							<td width="100" align="center">
							#if($!squadInfo.sect.offDutyTime != "")
								$!squadInfo.sect.offDutyTime #else &nbsp; #end
							</td>
							<td width="150" align="center">
							#if($!squadInfo.sect.onDutyAvailabilityTime != "")
								$!squadInfo.sect.onDutyAvailabilityTime #else &nbsp; #end
							</td>
							<td width="150" align="center">
							#if($!squadInfo.sect.offDutyAvailabilityTime != "")
								$!squadInfo.sect.offDutyAvailabilityTime #else &nbsp; #end
							</td>
							<td width="100" align="center">
								#foreach($!erow in $globals.getEnumerationItems("squadSectType"))
								#if($!erow.value == $!squadInfo.sect.squadSectType) $!erow.name
								#end #end
							</td>
						</tr>
						#end
					</TBody>
				</table>
				</div>
				<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-140;
oDiv.style.height=sHeight+"px";
</script>
				#if($!enable == "true")
				<div class="listRange_pagebar" style="position:relative">
					$text.get("common.theNo")$!pageNo$text.get("ao.common.page")&nbsp;&nbsp;
					<a href="javascript:document.listReport.submit();"
						onclick="getFristPage()">$text.get("common.firstPage")</a>&nbsp;&nbsp;#if($pageNo!=1)
					<a href="javascript:document.listReport.submit();"
						onclick="prePage()">$text.get("common.prePage")</a>#end&nbsp;&nbsp;
					#if($!pageNo!=$!pageSumList.size())
					<a href="javascript:document.listReport.submit();"
						onclick="nextPage()">$text.get("common.nextPage")</a>#end&nbsp;&nbsp;&nbsp;
					<select name="pageNo" id="pageNo">
						#foreach($!pageNumber in $!pageSumList) #if($!pageNo == $!pageNumber)
						<option value="$pageNumber" selected="selected">
							$!pageNumber
						</option>
						#else
						<option value="$pageNumber">
							$!pageNumber
						</option>
						#end #end
					</select>
					<input type="hidden" id="pageSize" name="pageSize"
						value="$!pageSize">
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
			alert($("pageNo").value);
		}
		
		function prePage(){
			$("pageNo").value = Number($("pageNo").value) - 1 < 1?1:Number($("pageNo").value)-1;
			
		}
		
		function getFristPage(){
			$("pageNo").value = 1;
			
		}
	</script>
</html>
