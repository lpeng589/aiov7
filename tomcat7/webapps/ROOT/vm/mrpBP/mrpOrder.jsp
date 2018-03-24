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
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/dragcols.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>

<script type="text/javascript">
function changeMrpFrom(mrpFrom){
	if(mrpFrom==0){
		simulate();
		return;
	}
	if(mrpFrom==1){
		alert("the order is not exist!");
		n("mrpFrom")[0].selectedIndex=1;
		return;
	}
	form.action = "/MrpBP.do?method=orderSel&mrpFrom="+mrpFrom;
	form.submit();
}
var rVal;
var intervalId;
var isML = false;//现在是否是模拟定单

var mnId = "";
var isTitleAdd=false;//动态属性表头是否已添加
function simulate(){
	$('mrpFrom').selectedIndex=2;
	if(!isML){
		delTableRow('tblSort');//存在的数据行
		$("tblSort").firstChild.appendChild(getTitleTr());
	}
	isML=true;
	var url = "/vm/mrp/simulateOrder.html";
	url = "/MrpBP.do?method=simulateOrder&mnId="+mnId;
	var var1 = "";  
	dialogSettings = "Center:yes;Resizable:no;DialogHeight:500px;DialogWidth:750px;Status:no";
	rVal = window.showModalDialog( url, var1, dialogSettings );
	//alert(rVal);
	var infos = rVal.replace(" ","").split(";");
	mnId = infos[1];
	addRowInfo(infos);
	//simulate1();
	//intervalId = window.setInterval("flushInfo()",500);	
}
//当点击模拟后每500毫秒都会执行一次，直到返回值不为空
function flushInfo(){
	if(!rVal)return;
	if((""+rVal)!="cancel")
		simulate1();
	window.clearInterval(intervalId);
}
function getTitleTr(){
	var tr = document.createElement("tr");
	str='<input type="checkbox" class="cbox" name="cb" value="" checked="checked">&nbsp;';
	addNewTd(tr,str,true);//加上表头
	addNewTd(tr,"$text.get("mrp.msg.source")",false);//加上表头
	addNewTd(tr,"$text.get("mrp.lb.maOrderNumber")",false);//加上表头
	addNewTd(tr,"$text.get("mrp.lb.goodsNumber")",false);//加上表头
	addNewTd(tr,"$text.get("mrp.lb.goodsName")",false);//加上表头
	addNewTd(tr,"$text.get("mrp.lb.produceQty")",false);//加上表头
	addNewTd(tr,"$text.get("mrp.lb.needDate")",false);//加上表头
	addNewTd(tr,"$text.get("mrp.lb.employee")",false);//加上表头
	addNewTd(tr,"$text.get("mrp.lb.department")",false);//加上表头
	//addNewTd(tr,"分支机构",false);//加上表头
	return tr; 
}
function addRowInfo(infos){
	//alert(infos);
	var tr = document.createElement("tr");
	str='<input type="checkbox" class="cbox" name="cb" value="" checked="checked">&nbsp;';
	addNewTd(tr,str,true);//加上复选框
	str = "$text.get("mrp.lb.mlOrder")";
	addNewTd(tr,str,false);//加上订单来源
	str ='<input name="orderId" value="'+infos[1]+'" readonly="readonly">';
	addNewTd(tr,str,true);//加上关联ID
	str = '<input name="goodsID" value="'+infos[2]+'" type="hidden">';
	str +='<input name="goodsNumber" value="'+infos[3]+'" readonly="readonly">';
	addNewTd(tr,str,true);//加上商品编码
	str ='<input name="goodsName" value="'+infos[4]+'" readonly="readonly">';
	addNewTd(tr,str,true);//加上商品名

		
	str = '<input name="count" value="'+infos[5]+'">';
	addNewTd(tr,str,true);//加上投产数量
	str ='<input name="billDate" value="'+infos[6]+'">';
	addNewTd(tr,str,true);//加上需求日期


	str ='<input type="hidden" name="EmployeeID" value="'+infos[7]+'">';
	str +='<input name="employee" value="'+infos[8]+'" readonly="readonly">';
	addNewTd(tr,str,true);//经手人


	str ='<input type="hidden" name="DepartmentCode" value="'+infos[9]+'">';
	str += '<input name="department" value="'+infos[10]+'" readonly="readonly">' ;
	addNewTd(tr,str,true);//部门
	str ='<input name="scompany" value="'+infos[11]+'" readonly="readonly">' ;
	//addNewTd(tr,str,true);//分支机构
	//---------动态属性


	prop = infos[12].split("/");
	//alert(prop);
	if(prop.length>1){//如果存在属性		
		var titleTr = $("tblSort").firstChild.firstChild;
		len = (prop.length-1)/3;//值，中文名，字段名


		if(!isTitleAdd){//如果没有加表头

			isTitleAdd = true;
			for(i=0;i<len;i++){	
				str=prop[len+i]+" ";
				addNewTd(titleTr,str,false);
			}
		}
		for(i=0;i<len;i++){				
			str='<input name="'+prop[2*len+i]+'" value="'+prop[i]+' " readonly="readonly">';
			addNewTd(tr,str,true);
		}
	}
	//---------
	$("tblSort").lastChild.appendChild(tr);
}

function simulate1(){
	var tbl = document.getElementById("tblSort");
	//如果没有数据行时，复制不起作用

	//var tr = tbl.lastChild.lastChild.cloneNode(true);
	var tr = document.createElement("tr");
	for(i=0;i<tbl.lastChild.firstChild.childNodes.length;i++){
		var td = document.createElement("td");
		td.innerHTML="&nbsp;";
		tr.appendChild(td);
	}
	//改变单元格内容为传回的数据

	var cells = tr.childNodes;
	cells[0].innerHTML="<input type='checkbox' class='cbox' name='cb' checked>";
	alert(rVal);
	for(i=1;i<cells.length;i++){
		cells[i].innerHTML=rVal[i-1]+"&nbsp;";
	}
	cells[7].innerHTML='<input name="count" value="'+rVal[6]+'">'
	tbl.lastChild.appendChild(tr);
}
//清除选中数据，只是不显示，数据库不变
function delRow(){
	var tName = "tblSort";
	var rows = getRows(tName);
	var indexs = getCheckedIndex1("cb");
	if(!indexs.length){alert("$text.get('mrp.msg.Noselect')");}
	for(i=indexs.length-1;i>=0;i--){
	//alert(rows[indexs[i]].innerHTML);
		$(tName).deleteRow(indexs[i]+1);
	}
}
function onsub(){
	if(isML){//如果展开的是模拟订单
		var mrpFrom = "0";//订单来源为0
		var cbs = n("cb");
		var cb = new Array();
		var flag = false;
		for(i=1;i<cbs.length;i++){
			cb[cb.length]=cbs[i].checked;
		}
		if(cb.toString().indexOf("true")!=-1){	
			flag = true;
		}
		if(!flag){
			alert("$text.get('mrp.msg.NoCheckOrderList')");
			return;
		}
		form.action="/MrpBP.do?method=bomSelPro&mrpFrom="+mrpFrom+"&cb="+cb;
		form.submit();
		return;
	}
	var indexs = getCheckedIndex1("cb");//被选中的复选框的下标，不包括全选功能复选框
	var orderIdTmp = document.getElementsByName("cb");//获得所有复选框
	var countsTmp = document.getElementsByName("count");//获得所有投产数量文本框
	var EmployeeIDTmp = document.getElementsByName("EmployeeID");
	var orderIds = new Array();//用于存放被选中的订单明细ID
	var counts = new Array();//用于存放投产数量
	var employeeIds = new Array();//用于存放经手人


	for(i=0;i<indexs.length;i++){
		index = indexs[i];
		orderIds[orderIds.length]=orderIdTmp[index+1].value;//获得选中订单明细ID
		counts[counts.length]=countsTmp[index].value;//获得选中的投产数量

		employeeIds[employeeIds.length] = EmployeeIDTmp[index].value;
	}	
	if(orderIds.length==0){
		alert("$text.get('mrp.msg.SelectOrderList')");
		return;
	}
	form.action="/MrpBP.do?method=bomSelPro&orderIds="+orderIds+"&counts="+counts;
	form.submit();
	
}
	function showDate(obj){
		WdatePicker();
	}
	function changeSelCondition(obj){
	try{
		var txt = $("selTxt");
		txt.name=obj.value;
		if(obj.value=="date"){		
			$("selTxt2").style.display="";
			$("lab2").style.display="";
		}else{
			$("selTxt2").style.display="none";
			$("lab2").style.display="none";
		}
		txt.value="";	
		}catch(e){alert(e.message)}
	}
	function selectOrder(){
		
		var form = document.getElementsByName("form")[0];
		form.action = "/MrpBP.do?method=orderSel";
		form.submit();
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
<body onLoad="showtable('tblSort');showStatus();checkAll('cb',true);sortables_init('tblSort');">
<form method="post" scope="request" name="form" action="/MrpBP.do?method=orderSel" >
<div>
	<div class="HeadingIcon">
		<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
	</div>
	<div class="HeadingTitle">$text.get("mrp.lb.mrpOrderChoose")<br></div>
		<ul class="HeadingButton">
			<li>
				<button type="button" onClick="location.href='/MrpBP.do'" class="b2">$text.get("mrp.lb.back")</button>
			</li>
		</ul>
	</div>
	<div id="listRange_id">
		<div class="listRange_1">
			<li><span>$text.get("mrp.lb.mrpFrom"):</span>
				<select name="mrpFrom" id="mrpFrom" onChange="changeMrpFrom(this.value)">
					<option value="1">$text.get("mrp.lb.produceOrder")</option>
					<option value="2" #if($!mrpFrom==2)selected #end>$text.get("mrp.lb.salesOrder")</option>
					<option value="0">$text.get("mrp.lb.mlProduce")</option>
				</select>
				<button type="button" onClick="simulate()">$text.get("mrp.lb.mlProduce")</button>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<select name="selCondition" onChange="changeSelCondition(this)">
					<option value="goods">$text.get("mrp.lb.selWithGoods")</option>
					<option value="date" #if($selCondition=="date") selected="selected" #end >$text.get("mrp.lb.selWithDate")</option>
				</select>
						
						
						<input id="selTxt"  name="$selCondition" onKeyPress="if(this.name=='date') return false;" value="$!selConditionStr" onClick="if(this.name=='date') showDate(this);">
						<label id="lab2" #if($selCondition=="goods") style="display:none;" #end >$text.get("mrp.lb.dateScope")</label>
						<input id="selTxt2" onClick="showDate(this)" onKeyPress="return false" value="$!date2" onBlur="$('date2').value=this.value" #if($selCondition=="goods") style="display:none;" #end>
						<input id="date2" name="date2" type="hidden">
						
					
						&nbsp;&nbsp;&nbsp;&nbsp;
						<button type="button" onClick="selectOrder()" class="b2">$text.get("common.lb.query")</button>
						
					</div>
					<div class="scroll_function_small_a" id="conter">
					<table border="0" cellpadding="0" cellspacing="0"
						class="listRange_list_function" name="table" id="tblSort" width="100%">
						<thead>
							<tr style="position:relative;top:expression(this.offsetParent.scrollTop);">
								<td width="40">
									<input type="checkbox" name="cb"  onclick="checkThisCks(this)">
								</td>
								<td width="120">
									$text.get("mrp.lb.mrpFrom")
								</td>
								<td width="200">
									$text.get("mrp.lb.relationId")
								</td>
								<td width="100"> 
									$text.get("mrp.lb.goodsNumber")
								</td>
								<td width="80">
									$text.get("mrp.lb.goodsFullName")
								</td>
								#foreach($prop in $props)
								<td width="80">$prop</td>
								#end
								<td width="80"> 
									$text.get("mrp.lb.produceQty")
								</td>
								<td width="100">
									$text.get("mrp.lb.needDate")
								</td>
								<td width="100">
									$text.get("mrp.lb.employee")
								</td>
								<td width="100">
									$text.get("mrp.lb.department")
								</td>
								<!-- 
								<td width="100">
									$text.get("mrp.lb.scompany")
								</td>
								 -->
							</tr>
						</thead>
						<tbody>
							#foreach($row in $list)
							<tr>
								<td width="30">
									<input type="checkbox" name="cb" value="$!globals.get($row,9)">
								</td>
								<td>
								<input name="mrpFroms" value="#if($!globals.get($row,8)=="1")$text.get('mrp.lb.produceOrder')#else$text.get('moduleFlow.lb.SalesOrder')#end" size="60" readonly="readonly">									
								</td>
								<td>
									<input name="orderIds" value="$!globals.get($row,0)" readonly="readonly"> 
								</td>
								<td>
									<input name="goodsCodes" value="$!globals.get($row,1)" type="hidden"> 
									<input  value="$!globals.get($row,10)" readonly="readonly" style="width:60px;"> 
								</td>
								<td>
									$!globals.get($row,2) &nbsp;
								</td>
								#foreach($l in $index)
								<td>
									<input value="$!globals.get($row,$l)" readonly="readonly">
								</td>
								#end
								<td>
								<input name="count" value=$!globals.get($row,3)>
								</td>
								<td>
									 $!globals.get($row,4) &nbsp;
								</td>
								<td>
									$!globals.get($row,5) &nbsp;
								</td>
								<td>
									$!globals.get($row,6) &nbsp;
								</td>
								<!-- 
								<td>
									 $!globals.get($row,7) &nbsp;
								</td>
								 -->
							</tr>
							#end
							
						</tbody>
					</table>
					</div>
<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-92;
oDiv.style.height=sHeight+"px";
//]]>
</script>
<div style="float:left; height:22px; line-height:22px;"><button type="button" onclick ="onsub()">$text.get("mrp.lb.showBom")</button></div>
					<div class="listRange_pagebar"> $!pageBar </div>
			</div>
		</form>
	</body>
</html>
