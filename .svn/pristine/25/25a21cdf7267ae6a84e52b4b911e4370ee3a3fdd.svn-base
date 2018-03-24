<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<title>button</title>
<script>
	function brotherUrl(tableName,index){ 	
		var url=self.parent.frames["bottomFrame"].location.href;
		if(url.indexOf("UserFunctionAction.do")>-1){
			if(!confirm("是否要离开当前页面?")){
				return ;
			}
		}
		var approveStatus;
		if(index==1){
			approveStatus='transing'
		}else if(index==2){
			approveStatus='consignation'
		}else if(index==3){
			approveStatus='transing2'
		}else{
			approveStatus='finish'
		}
		var url = "/OAMyWorkFlow.do?operation=$globals.getOP("OP_QUERY")&approveStatus="+approveStatus+"&src=menu&winCurIndex=$!request.getParameter("winCurIndex")";
		window.parent.document.getElementById("bottomFrame").src=url;
		if(index==1){
			document.getElementById("divShow_1").className= "listRange_1_space_1_div_a";
			document.getElementById("divShow_2").className= "listRange_1_space_1_div_b";
			document.getElementById("divShow_3").className= "listRange_1_space_1_div_b";
			document.getElementById("divShow_4").className= "listRange_1_space_1_div_b";
		}else if(index==2){
			document.getElementById("divShow_1").className= "listRange_1_space_1_div_b";
			document.getElementById("divShow_2").className= "listRange_1_space_1_div_a";
			document.getElementById("divShow_3").className= "listRange_1_space_1_div_b";
			document.getElementById("divShow_4").className= "listRange_1_space_1_div_b";
		}else if(index==3){
			document.getElementById("divShow_1").className= "listRange_1_space_1_div_b";
			document.getElementById("divShow_2").className= "listRange_1_space_1_div_b";
			document.getElementById("divShow_3").className= "listRange_1_space_1_div_a";
			document.getElementById("divShow_4").className= "listRange_1_space_1_div_b";
		}else{
			document.getElementById("divShow_1").className= "listRange_1_space_1_div_b";
			document.getElementById("divShow_2").className= "listRange_1_space_1_div_b";
			document.getElementById("divShow_3").className= "listRange_1_space_1_div_b";
			document.getElementById("divShow_4").className= "listRange_1_space_1_div_a";
		}
	}

</script>
<style type="text/css">
<!--
body {
	height:25px; width:100%;
	background:url(/$globals.getStylePath()/images/aiobg.gif) repeat-x left -121px;
}
-->
</style>
</head>
<body onload='brotherUrl("$text.get("common.lb.transacting")",1);'>

<div style="padding-left:5px;">

<div id="divShow_1" class="listRange_1_space_1_div_a" onclick="brotherUrl('$text.get("common.lb.transacting")',1)">待审</div>
<div id="divShow_2" class="listRange_1_space_1_div_b" #if("$!existConsign"=="") style="display:none;" #end onclick="brotherUrl('$text.get("common.lb.transacting")',2)">委托</div>
<div id="divShow_3" class="listRange_1_space_1_div_b" onclick="brotherUrl('$text.get("common.lb.transacting")',3)">$text.get("common.lb.transacting")</div>
<div id="divShow_4" class="listRange_1_space_1_div_b" onclick="brotherUrl('$text.get("common.lb.transacted")',4)">$text.get("common.lb.transacted")</div>
</body>
</html>
