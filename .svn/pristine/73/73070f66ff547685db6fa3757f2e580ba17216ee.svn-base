<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<meta name="renderer" content="webkit">	
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link type="text/css" rel="stylesheet" href="/style/css/plan.css" />
	<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
	<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
	<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/CRselectBoxList.css" />
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script type="text/javascript" src="/js/jquery.jerichotab.js"></script>
	<script type="text/javascript" src="/js/function.js"></script>
	<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
	<script type="text/javascript" src="$globals.js("/js/workPlan.vjs","",$text)"></script>

	
	<script type="text/javascript" src="/js/aioselect.js"></script>
	<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
	<script type="text/javascript" src="/js/public_utils.js"></script>
	<script type="text/javascript">
	/**
	*	打开即时消息窗口
	*/
	function msgCommunicate(empId,empName){
		mdiwin('/MessageQueryAction.do?src=menu','即时消息');
	 	//openMsgDialog("person",empId,empName) ;
	}

	function openMsgDialog(operType,sendId,sendName){
		showreModule('$text.get("oa.mydesk.news")','/MessageQueryAction.do?src=menu') ;
		var activeIndex = jQuery.fn.jerichoTab.activeIndex ;
		var msg = 'window.frames["jerichotabiframe_'+activeIndex+'"].openDialog("'+operType+'","'+sendId+'","'+sendName+'")' ;
		setTimeout(msg,500);
		/*var adNum = $("#messageId div") ;
		if(adNum.html()!="" && parseInt(adNum.html())>0){
			if(parseInt(adNum.html())==1){
				$("#messageId").html("") ;
				//$("#messageId").parent().css("width","37px");
			}else{
				adNum.html(parseInt(adNum.html())-1) ;
			}
		}
		$("#msgList").hide();
		$("#show"+sendId).remove() ;*/
	}
	function showreModule(title,strUrl){			
		jQuery.fn.jerichoTab.addTab({
			title: title,
			closeable: true,
			tabFirer:strUrl,
			data: {
				dataType: 'iframe',
				dataLink: strUrl
			}
		}).loadData();	
	}
function addPlan(){
	var asybox = parent.asyncbox ;
	if(typeof(parent.asyncbox)=="undefined"  || typeof(parent.openMenu)!="undefined"){
		asybox = asyncbox ;
	}
	var height = 490;
	if($(document).height()<400){height=360}
	asybox.open({
　　　	id : 'planId',
		url : '/OAWorkPlanAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&userId=$strUserId&planType=$planType&strDate=$strDate&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus',
		cache : false,
		modal : true,
	 	title : '写工作计划',
　　　	width : 970,
　　　	height : height,
		btnsbar : [{
		    text   : '保存新增',
		    action : 'saveNew'
		},{
		    text   : '保 存',
		    action : 'save'
		}].concat(jQuery.btn.CANCEL),
		callback : function(action,iframe){
	　　　　	if(action == 'save'){
	　　　　		if(!asybox.opener("planId").beforSubmit()){
					return false ;
				}
				alert("添加成功") ;
				window.location.reload() ;
	　　　　	}else if(action == 'saveNew'){
				if(!asybox.opener("planId").beforSubmit("addNew")){
					return false ;
				}
				return false ;
				window.location.reload() ;
			}else{
				window.location.reload() ;
			}
	　　}
	});
}

function updatePlan(strUrl){
	var asybox = parent.asyncbox ;
	if(typeof(parent.asyncbox)=="undefined"  || typeof(parent.openMenu)!="undefined"){
		asybox = asyncbox ;
	}
	var height = 490;
	if($(document).height()<400){height=360}
	asybox.open({
　　　	id : 'planId',
		url : strUrl,
		cache : false,
		modal : false,
	 	title : '修改工作计划',
　　　	width : 970,
　　　	height : height,
		btnsbar : [{
		    text   : '保 存',
		    action : 'save'
		}].concat(jQuery.btn.CANCEL),
		callback : function(action,iframe){
	　　　　	if(action == 'save'){
	　　　　		if(!asybox.opener("planId").beforSubmit()){
					return false ;
				}
				alert("修改成功") ;
				window.location.reload() ;
	　　　　	}
	　　}
	});
}

function addSummar(strUrl){
	var asybox = parent.asyncbox ;
	if(typeof(parent.asyncbox)=="undefined"  || typeof(parent.openMenu)!="undefined"){
		asybox = asyncbox ;
	}
	var height = 490;
	if($(document).height()<400){height=360}
	asybox.open({
　　　	id : 'planId',
		url : strUrl,
		cache : false,
		modal : false,
	 	title : '计划总结',
　　　	width : 970,
　　　	height : height,
		btnsbar : [{
		    text   : '保 存',
		    action : 'save'
		}].concat(jQuery.btn.CANCEL),
		callback : function(action,iframe){
	　　　　	if(action == 'save'){
	　　　　		if(!asybox.opener("planId").beforSubmit()){
					return false ;
				}
				alert("添加成功") ;
				window.location.reload() ;
	　　　　	}
	　　}
	});
}

function closeCommit(){
	document.getElementById("massage_box").style.display="none";
}

function commit(planId,commitType,planEmployeeId,planTitle,commitId,flag){
	if(typeof(commitId)!="undefined"){
		document.commitform.commitId.value= commitId;
	}
	if(flag=="DP"){
		document.getElementById("FlagName").innerHTML="点评计划";
	}else if(flag=="HF"){
		document.getElementById("FlagName").innerHTML="回复点评";
	}else if(flag=="DF"){
		document.getElementById("FlagName").innerHTML="点评总结";
	}else{
		document.getElementById("FlagName").innerHTML="点评计划";
	}
	document.commitform.commitType.value= commitType;
	document.commitform.planId.value= planId;
	document.commitform.planEmployeeId.value= planEmployeeId;
	document.commitform.planTitle.value= planTitle;
	document.getElementById("massage_box").style.display="block";
	jQuery("#commitName").focus() ;
}

function commitSubmit(commitId){
	if(document.commitform.commit.value == ""){
		alert("$text.get("oa.workplan.comment")$text.get("common.msg.fileFlow.NoContent")");
		return;
	}
	/*if (getStringLength(document.commitform.commit.value)>1000)
	{
		alert("$text.get("oa.workplan.comment")$!text.get('common.validate.error.longer')1000$!text.get('common.excl')\n $!text.get('common.charDesc')")
		focusItem(element);
		return false;
	}*/
	document.commitform.submit();
}
function alertSet(relationId,planTitle){
	var urls=encodeURIComponent('/OAWorkPlanAction.do?operation=4&planType=day&flagAdvice=advice&winCurIndex=$winCurIndex&noback=true&nochangeTop=true&keyId='+relationId);
	var typestr=encodeURIComponent('$text.get("com.oa.plan.alert")'+planTitle);
	var title=encodeURIComponent('$text.get("com.oa.plan.name")');
	var date= new Date();
	var nochangeTop = nochangeTop;
	var url = "/UtilServlet?operation=alertDetail&relationId="+relationId+"&falg=true"+"&title="+title+"&typestr="+typestr+"&urls="+urls+"&date="+date;
	asyncbox.open({
		id : 'dealdiv',
　　　   	url : url,
		cache : false,
		modal : false,		
	 	title : '$text.get("workflow.msg.warmsetting")',
　　 　	width : 560,
　　 　	height : 460,
 		 top :5,
	    callback : function(action,opener){
		    if(action == 'ok'){ 
		    	opener.checkAlertSet();
		    	return false;
			}
　　      }
 　 });
}
function refreshIframe(){
	jQuery.close('dealdiv');
}


function resizeImg(){
	scrwidth = (document.documentElement.clientWidth) - 200;
	for(var i=0; i<document.images.length; i++) {
		var img = document.images[i];
		if(img.width > scrwidth){
			img.width = scrwidth;
		}
   }	
}

function showOrHide(strId){
	var str = jQuery("#"+strId)
	if(str.css("display") == "block"){
		str.hide();
	}else{
		str.show();
	}
}
function existLeftFrame(){
	if(typeof(window.parent.frames["planList"])=="undefined"){
		$("#topTitle").hide() ;
	}
}
function dingWei(){
		var newsId="$!dingWei";
		if(newsId!= ''){
			window.location.href="#"+newsId;
		}
}

function keywordSearch(){
	var keywordVal = $("#keyWord").val();
	if(containSpecial(keywordVal)){
		alert("包含特殊字符！") ;
		return false ;
	}
	if(keywordVal=="关键字搜索" || keywordVal.trim()==""){
		alert("请输入关键字！","搜索提示") ;
		return false ;
	}
	var planType="";
	if("$!planType"=="week"){
		planType="week";
	}
	if("$!planType"=="month"){
		planType="month";
	}
	window.location.href = "/OAWorkPlanAction.do?operation=4&associateId=$!associateId&planType="+planType+"&userId=$!strUserId&queryType=advance&keyWord="+encodeURI(keywordVal) ;
}
function containSpecial( s ){      
	var txt=new RegExp("[\\`,\\~,\\!,\\@,\#,\\$,\\%,\\^,\\+,\\*,\\&,\\\\,\\/,\\?,\\|,\\:,\\.,\\<,\\>,\\{,\\},\\(,\\),\\',\\;,\\=,\"]");  
	return ( txt.test(s) );      
}
function selectOver(){
	var value=$("#_view").val();
	window.location="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$strDate&keyId=$!keyId&opType=overThing&planStatus="+value;
}
function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}

//弹出框返回函数处理





function dealAsyn(){
	if(jQuery.exist('dealdiv')){
		jQuery.close('dealdiv');
	}else{
		jQuery.close('planId');
		window.location.reload();
	}
}

//table超出处理
function tableWidth(){
	$(".data_01 table").attr("width","100%").css("border-collapse","collapse").css("text-align","center").css("width","100%");
	$(".data_01 table tr td").each(function(){
		$(this).css("border","1px #000 solid");
	});
	$("div.data_01").find("p span").each(function(){
		$(this).css("white-space","normal");	
	});
	
	$(".data_04 table").css("border-collapse","collapse").css("width","100%");
	$(".data_04 table tr td").each(function(){
		$(this).css("border","1px #000 solid");
	});
	$("div.data_04").find("p span").each(function(){
		$(this).css("white-space","normal");	
	});
}

function mdiwin(url,title){
	if(typeof(window.parent.frames["f_mainFrame"])=="undefined"){
		window.open(url);
	}else{
		top.mdiwin(url,title);
	}
}
</script>
		<style type="text/css">
	.search_button2{
		background:url(/style/images/button_bg.gif) no-repeat center;
		width:60px;
		height:24px; 
		border:0px; 
		font-size:12px;
		text-align:center; 
		margin:0px; 
		padding:0px 0px 0px 0px;
		cursor:pointer;
   }
  .CRselectBox a.CRselectValue {
		font-size:12px;
	}
	.search_button{ height:24px; vertical-align:middle; border:0px; border-top:1px solid #bbbbbb; border-right:1px solid #bbbbbb; border-bottom:1px solid #bbbbbb; width:20px; background:url(/style/images/bbs/search.gif) center}
	span .data_02_pp{background:url(/style/images/plan/pp_01.gif);-webkit-transition:all 0.2s ease-in; opacity:0; font-size:1px; margin:1px 0px 0px 2px; width:14px; height:11px; float:left;}
	span .data_02_pp:hover{background:url(/style/images/plan/pp_02.gif);}
	.data_02 span:hover .data_02_del,.data_02 span:hover .data_02_pp{opacity:1;}
	.data_02_del{ opacity:0;-webkit-transition:all 0.2s ease-in;}
	
	
	.TopTitle .CRselectBox .CRselectBoxOptions{width:100px;}
	.data_03 .data_02_pp{background:url(/style/images/plan/pp_02.gif); font-size:1px; margin:1px 0px 0px 2px; width:14px; height:11px; float:left;}
	.data_02{height:100%;}
		
	.data_02 div{display:inline-block;}
	.data_02 .TypeDiv{width:100%;}
	.data_02 span{display:inline-block;margin:10px 5px 0 0;border:1px #a1a1a1 dashed;padding:5px;}
	.data_02 span:hover{border:1px #000 solid;}
	

	.data_04_p{line-height:21px;}
	.data_03 ul div li div{clear:both;}
	/*
	.data_02_del{-webkit-transform:rotate(0deg);-webkit-transition:all 0.5s ease-in;}
	
	.data_02_del:hover{-webkit-transform:rotate(360deg);}
	*/
</style>
	</head>
	<body onload="resizeImg();existLeftFrame();dingWei();tableWidth();">
		<input type="hidden" value="$planType" />
		#if("$flagAdvice"!="advice")
		<div class="TopTitle">
			#if("$strUserId"=="$LoginBean.id")
			#if($$LoginBean.getOperationMap().get("/OAWorkPlanAction.do").add())
				
			<span><button class="bu_02_i_n" type="button"
					onclick="javascript:addPlan();">
					写计划



				</button>
			</span> #end #end 
			<ul>
			#if("$!planType" != "event" && "$!keyId" == "")
			$!globals.get($!AssItemCount,0)
			<li><a href="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$strDate&keyId=$!keyId">#if("$!assName"=="" && "$!associateId"=="")<b>[$!strUserName$text.get("oa.bbs.plans")]</b>#else [$!strUserName$text.get("oa.bbs.plans")] #end</a></li>
			#set($flag=0)
			#if("$strUserId"=="$LoginBean.id")
				#foreach($row in $existAssociate)	
					#if( "$!row.isEmployee" == "1")
						<li><a href="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$strDate&associateId=$row.id&keyId=$!keyId&assName=$!globals.encode($!row.name)">#if("$!assName"=="$!row.name" || "$!associateId"=="$row.id")<b>[${row.name}$text.get("oa.bbs.plan")]</b>  #else[${row.name}$text.get("oa.bbs.plan")]#end</a></li>
						<li style="margin:0px;"><span style="margin-left:2px;margin-top:-2px;color:red;">$!AssItemCount.get($!flag)</span></li>
						#set($flag=$!flag+1)
					#end
				#end
			#end
			<li><a href="javascript:opencalWindow('/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=day&strDate=$strDate&opType=calendar')">[$text.get("workplan.lb.calenview")]</a></li>
	#end
	</ul>
	<!-- 
	<ul>
	#if("$!planType" != "event" && "$!keyId" == "")
		<li style="width:220px;"><a href="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$strDate&keyId=$!keyId">[$!strUserName <b style="color:#316bcc;">$!globals.getDate($!beginDate) - $!globals.getDate($!endDate)</b> $text.get("oa.bbs.plans")]</a></li>
		#foreach($row in $associate)	
			#if( "$!row.isEmployee" == "1")
				<li><a href="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$strDate&associateId=$row.id&keyId=$!keyId">[${row.name}$text.get("oa.bbs.plan")]</a></li>
			#end
		#end
	#end
	</ul>
	 -->
	 #if($$LoginBean.getOperationMap().get("/OAWorkPlanAction.do").query())	
	 #if("$!dsFlag" != "dsflag")			
	 <div><button class="search_button2" type="button" onclick="showSearch();">高级查询</button></div>
	<!-- <div><input type="text" class="search_text" id="keyWord" name="keyWord" #if($!keyWord!="") value="$!keyWord" #else value="关键字搜索" #end onKeyDown="if(event.keyCode==13) keywordSearch();" onblur="if(this.value=='')this.value='关键字搜索';" onfocus="if(this.value=='关键字搜索'){this.value='';}this.select();"/></div> -->
	<div><input type="text" class="search_text" id="keyWord" name="keyWord" #if($!keyWord!="") value='$globals.revertTextCode3("$!keyWord")' #else value="关键字搜索" #end onKeyDown="if(event.keyCode==13) keywordSearch();" onblur="if(this.value=='')this.value='关键字搜索';" onfocus="if(this.value=='关键字搜索'){this.value='';}this.select();"/><input type="button" class="search_button" style="cursor: pointer;" onclick="javascript:keywordSearch();"/></div>  
 	<div style="margin-right:5px;margin-top:3px;">
 	<span style="font-weight: normal;">总结状态：</span>
	 <script type="text/javascript">
	  #set($enums=$globals.getEnumerationItems("OverThing"))	
			#if($enums.size()<=10)		
				aioselect('$!globals.get($row,1)_view',[
					#foreach($erow in $enums)
						#if($velocityCount==$enums.size())
							{value:'$!erow.value',name:'$erow.name'}
						#else
							{value:'$!erow.value',name:'$erow.name'},
						#end
					#end
				],'$!planStatus','100','selectOver()');
			#end
	 </script>
	</div>
	#end
	#end
</div>
#end
<form name="commitform" method="post"  action="/OAWorkPlanAction.do"  >
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")"/>
<input type="hidden" name="opType" value="commit"/>
<input type="hidden" name="planType" value="$planType" /> 
<input type="hidden" name="strUserId" value="$strUserId" /> 
<input type="hidden" name="commitType" value="" /> 
<input type="hidden" name="strDate" value="$strDate" /> 
<input type="hidden" name="planId" value="" />
<input type="hidden" name="keyId" value="$!keyId" />
<input type="hidden" name="commitId" value="" />
<input type="hidden" name="planEmployeeId" value="" />
<input type="hidden" name="planTitle" value="" />
<input type="hidden" name="score" value='$!score'/>
<input type="hidden" name="planStatus" value='$!planStatus'/>
<input type="hidden" name="winCurIndex" value='$!winCurIndex'/>
<input type="hidden" name="assName" value="$!assName"/>
<input type="hidden"  name="associateId" value="$!associateId"/>
<input type="hidden" name="flagAdvice" value="$!flagAdvice"/>
<input type="hidden" id="flagId" name="flagId" value="$!flagId"/>
<div id="dataId" style="float:left;overflow:hidden;overflow-y:auto;width:100%;position:relative;margin:5px 0 0 0;">
<script type="text/javascript">
var oDiv=document.getElementById("dataId");
var sHeight=0;
if(typeof(window.parent.frames["planList"])!="undefined"){
	sHeight= parent.document.documentElement.clientHeight-55;
}else{
	sHeight=document.documentElement.clientHeight-55;
}
oDiv.style.height=sHeight+"px";
</script>

#if("$!weekPlan.size()" == "0" && "$!planType" !="event"  && "$!score" =="" && $!$dayMap.keySet().size()==0)
<div class="data" style="padding:100px 0px 100px 0px;">
	#if($!keyWord.length()>0 || $!queryType.length()>0 || $!planStatus.length()>0)
	<div class="data_00">
		<div class="data_00">#if("$strUserId"=="$LoginBean.id" && "$!associateId"=="")<a href="javascript:addPlan();"></a>#end</div>
		很抱歉，没有找到与您条件匹配的数据!
	</div>
	#else
	 <div class="data_00">#if("$strUserId"=="$LoginBean.id" && "$!associateId"=="")<a  href="javascript:addPlan();"></a>#end</div>
	<div class="data_00">
		#if("$!planType"=="day")
			$!strUserName ，今天还没有#if("$!associateId" == "")写#else$!{assName}#end工作计划哦



		#elseif("$!planType"=="week")
			$!strUserName ，本周还没有#if("$!associateId" == "")写#else$!{assName}#end工作计划哦



		#else
			$!strUserName ，本月还没有#if("$!associateId" == "")写#else$!{assName}#end工作计划哦



		#end
	</div>
	#end
</div>
#else
#foreach($planrow in $weekPlan)

<div class="data" id="dataId" style="border:1px solid #B4B4B4;">
	<div class="data_top"><!-- style="cursor: pointer;" onclick="showOrHide('$planrow.id');" -->
		<span title="$!planrow.title" style="background:url(/style/images/plan/column_a.gif) no-repeat left center;">
		#if("$!planrow.planType"=="week")
		[周计划]
		#elseif("$!planrow.planType"=="day")
		[日计划]
		#elseif("$!planrow.planType"=="month")
		[月计划]
		#end
		#if($!planrow.title.length()>60) $!planrow.title.substring(0,60)... #else $!planrow.title #end</span>
		<div>
			<div style="margin-top:7px;">
				<a href='javascript:alertSet("$planrow.id","$globals.revertTextCode3("$!planrow.title")");'><img src="/style/images/plan/time.gif" title="$text.get('mywork.lb.checkWarm')"/> </a>
				#if($planrow.employeeID==$LoginBean.id)
					#if($LoginBean.operationMap.get("/OAWorkPlanAction.do").update())
						<a href="javascript:updatePlan('/OAWorkPlanAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&planSum=plan&keyId=$!keyId&planId=$planrow.id&userId=$strUserId&planType=$planrow.planType&strDate=$strDate&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus');" title="$text.get("common.lb.update")"><img src="/style/images/plan/ti_02.gif" /></a>
					#end
					#if($LoginBean.operationMap.get("/OAWorkPlanAction.do").delete())
						<!--<a href="javascript:if(confirm('$text.get("common.msg.confirmDel")')){window.location.href='/OAWorkPlanAction.do?operation=$globals.getOP("OP_DELETE")&keyId=$planrow.id&userId=$strUserId&planType=$planType&strDate=$strDate&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus';#if("$!windowOpen"=="true")window.close()#end}"  title="$text.get("common.lb.del")"><img src="/style/images/plan/ti_04.gif"/></a>
						  -->
						  <a href="javascript:delPlan();" title='$text.get("common.lb.del")'><img src="/style/images/plan/ti_04.gif"/></a>
					#end
				#end
			</div>
		</div>
			<script type="text/javascript">
			function delPlan(){
				if(confirm('$text.get("common.msg.confirmDel")')){	
					//window.location.href='/OAWorkPlanAction.do?operation=$globals.getOP("OP_DELETE")&keyId=$planrow.id&userId=$strUserId&planType=day&strDate=$strDate&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus';				
					commitform.action = '/OAWorkPlanAction.do?operation=$globals.getOP("OP_DELETE")&keyId=$planrow.id&userId=$strUserId&planType=day&strDate=$strDate&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus';
					commitform.submit();									
					//parent.window.close();					 
				}
			}
			</script>
	</div>
	<div id="$planrow.id" style="display:block;">
	<div class="data_01">
		<p>$planrow.content</p>
		<span>$!planrow.createTime</span>
	</div>
	#if($!assItem.get($planrow.id)!="")
	
	#foreach($row in $associate)
	#set( $monkey = 0 )
	<div class="data_02">
	#foreach($item in $assItem.get($planrow.id)) 
		#if($globals.get($item,1) == $row.id && $globals.get($item,4)==$planrow.id)
		#set( $monkey = 1 )				
		#end
		#end
		#if($monkey == 1)	
		<div class="TypeDiv" >$row.name：</div>	
		#end
		#foreach($item in $assItem.get($planrow.id)) 
		#if($globals.get($item,1) == $row.id && $globals.get($item,4)==$planrow.id)
		
			<span id="${planrow.id}_${row.id}_$globals.get($item,2)">
				#if("$!planrow.employeeID"!="$LoginBean.id")
				<a></a>
				#else
				<a href="javascript:delAssoicate('$planrow.id','${row.id}','$globals.get($item,2)')" class="data_02_del"></a>
				#end
				<div title="$globals.get($item,3)" style="cursor:pointer;" #if($row.linkAddress!="") onclick="openLink('$globals.get($item,2)','$row.linkAddress')" #end>#if($globals.get($item,3).length() >10) $globals.get($item,3).substring(0,10) #else $globals.get($item,3) #end</div>
				#if($row.isEmployee=="1" and $globals.get($item,2)!=$LoginBean.id)<a href="javascript:msgComm('$globals.get($item,2)','$globals.get($item,3)')" class="data_02_pp"></a>#end
			</span>
		#end
		#end
	</div>
	#end
	#end
	#if($planrow.attach != "")
	<div class="data_02">
		<div class="TypeDiv">附&nbsp;&nbsp;&nbsp;&nbsp;件：</div><!--<a href="#" class="Ic_006"></a>-->
		#foreach ($str in $globals.strSplit($planrow.attach,';'))
		<span><a href="/ReadFile?tempFile=path&path=/plan/&fileName=$!globals.encode($str)" target="_blank"><div>$str</div></a></span>
		#end
	</div>
	#end
	<div class="data_01_bu">
			<div><a class="className" href="javascript:commit('$planrow.id','0','$planrow.employeeID','$!globals.encodeHTML2($planrow.title)','DP')" title="$text.get("oa.workplan.comment")计划"><img src="/style/images/plan/ti_03.gif"/></a></div>
			#if($!planrow.employeeID == "$LoginBean.id" && "$!planrow.summary"=="") 
				<input type="button" onclick="addSummar('/OAWorkPlanAction.do?operation=$globals.getOP("OP_ADD")&opType=summaryPrepare&planSum=plan&keyId=$!keyId&planId=$planrow.id&userId=$strUserId&planType=$planrow.planType&strDate=$strDate&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus');" class="bu_02_i_n" value="$text.get("oa.planSummar.add")" />
			#end
		</div>
	#if($commits.size()>0)
	<div class="data_03">
		<ul style="padding:0;">
			#foreach($comm in $commits)
			#if("$globals.get($comm,2)"=="0" && "$globals.get($comm,1)"==$planrow.id)
			<div #if($!commitMap.get($globals.get($comm,0))=="")style="border-bottom:1px dashed #aaaaaa;"#end class="commit">
				<li><img src="/style/images/plan/photo.gif" width="26" height="26"/></li>
				<li>
					<div><b>$globals.get($comm,5)</b><a href="javascript:commit('$planrow.id','0','$planrow.employeeID','$!globals.encodeHTML2($planrow.title)','$globals.get($comm,0)','HF');" class="data_02_pp" title="回复"></a><span>$globals.get($comm,6)</span></div><br />
					<div>$globals.encodeHTML($globals.get($comm,3))</div>
				</li>
			</div>
			#if($!commitMap.get($globals.get($comm,0))!="")
			#set($commList=$commitMap.get($globals.get($comm,0)))
			<div style="border-bottom:1px dashed #aaaaaa;">
				#foreach($comm2 in $commList)
				<ul style="padding:0;">
					<div>
						<li><img src="/style/images/plan/photo.gif" width="26" height="26" /></li>
						<li>
							<div><b>$globals.get($comm2,5)</b><a href="javascript:commit('$planrow.id','0','$planrow.employeeID','$!globals.encodeHTML2($planrow.title)','$globals.get($comm,0)','HF');" class="data_02_pp"></a><span>$globals.get($comm2,6)</span></div><br />
							<div>$globals.encodeHTML($globals.get($comm2,3))</div>
						</li>
					</div>
				</ul>
				#end
			</div>
			#end
			#end
			#end
		</ul>
	</div>
	#end
	#if("$!planrow.summary"!="")
	<div class="data_04">
		<div class="data_04_div"><b>总结</b>
			<span>$!planrow.time小时</span>
			<span style="float: right;">
				<a class="className" href="javascript:commit('$planrow.id','1','$planrow.employeeID','$!globals.encodeHTML2($planrow.title)','','DF')" title="$text.get("oa.workplan.comment")总结"><img src="/style/images/plan/ti_03.gif"/></a>
				#if($!planrow.employeeID == "$LoginBean.id") 
					<a href="javascript:addSummar('/OAWorkPlanAction.do?operation=$globals.getOP("OP_ADD")&opType=summaryPrepare&planSum=plan&keyId=$!keyId&planId=$planrow.id&userId=$strUserId&planType=$planrow.planType&strDate=$strDate&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus');" title="修改$text.get("oa.planSummar.add")"><img src="style/plan/M_1.gif" /></a>
				#end
			</span>
		</div>
		<p class="data_04_p">$!planrow.summary</p>
		<span class="data_04_span">$!planrow.summaryTime</span>
		#if($commits.size()>0)
		<div style="float:left;width:97%;margin-left:20px;border-top:1px solid #c5c5c5;"></div>
		#foreach($comm in $commits)
		#if("$globals.get($comm,2)"=="1" && "$globals.get($comm,1)"==$planrow.id)
		<div class="data_03" style="border:0px;">
			<ul style="padding:0;">
				<div #if($!commitMap.get($globals.get($comm,0))=="")style="border-bottom:1px dashed #aaaaaa;"#end class="commit">
					<li><img src="/style/images/plan/photo.gif" width="26" height="26" /></li>
					<li>
						<div><b>$globals.get($comm,5)</b><a href="javascript:commit('$planrow.id','1','$planrow.employeeID','$!globals.encodeHTML2($planrow.title)','$globals.get($comm,0)','HF');" class="data_02_pp" title="回复"></a><span>$globals.get($comm,6)</span></div><br />
						<div>$globals.encodeHTML($globals.get($comm,3))</div>
					</li>
				</div>
				#if($!commitMap.get($globals.get($comm,0))!="")
				#set($commList=$commitMap.get($globals.get($comm,0)))
				<div style="border-bottom:1px dashed #aaaaaa;">
					<ul style="padding:0;">
						#foreach($comm2 in $commList)
						<div>
							<li><img src="/style/images/plan/photo.gif" width="26" height="26" /></li>
							<li>
								<div><b>$globals.get($comm2,5)</b><a href="javascript:commit('$planrow.id','1','$planrow.employeeID','$!globals.encodeHTML2($planrow.title)','$globals.get($comm,0)','HF');" class="data_02_pp"></a><span>$globals.get($comm,6)</span></div><br />
								<div>$globals.encodeHTML($globals.get($comm2,3))</div>
							</li>
						</div>
						#end
					</ul>
				</div>
				#end
			</ul>
		</div>
		#end
		#end
		#end
	</div>
	#end
	<div class="data_05" style="background:url(/style/images/plan/back_$!{planrow.statusId}.png);"></div>
	</div>
</div>
#end
#end

#foreach($date in $dayMap.keySet())

<div class="data" id="dataId">
#foreach($planrow in $dayMap.get($date))
	<div class="data_top">
		<span title="$!planrow.title" style="background:url(/style/images/plan/column_a.gif) no-repeat left center;padding-left:25px;">
		 $globals.getDate($date)</span> <b style="float:left;margin-left:10px;">$!globals.getWeekByDate("$date")</b>
		<div style="float:left;margin-left:30px;color:#6e6e6e;">#if($!timeMap.get($date)!="")总耗时 <a style="color:#f76300;">$!timeMap.get($date)</a> 小时#end</div>
		<div>
			<div style="margin-top:7px;"><a href="javascript:alertSet('$planrow.id','$!planrow.title');"><img src="/style/images/plan/time.gif" title="$text.get('mywork.lb.checkWarm')"/> </a>
			</div>
		</div>
	</div>
	<div style="float:left;width:100%;border-bottom:1px solid #c5c5c5;">
	<div style="float: left;margin-left:15px;margin-top:15px;"><b>
	#if("$!planrow.planType"=="week")
		[周计划]
		#elseif("$!planrow.planType"=="day")
		[日计划]
		#elseif("$!planrow.planType"=="month")
		[月计划]
	#end
 	#if("$planrow.employeeID"!="$LoginBean.id")[$!employeeMap.get($planrow.employeeID)]#end#if($!planrow.title.length()>60) $!planrow.title.substring(0,60)... #else $!planrow.title #end</b></div>
	<div class="data_01" style="overflow-x:auto;">
		<p>$planrow.content</p>
		<span>$!planrow.createTime</span>
	</div>
	#if($!assItem.get($planrow.id)!="")
	#foreach($row in $associate)
	#set( $monkey = 0 )
	<div class="data_02">	
	#foreach($item in $assItem.get($planrow.id)) 
		#if($globals.get($item,1) == $row.id && $globals.get($item,4)==$planrow.id)
		#set( $monkey = 1 )		
		#end
		#end
		#if($monkey == 1)
		<div class="TypeDiv">$row.name：</div>
		#end 								
		<!-- <a class="Ic_005"></a> -->
		#foreach($item in $assItem.get($planrow.id)) 
		#if($globals.get($item,1) == $row.id && $globals.get($item,4)==$planrow.id)
		
		<span id="${planrow.id}_${row.id}_$globals.get($item,2)">
			#if("$!planrow.employeeID"!="$LoginBean.id")
			<a></a>
			#else
			<a href="javascript:delAssoicate('$planrow.id','${row.id}','$globals.get($item,2)')" class="data_02_del"></a>
			#end
			<div>#if($globals.get($item,3).length() >10)  $globals.get($item,3).substring(0,10) #else $globals.get($item,3) #end</div>
			#if($row.isEmployee=="1" and $globals.get($item,2)!=$LoginBean.id)<a href="javascript:msgComm('$globals.get($item,2)','$globals.get($item,3)')" class="data_02_pp"></a>#end
		</span>
		#end
		#end
	</div>
	#end
	#end
	#if($planrow.attach != "")
	<div class="data_02">
		<div>附&nbsp;&nbsp;&nbsp;&nbsp;件：</div><!--<a href="#" class="Ic_006"></a>-->
		#foreach ($str in $globals.strSplit($planrow.attach,';'))
		<span><a href="/ReadFile?tempFile=path&path=/plan/&fileName=$!globals.encode($str)" target="_blank"><div>$str</div></a></span>
		#end
	</div>
	#end
	<div class="data_01_bu">
			<div><a class="className" href="javascript:commit('$planrow.id','0','$planrow.employeeID','$!globals.encodeHTML2($planrow.title)')" title="$text.get("oa.workplan.comment")计划"><img src="/style/images/plan/ti_03.gif"/></a></div>
			#if($!planrow.employeeID == "$LoginBean.id" && "$!planrow.summary"=="") 
				<input type="button" onclick="addSummar('/OAWorkPlanAction.do?operation=$globals.getOP("OP_ADD")&opType=summaryPrepare&planSum=plan&keyId=$!keyId&planId=$planrow.id&userId=$strUserId&planType=$planrow.planType&strDate=$strDate&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus');" class="bu_02_i_n" value="$text.get("oa.planSummar.add")" />
			#end
		</div>
	#if($commits.size()>0)
	<div class="data_03">
		<ul style="padding:0;">
			#foreach($comm in $commits)
			#if("$globals.get($comm,2)"=="0" && "$globals.get($comm,1)"==$planrow.id)
			<div #if($!commitMap.get($globals.get($comm,0))=="")style="border-bottom:1px dashed #aaaaaa;"#end class="commit">
				<li><img src="/style/images/plan/photo.gif" width="26" height="26"/></li>
				<li>
					<div><b>$globals.get($comm,5)</b><a href="javascript:commit('$planrow.id','0','$planrow.employeeID','$!globals.encodeHTML2($planrow.title)','$globals.get($comm,0)','HF');" class="data_02_pp" title="回复"></a><span>$globals.get($comm,6)</span></div><br />
					<div>$globals.encodeHTML($globals.get($comm,3))</div>
				</li>
			</div>
			#if($!commitMap.get($globals.get($comm,0))!="")
			#set($commList=$commitMap.get($globals.get($comm,0)))
			<div style="border-bottom:1px dashed #aaaaaa;">
				#foreach($comm2 in $commList)
				<ul style="padding:0;">
					<div>
						<li><img src="/style/images/plan/photo.gif" width="26" height="26" /></li>
						<li>
							<div><b>$globals.get($comm2,5)</b><a href="javascript:commit('$planrow.id','0','$planrow.employeeID','$!globals.encodeHTML2($planrow.title)','$globals.get($comm,0)','HF');" class="data_02_pp"></a><span>$globals.get($comm2,6)</span></div><br />
							<div>$globals.encodeHTML($globals.get($comm2,3))</div>
						</li>
					</div>
				</ul>
				#end
			</div>
			#end
			#end
			#end
		</ul>
	</div>
	#end
	#if("$!planrow.summary"!="")
	<div class="data_04">
		<div class="data_04_div"><b>总结</b>
			<span>$!planrow.time小时</span>
			<span style="float: right;">
				<a class="className" href="javascript:commit('$planrow.id','1','$planrow.employeeID','$!globals.encodeHTML2($planrow.title)','','DF')" title="$text.get("oa.workplan.comment")总结"><img src="/style/images/plan/ti_03.gif"/></a>
				#if($!planrow.employeeID == "$LoginBean.id") 
					<a href="javascript:addSummar('/OAWorkPlanAction.do?operation=$globals.getOP("OP_ADD")&opType=summaryPrepare&planSum=plan&keyId=$!keyId&planId=$planrow.id&userId=$strUserId&planType=$planrow.planType&strDate=$strDate&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus');" title="修改$text.get("oa.planSummar.add")"><img src="style/plan/M_1.gif" /></a>
				#end
			</span>
		</div>
		<p class="data_04_p">$!planrow.summary</p>
		<span class="data_04_span">$!planrow.summaryTime</span>
		#if($commits.size()>0)
		<div style="float:left;width:97%;margin-left:20px;border-top:1px solid #c5c5c5;"></div>
		#foreach($comm in $commits)
		#if("$globals.get($comm,2)"=="1" && "$globals.get($comm,1)"==$planrow.id)
		<div class="data_03" style="border:0px;">
			<ul style="padding:0;">
				<div #if($!commitMap.get($globals.get($comm,0))=="")style="border-bottom:1px dashed #aaaaaa;"#end class="commit">
					<li><img src="/style/images/plan/photo.gif" width="26" height="26" /></li>
					<li>
						<div><b>$globals.get($comm,5)</b><a href="javascript:commit('$planrow.id','1','$planrow.employeeID','$!globals.encodeHTML2($planrow.title)','$globals.get($comm,0)','HF');" class="data_02_pp" title="回复"></a><span>$globals.get($comm,6)</span></div><br />
						<div>$globals.encodeHTML($globals.get($comm,3))</div>
					</li>
				</div>
				#if($!commitMap.get($globals.get($comm,0))!="")
				#set($commList=$commitMap.get($globals.get($comm,0)))
				<div style="border-bottom:1px dashed #aaaaaa;">
					<ul style="padding:0;">
						#foreach($comm2 in $commList)
						<div>
							<li><img src="/style/images/plan/photo.gif" width="26" height="26" /></li>
							<li>
								<div><b>$globals.get($comm2,5)</b><a href="javascript:commit('$planrow.id','1','$planrow.employeeID','$!globals.encodeHTML2($planrow.title)','$globals.get($comm,0)','HF');" class="data_02_pp"></a><span>$globals.get($comm,6)</span></div><br />
								<div>$globals.encodeHTML($globals.get($comm2,3))</div>
							</li>
						</div>
						#end
					</ul>
				</div>
				#end
			</ul>
		</div>
		#end
		#end
		#end
	</div>
	#end
	
	<div class="data_06" style="background:url(/style/images/plan/back_$!{planrow.statusId}.png);"></div>
	</div>
#end
</div>
#end

<div id="massage_box" class="popup" style="background:url(/style/images/plan/popup_bg_b.png) no-repeat 0 0;width:601px;left:20%;">
	<div class="popup_title" style="width:590px;"><span id="FlagName"></span><span style="float:right;margin:12px 20px 0 0;"><a href="javascript:closeCommit();"><img src="/style/images/plan/error.gif"></a></span></div>
	<div class="popup_data"><textarea name="commit" rows="8"  id="commitName"  style="margin-top:10px;width:570px;"></textarea></div>
	<div class="popup_button"><button type="button" onclick="commitSubmit();">确定</button></div>
</div>

</form>
</div>
<div id="w" class="search_popup" style="display:none;height: 150px;">
		<div onmousedown="MDown(w)" style="cursor: move;">
			<span class="ico_4"></span>$text.get("com.query.conditions")
		</div>
		<form method="post" id="myform" name="myform" action="/OAWorkPlanAction.do?operation=4&opType=search">
			<input type="hidden" name="planType" value="$planType" /> 
			<input type="hidden" name="strUserId" value="$strUserId" /> 
			<input type="hidden" name="strDate" value="$strDate" /> 
			<input type="hidden" name="keyId" value="$!keyId" />
		 	<input type="hidden" name="queryType" value="advance"> 
			<table style="margin-top: 10px;">			
				<tr>
					<td align="right"><span style="margin-left: 15px;">标题：</span></td>
					<td ><input size="13" id="planTitle" name="planTitle" type="text" value="$!planTitle" onKeyDown="if(event.keyCode==13) subForm();"/></td>
					<!-- <td>$text.get("oa.common.publisher")：	</td>		
					<td>
						<input name="createBy" id="createBy"  type="hidden" value="$!palnSearchForm.createBy" />
						<input name="proUserName" id="proUserName" size="13" onKeyDown="if(event.keyCode==13) subForm();" type="text" readonly="readonly"  onClick="deptPopForAccount('userGroup','proUserName','createBy');" value="$!palnSearchForm.proUserName" />
						<img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForAccount('userGroup','proUserName','createBy');" />		
					</td>
					 -->
					 <td align="right">计划类型：</td>
					<td>
						<select name="searchPlanType" id="searchPlanType" value="$!searchPlanType" style="width:113px" onKeyDown="if(event.keyCode==13) subForm();">
							<option value="" selected="selected"  ></option>
							#foreach($row_planType in $globals.getEnumerationItems("toplanType"))
								<option  value="$row_planType.value" 
									#if($!searchPlanType==$row_planType.value) selected #end >
									$row_planType.name计划
								</option>
							#end
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">时间：</td>
					<td >
						<input name="beginTime"  id="beginTime" size="13" value="$!beginTime" onKeyDown="if(event.keyCode==13) subForm();" onClick="openInputDate(this);" />
				    </td>
					<td align="center"> 至	</td>
					<td>					
						<input name="endTime"  id="endTime" size="13" value="$!endTime" onKeyDown="if(event.keyCode==13) subForm();" onClick="openInputDate(this);" />					
					</td>
				</tr>
				<!-- 
				<tr>
					<td align="right">$text.get("oa.common.adviceType")：</td>
					<td>
						<select name="adviceType" id="select" value="$!palnSearchForm.planType" style="width:110px" onKeyDown="if(event.keyCode==13) subForm();">
							<option value="" selected="selected"  ></option>
							#foreach($row_adviceType in $globals.getEnumerationItems("AdviceType"))
								<option  value="$row_adviceType.value" 
									#if($!palnSearchForm.planType==$row_adviceType.value) selected #end >
									$row_adviceType.name 
								</option>
							#end
						</select>
					</td>
					<td></td>
					<td></td>
					
				</tr>
				 -->
			</table>
			<span class="search_popup_bu" ><input type="button" onClick="subForm();" class="bu_1" /><input type="button" onclick="closeSearch();" class="bu_2" /></span>
			</form>
			<script language="javascript">			
				var flagId = "#"+$("#flagId").val();
				var container = $('div');
				var scrollTo = $(flagId);

				container.scrollTop(
				scrollTo.offset().top - container.offset().top + container.scrollTop()
				);										
				function showSearch(){
					$("#createBy").val("");
					$("#proUserName").val("");
					if($('#w').css("display")== "none")
					 $('#w').show();
					else
					 $('#w').hide();
				}
				
				function closeSearch(){
					$('#w').hide();
				}
				function subForm(){
					var title = $("#planTitle").val();
					var beginTime = $("#beginTime").val();
					var endTime = $("#endTime").val();
					if(title.length==0 && beginTime.length==0 && endTime.length==0){
						alert("标题和时间不能同时为空！");
						return false;
					}
					myform.submit();
					closeSearch();
				}
			</script>
	<div id="userIdDiv" class="msgpoup_middle">
		
	</div>
	
  </div>
  
</body>