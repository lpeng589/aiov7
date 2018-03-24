<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<title>top</title>
<style type="text/css"> 
<!--
body {
	background:#ffffff url(/style/images/aiobg.gif) repeat-x left -121px;
}
-->

.tips_div{
width:650px;
height:65px;
border:1px solid #57a7ff;
background-color:#e3f0ff;
}
.tips_content{
margin-top:2px;
margin-left:2px;
margin-right:2px;
border: 1px dotted #CCCCFF;
background:#F4FAFF;
width:100%;
height:30px;
}
.tips_operate{
width:100%;
text-align:left;
margin-top:0px;
margin-bottom:0px;
}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">

function $d(obj){
	return document.getElementById(obj) ;
}
var keyId = "" ;
function showDisplay(varDiv){
	if("pindex"==varDiv){
		$d("pindex").style.display = "block" ;
		$d("detail").style.display = "none" ;
		$d("pindexId").className = "listRange_1_space_1_div_a" ;
		$d("detailId").className = "listRange_1_space_1_div_b" ;
	}else{
		var strKeyId = document.frames["pindex"].document.frames["leftFrame"].document.getElementById("clientId").value ;
		if(strKeyId==="")return ;
		if(keyId =="" || strKeyId!=keyId){
			keyId = strKeyId ;
			$d("detail").src = "/CrmTabAction.do?operation=5&showClose=false&keyId="+keyId;
			var frameHeight = document.all['detail'].document.body.clientHeight;
			var varDetail= $d("detail");
			varDetail.style.height=(frameHeight-22)+"px";
		}
		$d("pindex").style.display = "none" ;
		$d("detail").style.display = "block" ;
		$d("detailId").className = "listRange_1_space_1_div_a" ;
		$d("pindexId").className = "listRange_1_space_1_div_b" ;
	}
}
function setHeight(){
	var frameHeight = document.body.clientHeight;	
	var varCenter=$d("pindex");
	frameHeight = frameHeight -30;
	varCenter.style.height=frameHeight+"px";
	varCenter.contentWindow.leftFrame.setHeight(frameHeight);
}


</script>
#if($callCenter.get("ccState") == "0" && $globals.isSameNet($callCenter.get("ccIP")))
<script type="text/javascript" id="cCallScript" src="http://$callCenter.get("ccIP")/mixinterface/interface.js">
</script>
<script type="text/javascript">
var extension="$!LoginBean.extension";
var callCenterStart = false;
var hadConnect=false;
var curId='';
var curTels;
var curPos;
var hadResult;//接口总是会多次调用，用这个变量来处理多次调用的问题


var isstop=false;
function callCenter(){
	if(extension == ""){
		alert("请设置您的分机号");
		return;
	}
	if(callCenterStart){
		$("#ccenterPanel").hide();
		callCenterStart = false;
		document.getElementById("ccBt").value="启动自动呼叫";
		isstop = true;
		//保存呼叫结果
		endTime = new Date();
		if(curId != ""){			
			 jQuery.ajax({
				 url: "/ClientServiceAction.do?type=ccallSave&state=1&startTime="+startTime.getTime()+"&endTime="+endTime.getTime()+"&tel="+curTels[curPos-1]+"&id="+curId,
				 async: false   
				});	
			curId = "";	
		}
	}else{		
		$("#ccenterPanel").show();
		isstop = false;
		callCenterStart = true;
		document.getElementById("ccBt").value="停止自动呼叫";		
		if(!hadConnect){
			debug('正在加载呼叫中心，请稍候');
			mixinterface();
			hadConnect = true;
		}else{		
			doCall();
		}		
	}
}

function mixOnInit(){
	debug('呼叫中心加载完毕');
	
	mixConnect('$callCenter.get("ccIP")','$callCenter.get("ccPort")');
	
}
function mixOnConnectError(){
	debug('连接失败，请检查IP:$callCenter.get("ccIP"),端口：$callCenter.get("ccPort")配置是否正确');
	alert("呼叫中心连接失败，请检查IP:$callCenter.get("ccIP"),端口：$callCenter.get("ccPort")配置是否正确");
}
function mixOnConnect(){  	
	debug('呼叫中心连接完毕');	
	doCall();
}

function mixOnDial(str){	
	ss = parseStr(str,"Status");	
	if(ss != "0"){
		debug('呼叫:'+curTels[curPos-1]+'失败'+ss+",请检查后重新呼叫");
		hadResult = true;
		
	}
}
function mixOnHangupCus(str){
	if(!hadResult){
		hadResult = true;
		ss = parseStr(str,"Cause");		
		if(isRecall){
			//重新呼叫,本地呼叫
			
			//启动时钟，5秒后重拨			
			startCTimer('dail("'+curTels[curPos-1]+'")',4);
			isRecall = false;
			isLocalcall = false;
			debug('继续呼叫:'+curTels[curPos-1]);			
		}else if(isLocalcall){
			//重新呼叫,本地呼叫
			
			//启动时钟，5秒后重拨			
			startCTimer('dail("'+curTels[curPos-1]+'",true)',4);
			isRecall = false;
			isLocalcall = false;
			debug('继续呼叫:'+curTels[curPos-1]);			
		}else if(ss == "777"){
			//启动时钟，5秒后重拨
			startCTimer('dail("'+curTels[curPos-1]+'")',5);
			debug('继续呼叫:'+curTels[curPos-1]);
		}else{
			debug('呼叫:'+curTels[curPos-1]+'结束，'+ss);
			//如果该客户还有电话，则显示，选择界面
			if(curPos < curTels.length){
				$("#nextSelected").show();
				startCTimer('doCall()',10);						
			}else{
				//保存客户信息
				saveCallInfo();
				//发起下一呼叫
				startCTimer('doCall()',5);				
			}
		}
	}	
}

function doCallNext(){
	$("#nextSelected").hide();
	$("#paseBt").hide();
	stopCTimer();
	dail(curTels[curPos]);				
	debug('正在呼叫:'+curTels[curPos]);
	curPos++;
}
//暂停，只是将计时时钟改为一分钟
function doPase(){
	timerNum = 60;
}
function doContinue(){
	timerNum = 0;
}

function saveCallInfo(){
	//alert("save");
}

var timerFunction;
var timerNum;
function startCTimer(func,time){
	$("#paseBt").show();
	timerFunction = func;
	timerNum = time;
	doCTime();
}
function stopCTimer(){
	timerFunction='';
	timerNum = 0;	
}
function doCTime(){
	if(isstop){
		document.getElementById("timeSpan").innerHTML= '';
		timerFunction='';
		$("#paseBt").hide();
		return;
	}
	if(timerNum>0){		
		document.getElementById("timeSpan").innerHTML= ''+timerNum;
		timerNum--;
		setTimeout('doCTime()',1000);
	}else{
		document.getElementById("timeSpan").innerHTML= '';
		eval(timerFunction);
		timerFunction='';
		$("#paseBt").hide();
	}
}

var curPopTel = "";
var localeMobile=false;
function dail(tel,local){	
	hadResult = false;
	ctel = tel; 
	
	if(!local && ctel.substr(0,1)=="1"){
		ctel = "0"+ctel;
	}
	mixDial(extension, '$callCenter.get("ccPrex")'+ctel, extension+tel);	
	
	if(curPopTel != tel){
		curPopTel = tel;
		var varCenter=$d("pindex");
		varCenter.contentWindow.leftFrame.$("keyWord").value=curPopTel;
		varCenter.contentWindow.leftFrame.queryClient();
	}		
}

function mixOnResult(str){
}
function mixOnHangup(str){
}

function parseStr(str,key){
	pos = str.indexOf(key)+key.length+2;
	ss = str.substring(pos,str.indexOf("\r",pos));	
	return ss;
}
var Uniqueid;
var startTime;
function doCall(){		
	$("#nextSelected").hide();
	$("#paseBt").hide();
	
	if(curId !=''){
		//保存呼叫结果
		endTime = new Date();
		 jQuery.ajax({
			 url: "/ClientServiceAction.do?type=ccallSave&state=1&startTime="+startTime.getTime()+"&endTime="+endTime.getTime()+"&tel="+curTels[curPos-1]+"&id="+curId,
			 async: false   
			});			
	}
	startTime = new Date();
	
	if(isstop){
		return;
	}
	//取呼叫的号码
	jQuery.get("/ClientServiceAction.do?type=ccCRMTel",function(data){		
		//如果呼叫号码为空则延时再执行
		if(data == null || data == "NODATA"){			
			debug('没有呼叫任务');
			setTimeout('doCall()',120000);
		}else{
			ss = data.split(":");
			curId = ss[0];
			tel = ss[1];			
			curTels = tel.split(";");
			curPos = 0;
						
			dail(curTels[curPos]);				
			debug('正在呼叫:'+curTels[curPos]);
			curPos++;
		}
	});
}

function doHug(){
	mixHangup(extension,extension+tel);
}
var isRecall=false;
function doRecall(){
	isRecall = true;
	mixHangup(extension,extension+tel);
}
var isLocalcall=false;
function doLocalcall(){
	isLocalcall = true;
	mixHangup(extension,extension+tel);
}
function debug(Str){
	document.getElementById("interfaceStatus").innerHTML= '<font color="green"><b>'+Str+'</b></font>';
}
</script>

#end
</head>

<body onLoad="setHeight();">
<div style="float: left;width: 100%;">




<div>
<div id="pindexId" class="listRange_1_space_1_div_a" onClick="showDisplay('pindex')">$text.get("crm.service.auditorium")</div>
<div id="detailId" class="listRange_1_space_1_div_b" onClick="showDisplay('detail')">$text.get("common.lb.clientDetail")</div>
#if($callCenter.get("ccState") == "0"  && $globals.isSameNet($callCenter.get("ccIP")) && "$!ClientServiceKeyWord" == "")  
<div id="Autocall" style="padding:0 0 0 320px;margin:0px;vertical-align:top;">
<button style="color:red;margin:0px 0px 0px 0px;padding:0px" onClick="callCenter()" id="ccBt">启用自动呼叫</button>	
</div>
<div class="tips_div" id="ccenterPanel" style="display:none;  cursor:default; position:absolute; left:400px; top:0px;">
   	<div class="tips_content">
		<span id=interfaceStatus style="height:25px;margin:0px;padding:0px;float:left">
	
		</span>
		<span id="timeSpan" style="float:right;width:50px;height25px;color:red;font-size:18px;font-weight:800"></span>
	</div>
   	<div class="tips_operate">
   	<div id="n" style="margin:4px;float:left;height:20px">
		<button type="button" onclick="doHug()"  style="width:50px;margin:2px 4px 2px 4px;">挂断</button>
		<button type="button" onclick="doRecall()"  style="width:50px;margin:2px 4px 2px 4px;">重新呼叫</button>
		<button type="button" onclick="doLocalcall()"  style="width:50px;margin:2px 4px 2px 4px;">本地手机号码</button>
	</div>
   	<div id="paseBt" style="display:none;margin:4px;float:left;height:20px">
		<button type="button" onclick="doPase()"  style="width:50px;margin:2px 4px 2px 4px;">暂停</button>
		<button type="button" onclick="doContinue()"  style="width:50px;margin:2px 4px 2px 4px;">继续</button>
	</div>
	<div id="nextSelected" style="display:none;margin:4px;float:right">
	是否继续呼叫本客户其它号码?
	<button type="button" onclick="doCallNext()"  style="width:35px;margin-right:5px">是</button>
	<button type="button" onclick="doCall()"  style="width:35px;margin-right:20px;border:1px dotted  #666666">否</button>
	</div>
   	</div>
</div>
#end
</div>
<div style="float: left;width: 100%; height:auto;background:#ffffff;">
  <iframe style="display: block;" id="pindex"  src="/ClientServiceAction.do?type=bottom" height="600px" width="100%"></iframe>
  <iframe style="display: none;" id="detail" src="" width="100%"></iframe>
</div>
</div>
</body>
</html>