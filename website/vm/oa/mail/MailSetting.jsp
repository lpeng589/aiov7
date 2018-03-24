<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css">

<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/validate.vjs"></script>
<script type="text/javascript">

function beforSubmit(form){   
	if(!confirm("$text.get("common.msg.confirmupdate")")){
		return false;
	}

	if(!validate(form))return false;
	disableForm(form);
	//window.parent.frames['leftFrame'].location.reload() ;
	return true;
}	

if(typeof(top.junblockUI)!="undefined"){
	top.junblockUI();
}



</script>
<style type="text/css">
.listrange_woke{width:900px;padding:10px 0 10px 0;padding:0;overflow:hidden;}
</style>
</head>
<body style="overflow-y:auto;">

	
<div align="center" >
	<div class="scroll_function_small_a" id= "bodydiv" style="overflow-y:auto">
				<div class="listRange_div_jag_hint" style="width:1000px;margin:20px 0 0 0;height:auto;" align="center" >
					<div class="listRange_div_jag_div" style="width:1000px; height:22px; line-height:22px;">$text.get("email.lb.mailset")</div>	
<form id="form" name="form" method="post" action="/EMailSettingAction.do" onSubmit="return beforSubmit(this)" >
<input type="hidden" name="operation" value="$!globals.getOP("OP_UPDATE")"/>	
					<fieldset style="color:#333;margin:10px;padding:0 0 10px 0;"><legend>$text.get("email.lb.filtergz")</legend>
								
					<ul class="listRange_woke" style="height:150px;">	
						<li style="width:900px;">
							<span style="width:100px;padding-right:10px">$text.get("email.lb.filterDate"):</span>
							$text.get("email.lb.reclacur")<input name="filterDate" id="smtpserver" type="text" value="$!result.get("filterDate")" style="width:30px;" >
							$text.get("email.lb.reclacur2")
						</li>
						<li style="width:900px;">
							<span style="width:100px;padding-right:10px"></span>	
							<font color="red">$text.get("email.msg.filterdomain")</font>	
						</li>
						<li style="width:900px;height:auto;">
							<span style="width:100px;padding-right:10px">$text.get("email.lb.filterdomain"):</span>						
							<textarea style="width:680px;height:70px" name="filterDomain">$!result.get("filterDomain")</textarea>
						</li>
					</ul>
					<button type="submit" name="Submit" style="width:60px">$text.get("common.lb.save")</button>
					</fieldset>
</form>					
<form id="form" name="form" method="post" action="/EMailSettingAction.do" onSubmit="return beforSubmit(this)" >
<input type="hidden" name="operation" value="$!globals.getOP("OP_UPDATE")"/>
					<fieldset style="color:#333;margin:10px;padding:0 0 10px 0;"><legend>$text.get("email.lb.labelset")</legend>								
					<ul class="listRange_woke" style="height:80px;">
						<li style="width:900px;height:auto;margin:10px 0 0 0;">
							<span style="width:100px;padding-right:10px">$text.get("email.lb.label"):</span>						
							<textarea style="width:680px;height:50px" name="mailLabel">$!result.get("mailLabel")</textarea>
						</li>
					</ul>
					<button type="submit" name="Submit" style="width:60px">$text.get("common.lb.save")</button>
					</fieldset>
</form>					
				</div>																		
	</div>			
 
</div>

</body>
</html>
	<script type="text/javascript">
	var oDiv=document.getElementById("bodydiv");
	var sHeight=document.body.clientHeight-15;
	oDiv.style.height=sHeight+"px";
	</script>
