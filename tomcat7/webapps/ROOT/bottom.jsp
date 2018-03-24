<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Bottom</title>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="$globals.js("/js/mainBody.vjs","",$text)"></script>
#if($callCenter.get("ccState") == "0" && "$!LoginBean.extension"!="" && "$!LoginBean.telpop"=="1")
<script type="text/javascript" id="cCallScript" src="http://$callCenter.get("ccIP")/mixinterface/interface.js">
</script>
#end
<link rel="stylesheet" href="/$globals.getStylePath()/css/menu.css" type="text/css">
</head>

<body onLoad="setTimeout('flushMsg()',10000); #if("true" == "$!globals.getSysSetting('aioshop')")flushAIOShop();#end isExistNewChat();">
	<div class="bottom">
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
	onClick="mdiwin('/AlertAction.do?src=menu','$text.get("alertCenter.lb.msg")')" title=$text.get("alertCenter.lb.msg.number") style="vertical-align:top">
	<font color="red" ><div style="margin:5px 0px 0px 0px ;"><img id='alertIcon' src="/$globals.getStylePath()/images/home/alert_1.gif" style="cursor:hand"/></div><span id="alertNumber" style="font-size:5px"></span></font></a>  
	<a href="javascript:void(0)"
	 onClick="mdiwin('/AdviceAction.do?src=menu','$text.get("bottom.lb.advice")')" title=$text.get("bottom.lb.advice")>
	<font color="red" ><div style="margin:5px 0px 0px 0px ;"><img id='noteIcon' src="/$globals.getStylePath()/images/home/note_1.gif" style="cursor:hand"/></div><span id="adviceNumber" style="font-size:5px"></span></font></a>
	<a href="javascript:void(0)" onClick="mdiwin('/MessageQueryAction.do?src=menu','$text.get("oa.mydesk.news")')" title=$text.get("oa.mydesk.news.number")>
	<font color="red" ><div style="margin:5px 0px 0px 0px ;"><img id='msgIcon' src="/$globals.getStylePath()/images/home/msg_1.gif" style="cursor:hand"/></div><span id="msgNumber" style="font-size:5px"></span></font></a>
	 </div>
	<a href="/common/AIOPrint.exe" class="downloadActiveX" target="_blank" title=$text.get("common.lb.downloadActiveX")></a>
	<a href="/common/AIOHelp.zip" class="help" target="_blank" title=$text.get("login.lb.help")></a>
		
	<a title="$text.get('com.msg.prompt')" >
	<img id="promptSound" height="16" width="18" onClick="promptSound();" style="vertical-align:middle; cursor:pointer;margin-top:3px 0px 0px 0px" 
	src="/style/images#if($LoginBean.promptSound)/sound.gif#else/nosound.gif#end" alt="$text.get('com.msg.prompt')"/></a>	
	<a href="javascript:openCalc();" class="calculator" title="$text.get("calc.lb.calc")"></a>
	<a href=javascript:sureLoginOut() class="logout" title="$text.get("login.lb.logout")"></a>
			
		</div>
	</div> 
	#if("$!LoginBean.telpop"=="1")
	#if($callCenter.get("ccState") == "0" && "$!LoginBean.extension"!="" )
<script type="text/javascript">	
mixinterface();
function mixOnInit(){	
	mixConnect('$callCenter.get("ccIP")','$callCenter.get("ccPort")');	
}

function mixOnConnectError(){
	alert("呼叫中心连接失败，请检查IP:$callCenter.get("ccIP"),端口：$callCenter.get("ccPort")配置是否正确");
}
function mixOnConnect(){  	
	//注册弹弹事件
	mixSetPopScreen("$!LoginBean.extension","all","RING","$!LoginBean.extension");
	
	
}
function mixOnPopScreen(str){
	//Callerid RecordFile
	Callerid = parseStr(str,"Callerid");
	if(Callerid != "$!LoginBean.extension"){
		window.open("/ClientServiceAction.do?keyWord="+Callerid,"_blank","fullscreen");
	}
}
function mixOnResult(str){
    //alert(str);
}
function parseStr(str,key){
	pos = str.indexOf(key)+key.length+2;
	ss = str.substring(pos,str.indexOf("\r",pos));	
	return ss;
}

</script>
	
	#else
	<script type="text/javascript" src="/js/hzmdcall.js"></script>
	#end
	#end
	
	<div id=soundDiv></div>
	<div id="xx" style="cursor:default; position:absolute; left:400px; top:-50px;width:200px; height:200px;border:1px solid #ff00ff"></div>
</body>
</html>




