<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta name="apple-mobile-web-app-capable" content="yes"/>
<link href="/style/images/aio-icon.png" rel="apple-touch-icon"/>
<meta http-equiv="X-UA-Compatible" content="IE=9"/>
<title>$globals.getCompanyName('')</title>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="/js/jquery.jerichotab.js"></script>
<script type="text/javascript" src="$globals.js("/js/body_new.js","",$text)"></script>
<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript" src="/js/menu/jquery.bgiframe.js"></script>
<link rel="Stylesheet" href="/style/css/body.css" type="text/css"/>
<link rel="stylesheet" href="/style/css/body_menu.css" type="text/css"/>
<script type="text/javascript">
#if("$!AlertMsg" != "")
	alert("$!AlertMsg");
#end

var defSys = "$!LoginBean.defSys" ;
$(document).ready(function(){
#if("$!screenWidth" == "")
	//非正常登陆进入,先把屏幕宽度放入内存
	var screenWidth = $(document).width();
	jQuery.ajax({
	   type: "POST",
	   url: "/UtilServlet?operation=screenWidth&screenWidth="+screenWidth,
	   success: function(msg){
	   }
	});
#end
});
#if("$!globals.getSystemState().noRegUseDate" != "")
    if(confirm("$text.get("common.lb.plsReg")")){ 
    	window.location.href = '/RegisterAction.do?regFlag=2&step=two&encryptionType=$!globals.getSystemState().encryptionType'; 
	}
#end	
</script>
</head>
<body>
<div class="divTop">
    <p class="menuicon"><img src="/style/images/body/i_001.gif" style="cursor:pointer;" onClick="mopen();"></p>
    <div style="position:absolute; width:290px; top:10px;left:125px;" id="timeId"></div>
    <div class="menuTab" id="menuTabId"></div>
    <div style="position:absolute; width:260px; top:0px;right:130px;">
        <ul class="icons">
     		<li style="background:url(/style/images/body/i_005.gif) no-repeat;" onclick="sureLoginOut();" title="$text.get("login.lb.logout")"> </li>	   	
           	<li #if($LoginBean.promptSound)style="background:url(/style/images/body/i_004.gif) no-repeat;"#else style="background:url(/style/images/body/i_004_2.gif) no-repeat;" #end onClick="promptSound();" id="promptSound" title="$text.get('com.msg.prompt')"></li> 
            <li style="background:url(/style/images/body/i_003.gif) no-repeat;" onclick="showMessage();" id="messageId" title="$text.get("oa.mydesk.news.number")"></li>
            <li style="background:url(/style/images/body/i_002.gif) no-repeat;" onclick="showAdvice();" id="adviceId" title="$text.get("bottom.lb.advice")"></li>
			<li style="background:url(/style/images/body/i_000.gif) no-repeat;"onClick="mdiwin('/AlertAction.do?src=menu','$text.get("alertCenter.lb.msg")');" title=$text.get("alertCenter.lb.msg.number") id="alertId"></li>
            #if($callCenter.get("ccState") == "0"  && $globals.isSameNet($callCenter.get("ccIP")) && "$!LoginBean.extension"!="" )
			<li style="background:url(/style/images/body/i_0000.gif) no-repeat;" onclick="showTel();" title="呼叫中心"> </li>
			#end
            <div class="both"></div>		
        </ul>
    </div>
</div>
<div id="msgList" class="message" style="display:none">
	<div class="msgpoup_top"></div>
	<div id="userIdDiv" class="msgpoup_middle">
		
	</div>
	<div class="msgpoup_bottom"></div>
</div>
<div id="adviceList" class="advice">
	<iframe width=100%; height=100%; style="position:absolute;top:0px;left:0px;z-index:-1;border:none ;filter:Alpha(opacity=1)" frameborder=1></iframe>
	<div class="interior">
		<div class="interior_top">
			<div id="headTitle"></div>
			<span id="allAdvice" style="cursor:pointer;" onclick="mdiwin('/AdviceAction.do?src=menu','$text.get("bottom.lb.advice")')">查看全部</span>
		</div>
		<div class="interior_c">
		<table cellpadding="0" cellspacing="0" id="adviceBody">
				
		</table>
		</div>
	</div>
</div>
<div id="menuId" class="menuBody" style="padding:0px 0 0 10px;z-index:1000;">
	<iframe width=100%; height=100%; style="position:absolute;top:0px;left:0px;z-index:-1;border:none ;filter:Alpha(opacity=1)" frameborder=1></iframe>
	<div style="background:url(/style/images/body/i_017.gif) 20px 0px no-repeat; height:14px; float:left; width:400px;"></div>
	<div class="left">
    	<div class="name">
        	<p>      		
        		<img src="$!myPhoto" style="width:57px;height:57px;">       	
        	</p><span>$LoginBean.empFullName</span>
        </div>
        <ul class="dh">
				#foreach($module in $globals.getMainModule())
				 	#if($module !='-1') 
					<li onclick="getMenu($module);" #if($LoginBean.defSys==$module)class="li" #end value="$module">
					#if($module=='1')ERP #elseif($module=='2')OA #elseif($module=='3')CRM #elseif($module=='4')HR #end</li>
					#end
				#end
            
            <div class="both"></div>
        </ul>
        <ul id="treeNode" class="tree" style="height:280px;overflow:hidden;overflow-y:scroll;padding-right:10px;width:200px;">
        </ul>
        
        <div style="padding-left:8px;">
        	<input name="keywords" type="text" id="keywords" class="input_k" value="关键字,拼音,拼音简码搜索"  onfocus="if (this.value == '关键字,拼音,拼音简码搜索') this.value = '';" onBlur="if (this.value == '') this.value = '关键字,拼音,拼音简码搜索'" onkeydown="if(event.keyCode==8) {return;}else if(event.keyCode==13){searchMenu(this.value);}" onpropertychange="if(this.value!='关键字,拼音,拼音简码搜索'){searchMenu(this.value);}"/>
            <input name="image" type="image" src="/style/images/body/Search_b.gif"  border="0" class="imput_img" />
        </div>
    </div>
    <div class="right">
    	<ul class="menuli" style="overflow:hidden;overflow-y:auto;">
    		#foreach($menu in $myMenu)
    		#set($strUrl="$globals.get($menu,1)")
    		#if($strUrl.indexOf("?")!=-1)
    		#set($strUrl= $strUrl + "&src=menu")
    		#else
    		#set($strUrl= $strUrl + "?src=menu") 
    		#end
        	<li><a href="javascript:mdiwin('$strUrl','$globals.get($menu,0)');">$globals.get($menu,0)</a></li>
        	#end
        </ul>
        <img src="/style/images/body/menu_line.png" style="margin-bottom: 10px;" />
        <ul class="helpcontrol">
        	<li style="background:url(/style/images/body/i_008.gif) 29px 0px no-repeat;"><a href="/common/AIOHelp.zip" target="_blank">$text.get("login.lb.help")</a> </li>
            <li style="background:url(/style/images/body/i_010.gif) 28px 0px no-repeat;"><a href="/common/AIOPrint.exe" target="_blank">$text.get("common.lb.downloadActiveX")</a></li>
            <li style="background:url(/style/images/body/aiochat.png) 28px 0px no-repeat;"><a href="/common/KK_Setup.exe" target="_blank">$text.get("common.lb.downloadAIO")</a></li>
            <!-- 
            <li style="background:url(/style/images/body/i_010.gif) 28px 0px no-repeat;"><a href="/">控制面板</a></li>
             -->
            <li style="background:url(/style/images/body/i_009.gif) 28px 0px no-repeat;"><a href="javascript:changeSystem('$LoginBean.defSys')">切换旧系统</a></li>
        </ul>
    </div>
    <div class="both"></div>	
</div>
<div id="soundDivId" style="display:none"></div>
<div class="tab_content" style="width:100%;height:100%;">
	<div id="jerichotab_contentholder" class="content"></div>
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
<script type="text/javascript">
var jericho = {
    buildTabpanel: function() {
        jQuery.fn.initJerichoTab({
            renderTo: '.menuTab',
            uniqueId: 'myJerichoTab',
            tabs: [{
                title: '我的桌面',
                closeable: false,
                tabFirer: '/MyDesktopAction.do?src=menu',
                data: { dataType: 'iframe', dataLink: '/MyDesktopAction.do?src=menu' }
            }],
            activeTabIndex: 0,
            loadOnce: true
        });
   }
}
jericho.buildTabpanel();
#if("$!adviceMdiwin"!="" && $!adviceMdiwin.indexOf("mdiwin(")!=-1)
	$!adviceMdiwin ;
#end
document.getElementById("menuTabId").style.width = (document.documentElement.clientWidth-145)+"px" ;
window.onresize = function(){
	document.getElementById("menuTabId").style.width = (document.documentElement.clientWidth-145)+"px" ;
}
</script>
</body>
</html>