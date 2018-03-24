<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
<meta name="renderer" content="webkit">
<title>My JSP 'phoneScreen.jsp' starting page</title>
<link type="text/css" rel="stylesheet" href="/style/css/brother_detail.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<!--OBJECT ID="UsbPhone" CLASSID="CLSID:4CF8112F-57D5-474B-A4BC-70F72244BCD9"  width="0" height="0" codebase= "UsbPhone.ocx"></OBJECT-->  
	<OBJECT ID="UsbPhoneCtrl" CLASSID="CLSID:012D72C6-2491-405A-BA6E-BCC1F4807FFA"  width="0" height="0" codebase= "UsbPhoneCtrl.ocx"></OBJECT>  

	<script language="JavaScript" for="UsbPhoneCtrl" event="OnComingCall(date, callNumber, callName)" defer>
		// document.getElementById("ttt").innerHTML = "1052925814";
		//alert("header  sttime=="+document.getElementById("stTime").innerHTML);
		//alert("Call Time=="+date);
	
		var phone = callNumber;
		//addLog("OnComingCall(date, callNumber, callName)... ");
		jQuery.ajax({
			type: "POST",
			url: "/CRMClientAction.do",
			data: "operation=4&type=phoneScreen&CallNumber="+callNumber,
			success: function(msg){
				
				if(msg == "error"){
					alert("查询错误")
				}else if(msg == "empty"){
					
					$("#phoneScreen").show();
					$("#callNumber").text(phone)
					
					$("#newInfo").show();
					$("#addClietBtn").show();
	
					$("#showInfo").hide();
					$("#seeDetailBtn").hide();
				}else{
					var values = msg.split(":");
					
					$("#userName").text(values[0]);
					$("#gender").text(values[1]);
					$("#role").text(values[2]);
					$("#clientName").text(values[3]);
					$("#distinct").text(values[4]);
					
					
					$("#seeDetailBtn").attr("clientName",values[3]);
					$("#seeDetailBtn").attr("clientId",values[5]);
	
	
					$("#newInfo").hide();
					$("#addClietBtn").hide();
	
					$("#showInfo").show();
					$("#seeDetailBtn").show();
					
					
				}
				$("#phoneScreen").show();
			}
		});
	</script>

	<script language="JavaScript" for="UsbPhoneCtrl" event="UsbPhoneKeyPress(key)" defer>
		//alert("Press Key ..."+key);
		addLog("UsbPhoneKeyPress(key)... key="+key);
	</script>

	<script language="JavaScript" for="UsbPhoneCtrl" event="UsbPhoneEvent(EventX,Param)" defer>
		//alert("Param ..."+Param);
		addLog('UsbPhoneEvent(EventX,Param)... EventX='+EventX+' ,Param=' +Param);
	</script>

	<script type = "text/javascript" for="UsbPhoneCtrl" event="UsbPlug(IsPlugIn)" defer>
		//alert("IsPlugIn ..."+IsPlugIn);
		addLog("UsbPlug(IsPlugIn)... IsPlugIn="+IsPlugIn);
	</script>

	<script type = "text/javascript" for="UsbPhoneCtrl" event="OnRing()" defer>
		//alert("OnRing ...");
		addLog("OnRing() ...");
	</script>

	<script language=JavaScript type="text/JavaScript">
		var UsbPhoneCtrl1 = document.getElementById("UsbPhoneCtrl");
		for (var i in UsbPhoneCtrl1) {
		//document.write(i);
		//document.write("<br>");
		}
	</script>
		
	<script type = "text/javascript" for= "window" event= "onresize"> 
		//alert( "Resize the window... "); 
		addLog('Resize the window... ');
	</script>  
	
	<script>
	var usbPlugStatus;
	var enableWatchDog;
	
	function keyevent(event){
		//alert('event= '+event.keyCode);
		if(event.keyCode==116){
			beforelogout();
		}
		//pageframe.window.keyevent(event);
	}
	
	
	function beforelogout(){			

	}
	
	String.prototype.trim = function(){
         // ʽǰո
         // ÿַ
         return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	
	window.onbeforeunload = function(){        
		//	alert(1);
		beforelogout();
		// 	alert('onbeforeunload CTI='+iRet);
	}

	function GetPhoneStatus(){			
		var result=UsbPhoneCtrl.GetPhoneStatus();
		//alert('GetPhoneStatus...'+result);
		addLog('GetPhoneStatus()... ===>result='+result);
	}

	function GetUsbPhoneHwVersion(){			
		var result=UsbPhoneCtrl.GetUsbPhoneHwVersion();
		//alert('GetUsbPhoneHwVersion...'+result);
		addLog('GetUsbPhoneHwVersion()... ===>result='+result);
	}

	function DialNumber(){	
		var extension=callForm.extension.value;
		var dn=UsbPhoneCtrl.DialNumber(extension,extension.length);
		//alert('DialNumber...'+dn);
		addLog('DialNumber(szDialNumber, DialLength) szDialNumber='+extension+' ,DialLength='+extension.length +' ==>result='+dn);
	}

	function DirectDialout(){	
		var telphone=callForm.telphone.value;
		UsbPhoneCtrl.DirectDialout(telphone);
		//alert('DirectDialout...');
		addLog('DirectDialout(telphone)...telphone='+telphone);
	}

	function HandFreeOn(){			
		//alert('HandFreeOn ......');
		UsbPhoneCtrl.HandFreeOn();
		addLog('HandFreeOn()...');
	}
	
	function HandFreeOff(){			
		//alert('HandFreeOff ......');
		UsbPhoneCtrl.HandFreeOff();
		addLog('HandFreeOff()...');
	}
	
	function LineBusyDetect(){			
		//alert('LineBusyDetect ......');
		UsbPhoneCtrl.LineBusyDetect();
		addLog('LineBusyDetect()...');
	}

	function TestLog(){			
		//alert('UsbPhoneCtrl......'+UsbPhoneCtrl.CLASSID);
		//alert('TestLog......'+callForm.telphone.value);
		//UsbPhoneCtrl.OnRing();
		addLog('TestLog...... telphone='+callForm.telphone.value);
	}
	
	function GetUsbPlugStatus(){	
		usbPlugStatus=UsbPhoneCtrl.UsbPlugStatus;
		addLog('UsbPlugStatus ===>'+usbPlugStatus);
	}

	function GetEnableWatchDog(){	
		enableWatchDog=UsbPhoneCtrl.EnableWatchDog;
		addLog('EnableWatchDog ===>'+enableWatchDog);
	}
	
	function ClearLog(){			
		callForm.logArea.value='';
	}

	function addLog(log){			
		callForm.logArea.value +=new Date().toLocaleString();
		callForm.logArea.value +='      ';
		callForm.logArea.value +=log;
		callForm.logArea.value +='\r\n';
	}

	if(UsbPhoneCtrl.readyState==4){  
		usbPlugStatus=UsbPhoneCtrl.UsbPlugStatus;
		enableWatchDog=UsbPhoneCtrl.EnableWatchDog;
		//alert("yes");
		alert("success");  
	}else{  
		alert("加载失败,本控制只支持IE");  
	}  
	</script>
<script type="text/javascript">
	$(function(){
		$('#seeDetailBtn').bind('click', function() {
			var clientName = $(this).attr("clientName");
			var clientId = $(this).attr("clientId");
			top.mdiwin('/CRMClientAction.do?operation=5&type=detailNew&keyId='+clientId,clientName);
		});

	})
	
	function clientDeatil(clientId,clientName){
		top.mdiwin('/CRMClientAction.do?operation=5&type=detailNew&keyId='+clientId,clientName);
	}
	
	function addClient(){
	
		var height = 540;
		var winHeight = $("body").height();
		if(winHeight<height){
			height = winHeight;
		}
		var url = "/CRMClientAction.do?operation=6&moduleId=1";
		asyncbox.open({
			id:'dealdiv',url:url,title:'添加客户',width:840,height:500,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
		    callback : function(action,opener){
			    if(action == 'ok'){
			    	opener.beforeSubmit();
					return false;
				}
	　　  　 }
	　  });
	}
	
	function crmOpDeal(){
		asyncbox.tips('添加成功!','success');
	    jQuery.close("dealdiv"); 
	}
	
</script>

</head>

<body class="body">

<div id="contents" style="display:none">
  		
</div>
<div class="phoneScreen" style="display:none;" id="phoneScreen">
	<div class="topC">
		<b class="icon64"></b>
		<div class="tellDetail" id="showInfo" style="display:none;" >
			<p>
				<i class="tellName" id="userName"></i><i class="tellSex" id="gender"></i><i class="tellJob" id="role"></i>
			</p>
			<span id="clientName"></span>
			<span id="distinct"></span>
		</div>
		<div class="unKnow" style="display:none;" id="newInfo">
			<p class="message">
				陌生号码来电中...
			</p>
			<p class="tellNum" id="callNumber">
				
			</p>
		</div>
	</div>
	<div class="bottomBtn">
		<span class="seeDetailBtn" id="seeDetailBtn" style="display:none;"><b class="icon16"></b><em>查看详情</em></span>
		<span class="AddClietBtn" id="addClietBtn" style="display:none;"><b class="icon16"></b><em onClick="addClient()">新建客户</em></span>
	</div>
</div>
</body>
</html>
