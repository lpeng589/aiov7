<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("common.title.moreDisplay")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<style type="text/css">
	.listrange_pop-up_jag span{width:100px; text-align:right; float:left; display:inline-block;}
	.listrange_pop-up_jag input{border:1px solid #42789c; text-align:left; float:left; display:inline-block;}
</style>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script language="javascript">
#set($popup=$request.getParameter("popupWin"))
function m(str){return document.getElementById(str)}

function initData(){ 
	var url = "$globals.toChinseChar($request.getParameter('str'))";
	alert(url);
	var strArray = url.split(";");
	for(var i=0;i<strArray.length;i++){
		if(strArray[i].length>0){
			var valArray = strArray[i].split(":");
			m(valArray[0]).value = valArray[1];
		}
	}
}

function disclose(){ 
	if(!validate(form))return false;
	var str="";
	var lanArray = new Array('zh_CN','en','zh_TW') ;
	for(var i=0;i<lanArray.length;i++){
		var strVal = m(lanArray[i]).value;
		if(strVal.length>0)	{
			str += lanArray[i] + ":" + strVal + ";" ;
		}
	}
	#if("$!popup" != "")
	eval('window.parent.fill$!popup("'+str+'")');
	if(typeof(top.junblockUI)!="undefined"){
		top.junblockUI();
	}
	window.parent.jQuery.close("$!popup");
	#else
    window.parent.returnValue = str;
    window.close();
    #end
}	

function clearLanguage(){
	#if("$!popup" != "")
	eval('window.parent.fill$!popup("")');
	window.parent.jQuery.close("$!popup");
	#else
    window.parent.returnValue = "";
    window.close();
    #end
}

function closeWindow(){
	#if("$!popup" != "")
	window.parent.jQuery.close("$!popup");
	#else
	window.close()
	#end
}
var lenstr = "$globals.toChinseChar($request.getParameter('str'))";
putValidateItem("zh_CN","简体中文","any","",true,0,lenstr);
putValidateItem("en","English","any","",true,0,lenstr);
putValidateItem("zh_TW","繁体中文","any","",true,0,lenstr);

</script>
</head>
##set($colseType=$!request.getParameter("type"))#if("return"!="$!colseType")onbeforeunload = "return disclose();"#end
<body onLoad="initData();"  >

<form id="form" name="form" method="post" action="" onKeyPress="if(event.keyCode==13){disclose();}else if(event.keyCode==27){window.close();}">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("common.title.moreDisplay")</div>
	<ul class="HeadingButton_Pop-up">
		<li><button type="button" onClick="disclose();" class="b2">$text.get("common.lb.ok")</button></li>
		<li><button type="button" onClick="javascript:clearLanguage();" class="b2">$text.get("com.db.clear")</button></li>
		<li><button type="button" onClick="javascript:closeWindow();" class="b2">$text.get("com.lb.close")</button></li>
		
	</ul>
</div>
<div class="listRange_Pop-up_1">
	<ul class="listRange_Pop-up_jag">
		<li><span>简体中文：</span><input name="zh_CN" type="text" id="zh_CN" style="width:250px" value="" maxlength="500"></li>	
		<li><span>English：</span><input name="en" type="text" id="en" style="width:250px" value="" maxlength="500"></li>
		<li><span>繁体中文：</span><input name="zh_TW" type="text" id="zh_TW" style="width:250px" value="" maxlength="500"></li>
		
	</ul>
	</div>

</form> 
</body>
</html> 
