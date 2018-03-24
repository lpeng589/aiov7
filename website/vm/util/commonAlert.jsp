<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$text.get("workflow.msg.warmsetting")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/common.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js" ></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<style type="text/css">
	body{
		height: 98%;
	}
.asyncbox_btn{
	height: 23px;
}
</style>
<script type="text/javascript">
function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}

function m(value){
	return document.getElementById(value) ;
}
function n(value){
	return document.getElementsByName(value) ;
}
function checkAlertSet(){
	var alertType = n("alertType") ;
	var hasSelect = false ;
	for(var i=0;i<alertType.length;i++){
		if(alertType[i].checked){
			hasSelect = true ;
			break ;
		}
	}
	if(!hasSelect){
		asyncbox.tips('$text.get("workflow.msg.selectOneWarmkind")','alert',1500);
		return false ;
	}
	var alertDate = m("alertDate").value ;
	if(alertDate.length==0){
		asyncbox.tips('$text.get("workflow.msg.nowarmtime")','alert',1500);
		return false ;
	}
	var alertHour = m("alertHour").value ;
	var alertMinute = m("alertMinute").value ;
	if(!compareDate(alertDate,alertHour,alertMinute)){
		return false;
	}
	if(typeof(m("alertContent"))!="undefined" && m("alertContent")!=null){
		var alertContent = m("alertContent").value ;
		if(alertContent.length==0){
			asyncbox.tips('$text.get("common.msg.centerNotNull")','alert',1500);
			return false ;
		}
		if(alertContent.length>200){
			asyncbox.tips('$text.get("common.alert.content")','alert',1500);
			return false;
		}
	}
	
	var popedomUserIds = m("popedomUserIds") ;
	var popedomDeptIds = m("popedomDeptIds") ;

	if(typeof(popedomUserIds)!="undefined" && popedomUserIds!=null 
			&& popedomUserIds.value.length==0 && popedomDeptIds.value.length==0){
		asyncbox.tips('$text.get("common.msg.choiceWarmTo")','alert',1500);
		return false ;
	}
	var loopTime = m("loopTime").value ;
	if(!isInt(loopTime)){
		asyncbox.tips('$text.get("com.alert.times")','alert',1500);
		return false ;
	}
	var hasEndDate = m("endDateId") ;
	if(hasEndDate.checked){
		var endDate = m("endDate").value ;
		if(endDate.length==0){
			asyncbox.tips('$text.get("workflow.msg.noendtime")','alert',1500);
			return false ;
		}
	}
	form.submit() ;
}

function compareDate(startDatetime,hour,minute){
	var sdStr = startDatetime.split("-") ;
	var sd = new Date(sdStr[0],sdStr[1]-1,sdStr[2], hour, minute,"00");
	var diff = sd - new Date() ;
	if (diff < 0){
		asyncbox.tips('$text.get("workflow.msg.warmtimeSystemtime")','alert',1500);
		return false;
	}
	return true;
}

function cancelAlert(){
	form.action="/UtilServlet?operation=deleteAlert";
	form.submit();
	//window.parent.refreshIframe();//keke：这行js一定要删去，不然无法取消提醒



}

function closeDiv(){
	window.parent.jQuery.close('dealdiv');
}

function  totalNum(){
	var o = document.getElementById('alertContent').value;
	var len = getStringLength(o);
	var id = document.getElementById('fontnum');
	var d=getStringLength("$!alert.alertContent");
	if(d>0){
		$(id).html(d);
	}
	$(id).html(len);
	
	if (len == 200) {
		$(id).css("color","#B68101");
	} else if (len > 200) {
		$(id).css("color","red");
	} else {
		$(id).css("color","green");
	}	
}

function fillData(datas){	
	newOpenSelect(datas,fieldNames,fieldNIds,1);
	parent.jQuery.close('Popdiv');
}

</script>
</head>
<body onLoad="showStatus();totalNum();">
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" id="form" name="form" action="/UtilServlet?operation=addAlert" target="formFrame">
<input type="hidden" id="relationId"  name="relationId" value="$!relationId"/>
<input type="hidden" id="typestr" name="typestr" value="$!typestr"/>
<input type="hidden" id="urls" name="urls" value="$!urls"/>
<input type="hidden" id="title" name="title" value="$!title"/>
<input type="hidden" id="falg" name="falg" value="$!falg"></input>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("workflow.msg.warmsetting")</div>
	<ul class="HeadingButton">
		<li style="float:left;display:inline-block;"><a class="asyncbox_btn" href="javascript:void(0)" onclick="checkAlertSet();"><span>$text.get("common.lb.save")</span></a></li>
		#if("$!alert"!="")
		<li style="float:left;display:inline-block;"><a class="asyncbox_btn" href="javascript:void(0)" onclick="cancelAlert();"><span>$text.get("mywork.lb.canelwarm")</span></a></li>
		#end
		<li style="float:left;display:inline-block;margin-right: 24px;"></li>
		<!-- <li><a class="asyncbox_btn" href="javascript:void(0)" onClick="closeDiv();"><span>$text.get("common.lb.close")</span></a></li> -->
	</ul>
</div>
<div id="listRange_id">
	<div id="alertDivId" style="width: 500px;margin-top:5px;margin-left:10px;padding:1px;">
		<table border="0" width="100%" cellpadding="0" cellspacing="0" class="oalistRange_list_function" id="table" name="table">
			<tr>
			  <td align="right" valign="middle" bgcolor="#F5F5F5"><font color="red">*</font>$text.get("workflow.lb.standaloneNote")：</td>
			  <td valign="top" colspan="3">
			  	#foreach($wake in $globals.getEnumerationItems("WakeUpMode"))
				 	#if($alert.alertType.contains($wake.value) || $wake.value=="4")
					<input type="checkbox" id="alertType" name="alertType" checked value="$wake.value"/>$wake.name
					#else
					<input type="checkbox" id="alertType" name="alertType" value="$wake.value"/>$wake.name
					#end
	   			#end	 
	   		  </td>
			</tr>
			<tr>
			  <td align="right" valign="middle"  bgcolor="#F5F5F5"><font color="red">*</font>$text.get("workflow.msg.warmtime")：</td>
			  <td valign="top" colspan="3">
			  	<input id="alertDate" name="alertDate" type="text" class="text2" size="40" readonly="readonly" style="width:80px"  onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onClick="WdatePicker({lang:'$globals.getLocale()'});" value="$!alert.alertDate"> 
			  	<select id="alertHour" name="alertHour">
					#foreach($hour in [0..23])
						#if($hour<10)
							<option value="$hour" #if($hour=="$!alert.alertHour")selected#end>0$hour</option>
						#else
							<option value="$hour" #if($hour=="$!alert.alertHour")selected#end>$hour</option>
						#end
					#end
					</select>&nbsp;&nbsp;$text.get("oa.calendar.hour")
					<select id="alertMinute" name="alertMinute">
						<option value="05" #if("05"=="$!alert.alertMinute")selected#end>05</option>
						<option value="10" #if("10"=="$!alert.alertMinute")selected#end>10</option>
						<option value="15" #if("15"=="$!alert.alertMinute")selected#end>15</option>
						<option value="20" #if("20"=="$!alert.alertMinute")selected#end>20</option>
						<option value="25" #if("25"=="$!alert.alertMinute")selected#end>25</option>
						<option value="30" #if("30"=="$!alert.alertMinute")selected#end>30</option>
						<option value="35" #if("35"=="$!alert.alertMinute")selected#end>35</option>
						<option value="40" #if("40"=="$!alert.alertMinute")selected#end>40</option>
						<option value="45" #if("45"=="$!alert.alertMinute")selected#end>45</option>
						<option value="50" #if("50"=="$!alert.alertMinute")selected#end>50</option>
						<option value="55" #if("55"=="$!alert.alertMinute")selected#end>55</option>
					</select>&nbsp; $text.get("oa.calendar.minutes")
	   		  </td>
			</tr>
			#if("$!falg"==true)
			<tr>
			  <td align="right" valign="top" bgcolor="#F5F5F5"><font color="red">*</font>$text.get("common.msg.center")</td>
		      <td valign="top" colspan="3">
	          <textarea  name="alertContent" id='alertContent' cols="41" rows="3" onKeyPress="totalNum()" onKeyUp="totalNum()">$!alert.alertContent</textarea> 
	          <span id="fontnum" style="color:green">0</span><span>/</span><span id="maxnum" style="color:red">200</span>
	          
	          </td>
		    </tr>
		    
		    <tr>
			  <td align="right" valign="top" bgcolor="#F5F5F5"><font color="red">*</font>$text.get("common.lb.warmto")：</td>
		      <td valign="top" colspan="3">
		      	<input type="hidden"  name="popedomUserIds" id="popedomUserIds" #if($!userId !="") value="$!userId," #else  value="$!alert.popedomUserIds" #end/>
				<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value="$!alert.popedomDeptIds"/>
	          	<div class="oa_signDocument1" id="_context">
				<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds');"  alt="$text.get("oa.common.adviceDept")" class="search">&nbsp;<a style="cursor:pointer" title="$text.get('oa.select.dept')" onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds');">$text.get("oa.select.dept")</a></div>
					<select name="formatDeptName" id="formatDeptName" multiple="multiple">
						#foreach($user in $globals.listDeptNameByDeptId("$!alert.popedomDeptIds"))
						<option value="$user.deptId">$user.departmentName</option>
						#end
					</select>
				</div>
				<div class="oa_signDocument2">
					<img onClick="deleteOpation2('formatDeptName','popedomDeptIds');" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
				</div>
				<div class="oa_signDocument1" id="_context">
				<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="deptPop('userGroup','formatFileName','popedomUserIds');" alt="$text.get("oa.common.advicePerson")" class="search">&nbsp;<a style="cursor:pointer" title="$text.get('oa.select.personal')" onClick="deptPop('userGroup','formatFileName','popedomUserIds');">$text.get("oa.select.personal")</a></div>
					<select name="formatFileName" id="formatFileName" multiple="multiple">
					 	#if("$!userName" != ""){
							<option value="$userId">$userName</option>
						#end
						#foreach($user in $globals.listEmpNameByUserId("$!alert.popedomUserIds"))
						<option value="$user.id">$user.name</option>
						#end
					</select>
				</div>
				<div class="oa_signDocument2">
					<img onClick="deleteOpation2('formatFileName','popedomUserIds');" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
				</div>
			  </td>
		    </tr>
		    #end
		    <tr>
			  <td align="right" valign="top" bgcolor="#F5F5F5">$text.get("workflow.msg.whileset")：</td>
		      <td valign="top" colspan="3">
		      	&nbsp;$text.get("workflow.msg.warmcyclsi")：<input type="radio" id="isLoop" name="isLoop" value="no" #if("$!alert.isLoop"=="no" || "$!alert"=="")checked #end onClick="javascript:m('loopId').style.display='none';">$text.get("common.lb.no") 
		      	<input type="radio" id="isLoop" name="isLoop" value="yes" #if("$!alert.isLoop"=="yes")checked="checked" #end onClick="javascript:m('loopId').style.display='block';">$text.get("common.lb.yes")<br>
		      	<div id="loopId" style="display:#if("$!alert.isLoop"=="yes")block; #else none; #end border:1px solid #A2CEF5;width: 350px;padding-bottom: 5px;">
		      	&nbsp;$text.get("workflow.msg.cycleSet")：<br>
		      	<div style="border:1px solid #A2CEF5;margin-left: 40px;width: 300px;">
		      	<input type="radio" id="loopType" name="loopType" value="day" #if($alert.loopType=="day"  || "$!alert"=="")checked="checked"#end onClick="javascript:m('loopName').innerHTML='天'">$text.get("workflow.lb.byday") <input type="radio" name="loopType" value="week" #if($alert.loopType=="week")checked="checked"#end onClick="javascript:m('loopName').innerHTML='周'">$text.get("workflow.lb.byweek") 
		      	<input type="radio" id="loopType" name="loopType" value="month" #if($alert.loopType=="month")checked="checked"#end onClick="javascript:m('loopName').innerHTML='月'">$text.get("workflow.lb.bymonth") <input type="radio" id="loopType"  name="loopType" value="year" #if($alert.loopType=="year")checked="checked"#end onClick="javascript:m('loopName').innerHTML='年'">$text.get("workflow.lb.byyear")<br>
		      	#if("$!alert.loopTime"=="")#set($loopTime =1)#else#set($loopTime =$!alert.loopTime)#end
		      	&nbsp;$text.get("workflow.msg.partition")<input type="text" id="loopTime" name="loopTime" class="text2" style="width: 30px;text-align: center;" value="$!loopTime"> <label id="loopName">#if($alert.loopType=="day" || "$!alert"=="")$text.get("oa.calendar.day")#elseif($alert.loopType=="week")周 #elseif($alert.loopType=="month")月 #else 年 #end</label>$text.get("workflow.lb.warmonce")
		      	</div>
		      	&nbsp;$text.get("workflow.lb.endtimesetting")：<br>
		      	<div style="border:1px solid #A2CEF5;margin-left: 40px;width: 300px;">
		      	 <input type="radio"  name="hasEndDate" #if("$!alert.endDate"=="")checked="checked"#end value="false"  onClick="javascript:m('endDate').value='';m('endDate').disabled=true;">$text.get("workflow.lb.noendtime")<br>
		      	 <input type="radio"  name="hasEndDate" #if("$!alert.endDate"!="")checked="checked"#end id="endDateId" readonly="readonly" value="true" onClick="javascript:m('endDate').disabled=false;">$text.get("common.lb.endDate")： <input id="endDate" readonly="readonly"  name="endDate" #if("$!alert.endDate"=="")disabled="disabled"#end type="text" class="text2" size="40" style="width:80px"  onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onClick="WdatePicker({lang:'$globals.getLocale()'});" value="$!alert.endDate"> 
		      	</div>
		      	</div>
	          </td>
		    </tr>
	  </table>	
	</div>
</div>
</form>
</body>
</html>