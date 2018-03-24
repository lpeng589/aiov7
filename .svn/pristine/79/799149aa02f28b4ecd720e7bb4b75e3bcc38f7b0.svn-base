<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript">
	$(function(){
		$("iframe").css("height",$(window).height()-15);
		var wWidth = $(window).width();
		$("#mainFrame").css("width",wWidth-250);
	});
	
	function refreshStat(){
		topFrame.window.refreshStat();
	}
	function closeLeft(){
		$("#topFrame").css("width","0");
		$("#mainFrame").css("width",$("#mainFrame").width()+190);
	}
	function showLeft(){
		$("#topFrame").css("width","190px");
		$("#mainFrame").css("width",$("#mainFrame").width()-190);
	}
</script>
<style type="text/css">
iframe{float:left;}
</style>
</head>
	<body>
		#if("$!addTHhead" == "true")
		#parse("./././body2head.jsp")
		#end
		<iframe width="190" src="EMailAction.do?operation=4&type=left" id="topFrame" name="topFrame" frameborder="no" border="0" framespacing="0"></iframe>
		<iframe width="10" src="/vm/oa/mail/frix.jsp" name="frix" noresize="noresize" frameborder="no" border="0" framespacing="0" id="frix"  ></iframe>
		<iframe #if($isFromDeskTop== 'trues') src="$url" #else src="EMailQueryAction.do?operation=4&type=main " #end name="mainFrame" frameborder="no" border="0" framespacing="0" id="mainFrame" scrolling="no"></iframe>
	</body>	
	<!-- 
	<frameset id="topFrame" cols="190,10,500" frameborder="no" border="0" framespacing="0">
		<frame src="EMailAction.do?operation=4&type=left " name="leftFrame" scrolling="No"
			noresize="noresize" id="leftFrame" />
		<frame src="/vm/oa/mail/frix.jsp" name="frix" scrolling="No"
			  />	
		<frame #if($isFromDeskTop== 'trues') src="$url" #else
			src="EMailQueryAction.do?operation=4&type=main " #end name="mainFrame" id="mainFrame"
			scrolling="no" />
	</frameset>
 -->
</html>
