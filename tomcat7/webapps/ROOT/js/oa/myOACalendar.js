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
jQuery(document).ready(function() {	
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
	 var end=$("#end").val();
	 var url ="Ajax?type=load"+"&start="+start+"&end="+end+"&typename="+name;
	 jQuery.ajax({
  	 type: "POST",
   	 url: url,	   
   	 success: function(mycalendar_str){
   	 	$("#calendar").fullCalendar('removeEvents');
   	 	if(mycalendar_str != ""){   	 			
   	 			var ds = mycalendar_str.split(";");				
				for(i=0 ;i<ds.length;i++){
					var obj = jQuery.parseJSON(ds[i]);
					if(obj != null){
						var eve={};
							eve.title = obj.title;
				            eve.id=obj.id; 	           
				            eve.start=obj.start;   
				            eve.end=obj.end;
				            if(obj.delstatus=="1"){
				            	
				            	if(obj.type.indexOf("会议")>-1){
				            		eve.url = "#/OASearchMeeting.do?operation=4;我的会议";				            		
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
				            eve.className = eve.id+"_fin-link ";
				            if(obj.finishStatus=="1"){
				            	eve.className += " eve_fin-link  "; 
				            }    		           	           
				            if(obj.start != obj.end){
				            	eve.color="#"+obj.color;
				            	eve.className += "more-rows";	
				            }else{
							    eve.className+="fc-corner-bg-"+obj.color ;
							    
							}		      	            		            		            
			        	$('#calendar').fullCalendar('renderEvent', eve); 
			        	$('.eve_fin-link').append("<b class='fin-link'></b>");	       
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
	 var end=$("#end").val();
	 var url ="Ajax?type=load"+"&start="+start+"&end="+end+"&typename="+ids;
	 jQuery.ajax({
  	 type: "POST",
   	 url: url,	   
   	 success: function(mycalendar_str){
   	 	$("#calendar").fullCalendar('removeEvents');
   	 	if(mycalendar_str != ""){  	 	
   	 			var ds = mycalendar_str.split(";");				
				for(i=0 ;i<ds.length;i++){
					var obj = jQuery.parseJSON(ds[i]);
					if(obj != null){
						var eve={};
							
							eve.title = obj.title;
				            eve.id=obj.id; 	           
				            eve.start=obj.start;   
				            eve.end=obj.end;
				            if(obj.delstatus=="1"){
				            	
				            	if(obj.type.indexOf("会议")>0){
				            		eve.url = "#/OASearchMeeting.do?operation=4;我的会议";				            		
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
				            eve.className = eve.id+"_fin-link ";
				            if(obj.finishStatus=="1"){
				            	eve.className += " eve_fin-link  "; 
				            }   		           	           
				            if(obj.start != obj.end){
				            	eve.color="#"+obj.color;
				            	eve.className += "more-rows";	
				            }else{
							    eve.className+="fc-corner-bg-"+obj.color ;
							}		                  		            		            
			        	$('#calendar').fullCalendar('renderEvent', eve); 	    
			        	$('.eve_fin-link').append("<b class='fin-link'></b>");   
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

/**提醒时间函数*/
function showTime(obj){
	var fg = $("#bgg").attr('bg');
	if(fg=="t"){
		$("#bgg").attr('bg','f');
		$("#setTime").hide();
		$(obj).removeClass("b-remind-h").attr("title","设置提醒");
	}else{
		$("#bgg").attr('bg','t');
		$("#setTime").show();
		$(obj).addClass("b-remind-h").attr("title","取消提醒");
	}	
}
/*验证长度*/
function checkLenght(obj,flag){
	var len = 7;
	if(getStringLength(obj.value)>2*len){
		obj.value=getTopic(obj.value,len,'');
	}
}



/*人员弹出框*/
var fieldNames;
var fieldNIds;
function deptPopForJob(popname,fieldName,fieldNameIds){
	var urls = "/Accredit.do?popname=" + popname + "&inputType=radio";
	fieldNames=fieldName;
	fieldNIds=fieldNameIds;
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
		
			$("#calendarClientName").val(datas[1]);
			$("#calendarClientId").val(datas[0]);			
		
	}	
														
}






/*是否含有特殊字符*/
function containTF(str){
	if(str.indexOf(";")>-1)return false ;
	return true ;
}

/*完成事情*/
function finishEvent(Id){
	alert(id)	
	var status = $("#finishStatus").val();
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
		alert(msg)
			if(msg == "3"){
				if(status == "1"){
					$("#finishStatus").val("未完成");			
					$('.'+Id+'_fin-link').addClass(' eve_fin-link').append("<b class='fin-link'></b>");
				}else{
					$("#finishStatus").val("完成");
					$('.'+Id+'_fin-link').removeClass('eve_fin-link').find('.fin-link').remove();
				}	
				asyncbox.tips('操作成功','success');
			}else{
				asyncbox.tips('操作失败','error');
			}					
		}
	});	
}

function hideForm(){
	$("#addCalendar").hide();
	//$("#myDiv").hide();
	var nameId= $("#fc-day-content_"+EeventNum);
	$(nameId).remove();
	//$("#calendar").fullCalendar('removeEvents', "1_1");
	//$('.eve_fin-link').append("<b class='fin-link'></b>");
}


/*打开新窗口**/
function openNewDiv(time){	
	$.ajax({
		type: "POST",
		url: "/OACalendarAction.do",
		data: "operation=6&type=addCalendar&calendarFlag=new&dateTime="+time+"&crmEnter="+$("#crmEnter").val(),
		success: function(msg){
			jQuery("#addCalendar").html(msg);
			$("#addCalendar").show();			
		}
	});					
}

/*打开详情窗口*/
function openDetailDiv(eventId){	
	$.ajax({
		type: "POST",
		url: "/OACalendarAction.do",
		data: "operation=6&type=addCalendar&calendarFlag=detail&eventId="+eventId+"&crmEnter="+$("#crmEnter").val(),
		success: function(msg){
			jQuery("#addCalendar").html(msg);
			$("#addCalendar").show();			
		}
	});					
}

/*添加任务 **/
function addTask(){

	var taskId = $("#taskId").val();	
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
	
	var status = $("#uptStatus").val();//状态
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
		data: "operation="+operation+"&keyId="+$(obj).attr("keyId")+"&title="+encodeURIComponent(title)+"&remark="+encodeURIComponent(remark)+"&beginTime="+beginTime+"&endTime="+endTime+"&surveyor="+surveyor+"&itemId="+itemId+"&executor="+executor+"&status="+status+"&schedule="+schedule,
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