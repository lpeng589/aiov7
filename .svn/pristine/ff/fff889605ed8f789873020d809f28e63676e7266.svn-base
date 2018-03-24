<html>
<head>
<meta name="renderer" content="webkit">
<title>ͼ图片管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" type="text/css" href="/vm/oa/album/ui/tongda/theme/11/style.css">
<!-- <script src="/vm/album/ui/tongda/ccorrect_btn.js"></script> -->
<script language="javascript" src="/js/public_utils.js"></script>
<SCRIPT LANGUAGE=javascript>
		/*
		$(window).bind('load', function() {
			$('img').bind("contextmenu", function(e){ return false; })
		});
		*/
		function stop(){ 
			return false; 
		} 
		document.oncontextmenu=stop; 
</SCRIPT> 
<script>
	function bbimg(o){
	  var zoom=parseInt(o.style.zoom, 10)||100;
	  zoom+=event.wheelDelta/12;
	  if(zoom>0)
	     o.style.zoom=zoom+'%';
	  return false;
	}
	
	function mouseMove(ev){
		ev = ev || window.event;
		var mousePos = mouseCoords(ev);
		if (mousePos.x < screen.width/2) {
			parent.open_control.open_pic(-1);
		} else {
			parent.open_control.open_pic(1);
		}
	}

</script>
</head>
<body topmargin=3 style="background-color:gray" leftmargin=0>
	<table id="pictable" border="0" width=100% height=100% title="鼠标滚轮缩放，点击图片翻页" topmargin=3 cellpadding=0 cellspacing=0 onmousewheel="return bbimg(this)" onClick="mouseMove();">
		<tr>
			<td align="center" valign="center" class=big height=20>
				<font color="white"><b><div id="file_name"></div></b></font>
		    </td>
		</tr>
		<tr>
			<td align="center" valign="center">
			   	 <div id="div_image">
			   	 	<img src="/vm/oa/album/ui/image/ajax.gif">
			     </div>
		  	</td>
		</tr>
	</table>
	<script type="text/javascript">
	document.onkeydown = keyDown; 
	function keyDown(e) { 
		var iekey=event.keyCode; 
		action(iekey);
	} 
	function action(iekey) { 
		if(iekey==37) {
			control_pic(1)
		} 
		if(iekey==39) { 
			control_pic(2)
		} 
	}
</script> 
</body>
</html>