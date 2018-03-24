<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<style type="text/javascript" src="/js/function.js"></style>
<title>button</title>
</head>
<body>
<div class="Heading">
	<div class="HeadingIcon"></div>
	<ul class="HeadingButton">	
		<li><button onclick="closeWin();">$text.get("common.lb.close")</button></li>																														
	</ul>
</div>
	<div id="listRange_id" style="padding-left:15px;">
<table border="0" width="100%" cellpadding="0" cellspacing="0" class="listRange_list_function" style=" margin-top:3px;" name="table">
<thead>
<tr>
<td colspan="2">$text.get("common.lb.relatedAbstract")</td>
</tr>
</thead>
<tbody>
#foreach($strName in $stateMap.keySet())
<tr>
	<td>$text.get($strName): </td>
	<td><font color="red">￥$stateMap.get($strName)</font></td>
</tr>
#end
</tbody>
</table>
<table border="0" width="100%" cellpadding="0" cellspacing="0" class="listRange_list_function" style="margin-top:8px" name="table">
<thead>
<tr>
<td>$text.get("common.lb.transactionAccount")</td>
</tr>
</thead>
<tbody>
<tr>
<td>&nbsp;</td>
</tr>
</tbody>
</table>
</div>
</body>
</html>
