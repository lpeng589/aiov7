<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.common.communicationManger")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<style>
.crmdetail{
	float:left;
	height:auto;
	width:70%;
	margin-top:3px;
	margin-left:3px;
	text-align:left;
	background: #FFFFFF url(/$globals.getStylePath()/images/aiobg.gif) repeat-x left -221px;
	border:1px solid #A2CEF5;
}
.crmdetail li{
	width:30%;
	float:left;
	padding-left:1px;
	height:22px;
}
.crmdetail li span{
	float:left;
	width:90px;
	color:blue;
	text-align:right;
}
.menuReport2 {
	float:right;
	width:20%;
	margin-top:5px;
	margin-right:10px;
	border:1px solid #A2CEF5;
	overflow-y:auto;
	background:#FBFDFF;
}
.menuReport2 h1 {
	font-size:12px;
	margin:0px;
	padding:0px;
	margin-top:10px;
	margin-bottom:5px;
	text-indent:20px;
	height:23px;
	width:100%;
	line-height:23px;
	overflow:hidden;
}
.menuReport2 ul {
	margin:0px;
	padding:0px;
}
.menuReport2 ul li {
	float:left;
	width:220px;
	height:25px;
	padding-left:15px;
	line-height:25px;
	list-style-type:none;
	background:url(/$globals.getStylePath()/images/allbg.gif) no-repeat -187px 11px;
}
</style>
<script language="javascript">
function showreModule(title,href){		
	top.showreModule(title,href);	
}
function showDivCustomSetTable(){
	var returnValue = window.showModalDialog("/vm/crm/client/detailConfig.jsp","","dialogWidth=670px;dialogHeight=450px") ;
	if(typeof(returnValue)!="undefined"){
		var strType = returnValue.split("@@") ;
		if(strType[0]=="defaultConfig"){
			colConfigForm.action = "/ColConfigAction.do" ;
		}
		colConfigForm.colSelect.value=strType[1] ;
		colConfigForm.submit() ;
	}
}
</script>
</head>
<body>
<div id="oalistRange">
<script type="text/javascript">
var oDiv=document.getElementById("oalistRange");
var sHeight=document.body.clientHeight-15;
oDiv.style.height=sHeight+"px";
</script>
<div class="crmdetail" style="float: left;padding-top:10px;">
#if($LoginBean.id =="1")
<div style="float: right;">
<button type="button" id="CustomSetLink" onClick="showDivCustomSetTable();" class="b3">$text.get("com.define.colconfig")</button>
</div>
#end
#foreach ($row in $fieldInfos )
#if($globals.getScopeRight($mainTable.tableName,$row.fieldName,$scopeRight))
#if("$row.fieldName"=="id")
<input type="hidden" name="id" value="$!values.get($row.fieldName)"/>
#elseif("$row.inputType" != "100" && "$row.fieldType" != "3"  && "$row.fieldType" != "16"  && "$row.fieldType" != "13" && "$row.fieldType" != "14" && $row.fieldName != "createBy" && $row.fieldName != "createTime" && $row.fieldName != "lastUpdateBy" && $row.fieldName != "lastUpdateTime")
#if("$row.inputType" == "0")
	#if("$row.fieldType" == "17")
	<li><span>$row.display.get("$globals.getLocale()")：</span>$!values.get($row.fieldName)</li>
	#elseif("$row.fieldType" == "5" )
	<li><span>$row.display.get("$globals.getLocale()")：</span>$!values.get($row.fieldName)</li>	
	#elseif("$row.fieldType" == "6")
	<li><span>$row.display.get("$globals.getLocale()")：</span>$!values.get($row.fieldName)</li>
	#elseif("$row.fieldType" == "18" && ("$row.fieldName"=="ClientName" || "$row.fieldName"=="EmergentWhy"))
	<li style="width: 80%"><span>$row.display.get("$globals.getLocale()")：</span>$!values.get($row.fieldName)</li>	
	#elseif("$row.fieldType" == "18")
	#elseif("$row.fieldType" == "19")
	<li><span>$row.display.get("$globals.getLocale()")：</span>$!values.get($row.fieldName)</li>
	#elseif("$row.fieldType" == "1")
	<li><span>$row.display.get("$globals.getLocale()")：</span>$!globals.formatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)</li>	
	#elseif("$row.fieldType" == "20")
	<li><span>$row.display.get("$globals.getLocale()")：</span>$!values.get($row.fieldName)</li>
	#else
	<li><span>$row.display.get("$globals.getLocale()")：</span>$!values.get($row.fieldName)</li>
	#end	
	#set($totalFields=$totalFields+1)		
#elseif("$row.inputType" == "8")			
		<li><span>$row.display.get("$globals.getLocale()")：</span>
		#if("$row.fieldType" == "1")
		$!globals.formatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)</li>
		#else
		$!values.get($row.fieldName)</li>					
		#end
		#set($totalFields=$totalFields+1)
#elseif("$row.inputType" == "1")
	<li><span>$row.display.get("$globals.getLocale()")：</span>
	$!globals.getEnumerationItemsDisplay("$row.refEnumerationName","$values.get($row.fieldName)","$globals.getLocale()")
	</li>
#elseif("$row.inputType" == "7")
	<li><span>$row.display.get("$globals.getLocale()")：</span>$!values.get($row.fieldName)</li>
#elseif("$row.inputType" == "4")
#set($lstr = $!values.get($row.fieldName))
#if("$lstr" != "")
    #set($lan= $!values.get("LANGUAGEQUERY").get($lstr).get("$globals.getLocale()"))
#end 	
<li><span>$row.display.get("$globals.getLocale()")：</span>$!lan</li>	
#elseif("$row.inputType" == "2")
#if($row.getSelectBean().relationKey.hidden)
#elseif("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1")
	<li><span>$row.display.get("$globals.getLocale()")：</span>$globals.getSeqDis($!values.get($row.fieldName))
#else
#set($mainInput=$globals.getMainInputValue("$mainTable.tableName","$row.fieldName"))    
#set($popUrlValue="if(popMainInput('$mainInput'))openSelect('/UserFunctionAction.do?keyId=$!values.get($row.fieldName)&tableName=$mainTable.tableName&fieldName=$row.fieldName&operation="+$globals.getOP("OP_POPUP_SELECT")+"'#foreach($mainField in $row.getSelectBean().tableParams)+'&$mainField='+n('$mainField')[0].value#end,this,'$row.fieldName')")
<li  #if("District"=="$row.fieldName")style="width=95%"#end>
	<span>$row.display.get("$globals.getLocale()")：</span>
	#if("$row.fieldType" == "1")
		$!globals.formatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)
	#else
		$!values.get($row.fieldName)						
	#end
</li>
#end
#if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
#foreach($srow in $row.getSelectBean().viewFields)	
#set($colName="")
   #if($!srow.display!="")
	#set($index=$srow.display.indexOf(".")+1)
	#set($tableField=$tableName+"."+$srow.display.substring($index))
   #else
   	#set($tableField="")
   #end
#set($dis = $globals.getFieldDisplay($tableName,$row.getSelectBean().name,$tableField))
#if($dis == "") #set($dis = $globals.getFieldDisplay($srow.fieldName)) #end
#if($allConfigList.size()>0)
#if($globals.colIsExistConfigList("$mainTable.tableName","$colName","bill") || $!srow.display!="")
#if("$row.fieldType" == "18")
	<li style="width:95%;">
		<span>$dis：</span>
		 $!values.get($srow.asName)
	</li>
#else
	<li>
		<span>$dis：</span>
		#if($globals.getFieldBean($tableName,$srow.display.substring($index)).fieldType==1)
		$!globals.formatNumber($!values.get($srow.asName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)
		#else
		$!values.get($srow.asName)										
		#end
	</li>
#end		
#end
#else
 #if("$srow.hiddenInput"!="true")
 	#if("$row.fieldType" == "18")
		<li style="width:95%;">
			<span>$dis：</span>$!values.get($srow.asName)
		</li>
	#else
		<li>
			<span>$dis：</span>$!values.get($srow.asName)
		</li>
	#end		
 #end
#end				
#end				
#end	
#end
#end
#end
#end
</div>
<div class="menuReport2"  align="left">
	<h1><span>$text.get("common.lb.spotNews")</span> <!--span style="padding-left: 10px;">历史记录</span--></h1>
	<ul id="conter" style="overflow-y:auto;">
		#foreach($log in $logList)
			#if("$!globals.get($log,0)"=="history")
			<li><a href="javascript:void(0);" title="$globals.get($log,1)">$globals.subTitle($globals.get($log,1),30)</a></li>
			#else
			<li><a href="javascript:showreModule('$text.get("common.lb.spotClient")','/UserFunctionAction.do?parentTableName=CRMClientInfo&tableName=$globals.get($log,0)&src=menu&keyId=$globals.get($log,2)&f_brother=$!values.get('id')&operation=5&noback=true')" title="$globals.get($log,1)">$globals.subTitle($globals.get($log,1),30)</a></li>
			#end
		#end
	</ul>
	#if("$globals.getSysSetting('showDealing')"=="true" && $dealList.size()>0)
	<h1>$text.get("common.lb.dealRecord")</h1>
		<ul id="conter" style="overflow-y:auto;">
		#foreach($deal in $dealList)
			<li><a href="javascript:showreModule('$text.get("common.lb.dealRecord")','/UserFunctionAction.do?parentTableName=CRMClientInfo&tableName=$globals.get($deal,0)&src=menu&keyId=$globals.get($deal,2)&f_brother=$!values.get('id')&operation=5&noback=true')" title="$globals.get($deal,1)">$globals.subTitle($globals.get($deal,1),30)</a></li>
		#end
	</ul>
	#end
</div>
#if($childList.size()>0)
<div>
<table width="380" cellpadding="0" cellspacing="0" class="listRange_list_function" style="margin-left: 2px;margin-top: 10px;">
	<thead>
	<tr>
		#foreach($field in $globals.queryConfig("detailCRMClientInfoDet"))
		#set($dbField=$globals.getFieldBean("CRMClientInfoDet",$field.colName))
		<td width="$dbField.width" class="oabbs_tl">$globals.getFieldDisplay("CRMClientInfoDet.$field.colName")</td>
		#end
	</tr>
	</thead>
	<tbody>
	#foreach($childMap in $childList)
	<tr>
		#foreach($field in $globals.queryConfig("detailCRMClientInfoDet"))
			#set($dbField=$globals.getFieldBean("CRMClientInfoDet",$field.colName))
			#if($dbField.inputType==1 || $dbField.inputTypeOld==1)
			#set ($enumItem=$!globals.getEnumerationItemsDisplay("$dbField.refEnumerationName","$childMap.get($field.colName)","$globals.getLocale()"))
				<td class="oabbs_tc" title="$!enumItem">$!enumItem&nbsp;</td>
			#else
				<td>$!childMap.get("$!field.colName")&nbsp;</td>
			#end
		#end
	</tr>
	#end
	</tbody>
</table>
</div>
#end
<div  class="crmdetail" style="float: left;padding-top:10px;margin-top: 10px;">
#foreach ($row in $fieldInfos )
#if($globals.getScopeRight($mainTable.tableName,$row.fieldName,$scopeRight))	
#if("$row.fieldType" == "18" && "$row.fieldName"!="ClientName" && "$row.fieldName"!="EmergentWhy")
<li style="width: 80%"><span>$row.display.get("$globals.getLocale()")：</span>$!values.get($row.fieldName)</li>	
#elseif("$row.inputType" != "100" && "$row.inputType" !="3" && "$row.fieldType" == "3" && "$row.inputType"!="6")
<li style="width: 100%"><span>$row.display.get("$globals.getLocale()")：</span>
<div style="margin-left: 110px;">
$!values.get($row.fieldName)
</div>
</li>
#elseif("$row.fieldType"=="16")
<li style="width: 97%"><span>$row.display.get("$globals.getLocale()")：</span>
<div style="margin-left: 50px;">
$!values.get($row.fieldName)
</div>
</li>
#end
#end
#end
</div>
</div>
<form method="post" name="colConfigForm" action="/ColConfigAction.do?operation=1" target="_self">
<input type="hidden" name="parentCode" value="$!parentCode">
<input type="hidden" name="parentTableName" value="$!parentTableName">
<input type="hidden" name="allTableName" value="detailCRMClientInfo,detailCRMClientInfoDet,"/>
<input type="hidden" name="strAction" value="/CrmTabAction.do?tableName=CRMClientInfo&tabType=detail&keyId=$!values.get('id')" />
<input type="hidden" name="colType" value="bill" />
<input type="hidden" id="colSelect" name="colSelect" value="" />
 </form>
</body>
</html>

