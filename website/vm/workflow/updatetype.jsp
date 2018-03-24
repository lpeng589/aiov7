<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.workflow.type.update")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script>
function submitform(){
	if(!isChecked("typeUpdate")){
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	var cbs=document.getElementsByName("typeUpdate");
	for(var i=0;i<cbs.length;i++){
		if(cbs[i].checked){
			window.returnValue = cbs[i].value ;
			break ;
		}
	}
	window.close();
}
</script>
</head>
<body>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle"><span class="oabbs_tc">$text.get("oa.workflow.type.update")</span></div>
	<ul class="HeadingButton">
				<li>
					<button type="button" name="Submit" class="b2" onclick="return submitform();">$text.get("common.lb.save")</button></li>
			 	<li> 
			 		<button type="button" onClick="window.close();" class="b2">$text.get("common.lb.cancel")</button>
				</li>
			</ul>
	</div>
<div  style="height:200px; overflow-y:auto; margin-top:10px;">
		<table border="0" width="300"  cellpadding="0" align="center" cellspacing="0" class="listRange_list_function" name="table">
		<thead>
			<tr>
				<td class="oabbs_tl" width="30">&nbsp;</td>
				<td>$text.get("oa.workflow.type")</td>
			</tr>
		</thead>
		<tbody>	
			#foreach ($ls_row in $twftstype)
			<tr>
				<td class="oabbs_tl" width="30"><input type="radio" name="typeUpdate" value="$!globals.get($ls_row,0)" /></td>
				<td align="center">$!globals.get($ls_row,1)</td>
			</tr>
			#end
		</tbody>
	</table>
</div>
</body>
</html>
