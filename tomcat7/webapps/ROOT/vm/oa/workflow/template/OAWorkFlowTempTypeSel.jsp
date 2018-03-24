<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link rel="stylesheet" href="/style/css/about.css" type="text/css">
</head>
<body>
<form  method="post" scope="request" name="form" action="/OAWorkFlowTempAction.do" >
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD_PREPARE")">
<input type="hidden" name="step" value="next">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("oa.workflow.title.selTempType")</div>
	<ul class="HeadingButton">	  
	<li>
	<button type="button"  onClick="location.href='/OAWorkFlowTempQueryAction.do?operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button>
	</li>
	</ul>
</div>
<div id="listRange_id"  align="center">
	<div style="margin-top:15px">
<div id="contentleft">
		    <div id="contentright">
		    <div id="contentMain"> 
<div id="Content">
    <div id=pageTitle><div>
	</div></div>
	<div id="bodyUl" style="text-align:center;" >
		<ul style="margin:0px; padding:0px">		
			<li>
					<a href="/OAWorkFlowTempAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&step=next&type=1&winCurIndex=$!winCurIndex&moduleType=$!moduleType" class="regbut" style="text-align:center">$text.get("oa.workflow.lb.commonTemp")</a><br/><br/>
					<!-- <a href="/OAWorkFlowTempAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&step=next&type=2&winCurIndex=$!winCurIndex" class="regbut" style="text-align:center">$text.get("oa.workflow.lb.wordTemp")</a><br/><br/>					
					<a href="/OAWorkFlowTempAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&step=next&type=3&winCurIndex=$!winCurIndex" class="regbut" style="text-align:center">$text.get("oa.workflow.lb.xlsTemp")</a><br/><br/>
					 -->
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
