$(function(){
	$("#scroll-wrap").height(document.documentElement.clientHeight);
});

/*取消会议*/
function whyBox(meetingid,self){
	 var addTHhead = $("#addTHhead").val();
     var url="/Meeting.do?operation=2&requestType=WHYBOX&id="+meetingid+"&addTHhead="+addTHhead;
		asyncbox.open({
			id : 'warmsetting',url : url,title : '取消会议',
	　　 	width : 355, height : 240,
			btnsbar : jQuery.btn.OKCANCEL, 
		    callback : function(action,opener){
			    if(action == 'ok'){ 			    	
			    	var aform=opener.form;
			    	 var why=aform.why.value;
                    if(why == ""){
                          asyncbox.confirm('确定不写取消会议原因','提示',function(action){
　　　                     		if(action == 'ok'){
	　　　　                        aform.why.value="会议取消";
	                               	aform.submit();
	                        		$("#status_"+meetingid).html("");
				    	  	 		$("#rowstatus_"+meetingid).html("已取消");
				    	    		$(self).remove();	
　　　                       	}
　                           });  
　                	 }else{
                        aform.submit();
                        $("#status_"+meetingid).html("");
			    	    $("#rowstatus_"+meetingid).html("已取消");
			    	    $(self).remove();	
	    	    	 }	    	   		    	
			    	return false;
				}
	　　      }
	 　 });
}

function dealAsyn(){
 	asyncbox.tips("操作成功",'success');
	jQuery.close('warmsetting');
	
}

function openInputDate(obj){
	WdatePicker({lang:'zh_CN'});
}



//处理双击个人部门弹出框
function fillData(datas){
	$("#createBy").val(datas.split(";")[0])
	$("#userName").val(datas.split(";")[1])
	jQuery.close('Popdiv');
}

/*申请缺席*/
function absent(id,self){
	var signined="<a href=\"javascript:void(0);\" onclick=\"resetsignin('"+id+"',this)\" title='取消申请'>已申请不出席</a>";
	var signining="<a href=\"javascript:void(0);\" onclick=\"resetsignin('"+id+"',this)\" title='取消签到'>已签到</a>";
	asyncbox.open({
			id : 'warmsetting',html : '<h3>原因</h3><form name=\"wen\"><textarea id=\"yuan\" name=\"yuanyin\" cols=\"35\" rows=\"6\" ><\/textarea><\/form>' ,title : '不能出席',
	　　 	width : 300, height : 240,
			btnsbar : jQuery.btn.OKCANCEL, 
		    callback : function(action,opener){		    
			    if(action == 'ok'){ 	
				    if($("#yuan").val()==""){
				       	alert("原因不能为空");
				       	return false;
			    	}		    	
				url="/Meeting.do?operation=4&requestType=SIGNIN&absent=ABSENT&meetingId="+id+"&absentText="+encodeURIComponent($("#yuan").val().replace(/;/g,"|,|").replace(/=/g,"|-|"));
				jQuery.ajax({
				   	type: "GET",
				   	url: url,
				   	success: function(msg){
				      if(msg == "yes"){
				          asyncbox.tips("申请成功",'success');
				         $(self).parent().html(signined);
				      }else if(msg == "absent"){
					      asyncbox.tips("您已处理！",'error');
					      $(self).parent().html(signined);
				      }else if(msg == "signin"  ||  msg == "late" ){
					      asyncbox.tips("您已处理！",'error');
					      $(self).parent().html(signining);
				      }else if(msg == "cancel" ){
					      asyncbox.tips("会议已取消",'error');
					      $(self).parent().html("");
					      var shuzu= url.split("&");
					      var meetingid="";
					      for(i in shuzu){
					        if(shuzu[i].indexOf("meetingId")!=-1){
						        shuzu=shuzu[i].split("=");
							    meetingid=shuzu[1];
							    break;
					        }
					      }   
					      $("#rowstatus_"+meetingid).html("已取消");
				      }else{
				      	  asyncbox.tips("申请失败",'error');
				       }
				   }
				});
			}
	　　 }
	 });
}

/**签到*/
function signin(id,self){
	var signined="<a href=\"javascript:void(0);\" onclick=\"resetsignin('"+id+"',this)\" title='取消签到'>已签到</a>";
	var signining="<a href=\"javascript:void(0);\" onclick=\"resetsignin('"+id+"',this)\" title='取消申请'>已申请不出席</a>";
	url="/Meeting.do?operation=4&requestType=SIGNIN&meetingId="+id;
	jQuery.ajax({
	   type: "GET",
	   url: url,
	   success: function(msg){
	      if(msg == "yes"){
		      asyncbox.tips("签到成功",'success');
		        // $(self).text("已签到");
		      $(self).parent().html(signined);
	      }else if(msg == "absent"){
		      asyncbox.tips("您已处理！",'error');
		      $(self).parent().html(signining);
	      }else if(msg == "signin"  ||  msg == "late" ){
		      asyncbox.tips("您已处理！",'error');
		      $(self).parent().html(signined);
	      }else if(msg == "cancel" ){
		      asyncbox.tips("会议已取消",'error');
		      $(self).parent().html("");
		      var shuzu= url.split("&");
		      var meetingid="";
		      for(i in shuzu){
		        if(shuzu[i].indexOf("meetingId")!=-1){
		           shuzu=shuzu[i].split("=");
		           meetingid=shuzu[1];
		           break;
		        }
		      }    
	       $("#rowstatus_"+meetingid).html("已取消");
	       }else{
	       	  asyncbox.tips("签到失败",'error');
	       }
	   }
	});
}
/*重置签到*/
function resetsignin(id,self){
	url="/Meeting.do?operation=4&requestType=RESETSIGNIN&meetingId="+id;	
	jQuery.ajax({
	   type: "GET",
	   url: url,
	   success: function(msg){
	      if(msg == "yes"){
		      asyncbox.tips("操作成功",'success');
		      var a=" <a href=\"javascript:void(0);\" onclick=\"signin('"+id+"',this)\" >签到</a>"
		      a=a+ " <a href=\"javascript:void(0);\" onclick=\"absent('"+id+"',this)\" >不能出席</a>";
		      $(self).parent().html(a);
	      }else if(msg == "cancel" ){
	          asyncbox.tips("会议已取消",'error');
	       	  $(self).parent().html("");
		      var shuzu= url.split("&");
		      var meetingid="";
		      for(i in shuzu){
		        if(shuzu[i].indexOf("meetingId")!=-1){
		           shuzu=shuzu[i].split("=");
		           meetingid=shuzu[1];
		           break;
		        }
		      }	     
	       $("#rowstatus_"+meetingid).html("已取消");
	      }
	   }
	}); 
}


function  selectSubmit(){
	jQuery.ajax({
	   type: "POST",
	   url: "/Meeting.do",
	   data:"operation=4&"+$("#form").serialize(),
	   success: function(msg){
	      $("#tbody").html(msg);
	   }
	});
}

/*删除会议*/
function deletemeeting(url,self){
 	asyncbox.confirm('确定删除会议？','警告',function(action){
　　	if(action == 'ok'){
	　　　　jQuery.ajax({
				type: "GET",
				url:url,
				success: function(msg){
					if(msg == "yes"){
						asyncbox.tips("删除成功",'success');
						$(self).parent().parent().parent().remove();
					}else{
						asyncbox.tips("删除失败",'error');
					}
				}
			});
　		}
	});  
}

/*清空*/
function kong(){
	form.meetingStartTime.value="";
	form.meetingEndTime.value="";
	form.boardroomId.value="all";
	form.userType.value="0";
	form.selectStatus.value="0";
	//form.submit();
}

function formdel(){		
	var addTHhead = $("#addTHhead").val();
	//if(!isChecked("keyId")){ alert('请选择至少一条记录'); return false; } 
	if(sureDel('keyId')){
		form.action.value = "/Meeting.do?addTHhead="+addTHhead ;
		form.submit(); 
	}
}

function updateMeetings(Id){
	var addTHhead = $("#addTHhead").val();
	location.href="/Meeting.do?operation=7&meetingId="+Id+"&addTHhead="+addTHhead;
}