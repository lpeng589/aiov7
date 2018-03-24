<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
<!--  <frameset cols="180,*" frameborder="no" border="0" framespacing="0">
	 <frame src="OAKnowledgeCenter.do?type=toTree&winCurIndex=$!request.getParameter("winCurIndex")" name="leftFrame" scrolling="No"  noresize="noresize" id="leftFrame"/>
	 <frame src="/OAKnowCenter.do?type=oaKnowList" name="f_mainFrame" id="f_mainFrame" scrolling="no" />
</frameset><noframes></noframes>-->
<body>	
	#if("$!addTHhead" == "true")
		#parse("./././body2head.jsp")
	#end
	<iframe style="width:180px;float:left;" src="OAKnowledgeCenter.do?type=toTree&winCurIndex=$!request.getParameter("winCurIndex")&addTHhead=$!addTHhead" name="leftFrame" id="leftFrame" scrolling="No"  frameborder="no" marginwidth="0" marginheight="0" border="0" noresize="noresize" id="leftFrame" ></iframe>
	<iframe style="float:left;width:800px;"  src="/OAKnowCenter.do?type=oaKnowList&addTHhead=$!addTHhead"  name="f_mainFrame" id="f_mainFrame" frameborder="no" marginwidth="0" marginheight="0" border="0" scrolling="no" noresize="noresize" ></iframe>	 	 	
</body>
</html>
