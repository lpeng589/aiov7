<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="/$globals.getStylePath()/css/ListRange.css" rel="stylesheet" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript">
	function winClose(){
		window.close();
	}
</script>
<style>
.blest1 {
	width:64px;
	height:64px;
	background:url(../style/images/allbg.gif) no-repeat -118px 0;
}
.blest2 {
	width:64px;
	height:64px;
	background:url(../style/images/allbg.gif) no-repeat -118px 0;
	
}
.error {
	width:64px;
	height:64px;
	background:url(../style/images/allbg.gif) no-repeat -118px -65px;
}
.Prongix {
	width:64px;
	height:64px;
	background:url(../style/images/allbg.gif) no-repeat -118px -130px;
}


</style>
</head>
	#set($messageType=3)
  #if ($messageType==0) 
	#set($imgName="images/bleSt.gif")
	#set($tishi=$text.get("common.msg.successdialog"))
  #elseif ($messageType==1) 
	#set($imgName="images/error.gif")
	#set($tishi=$text.get("common.msg.errrordialog"))
  #else
	#set($imgName="images/Prongix.gif")
	#set($tishi=$text.get("common.msg.messagedialog"))
  #end 
<base target="_self">
<body onLoad="showStatus();this.focus();" onkeydown="down();">

<div id="listRange_id" align="center">
			<script type="text/javascript">
				var oDiv=document.getElementById("listRange_id");
				var sHeight=document.body.clientHeight-38;
				oDiv.style.height=sHeight+"px";
			</script>
	<div style="height:160px; width:400px;border:1px solid #ECECEC;margin-top:70px;background:#FFFFFF;">
		<div class="listRange_div_jag_div"><span>$tishi</span></div>
		<div class="listRange_Prongix">
				<li><div class="$imgName"></div></li>
				<li><span>$text.get("call.message.failure.nofind")</span></li>
		</div>
		<div style=" float:right; padding-bottom:15px;">
		                             
           <button name="button" type="button"  onclick="winClose()" class="b2">$text.get("common.lb.back")</button>

		</div>
	</div>
</div>
</body>
</html>
