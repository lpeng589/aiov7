<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("sms.receivednote.search")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/date.vjs"></script>
<script language="javascript">
function openInputDate(obj)
{
	c.showMoreDay = false;
	c.show(obj);
}
</script>
</head>

<body>

<form  method="post" scope="request" name="form" action="/NoteAction.do?operator=queryReceivedSMS&type=query">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("sms.receivednote.search")</div>
	<ul class="HeadingButton">
	
	#if($globals.getMOperation().query())
				<li><button type="submit"  onClick="form.submit()" class="b2">$text.get("common.lb.query")</button></li>
		#end
		</ul>
<div id="listRange_id">
		<div class="listRange_1">
			<li><span>$text.get("sms.note.sendMobile")：</span>
			  <input name="sendMobile" type="text" value="$!sendMobile"></li>
			<li><span>$text.get("sms.note.beginTime")：</span>
			  <input name="beginTime" type="text" value="$!beginTime" onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);" ></li>
			<li><span>$text.get("sms.note.endTime")：</span>		
			<input name="endTime" type="text" value="$!endTime" onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);"  ></li>
			
		</div>
		
		<div class="scroll_function_small_a" id="conter">
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
			<thead>
				<tr class="listhead">
					<td width="30" class="listheade"><IMG src="/style/images/down.jpg" border=0/></td>
					<td width="120" class="listhead30">$text.get("sms.note.business")</td>
					<td width="150">$text.get("sms.note.sendMobile")</td>
					<td width="150">$text.get("sms.note.time")</td>
					<td width="100">$text.get("sms.note.detail")</td>
				</tr>			
			</thead>
			<tbody> 
			    #set($count=0)
				#foreach ($row in $result )
				 #set($count=$count+1)
				<tr onDblClick="location.href='/NoteAction.do?keyId=$globals.get($row,0)&operator=queryReceivedSMS&type=detail'">
					<td align ="center" class="listheadonerow">$count</td>
					<td align="left">#set($exist=false) #foreach($erow in $globals.getEnumerationItems("sendSMSType"))
						 #if("$erow.value"=="$globals.get($row,1)") $erow.name #set($exist=true) #end #end
						 #if(!$exist) $globals.get($row,1) #end &nbsp;</td>
					<td align="left">$globals.get($row,2) &nbsp;</td>	
					<td align="left">$globals.get($row,3)</td>				
					<td align ="center"><a href='/NoteAction.do?keyId=$globals.get($row,0)&operator=queryReceivedSMS&type=detail&winCurIndex=$!winCurIndex'>$text.get("note.lb.detail")</a></td>
				</tr>
				#end
					</tbody>
			</table>

		</div>
<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-115;
oDiv.style.height=sHeight+"px";
//]]>
</script>
	<div class="listRange_pagebar"> $!pageBar</div>	
	</div>
</form> 
</body>
</html>
