<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>领用用品明细</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/style1/css/oa_news.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/oa/public_goods.js"></script>
<script type="text/javascript">
function delDet(obj){		
	if(sureDel(obj)){
		form.operation.value = 3 ;
		form.part.value = "ALLDEL" ;
		form.submit();
	}
}
</script>
</head>
<body >
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" name="form" action="/ApplyGoodsAction.do" target="formFrame">
<input type="hidden" name="operation" value="4"/>
<input type="hidden" name="part" value=""/>
<input type="hidden" name="GoodsNO" value="queryAll"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">领用用品明细</div>
	<ul class="HeadingButton">
	#if($del)
		<li><button type="button" name="Btn" onclick="delDet('keyId')">删除</button></li>	
		#end
		<li><button type="button"  name="backList" 
		onClick="closeChild();" class="b2">$text.get("common.lb.close")</button>
		</li>
	</ul>	
</div>
<div id="listRange_id">
<div class="scroll_function_small_a" id="conter" style="margin-top:0px;">
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.documentElement.clientHeight-125;
oDiv.style.height=sHeight+"px";
</script>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" id="tblSort" name="table">
		<thead>
			<tr>
				<td width="2%" class="oabbs_tc">
					<input type="checkbox" name="selAll" onClick="checkAll('keyId')" /> 
				</td>							
				<td width="15%" class="oabbs_tl">用品名称</td>
				<td width="10%" class="oabbs_tc">类型</td>
	     	    <td width="8%" class="oabbs_tc">单位</td>		        
		        <td width="15%" class="oabbs_tc">领用人</td>
		        <td width="10%" class="oabbs_tc">领用日期</td>
		        <td width="8%" class="oabbs_tc">领用数量</td>	
		        <td width="8%" class="oabbs_tc">已归还</td>			              
		        <td width="25%" class="oabbs_tc">备注</td>	
		        <td></td>
			</tr>
		</thead>
		<tbody>
		  #foreach ($log in $allList)
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				<td class="oabbs_tc"> 
					<input type="checkbox" name="keyId" value="$!log.id"/> 
				</td>							
				<td>$!log.goodsName&nbsp;</td>	
				<td  >$!log.type&nbsp;</td>			 	
				<td  > $!log.unit&nbsp;</td>								
				<td > $!log.applyGoodsBean.applyRole&nbsp;</td>
				<td  > $!log.applyGoodsBean.applyDate&nbsp;</td>
				<td  align="right"> $!log.applyQty&nbsp;</td>
				<td  align="right">$!log.back_sign&nbsp;</td>
				<td  > $!log.a_use&nbsp;</td>
				<td><input type="hidden" name="applyId" id="applyId" value="$!log.applyGoodsBean.id"/> </td>		   			
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