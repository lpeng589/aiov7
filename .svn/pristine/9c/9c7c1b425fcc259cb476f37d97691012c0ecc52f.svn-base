<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("mrp.lb.mrpCount")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />

<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/listGrid.js"></script>
<script type="text/javascript" src="$globals.js("/js/mrp.vjs","",$text)"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/dragcols.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript">
var style="$globals.getStylePath()";
function changeMrpFrom(mrpFrom){
	if(mrpFrom==0){
		simulate();
		return;
	}
	if(mrpFrom==1){
		//alert("生产计划订单不存在！");
		n("mrpFrom")[0].selectedIndex=1;
		//return;
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
		sortables_init('tblSort');
	}
	isML=true;
	var url ;//= "/vm/mrp/simulateOrder.html";
	url = "/MrpBP.do?method=simulateOrder&mnId="+mnId;
	var var1 = "";  
	dialogSettings = "Center:yes;Resizable:no;DialogHeight:500px;DialogWidth:800px;Status:no";
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
	addNewTd(tr,"$text.get('mrp.lb.mrpFrom')",false);//加上表头
	addNewTd(tr,"$text.get('mrp.lb.maOrderNumber')",false);//加上表头
	addNewTd(tr,"$text.get('mrp.lb.goodsNumber')",false);//加上表头
	addNewTd(tr,"$text.get('mrp.lb.goodsFullName')",false);//加上表头
	addNewTd(tr,"$text.get('mrp.lb.produceQty')",false);//加上表头
	addNewTd(tr,"$text.get('mrp.lb.needDate')",false);//加上表头
	addNewTd(tr,"$text.get('mrp.lb.employee')",false);//加上表头
	addNewTd(tr,"$text.get('mrp.lb.department')",false);//加上表头
	addNewTd(tr,"$text.get('muduleFlow.lb.produceDetailList')",false);//加上表头
	//addNewTd(tr,"分支机构",false);//加上表头
	return tr; 
}
function addRowInfo(infos){
	//alert(infos);
	var tr = document.createElement("tr");
	str='<input type="checkbox" class="cbox" name="cb" value="" checked="checked">&nbsp;';
	addNewTd(tr,str,true);//加上复选框
	str = "$text.get('mrp.lb.mlProduce')";
	addNewTd(tr,str,false);//加上订单来源
	str ='<input name="orderId" value="'+infos[1]+'" readonly="readonly">';
	addNewTd(tr,str,true);//加上关联ID
	str = '<input name="keyId" value="'+infos[2]+'" type="hidden">';
	str +='<input type="hidden" name="choose" value="true">';
	str +='<input type="hidden" name="trackNo" value="'+infos[12]+'">';
	str +='<input name="goodsNumber" value="'+infos[3]+'" readonly="readonly">';
	addNewTd(tr,str,true);//加上商品编码
	str ='<input name="goodsName" value="'+infos[4]+'" readonly="readonly">';
	addNewTd(tr,str,true);//加上商品名

	str = '<input name="count" value="'+infos[5]+'">';
	addNewTd(tr,str,true);//加上投产数量
	str ='<input name="billDate" value="'+infos[6]+'" readonly="readonly">';
	addNewTd(tr,str,true);//加上需求日期

	str ='<input type="hidden" name="EmployeeID" value="'+infos[7]+'">';
	str +='<input name="employee" value="'+infos[8]+'" readonly="readonly">';
	addNewTd(tr,str,true);//经手人

	str ='<input type="hidden" name="DepartmentCode" value="'+infos[9]+'">';
	str += '<input name="department" value="'+infos[10]+'" readonly="readonly">' ;
	addNewTd(tr,str,true);//部门
	str ='<input name="scompany" value="'+infos[11]+'" readonly="readonly">' ;
	str ='<input name="bomNo" value="'+infos[12]+'" readonly="readonly">';
	addNewTd(tr,str,true);//加上商品名

	//addNewTd(tr,str,true);//分支机构
	//---------动态属性

	var prop;
	if(infos[12]!=null&&infos[12]!="undefined"){
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
	if(!indexs.length){alert("$text.get('oa.common.alertDelete')");}
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
			alert("$text.get('mrp.help.pleaseSimulateOrder')");
			return;
		}
		form.action="/MrpBP.do?method=bomSelPro&mrpFrom="+mrpFrom+"&cb="+cb+"&winCurIndex=$winCurIndex";
		form.submit();
		return;
	}
	
	var keyIds = document.getElementsByName("cb");//获得所有复选框
	var orderIds="";
	for(var i=0;i<keyIds.length;i++){
		if(keyIds[i].checked){
			orderIds+=keyIds[i].value+",";
		}
	}	
	var mrpFrom = document.getElementById("mrpFrom");
	if(orderIds.length==0){
		if(mrpFrom.value=="2")
			alert("$text.get('mrp.help.pleaseChooseOrder')");
		else if(mrpFrom.value=="1")
			alert("$text.get('mrp.help.pleasePlanOrder')");
		return;
	}else{
		for(var i=0;i<keyIds.length;i++){
			if(keyIds[i].checked){
				var bomNo=document.getElementsByName("bomNo")[i];
				if(bomNo.options[bomNo.selectedIndex].value.length==0&&"$globals.getSysSetting("MRPNoMaterOper")"=="false"){
					alert("$text.get("mrp.msg.noBom")");
					return false;
				}		
			}
		}
	}
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	form.action="/MrpBP.do?method=bomSelPro";
	form.submit();
}
	
function openGoodSelect(){
	displayName= "$text.get('mrp.lb.goodsFullName')" ;
	var urlstr = '/UserFunctionAction.do?operation=$globals.getOP("OP_POPUP_SELECT")&selectName=MrpSelectGoods&popupWin=GoodSelect&MOID=$MOID&MOOP=query&LinkType=@URL:&displayName='+encodeURI(displayName); 
	asyncbox.open({id:'GoodSelect',title:'选择成品',url:urlstr,width:800,height:470}); 
}

function exeGoodSelect(str){
	if(typeof(str)!="undefined"){
		var strs=str.split(";");

		document.getElementsByName("goodsNumber")[0].value=strs[0];
		document.getElementsByName("goodsFullName")[0].value=strs[1];
		document.getElementsByName("goodsSpec")[0].value=strs[2];
	}
}
	
function showDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}

function selectOrder(){
	var form = document.getElementsByName("form")[0];
	form.action = "/MrpBP.do?method=orderSel";
	form.submit();
}

function selectLow(chk){
	var chooses = document.getElementsByName("choose");
	var i = parseInt(chk.value);
	var k=i-1;
	chooses[k].value=chk.checked;
}

function checkAllSub(vars){
	var cbs = document.getElementsByName("cb");
	var bomNos= document.getElementsByName("bomNo");

	for(i=0;i<cbs.length;i++){	
		if(bomNos[i].options.item(0).value.length>0){
			cbs[i].checked=vars.checked;
			selectLow(cbs[i]);
		}
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
<body onLoad="showStatus();">
<form method="post" scope="request" name="form" action="/MrpBP.do?method=orderSel" >
<input type="hidden" name="operation" value="4" >
<div class="Heading">
	<div class="HeadingIcon">
		<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
	</div>
	<div class="HeadingTitle">$text.get("mrp.lb.mrpOrderChoose")</div>
		<ul class="HeadingButton">
			<li><button type="button" onclick ="onsub()" class="b4">$text.get("mrp.lb.showBom")</button></li>	
			<li><button type="button"  onClick="selectOrder()" class="b2">$text.get("common.lb.query")</button></li>
			<li><button type="button" onClick="location.href='/MrpBP.do'" class="b2">$text.get("mrp.lb.back")</button></li>
		</ul>
	</div>
	<div id="listRange_id">
		<div class="listRange_1">
			#if($globals.getSysSetting("showMrpProc").equals("true"))
			<li><span>$text.get("mrp.lb.mrpFrom")：</span>
					<select name="mrpFrom" id="mrpFrom" onChange="changeMrpFrom(this.value);selectOrder();">						
							<option value="2" #if($!mrpFrom==2)selected #end>$text.get("mrp.lb.salesOrder")</option>						
							<option value="1" #if($!mrpFrom==1)selected #end>$text.get("mrp.lb.produceOrder")</option>
							<option value="0" #if($!mrpFrom==0)selected #end>$text.get("mrp.lb.mlProduce")</option>						
					</select>
			</li>
			#end
			<li>
				<span>$text.get("mrp.lb.operstatus")：</span>
				<select name="operStatus" id="operStatus" onChange="selectOrder();">						
							<option value="0" #if($!operStatus==0)selected #end>$text.get("mrp.lb.notOper")</option>						
							<option value="1" #if($!operStatus==1)selected #end>$text.get("mrp.lb.hasOper")</option>
							<option value="2" #if($!operStatus==2)selected #end>$text.get("mrp.lb.allOper")</option>						
				
				</select>
			</li>
			
			<li>
				<span>$text.get("mrp.lb.relationId")：</span><input type="text" name="billNo" value="$!billNo">
			</li>	
			<li>
				<span>$text.get("mrp.lb.goodsNumber")：</span><input type="text" name="goodsNumber" value="$!goodsNumber" onDblClick="openGoodSelect();" /><img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="openGoodSelect();" >
			</li>
			<li>
				<span>$text.get("mrp.lb.goodsFullName")：</span><input type="text" name="goodsFullName" value="$!goodsFullName" onDblClick="openGoodSelect();" /><img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="openGoodSelect();" >
			</li>		
			<li>
				<span>$text.get("mrp.lb.goodsSpec")：</span><input type="text" name="goodsSpec" value="$!goodsSpec" onDblClick="openGoodSelect();" /><img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="openGoodSelect();" >
			</li>
			<li><span>$text.get("mrp.lb.needDate")≥：</span><input type="text" name="sDate" value="$!sDate" onClick="showDate(this)"></li>
			<li><span>$text.get("mrp.lb.needDate")≤：</span><input type="text" name="eDate" value="$!eDate" onClick="showDate(this)"></li>	
			<li><input type="hidden" name="opByBillType"  id="opByBillType" value="1" /></li>
		</div>
		<div class="scroll_function_small_a" id="conter">
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" id="tblSort">
				<thead>
					<tr style="position:relative;top:expression(this.offsetParent.scrollTop);">
						<td width="30">&nbsp;</td>
						<td width="30" ><input type="checkbox" name="cbs"  onclick="checkAllSub(this);" value=""></td>
						#if($globals.getSysSetting("showMrpProc").equals("true"))
						<td width="80">$text.get("mrp.lb.mrpFrom")</td>
						#end
						<td width="120">$text.get("mrp.lb.trackNo")</td>
						<td width="120">$text.get("mrp.lb.goodsNumber")</td>
						<td width="250">$text.get("mrp.lb.goodsFullName")</td>
						<td width="150">$text.get("mrp.lb.goodsSpec")</td>
						#foreach($prop in $propList)
							#if($prop.isUsed==1&&!$!prop.propName.equals("Seq")&&!$!prop.propName.equals("ProDate")&&!$!prop.propName.equals("Availably"))
						<td width="80">$prop.display.get($!locale)</td>
							#end
						#end
						<td width="80">$text.get("mrp.lb.produceQty")</td>
						<td width="80">$text.get("mrp.lb.needDate")</td>
						<td width="60">$text.get("mrp.lb.employee")</td>
						<td width="100">$text.get("mrp.lb.department")</td>
						#if($globals.getVersion()=="8")
						<td width="160">$text.get("muduleFlow.lb.produceDetailList")</td>
						#else
						<td width="160">$text.get("QuoteArts")</td>
						#end
						
					</tr>
				</thead>
				<tbody>
					#foreach($row in $list)
					<tr>
						<td>#set($count=$velocityCount)
							$count					
						</td>
						<td>
							<input type="checkbox" class="cbox" name="cb" onClick="selectLow(this);" value="$velocityCount">
							<input type="hidden" name="trackNo" value="$row.get("trackNo")" >
							<input type="hidden" name="choose" value="false">
							<input type="hidden" name="mrpFrom" value="$row.get("mrpFrom")">
						</td>
						#if($globals.getSysSetting("showMrpProc").equals("true"))
						<td>#if($!mrpFrom=="1")$text.get('mrp.lb.produceOrder')#else$text.get('moduleFlow.lb.SalesOrder')#end</td>
						#end
						<td title="$row.get("trackNo")">$row.get("trackNo")</td>
						<td title="$row.get("goodsnumber")">$row.get("goodsnumber")&nbsp;</td>
						<td title="$row.get("goodsFullName")">$row.get("goodsFullName")&nbsp;</td>
						<td title="$row.get("GoodsSpec")">$row.get("GoodsSpec")&nbsp;</td>
						#set($propStr="")
						#foreach($prop in $propList)
							#if($prop.isUsed==1&&!$!prop.propName.equals("Seq")&&!$!prop.propName.equals("ProDate")&&!$!prop.propName.equals("Availably"))
							<td>$!row.get($!prop.propName)&nbsp;#set($propStr=$propStr+":"+$!row.get($!prop.propName))</td>
							#end
						#end								
						<td><input type="text" name="count" value="$!globals.formatNumber($row.get("produceQty"),false,false,$!globals.getSysIntswitch(),"tblBOM","Qty",true)">&nbsp;</td>
						<td>$row.get("sendDate") &nbsp;</td>
						<td>$row.get("EmpFullName") &nbsp;</td>
						<td>$row.get("DeptFullName") &nbsp;</td>
						<td><select name="bomNo">
							#set($count=0)
							#set($propStr=$row.get("goodsCode"))
							#foreach($billNo in $!bomNoMap.get($propStr))
							#set($count=1)
							<option value="$!billNo">$!billNo</option>
							#end									
							#if($count==0)
							<option value=""></option>
							#end
							</select>
						</td>
					</tr>
				#end		
			</tbody>
		</table>
	</div>
<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.documentElement.clientHeight-108;
oDiv.style.height=sHeight+"px";
//]]>
</script>
		</form>
	</body>
</html>
