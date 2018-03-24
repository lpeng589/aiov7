<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("tableInfo.title.detail")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
</head>
<body onLoad="showtable('tblSort');showStatus(); ">
<form  method="post" scope="request" name="form" action="/CustomQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
  <input type="hidden" name="winCurIndex" value="$!winCurIndex">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("tableInfo.title.detail")</div>
	<ul class="HeadingButton">
		<li><button type="button" onClick="location.href='/CustomQueryAction.do?winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button></li>
	</ul>
</div>
<div id="listRange_id" align="center">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
	<ul class="listRange_1">
			
		<li><span>$text.get("customTable.lb.tableName")：</span><input type="text" name="tableName" value="$result.display.get($globals.getLocale())"></li>	
		<li><span>$text.get("tableInfo.lb.tableName")：</span><input type="text" name="tableName" value="$result.tableName"></li>
		<li><span>$text.get("customTable.lb.tableType")： </span><input type="text" name="tableName" value="$globals.getDSByValue("tableType","$result.tableType")
	   #if("$result.tableType" == 1)
	      $globals.getDSByValue("tableType","$result.perantTableId")
	   #end"></li>		 
	</ul>
    <div class="listRange_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list" name="table" id="tblSort">
		<thead>
			<tr>
				
				<td width="10%" align="center" valign="middle" >$text.get("customTable.lb.fieldName")</td>
				<td width="15%" align="center" valign="middle" >$text.get("customTable.lb.moreLan")</td>
				<td width="6%" align="center" valign="middle" >$text.get("customTable.lb.isNULL")</td> 
				<td width="6%" align="center" valign="middle" >$text.get("customTable.lb.maxLength")</td> 
				<td width="6%" align="center" valign="middle" >$text.get("customTable.lb.defaultValue")</td> 
				<td width="6%" align="center" valign="middle" >$text.get("customTable.lb.listOrder")</td>  
				<td width="6%" align="center" valign="middle" >$text.get("customTable.lb.fieldType")</td> 
				<td width="6%" align="center" valign="middle" >$text.get("customTable.lb.inputType")</td> 
				<td width="11%" align="center" valign="middle" >$text.get("customTable.lb.inputValue")</td> 
			</tr>
		</thead>
		<tbody>
			#foreach ($row in $result.fieldInfos )
			#if($row.fieldName != "id" && $row.fieldName != "f_ref" )
                <tr> 				
                  <td >$!row.fieldName &nbsp;</td> 
				  <td >$!row.display.get($globals.getLocale()) &nbsp;</td> 
				  <td align="center">$globals.getDSByValue("nullType","$row.isNull")</td> 
				  <td align="center">$row.maxLength</td> 
				  <td align="center">$!row.defaultValue &nbsp;</td> 
				  <td align="center">$row.listOrder</td> 
				  <td align="center">$globals.getDSByValue("fieldType","$row.fieldType")</td> 
				  <td align="center">$globals.getDSByValue("inputType","$row.inputType")</td> 
				  <td >$!row.inputValue &nbsp;</td> 
                </tr> 
			#end
			#end
		</tbody>
		</table>
	</div>
	 <div class="listRange_1_photoAndDoc_1">
	 	  <span>$text.get("customTable.lb.dbOperation")：</span>
	 	  <textarea name="fieldCalculate" rows="4"  readonly>$!result.fieldCalculate</textarea>
	</div>	
	<div class="listRange_1_photoAndDoc_1">
          <span>$text.get("customTable.lb.extendButton")：</span>
          <textarea name="extendButton" rows="4">$!result.extendButton</textarea>
	</div>
	<div class="listRange_1_photoAndDoc_1"><span>$text.get("oa.common.remark")：</span><textarea name="tableDesc" rows="4">$!result.tableDesc</textarea>
		 </div>
</div>
</form>
</body>
</html>
