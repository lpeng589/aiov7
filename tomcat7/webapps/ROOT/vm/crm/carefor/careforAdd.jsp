<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("crm.carefor.title.path")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript" src="/js/date.vjs"></script>
<script language="javascript">

putValidateItem("name","$text.get("crm.carfor.lb.path")","any","",false,1,40 );

function beforSubmit(form){   
	if(!validate(form))return false;
	disableForm(form);
	return true;
}
</script>
</head>

<body onLoad="form.name.focus()">
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" name="form" onsubmit="return beforSubmit(this)" action="/CareforAction.do"  target="formFrame">
	<input type="hidden" name="operation" value="$globals.getOP('OP_ADD')">
	<input type="hidden" name="winCurIndex" value="$!winCurIndex">
	<input type="hidden" name="status" value="0">
	<div class="Heading">
			<div class="HeadingIcon">
				<img src="/$globals.getStylePath()/images/Left/Icon_1.gif" />
			</div>
			<div class="HeadingTitle">
				$text.get("crm.carefor.title.path")
			</div>
				<ul class="HeadingButton">
					#if($globals.getMOperation().add())
					<li>
						<button type="submit" class="b2">
							$text.get("common.lb.save")
						</button>
					</li>
					#end 
					<li>
						<button name="back" type="button" onClick="location.href='/CareforQueryAction.do?operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex'" class="b2">
							$text.get("common.lb.back")
						</button>
					</li>
				</ul>
		</div>
		<div class="listRange_1" id="">
				<ul>
					<li style="width:95%;">
						<span>$text.get("crm.carfor.lb.path"):</span><font color="red">*</font><input name="name" value=""	onKeyDown="if(event.keyCode==13) event.keyCode=9" style="width: 550px;">
					</li>
				</ul>
		</div>
			
	
</form>
</body>
</html>
