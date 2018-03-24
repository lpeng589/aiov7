<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("bol88.e.function")</title>
<link rel="stylesheet" href="/style/css/about.css" type="text/css">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">

<script language="javascript">
function submitAIO(){    
    document.form.submit();
}

function startPool(){
	#if("$FEE" == "0")
		window.location.href="/AIOBOL88EMailAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&type=keyword&winCurIndex=$winCurIndex";	
	#elseif("$FEE" == "1")
		alert('$text.get("common.msg.nopay")');
	#elseif("$FEE" == "2")
		alert('$text.get("common.msg.payOverdue")');
	#else
		alert('$text.get("common.msg.bindElectronCommerce")');
	#end
	
}
</script>
<style>
#bodyUl button {height:20px; line-height:20px; overflow:hidden; margin:0px; padding:0px;}
</style>
</head>

<body >
<form  method="post" scope="request" id="form" name="form" action="/AIOBOL88Action.do">
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")">
<input type="hidden" name="aioClose" value="0">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">
		$text.get("bol88.mailInfo")

	</div>
	<ul class="HeadingButton">
		<li></li>
	</ul>
</div>
<div id="listRange_id"  align="center">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
	<div style="margin-top:15px">
<div id="contentleft">
		    <div id="contentright">
		    <div id="contentMain"> 
<div id="Content">
    <div id=pageTitle>$text.get("bol88.mailInfo") 
    <div>
	</div></div>
	<div id="bodyUl">
		<ul >	
			<li >
				<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$text.get("bol88.advertisement")</span>							
			</li>
			<li >
					$text.get("bol88.mailInfo.currentlyState")： #if("$!MailPoolForm" == "" || "$!MailPoolForm.status" =="0") $text.get("bol88.mailInfo.NoStartup") #else $text.get("bol88.mailInfo.BeExecute") #end  #if("$!MailPoolForm" !="")		#end
					&nbsp;&nbsp;&nbsp;$MailPoolInfo								
			</li>
			
					#if("$!MailPoolForm" !="")	
			<li >
					$text.get("bol88.mailInfo.SumGroupware") $!MailPoolForm.totalSend  $text.get("oa.mail.mailAddress") <br/>
					$text.get("bol88.mailInfo.ThisGroupware") $!curCount  $text.get("oa.mail.mailAddress") <br/>
			</li>	
					#end									
			
			<li style="margin-left:80px">
			#if("$!MailPoolForm" == "" || "$!MailPoolForm.status" =="0") 
			<a href="javascript:startPool()" class="regbut" style="text-align:center">$text.get("bol88.mailInfo.BeginGroupware")</a>
			#else 
			<a href="/AIOBOL88EMailAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&type=keyword&winCurIndex=$winCurIndex" class="regbut" style="text-align:center">$text.get("bol88.mailInfo.AgainConfigure")</a>
			<a href="/AIOBOL88EMailAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&type=mailInfo&stop=true&winCurIndex=$winCurIndex" class="regbut" style="text-align:center">$text.get("bol88.mailInfo.StopGroupware")</a>
			
			#end	
			<a href="$globals.getBol88()/MailQueryAction.do?username=$username&JSESSION=$JSESSION" target="_blank" class="regbut" style="text-align:center">$text.get("bol88.mailInfo.inspectEmail")</a>
   			<a href="/AIOBOL88Action.do?winCurIndex=$winCurIndex" class="regbut" style="text-align:center">$text.get("common.lb.back")</a></li>
		</ul>
	</div> 
		    </div> 
            </div>
	    </div>
				    </div>
            </div>
	    </div>
</form>
</body>
</html>
