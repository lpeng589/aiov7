$(function(){

	$("i.service").click(function(){
		$("i.clientD").removeClass("bg12");
		$(this).removeClass("bg11");
		$(".ServerAndComplain").show();
		$(".ClientInfor").hide();
	});
	
	$("i.clientD").click(function(){
			$("i.service").addClass("bg11");
			$(this).addClass("bg12");
			$(".ServerAndComplain").hide();
			$(".ClientInfor").show();
	});
	
	/*服务、投诉表单详情*/
	$('.relateInfo').live('click', function() {
		var id = $(this).attr("id");
		var tableName = $(this).parents("ul").attr("tableName");
		var titleName = $(this).text();
		var url = '/CRMBrotherAction.do?operation=5&isConsole=true&keyId='+id+"&tableName="+tableName;
		asyncbox.open({id:'DetailDiv',title:titleName,modal:true,url:url,width:780,height:525});
	});
	
	/*联系人详情*/
	$('.contactDetail').live('click', function() {
		var id = $(this).parent().attr("id");
		jQuery.ajax({
			type: "POST",
			url: "/CRMBrotherAction.do",
			data: "operation=4&type=contactInfo&contactId="+id,
			success: function(msg){
				alert(msg);
			}
		});
	});
	
	/*发送邮件、手机*/
	$('.sendMsg').live('click', function() {
		var id = $(this).parent().attr("id");
		var msgType = $(this).attr("msgType");
		sendMsg(msgType,id);
	});
	
	/*服务、投诉表单详情*/
	$('.showClientName').live('click', function() {
		var id = $("#clientId").val();
		var tableName = $(this).text();
		top.mdiwin('/CRMClientAction.do?operation=5&type=detailNew&keyId='+id,tableName);
	});

});

/*关键字查询*/
function keyWordSubmit(){
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: "<div style='width:135px;'><img src='/style/images/load.gif'/><div style='color:#0179bb;font-weight:bold;'></div></div>",css:{ background: 'transparent'}}); 
	}
	var keyWord = $("#keyWord").val();
	jQuery.ajax({
		type: "POST",
		url: "/ServiceConsoleAction.do",
		data: "operation=4&type=keyWord&keyWord="+encodeURIComponent(encodeURIComponent(keyWord)),
		success: function(msg){
			if(msg != "error"){
				$("#clientsDiv").html(msg);
			}else{
				$("#clientsDiv").html("");
			}
			if(typeof(top.junblockUI)!="undefined"){
				top.junblockUI();
			}
		}
	});
	
	$("#clientsDiv").show();//显示客户展示部分
	$("#contentsDiv").hide();//隐藏内容展示部分
	$(".ClientInfor").hide();
}

/*客户详情*/
function clientDetail(clientId,clientName){
	var url = '/CRMBrotherAction.do?operation=6&tableName=CRMClientService&isConsole=true&brotherId='+clientId;//iframe默认跳转到新增服务页面
	$("#billFrame").attr("src",url);
	
	$("#clientsDiv").hide();//隐藏客户展示部分
	$(".ClientInfor").hide();//隐藏客户信息部分
	
	//显示服务投诉内容部分
	$(".ServerAndComplain").show()
	$("#contentsDiv").show();
	$(".showClientName").text(clientName);
	$("#clientId").val(clientId);
	
	getRelateInfo('CRMClientService');//显示服务关联信息
	getRelateInfo('CRMcomplaints');//显示投诉关联信息
	getRelateInfo('CRMSaleFollowUp');//显示销售跟单关联信息
	getRelateInfo('CRMClientInfoDet');//显示联系人信息关联信息
}

/*添加表单*/
function addBill(tableName){
	var url = "";
	var keyId = $("#clientId").val();
	if(tableName == "CRMcomplaints" || tableName == "CRMClientService"){
		url = '/CRMBrotherAction.do?tableName='+tableName+'&operation=6&isConsole=true&brotherId='+keyId;//iframe默认跳转到新增服务页面
	}else if(tableName == "CRMClientInfo"){
		url = '/CRMClientAction.do?operation=6&type=addContact&moduleId=1&viewId=1_1&isConsole=true&clientId='+keyId;
	}		
	$("#billFrame").attr("src",url);
}

/*获取服务与投诉数据
tableName:表名
*/
function getRelateInfo(tableName){
	jQuery.ajax({
		type: "POST",
		dataType:"json",
		url: "/ServiceConsoleAction.do",
		data: "operation=4&type=relate&tableName="+tableName+"&clientId="+$("#clientId").val(),
		success: function(msg){
			var str = "";
			for(var i=0;i<msg.length;i++){
				if(tableName == "CRMClientInfoDet"){
					var userName = msg[i][1];
					var mobile = msg[i][2];
					var email = msg[i][3];
					if(userName!=""){
						str +='<li id="'+msg[i][0]+'"><i style="color:#A0A0A0;">●</i><a href="#" class="contactDetail" >'+userName+'</a>';
						if(mobile !=""){
							str +='<a class="phone sendMsg" href="#" msgType="detailsms">'+mobile+'</a>';
						}
						if(email !=""){
							str +='<a class="email sendMsg" href="#" msgType="detailemail">'+email+'</a>';
						}
						str +='</li>'
					}
				}else{
					str +='<li><b>●</b><a href="#" id="'+msg[i][0]+'" class="relateInfo">'+msg[i][1]+'</a> '+msg[i][2]+' </li>'
				}
			}
			$("#show"+tableName).html(str);
		}
	});
}

/*处理服务、投诉表单操作成功后方法*/
function dealAsyn(){
	if(jQuery.exist('alertDiv')){
		//如果是提醒。关闭弹出框不刷新页面
		jQuery.close('alertDiv');		
	}else{
		jQuery.close('detailUpdateId');//关闭修改页面	
		jQuery.opener('DetailDiv').window.location.reload();//刷新详情页面
	}
}

/*发送邮件、手机*/
function sendMsg(msgType,keyIds,top){
	if(keyIds!="" && keyIds.length!=0){
		if(msgType.indexOf("email")>-1){
			var left = 200;  
			var top = screen.height/2 - 250;	
			var str  = window.open("/EMailAction.do?operation=6&msgType="+msgType+"&type=main&sendPerson="+keyIds+"&noback=true",null,'menubar=no,toolbar=no,resizable=no,scrollbars=no,loaction=no,status=yes,width=900,height=500,left='+left+',top='+top);
		}else{
			var urls = '/CRMClientAction.do?operation=22&type=sendPrepare&msgType='+msgType+'&keyId='+keyIds ;
			asyncbox.open({
				id:'dealdiv',title :'短信发送',url:urls,cache:false,modal:true,width:710,height:355, btnsbar:jQuery.btn.OKCANCEL,
				callback : function(action,opener){
		　　　　　	if(action == 'ok'){
						var returnValue = opener.returnValue();
						if(returnValue=="false"){
							return false ;
						}else{
							$("#type").val("sendMessage") ;
							$("#operation").val("22");
							$("#newCreateBy").val(returnValue.split("@koron@")[0]);
							$("#handContent").val(returnValue.split("@koron@")[1]);
							$("#handSmsType").val(returnValue.split("@koron@")[2]);
							blockUI();
							form.submit() ;
						}
					}
				}
			});
		}
	}else{
		asyncbox.tips(selectOne);	
	} 
}