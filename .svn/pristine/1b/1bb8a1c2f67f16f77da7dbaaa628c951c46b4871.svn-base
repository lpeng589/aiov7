<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link type="text/css" rel="stylesheet"  href="/$globals.getStylePath()/css/classFunction.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
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

function closeWin(winName){
	if('$!comeAdd' == '0'){
		if(typeof(winName) != "undefined" && winName != "" ){
			parent.jQuery.close(winName);
			return;
		}
		if(typeof(this.parent.win)=="undefined" && (typeof(parent.jQuery)=="undefined" || typeof(parent.jQuery.fn.closeActiveTab)=="undefined")){
			#if("$!fresh"=="dialog")
			window.close() ;
			var varKey = window.opener.winObj.parentWindow.popWindow.document.getElementById("keySearch") ;
			if(typeof(varKey)!="undefined" && varKey!=null){
				varKey.value="$!keySearch";
			}
			window.opener.winObj.parentWindow.popWindow.beforeSubmit();
			#elseif("$!fresh"=="open")
			window.opener.beforeSubmit();
			#else
				window.close() ;
			#end
			
			//crmDetailHandleMail()CRM详情页面的一个方法,用于详情发送邮件后不进入下面流程   
			if(window.opener  != null && typeof(window.opener.crmDetailHandleMail)=="undefined"&&typeof(window.opener)!="undefined"&&typeof(window.opener.beforeSubmit)!="undefined" && typeof(window.opener.beforeSubmit)!="unknown"){
				window.opener.beforeSubmit() ;
				window.close() ;
			}
		}else{
			if(typeof(this.parent.win)!="undefined"){
				this.parent.win.removewin(this.parent.win.currentwin);
			}else if(typeof(parent.jQuery)!="undefined" && typeof(parent.jQuery.fn.closeActiveTab)!="undefined"){
				this.parent.jQuery.fn.closeActiveTab();
			}else{
				window.close();
			}
		}
	}else if('$!comeAdd' == '1'){
		window.close();
	}
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
		alert("$!request.getParameter("pframeName")");
		#if($!pframeName&&($!pframeName=="bottomFrame"||$!pframeName=="f_mainFrame"))
		#set($BACK_URL=$BACK_URL.replace("location.href","window.parent.parent.frames[1].location.href"))
		#end
		$BACK_URL
	}
}
#if("$!MESSAGE_RELOGIN"=="true")
function reloginToUrl(){
	$("#reloadTitle").show();
	document.reLoadDataForm.submit();
}
top.reLogin(window);
#end	

if("$!MESSAGE_TYPE"=="0" && "$!MESSAGE_AUTOBACK"=="1"&&"$!directJump"!="true")
	setTimeout('back()',1400);

##confirm框
#if ($MESSAGE_TYPE==3)
$MESSAGE_HTML
#end

</script>
<style>
.blest1{width:64px;height:64px;background:url(/style/images/allbg.gif) no-repeat -118px 0;}
.blest2{width:64px;height:64px;background:url(/style/images/allbg.gif) no-repeat -118px 0;}
.error{width:64px;height:64px;background:url(/style/images/allbg.gif) no-repeat -118px -65px;}
.Prongix{width:64px;height:64px;background:url(/style/images/allbg.gif) no-repeat -118px -130px;}
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
<!-- 重新登陆时记录提交的数据 -->
#if("$!MESSAGE_RELOGIN"=="true") 
		<form method="post" scope="request" name="reLoadDataForm" action="$!RElOGINURL">
		<div style="display:none">
		#foreach($key in $!RElOGINDATA.keySet())
			#foreach($val in $!RElOGINDATA.get($key))
				<textarea  name="$key">$!val</textarea>
			#end
		#end
		</div>
		<div class="listRange_id" align="center">
		<div style="font-size: 25px;padding-top: 50px;display:none" id="reloadTitle">正在进行校验，请稍候！</div>
		</div>
		</form>
#else			
		<form method="post" scope="request" name="form" action="">
			<div class="listRange_id" align="center">
			<script type="text/javascript">
				var oDiv=document.getElementById("listRange_id");
				var sHeight=document.body.clientHeight-38;
				if(oDiv != null)
					oDiv.style.height=sHeight+"px";
			</script>
				<div class="listRange_div_jag_hint" style="height:auto;overflow:hidden;">
					<div class="listRange_div_jag_div">
						<span>$tishi</span>
					</div>
					<div style="overflow:hidden;">
						<ul class="listRange_Prongix" style="padding:0 0 0 0;">
							<li style="width:65px;">
								<div class="$imgName"></div>
							</li>
							<li style="width:300px;">
								<span style="margin:15px 0 0 10px;">
									  	$MESSAGE_CONTENT
								</span>
							</li>
						</ul>
					</div>
					#if("$!LAST_SETTLEPERIOD"=="true")
					<div style="overflow:hidden;">
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
						<button name="button" type="button" onClick="closeYear()" class="hBtns">
							$text.get("common.lb.ok")
						</button>
					</div>
					#elseif("$!AUTO_JUMP"=="true")
					<script>function gouURL(){	
                this.parent.parent.location='/loginAction.do?operation=$globals.getOP("OP_LOGOUT")';
                }
                setTimeout("gouURL()",3000);
                </script>
					#else
					<div style="float:right; padding:10px 10px 15px 0; ">

					#if($noback)
						<button type="button" onClick="closeWin('$request.getParameter("popWinName")');" class="hBtns">$text.get("common.lb.close")</button>
					#else
						#if($noBackButton)
						#elseif ($BACK_URL == "")
						<button name="button" type="button" onClick="history.go(-1)" class="hBtns">
							$text.get("common.lb.back")
						</button>
						#else
						<button name="button" type="button" onClick="$BACK_URL" class="hBtns">
							#if("$!BUTTON"=="CLOSE")
							$text.get("common.lb.close")
							#else
							$text.get("common.lb.back")
							#end
						</button>
						#end
					#end
						
					</div>

					#end

				</div>
			</div>
		</form>
	#end ##非重新登陆框	
#else
</script>
<body onLoad="back();">
#end
	</body>
#end
</html>
