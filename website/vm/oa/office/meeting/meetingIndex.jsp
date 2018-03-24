<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#else
<title>$text.get("oa.log.LogIndex")</title>
#end
<link type="text/css" rel="stylesheet" href="/style1/css/meeting.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"   />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/listPage.vjs"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript" src="/js/oa/titletip/jquery.poshytip.js"  charset="utf-8"></script>
<script type="text/javascript" src="/js/oa/oaMeetIndex.js" charset="utf-8"></script>
<style type="text/css">
.hideBg{ display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;  height: 100%;  background-color:rgba(0, 0, 0, 0.23);  z-index:98;  -moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);}
</style>
</head>
<body class="html" onload="showStatus();">
#if("$!addTHhead" == "true")
	#parse("./././body2head.jsp")
	<div>
#else
<div id="scroll-wrap">
#end
<div class="meeting-wrap">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" name="form" action="/OASearchMeeting.do?addTHhead=$!addTHhead">
 <input type="hidden" name="operation" value="4"/>
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value=""/> 
 <input type="hidden" id="addTHhead" name="addTHhead"  value="$!addTHhead" />
<div class="heading">
	<div class="heading-title">
		<span class="title-span">
			<b class="icons week-icon"></b>
			<i>会议列表</i>
		</span>
		<ul class="btn-ul">
			<li>
				<span class="btn-s" onClick="location.href='/Meeting.do?operation=$globals.getOP('OP_ADD_PREPARE')&turnOut=true&addTHhead=$!addTHhead'">发布会议</span>
			</li>
			<li>
				<span class="btn-s" onClick="location.href='/Meeting.do?operation=4&requestType=BOARDROOMWEEK&myMeetingWeek=myWeek&addTHhead=$!addTHhead'">返回 </span>
			</li>
		</ul>
	</div>
</div>
<div id="listRange_id">
	<div class="search-wp" style="text-align: left;padding-left: 35px;">
		<ul class="select_ul">
			<li>
				<select name="boardroomId" >
				    <option value="">会议室</option>			   
				    #foreach($room in $boardroomMap.values())
				    <option value="$room.boardroomId"  #if("$!meetingSearchForm.boardroomId"=="$room.boardroomId") selected="selected" #end >$room.boardroomName</option>
				    #end
				 </select>
			</li>
			<li>
				<select name="userType" >
				    <option value="">与我相关</option>
				    <option value="2"  #if("$!meetingSearchForm.userType"=="2") selected="selected" #end>我参与</option>
				    <option value="3" #if("$!meetingSearchForm.userType"=="3") selected="selected" #end>我主持</option>
				    <option value="4" #if("$!meetingSearchForm.userType"=="4") selected="selected" #end>我发起</option>
				    <option value="5" #if("$!meetingSearchForm.userType"=="5") selected="selected" #end>我不出席</option>
		    	</select>
			</li>
			<li>
				<select name="selectStatus" >
		    		<option value="0">状态</option>
		    		<option value="1" #if("$!meetingSearchForm.selectStatus"=="1") selected="selected" #end>未开始</option>
				   	<option value="2" #if("$!meetingSearchForm.selectStatus"=="2") selected="selected" #end>进行中</option>
				   	<option value="3" #if("$!meetingSearchForm.selectStatus"=="3") selected="selected" #end>已结束</option>
				   	<option value="4" #if("$!meetingSearchForm.selectStatus"=="4") selected="selected" #end>已取消</option>
		    	</select>
			</li>
			<li>
				<input name="meetingStartTime" type="text" class="Wdate" value="$!meetingSearchForm.meetingStartTime" onClick="openInputDate(this);"/>
					-
				<input name="meetingEndTime" type="text" class="Wdate" value="$!meetingSearchForm.meetingEndTime" onClick="openInputDate(this);"/>
			</li>		
			<li>
				<input type="hidden" name="pageParam" value="" />
				<button name="Submits" type="submit"  class="btn-s">$text.get("common.lb.query")</button>
				<!-- <button name="Submits" type="button" onClick="kong()" class="b2">清空</button>	 -->	
			</li>
		</ul>
	 </div>
	 <div class="no-meet-list" style="display:none;">
	 	暂无会议
	 </div>	
	 <div class="h-meet-list" >
		<ul class="meet-c-list">
			#foreach($row in $request.getAttribute('list'))
			<li>
				
				 <div class="meet-content">
				 	<a class="am-title" href="/Meeting.do?operation=4&requestType=NOTE&shijian=$!yyyymmdd.format($row.startTime)&meetingId=$!row.id&boardroomName=$!row.boardroomName&addTHhead=$!addTHhead"  title="$!row.title">$!row.title</a>
					<div class="content-bottom">	 	
					 	<span>
					 		<i>会议室 :</i>					 							 				    				
		    				#if("$!boardroomMap.get($!row.boardroomId).boardroomName"=="")
		    				<em class="meet-room-name" title="已删除">
		   						 已删除
							</em>
		    				#else
		    				<em class="meet-room-name" title="$!boardroomMap.get($!row.boardroomId).boardroomName">
		    					$!boardroomMap.get($!row.boardroomId).boardroomName   
		    				</em>	
		    				#end
		    				
					 	</span>
					 	<span>
					 		<i class="person-name">$!globals.getEmpFullNameByUserId($!row.sponsor)</i>发起
					 	</span>
					 	<span>
					 		<i class="person-name" title="$!row.toastmasterName">$!row.toastmasterName</i>主持
					 	</span>
					 	<span>
					 		$formt.format($!row.startTime) - $formt.format($!row.endTime)
					 	</span>
				 	</div>	
				 	
				 </div>
				 #if("$!row.status"!="")  
				 <span class="meet-statu" title="$!row.status" style="cursor:pointer;color:#4760C4" id="rowstatus_$!row.id">已取消</span>		
			 	 #else
			 	  <span class="meet-statu" id="rowstatus_$!row.id">#if($row.startTime.after($today)) 未开始  #elseif($row.endTime.before($today)) 已结束  #else 进行中  #end</span>
			 	 #end	
			 	 
			 	 
				 <div class="meet-operate">
				 	<i>操作</i>
				 	<ul class="operate-menu">
				 		#if(!$row.endTime.before($today)&&("$!globals.getLoginBean().getId()"=="1" || "$!row.sponsor" == "$!globals.getLoginBean().getId()"))
				 		<li onclick="updateMeetings('$!row.id');">修改</li>
				 		#end
				 		#if("$!globals.getLoginBean().getId()"=="1" || "$!row.sponsor" == "$!globals.getLoginBean().getId()")
				 		<li onclick="deletemeeting('/Meeting.do?operation=3&meetingId=$!row.id&addTHhead=$!addTHhead',this)">删除会议</li>
				 		#end
				 		#if("$!row.status"=="" && $row.endTime.after($today) &&("$!globals.getLoginBean().getId()"=="1" || "$!row.sponsor" == "$!globals.getLoginBean().getId()")) 
				 		<li onclick=" whyBox('$!row.id',this)">取消会议</li>
				 		#end
				 		
				 		#if("$!row.status"=="")   				
	   					#set($bistr=";$!row.Signin")
	   					#set($selfstr=";$self=")
					    #set($str="$self=absent")
					    #if("$!row.Signin"!=""&&"$bistr.indexOf($selfstr)"!="-1")
					    	#if($row.endTime.after($today))
					    		#set( $shu=$row.startTime.getTime()-$today.getTime()<=(1000*60*$row.signinTime))
	   							#if($shu)
					    #if("$!row.Signin.indexOf($str)"!="-1")
					    <li title='取消申请' onclick="resetsignin('$!row.id',this)">已申请不出席</li>
				 		#else
				 		<li title='取消签到' onclick="resetsignin('$!row.id',this)">已签到</li>
				 		#end
				 		#end
	    					#end	
	    					#else
	    					#set($renyuan=";$!row.sponsor;$!row.toastmaster$!row.participant")
	     					#set($selfren=";$self;")
	   					#if($row.endTime.after($today)&&"$renyuan.indexOf($selfren)"!="-1")
	     					#set( $shu=$row.startTime.getTime()-$today.getTime()<=(1000*60*$row.signinTime))
	   					#if($shu)
	   					<li onclick="signin('$!row.id',this)">签到</li>
				 		<li onclick="absent('$!row.id',this)">不出席</li>
	   					#end
	    					#end
	   					#end
	   				</span>
	 				 	#end			 				 		
				 	</ul>
				 	<b class="triangle"></b>
				
				 </div>
			</li>
			#end
		</ul>
		<div class="bottom-page">
			<div class="listRange_pagebar"> $!pageBar </div>
		</div>
	</div>
</div>
</form>
<div id="hideBg" class="hideBg"></div>
<div id="closeDiv" style='width:135px;height:102px;position:absolute;left:650px;top:240px;display:none;z-index:10001;'><img src='/style/images/load.gif'/><div style='color:#0179bb;font-weight:bold;'>正在查询，请稍等......</div></div> 
</body>
</html>
