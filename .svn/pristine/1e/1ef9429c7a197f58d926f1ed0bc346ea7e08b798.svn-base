<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<link type='text/css' rel='stylesheet' href='/style1/css/theme.css' />
<link type='text/css' rel='stylesheet' href='/style1/css/fullcalendar.css' />
<link type='text/css' rel='stylesheet' href='/style1/css/fullcalendar.print.css' media='print' />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/CRselectBoxList.css"/>

<script type='text/javascript' src='/js/ui/jquery-1.7.1.min.js'></script>
<script type='text/javascript' src='/js/ui/jquery-ui-1.8.18.custom.min.js'></script>
<script type='text/javascript' src='/js/fullcalendars.js'></script>
<script type="text/javascript"  src="/js/aioselect.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type='text/javascript'> 
 
	$(document).ready(function() {
		var date = new Date();
		var d = date.getDate();
		var m = date.getMonth();
		var y = date.getFullYear();
		var beginTimeOut=date.getMilliseconds();
		
		$('#calendar').fullCalendar({
			theme: true,
			
			dragOpacity: .5,
			firstDay: 1,
			editable: false,
			weekMode: 'liquid', 
			//draggable: false,
			diableResizing: false,
			
			loading: function(bool) {
				if (bool) $('#loading').show();
				else $('#loading').hide();
			},
			eventMouseover: function(calEvent, jsEvent, view) {
				var fend = "";
				if(calEvent.ssss == "null"){
					fend = "";
				}else{
					fend = calEvent.ssss;
				}
				var fstart  = jQuery.fullCalendar.formatDate(calEvent.start, "yyyy-MM-dd HH:mm:ss");
				
				$(this).attr('title', "" + calEvent.title + " \n开始时间：" + fstart + " \n总结时间：" + fend + " " );
				$(this).css('cursor','pointer');
				$(this).css('font-weight', 'normal');	
			},
			eventMouseout: function(calEvent, jsEvent, view) {
				$(this).css('font-weight', 'normal');
			},
			eventClick: function(event, jsEvent, view) {
				var d = new Date();
				var year = d.getYear();
				var month = d.getMonth()+1;
				var startData = jQuery.fullCalendar.formatDate(event.start, "yyyy-MM-dd");
				window.open('/OAWorkPlanAction.do?operation=4&flagAdvice=advice&planType=day&keyId='+event.id+'&strDate='+startData+'&windowOpen=true');
			}
		});
		
	});
 	
 	//处理新增工作计划弹出框添加返回
 	function dealAsyn(){
 		window.location.reload();
 	}
 	
</script>
<script type="text/javascript">
	function fa(){
		var datas = "&strDate=${year}-${month}-01&userId=$userId&planStatus=$!planStatus";
		return datas;
	}
	function gs(){
		var userId = "&nbsp;&nbsp;（$globals.getEmpNameById($!userId)"+"）";
		return userId;
	}
	function selectUserId(){
		var userId = "$!userId";
		return userId;
	}
	function LogUserId(){
		var logBeanId="$!LoginBean.id";
		return logBeanId;
	}
</script>
<style type='text/css'> 
body {
	margin-top: 5px;
	text-align: center;
	font-size: 13px;
	font-family: "微软雅黑","Microsoft YaHei","Lucida Grande",Helvetica,Arial,Verdana,sans-serif;
}
 
#calendar {
	width: 98%;
	margin: 0 auto;
}
.planNormal{
	color:#000000;
	font-size:12px;
}
.noComplete{
	color:#ff0000;
	font-size:12px;
}
.noSumary{
	color:#ff00ff;
	font-size:12px;
}
 .selectPerson{
 	background-image:url("/style1/images/login_ye/user.gif");
 	cursor: pointer;
 	position:absolute;
 	top:10px;
 	left:59%;
 	width:36px;
 	height:28px;
 	text-align: center;
 }
 .overThing{
  position:absolute;
  top:15px;
  left:62%;
 }
</style>

</head>
<body> 
<form name="form" method="post" action="/OAWorkPlanAction.do?operation=4&opType=calendar">
<input type="hidden" name="userId" id="userId" value="$!userId"/>
<input type="hidden" name="planStatus" id="planStatus" value="$!planStatus"/>
<div id='loading' style='display:none'>loading...</div>
#if("$!msg"!="" && $!msg.length()>1)
<div class="selectPerson" name="selPersonBox" id="selPersonBox" onclick="selPerson();" title="选择查看人"></div>
#end
<div>
<div class="overThing">
	 <script type="text/javascript">
	  #set($enums=$globals.getEnumerationItems("OverThing"))	
			#if($enums.size()<=10)		
				aioselect('$!globals.get($row,1)_view',[
					#foreach($erow in $enums)
						#if($velocityCount==$enums.size())
							{value:'$!erow.value',name:'$erow.name'}
						#else
							{value:'$!erow.value',name:'$erow.name'},
						#end
					#end
				],'$!planStatus','100','selectOver()');
			#end
	 </script>
</div>
</div>
<div style="overflow: hidden;overflow-y: auto;padding-top: 8px;" id="calendarId">
<script type="text/javascript">
	var oDiv=document.getElementById("calendarId");
	var sHeight=document.documentElement.clientHeight-15;
	oDiv.style.height=sHeight+"px";
</script>
<div id='calendar'></div>

</div>
</form>
</body>
<script type="text/javascript">
 	function selPerson(){
 		asyncbox.open({
		 		id : 'selPerson',
		　　　	url : '/OAWorkPlanAction.do?operation=4&opType=selPerson',
			 	title : '选择查看人',
		　　　	width : 280,
		　　　	height : 300,
		     	btnsbar : jQuery.btn.OKCANCEL,
			 	callback : function(action,opener){
		　　　　　	//判断 action 值。


		　　　　　	if(action == 'ok' ){
				 		var userId= opener.document.getElementById("userId");
				  		if(userId.value == "" || userId.value == null){
		   					asyncbox.tips('请选择查看人','error',1200);
		   					return false;
		   				}
				  		document.getElementById("userId").value=userId.value;
				   		opener.checkAlert();
				   		form.submit();
				  		//window.location.reload() ;
		   				//window.parent.frames["leftFrame"].location.reload();
						   		 
		　　　　　	}
		　　　}
 		});
 	}
 	function selectOver(){
		var value=$("#_view").val();
		document.getElementById("planStatus").value=value;
		form.submit(); 
	}
</script>
</html>