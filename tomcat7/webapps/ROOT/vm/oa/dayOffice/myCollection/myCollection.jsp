<!doctype html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta charset="utf-8">
#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#else
<title>我的收藏</title>
#end
<link type="text/css" href="/style/css/todo/MyCollection.css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
jQuery(document).ready(function() {
	$("#context").keydown(function(event) {  
         if (event.keyCode == 13) {           	
			form.submit();
         }  
     }) 
})

function detailTitle(url,title){
	var addTHhead = $("#addTHhead").val();	
	/*if(addTHhead == "true"){
		window.location=url+"&addTHhead="+addTHhead;
	}else{*/
		var titles = title.split(":");
		mdiwin(url,titles[1]);
	//}	
}
function delCollection(id){	 
	 var url = "/OACollectionAction.do?operation=3&id="+id;
	 jQuery.ajax({
  	 type: "POST",
   	 url: url,		   
   	 success: function(msg){ 	 	
	   	 	if(msg != ""){ 	 
	   	 		asyncbox.tips('取消成功','success');		
				var li = $("#"+id);				   	 			   	 		
	   	 		$(".page-number").html("("+msg+")");	   	 		
	   	 		if(msg=="0"){
	   	 			$(li).hide();
	   	 			$(".scott").hide();
	   	 			$(".proList").prepend('<div class="no-collection">还没有收藏，赶紧去添加吧!</div>');	   	 			
	   	 		}else{
	   	 			$(li).hide(1000);
	   	 		}	 		   	 		
	   		}else{
	   			asyncbox.tips('取消失败','error');
	   		}		    
   		} 
	});
}
function queryLi(type){
	$("#tabFlag").val(type);
	if(type==""){
		$("#backColor").val("all");
	}else{
		$("#backColor").val(type);
	}	
	form.submit();
}
function searchFind(){
	form.submit();
}
/*页面切换*/
function pageSubmit(num){
	$("#pageNo").val(num);
	form.submit();
}
/*指定页*/
function submitQuery(){
	form.submit();
}

function onloadColor(){
	if("$!ctList" == "[]"){
		$(".scott").hide();
	}	
	var backColor = $("#backColor").val();
		
	if(backColor == "OABBSSends;OABBSTopic"){
		$("#OABBSSends").css('background','#ebf2fa');
	}else{
		$("#"+backColor).css('background','#ebf2fa');
	}
		
}
</script>

</head>

<body onload="onloadColor();showStatus();">
#if("$!addTHhead" == "true")
#parse("./././body2head.jsp")
<form method="post" name="form" action="/OACollectionAction.do?addTHhead=$!addTHhead">
#else
<form id="scroll-wrap" method="post" name="form" action="/OACollectionAction.do">
#end
<input type="hidden" name="operation" value="4"/>
<input type="hidden" name="tabFlag" id="tabFlag" value="$!tabFlag"/>
<input type="hidden" name="backColor" id="backColor" value="$!backColor"/>
<input type="hidden" id="addTHhead" name="addTHhead"  value="$!addTHhead" />
	<div class="wrap">
        <!-- 头部header 分割线 Start -->
        <div class="header">
            <div class="left-title">
                <b class="icons WaitPhoto"></b>
                <em>我的收藏</em>
                <div class="t-search">
                	<input class="t-search-txt" type="text" id="context" name="context" value="$!context" placeholder="输入要找的信息" />
                    <b class="t-search-btn icons" title="搜索" onclick="searchFind();"></b>
                </div>
                
            </div> 
        </div>
        <!-- 头部header 分割线 End -->
        <!-- td_main 分割线 Start -->   
        <div class="td-main">
        	<div class="lf td-main-left">
            	
            	<!-- 项目列表 proList 分割线 Start -->
                <div class="proList">
                	#if("$!ctList" == "[]")  
                	<div class="no-collection">
                		还没有收藏，赶紧去添加吧!
                	</div>             	
                	#end              	
                    <ul class="proListU">
                    
                     #foreach($log in $!ctList)
                        <li id="$log.id">
                          <i class="lf iContent" onclick="detailTitle('$log.url','$log.title');">$!globals.get($log.title.split(':'),1)</i> 
                          <span class="lf pr typeWait">
                            <i style="background:#$mapList.get($log.type)"> #if("$log.type" == "OABBSSends" || "$log.type" == "OABBSTopic")我的论坛#elseif("$log.type" == "CRMClientInfo")我的客户#elseif("$log.type" == "OAItems")我的项目#elseif("$log.type" == "OATask")我的任务
                           #elseif("$log.type" == "OAnewAdvice")通知通告#elseif("$log.type" == "OAWorkLog")我的日志#elseif("$log.type" == "OANews")新闻中心#elseif("$log.type" == "OAKnowCenter")知识中心#elseif("$log.type" == "OAMail")我的邮件#elseif("$log.type" == "OAOrdain")规章制度#end</i>
                          </span>
                          <b class="lf time">#if("$!log.createTime.substring(0,10)" == "$globals.getDate()")今日$!log.createTime.substring(11,19)                      
                      		#elseif("$!log.createTime.substring(0,10)" == "$!yestoday" )昨天$!log.createTime.substring(11,19)#elseif("$!log.createTime.substring(0,10)" == "$!oldthree")前天$!log.createTime.substring(11,19)
                      		#else$!log.createTime#end</b>
                          <span class="pr rf collected" title='取消收藏'  onclick="delCollection('$log.id');">
                            <b class="icons"></b>
                          </span>
                        </li>
                        #end  
                                                   
                    </ul>
                </div>
                <!-- 项目列表 proList 分割线 End -->    
            </div>
            <div class="lf td-main-right">
            	<ul class="uType">
            	<li class="no-type" id="all" onclick="queryLi('');">
                        <b class="pa" style="background-color:#efc0f6"></b>
                        全部分类
                 </li>
                 <li class="no-type" id="OAnewAdvice" onclick="queryLi('OAnewAdvice');">
                        <b class="pa" style="background-color:#df7ba6"></b>
                        通知通告
                 </li>
                 <li class="no-type" id="OABBSSends" onclick="queryLi('OABBSSends;OABBSTopic');">
                        <b class="pa" style="background-color:#47a91c"></b>
                        我的论坛
                 </li>
                 <!-- 
                 <li class="no-type" id="CRMClientInfo" onclick="queryLi('CRMClientInfo');">
                        <b class="pa" style="background-color:#9133DB"></b>
                        我的客户
                 </li>
                  -->
                 <li class="no-type" id="OAItems" onclick="queryLi('OAItems');">
                        <b class="pa" style="background-color:#b54143"></b>
                        我的项目
                 </li>
                 <li class="no-type" id="OATask" onclick="queryLi('OATask');">
                        <b class="pa" style="background-color:#3796bf"></b>
                        我的任务
                 </li>
                 <!-- 
                 <li class="no-type" id="OAWorkLog" onclick="queryLi('OAWorkLog');">
                        <b class="pa" style="background-color:#e5acae"></b>
                        我的日志
                 </li>  -->
                 <li class="no-type" id="OANews" onclick="queryLi('OANews');">
                        <b class="pa" style="background-color:#efc0f6"></b>
                        新闻中心
                 </li>  
                 <li class="no-type" id="OAKnowCenter" onclick="queryLi('OAKnowCenter');">
                        <b class="pa" style="background-color:#cf69e2"></b>
                        知识中心
                 </li>  
                 <li class="no-type" id="OAMail" onclick="queryLi('OAMail');">
                        <b class="pa" style="background-color:#abe7d9"></b>
                       我的邮件
                 </li>  
                 <li class="no-type" id="OAOrdain" onclick="queryLi('OAOrdain');">
                        <b class="pa" style="background-color:#47a81c"></b>
                       规章制度
                 </li>          	
                </ul>
            </div>
        </div>
        <!-- td_main 分割线 End -->
        $!pageBar
	<div style="clear:both;"></div>
    </div>
    
</form> 
<script type="text/javascript">
	var oDiv=document.getElementById("scroll-wrap");
	var sHeight=document.documentElement.clientHeight;
	oDiv.style.height=sHeight+"px";
</script>  
</body>
</html>
