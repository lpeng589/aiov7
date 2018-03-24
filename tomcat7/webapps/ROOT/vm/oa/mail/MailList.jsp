<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.mail.IndexPage")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/CRselectBox.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/email.css" />
<link type="text/css" rel="stylesheet" href="/style/css/mail.css" />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/popMenu.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/listGrid.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/oa/jquery-1.2.6.pack.js"></script>
<script type="text/javascript" src="/js/aioselect.js"></script>
<script type="text/javascript" src="/js/popMenu.js"></script>
<script type="text/javascript" src="$globals.js("/js/email.vjs","",$text)"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<style type="text/css">
#highSearch li{width: 230px;float: left;margin:5px 0 0 3px;}
#highSearch li span{float:left;width:55px;display:inline-block;text-align:right;margin-top: 5px;	}
#highSearch li input{float: left;display:inline-block;width: 150px;}
.toolbu_06 .CRselectBox .CRselectBoxOptions{width:90px;}
.toolbu_04 .CRselectBox .CRselectBoxOptions{width:110px;}
.listRange_pagebar{float: right;margin-right: 40px;}
#noread_a{font-size: 12px;color: #657AE7;}
#noread_a:hover{text-decoration: underline;}
</style>
<script type="text/javascript">
var groupId = "$!groupId";
var emailType="$!emailType";
var curMailId="";

#if("$!emailType"=="1" ||"$!emailType"=="")
function Do_transmitIntOut(){	
	popsubmits('transmitOut');
}
#else
function Do_transmitIntOut(){	
	popsubmits('transmitInt');
}
#end

var menu = new RightMenu();
menu.AddItem("newOpen","/style/images/mail/bq.gif","$text.get("email.lb.newwindow")","rbpm");
#if("$!emailType"!="" && "$!emailType" != "inner"  && "$!emailType" != "alert" && "$!emailType" != "assign" ) 
#if("$LoginId" == "1" || $LoginBean.operationMap.get("/OACommunication.do").query())
menu.AddItem("contact","/style/images/mail/contant.png","$text.get("email.lb.refClientMan")","rbpm");
#end
#if("$LoginId" == "1"  || $LoginBean.operationMap.get("/ClientAction.do").query())
menu.AddItem("client","/style/images/mail/client.gif","$text.get("calendar.lb.RelationKeHu")","rbpm");
#end
#end
menu.AddItem("sperator","","","rbpm",null);
menu.AddItem("signRead","/style/images/mail/read.gif","$text.get("email.lb.setread")","rbpm");
menu.AddItem("signNoread","/style/images/mail/unread.gif","$text.get("email.lb.setnoread")","rbpm");


#if("$!groupId" == "1" )
menu.AddItem("replay","/style/images/mail/replay.gif","$text.get("oa.mail.restore")","rbpm");
menu.AddItem("replayAll","/style/images/mail/replayall.gif","$text.get("oa.mail.restoreAll")","rbpm");
#end
#if("$!groupId" == "1" ||"$!groupId" == "3")
menu.AddItem("transmit","/style/images/mail/tran.gif","$text.get("oa.mail.transmit")","rbpm");
#if("$!emailType"=="1" ||"$!emailType"=="")
menu.AddItem("transmitIntOut","/style/images/mail/tran2.gif","$text.get("oa.mail.lb.trOut")","rbpm");
#else
menu.AddItem("transmitIntOut","/style/images/mail/tran2.gif","$text.get("oa.mail.lb.trIn")","rbpm");
#end
#end

#if("$!emailType" != "" && ("$LoginId" == "1"  || $LoginBean.operationMap.get("/EMailBlackQueryAction.do").add()))
menu.AddItem("black","/style/images/mail/black.gif","$text.get("email.lb.addblack")","rbpm");
#end
document.writeln(menu.GetMenu());
</script>

<script type="text/javascript">

$(document).ready(function(){
	if(typeof(top.junblockUI)!="undefined"){
		top.junblockUI();
	}
	$("#keyword").focus();
	
	$(".d-menu-li").hover(
		function(){$(this).find(".in-nav-ul").show();},
		function(){$(this).find(".in-nav-ul").hide();}
	);
	
});
function alertSet(){
	if($result.size() <= 0 || !isChecked("keyId") ){
		asyncbox.tips("$text.get('common.msg.mustSelectOne')",'alert',1500);
	} else {
		var keyIds=document.getElementsByName("keyId");
		var keyId="";
		var flag=false;
		var mt ="";
		var mr="";
		for(var i=0;i<keyIds.length;i++){
			if(keyIds[i].checked){
				if(flag){
					asyncbox.tips("$text.get('oa.mail.msg.transmit')",'alert',1500);
					return false;
				}else{
					flag=true;
					keyId=keyIds[i].value;
				}
			}
		}
		var urls=encodeURIComponent('/EMailAction.do?operation=5&detailType=true&noback=true&keyId='+keyId);
		var typestr=encodeURIComponent('$text.get("common.email.alert")'+curMailTitle);
		var title=encodeURIComponent('$text.get("oa.mail.myMail")');
		var date= new Date();
		var url = "/UtilServlet?operation=alertDetail&relationId="+keyId+"&falg=true"+"&title="+title+"&typestr="+typestr+"&urls="+urls+"&date="+date;
		asyncbox.open({
			id : 'warmsetting',
	　　　   	url : url,
		 	title : '$text.get("workflow.msg.warmsetting")',
	　　 	　	width : 560,
	　　	 　	height : 460,
		    callback : function(action,opener){
			    if(action == 'ok'){ 
			    	opener.checkAlertSet();
				}
	　　      }
	 　 });
	}
}

/* 弹出框返回函数处理 */
function dealAsyn(){
	jQuery.close('warmsetting');
	refreshPare();
	
}

function blockUI(){
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: "<div style='width:135px;'><img src='/style/images/load.gif'/></div>",css:{ background: 'transparent'}}); 
	}
}

function keywordQuery(){
	var keyword = $("#keyword");
	if(keyword.val().trim()=="标题查询"){
		keyword.val("") ;
	}
	blockUI();
	form.submit();
}

/* 任务分派 */
function assign(){
	if(!isChecked("keyId")){
		asyncbox.tips("$text.get('common.msg.mustSelectOne')",'alert',1500);
		return false;
	}	
	deptPop('userGroup','','','1');
}

function newOpenSelect(str,fieldName,fieldNameIds,flag){
	if(typeof(str) == "undefined") return;
	var mutli=str.split("|"); 	
	var eids= "";
	if(str.length>5){ 
		var len=mutli.length;
		if(len>1){len=len-1;}
		for(j=0;j<len;j++){ 
			fs=mutli[j].split(";");
			eids += fs[0]+",";
		}
	}
	if(eids != ""){	
		form.assignUser.value=eids;
		form.operation.value=$globals.getOP("OP_UPDATE");	
		form.submit();
	}
}

function refreshPare(){
	window.location.reload();
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

function selectChange(methodName,fieldName,fieldValue){
	$("#"+fieldName).val(fieldValue);
	setTimeout(methodName,100);
}

function searchSubmit(){
	blockUI();
	form.submit();
}

//邮件关联客户添加成功后回调方法




function crmOpDeal(){
	form.submit();
}
//客户弹出框




//关联客户
var mailIdc="";
var cNamec="";
var emailc="";
function openSelectParent(displayName,obj,obj2,mailId,cName,email){
	mailIdc=mailId;
	cNamec=cName;
	emailc=email;
	//var str  = window.showModalDialog(urlstr+"&MOID=a30d569a_1012222149354060240&MOOP=query&LinkType=@URL:&displayName="+displayName,"","dialogWidth=730px;dialogHeight=450px"); 
	var urls = "/UserFunctionAction.do?operation=22&selectName=ReportSelectClient&MOID=a30d569a_1012222149354060240&MOOP=query&popupWin=Popdiv&LinkType=@URL:&displayName="+encodeURI(displayName)
	asyncbox.open({id:'Popdiv',title:displayName,url:urls,width:750,height:470
		,callback : function(action,opener){		   
　　  　 }
	});
}

//客户弹出框回填方法




var ClientNo="";
var ClientName="";
function exePopdiv(returnValue){
	var str = returnValue;
	if(typeof(str)=="undefined"){return;}
	if(str.length>0){
		
		var strs = str.split('#;#');
		ClientNo=strs[2];
		ClientName=strs[1];
	}
	//detail_client(mailIdc,ClientNo,cNamec,emailc,ClientName);
	updateClient(ClientNo,ClientName,'1_1','true');
}
/*修改客户*/
function updateClient(keyId,keyName,viewId,contactFlag){
	var height = 540;
	var url = "";
	var title = "修改客户";
	if(contactFlag == "true"){
		url = "/CRMClientAction.do?operation=6&type=addContact&clientId="+keyId+"&emailAddress="+emailc;
		height = height -100;
		title = keyName+"------新增联系人";
	}
	//var url = "/CRMClientAction.do?operation=7&tableName=CRMClientInfo&contactTableName=CRMClientInfoDet&keyId="+keyId+"&nowHead=" + nowHead;
	asyncbox.open({
		id:'dealdiv',url:url,title:title,width:840,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	opener.beforeSubmit();
				return false;
			}
　　  　 }
　  });
}

function delselfById(strid){
	strid="#CK_"+strid;
	$(strid).parent().parent().remove();
}

var popN;
function deptPopForAccountList(popName){
	var urls = "/Accredit.do?popname="+popName+"&inputType=&inputType=radio" ;
	var	title = "请选择选客户联系人";
	popN = popName;
	asyncbox.open({
	   id : 'Popdiv',
	   title : title,
	　　　url : urls,
	　　　width : 750,
	　　　height : 430,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	if(action == 'ok'){
				var str = opener.strData;			
			if(str.indexOf("@") != -1){
				str=str.split(";");
				for( i in str){
					var temp=str[i];
					if(temp.indexOf("@") != -1){
						str=str[i];
						break;
					}
				}
				form.email.value=str;
			}
　　　　　	}else{
			popN = "";
		}
　　　	}
　	});
}

//处理双击个人部门弹出框




function fillData(datas){
	if(fieldNames == "email"){
		newOpenSelectSearch(datas,fieldNames,fieldNIds);
	}else if(popN == "clientGroup"){
		var str = datas;
		if(str.indexOf("@") != -1){
			str=str.split(";");
			for( i in str){
				var temp=str[i];
				if(temp.indexOf("@") != -1){
					str=str[i];
					break;
				}
			}
			form.email.value=str;
		}
	}else{
		newOpenSelect(datas,fieldNames,fieldNIds,flag);
	}
	jQuery.close('Popdiv');
}


/* 选中复选框 */
function selectKey(id){
	if($("#"+id).is(":checked")){
		$("#"+id).removeAttr("checked");
	}else{
		$("#"+id).attr("checked","true");
	}
}

function pageSubmit(pageNo){
	$("#pageNo").val(pageNo);
	blockUI();
	form.submit();
}
function submitQuery(){
	blockUI();
	form.submit();
}

/* 查询未读数据 */
function showNoRead(){
	$("#keyword").val('');
	$("#highSearch").find("input").each(function(){
		$(this).val('');
	});
	$("#view").val('1');
	submitQuery();
}

/* 获取显示未读邮件条数 */

function refreshStat(){
	parent.refreshStat();
}
refreshStat();
/* 邮件详情 */
function detailMail(mailId,mailTitle,createTime){
	var urls ="/EMailAction.do?operation=$globals.getOP("OP_DETAIL")&groupId="+groupId+"&emailType="+emailType+"&iframe=true&keyId="+mailId+"&createTime="+createTime;
	asyncbox.open({
	　　　url : urls,
	　　　width : document.documentElement.clientWidth,
	　　　height : document.documentElement.clientHeight,
	　　　callback : function(action){
	　　　}
	　});
}
</script>

</head>
<body>
<form  method="post" scope="request" name="form" action="/EMailQueryAction.do">
<input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")"/>
<input type="hidden" name="type" value="main"/>
<input type="hidden" name="groupId" value="$!groupId"/>
<input type="hidden" name="emailType" value="$!emailType"/>
<input type="hidden" name="deleteReal" value = "false"/>
<input type="hidden" name="isdesc" value = "$!EMailSearchForm.isdesc"/>
<input type="hidden" name="isprev" value = "false"/>
<input type="hidden" name="curMailId" value = ""/>
<input type="hidden" name="assignUser" value = ""/>
<input type="hidden" name="view" id="view" value=""/>
<input type="hidden" name="exportType" value = "aio"/>
<input type="hidden" id="searchType" name="searchType" value = "false"/>
<div class="MailSearch" id="list_mainframe">
		<li style="float:left;margin-left:10px;">
		<div style="color:#3C78AA;;font-weight:bold;font-size:15px;">
		  #if("$!groupId"=="1" && "$!emailType"!="assign")
		  	#if("$!emailType"=="alert")
		  		$!text.get("mail.lb.nextAlterMail")
		  	#elseif("$!emailType"=="collection")
		  		$!text.get("oa.mail.mailCollection")
		  	#else
			 	 $!text.get("oa.mail.receive.box")
			#end
		  #elseif("$!groupId"=="2")
		  	$!text.get("oa.mail.draft")$!text.get("oa.mail.box")
		  #elseif("$!groupId"=="3")
		  	$!text.get("oa.mail.outBox")$!text.get("oa.mail.box")
		  #elseif("$!groupId"=="4")
		  	$!text.get("oa.mail.dust")
		  #elseif("$!groupId"=="5")
		  	$!text.get("oa.mail.deleted")
		  #else
		  	$!groupInfo.groupName
		  #end	
			#if("$!emailType"=="inner" || "$!emailType"=="" )
			  	($text.get("oa.common.innerMail"))
			#elseif("$!emailType"=="assign")   
					$!text.get("email.lb.mypmail")
	
			#elseif("$!accountInfo.account" != "")
			  	($!accountInfo.account)
			#end	&nbsp;
		 #if("$!groupId"=="1")
		 	<a title="点击查看未读邮件" onclick="showNoRead()" id="noread_a">有 $!noReadSum 封未读邮件</a>
		 #end
		  
			</div>
		</li>
		<li>
		<input id ="keyword" style="height:22px;" name="keyword" type="text"  class="search_text" value="$!EMailSearchForm.keyword" onKeyDown="if(event.keyCode==13)keywordQuery();" placeholder="标题查询" /><input type="button" class="search_button" style="cursor: pointer;" onclick="javascript:keywordQuery();"/>
		<span onclick="showSearch();" class="btn-w">$text.get("common.lb.Advancequery")</span>
		</li>
</div>

<!-- main-wp 头部 Start  -->
<div class="main-wp">
<div class="toolbar" id="toolbar_id">
	<div class="toolbat_center HeadingButton">
		<ul>
		#if("$!groupId" == "2" )
			<li><span class="btn-w" onclick="javascript:return update(); ">$text.get("common.lb.update")</span></li>
		#end
		#if("$!groupId" != "2" && "$!groupId" != "3" )
			<li><span class="btn-w" onclick="javascript:return submits('revert'); ">$text.get("oa.mail.restore")</span></li>
			<li><span class="btn-w" onclick="javascript:return submits('revertAll'); ">$text.get("oa.mail.restoreAll")</span></li>
		#end
		#if("$!groupId" != "2")
			<li><span class="btn-w" onclick="javascript:return submits('transmit'); ">$text.get("oa.mail.transmit")</span></li>
			#if("$!emailType"=="1" ||"$!emailType"=="")
				<li><span class="btn-w" onclick="javascript:return submits('transmitOut'); " >$text.get("oa.mail.lb.trOut")</span></li>
			#else
				<li><span class="btn-w" onclick="javascript:return submits('transmitInt'); " >$text.get("oa.mail.lb.trIn")</span></li>
			#end
		#end
		#if("$!groupId" != "2" && "$!groupId" != "3" )
			<!-- <li><span class="btn-w" onclick="javascript:return assign();">$text.get("email.lb.assign")</span></li> -->
		#end
		<li><span class="btn-w" onclick="javascript:return sureDel('keyId');">$text.get("common.lb.del")</span></li>
		<li><span class="btn-w" onclick="javascript:return sureDelChe('keyId');">$text.get("oa.mail.clear")</span></li>
		#if($!groupId != "5")
			<li class="d-menu-li">
				<input type="hidden" value="-1" name="sign" id="sign" />
				<div class="btn-w h-child-btn">
					<em id="tb_mr1" onClick="">$text.get("email.lb.sign")</em>
					<b class="triangle"></b>
				</div>
				<ul class="in-nav-ul">
					<li onclick="selectChange('sign()','sign','1')">$text.get("email.lb.setread")</li>
					<li onclick="selectChange('sign()','sign','0')">$text.get("email.lb.setnoread")</li>
				</ul>
			</li>
		#end
		#if("$!groupId" != "2" && "$!emailType" !="assign")
			<li class="d-menu-li">
				<input type="hidden" value="-1" name="moveGroup" id="moveGroup" />
				<div class="btn-w h-child-btn">
					<em id="tb_mr1" onClick="">$text.get("oa.mail.move")</em>
					<b class="triangle"></b>
				</div>
				<ul class="in-nav-ul">
					#if("$!groupId" != "1")
						<li onclick="selectChange('move()','moveGroup','1')">$text.get("oa.mail.receive.box")</li>
					#end
					#if("$!groupId" != "3")
						<li onclick="selectChange('move()','moveGroup','3')">$text.get("oa.mail.outBox")$text.get("oa.mail.box")</li>
					#end
					#if("$!groupId" != "4")
						<li onclick="selectChange('move()','moveGroup','4')">$text.get("oa.mail.dust")</li>
					#end
					#if("$!groupId" != "5")
						<li onclick="selectChange('move()','moveGroup','5')">$text.get("oa.mail.deleted")</li>
					#end
					#foreach ($vg in $groups)	 		
						#if("$!groupId"!="$globals.get($vg,0)")
							<li onclick="selectChange('sign()','moveGroup','$globals.get($vg,0):$globals.get($vg,2)')">$globals.get($vg,1)</li>
						#end
					#end
				</ul>
			</li>
		#end
		<li class="d-menu-li">
			<input type="hidden" value="-1" name="setLabel" id="setLabel" />
			<div class="btn-w h-child-btn">
				<em id="tb_mr1" onclick="">$text.get("email.lb.label")</em>
				<b class="triangle"></b>
			</div>
			<ul class="in-nav-ul">
				#foreach($ll in $labelList)
					<li onclick="selectChange('setLabel()','setLabel','$!ll')">$!ll</li>
				#end  
				<li onclick="selectChange('setLabel()','setLabel','')">$text.get("common.lb.mail.clearMark")</li>
			</ul>
		</li>
		<li class="d-menu-li">
			<input type="hidden" value="-1" name="moreOperation" id="moreOperation" />
			<div class="btn-w h-child-btn">
				<em id="tb_mr1" onClick="">更多</em>
				<b class="triangle"></b>
			</div>
			<ul class="in-nav-ul">
				<!-- <li onclick="selectChange('moreOperation()','moreOperation','startPrint')">$text.get("common.lb.print")</li> -->
				<li onclick="selectChange('moreOperation()','moreOperation','export')" title='$text.get("oa.email.export")'>$text.get("oa.email.export")</li>
				<li onclick="selectChange('moreOperation()','moreOperation','import')" title='$text.get("email.lb.import")'>$text.get("email.lb.import")</li>
				<li onclick="selectChange('moreOperation()','moreOperation','stadexport')" title='$text.get("email.lb.stadexport")'>$text.get("email.lb.stadexport")</li>
				<li onclick="selectChange('moreOperation()','moreOperation','stadImport')" title='$text.get("email.lb.stadImport")'>$text.get("email.lb.stadImport")</li>
				<li onclick="selectChange('moreOperation()','moreOperation','movetotrash')" title='$text.get("emal.lb.trashMail")'>$text.get("emal.lb.trashMail")</li>
			</ul>
		</li>
		<li><span class="btn-w" onclick="javascript:alertSet()">$text.get("alertCenter.lb.awoke")</span></li>
		</ul>
	</div>
	
	#set($groupName = "")
	#if("$!groupId" == "1")
		#set($groupName = "发件人")
	#elseif("$!groupId" == "3" || "$!groupId" == "2")
		#set($groupName = "收件人")
	#else
		#set($groupName = "收/发件人")
	#end
</div>
	<ul class="head-t-ul">
		<input class="cbox" type="checkbox" name="selAll" id="selAll" onClick="checkAll('keyId')" />
		<img class="img" src="/style/images/mail/unread.gif" />
		<li class="addressee-li">
			$groupName
		</li>
		<li class="content-li">
			主题
		</li>
		<li class="time-li">
			时间 
		</li>
	</ul>
	<div class="time-block-email">
		#foreach ( $key in $result.keySet())
			<p class="p-time">
				#if("$key" == "1")
					今天
				#elseif("$key" == "2")
					昨天
				#elseif("$key" == "4")
					前天
				#elseif("$key" == "8")
					本周
				#elseif("$key" == "16")
					上周
				#elseif("$key" == "64")
					本月
				#elseif("$key" == "256")
					更早
				#end
			<i>（$result.get($key).size() 封）</i></p>
			#foreach($info in $result.get($key))
				#set($mailN = $!info.mailTo)
				#if("$!groupId"!="2" && "$!groupId"!="3")
				  #if("$!emailType"!="" && "$!emailType" != "inner")
					#if("$!clientMap.get($globals.getMailAddress($!info.mailFrom))" != "")
						#set($mailN = $globals.get($!clientMap.get($globals.getMailAddress($!info.mailFrom)),0)+"-"+$globals.get($!clientMap.get($globals.getMailAddress($!info.mailFrom)),1))  		
						#if("$!hide"!="true")
							#set($mailN = $mailN+"&lt;"+$globals.getMailAddress($!info.mailFrom)+"&gt;") 
						#end  		
					#elseif("$!noteMap.get($globals.getMailAddress($!info.mailFrom))" != "")
						#set($mailN = $globals.get($!noteMap.get($globals.getMailAddress($!info.mailFrom)),0))  		
						#if("$!hide"!="true")
							#set($mailN = $mailN+"&lt;"+$globals.getMailAddress($!info.mailFrom)+"&gt;") 
						#end  		
					#else
						#set($mailN = $globals.encodeHTML($!info.mailFrom)) 
						#if("$!hide"=="true")
							#set($mailN = $text.get("com.new.client")) 
						#end   	
					#end
				  #else
					#set($mailN = $globals.encodeHTML($!info.mailFrom)) 
				  #end		
				#end
				
				#set($assignN = "")
				#if("$!assignMap.get($!info.id)" != "")
					#foreach($assrow in $!assignMap.get($!info.id))
						#set($assignN = $assignN + $globals.get($assrow,1)+",")
					#end
					#set($assignN = $text.get("email.lb.assignTo")+":"+$assignN + $globals.get($!assignMap.get($!info.id).get(0),2))
				#end
				<ul class="content-ul" ondblclick="javascript:clickMail('$!info.id','$!info.mailTitle.replace("'","").replaceAll("\"","")','$!info.createTime')" >
					<input class="cbox" type="checkbox" name="keyId" value="$!info.id" id="CK_$!info.id"  emailType="$!emailType" moreMail="#if($!emailType !="assign")$!emailType#end"
	#if("$!groupId"!="2" && "$!groupId"!="3")
	email="$globals.getMailAddress($!info.mailFrom)" eName="$globals.getMailName($!info.mailFrom)" 
	contactId="$!globals.get($!noteMap.get($globals.getMailAddress($!info.mailFrom)),1)" clientId="$globals.get($!clientMap.get($globals.getMailAddress($!info.mailFrom)),2)"  clientName="$globals.get($!clientMap.get($globals.getMailAddress($!info.mailFrom)),0)"
	#else
	email="$globals.getMailAddress($!info.mailTo)" eName="$globals.getMailName($!info.mailTo)" 
	contactId="$!globals.get($!noteMap.get($globals.getMailAddress($!info.mailTo)),1)" clientId="$globals.get($!clientMap.get($globals.getMailAddress($!info.mailTo)),2)" clientName="$globals.get($!clientMap.get($globals.getMailAddress($mailN)),0)"
	#end
	accept = "$!info.mailTitle"	
	/>				
				#if("$!groupId" == "2")
					<img class="img" src="/style/images/mail/mail_draft.gif" />
				#elseif("$!groupId" == "3")
					<img class="img" src="/style/images/mail/mail_send.gif" />
				#elseif("$!groupId" == "4")
					<img class="img" src="/style/images/mail/mail_notread.gif" />
				#elseif("$!groupId" == "5")
					<img class="img" src="/style/images/mail/mail_delete.gif" />
				#else
					#if("$!info.state"=="0")
						<img class="img" src="/style/images/mail/unread.gif" />
					#else
						<img class="img" src="/style/images/mail/read.gif" />
					#end
				#end
					<li class="addressee-li" onclick="selectKey('CK_$!info.id')" title="$!groupName:$mailN">
						#if("$mailN"=="") $!info.mailBCc #else $!mailN #end
					</li>
					<li class="content-li" onclick="selectKey('CK_$!info.id')" 
					title="
$text.get("email.lb.recDate"):$!info.mailTime#if("$!info.replayDate" !="")

$text.get("email.lb.repDate"):$!info.replayDate#end#if("$!info.revolveDate" !="")

$text.get("email.lb.tranDate"):$!info.revolveDate#end 

$text.get("oa.subjects"):$!info.mailTitle
$text.get("email.lb.enclosure.size"):$!info.mailSize K #if("$!info.labelId"!="")

$text.get("email.lb.label"):$!info.labelId #end
	$assignN
	" 
	#if("$!info.state" == "0") style='font-weight:700' #end>
						#if("$!info.mailTitle" == "") $text.get("oa.mail.no") #else <span style="cursor: pointer;" onclick="javascript:clickMail('$!info.id','$!info.mailTitle.replace("'","").replaceAll("\"","")','$!info.createTime')">$!info.mailTitle&nbsp; </span>#end 
						#if("$!info.mailAttaches" != "")
							&nbsp;&nbsp;<img src="/style/images/mail/attach.gif" title="存在附件" />
						#end
						#if("$!info.remark" != "")
							&nbsp;&nbsp;<img src="/style/images/mail/set.gif" title="备注：$!info.remark" />
						#end
						#if("$!info.labelId" != "")
							&nbsp;&nbsp;<img src="/style/images/mail/alter.gif" 
							title='标签：#foreach($labelrow in $!info.labelId.split(";|,|\\s"))#if("$!labelrow" != "")$labelrow &nbsp; #end#end' />
						#end
						<span style="color:#ccc;margin-left: 30px;" onclick="javascript:clickMail('$!info.id','$!info.mailTitle.replace("'","").replaceAll("\"","")','$!info.createTime')">$!info.mailContent</span>
					</li>
					<li class="time-li" onclick="selectKey('CK_$!info.id')">
						#if("$!info.mailTime.substring(0,10)" =="$globals.getDate()") $!info.mailTime.substring(11) #else $!info.mailTime.substring(0,10) #end
					</li>
				</ul>
			#end
		#end
	</div>
	#if($result.size()==0)
		<div style="height:200px;border:1px solid #ccc;border-top:0;line-height:200px;">
			<div style="height:100%;text-align: center;">
				<span style="text-align:center;font-size:20px;color:#ccc;">没有找到邮件</span>
			</div>
		</div>
	#else
		<div class="listRange_pagebar">$!pageBar</div>
	#end
</div>
<!-- main-wp 头部 End  -->
	
	<div id="highSearch" class="search_popup" style="display:none;width: 470px;height: 160px;z-index: 99;">
			<div id="Divtitle" style="cursor: move; width: 460px;"   >
				<span class="ico_4" ></span>$text.get("common.lb.Advancequery")
			</div>
			<li>
				<span>$text.get("oa.common.keyWord"):</span>
				<input  id="highKeyword" name="highKeyword" type="text"  #if("$!clearSerchForm"!="yes" && "$searchType"=="true") value="$!EMailSearchForm.keyword" #end />
				<input type="hidden" name="pageParam" value="" />
			</li>
			
				#if("$!groupId"!="3"&&"$!groupId"!="2") 
				<li><span>$text.get("oa.mail.sendPeople"):</span> 
					#if($emailType=="")
						<input  id="email" name="email" #if("$!clearSerchForm"!="yes") value="$!EMailSearchForm.email" #end onDblClick="deptPopForAccount('userGroup','email','createBy');" />
						<img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForAccount('userGroup','email','createBy');" />
					#else  
					 	<input  id="email" name="email" #if("$!clearSerchForm"!="yes") value="$!EMailSearchForm.email" #end onDblClick="deptPopForAccountList('clientGroup');"/>
						<img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForAccountList('clientGroup');" />
					#end
				</li> 
			#end		
			#if("$!groupId"=="2"||"$!groupId"=="3")
		 		 <li><span>$text.get("oa.mail.addressee"):</span>
				    #if($emailType=="") 
						 <input  id="email" name="email" #if("$!clearSerchForm"!="yes") value="$!EMailSearchForm.email" #end onDblClick="deptPopForAccount('userGroup','email','createBy');" />
						 <img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForAccount('userGroup','email','createBy');" />		
				    #else
						 <input  id="email" name="email" #if("$!clearSerchForm"!="yes")value="$!EMailSearchForm.email" #end onDblClick="deptPopForAccountList('clientGroup');" />
						 <img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForAccountList('clientGroup');" />
				 	#end
		 		</li>														
			#end
			<li><span>$text.get("scope.lb.tsscopeValue"):</span><input type="text" name="beginTime"  onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);" #if("$!clearSerchForm"!="yes") value="$!EMailSearchForm.beginTime" #end/></li>
			<li><span>$text.get("oa.job.endtime"):</span><input type="text" name="endTime"  onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);" #if("$!clearSerchForm"!="yes") value="$!EMailSearchForm.endTime" #end/></li>
			<li><span>$text.get("email.lb.label"):</span>
			<select name = "labelId">
					<option value=""></option>
				#foreach($ll in $labelList)
					<option value="$!ll"  #if("$!clearSerchForm"!="yes" && $!EMailSearchForm.labelId==$$!ll) selected #end>$!ll</option>  
				#end  
			</select>
			</li>
			<li style="margin-top: 30px;margin-left: -60px;">
				<button name="Submits" type="button" class="sb" onclick="searchSubmit()">$text.get("common.lb.query")</button>
				<button name="cancel" type="button" class="sb" onclick="showSearch()">$text.get("common.lb.close")</button>
			</li>
	  </div>
</form>
<script language="javascript">
var posX;
var posY;       
	var fdiv = document.getElementById("highSearch");         
	document.getElementById("Divtitle").onmousedown=function(e){

	    if(!e) {e = window.event;}  //IE
	    posX = e.clientX - parseInt($(fdiv).css("left"));
	    posY = e.clientY - parseInt($(fdiv).css("top"));
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
</body>
</html>