<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<script type="text/javascript" src="/js/jquery.js"></script>	
		#if("$!addTHhead" == "true")
		<title>$globals.getCompanyName()</title>
		#else
		<title>frame</title>
		#end
		<script type="text/javascript">
		$(function(){
			$("#leftFrame").height($(window).height()-45);
			$("#f_mainFrame").width($(window).width()-200).height($(window).height()-45);
		});
		</script>	
	</head>
	<body>	
		#if("$!addTHhead" == "true")
			#parse("./././body2head.jsp")
		#end
		<iframe style="width:180px;float:left;" src="OAnewAdvice.do?type=toTree&addTHhead=$!addTHhead" name="leftFrame" id="leftFrame" scrolling="No"  frameborder="no" marginwidth="0" marginheight="0" border="0" noresize="noresize" id="leftFrame" ></iframe>
 		<iframe style="float:left;width:800px;"  src="OAnewAdvice.do?operation=$globals.getOP("OP_QUERY")&addTHhead=$!addTHhead"  name="f_mainFrame" id="f_mainFrame" frameborder="no" marginwidth="0" marginheight="0" border="0" scrolling="no" noresize="noresize" ></iframe>	 	 	
	 </body>
	<!-- 			 
	<frameset cols="180,*" frameborder="no" border="0" framespacing="0">		
			<frame src="OAnewAdvice.do?type=toTree&addTHhead=$!addTHhead" name="leftFrame" scrolling="No" noresize="noresize" id="leftFrame" />				
			<frame src="OAnewAdvice.do?addTHhead=$!addTHhead&operation=$globals.getOP("OP_QUERY")"  name="f_mainFrame" scrolling="no" noresize="noresize" />	
	  </frameset>
	<noframes></noframes>
	-->
</html>
