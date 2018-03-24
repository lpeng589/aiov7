<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<link type="text/css" rel="stylesheet"
	href="/style/css/brother_detail.css" />
<link type="text/css" rel="stylesheet"
	href="/js/skins/ZCMS/asyncbox.css" />
<link type="text/css" rel="stylesheet"
	href="/style/css/oaCalendarAdd.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/oa/oaCalendarAdd.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/crm_brotherPublic.js"></script>
<script type="text/javascript" src="/js/crm/crm_operation.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/crm/upload.vjs"></script>
<script type="text/javascript">
/**wyy*/
function addCollection(oTopicId){
	var flag = $("#CRM_"+oTopicId).attr('bg');
	 var url = "/OACollectionAction.do?operation=2&attType="+flag+"&typeName=CRMClientInfo&keyId="+oTopicId;
	 jQuery.ajax({
	 	 type: "POST",
	  	 url: url,	
	  	 data:{
	  	 	urlparam:"/CRMClientAction.do?operation=5&type=detailNew&keyId="+oTopicId,
	  	 	titleName:"客户:$!result.get('ClientName')"
	  	 },	   
	  	 success: function(msg){
	  	 	if(flag == "add"){
	  	 		if(msg == "OK"){
		  	 		asyncbox.tips('收藏成功','success');
		  	 		$("#CRM_"+oTopicId).attr('bg','del');
		  	 		$("#CRM_"+oTopicId).html('取消收藏');		  	 		
	  			}else{
	  				asyncbox.tips('收藏失败','error');		    
	  			}
	  	 	}else{
	  	 		if(msg == "OK"){
		  	 		asyncbox.tips('取消收藏成功','success');
		  	 		$("#CRM_"+oTopicId).attr('bg','add');
		  	 		$("#CRM_"+oTopicId).html('加入收藏');
	  			}else{
	  				asyncbox.tips('取消收藏失败','error');		    
	  			}
	  	 	}	  	 	
	  	}
	});
}

var tempTableName;//用于添加单据成功后,异步刷新相应的邻居表部分
var hideShowFieldValDiv;//存放隐藏的显示字段DIV
var hideEditPic;//存放隐藏的编辑图标
















$(function(){
	//表示线索详情点击转移，成功操作后跳转到客户列表的详情页面
	#if("$!clientTransferEnter" == "true")
		asyncbox.tips('客户转移成功!','success');
	#end
	
	
	//产品明细ul宽度
	var lLen = $(".det_ul .det_li").length;
	var lnum = lLen-1;
	$(".det_ul").width(lLen*125+16).find(".det_li:eq("+lnum+")").css("background","none");
	$(".det_cul").width(lLen*125+16);
	$(".bk_ul :text:visible").click(function(){
		jQuery(".inputBgColor").removeClass("inputBgColor");
		$(this).addClass("inputBgColor");
	});
	
	 $(".btn_item").hover(
	    function() { $(this).find("span").stop().animate({"width":"130px"},300).find("p").fadeIn("300");},
	    function() { $(this).find("span").stop().animate({"width":"40px"},300).find("p").fadeOut("300");}
	 );
	/*循环邻居表部分显示列表*/
	$(".in_dv").each(function(){
		var tableName = $(this).attr("id");
		ajaxLoadBrotherList(tableName,'1');
	})
	
	/*点击分页触发事件*/
	$('.page_a').live('click', function() {
		var tableName = $(this).parent().attr("tableName");
		ajaxLoadBrotherList(tableName,$(this).attr("pageNo"));
	});
	
	/*点击分页跳转按钮*/
	$('.go').live('click', function() {
		var selectPageNo = $(this).prev().val();//当前选择的页数








		var totalPage = $(this).attr("totalPage");//总数
		var tableName = $(this).parent().attr("tableName");
		
		if(selectPageNo<=totalPage){
			ajaxLoadBrotherList(tableName,selectPageNo);
		}
	});
	//右边宽度
	$(".inputbox").width($(".inWrap").width()-230);
	
	//快速定位








	var bWidth = document.body.offsetWidth;
	/*
	var zz = (bWidth-980)/2;
	if(zz < 50){
		$("#quickPositionDiv").css("left",910);
	}else{
		$("#quickPositionDiv").css("left",zz+980);
	}
	*/
	
	/*点击编辑字段*/
	$('.bk_li .update_i').live('click', function() {
		
		//若已有编辑先把之前的还原
		if($(".submitBtn").length>0){
			cancelFieldEdit($(".cancelBtn"));//调用还原方法
		}
		var fieldName =$(this).attr("fieldName");//字段名








		var inputType =$(this).attr("inputType");//输入类型
		var fieldType =$(this).attr("fieldType");//FieldType
		var fieldValue =$(this).attr("fieldValue");//字段值









		hideShowFieldValDiv = $(this).prev().clone();//存放展示值的div,点击取消按钮还原
		hideEditPic = $(this).clone();//编辑按钮
		var inputStr = '<input class="update_inp" type="text" id="'+fieldName+'" name="'+fieldName+'" value="'+fieldValue+'"';//输入类型拼接input框









		var operationBtn = ' <div class="btn btn-mini btn-danger submitBtn">确定</div><div class="btn btn-mini cancelBtn">取消</div> ';//确定修改按钮
		if(inputType == "0"){
			if(fieldType == "1"){
				inputStr +=' isDouble="true" >';//表示双精度，只能输入数字
			}else if(fieldType == "5"){
				inputStr +=' onClick="WdatePicker({lang:\'$globals.getLocale()\'})" readonly="readonly" >';
			}else if(fieldType == "6"){
				inputStr +='onClick="WdatePicker({lang:\'$globals.getLocale()\',dateFmt:\'yyyy-MM-dd HH:mm:ss\'})" readonly="readonly" >';
			}else if(fieldType == "3" || fieldType == "16"){
				inputStr ='<textarea class="update_txt" id="'+fieldName+'" name="'+fieldName+'" >'+fieldValue+'</textarea>';
			}else if(fieldType == "18"){
				inputStr +=">";
			}else{
				if(fieldName == "Trade"){
					inputStr = '<div class="swc_lt"><input type="hidden" id="'+fieldName+'" name="'+fieldName+'" value="'+fieldValue.split(":")[0]+'">';
					inputStr += '<input  name="tradeName"  id="tradeName" value="'+fieldValue.split(":")[1]+'" type="text" class="inp_w" ondblclick="openTrade()" readonly="readonly"/><a href="#" onclick="openTrade()"></a></div>'
				}else if(fieldName == "District"){
					inputStr = '<div class="swc_lt"><input type="hidden" id="'+fieldName+'" name="'+fieldName+'" value="'+fieldValue.split(":")[0]+'" >';
					inputStr += '<input  name="districtName"  id="districtName" value="'+fieldValue.split(":")[1]+'" type="text" class="inp_w" ondblclick="openDistrict()" readonly="readonly" /><a href="#" onclick="openDistrict()"></a></div>'
				}else{
					inputStr +=">";
				}
			}
			
		}else if(inputType == "1"){
			//$(this).prev().remove();//删除展示DIV
			//$(this).next().show();//显示隐藏的select
			//$(this).next().after(operationBtn);
		}else if(inputType == "2"){
		}else if(inputType == "5"){
		}else if(inputType == "10"){
		}else if(inputType == "14"){
			inputStr = '<div><input type="hidden" id="'+fieldName+'" name="'+fieldName+'" value="'+fieldValue.split(":")[0]+'" >';
			inputStr += '<input  name="'+fieldName+'Name"  id="'+fieldName+'Name" value="'+fieldValue.split(":")[1]+'" type="text" class="inp_w" readonly="readonly" ondblclick="deptPop(\'deptGroup\',\''+fieldName+'\')" style="border-right: 0;width: 168px;"/><a href="#" onclick="deptPop(\'deptGroup\',\''+fieldName+'\')"  style="border:1px solid #bbb;height:19px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%"></a></div>'
		}else if(inputType == "15"){
			inputStr = '<div><input type="hidden" id="'+fieldName+'" name="'+fieldName+'" value="'+fieldValue.split(":")[0]+'" >';
			inputStr += '<input  name="'+fieldName+'Name"  id="'+fieldName+'Name" value="'+fieldValue.split(":")[1]+'" type="text" class="inp_w" readonly="readonly" ondblclick="deptPop(\'userGroup\',\''+fieldName+'\')" style="border-right: 0;width: 168px;"/><a href="#" onclick="deptPop(\'userGroup\',\''+fieldName+'\')"  style="border:1px solid #bbb;height:19px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%"></a></div>'
		}else if(inputType == "20"){
		}else{
			inputStr +=">";
		}
		
		if(inputType == "1" || inputType == "5" || inputType == "10"){
			//枚举类型显示select标签，并在标签后添加操作按钮
			$(this).prev().remove();//删除展示DIV
			$(this).next().show();//显示隐藏的select
			$(this).next().after(operationBtn);
		}else{
			inputStr +=operationBtn;//拼接操作按钮
			$(this).prev().remove();//删除展示DIV
			$(this).before(inputStr);//编辑数据
		}
		$("#"+fieldName).focus(); //给予input焦点
		$(this).remove();//删除编辑按钮	 
		
	});
	
	/*编辑字段确定按钮*/
	$('.submitBtn').live('click', function() {
		var fieldName = hideEditPic.attr("fieldName");//字段名









	    var fieldValue = "";//字段值









	    var fieldValueDisplay = "";//显示名称.用于如枚举，弹出框，等等
	    
	    if(hideEditPic.attr("inputType") == "1"){
	    	if($("#"+fieldName+"_select").val()!=""){
	    		fieldValue = $("#"+fieldName+"_select").val();
	    		fieldValueDisplay = $("#"+fieldName+"_select :selected").text();
	    	}
	    }else if(fieldName == "Trade" || fieldName == "District"|| hideEditPic.attr("inputType") == "14" || hideEditPic.attr("inputType") == "15"){
	    	var divLabel = $(this).prev();
	    	fieldValue = divLabel.find("input:first").val();
	    	fieldValueDisplay = divLabel.find("input:last").val();
	    }else{
	    	fieldValue = $(this).prev().val();
	    }
	    
	    if(jQuery.trim(fieldValue)==""){
	    	cancelFieldEdit($(".cancelBtn"));//调用还原方法
	    }else{
			jQuery.ajax({
			   type: "POST",
			   url: "/CRMClientAction.do",
			   data: "operation=2&tableName=$tableName&type=detailUpdate&clientId=$!result.get("id")&moduleId=$!moduleId&viewId=$!viewId&fieldName="+fieldName+"&fieldValue="+encodeURIComponent(fieldValue),
			   success: function(msg){
			   		if(msg == "error"){
			   			asyncbox.tips('客户名称重复,请重新输入','error');
			   		}else{
			   			asyncbox.tips('修改成功','success','400');
			   			//操作成功修改信息
			   			
			   			if(fieldName == "Trade" || fieldName == "District"|| hideEditPic.attr("inputType") == "14" || hideEditPic.attr("inputType") == "15"){
			   				hideEditPic.attr("fieldValue",fieldValue+":"+fieldValueDisplay);
			   			}else{
				   			hideEditPic.attr("fieldValue",fieldValue);
			   			}
			   			
			   			//fieldValueDisplay!=""表示特殊处理
			   			if(fieldValueDisplay!=""){
				   			fieldValue = fieldValueDisplay;
			   			}
			   			hideShowFieldValDiv.text(fieldValue);
			   			hideShowFieldValDiv.attr("title",fieldValue);
			   			cancelFieldEdit($(".cancelBtn"));//调用还原方法
			   		}
			   }
			});
	    }
	});
	
	/*编辑字段取消按钮*/
	$('.cancelBtn').live('click', function() {
		cancelFieldEdit($(this));
	});
	
})

/*取消字段编辑*/
function cancelFieldEdit(obj){
		var selectObj = $(obj);//存放当前对象
		if(hideEditPic.attr("inputType") == "1"){
			$(obj).prev().prev().hide();//若是枚举类型,隐藏select标签。









			selectObj = $(obj).prev().prev();//当前selectObj对象为select标签，把hideShowFieldValDiv与hideEditPic放他之前
		}else{
			$(obj).prev().prev().remove();
		}
		
		selectObj.before(hideShowFieldValDiv);//还原展示数据div
		selectObj.before(hideEditPic);//还原编辑按钮
		//删除确定与取消按钮








		$(".submitBtn").remove();
		$(".cancelBtn").remove();
}


function scrollDiv(){
	var sHeight=document.documentElement.clientHeight;
	document.getElementById("contentDiv").style.height=sHeight-3+"px";
}


/*修改客户*/
function updateClient(keyId,viewId,nowHead,contactFlag){
	var nowHead ='1';
	var height = 500;
	var winHeight = $("#contentDiv").height();
	if(winHeight<height){
		height = winHeight;
	}
	var url = "";
	var title = "修改客户";
	if(contactFlag == "true"){
		url = "/CRMClientAction.do?operation=7&nowHead=2&linkMan=true&addContact=true&moduleId=$!moduleId&keyId="+keyId+"&viewId="+viewId;
		height = height -100;
		title = "新增联系人";
	}else{
		url = "/CRMClientAction.do?operation=7&moduleId=$!moduleId&keyId="+keyId+"&nowHead=" + nowHead +"&viewId="+viewId;
	}
	asyncbox.open({
		id:'dealdiv',url:url,title:title,width:840,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	opener.beforeSubmit();
				return false;
			}
　　  　 }
　  });
}

/*新增联系人*/
function addContact(){
	var nowHead ='1';
	var height =290;
	var url = "";
	var title = "新增联系人";
	url = '/CRMClientAction.do?operation=6&type=addContact&moduleId=$!moduleId&viewId=$viewId&clientId=$result.get("id")';
	//var url = "/CRMClientAction.do?operation=7&tableName=CRMClientInfo&contactTableName=CRMClientInfoDet&keyId="+keyId+"&nowHead=" + nowHead;
	asyncbox.open({
		id:'crmOpDiv',url:url,title:title,width:750,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	opener.beforeSubmit();
				return false;
			}
　　  　 }
　  });
}

//添加，更新成功后的处理








function crmOpDeal(){
	jQuery.close("crmOpDiv"); 
	window.location.reload();
}


/*查看历史信息*/
function history(clientId){
	var url = "/CRMClientAction.do?operation=4&type=history&clientId="+clientId;
	asyncbox.open({
		id:'crmOpDiv',url:url,title:'查看历史记录',width:800,height:450,cache:false,modal:true
　  });
}

function pageRefresh(){
	alert("还原成功");
	jQuery.close('crmOpDiv');
	window.location.reload();
}

/*流程图*/
function flowDepict(){ 
	window.open("/OAMyWorkFlow.do?keyId=$!keyId&operation=5&applyType=$!designId",null,"height=570, width=1010,top=50,left=100 ");
}

/*审核*/
function checkDialog(url){
	var urls = url+"&checkFlag=true";
	asyncbox.open({
		id:'dealdiv',url:urls,
	 	title:'审核',width:650,height : 370,
        btnsbar : jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
				if(opener.beforSubmit(opener.form)){
					opener.form.submit();
				}
				return false;
			}
　　  　 }
　 });
}

function dealCheck(){
	jQuery.close('dealdiv');
	window.location.href = '/CRMClientAction.do?operation=5&type=contact&tableName=CRMClientInfo&contactTableName=CRMClientInfoDet&moduleId=$!result.get("ModuleId")&viewId=$!viewId&keyId=$!keyId';
}

//添加邻居表表单









/*
	处理旧路径表单








	url:连接
	title:标题
	checkERPClient:是否检测ERP客户，true表示需要先检查








*/
function dealOldBroBill(url,title,checkERPClient){
	var erpCompanyCode = jQuery("#erpCompanyCode").val();
	if("" == erpCompanyCode){
		asyncbox.tips('ERP不存在此客户，请先进行CRM转ERP客户操作');
	}else{
		var clientName = jQuery("#ClientName").text();
		if(clientName == ""){
			clientName = jQuery("#ClientName").val()
		}
		var urls = url+"&f_brother=$!result.get('id')&paramValue=true&CompanyCode=$!erpCompanyCode&tblCompany.ComFullName="+encodeURI(clientName);//拼接上f_brother:客户ID
		
		mdiwin(urls,title)
	}
}


/*新增、修改操作成功后回调方法*/
function dealAsyn(){
	if(jQuery.exist('crmOpDiv')){
		pageRefresh();//客户列表还原客户成功操作
	}else if(jQuery.exist('brotherOperation')){
		jQuery.close('brotherOperation');//关闭详情页面按钮		
		ajaxLoadBrotherList(tempTableName,'1');
	}else if(jQuery.exist('alertDiv')){
		//如果是提醒。关闭弹出框不刷新页面










		jQuery.close('alertDiv');		
	}else if(jQuery.exist('taskAssignId')){
		//点击分派任务详情，点击完成操作成功后
		jQuery.close('taskAssignId');	
		jQuery("#"+hideTaskAssginId).remove();
	}else if(jQuery.exist('updateClientDetId')){
		//联系人修改成功后
		window.location.replace();
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

/*审核成功后返回按钮*/
function dealAsyncbox(){
	//window.location.href="/CRMBrotherAction.do?tableName="+jQuery("#tableName").val();
	window.location.reload();
}

function reverse(){
	window.location.href="/UserFunctionQueryAction.do?tableName=$!tableName&designId=$!designId&keyId=$result.get("id")&f_brother=$result.get("id")&operation=$globals.getOP("Op_RETAUDITING")&winCurIndex=$!winCurIndex&pageNo=$!pageNo&parentTableName=CRMClientInfo&saveDraft=$!saveDraft&crmReCheck=true&isCRMClient=true";
}

/*撤回*/
function cancelFlow(){
	//var url ='/OAMyWorkFlow.do?nextStep=cancel&lastNodeId=$lastNodeId&keyId=$result.get("id")&currNode=$result.get("workFlowNode")&designId=$designId&operation=$globals.getOP("Op_AUDITING")&winCurIndex=$!winCurIndex&crmCancel=true&moduleId=$moduleId&viewId=$viewId'
	//window.location.href=url;
	if(confirm('$text.get("oa.workFlow.Cancel")')){
		$("#nextStep").val("cancel");
		$("#operation").val('$globals.getOP("Op_AUDITING")');
		form.submit();
	}
}
//打印
function nowPrint(){
	var sHeight = $("#contentDiv").height();
	$("#contentDiv").height("100%");
	window.print();
	$("#contentDiv").height(sHeight);
}


function dealCheck(keyId){
	jQuery.close('dealdiv');
	approve(keyId,'deliver','$tableName')
}


//空函数,仅用于发送邮件后.判断时有此方法










function crmDetailHandleMail(){}



/*重写日程详情页面点击完成*/
function finishEvent(id){	
	jQuery.ajax({
		type: "POST",
		url: "/OACalendarAction.do",
		data: "operation=2&finishStatus=1&updateType=status&id="+id,
		success: function(msg){
			if(msg == "3"){		
				asyncbox.tips('操作成功','success');
				jQuery("#"+id).remove();
				hideForm();
			}else{
				asyncbox.tips('操作失败','error');
			}					
		}
	});	
}

/*重载上传附件成功方法,添加成功后异步提交后台*/
function onCompleteUpload(retstr,btnId){
	retstr = decodeURIComponent(retstr);  
	var buttonId = "affixuploadul";
	var hiddenName = "uploadaffix";
   	if(btnId!="undefined" && btnId!=null){
   		buttonId = "affixuploadul_"+btnId;
   		hiddenName = btnId;
   	}
   	
   	if(retstr ==""){
  	 	attachUpload.style.display="none";
   	}else{
		var mstrs = retstr.split(";");
		jQuery.ajax({
			type: "POST",
			url: "/CRMClientAction.do",
			data: 'operation=54&clientId=$!result.get("id")&uploadStrs='+retstr,
			success: function(msg){
				if(msg == "success"){
				    for(i=0;i<mstrs.length;i++){
				       jQuery("#li_uploadfile").remove();
					   str = mstrs[i];
					   if(str == "") continue;
					   var ul=jQuery('#'+buttonId);
					   var imgstr = "<li class='file_li' id='uploadfile_"+str+"'><input type=hidden name="+hiddenName+" value='"+str+"'><div class='showAffix' title='"+str+"'>"+str+"</div>";
				       imgstr += "&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:deleteupload(\""+str+"\",\"false\",\"CRMClientInfo\",\"AFFIX\");'>删除</a>&nbsp;&nbsp;&nbsp;";
					   imgstr += "<a href=\"/ReadFile?fileName="+encodeURI(str)+"&realName="+encodeURI(str)+"&tempFile=false&type=AFFIX&tableName=CRMClientInfo\" target=_blank>下载</a></li>";
					   ul.append(imgstr);
				    }
				    asyncbox.tips('上传成功','success');				    
				    var attachUpload = document.getElementById("attachUpload");				   
					attachUpload.style.display="none";
					//$("#noAffix").hide();
					
				}else{
					asyncbox.tips('上传失败!','error');
				}
			}
		});
   	}
	
}
function deleteupload(fileName,tempFile,tableName,type){
	//如果是临时文件，则需删除远程文件，正式文件，不能删除
	if(!confirm('$text.get("common.msg.confirmDel")')){
		return;
	}
	
	var li = jQuery("li[id='uploadfile_"+fileName+"']");
	jQuery.ajax({
		type: "POST",
		url: '/CRMClientAction.do',
		data: 'operation=3&clientId=$!result.get("id")&type=affix&tempFile='+tempFile+"&fileName="+encodeURIComponent(fileName),
		cache: false,
		success: function(msg){
		  	if(msg=="success"){
		  		asyncbox.tips('删除成功','success');
		  		jQuery("li[id='uploadfile_"+fileName+"']").remove();
		  		if($("#affixuploadul_uploadBtn li").length == 0){
		  			$("#noAffix").show();
		  		}
		  	}else if(msg=="failDelAffix"){
		  		asyncbox.tips('删除附件文件失败!','error');
		  	}else{
		  		asyncbox.tips('删除失败!','error');
		  	}
		}
	});
}


function showTxt(obj){
	var left = $(obj).offset().left;
	var top = $(obj).offset().top;
	var txt ='<div class="show-txt">'+$(obj).text()+'</div>';
	$("#contentDiv").append(txt);
	$(".show-txt").css({"left":left-5,"top":top,"width":$(obj).width()}).mouseout(function(){$(this).remove();});
}

</script>
</head>
<form name="form" action="/CRMClientAction.do" method="post">
	<input type="hidden" id="operation" name="operation"
		value="$globals.getOP('OP_QUERY')"> <input type="hidden"
		id="type" name="type" value=""> <input type="hidden"
		id="strType" name="strType" value="detail"> <input
		type="hidden" id="newCreateBy" name="newCreateBy" value=""> <input
		type="hidden" id="handContent" name="handContent" value=""> <input
		type="hidden" id="openHeight" name="openHeight" value=""> <input
		type="hidden" id="moduleId" name="moduleId" value="$!moduleId">
	<input type="hidden" id="handSmsType" name="handSmsType" value="">
	<input type="hidden" id="keyId" name="keyId" value='$!result.get("id")'>
	<input type="hidden" id="handContent" name="handContent" value=''>
	<input type="hidden" id="isDetail" name="isDetail" value='true'>
	<input type="hidden" id="tableName" name="tableName"
		value='$!tableName'> <input type="hidden" id="moduleId"
		name="moduleId" value='$!moduleId'> <input type="hidden"
		id="viewId" name="viewId" value='$!viewId'> <input
		type="hidden" id="nextStep" name="nextStep" value=''> <input
		type="hidden" id="erpCompanyCode" name="erpCompanyCode"
		value='$!erpCompanyCode'>




	<body class="body" onload="scrollDiv();">

		#set($showAttachment = "false") #set($updateOp = "false")
		#if($loginBean.operationMap.get("/CRMClientAction.do").update())
		#set($updateOp = "true") #end #set($isEdit = "false")
		<!-- 		
<table border="1">
<tr>
	<td>内容</td>
	<td>完成时间</td>
	<td>操作</td>
</tr>
<tr>
	<a onclick="addCommentInfo();">aaaaaa</a>
</tr>
#set($taskAssignList = $!commonInfoMap.get("CRMTaskAssign"))
#foreach($task in $taskAssignList)
<tr id="$!globals.get($task,0)">
	<td>$!globals.get($task,1)</td>
	<td>$!globals.get($task,2)</td>
	<td onclick="finishStatus('$!globals.get($task,0)')">完成</td>
</tr>
#end


</table>		
-->


		<div class="bd" style="overflow:auto;" id="contentDiv">
			<input type="hidden" name="tableName" id="tableName"
				value="$tableName"> <input type="hidden" name="keyInfo"
				id="keyInfo" value='ClientId:$!result.get("id")'> <input
				type="hidden" id="openSelectName" name="openSelectName" value="" />

			<div class="inWrap">
				<div class="inputbox">
					<div class="head_btns">
						<!-- 
		#if("$!attention" == "OK")
		<div class="btn btn-small rf" id="CRM_$result.get("id")" bg="del" onclick="addCollection('$result.get("id")');">取消收藏</div>
		#else
		<div class="btn btn-small rf" id="CRM_$result.get("id")" bg="add" onclick="addCollection('$result.get("id")');">加入收藏</div>
		#end
		-->
						<!-- <div class="btn btn-small rf" onclick="addTaskAssign();">任务分派</div> -->
						#if("$!moduleBean.workflow" == "1" && "$!designBean"!="")
						#if($!result.get("checkPersons").indexOf(";$!loginBean.id;")!=-1)
						<div class="btn btn-small rf"
							onclick="checkDialog('/OAMyWorkFlow.do?keyId=$!keyId&tableName=CRMClientInfo&operation=82')">审核</div>
						#end
						<div class="btn btn-small rf" onclick="flowDepict()">流程图</div>
						#end
						#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=tblCompany&moduleType=2").query())
						#if("$!erpCompanyCode" == "")
						<div class="btn btn-small rf" id="transferToErpBtn"
							onclick="transferToErp()">转入ERP</div>
						#end #end

						#if($loginBean.operationMap.get("/CRMClientTransferAction.do").query()
						&& "$loginBean.id" == "$!result.get('createBy')")
						<div class="btn btn-small rf" onclick="handOver();">移交</div>
						#end #if("$updateOp" == "true") #if($loginBean.id=="1" ||
						$loginBean.id=="$result.get('createBy')")
						<div class="btn btn-small rf" onclick="shareClient('$result.get("id")','$!result.get('ModuleId')','$!viewId','detail','dealdiv')">共享</div>
						#end #if(("$!OAWorkFlow" == "") || ("$!OAWorkFlow" == "true" &&
						$result.get("workFlowNode")=="0") ||
						($result.get("checkPersons").indexOf(";${loginBean.id};")>-1 &&
						$result.get("workFlowNode")!="-1")) #set($isEdit = "true")

						<div class="btn btn-small rf" onclick="addContact()">新增联系人</div>
						<div class="btn btn-small rf" onclick="updateClient('$result.get("id")','$!viewId','1','false')">修改</div>
						#end #end #if("$!OAWorkFlow" ==
						"true"&&$result.get("workFlowNode")!="-1"&&$result.get("checkPersons").indexOf(";${loginBean.id};")>-1)
						#set($keyId = $result.get("id"))
						<div class="btn btn-small rf"
							onclick="approve('$keyId','deliver','$tableName')">审核</div>
						#end #if("$!retCheckRight" == "true" &&
						$result.get("workFlowNode")=="-1")
						<div class="btn btn-small rf" onclick="reverse()">反审核</div>
						#end
						<!--  -->
						#if("$!allowCancel" == "true")
						<div class="btn btn-small rf" onclick="cancelFlow()">撤回</div>
						#end

						<!-- 
		#if("$!result.get('workFlowNode')" == "-1" && "$!result.get('fhzt')"!="2" && $loginBean.operationMap.get("/DeliverManageAction.do").query())
			<div class="btn btn-small rf" onclick="deliverGoods(this);">发货</div>
		#end
		 -->

					</div>
					<!-- 主表展示开始 -->
					#foreach ($maps in $mainMap.keySet())
					<div class="f_dv">
						#if($maps == 1)
						<div class="title_dv">
							<em class="txt" id="${tableName}_$maps">#if("$!globals.getEnumerationItemsDisplay($tableName,$maps)"
								== "") 默认分组 #else
								$globals.getEnumerationItemsDisplay("$tableName","$maps") #end</em>
						</div>
						#end
						<ul class="bk_ul" id="bkul$maps">
							#foreach ($row in $mainMap.get($maps)) #set($fieldBean =
							$globals.getFieldBean($tableName,$row)) #set($readStr = "true")
							#if($!readMainFieldsStr.indexOf(",${row},")==-1 && "$isEdit" ==
							"true") #set($readStr = "") #end #set($resultVal = "")
							#set($resultVal = $!result.get($fieldBean.fieldName))
							#if("$fieldBean.inputType" == "0") #if("$fieldBean.fieldType" ==
							"1")
							<li class="bk_li">
								<div class="swa_c1"
									title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
								<div class="ip_txt" name="$fieldBean.fieldName"
									id="$fieldBean.fieldName" readonly="readonly"
									title="$!globals.dealDoubleDigits("$!result.get($fieldBean.fieldName)","amount")">$!globals.dealDoubleDigits("$!resultVal","amount")</div>
								#if($!readStr == "")<i class="update_i" title='修改'
								fieldName='$fieldBean.fieldName'
								inputType='$fieldBean.inputType'
								fieldType='$fieldBean.fieldType'
								fieldValue='$!globals.dealDoubleDigits("$!resultVal","amount")'></i>
								#end
							</li> #elseif("$fieldBean.fieldType" == "5")
							<li class="bk_li">
								<div class="swa_c1 "
									title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
								<div class="ip_txt" name="$fieldBean.fieldName"
									id="$fieldBean.fieldName" readonly="readonly"
									title="$!resultVal">$!resultVal</div> #if($!readStr == "")<i
								class="update_i" title='修改' fieldName='$fieldBean.fieldName'
								inputType='$fieldBean.inputType'
								fieldType='$fieldBean.fieldType' fieldValue='$!resultVal'></i>
								#end
							</li> #elseif("$fieldBean.fieldType" == "6")
							<li class="bk_li ">
								<div class="swa_c1 "
									title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
								<div class="ip_txt" name="$fieldBean.fieldName"
									id="$fieldBean.fieldName" readonly="readonly"
									title="$!resultVal">$!resultVal</div> #if("$readStr" == "")<i
								class="update_i" title='修改' fieldName='$fieldBean.fieldName'
								inputType='$fieldBean.inputType'
								fieldType='$fieldBean.fieldType' fieldValue='$!resultVal'></i>
								#end
							</li> #elseif("$fieldBean.fieldType" == "3" || ("$fieldBean.fieldType"
							== "16"))
							<li class="bk_li ess">
								<div class="swa_c1 "
									title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
								<textarea class="ip_txt" name="$fieldBean.fieldName"
									id="$fieldBean.fieldName" readonly="readonly"
									title="$globals.replaceHTML($!resultVal)">$globals.replaceHTML($!resultVal)</textarea>
								#if($readStr == "")<i class="update_i" title='修改'
								fieldName='$fieldBean.fieldName'
								inputType='$fieldBean.inputType'
								fieldType='$fieldBean.fieldType' fieldValue='$!resultVal'></i>
								#end
							</li> #elseif("$fieldBean.fieldType" == "13"||"$fieldBean.fieldType"
							== "14") #set($showAttachment = "true")
							#elseif("$fieldBean.fieldType" == "18")
							<li class="bk_li ess">
								<div class="swa_c1 "
									title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
								<div class="ip_txt" name="$fieldBean.fieldName"
									id="$fieldBean.fieldName" readonly="readonly"
									title="$!resultVal">$!resultVal</div> #if($readStr == "")<i
								class="update_i" title='修改' fieldName='$fieldBean.fieldName'
								inputType='$fieldBean.inputType'
								fieldType='$fieldBean.fieldType' fieldValue='$!resultVal'></i>
								#end
							</li> #else #if("$fieldBean.fieldName" == "Trade")
							<li class="bk_li ">
								<div class="swa_c1 "
									title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
								<div class="ip_txt" name="$fieldBean.fieldName"
									id="$fieldBean.fieldName" readonly="readonly"
									title="$!workProfessionMap.get($!resultVal)">$!workProfessionMap.get($!resultVal)</div>
								#if($readStr == "")<i class="update_i" title='修改'
								fieldName='$fieldBean.fieldName'
								inputType='$fieldBean.inputType'
								fieldType='$fieldBean.fieldType'
								fieldValue='$!resultVal:$!workProfessionMap.get($!resultVal)'></i>
								#end
							</li> #elseif("$fieldBean.fieldName" == "District")
							<li class="bk_li ">
								<div class="swa_c1 "
									title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
								<div class="ip_txt" name="$fieldBean.fieldName"
									id="$fieldBean.fieldName" readonly="readonly"
									title="$!districtMap.get($!resultVal)">$!districtMap.get($!resultVal)</div>
								#if($readStr == "")<i class="update_i" title='修改'
								fieldName='$fieldBean.fieldName'
								inputType='$fieldBean.inputType'
								fieldType='$fieldBean.fieldType'
								fieldValue='$!resultVal:$!districtMap.get($!resultVal)'></i>
								#end
							</li> #else
							<li class="bk_li ">
								<div class="swa_c1 "
									title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
								<div class="ip_txt" name="$fieldBean.fieldName"
									id="$fieldBean.fieldName" readonly="readonly"
									title="$!resultVal">$!resultVal</div> #if($readStr == "")<i
								class="update_i" title='修改' fieldName='$fieldBean.fieldName'
								inputType='$fieldBean.inputType'
								fieldType='$fieldBean.fieldType' fieldValue='$!resultVal'></i>
								#end
							</li> #end #end #elseif("$fieldBean.inputType" == "1")
							<li class="bk_li">
								<div class="swa_c1"
									title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
								<div class="ip_txt" name="$fieldBean.fieldName"
									id="$fieldBean.fieldName" readonly="readonly"
									title='$globals.getEnumerationItemsDisplay("$fieldBean.refEnumerationName","$!resultVal")'>$globals.getEnumerationItemsDisplay("$fieldBean.refEnumerationName","$!resultVal")</div>
								#if($readStr == "") <i class="update_i" title='修改'
								fieldName='$fieldBean.fieldName'
								inputType='$fieldBean.inputType'
								fieldType='$fieldBean.fieldType'></i> <select class="slt_in"
								id="${fieldBean.fieldName}_select" style="display: none;">
									<option value="">--请选择--</option> #foreach($item in
									$globals.getEnumerationItems("$fieldBean.refEnumerationName"))
									<option value="$item.value" #if("$!resultVal" ==
										"$item.value") selected="selected" #end>$item.name</option>
									#end
							</select> #end
							</li> #elseif("$fieldBean.inputType" == "2")
							<li class="bk_li">
								<div class="swa_c1"
									title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
								#if("$fieldBean.fieldName" == "ClientId")
								<div class="ip_txt" name="$fieldBean.fieldName"
									id="$fieldBean.fieldName" readonly="readonly"
									title='$!result.get('CRMClientInfo.ClientName')'>$!result.get('CRMClientInfo.ClientName')</div>
								#else #set($disNames = "")
								#set($selectName="popup_"+$fieldBean.fieldName) #set($disNames =
								$!defineDisMap.get("$selectName"))
								<div class="ip_txt" name="$fieldBean.fieldName"
									id="$fieldBean.fieldName" readonly="readonly" title="$disNames">$disNames</div>
								#end
							</li> #elseif("$fieldBean.inputType" == "5")
							<li class="bk_li">
								<div class="swa_c1"
									title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
								#set($valStr ="") #foreach($str in $!resultVal.split(","))
								#set($valStr = $valStr
								+$globals.getEnumerationItemsDisplay("$fieldBean.refEnumerationName","$str")+",")
								#end #if($valStr != "") #set($valStr
								=$globals.subEndwith($valStr,$valStr.length())) #end
								<div class="ip_txt" name="$fieldBean.fieldName"
									id="$fieldBean.fieldName" readonly="readonly" title='$valStr'>$valStr</div>
							</li> #elseif("$fieldBean.inputType" == "10")
							<li class="bk_li">
								<div class="swa_c1"
									title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
								<div class="ip_txt" name="$fieldBean.fieldName"
									id="$fieldBean.fieldName" readonly="readonly"
									title='$globals.getEnumerationItemsDisplay("$fieldBean.refEnumerationName","$!resultVal")'>$globals.getEnumerationItemsDisplay("$fieldBean.refEnumerationName","$!resultVal")</div>

							</li> #elseif("$fieldBean.inputType" == "14")
							<li class="bk_li">
								<div class="swa_c1"
									title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
								<div class="ip_txt" name="$fieldBean.fieldName"
									id="$fieldBean.fieldName" readonly="readonly"
									title="$!deptMap.get($!resultVal)">$!deptMap.get($!resultVal)</div>
								#if($readStr == "")<i class="update_i" title='修改'
								fieldName='$fieldBean.fieldName'
								inputType='$fieldBean.inputType'
								fieldType='$fieldBean.fieldType'
								fieldValue='$!resultVal:$!deptMap.get($!resultVal)'></i> #end
							</li> #elseif("$fieldBean.inputType" == "15")
							<li class="bk_li">
								<div class="swa_c1"
									title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
								<div class="ip_txt" name="$fieldBean.fieldName"
									id="$fieldBean.fieldName" readonly="readonly"
									title="$!globals.getEmpFullNameByUserId($!resultVal)">$!globals.getEmpFullNameByUserId($!resultVal)</div>
								#if($readStr == "")<i class="update_i" title='修改'
								fieldName='$fieldBean.fieldName'
								inputType='$fieldBean.inputType'
								fieldType='$fieldBean.fieldType'
								fieldValue='$!resultVal:$!globals.getEmpFullNameByUserId($!resultVal)'></i>
								#end
							</li> #elseif("$fieldBean.inputType" == "20") #set($enumValue = "")
							#set($optionList = $selectMap.get($fieldBean.fieldName))
							#if("$!optionList" !="") #foreach($option in $optionList)
							#if("$globals.get($option,0)" ==
							"$!result.get($fieldBean.fieldName)") #set($enumValue =
							$globals.get($option,1)) #end #end #end
							<li class="bk_li">
								<div class="swa_c1"
									title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
								<div class="ip_txt" name="$fieldBean.fieldName"
									id="$fieldBean.fieldName" title="$enumValue">$enumValue</div>
							</li> #else
							<li class="bk_li ess">
								<div class="swa_c1"
									title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
								<div class="ip_txt" name="$fieldBean.fieldName"
									id="$fieldBean.fieldName" readonly="readonly"
									title="$!resultVal">$!resultVal</div> #if($readStr == "")<i
								class="update_i" title='修改' fieldName='$fieldBean.fieldName'
								inputType='$fieldBean.inputType'
								fieldType='$fieldBean.fieldType' fieldValue='$!resultVal'></i>
								#end
							</li> #end #end
						</ul>
					</div>
					#end
					<!-- 
        #if("$!result.get('workFlowNode')" == "-1" && $loginBean.operationMap.get("/DeliverManageAction.do").query())
        <div class="f_dv">
				<div class="title_dv">
					<em class="txt" >发货信息</em>
				</div>
				<ul class="bk_ul">
					<li class="bk_li">
						#set($valueStr = $!result.get('fhzt'))
						<div class="swa_c1" title='发货状态'>发货状态：</div>
			            <div class="ip_txt"  readonly="readonly" title='$globals.getEnumerationItemsDisplay("extent_fhzt","$valueStr")'>$globals.getEnumerationItemsDisplay("extent_fhzt","$valueStr")</div>
		            </li>
		            
		            <li class="bk_li">
		        		<div class="swa_c1" title='手机串码'>手机串码：</div>
		        		<div class="ip_txt" readonly="readonly" title="$!result.get('sjcm')">$!result.get('sjcm')</div>
		        	</li>
		        	
		        	<li class="bk_li ess">
		        		<div class="swa_c1 " title='投递备注'>投递备注：</div>
		        		<textarea class="ip_txt"  readonly="readonly" title="$globals.replaceHTML($!result.get('tdbz'))">$globals.replaceHTML($!result.get('tdbz'))</textarea>
		        	</li>
				</ul>
		</div>
		#end
		 -->
					<script type="text/javascript">
$(document).ready(function(){
	$("#detdv1").css({position:"absolute",right:"10000px"});
	$("#detdv2").css({position:"absolute",right:"10000px"});
	$("#tblSalesOutStock").css({display:"none"});
	$("#CRMSaleFollowUp").css({display:"none"});
	$("#tblSaleReceive").css({display:"none"});
	$("#CRMSaleReceivePlan").css({display:"none"});
	$("#tblSalseQuote").css({display:"none"});
	$("#tblSalesReturnStock").css({display:"none"});
	$("#tblSalesOrder").css({display:"none"});
	$("#bkul3").css({display:"none"});
	$(".fdv_main").append($("#bkul2"));
	$(".fdv_main").append($("#bkul3"));
	$(".fdv_main").append($("#tblSalesOrder"));
	$(".fdv_main").append($("#tblSalesReturnStock"));
	$(".fdv_main").append($("#tblSalseQuote"));
	$(".fdv_main").append($("#CRMSaleReceivePlan"));
	$(".fdv_main").append($("#tblSaleReceive"));
	$(".fdv_main").append($("#CRMSaleFollowUp"));
	$(".fdv_main").append($("#tblSalesOutStock"));
	});
	
	
	function closePage(obj,type){
		$(".fdv_nav").children().removeClass("onbtn");
		$(obj).addClass("onbtn");
		$(".fdv_main").children().css({display:"none"});
		$("#"+type).css({display:"block"});
	}
</script>
					<style type="text/css">
.fdvdiv {
	width: 100%;
	min-height: 200px; margin-bottom : 10px;
	border-bottom: 1px dashed gray;
	margin-bottom: 40px;
}

.fdv_nav {
	width: 100%;
	height: 30px;
	line-height: 30px;
	border-bottom: 2px solid #288FF3;
	margin-left: 1px;
}

.fdv_nav_btn {
	width: 55px;
	height: 30px;
	text-align: center;
	cursor: pointer;
	border: 1px solid #00BFFF;
	border-bottom:none;
	display: block;
	float: left;
	margin-right: 2px;
}

.fdv_main {
	padding-top: 10px;
}

.onbtn {
	color: white;
	background-color: #288FF3;
}
</style>
					<div class="fdvdiv">
						<div class="title_dv">
							<em class="txt"> 其他信息 </em>
						</div>
						<div class="fdv_nav">
							<span class="fdv_nav_btn onbtn"
								onclick="closePage(this,'bkul2');">跟单信息</span> <span
								class="fdv_nav_btn" onclick="closePage(this,'CRMSaleFollowUp');">跟单记录</span>
							<span class="fdv_nav_btn" onclick="closePage(this,'bkul3');">附加信息</span>
							<span class="fdv_nav_btn"
								onclick="closePage(this,'tblSalesOutStock');">销售记录</span> <span
								class="fdv_nav_btn" onclick="closePage(this,'tblSaleReceive');">收款记录</span>
							<span class="fdv_nav_btn"
								onclick="closePage(this,'CRMSaleReceivePlan');">收款计划</span> <span
								class="fdv_nav_btn" onclick="closePage(this,'tblSalseQuote');">销售报价</span>
							<span class="fdv_nav_btn"
								onclick="closePage(this,'tblSalesReturnStock');">销售退货</span> <span
								class="fdv_nav_btn" onclick="closePage(this,'tblSalesOrder');">销售订单</span>
						</div>
						<div class="fdv_main in_wdv"></div>
					</div>
					<!-- 主表展示结束 -->
					#if("$!moduleBean.swotStatus" == "1")
					<!-- 四块说明  Start -->
					<div class="f_dv">
						<div class="title_dv">
							<em class="txt">SWOT分析 </em>
						</div>
						<div class="four-block">

							<div class="four-block-1 swtoDiv">
								<p id="StrengthsTitle">优势 Strengths</p>
								<div id="StrengthsDiv">
									<div style="height: 65px;width: 175px;margin-top: 10px;"
										id="StrengthsCont" title="$!result.get("Strengths")">$!result.get("Strengths")</div>
									#if($!readStr == "")<i class="det_update"
										style="margin-right:0px;" onclick="editSWOT(this)"
										fieldName="Strengths" fieldValue="$!result.get("
										Strengths")" title="修改" id="StrengthsEditBtn"></i>#end
								</div>
							</div>

							<div class="four-block-2 swtoDiv">
								<p id="WeaknessesTitle">劣势 Weaknesses</p>
								<div id="WeaknessesDiv">
									<div style="height: 65px;width: 175px;margin-top: 10px;"
										id="WeaknessesCont" title="$!result.get("Weaknesses")">$!result.get("Weaknesses")</div>
									#if($!readStr == "")<i class="det_update"
										style="margin-right:0px;" onclick="editSWOT(this)"
										fieldName="Weaknesses" fieldValue="$!result.get("
										Weaknesses")" title="修改" id="WeaknessesEditBtn"></i>#end
								</div>
							</div>
							<div class="four-block-3 swtoDiv">
								<p id="OpportunitiesTitle">机遇 Opportunities</p>
								<div id="OpportunitiesDiv">
									<div style="height: 65px;width: 175px;margin-top: 10px;"
										id="OpportunitiesCont" title="$!result.get("Opportunities")">$!result.get("Opportunities")</div>
									#if($!readStr == "")<i class="det_update"
										style="margin-right:0px;" onclick="editSWOT(this)"
										fieldName="Opportunities" fieldValue="$!result.get("
										Opportunities")" title="修改" id="OpportunitiesEditBtn"></i>#end
								</div>
							</div>
							<div class="four-block-4 swtoDiv">
								<p id="ThreatsTitle">挑战 Threats</p>
								<div id="ThreatsDiv">
									<div style="height: 65px;width: 175px;margin-top: 10px;"
										id="ThreatsCont" title="$!result.get("Threats")">$!result.get("Threats")</div>
									#if($!readStr == "")<i class="det_update"
										style="margin-right:0px;" onclick="editSWOT(this)"
										fieldName="Threats" fieldValue="$!result.get("
										Threats")" title="修改" id="ThreatsEditBtn"></i>#end
								</div>
							</div>
						</div>
					</div>
					<!-- 四块说明  End -->
					#end

					<!-- 自定义明细开始 -->
					<ul>
						#foreach($childTableName in
						$moduleViewBean.childTableInfo.split(",")) #if("$!childTableName"
						!="")
						<div class="f_dv">
							<div class="title_dv">
								<em class="txt">$globals.getTableInfoBean("$childTableName").display.get("$globals.getLocale()")</em>
							</div>
						</div>
						<div style="overflow:auto;">
							<table border="0" cellspacing="0" class="d-table"
								id="${childTableName}_table">
								<thead>
									<tr>
										#foreach($childFieldName in
										$moduleViewBean.childDisplayFields.split(","))
										#if($childFieldName.indexOf("${childTableName}")>-1)
										#set($fieldNameStr =
										$globals.replaceString($childFieldName,"${childTableName}_",""))
										#set($bean =
										$globals.getFieldBean("$childTableName",$fieldNameStr))
										#if("$bean.inputType"=="2") #foreach($srow in
										$bean.getSelectBean().viewFields) #set($selectFieldBean =
										$globals.getFieldBean($srow.fieldName))
										#if($selectFieldBean.inputType!="100")
										#set($dis=$globals.getLocaleDisplay("$selectFieldBean.display"))
										<td>$dis</td> #end #end #else
										#set($dis=$globals.getLocaleDisplay("$bean.display"))
										<td>$dis</td> #end #end #end
									</tr>
								</thead>
								<tbody>
									#if($!result.get("TABLENAME_${childTableName}").size() != 0)
									#foreach ($det in $!result.get("TABLENAME_${childTableName}"))
									<tr>
										#foreach($childFieldName in
										$moduleViewBean.childDisplayFields.split(","))
										#if($childFieldName.indexOf("${childTableName}")>-1)
										#set($fieldNameStr =
										$globals.replaceString($childFieldName,"${childTableName}_",""))
										#set($bean =
										$globals.getFieldBean("$childTableName",$fieldNameStr))
										#set($nameStr = $childTableName+"_"+$bean.fieldName)
										#set($defaultWidth = "90px") #set($fieldVal = "")
										#set($fieldVal = $det.get("$bean.fieldName"))
										#if("$bean.inputType"=="0") #if("$bean.fieldType"=="1")
										<td width="$defaultWidth">$!globals.dealDoubleDigits("$fieldVal","amount")</td>
										#elseif("$bean.fieldType" == "13" || "$bean.fieldType" ==
										"14") #else
										<td width="$defaultWidth">$fieldVal</td> #end
										#elseif("$bean.inputType"=="1")
										<td width="$defaultWidth">
											$globals.getEnumerationItemsDisplay("$bean.refEnumerationName","$fieldVal")
										</td> #elseif("$bean.inputType"=="2")
										<td style="display: none;"><input type="text"
											id="$nameStr" name="$nameStr" value="$fieldVal" /></td>
										#set($returnFields = "") #foreach($srow in
										$bean.getSelectBean().viewFields) #set($selectFieldBean =
										$globals.getFieldBean($srow.fieldName)) #set($defineVal ="")
										#set($defineVal = $!det.get("$srow.fieldName"))
										#if($selectFieldBean.fieldType=="1") #set($defineVal =
										$!globals.dealDoubleDigits("$defineVal","amount")) #end
										#if($selectFieldBean.inputType!="100")
										<td width="$defaultWidth" title="$defineVal">$defineVal
											#set($returnFields = $returnFields + $srow.fieldName+",")</td>
										#end #end
										<input type="hidden" id="${bean.fieldName}ReturnFieldsStr"
											value="$returnFields" /> #elseif("$bean.inputType"=="14")
										#elseif("$bean.inputType"=="15") #end #end #end
									</tr>
									#end #end
								</tbody>

							</table>
						</div>
						#end #end
					</ul>
					<!-- 自定义明细结束 -->

					<!-- 明细 NewStart
      	#foreach ($maps in $contactMap.keySet())	
			<div class="f_dv">
				<div class="title_dv">
					<em class="txt" id="contactInfo">联系人信息</em>
				</div>
				#foreach ($contact in $!result.get("TABLENAME_${contactTableName}"))
					<ul class="bk_ul">
						#foreach ($row in $contactMap.get($maps))
							#set($fieldBean = $globals.getFieldBean($contactTableName,$row))
							#if("$fieldBean.inputType" == "0")
								#if("$fieldBean.fieldType" == "1")
						        	<li class="bk_li">
										<input type="hidden" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)"/>
						        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
						        		<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!globals.dealDoubleDigits("$!contact.get($fieldBean.fieldName)","amount")">$!globals.dealDoubleDigits("$!contact.get($fieldBean.fieldName)","amount")</div>
						        	</li>
								#elseif("$fieldBean.fieldType" == "5")
									<li class="bk_li">
						        		<div class="swa_c1 " title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
						        		<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!contact.get($fieldBean.fieldName)">$!contact.get($fieldBean.fieldName)</div>
						        	</li>	
								#elseif("$fieldBean.fieldType" == "6")
									<li class="bk_li ">
						        		<div class="swa_c1 " title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
						        		<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!contact.get($fieldBean.fieldName)">$!contact.get($fieldBean.fieldName)</div>
						        	</li>
								#elseif("$fieldBean.fieldType" == "3" || ("$fieldBean.fieldType" == "16"))
									<li class="bk_li ess">
						        		<div class="swa_c1 " title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
						        		<textarea class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!contact.get($fieldBean.fieldName)">$!contact.get($fieldBean.fieldName)</textarea>
						        	</li>
								#elseif("$fieldBean.fieldType" == "13"||"$fieldBean.fieldType" == "14")
									#set($showAttachment = "true") 
								#else
									<li class="bk_li ">
						        		<div class="swa_c1 " title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
										<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!contact.get($fieldBean.fieldName)">$!contact.get($fieldBean.fieldName)</div>
						        	</li>
								#end
							#elseif("$fieldBean.inputType" == "1")
								<li class="bk_li">
					          		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
						            <div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title='$globals.getEnumerationItemsDisplay("$fieldBean.refEnumerationName","$!contact.get($fieldBean.fieldName)")'>$globals.getEnumerationItemsDisplay("$fieldBean.refEnumerationName","$!contact.get($fieldBean.fieldName)")</div>
					           	</li>
							#elseif("$fieldBean.inputType" == "2")
						        	<li class="bk_li">
										<input type="hidden" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)"/>
						        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
						        		<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!contact.get($fieldBean.fieldName)">$!contact.get($fieldBean.fieldName)</div>
						        	</li>
							#elseif("$fieldBean.inputType" == "5")
							#elseif("$fieldBean.inputType" == "10")
							#elseif("$fieldBean.inputType" == "14")
								<li class="bk_li">
									<input type="hidden" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)"/>
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!deptMap.get($!contact.get($fieldBean.fieldName))">$!deptMap.get($!contact.get($fieldBean.fieldName))</div>
					        	</li>
							#elseif("$fieldBean.inputType" == "15")
								<li class="bk_li">
									<input type="hidden" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)"/>
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!globals.getEmpFullNameByUserId($!contact.get($fieldBean.fieldName))">$!globals.getEmpFullNameByUserId($!contact.get($fieldBean.fieldName))</div>
					        	</li>
							#else
								<li class="bk_li">
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<div class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" readonly="readonly" title="$!contact.get($fieldBean.fieldName)">$!contact.get($fieldBean.fieldName)</div>
					        	</li>
							#end
						#end
			        </ul>
				#end
	        </div>
        #end
        明细 NewEnd -->
					<!-- 附件开始 -->
					#if("$showAttachment" == "true")

					<div class="title_dv" style="overflow:hidden;">
						<em class="txt lf" id="attachmentInfo">附件管理</em>
						#if($loginBean.operationMap.get("/CRMClientAction.do").update()) <span
							class="btn btn-mini lf" style="margin:2px 0 0 5px;"
							onclick="upload('AFFIX','uploadBtn');">上传</span> #end
					</div>
					<ul id="affixuploadul_uploadBtn">
						#if("$!result.get('Attachment')" != "") #foreach($uprow in
						$result.get("Attachment").split(";")) #if($uprow != "")
						<li class="file_li" id="uploadfile_$uprow"><input type=hidden
							id="uploadBtn" name=uploadBtn value='$uprow' />
						<div class="showAffix" title="$uprow">$uprow</div>&nbsp;&nbsp;&nbsp;
							#if($loginBean.operationMap.get("/CRMClientAction.do").update())
							<a
							href='javascript:deleteupload("$uprow","false","CRMClientInfo","AFFIX");'>$text.get("common.lb.del")</a>
							#end &nbsp;&nbsp;<a
							href="/ReadFile?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=AFFIX&tableName=CRMClientInfo"
							target="_blank">$text.get("common.lb.download")</a></li> #end #end
						#end
					</ul>
					#end
					<!-- 附件结束-->
					<div>
						<!-- 评论开始 -->
						<div class="title_dv" style="overflow:hidden;">
							<em class="txt lf">评论</em>
						</div>
						#set($keyIdStr = $!result.get("id") )
						<iframe
							src="/DiscussAction.do?tableName=CRMClientInfoDiscuss&&parentIframeName=Frame_${keyIdStr}&f_ref=${keyIdStr}"
							width="100%" frameborder="no" scrolling="no"
							id="Frame_${keyIdStr}"></iframe>
						<!-- 评论结束 -->
					</div>
					<div class="icon32_btns">
						#if($brotherTableList.size()>0 &&
						$loginBean.operationMap.get("/CRMSalesFlowAction.do").query())
						<!-- <div class="btn_item"  onclick="flowSetting();">
				<span>
					<b class="icon32 b-1"></b>
					<em>流程设置</em>
					<p class="bg-p"></p>
				</span>
			</div> -->
						#end
						#if($loginBean.operationMap.get("/CRMPrintSetAction.do").query())
						<div class="btn_item" onclick="pinrtSetting();">
							<span> <b class="icon32 b-2"></b> <em>快递单打印</em>
								<p class="bg-p"></p>
							</span>
						</div>
						#end #if("$!scopeMap.get('printFlag')" == "true" ||
						"$!loginBean.id" == "1")
						<div class="btn_item" onclick="nowPrint();">
							<span> <b class="icon32 b-2"></b> <em>打印</em>
								<p class="bg-p"></p>
							</span>
						</div>
						#end #if("$!OAWorkFlow"!="true")
						<div class="btn_item" onclick="history('$result.get("id")');">
							<span> <b class="icon32 b-3"></b> <em>历史日志</em>
								<p class="bg-p"></p>
							</span>
						</div>
						#end
						<div class="btn_item" onclick="clientLog('$result.get("id")');">
							<span> <b class="icon32 b-3"></b> <em>最新动态</em>
								<p class="bg-p"></p>
							</span>
						</div>
					</div>
					<!-- 流程展示开始 -->
					#if($brotherTableList.size()>0 && "$!flowList" !="") #foreach($flow
					in $flowList) #set($filterTables = "") #foreach($tableName in
					$globals.get($flow,2).split(",")) #if("$!moduleMap.get($tableName)"
					!= "" &&
					($loginBean.operationMap.get("/CRMBrotherAction.do?tableName=$tableName").query()
					||
					$loginBean.operationMap.get("/UserFunctionQueryAction.do?parentTableName=CRMClientInfo&tableName=$tableName").query())
					||
					$loginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=$tableName").query())
					#set($filterTables = $filterTables + $tableName +",") #end #end
					#if($filterTables != "")
					<div class="det_dv" id="detdv$globals.get($flow,1)">
						<div class="title_dv">
							<em class="txt" id="$globals.get($flow,0)">$globals.get($flow,0)</em>
						</div>
						<div class="in_wdv">
							#foreach($tableName in $filterTables.split(","))
							<div class="in_dv" id="$tableName"></div>
							#end
						</div>
					</div>
					#end #end #end
					<!-- 流程展示开始 -->
				</div>

				<!-- 右边挂图 Start -->
				<div class="rightBlock">
					<div class="task-sche-contact">
						<ul class="t-tags-ul">
							<li class="t-s-li" show="tags-contact"><a>联系记录</a></li>
							<li show="tags-scha"><a>日程</a></li>
							<li show="tags-task"><a>任务</a></li>
							<li show="tags-log"><a>日志</a></li>
						</ul>
						<div class="t-s-c">
							<div class="tags-contact">
								#if($LoginBean.operationMap.get("/CRMBrotherAction.do?tableName=CRMSaleFollowUp").add())
								<div class="fast-add">
									<textarea class="txta" placeholder="快速添加联系记录"
										id="followUpContent"
										onKeyDown="if(event.keyCode==13) fastSubmit('CRMSaleFollowUp');"></textarea>
									<span class="btn-s" tableName="CRMSaleFollowUp"
										onclick="commentInfoSubmit(this)">添加</span>
								</div>
								#end
								#if($LoginBean.operationMap.get("/CRMBrotherAction.do?tableName=CRMSaleFollowUp").query())
								<ul class="tags-ul-list" id="CRMSaleFollowUpUL">
									#set($followUpList = $!commonInfoMap.get("CRMSaleFollowUp"))
									#foreach($follow in $followUpList)
									<li id="$!globals.get($follow,0)"><em
										title="$!globals.get($follow,1)" class="commonDetail">$!globals.get($follow,1)</em>
										<i>#if("$!globals.get($follow,2)"!="")$!globals.get($follow,2).substring(5)
											#end</i></li> #end
								</ul>
								<p class="p-more">
									<span onclick="commonInfoMore('CRMSaleFollowUp')" title="更多">更多</span>
								</p>
								#end
							</div>
							<div class="tags-scha" style="display:none;">
								#if($LoginBean.operationMap.get("/OACalendarAction.do?operation=4&queryFlag=List&crmEnter=true").query())
								<div class="fast-add">
									<textarea class="txta" placeholder="快速添加日程"
										id="calendarContent"
										onKeyDown="if(event.keyCode==13) fastSubmit('OACalendar');"></textarea>
									<span class="btn-s" tableName="OACalendar"
										onclick="commentInfoSubmit(this)">添加</span> <span
										class="time-sp"> <b class="icons default-time"
										onclick="WdatePicker({el:$dp.$('calendarStartTime')})"
										title="选择日程时间"></b> <span> <input class="time-inp"
											type="text" id="calendarStartTime"
											value='$globals.getHongVal("sys_date")'
											onclick="WdatePicker({lang:'$globals.getLocale()'})"
											readonly="readonly" />
											<div class="clear"></div>
									</span>
									</span>
								</div>
								<ul class="tags-ul-list" id="OACalendarUL">
									#set($calendarList = $!commonInfoMap.get("OACalendar"))
									#foreach($calendar in $calendarList)
									<li class="task-li" id="$!globals.get($calendar,0)"><em
										title="$!globals.get($calendar,1)" class="commonDetail">$!globals.get($calendar,1)</em>

										<i>#if("$!globals.get($calendar,2)"!="")$!globals.get($calendar,2).substring(5)#end</i>
										<b class="b-finish icons"
										onclick="finishCommentStatus('$!globals.get($calendar,0)','OACalendar');"
										title="点击完成" /></b></li> #end
								</ul>
								<p class="p-more">
									<span onclick="commonInfoMore('OACalendar')" title="更多">更多</span>
								</p>
								#end
							</div>

							<div class="tags-log" style="display:none;">
								#if($LoginBean.operationMap.get("/OACalendarAction.do?operation=4&queryFlag=List&crmEnter=true").query())
								<div class="fast-add">
									<textarea class="txta" placeholder="快速添加日志"
										id="OAWorkLogContent"
										onKeyDown="if(event.keyCode==13) fastSubmit('OAWorkLog');"></textarea>
									<span class="btn-s" tableName="OAWorkLog"
										onclick="commentInfoSubmit(this)">添加</span> <span
										class="time-sp"> <b class="icons default-time"
										onclick="WdatePicker({el:$dp.$('calendarStartTime')})"
										title="选择日程时间"></b> <span> <input class="time-inp"
											type="text" id="time" value='$globals.getHongVal("sys_date")'
											onclick="WdatePicker({lang:'$globals.getLocale()'})"
											readonly="readonly" />
											<div class="clear"></div>
									</span>
									</span>
								</div>
								<ul class="tags-ul-list" id="OAWorkLogUL">
									#set($OAWorkLogList = $!commonInfoMap.get("OAWorkLogUp"))
									#foreach($OAWorkLog in $OAWorkLogList)
									<li class="task-li" id="$!globals.get($OAWorkLog,0)"><em
										title="$!globals.get($OAWorkLog,2)" class="commonDetail">$!globals.get($OAWorkLog,2)</em>

										<i>#if("$!globals.get($OAWorkLog,1)"!="")$!globals.get($OAWorkLog,1).substring(5)#end</i>
										<b
										onclick="finishCommentStatus('$!globals.get($OAWorkLog,0)','OACalendar');"
										title="点击完成" /></b></li> #end
								</ul>
								<p class="p-more">
									<span onclick="commonInfoMore('OAWorkLog')" title="更多">更多</span>
								</p>
								#end
							</div>

							<div class="tags-task" style="display:none;">
								#if($LoginBean.operationMap.get("/OATaskAction.do?crmTaskEnter=true").query())
								<div class="fast-add">
									<textarea class="txta" placeholder="快速添加任务" id="taskContent"
										onKeyDown="if(event.keyCode==13) fastSubmit('OATask');"></textarea>
									<span class="btn-s" tableName="OATask"
										onclick="commentInfoSubmit(this)">添加</span> <span
										class="person-sp"> <b class="icons" title="选择执行人"
										onclick="deptPop('userGroup','taskUserId')"></b> <input
										type="hidden" id="taskUserId"> <input class="em-name"
										id="taskUserIdName" value="执行人"
										onclick="deptPop('userGroup','taskUserId')"
										readonly="readonly" />
									</span> <span class="time-sp"> <b class="icons default-time"
										onclick="WdatePicker({el:$dp.$('taskFinishTime')})"
										title="选择结束时间"></b> <span> <input class="time-inp"
											type="text" id="taskFinishTime"
											value='$globals.getHongVal("sys_date")'
											onclick="WdatePicker({lang:'$globals.getLocale()'})"
											readonly="readonly" />
											<div class="clear"></div>
									</span>
									</span>
								</div>
								#end
								#if($LoginBean.operationMap.get("/OATaskAction.do?crmTaskEnter=true").query())
								<ul class="tags-ul-list" id="OATaskUL">
									#set($taskList = $!commonInfoMap.get("OATask")) #foreach($task
									in $taskList) #set($taskStatus = "2")
									#if("$!globals.get($task,3)"!="" &&
									"$!globals.get($task,3)"!="$LoginBean.id") #set($taskStatus =
									"3") #end

									<li class="task-li" id="$!globals.get($task,0)"
										taskStatus="$!taskStatus"><em
										title="$!globals.get($task,1)" class="commonDetail">$!globals.get($task,1)</em>
										<i>#if("$!globals.get($task,2)"!="")$!globals.get($task,2).substring(5)#end</i>
										<b class="b-finish icons"
										onclick="finishCommentStatus('$!globals.get($task,0)','OATask');"
										title="点击完成" /></b></li> #end
								</ul>
								<p class="p-more">
									<span onclick="commonInfoMore('OATask')" title="更多">更多</span>
								</p>
								#end
							</div>
						</div>
					</div>
					#foreach ($contact in
					$!result.get("TABLENAME_${contactTableName}"))
					#if("$!contact.get('UserName')" != "")
					<div class="contactBlock">
						<span><b></b>$!contact.get("UserName") #if($!readStr == "")<i
							class="det_update" onclick="updateClientDet('$!contact.get("
							id")')" title="修改"></i>#end</span>
						<ul>
							<li><b class="bPhone"></b><i
								#if("$!contact.get('Mobile')" !=""
								) onclick="sendMsg('detailsms','$contact.get("
								id")')" style="cursor: pointer;" #end>$!contact.get("Mobile")</i></li>
							<li><b class="bEmail"></b><i
								#if("$!contact.get('ClientEmail')" !=""
								) onclick="sendMsg('detailemail','$contact.get("
								id")')" style="cursor: pointer;" #end>$!contact.get("ClientEmail")</i></li>
							<li><b class="bQQ"></b><i>$!contact.get("QQ")</i></li>
						</ul>
					</div>
					#end #end

				</div>
				<!-- 右边挂图 End -->
			</div>

			<!-- 定位导航 Start -->
			<div style="position:fixed;top:0px;width:200px;left: 60px;"
				id="quickPositionDiv">
				<!-- <div class="fix_dv">
		<em id="quickPosition">快速定位</em>
		<ul style="display:none;">
			#foreach ($maps in $mainMap.keySet())
				<li>
					<a href="#" titleName="${tableName}_$!maps">#if("$!globals.getEnumerationItemsDisplay($tableName,$maps)" == "") 默认分组  #else $globals.getEnumerationItemsDisplay("$tableName","$maps") #end</a>
				</li>
			#end
			
			<li>
				<a href="#" titleName="contactInfo">联系人信息</a>
			</li>
			
			#if("$showAttachment" == "true" && "$!result.get('Attachment')" != "")
			<li>
				<a href="#" id="attachmentInfo">附件管理</a>
			</li>
	        #end	
	        #if($brotherTableList.size()>0)
		        #if("$!flowList" !="")
		        	#foreach($flow in $flowList)
		        		<li>
							<a href="#" titleName="$globals.get($flow,0)">$globals.get($flow,0)</a>
						</li>
		        	#end
		        #else
		        	<li>
						<a href="#" titleName="defaultSaleFlow">默认流程</a>
					</li>
		        #end
	        #end
		</ul>
	</div>
</div> -->
				<!-- 定位导航 End -->
				<div class="addWrap pop-layer" id="addFeedbackDiv"
					style="top: 50px; left: 500px; display:none;"></div>
</form>
<div class="add-calendar" id="addCalendar"
	style="display:none;left:35%;top: 50px;"></div>

<div class="add-calendar" id="deliverGoodsDiv"
	style="top: 86px; left: 604px;width:260px;display: none;">
	<p class="inp-txt" style="width:260px;height:20px;">发货信息</p>
	<div class="point-block" align="center">
		<label class="lf">手机串码</label> <input class="lf point-i" type="text"
			id="mobileCode">
	</div>
	<div class="point-block" align="center">
		<label class="lf">投递备注</label>
		<textarea class="lf point-area" type="text" id="deliverRemark"></textarea>
	</div>
	<div class="point-block">
		<input class="lf btn-add" type="button" id="dns_btn" value="确定"
			onclick="deliverGoodsSubmit('$!result.get("id")');"> <input
			class="lf btn-cel" type="button" value="取消"
			onclick="deliverGoodsCancel(this);">
	</div>
	<b class="deliver-point"></b>
</div>

</body>
</html>
