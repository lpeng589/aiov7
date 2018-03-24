<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("tableInfo.title.add")</title>
<link type="text/css"  rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/validate.vjs"></script>
<script type="text/javascript" src="/js/editGrid.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>

<script language="javascript">

var editgriddata = new gridData();
var ftype="";
 #foreach ($row in $globals.getDS("fieldType"))
           ftype+="$row.value:$row.name;"; 
 #end
 //3是备注
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
					document.getElementsByName('moreInput')[EnumerationName.parentNode.parentNode.rowIndex+i].click();
				}
			}
		}else{
			EnumerationName.value = strName;
			document.getElementsByName('moreInput')[EnumerationName.parentNode.parentNode.rowIndex-1].value = strdisplay;
			document.getElementsByName('fieldName')[EnumerationName.parentNode.parentNode.rowIndex-1].value = fieldNameTem;
			document.getElementsByName('fieldName')[EnumerationName.parentNode.parentNode.rowIndex-1].readOnly = true ;
		}
}

function lostfocus(EnumerationValue){
	var inType = document.getElementsByName('inputType')[EnumerationValue.parentNode.parentNode.rowIndex-1].value;
	if(inType == '1'){
		var inTypeValue = document.getElementsByName('moreInputxxx')[EnumerationValue.parentNode.parentNode.rowIndex-1].value;
		document.getElementsByName('moreInput')[EnumerationValue.parentNode.parentNode.rowIndex-1].value = inTypeValue;
	}
}

function setMoreInput(str){
	
}

function setMaxLength(fTypeSelect) {
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
		/*if(type == 2){
			var str  = window.showModalDialog("/vm/tableInfo/mainTableSelect.jsp?type=2","","dialogWidth=730px;dialogHeight=450px");
	 		obj.value = str;	
		}*/
   }
}

var va = 'moredisClick(this)';
var vb = 'moreInputClick(this)';

addCols(editgriddata,'fieldName','$text.get("common.lb.ziduanbiaoshi")',120,2,true,'',500,0,'');
addCols(editgriddata,'fieldDisplay','$text.get("common.lb.ziduanmingcheng")',130,2,true,'',500,4,'');
addCols(editgriddata,'isNull','$text.get("customTable.lb.isNULL")',60,0,false,'0',10,1,isNull);
addCols(editgriddata,'isUnique','$text.get("customTable.lb.unique")',40,0,false,'0',10,1,isUnique);
addCols(editgriddata,'isStat','$text.get("customTable.lb.stat")',40,0,false,'0',10,-2,isStat);
addCols(editgriddata,'maxLength','$text.get("customTable.lb.maxLength")',60,0,false,'200',10,0,'');
addCols(editgriddata,'defaultValue','$text.get("customTable.lb.defaultValue")',80,2,false,'',50,0,'');
addCols(editgriddata,'listOrder','$text.get("customTable.lb.listOrder")',30,0,false,'',10,0,'');
addCols(editgriddata,'fieldType','$text.get("customTable.lb.fieldType")',80,0,false,'2',10,1,ftype);
addCols(editgriddata,'digits','$text.get("customTable.lb.digits")',50,0,false,'4',10,0,'');
addCols(editgriddata,'inputType','$text.get("customTable.lb.inputType")',80,0,false,'0',10,1,intype);
addCols(editgriddata,'width','$text.get("customTable.lb.inputSizeW")',60,0,false,'200',10,0,'');
addCols(editgriddata,'moreInputxxx','$text.get("common.lb.meijubiaoshi")',120,2,true,'',5000,44,vb);
addCols(editgriddata,'moreInput','$text.get("common.lb.meijubiaoshi")',80,2,true,'',5000,-2,vb);
addCols(editgriddata,'calculate','$text.get("customTable.lb.caculator")',150,2,true,'',8000,-2,'');
addCols(editgriddata,'fieldSysType','$text.get("customTable.lb.fieldSysType")',150,0,false,'',10,-2,fieldSysType);
addCols(editgriddata,'fieldIdentityStr','$text.get("customTable.lb.fieldIdentityStr")',130,0,false,'',10,-2,fieldIdentityStr);
addCols(editgriddata,'logicValidate','$text.get("customTable.lb.logicValidate")',150,2,true,'',3000,-2,'');
addCols(editgriddata,'popupType','$text.get("com.poput.type")',150,0,false,'',10,-2,popupType)
addCols(editgriddata,'groupName','$text.get("customTable.lb.groupName")',130,2,true,'',500,-2,'');
addCols(editgriddata,'isCopy','$text.get("common.lb.copy")',60,0,false,'1',10,-2,isStat);
addCols(editgriddata,'copyType','$text.get("common.lb.copy")',60,0,false,'1',10,-2,'');
addCols(editgriddata,'reAudit','$text.get("ReportBillView.button.Check")',60,0,false,'0',10,-2,'');
addCols(editgriddata,'isLog','记录日志',60,0,false,'0',10,-2,isStat);

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
	var str = document.getElementsByName('fieldSysType');
	for (i=0;i<str.length;i++){
		if(str[i].value="undefined"){
			document.getElementsByName('fieldSysType')[i].value="";
		}
	}
	if(!validate(form))return false;
	disableForm(form);
	
	//如果有选择SelectEmployeeTitleDepart关联表标识，则要有EmployeeID,DepartmentCode,TitleID三个字段。







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
	document.form.submit();
	window.parent.returnValue=document.getElementsByName("tableName")[0].value+";"+document.getElementsByName("tableDisplay")[0].value;
	//window.close();
}	

function addMainTable(urlStr){
	var str  = window.showModalDialog(urlStr,"","dialogWidth=730px;dialogHeight=450px");
	alert(str) ;
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
</script>
</head>

<body onLoad="showtable('edittable'); showStatus(); initTableList(edittableDIV,edittable,editgriddata,2);"> 

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/WorkFlowTableAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" a="b" value="$globals.getOP("OP_ADD")">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="parentTableName">
<input type="hidden" name="winCurIndex" value="$!winCurIndex">
<input type="hidden" name="isSunCmpShare" value="0">
<input type="hidden" name="isBaseInfo" value="0">
<input type="hidden" name="needsCopy" value="0">
<input type="hidden" name="classFlag" value="0">
<input type="hidden" name="draftFlag" value="0">
<input type="hidden" name="isView" value="0">
<input type="hidden" name="hasNext" value="0">
<input type="hidden" name="wakeUp" value="0">
<input type="hidden" name="isLayout" value="0">
<input type="hidden" name="triggerExpress" value="1">
<input type="hidden" name="fieldCalculate" value="">
<input type="hidden" name="extendButton" value="">
<input type="hidden" name="type" value="simple">
<input type="hidden" name="copyType" value="">
<input type="hidden" name="reAudit" value="0">
#foreach($erow in $globals.getEnumerationItems("SysParamControl"))
<input type="hidden" name="sysParameter" value="$erow.value">
#end

#foreach($erow in $globals.getEnumerationItems("FieldSysType"))
#if($erow.value=="Normal")
<input type="hidden" name="tableSysType" value="$erow.value">
#end
#end

<input type="hidden" name="tableName"   id="tableName" value="$!tableName"/>
<input type="hidden" name="tableCHName" id="" value="$!tableCHName"/>

<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("tableInfo.title.add")</div>
	<ul class="HeadingButton">	
		<li><button type="button" onClick="form.winCurIndex.value='$!winCurIndex';beforSubmit(document.form);"  class="hBtns">$text.get("common.lb.save")</button></li>
		<li><button type="button" onClick="javascript:window.close();"  class="hBtns">$text.get("common.lb.close")</button></li>
	</ul>
</div>
<div id="listRange_id" align="center">
		<div class="listRange_1" style="display: none;">	
			<li><span>$text.get("common.lb.tblMarker")：</span><font color="#FF0000">*</font><input  type="text"  name="tableName" value="$!tableName" ></li>
			<li style="width: 250px;"><span>$text.get("common.lb.tableName")：</span><font color="#FF0000">*</font><input  type="text"  name="tableDisplay" value="$!tableCHName"><img src="/$globals.getStylePath()/images/St.gif" onClick="mainmd();"></li>
			<li id="rowCount" style="display:none">
				<span>$text.get("customTable.lb.childRowCount")：</span>		
				<input type="text" name="defRowCount" value="10">
			</li>
			<li id="classCountDiv"  style="display:none">
				<span>$text.get("customTable.lb.classCount")：</span>		
				<input type="text" name="classCount" value="5">
			</li>
			<li id="oppoDiv"  style="display:none">
				<span>$text.get("customTable.lb.relation")：</span>		
				<input type="hidden" name="relationTable" >
				<input type="text" name="relationTableDis" value="" onDblClick="addRelationTable('/UserFunctionAction.do?tableName=tblUser&selectName=MainTableInfo&operation=22&MOID=20&MOOP=add');">			
			</li>
		</div>
		<div class="scroll_function_small_two">
			<div  name="edittableDIV" id="edittableDIV"> 
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list" name="edittable" id="edittable">
			</table>
			</div>
		</div>
	</div>

</form> 
</body>
</html>
