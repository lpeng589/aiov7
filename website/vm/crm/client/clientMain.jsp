<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("crm.client.list")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/jquery.divbox.js"></script>

<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="$globals.js("/js/client.vjs","",$text)"></script>
</head>

<body>
<form method="post" name="form" action="/ClientAction.do">
<input type="hidden" id="operation" name="operation" value="$globals.getOP('OP_QUERY')">
<input type="hidden" id="statusTo" name="statusTo" value="">
<input type="hidden" id="type" name="type" value="main">
<input type="hidden" id="ButtonType" name="ButtonType" value="">
<input type="hidden" id="tableName" name="tableName" value="CRMClientInfo">
<input type="hidden" id="reportName" name="reportName" value="$text.get('call.lb.client')">
<input type="hidden" id="fromCRM" name="fromCRM" value="clientList">
<input type="hidden" id="newCreateBy" name="newCreateBy" value="">
<input type="hidden" id="handContent" name="handContent" value="">
<input type="hidden" id="strStatus" name="strStatus" value="">
<input type="hidden" id="clientStatus" name="clientStatus" value="">
<input type="hidden" id="SQLSave" name="SQLSave" value="$!SQLSave">
#foreach($define in $defineList)
#if("2"=="$globals.get($define,4)")
<input type="hidden" id="$globals.get($define,1)" name="$globals.get($define,1)" value="$!conMap.get($globals.get($define,1))">
#end
#end 
<div class="oamainhead">
	<ul style="float: left; margin-left:4px;margin-top:3px;">
		#if($!clientList.size() != 0)
		<li style="float:left"><button type="button" onclick="showPie3D();">$text.get("crm.client.list.pie3d")</button></li>
		<li style="float:left"><button type="button" onclick="showLine2D();">$text.get("crm.client.list.line2d")</button></li>
		#end
		<li style="float:left">$text.get("common.lb.key")：


			<input style="width: 100px;border: 1px solid #1CA4FC;" type="text" name="keyWord"  value="$!conMap.get("keyWord")"  onKeyDown="if(event.keyCode==13&&this.value.length>0) beforeSubmit();"/>
			&nbsp;<button type="button" onclick="beforeSubmit();" style="margin-bottom:5px;">查询</button>
		</li>
	</ul>
<div class="HeadingButton">
	<ul style="margin-top:3px;">
	<li><button type="button" id="queryCondition"  type="button" class="b2">$text.get("com.query.conditions")</button></li>
	#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=CRMClientInfo").add())
	<li><button type="button"  onClick="addClientInfo('/UserFunctionQueryAction.do?tableName=CRMClientInfo&src=menu&operation=6&noback=true&fresh=open');" class="b2">$text.get("common.lb.add")</button></li>
	#end
	#if("$LoginBean.id"=="1")
	<li><button type="button"  class="b2" onclick="stopClient('0');">$text.get("OpenValue")</button></li>
	#end
	#if("$LoginBean.id"=="1")
	<li><button type="button"  class="b2" onclick="stopClient('-1');">$text.get("StopValue")</button></li>
	#end
	#if("$LoginBean.id"=="1")
	<li><button type="button"  class="b2" onclick="beforeDelete();">$text.get("common.lb.del")</button></li>
	#end
	#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=CRMClientInfo").add())
	<li><button type="button"  onClick="javascript:handOver()" class="b2">$text.get("com.hand.over")</button></li>
	#end
	#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=CRMClientInfo").add())
	<li><button type="button"  onClick="javascript:updateStatus()" class="b2">$text.get("common.lb.lifecycleTransfer")</button></li>
	#end
	#if("$LoginBean.id"=="1" && "$!exportId"!="")
	<li><button type="button" onClick="javascript:importData('$!exportId')" class="b2">$text.get("excel.lb.upload")</button></li>
	#end
	#if("$LoginBean.id"=="1")
	<li><button type="button" onClick="javascript:exportData()" class="b2">$text.get("com.bill.export")</button></li>
	#end
	#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=CRMClientInfo").print())
	<li><button type="button" onClick="javascript:printData()" class="b2">$text.get("common.lb.print")</button></li>
	#end
	#if($LoginBean.operationMap.get("/TelAction.do?operator=callTel").query())
	<li><button type="button" onClick="javascript:telCall()" class="b2">$text.get("common.lb.telCall")</button></li>
	#end
	#if($LoginBean.operationMap.get("/NoteAction.do?operator=sendSMS").query())
	<li><button type="button" onClick="javascript:sendMessage('sms');" class="b2">$text.get("com.sms.send")</button></li>
	#end

	<li><button type="button" onClick="javascript:sendMessage('email');" class="b2">$text.get("oa.common.sendEmail")</button></li>

	#if($LoginBean.id =="1")
	<li><button type="button" onclick="javascript:showDivCustomSetTable();">$text.get("com.define.colconfig")</button></li>
	#end
	#if("true" == "$!globals.getSysSetting('aioshop')")
	<li><button type="button" onclick="javascript:synchMember();">$text.get("common.msg.SynchronizeAIOSHOPVIP")</button></li>
	#end
	</ul>
	</div>
</div>
<div id="condtionsList">
		<div style="border: 1px solid #A2CEF5; background: #3399FF;margin-bottom: 10px; width:583px;height: 30px;">
		 <div style="float: left;line-height: 25px; margin-left:10px;font-weight: bold;color: #fff">$text.get("tableInfo.lb.searchCondition") </div>
		 <div style="margin-top:3px;float:right;text-align: right;margin-right: 5px;"><img id="closeDiv" style="cursor: pointer;" src="/$globals.getStylePath()/images/bottom/CloseWin_1.gif"/></div>
		</div>
			<div class="listRange_3" id="listid">
					#foreach($define in $defineList)
						#if("2"=="$globals.get($define,2)" && "1"=="$globals.get($define,4)")
						<li><p>$globals.get($define,0)</p>：


						<select name="$globals.get($define,1)">
						<option value=""></option>
						#foreach($enum in $globals.getEnumerationItems("CRMClientInfo","$globals.get($define,1)","$globals.getLocale()"))
							<option value="$!enum.value" #if("$!conMap.get($globals.get($define,1))"=="$!enum.value")selected#end>$enum.name</option>
						#end
						</select>
						</li>
						#end
					#end
					<li><p>$text.get("crm.client.notRemindingTime")</p>：


					<select name="LastContractTime">
					<option value=""></option>
					#foreach($enum in $globals.getOrderEnumerationItems("UnContractTime","$globals.getLocale()"))
						<option value="$!enum.value" #if("$!conMap.get('LastContractTime')"=="$!enum.value")selected#end>$enum.name</option>
					#end
					</select>
					<li><p>$text.get("common.lb.status")</p>：


						<select name="statusId">
							<option value="0" #if("$!conMap.get('statusId')"=="0")selected#end>$text.get("OpenValue")</option>
							<option value="-1" #if("$!conMap.get('statusId')"=="-1")selected#end>$text.get("StopValue")</option>
						</select>
					</li>
					#foreach($define in $defineList)
						#if("1"=="$globals.get($define,4)" && "3"=="$globals.get($define,2)")
							#if("$globals.get($define,1)"=="createBy")
							<li><p>$text.get("common.lb.follower")</p>：<input type="text" name="followPerson" ondblclick="selectCreateBy('followPerson');" value="$!conMap.get("followPerson")" style="width: 120px;"/>
								<img id="job_cy" src="/$globals.getStylePath()/images/St.gif" onClick="selectCreateBy('followPerson');" class="search"></li>
							<li><p>$text.get("crm.pie3d.createBy")</p>：<input type="text" name="createBy" ondblclick="selectCreateBy('createBy');" value="$!conMap.get("createBy")" style="width: 120px;"/>
								<img id="job_cy" src="/$globals.getStylePath()/images/St.gif" onClick="selectCreateBy('createBy');" class="search"></li>
							<input type="hidden" name="deptCode" value="$!conMap.get("deptCode")"/>
							<li><p>$text.get("oa.common.department")</p>：


								<input type="text" name="deptName" ondblclick="selectDept();" value="$!conMap.get("deptName")" style="width: 120px;"/>
								<img id="job_cy" src="/$globals.getStylePath()/images/St.gif" onClick="selectDept();" class="search"></li>
							#else
							<li><p>$globals.get($define,0)</p>：<input type="text" name="$globals.get($define,1)" value="$!conMap.get($globals.get($define,1))"/></li>
							#end
						#end
					#end
					<li><p>$text.get("common.lb.createTime")≥</p>：<input type="text" name="startTime" value="$!conMap.get("startTime")" onclick="openInputDate(this);"></li>
					<li><p>$text.get("common.lb.createTime")≤</p>：<input type="text" name="endTime" value="$!conMap.get("endTime")" onclick="openInputDate(this);"></li>
			</div>
			<div style="float:left; width:99%; margin:5px; padding:5px; text-align: center;">
            <button type="button" id="btnConfirmSCA" class="b3">$text.get("button.lable.sure")</button>
            <button type="button" id="btnCancelSCA" class="b3">$text.get("common.lb.close")</button>
        	</div>
</div>
		<div class="scroll_function_small_a" id="list_id">
<script type="text/javascript">
var oDiv=document.getElementById("list_id");
var dHeight=document.documentElement.clientHeight;
oDiv.style.height=dHeight-68+"px";
</script>
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="listRange_list_function" id="tblSort" name="table">
				<thead>
					<tr class="scrollColThead">
						<td width="25" class="oabbs_tc">&nbsp;</td>
						<td width="30" class="oabbs_tc"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
						#foreach($field in $fieldList)
						<td width="$field.width" class="oabbs_tc">$field.display</td>
						#end
						<!-- 这里注意加判断 -->
						#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=CRMClientInfo").update())	
						<td width="40" class="oabbs_tc">$text.get("oa.common.upd")</td>
						#end
						<td width="40" class="oabbs_tc">$text.get("sms.note.detail")</td>
					</tr>
				</thead>
				<tbody>
					#foreach ($client in $clientList)
					<tr onclick="setIframSrc2('/CrmTabAction.do?tableName=CRMClientInfo&tabType=detail&keyId=$!globals.get($!client,0)',this)" onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
						<td class="oabbs_tc">$velocityCount</td>
						<td class="oabbs_tc"><input type="checkbox" name="keyId" no="$velocityCount" value="$!globals.get($!client,0)"></td>
						#set($strIndex = 1)
						#foreach($field in $fieldList)
							#if("$field.fieldName"=="CRMClientInfo.createBy")
							<td class="oabbs_tc" title="$!globals.getEmpFullNameByUserId($!globals.get($!client,$strIndex))">$!globals.getEmpFullNameByUserId($!globals.get($!client,$strIndex)) &nbsp;</td>
							#elseif($field.inputType==1)
							#set ($enumItem=$!globals.getEnumerationItemsDisplay("$field.popUpName","$!globals.get($!client,$strIndex)","$globals.getLocale()"))
							<td class="oabbs_tc" title="$!enumItem">$!enumItem&nbsp;</td>
							#else
							<td class="oabbs_tl" title="$!globals.get($!client,$strIndex)">$!globals.get($!client,$strIndex) &nbsp;</td>
							#end
							#set($strIndex = $strIndex+1)
						#end
						#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=CRMClientInfo").update())
						<td width="60" class="oabbs_tc">
							<a href="javascript:mdiwin('/UserFunctionQueryAction.do?tableName=CRMClientInfo&src=menu&noback=true&operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId=$!globals.get($!client,0)','$text.get('common.msg.ClientData')')";>$text.get("oa.common.upd")</a>
						</td>
						#end
						<td width="60" class="oabbs_tc">
							<a href="javascript:mdiwin('/CrmTabAction.do?operation=5&keyId=$!globals.get($!client,0)','$text.get("common.lb.clientDetail")')";>$text.get("sms.note.detail")</a>
						</td>
					</tr>
					#end
				</tbody>
			</table>
		</div>
<div class="listRange_pagebar">$!pageBar</div>
</div>
	</form>
</div>											
</div>

</body>
#if($LoginBean.id =="1" && "$!parentCode"=="")
<form method="post" name="colConfigForm" action="/ColConfigAction.do?operation=1" target="_self">
<input type="hidden" name="tableName" value="CRMClientInfoList"/>
<input type="hidden" name="strAction" value="/ClientAction.do?operation=4&type=main&public=$!conMap.get("public")"/>
<input type="hidden" name="colType" value="list" />
<input type="hidden" id="colSelect" name="colSelect" value="" />
</form>
#end
</html>
