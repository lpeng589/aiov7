<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("roleadmin.title.add")</title>
</head>
<body>
<div style=" margin-top:100px; text-align:center;">
		#if($!globals.fileExists("daystudy$!globals.getLocale()/day${globals.daystudyNum()}.html"))
			<iframe src="/vm/daystudy$!globals.getLocale()/day${globals.daystudyNum()}.html"  frameborder=false scrolling="no" width="460" height="110" frameborder=no></iframe>
		#else
			$text.get("common.msg.notTran");
		#end
	</div>
</body>
</html>






