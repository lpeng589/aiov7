<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.mail.IndexPage")</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<link type="text/css" rel="stylesheet" href="/style/css/email.css" />
<link rel="stylesheet" href="/style/css/mail.css" type="text/css"/>
<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css"  />
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>		
<script language="javascript" src="$globals.js("/js/email.vjs","",$text)"></script>
<style type="text/css">
@media print{
	#title_div{display: none}
}
</style>
<script language="javascript">
//查看邮件的阅读状态 by dq 090605
function read_status_old(groupId){
  URL="/EMailAction.do?operation=4&pId=$!result.id&emailTitle=$globals.encode($!result.mailTitle)&type=readStatus&groupId="+groupId;
  myleft=(screen.availWidth-400)/2;
  mytop=250
  mywidth=400;
  myheight=300;
  window.open(URL,"read","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}

function read_status(groupId){
	var urls = "/EMailAction.do?operation=4&pId=$!result.id&emailTitle=$globals.encode($!result.mailTitle)&type=readStatus&groupId="+groupId;
	asyncbox.open({
		 title : "邮件状态",
	　　　url : urls,
	　　　cache : true,
	　　　width : 530,
	　　　height : 350,
		 btnsbar : jQuery.btn.CANCEL,
		 callback : function(action,opener){
　　　　　	if(action == 'ok'){
　　　　　	}
　　　	}
　	});
}

function alertSet(relationId,curMailTitle){
	var urls=encodeURIComponent('/EMailAction.do?operation=5&noback=true&keyId='+relationId);
	var typestr=encodeURIComponent('$text.get("common.email.alert")'+curMailTitle);
	var title=encodeURIComponent('$text.get("oa.mail.myMail")');
	var date= new Date();
	var url = "/UtilServlet?operation=alertDetail&detailType=true&relationId="+relationId+"&falg=true"+"&title="+title+"&typestr="+typestr+"&urls="+urls+"&date="+date;
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

function refreshIframe(){
	jQuery.close('warmsetting');
	window.location.reload();
}

function dealAsyn(){
   refreshIframe();
}

function cancelAlert(relationId){
	jQuery.ajax({
	   type: "POST",
	   url: "/UtilServlet?operation=deleteAlert&relationId="+relationId,
	   success: function(msg){
	   		alert("$text.get('tel.msg.cancelSuccess')")
			window.location.reload();			
	   }
	});
}

if(typeof(top.jblockUI)!="undefined")top.junblockUI();
function doBack(){
	var fromPage = '$!request.getParameter("fromPage")';
	if(fromPage == "main"){
		closeWin();
	}else{
		window.location.href='/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=$!moreMail&type=main&groupId=$groupId';	
	}
	
}

function clearLabel(str){
	window.location.href="/EMailQueryAction.do?operation=$!globals.getOP("OP_UPDATE")&emailType=$emailType&keyId=$result.id&clearLabel="+encodeURI(str);
}

function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}
#if("$!result.begReplay" == "1" && "$!result.userId" !="$!result.fromUserId")
	if(confirm("$text.get("email.msg.begReplay")")){
		jQuery.get("/EMailAction.do", {operation: "$!globals.getOP('OP_UPDATE')", begReplay: true,keyId:"$!result.id" });
	}else{
		jQuery.get("/EMailAction.do?operation=$!globals.getOP('OP_UPDATE')&noBegReplay=true&keyId=$!result.id"); 
	}
#end

function charset(){
	document.form.type.value="charset";
	document.form.submit();
}

//收藏功能
function Mailcollection(mailId,obj){
	//默认属性


	var imgSrc = "/style/images/plan/tuopiao_no.gif";
	var title = "加入收藏";
	var collectionType = "0";
	var tips = '$text.get("aio.cancel.favoriteSuccess")';

	var url = "/EMailQueryAction.do?operation=2&collectionMail=collection&mailId=" + mailId;
	if($(obj).attr("collectionType") == "1"){
		//已收藏，取消
		url += "&collectionType=0";
	}else{
		tips = '$text.get("aio.add.favorite")';
		url += "&collectionType=1";
		imgSrc = "/style/images/plan/tuopiao_yes.gif";
		title = "取消成功";
		collectionType = "1";
		
	}	
	jQuery.ajax({
	   type: "POST",
	   url: url,
	   data: "",
	   success: function(msg){
	   		if(msg == "ok"){
	   			 asyncbox.tips(tips,'success');
	   			 $(obj).find("img").attr("src",imgSrc);
 	   			 $(obj).find("img").attr("title",title);
 	   			 $(obj).attr("collectionType",collectionType);
 	   			 var flag = "del";	 
	 			 if($(obj).attr("collectionType") == "1"){
	 				flag="add";
	 			 }
	 			 addColletion(mailId,flag);
	   		}else{
	   			asyncbox.tips('操作失败','error');
	   		}
	   }
	});
}
/*wyy*/
function addColletion(oTopicId,flag){	
	 var url = "/OACollectionAction.do?operation=2&attType="+flag+"&typeName=OAMail&keyId="+oTopicId;
	 jQuery.ajax({
	 	 type: "POST",
	  	 url: url,	
	  	 data:{
	  	 	urlparam:"/EMailAction.do?operation=5&keyId=$!result.id&groupId=$!result.groupId&emailType=$!result.emailType&noback=true&detailType=true",
	  	 	titleName:"邮件:$!result.mailTitle"
	  	 }
	});
}
//关联客户
var mailIdc="";
var cNamec="";
var emailc="";
function contactClient(mailId,clientId,cName,email,clientName){

	mailIdc=mailId;
	cNamec=cName;
	emailc=email;
	if(clientId == ""){
		openSelect1('客户名称','ClientNo','ClientName');
	}else{
		detail_client(mailId,clientId,cName,email,clientName);
	}
}

function newClient(mailId,cName,email){
	detail_client(mailId,'',cName,email,'');
}

//客户弹出框


function openSelect1(displayName,obj,obj2){
	//var str  = window.showModalDialog(urlstr+"&MOID=a30d569a_1012222149354060240&MOOP=query&LinkType=@URL:&displayName="+displayName,"","dialogWidth=730px;dialogHeight=450px"); 
	var urls = "/UserFunctionAction.do?operation=22&selectName=ReportSelectClient&MOID=a30d569a_1012222149354060240&MOOP=query&popupWin=Popdiv&LinkType=@URL:&displayName="+encodeURI(displayName)
	asyncbox.open({id:'Popdiv',title:displayName,url:urls,width:750,height:470});
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

function selfdel(){
	if(!confirm('$text.get("common.msg.confirmDel")')){return false;}
	var url='/EMailQueryAction.do?operation=$!globals.getOP("OP_DELETE")&emailType=$!emailType&type=main&groupId=$groupId&keyId=$keyId&noback=true';
	jQuery.ajax({
		   type: "POST",
		   url: url,
		   success: function(msg){
		     if(msg=="ok"){
		    	 alert("删除成功");
		    	 closeWin();
		    	 window.location.href='/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=$!emailType&type=main&groupId=$groupId';
		     }else{
		    	 alert("删除失败");
		     }
		   }
		});
}

function selfclear(){
	if(!confirm('$text.get("common.msg.confirmDel")')){return false;}
	url='/EMailQueryAction.do?operation=$!globals.getOP("OP_DELETE")&emailType=$!emailType&type=main&groupId=$groupId&keyId=$keyId&deleteReal=true&noback=true';
	jQuery.ajax({
		   type: "POST",
		   url: url,
		   success: function(msg){
			   if(msg=="ok"){
			    	 alert("删除成功");
			    	 closeWin();
			    	 window.location.href='/EMailQueryAction.do?operation=$!globals.getOP("OP_QUERY")&emailType=$!emailType&type=main&groupId=$groupId';
			     }else{
			    	 alert("删除失败");
			     }
		   }
		});
}


function closeRemark(){
	if(old_val != ""){
		showRe('1');
		return false;
	}
	$("#remark_a").show();
	$("#remarks").hide();
}

function showRemark(){
	$("#remark_a").hide();
	$("#remarks").show();
	$("#remark").focus();
}
var old_val = "";
function saveRemark(type){
	var remark = $("#remark").val();
	if(type == "del"){
		if(!confirm('是否删除备注！')){return false;}
		remark = "";
	}else if(type == "update"){
		showRe('');
		old_val = "update";
		return false;
	}else{
		if(remark==""){
			asyncbox.tips("备注不能为空！",'alert',1500);
			$("#remark").focus();
			return false;
		}
	}
	old_val = "";
	jQuery.ajax({
	   type: "POST",
	   url: "/EMailAction.do?operation=2&collectionMail=dealRemark&id=$!result.id&content="+encodeURI(remark),
	   success: function(msg){
		   if(msg=="1"){
		   		if(type == "del"){
		   			$("#remark").val("");
		   			closeRemark();
			   		asyncbox.tips("备注删除成功！",'alert',1500);
		   		}else{
		   			showRemark();
		   			asyncbox.tips("操作备注成功！",'alert',1500);
		   		}
		   		showRe($("#remark").val());
		   }
	   }
	});
}

function showRe(remark){
	if(remark==""){
		$("#remark").focus();
		$("#remark").removeAttr("readonly").css("background-color","#FFFFFF");
		$("#btn_remark1").show();
		$("#btn_remark2").hide();
	}else{
		$("#remark").focus();
		$("#remarks").show();
		$("#remark_a").hide();
		$("#remark").attr("readonly","true").css("background-color","#FFF5CF");
		$("#btn_remark1").hide();
		$("#btn_remark2").show();
	}
}

$(document).ready(function (){
	showRe('$!result.remark');
	$(".d-menu-li").hover(
		function(){$(this).find(".in-nav-ul").show();},
		function(){$(this).find(".in-nav-ul").hide();}
	);
});


/* 返回列表 */
function backs(){
	window.location.href="/EMailQueryAction.do?operation=4&emailType=$!result.account&type=main&groupId=$!groupId";
}


/* 跳转 */
function breakMail(type){
	var url = "/EMailAction.do?operation=$!globals.getOP("OP_ADD_PREPARE")&emailType=$!result.account&moreMail=$!moreMail&type="+type+"&groupId=$groupId&keyId=$keyId";
	#if("$!iframe" !="true")
		url += "&noback=true";
	#end
	window.location.href = url;
}

function selectChange(methodName,fieldName,fieldValue){
	$("#"+fieldName).val(fieldValue);
	$("#type").val('');
	setTimeout(methodName,100);
}

function mailprint(){
	var sHeight = $("#oalistRange2").height();
	$("#oalistRange2").height("100%");
	document.getElementById("pcontent").height="100%";
	window.print();
	$("#oalistRange2").height(sHeight);

}

/* 上一条,下一条 */
function preNext(id,createTime){
	window.location.href="/EMailAction.do?operation=$globals.getOP("OP_DETAIL")&groupId=$groupId&emailType=$!result.account&detailType=$!detailType&keyId="+id+"&createTime="+createTime;
}


</script>
</head>
<body>
<form  method="post" scope="request" name="form" action="/EMailAction.do">
<input type="hidden" name="operation" value="$globals.getOP("OP_DETAIL")"/>
<input type="hidden" name="emailId" value="$!result.id" />
<input type="hidden" name="keyId" value="$!result.id" />
<input type="hidden" name="type" id="type" value="" />
<input type="hidden" name="iframe" value="$!iframe" />
<input type="hidden" name="emailType" value="$!result.account" />
<input type="hidden" name="emailTitle" value="$!result.mailTitle"/>
#if($!request.getParameter("fromPage")=="main")
<input type="hidden" name="noback" value="true"/>
#set($noback=true)
#else
<input type="hidden" name="noback" value="$!noback"/>
#end
<div id="mailDetail">
<div id="title_div">
<div class="MailSearch" id="list_mainframe" style="height:36px;">
	<li style="float:left;margin-left:10px;">
		<div style="color:#3C78AA;;font-weight:bold;font-size:15px;">
			&nbsp;&nbsp;详情
		</div>
	</li>
</div>
<div style="border-bottom: 1px solid #cccccc;height: 30px;">
	<div class="toolbat_center HeadingButton" style="padding-top:0px;">
		<ul>
			#if(!$noback)
			<li><span class="btn-w" onclick="backs()">返回</span></li>
			#end
			#if("$!result.groupId" == "1" )
			<li><span class="btn-w" onclick="breakMail('revert');">$text.get("oa.mail.restore")</span></li>
			<li><span class="btn-w" onclick="breakMail('revertAll');">$text.get("oa.mail.restoreAll")</span></li>
			#end
			#if("$!result.groupId" == "1" || "$!result.groupId" == "3")
			<li><span class="btn-w" onclick="breakMail('transmit');">$text.get("oa.mail.transmit")</span></li>
			#end
			<li><span class="btn-w" onclick="selfdel()">$text.get("common.lb.del")</span></li>
			<li><span class="btn-w" onclick="selfclear()">$text.get("oa.mail.clear")</span></li>
			<li class="d-menu-li">
				<input type="hidden" value="-1" name="charset" id="charset" />
				<div class="btn-w h-child-btn">
					<em id="tb_mr1" onclick="">$text.get("email.lb.Garbled")?</em>
					<b class="triangle"></b>
				</div>
				<ul class="in-nav-ul" style="min-width:100px;">
					<li onclick="selectChange('charset()','charset','gbk')">$text.get("email.lb.switch")GBK</li>
					<li onclick="selectChange('charset()','charset','big5')">$text.get("email.lb.switch")BIG5</li>
					<li onclick="selectChange('charset()','charset','utf-8')">$text.get("email.lb.switch")UTF-8</li>
					<li onclick="selectChange('charset()','charset','iso8859-1')">$text.get("email.lb.switch")ISO</li>
					<li onclick="selectChange('charset()','charset','Shift_JIS')">$text.get("email.lb.switch")Shift_JIS</li>
					<li onclick="selectChange('charset()','charset','JIS')">$text.get("email.lb.switch")JIS</li>
					<li onclick="selectChange('charset()','charset','KSC5601')">$text.get("email.lb.switch")KSC5601</li>
				</ul>
			</li>
			<li class="d-menu-li">
				<input type="hidden" value="-1" name="moreOperation" id="moreOperation" />
				<div class="btn-w h-child-btn">
					<em id="tb_mr1" onclick="">更多</em>
					<b class="triangle"></b>
				</div>
				<ul class="in-nav-ul" style="min-width:100px;">
					<li onclick="selectChange('moreOperation()','moreOperation','startPrint')">$text.get("common.lb.print")</li>
				</ul>
			</li>
			#if($noback)
				<li><span class="btn-w" onclick="closeWin();">$text.get("common.lb.close")</span></li>
			#end
		</ul>
	</div>
	<div style="float: right;margin-right: 40px;padding-top: 6px;">
		<span>
		#if("$!preData"!="")
			<img src="/style/images/bbs/prev.png" style="cursor: pointer;" onclick="preNext('$!preData.id','$!preData.createTime')" title="上一封"/>
		#end
		</span>
		<span style="padding-left: 20px;">
		#if("$!nextData"!="")
			<img src="/style/images/bbs/next.png" style="cursor: pointer;" onclick="preNext('$!nextData.id','$!nextData.createTime')" title="下一封"/>
		#end
		</span>
	</div>
</div>

<ul style="margin:4px;margin-top:2px; width:100%;float:left;">
	<li style="float:left; width:95%">
      <span style="float:left; display:inline-block; width:95%;">
		#if("$!assignMap.get($!result.id)" != "") 
			#set($assignN = "")
			#foreach($assrow in $!assignMap.get($!result.id))
				#set($assignN = $assignN + $globals.get($assrow,1)+",")
			#end
			#set($assignN = $globals.get($!assignMap.get($!result.id).get(0),3)+$text.get("email.lb.assignTo") + $assignN + $globals.get($!assignMap.get($!result.id).get(0),2))
		
			$assignN
		#end	
		#if("$!result.labelId"!="")&nbsp;$text.get("email.lb.label"):
			#foreach($labelrow in $!result.labelId.split(";|,|\\s"))
				#if("$!labelrow" != "")
					#if("$!labelMap.get($!labelrow)" != "")
		      			</span>
						<span style="width:80px;height:18px;color:#3399CC;padding-top:3px;float:left; display:inline-block;background:#$!labelMap.get($!labelrow)" >
						<a href="javascript:clearLabel('$!labelrow')"><img style="float:right" src="/style/images/mail/close.gif"/></a>
						<span style="float:right;width:55px;overflow:hidden;white-space:nowrap;">$labelrow</span></span>
					#else
						<span style="width:80px;height:18px;color:#ffffff;padding-top:3px;float:left; display:inline-block;background:#000000" >
						<a href="javascript:clearLabel('$!labelrow')"><img style="float:right" src="/style/images/mail/close.gif"/></a>
						<span style="float:right;width:55px;overflow:hidden;white-space:nowrap;">$labelrow</span></span>
					#end
				#end
			#end
		#end
	</li>
</ul>
</div>
<div id="oalistRange2" style="width: 98%;">
<script type="text/javascript"> 

var oDiv=$("#oalistRange2");
#if("$!iframe" =="true")
var sHeight=document.documentElement.clientHeight-90;
#elseif("$!newOpen" =="true")
var sHeight=document.documentElement.clientHeight-55;
#else
var sHeight=document.documentElement.clientHeight-65;
#end
oDiv.height(sHeight) ;
</script>
	<ul> 
		<li style="font-weight:bolder;color:#606060; font-size:16px;"><span>$text.get("oa.subjects")：</span>
				#if("$!result.mailTitle" == "") $text.get("oa.mail.no") #else  $!result.mailTitle &nbsp; #end  
				#if("$!assignMap.get($!result.id)" == "") 
					<a href="#" onclick="Mailcollection('$!result.id',this)" collectionType="$!result.collectionType"> 
					#if("$!result.collectionType" == "1")
						<img src="/style/images/plan/tuopiao_yes.gif" title="$text.get('aio.cancel.favorite')"/>
					#else
						<img src="/style/images/plan/tuopiao_no.gif" title="$text.get('aio.add.favorite')"/>    
					#end	
					</a>
				#end
				#if("$!alert"!="")
					<a href="#" onclick="alertSet('$!result.id','$!result.mailTitle')"><img src="/style/images/plan/time.gif" title="$text.get('mywork.lb.checkWarm')"/> </a>
				#end
				<a href="#" onclick="showRemark()" id="remark_a" > 
					<img src="/style/images/mail/black.gif" title="加备注"/>
				</a>
				#if("$!detailType" == "false")	
				<div style="float: right;margin-right: 40px;">
					<a href="#" onclick="javascript:mdiwin('/EMailAction.do?operation=5&keyId=$!result.id&groupId=$!result.groupId&emailType=$!result.emailType&noback=true&detailType=true&createTime=$!result.createTime','我的邮件')"><span style="font-size: 15px;"><img src="/style/images/mail/mail_detail.jpg" title=" $text.get('common.lb.detail')"/></span></a>  
				</div>
				#end		
	   </li>
			#if("$!result.groupId" != "2" && "$!result.groupId" != "3")
			#if("$!emailType"!="" && "$!emailType" != "inner")
				#if("$!clientMap.get($globals.getMailAddress($!result.mailFrom))" != "")
					#set($mailN = $globals.get($!clientMap.get($globals.getMailAddress($!result.mailFrom)),0)+"-"+$globals.get($!clientMap.get($globals.getMailAddress($!result.mailFrom)),1))  		
					#if("$!hide"!="true")
						#set($mailN = $mailN+"&lt;"+$globals.getMailAddress($!result.mailFrom)+"&gt;") 
					#end  		
				#elseif("$!noteMap.get($globals.getMailAddress($!result.mailFrom))" != "")
					#set($mailN = $globals.get($!noteMap.get($globals.getMailAddress($!result.mailFrom)),0))  		
					#if("$!hide"!="true")
						#set($mailN = $mailN+"&lt;"+$globals.getMailAddress($!result.mailFrom)+"&gt;") 
					#end  		
				#else
					#set($mailN = $globals.encodeHTML($!result.mailFrom)) 
					#if("$!hide"=="true")
						#set($mailN = $text.get("com.new.client")) 
					#end   	
				#end 
			#else
			#set($mailN = $globals.encodeHTML($!result.mailFrom)) 
			#end  	
				<li><span>$text.get("oa.mail.sendPeople")：</span>
			#if("$!clientMap.get($globals.getMailAddress($!result.mailFrom))" != "")
				<img src="/style/images/mail/client.gif" height=13/>	
			#elseif("$!noteMap.get($globals.getMailAddress($!result.mailFrom))" != "")
				<img src="/style/images/mail/contant.png" height=13/>		
			#end
				$mailN &nbsp;&nbsp; 
				#if("$!emailType"!="" && "$!emailType" != "inner")
					#if("$LoginId" == "1" || $LoginBean.operationMap.get("/OACommunication.do").add())
					<!-- <a href="javascript:detail_contact('$!result.id','$!globals.get($!noteMap.get($globals.getMailAddress($!result.mailFrom)),1)','$globals.getMailName($!result.mailFrom)','$globals.getMailAddress($!result.mailFrom)')">$text.get("oa.related")$text.get("oa.addresslist")</a>&nbsp;&nbsp;
					&nbsp;&nbsp; -->
					#end
					#if("$LoginId" == "1"  || $LoginBean.operationMap.get("/CRMClientAction.do").add())
					<a href="javascript:contactClient('$!result.id','$globals.get($!clientMap.get($globals.getMailAddress($!result.mailFrom)),2)','$globals.getMailName($!result.mailFrom)','$globals.getMailAddress($!result.mailFrom)','$globals.get($!clientMap.get($globals.getMailAddress($!result.mailFrom)),0)')">$text.get("calendar.lb.RelationKeHu")</a>&nbsp;&nbsp;
					&nbsp;&nbsp;
					#if("$globals.get($!clientMap.get($globals.getMailAddress($!result.mailFrom)),2)"=="")
					   <a href="javascript:newClient('$!result.id','$globals.getMailName($!result.mailFrom)','$globals.getMailAddress($!result.mailFrom)')">新客户</a>&nbsp;&nbsp;
					#end
					&nbsp;&nbsp;
					#end
				#end	
				</li>
				#end
				#if($groupId == 3)
					<!-- <span style="margin-left: 10px;font-size: 12px;"><a class="A1" href="javascript:read_status('3','');">查看邮件状态</a></span> -->
				#end
				#if("$!result.mailTo" != "")
	<li ><span>$text.get("oa.mail.addressee")：</span>
	   <!-- 收件人（receives包括所有收件人和密送 抄送人员 当len大于0说明是内部发件箱 -->
	  		#set($len = $receives.size())
	  		#if($len > 0)
	  			#set($list=$globals.encodeHTML($!result.mailTo).split(";"))
	  			#set($listSize = 0)
	  			#set($size = 0)
  				#foreach($mailTo in $list)
  					#set($listSize = $listSize+1)
  				#end
  				#set($showNum=0)
			  	#foreach($mailTo in $list)
			  			 #set($showNum = $showNum+1)
				  		 #set($num=0)
				  		 #if($showNum < 9)
					  		 #foreach($rev in $receives)
					  		 		
					  		 		<!-- 如果人名相同 显示人民 -->
						  			#if($globals.get($rev,0) == $mailTo)
						  				<!-- 匹配了 -->
						  				<u #if($groupId != 3)title="部门：$globals.get($rev,1)" #end style="cursor:inherit">
						  					#if($globals.get($rev,3) == 1)
						  						<img src='/style/images/email_open.gif' alt='收件人已读'/>$mailTo
						  					#elseif($globals.get($rev,3) == 0)
						  						<img src='/style/images/email_close.gif' alt='收件人未读'/>$mailTo
						  					#else
						  						<img src='/style/images/email_delete.gif' alt='收件人未读,发件人收回'/>$mailTo
						  					#end
						  					
						  					#if($size+1 < $listSize)
						  						,
						  					#else
						  						<!-- 如果是发件箱  -->
						  						<!-- 
						  						#if($groupId == 3)
						  							&nbsp;&nbsp;<a class="A1" href="javascript:read_status('3','');">查看邮件状态</a>
						  						#else
						  							&nbsp;&nbsp;<a class="A1" href="javascript:read_status('','');">全部详情</a>
						  						#end
						  						 -->
											#end
						  					#set($size = $size+1)
						  					
						  				</u>
						  		 	#elseif($globals.get($rev,0) != $mailTo&&($globals.get($rev,1) == $mailTo||$globals.get($rev,4) == $mailTo))
						  		 		
						  		 	 	#if($num==0)
								  		      <u #if($groupId != 3)title="部门：$mailTo" #end style="cursor:inherit">
								  		      	 <img src = "/style/images/unknown_email.gif" alt = "部门需点击详情查看"/>$mailTo
								  		      	 #if($size+1 < $listSize)
						  							
						  						 #else
							  						<!-- 如果是发件箱  -->
							  						<!-- 
							  						#if($groupId == 3)
							  							&nbsp;<a class="A1" href="javascript:read_status('3','');">查看邮件状态</a>
							  						#else
							  							&nbsp;<a class="A1" href="javascript:read_status('','');">全部详情</a>
							  						#end
							  						-->
							  					 #end	
								  		    
								  		      </u>
								  		      #set($size = $size+1)
						  		 	 	#end
						  		 	 	#set($num=$num+1)
						  		 	#end
					  		 #end
					  	#elseif($showNum==9)
					  		<span style="margin-left: 10px;font-size: 12px;"><a class="A1" href="javascript:read_status('3','');">查看更多</a></span>
				  		
				  		#end
				  		
			  	#end
			  
		  	#else
		  		 $globals.encodeHTML($!result.mailTo)
		  	#end
		  	#if($listSize<9)
				<span style="margin-left: 10px;font-size: 12px;"><a class="A1" href="javascript:read_status('3','');">查看更多</a></span>
			#end	
	 </li>
	#end
	
	#if("$!result.mailCc" != "")
	<li><span>$text.get("oa.mail.Cc")：</span>
	  <!-- 收件人（receives包括所有收件人和密送 抄送人员 当len大于0说明是内部发件箱 -->
	  		#if($len > 0)
	  			#set($listCC=$globals.encodeHTML($!result.mailCc).split(";"))
	  			#set($listSizeCC = 0)
	  			#set($sizeCC = 0)
  				#foreach($mailCC in $listCC)
  					#set($listSizeCC = $listSizeCC+1)
  				#end
  				#set($showNumCC = 0)	
			  	#foreach($mailCC in $listCC)
			  		 #set($numCC=0)
				  	 #set($showNumCC = $showNumCC+1)
				  	 #if($showNumCC < 9)	 
					  		 #foreach($rev in $receives)
					  		 		<!-- 如果人名相同 显示人民 -->
						  			#if($globals.get($rev,0) == $mailCC)
						  				<!-- 匹配了 -->
						  				<u #if($groupId != 3)title="部门：$globals.get($rev,1)"#end style="cursor:inherit">
						  					#if($globals.get($rev,3) == 1)
						  						<img src='/style/images/email_open.gif' alt='收件人已读'/>$mailCC
						  					#elseif($globals.get($rev,3) == 0)
						  						<img src='/style/images/email_close.gif' alt='收件人未读'/>$mailCC
						  					#else
						  						<img src='/style/images/email_delete.gif' alt='收件人未读,发件人收回'/>$mailCC
						  					#end
						  					
						  					#if($sizeCC+1 < $listSizeCC)
						  						
						  					#else
						  						<!-- 如果是发件箱  -->
						  						<!-- 
						  						#if($groupId == 3)
						  							&nbsp;&nbsp;<a class="A1" href="javascript:read_status('3','');">查看邮件状态</a>
						  						#else
						  							&nbsp;&nbsp;<a class="A1" href="javascript:read_status('','');">全部详情</a>
						  						#end
						  						-->
											#end
						  					#set($sizeCC = $sizeCC+1)
						  					
						  				</u>
						  		 	#elseif($globals.get($rev,0) != $mailCC&&($globals.get($rev,1) == $mailCC||$globals.get($rev,4) == $mailCC))
						  		 	 	#if($numCC==0)
								  		      <u #if($groupId != 3)title="部门：$mailCC"#end style="cursor:inherit">
								  		      	 <img src = "/style/images/unknown_email.gif" alt = "部门需点击详情查看"/>$mailCC
								  		      	 #if($sizeCC+1 < $listSizeCC)
						  						 #else
							  						<!-- 如果是发件箱  -->
							  						<!-- 
							  						#if($groupId == 3)
							  							&nbsp;<a class="A1" href="javascript:read_status('3','');">查看邮件状态</a>
							  						#else
							  							&nbsp;<a class="A1" href="javascript:read_status('','');">全部详情</a>
							  						#end
							  						 -->
							  					 #end	
								  		    
								  		      </u>
								  		      #set($sizeCC = $sizeCC+1)
						  		 	 	#end
						  		 	 	#set($numCC = $numCC+1)
						  		 	#end
					  		 #end
					 	#elseif($showNumCC==9)
					  		<span style="margin-left: 10px;font-size: 12px;"><a class="A1" href="javascript:read_status('3','');">...</a></span>
				  		#end 
			  	#end
		  	#else
		  		 $globals.encodeHTML($!result.mailCc)
		  	#end
	 </li>
	#end
	
	#if("$!result.mailBCc" != "")
	<li><span>$text.get("oa.mail.BCc")：</span>
	 <!-- 收件人（receives包括所有收件人和密送 抄送人员 当len大于0说明是内部发件箱 -->
	  		#if($len > 0)
	  			#set($listBCc=$globals.encodeHTML($!result.mailBCc).split(";"))
	  			#set($listSizeBCc = 0)
	  			#set($sizeBCc = 0)
  				#foreach($mailBCc in $listBCc)
  					#set($listSizeBCc = $listSizeBCc+1)
  				#end
  				 #set($showNumBCC = 0)
			  	#foreach($mailBCc in $listBCc)
				  		 #set($numBCc=0)
				  		 #set($showNumBCC = $showNumBCC+1)
				  		 #if($showNumBCC<9)
				  		 #foreach($rev in $receives)
				  		 		<!-- 如果人名相同 显示人民 -->
					  			#if($globals.get($rev,0) == $mailBCc)
					  				<!-- 匹配了 -->
					  				<u #if($groupId != 3)title="部门：$globals.get($rev,1)"#end style="cursor:inherit">
					  					#if($globals.get($rev,3) == 1)
					  						<img src='/style/images/email_open.gif' alt='收件人已读'/>$mailBCc
					  					#elseif($globals.get($rev,3) == 0)
					  						<img src='/style/images/email_close.gif' alt='收件人未读'/>$mailBCc
					  					#else
					  						<img src='/style/images/email_delete.gif' alt='收件人未读,发件人收回'/>$mailBCc
					  					#end
					  					
					  					#if($sizeBCc+1 < $listSizeBCc)
					  						,
					  					#else
					  						<!-- 如果是发件箱  -->
					  						<!-- 
					  						#if($groupId == 3)
					  							&nbsp;&nbsp;<a class="A1" href="javascript:read_status('3','');">查看邮件状态</a>
					  						#else
					  							&nbsp;&nbsp;<a class="A1" href="javascript:read_status('','');">全部详情</a>
					  						#end
					  						 -->
										#end
					  					#set($sizeBCc = $sizeBCc+1)
					  					
					  				</u>
					  		 	#elseif($globals.get($rev,0) != $mailCC&&($globals.get($rev,1) == $mailBCc||$globals.get($rev,4) == $mailBCc))
					  		 	 	#if($numBCc==0)
							  		      <u #if($groupId != 3)title="部门：$mailCC"#end style="cursor:inherit">
							  		      	 <img src = "/style/images/unknown_email.gif" alt = "部门需点击详情查看"/>$mailBCc
							  		      	 #if($sizeBCc+1 < $listSizeBCc)
					  							,
					  						 #else
						  						<!-- 如果是发件箱  -->
						  						<!-- 
						  						#if($groupId == 3)
						  							&nbsp;<a class="A1" href="javascript:read_status('3','');">查看邮件状态</a>
						  						#else
						  							&nbsp;<a class="A1" href="javascript:read_status('','');">全部详情</a>
						  						#end
						  						-->
						  					 #end	
							  		    
							  		      </u>
							  		      #set($sizeBCc = $sizeBCc+1)
					  		 	 	#end
					  		 	 	#set($numBCc = $numBCc+1)
					  		 	#end
				  		 #end
				  		#elseif($showNumBCC==9)
					  		<span style="margin-left: 10px;font-size: 12px;"><a class="A1" href="javascript:read_status('3','');">...</a></span>
				  		#end 
			  	#end
		  	#else
		  		$globals.encodeHTML($!result.mailBCc)
		  	#end
	 </li>
	#end
				<li><span>$text.get("email.lb.recDate")：</span>
				$result.mailTime
				#if("$!result.replayDate" !="")$text.get("email.lb.repDate"):$!result.replayDate#end
				#if("$!result.revolveDate" !="")$text.get("email.lb.tranDate"):$!result.revolveDate
				#end
				</li>
				#if("$!result.mailAttaches" != "")
				<li><span>$text.get("oa.mail.Attaches")：</span> 
					#foreach($att in $result.mailAttaches.split(",|;"))	
						#if($att.indexOf("&fileName=")> 0)
							#set($pp =$att.indexOf("&fileName=") +10)
							<img src="/$globals.getStylePath()/images/down.gif"/><a href="/ReadFile?tempFile=email&$att.substring(0,$pp)$!globals.encode($att.substring($pp))" target="_blank">$att.substring($pp)</a> &nbsp;&nbsp;<!-- <a href="/ReadFile?tempFile=email&$att.substring(0,$pp)$!globals.encode($att.substring($pp))&tempFile=previewFile" target="_blank">预览</a> -->
						#else
							<img src="/$globals.getStylePath()/images/down.gif"/> <a href="/ReadFile?tempFile=email&emlfile=$!globals.encode($!result.emlfile)&charset=$!result.mailcharset&attPath=$attPath&fileName=$!globals.encode($att)" target="_blank">$att</a> &nbsp;&nbsp;<!-- <a href="/ReadFile?tempFile=email&emlfile=$!globals.encode($!result.emlfile)&charset=$!result.mailcharset&attPath=$attPath&fileName=$!globals.encode($att)&type=previewFile" target="_blank">预览</a> -->
						#end
					#end
				</li>
				#end
				<li id="remarks" style="border-top: 1px solid #D0D0D0;padding:5px 0 0 40px;position:relative;display: none;">
					<span style="color:#B6B2B2;position:absolute;left:0;top:0;">备 注：</span>
					<textarea rows="3" cols="60" id="remark" name="remark" >$!result.remark</textarea>
					<div style="float:none;overflow:hidden;display: none;padding-top:5px;" id="btn_remark1">
						<span class="btn-w" onclick="saveRemark('save')">保存</span>
						<span class="btn-w" onclick="closeRemark()" title="取消">取消</span>
						
					</div>
					<div style="float:none;overflow:hidden;display: none" id="btn_remark2">
						<span class="btn_a" onclick="saveRemark('update')" title="编辑">编辑</span>
						<span class="btn_a" onclick="saveRemark('del')" title="删除">删除</span>
					</div>
				</li>
			</ul>   
			<iframe id="pcontent" name="pcontent" src="/EMailAction.do?operation=5&content=true&emailType=$!result.account&keyId=$!result.id" frameborder="no" width="100%" height="200" scrolling="no" onload="this.height=pcontent.document.body.scrollHeight;this.width=pcontent.document.body.scrollWidth;"/>
</div>
</div>
</form>
</body>
</html>