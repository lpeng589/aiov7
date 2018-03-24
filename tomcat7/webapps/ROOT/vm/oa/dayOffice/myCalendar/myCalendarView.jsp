<!DOCTYPE html>
<html class="html">
<head>
<meta name="renderer" content="webkit">#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#else
<title>我的日程</title>
#end
<link type='text/css' rel='stylesheet' href='/style1/css/theme.css' />
<link type='text/css' rel='stylesheet' href='/style1/css/fullcalendar.css' />
<link type='text/css' rel='stylesheet' href='/style1/css/fullcalendar.print.css' media='print' />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<style type='text/css'>
.def-list{text-align:left;} 
body{margin-top: 5px;text-align: center;font-size: 13px;font-family: "微软雅黑","Microsoft YaHei","Lucida Grande",Helvetica,Arial,Verdana,sans-serif;}
.hideBg{ display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;  height: 100%;  background-color:rgba(0, 0, 0, 0.23);  z-index:99;  -moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);}
.opCtLian:{border: 1px solid #dfdfdf;background: #f9f9f9;border-radius: 4px;width: 180px;margin-left: 30px;cursor:pointer;text-align: left;}
/*.fc-event-skin {color:#78b1ec;}*/
.fc-corner-bg-da4c4b{color:#da4c4b;height:17px;}
.fc-corner-bg-da4c4b .fin-link{border-color:#da4c4b;}
.fc-corner-bg-ec6754{color:#ec6754;height:17px;}
.fc-corner-bg-ec6754 .fin-link{border-color:#ec6754;}
.fc-corner-bg-e88b3b{color:#e88b3b;height:17px;}
.fc-corner-bg-e88b3b .fin-link{border-color:#e88b3b;}
.fc-corner-bg-cb9e5d{color:#cb9e5d;height:17px;}
.fc-corner-bg-cb9e5d .fin-link{border-color:#cb9e5d;}
.fc-corner-bg-a8bb48{color:#a8bb48;height:17px;}
.fc-corner-bg-a8bb48 .fin-link{border-color:#a8bb48;}
.fc-corner-bg-68aa63{color:#68aa63;height:17px;}
.fc-corner-bg-68aa63 .fin-link{border-color:#68aa63;}
.fc-corner-bg-358560{color:#358560;height:17px;}
.fc-corner-bg-358560 .fin-link{border-color:#358560;}
.fc-corner-bg-3caaa9{color:#3caaa9;height:17px;}
.fc-corner-bg-3caaa9 .fin-link{border-color:#3caaa9;}
.fc-corner-bg-438ab4{color:#438ab4;height:17px;}
.fc-corner-bg-438ab4 .fin-link{border-color:#438ab4;}
.fc-corner-bg-457198{color:#457198;height:17px;}
.fc-corner-bg-457198 .fin-link{border-color:#457198;}
.fc-corner-bg-6c74a3{color:#6c74a3;height:17px;}
.fc-corner-bg-6c74a3 .fin-link{border-color:#6c74a3;}
.fc-corner-bg-9670c7{color:#9670c7;height:17px;}
.fc-corner-bg-9670c7 .fin-link{border-color:#9670c7;}
.fc-corner-bg-d25db5{color:#d25db5;height:17px;}
.fc-corner-bg-d25db5 .fin-link{border-color:#d25db5;}
.fc-corner-bg-e9599e{color:#e9599e;height:17px;}
.fc-corner-bg-e9599e .fin-link{border-color:#e9599e;}
.fc-corner-bg-b1846f{color:#b1846f;height:17px;}
.fc-corner-bg-b1846f .fin-link{border-color:#b1846f;}
.fc-corner-left .fc-event-inner {}
.more-rows,.more-rows>div{border-radius:10px;}
.more-rows>div>span{color:#fff;padding-left:3px;position:relative;}
.eve_fin-link>div>span{font-style:oblique;}
</style>
<script type='text/javascript' src='/js/jquery.js'></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type='text/javascript' src='/js/oa/fullcalendar.js'></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/oa/oaCalendarAdd.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/includeAllTextBoxList.js"></script>
<script type="text/javascript" src="/js/oa/itemTaskPublic.js"></script>
<script type="text/javascript" src="/js/crm/upload.vjs"></script>
<script type='text/javascript'> 
var textBoxObj;

/*加载控件*/
function loadTextBox(boxId){
	textBoxObj = new jQuery.TextboxList('#'+boxId, {unique: true, plugins: {autocomplete: {}}});
	textBoxObj.getContainer().addClass('textboxlist-loading');
	textBoxObj.plugins['autocomplete'].setValues($textBoxValues);
	textBoxObj.getContainer().removeClass('textboxlist-loading');
}


var hideEevent;	
var EeventNum;
var clickEvent;
$(document).ready(function() {

	//loadTextBox("clientName");
	var date = new Date();
	var d = date.getDate();
	var m = date.getMonth();
	var y = date.getFullYear();
	var typename = $("#typename").val();
		
	$('#calendar').fullCalendar({
		theme: true,
		header: {
			left: 'prevYear,nextYear,prev,next today',
			center: 'title',
			right: 'month,basicWeek,agendaDay'
		},
		monthNames: ["一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"],
        monthNamesShort: ["一月- ", "二月- ", "三月- ", "四月- ", "五月- ", "六月- ", "七月- ", "八月- ", "九月- ", "十月- ", "十一月- ", "十二月- "],
        dayNames: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
        dayNamesShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
		events:function(start,end) {
				$("#calendar").fullCalendar('removeEvents');
				var month=start.getMonth()+1;
				if(start.getMonth()<9){
					month = "0"+month;
				}
				var monthEnd=end.getMonth()+1;
				if(end.getMonth()<9){
					monthEnd = "0"+monthEnd;
				}
				var dayStart=start.getDate();
				if(start.getDate()<9){
					dayStart = "0"+dayStart;
				}
				var dayEnd=end.getDate();
				if(end.getDate()<9){
					dayEnd = "0"+dayEnd;
				}
				var startTime = start.getFullYear()+"-"+month+"-"+dayStart;
				var endTime = end.getFullYear()+"-"+monthEnd+"-"+dayEnd+" 23:59:59";
				#if("$!crmEnter" == "true")					
	          		var urlR = "Ajax?type=load&crmEnter=true&clientId=$!clientId"+"&start="+startTime+"&end="+endTime+"&date="+date+"&typename=客户日程";
	            #else
	            	//var urlR
	            	var urlR = "Ajax?type=load&crmEnter="+"&start="+startTime+"&end="+endTime+"&date="+date+"&typename="+typename;
	            #end
	            jQuery.ajax({
	            url: urlR,	                    
	            cache:false,
	            success:function(data) { 
	            	//分类数量
	            	
	            	var data1 = data.split("|")[1].substring(1,data.split("|")[1].length-1);
	            	var d1 = data1.split(",");  	             	
	            	for(var j=0;j<d1.length;j++){	            		
	            		if(d1[j] != null){
	            			$("#"+trimStr(d1[j].split("=")[0])).find('em').text(trimStr(d1[j].split("=")[1]));
	            		}
	            			            		
	            	}
	            	
	            	//日程数据
	            	var data0 = data.split("|")[0];
					var ds = data0.split(";");						  								
					for(i=0 ;i<ds.length;i++){							 
						var obj = jQuery.parseJSON(ds[i]);
											
						if(obj != null){																											
				            var eve={};
							eve.title = obj.title;
				            eve.id=obj.id; 	           
				            eve.start=obj.start;   
				            eve.end=obj.end;
				            eve.className = eve.id+"_fin-link ";
				            if(obj.delstatus=="1"){				            	
				            	if(obj.type.indexOf("会议")>-1){
				            		eve.url = "#/OASearchMeeting.do?operation=4;我的会议";
				            		//会议完成处理
				            		var endDate=new Date(eve.end);
				            		if(endDate<date){
				            			eve.className += "  eve_fin-link "; 
				            		}    				            		
				            	}
				            	/*if(obj.type.indexOf("待办")>-1){
				            		eve.url = "#/ToDoAction.do?operation=4&Flag=outIn&keyId="+obj.relationId+";我的待办";		            	
				            	}*/
				            	if(obj.type.indexOf("任务")>-1){
				            		eve.url = "#/OATaskAction.do?operation=5&taskId="+obj.relationId+";我的任务";
				            		
				            	}
				            	if(obj.type.indexOf("项目")>-1){
				            		eve.url = "#/OAItemsAction.do?operation=5&itemId="+obj.relationId+";我的项目";
				            		
				            	}
				            	if(obj.type.indexOf("日志")>-1){
				            		eve.url = "#/OAWorkLogAction.do?operation=5&workLogId="+obj.relationId+";我的日志";
				            	}				            				          
				            } 		
				           
				            if(obj.finishStatus=="1"){
				            	eve.className += "  eve_fin-link "; 
				            }     	           
				            if(obj.start.substring(0, 10) != obj.end.substring(0, 10)){
				            	eve.color="#"+obj.color;
				            	eve.className += "more-rows";
				            	
				            }else{
							    eve.className +="fc-corner-bg-"+obj.color ;
							}		            		            		            
				        	$('#calendar').fullCalendar('renderEvent', eve);
				        	$('.eve_fin-link').find("span.fc-event-title").append("<b class='fin-link'></b>");			      
						}																						
					}				                                   	                 
	              }
	          })
	      },			
		dragOpacity: .5,
		firstDay: 0,		
		editable: true,
		weekMode: 'liquid', 
		editable: false,
		buttonText:{
			 prev:     '昨天',
			 next:     '明天',
			 prevYear: '去年',
			 nextYear: '明年',
			 month:    '添加',
			 week:     '刷新',
			 day:      '列表界面'
		},	
		eventMouseover: function(calEvent, jsEvent, view) {				
			$(this).attr('title', "" + calEvent.title);
			$(this).css('cursor','pointer');
			$(this).css('font-weight', 'normal');	
		},
		eventMouseout: function(calEvent, jsEvent, view) {
			$(this).css('font-weight', 'normal');
		},
		
	});
	$(".p-block b.cbox").click(function(){
		$(this).addClass("checked").siblings("b.cbox").removeClass("checked");
		$("#color").val(($(this).attr('bg')));
	});
	// 重新注册 左上角三个按钮 ： 功能按钮  
    /*jQuery('.fc-button-prev').unbind('click'),  
    jQuery('.fc-button-next').unbind('click')
    jQuery('.fc-button-prev').bind('click', fnMthChange);  
    jQuery('.fc-button-next').bind('click', fnMthChange);
    function fnMthChange() {  
        alert(jQuery('#calendar').fullCalendar('getView').name);
    }  */
       	
	$(".up-color b.cbox").click(function(){
		$(this).addClass("checked").siblings("b.cbox").removeClass("checked");
		 var url = "/OACalendarAction.do?operation=15&delFlag=TYPE&typeId="+$("#changeColor").attr('vId')+"&color="+$(this).attr('bg');
		 jQuery.ajax({
	  	 type: "POST",
	   	 url: url,		   
	   	 success: function(msg){
	   	 	if(msg == "3"){
	   	 		location.href = "/OACalendarAction.do?operation=4";				   	 			 		
	   			}	 				    
	   		}
	   		
		});
	});
	$(document).click(function(event){
		var target = $(event.target);
		if(target.attr("id")!="showAdd" && target.parents("#showAdd").attr("id")!="showAdd" &&
		target.attr("id")!="calendarShow" && target.parents("#calendarShow").attr("id")!="calendarShow"){
			$("#calendarShow").hide();
		}	
		if(target.attr("id")!="calendar" && target.parents("#calendar").attr("id")!="calendar" &&
		target.attr("id")!="myDiv" && target.parents("#myDiv").attr("id")!="myDiv"){
			var nameId= $("#fc-day-content_"+EeventNum);
			jQuery(nameId).remove();
			$("#myDiv").hide();
		}
		if(target.attr("id")!="changeColor" && 
		target.parents("#changeColor").attr("id")!="changeColor" ){
			$("#changeColor").hide();
		}
	})
			
});
	
//弹出框双击回填内容





function fillData(datas){   
	if(hideAsynId == "normalId"){
		$("#"+hideFieldName).val(datas.split(";")[0]);
	    $("#"+hideFieldName+"Name").val(datas.split(";")[1]);
	    jQuery.close(hideAsynId);
	}else if("participantBoxId" == hideAsynId){
		textBoxObj.add(datas.split(";")[1],datas.split(";")[0]);
		jQuery.close(hideAsynId);
    }else{		
		newOpenSelectSearch(datas);
		jQuery.close('Popdiv');
	}
}


//日程列表
function turnList(){
	var url="/OACalendarAction.do?operation=4&queryFlag=List&crmEnter=$!crmEnter&clientId=$!clientId&addTHhead="+$("#addTHhead").val();
	//form.target="formFrame";
	location.href=url;
	//form.submit();
	//mdiwin(url,"日程列表");
}
//视图切换
function turnView(type){
	if(type=="week"){
	$("#type").attr("onclick","turnView('month')");
	$("#calendar").fullCalendar('changeView',"basicWeek");
	}else{
	$("#type").attr("onclick","turnView('week')");
	$("#calendar").fullCalendar('changeView',"month");
	}
}
</script>
</head>
<body >
#if("$!addTHhead" == "true")
#parse("./././body2head.jsp")
#end
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" name="form" action="/OACalendarAction.do"  target="formFrame">
<input type="hidden" name="typename" id="typename" value="$!typename"/>
<input type="hidden" name="color" id="color" value="da4c4b"/>
<input type="hidden" name="ends" id="ends" />
<input type="hidden" name="start" id="start" />
<input type="hidden" name="crmEnter" id="crmEnter" value="$!crmEnter" />
<input type="hidden" id="fulljsData" value="$globals.getDate()"/>
<input type="hidden" id="ppk" value="$ppk"/>
<input type="hidden" id="addTHhead" name="addTHhead" value="$addTHhead"/>
<div id='loading' style='display:none'>loading...</div>
<div style="overflow: hidden;overflow-y: auto;width:100%;padding-top: 8px; position:relative;" id="calendarId">

<script type="text/javascript">
	$("#calendarId").height($(window).height()-20);
</script>
<div class="fc-wrap" style="position:relative;">
	<b onclick="turnView('week')" class="hBtns" style="position:absolute;right:400px;top:20px;cursor:pointer;z-index:999;" id="type">视图切换</b>
	<b onclick="turnList();" class="hBtns" style="position:absolute;right:250px;top:20px;cursor:pointer;z-index:999;">日程列表</b>
	<div id='calendar' class="fc-left">
		
	</div>
	<div class="fc-right">
		<div class="fc-right-block">
			<p>
				分类
			</p>
			
			<ul class="fc-list-ul" id="fc-list-ul">
				#if("$!crmEnter" == "true")
					<li id="客户日程">							
						<b  id="is-cbox_客户日程" class="cbox cbox-color-15 checked" sel="t" ></b>
						<label  for="is-cbox" >客户日程</label>		
						<em style="color: red;"></em>		
					</li>
				#else
				<li id="allDays">
					<b id="is-cbox_all" class="cbox cbox-color-2 "></b>
					<label  for="is-cbox"  onclick="queryEvent('',this);" >全部日程</label>
					<em style="color: red;"></em>			
				</li>
				<li id="我的待办">
					<b onclick="moreEvent(this);" id="is-cbox_我的待办" class="cbox cbox-color-12 checked" sel="t" ></b>
					<label  for="is-cbox"  onclick="queryEvent('我的待办',this);" >我的待办</label>
					<em style="color: red;"></em>			
				</li>
				<li id="我的会议">
					<b onclick="moreEvent(this);" id="is-cbox_我的会议" class="cbox cbox-color-4 checked" sel="t" ></b>
					<label  for="is-cbox"  onclick="queryEvent('我的会议',this);" >我的会议</label>	
					<em style="color: red;"></em>		
				</li>
				<li id="我的日志">
					<b onclick="moreEvent(this);" id="is-cbox_我的日志" class="cbox cbox-color-5 checked" sel="t" ></b>
					<label  for="is-cbox"  onclick="queryEvent('我的日志',this);" >我的日志</label>	
					<em style="color: red;"></em>		
				</li>
				<li id="我的任务">
					<b onclick="moreEvent(this);" id="is-cbox_我的任务" class="cbox cbox-color-6 checked" sel="t" ></b>
					<label  for="is-cbox"  onclick="queryEvent('我的任务',this);" >我的任务</label>		
					<em style="color: red;"></em>	
				</li>
				<li id="客户日程" style="border-bottom: 1px dashed #d1d1d1;">
					<b onclick="moreEvent(this);" id="is-cbox_客户日程" class="cbox cbox-color-15 checked" sel="t" ></b>
					<label  for="is-cbox"  onclick="queryEvent('客户日程',this);" >客户日程</label>		
					<em style="color: red;"></em>	
				</li>
				<li id="默认分类">
					<b onclick="moreEvent(this);" id="is-cbox_默认分类" class="cbox cbox-color-9 checked" sel="t" ></b>
					<label for="is-cbox"  onclick="queryEvent('默认分类',this);" >默认分类</label>	
					<em style="color: red;"></em>		
				</li>
				#foreach($log in $!typeNames)
				#if("$!globals.get($!log,1)" != "默认分类" && "$!globals.get($!log,1)" != "客户日程" && "$!globals.get($!log,1)" != "默认分类" && "$!globals.get($!log,1)" != "我的会议"
				&& "$!globals.get($!log,1)" != "我的日志" && "$!globals.get($!log,1)" != "我的任务" && "$!globals.get($!log,1)" != "我的待办")
				<li id="$!globals.get($!log,1)">
					<b  onclick="moreEvent(this);" id="is-cbox_$!globals.get($!log,1)" class="cbox cbox-color-1 checked" style="background-color:#$!globals.get($!log,2) " sel="t" ></b>
					<label  for="is-cbox"  id="$!globals.get($!log,0)" onclick="queryEvent('$!globals.get($!log,1)',this);" >$!globals.get($!log,1)</label>
					<em style="color: red;"></em>
					<b class="bg-icons update-color" title="更改颜色"  onclick="showColor('$!globals.get($!log,0)');"></b>
					<b class="bg-icons del-color" title="删除" onclick="delTp('$!globals.get($!log,0)','$!globals.get($!log,1)');"></b>
				</li>
				#end
				#end
				<li class="add-type">
					<span id="showAdd" onclick="showAdd();">
						<b class="bg-icons"></b>
						添加分类	
					</span>
					<div class="add-cal-type" id="calendarShow" style="display: none">
						<input class="inp-txt" type="text" id="cardName" onkeydown="checkLenght(this,'cardName');" placeholder="输入分类名称" />
						<p>选择分类颜色</p>
						<div class="p-block" id="p-block">
							<b class="cbox cbox-color-1 checked"  bg='da4c4b'></b>
							<b class="cbox cbox-color-2" bg='ec6754'></b>
							<b class="cbox cbox-color-3" bg='e88b3b'></b>
							<b class="cbox cbox-color-4" bg='cb9e5d'></b>
							<b class="cbox cbox-color-5" bg='a8bb48'></b>
							<b class="cbox cbox-color-6" bg='68aa63'></b>
							<b class="cbox cbox-color-7" bg='358560'></b>
							<b class="cbox cbox-color-8" bg='3caaa9'></b>
							<b class="cbox cbox-color-9" bg='438ab4'></b>
							<b class="cbox cbox-color-10" bg='457198'></b>
							<b class="cbox cbox-color-11" bg='6c74a3'></b>
							<b class="cbox cbox-color-12" bg='9670c7'></b>
							<b class="cbox cbox-color-13" bg='d25db5'></b>
							<b class="cbox cbox-color-14" bg='e9599e'></b>
							<b class="cbox cbox-color-15" bg='b1846f'></b>
						</div>
						<div class="p-block">
							<input class="lf btn-add" type="button" value="添加分类" onclick="addC();" />
							<input class="lf btn-cel" type="button" value="取消"  onclick="delC();" />
						</div>
					<b class="arrow-point"></b>											
				</div>	
				</li>	
				#end
			</ul>
			
		</div>
	</div>
</div>
<div class="add-calendar" id="addCalendar" style="display:none;"></div>
<div class="addWrap" id="addTaskDiv" style="display:none;"></div>
<div class="addWrap" id="updateTaskDiv" style="display:none;"></div>
<div  id="updateToDoDiv" style="display:none;"></div>
<div class="up-color" style="display: none;" id="changeColor" vId="">			
		<div class="up-color-block">
			<b class="cbox cbox-color-1" bg='da4c4b'></b>
			<b class="cbox cbox-color-2" bg='ec6754'></b>
			<b class="cbox cbox-color-3" bg='e88b3b'></b>
			<b class="cbox cbox-color-4" bg='cb9e5d'></b>
			<b class="cbox cbox-color-5" bg='a8bb48'></b>
			<b class="cbox cbox-color-6" bg='68aa63'></b>
			<b class="cbox cbox-color-7" bg='358560'></b>
			<b class="cbox cbox-color-8" bg='3caaa9'></b>
			<b class="cbox cbox-color-9" bg='438ab4'></b>
			<b class="cbox cbox-color-10" bg='457198'></b>
			<b class="cbox cbox-color-11" bg='6c74a3'></b>
			<b class="cbox cbox-color-12" bg='9670c7'></b>
			<b class="cbox cbox-color-13" bg='d25db5'></b>
			<b class="cbox cbox-color-14" bg='e9599e'></b>
			<b class="cbox cbox-color-15" bg='b1846f'></b>
		</div>
		<b class="arrow-point"></b>
</div>			
</div>
</form>
<div id="hideBg" class="hideBg"></div>  
</body>
</html>