var thInput = '<li class="pr icon-long-txt"><input class="lf txt-l inp" type="text" /><b class="icons-2 pa b-del" ></b><input type="hidden" class="quoteInputInfo" value="empty,empty">';
var thSelect = '<select class="s-select" title="选择总结进度"><option value="0">进度</operation><option value="0">0</operation><option value="25">25%</operation><option value="50">50%</operation><option value="75">75%</operation><option value="100">100%</operation></select>';
jQuery().ready(function() {
	/*右边显示*/	
	//加载排序
	jQuery.ajax({type:"post",url:"/THDesktopAction.do?operation=4&type=order&pot=right",success:function(msg){
			if(msg!=""){
				var dets = msg.split("|");				
				var da = "";
				for(var i=0;i<dets.length;i++){
					var ds = dets[i].split(";");
					if(ds[0] != ""){
						var modulName="";	
						if(ds[0] == "oaneedtodo"){
							modulName = '<div class="in-h" bd="oaneedtodo"><span><i class="f-b"><b class="icon-1 b-i1"></b>'+ds[2]+'</i></span><div class="d-add" id="oaneedtodo"><span class="in-span"><input class="lf add-inp" id="add_inp" name="add_inp" type="text" placeholder="输入需要做的事" /><em class="lf icon-1 add-em"></em></span></div></div>';
						}else if(ds[0] == "dayremark"){
							if($("#isOAorTrue").val() == "true"){
								modulName = "";
							}else{
								modulName = '<div class="in-h" bd="'+ds[0]+'" id="'+ds[0]+'"><span><i class="f-b"><b class="icon-1 b-i1"></b>'+ds[2]+'</i></span></div>';
							}
							
						}else{
							modulName = '<div class="in-h" bd="'+ds[0]+'" id="'+ds[0]+'"><span><i class="f-b"><b class="icon-1 b-i1"></b>'+ds[2]+'</i></span></div>';
						}
						da += modulName;						
					}					
				}				
				$("#leftatuo>.join-b").after(da);
				for(var i=0;i<dets.length;i++){
					var ds = dets[i].split(";");
					if(ds[0] != ""){
						jQuery.ajax({type:"post",url:"/THDesktopAction.do?operation=77&type=common&deskId="+ds[0],spanid:ds[0],success:function(data){							
								$(".i-m-r").find("#"+this.spanid).append(data);								
							}					
						})	
					}
				}						
			}
		}
	})
	
	/*中显示*/
	//加载排序
	jQuery.ajax({type:"post",url:"/THDesktopAction.do?operation=4&type=order&pot=mid",success:function(msg){
			if(msg!=""){			
				var dets = msg.split("|");					
				var da = "";
				for(var i=0;i<dets.length;i++){
					var ds = dets[i].split(";");
					if(ds[0] != ""){
						var modulName="";
						var nds = ds[2].replace("我的","");
						if(ds[0] == "oaworklog"){
							modulName = '日志<i class="i-k"></i>';
						}else{
							modulName = nds;
						}
						da += '<li sc="lt-'+ds[0]+'" bd="'+ds[0]+'">'+modulName+'</li>';
					}					
				}				
				$("#minatuo").append(da);	
				tagList();
				for(var i=0;i<dets.length;i++){
					var ds = dets[i].split(";");
					if(ds[0] != ""){
						jQuery.ajax({type:"post",url:ds[1],spanid:ds[0],success:function(msg){			
								if(msg.indexOf("html")!=-1){
									$(".i-m-m").html('');
									$(".i-m-m").append(msg);						
								}else{
									$(".lt-"+this.spanid).append(msg);
								}				
							}
						})	
					}
				}
			}															
		}
	})
	/*
	//日志
	jQuery.ajax({type:"post",url:"/OAWorkLogAction.do?operation=4&type=isTHWorkLog",success:function(msg){			
			if(msg !=""){
				$(".lt-oaworklog").append(msg);								
			}			
		}
	})
	//任务
	jQuery.ajax({type:"post",url:"/OATaskAction.do?operation=4&isTHTask=true",success:function(msg){			
			$(".lt-oaTask").append(msg);				
		}
	})
	
	//会议
	jQuery.ajax({type:"post",url:"/OASearchMeeting.do?operation=4&isMeetTh=true",success:function(msg){	
			if(msg.indexOf("html")!=-1){
				//$("#closeDiv").show();
				//$(".hideBg").show();
				$(".i-m-m").html('');
				$(".i-m-m").append(msg);						
			}else{
				$(".lt-oameeting").append(msg);
			}		
						
		}
	})
	//工作流
	jQuery.ajax({type:"post",url:"/OAMyWorkFlow.do?operation=4&isFlowTh=true",success:function(msg){	
			if(msg.indexOf("html")){		
				$(".lt-oaworkflow").append(msg);
			}								
		}
	})
	*/
	/*左边显示*/
	/*待完成任务数量*/
	jQuery.ajax({type:"post",url:"/OATaskAction.do?operation=4&isTHTask=true&isnum=true",success:function(msg){					
			if(msg != "" && msg != "0"){
				$("#totask").find("a").append('<i class="icon-1">'+msg+'</i>');
			}						
		}
	})
	/*待审核工作流数量*/
	jQuery.ajax({type:"post",url:"/OAMyWorkFlow.do?operation=4&isFlowTh=true&isnum=true",success:function(msg){			
			if(!msg.indexOf("html")){			
				$("#toworkflow").find("a").append('<i class="icon-1">'+msg+'</i>');
			}					
		}
	})
	/*待阅邮件数量*/
	jQuery.ajax({type:"post",url:"/THDesktopAction.do?operation=4&type=mail",success:function(msg){			
			if(msg != "0"){
				$("#toreademail").find("a").append('<i class="icon-1">'+msg+'</i>');
			}			
		}
	})
	//end	
	
	/*新增行，单击最后一个input默认加多一行*/
	$('.icon-long-txt .txt-l').live('click', function() {
		var parentId =$(this).parent().parent().attr("id");		
		var inputStr = thInput;
		if("summaryDiv" == parentId){
			//如果是总结，加上进度下拉框
			inputStr += thSelect;
		}
		inputStr += '</li>';				
		if($(this).index("#"+parentId+" .txt-l") == ($("#"+parentId).children().length-1)){
			$(this).parent().after(inputStr);
		} 
	});
	/*删除行*/
	$(".icon-long-txt>.b-del").live('click', function() {
		var parentId =$(this).parent().parent().attr("id");
		if($(this).parent().parent().children().length == 1){		
			asyncbox.tips('至少保留一个。');		
		}else{
			$(this).parent().remove();
		}
	});
	/*引用*/ 
	$(".quote_th").live('click', function(e){		
		hideBackfillName = $(this).attr("backfillName");		
		var workLogDate =$(this).parent().attr("quoteLogDate");//日期
		var workLogType =$(this).parent().attr("quoteLogType");//日期类型
		var tableName = $(this).attr("tableName");//表名
		var isOperation = $(this).attr("isOperation");//是否添加修改引用
		var workLogId = $(this).parent().attr("workLogId");//日志ID		
		var obj = $(this);
		if(workLogId == ""){
			asyncbox.tips('没有引用内容');
			return false;
		}
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
					$("#quoteDiv").remove();
					$("body").append(msg);
					$("#quoteDiv").css({"left":$(obj).offset().left,"top":$(obj).offset().top+30});
				}
				
			}
		}); 		
		stopEvent();//阻止冒泡
	}); 
	
	/*引用上期计划确认回填*/
	$("#quoteBackfillSubmit").live('click', function() {	
		var tableName = $("#quoteDiv").attr("tableName");//存放引用的type
		var readonlyStr = "";
		if(tableName=="OATask"){
			readonlyStr = " readonly='readonly' ";
		}
		
		if($("#quoteDiv :checkbox:checked").length>0){
			var str ='';
			var selectStr = '<select class="s-select" ><option value="0">0%</operation><option value="25">25%</operation><option value="50">50%</operation><option value="75">75%</operation><option value="100" selected="selected">100%</operation></select>';
			$("#quoteDiv :checkbox:checked").each(function(){
				if($(this).attr("displayStr") !=""){
					str += '<li class="pr icon-long-txt"><input class="lf txt-l inp" type="text" value="'+$(this).attr("displayStr")+'" '+readonlyStr+' /><b class="icons-2 pa b-del" ></b><input type="hidden" class="quoteInputInfo" value="'+tableName+","+$(this).attr("id")+'">';
					if("summaryDiv" == hideBackfillName){
						str +=selectStr;
					}
					str +="</li>";
				}
			})
			$("#"+hideBackfillName+" li").each(function(){
				if($(this).find("input").val()==""){
					$(this).remove();
				}
			})
			$("#"+hideBackfillName).append(str);
		}
		$("#quoteDiv").remove();
	});
	
	/*取消引用*/
	$("#quoteCancel").live('click', function() {
		$(this).parent().parent().remove();
	});
	
	/*添加待办*/
	
     $("#add_inp").live('keydown',function(event){
	     if (event.keyCode == 13) {  
	        addTodofcn();
	     }     	
     });
     $(".add-em").live('click',function(){
     	addTodofcn();
     });
     
     /*工作流状态切换*/
     $(".type-tags").live('click',function(){
     	//$(this).addClass('sel').siblings("i").removeClass('sel');
     	$("#closeDiv").show();
		$(".hideBg").show();
     	jQuery.ajax({type:"post",url:"/OAMyWorkFlow.do?operation=4&isFlowTh=true&approveStatus="+$(this).attr("flowType"),success:function(msg){			
				$(".lt-oaworkflow").html('');				
				$(".lt-oaworkflow").append(msg);
				$("#closeDiv").hide();
				$(".hideBg").hide();			
			}
		})
     })
     
     /*任务切换*/
     $(".task-tags").live('click',function(){
     	$("#closeDiv").show();
		$(".hideBg").show();
		jQuery.ajax({type:"post",url:"/OATaskAction.do?operation=4&isTHTask=true&queryType="+$(this).attr("tasktype"),success:function(msg){			
				$(".lt-oaTask").html('');
				$(".lt-oaTask").append(msg);
				$("#closeDiv").hide();
				$(".hideBg").hide();				
			}
		})    
     })
     
     //打分
     $(".pingfen").live("click",function(){
     	//$(".wl-pf").remove();
     	
     	if($(this).parent().next().attr("class") == "wl-pf"){			
			$(this).parent().next().remove();
		}else{
			if($(".wl-pf").val() == ""){
	     		$(".wl-pf").remove();
	     	}
			var spand = $(this).attr("id");
			jQuery.ajax({type:"post",url:"/MYSroceAction.do?operation=6",
				data:{workLogId:$(this).attr("workid"),sroceManId:$(this).attr("sroceManId"),createPlanDate:$(this).attr("createPlanDate")},
				success:function(msg){
					if(msg == ""){
						asyncbox.tips("请先进行打分设置","error");
					}else{
						$("#"+spand).parent().after(msg);
					}					
					
				}
			})			
		}	
     })
     //打分保存
     $(".con-btn").live("click",function(){
     		var pingfenId = $(this).attr("pingfenId");
     		var sroceType = $(this).siblings("#sroceType").val();
     		var comments = encodeTextCode($(this).parent().siblings("#comments").val());
     		var sroces = $(this).siblings().find("#sroces").val();	
     		if(sroceType == "-1"){
     			asyncbox.tips("请选择类型","error");
     			return false; 	
     		}
     		if(comments == ""){
     			asyncbox.tips("评语不能为空","error");
     			return false; 	
     		}   	
     		$(this).parent().parent().remove();
    		jQuery.ajax({type:"post",url:"/MYSroceAction.do?operation=1",
			data:{workLogId:$(this).attr("workid"),sroceManId:$(this).attr("sroceManId"),createPlanDate:$(this).attr("createPlanDate"),pingfenId:pingfenId,sroceType:sroceType,comments:comments,sroces:sroces},
			success:function(msg){
				if(msg == ""){
					asyncbox.tips("操作失败","error");
				}else{				
					asyncbox.tips("操作成功","success");
				}								
			}
		})	
     	
     })
     
     /*拖动*/	
    $("#minatuo" ).sortable({
    	start:function(event,ui){
    		$(this).css("cursor","move");
    	},
    	stop:function(event, ui){
    		$(this).css("cursor","auto");
    		newSort('minatuo');
    	}
    });
    $("#minatuo" ).disableSelection();
    $("#leftatuo" ).sortable({
    	start:function(event,ui){	    	
    		$(this).css("cursor","move");
    		$(ui.item.context).find("ul").hide();
    	},
    	stop:function(event, ui){
    		$(this).css("cursor","auto");
    		$(ui.item.context).find("ul").show();
    		newSort('leftatuo');
    	}
    });
    //$("#leftatuo" ).disableSelection();
  	/*end*/
	$(".join-b").click(function(){
		window.location.href  = "/AIO.do";
		//form.submit();
	})
  	
	$(".i-m-m").css("min-height",$(".i-main").height()+80);
	$(".mu-nav>li").mouseover(function(){		
		var sc = $(this).attr("sc");
		$(this).addClass("sel").append('<b class="icon-1 t"></b>').siblings("li").removeClass("sel").find("b.t").remove();
		$(".d-tw .s-p").html($(this).attr("title")).attr("sc",sc).show().siblings("div").hide();
		if($("#quoteDiv").val() != undefined){			
			$("#quoteDiv").remove();
		}
	});	
	
	
	$(".s-p").click(function(){		
		$(".d-tw").find(".s-p").siblings("div").remove();
		var sc = $(this).attr("sc");		
		$(this).hide();	
		var workID = $("#workIds").val();
		var url="";		
		if("oa-worklog"==sc){
			url = "/OAWorkLogAction.do?operation=6&isWorkTH=true&detType=summary&workLogType=day&workLogDate="+$("#nowDate").val()+"&workLogId="+workID;
			/*if(workID == ""){			
				url = "/OAWorkLogAction.do?operation=6&isWorkTH=true&detType=plan&workLogType=day&workLogDate=$globals.getDate()";
			}	*/						
		}
		if("oa-task"==sc){
			url = "/OATaskAction.do?operation=6&isTHEnter=true";							
		}
		if("oa-meeting"==sc){
			url = "/OASearchMeeting.do?operation=6&isMeetTh=true&operationType=add";							
		}
		if("oa-workflow"==sc){
			url = "/OAWorkFlowCreateAction.do?operation=4&isFlowTh=true";								
		}
		jQuery.ajax({type: "POST",url: url,success: function(msg){		
				$(".d-tw").append(msg);
				$("."+sc).show();
			}
		});
				
	});
	tagList();
});

function addTodofcn(){
	var context = encodeTextCode($("#add_inp").val());         	        	      	 
   	if((context).length<=0){
   	 	highlight($("#add_inp"),5);
   	 	return false;
   	}                	                  			
    var url = "/ToDoAction.do?operation=1&addFlag=quick";
 	jQuery.ajax({
 	 	type: "POST",url: url,	
 	 	data:{
 	 		context:context
 	 	},	   
  	 	success: function(msg){
  	 		if(msg != ""){
   	 		if(msg.indexOf("err")>-1){
   	 			//alert(msg);
   	 			asyncbox.tips(msg,'error');
   	 		}else if(msg.length>60){
 	 				//用户是否被T出
 	 					location.href = "/ToDoAction.do";
 	 				}else{
 	 					//alert("添加成功");
   	 			parent.asyncbox.tips('添加成功','success');			   	 			
   	 			$(".d-add>ul").remove();												   	 			 	 						   	 			
   	 			$("#add_inp").val("");
   	 			jQuery.ajax({type:"post",url:"/THDesktopAction.do?operation=77&type=common&deskId=oaneedtodo",spanid:"oaneedtodo",success:function(data){							
						$(".i-m-r").find("#"+this.spanid).append(data);								
					}					
				})		
   	 		}		   	 				   	 					   	 			
  	 		}else{
  	 			alert("添加失败");
  	 			//asyncbox.tips('添加失败!','error');
  	 		}
  	 			   	 		    
  	 	}
	});
}
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
		
		$("#"+contentDivName).find(".txt-l").each(function(){
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
			$("#nextPlanDiv").find(".txt-l").each(function(){
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
	
	$.ajax({
		type: "POST",
		url: "/OAWorkLogAction.do",
		data:data,
		success: function(msg){
			if(msg == "success"){
				asyncbox.tips(returnMsg+'成功','success');
				cel();
				setTimeout("loadWorkDiv();",1000);
			}else{
				asyncbox.tips(returnMsg+'失败!','error');
			}
		}
	});
}

/*从新加载日志*/
function loadWorkDiv(){
	$(".lt-oaworklog").html('');
	jQuery.ajax({type:"post",url:"/OAWorkLogAction.do?operation=4&type=isTHWorkLog",success:function(msg){			
			if(msg !=""){
				$(".lt-oaworklog").append(msg).show();								
			}			
		}
	})
}


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
	}else if("participantName" == fieldName){
		title = "选择参与人";
	}else if("toastmasterName" == fieldName){
		title = "选择主持人";
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
	    			}else if(asynId == "meetingId"){
	    				//调用fillMeeting方法,会议的
						fillMeeting(str,fieldName);
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
function fillMeeting(str,fieldName){
	var datas = str.split("|");
	for(var i=0;i<datas.length;i++){
		if(datas[i]!=""){
			var data = datas[i].split(";");	
			if(fieldName=="toastmasterName"){
				shareByTBox1.add(data[1],data[0]);
				var toastmasterId = jQuery("#toastmaster").val()+data[0]+";";
				jQuery("#toastmaster").val(toastmasterId);
			}						
			if(fieldName=="participantName"){
				shareByTBox2.add(data[1],data[0]);
				var participantId = jQuery("#participant").val()+data[0]+";";
				jQuery("#participant").val(participantId);
			}			
		}
	}
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
	}else if(hideAsynId == "meetingId"){
		//调用fillMeeting方法,会议的
		fillMeeting(datas,hideFieldName);
	}else{
		$("#"+hideFieldName).val(datas.split(";")[0]);
	    $("#"+hideFieldName+"Name").val(datas.split(";")[1]);
	}	
	jQuery.close(hideAsynId);
}

/*提交任务*/
function taskSubmit(obj){
	var curObj = obj;
	var title = $.trim($("#title").val());//标题
	var remark = $.trim($("#remark").val());//内容
	var executor = $("#executor").val();//负责人
	var beginTime = $.trim($("#beginTime").val());//开始时间
	var endTime = $.trim($("#endTime").val());//结束时间
	var surveyor = $("#surveyor").val();//验收人
	var itemId = $("#itemId").val();//关联项目
	var clientId = $("#taskClientId").val();//关联客户
	var operation = $(obj).attr("operation");//1:新增 2:修改
	var schedule = $("#schedule").val();//进度
	var status = $("#uptStatus").val();//状态

	var participant = $("#participantInfo").val();//参与人员
	var affix = "";//附件
	//asyncbox.tips($("#itemId").find("option:selected").attr("itemEndTime"))
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
	//附件
	$("#affixuploadul_affix").find(":input[name='affix']").each(function(){
		affix += $(this).val() +";"; 
	})
	
	//参与人

	if(participant!=""){
		participant = participant+",";
	}	
	//$(curObj).attr("onclick", "");//锁住按钮
	$.ajax({
		type: "POST",
		url: "/OATaskAction.do",
		data: "operation="+operation+"&keyId="+$(obj).attr("keyId")+"&title="+encodeURIComponent(title)+"&remark="+encodeURIComponent(remark)+"&beginTime="+beginTime+"&endTime="+endTime+"&surveyor="+surveyor+"&itemId="+itemId+"&executor="+executor+"&status="+status+"&schedule=0&clientId=&participant=&affix="+encodeURIComponent(affix),
		success: function(msg){
			var alertName = "添加";
			if(operation == "2"){
				alertName = "修改";
			}
			if(msg=="error"){
				asyncbox.tips(alertName+'失败!','error');				
			}else{
				asyncbox.tips(alertName+'成功','success');
				cel();
				//从新加载主页任务
				loadTaskDiv();
			}
		}
	});
	
}
/*从新加载任务*/
function loadTaskDiv(){
	$(".lt-oaTask").html('');
	jQuery.ajax({type:"post",url:"/OATaskAction.do?operation=4&isTHTask=true",success:function(msg){			
			$(".lt-oaTask").append(msg).show();				
		}
	})
}

/*验证会议室*/
function validRoom(){
	var starttime = $("#meetTime").val()+" "+trimStr($("#meetTime1").val())+":"+trimStr($("#meetTime2").val());
	var endtime = $("#meetTime").val()+" "+trimStr($("#meetTime3").val())+":"+trimStr($("#meetTime4").val());
	var url="/Meeting.do?operation=4&requestType=OCCUPATION&boardroomId="+$("#boardroomId").val()+"&meetingStartTime="+starttime+"&meetingEndTime="+endtime;		 
	jQuery.ajax({
	   type: "GET",
	   url:url ,
	   success: function(msg){	    
		   if(msg == "1"){
			   	asyncbox.tips('改会议室已经被占用了,不能使用','error');
			   	$("#boardroomId").val("");				      	
			}
			if(msg == "5"){
				asyncbox.tips('发生错误!','error');
				$("#boardroomId").val("");
			}
	 	}
	});
}
/*会议保存*/
function meetingSubmit(){
   var participant="";
   var participantname="";
   $("#participantName").parent().find(".textboxlist .boxSelect").each(function(){
		if($(this).attr("id")!="undefined"){
			participant +=$(this).attr("id")+";";	
		}
	})	
	$("#participantName").parent().find(".textboxlist .showSelect").each(function(){
		if($(this).text()!="undefined"){
			participantname +=$(this).text()+";";
		}
	}) 
   var toastmaster="";
   var toastmastername=""; 
   $("#toastmasterName").parent().find(".textboxlist .boxSelect").each(function(){
		if($(this).attr("id")!="undefined"){
			toastmaster +=$(this).attr("id")+";";
		}
	})
	$("#toastmasterName").parent().find(".textboxlist .showSelect").each(function(){
		if($(this).text()!="undefined"){
			toastmastername +=$(this).text()+";";
		}
	})     
    if(trimStr($("#meetTile").val()).length <=0){
   		highlight($("#meetTile"),5);
  		return false;
  	}
  	if(trimStr($("#meetContext").val()).length <=0){
   		highlight($("#meetContext"),5);
  		return false;
  	}
  	if(trimStr($("#toastmasterName").val()).length <=0){
   		asyncbox.tips('主持人不能为空!','error');
  		return false;
  	}
  	if(trimStr($("#participantName").val()).length <=0){
   		asyncbox.tips('参与人不能为空!','error');
  		return false;
  	}
  	if($("#boardroomId").val().length <=0){
   		asyncbox.tips('请选择会议室!','error');
  		return false;
  	}
 	var starttime = $("#meetTime").val()+" "+trimStr($("#meetTime1").val())+":"+trimStr($("#meetTime2").val())+":59";
	var endtime = $("#meetTime").val()+" "+trimStr($("#meetTime3").val())+":"+trimStr($("#meetTime4").val())+":59";
   	start = starttime.replace("-","/");//替换字符，变成标准格式  
	var d2=new Date();//取今天的日期  
	var d1 = new Date(Date.parse(start));
	var d3 = new Date(Date.parse(endtime.replace("-","/")));   
  	
    if(d1<d2){
       asyncbox.tips('开始时间必须大于当前时间!','error');
       return false;
    }
   	
  	if(d3<=d1){
	  	asyncbox.tips('结束时间必须大于开始时间!','error');
	  	return false;
  	}
  	//提交
  	$.ajax({
  		type:"post",
  		url:"/Meeting.do?operation=1&operationType=THnew",
  		data:{
  			meetTile:trimStr($("#meetTile").val()),
  			boardroomId:$("#boardroomId").val(),
  			starttime:starttime,
  			endtime:endtime,
  			meetContext:trimStr($("#meetContext").val()),
  			participant:participant,
  			participantname:participantname,
  			toastmaster:toastmaster,
   			toastmastername:toastmastername
  		},
  		success:function(msg){
  			if(msg == "3"){
  				asyncbox.tips("添加成功","success");
  				cel();
				//从新加载主页任务
				loadMeetkDiv(); 				
  			}else{
  				asyncbox.tips("添加失败","error"); 
  			}
  		}
  	})               	
}
/*从新加载会议*/
function loadMeetkDiv(){
	$(".lt-oameeting").html('');
	jQuery.ajax({type:"post",url:"/OASearchMeeting.do?operation=4&isMeetTh=true",success:function(msg){			
			$(".lt-oameeting").append(msg).show();			
		}
	})
}


/*拖动后从新排序*/
function newSort(ids){	
	var datas="";
	if(ids=="minatuo"){			
		var j=0;
		$("#minatuo>li").each(function(i){			
			if($(this).attr("bd") != undefined){
				datas += j+","+$(this).attr("bd")+";";
				j++;
			}
		});
		datas="mid;"+datas;
	}
	if(ids=="leftatuo"){			
		var j=0;
		$("#leftatuo>div").each(function(i){			
			if($(this).attr("bd") != undefined){
				datas += j+","+$(this).attr("bd")+";";
				j++;
			}
		});
		datas="right;"+datas;				
	}
	$.ajax({
		type:"post",
		url:"/THDesktopAction.do?operation=13",
		data:{setting:datas},
		success:function(msg){
			if(msg == "0"){
				asyncbox.tips('操作失败!','error');
			}
		}
	})
}

function tagList(){
	$(".u-lt>li").mouseover(function(){
		var sc = $(this).attr("sc");
		$(this).append('<i class="i-k"></i>').siblings("li").find("i").remove();
		$("."+sc).show().siblings("div.d-sc").hide();
	});
}
function closePop(obj){
	$(obj).parents("div.d-pf").hide();	
}
function cel(){
	$(".d-tw").find(".s-p").show().siblings("div").remove();
}


