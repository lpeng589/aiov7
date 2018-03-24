<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.calender.lookMyCalender")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link rel="stylesheet" href="/js/ui/jquery-ui-1.8.18.custom.css" type="text/css"/>
<script language="javascript" src="/js/function.js"></script>
<script src="/js/jquery.js" type="text/javascript"></script> 
<script type="text/javascript" src="/js/ui/jquery-ui-1.8.18.custom.min.js"></script>
<script>

function alertSet(relationId,str){
	var d = new Date();
	var year = d.getYear();
	var month = d.getMonth()+1;
	var urls=encodeURIComponent('/OAMyCalendar.do?operation=5&listType=image&id='+relationId+"&year="+year+"&month="+month);
	var typestr=encodeURIComponent('$text.get("com.oa.calendarWake")'+str);
	$("<div id='"+relationId+"' title='提醒设置' style='padding:0px;'><iframe  src='/UtilServlet?operation=alertDetail&relationId="+relationId+"&falg=false"+"&title=$text.get("oa.calendar.mycalendar")&typestr="+typestr+"&urls="+urls+"&date="+d+"' width='100%' height='100%' frameborder='no' framespacing='0' scrolling='no' /></div>").dialog({
		resizable: false ,
		width:552,
		height:450,
		close: function(event, ui) {
			$("#"+relationId).remove() ;
		}
	});
}
function upd(id){
	document.location="/OAMyCalendar.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&id="+id;
}
function collection(){
	var favoriteURL = "$!favoriteURL" ;
	var favoriteName = encodeURI("$!myCalendar.CalendarTitle");		
	var tabName = encodeURI("我的日历");				
	var str="/UtilServlet?operation=addDedailAdviceInfoCollection&module=dedailAdviceInfo&favoriteName="+favoriteName+"&tabName="+tabName+"&favoriteURL="+favoriteURL;
			
 	AjaxRequest(str);
    var value = response;
	var oaul=document.getElementById("empList");
	if(value=="1"){			 	
		alert('$text.get("oa.favorite.add.success")');
	}else if(value=="2"){
		alert('$text.get("oa.favorite.exist")');
	}else{
		alert('$text.get("oa.favorite.add.failture")');
	}
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
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("oa.calender.lookMyCalender")</div>
	<ul class="HeadingButton"><!--判断是否是收藏夹中连接过来(1为是)-->
			#if("$!isEspecial" != "1")
	    	<li><button onclick="javascript:alertSet('$myCalendar.id','$!text.get("oa.calendar.mycalendar")$myCalendar.CalendarDate[$myCalendar.CalendarTitle]')">$text.get("workflow.msg.warmsetting")</button></li>
			<li><button type="button" onClick="collection()" style="width:80px;" class="b2">$text.get("aio.add.favorite")</button></li>
			<li><button type="button" onClick="upd('$myCalendar.id')" class="b2">$text.get("sms.button.update")</button></li>
			<li><button type="button" onClick="history.go(-1)" class="b2">$text.get("common.lb.back")</button></li>
			#else
			<li><button type="button" onClick="javascript:closeWin();" class="b2">$text.get("common.lb.close")</button></li>
			#end
	</ul>
</div>
<div id="listRange_id">
	<div class="listRange_div_msg2">
			<ul>
			  <li><span>$text.get("oa.calender.Calenderdate")：</span>$myCalendar.CalendarDate #if("$!myCalendar.endDate"!="") —— $!myCalendar.endDate#end
              </li>
			  <li><span>$text.get("oa.calendar.calendarTitle")：</span>
			   $myCalendar.CalendarTitle
			  </li>
			  <li class="listRange_cont2"><span>$text.get("oa.calendar.Calendercontent")：</span>
			   $myCalendar.CalendarContext
			  </li>
			</ul>
	</div>
</body>
</html>
