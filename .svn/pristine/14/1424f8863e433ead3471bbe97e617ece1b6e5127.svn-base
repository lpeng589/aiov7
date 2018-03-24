<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>$text.get(&quot;oa.calendar.mycalendar&quot;)</title>
		<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
		<script language="javascript" src="/js/function.js"></script>
		<script type='text/javascript' src='/dwr/interface/CalendarDwr.js'></script>
		<script type='text/javascript' src='/dwr/engine.js'></script>
		<script language="javascript" src="/js/date.vjs"></script>
		<script language="javascript" src="/js/validate.vjs"></script>
		<script language="javascript" src="/js/formvalidate.vjs"></script>
		<script language="javascript" src="/js/assess/periods.js"></script>
		<script type="text/javascript">

var CalendarData=new Array(20); 
var madd=new Array(12); 
var TheDate=new Date(); 
var tgString="$text.get('oa.calender.jiaYi')"; 
var dzString="$text.get('oa.calender.ziChou')"; 
var numString="$text.get('oa.calender.oneToTen')"; 
var monString="$text.get('oa.calender.twoToTen')"; 
var weekString="$text.get('oa.calender.week')"; 
var sx="$text.get('oa.calender.animal')"; 
var cYear; 
var cMonth; 
var cDay; 
var cHour; 
var cDateString; 
var DateString; 
var Browser=navigator.appName; 
var myYear_Month;
var myDay;
var myMonth;
var title="&nbsp;";
function init() { 
	CalendarData[0]=0x41A95; 
	CalendarData[1]=0xD4A; 
	CalendarData[2]=0xDA5; 
	CalendarData[3]=0x20B55; 
	CalendarData[4]=0x56A; 
	CalendarData[5]=0x7155B; 
	CalendarData[6]=0x25D; 
	CalendarData[7]=0x92D; 
	CalendarData[8]=0x5192B; 
	CalendarData[9]=0xA95; 
	CalendarData[10]=0xB4A; 
	CalendarData[11]=0x416AA; 
	CalendarData[12]=0xAD5; 
	CalendarData[13]=0x90AB5; 
	CalendarData[14]=0x4BA; 
	CalendarData[15]=0xA5B; 
	CalendarData[16]=0x60A57; 
	CalendarData[17]=0x52B; 
	CalendarData[18]=0xA93; 
	CalendarData[19]=0x40E95; 
	madd[0]=0; 
	madd[1]=31; 
	madd[2]=59; 
	madd[3]=90; 
	madd[4]=120; 
	madd[5]=151; 
	madd[6]=181; 
	madd[7]=212; 
	madd[8]=243; 
	madd[9]=273; 
	madd[10]=304; 
	madd[11]=334; 
} 
function GetBit(m,n) { 
	return (m>>n)&1; 
} 
function e2c() { 
	var total,m,n,k; 
	var isEnd=false; 
	var tmp=TheDate.getYear(); 
	if (tmp<1900) tmp+=1900; 
		total=(tmp-2001)*365+Math.floor((tmp-2001)/4)+madd[TheDate.getMonth()]+TheDate.getDate() -23; 
	if (TheDate.getYear()%4==0&&TheDate.getMonth()>1) 
		total++; 
	for(m=0;;m++) { 
		k=(CalendarData[m]<0xfff)?11:12; 
		for(n=k;n>=0;n--) { 
			if(total<=29+GetBit(CalendarData[m],n)) 
		{ 
		isEnd=true; 
		break; 
		} 
		total=total-29-GetBit(CalendarData[m],n); 
		} 
		if(isEnd)break; 
	} 
	cYear=2001 + m; 
	cMonth=k-n+1; 
	cDay=total; 
	if(k==12) 
	{ 
		if(cMonth==Math.floor(CalendarData[m]/0x10000)+1) 
			cMonth=1-cMonth; 
		if(cMonth>Math.floor(CalendarData[m]/0x10000)+1) 
			cMonth--; 
	} 
	cHour=Math.floor((TheDate.getHours()+3)/2); 
} 
function GetcDateString() { 
	var tmp=""; 
	tmp+=tgString.charAt((cYear-4)%10); //年干 
	tmp+=dzString.charAt((cYear-4)%12); //年支 
	tmp+="$text.get('com.date.year')("; 
	tmp+=sx.charAt((cYear-4)%12); 
	tmp+=")"; 
	myMonth="";
	if(cMonth<1) 
	{ 
		tmp+="$text.get('oa.calender.embellish')"; 
		tmp+=monString.charAt(-cMonth-1); 
		myMonth+="$text.get('oa.calender.embellish')"; 
		myMonth+=monString.charAt(-cMonth-1); 
	} 
	else 
	tmp+=monString.charAt(cMonth-1); 
	tmp+="$text.get('com.date.month')"; 
	myMonth+=monString.charAt(cMonth-1); 
	myMonth+="$text.get('com.date.month')"; 
	myYear_Month=tmp;
	myDay="";
	myDay=(cDay<11)?"$text.get('oa.calender.begin')":((cDay<20)?"$text.get('oa.calender.tenr')":((cDay<30)?"$text.get('oa.calender.twenty')":"$text.get('oa.calender.thirty')")); 
	tmp+=(cDay<11)?"$text.get('oa.calender.begin')":((cDay<20)?"$text.get('oa.calender.tenr')":((cDay<30)?"$text.get('oa.calender.twenty')":"$text.get('oa.calender.thirty')")); 
	if(cDay%10!=0||cDay==10) 
	{
		tmp+=numString.charAt((cDay-1)%10);
		myDay+=numString.charAt((cDay-1)%10);
	} 
	cDateString=tmp; 
	return tmp; 
} 
function GetDateString() { 
	var tmp=""; 
	var t1=TheDate.getYear(); 
	if (t1<1900)t1+=1900;tmp+=t1+"-"+(TheDate.getMonth()+1)+"-"+TheDate.getDate()+" "+
		TheDate.getHours()+":"+((TheDate.getMinutes()<10)?"0":"")+TheDate.getMinutes()+" $text.get('oa.calender.weeks')"+
		weekString.charAt(TheDate.getDay()); 
			DateString=tmp; 
	return tmp; 
} 
function GetDateString2(date) { 
	var tmp=""; 
	var t1=date.getYear(); 
	if (t1<1900)t1+=1900;tmp+="$text.get('oa.calender.AD')"+t1+"$text.get('com.date.year')"+(date.getMonth()+1)+"$text.get('com.date.month')"+
			date.getDate()+"$text.get('com.date.day')"+"<br> $text.get('oa.calender.weeks')"+weekString.charAt(date.getDay()); 
	DateString=tmp; 
	return tmp; 
}
function getmyMonth() {
	return myMonth;
}
function getDATE(obj){
	TheDate = obj;
	init(); 
	e2c(); 
	GetDateString(); 
	GetcDateString(); 
	return cDateString;
}
var $ = function (id) {
    return "string" == typeof id ? document.getElementById(id) : id;
};

var Class = {
  create: function() {
    return function() {
      this.initialize.apply(this, arguments);
    }
  }
}

var Extend = function(destination, source) {
    for (var property in source) {
        destination[property] = source[property];
    }
    return destination;
}

var Calendar = Class.create();
	Calendar.prototype = {
	  	initialize: function(container, options) {
		this.Container = $(container);//容器(table结构)
		this.Days = [];//日期对象列表
		this.SetOptions(options);
		this.Year = this.options.Year || new Date().getFullYear();
		this.Month = this.options.Month || new Date().getMonth() + 1;
		this.SelectDay = this.options.SelectDay ? new Date(this.options.SelectDay) : null;
		this.onSelectDay = this.options.onSelectDay;
		this.onToday = this.options.onToday;
		this.onFinish = this.options.onFinish;	
		this.Draw();
 	 },
	  //设置默认属性







	  SetOptions: function(options) {
			this.options = {//默认值







			Year:			0,//显示年







			Month:			0,//显示月







			SelectDay:		null,//选择日期
			onSelectDay:	function(){},//在选择日期触发
			onToday:		function(){},//在当天日期触发







			onFinish:		function(){}//日历画完后触发







		};
		Extend(this.options, options || {});
	  },
	  //当前月








	  NowMonth: function() {
		this.PreDraw(new Date());
	  },
	  //上一月







	
	  PreMonth: function() {
		this.PreDraw(new Date(this.Year, this.Month - 2, 1));
	  },
	  //下一月







	
	  NextMonth: function() {
		this.PreDraw(new Date(this.Year, this.Month, 1));
	  },
	  //上一年







	
	  PreYear: function() {
		this.PreDraw(new Date(this.Year - 1, this.Month - 1, 1));
	  },
	  //下一年







	  NextYear: function() {
		this.PreDraw(new Date(this.Year + 1, this.Month - 1, 1));
	  },
	  //根据日期画日历







	
	  PreDraw: function(date) {
		//再设置属性







	
		this.Year = date.getFullYear(); this.Month = date.getMonth() + 1;
		//重新画日历







	
		this.Draw();
	  },
	  //画日历







	
	  Draw: function() {
		//用来保存日期列表
		var arr = [];
		//用当月第一天在一周中的日期值作为当月离第一天的天数
		for(var i = 1, firstDay = new Date(this.Year, this.Month - 1, 1).getDay(); i <= firstDay; i++){ arr.push(0); }
		//用当月最后一天在一个月中的日期值作为当月的天数
		for(var i = 1, monthDay = new Date(this.Year, this.Month, 0).getDate(); i <= monthDay; i++){ arr.push(i); }
		//清空原来的日期对象列表







	
		this.Days = [];
		//插入日期
		var frag = document.createDocumentFragment();
		while(arr.length){
			//每个星期插入一个tr
			var row = document.createElement("tr");
			//每个星期有7天







	
			for(var i = 1; i <= 7; i++){
				var cell = document.createElement("td"); cell.innerHTML = "&nbsp;";
				if(arr.length){
					var d = arr.shift();
					if(d){
						cell.innerHTML = d;
						this.Days[d] = cell;
						var on = new Date(this.Year, this.Month - 1, d);
						//判断是否今日
						this.IsSame(on, new Date()) && this.onToday(cell);
						//判断是否选择日期
						this.SelectDay && this.IsSame(on, this.SelectDay) && this.onSelectDay(cell);
					}
				}
				row.appendChild(cell);
			}
			frag.appendChild(row);
		}
		//先清空内容再插入(ie的table不能用innerHTML)
		while(this.Container.hasChildNodes()){ this.Container.removeChild(this.Container.firstChild); }
		this.Container.appendChild(frag);
		//附加程序
		this.onFinish();
	  },
	  //判断是否同一日







	
	  IsSame: function(d1, d2) {
		return (d1.getFullYear() == d2.getFullYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate());
	  } 
}

  var tip={$:function(ele){
	if(typeof(ele)=="object")
		return ele;
	else if(typeof(ele)=="string"||typeof(ele)=="number")
		return document.getElementById(ele.toString());
		return null;
	},
	mousePos:function(e){
		var x,y;
		var e = e||window.event;
		return{x:e.clientX+document.body.scrollLeft+document.documentElement.scrollLeft,y:e.clientY+document.body.scrollTop+document.documentElement.scrollTop};
	},
	start:function(a,b,c,obj){
	var current = new Date();
	current.setYear(a);
	current.setMonth(b-1);
	current.setDate(c);
	var d = GetDateString2(current);
	var d2 = getDATE(current);
	var self = this;
	var t = self.$("mjs:tip");
	obj.onmousemove=function(e){
	var mouse = self.mousePos(e);	
	//alert(mouse.x+"====>"+mouse.y);
			var _left=0;
			var _top=0;

			if(mouse.x>450)
			{
			_left=-225;			
			}
			if(mouse.y >450)
			{
			_top=-100;
			}
			t.style.left = mouse.x+_left + 10 + 'px';
			t.style.top = mouse.y +_top+ 10 + 'px';
			
			t.innerHTML = d+"<br>"+d2;
			
			t.style.display = '';
		}
		obj.onmouseout=function(){
			t.style.display = 'none';
		}
	}
  }
  function getYear_Month(){
 	 return myYear_Month;
  }
  function getmyDay(){
 	 return myDay;
  }
  function getmyDate(da){
 	 var mydate2="$text.get('oa.calender.AD')"+da.getYear()+"$text.get('com.date.year')";
  	 mydate2+=da.getMonth()+"$text.get('com.date.month')";
	 mydate2+=da.getDay()+"$text.get('com.date.day')<br>";
  	 mydate2+="$text.get('oa.calender.weeks')"+weekString.charAt(da.getDay())+"<br>"; 
  	 return mydate2;
  }

</script>
		<style type="text/css">

.Calendar {
	
	font-family:Verdana;
	font-size:14px;
	text-align:center;
	width:95%;
	height:100%;
	padding:5px;
}
.Calendar a{
	color:#33CC00;
}

.Calendar table{
	width:100%;
	text-align:center;
	border-right:1px solid #CCCCCC;
}

.Calendar table thead{

	color:#006699;
}

.Calendar table td {
	border-bottom:1px solid #d2d2d2;
	border-left:1px solid #D2D2D2;
	border-top:0px;
	height:13.5%;
	padding:5px;
	text-align:center;
	white-space:nowrap;
	font-size: 14px;
	padding:1px;
}
.Calendarbg {
	border-left:1px solid #D2D2D2;
	border-right:1px solid #D2D2D2;
	border-top:1px solid #D2D2D2;
	border-bottom:1px solid #d2d2d2;
	background:url(/style/images/aiobg.gif) repeat-x left -21px;
}
#idCalendarPre{
	cursor:pointer;
	float:left;
	padding-right:5px;
}
#idCalendarNext{
	cursor:pointer;
	float:right;
	padding-right:5px;
}
#idCalendar td.onToday {
	font-weight:bold;
	color:#C60;
}
#idCalendar td.onSelect {
	font-weight:bold;
}
BODY {
	FONT: 12px/1.8 arial
}
A {
	COLOR: #3366cc; TEXT-DECORATION: none
}
A:visited {
	COLOR: #3366cc; TEXT-DECORATION: none
}
A:hover {
	COLOR: #f60; TEXT-DECORATION: underline
}
.tip {
	BORDER-RIGHT: #ddd 2px solid; PADDING-RIGHT: 8px; BORDER-TOP: #ddd 2px solid; PADDING-LEFT: 8px; BACKGROUND: #f1f1f1; PADDING-BOTTOM: 8px; BORDER-LEFT: #ddd 2px solid; WIDTH: 190px; COLOR:#333399; PADDING-TOP: 8px; BORDER-BOTTOM: #ddd 2px solid
}
IMG {
	BORDER-TOP-STYLE: none; BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE: none
}
.tiatle {
	margin-left:3px;
	margin-right:15px;
	width:256px;
	float:left;
	height:277.25px;
	overflow:auto;
	overflow-y:auto;
	margin-top:2px;
}
.div {
	height: 50%;
}
.empDiv{
	margin-left:3px;
	padding-left:0px;
	margin-right:15px;
	width:208px;
	float:left;
	text-align:center;
	height:253px;
	overflow:auto;
	overflow-y:auto;
	margin-top:2px;
}
.empUl{
	float:left;
	height:auto;
	margin-top:3px;
	margin-left:0px;
	text-align:left;
	padding:2px 0px 2px 0;
	background: #FFFFFF url(../images/listRange_bg.gif) repeat-x;
	border:1px solid #E0E0E0;	
}

 
.listRange_my{
 	float:left;
	height:auto;
	width:99%;
	margin-top:3px;
	margin-left:3px;
	text-align:left;
	padding:2px 0px 2px 0;
	background: #FFFFFF url(../images/listRange_bg.gif) repeat-x;
	border:1px solid #E0E0E0;	
}
 
.listRange_my li{
	width:200px;
	float:left;
	height:18px;
	line-height:18px;
}

.listRange_my img {
	cursor:pointer;
}

.listRange_my li span{
	float:left;
	width:80px;
	margin-top:5px;
	text-align:right;
}

.listRange_my input{
	border:0;
	width: 80px;
	border: 1px solid #42789C;	
	text-align:left;
}

</style>

	</head>
	<body onload="onloadByTable()">
		<form action="/DutyPeriodsAction.do?type=2" name="periodsForm" id="periodsForm" method="post">
			<div class="Heading">
				<div class="HeadingIcon">
					<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
				</div>
				<div class="HeadingTitle">
					$text.get("dutyPeriods.add")
				</div>
				<ul class="HeadingButton">
				<li>
						<button type="submit" name="saveList" title="Ctrl+S"
							onClick="return subAdd();" class="b2" style="margin-top:0px;padding:0px; line-height:20px;">$text.get("common.lb.save")
						</button>
					</li>
					<li>
						<button type="button" name="backList" title="Ctrl+Z"
							onClick="window.history.go(-1);" class="b2" style=" margin-top:0px;padding:0px; line-height:20px;">$text.get("common.lb.back")
						</button>
					</li>
					
				</ul>
			</div>
		<input type="hidden" name="operation" id="operation" value="1">
		<div class="div">
			<div class="Calendar" align="center">
				<div class="Calendarbg">
					<div id="idCalendarPre">
						&lt;&lt;
					</div>
					<div id="idCalendarNext">
						&gt;&gt;
					</div><b style="color: #006699;">
					<span id="idCalendarYear"></span>$text.get("com.date.year")








					<span id="idCalendarMonth"></span>$text.get("com.date.month")</b>&nbsp;&nbsp;&nbsp;
				</div>
				<table width="100%" cellpadding="0" cellspacing="0" class="Calendar">
					<thead align="center">
						<tr>
							<td width="13%">$text.get("oa.calendar.sunday")</td>
							<td width="13%">$text.get("oa.calendar.monday")</td>
							<td width="13%">$text.get("oa.calendar.tuesday")</td>
							<td width="13%">$text.get("oa.calendar.wednesday")</td>
							<td width="13%">$text.get("oa.calendar.thursday")</td>
							<td width="13%">$text.get("oa.calendar.friday")</td>
							<td width="13%">$text.get("oa.calendar.saturday")</td>
						</tr>
					</thead>
					<tbody id="idCalendar">
					</tbody>
				</table>
			</div>

<script language="JavaScript">

var cale = new Calendar("idCalendar", {
	SelectDay: new Date().setDate(10),
	onSelectDay: function(o){ o.className = "onSelect"; },
	onToday: function(o){ o.className = "onToday"; },
	onFinish: function(){

		$("idCalendarYear").innerHTML = this.Year; $("idCalendarMonth").innerHTML = this.Month;
		
		var flag ;
		//-------------
		var d = new Date();
		var today=new Date();
		d.setYear(this.Year);
		today.setYear(this.Year);
		
		d.setMonth(this.Month-1);
		today.setMonth(this.Month-1);
		
		
		d.setMonth(d.getMonth()+1); 
		d.setDate(1);
	
		today.setDate(1);
		BirthDay=new Date(today.valueOf());//改成你的计时日期
		
		timeold=(d.getTime()-BirthDay.getTime());
		sectimeold=timeold/1000
		secondsold=Math.floor(sectimeold);
		msPerDay=24*60*60*1000
		e_daysold=timeold/msPerDay
		daysold=Math.floor(e_daysold);		

		//--------------
		var flag = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31];
		if(daysold==30)
		{
			flag = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30];
		}	
		if(daysold==29)
		{
			flag = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29];
		}		
		if(daysold==28)
		{
			flag = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28];
		}		
	var years = new Date().getYear() ;
	var dates = new Date().getDate();
	var months = new Date().getMonth() + 1 ;
	
	for(var i = 0, len = flag.length; i < len; i++)
	{
		var current = new Date();
		current.setYear(this.Year);
		
		current.setMonth(this.Month);
		current.setDate(flag[i]);
		var d = getDATE(current);
		var ddd =getmyDay();
		var dddd = getmyMonth();
		var myd=hh(this.Year,this.Month-1,flag[i]);
    	
		if(years==current.getYear() && dates==current.getDate() && months==current.getMonth()){
			this.Days[flag[i]].innerHTML = "<input type='hidden' name='tempDate"+flag[i]+"' id='tempDate"+flag[i]+"' value='"+this.Year+"-"+formatDate(this.Month)+"-"+formatDate(flag[i])+"' /><a href='javascript:void(0);' style='font-size:20px' onclick=\"openSelect('/UserFunctionAction.do?tableName=tblDutyPeriodsByDate&selectName=squadEnactmentPeriods&operation=22','squadEnactmentNo',"+flag[i]+")\" onmouseover=\"tip.start("+
			this.Year+","+this.Month+","+flag[i]+",this)\"" +"><b><font color='red'>" + flag[i] + "</font></b></a>";
			current.setDate(flag[i]);
		}else{
			this.Days[flag[i]].innerHTML = "<input type='hidden' name='tempDate"+flag[i]+"' id='tempDate"+flag[i]+"' value='"+this.Year+"-"+formatDate(this.Month)+"-"+formatDate(flag[i])+"' /><a href='javascript:void(0);' style='font-size:20px' onclick=\"openSelect('/UserFunctionAction.do?tableName=tblDutyPeriodsByDate&selectName=squadEnactmentPeriods&operation=22','squadEnactmentNo',"+flag[i]+")\" onmouseover=\"tip.start("+
			this.Year+","+this.Month+","+flag[i]+",this)\"" +"><b>" + flag[i] + "</b></a>";
			current.setDate(flag[i]);
		}
		
		}
	}
});



$("idCalendarPre").onclick = function(){ cale.PreMonth(); }
$("idCalendarNext").onclick = function(){ cale.NextMonth(); }

function hh(a,b,c)
{
	return a+"$text.get('oa.calender.embellish')"+b+"$text.get('com.date.month')"+c+"$text.get('com.date.day')";
}
//------------------
function getDayCount()
{
	var d = new Date();
	var today=new Date();
	d.setMonth(d.getMonth()+1); 
	d.setDate(1);
	//today.setMonth(today.getMonth()); 
	alert(d.getMonth()) ;
	today.setDate(1);
	BirthDay=new Date(today.valueOf());//改成你的计时日期
	
	timeold=(d.getTime()-BirthDay.getTime());
	sectimeold=timeold/1000
	secondsold=Math.floor(sectimeold);
	msPerDay=24*60*60*1000
	e_daysold=timeold/msPerDay
	daysold=Math.floor(e_daysold);
	return daysold;
}

//------begin ajax
 var xmlHttp;

//先新建一XHR对象

function createXMLHttpRequest() {

	//如果是IE，用activexobject
	if (window.ActiveXObject) {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	} 
	//如果其它浏览器就用XMLHttpRequest
	else if (window.XMLHttpRequest) {
		xmlHttp = new XMLHttpRequest();
	}
}

function startRequest(path) {
	createXMLHttpRequest();	
	//指定当readyState属性改变时执行的函数







	xmlHttp.onreadystatechange = handleStateChange;
	//创建一个新的http请求，并指定此请求的方法、URL以及验证信息
	xmlHttp.open("post", path, false);
	//发送请求到http服务器并接收回应
	xmlHttp.send("type=ajax");
}

function handleStateChange() {
　	//4数据接收完毕
	if(xmlHttp.readyState == 4) {
		//200返回请求状态为OK
		if(xmlHttp.status == 200) {
		
			title = xmlHttp.responseText;
		}
	}
}

function openInputDate(obj)
{
	c.showMoreDay = false;
	c.show(obj);
}
//------end ajax
</script>
			<DIV class=tip id=mjs:tip
				style="DISPLAY: none;LEFT: 0px;text-indent:999999999;POSITION: absolute; TOP: 0px"></DIV>
		</div>
		<div >
			<div>
				<ul class="listRange_my">
					<li>
						<span>$text.get("com.checkOnWorkAttendance.periodsAdd.cycleName"):<font color="red">*</font></span>
						<input type="text" name="periodsName" id="periodsName"/>
					</li>
					<li>
						<span>$text.get("oa.common.beginWorkingday"):</span>
						<input type="text" onclick="openInputDate(this)" name="startDate" id="startDate"/>
					</li>
					<li style="width:280px;">
						<span>$text.get("oa.common.endWorkingday"):</span>
						<input type="text" onclick="openInputDate(this)" name="endDate" id="endDate"/>&nbsp;&nbsp;
						<button type="button" style=" width: 80px; margin: 0px; padding: 0px;" onclick="openSelectByEmp('/UserFunctionAction.do?tableName=tblDutyPeriodsByDate&selectName=squadEnactmentPeriods&operation=22')" class="b4">$text.get("oa.common.choose.squadNo")</button>
					</li>
					<li>
						<span>$text.get("oa.communicate.chooose.employee"):</span>
						<input type="text" ondblclick="openSelect('/UserFunctionAction.do?tableName=tblEmpArrangeSquadList&selectName=employeeASL&operation=22','employeeNo')"/>
						<img src="/$globals.getStylePath()/images/St.gif"
							onClick="openSelect('/UserFunctionAction.do?tableName=tblEmpArrangeSquadList&selectName=employeeASL&operation=22','employeeNo')"/>
					</li>
					<li>
						<span>$text.get("oa.common.dept"):</span>
						<input type="text" ondblclick="openSelect('/UserFunctionAction.do?tableName=tblDecArrangeSquadList&selectName=departmentASL&operation=22','DepartmentNo')"/>
						<img src="/$globals.getStylePath()/images/St.gif"
							onClick="openSelect('/UserFunctionAction.do?tableName=tblDecArrangeSquadList&selectName=departmentASL&operation=22','DepartmentNo')"/>
					</li>
				</ul>
			</div>
			<div class="tiatle">
		<table border="0" cellpadding="0" cellspacing="0" 
				class="listRange_list" id="table"
				width="240">
				<THead>
					<tr class="listhead">
						<td class="listheade" width="30" align="center">
							<span style="vertical-align:middle;"><IMG
									src="/$globals.getStylePath()/images/down.jpg" border=0 />
							</span>
						</td>
						<td width="80" align="center">
							$text.get("oa.calendar.date")
						</td>
						<td width="100" align="center">
							$text.get("squad.squadEnactmentName")
						</td>
						<td width="30" align="center">
							<img src="/$globals.getStylePath()/images/del.gif"/>
					    </td>
					</tr>
				</THead>
				<TBody id="my_body" align="center">
					
				</TBody>
		  </table>
		</div>
			<div class="empDiv">
			<table border="0" cellpadding="0" cellspacing="0" 
				class="listRange_list" width="190">
				<thead align="center">
					<tr>
						<td width="30">
							<span style="vertical-align:middle;"><IMG
									src="/$globals.getStylePath()/images/down.jpg" border=0 />
							</span>
						</td>
						<td width="130">$text.get("stat.empFullName")</td>
						<td width="30">&nbsp;</td>
					</tr>
				</thead>
				<tbody id = "empBody" align="center">
				</tbody>
			</table>
		</div>
			
		<div class="empDiv">
			<table border="0" cellpadding="0" cellspacing="0" 
				class="listRange_list" width="190">
				<thead align="center">
					<tr>
						<td width="30">
							<span style="vertical-align:middle;"><IMG
									src="/$globals.getStylePath()/images/down.jpg" border=0 />
							</span>
						</td>
						<td width="130">$text.get("oa.common.departmentFullName")</td>
						<td width="30">&nbsp;</td>
					</tr>
				</thead>
				<tbody id="decBody" align="center">
				</tbody>
			</table>
			</div>
		</div>
		
		
		
		</form>
	</body>
</html>
