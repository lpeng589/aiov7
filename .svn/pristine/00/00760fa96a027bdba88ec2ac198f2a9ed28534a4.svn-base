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
function cl(inp)
{
	setTimeout(function(){if(inp.dropdown != undefined)inp.dropdown.close();},200);
}

function toWorkOrder(){
	if($("input[name=keyId]:checked").size()==0){
		alert("请选择至少一个物料");
		return;
	}
	if(!confirm("确定下达工令吗?")){
		return;
	}
	
	
	var error = "";
	var saveValues=new Array();
	$("input[name=keyId]:checked").each(function(){
		var pos = $("input[name=keyId]").index(this);
		var Qty = $("input[name=Qty]:eq("+pos+")").val();
		if(Qty<=0){
			error= $(this).attr("GoodsNumber") + "不能小于等于0";
		}
		saveValues[saveValues.length] = {id:$(this).val(),
				type:$(this).attr("otype"),
				Qty:Qty};
	});
	if(error != ""){
		alert(error);
		return ;
	}
	var postdata = JSON.stringify(saveValues); //生成所有需提交数据的json字符串
	if(typeof(top.jblockUI)!="undefined" ){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	jQuery.post("/Mrp.do?type=toWorkOrder",{data:postdata},function(json){ 
		if(json.code == "ERROR"){
			alert(json.msg);
		}else{
			alert("下达工令成功，别忘了送审");
			changeConfirm();
			
			window.location.href=window.location.href;
		}
		
		if(typeof(top.jblockUI)!="undefined" ){
			top.junblockUI()
		}
	},"json" ); 
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
		下达工令
	</div>
	<ul class="HeadingButton f-head-u">		
		<li><span class="btn btn-small" id="toGoods" name="toGoods" onClick="toWorkOrder();" >下达工令</span></li>
		<li><span class="btn btn-small" id="toGoods" name="toGoods" onClick="closeWindows('$!popWinName');" >返回</span></li>
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
				<td width="24"><input type="checkbox" checked onclick="jQuery('input[name=\'keyId\']').attr('checked',this.checked);" disableautocomplete="true" autocomplete="off"></td>
				<td width="40">类型</td>
				#foreach($rf in $GoodsFList)
					<td width="$globals.get($rf,2)">$globals.get($rf,1)</td>
				#end
				
				<td width="80">工令总数</td>
				<td width="80">已下达数</td>
				<td width="80">本次下达数</td>
			</tr>
		</thead>
	</table>
	</div>
	<div id="k_data" style="z-index: 40; width: 100%; overflow: auto; position: absolute; top: 0px; left: 0px; height: 354px;">
	<table id="k_listgrid" style="table-layout:fixed" border="0" cellspacing="0" width="900">
		<thead>
			<tr height="24">
				<td width="35">No.</td>
				<td width="24"><input type="checkbox" checked onclick="jQuery('input[name=\'keyId\']').attr('checked',this.checked);" disableautocomplete="true" autocomplete="off"></td>
				<td width="40">类型</td>
				#foreach($rf in $GoodsFList)
					<td width="$globals.get($rf,2)">$globals.get($rf,1)</td>
				#end
				<td width="80">工令总数</td>
				<td width="80">已下达数</td>
				<td width="80">本次下达数</td>
			</tr>
		</thead>
		<tbody>
			#foreach($row in $workList)
			<tr height="28px" onclick="gselectKeyId(this)" class="spaceRow">
				<td>$velocityCount</td>
				<td align="center"><input type="checkbox" otype="$row.get("orderno")" GoodsNumber="$!row.get("GoodsNumber")" value="$!row.get("id")" 
				  onclick="this.checked=!this.checked"
				  checked name="keyId"  disableautocomplete="true" autocomplete="off"></td>
				<td align="right">#if($row.get("orderno")=="1"  ) 子工令 #else 主工令 #end</td>  
				#foreach($rf in $GoodsFList)
					<td align="left">$!row.get($globals.get($rf,0))</td>
				#end
				<td align="right">$globals.formatNumberS($!row.get("needWQty"),false,true,'','PDMRP.needQty')</td>
				<td align="right">$globals.formatNumberS($!row.get("workOrderQty"),false,true,'','PDMRP.needQty')</td>
				<td align="right"><input class="ls3_in" style="width:80px;" onclick=" event.stopPropagation(); " name="Qty" value="$globals.formatNumberS($!row.get("Qty"),false,false,'','PDMRP.needQty')" disableautocomplete="true" autocomplete="off" > </td>
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
