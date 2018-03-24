<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.common.newsContent")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript" src="/js/jquery.qqFace.min.js"></script>
<link rel="stylesheet" href="/style/css/qqFace.css"  type="text/css" />
<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css" />

<script type="text/javascript"> 

//实例化表情插件




$(function(){
	$('#face1').qqFace({
		id : 'facebox1', //表情盒子的I
		assign: 'content', //给那个控件赋值


		path: '/style1/images/face/'	//表情存放的路径


	});
});

function checkMaxInput(){
	var maxLen=140;
	var myLen=0;
	var i=0;
	var str=jQuery.trim($('#content').val());
	for(;(i<str.length)&&(myLen<=maxLen*2);i++){
		if(str.charCodeAt(i)>0&&str.charCodeAt(i)<128)
			myLen++;
		else
			myLen+=2;
	}
	/*
	if(str.length>0){	
		jQuery('#replyButton').removeAttr('disabled');
	}else{
		jQuery('#replyButton').attr('disabled','disabled');
	}
	*/
	if(myLen>maxLen*2){
		$("#content").val(str.substring(0,i-1));
		 //document.getElementById('content').innerText=str.substring(0,i-1);
	}else{
		 document.getElementById('leaves').innerHTML=Math.floor((maxLen*2-myLen)/2);
	}
	
}

function checkContent(){
	var str=jQuery.trim($('#content').val());
	if(str.length==0){
		return false;
	}
	return true;
}

function submitForm(){
	if(!checkContent()){
		alert("请先输入评论内容!");
		return false;
	}

	 var content=$("#content").val();
	 $("#addreply").hide();
	 content = content.replace(/\[\/表情([0-9]*)\]/g,'<img src="/style1/images/face/$1.gif" border="0" />')
	 jQuery("#content").val(content);
	 replyform.submit();	
}	

function openAddReply(){
	if($("#addreply").css("display") == "block"){
		$("#addreply").hide();
		document.getElementById("content").value="";
	}else{
		$("#addreply").show();
		$('#content').focus();
	}
}

function isDelete(m){
	asyncbox.confirm('$text.get("oa.common.sureDelete")','$!text.get("clueTo")',function(action){
	　　 if(action == 'ok'){
		document.location = "/OAnewAdviceAction.do?operation=$globals.getOP("OP_DELETE")&type=deletereply&adviceId=$!adviceId&replyId="+m;
		}
	});
}
</script>
</head>
<body>
<a name="HTML_top"></a>

<form name="form" id="form" action="/OAnewAdvice.do?operation=5&type=addreplyPrepare&adviceId=$!adviceId" method="post">
<input type="hidden" name="operation" value="5" />
<div class="SNews_comment">
	<div>		
		<div class="comment_list">	
				<div class="comment_top">
					<span onclick="openAddReply();" style="cursor: pointer;font-weight:bold;color:blue;"><img src="style1/images/oaimages/C_C.gif" />$text.get("I.want.reply")</span>
				</div>
							
				<div style="overflow:hidden; overflow-y:auto; width:99%;scrollbar-base-color: #F9F9F9;">
					#if($replyList.size()==0)
						<br />
						<h4 align="center">
							暂时没有评论,<a href="javascript:void(0);" onclick="openAddReply();"><font color="blue">我要评论</font></a>!
						</h4>
					#end			 
					#foreach ($reply in $replyList)
					<table cellpadding="0" cellspacing="0" border="0"  >
						<tr>
							<td class="headPh">
									<img name="myPhoto" width="80" height="70"
										src="$!globals.checkEmployeePhoto('48',$!reply.createBy)" border="0" alt="用户头像" />
							</td>
							<td class="UsCo">
								<span> $!reply.fullName :</span>
								<div id=="result" style="margin-left: 40px;">
									$!reply.content.replaceAll("\r\n","<br/>") 
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="comment_list_bottom">
								<font style="margin-right: 10px;" >$!reply.createTime </font>
								#if($!LoginBean.id == 1)											
									<a href="javascript:isDelete('$reply.id');" style="margin-right: 30px;"><img src="style1/images/oaimages/ni_3.gif"  style="margin-right:2px;" /><span >$text.get("oa.common.del")</span></a>
								#end
							</td>
						</tr>
					</table>
					#end
							
					#if($replyList.size()>0)
						<div class="listRange_pagebar"  style="width: 400px;">
							<a href="#HTML_top;" onclick="openAddReply();" ><font style="float: left; padding-top:5px; color: blue;font-weight: bold; cursor: pointer;">快速评论&nbsp;&nbsp;&nbsp;</font></a>	
							$!pageBar						
						</div>
					#end
				
				</div>
			</div>
	</div>
</div>
</form>			
<form name="replyform" action="/OAnewAdviceAction.do?operation=$globals.getOP("OP_QUERY")&type=addreply" method="post">
<input type="hidden" name="adviceid" value="$!adviceId"/>
<div id="addreply" style="display:none;" class="reply_div">
	<div>
		<img src="style1/images/oaimages/l-i-02.gif" style="margin-left:10px;"/>$text.get("reply")<span style="float: right;margin-right:10px;">还可以输入<b id="leaves" style="color:red;">140</b>个字</span>
	</div>
	<textarea id="content" name="content" style="width:97%;height:95px;margin:5px;" onkeyup="checkMaxInput();" ></textarea>
	<span class="faceBtn" id="face1" onclick="javascript:$('#content').focus();"><img src="style1/images/face/face.gif"/>添加表情</img></span>
	<span style="float: right;margin-right:10px;">
		<input type="button" id="replyButton" class="comment_pl" onclick="submitForm();"  value="$text.get("reply")"/>
		<input type="button" class="comment_pl" onclick="openAddReply();" value="$text.get("com.lb.close")"/>
	</span>
</div>	
</form>	
</body>
</html>
