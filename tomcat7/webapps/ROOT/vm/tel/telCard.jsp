<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript">
function setCard(){
	if(document.form.card.value == ""){
		alert("$text.get('tel.msg.NoCommCard')");
		return;
	}
	if(document.form.card.value.length > 70){
		alert("$text.get('tel.msg.OverCommCardSize')");
		return;
	}
	document.form.type.value = "set";
	document.form.submit();
}
function closeCard(){
	document.form.type.value = "close";
	document.form.submit();
}
</script>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form action="/TelAction.do?operator=card" name="form" method="post" >
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
 <input type="hidden" name="noback" value="$!noback">
 <input type="hidden" name="type" value="">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("tel.msg.CommCardSet")</div>
	<ul class="HeadingButton">
					 <li></li>
			</ul>
</div>
<div id="listRange_id">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
    <div class="scroll_function_small_a">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
		<thead>
			<tr>
				<td colspan="2" class="oabbs_tl">&nbsp;</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="oabbs_tr">$text.get("tel.lb.cardState")：</td>
				<td>
					#if($!result.status == 1) $text.get("longtime.date.close") #else $text.get("com.lb.close") #end
				</td>
			</tr>
			<tr>
				<td class="oabbs_tr">$text.get("tel.lb.cardState")：</td>
				<td>
					<textarea name="card"  id="card" cols="60" rows="3" maxlength="3">$!result.card</textarea>
					<span style="color: #0066FF;margin-left:10px"><br/>$text.get("tel.msg.cardPowerSoOn")<br/>
					$text.get("tel.msg.adrsPhoneExample")</span>
				</td>
			</tr>
			
		<tr>
			<td colspan="2">
				#if($!result.status == 1) 
				<button type="button" name="button" onClick="setCard()" class="b2" style="margin-left:250px;width:80px">$text.get("common.lb.reset")</button>
				<button type="button" name="button" onClick="closeCard()" class="b2" style="width:80px">$text.get("com.lb.close")</button>
				#else 
				<button type="button" name="button" onClick="setCard()" class="b2" style="margin-left:250px;width:80px">$text.get("common.msg.set")</button>				
				#end
			</td>
		</tr>
		  </tbody>
		</table>
	</div>
</div>
</form>
</body>
</html>
