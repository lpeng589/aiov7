<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
	document.form.action="/TelQueryAction.do?operator=delete" ;
	if(!sureDel('keyId')){
		return false ;
	}
}

</script>
</head>

<body>

<form  method="post" scope="request" name="form" action="/TelQueryAction.do?operator=telList">
<input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("tel.lb.callList")</div>
	
	<ul class="HeadingButton">	
				#if($globals.getMOperation().query())
					<li><button type="submit"  onClick="form.submit()" class="b2">$text.get("common.lb.query")</button></li>
					<li><button name="clear" type="botton" onClick="clearForm(form);" class="b2">$text.get("common.lb.clear")</button></li>
				#end
				#if($globals.getMOperation().delete())
				<li><button type="submit" onClick="return deleteSMS();" class="b2">$text.get("common.lb.del")</button></li>
				#end
		</ul>		
				
</div>
<div id="listRange_id">
		<div class="listRange_1">
			
			<li><span>$text.get("tel.lb.hostCall"):</span>
			  <input name="mainCall" type="text" value="$!TelSearchForm.mainCall"></li>
			<li><span>$text.get("tel.lb.passCall"):</span>
			  <input name="called" type="text" value="$!TelSearchForm.called"></li>
			<li><span>$text.get("sms.note.beginTime"):</span>
			  <input name="startTime" type="text" value="$!TelSearchForm.startTime" onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);" ></li>
			<li><span>$text.get("sms.note.endTime"):</span>		
			<input name="endTime" type="text" value="$!TelSearchForm.endTime" onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);"  ></li>
						
		</div>
		
		<div class="scroll_function_small_a" id="conter">
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
			<thead>
				<tr class="listhead">
					<td width="30" class="listheade"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
					<td width="100" >$text.get("tel.lb.hostuser")</td>
					<td width="100" >$text.get("tel.lb.hostCall")</td>
					<td width="130">$text.get("tel.lb.callTimes")</td>
					<td width="150">$text.get("sms.note.status")</td>
					<td width="110">$text.get("tel.lb.passCall")</td>
					<td width="50">$text.get("tel.com.lb.ChargeRash")</td>
					<td width="130">$text.get("tel.lb.CallDown")</td>
					<td width="50">$text.get("tel.lb.calltime")</td>
					<td width="50">$text.get("mrp.lb.price2")</td>
					<td width="100">$text.get("sms.note.detail")</td>
									
				</tr>			
			</thead>
			<tbody> 
			#set($lt = 0)
			#set($lm = 0)
			#foreach ($row in $result )
				#set($rCount = 1)
				#if($!globals.get($row,8).size() > 1)
				#set($rCount = $!globals.get($row,8).size())
				#end
				<tr onDblClick="location.href='/TelAction.do?keyId=$globals.get($row,0)&operator=telDetail&winCurIndex=$!winCurIndex'">
					<td align ="center" valign="top" rowspan="$rCount" class="listheadonerow"><input type="checkbox" name="keyId" value="$globals.get($row,0)"/></td>
					<td align="left" valign="top" rowspan="$rCount"> $!globals.get($row,6) &nbsp;</td>
					<td align="left" valign="top" rowspan="$rCount"> $!globals.get($row,2) &nbsp;</td>
					<td align="left" valign="top" rowspan="$rCount"> $!globals.get($row,3) &nbsp;</td> 
					<td align="left" valign="top" rowspan="$rCount"> $!globals.get($row,5) &nbsp;</td>
					#if($!globals.get($row,8).size() == 0)
					<td align="left">&nbsp; </td>
					<td align="left">&nbsp; </td>
					<td align="left">&nbsp;  </td>
					<td align="right">&nbsp;  </td>
					<td align="right">&nbsp; </td>  
					#else
					#set($lt = $lt+$!globals.get($!globals.get($row,8).get(0),3))
					#set($lm = $lm+$!globals.get($!globals.get($row,8).get(0),4))
					<td align="left"> $!globals.get($!globals.get($row,8).get(0),1) &nbsp;</td>
					<td align="left"> $!globals.get($!globals.get($row,8).get(0),2) &nbsp;</td>
					<td align="left"> $!globals.get($!globals.get($row,8).get(0),5) &nbsp;</td>
					<td align="right"> $!globals.get($!globals.get($row,8).get(0),3) &nbsp;</td>
					<td align="right"> $!globals.get($!globals.get($row,8).get(0),4) &nbsp;</td>  
					#end 
					<td align ="center" valign="top" rowspan="$rCount"><a href='/TelAction.do?keyId=$globals.get($row,0)&operator=telDetail&winCurIndex=$!winCurIndex'>$text.get("note.lb.detail")</a></td>
				</tr>
				#if($rCount > 1)
				#set($rCount = $rCount+ -1)
				#foreach($rownum in [1..$rCount])
				#set($lt = $lt+$!globals.get($!globals.get($row,8).get($rownum),3))
				#set($lm = $lm+$!globals.get($!globals.get($row,8).get($rownum),4))
				<tr onDblClick="location.href='/TelAction.do?keyId=$globals.get($row,0)&operator=telDetail&winCurIndex=$!winCurIndex'">					
					<td align="left"> $!globals.get($!globals.get($row,8).get($rownum),1) &nbsp;</td>
					<td align="left"> $!globals.get($!globals.get($row,8).get($rownum),2) &nbsp;</td>
					<td align="left"> $!globals.get($!globals.get($row,8).get($rownum),5) &nbsp;</td>
					<td align="right"> $!globals.get($!globals.get($row,8).get($rownum),3) &nbsp;</td>
					<td align="right"> $!globals.get($!globals.get($row,8).get($rownum),4) &nbsp;</td>   
				</tr>
				#end
				#end
			#end
			
				<tr >
					<td align="right" colspan="8">$text.get("common.lb.total")</td>
					<td align="right"> $lt &nbsp;</td>
					<td align="right"> $lm &nbsp;</td>  
					
					<td align ="center" valign="top" >&nbsp;</td>
				</tr>
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
