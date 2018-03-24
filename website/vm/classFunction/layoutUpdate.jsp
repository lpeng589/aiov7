<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>#if("$!moduleName"=="")$!mainTable.display.get("$globals.getLocale()")#else$!moduleName#end</title>
<link rel="stylesheet" href="/style/css/layout.css" type="text/css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script language="javascript" src="$globals.js("/js/editPage.vjs","",$text)"></script>
<script language="javascript" src="/js/editGrid.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="$globals.js("/js/Main.vjs","",$text)"></script>
<script language="javascript" src="/js/setTime.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script language="javascript" src="$globals.js("/js/define.vjs",$mainTable.tableName,$text)"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<script language="javascript">

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
var printRight = $globals.getMOperationMap($request).print() ;
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

var childRowCount=0 ;
#foreach ($rowlist in $result ) 
	#set($griddata =$globals.get($rowlist,0)+"Data")
	#foreach ($row in $globals.get($rowlist,1) )
addRows($griddata,$globals.get($row,0));
childRowCount=childRowCount+1;
	#end
#end
##$globals.changeDetInputType("$!tableName","$!currNodeId","$!MOID")

#foreach ($row in $fieldInfos )
#if("$row.inputType" != "100" && $row.fieldName != "id"  && $row.fieldName != "createBy" && $row.fieldName != "createTime" && $row.fieldName != "lastUpdateBy" && $row.fieldName != "lastUpdateTime")
#if("$row.inputType" == "0"||"$row.inputType" == "7" || "$row.inputType" == "4" || "$row.inputType" == "8")		
#if("$row.inputType" == "4")
putValidateItem("$row.fieldName",'$!row.display.get("$globals.getLocale()")',"$row.getStringType()","$!row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),false);
#elseif("$row.fieldType"=="16")
putValidateItem("$row.fieldName",'$!row.display.get("$globals.getLocale()")',"#if("$!Safari"=="true")any#else$row.getStringType()#end","$!row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),false);
#elseif("$row.fieldType" == "13")
putValidateItem("uploadpic",'$!row.display.get("$globals.getLocale()")',"$row.getStringType()","$!row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),false);
#elseif("$row.fieldType" == "14")
putValidateItem("uploadaffix",'$!row.display.get("$globals.getLocale()")',"$row.getStringType()","$!row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),false);				
#else
putValidateItem("$row.fieldName",'$!row.display.get("$globals.getLocale()")',"$row.getStringType()","$!row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),true);
#end
#elseif("$row.inputType" == "2"||"$row.inputType" == "1")		
putValidateItem("$row.fieldName",'$!row.display.get("$globals.getLocale()")',"$row.getStringType()","$!row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),true);	
#elseif("$row.inputType" == "5" || "$row.inputType" == "10")		
putValidateItem("$row.fieldName",'$!row.display.get("$globals.getLocale()")',"checkBox","$!row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),true);	
#end		
#end	
#end

function initFieldValue(){
#foreach ($row in $fieldInfos )
	#if($row.inputType==5 || $row.inputType==10 || ($row.inputTypeOld==8 && ($row.inputType==5 || $row.inputType==10)))
		var fields = document.getElementsByName("$row.fieldName") ;
		for(var i=0;i<fields.length;i++){
			if("$!values.get("$row.fieldName")".indexOf(fields[i].value+",")!=-1 || "$!values.get("$row.fieldName")" == fields[i].value){
				fields[i].checked = true ; 
			}
		}
	#elseif($row.inputType==1)
		var fields = $("#$row.fieldName") ;
		fields.attr("value","$!values.get("$row.fieldName")") ;
	#end
#end
}



function deliverTo(url,retValUrl){
	$("#retValUrl").val(retValUrl);
	asyncbox.open({
		id : 'dealdiv',
　　　   	url : url,
	 	title : '转交',
　　 　 	width : 650,
　　 　	height : 370,
        btnsbar : jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
	    if(action == 'ok'){
			if(opener.beforSubmit(opener.form)){
				opener.form.submit();
			}
			return false;
		}else if(action == 'close'){
			jQuery.close('dealdiv');
		}else if(action == 'cancel'){
			jQuery.close('dealdiv');
		}
　　  　}
　 });
}


function dealAsyncbox(){
	var retValUrl = $("#retValUrl").val();
	jQuery.close('dealdiv');
	if(retValUrl==null || retValUrl.length==0){
		closeWin();
	}else{
		window.location.href = retValUrl;
	}
}


</script>
</head>

<body scroll="no" onLoad="initFieldValue();showStatus();  #foreach ($rowlist in $childTableList ) #if("$globals.getSysSetting($rowlist.tableSysType)"=="true" && ($childTableConfigList.get($rowlist.tableName).size()>0 || $allConfigList.size()==0)) #set($gridtable =$rowlist.tableName+"Table")  #set($griddata =$rowlist.tableName+"Data") showtable('$gridtable');  initTableList(${gridtable}DIV,$gridtable,$griddata,$rowlist.defRowCount);#end #end  initCalculate();initMainCaculate();#if("$quote"=="quote" && $mainTable.triggerExpress==1)loadFireEvent(); #end #if("$detail"=="detail") readonlyForm(document.form); #end execStat();#if( "$!globals.getVersion()"=="3"  || "$!globals.getVersion()"=="12" || "$!globals.getVersion()"=="13"  || "$!globals.getVersion()"=="11")initCountCal();#end">
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
<input type="hidden" id="noback" name="noback" value="$!noback">
<input type="hidden" id="logicValidate" name="logicValidate" value="">
<input type="hidden" id="moduleType" name="moduleType" value="$!moduleType">
<input type="hidden" id="defineName" name="defineName" value="">
<input type="hidden" id="checkFieldName" name="checkFieldName" value="">
<input type="hidden" id="fromCRM" name="fromCRM" value="$!fromCRM"/>
<input type="hidden" id="fromPage" name="fromPage" value="$!fromPage">
<input type="hidden" id="LinkType" name="LinkType" value="$!LinkType">
<input type="hidden" id="retValUrl" value="">
#if("$!planType"!="")
<input type="hidden" id="planType" name="planType" value="$!planType">
<input type="hidden" id="strType" name="strType" value="$!strType">
<input type="hidden" id="strDate" name="strDate" value="$!strDate">
<input type="hidden" id="userId" name="userId" value="$!userId">
#end
#if("$!detailFlow"=="detailFlow" || ("$!existFlow"=="exist" ))
<input type="hidden" id="varWakeUp" name="varWakeUp" value="">
<input type="hidden" id="varNextNode" name="varNextNode" value="">
<input type="hidden" id="varCheckPersons" name="varCheckPersons" value="">
<input type="hidden" id="varAttitude" name="varAttitude" value="">
<input type="hidden" id="optionType" name="optionType" value="">
<input type="hidden" id="isMyFlow" name="isMyFlow" value="$isMyFlow">
#end
<input type="hidden" id="deliverTo" name="deliverTo" value="">
<input type="hidden" id="OAWorkFlow" name="OAWorkFlow" value="#if($!OAWorkFlow)true#{else}false#end">
<input type="hidden" id="designId" name="designId" value="$!designId">
#if($!OAWorkFlow)
<input type="hidden" id="designId" name="designId" value="$!designId">
<input type="hidden" id="currNodeId" name="currNodeId" value="$!currNodeId">
<input type="hidden" id="fromAdvice" name="fromAdvice" value="$!noback">
<input type="hidden" id="approveStatus" name="approveStatus" value="#if($!request.getParameter("approveStatus"))$!request.getParameter("approveStatus")#{else}transing#end"/>
#end
#if("$copy" == "copy")
<input type="hidden" id="parentCode" name="parentCode" value="$globals.classCodeSubstring("$!parentCode",0)">
#else
<input type="hidden" id="parentCode" name="parentCode" value="$globals.classCodeSubstring("$!parentCode",5)">
#end
#set($printCount=false)
##if($!tabList == "")
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">#if("$!moduleName"=="")$!mainTable.display.get("$globals.getLocale()")#else$!moduleName#end</div>
	<ul class="HeadingButton">	
	#if("$auditprint"=="0"&&"$print"=="true"&&$globals.getMOperationMap($request).print())
		#set($printCount=true)
		<li><button type="button" onClick='window.showModalDialog("/UserFunctionQueryAction.do?tableName=$tableName&reportNumber=$!reportNumber&BillID=$!values.get("id")&operation=$globals.getOP("OP_PRINT")&parentTableName=$!parentTableName&winCurIndex=$!winCurIndex",winObj,"dialogWidth=258px;dialogHeight=245px,scroll=no;")' class="b2">$text.get("common.lb.print")</button></li>
	#elseif(("$flowStatus"=="finish"||"$flowStatus"==""||$currNodeId==-1)&&"$print"=="true"&&$globals.getMOperationMap($request).print())
		#set($printCount=true)
		<li><button type="button" onClick='window.showModalDialog("/UserFunctionQueryAction.do?tableName=$tableName&reportNumber=$!reportNumber&BillID=$!values.get("id")&operation=$globals.getOP("OP_PRINT")&parentTableName=$!parentTableName&winCurIndex=$!winCurIndex",winObj,"dialogWidth=258px;dialogHeight=245px;")' class="b2">$text.get("common.lb.print")</button></li>
	#end
	#if(!$!isDetail || ($checkRight && $!isDetail))
	<li><button type="button" id="deliverToBut" name="deliverToBut" title="Ctrl+S" onClick="if(beforSubmit(document.form)) {form.deliverTo.value='true';window.save=true; document.form.submit();}" class="b2">审核</button></li>
	<li><button type="button" title="Ctrl+S" onClick="if(beforSubmit(document.form)) {form.button.value='';window.save=true;document.form.submit();}" class="b2">暂存</button></li>
	#end
	<li><button type="button" id="backList"  name="backList" onClick="closeWindows();" class="b2">$text.get("common.lb.close")</button></li>
	#if($!OAWorkFlow)
	<li><button type="button" name="" onClick="javascript:flowDepict('$!designId','$values.get('id')');">$text.get("common.lb.checkFlowChart")&nbsp;</button></li>
	#end
	</ul>
</div>
##end
#if($parentName.length()>0)			
	<div class="scroll_function_big"><span>$text.get("common.msg.currstation")：$!parentName</span>
	</div>
#end
	<div id="listRange_id">
<script type="text/javascript">
function flowDepict(applyType,keyId){ 
	window.open("/OAMyWorkFlow.do?keyId="+keyId+"&operation=$globals.getOP("OP_DETAIL")&applyType="+applyType,null,"height=570, width=1010,top=50,left=100 ");
}
var oDiv2=m("listRange_id");
var sHeight=document.documentElement.clientHeight-52;
oDiv2.style.height=sHeight+"px";
</script>
		<div class="listRange_1">

#set($layoutHTML=$!mainTable.layoutHTML)
#foreach ($row in $fieldInfos )
#if($row.fieldName == "id")
<input type="hidden" id="id" name="id" value="$!values.get("id")">
#elseif($row.fieldName == "f_brother")
<input type="hidden" id="f_brother" name="f_brother" value="$!values.get("f_brother")">
#elseif($row.inputType==3 || $row.inputType==100 || ($row.fieldType==3 || $row.fieldType==16) && "$!Safari"=="true")
	#if($layoutHTML.indexOf("@$row.fieldName@")!=-1)
	#set($layoutHTML = $globals.replaceString($layoutHTML,"@$row.fieldName@","style=display:none;"))
	#else
	#if($row.fieldType==1)
	<input type="hidden" name="$row.fieldName" value="$!globals.newFormatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)">
	#elseif(($row.fieldType==3 || $row.fieldType==16) && "$!Safari"=="true")
	<textarea  name="$row.fieldName" style="visibility:hidden;">$!values.get("$row.fieldName")</textarea>
	#else
	<input type="hidden" name="$row.fieldName" value="$!values.get("$row.fieldName")"/>
	#end
	#end
#else
	#if($globals.indexOf($layoutHTML,"$row.fieldName")==-1)
		<input type="hidden" name="$row.fieldName" value="$!defValue"/>
	#end
#end
#if($row.fieldType==1)
	#set($layoutHTML=$globals.replaceString($layoutHTML,"#$row.fieldName#","$!globals.newFormatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)"))
	#else
	#set($layoutHTML=$globals.replaceString($layoutHTML,"#$row.fieldName#","$!values.get($row.fieldName)"))
	#end
#end
#if("$!Safari"=="true")
$globals.replaceTextArea($!layoutHTML)
#else
$!layoutHTML
#end

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
		<table border="0" cellpadding="0"  cellspacing="0" class="listRange_list_function_b" name="$gridtable" id="$gridtable">
		</table>
		</div>
	</div>	
#end	
#end
#end

#if($!OAWorkFlow)
<div class="listRange_1_photoAndDoc_1">
<b>$text.get("common.sign.advice")</b>
<br><br>
#foreach($!deliver in $!delivers)
##if($!deliver.endTime.length()>0)
<li style="width:95%;">$text.get("oa.workFlow.step")：$!workFlowDesignBeans.get($!designId).getFlowNodeMap().get($!deliver.nodeId).display&nbsp;&nbsp;&nbsp;$text.get("common.sign.human")：$!deliver.empFullName&nbsp;&nbsp;&nbsp;$text.get("common.sign.time")：$!deliver.attTime</li>
<li style="width:95%;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$!deliver.deliverance</li>
<br><br>
##end
#end
##set($curCount=0)
##foreach($!deliver in $!delivers)
##if($!deliver.nodeId.equals("$!currNodeId")&&$!deliver.createBy.equals("$!LoginBean.id")&&$!deliver.endTime.length()==0)
##set($curCount=$curCount+1)
##<li style="width:95%;">$text.get("common.msg.SignAdvice")<textarea id="deliverance" name=deliverance rows="3">$!deliver.deliverance</textarea></li>
##end
##end

##if(!$isDetail&&$curCount==0)
##<li style="width:95%;">$text.get("common.msg.SignAdvice")<textarea id="deliverance" name=deliverance rows="3">$!deliver.deliverance</textarea></li>
##end
##if(!$isDetail&&$!workFlowDesignBeans.get($!designId).getFlowNodeMap().get("$!currNodeId").timeLimit==0&&!$!workFlowDesignBeans.get($!designId).getFlowNodeMap().get("$!currNodeId").forwardTime)
##<br><br>
##<li style="width:95%;">$text.get("alertCenter.lb.awoke")：<input type="text" id="oaTimeLimit" name="oaTimeLimit" size="10" value="$!oaTimeLimit">
##<select id="oaTimeLimitUnit" name="oaTimeLimitUnit">
##<option value="0" #if($!oaTimeLimitUnit==0)selected#end>$text.get("oa.calendar.day")</option>
##<option value="1" #if($!oaTimeLimitUnit==1)selected#end>$text.get("oa.work.hour")</option>
##<option value="2" #if($!oaTimeLimitUnit==2)selected#end>$text.get("common.msg.minute")</option>
##</select>&nbsp;&nbsp;&nbsp;$text.get("common.msg.AwokeDeliverTo")</li>
###end
<ul id="affixuploadul">
</ul>
</div>
<div class="listRange_1_photoAndDoc_1">
<b>$text.get("oa.flow.affix.down")</b>&nbsp;&nbsp;#if(!$!checkRight)<button type="button" class="b2" onClick="addAffix()">$text.get("com.flow.add.affix")</button>#end
<div id="files_preview" style="margin-left:1px; background: none;"></div>
<br><br>
#foreach($!deliver in $!affixs)
<li style="width:95%;margin-top: 10px;list-style:none;">
		$text.get("oa.add.affix.person")：$!deliver.empFullName&nbsp;&nbsp;&nbsp;$text.get("oa.flow.upload.time")：$!deliver.attTime</li>&nbsp;&nbsp;&nbsp;
		$text.get("oa.flow.affix.name")：
		#foreach($affix in $globals.strSplitByFlag($deliver.affix,';'))
		$affix <a href="/ReadFile?fileName=$globals.urlEncode($affix)&type=AFFIX&tableName=$tableName&tempFile=previewFile" target="_blank">预览</a>&nbsp;&nbsp;<a href="/ReadFile?fileName=$globals.urlEncode($affix)&realName=$globals.urlEncode($affix)&tempFile=false&type=AFFIX&tableName=$tableName&down=true" target="_blank">$text.get("common.lb.download")</a>
		#end
#end
<br><br>
<input type="hidden" id="attachFiles" name="attachFiles" value="">
<input type="hidden" id="delFiles" name="delFiles" value="">
</div>
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
	location.href='/UserFunctionAction.do?tableName=$!tableName&keyId=$!values.get("id")&moduleType=$!moduleType&f_brother=$!f_brother&operation=7&winCurIndex=$!winCurIndex&pageNo=$!pageNo&parentCode=$!parentCode&parentTableName=$!parentTableName&saveDraft=$!saveDraft&noback=$!noback';
}
</script>
#end
 </form>
</body>
<script type="text/javascript">
#if("$!Safari"!="true")
#foreach($row in $fieldInfos)
#if($row.fieldType==16 && $row.inputType!=3 && $row.inputType!=6)
var $row.fieldName  ;
KindEditor.ready(function(K) {
	$row.fieldName = K.create('textarea[name="$row.fieldName"]',{
		uploadJson:'/UtilServlet?operation=uploadFile'
		#if($row.inputType==8)
		,readonlyMode : true
		#end
	});
});
#end
#end
#end

#foreach ($row in $fieldInfos)
	#if("$row.inputType"=="3")
	var varField = jQuery("#$row.fieldName") ;
	varField.after("*********") ;
	varField.remove();
	#elseif("$row.inputType"=="2" || "$row.inputTypeOld"=="2")
	var html = '' ;
	var varField = jQuery("#$row.fieldName") ;
	#if($row.getSelectBean().relationKey.hidden)
	html += '<input id="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" name="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" type="hidden" value="$!values.get($row.fieldName)">' ;
	#else
		#if("$row.fieldType" == "1")
			html += '<input id="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" name="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" #if("$row.inputType" == "8") readonly="readonly" style="color: #C0C0C0;" #else style="width:100px;"  #end '
			     + 'type="text"  size="20" onKeyDown="if(event.keyCode==13&&this.value.length>0) event.keyCode=9" #if("$row.popupType"!="2") onDblClick="$popUrlValue" #end  #if("$row.popupType"!="1") onKeyPress="if(event.keyCode ==13){$popUrlValue}"#end value="#if($!isDetail)$!globals.formatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)#else$!globals.newFormatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)#end"><img onClick="$popUrlValue" src="/$globals.getStylePath()/images/St.gif"> ';
		#else
			html += '<input id="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" name="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" #if("$row.inputType" == "8") readonly="readonly" style="color: #C0C0C0;" #else style="width:100px;"  #end '
			 	 + 'type="text"  size="20" onKeyDown="if(event.keyCode==13&&this.value.length>0) event.keyCode=9" #if("$row.popupType"!="2") onDblClick="$popUrlValue" #end  #if("$row.popupType"!="1") onKeyPress="if(event.keyCode ==13){$popUrlValue}"#end value="$!values.get($row.fieldName)"><img onClick="$popUrlValue" src="/$globals.getStylePath()/images/St.gif">' ;					
		#end
	#end
	#if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
		#foreach($srow in $row.getSelectBean().viewFields)
			#set($mainInput=$globals.getMainInputValue("$mainTable.tableName","$row.fieldName"))    
			#set($popUrlValue="if(popMainInput(\'$mainInput\'))openSelect(\'/UserFunctionAction.do?keyId=$!values.get($row.fieldName)&tableName=$mainTable.tableName&fieldName=$row.fieldName&operation="+$globals.getOP("OP_POPUP_SELECT")+"\'#foreach($mainField in $row.getSelectBean().tableParams)+\'&$mainField=\'+n(\'$mainField\')[0].value#end,this,\'$row.fieldName\')")
			#set($ajaxUrlValue="\'/UtilServlet?operation=Ajax&tableName=$mainTable.tableName&fieldName=$row.fieldName\' #foreach($mainField in $row.getSelectBean().tableParams)+\'&$mainField=\'+n(\'$mainField\')[0].value#end")
			#set($openUrlValue="\'/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&tableName=$mainTable.tableName&fieldName=$row.fieldName\' #foreach($mainField in $row.getSelectBean().tableParams)+\'&$mainField=\'+n(\'$mainField\')[0].value#end")
			#if($globals.getFieldBean($tableName,$srow.display.substring($index)).fieldType==1||$globals.getFieldBean($srow.fieldName).fieldType==1)
				html += '<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)" #if("$row.inputType" == "8") readonly="readonly" style="color: #C0C0C0;" #else style="width:100px;"  #end '
			 		 + 'onKeyDown="if(event.keyCode==13&&this.value.length>0) {event.keyCode=9;if(popMainInput(\'$globals.getMainInputValue("$mainTable.tableName","$row.fieldName")\'))mainSelect($ajaxUrlValue,$openUrlValue,this,\'$row.fieldName\')}" type="text" #if("$row.popupType"!="2") #if("$row.inputType" != "8")onDblClick="$popUrlValue" #end #end  size="20" #if("$row.popupType"!="1") onKeyPress="if(event.keyCode ==13){ $popUrlValue}" #end  value="$!globals.newFormatNumber($!values.get($srow.asName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)">#if("$row.inputType" != "8")<img onClick="$popUrlValue" src="/$globals.getStylePath()/images/St.gif">#end ';
			#else
				html += '<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)" #if("$row.inputType" == "8") readonly="readonly" style="color: #C0C0C0;" #else style="width:100px;" #end '
			         + 'onKeyDown="if(event.keyCode==13&&this.value.length>0) {event.keyCode=9;if(popMainInput(\'$globals.getMainInputValue("$mainTable.tableName","$row.fieldName")\'))mainSelect($ajaxUrlValue,$openUrlValue,this,\'$row.fieldName\')}" type="text" #if("$row.popupType"!="2") #if("$row.inputType" != "8")onDblClick="$popUrlValue" #end #end  size="20" #if("$row.popupType"!="1") onKeyPress="if(event.keyCode ==13){ $popUrlValue}" #end  value="$!values.get($srow.asName)">#if("$row.inputType" != "8")<img onClick="$popUrlValue" src="/$globals.getStylePath()/images/St.gif">#end ' ;
			#end
		#end	
	#end
	varField.after(html) ;
	varField.remove();
	#elseif("$row.fieldType" == "5")
	var varField = jQuery("#$row.fieldName") ;
	varField.click(function(){
		WdatePicker({lang:'$globals.getLocale()'});
	});
	#elseif("$row.fieldType" == "6")
	var varField = jQuery("#$row.fieldName") ;
	varField.click(function(){
		WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'});
	});
	#elseif("$row.fieldType" == "16")
	var varField = jQuery("#$row.fieldName") ;
	varField.css("visibility","hidden") ;
	#elseif("$row.fieldType"=="13")
	var varField = jQuery("#$row.fieldName") ;
	var html = '<button type="button"  id="picbuttonthe222" name="picbuttonthe222" onClick="upload(\'PIC\',\'$row.fieldName\');" class="b4">$text.get("upload.lb.picupload")</button>' ;
			 + '<ul id="picuploadul_${row.fieldName}">'
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
	var html = '<button type="button" id="affixbuttonthe_${$row.fieldName}" name="affixbuttonthe_${$row.fieldName}" onClick="upload(\'AFFIX\',\'$row.fieldName\');" class="b4">$text.get("upload.lb.affixupload")</button>' ;
	html += '<ul id="affixuploadul_${row.fieldName}">' ;
	#foreach($uprow in $!values.get($row.fieldName).split(";"))
		#if("$!uprow"!="")
		#if($uprow.indexOf(":") > -1)			
		html += '<li style="background:url();" id="uploadfile_$!globals.get($uprow.split(":"),0)"><input type=hidden name=$row.fieldName value="$uprow">$!globals.get($uprow.split(":"),1)' 			
			 + '<a href=\'javascript:deleteupload("$!globals.get($uprow.split(":"),0)","false","$tableName","AFFIX");\'>$text.get("common.lb.del")</a>' 
			 + '&nbsp;&nbsp;&nbsp;&nbsp;<a href="/ReadFile?fileName=$globals.urlEncode($!globals.get($uprow.split(":"),0))&realName=$globals.urlEncode($!globals.get($uprow.split(":"),1))&tempFile=false&type=AFFIX&tableName=$tableName&down=true" target="_blank">$text.get("common.lb.download")</a>' ;
			 + '</li> ' ;
		#else
		html += '<li style="background:url();" id="uploadfile_$uprow">'
			 + '<input type=hidden name=$row.fieldName value="$uprow">$uprow'			
			 + '<a href=\'javascript:deleteupload("$uprow","false","$tableName","AFFIX");\'>$text.get("common.lb.del")</a>'
			 + '&nbsp;&nbsp;&nbsp;&nbsp;<a href="/ReadFile?fileName=$globals.urlEncode($uprow)&type=AFFIX&tableName=$tableName&tempFile=previewFile" target="_blank">预览</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="/ReadFile?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=AFFIX&tableName=$tableName&down=true" target="_blank">$text.get("common.lb.download")</a>'
			 + '</li>' ;
		#end
		#end
	#end
	html += '</ul>' ;
	varField.after(html) ;
	varField.remove();
	#end
	
	#if("$row.inputType"=="8")
	var varField = jQuery("#$row.fieldName") ;
	varField.attr("readOnly",true) ;
	#end
#end
</script>
</html>
