<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("unitadmin.title.update")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>


<script language="javascript">
  function editPre(){
  	if(!isChecked("keyId")){		
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	form.winCurIndex.value="$!winCurIndex";
	form.operation.value="$globals.getOP("OP_UPDATE_PREPARE")";
	form.submit();
  }
  function beforeSumit(){
  	if(form.stockName.value=="default"){
  		alert("$text.get("iniGoods.msg.selectStock")");
  		form.stockName.focus()
  		return false
  	}
  	return true;
  }
</script>
</head>

<body onLoad="showtable('tblSort');showStatus();form.stockName.focus();">

<form  method="post" scope="request"  name="form" action="/IniGoodsQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
 <input type="hidden" name="goodsCode" value="$!goodsCode">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("iniGoods.lb.title")</div>
	<ul class="HeadingButton">
	#if($globals.getMOperation().update())
	<li><button type="button" onClick="return editPre();" class="b2">$text.get("common.lb.edit")</button></li>
	#end
	#if($globals.getMOperation().query())
			<li>
				<button type="submit"  onClick="if(beforeSumit()){forms[0].operation.value=$globals.getOP("OP_QUERY");return beforeBtnQuery();}else{return false;}" class="b2">$text.get("common.lb.query")</button>
			</li>
	#end
	#if($globals.getMOperation().query() && "$!goodsCode"!="")
	<li><button type="button" onClick="location.href='/IniGoodsQueryAction.do?type=back&goodsCode=$!goodsCode&winCurIndex=$!winCurIndex&stockName=$!iniGoodsForm.stockName'" class="b2">$text.get("common.lb.back")</button></li>
	#end
</ul>	
</div>
<div id="listRange_id">
		<div class="listRange_1">
			<li><span>$text.get("iniGoods.lb.goodsNo")：</span>
			  <input name="goodsNumber" type="text" value="$!iniGoodsForm.goodsNumber" onKeyDown="if(event.keyCode==13)event.keyCode=9;"></li>
			<li><span>$text.get("iniGoods.lb.goodsName")：</span>
			  <input name="goodsFullName" type="text" value="$!iniGoodsForm.goodsFullName" onKeyDown="if(event.keyCode==13)event.keyCode=9;"></li>
			<li><span>$text.get("iniGoods.lb.stock")：</span>		
			<select name="stockName" onKeyDown="if(event.keyCode==13)event.keyCode=9;">
			  	<option value="default"></option>
			  	#foreach($stock in $stockList)
			  		#set ($select="")
			  		#if("$stock.StockCode"=="$!iniGoodsForm.stockName")
			  			#set ($select="selected")
			  		#end
			  		<option value="$stock.StockCode" $!select>$stock.StockFullName</option>
			  	#end
			  	#set ($select="")
			  	#if("all"=="$!iniGoodsForm.stockName")
			  		#set ($select="selected")
			  	#end
			  	<option value="all" $!select>$text.get("com.goodsInit.allStock")</option>
			  </select>
			</li>
			<li><span>$text.get("common.lb.dimQuery")：</span><input name="dimQuery" type="text" value="$!iniGoodsForm.dimQuery" onKeyDown="if(event.keyCode==13)event.keyCode=9;"></li>				
			<!--$!iniGoodsForm.stockName<li><span>$text.get("iniGoods.lb.stockState")：</span><input name="storeStateID" type="text" value="$!iniGoodsSearchForm.storeStateID"></li>		-->
	
		</div>
		#if($parentName.length()>0)			
			<div class="scroll_function_big"><span>$text.get("common.msg.currstation")：$!parentName</span></div>
		#end
		<div class="scroll_function_small_a" id="conter">
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
			<thead>
				<tr class="scrollColThead">
				 <td width="45" align="center"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
					<td width="120" align="center">$text.get("iniGoods.lb.goodsNo")</td>
					<td width="150" align="center">$text.get("iniGoods.lb.goodsName")</td>
					<td width="80" align="center">$text.get("iniGoods.lb.goodsSpec")</td>
					<td width="80" align="center">$text.get("iniGoods.lb.goodsUnit")</td>
					<td width="150" align="center">$text.get("iniGoods.lb.stock")</td>
					#if($!existsTowUnit)
					 <td width="100" align="center">$text.get("iniGoods.lb.iniTwoQty")</td>
					#end
					<td width="100" align="center">$text.get("iniGoods.lb.iniQty")</td>
					<td width="100" align="center">$text.get("iniGoods.lb.iniAmount")</td>				
					<td width="80" align="center">#if("$currPeriod"=="-1")$text.get("common.lb.update")#else $text.get("common.lb.detail") #end</td>
					<td width="80" align="center">$text.get("common.lb.showNext")</td>
				</tr>			
			</thead>
			<tbody> 
					#foreach ($row in $result )
				<tr #if($globals.getMOperation().query()) onDblClick="location.href='/IniGoodsQueryAction.do?goodsCode=$globals.get($row,0)&stockName=$!globals.encode($!iniGoodsForm.getStockName())&stockCode=$globals.get($row,1)&type=next&winCurIndex=$!winCurIndex'" #end onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
					<td align ="center" class="listheadonerow"><input type="checkbox" name="keyId" value="$globals.get($row,0)@$globals.get($row,1)">
					</td>
					<td align="left">$!globals.encodeHTMLLine($globals.get($row,2)) &nbsp;</td>
					<td align="left">
					#if($globals.getMOperation().query())
					<a href='/IniGoodsQueryAction.do?goodsCode=$globals.get($row,0)&stockCode=$globals.get($row,1)&goodsFullName=$!globals.encode($!iniGoodsForm.getGoodsFullName())&type=next&winCurIndex=$!winCurIndex&goodsNumber=$!iniGoodsForm.getGoodsNumber()&stockName=$!globals.encode($!iniGoodsForm.getStockName())&dimQuery=$!globals.encode($!iniGoodsForm.getDimQuery())&pageNo=$!pageNo'>$!globals.encodeHTMLLine($globals.get($row,3)) &nbsp;</a>#else &nbsp;#end</td>	
					<td align="left">$!globals.get($!row,8) &nbsp;</td>		
					<td align="left">$!globals.get($!row,9) &nbsp;</td>				
					<td align="left">$!globals.get($!row,4) &nbsp;</td>
					#if($!existsTowUnit)
					<td align="right">$!globals.get($!row,10) &nbsp;</td>	
					#end
					<td align="right">$!globals.get($!row,5) &nbsp;<input name="number" type="hidden" value="$globals.get($row,5)"/></td>
					<td align="right">$!globals.get($!row,6) &nbsp;<input name="balance" type="hidden" value="$globals.get($row,6)" /></td>
					<td align ="center">#if($globals.getMOperation().update())<a href='/IniGoodsQueryAction.do?goodsCode=$globals.get($row,0)&stockCode=$globals.get($row,1)&goodsFullName=$!globals.encode($!iniGoodsForm.getGoodsFullName())&type=next&winCurIndex=$!winCurIndex&goodsNumber=$!iniGoodsForm.getGoodsNumber()&stockName=$!globals.encode($!iniGoodsForm.getStockName())&dimQuery=$!globals.encode($!iniGoodsForm.getDimQuery())&pageNo=$!pageNo'>#if("$currPeriod"=="-1")$text.get("common.lb.update")#else $text.get("common.lb.detail") #end</a>#else &nbsp; #end</td>
					<td align ="center">#if($globals.getMOperation().query())
	#if($globals.get($row,7)>0)
					<a href='/IniGoodsQueryAction.do?goodsCode=$globals.get($row,0)&stockCode=$globals.get($row,1)&type=next&winCurIndex=$!winCurIndex&goodsNumber=$!iniGoodsForm.getGoodsNumber()&goodsFullName=$!iniGoodsForm.getGoodsFullName()&stockName=$!iniGoodsForm.getStockName()&dimQuery=$!iniGoodsForm.getDimQuery()&pageNo=$!pageNo'>$text.get("common.lb.showNext")</a>
	#else
		$text.get("common.lb.noChild")
	#end
#else &nbsp; #end</td>
				</tr>
				#end
				
				<tr>
					<td align ="center"><strong>$text.get("common.lb.total")</strong></td>
					<td align="left">&nbsp; </td>
					<td align="left">&nbsp; </td>
					<td align="left">&nbsp; </td>					
					<td align="left">&nbsp; </td>
					<td align="left">&nbsp; </td>
					#if($!existsTowUnit)
					<td align ="right">&nbsp;</td>
					#end
					<td align="right" id="numberTotal">&nbsp;</td>
					<td align ="right" id="balanceTotal">&nbsp;</td>
					<td align="right" >&nbsp; </td>
					<td align ="right">&nbsp;</td>
				</tr>
				<script language="javascript" type="text/javascript">
					//生成合计数





					var balances = document.getElementsByName("balance");									
					var balanceTotal = 0;							
					for(var i=0;i<balances.length;i++){
						balanceTotal += parseFloat(balances[i].value.replaceAll(",",""));								
					}					
					document.getElementById("balanceTotal").innerHTML="<strong>"+Number(balanceTotal).toFixed(2)+"</strong>";	
						
					var numbers = document.getElementsByName("number");									
					var numberTotal = 0;							
					for(var i=0;i<numbers.length;i++){
						numberTotal += parseFloat(numbers[i].value.replaceAll(",",""));								
					}					
					document.getElementById("numberTotal").innerHTML="<strong>"+Number(numberTotal).toFixed(2)+"</strong>";					
				</script>
					</tbody>
		  </table>
		</div>
<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-115;
oDiv.style.height=sHeight+"px";
//]]>
</script>
	<div class="listRange_pagebar"> $!pageBar </div>	
	
</div>
</form>
</body>
</html>
