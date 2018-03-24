<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>免费版注册</title>
<link type="text/css" rel="stylesheet" href="/style/css/about.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<style type="text/css">
.wrap{text-align:center;}
#bodyul{display:inline-block;}
</style>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/validate.vjs"></script>
<script type="text/javascript">



	function setImgSize()
	{
		var maxwidth=400;
		var imgs = document.images;
		var length = imgs.length;
		for(var i=0;i<length;i++)
		{
			var img = imgs[i];
			var heigth = img.offsetHeight;
			var width = img.offsetWidth;
			var p = heigth/width;
			if(width>maxwidth)
			{
				var newWidth = maxwidth;
				var newHeigth=newWidth*p;
				img.width=newWidth;
				img.height=newHeigth;
				
			}
		}
	}
$(function(){
	$("#scroll-wrap").height(document.body.clientHeight-18);
});
</script>
</head>

<body onLoad="showStatus();setImgSize()" class="html">
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form"  action="/RegisterAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="evalpost" value="true">
<input type="hidden" name="regFlag" value="$regFlag">
<input type="hidden" name="encryptionType" value="$encryptionType">
<input type="hidden" name="winCurIndex" value="$!request.getParameter('winCurIndex')">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<div id="scroll-wrap">

	<div class="wrap">
		   
	<div id=pageTitle>
	注册成功
	<div>
	
	<div id="bodyUl">
		<ul>
			
			<li>
				<div class="itemName" style="width:400px;">请牢记您的序列号和密码，这是您接受科荣服务的唯一凭证</div>
				 	
			</li>
			<li style="color:red;">
				<div class="itemName" style="color:red;">序列号：</div>$request.getParameter("dogId")
			</li>
			<li style="color:red;">
				<div class="itemName" style="color:red;">密码：</div>$request.getParameter("seriesPassword")
			</li>
			<li id="titleDiv"></li>
		</ul>
	</div>
	<div>
		<button type="button" onClick="window.location.href='/';" class="btn btn-small">$text.get("common.lb.back")</button>
	</div>

</div>
</div>
</form> 
</body>
</html>

