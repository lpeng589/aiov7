<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#else
<title>$text.get("oa.bbs.indexPage")</title>
#end
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" /> 
<link type="text/css" rel="stylesheet" href="/style/css/forum.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>

<script language="javascript">
/**wyy*/
function addCollection(oTopicId,moduleId){
	 //var title = encodeURIComponent("论坛")+":$!BBSForum.topicName";
	 var url = "/OACollectionAction.do?operation=2&attType=add&typeName=OABBSSends&keyId="+moduleId;
	 jQuery.ajax({
	 	 type: "POST",
	  	 url: url,	
	  	 data:{
	  	 	urlparam:"/OABBSForumAction.do?operation=70&topicId="+oTopicId+"&forumId="+moduleId,
	  	 	titleName:"论坛:$!BBSForum.topicName"
	  	 },	   
	  	 success: function(msg){
	  	 	if(msg == "OK"){
	  	 		asyncbox.tips('收藏成功','success');
	  	 		var Id = $("#bt_"+oTopicId);
	  	 		var Ids = $("#"+oTopicId);
	  	 		$(Id).remove();
	  	 		$(Ids).append("<button id=\"bt_"+oTopicId+"\" type=\"button\" onClick=\"delCollection('"+oTopicId+"','"+moduleId+"');\" class=\"bu_02\">取消收藏</button>");
	  		}else{
	  			asyncbox.tips('收藏失败','error');		    
	  		}
	  	}
	});
}
function delCollection(oTopicId,moduleId){
	 var url = "/OACollectionAction.do?operation=2&attType=del&typeName=OABBSSends&keyId="+moduleId;
	 jQuery.ajax({
	 	 type: "POST",
	  	 url: url,		   
	  	 success: function(msg){
	  	 	if(msg == "OK"){
	  	 		asyncbox.tips('取消收藏成功','success');
	  	 		var Id = $("#bt_"+oTopicId);
	  	 		var Ids = $("#"+oTopicId);
	  	 		$(Id).remove();
	  	 		$(Ids).append("<button id=\"bt_"+oTopicId+"\" type=\"button\" onClick=\"addCollection('"+oTopicId+"','"+moduleId+"');\" class=\"bu_02\">添加收藏</button>");
	  		}else{
	  			asyncbox.tips('取消收藏失败','error');		    
	  		}
	  	}
	});
}


if(typeof(top.jblockUI)!="undefined")top.junblockUI();

var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="topicContent"]',{
		uploadJson:'/UtilServlet?operation=uploadFile',
		resizeType : 0
	});
});


function saveReadingInfo(){
 	var str="/UtilServlet?operation=addOAReadingInfo&infoId=$!BBSForum.id&infoTable=OABBSSend";
 	AjaxRequest(str);
}

function changeFileName(obj){
	var filePath = obj.value;
	var starIndex = filePath.lastIndexOf("\\");
	var endIndex = filePath.lastIndexOf(".");
	var fileName = filePath.substr(starIndex+1,(endIndex-starIndex-1));
	document.getElementById("fileName").value = fileName;
}

function showStatus(obj)  {
    document.getElementById("status").style.visibility="visible";
}

function voteSumit(){
	form2.action = "/OABBSVoteAction.do" ;
	form2.operation.value = $globals.getOP("OP_UPDATE") ;
	 
	form2.submit() ;
}

function changeVoteBtn(){
	var answer = document.getElementsByName("voteAnswer") ;
	var existCheck = false ;
	for(var i=0;i<answer.length;i++){
		if(answer[i].checked){
			existCheck = true ;
			break ;
		}
	}
	if(existCheck){
		document.getElementById("voteBtn").disabled = "" ;
	}else{
		document.getElementById("voteBtn").disabled = "disabled" ;
	}
}

function rt(){	
	editor.html("");
}

function forBack(){
	window.location="/OABBSForumQueryAction.do?topicId=$!BBSForum.topic.id&addTHhead=$!addTHhead";
}

function onDelete(replayId,topicid,formid){
	if(confirm("$text.get("oa.common.sureDelete")")){
		window.location="/OABBSForumAction.do?operation=3&type=replay&addTHhead=$!addTHhead&replayId="+replayId+"&topicId="+topicid+"&forumId="+formid;
	}
}

isSave=false;
function subPrepream(){
	var content= editor.html();
	//content= content.replace(/<[^>].*?>/g,""); 干掉编辑框中的所有标签。






	//content=content.replace(/&nbsp;/g,"");
	
	if(content.trim().length==0 && fileCount==0){
  		alert("$text.get('oa.common.content')"+"$text.get('common.validate.error.null')") ;
  		editor.focus();
  		return false ;
  	}
	document.form2.operation.value=$globals.getOP("OP_ADD");
	isSave=true;
	return true ;
}

//下载文件
function downFile(filepath,filename){
	document.location = "/OABBSForumAction.do?operation=$globals.getOP("OP_DOWNLOAD")&type=downFile&filename="+filepath+"&sendId=$globals.get($arr_send,0)&topicId=$!topicId&addTHhead=$!addTHhead&fileName2="+encodeURI(filename);
}

function deleteZhuti(sendId){
	if(confirm('$text.get("common.msg.confirmDel")')){
		location.href="/OABBSForumAction.do?operation=$globals.getOP("OP_DELETE")&sendId="+sendId+"&topicId=$!topicId&title=$globals.encode($!title)&userName=$globals.encode($!userName)&pageNo=$!pageNo&beginTime=$!beginTime&endTime=$!endTime&searchSel=$!searchSel&addTHhead=$!addTHhead";
	}
}

function collection(){
	var favoriteURL = encodeURIComponent("/OABBSForumAction.do?operation=70&forumId=$!BBSForum.id&topicId=$!BBSForum.topic.id&noback=true") ;
	var tabName = encodeURI("企业论坛");
	var favoriteName = encodeURIComponent('&ldquo;$!BBSForum.topicName.replaceAll("\"","&ldquo;")&rdquo;') ;								
	var str="/UtilServlet?operation=addDedailAdviceInfoCollection&module=dedailAdviceInfo&tabName="+tabName+"&&favoriteName="+favoriteName+"&favoriteURL="+favoriteURL;
	AjaxRequest(str);
   	var value = response;
	var oaul=document.getElementById("empList");
	if(value=="1"){			 	
		alert('$text.get("oa.favorite.add.success")');
	}else if(value=="2"){
		alert('$text.get("oa.favorite.exist")');
	}else{
		alert('$text.get("oa.favorite.add.failture")');
	}
}


//邀请阅读


function openInvite(){
 	
	var title = encodeURI("“$text.get("oa.bbs.innerBBS")-$!BBSTopic.SortName”主题为【$!BBSForum.topicName】");
	var userinvite = encodeURI("$globals.getEmpNameById($LoginBean.id)");
	var favoriteURL = encodeURIComponent("/OABBSForumAction.do?operation=70&forumId=$!BBSForum.id&topicId=$!BBSForum.topic.id") ;
	var favoriteName = "";
	
	#if("$!BBSTopic.isRealname" == "1") 
		favoriteName = encodeURIComponent("$!BBSUser.fullName$text.get("oa.common.oabbs.invite")&ldquo;$!BBSForum.topicName&rdquo;") ;					
	#else 
		favoriteName = encodeURIComponent("$!BBSUser.nickName$text.get("oa.common.oabbs.invite")&ldquo;$!BBSForum.topicName&rdquo;") ;						
	#end
	var urls = "/AdviceAction.do?module=invitePre&id=$!BBSForum.id&userinvite="+userinvite+"&favoriteURL=" + favoriteURL +"&favoriteName="+favoriteName+"&title="+title;
	asyncbox.open({
		 id : 'dealdiv',
	 	 title : '邀请阅读',
	　　　url : urls,
	　　　width : 700,
	　　　height : 350,
		 btnsbar : jQuery.btn.OKCANCEL,
		callback : function(action,opener){
	　　　　　	//判断 action 值


	　　　　　	if(action == 'ok'){
	　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID
	　　　　　　　		if(!opener.checkeSet()){
					return false;
		   		}else{
		   			opener.submitForm();
		   		}
	　　　　　}
　　　 	}
　	});
}



function beforeunload(){
	var content= editor.html();
	if(!isSave && content.length>0 ){
		if(notbefor!="not"){
			return "$text.get("common.msg.chanegnosave")"; 
		}
  	notbefor="";
  	}
}

function deleteForum(){
	if(confirm('$text.get("common.msg.confirmDel")')){
		window.location.href="/OABBSForumAction.do?operation=$globals.getOP("OP_DELETE")&forumId=$!BBSForum.id&topicId=$!BBSTopic.id&searchSel=$!searchSel&addTHhead=$!addTHhead";
	}
}

function keydown(){
	var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;  
	ev = oEditor.EditorWindow.event;		
	if(ev.ctrlKey && (ev.keyCode == 13)){
		if(subPrepream()){
			document.form.submit();
		}
	}
}

function FCKeditor_OnComplete( editorInstance ) { 
	editorInstance.EditorDocument.attachEvent("onkeydown", keydown); 
} 

function fieldSetReplay(name,time,data){
	str= '<fieldset class="bbsfieldset"><legend>$text.get("common.lb.Reference")：'+name+' '+time+'</legend>  '+
		 document.getElementById(data).innerHTML+
		 '</fieldset>&nbsp;'; 
	var height=document.getElementById("heart_id").scrollHeight;
	$("#heart_id").animate({scrollTop: height}, 500);
	editor.focus();
	editor.html(str) ;
	
}

function resizeImg(){
	 
	scrwidth = (document.body.clientWidth) - 300;
	for(var i=0; i<document.images.length; i++) {
		var img = document.images[i];
		if(img.width > scrwidth){
			img.width = scrwidth;
		}
   }
   
   if($(".right_content table").width() > (document.body.clientWidth-160)){
   		
   		var width = document.body.clientWidth-230 ;
   		$(".right_content").css({'width':width+'px','overflow-x':'auto'});
   }
}
function readingMark(){
	var strUrl = '/OAReadingRecordAction.do?readingInfoId=$!BBSForum.id&readingInfoTable=OABBSSend&url=$!uri#if($noback)&noback=true #end' ;
	asyncbox.open({
		title : '$text.get('oa.readingMark')',
		url : strUrl,
		cache : true,
		width : 820,
		height :350,
		btnsbar : jQuery.btn.CANCEL
	});
}
function ifVoteTime(){
 	#if( "$!voteDate" !="after" && $voteUser.size()<=0 )
 		window.location.href="/OABBSVoteAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&forumId=$!BBSForum.id&topicId=$!BBSTopic.id&searchSel=$!searchSel&addTHhead=$!addTHhead";
 	#else 
 	 	alert("投票已开始或是投票已结束！");
	#end	 
}
</script>

<style type="text/css">
.pagetopbtn {background:transparent url(/style/images/client/icon.gif) no-repeat scroll 1000px 1000px ;}
.pagetopbtn {width:40px;height:40px;background-position:0px -472px;cursor:pointer;display:block;}
.pagetop {position: absolute;z-index: 100;bottom:55px;right:80px;overflow: hidden; position: fixed;display:none;}
</style>
</head>
<body onLoad="saveReadingInfo() ; resizeImg(); " onbeforeunload="return beforeunload()" >
#if("$!addTHhead" == "true")
	#parse("./././body2head.jsp")
#end
<div class="framework" >
<a name="html_top"></a>
<div class="TopTitle" >
	<span><img src="/style/images/bbs/forumICON.gif" /></span>
	<span>$text.get("oa.inner.bbs")</span>
	<div>
			#set($theURL=$request.getQueryString().replace("&","/"))
		 
			
			#if($LoginBean.id=="1" || $BBSTopic.bbsUserId==$BBSUser.userID || $BBSTopic.bbsUserId2==$BBSUser.userID)				
				 #if($!BBSForum.isElite == 0)
					 <button type="button" onClick="javascript:location.href='/OABBSForumAction.do?operation=$globals.getOP("OP_UPDATE")&type=elite&paramValue=1&forumId=$!BBSForum.id&topicId=$!BBSForum.topic.id&searchSel=$!searchSel&addTHhead=$!addTHhead'" class="bu_02">$text.get("oa.bbs.setasDistillate")</button> 
				 #else
					 <button type="button" onClick="javascript:location.href='/OABBSForumAction.do?operation=$globals.getOP("OP_UPDATE")&type=elite&paramValue=0&forumId=$!BBSForum.id&topicId=$!BBSForum.topic.id&searchSel=$!searchSel&addTHhead=$!addTHhead'" class="bu_02">$text.get("oa.bbs.cancelDistillate") </button>
				 #end
				#if($!BBSForum.isSetTop == 0)
					<button type="button" onClick="javascript:location.href='/OABBSForumAction.do?operation=$globals.getOP("OP_UPDATE")&type=setTop&paramValue=1&forumId=$!BBSForum.id&topicId=$!BBSForum.topic.id&searchSel=$!searchSel&addTHhead=$!addTHhead'" class="bu_02">$text.get("oa.bbs.setTop")</button>
				#else
					<button type="button" onClick="javascript:location.href='/OABBSForumAction.do?operation=$globals.getOP("OP_UPDATE")&type=setTop&paramValue=0&forumId=$!BBSForum.id&topicId=$!BBSForum.topic.id&searchSel=$!searchSel&addTHhead=$!addTHhead'" class="bu_02">$text.get("common.lb.cancel")$text.get("oa.bbs.setTop")</button>
				#end			
			#end
		#if($noback)
		<button type="button" onClick="closeWin();" class="bu_01">$text.get("common.lb.close")</button></li>
		#else		
	  		#if("$!desktop"=="yes")
			<button type="button" onClick="history.go(-1)" class="bu_01">$text.get("common.lb.back")</button>
	  		#else
			#if("$!isEspecial"=="1")
			<button type="button" onClick="location.href='/OAFavorite.do?operation=4&type=OAFavorite&keyWord=$globals.encode($!keyWord)&addTHhead=$!addTHhead'" class="bu_01">$text.get("common.lb.back")</button>
			#else
	  		<button type="button" onClick="location.href='/OABBSForumQueryAction.do?topicId=$!BBSForum.topic.id&timeTlag=month&forumId=$!BBSForum.id&searchSel=$!searchSel&addTHhead=$!addTHhead'" class="bu_01">$text.get("common.lb.back")</button>
			#end
	 		#end
		#end 

	</div>
</div>
<div class="heart2">
	<div class="heart_title" style="border-bottom:0px;">
		<span>$text.get("common.msg.currstation")：#if("$!parentName"=="")$text.get("oabbs.com.lb.bbsHead") #else $!parentName #end &gt;&gt;
	 			 $!BBSForum.topicName</span>
         <input type="hidden" name="sendId" value="$!BBSForum.id" />
        <span style="float:right;margin:5px 50px 0 0;">  
          #if("$!nextforumId" !="")           
          <a href="/OABBSForumAction.do?operation=70&forumId=$nextforumId&topicId=$!BBSForum.topic.id&addTHhead=$!addTHhead">             
          <img src="/style/images/bbs/prev.png" title="$text.get("oa.bbs.lb.prepTopic")" border="0"/>
          </a>
          #end
          &nbsp;
          #if("$!preforumId" !="")
          <a href="/OABBSForumAction.do?operation=70&forumId=$preforumId&topicId=$!BBSForum.topic.id&addTHhead=$!addTHhead">           
          <img src="/style/images/bbs/next.png" title="$text.get("oa.bbs.lb.nextTopic")" border="0"/> </a>
          #end
        </span> 
	</div>
	<div class="heart_content" id="heart_id" style="background:#f5f5f5;border:1px solid #f5f5f5;" >
<form name="form2" action="/OABBSForumAction.do?&searchSel=$!searchSel&addTHhead=$!addTHhead" method="post">
<input type="hidden" name="operation" value="$globals.getOP("OP_SEND_PREPARE")"/>
<input type="hidden" name="topicId" value="$!BBSForum.topic.id" />
<input type="hidden" name="forumId" value="$!BBSForum.id" />
	<script type="text/javascript">
		var oDiv=document.getElementById("heart_id");
		var sHeight=document.documentElement.clientHeight-80;
		oDiv.style.height=sHeight+"px";
	</script>
		<a name="HTML_top">&nbsp;</a>
		<table cellpadding="0" cellspacing="0" class="notes" style="margin-top:0px;">
			<tr>
				<td class="notes_left">
					<div class="left_title">$!BBSForum.bbsUser.nickName#if("$!BBSTopic.isRealname" == "1" && "$!BBSForum.bbsUser.nickName"!="$!BBSForum.bbsUser.fullName")/$!BBSForum.bbsUser.fullName #end</div>
					<div class="left_photos">
						<img name="myPhoto" style="width:126px;height:126px;" src="$!myPhoto"  border="0" />						
					</div>
					<div class="left_describe">
						$text.get("oa.bbs.grade")：$globals.getGrade($!BBSForum.bbsUser.userscore,$gradeList)<br />
						$text.get("oa.bbs.scroe")：$!BBSForum.bbsUser.userscore 
						<span style="padding-left:20px;padding-top:5px">
						#foreach($us in $userlist)
						#if($us.id==$!BBSForum.bbsUser.userID && $us.statusId!=-1 && $!BBSForum.bbsUser.userID!=$LoginBean.id)
							<a href="javascript:top.msgCommunicate('$!BBSForum.bbsUser.userID','$!BBSForum.bbsUser.nickName')"><img src="style/plan/msg_icon.gif" title="即时信息" /></a>
						#end
						#end
						</span>
					</div>
				</td>
				<td class="notes_right">
					<div class="right_title"><div >$!BBSForum.topicName</div>
					<!-- 
					<span>人气：<strong>3268</strong></span><span>回复：<strong>4</strong></span> -->
					</div>
					<div class="right_floor" >
						<b>楼主</b>$text.get("oa.bbs.bbsPublish")$text.get("oabbs.com.lb.at")<font style="font-size:11px;">$!BBSForum.createTime</font> 
						<!-- 发布投票后 有权限所可以看到的操作，修改 删除 -->
						#if($!BBSForum.createBy==$LoginBean.id || "$LoginBean.id"=="1" || "$!BBSTopic.bbsUserId"=="$!BBSUser.userID" || $BBSTopic.bbsUserId2==$BBSUser.userID)
							<!-- 修改投票 -->
							#if("$!BBSForum.forumType"=="vote")
							<a href="#" onclick="ifVoteTime();">【$text.get("oa.common.upd")】</a>
							#end
							
							#if("$!BBSForum.forumType"!="vote")
							<a href="/OABBSForumAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&forumId=$!BBSForum.id&topicId=$!BBSTopic.id&searchSel=$!searchSel&addTHhead=$!addTHhead">【$text.get("oa.common.upd")】</a>
							#if("$LoginBean.id"=="1" || "$!BBSTopic.bbsUserId"=="$!BBSUser.userID" || $BBSTopic.bbsUserId2==$BBSUser.userID)
				  			<a href="javascript:deleteForum()">【$text.get("common.lb.del")$text.get("oa.subjects")】 </a>
							#end
							<a href="/OABBSForumAction.do?operation=70&forumId=$!BBSForum.id&topicId=$!BBSTopic.id&auth=$!BBSForum.bbsUser.id&searchSel=$!searchSel&publi=1&auto2=$!auto2&order=$!order&addTHhead=$!addTHhead">#if($!auto2=="222") 【$text.get("oa.bbs.lb.viewAll")】 #else 【$text.get("oa.bbs.lb.viewAuther")】 #end</a>
							<a href="/OABBSForumAction.do?operation=70&forumId=$!BBSForum.id&topicId=$!BBSTopic.id&order=$!order&searchSel=$!searchSel&publi=2&auto2=$!auto2&auth=$!BBSForum.bbsUser.id&addTHhead=$!addTHhead">#if($!order=="asc") 【$text.get("oa.bbs.lb.viewasc")】  #else  【$text.get("oa.bbs.lb.viewdesc")】 #end </a>

						#end
						#end
					</div>
					<div >
					<div class="right_content">$!BBSForum.topicContent</div><div align="left" id="titleMargin"> 
					#if("$!BBSForum.forumType"=="vote") 
					</div><div class="bbs_vote">
						<table  cellpadding="0" cellspacing="0" width="100%">
						<tr>
							#if(!$isVote)
							<td>&nbsp;</td>
							#end
							<td colspan="2">$text.get("oabbs.com.lb.total") <font color="red">$!voteUser.size()</font> $text.get("oabbs.com.lb.PeopleNumber")
						&nbsp;&nbsp;&nbsp;($text.get("alertCenter.lb.startDate"):$voteBean.beginTime &nbsp;$text.get("alertCenter.lb.endDate"):$voteBean.endTime)</td>
						</tr>
						#foreach($answer in $voteBean.voteAnswer)
						<tr>
							#if(!$isVote)
							<td style="width: 20px;">
								<input class="checkbox" #if($voteBean.answerType=="single")type="radio" #else type="checkbox" #end name="voteAnswer" 
									value="$answer.id" onClick="changeVoteBtn()" /></td>
							#end
							<td>
								<label for="option_0">$velocityCount. &nbsp;$answer.voteAnswer</label>					
							</td>
							<td class="optionvotes">&nbsp;</td>
						</tr>
						<tr>
							#if(!$isVote)
							<td>&nbsp;</td>
							#end
							 
							#set($answerCount=$answer.voteCount*100/$voteCount) 
							#if("$!answerCount"=="")#set($answerCount=0)#end
							<td>
								<div style="background: $globals.getBackGround($velocityCount);width: $answerCount%;">&nbsp;</div>
								#if($voteBean.voteType=="1")
								<div style="margin-top: 10px;">$text.get("oabbs.common.lb.voter")：






								#foreach($urow in $voteUser)
									#if($urow.answerId.indexOf($answer.id)>-1)
									#if("$!BBSTopic.isRealname" == "1") $urow.bbsUser.fullName#else $urow.bbsUser.nickName#end;
									#end
								#end
								</div>
								#end
							</td>
							<td width="15%">$answerCount % <em class="pollvote2">($answer.voteCount/$!voteCount)</em></td>
						</tr>
						#end
						<tr>
							#if(!$isVote)
							<td>&nbsp;</td>
							#end
							<td colspan="2">
								#if($isVote)
									#if("$!voteDate" =="after")$text.get("oa.bbs.msg.largerVoDate")<!-- 投票已结束不能在投票 -->
									#elseif("$!voteDate" =="before")$text.get("oa.bbs.msg.novoTime")<!-- 还未到投票时间 -->
									#else
									$text.get("oabbs.common.msg.voted")<!-- 您已经投过票 ，谢谢参与 -->
									#end
								#else
								<button style="margin-left: 20px;" class="bu_01" id="voteBtn" type="button" disabled="disabled" onClick="return voteSumit()">$text.get("common.lb.submit")</button>		
								#end
							</td>
						</tr>
						</table>
					</div>
					#end
					</div>
					#if($downScope || "$LoginBean.id"=="1" || "$!BBSTopic.bbsUserId"=="$!BBSUser.userID" || "$!BBSTopic.bbsUserId2"=="$!BBSUser.userID")
					<div class="bbs_attachment">
					  #if($!BBSForum.attachment.indexOf("|") > 0)
						  #foreach ($str in $globals.strSplit($!BBSForum.attachment,'|'))
						  	 #set($attch=$globals.strSplitByFlag($str,','))
							 <span style="margin-left:5px;"><a href="javascript:downFile('$globals.get($attch,1)','$globals.get($attch,0)')"><img src="/style/images/bbs/uploading.gif"/>$globals.get($attch,0)</a></span>
						  #end
					  #else
					  	#foreach ($str in $globals.strSplit($!BBSForum.attachment,';'))
							 <span style="margin-left:5px;"><a href="/ReadFile?tempFile=bbs&fileName=$!globals.encode($str)" target="_blank"><img src="/style/images/bbs/uploading.gif"/>$str </a></span>
						#end
					  #end
					  </div>
					#end
					<div class="right_foot">
						<div  id="$!BBSForum.topic.id">
						#if("$!isEspecial"!="1")						
							<button type="button" onClick="javascript:readingMark();" class="bu_02">$text.get("oa.readingMark")</button>
						  	<button type="button" onClick="javascript:openInvite();"  class="bu_02">$text.get("oa.common.invitereading")</button>
							<!--<button type="button" onClick="addCollection('$!BBSForum.topic.id')" class="bu_02">$text.get("oa.bbs.bt.Attention")</button>-->
							 
							#if("$!attentionCard" == "attentionCard")
							<button id="bt_$!BBSForum.topic.id" type="button" onClick="addCollection('$!BBSForum.topic.id','$!BBSForum.id');" class="bu_02">添加收藏</button>
						   	<!--<button type="button" onClick="javascript:location.href='/OABBSForumAction.do?operation=$globals.getOP("OP_QUERY")&topicId=$!BBSForum.topic.id&forumId=$!BBSForum.id&type=AttentionCard&searchSel=$!searchSel'" class="bu_02">$text.get("oa.bbs.bt.Attention")</button>-->
							#else
							<button id="bt_$!BBSForum.topic.id" type="button" onClick="delCollection('$!BBSForum.topic.id','$!BBSForum.id');" class="bu_02">取消收藏</button>
						  	<!--<button type="button" onClick="javascript:location.href='/OABBSForumAction.do?operation=$globals.getOP("OP_QUERY")&topicId=$!BBSForum.topic.id&forumId=$!BBSForum.id&type=deleteAttentionCard&searchSel=$!searchSel'" class="bu_02">$text.get("oa.bbs.bt.abolishAttention")$text.get("bbs.module.attention")</button>
							#end
						  	<!-- <button type="button" onClick="javascript:collection();" class="bu_02">$text.get("aio.add.favorite")</button> -->  
						#end

						</div>
						<div style="float: left;margin-left: 282px;font-size: 12px;">
							<a href="#" id="html_bm">$text.get("oa.bbs.celerityrevertto")</a>
						</div>
						<b>$!BBSForum.bbsUser.signature</b>
					</div>
				</td>
			</tr>
		</table>
		#foreach ($replay in $replayList)
		<table cellpadding="0" cellspacing="0" class="notes">
		<tr>
			<td class="notes_left">
				<div class="left_title">$!replay.bbsUser.nickName#if("$!BBSTopic.isRealname" == "1" && "$!replay.bbsUser.nickName"!="$!replay.bbsUser.fullName")/$!replay.bbsUser.fullName #end</div>
				<div class="left_photos">
				
				<img name="rephoto" src="$!globals.checkEmployeePhoto('140',$userPhonos.get($!replay.bbsUser.userID).getId())"/>
				
				</div>
				<div class="left_describe">
					$text.get("oa.bbs.grade")： $globals.getGrade($!replay.bbsUser.userscore,$gradeList)<br />
					$text.get("oa.bbs.scroe")： $!replay.bbsUser.userscore 
					<span style="padding-left:20px;">
					#foreach($us in $userlist)
					#if($us.id==$!replay.bbsUser.userID && $us.statusId!=-1 && $!replay.bbsUser.userID!=$LoginBean.id)
							<a href="javascript:top.msgCommunicate('$!replay.bbsUser.userID','$!replay.bbsUser.nickName')" style="margin-left:5px;"><img src="style/plan/msg_icon.gif" title="即时消息"/></a>
					#end
					#end
					</span>
				</div>
			</td>
			<td class="notes_right">
				<div class="right_floor">
					<span>$text.get("oa.bbs.reverttoas")<font style="font-size:11px;">$!replay.createTime</font></span>
					#if("$LoginBean.id"=="1" || "$!BBSTopic.bbsUserId"=="$!BBSUser.userID" || $BBSTopic.bbsUserId2==$BBSUser.userID)
					<span><a href="javascript:onDelete('$!replay.id','$!BBSTopic.id','$!BBSForum.id')">【$text.get("common.lb.del")】</a></span>
					#end
					<span><a href="/OABBSForumAction.do?operation=70&forumId=$!BBSForum.id&topicId=$!BBSTopic.id&auth=$!replay.bbsUser.id&searchSel=$!searchSel&publi=1&auto2=$!auto2&order=$!order&addTHhead=$!addTHhead">#if($!auto2=="222") 【$text.get("oa.bbs.lb.viewAll")】 #else 【$text.get("oa.bbs.lb.viewAuther")】 #end</a></span>
					
				  <span style="float:right;margin-right:30px;">
					#if($velocityCount==1)
						$text.get("oa.bbs.lb.t2")
					#elseif($velocityCount==2)
						$text.get("oa.bbs.lb.t3")	
					#elseif($velocityCount==3)
						$text.get("oa.bbs.lb.t4")
					#elseif($velocityCount==4)
						$text.get("oa.bbs.lb.t5")
					#elseif($velocityCount==5)
						$text.get("oa.bbs.lb.t6")		
					#end
					#set($cc=$velocityCount+1)	
					<em><a name="HTML_$cc" style="color:#A2A2A2;">$cc</a></em><sup>#</sup>
					</span> 
					
					 <span style="float:right;margin-right:10px;margin-top:1.5px;"> 
					<a href="#" onclick="fieldSetReplay('$!replay.bbsUser.nickName#if("$!BBSTopic.isRealname" == "1" && "$!replay.bbsUser.nickName"!="$!replay.bbsUser.fullName")/$!replay.bbsUser.fullName #end','$!replay.createTime','REPLAY_$velocityCount')">$text.get("oa.bbs.lb.refreplay")</a>
					  </span> 
				</div>
				<div class="right_content" id="REPLAY_$velocityCount">$!replay.content</div>
				#if($downScope || "$LoginBean.id"=="1" || "$!BBSTopic.bbsUserId"=="$!BBSUser.userID" || $BBSTopic.bbsUserId2==$BBSUser.userID)
				<div class="bbs_attachment">
			    #if($!replay.attachment.indexOf("|") > 0)
					#foreach ($str in $globals.strSplit($!replay.attachment,'|'))
					  	 #set($attch=$globals.strSplitByFlag($str,','))
						 <span style="margin-left:5px;"><a href="javascript:downFile('$globals.get($attch,1)','$globals.get($attch,0)')" ><img src="/$globals.getStylePath()/images/down.gif"/>&nbsp;$globals.get($attch,0)</a></span>
					#end			  
				#else
				  	#foreach ($str in $globals.strSplit($!replay.attachment,';'))
						 <span style="margin-left:5px;"><a href="/ReadFile?tempFile=bbs&fileName=$!globals.encode($str)" target="_blank"><img src="/$globals.getStylePath()/images/down.gif"/>&nbsp;$str</a></span>
					#end
				#end
	            </div>	
				#end
				<div class="right_foot"><b>$!replay.bbsUser.signature</b></div>
			</td>
		</tr>
	</table>
	#end
	<div class="listRange_pagebar"> $!pageBar </div>
	</form>
#if($replayScope || "$LoginBean.id"=="1" || "$!BBSTopic.bbsUserId"=="$!BBSUser.userID" || $BBSTopic.bbsUserId2==$BBSUser.userID)
<form name="form" method="post" action="/OABBSForumAction.do?searchSel=$!searchSel&addTHhead=$!addTHhead"  onsubmit="return subPrepream();" >
<input type="hidden" name="operation" value="$globals.getOP("OP_SEND")"/>
<input type="hidden" name="userId" value="$!BBSUser.id" />		
<input type="hidden" name="topicId" value="$!BBSForum.topic.id" />
<input type="hidden" name="forumId" value="$!BBSForum.id" />
<input type="hidden" name="attachFiles" value=""/>
<input type="hidden" name="delFiles" value=""/>
	<table cellpadding="0" cellspacing="0" class="notes">
		<tr>
			<td class="notes_left">
				<div class="left_title">$!BBSUser.nickName #if("$!BBSTopic.isRealname" == "1" && "$!BBSUser.nickName"!="$!BBSUser.fullName")/$!BBSUser.fullName #end</div>
				<div class="left_photos">
						<img name="userPhoto" src="$!userPhoto" border="0" />					
				</div>
			</td>
			<td class="notes_right">
				<div class="right_floor reply_notes"><b>我来回复</b><a name="HTML_replaybox"></a></div>
				<div><textarea  name="topicContent" style="width:95%;height:300px;visibility:hidden;"></textarea></div>
				<div class="reply_foot">
					#if($uploadScope || "$LoginBean.id"=="1" || "$!BBSTopic.bbsUserId"=="$!BBSUser.userID" || $BBSTopic.bbsUserId2==$BBSUser.userID)
					<span id="files">
						<a style="cursor:pointer;" onclick="openAttachUpload('/bbs/');">$text.get("oa.common.accessories")</a>
						<input type="checkbox" name="replayNote" value="adviceTrue"/>回复通知楼主
					</span> 
					<div id ="status" style="visibility:hidden;color:Red"></div>
					<div id="files_preview"></div>
					#end
				</div>
				<div class="reply_botton">
					<button type="submit" name="Submit" class="bu_01">$text.get("oa.bbs.revertto")</button>
			  		<button type="reset" class="bu_01" onclick="rt();">$!text.get("common.lb.reset")</button>
			  		
			  		<!-- <button type=button class="bu_01" onclick="forBack();">$!text.get("common.lb.back")</button> -->
				</div>
			</td>
		</tr>
	</table>
</form>
#end

		</div>
	</div>
</div>

<div class="pagetop">
<a id="html_top" class="pagetopbtn" title="返回顶部"></a>
</div>

#if("$!invite"=="true")
<script>
setTimeout('invite()',500);
function invite(){
if(confirm('是否邀请别人阅读？')){openInvite();}

}
</script>
#end
<script>
/*当客户列表表头不在可见区域时，固定表头*/
	$("#heart_id").scroll(function(){ 
		if($("#titleMargin").position().top<=25){
		
			$(".pagetop").show();
			
		}else{
			$(".pagetop").hide();
		}
	});
	
	
/*返回顶部*/
	$("#html_top").click(function(){
		$("#heart_id").animate({scrollTop: 0}, 500);	
		
	});
	
/*快速回复*/
	$("#html_bm").click(function(){
		var height=document.getElementById("heart_id").scrollHeight;
		$("#heart_id").animate({scrollTop: height}, 500);
		editor.focus();
	});
	
/*内容中超链接全部设为新窗口打开*/	
	$(function(){
	  $(".right_content a").attr("target","_blank");
	});
</script>
</body>
</html>
