<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("excel.lb.addModel")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>

<script language="javascript">

putValidateItem("importName","$text.get('import.template.name')","any","",false,1,99 );

function beforSubmit(form){   
	
	
	var items = form.elements;
	
	for(i=0;i<items.length;i++){
		if(items[i].type=="select-one"){
			if(items[i].value==""){
				alert(items[i].dis+"$text.get('common.validate.error.null')");
				items[i].focus();
				return false;
			}
		}
	}
	if(!validate(form))return false;
	
	disableForm(form);
	form.submit();
	return true;
}		

function setField(obj,fstr){
	path="/UtilServlet?operation=getFieldInfo&tableName="+obj.value;
	var xmlHttp;
	if (window.ActiveXObject) {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	} 
	else if (window.XMLHttpRequest) {
		xmlHttp = new XMLHttpRequest();
	}
	xmlHttp.onreadystatechange = function(){
		if(xmlHttp.readyState == 4) {
			if(xmlHttp.status == 200) {
				response = xmlHttp.responseText;
				if(response != ""){					
					fo = document.getElementById("from_"+fstr);
					vo = document.getElementById("view_"+fstr);					
					fo.options.length=0;
					vo.options.length=0;
					sps = response.split(";");
					for(i=0;i<sps.length;i++){
						vv = sps[i].split("|");
						fo.add(new Option(vv[1],vv[0])); 
						vo.add(new Option(vv[1],vv[0])); 
					}	
				}
			}else{
				response = "no response text" ;
			}
		}
	};
	xmlHttp.open("get", path, false);
	xmlHttp.send();
}
</script>
</head>

<body onLoad="showStatus();">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" id="form" name="form" action="/importDataAction.do?operation=$globals.getOP('OP_UPDATE')" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="advance" value="$advance">
<input type="hidden" name="winCurIndex" value="$winCurIndex">
<input type="hidden" name="tableName" value="$tableName">
<input type="hidden" name="id" value="$importBean.id">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("import.template.add")</div>
	<ul class="HeadingButton">		
	<li><button type="button" onClick="beforSubmit(document.form);" class="b2">$text.get("common.lb.save")</button></li>
	<li><button type="button" onClick="location.href='/importDataQueryAction.do?operation=$globals.getOP("OP_QUERY")&advance=$!advance&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button></li>
		
	</ul>
</div>
<div id="listRange_id">
		<div class="listRange_1">	
			<li><span>$text.get("excel.add.tableName")：</span>
			<input  type="text" value="$tableNameDisplay" readonly style="border:0px">
			</li>
			<li><span>$text.get("import.template.name")：</span><input  type="text"  name="importName" id="importName" value="$importBean.name"></li>
			<li><span>$text.get("excel.list.isUsed")：</span>
			<select name="isUsed" id="select"> 
			    <option value="1" #if("$importBean.flag" == "1") selected #end>$text.get("excel.list.yes")</option>
				<option value="2" #if("$importBean.flag" == "2") selected #end>$text.get("excel.list.no")</option>
		      </select>
			</li>
		</div>
		<div class="scroll_function_small_a" id="conter">
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-115;
oDiv.style.height=sHeight+"px";
</script>
			<table style="width: 600px;" border="0" width="100%" cellpadding="0" cellspacing="0" class="listRange_list" name="edittable" id="edittable">
			<thead>
			<tr>
				<td width="20%">$text.get("import.table.name")</td>
				<td width="10%" align="center"><!--input type="checkbox" id="selAll" onClick="checkAll('keyId')"-->$text.get("import.add.choose")</td>
				<td>$text.get("import.list.field")</td>
			</tr>
			</thead>
			<tbody>
			#foreach($rowt in $fieldList) 
			#set($firstrow = true)
			#set($rowCount=0)
			#foreach($rowf in $rowt.fieldInfos)
			   #if($globals.canImportField($rowf))
			   #set($rowCount=$rowCount+1) #end
			#end
			#foreach($rowf in $rowt.fieldInfos)
			#if($globals.canImportField($rowf))
			<tr>
				#set($name=$rowt.tableName+"_"+$rowf.fieldName)
				#if($firstrow) <td rowspan="$rowCount">  $rowt.display.get($globals.getLocale())</td> #set($firstrow = false) #end
				<td><input type="checkbox" name="key_$name" value="$rowf.fieldName" #if("$fieldMap.get($name).flag" == "1") checked #end #if($rowf.isNull == 1) onClick="return false;"   #end> <input type="hidden" name="id_$name" value="$fieldMap.get($name).id"/> </td>
				<td><input type="text" name="name_$name" value="#if($fieldMap.get($name))$fieldMap.get($name).name#else$rowf.display.get("$globals.getLocale()")#end" />
				#if($rowf.inputType == 2)
				<input type="hidden" name="tab_$name" value="$fieldMap.get($name).viewTableName"/>
				<input type="hidden" name="from_$name" value="$fieldMap.get($name).viewFieldName"/>
				<input type="hidden" name="view_$name" value="$fieldMap.get($name).viewSaveField"/>
				#end
				</td>
			</tr>
			#end
			#end
			#end
			</tbody>
			</table>
		</div>
  </div>
</form> 
</body>
</html>
