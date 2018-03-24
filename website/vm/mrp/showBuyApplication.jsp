<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$reportName</title>
<link type="text/css" href="/$globals.getStylePath()/css/classFunction.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/CRselectBoxList.css" />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/listgrid.css" />
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css" />
<link type="text/css" rel="stylesheet" href="/style/css/popOperation.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" /> 
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<style type="text/css">
.HeadingTitle{white-space:nowrap;} 
.scroll_function_small_a>div{background:#fff;}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.divbox.js"></script>
<script type="text/javascript" src="/js/menu/jquery.bgiframe.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>

<script type="text/javascript" src="$globals.js("/js/editPage.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/listGrid.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/function.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/k_listgrid.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/popOperation.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/dropdownselect.js","",$text)"></script>

<script language="javascript">

function cl(inp)
{
	setTimeout(function(){if(inp.dropdown != undefined)inp.dropdown.close();},200);
}
function toBuyApplication(){
	if($("input[name=keyId]:checked").size()==0){
		alert("请选择至少一个物料");
		return;
	}
	if(!confirm("确定下达采购订单吗?")){
		return;
	}
	
	if(typeof(top.jblockUI)!="undefined" ){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	jQuery.post("/Mrp.do?type=hasBuy",{},function(json){ 
		if(json.count > 0){
			if(typeof(top.jblockUI)!="undefined" ){
				top.junblockUI()
			}
			if(confirm("系统已有一个还在审核状态的请购单，请确认是否重复下单，继续请购请点击确认！")){
				toBuyApplicationConfirm();
			}
		}else{
			toBuyApplicationConfirm();
		}
	},"json" ); 
	
}
function toBuyApplicationConfirm(){
	if(typeof(top.jblockUI)!="undefined" ){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	
	var error = "";
	var saveValues=new Array();
	$("input[name=keyId]:checked").each(function(){
		var pos = $("input[name=keyId]").index(this);
		var buyQty = $("input[name=buyQty]:eq("+pos+")").val();
		if(buyQty<0){
			error= $(this).attr("GoodsNumber") + "不能小于0";
		}
		if(buyQty>0){
		saveValues[saveValues.length] = {GoodsCode:$(this).val(),
				lackQty:$(this).attr("lackQty"),
				totalLoss:$(this).attr("totalLoss"),
				hasQty:$(this).attr("hasQty"),
				Qty:buyQty};
		}
	});
	if(error != ""){
		alert(error);
		return ;
	}
	var postdata = JSON.stringify(saveValues); //生成所有需提交数据的json字符串
	if(typeof(top.jblockUI)!="undefined" ){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	jQuery.post("/Mrp.do?type=toBuyApplication",{mrpId:'$!mrpId',data:postdata},function(json){ 
		if(json.code == "ERROR"){
			alert("请购失败");
		}else{
			alert("请购成功，别忘了送审");
			changeConfirm();
			//window.location.href=window.location.href;
			
			/*
			turl="/UserFunctionAction.do?tableName=tblBuyApplication&keyId="+json.keyId+"&operation=5";
			width=document.documentElement.clientWidth;
			height=document.documentElement.clientHeight;
			openPop('PopMainOpdiv','',turl,width,height,false,true);   */
			
			$("#conter div").hide();
			$("#conter").append('<div style="margin: 50px 0px 0px 200px;font-size: 20px;">请先审核请购单 ， <a href="/Mrp.do?type=showBuyAppication&type=$!from&mrpId=$!mrpId">刷新</a></div>');
			
		}
		
		if(typeof(top.jblockUI)!="undefined" ){
			top.junblockUI()
		} 
	},"json" ); 
}

function showLack(GoodsCode){
	turl="/ReportDataAction.do?reportNumber=PDReportForGoodsDet&detTable_list=PDProduceRequire&GoodsCode="+GoodsCode;
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	openPop('PopMainOpdiv','',turl,width,height,false,true);  
}
</script>
</head>
<body style="overflow:hidden" onload="changeConfirm();">

<div id="Larger_content">
<iframe   name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" id="form"  name="form" action="/Mrp.do?type=showSalesOrder"  target="">


<div class="Heading">
	<div class="HeadingTitle">
		<b class="icons b-list"></b>
		缺料汇总表
	</div>
	<ul class="HeadingButton f-head-u">		
		<li><span class="btn btn-small" id="toGoods" name="toGoods" onClick="toBuyApplication();" >采购申请</span></li>
		<li><span class="btn btn-small" id="toGoods" name="toGoods" onClick="window.location.href='/Mrp.do?type=$!from'" >返回</span></li>
	</ul>
</div>
<div class="listRange_frame" id="listRange_frame">


<div class="scroll_function_small_a" id="conter" style="overflow:hidden" >
#if("$!ErrorMsg" !='') <div style="margin: 50px 0px 0px 200px;font-size: 20px;">$!ErrorMsg  <a href='/Mrp.do?type=showBuyAppication&mrpId=$!mrpId'>刷新</a></div> #end
<div style="width:100%;height:100%;overflow-x:hidden;overflow:hidden;position:relative;#if("$!ErrorMsg" !='') display:none; #end ">
	<div id="k_head" style="z-index: 50; overflow: hidden; width: 1000px; position: absolute; top: 0px; left: 0px;">
	<table id="k_listgrid" style="table-layout:fixed" border="0" cellspacing="0" width="900">
		<thead>
			<tr height="24">
				<td width="35">No.</td>
				<td width="24"><input type="checkbox" onclick="jQuery('input[name=\'keyId\']').attr('checked',this.checked);" disableautocomplete="true" autocomplete="off"></td>
				#foreach($rf in $GoodsFList)
					<td width="$globals.get($rf,2)">$globals.get($rf,1)</td>
				#end
				<!--<td width="80">需求总数</td>
				<td width="80">备品总数</td>
				<td width="80">已占用总数</td>
				<td width="80">已请购总数</td>-->
				<td width="80">缺料总数</td>
				<td width="80">备品总数</td>
				<td width="80">最低订货量</td>
				<td width="80">本次请购数</td>
			</tr>
		</thead>
	</table>
	</div>
	<div id="k_data" style="z-index: 40; width: 100%; overflow: auto; position: absolute; top: 0px; left: 0px; height: 354px;">
	<table id="k_listgrid" style="table-layout:fixed" border="0" cellspacing="0" width="900">
		<thead>
			<tr height="24">
				<td width="35">No.</td>
				<td width="24"><input type="checkbox" onclick="jQuery('input[name=\'keyId\']').attr('checked',this.checked);" disableautocomplete="true" autocomplete="off"></td>
				#foreach($rf in $GoodsFList)
					<td width="$globals.get($rf,2)">$globals.get($rf,1)</td>
				#end
				<!--<td width="80">需求总数</td>
				<td width="80">备品总数</td>
				<td width="80">已占用总数</td>
				<td width="80">已请购总数</td>-->
				<td width="80">缺料总数</td>
				<td width="80">备品总数</td>
				<td width="80">最低订货量</td>
				<td width="80">本次请购数</td>
			</tr>
		</thead>
		<tbody>
			#foreach($row in $buyList)
			<tr height="28px" onclick="gselectKeyId(this)" class="spaceRow">
				<td>$velocityCount</td>
				<td align="center"><input type="checkbox" value="$!row.get("GoodsCode")" 
				  lackQty="$globals.formatNumberS($!row.get("lackQty"),false,false,'','tblSalesOrderDet.Qty')"
				  totalLoss="$globals.formatNumberS($!row.get("LossQty"),false,false,'','tblSalesOrderDet.Qty')"
				  hasQty="$globals.formatNumberS($!row.get("BuyAppQty"),false,false,'','tblSalesOrderDet.Qty')"
				  GoodsNumber="$!row.get("GoodsNumber")"
				  checked name="keyId"  disableautocomplete="true" autocomplete="off"></td>
				#foreach($rf in $GoodsFList)
					<td align="left">$!row.get($globals.get($rf,0))</td>
				#end
				<!--  <td align="right">$globals.formatNumberS($!row.get("Qty"),false,true,'','tblSalesOrderDet.Qty')</td>
				<td align="right">$globals.formatNumberS($!row.get("LossQty"),false,true,'','tblSalesOrderDet.Qty')</td>
				<td align="right">$globals.formatNumberS($!row.get("PossesQty"),false,true,'','tblSalesOrderDet.Qty')</td>
				<td align="right">$globals.formatNumberS($!row.get("BuyAppQty"),false,true,'','tblSalesOrderDet.Qty')</td>-->
				<td align="right"><a href="javascript:showLack('$!row.get("GoodsCode")')" > $globals.formatNumberS($!row.get("lackQty"),false,true,'','tblSalesOrderDet.Qty')</a></td>
				<td align="right">$globals.formatNumberS($!row.get("LossQty"),false,true,'','tblSalesOrderDet.Qty')</td>
				<td align="right">$globals.formatNumberS($!row.get("reTimes"),false,true,'','tblSalesOrderDet.Qty')</td>
				<td align="right"><input class="ls3_in" style="width:80px;" name="buyQty" value="$globals.formatNumberS($!row.get("lackQty"),false,false,'','tblSalesOrderDet.Qty')" disableautocomplete="true" autocomplete="off" > </td>
			</tr>
			#end
		</tbody>
	</table>
	</div>
</div>
</div>
<script language="javascript">
	
var condHeight= 80;

var oDiv=document.getElementById("conter");
var dHeight=document.documentElement.clientHeight;
oDiv.style.height=dHeight-55-condHeight +"px";

$("#k_data").height($("#conter").height()-10);
$("#k_head").width($("#k_data")[0].clientWidth);
jQuery("#kt_head").width(jQuery("#k_column>table").outerWidth(true));
jQuery("#k_data tbody tr").click(function(){jQuery(this).toggleClass("highlightrow");});
$("#k_data").scroll(function () { 
	$("#k_head").scrollLeft($("#k_data").scrollLeft()); 
}); 

</script>
		
</div>	

</form>
</div>
</body>
</html>
