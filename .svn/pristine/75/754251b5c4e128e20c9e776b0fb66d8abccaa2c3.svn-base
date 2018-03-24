var curtaskClientIdName;
function publicPopSelectClient(popName,fieldName,asynId,isCheckBox){
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
	    			if(curtaskClientIdName != undefined && $(curtaskClientIdName).size()> 0 ){
	    				$(curtaskClientIdName).val(str.split(";")[1]);
	    				$(curtaskClientIdName).parent().find(".quoteInputInfo").val("OAClient,"+str.split(";")[0]);
	    				
	    			}
	    			
	    		}
	    	}else if(action == 'selectAll'){
				$("#"+fieldName).val("");
	    		$(curtaskClientIdName).val("");
	    		$(curtaskClientIdName).parent().find(".quoteInputInfo").val("empty,empty");
			} 
	    }
　  });
}

var addInput = '<span class="pr icon-long-txt"><i class="No-i"></i><input class="lf txtIconLong ip_txt" type="text"  id="taskClientIdName" /><b class="icons pa b-del" ></b><input type="hidden" class="quoteInputInfo" value="empty,empty">';
var inputSelect = '<select class="scheduleSelect" title="选择总结进度"><option value="0">进度</operation><option value="0">0</operation><option value="25">25%</operation><option value="50">50%</operation><option value="75">75%</operation><option value="100">100%</operation></select>';
var createTaskObj;
var prevDisObj;
var hideBackfillName;//隐藏引用回填DIV名称
$(function(){
	$(document).on("click", function(){
		$("#quoteDiv").hide();
		$("#followTree").hide();
		$("#workLogTemplateList").hide();
	});
	
	/*tab导航选择*/
	$("#tabSelect span").click(function(){
		$("#tabSelect span").removeClass("sel")
		var tabSelectName = $(this).attr("id");
		
		if(tabSelectName == "followByExplain"){
			$(".con-list").hide();
			$("#followByExplainDiv").show();
			  
		}else{ 
			changeTabSelect(tabSelectName);
		}
		
	});
	
	$(".teamLogQuery").click(function(){
		$("#teamLogDate").val($(this).attr("workLogDate"));
		$("#teamLogType").val($(this).attr("workLogType"));
		form.submit();
	});
	
	
	/*点击已存在的日志给予提示*/
	$(".existWorkLog").click(function(){
		var addTHhead = $("#addTHhead").val();
		//changeTabSelect('tabCreateBy');	
		
		var workLogType = $(this).attr("workLogType");
		var workLogDate = $(this).attr("workLogDate");
		
		var scrollId = workLogType;
		if("day" == workLogType){
			scrollId+=workLogDate;
		}
		
		var top = $("#tabCreateByConDiv div[scrollId='"+scrollId+"']").position().top;
		if(addTHhead == "true"){
			document.documentElement.scrollTop = top;
			//$("#scroll-quto").animate({scrollTop: top}, 500);
		}else{
			$("#scroll-wrap").animate({scrollTop: top}, 500);
		}		
	});
	
	
	/*添加修改时间类型切换*/
	$('#typeLi :radio').live('click', function() {
		if("day" == $(this).val()){
			$("#workLogDateLi").show();
			$("#thisSummary").text("今天总结：");
			$("#nextPlan").text("明天计划：");
		}else{ 
			$("#workLogDateLi").hide();
			$("#thisSummary").text("本周总结：");
			$("#nextPlan").text("下周计划：");
		}
	});
	
	/*引用显示*/
	$('.quote-i').live('mouseover mouseout', function(event){
		if (event.type == 'mouseover') {
			$(this).parents("span").find(".quote-ul").show();
			$("#quoteDiv").remove();
		} else {
			$(this).next().hide();
		}
	});
	
	/*引用显示*/
	$('.quote-ul').live('mouseover mouseout', function(event){
		if (event.type == 'mouseover') {
			$(this).show();
		} else {
			$(this).hide();
		}
	});
	
	/*删除日志*/
	$(".delWorkLog").click(function(){
		if(!confirm('确定删除?')){
			return false;
		}else{
			$.ajax({
				type: "POST",
				url: "/OAWorkLogAction.do",
				data: "operation=3&workLogId="+$(this).attr("workLogId"),
				success: function(msg){
					if(msg == "success"){
						asyncbox.tips('删除成功','success');
						setTimeout("form.submit()",1000);
					}else{
						asyncbox.tips('删除失败!','error');
					}
				}
			});
		}
	});
	
	/*修改*/
	$(".updWorkLog").click(function(){
		var isPlanTemplate = $(this).attr("isPlanTemplate");
		$.ajax({
			type: "POST",
			url: "/OAWorkLogAction.do",
			data: "operation=7&workLogId="+$(this).parents("div[name='con-wrap']").attr("workLogId")+"&detType="+$(this).attr("detType"),
			success: function(msg){
				if(msg == "error"){
					asyncbox.tips('修改出错!','error');
				}else{
					
					$("#updateWorkLog").html(msg);
					$(".addWrap").css("left","25%");
					$(".addWrap").css("top","80px");
					$(".addWrap").css("width","600px");
					$(".addWrap").css("height","auto");
						
					if("true" == isPlanTemplate){
						$(".addWrap").css("left","15%");
						$(".addWrap").css("top","30px");
						$(".addWrap").css("width","980px");
					}
					
					
					
					
					$("#updateWorkLog").show();
					publicDragDiv($(".addWrap"));
					/*
					var hideShareByInfo = $("#hideShareByInfo").val();
					loadTextBox('shareBy');
					
					//加载分享人数据					if(hideShareByInfo!=""){
						var arrStr = hideShareByInfo.split(";");
						for(var i=0;i<arrStr.length;i++){
							if(arrStr[i]!=""){
								shareByTBox.add(arrStr[i].split(":")[1],arrStr[i].split(":")[0]);
							}
						}
					}
					*/
					$("#hideBg").show();
				}
			}
		});
	});
	
	
	/*详情*/
	$(".detail").click(function(){
		var addTHhead = $("#addTHhead").val();
		if(addTHhead == "true"){
			window.location="/OAWorkLogAction.do?operation=5&addTHhead=true&workLogId="+$(this).attr("workLogId");
		}else{
			var type = $(this).attr("type");
			//var workLogDate = $(this).attr("workLogDate");
			var loginName = $(this).prev().text();	
			var title = loginName+"的日志";
			if(type == "week"){
				//title = loginName+$("#monday").val()+"至"+$("#sunday").val()+"的周报";
				title = loginName+"的周报";
			}
			var url = "/OAWorkLogAction.do?operation=5&workLogId="+$(this).attr("workLogId");		
			top.mdiwin(url,title);
		}		
	});
	
	
	
	/*新增行，单击最后一个input默认加多一行*/
	$('.quote-li .txtIconLong').live('click', function() {
		var parentId =$(this).parent().parent().attr("id");
		
		curtaskClientIdName = this;
		
		var inputStr = addInput;
		if("summaryDiv" == parentId){
			//如果是总结，加上进度下拉框
			inputStr += inputSelect;
		}
		inputStr += '</span>';
		
		
		if($(this).index("#"+parentId+" .txtIconLong") == ($("#"+parentId).children().length-1)){
			$(this).parent().after(inputStr);
		} 
		sortInputIndex(parentId);
	});
	
	/*删除行*/
	$(".txt-list .b-del").live('click', function() {
		var parentId =$(this).parent().parent().attr("id");
		if($(this).parent().parent().children().length == 1){
			asyncbox.tips('至少保留一个。');		
		}else{
			$(this).parent().remove();
		}
		
		sortInputIndex(parentId);
	});
	 
	/*引用*/ 
	$(".quote").live('click', function(e){
		var addTHhead = $("#addTHhead").val();
		$("#quoteDiv").remove();
		hideBackfillName = $(this).attr("backfillName");
		
		var workLogDate =$(this).parent().attr("quoteLogDate");//日期
		var workLogType =$(this).parent().attr("quoteLogType");//日期类型
		var tableName = $(this).attr("tableName");//表名
		var isOperation = $(this).attr("isOperation");//是否添加修改引用
		var workLogId = $(this).parent().attr("workLogId");//日志ID
		var obj = $(this);
		$.ajax({
			type: "POST",
			url: "/OAWorkLogAction.do",
			data: "operation=4&type=quote&isOperation="+isOperation+"&workLogDate="+workLogDate+"&tableName="+tableName+"&backfillName="+hideBackfillName+"&workLogType="+workLogType+"&workLogId="+workLogId,
			success: function(msg){
				if(msg == "error"){
					asyncbox.tips('引用出错');
				}else if(msg == ""){
					asyncbox.tips('没有引用内容');
				}else{
					if(addTHhead == "true"){
						$("#scroll-quto").append(msg);
						$("#quoteDiv").css({"left":$(obj).offset().left,"top":$(obj).offset().top+26});
					}else{
						$("#scroll-wrap").append(msg);
						$("#quoteDiv").css({"left":$(obj).offset().left,"top":$(obj).offset().top+28});
					}				
				}
				
			}
		}); 
		
		e = e||event; stopFunc(e);//阻止冒泡
	}); 
	
	/*引用确认*/
	$("#quoteSubmit").live('click', function(e) {
		$(".quote-ul").hide();//隐藏引用选项
		var ulObj = $(this).parents("p").next();
		var workLogId = $(ulObj).attr("workLogId");//日志ID
		var quoteInfo = "";//保存数据,传入后台更新
		var msgInfo = "";//保存内容,成功后显示到页面
		$("#quoteDiv input:checked").each(function(){
			quoteInfo += $(this).val()+":"+$(this).attr("displayStr")+";";
			msgInfo += $(this).attr("displayStr")+";";
		})
		$.ajax({
			type: "POST",
			url: "/OAWorkLogAction.do",
			data: "operation=1&type=addQuote&workLogId="+workLogId+"&quoteInfo="+encodeURIComponent(quoteInfo),
			success: function(msg){
				if(msg=="error"){
					asyncbox.tips('引用出错');	
				}else{
					var str = "";
					if(msgInfo!=""){
						for(var i=0;i<msgInfo.split(";").length;i++){
							if(msgInfo.split(";")[i]!=""){
								str += '<li><em>'+msgInfo.split(";")[i]+'</em><em style="color: red;margin-left: 10px;">100%</em></li>';							
							}
						}
						$(ulObj).append(str);
					}
					asyncbox.tips('操作成功','success');
					$("#quoteDiv").remove();
				}
			}
		});
		e = e||event; stopFunc(e);//阻止冒泡
	});
	
	/*生成任务*/
	$(".createTask").live('click', function() {
		//addTaskFast();
		var title =$(this).prev().text() 
		$.ajax({
			type: "POST",
			url: "/OATaskAction.do",
			data: "operation=6",
			success: function(msg){								
				$("#addTaskDiv").html(msg);
				$(".addWrap").css("top","30px");
				$(".addWrap").css("left","35%");
				$("#title").val(title);
				$("#title").attr("readonly","readonly");
				$("#endTime").val($("#beginTime").val());
				$("#addTaskDiv").show();
				publicDragDiv($(".addWrap"));
				loadTextBox('participantInfo');
				$("#hideBg").show();					
			}
		});
	
		createTaskObj = $(this);//存放全局变量,操作成功后获取对象修改.
	});
	
	/*关联任务*/
	$(".relationTask").live('click', function() {
		var url = "/OATaskAction.do?operation=5&taskId="+$(this).attr("taskId");
		top.mdiwin(url,$(this).parent().find("em").text());
	});
	/*关联客户*/
	$(".relationClient").live('click', function() {
		var url = "/CRMClientAction.do?operation=5&type=detailNew&keyId="+$(this).attr("taskId")+"&moduleId=1&viewId=1_1&src=menu&winCurIndex=4";
		var name="关联客户";
		top.mdiwin(url,name);
	});
	
	/*评论*/	
	$(".discuss").click(function(){
		$(".discussDiv").hide();
		var workLogId = $(this).attr("workLogId");
		var frameType = $(this).attr("frameType");
		var url = '/DiscussAction.do?tableName=OAWorkLogDiscuss&f_ref='+workLogId+'&parentIframeName='+frameType+'Frame_'+workLogId;
		$("#"+frameType+"Frame_"+workLogId).attr("src",url);
		$(this).next().show();
		prevDisObj = $(this);//把当前对象记录下来。
	});
	
	/*引用上期计划确认回填*/
	$("#quoteBackfillSubmit").live('click', function() {
		$(".quote-ul").hide();//隐藏引用选项
		var tableName = $("#quoteDiv").attr("tableName");//存放引用的type
		var readonlyStr = "";
		if(tableName=="OATask"){
			readonlyStr = " readonly='readonly' ";
		}
		
		if($("#quoteDiv :checkbox:checked").length>0){
			var str ='';
			var selectStr = '<select class="scheduleSelect" ><option value="0">0%</operation><option value="25">25%</operation><option value="50">50%</operation><option value="75">75%</operation><option value="100" selected="selected">100%</operation></select>';
			$("#quoteDiv :checkbox:checked").each(function(){
				if($(this).attr("displayStr") !=""){
					str +='<span class="pr icon-long-txt"><i class="No-i"></i><input class="lf txtIconLong ip_txt" type="text" value="'+$(this).attr("displayStr")+'" '+readonlyStr+' /><b class="icons pa b-del" ></b>';
					str +='<input type="hidden" class="quoteInputInfo" value="'+tableName+","+$(this).attr("id")+'"/>';
					if("summaryDiv" == hideBackfillName){
						str +=selectStr;
					}
					str +="</span>";
				}
			})
			$("#"+hideBackfillName+" span").each(function(){
				if($(this).find("input").val()==""){
					$(this).remove();
				}
			})
			$("#"+hideBackfillName).append(str);
		}
		sortInputIndex(hideBackfillName);
		$("#quoteDiv").remove();
	});
	
	/*处理单击引用层消失阻止冒泡方法*/
	$('#quoteDiv').live('click', function(e) {
		$("#quoteDiv").show();
		e = e||event; stopFunc(e);//阻止冒泡
	});
	
	/*取消引用*/
	$("#quoteCancel").live('click', function() {
		$(".quote-ul").hide();//隐藏引用选项
		$(this).parent().parent().remove();
	});
	
	/*打开关注树*/
	$(".showFollow").live('click', function(e) {
		$("#followTree").show();
		$("#followTree").bind("click",function(event){
			e = e||event; stopFunc(event);
		});
		e = e||event; stopFunc(e);//阻止冒泡
	});
	
	
	/*关注查询*/
	$(".followQuery").live('click', function() {
		var empId = $(this).parent().attr("empId");
		showFollowQuery(empId,$(this).parent().text());
	});
	
	/*添加计划明细*/
	$(".addWorkLogDet").live('click', function() {
		var detType=$(this).attr("detType")
		$.ajax({
			type: "POST",
			url: "/OAWorkLogAction.do",
			data: "operation=6&detType="+detType+"&workLogType="+$(this).parents("div[name='con-wrap']").attr("workLogType")+"&workLogDate="+$(this).parents("div[name='con-wrap']").attr("workLogDate")+"&workLogId="+$(this).parents("div[name='con-wrap']").attr("workLogId")+"&opertaionFlag="+$(this).attr("opertaion"),
			success: function(msg){
				$("#addWorkLogDet").html(msg);
				$(".addWrap").css("left","25%");
				$(".addWrap").css("top","80px");

				if("summary" == detType){
					$(".addWrap").css("top","10px");
				}
					
				if("true" == $("#isPlanTemplate").val() && detType == "plan"){
					$(".addWrap").css("left","15%");
					$(".addWrap").css("top","30px");
					$(".addWrap").css("width","980px");
				}
				
				$("#addWorkLogDet").show();
				publicDragDiv($(".addWrap"));
				//loadTextBox('shareBy');
				$("#hideBg").show();
				
			}
		});	
	});
	
	/*添加计划
	$(".followTab").live('click', function() {
		var tabName = $(this).attr("tabName");
		$(".followDiv").hide();
		$(".followTab").removeClass("sel-p");
		var myFollowObj = $("p[tabname='myFollow']");
		var myTeamObj = $("p[tabname='myTeam']");
		
		if("myFollow" == tabName){
			myFollowObj.addClass("sel-p");
		}else{
			myTeamObj.addClass("sel-p");
		}
		$("#"+tabName+"Div").show();
	});
	*/
	
	/*搜索我的团购关注与取消关注操作*/
	$(".followIcon").live('click', function() {
		var checkFlag = true;
		var node = treeObj.getNodeByParam("id", $(this).parent().attr("empId"));
		if($(this).attr("class").indexOf("foled")>-1){
			node.checked=false;
			checkFlag = false;
		}else{
			node.checked=true;
		}
		treeObj.updateNode(node, true);
		dealFollowInfo(checkFlag);		
	});
	
	/*删除关注人*/
	$(".delFollowEmp").live('click', function() {
		var node = treeObj.getNodeByParam("id", $(this).parent().attr("empId"));
		node.checked=false;
		treeObj.updateNode(node, true);
		dealFollowInfo(false);		
	});
	
	/*删除关注人*/
	$(".changeRadioDate").live('click', function() {
		$("#nextQuoteUl").attr("quoteLogDate",$(this).val());
	});
	
	
	/*修改日志模板*/
	$(".updateTemplate").live('click', function() {
		var templateId = $(this).parents("li").attr("id");
		var urls = '/OAWorkLogTemplateAction.do?operation=7&templateId='+templateId ;
		asyncbox.open({
			id:'workLogTemplate',title :'修改日志模板',url:urls,cache:false,modal:true,width:950,height:500, btnsbar:jQuery.btn.OKCANCEL,
			callback : function(action,opener){
	　　　　　	if(action == 'ok'){
					opener.formSubmit();
					return false;
	　　　　　	}
	　　　	}
	　	});
	});
	
	/*删除日志模板*/
	$(".delTemplate").live('click', function() {
		if(confirm("确定删除?")){
			var templateId = $(this).parents("li").attr("id");
			$.ajax({
				type: "POST",
				url: "/OAWorkLogTemplateAction.do",
				data: "operation=3&templateId="+templateId,
				success: function(msg){
					if(msg=="success"){
						asyncbox.tips('删除成功','success');
						form.submit();
						//workLogTemplateList();
					}else{
						asyncbox.tips('删除失败!','error');
					}
				}
			});
		}
	});
})

/*添加日志*/
function addWorkLog(){
	$("#addWorkLog").html($("#copyAddWorkLog").html());
	
	if($("#workLogDate option").length==0){
		if($(".week-ul li:first").attr("existWeekLog") == "true"){
			asyncbox.tips('本周日志情况已全部完成,只能进行修改操作');
			return false;
		}else{
			/*
			$("#typeLi span").remove();
			$("#workLogDateLi").hide();
			str = '<span class="lf radio-span"><input type="radio" class="type" name="type" value="week" checked="checked"/><label for="r-week">周报</label></span><span class="lf get-last">获取上期计划</span>';
			$("#typeLi").append(str);
			*/
		}
		
	}
	
	$(".addWrap").css("top","20px");
	$(".addWrap").css("left","25%");
	publicDragDiv($(".addWrap"));
	$("#addWorkLog").show();
	//loadTextBox('shareBy');
	$("#hideBg").show();
}

/*日志提交*/
function workLogSubmit(obj){
	var type = $("input[name='type']:checked").val();//类型
	var workLogDate = $("#workLogDate").val();//日志时间
	var summaryContents = "";//总结内容
	var planContents = "";//计划内容
	var affix = "";//附件
	var shareBy = "";//分享人	var clientId = $("#clientId").val();
	var operation = $(obj).attr("operation");//1:新增 2:修改
	
	//判断周报只能有一个
	if(type=="week" && $(".week-ul li:first").attr("existWeekLog") == "true" && $(obj).attr("workLogId") ==""){
		asyncbox.tips('已存在周报,不可新建');
		return false;
	}
	
	$("#summaryDiv").find(".txtIconLong").each(function(){
		if($.trim($(this).val()) !=""){
			summaryContents += $(this).val() + "&&" + $(this).parent().find("option:selected").val()+"&&"+$(this).parent().find(".quoteInputInfo").val()+"@"; 
		}
	})
	$("#planDiv").find(".txtIconLong").each(function(){
		if($.trim($(this).val()) !=""){
			planContents += $(this).val() + "&&empty&&"+$(this).parent().find(".quoteInputInfo").val()+"@"; 
		}
	})
	$("#affixuploadul_affix").find(":input[name='affix']").each(function(){
		affix += $(this).val() +";"; 
	})
	
	$("#shareBy").parent().find(".textboxlist .boxSelect").each(function(){
		if($(this).attr("id")!="undefined"){
			shareBy +=$(this).attr("id")+",";
		}
	})

	var returnMsg = "添加";
	if(operation == "2"){
		returnMsg = "修改";
	}
	
	$.ajax({
		type: "POST",
		url: "/OAWorkLogAction.do",
		data: "operation="+operation+"&workLogType="+type+"&workLogId="+$(obj).attr("workLogId")+"&workLogDate="+workLogDate+"&shareBy="+shareBy+"&clientId="+clientId+"&affix="+encodeURIComponent(affix)+"&summaryContents="+encodeURIComponent(summaryContents)+"&planContents="+encodeURIComponent(planContents),
		success: function(msg){
			if(msg == "success"){
				asyncbox.tips(returnMsg+'成功','success');
				setTimeout("form.submit()",1000);
			}else{
				asyncbox.tips(returnMsg+'失败!','error');
			}
		}
	});
}



var isSubmiting = false;
/*日志明细提交*/
function workLogDetSubmit(obj){
    
    
	var contents = "";//计划或总结内容
	var operation = $(obj).attr("operation");//1:新增 2:修改
	var workLogDate = $(obj).attr("workLogDate");//日志时间
	var workLogType = $(obj).attr("workLogType");//日志类型
	var workLogId = $(obj).attr("workLogId");//日志ID
	var detType = $(obj).attr("detType");//明细类型 plan计划,summary总结
	var isPlanTemplate = $(obj).attr("isPlanTemplate");//true表示使用模板
	var shareBy = "";//分享人
	var affix = "";//附件
	
	$("#affixuploadul_daffix").find(":input[name='daffix']").each(function(){
		affix += $(this).val() +";"; 
	})
	
	//获取内容信息，判断是否启用模板
	if("true" == isPlanTemplate){
		contents = editor.html();
		detType = "plan";
	}else{
		var contentDivName = "planDiv";
		if("summary" == detType){
			contentDivName = "summaryDiv";
		}
		
		$("#"+contentDivName).find(".txtIconLong").each(function(){
			if($.trim($(this).val()) !=""){
				if("plan" == detType){
					contents += $(this).val() + "&&empty&&"+$(this).parent().find(".quoteInputInfo").val()+"@"; 
				}else{
					contents += $(this).val() + "&&" + $(this).parent().find("option:selected").val()+"&&"+$(this).parent().find(".quoteInputInfo").val()+"@";
				}
			}
		})
	}
	
	var returnMsg = "添加";
	if(operation == "2"){
		returnMsg = "修改";
	}
	
	//处理分享人
	$("#shareBy").parent().find(".textboxlist .boxSelect").each(function(){
		if($(this).attr("id")!="undefined"){
			shareBy +=$(this).attr("id")+",";
		}
	})
	
	if(""==contents){
		asyncbox.tips("内容不能为空");
		return false;
	}
	
	var data = "operation="+operation+"&type=workLogDet&workLogType="+workLogType+"&workLogId="+workLogId+"&workLogDate="+workLogDate+"&contents="+encodeURIComponent(contents)+"&detType="+detType+"&isPlanTemplate="+isPlanTemplate+"&shareBy="+shareBy+"&affix="+encodeURIComponent(affix); 
	
	if("summary" == detType){
		var existNextWorkLog = $("#existNextWorkLog").val();
		if("true"==existNextWorkLog){
			data += "&existNextWorkLog="+existNextWorkLog;
		}else{
			var nextWorkLogDate = $("#nextWorkLogDate:checked").val();
			var nextPlanContents = "";
			var nextPlanAffix = "";
			$("#nextPlanDiv").find(".txtIconLong").each(function(){
				if($.trim($(this).val()) !=""){
					nextPlanContents += $(this).val() + "&&empty&&"+$(this).parent().find(".quoteInputInfo").val()+"@"; 
				}
			})
			
			$("#affixuploadul_nextPlanAffix").find(":input[name='nextPlanAffix']").each(function(){
				nextPlanAffix += $(this).val() +";"; 
			})
			
			data +="&nextWorkLogDate="+nextWorkLogDate+"&nextPlanContents="+encodeURIComponent(nextPlanContents)+"&nextPlanAffix="+encodeURIComponent(nextPlanAffix);		
			
		}
	}
	if(isSubmiting){
    	return;
    }
    isSubmiting = true;
    if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	$.ajax({
		type: "POST",
		url: "/OAWorkLogAction.do",
		data:data,
		success: function(msg){
			isSubmiting = false;
			if(typeof(top.jblockUI)!="undefined"){
				top.junblockUI(); 
			}
			if(msg == "success"){
				asyncbox.tips(returnMsg+'成功','success');
				setTimeout("form.submit()",1000);
			}else{
				asyncbox.tips(returnMsg+'失败!','error');
			}
		}
	});
}

/*生成任务成功后，回调方法*/
function createTaskSuccess(msg){
	var detId = $(createTaskObj).attr("detId");
	var relationId = msg.split(";")[0];
	$.ajax({
		type: "POST",
		url: "/OAWorkLogAction.do",
		data: "operation=2&type=updateDet&detId="+detId+"&relationId="+relationId,
		success: function(msg){
			if(msg != "error"){
				var str = '<span class="relationTask" taskId="'+relationId+'" style="font-size: 12px;cursor: pointer;">【关联任务】</span>';
				$(createTaskObj).before(str);
				$(createTaskObj).remove();
			}
		}
	});
	
	$("#hideBg").hide();	
	//清空任务添加内容
	$("#addTaskDiv").hide();
	$("#addTaskDiv :input").val("");
}


/*添加某天的日志*/
function addAppointWorkLog(obj){
	var workLogDate = $(obj).attr("workLogDate");
	var workLogType = $(obj).attr("workLogType");
	addWorkLog();
	var hideInput = '<input type="hidden" id="workLogDate" name="workLogDate" value="'+workLogDate+'">';
	if(workLogType == "day"){
		$("#workLogDateLi select").remove();
		$("#workLogDateLi").append(hideInput+"<span>"+workLogDate+"<span>");
		$("#r-week").parent().remove();
		//$("#workLogDateLi select option[value='"+workLogDate+"']").attr("selected","selected");
	}else{
		$("#r-day").parent().remove();
		$("#r-week").attr("checked","checked");
		$("#workLogDateLi").html(hideInput);
		$("#thisSummary").text("本周总结：");
		$("#nextPlan").text("下周计划：");
		//$(":radio[value='week']").attr("checked","checked");
	}
}

/*加一天或减一天*/
function addWeek(obj) { 
	var sign = $(obj).attr("sign");
	
	var currDT = new Date($(obj).attr("currDate")); 
	
	if(sign == "-"){
		currDT = addOneDay(currDT,'-'); 
	}else{
		currDT = addOneDay(currDT,'+'); 
	}
	
	var dateStr = currDT.getFullYear() + "-" +(currDT.getMonth()+1) + "-" + currDT.getDate()
	
	$("#weekStartTime").val(dateStr);
	
	form.submit();
} 


//增加或减少一天，由ope决定, + 为加，- 为减，否则不动 
function addOneDay(dt,ope) { 
	var num = 0; 
	if(ope=="-"){ 
		num = -1; 
	} 
	else if(ope=="+"){ 
		num = 1; 
	} 
	
	var y = dt.getFullYear(); 
	var m = dt.getMonth(); 
	var lastDay = getLastDay(y,m); 
	
	var d = dt.getDate(); 
	d += num; 
	if(d<1) { 
		m--; 
		if(m<0) { 
			y--; 
			m = 11; 
		} 
		d = getLastDay(y,m); 
	}else if(d>lastDay) { 
		m++; 
		if(m>11) { 
			y++; 
			m = 0; 
		} 
		d = 1; 
	} 
	var reDT = new Date(); 
	reDT.setYear(y); 
	reDT.setMonth(m); 
	reDT.setDate(d); 
	 
	return reDT; 
} 

//每月最后一天 
function getLastDay(y,m) { 
	var lastDay = 28; 
	m++; 
	if(m==1 || m==3 || m==5 || m==7 || m==8 || m==10 || m==12) { 
		lastDay = 31; 
	}else if(m==4 || m==6 || m==9 || m==11) { 
		lastDay = 30; 
	}else if(isLeapYear(y)==true) { 
		lastDay = 29; 
	} 
	return lastDay; 
} 

//是否为闰年 
function isLeapYear(y) { 
	var isLeap = false; 
	if(y%4==0 && y%100!=0 || y%400==0) { 
		isLeap = true; 
	} 
	return isLeap; 
} 

//阻止冒泡事件
function stopFunc(e){ 
	e.stopPropagation?e.stopPropagation():e.cancelBubble = true; 
}


/*公用拖动方法
传入一个DIV对象
*/
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
		});
	})
}

/*根据名称改变tabSelect*/
function changeTabSelect(tabSelectName){
	/*
	$("#tabSelect span").removeClass("sel");
	$("#tabSelect span[id='"+tabSelectName+"']").addClass("sel");
	$(".tabSelect").hide();
	$("#"+tabSelectName+"ConDiv").show();
	*/
	$("#tabSelectName").val(tabSelectName);
	$("#teamLogDate").val("");
	form.submit();	
}

function showFollowQuery(empId,empName){
	var url = "/OAWorkLogAction.do?operation=4&followEmpId="+empId;
	top.mdiwin(url,empName+"的日志");
}


/*加载KE编辑器*/
function loadKEditor(){
	jQuery.getScript('/js/kindeditor-min.js', function() {
		editor = KindEditor.create('textarea[name="summary"]',{
			items : [
		        'undo', 'redo', '|','source', 'template', 'code', 'cut', 'copy', 'paste',
		        'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
		        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'clearhtml', 'fullscreen', '/',
		        'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
		        'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage',
		        'flash', 'media', 'table', 'hr', 'emoticons',
		        'anchor', 'link', 'unlink'
			],
			uploadJson:'/UtilServlet?operation=uploadFile'
		});
	});
}
		
function searchPersonal(){
	$("#searchPersonalFrame li").show();
	var searchPersonalName = $("#searchPersonalName").val();
	
	if("" == $.trim(searchPersonalName)){
		$("#personalFrameTree").show();
		$("#searchPersonalFrame ").hide();
	}else{
		$("#searchPersonalFrame li").each(function(){
			if($(this).attr("empName").indexOf(searchPersonalName)==-1){
				$(this).hide();
			}
		})
		$("#personalFrameTree").hide();
		$("#searchPersonalFrame ").show();
	
	}
	
}		


/*选择关注方法*/
function followEmp(event, treeId, treeNode) {
	var checkFlag = treeNode.checked;
	dealFollowInfo(checkFlag);
};

/*处理取消与关注方法*/
function dealFollowInfo(checkFlag){
	var followIds = "";//存放关注的用户
	var nodes = treeObj.getCheckedNodes(true);
	for(var i=0;i<nodes.length;i++){
		followIds +=nodes[i].id+","
	}
	jQuery.ajax({
		type: "POST",
		url: "/OAWorkLogAction.do",
		data: "operation=2&type=followEmp&employeeId="+$("#userId").val()+"&followIds="+followIds,
		success: function(msg){
			if(msg == "error"){
				asyncbox.tips('操作失败!','error');
			}else{
				$("#myFollowUL").html(msg);
				if(checkFlag == true){
					asyncbox.tips('关注成功','success');
				}else{
					asyncbox.tips('取消成功','success');
				}
				$(".followIcon").removeClass("foled");
				$(".followIcon").addClass("fol");
				$(".followIcon").attr("title","点击关注");
				for(var i=0;i<followIds.split(",").length;i++){
					$("#"+followIds.split(",")[i]+"Icon").addClass("foled");
					$("#"+followIds.split(",")[i]+"Icon").attr("title","点击关注");
				}
			}
		}
	});

}

/*点击名字查看某人一周日志*/
function showFollow(event, treeId, treeNode) {
	showFollowQuery(treeNode.id,treeNode.name);
}

/*提醒写日志*/
function warmToWriteLog(followId){
	var teamLogDate = $("#teamLogDate").val();//日期
	var teamLogType = $("#teamLogType").val();//类型
	$.ajax({
		type: "POST",
		url: "/OAWorkLogAction.do",
		data: "operation=1&type=warmToWriteLog&followId="+followId+"&teamLogDate="+teamLogDate+"&teamLogType="+teamLogType,
		success: function(msg){
			if("success"==msg){
				asyncbox.tips('提醒成功','success');
			}else{
				asyncbox.tips('提醒失败!','error');
			}
		}
	});	
}

/*关注所有操作*/		
function selFollowAll(){
	if($("#selectFollowAll").attr("checked")=="checked"){
		treeObj.checkAllNodes(true);
		dealFollowInfo(true);
	}else{
		treeObj.checkAllNodes(false);
		dealFollowInfo(false);
	}
	
}		
		
/*input框序号排序*/
function sortInputIndex(parentId){
	var index=1	
	$("#"+parentId+" i").each(function(){
		$(this).text(index+"、");
		index++;
	})
	
}		

/*模板设置list*/
function workLogTemplateList(){
	$.ajax({
		type: "POST",
		url: "/OAWorkLogTemplateAction.do",
		data: "operation=4",
		success: function(msg){
			$("#workLogTemplateList").html(msg);
			$("#workLogTemplateList").show();
		}
	});
}

/*添加模板*/
function addWorkLogTemplate(){
	var urls = '/vm/oa/oaWorkLogTemplate/add.jsp' ;
	asyncbox.open({
		id:'workLogTemplate',title :'添加日志模板',url:urls,cache:false,modal:true,width:950,height:500, btnsbar:jQuery.btn.OKCANCEL,
		callback : function(action,opener){
　　　　　	if(action == 'ok'){
				opener.formSubmit();
				return false;
　　　　　	}
　　　	}
　	});
}

