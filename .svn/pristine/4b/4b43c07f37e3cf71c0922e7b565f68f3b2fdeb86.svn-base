<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>$text.get("mrp.lb.mrpCount")</title>
		<link rel="stylesheet"
			href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
		<script language="javascript" src="/js/function.js"></script>
		<script type="text/javascript" src="$globals.js("/js/mrp.vjs","",$text)"></script>
		<script type="text/javascript">
function MatereilAnalse(){
	var mrpFrom = form.mrpFrom.value;

	form.action = "/MrpBP.do?method=ma";
	form.submit();
}
function deleteList(){
	var cbs = form.cb;
	var txts = form.tId;
	var ids = getCbValue(cbs,txts);
	if(!ids){
		alert("$text.get('common.msg.mustSelectOne')");
	}
	form.action = "/MrpBP.do?method=delMrp&ids="+ids;
	form.submit();
}
//还有点问题








function getCbValue(cbs,txts){
	var val = "";
	
	try{	
		for(i=1;i<cbs.length;i++){
			if(cbs[i].checked){
				val+=txts[i-1].value+",";
			}
		}
		val = val.substr(0,val.length-1);		
	}catch(e){
		val = txts.value;		
	}
	return val;	
}
function mrpDetSel(orderId){
	url = "/MrpBP.do?method=mrpDet?orderId";
	var var1 = "";  
	dialogSettings = "Center:yes;Resizable:no;DialogHeight:500px;DialogWidth:750px;Status:no";
	rVal = window.showModalDialog( url, var1, dialogSettings );
	
}

</script>
	</head>
	<body onLoad="showtable('tblSort');showStatus(); ">
		<form method="post" scope="request" name="form" action="/MrpBP.do">
			<div>
				<div class="HeadingIcon">
					<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
				</div>
				<div class="HeadingTitle">
					$text.get("mrp.lb.mrpOrderDet")
				</div>
				<ul class="HeadingButton">
	</ul>

			</div>
			<div id="listRange_id">
				<div class="scroll_function_small_a" id="conter">
					<table border="0" cellpadding="0" cellspacing="0"
						class="listRange_list_function" name="table" id="tblSort" width="100%">
						<thead>
							<tr style="position:relative;top:expression(this.offsetParent.scrollTop);">
							<!--  
							<td>$text.get("mrp.lb.mrpId")<br></td>
							-->
							<td width="100">$text.get("mrp.lb.bomNumber")</td>
							<td width="100">$text.get("mrp.lb.bomName")</td>
							<td>$text.get("mrp.lb.productQty")</td>
							 <td>$text.get("mrp.lb.storeQty")</td>
							 <td>$text.get("mrp.lb.storingQty")</td>
							 <td>$text.get("mrp.lb.productingQty")</td>
							 <td>$text.get("mrp.lb.usedQty")</td>
							 <td>$text.get("mrp.lb.neededQty")</td>
							 <td width="110">$text.get("mrp.lb.createDate")</td>
							 <!-- 
							 <td>$text.get("mrp.lb.employee")</td>
							 <td>$text.get("mrp.lb.department")</td>
							 
							 <td>$text.get("mrp.lb.scompany")</td>
							  -->
							</tr>
						</thead>
						<tbody>
						#foreach($row in $list)
						<tr>
						<!-- 
						<td>$!globals.get($row,1)&nbsp;</td>
						 -->
						<td>$!globals.get($row,14)&nbsp;</td>
						<td>$!globals.get($row,2)&nbsp;</td>
						<td align="right">$!globals.get($row,3)&nbsp;</td>
						<td align="right">$!globals.get($row,4)&nbsp;</td>
						<td align="right">$!globals.get($row,6)&nbsp;</td>
						<td align="right">$!globals.get($row,7)&nbsp;</td>
						<td align="right">$!globals.get($row,8)&nbsp;</td>
						<td align="right">$!globals.get($row,9)&nbsp;</td>
						<td>$!globals.get($row,10)&nbsp;</td>
						<!-- 
						<td>$!globals.get($row,11)&nbsp;</td>
						<td>$!globals.get($row,12)&nbsp;</td>
						 -->
						<!-- 
						<td>$!globals.get($row,13)&nbsp;</td>
						 -->
						</tr>
						#end
						</tbody>
					</table>
				
</div>
				</div>
		</form>
	</body>
</html>
