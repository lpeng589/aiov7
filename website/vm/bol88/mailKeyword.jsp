<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("bol88.e.function")</title>
<link rel="stylesheet" href="/style/css/about.css" type="text/css">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">

<script language="javascript">
function submitAIO(){    	
    	document.form.submit();    
}
function submitConfirm(){
	document.form.action = "/AIOBOL88EMailAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&type=mailWrite";
	var keys = document.getElementsByName("keyId");
	nas = "";
	sums = 0;
	for(i=0;i<keys.length;i++){
		if(keys[i].checked){
			if(i==0){
				nas =keys[i].getAttribute("na");
			}else{
				nas += ";  "+keys[i].getAttribute("na");
			}
			sums += Number(keys[i].getAttribute("num"));
		}
	}
	if(keys.length > 0 && sums == 0){
		alert('$text.get("bol88.mailKeyword.chooseKeyword")');
		return false;
	}
	document.form.curKeywordName.value = nas;
	document.form.curKwMailNum.value=sums;
	document.form.submit();
	return true;
}
</script>
<style>
#bodyUl button {height:20px; line-height:20px; overflow:hidden; margin:0px; padding:0px;}
</style>
</head>

<body >
<form  method="post" scope="request" id="form" name="form" action="/AIOBOL88EMailAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&type=keyword">
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD_PREPARE")">
<input type="hidden" name="query" value="true">
<input type="hidden" name="winCurIndex" value="$winCurIndex">
<input type="hidden" name="curKeywordName" value="">
<input type="hidden" name="curKwMailNum" value="0">

<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">
		$text.get("bol88.mailKeyword.chooseKeyword")

	</div>
	<ul class="HeadingButton">
		<li></li>
	</ul>
</div>
<div id="listRange_id"  align="center">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
	<div style="margin-top:15px">
<div id="contentleft">
		    <div id="contentright">
		    <div id="contentMain"> 
<div id="Content"> 
    <div id=pageTitle>$text.get("bol88.mailKeyword.importKeyword") 
    <div>
	</div></div>
	<div id="bodyUl">
		<ul >		
			<li >
					$text.get("common.lb.key")：<input name="mailkeyword" type="text" class="text2"  value="$!MailPoolForm.mailkeyword" maxlength="10" style="width:250px">
					<button onclick="submitAIO()" class="b2">$text.get("common.lb.query")</button>	<br>
					<span>$text.get("bol88.mailKeyword.KeywordAnnotate")</span>					
			</li>
			<li >
					#if($query == "true")
						#if("$!result" == "" && "$!keyword"!="")
							$text.get("bol88.mailKeyword.KeywordNoMatching")

						#else
							#foreach($row in $result)
							<input type="checkbox" name="keyId" value="$globals.get($row,1)" num="$globals.get($row,2)" na="$globals.get($row,0)" checked  />$globals.get($row,0) ($globals.get($row,2)$text.get("bol88.mailKeyword.amountMailAddress"))<br>
							#end	
						#end
					#end									
			</li>
			<li style="margin-left:80px">
			<a href="javascript:submitConfirm();" class="regbut" style="text-align:center">$text.get("button.lable.sure")</a>
			<a href="/AIOBOL88EMailAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&type=mailInfo&winCurIndex=$winCurIndex" class="regbut" style="text-align:center">$text.get("common.lb.back")</a>
		</ul>
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
