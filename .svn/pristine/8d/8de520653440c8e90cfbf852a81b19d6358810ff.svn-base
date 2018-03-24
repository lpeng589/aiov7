<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link type="text/css" rel="stylesheet" href="/style/css/about.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<style type="text/css">
.wrap{text-align:center;}
#bodyul{display:inline-block;}
.btn-danger{width:150px};
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
$(function(){
	$("#scroll-wrap").height(document.body.clientHeight-18);
});
</script>
</head>
<body class="html">
<div id="scroll-wrap">
		    
<div class="wrap">
    <div id=pageTitle>欢迎使用AIO一体化软件</div>
	<div id="bodyUl">
		#if("$!globals.getSystemState().dogState" == "6")  
		<ul>
			<li>	
			您有以下方案可供选择： 
			</li>
			<li>	
			   <font style="width:350px;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">1、购买并插入USB加密狗 </font>
			   <button onClick=" window.location.href='/';" class="btn btn-danger btn-small">重新登陆</button>
			</li>
			<li>	
			   <font style="width:350px;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">2、购买并进行软加密注册 </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&step=two&encryptionType=3&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">软加密注册</button>
			</li>
			<li>	
			   <font style="width:350px;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">3、使用基础版 </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=1&step=two&encryptionType=0&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">使用基础版</button>
			</li>
     
		</ul>
		#elseif("$!globals.getSystemState().dogState" == "1" || "$!globals.getSystemState().dogState" == "7" || "$!globals.getSystemState().dogState" == "8")  
		<ul>
			<li>					
			当前注册方式为：基础版 
			</li>
			#if("$!globals.getSystemState().dogState" == "7")
			<li>					
			<span >$text.get("common.msg.evaluateTimeError") </span>
			</li>
			#elseif("$!globals.getSystemState().dogState" == "8")
			<li>					
			<span >免费结束，请使用正版软件 </span>
			</li>
			#end	
			<li>	
			您有以下方案可供选择：  
			</li>
			<li>	
			   <font style="width:400px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">1、购买并插入USB加密狗,并重启服务器 </font>
			   
			</li>
			<li>	
			   <font style="width:350px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">2、购买并进行软加密注册 </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&step=two&encryptionType=3&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">软加密注册</button>
			</li>
     
		</ul>
		#elseif($globals.getSystemState().dogState == 12)
		<ul>##ok
			<li>					
			当前注册方式为：#if("$!globals.getSystemState().encryptionType" == "3")  软加密(序列号 $!globals.getSystemState().dogId)
			#else 加密狗($text.get("upgrade.lb.id") $!globals.getSystemState().dogId) #end 
			</li>	
			<li >
			<span >		
			#if("$!globals.getSystemState().encryptionType" == "3")  软件版本与序列号对应版本不一致 
			#else 软件版本与加密狗对应版本不一致 #end			
			</span>
			</li>	
			<li>	
			您有以下方案可供选择： 
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">1、重新安装正确版本的软件  </font>
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">2、更换正确版本的序列号 </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&step=two&encryptionType=3&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">更换序列号</button>
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">3、插入正确版本的加密狗 </font>
			</li>
			<li>
				#if("$!globals.getSystemState().encryptionType" == "3")  
				<font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">4、申请更换序列号版本 </font>
			    <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&from=offline&encryptionType=$!globals.getSystemState().encryptionType&utype=updateFun&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">版本更换</button>
				#else 
				<font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">4、申请更换加密狗版本 </font>
			    <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&from=offline&encryptionType=$!globals.getSystemState().encryptionType&utype=updateFun&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">版本更换</button>
				#end
			</li>
		</ul>
		#elseif($globals.getSystemState().dogState == 13)
		<ul>
			<li>					
			当前注册方式为：#if("$!globals.getSystemState().encryptionType" == "3")  软加密(序列号 $!globals.getSystemState().dogId)
			#else 加密狗($text.get("upgrade.lb.id") $!globals.getSystemState().dogId) #end 
			</li>	
			<li >
			<span >					
			您的授权期限已过 
			</span>
			</li>	
			<li>	
			您有以下方案可供选择：  
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">1、授权时间升级  </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&from=offline&encryptionType=$!globals.getSystemState().encryptionType&utype=updateFun&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">授权时间升级</button>
			</li> 
		</ul>
		#elseif($globals.getSystemState().dogState == 11 && "$!globals.getSystemState().encryptionType" == "3")
		<ul>##ok
			<li>					
			当前注册方式为：软加密 (序列号 $!globals.getSystemState().dogId) 
			</li>	
			<li >
			<span >					
			软件特征码不符 ，您可能有以下情形之一：<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;1、更换服务器<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;2、更换服务器某一硬件<br/>
			&nbsp;&nbsp;&nbsp;&nbsp;3、重装操作系统   
			</span>
			</li>	
			<li>	
			您有以下方案可供选择：  
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">申请更新软件特征码 </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&from=offline&encryptionType=3&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">申请更新</button>
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">更换新的序列号 </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&step=two&encryptionType=3&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">更换序列号</button>
			</li>
		</ul>
		
		#elseif("$!globals.getSystemState().dogState" == "4" || "$!globals.getSystemState().noRegUseDate" != "")
		<ul> ##ok
			<li>					
			当前注册方式为：加密狗 ($text.get("upgrade.lb.id") $!globals.getSystemState().dogId)
			</li>	
			<li >
			<span >					
			您还未进行注册,请立即注册避免影响使用 
			</span>
			</li>		
			<li>	
			您有以下方案可供选择：  
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">1、系统注册  </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&step=two&encryptionType=$!globals.getSystemState().encryptionType&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">系统注册</button>
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">2、更换为软加密注册 </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&step=two&encryptionType=3&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">软加密注册</button>
			</li> 
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">3、刷新证书  </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&from=offline&encryptionType=$!globals.getSystemState().encryptionType&utype=updateFun&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">刷新证书</button>
			</li>    
		</ul>
		#elseif("$!globals.getSystemState().dogState" == "14")
		<ul> ##ok
			<li >
			<span >					
			未授权使用系统属性功能，但本系统启用且已使用了系统属性 ($!globals.getSystemState().dogId)
			</span>
			</li>		
			<li>	
			您有以下方案可供选择： 
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">1、购买属性功能  </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&from=offline&encryptionType=$!globals.getSystemState().encryptionType&utype=updateFun&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">升级授权</button>
			</li> 
		</ul>
		#elseif("$!globals.getSystemState().dogState" == "32")
		<ul> ##ok
			<li >
			<span >					
			未检测到加密狗，或软加密证书丢失 
			</span>
			</li>		
			<li>	
			您有以下方案可供选择：  
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">1、插入加密狗 </font>
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">2、软加密注册 </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&step=two&encryptionType=3&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">软加密注册</button>
			</li>  
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">3、继续试用 </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&step=continueEval&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">继续试用</button>
			</li>   
		</ul>
		#elseif("$!globals.getSystemState().dogState" == "15")
		<ul>
			<li >
			<span >					
			未授权使用外币功能，但本系统启用外币且已经开账  ($!globals.getSystemState().dogId)
			</span>
			</li>		
			<li>	
			您有以下方案可供选择：  
			</li>			
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">1、购买外币功能  </font>
			   #if("$!globals.getSystemState().encryptionType" == "1")
			   <button onClick=" window.location.href='/UpgradeAction.do?type=4&exig=true&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">升级授权</button>
			   #else
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&from=offline&encryptionType=$!globals.getSystemState().encryptionType&utype=updateFun&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">升级授权</button>
			   #end
			</li> 
		</ul>
		#elseif("$!globals.getSystemState().dogState" == "30")
		<ul>
			<li >
			<span >					
			未授权使用标准财务功能，但本系统启用标准财务且已经开账  ($!globals.getSystemState().dogId)
			</span>
			</li>		
			<li>	
			您有以下方案可供选择：  
			</li>			
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">1、购买标准财务功能  </font>
			   #if("$!globals.getSystemState().encryptionType" == "1")
			   <button onClick=" window.location.href='/UpgradeAction.do?type=4&exig=true&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">升级授权</button>
			   #else
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&from=offline&encryptionType=$!globals.getSystemState().encryptionType&utype=updateFun&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">升级授权</button>
			   #end
			</li> 
		</ul>
		#elseif("$!globals.getSystemState().dogState" == "31")
		<ul> 
			<li >
			<span >					
			证书失效,可能原因您已申请更新软加密证书  
			</span>
			</li>		
			<li>	
			您有以下方案可供选择：  
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">1、更换软加密注册 </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&step=two&encryptionType=3&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">软加密注册</button>
			</li>  
		</ul>		
		#elseif("$!globals.getSystemState().encryptionType" == "3")
		<ul>  ##ok
			<li>					
			当前注册方式为：软加密 (序列号 $!globals.getSystemState().dogId) 
			</li>	
				
			<li>	
			您有以下方案可供选择：  
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">购买用户、产品  </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&from=offline&encryptionType=3&utype=updateFun&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">升级授权</button>
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">更换序列号 </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&step=two&encryptionType=3&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">更换序列号</button>
			</li>
     
		</ul>
		#elseif("$!globals.getSystemState().encryptionType" == "1")
		<ul> ##ok
			<li>					
			当前注册方式为：加密狗一代 ($text.get("upgrade.lb.id") $!globals.getSystemState().dogId)
			</li>
			<li>	
			您有以下方案可供选择：  
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">1、升级用户数  </font>
			   <button onClick=" window.location.href='/UpgradeAction.do?type=2&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">升级用户数</button>
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">2、升级模块  </font>
			   <button onClick=" window.location.href='/UpgradeAction.do?type=1&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">升级模块</button>
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">3、升级功能  </font>
			   <button onClick=" window.location.href='/UpgradeAction.do?type=4&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">升级功能</button>
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">4、升级语言数  </font>
			   <button onClick=" window.location.href='/UpgradeAction.do?type=3&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">升级语言数</button>
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">5、更换为软加密注册 </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&step=two&encryptionType=3&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">软加密注册</button>
			</li>     
		</ul>
		#else 
		<ul> ##ok
			<li>					
			当前注册方式为：加密狗 ($text.get("upgrade.lb.id") $!globals.getSystemState().dogId)
			</li>				
			<li>	
			您有以下方案可供选择：  
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">1、购买用户、产品  </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&from=offline&encryptionType=2&utype=updateFun&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">升级授权</button>
			</li>
			<li>	
			   <font style="width:200px;display:inline-block;padding-left:20px;background: url(/style/images/ico.gif) no-repeat left center;">2、更换为软加密注册 </font>
			   <button onClick=" window.location.href='/RegisterAction.do?regFlag=2&step=two&encryptionType=3&winCurIndex=$!winCurIndex';" class="btn btn-danger btn-small">软加密注册</button>
			</li>
     
		</ul>
		#end			
			
	</div>
		    </div>
          
           
	    </div>
</body>
</html>