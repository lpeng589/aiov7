$(document).ready(function(){
	if(typeof(top.junblockUI)!="undefined"){
		top.junblockUI();
	}
	
	//多条件查询,更换背景颜色
	$("tr[name='mulTr']").hover(
	  function () {
	    $(this).addClass("mulTr");
	  },
	  function () {
	    $(this).removeClass("mulTr");
	  }
	);
	
	//统计图展开收起
	$("#chartUpDown").click(function(){
		if($(this).attr("class")=="chartUp"){
			 $("#chartContainer").hide();
		     $(this).removeClass();
		     $(this).addClass("chartDown");
		}else{
			$("#chartContainer").show();
	        $(this).removeClass();
	        $(this).addClass("chartUp");
		}
	});
	
	
	//选择两个选项加个清空按钮
	$("#searchCondition :checkbox").click(function(){
		if($(this).parents("ul").find("input:checked").length>=2 && $(this).parents("ul").find("li[name='emptyBtn']").length==0 ){
			var str = '<li name="emptyBtn"><button type="button" onclick="clearEnum(this)" class="clearBtn">清空</button> </li>'
			$(this).parents("ul").append(str);
		}else if($(this).parents("ul").find("input:checked").length < 2 && $(this).parents("ul").find("li[name='emptyBtn']").length==1 ){
			$(this).parents("ul").find("li[name='emptyBtn']").remove();
		}
	})
	
	//用于同比环比,个人部门类型显示删除图标
	$('.SuoYouZheLi').live('mouseover mouseout', function(event) {
	  if (event.type == 'mouseover') {
	     $(this).find(".CloseImg").css("height","7px");
	  } else {
	     $(this).find(".CloseImg").css("height","0");
	  }
	});
	
	//用于同比环比,个人部门类型删除方法
	$('.CloseImg').live('click', function() {
		delPopId($(this),$(this).attr("popSelId"),$(this).attr("popName"))
	});
});


/*
	生成统计图
	chartSwfName:统计图名称
	msSize :多类型行个数
	enumSize : 多类型列个数
	
*/
function getChart(chartSwfName,msSize,enumSize){
	var msWidth = 0;
	var columnSize = 10//默认个柱状图10px
	if(msSize != ""){
		var msSize = msSize;
		var enumSize = enumSize;
		if($("#secondFieldName").val() !=""){
			columnSize = 40;
		}		
		msWidth = msSize*enumSize*columnSize;
	}
	var chartWidth = $("#chartWidth").val();
	if(msWidth >chartWidth){
		chartWidth = msWidth
	}
	//设置最大值8000
	if(chartWidth > 8000){
		chartWidth = 8000;
	}

	var height = 450;
	if(chartSwfName == "Bar2D"){
		height = 600;
	}
	var chart = new FusionCharts("flash/FCF_"+chartSwfName+".swf", "chartId",chartWidth, height);     
	chart.setDataXML($("#chartData").val());
	chart.render("chartContainer");
	$("#chartContainer").show();

}


/*报表图切换*/
function choiceChart(chartSwfName,msSize,enumSize){
	getChart(chartSwfName,msSize,enumSize)
	$("#chartUpDown").removeClass();
    $("#chartUpDown").addClass("chartUp");
}

/*个人与部门弹出框*/
function popSelect(popName,obj){
	$("#popName").val(popName);//选择弹出框名字,
	var title = "";
	if(popName == "userGroup"){
		title = "选择个人"
	}else if(popName == "CrmClickGroup"){
		title = "选择客户"
	}else{
		title = "选择部门"
	}
	var url ="/Accredit.do?popname="+popName+"&inputType=checkbox&chooseData="+$("#"+popName+"Ids").val();
	asyncbox.open({
		id : 'Popdiv',url : url,title : title,width :755,height : 450,
		btnsbar :[{text:'清空',action:'selectAll'}].concat(jQuery.btn.OKCANCEL),
	    callback : function(action,opener){
	    	if(action == 'ok'){
	    		var str = opener.strData;
	    		var selIds = "";//ids
	    		var showUsers = "";//封装显示内容
	    		for(var i=0;i<str.split("|").length-1;i++){
	    			var arrVal = str.split("|")[i];
	    			showUsers +="<li id='"+arrVal.split(";")[0]+"'><span >"+arrVal.split(";")[1] +"</span><img src='/style/images/plan/del_02.gif' onclick=\"delPopId(this,'"+arrVal.split(";")[0]+"','"+popName+"')\"></li>"
		    		selIds +=arrVal.split(";")[0] + ",";
	    		}
	    		jQuery("#"+popName+"Ids").val(selIds);//popName + Ids,把选择的IDS存放到隐藏域提交后台查询
	    		//先删除所有内容
	    		jQuery(obj).parent().find("li").remove();
	    		//添加内容
	    		jQuery(obj).before(showUsers);
	    	} 
			if(action == 'selectAll'){
				//先将td内容清空
				$("#"+popName+"Td").find("li").remove();
				$("#"+popName+"Ids").val("");//把隐藏域ID清空
			}
	    }
　  });
}

/*删除个人或部门*/
function delPopId(obj,selectId,popName){
	var ids =",";
	//获取弹出框隐藏域ID信息
	if(popName == "district" || popName == "trade"){
		ids += $("#"+popName).val();
	}else{
		ids += $("#"+popName+"Ids").val();
	}
	
	//替换删除的ID
	ids = ids.replace(","+selectId+",",",");
	
	if(popName == "district" || popName == "trade"){
		$("#"+popName).val(ids.substring(1));
	}else{
		$("#"+popName+"Ids").val(ids.substring(1));
	}
	
	var liSize = $(obj).parent().parent().find("li").length;//获取选项数目
	if($("#type").val() == "compare"){
		//同比环比，选择也是一个li,所以判断==2
		if(liSize == 2){
			querySubmit();
		}	
	}else{
		//统计与周月
		if(liSize == 1){
			querySubmit();		
		}
	}
	//删除选项
	$(obj).parent().remove();
	
}

/*双击选择个人或部门*/
function dbPopDatas(popValues){
	//如果已存在,删除选项
	if($("#"+popValues.split(";")[0]).length == 1){
		$("#"+popValues.split(";")[0]).remove();
		
		//清掉选项ID
		var ids = "," + $("#"+$("#popName").val()+"Ids").val();
		ids = ids.replace(","+popValues.split(";")[0]+",",",");
		$("#"+$("#popName").val()+"Ids").val(ids)
	}else{
		var popName = $("#popName").val();
		var str ="<li id='"+popValues.split(";")[0]+"'><span>"+popValues.split(";")[1]+"</span><img src='/style/images/plan/del_02.gif' onclick=\"delPopId(this,'"+popValues.split(";")[0]+"','"+$("#popName").val()+"')\"></li>"
		$("#"+popName+"Ids").val($("#"+popName+"Ids").val()+popValues.split(";")[0]+",");
		$("#"+popName).before(str);
	}
}


//行业弹出框
function popTrade(obj){
	var url ="/CRMopenSelectAction.do?isIndexEnter=true";
	asyncbox.open({id : 'Popdiv',url : url,title : '行业查询',width :755,height : 460});	
}

/*行业查询返回的函数*/
function indexSearch(values,trade){	
	
	$("#trade").val(values);
	ajaxGetPopInfo('trade',values,$("#popTrade"));//根据id异步封装好数据,页面显示
	jQuery.close('Popdiv');//关闭弹出框
}

//行业清空
function selectAll(trade){
	$("#trade").val("");
	$("#tradeTd").find("li").remove();
	jQuery.close('Popdiv');//关闭弹出框
}

//地区弹出框
function popDistrict(obj){
	var url ="/CRMopenSelectAction.do?operation=4&isMultiple=true";
	asyncbox.open({
		id : 'Popdiv',url : url,title : '地区查询',width :800,height : 470,
		btnsbar :[{text:'清空',action:'selectAll'}].concat(jQuery.btn.OKCANCEL),
	    callback : function(action,opener){
	    	if(action == 'ok'){
	    		var districtIds = opener.returnVal();
		     	$("#district").val(districtIds.split(";")[1]);
				ajaxGetPopInfo('district',districtIds.split(";")[1],obj);
	    	} 
			if(action == 'selectAll'){
				$("#districtTd").find("li").remove();
				$("#district").val("");//把隐藏域ID清空
			}
	    }
　  });	
}

//异步根据行业或地区id,封装信息页面显示
function ajaxGetPopInfo(popName,selIds,obj){
	jQuery.ajax({
	   type: "POST",
	   url: "/CRMopenSelectAction.do?operation=4&type=popSelect&popName="+popName+"&selIds="+selIds,
	   success: function(msg){
	   	   $(obj).parent().find("li").remove();
    	   $(obj).before(msg);	
	   }
	});
}

//解除搜索
function relieveSearch(){
	$("#isDetail").val("");
	$("#removeSearch").val("true");
	form.submit();	
}


//改变同比阶段
function changePhase(phaseVal){
	var str="";
	jQuery("#unit option").remove();
	if(phaseVal == "quarter"){
		str+='<option value="month">月份</option><option value="week">周</option>';
	}else if(phaseVal == "month"){
		str+='<option value="week">周</option><option value="day">天</option>';
	}else if(phaseVal == "week"){
		str+='<option value="day">天</option>';
	}else{
		str+='<option value="quarter">季度</option><option value="month">月份</option>';
	}		
	jQuery("#unit").append(str);
}

//确定,提交表单
function submitQuery(){
	form.submit();
}

//清空本行的枚举类型
function clearEnum(obj){
	$(obj).parents("ul").find("input:checked").removeAttr("checked");
	if($(obj).attr("name")!="noClearBtn"){
		$(obj).parent().remove();
	}
}

//切换高级与普通查询
function changeSearch(searchFlag,obj){
	if(searchFlag == "high"){
		$("#searchCondition table tr").show();
	}else{
		$("#searchCondition table tr").hide();	
		$(".commonSearch").show();
	}
	$("#highSearch").val(searchFlag)
	$(obj).addClass("sel-li").siblings("li").removeClass("sel-li");
}

/*改为饼图	
function changePie2D(){
	new iChart.Pie2D({
		render : 'canvasDiv',
		data: gloChartData,
		animation : true,
		legend : {
			enable : true
		},
		title : {
			text : $("#captionName").val(),
			offsetx : -100
		},
		width : $("#chartWidth").val(),
		height : 450
	}).draw();
}
*/	

/**
*	锁屏
*/
function blockUI(){
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/style/images/load.gif'/>",css:{ background: 'transparent'}}); 
	}
}

/*查询按钮查询*/
function querySubmit(){
	if($("#type").val()=="compare"){
		//确认时间段不能为空

		if($("#isAround").val()=="true"){
			if($.trim($("#startTime").val()) == ""){
				alert("时间段不能为空");
				$("#startTime").focus();
				return false;
			}
			if($.trim($("#endTime").val()) == ""){
				alert("时间段不能为空");
				$("#endTime").focus();
				return false;
			}
		}else{
			if($.trim($("#searchTime").val()) == ""){
				alert("当前时间不能为空");
				$("#searchTime").focus();
				return false;
			}
		}
		
	}
	$("#isDetail").val("");//不查看详情

	if($("#unit").val()=="quarter" && $("#isAround").val()=="true"){
		jQuery.ajax({
		   type: "POST",
		   url: "/CRMReportAction.do?operation=4&type=checkUnit&startTime="+$("#startTime").val()+"&endTime="+$("#endTime").val(),
		   success: function(msg){
		   	   if(msg=="yes"){
		   	   		alert("查询时间必须大于环比单位");
		   	   }else{
		   	       blockUI();
			       form.submit();
		   	   }
		   }
		});
	}else{
		blockUI();
		form.submit();	
	}
}