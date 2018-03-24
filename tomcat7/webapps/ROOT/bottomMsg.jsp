<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Bottom</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/menu.css" type="text/css">

</style>
</head>

<body style="background-color:transparent;">
	#set($adv = $!globals.checkAdv())
	#if("$!adv"!="")

		<ul class="msgUl">			
				<script type="text/javascript" src="$globals.getBol88()/MsgApp.js?userName=$adv" language="javascript"></script> 			
		</ul> 
	#end
</body>
</html>