$(function(){
	$('.calendarDetail').live('click', function() {
		var relationId = $(this).attr("relationId");
		$.ajax({
			type: "POST",
			url: "/OACalendarAction.do",
			data: "operation=5&relationId="+relationId,
			success: function(msg){
				jQuery("#addCalendar").html(msg);
				jQuery("#addCalendar").show();
			}
		});
	})	
})

/*
 me:jquery对象
 count:闪烁的次数倍数
 调用例子:highlight($("#userName"),5);
*/
function highlight(me,count){
	var count = count-1;
	if(count<=0){
		me.css('background-color' , "");
		return;
	}
	if(count%2==0){
		me.css('background-color' , "#ffcccc");
	}else{
		me.css('background-color' , "");
	}
	setTimeout(function(){
		highlight(me,count);
	},200);
}

/*隐藏*/
function hideForm(){
	jQuery("#addCalendar").html("");
	jQuery("#addCalendar").hide();
}

 //选择日期
function selectDate(elementId){
	hideElementId = elementId;//存放元素名称
	WdatePicker({el:$dp.$(elementId),lang:'zh_CN',onpicked:dateChange})
}

//选择完日期后提交
function dateChange(){
	if(jQuery("#calendarStratTime").val() == ""){
		jQuery("#calendarStratTime").val(jQuery("#calendarFinishTime").val());
	}else if(jQuery("#calendarFinishTime").val() == ""){
		jQuery("#calendarFinishTime").val(jQuery("#calendarStratTime").val());
	}
}

/**提醒时间函数*/
function showTime(obj){
	var fg = jQuery("#bgg").attr('bg');
	if(fg=="t"){
		jQuery("#bgg").attr('bg','f');
		jQuery("#setTime").hide();
		jQuery(obj).removeClass("b-remind-h").attr("title","设置提醒");
	}else{
		jQuery("#bgg").attr('bg','t');
		jQuery("#setTime").show();
		jQuery(obj).addClass("b-remind-h").attr("title","取消提醒");
	}	
}

function hideForm(){
	jQuery("#addCalendar").html("");
	jQuery("#addCalendar").hide();
	var nameId= jQuery("#fc-day-content_"+EeventNum);
	jQuery(nameId).remove();
}



/*添加*/
 function saveForm(){
 	var stratTime=jQuery("#calendarStratTime").val();
   	var finishTime=jQuery("#calendarFinishTime").val();
   	var title=containTF(jQuery("#calendarTitle").val());
   	var finishStatus = jQuery("#finishStatus").val();
   	var taskId = jQuery("#calendarTaskId").val();
   	var relationId=jQuery("#relationId").val();//关联ID
   	if(finishStatus != undefined && finishStatus == "未完成"){
   		finishStatus = "1";
   	}else{
   		finishStatus = "0";
   	}
   if(stratTime>finishTime){
   		highlight(jQuery("#calendarFinishTime"),5);
   	 	return false;
   }
   
   	if(title==""){
   		highlight(jQuery("#calendarTitle"),5)
   		return false;
   	}   	
   	//shijian  
   	var alertTime="";
   	
   	if(jQuery("#bgg").attr('bg')=="t"){
   		var dayTime=jQuery("#dayTime").val();
   		var hours=jQuery("#hours").val();
   		var minutes=jQuery("#minutes").val();
   		
   		var alertTime = dayTime +" "+hours+":"+minutes+":00";
   		var alertDate=new Date(alertTime);
		var nowDate=new Date();
   		if(alertDate<nowDate){
			asyncbox.tips('提醒时间不得小于当前时间');
			return false;
		}
   	}
   	//保存 跟新
   	var clientId=jQuery("#calendarClientId").val();
   	var type = jQuery("#typeNames").val();  	
   	if(type == "客户日程"){
   		if(clientId == ""){
			highlight(jQuery("#calendarClientName"),5);
			return false;
		}
   	}
   	/*判断是否是有客户的有保存时时间控制*/
   	 	
   	if(clientId != undefined && clientId != "" && jQuery("#myFormId").val() =="" && type == "客户日程"){
   		//type = "客户日程";
   		var nowDate=new Date();
   		stratTime = stratTime+ " 23:59:00";
   		finishTime = finishTime+" 23:59:00"
   		var startDate=new Date(stratTime);
   		var finishDate=new Date(finishTime);		
   		if(startDate<nowDate || finishDate<nowDate){
			highlight(jQuery("#calendarStratTime"),5);
			highlight(jQuery("#calendarFinishTime"),5);
			return false;
		}
   	}
   	if(jQuery("#myFormId").val() !=""){  	   			
   		var url = "/OACalendarAction.do?operation=2&id="+$("#myFormId").val();   		
   	}else{
   		var url = "/OACalendarAction.do?operation=1";   			 
   	}	
	 jQuery.ajax({
  	 	type: "POST",url: url,
  	 	data:{
       	 		title:title,
       	 		stratTime:stratTime,
       	 		finishTime:finishTime,      	 		
       	 		type:type,     	 	     	 		
   				alertTime:alertTime,
   				clientId:clientId,
   				finishStatus:finishStatus,
   				relationId:relationId,
   				taskId:taskId			  	 	
       	 	},		   
   	 	success: function(msg){ 	 
	   	 	if(jQuery("#addCalendarBtn").length>0){
	   	 		asyncbox.tips('操作成功','success');
   	 			jQuery("#addCalendar").html("");
				jQuery("#addCalendar").hide();
   	 			var str = '<i relationId="'+relationId+'" class="calendarDetail">关联日程</i>';
   	 			jQuery("#addCalendarBtn").before(str);
   	 			jQuery("#addCalendarBtn").remove();
	   	 	}else{
	   	 			 	   	 		 	 	  	 		
   	 			if(msg != ""){
   	 				if(msg == "no"){
   	 					asyncbox.tips('操作失败!','error');  	 					
   	 				}else if(msg.indexOf("err")>-1){
   	 					asyncbox.tips(msg,'error');
   	 				}else{  
   	 					//添加数量 
   	 					jQuery("#addCalendar").hide();
   	 					asyncbox.tips('操作成功','success');	
	   	 				if(jQuery("#myFormId").val()!=""){
	   	 					jQuery("#calendar").fullCalendar('removeEvents', hideEevent);
	   	 				}  
	   	 				jQuery("#calendar").fullCalendar('removeEvents', "1_1");   
   	 					
   	 					if(jQuery("#myFormId").val() ==""){
   	 						var oldNum = $("#"+$("#typeNames").val()+">em").text();
	   	 				 	var new_num = parseFloat(oldNum)+parseFloat(1);
	   	 				 	jQuery("#"+$("#typeNames").val()+">em").text(new_num);
	   	 				 	//总量
						 	var oldall = $("#allDays>em").text();
						 	var newall = parseFloat(oldall)+parseFloat(1);
						 	jQuery("#allDays>em").text(newall);	
   	 					} 				 	
   	 				 	  	 				 											
						var eve={};
						 if(type.indexOf("客户")>-1){
							title = "客户:"+title;
						}else if(type.indexOf("任务")>-1){
							title = "任务:"+title;
						}
						eve.title = title;
			            eve.id=msg.split(";")[0];   
			            eve.start=stratTime;   
			            eve.end=finishTime; 
			            eve.className = eve.id+"_fin-link ";
		            	if(finishStatus=="1"){
		            		eve.className += "eve_fin-link "; 
		            	} 
			            if(stratTime != finishTime){
			            	eve.color="#"+msg.split(";")[1];
			            	eve.className += "more-rows";							            		            						            
			            }else{
			            	eve.className+="fc-corner-bg-"+msg.split(";")[1];
			            }		            
			        	jQuery('#calendar').fullCalendar('renderEvent',eve); 
			        	
			        	jQuery('.eve_fin-link').find("span.fc-event-title").append("<b class='fin-link'></b>");													
   	 				}  	 				 			  	 						     		           					
									
					var nameId= $("#fc-day-content_"+EeventNum);
					jQuery(nameId).remove();
   	 			}	
	   	 	}	   	 		
   	 	}
	});	 					
 }

/*删除*/
function delForm(typeName){
	if(!confirm("确定要删除？")){
		return false;
	} 
 	 var id = $("#myFormId").val();
 	 url = "/OACalendarAction.do?operation=3&id="+id;
	 jQuery.ajax({
	  	 type: "POST",
	   	 url: url,		   
	   	 success: function(msg){
	   	 	if(msg == "3"){
	   	 		//-数量 	
	   	 		var oldNum = $("#"+typeName+">em").text();
			 	var new_num = parseFloat(oldNum)-parseFloat(1);
			 	$("#"+typeName+">em").text(new_num);
			 	//总量
			 	var oldall = $("#allDays>em").text();
			 	var newall = parseFloat(oldall)-parseFloat(1);
			 	$("#allDays>em").text(newall);
			 	
	 			$("#addCalendar").hide(); 	
	 			$("#calendar").fullCalendar('removeEvents', hideEevent);
	 			asyncbox.tips('删除成功','success');		  	 		
	   		}else{
	   			asyncbox.tips('不能删除','error');	   			
	   		}
	   		jQuery('.eve_fin-link').find("span.fc-event-title").append("<b class='fin-link'></b>");	    
	   	}
	});	
}

/*链接客户*/
function openCtDetail(id,name){
	mdiwin("/CRMClientAction.do?operation=5&type=detailNew&keyId="+id,name);
}

/*打开新窗口**/
function openNewDiv(time){	
	$.ajax({
		type: "POST",
		url: "/OACalendarAction.do",
		data: "operation=6&type=addCalendar&calendarFlag=new&dateTime="+time+"&crmEnter="+$("#crmEnter").val(),
		success: function(msg){
			jQuery("#updateToDoDiv").hide();
			jQuery("#addCalendar").html(msg);
			jQuery("#addCalendar").show();			
		}
	});					
}

/*打开详情窗口*/
function openDetailDiv(eventId){
	var typeFlag = jQuery("."+eventId+"_fin-link").find('.fc-event-title').text();//判断是否待办
	if(typeFlag.indexOf("待办:")!=-1){
		$.ajax({
			type: "POST",
			url: "/ToDoAction.do",
			data: "operation=5&eventId="+eventId,
			success: function(msg){
				jQuery("#addCalendar").hide();
				jQuery("#updateToDoDiv").html(msg);							
				var yScroll = jQuery("."+eventId+"_fin-link").position().top;
				var xScroll	= jQuery("."+eventId+"_fin-link").position().left+195;
				jQuery(".add-calendar").css('top',yScroll+40);
				jQuery(".add-calendar").css('left',xScroll);
				jQuery("#updateToDoDiv").css('top',yScroll+40);
				jQuery("#updateToDoDiv").css('left',xScroll);
				jQuery("#updateToDoDiv").show();				
			}
		});	
	}else{
		$.ajax({
			type: "POST",
			url: "/OACalendarAction.do",
			data: "operation=6&type=addCalendar&calendarFlag=detail&eventId="+eventId+"&crmEnter="+$("#crmEnter").val(),
			success: function(msg){
				jQuery("#updateToDoDiv").hide();
				jQuery("#addCalendar").html(msg);
				jQuery("#addCalendar").show();			
			}
		});	
	}				
}

/*关闭待办的详情*/
function closeToDo(){
	jQuery("#updateToDoDiv").hide();
}

/*添加任务 **/
function addTask(){

	var taskId = $("#calendarTaskId").val();
	if(taskId != "" && taskId != undefined){
		var Id = taskId.split(";")[0];
		$.ajax({
			type: "POST",
			url: "/OATaskAction.do",
			data: "operation=7&taskId="+Id,
			success: function(msg){								
				$("#updateTaskDiv").html(msg);
				$("#updateTaskDiv").show();
				$(".addWrap").css("top","127px");
				$(".addWrap").css("left","35%");
				$("#hideBg").show();
				loadTextBox('participantInfo');
				publicDragDiv($(".addWrap"));	
				
			}
		});
	}else{
		$.ajax({
			type: "POST",
			url: "/OATaskAction.do",
			data: "operation=6",
			success: function(msg){								
				$("#addTaskDiv").html(msg);
				$("#addTaskDiv").show();
				$(".addWrap").css("top","127px");
				$(".addWrap").css("left","35%");
				$("#hideBg").show();
				loadTextBox('participantInfo');
				$("#title").val($("#calendarTitle").val());
				publicDragDiv($(".addWrap"));
									
			}
		});
	}		
}
/*提交任务*/
function taskSubmit(obj){
	
	var title = $.trim($("#title").val());//标题
	var remark = $.trim($("#remark").val());//内容
	var executor = $("#executor").val();//负责人

	
	var beginTime = $.trim($("#beginTime").val());//开始时间


	var endTime = $.trim($("#endTime").val());//结束时间
	var surveyor = $("#surveyor").val();//验收人


	var itemId = $("#itemId").val();//关联项目
	var operation = $(obj).attr("operation");//1:新增 2:修改
	var schedule = $("#schedule").val();//进度
	var taskClientId = $("#taskClientId").val();//客户
	var status = $("#uptStatus").val();//状态	var participant = $("#participantInfo").val();//参与人员
	var emergencyLevel = $("#emergencyLevel").val();//紧急程度
	var oaTaskType = $("#oaTaskType").val();//任务分类
	
	var affix = "";//附件
	//附件
	$("#affixuploadul_affix").find(":input[name='affix']").each(function(){
		affix += $(this).val() +";"; 
	})
	//alert($("#itemId").find("option:selected").attr("itemEndTime"))
	if(title==""){
		highlight($("#title"),5);
		return false;
	}
	
	//判断负责人是否为空

	if(executor==""){
		highlight($("#executor"),5);
		return false;
	}
	
	//判断截止时间是否小于今天
	if(endTime==""){	
		highlight($("#endTime"),5);
		return false;
	}
	var today=new Date();//取今天的日期  
	var beginDate = new Date(Date.parse(beginTime));  
	var endDate = new Date(Date.parse(endTime));  
	if(beginDate>endDate){  
		alert("执行截止时间不得小于开始时间"); 
		highlight($("#endTime"),5);
		return false;
	} 
	
	
	if(itemId!=""){
		var itemEndTime = $("#itemId").find("option:selected").attr("itemEndTime");//关联项目结束时间
		var itemEndDate = new Date(Date.parse(itemEndTime));  
		if(endDate>itemEndTime){  
			alert("执行截止时间不得大于关联项目的结束时间："+itemEndDate); 
			highlight($("#endTime"),5);
			return false;
		} 
	}
	
	$("#hideBg").hide();
	$.ajax({
		type: "POST",
		url: "/OATaskAction.do",
		data: "operation="+operation+"&keyId="+$(obj).attr("keyId")+"&title="+encodeURIComponent(title)+"&remark="+encodeURIComponent(remark)+"&beginTime="+beginTime+"&endTime="+endTime+"&surveyor="+surveyor+"&itemId="+itemId+"&executor="+executor+"&status="+status+"&schedule="+schedule+"&participant="+participant+"&emergencyLevel="+emergencyLevel+"&oaTaskType="+oaTaskType+"&affix="+encodeURIComponent(affix)+"&clientId="+encodeURIComponent(taskClientId),
		success: function(msg){
		hideAsynId="";
			if(msg=="error"){
				asyncbox.tips('添加失败!','error');
			}else{				
				var taskId = msg.split(";")[0];								
				if(operation == "1"){
					asyncbox.tips('添加成功','success');
					$("#calendarTaskId").val(taskId+";"+msg.split(";")[2]);					
					$("#appoint-other").text("已指派任务给:"+msg.split(";")[2]);
					$("#addTaskDiv").hide();
				}else{
					
					asyncbox.tips('修改成功','success');
					$("#calendarTaskId").val($(obj).attr("keyId")+";"+$("#executorName").val());					
					$("#appoint-other").text("已指派任务给:"+$("#executorName").val());
					$("#updateTaskDiv").hide();
				}			
				
			}
		}
	});
	
}

function closeLayer(obj){
	hideAsynId ="";
	$("#hideBg").hide();
	$(obj).parent().parent().hide();
}

/*特殊字符*/
function containTF(str){	
	return str.replace(new RegExp(";","gm"),"；");
}

/*验证长度*/
function checkLenght(obj,flag){
	var len = 7;
	if(getStringLength(obj.value)>2*len){
		obj.value=getTopic(obj.value,len,'');
	}
}


function openInputDate(obj){
	WdatePicker({lang:'zh_CN'});
}
function turnReapt(){
	var flag = $("#selector").val();
	if(flag=="1"){
		$("#point-block").hide();		
	}
	if(flag=="2"){
		$("#point-block").show();		
	}
}
function changeData(){
	if(document.getElementById("new_s").checked){	
		document.getElementById("toNews").style.display="";		
	}else{
		document.getElementById("toNews").style.display="none";		
	}
}

/*展示添加日历*/
function showAdd(){	
	$("#cardName").val("");
	$("#firstBg").addClass("checked").siblings("b.cbox").removeClass("checked");
	//$("#firstBg").addClass("checked");	
	$("#calendarShow").show();
	$("#cardName").focus();
}
/*查询日历**/
function queryEvent(name,obj){
	 $("li>b[id^='is-cbox_']").attr("sel","f").removeClass("checked");
	 
	 if($(obj).siblings("b").attr("sel") == "f"){
		$(obj).siblings("b").attr("sel","t").addClass("checked");
	 }else{
		$(obj).siblings("b").attr("sel","f").removeClass("checked");
	 }
	 if(name==""){
	 	
	 	 $("li>b[id^='is-cbox_']").attr("sel","t").addClass("checked");
	 }
	 var start=$("#start").val();
	 var end=$("#ends").val()+" 23:59:59";
	 var url ="Ajax?type=load"+"&start="+start+"&end="+end+"&typename="+name;
	 jQuery.ajax({
  	 type: "POST",
   	 url: url,	   
   	 success: function(mycalendar_str){
   	 	$("#calendar").fullCalendar('removeEvents');
   	 	if(mycalendar_str != ""){  
   	 			var data0 = mycalendar_str.split("|")[0];
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
				            		var endDate=new Date(eve.end);
				            		var date = new Date();
				            		if(endDate<date){
				            			eve.className += "eve_fin-link "; 
				            		}  			            		
				            	}
				            	if(obj.type.indexOf("待办")>-1){
				            		eve.url = "#/ToDoAction.do?operation=4&Flag=outIn&keyId="+obj.relationId+";我的待办";		            	
				            	}
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
				            	eve.className += " eve_fin-link  "; 
				            }    		           	           
				            if(obj.start.substring(0,10) != obj.end.substring(0,10)){
				            	eve.color="#"+obj.color;
				            	eve.className += "more-rows";	
				            }else{
							    eve.className+="fc-corner-bg-"+obj.color ;
							    
							}		      	            		            		            
			        	$('#calendar').fullCalendar('renderEvent', eve); 
			        	jQuery('.eve_fin-link').find("span.fc-event-title").append("<b class='fin-link'></b>");	       
					}
				}	 		
   			}		    
   		}
	});					
	//location.href = "/OACalendarAction.do?operation=4&typename="+name;
}
/*多条件过滤查询**/
function moreEvent(obj){
	
	if($(obj).attr("sel") == "f"){
		$(obj).attr("sel","t").addClass("checked");
	}else{
		$(obj).attr("sel","f").removeClass("checked");
	}
	var ids ="";
	$(".fc-list-ul>li").each(function(){
		if($("b",this).attr("sel") == "t"){
			ids += $("b",this).parents("li").attr('id')+";";					
   		}
	})
	if(ids==""){
		ids ="all";
	}
	 var start=$("#start").val();
	 var end=$("#ends").val()+" 23:59:59";;
	 var url ="Ajax?type=load"+"&start="+start+"&end="+end+"&typename="+ids;
	 jQuery.ajax({
  	 type: "POST",
   	 url: url,	   
   	 success: function(mycalendar_str){
   	 	$("#calendar").fullCalendar('removeEvents');
   	 	if(mycalendar_str != ""){  	 	
   	 			var data0 = mycalendar_str.split("|")[0];
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
				            	
				            	if(obj.type.indexOf("会议")>0){
				            		eve.url = "#/OASearchMeeting.do?operation=4;我的会议";
				            		var endDate=new Date(eve.end);
				            		var date = new Date();
				            		if(endDate<date){
				            			eve.className += "eve_fin-link "; 
				            		} 				            		
				            	}
				            	if(obj.type.indexOf("待办")>0){
				            		eve.url = "#/ToDoAction.do?operation=4&Flag=outIn&keyId="+obj.relationId+";我的待办";		            	
				            	}
				            	if(obj.type.indexOf("任务")>0){
				            		eve.url = "#/OATaskAction.do?operation=5&taskId="+obj.relationId+";我的任务";
				            		
				            	}
				            	if(obj.type.indexOf("项目")>0){
				            		eve.url = "#/OAItemsAction.do?operation=5&itemId="+obj.relationId+";我的项目";
				            		
				            	}
				            	if(obj.type.indexOf("日志")>0){
				            		eve.url = "#/OAWorkLogAction.do?operation=5&workLogId="+obj.relationId+";我的日志";
				            	
				            	}				            				          
				            } 
				            
				            if(obj.finishStatus=="1"){
				            	eve.className += " eve_fin-link  "; 
				            }   		           	           
				            if(obj.start.substring(0,10) != obj.end.substring(0,10)){
				            	eve.color="#"+obj.color;
				            	eve.className += "more-rows";	
				            }else{
							    eve.className+="fc-corner-bg-"+obj.color ;
							}		                  		            		            
			        	$('#calendar').fullCalendar('renderEvent', eve); 	    
			        	jQuery('.eve_fin-link').find("span.fc-event-title").append("<b class='fin-link'></b>");	 
					}
				}	 		
   			}		    
   		}
	});	
	//location.href = "/OACalendarAction.do?operation=4&typename="+ids;
}

/*添加日历*/
function addC(){
	var name = trimStr($("#cardName").val());
	if(name==""){		
		highlight($("#cardName"),5);
		return false;
	}
	if(name=="默认日历" || name=="我的任务" || name=="我的计划" || name=="我的会议" || name=="我的日志"){
		asyncbox.tips('已有此分类,不能添加此分类!','error');
		return false;
	}
	var color = $("#color").val();
	var url = "/OACalendarAction.do?operation=6";
	 jQuery.ajax({
  	 type: "POST",
   	 url: url,
   	 data:{
   	 	typeName:name,
		color:color
   	 },		   
   	 success: function(msg){
   	 	if(msg != ""){
   	 			var msgs = msg.split(";");
   	 			autoLi(msgs[0],msgs[1],name);	
   	 			$("#calendarShow").hide();  	 		
   			}		    
   		}
	});
}
/*取消日历*/
function delC(){
	$("#cardName").val("");

	$("#calendarShow").hide(); 
}

function autoLi(id,color,name){		
	var li = "<li id="+name+"><b onclick=\"moreEvent(this);\" id=\"is-cbox_"+name+"\" class=\"cbox cbox-color-1\" style=\"background-color:#"+color+" \" sel=\"f\"></b>"
			+"<label id=\""+id+"\" for=\"is-cbox\" onclick=\"queryEvent('"+name+"',this);\">"+name+"</label>"
			+"<em style='color:red;'>(0)</em>"
			+"<b class=\"bg-icons update-color\" title=\"更改颜色\"  onclick=\"showColor('"+id+"');\"></b>"
			+"<b class=\"bg-icons del-color\" title=\"删除\" onclick=\"delTp('"+id+"','"+name+"');\"></b>"
			+"</li>";
	var lil = "<option id=\"select_"+id+"\" value="+name+">"+name+"</option>"
	$(".add-type").before(li); 	
	$("#typeNames").append(lil);
    $("#calendarShow").hide();
}

/*颜色修改钱**/
function showColor(Id){
	var obj = $("#"+Id);
	$("#changeColor").show();
	$(obj).append($("#changeColor"));	
	$("#changeColor").attr('vId',Id);
	event.stopPropagation();
}

/*删除分类*/
function delTp(ID,name){
	if(!confirm("确定要删除？")){
		return false;
	}		 
	 var url = "/OACalendarAction.do?operation=3&delFlag=TYPE&typeId="+ID+"&type="+name;
	 jQuery.ajax({
  	 type: "POST",
   	 url: url,		   
   	 success: function(msg){
   	 	if(msg == "3"){
   	 		var li = $("#"+name);
   	 		var lil = $("#select_"+ID);				
   	 		$(li).hide();  
   	 		$(lil).remove(); 	 		
   		}
 		if(msg =="no"){
 			asyncbox.tips('有此类分类的日程,不能删除此分类!','error');
   			}		    
   		}
   		
	});
	event.stopPropagation();
}
/*根据值移除数组*/
Array.prototype.indexOf = function(val) {            
	for (var i = 0; i < this.length; i++) {
		if (this[i] == val) return i;
	}
	return -1;
};
Array.prototype.remove = function(val) {
	var index = this.indexOf(val);
	if (index > -1) {
		this.splice(index, 1);
	}
};

/*wyy*/
/*完成事情*/
function finishEvent(Id){	
	var status = jQuery("#finishStatus").val();
	if(status == "完成"){
		status = "1";
	}else{
		status = "0";
	}	
	$.ajax({
		type: "POST",
		url: "/OACalendarAction.do",
		data: "operation=2&updateType=status&id="+Id+"&finishStatus="+status,
		success: function(msg){	
			if(msg == "3"){		
				if(status == "1"){
					jQuery("#finishStatus").val("未完成");					
		            clickEvent.className.push('eve_fin-link');		          
		            $("#calendar").fullCalendar('updateEvent',clickEvent);
		            $('.eve_fin-link').find("span.fc-event-title").append("<b class='fin-link'></b>");	
					hideForm();
				}else{
					jQuery("#finishStatus").val("完成");
					clickEvent.className.remove('eve_fin-link');										
					$("#calendar").fullCalendar('updateEvent',clickEvent);
					$('.eve_fin-link').find("span.fc-event-title").append("<b class='fin-link'></b>");
					hideForm();
				}	
				asyncbox.tips('操作成功','success');
			}else{
				asyncbox.tips('操作失败','error');
			}					
		}
	});	
}
/*人员弹出框*/
var calendarClientName;
var calendarClientId;
var typeFlag;
function deptPopForJob(popname,Id,name,type){
	var urls = "/Accredit.do?popname=" + popname + "&inputType=radio";	
	calendarClientName=name;
	calendarClientId=Id;
	typeFlag=type;
	asyncbox.open({
		title : '请选择客户',
	    id : 'Popdiv',
	　　url : urls,
	　　width : 755,
	　	height : 430,
		btnsbar :jQuery.btn.OKCANCEL,
		callback : function(action,opener){	　　　　　	
　　　　　 if(action == 'ok'){	　　　　　　　
				var str = opener.strData;				
				newOpenSelectSearch(str);
　　　　　	}					 
　　　	 }
　	});
}

function newOpenSelectSearch(str){	
	if(str.length > 0 && str !=null){
		var datas = str.split(";");		
			if(typeFlag == "fillOld"){		
				$("#"+calendarClientId).val(datas[0]);
				$("#"+calendarClientName).remove();
				$("#"+calendarClientId).after("<div id=\""+calendarClientName+"\" onclick=\"openCtDetail('"+datas[0]+"','"+datas[1]+"');\" style=\"border: 1px solid #dfdfdf;background: #f9f9f9;border-radius: 4px;width: 180px;margin-left: 35px;cursor:pointer;text-align: left;\">"+datas[1]+"</div>");	
			}else{
				$("#"+calendarClientName).val(datas[1]);
				$("#"+calendarClientId).val(datas[0]);	
			}								
	}															
}
