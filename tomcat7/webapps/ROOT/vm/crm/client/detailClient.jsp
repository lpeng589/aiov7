<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("common.lb.clientDetail")</title>
<link rel="stylesheet" type="text/css" href="/style1/css/tab.css" />
<link rel="stylesheet" type="text/css" href="/style1/css/workflow2.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link rel="stylesheet" type="text/css" href="/style/css/clientModule.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/crm/crm_client.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/client.vjs"></script>  
<script type="text/javascript">
var selectOne = '$text.get("common.msg.mustSelectOne")';
function selectTab(tableName,clientId){
	showTab(tableName);
	$("#tagContent iframe").each(function(){
		if(this.id == "pindex_"+tableName){
			if($(this).attr("src")==""){
				if(tableName.indexOf("CRMClientInfo")>-1)
					$(this).attr("src","/CrmTabAction.do?tableName="+tableName+"&tabType=detail&keyId="+clientId+"");
				else
					$(this).attr("src","UserFunctionQueryAction.do?tableName="+tableName+"&src=menu&operation=4"+"&designId=$!designId&f_brother="+clientId+"&isTab=yes&parentTableName=CRMClientInfo&winCurIndex=$!winCurIndex&hasFrame=true")
			}
			$(this).show();
		}else{
			$(this).hide();
		}
	});
}
function MoveLeft(){
	var showId=$("li[style*='block'][id!='li_detail']").first().prev().attr("id");
	if(showId!=null && showId!="li_detail"){
		$("li[style*='block'][id!='li_detail']").last().attr("style","display:none;");
		$("#"+showId).attr("style","margin-left: 5px;display:block;");
	}
}

function MoveRight(){
	var showId=$("li[style*='block'][id!='li_detail']").last().next().attr("id");
	if(showId!=null){
		$("li[style*='block'][id!='li_detail']").first().attr("style","display:none;");
		$("#"+showId).attr("style","margin-left: 5px;display:block;");
	}
}
function showTab(tableName){
	$("#tags li").each(function(){
		if(this.id == "li_"+tableName)
			$(this).attr("class","selectTag");
	    else
	   		$(this).attr("class","");
	});
}


/*修改客户*/
function updateClient(keyId,viewId,nowHead,contactFlag){
	var height = 540;
		var winHeight = parseInt($("#tagContent").css("height").substring(0,$("#tagContent").css("height").length-2)) + $("#tagContent").position().top;
		if(winHeight<height){
			height = winHeight;
		}
	$("#openHeight").val(height);
	var url = "";
	var title = "修改客户";
	if(contactFlag == "true"){
		url = "/CRMClientAction.do?operation=7&nowHead=2&linkMan=true&addContact=true&moduleId=$!moduleId&keyId="+keyId+"&viewId="+viewId;
		height = height -100;
		title = "新增联系人";
	}else{
		url = "/CRMClientAction.do?operation=7&moduleId=$!moduleId&keyId="+keyId+"&nowHead=" + nowHead +"&viewId="+viewId;
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

//添加，更新成功后的处理

function crmOpDeal(){
	jQuery.close("crmOpDiv"); 
	window.location.reload();
}

/*查看历史信息*/
function history(clientId){
	var url = "/CRMClientAction.do?operation=4&type=history&clientId="+clientId;
	asyncbox.open({
		id:'crmOpDiv',url:url,title:'查看历史记录',width:800,height:450,cache:false,modal:true
　  });
}


function pageRefresh(){
	alert("还原成功");
	jQuery.close('crmOpDiv');
	window.location.reload();
}

//点击还原返回的方法

function dealAsyn(){
		pageRefresh();
	}

//邻居表排序设置

function childrenSort(){
	var urls = "/ClientSettingAction.do?operation=4&queryType=broTabSort&viewId=$viewId";
	asyncbox.open({
		id:'childdiv',
		title : '邻居表设置',
		url :urls,
		modal　:true,
		width : 510,
		height : 370,
		btnsbar : jQuery.btn.OKCANCEL,
		callback : function(action,opener){
  		if(action == 'ok'){
  			opener.saveOrder();
  		}
  	  }
	});
}

function destSet(){
	var urls = "/moduleFlow.do?operation=destSet";
	asyncbox.open({
		id:'setDiv',
		title : '我的导航设置',
		url :urls,
		width : 630,
		height : 430,
	});
}

function broSetfresh(){
	window.location.reload();
}

//CRM详情页面-点击修改按钮-点击共享-处理双击个人部门返回函数
function fillData(datas){
	jQuery.opener('dealdivNew').evaluate(datas);
	jQuery.close('Popdiv');
}

//处理添加,修改页面的自定义弹出框

function exeSelectPop(returnValue){
	if(jQuery.exist('dealdiv')){
	    jQuery.opener('dealdiv').exeSelectPop(returnValue);
	}
}

</script>
<style>
/*主窗*/
#scroll{height:95%;position:absolute;left:5px;top:13px;}
/*左边内容区*/
#scroLeft{float:left;height:100%;overflow:hidden;width:265px;}
/*右边滚动条轨道*/
#scroRight{/*background:#999;background:rgba(0,0,0,0.1);*/float:right;height:100%;width:5px;border-radius:5px;overflow:hidden;margin-left:1px;cursor:pointer;}
/*滚动条*/
#scroLine{position:absolute;top:3px;right:0;width:5px;background:#7494B8;opacity:0.7;border-radius:5px;cursor:pointer;}
</style>
</head>
<body class="body_ef">
<form name="form" action="/CRMClientAction.do" method="post">
<input type="hidden" id="operation" name="operation" value="$globals.getOP('OP_QUERY')">
<input type="hidden" id="type" name="type" value="">
<input type="hidden" id="strType" name="strType" value="detail">
<input type="hidden" id="newCreateBy" name="newCreateBy" value="">
<input type="hidden" id="handContent" name="handContent" value="">
<input type="hidden" id="openHeight" name="openHeight" value="">
<input type="hidden" id="nowHead" name="nowHead" value="$!nowHead">
<input type="hidden" id="moduleId" name="moduleId" value="$!moduleId">
<input type="hidden" id="keyId" name="keyId" value="$!clientMap.get('id')">
	<div id="wrapper">
			<div class="cont cf">
				<div id="scroll">
				<div class="m_card_l f_l" id="scroLeft">
					<div class="cardbox" style="margin-top:0px;">
						<h3>
							<span title="$!clientMap.get('ClientName')">$globals.subTitle($!clientMap.get("ClientName"),32)</span>
						</h3>
						<p class="contacts">
							#set($pageStr=","+$pageFields)
							#set($hiddenStr=","+$hiddenFields)
							#set($phone=$globals.getFieldBean($clientName,'Phone'))
							#set($phoneDis=$globals.getLocaleDisplay("$phone.display"))
							#if($pageStr.indexOf(',Phone,')>-1 && !($hiddenStr.indexOf(',Phone,')>-1))
							<span class="titleInfo">$phoneDis:</span> $!clientMap.get("Phone")
							#end
							<br />
							#set($fax=$globals.getFieldBean($clientName,'Fax'))
							#set($faxDis=$globals.getLocaleDisplay("$fax.display"))
							#if($pageStr.indexOf(',Fax,')>-1 && !($hiddenStr.indexOf(',Fax,')>-1))
							<span class="titleInfo">$faxDis:</span>$!clientMap.get("Fax")
							<br />
							#end
							#set($email=$globals.getFieldBean($clientName,'Email'))
							#set($emialDis=$globals.getLocaleDisplay("$email.display"))
							#if($pageStr.indexOf(',Email,')>-1 &&  !($hiddenStr.indexOf(',Email,')>-1))
							<span class="titleInfo">$emialDis:</span>$!clientMap.get("Email")
							<br />
							#end
							#set($url=$globals.getFieldBean($clientName,'URL'))
							#set($urlDis=$globals.getLocaleDisplay("$url.display"))
							#set($url=$!clientMap.get("URL"))
							#if(!($url.indexOf("http:")>-1))
								#set($url="http://$url")
							#end
							#if($pageStr.indexOf(',URL,')>-1 && !($hiddenStr.indexOf(',URL,')>-1))
							<span class="titleInfo">$urlDis:</span><a href="$url" target="_blank">$!clientMap.get("URL")</a>
							#end
						</p>
					</div>
					#set($DetName="TABLENAME_"+$clientDetName)
						#foreach($contact in $clientMap.get($DetName))
								<div class="cardbox" style="$style">
									<h3>
										#if("$!contact.get('mainUser')"=="1")<img src="/js/img/user_suit.gif" title="主联系人" />#end $!contact.get("UserName")
									</h3>
									<p class="contacts">
										#set($qq=$globals.getFieldBean($clientDetName,'QQ'))
										#set($qqDis=$globals.getLocaleDisplay("$qq.display"))
										#if($pageStr.indexOf(',contactQQ,')>-1 && !($hiddenStr.indexOf(',contactQQ,')>-1))
										<span class="titleInfo">$qqDis:</span><span class="titleDetail"><a class="myQQ" href="tencent://message/?uin=$!contact.get("QQ")&Site=$!contact.get("UserName")&Menu=yes">$!contact.get("QQ")</a></span>
										<br />
										#end
										#set($cphone=$globals.getFieldBean($clientDetName,'Telephone'))
										#set($cphoneDis=$globals.getLocaleDisplay("$cphone.display"))
										#if($pageStr.indexOf(',contactTelephone,')>-1 && !($hiddenStr.indexOf(',contactTelephone,')>-1))
										<span class="titleInfo">$cphoneDis:</span>$!contact.get("Telephone")
										#end
										<br />
										#set($cmobile=$globals.getFieldBean($clientDetName,'Mobile'))
										#set($cmobileDis=$globals.getLocaleDisplay("$cmobile.display"))
										#if($pageStr.indexOf(',contactMobile,')>-1 && !($hiddenStr.indexOf(',contactMobile,')>-1))
										<span class="titleInfo">$cmobileDis:</span>$!contact.get("Mobile")
										<br />
										#end
										#set($cemail=$globals.getFieldBean($clientDetName,'ClientEmail'))
										#set($cemailDis=$globals.getLocaleDisplay("$cemail.display"))
										#if($pageStr.indexOf(',contactClientEmail,')>-1 && !($hiddenStr.indexOf(',contactClientEmail,')>-1))
										<span class="titleInfo">$cemailDis:</span>$!contact.get("ClientEmail")
										#end
									</p>
									<div class="op" style="width:55px;">
										<a class="all_icon i_email" href="#"  onclick="javascript:if('$!contact.get('ClientEmail')'==''){ alert('请填写联系人邮箱地址后，再进行此操作!'); return false; }else{ sendMsg('detailemail','$!contact.get("id")');}" title="发邮件"></a>
										<a class="all_icon i_tel" href="#"   onclick="if('$!contact.get('Mobile')'==''){alert('请填写联系人手机信息后，再进行此操作!');return false;} else {sendMsg('detailsms','$!contact.get("id")',0);}" title="发短信"></a>
									</div>
								</div>
						#end
				</div>
				<div id="scroRight" >
	              <div id="scroLine"></div>
	        	</div>
	        	</div>
				<div id="con"  style="position:absolute;left:275px;">
<script type="text/javascript">
	var oDiv = document.getElementById("con");
	var cardDiv = document.getElementById("scroll");
	var sWidth=document.documentElement.clientWidth-cardDiv.clientWidth-20;
	oDiv.style.width=sWidth+"px";
</script>
					<ul id="tags"  style="width:80%;float: left;">
						<li id="li_detail" style="display:block;" class="selectTag"><a onClick="selectTab('detail');"  href="javascript:void(0)">$text.get("crm.tab.detail.info")</a> </li>
						#set($num=0)
						#if("$!screenWidth" == "")
							#set($screenWidth = "1024")
						#end
						#set($usedWidth = $math.sub($screenWidth,520))
						#foreach($row in $childTabList)
							#if("$!row.tableName" != "CRMWorkPlanView")
								#set($num=$num+1)
								#set ($w=$num*80)  
								#if($w<=$math.toInteger($usedWidth)) 
									#set($style="margin-left: 5px;display:block;")
								#else
									#set($style="margin-left: 5px;display:none;")
								#end
								<li id="li_$row.tableName" style="$style" ><a onClick="selectTab('$row.tableName','$!clientMap.get("id")');" href="javascript:void(0)">$globals.getTableDisplayName($row.tableName)</a> </li>
							#end
						#end
					</ul>
					<div style="margin-right: 10px;float:right;">
						 #if($style.indexOf("display:none;")>-1)
							<span onClick="MoveLeft();" style="cursor: pointer;"><img src="/$globals.getStylePath()/images/frame/left.gif"></span>
							<span onClick="MoveRight();" style=" margin-left:10px;cursor: pointer;"><img src="/$globals.getStylePath()/images/frame/right.gif"></span>
						 #end
					 	<span onclick="childrenSort();"> <img style="margin-left:40px;cursor: pointer;" src="/style1/images/workflow/set.png"  title="设置"/></span>
					</div>
					<div id="tagContent">
						<script type="text/javascript">
							var oDiv=document.getElementById("tagContent");
							var sHeight=document.documentElement.clientHeight-65;
							oDiv.style.height=sHeight+"px";
						</script>
						<iframe id="pindex_detail" name="pindex" style="display:block;"
							src="/CRMClientAction.do?operation=5&type=contact&tableName=$clientName&contactTableName=$clientDetName&moduleId=$moduleId&viewId=$viewId&keyId=$!clientMap.get("id")"
						     width="100%" height="100%" frameborder=no ></iframe>
						#foreach($row in $childTabList)
							<iframe id="pindex_$row.tableName" name="pindex" style="display:none;" src=""
							width="100%" height="100%" frameborder=no scrolling="no"></iframe>	
						#end
					</div>
				 </div>
				</div>
			</div>
		</form>
	</body>
<script type="text/javascript" src="/js/crm/scroll.js"></script> 
</html>