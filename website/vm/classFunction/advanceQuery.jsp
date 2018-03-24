<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self"></base>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title> $text.get("com.query.advance")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/listGrid.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/Main.vjs"></script>
<script language="javascript" src="/js/DragTable.js"></script>
<script language="javascript" src="/js/SortTable.js"></script>
<script language="javascript"> 
function openInputDate(obj)
{
	WdatePicker({lang:'$globals.getLocale()'});
}

function down(){
	if(event.ctrlKey&&event.keyCode==68){
		if(document.createEvent){
    		var evt = document.createEvent("HTMLEvents");	 	    		
    		evt.initEvent("click",false,false);
    		form.addPre.dispatchEvent(evt);	
		  }else{
			form.addPre.fireEvent('onClick');
		  } 
		
		event.keyCode=9;
	}else if(event.ctrlKey&&event.keyCode==90){
		if(document.createEvent){
    		var evt = document.createEvent("HTMLEvents");	 	    		
    		evt.initEvent("click",false,false);
    		form.backList.dispatchEvent(evt);	
		  }else{
			form.backList.fireEvent('onClick');
		  } 
		
		event.keyCode=9;
	}
}


function beforeButtonQuery(){	
	if("$!parentTableName"!=""){
		var keyIds = window.parent.frames[0].document.getElementsByName("keyId") ;
		var keyVal="";
		var index = 0 ;
		for(var i=0;i<keyIds.length;i++){
			if(keyIds[i].checked){
				keyVal=keyIds[i].value;			
				index=index+1 ;
			}
		}
		var reportName = window.parent.frames[0].document.getElementById("reportName"); 
		if(index>1){
			alert("$text.get('com.select.onlyOne')"+reportName.value+"$text.get('com.select.record')") ;
			return false;;
		}
		document.getElementById("f_brother").value=keyVal ;
	}
	for(var i=0;i<checkList.length;i++){
		var checkItem = checkList[i];
		var varCheck = document.getElementsByName("cols") ;
		var varNum = 100 ;
		for(var j=0;j<varCheck.length;j++){
			if(varCheck[j].value==checkItem.name){
				varNum = j ;
				break ;
			}
		}
		var conValue = document.getElementsByName("conValue") ;
		if(checkItem.isnull==true && (varNum==100 || conValue[varNum].value=="")){
			alert(checkItem.title+"$text.get('common.validate.error.null')")
			return false;
		}
		if(varNum!=100 && !containSC(conValue[varNum].value)){
			alert("$text.get('con.validate.contain.sc')") ;
			return false ;
		}
		if(typeof(conValue[varNum])!="undefined" && conValue[varNum].value.length>0){
			if(checkItem.type==1 && (varNum==100 || !isFloat(conValue[varNum].value))){
				alert(checkItem.title+"$text.get('common.validate.error.number')") ;
				return false ;
			}
			if(checkItem.type==0 && (varNum==100 || !isInt(conValue[varNum].value))){
				alert(checkItem.title+"$text.get('common.validate.error.integer')") ;
				return false ;
			}
		}
	}
	#if("$!advanceType"=="reportNumber")
		document.form.action = "/ReportDataAction.do";
	#elseif("$!advanceType"=="popup")
		document.form.action = "/UserFunctionAction.do";
		document.form.operation.value="22" ;
	#end
	document.form.queryChannel.value="advance";
}

var cols="<select name='cols' onChange='colChange(this)'>"+
	"<option value=\"\">&nbsp;</option>"+
	#foreach($row in $conditions)
    "<option value=\"$!globals.get($row,1)\">$!globals.get($row,0)</option>"+
	#end
	#foreach($row in $conditionCols)
	"<option value=\"$!globals.get($row,3)\">$!globals.get($row,0)</option>"+
	#end
	"</select>";

 //比较类型
var compType="<select name='compType'>"+
		#foreach($erow in $globals.getEnumerationItems("compType"))
                 "<option value='$erow.value'#if($erow.value=='like') selected #end>$erow.name</option>"+   
		#end
			  "</select>";				  
var relation="<select name='relation'>"+
		#foreach($erow in $globals.getEnumerationItems("relation"))
                 "<option value='$erow.value'>$erow.name</option>"+  
        #end
              "</select>";
			  
#set($firstcols = "")
function colChange(obj){
	obj.ttt = "ttt";
	var rowId = 0;
	var conTable = document.getElementById('advanceTable');	
	var celltd;
	var colslist = document.getElementsByName('cols');
	for(var i=0;i<conTable.rows.length;i++){
		if(colslist[i].ttt == 'ttt')
		{
			rowId = i;
			colslist[i].ttt = '';
			celltd = conTable.rows[i].cells[2];
			break;
		}
	}
	if(typeof(celltd)=="undefined")return ;
	if(obj.value == ''){	
	#foreach($row in $conditions)
		}else if(obj.value == "$!globals.get($row,1)"){
		#if($!globals.get($row,3) == "0")
		    #set($data="<input name='conValue' value='$!globals.get($row,2)' onKeyDown='if(event.keyCode==13) event.keyCode=9' >")	
		    celltd.innerHTML = "$data";
			#if($firstcols == "")
			    #set($firstcols = $data)
			#end	
		#elseif($!globals.get($row,3) == "1")	
		    #set($data="<select name='conValue' ><option value='' ></option>")
			#foreach($erow in $globals.getEnumerationItems($!globals.get($row,4)))
				#if("$erow.value" == "$!globals.get($row,2)") 
					#set($data=$data+"<option value='$erow.value' selected >$erow.name</option>") 
				#else
					#set($data=$data+"<option value='$erow.value' >$erow.name</option>")
				#end
			#end
			#set($data=$data+"</select>")
			#if($firstcols == "")
			    #set($firstcols = $data)
			#end	
		celltd.innerHTML = "$data";	
		
		#elseif($!globals.get($row,3) == "2")
			#set($data="<input name='conValue' value='$!globals.get($row,2)' onKeyDown='if(event.keyCode==13) openInputDate();' onClick='openInputDate();'  >")
		    celltd.innerHTML = "$data";	
			#if($firstcols == "")
			    #set($firstcols = $data)
			#end	
		#elseif($!globals.get($row,3) == "4")
			#set($data="<input name='conValue' value='$!globals.get($row,2)' ondblclick='openSelect(\"$!globals.get($row,4)\",\"$!globals.get($row,0)\",this)'>")
			celltd.innerHTML = "$data";	
			#if($firstcols == "")
			    #set($firstcols = $data)
			#end	
		#end	 
	#end
	#foreach($row in $conditionCols)
		}else if(obj.value == "$!globals.get($row,3)"){
		#if($!globals.get($row,2) == "string")
		    #set($data="<input name='conValue'  onKeyDown='if(event.keyCode==13) event.keyCode=9' >")
		    celltd.innerHTML = "$data";
			#if($firstcols == "")
			    #set($firstcols = $data)
			#end	
		#elseif($!globals.get($row,2) == "date")
			#set($data="<input name='conValue'  onKeyDown='if(event.keyCode==13) openInputDate();' onClick='openInputDate();'  >")
		    celltd.innerHTML = "$data";	
			#if($firstcols == "")
			    #set($firstcols = $data)
			#end	
		#end	 
	#end
	}
}


function addRow(varCols,varCompType,value,varRelation){
	
	var conTable = document.getElementById('advanceTable');	
	var newTr = conTable.insertRow(conTable.rowCount) ;
	newTr.rowId = conTable.rowCount;
	conTable.rowCount = Number(conTable.rowCount)+1;
	var sTd = newTr.insertCell(0);
	sTd.rowTr = newTr;
	sTd.innerHTML = varCols;
	sTd = newTr.insertCell(1);
	sTd.innerHTML = varCompType; 
	sTd = newTr.insertCell(2);
	sTd.rowTr = newTr;
	sTd.innerHTML = value;
	sTd.onclick = function(){
		if(this.rowTr.rowId == conTable.rowCount - 1) addRow(cols,compType,"$firstcols",relation);
	};
	
	sTd = newTr.insertCell(3);
	sTd.rowTr = newTr;
	sTd.innerHTML = varRelation; 
	
	sTd = newTr.insertCell(4);
	sTd.rowTr = newTr;
	sTd.innerHTML = "<img src='/$globals.getStylePath()/images/del.gif' border='0' class='search'/>"; 
	sTd.onclick = function(){
		conTable.deleteRow(this.rowTr.rowId);
		conTable.rowCount--;
		var colDataRows = conTable.rows; 
	    for (var i=0; i < colDataRows.length; i++) {
			colDataRows[i].rowId = i;
		}
	}
}

function init(){
	var varCols = "" ;
	var varCompType = "" ;
	var varRelation = "" ;
	var varValue = "" ;
	#set($colIndex=0)
	//高级查询后 条件处理
	#foreach($strCon in $advanceMap.get("$reportNumber"))
		varCols = cols.replace("value=\"$!globals.get($strCon,0)\"","value=\"$!globals.get($strCon,0)\" selected") ;
		varCompType = compType.replace("selected","").replace("value='$globals.existComType($!globals.get($strCon,1))'","value='$globals.existComType($!globals.get($strCon,1))' selected") ;
		varRelation = relation.replace("value='$!globals.get($strCon,3)'","value='$!globals.get($strCon,3)' selected") ;
		#foreach($row in $conditions)
		    #if("$!globals.get($strCon,0)"=="$!globals.get($row,1)")
			    #if("$!globals.get($row,3)"=="2")
			    	#set($colType="2")	##$text.get("oa.calendar.date")
			    #elseif("$!globals.get($row,3)"=="1")
			    	#set($colType="1")  ##$text.get("common.type.enumerate")
			    	#set($enumValue="$!globals.get($row,4)")
			    #elseif("$!globals.get($row,3)"=="4") ##$text.get("common.type.popup")
			    	#set($colType="3")
			    	#set($popupName=$!globals.get($row,4))
			    	#set($display=$!globals.get($row,0))
			    #else
			    	#set($colType="0")	##$text.get("common.other")
			    #end
		    #end
		#end
		#if("$!colType"=="2")
	    	varValue = "<input name='conValue' type='text' value='$!globals.get($strCon,2)' onKeyDown='if(event.keyCode==13) openInputDate();' onClick='openInputDate();'/>" ;
	    #elseif("$!colType"=="3")
	    	varValue = "<input name='conValue' type='text' value='$!globals.get($strCon,2)' ondblclick='openSelect(\"$popupName\",\"$display\",this)'/>" ;
	    #elseif("$!colType"=="1")
	    	varValue = "<select name='conValue' ><option value='' ></option>"+
					#foreach($erow in $globals.getEnumerationItems("$enumValue"))
						#if("$erow.value" == "$!globals.get($strCon,2)") 
							"<option value='$erow.value' selected >$erow.name</option>"+
						#else
							"<option value='$erow.value' >$erow.name</option>"+
						#end
					#end
					"</select>" ;
	    #else
	    	varValue = "<input name='conValue' type='text' value='$!globals.get($strCon,2)' />" ;
	    #end
    	addRow(varCols,varCompType,varValue,varRelation) ;
    	#set($colIndex=$colIndex+1)
	#end
	//第一次高级查询 默认值处理

	#if($colIndex==0)
	#foreach($condField in $conDefaultValue)
		///$condField.inputType $condField.fieldName
		varCols = cols.replace("value=\"$condField.fieldName\"","value=\"$condField.fieldName\" selected") ;
		varCompType = compType.replace("selected","").replace("value='$globals.existComType($condField.condition)'","value='$globals.existComType($condField.condition)' selected") ;
		varRelation = relation.replace("value='$condField.conditionJoin'","value='$condField.conditionJoin' selected") ;
		#if("$condField.fieldType"=="5")
	    	varValue = "<input name='conValue' type='text' value='$!condField.defaultValue' onKeyDown='if(event.keyCode==13) openInputDate();' onClick='openInputDate();'/>" ;
	    #elseif("$condField.inputType"==2)
	    	#set($displayName=$globals.replaceString($globals.replaceString($!field.display,"≥",""),"≤",""))
	    	varValue = "<input name='conValue' type='text' value='$!condField.defaultValue' ondblclick='openSelect(\"$condField.popUpName\",\"$displayName\",this)'>/>" ;
	    #elseif("$condField.inputType"==1)
	    	varValue = "<select name='conValue' ><option value='' ></option>"+
					#foreach($erow in $globals.getEnumerationItems("$!condField.popUpName"))
						#if("$erow.value" == "$!condField.defaultValue") 
							"<option value='$erow.value' selected >$erow.name</option>"+
						#else
							"<option value='$erow.value' >$erow.name</option>"+
						#end
					#end
					"</select>" ;
	    #else
	    	varValue = "<input name='conValue' type='text' value='$!condField.defaultValue' />" ;
	    #end
    	addRow(varCols,varCompType,varValue,varRelation) ;
	#end
	#end
	addRow(cols,compType,"$firstcols",relation);
}

function deleteRow(){
   var rowIndex = event.srcElement.parentElement.parentElement.rowIndex;
   var styles = document.getElementById("mytable");
   styles.deleteRow(rowIndex);
}

function returnBack(){
	if("$!advanceType"=="reportNumber"){
		document.form.queryChannel.value = "normal" ;
		document.form.action = "/ReportDataAction.do" ;
		document.form.submit() ;
	}else if("$!advanceType"=="popup"){
		document.form.action = "/UserFunctionAction.do";
		document.form.operation.value="22" ;
		form.submit() ;
	}else{
		document.form.queryChannel.value = "normal" ;
		document.form.submit() ;
	}
	
}

function checkItem(name,title,isnull,type){
	this.name = name;
	this.title = title;
	this.isnull = isnull ;
	this.type = type ;
}

var checkList = new Array();

#foreach($field in $condFields)
	#if("$field.isNull"=="1")
		#set($display=$globals.replaceString($globals.replaceString($!field.display,"≥",""),"≤",""))
		checkList[checkList.length] = new checkItem("$field.fieldName","$!display",true,$field.fieldType);
	#else
		#set($display=$globals.replaceString($globals.replaceString($!field.display,"≥",""),"≤",""))
		checkList[checkList.length] = new checkItem("$field.fieldName","$!display",false,$field.fieldType);
	#end
#end

function openSelect(selectName,display,obj){
 	var urlstr = '/UserFunctionAction.do?operation=22&displayName='+encodeURI(display)+'&LinkType=@URL:&tableName=$!reportNumber&selectName='+selectName ;
 	var str = window.showModalDialog(urlstr+"&MOID=41&MOOP=query","","dialogWidth=730px;dialogHeight=450px"); 
 	if(typeof(str)!="undefined"){
 		var content = str.split(";") ;
#foreach($field in $condFields)
#if($field.inputType==2)
#set($popBean=$!globals.getPopupBean($field.popUpName))
#if($popBean.saveFields.size()==0)
obj.value = content[0] ;
#else
#foreach($fv in $popBean.saveFields)
if("$fv.relationKey"=="true"){
	obj.value=content[$globals.subtracter($velocityCount,1)];
}
#end
#end				
#end
#end
 	}
}

function clickAddRow(){
	addRow(cols,compType,"$firstcols",relation) ;
}
</script>
<style type="text/css">
	.listRange_list_function tbody td {
		height:27px;
		line-height:27px;
	}
</style>
</head>

<!--body onLoad="showStatus();document.getElementById('subButton').focus();"-->
<body onLoad="init();">
<base target="_self"/>
<form  method="post" scope="request"  name="form" action="/UserFunctionQueryAction.do" onKeyDown="down()">
 <input type="hidden" name="root" value="$!root">
 <input type="hidden" name="isRoot" value="">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
 <input type="hidden" name="tableName" value="$!request.getParameter("tableName")">
 <input type="hidden" name="reportNumber" value="$!reportNumber"/>
 <input type="hidden" name="optionType" value="" > 
 <input type="hidden" name="parentCode" value="$!request.getParameter("parentCode")"> 
 <input type="hidden" name="f_brother" value="$!request.getParameter("f_brother")">
 <input type="hidden" name="defineName" value="">
 <input type="hidden" name="winCurIndex" value="$!request.getParameter("winCurIndex")">
 #set($parentTableName=$!request.getParameter("parentTableName"))
 <input type="hidden" name="parentTableName" value="$!parentTableName">
 <input type="hidden" name="selectName" value="$!request.getParameter("selectName")">
 <input type="hidden" name="fieldName" value="$!request.getParameter("fieldName")">
 <input type="hidden" name="displayName" value="$!request.getParameter("displayName")">
 <input type="hidden" name="iniPropField" value="$!request.getParameter("iniPropField")">
 <input type="hidden" name="MOID" value="$!request.getParameter("MOID")">
 <input type="hidden" name="MOOP" value="$!request.getParameter("MOOP")">
 <input type="hidden" name="strAction" value="$strAction" />
 <input type="hidden" name="queryChannel" value="advance" />
 $!mainHiddenFields
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("com.query.advance")</div>
	<ul class="HeadingButton">
		#set($winIndex=$!request.getParameter("winCurIndex"))
		<li><button name="Submit" id="subButton" type="submit" onClick="forms[0].operation.value=$globals.getOP("OP_QUERY"); return beforeButtonQuery();" class="b2">$text.get("common.lb.query")</button></li>
		<li><button type="button" name="backList" onClick="#if("$!winIndex"=="")javascript:returnBack();#{else}location.href='$!globals.getModuleUrlByWinCurIndex($!winCurIndex)&checkTab=Y&parentTableName=$!parentTableName&winCurIndex=$!winCurIndex'#end" class="b2">$text.get("common.lb.back")</button></li>
	</ul>
</div>
<!--高级查询条件列表-->
		<div class="scroll_function_small_a">
		
		<table border="0" id="mytable" cellpadding="0" width="450" cellspacing="0" class="listRange_list_function">
		<THead>
  <tr>
    <td colspan="4" style="text-align:left; text-indent:5px;">$text.get("tableInfo.lb.searchCondition")</td>
	<td width="25">
	  <img src="/$globals.getStylePath()/images/add.gif" onClick="clickAddRow()" class="search">	</td>
    </tr>
	  </THead>
	  <TBody id="advanceTable" rowCount="0">
	  </TBody>
</table>
</div>
</form>
</body>
</html>
