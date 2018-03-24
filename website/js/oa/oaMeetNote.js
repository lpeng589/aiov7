/*签到*/
function signin(url,self){
	jQuery.ajax({
	   type: "GET",
	   url: url,
	   success: function(msg){
	       if(msg.indexOf("br")!=-1){
	         	asyncbox.tips("签到成功",'success');
	         	var shu= $("#signined").text();
		        shu=parseInt(shu);
		        shu=shu+1;
	          	$("#signined").text(""+shu);
	         	shu= $("#unsignined").text();
	        	shu=parseInt(shu);
	        	shu=shu-1;
	          	$("#unsignined").text(""+shu);
	          	shu= $("#lated").text();
	        	shu=parseInt(shu);
	        	shu=shu+1;
	          	$("#lated").text(""+shu);
	           	$(self).parent().parent().append("<li>"+msg+"</li>");
	         	$(self).parent().text("已签到");
	       }else{
	         if(msg=="cancel"){
	             	asyncbox.tips("会议已取消",'error');
	             	$(self).parent().text("会议已取消");
	           }else{
	           		asyncbox.tips("签到失败",'error');
	           }
	       }
	   }
	});
}

/***/
function advice(url){
	jQuery.ajax({
	   type: "GET",
	   url: url,
	   success: function(msg){
	       if(msg == "yes"){
	       		asyncbox.tips("已通知",'success');    
	       }else{
	       		asyncbox.tips("通知失败",'error');
	       }
	   }
	});
}

/*笔录员*/
function takerchange(){
   var taker=$("#createBy").val();
   var url="/Meeting.do?operation=4&requestType=TAKER&meetingId="+$("#meetingId").val()+"&taker="+taker;
   jQuery.ajax({
   type: "GET",
   url: url,
   success: function(msg){
       if(msg == "yes"){
          asyncbox.tips("已指定成功",'success');  
          $("#takerName").text($("#createBy option:selected").text());  
       }else{
       asyncbox.tips("指定失败",'error');
       }
   }
});
}

/*删除讨论*/
function deletedebate(id){
	var url="/Meeting.do?operation=4&requestType=DEBATE&Debateoperate=delete&debateId="+id;
    jQuery.ajax({
	   type: "GET",
	   url: url,
	   success: function(msg){
	      if(msg=="yes"){
	          asyncbox.tips("删除成功",'success');  
	         alldebate();
	      }else{
	       asyncbox.tips("删除失败",'error');  
	      }
	   }
	});
}

function checkContent(){
	var str=jQuery.trim($('#content').val());
	if(str.length==0){
		return false;
	}
	return true;
}

/*提交讨论 */
function debatesubmit(){
		
    if(!checkContent()){
		alert("请先输入评论内容!");
		return false;
	}
	var content=$("#content").val();
	$("#addreply").hide();
	content = content.replace(/\[\/表情([0-9]*)\]/g,'<img src="/style1/images/face/$1.gif" border="0" />');
	content=encodeURIComponent(content);
	var url="/Meeting.do?operation=4&requestType=DEBATE&Debateoperate=add&meetingId="+$("#meetingId").val()+"&debateContent="+content;

	jQuery.ajax({
		type: "GET",
		url: url,
		success: function(msg){		
	        if(msg=="yes"){
	        $("#content").val("");
	           alldebate();
	           debateText();
	        }
		}
	});
}
/*讨论文本*/
function debateText(){
   if($("#debateform").css("display")=="none"){
	   $("#debateform").css("display","block");
	   $("#debatesay").css("display","none");
   }else{
	   $("#debateform").css("display","none");
	   $("#debatesay").css("display","block");
   }
}


/*申请缺席*/
function absent(id,self){
	var signined="<a href=\"javascript:void(0);\" onclick=\"resetsignin('"+id+"',this)\" title='取消申请'>已申请不赴会</a>";
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
					url="/Meeting.do?operation=4&requestType=SIGNIN&absent=ABSENT&meetingId="+id+"&absentText="+encodeURIComponent($("#yuan").val().replace(/;/g,",").replace(/=/g,"-"));
					jQuery.ajax({
					   type: "GET",
					   url: url,
					   success: function(msg){
					       if(msg == "yes"){
					          	asyncbox.tips("申请成功",'success');
					          	$(".span_self").html(signined);
					       }else if(msg == "absent"){
					       		asyncbox.tips("您已处理！",'error');
					       		$(".span_self").html(signined);
					       }else if(msg == "signin"  ||  msg == "late" ){
					       		asyncbox.tips("您已处理！",'error');
					       		$(".span_self").html(signining);
					       }else if(msg == "cancel" ){
					       		asyncbox.tips("会议已取消",'error');
					       		$(".span_self").html("会议已取消");
					      		var shuzu= url.split("&");
					      		var meetingid="";
					      		for(i in shuzu){
					        		if(shuzu[i].indexOf("meetingId")!=-1){
					           			shuzu=shuzu[i].split("=");
					           			meetingid=shuzu[1];
					           			break;
					        		}
					      		}					   				       
					       }else{
					       		asyncbox.tips("申请失败",'error');
					       }
					   }
					});
   				}
	　　      }
	 　 });
}

/*签到*/
function signinself(id,self){
	var signined="<a href=\"javascript:void(0);\" onclick=\"resetsignin('"+id+"',this)\" title='取消签到'>已签到</a>";
	var signining="<a href=\"javascript:void(0);\" onclick=\"resetsignin('"+id+"',this)\" title='取消申请'>已申请不赴会</a>";
	url="/Meeting.do?operation=4&requestType=SIGNIN&meetingId="+id;
	jQuery.ajax({
	   type: "GET",
	   url: url,
	   success: function(msg){
	       if(msg == "yes"){
		       asyncbox.tips("签到成功",'success');	        
		       $(".span_self").html(signined);
	       }else if(msg == "absent"){
	       		asyncbox.tips("您已处理！",'error');
	       $(".span_self").html(signining);
	       }else if(msg == "signin"  ||  msg == "late" ){
	       		asyncbox.tips("您已处理！",'error');
	       		$(".span_self").html(signined);
	       }else if(msg == "cancel" ){
	       		asyncbox.tips("会议已取消",'error');
	       		$(".span_self").html("会议已取消");
		      	var shuzu= url.split("&");
		      	var meetingid="";
		      	for(i in shuzu){
		        	if(shuzu[i].indexOf("meetingId")!=-1){
		           		shuzu=shuzu[i].split("=");
		           		meetingid=shuzu[1];
		           		break;
		        	}
		      	}	   	       
	       }else{
	        	asyncbox.tips("签到失败",'error');
	       }
	   }
	});
}

/*重置取消出席*/
function resetsignin(id,self){
	url="/Meeting.do?operation=4&requestType=RESETSIGNIN&meetingId="+id;
	jQuery.ajax({
	   type: "GET",
	   url: url,
	   success: function(msg){
	      if(msg == "yes"){
		      asyncbox.tips("操作成功",'success');
		      var a=" <a href=\"javascript:void(0);\" onclick=\"signinself('"+id+"',this)\" >签到</a>"
		      a=a+ " <a href=\"javascript:void(0);\" onclick=\"absent('"+id+"',this)\" >不能赴会</a>";
		      $(".span_self").html(a);
	      }else if(msg == "cancel" ){
		      asyncbox.tips("会议已取消",'error');
		      $(".span_self").html("会议已取消");
		      var shuzu= url.split("&");
		      var meetingid="";
		      for(i in shuzu){
		        	if(shuzu[i].indexOf("meetingId")!=-1){
		           		shuzu=shuzu[i].split("=");
		           		meetingid=shuzu[1];
		           		break;
		        	}
		      }	   	      
	      }
	   }
	});	   
}

function replayCont(obj,ID,distId){
	$("#discutform").hide();
	$("#createById").val(ID);
	$("#discutform").show();
	$(obj).after($("#discutform"));
	$("#discutId").val(distId);
	//$("#discutform").detach();
}
function discutText(){
	$("#discutform").hide();
}
function discutSubmit(){
	var content=$("#repContent").val();
	if(content==""){
		alert("内容不能为空");
	}
	var commitId = $("#discutId").val();
	var userId = $("#createById").val();
	//content = content.replace(/\[\/表情([0-9]*)\]/g,'<img src="/style1/images/face/$1.gif" border="0" />');
	content=encodeURIComponent(content);
	var url="/Meeting.do?operation=4&requestType=DEBATE&Debateoperate=add&flag=second&meetingId="+$("#meetingId").val()+"&debateContent="+content;

	jQuery.ajax({
		type: "GET",
		url: url,
		data:{
			userId:userId,
			commitId:commitId,
		},
		success: function(msg){		
	        if(msg=="yes"){
	        	//asyncbox.tips("操作成功",' success');
	        	$("#repContent").val("");
	            alldebate();
	            debateText();
	            
	        }
		}
	});
}