<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车辆管理信息</title>
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
function updateOne(obj){			
	url="/CarInfoAction.do?operation=7&id="+obj;
	form.target="formFrame";
	location.href=url;
	//mdiwin(url,"修改车辆信息");		
}
function delAll(obj,flag){		
	if(flag=="All"){
		if(sureDel(obj)){
			form.operation.value = 3 ;
			form.Flay.value = "All" ;
			form.submit();		
		}				
	}
	if(flag=="One"){
		if(confirm("确定要删除？")){
			form.action="/CarInfoAction.do?operation=3&Flay=One&id="+obj;
			form.submit();
		}		
	}			
}
function loadOrder(){
	var len = $('#mybody tr',document).length;
    for(var i = 0;i<len;i++){
        $('#mybody tr:eq('+i+') input[name=order]').val(i+1);
    }
    if(jQuery("#statusCar").val() !="" && jQuery("#statusCar").val() !=null && jQuery("#statusCar").val() !=undefined){
		jQuery("#carStatus").val(jQuery("#statusCar").val());
	}    
}
function addCar(){
	var url = "/CarInfoAction.do?operation=6";
	form.target="formFrame";
	location.href=url;
	//mdiwin(url,"添加车辆信息");
}
jQuery(document).ready(function(){
	// 添加审批人 
	jQuery("#addBtn").click(function(){
		var height = 240;	
		var url = "/CarInfoAction.do?operation=9";
		asyncbox.open({
			id:'dealdiv',url:url,title:'添加审批人',width:540,height:height,cache:false,modal:true,
		    callback : function(action,opener){
			    if(action == 'ok'){
			    	opener.save();
					return false;
				}
	　　  　 }
	　  });
	});
	// 修改保养，保险 
	jQuery("#updateBtn").click(function(){
		var keyId = $(this).parents("tr").find("input:[name=upId]").attr("value");		
		var height = 280;	
		var url = "/CarInfoAction.do?operation=7&updateFlag=XY&openFlag="+jQuery("#openFlag").val()+"&id="+keyId;
		asyncbox.open({
			id:'updatediv',url:url,title:'修改信息',width:640,height:height,cache:false,modal:true		  
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
function turnPage(obj){
	if(obj==1){
		location.href="/CarInfoAction.do?operation=4&openFlag=1";
	}
	if(obj==2){
		location.href="/CarInfoAction.do?operation=4&openFlag=2";
	}
	if(obj==3){
		location.href="/CarInfoAction.do?operation=4&openFlag=3";
	}
}
function querySubmit(){
	form.submit();
}
function queryTime(flag,obj){
	if(flag==2){
		location.href="/CarInfoAction.do?operation=4&openFlag=2&insureTime="+obj;
	}else{
		location.href="/CarInfoAction.do?operation=4&openFlag=3&nextTime="+obj;
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
<form method="post" name="form" action="/CarInfoAction.do" >
<input type="hidden" name="openFlag"  id="openFlag" value="$!openFlag"/>
<input type="hidden" name="operation" value=""/>
<input type="hidden" name="Flay" value=""/>
<input type="hidden" id="statusCar" value="$!lvForm.carStatus"/>
<input type="hidden" id="sortInfo" name="sortInfo" value="$!conMap.get('sortInfo')"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle" onclick="turnPage('1');"><a>车辆管理信息</a></div>
	<div class="HeadingTitle" onclick="turnPage('2');"><a>车辆保险提醒</a></div>
	<div class="HeadingTitle" onclick="turnPage('3');"><a>车辆保养提醒</a></div>		
</div>
<div id="listRange_id">
	
	<ul class="HeadingButton">
		#if($!openFlag == 2 )
		#elseif($!openFlag == 3)
		#else		
		#if($$LoginBean.getOperationMap().get("/CarInfoAction.do").add())	
		<li><div class="btn btn-small"><a href="javascript:" id="addBtn" title="添加审批人" >添加审批人</a></div>
		</li>		
		<li><div class="btn btn-small"><a href="javascript:addCar();" title="添加车辆" >添加车辆</a></div>
		</li>	
		#end	
		#if($$LoginBean.getOperationMap().get("/CarInfoAction.do").delete())		
		<li><div class="btn btn-small"  onclick="delAll('keyId','All')">删除</div></li>	
		#end	
		<li><div class="btn btn-small"  name="backList" 
		onClick="closeWin();" class="b2">$text.get("common.lb.close")</div>
		</li>
		#end		
	</ul>
	
<div class="scroll_function_small_a" id="conter" style="margin-top:0px;">
	<div>		
				#if($!openFlag == 2 )				
					<div class="btn btn-small" onclick="queryTime('2','week');">一周内</div>
					<div class="btn btn-small" onclick="queryTime('2','month');">一月内</div>
				#elseif($!openFlag == 3)
					<div class="btn btn-small" onclick="queryTime('3','week');">一周内</div>
					<div class="btn btn-small" onclick="queryTime('3','month');">一月内</div>
				#else
					<span>车辆名称：</span>
					<input size="15" id="carName" name="carName" type="text" value="$!lvForm.carName" />
					<span>状态：</span>
					<select name="carStatus"  id="carStatus" style="width:110px" onchange="querySubmit();">
						<option selected="selected"></option>
						<option value="1">正常</option>
						<option value="2">维修</option>
						<option value="3">报废</option>							
					</select>
					<span>车牌号：</span>						
					<input name="carNo"  id="carNo" size="15" value="$!lvForm.carNo" />	
					<div class="btn btn-small btn-danger" onclick="querySubmit();">查询</div>
				#end								
	</div>
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.documentElement.clientHeight-125;
oDiv.style.height=sHeight+"px";
</script>
	#if($!openFlag == 2)
	<table class="ViewTable" border="0" style="width:60%" cellpadding="0" cellspacing="0" bordercolor="#CDCDCD">
		<thead>
			<tr class="maintop">										
				<td>NO.</td>								        
		        <td><span sort="DESC" fieldName="CarNo" style="cursor: pointer;" title="点击排序">车辆牌号</span></td>				
				<td><span sort="DESC" fieldName="CarName" style="cursor: pointer;" title="点击排序">车辆名称</span></td>
			    <td><span sort="DESC" fieldName="company" style="cursor: pointer;" title="点击排序">保险公司</span></td>
			    <td><span sort="DESC" fieldName="dealPeople" style="cursor: pointer;" title="点击排序">保险经办人</span></td>
			    <td><span sort="DESC" fieldName="insureCost" style="cursor: pointer;" title="点击排序">保费</span></td>
			    <td><span sort="DESC" fieldName="insureDate" style="cursor: pointer;" title="点击排序">投保时间</span></td>
			    <td><span sort="DESC" fieldName="ovreDate" style="cursor: pointer;" title="点击排序">结束时间</span></td>
			    <td >修改保险</td>			   
			</tr>
		</thead>
		<tbody id="mybody">
		  #foreach ($log in $list)
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				<input type="hidden" name="upId" id="upId" value="$!log.id" />
				<td >
					<input id="order" name="order" style="text-align:center;width: 20px;border: 0px;" readonly="readonly">
				</td>																
				<td >$!log.CarNo</td>
				<td >$!log.CarName</td>		
			 	<td >$!log.company</td>		 	
				<td >$globals.getEmpFullNameByUserId("$!log.dealPeople")</td>
				<td >$!log.insureCost</td>
				<td >$!log.insureDate</td>
				<td >$!log.ovreDate</td>
				<td >
				 #if($$LoginBean.getOperationMap().get("/CarInfoAction.do").update())					    
					<a href="javascript:" id="updateBtn"  title="修改下一次保养时间" ><span style="color:#0000ff;">修改</span></a>				
				#end
				</td>		     											
			</tr>
			#end
		  </tbody>
		</table>
	#elseif($!openFlag == 3)
	<table class="ViewTable" border="0" style="width:60%" cellpadding="0" cellspacing="0" bordercolor="#CDCDCD" >
		<thead>
			<tr class="maintop">										
				<td >NO.</td>								        
		        <td ><span sort="DESC" fieldName="CarNo" style="cursor: pointer;" title="点击排序">车辆牌号</span></td>				
				<td ><span sort="DESC" fieldName="CarName" style="cursor: pointer;" title="点击排序">车辆名称</span></td>
				<td ><span sort="DESC" fieldName="maintainPeople" style="cursor: pointer;" title="点击排序">保养经办人</span></td>
			    <td ><span sort="DESC" fieldName="nextMaintainDate" style="cursor: pointer;" title="点击排序">保养下次日期</span></td>
			    <td >修改下一次保养时间</td>			   			   
			</tr>
		</thead>
		<tbody id="mybody">
		  #foreach ($log in $list)
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				<input  type="hidden" name="upId" id="upId" value="$!log.id" />
				<td class="oabbs_tc">
					<input id="order" name="order" style="text-align:center;width: 20px;border: 0px;" readonly="readonly">
				</td>																
				<td >$!log.CarNo</td>
				<td >$!log.CarName</td>	
				<td >$globals.getEmpFullNameByUserId("$!log.maintainPeople")</td>	
			 	<td >$!log.nextMaintainDate</td>
			 	
			 	<td >
			 	 #if($$LoginBean.getOperationMap().get("/CarInfoAction.do").update())					    
					<a href="javascript:" id="updateBtn" title="修改下一次保养时间" ><span style="color:#0000ff;">修改</span></a>				
				#end
				</td>		 		     											
			</tr>
			#end
		  </tbody>
		</table>
	#else
		<table class="ViewTable" border="0" style="width:95%"  cellpadding="0" cellspacing="0" bordercolor="#CDCDCD" >
		<thead>
			<tr class="maintop">					
					
				<td >NO.</td>
				<td >
				<input type="checkbox" name="selAll" onClick="checkAll('keyId')" /> 
				</td>				        
		        <td ><span sort="DESC" fieldName="CarNo" style="cursor: pointer;" title="点击排序">车辆牌号</span></td>				
				<td ><span sort="DESC" fieldName="CarName" style="cursor: pointer;" title="点击排序">车辆名称</span></td>
			    <td><span sort="DESC" fieldName="BusLoad" style="cursor: pointer;" title="点击排序">最大载人数</span></td>
			    <td ><span sort="DESC" fieldName="maxLoad" style="cursor: pointer;" title="点击排序">最大载重</span></td>
			    <td ><span sort="DESC" fieldName="carStatus" style="cursor: pointer;" title="点击排序">状态</span></td>
			    <td ><span sort="DESC" fieldName="buyTime" style="cursor: pointer;" title="点击排序">购置车辆时间</span></td>			   
	     	    <td ><span sort="DESC" fieldName="badLimit" style="cursor: pointer;" title="点击排序">报废年限</span></td>
	     	    <td ><span sort="DESC" fieldName="runPapersDate" style="cursor: pointer;" title="点击排序">行驶证到期日期</span></td>
	     	    <td ><span sort="DESC" fieldName="surveyDate" style="cursor: pointer;" title="点击排序">年检日期</span></td>
		        <td >修改</td>
		        <td >删除</td>
			</tr>
		</thead>
		<tbody id="mybody">
		  #foreach ($log in $list)
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				<td >
					<input id="order" name="order" style="text-align:center;width: 20px;border: 0px;" readonly="readonly">
				</td>				
				<td > 
					<input type="checkbox" name="keyId"  value="$!log.id"/> 
				</td>												
				<td >$!log.CarNo</td>
				<td >$!log.CarName</td>
			 	<td > $!log.BusLoad</td>
			 	<td >$!log.maxLoad</td>		 	
				<td > 
				#if("$!log.carStatus" == "1")正常#elseif("$!log.carStatus" == "2")维修#else报废#end
				</td>
				<td >$!log.buyTime</td>
				<td >$!log.badLimit</td>
				<td >$!log.runPapersDate</td>
				<td >$!log.surveyDate</td>		     
				<td >	
				    #if($$LoginBean.getOperationMap().get("/CarInfoAction.do").update())
					<a href="javascript:updateOne('$!log.id')"  title="修改" ><span style="color:#0000ff;">修改</span></a>
				#end
				</td>
				<td >
				#if($$LoginBean.getOperationMap().get("/CarInfoAction.do").delete())		    
				<a href="javascript:delAll('$!log.id','One')" title="删除"><span style="color:#ff0000;font-size:14px;">X</span></a>
				#end
				</td>									
			</tr>
			#end
		  </tbody>
		</table>
	#end
	</div>
<div class="listRange_pagebar"> $!pageBar </div>
</div>
</form>	
</body>

</html>