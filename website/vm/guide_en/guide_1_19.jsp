<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>System user create</title>
<link rel="stylesheet" href="/style/css/guide.css" type="text/css">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script type="text/javascript">
function mdiwin(url,title){
	top.showreModule(title,url);
}

function openAcc()
{
	if(confirm("Ensure to open account？"))
	{
		document.body.style.cursor="wait" ;
		location.href="/SysAccAction.do?exe=exe&type=beginAcc&fromPage=guide_1_19.jsp";	
	}
}
function openAutoAcc()
{
	if(confirm("Are you sure to open account directly？"))
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
		User Guide
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
    <div id=pageTitle><span>2：Input opening balance (2.10)</span><div>
	</div></div>
	<div id="bodyUl">
		<ul>
					<li>
				10、Open account； <a href="javascript:mdiwin('/SysAccAction.do?type=beginAcc&src=menu','Open account')">Click here：Open account	</a>			</li>
				<li>Please check Trial balance before open account，if you want to open account directly, please click here：Open account directly。After open account，please restart to login before to use it</li>
				<li class="add"><a href="javascript:openAutoAcc();">Open account directly</a></li>
				<li class="add"><a href="javascript:openAcc();">Open account directly</a></li>
		</ul>
	<br></div>
	
	<br><br>
	<div id="regfoot"><a href="guide_1_18.jsp" class="regbut">Previous</a>
</div>

		    </div>
            </div>
	    </div>
				    </div>
            </div>
	    </div>
</body>
</html>