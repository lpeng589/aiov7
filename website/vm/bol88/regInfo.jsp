<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("unitadmin.title.add")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript">

putValidateItem("userName","$text.get("bol88.e.account")","any","",false,0,10);
putValidateItem("password","$text.get("bol88.e.password")","any","",false,1,99 );

function beforSubmit(form){   
	if(!validate(form))return false;
	disableForm(form);
	return true;
}	
</script>
<style>
.listRange_woke button {height:20px; line-height:20px; overflow:hidden; margin:0px; padding:0px; margin-right:5px;}
</style>
</head>

<body onLoad="showStatus();">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/AIOBOL88Action.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")">
<input type="hidden" name="winCurIndex" value="$winCurIndex">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("bol88.e.account.binding")</div>
	<ul class="HeadingButton">
	
	</ul>
</div>





<div id="listRange_id" align="center">
			<script type="text/javascript">
				var oDiv=document.getElementById("listRange_id");
				var sHeight=document.body.clientHeight-38;
				oDiv.style.height=sHeight+"px";
			</script>
	<div class="scroll_function_small_a">
				<div class="listRange_div_jag_hint" align="center">
					<div class="listRange_div_jag_div"></div>					
						<ul class="listRange_woke">
							<li>
								<span>$text.get("bol88.e.account")：</span>
								<input name="userName" type="text" style="width:150px" class="text" value="$!result.userName" maxlength="50">
							</li>
							<li>
								<span>$text.get("bol88.e.password")：</span>
								<input name="password" type="password"  style="width:150px" class="text" maxlength="50">
							</li>
							<li style="text-align:center"><a href="$globals.getBol88()/MemberAction.do?operation=6" target="_blank">$text.get("bol88.e.no.account") $text.get("bol88.e.binding.flag10")</a></li>
							<li style="text-align:center">
							<button type="submit" class="b2" style="width:50px">$text.get("common.lb.ok")</button>
							<button type="button"  onClick="location.href='/AIOBOL88Action.do'" class="b2" style="width:50px">$text.get("common.lb.back")</button></li>
						</ul>
				</div>																		
	</div>			

</div>

</form> 
</body>
</html>
