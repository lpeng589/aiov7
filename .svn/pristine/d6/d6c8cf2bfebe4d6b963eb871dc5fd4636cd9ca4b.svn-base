var requiredMajorVersion = 9;
var requiredMinorVersion = 0;
var requiredRevision = 124;
var hasProductInstall = DetectFlashVer(6, 0, 65);
var hasRequestedVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);

function writeFunctionListSWF(){
	if ( hasProductInstall && !hasRequestedVersion ) {
		var MMPlayerType = (isIE == true) ? "ActiveX" : "PlugIn";
		var MMredirectURL = window.location;
	    document.title = document.title.slice(0, 47) + " - Flash Player Installation";
	    var MMdoctitle = document.title;
	
		AC_FL_RunContent(
			"src", "/flash/playerProductInstall",
			"FlashVars", "MMredirectURL="+MMredirectURL+'&MMplayerType='+MMPlayerType+'&MMdoctitle='+MMdoctitle+"",
			"codebase","/swflash.cab",
			"width", "100%",
			"height", "100%",
			"align", "middle",
			"id", "functionListObj",
			"quality", "high",
			"bgcolor", "#ffffff",
			"name", "test",
			"wmode","transparent",
			"allowScriptAccess","sameDomain",
			"type", "application/x-shockwave-flash",
			"pluginspage", "http://www.adobe.com/go/getflashplayer"
		);
	} else {
		AC_FL_RunContent(
				"src", "/flash/FunctionList",
				"codebase","/swflash.cab",
				"width", "100%",
				"height", "100%",
				"align", "middle",
				"id", "functionListObj",
				"quality", "high",
				"bgcolor", "#ffffff",
				"name", "test",
				"wmode","transparent",
				"allowScriptAccess","sameDomain",
				"type", "application/x-shockwave-flash",
				"pluginspage", "http://www.adobe.com/go/getflashplayer"
		);
	  }
}

function onSize(width,height){
		var oDiv=document.getElementById("conter");
		var sh = oDiv.style.height;
		var oDivHeight = Number(sh.substr(0,sh.length-2));
		var oDivWidth = document.body.clientWidth;
		obj = document.getElementById("functionListObj");
		if(height < oDivHeight-20){
			obj.height=height;
		}else{
			obj.height="100%";
		}
		if(width<oDivWidth-20){
			obj.width=parseInt(width)+20;
		}else{
			obj.width="100%";
		}
}