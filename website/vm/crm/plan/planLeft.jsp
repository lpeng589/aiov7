<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link  type="text/css" rel="stylesheet" href="/style/css/plan.css" />
<script type="text/javascript" src="/js/webDate.js"></script>

<style>
.Calendar {font-family:Verdana;background:url(/style/images/plan/beijing.jpg);font-size:12px;width:237px;height:230px;line-height:1.5em;float:left;}
.calendar_title{height:40px;padding-left:30px;padding-right:30px;}
.calendar_title{color:#FFFFFF;font-size:11px;} 
.calendar_title div{padding-top:10px;}
.Calendar table{width:100%; border:0;padding-left:3px;}
.Calendar table a{color:#6C7174; font-size:11px;font-family:"黑体";text-decoration:none;font-weight:bold;}
.Calendar table thead{color:#acacac;}
.Calendar table thead td {text-align:center;font-size:10px;color:#8E9396;}
.Calendar table tbody td {font-size: 11px;padding-left:3px;height:31px;width:30px;text-align:center;}
#idCalendarPre{cursor:pointer;float:left;padding-right:5px;}
#idCalendarNext{cursor:pointer;float:right;padding-right:5px;}
#idCalendarPreYear{cursor:pointer;float:left;padding-right:5px;}
#idCalendarNextYear{cursor:pointer;float:right;padding-right:5px;}
.onToday {background:#A4D143;color:#fff;}
#idCalendar td.onSelect {font-weight:bold;}
#plan_l {width: 100%;margin-right:2px; float: left; height: auto; overflow-y:auto; overflow-x:hidden; background:#ECF4FB;border:1px solid #A2CEF5; height: auto;}
#plan_top {
	width:100%;
	height:25px; margin-top:5px;text-align:center;
	color:#0000FF;
}
#plan_r {width: 100%;float: left; height:100%;}
</style>
</head>
<body onload="openDate();">
<div class="framework">
	<div class="TopTitle" style="padding-left:80px;">
		<a href="#" class="data_day_b">日</a>
		<a href="#" class="data_day_a">周</a>
		<a href="#" class="data_day_a">月</a>
	</div>
	<div class="Calendar">
		<div class="calendar_title">
		<div id="idCalendarPre">&lt;&lt;</div>
		<div id="idCalendarNext">&gt;&gt;</div>
		<div style="margin-left:45px;"><span id="idCalendarYear"></span> $text.get("com.date.year") <span id="idCalendarMonth"></span> $text.get("oa.calendar.month")</div>
		</div>
		<table cellspacing="0">
		  <thead>
		    <tr>
		      <td>SUN</td>
		      <td>MON</td>
		      <td>TUE</td>
		      <td>WED</td>
		      <td>THU</td>
		      <td>FRI</td>
		      <td>SAT</td>
		    </tr>
		  </thead>
		  <tbody id="idCalendar">
		  </tbody>
		</table>
	</div>
					
	<div class="LeftBorder">
		<ul class="LeftMenu_list">
			<li><span class="off">未确认提醒</span></li>
			<li><span class="off">已接收提醒</span></li>
			<li><span class="on">已发送提醒</span>
				<ul>
					<li>自定义桌面</li>
					<li>windows快捷组</li>
				</ul>
			</li>
		</ul>
	</div>	
</div>			
</body>
</html>