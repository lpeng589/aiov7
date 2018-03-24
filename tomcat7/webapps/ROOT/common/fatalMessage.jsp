<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="refresh" content="2; url=fatalMessage.jsp?from=$!request.getParameter('from')">
<title>$text.get("common.lb.aio2")</title>
<link href="/$globals.getStylePath()/css/ListRange.css" rel="stylesheet" type="text/css">
<script language="javascript" src="/js/function.js"></script>

#if($globals.getSystemState().lastErrorCode == 0 && ($globals.getSystemState().dogState == 0 || $globals.getSystemState().dogState == 1))
	#if($request.getParameter("from") == "index")
		  <script language="javascript">window.location.href = '../login.jsp'; </script>
	#else
		<script language="javascript">window.location.href = '/MenuQueryAction.do?sysType=$!LoginBean.defSys'; </script>
	#end
	
#elseif($globals.getSystemState().dogState == 4 && $globals.getSystemState().encryptionType==0)
	<script language="javascript">window.location.href = '/RegisterAction.do?regFlag=1&step=two&encryptionType=0'; </script>	
#elseif($globals.getSystemState().dogState == 4)
	<script language="javascript">window.location.href = '/RegisterAction.do?regFlag=2'; </script>	
#elseif($globals.getSystemState().dogState == 10)
	<script language="javascript">window.location.href = '/UpgradeAction.do?type=1&exig=true'; </script>	
#elseif($globals.getSystemState().dogState == 12)
	<script language="javascript">window.location.href = '/RegisterAction.do?regFlag=2'; </script>	
#elseif($globals.getSystemState().dogState == 11)
	<script language="javascript">window.location.href = '/RegisterAction.do?regFlag=2'; </script>	
#elseif($globals.getSystemState().dogState == 6)
	<script language="javascript">window.location.href = '/RegisterAction.do?regFlag=2'; </script>	
#elseif($globals.getSystemState().dogState == 7)
	<script language="javascript">window.location.href = '/RegisterAction.do?regFlag=2'; </script>
#elseif($globals.getSystemState().dogState == 8)
	<script language="javascript">window.location.href = '/RegisterAction.do?regFlag=2'; </script>		
#elseif($globals.getSystemState().dogState == 13)
	<script language="javascript">window.location.href = '/RegisterAction.do?regFlag=2'; </script>		
#elseif($globals.getSystemState().dogState == 14)
	<script language="javascript">window.location.href = '/RegisterAction.do?regFlag=2'; </script>		
#elseif($globals.getSystemState().dogState == 15)
	<script language="javascript">window.location.href = '/RegisterAction.do?regFlag=2'; </script>		
#elseif($globals.getSystemState().dogState == 2 || $globals.getSystemState().dogState == 16 || 
	$globals.getSystemState().dogState == 17 || $globals.getSystemState().dogState == 18 || 
	$globals.getSystemState().dogState == 19|| $globals.getSystemState().dogState == 30
	|| $globals.getSystemState().dogState == 31|| $globals.getSystemState().dogState == 32)
	<script language="javascript">window.location.href = '/RegisterAction.do?regFlag=2'; </script>								
#end
<style>
.Prongix {
	width:64px;
	height:64px;
	background:url(/style/images/allbg.gif) no-repeat -121px -130px;
}
</style>
</head>
<base target="_self">
<body onLoad="showStatus();" >
<form method="post" scope="request" name="form" action="">
<input type="hidden" name="test" value="">
	<div id="listRange_id" align="center">
<script type="text/javascript">
var oDiv=document.getElementById("listRange_id");
var sHeight=document.body.clientHeight-38;
oDiv.style.height=sHeight+"px";
</script>
	<div class="listRange_div_jag_hint">
		<div class="listRange_div_jag_div"><span>$text.get("common.msg.messagedialog")</span></div>
			<div class="listRange_Prongix">
				<li><div class="Prongix"></div></li>
				<li><span  style="text-align: left;">
				#if($globals.getSystemState().lastErrorCode != 0) 
					$text.get("common.msg.dbError.update") 
				#elseif($globals.getSystemState().dogState == 2) 
					$text.get("common.msg.dogUserError",["$globals.getSystemState().reluserNum","$globals.getSystemState().userNum"])  
				#elseif($globals.getSystemState().dogState == 3) 
					$text.get("common.msg.dogFunctionError")  
				#elseif($globals.getSystemState().dogState == 5) 
					$text.get("common.msg.dogError") 
				#elseif($globals.getSystemState().dogState == 6) 
					$text.get("common.msg.dogError") 
				#elseif($globals.getSystemState().dogState == 7) 
					$text.get("common.msg.evaluateTimeError") 
				#elseif($globals.getSystemState().dogState == 8) 
					$text.get("common.msg.evaluateBisError") 
				#elseif($globals.getSystemState().dogState == 20) 
					$text.get("common.msg.systemRestart") 	
				#elseif($globals.getSystemState().dogState == 9) 
					The dog has locked .  
				#end</span></li>
			</div>
		<div style="float:right; padding-bottom: 15px;">	
		#if($globals.getSystemState().dogState == 2) 
			<button name="button" type="button" onClick="location.href = '/UpgradeAction.do?type=2&exig=true';" >$text.get("common.lb.upgrade")</button>
		#elseif($globals.getSystemState().dogState == 3) 
			<button name="button" type="button" onClick="location.href = '/UpgradeAction.do?type=4&exig=true';" >$text.get("common.lb.upgrade")</button>
		#elseif($globals.getSystemState().dogState == 6) 
			<button name="button" type="button" onClick="location.href = '/RegisterAction.do?regFlag=1';" >$text.get("common.lb.evaluate")</button>
		#end
			
              <button name="button" type="button" onClick="location.reload();" >$text.get("common.lb.refresh")</button>
		</div>
	</div>
</div>
</form>
</body>
</html>
