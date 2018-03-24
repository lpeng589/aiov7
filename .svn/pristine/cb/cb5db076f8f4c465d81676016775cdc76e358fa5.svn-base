<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.mail.mailDirectory")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/email.css" />
<script language="javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<style type="text/css">
.listRange_1{margin-top: 10px;}
.listRange_list_function tbody tr{height:30px;}
.scroll_function_small_a{padding-top: 10px;}
.listRange_pagebar{float: right;margin-right: 40px;}
</style>
<script>

function stop(type){
	if(!isChecked("keyId")){
		asyncbox.tips("$text.get("common.msg.mustSelectOne")",'alert',1500);
		return false;
	}
	if(type){
		asyncbox.confirm('确认停用？','提示',function(action){
		　　　if(action == 'ok'){
		　　　　　$("#op").val("stop");
				document.form.operation.value="$globals.getOP("OP_UPDATE_PREPARE")";
				document.form.submit();
		　　　}
		　});
	}else{
		asyncbox.confirm('确认启用？','提示',function(action){
		　　　if(action == 'ok'){
		　　　　　$("#op").val("start");
				document.form.operation.value="$globals.getOP("OP_UPDATE_PREPARE")";
				document.form.submit();
		　　　}
		　});
	}
}

function cancelcheck(){
	$("input[name='keyId']:checked").attr("checked",false);
}

function delcheck(){
	$("input[name='keyId']:checked").each(function(i,wo){
		$(wo).parent().parent().remove();
	});
}
function delaccount(){
	var shu=$("input[name='keyId']:checked").size();
	if(shu==0){
		asyncbox.tips("请选择至少一条记录",'alert',1500);
		return false;
	}
	asyncbox.confirm('确定要删除邮箱，这将无法恢复哦！','提示',function(action){
	　　　if(action == 'ok'){
			form.operation.value="3";
			jQuery.ajax({
				   type: "POST",
				   url: "/EMailAccountQueryAction.do",
				   data:$("#form").serialize(),
				   success: function(msg){
				     if(msg=="many"){
				     	asyncbox.confirm('发现邮箱有相关邮件，是否继续删除？','提示',function(action){
				     		if(action == 'ok'){
				    		 jQuery.ajax({
				    			   type: "POST",
				    			   url: "/EMailAccountQueryAction.do",
				    			   data:"delsure=true&"+$("#form").serialize(),
				    			   success: function(msg){
				    			     if(msg=="ok"){
				    			     	asyncbox.tips("删除成功",'success',1500);
				    			    	delcheck();
				    			     }else{
				    			     	asyncbox.tips("删除失败",'error',1500);
				    			     }
				    			   }
				    			});
				     		}else{
				     			cancelcheck();
				     		}
				     	});
				     }else if(msg=="ok"){
				     	asyncbox.tips("删除成功",'success',1500);
				    	delcheck();
			    	 }else{
			    		 asyncbox.tips("删除失败",'error',1500); 
			    	 }
				   }
				});
			}
		});
}

function onSubmit(){
	$("#operation").val("4");
	form.submit();
}

function select_tr(obj){
	if($(obj).parent().find("input[id=keyId]").is(":checked")){
		$(obj).parent().find("input[id=keyId]").removeAttr("checked");
	}else{
		$(obj).parent().find("input[id=keyId]").attr("checked","true");
	}
}

function dealMailSetting(operation,id){
	var title = "";
	if(operation == "6"){
		title = "添加邮件帐户";
	}else{
		title = "修改邮件帐户";
	}
	var strUrl = '/EMailAccountAction.do?operation='+operation+'&mainAccount=$!mainAccount&accountId='+id;
	asyncbox.open({
	　　 url : strUrl,
		title : title,
		id : 'dealSetting',
	　　	width : 550,
	　　 height : 450
	});
}

function dealAsyn(type){
	if(type == "OK"){
		window.location.href="/EMailAccountQueryAction.do";
	}else if(type == "SignOK"){
		jQuery.close('dealSign');
	}
}

function sign(id){
	asyncbox.open({
	　　 url : '/EMailAccountAction.do?operation=7&op=signPre&mainAccount=$!mainAccount&accountId='+id,
		title : '签名设置',
		id : 'dealSign',
	　　	width : 700,
	　　 height : 400
	});
}

function defaultset(id){
	window.location.href="EMailAccountQueryAction.do?operation=7&accountId="+id+"&op=default&mainAccount=$!mainAccount";
}
</script>
</head>
<body>
<form id="form" method="post" name="form" action="/EMailAccountQueryAction.do">
	<input type="hidden" name="operation" id="operation" value="4" />
	<input type="hidden" name="op" id="op" value="" />	
	<input type="hidden" name="mainAccount" id="mainAccount" value="$!EMailAccountSearchForm.mainAccount" />
	<div class="Heading">
		<div class="HeadingTitle" style="padding:0;color:#3C78AA;">
			邮件帐户设置
		</div>
		<ul class="HeadingButton">
			<li>
				#if($LoginBean.operationMap.get("/EMailAccountQueryAction.do").add() || $LoginBean.id == "1")
				<span class="btn-w" name="readOuter" type="button" onclick="dealMailSetting('$globals.getOP("OP_ADD_PREPARE")','$!globals.get($vg,0)')">$text.get("common.lb.add")</span>
				#end
				#if($LoginBean.operationMap.get("/EMailAccountQueryAction.do").delete() || $LoginBean.id == "1")
				<span class="btn-w" name="readOuter" type="button" onclick="delaccount()">$text.get("common.lb.del")</span>
				#end
				#if($LoginBean.operationMap.get("/EMailAccountQueryAction.do").update() || $LoginBean.id == "1")
				<span class="btn-w" name="readOuter" type="button" onclick="return stop(true);">$text.get("StopValue")</span>
				<span class="btn-w" name="readOuter" type="button" onclick="return stop(false);">$text.get("OpenValue")</span>	
				#end
				#if("$!mainAccount" != "")
				<span class="btn-w" type="button" name="readOuter" onclick="window.location.href='EMailAccountQueryAction.do'">$text.get("common.lb.back")</span>
				#end
			</li>
		</ul>
	</div>
	<div class="listRange_1" id="listid">
		<ul>
			<li>
				<span>关键字查询</span>
				<div class="swa_c2">
					<input name="keyWord" id="keyWord" value="$!EMailAccountSearchForm.keyWord" onKeyDown="if(event.keyCode==13) onSubmit();" />
					<b class="stBtn icon16" onClick="onSubmit()" ></b>
				</div>
				<!-- <a href="javascript:void(0)" class="qd_a" id="qdate_default" style="margin-left: 30px;">个人账户</a> -->
			</li>
			<li>
				<span>邮箱状态 </span>
				<div class="swa_c2">
					<select class="ls3_in" id="emailStatus" name="emailStatus" onchange="onSubmit()">
						<option value=""></option>
						<option title="启用" value="1" #if("$!EMailAccountSearchForm.emailStatus"=="1") selected #end>启用</option>
						<option title="停用" value="2" #if("$!EMailAccountSearchForm.emailStatus"=="2") selected #end>停用</option>
					</select>
				</div>
			</li>
		</ul>
	</div>
	<div class="scroll_function_small_a" id="list_id">
		<script type="text/javascript">
			var sHeight=document.documentElement.clientHeight;
			$("#list_id").height(sHeight-90);
		</script>
		<table width="600" border="0" cellpadding="0" cellspacing="0"
			class="listRange_list_function" id="tblSort" name="table">
			<thead>
				<tr class="scrollColThead">
					<td class="oabbs_tc" width="3%"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
					<td class="oabbs_tc" width="15%">$text.get("oa.email.Accountname")</td>
					<td class="oabbs_tl" width="20%">$text.get("oa.email.address")</td>		
					<td class="oabbs_tc" width="10%" >$text.get("common.lb.status")</td>
					#if("$!mainAccount" == "")
					<td class="oabbs_tc" width="10%">默认帐户</td>
					<td class="oabbs_tc" width="10%" style="text-align: left;">$text.get("role.lb.createBy")</td>
					#else
					<td class="oabbs_tc" width="15%" >$text.get("oa.elected.account")</td>
					#end
					<td class="oabbs_tc" >$text.get("oa.calendar.option")</td>
				</tr>
			</thead>
			<tbody>
				#foreach ($vg in $list)
				<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);" >
					<td align ="oabbs_t"><input type="checkbox" name="keyId" id="keyId" value="$!globals.get($vg,0)" ></td>
					<td class="oabbs_tl" onclick="select_tr(this)">$!globals.get($vg,1)</td>
					<td class="oabbs_tl" title="$!globals.get($vg,2)" onclick="select_tr(this)">$!globals.get($vg,2)</td>				
					<td class="oabbs_tc" onclick="select_tr(this)">#if("$!globals.get($vg,6)" == "1") $text.get("oa.bbs.started") #else $text.get("oa.bbs.stopstarted") #end </td>
					#if("$!mainAccount" == "")
					<td class="oabbs_tc" onclick="select_tr(this)">#if("$!globals.get($vg,4)"=="1") $text.get("currency.lb.isChecked") #else $text.get("common.lb.no") #end</td>
					<td class="oabbs_tl" onclick="select_tr(this)">$!globals.get($vg,5)</td>
					#else
					<td class="oabbs_tl" onclick="select_tr(this)">$!globals.get($vg,3)</td>
					#end
					<td class="oabbs_tc"> 
						#if($LoginBean.operationMap.get("/EMailAccountQueryAction.do").update() || $LoginBean.id == "1")
						<a style="padding-left:13px;cursor:pointer;" onclick="dealMailSetting('$globals.getOP("OP_UPDATE_PREPARE")','$!globals.get($vg,0)')"> 设置</a>
						&nbsp;
						<a style="padding-left:12px;cursor:pointer;" onclick="sign('$!globals.get($vg,0)')">$text.get("oa.elected.signature")</a>
						&nbsp;
						#if("$!mainAccount" == "")
						<a style="padding-left:10px;cursor: pointer;" onclick="defaultset('$!globals.get($vg,0)')">设为默认</a>
						&nbsp;
						<!-- <a style="background:url(/style/images/mail/share.gif) center no-repeat left;padding-left:12px;"  href="EMailAccountAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&accountId=$!globals.get($vg,0)&op=sharePre&mainAccount=$!mainAccount">$text.get("email.lb.shareuser")</a> -->
						#end
						#end
					</td>
				</tr>
			 #end
			</tbody>
		</table>
	</div>
</form>
</body>
</html>