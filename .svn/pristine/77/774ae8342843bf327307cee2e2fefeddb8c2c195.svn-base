var commentBefore = '<div class="tk_bd" id="addComment"><textarea class="tk_txt" name="contents" id="contents">';
var commentAfter = '</textarea><input class="tk_btn" type="button" id="cancel" value="取 消" /><input class="tk_btn" type="button" onclick="commentSubmit()" value="发 表" /></div>';
var isApprove;
$(document).ready(function(){
	//单击令联系人信息div隐藏
	$(document).on("click", function(){
		$(".showDetail").hide();//隐藏事件转换DIV
	});
	
	/*返回顶部*/
	$("#html_top").click(function(){
		$("#clientId").animate({scrollTop: 0}, 500);	
	});
	
	//添加评论div
	if(typeof(top.junblockUI)!="undefined"){
		top.junblockUI();
	}
	lableAlign(); //文字间加空格，用于两端对齐
	//排序处理
	if($("#sortInfo").val() != undefined && $("#sortInfo").val() != ""){
		var sorrInfo = $("#sortInfo").val().split(",");
		if(sorrInfo[0] == "ASC"){
			$(".maintop li span[fieldName='"+sorrInfo[1]+"']").attr("sort","DESC")
			$(".maintop li span[fieldName='"+sorrInfo[1]+"']").after('<img src="/style/images/mail/asc.gif" height="10px;" width="8px;"/>')
		}else{
			$(".maintop li span[fieldName='"+sorrInfo[1]+"']").attr("sort","ASC")
			$(".maintop li span[fieldName='"+sorrInfo[1]+"']").after('<img src="/style/images/mail/desc.gif" height="10px;" width="8px;"/>')
		}
	}
	
	//点击input改变背景颜色
	$(".bk_ul :text").click(function(){
		jQuery(".inputBgColor").removeClass("inputBgColor");
		$(this).addClass("inputBgColor");
	})
	
	//改变价格更改总和
	$("input[name$='_Price']").live('change', function() {
		var qty = $(this).parents("li").find("input[name$='_Qty']").val();//个数
		if(qty != ""){
			var amount = $(this).val() * qty;
			$(this).parents("li").find("input[name$='_Amount']").val(amount);
		}
		jQuery("#TotalAmount").val(getTotalCount());//回填总和
	});
	
	//改变个数更改总和
	$("input[name$='_Qty']").live('change', function() {
		var price = $(this).parents("li").find("input[name$='_Price']").val();//价格
		if(price != ""){
			var amount = $(this).val() * price;
			$(this).parents("li").find("input[name$='_Amount']").val(amount);
		}
		jQuery("#TotalAmount").val(getTotalCount());//回填总和
	});
	
	/*当客户列表表头不在可见区域时，固定表头*/
	$("#clientId").scroll(function(){ 
		var maintop = $("#maintop");
		var topNum = $("#clientId").scrollTop();
		var leftNum = $("#clientId").scrollLeft();
		if(topNum>=100){
			maintop.addClass("maintop_fix").css("top",topNum);
			$(".pagetop").show();
		}else{
			maintop.removeClass("maintop_fix");
			$(".pagetop").hide();
		}
		if(leftNum > 1){
			$(".hd .m_r").css("right",$(".hd").width()- $("#clientId").width() - leftNum +20);
		}else{
			$(".hd .m_r").css("right",$(".hd").width()- $("#clientId").width() - leftNum +20);
		}
	});
	
	/*返回顶部*/
	$("#html_top").click(function(){
		$("#clientId").animate({scrollTop: 0}, 500);	
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
	
	/*单击客户列表表头排序*/
	$(".maintop li span").click(function () {
		var sortInfo = $(this).attr("sort") + "," + $(this).attr("fieldName");
		$("#sortInfo").val(sortInfo);
		submitQuery();
	});
	
	/*展开更多查询条件*/
	$("#searchExpand").click(function(){
		if($("#showSearch").css("display")=="none"){
			$("#showSearch").slideDown();
			$(".zk").css({backgroundPosition:'0px -1069px'}).html("收起") ;
			$("#expandStatus").val("open");
		}else{
			$("#showSearch").slideUp();
			$(".zk").css({backgroundPosition:'0px -367px'}).html("展开") ;
			$("#expandStatus").val("close");
		}
	}) ;
	
	/*列表部门人员查询按钮*/
	$(".sbtn_a").click(function(){
		var popName = $(this).attr("popName");
		title = '选择职员';
		if(popName == "deptGroup"){
			title = '选择部门';
		}
		url ="/Accredit.do?popname="+popName+"&inputType=checkbox&chooseData="+$("#employeeId").val();
		asyncbox.open({
			id : 'popId',url : url,title : title,width :'755',height : '435',
			btnsbar :[{text:'清空',action:'selectAll'}].concat(jQuery.btn.OKCANCEL),
		    callback : function(action,opener){
		    	if(action == 'ok'){
		    		var str = opener.strData;
		    		var existIds = ","+$("#employeeId").val();
		    		var newIds = "";//存放新的ID
		    		for(var i=0;i<str.split("|").length;i++){
		    			var selId = ","+str.split("|")[i].split(";")[0]+",";
						if(existIds.indexOf(selId)==-1){
							newIds +=str.split("|")[i].split(";")[0]+",";
						}		    			
		    		}
		    		
		    		$("#employeeId").val($("#employeeId").val()+newIds);
		    	} 
				if(action == 'selectAll'){
					$("#employeeId").val("");
				}
				submitQuery();
		    }
	　    });
	}) 
	
	
	/*添加*/
	$("#addBtn").click(function(){
		//var urls = '/vm/crm/brother/addNew.jsp' ;
		var urls = '/CRMBrotherAction.do?operation=6&tableName='+$("#tableName").val();
		var height = 420;
		var winHeight = $("#clientId").height()+30;
		if(winHeight<height){
			height = winHeight;
		}
		asyncbox.open({
			id:'dealdiv',url:urls,title:'添加',width:820,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
		    callback : function(action,opener){
			    if(action == 'ok'){
			    	opener.beforeSubmit();
					return false;
				}
	　　  　	}
	　  });
	});
	
	/*修改*/
	$(".update").click(function (){
		var tableName = $("#tableName").val();
		var keyId = $(this).parent().parent().attr("id");
		update(tableName,keyId,'dealdiv');
	});
	
	
	/*详情*/
	$(".detail").click(function(){
		var keyId = $(this).parents("ul").attr("id");
		var titleName = $(this).parents("ul").find("em[name='Topic']").text();
		
		if($("#tableName").val() == "CRMPotentialClient"){
			titleName = $(this).parents("ul").find("em[name='ClientName']").text();
		}
		
		if(titleName == ""){
			titleName = "详情";
		}
		
		var height = 400;
		var winHeight = $("#clientId").height()+30;
		if(winHeight<height){
			height = winHeight;
		}
		
		var url = '/CRMBrotherAction.do?operation=5&keyId='+keyId+"&tableName="+$("#tableName").val();
		if($("#tableName").val()=="CRMSaleContract" || $("#tableName").val()=="CRMSalesChance" || $("#tableName").val()=="CRMPotentialClient" || $("#tableName").val()=="CRMSaleFollowUp"){
			mdiwin(url,titleName);
		}else{
			asyncbox.open({id:'DetailDiv',title:titleName,modal:true,url:url,width:780,height:height});
		}
		
	});
	
	/*双击查看详情	*/
	$("#titleId ul").dblclick(function(){
		var keyId = $(this).attr("id");
		var titleName = $(this).find("em[name='Topic']").text();
		
		if($("#tableName").val() == "CRMPotentialClient"){
			titleName = $(this).find("em[name='ClientName']").text();
		}
		
		if(titleName == ""){
			titleName = "详情";
		}
		
		var url = '/CRMBrotherAction.do?operation=5&keyId='+keyId+"&tableName="+$("#tableName").val();
		if(jQuery("#tableName").val()=="CRMSaleContract" || jQuery("#tableName").val()=="CRMSalesChance" || jQuery("#tableName").val()=="CRMPotentialClient" || jQuery("#tableName").val()=="CRMSaleFollowUp"){
			mdiwin(url,titleName);
		}else{
			asyncbox.open({id:'DetailDiv',title:titleName,modal:true,url:url,width:780,height:450});
		}
	})
	
	
	/*删除*/
	$("#deleteBtn").click(function(){
		if(!isChecked("keyId")){
			asyncbox.tips(deleteMsg);
			return false;
		}
		if(!confirm(confirmMsg)){
			return false;
		}else{
			$("#operation").val(operation);
			blockUI();
			form.submit();
		}
	});
	
	/*导出*/
	$("#exportBtn").click(function(){
		if(!isChecked("keyId")){
			asyncbox.tips(deleteMsg);
			return false;
		}
		if(!confirm("确定导出?")){
			return false;
		}else{
			$("#operation").val("99");
			$("#ButtonType").val("billExport");
			blockUI();
			form.submit();
		}
	});
	
	/*导入*/
	$("#importBtn").click(function(){
		var url = "/vm/crm/brother/importData.jsp?tableName="+$("#tableName").val()+"&NoBack=NoBack";
		mdiwin(url,'导入');
	});
	
	
	 //$("#PotentialAllocatee").click(function(){
		
	//});
	/*线索回收*/
	$("#PotentialCallback").click(function(){
		if(!isChecked("keyId")){
			asyncbox.tips(deleteMsg);
			return false;
		}
		
		$("#operation").val("4");
		$("#type").val("PotentialCallback");
		blockUI();
		form.submit();
	});
	
	$(".getDatas").click(function(){
		$("#getDataType").val($(this).attr("itemVal"));
		$("#operation").val("22");
		blockUI();
		form.submit();
	});
	
	//删除明细
	$(".del_sp").live('click', function() {
		if(!confirm("确定删除?")){
			return false;
		}else{
			var tableName = jQuery("#tableName").val();//主表表名
			if(tableName == "CRMSaleContract" || tableName == "CRMsalesQuot"){
				$(this).parents("li").remove();
				var childTableName = jQuery("#childTableName").val();//明细表名
				var totalCount = 0;
			    jQuery("input[name='"+childTableName+"_Amount']").each(function(){
			    	totalCount += parseFloat($(this).val());
			    })
			    jQuery("#TotalAmount").val(totalCount);//回填总和	
			}else{
				$(this).parents("ul").remove();
			}
		}
	})
	
	/*评论*/
	$("li.down-up").click(function(){
		var sc = $(this).attr("sc");
		if(sc == "hide"){
			var keyId = $(this).parents("ul").attr("id");		
			var url = '/DiscussAction.do?tableName=CRMSaleFollowUpLog&f_ref='+keyId+'&parentIframeName=Frame_'+keyId;
			$("#Frame_"+keyId).attr("src",url);
			$(this).css("background-position","-2px -643px").attr("sc","show").siblings(".discuss").show();
		}else{
			$(this).css("background-position","-2px -1030px").attr("sc","hide").siblings(".discuss").hide();
		}	
	});
});

/*textarea改变背景颜色*/
function changeBgColor(obj){
	jQuery(".inputBgColor").removeClass("inputBgColor");
	$(obj).addClass("inputBgColor");
}

/**
*	锁屏
*/
function blockUI(){
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: "<div style='width:135px;'><img src='/style/images/load.gif'/><div style='color:#0179bb;font-weight:bold;'>正在查询，请稍后。。。</div></div>",css:{ background: 'transparent'}}); 
	}
}

/**
*	查询提交
*/
function submitQuery(){
	blockUI();
	form.submit();
}

/*修改*/
function update(tableName,keyId,asynId){
	var urls = '/CRMBrotherAction.do?operation=7&tableName='+tableName+"&keyId="+keyId;
	var isConsole = $("#isConsole").val();//true表示从客户控制台进入
	
	if(isConsole == "true"){
		window.location.href=urls+"&isConsole=true";
	}else{
		var height = 430;
		var winHeight = $("#clientId").height()+30;
		if(winHeight<height){
			height = winHeight;
		}
		asyncbox.open({
			id:asynId,url:urls,title:'修改单据',width:780,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
		    callback : function(action,opener){
			    if(action == 'ok'){
			    	opener.beforeSubmit();
					return false;
				}
	　　  　	}
	　    });
	}
}

/**
*	分页查询
*/
function pageSubmit(pageNo){
	$("#pageNo").val(pageNo);
	blockUI();
	form.submit();
}

var hideFieldName;
/*个人与部门弹出框*/
function popSelect(popName,fieldName){
	hideFieldName = fieldName;//字段名,回填的ID值
	var title = "选择客户";
	if(popName == "userGroup"){
		title = "选择个人"
	}else if(popName == "deptGroup"){
		title = "选择部门"
	}
	var url ="/Accredit.do?popname="+popName;
	asyncbox.open({
		id : 'Popdiv',url : url,title : title,width :755,height : 450,
		btnsbar :[{text:'清空',action:'selectAll'}].concat(jQuery.btn.OKCANCEL),
	    callback : function(action,opener){
	   	if(action == 'ok'){
	    		var str = opener.strData;
	    		jQuery("#"+fieldName).val(str.split(";")[0]);
				jQuery("#"+fieldName+"Name").val(str.split(";")[1]);
				
				if(fieldName == "ClientId"){
					jQuery("#f_brother").val(str.split(";")[0]);
				}
				getRelateEnumer();
	    	} 
			if(action == 'selectAll'){
				jQuery("#"+fieldName).val("");
				jQuery("#"+fieldName+"Name").val("");
			}
			//如果是线索分配，则选择用户后提交记录
			if(fieldName=='EmployeeID'&&jQuery("#EmployeeID").val().length>0){
				$("#operation").val("4");
				$("#type").val("PotentialAllocatee");
				blockUI();
				form.submit();
			}
	    }
　  });
}

/*个人部门客户弹出框双击回填方法*/
function fillData(datas){
	jQuery("#"+hideFieldName).val(datas.split(";")[0]);
	jQuery("#"+hideFieldName+"Name").val(datas.split(";")[1]);
	if(hideFieldName == "ClientId"){
		jQuery("#f_brother").val(datas.split(";")[0]);
	}
	parent.jQuery.close('Popdiv');
	getRelateEnumer();
}

/*自定义弹出框*/
function definePop(urls,fieldName,displayName){
	if(jQuery("#ClientId").val()==""){
		alert("请先选择客户");
		return false;
	}
	hideFieldName = fieldName;
	var url = urls+"&ClientId="+jQuery("#ClientId").val()+"&src=menu&MOID=$MOID&MOOP=add&popupWin=Popdiv&popupWin=MainPopup";
	asyncbox.open({id:'Popdiv',title:displayName,url:url,width:750,height:470});
}

/*自定义弹出框回填方法*/
function exePopdiv(returnValue){
	if(typeof(returnValue)=="undefined") return ;
	var note = returnValue.split("#;#") ;
	jQuery("#"+hideFieldName).val(note[0]);
	jQuery("#"+hideFieldName+"Name").val(note[1]);
}

//提交表单前验证
function beforeSubmit(){
	var isError = "false" //用于验证是否错误，true表示有错并返回;
	//判断主表input类型
	jQuery(".bk_ul").find(":input[notNull='true']").each(function(){
		if(jQuery.trim($(this).val()) == ""){
			alert($(this).attr("displayName")+"不能为空!");
			$(this).focus();
			isError = "true";
			return false;
		}
	})

	if(isError == "true"){
		return false;
	}
	
	//检测商品值
	var tableName = jQuery("#tableName").val();
	if(tableName == "CRMsalesQuot" || tableName == "CRMSaleContract"){
		jQuery(":input[name$='Qty']").each(function(){
			if($.trim($(this).val()) == ""){
				alert("数量不能为空,请输入");
				$(this).focus();
				isError = "true";
				return false;
			}else if(isNaN($(this).val())){
				alert("数量只能输入数字,请修改");
				$(this).select();
				isError = "true";
				return false;
			}
		})
		
		jQuery(":input[name$='Price']").each(function(){
			if($.trim($(this).val()) == ""){
				alert("单价不能为空,请输入");
				$(this).focus();
				isError = "true";
				return false;
			}else if(isNaN($(this).val())){
				alert("单价只能输入数字,请修改");
				$(this).select();
				isError = "true";
				return false;
			}
		})
	}
	
	
	if(isError == "true"){
		return false;
	}
	
	
	//特殊处理
	if($("#tableName").val() == "CRMSaleFollowUp"){
		//销售跟单
		if(jQuery("#GenWorkPlan").val() == "1" && jQuery("#NextVisitTime").val()==""){
			alert("生成工作计划为'是'时，生成的工作计划日期'下次跟进时间'不能为空");
			jQuery("#NextVisitTime").focus();
			return false;
		}
		
		if(jQuery("#NextVisitTime").val() !=""){
			var nextVisitTime = new Date(jQuery("#NextVisitTime").val());
			var visitTime = new Date(jQuery("#VisitTime").val());
			if(nextVisitTime.valueOf()<visitTime.valueOf()){
				alert("下次跟进时间必须大于跟进时间。");
				return false;
			}
		}
	}
	
	form.submit();
}

/*新增、修改操作成功后回调方法*/
function dealAsyn(){
	if(jQuery.exist('crmOpDiv')){
		pageRefresh();//客户列表还原客户成功操作
	}else if(jQuery.exist('brotherOperation')){
		jQuery.close('brotherOperation');//关闭详情页面按钮		
		ajaxLoadBrotherList(tempTableName,'1');
	}else if(jQuery.exist('alertDiv')){
		//如果是提醒。关闭弹出框不刷新页面		jQuery.close('alertDiv');		
	}else if(jQuery.exist('detailUpdateId')){
		if(jQuery.exist('brotherDetail')){
			jQuery.close('detailUpdateId');//关闭详情页面按钮
			jQuery.close('brotherDetail');//关闭修改页面按钮		
			ajaxLoadBrotherList(tempTableName,'1');
		}else{
			window.location.reload();
		}
		/*
		方法2
		if(jQuery.exist('DetailDiv')){
		//如果是邻居表点击详情(弹出框)的,点击修改成功后 1.关闭修改弹出框 2.刷新详情页面
			jQuery.close('detailUpdateId');//关闭修改页面	
			jQuery.opener('DetailDiv').window.location.reload();//刷新详情页面
		}else{
			window.location.reload();//兄弟表详情页面修改成功刷新页面
		}*/
	}else{
		//window.location.reload();
		window.location.href="/CRMBrotherAction.do?tableName="+jQuery("#tableName").val();	
	}
}

//在文字后加空格，chrome下实现两端对齐

function lableAlign(){
	var nLength = new Array();
	var reg = /^[u4E00-u9FA5]+$/;
	$('.d_test').each(function(){
		var a = $(this).text();
		var str = '';
		var zStr = '';
		for(var i=0; i<a.length;i++){
			str += a.charAt(i)+' ';
			if(!reg.test(a.charAt(i))){
				zStr += a.charAt(i);
			}
		}
		nLength.push(zStr.trim().length);
		$(this).text(str);	
	});
}

/*详情连接修改方法*/
function billUpdate(keyId,tableName){
	var urls = '/CRMBrotherAction.do?operation=7&tableName='+tableName+"&keyId="+keyId;
	asyncbox.open({
		id:'dealdiv',url:urls,title:'修改跟单',width:780,height:450,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	opener.beforeSubmit();
				return false;
			}
　　  　	}
　  });
}

/*模糊查询*/
function keywordSubmit(){

	//判断模糊查询信息
	var pattern = /^[a-z\d\u4E00-\u9FA5]+$/i;   /*正则表达式，只能输入中文、字母、英文*/
	if($.trim($("#keyword").val())!=""){
		if(!pattern.test($("#keyword").val())){
			alert( "查询内容含有特殊字符,只能输入中文、字母、数字"); 
			$("#keyword").select();
			return false;
		}
	}
	
	//若关键字为空或者当前关键字与原来搜索的关键字不同时页码给1
	if($("#rekeyword").val()=="" || $("#keyword").attr("reValues") != $("#keyword").val()){
		$("#pageNo").val("1");
	}
	
	blockUI();
	form.submit();
}

function goodsSelect(){
	var urls = '/CRMBrotherAction.do?operation=4&type=goodsTree';
	asyncbox.open({
		id:'goodDivss',url:urls,title:'商品弹出框',width:960,height:500,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	var str = opener.returnVal();//返回值
		    	var goodsArr = str.split(";");//转换成数组		    	
		    	var childTableName = jQuery("#childTableName").val();//明细表名
		    	
		    	//input文本框name的名称，用于后台封装map中
		    	var fieldGoodsName = childTableName +"_GoodsCode"//商品名称
		    	var fieldUnit = childTableName +"_Unit"//单位
		    	var fieldQty = childTableName +"_Qty"//数量
		    	var fieldPrice = childTableName +"_Price"//单价
		    	var fieldUnit = childTableName +"_Unit"//单价
		    	var fieldAmount = childTableName +"_Amount"//总和
		    	
		    	var totalCount = 0;
		    	var liStr = "";//存放li信息
		    	for(var i=0;i<goodsArr.length;i++){
		    		if(typeof(goodsArr[i].split(":")[0])!="undefined" && goodsArr[i].split(":")[0] != ""){
			    		liStr +='<li class="det_li"><span class="det_sp"><input type="hidden" name="'+fieldGoodsName+'" value="'+goodsArr[i].split(":")[0]+'"><input type="text" class="ip_txt" value="'+goodsArr[i].split(":")[1]+'" readonly="readonly"/></span><span class="det_sp"><input type="hidden" name="'+fieldUnit+'" value="'+goodsArr[i].split(":")[3]+'"><input type="text" class="ip_txt" value="'+goodsArr[i].split(":")[4]+'" readonly="readonly"/></span><span class="det_sp"><input type="text" class="ip_txt" name="'+fieldQty+'" value="1"/></span>';
			    		liStr +='<span class="det_sp"><input type="text" class="ip_txt" value="'+goodsArr[i].split(":")[2]+'" name="'+fieldPrice+'"/></span><span class="det_sp"><input type="text" class="ip_txt" name="'+fieldAmount+'" readonly="readonly" value="'+goodsArr[i].split(":")[2]+'" /></span><span class="del_sp" title="删除"></span></li>';
			    		totalCount += parseFloat(goodsArr[i].split(":")[2]);//累加总和
		    		}
		    	}
		    	jQuery("#goodsContent").append(liStr);
		    	jQuery("#TotalAmount").val(parseFloat(jQuery("#TotalAmount").val()) + totalCount);//回填总和
			}
　　  　	}
　  });
}

function dbGoodsSelect(goodInfo){
	var childTableName = jQuery("#childTableName").val();//明细表名
	var fieldGoodsName = childTableName +"_GoodsCode"//商品名称
   	var fieldUnit = childTableName +"_Unit"//单位
   	var fieldQty = childTableName +"_Qty"//数量
   	var fieldPrice = childTableName +"_Price"//单价
   	var fieldUnit = childTableName +"_Unit"//单价
   	var fieldAmount = childTableName +"_Amount"//总和
   	
   	var totalCount = 0;
   	var liStr = "";//存放li信息
	if(typeof(goodInfo.split(":")[0])!="undefined" && goodInfo.split(":")[0] != ""){
 		liStr +='<li class="det_li"><span class="det_sp"><input type="hidden" name="'+fieldGoodsName+'" value="'+goodInfo.split(":")[0]+'"><input type="text" class="ip_txt" value="'+goodInfo.split(":")[1]+'" readonly="readonly"/></span><span class="det_sp"><input type="hidden" name="'+fieldUnit+'" value="'+goodInfo.split(":")[3]+'"><input type="text" class="ip_txt" value="'+goodInfo.split(":")[4]+'" readonly="readonly"/></span><span class="det_sp"><input type="text" class="ip_txt" name="'+fieldQty+'" value="1"/></span>';
 		liStr +='<span class="det_sp"><input type="text" class="ip_txt" value="'+goodInfo.split(":")[2]+'" name="'+fieldPrice+'"/></span><span class="det_sp"><input type="text" class="ip_txt" name="'+fieldAmount+'" readonly="readonly" value="'+goodInfo.split(":")[2]+'" /></span><span class="del_sp" title="删除"></span></li>';
 		totalCount += parseFloat(goodInfo.split(":")[2]);//累加总和
	}
	var totalCount = parseFloat(jQuery("#TotalAmount").val()) + parseFloat(goodInfo.split(":")[2]);
	jQuery("#goodsContent").append(liStr);
	jQuery("#TotalAmount").val(totalCount);//回填总和
	$.close('goodDivss');
}

//根据明细获取总金额
function getTotalCount(){
	var totalCount = 0;
	jQuery("input[name$='_Amount']").each(function(){
		totalCount += parseFloat(jQuery(this).val());
	})
	return totalCount;
}

/*输入页码检测*/
function checkPageNo(selectPageNo,totalPage){
	if(isNaN(selectPageNo)){
		alert("只能输入数字.");
		$("#pageNo").val("");
		$("#pageNo").focus();
		return false;
	}
	
	if(selectPageNo<0){
		alert("页数不能输入负数");
		$("#pageNo").focus();
		return false;
	}
	
	if(parseInt(selectPageNo)>parseInt(totalPage)){
		alert("输入的页数已超出最大页数，请重新输入");
		$("#pageNo").focus();
		return false;
	}
	return true;
}

/*添加明细*/
function addChild(){
	var copyUl = jQuery("#childContent").find("ul[class='det_cul']:first").clone(true);
	jQuery("#childContent").append(copyUl);
	jQuery("#childContent").find("ul:last :input").val("");
	jQuery("#childContent").find("ul:last .del_sp").show();
	
}

/*选择客户后，根据存在的关联客户下拉框类型异步获取option选项*/
function getRelateEnumer(){
	var fieldNames = "";	
	jQuery("select[inputType='20']").each(function(){
		fieldNames += $(this).attr("id")+",";	
	})
	if(fieldNames!=""){
		$.ajax({
			type: "POST",
			dataType:"json",
			url: "/CRMBrotherAction.do",
			data: "operation=4&type=releateEnumer&tableName="+jQuery("#tableName").val()+"&fieldNames="+fieldNames+"&clientId="+jQuery("#ClientId").val(),
			success: function(msg){
				for (var index in msg) { 
					//msg表示js对象, index是key, msg[index]是值   
					jQuery("#"+index).html(msg[index])
				}
			}
		});	
	}
}





function fieldSetting(){
	var urls = "/CRMBrotherSettingAction.do?tableName="+$("#tableName").val();
	asyncbox.open({
		id:'addModuleId',title :'字段设置',url:urls,cache:false,modal:true,width:'1020',height:500,
		callback : function(action,opener){
			if(action == 'close'){
				parent.jQuery.close('modulediv');
			}
		}
	});
}

/*连接进入客户详情页面*/
function showClient(keyId,clientName){
	mdiwin('/CRMClientAction.do?operation=5&type=detailNew&keyId='+keyId,clientName);
}

/*显示联系人*/
function showContact(contactId,obj){
	$.ajax({
	type: "POST",
	url: "/CRMBrotherAction.do",
	data: "operation=4&type=contactInfo&contactId="+contactId,
	success: function(msg){
		if(msg == "error"){
			
		}else{
			var contactInfo = msg.split(",");
			var mobile = contactInfo[1];
			var email = contactInfo[2];
			
			var str = "<b></b><i>"+contactInfo[0] +"</i><br/>";
			
			if(mobile != ""){
				str +="<i>"+mobile+"</i><br/>";
			}
			
			if(email != ""){
				str +="<i class='email' title='"+email+"'>"+email+"</i>";
			}
			$(obj).parent().parent().next().html(str);
			$(obj).parent().parent().next().show();
		}
	}
	});
}

/*提醒*/
function alertSet(relationId,planTitle){
	var tableName = $("#tableName",document).val()
	var urls=encodeURIComponent('/CRMBrotherAction.do?operation=5&alertEnter=true&tableName='+tableName+"&keyId="+relationId);
	var typestr=encodeURIComponent(planTitle+'提醒：');
	var title=encodeURIComponent(planTitle);
	var date= new Date();
	var url = "/UtilServlet?operation=alertDetail&relationId="+relationId+"&falg=true"+"&title="+title+"&typestr="+typestr+"&urls="+urls+"&date="+date;
	var height = 490;
	if($(document).height()<400){height=360}
	asyncbox.open({
		id : 'alertDiv',url : url,
		cache : false,modal : false,		
	 	title : '提醒设置',
　　 　	width : 600, height : height, top :5,
	    callback : function(action,opener){
		    if(action == 'ok'){ 
		    	opener.checkAlertSet();
		    	return false;
			}
　　      }
 　 });
}

/*审核*/
function approve(keyId,titleName,tableName){
	var title="审核";
	if(titleName == "deliver"){
		title="转交";
	}
	var url = '/OAMyWorkFlow.do?&operation=82&keyId='+keyId+"&tableName="+tableName;
	asyncbox.open({
		id:'deliverTo',url:url,title:title,width:650,height:370,
        btnsbar:jQuery.btn.OKCANCEL,
	    callback:function(action,opener){
		    if(action == 'ok'){
				if(opener.beforSubmit(opener.form)){
					opener.form.submit();
				}
				return false;
			}
　　     }
　 });
}

/*审核成功后返回按钮*/
function dealAsyncbox(){
	//window.location.href="/CRMBrotherAction.do?tableName="+jQuery("#tableName").val();
	window.location.reload();
}


/*流程图*/
function workFlowDepict(keyId){ 
	var designId = $("#designId").val()
	window.open("/OAMyWorkFlow.do?operation=5&applyType="+designId+"&keyId="+keyId,null,"height=570, width=1010,top=50,left=100 ");
}

/*添加页面点击转交*/
function approveBeforeAdd(){
	jQuery("#approveBefore").val("true");//设为true,操作成功返回index.jsp的dealCheck(keyId)方法
	beforeSubmit();
}



//地区弹出框
function openDistrict(){
	asyncbox.open({
		id : 'crmOpenId',
　　　   	url : '/CRMopenSelectAction.do?operation=4',
	 	title : '选择地区',
　　 　 	width :610,
　　 	　	height : 450,
		btnsbar : jQuery.btn.OKCANCEL,
		callback : function(action,opener){
		    if(action == 'ok'){
		    	var info = opener.returnVal();
		    	if(info == ","){
		    		alert("必须选择一个");
		    		return false;
		    	}
		    	dealDistrict(info);
		   	}
　　  	}
　    });
}

//地区弹出框处理函数
function dealDistrict(districtInfo){
	var info = districtInfo.split(",");
	/*
	if(info[0].length > 13){
		info[0] = info[0].substring(0,13) + "..."
	}
	*/
	jQuery("#districtName").val(info[0]);
	jQuery("#District").val(info[1]);

}

//行业弹出框
function openTrade(){
	var height = 540;
	var winHeight = $("#clientId").height()+30;
	if(winHeight<height){
		height = winHeight;
	}
	asyncbox.open({
		id : 'crmOpenId',
　　　   	url : '/CRMopenSelectAction.do',
	 	title : '行业',
　　 　 	width :780,
　　 　	height : height,
    	callback : function(action,opener){
		    
　　  　 }
　   });
}

//行业弹出框处理函数
function dealTrade(professionId,professionName){
	jQuery("#Trade").val(professionId);
	/*
	if(professionName.length > 13){
		professionName = professionName.substring(0,13) + "..."
	}
	*/
	jQuery("#tradeName").val(professionName);
}

/*客户转移*/
function clientTransfer(operationFlag,moduleId){
	if(operationFlag=="detail"){
		var keyId = $("#keyId").val();
		$.ajax({
		    type: "POST",
		    url: "/CRMBrotherAction.do",
		    data: "operation=1&type=clientTransfer&operationFlag=detail&moduleId="+moduleId+"&keyId="+keyId,
		    success: function(msg){
		    	if(msg == "success"){
		    		window.location.href="/CRMClientAction.do?operation=5&type=detailNew&clientTransferEnter=true&keyId="+keyId;
		    	}else if(msg == "error"){
		    		asyncbox.tips('转移错误','error');
		    	}else{
		    		alert(msg);
		    	}
		    }
		});	
	}else{
		if(!isChecked("keyId")){
			asyncbox.tips(deleteMsg);
			return false;
		}
		$("#operation").val("1");
		$("#type").val("clientTransfer");
		$("#moduleId").val(moduleId);
		form.submit();
	}
}

function openInputDate(obj){
	WdatePicker({lang:'zh_CN'});
}

function hideForm(){
	//jQuery("#isNewCalendar option[value='2']").attr("selected","selected")
	jQuery("#addCalendar").html("");
	jQuery("#addCalendar").hide();
}

/*添加日程*/
function addCalendar(){
	if(jQuery("#ClientIdName").val()==""){
		asyncbox.tips('请先选择客户');
	}else{
		var clientId = jQuery("#ClientId").val();
		$.ajax({
			type: "POST",
			url: "/OACalendarAction.do",
			data: "operation=6&type=addCalendar&crmEnter=true&onlyShow=true&calendarClientId="+clientId,
			success: function(msg){			
				jQuery("#addCalendar").html(msg);
				jQuery("#addCalendar").show();
			}
		});
		
		
	}
}


 /*添加*/
 function saveForm(){
   	var title=jQuery("#calendarTitle").val();//标题
 	var stratTime=jQuery("#calendarStratTime").val();//开始时间
   	var finishTime=jQuery("#calendarFinishTime").val();//结束时间
   
    if(stratTime>finishTime){
   		highlight(jQuery("#calendarFinishTime"),5);
   	 	return false;
    }
   
   	if(title==""){
   		highlight(jQuery("#calendarTitle"),5)
   		return false;
   	}   
   	//shijian  
   	var alertTime="";
   	
   	//判断点击了提醒图标
   	if(jQuery("#bgg").attr('bg')=="t"){
   		var d = new Date();
   		var dayTime=jQuery("#dayTime").val();
   		var hours=jQuery("#hours").val();
   		var minutes=jQuery("#minutes").val();
   		
   		var alertTime = dayTime +" "+hours+":"+minutes+":00";
   		var alertDate=new Date(alertTime);
		var nowDate=new Date();
   		if(alertDate<nowDate){
			asyncbox.tips('提醒时间不得小于当前时间');
			return false;
		}
   	}
  	
   	//保存 跟新
   	var clientId=jQuery("#calendarClientId").val();
   	
	jQuery.ajax({
  	 	type: "POST",url: '/OACalendarAction.do?operation=1',
  	 	data:{
       	 		title:title,
       	 		stratTime:stratTime,
       	 		finishTime:finishTime,      	 		
       	 		type:'客户日程',     	 	     	 		
   				alertTime:alertTime,
   				clientId:clientId  				  	 	
       	 	},		   
   	 	success: function(msg){
   	 		asyncbox.tips('操作成功','success');
   	 		hideForm();
   	 	}
	});	 					
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

/**提醒时间函数*/
function showTime(obj){
	var fg = jQuery("#bgg").attr('bg');
	if(fg=="t"){
		jQuery("#bgg").attr('bg','f');
		jQuery("#setTime").hide();
		jQuery(obj).removeClass("b-remind-h").attr("title","设置提醒");
	}else{
		jQuery("#bgg").attr('bg','t');
		jQuery("#setTime").show();
		jQuery(obj).addClass("b-remind-h").attr("title","取消提醒");
	}	
}
/*线索分配*/
function PotentialAllocatee(){
	if(!isChecked("keyId")){
		asyncbox.tips(deleteMsg);
		return false;
	}
	popSelect("userGroup","EmployeeID");
}

//扩展按扭
function extendSubmit(vars,vname,selected)
{
	document.getElementById("ButtonTypeName").value = vname;
	form.defineName.value=vars;
	 
	form.operation.value=25
	form.submit();
}