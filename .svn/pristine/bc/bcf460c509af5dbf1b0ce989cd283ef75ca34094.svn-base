<!DOCTYPE html >
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0"/>
<title>$text.get("com.logo.setting")</title>
<link type="text/css" rel="stylesheet" href="/style/css/logoSet.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
function uploadBefore(logo,name){
	var varLogo = document.getElementById(logo).value ;		
	document.getElementById("logoType").value=logo ;
	
	document.getElementById("logoname").value=name ;
	if(varLogo.length==0){
		alert("$text.get('com.logo.not.null')") ;
		return ;
	}
	form.submit() ;
}
function setImgSize(){
	var maxwidth=500;
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
	
function changeSubmit(){
	jQuery.ajax({
		type:"post",
		url:"/LogoSetAction.do?operation=1&typeFlag=change&companyName="+$("#companyName").val()+"&companyLink="+$("#companyLink").val(),
		success:function(msg){
			if(msg == "3"){
				asyncbox.tips('保存成功','success');
			}			
		}
	})
}

$(function(){
	$(".tags-u>li").click(function(){
		var sw = $(this).attr("show");
		$(this).addClass("sel").siblings("li").removeClass("sel").parents("ul").siblings("ul").hide();
		$("."+sw+"-u").show();
	});
	$(".scroll-wrap").height(document.documentElement.clientHeight);
});
</script>
</head>
<body class="html" onLoad="setImgSize()">
<iframe name="formFrame" style="display:none"></iframe>
<div class="scroll-wrap">
<div class="wrap">
<form name="form" action="/LogoSetAction.do" method="post" enctype="multipart/form-data" target="formFrame">
<input type="hidden" name="operation" value="1">
<input type="hidden" name="logoType" id="logoType" value="">
<input type="hidden" name="logoname" id="logoname" value="">
			<div class="body-ul" id="bodyUl">
				<ul class="tags-u">
					<li class="sel" show="logo">LOGO设置</li>
					<li show="login">登录页面设置</li>
				</ul>
			
				<ul class="list-u logo-u">
					<li class="hi-li">
						<em class="e-item">公司名称</em>
						<input class="w-inp" type="text" id="companyName" value="$!companyName" name="companyName"  onChange="changeSubmit();">																															
					</li>
					<li class="hi-li">
						<em class="e-item">公司网址</em>
						<input class="w-inp" type="text" id="companyLink" value="$!companyLink" name="companyLink"  onChange="changeSubmit();">																															
					</li>
					<li class="d-li">
						<p class="p-item">公司登陆LOGO：<i>(建议公司登陆LOGO大小：宽为357像素，高为55像素。)</i></p>
						<div class="d-in-wp">
							<input class="inp-file" type="file" id="company" name="company" />																			
							<span class="btn-blue" onClick="uploadBefore('company','company');">上传</span>
							#if("$!company"!="")												
							<div class="d-proview">
								<span class="pr sp-image">
									<img style="height:200px;" src="/style/v7/user/$company" />
									<b class="pa icons b-del" onClick="location.href='/LogoSetAction.do?operation=3&logoType=company&logoName=$company'"></b>
								</span>
							</div>
							#end
						</div>
					</li>
					<li class="d-li">
						<p class="p-item">$text.get("com.print.logo")<i>(建议打印LOGO大小：宽为300像素，高为60像素。格式为bmp)</i></p>
						<div class="d-in-wp">
							<input class="inp-file" type="file"  name="printLogo" id="printLogo" /> 
							<span class="btn-blue" onClick="uploadBefore('printLogo','printLogo')">上传</span>						
							#if("$!printLogo"!="")
							<div class="d-proview">
								<span class="pr sp-image">
									<img style="height:200px;" src="/ReadFile.jpg?fileName=$globals.urlEncode($printLogo)&tempFile=print&type=PIC">
									<b class="pa icons b-del" onClick="location.href='/LogoSetAction.do?operation=3'"></b>
								</span>
							</div>						
							#end
						</div>
					</li>
				</ul>
				
				
				<ul class="list-u login-u" style="display:none;">  
					<li class="d-li">
						<p class="p-item">登陆背景图片1<i>(建议图片大小：宽为1366像素，高为400像素。)</i></p>
						<div class="d-in-wp">
							<input class="inp-file" type="file" id="loginLogo_1" name="loginLogo_1" />
							<span class="btn-blue" onClick="uploadBefore('loginLogo_1','loginLogo_1');">上传</span>					
							#if("$!loginLogo_1"!="")
							<div class="d-proview">
								<p style="padding:0;margin:10px 0;">界面LOGO_1$text.get("common.lb.preview")：</p>
								<span class="pr sp-image">
									<img style="height:200px;" src="/style/v7/user/$loginLogo_1" />
									<b class="pa icons b-del" onClick="location.href='/LogoSetAction.do?operation=3&logoType=loginLogo&logoName=$loginLogo_1'"></b>
								</span>
							</div>																								
							#end
						</div>
					</li>
					<li class="d-li">
						<p class="p-item">登陆背景图片2<i>(建议图片大小：宽为1366像素，高为400像素。)</i></p>
						<div class="d-in-wp">
							<input class="inp-file" type="file" id="loginLogo_2" name="loginLogo_2" />
							<span class="btn-blue" onClick="uploadBefore('loginLogo_2','loginLogo_2');">上传</span>						
							#if("$!loginLogo_2"!="")
							<div class="d-proview">
								<p style="padding:0;margin:10px 0;">界面LOGO_2$text.get("common.lb.preview")：</p>
								<span class="pr sp-image">
									<img style="height:200px;" src="/style/v7/user/$loginLogo_2">
									<b class="pa icons b-del" onClick="location.href='/LogoSetAction.do?operation=3&logoType=loginLogo&logoName=$loginLogo_2'"></b>
								</span>
							</div>																								
							#end
						</div>
					</li>
					<li class="d-li">
						<p class="p-item">登陆背景图片3<i>(建议图片大小：宽为1366像素，高为400像素。)</i></p>
						<div class="d-in-wp">
							<input class="inp-file" type="file" id="loginLogo_3" name="loginLogo_3" />
							<span class="btn-blue" onClick="uploadBefore('loginLogo_3','loginLogo_3');">上传</span>						
							#if("$!loginLogo_3"!="")
							<div class="d-proview">
								<p style="padding:0;margin:10px 0;">界面LOGO_3$text.get("common.lb.preview")：</p>
								<span class="pr sp-image">
									<img style="height:200px;" src="/style/v7/user/$loginLogo_3">
									<b class="pa icons b-del" onClick="location.href='/LogoSetAction.do?operation=3&logoType=loginLogo&logoName=$loginLogo_3'"></b>
								</span>
							</div>						
							#end
						</div>
					</li>
					
				</ul>
            </div>


</form>

</div>
</dvi>
</body>
</html>