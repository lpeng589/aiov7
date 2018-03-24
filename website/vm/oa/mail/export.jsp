<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title></title>
		<link href="/$globals.getStylePath()/css/ListRange.css"
			rel="stylesheet" type="text/css">
		<script language="javascript" src="/js/function.js"></script>
		<script language="javascript">
if(typeof(top.junblockUI)!="undefined"){
	top.junblockUI();
}

function back(){
#if($noback)
	closeWin();
#else
	$BACK_URL;
#end	
}
function closeWin(){
if('$!comeAdd' == '0')
	if(typeof(this.parent.win)=="undefined"){
		window.close() ;
		#if("$!fresh"=="true")
		var varKey = window.opener.winObj.parentWindow.popWindow.document.getElementById("keySearch") ;
		if(typeof(varKey)!="undefined"){
			varKey.value="$keySearch";
		}
		window.opener.winObj.parentWindow.popWindow.beforeSubmit();
		#end 
	}else{
		this.parent.win.removewin(this.parent.win.currentwin);
	}
	
else if('$!comeAdd' == '1')
	window.close();
}
#if(!$immediately)
function closeYear(){
var yc=document.getElementsByName("yearClose");
 for(var i=0;i<yc.length;i++){
   if(yc[i].checked){
     if(yc[i].value=="yes")
	 {
	  location.href='/SysAccAction.do?type=yearSettleAcc&src=menu';return true;
	 }else{
	  $BACK_URL;return true;
	 }
	 break;
   }
 }
 alert('$text.get("tip.plese.select")');
 return false;

}



function down(){
	if(event.ctrlKey && (event.keyCode >= 49 && event.keyCode <= 57)){
		//top.$("#top_link_"+(event.keyCode-48)).focus(); 
		
		top.$("#getFocus").focus();
		top.$("#top_link_"+(event.keyCode-48)).trigger("click");		
		top.menuObject = top.$("#top_link_"+(event.keyCode-48));
		event.returnValue=false;	
	}
	if(event.ctrlKey&&event.keyCode==90){
		$BACK_URL;
	}
}

if("$!MESSAGE_TYPE"=="0" && "$!MESSAGE_AUTOBACK"=="1")
	setTimeout('back()',1400);
	
#if ($MESSAGE_TYPE==3)
$MESSAGE_HTML
#end

#if($!directJump)
$BACK_URL
#end
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
	#set($tishi=$text.get("common.msg.successdialog"))
	#set($imgName="blest1") #if ($MESSAGE_TYPE==0) #set($imgName="blest2")
	#set($tishi=$text.get("common.msg.successdialog")) #elseif
	($MESSAGE_TYPE==1) #set($imgName="error")
	#set($tishi=$text.get("common.msg.errrordialog")) #else
	#set($imgName="Prongix")
	#set($tishi=$text.get("common.msg.messagedialog")) #end
	#if ($MESSAGE_TYPE!=3)
	<base target="_self">
	<body onLoad="showStatus();this.focus();" onkeydown="down();">
		<form method="post" scope="request" name="form" action="">
			<div class="listRange_id" align="center">
			<script type="text/javascript">
				var oDiv=document.getElementById("listRange_id");
				var sHeight=document.body.clientHeight-38;
				if(oDiv != null)
					oDiv.style.height=sHeight+"px";
			</script>
				<div class="listRange_div_jag_hint">
					<div class="listRange_div_jag_div">
						<span>$tishi</span>
					</div>
					<div>
						<ul class="listRange_Prongix">
							<li>
								<div class="$imgName"></div>
							</li>
							<li>
								<span>
								<a target="_blank" href="/ReadFile?tempFile=path&path=&fileName=$fileName" onClick="$BACK_URL">
									  	$MESSAGE_CONTENT 
								</a>	  	
								</span>
							</li>
						</ul>
					</div>
					#if("$!LAST_SETTLEPERIOD"=="true")
					<div>
						<ul class="listRange_Prongix">
							<li>
								<span>$text.get("settle.lastperiod.tip") </span>
							</li>
						</ul>
					</div>
					<div>
						<input type="radio" name="yearClose" value="yes" />
						$text.get("common.lb.yes") &nbsp;
						<input type="radio" name="yearClose" value="no" />
						$text.get("common.lb.no") &nbsp;
						<button name="button" type="button" onClick="closeYear()"
							class="b2">
							$text.get("common.lb.ok")
						</button>
					</div>
					#elseif("$!AUTO_JUMP"=="true")
					<script>function gouURL(){	
                this.parent.parent.bottomFrame.location='/loginAction.do?operation=$globals.getOP("OP_LOGOUT")';
                }
                setTimeout("gouURL()",3000);
                </script>
					#else
					<div style=" float:right; padding-bottom:15px; ">

					#if($noback)
						<button type="button" onClick="closeWin();" class="b2">$text.get("common.lb.close")</button>
					#else
						#if ($BACK_URL == "")
						<button name="button" type="button" onClick="history.go(-1)"
							class="b2">
							$text.get("common.lb.back")
						</button>
						#else
						<button name="button" type="button" onClick="$BACK_URL" class="b2">
							$text.get("common.lb.back")
						</button>
						#end
					#end
						
					</div>

					#end

				</div>
			</div>
		</form>
#else
</script>
<body onLoad="back();">
#end
	</body>
#end
</html>
