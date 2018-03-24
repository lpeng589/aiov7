<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("com.table.layout.html")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript">
function WdatePicker(){}

function initFieldData(){
	var layoutHTML = window.dialogArguments.layoutHTML.html();
	$("#listRange_id").html(layoutHTML) ;
	$(":input").each(function(i){
		this.readOnly = true ;
   		this.ondblclick = function(){
   			setFieldName(this) ;
   		} ;
 	});
}

#set($strTableName=$request.getParameter("tableName"))
var tableName = "$!strTableName" ;
function setFieldName(fieldValue){
	var fieldName = fieldValue.name ;
	var displayName = encodeURI("$text.get("com.table.field.relation")") ;
	var str  = window.showModalDialog("/UserFunctionAction.do?operation=22&selectName=selectDBFieldName&tableName="+tableName+"&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,"","dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)!="undefined"){
		var strValue = str.split(";") ;
		if(fieldValue.type!="radio" && fieldValue.type!="checkbox" && fieldValue.type!="select-one"){
			var field2 = document.getElementById(strValue[0]) ;
			if(field2!=null && fieldName!=strValue[0]){
				alert("$text.get("com.table.exist.field")") ;
				return ;
			}
			if(strValue[0]==""){
				strValue[0] = (new Date()).getTime() ;
				fieldValue.value = "" ;
			}else{
				fieldValue.value = "#"+strValue[0]+"#" ;
			}
 		}
 		var html = $("#listRange_id") ;
		var htmlValue = html.html() ;
 		htmlValue = htmlValue.replace("id=\""+fieldName+"\"", "");
    	htmlValue = htmlValue.replace("id='"+fieldName+"'", "");
    	htmlValue = htmlValue.replace("id="+fieldName, "");
		htmlValue = htmlValue.replace("name=\""+fieldName+"\"", "id=\""+strValue[0]+"\" name=\""+strValue[0]+"\"") ;
		htmlValue = htmlValue.replace("name="+fieldName,"id=\""+strValue[0]+"\" name=\""+strValue[0]+"\"") ;
		
		html.html(htmlValue) ;
		$(":input").each(function(i){
			this.readOnly = true ;
	   		this.ondblclick = function(){
	   			setFieldName(this) ;
	   		} ;
	 	});
 		document.getElementById(strValue[0]).focus() ;
	}
}

function saveField(){
	if(!submitBefore()){return}
	$(":input").each(function(i){
		this.readOnly = false ;
 	});
	window.returnValue = $("#listRange_id").html() ;
	window.close() ;
}

function submitBefore(){
	var exist = false ;
	var nullField = "" ;
	#foreach($field in $globals.getTableInfoBean($!strTableName).fieldInfos)
		#if($field.inputType!=3 && $field.inputType!=100 
				&& $field.fieldName!="id" && $field.fieldName!="f_brother" && $field.isNull=="1")
		var varField = document.getElementById("$field.fieldName") ;
		if(typeof(varField)=="undefined" || varField==null){
			exist = true ;
			nullField += "\""+"$field.display.get($globals.getLocale())"+"\", " ;
		}
		#end
	#end
	if(exist){
		if(!confirm(nullField+"$text.get("com.layout.info.alert")")){
			return false ;
		} 
	}
	return true ;
}
function validateField(){
	var exist = false ;
	#foreach($field in $globals.getTableInfoBean($!strTableName).fieldInfos)
		#if($field.inputType!=3 && $field.inputType!=100 
				&& $field.fieldName!="id" && $field.fieldName!="f_brother")
		var varField = document.getElementById("$field.fieldName")
		if(typeof(varField)=="undefined" || varField==null){
			exist = true ;
			alert("\""+"$field.display.get($globals.getLocale())"+"\"$text.get("com.table.noexist.field")") ;
			return ;
		}
		#end
	#end
	if(!exist){
		alert("$text.get("com.table.field.sucess")") ;
	}
}
</script>

</head>

<body onLoad="initFieldData()">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("com.layout.field.validate")</div>
	<ul class="HeadingButton">
		<li><button onclick="saveField();">$text.get("common.lb.save")</button></li>
		<li><button onclick="validateField();">$text.get("com.layout.field.validate2")</button></li>
		<li><button onclick="javascript:window.close();">$text.get("common.lb.close")</button></li>
	</ul>
</div>
<div id="listRange_id">
</div>

</body>
</html>
