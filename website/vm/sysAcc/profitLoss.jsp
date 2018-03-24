<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script language="javascript" src="/js/function.js"></script>
<title>$text.get("muduleFlow.lb.tocost")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script type="text/javascript">
	function submits(){
		if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
		document.getElementById("tip").innerHTML='结转损益中......根据电脑配置不同可能需要几十秒至几分钟.';
		courseDisplay(); 
		window.location.href='SysAccAction.do?exe=exe&type=settleProfitLoss' ;
	}
	
	function courseDisplay(){
	    var obj=document.getElementById("course"); 
	    var urls="/UtilServlet?operation=closeAccCourse";
		AjaxRequest(urls) ;
		if(response==""||response=="checkNotAuditBill"){
			 obj.innerHTML='$text.get("closeacc.tip.notAuditBill")';		 
		}else if(response=="checkDraftBill"){
			 obj.innerHTML='$text.get("closeacc.tip.draftBill")';		 
		}else if(response==""||response=="reCalBegin"){
			 obj.innerHTML='$text.get("closeacc.tip.recal")';		 
		}else if(response=="settleCostBegin"){
			 obj.innerHTML='$text.get("closeacc.tip.setcost")';		 
		}else if(response=="settleProfitLossBegin"){		
			 obj.innerHTML='$text.get("closeacc.tip.setprofitloss")';		 
		}else if(response=="settleModulesBegin"){		
		 	obj.innerHTML='$text.get("closeacc.tip.setmodules")';		 
		}
		setTimeout("courseDisplay()",20);
 	}
</script>
</head>
<body>

<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("muduleFlow.lb.tocost")</div>
	<ul class="HeadingButton">
		<li><button type="button" id="submitID"  onClick="submits();" class="b4">$text.get("muduleFlow.lb.tocost")</button></li>
	</ul>
</div>


<div id="listRange_id">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
	<div class="listRange_sys">
			<div class="sysAcctite">结转损益简介：</div>
			<div class="sysAccconter"><span></span>结转损益主要是将所有损益类科目的本期余额全部自动转入本年利润科目，并生成一张结转损益记账凭证。可以反映集团企业在一个会计期间内实现的利润或亏损总额。<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<ul>
		</ul>
	</div>
	<div id="showList"></div>
	<div id="tip" align="center" style="color: red;font-size: 14px;font-weight:bold;"></div>
	<div id="course" align="center" style="color: red;font-size: 14px;font-weight:bold;"></div>
</div>
</body>
</html>
