<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<style>
.scrollColThead {
	position: relative;
	top: expression(this.parentElement.parentElement.parentElement.scrollTop);
	z-index:2;
}
</style>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript">
function $(obj){
		return document.getElementById(obj);
}

function $$(obj){
		return document.getElementsByName(obj);
}

var No = 0;
function addTr(varTableName,varTableDis,varFieldName,varFieldDis,varType){

	if(isExistPopup(varTableName+"-"+varFieldName)){
		return ;
	}
	var mainTable = $("mainTable");
	var rowNo = mainTable.rows.length ;
	var my_row = mainTable.insertRow(rowNo);
	var serial = my_row.insertCell();
	var tableName = my_row.insertCell();
	var fieldName = my_row.insertCell();
	var fieldType = my_row.insertCell();
	var fieldLanguage = my_row.insertCell();
	var del = my_row.insertCell();
	for(var i = 0;;i++){
		if($(i) == null){	
			No = i;		
			break;
		}
	}
	my_row.id = No;
	serial.id = "serial"+No;
	serial.className = "listheadonerow";
	serial.innerHTML = No+1;
	tableName.innerHTML =  "<input type=hidden name=fieldName value=\""+varTableName+"-"+varFieldName+"\"/>"+varTableDis;
	fieldName.innerHTML =  varFieldDis;
	if("list"==varType){
		varType = "$text.get("com.popup.report")" ;
	}else{
		varType = "$text.get("com.popup.bill")" ;
	}
	fieldType.innerHTML = varType ;
	fieldLanguage.innerHTML =  "<input type=text name=\"fieldLanague\" readonly ondblclick='moreLanguage(\"$globals.getLocale()\",this)'/>";
	del.style.cursor="pointer";
	del.align="center"
	del.innerHTML = "<img src='/$globals.getStylePath()/images/del.gif' onclick='delRow(this)' />";
	No++;
}

function delRow(obj){
	var tr = obj.parentNode.parentNode;
	var start = Number(tr.id) + 1;
	$("mainTable").deleteRow(tr.rowIndex-1);
	for(;;start++){
		if($(start) == null){
			break;
		}
		$(start).id = start -1;
		$("serial"+start).innerHTML = start;
		$("serial"+start).id = "serial"+(start -1);
	}
	No--;
}

function isExistPopup(popup){
	for(j=1;j<$("mainTable").rows.length;j++){
		if(popup==$$("fieldName")[j-1].value){
			return true ;
		}
	 }
  	return false;
}

function selectModuleColName(){
	var moduleName = $("moduleName").value ;
	if(moduleName.trim().length==0){
		alert("$text.get('com.module.name.null')") ;
		return ;
	}
	var urlstr = "/vm/coldisplay/popModuleColList.jsp?tableName="+moduleName ;
	var str = window.showModalDialog(urlstr,"","dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)=="undefined")return false ;
	var moduleFields = str.split("|") ;
	for(var i=0;i<moduleFields.length-1;i++){
		var field = moduleFields[i].split(";") ;
		addTr(field[0],field[1],field[2],field[3],field[4]) ;
	}
}
	
function selectModuleName(){
	var displayName = encodeURI("$text.get("com.module.name")") ;
	var urlstr = '/UserFunctionAction.do?operation=22&selectName=modulesPop&displayName='+displayName ;
	var str = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=query&LinkType=@URL:","","dialogWidth=830px;dialogHeight=450px"); 
	if(typeof(str)=="undefined") return ;
	var field = str.split(";") ;
	$("moduleId").value=field[0]
	$("moduleName").value=field[1]
	$("moduleDisplay").value=field[2]
	$("addNewField").style.display = "block" ;
	selectModuleColName() ;
}

function moreLanguage(len,moreLanguagefield){
	var str  = window.showModalDialog("/common/moreLanguage.jsp?len="+len+"&str="+encodeURI(moreLanguagefield.value),"","dialogWidth=730px;dialogHeight=450px");
	moreLanguagefield.value = str;
}

function beforSubmit(){
	var moduleDisplay = $("moduleDisplay").value ;
	if(moduleDisplay.trim().length==0){
		alert("$text.get('com.module.name.null')") ;
		return false ;
	}
	if($("mainTable").rows.length==1){
		alert("$text.get('com.popup.field.one')") ;
		return false ;
	}
	return true ;
}
</script>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form id="form" method="post" scope="request" name="form" action="/ModuleColAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input id="operation" type="hidden" name="operation" value="$globals.getOP("OP_ADD")">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="moduleId" value="">
<input type="hidden" name="moduleName" value="">
<input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get('com.add.module.name')</div>
	<ul class="HeadingButton">
		 <li>
		 #if($globals.getMOperation().add())
		 <button type="submit" class="b2">$text.get("mrp.lb.save")</button>
		 #end
		 <button type="button" onClick="location.href='/ModuleColAction.do?operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button>
		 </li>
	</ul>
</div>

<div id="listRange">
		<div class="listRange_1">
			<li><span> $text.get("com.module.name")：</span>
				<input type="text" id="moduleDisplay" name="moduleDisplay" onClick="selectModuleName();" style="50px; background:url(/$globals.getStylePath()/images/St.gif) no-repeat bottom right;cursor:pointer" readonly="readonly">
			</li>
			<li id="addNewField" style="display: none">
				<span><button class="b4" onClick="selectModuleColName();">$text.get("com.add.field")</button></span>
			</li>
		</div>
		<div id="show_$row.getId().trim()">
		<div class="scroll_function_small_a" id="conter">
		<script type="text/javascript">
			var oDiv=document.getElementById("conter");
			var sHeight=document.body.clientHeight-65;
			oDiv.style.height=sHeight+"px";
		</script>
				<table id="myTable" width="630" border="0" cellpadding="0" cellspacing="0" class="listRange_list">
				<thead>
					<tr style="text-align:center;" id="kkkkkkk" class="scrollColThead">
						<td class="listheade" width="20" align="center">
							<span style="vertical-align:middle;"><IMG
									src="/$globals.getStylePath()/images/down.jpg" border=0 />
							</span>
						</td>
						<td width="150">$text.get("tableInfo.lb.tabeDisplayName")</td>
						<td width="180">$text.get("com.popup.field.name")</td>
						<td width="80">$text.get("com.popup.type")</td>
						<td width="200">$text.get("customTable.lb.moreLan")</td>
						<td width="40" >$text.get("common.lb.del")</td>			
					</tr>
				</thead>
				<tbody id="mainTable">				
					<tr style="text-align:center;">										
					</tr>  
				</tbody>			  
			</table>
		    </div>
	</div>	
</div>
</div>
</div>
</form>
	
</body>
</html>

