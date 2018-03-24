<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>系统角色添加</title>
<link rel="stylesheet" href="/style/css/guide.css" type="text/css">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script type="text/javascript">
function mdiwin(url,title){
	top.showreModule(title,url);
}
function openAcc()
{
	if(confirm("确定是否直接开账？"))
	{
		document.body.style.cursor="wait" ;
		location.href="/SysAccAction.do?type=beginAcc&exe=exe&autoBegin=true&fromPage=guide_1_18.jsp";	
	}
}
</script>
</head>
<body >
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">
		使用向导
	</div>
	<ul class="HeadingButton">
		
	</ul>
</div>
<div id="listRange_id"  align="center">
	<div class="upgrade_mtop">
<div id="contentleft">
		    <div id="contentright">
		    <div id="contentMain"> 
			
<div id="Content">
    <div id=pageTitle><span>第二步：录入期初 (2.9)</span><div>
	</div></div>
	<div id="bodyUl">
		<ul>
					<li>
				9、财务期初； <a href="javascript:mdiwin('/IniAccQueryAction.do?src=menu','录入财务期初')">点击这里：录入财务期初</a>

				</li>
				<li>注意：如果没有完整的财务期初，请跳过此步骤，<a href="javascript:openAcc()">点击这里：直接开账。</a></li>
				<li class="add"><a href="javascript:openAcc()">直接开账</a></li>
				<li class="add"><a href="javascript:mdiwin('/IniAccQueryAction.do?src=menu','录入财务期初')">现在录入财务期初</a></li>
		</ul>
	<br></div>
	
	<br><br>
	<div id="regfoot"><a href="guide_1_17.jsp" class="regbut">上一步</a>&nbsp;&nbsp;<a href="guide_1_19.jsp" class="regbut">下一步</a>
	</div>
</div>

		    </div>
            </div>
	    </div>
				    </div>
            </div>
	    </div>
</body>
</html>