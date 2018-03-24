<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("tableInfo.title")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript">

#set($scopeFlag = $request.getParameter("flag"))
function disclose(tableName,dis){
	document.form.action = "/vm/roleAdmin/fieldSelect.jsp?flag=$!scopeFlag&tableName="+tableName;
	document.form.submit();
}

function submitQuery(){
	document.form.action="/vm/roleAdmin/mainTableSelect.jsp?flag=$!scopeFlag";
	document.form.submit();
}


function retdisclose(tableName,tableDisplay,fieldName,fieldDisplay){
    var returnValue = tableName+";"+fieldName+";"+tableDisplay+";"+fieldDisplay;
    window.parent.exeTableSelect(returnValue);
    window.parent.jQuery.close('TableSelect');
}
</script>
</head>
<body onLoad="showtable('tblSort');showStatus(); " scroll="no">

<form  method="post" scope="request" name="form" action="/vm/roleAdmin/mainTableSelect.jsp?flag=$!scopeFlag">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
 <input type="hidden" name="dis" value="$!request.getParameter("tableName")">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitlesmall">$text.get("tip.plese.select") $text.get("tableInfo.lb.tabeDisplayName")</div>
	<ul class="HeadingButton_Pop-up">
	#if($request.getParameter("flag") != 0)
		<li><button type="button" onClick="submitQuery();" class="b2">$text.get("common.lb.query")</button></li>	
	#end
	<li><button type="button" onClick="javascript:window.parent.jQuery.close('TableSelect');">$text.get("common.lb.close")</button></li>
	</ul>	
</div>
<div class="listRange_Pop-up">
	<ul class="listRange_1_Pop-up">
	#if($request.getParameter("flag") != 0)
	<li>
			$text.get("oa.common.keyWord")：<input type="text"  name="tableName" value="$!request.getParameter("tableName")">	
			</li>
	#end
</ul>
<div class="listRange_Pop-up_scroll" style="height:230px;">
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list" name="table" id="tblSort">
		<thead>
			<tr>				
				<td>$text.get("tableInfo.lb.tabeDisplayName")</td>
			</tr>			
		</thead>
		<tbody>
		#if($request.getParameter("flag")==0)
		#foreach ($row in $scopeCata )	
			<tr onDblClick="retdisclose('$globals.get($row,0)','$globals.get($row,3).get($globals.getLocale())','classCode','')">
				<td align="left">$globals.get($row,3).get($globals.getLocale()) &nbsp;</td>			
			</tr>
		#end
		#end
		
		#if($request.getParameter("flag") != 0)
		#set($qua=$request.getParameter("tableName"))
		#if($qua)	
			#foreach ($row in $scopeModule.values() )	
				#if($globals.get($row,0).get($globals.getLocale()).indexOf($!qua)>=0&&$globals.get($row,1) ==$!LoginBean.defSys)
		<tr onDblClick="disclose('$globals.get($row,3)','')">
		<td align="left">$globals.get($row,0).get($globals.getLocale()) &nbsp;</td>			
		</tr>
				#end
			#end
		#else
			#foreach ($row in $scopeModule.values())			
			#if($globals.get($row,1) ==$!LoginBean.defSys)
		<tr onDblClick="disclose('$globals.get($row,3)','')">
		#if($globals.get($row,0).get($globals.getLocale()).length()>0)
		<td align="left">$globals.get($row,0).get($globals.getLocale()) &nbsp;</td>	
		#else
		<td align="left">$globals.get($row,3) &nbsp;</td>	
		#end		
		</tr>
			#end
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
