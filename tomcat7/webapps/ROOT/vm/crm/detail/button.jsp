<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<title>button</title>
<script>
	#set($count=1)
	#foreach($row in $childTabList)
		#set($count=$count+1)
	#end
	var count = $count;
	function brotherUrl(tableName,index){ 
		var keyId = document.getElementById("mainKeyId").value;
		var url = "/UserFunctionQueryAction.do?tableName="+tableName+"&src=menu&operation=4"+"&designId=$!designId&f_brother="+keyId+"&isTab=yes&winCurIndex=$!winCurIndex";
		if(tableName!='$!tableName'){
		  url="/UserFunctionQueryAction.do?tableName="+tableName+"&src=menu&operation=4"+"&designId=$!designId&f_brother="+keyId+"&isTab=yes&parentTableName=$!tableName&winCurIndex=$!winCurIndex";
		}
		window.parent.document.getElementById("bottomFrame").src=url;
		document.getElementById("checkItem").value=tableName ;
		for(var i=0;i<count;i++){
			if(i == index){
				document.getElementById("divShow_"+i).className= "listRange_1_space_1_div_a";
			}else{
				document.getElementById("divShow_"+i).className= "listRange_1_space_1_div_b";
			}
		}
	}
	
	function bludUlr(tableName,index){ 
		var keyId = document.getElementById("mainKeyId").value;
		if(keyId.length>0){
			#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=$!tableName").update() && "$!public"=="")
			#set ($operation="7")
			#else
			#set ($operation="5")
			#end
			#if("$!showClose"=="true")
			var url = "/UserFunctionAction.do?tableName="+tableName+"&operation=$operation&designId=$!designId&fromCRM=detail&keyId="+keyId ;
			#else
			var url = "/CrmTabAction.do?tableName="+tableName+"&tabIndex=CRMClientInfot&fromCRM=true&tabType=detail&designId=$!designId&keyId="+keyId+"&operation=5";
			#end
			window.parent.document.getElementById("bottomFrame").src=url+"&tabList=Y";
			document.getElementById("checkItem").value=tableName ;
			for(var i=0;i<=count;i++){
				if(i == index){
					document.getElementById("divShow_"+i).className= "listRange_1_space_1_div_a";
				}else{
					document.getElementById("divShow_"+i).className= "listRange_1_space_1_div_b";
				}
			}
		}
	}


function showNeighbor(){
	var returnVaue = window.showModalDialog("/crmUserFunctionAction.do?tableName=$tableName&type=neighbor","","dialogWidth=360px;dialogHeight=330px") ;
	if(typeof(returnVaue)!="undefined"){
		window.location.reload() ;
	}
}

function closeWin(){
	if(typeof(this.parent.parent.win)!="undefined"){
		this.parent.parent.win.removewin(this.parent.parent.win.currentwin);
	}else if(typeof(parent.parent.jQuery.fn.closeActiveTab)!="undefined"){
		this.parent.parent.jQuery.fn.closeActiveTab();
	}else{
		window.close() ;
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
#set ($tIndex=1)
#set ($strIndex=1) 
#foreach($row in $childTabList)
	#if("$!tabIndex"=="$!row.tableName")
		#set($strIndex=$tIndex)
	#end
	#set($tIndex=$tIndex+1)
#end
<body onload='#if("$!tabIndex"!="")brotherUrl("$tabIndex",$strIndex);#end'>
<div style="padding-left:5px;overflow:hidden;height:25px;width:900px;float:left;" id="outDIV">
<div style="width:9000px" id="inDIV">
#if($tableName != "")
<div id="divShow_0" class="listRange_1_space_1_div_a" style="float:left" onClick="bludUlr('$!tableName',0);">$text.get("crm.tab.detail.info")</div>
#end
#set($index=1)
#foreach($row in $childTabList)
<div id="divShow_$index" class="listRange_1_space_1_div_b" style="float:left" onClick="brotherUrl('$row.tableName',$index);">$globals.getTableDisplayName($row.tableName)</div>
#set($index=$index+1)
#end
</div>
</div>
#if("$!showClose"=="true")
<div class="crm_tabbutton">
<button onclick="closeWin();" name="btnClose" style="width: 55px; margin-top:-5px">$text.get("common.lb.close")</button>
</div>
#else
<div class="crm_tabbutton">
 <span id="div_min" onClick="setFrameHight('div_min');" class="search"><img src="/$globals.getStylePath()/images/min.gif" alt="$text.get("crm.tab.min")"></span> 
<span id="div_ret" onClick="setFrameHight('div_ret');" class="search"><img src="/$globals.getStylePath()/images/rev.gif" alt="$text.get("crm.tab.rev")"></span>
 <span id="div_max" onClick="setFrameHight('div_max');" class="search"><img src="/$globals.getStylePath()/images/max.gif" alt="$text.get("crm.tab.max")"></span> 
<span id="rw" onClick="showNeighbor()" class="search"><img src="/$globals.getStylePath()/images/show.gif" alt="$text.get("tblNeighbourMain.setting")"></span>
</div>
#end
<div class="crm_tabbutton" id="rightLeftDIV">
<span id="div_min" onClick="move('left');" class="search" style="padding-left:10px"><img src="/$globals.getStylePath()/images/frame/left.gif" ></span>
<span id="div_min" onClick="move('right');" class="search" style="padding-left:10px;padding-right:10px"><img src="/$globals.getStylePath()/images/frame/right.gif" ></span>
</div>
<input type="hidden" id="mainKeyId" value="$!firstId"/>
#if("$!tabIndex"!="")
<input type="hidden" id="checkItem" value="$!tabIndex"/>
#else
<input type="hidden" id="checkItem" value="$!tableName"/>
#end
</body>
<script language="javascript">
	function setFrameHight(name){
		var frm = parent.document.getElementById("crmFramset") ;
		if(name == "div_min"){
			frm.rows = "100%,25,0";
			var dHeight2=window.parent.frames['topFrame'].document.body.clientHeight;
			var oDiv2=window.parent.frames['topFrame'].document.getElementById("listRange_id");
			if(oDiv2 == null || oDiv2=="null"){
				var oDiv=window.parent.frames['topFrame'].document.getElementById("list_id");
				var dHeight=window.parent.frames['topFrame'].document.body.clientHeight;
				oDiv.style.height=dHeight-68+"px";
			}else{
			 	oDiv2.style.height=dHeight2-32+"px";
			}
		}
		if(name == "div_max"){
			frm.rows = "0,25,100%";
			var dHeight2=window.parent.frames['bottomFrame'].document.body.clientHeight;
			var oDiv2=window.parent.frames['bottomFrame'].document.getElementById("oalistRange");
			if(oDiv2 == null || oDiv2=="null"){
				var oDiv=window.parent.frames['bottomFrame'].document.getElementById("listRange_id");
				var sHeight=window.parent.frames['bottomFrame'].document.getElementById("conter");
				var dHeight=window.parent.frames['bottomFrame'].document.body.clientHeight;
				if(oDiv!=null){
					oDiv.style.height = dHeight-68+"px";
				}
				if(sHeight!=null){
					sHeight.style.height = dHeight-68+"px";
				}
			}else{
			 	oDiv2.style.height=dHeight2-32+"px";
			}
		}
		if(name == "div_ret"){
			frm.rows = "108%,25,92%";	
			var dHeight3=window.parent.frames['topFrame'].document.body.clientHeight;
			//上面
			var oDiv2=window.parent.frames['topFrame'].document.getElementById("list_id");
			var dHeight2=window.parent.frames['topFrame'].document.body.clientHeight;
			if(oDiv2 != null){
				oDiv2.style.height=dHeight2-68+"px";
			}
			//下面
			var dHeight4=window.parent.frames['bottomFrame'].document.body.clientHeight;
			var oDiv4=window.parent.frames['bottomFrame'].document.getElementById("oalistRange");
			if(oDiv4 != null){
				oDiv4.style.height=dHeight4-20+"px";
			}else{
				var oDiv=window.parent.frames['bottomFrame'].document.getElementById("listRange_id");
				var sHeight=window.parent.frames['bottomFrame'].document.getElementById("conter");
				var dHeight=window.parent.frames['bottomFrame'].document.body.clientHeight;
				if(oDiv!=null){
					oDiv.style.height = dHeight-68+"px";
				}
				if(sHeight!=null){
					sHeight.style.height = dHeight-68+"px";
				}
			}
		}
	}
	function move(dir){
		
		scrollLeft =  Number(document.getElementById("outDIV").scrollLeft);
		if(dir == "left"){
			document.getElementById("outDIV").scrollLeft = scrollLeft-82;
		}else{
			document.getElementById("outDIV").scrollLeft = scrollLeft+82;
		}
	}
	//用于控件左右翻页
	outwidth = (document.body.scrollWidth-130);
	document.getElementById("outDIV").style.width=outwidth+"px";
	inwidth = document.getElementById("inDIV").childNodes.length*82;
	document.getElementById("inDIV").style.width=inwidth+"px";
	if(outwidth>=inwidth){
		document.getElementById("rightLeftDIV").style.display="none";
	}
	
</script>
</html>
