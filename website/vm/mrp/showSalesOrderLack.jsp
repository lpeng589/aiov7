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
function gselectKeyId(obj){
	ind = jQuery(obj).index();//取得行号
	trobj = $(obj,document);
	keyi = trobj.find("input[name=keyId]");
	if(keyi.size() > 0){
		if(keyi.attr("type")=="radio"){
			keyi[0].checked = true;
		}else{		
			keyi[0].checked = !keyi[0].checked;
		}
	}
}

var MOID="$LoginBean.getOperationMap().get('/UserFunctionQueryAction.do?tableName=PDMRP').getModuleId()";
function openInputDate(obj)
{
	WdatePicker({lang:'$globals.getLocale()'});
}


function beforeButtonQuery(){
	return true;
}
function submitQuery(){
	window.save=true;
	document.forms[0].submit();
}
function clearCondition(){
	$("#conditionDIV").find("input[type=text]").val("");
	$("#conditionDIV").find("input:not([type])").val("");
	$("#conditionDIV").find("select").val("");
	$("#conditionDIV").find("input[belongTableName]").val("");
	
}
function cl(inp)
{
	setTimeout(function(){if(inp.dropdown != undefined)inp.dropdown.close();},200);
}
function detail(tableName,keyId){
	turl="/UserFunctionAction.do?tableName="+tableName+"&keyId="+keyId+"&operation=$globals.getOP("OP_DETAIL")";
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	openPop('PopMainOpdiv','',turl,width,height,false,height==document.documentElement.clientHeight);  
	
}
function toProduceRequire(){
	window.save=true;
	document.form.action="/Mrp.do?type=toProduceRequire";
	document.form.submit();
}
</script>
</head>
<body style="overflow:hidden" onload="changeConfirm();">

<div id="Larger_content">
<iframe   name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" id="form"  name="form" action="/Mrp.do?type=selSalesOrder"  target="formFrame">
<input type="hidden" name="saleOrders" value="">

<div class="Heading">
	<div class="HeadingTitle">
		<b class="icons b-list"></b>
		当前所有订单缺货明细
	</div>
	<ul class="HeadingButton f-head-u">		
		<li><span class="btn btn-small" id="toGoods" name="toGoods" onClick="toProduceRequire();" >下达制需求单</span></li>
		<li><span class="btn btn-small" id="toGoods" name="toGoods" onClick="window.location.href='/Mrp.do?type=selSalesOrderForPD';" >返回</span></li>
	</ul>
</div>
<div class="listRange_frame" id="listRange_frame">


<div class="scroll_function_small_a" id="conter" style="overflow:hidden" >
<div style="width:100%;height:100%;overflow-x:hidden;overflow:hidden;position:relative">
	<div id="k_head" style="z-index: 50; overflow: hidden; width: 1000px; position: absolute; top: 0px; left: 0px;">
	<table id="k_listgrid" style="table-layout:fixed" border="0" cellspacing="0" width="900">
		<thead>
			<tr height="24">
				<td width="35">No.</td> 
				#foreach($row in $GoodsFList)
				<td width="$globals.get($row,2)">$globals.get($row,1)</td>
				#end
				<td width="80">订单待出库数量</td>
				<td width="80">当前库存数量</td>
				<td width="80">在制/在途</td>
				<td width="80">补货量</td>
			</tr>
		</thead>
	</table>
	</div>
	<div id="k_data" style="z-index: 40; width: 100%; overflow: auto; position: absolute; top: 0px; left: 0px; height: 354px;">
	<table id="k_listgrid" style="table-layout:fixed" border="0" cellspacing="0" width="900">
		<thead>
			<tr height="24">
				<td width="35">No.</td>
				#foreach($row in $GoodsFList)
				<td width="$globals.get($row,2)">$globals.get($row,1)</td>
				#end
				<td width="80">订单待出库数量</td>
				<td width="80">当前库存数量</td>
				<td width="80">在制/在途</td>
				<td width="80">补货量</td>
			</tr>
		</thead>
		<tbody>
			#foreach($srow in $salesList)
			<tr height="28px" onclick="gselectKeyId(this)" class="spaceRow">
				<td>$velocityCount</td>
				<input type=hidden name=SourceId value="$srow.get("GoodsCode"):$srow.get("attrType"):$srow.get("BatchNo"):$!srow.get("yearNO"):$srow.get("Hue"):$srow.get("Inch"):a"/>
				#foreach($row in $GoodsFList)
				<td width="$globals.get($row,2)">$!globals.encodeHTML($srow.get($globals.get($row,0)))</td>
				#end
				<td width="80">$globals.formatNumberS($srow.get("NotOutQty"),false,true,'','tblSalesOrderDet.Qty')</td>
				<td width="80">$globals.formatNumberS($srow.get("lastQty"),false,true,'','tblSalesOrderDet.Qty')</td>
				<td width="80">$globals.formatNumberS($srow.get("prodQty"),false,true,'','tblSalesOrderDet.Qty')</td>
				<td width="80"><input type="text" style="width:78px" name="lackQty" value="$globals.formatNumberS($srow.get("lackQty"),false,false,'','tblSalesOrderDet.Qty')" disableautocomplete="true" autocomplete="off"/></td>
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
