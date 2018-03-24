<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("crm.carefor.title.path")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script language="javascript">


function extendSubmit(status)
{
	if(!isChecked("keyId")){		
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	document.getElementById("operation").value = "$globals.getOP('OP_UPDATE')" ;
	document.getElementById("statusTo").value = status ;
	document.getElementById("type").value="stopOrEnable";
	form.submit();
}

function stopPath(){
	if(!isChecked("keyId")){		
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	if(window.confirm('$text.get("crm.msg.stopall")')){
		document.getElementById("operation").value = "$globals.getOP('OP_UPDATE')" ;
		document.getElementById("type").value = "stopPath";
		form.submit();
	}
}
</script>
</head>

<body onLoad="">
<form method="post" name="form" action="/CareforQueryAction.do">
	<input type="hidden" name="operation" value="$globals.getOP('OP_QUERY')">
	<input type="hidden" name="winCurIndex" value="$!winCurIndex">
	<input type="hidden" name="statusTo" value="">	

	<input type="hidden" name="type" value="">
		<div class="Heading">
			<div class="HeadingIcon">
				<img src="/$globals.getStylePath()/images/Left/Icon_1.gif" />
			</div>
			<div class="HeadingTitle">
				$text.get("crm.carefor.title.path")
			</div>
				<ul class="HeadingButton">
					<li>
						<button name="Submits" type="submit" onClick="forms[0].operation.value=$globals.getOP("OP_QUERY");return beforeBtnQuery();"  class="b2">
							$text.get("common.lb.query")
						</button>
					</li>
					<li><button name="clear" type="botton" onClick="clearForm(form);" class="b2">$text.get("common.lb.clear")</button></li>
					
					#if($globals.getMOperation().add())
					<li><button type="button" onClick="location.href='/CareforAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.add")</button></li>
					#end 
					
					#if($globals.getMOperation().delete())
					<li><button type="submit" onClick="javascript:return sureDel('keyId');" class="b2">$text.get("common.lb.del")</button></li>
					#end 
					#if($globals.getMOperation().update())
					<li>
						<input type="hidden" name="ButtonType" value="execDefine">
						<button type="button"
							onClick="extendSubmit(0)">
							$text.get('wokeflow.lb.enable')
						</button>
					</li>
					#end 
					#if($globals.getMOperation().update())
					<li>
						<input type="hidden" name="ButtonType" value="execDefine">
						<button type="button"
							onClick="extendSubmit(-1)">
							$text.get('billflow.stop')
						</button>
					</li>
					#end
					#if($globals.getMOperation().update())
					<li>
						<button type="button" onClick="stopPath()" class="b2">
							$text.get("crm.carefor.lb.stopAllpath")
						</button>
					</li>
					#end
					
				</ul>
			</div>
			<div class="listRange_1" id="listid">
				<ul>
					<li>
						<span>$text.get("crm.carfor.lb.path"):</span>
						<input name="name" value="$!CareforSearchForm.name"
							onKeyDown="if(event.keyCode==13) event.keyCode=9">
					</li>
					<li>
						<span>$text.get("common.lb.status"):</span>
						<select name="status">
							<option value=""></option>
							#foreach($enum in $globals.getEnumerationItems('StopOpenState'))
								<option value="$enum.value" #if($enum.value==$CareforSearchForm.status) selected="selected" #end>$enum.name</option>
							#end
						</select>
					</li>
				</ul>
			</div>
	
		<div class="scroll_function_small_a" id="list_id">
				<script type="text/javascript">
var oDiv=document.getElementById("list_id");
var sHeight=document.body.clientHeight-100;
oDiv.style.height=sHeight+"px";
</script>
			<table width="500" border="0" cellpadding="0" cellspacing="0"
				class="listRange_list_function" id="tblSort" name="table">
				<thead>
					<tr>
						<td width="30" class="oabbs_tc">
							<input type="checkbox" name="selAll" onClick="checkAll('keyId')">
						</td>
						<td width="300" class="oabbs_tl">
							$text.get("crm.carfor.lb.path")
						</td>
						<td width="100" class="oabbs_tc">
							$text.get("common.lb.status")
						</td>
						#if($globals.getMOperation().update())
						<td width="200" class="oabbs_tc">
							$text.get("common.lb.update")
						</td>
						#end
						<td width="100" class="oabbs_tc">
							$text.get("common.lb.detail")
						</td>
					</tr>
				</thead>
				<tbody>

					#foreach ($carefor in $carefors)
					<tr onMouseMove="setBackground(this,true);"
						onMouseOut="setBackground(this,false);">

						<td class="oabbs_tc">
							<input type="checkbox" name="keyId" value="$carefor.id">
						</td>

						<td class="oabbs_tl" > 
							$carefor.name &nbsp;
						</td>
						<td class="oabbs_tc">
						$globals.getEnumerationItemsDisplay('StopOpenState',$carefor.status)
						&nbsp;
						</td>

						#if($globals.getMOperation().update())
						<td class="oabbs_tc">
							<a href='/CareforAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&KeyId=$carefor.id&winCurIndex=$!winCurIndex'>$text.get("common.lb.update")</a>
							&nbsp;&nbsp;
							<a href='/CareforAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&KeyId=$carefor.id&winCurIndex=$!winCurIndex'>$text.get("crm.carefor.title.event")</a>
							
						</td>
						#end
						<td class="oabbs_tc">
							<a href='/CareforAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&KeyId=$carefor.id&winCurIndex=$!winCurIndex&detail=true'>$text.get("common.lb.detail")</a>
						</td>
					</tr>
					#end
				</tbody>
			</table>
		<div class="listRange_pagebar"> $!pageBar </div>
</div>
	</form>
</body>
</html>
