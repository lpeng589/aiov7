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
	var d = new Date();
	LaunchCalendar(obj,d);
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
	<li>
	<button type="button" onClick="location.href='/NoteAction.do?operator=queryReceivedSMS&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button>
	</li>
	</ul>
</div>
<div id="listRange_id">
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
				<td width="90%">#set($exist=false) #foreach($erow in $globals.getEnumerationItems("sendSMSType"))
						 #if("$erow.value"=="$globals.get($receivednote,0)") $erow.name #set($exist=true) #end #end
						 #if(!$exist) $globals.get($receivednote,0) #end &nbsp;
				</td>
		  </tr>
			<tr>
				<td class="oabbs_tr">$text.get("sms.note.sendMobile")：</td>
				<td>
				$globals.get($receivednote,1) &nbsp;
				</td>
			</tr>	
			<tr>
			 <td class="oabbs_tr">$text.get("sms.note.time")：</td>
			 <td>
			  $globals.get($receivednote,2) &nbsp;	 
			 </td>
			</tr>												
			<tr>
				<td class="oabbs_tr">$text.get("sms.note.content")：</td>
				<td>
             $globals.get($receivednote,3) &nbsp;	 
				</td>
			</tr>
		  </tbody>
		</table>
	  </div>	
</div>
</form> 
</body>
</html>
