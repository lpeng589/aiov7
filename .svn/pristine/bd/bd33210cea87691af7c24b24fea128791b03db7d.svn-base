<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" class="html">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#end
<link type="text/css" rel="stylesheet" href="/style1/css/meeting.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<link type="text/css" rel="stylesheet" href="/style/css/qqFace.css" />
<link type="text/css" rel="stylesheet" href="/style/css/oameeting/tip-yellow/tip-yellow.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript" src="/js/jquery.qqFace.min.js"></script>
<script type="text/javascript" src="$globals.js("/js/workPlan.vjs","",$text)"></script>
<script type="text/javascript"  src="/js/oa/titletip/jquery.poshytip.js" charset="utf-8"></script>
<script type="text/javascript"  src="/js/oa/oaMeetNote.js" charset="utf-8"></script>
<style type="text/css">
.meeting-wrap{padding:0 20px 20px 20px;}
#debatetable,#debatetable td{border:none;padding:0px; }
.debateli table,.debateli  td{border:none;padding:0px;}
.editordiv{ width:867px;height:280px; overFlow-x:scroll; overFlow-y:scroll; }
</style>
<script language="javascript" >

$(function(){
	$("a[title]").poshytip();
   	$(".tags-li").click(function(){
	   $(this).addClass("sel-li").siblings("li").removeClass("sel-li");
	   $("."+$(this).attr("show")).show().siblings("div").hide();
   	});
    /*$('#face1').qqFace({
		id : 'facebox1', //表情盒子的I
		assign: 'content', //给那个控件赋值
		path: '/style1/images/face/'	//表情存放的路径
	});	
	$('#face2').qqFace({
		id : 'facebox1', //表情盒子的I
		assign: 'content', //给那个控件赋值
		path: '/style1/images/face/'	//表情存放的路径
	});*/
  	//alldebate();
});

function alldebate(){
 	window.frames["noteFrame"].location.reload();
}

</script>
</head>
<body>
#if("$!addTHhead" == "true")
#parse("./././body2head.jsp")
#end
 <div id="scroll-wrap">
 <div class="meeting-wrap">
 <form action="/Meeting.do?operation=4&requestType=ADDNOTE" method="post">
 #if("$!addTHhead" != "true")
	<div class="heading">
		<div class="d-btn">
			<span class="lf meeting-name">
				$!request.getAttribute('meeting').title
			</span>
			<ul class="btn-ul">
				#if("$!advicEnter" != "true" && !$!meeting.endTime.before($today))
			     <li>
			     	<button type="submit" name="Submit" class="btn-s">保存</button>
			     </li>
			     #end
			     #if(!$!meeting.endTime.before($today))
			     <li>
			     	<button type="button"  name="backList" url="/Meeting.do?operation=7&meetingId=$meeting.id&backUrl=/Meeting.do?operation=4@requestType=NOTE@meetingId=$meeting.id" onClick="returnList(this);"  class="btn-s">修改</button>
			     </li>
			     #end
			     <li>
			     	<button type="button"  name="backList" title="Ctrl+Z" #if("$!backUrl"!="")url='$backUrl.replaceAll('@','&')' #else url='/OASearchMeeting.do?operation=4'#end onClick="returnList(this);" class="btn-s">返回</button>
			     </li>
			</ul>
		</div>
	</div>
#end	


<ul class="meeting-info">
	<li>
		<i>会议时间：</i>
		<em class="em-short">
			$formt.format($!request.getAttribute('meeting').startTime) ——$formt.format($!request.getAttribute('meeting').endTime)
		</em>
		<i>会议室：</i>
		<em class="em-short">
			#if("$!meeting.boardroomId"=="outer") $!meeting.outerAddress #else
			    #if("$!boardroomMap.get($meeting.boardroomId)"=="")
					 此内部会议室已被管理员删除



				#else
			   		$boardroomMap.get($meeting.boardroomId).boardroomName  
			   	#end 
			#end
		</em>
	</li>
	<li>
		<i>发起人：</i>
		<em>
			$!globals.getEmpFullNameByUserId($!request.getAttribute('meeting').sponsor)
		</em>
	</li>
	<li>
		<i>主持人：</i>
		<em>
			$!request.getAttribute('meeting').toastmasterName
		</em>
	</li>
	<li>
		<i>参与人：</i>
		<em>
			$!request.getAttribute('meeting').participantName
		</em>
	</li>
	<li>
		<i>内容：</i>		
		<em title="$!request.getAttribute('meeting').meetingContent">$!request.getAttribute('meeting').meetingContent</em>
		<div style="display:none">$!request.getAttribute('meeting').meetingContent</div>		
	</li>
	<li>
		<i>$text.get("upload.lb.affix")：</i>
		<span>
			#if($!meeting.filePath.indexOf(";") > 0)
				#foreach ($str in $globals.strSplit($!meeting.filePath,';'))
					<img src="/$globals.getStylePath()/images/down.gif"/>
					<a	href='/ReadFile?tempFile=path&path=/affix/OAMeeting/&fileName=$!globals.encode($!str)'target="_blank"> $str</a> 
			    #end
			#else
				暂无
			#end
		</span>
	</li>
</ul>




<input name="meetingId" id="meetingId" type="hidden" value="$!meetingId" />
<ul class="tags-ul">
	<li show="meet-cond" class="tags-li sel-li">会议情况</li>
	<li show="meet-note" class="tags-li">会议笔录</li>
	<li show="meet-talk" class="tags-li">讨论区</li>
</ul>
<div class="easyui-tabs" >

<!-- 会议情况   Start  -->
	<div title="会议情况" class="meet-cond">
		<ul class="cond-ul">
			<li>
				<i>参与总人数：</i>
				<em>$sum</em>
			</li>
			<li>
				<i>已签到：</i>
				<em>$signin</em>
			</li>
			<li>
				<i>未签到：</i>
				<em>$unsignin</em>
			</li>
			<li>
				<i>不出席：</i>
				<em>$absent</em>
			</li>
			#if("$!late"!="-1")
			<li>
				<i>迟到：</i>
				<em>$late</em>
			</li>
			#end
		</ul>
	
        <table height="309" border="0" cellspacing="0" cellpadding="0" style="width:100%;" >

      	<tr>
       		<td height="282" colspan="8">
      
       <ul class="person-cond-ul">
        #foreach( $userid in $userMap.keySet())
        #if("$userid.indexOf('detail')"=="-1"&&"$userid.indexOf('WHY')"=="-1")
          <li>
          		<img  src="$!globals.checkEmployeePhoto('48',$!globals.getEmployeeById($userid).getId())"/>      		 		
        	 <ul>
        		<li class="person-name">#if($userid.indexOf("001") == 0) $!globals.getDepartMentById("$userid") #else $!globals.getEmpFullNameByUserId($userid) #end</li>
        #if("$userid"!="$loginer"||!$meeting.endTime.after($today))		
        	#if("$!meeting.status"=="")	
        		#if("$userMap.get($userid)" == "")
        		<li>
        		 #if($meeting.endTime.after($today))
        		 #set( $shu=$meeting.startTime.getTime()-$today.getTime()<=(1000*60*$meeting.signinTime))
        		 #if($shu)
        		<input type="button" class="btn btn-mini" onclick="signin('/Meeting.do?operation=4&requestType=SIGNIN&meetingId=$!meeting.id&userId=$userid',this)" value="签到" />
        		#end
        		<input type="button" class="btn btn-mini"  onclick="advice('/Meeting.do?operation=4&requestType=ADVICE&meetingId=$!meeting.id&userId=$userid&wakeUpMode=$!meeting.wakeUpMode')" value="通知" />
        		<input type="button" class="btn btn-mini"  onclick="msgComm('$userid','#if($userid.indexOf("001") == 0)$!globals.getDepartMentById("$userid")#else$!globals.getEmpFullNameByUserId($userid)#end')" value="对话" />
        		#else
        		会议已结束



        		#end
        		</li>
        		#elseif("$userMap.get($userid)" == "absent")
        		#set($why="$userid"+"WHY")
        		<li><a 
        		#if("$loginer"=="$meeting.sponsor"||"$loginer"=="1")
        		title="$userMap.get($why).replace("|,|",";").replace("|-|","=")"
        		#end
        		>有事不能到席</a></li>
        		 #set($detail="$userid"+"detail")
        		<li>$userMap.get($detail)</li>
        		#elseif("$userMap.get($userid)" == "late")
        		<li>已签到,但迟到</li>
        		 #set($detail="$userid"+"detail")
        		 <li style="display:none;">$userMap.get($detail)</li>
        		#else
        		<li>已签到</li>
        		 #set($detail="$userid"+"detail")
        		 <li>$userMap.get($detail)</li>
        		#end
        	#else
               <li>会议已取消</li>	
        	#end
        #else
           
	 #if("$!meeting.status"=="")
   
    			<span class="span_self"> 
   				 #set($bistr=";$!meeting.signin")
  				 #set($selfstr=";$loginer=")
     			#set($str="$loginer=absent")
   				 #if("$!meeting.Signin"!=""&&"$bistr.indexOf($selfstr)"!="-1")
   					 #if($!meeting.endTime.after($today))
     					#set( $shu=$!meeting.startTime.getTime()-$today.getTime()<=(1000*60*$!meeting.signinTime))
    					#if($shu)
    							#if("$!meeting.Signin.indexOf($str)"!="-1")
     								<a href="javascript:void(0);" class="btn btn-mini" onclick="resetsignin('$!meeting.id',this)" title='取消申请'>已申请不赴会</a>
    							#else
    								 <a href="javascript:void(0);"onclick="resetsignin('$!meeting.id',this)" title='取消签到'>已签到</a>

   								 #end
    	
        				#end
                    
     				#end
   				 #else
    				 #set($renyuan=";$!meeting.sponsor;$!meeting.toastmaster$!meeting.participant")
      				#set($selfren=";$loginer;")
   					 #if($!meeting.endTime.after($today))
   					        #if("$renyuan.indexOf($selfren)"!="-1")
     				 			#set( $shu=$!meeting.startTime.getTime()-$today.getTime()<=(1000*60*$!meeting.signinTime))
    							#if($shu)
    								<a href="javascript:void(0);" class="btn btn-mini" onclick="signinself('$!meeting.id',this)" >签到</a>
     								<a href="javascript:void(0);" class="btn btn-mini" onclick="absent('$!meeting.id',this)" >不能赴会</a>
     							#end
     			            #end
     			      
     				    
    			 	#end
   				 #end
    </span>
     	#else
   会议已取消






   #end
  

        #end		
          	 </ul>
          	 
          </li>
             #end
          #end
       </ul>
       </td>
     </tr>
</table>
</div>
<!-- 会议情况 End -->
<!-- 会议笔录 Start --> 
<div show="会议笔录" class="meet-note">
<div class="d-block">
#if("$!meeting.sponsor"=="$loginer"||"$!globals.getLoginBean().getId()"=="1")
	
		<span>请指定笔录员：</span>
	   <select name="taker" id="createBy">
	    #foreach( $userid in $userMap.keySet())
	    <option value="$userid" #if("$!meeting.taker"=="$userid")selected="selected"#end>$!globals.getEmpFullNameByUserId($userid)</option>
	    #end
	   </select>
		<input type="button" class="btn-s btn-green" value="确定" onclick="takerchange();" />
	
#end
#if("$!meeting.taker"!="")
	
		<span id="takerName">$!globals.getEmpFullNameByUserId($!meeting.taker)</span>
		<span>记录本次会议</span>
	
#end
</div>
    #if("$!meeting.taker"=="$loginer")
     <input type="hidden"  name="noter" value="true" />
      <input type="hidden"  name="meetingTime" value="$!yyyymmdd.format($meeting.startTime)" />
      <textarea class="show-meet-note" name="meetingNote" cols="110" rows="14">$!meeting.meetingNote</textarea>
      #else
      <div class="show-meet-note">$!meeting.meetingNote</div>
      #end
          </div>
<!-- 会议笔录  End -->   
<!-- 讨论区 Start -->    


<div title="讨论区" class="meet-talk">	
	<input type="button" value="刷新" class="btn btn-mini" onclick="alldebate()"/> 
	<iframe scrolling="no" frameborder="0" scrolling="0" width="100%" height="330px;" name="noteFrame" id="noteFrame" src="/DiscussAction.do?tableName=OAMeetingDebateLog&f_ref=$!meeting.id&parentIframeName=noteFrame"></iframe> 
	
</div> 

<script type="text/javascript">
$(function(){
	$("#scroll-wrap").height(document.documentElement.clientHeight);
});
</script>
<!-- 讨论区 End -->
</form>
</div>
</div>
 </body>
 </html>