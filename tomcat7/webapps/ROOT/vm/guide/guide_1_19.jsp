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
	if(confirm("确定开账？"))
	{
		document.body.style.cursor="wait" ;
		location.href="/SysAccAction.do?exe=exe&type=beginAcc&fromPage=guide_1_19.jsp";	
	}
}
function openAutoAcc()
{
	if(confirm("确定是否直接开账？"))
	{
		document.body.style.cursor="wait" ;
		location.href="/SysAccAction.do?type=beginAcc&exe=exe&autoBegin=true&fromPage=guide_1_19.jsp";	
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
    <div id=pageTitle><span>第二步：录入期初 (2.10)</span><div>
	</div></div>
	<div id="bodyUl">
		<ul>
					<li>
				10、开账； <a href="javascript:mdiwin('/SysAccAction.do?type=beginAcc&src=menu','开账')">点击这里：开账	</a>			</li>
				<li>开账前必须试算平衡，如果想直接开账请点击这里：直接开账。开账后，请重新登录，开始使用</li>
				<li class="add"><a href="javascript:openAutoAcc();">直接开账</a></li>
				<li class="add"><a href="javascript:openAcc();">开账</a></li>
		</ul>
	<br></div>
	
	<br><br>
	<div id="regfoot"><a href="guide_1_18.jsp" class="regbut">上一步</a>
</div>

		    </div>
            </div>
	    </div>
				    </div>
            </div>
	    </div>
</body>
</html>