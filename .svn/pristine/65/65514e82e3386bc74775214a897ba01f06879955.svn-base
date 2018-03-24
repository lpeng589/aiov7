<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#else
<title>我的日程</title>
#end
<link type="text/css" rel="stylesheet" href="/style1/css/calendarList.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" >
function openInputDate(obj){
	WdatePicker({lang:'zh_CN'});
}
function loadPage(){
	$("#pageSize").hide();
}
function turnForm(type,relationId,clientId){
	
	var url = "";
	var title = "";
	if(type.indexOf("会议")>-1){
   		url = "/OASearchMeeting.do?operation=4";
   		title = "我的会议";				            		
   	}  	
   	if(type.indexOf("任务")>-1){
   		url = "/OATaskAction.do?operation=5&taskId="+relationId;
   		title="我的任务";
   	}
   	if(type.indexOf("项目")>-1){
   		url = "/OAItemsAction.do?operation=5&itemId="+relationId;
   		title="我的项目";
   	}
   	if(type.indexOf("日志")>-1){
   		url = "/OAWorkLogAction.do?crmEnter=true&clientId="+clientId;
   		title="我的日志";
   	}
   	if(type.indexOf("客户")>-1){
   		url = "/CRMClientAction.do?operation=5&keyId="+clientId;
   		title="我的客户";
   	}	
   	if(url == "" && title ==""){
   		return false;
   	}else{
   		mdiwin(url,title);
   	}
}
function backHome(){
	location.href="/OACalendarAction.do?crmEnter=$!lvForm.crmEnter&clientId=$!clientId&addTHhead="+$("#addTHhead").val();
}

function querySubmit(){
	$(".hideBg").show();
	$("#closeDiv").show();
	form.submit();
}
function delCalendar(obj){
	if(!isChecked(obj)){
		alert("请选择一条记录!");
		return false;
	}
	if(!confirm("确定要删除？")){
		return false;
	}
	var ids ="";
	$("#mytable>tr").each(function(){
		if($("input",this).is(":checked")){
			ids += $("input",this).val()+";";
		}
	})
	if(ids != ""){
		jQuery.ajax({
			type:"post",
			url:"/OACalendarAction.do?operation=3&id="+ids,
			success:function(msg){
				if(msg == "3"){
					
					var page1 = $(".listRange_pagebar>div").text().split("(")[1];
					var pages1 = $(".listRange_pagebar>div").text().split("(")[0];
					var page2 = page1.split(")")[0];
					var pages2 = page1.split(")")[1];
					asyncbox.tips('删除成功','success');
					//form.submit();
					//window.location.reload();
					var id = ids.split(";");
					for(var i=0;i<id.length;i++){
						if(id[i] != ""){
							$("#"+id[i]).remove();
						}
					}					
					var num = parseFloat(page2) - parseFloat(id.length)+1;				
					$(".listRange_pagebar>div").text(pages1+"("+num+")"+pages2);					
				}else{
					asyncbox.tips(msg,'error');
				}
			}
		})
	}
}

function batchOperation(obj,finishStatusVal){
	if(!isChecked(obj)){
		alert("请选择一条记录!");
		return false;
	}
	
	var confirmStr = "确定要批量设置完成？";
	if(finishStatusVal == "0"){
		confirmStr = "确定要批量设置未完成？";
	}
	
	if(!confirm(confirmStr)){
		return false;
	}
	var ids ="";
	$("#mytable>tr").each(function(){
		if($("input",this).is(":checked")){
			ids += $("input",this).val()+",";
		}
	})
	
	jQuery.ajax({
		type: "POST",
		url: "/OACalendarAction.do",
		data: "operation=2&updateType=batchOperation&finishStatusVal="+finishStatusVal+"&ids="+ids,
		success: function(msg){
			if(msg=="success"){
				asyncbox.tips('操作成功','success');
				setTimeout("window.location.reload();",1000);
			}else{
				asyncbox.tips('操作失败!','error');
			}
		}
	});
	
}

</script>
<style type="text/css">
.hideBg{ display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;  height: 100%;  background-color:rgba(0, 0, 0, 0.23);  z-index:98;  -moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);}
</style>
</head>
<body class="html" onload="loadPage();showStatus();">
#if("$!addTHhead" == "true")
#parse("./././body2head.jsp")
#end
<div>
<div class="meeting-wrap" style="height:600px">
  
<iframe name="formFrame" style="display:none"></iframe>
<form action="/OACalendarAction.do?operation=4&queryFlag=List&addTHhead=$!addTHhead" method="post" name="form" >
<input type="hidden" id="operation" name="operation" value="$globals.getOP('OP_QUERY')"/>
<input type="hidden" id="pageNo" name="pageNo" />
<input type="hidden" id="clientId" name="clientId" value="$!clientId"/>
<input type="hidden" id="addTHhead" name="addTHhead" value="$addTHhead"/>
<input type="hidden" id="crmEnter" name="crmEnter" value="$!crmEnter"/>
<div class="heading">
	<div class="heading-title">
		<span class="lf title-span">
			<b class="icons week-icon"></b>
			<i>日程列表</i>
		</span>
		<ul class="btn-ul">	
			<li><span onClick="batchOperation('keyId','1');"  class="btn-s">批量完成</span></li>
			<li><span onClick="batchOperation('keyId','0');"  class="btn-s">批量未完成</span></li>	
			<li><span onClick="delCalendar('keyId');"  class="btn-s">删除</span></li>	
			<li><span onClick="backHome();"  class="btn-s">返回</span></li>			 
		</ul>
	</div>
</div>
<div class="search-div-wp">
	<div class="search-div">
		<span>
			<em>创建人</em>
			<input name="userId" value="$!userId" />
		</span>
		<span>
			<em>主题</em>
			<input name="listTitle" value="$!listTitle" />
		</span>
		<span>
			<em>客户</em>
			<input name="clientName" value="$!clientName" />
		</span>
		<select name="type">
		#if("$!lvForm.crmEnter" == "true")
			<option value="客户日程">客户日程</option>
		#else
			<option value="">请选择分类</option>
			#foreach($log in $!typeList)			
				<option value="$!globals.get($!log,1)" #if("$!globals.get($!log,1)" == "$!type")selected="selected"#end>$!globals.get($!log,1)</option>			
			#end
		#end
		</select>
		<select name="finishStatus">		
			<option value="">完成状态</option>						
			<option value="1" #if("1" == "$!finishStatus")selected="selected"#end>已完成</option>	
			<option value="0" #if("0" == "$!finishStatus")selected="selected"#end>未完成</option>			
		</select>
		<span>
			<em>开始时间</em>
			<input name="createTime" value="$!createTime" style="width: 80px;" onclick="openInputDate(this);"/>
			<em>至</em>
			<input name="endTime" value="$!endTime"style="width: 80px;" onclick="openInputDate(this);"/>
		</span>
		<span class="btn-s" onclick="querySubmit();">查询</span>	
	</div>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" id="tblSort" name="table">
		<thead>
			<tr>
				<td width="2%" class="oabbs_tc">
					<input type="checkbox" name="selAll" onClick="checkAll('keyId')" /> 
				</td>	
				<td width="50" class="oabbs_tl">创建人</td>			
				<td width="300" class="oabbs_tl">主题</td>
				<td width="50" class="oabbs_tc">日程分类</td>
				<td width="70" class="oabbs_tc">开始时间</td>
			    <td width="70" class="oabbs_tc">结束时间</td>
	     	    <td width="50" class="oabbs_tc">完成状态</td>
		        <td width="300" class="oabbs_tc">关联客户</td>		        
			</tr>
		</thead>
		<tbody id="mytable">
		#foreach( $row in $rsList)
		<tr id="$!globals.get($!row,0)">
			<input type="hidden" name="listId" value="$!globals.get($!row,0)" />	
			<td width="2%" class="oabbs_tc"> 
				<input type="checkbox" name="keyId"  value="$!globals.get($!row,0)"/> 
			</td>	
			<td >$!globals.getEmpFullNameByUserId($!globals.get($!row,5))</td>
		  	<td title="$!globals.get($!row,1)"><span style="cursor:pointer;" onclick="turnForm('$!globals.get($!row,2)','$!globals.get($!row,8)','$!globals.get($!row,9)');">$!globals.get($!row,1)</span></td>
			<td >$!globals.get($!row,2)</td>
		    <td >$!globals.get($!row,3)</td>
     	    <td >$!globals.get($!row,4)</td>
     	    <td >#if("$!globals.get($!row,11)" == "1")已完成#else未完成#end</td>     	   
     	    <td title="$!globals.get($!row,10)"><span style="cursor:pointer;" onclick="turnForm('$!globals.get($!row,2)','$!globals.get($!row,8)','$!globals.get($!row,9)');">$!globals.get($!row,10)</span></td>	        
		 </tr>
		#end
		  </tbody>
		</table>
		</form>
		<div class="bottom-page">
			<div class="listRange_pagebar">
			$!pageBar
			</div>
		</div>
</div>		
</div>	
<script type="text/javascript">
$(function(){
	$("#scroll-wrap").height(document.documentElement.clientHeight);
});
</script>
<div id="hideBg" class="hideBg"></div>
<div id="closeDiv" style='width:135px;height:102px;position:absolute;left:650px;top:240px;display:none;z-index:10001;'><img src='/style/images/load.gif'/><div style='color:#0179bb;font-weight:bold;'>正在查询，请稍等......</div></div>      	
</body>