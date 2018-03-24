<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript">
﻿if(typeof(top.junblockUI)!="undefined"){
	top.junblockUI();
}
function testHttp(){
	var httpURL = document.getElementById("linkAddr").value ;
  	if(httpURL.length==0){
  		alert("$text.get("alertCenter.lb.AIOSHOPAddressNotNull")") ;
  		return false ;
  	}
  	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
  	form.submit() ;
}

</script>
</head>

<body>
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/AIOShopAction.do" target="formFrame">
<input type="hidden" name="operation" value="1"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("sms.title.set")</div>
</div>
<div id="listRange_id" align="center">
	<div class="listRange_div_jag">
		<div class="listRange_div_jag_div"><span>$text.get("sms.title.set")</span></div>
			<ul class="listRange_jag">
				<li><span>$text.get("alertCenter.lb.AIOSHOPAddress")</span>	    
	    			<input name="linkAddr" id="linkAddr" type="text"  value="#if("$!linkAddr"=="")http://#else $!linkAddr#end">&nbsp;</li>
				<li style="margin-top:10px; text-align:center; margin-bottom:10px;">
					<button type="button" onClick="return testHttp();"> #if("$flag" == "0") $text.get("StopValue") #else $text.get("OpenValue") #end</button>
				</li>
			</ul>
		</div>	
</div>
</form> 
</body>
</html>
