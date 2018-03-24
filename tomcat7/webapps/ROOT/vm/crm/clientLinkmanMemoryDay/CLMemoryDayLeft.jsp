<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("crm.client.attention.path")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/date.vjs"></script>
<script language="javascript">
function goFarme(username){
	var varURL = "/ClientLinkmanMemoryDayAction.do?type=main&operation=4" ;
	if(username ==null)
		window.parent.mainFrame.location=varURL;
	else
		window.parent.mainFrame.location=varURL+'&firstName='+encodeURI(username);
}

</script>
</head>

<body onLoad="">
<form method="post" name="form" action="/ClientLinkmanMemoryDayAction.do">
 <div class="aoHeadingFrametop">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="oaHeadingTitle">$text.get('crm.bjx')</div>
</div>
<div class="aomainall" id="conter">
<script type="text/javascript"> 
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-38;
oDiv.style.height=sHeight+"px";
</script>
<ul class="oamailleft_list1">
	<a href="javascript:goFarme()">
	<span><img src="/$globals.getStylePath()/images/mailwrite.gif"/></span>$text.get('property.choose.item1')$text.get("crm.bjx")</a>
	<a href='/ClientLinkmanMemoryDayAction.do?type=left&operation=4&tidy=tidy'>$text.get('crm.tidy.up')</a>
</ul>
<div>
	#foreach($key in $valueMap.keySet())
		#set($index=1)
		<span>$key</span>
		#foreach($firstName in $valueMap.get($key))
			<a href="javascript:goFarme('$firstName')">$firstName</a>
			#if($index%7==0)
				<br>&nbsp;&nbsp;&nbsp;
			#end
			#set($index = $index+1)
		#end
		<br>
	#end
</div>
		</div>
		</div>
		</div>
	</form>
</body>
</html>

