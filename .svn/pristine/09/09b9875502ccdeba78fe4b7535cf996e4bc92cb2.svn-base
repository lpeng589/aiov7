<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<title>$text.get("emailFilter.title.detail")</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<link rel="stylesheet"
			href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
		<script language="javascript" src="/js/function.js"></script>
		<script language="javascript" src="/js/validate.vjs"></script>
		<style type="text/css">
			.newsReply{
				min-height: 120px; 
		        max-height: 300px;
		        width: 200px;
		        margin-left: auto;
		        margin-right: auto;
		        padding: 3px;
		        outline: 0;
		        word-wrap: break-word;
		        overflow-x: hidden;
		        overflow-y: auto;
		      
		        _overflow-y: visible
		        
			}				
		.listrange_jag li{width:100%;}
		.listrange_jag li>span{width:80px;display:inline-block;text-align:right;}
		.listrange_jag li>div{display:inline-block;}
		</style>
	</head>
	<body onLoad="showStatus();">
		<iframe name="formFrame" style="display:none"></iframe>
		<div class="Heading">
			<div class="HeadingIcon">
				<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
			</div>
			<div class="HeadingTitle">
				$text.get("emailFilter.title.detail")
			</div>
			<ul class="HeadingButton">
			</ul>
		</div>

		<div id="listRange_id" align="center">
			<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
			<div class="listRange_div_jag">
				<div class="listRange_div_jag_div"></div>
				<ul class="listRange_jag" style="margin-top:10px;background:#f3f1f2;padding:20px 0 20px 0;">
					<li>
						<span>$text.get("EmailFilter.mailAddress")：</span>
						<div style="border: 1 solid #a1a1a1;width: 200px;">
							$!result.addressName
						</div>

					</li>
					<li>
						<span>$text.get("EmailFilter.folderName")：</span>
						<div style="border: 1 solid #a1a1a1;width: 200px;">
							$!result.folderName
						</div>

					</li>
					<li>
						<span>$text.get("EmailFilter.filterCondition")：</span>

						<div class = "newsReply" style="border: 1 solid #a1a1a1;height: 100px;padding:0;">
							$!result.filterCondition
						</div>

					</li>
					<li style="margin-top:10px;margin-left:40px; float:left; ">
						<center>
							<button type="button"
								onClick="location.href='/EmailFilterQueryAction.do?operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex'" class="b2">
								$text.get("common.lb.back")
							</button>
						</center>
					</li>
				</ul>
			</div>
		</div>
	</body>
</html>
