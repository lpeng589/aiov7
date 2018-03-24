$(function(){
	/*快速定位*/
	$("#quickPositionDiv").hover(
		function () {
			$("#quickPosition").hide();
			$(".fix_dv ul").show();
		},
		function () {
			$("#quickPosition").show();
			$(".fix_dv ul").hide();
		}
	);
	
	$("#quickPositionDiv ").find("a").on("click", function(){
		var titleName = $(this).attr("titleName");
		var top = $("#"+titleName).position().top;
		$(".bd").animate({scrollTop: top}, 500);	
	});
})





/*CRM流程设置*/
function flowSetting(){
	var urls = '/CRMSalesFlowAction.do?operation=4&tableName='+$("#tableName").val();
		asyncbox.open({
			id:'dealdiv',url:urls,title:'流程设置',width:760,height:460,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
			callback : function(action,opener){
			if(action == 'ok'){
				opener.beforeSubmit();
				return false;
			}
		}
	});	
}
/*快递单打印*/
function pinrtSetting(){
	var urls = '/CRMPrintSetAction.do?operation=96&id='+$("#keyInfo").val();
	asyncbox.open({
			id:'printdiv',url:urls,title:'快递单打印',width:410,height:430,cache:false,modal:true,			
			callback : function(action,opener){
			if(action == 'ok'){
				//opener.beforeSubmit();
				return false;
			}
		}
	});	
}



//添加邻居表表单

function addBroBill(tableName,displayName){
	var height = getWinHeight();//获取弹出框高度
	tempTableName = tableName;//把当前表名存放于全局邻居表
	var urls = '/CRMBrotherAction.do?operation=6&noApprove=true&tableName='+tableName+"&keyInfo="+$("#keyInfo").val();
	asyncbox.open({
		id:'brotherOperation',url:urls,title:'添加'+displayName,width:780,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	opener.beforeSubmit();
				return false;
			}
　　  　	}
　  });
}



//修改邻居表表单
function updateBroBill(keyId,tableName,displayName){
	var height = getWinHeight();//获取弹出框高度
	tempTableName = tableName;//把当前表名存放于全局邻居表
	var urls = '/CRMBrotherAction.do?operation=7&tableName='+tableName+"&keyId="+keyId+"&keyInfo="+$("#keyInfo").val();
	asyncbox.open({
		id:'brotherOperation',url:urls,title:'修改'+displayName,width:780,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	opener.beforeSubmit();
				return false;
			}
　　  　	}
　  });
}


//详情邻居表表单

function detailBroBill(keyId,tableName,displayName){
	var height = getWinHeight();//获取弹出框高度
	tempTableName = tableName;//把当前表名存放于全局邻居表

	var urls = '/CRMBrotherAction.do?operation=5&tableName='+tableName+"&keyId="+keyId;
	if(tableName == "CRMSalesChance" || tableName == "CRMSaleContract"){
		mdiwin(urls,displayName);	
	}else{
		asyncbox.open({
			id:'brotherDetail',url:urls,title:displayName+'详情',width:780,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
		    callback : function(action,opener){
			    if(action == 'ok'){
			    	opener.beforeSubmit();
					return false;
				}
	　　  　	}
	　 	});
	
	}
}

/*根据表名异步加载邻居表列表*/
function ajaxLoadBrotherList(tableName,pageNo){
	jQuery.ajax({
		type: "POST",
		url: "/CRMBrotherAction.do?operation=4&type=ajaxBrotherList&tableName="+tableName+"&pageNo="+pageNo+"&keyInfo="+$("#keyInfo").val(),
		success: function(msg){
			jQuery("#"+tableName).html(msg);
		}
	});
}

function getWinHeight(){
	var height = 430;
	var winHeight = $("#contentDiv").height();
	if(winHeight<height){
		height = winHeight;
	}
	return height;
}