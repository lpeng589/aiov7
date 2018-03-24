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
<link type="text/css" rel="stylesheet" href="/js/oa/textboxlist/TextboxList.css" type="text/css" media="screen" charset="utf-8" />
<link type="text/css" rel="stylesheet" href="/js/oa/textboxlist/TextboxList.Autocomplete.css" type="text/css" media="screen" charset="utf-8" />
<link type="text/css" rel="stylesheet" href="/style/themes/default/easyui.css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/formvalidate.vjs","",$text)"></script>

<script type="text/javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="$globals.js("/js/workPlan.vjs","",$text)"></script>
<script type="text/javascript" src="/js/oa/oameetingForm.js"></script>

<script type="text/javascript" src="/js/includeAllTextBoxList.js"></script>
<!--  
<script type="text/javascript" src="/js/oa/textboxlist/GrowingInput.js"charset="utf-8"></script>
<script type="text/javascript" src="/js/oa/textboxlist/TextboxList.js" charset="utf-8"></script>		
<script type="text/javascript" src="/js/oa/textboxlist/TextboxList.Autocomplete.js" charset="utf-8"></script>
-->
<style type="text/css" >
.textinput{width:600px;height:25px;border:1px solid #d1d1d1;}
select{border:1px solid #d1d1d1;}
.select{background:#F00; width:100px; height:25px; display:inline-block;}
.selectli{ background:#CCC; color:#FFF; width:300px; height:25px; display:inline-block; text-align:center; font-size:25px; line-height:25px;}
.form_tags { margin-bottom: 10px; }
.textboxlist-loading { background: url('images/spinner.gif') no-repeat 380px center; }
.note { color: #666; font-size: 90%; }

#boardroom{width:602px;height:27px;border:1px solid #d1d1d1;}
#meetingContent{border:1px solid #d1d1d1;width:600px;}
#regularriqi{width:120px;height:25px;border:1px solid #d1d1d1;}
.textboxlist{width:602px; }
.textboxlist-bits{height:auto;border:1px solid #d1d1d1;}	
</style>
<script type="text/javascript">
jQuery(function(){
	shareByTBox1 = new jQuery.TextboxList('#toastmasterName', {unique: true, plugins: {autocomplete: {}}});
	shareByTBox1.getContainer().addClass('textboxlist-loading');
	shareByTBox1.plugins['autocomplete'].setValues($textBoxValues);
	shareByTBox1.getContainer().removeClass('textboxlist-loading');
	shareByTBox2 = new jQuery.TextboxList('#participantName', {unique: true, plugins: {autocomplete: {}}});
	shareByTBox2.getContainer().addClass('textboxlist-loading');
	shareByTBox2.plugins['autocomplete'].setValues($textBoxValues);
	shareByTBox2.getContainer().removeClass('textboxlist-loading');	 
	
	var hideToastmaster = $("#hideToastmaster").val();
	var hideToastmasterName = $("#hideToastmasterName").val();
	var hideParticipant = $("#hideParticipant").val();
	var hideParticipantName = $("#hideParticipantName").val();
	if(hideToastmasterName!=""){
		var arrStr1 = hideToastmaster.split(";");
		var arrStr2 = hideToastmasterName.split(";");		
		for(var i=0;i<arrStr1.length;i++){
			if(arrStr1[i]!=""){
				shareByTBox1.add(arrStr2[i],arrStr1[i]);
			}
		}
	}
	if(hideParticipantName!=""){
		var arrStr1 = hideParticipant.split(";");
		var arrStr2 = hideParticipantName.split(";");		
		for(var i=0;i<arrStr1.length;i++){
			if(arrStr1[i]!=""){
				shareByTBox2.add(arrStr2[i],arrStr1[i]);
			}
		}
	}
	
		
	var starttime=getDateParse("$!meeting.startTime");
  	var endtime=getDateParse("$!meeting.endTime");  		
    if(starttime!=1111){
      	var start=new Date(starttime);
      	var month=start.getMonth()+1;
      	var day=start.getDate();
      	if(month<10){
      		month="0"+month;
      	}
      	if(day<10){
      		day="0"+day;
      	}
     	form.regularriqi.value=start.getFullYear()+"-"+month+"-"+day;
        //riqiweek(form.regularriqi);
     	var hs=start.getHours();
     	if(hs<10){
        	hs="0"+hs;
     	}else{
        	hs=""+hs;
     	}
     	var mins=start.getMinutes();
     	if(mins<10){
        	mins="0"+mins;
     	}else{
       		mins=""+mins;
     	}
     	$("#starthouseregular option[value="+hs+"]").attr("selected","true");
     	$("#startminregular option[value="+mins+"]").attr("selected","true");
  		}else{	
  			start=new Date();
  			var day=start.getDate();
  			var month=start.getMonth()+1;
         	var day=start.getDate();
         	if(month<10){
         	month="0"+month;
         }
        if(day<10){
        	day="0"+day;
        }
 	     	form.regularriqi.value=start.getFullYear()+"-"+month+"-"+day;  
 		}
  		if(endtime!=1111){
  	     	var start=new Date(endtime);    
  	     	var hs=start.getHours();
  	    if(hs<10){
  	        hs="0"+hs;
  	    }else{
  	        hs=""+hs;
  	    }
	    var mins=start.getMinutes();
	    if(mins<10){
	        mins="0"+mins;
	    }else{
	       mins=""+mins;
	    }
	    $("#endhouseregular option[value="+hs+"]").attr("selected","true");
	    $("#endminregular option[value="+mins+"]").attr("selected","true");
  	} 	  	 
});
</script>
  </head>
  
  <body onload="showStatus();">
  	#if("$!addTHhead" == "true")
		#parse("./././body2head.jsp")
	#end
  <div id="scroll-wrap">
  
  <div class="meeting-wrap">
  
  <iframe name="formFrame" style="display:none"></iframe>
  #if("$!request.getAttribute('meeting').id" != "")
  <form name="form" action="/Meeting.do?operation=2&addTHhead=$!addTHhead" method="post"    target="formFrame">
   <input type="hidden" name="backUrl" value="$!backUrl" />
  #else
   <form name="form" action="/Meeting.do?operation=1&addTHhead=$!addTHhead" method="post"   target="formFrame">
   <input type="hidden" name="backUrl" value="$!backUrl" />
  #end

 <input type="hidden" id="updateStartTime" value="$!meeting.startTime" />
 <input type="hidden" id="updateEndTime" value="$!meeting.endTime" />
 <input type="hidden" id="updateBoardroom" value="$!meeting.boardroomId" />
 <input type="hidden" id="updateMeetingId" value="$!meeting.id" />
 <input type="hidden" id="hideParticipantName" value="$!request.getAttribute('meeting').participantName" />
 <input type="hidden" id="hideToastmasterName" value="$!request.getAttribute('meeting').toastmasterName" />
 <input type="hidden" id="hideParticipant" value="$!request.getAttribute('meeting').participant" />
 <input type="hidden" id="hideToastmaster" value="$!request.getAttribute('meeting').toastmaster" />
 <div class="heading">
	<div class="heading-title">
		<span class="title-span">
			<b class="icons week-icon"></b>
			<i>发布会议</i>
		</span>
		<ul class="btn-ul">
		#if("$!request.getAttribute('meeting').id" != "")
		    <li><button type="button" class="btn-s" name="Submit" onclick="checksubmit()">保存</button></li>
		   
		#else
		    <li><button type="button" name="Submit" class="btn-s"onclick="checksubmit()">$text.get("common.lb.save")</button></li>
		    <li><button type="reset" class="btn-s" onClick="reset();">$text.get("common.lb.reset")</button></li>
		#end	   	    
		     <li><button type="button"  name="backList" title="返回"
		     #if("$!turnOut" == "true")
		     url='/OASearchMeeting.do?operation=4&addTHhead=$!addTHhead'
		     #else 
		     url='/Meeting.do?operation=4&requestType=BOARDROOMWEEK&myMeetingWeek=myWeek&addTHhead=$!addTHhead'
		     #end 
		     onClick="returnList(this);" class="btn-s">返回</button></li>
		</ul>	
	</div>
	
</div>
  <div id="listRange_id" style="width:800px;margin:0 auto;">
<input type="hidden" name="attachFiles" value="$!meeting.filePath" />
<input type="hidden" name="delFiles" value="" />
<input type="hidden" name="meetingId" value="$!request.getAttribute('meeting').id" />
<input type="hidden" name="regularMeeting" value="0" />
<input type="hidden" name="regularDay" value="1" />
	<input name="meetingStartTime" type="hidden" value="$!request.getAttribute('meeting').startTime"/>
	 <input name="meetingEndTime" type="hidden" value="$!request.getAttribute('meeting').endTime"/>
<table>
#if("$!meeting.status"!="")
 <tr  >
    <td align="right">状态</td>
    <td> 
     <input name="isCancel"  value="1" type="radio" checked="checked" />取消
     <input name="isCancel"  value="0" type="radio" />重新启动
    </td>
  </tr>	
  #end
 <tr>
    <td align="right" style="color:red;">主题</td>
    <td width="700"><input name="title" type="text" value="$!request.getAttribute('meeting').title" class="textinput"/></td>
  </tr> 
   <tr>
    <td align="right" style="color:red;">主持人</td>
    <td>
    	<div class="lf pr txt-list share-d" >
            <input name="toastmaster"  type="hidden" id="toastmaster" value="" />
            <input name="toastmasterName" class="lf txtIconLong ip_txt" type="text" id="toastmasterName" />
        	<span class="icons share-s" onclick="deptPopForJob('userGroup','toastmasterName','toastmaster');"></span>       
        </div>  	
	</td>
    </tr>  											
	<tr>
		<td align="right">会议日期</td>
		<td><input id="regularriqi" name="regularriqi"  type="text" class="Wdate" value="$!globals.getDate()"  onchange="riqiweek(this)" onClick="openInputDate(this);"/>
	 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 		<select id="starthouseregular" name="starthouseregular" >
	 			<option value="00" selected="selected" >00</option>
	   			#foreach($i in [1..23])
	    		#if($i<10)
	    		<option value="0$i"  #if("$!meeting.id"==""&&$i==8)selected="selected" #end>0$i</option>
	    		#else
	    		<option value="$i">$i</option>
	    		#end		      
	    		#end
	   		</select>&nbsp;时	
	   		<select id="startminregular" name="startminregular" >
	    		<option value="00" selected="selected" >00</option>
	    		#foreach($i in [1..59])
	   			#if($i<10)
	    		<option value="0$i">0$i</option>
	    		#else
	    		<option value="$i">$i</option>
	    		#end
	    		#end
	   		</select>&nbsp;分	
	   		&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
	    	<select id="endhouseregular" name="endhouseregular" >
	 			<option value="00" selected="selected" >00</option>
	    		#foreach($i in [1..23])
	    		#if($i<10)
	    		<option value="0$i">0$i</option>
	    		#else
	    		<option value="$i"  #if("$!meeting.id"==""&&$i==10)selected="selected" #end>$i</option>
	    		#end		      
	    		#end
	   		</select>&nbsp;时





	
	   		<select id="endminregular" name="endminregular" >
	    		<option value="00" selected="selected" >00</option>
	    		#foreach($i in [1..59])
	   			#if($i<10)
	    		<option value="0$i">0$i</option>
	    		#else
	    		<option value="$i">$i</option>
	    		#end
	    		#end
	   		</select>&nbsp;分		   		   
	  	</td>                
	</tr>
  <tr>
    <td  align="right" style="color:red;">参与者 </td>
    <td> 
    	<div class="pr lf txt-list share-d" >
    		<input name="participant" type="hidden" id="participant" value=""/> 
	    	<input name="participantName" class="lf txtIconLong ip_txt" type="text" id="participantName" /> 
	    	<b onclick="deptPopForJob('userGroup','participantName','participant');" title="选择人员" class="pa b-ry icons"></b>
	    	<b onclick="deptPopForJob('deptGroup','participantName','participant');" title="选择部门" class="pa b-bm icons"></b>			    		
     	</div>
     	<!-- <span class="icons share-s" ></span> -->
	</td>
  </tr>
 
   <tr>
    <td align="right">会议室</td>
    <td>
    	<select id="boardroom" name="boardroomId"  onchange="boardroomchange()"><!-- onfocus="ajaxboardroom()" -->
   			<option value="0" id="1_9">请选择</option> 
     		#foreach($room in $boardroomMap.values())
    		<option  renshu="$room.peopleNumber" value="$room.boardroomId" zhan="0" #if("$room.boardroomId"=="$meeting.boardroomId")selected="selected" #end>$room.boardroomName</option>
    		#end   		
    	</select>
    	<input class="btn-s" type="button" value="会议室使用情况" onclick="meetRoomUsing();" /> 
    </td>
  </tr>
  <tr>
    	<td align="right" style="color:red;">会议内容</td>
    	<td><textarea name="meetingContent" id="meetingContent" cols="71" rows="8" >$!request.getAttribute('meeting').meetingContent</textarea></td>
  </tr>
  <tr>
	<td ></td>
	<td>
		<em class="pr update-file" onclick="openAttachUpload('/affix/OAMeeting/');window.save=true;">
			<b class="icons pa"></b>
			$text.get("upload.lb.affixupload")
		</em>
		<div id="status" style="visibility:hidden;color:Red"></div>
		<div id="files_preview">
		      #if($!meeting.filePath.indexOf(";") > 0)
		  #foreach ($str in $globals.strSplit($!meeting.filePath,';'))
		  	 <div  id ="$str" style ="height:18px; color:#ff0000;">
		  	 <a href="javascript:;" onclick="removeFile('$str');"><img src="/style/images/del.gif"></a>&nbsp;&nbsp;
		  	 $str<input type=hidden name="attachFile" value="$str"/></div>					 
		  #end	
	 	 #end		
		</div>				
	</td>
</tr>
</table>

#if("$!request.getAttribute('meeting').toastmaster" == "")
<table id="twoTbl" style="display: none">
#else
<table id="twoTbl">
#end
	<tr class="isregular" >
			<td>是否例会</td>
	 		<td>
	 			<input name="isregular" onclick="regulardisplay('1')" value="1" type="radio" #if($meeting.regularMeeting>0)checked="checked"#end  />是  
	 			<input name="isregular" onclick="regulardisplay('0')" value="0" type="radio"  #if("$!meeting.regularMeeting"==""||"$!meeting.regularMeeting"=="0") checked="checked" #end  />否 
	 		</td>                
		</tr>		
	  		
	  	<tr class="regular" #if("$!meeting.regularMeeting"==""||"$!meeting.regularMeeting"=="0") style="display:none;"#end>
	   		<td></td>
	    	<td>   			
	    		<input name="regulartype"  value="1" type="radio" checked="checked"   />每天   
	     		<input name="regulartype"  value="2" type="radio"  #if("$meeting.regularMeeting"=="2")checked="checked" #end    />每周        		
	    		<input name="regulartype"  value="3" type="radio"  #if("$meeting.regularMeeting"=="3")checked="checked" #end   />每月 
	    	    <span>例会结束日期:</span>
	    	    <input name="regularend" onClick="openInputDate(this);" value="$!meeting.regularend" style="width: 80px;"/>  			
	    	</td>                
	  	</tr>							
	  <tr>
	    <td>通知方式</td>
	    <td>#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
	    	<input type="checkbox" name="wakeUpMode"  value="$row_wakeUpType.value"				
			#if("$!meeting.wakeUpMode"!="")
			   #if("$meeting.wakeUpMode.indexOf($row_wakeUpType.value)"!="-1")
			   checked
			   #end
			 #else
			   #if("$row_wakeUpType.name"=="通知提示")
				checked
			#end
			#end
			 />		
		$row_wakeUpType.name #end</td>
	  </tr>
	  
	  
	  <tr>
	    <td>设置 </td>
	    <td>提前
	    	<select name="warnTime">   
	     	#foreach($i in [5,10,15,20,25,30,60,120])
	    		<option value="$i"  #if("$!meeting.warnTime"=="$i")
	                          selected="selected"
	                        #end
	    	>$i</option>
	    	#end
	    	</select>分钟提醒，                               
	 		<select name="signinTime">  
	     	#foreach($i in [5,10,15,20,25,30,60,120])
	    	<option value="$i"  #if("$!meeting.signinTime"=="$i")
	                          selected="selected"
	                        #end
	    	>$i</option>
	    	#end
	    	</select>分钟开始签到





	     </td>                    
	 </tr>
</table>
#if("$!request.getAttribute('meeting').toastmaster" == "")
<div id="tuBtton" class="btn-s" style="margin-left: 350px;display:inline-block;" bg="more" onclick="moreData(this);">更多</div>
#end
</form>
</div>
</div>
</div>

<script type="text/javascript">
$(function(){
	$("#scroll-wrap").height(document.documentElement.clientHeight);
});
</script>
  </body>
</html>
