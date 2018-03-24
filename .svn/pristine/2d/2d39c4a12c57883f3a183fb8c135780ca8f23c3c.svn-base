$(document).ready(function(){
	getMenu(defSys) ;
	$("#msgList").bind("mouseout", function(){
		$("#msgList").hide();
	});
	$("#msgList").bind("mouseover", function(){
		$("#msgList").show();
	});
	$(document).keydown(function(event){
		if(event.keyCode==8) {
			//return false;
		}
	});
	$("#timeId").html(showdate()+"&nbsp;&nbsp;"+showweek()) ;
});

function showweek(){
	now = new Date() 
	if (now.getDay() == 0) return ("$text.get("oa.calendar.sunday")")   
	if (now.getDay() == 1) return ("$text.get("oa.calendar.monday")")   
	if (now.getDay() == 2) return ("$text.get("oa.calendar.tuesday")")   
	if (now.getDay() == 3) return ("$text.get("oa.calendar.wednesday")")  
	if (now.getDay() == 4) return ("$text.get("oa.calendar.thursday")")  
	if (now.getDay() == 5) return ("$text.get("oa.calendar.friday")")  
	if (now.getDay() == 6) return ("$text.get("oa.calendar.saturday")")   	
}   

function showdate() {   
	var now = new Date()   
	var year = now.getFullYear() ;   
	var month = now.getMonth()+1 ;
	var day = now.getDate() ; 
	return year+"<a #if($!NowYear==-1)title='$!{NowYear}.$!NowPeriod'#end>$text.get("com.date.year")"+month+"$text.get("com.date.month")"+day+"$text.get("com.date.day")</a>"   
} 

var setting = {
	showLine:false,
	isMenu:true,
	callback:{
		dblclick: zTreeOnDblclick
	}
};

function zTreeOnDblclick(event, treeId, treeNode){
	if(treeNode!=null && treeNode.strUrl!=null && treeNode.strUrl!=""){
		showreModule(treeNode.name,treeNode.strUrl) ;
	}
}

function searchMenu(keyWord){
	if(keyWord.length>0){
		jQuery.get("/UtilServlet?operation=menu&keyWord="+encodeURIComponent(keyWord),function(response){
			jQuery(".dh li").each(function(i){
				$(this).removeClass("li");
			}) ;
			var zNodes =eval(response) ;
			$("#treeNode").zTree(setting, zNodes);
		});
	}else{
		getMenu(defSys) ;
	}
}
function getMenu(defSys){
	jQuery(".dh li").each(function(){
		if($(this).attr("value")==defSys){
			$(this).addClass("li");
		}else{
			$(this).removeClass("li");
		}
	}) ;
	
	jQuery.get("/UtilServlet?operation=menu&defSys="+defSys,function(response){
		var zNodes =eval(response) ;
		zNodes[0].iconOpen = "/style/images/body/nn_1.png";
		zNodes[0].iconClose = "/style/images/body/nn_2.png";
		zNodes[0].icon = "/style/images/body/nn_3.png";
		$("#treeNode").zTree(setting, zNodes);
	});
}

jQuery().ready(function() {
    setTimeout('flushMsg()',2000);
});

function addTab(title,strUrl){
	 jQuery.fn.jerichoTab.addTab({
		title: title,
		tabFirer:strUrl,
		closeable: true,
		data: {
			dataType: 'iframe',
			dataLink: strUrl
		}
	}).loadData();	
}
/**
*	新版主界面 wxq
*/
function showreModule(title,strUrl){	
	//弹出到多窗口的模块一般都要加src=menu 
	if(strUrl.indexOf("src=")== -1){
		if(strUrl.indexOf("?") > 0){
			strUrl = strUrl + "&src=menu";
		}else{
			strUrl = strUrl + "?src= menu";
		}
	}
		
	jQuery.fn.jerichoTab.addTab({
		title: title,
		closeable: true,
		tabFirer:strUrl,
		data: {
			dataType: 'iframe',
			dataLink: strUrl
		}
	}).loadData();	
}

/**
*	多窗口
*/
function mdiwin(strUrl,title,alertId,strType){	
	jQuery.fn.jerichoTab.addTab({
		title: title,
		closeable: true,
		tabFirer:strUrl,
		data: {
			dataType: 'iframe',
			dataLink: strUrl
		}
	}).loadData();
	if(typeof(alertId)!="undefined"){
		jQuery.get("/UtilServlet?operation=updateAlertStatus&alertId="+alertId+"&type="+strType);
	}
}

/**
*	退出系统
*/
function sureLoginOut(){
	if(confirm('$text.get("login.lb.sureLoginOutSystem")','')) {
		window.location='/loginAction.do?operation=$globals.getOP("OP_LOGOUT")';
	}
}

/**
*	弹出菜单，单击层外面任何地方，关闭层
*/
function mopen(){	
	if($("#menuId").css("display") == "none"){
		if(navigator.appVersion.indexOf("MSIE 6")>-1 || navigator.appVersion.indexOf("MSIE 7")>-1){
			$('#menuId').bgiframe();
		}
		$("#menuId").show();
		$(document).click(function(event){
			if(event.target.parentNode.className != "menuicon"){
				var isUrl = $(event.target).parent().attr("isUrl") ;
				if(typeof($(event.target).parents(".menuBody").attr("id"))=="undefined" 
					|| event.target.tagName=="A" || typeof(isUrl)!="undefined"){
					$("#menuId").hide();
				}
			}
		});
		
		var frameId = $(".curholder").attr("id").replace("holder","iframe") ;
		$(window.frames[frameId].document).click(function(){
			$("#menuId").hide();
		});
		for(var j=0;j<window.frames[frameId].frames.length;j++){
			$(window.frames[frameId].frames[j].document).click(function(){
				$("#menuId").hide();
			});
		}
	}else{
		$("#menuId").hide();
	}
	
}

/**
*	静止声音
*/
function promptSound(){
	jQuery.get('/UtilServlet?operation=promptSound',function(response){
		var promptSound = $("#promptSound") ;
		if(response=="true"){
			promptSound.css("background","url(/style/images/body/i_004.gif) no-repeat") ;
		}else{
			promptSound.css("background","url(/style/images/body/i_004_2.gif) no-repeat") ;
		}
	});
}

/**
*	刷新系统消息
*/
function flushMsg(){
	jQuery.get("/UtilServlet?operation=msgData"+"&time="+(new Date()).getTime(),function(response){
		if(response.length>0 && response!="no response text"&&response!="close"){
			var arrayMsg = response.split("||") ;	
			if(arrayMsg[0] == 0){				
				$("#alertId").html("") ;
			}else{
				$("#alertId").html("<span>"+arrayMsg[0]+"</span>") ;
			}
			if(arrayMsg[1] == 0){				
				$("#adviceId").html("") ;
			}else{
				$("#adviceId").html("<span>"+arrayMsg[1]+"</span>") ;
			}	
			if(arrayMsg[2] == 0){				
				$("#messageId").html("") ;
			}else{
				$("#messageId").html("<span>"+arrayMsg[2]+"</span>") ;
				$("#userIdDiv").html(arrayMsg[3]) ;
			}
			if(arrayMsg[4] != ""){
				$("#soundDivId").html(arrayMsg[4]);
			}		
		}
		var alertTime = 0.5 ;		
		setTimeout('flushMsg()',alertTime*60*1000);
	}); 
}

/**
*	以下全部是关于通知的代码 阅读通知
*/
function read(keyId){
	jQuery.get("/UtilServlet?operation=updateAlertStatus&type=advice&alertId="+keyId
											+"&time="+(new Date()).getTime(),function(data){
		var adNum = $("#adviceId span") ;
		if(adNum.html()!="" && parseInt(adNum.html())>0){
			if(parseInt(adNum.html())==1){
				$("#adviceId").html("") ;
			}else{
				adNum.html(parseInt(adNum.html())-1) ;
			}
		}
	});
}

/**
*	移除已经阅读的通知
*/
function removeAdvice(keyId){
	$("#"+keyId).remove();
	read(keyId);
	if($("#adviceBody > tr").length==0){
		getAdvice() ;
	}
}

/**
*	获取最新的通知
*/
function getAdvice(){
	jQuery.getJSON("/UtilServlet?operation=adviceData"+"&time="+(new Date()).getTime(),function(data){
		var adviceBody = $("#adviceBody") ;
		adviceBody.html("") ;
		if(data.length==0){
			$("#headTitle").html("<img src='/style/images/body/tip.gif'/>今天没有您的通知消息了") ;
			adviceBody.append("<tr class='noAdvice'><td align='center'>今天没有您的通知消息了，做点其它的吧！</td></tr>") ;
		}else{
			$("#headTitle").html("<img src='/style/images/body/tip.gif'/>您还有通知消息未处理") ;
			jQuery.each(data, function(i, advice) {
				if(i%2==1){
				 	adviceBody.append("<tr id='"+advice.id+"' style='background: #fff;' title='"+advice.Title+"'><td align='left'>"+advice.Content+"</td><td align='right' width='22px'><img src='/style/images/body/close2.gif' title='已阅' onclick=\"removeAdvice('"+advice.id+"')\"/></td></tr>") ;
				}else{
					adviceBody.append("<tr id='"+advice.id+"'><td align='left' title='"+advice.Title+"'>"+advice.Content+"</td><td align='right'><img id='closeBt' src='/style/images/body/close.gif' title='已阅' onclick=\"removeAdvice('"+advice.id+"')\"/></td></tr>") ;
				}
			});
			$("#adviceBody tr").hover(
				function(){
					//$(this).css("background","#FFCC99") ;
				},
				function(){
					//$(this).css("background","#FFFFCC") ;
				}
			);
		}
	});
}
/**
*	显示下拉通知
*/
function showAdvice(){
	if($("#adviceList").css("display") == "none"){
		getAdvice() ;
		$("#adviceList").show();
		$(document).click(function(event){
			var target = $(event.target);
			if((target.attr("id")!="adviceId" && target.parent().attr("id")!="adviceId" 
				&& target.parents("#adviceList").length == 0 && target.attr("id")!="closeBt") 
				|| event.target.tagName=="A" || target.attr("id")=="allAdvice") {
				$("#adviceList").hide();
			}
		});
		
		var frameId = $(".curholder").attr("id").replace("holder","iframe") ;
		$(window.frames[frameId].document).click(function(){
			$("#adviceList").hide();
		});
		for(var j=0;j<window.frames[frameId].frames.length;j++){
			$(window.frames[frameId].frames[j].document).click(function(){
				$("#adviceList").hide();
			});
		}
	}else{
		$("#adviceList").hide();
	}
}

/**
*	显示即时通讯消息
*/
function showMessage(){
	var msgDiv = $("#userIdDiv").html() ;
	if(msgDiv.trim().length==0){
		mdiwin('/MessageQueryAction.do?src=menu','$text.get("oa.mydesk.news")')
	}else{
		if($("#msgList").css("display") == "none"){
			$("#msgList").show();
		}else{
			$("#msgList").hide();
		}
	}
}

/**
*	打开即时消息窗口
*/
function msgCommunicate(empId,empName){
 	openMsgDialog("person",empId,empName) ;
}

function openMsgDialog(operType,sendId,sendName){
	showreModule('$text.get("oa.mydesk.news")','/MessageQueryAction.do?src=menu') ;
	var activeIndex = jQuery.fn.jerichoTab.activeIndex ;
	var msg = 'window.frames["jerichotabiframe_'+activeIndex+'"].openDialog("'+operType+'","'+sendId+'","'+sendName+'")' ;
	setTimeout(msg,500);
	var adNum = $("#messageId span") ;
	if(adNum.html()!="" && parseInt(adNum.html())>0){
		if(parseInt(adNum.html())==1){
			$("#messageId").html("") ;
		}else{
			adNum.html(parseInt(adNum.html())-1) ;
		}
	}
	$("#msgList").hide();
	$("#show"+sendId).remove() ;
}

/*
*	设置Cookie
*/
function setCookie(name,value,hours,path){
    var name = escape(name);
    var value = escape(value);
    var expires = new Date();
    expires.setTime(expires.getTime() + hours*3600000);
    path = path == "" ? "" : ";path=" + path;
    _expires = (typeof hours) == "string" ? "" : ";expires=" + expires.toUTCString();
    document.cookie = name + "=" + value + _expires + path;
}

function changeSystem(sysType){
	setCookie("systemType",'old',10000,"/") ;
	location.href='/MenuQueryAction.do?sysType='+sysType ;
}
