<!DOCTYPE html >
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("com.colName.update")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" / >
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>

<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script> 
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<script language="javascript">
function m(obj){
	return document.getElementById(obj);
}

function beforeSubmit(){
	
	var userLanguage ="";
	isError = false;
	$("input[name=fieldDis]",document).each(function(){
		id = $(this).attr("id");
		oldValue =  $(this).attr("oldValue");
		value = $(this).val();
		if(!isError && value != oldValue){
			if(!containSC(value)){
				alert("列名不可以包含特殊字符");
				isError = true;			
			}
			userLanguage+=id+"@"+value+"#" ;
		}
	});
	if(isError){
		return;
	}
	if(userLanguage == ""){
		alert("未修改任何字段");
		return;
	}
	m("userLanguage").value=userLanguage ;
	form.submit() ;
}
</script>
</head>

<body onLoad="showtable('tblSort');showStatus(); ">
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/ColDisplayAction.do" target="formFrame">
 <input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
 <input type="hidden" name="winCurIndex" name=""/>
 <input type="hidden" name="search" value="$!search">
 <input type="hidden" name="popWinName" value="$!popWinName">
 <input type="hidden" name="tableName" value="$!result.tableName" />
 <input type="hidden" id="userLanguage" name="userLanguage" value="" />
 <input type="hidden" id="fromConfig" name="fromConfig" value="$!fromConfig" />
<div class="Heading">
	
	<ul class="HeadingButton">
	<li><button type="button" onClick="beforeSubmit();" class="b2">$text.get("mrp.lb.save")</button></li>
		#if("$fromConfig"=="config")
		<li><button type="button" onClick="closeWin('$!popWinName');" class="b2">$text.get("common.lb.close")</button></li>
		#else
		<li><button type="button" onClick="location.href='/ColDisplayAction.do?operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex&search=$globals.encode($!search)&LinkType=@URL:'" class="b2">$text.get("common.lb.back")</button></li>
		#end
		
	</ul>	
</div>
<div id="listRange_id">
	<div class="scroll_function_small_a" id="conter">
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort" style="width:550px;">
		<thead>
			<tr>
				<td width="150" align="center">表名</td>
				<td width="180" align="center">字段名</td>
			</tr>			
		</thead>
		<tbody>
		#foreach ($col in $result.fieldInfos )
			#if("$!col.languageId"!="" && "$col.inputType"!="3" && "$col.inputType"!="100")
			#set($filedLan=$!col.fieldName+"@"+$result.tableName)
			<tr>
				<td align="left">$!result.display.get("$globals.getLocale()")</td>
				<td align="left">
					<input id="$filedLan" name="fieldDis" type="text" oldValue="$!col.display.get("$globals.getLocale()")" value="$!col.display.get("$globals.getLocale()")">
				</td>
			</tr>
			#end
		#end
		#foreach($table in $childList)
		#foreach ($col in $table.fieldInfos )
			#if("$!col.languageId"!="" && "$col.inputType"!="3" && "$col.inputType"!="100")
			#set($filedLan=$!col.fieldName+"@"+$table.tableName)
			<tr>
				<td align="left">$!table.display.get("$globals.getLocale()")</td>
				<td align="left">
					<input id="$filedLan"  name="fieldDis" type="text" oldValue="$!col.display.get("$globals.getLocale()")"  value="$!col.display.get("$globals.getLocale()")">
				</td>
			</tr>
			#end
		#end
		#end
		</tbody>
		</table>
	  </div>
</form>
<script type="text/javascript">
//<![CDATA[
$("#conter").height($(window).height()-42);
//]]>
</script>
</body>
</html>
