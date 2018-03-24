<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.common.communicationManger")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script> 
<script type="text/javascript">
function selectClient(){
	var keyIds = document.getElementsByName("keyId") ;
	var keyId = "" ;
	for(var i=0;i<keyIds.length;i++){
	   if(keyIds[i].checked){
		  keyId = keyIds[i].value ;
	   }	    
	}	
	window.returnValue = keyId ;
	window.close() ;
}

function clickClient(keyId){
	window.returnValue = keyId ;
	window.close() ;
}
function loadBefore(){
	#if($listClient.size()==1)
		window.returnValue = "$globals.getList($listClient,0).id" ;
		window.close() ;
	#end
}
</script>
</head>

<body onload="loadBefore();">
<div class="oamainhead">
<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
<div class="HeadingTitle">$text.get("SelectClient.crm.service.error")</div>
<div class="HeadingButton">
<li><button type="button" class="b2" onclick="selectClient();">$text.get("button.lable.sure")</button></li>
<li><button type="button" class="b2" onclick="javascript:window.close();">$text.get("common.lb.close")</button></li>
</div>
</div>
    <div id="oalistRange">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table">
		<thead>
			<tr>
				<td width="35" class="oabbs_tc">$text.get("mrp.lb.chooseOne")</td>
				<td width="200" class="oabbs_tc">$text.get("crm.client.name")</td>
				<td width="80" class="oabbs_tc">$text.get("crm.client.type")</td>
				<td width="100" class="oabbs_tc">$text.get("oa.common.tel")</td>
				<td width="*" class="oabbs_tc">$text.get("common.lb.address")</td>
			</tr>
		</thead>
		<tbody>
		 #foreach($client in $listClient)
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);" ondblclick="clickClient('$client.id');">
				<td>
				  <input type="radio" name="keyId" value="$client.id" #if($velocityCount==1)checked #end/></td>
				<td align="center">$!client.clientName &nbsp;</td>
				<td align="center">$!globals.getEnumerationItemsDisplay("ClientStyle",$client.clientType,"$globals.getLocale()") &nbsp;</td>
				<td align="center">$!client.phone &nbsp;</td>
				<td align="center">$!client.address &nbsp;</td>
			</tr>
		#end
		  </tbody>
		</table>
		</div>
	</div>
	<div class="listRange_pagebar"> $!pageBar </div>
</body>
</html>

