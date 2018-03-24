$(function(){

	/*任务详情添加切换*/
	$(".add-span").click(function(){
		$(this).hide().siblings(".d-task").show();	
		$("#fastTitle").focus();
	});
	
	/*任务详情取消切换*/
	$(".cancel-btn").click(function(){
		$(".d-task").hide().siblings(".add-span").show();	
	});
	
	/*修改任务*/
		$(".updateTask").click(function(){

		//true表示CRM进入
		var crmTaskEnter = "";
		if(typeof($("#crmTaskEnter"))!="undefined"){
			crmTaskEnter = $("#crmTaskEnter").val();
		}
		$.ajax({
			type: "POST",
			url: "/OATaskAction.do",
			data: "operation=7&taskId="+$(this).parent().attr("id")+"&crmTaskEnter="+crmTaskEnter,
			success: function(msg){
				$("#updateTaskDiv").html(msg);
				$("#updateTaskDiv").show();
				$(".addWrap").css("top","10px");
				$(".addWrap").css("left","35%");
				publicDragDiv($(".addWrap"));
				
				var hideParticipantInfo = $("#hideParticipantInfo").val();
				loadTextBox('participantInfo');
				
				//加载分享人数据
				if(hideParticipantInfo!=""){
					var arrStr = hideParticipantInfo.split(";");
					for(var i=0;i<arrStr.length;i++){
						if(arrStr[i]!=""){
							textBoxObj.add(arrStr[i].split(":")[1],arrStr[i].split(":")[0]);
						}
					}
				}
					
					
				$("#hideBg").height($(".wrap").height()+10).show();
			}
		});	
		stopEvent();
	});
	
	/*删除*/
	$(".del").click(function(){
		if(!confirm('确定删除?')){
			return false;
		}else{
			$.ajax({
				type: "POST",
				url: "/OATaskAction.do",
				data: "operation=3&taskId="+$(this).parent().attr("id"),
				success: function(msg){
					if(msg == "error"){
						asyncbox.tips('删除失败!','error');
					}else{
						asyncbox.tips('删除成功','success');
						setTimeout("form.submit()",1000);
					}
				}
			});
		}
		stopEvent();
	})
	
	/*双击详情*/
	$(".mdiwin-li").dblclick(function(){
		var taskId = $(this).attr("id");
		var taskName = $(this).find(".titleName").text();
		taskDetail(taskId,taskName);
	})
	
	/*单击详情*/
	$(".titleName").click(function(){
		var taskId = $(this).parents("li").attr("id");
		var taskName = $(this).text();
		taskDetail(taskId,taskName);
	})
	
	
	/*项目详情四个tab切换*/
	$(".uNav li").click(function(){
		var tabName = $(this).attr("id");
		var itemId = $("#itemId").val();
		
		$(".mission[id!='tabDiscussShow']").hide();
		$(".uNav li").removeClass("sel");
		
		$(this).addClass("sel")
		$("#"+tabName+"Show").show();
		
	})
	
	
	$(".participantMore").click(function(){
		$(".mission[id!='tabDiscussShow']").hide();
		$(".uNav li").removeClass("sel");
		
		$("#tabParticipant").addClass("sel")
		$("#tabParticipantShow").show();
		
	})
	
	
	/*列表改变项目状态
	$(".proBtn[id!='participantFlag']").live('click', function() {
		if($(this).attr("status") != "surveyoring"){
			var taskId = $(this).parent().attr("id");
			changeTaskStatus(taskId,$(this));
		}
	});
	*/
	
	/**/
	$('.surveyorOp').live('click', function() {
		var taskId = $(this).parents("li").attr("id");
		showFeedback(taskId,$(this));
	})
	
	/*删除参与者*/
	$('#showParticipant .b-del-t').live('click', function() {
		var taskId = $("#taskId").val();
		var employeeId = $(this).parent().attr("empId");
		var currObj = $(this);
		var participants = "";
		$("#showParticipant li[empId!='"+employeeId+"']").each(function(){
			if(typeof($(this).attr("empId"))!="undefined" && $(this).attr("empId")!="addParticipant"){
				participants += $(this).attr("empId")+",";
			}
		})
		
		$.ajax({
			type: "POST",
			url: "/OATaskAction.do",
			data: "operation=3&type=participant&taskId="+taskId+"&employeeId="+employeeId+"&participants="+participants,
			success: function(msg){
				if(msg == "error"){
					asyncbox.tips('删除失败','error');
				}else{
					asyncbox.tips('操作成功','success');
					$(currObj).parent().remove();
					updPageParticipantInfo();//更新头部参与人信息
				}
			}
		});		
		e = e||event; stopFunc(e);//阻止冒泡
	});
})

/*提交任务*/
function taskSubmit(obj){
	var curObj = obj;
	var title = $.trim($("#title").val());//标题
	var remark = $.trim($("#remark").val());//内容
	var executor = $("#executor").val();//负责人	var beginTime = $.trim($("#beginTime").val());//开始时间	var endTime = $.trim($("#endTime").val());//结束时间
	var surveyor = $("#surveyor").val();//验收人	var itemId = $("#itemId").val();//关联项目
	var clientId = $("#taskClientId").val();//关联客户
	var operation = $(obj).attr("operation");//1:新增 2:修改
	var schedule = $("#schedule").val();//进度
	var status = $("#uptStatus").val();//状态
	var emergencyLevel = $("#emergencyLevel").val();//紧急程度
	var oaTaskType = $("#oaTaskType").val();//任务分类

	var participant = $("#participantInfo").val();//参与人员
	var affix = "";//附件
	//asyncbox.tips($("#itemId").find("option:selected").attr("itemEndTime"))
	if(title==""){
		highlight($("#title"),5);
		return false;
	}
	
	if(!containSC(title)){
		 alert("任务名称不能含有特殊字符(' \" | ; \\)") ;
		 highlight($("#title"),5);
		return false;
	 }
	 if(!containSC(remark)){
		 alert("详情描述不能含有特殊字符(' \" | ; \\)") ;
		 highlight($("#remark"),5);
		return false;
	 }
	
	
	//判断截止时间是否小于今天
	if(beginTime==""){
		asyncbox.tips("开始时间不能为空!"); 
		highlight($("#beginTime"),5);
		return false;
	}
	
	var beginDate = new Date(beginTime);  
	var endDate = new Date(endTime);  
		
	//判断截止时间是否小于今天
	if(endTime==""){
		asyncbox.tips("执行时间不能为空!"); 
		highlight($("#endTime"),5);
		return false;
	}else{
		var today=new Date();//取今天的日期  
		if(beginDate>endDate){  
			asyncbox.tips("截止时间不得小于开始时间"); 
			highlight($("#endTime"),5);
			return false;
		} 
		
		if(DateDiff(endDate,today)<0){  
			asyncbox.tips("截止时间不得小于今天");  
			highlight($("#endTime"),5);
			return false;
		}  
		
		if(itemId!=""){
			var itemEndTime = $("#itemId").find("option:selected").attr("itemEndTime");//关联项目结束时间
			var itemEndDate = new Date(itemEndTime);  
			if(endDate>itemEndDate){  
				asyncbox.tips("任务的截止时间不得大于关联项目的结束时间："+itemEndTime); 
				highlight($("#endTime"),5);
				return false;
			} 
		}
	}
	
	if($("#relateClientId").attr("class").indexOf("notNull")>-1&&clientId==""){
			asyncbox.tips("关联客户不能为空"); 
			highlight($("#clientIdName"),5);
			return false;
	}
	
	
	
	//附件
	$("#affixuploadul_affix").find(":input[name='affix']").each(function(){
		affix += $(this).val() +";"; 
	})
	
	//参与人
	if(participant!=""){
		participant = participant+",";
	}
	
	$(curObj).attr("onclick", "");//锁住按钮
	$.ajax({
		type: "POST",
		url: "/OATaskAction.do",
		data: "operation="+operation+"&keyId="+$(obj).attr("keyId")+"&title="+encodeURIComponent(title)+"&remark="+encodeURIComponent(remark)+"&beginTime="+beginTime+"&endTime="+endTime+"&surveyor="+surveyor+"&itemId="+itemId+"&executor="+executor+"&status="+status+"&schedule="+schedule+"&clientId="+clientId+"&participant="+participant+"&emergencyLevel="+emergencyLevel+"&oaTaskType="+oaTaskType+"&affix="+encodeURIComponent(affix),
		success: function(msg){
			var alertName = "添加";
			if(operation == "2"){
				alertName = "修改";
			}
			if(msg=="error"){
				asyncbox.tips(alertName+'失败!','error');
				
				//失败绑定按钮
				$(curObj).bind("click", function() {
					taskSubmit(this);
				});
			}else{
				asyncbox.tips(alertName+'成功','success');
				if($("#tableName").val() == "OAWorkLog"){
					//根据表名区分操作，我的日志调用oaWorkLog.js的createTaskSuccess()方法
					createTaskSuccess(msg);								
				}else if(typeof($("#ModuleId").val())!="undefined"){
					//客户列表添加任务成功后,关闭弹出框
					$(".addWrap").html("");
					$(".addWrap").hide();
				}else{
					setTimeout("form.submit()",1000);
				}
			}
		}
	});
	
}


/*快速添加任务方法&添加任务打开*/
function addTask(){
	//true表示CRM进入
	var crmTaskEnter = "";
	if(typeof($("#crmTaskEnter"))!="undefined"){
		crmTaskEnter = $("#crmTaskEnter").val();
	}
	$.ajax({
		type: "POST",
		url: "/OATaskAction.do",
		data: "operation=6&crmTaskEnter="+crmTaskEnter,
		success: function(msg){								
			$("#addTaskDiv").html(msg);
			$(".addWrap").css("top","10px");
			$(".addWrap").css("left","35%");
			$("#addTaskDiv").show();
			loadTextBox('participantInfo');
			publicDragDiv($(".addWrap"));
			$("#hideBg").height($(".wrap").height()+10).show();					
		}
	});
}

/*根据职员ids,更新参与人员字段并封装显示数据*/
function updParticipantsInfo(employeeIds){

	var employeeIdsStr = "";//封装参与者信息
	var strArr = employeeIds.split(",");
	for(var i=0;i<strArr.length;i++){
		if(strArr[i]!="" && $(".mesOnline[empId='"+strArr[i]+"']").length==0){
			employeeIdsStr += strArr[i]+",";
		}
	}
	if("" == employeeIdsStr){
		asyncbox.tips('参与人已存在!');
	}else{
		var taskTitle = $("#taskTitle").val();
		$.ajax({
			type: "POST",
			url: "/OATaskAction.do",
			data: "operation=2&type=participantsInfo&taskId="+$("#taskId").val()+"&employeeIds="+employeeIdsStr+"&taskTitle="+encodeURIComponent(taskTitle),
			success: function(msg){
				if(msg == "error"){
					asyncbox.tips('添加失败!','error');
				}else{
					$("#showParticipant li:last").before(msg);//显示参与者	
					updPageParticipantInfo();//更新头部参与人信息	
					
					asyncbox.tips('添加成功!','success');
				}
			}
		});	
	}


}

/*显示反馈弹出框*/
function showFeedback(taskId,obj){
	hideTaskId = taskId;
	hideStatusBtnObj = obj;
	$.ajax({
		type: "POST",
		url: '/vm/oa/oaTask/addFeedback.jsp?isHead=true',
		success: function(msg){
			$("#addFeedbackDiv").html(msg)
		}
	});
	$("#addFeedbackDiv").show();
}

/*任务详情头部状态按钮反馈意见确认*/
function feedbackSubmit(){
	var feedbackContent = $("#feedbackContent").val();
	
	if($.trim(feedbackContent)==""){
		asyncbox.tips('情况描述不能为空');
	}else{
		$("#feedbackSubmitBtn").attr("disabled","disabled");//锁住按钮
		changeTaskStatus(hideTaskId,feedbackContent,hideStatusBtnObj)
	}
}


/*修改任务状态,
itemId:项目ID
isDetail:true表示详情进入，成功后不刷新页面
*/
function changeTaskStatus(taskId,feedbackContent,obj){
	var status = $(obj).attr("status");
	var isDetail = $(obj).attr("isDetail");
	$.ajax({
		type: "POST",
		url: "/OATaskAction.do",
		data: "operation=2&type=changeStatus&taskId="+taskId+"&status="+status+"&feedbackContent="+encodeURIComponent(feedbackContent),
		success: function(msg){
			if(msg == "error"){
				asyncbox.tips('操作失败!','error');
				$("#feedbackSubmitBtn").attr("disabled","");//解除按钮
			}else{
				$(".pa-statu").remove();//先删除完成标签
				asyncbox.tips('操作成功!','success');
				if(isDetail=="true"){
					/*
					if(status == "3"){
						str = "<span class='finish pr'>验收中</span>";
						$(obj).after(str);
						$(obj).remove();
					}else{
						setTimeout("window.location.reload()",1000);
					}*/
					setTimeout("window.location.reload()",1000);
				}else{
					//任务列表刷新
					setTimeout("form.submit()",1000);
				}
				
			}
		}
	});
}

//关闭层
function closeLayer(obj){
	$(obj).parent().parent().hide();
	$(obj).parent().parent().html("");
	$("#hideBg").hide();
}


/*设置提醒*/
function setAlter(){
	var alterDay = $("#alterDay").val();
	var alterHour = $("#alterHour").val();
	var alterMinutes = $("#alterMinutes").val();


	if("" == alterDay){
		asyncbox.tips('时间不得为空');
		return false;
	}
	//判断时间
	var timeStr = alterDay +" "+alterHour+":"+alterMinutes+":00";
	
	var alertDate=new Date(timeStr);
	var nowDate=new Date();
	
	if(alertDate<nowDate){
		asyncbox.tips('提醒时间不得小于当前时间');
		return false;
	}
	$.ajax({
		type: "POST",
		url: "/OATaskAction.do",
		data: "operation=1&type=addWarn&isSetTime=true&alterDay="+alterDay+"&alterHour="+alterHour+"&alterMinutes="+alterMinutes+"&keyId="+$("#taskId").val(),
		success: function(msg){
			if(msg=="error"){
				asyncbox.tips('设置失败!','error');
			}else{
				$("#alertTimeShow").html(msg)
				$("#show_t").hide();
				asyncbox.tips('设置成功','success');
			}
		}
	});
}



/*修改单个字段*/
function updSingleField(obj){
	var fieldName = $(obj).attr("fieldName");
	var fieldVal = $(obj).val();
	
	if("" == $.trim(fieldVal) && "title" == fieldName){
		asyncbox.tips("标题不能为空");
		$(obj).val($(obj).attr("defvalue"));//还原默认值
		return false;
		
	}
	if("title" == fieldName){
		if(!containSC($.trim(fieldVal))){
			asyncbox.tips("标题不能含有特殊字符(' \" | ; \\)");
			$(obj).val($(obj).attr("defvalue"));//还原默认值
			return false;
		}
	}	
	
	
	if(fieldName == "beginTime"){
		var beginDate = new Date(fieldVal);  
		var endDate = new Date($("#taskEndTime").val());  
		
		if(beginDate>endDate){  
			asyncbox.tips("开始时间不得大于结束时间"); 
			$(obj).val($(obj).attr("defvalue"));
			return false;
		} 
	}else if(fieldName == "endTime"){
		var beginDate = new Date($("#taskBeginTime").val());  
		var endDate = new Date(fieldVal);  
		var nowDate = new Date();  
		if(DateDiff(endDate,nowDate)<0){
			asyncbox.tips('截止时间不得小于今天');
			$(obj).val($(obj).attr("defvalue"));
			return false;
		}
		if(beginDate>endDate){  
			asyncbox.tips("执行截止时间不得小于开始时间"); 
			$(obj).val($(obj).attr("defvalue"));
			return false;
		} 
	}
	
	$.ajax({
		type: "POST",
		url: "/OATaskAction.do",
		data: "operation=2&type=updateSingle&fieldName="+fieldName+"&fieldVal="+encodeURIComponent(fieldVal)+"&taskId="+$("#taskId").val(),
		success: function(msg){
			if(msg=="error"){
				asyncbox.tips('修改失败!','error');
			}else{
				asyncbox.tips('修改成功','success');
				
				//成功后调用还原按钮并更改某些信息
				if(fieldName == "endTime"){
					//根据修改时间,修改相差天数
					var updDate = new Date(fieldVal);//修改的时间
					var nowDate = new Date($("#today").val());//今天
					var time = DateDiff(updDate,nowDate);//获取相差天数
					if(time == 0){
						$("#taskTimeDiff").html("今天到期<i></i>");
					}else{
						$("#taskTimeDiff").html("剩余<i>"+time+"</i>天");
					}
					
					$("#fastEndTime").attr("defvalue",fieldVal);
					$("#fastEndTime").val(fieldVal);
				}
				$(obj).attr("title",fieldVal);//改变title值
				$(obj).attr("defvalue",fieldVal);//改变默认值
				window.frames["tabDiscussIframe"].window.location.reload();
			}
		}
	});	
}

function itemDetail(obj){
	var url = "/OAItemsAction.do?operation=5&itemId="+$(obj).attr("itemId");
	mdiwin(url,$(obj).text());
}

/*改变任务人员*/
function changeTaskMember(employeeId,employeeName,fieldName){
	var taskId = $("#taskId").val();
	$.ajax({
		type: "POST",
		url: "/OATaskAction.do",
		data: "operation=2&type=updateSingle&fieldName="+fieldName+"&fieldVal="+employeeId+"&taskId="+taskId,
		success: function(msg){
			if("error" == msg){
				asyncbox.tips('修改失败!','error');
			}else{
				asyncbox.tips('修改成功','success');
				setTimeout("window.location.reload()",1000);
			}
		}
	});	
}

/*任务详情*/
function taskDetail(taskId,taskName){	
	var addTHhead = $("#addTHhead").val();
	if(addTHhead == "true"){
		window.location='/OATaskAction.do?operation=5&addTHhead=true&taskId='+taskId;
	}else{
		var nameStr = "任务:"+taskName;
		mdiwin('/OATaskAction.do?operation=5&taskId='+taskId,nameStr);
	}	
}

function showBatchTransfer(){
	$("#batchTransferDiv").show();
}

function closeBatchTransfer(){
	$("#batchTransferDiv").hide();
}

function batchTransfer(obj){
	var transferTo = $("#transferTo").val();
	var transferExecutor = $("#transferExecutor").val();
	
	if(transferExecutor == ""){
		asyncbox.tips('处理人不能为空');
		return false;
	}
	
	if(transferTo == ""){
		asyncbox.tips('移交给不能为空');
		return false;
	}
	
	
	
	var transferType = $("#transferType").val();
	$.ajax({
		type: "POST",
		url: "/OATaskAction.do",
		data: "operation=2&type=batchTransfer&transferTo="+transferTo+"&transferExecutor="+transferExecutor+"&transferType="+transferType,
		success: function(msg){
			if(msg=="success"){
				asyncbox.tips('移交成功','success');
			}else{
				asyncbox.tips('移交失败!','error');
			}
			setTimeout("form.submit()",1000);
		}
	});	
}

/*是否含有特殊字符*/
function containSC(str){
	if(str.indexOf("'")>-1)return false ;
	if(str.indexOf("\"")>-1)return false ;
	if(str.indexOf("|")>-1)return false ;
	if(str.indexOf(";")>-1)return false ;
	if(str.indexOf("\\")>-1)return false ;
	return true ;
}