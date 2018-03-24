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
</head>

<body onLoad="showStatus();" scroll="no" style="overflow:hidden;">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/UploadAction.do" enctype="multipart/form-data" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")">
<input type="hidden" name="type" value="$type">
<input type="hidden" name="NOTTOKEN" value="NOTTOKEN">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">#if($type == "PIC") $text.get("upload.lb.picupload") #else $text.get("upload.lb.affixupload")  #end </div>
</div>
<div style="width:100%;padding-top:5px; height:auto;float:left; border-top:1px solid #CCCCCC; background:#fff url(/$globals.getStylePath()/images/aiobg.gif) repeat-x left -221px;">

		<div style="padding-left: 50px;padding-top:20px;">
			$text.get("upload.lb.fileName")：<input name="fileName" class="text" type="file" value="" maxlength="170">
		</div>
		<div style="padding-left: 100px;padding-top: 20px;">
			<button type="submit" style="width:80px;">$text.get("upload.lb.upload")</button>
			<button type="button" style="width:80px;"onClick="window.close();">$text.get("common.lb.close")</button>
		</div>


</div>
</form> 
</body>
</html>
