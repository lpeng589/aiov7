<!DOCTYPE html>
<html style="overflow:hidden;">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=8"/>
<link href="/style/images/aio-icon.png" rel="apple-touch-icon"/>
<!-- <title>$globals.getCompanyName() - 科荣软件</title>--> 
<title>$globals.getCompanyName()</title>
<link type="text/css" rel="stylesheet" href="/style/v7/css/body-v7.css" />
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="/js/jquery.jerichotab.js"></script>
<script type="text/javascript" src="/js/menu/jquery.bgiframe.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script type="text/javascript" src="/js/menu/jquery.dimensions.js"></script>
<script type="text/javascript" src="/js/menu/jquery.positionBy.js"></script>
<script type="text/javascript" src="/js/menu/jquery.jdMenu.js"></script>
<script type="text/javascript" src="/js/menu/jquery.lavalamp.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/intro/intro.js"></script>	
<script type="text/javascript" src="$globals.js("/js/login.vjs","",$text)"></script>

<script type="text/javascript" src="$globals.js("/js/function.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/body2.vjs","",$text)"></script>
<link href="/js/intro/introjs.min.css" rel="stylesheet">
<script type="text/javascript">
#if("$!AlertMsg" != "")
	alert("$!AlertMsg");
#end
//检测浏览器版本 
if(navigator.userAgent.toLowerCase().indexOf("chrome")==-1 && navigator.userAgent.toLowerCase().indexOf("firefox")==-1
			&& navigator.userAgent.toLowerCase().indexOf("safari")==-1 && navigator.userAgent.toLowerCase().indexOf("msie 8.0")==-1){
	window.location.href = "/browser.jsp";
}
var defSys = "$!LoginBean.defSys" ;
#if("$!globals.getSystemState().noRegUseDate" != "")
     if(confirm("$text.get("common.lb.plsReg")")){
    	window.location.href = '/RegisterAction.do?regFlag=2&step=two&encryptionType=$!globals.getSystemState().encryptionType';
	}
#end	
var cururl="";

function changeDefSys(defSys){
	if(defSys == -1){	
		showreModule(title='$text.get('role.lb.bol88')','/AIOBOL88Action.do?');		
	}
	var url = '/MenuQueryAction.do?sysType='+defSys+'&loadMenu=true';
	if(url.length > 0 && url != cururl){
		jQuery.ajax({
			type: "POST",
			url: url,
			async: false,
			cache: false,
			success: function(data){
				cururl = url;
				var menuMove = "<span class=\"menu-move-span\" move=\"right\" title=\"右移\"><b class=\"icons move-right\"></b></span>";
				var menuMove5 = "<span class=\"menu-move-span-5\" move=\"right\" title=\"右移\"><b class=\"icons move-right\"></b></span>";
				if(defSys == 1){
					$("#ERP_UL").html(data);
				}else if(defSys == 2){
					$("#OA_UL").html(data);
				}else if(defSys == 3){
					$("#CRM_UL").html(data);
				}else if(defSys == 0){
					$("#COMMON_UL").html(data);
				}
			}
		});
	}
	
	
}
//菜单偏移调整
function firstLiHover(){
	$(".first-li").hover(
	    function() { 
	    	$(this).addClass("sel-li").find(".second-menu-div").show();
	    	var oDiv = $(this).find(".second-menu-div");
	    	var screenWidth = $(document.body).outerWidth(true); //body可视宽度
	    	var sWidth = 0;   //容器宽度初值  
	    	var tOffset =  $(this).offset().left; //偏移Left
	    	oDiv.find(">div").each(function(){
	    		var objU = $(this).find(">ul");
	    		var deUW = objU.width();
	    		if(objU.attr("default") == "1"){
	    			return false;
	    		}else
	    		{
		    		if( objU.find(">li").size() > 10){
		    			objU.width(deUW*2).attr("default","1").find(">li").css({"width":deUW-20,"float":"left","display":"inline-block"});
		    		}
	    		}
	    	});
	    	for(var s=0;s<oDiv.find(">div").size();s++){
	    		sWidth += (oDiv.find(">div:eq("+s+")").outerWidth(true));
	    	}
	    	oDiv.width(sWidth+1)
	    	if((sWidth+22+tOffset) > screenWidth){
		    	if((sWidth+22) > screenWidth){  //间距+边框=22
		    		oDiv.width(screenWidth-22).css("left",0-tOffset);
		    	}else{
		    		oDiv.css("left",0-((sWidth+22)-(screenWidth-tOffset))-20);
		    	}
	    	}
	    },
	    function() { $(".second-menu-div", this).hide();$(this).removeClass("sel-li"); }
    );
}
$(window).resize(function(){
	var bodyWidth  = $(document.body).outerWidth(true); //body宽度
	$("#menuTabId").width(bodyWidth-200);
	$(".head_r").width(bodyWidth-260);
});
//新菜单-功能模块判断
function NewMenuSpan(){
	var sLen =$(".lavaLampWithImage span").size();
	for(var i=0;i<sLen;i++){
		var z = $(".lavaLampWithImage span:eq("+i+")").attr("Number");
		changeDefSys(z);
	}
	
	var bodyWidth  = $(document.body).outerWidth(true); //body宽度
	
	$("#menuTabId").width(bodyWidth-200);
	$(".head_r").width(bodyWidth-260);
	
	firstLiHover();
	v7NewMenu();
	
	dw = 56 *($(".first-li").size());
	if(document.documentElement.clientWidth -260 - dw < 150){
		//长度不足以显示搜索条，隐藏

		$(".searchDiv").hide();
	}
}

$(function(){
	NewMenuSpan();
	MoveNum();
	#if("$!showFirst"=="true")
	startIntro();
	#end
});


function showWin(moduleType){
	if(moduleType == 1){
		showModule('/newMenu.do?keyId=28141812_0811271603291400086&name=ERP导航&operation=0',moduleType);
	}else{
		showModule('/newMenu.do?keyId=desk&operation=0&moduleType='+moduleType,moduleType);
	}
}

/* 跳转到模块 */
function showModule(url,sysType){
	var moduleName = "";
	if(sysType == "1"){
		moduleName = "ERP导航";
	}else if(sysType == "2"){
		moduleName = "OA导航";
	}else if(sysType == "3"){
		moduleName = "CRM导航";
	}else if(sysType == "4"){
		moduleName = "HR导航";
	}
	mdiwin(url,moduleName);
}
/*wyy弹出聊天框*/
//全局
var ksSendId;
var ksSendType;
function openkk(){	
	jQuery.ajax({
		type:"post",
		url:"/MessageQueryAction.do?src=menu",
		async: false,
		success:function(msg){						
			$("#showKs").html(msg);			
			organization();			
			var firstLoad = $("#firstLoad").val();
			eval(firstLoad);
			publicDragDiv($("#popup_msg"));	
		}			
	})　
	
}
function closeKK(){
	$("#popup_msg").hide();	
}
function showKKLog(){
	$("#showKK").show();
	$("#showKKDiv").removeClass("showKKlog-back").hide();
}

function openMsgDialog(sendType,sendId,sendName,obj){
	openkk();	
	$(obj).addClass("focus-li").siblings("li").removeClass("focus-li");	
	//判断是否大于5个  
	if($("#ul_left>li").size() == 5 && $("#li_"+sendId).val() == undefined){
		asyncbox.tips("打开窗口不能多于5个","error");
		return;
	}		
	jQuery.ajax({
		type:"post",
		url:"/MessageAction.do?operation=70&sendType="+sendType+"&sendId="+sendId,
		success:function(msg){
			
			ksSendId = sendId;	
			ksSendType = sendType;		
			if($("#ul_left").val() != undefined){	
				$("#showKK>.kk_class").hide();//全部清除			
				//是否重复				
				if($("#li_"+sendId).val() == undefined){
					var li='<li id="li_'+sendId+'" onclick=\'openDialog("'+sendType+'","'+sendId+'","'+sendName+'",this)\'><em class="in-em" title='+sendName+'>'+sendName+'</em><b title="关闭" onClick="closePopup(\''+sendId+'\');" class="close-talk"></b></li>';
					$("#ul_left").append(li);
				}else{
					$("#right_"+sendId).show();
					$("#li_"+sendId).addClass("focus-li").siblings("li").removeClass("focus-li");
					return;
				}			
			}else{			
				var div ='<div class="talk-list" id="talk-list"><ul id="ul_left"></ul><b class="hide-talk" onclick="hideKKdLog();">-</b><b title="关闭" class="close-talk" onclick="delKKdLog();"></b></div>';	
				var li='<li id="li_'+sendId+'" onclick=\'openDialog("'+sendType+'","'+sendId+'","'+sendName+'",this)\'><em class="in-em" title='+sendName+'>'+sendName+'</em><b title="关闭" onClick="closePopup(\''+sendId+'\');" class="close-talk"></b></li>';
				$("#showKK").append(div);				
				$("#ul_left").append(li);
			}
			var kkDiv='<div class="kk_class" id="right_'+sendId+'"></div>';
			
			
			$("#li_"+sendId).addClass("focus-li").siblings("li").removeClass("focus-li");
			$("#showKK").append(kkDiv);
			$("#right_"+sendId).html(msg);
			$("#right_"+sendId).show();
			$("#showKK").show();			
			load();
			
			$("#show"+sendId).remove();
			//消息- 1
			if($("#messageId>.newsmag").text() > 1){
				var num = parseFloat($("#messageId>.newsmag").text()) - 1;
				$("#messageId>.newsmag").text(num);
			}else{
				$("#messageId").html('');
			}
			publicDragDiv($("#showKK"));
		}
	});	
}

function feedBack(){
	asyncbox.open({
	 	title : '反馈建议',
	 	id : 'D345',
	　　	url : 'feedback.jsp',
		height: 285,
		width: 420,
		callback : function(action,opener){				
	　　　	}
　　　	});
	$("#D345").find("iframe").css("height","255px");

}

function closedialog(code){
	if(code >= 200 && code <= 400){
		parent.jQuery.close('D345');
		asyncbox.success('感谢您的反馈建议，我们将尽快解决您的问题！   <a class="help-a" href="http://help.koronsoft.com" target="_blank">去帮助中心看看</a>','提示',function(action){
			if(action == 'close'){
			}
		});
	}else if(code>400){
		asyncbox.tips('反馈建议失败','error',1500);
	}
}
function execPlugin(param){	
	try{		
		file = param.substring(0,param.indexOf(";"));
		defineName=param.substring(param.indexOf(";")+1);
		if($("#plugin").size()==0){
			$(document.body).append('<embed type="application/np-print" style="width:0px;height:0px;" id="plugin"/>');
		}
		#set($coks = $request.getCookies())
		#set($JSESSIONID = "")
	    #foreach($cok in $coks) 
	        #if($cok.getName().equals("JSESSIONID")) 
	        	   #set($JSESSIONID = $cok.getValue())
	        #end
	    #end
		
		var mimetype = navigator.mimeTypes["application/np-print"];		
		if(mimetype){
			if(mimetype.enabledPlugin){
				 var cb = document.getElementById("plugin");
				 cb.OpenDefine(file,defineName,'$!IP|$!port','$!JSESSIONID','$LoginBean.id');
			}
		}else{
			var obj = new ActiveXObject("KoronReportPrint.DefineEdit") ;
		    obj.URL="$!IP|$!port" ;
		   	obj.JsessionID ="$!JSESSIONID" ;
		   	obj.UserId ='$LoginBean.id' ;
		    obj.FileName=file ;
		    obj.DefineName=defineName ;
			obj.OpenDefine();	
		}
	}catch (e){
		asyncbox.alert("$text.get('com.sure.print.control')<br><br><a class='aio-print' href='/common/AIOPrint.exe' target='_blank'>下载控件</a>","信息提示");
	}

}


function newpasswardkeyup(tourl){
	if(event.keyCode==13) {
		if($("#newpassward").val() !=""){
			reloginajax(tourl);
			jQuery.close("D345PA");
		}
	}
}
function reloginajax(tourl){
	var pass = hex_md5($("#newpassward").val());
　　　	jQuery.post("/MobileAjax?op=LOGIN",
	       { name: $("#LoginUserUser").val(), password: pass },
	       	   function(data){
	    		if(data.code != "OK"){
	          		alert(data.msg);
	          		reLogin(tourl);
	          	}else{
	          		if(typeof(tourl) != 'undefined'){
	          			tourl.reloginToUrl();
	          		}
				}
	        } ,
		　　		"json" 
	   	); 
}



function reLogin(tourl){
	asyncbox.open({
	 	title : '请输入密码',
	 	id : 'D345PA',
	　　	html : "<li style='margin: 10px 0px 10px 10px;'>您好，<i class='i-color'>$!LoginBean.EmpFullName</i></li><input style='margin: 10px 10px;width: 270px;height: 25px;'  type='password' id='newpassward' /> ",
		height: 170,
		width: 300,
		btnsbar : jQuery.btn.OKCANCEL,
		callback : function(action,opener){	
			if(action == 'ok'){
				reloginajax(tourl);
		　　　}
　　　	}
　　　});
	$("#newpassward").keyup(function(){
		newpasswardkeyup(tourl);
	});
	$("#newpassward").focus();
    
}


</script>
</head>
<body>
<div id="headarea">
    <div class="area" >
    <div class="head_l">
    <a id="menuId" href="javascript:void(0);" onclick="mdiwin('/UpgradeAction.do?type=5&src=menu','关于软件');"><span #if("$!companyLogo" !="") style="background: url($!companyLogo) no-repeat 0 0;" #end ></span></a>
    </div>
    <div class="icons chang_btn #if("$LoginBean.viewTopMenu"=="1") po-fav #else po-menu #end" title="显示收藏菜单"></div>
    <div class="head_r">
    <div style="height:66px;">
    <!-- 菜单1 Start -->
    <div class="head_menu" #if("$LoginBean.viewTopMenu"!="0")style="display:none;" #end>
	    <ul class="tmenu_ul">
	    #set($index=1)
	    #foreach($menu in $myMenu)
	    	#set($strUrl="$globals.get($menu,1)")
	    	#set($strUrl2="$globals.get($menu,1)")
	    	#if("$!strUrl"!="")
	    	#if($strUrl.indexOf("operation=6")!=-1)#set($strUrl2= $strUrl.replace("&operation=6",""))#end
	    	#if($globals.getMOperation($strUrl2).add()
					|| $globals.getMOperation($strUrl2).query()
					|| $strUrl2.indexOf("&designId")!=-1) 
	    		#if($strUrl.indexOf("?")!=-1)#set($strUrl= $strUrl + "&src=menu") #else #set($strUrl= $strUrl + "?src=menu") #end
	    	<li class="tmenu_li" onmouseover="addC(this);" onmouseout="removeC(this);" onclick="javascript:mdiwin('$strUrl','$globals.get($menu,0)');">
	    		<div class="img_dv">
	    		#if($globals.get($menu,2) == "/icon/default.png")
	    		<img class="face front" src="/style/images/newIcon/sale${index}.png"/>
	    		<img class="face back" src="/style/images/newIcon/sale${index}.png"/>
	    		#set($index=$index+1)
	    		#else
	    		<img class="face front" src="$globals.get($menu,2)"/>
	    		<img class="face back" src="$globals.get($menu,2)"/>
	    		#end
	    		</div>
	    		<span class="word_sp" #if($globals.get($menu,0).length()>5)title="$!globals.get($menu,0)"#end>#if($globals.get($menu,0).length()>5)$globals.get($menu,0).substring(0,5)#else $globals.get($menu,0)#end</span>
	    	</li>
	    	#end
	    	#end
	    #end
	    </ul>
	    <div class="menuother"><a href="#" class="t"><em class="ad_em"></em></a></div>
    </div>
    <!-- 菜单1 End -->
    <!-- 菜单2 Start -->
    <div id="menuListId" #if("$LoginBean.viewTopMenu"=="0")style="display:none;" #end>
    	<div class="v7-new-menu">
    		<div class="m-block" id="CRM_m-block">
    			<p class="p-icons">
    				<b class="icons crm-icon" title="CRM-客户关系管理"></b>
    			</p>
    			<div class="crm-block">
	    			<ul class="first-menu-list" id="CRM_UL">
	    			</ul>
    			</div>
    		</div>
    		<div class="m-block" id="ERP_m-block">
    			<p class="p-icons">
    				<b class="icons erp-icon" onclick="showWin('1')" title="ERP-进销存财务"></b>
    			</p>
    			<div class="erp-block">
	    			<ul class="first-menu-list" id="ERP_UL">
	    			</ul>
    			</div>
    		</div>
    		<div class="m-block" id="OA_m-block">
    			<p class="p-icons">
    				<b class="icons oa-icon" onclick="showWin('2')" title="OA-办公自动化"></b>
    			</p>
    			<div class="oa-block">
	    			<ul class="first-menu-list" id="OA_UL">
	    			</ul>
    			</div>
    		</div>
    		<div class="m-block">
    			<p class="p-icons">
    				<b class="icons sys-icon"></b>
    			</p>
    			<div class="sys-block">
	    			<ul class="first-menu-list" id="COMMON_UL">
	    			</ul>
    			</div>
    		</div>
    		<div class="clear"></div>
    	</div>
    			<div style="display:none;">
			    <ul class="lavaLampWithImage" id="1" style="z-index:2;"> 
					#foreach($module in [1,2,3,0]) 
						#if(($module =='1' && $LoginBean.canJxc==1) || ($module =='2' && $LoginBean.canOa==1) 
							|| ($module =='3' && $LoginBean.canCrm==1) || ($module =='0'))
							<span class="lava_span bg_$module" Number="$module" onClick="changeDefSys($module);">
							<a href="javascript:void(0)" #if($module=='1') title="$text.get('role.lb.pss')" #elseif($module=='2') title="$text.get('role.lb.oa')" #elseif($module=='3') title="$text.get('role.lb.crm')" #elseif($module=='4') title="$text.get('role.lb.hr')" #end >#if($module=='1')ERP #elseif($module=='2')OA #elseif($module=='3')CRM #elseif($module=='4')HR #end</a></span>	
						#end
					#end
					<!--  <li onClick="changeDefSys(-1);"><a href="javascript:void(0)" title="$text.get('role.lb.bol88')">BOL88</a></li>	 -->
				</ul>
				<input type="text" name="getFocus" id="getFocus" style="width:0px; height:0px;border:none;padding:0;">
				<div id="menu" class='menu_a' >
					<ul class='jd_menu' >
						
					</ul>
				</div>
			</div>
    </div>
   
    <!-- 菜单2 End -->
    </div>
    
    <div class="menuTab" id="menuTabId"></div>
    </div></div>
    <!-- 右上角头像 Start -->
   
    <div class="head-sculpture">
    	<div class="scul-img" id="sculimg">
    		<img src="$!myPhoto" />   	
    		<b class="triangle"></b>
    	</div>
    	<div class="scul-bottom">
    		<ul class="scul-1"><input type="hidden" id="LoginUserUser" value=$!LoginBean.name>
    			<li>您好，<i class="i-color">$!LoginBean.EmpFullName</i></li>
    			<li><b>部门：</b><em>$!LoginBean.departmentName</em></li>
    			<li><b>职务：</b><em>$globals.getEnumerationItemsDisplay("duty","$!LoginBean.titleId")</em></li>
    		
			
				<li><b>期间年：</b><em>$!accPeriod.AccYear</em></li>
				<li><b>期间：</b><em>$!accPeriod.AccPeriod</em></li>
		</ul>
    		<ul class="scul-2 bd-top">
    			<!-- 
    			#if("$globals.getSysSetting('isStartOA')" == "true")
    			<li><a href="/OA.do">办公平台</a></li> 
    			#end  --> 		
    			<!-- <li><a id="newGride" href="http://q.krrj.cn/s.d?seriaId=$!globals.getSystemState().dogId&kw=新手引导" target="_blank">新手引导</a></li> -->
    			<li><input id="showwebnode" type="checkbox" onchange="changeWebNote(this)" #if("$LoginBean.showWebNote" == "0") checked #end ><label for="showwebnode"> 显示网页通知</label></a></li>
    			<li><a id="fastKey" href="javascript:showHelp('bill')" >快捷键说明</a></li>
    			<li><a href="/common/KK_Setup.exe" target="_blank">下载KK聊天</a></li>
    			<li><a href="/common/AIOPrint.exe" target="_blank">下载科荣控件</a></li>
    			<li onclick="showMOBDiv('/d.html','/common/3g.qq.com/mo.apk','下载手机版到电脑');">下载安卓手机版</li>
    			<li onclick="showMOBDiv('/dcat.html','/common/3g.qq.com/mcat.apk','下载短信猫到电脑');">下载短信猫</li>
    			<li onclick="mdiwin('/UpgradeAction.do?type=5&src=menu','关于软件');">关于软件</li>
    			<!--<li class="bd-top"><a href="javascript:void(0)" onclick="feedBack()" target="_blank">反馈建议</a></li>   -->
    			
    			<li class="bd-top" onclick="sureLoginOut();" title="$text.get("login.lb.logout")">退出</li>
    		</ul>
    	</div>
    </div>
    <!-- 即时消息 Start-->  
    <div id="showKs" style="position:relative;"></div>
    <div id="showKK" style="display: none;">
    	
    </div>
    <div onclick="showKKLog();" class="showKKlog" id="showKKDiv">显示对话</div>
    <!-- 即时消息 End -->
    <!-- 右上角头像 End -->
    <div class="head_nav_menu" #if("$LoginBean.showWebNote" != "0") style= "display:none" #end>
	    <ul>
		    <li class="advice-li">
		    	<div class="d-advice">
			    	<a class="n1" onclick="mdiwin('/AdviceAction.do?src=menu','$text.get("bottom.lb.advice")')" title="单击查看全部$text.get("bottom.lb.advice")"></a>
			    	<div id="adviceId"></div>
			    	<div id="adviceList" class="advice">
						<iframe style="width:100%;height:100%;position:absolute;bottom:-1px;left:-1px;z-index:-1;border:none;filter:Alpha(opacity=1);" frameborder=1></iframe>
						<div style="width:1px;height:37px;background:#fff;position:absolute;right:-1px;bottom:0;"></div>
						<div class="interior">
							<ul id="adviceBody">
									
							</ul>
						</div>
					</div>
				</div>
		    </li>
		    
		    <!--<li class="meddage-li" onclick="javascript:mdiwin('/MessageQueryAction.do?src=menu','$text.get("oa.mydesk.news")');">
		    	 -->
		      <li class="meddage-li" onclick="openkk();">
		    	<div class="d-message">
			    	<a class="n2" title="$text.get("oa.mydesk.news.number")"></a>
			    	<div id="messageId"></div>
			    	<div id="msgList" class="message" style="display: none;">
					  	<iframe style="width:100%;height:100%;position:absolute;top:0px;left:0px;z-index:-1;border:none;filter:Alpha(opacity=1);" frameborder="1"></iframe>
						<div style="width:1px;height:36px;background:#fff;position:absolute;right:0;bottom:1px;"></div>
						<div id="userIdDiv" class="msgpoup_middle">
							
						</div>
					</div>
				</div>
		    </li>
	    </ul>
	 
    </div>
    
  </div>

  <div id="menu_more">
  	<iframe width=100%; height=100%; style="position:absolute;top:0px;left:0px;z-index:-1;border:none ;filter:Alpha(opacity=1)" frameborder=1></iframe>
  	<div class="h">
        <div class="mm_t"><a href="#" ></a></div>
    </div>
    <div class="bd">
        <div id="scroll">
      		<div id="scroLeft">
		        <ul id="menuList">
		        
		        </ul>
	        </div>
		    <div id="scroRight" >
	              <div id="scroLine"></div>
	        </div>
   		</div>
        <a class="i_all i_reset" href="javascript:mdiwin('/moduleFlow.do?operation=destSet&src=menu','我的快捷方式')">设置</a>
        </div>
        <div class="f"></div>
   </div>
	<div class="tab_content" style="width:100%;height:80%;">
		<div id="jerichotab_contentholder" class="content"></div>
	</div>
  </div>
  
<script type="text/javascript">
var titles = "";
var urls = "";
var moduleType = "";
var urls1 = "/newMenu.do?keyId=desk&name=我的工作台&operation=0";
var titles1 = "我的工作台";
var urls2 = "/newMenu.do?keyId=28141812_0811271603291400086&name=我的工作台&operation=0"; 
#if($LoginBean.defDesk != "")
	moduleType = "$!globals.get($globals.strSplit($LoginBean.defDesk,','),0)";
	titles = "$!globals.get($globals.strSplit($LoginBean.defDesk,','),1)";
	#if("$LoginBean.showDesk" == "ERP")
		urls = "/newMenu.do?keyId=28141812_0811271603291400086&name=ERP导航&operation=0";	
	#else
		urls = "$!globals.get($globals.strSplit($LoginBean.defDesk,','),2)";
	#end		
#end
if(urls == ""){
	if($LoginBean.canHr==1){
		urls = urls1+"&moduleType=4";
	}
	if($LoginBean.canCrm==1){
		urls = urls1+"&moduleType=3";
	}
	if($LoginBean.canOa==1){
		urls = urls1+"&moduleType=2";
	}
	if($LoginBean.canJxc==1){
		urls = urls2;
	}
	if($LoginBean.canHr==0 && $LoginBean.canCrm == 0 && $LoginBean.canOa == 0 && $LoginBean.canJxc == 0){
		urls = urls1+"&moduleType=2";
	}
	
	titles = titles1;
	var canStr = new Array($LoginBean.canJxc,$LoginBean.canOa,$LoginBean.canCrm,$LoginBean.canHr);
	
	if("$LoginBean.defSys"!=""){
		if("$!LoginBean.defSys"!="1"){
			if(canStr[($LoginBean.defSys)-1]==1){
				urls = urls1+"&moduleType=$LoginBean.defSys";
				titles = titles1;
			}
		}else{
			if($LoginBean.canJxc==1){
				urls = urls2;
			}
		}
	}
	
	if("$LoginBean.showDesk"!=""){
		if("$!LoginBean.showDesk"=="OA"){
			if(canStr[1]==1){
				urls = urls1+"&moduleType=2";
				titles = titles1;
			}
		}else{
			if($LoginBean.canJxc==1){
				urls = urls2;
			}
		}
	}
	
}
var jericho = {
    buildTabpanel: function() {
        jQuery.fn.initJerichoTab({
            renderTo: '.menuTab',
            uniqueId: 'myJerichoTab',
            tabs: [{
                title: titles,
                closeable: false,
                tabFirer: urls,
                data: { dataType: 'iframe', dataLink: urls }
            }],
            activeTabIndex: 0,
            loadOnce: true
        });
   }
}
jericho.buildTabpanel();
#if("$!adviceMdiwin"!="" && $!adviceMdiwin.indexOf("mdiwin(")!=-1)
	$!adviceMdiwin ;
#end

</script>

<script language="javascript">


</script>
<div class="about-soft">
	<a class="koron" href="http://www.koronsoft.com" target="_blank"><img src="/style/v7/images/logo.png" alt="科荣软件" /></a>
	<p>
		<em><i class="i-r">软件版本：</i><i>科荣AIO-7.2106</i></em>
		<em><i class="i-r">授权给：</i><i>深圳科荣软件有限公司</i></em>
		<em><i class="i-r">注册时间：</i><i>2014.01.01</i></em>
		<em><i class="i-r">版权所有：</i><i>深圳科荣软件有限公司</i></em>
	</p>
	<b class="close-about" title="关闭"></b>
</div>
<div class="in-bg"></div>

<div class="searchDiv">
<input name="search" id = "search" value="有问题,来找我"  disableautocomplete="true" autocomplete="off"/>
<ul style="display:none">
	<!-- <li href="http://q.krrj.cn/s.d?seriaId=$!globals.getSystemState().dogId&kw=@condition">使用帮助,反馈,建议<br></li>  -->
	#foreach($fq in $fastQuery)
	<li href="$globals.get($fq,1)">$globals.get($fq,0)<br><br></li>
	#end
</ul>
</div>

</body>
</html>

