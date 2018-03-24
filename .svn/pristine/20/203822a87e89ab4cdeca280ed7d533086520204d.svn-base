<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("sms.sendnote.search")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/date.vjs"></script>

<script language="javascript">
function openInputDate(obj)
{
	c.showMoreDay = false;
	c.show(obj);
}
function deleteSMS(){
	document.form.action="/NoteAction.do?operator=delSMS" ;
	if(!sureDel('keyId')){
		return false ;
	}
}
function repeatSend(){
	document.form.action= "/NoteAction.do?operator=sendSMS&repeatSend=true" ;
}
</script>
</head>

<body>

<form  method="post" scope="request" name="form" action="/NoteAction.do?operator=querySendSMS&type=query">
<input type="hidden" name="operation" value="querySendSMS"/>
<input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("sms.sendnote.search")</div>
	
	<ul class="HeadingButton">
	
	#if($globals.getMOperation().query())
					<li><button type="submit"  onClick="form.submit()" class="b2">$text.get("common.lb.query")</button></li>
					<li><button name="clear" type="botton" onClick="clearForm(form);" class="b2">重置</button></li>
				#end
				#if($globals.getMOperation().delete())
				<li><button type="submit" onClick="return deleteSMS();" class="b2">$text.get("common.lb.del")</button></li>
				#end
				#if($globals.getMOperation().query())
				<li><button type="submit" onClick="repeatSend();" class="b2">$text.get("com.note.anew")</button></li>
				#end
		</ul>		
				
</div>
<div id="listRange_id">
		<div class="listRange_1">
			<li><span>$text.get("sms.note.receiveMobile"):</span>
			  <input name="receiveMobile" type="text" value="$!receiveMobile"></li>
			<li><span>$text.get("messsage.lb.sender"):</span>
			  <input name="sender" type="text" value="$!sender"></li>
			<li><span>$text.get("sms.note.beginTime"):</span>
			  <input name="beginTime" type="text" value="$!beginTime" onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);" ></li>
			<li><span>$text.get("sms.note.endTime"):</span>		
			<input name="endTime" type="text" value="$!endTime" onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);"  ></li>
			<li><span>$text.get("sms.note.status"):</span><select name="state" >
					<option value="" ></option>
					<option value="2" #if("2"=="$!state") selected #end>发送成功</option>
					<option value="-1" #if("-1"=="$!state") selected #end>发送失败</option>
					<option value="0" #if("0"=="$!state") selected #end>未发短信</option>
					</select></li>				
		</div>
		
		<div class="scroll_function_small_a" id="conter">
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
			<thead>
				<tr class="listhead">
					<td width="30" class="listheade">&nbsp;</td>
					<td width="30" class="listheade"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
					<td width="120" class="listhead30">发送人</td>
					<td width="130">发送时间</td>
					<td width="150">接收人</td>
					<td width="*">内容</td>
					<td width="100">状态</td>		
				</tr>			
			</thead>
			<tbody> 
				#foreach ($row in $result )
				<tr onDblClick="location.href='/NoteAction.do?keyId=$globals.get($row,0)&operator=querySendSMS&type=detail'">
					<td width="30" >$velocityCount</td>
					<td align ="center" ><input type="checkbox" name="keyId" value="$globals.get($row,1):$globals.get($row,0)"/></td>
					<td align="left">$!globals.get($row,3) &nbsp;</td>
					<td align="left">$!globals.get($row,4)&nbsp;</td>
					<td align="left" title="$!globals.get($row,5)#if("$!globals.get($row,6)" != "") ($!globals.get($row,6)) #end">$!globals.get($row,5)#if("$!globals.get($row,6)" != "") ($!globals.get($row,6)) #end &nbsp;</td>	
					<td align="left">$!globals.get($row,7)</td>				
					<td align="left">$!globals.get($row,2) &nbsp;</td> 
				</tr>
				#end
					</tbody>
			</table>
		</div>
		<div class="listRange_pagebar"> $!pageBar </div>	
		</div>
<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-140;
oDiv.style.height=sHeight+"px";
//]]>
</script>
</form> 
</body>
</html>
