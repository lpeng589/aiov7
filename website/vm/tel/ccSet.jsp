<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/style/css/about.css" type="text/css">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script type="text/javascript">

function save(){
	
	if(document.form.ccIP.value=="" && document.form.ccState.value=="0"){
		alert("中心IP$text.get("common.validate.error.null")");
		return;
	}
	document.form.op.value="save";
	document.form.submit();
}
</script>
<style type="text/css">
.itemname{float:none;}
</style>
</head>
<body >
<form method="post" action="/TelAction.do?operator=ccSet" name="form" onKeyDown="keyDownOp()" >
<input type=hidden name="op" value=''/>
<div id="listRange_id"  align="center">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-18;
			oDiv.style.height=sHeight+"px";
		</script>
<div id="contentleft">
		    <div id="contentright">
		    <div id="contentMain"> 
<div id="Content">
    <div id=pageTitle><span>$text.get("call.center .setting") &nbsp;&nbsp;&nbsp;&nbsp;</span><div>
	</div></div>
	<div id="bodyUl">
		<ul>
			<li>
				<div class="itemName" style="text-align:left"> 
				$text.get("call.center") <select name="ccState" style="width:200px">   
				#foreach($row in $globals.getEnumerationItems("IsAble")) 
				<option value = "$row.value" #if("$row.value"==$result.get("ccState")) selected #end> $row.name</option>
				#end</select></div>				
			</li>
			<li>
				<div class="itemName" style="text-align:left"> $text.get("center.IP")
				<input type="text" name="ccIP" style="width:200px" value="$result.get("ccIP")"/></font> 
				
				</div>			
			</li>
			<li>
				<div class="itemName" style="text-align:left"> $text.get("center.port")
				<input type="text" name="ccPort" style="width:200px;" value="$result.get("ccPort")"/></font> 
				
				</div>			
			</li>
			<li>
				<div class="itemName" style="text-align:left"> $text.get("record.file.path")
				<input type="text" name="ccAudioFile" style="width:200px;" value="$result.get("ccAudioFile")"/></font> 
				
				</div>			
			</li>
			<li>
				<div class="itemName" style="text-align:left"> $text.get("area.code")
				<input type="text" name="ccPrex" style="width:200px;" value="$result.get("ccPrex")"/></font> 
				
				</div>			
			</li>
			<li><button type="button" onclick="save();">$text.get("common.lb.save")</button></li>
     
		</ul>
	</div>
		    </div>
            </div>
	    </div>
            </div>
	    </div>
</form>	    
</body>
</html>