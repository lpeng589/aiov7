
try{
objStr = '<object id="UsbPhoneBox" classid="CLSID:9B5942A6-FE19-49F2-AFB3-97DB09FC20E6" height="0" width="0"><param name="EnableWatchDog" value="1"></object>\
	<SCRIPT LANGUAGE="JavaScript" FOR=UsbPhoneBox EVENT=OnComingCall(DevId,CallTime,CallNumber,CallName)> ComingCallEvent(DevId, CallTime,CallNumber,CallName)</SCRIPT>';
document.write(objStr);
}catch (e){
	alert('');
}

function ComingCallEvent(DevId,CallTime,CallNumber,CallName)
{
	//alert("\r\n[设备" +DevId + "] ："　 + CallNumber+ " [" + CallTime+ "]" + CallName);
	//有些设备过来的电话号码前有9字，所以这里判断，如果第一位是非0和非1，则去掉，第一位
	if(CallNumber.substr(0,1)!="0" && CallNumber.substr(0,1)!="1" ){
		CallNumber = CallNumber.substr(1);
	}
	
	jQuery.ajax({
		type: "POST",
		url: "/CRMClientAction.do",
		data: "operation=4&type=phoneScreen&CallNumber="+CallNumber,
		success: function(msg){
			if(msg == "error"){
				alert("查询错误")
			}else if(msg == "empty"){
				
				$("#callNumber").text(CallNumber)
				
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
	
	//showreModule("坐席台","/ClientServiceAction.do?keyWord="+CallNumber);
	//var openw =window.open("/ClientServiceAction.do?keyWord="+CallNumber,"_blank","fullscreen");
}

function phoneBoxUnload(){
	UsbPhoneBox.Unload();
}

if (window.addEventListener){
	window.addEventListener('unload', phoneBoxUnload, false);	
	//UsbPhoneBox.addEventListener('ComingCall', ComingCallEvent, false);
} else if (window.attachEvent){
	window.attachEvent('onunload', phoneBoxUnload);
	//UsbPhoneBox.attachEvent('OnComingCall', ComingCallEvent);
}