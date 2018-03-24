<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("sms.balance.search")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript">
function modify()
{
	form.submit();
}
</script>
</head>

<body>

<form  method="post" scope="request" name="form" action="/NoteAction.do">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("sms.balance.search")</div>
</div>
<div id="listRange_id" align="center">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
<div class="listRange_div_jag_hint" >
		<div class="listRange_div_jag_div"><span>$text.get("sms.getbalance.systip")</span></div>
		#if("$!noOpen"=="true")
		<div class="listRange_Prongix">
					<li style="width:64px;height:64px;background:url(/style/images/allbg.gif) no-repeat -121px -65px;"></li>
					<li><span>$text.get("note.alreadydisabled")</span></li>
		</div>
		<div style=" padding-bottom:15px;">
			 <button name="button" type="button" onClick="location.href='/SMSSetAction.do'">$text.get("sms.getbalance.opennote")</button>
		</div>
		#else
		<div>
			<ul class="listRange_Prongix"> 
			
				#if("$!msg" != "") 
				<li style="width:64px;height:64px;background:url(/style/images/allbg.gif) no-repeat -121px -65px;"></li>
				<li style="text-align:right;"><span>$!msg</span></li>
				#else 
				<li style="width:64px;height:64px;background:url(/style/images/allbg.gif) no-repeat -121px 0;"></li>
				<li style="text-align:left;"><span>$!balance </span>
				</li>
				#end
			</ul>
		</div>
		#end
	</div>
</div>
</form> 
</body>
</html>
