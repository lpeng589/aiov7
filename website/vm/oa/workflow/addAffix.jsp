<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("com.flow.add.affix")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css"  />
<style type="text/css">
body{height: 98%;}
.listRange_jag{margin:10px;}
.listRange_jag li{ float:none;text-align:left; width:auto; margin-bottom:10px;margin-left: -60px;}
.HeadingTitle div{ float:left;}
#files_preview div {width: 180px;height: 40px;white-space:nowrap;text-overflow:ellipsis;overflow:hidden;}
</style>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script type="text/javascript">

function beforSubmit(form){ 
	if(form.deliverance.value.length==0 && form.attachFiles.value.length==0){
		alert('$text.get("com.deliverance.affix.null")') ;
		return false ;
	}
	window.parent.returnValue="deliverTo";
	return true;
}

</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form  method="post" scope="request" name="form" action="/OAMyWorkFlow.do" onSubmit="return beforSubmit(this);"  target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="winCurIndex" value="$!winCurIndex">
<input type="hidden" name="keyId" value="$!keyId">
<input type="hidden" name="attachFiles" value="">
<input type="hidden" name="delFiles" value="">
<input type="hidden" name="designId" value="$!designId">
<input type="hidden" name="tableName" value="$!tableName">

<div class="Heading">
	
	<div class="HeadingTitle" >
		<div>$text.get("com.flow.add.affix")</div>
	</div>
	
</div>

<div class="listRange_frame" align="center">
		<div class="scroll_function_small_a" id="conter">

	<div class="listRange_div_jag" style="width:610px;height: 230px;overflow-y:auto;">
		<ul class="listRange_jag" style="width:auto;">
			<li style="width:100%;margin:0;">
				<span style="float:left;display:inline-block;">$text.get("common.msg.SignAdvice")</span>
				<textarea style="width: 420px;" name=deliverance rows="5" cols="40" >$!deliver.deliverance</textarea>
			</li>
			<li style="width:100%;margin:0;">
				<span style="float:left;display:inline-block;">$text.get("upload.lb.affix")：</span>
				<div style="float: left;"><button type=button class="hBtns" onClick="openAttachUpload('/affix/$!tableName/')">$text.get("oa.common.accessories")</button></div>
				<div id="files_preview" style="margin-left:90px; background: none;"></div>
			</li>
			<!-- 
			<li style="margin-top:5px;"><span></span>
				<button type = "submit" class="b2">$text.get("common.msg.submit")</button>
				<button type = "button" onClick="javascript:window.close();" class="b2">$text.get("common.lb.close")</button>
			</li>
			 -->
		</ul>
	</div>
</div>
</div>
</form>
</body>
</html>
