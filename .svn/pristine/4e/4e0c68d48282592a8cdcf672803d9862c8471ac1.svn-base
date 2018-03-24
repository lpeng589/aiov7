<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get(&quot;tableMap.lb.title&quot;)</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/userManage.js"></script>
<script type="text/javascript" src="/js/validate.vjs"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">


var currentElement = "";
var paramNum = $tableMapList.size();
function change(type,num){
	var row = 0;
	paramNum = num;
	var mostlyTable = document.getElementById("mostlytable").value;
	mostlyTableField = document.getElementById(mostlyTable).value;
	var childTable = document.getElementById("childtable").value;
	childTableField = document.getElementById(childTable).value;
	items = document.getElementsByTagName("input");
					
	for(var i = 0 ; i < paramNum; i ++){
			
		var temp = document.getElementById("param_" + i);
		if(temp != null){
			temp.id = "";
			temp.name = "";
			temp.value = "";
			if(temp.target == "parameter"){	
				document.getElementById("form").removeChild(temp);
			}
		}
	}
	paramNum = 0;
	var tab = document.getElementById("myTable");
	var rowList = tab.rows;
	for(var i = rowList.length - 1; i > 0; i --){
		// 清空主table
		tab.deleteRow(i);
	}
	if(mostlyTableField != "" && childTableField != "")	{
		mostlyField = mostlyTableField.split("@");
		childField  =  childTableField.split("@");
		outter:
		for(var i = 0 ; i < childField.length; i ++){
			field = "";
			display = "";
			if(childField[i].trim() != "" ){
				field = childField[i].split("-")[0];
				display = childField[i].split("-")[1];
				//if(field != "id"){
					var tr = document.createElement("tr");
					tr.id = "row_" + row;
					tr.style.textAlign = "center";
					var mostlyTd = document.createElement("td");
					mostlyTd.id = "mostly_tempField_" + row;					
					mostlyTd.innerHTML = "&nbsp;";
					mostlyTd.align="left";
					
					var childTd = document.createElement("td");
					childTd.id =  "child_" +field+ "_" + row;
					childTd.attachEvent("ondblclick",updateFeild);
					var delTd = document.createElement("td");
					delTd.style.cursor="pointer";
					delTd.id = "delete_" + row;
					delTd.innerHTML = '$text.get("common.lb.del")';
					delTd.attachEvent("onclick",delRow);
					tr.appendChild(mostlyTd);
					childTd.innerHTML = field+"("+display+")";
					childTd.align="left";
					tr.appendChild(childTd);
					tr.appendChild(delTd);
					document.getElementById("mainTable").appendChild(tr);
					var requestParam = document.createElement("input");
					paramNum ++;
					requestParam.id = "param_" + row;
					requestParam.target = "parameter";
					requestParam.type = "hidden";
					requestParam.value = field + "#";
					document.getElementById("form").appendChild(requestParam);
					row ++;
					inner:
					for(var j = 0 ; j < mostlyField.length; j ++){
						tempField = "";
						tempDisplay = "";
						if(mostlyField[j].trim() != ""){
							tempField = mostlyField[j].split("-")[0];
							tempDisplay  = mostlyField[j].split("-")[1];
							if(field == tempField ){//&& field != "id"				
								mostlyTd.id =  "mostly_" + tempField + "_" + row;
								mostlyTd.innerHTML = tempField+"("+tempDisplay+")";
								requestParam.name = "fieldMapping";
								requestParam.value += tempField;
								continue outter;
							}
						}
					}
			//	}
			} 
		}
	}
}

function updateFeild(){

	elementId = event.srcElement.id;
	currentElement = event.srcElement;
	innerHTML = currentElement.innerHTML;
	document.getElementById(elementId).innerHTML = "&nbsp;";
	var tempSelect = document.createElement("select");
	tempSelect.id = "temp";
	tempSelect.onblur=function(){save(this)};
	var fields = "";
	if(elementId.split("_")[0] == "mostly"){
		fields = mostlyTableField;
		if(fields == ""){
			alert('$text.get("tableMap.lb.selectSourMsg")');
			return;
		}
	}
	if(elementId.split("_")[0] == "child"){
		fields = childTableField;
		if(fields == ""){
			alert('$text.get("tableMap.lb.selectTargetMsg")');
			return;
		}
	}
	fieldList = fields.split("@");
	for(var i = 0 ; i < fieldList.length; i ++){
		if(fieldList[i].trim() != ""){
			var tempOption = document.createElement("option");
			tempOption.value = fieldList[i].split("-")[0];
			tempOption.innerHTML = fieldList[i].split("-")[0]+"("+fieldList[i].split("-")[1]+")";
			if(innerHTML == tempOption.innerHTML){
				tempOption.selected = true;
			}
			tempSelect.appendChild(tempOption);
		}
	}
	document.getElementById(elementId).appendChild(tempSelect);
}

function save(obj){
	elementId = obj.id;
	value = obj.value;
	var ops = obj.options;
	var content = "";
	for(var i = 0 ; i < ops.length; i ++){
		if(ops[i].value == value){
			content = ops[i].innerHTML;
		}
	}	
	
	currentElement.removeChild(obj);
   //
	    var lastIndex=currentElement.id.lastIndexOf("_");
	    var index=currentElement.id.substring(lastIndex+1);
		//
	currentElement.id = currentElement.id.split("_")[0] + "_" + value + "_" + index;
	currentElement.innerHTML = content; 
	  
	if(currentElement.id.split("_")[0] == "mostly"){
	   
		tempVlaue = document.getElementById("param_" +index).value;
		document.getElementById("param_" + index).value = value + "#" 
			+ tempVlaue.split("#")[1];
	}else if(currentElement.id.split("_")[0] == "child"){
		tempVlaue = document.getElementById("param_" + index).value;
		document.getElementById("param_" + index).name = "fieldMapping";
		document.getElementById("param_" + index).value = tempVlaue.split("#")[0] + "#" 
			+ value;
	}
	
}

function update(a){
	updateFeild();	
}

function delRow(){
	var tab = document.getElementById("mainTable");
	var tr = document.getElementById("row_" + event.srcElement.id.split("_")[1]);
	var temp = document.getElementById("param_" + event.srcElement.id.split("_")[1]);
	if(temp != null){
		temp.id = "";
		temp.name = "";
		temp.value = "";	
		if(temp.target == "parameter"){	
			document.getElementById("form").removeChild(temp);
		}
	}
	tab.removeChild(tr);	
}
function addRow(){
	var tab = document.getElementById("mainTable");
	var maxID =0;
	$("tr[id*=row_]").each(function(){
		var mid = $(this).attr("id");
		mid = mid.substring(4);
		if(Number(mid) > maxID){
			maxID = Number(mid);
		}
	});
	maxID ++;
	$("#mainTable").append('<tr id="row_'+maxID+'">\
			<td id="mostly_$avlue_'+maxID+'" onDblClick="update(mostlyTableField);">&nbsp;</td>\
			<td id="child_tempvalue_'+maxID+'" onDblClick="update(childTableField);">&nbsp;</td>	\
			<td align="center" id="delete_'+maxID+'" onClick="delRow();" style="cursor:pointer;color:blue;">$text.get("common.lb.del")</td>\
			</tr>\
			<input type="hidden" value="#" name="fieldMapping" id="param_'+maxID+'"/>');
}

</script>
</head>

<body>

<iframe name="formFrame" style="display:none"></iframe>
<form id="form" method="post" scope="request" name="form" action="/tableMappedAction.do" target="formFrame">
<input id="operation" type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="winCurIndex" value="$!winCurIndex">
<!--<input type="hidden" name="createBy" value="$result.createBy">
<input type="hidden" name="id" value="$result.id">-->
	#set($mostlyTableField = "")
	#set($childTableField = "")
	#foreach($table in $fieldMap.keySet())
		
		#set($paramvalue = "")
		#foreach($row in $fieldMap.get($table))
		#set($value = "")
		#set($content = "")
		#set($num = 0)
		#foreach($str in $row)
			#set($num = $num + 1)
			#if($num == 1)
				#set($value = $str)
			#end
			#if($num == 2)
				#set($content = $str)
			#end			
		#end
		#set($paramvalue = $paramvalue +( $value + "-" + $content + "@"))
		#end
		#if($table == $mostly)
			#set($mostlyTableField = $paramvalue)
		#end
		#if($table == $child)
			#set($childTableField = $paramvalue)
		#end
		<input id="$table" value="$paramvalue" type="hidden">	
	#end	
<div class="Heading">
	<div class="HeadingTitle">$text.get("tableMap.lb.title")</div>
	<ul class="HeadingButton">
		 
		 #if($LoginBean.operationMap.get("/tableMappedQueryAction.do").update() || $!LoginBean.id=="1")
		 <li><button type="submit" class="b2">$text.get("mrp.lb.save")</button></li>
		 #end
		 #if($LoginBean.operationMap.get("/tableMappedQueryAction.do").query() || $!LoginBean.id=="1")
		 <li><button type="button"    onClick="location.href='/tableMappedQueryAction.do?operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button></li>
		 #end
		 
	</ul>
</div>

<div id="listRange" style="padding:0 10px;">
			<div class="listRange_1">
				<li style="width:420px"><span style="width:60px;">$text.get("tableMap.lb.source")：</span>
					<select name="mostlytable" id="mostlytable" onChange="change('mostly',$rowNum);" style="width:355px">
						<option>--------$text.get("tableMap.lb.selectSource")--------</option>
#set($mostlytable = "")
#set($sum = 0)
#foreach($row in $tableList)
	#set($sum = $sum + 1)#set($value = "")#set($content = "")#set($num = 0)
	#foreach($str in $row)
		#set($num = $num + 1)
		#if($sum == 1)
			#if($num == 1)
				#set($mostlytable = $str)
			#end	
		#end
		#if($num == 1)
			#set($value = $str)
		#end
		#if($num == 2)
			#set($content = $str)
		#end
	#end
	#if($value == $mostly)
		#set($selected = "selected")
		#else
		#set($selected = "")
	#end
	<option value="$value" $!selected>$value($content)</option>
#end</select>
					</li>
				<li style="width:420px"><span style="width:60px">$text.get("tableMap.lb.target")：</span>
					<select name="childtable" id="childtable" onChange="change('child',$rowNum);" style="width:355px">
					<option>--------$text.get("tableMap.lb.selectTarget")--------</option>
#set($childtable = "")
#set($sum = 0)
#foreach($row in $tableList)
	#set($sum = $sum + 1)#set($value = "")#set($content = "")#set($num = 0)
	#foreach($str in $row)
		#set($num = $num + 1)
		#if($sum == 1)
			#if($num == 1)
				#set($childtable = $str)
			#end	
		#end
		#if($num == 1)
			#set($value = $str)
		#end
		#if($num == 2)
			#set($content = $str)
		#end
	#end
	#if($value == $child)
		#set($selected = "selected")
		#else
		#set($selected = "")
	#end
	<option value="$value" $!selected>$value($content)</option>
#end</select>				</li>
			</div>
			<div id="show_$row.getId().trim()">
			<div class="scroll_function_small_a" id="conter">
						<script type="text/javascript">
var oDiv=document.getElementById("conter");
var dHeight=document.documentElement.clientHeight-70;
oDiv.style.height=dHeight+"px";
</script>
				<table id="myTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="listRange_list">
				<thead>
				<tr style="text-align:center;" id="kkkkkkk">
					<td>$text.get("tableMap.lb.sourceField")</td>	
					<td>$text.get("tableMap.lb.targetField")</td>
					<td width="50" align="center" onClick="addRow();" style="cursor:pointer;color:blue;">添加</td>			
				</tr>
				</thead>
				<tbody id="mainTable">
					#set($rowNum = 0)	
					#foreach($row in $tableMapList)
						#set($avlue = $row.getMostlyTableField())
						#set($childTd = $row.getChildTableField())
						#set($content = "")
						#set($childContent = "&nbsp;")
						
						#foreach($tvalue in $fieldMap.get($mostly))
							#set($tempvalue = "")
							#set($tcontent = "")
							#set($tnum = 0)
							#foreach($tstr in $tvalue) 
								#set($tnum = $tnum + 1)
								#if($tnum == 1)
									#set($tempvalue = $tstr)
								#end
								#if($tnum == 2)
									#set($tcontent = $tempvalue+"("+$tstr+")")
								#end			
							#end
							#if($tempvalue == $avlue)
								#set($content = $tcontent)
							#end
						#end
						
						#foreach($tvalue in $fieldMap.get($child))
							#set($tempvalue = "")
							#set($tcontent = "")
							#set($tnum = 0)
							#foreach($tstr in $tvalue) 
								#set($tnum = $tnum + 1)
								#if($tnum == 1)
									#set($tempvalue = $tstr)
								#end
								#if($tnum == 2)
									#set($tcontent = $tempvalue+"("+$tstr+")")
								#end			
							#end
							#if($tempvalue == $childTd)
								#set($childContent = $tcontent)
							#end
						#end
						#set($flag = false)
							
						<tr id="row_$rowNum">
						<td id="mostly_$avlue_$rowNum" onDblClick="update(mostlyTableField);">$content</td>
						<td id="child_tempvalue_$rowNum" onDblClick="update(childTableField);">$childContent</td>	
						<td align="center" id="delete_$rowNum" onClick="delRow();" style="cursor:pointer;color:blue;">$text.get("common.lb.del")</td>									
						</tr>						
						<input type="hidden" value="$avlue#$childTd" name="fieldMapping" id="param_$rowNum"/>
						#set($rowNum = $rowNum + 1)			
							
					#end
				</tbody>			  
			</table>
		    </div>
			</div>
			<input type="hidden" name="oldMostlyChild" value="$mostly@$child"/>
		</div>
</div>
</div>
</form>
<script type="text/javascript">
	var mostlyTableField = document.getElementById("$mostly").value;
	var childTableField = document.getElementById("$child").value;
</script>
</body>
</html>
