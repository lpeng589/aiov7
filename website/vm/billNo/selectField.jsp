<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("tableInfo.title")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript">
function saveFields(){
	var thechbox=document.getElementsByName("keyId");
	var value="";
	for(var i=0;i<thechbox.length;i++){
		if(thechbox[i].checked){
			value = thechbox[i].value;
		}
	}
	if(value==""){
		alert("请选择一项！");
		return false;
	}
	parent.jQuery.opener('billDiv').evaluate(value);
	closeDiv();
}
function returnTable(){
	jQuery("#selectType").val('queryTable');
	form.submit();
}

function dblSelect(object){
	jQuery(object).find("input[name=keyId]").attr("checked","true");
	saveFields();
}

function closeDiv(){
	parent.jQuery.close('Popdiv');
}
</script>
</head>

<body scroll="no">
<form  method="post" scope="request" name="form" action="/BillNo.do">
<input name="selectType" id="selectType" type="hidden" value="queryField">
<input type="hidden"  name="searchName" value="$!searchName">	
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitlesmall">$text.get("tip.plese.select") $text.get("customTable.lb.fieldName")</div>
	<ul class="HeadingButton_Pop-up">
		<li><button type="button" onClick="saveFields();" class="b2">$text.get("common.lb.ok")</button></li>
		<li><button type="button" onClick="returnTable();" class="b2">$text.get("common.lb.back")</button></li>	
		<li><button onClick="closeDiv();">$text.get("common.lb.close")</button></li>
	</ul>
</div>
<div class="listRange_Pop-up">
	<ul class="listRange_1_Pop-up">
		<li>
			
		</li>
	</ul>
	<div class="listRange_Pop-up_scroll"  style="height: 350px;">
			
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list" name="table" id="tblSort">
			<thead>
				<tr>
					<td>&nbsp;
					</td>
					<td>$text.get("tableInfo.lb.tabeDisplayName")(中文)
					</td>
					<td>$text.get("customTable.lb.fieldName")(中文)</td>
					<td>字段名(英文)</td>
				</tr>			
			</thead>
			<tbody>	
				#foreach ($row in $fields)
					<tr ondblclick="dblSelect(this)">
						<td><input type="radio" name="keyId" id="keyId" value="$globals.get($row,0);$globals.get($row,1);$globals.get($row,2);$globals.get($row,3)" style="height: 20px;width: 20px;"/></td>
						<td align="left">$globals.get($row,0) &nbsp;</td>
						<td align="left">$globals.get($row,1) &nbsp;</td>
						<td align="left">$globals.get($row,3) &nbsp;</td>	
					</tr>
				#end
			</tbody>
		</table>
	</div>
</div>
</form>
</body>
</html>
