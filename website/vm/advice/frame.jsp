<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/style/css/base_button.css" type="text/css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#else
<title>frame</title>
#end
<style type="text/css">
/*************全局公用Base样式*************/
body,div,form,img,ul,ol,li,dl,dt,dd,a,label,em,p,h1,h2,h3,h4,h5,h6,input,select,button,textarea{margin:0px; padding:0px;}
body{font:12px/1.5 Microsoft Yahei,Arial;background:#efefef;}
li{list-style: none;}
em,i{font-style:normal;}
button,input,select,textarea{outline:0;font:12px/1.5 Microsoft Yahei,Arial;box-sizing:border-box;}
.backPage{position:absolute;left:138px;top:5px;}

</style>
<script type="text/javascript">
	$(function(){
		$("#f_mainFrame").width(window.screen.width-210);
		if($("#addTHhead").val() == "true"){	
			$("iframe").each(function(){
				$(this).height($(window).height()-$(".d-top").height());			
			});
		}else{
			$("iframe").each(function(){
				$(this).height($(window.parent.document).find("body").height()-110);
			});
		}
		
	})
		
	function iPadBack(obj){
		$("#f_mainFrame").attr("src","/AdviceAction.do?operation=4");
		$(obj).remove();
	}
</script>
</head>
	<!-- 
	<frameset cols="185,*" frameborder="no" border="0" framespacing="0" id="hz">	
		<input type="text" value="$!deskType" />
			<frame src="AdviceAction.do?actiontype=toTree&deskType=$!deskType" name="leftFrame" scrolling="No" noresize="noresize" id="leftFrame" />
			<frame src="AdviceAction.do?operation=$globals.getOP("OP_QUERY")"  name="f_mainFrame" id="f_mainFrame" scrolling="no" noresize="noresize" />
	 </frameset>
	<noframes></noframes>
	 -->
	 <body id="body">	
	 <input type="hidden" id="addTHhead" value="$!addTHhead" />
		#if("$!addTHhead" == "true")
			#parse("./././body2head.jsp")
			<iframe style="width:190px;float:left;" src="AdviceAction.do?actiontype=toTree&deskType=$!deskType&addTHhead=$!addTHhead" name="leftFrame" id="leftFrame" scrolling="No"  frameborder="no" marginwidth="0" marginheight="0" border="0" noresize="noresize" id="leftFrame" ></iframe>
	 		<iframe style="float:left;"  src="AdviceAction.do?addTHhead=$!addTHhead&operation=$globals.getOP("OP_QUERY")"  name="f_mainFrame" id="f_mainFrame" frameborder="no" marginwidth="0" marginheight="0" border="0" scrolling="no" noresize="noresize" ></iframe>
		#else
	 	<iframe style="width:190px;float:left;" src="AdviceAction.do?actiontype=toTree&deskType=$!deskType" name="leftFrame" id="leftFrame" scrolling="No" frameborder="no" marginwidth="0" marginheight="0" border="0" noresize="noresize" id="leftFrame" ></iframe>
	 	<iframe style="float:left;"  src="AdviceAction.do?operation=$globals.getOP("OP_QUERY")"  name="f_mainFrame" id="f_mainFrame" frameborder="no" marginwidth="0" marginheight="0" border="0" scrolling="no" noresize="noresize" ></iframe>
	 	#end	 	
	 </body>
	 
</html>
