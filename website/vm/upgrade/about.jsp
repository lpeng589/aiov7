<html> 
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>关于软件</title>
<link type="text/css" rel="stylesheet" href="/style/css/about.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<link type="text/css" rel="stylesheet" href="/style/css/demo2.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
$(function(){
	$("#scroll-wrap").height(document.body.clientHeight-18);
});


function upgradeSoft(){
	#if("$LoginBean.id"!="1")
		alert("请用admin帐号执行此操作");
	#else
		if(confirm("更新软件期间将会停止服务，直到升级完成，是否继续？")){
			var timestamp = Date.parse(new Date()); 
			jQuery.get("/CommonServlet?operation=setUpgrade&t="+timestamp,function(data){
				alert(data);
			});
		}
	#end
}
 
function checkVersion(){
	setTimeout(function(){
		var timestamp = Date.parse(new Date()); 
		jQuery.get("/CommonServlet?operation=checkVersion&t="+timestamp,			
			function(data){ 
				if(data.length > 0){
					var version = data;
					var firv = version.substring(0,version.lastIndexOf("."));
					var lstv = version.substring(version.lastIndexOf(".") +1);
					//version=='V$!{version_id}.0.$!order_id'
					if(firv > 'V$!{version_id}.0' || (firv == 'V$!{version_id}.0' && Number(lstv) > Number($!order_id) )){
						$("#newversion").html('当前最新版本'+version+'<a href="javascript:upgradeSoft();" style="margin-left:20px">立即更新</a>');
					}else{
						$("#newversion").html('当前没有可用更新');					
					}
					
				}
		});
	},1000);  	
}


</script>

</head>
<body class="html" onload="checkVersion()">
<div id="scroll-wrap">
	<div class="wrap">
    	<p id="pageTitle">$text.get("upgrade.title.about")</p>
		<div id="bodyUl">
			<ul>
				<li>
					<div class="itemName">加密方式：</div>
					<div class="d-left">
						#if($!globals.getSystemState().isFree())  
						基础版 #if("$!code" != "")(序列号 $code) #end
						#elseif("$!globals.getSystemState().encryptionType" == "3")
						软加密 (序列号 $code)
						#elseif("$!globals.getSystemState().encryptionType" == "2")
						加密狗 ($text.get("upgrade.lb.id") $code)
						#else
						硬加密 ($text.get("upgrade.lb.id")  $code) 
						#end
					</div>
					#if("$!LoginBean.id"=="1")
					<span onClick="top.mdiwin('/RegisterAction.do?regFlag=2','注册授权');" class="regbut btn btn-small btn-danger">注册授权</span>
					#end
					<a href="#if("$LoginBean.id"!="1") javascript: alert('请使用管理员帐号登入科荣软件在线平台'); #else  http://m.krrj.cn/login.htm?operate=aiologin&validcode=$validcode; #end" target="_blank" class="regbut btn btn-small btn-danger">购买服务、产品、升级用户数</a>
				</li>
				
				<li>
					<div class="itemName">$text.get("com.product.name")：</div>
					<div class="d-left">
						#set($baseVersioin="")
						#if($globals.hasMainModule("1"))
							#if($!globals.getSystemState().funQJ)
								#set($baseVersioin="旗舰版")
							#elseif($!globals.getSystemState().funZY)
								#set($baseVersioin="专业版")	
							#else
								#set($baseVersioin="基础版")	
							#end
						#end
												
						$globals.getSysVersionName() $baseVersioin 	
					</div>
						<span  style="float: left;margin-left:30px;">远程访问地址</span>
						#if("$!globals.getSystemState().remoteName"=="")
						<a href="javascript:top.mdiwin('/SystemSetAction.do?page=remoteSet','系统配置');" style="padding-left:10px">设置</a>
						#else
						<a href="http://vip.krrj.cn/$!globals.getSystemState().remoteName" target="_blank"style="padding-left:10px">http://vip.krrj.cn/$!globals.getSystemState().remoteName</a>
						#end
				</li>
				<li>
					<div class="itemName">产品版本：</div>
					<div class="d-left">
						V$!{version_id}.0.$!order_id 
						<span style="margin-left:40px" id="newversion">					
						正在检查更新。。。
						</span>
					</div>
				</li>	
				
			#if("$!globals.getSystemState().dogState" == "0")
				<li>
					<div class="itemName">注册公司：</div>
					<div class="d-left">
						#if("$!globals.getCompanyName()" == "") 
						 	#if("$!globals.getSystemState().companyName" == "")  $text.get("upgrade.msg.noReg")  #else $globals.getSystemState().companyName   #end
						#else
							$!globals.getCompanyName()
						#end
					</div>
				</li>			
				<li>
					<div class="itemName">$text.get("upgrade.lb.regdate")：</div>
					<div class="d-left">
					#if("$!globals.getSystemState().registerDate" == "")  
						$text.get("upgrade.msg.noReg")  
					#else 
						$globals.getSystemState().registerDate  
						#if($!globals.getSystemState().grantDate != "") (授权期至$!globals.getSystemState().grantDate) #end
					#end
					</div>
				</li>
			
				#end
				<li>
					<div class="itemName">帐套运行状态：</div>
					<div class="d-left">
						#if("$!globals.getSystemState().accountType" == "0")  
						正常运行
						#else
						仅查询  
						#end		
						#if("$!LoginBean.id"=="1")		
						<a href="javascript:top.mdiwin('/UpgradeAction.do?operation=4','帐套使用详情');" style="margin-left:20px">帐套使用详情</a>
						#end
					</div>
				</li>
				<li>
				<div class="itemName">$text.get("upgrade.lb.iModule")：</div>
				<div class="d-left">
				#foreach($erow in $globals.getEnumerationItems("MainModule"))
					#if($globals.hasMainModule($erow.value) && "$erow.value" != "0" && "$erow.value" != "4")
					<font>$erow.name  
						#if("$erow.value" == "1")
						(#if($globals.getSystemState().userNum >= 5000)无限#else$globals.getSystemState().userNum#end 用户)
						#elseif("$erow.value" == "2")
						(#if($globals.getSystemState().oaUserNum >= 5000)无限#else$globals.getSystemState().oaUserNum#end 用户)
						#elseif("$erow.value" == "3")
						(#if($globals.getSystemState().crmUserNum >= 5000)无限#else$globals.getSystemState().crmUserNum#end 用户)
						#elseif("$erow.value" == "4")
						(#if($globals.getSystemState().hrUserNum >= 5000)无限#else$globals.getSystemState().hrUserNum#end 用户)
						#end
					</font>				
					#end
				#end
				</div>
				</li>
				<li>
				<div class="itemName">增强包：</div>
				<div class="dz-left">				
				#if($globals.getSystemState().funUserDefine)<font>	$text.get("upgrade.lb.userDefine")	  </font>#end 		
				#if($globals.getSystemState().funMoreCurrency)<font>	$text.get("upgrade.lb.moreCurrency")</font>#end
				#if($globals.getSystemState().funAttribute)<font>	商品属性管理	  </font>#end 
				#if($globals.getSystemState().funZQYW)<font>	增强业务	  </font>#end 
				</div>
				</li>
	      <li>
	        <div class="itemName">授权帐套数量：</div>
	        <div class="d-left">
	        $globals.getSystemState().serverCount
	        </div>
	      </li>
	      </u>
		</div>
		<div class="container">
			   <p style="font-size: 14px;color: #166492;">产品动态:</p>
			   <div class="roll-wrap roll_row" id="e">
		        <ul class="roll__list" id = "roll__list"style="position: absolute; left: 0; top: 0;">
	        	#foreach($item in $productInfos)
	        	#set($len = $item.length()*16)
				 <li style="width:${len}px;">$item</li>
				#end 
		        </ul>
		    </div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/js/rollSlide.js"></script>
<script>
	
      jQuery(function($) {
    	   $('#a').rollSlide({
   	        orientation: 'left',
   	        num: 1,
   	        v: 1000,
   	        space: 3000,
   	        isRoll: false
   	    });
   	    $('#b').rollSlide({
   	        orientation: 'right',
   	        num: 2,
   	        v: 1500,
   	        space: 3000,
   	        isRoll: true
   	    });
   	    $('#c').rollSlide({
   	        orientation: 'top',
   	        num: 1,
   	        v: 1500,
   	        //space: 500,
   	        isRoll: true
   	    });
   	    $('#d').rollSlide({
   	        orientation: 'bottom',
   	        num: 3,
   	        v: 1500,
   	        space: 1000,
   	        isRoll: true
   	    });
   	    $('#e').rollNoInterval().left();
   	    $('#f').rollNoInterval().right();
   	    $('#g').rollNoInterval().top();
   	    $('#h').rollNoInterval().bottom(); 
   	    

    } 
     );
</script>
</body>
</html>