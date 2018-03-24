<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>文件预览</title>
	<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
	<script language="javascript" src="/js/jquery.js"></script>
	<script language="javascript">
	function maximize_window(){ 
		var window_width=screen.availWidth-20; 
		var window_height=screen.availHeight-20; 
		self.moveTo(0,0) 
		self.resizeTo(window_width,window_height) ;
	} 
	</script>
</head>
  
<body onload="maximize_window();">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">文件预览</div>
	<ul class="HeadingButton">
		<li><button  type="button" onClick="window.close();">关闭</button></li>
	</ul>
</div>
<div id="listRange_id" style="overflow-x:auto;">
    <div class="listRange_1">
    <div style="width:70%;margin:0px auto;">
    	#if("$!ext"=="pdf")
    	<embed width="100%" height="98%" id="plugin" name="plugin" src="/ReadFile?path=$!path&view=data" type="application/pdf">
    	#elseif("$!ext"=="images")
    	<img src="/ReadFile?path=$!path&view=data" border="0">
    	#elseif("$!ext"=="office")
    	<iframe src="/temp/$!{p}.html" frameborder="no" id="pcontent" name="pcontent" style="width:100%;" onload="this.height=pcontent.document.body.scrollHeight"></iframe>
    	#else
    	$!html
    	#end
    </div>
	</div>
</div>
<script language="javascript">
$("#listRange_id").height($(window).height()-32);
#if("$!ext"=="office")
$("#pcontent").height($(window).height()-52);
#end
</script>
</body>
</html>
