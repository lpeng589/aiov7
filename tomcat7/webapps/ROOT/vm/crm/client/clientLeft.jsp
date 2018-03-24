<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("crm.client.attention.path")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<link rel="stylesheet" href="/js/tree/jquery.treeview.css" /> 
<script src="/js/jquery.js" type="text/javascript"></script>
<script src="/js/jquery.cookie.js" type="text/javascript"></script>
<script src="/js/tree/jquery.treeview.js" type="text/javascript"></script>
<script language="javascript">
$(document).ready(function(){
	var curLi ;	
	$("#navigation").treeview({
		persist: "cookie",
		cookieId: "aioemail" ,
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
function goFarme2(checkId,checkType){
	/*选择所有*/
	if("all"==checkType){
		var checkAll = document.getElementsByName(checkId) ;
		var checkObj = document.getElementById(checkId) ;
		checkObj.checked = !checkObj.checked ;
		for(var i=0;i<checkAll.length;i++){
			if(checkAll[i].id!=""){
				checkAll[i].checked = checkObj.checked ;
			}
		}
	}else if("checkAll"==checkType){
		var checkAll = document.getElementsByName(checkId) ;
		var checkObj = document.getElementById(checkId) ;
		for(var i=0;i<checkAll.length;i++){
			if(checkAll[i].id!=""){
				checkAll[i].checked = checkObj.checked ;
			}
		}
	}else{
		if(typeof(checkId)!="undefined"){
			var checkObj = document.getElementById(checkId) ;
			checkObj.checked = !checkObj.checked ;
		}
	}
	#foreach($define in $!leftDefine)
		#if("Status"=="$!globals.get($define,1)" && "$public"=="public")
		#else
			var defin$velocityCount = document.getElementsByName("$!globals.get($define,1)") ;
			var str$!globals.get($define,1) = "" ;
			for(var i=0;i<defin${velocityCount}.length;i++){
				if(defin$velocityCount[i].checked){
					str$!globals.get($define,1) += defin$velocityCount[i].value+"|"
				}
			}
			window.parent.frames["mainFrame"].frames["topFrame"].document.getElementById("$!globals.get($define,1)").value = str$!globals.get($define,1) ;
		#end
	#end
	
	window.parent.frames['mainFrame'].frames["topFrame"].document.forms['form'].submit();
}
</script>
</head>

<body>
 <div class="aoHeadingFrametop">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="oaHeadingTitle">#if("$!public"=="public")$text.get("crm.client.publicPool")#else$text.get("crm.client.list")#end</div>
</div>
<div class="aomainall" id="conter" style="width: 129px;">
<script type="text/javascript"> 
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-38;
oDiv.style.height=sHeight+"px";
</script>
<ul class="oamailleft_list1">
	<span><img src="/$globals.getStylePath()/images/mailwrite.gif"/></span>$text.get("crm.client.assortTerm")
</ul>
<ul id="navigation">	
	$!result
</ul>
</div>

</body>
</html>

