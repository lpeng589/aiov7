<!-- saved from url=(0014)about:internet -->
<html lang="en">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看流程</title>
<link type="text/css" href="/style/css/flow-design.css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="JavaScript" type="text/javascript">
function onCompleteUpload(str){
    alert(str);
}
</script>
</head>

<body >
#if("$!flowNew"=="true")
<div class="m-left">
  	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			id="fileUpload" width="100%" height="100%" 
			codebase="/swflash.cab">
			<param name="movie" value="/flash/WorkFlowViewNew.swf"/>
			<param name="quality" value="high" />
			<param name="bgcolor" value="#ffffff" />
			<param name="flashvars" value="serverURL=http://$!ip/&fileName=$!designId&state=$!flowDepict" />
			<param name="allowScriptAccess" value="sameDomain" />
			<embed src="/flash/WorkFlowViewNew.swf" quality="high" bgcolor="#ffffff"
				width="100%" height="100%" name="column" align="middle" 
				play="true" loop="false" allowScriptAccess="sameDomain"
				flashvars="serverURL=http://$!ip/&fileName=$!designId&state=$!flowDepict"				
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
	</object>
</div>
<div class="m-right" style="margin:0;"> 
#if($!delivers.size()>0)
<div class="view-ul-wp">
	<p class="t-p">审核记录</p>
	<ul class="view-ul">
		#foreach($!deliver in $!delivers)
		<li class="view-li">
			<div class="d-pa">
				<em class="node-em">第$!velocityCount步&nbsp;&nbsp;$!deliver.nodeID</em>
				<span class="check-person">$!deliver.checkPerson</span>
			</div>
			<div class="d-dbk">
				<em class="end-time">[$!deliver.endTime]</em>
				<div class="app-work-check">
					&nbsp;$!deliver.approvalOpinions<br>
					#foreach($affix in $globals.strSplitByFlag($deliver.affix,';'))
						#if("$!affix"!="")
						$affix <a href="/ReadFile?fileName=$globals.urlEncode($affix)&realName=$globals.urlEncode($affix)&tempFile=false&type=AFFIX&tableName=$!tableInfo.tableName&down=true" target="_blank">$text.get("common.lb.download")</a>
						#end
					#end
				</div>
				<em class="node-type">
					$!deliver.nodeType #if("$!deliver.workFlowNode"!="")-> [$!deliver.workFlowNode]-$!deliver.checkPersons #end
				</em>
			</div>
		</li>
		#end
	</ul>
</div>
#end
</div>
#else
	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			id="fileUpload" width="100%" height="100%" 
			codebase="/swflash.cab">
			<param name="movie" value="/flash/WorkFlowView.swf"/>
			<param name="quality" value="high" />
			<param name="bgcolor" value="#ffffff" />
			<param name="flashvars" value="serverURL=http://$!ip/&fileName=$!fileName&state=$!flowDepict" />
			<param name="allowScriptAccess" value="sameDomain" />
			<embed src="/flash/WorkFlowView.swf" quality="high" bgcolor="#ffffff"
				width="100%" height="100%" name="column" align="middle" 
				play="true" loop="false" allowScriptAccess="sameDomain"
				flashvars="serverURL=http://$!ip/&fileName=$!fileName&state=$!flowDepict"				
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
	</object>
#end
</body>
<script type="text/javascript">
	var dWidth = 1150;
	if($(window).width()<=1024){
		dWidth = 1024;
	}
	$(".m-left").width(dWidth-340);
</script>
</html>