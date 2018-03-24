<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("unitadmin.title.add")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript">

putValidateItem("unitName","$text.get("unit.lb.name")","any","",false,0,10);
putValidateItem("remark","$text.get("unit.lb.remark")","any","",true,1,99 );

function beforSubmit(form){   
	if(!validate(form))return false;
	disableForm(form);
	return true;
}	
</script>
</head>

<body onLoad="showStatus();">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/UnitAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="createBy" value="$result.createBy">
<input type="hidden" name="id" value="$result.id">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("unitadmin.title.add")</div>
	<ul class="HeadingButton">
		
	</ul>
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
				<li><span>$text.get("unit.lb.name")：</span><input name="unitName" type="text" style="width:100px" value="" maxlength="50">
                    <img src="/$globals.getStylePath()/images/biaozianniu.gif" width="15" height="15"></li>
			  <li><span>$text.get("unit.lb.remark")：</span><input name="remark" type="text"  style="width:150px" value="" maxlength="50"></li>
				<li style="margin-top:10px;margin-left:40px; float:left; "><button type="submit" class="b2">$text.get("common.lb.add")</button><button type="button"  onClick="location.href='/UnitQueryAction.do?operation=$globals.getOP("OP_QUERY")'" class="b2">$text.get("common.lb.back")</button></li>
			</ul>
		<div>($text.get("common.title.mustInput")</span><img src="/$globals.getStylePath()/images/biaozianniu.gif" width="15" height="15">)</div>
		</div>
</div>
</form> 
</body>
</html>
