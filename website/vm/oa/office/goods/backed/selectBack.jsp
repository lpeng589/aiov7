<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>归还</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/style1/css/oa_news.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/oa/public_goods.js"></script>
<script type="text/javascript">
function showDate(){
	//document.getElementById("operation").value=15;
	document.getElementById("backedForm").submit();
};
function loadUnback(){
	$("#unblist",document).each(function(){
		$("tr",this).each(function(){					   
			var applyQty = $("#applyQty",this).val();
			var back_sign = $("#back_sign",this).val();
			if(back_sign==""){
				back_sign ="0";
				}
			var total = parseFloat(applyQty) - parseFloat(back_sign);
			$("#unback",this).val(total);   			             			   				
  		});
		})
}
</script>
<style type="text/css">
.SearchDV{padding:5px 0;overflow:hidden;}
.HeadingButton .hb_li{margin:0 0 0 5px;}
.HeadingButton .hb_li .txt_inp{width:120px;padding:0;}
.HeadingButton .hb_li .btn_inp{margin:2px 0 0 0;}
</style>
</head>
<body onload="loadUnback();">
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" name="backedForm" id="backedForm" action="/BackGoodsAction.do">
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_QUOTE')"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">归还</div>
	
</div>
<div class="SearchDV">
	<ul class="HeadingButton" style="clear:both;float:none;margin:0 0 0 10px;">
		<li class="hb_li"><span class="txt_sp">领用人：</span><input class="txt_inp" type="text" style="width: 100px" name="quRole" id="quRole" value="$!quRole"/></li>
		<li class="hb_li"><span class="txt_sp">用品名称：</span><input class="txt_inp" type="text" style="width: 100px" name="qugoods" id="qugoods" value="$!qugoods"/></li>
		<li class="hb_li"><span class="txt_sp">领用时间：</span><input class="txt_inp" type="text" style="width: 100px" name="dateTime" id="dateTime" value="$!dateTime" onClick="openInputDate(this);"/></li>
		<li class="hb_li" style="float:right;margin-right: 5px"><button class="btn_inp" type="button" name="query" onClick="showDate();" class="b2">查询</button></li>		
	</ul>
</div>
<div id="listRange_id">
<div class="scroll_function_small_a" id="conter" style="margin-top:0px;">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" id="mytable" name="mytable">
		<thead>
			<tr>
				<td width="5%" class="oabbs_tc">
				<input type="checkbox" name="selAll" onClick="checkAll('keyId')" /> 
				</td>						
				<td width="35%" class="oabbs_tl">用品名称</td>
				<td width="25%" class="oabbs_tc">领用日期</td>
	     	    <td width="16%" class="oabbs_tc">单位</td>	
	     	    <td width="10%" class="oabbs_tc">领用人</td>
	     	    <td width="10%" class="oabbs_tc">未归还数量</td>	
	     	    <td></td>
	     	    <td></td>		
				<td></td>
			</tr>
		</thead>
		<tbody id="unblist">
		  #foreach ($log in $logList)
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);"
			 ondblclick="dbFillData('$!flag','$!log.goodsName','$!log.applyGoodsBean.applyDate','$!log.unit','$!log.applyGoodsBean.applyRole','$!log.applyQty','$!log.back_sign','','$!log.id')">				
				<td  class="oabbs_tc"> 
					<input type="checkbox" name="keyId" id="keyId" value="$!log.id"/> 
				</td>							
				<td ><input type="text" id="goodsName" name="goodsName" readonly="readonly" value="$!log.goodsName"/></td>	
				<td  class="oabbs_tc"><input type="text" id="type" readonly="readonly" name="type" value="$!log.applyGoodsBean.applyDate"/></td>
				<td  class="oabbs_tc"><input type="text" id="unit" name="unit" readonly="readonly" value="$!log.unit"/> </td>
				<td  class="oabbs_tc"><input type="text" id="applyRole" readonly="readonly"   name="applyRole" value="$!log.applyGoodsBean.applyRole"/> </td>
				<td  class="oabbs_tc"><input type="text" id="unback" readonly="readonly" name="unback" value=""/> </td>	
				<td><input type="hidden" id="back_sign" name="back_sign" readonly="readonly" value="$!log.back_sign"/> </td>
				<td><input type="hidden" id="applyQty" name="applyQty" readonly="readonly" value="$!log.applyQty"/> </td>
				<td><input type="hidden" id="id" name="id" value="$!log.id"/> </td>							       		
			</tr>
			#end
		  </tbody>
		</table>
	</div>
<div class="listRange_pagebar"> $!pageBar </div>
</div>
</form>
</body>

</html>