<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("crm.client.attention.path")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/date.vjs"></script>
<script language="javascript">
function openSelect1(urlstr,displayName,obj,obj2){
	displayName=encodeURI(displayName) ;
	var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=query&LinkType=@URL:&displayName="+displayName,"","dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)=="undefined"){return;}
	if(str.length>0){
		var strs = str.split(';');
		document.getElementById(obj).value=strs[0];
		document.getElementById(obj2).value=strs[1];
	}else{
		document.getElementById(obj).value="";
		document.getElementById(obj2).value="";
	}
}
function deleteList(){
	if(sureDel('keyId')){		
		form.operation.value = "$globals.getOP('OP_QUERY')" ;
		form.type.value = "delete" ;
		form.submit();
	}
}

function query(){
	form.operation.value="$globals.getOP('OP_QUERY')";
	form.submit();
}
</script>
<style> 
.scrollColThead {
	position: relative;
	top: expression(this.parentElement.parentElement.parentElement.scrollTop);
	z-index:2;
}
</style>
</head>

<body onLoad="">
<form method="post" name="form" action="/ClientLinkmanMemoryDayAction.do">
<input type="hidden" name="operation" value="$globals.getOP('OP_QUERY')">
<input type="hidden" name="firstName" value="$!firstName">
<input type="hidden" name="type" value="main">
<input type="hidden" name="id" value="">
		<div class="Heading">
			<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif" /></div>
			<div class="HeadingTitle">
				$text.get("crm.clientLinkmanMemoryDay")
			</div>
				<ul class="HeadingButton">
					<li><button type="button" onClick="query();" class="b2">$text.get("common.lb.query")</button></li>
					#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?parentTableName=CRMClientInfo&tableName=CRMMemoryDay").add())
					<li><button type="button" onClick="javascript:mdiwin('/UserFunctionQueryAction.do?parentTableName=CRMClientInfo&tableName=CRMMemoryDay&src=menu&operation=6&noback=true','$text.get("crm.clientLinkmanMemoryDay.addClientLinkmanMemoryDay")');" class="b2">$text.get("common.lb.add")</button></li>
					#end
					<!-- 
					#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?parentTableName=CRMClientInfo&tableName=CRMMemoryDay").delete())
					<li><button type="button" onClick="javascript:deleteList();" class="b2">$text.get("common.lb.del")</button></li>
					#end
					 -->
				</ul>
			</div>
			<div class="listRange_1" id="listid">
				<ul>
					<li>
						<span>$text.get("oa.mail.linkman"):</span>
						<input name="userName" value="$!userName" onKeyDown="if(event.keyCode==13) event.keyCode=9">
					</li>
					<li>
						<span>$text.get("crm.client.name"):</span>
						<input type="hidden" name="ClientNo" value="$!ClientNo">
						<input name="ClientName" value="$!ClientName" onKeyDown="if(event.keyCode==13) event.keyCode=9"
							ondblclick="openSelect1('/UserFunctionAction.do?operation=22&selectName=ReportSelectClient','$text.get('crm.client.name')','ClientNo','ClientName')"';><img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="openSelect1('/UserFunctionAction.do?operation=22&selectName=ReportSelectClient','$text.get('crm.client.name')','ClientNo','ClientName')"';>
					</li>
				</ul>
			</div>
	
		<div class="scroll_function_small_a" id="list_id">
<script type="text/javascript">
	var oDiv=document.getElementById("list_id");
	var sHeight=document.body.clientHeight-100;
	oDiv.style.height=sHeight+"px";
</script>
			<table width="700" border="0" cellpadding="0" cellspacing="0"
				class="listRange_list_function" id="tblSort" name="table">
				<thead>
					<tr class="scrollColThead">
						<td width="20" class="oabbs_tc">&nbsp;</td>
						<td width="30" class="oabbs_tc"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
						<td width="80" class="oabbs_tc">$text.get("oa.mail.linkman")</td><!-- 联系人 -->
						<td width="120"class="oabbs_tc">$text.get("crm.client.name")</td><!-- 客户 -->
						<td width="80" class="oabbs_tc">$text.get("crm.memory.type")</td><!-- 纪念日类型 -->
						<td width="60" class="oabbs_tc">$text.get("oa.calendar.daterType")</td><!-- 日期类型 -->
						<td width="80" class="oabbs_tc">$text.get("crm.memory")</td><!-- 纪念日 -->
						<td width="100" class="oabbs_tc">$text.get("crm.next.remind")</td><!-- 下次提醒时间 -->
						#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?parentTableName=CRMClientInfo&tableName=CRMMemoryDay").update())
						<td width="60" class="oabbs_tc">$text.get("oa.common.upd")</td><!-- 修改 -->
						#end
						<td width="60" class="oabbs_tc">$text.get("sms.note.detail")</td><!-- 详情 -->
					</tr>
				</thead>
				<tbody>
					#foreach ($det in $list)
					<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
						<td class="oabbs_tc">$velocityCount</td>
						<td width="30" class="oabbs_tc"><input type="checkbox" name="keyId" value="$globals.get($det,8):$globals.get($det,6)"></td>
						<td width="80" class="oabbs_tc">$!globals.get($det,2)&nbsp;</td>
						<td width="120" class="oabbs_tc">$!globals.get($det,1)&nbsp;</td>
						<td width="80" class="oabbs_tc">$!globals.getEnumerationItemsDisplay("MemoryType","$globals.get($det,4)","$globals.getLocale()")&nbsp;</td>
						<td width="60" class="oabbs_tc">$!globals.getEnumerationItemsDisplay("dateType","$globals.get($det,3)","$globals.getLocale()")&nbsp;</td>
						<td width="60" class="oabbs_tc">$!globals.get($det,5)&nbsp;</td>
						<td width="100" class="oabbs_tc">$!globals.get($det,7)&nbsp;</td>
						#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?parentTableName=CRMClientInfo&tableName=CRMMemoryDay").update())
						<td width="60" class="oabbs_tc">
							<a href="javascript:mdiwin('/UserFunctionQueryAction.do?tableName=CRMMemoryDay&parentTableName=CRMClientInfo&src=menu&noback=true&operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId=$!globals.get($det,6)','$text.get("oa.common.upd")$text.get("crm.client.linkman")')";>$text.get("oa.common.upd")</a>
						</td>
						#end
						<td width="60" class="oabbs_tc">
							<a href="javascript:mdiwin('/UserFunctionAction.do?tableName=CRMMemoryDay&parentTableName=CRMClientInfo&src=menu&noback=true&keyId=$!globals.get($det,6)&operation=5','$text.get("sms.note.detail")$text.get("crm.client.linkman")')";>$text.get("sms.note.detail")</a>
						</td>
					</tr>
					#end
				</tbody>
			</table>
		</div>
		<div class="listRange_pagebar"> $!pageBar </div>
	</form>
</body>
</html>
