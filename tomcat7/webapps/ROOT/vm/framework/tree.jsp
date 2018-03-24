<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script type="text/javascript" src="/js/function.js"></script>
<link rel="stylesheet" href="/js/tree/jquery.treeview.css" /> 
<script src="/js/jquery.js" type="text/javascript"></script>
<script src="/js/jquery.cookie.js" type="text/javascript"></script>
<script src="/js/tree/jquery.treeview.js" type="text/javascript"></script>
<script> 
$(document).ready(function(){
	var curLi ;	
	// first example
	$("#navigation").treeview({
		persist: "cookie",
		cookieId: "aioemail" ,
		//animated: true,
		collapsed: true,
		unique: false
	});
	
	$("#navigation").find('span').click(function (){
		if(curLi){
			curLi.removeClass("selNode");
		}
		$(this).addClass("selNode");
		curLi = $(this);
	});
	
});
function goFarme(folderName)
{
	var varUrl = "/FrameworkAction.do?type=list&folderName="+encodeURI(folderName) ;
	window.parent.f_mainFrame.location=varUrl;	
}

function goFarmeList(classcode){
	var varUrl = "/FrameworkAction.do?type=list&classcode="+classcode ;
	window.parent.f_mainFrame.location=varUrl;	
}
function goFarme2(varUrl)
{
	window.parent.f_mainFrame.location=varUrl;	
}
</script>
</head>
 
<body>
 <div class="aoHeadingFrametop">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="oaHeadingTitle">$text.get("com.framework.from")</div>
</div>
<div class="aomainall" id="conter">
<script type="text/javascript"> 
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-38;
oDiv.style.height=sHeight+"px";
</script>
<ul class="oamailleft_list1">
		<a href="javascript:goFarme2('/UserFunctionQueryAction.do?tableName=tblDepartment&winCurIndex=$!winCurIndex&src=menu')" ><span><img src="/$globals.getStylePath()/images/mailwrite.gif"/></span>$text.get("com.set.dept")</a>	
</ul>
<div  id="innerEmail">
<ul id="navigation">		
		<li><span ><a href="javascript:goFarme('')">#if($!globals.getCompanyName('')=="")$text.get("com.framework.from")#else$!globals.getCompanyName('')#end</a><font style="color:#ff0000" id="_1_top"></font></span>
			<ul>
				$result				
			</ul>
		</li>
</ul>
</div>
</div>
</body>
</html>

