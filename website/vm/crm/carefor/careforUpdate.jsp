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

function delEvent(delId,keyId){
	if(confirm('$text.get("common.msg.confirmDel")')){
 		window.location.href = '/CareforDelAction.do?nextSeq=$nextSeq&id='+delId+'&operation=$globals.getOP('OP_DELETE')&KeyId='+keyId+'&winCurIndex=$!winCurIndex';
 	}
 }

</script>
</head>

<body>
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" name="form" onSubmit="return beforSubmit(this)" action="/CareforAction.do" target="formFrame">
	<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")">
	<input type="hidden" name="winCurIndex" value="$!winCurIndex">
	<input type="hidden" name="status" value="$bean.status">
	<input type="hidden" name="id" value="$bean.id">
	<input type="hidden" name="KeyId" value="$bean.id">
	<input type="hidden" name="type" value="">
	<div class="Heading">
			<div class="HeadingIcon">
				<img src="/$globals.getStylePath()/images/Left/Icon_1.gif" />
			</div>
			<div class="HeadingTitle">
				$text.get("crm.carefor.title.path")
			</div>
				<ul class="HeadingButton">
					#if($globals.getMOperation().update()&& "$!detail" != "true")
					<li>
						<button type="submit" class="b2" >
							$text.get("common.lb.save")
						</button>
					</li>
					<li>
						<button type="button" onClick="location.href= '/CareforDelAction.do?nextSeq=$nextSeq&id=$bean.id&operation=$globals.getOP('OP_ADD_PREPARE')&winCurIndex=$!winCurIndex'" class="b2">
							$text.get("crm.carfor.lb.newEvent")
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
		<div id="listRange_id">
		<div class="listRange_1">
				<ul>
					<li style="width:95%;">
						<span>$text.get("crm.carfor.lb.path")：<font color="red">*</font></span>
						<input name="name" value="$bean.name"
							onKeyDown="if(event.keyCode==13) event.keyCode=9" style="width: 550px;">
					</li>
				</ul>
		</div>
	
		<div class="scroll_function_small_a" id="list_id">
		<script type="text/javascript">
var oDiv=document.getElementById("list_id");
var sHeight=document.body.clientHeight-130;
oDiv.style.height=sHeight+"px";
</script>
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="listRange_list_function" id="tblSort" name="table">
				<thead>
					<tr>
						<td width="40" class="oabbs_tc">
							$text.get("iniAcc.lb.iniAcc.lb.sequencenumber")
						</td>
						
						<td width="100" class="oabbs_tl">
							$text.get("com.carefor.lb.event")
						</td>
						
						<td width="460" class="oabbs_tc">
							$text.get("crm.carefor.lb.execdate")
						</td>
					
						<td width="*" class="oabbs_tc">
							$text.get("crm.carefor.lb.execaction")
						</td>
						#if("$!detail"=="true")
						<td width="40" class="oabbs_tc">
							$text.get("common.lb.detail")
						</td>
						#else
						<td width="40" class="oabbs_tc">
							$text.get("common.lb.edit")
						</td>
						<td width="40" class="oabbs_tc">
							$text.get("common.lb.del")
						</td>
						#end
					</tr>
				</thead>
				<tbody>
					#set($index = 0)
					#foreach ($careforDel in $careforDels)
					#set($index = $index+1)
					<tr onMouseMove="setBackground(this,true);"
						onMouseOut="setBackground(this,false);">

						<td class="oabbs_tc">
							$index &nbsp;
						</td>

						<td class="oabbs_tl">
							$careforDel.actionName &nbsp;
						</td>
						<td class="oabbs_tl">
							#if($careforDel.dateType==0)
							<strong>$text.get("crm.carefor.lb.nodate"):</strong>$text.get("crm.carefor.lb.nodatelimit")
							#elseif($careforDel.dateType==1)
							<strong>$text.get("crm.carefor.lb.absolutedate"):</strong>$text.get("alertCenter.lb.startDate")$careforDel.startDate $text.get("alertCenter.lb.endDate")$careforDel.endDate
							#elseif($careforDel.dateType==2)
							<strong>$text.get("crm.carefor.lb.comparativedate"):</strong>$text.get("crm.carefor.lb.afterdate")$careforDel.baselineDate$text.get("crm.carefor.lb.afterdateover")，$text.get("crm.carefor.lb.erery")$careforDel.space$text.get("crm.carefor.lb.toexec")$text.get("crm.carefor.lb.repetition")$careforDel.runDates$text.get("crm.carefor.lb.ci")
							#elseif($careforDel.dateType==3)
							<strong>$text.get("crm.carefor.lb.cycle"):</strong>$text.get("crm.carefor.lb.afterdate")$careforDel.baselineDate$text.get("crm.carefor.lb.afterdateover")，$text.get("crm.carefor.lb.erery")$careforDel.space$text.get("crm.carefor.lb.toexec")$text.get("crm.carefor.lb.repetition")$careforDel.runDates$text.get("crm.carefor.lb.ci")，$text.get("crm.carefor.lb.cycleTitle")$careforDel.runTimes$text.get("crm.carefor.lb.yearci")
							#end
						</td>
						<td class="oabbs_tl" 
						title="#if($careforDel.actionType == 0)
							$careforDel.eventContent
						#elseif($careforDel.actionType == 1)
							$careforDel.smsContent
						#elseif($careforDel.actionType == 2)
							$careforDel.mailTitle
						#end">
						#if($careforDel.actionType == 0)
							<strong>$text.get("crm.carefor.lb.actionconcretely"):</strong> $careforDel.eventContent
						#elseif($careforDel.actionType == 1)
							<strong>$text.get("com.carefor.lb.sendsms"):</strong> $careforDel.smsContent
						#elseif($careforDel.actionType == 2)
							<strong>$text.get("common.lb.sendmail"):</strong> $careforDel.mailTitle
						#end
						</td>
						#if("$!detail"=="true")
						<td class="oabbs_tc">
							<a href="/CareforDelAction.do?nextSeq=$nextSeq&id=$careforDel.id&operation=$globals.getOP('OP_UPDATE_PREPARE')&detail=true&KeyId=$bean.id&winCurIndex=$!winCurIndex">$text.get("common.lb.detail")</a>
						</td>
						#else
						<td class="oabbs_tc">
							<a href="/CareforDelAction.do?nextSeq=$nextSeq&id=$careforDel.id&operation=$globals.getOP('OP_UPDATE_PREPARE')&KeyId=$bean.id&winCurIndex=$!winCurIndex">$text.get("common.lb.edit")</a>
						</td>
						<td class="oabbs_tc">
							<a href="javascript:delEvent('$careforDel.id','$bean.id') ">$text.get("mrp.lb.delete")</a>
						</td>
						#end
					</tr>
					#end
				</tbody>
			</table>
			
		</div>
	</div>	
	
</form>
</body>
</html>
