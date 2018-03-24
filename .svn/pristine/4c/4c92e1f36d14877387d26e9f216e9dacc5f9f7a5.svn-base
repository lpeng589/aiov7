<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("mrp.lb.mrpCount")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />

<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="$globals.js("/js/mrp.vjs","",$text)"></script>
<script language="javascript" src="/js/listGrid.js"></script>
<script type="text/javascript" src="/js/dragcols.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>

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
	var trackNos=getCbValue(cbs,form.trackNo); 
	if(!ids){
		alert("$text.get('common.msg.mustSelectOne')");
		return;
	}
	if(!confirm("$text.get('common.msg.confirmDel')"))return false;
	form.action = "/MrpBP.do?method=delMrp&trackNo="+trackNos+"&ids="+ids;
	form.submit();
}


var obj;
var field;
function openSelect1(urlstr,displayName,obj,field){
	window.obj = obj;
	window.field = field;
	var urlstr = urlstr+"&popupWin=SelectPopup&MOID=$MOID&MOOP=query&LinkType=@URL:&displayName="+encodeURI(displayName); 
	asyncbox.open({id:'SelectPopup',title:displayName,url:urlstr,width:800,height:470}); 
}

function exeSelectPopup(str){
	if(typeof(str)!="undefined"){
		var mutli=str.split("|"); 
		hid="";
		dis="";
		if(str.length>0){
			var len=mutli.length;
			if(len>1){len=len-1}
			for(j=0;j<len;j++){ 
				fs=mutli[j].split(";");
					dis=fs[1];
					hid=fs[0];
				if(hid.indexOf("@Sess:")>=0){
					document.getElementById(obj).value="";
					document.getElementById(field).value="";
				}else{
					document.getElementById(obj).value=dis;
					document.getElementById(field).value=hid;
				}
			}
		}else{
			document.getElementById(obj).value="";
			document.getElementById(field).value="";
		}
	}
}

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
	url = "/MrpBP.do?method=mrpDetSel&id="+orderId;
	var var1 = "";  
	dialogSettings = "Center:yes;Resizable:no;DialogHeight:500px;DialogWidth:750px;Status:no";
	rVal = window.showModalDialog( url, var1, dialogSettings );
}

//修改MRP信息
function updateMrp(trackNo,count,mrpFrom,bomNo){
	var form = document.getElementsByName("form")[0];
	form.action = "/MrpBP.do?method=bomDemand&opeType=upMrp&choose=true&bomNo="+bomNo+"&trackNo="+trackNo+"&count="+count+"&mrpFrom="+mrpFrom;

	form.submit();
}
function suopin(){
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
}
</script>
<style type="text/css">
.resizeDiv{width:2px;overflow:hidden; float:right; cursor:col-resize;}
.listRange_list_function thead td{
	padding-left:1px;
	padding-right:1px;
}
</style>
</head>
<body onLoad="showtable('tblSort');showStatus();sortables_init('tblSort');">
<form method="post" scope="request" name="form" action="/MrpBP.do">		
<div class="Heading">
	<div class="HeadingIcon">
		<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
	</div>
	<div class="HeadingTitle">$text.get("mrp.lb.mrpManager")</div>
	<ul class="HeadingButton">
		<li><button type="button"  onClick="form.submit();" class="b2">$text.get("common.lb.query")</button></li>
		<li><button type="button" name="addPre" onClick="location.href='/MrpBP.do?method=orderSel&winCurIndex=$winCurIndex'" class="b2">$text.get("mrp.lb.add")</button></li>
		#if($LoginBean.operationMap.get("/MrpBP.do").delete())
		<li><button type="button"  onClick="deleteList()" class="b2">$text.get("mrp.lb.delete")</button></li>
		#end
	</ul>
</div>
<div id="listRange_id">
	<div class="listRange_1">
		#if($globals.getVersion()=="8")
		<li><span>$text.get("mrp.lb.mrpFrom")：</span>
			<select name="mrpFrom" id="mrpFrom" onChange="form.submit();">	
				<option value="2" #if($!mrpFrom=="2") selected="selected" #end>$text.get("mrp.lb.salesOrder")</option>				
				<option value="1" #if($mrpFrom=="1") selected="selected" #end>$text.get("mrp.lb.produceOrder")</option>				
				#if($globals.getSysSetting("showMrpProc").equals("true"))<option value="0" #if($!mrpFrom=="0") selected="selected" #end>$text.get("mrp.lb.mlProduce")</option>#end				
			</select>
		</li>
		#else
			<input type="hidden" name="mrpFrom" value="2">
		#end
		<li><span>$text.get("mrp.lb.relationId")：</span><input type="input" name="billNo" id="billNo" value="$!billNo"></li>
		<li><span>$text.get("mrp.lb.goodsFullName")：</span><input name="goodsClassCode" id="goodsClassCode" type="hidden" value="$!goodsClassCode" /><input type="text" id="goods" name="goods" value="$!goods" onDblClick="openSelect1('/UserFunctionAction.do?operation=$globals.getOP("OP_POPUP_SELECT")&selectName=MrpSelectGoods','$text.get('mrp.lb.goodsFullName')','goods','goodsClassCode');" /><img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="openSelect1('/UserFunctionAction.do?operation=$globals.getOP("OP_POPUP_SELECT")&selectName=MrpSelectGoods','$text.get('mrp.lb.goodsFullName')','goods','goodsClassCode');" ></li>
		<li><span>$text.get("mrp.lb.employee")：</span><input name="employeeId" id="employeeId" type="hidden" value="$!employeeId" /><input type="input" id="employee" name="employee" value="$!employee" onDblClick="openSelect1('/UserFunctionAction.do?operation=$globals.getOP("OP_POPUP_SELECT")&selectName=SelectOAEmployeeName','$text.get('mrp.lb.employee')','employee','employeeId');" /><img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="openSelect1('/UserFunctionAction.do?operation=$globals.getOP("OP_POPUP_SELECT")&selectName=SelectOAEmployeeName','$text.get('mrp.lb.employee')','employee','employeeId');" ></li>
		<li><span>$text.get("mrp.lb.department")：</span><input name="departmentId" id="departmentId" type="hidden" value="$!departmentId" /><input type="input" id="department" name="department" value="$!department" onDblClick="openSelect1('/UserFunctionAction.do?operation=$globals.getOP("OP_POPUP_SELECT")&selectName=SelectDepartmentName','$text.get('mrp.lb.department')','department','departmentId');" /><img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="openSelect1('/UserFunctionAction.do?operation=$globals.getOP("OP_POPUP_SELECT")&selectName=SelectDepartmentName','$text.get('mrp.lb.department')','department','departmentId');" ></li>	
	</div>
	<div class="scroll_function_small_a" id="conter">
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
			<thead>
				<tr style="position:relative;top:expression(this.offsetParent.scrollTop);">
					<td width="30"><input type="checkbox" name="cb"  onclick="checkThisCks(this)"></td>
					<td width="80">$text.get("mrp.lb.mrpFrom")</td>
					<td width="110">$text.get("mrp.lb.relationId")</td>
					<td width="100">$text.get("mrp.lb.goodsNumber")</td>
					<td width="150">$text.get("mrp.lb.goodsFullName")</td>
					<td width="90">$text.get("mrp.lb.produceQty")</td>
					<td width="70">已下申请单</td>
					<td width="70">已下订单</td>
					#if($globals.getSysSetting("showMrpProc").equals("true"))
					<td width="90">$text.get("mrp.lb.storeQty")</td>
					<td width="90">$text.get("mrp.lb.storingQty")</td>
					<td width="90">$text.get("mrp.lb.productingQty")</td>
					<td width="90">$text.get("mrp.lb.usedQty")</td>
					#end
					<td width="110">BOM单号</td>
					<td width="100" onclick="sortTable(document.getElementById('tblSort'), 0,'date');">$text.get("mrp.lb.createDate")</td>
					<td width="60">$text.get("mrp.lb.employee")</td>
					<td width="70">$text.get("mrp.lb.department")</td>
					<td width="50">$text.get("mrp.lb.update")</td>
				</tr>								
			</thead>
			<tbody>
			#foreach($row in $list)
				<tr>
					<td align="center"><input type="checkbox" name="cb">
						<input type="hidden" name="tId" value="$!row.get("id")">
						<input type="hidden" name="trackNo" value="$!row.get("relationNo")">
					</td>
					<td align="center">
					#if($row.get("mrpFrom")=="1")$text.get("mrp.lb.produceOrder")#end #if($row.get("mrpFrom")=="2") $text.get("mrp.lb.salesOrder") #end
					#if($row.get("mrpFrom")=="0")$text.get("mrp.lb.mlProduce") #end &nbsp;
					</td>
					<td><a href="javascript:suopin();updateMrp('$row.get("relationNo")',$globals.formatNumberS($row.get("ProductQty"),false,false,"DigitsQty",""),$row.get("mrpFrom"),'$!row.get("bomNo")')">$!row.get("relationNo")</a></td>
					<td>$!row.get("goodsNumber")&nbsp;</td>
					<td>$!row.get("goodsName")&nbsp;</td>
					<td align="right">$globals.formatNumberS($!row.get("ProductQty"),false,false,"Qty","")&nbsp;</td>
				
					<td align="right">
					#foreach($erow in $globals.getEnumerationItems("hasCommon"))
						#if($erow.value==$!row.get("hasApp"))$erow.name#end	
					#end&nbsp;</td>
					<td align="right">
					#foreach($erow in $globals.getEnumerationItems("hasCommon"))
						#if($erow.value==$!row.get("hasOrder"))$erow.name#end	
					#end&nbsp;</td>
					#if($globals.getSysSetting("showMrpProc").equals("true"))
					<td align="right">$globals.formatNumberS($!row.get("StoreQty"),false,false,"DigitsQty","")&nbsp;</td>
					<td align="right">$globals.formatNumberS($!row.get("StoringQty"),false,false,"DigitsQty","")&nbsp;</td>
					<td align="right">$globals.formatNumberS($!row.get("ProductingQty"),false,false,"DigitsQty","")&nbsp;</td>
					<td align="right">$globals.formatNumberS($!row.get("UsedQty"),false,false,"DigitsQty","")&nbsp;</td>
					#end
					<td align="center"><a href='javascript:mdiwin("/UserFunctionQueryAction.do?tableName=tblBOM&operation=5&keyId=$!row.get("bomDetId")","工艺单")'>$!row.get("bomNo")&nbsp;</a></td>
					<td align="center">$!row.get("CreateDate")&nbsp;</td>
					<td align="center">$!row.get("emp")&nbsp;</td>
					<td align="center">$!row.get("deptFullName")&nbsp;</td>
					#if($!row.get("createBy").equals($!loginId)||$!loginId.equals("1")||$!updateOtherRight.indexOf($!globals.get("createBy").concat(";"))>=0||$!updateDeptRight.indexOf($!row.get("deptCode").concat(";"))>=0)				
					<td align="center"><a href="javascript:suopin();updateMrp('$row.get("relationNo")',$globals.formatNumberS($row.get("ProductQty"),false,false,"DigitsQty",""),$row.get("mrpFrom"),'$!row.get("bomNo")')">$text.get("mrp.lb.update")</a></td>
					#else
					<td align="center">$text.get("mrp.lb.update")</td>
					#end
				</tr>
			#end
			</tbody>
		</table>
	</div>				
</div>

<script type="text/javascript">
$("#conter").height($(window).height()-130);
</script>
<div class="listRange_pagebar"> $!pageBar </div>
<input type="hidden" name="operation" value="">
</form>
</body>
</html>
