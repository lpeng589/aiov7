<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.mydesk.workPlan")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link rel="StyleSheet" href="/$globals.getStylePath()/css/dtree.css" type="text/css" />
<script type="text/javascript" src="/js/dtree.vjs"></script>
<script type="text/javascript" src="/js/webDate.js"></script>
<script type="text/javascript" src="/js/function.js"></script> 
<style>
.plan_date {
border-top:1px solid #ccc;
border-bottom: 1px solid #ccc;margin-bottom: 10px;}
.Calendar {font-family:Verdana;font-size:12px;background-color:#ECF4FB;text-align:center;width:100%;height:160px;padding:10px;line-height:1.5em;}
.Calendar a{color:#1e5494; }
.Calendar table{width:100%; border:0;}
.Calendar table thead{color:#acacac;}
.Calendar table td {font-size: 11px;padding:1px;}
#idCalendarPre{cursor:pointer;float:left;padding-right:5px;}
#idCalendarNext{cursor:pointer;float:right;padding-right:5px;}
#idCalendarPreYear{cursor:pointer;float:left;padding-right:5px;}
#idCalendarNextYear{cursor:pointer;float:right;padding-right:5px;}
#idCalendar td.onToday {font-weight:bold;color:#C60; }
#idCalendar td.onSelect {font-weight:bold;}
#plan_l {width: 100%;margin-right:2px; float: left; height: auto; overflow-y:auto; overflow-x:hidden; background:#ECF4FB;border:1px solid #A2CEF5; height: auto;}
#plan_top {
	width:100%;
	height:25px; margin-top:5px;text-align:center;
	color:#0000FF;
}
#plan_r {width: 100%;float: left; height:100%;}

</style>
<script type="text/javascript">
var curUserId = "" ;
var curPlanType = "week" ;
var curDate = "" ;
function goListPlan(){
	var listPlan = window.parent.frames['mainFrame'] ;
	curUserId = "" ;
	curPlanType = "day" ;
	curDate = "" ;
	listPlan.src = "/OAWorkPlanAction.do?operation=4&userId="+curUserId+"&planType="+curPlanType+"&strDate="+curDate ;
}

function isQuery(){
	var isQueryList = window.parent.frames['mainFrame'].document.getElementById("isQueryList") ;
	if(isQueryList==null || typeof(isQueryList)=="undefined"){
		goListPlan() ;
		return false;
	}
	return true ;
}

function clickType(type){
	curPlanType = type;
	window.parent.frames['mainFrame'].location= "/OAWorkPlanAction.do?operation=4&userId="+curUserId+"&planType="+curPlanType+"&strDate="+curDate ;
}
function clickTree(userId){
	AjaxRequest('/UtilServlet?operation=hasVisitWorkPlan&userId='+userId);
	if(response!="no response text"&&response!="close"){	
		if(response=="false"){
			alert('$text.get("oa.workplan.right.error")');
			return false ;
		}
	}
	curUserId =userId;
	window.parent.frames['mainFrame'].location= "/OAWorkPlanAction.do?operation=4&userId="+curUserId+"&planType="+curPlanType+"&strDate="+curDate ;
}
function clickDate(planDate){
	curDate = planDate;
	window.parent.frames['mainFrame'].location= "/OAWorkPlanAction.do?operation=4&userId="+curUserId+"&planType="+curPlanType+"&strDate="+curDate ;
}
</script>
</head>
<body onLoad="goListPlan();openDate();">
 <div class="aoHeadingFrametop">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
</div>
	 	 <div id="plan_l">
		 	<script type="text/javascript">
var oDiv=document.getElementById("plan_l");
var sHeight=document.body.clientHeight-38;
oDiv.style.height=sHeight+"px";
</script>
		 	<div id="plan_top">
		 	<input name="planType" id="planTypeDay"  type="radio" style="border:0px; width:15px;"  value="day" checked="checked" onClick="clickType('day')"><label for="planTypeDay">$text.get("oa.type.day")</label>
			<input name="planType" id="planTypeWeek"  type="radio" style="border:0px; width:15px;"  value="week" onClick="clickType('week')"><label for="planTypeWeek">$text.get("oa.type.week")</label>
			<input name="planType" id="planTypeMonth"  type="radio" style="border:0px; width:15px;"  value="month" onClick="clickType('month')"><label for="planTypeMonth">$text.get("oa.type.month")</label>
			<input name="planType" id="planTypeSeason"  type="radio" style="border:0px; width:15px;"  value="season" onClick="clickType('season')"><label for="planTypeSeason">$text.get("oa.type.season")</label>
			<input name="planType" id="planTypeYear" type="radio" style="border:0px; width:15px;"  value="year" onClick="clickType('year')"><label for="planTypeYear">$text.get("oa.type.year")</label></div>
		<div class="plan_date">
		<div class="Calendar">
		  <div id="idCalendarPre">&lt;&lt;</div>
		  <div id="idCalendarNext">&gt;&gt;</div>
		  <span id="idCalendarYear"></span>$text.get("com.date.year") <span id="idCalendarMonth"></span>$text.get("oa.calendar.month")
		  <table cellspacing="0">
		    <thead>
		      <tr>
		        <td>$text.get("data.lb.w0")</td>
		        <td>$text.get("data.lb.w1")</td>
		        <td>$text.get("data.lb.w2")</td>
		        <td>$text.get("data.lb.w3")</td>
		        <td>$text.get("data.lb.w4")</td>
		        <td>$text.get("data.lb.w5")</td>
		        <td>$text.get("data.lb.w6")</td>
		      </tr>
		    </thead>
		    <tbody id="idCalendar">
		    </tbody>
		  </table>
		  <div id="idCalendarPreYear">&lt;&lt;</div>
		  <div id="idCalendarNextYear">&gt;&gt;</div>
		 </div>
		</div>
		<div class="plan_tree"> 
		<a style="padding:5px 0px 5px 20px" href="javascript:clickTree('$LoginBean.id');">$text.get("workplan.lb.myplan")</a>
	<script type="text/javascript">
	
		d = new dTree('d');
		
		d.add(0,-1,'$text.get("oa.all.employee")');
		$!treeEployee
		document.write(d);
	
	</script>
		</div>
</div>

</body>
</html>
