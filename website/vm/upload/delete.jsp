<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self">

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>#if($type == "PIC") $text.get("upload.lb.picupload") #else $text.get("upload.lb.affixupload")  #end </title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript">

putValidateItem("fileName","$text.get("upload.lb.fileName")","any","",false,0,250);

function beforSubmit(form){   
	if(!validate(form))return false;
	disableForm(form);
	return true;
}	
</script>
<style>
.Prongix {
	width:64px;
	height:64px;
	background:url(/style/images/allbg.gif) no-repeat -121px -130px;
}
</style>
</head>

<body onLoad="showStatus();" scroll="no">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/UploadDelAction.do"  onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_DELETE")">
<input type="hidden" name="type" value="$type">
<input type="hidden" name="fileName" value="$fileName">
<input type="hidden" name="tempFile" value="$tempFile">
<input type="hidden" name="tableName" value="$tableName">
<input type="hidden" name="NOTTOKEN" value="NOTTOKEN">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("common.msg.message")</div>
</div>
		<div style="width:100%;padding-top:5px; height:auto;float:left; border-top:1px solid #CCCCCC; background:#fff url(/$globals.getStylePath()/images/aiobg.gif) repeat-x left -221px;">

		<div style="padding-left: 100px;padding-top:20px; width:100%; border-bottom:1px dotted  #ccc">
			<li class="Prongix" style=" float:left;"></li>
			<li style="float:left; text-indent:15px; padding-top:30px;">$text.get("common.msg.confirmDel")</li>
			
		</div>
		<div style="padding-left: 100px;padding-top: 20px;">
			<button type="submit" style="width:80px;">$text.get("common.lb.yes")</button>&nbsp;<button type="button" style="width:80px;" onClick="window.close();">$text.get("common.lb.no")</button>
		</div>


</div>
		
</form> 
</body>
</html>
