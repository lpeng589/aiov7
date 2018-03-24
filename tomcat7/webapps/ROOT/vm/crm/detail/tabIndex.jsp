





<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("common.lb.detail")</title>
</head>
<input type="hidden" id="clientId" name="clientId" value="$!keyId">
#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=$!tableName").update() && "$!public"=="")
#set ($operation="7")
#else
#set ($operation="5")
#end
#if("$!showClose"=="false")
 #set($isClose="false")
#else
 #set($isClose="true")
#end
<frameset rows="28,100%" cols="*" frameborder="no" border="0" framespacing="0">
  <frame src="/CrmTabAction.do?tableName=$!tableName&tabType=tab&showClose=$!isClose&keyId=$keyId&designId=$!designId&winCurIndex=$!winCurIndex&public=$!public" name="moddiFrame" scrolling="No" noresize="noresize" id="moddiFrame" />
  <frame src="/UserFunctionAction.do?tableName=$!tableName&operation=$!operation&keyId=$keyId&fromCRM=detail&tabList=Y&designId=$!designId&winCurIndex=$!winCurIndex" name="bottomFrame" scrolling="No" noresize="noresize" id="bottomFrame" title="bottomFrame" />
</frameset><noframes></noframes>
</html>
