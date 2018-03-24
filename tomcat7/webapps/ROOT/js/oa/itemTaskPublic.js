/*存放我的项目与我的任务共用方法*/
var pointTaskId;//指派时存放任务ID
var pointByObj;//指派对象
var hideElementId;//存放隐藏的日期id,用于点击后操作

$(function(){
	updPageParticipantInfo();//先加载头部参与人信息
	
	$(document).on("click", function(){
		$("#show_t").hide();//隐藏提醒选项
		$("#pointEmpDiv").hide();//隐藏指派人DIV
	});
	
	/*拖动需要*/
	$(document).bind("mouseup",function(){
		$(this).unbind("mousemove");
	});
	
	//处理添加修改弹出层点击input框不拖动方法
	$('.addWrap :input').live('mouseover mouseout', function(event) {
		if (event.type == 'mouseover') {
			$(".addWrap").unbind("mousedown");
		}else {
		    publicDragDiv($(".addWrap"))
		}
	});	
		
	$("select").on("click", function(e){
		//下拉框阻止冒泡 
		e = e||event; stopFunc(e);//阻止冒泡
	});
	
	//高级查询
	$(".highSearch").on("click", function(e){
		$(".tag-pa").show();
	});
	
	//高级查询取消
	$(".tag-su .qx").on("click", function(e){
		$(".tag-pa").hide();
	});
	
	//高级查询重置
	$(".tag-su .cz").on("click", function(e){
		$(".tag-su :input[class!='btn-add']").val("");
		$("#searchKeyWord").val("");
	});
	
	//显示提醒
	$("#setWarn").click(function(e){
		$("#show_t").show();
		e = e||event; stopFunc(e);//阻止冒泡
	})
	
	//加载页面时,判断当前选择的tab,选中的加上class="sel"
	var tabSelectName = $("#tabSelectName").val();
	$("#"+tabSelectName).addClass("sel");
	
	/*tab导航查询*/
	$(".tagU li").click(function(){
		var tabSelectName = $(this).attr("id");
		$("#tabSelectName").val(tabSelectName);
		
		if(tabSelectName.indexOf("surveyor")>-1){
			//验收中默认值为3,
			$("#searchStatus").val("3");
		}else{
			//其余状态都唯一
			$("#searchStatus").val("1");
		}
		$("#hasSearchCondition").val(checkSearchCondition());
		form.submit();	
	})
	
	/*列表查询状态*/
	$(".wp-status i").click(function(e){
		$("#searchStatus").val($(this).attr("status"))
		form.submit();
	})
	
	/*项目详情,任务详情-任务 点击进入详情*/
	$('.tContent').live('click', function() {
		var url = "/OATaskAction.do?operation=5&taskId="+$(this).parent().attr("id");
		mdiwin(url,"任务:"+$(this).text());
	});
	
	/*
	//状态改变按钮 执行中-完成
	$(".running").mouseover(function(){
		$(this).html("完成").removeClass("running").addClass("achieve");
	}).mouseout(function(){
		$(this).html("执行中").removeClass("achieve").addClass("running");
	});
	
	//状态改变按钮 完成-重新启动
	$(".achieve").mouseover(function(){
		$(this).html("重新启动").removeClass("achieve").addClass("running");
	}).mouseout(function(){
		$(this).html("完成").removeClass("running").addClass("achieve");
	});
	*/
	/*项目与任务详情--任务未完成任务点击完成功能*/
	$('.updTaskStatus').live('click', function() {
		hideTaskId = $(this).parents("li").attr("id");
		hideStatusBtnObj = $(this);
		$.ajax({
			type: "POST",
			url: '/vm/oa/oaTask/addFeedback.jsp',
			success: function(msg){
				$("#addChildFeedbackDiv").html(msg)
			}
		});
		$("#addChildFeedbackDiv").show();
		
	});
	
	//选择指派人DIV
	$('.showAppoint').live('click', function(e){
		$("#pointEmpDiv").remove();
		pointTaskId = $(this).parent().attr("id");
		pointByObj = $(this);
		var pointEmpInfo ="";
		var existEmpId = ""; 
		$(".mesOnline").each(function(){
			var empId = $(this).attr("empId");
			var empName = $(this).find("i b").text();
			if(typeof(empId)!="undefined" && empId!="" && existEmpId.indexOf(","+empId+",")==-1){
				existEmpId += ","+empId+",";
				pointEmpInfo += '<div class="point-block"  ><label class="lf selPointBy" style="cursor: pointer;" empId="'+empId+'">'+empName+'</label></div>'
			}
		})
		
		if(pointEmpInfo ==""){
			asyncbox.tips('没有参与者可指派,请先选择参与者');		
		}else{
			var str ='<div class="add-calendar" id="pointEmpDiv" style="display: none;" >'+pointEmpInfo+'</div>';  
			$(this).append(str);
			//$("#pointEmpDiv").html(pointEmpInfo);
		}	
		$("#pointEmpDiv").show();
		e = e||event; stopFunc(e);//阻止冒泡
		
	});
	
	/*选择指派人并处理*/
	$('.selPointBy').live('click', function() {
		var fieldVal= $(this).attr("empId");
		$.ajax({
			type: "POST",
			url: "/OATaskAction.do",
			data: "operation=2&type=updateSingle&fieldName=executor&fieldVal="+fieldVal+"&taskId="+pointTaskId,
			success: function(msg){
				if(msg=="error"){
					asyncbox.tips('指派失败!','error');
				}else{
					asyncbox.tips('指派成功','success');
					$(pointByObj).before(msg);
					$(pointByObj).remove();
					$("#pointEmpDiv").remove();
				}
			}
		})	
	});
	
	/*详情删除任务方法*/
	$('#runTaskUl .b-del').live('click', function() {
		var taskId = $(this).parent().parent().attr("id");
		var parentLi = $(this).parent().parent();//存放LI对象
		if(!confirm('确定删除?')){
			return false;
		}else{
			$.ajax({
				type: "POST",
				url: "/OATaskAction.do",
				data: "operation=3&taskId="+taskId,
				success: function(msg){
					if(msg == "error"){
						asyncbox.tips('删除失败!','error');
					}else{
						asyncbox.tips('删除成功','success');
						parentLi.remove();
					}
				}
			});
		}	
	});
	
	
	/*任务、项目改变进度条*/
	$('.changeSchedule').live('click', function() {
		var fieldVal = $(this).attr("fieldVal");
		var actionName = $(this).parent().attr("actionName");
		var refInfo = "";
		if("OATaskAction" == actionName){
			refInfo = "&taskId="+$("#taskId").val();
		}else{
			refInfo = "&itemId="+$("#itemId").val();
		}
		$.ajax({
			type: "POST",
			url: "/"+actionName+".do",
			data: "operation=2&type=updateSingle&fieldName=schedule&fieldVal="+fieldVal+refInfo,
			success: function(msg){
				if("error" == "msg"){
					asyncbox.tips('修改失败!','error');
				}else{
					asyncbox.tips('修改成功','success');
					$("#colorBar").css("width",fieldVal+"%");
					$(".num-po").text(fieldVal+"%");
				}
			}
		})	
	});
	
	/*删除提醒*/
	$('.delAlert').live('click', function() {
		var actionName = $(this).attr("actionName");
		var keyId = $(this).attr("keyId");
		var currObj = $(this);
		$.ajax({
			type: "POST",
			url: "/"+actionName+".do",
			data: "operation=3&type=delAlert&keyId="+keyId,
			success: function(msg){
				if("error" == "msg"){
					asyncbox.tips('取消失败!','error');
				}else{
					asyncbox.tips('取消成功','success');
					$(currObj).parent().html("");
				}
			}
		})	
	});
	
	/*删除提醒*/
	/*$('.mesOnline').live('click', function() {
		mdiwin('/MessageQueryAction.do?src=menu','即时消息');	
	});*/
	
	/*编辑详情*/
	$("#remarkInput").click(function(e){
		$("#remarkEditDiv").height($("#remarkDiv").height()).show().find("#remarkTextarea").height($("#remarkDiv").height()-20).focus();
	})
	
	/*编辑详情取消*/
	$(".remarkEditCancel").click(function(e){
		$("#remarkEditDiv").hide();
	})
	
	
})



/*
	公用弹出框

	popName:弹出框类型userGroup:职员
	fieldName:字段名，用于回填,$("#"+fieldName)隐藏对象，$("#"+fieldName+"Name")展示对象
	asynId:弹出框ID，区分不同回填操作,normalId:表示一般回填

	isCheckBox:true 表示多选,默认单选

*/
var hideFieldName;//存放弹出框字段名，用于双击回填方法

var hideAsynId;//存放弹出框ID，区别不同操作

function publicPopSelect(popName,fieldName,asynId,isCheckBox){
	hideFieldName = fieldName;//存放回填字段名，用于双击回填
	hideAsynId = asynId;//存放弹出框ID
	var title = "选择负责人";
	if(fieldName == "surveyor"){
		title = "选择验收人";
	}else if("surveyor" == fieldName){
		title = "选择分享人";
	}
	var url ="/Accredit.do?popname="+popName;
	if(isCheckBox == "true"){
		url +="&inputType=checkbox";
	}
	
	var btnsbarStr = jQuery.btn.OKCANCEL;
	if(asynId == "normalId"){
		//一般回填才有清空按钮
		btnsbarStr = [{text:'清空',action:'selectAll'}].concat(jQuery.btn.OKCANCEL);
	}
	asyncbox.open({
		id :asynId,url:url,title:title,width:755,height:450,
		btnsbar :btnsbarStr,
	    callback : function(action,opener){
	    	if(action == 'ok'){
	    		var str = opener.strData;
	    		if(str!=""){
	    			if(asynId == "participantId"){
	    				//项目、任务参与者回填
		    			var employeeIds = "";//封装参与者信息
						var strArr = str.split("|");
						for(var i=0;i<strArr.length;i++){
							var empInfo =strArr[i].split(";");
							employeeIds += empInfo[0]+",";
						}
						updParticipantsInfo(employeeIds);
						
	    			}else if("participantBoxId" == asynId){
	    				var strArr = str.split("|");
						for(var i=0;i<strArr.length;i++){
							var empInfo =strArr[i].split(";");
							if(""!=empInfo[0]){
								textBoxObj.add(empInfo[1],empInfo[0]);
							}
						}
	    			}else if("changeSurveyor" == asynId){
	    				//修改验收人
	    				changeTaskMember(str.split(";")[0],str.split(";")[1],'surveyor');
	    			}else if("changeExecutor" == asynId){
	    				//修改负责人
	    				changeTaskMember(str.split(";")[0],str.split(";")[1],'executor');
	    			}else if(asynId == "shareId"){
	    				//调用oaWorkLog.js方法,共享人

						var strArr = str.split("|");
						for(var i=0;i<strArr.length;i++){
							var empInfo =strArr[i].split(";");
							if(empInfo[0]!="" && $("#"+empInfo[0]).length==0){
								shareByTBox.add(empInfo[1],empInfo[0]);
							}
						}
	    			}else{
	    				//一般回填方法
	    				$("#"+fieldName).val(str.split(";")[0]);
	    				$("#"+fieldName+"Name").val(str.split(";")[1]);
	    			}
	    		}
	    	}else if(action == 'selectAll'){
				$("#"+fieldName).val("");
	    		$("#"+fieldName+"Name").val("");
			} 
	    }
　  });
}

/*双击回填字段*/
function fillData(datas){
	if(hideAsynId == "participantId"){
		//参与者
		updParticipantsInfo(datas.split(";")[0]+",");
	}else if("participantBoxId" == hideAsynId){
		textBoxObj.add(datas.split(";")[1],datas.split(";")[0]);
    }else if("changeSurveyor" == hideAsynId){
		//修改验收人
		changeTaskMember(datas.split(";")[0],datas.split(";")[1],'surveyor');
	}else if("changeExecutor" == hideAsynId){
		//修改负责人
		changeTaskMember(datas.split(";")[0],datas.split(";")[1],'executor');
	}else if(hideAsynId == "shareId"){
		//调用oaWorkLog.js方法,共享人

		shareByTBox.add(datas.split(";")[1],datas.split(";")[0]);
	}else{
		$("#"+hideFieldName).val(datas.split(";")[0]);
	    $("#"+hideFieldName+"Name").val(datas.split(";")[1]);
	}
	
	jQuery.close(hideAsynId);
}


/*快速添加任务方法&添加任务打开*/
function addTaskFast(){
	$("#addTaskDiv").html($("#copyAddTaskFast").html());
	$("#addTaskDiv").show();
	publicDragDiv($("#addTaskDiv"));
	updPageParticipantInfo()
	$("#hideBg").show();
}


/*提交任务
isDetail :true表示详情进入,false表示列表
*/
function fastTaskSubmit(isDetail,obj){
	var title = "";//标题
	if("true" == isDetail){
		title = $.trim($("#fastTitle").val());//标题
	}else{
		title = $.trim($("#fastTitle").text());
	}
	var executor = $("#fastExecutor").val();//指派人
	var endTime = $.trim($("#fastEndTime").val());//结束时间
	var itemEndTime = $("#fastEndTime").attr("defValue");//获取项目的截止时间
	var isAddChild = $("#fastEndTime").attr("isAddChild");//true表示子任务

	var taskId = "";//默认没有任务id
	if(isAddChild == "true"){
		//isCatalog = "1";
		taskId = $("#taskId").val();
	}
	
	var itemId = "";//默认没有项目ID
	if(typeof($("#itemId").val()) != "undefined"){
		itemId = $("#itemId").val();
	}
	
	if(title==""){
		highlight($("#fastTitle"),5);
		return false;
	}
	
	if(endTime == ""){
		highlight($("#fastEndTime"),5);
		return false;
	}
	
	//判断截止时间是否小于今天
	var today=new Date();//取今天的日期  
	var endDate = new Date(endTime);  
	
	if(DateDiff(endDate,today)<0){  
		asyncbox.tips("截止时间不得等于今天");
		highlight($("#fastEndTime"),5);
		return false;
	}  
	
	if(itemEndTime!=""){
		var itemEndDate = new Date(itemEndTime);
		if(endDate>itemEndDate){  
			if(isAddChild=="true"){
				asyncbox.tips("截止时间不得大于主任务结束时间");
			}else{
				asyncbox.tips("截止时间不得大于项目结束时间");
			}
			highlight($("#fastEndTime"),5);
			return false;
		}  
	}
	
	//若主任务有关联客户，子任务默认也关联
	var taskClientId = "";
	if(typeof($("#taskClientId"))!="undefined"){
		taskClientId = $("#taskClientId").val();
	}
	
	$.ajax({
		type: "POST",
		url: "/OATaskAction.do",
		data: "operation=1&schedule=0&remark=&title="+encodeURIComponent(title)+"&executor="+executor+"&endTime="+endTime+"&itemId="+itemId+"&taskId="+taskId+"&clientId="+taskClientId,
		success: function(msg){
			if(msg=="error"){
				asyncbox.tips('添加失败!','error');
			}else{
				asyncbox.tips('添加成功','success');
				if("true" == isDetail){
					var taskArr = msg.split(";");
					
					var str = '<li id='+taskArr[0]+'><div class="d-oper"><b class="b-del icons" title="删除"></b></div><span class="tContent">'+taskArr[1]+'</span>';
					
					if(taskArr[2] == ""){
						str +='<span class="sAppoint showAppoint">未指派</span>';
					}else{
						str +='<span class="sFinish showAppoint">'+taskArr[2]+' '+taskArr[3]+'</span><span class="sFinish updTaskStatus" status="'+taskArr[4]+'">完成</span></li>';
					}
					if($("#runTaskUl li").length == 0){
						$("#runTaskUl").append(str);
					}else{
						$("#runTaskUl li:first").before(str);
					}
					$(".addWrap").hide();
				
					//还原快速添加内容
	
					$("#fastTitle").val("");//清空内容
					$("#fastExecutor").val("");//清空负责人
	
					$("input[name='taskEndTimeFlag']").get(0).checked=true;//选为默认时间 
					taskEndTimeSel('0');
					$("#hideBg").hide();
				}else{
					setTimeout("form.submit();",1000)
					
				}
			}
		}
	});
}


/*选择快速添加任务截止时间类型*/
function taskEndTimeSel(taskEndTimeFlag){
	if(taskEndTimeFlag == "1"){
		//指定时间
		$("#showEndTime").show();
		$("#fastEndTime").val("");//截止时间为空
	}else{
		//默认时间
		$("#showEndTime").hide();
		$("#fastEndTime").val($("#fastEndTime").attr("defValue"));//设置默认为项目截止时间

	}
}


/***
* me:jquery对象
* count:闪烁的次数

* 调用例子:highlight($("#userName"),5);
*/
function highlight(me,count){
	var count = count-1;
	if(count<=0){
		me.css({'background-color' : ""});
		return;
	}
	if(count%2==0){
		me.css({'background-color' : "#ffcccc"});
	}else{
		me.css({'background-color' : ""});
	}
	setTimeout(function(){
		highlight(me,count);
	},200);
	
}


/*选择分页*/
function pageSubmit (pageNo){
	$("#pageNo").val(pageNo);
	form.submit();
}

//阻止冒泡事件
function stopFunc(e){ 
	e.stopPropagation?e.stopPropagation():e.cancelBubble = true; 
}

/**
比较两个日期相差的天数，可为负值

**/
function DateDiff(sDate1, sDate2){ 
	iDays = parseInt(Math.abs(sDate1 - sDate2) / 1000 / 60 / 60 /24);  
	if((sDate1 - sDate2)<0){
		return -iDays;
	}
	return iDays; 
}


/*更新页面头部参与者信息*/
function updPageParticipantInfo(){
	
	var participantNames = "";//存放参与者信息
	var title = "";//title属性存放名称
	var participantCount = 0;
	var fastExecutorSelect = '<option value="">暂不指定</option>';//fastExecutor下拉框更新
	$(".mesOnline").each(function(){
		var empName = $(this).find("i b").text();
		var empId = $(this).attr("empId");
		if(empName!="" && participantNames.indexOf("<a>"+empName+"</a>")==-1){
			participantNames +="<a>"+empName+"</a>";
			title += empName+" ";
			fastExecutorSelect += '<option value="'+$(this).attr("empId")+'">'+empName+'</option>';
			participantCount++;
		}
	})
	$("#participants").html(participantNames);//更新头部参与人信息  
	$("#participantCount").text("参与人员（"+(participantCount)+"人）");//更新参与人员人数
	$("#participantsDiv").html(participantNames)
	//更新任务详情添加时选择的负责人下拉框

	if($("#fastExecutor").length>0){
		$("#fastExecutor").html(fastExecutorSelect);
	}
}

/*公用拖动方法
传入一个DIV对象
*/
var dragHeight;
function publicDragDiv(obj){
	$(obj).bind("mousedown",function(event){
		var offset_x = $(this)[0].offsetLeft;//x坐标
		var offset_y = $(this)[0].offsetTop;//y坐标
		/* 获取当前鼠标的坐标 */
		var mouse_x = event.pageX;
		var mouse_y = event.pageY;				
	
		/* 绑定拖动事件 */
		/* 由于拖动时，可能鼠标会移出元素，所以应该使用全局（document）元素 */
		$(document).bind("mousemove",function(ev){
			/* 计算鼠标移动了的位置 */
			var _x = ev.pageX - mouse_x;
			var _y = ev.pageY - mouse_y;
			
			/* 设置移动后的元素坐标 */
			var now_x = (offset_x + _x ) + "px";
			var now_y = (offset_y + _y ) + "px";					
			/* 改变目标元素的位置 */
			$(obj).css({
				top:now_y,
				left:now_x
			});
			
			//console.log($(obj).attr("bg"));
		});
	})

}

//选择日期
function selectDate(elementId){
	hideElementId = elementId;//存放元素名称
	WdatePicker({el:$dp.$(elementId),lang:'zh_CN',onpicked:dateChange})
}
//选择完日期后提交
function dateChange(){
	updSingleField($("#"+hideElementId));
}

/*收藏*/
/*
collectionId 要收藏的Id
titleName 标题
urlparam 点击链接
typeName 收藏类型
*/
function getCollection(collectionId,titleName,urlparam,typeName){
	var bg= $(".join-collect").attr('bg');//是收藏t还是取消f
	var type = "del";
	if(bg == "t"){
		type = "add";
	}	
	var url = "/OACollectionAction.do?operation=2&attType="+type+"&typeName="+typeName+"&keyId="+collectionId;
	jQuery.ajax({
	 	 type: "POST",
	  	 url: url,	
	  	 data:{
	  	 	urlparam:urlparam,
	  	 	titleName:titleName
	  	 },	   
	  	 success: function(msg){
	  	 	if(type == "add"){  	 
	  	 		if(msg == "OK"){
	  	 			asyncbox.tips('收藏成功','success');
	  	 			$(".join-collect").attr('bg','f');
	  	 			$(".join-collect").attr('title','取消收藏');
	  	 			$(".join-collect").css('background-position','-236px -126px');
	  	 		}else{
	  	 			asyncbox.tips('收藏失败','error');
	  	 		}			  					  			  	  			
	  	 	}else{
	  	 		if(msg == "OK"){
	  	 			asyncbox.tips('取消成功','success');
	  	 			$(".join-collect").attr('bg','t');
	  	 			$(".join-collect").attr('title','加入收藏');
	  	 			$(".join-collect").css('background-position','-203px -127px');
	  	 		}else{
	  	 			asyncbox.tips('取消失败','error');
	  	 		}	  	 		
	  	 	}	  	 	
	  	}
	});
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

function updateTaskStatus(){
	var feedbackContent = $("#feedbackContent").val();
	if($.trim(feedbackContent)==""){
		asyncbox.tips('情况描述不能为空');
		return false;
	}
	$("#feedbackSubmitBtn").attr("disabled","disabled");//锁住按钮
	var taskId = hideTaskId;
	var status = $(hideStatusBtnObj).attr("status");
	var li = $(hideStatusBtnObj).parents("li");//存放未完成的li,用于完成删除
	var currObj = $(hideStatusBtnObj);//当前对象
	$.ajax({
		type: "POST",
		url: "/OATaskAction.do",
		data: "operation=2&type=changeStatus&status="+status+"&taskId="+taskId+"&feedbackContent="+encodeURIComponent(feedbackContent),
		async: false,
		success: function(msg){
			$("#addChildFeedbackDiv").hide();
			$("#addChildFeedbackDiv").html("")
			if(msg =="error"){
				asyncbox.tips('操作失败!','error');
				$("#feedbackSubmitBtn").attr("disabled","");//解除锁住按钮
			}else if(msg == "3"){
				asyncbox.tips('等待验收','success');
				li.find(".updTaskStatus ").text("验收中");
				li.find(".updTaskStatus").removeClass("updTaskStatus");
			}else if(msg == "1"){
				asyncbox.tips('退回成功','success');
				$(currObj).parent().parent().remove();
			}else{
				if($("#finishTaskUl li").length == 0){
					$("#finishTaskUl").append(msg);
				}else{
					$("#finishTaskUl li:first").before(msg);
				}
				asyncbox.tips('操作成功','success');
				li.remove();//删除未完成的li
			}
		}
	});
}

/*客户详情*/
function clientDetail(clientId,clientName){
	mdiwin('/CRMClientAction.do?operation=5&type=detailNew&keyId='+clientId,clientName);
}

/*控件提交参与人*/
function participantBoxSubmit(){
	if(""==$("#participantBoxId").val()){
		asyncbox.tips('请选择参与人');
	}else{
		updParticipantsInfo($("#participantBoxId").val());
		$("#participantBoxId").val("");
		$(".textboxlist-bits").remove();
		loadTextBox('participantBoxId');
	
	}
}


function updDetailRemark(actionName,keyId){
	var actionNameStr = "/"+actionName+".do";
	var fieldVal = $("#remarkTextarea").val();
	
	if(!containSC($.trim(fieldVal))){
		asyncbox.tips("不能含有特殊字符(' \" | ; \\)");
		return false;
	}

	var fieldTitle = $("#remarkTextarea").attr("title");
	
	if(fieldVal!=fieldTitle){
		var keyIdStr = "&taskId="+keyId;
		if(actionName == "OAItemsAction"){
			keyIdStr = "&itemId="+keyId;
		}
		$.ajax({
			type: "POST",
			url: actionNameStr,
			data: "operation=2&type=updateSingle&fieldName=remark&fieldVal="+encodeURIComponent(fieldVal)+keyIdStr,
			success: function(msg){
				if(msg=="error"){
				asyncbox.tips('修改失败!','error');
			}else{
				asyncbox.tips('修改成功','success');
				$("#remarkInput").val(fieldVal);
				$("#remarkDiv i").text(fieldVal);
				$("#remarkTextarea").val(fieldVal);
				$("#remarkTextarea").attr("title",fieldVal);
				$("#remarkEditDiv").hide();
				window.frames["tabDiscussIframe"].window.location.reload();
			}
			}
		});	
	}else{
		
	}
}


/*
模糊查询确认
*/
function searchConSubmit(){
	$("#hasSearchCondition").val(checkSearchCondition());
	form.submit();
}

/*获取模糊查询条件标识*/
function checkSearchCondition(){
	var hasSearchCondition ="";
	
	if($.trim($("#searchKeyWord").val()) !=""){
		hasSearchCondition ="true";
	}
	
	if(hasSearchCondition == ""){
		$(".tag-pa :input[class!='btn-add']").each(function(){
			if($.trim($(this).val())!=""){
				hasSearchCondition = "true";
				return false;
			}
		})
	}
	
	return hasSearchCondition;
}