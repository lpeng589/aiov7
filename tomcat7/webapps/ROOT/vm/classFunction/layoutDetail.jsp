<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>#if("$!moduleName"=="")$!mainTable.display.get("$globals.getLocale()")#else$!moduleName#end $text.get("common.lb.detail")</title>
<link rel="stylesheet" href="/style/css/layout.css" type="text/css">
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script language="javascript" src="$globals.js("/js/editPage.vjs","",$text)"></script>
<script language="javascript" src="/js/editGrid.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="$globals.js("/js/Main.vjs","",$text)"></script>
<script language="javascript" src="$globals.js("/js/date.vjs","",$text)"></script>
<script language="javascript" src="$globals.js("/js/longTime.vjs","",$text)"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/setTime.js"></script>
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script language="javascript" src="$globals.js("/js/define.vjs",$mainTable.tableName,$text)"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript">
function m(value){
	return document.getElementById(value) ;
}

function flowDepict(applyType,keyId){ 
	window.open("/OAMyWorkFlow.do?keyId="+keyId+"&operation=$globals.getOP("OP_DETAIL")&applyType="+applyType,null,"height=570, width=1010,top=50,left=100 ");
}


var valueId = "$!values.get("id")";
var upIniFlag=1; 
  
var fromFoward = "$!fromFoward";

var ids=-1;
var fieldS;
var returnValue;
var operation = $globals.getOP("OP_UPDATE");
var noback="$!noback";
var detail = "$!detail"

#set($flowNode = $globals.getBillCheckIsFreeSeting("$mainTable.tableName").get(0))
var flowNodeid ="$!flowNode.id"; 
var flowNodedisplay ="$!flowNode.display";
var detailFlow = "$!detailFlow";
var existFlow = "$!existFlow";
var winCurIndex = "$!winCurIndex";
var operation = $globals.getOP("OP_UPDATE");
var moduleType = "$!moduleType" ;
if("$!parentTableName"!="" 
			&& window.parent.document.getElementById("bottomFrame")){
	var displayType = window.parent.document.getElementById("displayType");
	if(displayType!=null && typeof(displayType)!="undefined"){
		displayType.value = "detail" ;
	}
}

var workFlowNode = "$!values.get('workFlowNode')";
var strWakeUp = "$!strWakeUp";
var printRight = $globals.getMOperationMap($request).print();
#if("$detail"=="detail")  window.save=true; #end

#foreach ($rowlist in $childTableList )  
#set($allChildName="$allChildName"+"$rowlist.tableName"+",")
#set($griddata =$rowlist.tableName+"Data")
#if($allConfigList.size()>0)
#foreach ($colConfig in $childTableConfigList.get($rowlist.tableName))
#set ($row="")#set ($isExist="false")
#foreach ($config_row in $rowlist.fieldInfos )#if($colConfig.colName==$config_row.fieldName)#set ($row=$config_row)#set ($isExist="true")#end#end
#if($isExist!="false") #set($fname = $rowlist.tableName+"_"+$row.fieldName)
#if(!$globals.getScopeRight($rowlist.tableName,$row.fieldName,$scopeRight))
#if("$row.inputType" == "2")			  			
#set($inputValue = "")
	for(var i=0;i<${griddata}.cols.length;i++){
		if(${griddata}.cols[i].name=="$fname"){
			${griddata}.cols[i].inputType=-2;
			${griddata}.cols[i].width=0;
			${griddata}.cols[i].inputValue='$inputValue';
		}
	}
#if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
#foreach($srow in $row.getSelectBean().viewFields)
#set($disField = $rowlist.tableName+"_"+$globals.getTableField($srow.asName))	
	for(var i=0;i<${griddata}.cols.length;i++){
		if(${griddata}.cols[i].name=="$disField"){
			${griddata}.cols[i].inputType=-2;
			${griddata}.cols[i].width=0;
			${griddata}.cols[i].inputValue='$inputValue';
		}
	}						
#end				   
#end
#else
#set($inputValue = "")
	for(var i=0;i<${griddata}.cols.length;i++){
		if(${griddata}.cols[i].name=="$fname"){
			${griddata}.cols[i].inputType=-2;
			${griddata}.cols[i].width=0;
			${griddata}.cols[i].inputValue='$inputValue';
		}
	}
#end
#end
#end
#end
#else
#foreach ($row in $rowlist.fieldInfos )
#set($fname = $rowlist.tableName+"_"+$row.fieldName)#set($flag=true)
#if($!moduleTable&&$!moduleTable.get($rowlist.tableName))#set($flag=$!moduleTable.get($rowlist.tableName).contains($row.fieldName))#end
#if(!$globals.getScopeRight($rowlist.tableName,$row.fieldName,$scopeRight)||!$flag)
#if("$row.inputType" != "100" && $row.fieldName != "id" && $row.fieldName != "f_ref" && $row.fieldName != "f_brother"&&$row.inputType!=3&&$row.inputType!=6)			  			
#if("$row.inputType" == "2")			  			
#set($inputValue = "")
	for(var i=0;i<${griddata}.cols.length;i++){
		if(${griddata}.cols[i].name=="$fname"){
			${griddata}.cols[i].inputType=-2;
			${griddata}.cols[i].width=0;
			${griddata}.cols[i].inputValue='$inputValue';
		}
	}
#if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
#foreach($srow in $row.getSelectBean().viewFields)
#set($disField = $rowlist.tableName+"_"+$globals.getTableField($srow.asName))	
	for(var i=0;i<${griddata}.cols.length;i++){
		if(${griddata}.cols[i].name=="$disField"){
			${griddata}.cols[i].inputType=-2;
			${griddata}.cols[i].width=0;
			${griddata}.cols[i].inputValue='$inputValue';
		}
	}						
#end				   
#end
#else
#set($inputValue = "")
	for(var i=0;i<${griddata}.cols.length;i++){
		if(${griddata}.cols[i].name=="$fname"){
			${griddata}.cols[i].inputType=-2;
			${griddata}.cols[i].width=0;
			${griddata}.cols[i].inputValue='$inputValue';
		}
	}
#end
#end
#end
#end
#end
#end

function AuditingBlockUI(strUrl){
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	window.location.href=strUrl ;
}

function draftAudit(){
	form.operation.value = $globals.getOP("OP_EXTENDBUTTON_DEFINE") ;
	document.getElementById("ButtonType").value = "draftAudit" ;
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	document.getElementById("from").value = "detail" ;
	form.submit() ;
}

function update(){
	location.href="/UserFunctionAction.do?tableName=$!tableName&keyId=$!values.get("id")"+
	"&f_brother=$!f_brother&parentCode=$!parentCode&operation=$globals.getOP("OP_UPDATE_PREPARE")&noback=$!noback&winCurIndex=$!winCurIndex&fromPage=detail&pageNo=$!pageNo&parentCode="+
	document.getElementById("parentCode").value+"&parentTableName=$!parentTableName&saveDraft=#if("draft"=="$!values.get('workFlowNodeName')")draft#end";	
}

function initFieldValue(){
#foreach ($row in $fieldInfos )
	#if($row.inputType==5 || $row.inputType==10)
		var fields = document.getElementsByName("$row.fieldName") ;
		for(var i=0;i<fields.length;i++){
			if("$!values.get("$row.fieldName")".indexOf(fields[i].value+",")!=-1 || "$!values.get("$row.fieldName")"==fields[i].value){
				fields[i].checked = true ; 
			}
		}
	#elseif($row.inputType==1)
		var fields = $("#$row.fieldName") ;
		fields.attr("value","$!values.get("$row.fieldName")") ;
	#end
#end
}

function preview(){
	var bdhtml = window.document.body.innerHTML;
	var sprnstr = "<!--start-->";
	var eprnstr = "<!--end-->"
	prnhtml=bdhtml.substring(bdhtml.indexOf(sprnstr)+18); 
	prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr));
	window.document.body.innerHTML=prnhtml;
	window.print();
	window.document.body.innerHTML=bdhtml;
}
</script>
</head>

<body scroll="no" onLoad="initFieldValue();#foreach ($rowlist in $childTableList ) #if("$globals.getSysSetting($rowlist.tableSysType)"=="true" && ($childTableConfigList.get($rowlist.tableName).size()>0 || $allConfigList.size()==0)) #set($gridtable =$rowlist.tableName+"Table")  #set($griddata =$rowlist.tableName+"Data") showtable('$gridtable');  initTableList(${gridtable}DIV,$gridtable,$griddata,$rowlist.defRowCount);#end #end   initCalculate();initMainCaculate();#if("$quote"=="quote" && $mainTable.triggerExpress==1)loadFireEvent(); #end #if("$detail"=="detail") readonlyForm(document.form); #end execStat();#if(  "$!globals.getVersion()"=="3"  || "$!globals.getVersion()"=="12" || "$!globals.getVersion()"=="13"  || "$!globals.getVersion()"=="11")initCountCal();#end#if($globals.isExistSeq())iniGoodsSeqSet();#end ">
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" onKeyDown="down()" scope="request" id="form" name="form" action="/UserFunctionAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" id="operation" name="operation" value="$globals.getOP("OP_UPDATE")">
<input type="hidden" id="org.apache.struts.taglib.html.TOKEN" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" id="tableName" name="tableName" value="$!tableName">
<input type="hidden" id="button" name="button" value="">
<input type="hidden" id="frameName" name="frameName" value="$fName">
<input type="hidden" id="winCurIndex" name="winCurIndex" value="$winCurIndex">
<input type="hidden" id="pageNo" name="pageNo" value="$!pageNo" />
<input type="hidden" id="parentTableName" name="parentTableName" value="$!parentTableName">
<input type="hidden" id="defineInfo" name="defineInfo" value="">
<input type="hidden" id="detLineNo" name="detLineNo" value="">
<input type="hidden" id="saveDraft" name="saveDraft" value="$saveDraft">
<input type="hidden" id="moduleType" name="moduleType" value="$!moduleType">
<input type="hidden" id="noback" name="noback" value="$!noback">
<input type="hidden" id="logicValidate" name="logicValidate" value="">
<input type="hidden" id="defineName" name="defineName" value="">
<input type="hidden" id="checkFieldName" name="checkFieldName" value="">
<input type="hidden" id="fromCRM" name="fromCRM" value="$!fromCRM"/>
<input type="hidden" id="ButtonType" name="ButtonType" value="">
<input type="hidden" id="varKeyIds" name="varKeyIds" value="$!values.get("id")">
<input type="hidden" id="from" name="from" value="">
#if("$!detailFlow"=="detailFlow" || ("$!existFlow"=="exist" ))
<input type="hidden" id="varWakeUp" name="varWakeUp" value="">
<input type="hidden" id="varNextNode" name="varNextNode" value="">
<input type="hidden" id="varCheckPersons" name="varCheckPersons" value="">
<input type="hidden" id="varAttitude" name="varAttitude" value="">
<input type="hidden" id="optionType" name="optionType" value="">
<input type="hidden" id="isMyFlow" name="isMyFlow" value="$isMyFlow">
#end
<input type="hidden" id="OAWorkFlow" name="OAWorkFlow" value="#if($!OAWorkFlow)true#{else}false#end">
#if($!OAWorkFlow)
<input type="hidden" id="designId" name="designId" value="$!designId">
<input type="hidden" id="currNodeId" name="currNodeId" value="$!currNodeId">
#end
#if("$copy" == "copy")
<input type="hidden" id="parentCode" name="parentCode" value="$globals.classCodeSubstring("$!parentCode",0)">
#else
<input type="hidden" id="parentCode" name="parentCode" value="$globals.classCodeSubstring("$!parentCode",5)">
#end
#set($printCount=false)
<object id="WebBrowser" width=0 height=0 classid="CLSID:8856F961_340A_11D0_A96B_00C04FD705A2"></object>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">#if("$!moduleName"=="")$!mainTable.display.get("$globals.getLocale()")#else$!moduleName#end </div>
	#if($!tabList == "")
	<ul class="HeadingButton">
	#if($globals.getMOperationMap($request).add() && "true"!="$!isLinkType"&&!$!OAWorkFlow)
		<li><button onClick="location.href='/UserFunctionAction.do?tableName=$tableName&operation=6&parentTableName=$!parentTableName&f_brother=$!values.get("f_brother")&winCurIndex=$winCurIndex'">$text.get("common.lb.add")</button></li>
		<li><button onClick="location.href='/UserFunctionAction.do?tableName=$tableName&operation=17&keyId=$!keyId&parentTableName=$!parentTableName&f_brother=$!values.get("f_brother")&winCurIndex=$winCurIndex'">$text.get("common.lb.copy")</button></li>
	#end
	#set($msg=$text.get("define.button.reverse"))
	#if($globals.getMOperationMap($request).update()&&(!$!flowAudit||$!flowAudit.indexOf("$msg")<0)&&!$!OAWorkFlow&&$!updateRight)
	<li><button type="button" onClick="update();">$text.get("common.lb.update")</button></li>
	#end
	<li>$!flowAudit</li>
	#if("draft"=="$!values.get('workFlowNodeName')"&&$!tableName!="tblView")
	<li><button type="button" onClick="draftAudit();">$text.get("common.lb.draftSave")</button></li>
	#end
	#if("$auditprint"=="0"&&"$print"=="true"&&$globals.getMOperationMap($request).print())
		#set($printCount=true)
		<li><button type="button" onClick='window.showModalDialog("/UserFunctionQueryAction.do?tableName=$tableName&reportNumber=$!reportNumber&BillID=$!values.get("id")&operation=$globals.getOP("OP_PRINT")&parentTableName=$!parentTableName&winCurIndex=$!winCurIndex",winObj,"dialogWidth=258px;dialogHeight=245px,scroll=no;")' class="b2">$text.get("common.lb.print")</button></li>
	#elseif(("$flowStatus"=="finish"||"$flowStatus"==""||$currNodeId==-1)&&"$print"=="true"&&$globals.getMOperationMap($request).print())
		#set($printCount=true)
		<li><button type="button" onClick='window.showModalDialog("/UserFunctionQueryAction.do?tableName=$tableName&reportNumber=$!reportNumber&BillID=$!values.get("id")&operation=$globals.getOP("OP_PRINT")&parentTableName=$!parentTableName&winCurIndex=$!winCurIndex",winObj,"dialogWidth=258px;dialogHeight=245px;")' class="b2">$text.get("common.lb.print")</button></li>
	#end
	#if("true"!="$!isLinkType")
		#if($mainTable.hasNext==1  && "$!noback"!="true")
			$globals.hasNext("$!mainTable.tableName","$!parentTableName","$values.get('id')","$f_brother")
		#end
		#foreach($brow in $customButton)
			<li>$!brow</li>
		#end
		#if($inIniAmount)
		   <li><button type="button" id="inIniAmount" onClick="location.href='/UserFunctionAction.do?tableName=$tableName&keyId=$keyId&operation=$globals.getOP("OP_UPDATE_PREPARE")&f_brother=$!f_brother&parentTableName=$!parentTableName&winCurIndex=$!winCurIndex&onOff=onOff'">$!inIniAmountName</button></li>
		#end
		
		#if($globals.getMOperationMap($request).query() && "$!fromCRM"==""&&($!tabList==""||($!tabList!=""&&$!parentTableName.length()>0)))
			#if($!OAWorkFlow)
			#if("$!noback"=="true")
			<li><button type="button" id="backList"  name="backList" onClick="closeWin();" class="b2">$text.get("common.lb.close")</button></li>		
			#else
			#if($!request.getParameter("OAFlowQueryType"))
			<li><button type="button" id="backList"  name="backList" onClick="location.href='/OAMyWorkFlowQuery.do?type=$!request.getParameter("OAFlowQueryType")&approveStatus=#if($!request.getParameter("approveStatus"))$!request.getParameter("approveStatus")#{else}transing#end&operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button></li>			
			#else
			<li><button type="button" id="backList"  name="backList" onClick="location.href='/OAMyWorkFlow.do?type=$!request.getParameter("OAFlowQueryType")&approveStatus=#if($!request.getParameter("approveStatus"))$!request.getParameter("approveStatus")#{else}transing#end&operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button></li>
			#end
			#end
			#else
			#if("yes"=="$!isMyFlow")
			<li><button type="button" id="backList"  name="backList" onClick="location.href='/OAMyWorkFlow.do?winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button></li>
			#elseif("$!noback"=="true")
			<li><button type="button" id="backList"  name="backList" onClick="closeWin();" class="b2">$text.get("common.lb.close")</button></li>			
			#else
			<li><button type="button" id="backList"  name="backList" onClick="location.href='/UserFunctionQueryAction.do?operation=$globals.getOP("OP_QUERY")&parentCode=$globals.classCodeSubstring("$!parentCode",5)&tableName=$tableName&f_brother=$!f_brother&checkTab=Y&parentTableName=$!parentTableName&draftQuery=$!saveDraft&currentRow=$!values.get("id")&winCurIndex=$!winCurIndex&pageNo=$!pageNo'" class="b2">$text.get("common.lb.back")</button></li>
			#end
			#end
		#end
	#else
		<li><button type="button" id="backList"  name="backList" onClick="closeWin();" class="b2">$text.get("common.lb.close")</button></li>			
	#end
	#if($!OAWorkFlow)
	<li><button type="button" name="" onClick="javascript:flowDepict('$!designId','$values.get('id')');">$text.get("common.lb.checkFlowChart")&nbsp;</button></li>
	#end
	<li><button type="button" onclick="preview();">打印</button></li>
	</ul>
	#end
</div>
<!--start-->
#if($parentName.length()>0)			
			<div class="scroll_function_big"><span>$text.get("common.msg.currstation")：$!parentName</span>
				
			</div>
			#end
	<div id="listRange_id">
	#if($!tabList == "")
		<script type="text/javascript">
			$("#listRange_id").height(document.documentElement.clientHeight-32);
		</script>
	#else
		<script type="text/javascript">
			$("#listRange_id").height(document.documentElement.clientHeight);
		</script>
	#end
		<div class="listRange_1">
#set($layoutHTML=$!mainTable.layoutHTML)
#foreach ($row in $fieldInfos )
#if($row.fieldName == "id")
	<input type="hidden" id="id" name="id" value="$!values.get("id")">
#elseif($row.inputType==3)
	#if($layoutHTML.indexOf("@$row.fieldName@")!=-1)
	#set($layoutHTML = $globals.replaceString($layoutHTML,"@$row.fieldName@","style=display:none;"))
	#else
	<input type="hidden" id="$row.fieldName" name="$row.fieldName" value="$!values.get($row.fieldName)"/>
	#end
#else
	#if($row.fieldType==1)
	#set($layoutHTML=$globals.replaceString($layoutHTML,"#$row.fieldName#","$!globals.newFormatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)"))
	#else
	#set($layoutHTML=$globals.replaceString($layoutHTML,"#$row.fieldName#","$!values.get($row.fieldName)"))
	#end
#end
#end

$globals.replaceTextArea($!layoutHTML)

	 #foreach ($rowlist in $childTableList )
		<input type="hidden" id="${rowlist.tableName}PopupType" name="${rowlist.tableName}PopupType" value="$globals.getTableInfoBean(${rowlist.tableName}).popupType"/> 
	 #end
#foreach ($rowlist in $childTableList ) 
#if("$globals.getSysSetting($rowlist.tableSysType)"=="true")
#set($gridtable =$rowlist.tableName+"Table")
<script language="javascript">
var tableId="$gridtable";
</script>
#if($childTableConfigList.get($rowlist.tableName).size()>0 || $allConfigList.size()==0)
	<div class="scroll_function_small_ud">
		<span style="font-weight:bolder;color:#666666;">$rowlist.display.get($globals.getLocale())</span>
		<div  name="${gridtable}DIV" id="${gridtable}DIV"> 
		<table border="0" cellpadding="0"  cellspacing="0" class="listRange_list_function_b" style="table-layout:fixed;" name="$gridtable" id="$gridtable">
		<thead>
			<tr>
			<td class="listheade" width="35">No.</td>
		#set($colInputType="")
		#set($width=0)
		#if($allConfigList.size()>0)
	 #foreach ($colConfig in $childTableConfigList.get($rowlist.tableName))
	 	  #set ($row="")
	 	  #set ($isExist="false")
	 	  #foreach ($config_row in $rowlist.fieldInfos )
	 		 #if($colConfig.colName==$config_row.fieldName)
	 		 	#set ($row=$config_row)
	 		 	#set ($isExist="true")
	 		 #end
	 	  #end
	 	  #if($isExist=="false")
				#foreach ($row in $rowlist.fieldInfos )
					
					#if("$row.inputType" == "2" || ("$row.inputType"=="6" && "$row.inputTypeOld"=="2"))
						#set($ajaxStr=":")
						#if($row.insertTable.length()>0)
						#foreach($srow in $row.getSelectBean().viewFields)
							#set($ajaxStr=$ajaxStr+$rowlist.tableName+"_"+$globals.getTableField($srow.asName)+";")
						#end
						#end
						
						
						#set($fname = $rowlist.tableName+"_"+$row.fieldName)
			    		#set($nullable = "false")
						#if("$row.isNull" == "0")
				    		#set($nullable = "true")
						#end
						#set($inputValue = "")
						 #foreach($mainField in $row.getSelectBean().tableParams)
							#if($mainField.indexOf("@TABLENAME")>=0)
								#set($index=$mainField.indexOf("_")+1)
								#set($param=$rowlist.tableName+"_"+$mainField.substring($index))
								#set($disP=$rowlist.tableName+"."+$mainField.substring($index))
								
							#else			
								#if(($mainField.indexOf($rowlist.tableName)==-1 &&$globals.getFieldBean($!tableName,$mainField).inputType!=100)||($mainField.indexOf($rowlist.tableName)>=0))
									#set($disP=$!tableName+"."+$mainField)										
								#end
							#end
						 #end
					   	 #set($inputValue = "$rowlist.tableName:$row.fieldName")
					   	 #set($inputValue2 = "$rowlist.tableName"+"_$row.fieldName")
					   	 #if("$row.inputType"=="6" && "$row.inputTypeOld"=="2")
					   	 	#set($inputType2=$row.inputTypeOld)
					   	 #else
					   	 	#set($inputType2=$row.inputType)
					   	 #end
					   	 
						 #if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
					  	 #foreach($srow in $row.getSelectBean().viewFields)
					  	    #if(("$!srow.display"=="" && "$srow.fieldName"=="$colConfig.colName") 
					  	    								|| ("$!srow.display"!="" && "$srow.display"=="$colConfig.colName"))
								 #if($srow.display.length()>0)
								 	#set($index=$srow.display.indexOf(".")+1)
									#set($tableField=$rowlist.tableName+"."+$srow.display.substring($index))
									#set($dis = $globals.getFieldDisplay($srow.display))
									#if($dis==""||$dis.indexOf("not Exist")>=0)
									#set($dis = $globals.getFieldDisplay($tableField))
									#end
								 #else
								 	#set($dis="")
								 #end				
								 #if($dis == "") 
									#set($dis = $globals.getFieldDisplay($rowlist.tableName,$row.getSelectBean().name,$srow.fieldName)) 
								 #end
								 #set($disField = $rowlist.tableName+"_"+$globals.getTableField($srow.asName))	
								 #set($disWidth = "")
								 #set($returnValue=$globals.getFieldWidth("$rowlist.tableName","$globals.getTableField($srow.asName)"))
								 #if($returnValue!=0)
								 	#set($disWidth=$returnValue) 
								 #else
								 	#set($disWidth=$srow.width) 
								 #end	    
								 #if($globals.getScopeRight($rowlist.tableName,$row.fieldName,$scopeRight))    
								 	<td width="$disWidth">$dis</td>
								 	#set($width=$width+$disWidth)
								 	#if($srow.display.indexOf("GoodsCode")>=0)
								 		#set($colInputType=$colInputType+$inputType2+":"+":"+$row.isStat+":GoodsFullName,")
					    			#else
								 		#set($colInputType=$colInputType+$inputType2+":"+":"+$row.isStat+":"+$row.fieldType+",")
								 	#end
								 #else
								 	#set($colInputType=$colInputType+"-2:"+":"+$row.fieldType+",")
								 #end
							#end	
						 #end
						 #end
					   #end
				  #end
				  
		  #else
		  	  #set($fname = $rowlist.tableName+"_"+$row.fieldName)
			  #if($globals.getScopeRight($rowlist.tableName,$row.fieldName,$scopeRight))
			  	#if("$row.inputType"=="6")
					 #set($inputType2=$row.inputTypeOld)
				#else
					 #set($inputType2=$row.inputType)
				#end
			    #if("$row.inputType" != "100" && $row.fieldName != "id" && $row.fieldName != "f_ref" && $row.fieldName != "f_brother")					
					#set($defValue="")
					#if($globals.getUrlBillDef($fname).length()>0)
				  		#set($defValue=$globals.getUrlBillDef($fname))
				  	#elseif($globals.getUrlBillDef($fname).length()==0)
						#set($defValue=$row.getDefValue())
				 	#end
				    #set($nullable = "false")
					#if("$row.isNull" == "0")
					    #set($nullable = "true")
					#end
					#set($inputValue = "")
					#if("$row.inputType" == "1" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "1"))
						  #foreach($erow in $globals.getEnumerationItems($row.refEnumerationName))
							  #set($inputValue = "$inputValue;$erow.value:$erow.name")
						  #end
						  #set($colInputType=$colInputType+$inputType2+":"+$!defValue+":"+$row.isStat+":"+$inputValue+",")
						#set($width=$width+$row.width)
						 <td width="$row.width">$!row.display.get("$globals.getLocale()")</td>
				    #elseif("$row.inputType"=="2" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "2"))
					 	#set($inputValue = "$rowlist.tableName:$row.fieldName")
						#set($inputValue2 = "$rowlist.tableName"+"_$row.fieldName")
						#foreach($mainField in $row.getSelectBean().tableParams)
							#if($mainField.indexOf("@TABLENAME")>=0)
								#set($index=$mainField.indexOf("_")+1)
								#set($param=$rowlist.tableName+"_"+$mainField.substring($index))
								#set($disP=$rowlist.tableName+"."+$mainField.substring($index))
								
							#else			
								#if(($mainField.indexOf($rowlist.tableName)==-1 &&$globals.getFieldBean($!tableName,$mainField).inputType!=100)||($mainField.indexOf($rowlist.tableName)>=0))
									#set($disP=$!tableName+"."+$mainField)
															
								#end
							#end
						#end
						#if($row.getSelectBean().relationKey.hidden)
								#set($colInputType=$colInputType+"-2:"+$!defValue+",")
						#else
								#if("$row.inputType" == "2" and "$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1")<!--序列号字段-->
								   #set($colInputType=$colInputType+"-2:"+$!defValue+",")
									<td width="$row.width">$!row.display.get("$globals.getLocale()")</td>
									#set($width=$width+$row.width)
									#set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":seq,")
								#else
									<td width="$row.width">$!row.display.get("$globals.getLocale()")</td>
									#set($width=$width+$row.width)									
									#set($colInputType=$colInputType+$inputType2+":"+$!defValue+":"+$row.isStat+":"+$row.fieldType+",")	
					  			#end					  			
						#end
				    #elseif("$row.inputType" == "5" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "5"))
					  	  #foreach($erow in $globals.getEnumerationItems($row.refEnumerationName))
							  #set($inputValue = "$inputValue;$erow.value:$erow.name")
						  #end		
						  #set($colInputType=$colInputType+$row.inputTypeOld+":"+$!defValue+":"+$row.isStat+":"+$inputValue+",")			  
					      <td width="$row.width">$!row.display.get("$globals.getLocale()")</td>
					      #set($width=$width+$row.width)
					#elseif("$row.inputType" == "4" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "4"))
						#set($fhname=$fname+"_lan")		
						#set($colInputType=$colInputType+$row.inputType2+":"+$!defValue+":"+$row.isStat+":"+$row.fieldType+",")	
						<td width="$row.width">$!row.display.get("$globals.getLocale()")</td>
						#set($width=$width+$row.width)
						  #set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+",")
							
					#elseif("$row.inputType" == "3")			
						#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+",")
					#else
						#set($colInputType=$colInputType+$inputType2+":"+$!defValue+":"+$row.isStat+":"+$row.fieldType+",")
						<td width="$row.width">$!row.display.get("$globals.getLocale()")</td>	
						#set($width=$width+$row.width)
					#end
				#end
			  #else
			  	#set($nullable = "false")
				#if("$row.isNull" == "0")
					 #set($nullable = "true")
				#end
				#set($inputValue = "")	
				#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+",")
			  #end
		    #end
	    #end
	    #foreach ($config_row in $rowlist.fieldInfos )
	    	 #set($fname = $rowlist.tableName+"_"+$config_row.fieldName)
	    	 #set($defValue="")
			 #if($globals.getUrlBillDef($fname).length()>0)
				 #set($defValue=$globals.getUrlBillDef($fname))
			 #elseif($globals.getUrlBillDef($fname).length()==0)
				 #set($defValue=$config_row.getDefValue())
			 #end
			 #set($nullable = "false")
			 #if("$config_row.isNull" == "0")
				 #set($nullable = "true")
			 #end
			 #set($inputValue = "")
	 	     #if("$config_row.inputType"=="3")
				#set($colInputType=$colInputType+"-2:"+$!defValue+",")
	 	     #elseif("$config_row.inputType"=="2" && $config_row.getSelectBean().viewFields.size()>0)
				#if($config_row.getSelectBean().relationKey.hidden)
					#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+",")			
				#else
					#set ($isExist="false")
					#foreach ($colConfig in $childTableConfigList.get($rowlist.tableName))
	 	     			#if("$!config_row.fieldName"=="$!colConfig.colName")
	 	     				#set ($isExist="true")
	 	     			#end
	 	     		#end
	 	     		#if("$isExist"=="false")
					#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+",")	
					#end
				#end
				#if(!("$config_row.fieldSysType"=="GoodsField" and $globals.getPropBean($config_row.fieldName).isSequence=="1"))
	 	     	#foreach($srow in $config_row.getSelectBean().viewFields)
	 	     		#set ($isExist="false")
	 	     		#foreach ($colConfig in $childTableConfigList.get($rowlist.tableName))
	 	     			#if(("$!srow.display"=="" && "$srow.fieldName"=="$colConfig.colName") 
	 	     						|| ("$!srow.display"!="" && "$srow.display"=="$colConfig.colName"))
	 	     				#set ($isExist="true")
	 	     			#end
	 	     		#end
	 	     		#if("$isExist"=="false")
	 	     			#if($srow.display.length()>0)
							#set($index=$srow.display.indexOf(".")+1)
							#set($tableField=$rowlist.tableName+"."+$srow.display.substring($index))
							#set($dis = $globals.getFieldDisplay($srow.display))
							#if($dis==""||$dis.indexOf("not Exist")>=0)
							#set($dis = $globals.getFieldDisplay($tableField))
							#end
						#else
							#set($dis="")
						#end				
						#if($dis == "") 
							#set($dis = $globals.getFieldDisplay($rowlist.tableName,$row.getSelectBean().name,$srow.fieldName)) 
						#end
						#set($disField = $rowlist.tableName+"_"+$globals.getTableField($srow.asName))
						#set($returnValue=$globals.getFieldWidth("$rowlist.tableName","$globals.getTableField($config_row.asName)"))			        
						#if($returnValue!=0)
								 #set($disWidth=$returnValue) 
						#else
								#set($disWidth=$config_row.width) 
						#end
						#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+",")	
				    #end
				 #end
	 	     	 #end
	 	     #elseif("$config_row.inputType" != "100"  && $config_row.fieldName != "id" && $config_row.fieldName != "f_ref" && $config_row.fieldName != "f_brother")
	 	     	#set ($isExist="false")
	 	     	#foreach ($colConfig in $childTableConfigList.get($rowlist.tableName))
	 	     		#if("$config_row.fieldName"=="$colConfig.colName")
	 	     			#set ($isExist="true")
	 	     		#end
	 	     	#end
	 	     	#if("$isExist"=="false" && "$!config_row.display"!="")
					#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+",")	
	 	     	#end
	 	     #end
	   #end
    #else
       #foreach ($row in $rowlist.fieldInfos )
       	  #set($fname = $rowlist.tableName+"_"+$row.fieldName)
       	  #set($flag=true)
       	  #if($!moduleTable&&$!moduleTable.get($rowlist.tableName))
       	  	#set($flag=$!moduleTable.get($rowlist.tableName).contains($row.fieldName))
       	  #end
		    #if("$row.inputType" != "100" && $row.fieldName != "id" && $row.fieldName != "f_ref" && $row.fieldName != "f_brother")	
		   	 #if(!$globals.getScopeRight($rowlist.tableName,$row.fieldName,$scopeRight)||!$flag)
       	   		#set($colInputType=$colInputType+"-2::0:,")
       	 	 #else			  
				  #set($defValue="")
				  #if($globals.getUrlBillDef($fname).length()>0)
				  	#set($defValue=$globals.getUrlBillDef($fname))
				  #elseif($globals.getUrlBillDef($fname).length()==0)
					#set($defValue=$row.getDefValue())
				  #end
			      #set($nullable = "false")
				  #if("$row.isNull" == "0")
				    #set($nullable = "true")
				  #end
				
				  #set($inputValue = "")
				  #if("$row.inputType" == "1" )
					  #foreach($erow in $globals.getEnumerationItems($row.refEnumerationName))
						  #set($inputValue = "$inputValue;$erow.value:$erow.name")
					  #end
					 #set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":"+$inputValue+":"+$row.fieldType+",")
					 #set($width=$width+$row.width)	
					 <td width="$row.width">$!row.display.get("$globals.getLocale()")</td>
				  #elseif("$row.inputType" == "5")
				  	  #foreach($erow in $globals.getEnumerationItems($row.refEnumerationName))
						  #set($inputValue = "$inputValue;$erow.value:$erow.name")
					  #end
					  #set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":"+$inputValue+":"+$row.fieldType+",")
					  #set($width=$width+$row.width)
				      <td width="$row.width">$!row.display.get("$globals.getLocale()")</td>					  
				  #elseif("$row.inputType" == "2")
					#set($ajaxStr=":")
					#if($row.insertTable.length()>0)
					#foreach($srow in $row.getSelectBean().viewFields)
						#set($ajaxStr=$ajaxStr+$rowlist.tableName+"_"+$globals.getTableField($srow.asName)+";")
					#end
					#end
						
					  #set($inputValue = "$rowlist.tableName:$row.fieldName")
					  #set($inputValue2 = "$rowlist.tableName"+"_$row.fieldName")	
					#foreach($mainField in $row.getSelectBean().tableParams)
						#if($mainField.indexOf("@TABLENAME")>=0)
							#set($index=$mainField.indexOf("_")+1)
							#set($param=$rowlist.tableName+"_"+$mainField.substring($index))
							#set($disP=$rowlist.tableName+"."+$mainField.substring($index))
							
						#else 
							#if(($mainField.indexOf($rowlist.tableName)==-1 &&$globals.getFieldBean($!tableName,$mainField).inputType!=100)||($mainField.indexOf($rowlist.tableName)>=0))
								#set($disP=$!tableName+"."+$mainField)									
							#end
						#end
					#end
					
					#if($row.getSelectBean().relationKey.hidden)
					     #if("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1")
							#set($colInputType=$colInputType+"-2:"+$!defValue+",")
							#set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":"+$row.fieldType+",")
							#set($width=$width+$row.width)
							<td width="$row.width">$!row.display.get("$globals.getLocale()")</td>							
						 #else
							#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+",")				
						 #end
					#else
						#if("$row.inputType" == "2" and "$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1")
							#set($colInputType=$colInputType+"-2:"+$!defValue+",")
							#set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":seq,")
							#set($width=$width+$row.width)
							<td width="$row.width">$!row.display.get("$globals.getLocale()")</td>	
						#else
							
							#set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":"+$row.fieldType+",")
							#set($width=$width+$row.width)
							<td width="$row.width">$!row.display.get("$globals.getLocale()")</td>
			  			#end
				    #end
				 #if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
				 	
					#foreach($srow in $row.getSelectBean().viewFields)
						 #if($srow.display.length()>0)
						 	#set($index=$srow.display.indexOf(".")+1)
							#set($tableField=$rowlist.tableName+"."+$srow.display.substring($index))
							#set($dis = $globals.getFieldDisplay($srow.display))
							#if($dis==""||$dis.indexOf("not Exist")>=0)
							#set($dis = $globals.getFieldDisplay($tableField))
							#end
						 #else
						 	#set($dis="")
						 #end				
						 #if($dis == "") 
							#set($dis = $globals.getFieldDisplay($rowlist.tableName,$row.getSelectBean().name,$srow.fieldName)) 
						 #end
						 #set($disField = $rowlist.tableName+"_"+$globals.getTableField($srow.asName))	
						 #set($returnValue=$globals.getFieldWidth("$rowlist.tableName","$globals.getTableField($srow.asName)"))
						 #if($returnValue!=0)
							#set($disWidth=$returnValue) 
						 #else
							#set($disWidth=$srow.width) 
						 #end
					    #if("$!srow.hiddenInput"!="true")	
					    	#if($tableField.indexOf("GoodsCode")>=0)
					    		#set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":GoodsFullName,")	
					    	#else
								#set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":"+$row.fieldType+",")	
							#end
							#set($width=$width+$disWidth)
					    	<td width="$disWidth">$dis</td>	  
					 	#else
							#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+",")	
					    #end
					    #set($tableField="")
					#end
			     #end
				#elseif("$row.inputType" == "3" || "$row.inputType" == "6")	
					#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+",")	
				#elseif("$row.inputType" == "4")
					#set($fhname=$fname+"_lan")	
					#set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":"+$row.fieldType+",")	
					#set($width=$width+$row.width)
					<td width="$row.width">$!row.display.get("$globals.getLocale()")</td>	  
					#set($colInputType=$colInputType+"-2:"+$!defValue+",")		
				#else	
					#set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":"+$row.fieldType+",")		
					#set($width=$width+$row.width)
					<td width="$row.width">$!row.display.get("$globals.getLocale()")</td>	  
				#end
			#end
			#end
		#end
    #end	
    	
		</tr>
		</thead>
		<tbody>
		#set($tableNamec=$rowlist.tableName)
		#set($count=0)
		#foreach($rowlist in $result) 
		
		#if($globals.get($rowlist,0).equals($tableNamec))
		#set($colobjs=$colInputType.substring(0,$globals.subtracter($colInputType.length(),1)).split(","))

		#foreach ($row in $globals.get($rowlist,1))		
		#set($rowobjs=$globals.get($row,0).substring(1,$globals.subtracter($globals.get($row,0).length(),1)).split("','"))
		#set($GoodsFullName="")
		#foreach ($col in $colobjs)
			#if($!globals.get($col.split(":"),3).equals("GoodsFullName"))
				#set($GoodsFullName=$globals.get($rowobjs,$globals.subtracter($velocityCount,1)))
			#end
		#end
		
		<tr>
		<td class="listheadonerow">$velocityCount</td>		
		#foreach ($col in $colobjs)
			#if(!$globals.get($col.split(":"),0).equals("-2"))
			#set($value="")
			#if($globals.get($rowobjs,$globals.subtracter($velocityCount,1)).length()>0)
			#set($value=$globals.get($rowobjs,$globals.subtracter($velocityCount,1)))
			#elseif(!$globals.get($col.split(":"),1).length()>0)
			#set($value=!$globals.get($col.split(":"),1))
			#else
			#set($value="&nbsp;")
			#end
			#if($globals.get($col.split(":"),0).equals("1"))
				#set($index=$col.indexOf(";")+1)
				#set($inputVals=$col.substring($index).split(";"))
				#foreach ($inputVal in $inputVals)
					#if($globals.get($inputVal.split(":"),0).equals($value))
						#set($value=$globals.get($inputVal.split(":"),1))
					#end
				#end
			#elseif($globals.get($col.split(":"),0).equals("5"))
				#set($index=$col.indexOf(";")+1)
				#set($inputVals=$col.substring($index).split(";"))
				#set($values=$value.split(","))
				#set($value="")
				#foreach ($inputVal in $inputVals)
					#foreach($val in $values)
						#if($globals.get($inputVal.split(":"),0)=="$val")
						#set($value=$value+$globals.get($inputVal.split(":"),1)+",")
						#end
					#end	
				#end
				#if($value.length()>0)
					#set($value=$value.substring(0,$globals.subtracter($value.length(),1)))
				#else
					#set($value="&nbsp;")
				#end
			#end
			#if($globals.get($col.split(":"),3).equals("0") || $globals.get($col.split(":"),3).equals("1"))	
			<td align="right">$value</td>
			#elseif($globals.get($col.split(":"),3).equals("seq"))	
			<td align="left">$value#if($value!="&nbsp;")<img src='/style/images/St.gif' class='search' onClick="window.open('ReportDataAction.do?reportNumber=ReportSeqDispose&BillNo=$!values.get("BillNo")&GoodsFullName=$globals.encode($!GoodsFullName)&queryChannel=normal&LinkType=@URL:')" />#end</td>
			#else
			<td align="left">$value</td>
			#end
			#end
		#end
		</tr>
		#set($!count=$!count+1)
		#end
		<!--统计行  -->
		<tr style="background:#FFFFEE">
		<td align="left">&nbsp;</td>
		#set($count=0)
		#foreach ($col in $colobjs)
		#if(!$globals.get($col.split(":"),0).equals("-2"))
			#if($globals.get($col.split(":"),2).equals("1"))
			#set($total="0")
			#foreach($row in $globals.get($rowlist,1))
			#set($rowobjs=$globals.get($row,0).substring(1,$globals.subtracter($globals.get($row,0).length(),1)).split("','"))
			#set($total=$globals.add($total,$globals.get($rowobjs,$count)))
			#end
			<td align="right">$total</td>
			#else
			<td align="left">&nbsp;</td>
			#end
		#end
		#set($count=$count+1)
		#end
		</tr>
		#end
		#end		
		</tbody>
		</table>
		</div>
		<script language="javascript">
			document.getElementById("$gridtable").width=$width+30
		</script>
	</div>	
#end	
#end
 #end
#if($!OAWorkFlow)
<div class="listRange_1_photoAndDoc_1">
<b>$text.get("common.sign.advice")</b>
<br><br>
#foreach($!deliver in $!delivers)
<li style="width:95%;">$text.get("oa.workFlow.step")：$!workFlowDesignBeans.get($!designId).getFlowNodeMap().get($!deliver.nodeId).display&nbsp;&nbsp;&nbsp;$text.get("common.sign.human")：$!deliver.empFullName&nbsp;&nbsp;&nbsp;$text.get("common.sign.time")：$!deliver.attTime</li>
<li style="width:95%;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$!deliver.deliverance.replaceAll("\r\n","<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")</li>
<br><br>
#end
</div>
<!--end-->
<div class="listRange_1_photoAndDoc_1">
<b>$text.get("oa.flow.affix.down")</b>&nbsp;&nbsp;<button type=button class="b2" onClick="addAffix()">$text.get("com.flow.add.affix")</button>
<div id="files_preview" style="margin-left:1px; background: none;"></div>
<br><br>
#foreach($!deliver in $!affixs)
<li style="width:95%;margin-top: 10px;">
		$text.get("oa.add.affix.person")：$!deliver.empFullName&nbsp;&nbsp;&nbsp;$text.get("oa.flow.upload.time")：$!deliver.attTime</li>&nbsp;&nbsp;&nbsp;
		$text.get("oa.flow.affix.name")：







		#foreach($affix in $globals.strSplitByFlag($deliver.affix,';'))
		$affix <a href="/ReadFile?fileName=$globals.urlEncode($affix)&type=AFFIX&tableName=$tableName&tempFile=previewFile" target="_blank">预览</a>&nbsp;&nbsp; <a href="/ReadFile?fileName=$globals.urlEncode($affix)&realName=$globals.urlEncode($affix)&tempFile=false&type=AFFIX&tableName=$tableName&down=true" target="_blank">$text.get("common.lb.download")</a>
		#end
#end
<br><br>
<input type="hidden" id="attachFiles" name="attachFiles" value="">
<input type="hidden" id="delFiles" name="delFiles" value="">
</div>
<script type="text/javascript">
/*
function addAffix(){
	var retVal = window.showModalDialog("/OAMyWorkFlow.do?keyId=$!values.get('id')&tableName=$!tableName&operation=6",this,"dialogWidth=750px;dialogHeight=450px") ;
	if(retVal=="deliverTo"){			
		location.href='/UserFunctionAction.do?tableName=$!tableName&keyId=$!values.get("id")&moduleType=$!moduleType&f_brother=$!f_brother&operation=$globals.getOP("OP_DETAIL")&winCurIndex=$!winCurIndex&pageNo=$!pageNo&parentCode=$!parentCode&parentTableName=$!parentTableName&saveDraft=$!saveDraft&noback=$!noback'
	}
}
*/
</script>
<script type="text/javascript">
function addAffix(){
	asyncbox.open({
		id : 'dealdiv',
　　　   url : '/OAMyWorkFlow.do?keyId=$!values.get("id")&tableName=$!tableName&operation=6',
	 	title : '$text.get("com.flow.add.affix")',	width : 730,height : 390,
        btnsbar : jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
			 if(action == 'ok'){
				if(opener.beforSubmit(opener.form)){
					opener.form.submit();
				}
				return false;
			}
	　　  }
	});
}
function dealAsyn(){
	location.href='/UserFunctionAction.do?tableName=$!tableName&keyId=$!values.get("id")&moduleType=$!moduleType&f_brother=$!f_brother&operation=$globals.getOP("OP_DETAIL")&winCurIndex=$!winCurIndex&pageNo=$!pageNo&parentCode=$!parentCode&parentTableName=$!parentTableName&saveDraft=$!saveDraft&noback=$!noback';
}

</script>

#end

</div>
</form> 
</body>
<script type="text/javascript">
#foreach($row in $fieldInfos)
#if($row.fieldType==16 && $row.inputType!=3 && $row.inputType!=6)
var $row.fieldName  ;
KindEditor.ready(function(K) {
	var editor = jQuery("#$row.fieldName") ;
	if(typeof(editor.attr("name"))!="undefined"){
	$row.fieldName = K.create('textarea[name="$row.fieldName"]',{
		uploadJson:'/UtilServlet?operation=uploadFile'
		#if($row.inputType==8)
		,readonlyMode : true
		#end
	});
	}
});
#end
#end
#foreach ($row in $fieldInfos)
	#if("$row.inputType"=="3")
	var varField = jQuery("#$row.fieldName") ;
	if(varField.attr("type")!="hidden"){
		varField.after("*********") ;
		varField.remove();
	}
	#elseif("$row.inputType"=="2")
	var html = '' ;
	var varField = jQuery("#$row.fieldName") ;
	#if($row.getSelectBean().relationKey.hidden)
	html += '<input id="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" name="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" type="hidden" value="$!values.get($row.fieldName)">' ;
	#else
		#if("$row.fieldType" == "1")
			html += '<input id="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" name="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" #if("$row.inputType" == "8") readonly="readonly" style="color: #C0C0C0;" #end '
			     + 'type="text"  size="20" onKeyDown="if(event.keyCode==13&&this.value.length>0) event.keyCode=9" #if("$row.popupType"!="2") onDblClick="$popUrlValue" #end  #if("$row.popupType"!="1") onKeyPress="if(event.keyCode ==13){$popUrlValue}"#end value="#if($!isDetail)$!globals.formatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)#else$!globals.newFormatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)#end"><img onClick="$popUrlValue" src="/$globals.getStylePath()/images/St.gif"> ';
		#else
			html += '<input id="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" name="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" #if("$row.inputType" == "8") readonly="readonly" style="color: #C0C0C0;" #end '
			 	 = 'type="text"  size="20" onKeyDown="if(event.keyCode==13&&this.value.length>0) event.keyCode=9" #if("$row.popupType"!="2") onDblClick="$popUrlValue" #end  #if("$row.popupType"!="1") onKeyPress="if(event.keyCode ==13){$popUrlValue}"#end value="$!values.get($row.fieldName)"><img onClick="$popUrlValue" src="/$globals.getStylePath()/images/St.gif">' ;					
		#end
	#end
	#if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
		#foreach($srow in $row.getSelectBean().viewFields)
			#set($mainInput=$globals.getMainInputValue("$mainTable.tableName","$row.fieldName"))    
			#set($popUrlValue="if(popMainInput(\'$mainInput\'))openSelect(\'/UserFunctionAction.do?keyId=$!values.get($row.fieldName)&tableName=$mainTable.tableName&fieldName=$row.fieldName&operation="+$globals.getOP("OP_POPUP_SELECT")+"\'#foreach($mainField in $row.getSelectBean().tableParams)+\'&$mainField=\'+n(\'$mainField\')[0].value#end,this,\'$row.fieldName\')")
			#set($ajaxUrlValue="\'/UtilServlet?operation=Ajax&tableName=$mainTable.tableName&fieldName=$row.fieldName\' #foreach($mainField in $row.getSelectBean().tableParams)+\'&$mainField=\'+n(\'$mainField\')[0].value#end")
			#set($openUrlValue="\'/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&tableName=$mainTable.tableName&fieldName=$row.fieldName\' #foreach($mainField in $row.getSelectBean().tableParams)+\'&$mainField=\'+n(\'$mainField\')[0].value#end")
			#if($globals.getFieldBean($tableName,$srow.display.substring($index)).fieldType==1||$globals.getFieldBean($srow.fieldName).fieldType==1)
				html += '<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)" #if("$row.inputType" == "8") readonly="readonly" style="color: #C0C0C0;" #end '
			 		 + 'onKeyDown="if(event.keyCode==13&&this.value.length>0) {event.keyCode=9;if(popMainInput(\'$globals.getMainInputValue("$mainTable.tableName","$row.fieldName")\'))mainSelect($ajaxUrlValue,$openUrlValue,this,\'$row.fieldName\')}" type="text" #if("$row.popupType"!="2") #if("$row.inputType" != "8")onDblClick="$popUrlValue" #end #end  size="20" #if("$row.popupType"!="1") onKeyPress="if(event.keyCode ==13){ $popUrlValue}" #end  value="$!globals.newFormatNumber($!values.get($srow.asName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)">#if("$row.inputType" != "8")<img onClick="$popUrlValue" src="/$globals.getStylePath()/images/St.gif">#end ';
			#else
				html += '<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)" #if("$row.inputType" == "8") readonly="readonly" style="color: #C0C0C0;" #end '
			         + 'onKeyDown="if(event.keyCode==13&&this.value.length>0) {event.keyCode=9;if(popMainInput(\'$globals.getMainInputValue("$mainTable.tableName","$row.fieldName")\'))mainSelect($ajaxUrlValue,$openUrlValue,this,\'$row.fieldName\')}" type="text" #if("$row.popupType"!="2") #if("$row.inputType" != "8")onDblClick="$popUrlValue" #end #end  size="20" #if("$row.popupType"!="1") onKeyPress="if(event.keyCode ==13){ $popUrlValue}" #end  value="$!values.get($srow.asName)">#if("$row.inputType" != "8")<img onClick="$popUrlValue" src="/$globals.getStylePath()/images/St.gif">#end ' ;
			#end
		#end	
	#end
	varField.after(html) ;
	varField.remove();
	#elseif("$row.fieldType" == "16")
	var varField = jQuery("#$row.fieldName") ;
	varField.css("visibility","hidden") ;	
	#elseif("$row.fieldType"=="13")
	var varField = jQuery("#$row.fieldName") ;
	var html = '<button id="picbuttonthe222" name="picbuttonthe222" onClick="upload(\'PIC\');" class="b4">$text.get("upload.lb.picupload")</button><ul id="picuploadul"></ul>' ;
			 + '<ul id="picuploadul">'
	#foreach($uprow in $PIC)
	#if($uprow.indexOf(":") > -1)					
	html += '<li style="background:url();" id="uploadfile_$!globals.get($uprow.split(":"),0)"><input type=hidden id="uploadpic" name=uploadpic value="$uprow">' ;
			+ '<a href="/ReadFile.jpg?fileName=$globals.urlEncode($!globals.get($uprow.split(":"),0))&realName=$globals.urlEncode($!globals.get($uprow.split(":"),1))&tempFile=false&type=PIC&tableName=$tableName" target="_blank"><img src="/ReadFile.jpg?fileName=$globals.urlEncode($!globals.get($uprow.split(":"),0))&realName=$globals.urlEncode($!globals.get($uprow.split(":"),1))&tempFile=false&type=PIC&tableName=$tableName" width="150" height="150" border="0"></a><div>$!globals.get($uprow.split(":"),1)&nbsp;&nbsp;#if("$detail" != "detail")<a href="javascript:deleteupload("$!globals.get($uprow.split(":"),0)","false","$tableName","PIC");">$text.get("common.lb.del")</a>#end</div>' 
			+ '</li>'
	#else
	html += '<li style="background:url();" id="uploadfile_$uprow"><input type=hidden id="uploadpic" name=uploadpic value="$uprow">'
			 + '<a href="/ReadFile.jpg?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=PIC&tableName=$tableName" target="_blank"><img src="/ReadFile.jpg?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=PIC&tableName=$tableName" width="150" height="150" border="0"></a><div>$uprow&nbsp;&nbsp;#if("$detail" != "detail")<a href="javascript:deleteupload("$uprow","false","$tableName","PIC");">$text.get("common.lb.del")</a>#end</div>' ;
	</li>
	#end
	#end
	html += '</ul>' ;
	varField.after(html) ;
	varField.remove();
	#elseif("$row.fieldType"=="14")
	var varField = jQuery("#$row.fieldName") ;
	var html = '<button id="affixbuttonthe222" type="button" name="affixbuttonthe222" onClick="upload(\'AFFIX\');" class="b4">$text.get("upload.lb.affixupload")</button><ul id="affixuploadul"></ul>' ;
	html += '<ul id="affixuploadul">' ;
	#foreach($uprow in $AFFIX)	
	#if($uprow.indexOf(":") > -1)			
	html += '<li style="background:url();" id="uploadfile_$!globals.get($uprow.split(":"),0)"><input type=hidden id="uploadaffix" name=uploadaffix value="$uprow">$!globals.get($uprow.split(":"),1)' 			
		 + '<a href=\'javascript:deleteupload("$!globals.get($uprow.split(":"),0)","false","$tableName","AFFIX");\'>$text.get("common.lb.del")</a>' 
		 + '&nbsp;&nbsp;&nbsp;&nbsp;<a href="/ReadFile?fileName=$globals.urlEncode($!globals.get($uprow.split(":"),0))&realName=$globals.urlEncode($!globals.get($uprow.split(":"),1))&tempFile=false&type=AFFIX&tableName=$tableName&down=true" target="_blank">$text.get("common.lb.download")</a>' ;
		 + '</li> ' ;
	#else
	html += '<li style="background:url();" id="uploadfile_$uprow">'
		 + '<input type=hidden id="uploadaffix" name=uploadaffix value="$uprow">$uprow'			
		 + '<a href=\'javascript:deleteupload("$uprow","false","$tableName","AFFIX");\'>$text.get("common.lb.del")</a>'
		 + '&nbsp;&nbsp;&nbsp;&nbsp;<a href="/ReadFile?fileName=$globals.urlEncode($uprow)&type=AFFIX&tableName=$tableName&tempFile=previewFile" target="_blank">预览</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="/ReadFile?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=AFFIX&tableName=$tableName&down=true" target="_blank">$text.get("common.lb.download")</a>'
		 + '</li>' ;
	#end
	#end
	html += '</ul>' ;
	varField.after(html) ;
	varField.remove();
	#end
#end
</script>
</html>