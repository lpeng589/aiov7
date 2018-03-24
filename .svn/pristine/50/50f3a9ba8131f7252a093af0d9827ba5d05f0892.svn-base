<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
<link rel="stylesheet" href="/style1/css/workflow.css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css"/>
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script language="javascript">

function openInputDate(obj)
{
	WdatePicker({lang:'$globals.getLocale()'});
}

function beforeHasRead(){
	if(!isChecked("keyId")){
		asyncbox.alert('$text.get("common.msg.mustSelectOne")','$!text.get("clueTo")');
		return false;
	}
	document.form.operation.value = "$globals.getOP("OP_UPDATE")" ;
	document.form.submit();
}

function read(keyId){
	jQuery.get("/UtilServlet?operation=updateAlertStatus&type=advice&alertId="+keyId,function(){
		$("#st_"+keyId).html("$text.get("oa.mail.hasread")");
		$("#rd_"+keyId).html("");
	}) ;
}

function showSearch(){
	if($('#w').css("display")== "none")
	    $('#w').show();
	else
	    $('#w').hide();
}

function closeSearch(){ 
	$('#w').hide();
}

function subForm(){
	myform.submit();
	closeSearch();
}

function beforeHasRead(){
	if(!isChecked("keyId")){
		asyncbox.alert('$text.get("common.msg.mustSelectOne")','$!text.get("clueTo")');
		return false;
	}
	document.form.operation.value = "$globals.getOP("OP_UPDATE")" ;
	document.form.submit();
}
function handle(name,type){
	var items = document.getElementsByName(name);
	var mydatasIds="";
	/*
	var hasItem = false;
	for(var i=0;i<items.length;i++){
		if(items[i].checked){
			hasItem = true;
			break;
		}
	}
	if(hasItem){
		asyncbox.confirm('$text.get("oa.common.sureDelete")','$!text.get("clueTo")',function(action){
			if(action != 'ok'){
　				return;
			
			}});	
	}else{
		asyncbox.alert('$text.get('common.msg.mustSelectOne')','$!text.get("clueTo")');
		return;
	} */
	
	for(var i=0;i<items.length;i++){
		if(type == 1){
			if(items[i].checked){
				mydatasIds+=items[i].value;
				update(items[i].value);
				return true;
			}
		}else if(type == 2){
			if(items[i].checked){
				var value = items[i].value;
				mydatasIds+=value+",";
			}
		}
	}
	if(mydatasIds!="" && mydatasIds.length!=0){
			mydatasIds = mydatasIds.substr(0,mydatasIds.length-1);
			asyncbox.confirm('$text.get("oa.common.sureDelete")','$!text.get("clueTo")',function(action){
　　　				if(action == 'ok'){
　　　					if(typeof(top.jblockUI)!="undefined" ){
　　　						top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
　　　					}
	　　　				jQuery.ajax({
	　　　					 url: "/AdviceAction.do?operation=3",
	　　　					 type: "POST",
	　　　					 data: "keyId="+mydatasIds,
	   					 	 async: false   
	   					}).responseText;
　　　					if(typeof(top.jblockUI)!="undefined" ){
　　　						top.junblockUI()
　　　					} 
　　　　　			document.location.href = document.location.href;
				
　			}});	
	}else{
		asyncbox.alert('$text.get('common.msg.mustSelectOne')','$!text.get("clueTo")');
	}
}

function changeWidth(){
	var width = document.documentElement.clientWidth - 450 ;
	$(".thehelf").find("a").each(function(i){		
		$(this).css({width: width,'white-space': 'nowrap', 'text-overflow': 'ellipsis',  overflow: 'hidden',float: 'left'}) ;
	});		
}

function changeImage(id){
	if($('#'+"markImg_"+id).attr("src").indexOf("noRead.gif")>-1){	
		$('#'+"markImg_"+id).attr("src","style1/images/oaimages/read.gif");
	}	
	jQuery.get("/AdviceAction.do?operation=$globals.getOP('OP_UPDATE')&keyId=" + id);
}

function mdiwin(url,title){
	if(IsPC() == true){
		if(typeof(window.parent.frames["f_mainFrame"])=="undefined"){
			window.open(url);
		}else{
			top.mdiwin(url,title);
		}
	}else{
		$(window.parent.document).find("#body").append('<div class="btn btn-small backPage" onclick="iPadBack(this);">返回</div>');
		$(window.parent.document).find("#f_mainFrame").attr("src",url);
	}
	
}
function IsPC()  
{  
	var userAgentInfo = navigator.userAgent;  
	var Agents = new Array("Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod");  
	var flag = true;  
	for (var v = 0; v < Agents.length; v++) {  
	    if (userAgentInfo.indexOf(Agents[v]) > 0) { flag = false; break; }  
	}  
	return flag;  
}
</script>
</head>

<body onLoad="showtable('tblSort');showStatus();changeWidth(); ">
	<form style="margin:0px;"  method="post" scope="request" id="form" name="form" action="/AdviceAction.do">
	 <input type="hidden" name="operation" value="$globals.getOP('OP_QUERY')"/>
	 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/> 
	  <input type="hidden" name="winCurIndex" value="$!winCurIndex"/>
	  <input type="hidden" name="type" value="$!AdviceForm.type"/>
	  <input type="hidden" name="status" value="$!AdviceForm.status"/>
	  <input type="hidden" id="addTHhead" value="$!addTHhead" />
		<table cellpadding="0" cellspacing="0" border="0" class="framework" >
			<tr>
				<td>
					<div class="TopTitle">
						<span>
						当前位置:
						#if("$!AdviceForm.type"=="" && "$!AdviceForm.status"=="")
							所有消息  
						#end
						#if("$!AdviceForm.type"=="" && "$!AdviceForm.status"=="noRead")
							未读消息
						#end
						#if("$!AdviceForm.type"=="" && "$!AdviceForm.status"=="read")
							已读消息
						#end
						#if("$!AdviceForm.type"=="notApprove" && "$!AdviceForm.status"=="")
							待审消息
						#end
						#if("$!AdviceForm.type"=="approve" && "$!AdviceForm.status"=="")
							已审消息
						#end
						#if("$!AdviceForm.type"=="email" && "$!AdviceForm.status"=="")
							邮件消息
						#end
						#if("$!AdviceForm.type"=="other" && "$!AdviceForm.status"=="")
							其他消息
						#end
						</span>
					</div>
					
					<table class="data_list_head"  cellpadding="0" cellspacing="0" >
						<thead>
							<tr>
								<td width="25px;">
									序号
								</td>
								<td width="25px;">
									选择
								</td>
								
								<td width="*" >
									通知标题
								</td>
								<td  width="130px;" align="center"> 
									通知时间
								</td>
								<td width="40px;" align="center">
									状态   
								</td>
								<td width="55px;">
									操作
								</td>
							</tr>
						</thead>
					</table>
					<div id="data_list_id" class="data_list"  style="overflow:hidden;overflow-y:auto;width:99%;margin-top: 0px;" >
					<script type="text/javascript">
						if($("#addTHhead").val() == "true"){	
							var iframeHeight = $(window.parent.parent.document).find("#leftFrame").height();								
						}else{
							var iframeHeight = $(window.parent.parent.document).find("body").height();
						}			
						var oDiv=document.getElementById("data_list_id");
						//var sHeight=document.documentElement.clientHeight-115;
						var sHeight=iframeHeight-260;
						oDiv.style.height=sHeight+"px";
					</script>
						<table  cellpadding="0" cellspacing="0" >				
							<tbody>
								#foreach ($row in $result )
							
								<tr #if($globals.isOddNumber($velocityCount)) class="c1" #else class="c2" #end>
									
									<td width="25px;">
									 $velocityCount
									</td>
									<td width="25px;" style="vertical-align:middle;">		
										<input type="checkbox" name="keyId" value="$row.id"/>
									</td>
							
								  #set($start=$row.Content.indexOf(">")+1)
									<td class="thehelf" title='$row.title'>
									<font style="float: left; " >
									#if("$row.Status"=="read")
										<img src="style1/images/oaimages/read.gif" id="markImg_$row.id" title="已读消息"/>&nbsp;
									#else
										<img src="style1/images/oaimages/noRead.gif" id="markImg_$row.id" title="未读消息"/>&nbsp;
									#end
																	
									</font>
									#if($row.Content.indexOf("</a>")>-1)
										<span onclick="changeImage('$row.id');">$row.Content.replace("',","&addTHhead=$!addTHhead',") &nbsp;</span>
									#else
										<span onclick="javascript:window.location.href='/AdviceAction.do?keyId=$row.id&operation=$globals.getOP("OP_UPDATE")&winCurIndex=$winCurIndex';">$row.Content &nbsp;</span>
									#end
									</td>
									<td width="160px;">
										$!row.createTime &nbsp;
									</td>
									<td width="40px;" align="center">
										#if("$row.Status"=="read")$text.get("oa.mail.hasread")#else
										<span id="st_$row.id"> $text.get("oa.mail.notRead") </span>
										#end 
									</td>
									<td width="40px;" align="center">
										#if("$row.Status"!="read")
										<span id="rd_$row.id"> <a
											href='/AdviceAction.do?keyId=$row.id&operation=$globals.getOP("OP_UPDATE")&winCurIndex=$winCurIndex'><font color="blue">$text.get("oa.mail.readed")</font></a>
										</span> #end &nbsp;
									</td>
								</tr>
								#end
							</tbody>		
						</table>	
						#if($result.size()==0)
							<div>暂无通知消息</div>		
						#end		
					</div>
					<div class="bottom">
						<div class="bottom_left">
							<input type="checkbox" value="checkbox" style="margin-left: 45px;" name="selAll" onClick="checkAll('keyId')"/>全选




							<a href="javascript:handle('keyId',2)">
								<img src="style1/images/oaimages/11.gif" width="14" height="14" /><span style="margin-left: 2px;">删除</span>
							</a>
							<a href="javascript:void(0);" onclick="return beforeHasRead();">
								<img src="style1/images/oaimages/tt.bmp" width="14" height="14" /><span style="margin-left: 2px;">标记已读</span>
							</a>
							<a href="javascript:form.submit();">
								<img src="style1/images/refur.gif" width="14" height="15" /><span style="margin-left: 2px;">刷新</span></a>
						</div>
						<div class="listRange_pagebar">
							$!pageBar
						</div>
					</div>
				</td>		
				</tr>	
			</table>
		</form>
		<div id="w" class="search_popup" style="display:none;height: 190px;top: 150px;left: 250px;">
			<div id="Divtitle" style="cursor:move;">
				<span class="ico_4"></span>$text.get("com.query.conditions")
			</div>
			<form method="post" name="myform"
				action="/AdviceAction.do?operation=4&selectType=gaoji">
				<table style="margin-left: 20px; margin-top: 20px;">
					<tr>
						<td >通知标题:</td>
						<td ><input size="13" name="title" type="text" value="$!AdviceForm.title" onKeyDown="if(event.keyCode==13) subForm();"/>	
						</td>	
					</tr>	
					<tr>
						<td >通知时间:</td>
						<td align="left">
							<input name="startDate" size="13"
								value="$!AdviceForm.startDate"
								onKeyDown="if(event.keyCode==13) subForm();"
								onClick="openInputDate(this);" />
							至





						<input name="endDate" size="13" value="$!AdviceForm.endDate"
								onKeyDown="if(event.keyCode==13) subForm();"
								onClick="openInputDate(this);" />				
						</td>
					</tr>			
				</table>
				<span class="search_popup_bu"><input type="button"
						onClick="subForm();" class="bu_1" />
					<input type="button" onClick="closeSearch();" class="bu_2" />
				</span>
			</form>
		</div>
		<script language="javascript">
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
	</body>
	</html>