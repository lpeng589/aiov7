<html>
<head>
<meta name="renderer" content="webkit">
<script   language= "javascript"> 
function maximize_window(){ 
var   window_width=screen.availWidth-20; 
var   window_height=screen.availHeight-20; 
self.moveTo(0,0) 
self.resizeTo(window_width,window_height) 
} 
</script>
</head>
<body onload="maximize_window()">

#set($cd ="false")
#if($LoginBean.id == "1" || $!onDown=="true")
#set($cd ="true")
#end
<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
		id="readImage" width="100%" height="95%"
		codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
		<param name="movie" value="/flash/readImage.swf" />
		<param name="quality" value="high" />
		<param name="bgcolor" value="#869ca7" />
		<param name="flashvars" value="url=/ReadFile&path=$path&fileName=$globals.encode($fileName)&cd=$cd" />
		<param name="allowScriptAccess" value="sameDomain" />
		<embed src="/flash/readImage.swf" quality="high" bgcolor="#869ca7"
			width="100%" height="95%" name="readImage" align="middle" 
			play="true"
			loop="false"
			flashvars="url=/ReadFile&path=$path&fileName=$globals.encode($fileName)&cd=$cd"
			quality="high" 
			allowScriptAccess="sameDomain"
			type="application/x-shockwave-flash"
			pluginspage="http://www.adobe.com/go/getflashplayer">
		</embed>
</object>
</body>
</html>