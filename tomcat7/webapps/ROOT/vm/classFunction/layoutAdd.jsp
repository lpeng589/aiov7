<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html> 
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$mainTable.display.get("$globals.getLocale()")</title>
<link rel="stylesheet" href="/style/css/layout.css" type="text/css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script language="javascript" src="$globals.js("/js/editPage.vjs","",$text)"></script>
<script language="javascript" src="/js/editGrid.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/setTime.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script language="javascript" src="$globals.js("/js/define.vjs",$mainTable.tableName,$text)"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<script language="javascript">
var ids=-1;
var fieldS;
var returnValue;
var existFlow;
#set($flowNode = $globals.getBillCheckIsFreeSeting("$mainTable.tableName").get(0))
var flowNodeid ="$!flowNode.id"; 
var flowNodedisplay ="$!flowNode.display";
var detailFlow = "$!detailFlow";
existFlow = "$!existFlow";
var operation = $globals.getOP("OP_ADD");
var winCurIndex = "$!winCurIndex";
var printRight = $globals.getMOperationMap($request).print() ;
var noback = "$!noback" ;
var fromCRM = "$!fromCRM" ;
var moduleType = "$!moduleType" ;
//这里用于URL明细表的传值。原来的getUrlBillDef对于明细表不再适用，因为，明细JS被静态化。这里为了区分URL值前加DF 例子MailCRM.jsp
$globals.changeDefaultValue()
$globals.changeDetInputType("$!tableName","0","$!MOID")


$globals.changeDefaultValue()
$globals.changeDetInputType("$!tableName","0","$!MOID")
#foreach ($row in $fieldInfos )
#if("$row.inputType" != "100" && $row.fieldName != "id"  && $row.fieldName != "createBy" && $row.fieldName != "createTime" && $row.fieldName != "lastUpdateBy" && $row.fieldName != "lastUpdateTime")
#if("$row.inputType" == "0"||"$row.inputType" == "7" || "$row.inputType" == "4")		
#if("$row.inputType" == "4"  || "$row.fieldType"=="16" )
putValidateItem("$row.fieldName",'$!row.display.get("$globals.getLocale()")',"$row.getStringType()","$!row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),false);
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
			window.location.href = retValUrl ;
		}else if(action == 'cancel'){
			window.location.href = retValUrl ;
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

<body onLoad=" showStatus();  #foreach ($rowlist in $childTableList ) #if("$globals.getSysSetting($rowlist.tableSysType)"=="true" && ($childTableConfigList.get($rowlist.tableName).size()>0 || $allConfigList.size()==0)) #set($gridtable =$rowlist.tableName+"Table")  #set($griddata =$rowlist.tableName+"Data") showtable('$gridtable');  initTableList(${gridtable}DIV,$gridtable,$griddata,$rowlist.defRowCount);#end #end initCalculate();initMainCaculate();#if( "$!globals.getVersion()"=="3"  || "$!globals.getVersion()"=="12" ||"$!globals.getVersion()"=="13" || "$!globals.getVersion()"=="11")initCountCal();#end">
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post"  onKeyDown="down()" scope="request" id="form" name="form"  action="/UserFunctionAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" id="conver" name="conver">
<input type="hidden" id="operation" name="operation" value="$globals.getOP("OP_ADD")">
<input type="hidden" id="org.apache.struts.taglib.html.TOKEN" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" id="tableName" name="tableName" value="$!tableName">
<input type="hidden" id="parentCode" name="parentCode" value="$!parentCode">
<input type="hidden" id="button" name="button" >
<input type="hidden" id="noback" name="noback" value="$!noback">
<input type="hidden" id="fresh" name="fresh" value="$!fresh">
<input type="hidden" id="detLineNo" name="detLineNo" value="">
<input type="hidden" id="logicValidate" name="logicValidate" value="">
<input type="hidden" id="moduleType" name="moduleType" value="$!moduleType">
<input type="hidden" id="defineInfo" name="defineInfo" value="">
<input type="hidden" id="parentTableName" name="parentTableName" value="$!parentTableName">
<input type="hidden" id="winCurIndex" name="winCurIndex" value="$!winCurIndex">
<input type="hidden" id="defineName" name="defineName" value="">
<input type="hidden" id="checkFieldName" name="checkFieldName" value="">
<input type="hidden" id="isTab" name="isTab" value="$!isTab">
<input type="hidden" id="deliverTo" name="deliverTo" value="">
<input type="hidden" id="f_brother" name="f_brother" value="$!f_brother">
<input type="hidden" id="retValUrl" value="">
#if("$!planType"!="")
<input type="hidden" id="planType" name="planType" value="$!planType">
<input type="hidden" id="strType" name="strType" value="$!strType">
<input type="hidden" id="strDate" name="strDate" value="$!strDate">
<input type="hidden" id="userId" name="userId" value="$!userId">
#end
#if($existFlow=="exist")
<input type="hidden" id="varWakeUp" name="varWakeUp" value="">
<input type="hidden" id="varNextNode" name="varNextNode" value="">
<input type="hidden" id="varCheckPersons" name="varCheckPersons" value="">
<input type="hidden" id="varAttitude" name="varAttitude" value="">
#end
#if($existSMS)
<input type="hidden" id="smsType" name="smsType" value="">
<input type="hidden" id="wakeUpMode" name="wakeUpMode" value="">
<input type="hidden" id="wakeUpContent" name="wakeUpContent" value="">
<input type="hidden" id="popedomDeptIds" name="popedomDeptIds" value="">
<input type="hidden" id="popedomUserIds" name="popedomUserIds" value="">
<input type="hidden" id="popedomCRMCompany" name="popedomCRMCompany" value="">
<input type="hidden" id="popedomCompanyCodes" name="popedomCompanyCodes" value="">
#end
<input type="hidden" id="OAWorkFlow" name="OAWorkFlow" value="#if($!OAWorkFlow)true#{else}false#end">
#if($!OAWorkFlow)
<input type="hidden" id="designId" name="designId" value="$!designId"/>
#end
<input type="hidden" id="OAWorkFlow" name="OAWorkFlow" value="#if($!OAWorkFlow)true#{else}false#end"/>
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
	<div id="listRange_id">
<script type="text/javascript">
function flowDepict(applyType,keyId){ 
	window.open("/OAMyWorkFlow.do?keyId="+keyId+"&operation=$globals.getOP("OP_DETAIL")&applyType="+applyType,null,"height=570, width=1010,top=50,left=100 ");
}
var oDiv2=m("listRange_id");
var sHeight=document.documentElement.clientHeight-32;
oDiv2.style.height=sHeight+"px";
</script>
#if($parentName.length()>0)			
	<div class="scroll_function_big"><span>$text.get("common.msg.currstation")：$!parentName</span>
		
	</div>
#end
<div class="listRange_1">



#set($layoutHTML=$!mainTable.layoutHTML)
#foreach ($row in $fieldInfos )
#set($defValue="")
#if($globals.getUrlBillDef($row.fieldName).length()>0)#set($defValue=$globals.getUrlBillDef($row.fieldName))
#elseif($globals.getUrlBillDef($row.fieldName).length()==0)
	#if($!parentCode.length()>0&&$row.getFieldIdentityStr().equals("copySuper"))#set($defValue=$copySupValue.get($row.getFieldName()))
	#elseif($!row.getFieldIdentityStr()=="BillNo")
		#if($!row.getDefaultValue()=="")
			#set($str=$row.getFieldName())
			#set($str="_"+$str)
			#set($str=$!tableName+$str)
			#set($defValue=$globals.getBillNoCode($str))
		#else
			#set($defValue=$globals.getBillNoCode($!row.getDefaultValue()))
		#end
	#elseif($row.getFieldIdentityStr().equals("lastValueAdd"))#set($defValue=$lastValues.get($row.getFieldName()))
	#elseif($row.getFieldIdentityStr().equals("paramDefaultValue"))#set($defValue=$paramValues.get($row.getFieldName()))
	#else#set($defValue=$row.getDefValue())
	#end
#end
#if($row.inputType==3)
	#if($layoutHTML.indexOf("@$row.fieldName@")!=-1)
	#set($layoutHTML = $globals.replaceString($layoutHTML,"@$row.fieldName@","style=display:none;"))
	#else
	<input type="hidden" name="$row.fieldName" value="$!defValue"/>
	#end
#else
	#if($row.inputType!=100 && $row.fieldName!="id" && $row.fieldName!="f_brother"
				&& $globals.indexOf($layoutHTML,"$row.fieldName")==-1)
		<input type="hidden" name="$row.fieldName" value="$!defValue"/>
	#end
#end

#if($row.inputType==2 || $row.inputTypeOld==2)
#set($layoutHTML=$globals.replaceString($layoutHTML,"#$row.fieldName#",""))
#else
#set($layoutHTML=$globals.replaceString($layoutHTML,"#$row.fieldName#","$!defValue"))
#end

#end

$globals.replaceString($!layoutHTML,"value= ","  ")

#foreach ($rowlist in $childTableList )
<input type="hidden" id="${rowlist.tableName}PopupType" name="${rowlist.tableName}PopupType" value="$globals.getTableInfoBean(${rowlist.tableName}).popupType"/> 
#end
#foreach ($rowlist in $childTableList ) 
#if("$globals.getSysSetting($rowlist.tableSysType)"=="true")#set($gridtable =$rowlist.tableName+"Table")
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
</div>
</form>
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
#foreach ($row in $fieldInfos )
	#if("$row.inputType"=="3")
	var varField = jQuery("#$row.fieldName") ;
	varField.after("*********") ;
	varField.remove();
	#elseif("$row.inputType"=="2")
	#set($ajaxStr="")
	#if($row.insertTable.length()>0)
	#set($ajaxStr="+")
	#foreach($srow in $row.getSelectBean().viewFields)
		#set($ajaxStr=$ajaxStr+"\'&"+$globals.getTableField($srow.asName)+"=\'+form."+$globals.getTableField($srow.asName)+".value")
	#end
	#end
	var html = '' ;
	var varField = jQuery("#$row.fieldName") ;
	#if($row.getSelectBean().relationKey.hidden)
		html += '<input id="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" name="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" type="hidden" value="#if($!parentCode.length()>0&&$row.getFieldIdentityStr().equals("copySuper"))$!defValue#else$!values.get($row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName))#end">' ;
	#else
		#set($mainInput=$globals.getMainInputValue("$mainTable.tableName","$row.fieldName"))    
		#set($popUrlValue="if(popMainInput(\'$mainInput\'))openSelect(\'/UserFunctionAction.do?tableName=$mainTable.tableName&fieldName=$row.fieldName&operation="+$globals.getOP("OP_POPUP_SELECT")+"\'#foreach($mainField in $row.getSelectBean().tableParams)+'&$mainField='+n(\'$mainField\')[0].value#end,this,\'$row.fieldName\')")
		html += '<input id="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)"  name="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" style="width:100px;" type="text"  size="20" value="$!values.get($row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName))" onKeyDown="if(event.keyCode==13&&this.value.length>0) event.keyCode=9" #if("$!row.popupType"!="2") onDblClick="$popUrlValue" #end #if("$!row.popupType"!="1") onKeyPress="if(event.keyCode ==13){ $popUrlValue}" #end><img src="/$globals.getStylePath()/images/St.gif" onClick="$popUrlValue">' ;
	#end
	#if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
		#foreach($srow in $row.getSelectBean().viewFields)
			#set($colName="")#set($totalFields=$totalFields+1)
			#if($!srow.display!="")
				#if($!srow.display.indexOf("@TABLENAME")==0)
					#set($index=$srow.display.indexOf(".")+1)#set($tableField=$tableName+"."+$srow.display.substring($index))
				#else
					#set($tableField=$srow.display)
				#end
				#set($colName="$srow.display")
			#else
				#set($tableField="$srow.fieldName")
				#set($colName="$srow.fieldName")
			#end
			#set($dis = $globals.getFieldDisplay($tableField))	
			#if($dis == "")#set($dis = $globals.getFieldDisplay($rowlist.tableName,$row.getSelectBean().name,$srow.fieldName))#end
			#set($mainInput=$globals.getMainInputValue("$mainTable.tableName","$row.fieldName"))
			#set($popUrlValue="if(popMainInput(\'$mainInput\'))openSelect(\'/UserFunctionAction.do?tableName=$mainTable.tableName&fieldName=$row.fieldName&operation="+$globals.getOP("OP_POPUP_SELECT")+"\'#foreach($mainField in $row.getSelectBean().tableParams)+\'&$mainField=\'+n(\'$mainField\')[0].value#end,this,\'$row.fieldName\')")
			#set($ajaxUrlValue="\'/UtilServlet?operation=Ajax&tableName=$mainTable.tableName&fieldName=$row.fieldName\' #foreach($mainField in $row.getSelectBean().tableParams)+\'&$mainField=\'+n(\'$mainField\')[0].value#end"+$ajaxStr)
			#set($openUrlValue="\'/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&tableName=$mainTable.tableName&fieldName=$row.fieldName\' #foreach($mainField in $row.getSelectBean().tableParams)+\'&$mainField=\'+n(\'$mainField\')[0].value#end")
			html += '<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)" onKeyDown="if(event.keyCode==13&&this.value.length>0) {event.keyCode=9;if(popMainInput(\'$globals.getMainInputValue("$mainTable.tableName","$row.fieldName")\'))mainSelect($ajaxUrlValue,$openUrlValue,this,\'$row.fieldName\')}" type="text" #if("$!row.popupType"!="2") onDblClick="$popUrlValue" #end value="$!values.get($srow.asName)"  size="20" #if("$!row.popupType"!="1") onKeyPress="if(event.keyCode ==13){$popUrlValue}" #end onBlur="isCheckField(this,\'$row.fieldName\');" style="width:100px;" ><img src="/$globals.getStylePath()/images/St.gif" onClick="$popUrlValue"><input type="hidden" id="tbl_$globals.getTableField($srow.asName)" value="$!values.get($srow.asName)"/>' ;
		#end
	#end
	varField.after(html) ;
	varField.remove();
	#elseif("$row.fieldType" == "5")
	var varField = jQuery("#$row.fieldName") ;
	varField.click(function(){
		WdatePicker({lang:'$globals.getLocale()'});
	}) ;
	#elseif("$row.fieldType" == "6")
	var varField = jQuery("#$row.fieldName") ;
	varField.click(function(){
		WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'});
	}) ;
	#elseif("$row.fieldType" == "16")
	//var varField = jQuery("#$row.fieldName") ;
	//varField.after('<textarea id="FCKeditor1" name="$row.fieldName" style="display:none"></textarea><input type="hidden" id="FCKeditor1___Config" value="" style="display:none" /><iframe id="FCKeditor1___Frame" src="/fckeditor/editor/fckeditor.html?InstanceName=FCKeditor1&amp;Toolbar=Small" width="100%" height="200" frameborder="0" scrolling="no"></iframe>');
	//varField.remove();
	#elseif("$row.fieldType"=="13")
	var varField = jQuery("#$row.fieldName") ;
	varField.after('<button type="button" id="picbuttonthe${row.fieldName}" name="picbuttonthe${row.fieldName}" onClick="upload(\'PIC\',\'$row.fieldName\');" class="b4">$text.get("upload.lb.picupload")</button><ul id="picuploadul_${row.fieldName}"></ul>') ;
	varField.remove();
	#elseif("$row.fieldType"=="14")
	var varField = jQuery("#$row.fieldName") ;
	varField.after('<button type="button" id="affixbuttonthe${row.fieldName}" name="affixbuttonthe${row.fieldName}" onClick="upload(\'AFFIX\',\'$row.fieldName\');" class="b4">$text.get("upload.lb.affixupload")</button><ul id="affixuploadul_${row.fieldName}"></ul>') ;
	varField.remove();
	#end
	#if("$row.inputType"=="8")
	var varField = jQuery("#$row.fieldName") ;
	varField.attr("readOnly",true) ;
	#end
#end
</script>
</body>
</html>