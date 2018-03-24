<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="/$globals.getStylePath()/css/ListRange.css" rel="stylesheet" type="text/css">
<script language="javascript" src="/js/function.js"></script>

</head>
<script type="text/javascript">
	function disDiv(){
		document.getElementById("message").style.display = "none";
		document.getElementById("contentsDiv").style.display = "block";
	}
	
	
</script>
<base target="_self">
<body >
<form method="post" scope="request" name="form" id="form" action="/InCardAnnal.do">
<div id="listRange_id" align="center">
			<script type="text/javascript">
				var oDiv=document.getElementById("listRange_id");
				var sHeight=document.body.clientHeight-38;
				oDiv.style.height=sHeight+"px";
			</script>
	<div class="listRange_div_jag_hint" id="message" align="center">
		<div class="listRange_div_jag_div" ><span>$text.get("common.msg.message")</span></div>
		<div>
			<ul class="listRange_Prongix">
				<li style="width:64px;height:64px;background:url(../style/images/allbg.gif) no-repeat -121px -65px;"></li>
				<li><span>$text.get("com.checkOnWorkAttendance.msg.clew")</span></li>
			</ul>
		</div>
		<div style=" float:right; padding-bottom:15px;">                        
            <button name="button" type="button"  onclick="disDiv()" class="b2">$text.get("common.lb.back")</button>
		</div>
		
	</div>
	
	<div id="contentsDiv" style="display: none;height: 400px;">
	<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("com.checkOnWorkAttendance.importCardNote")</div>
	<ul class="HeadingButton">		
		
		<li>
						<button type="button" name="backList" title="Ctrl+Z"
							onClick="javascript:document.form.submit();" class="b2">$text.get("common.lb.back")
						</button>
					</li>
	</ul>
</div>
	$text.get("com.checkOnWorkAttendance.nonentityDeptNo"):
	<table border="0" width="500" cellpadding="0" cellspacing="0" class="listRange_list" >
				<thead style="text-align: center;">
					<tr>
						<td class="listheade" width="30" align="center">
								<span style="vertical-align:middle;"><IMG
										src="/$globals.getStylePath()/images/down.jpg" border=0 /> </span>
							</td>
						<td>$text.get("user.lb.employeeId")</td>
					</tr>
				</thead>
				<tbody id="dataTbody" style="text-align: center;">
					#foreach($employeeNo in $employeeNos)
						<tr>
							<td class="listheadonerow" align="center" width="30">
								$velocityCount
							</td>
							<td>$employeeNo</td>
						</tr>
					#end
				</tbody>
		</table></div>
</div>
</form>
</body>
</html>
