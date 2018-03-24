<!DOCTYPE html >
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("tableInfo.title.update")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script type="text/javascript" src="$globals.js("/js/editPage.vjs","",$text)"></script>
<script type="text/javascript" src="/js/validate.vjs"></script>
<script type="text/javascript" src="/js/editGrid.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script> 
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<script type="text/javascript">
var layoutHTML  ;
KindEditor.ready(function(K) {
	layoutHTML = K.create('textarea[name="layoutHTML"]',{
		uploadJson:'/UtilServlet?operation=uploadFile'
	});
});


var editgriddata = new gridData();
var ftype="";
 #foreach ($row in $globals.getDS("fieldType"))
           ftype+="$row.value:$row.name;";  
 #end
 
var intype="";
 #foreach ($row in $globals.getDS("inputType"))
           intype+="$row.value:$row.name;";  
 #end

var isNull="";
#foreach ($row in $globals.getDS("nullType"))
    isNull+="$row.value:$row.name;"; 
#end

var fieldSysType="";
#foreach($row in $globals.getEnumerationItems("FieldSysType"))
	fieldSysType+="$row.value:$row.name;";	
#end
	
var fieldIdentityStr="";
#foreach ($row in $globals.getEnumerationItems("fieldIdentityStr"))
    fieldIdentityStr+="$row.value:$row.name;";  
#end

var copyType = "";
#foreach ($row in $globals.getEnumerationItems("copyType"))
copyType += "$row.value:$row.name;";  
#end

var popupType="" ;
#foreach($row in $globals.getEnumerationItems("popupSysType"))
	popupType+="$row.value:$row.name;";	
#end
	 
var isUnique="0:$text.get("common.lb.no");1:$text.get("common.lb.yes")";
var isStat="0:$text.get("common.lb.no");1:$text.get("common.lb.yes")";

function mainmd(){
	var mlf = document.form.tableDisplay;
	var url = "/common/moreLanguage.jsp?popupWin=MainLanguage&len=50&str="+mlf.value;
	asyncbox.open({id:'MainLanguage',title:'多语言',url:url,width:530,height:300}); 	   
}

function fillMainLanguage(str) {
	var mlf = document.form.tableDisplay;
	if(typeof(str)!="undefined"){
		mlf.value = str;
	}
}

var moreLanguagefield;
function moreLanguage(len,moreLanguagefield){
	window.moreLanguagefield = moreLanguagefield;
	var url = "/common/moreLanguage.jsp?popupWin=Language&len="+len+"&str="+encodeURI(moreLanguagefield.value);
	asyncbox.open({id:'Language',title:'多语言',url:url,width:530,height:300}); 
}

function fillLanguage(str){
	if(typeof(str)!="undefined"){
	  	 moreLanguagefield.value = str;
	}
}

function moreInputClick(obj){
   if(event.keyCode == 13){
   		obj.testi = "testi";	
		var id = 0;	
   		var mi = document.getElementsByName('moreInput'); 
   		for(i=0;i<mi.length;i++){
			if(mi[i].testi == "testi"){
				id = i;
				obj.testi = "";	
			}	
		}
		var ti = document.getElementsByName('inputType'); 
		var type = ti[id].value;
		
		if(type == 1){
			var str  = window.showModalDialog("/vm/tableInfo/enumeration.jsp","","dialogWidth=730px;dialogHeight=450px");
	 		obj.value = str;	
		}
		/*
		if(type == 2){
		var str  = window.showModalDialog("/vm/tableInfo/mainTableSelect.jsp?type=2","","dialogWidth=730px;dialogHeight=450px");
	 		obj.value = str;	
		} */
   }
}

var editValue = false ;
var va = 'moredisClick(this)';
var vb = 'moreInputClick(this)';
addCols(editgriddata,'fieldId','',130,2,true,'',500,-2,'');
addCols(editgriddata,'fieldName','$text.get("customTable.lb.fieldName")',300,2,true,'',500,8,'');
addCols(editgriddata,'fieldDisplay','$text.get("customTable.lb.moreLan")',300,2,true,'',500,4,'');
addCols(editgriddata,'isNull','$text.get("customTable.lb.isNULL")',60,0,false,'0',10,1,isNull);
addCols(editgriddata,'isUnique','$text.get("customTable.lb.unique")',40,0,false,'0',10,1,isUnique);
addCols(editgriddata,'isStat','$text.get("customTable.lb.stat")',40,0,false,'0',10,1,isStat);
addCols(editgriddata,'maxLength','$text.get("customTable.lb.maxLength")',50,0,false,'',10,0,'');
addCols(editgriddata,'defaultValue','$text.get("customTable.lb.defaultValue")',80,2,false,'',50,0,'');
addCols(editgriddata,'listOrder','$text.get("customTable.lb.listOrder")',30,0,false,'',10,0,'');
addCols(editgriddata,'fieldType','$text.get("customTable.lb.fieldType")',80,0,false,'2',10,1,ftype);
addCols(editgriddata,'digits','$text.get("customTable.lb.digits")',30,0,false,'0',10,0,'');
addCols(editgriddata,'inputType','$text.get("customTable.lb.inputType")',80,0,false,'0',10,1,intype);
addCols(editgriddata,'width','$text.get("customTable.lb.inputSizeW")',50,0,false,'224',10,0,'');
addCols(editgriddata,'moreInput','$text.get("customTable.lb.inputValue")',250,2,true,'',5000,-1,vb);
addCols(editgriddata,'calculate','$text.get("customTable.lb.caculator")',150,2,true,'',8000,-0,'');
addCols(editgriddata,'fieldSysType','$text.get("customTable.lb.fieldSysType")',150,0,false,'',10,1,fieldSysType);
addCols(editgriddata,'fieldIdentityStr','$text.get("customTable.lb.fieldIdentityStr")',130,0,false,'',10,1,fieldIdentityStr);
addCols(editgriddata,'copyType','$text.get("com.field.copy.type")',150,0,false,'',10,1,copyType);
addCols(editgriddata,'logicValidate','$text.get("customTable.lb.logicValidate")',150,2,true,'',3000,-0,'');
addCols(editgriddata,'popupType','$text.get("com.poput.type")',150,0,false,'',10,1,popupType)
addCols(editgriddata,'groupName','$text.get("customTable.lb.groupName")',130,2,true,'',500,4,'');
addCols(editgriddata,'insertTable','$text.get("tableInfo.lb.autoInsertTable")',100,2,true,'',100,-0,'');
addCols(editgriddata,'isCopy','$text.get("common.field.bill.copy")',60,0,false,'1',10,1,isStat);
addCols(editgriddata,'isReAudit','$text.get("ReportBillView.button.Check")',60,0,false,'0',10,1,isStat);
#if($result.tableType == 1)
addCols(editgriddata,'isLog','记录日志',60,0,false,'0',10,-2,isStat);
#else
addCols(editgriddata,'isLog','记录日志',60,0,false,'0',10,1,isStat);
#end

#foreach ($row in $result.fieldInfos)
    #set($iv="")
	#set($selectMyd="")
	#if("$row.inputType" == "1" || ("$row.inputType" == "6" && "$row.inputTypeOld"=="1"))
	  #set($iv="$!row.refEnumerationName")
	#elseif("$row.inputType" == "5"  || ("$row.inputType" == "6" && "$row.inputTypeOld"=="5"))
	  #set($iv="$!row.refEnumerationName")
	#elseif("$row.inputType" == "16"  || ("$row.inputType" == "6" && "$row.inputTypeOld"=="16"))
	  #set($iv="$!row.inputValue")  
	#elseif("$row.inputType" == "10"  || ("$row.inputType" == "6" && "$row.inputTypeOld"=="10"))
	  #set($iv="$!row.refEnumerationName")
	#elseif("$row.inputType" == "2"  || "$row.inputType" == "6" || "$row.inputType" == "11")
	  #set($iv="$!row.inputValue")
	#end 
	
	#set($iv = $iv.replaceAll("'", "&apos;"))
	
	
	#if($row.fieldName != "id" && $row.fieldName != "f_ref" && $row.fieldName != "f_brother")
		#if("$row.fieldName"=="checkPersons" || "$row.fieldName"=="classCode" || "$row.fieldName"=="workFlowNode"
	 		|| "$row.fieldName"=="workFlowNodeName" || "$row.fieldName"=="createBy" || "$row.fieldName"=="lastUpdateTime"
	 		|| "$row.fieldName"=="SCompanyID" || "$row.fieldName"=="statusId" || "$row.fieldName"=="lastUpdateBy"
	 		|| "$row.fieldName"=="createTime" || "$row.fieldName"=="isCatalog")
		#else
			addRows(editgriddata,'$row.id','$row.fieldName','$!row.display','$row.isNull','$row.isUnique','$row.isStat','$row.maxLength','$!row.defaultValue','$row.listOrder','$row.fieldType','$!row.digits','$!row.inputType','$row.width','$iv','$!row.calculate','$!row.fieldSysType','$!row.fieldIdentityStr','$!row.copyType','$!row.logicValidate','$!row.popupType','$!row.groupDisplay','$!row.insertTable','$!row.isCopy','$!row.isReAudit','$!row.isLog');
		#end
	#end
#end

function changeMC(theit){
     if(theit.value == 0){
	    perantDiv.style.display='none';
	    isNullDiv.style.display='none';
		rowCount.style.display='none';
		isReAudit.style.display='block';
	 }else{
	    perantDiv.style.display='block';
	    isNullDiv.style.display='block';
		rowCount.style.display='block';
		isReAudit.style.display='none';
	 }
}
function changeTabInfo(theit){
     if(theit.value == 0){
	    oppoDiv.style.display='none';
	 }else{
	    oppoDiv.style.display='block';
	 }
}
function changeClass(theit){
     if(theit.value == 0){
	    classCountDiv.style.display='none';
	 }else{
	    classCountDiv.style.display='block';
	 }
}
putValidateItem("tableDisplay","$text.get("customTable.lb.moreLan")","any","",false,-99999999999999999999,50);	
function m(obj){
	return document.getElementById(obj);
}
function beforSubmit(form){ 
	if(m("tableDisplay").value == "" || m("tableDisplay").value == "en:;zh_CN:;zh_TW:;"){
		alert("$text.get("workflow.msg.NoLanguage")");
		return false;
	} 
	
	var existField = false ;
	var fieldNames = document.getElementsByName("fieldName");
	var fieldDisplays = document.getElementsByName("fieldDisplay");
	var maxLengths = document.getElementsByName("maxLength");
	for(var field=0; field<fieldNames.length; field++){
		if(fieldNames[field].value.length>0){
			existField = true ;
		}
		if(fieldNames[field].value==""&&fieldDisplays[field].value!=""){
			alert("$text.get("workflow.msg.NoFieldName")");
			return false;
		}
		if(maxLengths[field].value=="" && fieldNames[field].value!=""){
			alert("$text.get("table.no.input.maxLength")");
			return false;
		}
	}
	if(!existField){
		alert("$text.get('table.field.not.null')") ;
		return false ;
	}
	/*
	if(confirm("$text.get("tblSysDeploy.update.Alert")")){
		document.getElementById("restart").value = "1";
	} */
	if(!validate(form))return false;
	disableForm(form);
	form.parentTableName.value="";
	var op = form.perantTableName.options;
	for(i=0;i<op.length;i++){
		form.parentTableName.value = form.parentTableName.value + op[i].value +";"
		
	}
	//var inputTypeItems=document.getElementsByName("inputType");
	//var widthItems=document.getElementsByName("width");
	//for(i=0;i<inputTypeItems.length;i++){
	//   if(inputTypeItems[i].value!="100"&&inputTypeItems[i].value!="3"){
	//      if(!Number(widthItems[i].value)>0){
	//	    alert("$text.get('tableinfo.fieldwidth.error')");
	//		widthItems[i].focus();return false;
	//	  }
	//   }
	//}
	form.layoutHTML.value = layoutHTML.html() ;
	window.save = true;
	document.form.submit();
	return true;
}	
function copySave()
{
	form.operation.value=$globals.getOP("OP_COPY")
    if(beforSubmit(form))return true;
}
function addMainTable(urlStr){
	urlStr += "&popupWin=TablePopup";
	asyncbox.open({id:'TablePopup',title:'对应主表',url:urlStr,width:730,height:470});
}

function exeTablePopup(str){
	if(str == undefined){
		return;
	}
	fs=str.split(";");   
	//保存字段
	for(var i=0;i<document.form.perantTableName.options.length;i++) {
		if(document.form.perantTableName.options[i].value == fs[0]){
			return;
		}
    }
	
	var oOption = document.createElement("option");
	oOption.appendChild(document.createTextNode(fs[1]));	
	oOption.setAttribute("value", fs[0]);
	document.form.perantTableName.appendChild(oOption);
}

function addRelationTable(urlStr){
var str  = window.showModalDialog(urlStr,"","dialogWidth=730px;dialogHeight=450px");
	if(str == undefined){
		return;
	}
	fs=str.split(";");   
	//保存字段 
	form.relationTable.value=fs[0]
	form.relationTableDis.value=fs[1];
}
function delMainTable(){
	cur = document.form.perantTableName.selectedIndex;	
	if(cur<0){
		return;
	}
	document.form.perantTableName.remove(cur);
}

function fieldValidate(){
	if(layoutHTML.html().length==0){
		alert("$text.get("com.table.html.null")") ;
		return ;
	}
	var width = screen.availWidth;
	var height = screen.availHeight;
	var returnValue = window.showModalDialog("/vm/tableInfo/HTMLValidateField.jsp?tableName=$!result.tableName",window,"dialogWidth="+width+"px;dialogHeight="+height+"px,scroll=no;")
	if(typeof(returnValue)!="undefined"){
		layoutHTML.html(returnValue);
	}
}


function keyDown(e) { 
	var iekey=event.keyCode; 
	if(iekey==8){
		if(!(event.srcElement.tagName=="INPUT" && event.srcElement.readOnly == false)){
			return false;
		}
	}
}
document.onkeydown = keyDown; 

</script>
<style type="text/css">
input[disabled],
textarea[disabled],
input[readonly],
textarea[readonly]
{
	cursor:text;
  	background-color: #fff;
}
</style>
</head>

<body onLoad="showtable('edittable'); showStatus(); initTableList(edittableDIV,edittable,editgriddata); " >

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/CustomAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="search" value="$!search">
<input type="hidden" name="parentTableName">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
 <input type="hidden" name="restart" value="0">
 
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("tableInfo.title.update")</div>
	<ul class="HeadingButton">
	#if($!operation==$globals.getOP("OP_COPY_PREPARE"))
		<li><button type="button" onClick="copySave()" class="b2">$text.get("common.lb.copySave")</button></li>
		#else		
		<li><button type="button" onClick="beforSubmit(document.form);" class="b2">$text.get("common.lb.update")</button></li>
		#end	
		#if($globals.getMOperation().query())
		<li><button type="button" onClick="location.href='/ReportSetQueryAction.do?reportNumber=$!result.tableName&type=table&winCurIndex=$!winCurIndex'" class="b4">$text.get("tableInfo.lb.view")</button></li>	
		<li><button type="button" onClick="location.href='/CustomQueryAction.do?operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex&search=$!search&LinkType=@URL:'" class="b2">$text.get("common.lb.back")</button></li>
		#end
		
	</ul>
</div>
<div id="listRange_id" align="center">
		<script type="text/javascript">
			$("#listRange_id").height($(window).height()-37);
		</script>
		<div class="listRange_1">	
			<li><span>$text.get("customTable.lb.tableName")：</span><input  type="text" id="tableName" name="tableName" value="$!result.tableName"></li>
			<li><span>$text.get("customTable.lb.moreLan")：</span><input  type="text" id="tableDisplay" name="tableDisplay" value="$!result.display" onKeyPress="if(event.keyCode == 13){  mainmd();  }"><img src="/$globals.getStylePath()/images/St.gif" onClick="mainmd();"></li>
			<li id="rowCount" style="display:#if($result.tableType == 0) none #else display #end">
			<span>
				$text.get("customTable.lb.childRowCount")：	</span>		
				<input type="text" name="defRowCount" value="$!result.defRowCount">
				 </li>
				 <li><span>$text.get("customTable.lb.isSunCmpShare")：</span>
			<select name="isSunCmpShare" id="isSunCmpShare">
			<option value="0" #if("$!isshare" =="0") selected #end>$text.get("common.lb.no")</option>
			<option value="1" #if("$!isshare" =="1") selected #end>$text.get("common.lb.yes")</option>			
			</select></li>				 
			<li><span>$text.get("customTable.lb.isBaseInfo")：</span>
			<select name="isBaseInfo" id="isBaseInfo">
				<option value="0" #if("$isBaseInfo" != "1") selected #end>$text.get("common.lb.no")</option> 
			    <option value="1" #if("$isBaseInfo" =="1") selected #end>$text.get("common.lb.yes")</option> 
			</select></li>
				<li><span>$text.get("customTable.lb.needsCopy")：</span>
			<select name="needsCopy" id="needsCopy">
				<option value="0" #if("$needsCopy" != "1") selected #end>$text.get("common.lb.no")</option> 
			    <option value="1" #if("$needsCopy"=="1") selected #end>$text.get("common.lb.yes")</option> 
			</select></li>	 
			<li><span>$text.get("customTable.lb.wakeUp")：</span>
			<select name="wakeUp" id="wakeUp">
				<option value="0" #if("$!result.wakeUp"=="0") selected #end>$text.get("common.lb.no")</option> 
			    <option value="1" #if("$!result.wakeUp"=="1") selected #end>$text.get("common.lb.yes")</option> 
			</select></li>	 
			<li><span>$text.get("com.db.tableInfo")：</span>
			<select name="hasNext" id="hasNext">
				<option value="0" #if("$!result.hasNext"=="0") selected #end>$text.get("common.lb.no")</option> 
			    <option value="1" #if("$!result.hasNext"=="1") selected #end>$text.get("common.lb.yes")</option> 
			</select></li>	
			<li><span>$text.get("com.db.view")：</span>
			<select name="isView" id="isView" onChange="changeTabInfo(this)">
				<option value="0" #if("$!result.isView"=="0") selected #end>$text.get("common.lb.no")</option> 
			    <option value="1" #if("$!result.isView"=="1") selected #end>$text.get("common.lb.yes")</option> 
			</select></li>		 
			<li id="oppoDiv"  style="display:none"  onChange="changeTabInfo(this)">
			<span >
				$text.get("customTable.lb.relation")：	</span>		
				<input type="hidden" name="relationTable" value="$!result.relationTable">
				<input type="text" name="relationTableDis" value="$globals.getTableDisplayName($!result.relationTable)"
				 onDblClick="addRelationTable('/UserFunctionAction.do?tableName=tblUser&selectName=MainTableInfo&operation=22&MOID=20&MOOP=add');">			
			</li>
			<li><span>$text.get("customTable.lb.classFlag")：</span><select name="classFlag">
					<option value="0" #if("$!result.classFlag"=="0") selected #end>$text.get("common.lb.no")</option>
					<option value="1" #if("$!result.classFlag"=="1") selected #end>$text.get("common.lb.yes")</option>
				</select>
			</li>
			<li id="classCountDiv"  #if("$!result.classFlag"=="0") style="display:none" #else style="display:block" #end>
				<span>
				$text.get("customTable.lb.classCount")：	</span>		
				<input type="text" name="classCount" value="$!result.classCount">
			</li>
			<li><span>$text.get("customTable.lb.draftFlag")：</span><select name="draftFlag">
					<option value="0" #if("$!result.draftFlag"=="0") selected #end>$text.get("common.lb.no")</option>
					<option value="1" #if("$!result.draftFlag" =="1") selected #end>$text.get("common.lb.yes")</option>
				</select>
			</li>
			<li><span>$text.get("tableinfo.sysparamecontrol")：</span>
					<select name="sysParameter" style="width:95px" onmouseup="this.style.width='auto'" onBlur="this.style.width='95px'" onChange="this.style.width = '95px'">
					#foreach($erow in $globals.getEnumerationItems("SysParamControl"))
						<option value="$erow.value" #if($erow.value=="$!result.sysParameter") selected="selected" #end>$erow.name</option>
					#end
					</select>
			</li>				
			<li><span>$text.get("customTable.lb.fieldSysType")：</span>
					<select name="tableSysType" style="width:95px" onmouseover="this.style.width='110px'" onBlur="this.style.width='95px'" onChange="this.style.width = '95px'">
					#foreach($erow in $globals.getEnumerationItems("FieldSysType"))
						<option value="$erow.value" #if($erow.value=="$!result.tableSysType") selected="selected" #end>$erow.name</option>
					#end
					</select>
			</li>	
			<li><span>$text.get("customTable.lb.approveFlow")：</span><input  type="text"  name="approveFlow" value="$!result.approveFlow"></li>		
			<li><span>$text.get("customTable.lb.triggerExpress")：</span>
				<select name="triggerExpress" id="triggerExpress">
					<option #if("$!result.triggerExpress"=="1") selected #end value="1">$text.get("common.lb.yes")</option> 
					<option #if("$!result.triggerExpress"=="0") selected #end value="0">$text.get("common.lb.no")</option> 			    
				</select>
			</li>
			<li><span>$text.get("com.tableInfo.layout")：</span>
				<select name="isLayout" id="isLayout">
					<option #if("$!result.isLayout"=="1") selected #end value="1">$text.get("common.lb.yes")</option> 
					<option #if("$!result.isLayout"=="0") selected #end value="0">$text.get("common.lb.no")</option> 			    
				</select>
			</li>
			<li id="isReAudit" style="display:#if($result.tableType != 0) none #else display #end"><span>$text.get("ReportBillView.button.SupportCheck")</span>
				<select name="reAudit" id="reAudit">
					<option #if("$!result.reAudit"=="1") selected #end value="1">$text.get("common.lb.yes")</option> 
					<option #if("$!result.reAudit"=="0") selected #end value="0">$text.get("common.lb.no")</option> 			    
				</select>
			</li>
			<li><span>$text.get("excel.list.isUsed")：</span> 
				<select name="isUsed" id="isUsed">
					<option #if("$!result.isUsed"=="0") selected #end value="0">$text.get("common.lb.no")</option> 
					<option #if("$!result.isUsed"=="1") selected #end value="1" >$text.get("common.lb.yes")</option> 
				</select>
			</li>
			<li><span>表单宽度：</span>
				<input  type="text" name="tWidth" id="tWidth" value="$!result.tWidth">
			</li>
			<li><span>表单高度：</span>
				<input  type="text" name="tHeight" id="tHeight" value="$!result.tHeight">
			</li>
		</div>
	<div style="display:block" class="listRange_1">
		<li style="display:none"><span>$text.get("customTable.lb.tableType")：</span>
			<select name="tableType" id="tableType"  onChange="changeMC(this);">
			#foreach ($row in $globals.getDS("tableType"))
			#if($row.value == $result.tableType)
				<option value="$row.value" selected>$row.name</option> 
			#else
				<option value="$row.value">$row.name</option> 
			#end
			#end
			</select></li>
			<li id="isNullDiv" style="display:#if($result.tableType == 0) none #else display #end;">	
				<span>$text.get("customTable.lb.isNULL")：</span>
				<select name="isNULL" id="isNULL">
				#foreach ($row in $globals.getDS("nullType"))
				<option value="$row.value" #if($!result.isNull==$row.value) selected #end>$row.name</option>  
				#end
				</select>
			</li>
		<!--  对应主表 -->
					<li id="perantDiv"  style="width:400px;display:#if($result.tableType == 0) none #else display #end;height:auto;">
					<!--  当为部门范围时显示这一个块 -->
						<span>$text.get("customTable.lb.inputMain")：</span>
						<select name="perantTableName" size="3" multiple>
						#foreach($row in $parentTableNames)
					    <option value="$globals.get($row,0)">$globals.get($row,1)</option>
					    #end
						</select>
						&nbsp;
						<button type="button" name="dsa" onClick="addMainTable('/UserFunctionAction.do?tableName=tblUser&selectName=MainTableInfo&operation=22&MOID=20&MOOP=update');" class="b2">$text.get("common.lb.add")</button>
						<button type="button" name="dsd" onClick="delMainTable();" class="b2">$text.get("common.lb.del")</button>
					</li>
			<li><span>$text.get("com.report.module.type")：</span>
			<select name="MainModule" id="MainModule">				
				#foreach($erow in $globals.getEnumerationItems("MainModule"))
					<option value="$erow.value" #if($erow.value==$!result.MainModule) selected #end >$erow.name</option>
				#end
			</select>
		</li>
				</div>
		<div class="scroll_function_small_two">
			<div  name="edittableDIV" id="edittableDIV"> 
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function_b" name="edittable" id="edittable">
			</table>
			</div>
		</div>
	  <div class="listRange_1_photoAndDoc_1"><span>$text.get("customTable.lb.dbOperation")：</span><textarea name="fieldCalculate" rows="4">$!result.fieldCalculate</textarea>
	  </div>
	   <div class="listRange_1_photoAndDoc_1">
          <span>$text.get("customTable.lb.extendButton")：</span>
          <textarea name="extendButton" rows="4">$!result.extendButton</textarea>
	</div>
	 <div class="listRange_1_photoAndDoc_1"><span>$tdata_list_idTabext.get("oa.common.remark")：</span><textarea name="tableDesc" rows="4">$!result.tableDesc</textarea>
		 </div>
		<span style="float: left;margin-left: 15px;">$text.get("com.tableInfo.layout.html")：<button onclick="fieldValidate()">$text.get("com.table.field.validate")</button></span>
		<div class="listRange_1_photoAndDoc_2">
			<textarea name="layoutHTML" style="width:99%;height:400px;visibility:hidden;">$!globals.encodeHTML2($!result.layoutHTML)</textarea>
		 </div>
</div>
</form>
</body>
</html>
