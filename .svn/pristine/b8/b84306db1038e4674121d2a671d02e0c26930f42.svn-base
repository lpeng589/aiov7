<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/style/css/about.css" type="text/css">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script type="text/javascript">

function changeremainpass(id){
	obj = document.getElementById(id);
	obj.checked = !obj.checked;
}

var langNum=$langNum;
function save(){
	if(langNum>1){
		items = document.getElementsByName("saveLang");
		snum=0;
		def=false;
		for(i=0;i<items.length;i++){
			if(items[i].checked){
				snum++;
				if(items[i].value==document.form.defaultLang.value){
					def = true;
				}
			}
		}
		if(!def&&snum>0){
			alert("$text.get("language.msg.mustDefault")");
			return false;
		}
		if(snum>langNum){
			alert("$text.get("language.msg.maxlang")："+langNum);
			return false;
		}
	}
	document.form.isSave.value="true";
	document.form.submit();
}
</script>
<style type="text/css">
#bodyul li{padding:0 0 0 0;overflow:hidden;}
</style>
</head>
<body >
<form method="post" action="/UpgradeAction.do?type=6" name="form" onKeyDown="keyDownOp()" >
<input type=hidden name="isSave" value=false/>
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
    <div id=pageTitle><span>$text.get("language.lb.selSyslang")  &nbsp;&nbsp;&nbsp;&nbsp;$text.get("language.msg.sellangnum",[$langNum])</span><div>
	</div></div>
	<div id="bodyUl">
		<ul>
			<li>
				<div class="itemName" style="text-align:left"> <span>
				$text.get("language.lb.deflang") <select name="defaultLang"> 
				#foreach($row in $sysLocale) 
				<option value = "$row" #if("$globals.getSysSetting('defaultlanguage')"=="$row") selected #end> $globals.getLanguage($row)</option>
				#end</select></span></div>				
			</li>
			#if($langNum >1)
			#foreach($row in $sysLocale)
			<li>
				<div class="itemName" style="text-align:left"> <input type="checkbox" name="saveLang" id="$row" value="$row" #if($!userLocale.get("$row") != "") checked #end>
				<font onclick="changeremainpass('$row')">$globals.getLanguage($row)</font> 
				
				</div>			
			</li>
			#end
			#end
			<li><button type="button" onClick="save()">$text.get("common.lb.save")</button></li>
     
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