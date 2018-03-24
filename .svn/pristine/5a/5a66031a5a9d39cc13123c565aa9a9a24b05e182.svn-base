<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("sysAcc.lb.yearCloseAcc")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script type="text/javascript">
	function retailSingle(){
		var varShop = document.getElementById("selectShop") ;
		var obj = new ActiveXObject("KoronCom.RetailSingle") ;
		if(typeof(varShop)!="undefined"){
			obj.ShopId = varShop.value ;
		}
		obj.url = "$!IP|$!port" ;
		obj.UserId = "$LoginBean.id" ;
		
		obj.Load();
	}
</script>
</head>
<body>

<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("common.lb.retailSingle")</div>
	<ul class="HeadingButton">
		<li></li>
	</ul>
</div>


<div id="listRange_id">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
	<div class="listRange_sys">
			<div class="sysAcctite">$text.get("common.lb.retailSingle")</div>
			#if("$!listShop"!="")
			<div class="sysAccconter"><span></span><br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					$text.get("common.lb.chooseStorefront")：

					<select id="selectShop">
					#foreach($shop in $listShop)
						<option value="$globals.get($shop,0)">$globals.get($shop,1)</option>
					#end
					</select>&nbsp;&nbsp;<br>
			</div>
			<div style="padding-left: 200px;">
				<button type="button"  onClick="retailSingle();" style="float: center;">$text.get("common.lb.retailSingle.login")</button>
			</div>
			#end
			<ul>
		</ul>
	</div>
</div>
</body>
</html>
