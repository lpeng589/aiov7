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
#set($pic = $request.getParameter("pic"))
#set($tableName = $request.getParameter("tableName"))
#foreach($uprow in $pic.split(";"))
#if($uprow.indexOf("http:") > -1)					
<img src="$uprow"/>
#elseif($uprow.indexOf(":") > -1)		
<img src="/ReadFile.jpg?fileName=$globals.urlEncode($!globals.get($uprow.split(":"),0))&realName=$globals.urlEncode($!globals.get($uprow.split(":"),1))&tempFile=false&type=PIC&tableName=$tableName"/>			
#else
<img src="/ReadFile.jpg?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=PIC&tableName=$tableName">
#end
#end

</body>
</html>