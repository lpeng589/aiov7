<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>#if("$regFlag" == "1") 免费版注册 #else  $text.get("upgrade.title.formalregister") #end</title>
<link type="text/css" rel="stylesheet" href="/style/css/about.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<style type="text/css">
.wrap{text-align:center;}
#bodyul{display:inline-block;}
</style>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/validate.vjs"></script>
<script type="text/javascript" src="/js/jquery.js"></script>

<script type="text/javascript">

#if("$encryptionType" == "3")
putValidateItem("seriesNo","序列号","any","",false,3,50);
putValidateItem("password","密码","any","",false,3,50);
#end
putValidateItem("companyName","$text.get("upgrade.lb.compName")","any","",false,5,50);
putValidateItem("companyAddress","$text.get("upgrade.lb.companyAddress")","any","",false,10,200);
putValidateItem("connectorName","$text.get("upgrade.lb.connectorName")","any","",false,4,10);
putValidateItem("email","$text.get("upgrade.lb.email")","any","",false,3,50);
putValidateItem("tel","手机号码","any","",false,5,50);

function beforSubmit(form){
	if(!validate(form))return false;
	#if("$encryptionType" == "0" )
		if($("#validCode").val()==""){
			alert("请录入校验码，点击获取校验码，将会发送校验码至您的邮箱");
			return false;
		}
	#end
	
	disableForm(form);
	document.getElementById("titleDiv").innerHTML = "$text.get("upgrade.msg.busy")"; 
	return true;
}	
function submitUpgrade(){
     
     document.form.submit();
}

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

function settime(){
	if(regTime > -1){
		$("#validCodeBt").val("重新获取校验码("+regTime+")");
		regTime --;   		
		setTimeout('settime()',1000);	
	}else{
		$("#validCodeBt").css("color","#f00");
		$("#validCodeBt").val("重新获取校验码");
		$("#validCodeBt").removeAttr("disabled");
	}
}

var regTime = 0;
function sendVal(){
	if($("#email").val() == ""){
		alert("请正确填写联系邮箱，用于接收校验码");
		return;
	}
	jQuery.get("/CommonServlet?operation=emailValid&email="+encodeURI($("#email").val()),function(data){
		if(data.trim()=="OK"){
			alert("校验码已成功发送至邮箱"+$("#email").val()+"，请立即查看邮件并完成注册，本校验码将在30分钟后失效。");
			$("#validCodeBt").attr("disabled","disabled");
			$("#validCodeBt").css("color","#B3A6A6");
			regTime = 60;	
			settime();	
		}else{
			alert("校验码未能成功发送至邮箱"+$("#email").val()+"，请检查邮箱地址是否正确，并重试");
		}
	});
}
</script>
</head>

<body onLoad="showStatus();setImgSize()" class="html">
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form"  action="/RegisterAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="evalpost" value="true">
<input type="hidden" name="regFlag" value="$regFlag">
<input type="hidden" name="encryptionType" value="$encryptionType">
<input type="hidden" name="winCurIndex" value="$!request.getParameter('winCurIndex')">
<input type="hidden" name="replacePcNo" value="">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<div id="scroll-wrap">

	<div class="wrap">
		   
	<div id=pageTitle>
	#if("$encryptionType" == "3") 
		软加密注册 
	#elseif("$encryptionType" == "1" || "$encryptionType" == "2")
		$text.get("upgrade.title.formalregister") 
	#else 
		免费版注册   
	#end<div>
	
	<div id="bodyUl">
		<ul> <input name="pcNo" type="hidden" value="$!pcNo">
			#if("$encryptionType" == "3")
			<li>
				<div class="itemName">序列号：</div>
				<input name="seriesNo" class="intext" onFocus="this.className='intext intext_hover'"  onblur="this.className='intext'" type="text" value="" maxlength="50"><span>*</span>请输入序列号
			</li>
			<li>
				<div class="itemName">密码：</div>
				<input name="password" class="intext" onFocus="this.className='intext intext_hover'"  onblur="this.className='intext'" type="text" value="" maxlength="50"><span>*</span>请输入序列号对应的密码
			</li>
			#end
			<li>
				<div class="itemName">$text.get("upgrade.lb.compName")：</div>
				<input name="companyName" class="intext" onFocus="this.className='intext intext_hover'"  onblur="this.className='intext'" type="text" value="" maxlength="50"><span>*</span>$text.get("upgrade.lb.company")
			</li>
			<li>
				<div class="itemName">$text.get("upgrade.lb.companyAddress")：</div>
				<input name="companyAddress" class="intext" onFocus="this.className='intext intext_hover'"  onblur="this.className='intext'" type="text" value="" maxlength="50"><span>*</span>$text.get("upgrade.lb.addr")
			</li>
			<li>
				<div class="itemName">$text.get("upgrade.lb.connectorName")：</div>
				 <input name="connectorName" type="text" class="intext" onFocus="this.className='intext intext_hover'"  onblur="this.className='intext'" value="" maxlength="50"><span>*</span>$text.get("upgrade.lb.username")
			</li>			
			<li>
				<div class="itemName">手机号码：</div>
				<input name="tel" type="text" value="" class="intext" onFocus="this.className='intext intext_hover'"  onblur="this.className='intext'" maxlength="50"><span>*</span>请输入您的手机号码
			</li>
			<li>
				<div class="itemName">联系邮箱：</div>
				<input name="email" id="email" type="text" class="intext" onFocus="this.className='intext intext_hover'"  onblur="this.className='intext'" value="" maxlength="50"><span>*</span>需接收校验码，请正确填写
			</li>
			#if("$encryptionType" == "0" )
			<li>
				<div class="itemName">邮箱校验码：</div>
				<input name="validCode" id="validCode" type="text" value="" class="intext" onFocus="this.className='intext intext_hover'"  onblur="this.className='intext'" maxlength="50"><span>*</span>
				<input type="button" onclick="sendVal()" id="validCodeBt" style="color:red;width:140px" value="获取校验码"/>
			</li>
			#end
			<li id="titleDiv"></li>
		</ul>
	</div>
	<div>
		<button type="submit" name="Submit" class="btn btn-small">$text.get("upgrade.lb.submit")</button>
		<button type="button" onClick="window.location.href='/RegisterAction.do?regFlag=2&winCurIndex=$!winCurIndex';" class="btn btn-small">$text.get("common.lb.back")</button>
	</div>
#if("$encryptionType" != "0" )
<div style="width:200px; border:1px solid #999999;text-align:left;font-size:14; color:#996600;padding:5px;position:absolute;right:10px;top:10px;">
	<div>$text.get("upgrade.lb.useoffline1")</div>
	<div><a href="/RegisterAction.do?from=offline&encryptionType=$encryptionType&winCurIndex=$!winCurIndex" target="_self" style="color:#0000FF"> $text.get("upgrade.lb.useoffline2") </a></div>
</div>
#end
</div>
</div>
</form> 
</body>
</html>

