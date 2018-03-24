<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=8"/>
<link href="/style/images/aio-icon.png" rel="apple-touch-icon"/>
<title>$globals.getCompanyName('')</title>
<link type="text/css" rel="stylesheet" href="/style/css/demoMenu.css" />
<link type="text/css" rel="stylesheet" href="/style/v7/css/loginBase.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/body_v7.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="/js/jquery.jerichotab.js"></script>
<script type="text/javascript" src="/js/menu/jquery.bgiframe.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/menu/jquery.dimensions.js"></script>
<script type="text/javascript" src="/js/menu/jquery.positionBy.js"></script>
<script type="text/javascript" src="/js/menu/jquery.jdMenu.js"></script>

<script type="text/javascript">

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
		jQuery.get(url,function(data){ 
			cururl = url;
			$(".jd_menu").html(data);
			$('ul.jd_menu').jdMenu();
			var liNum; //新菜单li个数
			if(defSys == 1){
				$("#menu").find("ul.jd_menu").css("background-position","0 0").removeClass().addClass("jd_menu").addClass("erp_bg");
				liNum = $(".menu_a ul.jd_menu li.bd_li").size();
			}else if(defSys == 2){ 
				$("#menu").find("ul.jd_menu").css("background-position","0 -24px").removeClass().addClass("jd_menu").addClass("oa_bg");
				liNum = $(".menu_a ul.jd_menu li.bd_li").size();
			}else if(defSys == 3){
				$("#menu").find("ul.jd_menu").css("background-position","0 -48px").removeClass().addClass("jd_menu").addClass("crm_bg");
				liNum = $(".menu_a ul.jd_menu li.bd_li").size();
			}else if(defSys == 4){
				$("#menu").find("ul.jd_menu").css("background-position","0 -72px").removeClass().addClass("jd_menu").addClass("hr_bg");
				liNum = $(".menu_a ul.jd_menu li.bd_li").size();
			}
			$("span.bg_"+defSys).addClass("bg_1_c").siblings("span").removeClass("bg_1_c");
			$(".bd_li").last().css("background","none");
			$("ul.jd_menu").css("width",liNum*78);
		}); 
	}
}
//新菜单-功能模块判断
function NewMenuSpan()
{
	var sLen =$(".lavaLampWithImage span").size();
	var hNumber = false;
	for(var i=0;i<sLen;i++){
		$(".lavaLampWithImage span:eq("+i+")").addClass("lf_"+i);
		var z = $(".lavaLampWithImage span:eq("+i+")").attr("Number");
		if(z ==defSys ){
			changeDefSys(defSys);
			hNumber = true;
		}
	}
	if(hNumber == false){
		var oNum = $(".lavaLampWithImage span:eq(0)").attr("Number");
		changeDefSys(oNum);
	}
}

	
$(function(){
	$(".bottomL").mouseover(function(){
		var sNum = $(this).find(".menuList").size();
		$(this).find(".menuList:first").css("border","0");
		$(this).find(".menuListWp").show().width(sNum*105+sNum);
		if(sNum>4){
			$(this).find(".menuListWp").css("left",-(sNum*105+sNum)/2);
		}	
	}).mouseout(function(){
		$(this).find(".menuListWp").hide();
	});
	$(".topL").mouseover(function(){
		$(this).find(".topListWp").show();	
	}).mouseout(function(){
		$(this).find(".topListWp").hide();
	});
	
	NewMenuSpan();
})
</script>
</head>
<body>
<!-- header头部      分割线     Start -->
<div class="headarea" id="headarea">
	<!-- oHeader Start 分割线 --> 
	<div class="oHeader">
    	<a href="#"><img src="/style/v7/images/logo.png" alt="科荣V7" /></a>
        <div>
        	<ul class="topU">
            	<li class="topL">
            		<em>你好，</em><i class="downExtend">黄振<b class="triangle"></b></i>
            		<div class="topListWp">
            			<div class="person">
            				<a href="#">
            					<img src="/style/v7/images/txx.jpg" alt="更换头像" />
            				</a>
            				<ul>
            					<li><a>软件设置</a></li>
            					<li><a>下载中心</a></li>
            					<li><a>关于软件</a></li>
            					<li><a>退出登录</a></li>
            				</ul>
            			</div>
            		</div>
            	</li>
                <li class="topL">
                	<i class="downExtend">代办<b class="triangle"></b></i>
                	<div class="topListWp">
          				<ul>
          					<li><a>软件设置</a></li>
          					<li><a>下载中心</a></li>
          					<li><a>关于软件</a></li>
          					<li><a>退出登录</a></li>
          				</ul>
            		</div>
                </li>
                <li class="topL">
                	<i class="downExtend">通知<b class="triangle"></b></i>
                	<div class="topListWp">
          				<ul>
          					<li><a>软件设置</a></li>
          					<li><a>下载中心</a></li>
          					<li><a>关于软件</a></li>
          					<li><a>退出登录</a></li>
          				</ul>
            		</div>
                </li>
                <li class="topL">
                	<i class="downExtend">消息<b class="triangle"></b></i>
                	<div class="topListWp">
          				<ul>
          					<li><a>软件设置</a></li>
          					<li><a>下载中心</a></li>
          					<li><a>关于软件</a></li>
          					<li><a>退出登录</a></li>
          				</ul>
            		</div>
                </li>
            </ul>
            <ul class="bottomU">
            	<li class="bottomL sel">
            		<b class="icon16"></b><i>业务系统</i><b class="bdlink_w2"></b>
            		<!-- MenulistWp Start 分割线 -->
            		<div class="menuListWp">
            			<div class="menuList">
            				<p class="tP">采购管理</p>
            				<ul class="uList">
            					<li>采购管理</li>
            					<li>盘点管理</li>
            					<li>存货调单价</li>
            					<li>调拨管理</li>
            					<li>其他出库单</li>
            				</ul>
            			</div>
            			<div class="menuList">
            				<p class="tP">销售管理</p>
            				<ul class="uList">
            					<li>仓库管理</li>
            					<li>盘点管理</li>
            					<li>存货调单价</li>
            					<li>调拨管理</li>
            					<li>其他出库单</li>
            				</ul>
            			</div>
            			<div class="menuList">
            				<p class="tP">销售管理</p>
            				<ul class="uList">
            					<li>仓库管理</li>
            					<li>盘点管理</li>
            					<li>存货调单价</li>
            					<li>调拨管理</li>
            					<li>其他出库单</li>
            				</ul>
            			</div>
            			<div class="menuList">
            				<p class="tP">销售管理</p>
            				<ul class="uList">
            					<li>仓库管理</li>
            					<li>盘点管理</li>
            					<li>存货调单价</li>
            					<li>调拨管理</li>
            					<li>其他出库单</li>
            					<li>其他出库单</li>
            				</ul>
            			</div>
            			<div class="menuList">
            				<p class="tP">销售管理</p>
            				<ul class="uList">
            					<li>仓库管理</li>
            					<li>盘点管理</li>
            					<li>存货调单价</li>
            					<li>调拨管理</li>
            					<li>其他出库单</li>
            					<li>其他出库单</li>
            				</ul>
            			</div>
            		</div>
            		<!-- MenulistWp End 分割线 -->
            	</li>
                <li class="bottomL">
                	<i>日常办公</i><b class="bdlink_w2"></b>
                	<!-- MenulistWp Start 分割线 -->
            		<div class="menuListWp">
            			<div class="menuList">
            				<p class="tP">采购管理</p>
            				<ul class="uList">
            					<li>采购管理</li>
            					<li>盘点管理</li>
            					<li>存货调单价</li>
            					<li>调拨管理</li>
            					<li>其他出库单</li>
            				</ul>
            			</div>
            			<div class="menuList">
            				<p class="tP">采购管理</p>
            				<ul class="uList">
            					<li>采购管理</li>
            					<li>盘点管理</li>
            					<li>存货调单价</li>
            					<li>调拨管理</li>
            					<li>其他出库单</li>
            				</ul>
            			</div>
            			<div class="menuList">
            				<p class="tP">销售管理</p>
            				<ul class="uList">
            					<li>仓库管理</li>
            					<li>盘点管理</li>
            					<li>存货调单价</li>
            					<li>调拨管理</li>
            					<li>其他出库单</li>
            				</ul>
            			</div>
            			<div class="menuList">
            				<p class="tP">销售管理</p>
            				<ul class="uList">
            					<li>仓库管理</li>
            					<li>盘点管理</li>
            					<li>存货调单价</li>
            					<li>调拨管理</li>
            					<li>其他出库单</li>
            				</ul>
            			</div>
            			<div class="menuList">
            				<p class="tP">销售管理</p>
            				<ul class="uList">
            					<li>仓库管理</li>
            					<li>盘点管理</li>
            					<li>存货调单价</li>
            					<li>调拨管理</li>
            					<li>其他出库单</li>
            					<li>其他出库单</li>
            				</ul>
            			</div>
            		</div>
            		<!-- MenulistWp End 分割线 -->
                </li>
                <li class="bottomL">
                	<i>客户管理</i><b class="bdlink_w2"></b>
                	<!-- MenulistWp Start 分割线 -->
            		<div class="menuListWp">
            			<div class="menuList">
            				<p class="tP">客户管理</p>
            				<ul class="uList">
            					<li>采购管理</li>
            					<li>盘点管理</li>
            					<li>存货调单价</li>
            					<li>调拨管理</li>
            					<li>其他出库单</li>
            				</ul>
            			</div>
            		</div>
            		<!-- MenulistWp End 分割线 -->
                </li>
                <li class="bottomL">
                	<i>分析报表</i><b class="bdlink_w2"></b>
                	<!-- MenulistWp Start 分割线 -->
            		<div class="menuListWp">
            			<div class="menuList">
            				<p class="tP">分析报表</p>
            				<ul class="uList">
            					<li>采购管理</li>
            					<li>盘点管理</li>
            					<li>存货调单价</li>
            					<li>调拨管理</li>
            					<li>其他出库单</li>
            				</ul>
            			</div>
            		</div>
            		<!-- MenulistWp End 分割线 -->
                </li>
                <li class="bottomL">
                	<i>系统管理</i>
                	<!-- MenulistWp Start 分割线 -->
            		<div class="menuListWp">
            			<div class="menuList">
            				<p class="tP">系统管理</p>
            				<ul class="uList">
            					<li>采购管理</li>
            					<li>盘点管理</li>
            					<li>存货调单价</li>
            					<li>调拨管理</li>
            					<li>其他出库单</li>
            				</ul>
            			</div>
            		</div>
            		<!-- MenulistWp End 分割线 -->
               	</li>
            </ul>
        </div>
    </div>
    <!-- oHeader End 分割线 -->
    <!-- 菜单2 Start -->
    <div id="menuListId" style="display:none;" >
	    <ul class="lavaLampWithImage" id="1" style="z-index:2;"> 
			#foreach($module in $globals.getMainModule()) 
				#if(($module =='1' && $LoginBean.canJxc==1) || ($module =='2' && $LoginBean.canOa==1) 
					|| ($module =='3' && $LoginBean.canCrm==1) || ($module =='4' && $LoginBean.canHr==1))
					<span class="lava_span bg_$module" Number="$module" onClick="changeDefSys($module);">
					<a href="javascript:void(0)" #if($module=='1') title="$text.get('role.lb.pss')" #elseif($module=='2') title="$text.get('role.lb.oa')" #elseif($module=='3') title="$text.get('role.lb.crm')" #elseif($module=='4') title="$text.get('role.lb.hr')" #end >#if($module=='1')ERP #elseif($module=='2')OA #elseif($module=='3')CRM #elseif($module=='4')HR #end</a></span>	
				#end
			#end
			
		</ul>
		<div id="menu" class='menu_a' >
			<ul class='jd_menu' >
				
			</ul>
		</div>
    </div>
    <!-- 菜单2 End -->
	<!-- menuTagList  Start 分割线 -->
	<div class="menuTagList">
		<div>
  			<div class="menuTab" id="menuTabId"></div>
  			<div class="menuSearch">
  				<span>
  					<input type="text" placeholder="请输入关键字" />
  					<b></b>
  				</span>
  			</div>
  		</div>
  	</div>
  	<!-- menuTagList  End 分割线 -->
</div>
<!-- header头部      分割线     End -->
	<div class="tab_content" style="width:100%;height:80%;">
		<div id="jerichotab_contentholder" class="content"></div>
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
	urls = "$!globals.get($globals.strSplit($LoginBean.defDesk,','),2)";
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
	titles = titles1;
	var canStr = new Array($LoginBean.canJxc,$LoginBean.canOa,$LoginBean.canCrm,$LoginBean.canHr);
	if("$LoginBean.defSys"!=""){
		if("$LoginBean.defSys"!="1"){
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

$("#menuTabId").width("800px");

</script>


</body>
</html>
