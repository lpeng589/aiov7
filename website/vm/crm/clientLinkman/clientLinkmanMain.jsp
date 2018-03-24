<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("crm.client.attention.path")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/popOperation.css" />
<style type="text/css"> 
.scrollColThead {position: relative;top: expression(this.parentElement.parentElement.parentElement.scrollTop);z-index:2;}
.listRange_pagebar{float: right;margin-right: 40px;}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/date.vjs"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/popOperation.js"></script>
<script type="text/javascript">
function Del(){
	if(!isChecked("keyId")){		
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	if(!window.confirm('$text.get("wokeflow.do.delete")'))return;
	document.getElementById("operation").value = "$globals.getOP('OP_DELETE')" ;
	form.submit();
}

/*客户弹出框*/
function popSelect(){
	var url ="/Accredit.do?popname=CrmClickGroup";
	asyncbox.open({
		id : 'Popdiv',url : url,title: '选择客户',width :755,height : 450,
		btnsbar :jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
	    	if(action == 'ok'){
	    		var str = opener.strData;
	    		$("#ClientNo").val(str.split(";")[0]);
	    		$("#ClientName").val(str.split(";")[1]);
	    	} 
	    }
　  });
}

function fillData(datas){
	var str = datas;
	$("#ClientNo").val(str.split(";")[0]);
	$("#ClientName").val(str.split(";")[1]);
	jQuery.close('Popdiv');
}	

function query(){
	form.operation.value="$globals.getOP('OP_QUERY')";
	form.submit();
}
function sendSMS(){
	if(!isChecked("keyId")){
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	var varValue = window.showModalDialog("/vm/classFunction/sendMessage.jsp","","dialogWidth=645px;dialogHeight=350px") ;
	if(typeof(varValue)=="undefined")return false;
	document.getElementById("sendMessage").value = varValue ;
	document.getElementById("type").value = "sendSMS" ;
}

function callTel(){
	var allCheckBoxs=document.getElementsByName("keyId");
	var communicationIds="";
	for(var i=0;i<allCheckBoxs.length;i++)
	{
		if(allCheckBoxs[i].type=="checkbox")
		{
			if(allCheckBoxs[i].checked==true)
			{
				var value = allCheckBoxs[i].value;
				communicationIds+=value+","
			}
		}
	}
	if(communicationIds!="" && communicationIds.length!=0)
	{
		communicationIds = communicationIds.substr(0,communicationIds.length-1);
		if(communicationIds.indexOf(",") >0 ){
			alert("$text.get('common.msg.callConfine.chooseOneNote')");		
		}else{
			var str  = window.showModalDialog("/TelAction.do?operator=callTel&from=CRMClientInfoDet&keyId="+communicationIds,"","dialogWidth=600px;dialogHeight=350px");	
		}		
	}else
	{
		alert("$text.get('common.msg.mustSelectOne')");		
	}    
  }
/*修改客户*/
function updateClient(contactId){
	var url = "/CRMClientAction.do?operation=7&type=updContact&contactId="+contactId;
	asyncbox.open({
		id:'crmOpDiv',url:url,title:'修改客户',width:750,height:255,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
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
	//jQuery.close("crmOpDiv"); 
	//window.location.reload();
	window.location.href="/ClientLinkmanAction.do?operation=4&type=main"
} 
/*客户详情*/
function detailClient(contactId){
	var url = "/CRMClientAction.do?operation=7&type=updContact&isDetail=true&contactId="+contactId;
	asyncbox.open({
		id:'crmOpDiv',url:url,title:'联系人详情',width:750,height:230,cache:false,modal:true,
　  });
}
</script>
</head>

<body>
<div id="viewOpDiv" class="custom-menu" style="z-index: 99; position: absolute; left: 100px; top: 20px; display: none;">
    <ul >        
    </ul>
</div>
<form method="post" name="form" action="/ClientLinkmanAction.do">
	<input type="hidden" id="operation" name="operation" value="$globals.getOP('OP_QUERY')">
	<input type="hidden" id="firstName" name="firstName" value="$!firstName">
	<input type="hidden" id="type" name="type" value="main">
		<input type="hidden" id="sendMessage" name="sendMessage" value="">
		<div class="Heading">
			<div class="HeadingTitle" style="padding:0;">
				$text.get("crm.client.linkman")
			</div>
				<ul class="HeadingButton">
					<li>
						<span type="submit" onClick="query();" class="hBtns">$text.get("common.lb.query")</span>
						<!-- <span type="submit" onClick="Del();" class="hBtns">$text.get("common.lb.del")</span> -->
						
					</li>
					<!-- 
					#if($LoginBean.operationMap.get("/ClientLinkmanAction.do").add())
					<li><span name="Submits" type="submit" onClick="return sendSMS();" class="hBtns">$text.get("com.sms.send")</span></li>
					#end
					#if($LoginBean.operationMap.get("/TelAction.do?operator=callTel").query())
					<li><span name="button" type="button" onClick="callTel()" class="hBtns">$text.get("common.lb.telCall")</span>
					#end
					 -->
				</ul>
			</div>
			<div class="listRange_1" id="listid">
				<ul>
					<li>
						<span>$text.get("crm.linkman.name")</span>
						<input name="userName" value="$!userName" onKeyDown="if(event.keyCode==13) query();">
					</li>
					<li>
						<span>$text.get("crm.client.name")</span>
						<input type="hidden" name="ClientNo" id="ClientNo" value="$!ClientNo" />
						<div class="swa_c2">
							<input name="ClientName" id="ClientName" value="$!ClientName" onKeyDown="if(event.keyCode==13) query();" ondblclick="popSelect();"/>
							<b class="stBtn icon16" onClick="popSelect();"></b>
						</div>
					</li>
					<li>
						<span>$text.get("oa.communcation.phone")</span>
						<input name="mobile" value="$!mobile" onKeyDown="if(event.keyCode==13) query();">
					</li>
					<li>
						<span>$text.get("oa.common.tel")</span>
						<input name="telephone" value="$!telephone" onKeyDown="if(event.keyCode==13) query();">
					</li>
				</ul>
			</div>
	
		<div class="scroll_function_small_a" id="list_id">
<script type="text/javascript">
var oDiv=document.getElementById("list_id");
var dHeight=document.documentElement.clientHeight;
var sHeight=document.getElementById("listid").clientHeight;
oDiv.style.height=dHeight-sHeight-100+"px";;
</script>
			<table width="600" border="0" cellpadding="0" cellspacing="0"
				class="listRange_list_function" id="tblSort" name="table">
				<thead>
					<tr class="scrollColThead">
						<td width="25" class="oabbs_tc">&nbsp;</td>
						<td width="30" class="oabbs_tc"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
						<td width="30" class="oabbs_tl">操作</td>
						<td width="80" class="oabbs_tl">$text.get("crm.linkman.name")</td>
						<td width="180" class="oabbs_tl">$text.get("crm.client.name")</td>
						<td width="60" class="oabbs_tc">$text.get("oa.common.sex")</td>
						<td width="80" class="oabbs_tc">$text.get("oa.communcation.phone")</td>
						<td width="100" class="oabbs_tc">$text.get("oa.common.tel")</td>
						<td width="90" class="oabbs_tc">QQ</td>
						<td width="140" class="oabbs_tc">Email</td>
					</tr>
				</thead>
				<tbody>
					#foreach ($det in $list)
					<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
						<td class="oabbs_tc">$velocityCount</td>
						<td class="oabbs_tc"><input type="checkbox" name="keyId" value="$globals.get($det,3)"></td>
						<!-- 
						<td align="center" class="opbt"
							#if($LoginBean.operationMap.get("/ClientLinkmanAction.do").update())
								f1="修改:javascript:updateClient('$globals.get($det,3)')"
							#end
							#if($LoginBean.operationMap.get("/ClientLinkmanAction.do").query())
								f2="详情:javascript:detailClient('$globals.get($det,3)')"
							#end
						>操作</td>
						 -->
						<td class="oabbs_tc" onclick="detailClient('$globals.get($det,3)')" style="cursor: pointer;">详情</td>
						<td class="oabbs_tc" title="$!globals.get($det,1)">$!globals.get($det,1)</td>
						<td class="oabbs_tl" title="$!globals.get($det,0)"><a href="javascript:mdiwin('/CRMClientAction.do?operation=5&type=detailNew&moduleId=$globals.get($det,10)&keyId=$globals.get($det,4)','$text.get("common.lb.clientDetail")')">$!globals.get($det,0)</a></td>
						<td class="oabbs_tc">$!globals.getEnumerationItemsDisplay("sex","$globals.get($det,9)","$globals.getLocale()")</td>
						<td class="oabbs_tc" title="$!globals.get($det,8)">$!globals.get($det,8)</td>
						<td class="oabbs_tc" title="$!globals.get($det,2)">$!globals.get($det,2)</td>
						<td class="oabbs_tc" title="$!globals.get($det,7)">$!globals.get($det,7)</td>
						<td class="oabbs_tc" title="$!globals.get($det,6)">$!globals.get($det,6)</td>
						<!-- 这里注意加判断 -->
						
					</tr>
					#end
				</tbody>
			</table>
		</div>
		<div class="listRange_pagebar" style="position:relative"> $!pageBar </div>
	</form>
</body>
</html>
