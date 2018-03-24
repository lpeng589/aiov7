$(function(){
	/*列表改变项目状态
	$('.proBtn').live('click', function() {
		var itemId = $(this).parent().attr("id");
		changeItemStatus(itemId,$(this));
	});
	*/
	
	/*单击详情*/
	$(".proName").click(function(){
		var url = "/OAItemsAction.do?operation=5&itemId="+$(this).parents("li").attr("id")+"&addTHhead="+$("#addTHhead").val();
		if($("#addTHhead").val() == "true"){
			window.location.href  = url;
		}else{
			mdiwin(url,"项目:"+$(this).text());
		}		
	});
	
	/*双击详情*/
	$(".proListU li").dblclick(function(){
		var url = "/OAItemsAction.do?operation=5&itemId="+$(this).attr("id");
		mdiwin(url,"项目:"+$(this).find('.proName').text());
	})
	
	/*删除*/
	$(".del").click(function(){
		if(!confirm('删除项目时会把关联的任务一起删除，确定删除?')){
			return false;
		}else{
			$.ajax({
				type: "POST",
				url: "/OAItemsAction.do",
				data: "operation=3&itemId="+$(this).parent().attr("id"),
				success: function(msg){
					if(msg == "error"){
						asyncbox.tips('删除失败!','error');
					}else{
						asyncbox.tips('删除成功','success');
						setTimeout("window.location.reload()",1000);
					}
				}
			});
		}
	})
	
	/*项目详情四个tab切换*/
	$(".uNav li").click(function(){
		var tabName = $(this).attr("id");
		var itemId = $("#itemId").val();
		
		$(".mission").hide();
		$(".uNav li").removeClass("sel");
		
		$(this).addClass("sel")
		$("#"+tabName+"Show").show();
		if(tabName == "tabDiscuss"){
			$("#tabDiscussIframe").attr("src","/DiscussAction.do?tableName=OAItemsLog&f_ref="+$("#itemId").val()+"&parentIframeName="+$("#parentIframeName").val())
		}else if(tabName == "tabTask"){
			$.ajax({
				type: "POST",
				url: "/OATaskAction.do",
				data: "operation=5&type=detailByItem&itemId="+itemId,
				success: function(msg){
					$("#"+tabName+"Show").html(msg);
				}
			});
			
			//$("#taskIframe").attr("src","/OATaskAction.do?operation=5&type=detailByItem&itemId="+itemId)
		}
	})
	
	$(".participantMore").click(function(){
		$(".mission[id!='tabDiscussShow']").hide();
		$(".uNav li").removeClass("sel");
		
		$("#tabParticipant").addClass("sel")
		$("#tabParticipantShow").show();
		
	})
	
	/*设置提醒*/
	$("#showWarn li").click(function(e){
		var warnTime = $(this).attr("warnTime");
		var isSetTime = "false";
		if(warnTime == "set"){
			isSetTime = "true";
		}else{
			$.ajax({
				type: "POST",
				url: "/OAItemsAction.do",
				data: "operation=1&type=addWarn&isSetTime="+isSetTime+"&warnTime="+warnTime+"&keyId="+$("#itemId").val(),
				success: function(msg){
					if(msg=="error"){
						asyncbox.tips('设置失败!','error');
					}else{
						asyncbox.tips('设置成功','success');
					}
				}
			});
		}
	})
	
	/*删除参与者*/
	$('#showParticipant .b-del-t').live('click', function(e) {
		var itemId = $("#itemId").val();
		var employeeId = $(this).parent().attr("empId");
		var currObj = $(this);
		
		var participants = "";
		$("#showParticipant li[empId!='"+employeeId+"']").each(function(){
			if(typeof($(this).attr("empId"))!="undefined"  && $(this).attr("empId")!="addParticipant"){
				participants += $(this).attr("empId")+",";
			}
		})
		
		$.ajax({
			type: "POST",
			url: "/OAItemsAction.do",
			data: "operation=3&type=participant&itemId="+itemId+"&employeeId="+employeeId+"&participants="+participants,
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

/*添加项目*/
function addItem(){
	$("#addItemDiv").html($("#copyAddItem").html());
	$(".addWrap").css("top","127px");
	$(".addWrap").css("left","35%");
	$("#addItemDiv").show();
	$("#hideBg").show();
	publicDragDiv($(".addWrap"));
}

/*提交项目*/
function itemSubmit(obj){
	var curObj = obj;
	var title = $.trim($("#title").val());//标题
	var remark = $.trim($("#remark").val());//内容
	var beginTime = $.trim($("#beginTime").val());//开始时间
	var endTime = $.trim($("#endTime").val());//结束时间
	var schedule = $.trim($("#schedule").val());//项目进度
	var clientId = $("#clientId").val();//客户Id
	if(title==""){
		highlight($("#title"),5);
		return false;
	}
	
	
	//判断截止时间是否小于今天
	if(beginTime==""){
		asyncbox.tips("开始时间不能为空!"); 
		highlight($("#beginTime"),5);
		return false;
	}
	
	
	//判断截止时间是否小于今天
	if(endTime==""){
		asyncbox.tips("执行截止时间不能为空!"); 
		highlight($("#endTime"),5);
		return false;
	}else{
		var today=new Date();//取今天的日期  
		var beginDate = new Date(Date.parse(beginTime));  
		var endDate = new Date(Date.parse(endTime));  
		
		if(beginDate>endDate){  
			asyncbox.tips("执行截止时间不得小于开始时间"); 
			highlight($("#endTime"),5);
			return false;
		} 
		
		if(DateDiff(endDate,today)<0){  
			asyncbox.tips("截止时间不得小于今天");  
			highlight($("#endTime"),5);
			return false;
		}  
	}
	
	$(curObj).attr("onclick", "");//锁住按钮
	$.ajax({
		type: "POST",
		url: "/OAItemsAction.do",
		data: "operation=1&title="+encodeURIComponent(title)+"&remark="+encodeURIComponent(remark)+"&beginTime="+beginTime+"&endTime="+endTime+"&schedule="+schedule+"&clientId="+clientId,
		success: function(msg){
			if(msg=="success"){
				asyncbox.tips('添加成功','success');
				setTimeout("window.location.reload()",1000);
			}else{
				asyncbox.tips('添加失败!','error');
				//失败绑定按钮
				$(curObj).bind("click", function() {
					itemSubmit(this);
				});
			}
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
		var itemTitle = $("#itemTitle").val();
		$.ajax({
			type: "POST",
			url: "/OAItemsAction.do",
			data: "operation=2&type=participantsInfo&itemId="+$("#itemId").val()+"&employeeIds="+employeeIdsStr+"&itemTitle="+encodeURIComponent(itemTitle),
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
function showFeedback(itemId,obj){
	hideItemId = itemId;
	hideStatusBtnObj = obj;
	$.ajax({
		type: "POST",
		url: '/vm/oa/oaTask/addFeedback.jsp?isHead=true&itemEnter=true',
		success: function(msg){
			$("#addFeedbackDiv").html(msg)
		}
	});
	$("#addFeedbackDiv").show();
}


/*反馈意见确认*/
function feedbackItemSubmit(){
	var feedbackContent = $("#feedbackContent").val();
	if($.trim(feedbackContent)==""){
		asyncbox.tips('情况描述不能为空');
	}else{
		$("#feedbackSubmitBtn").attr("disabled","disabled");//锁住按钮
		changeItemStatus(hideItemId,feedbackContent,hideStatusBtnObj)
	}
}

/*修改状态,改为完成
itemId:项目ID
isDetail:true表示详情进入，成功后不刷新页面
*/
function changeItemStatus(itemId,feedbackContent,obj){
	var status = $(obj).attr("status");
	var isDetail = $(obj).attr("isDetail");
	$.ajax({
		type: "POST",
		url: "/OAItemsAction.do",
		data: "operation=2&type=changeStatus&id="+itemId+"&status="+status+"&feedbackContent="+encodeURIComponent(feedbackContent),
		success: function(msg){
			if(msg == "error"){
				asyncbox.tips('操作失败!','error');
				$("#feedbackSubmitBtn").attr("disabled","");//解除锁住按钮
			}else{
				//$(".pa-statu").remove();//先删除完成标签
				asyncbox.tips('操作成功!','success');
				if(isDetail=="true"){
					/*
					var str = "";
					if(status == "1"){
						str = "<span class='finish pr' status='2' isDetail='true' onclick=\"changeItemStatus('"+itemId+"',this);\"><b class='pa icons'></b>完成</span>";
					}else{
						
						str = "<span class='finish pr' status='1' isDetail='true' onclick=\"changeItemStatus('"+itemId+"',this);\"><b class='pa icons'></b>重新启动</span>";
						$("#wrap").append("<div class='pa pa-statu'></div>");
						
					$(obj).after(str);
					$(obj).remove();	
					}*/
					setTimeout("window.location.reload()",1000);
				}else{
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
		asyncbox.tips('时间不能为空!','error');
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
		url: "/OAItemsAction.do",
		data: "operation=1&type=addWarn&isSetTime=true&alterDay="+alterDay+"&alterHour="+alterHour+"&alterMinutes="+alterMinutes+"&keyId="+$("#itemId").val(),
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
	
	
	if(fieldName == "beginTime"){
		var beginDate = new Date(Date.parse(fieldVal));  
		var endDate = new Date($("#itemEndTime").val());  
		if(beginDate>endDate){  
			asyncbox.tips('开始时间不得大于结束时间');
			$(obj).val($(obj).attr("defvalue"));
			return false;
		} 
	}else if(fieldName == "endTime"){
		var beginDate = new Date($("#itemBeginTime").val());  
		var endDate = new Date(fieldVal);
		var nowDate = new Date();    
		if(DateDiff(endDate,nowDate)<0){
			asyncbox.tips('截止时间不得小于今天');
			$(obj).val($(obj).attr("defvalue"));
			return false;
		}
		if(beginDate>endDate){  
			asyncbox.tips('截止时间不得小于开始时间');
			$(obj).val($(obj).attr("defvalue"));
			return false;
		} 
	}
	$.ajax({
		type: "POST",
		url: "/OAItemsAction.do",
		data: "operation=2&type=updateSingle&fieldName="+fieldName+"&fieldVal="+encodeURIComponent(fieldVal)+"&itemId="+$("#itemId").val(),
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
						$("#itemTimeDiff").html("今天到期<i></i>");
					}else{
						$("#itemTimeDiff").html("剩余<i>"+time+"</i>天");
					}
				}
				$(obj).attr("title",fieldVal);//改变title值
				$(obj).attr("defvalue",fieldVal);//改变默认值
				window.frames["tabDiscussIframe"].window.location.reload();
			}
		}
	});	
}

