<!doctype html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta charset="utf-8">
<title>$globals.getCompanyName() - 科荣软件</title>
<link type="text/css" rel="stylesheet" href="/style/v7/css/loginBase.css" />
<script type="text/javascript" src="/js/jquery.js"></script> 
<script type="text/javascript" src="$globals.js("/js/login.vjs","",$text)"></script>	
<link href="/js/intro/introjs.min.css" rel="stylesheet">
<script type="text/javascript" src="/js/intro/intro.min.js"></script>	

<script type="text/javascript">
if(navigator.userAgent.toLowerCase().indexOf("chrome")==-1 
			&& navigator.userAgent.toLowerCase().indexOf("safari")==-1){
	window.location.href = "/browser.jsp";
}

#if($globals.getSystemState().lastErrorCode != 0 || ($globals.getSystemState().dogState != 0 && $globals.getSystemState().dogState != 1 ))
	#if($type=="closeWindow")
	   window.close();
	#end
	window.location.href = 'common/fatalMessage.jsp?from=index';	 
#end

try{
	var koron = new ActiveXObject("KoronCom.TrustedSites") ;
}catch(e){}

//幻灯片  
var iShow = 1;
var changeTime = 5000;
function show()
{
    jQuery(".bgShow:eq("+iShow+")").fadeIn(1000).siblings(".bgShow").fadeOut(1000);
    jQuery(".barNum:eq("+iShow+")").addClass("sel").siblings(".barNum").removeClass("sel");
    iShow = iShow + 1;
    if(iShow == $(".barNum").length){
        iShow = 0;
    }
    
}

$(document).ready(function(){
	jQuery.ajax({
		type:"post",
		url:"/loginAction.do?operation=15",
		success:function(msg){		
			var datas = msg.split(";");
			var bg = "/style/v7/images/logo.png";
			var bg1 = "/style/v7/images/banner_01.jpg";
			var bg2 = "/style/v7/images/banner_02.jpg";
			var bg3 = "/style/v7/images/banner_03.jpg";
			/*if(datas[4] != "title"){
				document.title=datas[4]+"-- Powered by AIO V7";
			}*/
			if(datas[5] != "link"){
				if(datas[5].indexOf("http://")!=0){
					datas[5] = "http://"+datas[5];
				}
				$("#fir_header").attr('href',datas[5]);
			}			
			if(datas[0] != "companyLogo"){
				bg = "/style/v7/user/"+datas[0];
			}
			if(datas[1] != "logo1"){
				bg1 = "/style/v7/user/"+datas[1];
			}
			if(datas[2] != "logo2"){
				bg2 = "/style/v7/user/"+datas[2];
			}
			if(datas[3] != "logo3"){
				bg3 = "/style/v7/user/"+datas[3];
			}
			
			var li = '<li class="bgShow" style="background:url('+bg1+') no-repeat center;display:inline-block;"></li>'
				+'<li class="bgShow" style="background:url('+bg2+') no-repeat center;"></li>'
				+'<li class="bgShow" style="background:url('+bg3+') no-repeat center;"></li>';
			$(".bannerLogin").find('.banner').append(li);
			$(".header").find('img').attr('src',bg);
			
		}
	})　
	
	jQuery(".bgShow:eq(0)").show();
	setInterval(show, changeTime);
	
	if($(window).width()>1366){
		$(".login>div").css("right",($(window).width()-1366)/2+30);
	}
	
	$(".barNum").click(function(){
	    iShow = $(this).attr("Num")-1;
	    $(".bgShow:eq("+iShow+")").fadeIn().siblings(".bgShow ").fadeOut();
	    $(".barNum:eq("+iShow+")").addClass("sel").siblings(".barNum").removeClass("sel");
	});
	
	$(".b-close").click(function(){
		$(this).parents(".d-pop").hide();
		$(".pop-bg").hide();
	});

});
//用户名一键清除 
function removeVal(obj){
	$(obj).siblings("input").val("").focus();
}
function delInput(obj){
	if($(obj).val() != ""){
		$(obj).siblings("b.uin_del").show();
	}else{
		$(obj).siblings("b.uin_del").hide();
	}
}
//登录框焦点事件  
function loginInp(){
	var name = getCookieValue('userName');
	$(document.forms[0].name).siblings("label").hide();
	document.forms[0].name.value= name;
	if(name != ""){		
		document.forms[0].password.focus();
		$(document.forms[0].password).siblings("label").hide();
	}
	
	if(typeof(document.forms[0].remainpass) != "undefined"){ 
		var password = getCookieValue('password');
		document.forms[0].password.value= password;
		if(password != ""){		
			document.forms[0].remainpass.checked = true;
		}
	}
	$(".loginInp").focus(function(){
    	$(this).siblings("label").hide();
    	if($(this).val() != ""){
    		$(this).siblings("b.uin_del").show();
    	}
    }).blur(function(){
    	if($(this).val() == ""){
    		$(this).siblings("label").show();
    	}
    });
}

function tsPop(obj){
	var tshow = $(obj).attr("tshow");
	$(".pop-bg").height($(window).height()).show();
	$("."+tshow).css("left",($(window).width()-460)/2).show();
}



</script>
</head>
<body onLoad="setFocus();windsMax();loginInp();">
<iframe name="formFrame" style="display:none"></iframe>
<form name="LoginForm" method="post" action="/loginAction.do" onKeyDown="keyDownOp()" target="formFrame" autocomplete="off"> 
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="NOTTOKEN" value="true">
<input type="hidden" id="operation" name="operation" value="$globals.getOP("OP_LOGIN")"/>
<input type="hidden" id="noSuchUser" value="$text.get("login.lb.noSuchUser")"/>
<input type="hidden" id="ukId" name="ukId" value="">
<input type="hidden" id="strM1" name="strM1" value="">
<input type="hidden" id="strM2" name="strM2" value="">
<input type="hidden" id="clientKey" name="clientKey" value="">
<input type="hidden" id="random" name="random" value="">
<input type="hidden" id="screenWidth" name="screenWidth" value="">
<input type="hidden" id="sunCompany" name="sunCompany" value="1">
<input type="hidden" id="style" name="style" #if("$!globals.getLocale()" == "en") value="style4" #else value="style1" #end/>
<input type="hidden" id="system" name="system" value="new"/>
<input type="hidden" id="loc" name="loc" value="zh_CN" />

	<!-- header头部 Start -->
	<div class="header">
		<a href="http://www.krrj.cn" target="_blank" id="fir_header"><img width="357" height="55" src="/style/v7/images/logo.png" alt="科荣V7" /></a>
		<ul>
			<li onclick="tsPop(this);" tshow="first-pop"><a>首次使用</a></li>
			<li onclick="tsPop(this);" tshow="client-pop"><a>客户端</a></li>
			<li><a href="http://help.koronsoft.com" target="_blank">反馈建议</a></li>
			<li><a href="/mlogin.jsp" target="_blank">手机版</a></li>
		</ul>
    </div>
	<!-- header头部 End -->
	<!-- banner_Login Start -->
	<div class="bannerLogin">
		<ul class="banner">
		<!-- 
			<li class="bgShow bg1"></li>
			<li class="bgShow bg2"></li>
			<li class="bgShow bg3"></li> -->
		</ul>
		<div class="login">
			<ul class="barItems">
				<li class="barNum sel" Num="1"></li>
                <li class="barNum" Num="2"></li>
                <li class="barNum" Num="3"></li>
			</ul>
			<div>
				<div class="animateLogin">
					<div class="animating">
						<ul id="comLogin" class="login_u">
							<li>
								<p>账号登录</p>
							</li>
							<li>
								<span>
									<label for="name" id="po-name">用户名</label>
									<input id="name" name="name" onpropertychange="delInput(this);" oninput="delInput(this);" class="txt_inp loginInp" type="text" />
									<b class="uin_del Icon16" onclick="removeVal(this);"></b>
								</span>
							</li>
							<li>
								<span>
									<label for="password" id="po-psw">登录密码</label>
									<input id="password" name="password" class="txt_inp loginInp" type="password" onkeydown="if(event.keyCode==13){formSubmit();}" />
								</span>
							</li>
							<li>
								<input id="remainpass" class="cbox_inp" type="checkbox" />
								<label for="remainpass">记住密码</label>					
							</li>
							<li>
								<span class="loginBtn" onClick="formSubmit();">登 录</span>
							</li>
						</ul>
		                
					</div>
				</div>
			</div>
		</div>
		<embed type="application/np-print" style="width:0px;height:0px;" id="plugin"/>
	</div>
	<!-- banner_Login End -->
	<!-- Footer Start -->
	<div class="Footer">
		Copyright © 2008 - 2014 Koronsoft Inc. All Rights Reserved.<br/>Powered by <a href="http://www.krrj.cn" target="_blank">AIO V7</a>
	</div>
	<!-- Footer End -->
	<div class="pop-bg">
	</div>
	<!-- 首次使用 Start -->
	<div class="d-pop first-pop" style="height:400px;">
		<div class="first-t">
			首次使用
			<b class="b-close"></b>
		</div>
		<ul class="first-ul">
			<li>
				<b>1</b>
				<span>软件初始用户名及密码均是admin，请登录后在系统》修改密码中及时修改。</span>
			</li>
			<li>
				<b>2</b>
				<span>软件目前暂时不支持IE系列浏览器，请下载<a href="/common/chrome.exe" target="_blank">chrome内核浏览器</a>。</span>
			</li>
			<li>
				<b>3</b>
				<span>如果登录的时候提示“你可能未正确安装控件”，请下载<a href="/common/AIOPrint.exe" target="_blank">科荣控件</a>并安装</span>
			</li>
			<li>
				<b>4</b>
				<span>如需反馈问题，请在<a href="http://help.koronsoft.com" target="_blank">help.koronsoft.com</a>中提问</span>
			</li>
		</ul>
	</div>
	<!-- 首次使用 End -->
	<!-- 客户端 Start -->
	<div class="d-pop client-pop" >
		<div class="first-t">
			客户端  
			<b class="b-close"></b>
		</div>
		<ul class="ct-ul">
			<li title="点击下载 " onclick="javascript:window.open('/common/chrome.exe');">
				<b class="b-chrome"></b>
				<i>谷歌浏览器</i>
			</li>
			<li title="点击下载" onclick="javascript:window.open('/common/KK_Setup.exe');">
				<b class="b-KK"></b>
				<i>KK聊天</i>
			</li>
			<li title="点击下载" onclick="javascript:window.open('/common/AIOPrint.exe');">
				<b class="b-AIO"></b>
				<i>科荣控件</i>
			</li>
			<li title="点击下载" onclick="javascript:window.open('/common/3g.qq.com/mo.apk');">
				<img src="/CommonServlet?operation=qrcodeDownload" alt="手机二维码" width="60" />
				<i>安卓手机版</i>
			</li>
		</ul>
	</div>
	<!-- 客户端 End -->
</body>
</form>
<embed id="s_simnew61"  type="application/npsyunew6-plugin" hidden="true"> </embed><!--创建firefox,chrome等插件-->
</html>
