<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("com.consign.setting")</title>
<link rel="stylesheet" href=" /$globals.getStylePath()/css/ListRange.css" type="text/css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script>

function openInputTime(obj){
	WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'});
}

function fillData(datas){
	newOpenSelectSearch(datas,fieldNames,fieldNIds);
	jQuery.close('Popdiv');
}
</script>
</head>

<body>

<form action="/WokeConsignLogAction.do?operation=$globals.getOP("OP_UPDATE")" name="form" method="post">
<input type="hidden" name="id" value="$wokecongin.id"/>
<div class="Heading">
	<div class="HeadingIcon"><img src=" /$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("com.consign.setting")</div>
	<ul class="HeadingButton">
	<li>
	<button type="submit" name="Submit" class="b2">$text.get("common.lb.save")</button></li>
	<li><button type="button" onClick="javascritp:location='/WokeConsignLogAction.do'" class="b2">$text.get("common.lb.back")</button>	
	</li>
		
	</ul>
</div>
<div id="listRange_id" align="center">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
				<div class="listRange_div_jag_hint" style="height:200px;">
					<div class="listRange_div_jag_div"><span>$text.get("com.work.consign")</span></div>					
						<ul class="listRange_woke" style="padding:5px 0 0 0;overflow:hidden;">
							<li>
								<span>$text.get("com.work.confignor")：</span>
								<input type="hidden" id="CongignUserID" name="CongignUserID" value="$wokecongin.CongignUserID"/>
								<input type="text" id="consignName" name="consignName" value="$wokecongin.Consigusername" onKeyDown="if(event.keyCode!=13&&event.keyCode!=9)event.returnValue=false;"  onDblClick="deptPopForAccount('userGroup','consignName','CongignUserID');" class="text"/>
								<img src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForAccount('userGroup','consignName','CongignUserID');" class="search"/>
							</li>
							<li>
								<span>$text.get("com.begin.time")：</span>
								<input class="text" type="text" name="beginTime" value="$!wokecongin.beginTime" onKeyDown="if(event.keyCode==13){ if(this.value.length>0){ event.keyCode=9; }else{  openInputTime(this);}}" onClick="openInputTime(this);" />
							</li>	
							<li>
								<span>$text.get("com.end.time")：</span>
								<input class="text" type="text" name="endTime" value="$!wokecongin.endTime" onKeyDown="if(event.keyCode==13){ if(this.value.length>0){ event.keyCode=9; }else{  openInputTime(this);}}" onClick="openInputTime(this);" />
							</li>	
							<li>
								<span>$text.get("excel.list.isUsed")：</span>
								<input type="radio" name="isStart" value="1" #if($wokecongin.State==1) checked #end>$text.get("oa.common.yes")
								<input type="radio" name="isStart" value="0" #if($wokecongin.State==0) checked #end>$text.get("oa.common.no")
							</li>															
						</ul>
				</div>																		
	</div>			

</form>
</body>
</html>
