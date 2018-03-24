<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("common.lb.detail")</title>
</head>

<input type="hidden" name="f_brother" value="$!keyId">
<input type="hidden" id="displayType" value=""/>
#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=CRMClientInfo").update() && "$!public"=="")
#set ($operation="7")
#else
#set ($operation="5")
#end

<frameset  id="crmFramset" rows="108%,25,90%" cols="*" frameborder="no" border="0" framespacing="0">
  <frame src="/ClientAction.do?operation=4&type=main&public=$!public&src=$!src" name="topFrame" scrolling="No" noresize="noresize" id="topFrame"/>
  <frame src="/CrmTabAction.do?tableName=CRMClientInfo&tabType=tab&winCurIndex=$!winCurIndex&public=$!public" name="moddiFrame" scrolling="No" noresize="noresize" id="moddiFrame" />
  <frame src="" name="bottomFrame" scrolling="No" noresize="noresize" id="bottomFrame" title="bottomFrame" />
</frameset><noframes></noframes>
</html>
