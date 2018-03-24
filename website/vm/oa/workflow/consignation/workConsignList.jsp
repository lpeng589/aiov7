<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
<link rel="stylesheet" href="/style1/css/workflow.css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css"/>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script language="javascript">

function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}


function addConsign(){
	var urlstr = '/WorkConsignAction.do?operation=6';
	asyncbox.open({id:'AddPopdiv',title:'添加工作委托',url:urlstr,width:550,height:330,btnsbar:jQuery.btn.OKCANCEL,
		callback:function(action,opener){
　　　　　if(action == 'ok'){
　　　　　　　opener.beforeSubmit();
			 return false;
　　　　　}
　　　}
	});
}

function updateConsign(keyId){
	var urlstr = '/WorkConsignAction.do?operation=7&keyId='+keyId;
	asyncbox.open({id:'AddPopdiv',title:'添加工作委托',url:urlstr,width:550,height:330,btnsbar:jQuery.btn.OKCANCEL,
		callback:function(action,opener){
　　　　　if(action == 'ok'){
　　　　　　　opener.beforeSubmit();
			 return false;
　　　　　}
　　　}
	});
}

function cancelConsigns(){
	if(!isChecked("keyId")){
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	form.type.value = "cancel";
	form.submit();
}

function cancelConsign(keyId){
	window.location.href = "/WorkConsignAction.do?operation=4&type=cancel&keyId="+keyId;
}


function dealAsyn(){
	form.submit();
}
</script>
</head>

<body onLoad="showStatus();">
	<form style="margin:0px;"  method="post" scope="request" id="form" name="form" action="/WorkConsignAction.do">
	<input type="hidden" name="operation" value="$globals.getOP('OP_QUERY')"/>
	<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/> 
	<input type="hidden" name="winCurIndex" value="$!winCurIndex"/>
	<input type="hidden" name="type" value="list"/>
	<table cellpadding="0" cellspacing="0" border="0" class="framework" style="margin-top:0px;">
	<tr>
		<td>
			<div class="TopTitle">
				<span>
				当前位置:
				#if("$!consignForm.queryType"=="myself")
					委托给我
				#elseif("$!consignForm.queryType"=="others")
					委托他人
				#else
					所有委托
				#end
				</span>
				<span style="float: right; margin-right: 2px;">
					<button type="button" class="bu_02" onclick="addConsign()">添加委托</button>		
				</span>
			</div>
			<table class="data_list_head"  cellpadding="0" cellspacing="0" >
				<thead>
					<tr>
						<td width="35">
							序号
						</td>
						<!-- 
						<td width="25">
							&nbsp;
						</td> -->
						<td width="100">
							委托人
						</td>
						<td width="100">
							代理人
						</td>
						<td width="200" >
							流程名称
						</td>
						<td  width="130" align="center"> 
							开始时间
						</td>
						<td width="130" align="center">
							结束时间
						</td>
						<td width="120" align="center">
							状态
						</td>
						<td width="120">
							操作
						</td>
					</tr>
				</thead>
			</table>
		<div id="data_list_id" class="data_list"  style="overflow:hidden;overflow-y:auto;width:99%;margin-top: 0px;" >
		<script type="text/javascript">
			$("#data_list_id").height($(window.parent).height()-120);
		</script>
			<table  cellpadding="0" cellspacing="0" >					
				<tbody>
					#foreach ($consign in $consignList )
					<tr #if($globals.isOddNumber($velocityCount)) class="c1" #else class="c2" #end>
						<td width="35">
						 $velocityCount
						</td>
						<!-- 
						<td width="25">		
							<input type="checkbox" name="keyId" value="$!consign.id"/>
						</td>
						-->
						<td width="100">
							$!globals.getEmpNameById($consign.userid)&nbsp;
						</td>
						<td width="100">
							$globals.getEmpNameById($consign.congignuserid)&nbsp;
						</td>
						<td width="200">
							#if("$!consign.flowName"=="")
							全部流程
							#else
							$!consign.flowNameDis&nbsp;
							#end
						</td>
						<td width="130"  align="center">
							$!consign.beginTime&nbsp;
						</td>
						<td width="130" align="center">
							$!consign.endTime&nbsp;
						</td>
						<td width="120" align="center">
							#if("$!consign.state"=="0")
							<font color="red">过期</font>&nbsp;
							#elseif("$!consign.state"=="-1")
							<font color="red">撤销</font>&nbsp;
							#else
							<font color="green">进行中</font>&nbsp;
							#end
						</td>
						<td width="120">
							#if("$!consign.state"=="1" && "$!consign.userid"=="$!LoginBean.id")
							<a href="javascript:updateConsign('$!consign.id');" style="color:#0179bb;">修改</a>
							<a href="javascript:cancelConsign('$!consign.id');" style="color:#0179bb;">撤销</a>
							#end
						</td>
					</tr>
					#end
				</tbody>		
			</table>	
			#if($consignList.size()==0)
				<div>暂无委托</div>		
			#end		
		</div>
				<div class="bottom">
					<div class="bottom_left">
						<a href="javascript:form.submit();">
							<img src="style1/images/refur.gif" width="14" height="15" /><span style="margin-left: 2px;">刷新</span>
						</a>
					</div>
					<div class="listRange_pagebar">
						$!pageBar
					</div>
				</div>
			</td>		
			</tr>	
		</table>
		</form>
	</body>
	</html>