<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$text.get("oa.calender.addMyCalender")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
<link rel="stylesheet" href="/style/css/common.css" type="text/css"/>
<link rel="stylesheet" type="text/css" href="/style/themes/default/easyui.css"/>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.easyui.min.js"></script>
<script type="text/javascript">
$(function(){
	$('#uiform input').each(function () { 
    	if ($(this).attr('required') || $(this).attr('validType')) 
		$(this).validatebox(); 
    });
    
    jQuery.extend(jQuery.fn.validatebox.defaults.rules, {
	    getStringLength: {
	        validator: function(value, param){
	            return value.length <= param[0];
	        },
	        message: '$text.get("oa.calendar.Content.Length")'
	    },
	    getTitle: {
	    	validator: function(value, param){
	            return value.length <= param[0];
	        },
	    	message: '$text.get("oa.calendar.titleLength")'
	    }
	 });
});

function checkForms(){
	var flag = true;
    $("input").each(function () {
		if($(this).attr('required') || $(this).attr('validType')) {
		   	if(!$(this).validatebox('isValid')) {
		       flag = false; 
		       return; 
		   	}
	   }
	})
	var alertType = n("alertType") ;
	var hasSelect = false ;
	for(var i=0;i<alertType.length;i++){
		if(alertType[i].checked){
			hasSelect = true ;
			break ;
		}
	}
	if(!hasSelect){
		alert("$text.get('workflow.msg.selectOneWarmkind')") ;
		alertType[0].focus();
		flag = false;
		return false ;
	}
	if(flag){
   		form.submit();
   		//parent.window.location.reload();
   	}
}
</script>
<script type="text/javascript">
function isExist(obj,array){
	var fs=array.split(";");
	for(i=0;i<fs.length;i++){
		if((fs[i]+";")==(obj)){
			return false;
		}
	}
	return true;	
}

function deleteCalendar(){
	if(confirm("$text.get("oa.common.sureDelete")")){
		jQuery.get("/OAMyCalendar.do?operation=3&types=types&id=$!eventId", function(data){
		  if(data=="success"){
		  	window.parent.frames.location.reload();
		  }
		});
	}
}

function m(value){
	return document.getElementById(value) ;
}
function n(value){
	return document.getElementsByName(value) ;
}

function cancelAlert(){
	var s=m("calendars");
	var t=m("warmsetting");
	s.style.display="none";
	t.style.display="block";
}

function backs(){
	var s=m("calendars");
	var t=m("warmsetting");
	s.style.display="block";
	t.style.display="none";
}


function comTimes(){
	var loopTime = m("loopTime").value ;
	if(!isInt(loopTime)){
		alert("$text.get('com.alert.times')") ;
		return false ;
	}
	var hasEndDate = m("endDateId") ;
	if(hasEndDate.checked){
		var endDate = m("endDate2").value ;
		if(endDate.length==0){
			alert("$text.get('workflow.msg.noendtime')") ;
			return false ;
		}
	}
	return true;
}
function compareDate(startDatetime,hour,minute){
	var sdStr = startDatetime.split("-") ;
	var sd = new Date(sdStr[0],sdStr[1]-1,sdStr[2], hour, minute,"00");
	var diff = sd - new Date() ;
	if (diff < 0){
		alert("$text.get('workflow.msg.warmtimeSystemtime')");
		return false;
	}
	return true;
}

function deleteAlert(id){
	var url = "/UtilServlet?operation=deleteAlert&relationId="+id;
	AjaxRequest(url);
	window.location.reload();
}

function kan(type){
	if(document.getElementById("flo").value == 0){
		document.getElementById("flo").value=1;
		document.getElementById("she1").innerHTML="隐藏提示设置";
		document.getElementById("tr_1").style.display="";
		document.getElementById("tr_3").style.display="";
		document.getElementById("tr_4").style.display="";
	}else{
		document.getElementById("flo").value=0;
		document.getElementById("she1").innerHTML="展开提示设置";
		document.getElementById("tr_1").style.display="none";
		document.getElementById("tr_3").style.display="none";
		document.getElementById("tr_4").style.display="none";
	}
}
function reads(){
	if("$!alert"!=""){
		document.getElementById("flo").value=1;
		document.getElementById("she1").innerHTML="隐藏提示设置";
	}
}
</script>
</head>
<body onload="reads()">
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" name="form" action="/OAMyCalendar.do" target="formFrame">
<input type="hidden" id="operation" name="operation" value="$operation"/>
<input type="hidden" name="date" value="$!date"/>
<input type="hidden" name="id" value="$!eventId"/>
<input type="hidden" name="createBy" value="$!calendarInfo.createBy"/>
<input type="hidden" name="createTime" value="$!calendarInfo.createTime"/>
<input type="hidden" name="types" value="types"/>
<input name="flo" id="flo" type="hidden" value="0"/>
<input type="hidden" name="title" value="我的日历"/>
<input type="hidden" name="typestr" value="$text.get("com.oa.calendarWake")$!text.get("oa.calendar.mycalendar")"/>
<input type="hidden" name="urls" value="/OAMyCalendar.do?noback=true&isEspecial=1&operation=5"/>
<input type="hidden" name="falg" value="false"/>
<div id="listRange_id" style="padding-top: 10px;">
	
	<div id="alertDivId" style="width: 600px;margin-top:5px;margin-left:10px;">
		<div>
		#if("$!alert"!="")<a class="asyncbox_btn" style="float: right;" href="javascript:void(0)" onclick="deleteAlert('$!eventId');"><span>$text.get("mywork.lb.canelwarm")</span></a>#end
		#if("$!calendarInfo"!="")<a class="asyncbox_btn" style="float: right;" href="javascript:void(0)" onclick="deleteCalendar()"><span>$text.get("common.lb.print.Delete")</span></a>#end
		<a class="asyncbox_btn" style="float: right;" href="javascript:void(0)" onclick="kan()" ><span id="she1">展开提示设置</span></a></div>
		<table border="0" width="100%" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
			<tr>
			  <td align="right" valign="middle" bgcolor="#F5F5F5">$text.get("oa.calender.Calenderdate")：<font color="red">*</font></td>
			  <td><input name="calendarDate" id="calendarDate" type="text" class="text" size="24" value="#if($operation==1)$!date#else$!calendarInfo.calendarDate#end" onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onClick="WdatePicker({lang:'zh_CN'});" class="easyui-validatebox" required="true"/>
			  至&nbsp;<input name="endDate" id="endDate" type="text" class="easyui-validatebox text" required="true" size="24" value="#if($operation==1)$!date#else$!calendarInfo.endDate#end" onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onClick="WdatePicker({lang:'zh_CN'});" /></td>
			</tr>
		    <tr>
			  <td valign="top" class="oabbs_tr" bgcolor="#F5F5F5" align="right">$text.get("oa.calendar.calendarTitle")：<font color="#FF0000">* </font></td>
			  <td><input name="calendarTitle" id="calendarTitle" type="text" class="text easyui-validatebox" validType="getTitle[40]" required="true" style="width: 250px;" value="$!calendarInfo.calendarTitle" /></td>
		    </tr>
			<tr>
				<td valign="top" class="oabbs_tr" bgcolor="#F5F5F5" align="right">$text.get("oa.calendar.content")：<font color="#FF0000">* </font></td>
				<td><textarea name="calendarContext" id="calendarContext" cols="44" rows="5" class="easyui-validatebox" validType="getStringLength[8000]" required="true">$!calendarInfo.calendarContext</textarea></td>
	        </tr>
	        <tr id="tr_4" #if("$!alert"=="")style="display: none;"#end >
			  <td align="right" valign="middle" bgcolor="#F5F5F5"><font color="red">*</font>$text.get("workflow.lb.standaloneNote")：</td>
			  <td valign="top" colspan="3">
			  	#foreach($wake in $globals.getEnumerationItems("WakeUpMode"))
				 	#if($alert.alertType.contains($wake.value) || $wake.value=="4")
					<input type="checkbox" name="alertType" checked value="$wake.value"/>$wake.name
					#else
					<input type="checkbox" name="alertType" value="$wake.value"/>$wake.name
					#end
	   			#end	 
	   		  </td>
			</tr>
			
	        <tr id="tr_1" #if("$!alert"=="")style="display: none;"#end >
			  <td align="right" valign="middle"  bgcolor="#F5F5F5"><font color="red">*</font>$text.get("workflow.msg.warmtime")：</td>
			  <td valign="top" colspan="3">
			  	<input name="alertDate" id="alertDate" readonly="readonly" type="text" class="easyui-validatebox text2" required="true" size="40" style="width:80px"  onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onClick="WdatePicker({lang:'$globals.getLocale()'});" value="$!alert.alertDate"/> 
			  	<select name="alertHour" id="alertHour">
					#foreach($hour in [0..23])
						#if($hour<10)
							<option value="$hour" #if($hour=="$!alert.alertHour")selected#end>0$hour</option>
						#else
							<option value="$hour" #if($hour=="$!alert.alertHour")selected#end>$hour</option>
						#end
					#end
					</select>&nbsp;&nbsp;$text.get("oa.calendar.hour")
					<select name="alertMinute" id="alertMinute">
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
		    <tr id="tr_3" #if("$!alert"=="")style="display: none;"#end >
			  <td align="right" valign="top" bgcolor="#F5F5F5">$text.get("workflow.msg.whileset")：</td>
		      <td valign="top" colspan="3">
		      	&nbsp;$text.get("workflow.msg.warmcyclsi")：<input type="radio" name="isLoop" value="no" #if("$!alert.isLoop"=="no" || "$!alert"=="")checked #end onClick="javascript:m('loopId').style.display='none';"/>$text.get("common.lb.no") 
		      	<input type="radio" name="isLoop" value="yes" #if("$!alert.isLoop"=="yes")checked="checked" #end onClick="javascript:m('loopId').style.display='block';"/>$text.get("common.lb.yes")<br/>
		      	<div id="loopId" style="display:#if("$!alert.isLoop"=="yes")block; #else none; #end border:1px solid #A2CEF5;width: 350px;padding-bottom: 5px;">
		      	&nbsp;$text.get("workflow.msg.cycleSet")：<br/>
		      	<div style="border:1px solid #A2CEF5;margin-left: 40px;width: 300px;">
		      	<input type="radio" name="loopType" value="day" #if($alert.loopType=="day"  || "$!alert"=="")checked="checked"#end onClick="javascript:m('loopName').innerHTML='天'"/>$text.get("workflow.lb.byday") <input type="radio" name="loopType" value="week" #if($alert.loopType=="week")checked="checked"#end onClick="javascript:m('loopName').innerHTML='周'"/>$text.get("workflow.lb.byweek") 
		      	<input type="radio" name="loopType" value="month" #if($alert.loopType=="month")checked="checked"#end onClick="javascript:m('loopName').innerHTML='月'"/>$text.get("workflow.lb.bymonth") <input type="radio" name="loopType" value="year" #if($alert.loopType=="year")checked="checked"#end onClick="javascript:m('loopName').innerHTML='年'"/>$text.get("workflow.lb.byyear")<br/>
		      	#if("$!alert.loopTime"=="")#set($loopTime =1)#else#set($loopTime =$!alert.loopTime)#end
		      	&nbsp;$text.get("workflow.msg.partition")<input type="text" name="loopTime" id="loopTime" class="text2" style="width: 30px;text-align: center;" value="$!loopTime"/> <label id="loopName">#if($alert.loopType=="day" || "$!alert"=="")$text.get("oa.calendar.day")#elseif($alert.loopType=="week")周 #elseif($alert.loopType=="month")月 #else 年 #end</label>$text.get("workflow.lb.warmonce")
		      	</div>
		      	&nbsp;$text.get("workflow.lb.endtimesetting")：<br/>
		      	<div style="border:1px solid #A2CEF5;margin-left: 40px;width: 300px;">
		      	 <input type="radio" name="hasEndDate" #if("$!alert.endDate"=="")checked="checked"#end value="false"  onClick="javascript:m('endDate2').value='';m('endDate2').disabled=true;"/>$text.get("workflow.lb.noendtime")<br/>
		      	 <input type="radio" name="hasEndDate" #if("$!alert.endDate"!="")checked="checked"#end id="endDateId" value="true" onClick="javascript:m('endDate2').disabled=false;"/>$text.get("common.lb.endDate")： 
		      	 <input id="endDate2" name="endDate2" #if("$!alert.endDate"=="")disabled="disabled"#end type="text" class="text2" size="40" style="width:80px"  onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onClick="WdatePicker({lang:'zh_CN'});" value="$!alert.endDate"/> 
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
