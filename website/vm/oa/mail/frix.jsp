

<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>flex</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/menu.css" type="text/css"> 
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript"> 
function ShowOrHideEmpFrame2(obj)
{ 
	var frm = window.parent.frames;
    if(obj.isShowLeft=="true")
    {
        parent.showLeft();
        obj.isShowLeft = "false";
		obj.src="/$globals.getStylePath()/images/flex_shrink.gif";
     }
     else
     {
         parent.closeLeft();
         obj.isShowLeft = "true";
		 obj.src="/$globals.getStylePath()/images/flex_reach.gif";
     }
  	//frm.frames["mainFrame"].location.reload();
}
</script>

</head>

<body class="flex2" scroll="no">
	<table border="0" height="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td id="menu">
<img id="flexId" style="margin-left:1px;cursor:pointer;" src="/$globals.getStylePath()/images/flex_shrink.gif" isShowLeft = "false" border="0" onClick="ShowOrHideEmpFrame2(this)"  isShowLeft="false" style="cursor:hand" title="$text.get('common.title.closeUserList')" />
			</td>
		</tr>
</table>
</body>
</html>

