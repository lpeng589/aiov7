<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
<title >$text.get("tableInfo.title")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript">

function submitQuery(){
	form.submit();
}

function dbtable(id){
	jQuery("#selectType").val('queryField');
	jQuery("#tableId").val(id);
	form.submit();
}

function loads(){
	jQuery("#searchName").focus();
}
function closeDiv(){
	parent.jQuery.close('Popdiv');
}
</script>
</head>
<body onload="loads()" scroll="no">

<form  method="post" scope="request" name="form" action="/BillNo.do">
 <input type="hidden" name="selectType" id="selectType" value="queryTable">
 <input type="hidden" name="tableId" id="tableId" value="">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitlesmall">$text.get("tip.plese.select") $text.get("tableInfo.lb.tabeDisplayName")</div>
	<ul class="HeadingButton_Pop-up">
	<li><button type="button" onClick="submitQuery();" class="b2">$text.get("common.lb.query")</button></li>
	<li><button type="button" onClick="closeDiv();">$text.get("common.lb.close")</button></li>
	</ul>	
</div>
<div class="listRange_Pop-up">
	<ul class="listRange_1_Pop-up">
		<li>
			$text.get("oa.common.keyWord")：<input type="text" id="searchName" name="searchName" value="$!searchName" onkeydown="if(event.keyCode==13){submitQuery();}">	
		</li>
	</ul>
	<div class="listRange_Pop-up_scroll"  style="height: 280px;width:98%">
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list" name="table" id="tblSort" >
		<thead>
			<tr>				
				<td>$text.get("tableInfo.lb.tabeDisplayName")(中文)</td>				
				<td>表名(英文)</td>
			</tr>			
		</thead>
		<tbody>
		#foreach ($row in $tableList)	
			<tr onDblClick="dbtable('$globals.get($row,0)')">
				<td align="left">$globals.get($row,1) &nbsp;</td>
				<td align="left">$globals.get($row,2) &nbsp;</td>	
			</tr>
		#end
		</tbody>
		</table>
	</div>
</div>
</form>
</body>

</html>

