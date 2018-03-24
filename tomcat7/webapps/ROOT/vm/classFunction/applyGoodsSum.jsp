
<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>345</title>
		<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
			<script language="javascript" src="/js/function.js"></script>
	<script type="text/javascript">

function $(obj){
	
	return document.getElementById(obj);
}

  </script>
</head>
	<body>
		<form action="/ApplyGoodsBillAction.do" name="listReport" method="post">
		<input type="hidden" name="operation" value="1">
			<div class="Heading">
				<div class="HeadingIcon">
					<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
				</div>
				<div class="HeadingTitle">
					$text.get("applyGoods.lb.totalList")
				</div>
				<ul class="HeadingButton">
				
					<li>
						<button name="add" type="submit" class="b6">$text.get("applyGoods.lb.OrderBuyBill")
						</button>
					</li>
					<li><button type="button" name="backList" title="Ctrl+Z"
							onClick="window.location.href ='/UserFunctionQueryAction.do?tableName=tblApplyGoodsBill&winCurIndex=$!winCurIndex';" class="b2">
							$text.get("common.lb.back")
						</button></li>
				</ul>
			</div>

<div class="scroll_function_small_a" id="conter">
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-140;
oDiv.style.height=sHeight+"px";
</script>
				<table border="0" cellpadding="0" cellspacing="0"
					class="listRange_list_function" id="tblSort" width="880">
					<THead>
						<tr class="listhead">
							<td class="listheade" width="30" align="center">
								<span style="vertical-align:middle;"><IMG
										src="/$globals.getStylePath()/images/down.jpg" border=0 /> </span>
							</td>
							<td width="100" align="center">
								$text.get("iniGoods.lb.goodsNo")
							</td>
							<td width="100" align="center">
								$text.get("call.lb.goodsName")
							</td>
							<td width="100" align="center">
								$text.get("applyGoods.lb.BillMount")
							</td>
							<td width="150" align="center">
								$text.get("applyGoods.lb.depotMount")
							</td>
							<td width="150" align="center">
								$text.get("applyGoods.lb.lackMount")
							</td>
							<td width="150" align="center">
								$text.get("applyGoods.lb.ComeGoSelect")
							</td>
						</tr>
					</THead>
					<TBody>
						#foreach($bean in $applys)
						<tr>
							<td class="listheadonerow" align="center" width="30">
								$velocityCount
							</td>
							<td width="100" align="center">
							#if($!bean.goodsNo != "")
								$!bean.goodsNo #else &nbsp; #end
							</td>
							<td width="100" align="center">
							#if($!bean.goodsName != "")
								$!bean.goodsName #else &nbsp; #end
							</td>
							<td width="100" align="center">
							#if($!bean.billNumber != "")
								$!bean.billNumber #else &nbsp; #end
							</td>
							<td width="150" align="center">
							#if($!bean.stockNumber != "")
								$!bean.stockNumber #else &nbsp; #end
							</td>
							<td width="150" align="center">
							#if($!bean.OOSNumber != "")
								$!bean.OOSNumber #else &nbsp; #end
							</td>
							<td width="150" align="center">
							<select name="companyCondition" style="width: 100px;">
								<option value="-1">$text.get("applyGoods.lb.priceLowest")</option>
								<option value="1">$text.get("applyGoods.lb.OrderLastest")</option>
							</select>
							</td>
						</tr>
						#end
						
					</TBody>
				</table>
				</div>
				
			#if($enable == "true")
				<div class="listRange_pagebar" style="position:relative">
					$text.get("common.theNo")$pageNo$text.get("ao.common.page")&nbsp;&nbsp;
					<a href="javascript:document.periods.submit();"
						onclick="getFristPage()">$text.get("common.firstPage")</a>&nbsp;&nbsp;#if($pageNo!=1)
					<a href="javascript:document.periods.submit();" onClick="prePage()">$text.get("common.prePage")</a>#end&nbsp;&nbsp;
					#if($pageNo!=$pageSumList.size())
					<a href="javascript:document.periods.submit();"
						onclick="nextPage()">$text.get("common.nextPage")</a>#end&nbsp;&nbsp;&nbsp;
					<select name="pageNo" id="pageNo">
						#foreach($pageNumber in $pageSumList) #if($pageNo == $pageNumber)
						<option value="$pageNumber" selected="selected">
							$pageNumber
						</option>
						#else
						<option value="$pageNumber">
							$pageNumber
						</option>
						#end #end
					</select>
					<input type="hidden" id="pageSizeBy" name="pageSizeBy"
						value="$pageSizeBy">
					<button type="submit" name="ppbutton">
						go
					</button>
					&nbsp;
				</div>
				#end
		</form>
	<script type="text/javascript">
		function nextPage(){
			$("pageNo").value = Number($("pageNo").value) + 1;
		}
		
		function prePage(){
			$("pageNo").value = Number($("pageNo").value) - 1 < 1?1:Number($("pageNo").value)-1;
		}
		
		function getFristPage(){
			$("pageNo").value = 1;
			
		}
	</script>
	</body>
</html>
