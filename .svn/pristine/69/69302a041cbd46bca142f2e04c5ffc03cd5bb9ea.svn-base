<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("sms.title.addfunc")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript">
function addFund()
{
   var carNo=document.getElementById("cardNo").value;
   var password=document.getElementById("password").value;
   if(carNo==""||password=="")
   {
     alert("$text.get('sms.addfunc.info')");return false;
   }
	form.submit();
}
</script>
</head>

<body>

<form  method="post" scope="request" name="form" action="/NoteAction.do?operator=addFund&type=add">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("sms.title.addfunc")</div>
</div>
<div id="listRange_id" align="center">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
	<div class="listRange_div_jag">
		<div class="listRange_div_jag_div"></div>
			<ul class="listRange_jag">
		<li><span>$text.get("sms.gou.num")：</span>
	      <input name="gouId" id="gouId"  type="text" value="$!gouId" readonly style=" color:#CCCCCC"></li>
	    <li><span>$text.get("sms.addfunc.cardno")：</span>
	      <input name="cardNo" id="cardNo" type="text"></li>	
		<li><span>$text.get("sms.addfunc.cardpass")：</span>
	      <input name="password" id="password" type="password"></li>	
		
	<li style="margin-top:10px; text-align:center; margin-bottom:10px;">
	
	<button type="button" onClick="addFund()">$text.get("mrp.lb.save")</button>
	
	<button type="reset">$text.get("sms.addfunc.reset")</button>
	</li>
			</ul>
	</div>
</div>
</form> 
</body>
</html>
