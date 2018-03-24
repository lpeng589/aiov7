/*人员弹出框*/
var fieldNames;
var fieldNIds;
function deptPopForJob(popname,fieldName,fieldNameIds){
	/*var participantId="";
   	$("#participantName").parent().find(".textboxlist .boxSelect").each(function(){
		if($(this).attr("id")!="undefined"){
			participantId +=$(this).attr("id")+",";
		}
	})*/
	var urls = "/Accredit.do?popname=" + popname + "&inputType=checkbox";
	fieldNames=fieldName;
	fieldNIds=fieldNameIds;
	asyncbox.open({
		title : '请选择人员',
	    id : 'Popdiv',
	　　url : urls,
	　　width : 755,
	　	height : 430,
		btnsbar :jQuery.btn.OKCANCEL,
		callback : function(action,opener){	　　　　　	
　　　　　 if(action == 'ok'){	　　　　　　　
				var str = opener.strData;				
				newOpenSelectSearch(str,fieldName,fieldNameIds);
　　　　　	}					 
　　　	 }
　	});
}

function newOpenSelectSearch(str,fieldName,fieldNameIds){
	var datas = str.split("|");
	for(var i=0;i<datas.length;i++){
		if(datas[i]!=""){
			var data = datas[i].split(";");	
			if(fieldName=="toastmasterName"){
				shareByTBox1.add(data[1],data[0]);
				var toastmasterId = jQuery("#toastmaster").val()+data[0]+";";
				jQuery("#toastmaster").val(toastmasterId);
			}						
			if(fieldName=="participantName"){
				shareByTBox2.add(data[1],data[0]);
				var participantId = jQuery("#participant").val()+data[0]+";";
				jQuery("#participant").val(participantId);
			}			
		}
	}
}

//弹出框双击回填内容
function fillData(datas){   
	newOpenSelectSearch(datas,fieldNames,fieldNIds);
	jQuery.close('Popdiv')
}

function openInputDate(obj){
	WdatePicker({lang:'zh_CN'});
}



function timehidden(){
	 var val= form.regularriqi.value+" "+form.starthouseregular.value+":"+form.startminregular.value; 
	 form.meetingStartTime.value=val;
	 var val= form.regularriqi.value+" "+form.endhouseregular.value+":"+form.endminregular.value;
	 form.meetingEndTime.value=val; 
}

/*会议情况查询**/
var starttime;
var endtime;
var url="b";
var urla="a";
function boardroomchange(){
	var boardroomId = $("#boardroom").val();
	if(boardroomId == "newOpen"){
		$("#boardroom").val("0");
		var height = 340;	
		var url = "/OABoardroom.do?operation=6&flagOut=Out";
		asyncbox.open({
			id:'Prodiv',url:url,title:'新增会议室',width:540,height:height,cache:false,modal:true			  
	　  });
		return false;
	}else if(boardroomId == "0"){
		return false;
	}else{
		timehidden();
		starttime=form.meetingStartTime.value;
		endtime=form.meetingEndTime.value;
		var date=/^(\d{4})-(\d{1,2})-(\d{1,2})\s(\d{1})(\d{1}):(\d{1})(\d{1})$/
		if(date.test(starttime)&&date.test(endtime)){
			url="/Meeting.do?operation=4&requestType=OCCUPATION&boardroomId="+boardroomId+"&meetingStartTime="+starttime+"&meetingEndTime="+endtime;		 
			  jQuery.ajax({
			   type: "GET",
			   url:url ,
			   success: function(msg){	    
				   if(msg == "1"){
					   	asyncbox.tips('改会议室已经被占用了,不能使用','error');
					   	$("#boardroom").val("0");				      	
					}
					if(msg == "5"){
						asyncbox.tips('发生错误!','error');
						$("#boardroom").val("0");
					}
	   			}
			});
		}
	}
}
/**时间格式*/
function IEInt(time){
	  var shu=0;
	  if(time == "00"){
	  		return shu;
	  }else if(time == "01"){
	  		return 1;
	  }else if(time == "02"){
	   		return 2;
	  }else if(time == "03"){
	   		return 3;
	  }else if(time == "04"){
	   		return 4;
	  }else if(time == "05"){
	   		return 5;
	  }else if(time == "06"){
	   		return 6;
	  }else if(time == "07"){
	   		return 7;
	  }else if(time == "08"){
	   		return 8;
	  }else if(time == "09"){
	   		return 9;
	  }else{
	    	return parseInt(time);
	  }
  
}

/*空 判断*/
function isNull(variable,message){
	if(variable=="" || variable==null){
		alert(message);
		return false;
	}else{
		return true;
	}
}

/*调会议周程表*/
function weektable(){
	var start=form.regularriqi.value;
  	if(start == ""){
  		start=new Date();
    	start= start.getFullYear()+"-"+(start.getMonth()+1)+"-"+start.getDate();     
  	}
  	var boardroom=form.boardroomId.value;
  	if(boardroom == "0" ){
     	alert("请选择会议室");
     	return ;
  	}
  	var url="/Meeting.do?operation=4&requestType=BOARDROOMWEEK&Select=SELECT&boardroomId="+boardroom+"&meetingStartTime="+start;
  	asyncbox.open({
			id : 'warmsetting',url:url ,title : '会议室周程表',
	　　 	width : 1143, height :500
	 　 });
}

/*会议使用情况*/
function meetRoomUsing(){
	var dateTime = $("#regularriqi").val();
	var url="/Meeting.do?operation=4&requestType=MEETROOMUSING&dateTime="+dateTime;
  	asyncbox.open({
			id : 'meetRoomUsing',url:url ,title : '会议室使用情况',
	　　 	width : 650, height :400
	 　 });
}

/*时间格式转化**/
function getDateParse(datestr){
   if(datestr != ""){
    	var temp=datestr.split(" ");
     	var yyyymmdd=temp[0].split("-");
     	var yyyy=parseInt(yyyymmdd[0]);
     	var mm=IEInt(yyyymmdd[1])-1;
     	var dd=IEInt(yyyymmdd[2]);
     	var hhmin=temp[1].split(":");
     	var hh=IEInt(hhmin[0]);
     	var min=IEInt(hhmin[1]);
    	return new Date(yyyy,mm,dd,hh,min,0);
    }else{
    	return 1111;
    }
}


/*填充会议日期**/
function riqiweek(self){
      var wen=$(self).val();  
      if(wen!=""){
      var riqi= wen.split("-");
      var d= new Date(riqi[0],(riqi[1]-1),riqi[2]);
      d= d.getDay();
      if(d==1){
         	$("#regularweekstr").text("星期一");
         	form.regularweek.value="1";
      }else if(d==2){
      		$("#regularweekstr").text("星期二");
      		form.regularweek.value="2";
      }else if(d==3){
      		$("#regularweekstr").text("星期三");
      		form.regularweek.value="3";
      }else if(d==4){
      		$("#regularweekstr").text("星期四");
      		form.regularweek.value="4";
      }else if(d==5){
      		$("#regularweekstr").text("星期五");
      		form.regularweek.value="5";
      }else if(d==6){
      		$("#regularweekstr").text("星期六");
      		form.regularweek.value="6";
      }else{
      		$("#regularweekstr").text("星期日");
      		form.regularweek.value="0";
      }
    }
}

/*例会切换**/
function regulardisplay(val){
   if(val=="0"){
      $(".regular").css("display","none");
   }else{
      $(".regular").css("display","table-row");
   }
}

/*显示更多*/
function moreData(obj){
	if($(obj).attr('bg') == "more"){
		jQuery("#twoTbl").show();
		$(obj).attr('bg','litle');
		$(obj).html("收起");
	}else{
		jQuery("#twoTbl").hide();
		$(obj).attr('bg','more');
		$(obj).html("更多");
	}
}



/*会议保存**/
var updateStartTime=$("#updateStartTime").val();
var updateEndTime=$("#updateEndTime").val();
var updateBoardroom=$("#updateBoardroom").val();
var updateMeetingId=$("#updateMeetingId").val();
function checksubmit(){
	if(updateStartTime == undefined){
		updateStartTime="";
	}
	if(updateEndTime == undefined){
		updateEndTime="";
	}
	if(updateBoardroom == undefined){
		updateBoardroom="";
	}
	if(updateMeetingId == undefined){
		updateMeetingId="";
	}
   var participant="";
   var participantname="";
   var nums = 0;//参与人数
   $("#participantName").parent().find(".textboxlist .boxSelect").each(function(){
		if($(this).attr("id")!="undefined"){
			participant +=$(this).attr("id")+";";
			nums +=1;
		}
	})
	
	$("#participantName").parent().find(".textboxlist .showSelect").each(function(){
		if($(this).text()!="undefined"){
			participantname +=$(this).text()+";";
		}
	}) 
   var toastmaster="";
   var toastmastername="";
  
   $("#toastmasterName").parent().find(".textboxlist .boxSelect").each(function(){
		if($(this).attr("id")!="undefined"){
			toastmaster +=$(this).attr("id")+";";
		}
	})
	$("#toastmasterName").parent().find(".textboxlist .showSelect").each(function(){
		if($(this).text()!="undefined"){
			toastmastername +=$(this).text()+";";
		}
	}) 
   form.toastmaster.value=toastmaster;
   form.toastmasterName.value=toastmastername;
   form.participant.value=participant;
   form.participantName.value=participantname;
    
    timehidden();
    var Notepadcontent = $("#meetingContent").val();
    
    
    if(!isNull(form.title.value,'主题不能为空')){
   		$(form.title).focus();
  		return false;
  	}
  
  	if(!isNull(form.toastmaster.value,'请选择主持人')){
  		return false;
  	}
  	  	  	 	 
	if(!isNull(form.regularriqi.value,'请输入会议日期')){  	  
	 	return false;
	}
  	
  	var isregular = $("input[name='isregular']:checked").val();
  	if(isregular=="0"){
  	form.regularMeeting.value="0";
  	}else{
  		form.regularMeeting.value=$("input[name='regulartype']:checked").val(); 			
   		var start=$("#regularriqi").val(); 	  
  	}
 	
    var temp=form.meetingStartTime.value.split(" ");
    var yyyymmdd=temp[0].split("-");
    var yyyy=parseInt(yyyymmdd[0]);
    var mm=IEInt(yyyymmdd[1])-1;
    var dd=IEInt(yyyymmdd[2]);
    var hhmin=temp[1].split(":");
    var hh=IEInt(hhmin[0]);
    var min=IEInt(hhmin[1]);
    var starttime=new Date(yyyy,mm,dd,hh,min,0);
    var temp=form.meetingEndTime.value.split(" ");
    var yyyymmdd=temp[0].split("-");
    var yyyy=parseInt(yyyymmdd[0]);
    var mm=IEInt(yyyymmdd[1])-1;
    var dd=IEInt(yyyymmdd[2]);
    var hhmin=temp[1].split(":");
    var hh=IEInt(hhmin[0]);
    var min=IEInt(hhmin[1]);
    var endtime=new Date(yyyy,mm,dd,hh,min,0);
 	var meetingId=form.meetingId.value;
  	if(meetingId=="" || meetingId==null){
  	    if(starttime-(new Date())<0){
  	       alert("开始时间必须大于当前时间");
  	       return false;
  	    }
  	}
  	
  	if(endtime-starttime<=0){
	  	alert("结束时间必须大于开始时间");
	  	return false;
  	}
  	
  	if( $("input[name='wakeUpMode']:checked").size()==0){
  	  alert("通知方式至少选择一种");
  	  return false;
  	}
  	  	
  	if(!isNull(form.participant.value,'请选择参与人')){
  		return false;
  	}
  	  	  	
  	if(!isNull(Notepadcontent,'内容不能为空')){		
  		return false;
  	}
  	  	  	
    var boardroom= $("#boardroom").val();
    if(boardroom == "0"){
      alert("请选择会议室");
      return false;
    }else{
    if((getDateParse(updateStartTime)- getDateParse(form.meetingStartTime.value)==0) && (getDateParse(updateEndTime) - getDateParse(form.meetingEndTime.value)==0 ) && updateBoardroom == form.boardroomId.value){        
           form.submit();
           return true;
    }else{
  //判断会议人数是否大于会议室容纳人数只是警告
    	var renshu=$("#boardroom option:selected").attr("renshu");
    	if(isNaN(renshu)){
    		renshu=0;
    	}
    	renshu=renshu+"";
    	renshu=parseInt(renshu);
    	if(renshu!=0){   
    		if(renshu<nums){
    			asyncbox.confirm('参与人员多于会议室大约容纳人员,确定继续？','警告',function(action){
	　　　          if(action == 'ok'){
	                        form.submit();
	              			return true;
					}
				});
    		}else{
		         form.submit();
         		 return true;
    		}   			    
    	}else{
             form.submit();
   			 return true;
    	}       	
		}   
    }          	
}

function reset(){
	form.reset();
}