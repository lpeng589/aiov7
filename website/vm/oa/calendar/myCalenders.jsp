<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.calendar.mycalendar")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script>
	function addCalendar(){
		document.location='/OAMyCalendar.do?operation=$globals.getOP("OP_ADD_PREPARE")&calendar_time=$calendar_time';
	}
	function del(id){
		document.location="/OAMyCalendar.do?operation=$globals.getOP("OP_DELETE")&id="+id+"&calendar_time=$calendar_time";
	}
	function upd(id){
		document.location="/OAMyCalendar.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&id="+id;
	}	
	function goCalendarList(today){
		var year = today.substr(0,4);
		var month = today.substr(5,2);
		window.parent.document.location="/OAMyCalendar.do?type=list&year="+year+"&month="+month;
	}
</script>
</head>
<body>
<div class="oa_Calendarframeirht">
<div class="oa_calendarright">
<ul>
<li>$text.get("data.lb.today")：</li>
<li>$text.get("oa.calendar.gregorian")：$calendar_time $text.get("oa.calendar.luner")：$LunarCalendar</li>
<li>$text.get("oa.calender.choiceDate")：</li>
<li>$text.get("oa.calendar.gregorian")：$calendar_time $text.get("oa.calendar.luner")：$LunarCalendar</li>
</ul>
</div>
<div class="oa_calendarright">
<ul>
<li><span><button type="button" onClick="goCalendarList('$calendar_time')" class="b4">$text.get("oa.calender.monthCalender")</button><button type="button" onClick="addCalendar()" class="b2">$text.get("common.lb.add")</button></span></li>
</ul>
</div>
#if($list.size()>0)
#foreach($v in $list)
<div class="oa_calendarright">
<ul>
<li>
$text.get("message.lb.title")：$v.CalendarTitle
</li>
<li>
$text.get("sms.note.time")：$v.CalendarDate &nbsp;&nbsp;&nbsp;
</li>
<li>
$text.get("oa.calendar.calendarWakeUpTime")：$v.NowWakeUpBeginDate
</li>
<li class="oa_calendaratr">
$v.CalendarContext
</li>
<li><span><a href="javascript:upd('$v.id')">$text.get("oa.common.upd")</a>
  <a href="javascript:del('$v.id')">$text.get("common.lb.del")</a></span></li>
</ul>
</div>
#end
#else
<div class="oa_calendarright" style="text-align:center">
$text.get("oa.calender.noCalenderNote")
</div>
#end
</div>
</body>
</html>
