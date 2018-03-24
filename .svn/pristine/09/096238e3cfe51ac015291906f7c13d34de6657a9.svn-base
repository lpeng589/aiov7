<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" rel="stylesheet" href="/style/css/plan.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/CRselectBoxList.css"/>

<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script> 
<script type="text/javascript"  src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="$globals.js("/js/workPlan.vjs","",$text)"></script>
<script type="text/javascript"  src="/js/aioselect.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script>
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
		btnsbar : [{text : '保存新增',action : 'saveNew'},
				{text : '保 存',action : 'save'}].concat(jQuery.btn.CANCEL),
		callback : function(action,iframe){
	　　　　	if(action == 'save'){
	　　　　		if(!asybox.opener("planId").beforSubmit()){
					return false ;
				}
	　　　　	}else if(action == 'saveNew'){
				if(!asybox.opener("planId").beforSubmit("addNew")){
					return false ;
				}
			}else{
				window.location.reload() ;
			}
	　　}
	});
}

function updatePlan(strUrl){
	var asybox = parent.asyncbox ;
	if(typeof(parent.asyncbox)=="undefined" || typeof(parent.openMenu)!="undefined"){
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
	　　　　	}
	　　}
	});
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
	var divHtml = ''+
	'<div class="commit">'+
		'<li><img src="/style/images/plan/photo.gif" width="26" height="26"></li>'+
		'<li>'+
			'<div><b>江远香</b><a href=\'javascript:commit("'+commitId+'","0","0ef3502a_1105071219215520280","江远香日计划","04178161_1308010916547910162","HF");" class="data_02_pp" title="回复"></a><span>2013-08-01 09:16:54</span></div><br>'+
			'<div>而</div>'+
		'</li>'+
	'</div>';
	
	document.commitform.submit();
}

function resizeImg(){
	if($("#del").val() == "DELETE"){
		window.close();
		window.parent.opener.location.reload();
		window.opener.location.reload();		
	}
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
	window.location.href = "/OAWorkPlanAction.do?operation=4&associateId=$!associateId&userId=$!strUserId&queryType=advance&keyWord="+encodeURIComponent(keywordVal) ;
}
function containSpecial( s ){      
	var txt=new RegExp("[\\`,\\~,\\!,\\@,\#,\\$,\\%,\\^,\\+,\\*,\\&,\\\\,\\/,\\?,\\|,\\:,\\.,\\<,\\>,\\{,\\},\\(,\\),\\',\\;,\\=,\"]");  
	return ( txt.test(s) );      
}

function existLeftFrame(){
	if(typeof(window.parent.frames["planList"])=="undefined"){
		$("#topTitle").hide() ;
	}
}

function alertSet(relationId,planTitle){
	var urls=encodeURIComponent('/OAWorkPlanAction.do?operation=4&planType=day&flagAdvice=advice&winCurIndex=$winCurIndex&noback=true&nochangeTop=true&keyId='+relationId);
	var typestr=encodeURIComponent('$text.get("com.oa.plan.alert")'+planTitle);
	var title=encodeURIComponent('$text.get("com.oa.plan.name")');
	var date= new Date();
	var nochangeTop = nochangeTop;
	var url = "/UtilServlet?operation=alertDetail&relationId="+relationId+"&falg=true"+"&title="+title+"&typestr="+typestr+"&urls="+urls+"&date="+date;
	var height = 490;
	if($(document).height()<400){height=360}
	asyncbox.open({
		id : 'dealdiv',url : url,
		cache : false,modal : false,		
	 	title : '$text.get("workflow.msg.warmsetting")',
　　 　	width : 600, height : height, top :5,
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
function selectOver(){
	var value=$("#_view").val();
	window.location="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$strDate&keyId=$!keyId&opType=overThing&planStatus="+value;
}
function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}
function dingWei(){
		var newsId="$!dingWei";
		if(newsId!= ''){
			window.location.href="#"+newsId;
		}
}


</script>
<style type="text/css">
	.overThing{
		background:url(style1/images/select_box_auto.gif) no-repeat right center;
		cursor:pointer;
		margin-left:4px;
		padding-left:0px;
		width:60px;
		height:25px;
		line-height:25px;
	}								
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
	.data_02{height:100%;;}
		
	.data_02 div{display:inline-block;}
	.data_02 .TypeDiv{width:100%;}
	.data_02 span{display:inline-block;margin:10px 5px 0 0;border:1px #a1a1a1 dashed;padding:5px;}
	.data_02 span:hover{border:1px #000 solid;}
	
	.data_04_p{line-height:21px;}
	.data_03 ul div li div{clear:both;}
</style>
<script type="text/javascript">
$(function(){
	$(".data_01 table").attr("width","100%").css("border-collapse","collapse").css("text-align","center").css("width","100%");
	$(".data_01 table tr td").each(function(){
		$(this).css("border","1px #000 solid");
	});
});
</script>
</head>
<body onload="resizeImg();">
#if("$flagAdvice"!="advice")
<div class="TopTitle" id="topTitle">
		#if("$strUserId"=="$LoginBean.id")
			#if($$LoginBean.getOperationMap().get("/OAWorkPlanAction.do").add())
				
				<span><button class="bu_02_i_n" type="button" onclick="javascript:addPlan();">写计划</button></span>
				
			#end
		#end
	
	<ul>
	#if("$!planType" != "event" && "$!keyId" == "")
	$!globals.get($!AssItemCount,0)
		<li><a href="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$strDate&keyId=$!keyId">#if("$!assName"=="" && "$!associateId"=="")<b>[$!strUserName$text.get("oa.bbs.plans")]</b>#else [$!strUserName$text.get("oa.bbs.plans")] #end</a></li>
		#set($flag=0)		
	#if("$strUserId"=="$LoginBean.id")
		#foreach($row in $existAssociate)	
			#if( "$!row.isEmployee" == "1")
				<li id="$row.id"><a href="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$strDate&associateId=$row.id&keyId=$!keyId&assName=$!globals.encode($!row.name)">#if("$!assName"=="$!row.name" || "$!associateId"=="$row.id")<b>[${row.name}$text.get("oa.bbs.plan")]</b>  #else[${row.name}$text.get("oa.bbs.plan")]#end</a></li>
				 <li style="margin:0px;"><span style="margin-left:2px;margin-top:-2px;color:red;">$!AssItemCount.get($!flag)</span></li>
				#set($flag=$!flag+1)
			#end
		#end
	#end
		<li><a href="javascript:opencalWindow('/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=day&strDate=$strDate&opType=calendar')">[$text.get("workplan.lb.calenview")]</a></li>
	#end
	</ul>
	#if($$LoginBean.getOperationMap().get("/OAWorkPlanAction.do").query())
	#if("$!dsFlag" != "dsflag")			
	 <div><button class="search_button2" type="button" onclick="showSearch();">高级查询</button></div>
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
<form name="commitform" id="commitform" method="post"  action="/OAWorkPlanAction.do"  >
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
<input type="hidden" name="flagAdvice" value="$!flagAdvice"/>
<input type="hidden"  name="associateId" value="$!associateId"/>
<input type="hidden" id="flagId" name="flagId" value="$!flagId"/>
<input type="hidden" id="del" name="del" value="$!del"/>
<div id="dataId" style="float:left;overflow:hidden;overflow-y:auto;width:100%;position:relative;margin:5px 0 0 0;box-sizing:">
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
#if("$!result.size()" == "0" && "$!planType" !="event"  && "$!score" =="")
<div class="data" style="padding:100px 0px 100px 0px;">
	#if($!keyWord.length()>0 || $!queryType.length()>0 || $!planStatus.length()>0)
	<div class="data_00">
		<div class="data_00">#if("$strUserId"=="$LoginBean.id" && "$!associateId"=="")<a href="javascript:addPlan();"></a>#end</div>
		很抱歉，没有找到与您条件匹配的数据!
	</div>
	#else
 	<div class="data_00">#if("$strUserId"=="$LoginBean.id" && "$!associateId"=="")<a href="javascript:addPlan();"></a>#end</div>
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
#foreach($planrow in $result)
<a name="$!dingWei"></a>
<div class="data" id="dataId">
	<div class="data_top"><!-- style="cursor: pointer;" onclick="showOrHide('$planrow.id');" -->
		<span title="$!planrow.title" style="background:url(/style/images/plan/column_a.gif) no-repeat left center;">#if("$!associateId"!="" || "$!flagAdvice"=="advice" || "$!queryType"=="advance")[$!employeeMap.get($planrow.employeeID)]#end #if($!planrow.title.length()>60) $!planrow.title.substring(0,60)... #else $!planrow.title #end<b class="data_top_gray">[#if("$!associateId"!="" || "$!flagAdvice"=="advice" || "$!queryType"=="advance")$planrow.beginDate.substring(0,10) #end $planrow.beginDate.substring(11,16) - $planrow.endDate.substring(11,16)]</b><b class="data_top_gray">#if($!planrow.grade.length()>0)[$globals.getEnumerationItemsDisplay("planGrade",$planrow.grade)]#end</b>&nbsp;&nbsp;</span>
		<div>
			<div style="margin-top:7px;">
					<a href='javascript:alertSet("$planrow.id","$globals.revertTextCode3("$!planrow.title")");'><img src="/style/images/plan/time.gif" title="$text.get('mywork.lb.checkWarm')"/> </a>
				#if($planrow.employeeID==$LoginBean.id)
					#if($LoginBean.operationMap.get("/OAWorkPlanAction.do").update())
						<a href="javascript:updatePlan('/OAWorkPlanAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&planSum=plan&keyId=$!keyId&planId=$planrow.id&userId=$strUserId&planType=$planrow.planType&strDate=$strDate&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus');" title="$text.get("common.lb.update")"><img src="/style/images/plan/ti_02.gif" /></a>
					#end
					#if($LoginBean.operationMap.get("/OAWorkPlanAction.do").delete())
						<!--<a href="javascript:if(confirm('$text.get("common.msg.confirmDel")')){window.location.href='/OAWorkPlanAction.do?operation=$globals.getOP("OP_DELETE")&keyId=$planrow.id&userId=$strUserId&planType=day&strDate=$strDate&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus';#if("$!result.size()"=="1")window.close();#end}"  title="$text.get("common.lb.del")"><img src="/style/images/plan/ti_04.gif"/></a>
						-->
						<a href="javascript:delPlan();" title='$text.get("common.lb.del")'><img src="/style/images/plan/ti_04.gif"/></a>
					#end
				#end
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
	</div>
	<div id="$planrow.id" style="display:block;">
	<div class="data_01" style="overflow-x:auto;overflow-y:hidden;height:100%;">
		<p>$planrow.content</p>
		<span style="float:right;">$!planrow.createTime</span>
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
		<div>附&nbsp;&nbsp;&nbsp;&nbsp;件：</div><!--<a href="#" class="Ic_006"></a>-->
		#foreach ($str in $globals.strSplit($planrow.attach,';'))
		<span><a href="/ReadFile?tempFile=path&path=/plan/&fileName=$!globals.encode($str)" target="_blank"><div>$str</div></a></span>
		#end
	</div>
	#end
	<div class="data_01_bu">
			<div><a id="className" href="javascript:commit('$planrow.id','0','$planrow.employeeID','$!globals.encodeHTML2($planrow.title)','','DP')" title="$text.get("oa.workplan.comment")计划"><img src="/style/images/plan/ti_03.gif"/></a></div>
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
				<a id="className" href="javascript:commit('$planrow.id','1','$planrow.employeeID','$!globals.encodeHTML2($planrow.title)','','DF')" title="$text.get("oa.workplan.comment")总结"><img src="/style/images/plan/ti_03.gif"/></a>
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
								<div><b>$globals.get($comm2,5)</b><a href="javascript:commit('$planrow.id','1','$planrow.employeeID','$!globals.encodeHTML2($planrow.title)','$globals.get($comm,0)','HF');" class="data_02_pp"></a><span>$globals.get($comm2,6)</span></div><br />
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
<div id="massage_box" class="popup" style="background:url(/style/images/plan/popup_bg_b.png) no-repeat 0 0;width:601px;left:20%;">
	<div class="popup_title" style="width:590px;"><span id="FlagName"></span><span style="float:right;margin:5px 20px 0 0;display:inline-block;"><a href="javascript:closeCommit();"><img src="/style/images/plan/error.gif"></a></span></div>
	<div class="popup_data" style="margin:0;pading:0px;"><textarea name="commit" id="commitName" rows="8" style="margin-left:11px;margin-top:10px;width:570px;"></textarea></div>
	<div class="popup_button"><button type="button" onclick="commitSubmit();">确定</button></div>
</div>
</form>
</div>
<div id="w" class="search_popup" style="display:none;height: 150px;top: 150px;left: 250px;">
		<div id="Divtitle" style="cursor: move;">
			<span class="ico_4"></span>$text.get("com.query.conditions")
		</div>
		<form method="post" id="myform" name="myform" action="/OAWorkPlanAction.do?operation=4&opType=search">
			<input type="hidden" name="planType" value="$planType" /> 
			<input type="hidden" name="strUserId" value="$strUserId" /> 
			<input type="hidden" name="strDate" value="$strDate" /> 
			<input type="hidden" name="keyId" value="$!keyId" />
		 	<input type="hidden" name="queryType" value="advance"> 
		
			<table style="margin-top:10px;">			
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
				
var posX;
var posY;       
fdiv = document.getElementById("w");         
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
	</div>
</body>