<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客户移交</title>
<link type="text/css" rel="stylesheet" href="/style1/css/ListRange.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/oa_news.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/clientTransfer.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script> 
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/oa/public_goods.js"></script>
<script type="text/javascript">
function fillData(datas){	
	newOpenSelect(datas,fieldNames,fieldNIds,1);
	jQuery.close('Popdiv');
}
function receive(obj){
	if(!isChecked("keyId")){
		alert("请选择一条记录！");
		return false;
	}	
	var dataArr = [];
	$("#mybody",document).each(function(){ 
		$("tr",this).each(function(){
			if($("input",this).is(":checked")){
				var keyId = $("#keyId",this).val();
				var tr = []; 														   
	      		tr.push(keyId);      			             			   				
	      		dataArr.push(tr);
    		}
		})													    		
	});
	jQuery.ajax({
		   type: "POST",
		   url: "/CRMClientTransferAction.do?operation=1&flag="+obj+"&keyId="+dataArr,		   
		   success: function(msg){
		     alert(msg);
		     window.location.reload();
		   }
		});
}
function receiveSig(obj,id){	
	jQuery.ajax({
		   type: "POST",
		   url: "/CRMClientTransferAction.do?operation=1&flag="+obj+"&keyId="+id,		   
		   success: function(msg){
		     alert(msg);
		     window.location.reload();
		   }
		});
}
//查询详情
$(document).ready(function(){
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
	/*$(".detail1Btn").click(function(){
		var keyId = $(this).parents("tr").find("input:[name=keyId]").attr("value");
		var height = 300;	
		var url = "/CRMClientTransferAction.do?operation=4&flay=detail&id="+keyId;
		asyncbox.open({
			id:'detaildiv',url:url,title:'客户移交',width:440,height:height,cache:false,modal:true,
		    callback : function(action,opener){
			    if(action == 'ok'){			    
					return false;
				}
	　　  　 }
	　  });
	});*/
	$(".newBtn").click(function(){
		var keyId = $(this).parents("tr").find("input:[name=clientId]").attr("value");
		var url = "/CRMClientAction.do?operation=5&noback=true&keyId="+keyId;
		mdiwin(url,"公司信息");
	});
	
	/*单击列表表头排序*/
	$(".maintop td span").click(function () {
		var sortInfo = $(this).attr("sort") + "," + $(this).attr("fieldName");
		$("#sortInfo").val(sortInfo);
		showSearch();
	});
	//点击表格行选中该行
	/*$(".ViewTable tr").slice(1).each(function(){
		  var p = this;
		  $(this).children().slice(1).click(function(){
			  $($(p).children()[1]).children().each(function(){
				  if(this.type=="checkbox"){
				  if(!this.checked){
					  this.checked = true;
				  }else{
					  this.checked = false;
				  }
				 }
		  	});
		  });
	});*/
})
function showSearch(){
	form.submit();
}
function buttonSubmit(flag,obj){
	if(flag=="1"){
		$("#clientStatus").val(obj);		
	}
	if(flag=="2"){
		$("#clientStyle").val(obj);		
	}
	if(flag=="3"){
		$("#statusId").val(obj);		
	}
	if(flag=="4"){
		$("#audit").val(obj);		
	}
	showSearch();
}
function loadOrder(){
	var len = $('#mybody tr',document).length;
    for(var i = 0;i<len;i++){
        $('#mybody tr:eq('+i+') input[name=order]').val(i+1);
    }
    
    if($("#clientStatus").val() != undefined && $("#clientStatus").val() != ""){
    	$(".flag1 li span[id=All1]").removeClass("enumName");
    	$(".flag1 li span[id='"+$("#clientStatus").val()+"']").addClass("enumName");
    }
    if($("#clientStyle").val() != undefined && $("#clientStyle").val() != ""){
    	$(".flag2 li span[id=All2]").removeClass("enumName");
    	$(".flag2 li span[id='"+$("#clientStyle").val()+"']").addClass("enumName");
    }
    if($("#statusId").val() != undefined && $("#statusId").val() != ""){
    	$(".flag3 li span[id=All3]").removeClass("enumName");
    	$(".flag3 li span[id='"+$("#statusId").val()+"']").addClass("enumName");
    }
    if($("#audit").val() != "0" && $("#audit").val() != ""){       
    	$(".flag4 li span[id=0]").removeClass("enumName");
    	$(".flag4 li span[id='"+$("#audit").val()+"']").addClass("enumName");
    }
        
}
</script>
</head>
<body onload="loadOrder();">
<form action="/CRMClientTransferAction.do" method="post" name="form">
<input type="hidden" name="operation" value="4"/>
<input type="hidden" id="sortInfo" name="sortInfo" value="$!conMap.get('sortInfo')"/>
<input type="hidden" id="clientStatus" name="clientStatus" value="$!clientStatus"/>
<input type="hidden" id="clientStyle" name="clientStyle" value="$!clientStyle"/>
<input type="hidden" id="statusId" name="statusId" value="$!statusId"/>
<input type="hidden" id="audit" name="audit" value="$!audit"/>

<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></img></div>
	<div class="HeadingTitle">客户移交</div>	
	<ul class="HeadingButton">											
		<li><div class="btn btn-small" id="receive" name="receive" onclick="receive('1');">接收</div></li>
		<li><div class="btn btn-small" id="refuse" name="refuse" onclick="receive('2');">拒绝</div></li>	
	</ul>	
</div>
<div class="TransferTime">	
	<ul>
		<li>
			<i>移交日期：</i>
			<input type="text" class="txt_inp" id="beginTime" name="beginTime"  value="$!beginTime" onclick="openInputDate(this);" />	
		</li>		
		<li>
			<i>至：</i>
			<input type="text" class="txt_inp" id="endTime" name="endTime"  value="$!endTime" onclick="openInputDate(this);" />	
		</li>
		<li>
			<i>客户名称：</i>
			<input type="text" class="txt_inp" id="clientName" name="clientName"  value="$!clientName" />	
		</li>
		<li>
			<i>移交人：</i>
			<input type="text" class="txt_inp" id="transMan" name="transMan" value="$!transMan" ondblclick="deptPop('userGroup','cc','transMan','selectUserIdsForCopy','1');" />	
			<b onclick="deptPop('userGroup','cc','transMan','selectUserIdsForCopy','1');"></b>
		</li>
		<li>
			<i>移交对象：</i>
			<input  type="text" class="txt_inp" id="transferTo" name="transferTo" value="$!transferTo" ondblclick="deptPop('userGroup','cc','transferTo','selectUserIdsForCopy','1');" disableautocomplete="true" autocomplete="off" />		
			<b onclick="deptPop('userGroup','cc','transferTo','selectUserIdsForCopy','1');"></b>
		</li>
		<li>
			<div name="queryList" id="queryList" onclick="showSearch();" class="btn btn-mini btn-danger">$text.get("com.query.conditions")</div>	
		</li>
	</ul>					
</div>
<div class="searchBlock">
	<span >处理结果:</span>	
	<ul class="flag4">
		<li>
			<span id="All4" onclick="buttonSubmit('4','All4')">全部</span>
		</li>	
		#foreach($log in $!listReceive)		
		<li>
			<span id="$!globals.getFromArray($!log,1)" #if("$!globals.getFromArray($!log,1)"=="0") class="enumName" #end  onclick="buttonSubmit('4','$!globals.getFromArray($!log,1)')" name="$!globals.getFromArray($!log,1)">$!globals.getFromArray($!log,0)</span>
		</li>
		#end						
	</ul>
</div>	






<div id="contentsDiv" style="overflow: auto;margin: 0 auto; width: 95%;">
<table class="ViewTable" border="0"  cellpadding="0" cellspacing="0" bordercolor="#CDCDCD">
	<thead>
		<tr class="maintop" style="text-align:left;">
		<td style="width:25px;">NO.</td>
		<td ><input type="checkbox"  name="selAll" onclick="checkAll('keyId')" /></td>
		<td><span class="1_2" sort="DESC" fieldName="T.transferDate" style="cursor: pointer;" title="点击排序">移交日期</span></td>
		<td ><span class="1_3" sort="DESC" fieldName="I.ClientName" style="cursor: pointer;" title="点击排序">客户名称</span></td>
		<td ><span class="1_4" sort="DESC" fieldName="I.ClientLabel" style="cursor: pointer;" title="点击排序">客户标记</span></td>	
		<td ><span class="1_5" sort="DESC" fieldName="I.Status" style="cursor: pointer;" title="点击排序">生命周期</span></td>
		<td ><span class="1_6" sort="DESC" fieldName="I.ClientType" style="cursor: pointer;" title="点击排序">客户类型</span></td>				
		<td ><span class="1_7" sort="DESC" fieldName="T.TransferMan" style="cursor: pointer;" title="点击排序">移交者</span></td> 
		<td ><span class="1_8" sort="DESC" fieldName="T.TransferTo" style="cursor: pointer;" title="点击排序">移交对象</span></td> 				
		<td ><span class="1_10" sort="DESC" fieldName="T.Audit" style="cursor: pointer;" title="点击排序">处理结果</span></td> 
		<td >操作</td>
		
		</tr>
	</thead>
	<tbody id="mybody">		
			#foreach($log in $!logList)
				<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
					<td><input id="order" name="order" style="width:25px;border:0px" readonly="readonly"></td>
					<td>
					#if("$!globals.getFromArray($!log,10)" == "$!loginId" && "$!globals.getFromArray($!log,7)" == "0")
						<input type="checkbox" name="keyId" id="keyId" value="$!globals.getFromArray($!log,0)"/> 					
					#end
					</td>					
					<td>$!globals.getFromArray($!log,6)</td>
					<td><a href="javascript:" id="newBtn" class="newBtn">
					<span style="color:#0000ff;">$!globals.getFromArray($!log,1)</span></a></td>
					<td>$globals.getEnumerationItemsDisplay("ClientLabel","$!globals.getFromArray($!log,12)")</td>
					<td>$globals.getEnumerationItemsDisplay("ClientStatus","$!globals.getFromArray($!log,3)")</td>
					<td>$globals.getEnumerationItemsDisplay("ClientStyle","$!globals.getFromArray($!log,4)")</td>							
					<td>$globals.getEmpFullNameByUserId("$!globals.getFromArray($!log,5)")</td>
					<td>$globals.getEmpFullNameByUserId("$!globals.getFromArray($!log,10)")</td>					
					<td>$globals.getEnumerationItemsDisplay("ReceiveStatus","$!globals.getFromArray($!log,9)")</td>												
					<td class="detail" > 
					#if("$!globals.getFromArray($!log,10)" == "$!loginId" && "$!globals.getFromArray($!log,7)" == "0")
						<a href="javascript:receiveSig('1','$!globals.getFromArray($!log,0)')" title="接受" ><span style="color:#0000ff;">接受</span>
						</a>&nbsp;&nbsp;&nbsp;
						<a href="javascript:receiveSig('2','$!globals.getFromArray($!log,0)')" title="拒绝" ><span style="color:#0000ff;">拒绝</span>
						</a>
					#end
					</td>
					<input type="hidden" name="clientId" value="$!globals.getFromArray($!log,13)"/>				
				</tr>	
			#end					
	</tbody>	
</table>	
</div>
<div class="listRange_pagebar">
	$!pageBar
</div>
<script type="text/javascript">
	var oDiv=document.getElementById("contentsDiv");
	var sHeight=document.documentElement.clientHeight-150;
	oDiv.style.height=sHeight+"px";
</script>
</form>
</body>
</html>
