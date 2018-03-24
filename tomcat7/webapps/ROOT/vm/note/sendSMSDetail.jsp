<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("sms.sendnote.search")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/date.vjs"></script>
<script language="javascript">
function openInputDate(obj)
{
	var d = new Date();
	LaunchCalendar(obj,d);
}

function repeatSend(){
	document.form.action= "/NoteAction.do?operator=sendSMS&repeatSend=true" ;
}

</script>
</head>

<body>

<form  method="post" scope="request" name="form" action="/NoteAction.do?operator=querySendSMS&type=query">
<input type="hidden" name="winCurIndex" value="$!winCurIndex">
<input type="hidden" name="keyId" value="$globals.get($sendnote,6)">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("sms.sendnote.search")</div>
		<ul class="HeadingButton">
	<li>
	<button type="button" onClick="location.href='/NoteAction.do?operator=querySendSMS&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button>
	#if($globals.getMOperation().query())
		<li><button type="submit" onClick="repeatSend();" class="b2">$text.get("com.note.anew")</button></li>
	#end
	</li>
	</ul>
</div>
<div id="listRange_id" align="center">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
    <div class="scroll_function_small_a">
		<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
		<thead>
			<tr>
				<td colspan="2" class="oabbs_tl">&nbsp;</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td width="20%" class="oabbs_tr">$text.get("sms.note.business")：</td>
				<td width="90%">$globals.getEnumerationItemsDisplay("sendSMSType","$globals.get($sendnote,0)") &nbsp;
				</td>
		  </tr>
			<tr>
				<td class="oabbs_tr">$text.get("sms.note.receiveMobile")：</td>
				<td>
				$globals.get($sendnote,1) &nbsp;
				</td>
			</tr>	
			<tr>
			 <td class="oabbs_tr">$text.get("sms.note.time")：</td>
			 <td>
			  $globals.get($sendnote,2) &nbsp;	 
			 </td>
			</tr>												
			<tr>
				<td class="oabbs_tr">$text.get("sms.note.status")：</td>
				<td>
            #set($exist=false) #foreach($erow in $globals.getEnumerationItems("sendSMSStatus"))
						 #if("$erow.value"=="$globals.get($sendnote,3)") $erow.name #set($exist=true) #end #end
						 #if(!$exist) $globals.get($sendnote,3) #end
					    &nbsp; 
				</td>
			</tr>
			<tr>
				<td class="oabbs_tr">$text.get("sms.note.content")：</td>
				<td>
             $globals.get($sendnote,4) &nbsp;	 
				</td>
			</tr>
			<tr>
				<td class="oabbs_tr">$text.get("sms.lb.feeTimes")：</td>
				<td>
             $globals.get($sendnote,7) &nbsp;	 
				</td>
			</tr>
			<tr>
				<td class="oabbs_tr">$text.get("oa.common.remark")：</td>
				<td>
             $!globals.get($sendnote,5) &nbsp;	 
				</td>
			</tr>
			
		  </tbody>
		</table>
	  </div>	
</div>
</form> 
</body>
</html>
