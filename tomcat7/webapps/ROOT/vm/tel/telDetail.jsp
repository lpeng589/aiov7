<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
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

<form  method="post" scope="request" name="form" action="/TelQueryAction.do?operator=telList">
<input type="hidden" name="winCurIndex" value="$!winCurIndex">
<input type="hidden" name="keyId" value="$globals.get($result,0)">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("tel.title.BuyOrderDetail")</div>
		<ul class="HeadingButton">
	<li>
	<button type="button" onClick="location.href='/TelQueryAction.do?operator=telList&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button>
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
				<td width="20%" class="oabbs_tr">$text.get("tel.lb.hostuser")：</td>
				<td width="90%"> $!globals.get($result,13) &nbsp;
				</td>
		  </tr>
			<tr>
				<td width="20%" class="oabbs_tr">$text.get("tel.lb.hostCall")：</td>
				<td width="90%"> $!globals.get($result,2) &nbsp;
				</td>
		  </tr>
		  <tr>
				<td width="20%" class="oabbs_tr">$text.get("tel.lb.callTimes")：</td>
				<td width="90%"> $!globals.get($result,5) &nbsp;
				</td>
		  </tr>
		  <tr>
				<td width="20%" class="oabbs_tr">$text.get("tel.lb.passCallDescribe")：</td>
				<td width="90%"> $!globals.get($result,4) &nbsp;
				</td>
		  </tr>
		  <tr>
				<td width="20%" class="oabbs_tr">$text.get("sms.note.status")：</td>
				<td width="90%"> $!globals.get($result,7) &nbsp;
				</td>
		  </tr>
		  <tr>
		  		<td width="20%" class="oabbs_tr">$text.get("commom.lb.buyOrder")：</td>
				<td width="90%" class="oabbs_tl"> 
				<table width="50%" align="left" border="0" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
					<thead>
					<tr>
						<td width="20%" class="oabbs_tc">$text.get("tel.lb.passCall")</td>
						<td width="10%" class="oabbs_tc">$text.get("tel.com.lb.ChargeRash")</td>
						<td width="30%" class="oabbs_tc">$text.get("tel.lb.CallDown")</td>
						<td width="10%" class="oabbs_tc">$text.get("tel.lb.calltime")</td>
						<td width="10%" class="oabbs_tc">$text.get("mrp.lb.price2")</td>
					</tr>
					</thead>
					<tbody>
					#set($lt = 0)
					#set($lm = 0)
					#foreach($row in $resultList)
					#set($lt = $lt+$!globals.get($row,10))
					#set($lm = $lm+$!globals.get($row,11))
					<tr>
						<td > $!globals.get($row,8) &nbsp;</td>
						<td  class="oabbs_tr"> $!globals.get($row,9) &nbsp;</td>
						<td  > $!globals.get($row,12) &nbsp;</td>
						<td  class="oabbs_tr"> $!globals.get($row,10) &nbsp;</td>
						<td  class="oabbs_tr"> $!globals.get($row,11) &nbsp;</td>
					</tr>
					#end
					<tr>
						<td colspan="3" class="oabbs_tr"> $text.get("common.lb.total") &nbsp;</td>
						<td  class="oabbs_tr"> $lt &nbsp;</td>
						<td  class="oabbs_tr"> $lm &nbsp;</td>
					</tr>
					</tbody>
				</table>
				</td>
		  </tr>
			
		  </tbody>
		</table>
	  </div>	
</div>
</form> 
</body>
</html>
