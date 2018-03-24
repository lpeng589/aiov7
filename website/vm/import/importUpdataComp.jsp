<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("excel.lb.addModel")</title>
<link type="text/css" rel="stylesheet" href="/style/css/about.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript">
if(typeof(top.junblockUI)!="undefined"){	
	top.junblockUI();
}
</script>
</head>

<body >
<form  method="post" scope="request" id="form" name="form" action="/importDataAction.do?keyId=$importId" >
<input type="hidden" name="importName" value="$importId"/>
<div class="Heading">
	<div class="HeadingTitle">$text.get("import.upload.result")</div>
</div>
<div>
	<div class="">

    <div id=pageTitle><span>$text.get("import.upload.result") &nbsp; [$!globals.getTableDisplayName($importName)]</span>
    <ul class="HeadingButton">	
		#if("$!NoBack"=="NoBack")
			#if("$!importName"!="tblIniAccDet")
				<li><button type="button"  class="btn btn-small" onClick="closeWin();">$text.get("common.lb.close")</button></li>
			#end
		#else	
		<li><button type="button"  class="btn btn-small" onClick="location.href='/importDataAction.do?operation=91&fromPage=$!fromPage&tableName=$importName&moduleType=$moduleType&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button></li>
		#end
	</ul>
    <div>
	</div></div>
	<div id="bodyUl">
		<ul>		
			<li>
				<div class="itemName">$text.get("aio.import.total")</div>$!totalimport					
			</li>
			<li>
				<div class="itemName">$text.get("import.succees.number")</div>$!successimport					
			</li>
			<li>
				<div class="itemName">$text.get("import.failure.number")</div>$!errorimport					
			</li>
			#if("$!errorimport" != "0")
			<li>
				<div class="itemName"><a href="/UtilServlet?operation=readErrorExcel&fileName=$!fileName">$text.get("download.error.report")</a></div>					
			</li>
			#end
		</ul>
	</div>
		    </div>

	    </div>
</form>
</body>
</html>
