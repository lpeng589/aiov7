<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" />
<link type="text/css" rel="stylesheet" href="/style/css/mail.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/validate.vjs"></script>

<script type="text/javascript">

putValidateItem("gouPass","$text.get("sms.lb.gouPass")","any","",false,0,10);
putValidateItem("smsSign","$text.get("sms.lb.sign")","any","",false,1,10 );
function smsStart()
{
	if(!validate(document.form))return false;
	
  var url =document.getElementById("url");
  var gouPass=document.getElementById("gouPass");

   disableForm(document.form);
    if($flag==1)
	{
	    form.action="/SMSSetAction.do?choice=start&smsUrl="+url.value+"&smsGouPass="+gouPass.value;
		form.submit();
	}else{
		 form.action="/SMSSetAction.do?choice=stop";
		 form.submit();
	}
}

</script>
</head>

<body>

<form  method="post" scope="request" name="form" action="/SMSSetAction.do">
<input type="hidden" name="id" value="$!noteSet.id"/>
<input name="url" id="url"  type="hidden" value="$!noteSet.url">
<div class="MailAccount" id="MailAccount">
	<script type="text/javascript">
	var oDiv=document.getElementById("MailAccount");
	var sHeight=document.body.clientHeight-52;
	oDiv.style.height=sHeight+"px";
	</script>
	<div class="list_left_topleft"></div><div class="list_left_topright"></div>
	
	<div class="MailAccount_content">
		<div style="text-align:center;color:red;">#if("$flag" == "0") $text.get("note.alreadyenable") #else $text.get("note.alreadydisabled") #end</div>	
		<ul class="MailAccount_form" style="border:none;margin:0;padding-top:10px;">
		#if($display=="1")
			<li>
				<span>$text.get("sms.lb.gouPass")：</span>
				<input name="gouPass" id="gouPass" type="text"  value="$!noteSet.gouPass">
			</li>
			<li>
				<span>$text.get("sms.lb.sign")：</span>
				<input name="smsSign" id="smsSign" type="text"  value="$!noteSet.smsSign">
			</li>
			<li style="text-align:center;">
					
					<button type="button" class="toolbu_02" style="float:none;" onClick="smsStart()"> #if("$flag" == "0") $text.get("StopValue") #else $text.get("OpenValue") #end</button>
			</li>		
		#else
			<li>
				<span></span>
				<p>$text.get("sms.msg.unavailable")</p>
			</li>
	    #end		
		
		</ul>
	</div>
	<div class="list_left_bottomleft"></div><div class="list_left_bottomright"></div>
</div>
</form> 
</body>
</html>
