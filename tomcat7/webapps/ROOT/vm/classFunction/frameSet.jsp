<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$reportName</title>
<script type="text/javascript" src="/js/jquery.js"></script>
<link type="text/css" rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />

<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
</head>
<frameset cols="190px,*" style="border:none;padding:0px;margin:0px" >
  <frame id="leftTreeFrame" name="leftTreeFrame" src="/TreeAction.do?tableName=$tableName&moduleType=$!moduleType&SysType=$!SysType&MOID=$!MOID&editable=$!editable&subSql=$!subSql" style="border:none;padding:0px;margin:0px" />
  <frame id="mainTreeFrame" name="mainTreeFrame" src="$!mainSrc" style="border:none;padding:0px;margin:0px" scrolling="no" />
</frameset>
</html>
