<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script type="text/javascript" src="/js/function.js"></script>
<link rel="stylesheet" href="/js/tree/jquery.treeview.css" /> 
<script src="/js/jquery.js" type="text/javascript"></script>
<script src="/js/jquery.cookie.js" type="text/javascript"></script>
<script src="/js/tree/jquery.treeview.js" type="text/javascript"></script>
<script>
$(document).ready(function(){
	var curLi ;	
	// first example
	$("#navigation").treeview({
		persist: "cookie",
		cookieId: "aioemail" ,
		//animated: true,
		collapsed: true,
		unique: false
	});
	
	$("#navigation").find('span').click(function (){
		if(curLi){
			curLi.removeClass("selNode");
		}
		$(this).addClass("selNode");
		curLi = $(this);
	});
	
});
function goFarme(url){
	window.parent.f_mainFrame.location=url;	
}
</script>
</head>

<body>
 <div class="aoHeadingFrametop">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="oaHeadingTitle">$text.get("oa.workflow.create")</div>
</div>
<div class="aomainall" id="conter">
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-38;
oDiv.style.height=sHeight+"px";
</script>
#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=tblWorkFlowType").add())
<ul class="oamailleft_list1">
		<a href="javascript:goFarme('/UserFunctionQueryAction.do?tableName=tblWorkFlowType&winCurIndex=$!request.getParameter("winCurIndex")')" ><span><img src="/$globals.getStylePath()/images/mailwrite.gif"/></span>$text.get("oa.workflow.WorkflowTypeSet")</a>
</ul>
#end
<div  id="innerEmail">
<ul id="navigation">	
		#set($str=",")
		#foreach($pal in $result)
		#if(!($str.indexOf($!globals.get($pal,5))>-1))
		#set($str=$str+"$!globals.get($pal,5)"+",")
		<li><span ><a href="javascript:goFarme('/OAMyWorkFlow.do?approveStatus=transing&flowBelong=self&classCode=$!globals.get($pal,1)&operation=$globals.getOP("OP_QUERY")&winCurIndex=$!request.getParameter("winCurIndex")')">$!globals.get($pal,3)</a><font style="color:#ff0000" id="_1_top"></font></span>
			<ul>
			#foreach($flow in $result)	
               	#if($!globals.get($flow,5)==$!globals.get($pal,5))
                	#if($!globals.get($flow,2)==1)    
                		#if("$globals.get($flow,12)"!="1")
	                		#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=$!globals.get($flow,8)").add())
	                			<li><span><a href="javascript:goFarme('/UserFunctionQueryAction.do?tableName=$!globals.get($flow,8)&src=menu&operation=$globals.getOP("OP_ADD_PREPARE")&designId=$!globals.get($flow,0)&winCurIndex=$!request.getParameter("winCurIndex")')">$!globals.get($flow,1)</a><font style="color:#ff0000" id="_1"></font>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:goFarme('/OAWorkFlowCreateAction.do?operation=$globals.getOP("OP_DETAIL")&keyId=$!globals.get($flow,0)')"><font style="color:#ff0000" id="_1">$text.get("oa.workflow.elaborate")</font></a></span></li>
	                		#end
                		#else
                			<li><span><a href="javascript:goFarme('/OAWorkFlowAction.do?tableName=$!globals.get($flow,8)&src=menu&operation=$globals.getOP("OP_ADD_PREPARE")&designId=$!globals.get($flow,0)&winCurIndex=$!request.getParameter("winCurIndex")')">$!globals.get($flow,1)</a><font style="color:#ff0000" id="_1"></font>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:goFarme('/OAWorkFlowCreateAction.do?operation=$globals.getOP("OP_DETAIL")&keyId=$!globals.get($flow,0)')"><font style="color:#ff0000" id="_1">$text.get("oa.workflow.elaborate")</font></a></span></li>
                		#end
                    #else  
               			<li><span><a href="javascript:goFarme('/OAFileWorkFlowAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&templateId=$!globals.get($flow,0)&winCurIndex=$!winCurIndex')">44$!globals.get($flow,1)</a><font style="color:#ff0000" id="_1"></font>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:goFarme('/OAWorkFlowCreateAction.do?operation=$globals.getOP("OP_DETAIL")&keyId=$!globals.get($flow,0)')"><font style="color:#ff0000" id="_1">$text.get("oa.workflow.elaborate")</font></a></span></li>				
					#end
				#end
			#end
			</ul>
		</li>
		#end
		#end
		
</ul>
</div>
</div>
</body>
</html>
