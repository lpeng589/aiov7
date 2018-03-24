<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>#if("$type" == "1") 
		$text.get("upgrade.title.module") 
	#elseif("$type" == "2")
		$text.get("upgrade.title.user") 
	#elseif("$type" == "3")
		$text.get("upgrade.title.language") 
	#elseif("$type" == "4")
		$text.get("upgrade.title.function") 
	#end</title>
<link rel="stylesheet" href="/style/css/reg.css" type="text/css">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript">
#if("true" == $exig)
putValidateItem("password","$text.get("upgrade.lb.password")","any","",false,0,20);
#end
function beforSubmit(form){   
	var aucodes = document.getElementsByName("aucode");
	for(i=0;i<aucodes.length;i++){
		if(aucodes[i].value == ''){
			alert('$text.get("upgrade.lb.code")$text.get('common.validate.error.null')');
			return false;
		}
	}
	if(!validate(form))return false;
	
	disableForm(form);
	return true;
}	

function setImgSize()
{
	var maxwidth=400;
	var imgs = document.images;
	var length = imgs.length;
	for(var i=0;i<length;i++)
	{
		var img = imgs[i];
		var heigth = img.offsetHeight;
		var width = img.offsetWidth;
		var p = heigth/width;
		if(width>maxwidth)
		{
			var newWidth = maxwidth;
			var newHeigth=newWidth*p;
			img.width=newWidth;
			img.height=newHeigth;
			
		}
	}
}
</script>
</head>

<body onLoad="showStatus();setImgSize()" style="overflow-y:outo;">
<form  method="post" scope="request" name="form" action="/UpgradeAction.do" onSubmit="return beforSubmit(this);" >
<input type="hidden" name="operation" value="$globals.getOP("OP_UPGRADE")">
<input type="hidden" name="type" value="$type">
<input type="hidden" name="module" value="$module">
<input type="hidden" name="userNum" value="$userNum">
<input type="hidden" name="languageNum" value="$languageNum">
<input type="hidden" name="exig" value="$exig">
<input type="hidden" name="functionNum" value="$functionNum">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">

<div id="listRange_id"  align="center">
<script type="text/javascript"> 
var oDiv=document.getElementById("listRange_id");
var sHeight=document.body.clientHeight;
oDiv.style.height=sHeight+"px";
</script>
	<div class="upgrade_mtop" style="margin-top:10px; border:1px solid #FFFFFF" >
	<table >
	<tr><td valign="true">	
	    <div id="contentleft" >
		    <div id="contentright" >
		    <div id="contentMain" > 
<div id="Content" >
	<div id=pageTitle><span>
	#if("$type" == "1") 
		$text.get("upgrade.title.module") 
	#elseif("$type" == "2")
		$text.get("upgrade.title.user") 
	#elseif("$type" == "3")
		$text.get("upgrade.title.language") 
	#elseif("$type" == "4")
		$text.get("upgrade.title.function") 
	#end</span><div>
	</div></div>
	<div id="bodyUl">
	
		<ul>
	 	#if("true" == $exig)
		<li><div class="itemName">$text.get("upgrade.lb.password")</div>
		<input name="password" type="password" value="" maxlength="10" class="intext" onFocus="this.className='intext intext_hover'"  onblur="this.className='intext'" > 
		</li>
		#end
		
		<li><div class="itemName"> $text.get("common.lb.plsEnter")$text.get("upgrade.lb.code")</div>
		<input name="aucode" type="text" value="" maxlength="10" class="intext" onFocus="this.className='intext intext_hover'"  onblur="this.className='intext'" > 
		<input name="aucode" type="text" value="" maxlength="10" class="intext" onFocus="this.className='intext intext_hover'"  onblur="this.className='intext'">
		</li>
		#foreach($a in [2,3,4])
			#if($a <= $codeNum)
			<li><div class="itemName">&nbsp;</div>
			<input name="aucode" type="text" value="" maxlength="10" class="intext" onFocus="this.className='intext intext_hover'"  onblur="this.className='intext'"> 
			<input name="aucode" type="text" value="" maxlength="10" class="intext" onFocus="this.className='intext intext_hover'"  onblur="this.className='intext'"> 
		</li>
			#end
		#end
				<li id="titleDiv"></li>
			</ul>
	
		
	</div>
	<br>
	<div id="regfoot" style="width:70%;">
	<button type="submit" name="Submit" class="regbut">$text.get("common.lb.nextT")</button>
	<button type="button" onclick="window.location.href='/RegisterAction.do?regFlag=2&winCurIndex=$!winCurIndex';" class="regbut">$text.get("common.lb.back")</button>
	</div>
</div>
</div>
</div>
</div>
</td>
<td valign="true">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</td>
</tr>
</table>


</div>
</div>
</form> 
</body>
</html>





