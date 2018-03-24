



<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$reportName</title>
</head>
<input type="hidden" id="displayType" value=""/>
<frameset id="crmFramset" rows="100%,25,0" frameborder="no" border="0" framespacing="0">
  <frame src="/UserFunctionQueryAction.do?tableName=$!request.getParameter("tableName")&nextNodeIds=$!request.getParameter("nextNodeIds")&currNodeId=$!request.getParameter("currNodeId")&keyId=$!request.getParameter("keyId")&operation=$globals.getOP("OP_UPDATE_PREPARE")&winCurIndex=$!request.getParameter("winCurIndex")&designId=$!request.getParameter("designId")&oaTimeLimit=$!request.getParameter("oaTimeLimit")&oaTimeLimitUnit=$!request.getParameter("oaTimeLimitUnit")" scrolling="No" noresize="noresize" id="topFrame"/>
  #if("$!winCurIndex"!="")
  <frame src="/crmUserFunctionAction.do?tableName=$!request.getParameter("tableName")&f_brother=$!request.getParameter("keyId")&tabIndex=$!tabIndex&winCurIndex=$!request.getParameter("winCurIndex")" name="moddiFrame" scrolling="No" noresize="noresize" id="moddiFrame" />
  #else
  <frame src="/crmUserFunctionAction.do?tableName=$!request.getParameter("tableName")&f_brother=$!request.getParameter("keyId")&winCurIndex=$!request.getParameter("winCurIndex")" name="moddiFrame" scrolling="No" noresize="noresize" id="moddiFrame" />
  #end
  <frame src="/UserFunctionAction.do?tableName=$!request.getParameter("tableName")&operation=5&keyId=$!request.getParameter("keyId")&tabList=Y&winCurIndex=$!request.getParameter("winCurIndex")" name="bottomFrame" scrolling="no" noresize="noresize" id="bottomFrame"/>
</frameset>
<noframes>
</noframes>

</html>
