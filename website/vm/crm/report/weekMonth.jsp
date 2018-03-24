<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/webDateByCrmReport.js"></script>
<style type="text/css">
.left{width: 400px;float: left;}
.main{float: left;}

/*日历*/
.Calendar {font-family:Verdana;background:url(/style/images/plan/beijing.gif);font-size:12px;
width:370px;height:230px;line-height:1.5em;float:left;}
.calendar_title{height:40px;padding-left:30px;padding-right:30px;}
.calendar_title{color:#FFFFFF;font-size:11px;} 
.calendar_title div{padding-top:10px;}
.Calendar table{border:0;width:370px;}
.Calendar table a{float:left;color:#6C7174;width:31px;height:20px; font-size:12px;font-family:"黑体";text-decoration:none;font-weight:bold;}
.Calendar table thead{color:#acacac;}
.Calendar table thead td {text-align:center;font-size:10px;color:#8E9396;}
.Calendar table tbody td {font-size: 11px;height:31px;text-align:center;vertical-align:middle;}
.Calendar table tbody td span{float:left;height:5px;width:5px;margin-left:13px;}
#idCalendarPre{cursor:pointer;float:left;padding-right:5px;}
#idCalendarNext{cursor:pointer;float:right;padding-right:130px;}
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
.calendar_title{font-weight:bold;}
.calendar_title{font-size:14px;}
.calendar_title img{padding-top:5px;}
.LeftMenu_list a{text-decoration: none;}
.showWeek{width: 140px;}


.monthDiv{width: 370px;height: 247px;font-size: 12px;float: left;background-image: url("/style/images/client/monthBg.jpg");}
.monthHead{width: 100%;height: 38px;}
.monthTableDiv{width: 100%;}
.monthTableDiv table{width: 370;height: 190px;}
.monthTableDiv table tr td{width: 92px;height: 58px;font-size: 22px;text-align: center;cursor: pointer;}
.mulTr{background-color: #A4D143;}
#monthPre{width: 50px;float: left;cursor: pointer;}
#yearContent{width: 100px;float: left;padding-top: 12px;margin-left: 80px;}
#yearContent span{font-size: 18px;}
#monthNext{width: 5px;float: right;cursor: pointer;}	
		
</style>
<script type="text/javascript">
	$(document).ready(function(){
		var condition = "";
		#if("$!isWeekQuery" == "true")
			openDate()//生成日历
			var startTime = $("td[class='onToday']").parent().find("a:first").attr("id");
			var weekName = $("td[class='onToday']").parent().find("a:last").html();
			var count = $("td[class='onToday']").parent().find("a").length -2
			var endTime = $("td[class='onToday']").parent().find("a:eq("+count+")").attr("id");
			condition = "&isWeekQuery=true&weekStartTime="+startTime+"&weekEndTime="+endTime+"&weekName="+encodeURI(weekName);
		#else
			var year = $year;
			var month = $month;
			$("#"+month).addClass("mulTr")
			$("#monthYear").html(year)
			condition = "&year="+year +"&month="+month;
		#end
			$("#detailFrame").attr("src","/CRMReportAction.do?operation=4&type=weekMonthDetail"+condition);
			
		$(".monthTableDiv table tr td").click(function(){
			$(".monthTableDiv table tr td").removeClass("mulTr")
			$(this).addClass("mulTr");
			$("#detailFrame").attr("src","/CRMReportAction.do?operation=4&type=weekMonthDetail&year="+$("#monthYear").html()+"&month="+$(this).attr("id"));
		});
	});
	
	//选择周的跳转
	function showWeekReport(obj){
		var startTime = $(obj).parent().parent().find("a:first").attr("id");
		var weekName = $(obj).parent().parent().find("a:last").html();
		var count = $(obj).parent().parent().find("a").length -2;
		var endTime = $(obj).parent().parent().find("a:eq("+count+")").attr("id")
		condition = "&isWeekQuery=true&weekStartTime="+startTime+"&weekEndTime="+endTime+"&weekName="+encodeURI(weekName);
		$("#detailFrame").attr("src","/CRMReportAction.do?operation=4&type=weekMonthDetail"+condition);
	}
	
	//月报表的上下年
	function changeYear(sign){
		var year = parseInt($("#monthYear").html())
		if(sign == "add"){
			$("#monthYear").html(year+1)
		}else{
			$("#monthYear").html(year-1)
		}
	}
</script>
</head>
 
<body> 
	<div id="left" class="left">
		
		#if("$!isWeekQuery" == "true")
		<div class="Calendar" id=idCalendar">
			<div class="calendar_title">
			<div id="idCalendarPre"><img src="/style/images/plan/left.jpg"/></div>
			<div id="idCalendarNext"><img src="/style/images/plan/right.jpg"/></div>
			<div style="margin-left:45px;padding-top:12px;"><span id="idCalendarYear"></span> $text.get("com.date.year") <span id="idCalendarMonth"></span> $text.get("oa.calendar.month")</div>
			</div>
			<div style="float:left;">
			<table cellspacing="0" style="margin-left:4px;">
			  <thead>
			    <tr>
			      <td width="31px">SUN</td>
			      <td width="31px">MON</td>
			      <td width="31px">TUE</td>
			      <td width="31px">WED</td>
			      <td width="31px">THU</td>
			      <td width="31px">FRI</td>
			      <td width="31px">SAT</td>
			      <td >&nbsp;</td>
			    </tr>
			  </thead>
			  <tbody id="idCalendar">
			  </tbody>
			</table>
			</div>
		</div>
		#else
		<div class="monthDiv">
		<div class="monthHead">
			<div class="calendar_title">
			<div id="monthPre"  onclick="changeYear('sub')"><img src="/style/images/plan/left.jpg"/></div>
			<div id="yearContent" ><span id="monthYear" >$!year</span><span>年</span> </div>
			<div id="monthNext"  onclick="changeYear('add')"><img src="/style/images/plan/right.jpg"/></div>
			</div>
		</div>
		<div class="monthTableDiv" style="float: left;">
			<table>
				<tr height="19px;"></tr>
				<tr>
					<td id="1">1月</td>
					<td id="2">2月</td>
					<td id="3">3月</td>
					<td id="4">4月</td>										
				</tr>
				<tr>
					<td id="5">5月</td>
					<td id="6">6月</td>
					<td id="7">7月</td>
					<td id="8">8月</td>										
				</tr>
				<tr>
					<td id="9">9月</td>
					<td id="10">10月</td>
					<td id="11">11月</td>
					<td id="12">12月</td>										
				</tr>
			</table>
		
		</div>
	
		#end
	
	</div>
	</div>
	<div id="main" class="main" >
	<iframe id="detailFrame" name="detailFrame" width="100%" frameborder=no scrolling="no"></iframe>
	</div>
	
	<script type="text/javascript">
	var main=document.getElementById("main");
	var left=document.getElementById("left");
	var detailFrame=document.getElementById("detailFrame");	
	var sHeight=document.documentElement.clientHeight-15;
	var sWidth=document.documentElement.clientWidth-10;
	main.style.height=sHeight+"px";
	main.style.width=sWidth-450+"px";
	left.style.height=sHeight+"px";
	
	detailFrame.style.height=sHeight-10+"px";
	</script>
</body>
</html>
