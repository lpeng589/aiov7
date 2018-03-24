<!DOCTYPE html>
<html class="html">
<head>
<meta name="renderer" content="webkit">
<link type='text/css' rel='stylesheet' href='/style1/css/theme.css' />
<link type='text/css' rel='stylesheet' href='/style1/css/fullcalendar.css' />
<link type='text/css' rel='stylesheet' href='/style1/css/fullcalendar.print.css' media='print' />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<style type='text/css'> 
body{margin-top: 5px;text-align: center;font-size: 13px;font-family: "微软雅黑","Microsoft YaHei","Lucida Grande",Helvetica,Arial,Verdana,sans-serif;}
.fc-event-skin {color:#da4c4b;}
.fc-event-hori {margin-bottom: 1px;}
.fc-event-draggable .fc-event-skin{}
.fc-corner-left .fc-event-inner {}
</style>
<script type='text/javascript' src='/js/ui/jquery-1.7.1.min.js'></script>
<script type='text/javascript' src='/js/ui/jquery-ui-1.8.18.custom.min.js'></script>
<script type='text/javascript' src='/js/fullcalendar.js'></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type='text/javascript'> 
	$(document).ready(function() {
		var date = new Date();
		var d = date.getDate();
		var m = date.getMonth();
		var y = date.getFullYear();
		
		$('#calendar').fullCalendar({
			theme: true,
			header: {
				left: 'prevYear,nextYear,prev,next today',
				center: 'title',
				right: 'month,agendaWeek,agendaDay'
			},
			dragOpacity: .5,
			firstDay: 1,
			editable: true,
			weekMode: 'liquid', 
			diableResizing: false,
			buttonText:{
				 prev:     '昨天',
				 next:     '明天',
				 prevYear: '去年',
				 nextYear: '明年',
				 month:    '添加',
				 week:     '刷新',
				 day:      '列表界面'
			},
			loading: function(bool) {
				if (bool) $('#loading').show();
				else $('#loading').hide();
			},
			eventMouseover: function(calEvent, jsEvent, view) {				
				$(this).attr('title', "" + calEvent.title);
				$(this).css('cursor','pointer');
				$(this).css('font-weight', 'normal');	
			},
			eventMouseout: function(calEvent, jsEvent, view) {
				$(this).css('font-weight', 'normal');
			}
		});
		
		
		$(".fc-list-ul b.cbox").click(function(){
			if($(this).attr("sel") == "f"){
				$(this).attr("sel","t").addClass("checked");
			}else{
				$(this).attr("sel","f").removeClass("checked");
			}
		});
		
		$(".p-block b.cbox").click(function(){
			$(this).addClass("checked").siblings("b.cbox").removeClass("checked");
			
		});
		
	});
 
 	//弹出框添加成功后。处理的函数，刷新日历




	var dealtype = "";
 	function dealAsyn(){
 		if(dealtype == "add"){
	 		alert("添加成功!");
 		}else if(dealtype == "update"){
 			alert("修改成功!");
 		}
 		window.location.reload();
 	}
 
</script>
</head>
<body>

<div id='loading' style='display:none'>loading...</div>
<div style="overflow: hidden;overflow-y: auto;width:100%;padding-top: 8px; position:relative;" id="calendarId">
<script type="text/javascript">
	var oDiv=document.getElementById("calendarId");
	var sHeight=document.documentElement.clientHeight-15;
	oDiv.style.height=sHeight+"px";
</script>
<div class="fc-wrap">
	<div id='calendar' class="fc-left"></div>
	
	<div class="fc-right">
		<div class="fc-right-block">
			<p>
				日历
				<i>添加日历</i>
			</p>
			<div class="add-cal-type">
				<input class="inp-txt" type="text" placeholder="输入日历名称" />
				<p>选择日历颜色</p>
				<div class="p-block">
					<b class="cbox cbox-color-1 checked"></b>
					<b class="cbox cbox-color-2"></b>
					<b class="cbox cbox-color-3"></b>
					<b class="cbox cbox-color-4"></b>
					<b class="cbox cbox-color-5"></b>
					<b class="cbox cbox-color-6"></b>
					<b class="cbox cbox-color-7"></b>
					<b class="cbox cbox-color-8"></b>
					<b class="cbox cbox-color-9"></b>
					<b class="cbox cbox-color-10"></b>
					<b class="cbox cbox-color-11"></b>
					<b class="cbox cbox-color-12"></b>
					<b class="cbox cbox-color-13"></b>
					<b class="cbox cbox-color-14"></b>
					<b class="cbox cbox-color-15"></b>
				</div>
				<div class="p-block">
					<input class="lf btn-add" type="button" value="创建日历" />
					<input class="lf btn-cel" type="button" value="取消" />
				</div>
				<b class="arrow-point"></b>
			</div>
			<ul class="fc-list-ul">
				<li>
					<b id="is-cbox" class="cbox cbox-color-1" sel="f"></b>
					<label for="is-cbox">默认日历</label>
				</li>
				<li>
					<b id="is-cbox" class="cbox cbox-color-5 checked" sel="t"></b>
					<label for="is-cbox">代办</label>
				</li>
				<li>
					<b id="is-cbox" class="cbox cbox-color-11" sel="f"></b>
					<label for="is-cbox">计划</label>
				</li>
			</ul>
		</div>
		
		<div class="fc-right-block">
			<p>
				项目
			</p>
			<ul class="fc-list-ul">
				<li>
					<b id="is-cbox" class="cbox cbox-color-1" sel="f"></b>
					<label for="is-cbox">本周iPad的开发任务</label>
				</li>
				<li>
					<b id="is-cbox" class="cbox cbox-color-1" sel="f"></b>
					<label for="is-cbox">熟悉Tower</label>
				</li>
			</ul>
		</div>
	</div>
</div>

<div class="add-calendar">
	<input class="inp-txt" type="text" placeholder="新的日程安排" />
	<div class="point-block">
		<label class="lf">日历</label>
		<select class="lf point-s">
			<option>默认日历</option>
		</select>
	</div>
	<div class="point-block">
		<label class="lf">开始</label>
		<input class="lf point-i" type="text" />
	</div>
	<div class="point-block">
		<label class="lf">结束</label>
		<input class="lf point-i" type="text" />
	</div>
	<div class="point-block">
		<label class="lf">重复</label>
		<select class="lf point-s">
			<option>不重复</option>
		</select>
	</div>
	<div class="point-block">
		<input id="is-ccbox" class="lf cbox" type="checkbox" />
		<label class="lf" for="is-ccbox">发送通知给其他人</label>
	</div>
	<div class="point-block">
		<input class="lf btn-add" type="button" value="添加" />
		<input class="lf btn-cel" type="button" value="取消" />
	</div>
	<b class="arrow-point"></b>
</div>



</body>
</html>