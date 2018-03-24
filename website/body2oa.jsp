<!DOCTYPE HTML>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$globals.getCompanyName()</title>
<link type="text/css" rel="stylesheet" href="/js/oa/textboxlist/TextboxList.css" type="text/css" media="screen" charset="utf-8" />
<link type="text/css" rel="stylesheet" href="/js/oa/textboxlist/TextboxList.Autocomplete.css" type="text/css" media="screen" charset="utf-8" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"  />
<link type="text/css" href="/style4/css/base.css" rel="stylesheet" />
<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/body2TH.js"></script>
<script type="text/javascript" src="/js/mySroce.js"></script>
<script type="text/javascript" src="/js/crm/upload.vjs"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="js/ui/jquery-ui-1.8.18.custom.min.js"></script>

<script type="text/javascript" src="/js/includeAllTextBoxList.js"></script>
<style type="text/css">
.hideBg{ display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;  height: 100%;  background-color:rgba(0, 0, 0, 0.23);  z-index:1998;  -moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);}
</style>
</head>

<body class="html">
<input type="hidden" id="nowDate" value="$!globals.getDate()" />
<input type="hidden" id="workIds" value="$globals.getWorkId($globals.getLoginBean().id)" />
<input type="hidden" id="isOAorTrue" value="$globals.getSysSetting('isStartOA')" />

	<!-- 顶部 Start -->
	<div class="d-top">
		<div class="inside-top">
			<a class="lf logo-a">
				#if("$globals.getSysSetting('isStartOA')" == "true")
				<img src="/style/v7/images/v7-logo.png" src="logo" width="126px;" height="33px;" /> 
				#else
				<img src="/style4/images/h-logo.png" src="logo" />
				#end
			</a>
			<ul class="lf nav-top">
				<li><a href="/OA.do">首页</a></li>
				<li><a href="/OAWorkLogAction.do?src=menu&addTHhead=true">日志</a></li>				
				<li><a href="/OATaskAction.do?src=menu&addTHhead=true">任务</a></li>
				<li><a href="/OACalendarAction.do?src=menu&addTHhead=true">日程</a></li>
				<li><a href="/EMailAction.do?src=menu&addTHhead=true">邮件</a></li>
				<li><a href="/Meeting.do?operation=4&requestType=BOARDROOMWEEK&myMeetingWeek=myWeek&src=menu&addTHhead=true">会议</a></li>
				<li><a href="/OAMyWorkFlow.do?src=menu&addTHhead=true">工作流</a></li>
				<li class="pr more-pr">
					<a href="#">更多</a>
					<div class="more-menu">
						<a href="/OAItemsAction.do?src=menu&addTHhead=true">项目</a>
						<a href="/ToDoAction.do?src=menu&addTHhead=true">便签</a>
						#if("$globals.getSysSetting('isStartOA')" == "true")
						<a href="/OACollectionAction.do?src=menu&addTHhead=true">收藏</a>						
						<a href="/OABBSForumAction.do?src=menu&addTHhead=true">论坛</a>						
						<a href="/OAnewAdviceAction.do?src=menu&addTHhead=true">通知通告</a>
						<a href="/OAKnowledgeCenter.do?src=menu&addTHhead=true">知识中心</a>
						<a href="/OANewsAction.do?src=menu&addTHhead=true">新闻中心</a>
						<!--<a href="/MYSroceAction.do?operation=15&addTHhead=true">打分设置</a>--> 
						#end
					</div>
				</li>
			</ul>
			<div class="rf pr def-top">
				<a class="lf def-a"><img src="$globals.checkEmployeePhoto('48',$!LoginBean.Id)" alt="def" width="35" /></a>
				<i class="lf def-name">$!LoginBean.EmpFullName</i>
				<b class="triangle"></b>
				<ul class="def-list">
	    			<li><b>部门：</b><em>$!LoginBean.departmentName</em></li>
	    			<li><b>职务：</b><em>$globals.getEnumerationItemsDisplay("duty","$!LoginBean.titleId")</em></li>
	    			#if("$globals.getSysSetting('isStartOA')" == "true")
	    			<li><a href="/common/KK_Setup.exe" target="_blank" style="color:#2C5174;">下载KK聊天</a></li>
	    			<li><a href="/common/AIOPrint.exe" target="_blank" style="color:#2C5174;">下载控件</a></li>
	    			<li onclick="showDiv();">下载手机版</li>
	    			<li onclick="mdiwin('/UpgradeAction.do?type=5&src=menu','关于软件');">关于软件</li>
	    			<li><a href="javascript:void(0)" onclick="feedBack()" target="_blank" style="color:#2C5174;">反馈建议</a></li>
	    			#end
	    			<li onclick="sureLoginOut();" title="$text.get("login.lb.logout")">退出</li>
	    		</ul>	    		
			</div>	
			#if("$globals.getSysSetting('isStartOA')" != "true")	       
			<div class="icon-1 notice-top" onclick="opennews();">
				<b class="icon-1 notice-num"></b>
			</div>	
			#end		
		</div>
	</div>
	<!-- 顶部 End -->
	<div class="i-main">
		<!-- 左 Start -->
		<div class="pr i-m-l">
			<div class="t-def">
				<b class="k-t"></b>
				<img class="k-img" src="$globals.checkEmployeePhoto('140',$!LoginBean.Id)" />
			</div>
			<div class="lev lev-sel">
				<a href="/THSYS.do">
					<i class="icon-1 w-16 i-1"></i>
					首页
				</a>
			</div>
			<div class="lev">
				<a href="#" onclick="opennews();">
					<i class="icon-1 w-16 i-2"></i>
					消息
				</a>
			</div>
			<div class="lev">
				<a href="/OACollectionAction.do?addTHhead=true">
					<i class="icon-1 w-16 i-3"></i>
					关注
				</a>
			</div>

			<div class="in-h" style="min-height:0px;">
				<span>
					<i>应用</i>
				</span>
			</div>

			<div class="lev1" id="toworkflow">
				<a href="/OAMyWorkFlow.do?src=menu&addTHhead=true">
					待审核流程				
				</a>
				<b class="icon-1 i-4"></b>
			</div>

			<div class="lev1" id="totask">
				<a href="/OATaskAction.do?src=menu&addTHhead=true">
					待完成任务
				</a>
				<b class="icon-1 i-5"></b>
			</div>

			<div class="lev1" id="toreademail">
				<a href="/EMailAction.do?src=menu&addTHhead=true">
					待阅读邮件
				</a>
				<b class="icon-1 i-6"></b>
			</div>	
			#if("$globals.getSysSetting('isStartOA')" == "true")		
			<div class="in-h" style="min-height:0px;">
				<span>
					<i>收藏</i>
				</span>
			</div>
			<div class="d-sci">		
			 <!-- 菜单1 Start -->			   
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
			    	<a class="bk-a" href="$strUrl"   target="_blank">
						#if($globals.get($menu,2) == "/icon/default.png")
			    		<img class="face front" src="/style/images/newIcon/sale${index}.png"/>		    		
			    		#set($index=$index+1)
			    		#else
			    		<img class="face front" src="$globals.get($menu,2)"/>
			    		#end
						<i>#if($globals.get($menu,0).length()>5)$globals.get($menu,0).substring(0,5)#else $globals.get($menu,0)#end</i>
					</a>		    		    	
			    	#end
			    	#end
			    #end	
			    </div>
				<b class="icon-1 bot-b"></b>
			</div>	
			#else		  
	<!-- 菜单1 End -->	
	
			<div class="in-h" style="min-height:0px;">
				<span>
					<i>系统导航</i>
				</span>
			</div>
			<div class="d-sci">
				<a class="bk-a" href="#" target="_blank">
					<b class="icon-1 b-1"></b>
					<i>人力资源</i>
				</a>
				<a class="bk-a" href="#" target="_blank">
					<b class="icon-1 b-2"></b>
					<i>客户关系</i>
				</a>
				<a class="bk-a" href="#" target="_blank">
					<b class="icon-1 b-3"></b>
					<i>媒资管理</i>
				</a>
				<a class="bk-a" href="#" target="_blank">
					<b class="icon-1 b-4"></b>
					<i>数据分析</i>
				</a>
				<a class="bk-a" href="#" target="_blank">
					<b class="icon-1 b-5"></b>
					<i>NVOD播出</i>
				</a>
				<a class="bk-a" href="#" target="_blank">
					<b class="icon-1 b-6"></b>
					<i>OTT业务</i>
				</a>
				<a class="bk-a" href="#" target="_blank">
					<b class="icon-1 b-7"></b>
					<i>运营管理</i>
				</a>
			</div>
			<b class="icon-1 bot-b"></b>
		</div>
		#end
		 	
		<!-- 左 End -->
		<!-- 中 Start -->
		<div class="i-m-m">
			<ul class="mu-nav">
				<li title="添加日志" sc="oa-worklog" class="sel"><b class="icon-1 ic i-7"></b>日志<b class="icon-1 t"></b></li>
				<li title="添加任务" sc="oa-task"><b class="icon-1 ic i-8"></b>任务</li>
				<li title="添加会议" sc="oa-meeting"><b class="icon-1 ic i-9"></b>会议</li>
				<li title="添加工作流" sc="oa-workflow"><b class="icon-1 ic i-10"></b>工作流</li>
			</ul>
			<div class="d-tw">
				<span class="s-p" sc="oa-worklog">添加日志</span>							
				<!-- 工作流 End -->
			</div>
			<!-- 列表展示 Start -->
			<div class="d-lt" >
				<ul class="u-lt" id="minatuo" >
				</ul>
			</div>
			<!-- 日志 Start -->
			<div class="d-sc lt-wl lt-oaworklog"></div>
			<div class="d-sc lt-tk lt-oaTask"></div>
			<div class="d-sc lt-mt lt-oameeting"></div>
			<div class="d-sc lt-wf lt-oaworkflow" ></div>
			<div class="d-sc lt-wf lt-oaItems" ></div>											
			<!-- 列表展示 End -->
		</div>
		<!-- 中 End -->
		<!-- 右 Start -->
		<div class="pr i-m-r" id="leftatuo">
			<div class="join-b"><span >进入网站后台</span></div>									
		</div>
		
		<!-- 右 End -->
		<p class="clear"></p>
	</div>

	<div class="footer">	
	</div>
	<div id="hideBg" class="hideBg"></div>
 	<div id="closeDiv" style='width:135px;height:102px;position:absolute;left:650px;top:240px;display:none;z-index:10001;'><img src='/style/images/load.gif'/><div style='color:#0179bb;font-weight:bold;'>正在查询，请稍等......</div></div>  
	#if("$globals.getSysSetting('isStartOA')" == "true")
	<!-- 即时消息 Start-->  
    <div id="showKs"></div>
    <div id="showKK" style="display: none;">
    	
    </div>
    <div onclick="showKKLog();" class="showKKlog" id="showKKDiv">显示对话</div>
    <!-- 即时消息 End -->

	<div class="head_nav_menu">
	    <ul>
		    <li class="advice-li">
		    	<div class="d-advice">
			    	<a class="n1" onclick="opennews();" title="单击查看全部通知消息"></a>
			    	<div id="adviceId"></div>			    	
				</div>
		    </li>
		      <li class="meddage-li" onclick="openkk();">
		    	<div class="d-message">
			    	<a class="n2" title="消息"></a>
			    	<div id="messageId"></div>
			    	<div id="msgList" class="message" style="display: none;">
						<div style="width:1px;height:36px;background:#fff;position:absolute;right:0;bottom:1px;"></div>
						<div id="userIdDiv" class="msgpoup_middle">
							
						</div>
					</div>
				</div>
		    </li>
	    </ul>
	 
    </div>
    
    <!-- 手机二维码-弹出层 Start -->
	<div id="picDiv"  class="search_popup"  style="display:none;top: 150px;">
	
	<div id="Divtitle" class="move_dv" style="cursor:move;">
		
	</div>
  	<p class="img_p">
  		<img src="/CommonServlet?operation=download" alt="手机二维码" width="150" />
	</p>
	<p style="text-align:center;margin:30px 0 0 0;">
		<a href="/common/mo.apk" target="_blank" class="btn btn-success" style="color:#fff;">$text.get("common.lb.download.to.computer")</a>
		<a class="btn" onclick="closeSearch();">关闭</a>
	</p>
	<iframe style='width:362px;height:312px;position:absolute;left:-6px;top:-6px;z-index:-1;display:block;border-style:none;filter:Alpha(opacity=0);'></iframe>
  </div>
  <!-- 手机二维码-弹出层 End -->
	#end
</body>
</html>
