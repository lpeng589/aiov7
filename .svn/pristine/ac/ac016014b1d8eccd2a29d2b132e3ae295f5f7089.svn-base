<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>客服数据库操作</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script> 
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

		
<script type="text/javascript">		
	function beforeSubmit(){
		if(document.getElementById("newfileName").value==""){
			alert("文件为空");
		}else{
			document.form.submit();
		}		
	}
	$!msg

</script>
</head>

<body onLoad="showtable('tblSort');showStatus();">

<form  method="post" scope="request" name="form" action="/ServiceAction.do?opType=fileUpload&exec=true" enctype="multipart/form-data">
 <input type="hidden" name="fileName" value="$!fileName">
 <input type="hidden" name="path" value="$!path">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">

	<input type="file" name="newfileName"  id="newfileName" style="width:350px;height:25px; margin: 20px; vertical-align:bottom" class="text" />

</form>

</body>
</html>
