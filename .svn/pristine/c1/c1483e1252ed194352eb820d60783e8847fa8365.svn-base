<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="/style/css/forum.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<link type="text/css" href="/style/css/base_button.css" rel="stylesheet" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#else
<title>$text.get("oa.bbs.indexPage")</title>
#end
<script type="text/javascript">

function personInfo(){
	asyncbox.open({
　　　	id : 'personInfo',
		url : '/OABBSUserAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&type=user',
		cache : false,modal : false,
	 	title : '$text.get("oa.common.upd")$text.get("oa.bbs.specialismData")',
　　　	width : 580,height : 225,
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

function dealAsyn(){
	jQuery.close('personInfo');
}

function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
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

function keywordQuery(){
	var keyword = $("#keyword");
 
	if(keyword.val().trim()=="标题搜索" ){
		asyncbox.tips('标题不能为空','error');
        return false;
	} 
		form.submit();	 
}

function yenZheng(){
	var hightTitle=$("#forumTitle");
	var sender=$("#userName");
	var beginTime=$("#beginTime");
	var endTime=$("#endTime");
	var topicType=$("#topic");
	var keyword = $("#keyword");
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

function fillData(datas){
	newOpenSelectSearch(datas,fieldNames,fieldNIds);
	jQuery.close('Popdiv');
}

</script>
<style type="text/css">
button{
	margin-right:5px;
	height:22px;
	line-height:16px;
	vertical-align:middle;
	font-size:12px;
	cursor:pointer;
	width:0;
	width:auto;
	padding:2px 3px 0 3px;
	overflow:visible;
	background:url(/style/images/aiobg.gif) repeat-x left top;
}

</style>
</head>
<body>
#if("$!addTHhead" == "true")
	#parse("./././body2head.jsp")
#end
<div class="framework">
<form name="form" action="/OABBSForumQueryAction.do?queryType=dayForum&searchSel=yes&addTHhead=$!addTHhead" method="post" >
	<div class="TopTitle">
		<span><img src="/style/images/bbs/forumICON.gif" /></span>
		<span>$!text.get("oa.bbs.indexPage")</span>
		<span style="margin-left:20px;"><input id ="keyword" style="width: 150px;" name="keyword" type="text"  class="search_text" value="#if($!forumSearchForm.forumTitle!="")$!forumSearchForm.forumTitle#else标题搜索#end" onKeyDown="if(event.keyCode==13)return keywordQuery();" onblur="if(this.value=='')this.value='标题搜索';" 
			onfocus="if(this.value=='标题搜索'){this.value='';}this.select();"/><input type="button" class="search_button" style="cursor: pointer;" onclick="javascript:keywordQuery();"/>
			<button type="button" style="height: 24px; margin-left:-4px;" onclick="showSearch();">$text.get("common.lb.Advancequery")</button></span>
			
		<div>
			<!-- <input type="button" class="bu_02" value="列表显示" /> -->
			<input type="button" class="bu_02" onClick="javascript:location.href='/OABBSForumQueryAction.do?queryType=dayForum&timeTlag=month&addTHhead=$!addTHhead';" value="近日新贴" />
			<!--<input type="button" class="bu_02" onClick="personInfo();" value="$text.get("oa.bbs.specialismData")" /> -->		
		</div>					
	</div>
</form>
	<div class="heart"  id="heart_id" style="overflow-x:hidden;overflow-y:auto;">
<script type="text/javascript">
	var oDiv=document.getElementById("heart_id");
	var sHeight=document.documentElement.clientHeight-55;
	oDiv.style.height=sHeight+"px";
</script>
		#foreach($en in $globals.getEnumerationItems("OABBSList"))
		#set($foundbbs="false")
    	#foreach ($topic in $topicList)
			#if("$!globals.get($topic,7)" == $en.value)
				#set($foundbbs="true")
			#end
		#end
		#if($foundbbs=="true")
		<div class="heart_title" style="width:99%;"><span>$en.name</span></div>
		<ul class="heart_list" style="width:99%;">
			#set($topicIndex=0)
			#foreach ($topic in $topicList)
			#if("$!globals.get($topic,7)" == $en.value)
			#set($topicIndex=$topicIndex+1)
			#if($topicIndex>6)
			#set($topicIndex=1)
			#end
			<li>
				#set($forum=$!mapForum.get($!globals.get($topic,4)))
				<div class="listIcon"><img style="width:70px;height:66px;" #if("$!globals.get($topic,8)"=="")src="/style/images/bbs/list_icon_00${topicIndex}.gif" #else src="/ReadFile.jpg?fileName=$globals.urlEncode($!globals.get($topic,8))&tempFile=false&type=PIC&tableName=OABBSTopic&addTHhead=$!addTHhead" #end onclick="location.href='/OABBSForumQueryAction.do?src=menu&topicId=$!globals.get($topic,5)&addTHhead=$!addTHhead'"/></div>
				<div class="listMatter"><B><a href="/OABBSForumQueryAction.do?src=menu&topicId=$!globals.get($topic,5)&addTHhead=$!addTHhead">$!globals.get($topic,0)</B></a><br />
					$!text.get("oa.bbs.sortUser"):$!globals.get($topic,1)&nbsp;$!globals.get($topic,6)<br />
					$!text.get("oa.bbs.subjectCounts"):$!globals.get($topic,3)<br />
					#set($dayCount=0)
					#if($!todayForumCount.get($!globals.get($topic,5)))
						#set($dayCount = $!todayForumCount.get($!globals.get($topic,5)))
					#end
					#if("$dayCount"=="0")
					<span style="color:#8d8d8d;">$!text.get("obbs.lb.newsendCount")：$!dayCount</span>
					#else
					<span>$!text.get("obbs.lb.newsendCount")：$!dayCount</span>
					#end
					<!--  &nbsp;&nbsp;最新：$globals.substring($!forum.lastUpdateTime,10)-->
				</div>
			</li>
			#end
			#end
		</ul>
		#end
		#end
	</div>
</div>

<form name="hight_form" method="post" action="/OABBSForumQueryAction.do?queryType=dayForum&searchSel=yes&addTHhead=$!addTHhead" onsubmit="return yenZheng();">
	<div id="highSearch" class="search_popup" style="display:none;width: 470px;height: 180px;top: 150px;left: 250px;">
			<div id="Divtitle" style="cursor: move;width: 460px;">
				<span class="ico_4" ></span>高级查询
			</div>
			<li>
				<span>$text.get("oa.bbs.sendbbsTitle"):</span>
				<input  id="forumTitle" name="forumTitle" type="text"   value="" />
				<input id="type" name="type" type="hidden" value="hightype"/>
				<input type="hidden" name="pageParam" value="" />
			</li>
			
			<li><span>$text.get("oa.bbs.sender"):</span> 
				<input name="createBy" id="createBy"  type="hidden" value="" />
				<input name="userName" id="userName" size="13" onKeyDown="if(event.keyCode==13) subForm();" type="text" readonly="readonly" onClick="deptPopForAccount('userGroup','userName','createBy');" value="" />
				<img style="cursor:pointer;" src="/style1/images/St.gif" onClick="deptPopForAccount('userGroup','userName','createBy');" />	
			</li> 
			
			<li><span>$text.get("scope.lb.tsscopeValue"):</span><input type="text" id="beginTime" name="beginTime"  onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);"  value="" /></li>
			<li><span>$text.get("oa.job.endtime"):</span><input type="text" id="endTime" name="endTime"  onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);"  value="" /></li>
			<li><span>$text.get("oa.bbs.sortName"):</span>
				<select id="topic" name ="topic" style="width:160px;">
						<option value="">
					#foreach($topic in $topicList)
						<option value="$!globals.get($topic,5)">$!globals.get($topic,0)</option>
					#end			 
				</select>
			</li>
			<li style="margin-top: 30px;margin-left: -80px;">
				<button name="Submit" type="submit"  class="sb">$text.get("common.lb.query")</button>&nbsp;&nbsp;
				<button name="" type="button" class="sb" onclick="reSetValue()">$text.get("common.lb.reset")</button>&nbsp;&nbsp;
				<button name="" type="button" class="sb" onclick="closeSearch()">$text.get("common.lb.close")</button>
				
			</li>
	  </div>
	  	<script language="javascript">
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
</script>
</form>
</body>
</html>