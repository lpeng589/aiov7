<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("user.lb.modifyPass")</title>
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

<form  method="post" scope="request" name="form" action="/NoteAction.do?operator=modifyPass&type=update">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
 <input name="url" id="url"  type="hidden" value="$!gouId" >
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("oa.note.change.password")</div>
</div>
<div id="listRange_id" align="center">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
	<div class="listRange_div_jag">
		<div class="listRange_div_jag_div"><span>$text.get("oa.note.change.password")</span></div>
			<ul class="listRange_jag">
	    <li><span>$text.get("sms.modpass.newpass")：</span>
	      <input name="newPassword" id="newPassword" type="text"></li>	
		
	<li style="margin-top:10px; text-align:center; margin-bottom:10px;">
	
	<button type="button" onClick="modify()">$text.get("mrp.lb.save")</button>
	
	<button type="reset">$text.get("sms.addfunc.reset")</button>
	</li>
			</ul>
	</div>
</div>
</form> 
</body>
</html>
