<!DOCTYPE HTML>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$globals.getCompanyName()</title>
<link type="text/css" href="/style4/css/base.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"  />
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="/js/mySroce.js"></script>
<style type="text/css">
.hideBg{ display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;  height: 100%;  background-color:rgba(0, 0, 0, 0.23);  z-index:98;  -moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);}
</style>
</head>
<body class="html">
<input type="hidden" id="nowDate" value="$!globals.getDate()" />
<input type="hidden" id="workIds" value="$globals.getWorkId($globals.getLoginBean().id)" />
	<div id="hideBg" class="hideBg"></div>
 	<div id="closeDiv" style='width:135px;height:102px;position:absolute;left:650px;top:240px;display:none;z-index:10001;'><img src='/style/images/load.gif'/><div style='color:#0179bb;font-weight:bold;'>正在查询，请稍等......</div></div>  
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
						<!-- <a href="/MYSroceAction.do?operation=15&addTHhead=true">打分设置</a> -->
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
