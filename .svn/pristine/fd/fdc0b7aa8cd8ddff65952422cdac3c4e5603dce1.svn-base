var requiredMajorVersion = 9;
var requiredMinorVersion = 0;
var requiredRevision = 124;
var hasProductInstall = DetectFlashVer(6, 0, 65);
var hasRequestedVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);

function writeSWF(){
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
				"src", "/flash/clock",
				"codebase","/swflash.cab",
				"width", "210",
				"height", "200",
				"align", "middle",
				"id", "myComplete",
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
