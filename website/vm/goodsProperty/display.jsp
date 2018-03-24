<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("common.title.moreDisplay")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript">

function initData(){
     var url = new String(this.location);
	 var disstr = url.substring(url.indexOf("?str=")+5);
	 if(disstr.indexOf(";") > 0){
#foreach($row in $LocaleTable.values())
#set($t = $row.toString())
#set($lv = "document.form1."+$t+".value")
	var l = "$t"	;
	
	if(disstr.indexOf(l)>-1){
		$lv = disstr.substring(disstr.indexOf(l)+l.length+1,disstr.indexOf(";",disstr.indexOf(l)+l.length));
	}					
#end	
	}else{	
		#set($lv = "document.form1."+$DefaultLocale.toString()+".value")
		$lv = disstr;
	}	 
}

function disclose(){
var str="";
#foreach($row in $LocaleTable.values())
#set($t = $row.toString())
#set($lv = "document.form1."+$t+".value")
	var l = "$t"	;	
	str+="$row.toString():"+$lv+";";
						
#end
    window.parent.returnValue = str;
    window.close();
}	
</script>
</head>

<body onLoad="showStatus();initData();">

<form id="form1" name="form1" method="post" action="">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("common.title.moreDisplay")</div>
	<ul class="HeadingButton_Pop-up" style="display:none">

	</ul>
</div>
<div class="listRange_Pop-up_1">
	<ul class="listRange_Pop-up_jag">
		<li><span>$DefaultLocale.getDisplayName($DefaultLocale) ：</span><input name="$DefaultLocale.toString()" type="text" id="$DefaultLocale.toString()" style="width:100px" value="" maxlength="100"><img src="/$globals.getStylePath()/images/biaozianniu.gif" width="15" height="15"></li>	
			#foreach($row in $LocaleTable.values())
				#if($row.toString() != $DefaultLocale.toString())
				<li><span>$row.getDisplayName($row)：</span><input name="$row.toString()" type="text" id="$row.toString()" style="width:100px" value="" maxlength="100"><img src="/$globals.getStylePath()/images/biaozianniu.gif" width="15" height="15"></li>	
				#end
			#end			
			<li class="listRange_Pop-up_jag_button"><button type="button" onclick="disclose();" class="b2">$text.get("common.lb.ok")</button></li>
	</ul>
		<div class="listRange_jag_doc"><span>($text.get("common.title.mustInput")</span><img src="/$globals.getStylePath()/images/biaozianniu.gif" width="15" height="15">)</div>
</div>

</form> 
</body>
</html>
