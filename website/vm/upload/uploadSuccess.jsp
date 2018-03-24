<html>
<head>
<meta name="renderer" content="webkit">
<script language="javascript">
	#if("$from" == "fck")
	window.location.href="/fckeditor/editor/dialog/fck_image.jsp?url=$fileName";
	#else
	window.parent.returnValue = '$fileName'; 
	window.close();
	#end
</script>
</head>

<body onLoad="showStatus();">

</body>
</html>
