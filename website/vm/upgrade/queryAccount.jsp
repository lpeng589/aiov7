<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/style/css/about.css" type="text/css">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<style type="text/css">
#bodyul li{padding:0 0 0 0;overflow:hidden;}
</style>
</head>
<body >
<div id="listRange_id"  align="center">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-18;
			oDiv.style.height=sHeight+"px";
		</script>
<div id="contentleft" style="margin-top:10px;height:100%;"> 
		    <div id="contentright">
		    <div id="contentMain"> 
<div id="Content">
    <div id=pageTitle><span>加密狗对应正常帐套</span><div>
	</div></div>
	<div id="bodyUl" style="margin-top:10px">
		<ul >
			#foreach($row in $list)
			<li>
				<div class="itemName" style="width:100px;padding-right:50px">$globals.get($row,0)</div>	$globals.get($row,1)			
			</li>  
      		#end
     
		</ul>
	</div>
		    </div>
            </div>
	    </div>
            </div>
	    </div>
</body>
</html>