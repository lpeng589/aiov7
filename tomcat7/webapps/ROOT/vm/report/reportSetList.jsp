<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("reportSet.lb.title")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script> 
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">

try{
	var koron = new ActiveXObject("KoronCom.TrustedSites") ;
	koron.add("$!IP") ;
}catch (e){
}

function deleteReport()
{
	if(sureDel('keyId'))
	{
		form.submit();
	}
}
function copyReport(){
	if(!isChecked("keyId")){		
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	form.operation.value=$globals.getOP("OP_COPY_PREPARE");
	form.action="/ReportSetAction.do";
	form.submit();
}

function printFormatSet(keyId){
	var urls = "/ReportSetAction.do?operation=84&reportId="+keyId;
	//window.showModalDialog("/ReportSetAction.do?operation=84&reportId="+keyId,winObj,"dialogWidth:650px;dialogHeight:400px;center:yes;help:no;resizable:no;status:no;scroll:no;");
	asyncbox.open({id:'Popdiv',title:'样式设计弹出框',url:urls,width:750,height:470});
}

function sqlSet(flag,fileName,reportNumber,reportId){
	try{
		var mimetype = navigator.mimeTypes["application/np-print"];
		if(mimetype){
			if(mimetype.enabledPlugin){
				var cb = document.getElementById("plugin");
				cb.sql('$!globals.getLocale()',fileName,'$!IP|$!port',reportId,reportNumber,flag,"$!JSESSIONID",'$LoginBean.id');
			}
		}else{
			var obj = new ActiveXObject("KoronReportPrint.SQLDesign") ;
	    	obj.URL="$!IP|$!port" ;
	    	obj.NewFlag = flag ;
	    	obj.FileName = fileName ;
	    	obj.ReportName = reportNumber ;
	   		obj.ReportId = reportId ;
	   		obj.Cookie ="$!JSESSIONID" ;
		   	obj.UserId ='$LoginBean.id' ;
	    	obj.Language = "$!globals.getLocale()" ;
			obj.OpenSQL();	
		}
	}catch (e){
		alert("$text.get('com.sure.print.control')") ;
	}
}



function dealAsyn(){
	jQuery.close("setId");
}

function submits(){
	document.getElementById('pageNo').value='1';
	form.submit();
}
</script>
</head>

<body onLoad="showtable('tblSort');showStatus();">
<form  method="post" scope="request" name="form" action="/ReportSetQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
 <input type="hidden" name="styleType" value="$!styleType">
<div class="Heading">
	<div class="HeadingTitle">#if("$!styleType"=="print") 打印样式设计 #else $text.get("reportSet.lb.title")#end</div>
	<embed type="application/np-print" style="width:0px;height:0px;" id="plugin"/>
	<ul class="HeadingButton">
		#if($globals.getMOperation().query())
		<li>
			<input type="hidden" value="$!winCurIndex" name="winCurIndex">
			<button type="submit" onClick="javascript:document.getElementById('pageNo').value='1'" class="hBtns">$text.get("common.lb.query")</button>
		</li>
		#end
		#if($globals.getMOperation().update())
		<li><button type="button"  onClick="copyReport()" class="hBtns">$text.get("common.lb.copy")</button></li>	
		#end
		#if($globals.getMOperation().add())
		<li><button type="button" onClick="location.href='/ReportSetAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&winCurIndex=$!winCurIndex'" class="hBtns">$text.get("common.lb.add")</button></li> 
		#end
		#if($globals.getMOperation().delete())
		<li><button type="button"  onClick="deleteReport()" class="hBtns">$text.get("common.lb.del")</button></li>	
		#end
	</ul>
</div>
<div id="listRange_id">
		<div class="listRange_1">
			<li><span>$text.get("reportSet.lb.reportName")：</span>
			  <input name="reportNames" type="text" value="$!reportNames" onKeyDown="if(event.keyCode==13){submits();}"></li>
			<li><span>$text.get("reportSet.lb.statusId")：</span>				  
			  <select name="statusIds">
				#foreach($erow in $globals.getEnumerationItems("IsAble"))
					<option value="$erow.value" #if($erow.value==$!statusIds) selected="selected" #end>$erow.name</option>
				#end
			  </select></li>
			  <li><span>$text.get("reportSet.lb.reportType")：</span>				  
			  <select name="reportType" onChange="changeType()" >
			  		<option value=""></option>
					#foreach($erow in $globals.getEnumerationItems("ReportType"))
						<option value="$erow.value" #if($erow.value==$!reportType)  selected="selected" #end >$erow.name</option>
					#end 
				</select></li>
			 <li><span>$text.get("reportSet.lb.module")：</span>				  
			  <select name="reportModule" >
			  		<option value=""></option>
					#foreach($erow in $globals.getEnumerationItems("MainModule"))
						<option value="$erow.value" #if($erow.value==$!reportModule)  selected="selected" #end >$erow.name</option>
					#end 
				</select></li>	
		</div>
		<div class="scroll_function_small_a" id="conter">
		<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.documentElement.clientHeight-102;
oDiv.style.height=sHeight+"px";
</script>
			<table border="0" width="1000" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
			<thead>
				<tr>
					<td width="30">&nbsp;</td>
					#if("$!styleType"!="print")
					<td width="30">
					<input type="checkbox" name="selAll" onClick="checkAll('keyId')">
					</td>
					#end
					<td width="180" align="center">$text.get("reportSet.lb.reportNumber")</td>
					<td width="*" align="center">$text.get("reportSet.lb.reportName")</td>
					<td width="80" align="center">$text.get("reportSet.lb.reportType")</td>
					<!-- 
					<td width="10%" align="center">$text.get("reportSet.lb.formatFile")</td>
					-->
					<td width="130" align="center">$text.get("common.lb.createTime")</td>
					#if("$!styleType"!="print")
					<td width="60" align="center">$text.get("reportSet.lb.statusId")</td>
					#end	
					<td width="120" align="center">$text.get("common.lb.operation")</td>
				</tr>			
			</thead>
			<tbody>
			#foreach($report in $result)
						<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
					<td align="center">$!velocityCount</td>
					#if("$!styleType"!="print")
					<td align="center">
						<input type="checkbox" name="keyId" sqlFormat="$report.newFlag|$report.SQLFileName|$report.ReportNumber|$report.id" value="$report.id">
					</td>
					#end
					<td align="left">$report.ReportNumber</td>
					<td align="left">$report.ReportName</td>
					<td align="center">$globals.getEnumerationItemsDisplay("ReportType","$report.ReportType")</td>
					<!-- 
					<td align="center">
					<select name="reportDet_$report.ReportNumber" onChange='changeFileName("$report.ReportNumber",this)'>
					  <option value="">$text.get("reportSet.lb.selectFormat")</option>
					  #foreach($reportDet in $report.reportDetBean.values())
					  	<option value="$reportDet.newFlag $reportDet.FormatFileName $reportDet.FormatName">$reportDet.FormatName</option>
					  #end
					</select></td>
					 -->
					<td align="center">$report.createTime</td>
					#if("$!styleType"!="print")
					<td align="center">
					&nbsp;
					#foreach($erow in $globals.getEnumerationItems("IsAble"))
						#if($erow.value==$report.statusId)
							$erow.name
						#end
					#end
					</td>
					#end
					<td align="center">
					#if("$!styleType"=="print")	
					<a href="javascript:printFormatSet('$!report.id');">样式设计</a>
					#else
						#if($globals.getMOperation().update())
						<a href='/ReportSetAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId=$report.id&winCurIndex=$!winCurIndex&reportNames=$globals.encode($!reportNames)'>$text.get("common.lb.update")</a>
						&nbsp; | &nbsp;
						<a href="javascript:sqlSet('$report.newFlag','$report.SQLFileName','$report.ReportNumber','$report.id');">SQL设计</a>
						#end 
					#end
					&nbsp;
					</td>
				</tr>
		#end
					</tbody>
			</table>
		</div>
	<div class="page-wp">
		<div class="listRange_pagebar">$!pageBar</div>
	</div>
</div>
</form>

<script language="javascript">
function copyStyle(id,detNo)
{
  var vals=document.getElementsByName(detNo)[0].value;
  if(vals.length==0){
  	 alert("$text.get("report.msg.selectCopyStyle")");
  	 return;
  }
  var detVals=vals.split(' ');
  if(detVals[0]=='NEW')
  {
  	alert("$text.get("report.msg.selectCopyStyle")");
  	return ;
  }
  var styleName=prompt("$text.get("report.lb.copyStyleName")","$text.get("report.lb.inputStyleName")");
  if(styleName.length>0){
  	location.href="/ReportSetAction.do?operation=$globals.getOP("OP_UPDATE")&type=copy&keyId="+id+"&FormatFileName="+detVals[1]+"&winCurIndex=$!winCurIndex&styleName="+styleName;
  }
  
}
</script>
</body>
</html>
