<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />

<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="$globals.js("/js/mrp.vjs","",$text)"></script>
<script type="text/javascript" src="/js/dragcols.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>

<title>$text.get("mrp.lb.mrpCount")</title>
<script type="text/javascript">
function suopin(){
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
}
function jiesuo(){
	if(typeof(top.junblockUI)!="undefined"){
		top.junblockUI();
	}
}
//下达采购订单
function doOrder(){
	var flag=false;
	var isSelect = false;
	var choose = document.getElementsByName("choose");
	for(i=0; i<choose.length; i++){
		if(choose[i].value=="true"){
			isSelect = true;
			break;
		}
	}
	if(!isSelect){
		alert("$text.get('mrp.lb.noChooseGoods')");
		return;
	}
	var needProducts = document.getElementsByName("needProduct");
	for(i=0; i<choose.length; i++){
		if(choose[i].value=="true"&&Number(needProducts[i].value)>0){
			flag = true;
			break;
		}			
	}	
	if(!flag){
		alert("$text.get('mrp.msg.AllGoodsNeededQtyBuy')");
		return;
	}
	if(!confirm('$text.get("mrp.msg.doOrderPre")')){
		return false;
	}
	var url = "/MrpBP.do?method=doOrder";
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	form.action=url;
	form.submit();		
}
		
//下达请购单


function doQuoteBuy(){
	var flag=false;
	var isSelect = false;
	var choose = document.getElementsByName("choose");
	for(i=0; i<choose.length; i++){
		if(choose[i].value=="true"){
			isSelect = true;
			break;
		}
	}
	if(!isSelect){
		alert("$text.get('mrp.lb.noChooseGoods')");
		return;
	}
	var needProducts = document.getElementsByName("needProduct");
	for(i=0; i<choose.length; i++){
		if(choose[i].value=="true"&&Number(needProducts[i].value)>0){
			flag = true;
			break;
		}			
	}
	var zeroApply = document.getElementsByName("zeroApply")[0]
	if(!flag&&zeroApply.checked==false){
		alert("$text.get('mrp.msg.AllGoodsNeededQtyQuoteBuy')");
		return;
	}
	if(!confirm('$text.get("mrp.msg.doQuoteBuyPre")')){
		return false;
	}
	var url = "/MrpBP.do?method=doQuoteBuy";
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	form.action=url;
	form.submit();		
}

//下达生产任务单


function doProduceOrder(){
	var flag=false;
	var isSelect = false;
	var chks = document.getElementsByName("choose");
	var BOMId=document.getElementsByName("BOMId");
	for(i=0; i<chks.length; i++){
		if(BOMId[i].value.length==0)continue;
			if(chks[i].value=="true"){
				isSelect = true;
				break;
			}
	}
	if(!isSelect){
		alert("$text.get('mrp.lb.noChooseGoods')");
		return;
	}
	var needProducts = document.getElementsByName("needProduct");
	for(i=0; i<chks.length; i++){
		if(BOMId[i].value.length==0)continue;
		if(chks[i].value=="true"&&Number(needProducts[i].value)>0){
			flag = true;
			break;
		}			
	}
	if(!flag){
		alert("$text.get('mrp.msg.AllGoodsNeededQtyProduce')");
		return;
	}
	if(!confirm('$text.get("mrp.msg.doProducePre")')){
		return false;
	}
	
	var url = "/MrpBP.do?method=doProduceOrder";
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	form.action=url;
	form.submit();
	return;	
}
		
function selectLow(chk){
	var billNos = document.getElementsByName("billNo");
	var chooses = document.getElementsByName("choose");
	var i = parseInt(chk.value);
	var k=i-1;
	chooses[k].value=chk.checked;
	for(i;i<billNos.length;i++){
		if(billNos[i].value==""){
			chooses[i].value=chk.checked;
		}else{
			break;
		}
	}
}

function checkAllSub(chkCap){
	var cbs = document.getElementsByName("cb");
	for(i=1;i<cbs.length;i++){
		if(!cbs[i].disabled){
			cbs[i].checked=chkCap.checked;
			selectLow(cbs[i]);
		}
	}
}



		var storeCheck=false;
		var usedCheck=false;
		var storeingCheck=false;
		var productingCheck=false;
		function checkMrpSets(flag){
			demand(flag,"store");
			demand(flag,"used");
			demand(flag,"storeing");
		}
//保存
function save(){
	try{
		var isSelect = false;
		var chks = document.getElementsByName("cb");
		for(i=1; i<chks.length; i++){
			if(chks[i].checked){
				isSelect = true;
				break;
			}
		}
		if(!isSelect){
			alert("$text.get('mrp.matereiDemandJs.info1')");
			return;
		}
			
		var form = document.getElementsByName("form")[0];
		form.action="/MrpBP.do?method=saveMrp";
		form.submit();
	}catch(e)	{alert(e.description);}
}		
function goBack(){
	location.href="/MrpBP.do?mrpFrom=$!mrpFrom";
}		

function checkLastBom(){
	//获得表"tblSort"的第"1"行开始的第"2"列的"文本内容"'
	var boms = getCellInfos('tblSort',1,3,true);
	getCellInfos('tblSort',1,6,true);
	var bom ;
	var cbs = n("cb");//获得复选框
	var len = boms.length;
	for(i=0;i<len;i++){
		bom = boms[i].trim();
		if(bom.indexOf("~")!=-1){
			if(cbs[i+1].isPlace=="true"){//检查如果当前主料没有替换料，默认第一个被选中
				if(cbs[i+1].path==cbs[i+1].mainPath){
					var isselect=false;
					for(j=0;j<len;j++){
						if(cbs[j+1].mainPath==cbs[i+1].mainPath&&cbs[j+1].checked){
							isselect=true;
						}
					}
					if(!isselect){
						cbs[i+1].checked=true;
					}
				}
			}else{
				cbs[i+1].disabled="disabled";
				cbs[i+1].checked=false;
			}
		}else{
			cbs[i+1].checked=true;
		}
	}
}

function printData(){
	form.target="formFrame";
	form.action="/MrpBP.do?method=savePrintData";
	form.submit();
	var urls = "/ReportDataMrpAction.do?operation=printActiveX&reportNumber=mrpDemand2Print";
	asyncbox.open({id:'print',title:'打印单据',url:urls,width:300,height:280});
	form.target="";		
}

var obj ;
var field ;
function openSelect1(urlstr,displayName,obj,field){
	window.obj = obj;
	window.field = field;
	var urlstr = urlstr+"&popupWin=DeptPopup&MOID=$MOID&MOOP=query&LinkType=@URL:&displayName="+encodeURI(displayName); 
	asyncbox.open({id:'DeptPopup',title:displayName,url:urlstr,width:800,height:470}); 
}

function exeDeptPopup(str){
	if(typeof(str)!="undefined"){
		fs=str.split("|"); 
				
		document.getElementById("StockCode1").value="";
		document.getElementById("Stock").value="";
		if(fs.length==1){
			var kv=fs[0].split(";");
			if(kv[0].length>0){
				document.getElementById("StockCode1").value+=kv[0]+";";
				document.getElementById("Stock").value+=kv[1]+";";
			}
		}
		for(var i=0;i<fs.length-1;i++){
			var kv=fs[i].split(";");
					
			document.getElementById("StockCode1").value+=kv[0]+";";
			document.getElementById("Stock").value+=kv[1]+";";
		}
	}
	if(obj=="Stock"){
		var stocks = document.getElementsByName("stockCode");
		var stockNames = document.getElementsByName("stockName");
		for(i=0;i<stocks.length;i++){
			stocks[i].value=document.getElementById("StockCode1").value;
			stockNames[i].value=document.getElementById("Stock").value;
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
<iframe name="formFrame" style="display:none"></iframe>
<body onLoad="showtable('tblSort');showStatus();checkLastBom(); checkAll('cb',true);sortables_init('tblSort');jiesuo();">
<form method="post" scope="request" name="form" action="/MrpBP.do?method=bomDemand&opeType=BackMrp">	
<input type="hidden" name="neededAll" value="">
<input type="hidden" name="storeingAll" value="">
<input type="hidden" name="productingAll" value="">
<input type="hidden" name="usedAll" value="">
<input type="hidden" name="method" value="">
<input type="hidden" name="mrpId" value="$!mrpId">
<input type="hidden" name="mrpFrom" value="$!mrpFrom">
<div class="Heading">
	<div class="HeadingIcon">
		<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
	</div>
	<div class="HeadingTitle">$text.get("mrp.lb.mrpCount")</div>
	<ul class="HeadingButton">
		<li>
			
		<input type="checkbox" class="cbox" id="mrpSetId7" name="zeroApply"  value="true" onClick="" checked >
		<label for="mrpSetId7">$text.get("mrp.lb.zeroApply")</label>
		
		
		<label for="mrpSetId7">$text.get("mrp.lb.defaultStock"):</label>
		<input id="StockCode1" name="StockCode1" type="hidden" value="$!StockCode1" /><input type="input" id="Stock" name="Stock" value="$!Stock" onDblClick="openSelect1('/UserFunctionAction.do?operation=$globals.getOP("OP_POPUP_SELECT")&selectName=SelectStocksCheckMRP','$text.get('mrp.lb.defaultStock')','Stock','StockCode1');" /><img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="openSelect1('/UserFunctionAction.do?operation=$globals.getOP("OP_POPUP_SELECT")&selectName=SelectStocksCheckMRP','$text.get('mrp.lb.defaultStock')','Stock','StockCode1');" >
		
		<button type="button" name="saveList" onClick="suopin();form.submit();" class="b4">$text.get("mrp.lb.showBom")</button>
		#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=tblBuyApplication").add())
		<button type="button" onClick="doQuoteBuy()">$text.get("mrp.lb.addQuoteBuy")</button>
		#end
		#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=tblBuyOrder").add())
		<button  type="button"  onClick="doOrder()">$text.get("mrp.lb.doBuyOrder")</button>
		#end
		#if($globals.getVersion()==8)
		#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=tblProduce").add())
		<button type="button"  onClick="doProduceOrder()">$text.get("mrp.lb.doProduceOrder")</button>
		#end
		#end
		
		
		#if($LoginBean.operationMap.get("/MrpBP.do").print())		
		<button type="button" onClick='printData();' class="b2">$text.get("common.lb.print")</button>
		#end
		<button type="button" onClick="goBack()" class="b2">$text.get("common.lb.back")</button>
		</li>

		</ul>
</div>
<div id="listRange_id">
<div class="scroll_function_small_a" id="conter">
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" id="tblSort" name="table">
						<thead>
							<tr style="position:relative;top:expression(this.offsetParent.scrollTop);">
								<td width="40">No.</td>
								<td width="30"><input type="checkbox" name="cb" onClick="checkAllSub(this)">
								</td>
								<td width="100"> 
									$text.get("mrp.lb.bomNumber")
								</td>
								<td width="250">
									$text.get("mrp.lb.bomName")
								</td>
								<td width="100">
									$text.get("call.lb.spec")
								</td>
								<td width="50">
									$text.get("iniGoods.lb.Unit")
								</td>
								#if($globals.getVersion()=="8")
								<td width="100">$text.get("mrp.lb.MaterielAttribute")</td>
								#end
								
								#foreach($prop in $propList)
									#if($prop.isUsed==1&&!$!prop.propName.equals("Seq")&&$globals.getFieldBean("tblBOM",$!prop.propName))
										<td width="80">$prop.display.get($!locale)</td>
									#end
								#end
								<td width="60">$text.get("mrp.lb.numUnit")</td>
								<td width="100">本单毛需求</td>
								<td width="100">$text.get("mrp.lb.productQty")</td>
								<td width="100">实际库存</td>
								<td width="60">采购申请</td>
								<td width="60">$text.get("mrp.lb.storingQty")</td>
								<td width="60">$text.get("mrp.lb.productingQty")</td>
								<td width="100">$text.get("mrp.lb.neededQty")</td>
							</tr>
						</thead>
						<tbody>
						#set($trackNo="")
						#foreach($!ha in $!orderList)
						#set($row=$!pathMaps.get($!ha))
						<tr>
							#set($good=$!goodsMap.get($!row.get("goodsCode")))
							<td width="40">$velocityCount</td>
							<td width="30">						
								<input type="checkbox" name="cb" value="$velocityCount" isPlace="$!row.get("isPlace")" #if($!row.get("cb")=="true") checked="$!row.get("cb")" #end  path="$!row.get("path")" mainPath="$!row.get("mainPath")" onclick="selectLow(this);">
								<input type="hidden" name="choose" value="true">
								<input type="hidden" name="goodPropHash" value="$!ha">
								<input type="hidden" name="goodsNumber" value="$!row.get("ch")">
								<input type="hidden" name="goodsCode" value="$!row.get("goodsCode")">
								<input type="hidden" name="needProduct" value="$!row.get("needProduct")">
								<input type="hidden" name="store" value="$!row.get("store")">
								<input type="hidden" name="trackNo" value="$!row.get("trackNo")">
								<input type="hidden" name="startDate" value="$!row.get("startDate")">
								<input type="hidden" name="submitDate" value="$!row.get("submitDate")">
								<input type="hidden" name="path" value="$!row.get("path")">
								<input type="hidden" name="bomdetId" value="$!row.get("bomdetId")">
								<input type="hidden" name="BOMId" value="$!row.get("BOMId")">
								<input type="hidden" name="MaterielAttribute" value="$!good.get("MaterielAttribute")">
								<input type="hidden" name="qty" value="$!row.get("qty")">
							</td>
							
							<td title="$!row.get("goodsNumber")"><input type="text" name="goodsNumberz" value="$good.get("goodsNumber")"></td>
							<td title="$!row.get("goodsFullName")"><input type="text" name="goodsFullName" value="$good.get("goodsFullName")"></td>
							<td title="$!row.get("goodsSpec")"><input type="text" name="goodsSpec" value="$good.get("goodsSpec")"></td>
							<td title="$!row.get("unitName")"><input type="text" name="unitName" value="$good.get("unitName")"></td>
							#set($count=0)
							#foreach($erow in $globals.getEnumerationItems("MaterielAttribute"))
								#if($erow.value==$!good.get("MaterielAttribute"))
									<td title="$erow.name"><input type="text" name="materName" value="$erow.name"></td>
									#set($count=1)
								#end
							#end

							#foreach($prop in $propList)
							#if($prop.isUsed==1&&!$!prop.propName.equals("Seq")&&$globals.getFieldBean("tblBOM",$!prop.propName))
							<td><input type="text" name="$!prop.propName" value="$!row.get($prop.propName)"></td>
							#end
							#end	
							<td><input type="text" name="unitNum" value="$!row.get("unitNum")"></td>
							<td><input type="text" name="BCount" value="$!row.get("BCount")"></td>
							<td><input type="text" name="count" value="$!row.get("count")"></td>
							<td>$!row.get("store")&nbsp;</td>
							<td><input type="text" name="apping" value="$!row.get("apping")"></td>
							<td><input type="text" name="storeing" value="$!row.get("storeing")"></td>
							<td><input type="text" name="producting" value="$!row.get("producting")"></td>
							<td><input type="text" name="needed" value="$!row.get("needProduct")"></td>
						</tr>
						#end						
						</tbody>
					</table>
					</div>
					
				</div>			
							
		</form>
<script type="text/javascript">
$("#conter").height($(window).height()-50);
</script>
	</body>
</html>
