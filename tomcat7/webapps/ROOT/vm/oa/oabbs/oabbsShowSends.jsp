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
<link rel="stylesheet" type="text/css" href="/$globals.getStylePath()/css/ListRange.css" />
<link rel="stylesheet" type="text/css" href="/$globals.getStylePath()/css/CRselectBox2.css"/>
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css"/>
<link rel="stylesheet" type="text/css" href="/style/css/forum.css" />
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/aioselect.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>

<style>
.appTime>span{float:left;display:inline-block;}
.appTime>input{float:left;display:inline-block;width:100px;}
.appTime>div{float:left;display:inline-block;}
.deTime>span{display:block;text-align:center;width:100%;}
.deTime>ul{width:100%;display:none;}
.deTime>ul li{line-height:25px;cursor:pointer;text-align:center;}
.deTime>ul li:hover{background:#f2f2f2;}
</style>
<script>
/*wyy**/
function addDelMoudle(oTopicId){
	 var flag = $("#moudle_"+oTopicId).attr('bg');
	 var url = "/OACollectionAction.do?operation=2&attType="+flag+"&typeName=OABBSTopic&keyId="+oTopicId;
	 jQuery.ajax({
	 	 type: "POST",
	  	 url: url,	
	  	 data:{
	  	 	urlparam:"/OABBSForumQueryAction.do?topicId="+oTopicId,
	  	 	titleName:"论坛版块:$!moudelNames"
	  	 },	   
	  	 success: function(msg){
	  	 	if(flag == "add"){
	  	 		if(msg == "OK"){
		  	 		asyncbox.tips('收藏成功','success');
		  	 		$("#moudle_"+oTopicId).attr('bg','del');
		  	 		$("#moudle_"+oTopicId).html('取消关注');		  	 		
	  			}else{
	  				asyncbox.tips('收藏失败','error');		    
	  			}
	  	 	}else{
	  	 		if(msg == "OK"){
		  	 		asyncbox.tips('取消收藏成功','success');
		  	 		$("#moudle_"+oTopicId).attr('bg','add');
		  	 		$("#moudle_"+oTopicId).html('关注版块');
	  			}else{
	  				asyncbox.tips('取消收藏失败','error');		    
	  			}
	  	 	}	  	 	
	  	}
	});
}


function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}

function clearPagePara(form){
   form.pageParam.value ="clearPageParam";
   form.conditionType.value = "condition";
}

function moveForum(){

	if(!isChecked("forumId")){
		alert('$text.get("common.msg.mustSelectOne")');
		return false;   
	}
	document.form.operation.value = "$globals.getOP("OP_UPDATE")" ;
	document.form.type.value = "move" ;
	
	var displayName = encodeURI("$text.get("oabbs.common.lb.Move")") ;
	var varURL = "/UserFunctionAction.do?&selectName=bbsTopicMove&operation=$globals.getOP("OP_POPUP_SELECT")"
	//var varValue = window.showModalDialog(varURL+"&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,"","dialogWidth=750px;dialogHeight=430px") ;
	asyncbox.open({id:'Popdiv',title:"分级目录",url:"/UserFunctionAction.do?&selectName=bbsTopicMove&popupWin=Popdiv&operation=$globals.getOP("OP_POPUP_SELECT")&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,width:750,height:470});
	/*varValue = backValue;
	alert(varValue);
	if(typeof(varValue)=="undefined")return ;
	document.form.newTopicId.value = varValue.split(";")[0] ;
	document.form.submit() ;*/
}

function exePopdiv(returnValue){
	if(typeof(returnValue)=="undefined")return ;
	document.form.newTopicId.value = returnValue.split("#;#")[0] ;
	document.form.submit() ;
}


function seltimeChange(){
	document.form.submit();
}
function orderChange(){
	document.form.submit();
}
function elite(){
	if(document.form.elite.value=='true'){
		document.form.elite.value='false'; 
	}else{
		document.form.elite.value='true'; 
	}
	
	document.form.submit();
}
function del(){
	if(sureDel('forumId')){
		document.form.submit();
	}
}
function personInfo(){
	asyncbox.open({
　　　	id : 'personInfo',
		url : '/OABBSUserAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&type=user',
		cache : false,
		modal : false,
	 	title : '$text.get("oa.common.upd")$text.get("oa.bbs.specialismData")',
　　　	width : 580,
　　　	height : 205,
		btnsbar : jQuery.btn.OKCANCEL, 
　　　	callback : function(action,opener){
　　　　　	var t = this;
　　　　　	if(action == 'ok'){
　　　　　　　	if(!opener.saveInfo()){
					return false ;
				}
　　　　　	}
　　　	}
	});
}

function openSelect1(urlstr,displayName,obj){

	displayName=encodeURI(displayName) ;
	var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,winObj,"dialogWidth=730px;dialogHeight=450px"); 

	if(typeof(str)!="undefined"){
	var mutli=str.split("|"); 
	hid="";
	dis="";
	if(str.length>0){
		var len=mutli.length;
		if(len>1){len=len-1}
		for(j=0;j<len;j++){ 
			fs=mutli[j].split(";");
				dis=fs[4];
				hid=fs[1];
			if(hid.indexOf("@Sess:")>=0){
				document.getElementById(obj).value="";
			}else{
				document.getElementById(obj).value=dis;
				
			}
		}
	}else{
		document.getElementById(obj).value="";
	}
	}
}



function keywordQuery(){
	var keyword = $("#keyword");
	if(keyword.val().trim()=="标题搜索"){
		asyncbox.tips('标题不能为空','error');
        return false;
	}
		reSetValue();
		form.submit();
	
}


function showReplay(strUrl){
	window.location.href = strUrl ;
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
}

function showSearch(){
	if($('#highKeyword').val() == '关键字搜索'){
		$('#highKeyword').val("");
	}
	if($('#highSearch').css("display")== "none"){
	    $('#highSearch').show();
	    $("#searchType").val("true");
	}
	else{
	    $('#highSearch').hide();
	    $('#highSearch :input').val("");
	    $("#searchType").val("false");
	    
	}
}
function closeSearch(){
	$('#highSearch').hide();
}
function yenZheng(){
	var hightTitle=$("#forumTitle");
	var sender=$("#userName");
	var beginTime=$("#beginTime");
	var endTime=$("#endTime");
	var topicType=$("#topic");
	
	if((hightTitle.val() == null || hightTitle.val().trim()=="" ) &&
	   (sender.val()==null || sender.val().trim()==""  )&&
	   (beginTime.val()==null || beginTime.val().trim()=="" )&&
	   (endTime.val()==null || endTime.val().trim()==""  ) &&
	   (topicType.val()==null || topicType.val().trim()=="") ){
	     alert("查询条件为空，请输入后再查询");
	     return false;
	   }
	   return true;
}
function reSetValue(){
	$("#forumTitle").val('');
	$("#userName").val('');
	$("#beginTime").val('');
	$("#endTime").val('');
	$("#topic").val('');
}

function evaluate(){
	var datas =document.getElementById("dbData").value;
	newOpenSelectSearch(datas,"userName","createBy");
}
var theU;
function getTheURL(){
 		theU=window.location.href;
		//alert(theU.lastIndexOf("/"));
		theU=theU.substring(theU.lastIndexOf("/"));
		theU=theU.replace(/&/g,">");

}


//处理双击弹出框






function fillData(datas){
	if(typeof(datas)=="undefined") return ;
	var note = datas.split(";") ;
	$("#userName").val(note[1]);
	jQuery.close('Popdiv');
}


</script>
<style type="text/css">
.search_text2{ height:20px; line-height:20px;  border:1px solid #bbbbbb; vertical-align:middle;width:130px;background:#fff;}
</style>

</head>
<body onload="getTheURL()" >
#if("$!addTHhead" == "true")
	#parse("./././body2head.jsp")
#end
<form name="form" action="/OABBSForumQueryAction.do?#if($!searchSel&& $!searchSel=="yes")searchSel#{else}boweSearchSel#end=yes&addTHhead=$!addTHhead" method="post" >
 
<input type="hidden" id="userId" name="userId" value="$!BBSUser.userID"/>
<input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")"/>
<input type="hidden" name="type" value=""/>
<input type="hidden" name="topicId" value="$!topicId"/>
<input type="hidden" name="newTopicId" value=""/>
<input type="hidden" name="elite" value="$!forumSearchForm.elite"/>
<input type="hidden" name="topicType" value="$!forumSearchForm.topicType"/>
<input type="hidden" name="queryType" value="$!queryType"/>
<input type="hidden" name="searchSel" value="$!searchSel"/>

<input type="hidden" name="dbData" id="dbData" value="" onpropertyChange="javascript:if(this.value!=null)evaluate()"/>
<div class="framework">
<div class="TopTitle">
	<span><img src="/style/images/bbs/forumICON.gif"/></span>
	<span>$text.get("oa.bbs.innerBBS")  </span>
	<span style="margin-left:20px;">
		#if((!$!dayForumNone) ||$!dayForumNone!="none")
				<input  id ="keyword" style="width:140px;" name="keyword" type="text"  class="search_text" value="#if($!forumSearchForm.keyword!="")$!forumSearchForm.keyword#else标题搜索#end" onKeyDown="if(event.keyCode==13)return keywordQuery();" onblur="if(this.value=='')this.value='标题搜索';" 
				onfocus="if(this.value=='标题搜索'){this.value='';}this.select();"/><input type="button" class="search_button" style="cursor: pointer;" onclick="javascript:keywordQuery();"/>
				<button type="button" style="height: 24px; margin-left:-4px;" onclick="showSearch();">$text.get("common.lb.Advancequery")</button>
		#end
	</span>
	<div>
		
  		#if((!$!dayForumNone) ||$!dayForumNone!="none")
  		<button type="button" class="bu_02" onClick="javascript:location.href='/OABBSForumQueryAction.do?queryType=dayForum&addTHhead=$!addTHhead';">今日新贴</button>
  		#end
  		#if("$!queryType"=="" && $!searchSel!="yes")
  		#if($addScope || "$LoginBean.id"=="1" || "$!BBSTopic.bbsUserId"=="$!BBSUser.userID" || $BBSTopic.bbsUserId2==$BBSUser.userID)
	   	<button type="button" onClick="location.href='/OABBSForumAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&topicId=$!topicId&addTHhead=$!addTHhead'" class="bu_02">$text.get("oabbs.common.lb.newTheme")</button>
	   	<button type="button" onClick="location.href='/OABBSVoteAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&topicId=$!topicId&addTHhead=$!addTHhead'" class="bu_02">$text.get("oabbs.com.title.IssueVote")</button>
		#end
		#if($!attention=="attention")
		<button type="button" onClick="addDelMoudle('$!topicId');" id="moudle_$!topicId" class="bu_02" bg="add">$text.get("bbs.module.attention")$text.get("oa.bbs.bt.tblAttention")</button>
	  	<!-- <button type="button" onClick="location.href='/OABBSForumAction.do?operation=$globals.getOP("OP_QUERY")&topicId=$!topicId&type=Attention'" class="bu_02">$text.get("bbs.module.attention")$text.get("oa.bbs.bt.tblAttention")</button> -->
		#else
		<button type="button" onClick="addDelMoudle('$!topicId');" id="moudle_$!topicId" class="bu_02" bg="del">$text.get("cannel")$text.get("bbs.module.attention")</button>
	  	<!--<button type="button" onClick="location.href='/OABBSForumAction.do?operation=$globals.getOP("OP_QUERY")&topicId=$!topicId&type=DeleteAttention'" class="bu_02">$text.get("cannel")$text.get("bbs.module.attention")</button> -->
		#end
	
		#if($LoginBean.id=="1" || $BBSTopic.bbsUserId==$BBSUser.userID || $BBSTopic.bbsUserId2==$BBSUser.userID)
		<!--
		<button type="button" onClick="javascript:mdiwin('/UserFunctionAction.do?tableName=OABBSTopic&keyId=$!BBSTopic.id&operation=7&noback=true&noAuth=true','$text.get("oabbs.common.lb.boardManage")')" class="bu_02">$text.get("oabbs.common.lb.boardManage")</button>
		-->
	   	<button type="button" onClick="javascript:moveForum();" class="bu_02">$text.get("oabbs.common.lb.moveData")</button>
 	   		
		#end
	  	<!--<button type="button" onClick="javascript:personInfo();" class="bu_02">$text.get("oa.bbs.specialismData")</button>
		
		#if($!attention=="attention")
	  	<button type="button" onClick="location.href='/OABBSForumAction.do?operation=$globals.getOP("OP_QUERY")&topicId=$!topicId&type=Attention'" class="bu_02">$text.get("bbs.module.attention")$text.get("oa.bbs.bt.tblAttention")</button>
		#else
	  	<button type="button" onClick="location.href='/OABBSForumAction.do?operation=$globals.getOP("OP_QUERY")&topicId=$!topicId&type=DeleteAttention'" class="bu_02">$text.get("cannel")$text.get("bbs.module.attention")$text.get("oa.bbs.bt.tblAttention")</button>
		#end
  		-->
  		
  		#if($LoginBean.id=="1" || $BBSTopic.bbsUserId==$BBSUser.userID || $BBSTopic.bbsUserId2==$BBSUser.userID)
  		
  		 #if($LoginBean.getOperationMap().get("/UserFunctionQueryAction.do?tableName=OABBSTopic").update())
  		<button type="button" onClick="javascript:mdiwin('/UserFunctionAction.do?tableName=OABBSTopic&keyId=$!BBSTopic.id&operation=7&noback=true&noAuth=true','$text.get("oabbs.common.lb.boardManage")')" class="bu_02">$text.get("oabbs.common.lb.boardManage")</button>
  		 
  		 #elseif($LoginBean.getOperationMap().get("/UserFunctionQueryAction.do?tableName=OABBSTopic").query())
			<button type="button" onClick="javascript:mdiwin('/UserFunctionAction.do?tableName=OABBSTopic&keyId=$!BBSTopic.id&operation=5&noback=true&noAuth=true','$text.get("oabbs.common.lb.boardManage")')" class="bu_02">$text.get("oabbs.common.lb.boardManage")</button>
  		 #end
		<button type="button" onClick="javascript:del();" class="bu_01">$text.get("common.lb.del")</button>	
  		#end
  		#end	
  		#if(!$!dayForumNone  || $!dayForumNone =="none")
		<button type="button" onClick="location.href='/OABBSForumQueryAction.do?queryType=dayForum&timeTlag=one&addTHhead=$!addTHhead';" class="bu_011">今日新贴</button>
		<button type="button" onClick="location.href='/OABBSForumQueryAction.do?queryType=dayForum&timeTlag=three&addTHhead=$!addTHhead';" class="bu_011">三天内贴</button>
		<button type="button" onClick="location.href='/OABBSForumQueryAction.do?queryType=dayForum&timeTlag=zhou&addTHhead=$!addTHhead';" class="bu_011">本周内贴</button>
		<button type="button" onClick="location.href='/OABBSForumQueryAction.do?queryType=dayForum&timeTlag=month&addTHhead=$!addTHhead';" class="bu_011">本月内贴</button>	
		#end
  		<button type="button" onClick="location.href='/OABBSForumAction.do?displayList=$!displayList&addTHhead=$!addTHhead'" class="bu_01">$text.get("common.lb.back")</button>
		
	</div>
</div>
<div class="heart">
		<div class="heart_title" style="border-bottom:0px;">
			<span>$text.get("common.msg.currstation")：#if("$!parentName"=="")<a>$text.get("oabbs.com.lb.bbsHead") #else $!parentName #end</span>
			<span style="width:500px;float:right;text-align:right;padding-right:30px;font-weight:normal;">
				#if($topicType.size() > 0)
				<a href="javascript:document.form.topicType.value=''; document.form.submit();">$text.get("property.choose.item1")</a> &nbsp;
				#foreach($row in $topicType)
				<a href="javascript:document.form.topicType.value='$globals.get($row,1)'; document.form.submit();">$globals.get($row,1)</a> &nbsp;
				#end
				#end
			</span>
			
		</div>
		<div class="heart_content" id="heart_id2" style="border:1px solid #dedede;border-bottom:0px;padding-top:5px;">
<script type="text/javascript">
	var oDiv=document.getElementById("heart_id2");
	var sHeight=document.documentElement.clientHeight-110;
	oDiv.style.height=sHeight+"px";
</script>
		<table cellpadding="0" cellspacing="0">
		<thead>
			<tr>
			    <td width="20">
					<input type="checkbox" name="selAll" onClick="checkAll('forumId')">
				</td>
				#if("$!queryType"=="dayForum" && "$!searchSel"=="dayForum")
				 <td>全选</td> 
				#else
				<td style="float:left;">
				<ul style="height:100%;padding:0px;margin:0px;">
				<li style="float:left;padding-left:5px">		
				<script type="text/javascript">aioselect('seltime',[{value:'0',name:'$!text.get("oa.bbs.lb.viewallTime")'},{value:'1',name:'$!text.get("oa.bbs.lb.viewonedate")'},
						{value:'2',name:'$!text.get("oa.bbs.lb.view2date")'},{value:'3',name:'$!text.get("oa.bbs.lb.view1week")'},{value:'4',name:'$!text.get("oa.bbs.lb.view1m")'},
						{value:'5',name:'$!text.get("oa.bbs.lb.view3m")'}],'$!forumSearchForm.seltime','60','seltimeChange()');</script>	
				</li>
				<li style="float:left;padding-left:5px">
					<script type="text/javascript">aioselect('orderby',[{value:'0',name:'$!text.get("common.lb.defaultsort")'},{value:'1',name:'$!text.get("oa.bbs.lb.lastnew")'},
					{value:'2',name:'$!text.get("oa.bbs.lb.lastreplay")'},{value:'3',name:'$!text.get("oa.bbs.lb.hotgr")'}],'$!forumSearchForm.orderby','60','orderChange()');</script>
				</li>
				<li style="float:left;padding-left:5px">
				<span >|</span>&nbsp;&nbsp;&nbsp;<a href="javascript:elite()">#if("$!forumSearchForm.elite" == "true") $!text.get("property.choose.item1") #else $!text.get("oa.bbs.lb.addj")#end</a>
				</li>
				</ul>				
				</td>
				#end
				#if("$!queryType"=="dayForum")
				<td>版块</td>
				#end
				<td>$!text.get("oa.bbs.anuther")</td>
				<td>$!text.get("obbs.lb.lastRE")</td>
				<td class="align_center">$text.get("oa.bbs.revertto")数</td>
				<td class="align_center">$!text.get("oa.bbs.pointCount")</td>
			    <!-- <td class="align_center">$text.get("common.lb.operation")</td>  -->
			   </tr>
		</thead>
		<tbody>		
	 
		#foreach ($forum in $forumList)
			<tr #if($globals.isOddNumber($velocityCount)) style="background:#fff;"#end>
				<td width="20"> 
					<input type="checkbox" name="forumId" value="$!forum.id"/>
				</td >
				<td>
				<div class="oabbs_theme2" >
				<span class="vimg" style="width:17px">
				#if($forum.isSetTop =="1" )
				 <img src="/$globals.getStylePath()/images/oabbs/top.gif" style="cursor:default;"/>
				#else 
				<img src="/$globals.getStylePath()/images/oabbs/folder_old.gif" style="cursor:default;"/>				 
				#end	
				</span>			
				#if($forum.isElite =="1")
				<span class="vimg" style="width:17px">
				<img src="/$globals.getStylePath()/images/oabbs/digest_1.gif" style="cursor:default;"/>
				</span> 
				#end
				#if($forum.forumType =="vote")
				<span class="vimg" style="width:17px">
				<img src="/$globals.getStylePath()/images/oabbs/pollsmall.gif" style="cursor:default;"/>
				</span>
				#end
				&nbsp;
				<a href="javascript:callBack('$globals.get($row_sends,0)');">$globals.get($row_sends,1)</a>
				<a href="javascript:showReplay('/OABBSForumAction.do?operation=$globals.getOP("OP_SEND_PREPARE")&forumId=$!forum.id&topicId=$!forum.topic.id&searchSel=$!searchSel&addTHhead=$!addTHhead');" class="showFont">
				#if("$!forum.topicTyle" != "") <span style="font-weight:bold;color:#CC9966;font-size:12px;">[$!forum.topicTyle] </span> &nbsp; #end
				
				#if($!forum.topicName.length()>40)
					
					<font class="font13" title="$!forum.topicName">$!forum.topicName.substring(0,35)</font>...
				#else
					<font class="font13">$!forum.topicName</font></a>
				#end
				 	 &nbsp;
				#if($forum.pointCount>=$forum.topic.hotClick)
				 
				 <span class="vimg" style="width:17px">
					<img src="/style/images/bbs/hot_1.gif" style="width:21px;height:14px; cursor:default;"/>
				 </span>
				 
				#end
				
			
				#if($globals.isNews($forum.createTime,$forum.topic.newDay))
				 <span class="vimg" style="width:17px">
					<img src="/style/images/bbs/new.gif" style="width:21px;height:14px;cursor:default;"/>
				 </span>
				#end
				 </div>
			
	
				</td>
				#if("$!queryType"=="dayForum")
				<td width="120" style="line-height:15px;font-size:12px;">$!forum.topic.sortName</td>
				#end
				<td width="120" style="line-height:15px">
				#if("$!BBSTopic.isRealname" == "1") $!forum.bbsUser.fullName #else $!forum.bbsUser.nickName #end 
				 <br/>
				   <span style="font-size:10px;color:#666666"> $forum.createTime</span></div>
				</td>
				<td width="120" style="line-height:15px">
				#if("$!BBSTopic.isRealname" == "1") $!forum.lastUpdateBy.fullName #else $!forum.lastUpdateBy.nickName #end 
				<br/>
				   <span style="font-size:10px;color:#666666">#if($!forum.lastUpdateBy != "")  $forum.lastUpdateTime #end</span></div>
				</td>
			    <td width="80" class="align_center">
			 	$!forum.returnCount
				</td>
				<td width="80" class="align_center">
			 	$!forum.pointCount
				</td>
			<!--
	 			 #if($LoginBean.id=="1" || $BBSTopic.bbsUserId==$BBSUser.userID || $BBSTopic.bbsUserId2==$BBSUser.userID ||
				   $!forum.topic.bbsUserId==$LoginBean.id || $!forum.topic.bbsUserId2==$LoginBean.id
				    )
				
					<td width="120" class="align_center">
						
					 #if($forum.isElite == 0)
					 
					 <a href="/OABBSForumAction.do?operation=$globals.getOP("OP_UPDATE")&type=elite&paramValue=1&forumId=$forum.id&topicId=$!forum.topic.id&searchSel=$!searchSel">$text.get("oa.bbs.setasDistillate")</a>&nbsp; 
					 #else
					 <a href="/OABBSForumAction.do?operation=$globals.getOP("OP_UPDATE")&type=elite&paramValue=0&forumId=$forum.id&topicId=$!forum.topic.id&searchSel=$!searchSel">$text.get("oa.bbs.cancelDistillate")</a>&nbsp; 
					 #end
					 #if($forum.isSetTop == 0)
					 <a href="/OABBSForumAction.do?operation=$globals.getOP("OP_UPDATE")&type=setTop&paramValue=1&forumId=$forum.id&topicId=$!forum.topic.id&searchSel=$!searchSel">$text.get("oa.bbs.setTop") </a>  &nbsp; 
					 #else
					 <a href="/OABBSForumAction.do?operation=$globals.getOP("OP_UPDATE")&type=setTop&paramValue=0&forumId=$forum.id&topicId=$!forum.topic.id&searchSel=$!searchSel">$text.get("common.lb.cancel")$text.get("oa.bbs.setTop") </a>&nbsp;
					 #end
					</td>
				  #else
				  <td width="120" class="align_center" style="color:black;">
				    #if($forum.isElite == 0)
					 	$text.get("oa.bbs.setasDistillate") &nbsp; 
					 #else
						$text.get("oa.bbs.cancelDistillate")&nbsp; 
					 #end
					 #if($forum.isSetTop == 0)
					$text.get("oa.bbs.setTop") &nbsp; 
					 #else
					$text.get("common.lb.cancel")$text.get("oa.bbs.setTop") &nbsp;
					 #end
				 </td>
	 			 #end
	 			-->
			</tr>	
			#end
		  </tbody>
		</table>
			#if($forumList.size()==0)
				<div class="nodata">暂无相关信息</div>		
 			#end
	  </div>	
	  <div class="oabbs_foot2">
	  	<div class="listRange_pagebar"  style="margin-top:-5px;">  $!pageBar </div>
		<span>
			<img src="/$globals.getStylePath()/images/oabbs/pollsmall.gif"/>$text.get("oabbs.com.lb.Check")
		</span>
		<span>
			<img src="/$globals.getStylePath()/images/oabbs/top.gif"/>$text.get("oa.bbs.setTop")$text.get("oa.subjects")
		</span>
		<span>
			<img src="/$globals.getStylePath()/images/oabbs/digest_1.gif" width="21" height="14"/>$text.get("oa.bbs.distillate")$text.get("oa.subjects")
		</span>
	 </div>
</div>
</div>
</form>

<form name="hight_form" method="post" action="/OABBSForumQueryAction.do?queryType=dayForum&#if($!searchSel  && $!searchSel=="yes")searchSel #else boweSearchSel #end=yes" onsubmit="return yenZheng();">
	<input type="hidden" name="topicId" value="$!topicId"/> 
	<input type="hidden" name="searchSel" value="$!searchSel"/>
	<div id="highSearch" class="search_popup" style="display:none;width: 470px;top: 150px;left: 250px;height: #if($!searchSel && $!searchSel=="yes")185px; #else 150px;#end">
			<div id="Divtitle" style="cursor: move; width: 460px;"   >
				<span class="ico_4" ></span>高级查询
			</div>
			<li>
				<span>$text.get("oa.bbs.sendbbsTitle"):</span>
				<input  id="forumTitle" name="forumTitle" type="text"   value="$!forumSearchForm.forumTitle" class="search_text2"/>
				<input id="type" name="type" type="hidden" value="hightype"/>
				<input type="hidden" name="pageParam" value="" />
			</li>
			
			<li><span>$text.get("oa.bbs.sender"):</span> 
				<input name="createBy" id="createBy"  type="hidden" value="" />
				<input class="search_text2" name="userName" id="userName" size="13" onKeyDown="if(event.keyCode==13) subForm();" type="text" readonly="readonly" onClick="deptPopForAccount('userGroup','userName','createBy');" value="$!forumSearchForm.userName" />
				<img style="cursor:pointer;" src="/style1/images/St.gif" onClick="deptPopForAccount('userGroup','userName','createBy');" />	
			</li>
			
			<li><span>$text.get("scope.lb.tsscopeValue"):</span><input class="search_text2" type="text" name="beginTime" id="beginTime" onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);"  value="$!forumSearchForm.beginTime" /></li>
			<li><span>$text.get("oa.job.endtime"):</span><input class="search_text2" type="text" name="endTime" id="endTime"  onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);"  value="$!forumSearchForm.endTime" /></li>
			#if($!searchSel && $!searchSel=="yes")
				<li><span>$text.get("oa.bbs.sortName"):</span>
				<select name ="topic" id="topic" style="width:160px; ">
					#foreach($topic in $topicList)
					 #if($!globals.get($topic,5)==$!forumSearchForm.topic)
						 <option value="$!globals.get($topic,5)">$!globals.get($topic,0)</option>
					 #end
					#end
						<option value=""></option>
					#foreach($topic in $topicList)
						<option value="$!globals.get($topic,5)">$!globals.get($topic,0)</option>
						
					#end				 
				</select>
			</li>
			#end
			<li style="margin-top:5px;margin-left: 150px;">
				<button name="Submit" type="submit"  class="sb">$text.get("common.lb.query")</button>&nbsp;&nbsp;
				<button name="" type="button" class="sb" onclick="reSetValue()" >$text.get("common.lb.reset")</button>&nbsp;&nbsp;
				<button name="" type="button" class="sb" onclick="closeSearch()">$text.get("common.lb.close")</button>
				
			</li>
	  </div>
</form>

</body>
</html>

<script type="text/javascript">

var posX;
var posY;       
	fdiv = document.getElementById("highSearch");         
	document.getElementById("Divtitle").onmousedown=function(e){
	    if(!e) e = window.event;  //IE
	    posX = e.clientX - parseInt(fdiv.style.left);
	    posY = e.clientY - parseInt(fdiv.style.top);
	    document.onmousemove = mousemove;           
	}

document.onmouseup = function(){
    document.onmousemove = null;
}
function mousemove(ev){
    if(ev==null) ev = window.event;//IE
    fdiv.style.left = (ev.clientX - posX) + "px";
    fdiv.style.top = (ev.clientY - posY) + "px";
}

<!--
	lId = "$!request.getParameter("forumId")";
	if(lId != null){
		items = document.getElementsByName('forumId');
				for(ii = 0;ii<items.length;ii++){
					if(items[ii].value == lId){
						//items[ii].checked =true;
						items[ii].focus(); 
						break;
					}
				}
	}
	

//-->

</script>
