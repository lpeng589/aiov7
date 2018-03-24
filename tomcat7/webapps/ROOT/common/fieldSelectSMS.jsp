<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("tableInfo.title")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script language="javascript">
function disclose(tableName,tableDisplay,fieldName,fieldDisplay){
    window.parent.returnValue = fieldDisplay;
	
    window.close();

}
function returnMain(){
#if($request.getParameter("dis"))
	document.form.action = "/common/mainTableSelect.jsp?flag=1";
#else
	document.form.action = "/common/mainTableSelect.jsp?flag=1";
#end
document.form.submit();
}
</script>
</head>

<body onLoad="showtable('tblSort');showStatus();" scroll="no">

<form  method="post" scope="request" name="form" action="/CustomQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
  <input type="hidden" name="winCurIndex" value="$!winCurIndex">
  <input type="hidden" name="tableName" value="$!request.getParameter("dis")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitlesmall">$text.get("tip.plese.select") $text.get("customTable.lb.fieldName")</div>
	<ul class="HeadingButton_Pop-up">
			<li><button type="button" onClick="javascript:window.close();" class="b2">$text.get("common.lb.close")</button></li>	
	</ul>
</div>
<div class="listRange_Pop-up_scroll">
			
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list" name="table" id="tblSort">
		<thead>
			<tr>
				<td>$text.get("customTable.lb.fieldName")</td>
			</tr>			
		</thead>
		<tbody>		
		
		
	
		#if($globals.get($scopeModule.get($request.getParameter("tableName")),4)=="1")
		   #foreach ($row in $TABLE_INFO.get($request.getParameter("tableName")).fieldInfos )
		  		#if("$row.fieldName" != "id" && "$row.fieldName" != "f_ref" && "$row.inputType"!="3" && "$row.inputType"!="100"
												&& "$!row.display.get($globals.getLocale())"!="")
			<tr ondblclick="disclose('$request.getParameter("tableName")','$TABLE_INFO.get($request.getParameter("tableName")).display.get($globals.getLocale())','$row.fieldName','$row.display.get($globals.getLocale())')">
				<td align="left">$!row.display.get($globals.getLocale()) &nbsp;</td>			
			</tr>
				#end
			#end
			
		#end

		</tbody>

		</table>
	</div>
</form>
</body>
</html>
