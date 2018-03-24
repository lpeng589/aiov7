<!DOCTYPE html >
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css">
<link type="text/css" rel="stylesheet" href="/style/css/body2.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
function showreModule(title,href){		
	top.showreModule(title,href);	
}
$(function(){
	$(".menuReport").click(function(){
		$(this).children("ul").show().end().siblings().children("ul").hide();
	});
	$(".l_m_ul:eq(0)>li.de-desk-li:eq(0)").css("border-top","0");
	#if("$keyId"=="desk")
		showDesk();
	#end
	$('#desk_li').bind('click', function() {
		var url = '/newMenu.do?keyId=desk&name=我的桌面&operation=0&moduleType='+$(this).attr("moduleType");
		window.location.href=url;
	});
	$("#b_ld").bind("click",function(){
		stopPropagation();
		addDesk('/newMenu.do?keyId=desk&name=我的工作台&moduleType='+$(this).attr("moduleType")+'&operation=0','我的工作台',$(this).attr("moduleType"));
	});

});
//2013-08-05
function NewMenuAction(){
	//中间菜单居中
	var sWidth=window.screen.width;
	var tWidth = $("table.tbl_wp").width();
	var cl = sWidth -tWidth -110;
	if(cl < 270){
		$(".column_r").width(cl-20);
		$("#cmainId").width(tWidth);
	}else{
		#if("$keyId"=="desk")
			//$("#cmainId").width("90%");
		#else
			$("#cmainId").width(sWidth-400);
		#end
	}
	$("table.tbl_wp").width(tWidth);
	//右边菜单
	var uH = 0;
	$(".bd_ul_dv").each(function(){
		var tHeight = $(this).find("ul.bd_ul").height();
		if(tHeight > 195){
			$(this).addClass("uH");
			uH++; 
		}else{
			$(this).addClass("nH");
		}
	});
	if(uH == "1"){
		$(".bd div.uH").height(390-$(".bd div.nH ul").height());
	}
	if(uH == "2"){
		$(".bd_ul_dv").each(function(){
			$(this).height("195px");
		});
	}
	//左边菜单小图标








	var sNum = $("ul.l_m_ul li").length;
	var zNum = 0;
	for(var i=0;i<sNum;i++){
		var bg = 10-(zNum*30);
		$("ul.l_m_ul li:eq("+i+") i.bg_i").css("background-position","10px "+bg+"px");
		zNum++;
		if(zNum == "7"){
			zNum =0;
		}
	}
	//图标动画
	var tdLen = $(".img_td .img_p").length;
	var tdNum = 1;
	for(var t=0;t<tdLen;t++){
		$(".img_td .img_p:eq("+t+") img").addClass("mm_"+tdNum);
		if(tdNum == "4"){
			tdNum = 1;
		}
		tdNum++;
	}
	$(".ddv_dz").mouseover(function(){
		$(this).find("div.add_fav").show();
	}).mouseout(function(){
		$(this).find("div.add_fav").hide();
	});
}
//加入收藏
function addMyDest(url,title){
	parent.addMyDest(url,title);
}

//显示桌面
function showDesk(){
	$(".l_menu").find(".sel").removeClass("sel");
	$("#desk_li").addClass("sel");
	$("#cmainId").html("<iframe id='firameMain' frameborder='false' src='/MyDesktopAction.do?src=menu' name='firameMain' style='margin-top: 10px;' scrolling='no' width='100%' height='100%'></iframe>");
}

//设置桌面
function addDesk(url,title,mainModule){
	var urls = "/UtilServlet?operation=setDesk&url="+encodeURIComponent(url)+"&title="+encodeURIComponent(title)+"&mainModule="+mainModule;
	jQuery.ajax({ type: "POST", url: urls, success: function(msg){
		if(msg == "OK"){
			asyncbox.tips('设为我的工作台成功','success');
		}else{
			asyncbox.tips('设为我的工作台失败','error');
		}
	}});
}
function stopPropagation(e) {
    e = e || window.event;
    if(e.stopPropagation) { //W3C阻止冒泡方法
        e.stopPropagation();
    } else {
        e.cancelBubble = true; //IE阻止冒泡方法
    }
}
</script>
<style type="text/css">
.add_fav{display:none;cursor:pointer;border-radius:2px;position:absolute;top:25px;z-index:999;right:8px;width:16px;height:16px;zoom:1;}
.add_fav i{background:url(/style/images/body2/fav_star.png) no-repeat 0 0;display:inline-block;width:16px;height:16px;}
body{background:url(/style/images/body2/showM.png) repeat 0 0;}
.cmain table td{width:65px;line-height:0;}
.cmain table td>div{width:65px;height:65px;overflow:hidden;}
.cmain table td .link_p{width:65px;height:65px;overflow:hidden;}
.cmain table td p{width:65px;height:16px;line-height:16px;}
.cmain table td p.img_p{width:65px;cursor:pointer;text-align:center;position:relative;background:url(/style/images/body2/show_bg.png) no-repeat 8px 0;height:44px;}
.cmain table td p.img_p img{position:absolute;top:0;left:9px;width:40px;}
.cmain table td p.img_p:hover img.mm_1{-webkit-animation-name:myMovieO;-webkit-animation-duration:0.5s;}
.cmain table td p.img_p:hover img.mm_2{-webkit-animation-name:myMovieO;-webkit-animation-duration:0.5s;}
.cmain table td p.img_p:hover img.mm_3{-webkit-animation-name:myMovieO;-webkit-animation-duration:0.5s;}
.cmain table td p.img_p:hover img.mm_4{-webkit-animation-name:myMovieF;-webkit-animation-duration:0.5s;}
.cmain table td p.txt_p{width:65px;cursor:pointer;text-align:center;color:#2b4157;}
.cmain table td span.img_span{width:65px;display:block;position:relative;background:url(/style/images/body2/show_bg.png) no-repeat 8px -45px;height:44px;text-align:center;}
.cmain table td span.img_span img{width:40px;position:absolute;left:8px;}
.cmain table td span.txt_span{width:65px;line-height:16px;display:block;color:#2b4157;}
@-webkit-keyframes myMovie{
	0%{top:1px;}   	5%{left:10px;}
	10%{top:0;} 	15%{left:9px;}
	20%{top:1px;} 	25%{left:10px;}
	30%{top:0;} 	35%{left:9px;}
	40%{top:1px;} 	45%{left:10px;}
	50%{top:0;} 	55%{left:9px;}
	60%{top:1px;} 	65%{left:10px;}
	70%{top:0;} 	75%{left:9px;}
	80%{top:1px;} 	85%{left:10px;}
	90%{top:0;} 	95%{left:9px;}
}
@-webkit-keyframes myMovieO{
	0%{opacity:1;-webkit-transform:scale(1);}
	50%{opacity:0;-webkit-transform:scale(5);}
	100%{opacity:1;-webkit-transform:scale(1);}
}
@-webkit-keyframes myMovieT{
	0%{top:2px;}   	5%{top:0;}
	10%{top:2px;} 	15%{top:0;}
	20%{top:2px;} 	25%{top:0;}
	30%{top:2px;} 	35%{top:0;}
	40%{top:2px;} 	45%{top:0;}
	50%{top:2px;} 	55%{top:0;}
	60%{top:2px;} 	65%{top:0;}
	70%{top:2px;} 	75%{top:0;}
	80%{top:2px;} 	85%{top:0;}
	90%{top:2px;} 	95%{top:0;}
}
@-webkit-keyframes myMovieF{
	0%{left:7px;}   5%{left:9px;}
	10%{left:7px;} 	15%{left:9px;}
	20%{left:7px;} 	25%{left:9px;}
	30%{left:7px;} 	35%{left:9px;}
	40%{left:7px;} 	45%{left:9px;}
	50%{left:7px;} 	55%{left:9px;}
	60%{left:7px;} 	65%{left:9px;}
	70%{left:7px;} 	75%{left:9px;}
	80%{left:7px;} 	85%{left:9px;}
	90%{left:7px;} 	95%{left:9px;}
}

.OAWidth{width: 98%;margin: 0 0 0 10px;}
</style>
</head>

<body onload="NewMenuAction();">
<form  method="post" scope="request" name="form" action="/newMenu.do">
<div id="mainarea">
      <div class="column_l">
      	<div class="cmain" id="cmainId" #if("$!defSys"=="2") style="width:98%;margin: 0 0 0 10px;" #else style="width:90%;" #end>
<script type="text/javascript">
#if("$keyId"=="desk")
	$("#cmainId").height($(document).height()-30);
#else
	$("#cmainId").height($(document).height()-50);
#end


</script>
  <table border="0" cellpadding="0" cellspacing="0" align="center" class="tbl_wp" style="margin:0 auto;padding:15px 0 0 0;">
 		#set($int=0)
 		#set($strUrl="")
 		#foreach($elment in [1..7])
 			#set($int=$int+1)
 			<tr>
 				#set($td=0)
 				#foreach($elment in [1..11])
 					#set($td=$td+1)
 					#set($tdName="")
 					#set($tdName=$!moduHash.get($!tdImgLinkList.get("tdx${int}y$td")).get(1))
 					<td id="tdx${int}y$td" name="tdx${int}y$td" align="center" class="img_td">
 						  #if($!tdImgsList.get("tdx${int}y$td"))
								#if($!tdImgLinkList.get("tdx${int}y$td"))
									<div class="ddv_dz" style="display:inline-block;position:relative;">
									#set($strUrl=$!moduHash.get($!tdImgLinkList.get("tdx${int}y$td")).get(0))
									#set($opUrl=$strUrl)
									#if($strUrl.indexOf("moduleType=")!=-1 && $globals.getMOperation($strUrl).query()==false)
									#set($opUrl=$strUrl.substring(0,$strUrl.lastIndexOf("&")))
									#end
									#if($globals.getMOperation($opUrl).add()
										|| $globals.getMOperation($opUrl).query())
					 	 			#set($operation=$!tdOpType.get("tdx${int}y$td"))
					 	 			#if($strUrl.indexOf("/UserFunctionQueryAction.do")==-1)
					 	 				#if($strUrl.indexOf("?")==-1)
					 	 				#set($strUrl=$strUrl+"?src=menu")
					 	 				#else
					 	 				#set($strUrl=$strUrl+"&src=menu")
					 	 				#end
					 	 				#if($strUrl.indexOf("/VoucherManageAction.do")!=-1 && "$!operation"=="6")
					 	 				#set($strUrl='/VoucherManage.do?operation=6&isEspecial=list')
					 	 				#end
					 	 			#else
					 	 			#if($operation==6)
					 	 			#set($strUrl=$strUrl+'&src=menu&operation='+$operation)
					 	 			#else
					 	 			#set($strUrl=$strUrl+'&src=menu')
					 	 			#end
					 	 			#end
					 	 			#if($!tdDis.get("tdx${int}y$td").length()>0)
					 	 			#set($tdName=$!tdDis.get("tdx${int}y$td"))
					 	 			#end
					 	 			<div class="add_fav" onclick="addMyDest('$!strUrl','$!tdName');"><i title='收藏此菜单'></i></div>
					 	 			<p class="img_p"><img onclick="showreModule('$!tdName','$!strUrl');" title="$!tdName" src="/style/images/newMenu/$!tdImgsList.get("tdx${int}y$td")"/></p>
 									<p class="txt_p" onclick="showreModule('$!tdName','$!strUrl');" title="$!tdName">#if($!tdName.length()>5)$!tdName.substring(0,5)#else $!tdName #end</p>
 						 			#else
 						 			<span class="img_span" title="$!tdName"><img src="/style/images/newMenu/$!tdImgsList.get("tdx${int}y$td")" /></span></span>
	 								<span class="txt_span" style="color:#999999;" title="$!tdName">#if($!tdName.length()>5)$!tdName.substring(0,5)#else $!tdName #end </span>
 						 			#end
 						 			</div>
						  		#else
						  			<div>
									#if($!tdImgsList.get("tdx${int}y$td").indexOf("sale")>-1)
									<span class="img_span"><img src="style/images/newMenu/$!tdImgsList.get("tdx${int}y$td")" /></span>
									#if($!tdDis.get("tdx${int}y$td").length()>0)
									<span class="txt_span" style="color:#999999;" title="$!tdDis.get("tdx${int}y$td")">#if($!tdDis.get("tdx${int}y$td").length()>5)$!tdDis.get("tdx${int}y$td").substring(0,5)#else $!tdDis.get("tdx${int}y$td") #end </span>
									#end
									#else
 						  		   	<p class="link_p"><img src="/style/images/newMenu/$!tdImgsList.get("tdx${int}y$td")"/></p>
									#end
									</div>
						  		#end
						 #else
 							 &nbsp;
 						 #end
 					 </td>
 				#end
 			</tr>
 		#end
	  	</table>
      	</div>
      	#if("$defSys" !="2" )
      	<div class="l_menu">
	      	<ul class="l_m_ul">
	      	<li id="desk_li" class="l_desk_li" style="display: none;"><i class="home_i"></i><i class="b_i" title="我的桌面">我的桌面</i><b title="设为工作台" id="b_ld"></b></li>
	      	#set($mydesk="")
	       	#foreach($menu in $MenuList)
	      		#if($velocityCount<7)
	      		<li class="de-desk-li #if($!globals.get($menu,0)=="$keyId") sel #end " onClick="location.href='/newMenu.do?keyId=$!globals.get($menu,0)&name=$!globals.get($menu,1)&operation=0'"><i class="bg_i"></i><i class="b_i" title="$!globals.get($menu,1)">$!globals.get($menu,1)</i><b title="设为工作台" onclick="stopPropagation();addDesk('/newMenu.do?keyId=$!globals.get($menu,0)&name=我的工作台&operation=0','我的工作台','$!globals.get($menu,2)');"></b></li>
	      		#set($mydesk="$!globals.get($menu,2)")
	      		#end
	       	#end
	      	</ul>
	      	<ul class="l_m_ul">
	       	#foreach($menu in $MenuList)
	      		#if($velocityCount>6)
	      		<li #if($!globals.get($menu,0)=="$keyId") class="sel" #end onClick="location.href='/newMenu.do?keyId=$!globals.get($menu,0)&name=$!globals.get($menu,1)&operation=0'"><i class="bg_i"></i><i class="b_i" title="$!globals.get($menu,1)">$!globals.get($menu,1)</i><b title="设为工作台" onclick="stopPropagation();addDesk('/newMenu.do?keyId=$!globals.get($menu,0)&name=我的工作台&operation=0','我的工作台','$!globals.get($menu,2)');"></b></li>
	      		#end
	       	#end
	      	</ul>
	      	<script type="text/javascript">
		 		#if("$mydesk"!="1")
		 			//如果是oa显示我的桌面
		 			$(".l_desk_li").show();
		 			$("#b_ld").attr("moduleType","$mydesk")
		 			$("#desk_li").attr("moduleType","$mydesk");
		 		#end
		 	</script>
      	</div>
      <!-- col-r end -->
      </div>
      #end
      #if($funList.size()>0 && $reportList.size()>0)
      <div class="column_r">
        #if($funList.size()>0)
        <div class="rig_box">
          <div class="rig_title">
          	<i class="i_link"></i>
            <em class="w_em">功能列表</em>
            <i class="i_add"></i>
          </div>
          <div class="bd">
          	<div class="bd_ul_dv">
	            <ul class="bd_ul">
		 		#foreach($fun in $funList)
		 			#set($strUrl=$!globals.get($fun,1))
		 			#if($strUrl.indexOf("?")==-1)
		 				#set($strUrl=$strUrl+"?src=menu")
		 			#else
		 				#set($strUrl=$strUrl+"&src=menu")
		 			#end
					<li><a href="javascript:showreModule('$!globals.get($fun,0)','$strUrl')">#if($!globals.get($fun,0).length()>8)$!globals.get($fun,0).substring(0,8)#else $!globals.get($fun,0)#end</a></li>
				#end
	            </ul>
	          </div>
          </div>
      	</div>
      	#end
      	#if($reportList.size()>0)
      	<div class="rig_box">
          <div class="rig_title">
          	<i class="i_link"></i>
            <em class="w_em">报表</em>
            <i class="i_add"></i>
          </div>
          <div class="bd">
          	<div class="bd_ul_dv">
	            <ul class="bd_ul">
		 		#foreach($report in $reportList)
		 			#set($strUrl=$!globals.get($report,1))
		 			#if($strUrl.indexOf("?")==-1)
		 				#set($strUrl=$strUrl+"?src=menu")
		 			#else
		 				#set($strUrl=$strUrl+"&src=menu")
		 			#end
					<li><a href="javascript:showreModule('$!globals.get($report,0)','$strUrl')" title="$!globals.get($report,0)">#if($!globals.get($report,0).length()>8)$!globals.get($report,0).substring(0,8)#else $!globals.get($report,0)#end</a></li>
				#end
	            </ul>
	         </div>
          </div>
        </div>
        #end
        #end
          <!-- col-r end -->
        </div>
        <!-- main end -->
      </div>
       <!-- case end -->
</div>
</form>
</body>
</html>
