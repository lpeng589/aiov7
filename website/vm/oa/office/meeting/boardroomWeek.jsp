<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" class="html">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#else
<title>会议室周程表</title>
#end
<link type="text/css" href="/style1/css/meeting.css" rel="stylesheet" />
<link type="text/css" href="/js/skins/ZCMS/asyncbox.css" rel="stylesheet" />
<link type="text/css" href="/style/themes/default/easyui.css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<style type="text/css">
.hideBg{ display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;  height: 100%;  background-color:rgba(0, 0, 0, 0.23);  z-index:98;  -moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);}
</style>
  </head>
  
  <body onload="showStatus();">
	#if("$!addTHhead" == "true")
	#parse("./././body2head.jsp")
	#end
  <div id="scroll-wrap">
  
  <div class="meeting-wrap">
  
  
 <div class="heading">
	<div class="heading-title">
		<span class="lf title-span">
			<b class="icons week-icon"></b>
			<i>#if("$!boardroomIndex" == "boardroomIndex")会议室周程表#else会议周程表#end</i>
		</span>
		<div class="lf next-prev-dv">
		    <form id="form" action="/Meeting.do" name="form" method="post"> 
		        #if("$!boardroomId"!="")
		        <span>会议室</span>
				<select name="boardroomId" style="width: 100px;" onchange="changeSelect();">  
				     #foreach($room in $boardroomMap.values())
				    <option value="$room.boardroomId" #if("$!boardroomId"=="$room.boardroomId") selected="selected" #end>$room.boardroomName</option>
				    #end
				    #end
			    </select>	
				<input name="operation" type="hidden" value="4" />
				<input name="conEnter" type="hidden" value="$!conEnter" />
				<input name="requestType" type="hidden" value="BOARDROOMWEEK" />
				<input name="myMeetingWeek" type="hidden" value="$!myWeek" />
				<input name="week" type="hidden" #if("$!weekstr" != "")value="$!weekstr"#else value="0" #end />
				<input name="boardroomIndex" type="hidden" value="$!boardroomIndex" />
				<input name="requestType" type="hidden" value="$!type" />
				<input type="button" class="btn-s" value="上一周" onclick="operations('subtract')" />  
				<input type="button" class="btn-s" value="本周 " onclick="operations('now')" />
				<input type="button" class="btn-s" value="下一周" onclick="operations('add')" />
				<input name="meetingStartTime" type="text" style="width:20px;cursor:pointer;" class="Wdate"  onClick="changeSize(this);"/><!-- openInputDate(this); -->
		 		<input type="hidden" id="addTHhead" name="addTHhead"  value="$!addTHhead" />
		 	</form>
		</div>
		<ul class="btn-ul">
			#if($$LoginBean.getOperationMap().get("/Meeting.do?operation=4&requestType=BOARDROOMWEEK&myMeetingWeek=myWeek").add())	
				#if("$!conEnter" != "true")
				<li><button type="button" class="btn-s" onClick="location.href='/Meeting.do?operation=$globals.getOP('OP_ADD_PREPARE')&addTHhead=$!addTHhead'">发布会议</button></li>
				#end
			#end
			#if($$LoginBean.getOperationMap().get("/Meeting.do?operation=4&requestType=BOARDROOMWEEK&myMeetingWeek=myWeek").query())
				#if("$!conEnter" != "true")
				<li><button type="button" class="btn-s" onClick="location.href='/OASearchMeeting.do?operation=4&addTHhead=$!addTHhead'">会议列表</button></li>
				#end
			<!-- 
			<li><button type="button" class="btn-s" onClick="location.href='/Meeting.do?operation=4&requestType=BOARDROOMWEEK&myMeetingWeek=myWeek'">我的周程表</button></li>
			 -->
				#if("$!conEnter" != "true")
				<li><button type="button" class="btn-s" onClick="location.href='/Meeting.do?operation=4&requestType=BOARDROOMWEEK&boardroomIndex=boardroomIndex&addTHhead=$!addTHhead'">会议室周程表</button></li>	   
				#end
			#end
			#if($$LoginBean.getOperationMap().get("/Meeting.do?operation=4&requestType=BOARDROOMWEEK&myMeetingWeek=myWeek").add())
				#if("$!conEnter" != "true")
				<li><button type="button" class="btn-s" onclick="mdiwin('/OABoardroom.do?src=menu&addTHhead=$!addTHhead','会议室管理');">会议室管理</button></li>	   
				#end
			#end
			#if("$!boardroomIndex" == "boardroomIndex")
				#if("$!conEnter" != "true")
				
				<li><button type="button" class="btn-s" onClick="location.href='/Meeting.do?operation=4&requestType=BOARDROOMWEEK&myMeetingWeek=myWeek&src=menu&addTHhead=$!addTHhead'">返回</button></li>
				#end
			#end
		</ul>
	</div>
</div>
 
  <div id="listRange_id">

<table width="980" class="meet-week-table" border="0" cellpadding="0" cellspacing="0">
	<thead>
	  <tr>
	    <td width="120" height="57"></td>
	    <td width="120" class="font-red" #if("$!pdf.format($days.get(0))" == "$globals.getDate()") id="today" #end>星期日 $!formt.format($days.get(0))</td>
	    <td width="120" #if("$!pdf.format($days.get(1))" == "$globals.getDate()") id="today" #end>星期一$!formt.format($days.get(1))</td>
	    <td width="120" #if("$!pdf.format($days.get(2))" == "$globals.getDate()") id="today" #end>星期二$!formt.format($days.get(2))</td>
	    <td width="120" #if("$!pdf.format($days.get(3))" == "$globals.getDate()") id="today" #end>星期三$!formt.format($days.get(3))</td>
	    <td width="120" #if("$!pdf.format($days.get(4))" == "$globals.getDate()") id="today" #end>星期四$!formt.format($days.get(4))</td>
	    <td width="120" #if("$!pdf.format($days.get(5))" == "$globals.getDate()") id="today" #end>星期五$!formt.format($days.get(5))</td>
	    <td width="120" class="font-red" #if("$!pdf.format($days.get(6))" == "$globals.getDate()") id="today" #end>星期六$!formt.format($days.get(6))</td>
	  </tr>
  </thead>
  <tbody>
	  <tr>
	    <td height="180" align="center">上午</td>
	    <td id="d0_00"></td>
	    <td id="d1_00"></td>
	    <td id="d2_00"></td>
	    <td id="d3_00"></td>
	    <td id="d4_00"></td>
	    <td id="d5_00"></td>
	    <td id="d6_00"></td>
	  </tr>
	  <tr>
	    <td height="180" align="center">下午</td>
	    <td id="d0_12"></td>
	    <td id="d1_12"></td>
	    <td id="d2_12"></td>
	    <td id="d3_12"></td>
	    <td id="d4_12"></td>
	    <td id="d5_12"></td>
	    <td id="d6_12"></td>
	  </tr>
	  <tr>
	    <td height="180" align="center">晚上</td>
	    <td id="d0_18"></td>
	    <td id="d1_18"></td>
	    <td id="d2_18"></td>
	    <td id="d3_18"></td>
	    <td id="d4_18"></td>
	    <td id="d5_18"></td>
	    <td id="d6_18"></td>
	  </tr>
  </tbody>
</table>
 
</div>

</div>

</div>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/formvalidate.vjs","",$text)"></script>
<script type="text/javascript" src="/js/kindeditor-min.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/lang/${globals.getLocale()}.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript" src="/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript">
function changeSelect(){
	$(".hideBg").show();
    $("#closeDiv").show();
	form.submit();
}

function changeSize(obj){
	$(obj).width(100);
	obj.value="$!today";	
	openInputDate(obj);
}
function dateChange(){
	operations("now");
}
function openInputDate(obj){
	//WdatePicker({lang:'$globals.getLocale()'});
	WdatePicker({lang:'zh_CN',onpicked:dateChange})	
}



var  startstr="";
var  endstr="";
 var shu=["d0_00","d0_12","d0_18","d1_00","d1_12","d1_18","d2_00","d2_12","d2_18","d3_00","d3_12","d3_18","d4_00","d4_12","d4_18","d5_00","d5_12","d5_18","d6_00","d6_12","d6_18"];
var meetings=
[
   #foreach($meeting in $meetings)
   {meetingId:'$meeting.id',meetingtitle:'$meeting.title',sponsor:'$!globals.getEmpFullNameByUserId($meeting.sponsor)', toastmasterName:'$meeting.toastmasterName', startTime:'$timeformat.format($meeting.startTime)', endTime:'$timeformat.format($meeting.endTime)',sponsorId:'$!meeting.sponsor',toastmaster:'$!meeting.toastmaster',participant:'$!meeting.participant',authority:'$!meeting.authority',selectop:'$!meeting.selectop',boardroom:'#if("$!meeting.boardroomId"=="outer")$!meeting.outerAddress#else #if("$!boardroomMap.get($!meeting.boardroomId).boardroomName"=="")会议室已删除#end $!boardroomMap.get($!meeting.boardroomId).boardroomName#end'},
   #end
];
$(function(){
   for(var i in meetings){
      var meeting=meetings[i];
      var a= Date.parse(meeting.startTime);
      var b=Date.parse("$days.get(0)");
      var id="";
      if(a<b){
      	  id="d0_00";
      }else{
      	  a=new Date(a);
      	  id="d";
	      if(a.getHours()>=0 && a.getHours()<12 ){
	          id=id+a.getDay()+"_00";
	      }else if(a.getHours()>=12 && a.getHours()<18 ){
	          id=id+a.getDay()+"_12";
	      }else{
	          id=id+a.getDay()+"_18";
	      }
      }
      startstr=id;     
      /*a= Date.parse(meeting.endTime);
      b=Date.parse("$days.get(7)");
      id="";
      if(a>b){
       	  id="d6_18";
      }else{
	      a=new Date(a);
	      id="d";
	      if(a.getHours()>=0 && a.getHours()<12 ){
	          id=id+a.getDay()+"_00";
	      }else if(a.getHours()>=12 && a.getHours()<18 ){
	          id=id+a.getDay()+"_12";
	      }else {
	          id=id+a.getDay()+"_18";
	      }
      }    */
       endstr=id;
      
  var yyyy=datestyle(meeting.startTime).split(" ")[0];
    var div="<a href='javascript:void(0);' style='cursor:default;' ";
     #if("$!type"!="SELECT")    
    if(meeting.selectop=="1"){
    var renyuan=";"+meeting.sponsorId+";"+meeting.toastmaster+meeting.participant+meeting.authority;
   if(renyuan.indexOf(";$!globals.getLoginBean().getId();")!=-1||"$!globals.getLoginBean().getId()"=="1"){
     div=div + "  onclick=\"ahref('"+meeting.meetingId+"','"+yyyy+"')\"";
    }else{
         div=div + "  onclick=\"alert('sorry!你没有这个会议的查看权限')\"";
    }
    }else{
       div=div + "  onclick=\"ahref('"+meeting.meetingId+"','"+yyyy+"')\"";
    }
     #end
     if(meeting.meetingtitle != undefined && meeting.meetingtitle.length > 30){
     	var meetingtitle = meeting.meetingtitle.substring(0,30)+"...";
     }else{
     	meetingtitle = meeting.meetingtitle;
     }
     //判断是否完成
     var yourtime = datestyle(meeting.endTime).replace("-","/");//替换字符，变成标准格式  
	 var d2=new Date();//取今天的日期  
	 var d1 = new Date(Date.parse(yourtime));  	
     if(d1 < d2){
     	div=div+" > <div class='meeted'> <ul> ";
     }else{
     	div=div+" > <div class='meeting'> <ul> ";
     }        
      div = div + " <li class='meet-title' title="+meeting.meetingtitle+">主题："+meetingtitle+"<span class='show-all-title'>主题："+meeting.meetingtitle+"</span></li>";
      div = div + " <li>发起人："+meeting.sponsor+"</li>";
      div = div + "  <li>主持人："+meeting.toastmasterName+"</li>";
       div = div + "  <li class='meet-room-time'>会议室："+meeting.boardroom+"</li>";
      div = div + "  <li class='meet-room-time'>时间："+datestyle(meeting.startTime).split(" ")[1] +"-"+datestyle(meeting.endTime).split(" ")[1]+"</li>";
      div = div + " </ul> </div> </a>";
    //  $(id).append(div);
      
       var isOk=false;
	   for(var i=0;i<shu.length;i++){
		   if(shu[i]==startstr||isOk){
		   		$("#"+shu[i]).append(div);//实质操作的部分
		   		isOk=true;
		   		if(shu[i]==endstr){
		   			break;
		   		}
		   }
	   }
   }
   
   #if("$!istoday"!="")
   $("tr").find("td:eq($!istoday)").addClass("today");
   #end
});

function ahref(meetingid,meetingTime){
    var formval=$("#form").serialize();
    formval=formval.replace(/&/g,"@");
    var addTHhead = $("#addTHhead").val(); 
    if(addTHhead == "true"){
    	window.location.href ="/Meeting.do?operation=4&addTHhead=true&requestType=NOTE&shijian="+meetingTime+"&meetingId="+meetingid+"&backUrl=/Meeting.do?"+formval;
    }else{
    	window.location.href ="/Meeting.do?operation=4&requestType=NOTE&shijian="+meetingTime+"&meetingId="+meetingid+"&backUrl=/Meeting.do?"+formval;
    }  	
}


function operations(week){
var nowweek=form.week.value;
  if(week=="add"){
    nowweek=parseFloat(nowweek)+parseFloat(1);//上一周
  }else if(week=="subtract"){
    nowweek=parseFloat(nowweek)-parseFloat(1);//下一周
  }else{
  	nowweek=0;
  }
  form.week.value=nowweek;
  $(".hideBg").show();
  $("#closeDiv").show();
  form.submit();
}

function datestyle(time){
   var start=new Date(time);
   var str="";
   var month=start.getMonth()+1;
         var day=start.getDate();
         if(month<10){
         month="0"+month;
         }
         if(day<10){
         day="0"+day;
         }
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
   str=start.getFullYear()+"-"+month+"-"+day+" "+hs+":"+mins;
   return str;
}
</script>
<script type="text/javascript">
$(function(){
	$("#scroll-wrap").height(document.documentElement.clientHeight);
});
</script>
<div id="hideBg" class="hideBg"></div>
<div id="closeDiv" style='width:135px;height:102px;position:absolute;left:650px;top:240px;display:none;z-index:10001;'><img src='/style/images/load.gif'/><div style='color:#0179bb;font-weight:bold;'>正在查询，请稍等......</div></div> 

  </body>
</html>
