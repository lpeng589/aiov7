<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车辆领用管理</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/style1/css/ListRange.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/oa_news.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/clientTransfer.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
function openInputDate(obj){
	WdatePicker({lang:'zh_CN'});
}
/**
 * 确认删除
 */
function sureDel(obj){
	if(!isChecked(obj)){
		alert("请选择一条记录!");
		return false;
	}
	if(!confirm("确定要删除？")){
		return false;
	}else{
		return true;
	}	
}
function delAll(obj,flag,orNo){		
	if(flag=="All"){
		if(sureDel(obj)){
			form.operation.value = 3 ;
			form.Flay.value = "All" ;
			form.submit();		
		}				
	}
	if(flag=="One"){
		if(confirm("确定要删除？")){
			if(orNo=="0"){
				alert("等待审批不能删除！");
				return false;
			}
			if(orNo=="1"){
				alert("已派车不能删除！");
				return false;
			}
			form.action="/CarOperateAction.do?operation=3&Flay=One&id="+obj;
			form.submit();
		}		
	}			
}
function loadOrder(){
	var len = $('#mybody tr',document).length;
    for(var i = 0;i<len;i++){
        $('#mybody tr:eq('+i+') input[name=order]').val(i+1);
    }
    if(jQuery("#roleId").val() !="" && jQuery("#roleId").val() !=null && jQuery("#roleId").val() !=undefined){
		jQuery("#approver").val(jQuery("#roleId").val());
	}    
}
function backCar(obj,flag){		
	if(confirm("确定要归还？")){
		if(flag=="0"){
			alert("等待审批，不能归还！");
			return false;
		}
		if(flag=="2"){
			alert("审批未通过，不能归还！");
			return false;
		}
		if(flag=="3"){
			alert("以完成，不能归还！");
			return false;
		}
		form.action="/CarOperateAction.do?operation=15&id="+obj;
		form.submit();
	}		
				
}
function yesOROut(obj,flag){	
	if(flag == "" || flag ==null){
		alert("该申请没有启用审核");
		return false;
	}
	if(flag != "0"){
		alert("该申请审核过了 ");
		return false;
	}
	var url = "/CarOperateAction.do?operation=6&id="+obj+"&carFlay=PAI";	
	location.href=url;
}
jQuery(document).ready(function(){
	// 明细查询 
	jQuery(".allBtn").click(function(){
		var keyId = $(this).parents("tr").find("input:[name=keyId]").attr("value");
		var height = 340;	
		var url = "/CarOperateAction.do?operation=5&id="+keyId;
		asyncbox.open({
			id:'alldiv',url:url,title:'车辆出入信息',width:440,height:height,cache:false,modal:true	  
	　  });
	});
	jQuery("#addBtn").click(function(){
		var height = 340;	
		var url = "/CarOperateAction.do?operation=6";
		asyncbox.open({
			id:'alldiv',url:url,title:'车辆领用',width:740,height:height,cache:false,modal:true	  
	　  });
	});
	jQuery(".updateBtn").click(function(){
		var keyId = $(this).parents("tr").find("input:[name=keyId]").attr("value");
		var status = $(this).parents("tr").find("input:[name=status]").attr("value");
		if(status == "1" || status == "3"){
			alert("已提交申请不能进行修改");
			return false;
		}		
		var url="/CarOperateAction.do?operation=7&carFlay=UPDATE&id="+keyId;
		var height = 340;		
		asyncbox.open({
			id:'updatediv',url:url,title:'领用修改',width:740,height:height,cache:false,modal:true  
	　  });
	});
	jQuery(".appBtn").click(function(){
		var keyId = $(this).parents("tr").find("input:[name=keyId]").attr("value");
		var flag = $(this).parents("tr").find("input:[name=status]").attr("value");
		var apporer = $(this).parents("tr").find("input:[name=apporer]").attr("value");
		var loginId = $("#loginId").val();
		if(loginId != apporer && loginId !="1"){
			alert("你不是该指定审批人，不能审批 ");
			return false;
		}
		if(flag == "" || flag ==null){
			alert("该申请没有启用审核");
			return false;
		}
		if(flag != "0"){
			alert("该申请审核过了 ");
			return false;
		}
		var url = "/CarOperateAction.do?operation=6&id="+keyId+"&carFlay=PAI";		
		var height = 240;		
		asyncbox.open({
			id:'appdiv',url:url,title:'派车 ',width:440,height:height,cache:false,modal:true	  
	　  });
	});
	if($("#sortInfo").val() != undefined && $("#sortInfo").val() != ""){
		var sorrInfo = $("#sortInfo").val().split(",");	
		if(sorrInfo[0] == "ASC"){
			$(".maintop td span[fieldName='"+sorrInfo[1]+"']").attr("sort","DESC")
			$(".maintop td span[fieldName='"+sorrInfo[1]+"']").after('<img src="/style/images/mail/asc.gif" height="10px;" width="8px;"/>')
		}else{
			$(".maintop td span[fieldName='"+sorrInfo[1]+"']").attr("sort","ASC")
			$(".maintop td span[fieldName='"+sorrInfo[1]+"']").after('<img src="/style/images/mail/desc.gif" height="10px;" width="8px;"/>')
		}
			
	}
	/*单击列表表头排序*/
	$(".maintop td span").click(function () {
		var sortInfo = $(this).attr("sort") + "," + $(this).attr("fieldName");
		$("#sortInfo").val(sortInfo);
		querySubmit();
	});
})
function querySubmit(){
	form.submit();
}
function queryTime(flag,obj){
	if(flag==2){
		location.href="/CarOperateAction.do?operation=4&openFlag=2&insureTime="+obj;
	}else{
		location.href="/CarOperateAction.do?operation=4&openFlag=3&nextTime="+obj;
	}
	
}
</script>
<style type="text/css">
.listRange_list_function tbody td .item_sp{display:block;border-top:1px #AED3F1 solid;}
.listRange_list_function tbody td .item_sp:first-child{border:0;}
</style>
</head>
<body onload="loadOrder()">
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" name="form" action="/CarOperateAction.do" >
<input type="hidden" name="operation" value="4"/>
<input type="hidden" name="Flay" id="Flay" value=""/>
<input type="hidden" id="loginId" value="$!loginId"/>
<input type="hidden" name="roleId" id="roleId" value="$!lvForm.approver"/>
<input type="hidden" id="sortInfo" name="sortInfo" value="$!conMap.get('sortInfo')"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle" >车辆领用管理</div>		
</div>
<div id="listRange_id">
	
	<ul class="HeadingButton">	
	 #if($$LoginBean.getOperationMap().get("/CarInfoAction.do").add())	
		<li><div class="btn btn-small"><a href="javascript:" id="addBtn" title="车辆领用" >车辆领用</a></div>
		</li>
		#end	
		 #if($$LoginBean.getOperationMap().get("/CarInfoAction.do").delete())			
		<li><div class="btn btn-small"  onclick="delAll('keyId','All')">删除</div></li>	
		#end	
		<li><div class="btn btn-small"  name="backList" 
		onClick="closeWin();" class="b2">$text.get("common.lb.close")</div>
		</li>		
	</ul>
	
<div class="scroll_function_small_a" id="conter" style="margin-top:0px;">
	<div>								
		<span>审批人：</span>
		<select name="approver"  id="approver" style="width:120px" onchange="querySubmit();">
			<option selected="selected"></option>
			#foreach($log in $!nameList)
			<option value='$!globals.get($!log,2)'>$!globals.get($!log,1)</option>
			#end
		</select>						
		<span>车牌号：</span>						
		<input name="carNo"  id="carNo" size="15"  value="$!lvForm.carNo" />
		<span>领用时间：</span>						
		<input name="fTimes"  id="fTimes" size="15" onClick="openInputDate(this);" value="$!fTimes" />	
		<span>至：</span>						
		<input name="eTimes"  id="eTimes" size="15" onClick="openInputDate(this);" value="$!eTimes" />		
		<div class="btn btn-small btn-danger" onclick="querySubmit();">查询</div>									
	</div>
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.documentElement.clientHeight-125;
oDiv.style.height=sHeight+"px";
</script>
		<table class="ViewTable" border="0" style="width:95%"  cellpadding="0" cellspacing="0" bordercolor="#CDCDCD" >
		<thead>
			<tr class="maintop">					
					
				<td >NO.</td>
				<td >
				<input type="checkbox" name="selAll" onClick="checkAll('keyId')" /> 
				</td>				        
		        <td ><span sort="DESC" fieldName="carNo" style="cursor: pointer;" title="点击排序">车辆牌号</span></td>				
				<td ><span sort="DESC" fieldName="userCarPerson" style="cursor: pointer;" title="点击排序">领用人</span></td>
			    <td ><span sort="DESC" fieldName="outCarDate" style="cursor: pointer;" title="点击排序">领用日期</span></td>
			    <td ><span sort="DESC" fieldName="overCarDate" style="cursor: pointer;" title="点击排序">结束日期</span></td>			   
	     	    <td ><span sort="DESC" fieldName="destination" style="cursor: pointer;" title="点击排序">目的地</span></td>
	     	    <td ><span sort="DESC" fieldName="approver" style="cursor: pointer;" title="点击排序">审批人</span></td>
		        <td ><span sort="DESC" fieldName="status" style="cursor: pointer;" title="点击排序">状态</span></td>
		        <td >操作</td>
			</tr>
		</thead>
		<tbody id="mybody">
		  #foreach ($log in $list)
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				<td >
					<input id="order" name="order" style="text-align:center;width: 20px;border: 0px;" readonly="readonly">
				</td>				
				<td > 
					<input type="checkbox" name="keyId"  id="keyId" value="$!log.id"/> 
				</td>												
				<td >$!log.carNo</td>
				<td >$globals.getEmpFullNameByUserId("$!log.userCarPerson")</td>
			 	<td >$!log.outCarDate</td>
			 	<td >$!log.overCarDate</td>		 						
				<td >$!log.destination</td>
				<td >$globals.getEmpFullNameByUserId("$!log.approver")</td>
				<td ><a href="javascript:"  title="状态" class="allBtn"><span style="color:#0000ff;font-size:14px;">
				#if("$!log.status" == "0")等待审批#elseif("$!log.status" == "1")已派车#elseif("$!log.status" == "2")未通过#elseif("$!log.status" == "3")已完成#else没有启用审核#end
				</a></td>	
				<td >	
				 #if($$LoginBean.getOperationMap().get("/CarInfoAction.do").update())
					<a href="javascript:" class="updateBtn" title="修改" ><span style="color:#0000ff;font-size:14px;">修改</span></a>
					#end
					#if("$!loadFlag" == "true")			    
					<a href="javascript:" class="appBtn"  title="审核" ><span style="color:#0000ff;font-size:14px;">审核</span></a>
					#end	
					<a href="javascript:backCar('$!log.id','$!log.status')"  title="归还" ><span style="color:#0000ff;font-size:14px;">归还</span></a>													    
					 #if($$LoginBean.getOperationMap().get("/CarInfoAction.do").delete())
					<a href="javascript:delAll('$!log.id','One','$!log.status')" title="删除"><span style="color:#ff0000;font-size:14px;">删除</span></a>				
				#end
				</td>	
				<input type="hidden" id ="status" name="status" value="$!log.status" />	
				<input type="hidden" id ="apporer" name="apporer" value="$!log.approver" />							
			</tr>
			#end
		  </tbody>
		</table>
	</div>
<div class="listRange_pagebar"> $!pageBar </div>
</div>
</form>	
</body>

</html>