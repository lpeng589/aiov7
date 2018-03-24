var hideTaskAssginId;//存放任务分派完成ID
$(document).ready(function(){
	//$('.mainContent select').select();
	
	$(".four-block textarea").mouseover(function(){
		$(this).focus();
	});
	
	$(".t-tags-ul li").click(function(){
		var show = $(this).attr("show");
		$(this).addClass("t-s-li").siblings("li").removeClass("t-s-li");
		$("."+show).show().siblings("div").hide();
	}); 
	
	$(".swtoDiv").hover(
		function () {
			$(this).find(".det_update").show();
		},
		function () {
		  $(this).find(".det_update").hide();
		}
	);
	
	$(".contactBlock").hover(
		function () {
			$(this).find(".det_update").show();
		},
		function () {
		  $(this).find(".det_update").hide();
		}
	);
	
	
	
	$('.commonDetail').live('click', function() {
		var parentsId = $(this).parents("ul").attr("id");//UL的id
		var keyId = $(this).parent().attr("id");//主键id
		var url = '';
		if("CRMSaleFollowUpUL" == parentsId){
			//联系记录详情
			url = '/CRMBrotherAction.do?tableName=CRMSaleFollowUp&operation=5&keyId='+keyId;
			asyncbox.open({id:'DetailDiv',title:'联系记录详情',modal:true,url:url,width:780,height:400});
		}else if("OACalendarUL" == parentsId){
			//日程详情
			$.ajax({
				type: "POST",
				url: "/OACalendarAction.do",
				data: "operation=6&type=addCalendar&calendarFlag=detail&crmEnter=true&eventId="+keyId,
				success: function(msg){
					jQuery("#addCalendar").html(msg);
					jQuery("#addCalendar").show();
				}
			});
		}else if("OAWorkLogUL" == parentsId){
			//日志详情
			url = "/OAWorkLogAction.do?crmEnter=true&clientId="+keyId;
			title = "我的日志";
			top.mdiwin(url,title);
		}else if("OATaskUL" == parentsId){
			var title = "任务:"+$(this).text();
			mdiwin('/OATaskAction.do?operation=5&taskId='+keyId,title);
			
			/*
			hideTaskAssginId = keyId;
			//任务详情
			var urls = '/CRMTaskAssignAction.do?operation=7&isDetail=true&taskAssignId='+keyId ;
			asyncbox.open({
				id:'taskAssignId',url:urls,title:'任务分派详情',width:570,height:345,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
				callback : function(action,opener){
					 if(action == 'ok'){
						opener.submitBefore();
						return false;
					}
				}
			});
			*/
		}else{
			//报错
			asyncbox.tips('查看出错了。');
		}
		
		
	});
	
	/*删除明细行*/
	$('.childDel').live('click', function() {
		if($(this).parents("tbody").children().length==1){
			var tableName = $(this).parents("table").attr("tableName"); 
			addChildRow(tableName);
			asyncbox.tips('必须保留一行');
		}
		$(this).parents("tr").remove();
	});
	
});
//添加联系人
function addClient(){
   	jQuery("#contacts").clone(true).appendTo("p");
   //	jQuery("p div:last-child input").val("");
   	jQuery("p div:last-child input").removeProp("readonly");
   	//jQuery("p div:last-child font").remove();
   	jQuery("p div:last-child input").css("backgroundColor","");
   	jQuery("p div:last-child :checkbox").removeProp("checked");
   	jQuery("p div:last-child input[name$='mainUser']").val("2");
   	jQuery("p div:last-child").show();
   	jQuery("p div:last-child").find("img[id='delImg']").show();
	jQuery("p div:last-child").find(".menu_select").remove();
	//addSelectDiv(jQuery("p div:last-child"));	
}

function delClient(obj){
	//先判断是否只有一个联系人
	if(jQuery("div[id='contacts']:visible").length > 1){
		//删除时如果input有值给与删除提示（allEmpty == "1"）），
		var allEmpty = "0";
		jQuery(obj).parents("div[id='contacts']").find("li").children("input").each(function(){
				if(jQuery(this).val().trim() != ""){
					allEmpty = "1";
					return false;
				}
		});
		if(allEmpty == "1"){
			if(confirm('您确认删除操作么？')){
				jQuery(obj).parent().parent().remove();
			}
		}
		else{
			jQuery(obj).parent().parent().remove();
		}
		
		var nameNotNullCount =0;
		jQuery(".contactDiv div[id='contacts']:visible ul li input[id='UserName']").each(function(){
			if($(this).prev().find("font").attr("class") == "notNull"){
				nameNotNullCount++;
			}
		})
		
		if(nameNotNullCount ==0 && jQuery(".contactDiv div[id='contacts']:visible ul li input[id='UserName']").length >0){
			jQuery(".contactDiv div[id='contacts']:visible ul li input[id='UserName']:first").prev().html('<font class="notNull" id="联系人" color="red">*</font>联系人：');
		}
	
		
	}else{
		asyncbox.tips("必须保留一个联系人");
	}
}


function showInfo(headName){
	jQuery("#clientHead").removeClass();
	jQuery("#contactHead").removeClass();
	jQuery("#affixHead").removeClass();
	jQuery(".clientDiv").hide();
	jQuery(".contactDiv").hide();
	jQuery("#affixDiv").hide();
	jQuery("#submitButton").hide();	
	jQuery("#defineDetUl").hide();	
	
	if(headName == "client"){
		jQuery("#clientHead").addClass("sel");
		jQuery(".clientDiv").show();
		jQuery("#defineDetUl").show();	
		jQuery("#submitButton").show();
		jQuery("#nowHead").val("1");
	}else if(headName == "contact"){
		jQuery("#contactHead").addClass("sel");
		jQuery(".contactDiv").show();
		jQuery("#nowHead").val("2");
	}else{
		jQuery("#affixHead").addClass("sel");
		jQuery("#affixDiv").show();
		jQuery("#nowHead").val("3");
	}

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
	asyncbox.open({
		id : 'crmOpenId',
　　　   	url : '/CRMopenSelectAction.do',
	 	title : '行业',
　　 　 	width :840,
　　 　		height : 450,
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

/*
function ajaxVerify(colName,obj,type){
	var colVal = jQuery(obj).val();
	if(jQuery(obj).val().trim() != ""){
		if(jQuery(obj).val().trim()=="'" || jQuery(obj).val().trim()=="'''"|| jQuery(obj).val().trim()== "'''''"){
			alert("不能输入非法字符,请重新输入");
			jQuery(obj).val("");
			jQuery(obj).focus();
			return false;
		}
		//先判断联系人INPUT标签中是否有两个同样的值
		var flag =0;
		jQuery("input[id='" + colName +"']").each(function(){
			if($(this).val() != ""){
				if($(this).val() == $(obj).val()){
					flag++;
				}
			}
 		});
		if(flag >=2){
			alert("联系人" + jQuery(obj).prev().text().replace("：","").replace("*","") +"已重复");
			jQuery(obj).next("div").html("<font color='red' class='valSame'>已重复 </font>");	
			jQuery(obj).focus();
			return false;
		}else{
			jQuery(".valSame").remove();
		}
		
		if(type != "add"){
			//用于判断是否是原来的数据
			if(colName == "ClientName"){
				if(jQuery("#cliName").val() == colVal){
					return false;
				}
			}else if(colName == "Mobile"){
				if(con_mobile.indexOf(colVal.trim()) >-1){
					return false;
				}
			}else if(colName == "ClientEmail"){
				if(con_email.indexOf(colVal.trim()) >-1){
					return false;
				}
			}else if(colName == "Telephone"){
				if(con_tel.indexOf(colVal.trim()) >-1){
					return false;
				}
			}
		}
		var url = "CRMClientAction.do?operation=4&type=contactVerify&colName=" + colName +"&colVal=" + encodeURIComponent(encodeURIComponent(colVal));
		jQuery.ajax({
		   type: "POST",
		   url: url,
		   success: function(msg){
		   		if(msg != "no"){
		   				alert(msg);
						jQuery(obj).next("div").html("<font color='red' class='existed'>已存在 </font>");	
						jQuery(obj).focus();
		   		}else{
		   			jQuery(obj).next("div").html("");	
		   		}
		   }
		});
	}
}
*/
//处理已存在,清空数据后
function checkExist(obj){
	if(obj.value == ""){
		jQuery(obj).next("div").html("");		
	}
}

function beforeSubmit(){
	//在客户联系人点击修改进入,不检查客户字段
	if($("#linkMan").val() == undefined || $("#linkMan").val() == ""){
		//判断客户不能为空
		for(var i=0;i<jQuery("#clientAllDiv .notNull").length;i++){
			var id = jQuery("#clientAllDiv .notNull").eq(i).parent().next(":input").attr("id");
			if(id != undefined && jQuery("#" + id).val().trim() == ""){
				asyncbox.tips(jQuery("#clientAllDiv .notNull").eq(i).attr("id") + "不能为空");
				showInfo('client')
				jQuery("#" + id).focus();
				return false;
			}
		}
	}
	
	var clientName = jQuery("#ClientName").val();
	if(!isHaveSpecilizeChar(clientName)){
		asyncbox.tips("客户名称不能存在特殊字符");
		//跳转到客户信息标题
		jQuery("#clientHead").addClass("sel");
		jQuery(".clientDiv").show();
		jQuery("#submitButton").show();
		jQuery("#contactHead").removeClass();
		jQuery(".contactDiv").hide();
		jQuery("#affixHead").removeClass();
		jQuery("#affixDiv").hide();
		jQuery("#nowHead").val("1");
		
		jQuery("#ClientName").focus();
		return false;
	}
	
	//判断联系人不能为空
  	var contactEmpty = "0"
  	var $contacts;
  	if(jQuery("div[id='contacts']:visible").length !=0){
  		$contacts = jQuery("div[id='contacts']:visible");
  	}else{
  		$contacts = jQuery("div[id='contacts']");  	
  	}
	$contacts.each(function(i){
		jQuery(this).find("li").each(function(){
			if(jQuery(this).find("font").attr("class") == "notNull" ){
				if((jQuery(this).find(":input").size() >0 && jQuery.trim(jQuery(this).find(":input").val()) == "") ||
					(jQuery(this).find("select").size() >0 && jQuery.trim(jQuery(this).find("select").val()) == "")){
					asyncbox.tips(jQuery(this).find("font").attr("id") + "不能为空");
					showInfo('contact')
					jQuery(this).find("input").focus();
					contactEmpty = "1"
				}
			}
			if(contactEmpty == "1"){
				return false;
			}
		})
		if(contactEmpty == "1"){
			return false;
		}
	})
	if(contactEmpty == "1"){
		return false;
	}
	
	
	
	var $select = jQuery("select");
	//jQuery("#linkMan").val() == true ,表示从客户联系人进入,不判断客户信息的字段
	if(jQuery("#linkMan").val() == "true"){
		$select = jQuery(".contactDiv select");
	}
	//判断SELECT标签必填的
	var flag = "0"
 	$select.each(function(){
 		if(jQuery(this).val() == "" && jQuery(this).prev("span").attr("isnull") == "1"){
 			asyncbox.tips(jQuery(this).prev().children("font").attr("id") + "不能为空");
			flag ="1";
			return false;				
 		}
 	});
 	if(flag == "1"){
 		return false;
 	}
	
 	//客户判断邮箱
 	if(jQuery("#Email").val() != undefined && jQuery.trim(jQuery("#Email").val())!= ""){
 		if(!isMail(jQuery("#Email").val())){
 			asyncbox.tips("客户邮箱地址输入不正确,请重新输入");
			jQuery("#Email").focus();
			return false;
 		}
 	}
 	
	var mailFlag = "0";
 	jQuery("input[id='ClientEmail']").each(function(i){
 		if(jQuery(this).val() != undefined && jQuery.trim(jQuery(this).val()) != ""){
 			if(!isMail(jQuery(this).val())){
 				asyncbox.tips("联系人邮箱地址输入不正确,请重新输入");
				jQuery(this).focus();
				mailFlag = "1";
				return false;
			};
 		}
 	})
 	
 	if(mailFlag == "1"){
 		return false;
 	}
	
 	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	document.form.submit();
}


function addEmergencyWhy(obj){
	
}

function shareClient(keyId,moduleId,viewId,enterFlag,asynId){
	var clientName = "";
	if(enterFlag == "update"){
		clientName = jQuery("#ClientName").val();
	}else{
		clientName = jQuery("#ClientName").text();
	}
	var url = "/CRMClientAction.do?operation=22&type=shareClient&clientIds="+keyId;
	asyncbox.open({
		id:asynId,url:url,title:'共享客户',width:585,height:270,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
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
				jQuery.ajax({
				   type: "POST",
				   url: "/CRMClientAction.do?operation=22&type=shareValue&clientIds="+clientIds+"&delDeptIds="+delDeptIds+"&delUserIds="+delUserIds+"&delTitleIds="+delTitleIds +"&viewId="+viewId+"&moduleId="+moduleId+"&addDeptIds="+addDeptIds+"&addUserIds="+addUserIds+"&addTitleIds="+addTitleIds+"&isShareClient="+isShareClient+"&isSingle="+isSingle+"&allDeptIds="+allDeptIds+"&allUserIds="+allUserIds+"&allTitleIds="+allTitleIds,
				   success: function(msg){
				       if(msg == "ok"){
					       asyncbox.tips('操作成功 !','success');
				       }else{
				     	   asyncbox.tips('操作失败 !','error');
				       }	    	  
				   }
				});
			}
		}
	});
}

var hidePopFileName;//存放隐藏的弹出框ID
function deptPop(popname,selName){
	hidePopFileName = selName;
	//jQuery("#openSelectName").val(selName)
	var urls = "/Accredit.do?popname=" + popname 
	
	if(selName!="fgs")urls=urls+"&value="+jQuery("#fgs").val()
	
	 urls=urls+"&chooseData=" +jQuery("#"+selName).val();
	var title = "请选择个人";
	if(popname == "deptGroup" ){
		title = "请选择部门";
	}
	asyncbox.open({
	   id : 'Popdiv',
	   title : title,
	　　　url : urls,
	　　　width : 755,
	　　　height : 435,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。
　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。
				var str = opener.strData;
				var ids = "";
				var names = "";
				
				ids += str.split(";")[0] ;
				names += str.split(";")[1] ;
				
				
				/*
				for(var i=0;i<str.split("|").length-1;i++){
					ids += str.split("|")[i].split(";")[0] + ",";
					names += str.split("|")[i].split(";")[1] + ",";
				}*/
				jQuery("#"+selName).val(ids);
				jQuery("#"+selName+"Name").val(names);
　　　　　	}
　　　	}
　	});
}

function fillData(value){
	var openId = value.split(";")[0];	
	var openName = value.split(";")[1];	
	jQuery("#" +hidePopFileName).val(openId);
	jQuery("#" +hidePopFileName+ "Name").val(openName);
	if(typeof(parent.fillData)=="undefined"){
		jQuery.close('Popdiv');
	}else{
		parent.jQuery.close('Popdiv');
	}
}

function checkMainuser(obj){
	if($(obj).attr("checked")=="checked"){
		$(obj).next().val("1");
	}else{
		$(obj).next().val("2");
	}

}


var defineFieldName ="";//用于自定义弹出框回填字段名
var defineTableName ="";//用于自定义弹出框回填表名
var defineObj ="";//当前对象

/*自定义弹出框
fieldName:回填字段名
displayName:弹出框显示名称
selectName:弹出框名称
*/

function definePop(fieldName,displayName,selectName,tableName,asyncId){
	defineFieldName = fieldName;
	defineTableName = tableName;
	var urlstr = '/UserFunctionAction.do?operation=22&tableName='+tableName+'&selectName='+selectName+'&popupWin='+asyncId+'&MOID='+MOID+'&MOOP=add&LinkType=@URL:&displayName='+displayName;
	asyncbox.open({id:asyncId,title:displayName,url:urlstr,width:750,height:470})
}


/*
	自定义弹出框数据回填处理函数
	默认返回的第一个值为ID,第二个值为名称
	
*/
function exeSelectPop(returnValue){
	if(typeof(returnValue)=="undefined"){
		return false;
	}
	var defineIds = "";//存放id
	var definenames="";//存放中文
	var str = returnValue.split("#|#");
	if(str.length == 1){
		//单选
		defineIds = returnValue.split("#;#")[0];
		definenames = returnValue.split("#;#")[1];
	}else{
		for(var i=0;i<str.length;i++){
			if(str[i] != ""){
				defineIds +=str[i].split("#;#")[0]+";";
				definenames +=str[i].split("#;#")[1]+", ";
			}
		}
	}
	jQuery("#"+defineFieldName).val(defineIds)
	jQuery("#"+defineFieldName+"Name").val(definenames)
	jQuery("#"+defineFieldName+"Name").attr("title",definenames)
}



/*自定义明细弹出框
fieldName:回填字段名

displayName:弹出框显示名称

selectName:弹出框名称

tableName:表名

obj:当前对象
*/

function definePopDet(fieldName,displayName,selectName,tableName,obj){
	defineFieldName = fieldName;
	defineTableName = tableName;
	defineObj = obj;
	var urlstr = '/UserFunctionAction.do?operation=22&tableName='+tableName+'&selectName='+selectName+'&popupWin=SelectPopDet&MOID='+MOID+'&MOOP=add&LinkType=@URL:&displayName='+displayName;
	asyncbox.open({id:'SelectPopDet',title:displayName,url:urlstr,width:750,height:470})
}

/*单击自定义弹出框*/
function definePopDetClick(fieldName,displayName,selectName,tableName,obj){
	definePopDet(fieldName,displayName,selectName,tableName,$(obj).next());
}


/*自定义明细弹出框回填*/
function exeSelectPopDet(returnValue){
	var currTrObj = $(defineObj).parents("tr");//存放当前的TR对象
	
	var returnValueArr = returnValue.split("|");
	for(var i=0;i<returnValueArr.length;i++){
		var valueArr = returnValueArr[i].split(";");
		var returnFieldsStr = jQuery("#"+defineFieldName+"ReturnFieldsStr").val();//获取回填字段
		$(currTrObj).find("#"+defineTableName+"_"+defineFieldName).val(valueArr[0]);//第一个字段回填ID
		for(var j=0;j<returnFieldsStr.split(",").length;j++){
			$(currTrObj).find(":input[id='"+returnFieldsStr.split(",")[j]+"']").val(valueArr[j+1]);//回填展示字段
		}
		
		//如果多选大于一，往下递增行
		if(returnValueArr.length>1 && i!=returnValueArr.length-1){
			var copyRow = $(currTrObj).clone(true);
			$(currTrObj).after(copyRow);
			$(currTrObj).next().find(":input[class!='returnFields']").val("");
			currTrObj = $(currTrObj).next();//把当前对象给下一行tr,回填值
		}	
	}
}



//判断是否存在特殊字符
function isHaveSpecilizeChar(data){
	//换标准的特殊字符处理
	return containSC(data);
	//原来的特殊字符处理
	/*
	var i = data.indexOf("\"");
	var j = data.indexOf("\'");
	var m = data.indexOf("\\");
	var n = data.indexOf("\/");
	var c0=data.indexOf("@");
	var c1=data.indexOf("#");
	var c2=data.indexOf("$");
	var c3=data.indexOf("%");
	var c4=data.indexOf("^");
	var c5=data.indexOf("&");
	var c6=data.indexOf("*");
	var c7=data.indexOf("~");
	var c8=data.indexOf("!");
	var c9=data.indexOf("-");
	if (i >= 0 || j >= 0 || m >= 0 || n >= 0 ||c0>=0 || c1>=0 || c2>=0
		||c3>=0||c4>=0||c5>=0||c6>=0||c7>=0||c8>=0 || c9>=0) {
		return false;
	}
	return true;*/
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


/*添加页面点击转交*/
function approveBeforeAdd(){
	jQuery("#approveBefore").val("true");//设为true,操作成功返回index.jsp的dealCheck(keyId)方法
	beforeSubmit();
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

/*快速添加*/
function fastSubmit(tableName){
	var obj = $("span[tableName='"+tableName+"']");
	commentInfoSubmit(obj);
}

/*添加通用资料*/
function commentInfoSubmit(obj){
	var content="";//内容
	var tableName = $(obj).attr("tableName");//表名
	var contentObj;
	var today=new Date();//取今天的日期  
	if("OACalendar" == tableName){
		content = $("#calendarContent").val();
		contentObj = $("#calendarContent");
		
		if($("#calendarStartTime").val()==""){
			asyncbox.tips('日程开始时间不能为空');
			showWdatepicker('calendarStartTime');
			return false;
		}else{
			var startDate = new Date($("#calendarStartTime").val());
			if(DateDiff(startDate,today)<0){  
				asyncbox.tips("开始时间不得小于今天");  
				showWdatepicker('calendarStartTime');
				return false;
			}    
		}
		
	}else if("OAWorkLog" == tableName){
		content = $("#OAWorkLogContent").val();
		contentObj = $("#OAWorklogContent");
		
		if($("#time").val()==""){
			asyncbox.tips('日志开始时间不能为空');
			showWdatepicker('time');
			return false;
		}else{
			var startDate = new Date($("#time").val());
			if(DateDiff(startDate,today)<0){  
				asyncbox.tips("开始时间不得小于今天");  
				showWdatepicker('time');
				return false;
			}    
		}
		
	}else if("OATask" == tableName){
		content = $("#taskContent").val();
		contentObj = $("#taskContent");
		
		if($("#taskUserId").val()==""){
			asyncbox.tips('执行人不能为空');
			
			return false;
		}
		
		if($("#taskFinishTime").val()==""){
			asyncbox.tips('任务完成时间不能为空');
			showWdatepicker('taskFinishTime');
			return false;
		}else{
			var finishDate = new Date($("#taskFinishTime").val());
			if(DateDiff(finishDate,today)<0){  
				asyncbox.tips("完成时间不得小于今天");  
				showWdatepicker('taskFinishTime');
				return false;
			}  
		}
	}else{
		content = $("#followUpContent").val();
		contentObj = $("#followUpContent");
	}
	
	if($.trim(content)==""){
		asyncbox.tips('内容不能为空');
		contentObj.focus();
		return false;
	}else{
		if(!isHaveSpecilizeChar(content)){
			asyncbox.tips('内容不能存在特殊字符');
			contentObj.focus();
			return false;
		}
	}
	
	jQuery.ajax({
		type: "POST",
		url: "/CRMClientAction.do",
		data: "operation=1&type=addCommonInfo&content="+encodeURIComponent(content)+"&tableName="+tableName+"&finishTime="+$("#taskFinishTime").val()+"&taskUserId="+$("#taskUserId").val()+"&clientId="+$("#keyId").val()+"&startTime="+$("#calendarStartTime").val()+"&time="+$("#time").val(),
		success: function(msg){
			if("error" == msg){
				asyncbox.tips('添加失败','error');
			}else if("dealLine"==msg){
				//被迫下线
		    	window.location.reload();
			}else{
				asyncbox.tips('添加成功','success');
				if(""!=msg){
					var msgArr = msg.split("&&");
					var infoStr = '<li taskStatus="2" ';
					
					if("CRMSaleFollowUp"!=tableName){
						infoStr +=' class="task-li" ';
					}
					
					infoStr += 'id="'+msgArr[0]+'" title="'+msgArr[1]+'"><em class="commonDetail">'+msgArr[1]+'</em><i>'+msgArr[2]+'</i>';
					
					//任务分派加上点击按钮
					if("CRMSaleFollowUp"!=tableName&&"OAWorkLog"!=tableName){
						infoStr +='<b class="b-finish icons" onclick="finishCommentStatus(\''+msgArr[0]+'\',\''+tableName+'\');" title="点击完成"/></b>';
					}
					if("OAWorkLog"==tableName){
						infoStr +='<b  onclick="finishCommentStatus(\''+msgArr[0]+'\',\''+tableName+'\');" title="点击完成"/></b>';
					}
					infoStr +='</li>';
					if(jQuery("#"+tableName+"UL").children().length == 0){
						jQuery("#"+tableName+"UL").append(infoStr);
					}else{
						jQuery("#"+tableName+"UL li:first").before(infoStr);
					}
					
					//大于三个去掉	最后一个				
					if(jQuery("#"+tableName+"UL li").length>3){
						jQuery("#"+tableName+"UL li:last").remove();
					}
				}
				$(contentObj).val("")
			}
		}
	});
}

/*任务分派完成状态*/
function finishCommentStatus(keyId,tableName){
	if("OATask" == tableName){
		hideTaskAssginId = keyId;
		$.ajax({
			type: "POST",
			url: '/vm/oa/oaTask/addFeedback.jsp?isHead=true',
			success: function(msg){
				$("#addFeedbackDiv").html(msg);
			}
		});
		$("#addFeedbackDiv").show();
	}else{
		$.ajax({
			type: "POST",
			url: "/OACalendarAction.do",
			data: "operation=2&updateType=status&finishStatus=1&id="+keyId,
			success: function(msg){
				if("3" == msg){
					asyncbox.tips('操作成功','success');
					$("#"+keyId).remove();
				}else{
					asyncbox.tips('操作失败!','error');
				}
			}
		});		
	}
}

/*任务详情头部状态按钮反馈意见确认*/
function feedbackSubmit(){
	var feedbackContent = $("#feedbackContent").val();
	if($.trim(feedbackContent)==""){
		asyncbox.tips('情况描述不能为空');
	}else{
		$("#feedbackSubmitBtn").attr("disabled","disabled");//锁住按钮
		changeTaskStatus(hideTaskAssginId,feedbackContent)
	}
}


/*修改任务状态,
itemId:项目ID
isDetail:true表示详情进入，成功后不刷新页面
*/
function changeTaskStatus(taskId,feedbackContent){
	var status = $("#"+taskId).attr("taskStatus");
	$.ajax({
		type: "POST",
		url: "/OATaskAction.do",
		data: "operation=2&type=changeStatus&taskId="+taskId+"&status="+status+"&feedbackContent="+encodeURIComponent(feedbackContent),
		success: function(msg){
			if(msg == "error"){
				asyncbox.tips('操作失败!','error');
				$("#feedbackSubmitBtn").attr("disabled","");//解除按钮
			}else{
				asyncbox.tips('操作成功!','success');
				$("#addFeedbackDiv").hide();
				$("#"+taskId).remove();
			}
		}
	});
}


//选择日期
function showWdatepicker(elementId){
	hideElementId = elementId;//存放元素名称
	WdatePicker({el:$dp.$(elementId),lang:'zh_CN'})
}

/*客户移交*/
function handOver(){
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
				form.submit() ;
　　　　　	}
　　　	}
　	});	
}

/*修改客户*/
function updateClientDet(contactId){
	var url = "/CRMClientAction.do?operation=7&type=updContact&contactId="+contactId;
	asyncbox.open({
		id:'updateClientDetId',url:url,title:'修改客户',width:750,height:255,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	opener.beforeSubmit();
				return false;
			}
　　  　 }
　  });
}



var hideSWOTDiv;//保存还原信息对象
var hideSWOTName;//保存还原信息对象
/*编辑SWOT*/
function editSWOT(obj){
	if(typeof(hideSWOTName) != "undefined"){
		//先还原之前的
		restoreSWOTEdit(hideSWOTName);
	}
	var fieldName = $(obj).attr("fieldName");
	var fieldValue = $(obj).attr("fieldValue");
	hideSWOTDiv = $("#"+fieldName+"Div").clone(true);//拷贝详情信息，用于还原
	hideSWOTName = fieldName; 
	var str ='<div id="'+fieldName+'Div"><textarea id="'+fieldName+'" onKeyDown="if(event.keyCode==13)" style="width:180px;height:70px;margin-top:10px;">'+fieldValue+'</textarea><div class="btn btn-mini " onclick="restoreSWOTEdit(\''+fieldName+'\')" style="float:right;margin-right:3px;" >取消</div><div class="btn btn-mini btn-danger " onclick="updateSWOT(\''+fieldName+'\')" style="float:right;margin-right:3px;">确定</div></div>'
	$("#"+fieldName+"Div").remove();
	$("#"+fieldName+"Title").after(str);
	
}

/*还原编辑*/
function restoreSWOTEdit(fieldName){
	$("#"+fieldName+"Div").remove();
	$("#"+fieldName+"Title").after(hideSWOTDiv);
}

/*SWOT修改提交*/
function updateSWOT(fieldName){
	var fieldValue = jQuery.trim($("#"+fieldName).val());
	jQuery.ajax({
	    type: "POST",
	    url: "/CRMClientAction.do",
	    data: "operation=2&tableName="+$("#tableName").val()+"&type=detailUpdate&clientId="+$("#keyId").val()+"&moduleId="+$("#moduleId").val()+"&viewId="+$("#viewId").val()+"&fieldName="+fieldName+"&fieldValue="+encodeURIComponent(fieldValue),
	    success: function(msg){
	   		if(msg == "error"){
	   			asyncbox.tips('修改失败','error');
	   		}else{
	   			asyncbox.tips('修改成功','success','400');
	   			$("#"+fieldName+"Div").remove();
				$("#"+fieldName+"Title").after(hideSWOTDiv);
				$("#"+fieldName+"Cont").text(fieldValue);
				$("#"+fieldName+"Cont").attr("title",fieldValue);
				$("#"+fieldName+"EditBtn").attr("fieldValue",fieldValue);
	   		}
	    }
	})

}


/*最新动态*/
function clientLog(clientId){
	var url = "/CRMClientAction.do?operation=4&type=clientLog&clientId="+clientId;
	asyncbox.open({
		id:'crmOpDiv',url:url,title:'查看最新动态',width:800,height:450,cache:false,modal:true
　  });
}

function transferToErp(){
	$.ajax({
		type: "POST",
		url: "/CRMClientAction.do",
		data: "operation=1&type=addTransfer&isDetail=true&transferType=crmToErp&ModuleId="+$("#moduleId").val()+"&viewId="+$("#viewId").val()+"&clientIds="+$("#keyId").val(),
		success: function(msg){
			if("success"==msg){
				asyncbox.tips('转入ERP成功','success');
				$("#transferToErpBtn").remove();
				$("#isExistERPClient").val("true");
			}else{
				asyncbox.tips('转入ERP失败','error');
			}
		}
	});	
}

/*通用更多*/
function commonInfoMore(tableName){
	var url = "";
	var title = "";
	var clientId = $("#keyId").val()
	if("CRMSaleFollowUp" == tableName){
		url = "/CRMBrotherAction.do?tableName=CRMSaleFollowUp&clientKeyId="+clientId;
		title = "联系记录";
	}else if("OACalendar" == tableName){
		url = "/OACalendarAction.do?crmEnter=true&clientId="+clientId;
		title = "客户日程";
	}else if("OAWorkLog" == tableName){
		url = '/OAWorkLogAction.do?operation=4&crmEnter=true&type=WorkLogList&keyId='+clientId;
		asyncbox.open({id:'DetailDiv',title:'日志列表详情',modal:true,url:url,width:800,height:800});
		return null;
		//title="我的日志";
	}else{
		url = "/OATaskAction.do?operation=4&crmTaskEnter=true&clientId="+clientId;
		title = "我的任务";
	}
	top.mdiwin(url,title);
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

function deliverGoods(obj){
	$("#mobileCode").val("");
	$("#deliverRemark").val("");
	$("#deliverGoodsDiv").css({"left":$(obj).offset().left-305,"top":$(obj).offset().top-5});
	$("#deliverGoodsDiv").show();
}

function deliverGoodsSubmit(clientId){
	var mobileCode = $("#mobileCode").val();
	var deliverRemark = $("#deliverRemark").val();
	$.ajax({
		type: "POST",
		url: "/CRMClientAction.do",
		data: "operation=22&type=deliverGoods&clientId="+clientId+"&mobileCode="+encodeURIComponent(mobileCode)+"&deliverRemark="+encodeURIComponent(deliverRemark),
		success: function(msg){
			if(msg=="success"){
				window.location.reload();
			}else{
				asyncbox.tips('操作失败','error');
			}
		}
	});
}

function deliverGoodsCancel(obj){
	$("#deliverGoodsDiv").hide();
}


/*新增明细表行*/
function addChildRow(tableName){
	var coptRow = jQuery("#"+tableName+"_table tbody tr:last").clone(true);
	jQuery("#"+tableName+"_table tbody tr:last").after(coptRow);
	jQuery("#"+tableName+"_table tbody tr:last").find(":input[class!='returnFields']").val("");
}

function enumSelect(obj){
	if($(obj).val()=="add"){
		var url = "/CRMClientAction.do?operation=6&type=enumer&enumerName=" + $(obj).attr("enumerName")+"&selName="+$(obj).attr("enumerName");
		asyncbox.open({
			id:'enumerId',url:url,title:'新增'+$(obj).prev().text().replace("*","").replace("：",""),width:430,height:140,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
			callback : function(action,opener){
				$(obj).val("");
				if(action == "ok"){
					if(typeof(top.jblockUI)!="undefined"){
						top.jblockUI({ message: " <img src='/style/images/load.gif'/>",css:{ background: 'transparent'}}); 
					}
					var enumerVal = opener.form.enumerVal.value;
					var languageVal = opener.form.languageVal.value;
					
					var enumerId = opener.form.enumerId.value; 
					$.ajax({
						type: "POST",
						url: "/CRMClientAction.do",
						data: "operation=1&type=enumer&sort=100&enumerVal="+enumerVal+"&languageVal="+languageVal+"&enumerId="+enumerId,
						success: function(msg){
							if(msg == "success"){
								alert("添加成功");
								jQuery("select[enumerName='"+$(obj).attr("enumerName")+"']").find("option:last").before('<option value="'+enumerVal+'">'+languageVal+'</option>')
								parent.jQuery.close('enumerId');
							}else{
								alert("添加失败");
							}
							if(typeof(top.junblockUI)!="undefined"){
								top.junblockUI();
							}
						}
					});
					return false;	
				}
			}
	　  });
	}
}
