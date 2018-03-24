<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("message.lb.pageTitle")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript">

</script>

</head>

<body onLoad="showStatus();">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/MessageAction.do"  target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_REVERT_PREPARE")">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("message.lb.pageTitle")</div>
	<ul class="HeadingButton">
		 	 #if($result.getReceive()==$loginId && "$!noReplay"=="")
			   <li><button type="button" onClick="location.href='/MessageAction.do?operation=$globals.getOP("OP_REVERT_PREPARE")&keyId=$result.getId()&winCurIndex=$!winCurIndex'" class="b2">$text.get("message.lb.revert")</button></li>
			  #end
			  #if("$!desktop"=="yes")
			<li><button type="button" onClick="history.go(-1)" class="b2">$text.get("common.lb.back")</button></li>
			  #else
			  	 <li><button type="button"  onClick="location.href='/MessageQueryAction.do?operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button></li>
			  #end
			   
	</ul>
</div>
<div id="listRange_id">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
	<div class="listRange_div_msg">
		<div class="listRange_div_msg_div"></div>
			<ul>
			  
			  <li class="msgbg"><span>$text.get("messsage.lb.sender")：</span>
			  $result.getSendName()</li>
			  <li ><span>$text.get("message.lb.createTime")：</span>
			   $result.getCreateTime()
			  </li>
			  <li class="msgbg"><span>$text.get("message.lb.content")：</span>
			   $result.getContent()
			  </li>	
			  <li><span>$text.get("upload.lb.affix")：</span>
<ul id="affixuploadul">
#foreach($uprow in $!affix)	
#if($uprow.indexOf(":") > -1)			
<li style='background:url();' id="uploadfile_$!globals.get($uprow.split(":"),0)">
<input type=hidden name=uploadaffix value='$uprow'>$!globals.get($uprow.split(":"),1)	
&nbsp;&nbsp;&nbsp;&nbsp;<a href="/ReadFile?fileName=$globals.urlEncode($!globals.get($uprow.split(":"),0))&realName=$globals.urlEncode($!globals.get($uprow.split(":"),1))&tempFile=false&type=AFFIX&tableName=workFlowDesign" target="_blank">$text.get("common.lb.download")</a>
</li>
#else
<li style='background:url();' id="uploadfile_$uprow">
<input type=hidden name=uploadaffix value='$uprow'>$uprow	
&nbsp;&nbsp;&nbsp;&nbsp;<a href="/ReadFile?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=AFFIX&tableName=workFlowDesign" target="_blank">$text.get("common.lb.download")</a>
</li>
#end
#end
</li>
</ul>
	</div>
</div>
</form> 
</body>
</html>
