<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.calendar.updateMyCalendar")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/date.vjs"></script>
<script language="javascript" src="/js/formvalidate.vjs"></script>
<script>
function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}
function compareDate(startDatetime,hour,minute){
	var sdStr = startDatetime.split("-") ;
	var sd = new Date(sdStr[0],sdStr[1]-1,sdStr[2], hour, minute,"00");
	var diff = sd - new Date() ;
	if (diff < 0){
		alert("$text.get('com.calendar.diff')");
		return false;
	}
	return true;
}

function checkForm(){
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
	if(!isNull(CalendarTitle,'$text.get("oa.calendar.calendarTitle")'))
	{
		return false;
	}					
	if(getStringLength(CalendarTitle)>40){
		alert('$text.get("oa.calendar.titleLength")') ;
		return false ;
	}
	//判断是否输入合法
	if(!isTitle(CalendarTitle))
	{
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
	return true;
}

function forback()
{
if(confirm("$text.get('oa.common.edit.content')")==true)
{
window.location.href="/OAMyCalendar.do?type=list&keyWord=$globals.encode($!keyWord)&year=$year&month=$month&pageNo=$!pageNo" ;
}
}
function openSelect(selectName,displayName,obj,field){
	displayName=encodeURI(displayName) ;
	var str  = window.showModalDialog("/UserFunctionAction.do?selectName="+selectName+"&operation=22&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,"","dialogWidth=730px;dialogHeight=450px");
	if(typeof(str)!="undefined"){
		if(";;"==str){
			$(obj).value="";
			$(field).value="";
		}
		var mutli=str.split("|"); 
		hid="";
		dis="";
		if(mutli.length>0){
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
</script>
</head>
<body >

<form name="form" method="post" onSubmit="return checkForm();" action="/OAMyCalendar.do" >
<input type="hidden" name="id" value="$!myCalendar.id"/>
<input type="hidden" name="type2" value="1"/>
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")"/>
<input type="hidden" name="createBy" value="$!myCalendar.createBy"/>
<input type="hidden" name="createTime" value="$!myCalendar.createTime"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("oa.calendar.updateMyCalendar")</div>
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
				<td>从:<input name="calendarDate" type="text" class="text" size="24" value="$!myCalendar.calendarDate" readonly="readonly" onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onClick="WdatePicker({lang:'zh_CN'});">
			  到：<input name="endDate" type="text" class="text" size="24" value="$!myCalendar.endDate" onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onClick="WdatePicker({lang:'zh_CN'});" ></td>
	        </tr>
			<tr>
				<td valign="top" class="oabbs_tr">$text.get("oa.calendar.calendarTitle")：</td>
				<td><input name="calendarTitle" type="text" class="text" style="width: 250px;" value="$!myCalendar.calendarTitle"><font color="#FF0000">* </font></td>
	        </tr>
	        <!-- <tr>
				<td valign="top" class="oabbs_tr">$text.get("calendar.lb.relationShiper")：</td>
				<td>
					<input name="assPeople" type="hidden" value="$!myCalendar.assPeople">
					<input name="assPeopleName" id="assPeopleName" value="$!assPeopleName" class="text" style="width: 250px;" onClick="openSelect('MsgReceiveCheckBox','$text.get("calendar.lb.relationShiper")','assPeopleName','assPeople')"/>
					<img id="job_cy" src="/$globals.getStylePath()/images/St.gif" onClick="openSelect('MsgReceiveCheckBox','$text.get("calendar.lb.relationShiper")','assPeopleName','assPeople')" class="search">
				</td>
			</tr>
			<tr>
				<td valign="top" class="oabbs_tr">$text.get("calendar.lb.RelationKeHu")：</td>
				<td>
					<input name="assClient" type="hidden" value="$!myCalendar.assClient">
					<input name="assClientName" id="assClientName" value="$!assClientName" class="text" style="width: 250px;" onClick="openSelect('SelectMeClient2','$text.get("calendar.lb.RelationKeHu")','assClientName','assClient')"/>
					<img id="job_cy" src="/$globals.getStylePath()/images/St.gif" onClick="openSelect('SelectMeClient2','$text.get("calendar.lb.RelationKeHu")','assClientName','assClient')" class="search">
				</td>
			</tr> -->
			<tr>
				<td valign="top" class="oabbs_tr">$text.get("oa.calendar.content")：</td>
				<td><textarea name="calendarContext" cols="50" rows="6">$!myCalendar.calendarContext</textarea><font color="#FF0000">* </font></td>
	        </tr>           
		   </tbody>
		</table>
	</div>
</div>
</form>
</body>
</html>
