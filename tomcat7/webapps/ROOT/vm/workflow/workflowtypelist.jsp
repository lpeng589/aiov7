<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.workflow.create")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<style>
.re_cer {
	WIDTH: 100%;
	float:left;
	height:auto;
}
.pro_con {
	float:left;
	PADDING-TOP: 10px; ZOOM: 1;
}
.pro_con LI {
	PADDING-BOTTOM: 5px; MARGIN: 0px 6px; ZOOM: 1;
	width:280px;
}

.pro_right {
	FLOAT: left; MARGIN-LEFT: 10px; WIDTH: 300px; LINE-HEIGHT: 22px;
}
.pro_right DT strong {
	DISPLAY: inline; FONT-SIZE: 14px;   COLOR: #000;
}
.pro_right a:link {
	COLOR: #0000ff;
	text-decoration:underline;
}

.pro_right a:visited {
	COLOR: #0000ff;
	text-decoration:underline;
}
.pro_right a:hover {
	color:#FF6600;
	text-decoration:underline;
	}
.pro_right a:active {
	COLOR: #0000ff;
	text-decoration:underline;
}

.pro_right DT {
	font-size:14px;
	padding-bottom:3px;
}
.pro_right DD {
	margin-left:5px;
	PADDING-LEFT: 15px; BACKGROUND: url(/$globals.getStylePath()/images/ico_li.gif) no-repeat left 40%; COLOR: #666;
}

</style>
</head>

<body>
<iframe name="formFrame" style="display:none"></iframe>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
		<div class="HeadingTitle">$text.get("oa.workflow.create")</div>
</div>
<div id="listRange_id">
<script type="text/javascript">
var oDiv=document.getElementById("listRange_id");
var sHeight=document.body.clientHeight-38;
oDiv.style.height=sHeight+"px";

function showreModule(title,href)
    {		
    	top.showreModule(title,url);	
		
    }
</script>
</script>
	<div class="oa_work">
		<div class="re_cer">
			#foreach($pal in $listpal)
			<ul class="pro_con">
				<li>
                    <dl class="pro_right">
                        <dt><strong>$!globals.get($pal,1)</strong></dt>
                        #foreach($flow in $list)
                           #if($flow.ClassCode==$!globals.get($pal,0))
                               <dd><a href="javascript:showreModule('$globals.getTableDisplayName("$flow.TableName")','/UserFunctionQueryAction.do?tableName=$flow.TableName&src=menu&operation=6');">$globals.getTableDisplayName("$flow.TableName")</a></dd>
                           #end
                        #end
                        #foreach($file in $tempList)
                        	#if(1==$LoginBean.id || $globals.canVisit($globals.get($file,5)))
	                        	#if($globals.get($file,4)==$globals.get($pal,0))
	                        		<dd><a href="javascript:showreModule('$globals.get($file,1)','/OAFileWorkFlowAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&templateId=$globals.get($file,0)')">
	                        			 $globals.get($file,1)
	                        		</a>
	                        		</dd>
	                        	#end
                        	#end
                        #end
                    </dl>
				</li>	 	 
             </ul>
            #end
		 </div>
	</div>
</body>

</html>
