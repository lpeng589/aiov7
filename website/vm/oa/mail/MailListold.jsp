<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.mail.IndexPage")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css"/>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/CRselectBox.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/mail.css" type="text/css"/>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/popMenu.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/oa_news.css" />
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
});
function alertSet(){
	if($result.size() <= 0 || !isChecked("keyId") ){
	 	alert('$text.get("common.msg.mustSelectOne")');
	} else {
		var urls=encodeURIComponent('/EMailAction.do?operation=5&detailType=true&noback=true&keyId='+curMailId);
		var typestr=encodeURIComponent('$text.get("common.email.alert")'+curMailTitle);
		var title=encodeURIComponent('$text.get("oa.mail.myMail")');
		var date= new Date();
		var url = "/UtilServlet?operation=alertDetail&relationId="+curMailId+"&falg=true"+"&title="+title+"&typestr="+typestr+"&urls="+urls+"&date="+date;
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

//弹出框返回函数处理











function dealAsyn(){
	jQuery.close('warmsetting');
	window.refreshIframe();
	
}

function keywordQuery(){
	var keyword = $("#keyword");
	if(keyword.val().trim()=="标题查询"){
		keyword.val("") ;
	}
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: "<div style='width:135px;'><img src='/style/images/load.gif'/></div>",css:{ background: 'transparent'}}); 
	}
	form.submit();
}

function assign(){
	if(!isChecked("keyId")){
		alert('$text.get("common.msg.mustSelectOne")');
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

function refreshIframe(){
	jQuery.close('warmsetting');
	mailDetailFrame.window.location.reload();
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

function selectChange(methodName){
	setTimeout(methodName,100);
}

function searchSubmit(){
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: "<div style='width:135px;'><img src='/style/images/load.gif'/></div>",css:{ background: 'transparent'}}); 
	}
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



function deptPopForAccountList(){
	var urls = "/Accredit.do?popname=clientGroup&inputType=&inputType=radio" ;
	var	title = "请选择选客户联系人";	
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
　　　　　	}
　　　	}
　	});
}


//处理双击个人部门弹出框


function fillData(datas){
	#if($emailType=="")
	    $("#email").val(datas.split(";")[1]);
	#else
		$("#email").val(datas.split(";")[2]);
	#end
	jQuery.close('Popdiv');
}

</script>

</head>
<body style="background:#EEF9FF;">
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
<input type="hidden" name="exportType" value = "aio"/>
<input type="hidden" id="searchType" name="searchType" value = "false"/>
			
<div class="MailSearch" id="list_mainframe">
		<li style="float:left;margin-left:10px;">
		<div style="color:#666; padding-top:5px;font-family: 微软雅黑;">$!text.get("oa.common.currentStation")：










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
			</div>
		</li>
		<li><input id ="keyword" style="width:160px;height:20px;" name="keyword" type="text"  class="search_text" value="#if($!EMailSearchForm.keyword!="" && "$searchType" !="true")$!EMailSearchForm.keyword#else标题查询#end" onKeyDown="if(event.keyCode==13)keywordQuery();" onblur="if(this.value=='')this.value='标题查询';" 
			onfocus="if(this.value=='标题查询'){this.value='';}this.select();"/><input type="button" class="search_button" style="cursor: pointer;" onclick="javascript:keywordQuery();"/><button type="button" style="height: 24px;" onclick="showSearch();">$text.get("common.lb.Advancequery")</button></li>
</div>

<div class="toolbar" id="toolbar_id">
<div class="toolbat_left"></div>
<div class="toolbat_center">
<ul style="margin:10px 0 0 5px;padding:0;">
	#if("$!groupId" == "2" )
	<li class="toolbu_01" onClick="javascript:return update(); ">$text.get("common.lb.update")</li>
	#end
	#if("$!groupId" != "2" && "$!groupId" != "3" )
	<li class="toolbu_01" onClick="javascript:return submits('revert'); ">$text.get("oa.mail.restore")</li>
	<li class="toolbu_02" onClick="javascript:return submits('revertAll'); ">$text.get("oa.mail.restoreAll")</li>
	
	<!--<li onClick="javascript:return submits('revertAll'); "><img src="/style/images/mail/replayall.gif" ><span>$text.get("oa.mail.restoreAll")</span></li>-->
	#end
	#if("$!groupId" != "2")
	<li class="toolbu_01" onClick="javascript:return submits('transmit'); ">$text.get("oa.mail.transmit")</li>
	#if("$!emailType"=="1" ||"$!emailType"=="")
	<li class="toolbu_01" onClick="javascript:return submits('transmitOut'); " >$text.get("oa.mail.lb.trOut")</li>
	#else
	<li class="toolbu_01" onClick="javascript:return submits('transmitInt'); " >$text.get("oa.mail.lb.trIn")</li>
	#end
	#end	
	#if("$!groupId" != "2" && "$!groupId" != "3" )
	<li class="toolbu_01" onClick="javascript:return assign();">$text.get("email.lb.assign")</li>
	#end
	<li class="toolbu_01" onClick="javascript:return sureDel('keyId');">$text.get("common.lb.del")</li>
	<li class="toolbu_02" onClick="javascript:return sureDelChe('keyId');">$text.get("oa.mail.clear")</li>
	<li>
		<span class="toolbu_03" onClick="">
		<script type="text/javascript">
			#if($!groupId == "5")
				aioselectDown('sign',[],'-1','$text.get("email.lb.sign")','80','sign()');
			#else
				aioselectDown('sign',[{value:"1",name:"$text.get("email.lb.setread")"},{value:"0",name:"$text.get("email.lb.setnoread")"}],'-1','$text.get("email.lb.sign")','80','sign()');
			#end
		</script>
		</span>
		#if("$!groupId" != "2" && "$!groupId" != "3" && "$!emailType" !="assign")
		<span class="toolbu_04" onClick="">
		<script type="text/javascript">
			var dav = new Array();
			#if("$!groupId" != "1")
			dav[dav.length] ={value:"1",name:"$text.get("oa.mail.receive.box")"};
			#end
			#if("$!groupId" != "4")
			dav[dav.length] ={value:"4",name:"$text.get("oa.mail.dust")"};
			#end
			#if("$!groupId" != "5")
			dav[dav.length] ={value:"5",name:"$text.get("oa.mail.deleted")"};
			#end
			#foreach ($vg in $groups)	 		
			#if("$!groupId"!="$globals.get($vg,0)")
			dav[dav.length] ={value:"$globals.get($vg,0):$globals.get($vg,2)",name:"$globals.get($vg,1)"};
			#end
			#end
			aioselectDown('moveGroup',dav,'-1','$text.get("oa.mail.move")','80','move()');</script>
		</span>	
		#end
		<span class="toolbu_05" onClick="">
		<script type="text/javascript">
			var dav = new Array();
			#foreach($ll in $labelList)
			dav[dav.length] ={value:"$!ll",name:"<span style='width:15px;height:15px;background:#$!labelMap.get($!ll)'  ></span>&nbsp;$!ll"};
			#end  
			dav[dav.length] ={value:"",name:"&nbsp;$text.get("common.lb.mail.clearMark")"};	
			
			aioselectDown('setLabel',dav,'-1','$text.get("email.lb.label")','80','setLabel()');
		</script>
		</span>
		<span class="toolbu_06" onClick="">
		<script type="text/javascript">	
			aioselectDown('moreOperation',
			[{value:"startPrint",name:"$text.get("common.lb.print")"},{value:"export",name:"$text.get("oa.email.export")"},{value:"import",name:"$text.get("email.lb.import")"},
			{value:"stadexport",name:"$text.get("email.lb.stadexport")"},{value:"stadImport",name:"$text.get("email.lb.stadImport")"},{value:"movetotrash",name:"$text.get("emal.lb.trashMail")"}],'-1','更多','80','moreOperation()');
		</script>  
		</span>
	</li>
	<!-- 
		<li class="toolbu_01" onClick="startPrint(mailDetailFrame.oalistRange2)">$text.get("common.lb.print")</li>
		<li class="toolbu_07" onClick="movetotrash()">$text.get("emal.lb.trashMail")</li>
	 -->
	<li class="toolbu_01" onClick="javascript:alertSet()">$text.get("alertCenter.lb.awoke")</li>
	<li style="margin-left: 0px;text-align: left;"><span class="toolbu_04" style="width: 66px;" >
	<div style="margin-left: -5px;">
	<script type="text/javascript">
	var dav = new Array();
			dav[dav.length] ={value:"gbk",name:"切换为GBK"};
			dav[dav.length] ={value:"big5",name:"切换为BIG5"};
			dav[dav.length] ={value:"utf-8",name:"切换为UTF-8"};
			dav[dav.length] ={value:"iso8859-1",name:"切换为ISO"};
			dav[dav.length] ={value:"Shift_JIS",name:"切换为Shift_JIS"};
			dav[dav.length] ={value:"JIS",name:"切换为JIS"};
			dav[dav.length] ={value:"KSC5601",name:"切换为KSC5601"};
			aioselectDown('charset',dav,'-1','$text.get("email.lb.Garbled")','140','charset()');</script></div>
	</span></li>
</ul>
</div>
	<div class="toolbat_right">
		<img alt="$text.get('previous.email')" src="/style/images/bbs/prev.png"  onclick="prenext(true,'$!totalPage')"/>&nbsp;&nbsp;
		<img alt="$text.get('next.email')" src="/style/images/bbs/next.png" onClick="prenext(false,'$!totalPage')"/>
	</div>
</div>

<div style="width:100%;float:left;">
	<div class="list_left" id="listMailDiv">
		<div class="list_left_topleft"></div><div class="list_left_topright"></div>
		<div>
			<script type="text/javascript">
				function mySortTable(sTable, iCol, sDataType){
					sortTable(sTable, iCol, sDataType);
					document.getElementById('selAll').checked = false;
				}
			</script>
			<div class="maillisthead">
			<span style="float:left;">
				<input type="checkbox" name="selAll" id="selAll" onClick="checkAll('keyId')" />
				$text.get("common.lb.selectAll")
			</span>
			
			<span class="listhead_down" >
			<!--<span style="height:100%;padding-top:5px;vertical-align:top">
			#if("$!EMailSearchForm.isdesc"=="true")
			<img border="0" id="isdescimg" onClick="isdescimgclick()" src="/style/images/mail/desc.gif"/>
			</span>
			#else
			<img border="0" id="isdescimg" onClick="isdescimgclick()" src="/style/images/mail/asc.gif"/></span>
			#end 		-->
			<script type="text/javascript">aioselectMail('orderby',[{value:'0',name:'$text.get("email.lb.sortDate")'},{value:'1',name:'$text.get("email.lb.sortfrom")'},
	{value:'2',name:'$text.get("email.lb.sorttitle")'},{value:'3',name:'$text.get("email.lb.sortsize")'}],'$!EMailSearchForm.orderby','87','orderByChange()');</script>	
			</span>
			<span class="listhead_down" >
			<script type="text/javascript">
			aioselectMail('view',[{value:'0',name:'$text.get("email.lb.viewall")'},{value:'1',name:'$text.get("common.lb.view")$text.get("email.lb.noreaded")'},
	{value:'2',name:'$text.get("common.lb.view")$text.get("email.lb.readed")'},{value:'3',name:'$text.get("common.lb.view")$text.get("email.lb.replay")'},{value:'4',name:'$text.get("common.lb.view")$text.get("email.lb.traned")'},{value:'5',name:'$text.get("common.lb.view")$text.get("email.lb.assigned")'}],
		'$!EMailSearchForm.view','87','viewChange()');</script>	
		</span>
</div>		
<div id="oalistRange" style="width:99%;height:100%;overflow-x:hidden;margin-top:0px;padding:0px;" >
<script type="text/javascript">
	var oDiv=document.getElementById("oalistRange");
	var sHeight=document.getElementById("list_mainframe").clientHeight;
	var tHeight=document.getElementById("toolbar_id").clientHeight ;
	var dHeight=document.documentElement.clientHeight-92;
	oDiv.style.height=dHeight-sHeight-tHeight+"px";
</script>
			<table style="width:100%;" border="0" cellpadding="0" class="maillisttable" cellspacing="0" id="tblSort" name="table" >
			<tbody>			
	#foreach ($info in $result)
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
	
	<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);" onclick="javascript:clickMail('$!info.id','$!info.mailTitle.replace("'","").replaceAll("\"","")')" 
	title="
$text.get("oa.mail.addresser"):$mailN
$text.get("email.lb.recDate"):$!info.mailTime#if("$!info.replayDate" !="")
$text.get("email.lb.repDate"):$!info.replayDate#end#if("$!info.revolveDate" !="")
$text.get("email.lb.tranDate"):$!info.revolveDate#end 

$text.get("oa.subjects"):$!info.mailTitle
$text.get("email.lb.enclosure.size"):$!info.mailSize K #if("$!info.labelId"!="")
$text.get("email.lb.label")::$!info.labelId #end
	$assignN
	" oncontextmenu = "popMenu('$!info.id')"
	#if("$!info.state" == "0") style='font-weight:700' #end
	>
	<td valign="top" width="20">			
	<input  type="checkbox" name="keyId" value="$!info.id" id="CK_$!info.id"  emailType="$!info.account" moreMail="#if($!emailType !="assign")$!emailType#end"
	#if("$!groupId"!="2" && "$!groupId"!="3")
	email="$globals.getMailAddress($!info.mailFrom)" eName="$globals.getMailName($!info.mailFrom)" 
	contactId="$!globals.get($!noteMap.get($globals.getMailAddress($!info.mailFrom)),1)" clientId="$globals.get($!clientMap.get($globals.getMailAddress($!info.mailFrom)),2)"  clientName="$globals.get($!clientMap.get($globals.getMailAddress($!info.mailFrom)),0)"
	#else
	email="$globals.getMailAddress($!info.mailTo)" eName="$globals.getMailName($!info.mailTo)" 
	contactId="$!globals.get($!noteMap.get($globals.getMailAddress($!info.mailTo)),1)" clientId="$globals.get($!clientMap.get($globals.getMailAddress($!info.mailTo)),2)" clientName="$globals.get($!clientMap.get($globals.getMailAddress($mailN)),0)"
	#end
	accept = "$!info.mailTitle"	
	/>
	</td>
	<td width="*">
	<div style="width:20px;padding-left:3px; float:left;">
	#if("$!info.state"=="0")
	<img src="/style/images/mail/unread.gif" id="IM_$!info.id" alt="$text.get("oa.mail.notRead")"/>
	#else  
	<img src="/style/images/mail/read.gif" id="IM_$!info.id" alt="$text.get("oa.mail.readed")"/>
	#end</div>
	<div class="mc" style="font-weight:bold">$mailN </div>
	<div class="mc" style="float:left; margin-left:3px; color:#606060" >#if("$!info.mailTitle" == "") $text.get("oa.mail.no") #else $!info.mailTitle&nbsp; #end</div>	
	</td>
	<td width="80" align="right" style="padding-right:10px">
	<span style="width:70px; color:#A5A5A5;display:block;" >
	#if("$!info.mailTime.substring(0,10)" =="$globals.getDate()") $!info.mailTime.substring(11) #else $!info.mailTime.substring(0,10) #end</span>
	<span class="ma">
	#if("$!info.labelId"!="")
		#foreach($labelrow in $!info.labelId.split(";|,|\\s"))
		#if("$!labelrow" != "")
		#if("$!labelMap.get($!labelrow)" != "")
		<span style="width:7px;height:13px;background:#$!labelMap.get($!labelrow)" title="$labelrow"></span>
		#else
		<span style="width:7px;height:13px;background:#000000" title="$labelrow"></span>
		#end
		#end
		#end
	#end
	
	#if("$!assignN"!="") <img src="/style/images/mail/assign.gif" alt="$!assignN" class="si"/>#end
	#if("$!info.mailAttaches" != "") <img border=0 src="/style/images/mail/attach.gif" />#end&nbsp;</span>
	</td> 			   
	</tr>
	#end
			  </tbody>
			</table>
			
		</div>
	</div>
		<div class="listRange_pagebar" style="width:100%;margin-left:10px ">$!pageBar</div> 
		<div class="list_left_bottomleft"></div><div class="list_left_bottomright"></div>
	</div>
	<div class="fgx" id="moveId"></div>
	
	<div id="oaMailDetail" class="list_right">
		<iframe id="mailDetailFrame" name="mailDetailFrame" src=""  frameborder="false" scrolling="no" class="list" width="100%" height="100%" frameborder="no"></iframe>	
	</div>
<script type="text/javascript">
	var oDiv=document.getElementById("oaMailDetail");
	var dWidth=document.documentElement.clientWidth-310;
	oDiv.style.width = dWidth+"px";
</script>
</div>
	<script type="text/javascript">
	var oDiv=document.getElementById("oaMailDetail");
	var sHeight=document.getElementById("list_mainframe").clientHeight;
	var dHeight=document.documentElement.clientHeight-81;
	oDiv.style.height=dHeight-sHeight+"px";
	document.getElementById("moveId").style.height=dHeight-sHeight+"px";
	</script>
	
	<div id="highSearch" class="search_popup" style="display:none;width: 470px;height: 160px;z-index: 99;">
			<div onmousedown="MDown(highSearch)" style="cursor: move; width: 460px;"   >
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
					 	<input  id="email" name="email" #if("$!clearSerchForm"!="yes") value="$!EMailSearchForm.email" #end onDblClick="deptPopForAccountList();"/>
						<img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForAccountList();" />
					#end
				</li> 
			#end		
			#if("$!groupId"=="2"||"$!groupId"=="3")
		 		 <li><span>$text.get("oa.mail.addressee"):</span>
				    #if($emailType=="") 
						 <input  id="email" name="email" #if("$!clearSerchForm"!="yes") value="$!EMailSearchForm.email" #end onDblClick="deptPopForAccount('userGroup','email','createBy');" />
						 <img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForAccount('userGroup','email','createBy');" />		
				    #else
						 <input  id="email" name="email" #if("$!clearSerchForm"!="yes")value="$!EMailSearchForm.email" #end onDblClick="deptPopForAccountList();" />
						 <img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForAccountList();" />
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
				<button name="Submits" type="button"  class="sb" onclick="searchSubmit()">$text.get("common.lb.query")</button>
				<button name="cancel" type="button"  class="sb" onclick="showSearch()">$text.get("common.lb.close")</button>
			</li>
	  </div>
</form>
</body>
</html>
<script type="text/javascript">
	
	parent.leftFrame.refreshStat();
	items = document.getElementsByName('keyId');
	if(items.length>0){
		if("$!curMailId" != ""){
			for(ii = 0;ii<items.length;ii++){
				if(items[ii].value == "$!curMailId"){
					items[ii].checked =true;
					items[ii].focus(); 
					clickMail(items[ii].value,items[ii].accept);
					break;
				}
			}
		}else if("$!isprev" == "true"){
			items[items.length-1].checked =true;
			items[items.length-1].focus();
			clickMail(items[items.length-1].value,items[items.length-1].accept);
		}
	}   
	function charset(){
		document.frames("mailDetailFrame").document.form.type.value="charset";
		document.frames("mailDetailFrame").document.form.submit();
	}

</script>