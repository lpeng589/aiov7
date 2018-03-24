<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Welcome</title>
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.dragsort.js"></script>
<link rel="stylesheet" href="/$globals.getStylePath()/css/menu.css" type="text/css">
<script type="Text/Javascript" src="/js/MSIE.PNG.js"></script>
<script type="text/javascript" src="$globals.js("/js/welcomeHome.vjs","",$text)"></script>
<style type="text/css">
.menuSetDestBg,.changeIconBg{ behavior: url(iepngfix.htc) } 
</style> 
<script type="text/javascript">
function defaultpage(){ 
		#if("$!LoginBean.defaultPage"!="" && "$!request.getParameter('noDefault')" != "true")
			#if($LoginBean.operationMap.get("$!LoginBean.defaultPage").query())
				#set($strName=$globals.getModelNameByLinkAddress("$!LoginBean.defaultPage"))
				#if($globals.isExist("$!LoginBean.defaultPage","?"))
					mdiwin("$!LoginBean.defaultPage"+"&src=menu&name=$!strName","$!strName") ;
				#else
					mdiwin("$!LoginBean.defaultPage"+"?src=menu&name=$!strName","$!strName") ;
				#end			
			#end		
		#end
		
		#if("$!adviceMdiwin"!="" && $!adviceMdiwin.indexOf("mdiwin(")!=-1)
				$!adviceMdiwin
		#end
		
}
</script>
</head>

<body onload="fixPng(); defaultpage();" class="destBody" onkeydown="pagekeydown()">
	<input type="text" name="getFocus" id="getFocus" style="width:0px; height:0px">
	<!-- filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=$globals.get($row,3)) 可以解决IE6不透明的问题，但不解决也可以 -->
	<table height="100%" width="90%" border="0" cellspacing="0" cellpadding="0" style="table-layout: fixed;">
	<tr><td width="20px" valign="top">
	<div class="mtmoveLeft" ><a id="moveLeft"  onclick="move(true)"></a></div>
	</td>
	<td width="*" align="center" >
	<div id="conter" style="height:90%;width:90%;float:left;overflow:hidden;">
	<ul class="SystemTable" id="mainTable">
	#foreach ($row in $result )	
	#if("$!globals.get($row,2)" != "")	
	<li id="D_$globals.get($row,0)" oncontextmenu = "popSet('$globals.get($row,0)')" >		
		<a href="javascript:void(0)" myclick="mdiwin('$globals.get($row,2)','$globals.get($row,1)')">		
			<span><img src="$globals.get($row,3)"/></span>
			<div>$globals.get($row,1)</div> 
		</a>
  	</li>
  	#end			
	#end		
	</ul>
	</div>	
	</td>
	<td width="20px" valign="top">
	<div class="mtmoveRight" ><a id="moveRight"  onclick="move(false)"></a></div>
	</td>
	</tr>
	</table>
	<!-- 换图标 -->
	<div class="changeIconBg" id="changeIconBg" style="position:absolute;display:none;">
	</div>
	<div class="changeIcon" id="changeIcon" style="position:absolute;display:none;">
		<a class="left" href="javascript:void(0)" onclick="changeIconMove(true)"></a>
		<div class="mid" id="changeIconMid">
		<div class="midCon" id="changeIconMidCon" style="">
			
		</div> 	
		</div>
		<a class="right" href="javascript:void(0)" onclick="changeIconMove(false)"></a>
		<a class="close" href="javascript:void(0)" onclick="changeIconClose()"></a>
	</div>
	<!-- 右键加入我的桌面-->
	<div class="menuSetDest" id ="menuSet"  style="display:none">
	<div class="menuSetDestBg" id ="menuSetBg">
		<a href="javascript:void(0)" onclick="changePic()">$text.get("mydest.lb.changeIcon")</a><br/>
		<a href="javascript:void(0)" onclick="cancelDest()">$text.get("mydest.lb.del")</a>
	</div>
	</div>
</body>
</html>