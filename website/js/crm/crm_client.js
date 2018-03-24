var broherIframeTableName;
$(document).ready(function(){
	//单击按钮触发事件
	$(document).on("click", function(){
		$(".isd_dv").hide();//隐藏事件转换DIV
		$(".pop-weixin").hide();
	});
		
	if(typeof(top.junblockUI)!="undefined"){
		top.junblockUI();
	}
	
	//处理搜索的初始化与单选多选按钮,全部按钮是否选中
	if($("#searchFields").val()!= undefined && $("#searchFields").val()!= ""){
		for(var i=0;i<$("#searchFields").val().split(",").length;i++){
			var mulName = $("#searchFields").val().split(",")[i];
			if($("#" + mulName+"_mul").val() == "true"){
				$("#" + mulName+"_btn").parent().find(":checkbox").show();
			    $("#" + mulName+"_btn").find("span").html("单选");
			    $("#" + mulName+"_btn").find("span").attr("title","单选");
			    $("#" + mulName+"_btn").css("backgroundPositionY","40px");
		    	$("#" + mulName+"_btn").find("span").css("marginLeft","-5px");
			    var str = '<a href="#" class="nomore" onclick="submitQuery();"><span style="float: left;width: 35px;margin-top: -1px;" title="查询">查询</span></a>'
			    $("#" + mulName+"_btn").before(str);
				if($("#" + mulName+"_btn").parent().find("li[class='sel'] :gt(0)").length >0){
			    	$("#" + mulName+"_btn").parent().find("li:first").removeClass("sel");
			    }
			}else{
				if($("#" + mulName+"_btn").parent().find("li[class='sel']").length ==2){
					//处理单选，若有两选项删除"全部"
					$("#" + mulName+"_btn").parent().find("li:first").removeClass("sel");
				}
				
				if($(".col ul li[name='LastContractTime'][class='sel']").length == 2 && mulName =="LastContractTime"){
					//处理最近联系天数，若有两选项删除"全部"
					$(".col ul li[name='LastContractTime']:first").removeClass("sel")
				}
			}
		}	
	}
	
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
	
	//更新客户后滚动
	if($("#updateFlag").val() == "true"){
		if($("#scroll"+$("#updateKeyId").val()).attr("id") != undefined){
			var clientIdHeight = parseInt($("#clientId").css("height").substring(0,$("#clientId").css("height").length-2));
			if($("#scroll"+$("#updateKeyId").val()).position().top > clientIdHeight){
				$("#clientId").animate({scrollTop: $("#scroll"+$("#updateKeyId").val()).position().top-$(".maintablearea").position().top-30}, 0);
			}
		}
		$("#updateFlag").val("");
		$("#updateKeyId").val("");
	}
	
	
	/*当客户列表表头不在可见区域时，固定表头*/
	$("#clientId").scroll(function(){ 
		var maintop = $("#maintop");
		if($("#titleId").position().top<=25){
			maintop.addClass("maintop_fix");
			$(".pagetop").show();
		}else{
			maintop.removeClass("maintop_fix");
			$(".pagetop").hide();
		}
	});
	
	/*返回顶部*/
	$("#html_top").click(function(){
		$("#clientId").animate({scrollTop: 0}, 500);	
	});
	
	/*双击弹开联系人明细*/
	$(".bd .context-menu-one").dblclick(function () { 
		if($(this).attr("class")=="maintop"){return;}
		var keyId = $(this).find("input").attr("value");
		var cName = $(this).find(".col_name").text();
		cName = cName.substring(cName.indexOf(".")+1);   // 截取客户名称		
		var moduleId = $("#ModuleId").val();
		mdiwin('/CRMClientAction.do?operation=5&type=detailNew&keyId='+keyId+'&moduleId='+moduleId+"&viewId="+$("#viewId").val(),cName);
	});
	
	/*单击向下图标弹开联系人明细*/
	$(".bd ul li[class='col_name']").click(function () {
		if($(this).parents("ul").find(".minfo").css("display")=="none"){
			$(this).parents("ul").removeClass("col") ;
			$(this).parents("ul").addClass("selcol") ;
			$(this).parents("ul").find(".minfo").show() ;
			$(this).parents("ul").find(".col_name").css({backgroundPosition: '0px -643px'}) ;
			var keyId = $(this).parent().find(":checkbox").val();
			$("#Frame_"+keyId).attr("src","/DiscussAction.do?tableName=CRMClientInfoDiscuss&f_ref="+keyId+"&parentIframeName=Frame_"+keyId);
		}else{
			$(this).parents("ul").removeClass("selcol") ;
			$(this).parents("ul").addClass("col") ;
			$(this).parents("ul").find(".minfo").hide() ;
			$(this).parents("ul").find(".col_name").css({backgroundPosition: '0px -1031px'});
		}
	});
	
	
	/*单击客户列表表头排序*/
	$(".maintop li span").click(function () {
		var sortInfo = $(this).attr("sort") + "," + $(this).attr("fieldName");
		$("#sortInfo").val(sortInfo);
		submitQuery();
	});
	
	
	/*单击选中当前行*/
	$(".bd ul").click(function (event) {
		if(event.target.tagName=="INPUT"){return;}
		if($(this).attr("class")=="maintop"){return;}
		$(".bd ul").each(function(i){
			$(this).find(":checkbox").removeAttr("checked") ;
		}) ;
		$(this).find(":checkbox").attr("checked","checked") ;
		/*if($(this).attr("class")!="maintop"){
			$(".bd ul").each(function(){
				if(typeof($(this).attr("class"))!="undefined" && $(this).attr("class")!="maintop"){
					$(this).removeClass("selcol") ;
					$(this).addClass("col") ;
					$(this).find(".minfo").hide() ;
					$(this).find(".col_name").css({backgroundPosition: '27px -643px'});
				}
			});
			$(this).removeClass("col") ;
			$(this).addClass("selcol") ;
			$(this).find(".minfo").show() ;
			$(this).find(".col_name").css({backgroundPosition: '27px -1031px'}) ;
		}*/
		if($(".brother_table").css("display")=="block"){
			window.frames["brotherTable"].changeIframe($(this).find(":checkbox").attr("value")) ;
		}
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
	
	/*划出兄弟表窗口*/
	$(".pagerightbtn").click(function(){
		var keyId = $("input[name='keyId']:checked").val() ;
		if(typeof(keyId)=="undefined"){
			asyncbox.tips('请选择一个客户');
			return ;
		}
		var moduleId = $("#ModuleId").val();
		var strUrl = "/CRMClientAction.do?operation=22&type=brotherTable&moduleId="+moduleId+"&keyId="+keyId+"&viewId="+$("#viewId").val();
		var brother = $("#brotherTable") ;
		if(typeof(brother.attr("src"))!="undefined"){
			brother.src = strUrl ;
		}else{
			var html = '<div class="brother_table" id="brotherid" > '
			 	+ '<iframe id="brotherTable" name="brotherTable" allowTransparency="true" src="'+strUrl+'" ' 
			 	+ 'frameborder="no" scrolling="no" onload="this.height=brotherTable.document.documentElement.scrollHeight-10;this.width=brotherTable.document.documentElement.scrollWidth;"></iframe>'
			 	+ '</div>' ;
			$("body").append(html) ;
		}
		$(".brother_table").show("slow");
		if(/iPad/gi.test(navigator.userAgent)){
			$(".brother_table").css({right:'1px'});
		}
	});
	
	/*搜索弹出框*/
	$(".sbtn[id !='multipleSelect']").click(function(){
		var width = 755;
		var height = 435;
		var url;
		var openId = "crmOpenId";
		var searchName = "";
		var title;
		if($(this).attr("id") == "strade"){
			url ="/CRMopenSelectAction.do?isIndexEnter=true";
		　　 var height = 540;
			var winHeight = parseInt($("#clientId").css("height").substring(0,$("#clientId").css("height").length-2)) + $("#clientId").position().top;
			if(winHeight<height){
				height = winHeight;
			}
			searchName = "trade";
			title = '选择行业'
		}else if($(this).attr("id") == "sdistrict"){
			url ="/CRMopenSelectAction.do?operation=4&isMultiple=true";
			searchName = "district";
			title = '选择地区';
			width = 800;
			height = 450;
		}else if($(this).attr("id") == "semployee"){
			url ="/Accredit.do?popname=userGroup&inputType=checkbox&leavePerson=yes&chooseData="+$("#employee").val();
			$("#popSelectName").val("employee")
			openId = "indexPop";
			title = '选择职员'
		}else if($(this).attr("id") == "sdepartMent"){
			url ="/Accredit.do?popname=deptGroup&inputType=checkbox&chooseData="+$("#departMent").val();
			$("#popSelectName").val("departMent")
			openId = "indexPop";
			title = '选择部门'
		}
		if($(this).attr("id") == "strade" ){
			asyncbox.open({id:openId,url:url,title:title,width:width,height:height});
		}else if($(this).attr("id") == "sdistrict"){
			asyncbox.open({id:openId,url:url,title:title,width:width,height:height,
				 btnsbar : jQuery.btn.OKCANCEL,
				 callback : function(action,opener){
				     if(action == 'ok'){
				     	var trades = opener.returnVal();
				     	$("#district").val(trades.split(";")[1]);
						$("#districtName").val(trades.split(";")[0]);
						submitQuery();
					 }else if(action == 'close' ||action == 'cancel'){
						$("#tempDistrictIds").val("");
						$("#tempDistrictNames").val("");
					}
		　　  	 }
			});
		
		}else{
			asyncbox.open({
				id : openId,url : url,title : title,width :width,height : height,
				btnsbar :[{text:'清空',action:'selectAll'}].concat(jQuery.btn.OKCANCEL),
			    callback : function(action,opener){
			    	if(action == 'ok'){
			    		url="/CRMClientAction.do?searchName=";
			    		var searchId ="";
			    		var nameVals ="";
			    		var str = opener.strData;
			    		if(str.split("|").length-1 == 1){
			    			searchId =str.split(";")[0];
			    			nameVals = str.split(";")[1];
			    		}else{
				    		for(var i=0;i<str.split("|").length-1;i++){
				    			var arrVal = str.split("|")[i];
				    			searchId +=arrVal.split(";")[0] + ",";
				    			nameVals += arrVal.split(";")[1] + ",";
				    		}
			    		}
						var selecrName = $("#popSelectName").val();
						$("#" + selecrName).val(","+searchId);
						$("#" + selecrName + "Name").val(nameVals);
						if(typeof(top.jblockUI)!="undefined"){
							top.jblockUI({ message: " <img src='/style/images/load.gif'/>",css:{ background: 'transparent'}}); 
						}
						form.submit();
			    	} 
					if(action == 'selectAll'){
						if($("#popSelectName").val() == "employee"){
							$("#employeeName").val("");
							$("#employee").val("");
						}else{
							$("#departMentName").val("");
							$("#departMent").val("");
						}
						blockUI();
						form.submit();
					}
			    }
		　    });
		}
	}) ;
	
	/*导航单选多选*/
	$(".multipleSelect").click(function (){
		if($("#" + $(this).attr("mulName")).val() == "true"){
			$(this).parent().find(":checkbox").hide();
		    $(this).find("span").html("多选");
		    $(this).find("span").attr("title","多选");
		    $(this).css("backgroundPositionY","4px");
		    $(this).find("span").css("marginLeft","0px");
		    $(this).prev().remove();
		    $("#"+$(this).attr("mulName")).val("");
		    $("#"+$(this).attr("mulName").replace("_mul","")).val("");
		    $(this).parent().find("li").removeClass("sel");
		    $(this).parent().find(":checkbox").removeProp("checked");
		    $(this).parent().find("li:first").addClass("sel");
		    submitQuery();
		}else{
			$(this).parent().find(":checkbox").show()
		    $(this).find("span").html("单选");
		    $(this).find("span").attr("title","单选");
		    $(this).css("backgroundPositionY","40px");
		    $(this).find("span").css("marginLeft","-5px");
		    //var str = '<div class="op sbtn41" style="float: left;margin-top: 0px;width: 40px;" onclick="submitQuery();"><span class="a"  style="width: 5px;height: 24px;margin-right: 0px;"></span><a title="确定">确定</a><span class="c" style="width: 5px;height: 24px;margin-right: 0px;"></span></div>'
		    var str = '<a href="#" class="nomore" onclick="submitQuery();"><span style="float: left;width: 35px;margin-top: -1px;" title="查询">查询</span></a>'
		    $(this).before(str);
		    $("#"+$(this).attr("mulName")).val("true");
		}
	}) ;
	/*默认把第一行展开*/
	//$(".bd ul .expands").first().click();
	/*选中数据*/
	$(".main_search .col ul li").click(function (){
		if($(this).attr("enumValue")=="all"){
			//表示点击全部
			$(".main_search .col ul li[name='"+$(this).attr("name")+"']").removeClass("sel") ;
			$(this).addClass("sel") ;
			$(this).parent().find(":checkbox").removeProp("checked");
			$("#"+$(this).attr("name")).val("") ;
			if($("#" + $(this).attr("name")+"_mul").val() == "true"){
				//如果是多选，点击全部后，变为单选				$("#" + $(this).attr("name")+"_mul").val("");
			}
		}else{
			var fieldValue = "" ;
			//判断是否多选，
			if($("#"+$(this).attr("name")+"_mul").val() == "true"){
				//先去掉全选的class
				$(this).parent().find("li:first").removeClass("sel");
				if($(this).attr("class")=="sel"){
					//以选中取消
					$(this).removeClass("sel") ;
					$(this).prev().removeAttr("checked");
				}else{
					$(this).addClass("sel") ;
					$(this).prev().attr("checked","checked");
				}
				$(".main_search .col ul li[name='"+$(this).attr("name")+"']").each(function(){
					if($(this).attr("class")=="sel" && $(this).attr("enumValue")!="all"){
						fieldValue += $(this).attr("enumValue")+ "," ;
					}
				}) ;
			}else{				
				if($(this).parent().find("li[class='sel']").length>0){
					$("#"+ $(this).attr("name")).val("");
				}
				fieldValue = $(this).attr("enumValue")+ ",";
			}	
			
		}
		
		$("#"+$(this).attr("name")).val(fieldValue);
		$("#pageNo").val("1");
		//statusVal所属的标签有其他的单击事件提交表单提交
		if($("#"+$(this).attr("name")+"_mul").val() != "true" && $(this).attr("statusVal")==undefined){
			blockUI();
			form.submit() ;
		}
	}) ;
	
	//多选checkBox单击事件
	$(".main_search :checkbox").click(function (){
		$(this).parent().find("li:first").removeClass("sel");
		var boxsValue = "";
		if($(this).attr("checked") !=undefined){
			boxsValue = $("#" + $(this).attr("name").replace("_boxs","")).val() + $(this).val() + ",";
			$(this).next().addClass("sel");
		}else{
			boxsValue = $("#" + $(this).attr("name").replace("_boxs","")).val().replace($(this).val() +",","")
			$(this).next().removeClass("sel");
		}
		$("#" + $(this).attr("name").replace("_boxs","")).val(boxsValue);
	})
	
	/*全选*/
	$("#checkAll").click(function(){
		var check = $(this).attr("checked") ;
		$("input[name='keyId']").each(function() {
			if(typeof(check)!="undefined"){
				$(this).attr("checked", "checked");
			}else{
				$(this).removeAttr("checked");
			}
		});
	});
	
	/*删除客户按钮*/
	$("#deleteBtn").click(function(){
		if(!isChecked("keyId")){
			asyncbox.tips(deleteMsg);
			return false;
		}
		if(!confirm(confirmMsg)){
			return false;
		}else{
			form.operation.value=operation;
			blockUI();
			form.submit();
		}
	});
	
	/*打印客户资料*/
	$("#printBtn").click(function(){
		alert("暂不要提bug，正正开发当中") ;
		//window.showModalDialog("/UserFunctionQueryAction.do?tableName=CRMClientInfo&reportNumber=CRMClientInfoList&operation=$globals.getOP("OP_PRINT")&parentTableName=CRMClientInfo&winCurIndex=$!winCurIndex",window,"dialogWidth=258px;dialogHeight=248px,scroll=no;") ;
	});
	
	/*客户资料移交*/
	$("#handBtn").click(function(){
		if(!hasCheck("keyId")){
			asyncbox.tips(selectOne);
			return false;
		}
		
		var error = "false";
		$("input:[name='keyId']:checked").each(function(){
			if($(this).parent().attr("shareOp") == "false"){
				error="【"+$(this).parent().attr("ClientName")+"】";
				return false;
			}
		}) ;
		
		if(error!="false"){
			asyncbox.tips(error+"没有移交权限");
			return false;
		}
		
		
		
		
		var urls = '/vm/crm/client/handOver.jsp' ;
		asyncbox.open({
			id:'handOver',title :'客户资料移交',url:urls,cache:false,modal:true,width:600,height:305, btnsbar:jQuery.btn.OKCANCEL,
			callback : function(action,opener){
	　　　　　	if(action == 'ok'){
					var createBy = opener.document.getElementById("newCreateBy").value ;
					var content = opener.document.getElementById("content").value ;
					if(createBy.length==0){
						alert("接收人不能为空!") ;
						return false;
					}
					$("#type").val("handOver");
					$("#operation").val("22");
					$("#newCreateBy").val(createBy);
					$("#handContent").val(content); 
					blockUI();
					form.submit() ;
	　　　　　	}
	　　　	}
　		});
	});
	
	/*生命周期转换*/
	$("#statusBtn").click(function(){
		if(!hasCheck("keyId")){
			asyncbox.tips(selectOne);
			return false;
		}
		var urls = '/CRMClientAction.do?type=selectStatus&operation=22&moduleId='+jQuery("#ModuleId").val();
		asyncbox.open({
			id:'dealdiv',title :'生命周期转换',url:urls,cache:false,modal:true,width:310,height:295, btnsbar:jQuery.btn.OKCANCEL,
			callback : function(action,opener){
	　　　　　	if(action == 'ok'){
					$("#type").val("status") ;
					$("#operation").val("22");
					$("#strStatus").val(opener.returnValue()) ;
					blockUI();
					form.submit() ;
				}
		　　}
	　	});
	});
	
	/*模板转换*/
	$("#moduleBtn").click(function(){
		if(!hasCheck("keyId")){
			asyncbox.tips(selectOne);
			return false;
		}
		var urls = '/CRMClientAction.do?operation=4&type=moduleTransfer&moduleId='+$("#ModuleId").val();
		asyncbox.open({
			id:'modulediv',title :'客户模板转移',url:urls,cache:false,modal:true,width:295,height:295, btnsbar:jQuery.btn.OKCANCEL,
			callback : function(action,opener){
	　　　　　	if(action == 'ok'){
					$("#type").val("moduleTransfer") ;
					$("#operation").val("22");
					$("#moduleTransferId").val(opener.returnValue()) ;
					form.submit() ;
				}
		　　}
	　	});
	});
	
	
	
	/*客户设置*/
	$("#setBtn").click(function(){
		mdiwin('/ClientSettingAction.do?src=menu','客户设置');
	});
	
	/*客户设置新*/
	$("#setBtnNew").click(function(){
		var urls = "/ClientSettingAction.do?operation=4&type=modules"
		asyncbox.open({
			id:'modulediv',title :'模板设置',url:urls,cache:false,modal:true,width:550,height:350
	　	});
	});
	
	/*客户视图设置*/
	$("#moduleViewEdit").click(function(){
		var urls = "/ClientSettingAction.do?operation=4&queryType=moduleView&moduleId="+$("#ModuleId").val();
		asyncbox.open({
			id:'moduleViewId',title :'视图设置',url:urls,cache:false,modal:true,width:550,height:350
	　	});
	});
	
	/*ERP转CRM弹出框*/
	$("#erpToCrm").click(function(){
		var urlstr = '/UserFunctionAction.do?operation=22&popupWin=ErpToCrm&tableName=CRMClientInfo&selectName=same_CrmCompanysame2&pupupWin=ErpToCrm&MOID='+varMOID+'&MOOP=query&LinkType=@URL:&displayName=客户弹出框';
		asyncbox.open({id:'ErpToCrm',title:'客户弹出框',url:urlstr,width:750,height:470});
	})
	
	/*增加合同*/
	$("#addCompact").click(function(){
		if(!isChecked("keyId")){
			asyncbox.tips(deleteMsg);
			return false;
		}
		var items = document.getElementsByName("keyId");
		var yxqs = document.getElementsByName("yxq");
		var ywys = document.getElementsByName("ywy");
		var item;
		var yxq;
		var ywy;
		for(var i=0;i<items.length;i++){
	    	if(items[i].checked){
	    		item=items[i];
	    		yxq=yxqs[i];
	    		ywy=ywys[i];
	    	}
		}
		var urlstr = '/UserFunctionAction.do?tableName=ContractManagement&operation=6&paramValue=true&CustomerName='+item.value+'&CustBankCard='+yxq.value+'&EmployeeID='+ywy.value;
		mdiwin(urlstr,'合同管理');
	})
	
	/*查看合同*/
	$("#queryCompact").click(function(){
		if(!isChecked("keyId")){
			asyncbox.tips(deleteMsg);
			return false;
		}
		var items = document.getElementsByName("keyId");
		var ClientNames = document.getElementsByName("ClientName");
		var item;
		var ClientName;
		for(var i=0;i<items.length;i++){
	    	if(items[i].checked){
	    		item=items[i];
	    		ClientName=ClientNames[i];
	    	}
		}
		var urlstr = '/UserFunctionQueryAction.do?tableName=ContractManagement&paramValue=true&BillDate2=&src=&LinkType=@URL:&ClientId='+item.value+'&ClientName='+encodeURIComponent(ClientName.value);
		mdiwin(urlstr,'合同管理');
	})
	/*查看银行记录*/
	$("#queryBankNote").click(function(){
		if(!isChecked("keyId")){
			asyncbox.tips(deleteMsg);
			return false;
		}
		var items = document.getElementsByName("keyId");		
		var ClientNames = document.getElementsByName("ClientName");
		var item;
		var ClientName;
		for(var i=0;i<items.length;i++){
	    	if(items[i].checked){
	    		item=items[i];
	    		ClientName=ClientNames[i];
	    	}
		}
		var urlstr = '/UserFunctionQueryAction.do?tableName=BatchUpdateInterestCapital&paramValue=true&TradeTime2=&src=&LinkType=@URL:&ClientId='+item.value+'&ClientName='+encodeURIComponent(ClientName.value);
		mdiwin(urlstr,'银行记录');
	})
	
	/*CRM&ERP客户转换*/
	$("#crmToErp").click(function(e){
		if(!isChecked("keyId")){
			asyncbox.tips(deleteMsg);
			return false;
		}
		var transferType = $(this).attr("transferType");//转换类型,crmToErp or erpToCrm
		var keyIds="";
		$("input:[name='keyId']:checked").each(function(){
			keyIds += $(this).attr("value")+"," ;
		}) ;
		CRMTransfer(keyIds,transferType);
		e = e||event; stopFunc(e);//阻止冒泡
	});
	
	/*批量客户共享*/
	$("#mulShareClient").click(function(){
		if(!isChecked("keyId")){
			asyncbox.tips(deleteMsg);
			return false;
		}
		var keyIds="";
		var error = "false";
		$("input:[name='keyId']:checked").each(function(){
			if($(this).parent().attr("shareOp") == "false"){
				error="【"+$(this).parent().attr("ClientName")+"】";
				return false;
			}
			keyIds += $(this).attr("value")+"," ;
		}) ;
		
		if(error!="false"){
			asyncbox.tips(error+"没有共享权限");
			return false;
		}
		
		var moduleId = $("#ModuleId").val();
		shareClient(keyIds,moduleId);
		
	});
	
	/*客户导入*/
	$("#importBtn").click(function(){
		
		mdiwin('/CRMClientAction.do?operation=91&NoBack=NoBack&moduleId='+$("#ModuleId").val()+"&viewId="+$("#viewId").val(),'客户导入');
	});
	
	/*客户导出*/
	$("#exportBtn").click(function(){
		if(!isChecked("keyId")){
			asyncbox.tips(selectOne);
			return false;
		}
		form.operation.value=92;
		blockUI();
		form.submit() ;
	});
	
	/*客户导出所有*/
	$("#exportAllBtn").click(function(){
		if(!confirm("确定导出所有?")){
			return false;
		}else{
			$("#operation").val("92")
			$("#exportAll").val("true");
			blockUI();
			form.submit();
		}
	});
	
	
	
	/*关注的客户*/
	$("#attentionBtn").click(function(){
		$("#myAttention").val("ok");
		blockUI();
		form.submit();
	});
	
	/*修改默认模板*/
	$("#setDefModuleBtn").click(function(){
		var urls = '/CRMClientAction.do?operation=4&type=moduleTransfer&isSetDefModule=true';
		asyncbox.open({
			id:'modulediv',title :'默认模板修改',url:urls,cache:false,modal:true,width:295,height:295, btnsbar:jQuery.btn.OKCANCEL,
			callback : function(action,opener){
	　　　　　	if(action == 'ok'){
					$("#type").val("setDefModule") ;
					$("#operation").val("22");
					$("#defModuleId").val(opener.returnValue()) ;
					blockUI();
					form.submit() ;
				}
		　　}
	　	});
	});
	
	/*成交客户*/
	$(".completeBtn").click(function(){
		var keyValue = $(this).parents("ul").find("input").attr("value");
		$("input[name='keyId']").each(function() {
			if($(this).attr("value")==keyValue){
				$(this).attr("checked","checked");
			}else{
				$(this).removeAttr("checked");
			}
		});
		$("#type").val("status") ;
		$("#operation").val("22");
		$("#strStatus").val("4") ;
		
		blockUI();
		form.submit();
	});
	
	/*公共池客户*/
	$("#publicBtn").click(function(){
		$("#public").val("public");
		blockUI();
		form.submit();
	});
	
	/*客户列表*/
	$("#clientBtn").click(function(){
		$("#public").val("");
		blockUI();
		form.submit();
	});
	
	/*发送短信*/
	$("#msgBtn").click(function(){
		if(!isChecked("keyId")){
			asyncbox.tips(deleteMsg);
			return false;
		}
		var keyIds="";
		$("input:[name='keyId']:checked").each(function(){
			keyIds += $(this).attr("value")+"," ;
		}) ;
	    $.ajax({
		   type: "POST",
		   url: "/CRMClientAction.do?operation=4&type=queryContactInfo&fieldName=Mobile&clientId="+keyIds,
		   success: function(msg){
		   		if(msg == "no"){
		   			alert("请填写联系人手机信息后，再进行此操作");
		   		}else{
		   			sendMsg("sms",keyIds,top) ;
		   		}
		   }
		});
	});
	
	/*发送邮件*/
	$("#emailBtn").click(function(){
		if(!isChecked("keyId")){
			asyncbox.tips(deleteMsg);
			return false;
		}
		var keyIds="";
		$("input:[name='keyId']:checked").each(function(){
			keyIds += $(this).attr("value")+"," ;
		}) ;
		$.ajax({
		   type: "POST",
		   url: "/CRMClientAction.do?operation=4&type=queryContactInfo&fieldName=ClientEmail&clientId="+keyIds,
		   success: function(msg){
		   		if(msg == "no"){
		   			alert("请填写联系人邮箱地址后，再进行此操作");
		   		}else{
		   			sendMsg("email",keyIds) ;
		   		}
		   }
		});
	});
	
	/*显示微信DIV*/
	$("#weixinBtn").click(function(e){
		if(!isChecked("keyId")){
			asyncbox.tips(deleteMsg);
			return false;
		}
		$("#weixinContents").val("");
		$(".pop-weixin").show();
		e = e||event; stopFunc(e);//阻止冒泡
	});
	
	/*点击总显示微信*/
	$('.pop-weixin').live('click', function(e) {
		$(".pop-weixin").show();
		e = e||event; stopFunc(e);//阻止冒泡
	})
	
	/*启用*/
	$("#startBtn").click(function(){
		stopClient(0,top) ;
	});
	
	/*停用*/
	$("#stopBtn").click(function(){
		stopClient(-1,top) ;
	});
	
	/*添加*/
	$("#addBtn").click(function(){
		var height = 540;
		var winHeight = $("#clientId").height()+30;
		if(winHeight<height){
			height = winHeight;
		}
		var moduleId = $("#ModuleId").val();
		var url = "/CRMClientAction.do?operation=6&tableName=CRMClientInfo&contactTableName=CRMClientInfoDet&moduleId=" + moduleId+"&viewId="+$("#viewId").val();
		asyncbox.open({
			id:'dealdiv',url:url,title:'添加客户',width:840,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
		    callback : function(action,opener){
			    if(action == 'ok'){
			    	opener.beforeSubmit();
					return false;
				}
	　　  　 }
	　  });
	});
	
	/*编辑*/
	$(".updateBtn").click(function(){
		var keyId = $(this).parents("ul").find("input").attr("value");
		updateClient(keyId,"false") ;
	});
	
	/*客户详情*/
	$(".detailBtn").click(function(){
		var moduleId = $("#ModuleId").val();
		var keyId = $(this).parents("ul").find("input").attr("value");
		var cName = $(this).parents("ul").find(".col_name").text();
		cName = cName.substring(cName.indexOf(".")+1);   // 截取客户名称
		mdiwin('/CRMClientAction.do?operation=5&type=detailNew&keyId='+keyId+'&moduleId='+moduleId+"&viewId="+$("#viewId").val(),cName);
	});
	
	
	/*切换客户模板*/
	$(".module_list .lp").click(function(){
		var moduleId = $(this).attr("id") ;
		//var pageSize = $(this).attr("pageSize");
		$("#ModuleId").val(moduleId);
		//$("#pageSize").val(pageSize);
		$("#viewId").val("");
		blockUI();
		form.submit();
	});
	
	/*切换视图*/
	$(".view_list .lp").click(function(){
		var viewId = $(this).attr("id") ;
		$("#viewId").val(viewId);
		blockUI();
		form.submit();
	});
	
	
	
	
	$('.bd .context-menu-one').contextPopup({
	   	
       title: '',
       items: [
         {
         icon:'0 -578px',label:'修改',action:function(e) {
	      	var keyId = $(e.target).parents("ul").find("input").attr("value") ;
    	   	updateClient(keyId,"false") ;
         } },
         {icon:'-1px -684px',label:'详情',action:function(e) { 
			 var keyId = $(e.target).parents("ul").find("input").attr("value") ;
			 var cName = $(e.target).parents("ul").find(".col_name").text();
			 cName = cName.substring(cName.indexOf(".")+1);   // 截取客户名称
			 mdiwin('/CRMClientAction.do?operation=5&type=detailNew&keyId='+keyId+"&moduleId="+$("#ModuleId").val()+"&viewId="+$("#viewId").val(),cName);
		 } },
         {icon:'-2px -702px',label:'共享',action:function(e) { 
         	var obj = $(e.target).parents("ul").find("input");
         
			var keyId = $(obj).attr("value") ;
			var moduleId = $("#ModuleId").val();
			
			//检查是否有共享权限 
			var error = "false";
			
			if($(obj).parent().attr("shareOp") == "false"){
				error = "【"+$(obj).parent().attr("ClientName")+"】";
			}
			
			if(error!="false"){
				asyncbox.tips(error+"没有共享权限");
				return false;
			}
		
			shareClient(keyId,moduleId);
		 } },
		 {icon:'0 -378px',label:'新增联系人',action:function(e) { 
			 var keyId = $(e.target).parents("ul").find("input").attr("value") ;
			 var cName = $(e.target).parents("ul").find(".col_name").text();
			 cName = cName.substring(cName.indexOf(".")+1);   // 截取客户名称
			 addContact(keyId)
		 } },
		 null,
		 {icon:'-25px -702px',label:'刷新',action:function(e) { 
			 submitQuery();
		 } },
         {icon:'-24px -644px',label:'百度一下',action:function(e) {
         	var clientName = $(e.target).parents("ul").find(".col_name").text();
         	clientName = encodeURIComponent(clientName.substring(clientName.indexOf(".")+1));   // 截取客户名称
         	var url ="http://www.baidu.com/s?wd="+clientName ;
        	width=document.documentElement.clientWidth-100;
			height=document.documentElement.clientHeight-20;			
			openPop('EMailPopWin','百度一下',url,width,height,false,false);
         } },
         {icon:'-24px -664px',label:'百度地图',action:function(e) {
         	var clientName = encodeURIComponent($(e.target).parents("ul").find(".col_name").text());
         	var keyId = $(e.target).parents("ul").find("input").attr("value") ;
         	jQuery.get("/PublicServlet?operation=address&keyId="+keyId,function(response){
         		if(response.length>0){
         			clientName = encodeURIComponent(response) ;
         		}
         		var url ="http://map.baidu.com/?newmap=1&s=s%26wd%3D"+clientName ;
         		width=document.documentElement.clientWidth-100;
				height=document.documentElement.clientHeight-20;			
				openPop('EMailPopWin','百度地图',url,width,height,false,false);
         	});
         } },
         null,
         {icon:'0px -450px',label:'短信',action:function(e) {
         	var keyId = $(e.target).parents("ul").find("input").attr("value") ;
         	$.ajax({
			   type: "POST",
			   url: "/CRMClientAction.do?operation=4&type=queryContactInfo&fieldName=Mobile&clientId="+keyId,
			   success: function(msg){
			   		if(msg == "no"){
			   			alert("请填写联系人手机信息后，再进行此操作");
			   		}else{
			   			sendMsg("sms",keyId,top) ;
			   		}
			   }
			});
         }},
         {icon:'0px -425px',label:'邮件',action:function(e) {
         	var keyId = $(e.target).parents("ul").find("input").attr("value") ;
         	$.ajax({
			   type: "POST",
			   url: "/CRMClientAction.do?operation=4&type=queryContactInfo&fieldName=ClientEmail&clientId="+keyId,
			   success: function(msg){
			   		if(msg == "no"){
			   			alert("请填写联系人邮箱地址后，再进行此操作");
			   		}else{
			   			sendMsg("email",keyId);
			   		}
			   }
			});
         } }
       ]
     });
     
     /*关注客户 或取消关注*/
     $(".attentBtn").click(function(){
     	var keyId = $(this).parents("ul").find("input").attr("value");
     	var title = $("#scroll"+keyId).attr("clientname");    	
     	var attType = "add" ;
     	if($(this).text()=="取消"){
     		attType = "delete";
     		$(this).html('<span class="all_icon i_sheard"></span>关注');
     	}else{
     		$(this).html('<span class="all_icon i_sheard"></span>取消');
     	}  
     	var url="/OACollectionAction.do?operation=2&typeName=CRMClientInfo&attType="+attType+"&keyId="+keyId;
     	jQuery.ajax({
	 	 type: "POST",
	  	 url: url,	
	  	 data:{
	  	 	urlparam:"/CrmTabAction.do?operation=5&keyId="+keyId,
	  	 	titleName:"客户:"+title
	  	 },	   
	  	 success: function(msg){
	  	 	if(attType=="add"){
				if(msg=="OK"){
					asyncbox.tips('关注客户成功','success') ;
				}else{
					asyncbox.tips('关注客户失败','error') ;
				}
	     	}else{
	     		if(msg=="OK"){
					asyncbox.tips('取消关注客户成功','success') ;
				}else{
					asyncbox.tips('取消关注客户失败','error') ;
				}
	     	}	  	 	
	  	}
	});   	
     	/*jQuery.get("/OACollectionAction.do?operation=2&typeName=CRMClientInfo&attType="+attType+"&keyId="+keyId+"&moduleId="+jQuery("#ModuleId").val(),
     		function(response){
			if(attType=="add"){
				if(response=="OK"){
					asyncbox.tips('关注客户成功','success') ;
				}else{
					asyncbox.tips('关注客户失败','error') ;
				}
	     	}else{
	     		if(response=="OK"){
					asyncbox.tips('取消关注客户成功','success') ;
				}else{
					asyncbox.tips('取消关注客户失败','error') ;
				}
	     	}
		});*/
     });
    
    /* 
    //搜索按钮改变值
   	var selectId ="";
   	var subValue = "";
   	$(".sbtn[id !='multipleSelect']").each(function(i){
   	   selectId = $(this).attr("id");
   	   if($("#" + selectId.substr(1) + "Name").val() != ""){
   	   	   if(selectId != "strade" && selectId != "sdistrict"){
	   	   	   subValue = $("#" + selectId.substr(1) + "Name").val();
			   $(this).children("a").html(subValue.substring(0,6));
   	   	   }
   	   }else{
   	   	   if(selectId == "semployee"){
   	   	       $(this).children("a").html("选择职员");		
   	   	   }else if(selectId == "sdepartment"){
   	   	       $(this).children("a").html("选择部门");		
   	   	   }
   	   }	 
	});
	*/
	
	/*添加任务分派*/
	$(".addCRMTaskAssign").click(function(){
		var clientId = $(this).parents("ul").find("input").attr("value");
		//true表示CRM进入
		$.ajax({
			type: "POST",
			url: "/OATaskAction.do",
			data: "operation=6&clientId="+clientId,
			success: function(msg){								
				$("#addTaskDiv").html(msg);
				$(".addWrap").css("top","10px");
				$(".addWrap").css("left","35%");
				$("#addTaskDiv").show();
				loadTextBox('participantInfo');
				//publicDragDiv($(".addWrap"));
				//$("#hideBg").height($(".wrap").height()+10).show();					
			}
		});
		
	});
	
	/*添加日程*/
	$(".addOACalendar").click(function(){
		var clientId = $(this).parents("ul").find("input").attr("value");
		$.ajax({
			type: "POST",
			url: "/OACalendarAction.do",
			data: "operation=6&type=addCalendar&crmEnter=true&onlyShow=true&calendarClientId="+clientId,
			success: function(msg){
				$("#addCalendar").html(msg);
				$("#addCalendar").show();
			}
		});
	});
	
	
	/*添加任务分派*/
	$(".addCRMSaleFollowUp").click(function(){
		var clientId = $(this).parents("ul").find("input").attr("value");
		var urls = '/CRMBrotherAction.do?operation=6&noApprove=true&tableName=CRMSaleFollowUp&keyInfo=ClientId:'+clientId;
		asyncbox.open({
			id:'CRMSaleFollowUpId',url:urls,title:'添加联系记录',width:780,height:420,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
		    callback : function(action,opener){
			    if(action == 'ok'){
			    	opener.beforeSubmit();
					return false;
				}
	　　  　	}
	　  });
	   
	});
	
	/*审核状态查询与发货状态查询*/
	$(".searchMySelfStatus li").click(function(){
		var value = $(this).attr("statusVal");
		var fieldName = $(this).parent().attr("fieldName");
		$("#"+fieldName).val(value);
		blockUI();
		form.submit();
	});

	/*电信录单审核选项*/
	$(".postpayStatus li").click(function(){
		var value = $(this).attr("statusVal");
		$("#telecomRecordVal").val(value);
		blockUI();
		form.submit();
	});

	
	/*获取资料*/
	$("#getWorkFlowInfo").click(function(){
		$("#type").val("getWorkFlowInfo");
		$("#operation").val("22");
		blockUI();
		form.submit();
	});
	
	/*释放资料*/
	$("#releaseInfo").click(function(){
		if(!isChecked("keyId")){
			asyncbox.tips(deleteMsg);
			return false;
		}
		$("#type").val("releaseInfo");
		$("#operation").val("22");
		blockUI();
		form.submit();
	});
	
	/*拓展按钮*/
	$("#extendButton_CRMClientInfo_PickUp").click(function(){
		$("#type").val("extendButton");
		$("#operation").val("22");
		$("#defineName").val("CRMClientInfo_PickUp");
		blockUI();
		form.submit();
	});
	$("#extendButton_CRMClientInfo_GiveUp").click(function(){
		$("#type").val("extendButton");
		$("#operation").val("22");
		$("#defineName").val("CRMClientInfo_GiveUp");
		blockUI();
		form.submit();
	});
	
	
});

/*共享客户*/
function shareClient(keyId,moduleId){
	var url = "/CRMClientAction.do?operation=22&type=shareClient&clientIds="+keyId;
	asyncbox.open({
		id:'dealdiv',url:url,title:'共享客户',width:585,height:240,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
		callback : function(action,opener){
			if(action == "ok"){
				var clientIds = opener.returnVal('clientIds');//客户ids
				var isSingle = opener.returnVal('isSingle');//是否单个共享
				var isShareClient = opener.returnIsShare();//是否提醒共享人
				
				var allDeptIds = opener.returnVal('popedomDeptIds');
				var allUserIds = opener.returnVal('popedomUserIds');
				var allTitleIds = opener.returnVal('popedomTitleIds');
				//新增ids
				var addDeptIds = opener.returnSaveVal('popedomDeptIds');
				var addUserIds = opener.returnSaveVal('popedomUserIds');
				var addTitleIds = opener.returnSaveVal('popedomTitleIds');//职位ids
				
				//删除ids
				var delDeptIds = opener.returnDelVal('popedomDeptIds');
				var delUserIds = opener.returnDelVal('popedomUserIds');
				var delTitleIds = opener.returnDelVal('popedomTitleIds');//职位ids
				if(clientIds.length>0){
					$.ajax({
					   type: "POST",
					   url: "/CRMClientAction.do?operation=22&type=shareValue&clientIds="+clientIds+"&delDeptIds="+delDeptIds+"&delUserIds="+delUserIds+"&delTitleIds="+delTitleIds +"&viewId="+$("#viewId").val()+"&moduleId="+$("#ModuleId").val()+"&addDeptIds="+addDeptIds+"&addUserIds="+addUserIds+"&addTitleIds="+addTitleIds+"&isShareClient="+isShareClient+"&isSingle="+isSingle+"&allDeptIds="+allDeptIds+"&allUserIds="+allUserIds+"&allTitleIds="+allTitleIds,
					   success: function(msg){
					       if(msg == "ok"){
						       asyncbox.tips('操作成功 !','success');
					       }else{
					     	   asyncbox.tips('操作失败 !','error');
					       }	    	  
					   }
					});
				}else{
					alert("共享人不能为空！");
					return false ;
				}
			}
		}
　  });
}

/*修改客户*/
function updateClient(keyId,contactFlag){
	var height = 540;
	var winHeight = $("#clientId").height()+30;
	if(winHeight<height){
		height = winHeight;
	}
	
	var moduleId = $("#ModuleId").val();
	var url = "";
	var title = "修改客户";
	if(contactFlag == "true"){
		url = "/CRMClientAction.do?operation=7&nowHead=2&linkMan=true&addContact=true&tableName=CRMClientInfo&contactTableName=CRMClientInfoDet&keyId="+keyId+"&moduleId="+moduleId+"&viewId="+$("#viewId").val();
		height = height -100;
		title = "新增联系人"
	}else{
		url = "/CRMClientAction.do?operation=7&tableName=CRMClientInfo&contactTableName=CRMClientInfoDet&keyId="+keyId +"&moduleId="+moduleId+"&viewId="+$("#viewId").val();
	}
	asyncbox.open({
		id:'dealdiv',url:url,title:title,width:840,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	opener.beforeSubmit();
		    	$("#updateFlag").val("true");
		    	$("#updateKeyId").val(opener.form.clientId.value);
				return false;
			}
　　  　 }
　  });
}

/*启用或停用客户*/
function stopClient(status,top){
	if(!hasCheck("keyId")){
		asyncbox.tips(selectOne);
		return false;
	}
	var message = "你确定停用此用户吗？" ;
	if(0 == status){
		message = "你确定启用此用户吗？" ;
	}
	if(!confirm(message)){
		return false;
	}
	$("#operation").val("22") ;
	$("#type").val("stopClient") ;
	$("#clientStatus").val(status) ;
	blockUI();
	form.submit() ;
}

function sendMsg(msgType,keyIds,top){
	if(keyIds!="" && keyIds.length!=0){
		if(msgType.indexOf("email")>-1){
			var url = "/EMailAction.do?operation=6&msgType="+msgType+"&type=main&sendPerson="+keyIds+"&noback=true";
			
			width=document.documentElement.clientWidth-100;
			height=document.documentElement.clientHeight-20;			
			openPop('EMailPopWin','邮件发送',url,width,height,false,false);
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

/*模糊查询*/
function submitQuery(){
	blockUI();
	//$("#pageNo").val("1");
	form.submit();
	
}

function pageSubmit(pageNo){
	$("#pageNo").val(pageNo);
	blockUI();
	form.submit();
}

/**
*	锁屏
*/
function blockUI(){
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: "<div style='width:135px;'><img src='/style/images/load.gif'/><div style='color:#0179bb;font-weight:bold;'>正在查询，请稍后。。。</div></div>",css:{ background: 'transparent'}}); 
	}
}

/*弹出iframe页面*/
function openIframe(options){
	
	var frameId = $("#aio_bg_iv");
	if(typeof(frameId)!="undefined"){frameId.remove();}
	
	var html = '<div id="aiobox" style="position:absolute;top:0;left:0;z-index:201;overflow:hidden"><div id="handle" style="width:98%;height:20px;margin-top:10px;background:transparent;cursor:move;"></div>'
			+ '<iframe id="boxframe" name="boxframe" allowTransparency="true" src="'+options.url+'" frameborder="no" width="100%" height="100%" scrolling="no" onload="this.height=boxframe.document.body.scrollHeight;this.width=boxframe.document.body.scrollWidth;" />'
			+ '</div>';
	$("body").append(html);
	/*位置偏移*/
	var aioLeft = 0 ;
	var aioTop = 0 ;
	if(typeof(options.left)!="undefined"){aioLeft = options.left;}
	if(typeof(options.top)!="undefined"){aioTop = options.top;}
	if(aioLeft==0){aioLeft=(document.body.clientWidth-400)/2}
	if(aioTop==0){aioTop=(document.body.clientHeight-400)/2}
	/*宽度*/
	if(typeof(options.width)!="undefined"){
		$("#aiobox").css({width:options.width+"px"}) ;
	}
	/*高度*/
	if(typeof(options.height)!="undefined"){
		$("#aiobox").css({height:options.height+"px"}) ;
	}
	$("#aiobox").css({left:aioLeft+"px",top:aioTop+"px"}) ;
	if(options.model){
		$("body").append('<div id="aio_bg_iv" style="left:0px;top:0px;width:100%;height:6000px;position:absolute;filter: alpha(opacity=10);z-index:200;opacity: 0.1;background-color:rgb(0, 0, 0);"></div>') ;
	}
	/*注册拖拽*/
	//Drag.init(document.getElementById("handle"),document.getElementById("aiobox")) ;
}

function closeFrame(){
	$("#aio_bg_iv").remove() ;
	$("#aiobox").remove() ;
}

//添加，更新成功后的处理

function crmOpDeal(){
     $.close("dealdiv"); 
	 submitQuery();
}

//弹出框function dealAsyn(){
	if(jQuery.exist('alertDiv')){
		//如果是提醒。关闭弹出框不刷新页面
		jQuery.close('alertDiv');		
	}else if($.exist('modulediv')){
		$.close("addModuleId"); 
		$.opener('modulediv').dealAsyn();
	}else if($.exist('labelManId')){
		$.close("addLabelId"); 
		jQuery.opener("labelManId").dealAsyn();
	}else if($.exist('addLabelId')){
		jQuery.opener("addLabelId").dealAsyn();
	}else if($.exist('moduleViewId')){
		$.close("addModuleViewId"); 
		jQuery.opener("moduleViewId").dealAsyn();
	}else if($.exist('brotherId')){
		//销售跟单添加完毕后调用的方法		//调用iframe[name=brotherTable]的reloadBrother(brotherTable.jsp)方法
		window.frames['brotherTable'].reloadBrother(broherIframeTableName)
		$.close("brotherId"); 
	}else if($.exist('planId')){
		//工作计划添加完毕后调用的方法
		//调用iframe[name=brotherTable]的reloadBrother(brotherTable.jsp)方法
		window.frames['brotherTable'].reloadBrother('CRMWorkPlanView')
		$.close("planId"); 
	}else if($.exist('billUpdate')){
		//查看邻居表,销售跟单详情点击修改成功后操作
		jQuery.opener("billDetailDiv").reload();//刷新详情页面
		jQuery.close("billUpdate"); 
	}else if($.exist('taskAssignId')){
		//添加任务分派
		asyncbox.tips('添加成功','success');
		jQuery.close("taskAssignId"); 
	}else if($.exist('CRMSaleFollowUpId')){
		//添加联系记录
		asyncbox.tips('添加成功','success');
		jQuery.close("CRMSaleFollowUpId"); 
	}
	
	
}

//首页搜索
function indexSearch(searchId,searchName){
	var ids ="";
	var name = "";
	if(searchName == "trade"){
		$("#trade").val(searchId);
	}else if(searchName == "district"){
		$("#district").val(searchId.split(";")[1]);
	}else if(searchName == "foregin"){
		$("#district").val(searchId.split(",")[0]);
	}else{
		searchName = $("#popSelectName").val();
		$("#" + searchName).val(","+searchId.split(";")[0]+",");
	}
	$("#searchName").val(searchName);
	submitQuery();
}

//行业与地区弹出框默认全选
function selectAll(selectName){
	if(selectName == "trade"){
		//$("#tradeName").val("");
		$("#trade").val("");
	}else{
		$("#districtName").val("");
		$("#district").val("");
	}
	submitQuery();
}

//导航选项删除
function delSelected(id,name,delName,divName){
	$("."+divName +" div[id='"+id+"']").remove();
	var delVal = ","+ id + ","
	var reVals = "," +$("#" +delName).val();
	reVals = reVals.replace(delVal,",");
	$("#" +delName).val(reVals.substring(1));
	if($("#"+delName+"_select ul div").length == 0){
		$("#" +delName).val("");
		submitQuery();
	}
}


//双击模板移交
function dbModuleTran(moduleId){
	$("#type").val("moduleTransfer") ;
	$("#operation").val("22");
	$("#moduleTransferId").val(moduleId) ;
	form.submit() ;
}

//双击生命周期转换
function dbPreMode(preModeId){
	$("#type").val("status") ;
	$("#operation").val("22");
	$("#strStatus").val(preModeId) ;
	blockUI();
	form.submit() ;
}

function clearALL(clearName){
	$("#"+clearName).val("");
	submitQuery();
}

//新增后,默认弹出模板窗口
function openModule(firstEnter,moduleId,moduleName){
	if(firstEnter == "false"){
        var screenWidth = 1020;
		if(window.screen.width > 1200){
			screenWidth = 1200;
		}
		var urls = "/ClientSettingAction.do?moduleId="+moduleId
		asyncbox.open({
			id:'addModuleId',title :'修改'+moduleName+'模板',url:urls,cache:false,modal:true,width:screenWidth,height:500,
			callback : function(action,opener){
				if(action == 'close'){
					jQuery.close('modulediv');
				}
			}
		});
	}
}

//新增后,默认弹出模板视图窗口
function openModuleView(firstEnter,viewId,viewName,moduleId){
	if(firstEnter == "false"){
        var screenWidth = 1020;
		if(window.screen.width > 1200){
			screenWidth = 1200;
		}
		var urls = "/ClientSettingAction.do?queryType=moduleView&viewId="+viewId+"&moduleId="+moduleId;
		asyncbox.open({
			id:'updModuleViewId',title :'修改'+viewName+'模板',url:urls,cache:false,modal:true,width:screenWidth,height:500,
			callback : function(action,opener){
				if(action == 'close'){
					jQuery.close('moduleViewId');
				}
			}
		});
	}
}

/*新增标签*/
function addLabel(){
		var urls = '/CRMClientAction.do?operation=6&type=label&moduleId='+$("#ModuleId").val();
		asyncbox.open({
			id:'addLabelId',title :'新增标签',url:urls,cache:false,modal:true,width:440,height:223,btnsbar:jQuery.btn.OKCANCEL,
			callback : function(action,opener){
				if(action == 'ok'){
					opener.beforeSubmit()
					return false;
				}
			}
		});
		$(".labelEdit").hide()
}

/*标签编辑show*/
function editLabel(obj,liId){
	$(".labelEdit").hide();
	$(obj).find("img").show();
}

/*打开标签框*/
function openLabelEdit(obj,liId){
	var labelMenuHeight = parseInt($(".nui-menu").css("height").substring(0,$(".nui-menu").css("height").length-2));
	$("#labelShow :checkbox").removeProp("checked");
	if(jQuery(obj).position().top + labelMenuHeight > jQuery(".scott").position().top){
		var top = jQuery(obj).position().top - labelMenuHeight +35 + "px";
	}else{
		var top = jQuery(obj).position().top + 36 + "px";
	}
	var left = jQuery(obj).position().left - 85 + "px";
	$("#labelShow").css("top",top);
	$("#labelShow").css("left",left);
	$("#labelShow").attr("liId",liId);
	$("#"+liId+" div").each(function(){
		$("#"+$(this).attr("labelId") +"_box").attr("checked","checked");
	})
	$("#labelShow").show();
}


function hiddenLabelEdit(hideId){
	$("#"+hideId).hide();
}

function showLabelEdit(showId){
	$("#"+showId).show();
}


/*选择标签,并更新数据库*/
function selLabel(obj,id,labelName,labelColor){
	$(".labelEdit").hide();
	var $labelDiv = $("#"+$("#labelShow").attr("liId")+ " div[labelId='"+id+"']");
	if($("#"+$("#labelShow").attr("liId") + " div").length == 2 && $labelDiv.length == 0){
		asyncbox.tips('最多选择两个标签','',400);
		$("#labelShow").hide();
		return false;
	}else{
		if($labelDiv.length == 1){
			$labelDiv.remove();
			$("#labelShow").hide();
		}else{
			var str ='<div labelId="'+id+'" tabindex="0" class="nui-tag nui-tag-1" style="margin-right:3px;">'
			+'<span onclick="searchLabel(\''+id+'\')" onmouseover="showDel(this,\''+labelColor+'\',\'name\')" onmouseout="hideDel(this,\'name\')" class="nui-tag-text" style="background-color:'+labelColor+'">'+labelName+'</span>'
			+'<span id="'+id+'" onmouseover="showDel(this,\''+labelColor+'\',\'del\')" onmouseout="hideDel(this,\'del\')" onclick="delLabel(this)" tabindex="0" class="nui-tag-close nui-close" style="display:none;background-color:'+labelColor+'" title="取消标签"><b class="nui-ico" >x</b></span></div>';
			$("#"+$("#labelShow").attr("liId")).append(str);
			$(obj).parent().parent().hide();
		}
		
		var labelIds = "";
		$("#"+$("#labelShow").attr("liId") + " div").each(function(){
			labelIds += $(this).attr("labelId") +",";
		})
		var clientId = $("#"+$("#labelShow").attr("liId")).parents("ul").find("input").val();
		$.ajax({
		   type: "POST",
		   url: "/CRMClientAction.do?operation=2&type=selLabel&labelId="+labelIds +"&clientId="+clientId,
		   success: function(msg){
				if(msg == "yes"){
					if($labelDiv.length == 1){
						asyncbox.tips('取消成功 !','success',300);
					}else{
						asyncbox.tips('添加成功 !','success',300);
					}
				}else{
					asyncbox.tips('添加失败','error',300);
				}
		   }
		});
	}
	$(".labelEdit").hide()
}

/*标签显示*/
function showDel(obj,color,flag){
	if(flag == "name"){
		$(obj).animate({
		  borderTopRightRadius:0,
		  borderBottomRightRadius:0
		},100);  
		
		$(obj).next().animate({
		  borderTopLeftRadius:0,
		  borderTopRightRadius:3,
		  borderBottomLeftRadius:0,
		  borderBottomRightRadius:3
		},100);
		$(obj).next().show();
	}else{
		$(obj).prev().animate({
		  borderTopRightRadius:0,
		  borderBottomRightRadius:0
		},100);
		$(obj).show();
	}
}

/*标签删除按钮隐藏*/
function hideDel(obj,flag){
	if(flag == "name"){
		$(obj).next().hide();
		$(obj).animate({
		  borderTopRightRadius:3,
		  borderBottomRightRadius:3
		},100);  
	}else{
		$(obj).hide();
		$(obj).prev().animate({
		  borderTopRightRadius:3,
		  borderBottomRightRadius:3
		},100);
	}
}

/*取消标签,并后台更新数据库*/
function delLabel(obj){
	var clientId = $(obj).parents("ul").find("input[name='keyId']").val();
	$.ajax({
	   type: "POST",
	   url: "/CRMClientAction.do?operation=3&type=delLabel&isDelBean=false&labelId="+$(obj).attr("id")+"&clientId="+clientId,
	   success: function(msg){
	       if(msg=="yes"){
	       		$(obj).parent().remove();
	       }else{
	       		alert("删除失败");
	       }
	   }
	});
}

/*取消所有标签*/
function delAllLabel(){
	$(".labelEdit").hide();
	if($("#"+$("#labelShow").attr("liId") + " div").length>0){
		asyncbox.confirm('确定取消吗?','客户标签',function(action){
		　　　if(action == 'ok'){
		　　　　  $("#"+$("#labelShow").attr("liId") + " div").remove();
				var clientId = $("#"+$("#labelShow").attr("liId")).parents("ul").find("input").val();	
				$.ajax({
				   type: "POST",
				   url: "/CRMClientAction.do?operation=2&type=selLabel&labelId=&clientId="+clientId,
				   success: function(msg){
				        if(msg == "yes"){
							asyncbox.tips('取消成功 !','success',300);
						}else{
							asyncbox.tips('取消失败','error',300);
						}
				   }
				});
		　　　}
			 $(".labelEdit").hide()
		});
	}
}

/*CRM标签管理*/
function labelManager(){
	var urls = "/CRMClientAction.do?operation=4&type=label&moduleId="+$("#ModuleId").val()
	asyncbox.open({
		id:'labelManId',title :'标签管理',url:urls,cache:false,modal:true,width:550,height:350
　	});
	$(".labelEdit").hide()
}

/*导航标签查询*/
function searchLabel(labelId){
	if($("#labelQueryIds").val().indexOf(labelId +",") == -1){
		$("#labelQueryIds").val($("#labelQueryIds").val() + labelId +",");
		blockUI();
		form.submit() ;
	}
}

function delSearchLabel(labelId){
	if(labelId == "clear"){
		$("#labelQueryIds").val('')
	}else{
		$("#labelQueryIds").val($("#labelQueryIds").val().replace(labelId +",",""));
	}
	blockUI();
	form.submit() ;
}

//邻居表排序设置

function childrenSort(){
	var  viewId=$("#viewId").val();
	var urls = "/ClientSettingAction.do?operation=7&updPreType=brotherSort&viewId="+viewId;
	asyncbox.open({
		id:'childdiv',
		title : '邻居表设置',
		url :urls,
		modal　:true,
		width : 510,
		height : 370,
		btnsbar : jQuery.btn.OKCANCEL,
		callback : function(action,opener){
  		if(action == 'ok'){
  			opener.saveOrder();
  		}
  	  }
	});
}

function broSetfresh(){
	submitQuery();
//	window.location.reload();
}

//关键字提交
function keywordSubmit(){
	//若关键字为空或者当前关键字与原来搜索的关键字不同时页码给1
	if($("#rekeyword").val()=="" || $("#rekeyword").val() != $("#keyword").val()){
		$("#pageNo").val("1");
	}
	blockUI();
	form.submit();
}

//处理CRM,ERP客户
function CRMTransfer(keyIds,transferType){
	$.ajax({
		type: "POST",
		url: "/CRMClientAction.do",
		data: "operation=4&type=checkExistClients&clientIds="+keyIds+"&transferType="+transferType,
		async: false,
		success: function(msg){
			if(msg =="no"){
				alert("查询存在客户错误" );
			}else if(msg =="dealLine"){
		    	//被迫下线
		    	window.location.reload();
		    }else{
				var existIds = msg;
				var newKeyIds="";
				//过滤已转换的客户id
				for(var i=0;i<keyIds.split(",").length;i++){
					if(existIds.indexOf(keyIds.split(",")[i]) == -1){
						newKeyIds += keyIds.split(",")[i]+"," ;
					}
				}
				$.ajax({
					type: "POST",
					url: "/CRMClientAction.do",
					data: "operation=4&type=existClientNames&clientIds="+newKeyIds+"&transferType="+transferType,
					async: false,
					success: function(msg){
						if(msg == "errot"){
							alert("查询是否存在相同客户名称错误" );
						}else{
							if(msg != ""){
								alert(msg+"存在相同名称,无法转换客户");
							}else{
								$("#transferType").val(transferType);
								$("#clientIds").val(newKeyIds);
								$("#existIds").val(existIds);
								if(transferType == "erpToCrm"){
									$("#operation").val("1");
									$("#type").val("addTransfer");
									blockUI();
									form.submit();
								}else{
									$(".op .isd_dv").show();
								/*
									if(!confirm("是否选择转入ERP客户分类?")){
										if(confirm("是否将客户直接转入ERP客户的根级目录下?")){
											$("#operation").val("1");
											$("#type").val("addTransfer");
											blockUI();
											form.submit();
										}
									}else{
										var urlstr = "/UserFunctionAction.do?operation=22&selectName=SelectClientCatalogs&MOID="+varMOID+"&tableName=CRMClientInfo&MOOP=query&popupWin=ERPClient&LinkType=@URL:&displayName=客户";
										//var urlstr = '/UserFunctionAction.do?operation=22&selectName=transfer_selectClient'+"&MOID=$MOID&MOOP=add&popupWin=ERPClient&LinkType=@URL:&displayName=客户" ;
										//var urlstr = "/UserFunctionAction.do?tableName=tblSaleReceive&fieldName=CompanyCode&operation=22&AcceptTypeID=Receive&src=menu&MOID=83e8760a_0811132154273900324&MOOP=add&popupWin=ERPClient" ;
										asyncbox.open({id:'ERPClient',title:'客户',url:urlstr,width:750,height:470});
									}
									*/
								}
								//var urls = "/CRMClientAction.do?operation=1&type=addTransfer&moduleId="+$("#ModuleId").val()+"&viewId="+$("#viewId").val()+"&transferType="+transferType+"&clientIds="+newKeyIds+"&existIds="+existIds;
								//window.location.href=urls;
							}
						}
					  
					}
				});
				
			}
		}
	});
}


/*CRM转ERP客户选择分类*/
function exeERPClient(datas){
	var classCode = datas.split("#;#")[0];
	//classCode = "" 表示点击了清空
	if(classCode != ""){
		$("#operation").val("1");
		$("#type").val("addTransfer");
		$("#parentClasscode").val(classCode);
		blockUI();
		form.submit();
	}
}

/*转换炒作*/
function transferConfirm(obj){
	var Con = $(obj).attr("Con");
	if(Con == "y"){
		//转入子类
		$(".isd_dv").hide();
		var urlstr = "/UserFunctionAction.do?operation=22&selectName=SelectClientCatalogs&MOID="+varMOID+"&tableName=CRMClientInfo&MOOP=query&popupWin=ERPClient&LinkType=@URL:&displayName=客户";
		asyncbox.open({id:'ERPClient',title:'客户',url:urlstr,width:750,height:470});
	}else if(Con == "n"){
		//转入根目录
		$("#operation").val("1");
		$("#type").val("addTransfer");
		blockUI();
		form.submit();
	}
	$(".isd_dv").hide();
}

/**
*添加销售跟单单据
*/
function addBill(tableName){
	broherIframeTableName = tableName;//添加成功后刷新客户列表动态-邻居表
	var keyId = $("input[name='keyId']:checked").val() ;
	var urls = '/CRMBrotherAction.do?operation=6&noApprove=true&tableName='+tableName+"&brotherId="+keyId;
	asyncbox.open({
		id:'brotherId',url:urls,title:'添加跟单',width:840,height:450,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	opener.beforeSubmit();
				return false;
			}
　　  　	}
　  });
}

//处理添加,修改页面的自定义弹出框
function exeSelectPop(returnValue){
	if(jQuery.exist('dealdiv')){
	    jQuery.opener('dealdiv').exeSelectPop(returnValue);
	}
}

//ERP转CRM弹出框返回值处理
function exeErpToCrm(returnValue){
	if(typeof(returnValue)=="undefined") return ;
	var str = returnValue.split("#|#");
	var clientIds = "";
	var clientId="";
	for(var i=0;i<str.length;i++){
		clientId=str[i].split("#;#")[0] ;
		if(clientId !=""){
			clientIds +=clientId+",";
		}
	}
	//clientIds 为空表示点击清空
	if(clientIds != ""){
		var transferType = $("#erpToCrm").attr("transferType");//转换类型,crmToErp or erpToCrm
		CRMTransfer(clientIds,transferType);
	}
	jQuery.close('ErpToCrm');
	
}

/**
*客户列表弹出框兄弟表，点击销售跟单详情方法*/
function billDetail(tableName,keyId){
	var titleName = $("input[name='keyId']:checked").parents("ul").find(".col_name").text();
	titleName = titleName.substring(titleName.indexOf(".")+1);   // 截取客户名称
	var url = '/CRMBrotherAction.do?operation=5&type=detailNew&noApprove=true&keyId='+keyId+"&tableName="+tableName;
	if(tableName == "CRMSaleContract" || tableName == "CRMSalesChance"){
		mdiwin(url,titleName);
	}else{
		asyncbox.open({id:'billDetailDiv',title:titleName,cache:false,modal:true,url:url,width:780,height:450});
	}
}

/*详情连接修改方法*/
function billUpdate(keyId,tableName){
	var urls = '/CRMBrotherAction.do?operation=7&tableName='+tableName+"&keyId="+keyId;
	asyncbox.open({
		id:'billUpdate',url:urls,title:'修改跟单',width:840,height:450,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	opener.beforeSubmit();
				return false;
			}
　　  　	}
　  });
}

//阻止冒泡事件
function stopFunc(e){ 
	e.stopPropagation?e.stopPropagation():e.cancelBubble = true; 
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
	window.location.href="/CRMClientAction.do?ModuleId="+$("#ModuleId").val()+"&viewId="+$("#viewId").val()
}


/*流程图*/
function workFlowDepict(keyId){ 
	var designId = $("#designId").val()
	window.open("/OAMyWorkFlow.do?operation=5&applyType="+designId+"&keyId="+keyId,null,"height=570, width=1010,top=50,left=100 ");
}

/*新增联系人*/
function addContact(keyId){
	var nowHead ='1';
	var height = 290;
	var url = "";
	var title = "新增联系人";
	url = '/CRMClientAction.do?operation=6&type=addContact&moduleId='+$("#ModuleId").val()+'&viewId='+$("#viewId").val()+'&clientId='+keyId;
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

function addCommonInfo(obj){

	var clientId = $("#keyId").val();
	var tableName = "";
	var content = "";
}

function weixinSendMsg(){
	var weixinContents = $("#weixinContents").val();
	if($.trim(weixinContents)==""){
		asyncbox.tips('信息不能为空');
		return false;
	}
	$("#form").attr("action","/WeixinClientAction.do");
	$("#type").val("sendMessage");
	form.submit();
}

function closeWeixin(){
	jQuery(".pop-weixin").hide();
	event.stopPropagation();
}

