<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">

<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript">


putValidateItem("email","$text.get("oa.email.address")","mail","",false,0,150);

function beforSubmit(form){   
	if(!validate(form))return false;
	disableForm(form);
	return true;
}	

if(typeof(top.junblockUI)!="undefined"){
	top.junblockUI();
}


function submitform()
{
	document.form.submit();
}

</script>
<style type="text/css">
.listrange_woke li{float:none;}
</style>
</head>
<body  scroll="no">

<form id="form" name="form" method="post" action="/EMailBlackAction.do" onSubmit="return beforSubmit(this)" >
<input type="hidden" name="operation" value="$!globals.getOP("OP_ADD")"/>



<div class="oamainhead">
<div class="HeadingButton">

	</div>
  </div>			

<div align="center">
	<div class="scroll_function_small_a" >
				<div class="listRange_div_jag_hint" style="width:600px;" align="center" >
					<div class="listRange_div_jag_div" style="width:600px; height:22px; line-height:22px;">$text.get("email.msg.addblack")</div>						
					<ul class="listRange_woke" style="width:480px; " >
						<!--  第一页-->
						<li style="width:420px;">
							<span style="width:150px;">$text.get("oa.email.address")</span>
							<input name="email" id="email" type="text" value="" class="text"  style="width:230px;">
						</li>					
						
						<li style="width:420px;text-align:center">
						<button type="submit" name="Submit">$text.get("common.lb.save")</button>
						<button type="button" onClick="location.href='/EMailBlackQueryAction.do'">$text.get("common.lb.back")</button>
						</li>
					
					</ul>
				</div>																		
	</div>			
 
</div>
</form>

</body>
</html>
