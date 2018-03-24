<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>#if("$!moduleName"=="")$!mainTable.display.get("$globals.getLocale()")#else$!moduleName#end $text.get("common.lb.detail")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<link type="text/css" rel="stylesheet" href="/js/ui/jquery-ui-1.8.18.custom.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/dragcols.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<!-- 不能用editGrid2 zxy===== -->
<script type="text/javascript" src="/js/editGrid.js"></script>
<script type="text/javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/define.vjs",$mainTable.tableName,$text)"></script>
<script language="javascript">
#if($mainTable.wakeUp=="1")
function alertSet(relationId){	
	var urls=encodeURIComponent('/UserFunctionAction.do?tableName=CRMSaleFollowUp&keyId='+relationId+'&operation=5&winCurIndex=16&pageNo=&parentCode=&parentTableName=$!parentTableName&saveDraft=');
	var	typestr=encodeURIComponent('$text.get("common.CRMSaleFollowUp.alert")');
	var	title=encodeURIComponent('$text.get("crm.client.distributionFUV")');
	var date=new Date();
	var frame=window.parent.frames['moddiFrame'];
	if(typeof(frame)!="undefined"){
		window.parent.frames['moddiFrame'].setFrameHight('div_max');
	}
	var url = "/UtilServlet?operation=alertDetail&relationId="+relationId+"&falg=true"+"&title="+title+"&typestr="+typestr+"&urls="+urls+"&date="+date;
	asyncbox.open({
		id : 'warmsetting',
　　　   	url : url,
	 	title : '$text.get("workflow.msg.warmsetting")',
　　 　	width : 560,
　　 　	height : 460,
	    callback : function(action,opener){
		    if(action == 'ok'){ 
		    	opener.checkAlertSet();
			}
　　      }
 　 });
}
//弹出框返回函数处理






function dealAsyn(){
	jQuery.close('warmsetting');
}
function refreshIframe(){
	jQuery.close('warmsetting');
}
#end

var valueId = "$!values.get("id")";
var upIniFlag=1; 
var fromFoward = "$!fromFoward";
var ids=-1;
var fieldS;
var returnValue;
var operation = $globals.getOP("OP_UPDATE");
var noback="$!noback";
var detail = "$!detail"
var flowNodeid ="$!flowNode.id"; 
var flowNodedisplay ="$!flowNode.display";
var detailFlow = "$!detailFlow";
var existFlow = "$!existFlow";
var winCurIndex = "$!winCurIndex";
var f_brother = "$!values.get("f_brother")";
var operation = $globals.getOP("OP_UPDATE");
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

#if("draft"!="$!values.get('workFlowNodeName')")
#if("$!designId"=="")
$globals.changeDetInputType("$!tableName","$!userLastNode","$!MOID")
#else
$globals.changeDetInputType("$!tableName","$!userLastNode","$!designId")
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
	location.href="/UserFunctionAction.do?tableName=$!tableName&designId=$!designId&keyId=$!values.get("id")"+
	"&f_brother=$!f_brother&parentCode=$!parentCode&operation=$globals.getOP("OP_UPDATE_PREPARE")&noback=$!noback&winCurIndex=$!winCurIndex&fromPage=detail&pageNo=$!pageNo&parentCode="+
	document.getElementById("parentCode").value+"&moduleType=$!moduleType&parentTableName=$!parentTableName&saveDraft=#if("draft"=="$!values.get('workFlowNodeName')")draft#end";	
}
#if($!OAWorkFlow)
//审核按钮的超链接
function check(str){
	if(str=="approve"){
	/*
		var retVal=window.showModalDialog("/OAMyWorkFlow.do?keyId=$values.get("id")&tableName=$!tableName&operation=$globals.getOP("OP_AUDITING_PREPARE")&fromPage=erp",this,'dialogWidth=750px;dialogHeight=450px');

		if(retVal=="deliverTo"){//如果用户已经确定转交了，刷新此页面			
			location.href='/UserFunctionQueryAction.do?operation=$globals.getOP("OP_QUERY")&parentCode=$globals.classCodeSubstring("$!parentCode",5)&tableName=$tableName&f_brother=$!f_brother&checkTab=Y&parentTableName=$!parentTableName&draftQuery=$!saveDraft&currentRow=$!values.get("id")&winCurIndex=$!winCurIndex&pageNo=$!pageNo'
		}
	*/
	checkDialog('/OAMyWorkFlow.do?keyId=$values.get("id")&tableName=$!tableName&operation=$globals.getOP("OP_AUDITING_PREPARE")&fromPage=erp&moduleType=$!moduleType&f_brother=$!f_brother&winCurIndex=$!winCurIndex&pageNo=$!pageNo&&parentCode=$globals.classCodeSubstring("$!parentCode",5)&parentTableName=$!parentTableName&saveDraft=$!saveDraft');	
	}else if(str=="reverse"){
		location.href="/UserFunctionQueryAction.do?tableName=$!tableName&designId=$!designId&keyId=$values.get("id")&f_brother=$!f_brother&operation=$globals.getOP("Op_RETAUDITING")&winCurIndex=$!winCurIndex&pageNo=$!pageNo&parentTableName=$!parentTableName&saveDraft=$!saveDraft&detailReCheck=true";
	}
}
#end

//审核弹出新窗口







function checkDialog(url){
	var urls = url+"&checkFlag=true";
	asyncbox.open({
		id : 'dealdiv',
　　　   	url : urls,
	 	title : '$text.get("common.lb.approve")',
　　 　 	width : 650,
　　 	height : 370,
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

function dealCheck(url){
	jQuery.close('deliverTo');
	window.location.href = url + "&operation=5";
}

function dealAsyncbox(){
	jQuery.close('dealdiv');
	window.location.href = '/UserFunctionQueryAction.do?operation=$globals.getOP("OP_QUERY")&parentCode=$globals.classCodeSubstring("$!parentCode",5)&tableName=$tableName&f_brother=$!f_brother&checkTab=Y&parentTableName=$!parentTableName&draftQuery=$!saveDraft&currentRow=$!values.get("id")&winCurIndex=$!winCurIndex&pageNo=$!pageNo';
}
//扩展按扭
function extendSubmit(vars,vname,selected)
{
	document.getElementById("ButtonTypeName").value = vname;
	if(vars == "custom"){
		document.getElementById("ButtonType").value = 'custom';
	}else if(vars == "handover"){
		var varValue = window.showModalDialog("/vm/classFunction/selectCRMCustomer.jsp","","dialogWidth=465px;dialogHeight=260px") ;
		if(typeof(varValue)=="undefined")return ;
		var arrValue = varValue.split("|") ;
		document.getElementById("newCreateBy").value=arrValue[0] ;
		document.getElementById("wakeUp").value=arrValue[1] ;
		document.getElementById("ButtonType").value = "handover" ;
	}else if(vars == "sendEmail"){
		document.getElementById("ButtonType").value = "sendEmail" ;
		if(window.parent.document.getElementById("bottomFrame")){
			var varMin = window.parent.document.getElementById("moddiFrame").contentWindow.document.getElementById("div_min");
			if(typeof(varMin)!="undefined"){
				varMin.click() ;
			}
		}
	}else if(vars == "sendSMS"){
		var varValue = window.showModalDialog("/vm/classFunction/sendMessage.jsp","","dialogWidth=645px;dialogHeight=350px") ;
		if(typeof(varValue)=="undefined")return ;
		document.getElementById("sendMessage").value = varValue ;
		document.getElementById("ButtonType").value = "sendSMS" ;
	}else if(vars == "folderRight"){
		var listObj = document.getElementById("functionListObj");
		var keyIdStr=listObj.getCBoxValue("hidden");
		//保存已经添加的数据






		AjaxRequest('/UtilServlet?operation=folderRight&type=getData&tableName='+form.tableName.value+'&keyId='+keyIdStr);
		var varValue = window.showModalDialog("/vm/oa/oaPublicMsg/folderRight.jsp?tableName=$!tableName&keyId="+keyIdStr,"","dialogWidth=565px;dialogHeight=350px") ;
		if(typeof(varValue)=="undefined")return ;
	}else if(vars == "dataMove"){
		displayName = encodeURI("$text.get('common.msg.gradeDirectory')") ;
		var selectName = document.getElementById("selectName").value ;
		var strURL = "/UserFunctionAction.do?&selectName="+selectName+"&isRoot=isRoot&operation=$globals.getOP("OP_POPUP_SELECT")" ;
		var str  = window.showModalDialog(strURL+"&isQuote=1&popDataType=dataMove&MOID="+MOID+"&MOOP=query&LinkType=@URL:&displayName="+displayName,"",
											"dialogWidth=750px;dialogHeight=430px");
		if(typeof(str)=="undefined")return false ;
		var fieldValue=str.split(";"); 
		
		document.getElementById("classCode").value=fieldValue[0];
		document.getElementById("ButtonType").value = "dataMove" ;
		if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
	}else if(vars=="billExport"){
		if(!confirm("$text.get('common.msg.whetherExport')"))return
		document.getElementById("ButtonType").value = "billExport" ;
		if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
	}else if(vars=="StopValue_BaseInfo_tblEmployee"){
		var chks = document.getElementsByName("keyId");
		for(var i=0;i<chks.length;i++){
			if(chks[i].checked){
				var v = chks[i].value;
				if(v=="1"){
					alert("$text.get('common.msg.SuperAdmin')");
					return false;
				}
			}
		}
		document.getElementById("ButtonType").value = "execDefine" ;
		form.defineName.value=vars;
	}else if(vars == "on" || vars == "down" || vars == "all"){
		var opType  = "update" ;
		var updateImg = "" ;
		if("all"==vars){
			if(!confirm("$text.get('common.msg.synchronizeBol88')")){
				return ;
			}
			if(confirm("$text.get('common.msg.whetherUploadImg')")){
				updateImg = "yes" ;
			}
		}else{
			if("down"==vars){
				if(confirm("$text.get('common.msg.whetherDelGoodData')")){
					opType = "delete" ;
				}
			}else{
				if(confirm("$text.get('common.msg.whetherUpdateImg')")){
					updateImg = "yes" ;
				}
			}
		}
		document.getElementById("ButtonType").value = "shelf" ;
		document.getElementById("opType").value = opType ;
		document.getElementById("updateImg").value = updateImg ;
		document.getElementById("shelfType").value = vars ;
		if("down"==vars){
			if(typeof(top.jblockUI)!="undefined"){
				top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
			}
		}else{
			setInterval("Refresh()",1000);
		}
	}else if(vars=="ImageCatalog"){
		document.getElementById("ButtonType").value = "ImageCatalog" ;
		alert("$text.get('common.msg.ProduceImagesList')") ;
	}else{
		document.getElementById("ButtonType").value = "execDefine" ;
		form.defineName.value=vars;
	}
	form.operation.value=$globals.getOP("OP_EXTENDBUTTON_DEFINE");
	form.submit();
}


//复核
var copyInput;
function reAuditOp(inputType,fieldType,fieldName,fieldValue,obj,openSel,popupType,openSelId){	
	if($(".listRange_1 button").length == 1 || $("button[id='Remark']").attr("id") != undefined){
		$(".listRange_1 button").prev().prev().after(copyInput);
		$(".listRange_1 button").prev().remove();
		$(".listRange_1 button").remove();
		
		if($("button[id='Remark']").attr("id") != undefined){
			$("button[id='Remark']").prev().prev().after(copyInput);
			$("button[id='Remark']").prev().remove();
			$("button[id='Remark']").remove();
	
		}
	}
	copyInput = $(obj).clone(true)
	var reAuditBefore = "reAuditOp('" + inputType + "','" + fieldType + "','" + fieldName +"'";//用于确定后好拼接<span>用 
	var reAuditAfter = "this,'" + openSel + "','" + popupType + "','" + openSelId +"')"; //用于确定后好拼接<span>用






	var str = "";
	var selectId = fieldName;
	if(inputType == "0"){
		if(fieldType == "5"){
			str = '<input id="' + fieldName + '" name="' + fieldName +'" type="text" onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onClick="WdatePicker({lang:\'zh_CN\'});" value="' + fieldValue + '"><button type="button" id="' + selectId + '" onclick="auditSubmit(this,\'' + inputType +'\')" reAuditBefore="' + reAuditBefore + '" reAuditAfter ="' + reAuditAfter +'">确定</button>';
		}else if(fieldType == "3"){
			str = '<textarea rows="5" style="width:85%;" id="' + fieldName + '" name="' + fieldName + '" >' + fieldValue + '</textarea><button type="button" id="' + selectId + '" onclick="auditSubmit(this,\'' + inputType +'\',\'true\')" reAuditBefore="' + reAuditBefore + '" reAuditAfter ="' + reAuditAfter +'">确定</button>'
		}else{
			str = '<input name="' + fieldName +'" type="text" value="' + fieldValue +'"><button type="button" id="' + selectId + '" onclick="auditSubmit(this,\'' + inputType +'\')" reAuditBefore="' + reAuditBefore + '" reAuditAfter ="' + reAuditAfter +'">确定</button>';
		}				
	}else if(inputType == "1"){
		var enumId = $(obj).attr("id");
		var enumVals =  $("#" + fieldName + "_Enum").val();
		var selectStr =  '<select name="' + fieldName +'" id="'+ fieldName +'">';
		for(var i=0;i<enumVals.split(",").length-1;i++){
			var en = enumVals.split(",")[i];
			var enumValue = en.split(":")[0];
			var enumName = en.split(":")[1];
			selectStr +='<option value="' + enumValue +'"';  
			if(enumId == enumValue){
				selectStr += ' selected="selected"';  
			}
				selectStr += '>' + enumName + '</option>';
		}
		selectStr += '</select><button type="button" id="' + selectId + '" onclick="auditSubmit(this,\'' + inputType +'\')" reAuditBefore="' + reAuditBefore + '" reAuditAfter ="' + reAuditAfter +'" fieldName="' + fieldName + '" >确定</button>';
		str = selectStr;
	}else if(inputType == "7"){
		//var dyGetPYM = "dyGetPYM('" + openSel + "',this,'" + fieldName + "')"
		//str = '<input id="' + fieldName + '" name="' + fieldName +'" type="text" onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onpropertychange="' + dyGetPYM + '" value="' + fieldValue + '"><button type="button" id="' + selectId + '" onclick="auditSubmit(this,\'' + inputType +'\')" reAuditBefore="' + reAuditBefore + '" reAuditAfter ="' + reAuditAfter +'">确定</button>';
	}else if(inputType == "9"){
		//if(fieldType == "1"){
		//}else{
		//	str = '<input id="' + fieldName + '" name="' + fieldName +'" type="text" onKeyDown="if(event.keyCode==13){event.keyCode=9;}" value="' + fieldValue + '"><button type="button" id="' + selectId + '" onclick="auditSubmit(this,\'' + inputType +'\')" reAuditBefore="' + reAuditBefore + '" reAuditAfter ="' + reAuditAfter +'">确定</button>';
		//}
	}else if(inputType == "2"){
		selectId = openSelId;
		str = '<input id="' + fieldName + '" name="' + fieldName  + '" value ="' + fieldValue + '" type="text"';
		if(inputType != "8"){
			str += ' readonly="readonly" ';
			if(popupType != "2"){
				str += ' onDblClick="' + $(obj).attr("name") + '"';  
			}
		}
		//if(popupType != "1"){
		//	str += ' onKeyPress="if(event.keyCode ==13){' +  $(obj).attr("name") + '}'
		//}
		str += ' ><button type="button" id="' + selectId + '" onclick="auditSubmit(this,\'' + inputType +'\')" reAuditBefore="' + reAuditBefore + '" reAuditAfter ="' + reAuditAfter +'" doubleC ="' + $(obj).attr("name") + '">确定</button>'
		setTimeout($(obj).attr("name"),'100');
	}
	
	$(obj).prev().after(str);
	if(inputType == "1"){
		$(obj).parent().find("select").select();
		$(obj).parent().find("select").focus();
	}else{
		$(obj).parent().find("input").select();
	}
	$(obj).remove();
}

//提交复核
function auditSubmit(obj,inputType,textareaFlag){
	var tableName = $("#tableName").val();
	var keyId = $("#varKeyIds").val();
	var colName = $(obj).attr("id");
	var colValue = "";
	if(inputType == 2){
		colValue = $("#" + $(obj).attr("id")).val();
	}else{
		colValue = $(obj).prev().val();
	}
	var url = "/UserFunctionAction.do?operation=2&type=reAudit&tableName=" + tableName +"&keyId=" + keyId + "&colName=" + colName + "&colValue=" + encodeURIComponent(encodeURIComponent(colValue))
	jQuery.ajax({
	   type: "POST",
	   url: url,
	   success: function(msg){
	     if(msg == "ok"){
	     	var spanValue = $(obj).prev().val();
	     	if(inputType == "1"){
	     		var enumId = $(obj).prev().val();
				var enumVals =  $("#" + $(obj).attr("fieldName") + "_Enum").val();
	     		for(var i=0;i<enumVals.split(",").length-1;i++){
					var en = enumVals.split(",")[i];
					var enumValue = en.split(":")[0];
					var enumName = en.split(":")[1];
					if(enumId == enumValue){
			     		spanValue = enumName;
					}
				}
	     	}
	     	
	     	if(textareaFlag == "true"){
	     		if(spanValue == ""){
	     			spanValue = "无";
	     		}
	     	}
	     	
	     	var oncli = $(obj).attr("reAuditBefore") + ",'" + $(obj).prev().val()  + "'," + $(obj).attr("reAuditAfter");
	     	var str = '<span title="' + spanValue + '" class="input" onclick="' + oncli + '"'
	     	if(inputType == "2"){
	     		str += ' name="' + $(obj).attr("doubleC") + '"';  
	     	}
	   		str += '>' + spanValue + '</span>'
			$(obj).prev().prev().after(str);
			$(obj).prev().remove();	     
			$(obj).remove();	     
	     }else{
	     	alert(msg);
	     	$(obj).prev().select();
	     	$(obj).prev().focus();
	     }
	     
	   }
	});
}


$(function(){
	$(".h-child-btn").mouseover(function(){
		$(this).addClass("h-height").find(".d-more").show().siblings(".br-link").show();
	}).mouseout(function(){
		$(this).removeClass("h-height").find(".d-more").hide().siblings(".br-link").hide();
	});
	//决定是否有上下条按扭
	if(typeof(parent.window.document.getElementsByName("keyId").length) == 'undefined' || parent.window.document.getElementsByName("keyId").length<=0){
		$("#preOne").hide();
		$("#nextOne").hide();
	}
});
</script>
#if("$!Safari"=="true")
<style>
.listRange_1 li{height:32px;}
.listRange_1 input{border: 1px solid #1E95E9;}
</style>
#end
</head>

<body scroll="no" onLoad="checkHasFrame();#foreach ($rowlist in $childTableList ) #if("$globals.getSysSetting($rowlist.tableSysType)"=="true" && "$rowlist.isUsed"=="1" && ($childTableConfigList.get($rowlist.tableName).size()>0 || $allConfigList.size()==0)) #set($gridtable =$rowlist.tableName+"Table")  #set($griddata =$rowlist.tableName+"Data") showtable('$gridtable');  initTableList(${gridtable}DIV,${gridtable},$griddata,$rowlist.defRowCount);sortables_init('${rowlist.tableName}Table');#end #end ulMaxWidth();showGridTags();">
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" onKeyDown="down()" scope="request" name="form" action="/UserFunctionAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" id="operation" name="operation" value="$globals.getOP("OP_UPDATE")"/>
<input type="hidden" id="org.apache.struts.taglib.html.TOKEN" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/>
<input type="hidden" id="tableName" name="tableName" value="$!tableName"/>
<input type="hidden" id="button" name="button" value=""/>
<input type="hidden" id="frameName" name="frameName" value="$fName"/>
<input type="hidden" id="winCurIndex" name="winCurIndex" value="$winCurIndex"/>
<input type="hidden" id="pageNo" name="pageNo" value="$!pageNo" />
<input type="hidden" id="parentTableName" name="parentTableName" value="$!parentTableName"/>
<input type="hidden" id="defineInfo" name="defineInfo" value=""/>
<input type="hidden" id="moduleType" name="moduleType" value="$!moduleType"/>
<input type="hidden" id="detLineNo" name="detLineNo" value=""/>
<input type="hidden" id="saveDraft" name="saveDraft" value="$saveDraft"/>
<input type="hidden" id="noback" name="noback" value="$!noback"/>
<input type="hidden" id="logicValidate" name="logicValidate" value=""/>
<input type="hidden" id="defineName" name="defineName" value=""/>
<input type="hidden" id="checkFieldName" name="checkFieldName" value=""/>
<input type="hidden" id="fromCRM" name="fromCRM" value="$!fromCRM"/>
<input type="hidden" id="ButtonType" name="ButtonType" value=""/>
<input type="hidden" id="varKeyIds" name="varKeyIds" value="$!values.get("id")"/>
<input type="hidden" id="id" name="id" value="$!values.get("id")" />
<input type="hidden" id="from" name="from" value=""/>
<input type="hidden" id="hasFrame" name="hasFrame" value=""/>
#if("$!detailFlow"=="detailFlow" || ("$!existFlow"=="exist" ))
<input type="hidden" id="varWakeUp" name="varWakeUp" value=""/>
<input type="hidden" id="varNextNode" name="varNextNode" value=""/>
<input type="hidden" id="varCheckPersons" name="varCheckPersons" value=""/>
<input type="hidden" id="varAttitude" name="varAttitude" value=""/>
<input type="hidden" id="optionType" name="optionType" value=""/>
<input type="hidden" id="isMyFlow" name="isMyFlow" value="$isMyFlow"/>

#end
<input type="hidden" id="OAWorkFlow" name="OAWorkFlow" value="#if($!OAWorkFlow)true#{else}false#end"/>
#if($!OAWorkFlow)
<input type="hidden" id="designId" name="designId" value="$!designId"/>
<input type="hidden" id="currNodeId" name="currNodeId" value="$!currNodeId"/>
#end
#if("$copy" == "copy")
<input type="hidden" id="parentCode" name="parentCode" value="$globals.classCodeSubstring("$!parentCode",0)"/>
#else
<input type="hidden" id="parentCode" name="parentCode" value="$globals.classCodeSubstring("$!parentCode",5)"/>
#end
#set($printCount=false)

<div class="Heading">
	<div class="HeadingTitle">
		<b class="icons"></b>
		#if("$!moduleName"=="")$!mainTable.display.get("$globals.getLocale()")#else$!moduleName#end -详情
	 </div>	 
	 
#if($!tabList == "" && "true"!="$!isLinkType")
	<ul class="HeadingButton f-head-u">
		<li><span class="btn btn-small" id="preOne" onClick="changePos('PRE');">上条</span></li>
		<li><span class="btn btn-small" id="nextOne" onClick="changePos('NEXT');">下条</span></li>
	#set($msg=$text.get("define.button.reverse"))
	#if($globals.getMOperationMap($request).update()&&$!updateRight && "$workFlow"!="OA")
		<li><span class="btn btn-small" #if($!CannotOper)disabled="true"#end onClick="update();">$text.get("common.lb.update")</span></li>
	#end	
	#if($!checkRight)
	<li><span class="btn btn-small" #if($!CannotOper)disabled="true"#end onClick="check('approve');">$text.get("common.lb.approve")</span></li>	
	#end
	
	#if($!retCheckRight)
	<li><span class="btn btn-small" #if($!CannotOper)disabled="true"#end onClick="check('reverse');">$text.get("define.button.reverse")</span></li>	
	#end	
	#if("draft"=="$!values.get('workFlowNodeName')"&&$!tableName!="tblView")
	<li><span class="btn btn-small" #if($!CannotOper)disabled="true"#end onClick="draftAudit();">$text.get("common.lb.draftSave")</span></li>
	#end
	#if("$auditprint"=="0"&&"$print"=="true"&& $globals.getMOperationMap($request).print())
		#set($printCount=true)
		<li><span class="btn btn-small" onClick="printControl('/UserFunctionQueryAction.do?tableName=$tableName&reportNumber=$!reportNumber&BillID=$!values.get("id")&operation=$globals.getOP("OP_PRINT")&moduleType=$!moduleType&parentTableName=$!parentTableName&winCurIndex=$!winCurIndex')">$text.get("common.lb.print")</span></li>
	#elseif(("$flowStatus"=="finish"||"$flowStatus"==""||$currNodeId==-1)&&"$print"=="true"&& $globals.getMOperationMap($request).print())
		#set($printCount=true)
		<li><span class="btn btn-small" onClick="printControl('/UserFunctionQueryAction.do?tableName=$tableName&reportNumber=$!reportNumber&BillID=$!values.get("id")&operation=$globals.getOP("OP_PRINT")&moduleType=$!moduleType&parentTableName=$!parentTableName&winCurIndex=$!winCurIndex')">$text.get("common.lb.print")</span></li>
	#end
		
		<li>
			<div class="btn btn-small h-child-btn">
				<a id="tb_toolBtn" moreId="toolMore">操作</a>
				<div id="toolMore" class="d-more" >
				<div class="out-css">
				#if($globals.getMOperationMap($request).add() && "true"!="$!isLinkType" && "$workFlow"!="OA" && !$!CannotOper) 
					<a onClick="location.href='/UserFunctionAction.do?tableName=$tableName&operation=6&parentTableName=$!parentTableName&moduleType=$!moduleType&f_brother=$!values.get("f_brother")&noback=$!noback&winCurIndex=$winCurIndex'">$text.get("common.lb.add")</a>
					#if($globals.getTableInfoBean($tableName).getExtendButton().indexOf("copy")>0 && !$!CannotOper )
						<a href="javascript:copyOp('$!values.get("id")')">$text.get("common.lb.copy")</a>
					#end
				#end
				$!extendButton 
				#foreach($brow in $customButton)
					<li>$!brow</li>
				#end
				 
				#if($mainTable.wakeUp=="1")
					<a onClick="javascript:alertSet('$!values.get("id")')">$text.get("workflow.msg.warmsetting")</a>
				#end
				#if($inIniAmount)
				   <a id="inIniAmount" onClick="location.href='/UserFunctionAction.do?tableName=$tableName&keyId=$keyId&operation=$globals.getOP("OP_UPDATE_PREPARE")&moduleType=$!moduleType&f_brother=$!f_brother&parentTableName=$!parentTableName&winCurIndex=$!winCurIndex&onOff=onOff'">$!inIniAmountName</a>
				#end
				#if($globals.getMOperationMap($request).sendEmail() && "$!quote" != "quote" && "$!saveDraft" != "quoteDraft")
					#if("$!detail"!="detail")#set($isUpdate="update")#end
					#if($!OAWorkFlow)
					<li><span class="btn btn-small" id="addPre" name="addPre" onClick="javascript:mdiwin('/UserFunctionAction.do?noback=true&fromJsp=$!isUpdate&type=email&parentCode=$globals.classCodeSubstring("$!parentCode",5)&keyId=$keyId&moduleType=$!moduleType&tableName=$tableName&operation=$globals.getOP("OP_DETAIL")&parentTableName=$!parentTableName&saveDraft=$!saveDraft','$text.get("oa.mail.sendBill")')" >$text.get("common.lb.sendmail")</span></li>	
					#else
					<li><span class="btn btn-small" id="addPre" name="addPre" onClick="location.href='/UserFunctionAction.do?noback=$!noback&fromJsp=$!isUpdate&type=email&parentCode=$globals.classCodeSubstring("$!parentCode",5)&keyId=$keyId&moduleType=$!moduleType&tableName=$tableName&operation=$globals.getOP("OP_DETAIL")&parentTableName=$!parentTableName&saveDraft=$!saveDraft&winCurIndex=$!winCurIndex'">$text.get("common.lb.sendmail")</span></li>	
					#end
				#end
				#if($!OAWorkFlow)
				<li><span class="btn btn-small" onClick="javascript:flowDepict('$!designId','$values.get('id')');">$text.get("common.lb.checkFlowChart")&nbsp;</span></li>
				#end
				</div>
				</div>
				<b class="triangle"></b>
			</div>			
		</li>	
		
		#if("$!queryBtn"!="")
		<li>
			<div class="btn btn-small h-child-btn">
				<a id="tb_queryBtn" moreId="queryMore">联查</a>
				<div id="queryMore" class="d-more">
					<div class="out-css">
						$!queryBtn		
					</div>		
				</div>
				<b class="triangle"></b>
			</div>			
		</li>
		#end
		#if("$!pushBtn"!="")
		<li>
			<div class="btn btn-small h-child-btn">
				<a id="tb_pushBtn" moreId="pushMore">推单</a>
				<div id="pushMore" class="d-more">
					<div class="out-css">
						$!pushBtn		
					</div>		
				</div>
				<b class="triangle"></b>
			</div>			
		</li>
		#end
				
		<li><span class="btn btn-small" id="backList"  name="backList" onClick="closeWindows();">$text.get("common.lb.close")</span></li>
	</ul>
	#elseif($!tabList == 1)
		<ul class="HeadingButton f-head-u">
		<li><button type="button" id="backList"  name="backList" onClick="closeWin();" class="b2">$text.get("common.lb.close")</button></li>
		#if("$auditprint"=="0"&&"$print"=="true"&& $globals.getMOperationMap($request).print())
			#set($printCount=true)
			<li><span class="btn btn-small" onClick="printControl('/UserFunctionQueryAction.do?tableName=$tableName&reportNumber=$!reportNumber&BillID=$!values.get("id")&operation=$globals.getOP("OP_PRINT")&moduleType=$!moduleType&parentTableName=$!parentTableName&winCurIndex=$!winCurIndex')">$text.get("common.lb.print")</span></li>
		#elseif(("$flowStatus"=="finish"||"$flowStatus"==""||$currNodeId==-1)&&"$print"=="true"&& $globals.getMOperationMap($request).print())
			#set($printCount=true)
			<li><span class="btn btn-small" onClick="printControl('/UserFunctionQueryAction.do?tableName=$tableName&reportNumber=$!reportNumber&BillID=$!values.get("id")&operation=$globals.getOP("OP_PRINT")&moduleType=$!moduleType&parentTableName=$!parentTableName&winCurIndex=$!winCurIndex')">$text.get("common.lb.print")</span></li>
		#end
		</ul>
	#end
</div>

#if($parentName.length()>0)			
<div class="scroll_function_big"><span>$text.get("common.msg.currstation")：$!parentName</span>
</div>
#end
	<div id="listRange_id" style="width:auot;padding:0 10px;">
	#if($!tabList == "")
		<script type="text/javascript">
			var oDiv2=m("listRange_id");
			var sHeight2=document.documentElement.clientHeight-32;
			oDiv2.style.height=sHeight2+"px";
		</script>
	#else
		<script type="text/javascript">
			var oDiv2=m("listRange_id");
			var sHeight2=document.documentElement.clientHeight;
			oDiv2.style.height=sHeight2+"px";
		</script>
	#end
		<div class="listRange_1">
		<ul class="wp_ul">
		#set($totalFields=0)
		#set($audit = "false")
		#if("$globals.getSysSetting('ReAudit')" == "true" && "$reAudit" == "1" )
			#set($audit = "true")
		#end
#foreach ($row in $fieldInfos )
#if($globals.getScopeRight($mainTable.tableName,$row.fieldName,$scopeRight))
#if($row.fieldName=="printCount" && $printCount==true)
	<li><div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>
	<input class="re_ol" type="text" readonly="readonly" value="$!values.get("printCount")"/>
#elseif("$row.inputType" != "100" && "$row.fieldType" != "3"  && "$row.fieldType" != "16"  && "$row.fieldType" != "13" && "$row.fieldType" != "14" && $row.fieldName != "id" && $row.fieldName != "f_brother" && $row.fieldName != "createBy" && $row.fieldName != "createTime" && $row.fieldName != "lastUpdateBy" && $row.fieldName != "lastUpdateTime")
#if("$row.inputType" == "0")
       #if("$row.fieldType" == "17")
<li><div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div> <span class='input' title="$!values.get($row.fieldName)" #if("$audit" == "true" && "$row.isReAudit" == "1")onclick="reAuditOp($row.inputType,$row.fieldType,$row.fieldName,$!values.get($row.fieldName),this,'','','')" #end>$!values.get($row.fieldName)</span></li>
#if("$row.width"=="0")
<li style="float: none;width: 1px;"></li>
#end
#elseif("$row.fieldType" == "5" )
<li>#if("$audit" == "true" && "$row.isReAudit" == "1")<div class="swa_c1" style="color:red;" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div> #else <div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div> #end<span class='input'  title="$!values.get($row.fieldName)" #if("$audit" == "true" && "$row.isReAudit" == "1") onclick="reAuditOp('$row.inputType','$row.fieldType','$row.fieldName','$!values.get($row.fieldName)',this,'','','')" #end>$!values.get($row.fieldName)</span></li>
<input id="$row.fieldName" name="$row.fieldName" type="hidden" value="$!values.get($row.fieldName)"/>
#if("$row.width"=="0")
<li style="float: none;width: 1px;"></li>
#end	
#elseif("$row.fieldType" == "6")
<li>#if("$audit" == "true" && "$row.isReAudit" == "1")<div class="swa_c1" style="color:red;" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div> #else <div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>#end <div class="swa_c2" ><input type="text" size="20" class="inputMid" title="$!values.get($row.fieldName)" readonly="readonly" value="$!values.get($row.fieldName)" #if("$audit" == "true" && "$row.isReAudit" == "1") onclick="reAuditOp('$row.inputType','$row.fieldType','$row.fieldName','$!values.get($row.fieldName)',this,'','','')" #end/></div></li>
#elseif("$row.fieldType" == "19")
<li><div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div> <span class='input'  title="$!values.get($row.fieldName)" #if("$audit" == "true" && "$row.isReAudit" == "1") onclick="reAuditOp('$row.inputType','$row.fieldType','$row.fieldName','$!values.get($row.fieldName)',this,'','','')" #end>$!values.get($row.fieldName)</span></li>
#if("$row.width"=="0")
<li style="float: none;width: 1px;"></li>
#end
#elseif("$row.fieldType" == "1"||"$row.fieldType" == "0")
<li>#if("$audit" == "true" && "$row.isReAudit" == "1")<div class="swa_c1" style="color:red;" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div> #else <div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div> #end<span class='input' title="$!globals.formatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)" #if("$audit" == "true" && "$row.isReAudit" == "1") onclick="reAuditOp('$row.inputType','$row.fieldType','$row.fieldName','$!globals.formatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)',this,'','','')" #end>$!globals.formatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)</span></li>
#if("$row.width"=="0")
<li style="float: none;width: 1px;"></li>
#end	
#elseif("$row.fieldType" == "18")
<li class="longChar"><div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><input type="text" size="20" class="inputLong"  title="$!values.get($row.fieldName)" readonly="readonly" value="$!values.get($row.fieldName)" #if("$audit" == "true" && "$row.isReAudit" == "1") onclick="reAuditOp('$row.inputType','$row.fieldType','$row.fieldName','$!values.get($row.fieldName)',this,'','','')" #end/></li>
#if("$row.width"=="0")
<li style="float: none;width: 1px;"></li>
#end	
#elseif("$row.fieldType" == "20")
<li>#if("$audit" == "true" && "$row.isReAudit" == "1")<div class="swa_c1" style="color:red;" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div> #else <div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div> #end <span class='input'  title="$!values.get($row.fieldName)" #if("$audit" == "true" && "$row.isReAudit" == "1") onclick="reAuditOp('$row.inputType','$row.fieldType','$row.fieldName','$!values.get($row.fieldName)',this,'','','')" #end>$!values.get($row.fieldName)</span></li>
#if("$row.width"=="0")
<li style="float: none;width: 1px;"></li>
#end
#else
<li>#if("$audit" == "true" && "$row.isReAudit" == "1")<div class="swa_c1" style="color:red;" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>#else <div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>#end<span class='input' title='$!values.get($row.fieldName)' #if("$audit" == "true" && "$row.isReAudit" == "1") onclick="reAuditOp('$row.inputType','$row.fieldType','$row.fieldName','$!values.get($row.fieldName)',this,'','','')" #end>$!globals.encodeHTML4("$!values.get($row.fieldName)")&nbsp;</span></li>
#if("$row.width"=="0")
<li style="float: none;width: 1px;"></li>
#end
#end
	#set($totalFields=$totalFields+1)		
#elseif("$row.inputType" == "8")		
		#if("$row.fieldType" == "1")
		<li><div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><span class='input' title="$!globals.formatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)">$!globals.formatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)</span></li>
		#elseif("$row.fieldType" == "18")
		<li class="longChar">#if("$audit" == "true" && "$row.isReAudit" == "1")<div class="swa_c1" style="color:red;" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>#else<div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>#end<input type="text" size="20" class="inputLong" title="$!values.get($row.fieldName)" readonly="readonly" value="$!values.get($row.fieldName)"/></li>
		#elseif("$row.fieldType" == "20")
		<li>#if("$audit" == "true" && "$row.isReAudit" == "1")<div class="swa_c1" style="color:red;" title='$row.display.get("$globals.getLocale()"))'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()"))</div></div><div class="d_mh"></div></div> #else <div class="swa_c1" title='$row.display.get("$globals.getLocale()"))'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()"))</div></div><div class="d_mh"></div></div> #end<span class='input' style="width:103px;" title="$!values.get($row.fieldName)">$!values.get($row.fieldName)</span></li>		
		#else
		<li>#if("$audit" == "true" && "$row.isReAudit" == "1")<div class="swa_c1" style="color:red;" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>#else<div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div> #end<span class='input' title="$!values.get($row.fieldName)">$!values.get($row.fieldName)</span></li>					
		#end
		#set($totalFields=$totalFields+1)
		#if("$row.width"=="0")
		<li style="float: none;width: 1px;"></li>
		#end
#elseif("$row.inputType" == "1")
	<li>#if("$audit" == "true" && "$row.isReAudit" == "1")<div class="swa_c1" style="color:red;" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>#else <div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>#end<span class='input' title="$globals.getEnumerationItemsDisplay("$row.refEnumerationName","$values.get($row.fieldName)")" id="$values.get($row.fieldName)" #if("$audit" == "true" && "$row.isReAudit" == "1") onclick="reAuditOp('$row.inputType','$row.fieldType','$row.fieldName','$!values.get($row.fieldName)',this,'','','')" #end>$globals.getEnumerationItemsDisplay("$row.refEnumerationName","$values.get($row.fieldName)")</span></li>
	<div style="display: none;">
		 	<input id="${row.fieldName}_Enum" value="#foreach($item in $globals.getEnumerationItems("$row.refEnumerationName"))$item.value:$item.name,#end"/>	 
	</div>
	#set($totalFields=$totalFields+1)
	#if("$row.width"=="0")
	<li style="float: none;width: 1px;"></li>
	#end
#elseif("$row.inputType" == "16")
	<li #if("$row.fieldType" == "18")style="width:95%;"#end><div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><span class='input' title='$!values.get($row.fieldName)' >$!globals.encodeHTML4($!values.get($row.fieldName))</span></li>
#elseif("$row.inputType" == "3" || "$row.inputType" == "6")
	#if("$row.fieldType" == "1")
		<input id="$row.fieldName" name="$row.fieldName" type="hidden" value="#if($!isDetail)$!globals.formatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)#else$!globals.newFormatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)#end">	
	#else
  		<input id="$row.fieldName" name="$row.fieldName" type="hidden" value="$!values.get($row.fieldName)">	
  	#end
#elseif("$row.inputType" == "7") 
		#set($pymUrl="/UtilServlet?operation=ajaxPYM&type=updatePYM")
	<li #if("$row.fieldType" == "18")style="width:95%;"#end><div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><span class='input' title='$!values.get($row.fieldName)' >$!globals.encodeHTML4($!values.get($row.fieldName))</span></li>
#elseif("$row.inputType"=="9")
		#set($reqUrl="/UtilServlet?operation=calculate")
	#if("$row.fieldType" == "1")
	<li><div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><span class='input' title="$!globals.formatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)">$!globals.formatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)</span></li>
	#else
	<li><div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><span class='input' title="$!values.get($row.fieldName)">$!values.get($row.fieldName)</span></li>
	#end
	#if("$row.width"=="0")
	<li style="float: none;width: 1px;"></li>
	#end
#elseif("$row.inputType"=="10")
#set($enumList=$globals.getEnumerationItems($row.refEnumerationName))
<li style="width:${row.width}px;"><div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>
<div class="swa_c2" >
#foreach($erow in $enumList)
#set ($check="")
#if($erow.value == $!values.get($row.fieldName))#set ($check="checked")#end
<input type="radio" class="cbox" id="$row.fieldName" name="$row.fieldName" $!check value="$erow.value" /><label class="cbox_w">$erow.name</label>
#end
</div>
#elseif("$row.inputType" == "4")
#set($lstr = $!values.get($row.fieldName))
#if("$lstr" != "")
   #set($lan= $!values.get("LANGUAGEQUERY").get($lstr).get("$globals.getLocale()"))
#end 	
<li><div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div> <input class="re_ol" type="text" readonly="readonly" title="$!lan" value="$!lan"></li>
#if("$row.width"=="0")
<li style="float: none;width: 1px;"></li>
#end
#elseif("$row.inputType"==5)
#set($enumList=$globals.getEnumerationItems($row.refEnumerationName))
<li #if($enumList.size()!=1)style="width:98%;"#end><div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>
<div class="swa_c2" >
#foreach($erow in $enumList)
#set ($check="")
#foreach($fieldValue in $globals.strSplit("$!values.get($row.fieldName)",","))
#if($erow.value == $fieldValue)#set ($check="checked")#end#end
<input type="checkbox" class="cbox" id="$row.fieldName" name="$row.fieldName" $!check value="$erow.value" id="cbox_$erow.value" onclick="return false;"/><label for="cbox_$erow.value" class="cbox_w">$erow.name</label>
#end
</div>
</li>		
#elseif("$row.inputType" == "2")
	<input id="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" name="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" type="hidden" value="$!values.get($row.fieldName)"/>
#if($row.getSelectBean().relationKey.hidden)
#elseif("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1")
	#set($dismh="$row.fieldName"+"_mh")          
	#set($mainInput=$globals.getMainInputValue("$mainTable.tableName","$row.fieldName"))    
	#if($!row.logicValidate.indexOf("@IOO:Outstore")!=-1)
		#set($imgPopUrlValue="if(popMainInput('$mainInput'))openSelect('/UserFunctionAction.do?operation="+$globals.getOP("OP_POPUP_SELECT")+"&type=stockSequence&seq=1',n('$dismh')[0],'$dismh')")
		#set($dbPopUrlValue="if(popMainInput('$mainInput'))openSelect('/UserFunctionAction.do?operation="+$globals.getOP("OP_POPUP_SELECT")+"&type=addSequence&seq=1',n('$dismh')[0],'$dismh')")
	#else    
		#set($dbPopUrlValue="if(popMainInput('$mainInput'))openSelect('/UserFunctionAction.do?operation="+$globals.getOP("OP_POPUP_SELECT")+"&type=addSequence&seq=1',n('$dismh')[0],'$dismh')")
		#set($imgPopUrlValue=$dbPopUrlValue)
	#end
	<li>#if("$audit" == "true" && "$row.isReAudit" == "1")<div class="swa_c1" style="color:red;" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>#else <div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>#end
	
	<span class='input' name="$dbPopUrlValue" title="$globals.getSeqDis($!values.get($row.fieldName))" #if("$audit" == "true" && "$row.isReAudit" == "1") onclick="reAuditOp('$row.inputType','$row.fieldType','$dismh','$!values.get($row.fieldName)',this,'','$row.popupType','$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)')" #end>$globals.getSeqDis($!values.get($row.fieldName))<img src="/$globals.getStylePath()/images/St.gif" onClick="$imgPopUrlValue" /></span>
	<input id="$dismh" name="$dismh" type="hidden" size="20" value="$globals.getSeqDis($!values.get($row.fieldName))"/>
	
#else
#set($mainInput=$globals.getMainInputValue("$mainTable.tableName","$row.fieldName"))    
#set($popUrlValue="if(popMainInput('$mainInput'))openSelect('/UserFunctionAction.do?keyId=$!values.get($row.fieldName)&tableName=$mainTable.tableName&fieldName=$row.fieldName&operation="+$globals.getOP("OP_POPUP_SELECT")+"'#foreach($mainField in $row.getSelectBean().tableParams)+'&$mainField='+n('$mainField')[0].value#end,this,'$row.fieldName')")
<li>
	#if("$audit" == "true" && "$row.isReAudit" == "1")<div class="swa_c1" style="color:red;" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div> #else <div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div> #end
	#if("$row.fieldType" == "1")
	
		<span class='input' title="$!globals.formatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)" name="$popUrlValue" #if("$audit" == "true" && "$row.isReAudit" == "1") onclick="reAuditOp('$row.inputType','$row.fieldType','$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)','$!values.get($row.fieldName)',this,'','$row.popupType','$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)')" #end>$!globals.formatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)</span>
		<input id="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" name="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" value="#if($!isDetail)$!globals.formatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)#else$!globals.newFormatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)#end" type="hidden"/>
	
	#else
	
		<span class='input' title="$!values.get($row.fieldName)" name="$popUrlValue" #if("$audit" == "true" && "$row.isReAudit" == "1") onclick="reAuditOp('$row.inputType','$row.fieldType','$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)','$!values.get($row.fieldName)',this,'','$row.popupType','$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)')" #end>$!values.get($row.fieldName)</span>					
		<input id="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" name="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" value="$!values.get($row.fieldName)" type="hidden"/>
	
	#end
</li>
#end
#if("$row.width"=="0")
<li style="float: none;width: 1px;"></li>
#end
#if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
#foreach($srow in $row.getSelectBean().viewFields)	
#set($totalFields=$totalFields+1)
#set($colName="")
   #if($!srow.display!="")
	#if($!srow.display.indexOf("@TABLENAME")==0)
#set($index=$srow.display.indexOf(".")+1)#set($tableField=$tableName+"."+$srow.display.substring($index))
#else
#set($tableField=$srow.display)
#end
	#set($colName="$srow.display")
   #else
   	#set($tableField="")
   	#set($colName="$srow.fieldName")
   #end
#set($viewFieldType=$globals.getFieldBean($srow.fieldName).fieldType)
#set($dis = $globals.getFieldDisplay($tableName,$row.getSelectBean().name,$tableField))
#if($dis == "") 
#set($dis = $globals.getFieldDisplay($srow.fieldName)) #end
#set($mainInput=$globals.getMainInputValue("$mainTable.tableName","$row.fieldName"))    
#set($popUrlValue="if(popMainInput('$mainInput'))openSelect('/UserFunctionAction.do?keyId=$!values.get($row.fieldName)&tableName=$mainTable.tableName&fieldName=$row.fieldName&operation="+$globals.getOP("OP_POPUP_SELECT")+"'#foreach($mainField in $row.getSelectBean().tableParams)+'&$mainField='+n('$mainField')[0].value#end,this,'$row.fieldName')")
#set($ajaxUrlValue="'/UtilServlet?operation=Ajax&tableName=$mainTable.tableName&fieldName=$row.fieldName' #foreach($mainField in $row.getSelectBean().tableParams)+'&$mainField='+n('$mainField')[0].value#end")
#set($openUrlValue="'/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&tableName=$mainTable.tableName&fieldName=$row.fieldName' #foreach($mainField in $row.getSelectBean().tableParams)+'&$mainField='+n('$mainField')[0].value#end")												
#if($allConfigList.size()>0)
#if($globals.colIsExistConfigList("$mainTable.tableName","$colName","bill") || $!srow.display!="")
#if("$row.fieldType" == "18")
	<li class="longChar">
		#if("$audit" == "true" && "$row.isReAudit" == "1")<div class="swa_c1" style="color:red;" title='$dis'><div class="d_box"><div class="d_test">$dis</div></div><div class="d_mh"></div></div> #else <div class="swa_c1" title='$dis'><div class="d_box"><div class="d_test">$dis</div></div><div class="d_mh"></div></div> #end
		
		<span class='input' title="$!values.get($srow.asName)" name="$popUrlValue" #if("$audit" == "true" && "$row.isReAudit" == "1") onclick="reAuditOp('$row.inputType','$row.fieldType','$globals.getTableField($srow.asName)','$!values.get($row.fieldName)',this,'','$row.popupType','$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)')" #end>$!values.get($srow.asName)</span>
		<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)"  type="hidden" value="$!values.get($srow.asName)"/>
		
	</li>
#else
	<li>
		#if("$audit" == "true" && "$row.isReAudit" == "1")<div class="swa_c1" style="color:red;" title='$dis'><div class="d_box"><div class="d_test">$dis</div></div><div class="d_mh"></div></div>#else <div class="swa_c1" title='$dis'><div class="d_box"><div class="d_test">$dis</div></div><div class="d_mh"></div></div>#end
		#if($globals.getFieldBean($srow.display).fieldType==1||$globals.getFieldBean($srow.fieldName).fieldType==1)
		
		<span class='input' title="$!globals.newFormatNumber($!values.get($srow.asName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)" name="$popUrlValue" #if("$audit" == "true" && "$row.isReAudit" == "1") onclick="reAuditOp('$row.inputType','$row.fieldType','$globals.getTableField($srow.asName)','$!values.get($row.fieldName)',this,'','$row.popupType','$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)')" #end>$!globals.newFormatNumber($!values.get($srow.asName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)</span>
		<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)" value="$!globals.newFormatNumber($!values.get($srow.asName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)" type="hidden"/>
		
		#else
		
		<span class='input' title="$!values.get($srow.asName)" #if("$audit" == "true" && "$row.isReAudit" == "1") onclick="reAuditOp('$row.inputType','$row.fieldType','$globals.getTableField($srow.asName)','$!values.get($srow.asName)',this,'','$row.popupType','$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)')" #end name="$popUrlValue">$!values.get($srow.asName)</span>									
		<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)" value="$!values.get($srow.asName)" type="hidden"/>
		
		#end
	</li>
	#if("$row.width"=="0")
	<li style="float: none;width: 1px;"></li>
	#end
	#end		
	#set($tableField="")
	#end
#else
 #if("$srow.hiddenInput"=="true")
 		
 #else
 	#if("$row.fieldType" == "18")
		<li style="width:95%;">
			#if("$audit" == "true" && "$row.isReAudit" == "1")<div class="swa_c1" style="color:red;" title='$dis'><div class="d_box"><div class="d_test">$dis</div></div><div class="d_mh"></div></div> #else <div class="swa_c1" title='$dis'><div class="d_box"><div class="d_test">$dis</div></div><div class="d_mh"></div></div> #end
			
			<span class='input' title="$!values.get($srow.asName)" name="$popUrlValue" #if("$audit" == "true" && "$row.isReAudit" == "1") onclick="reAuditOp('$row.inputType','$row.fieldType','$globals.getTableField($srow.asName)','$!values.get($row.fieldName)',this,'','$row.popupType','$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)')" #end >$!values.get($srow.asName)</span></li>
			<input style="width:550px;" id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)" value="$!values.get($srow.asName)" type="hidden"/>
			
	#else
		<li>
			#if("$audit" == "true" && "$row.isReAudit" == "1")<div class="swa_c1" style="color:red;" title='$dis'><div class="d_box"><div class="d_test">$dis</div></div><div class="d_mh"></div></div> #else <div class="swa_c1" title='$dis'><div class="d_box"><div class="d_test">$dis</div></div><div class="d_mh"></div></div>#end
			#if($globals.getFieldBean($srow.display).fieldType==1||$globals.getFieldBean($srow.fieldName).fieldType==1)
			
			<span class='input' title="$!globals.newFormatNumber($!values.get($srow.asName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)" name="$popUrlValue" #if("$audit" == "true" && "$row.isReAudit" == "1") onclick="reAuditOp('$row.inputType','$row.fieldType','$globals.getTableField($srow.asName)','$!globals.newFormatNumber($!values.get($srow.asName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)',this,'','$row.popupType','$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)')" #end>$!globals.newFormatNumber($!values.get($srow.asName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)</span>
			<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)" value="$!globals.newFormatNumber($!values.get($srow.asName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)" type="hidden"/>
			
			#else
			
			<span class='input'title="$!values.get($srow.asName)" name="$popUrlValue "#if("$audit" == "true" && "$row.isReAudit" == "1")  onclick="reAuditOp('$row.inputType','$row.fieldType','$globals.getTableField($srow.asName)','$!values.get($srow.asName)',this,'','$row.popupType','$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)')" #end>$!values.get($srow.asName)</span>
			<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)" value="$!values.get($srow.asName)" type="hidden"/>
			
			#end
		</li>
		#if("$row.width"=="0")
		<li style="float: none;width: 1px;"></li>
		#end
	#end		
	#set($tableField="")
 #end
#end				
#end				
#end	
#end
#end
#end
#end
</ul>
</div> 
#foreach ($row in $fieldInfos )
#if($globals.getScopeRight($mainTable.tableName,$row.fieldName,$scopeRight))	
#if("$row.inputType" != "100" && "$row.inputType" !="3" && "$row.fieldType" == "3" && "$row.inputType"!="6" && $row.width==-1)
#if($row.maxLength>5000)#set($rows=8)#else#set($rows=5)#end
<div class="listRange_1_photoAndDoc_1"><span>$row.display.get("$globals.getLocale()")：</span>$!values.get($row.fieldName).replaceAll("\r\n","<br/>")
</div>
#end	
#if("$row.inputType" != "100" && "$row.inputType" !="3" && "$row.fieldType" == "16" && "$row.inputType"!="6" && $row.width==-1)
<div class="listRange_1_photoAndDoc_1"><span>$row.display.get("$globals.getLocale()")：</span>
<div style="float: left;">
$!values.get($row.fieldName)
</div>
</div>
#end
#end
#end



#if("$globals.getSysSetting('careforAction')"=="true" && "$!fromCRM"=="detail")
<div style="margin-left:3px;float:left;margin-top:3px; height:200px;padding-bottom:3px;width:99%;border:2px solid #DAEDFE; overflow-x:hidden;overflow-y:auto;">
<iframe src="/CareforExecuteAction.do?operation=4&type=iframe&clientId=$values.get('id')" width="100%" height="200" scrolling="no"  frameborder=false></iframe>
</div>
#end
	 #foreach ($rowlist in $childTableList )
		<input type="hidden" id="${rowlist.tableName}PopupType" name="${rowlist.tableName}PopupType" value="$globals.getTableInfoBean(${rowlist.tableName}).popupType"/> 
	 #end
	 
<div class="showGridTags">	 
	#foreach ($rowlist in $childTableList ) 
#if("$globals.getSysSetting($rowlist.tableSysType)"=="true" && "$rowlist.isUsed"=="1")
#set($gridtable =$rowlist.tableName+"Table")

#if($childTableConfigList.get($rowlist.tableName).size()>0 || $allConfigList.size()==0) 
	 <span show="${gridtable}DIV" class="tags" onclick="changeTags(this);">$rowlist.display.get($globals.getLocale())</span>
	
#end	
#end
 #end	 
</div>	 
<div class="scroll_function_small_ud" style="overflow:auto;">
#foreach ($rowlist in $childTableList ) 
#if("$globals.getSysSetting($rowlist.tableSysType)"=="true" && "$rowlist.isUsed"=="1")
#set($gridtable =$rowlist.tableName+"Table")
<script language="javascript">
var tableId="$gridtable";
</script>
#if($childTableConfigList.get($rowlist.tableName).size()>0 || $allConfigList.size()==0)	
		<div   name="${gridtable}DIV" id="${gridtable}DIV"> 
		<table border="0" cellpadding="0"  cellspacing="0"  class="listRange_list_function_b"  name="$gridtable" id="$gridtable">
		<thead><!-- 
			<tr>
			<td class="listheade" width="35">No.</td> -->
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
								 #set($fieldName=$srow.fieldName)
								 #if("$!srow.asName"!="")
								 #set($fieldName=$!srow.asName)
								 #end	    
								 #if($globals.getScopeRight($rowlist.tableName,$row.fieldName,$scopeRight))    
								 	<!-- <td width="$disWidth">$dis</td> -->
								 	#set($width=$width+$disWidth)
								 	#if($srow.display.indexOf("GoodsCode")>=0)
								 		#set($colInputType=$colInputType+$inputType2+":"+":"+$row.isStat+":GoodsFullName"+":"+$fieldName+",")
					    			#else
								 		#set($colInputType=$colInputType+"$!{inputType2}::$!{row.isStat}:$!{tableField}:$!{row.fieldType}"+":"+$fieldName+",")
								 	#end
								 #else
								 	#set($colInputType=$colInputType+"-2:"+":"+$row.fieldType+":"+$fieldName+",")
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
							  #set($inputValue = "$inputValue;$erow.value@select@$erow.name")
						  #end
						  #set($colInputType=$colInputType+$inputType2+":"+$!defValue+":"+$row.isStat+":"+$inputValue+":"+$row.fieldName+",")
						#set($width=$width+$row.width)
						 <!-- <td width="$row.width">$!row.display.get("$globals.getLocale()")</td>-->
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
								    #set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":Seq,")
									<!-- <td width="$row.width">$!row.display.get("$globals.getLocale()")</td>-->
									#set($width=$width+$row.width)
									#set($colInputType=$colInputType+"-2:"+$!defValue+",")
								#else
									<!-- <td width="$row.width">$!row.display.get("$globals.getLocale()")</td>-->
									#set($width=$width+$row.width)									
									#set($colInputType=$colInputType+$inputType2+":"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":"+$row.fieldName+",")	
					  			#end					  			
						#end
				    #elseif("$row.inputType" == "5" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "5"))
					  	  #foreach($erow in $globals.getEnumerationItems($row.refEnumerationName))
							  #set($inputValue = "$inputValue;$erow.value@select@$erow.name")
						  #end		
						  #set($colInputType=$colInputType+$row.inputTypeOld+":"+$!defValue+":"+$row.isStat+":"+$inputValue+":"+$row.fieldName+",")			  
					      <!-- <td width="$row.width">$!row.display.get("$globals.getLocale()")</td>-->
					      #set($width=$width+$row.width)
					#elseif("$row.inputType" == "4" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "4"))
						#set($fhname=$fname+"_lan")		
						#set($colInputType=$colInputType+$row.inputType2+":"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":"+$row.fieldName+",")	
						<!-- <td width="$row.width">$!row.display.get("$globals.getLocale()")</td>-->
						#set($width=$width+$row.width)
						  #set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":"+$row.fieldName+",")
							
					#elseif("$row.inputType" == "3")			
						#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":"+$row.fieldName+",")
					#else
						#set($colInputType=$colInputType+$inputType2+":"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":"+$row.fieldName+",")
						<!-- <td width="$row.width">$!row.display.get("$globals.getLocale()")</td>	-->
						#set($width=$width+$row.width)
					#end
				#end
			  #else
			  	#set($nullable = "false")
				#if("$row.isNull" == "0")
					 #set($nullable = "true")
				#end
				#set($inputValue = "")	
				#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":"+$row.fieldName+",")
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
					#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":"+$row.fieldName+",")			
				#else
					#set ($isExist="false")
					#foreach ($colConfig in $childTableConfigList.get($rowlist.tableName))
	 	     			#if("$!config_row.fieldName"=="$!colConfig.colName")
	 	     				#set ($isExist="true")
	 	     			#end
	 	     		#end
	 	     		#if("$isExist"=="false")
					#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":"+$row.fieldName+",")	
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
						#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":"+$config_row.fieldName+",")	
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
					#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":"+$config_row.fieldName+",")	
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
		   	 #if((!$!globals.getFlowFieldRight($!designId,$!row.fieldName,$!rowlist.tableName,$!currNodeId,$!values.get("id").toString(),$!values.get("f_brother").toString(),$!LoginBean.id,$!tableName) && "draft"!="$!values.get('workFlowNodeName')") 
		   	 	||!$globals.getScopeRight($rowlist.tableName,$row.fieldName,$scopeRight)||!$flag)
		   		 #set($colInputType=$colInputType+"-2::0::,") 
		   	 	 #if("$row.inputType" == "2")
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
						#set($fieldName=$srow.fieldName)
						 #if("$!srow.asName"!="")
						 #set($fieldName=$!srow.asName)
						 #end
					    #if("$!srow.hiddenInput"!="true")	
					    	#if($tableField.indexOf("GoodsCode")>=0)
					    		#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":GoodsFullName"+":"+$fieldName+",")	
					    	#else
								#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$tableField+":"+$fieldName+",")	
							#end
							#set($width=$width+$disWidth)
					 	#else
							#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldTyp+":"+$fieldName+",")	
					    #end
					    #set($tableField="")
					#end
			     #end
       	   		#end
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
						  #set($inputValue = "$inputValue;$erow.value@select@$erow.name")
					  #end
					 #set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":"+$inputValue+":"+$row.fieldType+":"+$row.fieldName+",")
					 #set($width=$width+$row.width)	
					 <!-- <td width="$row.width">$!row.display.get("$globals.getLocale()")</td>-->
				  #elseif("$row.inputType" == "5")
				  	  #foreach($erow in $globals.getEnumerationItems($row.refEnumerationName))
						  #set($inputValue = "$inputValue;$erow.value@select@$erow.name")
					  #end
					  #set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":"+$inputValue+":"+$row.fieldType+":"+$row.fieldName+",")
					  #set($width=$width+$row.width)
				      <!-- <td width="$row.width">$!row.display.get("$globals.getLocale()")</td>	-->				  
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
							#set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":"+$row.fieldName+",")
							#set($width=$width+$row.width)
							<!-- <td width="$row.width">$!row.display.get("$globals.getLocale()")</td>	-->						
						 #else
							#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":"+$row.fieldName+",")				
						 #end
					#else
						#if("$row.inputType" == "2" and "$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1")
							#set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":Seq,")
							#set($colInputType=$colInputType+"-2:"+$!defValue+",")
							#set($width=$width+$row.width)
							<!-- <td width="$row.width">$!row.display.get("$globals.getLocale()")</td>-->	
						#else
							
							#set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":"+$row.fieldName+",")
							#set($width=$width+$row.width)
							<!-- <td width="$row.width">$!row.display.get("$globals.getLocale()")</td>-->
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
						 #set($fieldName=$srow.fieldName)
						 #if("$!srow.asName"!="")
						 #set($fieldName=$!srow.asName)
						 #end
					    #if("$!srow.hiddenInput"!="true")	
					    	#if($tableField.indexOf("GoodsCode")>=0)
					    		#set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":GoodsFullName"+":"+$fieldName+",")	
					    	#else
								#set($colInputType=$colInputType+"${row.inputType}:$!{defValue}:$!{row.isStat}:$!{tableField}:$!{row.fieldType}"+":"+$fieldName+",")
							#end
							#set($width=$width+$disWidth)
					    	<!-- <td width="$disWidth">$dis</td>	-->  
					 	#else
							#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":"+$fieldName+",")	
					    #end
					    #set($tableField="")
					#end
			     #end
				#elseif("$row.inputType" == "3" || "$row.inputType" == "6")	
					#set($colInputType=$colInputType+"-2:"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":"+$row.fieldName+",")	
				#elseif("$row.inputType" == "4")
					#set($fhname=$fname+"_lan")	
					#set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":"+$row.fieldName+",")	
					#set($width=$width+$row.width)
					<!-- <td width="$row.width">$!row.display.get("$globals.getLocale()")</td>	-->  
					#set($colInputType=$colInputType+"-2:"+$!defValue+",")		
				#else	
					#set($colInputType=$colInputType+$row.inputType+":"+$!defValue+":"+$row.isStat+":"+$row.fieldType+":"+$row.fieldName+",")		
					#set($width=$width+$row.width)
					<!-- <td width="$row.width">$!row.display.get("$globals.getLocale()")</td>	  -->
				#end
			#end
			#end
		#end
    #end	
    	
		<!-- </tr> -->
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
		#set($propVal="")
		#foreach ($col in $colobjs)
			#if($!globals.get($col.split(":"),3).equals("GoodsFullName"))
				#set($GoodsFullName=$globals.get($rowobjs,$globals.subtracter($velocityCount,1)))
			#else
				#set($tempVal="")
				#set($temp=$!globals.get($col.split(":"),3))
				#set($index=$temp.indexOf(".")+1)
				#set($temp=$temp.substring($index))
				#set($count=$velocityCount)
				#set($tempVal=$globals.get($rowobjs,$globals.subtracter($velocityCount,1)))
				#foreach($prop in $PROP_INFOL)			
					#if(!$prop.propName.equals("Seq")&&$prop.isUsed==1&&$!temp.equals($prop.propName))					
						#set($propVal=$propVal+"&"+$prop.propName+"="+$globals.encode($!tempVal))
					#end
				#end
			#end
		#end

		<tr>
		<td class="listheadonerow">$velocityCount</td>	
					
		#foreach ($col in $colobjs)
			#if(!$globals.get($col.split(":"),0).equals("-2"))
			#set($value="&nbsp;")
			#if($globals.get($rowobjs,$globals.subtracter($velocityCount,1)).length()>0)
			#foreach($cValue in $rowobjs)
				#set($strValue = $cValue.split("@koron@"))
				#if($globals.get($strValue,0)==$globals.get($col.split(":"),4)
					|| $globals.get($strValue,0)==$globals.get($col.split(":"),5)
					||$globals.get($strValue,0)==$globals.get($col.split(":"),6)
					||$globals.get($strValue,0)==$globals.get($col.split(":"),7))
				#set($value = $globals.get($strValue,1))
				#else
				#end
			#end
			#elseif(!$globals.get($col.split(":"),1).length()>0)
			#set($value=!$globals.get($col.split(":"),1))
			#else
			#set($value="&nbsp;")
			#end	
			#if($value==-100000)		
				#set($value="&nbsp;")
			#end
			#if($globals.get($col.split(":"),0).equals("1"))
				#set($index=$col.indexOf(";")+1)
				#set($inputVals=$col.substring($index).split(";"))
				#foreach ($inputVal in $inputVals)
					#if($globals.get($inputVal.split("@select@"),0).equals($value))		
						#set($value = $globals.get($inputVal.split("@select@"),1))				
						#set($index2 = $value.indexOf(":"))
						#if($index2>-1)
						#set($value=$value.substring(0,$index2))
						#end
					#end
				#end
			#elseif($globals.get($col.split(":"),0).equals("5"))
				#set($index=$col.indexOf(";")+1)
				#set($inputVals=$col.substring($index).split(";"))
				#set($arrayValues=$value.split(","))
				#set($value="")
				#set($value2="")
				#foreach ($inputVal in $inputVals)
					<!-- $inputVal -->
					#foreach($val in $arrayValues)
						#if($globals.get($inputVal.split("@select@"),0) == $val)
							#set($value2 = $globals.get($inputVal.split("@select@"),1))				
							#set($index2 = $value2.indexOf(":"))
							#if($index2>-1)
							#set($value=$value+$value2.substring(0,$index2)+",")
							#else
							#set($value=$value+$value2+",")
							#end
						#end
					#end	
				#end
				#if($value.length()>0)
					#set($value=$value.substring(0,$globals.subtracter($value.length(),1)))
				#else
					#set($value="&nbsp;")
				#end
			#end
			#if("$!value"=="")
			#set($value="&nbsp;")
			#end
			#if($globals.get($col.split(":"),3).equals("0") || $globals.get($col.split(":"),3).equals("1"))	
			<td align="right">$value</td>
			#elseif($globals.get($col.split(":"),4).equals("Seq"))	
			<td align="left">#if($value!="&nbsp;")<img src='/style/images/St.gif' class='search' onClick="javascript:asyncbox.open({id:'ChildPopup',title:'序列号清单',url:'/UserFunctionAction.do?operation=22&type=stockSequence&seq=$value&childTableName=$tableNamec&page=detail',width:450,height:400});" />#end$value</td>
			#elseif($globals.get($col.split(":"),3).equals("3"))
			<td align="left" style="height: 80px;">$value.replaceAll("\r\n","<br>")</td>
			#else
			<td align="left" title="$!value">$!value</td>
			#end
			#end
		#end
		</tr>
		#set($!count=$!count+1)
		#end
		
		#set($hasCount=false)
		#foreach ($col in $colobjs)
		#if(!$globals.get($col.split(":"),0).equals("-2"))
			#if($globals.get($col.split(":"),2).equals("1"))
			#set($hasCount=true)
			#end
		#end
		#end
		#if($hasCount)
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
			#foreach($rowValue in $rowobjs)
			#if($globals.get($col.split(":"),4)==$globals.get($rowValue.split("@koron@"),0))
			#set($total=$globals.add("$total",$globals.get($rowValue.split("@koron@"),1)))
			#end
			#end
			#end
			<td align="right">$total</td>
			#else
			<td align="right">&nbsp;</td>
			#end
		#end
		#set($count=$count+1)
		#end
		</tr>
		#end
		#end
		#end		
		</tbody>
		</table>
		</div>
		<script language="javascript">
			document.getElementById("$gridtable").width=$width+30
		</script>
		
#end	
#end
 #end
 </div>
 
 
<div><span style="color:#0000FF;line-height:20px">$!mainTable.tableDesc.replaceAll("\r\n","<br/>")</span></div>
#foreach ($row in $fieldInfos )
#if($globals.getScopeRight($mainTable.tableName,$row.fieldName,$scopeRight))	
#if("$row.inputType" != "100" && "$row.inputType" !="3" && "$row.fieldType" == "3" && "$row.inputType"!="6" && $row.width!=-1)
#if($row.maxLength>5000)#set($rows=8)#else#set($rows=5)#end

<div style="width:99%;height:auto;text-align:left;padding:5px;overflow:hidden;">#if("$audit" == "true" && "$row.isReAudit" == "1")<span style="color:red;float: left;" >$row.display.get("$globals.getLocale()")：</span> #else <span>$row.display.get("$globals.getLocale()")：</span> #end
<span #if("$audit" == "true" && "$row.isReAudit" == "1")onclick="reAuditOp('$row.inputType','$row.fieldType','$row.fieldName','$!values.get($row.fieldName).replaceAll("\r\n","<br/>")',this,'','','')" #end>#if($!values.get($row.fieldName).replaceAll("\r\n","<br/>") == "") 无 #else $!values.get($row.fieldName).replaceAll("\r\n","<br/>")  #end</span>
</div>
#end	
#if("$row.inputType" != "100" && "$row.inputType" !="3" && "$row.fieldType" == "16" && "$row.inputType"!="6" && $row.width!=-1)
<div style="width:99%;height:auto;text-align:left;padding:5px;border-bottom:1px dotted #A2CEF5;overflow:hidden;"><span style="float:left;">$row.display.get("$globals.getLocale()")：</span>
<div style="float:left">
$!values.get($row.fieldName)
</div>
</div>
#end	
#if("$row.inputType" != "100" && "$row.inputType" !="3" && "$row.fieldType" == "14" && "$row.inputType"!="6")
		<div class="listRange_1_photoAndDoc_1"><span>$row.display.get("$globals.getLocale()")：</span>
		<ul id="affixuploadul">
#foreach($uprow in $AFFIX)	
#if($uprow.indexOf(":") > -1)			
<li style='background:url();' id="uploadfile_$!globals.get($uprow.split(":"),0)">$!globals.get($uprow.split(":"),1)		
	&nbsp;&nbsp;&nbsp;&nbsp;<a href="/ReadFile?fileName=$globals.urlEncode($!globals.get($uprow.split(":"),0))&realName=$globals.urlEncode($!globals.get($uprow.split(":"),1))&tempFile=false&type=AFFIX&tableName=$tableName" target="_blank">$text.get("common.lb.download")</a>
</li>
#else
<li style='background:url();' id="uploadfile_$uprow">$uprow	
	&nbsp;&nbsp;&nbsp;&nbsp;<a href="/ReadFile?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=AFFIX&tableName=$tableName" target="_blank">$text.get("common.lb.download")</a>
</li>
#end
#end
</ul><br/></div>
#end				
#if("$row.inputType" != "100" && "$row.inputType" !="3" && "$row.fieldType" == "13" && "$row.inputType"!="6")				
<div class="listRange_1_photoAndDoc_1"><span>$row.display.get("$globals.getLocale()")：</span>
<ul id="picuploadul">
#foreach($uprow in $PIC)
#if($uprow.indexOf("http:") > -1)					
<li style='background:url();' id='uploadfile_$uprow'>
<a href="$uprow" target="_blank"><img src="$uprow" width="150"  border="0"></a><div>$uprow&nbsp;&nbsp;#if("$detail" != "detail")<a href='javascript:deleteupload("$uprow","false","$tableName","PIC");'>$text.get("common.lb.del")</a>#end</div>
</li>
#elseif($uprow.indexOf(":") > -1)					
<li style='background:url();' id='uploadfile_$!globals.get($uprow.split(":"),0)'>
<a href="/ReadFile.jpg?fileName=$globals.urlEncode($!globals.get($uprow.split(":"),0))&realName=$globals.urlEncode($!globals.get($uprow.split(":"),1))&tempFile=false&type=PIC&tableName=$tableName" target="_blank"><img src="/ReadFile.jpg?fileName=$globals.urlEncode($!globals.get($uprow.split(":"),0))&realName=$globals.urlEncode($!globals.get($uprow.split(":"),1))&tempFile=false&type=PIC&tableName=$tableName" width="150" border="0"></a><div>$!globals.get($uprow.split(":"),1)&nbsp;&nbsp;#if("$detail" != "detail")<a href='javascript:deleteupload("$!globals.get($uprow.split(":"),0)","false","$tableName","PIC");'>$text.get("common.lb.del")</a>#end</div>
</li>
#else
<li style='background:url();' id='uploadfile_$uprow'>
<a href="/ReadFile.jpg?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=PIC&tableName=$tableName" target="_blank"><img src="/ReadFile.jpg?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=PIC&tableName=$tableName" width="150"  border="0"></a><div>$uprow&nbsp;&nbsp;#if("$detail" != "detail")<a href='javascript:deleteupload("$uprow","false","$tableName","PIC");'>$text.get("common.lb.del")</a>#end</div>
</li>
#end
#end
</ul></div>
#end	
#end
#end


#if($!OAWorkFlow)
#if("$!flowNew"=="true")
<div class="">
	#if($!delivers.size()>0)
	<div class="view-ul-wp">
		<p class="t-p">审核记录<span class="hBtns" onClick="addAffix()">补充意见</span></p>
		<ul class="view-ul">
			#foreach($!deliver in $!delivers)
			<li class="view-li">
				<div class="d-pa">
					<em class="node-em">第$!velocityCount步&nbsp;&nbsp;$!deliver.nodeID</em>
					<span class="check-person">$!deliver.checkPerson</span>
				</div>
				<div class="d-dbk">
					<em class="end-time">[$!deliver.endTime]</em>
					<div class="app-work-check">
						&nbsp;$!deliver.approvalOpinions<br>
						#foreach($affix in $globals.strSplitByFlag($deliver.affix,';'))
							#if("$!affix"!="")
							$affix <a href="/ReadFile?fileName=$globals.urlEncode($affix)&realName=$globals.urlEncode($affix)&tempFile=false&type=AFFIX&tableName=$!tableInfo.tableName&down=true" target="_blank">$text.get("common.lb.download")</a>
							#end
						#end
					</div>
					<em class="node-type">
						$!deliver.nodeType #if("$!deliver.workFlowNode"!="")-> [$!deliver.workFlowNode]-$!deliver.checkPersons #end
					</em>
				</div>
			</li>
			#end
		</ul>
		<p style="margin-top:20px;"><span class="hBtns" onClick="addAffix()">补充意见</span></p>
	</div>
 	#end
#else
<div class="listRange_1_photoAndDoc_1">
<b>$text.get("common.sign.advice")</b>
<br><br/>
#foreach($!deliver in $!delivers)
<li style="width:95%;">$text.get("oa.workFlow.step")：$!workFlowDesignBeans.get($!designId).getFlowNodeMap().get($!deliver.nodeId).display&nbsp;&nbsp;&nbsp;$text.get("common.sign.human")：$!deliver.empFullName&nbsp;&nbsp;&nbsp;$text.get("common.sign.time")：$!deliver.attTime</li>
<li style="width:95%;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$!deliver.deliverance.replaceAll("\r\n","<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")</li>
<br><br/>
#end
</div>
<div class="listRange_1_photoAndDoc_1">
<b>$text.get("oa.flow.affix.down")</b>&nbsp;&nbsp;<button type=button class="btn btn-mini" style="font-family:microsoft yahei;" onClick="addAffix()">$text.get("com.flow.add.affix")</button>
<div id="files_preview" style="margin-left:1px; background: none;"></div>
<br><br/>
#foreach($!deliver in $!affixs)
<li style="width:95%;margin-top: 10px;">
		$text.get("oa.add.affix.person")：$!deliver.empFullName&nbsp;&nbsp;&nbsp;$text.get("oa.flow.upload.time")：$!deliver.attTime</li>&nbsp;&nbsp;&nbsp;
		$text.get("oa.flow.affix.name")：



		#foreach($affix in $globals.strSplitByFlag($deliver.affix,';'))
		$affix <a href="/ReadFile?fileName=$globals.urlEncode($affix)&realName=$globals.urlEncode($affix)&tempFile=false&type=AFFIX&tableName=$tableName" target="_blank">$text.get("common.lb.download")</a>
		#end
#end
#end
<br><br/>
<input type="hidden" id="attachFiles" name="attachFiles" value=""/>
<input type="hidden" id="delFiles" name="delFiles" value=""/>
</div>
<script type="text/javascript">
//新添加会签意见弹出框
function addAffix(){
	asyncbox.open({id:'dealdiv',url:'/OAMyWorkFlow.do?keyId=$!values.get("id")&tableName=$!tableName&operation=6',
	 	title:'$text.get("com.flow.add.affix")',width:650,height:370,btnsbar:jQuery.btn.OKCANCEL,
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
	location.href='/UserFunctionAction.do?tableName=$!tableName&keyId=$!values.get("id")&moduleType=$!moduleType&f_brother=$!f_brother&operation=$globals.getOP("OP_DETAIL")&winCurIndex=$!winCurIndex&pageNo=$!pageNo&parentCode=$!parentCode&parentTableName=$!parentTableName&saveDraft=$!saveDraft&noback=$!noback&OAFlowQueryType=all';
}
</script>
#end
</form>
<script type="text/javascript">
cyh.lableAlign(); 
</script> 
</body>
</html>
