<link rel="stylesheet" href="/style/css/qqFace.css"  type="text/css" />
<script type="text/javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script type="text/javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script type="text/javascript" src="/js/jquery.qqFace.js"></script>
<script type="text/javascript">
var contentId = "content_"+ksSendId;

$(function(){
	   $('#face1_'+ksSendId).qqFace({
		id : 'facebox1', 
		assign: contentId, 
		path: '/js/plugins/emoticons/images/', 
		tip : 'em_'
	 });
	 //回车事件
	 $("#content_"+ksSendId).keydown(function(event) {
	 	if (event.ctrlKey && event.which == 13) {  
         	sendMsg();
         }  
         /*if (event.keyCode == 13) {  
         	sendMsg();
         }  */
     }) 
});

function replace_em(str){
	 str = str.replace(/\</g,'<；');
	 str = str.replace(/\>/g,'>；'); 
	 str = str.replace(/\n/g,'<；br/>；'); 
	 str = str.replace(/\[em_([0-9]*)\]/g,'<img src="/js/plugins/emoticons/images/$1.gif" border="0" />'); 
	 return str; 
}

function onCompleteUpload(retstr){
	retstr = decodeURIComponent(retstr); 
	if(retstr.length>0){
		var content = "发送了文件："+retstr.substring(0,retstr.length-1) ;
		var message = $("#msgList_"+ksSendId) ;
		var curTime = DateFormat.format(new Date(),'hh-mm-ss').replaceAll("-",":");
		message.append("<div class=\"nameIn\">$!LoginBean.empFullName<span>"+curTime+"</span></div><p>"+content+"</p>");
		jQuery.get("/MessageAction.do?operation=71&sendType="+ksSendType+"&receiveId="+ksSendId+"&receiveName="+encodeURI("$!user.empFullName")+"&content="+encodeURIComponent(content)+"&affix="+encodeURIComponent(retstr),function(data){
			if("NO"==data){
				message.append("<div class=\"nameOn\">$!LoginBean.empFullName<span>"+curTime+"</span></div><p>信息发送失败</p>");
			}
		}) ;
		var varMsg = document.getElementById("msgend_"+ksSendId) ;
		varMsg.scrollIntoView() ;
	} 
	var attachUpload = document.getElementById("attachUpload");
	attachUpload.style.display="none";
}

/*验证是否为空*/
function isNull(variable,message){
	if(variable=="" || variable==null){
		alert(message+" "+"$text.get('common.validate.error.null')");
		return false;
	}else{
		return true;
	}		
}
function sendMsg(){
	var content = $("#content_"+ksSendId).val();
	if(trimStr(content).length == 0){
		alert(trimStr(content)+" 发送"+"$text.get('common.validate.error.null')");
		return false;
	}		
	if(!isNull(content,'发送'+'$text.get("common.lb.content")'))	{
		return false;
	}
	
	if(content.length>0){
		content = replace_em(content);
		if(content.indexOf("<；br/>；")!=-1){
			content = content.replaceAll("<；br/>；","<br/>");
		}
		
		var message = $("#msgList_"+ksSendId) ;
		var curTime = DateFormat.format(new Date(),'hh-mm-ss').replaceAll("-",":");
		/*if(content.indexOf("width=")>-1){
			content = content.replaceAll("<img title=\"单击放大\" alt=\"单击放大\"","<img title=\"单击放大\" alt=\"单击放大\" style=\"cursor: pointer;\" onclick=\"zoomImage(this)\"") ;
		}*/	
		message.append("<div class=\"nameIn\">$!LoginBean.empFullName<span>"+curTime+"</span></div><p>"+content.replaceAll("<；","&lt;").replaceAll("；","")+"</p>");
		
		jQuery.ajax({
			type:"post",
			url:"/MessageAction.do?operation=71&sendType="+ksSendType+"&receiveId="+ksSendId+"&receiveName="+encodeURI("$!user.empFullName"),
			data:{
				content:encodeURI(content.replaceAll("<；","&lt;").replaceAll("；",""))
			},
			success:function(data){
				if("NO"==data){
					message.append("<div class=\"nameOn\">$!LoginBean.empFullName<span>"+curTime+"</span></div><p>信息发送失败</p>");
				}
			}
		});	
		$("#content_"+ksSendId).val('');	
		var varMsg = document.getElementById("msgend_"+ksSendId) ;
		varMsg.scrollIntoView() ;
	}
}

function load(){
	
    var winIndex = 1 ;
    if(typeof(window.parent.top.pindex)!="undefined"){
        winIndex = window.parent.top.pindex.win.currentwin.divIndex
    }else{
        winIndex = parent.parent.jQuery.fn.jerichoTab.activeIndex ;
    }
	if(winIndex==window.parent.$("#winIndex").val()){
		var message = $("#msgList_"+ksSendId) ;
		jQuery.get("/MessageAction.do?operation=69&receiveType="+ksSendType+"&receiveId="+ksSendId,function(data){
			if(data.length>0){
				message.append(data);
				var varMsg = document.getElementById("msgend_"+ksSendId) ;
				if($("#li_"+ksSendId).val() != undefined){
					if($("#showKKDiv").is(":visible")){
						$("#showKKDiv").addClass('showKKlog-back');						
					}						
				}
				if($("#showKK").html() != ""){
					varMsg.scrollIntoView() ;
				}
				
			}
		});
	}
	lod = setTimeout("load();",10000);
	//setTimeout("load();",10000);
}



function receiveAffix(msgId,msgId){
	jQuery.get("/MessageAction.do?operation=2&msgId="+msgId,function(data){});
	$("#"+msgId).html("下载") ;
}

function historyMsg(sendId){
	if($("#historyId_"+sendId).css("display")=="none"){
		$("#historyId_"+sendId).attr('src','/MessageAction.do?operation=4&type=history&operType='+ksSendType+'&receive='+sendId);
		$("#empInfoId_"+sendId).hide() ;
		$("#historyId_"+sendId).show() ;
	}else{
		$("#empInfoId_"+sendId).show() ;
		$("#historyId_"+sendId).hide() ;
	}
}

function zoomImage(obj){
	window.open(obj.src) ;
}
//图片上传前操作



function openUploadFile(Id){
	document.getElementById("uploadFile_"+Id).click();
}
function before_upload(Id){	
	var formobj =  document.getElementById("form_"+Id);
	var formdata = new FormData(formobj);
	jQuery.ajax({
       	type:"POST",url: "/UploadServlet?path=/pic/tblEmployee/",data:formdata, contentType:false,processData:false,
		success: function(data){
			if(data.indexOf("ERROR")>=0){
				jQuery("#li_uploadfile").remove();
				asyncbox.alert(data,'error');
			}else{			
				var curTime = DateFormat.format(new Date(),'hh-mm-ss').replaceAll("-",":");
				var content = '<a href="/ReadFile.jpg?tempFile=false&type=PIC&tableName=tblEmployee&fileName='+data+'" target="_blank"><p><img src="/ReadFile.jpg?tempFile=false&type=PIC&tableName=tblEmployee&fileName='+data+'" width="200" height="100" alt="单击放大" ></a></p>';
				$("#msgList_"+ksSendId).append("<div class=\"nameIn\">$!LoginBean.empFullName<span>"+curTime+"</span></div>"+content);	
				jQuery.get("/MessageAction.do?operation=71&sendType="+ksSendType+"&receiveId="+ksSendId+"&receiveName="+encodeURI("$!user.empFullName")+"&content="+encodeURIComponent(content)+"&picture="+encodeURI(data),function(data){
					if("NO"==data){
						message.append("<div class=\"nameOn\">$!LoginBean.empFullName<span>"+curTime+"</span></div><p>上传失败</p>");
					}
				}) ;
				
				var varMsg = document.getElementById("msgend_"+ksSendId) ;
				varMsg.scrollIntoView() ;
				
				
			}						
		},
		error:function(msg){
			jQuery("#li_uploadfile").remove();
			asyncbox.alert(msg,'error');
		}
	});  	
}
function closePopup(sendId){
	/*var hans = $("#ul_left>li").last().attr('onclick');
	eval(hans);*/
	if($("#ul_left>li").size() == 1){
		clearTimeout(lod);
		//clearTimeout(openDg);
		$("#talk-list").remove();
		$("#showKK").html('');
		$("#showKK").hide();
	}else{		
		if($("#li_"+sendId).next().val() == undefined){
			/*$("#li_"+sendId).prev().addClass("focus-li");
			$("#right_"+sendId).prev().show();*/
			$("#li_"+sendId).prev().addClass("focus-li");
			var hans = $("#li_"+sendId).prev().attr('onclick');
			eval(hans);
		}else{
			/*$("#li_"+sendId).next().addClass("focus-li");
			$("#right_"+sendId).next().show();*/
			$("#li_"+sendId).next().addClass("focus-li");
			var hans = $("#li_"+sendId).next().attr('onclick');
			eval(hans);
		}
	}
	$("#li_"+sendId).remove();
	$("#right_"+sendId).remove();
	stopEvent();
}

//-->
</script>

<div class="poupMsg">	
	<div class="Poup_body">
		<table cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td valign="top">
					<table cellpadding="0" cellspacing="0" border="0" style="width:340px;border-right:1px #1362b6 solid;">
					<tr>
						<td valign="top">
							<div  class="magList_body">
								<div id="msgList_$!sendId">
								
								</div>
								<a id='msgend_$!sendId' ></a>
							</div>
						</td>
					</tr>
					<tr charoff="2">
						<td class="entering">
							<div class="enter-list">
								<span class="kk-icons faceBtn" id="face1_$!sendId" ></span>
								<form class="e-form" action="/UploadServlet" method="post" name="form_$!sendId" id="form_$!sendId" enctype="multipart/form-data">
									<input id="uploadFile_$!sendId" name="uploadFile_$!sendId"  type="file" style="display: none;" onchange="before_upload('$!sendId');"/>
									<span class="kk-icons pictureBtn" title="图片上传" onclick="openUploadFile('$!sendId');"></span>
								</form>		
								#if("$!sendId" != "$!globals.getLoginBean().id")					
								<span class="kk-icons afileBtn" onclick="openAttachUpload('/message/$!sendId/');"></span>
								#end
								<span class="s-history" onclick="historyMsg('$!sendId');">聊天记录</span>
								<p class="clear"></p>
							</div>
							<textarea class="im-content" name="content_$!sendId" id="content_$!sendId"></textarea>	
							<div class="get-kk-btn">			
								<input type="button" value="发送" onClick="sendMsg();" title="Ctrl+Enter"/>
								<input type="button" value="关闭" onClick="closePopup('$!sendId');"/>
							</div>		
						</td>
					</tr>
				</table>
				</td>
				<td valign="top" style="font-size: 12px;">
					#if("$!sendType"=="person")
					<table id="empInfoId_$!sendId" cellpadding="0" cellspacing="0" border="0">
						<!-- 
						<tr>
							<td class="magList_head" style="width:134px;">$!user.empFullName<span>详细资料</span></td>
						</tr>
						 -->
						<tr>
							<td valign="middle" align="center"  style="width:134px;height:100px;">
								<img width="80px" height="80px;" src="$!myPhoto">							
							</td>
						</tr>
						<tr>
							<td class="magusInfo">
								<div>姓名：<span>$!user.empFullName</span></div>
								<div>性别：<span>$!globals.getEnumerationItemsDisplay('sex',$!user.Gender)</span></div>
								<div>手机：<span>$!user.Mobile</span></div>
								<div>电话：<span>$!user.Tel</span></div>
								<div>邮箱：<span>$!user.Email</span></div>
								<div>部门：<span>$!deptName</span></div>
								<div>职位：<span>$!globals.getEnumerationItemsDisplay('duty',$!user.TitleID)</span></div>
							</td>
						</tr>
					</table>
					#else
					<table id="empInfoId_$!sendId" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td class="magList_head" ><span>#if("$!sendType"=="dept")部门成员#else分组成员#end</span></td>
						</tr>
						<tr>
							<td valign="top">
								<div class="groupList s-group" style="width:137px; height:285px;line-height: 25px;">
									#foreach($user in $empList)
									<div onclick="javascript:window.parent.openDialog('person','$user.id','$!user.empFullName')"><span class="s-img #if($globals.isOnline($user.id))group_c1 #else group_c2 #end"><img src="$globals.checkEmployeePhoto('48',$user.id)" /><i class="ib"></i></span> $!user.empFullName</div>
									#end
								</div>
							</td>
						</tr>
					</table>
					#end
					<iframe id="historyId_$!sendId" style="display:none; margin-top:2px;" src="" marginheight="0" marginwidth="0" width="340" height="300" scrolling="no" frameborder="0" ></iframe>
				</td>
			</tr>
		</table>	
		</div>
</div>