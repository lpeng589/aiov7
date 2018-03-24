<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.apply.type")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">

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
<div id="listRange_id">
<script type="text/javascript" language="javascript">
function closeWin(tableFullName,tableName)
{
	
	var str=tableFullName+","+tableName;
	window.parent.returnValue = str;
	window.close();
}


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
                                <dd><a href="#" onClick="closeWin('$globals.getTableDisplayName("$flow.TableName")','$flow.TableName')">$globals.getTableDisplayName("$flow.TableName")</a></dd>
                                	#end
                                #end
                               
                              </dl>
						</li>	 	 
                  </ul>
                  #end
				 
		      </div>
		
		
	</div>
	  </div>
	  
</body>

</html>
