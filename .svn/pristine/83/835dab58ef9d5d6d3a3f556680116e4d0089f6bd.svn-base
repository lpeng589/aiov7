var hideTaskAssignId;//改变状态存放ID
$(function(){
	
	$('tr').live('dblclick', function(e) {
		detailTaskAssign($(this).attr("taskAssignId"))
	});
	
	/*全选*/
	$("#checkAll").click(function(){
		var check = $(this).attr("checked") ;
		if(check == "checked"){
			$("input[name='keyId']").attr("checked", "checked");
		}else{
			$("input[name='keyId']").removeAttr("checked");
		}
	});
	
	/*枚举查询*/
	$("#taskStatusUL label").click(function(){
		$("#searchTaskStatus").val($(this).attr("val"));
		query();
	});
	
	
	
})

/*添加任务分派*/
function addTaskAssign(){
	var urls = '/CRMTaskAssignAction.do?operation=6';
	asyncbox.open({
		id:'taskAssignId',url:urls,title:'添加任务分派',width:570,height:315,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
		callback : function(action,opener){
			 if(action == 'ok'){
				opener.submitBefore();
				return false;
			}
		}
	});
}

/*添加任务分派*/
function delTaskAssign(){
	if(!isChecked("keyId")){
		asyncbox.tips("至少选择一个任务");
		return false;
	}
	if(!confirm("确定删除?")){
		return false;
	}else{
		$("#operation").val("3");
		query();
	}
}



/*新增、修改操作成功后回调方法*/
function dealAsyn(){
	form.submit();
}

/*查询*/
function query(){
	form.submit();
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
	var title = "选择执行人";
	var url ="/Accredit.do?popname="+popName;
	if(isCheckBox == "true"){
		url +="&inputType=checkbox";
	}
	asyncbox.open({
		id :asynId,url:url,title:title,width:755,height:450,
		btnsbar :[{text:'清空',action:'selectAll'}].concat(jQuery.btn.OKCANCEL),
	    callback : function(action,opener){
	    	if(action == 'ok'){
	    		var str = opener.strData;
	    		var existIds = ","+$("#searchUserId").val();
	    		var newIds = "";//存放新的ID
	    		for(var i=0;i<str.split("|").length;i++){
	    			if("" != str.split("|")[i].split(";")[0]){
		    			var selId = ","+str.split("|")[i].split(";")[0]+",";
						if(existIds.indexOf(selId)==-1){
							newIds +=str.split("|")[i].split(";")[0]+",";
						}		    			
	    			}
	    		}
	    		$("#"+fieldName).val($("#"+fieldName).val()+newIds);
	    	}else if(action == 'selectAll'){
				jQuery("#"+fieldName).val("");
			}
	    	query(); 
	    }
　  });
}

/*双击回填字段*/
function fillData(datas){
	jQuery("#"+hideFieldName).val(datas.split(";")[0]+",");
    //jQuery("#"+hideFieldName+"Name").val(datas.split(";")[1]);
	//jQuery.close(hideAsynId);
	query(); 
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


/*更新*/
function updTaskAssign(taskAssignId,taskStatus){
	var height = 345;
	if(taskStatus == "0"){
		height = 430;
	}
	var urls = '/CRMTaskAssignAction.do?operation=7&taskAssignId='+taskAssignId ;
	asyncbox.open({
		id:'taskAssignId',url:urls,title:'修改任务分派',width:570,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
		callback : function(action,opener){
			 if(action == 'ok'){
				opener.submitBefore();
				return false;
			}
		}
	});
}

/*详情*/
function detailTaskAssign(taskAssignId,taskStatus){
	var height = 345;
	var urls = '/CRMTaskAssignAction.do?operation=7&isDetail=true&taskAssignId='+taskAssignId ;
	asyncbox.open({
		id:'taskAssignId',url:urls,title:'任务分派详情',width:570,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
		callback : function(action,opener){
			 if(action == 'ok'){
				opener.submitBefore();
				return false;
			}
		}
	});
}

/*展示描述方法*/
function showTaskSummary(taskAssignId){
	jQuery("#taskSummary").remove();
	hideTaskAssignId = taskAssignId ;
	jQuery("tr[taskassignid='"+taskAssignId+"'] .finishStatus").append(jQuery("#copyTaskSummary").html());
	
}

/*完成状态*/
function finishStatus(taskAssignId){
	var urls = '/CRMTaskAssignAction.do?operation=7&isDetail=true&finishStatus=true&taskAssignId='+taskAssignId ;
	asyncbox.open({
		id:'taskAssignId',url:urls,title:'任务完成',width:570,height:390,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
		callback : function(action,opener){
			 if(action == 'ok'){
				opener.submitBefore();
				return false;
			}
		}
	});
}


 //选择日期
function selectDate(elementId){
	hideElementId = elementId;//存放元素名称
	WdatePicker({el:$dp.$(elementId),lang:'zh_CN',onpicked:dateChange})
}

//选择完日期后提交
function dateChange(){
	query();
}

function showClient(clientId,clientName){
	top.mdiwin('/CRMClientAction.do?operation=5&type=detailNew&keyId='+clientId,clientName)
}

//查询清空
function clearALL(fieldName){
	$("#"+fieldName).val("");
	query();
}
