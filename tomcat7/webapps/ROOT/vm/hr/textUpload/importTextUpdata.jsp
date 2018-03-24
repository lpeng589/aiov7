<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>$text.get("excel.lb.addModel")</title>
		<link rel="stylesheet" href="/style/css/about.css" type="text/css">
		<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
		<script type="text/javascript" src="/js/function.js"></script>
		<script language="javascript">
		if(typeof(top.junblockUI)!="undefined"){	
			top.junblockUI();
		}
		</script>
	</head>

	<body>
		<form method="post" scope="request" id="form" name="form"
			action="/importDataAction.do?keyId=$importId">
			<input type="hidden" id="importName" name="importName" value="$importId" />
			<div class="Heading">
				<div class="HeadingIcon">
					<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
				</div>
				<div class="HeadingTitle">
					$text.get("import.upload.result")
				</div>
			</div>
			<div id="listRange_id" align="center">
				<div class="upgrade_mtop">
					<div id="contentleft">
						<div id="contentright" style="float: none;">
							<div id="contentMain">
								<div id="Content">
									<div id=pageTitle>
										<span>$text.get("import.upload.result")</span>
									</div>
									<div id="bodyUl">
											$text.get("import.card.annals.success")
									</div>
									<div style="margin-top: 50px;margin-left: 200px;">
										<button type="button" onclick="javascript:window.location.href='/TextUploadAction.do'">返回</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<button type="button" onclick="closeWin()">关闭</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</body>
</html>
