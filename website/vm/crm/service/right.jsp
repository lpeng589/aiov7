<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="/$globals.getStylePath()/css/ListRange.css">
<title></title>
<style>
.qas{ float:left; width:180px; height:auto; margin-left:5px; margin-top:3px;font-size:12px; vertical-align:middle; 	background: #FFFFFF url(/style/images/aiobg.gif) repeat-x left -221px;
	border:1px solid #A2CEF5; padding:5px;}
.qas ul { margin:0px; padding:0px;}
.qas ul li {float:left; width:180px; height:25px; line-height:180% }
.linkd a:link{ text-decoration:underline; color:#333; vertical-align:baseline;}
.qaslink { border-bottom:1px dashed #999999;}
.textb { float:left; background:#F0F8FF; border:0px; border:1px solid #A2CEF5; height:22px;width: 122px;}
</style>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript">
function showreModule(keyId){
	AjaxRequest("/UtilServlet?operation=updateQA&keyId="+keyId) ;
	var url = "/UserFunctionQueryAction.do?tableName=CRMQA&src=menu&operation=5&noback=true&keyId="+keyId ;
	top.showreModule('$text.get("common.msg.QA.depository")',url);	
}
function showreModule2(strName,url){
	top.showreModule('$text.get("common.msg.QA.depository")',url);	
}
function beforeSubmit(){
	var strQuestion = document.getElementById("question").value ;
	if(strQuestion.length==0){
		alert("$text.get('common.msg.QANotNull')");
		return false;
	}
	return true ;
}
</script>
</head>

<body style="overflow-x:hidden;"> 
<div class="qas">
<ul>
<form action="/ClientServiceAction.do?type=right" method="post">
<li><img src="/$globals.getStylePath()/images/St.gif" style="vertical-align:middle"  width="16" height="16">$text.get("common.msg.QA")：<a href="javascript:showreModule2('$text.get("common.msg.QA.depository")','/UserFunctionQueryAction.do?tableName=CRMQA&src=menu')">$text.get("common.lb.QA.maintenance")</a></li>
<li><input type="text" class="textb" name="question" value="$!question"/> 
<button name="Submits" type="submit" onClick="return beforeSubmit();">$text.get("common.lb.retrieval")</button>
</form>
<span class="linkd"></span></li>
#foreach($question in $listQA)
	<li><a href="javascript:showreModule('$globals.get($question,0)');">$globals.get($question,1)</a></li>
#end
<li class="qaslink"><font color="#666606">$text.get("crm.service.QA.retrieval.announcements")：</font></li>
<li><font color="#666606">1、$text.get("crm.service.QA.retrieval.announcements.No1")。</font></li>
</ul>
</div>
</body>
</html>
