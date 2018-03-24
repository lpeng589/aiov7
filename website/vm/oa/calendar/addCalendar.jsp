<html>
<head>
<meta name="renderer" content="webkit">	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>$text.get("oa.calender.addMyCalender")</title>
	<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/formvalidate.vjs"></script>
<script>
function backMyCalendar(){
	document.location="/OAMyCalendar.do?type=MyCalender&calendar_time=$calendar_time";
}

function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}

function checkForm()
{
	var CalendarTitle = form.calendarTitle.value;
	var CalendarContext = form.calendarContext.value;
	var endDate = form.endDate.value;
	var calendarDate = form.calendarDate.value;
	
	if(!isNull(calendarDate,'$text.get("oa.calender.Calenderdate")')){
		return false;
	}
	if(!isNull(endDate,'$text.get("oa.calender.Calenderdate")')){
		return false;
	}
	if(calendarDate > endDate){
		alert('$text.get("oa.msg.endDate.afterStartDate")');
		return false;
	}
	if(!isNull(CalendarTitle,'$text.get("oa.calendar.calendarTitle")')){
		return false;
	}
						
	if(getStringLength(CalendarTitle)>40){
		alert('$text.get("oa.calendar.titleLength")') ;
		return false ;
	}
	//判断是否输入合法
	if(!isTitle(CalendarTitle)){
		alert("$text.get('oa.calendar.calendarTitle')"+'$text.get("common.validate.error.any")');
	    return false;
	}
	if(!isNull(CalendarContext,'$text.get("oa.calendar.Calendercontent")')){
		return false;
	}
	if(getStringLength(CalendarContext)>8000){
		alert('$text.get("oa.calendar.Content.Length")') ;
		return false ;
	}
	/*
	if(!checkAlertSet()){
		return false ;
	}*/
	return true;	
}
/*
function checkAlertSet(){
	var	alertDiv = m("alertDivId").style.display ;
	if("none"==alertDiv){return true ;}
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
		return false ;
	}
	var alertDate = m("alertDate").value ;
	if(alertDate.length==0){
		alert("$text.get('workflow.msg.nowarmtime')") ;
		return false ;
	}
	var alertHour = m("alertHour").value ;
	var alertMinute = m("alertMinute").value ;
	if(!compareDate(alertDate,alertHour,alertMinute)){
		return false;
	}
	var loopTime = m("loopTime").value ;
	if(!isInt(loopTime)){
		alert("$text.get('com.alert.times')") ;
		return false ;
	}
	var hasEndDate = m("endDateId") ;
	if(hasEndDate.checked){
		var endDate = m("endDate").value ;
		if(endDate.length==0){
			alert("$text.get('workflow.msg.noendtime')") ;
			return false ;
		}
	}
	return true ;
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
*/
function isNull(variable,message)
{
	if(variable=="" || variable==null)
	{
		alert(message+" "+"$text.get('common.validate.error.null')");
		return false;
	}else
	{
		return true;
	}
			
}

function openSelect(selectName,displayName,obj,field){
	
	displayName=encodeURI(displayName) ;
	var str  = window.showModalDialog("/UserFunctionAction.do?selectName="+selectName+"&operation=22&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,"","dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)!="undefined"){
		if(";;;"==str){
			$(obj).value="";
			$(field).value="";
		}
		var mutli=str.split("|"); 
		hid="";
		dis="";
		if(str.length>0){
			var len=mutli.length;
			if(len>1){len=len-1}
			for(j=0;j<len;j++){ 
				fs=mutli[j].split(";");
					dis=fs[1]+";";
					hid=fs[0]+";";
				if(hid.indexOf("@Sess:")>=0){
					$(obj).value="";
					$(field).value="";
				}else{
				   if(isExist(dis,$(obj).value)==true){
					   $(obj).value=$(obj).value+dis;
					   $(field).value=$(field).value+hid;
				   }
				}
			}
		}
	}
}
function isExist(obj,array){
	var fs=array.split(";");
	for(i=0;i<fs.length;i++){
		if((fs[i]+";")==(obj)){
			return false;
		}
	}
	return true;	
}
function $(obj){
	return document.getElementById(obj) ;
}
function m(value){
	return document.getElementById(value) ;
}
function n(value){
	return document.getElementsByName(value) ;
}
</script>
</head>
<body>
<form name="form" method="post" onSubmit="return checkForm();" action="/OAMyCalendar.do">
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")"/>
<input type="hidden" name="type2" value="$type2"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("oa.common.add")$text.get("oa.calendar")</div>
	<ul class="HeadingButton">
		<li>
			<button type="submit" name="Submit" class="b2">$text.get("common.lb.save")</button>
			<button type="reset" class="b2">$text.get("common.lb.reset")</button>
			<button type="button" onClick="history.go(-1)" class="b2">$text.get("common.lb.back")</button>
			
		</li>	
	</ul>
</div>
<div id="listRange_id" align="center">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
    <div class="oalistRange_scroll_1">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
			<thead>
			<tr>
				<td colspan="2" class="oabbs_tc">&nbsp;</td>
			</tr>
			</thead>
			<tbody>
			<tr>
				<td valign="top" class="oabbs_tr">$text.get("oa.calender.Calenderdate")：</td>
				<td>从:<input name="calendarDate" type="text" class="text" size="24" value="$!calendar_time" onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onClick="WdatePicker({lang:'zh_CN'});">
			  到：<input name="endDate" type="text" class="text" size="24" value="$!calendar_time" onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onClick="WdatePicker({lang:'zh_CN'});" ></td>
	        </tr>
			<tr>
				<td valign="top" class="oabbs_tr">$text.get("oa.calendar.calendarTitle")：</td>
				<td><input name="calendarTitle" type="text" class="text" style="width: 250px;" ><font color="#FF0000">* </font></td>
	        </tr>
	       <!-- <tr>
				<td valign="top" class="oabbs_tr">$text.get("calendar.lb.relationShiper")：</td>
				<td>
					<input name="assPeople" type="hidden" value="">
					<input name="assPeopleName" id="assPeopleName"  class="text" style="width: 250px;" onClick="openSelect('MsgReceiveCheckBox','$text.get("calendar.lb.relationShiper")','assPeopleName','assPeople')"/>
					<img id="job_cy" src="/$globals.getStylePath()/images/St.gif" onClick="openSelect('MsgReceiveCheckBox','$text.get("calendar.lb.relationShiper")','assPeopleName','assPeople')" class="search">
				</td>
			</tr>
			<tr>
				<td valign="top" class="oabbs_tr">$text.get("calendar.lb.RelationKeHu")：</td>
				<td>
					<input name="assClient" type="hidden" value="">
					<input name="assClientName" id="assClientName"  class="text" style="width: 250px;" onClick="openSelect('SelectMeClient2','$text.get("calendar.lb.RelationKeHu")','assClientName','assClient')"/>
					<img id="job_cy" src="/$globals.getStylePath()/images/St.gif" onClick="openSelect('SelectMeClient2','$text.get("calendar.lb.RelationKeHu")','assClientName','assClient')" class="search">
				</td>
			</tr> -->
			<tr>
				<td valign="top" class="oabbs_tr">$text.get("oa.calendar.content")：</td>
				<td><textarea name="calendarContext" cols="50" rows="6"></textarea><font color="#FF0000">* </font></td>
	        </tr>
		   </tbody>
		</table>
	</div>
</div>
</form>
</body>
</html>