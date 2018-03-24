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
</script>
<style>
#bodyUl button {height:20px; line-height:20px; overflow:hidden; margin:0px; padding:0px;}
</style>
</head>

<body >
<form  method="post" scope="request" id="form" name="form" action="/AIOBOL88Action.do">
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")">
<input type="hidden" name="aioClose" value="0">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">
		$text.get("bol88.e.function")
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
    <div id=pageTitle>$text.get("bol88.welcome.use")$text.get("bol88.e.you.can")<div>
	</div></div>
	<div id="bodyUl">
		<ul >		
			<li >
					<a href="$globals.getBol88()/loginAction.do?username=$result.userName&JSESSION=$result.password&operation=10&url=SupplyAndDemandQueryAction.do" target="_blank" class="regbut" style="text-align:center">$text.get("bol88.e.free.publish.supply")</a>
					
					<a href="$globals.getBol88()/loginAction.do?username=$result.userName&JSESSION=$result.password&operation=10&url=ProductQueryAction.do" target="_blank" class="regbut" style="text-align:center">$text.get("bol88.e.free.publish.product")</a>
					
					<a href="$globals.getBol88()/loginAction.do?username=$result.userName&JSESSION=$result.password&operation=10&url=$globals.getBol88()/CompanyYellowPageQueryAction.do" target="_blank" class="regbut" style="text-align:center">$text.get("bol88.e.free.publish.company")</a>
					<a href="$globals.getBol88()/${result.userName}" target="_blank" class="regbut" style="text-align:center">$text.get("bol88.into.homepage")</a><br/><br/>
					
					<a href="$globals.getBol88()/loginAction.do?username=$result.userName&JSESSION=$result.password&operation=10&url=AdvertiseQueryAction.do" target="_blank" class="regbut" style="text-align:center">$text.get("bol88.e.free.publish.advertisement")</a>
					#if("$globals.getSystemState().dogState" == "0"|| "$globals.getSystemState().dogState" == "4")					
					<input type="checkbox" name="flag" value="0" #if("$result.flag" == "0") checked #end onClick="submitAIO();">
					($text.get("bol88.e.notice"))
					#end
					<br/><br/>
				  	<a href="/AIOBOL88EMailAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&username=$result.userName&JSESSION=$result.password&type=mailInfo&winCurIndex=$winCurIndex" class="regbut" style="text-align:center">$text.get("bol88.into.mail")</a>
					($text.get("bol88.into.mailNote"))
					<br/><br/>  
					<a href="/AIOBOL88Action.do?operation=$globals.getOP("OP_ADD_PREPARE")&winCurIndex=$winCurIndex" class="regbut" style="text-align:center">$text.get("bol88.e.binding.again")BOL88</a>
					
			</li>
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
