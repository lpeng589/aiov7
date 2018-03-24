<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$reportName</title>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
</head>
<input type="hidden" id="displayType" value=""/>
<frameset cols="100px,*">
  <frame src="/crmUserFunctionAction.do?tableName=$!tableName&tabIndex=$!tabIndex&f_brother=$!keyId&winCurIndex=$!winCurIndex&popWinName=$!popWinName" />
  <frame id="bottomFrame" src="/UserFunctionAction.do?tableName=$!tableName&operation=5&keyId=$!keyId&checkTab=Y&winCurIndex=$!winCurIndex&popWinName=$!popWinName" />
</frameset>
</html>
