<html> 
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$globals.getCompanyName('')</title>
	
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="/js/menu/jquery.dimensions.js"></script>
<script type="text/javascript" src="/js/menu/jquery.positionBy.js"></script>
<script type="text/javascript" src="/js/menu/jquery.bgiframe.js"></script>
<script type="text/javascript" src="/js/menu/jquery.jdMenu.js"></script>
<script type="text/javascript" src="/js/menu/jquery.easing.min.js"></script>
<script type="text/javascript" src="/js/menu/jquery.lavalamp.js"></script>
<script type="text/javascript" src="$globals.js("/js/mainBody.vjs","",$text)"></script>
<link rel="stylesheet" href="/$globals.getStylePath()/css/menu.css" type="text/css"> 

<script language="JavaScript"> 

#if("$!AlertMsg" != "")
	alert("$!AlertMsg");
#end

#if("$!globals.getSystemState().noRegUseDate" != "")
    if(confirm("$text.get("common.lb.plsReg")")){
    	window.location.href = '/RegisterAction.do?regFlag=2&step=two&encryptionType=$!globals.getSystemState().encryptionType';
	}
#end	
var loginSys = "$!LoginBean.defSys" ;
var cururl="";

var hiddenTopMenu = false;
#if($LoginBean.viewTopMenu != "1") 
	hiddenTopMenu = true;
#end

function changeSystem(){
	setCookie("systemType",'new',10000,"/") ;
	location.href="/MenuQueryAction.do?sysType=$!LoginBean.defSys&system=new" ;
}
function changeDefSys(defSys){
	var url = "";
	if(defSys == -1){
	#if($LoginBean.operationMap.get("/AIOBOL88Action.do").query())		
		showreModule(title='$text.get('role.lb.bol88')','/AIOBOL88Action.do?');		
	#else
		alert("$text.get('common.msg.RET_NO_RIGHT_ERROR.bol88')");		
	#end
	}else if(defSys == 1){
	#if($globals.hasMainModule("1"))
		{url = '/MenuQueryAction.do?sysType=1&loadMenu=true'; document.getElementById("systemImg").src="/$globals.getStylePath()/images/home/MERP.gif"; }
	#else
		alert("$text.get('common.msg.RET_NO_RIGHT_ERROR.pps')");	
	#end
	}else if(defSys == 2){ 
	#if($globals.hasMainModule("2"))
		{url = '/MenuQueryAction.do?sysType=2&loadMenu=true'; document.getElementById("systemImg").src="/$globals.getStylePath()/images/home/MOA.gif";}
	#else
		alert("$text.get('common.msg.RET_NO_RIGHT_ERROR.oa')");		
	#end
	}else if(defSys == 3){
	#if($globals.hasMainModule("3"))
		{url = '/MenuQueryAction.do?sysType=3&loadMenu=true'; document.getElementById("systemImg").src="/$globals.getStylePath()/images/home/MCRM.gif";}
	#else
		alert("$text.get('common.msg.RET_NO_RIGHT_ERROR.crm')");		
	#end
	}else if(defSys == 4){
	#if($globals.hasMainModule("4"))
		{url = '/MenuQueryAction.do?sysType=4&loadMenu=true';document.getElementById("systemImg").src="/$globals.getStylePath()/images/home/MHR.gif";}
	#else
		alert("$text.get('common.msg.RET_NO_RIGHT_ERROR.hr')");		
	#end
	}
	
	if(url.length > 0 && url != cururl){
		jQuery.get(url,function(data){ 
			cururl = url;
			$(".jd_menu").html(data);
			$('ul.jd_menu').jdMenu();
			
		}); 
	}
}

$(document).ready(function() {
    $("#1").lavaLamp({
        fx: "backout", 
        speed: 700,
        click: function(event, menuItem) {
            return false;
        }
    });
    var defSys = "$!LoginBean.defSys";
    if(defSys == ""){
    	defSys = "1";
    }
    
    if(defSys == 1){
	#if($globals.hasMainModule("1"))		
	#else
		defSys = 2;
	#end
	}
	if(defSys == 2){ 
	#if($globals.hasMainModule("2"))
	#else
		defSys = 3;
	#end
	}
	if(defSys == 3){
	#if($globals.hasMainModule("3"))
	#else
		defSys = 4;	
	#end
	}
	if(defSys == 4){
	#if($globals.hasMainModule("4"))
		
	#else
		alert("$text.get('common.msg.RET_NO_RIGHT_ERROR.pps')");		
		return;
	#end
	}
    
    changeDefSys(defSys);
    
    #set($num=0)
	#foreach($module in $globals.getMainModule())
	#if(($module =='1' && $LoginBean.canJxc==1) || ($module =='2' && $LoginBean.canOa==1) 
				|| ($module =='3' && $LoginBean.canCrm==1) || ($module =='4' && $LoginBean.canHr==1))
	#set($num=$num+1)
	#end
	#end	
	var w=218-(5-$num)*40 +"px";;
	$("ul[class='lavaLampWithImage']").css({width: w});
	
	//安卓二维码




	var sWidth = $(document.body).outerWidth(true);
	var BW = (sWidth-300)/2;
	$("#picDiv").css("left",BW);
    
});
function showDiv(){
	$("#picDiv").show();
}
function closeSearch(){
	$('#picDiv').hide();
}
//监听浏览器可视宽度




$(window).resize(function(){
	var sWidth = $(document.body).outerWidth(true);
	var BW = (sWidth-300)/2;
	$("#picDiv").css("left",BW);
});

</script>
#if($callCenter.get("ccState") == "0"  && $globals.isSameNet($callCenter.get("ccIP")) && "$!LoginBean.extension"!="" )
<script type="text/javascript" id="cCallScript" src="http://$callCenter.get("ccIP")/mixinterface/interface.js">
</script>
#end
</head>
<body scroll="no" onLoad="loadExplore();#if("$!useGuide"=="OK")open_guide();#elseif($!url.length()>0) #end ; setTimeout('flushMsg()',2000); #if("true" == "$!globals.getSysSetting('aioshop')")flushAIOShop();#end " onUnload="RunOnUnload();">
<div id="header" class="top" style="#if($LoginBean.viewTopMenu != "1") display:none #end">
	<div class="logo"><img id="logoimg" src="/$globals.getStylePath()/images/home/logo.gif"/>&nbsp;</div>
	<ul class="lavaLampWithImage" id="1"> 
		#foreach($module in $globals.getMainModule()) 
			#if(($module =='1' && $LoginBean.canJxc==1) || ($module =='2' && $LoginBean.canOa==1) 
				|| ($module =='3' && $LoginBean.canCrm==1) || ($module =='4' && $LoginBean.canHr==1))
				<li onClick="changeDefSys($module);" #if($module =="$!LoginBean.defSys") class="current" #end>
				<a href="javascript:void(0)" #if($module=='1') title="$text.get('role.lb.pss')" #elseif($module=='2') title="$text.get('role.lb.oa')" #elseif($module=='3') title="$text.get('role.lb.crm')" #elseif($module=='4') title="$text.get('role.lb.hr')" #end >#if($module=='1')ERP #elseif($module=='2')OA #elseif($module=='3')CRM #elseif($module=='4')HR #end</a></li>	
			
			#end
		#end
		<!--  <li onClick="changeDefSys(-1);"><a href="javascript:void(0)" title="$text.get('role.lb.bol88')">BOL88</a></li>	 -->
	</ul>
	<!--用于子窗口焦点切换回父窗口，不得删除--><input type="text" name="getFocus" id="getFocus" style="width:0px; height:0px">	
	<div id="menu" style="padding-top:4px; " class='menu_a' >
		<ul class='jd_menu'  style="float:right">
			
		</ul>
		<dl style="width:30px;height:31px;float:right;margin-right:10px;padding:0px;">
			<dd style="padding:0px"><img id="systemImg" src="/$globals.getStylePath()/images/home/MERP.gif" /></dd>
		</dl>
	</div>
</div>
<!-- 手机二维码-弹出层 Start -->
	<div id="picDiv"  class="search_popup"  style="top: 150px;display:none;">
	<div id="Divtitle" class="move_dv" style="cursor:move;">
		
	</div>
  	<p class="img_p">
		<img src="/style/images/body2/android.png" alt="手机二维码" width="150" />
	</p>
	<p style="text-align:center;margin:30px 0 0 0;">
		<a href="/common/M-Office.apk" class="btn btn-success" style="color:#fff;">$text.get("common.lb.download.to.computer")</a>
		<a class="btn" onclick="closeSearch();">关闭</a>
	</p>
  </div>
  <!-- 手机二维码-弹出层 End -->		
<iframe id="pindex" name="pindex" src="/welcome.jsp"  frameborder=false scrolling="no" class="list" width="100%" height="100%" frameborder=no onload="setCwinHeight()" ></iframe>

<div class="bottom" style="height:25px">
		<div class="date">
			<span><a title="#if($!NowYear==-1)$text.get("login.lb.noMakeBill")#else$!NowYear.$!NowPeriod #end">$LoginBean.empFullName</a> </span>
			<span><script language="Javascript">   document.write(showdate())  </script></span>
			<span><script language="Javascript">   document.write(showweek())  </script></span>
		</div>
		<div class="AdShow">
			<iframe src="bottomMsg.jsp" scrolling="no" allowtransparency="true" style="width:100%;" height="18" frameborder="0" id="bottomMsg.jsp"/></iframe>
		</div>
		<div class="infoDispose">
			<div>
	<a href="javascript:void(0)" 
		onClick=" mdiwin('/AlertAction.do?src=menu','$text.get("alertCenter.lb.msg")');" title=$text.get("alertCenter.lb.msg.number") style="vertical-align:top">
	<font color="red"><div style="margin:5px 0px 0px 0px ;"><img id='alertIcon' src="/$globals.getStylePath()/images/home/alert_1.gif" style="cursor:hand"/></div><span id="alertNumber"></span></font></a>  
	<a href="javascript:void(0)"
	 	onClick="openAdvice();" title=$text.get("bottom.lb.advice")>
	<font color="red"><div style="margin:5px 0px 0px 0px ;"><img id='noteIcon' src="/$globals.getStylePath()/images/home/note_1.gif" style="cursor:hand"/></div><span id="adviceNumber"></span></font></a>
	<a href="javascript:void(0)" onClick="openMsg();" title=$text.get("oa.mydesk.news.number")>
	<font color="red"><div style="margin:5px 0px 0px 0px ;"><img id='msgIcon' src="/$globals.getStylePath()/images/home/msg_1.gif" style="cursor:hand"/></div><span id="msgNumber"></span></font></a>
	 </div>
		
	<a title="$text.get('com.msg.prompt')" >
	<img id="promptSound" height="16" width="18" onClick="promptSound();" style="vertical-align:middle; cursor:pointer;margin-top:3px 0px 0px 0px" 
	src="/style/images#if($LoginBean.promptSound)/sound.gif#else/nosound.gif#end" alt="$text.get('com.msg.prompt')"/></a>
	<a title="$text.get('com.msg.prompt')" >
	#if($callCenter.get("ccState") == "0"  && $globals.isSameNet($callCenter.get("ccIP")) && "$!LoginBean.extension"!="" )
	<img id="showTel" height="16" width="18" onClick="showTel();" style="vertical-align:middle; cursor:pointer;margin-top:3px 0px 0px 0px" 
	src="/$globals.getStylePath()/images/home/tel.gif" alt="呼叫中心"/></a>	
	#end
	<a href="javascript:moreApp();" class="help" title="$text.get("alertCenter.lb.more")"></a>
	<a href=javascript:sureLoginOut() class="logout" title="$text.get("login.lb.logout")"></a>
			
		</div>
</div> 
	#if($callCenter.get("ccState") == "0"  && $globals.isSameNet($callCenter.get("ccIP")) && "$!LoginBean.extension"!="" )	
	<script type="text/javascript">
	var extension = "$!LoginBean.extension";
	var ccIP = '$callCenter.get("ccIP")';
	var ccPort = '$callCenter.get("ccPort")';
	var telPop= false;
	var ccPrex = '$callCenter.get("ccPrex")';
	#if("$!LoginBean.telpop"=="1")
		telPop = true;
	#end
	</script>
	<script type="text/javascript" src="$globals.js("/js/mixMainCall.vjs","",$text)"></script>	
	#elseif("$!LoginBean.telpop"=="1")
	<script type="text/javascript" src="/js/hzmdcall.js"></script>
	#end
		
<div id=soundDiv style="display:none"></div>
<div id=moreAppDiv class="moreAppDiv" style="display:none">
	<!-- <a href="/common/AIOHelp.zip" target="_blank" title=$text.get("login.lb.help")>$text.get("login.lb.help")</a> -->
	<a href="/common/AIOPrint.exe" target="_blank" title=$text.get("common.lb.downloadActiveX")>$text.get("common.lb.downloadActiveX")</a>	
	<a href="/common/KK_Setup.exe" class="downloadAIO" target="_blank" title=$text.get("common.lb.downloadAIO")>$text.get("common.lb.downloadAIO")</a>
	<a onclick="showDiv();" style="cursor:pointer;">$text.get("common.lb.download.Android")</a>
	<a href="javascript:openCalc();" title="$text.get("calc.lb.calc")">$text.get("calc.lb.calc")</a>
	<a href="javascript:changeSystem();">切换风格</a>
</div>
<div id=msgDiv class="msgDiv" style="display:none">
	<div class="msgpoup_top"></div>
	<div id="userIdDiv" class="msgpoup_middle">
		
	</div>
	<div class="msgpoup_bottom"></div>
</div>
<div id="adviceDiv" style="display:none">
	<div id="adviceHead">
	<p class="floatLeft" id="headTitle">你还有通知消息未处理</p>
	<p class="allAdvice"><a style="cursor:pointer;color:blue;" onclick="mdiwin('/AdviceAction.do?src=menu','$text.get("bottom.lb.advice")');">查看全部消息</a></p>
	</div> 
	<div id="adviceBody" class="advicelist">
		
	</div>
</div>
#if($callCenter.get("ccState") == "0"  && $globals.isSameNet($callCenter.get("ccIP")) && "$!LoginBean.extension"!="" )
<div id=telDiv class="telDiv" style="display:none" >
	<div class="telDiv_title">
		<span style="margin-left:20px;width:120px;">电话分机:$!LoginBean.extension </span>
		<span id="telState" style="margin-left:00px;width:70px;"></span>
		<span id="connState" style="margin-left:0px;width:70px;text-align:right"></span>		
	</div>
	<div class="telDiv_bt">
	<button onClick="doDND()" id="DNDBt">分机示忙</button>
	<button onClick="doHold()" id="DNDBt">通话保持</button>
	<button onClick="telCall()">电话呼叫</button>
	<button onClick="telHang()">挂断电话</button>
	<button onClick="telMove()">电话转接</button>
	<button onClick="telQiang()">电话抢接</button>
	</div>
</div>
<div id=inputDiv class="inputDiv" style="display:none" >
	<div class="inputDiv_title">
		<span id="inputDiv_title" style="margin-left:10px;width:120px;">请输入号码 </span>
	</div>
	<div style="text-align:center">
		<input type="text" style="width:95%" name="inputObj" id="inputObj"> 
	</div>
	<div  class="inputDiv_bt">
		<button type="button" onClick="doInputOk()"  style="width:50px;margin:2px 4px 2px 4px;">确定</button>
		<button type="button" onClick="doInputCancel()"  style="width:50px;margin:2px 4px 2px 4px;">取消</button>
	</div>
</div>
#end
<script language="javascript">
var posX;
var posY;       
	fdiv = document.getElementById("picDiv");         
	document.getElementById("Divtitle").onmousedown=function(e){
	    if(!e) e = window.event;  //IE
	    posX = e.clientX - parseInt(fdiv.style.left);
	    posY = e.clientY - parseInt(fdiv.style.top);
	    document.onmousemove = mousemove;           
	}

document.onmouseup = function(){
    document.onmousemove = null;
}
function mousemove(ev){
    if(ev==null) ev = window.event;//IE
    fdiv.style.left = (ev.clientX - posX) + "px";
    fdiv.style.top = (ev.clientY - posY) + "px";
}

</script>
</body>
</html>