

function submitBefore(){
	if($.trim(jQuery("#content").val()) == ""){
		asyncbox.tips('任务内容不能为空');
		jQuery("#content").focus();
		return false;
	}
	
	if($.trim(jQuery("#ref_idName").val()) == ""){
		asyncbox.tips('客户不能为空');
		highlight($("#ref_idName"),5);
		return false;
	}
	
	if($.trim(jQuery("#userIdName").val()) == ""){
		asyncbox.tips('执行人不能为空');
		highlight($("#userIdName"),5);
		return false;
	}
	
	if($.trim(jQuery("#finishTime").val()) == ""){
		asyncbox.tips('完成时间不能为空');
		highlight($("#finishTime"),5);
		return false;
	}else{
		var today=new Date();//取今天的日期  
		var endDate = new Date($("#endTime").val());  
		if(DateDiff(endDate,today)<0){  
			asyncbox.tips("截止时间不得小于今天");  
			highlight($("#endTime"),5);
			return false;
		}  
	}


	form.submit();
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
	asyncbox.open({
		id :asynId,url:url,title:title,width:755,height:450,
		btnsbar :jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
	    	if(action == 'ok'){
	    		var str = opener.strData;
	    		jQuery("#"+fieldName).val(str.split(";")[0]);
	    		jQuery("#"+fieldName+"Name").val(str.split(";")[1]);
	    	} 
	    }
　  });
}


/*双击回填字段*/
function fillData(datas){
	jQuery("#"+hideFieldName).val(datas.split(";")[0]);
    jQuery("#"+hideFieldName+"Name").val(datas.split(";")[1]);
	parent.jQuery.close(hideAsynId);
}


/*添加日程*/
function addCalendar(){
	var clientId = jQuery("#ref_id").val();
	$.ajax({
		type: "POST",
		url: "/OACalendarAction.do",
		data: "operation=6&type=addCalendar&clientId="+clientId,
		success: function(msg){
			jQuery("#addCalendar").html(msg);
			jQuery("#addCalendar").show();
		}
	});
}

