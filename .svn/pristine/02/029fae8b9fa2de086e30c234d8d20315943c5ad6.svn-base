<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("crm.carefor.path")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/date.vjs"></script>
<script language="javascript">
function openSelect1(urlstr,displayName,obj,obj2){
	displayName=encodeURI(displayName) ;
	var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,"","dialogWidth=730px;dialogHeight=450px"); 
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
function query(){
	form.submit();
}
function openMessage(id){
	var str = showModalDialog('/CareforExecuteAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&delId='+id,winObj,'dialogWidth=730px;dialogHeight=450px');
	if(str) location.reload();
}
</script>
<style>
img { vertical-align:middle;}
</style>
</head>

<body>
<form method="post" name="form" action="/CareforExecuteAction.do">
	<input type="hidden" name="operation" value="$globals.getOP('OP_QUERY')">
	<input type="hidden" name="statusTo" value="">
		<div class="Heading">
			<div class="HeadingIcon">
				<img src="/$globals.getStylePath()/images/Left/Icon_1.gif" />
			</div>
			<div class="HeadingTitle">
				$text.get("crm.carefor.path")
			</div>
				<ul class="HeadingButton">
					<li>
						<button name="Submits" type="submit" onClick="query()" class="b2">
							$text.get("common.lb.query")
						</button>
					</li>
				</ul>
			</div>
			<div class="listRange_1" id="listid">
				<ul>
					<li>
						<span>$text.get("call.lb.client"):</span>
						<input type="hidden" name="clientNo" value="$!clientNo">
						<input name="clientName" value="$!clientName"
							onKeyDown="if(event.keyCode==13) event.keyCode=9"
							ondblclick="openSelect1('/UserFunctionAction.do?operation=22&selectName=ReportSelectClient','$text.get('call.lb.client')','clientNo','clientName')"';><img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="openSelect1('/UserFunctionAction.do?operation=22&selectName=ReportSelectClient','$text.get('call.lb.client')','clientNo','clientName')"';>
					</li>
					<li>
						<span>$text.get("sms.note.status"):</span>
						<select name="status">
							<option value=""></option>
							#foreach($enum in $globals.getEnumerationItems('actionStatus'))
								<option value="$enum.value" #if($enum.value==$status) selected="selected" #end>$enum.name</option>
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
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="listRange_list_function" id="tblSort" name="table">
				<thead>
					<tr>
						
						
						<td width="40" class="oabbs_tc">
							$text.get("iniAcc.lb.iniAcc.lb.sequencenumber")
						</td>
						<td width="100" class="oabbs_tl">
							$text.get("call.lb.client")
						</td>
						<td width="100" class="oabbs_tl">
							$text.get("crm.carfor.lb.path")
						</td>
						<td width="100" class="oabbs_tc">
							$text.get("sms.note.status")
						</td>
						<td width="*" align="left">
							$text.get("crm.action.path") &nbsp;&nbsp;&nbsp;&nbsp;
							$text.get('crm.careforaction.lb.imageshow')
		<img src='/$globals.getStylePath()/images/carefor/ok.gif'>$text.get('common.msg.DEFAULT_SUCCESS')
		<img src='/$globals.getStylePath()/images/carefor/up.gif'>$text.get('crm.careforaction.lb.over')
		<img src='/$globals.getStylePath()/images/carefor/error.gif'>$text.get('common.msg.DEFAULT_FAILURE')
		<img src='/$globals.getStylePath()/images/carefor/not.gif'>$text.get('crm.careforaction.lb.notrun')
						</td>		
					</tr>
				</thead>
				<tbody>
					#set($index = 0)
					#foreach ($bean in $list)
					#set($index = $index+1)
					<tr onMouseMove="setBackground(this,true);"
						onMouseOut="setBackground(this,false);">
						<td width="30" >
							$index
						</td>
						<td width="100" >
							$!clientMap.get($!bean.clientId) &nbsp;
						</td>
						<td width="100" >$!bean.careforName &nbsp;
						</td>
						<td width="100" >
							#foreach($enum in $globals.getEnumerationItems('actionStatus'))
								#if($enum.value == $bean.status) $enum.name #end
							#end
						</td>
						<td width="*" >
							#foreach($one in $bean.careforActionDels) 
								#set($astyle = "")
								#if("$!one.endDate"!="")  
									#set($timediff = $globals.getTimeDiff("$!one.createTime","$!one.endDate")) 
									#if( $timediff> 0)
										#set($astyle = "style='color:red;' title='$text.get('crm.careforaction.lb.overTime')$timediff $text.get('oa.calendar.day')' ")
									#end
								#end					
								<a href='javascript:openMessage("$one.id")' $astyle > $one.eventName 
								#if("$one.status" == "2")
									<img src='/$globals.getStylePath()/images/carefor/ok.gif'>
								#elseif("$one.status" == "3")
									<img src='/$globals.getStylePath()/images/carefor/up.gif'>
								#elseif("$one.status" == "4")
									<img src='/$globals.getStylePath()/images/carefor/error.gif'>
								#else
									<img src='/$globals.getStylePath()/images/carefor/not.gif'>
								#end	
									</a>
									&nbsp;&nbsp; 
							#end	
					
						</td>
					</tr>
					#end
				</tbody>
			</table>
		</div>
		<div class="oalistRange_pagebar">$!pageBar</div> 
	</form>
</body>
</html>
