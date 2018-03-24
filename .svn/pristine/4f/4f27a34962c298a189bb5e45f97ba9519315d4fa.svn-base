<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>OA工作台</title>
<link  type="text/css" rel="stylesheet" href="/style/css/oadesktop.css" />
<link  type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/FusionCharts.js"></script>
<script type="text/javascript" src="/js/oa/jquery-1.2.6.pack.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<style>

</style>
</head>
<body  onload="loadData();">
<input type="hidden" id="fileName" name="fileName" value="$!fileName"/>
<div id="columns">
<script type="text/javascript">
	var oDiv=document.getElementById("columns");
	var sHeight=document.documentElement.clientHeight-15;
	oDiv.style.height=sHeight+"px";
</script>
    <div style="height:1px;"></div>
    <ul id="column1" class="column" style="width:${width1}%;" colwidth="${width1}">
    	#foreach($desk in $deskMap.get(1))
         <li class="widget" id="$!desk.id" rowCount="$!desk.dataRowCount">
         	<div class="widget-head"><span #if("$!desk.moreLinkAddress"!="")title="更多" style="cursor:pointer;" onclick="javascript:more('$!desk.moreLinkAddress','$!desk.modulName');"#end>$!desk.modulName</span>#if($velocityCount==1)<img onclick="setMydesk();" src="/style/images/body/i_008.gif" class="col_setting" title="模块设置"/>#end</div>
            <div class="widget-content" id="$!{desk.id}Load">
                <div class="loading"><img src="/style/images/loading2.gif"/><span>加载中,请稍候........</span></div>
            </div>
          </li>
        #end
    </ul>

    <ul id="column2" class="column" style="width:${width2}%;" colwidth="${width2}">
    	#foreach($desk in $deskMap.get(2))
         <li class="widget" id="$!desk.id" rowCount="$!desk.dataRowCount">
         	<div class="widget-head"><span #if("$!desk.moreLinkAddress"!="")title="更多" style="cursor:pointer;" onclick="javascript:more('$!desk.moreLinkAddress','$!desk.modulName');"#end>$!desk.modulName</span></div>
            <div class="widget-content" id="$!{desk.id}Load">
                <div class="loading"><img src="/style/images/loading2.gif"/><span>加载中,请稍候........</span></div>
            </div>
          </li>
        #end
    </ul>
       
    <ul id="column3" class="column"  style="width:${width3}%;" colwidth="${width3}">
    	#foreach($desk in $deskMap.get(3))
         <li class="widget" id="$!desk.id" rowCount="$!desk.dataRowCount">
         	<div class="widget-head"><span #if("$!desk.moreLinkAddress"!="")title="更多" style="cursor:pointer;" onclick="javascript:more('$!desk.moreLinkAddress','$!desk.modulName');"#end>$!desk.modulName</span></div>
            <div class="widget-content" id="$!{desk.id}Load">
                <div class="loading"><img src="/style/images/loading2.gif"/><span>加载中,请稍候........</span></div>
            </div>
          </li>
        #end  
    </ul>   
</div>
<script type="text/javascript">
function flushMsg(){
	jQuery.get("/MyDesktopAction.do?operation=77&type=msgCount"+"&time="+(new Date()).getTime(),function(response){
		if(response.length>0 && response!="close"){
			var arrayMsg = response.split(":") ;
			for(var i=0;i<arrayMsg.length;i++){
				if(arrayMsg[i] == 0){				
					$("#image"+(i+1)).attr("src","/style/images/desktop/msg_icon_"+(i+1)+".jpg");
				}else{
					$("#image"+(i+1)).attr("src","/style/images/desktop/msg_icons_"+(i+1)+".jpg");
				}	
			}
		}	
		setTimeout('flushMsg()',60*1000);
	}); 
}

function uploadImage(){
	//if(confirm("$text.get("mydesktop.msg.IsUploadPhoto")")){
		asyncbox.open({
	　　　	id : 'deskTopId',title : '$text.get("crm.deskTop.myHeadportrait")',
			url : '/MyDesktopAction.do?operation=$globals.getOP("OP_UPLOAD_PREPARE")',
			cache : false,modal:true,width:440,height:350,
			callback : function(action,opener){			    
	　　  　 }
		});
	//}
}

function loadData(){
	#foreach($desk in $deskMap.get(0))
	f$!{desk.id}();
	#end
}

#foreach($desk in $deskMap.get(0))
function f$!{desk.id}(rowCount){
	var rowCounts =  #if($!desk.dataRowCount=="")0 #else $!desk.dataRowCount #end ;
	if(typeof(rowCount)!="undefined"){
		rowCounts = rowCount;
	}
	jQuery.ajax({url: "/MyDesktopAction.do?operation=77&type=$!desk.moduleType&deskId=$!desk.id&rowCount="+rowCounts,cache: false,success: function(html){if(html.length==0){$("#"+"$!desk.id").find(".widget-content").hide();$("#"+"$!desk.id").children(".widget-head").children(".collapse").css({backgroundPosition: '-114px 0'});}$("#"+"$!{desk.id}Load").html(html) ;#if($!desk.moduleType=="person")flushMsg();#end changeWidth("$!desk.id");#if($!desk.moduleType=="news")if(typeof($("#YSlide").attr("id"))!="undefined"){YAO.YTabs({tabs: YAO.getEl('jSIndex').getElementsByTagName('a'),contents: YAO.getElByClassName('YSample', 'p', 'YSlide'),defaultIndex: 0,auto: true,fadeUp: true});}#end}});
}
#end
	
function changeWidth(columnId){
	$("#"+columnId).find(".Block_news_list").find("a").each(function(i){
		var width = $("#"+columnId).parent().outerWidth() ;
		width = width-110 ;
		$(this).width(width).find("i.char").css("max-width",width-26);
	});
	$("#"+columnId).find(".Block_word_list").find("a").each(function(i){
		var width = $("#"+columnId).parent().outerWidth() ;
		width = width-47 ;
		$(this).css("width",width) ;
	});
	$("#"+columnId).find(".Block_word_list").find("div").each(function(i){
		var width = $("#"+columnId).parent().outerWidth() ;
		width = width-35 ;
		$(this).css("width",width) ;
	});
	$("#"+columnId).find(".Block_honor_info").find("a").each(function(i){
		var width = $("#"+columnId).parent().outerWidth() ;
		width = width-200 ;
		$("#flow").css("width",width) ;
	});
}

function detail(id,typeid,userId){
 	if(typeid==1){
 		if(confirm("$text.get('crm.deskTop.applause')")){
 			AjaxRequest("/UtilServlet?operation=fameTopWish&id="+id+"&typeId="+typeid+"&userId="+userId);
 		}
 	}else if(typeid==2){
 		if(confirm("$text.get('crm.deskTop.Blood')")){
 			AjaxRequest("/UtilServlet?operation=fameTopWish&id="+id+"&typeId="+typeid+"&userId="+userId);
 		}
 	}else{
 		if(confirm("$text.get('crm.deskTop.Below.Defiant')")){
 			AjaxRequest("/UtilServlet?operation=fameTopWish&id="+id+"&typeId="+typeid+"&userId="+userId);
 		}
 	}
}

function fameWishdetail(status){
	window.open("/MyDesktopAction.do?operation=5&status="+status,"width=800,height=500");
}

function onfamewish(employeeID){
	if(document.getElementById(employeeID).alt !=""){
	  	document.getElementById(employeeID).alt="";
	}
}

function flowDepict(applyType,keyId){ 
	window.showModalDialog("/OAMyWorkFlow.do?keyId="+keyId+"&operation=$globals.getOP("OP_DETAIL")&applyType="+applyType,winObj,"dialogWidth=1010px;dialogHeight=570px");
}

function more(strUrl,strTitle){
	mdiwin(strUrl,strTitle) ;
}

function save(obj){
	var rowCount = $(obj).parents(".edit-box").find("input").val() ;
	var moduleId = $(obj).parents(".widget").attr("id") ;
	jQuery.get("/MyDesktopAction.do?operation=2&moduleId="+moduleId+"&rowCount="+rowCount, function(data){
		if("YES" == data){
			$(obj).parents(".edit-box").hide() ;
			refresh(moduleId,rowCount) ;
		}
	});
}
function refresh(strId,rowCount){
	jQuery("#"+strId+"Load").html('<div class="loading"><img src="/style/images/loading2.gif"/><span>加载中,请稍候........</span></div>');
	eval("f"+strId+"("+rowCount+")") ;
}

function setMydesk(){
	asyncbox.open({
　　　	id : 'deskTopId',title : '工作台设置',
		url : '/MyDesktopAction.do?operation=84&date='+(new Date().getTime()),
		cache : false,modal:true,width:700,height:430,
		btnsbar : jQuery.btn.OKCANCEL, 
　　　	callback : function(action,opener){
　　　　　	if(action == 'ok'){
　　　　　　　	if(!opener.saveDesk()){
					return false ;
				}
　　　　　	}
　　　	}
	});
}

function mdiwin(url,title,obj){
	if($(obj).parents("#YSlide").attr("id")!="YSlide"){
		$(obj).children("img").remove();
	}else{
		$(obj).parents(".YSample").find(".bg_i").remove();
	}
	top.showreModule(title,url);
}

function dealAsyn(){
	jQuery.close('deskTopId');
	window.document.location.reload()
}
</script>
<script language="javascript" type="text/javascript" src="/js/oa/pic-news.js"></script>
<script type="text/javascript" src="/js/oa/jquery-ui-personalized-1.6rc2.min.js"></script>
<script type="text/javascript" src="/js/oa/inettuts.js"></script>
</body>
</html>