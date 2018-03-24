<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>物料需求计算</title>
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
	
}

var MOID="$LoginBean.getOperationMap().get('/Mrp.do').getModuleId()";
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
function openMRP(keyId){
	turl="/Mrp.do?type=showBom&keyId="+keyId;
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	openPop('PopMainOpdiv','',turl,width,height,false,height==document.documentElement.clientHeight);  
	
}
function toCompute(){
	if($("input[name=keyId]:checked").size()==0){
		alert("请选择至少一个物料");
		return;
	}
	var saveValues=new Array();
	$("input[name=keyId]:checked").each(function(){
		var pos = $("input[name=keyId]").index(this);
		saveValues[saveValues.length] = $(this).val();
	});
	var postdata = JSON.stringify(saveValues); //生成所有需提交数据的json字符串
	if(typeof(top.jblockUI)!="undefined" ){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	jQuery.post("/Mrp.do?type=computeMRP",{data:postdata},function(json){ 
		if(typeof(top.jblockUI)!="undefined" ){
			top.junblockUI()
		}
		if(json.code == "ERROR"){
			alert(json.msg);
		}else{
			//alert("计算成功,现在进入原材料请购");
			changeConfirm();
			window.location.href=window.location.href;
		} 
	},"json" ); 
}
function toBuy(){
	window.location.href="/Mrp.do?type=showBuyAppication";
}
function showApp(mrpId){
	window.location.href="/Mrp.do?type=showBuyAppication&mrpId="+mrpId;
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
		请选择生产计划单开始运算
	</div>
	<ul class="HeadingButton f-head-u">		
		<li><span class="btn btn-small" id="toGoods" name="toGoods" onClick="toCompute();" >开始运算</span></li>
		<!-- <li><span class="btn btn-small" id="toGoods" name="toGoods" onClick="toBuy();" >物料请购</span></li>  -->
	</ul>
</div>
<div class="listRange_frame" id="listRange_frame">
<div class="listRange_3" id="conditionDIV">
    	
	       	
</div>

<div class="scroll_function_small_a" id="conter" style="overflow:hidden" >
<div style="width:100%;height:100%;overflow-x:hidden;overflow:hidden;position:relative">
	<div id="k_head" style="z-index: 50; overflow: hidden; width: 1000px; position: absolute; top: 0px; left: 0px;">
	<table id="k_listgrid" style="table-layout:fixed" border="0" cellspacing="0" width="900">
		<thead>
			<tr height="24">
				<td width="35">No.</td>
				<td width="24"><input type="checkbox" onclick="jQuery('input[name=\'keyId\']').attr('checked',this.checked);" checked disableautocomplete="true" autocomplete="off"></td>
				<td width="100">规划日期</td>
				<td width="80">物料编号</td>
				<td width="80">物料名称</td>
				<td width="80">物料规格</td>
				<td width="100">单位</td>
				<td width="100">经手人</td>
				<td width="80">计划开始生产</td>
				<td width="80">生产数量</td>
				<td width="60">运算状态</td>
				<td width="60">物料请购</td>
			</tr>
		</thead>
	</table>
	</div>
	<div id="k_data" style="z-index: 40; width: 100%; overflow: auto; position: absolute; top: 0px; left: 0px; height: 354px;">
	<table id="k_listgrid" style="table-layout:fixed" border="0" cellspacing="0" width="900">
		<thead>
			<tr height="24">
				<td width="35">No.</td>
				<td width="24"><input type="checkbox" onclick="jQuery('input[name=\'keyId\']').attr('checked',this.checked);" checked disableautocomplete="true" autocomplete="off"></td>
				<td width="100">规划日期</td>
				<td width="80">物料编号</td>
				<td width="80">物料名称</td>
				<td width="80">物料规格</td>
				<td width="100">单位</td>
				<td width="100">经手人</td>
				<td width="80">计划开始生产</td>
				<td width="80">规划数量</td>
				<td width="60">运算状态</td>
				<td width="60">物料请购</td>
			</tr>
		</thead>
		<tbody>
			#foreach($row in $goodList)
			#set($hasCom = false)
			#if($!row.get("ComputerId") != "") #set($hasCom = true)   #end
			<tr height="28px" onclick="gselectKeyId(this)" class="spaceRow">
				<td>$velocityCount</td>
				<td align="center">#if(!$hasCom)  <input type="checkbox" value="$!row.get("id")"   checked name="keyId"  disableautocomplete="true" autocomplete="off"> #end</td>
				<td align="left">$!row.get("BillDate")</td>
				<td align="left"><a href="javascript:openMRP('$!row.get("id")')">$!row.get("GoodsNumber")</a></td>
				<td align="left">$!row.get("GoodsFullName")</td>
				<td align="left">$!row.get("GoodsSpec")</td>
				<td align="left">$!row.get("BaseUnit")</td>
				<td align="left">$!row.get("EmpFullName")</td>
				<td align="left">$!row.get("startWork")</td>
				<td align="right">$globals.formatNumberS($!row.get("needQty"),false,true,'','PDMRP.needQty')</td>
				<td align="center">#if($hasCom) 已运算 #else 未运算 #end</td>
				<td align="center">#if($hasCom)<a href="javascript:showApp('$!row.get("id")')">#if($!row.get("lackQty")>0) 未请购  #else 已请购   #end </a> #end</td>
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
