<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.workflow.title.tempFileSet")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script type="text/javascript" src="/js/editGrid.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script language="javascript">
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

var fieldSysType=":;";
#foreach($row in $globals.getEnumerationItems("FieldSysType"))
	fieldSysType+="$row.value:$row.name;";	
#end
	
var fieldIdentityStr=":;";
#foreach ($row in $globals.getEnumerationItems("fieldIdentityStr"))
    fieldIdentityStr+="$row.value:$row.name;";  
#end

var popupType=":;" ;
#foreach($row in $globals.getEnumerationItems("popupSysType"))
	popupType+="$row.value:$row.name;";	
#end
	 
var isUnique="0:$text.get("common.lb.no");1:$text.get("common.lb.yes")";
var isStat="0:$text.get("common.lb.no");1:$text.get("common.lb.yes")";

function mainmd(){
   	var mlf = document.form.tableDisplay;
	  var str  = window.showModalDialog("/common/moreLanguage.jsp?len=50&str="+mlf.value,"","dialogWidth=730px;dialogHeight=450px");
		mlf.value = str;
		   
}
function moreLanguage(len,moreLanguagefield){
	 var str  = window.showModalDialog("/common/moreLanguage.jsp?len="+len+"&str="+moreLanguagefield.value,"","dialogWidth=530px;dialogHeight=300px");
	 if(typeof(str)!="undefined"){
	  	 moreLanguagefield.value = str;
	 }
}

function selectEnumeration(len,EnumerationName){
		var dispalyName = encodeURIComponent("$text.get('workFlow.lb.selectexmp')") ;
	  	var str  = window.showModalDialog('/UserFunctionAction.do?operation=22&selectName=selectPopupSelect&MOID=20&MOOP=add&LinkType=@URL:&displayName='+dispalyName,"","dialogWidth=730px;dialogHeight=450px");
		var strName="", strdisplay="",fieldNameTem="";
		var len=0,s=0;
		
		if(typeof(str)=="undefined"){return} ;
		
		var s = str.indexOf(";");
		var s1 = str.indexOf(";", s+1);
		len = str.length;		
		if(len > s){
			strdisplay = str.substring(0, s);
			strName = str.substring(s+1,s1);
			fieldNameTem = str.substring(s1+1,len-1);
		}
		if(strdisplay=="SelectEmployeeTitleDepart"){
			EnumerationName.value = strName;
			document.getElementsByName('moreInput')[EnumerationName.parentNode.parentNode.rowIndex-1].value = strdisplay;
		    fs = fieldNameTem.split(",");
		    for(var i=0;i<fs.length;i++){
				var value=fs[i];
				if(value !="" ){
					document.getElementsByName('fieldName')[EnumerationName.parentNode.parentNode.rowIndex-1+i].value = value;
					document.getElementsByName('fieldName')[EnumerationName.parentNode.parentNode.rowIndex-1+i].readOnly = true ;
					document.getElementsByName('fieldId')[EnumerationName.parentNode.parentNode.rowIndex-1+i].value="";
					document.getElementsByName('moreInput')[EnumerationName.parentNode.parentNode.rowIndex+i].click();
				}
			}
		}else{
			EnumerationName.value = strName;
			document.getElementsByName('moreInput')[EnumerationName.parentNode.parentNode.rowIndex-1].value = strdisplay;
			document.getElementsByName('fieldName')[EnumerationName.parentNode.parentNode.rowIndex-1].value = fieldNameTem;
			document.getElementsByName('fieldName')[EnumerationName.parentNode.parentNode.rowIndex-1].readOnly = true ;
			document.getElementsByName('fieldId')[EnumerationName.parentNode.parentNode.rowIndex-1].value="";
		}
}


function lostfocus(EnumerationValue){
	
}

function setMoreInput(str){
	
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
addCols(editgriddata,'fieldName','$text.get("common.lb.ziduanbiaoshi")',110,2,true,'',500,8,'');
addCols(editgriddata,'fieldDisplay','$text.get("common.lb.ziduanmingcheng")',130,2,true,'',500,4,'');
addCols(editgriddata,'isNull','$text.get("customTable.lb.isNULL")',60,0,false,'0',10,1,isNull);
addCols(editgriddata,'isUnique','$text.get("customTable.lb.unique")',40,0,false,'0',10,1,isUnique);
addCols(editgriddata,'isStat','$text.get("customTable.lb.stat")',40,0,false,'0',10,-2,isStat);
addCols(editgriddata,'maxLength','$text.get("customTable.lb.maxLength")',50,0,false,'200',10,0,'');
addCols(editgriddata,'defaultValue','$text.get("customTable.lb.defaultValue")',80,2,false,'',50,0,'');
addCols(editgriddata,'listOrder','$text.get("customTable.lb.listOrder")',30,0,false,'',10,0,'');
addCols(editgriddata,'fieldType','$text.get("customTable.lb.fieldType")',80,0,false,'2',10,1,ftype);
addCols(editgriddata,'digits','$text.get("customTable.lb.digits")',40,0,false,'0',10,0,'');
addCols(editgriddata,'inputType','$text.get("customTable.lb.inputType")',80,0,false,'0',10,1,intype);
addCols(editgriddata,'width','$text.get("customTable.lb.inputSizeW")',50,0,false,'200',10,0,'');
addCols(editgriddata,'moreInput','$text.get("common.lb.meijubiaoshi")',120,2,true,'',5000,0,'');
addCols(editgriddata,'calculate','$text.get("customTable.lb.caculator")',150,2,true,'',8000,-2,'');
addCols(editgriddata,'fieldSysType','$text.get("customTable.lb.fieldSysType")',150,0,false,'',10,-2,fieldSysType);
addCols(editgriddata,'fieldIdentityStr','$text.get("customTable.lb.fieldIdentityStr")',130,0,false,'',10,-2,fieldIdentityStr);
addCols(editgriddata,'logicValidate','$text.get("customTable.lb.logicValidate")',150,2,true,'',3000,-2,'');
addCols(editgriddata,'popupType','$text.get("com.poput.type")',150,0,false,'',10,-2,popupType)
addCols(editgriddata,'groupName','$text.get("customTable.lb.groupName")',130,2,true,'',500,-2,'');
addCols(editgriddata,'insertTable','$text.get("tableInfo.lb.autoInsertTable")',100,2,true,'',100,-2,'');
addCols(editgriddata,'isCopy','$text.get("common.lb.copy")',60,0,false,'1',10,-2,isStat);
addCols(editgriddata,'copyType','$text.get("common.lb.copy")',60,0,false,'',10,-2,'');
addCols(editgriddata,'isReAudit','$text.get("ReportBillView.button.Check")',60,0,false,'0',10,-2,'');
addCols(editgriddata,'isLog','记录日志',60,0,false,'0',10,-2,isStat);


#foreach ($row in $result.fieldInfos)
    #set($iv="")
	#set($selectMyd="")
	#if("$row.inputType" == "1" || ("$row.inputType" == "6" && "$row.inputTypeOld"=="1"))
	  #set($iv="$!row.refEnumerationName")
	#elseif("$row.inputType" == "5"  || ("$row.inputType" == "6" && "$row.inputTypeOld"=="5"))
	  #set($iv="$!row.refEnumerationName")
	#elseif("$row.inputType" == "10"  || ("$row.inputType" == "6" && "$row.inputTypeOld"=="10"))
	  #set($iv="$!row.refEnumerationName")
	#elseif("$row.inputType" == "2"  || ("$row.inputType" == "6" && "$row.inputTypeOld"=="2"))
	  #set($iv="$!row.inputValue")
	#end 

	#if($row.fieldName != "id" && $row.fieldName != "f_ref" && $row.fieldName != "f_brother"){
		#if("$row.fieldName"=="checkPersons" || "$row.fieldName"=="classCode" || "$row.fieldName"=="workFlowNode"
	 		|| "$row.fieldName"=="workFlowNodeName" || "$row.fieldName"=="createBy" || "$row.fieldName"=="lastUpdateTime"
	 		|| "$row.fieldName"=="SCompanyID" || "$row.fieldName"=="statusId" || "$row.fieldName"=="lastUpdateBy"
	 		|| "$row.fieldName"=="createTime" || "$row.fieldName"=="isCatalog"){
		}#else {
			addRows(editgriddata,'$row.id','$row.fieldName','$!row.display','$row.isNull','$row.isUnique','$row.isStat','$row.maxLength','$!row.defaultValue','$row.listOrder','$row.fieldType','$!row.digits','$!row.inputType','$row.width','$iv','$!row.calculate','$!row.fieldSysType','$!row.fieldIdentityStr','$!row.logicValidate','$!row.popupType','$!row.groupDisplay','$!row.insertTable','$!row.isCopy','','0');
		}
	#end}
	#end
#end


function changeMC(theit){
     if(theit.value == 0){
	    perantDiv.style.display='none';
		rowCount.style.display='none';
	 }else{
	    perantDiv.style.display='block';
		rowCount.style.display='block';
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
function $(obj){
	return document.getElementById(obj);
}
function beforSubmit(form){ 
	var existField = false ;
	var fieldNames=document.getElementsByName("fieldName");
	var fieldDisplays=document.getElementsByName("fieldDisplay");
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
	if(!validate(form))return false;
	disableForm(form);
    var inputs = document.getElementsByName('moreInput');
    for(var i=0; i<inputs.length;i++){
    	var value=inputs[i].value;
    	if(value=="SelectEmployeeTitleDepart"){
    		var fields = document.getElementsByName('fieldName');
    		var num = 0;//如果三个字段都有则为3
    		for(var j=0; j<fields.length; j++){
    			var fieldName=fields[j].value;
    			if(fieldName=="EmployeeID"||fieldName=="DepartmentCode"||fieldName=="TitleID"){
    				num++;
    			}
    		}
    		if(num<3){
    			alert("$text.get('workflow.msg.LessFieldName')");
    			return false;
    		}
    	}
    }
	var str = document.getElementsByName('fieldSysType');
	for (i=0;i<str.length;i++){
		if(str[i].value="undefined"){
			document.getElementsByName('fieldSysType')[i].value="";
		}
	}
	document.form.submit();
}	

function copySave(){
	form.operation.value=$globals.getOP("OP_COPY")
    if(beforSubmit(form))return true;
}

function addMainTable(urlStr){
	var str  = window.showModalDialog(urlStr,"","dialogWidth=730px;dialogHeight=450px");
	if(str == undefined){
		return;
	}
	fs=str.split(";");   
	//保存字段
	
	for(var i=0;i<document.form.perantTableName.options.length;i++){
		if(document.form.perantTableName.options[i].value == fs[0]){
			return;
		}
    }
	
	var oOption = document.createElement("option");
	oOption.appendChild(document.createTextNode(fs[1]));	
	oOption.setAttribute("value", fs[0]);
	document.form.perantTableName.appendChild(oOption);

	return 0;

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

function initCalculate(){}
function delFireEvent(){}
function execStat(){}


function displayLayout(obj){
	if(obj.selectedIndex==0){
		document.getElementById("layoutId").style.display = "block" ;
	}else{
		document.getElementById("layoutId").style.display = "none" ;
	}
}
</script>
</head>

<body onLoad="showtable('edittable'); showStatus(); initTableList(edittableDIV,edittable,editgriddata);">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/WorkFlowTableAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="search" value="$!search">
<input type="hidden" name="noback" value="true">
<input type="hidden" name="parentTableName" value="">
<input type="hidden" name="winCurIndex" value="$!winCurIndex">
<input type="hidden" name="tableType" value="$result.tableType">
<input type="hidden" name="isSunCmpShare" value="$!isshare">
<input type="hidden" name="isBaseInfo" value="$isBaseInfo">
<input type="hidden" name="needsCopy" value="$needsCopy">
<input type="hidden" name="wakeUp" value="$!result.wakeUp">
<input type="hidden" name="hasNext" value="$!result.hasNext">
<input type="hidden" name="isView" value="$!result.isView">
<input type="hidden" name="classFlag" value="$!result.classFlag">
<input type="hidden" name="draftFlag" value="$!result.draftFlag">
<input type="hidden" name="triggerExpress" value="$!result.triggerExpress">
<input type="hidden" name="sysParameter" value="$!result.sysParameter">
<input type="hidden" name="classCount" value="$!result.classCount">
<input type="hidden" name="tableSysType" value="$!result.tableSysType">
<input type="hidden" name="type" value="simple">
<input type="hidden" name="fieldCalculate" value="$!result.fieldCalculate">
<input type="hidden" name="extendButton" value="">
<input type="hidden" name="reAudit" value="0">
<div class="Heading">
	<div class="HeadingTitle">$text.get("oa.workflow.title.tempFileSet")</div>
	<ul class="HeadingButton"> 
		<li><button type="button" onClick="beforSubmit(document.form);" class="hBtns">$text.get("common.lb.save")</button></li>
		<li><button type="button" onClick="javascript:window.close();"  class="hBtns">$text.get("common.lb.close")</button></li>
	</ul>
</div>
<div id="listRange_id" align="center"  >
		<div class="listRange_1"  style="display: none;">
			<li><span>$text.get("common.lb.tblMarker")：</span><font color="#FF0000">*</font><input  type="text" name="tableName" value="$!result.tableName"></li>
			<li style="width: 250px;"><span>$text.get("common.lb.tableName")：</span><font color="#FF0000">*</font><input  type="text" name="tableDisplay" value="$!result.display" onKeyPress="if(event.keyCode == 13){mainmd();}" onDblClick='mainmd();';><img src="/$globals.getStylePath()/images/St.gif" onClick="mainmd();"></li>

			<li id="rowCount" style="display:#if($result.tableType == 0) none #else display #end">
			<span >$text.get("customTable.lb.childRowCount")：	</span>		
				<input type="text" name="defRowCount" value="$!result.defRowCount"></li>
			 	 	 
			<li id="oppoDiv"  style="display:none"  onChange="changeTabInfo(this)">
			<span >
				$text.get("customTable.lb.relation")：	</span>		
				<input type="hidden" name="relationTable" value="$!result.relationTable">
				<input type="text" name="relationTableDis" value="$globals.getTableDisplayName($!result.relationTable)"
				 onDblClick="addRelationTable('/UserFunctionAction.do?tableName=tblUser&selectName=MainTableInfo&operation=22&MOID=20&MOOP=add');">			
			</li>

			<li id="classCountDiv"  #if("$!result.classFlag"=="0") style="display:none" #else style="display:block" #end>
				<span>$text.get("customTable.lb.classCount")：	</span>	
				<input type="text" name="classCount" value="$!result.classCount">	
			</li>
			<li><span>$text.get("com.tableInfo.layout")：</span>
				<select name="isLayout" id="isLayout" onchange="displayLayout(this);">
					<option #if("$!result.isLayout"=="1") selected #end value="1">$text.get("common.lb.yes")</option> 
					<option #if("$!result.isLayout"=="0") selected #end value="0">$text.get("common.lb.no")</option> 			    
				</select>
			</li>
		</div>
		<div class="scroll_function_small_two">
			<div  name="edittableDIV" id="edittableDIV"> 
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function_b" name="edittable" id="edittable">
			</table>
			</div>
		</div>
</div>
</form>
<script type="text/javascript">
jQuery(".scroll_function_small_two").height(jQuery(window).height()-80);
</script>
</body>
</html>
