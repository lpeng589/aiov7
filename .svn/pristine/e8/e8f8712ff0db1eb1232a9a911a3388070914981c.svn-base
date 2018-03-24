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

function fillData(datas){
	newOpenSelectSearch(datas,fieldNames,fieldNIds);
	jQuery.close('Popdiv');
}
</script>
</head>
<body onLoad="showtable('tblSort');showStatus(); "> 
<form  method="post" scope="request" name="form" action="/OAWorkPlanCheckAction.do?opType=check">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
 <input type="hidden" name="winCurIndex" value="$!winCurIndex"> 
 <input name="employeeId" id="employeeId" type="hidden" value="$!OADayWorkPlanCheckSearchForm.employee" >
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("workplan.lb.plancheck")</div>
	<ul class="HeadingButton">
		<li><button name="Submit" type="submit" onClick="forms[0].operation.value=$globals.getOP("OP_QUERY");return beforeBtnQuery();" class="b2">$text.get("common.lb.query")</button></li>
		<li><button name="clear" type="botton" onClick="clearForm(form);" class="b2">$text.get("common.lb.clear")</button></li>
		
	</ul>
</div>
<div id="listRange_id">
	<div >
		<div style="margin-left:30px"><input name="planType" type="radio" value="day" 
		#if("$!OADayWorkPlanCheckSearchForm.planType"=="day"||"$!OADayWorkPlanCheckSearchForm.planType"=="") checked #end>$text.get("workplan.lb.checkdayplan")
		<input name="planType" type="radio" value="week" #if("$!OADayWorkPlanCheckSearchForm.planType"=="week") checked #end>$text.get("workplan.lb.checkweekplan")
		&nbsp;&nbsp;&nbsp;&nbsp;
		<img src="/style/images/np.gif"/>$text.get("workplan.lb.noplan")&nbsp;<img src="/style/images/lp.gif"/>$text.get("workplan.lb.delayplan")&nbsp;
		<img src="/style/images/ns.gif"/>$text.get("workplan.lb.nosummary")&nbsp;<img src="/style/images/ls.gif"/>$text.get("workplan.lb.delaysummary")</div>
		<div style="padding-left:30px;background: #FFFFFF url(/$globals.getStylePath()/images/aiobg.gif) repeat-x left -221px;border:1px solid #A2CEF5;">
			<div></div>			
			<div style="width:400px;padding-top:8px"><span>$text.get("workplan.lb.checkmonth")</span>
			<select name="myear" >
			#foreach($row in $yList)
			<option value="$row" #if("$row"=="$!OADayWorkPlanCheckSearchForm.myear") selected #end>$row$text.get("oa.type.year")</option>
			#end
			</select>
			<select name="month"  style="width:60px;">
			#set($cm = "$!OADayWorkPlanCheckSearchForm.month")
			#if("$!cm" == "") #set($cm = "$curM") #end
			#foreach($row in [1..12])
			<option value="$row" #if("$row"=="$cm" ) selected #end>$row$text.get("oa.type.month")</option>
			#end
			</select>
			</div>
			<div><input name="department" id="department" type="hidden" value="$!OADayWorkPlanCheckSearchForm.department" >
			 
				<span>$text.get("oa.common.department")：</span><input name="departmentName" id="departmentName" type="text" value="$!OADayWorkPlanCheckSearchForm.departmentName" maxlength="50" ondblclick="deptPopForAccount('deptGroup','departmentName','department');" readonly="true"><img style="cursor:pointer;" src="/style1/images/St.gif" onClick="deptPopForAccount('deptGroup','departmentName','department');" >
				<span>$text.get("oa.common.employee")：</span><input name="employee" id="employee" type="text" value="$!OADayWorkPlanCheckSearchForm.employee" maxlength="50" ondblclick="deptPopForAccount('userGroup','employee','employeeId');"  readonly="true"><img style="cursor:pointer;" src="/style1/images/St.gif" onClick="deptPopForAccount('userGroup','employee','employeeId');" >
			</div>
		</div>
	</div>
    <div class="scroll_function_small_a" id="conter">
		<table border="0" cellpadding="0" cellspacing="0" style="width:1650px" class="listRange_list" name="table" id="tblSort">
		<thead>
			<tr>
				<td align ="center"width="80">$text.get("hr.review.employee.name")</td>				
				<td align ="center"width="50">$text.get("workplan.lb.noplan")</td>
				<td align ="center"width="50">$text.get("workplan.lb.delayplan")</td>
				<td align ="center"width="50">$text.get("workplan.lb.nosummary")</td>
				<td align ="center"width="50">$text.get("workplan.lb.delaysummary")</td>	
				#if($!OADayWorkPlanCheckSearchForm.planType=="day") 
				<td align ="center"width="50">$text.get("workplan.lb.compstatus")</td>
				<td align ="center"width="50">$text.get("workplan.lb.timeavg")</td>	
				#end		
				#foreach($row in $monthHead) 
				<td align ="center"width="40"> 
				$weekLang.get($row.week)
				 <br/>
				$row.day
				</td>
				#end
				
			</tr>
		</thead>
		<tbody>
			#foreach ($row in $result )
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				<td align="left"> $globals.get($row,1)&nbsp;</td>
				<td align="left">$globals.get($row,3)&nbsp;</td>
				<td align="left">$globals.get($row,4)&nbsp;</td>
				<td align="left">$globals.get($row,5)&nbsp;</td>
				<td align="left">$globals.get($row,6)&nbsp;</td>
				#if($!OADayWorkPlanCheckSearchForm.planType=="day") 
				<td align="left">$globals.get($row,7)/$globals.get($row,8)&nbsp;</td>
				<td align="left">$globals.get($row,9)&nbsp;</td>
				#end
				#if($!OADayWorkPlanCheckSearchForm.planType=="week") 
					#foreach($row2 in $weekHead) 
					<td align ="center" colspan="$row2.dayNum" 
						style="cursor:hand" onclick="window.open('/OAWorkPlanAction.do?operation=4&userId=$globals.get($row,0)&planType=week&strDate=$!globals.get($row,2).get($row2.beginDate).dayStr')">
					
						#if("$globals.get($row,2).get($row2.beginDate).planStatus" == "0")
						<img src="/style/images/np.gif"/>
						#elseif("$globals.get($row,2).get($row2.beginDate).planStatus" == "2")
						<img src="/style/images/lp.gif"/>
						#end
						
						#if("$globals.get($row,2).get($row2.beginDate).sumStatus" == "0")
							#if("$globals.get($row,2).get($row2.beginDate).planStatus" != "0")
							<img src="/style/images/ns.gif"/>
							#end
						#elseif("$globals.get($row,2).get($row2.beginDate).sumStatus" == "2")
						<img src="/style/images/ls.gif"/>
						#end
					&nbsp;
					</td>
					#end
				#elseif($!OADayWorkPlanCheckSearchForm.planType=="day")
					#foreach($row2 in $monthHead) 
					<td align ="center" valign="bottom" style="cursor:hand" 
					onclick="window.open('/OAWorkPlanAction.do?operation=4&userId=$globals.get($row,0)&planType=day&strDate=$row2.strDay')">
					#if("$row2.isHoliday"=="0")
						#if("$!globals.get($row,2).get($row2.day)" == "")
						<img src="/style/images/np.gif"/><div>&nbsp;</div><div>&nbsp;</div>
						#else
							#if("$!globals.get($row,2).get($row2.day).planStatus"=="2")
							<img src="/style/images/lp.gif"/>
							#end
							#if("$!globals.get($row,2).get($row2.day).sumStatus"=="2")
							<img src="/style/images/ls.gif"/>
							#elseif("$!globals.get($row,2).get($row2.day).sumStatus"=="0")
							<img src="/style/images/ns.gif"/>
							#end
							<div style="width:40px;float:top"  title="$text.get("workplan.lb.plancomp")">$!globals.get($row,2).get($row2.day).planNum/$!globals.get($row,2).get($row2.day).completeNum</div>
							<div style="width:40px;float:top" title="$text.get("workplan.lb.timetotal")">$!globals.get($row,2).get($row2.day).compTimeHour</div>
						#end
					#else
					&nbsp;
					#end
					</td>
					#end
				#end		
			</tr>
			#end
		</tbody>
		</table>
	</div>
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-120;
oDiv.style.height=sHeight+"px";
</script>
		<div class="listRange_pagebar"> $!pageBar </div>
</div>
</form>
</body>
</html>
