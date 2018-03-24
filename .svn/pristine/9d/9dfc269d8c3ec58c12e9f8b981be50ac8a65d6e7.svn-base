<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>#if("$type" == "1") 
		$text.get("upgrade.title.module") 
	#elseif("$type" == "2")
		$text.get("upgrade.title.user") 
	#elseif("$type" == "3")
		$text.get("upgrade.title.language") 
	#elseif("$type" == "4")
		$text.get("upgrade.title.function") 
	#end</title>
<link type="text/css" rel="stylesheet" href="/style/css/about.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<style type="text/css">
.wrap{text-align:center;}
#bodyul{display:inline-block;}
</style>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/validate.vjs"></script>
<script type="text/javascript">
$(function(){
	$("#scroll-wrap").height(document.body.clientHeight-18);
});
function beforSubmit(form){  
	if(document.form.type.value == 2 ){
		if(!isInt(document.form.userNum.value)){
			alert("$text.get("upgrade.lb.userNum")$text.get('common.validate.error.integer')");
			return false;
		}
		if(document.form.userNum.value > 4999){
			alert('$text.get("upgrade.lb.userNum")$text.get("common.validate.error.larger")4999');
			return false;
		}else if(document.form.userNum.value <= $globals.getSystemState().userNum){
		alert('$text.get("upgrade.lb.userNum")$text.get("common.validate.error.less")$globals.getSystemState().userNum');
			return false;
		
		}
	}
	if(document.form.type.value == 3 ){
		if(!isInt(document.form.languageNum.value)){
			alert("$text.get("upgrade.lb.languageNum")$text.get('common.validate.error.integer')");
			return false;
		}
		if(document.form.languageNum.value > 11){
			alert('$text.get("upgrade.lb.languageNum")$text.get("common.validate.error.larger")11');
			return false;
		}else if(document.form.languageNum.value <= $globals.getSystemState().languageNum){
		alert('$text.get("upgrade.lb.languageNum")$text.get("common.validate.error.less")$globals.getSystemState().languageNum');
			return false;
		
		}
	}
	disableForm(form);
	return true;
}	

function setImgSize()
{
	var maxwidth=400;
	var imgs = document.images;
	var length = imgs.length;
	for(var i=0;i<length;i++)
	{
		var img = imgs[i];
		var heigth = img.offsetHeight;
		var width = img.offsetWidth;
		var p = heigth/width;
		if(width>maxwidth)
		{
			var newWidth = maxwidth;
			var newHeigth=newWidth*p;
			img.width=newWidth;
			img.height=newHeigth;
			
		}
	}
}
</script>
</head>

<body onLoad="showStatus();setImgSize()" class="html">
<form  method="post" scope="request" name="form" action="/UpgradeAction.do" onSubmit="return beforSubmit(this);" >
<input type="hidden" name="operation" value="$globals.getOP("OP_UPGRADE_PREPARE")">
<input type="hidden" name="step" value="one">
<input type="hidden" name="type" value="$type">
<input type="hidden" name="exig" value="$exig">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<div id="scroll-wrap">

	<div class="wrap">
	
	<div id=pageTitle>
	#if("$type" == "1") 
		$text.get("upgrade.title.module") 
	#elseif("$type" == "2")
		$text.get("upgrade.title.user") 
	#elseif("$type" == "3")
		$text.get("upgrade.title.language") 
	#elseif("$type" == "4")
		$text.get("upgrade.title.function") 
	#end
	</div>
	<div id="bodyUl">
			<ul>
			<li>
				<div class="itemName">$text.get("upgrade.lb.id")：</div>$code
			</li>			
	#set($hasu = false)		
	#if("$type" == "1") 
		<li><div class="itemName">$text.get("upgrade.lb.iModule")：</div> 
			#foreach($erow in $globals.getEnumerationItems("MainModule"))
			#if($globals.hasMainModule($erow.value)) 
						$erow.name  &nbsp;&nbsp;
			#end
			#end
		</li>			
		
		#foreach($erow in $mainModule)
		#if(!$globals.hasMainModule($erow.value)&&$erow.value != "0")
			#set($hasu = true)
		#end
		#end
		
		#if($hasu == true)				
			<li><div class="itemName">$text.get("common.lb.plsSelete")$text.get("upgrade.lb.module")：</div>
			<select name="module" >
			#foreach($erow in $mainModule)
			#if(!$globals.hasMainModule($erow.value)&&$erow.value != "0")
				<option value="$erow.value" >$erow.name</option>
			#end
			#end
			</select></li>
		#else
			<li><div class="itemName"></div>$text.get("upgrade.msg.noUmodule")</li>
		#end
	#elseif("$type" == "2")
	#set($hasu = true)		
		<li><div class="itemName">$text.get("upgrade.lb.iuser")：</div> $globals.getSystemState().userNum</li>
		<li><div class="itemName"> $text.get("common.lb.plsEnter")$text.get("upgrade.lb.userNum")：</div>
		<input name="userNum" type="text" value="" class="intext" onFocus="this.className='intext intext_hover'"  onblur="this.className='intext'" maxlength="50"> </li>	
	#elseif("$type" == "3")
		<li><div class="itemName">$text.get("upgrade.lb.ilanguage")：</div> $globals.getSystemState().languageNum
		</li>
	#set($hasu = true)
		<li><div class="itemName"> $text.get("common.lb.plsEnter")$text.get("upgrade.lb.languageNum")：</div><input name="languageNum" type="text" value="" class="intext" onFocus="this.className='intext intext_hover'"  onblur="this.className='intext'" maxlength="50"></li>
	#elseif("$type" == "4")
	  #set($hasu = false)
		<li><div class="itemName">$text.get("upgrade.lb.ifunction")：</div>
		#if($globals.getSystemState().funUserDefine) $text.get("upgrade.lb.userDefine") #else #set($hasu = true)  #end	
		#if($globals.getSystemState().funMoreCurrency) &nbsp;&nbsp;&nbsp; $text.get("upgrade.lb.moreCurrency") #else #set($hasu = true)   #end	
		#if($globals.getSystemState().funProduct) &nbsp;&nbsp;&nbsp; $text.get("upgrade.lb.poroduct") #else #set($hasu = true)   #end	
	
		</li>	
	
		#if($hasu == false )
		<li><div class="itemName"></div>$text.get("upgrade.msg.noFunctionUpgrade")</li>
		#else
		<li><div class="itemName">$text.get("common.lb.plsSelete")$text.get("upgrade.lb.functionNum")：</div>
		<select name="functionNum" >
		#if(!$globals.getSystemState().funUserDefine) <option value="51">$text.get("upgrade.lb.userDefine")</option> #end	
		#if(!$globals.getSystemState().funMoreCurrency) <option value="53">$text.get("upgrade.lb.moreCurrency")</option> #end			
		#if(!$globals.getSystemState().funProduct) <option value="54">$text.get("upgrade.lb.poroduct")</option> #end	
	
		</select>
		#end
	#end
				<li id="titleDiv"></li>
			</ul>
		
		
	</div>
	<div style="padding:0 0 15px 0;">
		#if($hasu == true)
		<button type="submit" name="Submit" class="btn btn-danger btn-small">$text.get("common.lb.nextT")</button>
		#end
		<button type="button" onclick="window.location.href='/RegisterAction.do?regFlag=2&winCurIndex=$!winCurIndex';" class="btn btn-small">$text.get("common.lb.back")</button>
	</div>
</div>



</div>
</form> 
</body>
</html>



