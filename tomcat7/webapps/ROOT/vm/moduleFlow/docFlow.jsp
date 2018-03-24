<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script src="/js/jquery.js" language="javascript"></script>
<script language="javascript">
function showreModule(title,href)
{		
	top.showreModule(title,href);	
}
</script>
<script type="text/javascript">
	$(function(){
		$(".menuReport").click(function(){
			$(this).children("ul").show().end().siblings().children("ul").hide();
		});
	});
</script>
<style>
p {margin:0px; padding:0px;}
.fc{color:#999}

</style>
</head>

<body scroll="no">

<form  method="post" scope="request" name="form" action="/CustomQueryAction.do">
 <input type="hidden" name="operation" value="4">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="a8358e12dec066bd31066cc9f10a282d">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$!name</div>

</div>

<div class="listRange_frame" style="overflow-y:auto" align="center" id="outconter">
<script type="text/javascript">
				var oDiv=document.getElementById("outconter");
				var sHeight=document.body.clientHeight-45;
				oDiv.style.height=sHeight+"px";
				</script>

  <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center" width="*" valign="middle">
    
 #if("$!globals.getVersion()"=="3")
<div class="scroll_function_small_menuReport">
#if($text.get("moduleFlow.lb.orderManage")=="$!name")
<div style="margin-top:5px;">
 <table style="width:540px" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
    </tr>
  <tr>
    <td height="60" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
  <tr>
    <td align="center">
	#if($globals.hasQuery("tblBuyOrder"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.BuyAskPrice")','/UserFunctionQueryAction.do?tableName=tblBuyAskPrice&src=menu&operation=6');" title="$text.get("moduleFlow.lb.BuyAskPrice")"><img src="/style/images/flow/pro4.gif" width="48" height="48" border="0"></a></p>
	 <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.BuyAskPrice")','/UserFunctionQueryAction.do?tableName=tblBuyAskPrice&src=menu&operation=6');" title="$text.get("moduleFlow.lb.BuyAskPrice")">$text.get("moduleFlow.lb.BuyAskPrice")</a></p>
	#else
	<p><img src="/style/images/flow/pro5.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.BuyAskPrice")</p>
	 #end
	 </td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblBuyOrder"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.Order")','/UserFunctionQueryAction.do?tableName=tblBuyOrder&src=menu&src=menu&operation=6');" title="$text.get("moduleFlow.lb.Order")"><img src="/style/images/flow/pro5.gif" width="48" height="48" border="0"></a></p>
	 <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.Order")','/UserFunctionQueryAction.do?tableName=tblBuyOrder&src=menu&src=menu&operation=6');" title="$text.get("moduleFlow.lb.Order")">$text.get("moduleFlow.lb.Order")</a></p>
	#else
	<p><img src="/style/images/flow/pro5.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.Order")</p>
	 #end
	 </td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblBuyInStock"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OrderList")','/UserFunctionQueryAction.do?tableName=tblBuyInStock&src=menu&operation=6');" title="$text.get("moduleFlow.lb.OrderList")"><img src="/style/images/flow/pro6.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OrderList")','/UserFunctionQueryAction.do?tableName=tblBuyInStock&src=menu&operation=6');" title="$text.get("moduleFlow.lb.OrderList")">$text.get("moduleFlow.lb.OrderList")</a></p>
	  #else
	 <p><img src="/style/images/flow/pro6.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.OrderList")</p>
	   #end
	</td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblPay"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OrderPayList")','/UserFunctionQueryAction.do?tableName=tblPay&src=menu&operation=6');" title="$text.get("moduleFlow.lb.OrderPayList")"><img src="/style/images/flow/pro2.gif" width="48" height="48" border="0"></a></p>
      <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OrderPayList")','/UserFunctionQueryAction.do?tableName=tblPay&src=menu&operation=6');" title="$text.get("moduleFlow.lb.OrderPayList")">$text.get("moduleFlow.lb.OrderPayList")</a></p>
	   #else
	 <p><img src="/style/images/flow/pro2.gif" width="48" height="48" border="0"></p> 
      <p class="fc">$text.get("moduleFlow.lb.OrderPayList")</p>
	  #end
	</td>
    </tr>
  <tr>
    <td height="60" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center"><img src="/style/images/flow/flowdown.gif"></td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblBuyOutStock"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OrderTuiList")','/UserFunctionQueryAction.do?tableName=tblBuyOutStock&src=menu&operation=6');" title="$text.get("moduleFlow.lb.OrderTuiList")"><img src="/style/images/flow/pro3.gif" width="48" height="48" border="0"></a></p>
      <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OrderTuiList")','/UserFunctionQueryAction.do?tableName=tblBuyOutStock&src=menu&operation=6');" title="$text.get("moduleFlow.lb.OrderTuiList")">$text.get("moduleFlow.lb.OrderTuiList")</a></p>
	   #else
	  <p><img src="/style/images/flow/pro3.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.OrderTuiList")</p>
	   #end
	</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
</table>
</div>
#elseif($text.get("moduleFlow.lb.SellManage")=="$!name")
<div style="margin-top:20px;">
<table style="width:540px" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
    </tr>
  <tr>
    <td align="center">
	<p><img src="/style/images/flow/sale5.gif" width="48" height="48" border="0"></p>
	<p class="fc">$text.get("moduleFlow.lb.CRMsalesQuot")(CRM)</p></td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblSalesOrder"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesOrder")','/UserFunctionQueryAction.do?tableName=tblSalesOrder&src=menu&operation=6');" title="$text.get("moduleFlow.lb.SalesOrder")"><img src="/style/images/flow/sale4.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesOrder")','/UserFunctionQueryAction.do?tableName=tblSalesOrder&src=menu&operation=6');" title="$text.get("moduleFlow.lb.SalesOrder")">$text.get("moduleFlow.lb.SalesOrder")</a></p>
	 #else
	 <p><img src="/style/images/flow/sale4.gif" width="48" height="48" border="0"></p>
     <p class="fc">$text.get("moduleFlow.lb.SalesOrder")</p>
	 #end
	</td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblSalesOutStock"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesOutStock")','/UserFunctionQueryAction.do?tableName=tblSalesOutStock&src=menu&operation=6');" title="$text.get("moduleFlow.lb.SalesOutStock")"><img src="/style/images/flow/sale6.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesOutStock")','/UserFunctionQueryAction.do?tableName=tblSalesOutStock&src=menu&operation=6');" title="$text.get("moduleFlow.lb.SalesOutStock")">$text.get("moduleFlow.lb.SalesOutStock")</a></p>
	#else
	<p><img src="/style/images/flow/sale6.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.SalesOutStock")</p>
	#end
	</td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblSaleReceive"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SaleReceive")','/UserFunctionQueryAction.do?tableName=tblSaleReceive&src=menu&operation=6');" title="$text.get("moduleFlow.lb.SaleReceive")"><img src="/style/images/flow/sale2.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SaleReceive")','/UserFunctionQueryAction.do?tableName=tblSaleReceive&src=menu&operation=6');" title="$text.get("moduleFlow.lb.SaleReceive")">$text.get("moduleFlow.lb.SaleReceive")</a></p>
	#else
	<p><img src="/style/images/flow/sale2.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.SaleReceive")</p>
	#end
	</td>
    </tr>
  <tr>
    <td height="60" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center"><img src="/style/images/flow/flowdown.gif"></td>
    <td align="center">&nbsp;</td>
    <td align="center"><img src="/style/images/flow/flowdown.gif"></td>
    <td align="center">&nbsp;</td>
    <td align="center"><img src="/style/images/flow/flowdown.gif"></td>
    </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblSalesRetailStock"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.TempSalesOrder")','/UserFunctionQueryAction.do?tableName=tblSalesRetailStock&src=menu&operation=6');" title="$text.get("moduleFlow.lb.TempSalesOrder")"><img src="/style/images/flow/war8.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.TempSalesOrder")','/UserFunctionQueryAction.do?tableName=tblSalesRetailStock&src=menu&operation=6');" title="$text.get("moduleFlow.lb.TempSalesOrder")">$text.get("moduleFlow.lb.TempSalesOrder")</a></p>
	#else
	<p><img src="/style/images/flow/war8.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.TempSalesOrder")</p>
	#end
	  </td>
    <td align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblSalesReturnStock"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesReturnStock")','/UserFunctionQueryAction.do?tableName=tblSalesReturnStock&src=menu&operation=6');" title="$text.get("moduleFlow.lb.SalesReturnStock")"><img src="/style/images/flow/sale3.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesReturnStock")','/UserFunctionQueryAction.do?tableName=tblSalesReturnStock&src=menu&operation=6');" title="$text.get("moduleFlow.lb.SalesReturnStock")">$text.get("moduleFlow.lb.SalesReturnStock")</a></p>
	 #else
	 <p><img src="/style/images/flow/sale3.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.SalesReturnStock")</p>
	 #end
	</td>
    <td align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblSalesInvoiceInfo"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesTicketManage")','/UserFunctionQueryAction.do?tableName=tblSalesInvoiceInfo&src=menu&operation=6');" title="$text.get("moduleFlow.lb.SalesTicketManage")"><img src="/style/images/flow/fin8.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesTicketManage")','/UserFunctionQueryAction.do?tableName=tblSalesInvoiceInfo&src=menu&operation=6');" title="$text.get("moduleFlow.lb.SalesTicketManage")">$text.get("moduleFlow.lb.SalesTicketManage")</a></p>
	 #else
	<p><img src="/style/images/flow/fin8.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.SalesTicketManage")</p>
	#end
	</td>
    </tr>
</table>
</div>
#elseif($text.get("muduleFlow.lb.Shengchanmanage")=="$!name")
	<div style=" padding-top:10px;">
<table width="680" border="0" cellpadding="0" cellspacing="0" style="width:680px">
  <tr>
    <td width="101" align="center">&nbsp;</td>
    <td width="35" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td width="47" align="center">&nbsp;</td>
    <td colspan="2" align="center">
		<p>&nbsp;</p>     </td>
    <td align="center">&nbsp;</td>
    <td width="75" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td width="101" align="center">&nbsp;</td>
    </tr>
  
  <tr>
    <td height="217" align="center">

	  #if($globals.hasQuery("tblBuyOutStock"))
	   <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.produceplan")','/Mrp.do?method=orderSel&winCurIndex=3');" title="$text.get('muduleFlow.lb.produceplan')"><img src="/style/images/flow/sale1.gif" width="48" height="48" border="0"></a></p>	  
	  <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.produceplan")','/Mrp.do?method=orderSel&winCurIndex=3');" title="$text.get('muduleFlow.lb.produceplan')">$text.get("muduleFlow.lb.produceplan")</a></p>
	   #else
	  	<p><img src="/style/images/flow/sale1.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("muduleFlow.lb.produceplan")</p>
	   #end
	   
	  #if($globals.hasQuery("tblModuleField"))
      <p><a href="javascript:showreModule('工序单配置','/UserFunctionQueryAction.do?tableName=tblModuleField&src=menu&operation=6');" title="工序单配置"><img 

src="/style/images/flow/sale6.gif" width="48" height="48" border="0"></a></p>	  
      <p><a href="javascript:showreModule('工序单配置','/UserFunctionQueryAction.do?tableName=tblModuleField&src=menu&operation=6');" title="工序单配置">工序单配置</a></p>
	  #else
	   <p><img src="/style/images/flow/sale6.gif" width="48" height="48" border="0"></p>	  
       <p class="fc">$text.get("moduleFlow.lb.SalesOrder")</p>
	   #end
	   
	    #if($globals.hasQuery("tblWorking"))
	  <p><a href="javascript:showreModule('工序流程设置','/UserFunctionQueryAction.do?tableName=tblWorking&parentCode=&operation=6');" title="工序流程设置"><img src="/style/images/flow/pro6.gif" width="48" height="48" border="0"></a></p>	  
	  <p><a href="javascript:showreModule('工序流程设置','/UserFunctionQueryAction.do?tableName=tblWorking&parentCode=&operation=6');" title="工序流程设置">工序流程设置</a></p>
	  #else
	  <p><img src="/style/images/flow/pro6.gif" width="48" height="48" border="0"></p>	  
	  <p class="fc">工序流程设置</p>
	   #end
	   
	   #if($globals.hasQuery("tblBOM"))
	  <p><a href="javascript:showreModule('$text.get("QuoteArts")','/UserFunctionQueryAction.do?tableName=tblBOM&operation=6');" title="$text.get('QuoteArts')"><img src="/style/images/flow/fin10.gif" width="48" height="48" border="0"></a></p>	  
	  <p><a href="javascript:showreModule('$text.get("QuoteArts")','/UserFunctionQueryAction.do?tableName=tblBOM&operation=6');" title="$text.get('QuoteArts')">$text.get("QuoteArts")</a></p>
	  #else
	  <p><img src="/style/images/flow/fin10.gif" width="48" height="48" border="0"></p>	  
	  <p class="fc">$text.get("muduleFlow.lb.produceDetailList")</p>
	   #end
	  
	  </td>
    <td align="center"><img src="/style/images/flow/m_flow4wf2.gif"></td>
    <td width="73" align="center">
	#if($LoginBean.operationMap.get("/Mrp.do").query())
		<p><a href="javascript:showreModule('$text.get("mrp.lb.mrpCount")','/Mrp.do?method=orderSel');"title="$text.get('mrp.lb.mrpCount')"><img 

src="/style/images/flow/sale5.gif" width="48" height="48" border="0"></a></p>		
		<p><a href="javascript:showreModule('$text.get("mrp.lb.mrpCount")','/Mrp.do?method=orderSel');" title="$text.get('mrp.lb.mrpCount')">$text.get("mrp.lb.mrpCount")</a></p>
	#else
		 <p><img src="/style/images/flow/sale5.gif" width="48" height="48" border="0"></p>	  
	 	 <p class="fc">$text.get("mrp.lb.mrpCount")</p>
	#end
	</td>
    <td width="32" align="center"><img src="/style/images/flow/m_flowwf.gif"></td>
    <td width="74" align="center">
	#if($globals.hasQuery("tblBuyOrder"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.Order")','/UserFunctionQueryAction.do?tableName=tblBuyOrder&parentCode=&operation=6');" title="$text.get('moduleFlow.lb.Order')"><img src="/style/images/flow/pro2.gif" width="48" height="48" border="0"></a></p>
      <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.Order")','/UserFunctionQueryAction.do?tableName=tblBuyOrder&parentCode=&operation=6');" title="$text.get('moduleFlow.lb.Order')">$text.get("moduleFlow.lb.Order")</a></p>
	   #else
	  <p><img src="/style/images/flow/pro2.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.Order")</p>
	   #end
	  
#if($globals.hasQuery("tblProduceBP"))
      <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.produceTaskbill")','/UserFunctionQueryAction.do?tableName=tblProduceBP&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.produceTaskbill')"><img src="/style/images/flow/war8.gif" width="48" height="48" border="0"></a></p>
      <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.produceTaskbill")','/UserFunctionQueryAction.do?tableName=tblProduceBP&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.produceTaskbill')">$text.get("muduleFlow.lb.produceTaskbill")</a></p>
	  #else
	   <p><img src="/style/images/flow/war8.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("muduleFlow.lb.produceTaskbill")</p>
	  #end
	  
	  </td>
    <td align="right"><img src="/style/images/flow/flowwf.gif"></td>
    <td colspan="2" align="center">
	 #if($globals.hasQuery("tblOutMaterialsBP"))
	<p><a href="javascript:showreModule('$text.get("OutMaterialsBP")','/UserFunctionQueryAction.do?tableName=tblOutMaterialsBP&parentCode=&operation=6');" title="$text.get('OutMaterialsBP')"><img src="/style/images/flow/pro3.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("OutMaterialsBP")','/UserFunctionQueryAction.do?tableName=tblOutMaterialsBP&parentCode=&operation=6');" title="$text.get('OutMaterialsBP')">$text.get("OutMaterialsBP")</a></p>
	#else
	<p><img src="/style/images/flow/pro3.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("OutMaterialsBP")</p>
	  #end
	  
	
	
		
		#if($globals.hasQuery("tblInProductBP"))
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.material4")','/UserFunctionQueryAction.do?tableName=tblInProductBP&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.material4')"><img src="/style/images/flow/war7.gif" width="48" height="48" border="0"></a></p>		
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.material4")','/UserFunctionQueryAction.do?tableName=tblInProductBP&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.material4')">$text.get("muduleFlow.lb.material4")</a></p>
		#else
		<p><img src="/style/images/flow/war7.gif" width="48" height="48" border="0"></p>		
		<p class="fc">$text.get("muduleFlow.lb.material4")</p>
		#end
		
		
		</td>
    <td width="47" align="left"><img src="/style/images/flow/m_line.gif"></td>
    <td width="75" align="center">
	#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=tblCosting").query())
	<p><a href="javascript:showreModule('成本核算','/UserFunctionQueryAction.do?tableName=tblCosting');" title="成本核算"><img src="/style/images/flow/war3.gif" width="48" 

height="48" border="0"></a></p>
      <p><a href="javascript:showreModule('成本核算','/UserFunctionQueryAction.do?tableName=tblCosting');" title="成本核算">成本核算</a></p>
	  #else
	  <p><img src="/style/images/flow/war3.gif" width="48" height="48" border="0"></p>
      <p class="fc">成本核算</p>
	  #end
	  </td>
    <td width="32" align="center"><img src="/style/images/flow/m_flow4wf.gif"></td>
    <td align="center" style="color:#999999"><p><img src="/style/images/flow/fin2.gif" width="48" height="48" border="0"></p>
        <p>$text.get("muduleFlow.lb.TermMaterial")</p>
        <p><img src="/style/images/flow/fin4.gif" width="48" height="48" border="0"></p>        
        <p>$text.get("muduleFlow.lb.TermMaterialed")</p>
        <p><img src="/style/images/flow/fin6.gif" width="48" height="48" border="0"></p>        
        <p>$text.get("muduleFlow.lb.moneyEveryDet")</p>
  </tr>
	
	 <tr>
    <td height="60" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td colspan="2" align="center"><p><img src="/style/images/flow/m_flowwf2.gif"></p>      </td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td colspan="2" align="center">
	#if($globals.hasQuery("tblEntrustGoods"))
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.returnbill")','/UserFunctionQueryAction.do?tableName=tblEntrustGoods&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.returnbill')"><img src="/style/images/flow/war2.gif" width="48" height="48" border="0"></a></p>		
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.returnbill")','/UserFunctionQueryAction.do?tableName=tblEntrustGoods&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.returnbill')">$text.get("muduleFlow.lb.returnbill")</a></p>
		 #else
		<p><img src="/style/images/flow/war2.gif" width="48" height="48" border="0"></p>		
		<p class="fc">$text.get("muduleFlow.lb.returnbill")</a></p>
		 #end
		
		</td>
    <td colspan="2" align="center">
	#if($globals.hasQuery("tblProduceCostBP"))
	<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.moneyEvery")','/UserFunctionQueryAction.do?tableName=tblProduceCostBP&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.moneyEvery')"><img src="/style/images/flow/fin9.gif" width="48" height="48" border="0"></a></p>
      <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.moneyEvery")','/UserFunctionQueryAction.do?tableName=tblProduceCostBP&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.moneyEvery')">$text.get("muduleFlow.lb.moneyEvery")</a></p>
	  #else
	  <p><img src="/style/images/flow/fin9.gif" width="48" height="48" border="0"></a></p>
      <p class="fc">$text.get("muduleFlow.lb.moneyEvery")</a></p>
	  #end
	  </td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
</table>
</div>
#elseif($text.get("muduleFlow.lb.WeiWaimanage")=="$!name")
<div style="margin-top:20px;">
<table style="width:800px;" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
  </tr>
  <tr>
    <td align="center"></td>
  	<td width="120" rowspan="4" align="center"><img src="/style/images/flow/flow4wf.gif" width="60" height="250"></td>
  	<td width="100" rowspan="1" align="center" valign="middle">
		#if($globals.hasQuery("tblProDrawBefore"))
		<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.tblProDrawBefore")','/UserFunctionQueryAction.do?

tableName=tblProDrawBefore&src=menu&operation=6');" title="$text.get("moduleFlow.lb.tblProDrawBefore")"><img src="/style/images/flow/war10.gif" width="48" height="48" 

border="0"></a></p>
    	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.tblProDrawBefore")','/UserFunctionQueryAction.do?tableName=tblProDrawBefore&src=menu&operation=6');" title="$text.get("moduleFlow.lb.tblProDrawBefore")">$text.get("moduleFlow.lb.tblProDrawBefore")</a></p>
	 	#else
		<p><img src="/style/images/flow/war10.gif" width="48" height="48" border="0"></p>
    	<p class="fc">$text.get("moduleFlow.lb.tblProDrawBefore")</p>
		#end
	</td>
  	<td align="center"><img src="/style/images/flow/flowr.gif"></td>
  	<td width="100" rowspan="1" align="center" valign="middle">
		#if($globals.hasQuery("tblProCheckBefore"))
		<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.tblProCheckBefore")','/UserFunctionQueryAction.do?

tableName=tblProCheckBefore&src=menu&operation=6');" title="$text.get("moduleFlow.lb.tblProCheckBefore")"><img src="/style/images/flow/sale1.gif" width="48" height="48" 

border="0"></a></p>
    	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.tblProCheckBefore")','/UserFunctionQueryAction.do?tableName=tblProCheckBefore&src=menu&operation=6');" title="$text.get("moduleFlow.lb.tblProCheckBefore")">$text.get("moduleFlow.lb.tblProCheckBefore")</a></p>
	 	#else
		<p><img src="/style/images/flow/sale1.gif" width="48" height="48" border="0"></p>
    	<p class="fc">$text.get("moduleFlow.lb.tblProCheckBefore")</p>
		#end
	</td>
  	<td width="120" rowspan="4" align="center"><img src="/style/images/flow/m_flow4wf2.gif" width="60" height="250"></td>
  </tr>
  <tr>
     <td width="100" rowspan="2" align="center" valign="middle">
		#if($globals.hasQuery("tblProduceInform"))
		<p><a href="javascript:showreModule('$text.get("QuoteProduceNotice")','/UserFunctionQueryAction.do?tableName=tblProduceInform&src=menu&operation=6');" title="$text.get("QuoteProduceNotice")"><img src="/style/images/flow/pro5.gif" width="48" height="48" border="0"></a></p>
    	<p><a href="javascript:showreModule('$text.get("QuoteProduceNotice")','/UserFunctionQueryAction.do?tableName=tblProduceInform&src=menu&operation=6');" title="$text.get("QuoteProduceNotice")">$text.get("QuoteProduceNotice")</a></p>
	 	#else
		<p><img src="/style/images/flow/pro5.gif" width="48" height="48" border="0"></p>
    	<p class="fc">$text.get("QuoteProduceNotice")</p>
		#end
	</td>
  	<td width="100" rowspan="1" align="center" valign="middle">
		#if($globals.hasQuery("tblProDrawLoom"))
		<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.tblProDrawLoom")','/UserFunctionQueryAction.do?tableName=tblProDrawLoom&src=menu&operation=6');" title="$text.get("moduleFlow.lb.tblProDrawLoom")"><img src="/style/images/flow/war10.gif" width="48" height="48" border="0"></a></p>
    	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.tblProDrawLoom")','/UserFunctionQueryAction.do?tableName=tblProDrawLoom&src=menu&operation=6');" title="$text.get("moduleFlow.lb.tblProDrawLoom")">$text.get("moduleFlow.lb.tblProDrawLoom")</a></p>
	 	#else
		<p><img src="/style/images/flow/war10.gif" width="48" height="48" border="0"></p>
    	<p class="fc">$text.get("moduleFlow.lb.tblProDrawLoom")</p>
		#end
	</td>
  	<td align="center"><img src="/style/images/flow/flowr.gif"></td>
  	<td width="100" rowspan="1" align="center" valign="middle">
		#if($globals.hasQuery("tblProCheckLoom"))
		<p><a href="javascript:showreModule('$text.get("QuoteCheckLoom")','/UserFunctionQueryAction.do?tableName=tblProCheckLoom&src=menu&operation=6');" title="$text.get("QuoteCheckLoom")"><img src="/style/images/flow/sale1.gif" width="48" height="48" border="0"></a></p>
    	<p><a href="javascript:showreModule('$text.get("QuoteCheckLoom")','/UserFunctionQueryAction.do?tableName=tblProCheckLoom&src=menu&operation=6');" title="$text.get("QuoteCheckLoom")">$text.get("QuoteCheckLoom")</a></p>
	 	#else
		<p><img src="/style/images/flow/sale1.gif" width="48" height="48" border="0"></p>
    	<p class="fc">$text.get("QuoteCheckLoom")</p>
		#end
	</td>
  	<td rowspan="2" align="center"><img src="/style/images/flow/flowwf.gif"></td>
  	<td width="100" rowspan="1" align="center" valign="middle">
		#if($globals.hasQuery("tblProductMaterial"))
		<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.tblProductMaterial")','/UserFunctionQueryAction.do?

tableName=tblProductMaterial&src=menu&operation=6');" title="$text.get("moduleFlow.lb.tblProductMaterial")"><img src="/style/images/flow/war8.gif" width="48" height="48" 

border="0"></a></p>
    	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.tblProductMaterial")','/UserFunctionQueryAction.do?tableName=tblProductMaterial&src=menu&operation=6');" title="$text.get("moduleFlow.lb.tblProductMaterial")">$text.get("moduleFlow.lb.tblProductMaterial")</a></p>
	 	#else
		<p><img src="/style/images/flow/war8.gif" width="48" height="48" border="0"></p>
    	<p class="fc">$text.get("moduleFlow.lb.tblProductMaterial")</p>
		#end
	</td>
  </tr>
  <tr>
  	<td width="100" rowspan="1" align="center" valign="middle">
		#if($globals.hasQuery("tblProDrawDye"))
		<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.tblProDrawDye")','/UserFunctionQueryAction.do?tableName=tblProDrawDye&src=menu&operation=6');" title="$text.get("moduleFlow.lb.tblProDrawDye")"><img src="/style/images/flow/war10.gif" width="48" height="48" border="0"></a></p>
    	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.tblProDrawDye")','/UserFunctionQueryAction.do?tableName=tblProDrawDye&src=menu&operation=6');" title="$text.get("moduleFlow.lb.tblProDrawDye")">$text.get("moduleFlow.lb.tblProDrawDye")</a></p>
	 	#else
		<p><img src="/style/images/flow/war10.gif" width="48" height="48" border="0"></p>
    	<p class="fc">$text.get("moduleFlow.lb.tblProDrawDye")</p>
		#end
	</td>
  	<td align="center"><img src="/style/images/flow/flowr.gif"></td>
  	<td width="100" rowspan="1" align="center" valign="middle">
		#if($globals.hasQuery("tblProCheckDye"))
		<p><a href="javascript:showreModule('染色验收单','/UserFunctionQueryAction.do?tableName=tblProCheckDye&src=menu&operation=6');" title="染色验收单"><img 

src="/style/images/flow/sale1.gif" width="48" height="48" border="0"></a></p>
    	<p><a href="javascript:showreModule('染色验收单','/UserFunctionQueryAction.do?tableName=tblProCheckDye&src=menu&operation=6');" title="染色验收单">染色验收单</a></p>
	 	#else
		<p><img src="/style/images/flow/sale1.gif" width="48" height="48" border="0"></p>
    	<p class="fc">染色验收单</p>
		#end
	</td>
  	<td width="100" rowspan="1" align="center" valign="middle">
		#if($globals.hasQuery("tblExpensed"))
		<p><a href="javascript:showreModule('委外费用分摊单','/UserFunctionQueryAction.do?tableName=tblPrecessFee&src=menu&operation=6');" title="委外费用分摊单

"><img 

src="/style/images/flow/war9.gif" width="48" height="48" border="0"></a></p>
    	<p><a href="javascript:showreModule('委外费用分摊单','/UserFunctionQueryAction.do?tableName=tblPrecessFee&src=menu&operation=6');" title="委外费用分摊单

">委外费用分摊单

</a></p>
	 	#else
		<p><img src="/style/images/flow/war9.gif" width="48" height="48" border="0"></p>
    	<p class="fc">委外费用分摊单

</p>
		#end
	</td>
  </tr>
  <tr>
    <td align="center"></td>
  	<td width="100" rowspan="1" align="center" valign="middle">
		#if($globals.hasQuery("tblProDrawAfter"))
		<p><a href="javascript:showreModule('后整理发料单','/UserFunctionQueryAction.do?tableName=tblProDrawAfter&src=menu&operation=6');" title="后整理发料单"><img 

src="/style/images/flow/war10.gif" width="48" height="48" border="0"></a></p>
    	<p><a href="javascript:showreModule('后整理发料单','/UserFunctionQueryAction.do?tableName=tblProDrawAfter&src=menu&operation=6');" title="后整理发料单">后整理发料单

</a></p>
	 	#else
		<p><img src="/style/images/flow/war10.gif" width="48" height="48" border="0"></p>
    	<p class="fc">后整理发料单</p>
		#end
	</td>
  	<td align="center"><img src="/style/images/flow/flowr.gif"></td>
  	<td width="100" rowspan="1" align="center" valign="middle">
		#if($globals.hasQuery("tblProCheckAfter"))
		<p><a href="javascript:showreModule('后整理验收单','/UserFunctionQueryAction.do?tableName=tblProCheckAfter&src=menu&operation=6');" title="后整理验收单"><img 

src="/style/images/flow/sale1.gif" width="48" height="48" border="0"></a></p>
    	<p><a href="javascript:showreModule('后整理验收单','/UserFunctionQueryAction.do?tableName=tblProCheckAfter&src=menu&operation=6');" title="后整理验收单">后整理验收单

</a></p>
	 	#else
		<p><img src="/style/images/flow/sale1.gif" width="48" height="48" border="0"></p>
    	<p class="fc">后整理验收单</p>
		#end
	</td>
  </tr>
  <tr>
  	<td align="center" height="30"></td>
  </tr>
  <tr>
  	<td align="center"></td>
  	<td width="100" rowspan="1" align="center" valign="middle">
		#if($globals.hasQuery("tblBorrowYarn"))
		<p><a href="javascript:showreModule('借纱单','/UserFunctionQueryAction.do?tableName=tblBorrowYarn&src=menu&operation=6');" title="借纱单"><img 

src="/style/images/flow/war10.gif" width="48" height="48" border="0"></a></p>
    	<p><a href="javascript:showreModule('借纱单','/UserFunctionQueryAction.do?tableName=tblBorrowYarn&src=menu&operation=6');" title="借纱单">借纱单</a></p>
	 	#else
		<p><img src="/style/images/flow/war10.gif" width="48" height="48" border="0"></p>
    	<p class="fc">借纱单</p>
		#end
	</td>
  	<td width="100" rowspan="1" align="center" valign="middle">
		#if($globals.hasQuery("tblReturnYarn"))
		<p><a href="javascript:showreModule('借纱还回单','/UserFunctionQueryAction.do?tableName=tblReturnYarn&src=menu&operation=6');" title="借纱还回单"><img 

src="/style/images/flow/fin11.gif" width="48" height="48" border="0"></a></p>
    	<p><a href="javascript:showreModule('借纱还回单','/UserFunctionQueryAction.do?tableName=tblReturnYarn&src=menu&operation=6');" title="借纱还回单">借纱还回单</a></p>
	 	#else
		<p><img src="/style/images/flow/fin11.gif" width="48" height="48" border="0"></p>
    	<p class="fc">借纱还回单</p>
		#end
	</td>
  	<td width="100" rowspan="1" align="center" valign="middle">
		#if($globals.hasQuery("tblReturnProduce"))
		<p><a href="javascript:showreModule('委外返工单','/UserFunctionQueryAction.do?tableName=tblReturnProduce&src=menu&operation=6');" title="委外返工单"><img 

src="/style/images/flow/fin12.gif" width="48" height="48" border="0"></a></p>
    	<p><a href="javascript:showreModule('委外返工单','/UserFunctionQueryAction.do?tableName=tblReturnProduce&src=menu&operation=6');" title="委外返工单">委外返工单</a></p>
	 	#else
		<p><img src="/style/images/flow/fin12.gif" width="48" height="48" border="0"></p>
    	<p class="fc">委外返工单</p>
		#end
	</td>
  	<td width="100" rowspan="1" align="center" valign="middle">
		#if($globals.hasQuery("tblProCheck"))
		<p><a href="javascript:showreModule('委外返工验收单','/UserFunctionQueryAction.do?tableName=tblProCheck&src=menu&operation=6');" title="委外返工验收单"><img 

src="/style/images/flow/fin7.gif" width="48" height="48" border="0"></a></p>
    	<p><a href="javascript:showreModule('委外返工验收单','/UserFunctionQueryAction.do?tableName=tblProCheck&src=menu&operation=6');" title="委外返工验收单">委外返工验收单</a></p>
	 	#else
		<p><img src="/style/images/flow/fin7.gif" width="48" height="48" border="0"></p>
    	<p class="fc">委外返工验收单</p>
		#end
	</td>
  	<td width="100" rowspan="1" align="center" valign="middle">
		#if($globals.hasQuery("tblPieceGoodsInDetail"))
		<p><a href="javascript:showreModule('成品细码入库单','/UserFunctionQueryAction.do?tableName=tblPieceGoodsInDetail&WorkProcedure=CPINMG&src=menu&operation=6');" title="成品细码入库单"><img src="/style/images/flow/war7.gif" width="48" height="48" border="0"></a></p>
    	<p><a href="javascript:showreModule('成品细码入库单','/UserFunctionQueryAction.do?tableName=tblPieceGoodsInDetail&WorkProcedure=CPINMG&src=menu&operation=6');" title="成












品细码入库单">成品细码入库单</a></p>
	 	#else
		<p><img src="/style/images/flow/war7.gif" width="48" height="48" border="0"></p>
    	<p class="fc">成品细码入库单</p>
		#end
	</td>
  	<td width="120" rowspan="1" align="center" valign="middle">
		#if($globals.hasQuery("tblClearBackMaterial"))
		<p><a href="javascript:showreModule('成品细码返工单','/UserFunctionQueryAction.do?tableName=tblClearBackMaterial&WorkProcedure=RETUTN&src=menu&operation=6');" title="成品细码返工单"><img src="/style/images/flow/sale2.gif" width="48" height="48" border="0"></a></p>
    	<p><a href="javascript:showreModule('成品细码返工单','/UserFunctionQueryAction.do?tableName=tblClearBackMaterial&WorkProcedure=RETUTN&src=menu&operation=6');" title="成












品细码返工单">成品细码返工单</a></p>
	 	#else
		<p><img src="/style/images/flow/sale2.gif" width="48" height="48" border="0"></p>
    	<p class="fc">成品细码返工单</p>
		#end
	</td>
  	<td></td>
  </tr>
</table>
</div>
#elseif("仓库管理"=="$!name")
<div style="margin-top:5px;">
<table width="1125" height="470" border="0" cellpadding="0" cellspacing="0" style="width:540px">
  <tr>
    <td width="277" rowspan="2" align="center" valign="middle">
	#if($globals.hasQuery("tblGoodsSplitForm"))
	<p><a href="javascript:showreModule('布匹裁剪单','/UserFunctionQueryAction.do?tableName=tblGoodsSplitForm&src=menu&operation=6');" title="布匹裁剪单"><img 

src="/style/images/flow/war1.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('布匹裁剪单','/UserFunctionQueryAction.do?tableName=tblGoodsSplitForm&src=menu&operation=6');" title="布匹裁剪单">布匹裁剪单</a></p>
	 #else
	<p><img src="/style/images/flow/war1.gif" width="48" height="48" border="0"></p>
    <p class="fc">布匹裁剪单</p>
	#end	</td>
    <td width="5" rowspan="9" align="center" valign="middle"><img src="/style/images/flow/lines.gif" width="5" height="400"></td>
    <td width="48" rowspan="2" align="center"><p><img src="/style/images/flow/war4.gif" width="48" height="48"></p>
       <p class="fc">调拨</p></td>
    <td width="252" rowspan="2" align="center"><img src="/style/images/flow/flowwf.gif" width="60" height="100"></td>
    <td width="251" align="center" valign="middle">
	#if($globals.hasQuery("tblAllot"))
	<p><a href="javascript:showreModule('同价调拨单','/UserFunctionQueryAction.do?tableName=tblAllot&src=menu&operation=6');" title="同价调拨单"><img 

src="/style/images/flow/war8.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('同价调拨单','/UserFunctionQueryAction.do?tableName=tblAllot&src=menu&operation=6');" title="同价调拨单">同价调拨单</a></p>
	 #else
	 <p><img src="/style/images/flow/war8.gif" width="48" height="48" border="0"></p>
     <p class="fc">同价调拨单</p>
	  #end	</td>
    <td width="5" rowspan="9" align="center" valign="middle"><img src="/style/images/flow/lines.gif" width="5" height="400"></td>
    <td width="251" align="center" valign="middle"> #if($globals.hasQuery("tblGoodsDisconnect"))
      <p><a href="javascript:showreModule('布匹分卷单','/UserFunctionQueryAction.do?tableName=tblGoodsDisconnect&src=menu&operation=6');" title="布匹分卷单"><img 

src="/style/images/flow/fin2.gif" alt="布匹分卷单" width="48" height="48" border="0"></a></p>
      <p><a href="javascript:showreModule('布匹分卷单','/UserFunctionQueryAction.do?tableName=tblGoodsDisconnect&src=menu&operation=6');" title="布匹分卷单">布匹分卷单</a></p>
      #else
      <p><img src="/style/images/flow/fin2.gif" alt="布匹分卷单" width="48" height="48" border="0"></p>
      <p class="fc">布匹分卷单</p>
      #end </td>
  </tr>
  <tr>
    <td align="center">
	#if($globals.hasQuery("tblAllotChange"))
	<p><a href="javascript:showreModule('变价调拨单','/UserFunctionQueryAction.do?tableName=tblAllotChange&src=menu&operation=6');" title="变价调拨单"><img 

src="/style/images/flow/war9.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('变价调拨单','/UserFunctionQueryAction.do?tableName=tblAllotChange&src=menu&operation=6');" title="变价调拨单">变价调拨单</a></p>
	 #else
	 <p><img src="/style/images/flow/war9.gif" width="48" height="48" border="0"></p>
     <p class="fc">变价调拨单</p>
	 #end	</td>
	<td align="center"> #if($globals.hasQuery("tblGoodsConnect"))
	  <p><a href="javascript:showreModule('布匹并卷单','/UserFunctionQueryAction.do?tableName=tblGoodsConnect&src=menu&operation=6');" title="布匹并卷单"><img 

src="/style/images/flow/fin3.gif" alt="布匹并卷单" width="48" height="48" border="0"></a></p>
	  <p><a href="javascript:showreModule('布匹并卷单','/UserFunctionQueryAction.do?tableName=tblGoodsConnect&src=menu&operation=6');" title="布匹并卷单">布匹并卷单</a></p>
	  #else
	  <p><img src="/style/images/flow/fin3.gif" alt="布匹并卷单" width="48" height="48" border="0"></p>
	  <p class="fc">变价调拨单</p>
	  #end </td>
	<td width="4" align="center" valign="middle">&nbsp;</td>
	<td width="47" align="center">&nbsp;</td>
    </tr>
  <tr>
    <td height="10" align="center">
	#if($globals.hasQuery("tblOtherIn"))
	<p><a href="javascript:showreModule('其它入库单','/UserFunctionQueryAction.do?tableName=tblOtherIn&src=menu&operation=6');" title="其它入库单"><img 

src="/style/images/flow/pro6.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('其它入库单','/UserFunctionQueryAction.do?tableName=tblOtherIn&src=menu&operation=6');" title="其它入库单">其它入库单</a></p>
	 #else
	  <p><img src="/style/images/flow/pro6.gif" width="48" height="48" border="0"></p>
      <p class="fc">其它入库单</p>
	 #end	</td>
    <td width="48" align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblAdjustPrice"))
	<p><a href="javascript:showreModule('存货调价单','/UserFunctionQueryAction.do?tableName=tblAdjustPrice&src=menu&operation=6');" title="存货调价单"><img 

src="/style/images/flow/war3.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('存货调价单','/UserFunctionQueryAction.do?tableName=tblAdjustPrice&src=menu&operation=6');" title="存货调价单">存货调价单</a></p>
	 #else
	<p><img src="/style/images/flow/war3.gif" width="48" height="48" border="0"></p>
    <p class="fc">存货调价单</a></p>
	 #end	</td>
    <td align="center">#if($globals.hasQuery("tblPiecesChange"))
      <p><a href="javascript:showreModule('匹数调整单','/UserFunctionQueryAction.do?tableName=tblPiecesChange&src=menu&operation=6');" title="匹数调整单"><img 

src="/style/images/flow/fin6.gif" alt="1" width="48" height="48" border="0"></a></p>
      <p><a href="javascript:showreModule('匹数调整单','/UserFunctionQueryAction.do?tableName=tblPiecesChange&src=menu&operation=6');" title="匹数调整单">匹数调整单</a></p>
      #else
      <p><img src="/style/images/flow/fin6.gif" alt="1" width="48" height="48" border="0"></p>
      <p class="fc">匹数调整单</a></p>
      #end </td>
    <td align="center">
	#if($globals.hasQuery("tblOtherOut"))
	<p><a href="javascript:showreModule('其它出库单','/UserFunctionQueryAction.do?tableName=tblOtherOut&src=menu&operation=6');" title="其它出库单"><img 

src="/style/images/flow/fin9.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('其它出库单','/UserFunctionQueryAction.do?tableName=tblOtherOut&src=menu&operation=6');" title="其它出库单">其它出库单</a></p>
	#else
	<p><img src="/style/images/flow/fin9.gif" width="48" height="48" border="0"></p>
    <p class="fc">其它出库单</p>
	 #end	</td>
  </tr>
  <tr>
    <td height="10" align="center">&nbsp;</td>
    <td width="48" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
  </tr>
    <td align="center">
	#if($globals.hasQuery("tblCheckMore"))
	<p><a href="javascript:showreModule('报溢单','/UserFunctionQueryAction.do?tableName=tblCheckMore&src=menu&operation=6');" title="报溢单"><img 

src="/style/images/flow/war6.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('报溢单','/UserFunctionQueryAction.do?tableName=tblCheckMore&src=menu&operation=6');" title="报溢单">报溢单</a></p>
	#else
	<p><img src="/style/images/flow/war6.gif" width="48" height="48" border="0"></p>
    <p class="fc">报溢单</p>
	#end	</td>
    <td align="center"><img src="/style/images/flow/flowl.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblCheckReady"))
	<p><a href="javascript:showreModule('库存盘点表','/UserFunctionQueryAction.do?tableName=tblStockCheck&src=menu&operation=6');" title="库存盘点表"><img 

src="/style/images/flow/war7.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('库存盘点表','/UserFunctionQueryAction.do?tableName=tblStockCheck&src=menu&operation=6');" title="库存盘点表">库存盘点表</a></p>
	#else
	<p><img src="/style/images/flow/war7.gif" width="48" height="48" border="0"></p>
    <p class="fc">库存盘点表</p>
	 #end	</td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblCheck"))
	 <p><a href="javascript:showreModule('报损单','/UserFunctionQueryAction.do?tableName=tblCheck&src=menu&operation=6');" title="报损单"><img 

src="/style/images/flow/war5.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('报损单','/UserFunctionQueryAction.do?tableName=tblCheck&src=menu&operation=6');" title="报损单">报损单</a></p>
	 #else
	 <p><img src="/style/images/flow/war5.gif" width="48" height="48" border="0"></p>
     <p class="fc">报损单</p>
	 #end	</td>
  </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
</table>
</div>
#elseif("财务管理"=="$!name")
<div style=" padding-top:10px;">
<table style=" width:640px;" width="640" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="169" rowspan="5" align="right"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="110" rowspan="3" align="center">
		#if($globals.hasQuery("tblIncome"))
		<p><a href="javascript:showreModule('其它收款单','/UserFunctionQueryAction.do?tableName=tblIncome&src=menu&operation=6');" title="其它收款单"><img 

src="/style/images/flow/war9.gif" width="48" height="48" border="0"></a></p>
         <p><a href="javascript:showreModule('其它收款单','/UserFunctionQueryAction.do?tableName=tblIncome&src=menu&operation=6');" title="其它收款单">其它收款单</a></p>
		  #else
		 <p><img src="/style/images/flow/war9.gif" width="48" height="48" border="0"></p>
         <p class="fc">其它收款单</p> 
		 #end 
		        
       #if($globals.hasQuery("tblUserTransAcc"))
          <p><a href="javascript:showreModule('现金银行转账单','/UserFunctionQueryAction.do?tableName=tblUserTransAcc&src=menu&operation=6');" title="现金银行转账单"><img 

src="/style/images/flow/war.gif" width="48" height="48" border="0"></a></p>
          <p><a href="javascript:showreModule('现金银行转账单','/UserFunctionQueryAction.do?tableName=tblUserTransAcc&src=menu&operation=6');" title="现金银行转账单">现金银行转账单</a></p>
		 #else
		 <p><img src="/style/images/flow/war.gif" width="48" height="48" border="0"></p>
         <p class="fc">现金银行转账单</p>
		 #end 
	   
	   </td>
        <td width="60" align="center">
		#if($globals.hasQuery("tblPay"))
		 <p><a href="javascript:showreModule('付款单','/UserFunctionQueryAction.do?tableName=tblPay&type=xxx&src=menu&operation=6');" title="付款单"><img 

src="/style/images/flow/fin10.gif" width="48" height="48" border="0"></a></p>
         <p><a href="javascript:showreModule('付款单','/UserFunctionQueryAction.do?tableName=tblPay&type=xxx&src=menu&operation=6');" title="付款单">付款单</a></p>
		 #else
		  <p><img src="/style/images/flow/fin10.gif" width="48" height="48" border="0"></p>
          <p class="fc">付款单</p>
		 #end 
		</td>
      </tr>
      <tr>
        <td align="center">
		#if($globals.hasQuery("tblSaleReceive"))
		<p><a href="javascript:showreModule('收款单','/UserFunctionQueryAction.do?tableName=tblSaleReceive&type=xxx&src=menu&operation=6');" title="收款单"><img 

src="/style/images/flow/war8.gif" width="48" height="48" border="0"></a></p>
         <p><a href="javascript:showreModule('收款单','/UserFunctionQueryAction.do?tableName=tblSaleReceive&type=xxx&src=menu&operation=6');" title="收款单">收款单</a></p>
		   #else
		 <p><img src="/style/images/flow/war8.gif" width="48" height="48" border="0"></p>
         <p class="fc">收款单</p>
		   #end 
		</td>
      </tr>
      <tr>
        <td align="center">
		#if($globals.hasQuery("tblExpensed"))
		 <p><a href="javascript:showreModule('其它付款单','/UserFunctionQueryAction.do?tableName=tblExpensed&src=menu&operation=6');" title="其它付款单"><img 

src="/style/images/flow/fin1.gif" width="48" height="48" border="0"></a></p>
         <p><a href="javascript:showreModule('其它付款单','/UserFunctionQueryAction.do?tableName=tblExpensed&src=menu&operation=6');" title="其它付款单">其它付款单</a></p>
		 #else
		 <p><img src="/style/images/flow/fin1.gif" width="48" height="48" border="0"></p>
         <p class="fc">其它付款单</p>
		 #end 
		</td>
      </tr>
      <tr>
        <td width="110" align="center">&nbsp;</td>
        <td align="center">&nbsp;</td>
      </tr>
    </table></td>
    <td width="25" rowspan="10" align="center"><img src="/style/images/flow/linel.gif" width="24" height="400"></td>
    <td width="60" align="center">&nbsp;</td>
    <td width="80" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="80" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="25" rowspan="10" align="center"><img src="/style/images/flow/liner.gif" width="24" height="400"></td>
    <td width="80" rowspan="10" align="center" valign="middle"><table width="100%" height="100%" border="0" cellpadding="0"  style="color:#999999" cellspacing="0">
      <tr>
        <td align="center"><img src="/style/images/flow/fin22.gif" width="48" height="48" border="0"><br>
凭证过账</td>
      </tr>
      <tr>
        <td height="35" align="center"><img src="/style/images/flow/flowx.gif" width="15" height="30"></td>
      </tr>
      <tr>
        <td align="center"><img src="/style/images/flow/fin6.gif" border="0"><br>
登账</td>
      </tr>
      <tr>
        <td height="35" align="center"><img src="/style/images/flow/flowx.gif" width="15" height="30"></td>
      </tr>
      <tr>
        <td align="center"><img src="/style/images/flow/fin4.gif" width="48" height="48" border="0"><br>
结转成本</td>
      </tr>
      <tr>
        <td height="35" align="center"><img src="/style/images/flow/flowx.gif" width="15" height="30"></td>
      </tr>
      <tr>
        <td align="center"><img src="/style/images/flow/fin2.gif" width="48" height="48" border="0"><br>
结转损益</td>
      </tr>
      <tr>
        <td height="35" align="center"><img src="/style/images/flow/flowx.gif" width="15" height="30"></td>
      </tr>
      <tr>
        <td align="center"><img src="/style/images/flow/fin3.gif" width="48" height="48" border="0"><br>
期末结账</td>
      </tr>
    </table>    </td>
  </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
  </tr>
  <tr>
    <td align="center"><img src="/style/images/flow/flowr.gif" width="47" height="15"></td>
    <td align="center">
	#if($globals.hasQuery("tblAccMain"))
		<p><a href="javascript:showreModule('凭证录入','/VoucherManage.do?operation=6&isEspecial=list');" title="凭证录入"><img 

src="/style/images/flow/pro3.gif" width="48" height="48" border="0"></a></p>
		<p><a href="javascript:showreModule('凭证录入','/VoucherManage.do?operation=6&isEspecial=list');" title="凭证录入">凭证录入</a></p>
		 #else
		<p><img src="/style/images/flow/pro3.gif" width="48" height="48" border="0"></p>
		<p class="fc">凭证录入</p>
		#end
	</td>
    <td align="center"><img src="/style/images/flow/flowr.gif" width="47" height="15"></td>
    <td align="center">
	#if($globals.hasQuery("tblAccMain"))
	<p><a href="javascript:showreModule('凭证管理','/VoucherManageAction.do?operation=4');" title="凭证管理"><img 

src="/style/images/flow/fin12.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('凭证管理','/VoucherManageAction.do?operation=4');" title="凭证管理">凭证管理</a></p>
	  #else
	<p><img src="/style/images/flow/fin12.gif" width="48" height="48" border="0"></p>
    <p class="fc">凭证管理</p>
	#end
	</td>
    <td align="center"><img src="/style/images/flow/flowr2.gif" width="47" height="15"></td>
  </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td rowspan="6" align="right" valign="top" style="color:#999999"><p>&nbsp;</p>
      <p>&nbsp;</p>
      <p>&nbsp;</p>
      <p>&nbsp;</p>
      <p>自</p>
      <p>动</p>
      <p>处</p>
	  <p>理</p></td>
  </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td rowspan="2" align="center"><p><img src="/style/images/flow/fin8.gif" width="48" height="48" border="0"></p>
<p class="fc">支票</p></td>
    <td rowspan="2" align="center"><img src="/style/images/flow/flowwf.gif" width="60" height="100"></td>
    <td align="center">
	#if($globals.hasQuery("tblChequeNoBuy"))
	<p><a href="javascript:showreModule('支票采购','/UserFunctionQueryAction.do?tableName=tblChequeNoBuy&src=menu&operation=6');" title="支票采购"><img 

src="/style/images/flow/fin9.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('支票采购','/UserFunctionQueryAction.do?tableName=tblChequeNoBuy&src=menu&operation=6');" title="支票采购">支票采购</a></p>
	#else
	<p><img src="/style/images/flow/fin9.gif" width="48" height="48" border="0"></p>
    <p class="fc">支票采购</p>
	#end
	</td>
  </tr>
  <tr>
    <td rowspan="6" align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="31%" rowspan="4" align="center"><p><img src="/style/images/flow/fin7.gif" width="48" height="48" border="0" /></p>
 <p class="fc">往来转账</p></td>
        <td width="20%" rowspan="4" align="center"><img src="/style/images/flow/flow4wf.gif" width="62" height="192"></td>
        <td width="49%" align="center">
		#if($globals.hasQuery("tblTransferSale1"))
		<p><a href="javascript:showreModule('预收转应收','/UserFunctionQueryAction.do?tableName=tblTransferSale1&src=menu&operation=6');" title="预收转应收"><img 

src="/style/images/flow/fin19.gif" width="48" height="48" border="0" /></a></p>
        <p><a href="javascript:showreModule('预收转应收','/UserFunctionQueryAction.do?tableName=tblTransferSale1&src=menu&operation=6');" title="预收转应收">预收转应收</a></p>
		#else
		<p><img src="/style/images/flow/fin19.gif" width="48" height="48" border="0" /></p>
        <p class="fc">预收转应收</p>
		#end
		</td>
      </tr>
      <tr>
        <td align="center">
		#if($globals.hasQuery("tblTransferSale2"))
		<p><a href="javascript:showreModule('应付转应收','/UserFunctionQueryAction.do?tableName=tblTransferSale2&src=menu&operation=6');" title="应付转应收"><img 

src="/style/images/flow/fin18.gif" width="48" height="48" border="0" /></a></p>
        <p><a href="javascript:showreModule('应付转应收','/UserFunctionQueryAction.do?tableName=tblTransferSale2&src=menu&operation=6');" title="应付转应收">应付转应收</a></p>
		#else
		<p><img src="/style/images/flow/fin18.gif" width="48" height="48" border="0" /></p>
        <p class="fc">应付转应收</p>
		#end
		</td>
      </tr>
      <tr>
        <td align="center">
		#if($globals.hasQuery("tblTransferSale3"))
		<p><a href="javascript:showreModule('预付转应付','/UserFunctionQueryAction.do?tableName=tblTransferSale3&src=menu&operation=6');" title="预付转应付"><img 

src="/style/images/flow/fin20.gif" width="48" height="48" border="0" /></a></p>
        <p><a href="javascript:showreModule('预付转应付','/UserFunctionQueryAction.do?tableName=tblTransferSale3&src=menu&operation=6');" title="预付转应付">预付转应付</p>
		#else
		<p><img src="/style/images/flow/fin20.gif" width="48" height="48" border="0" /></p>
        <p class="fc">预付转应付</p>
		#end
		</td>
      </tr>
      <tr>
        <td align="center">
		#if($globals.hasQuery("tblTransferSale4"))
		<p><a href="javascript:showreModule('应收转应付','/UserFunctionQueryAction.do?tableName=tblTransferSale4&src=menu&operation=6');" title="应收转应付"><img 

src="/style/images/flow/fin21.gif" width="48" height="48" border="0" /></a></p>
		<p><a href="javascript:showreModule('应收转应付','/UserFunctionQueryAction.do?tableName=tblTransferSale4&src=menu&operation=6');" title="应收转应付">应收转应付












</a></p>
		#else
		<p><img src="/style/images/flow/fin21.gif" width="48" height="48" border="0" /></p>
		<p class="fc">应收转应付</p>
		#end
		</td>
      </tr>
    </table></td>
    <td align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblChequeNoOut"))
	   <p><a href="javascript:showreModule('支票领用','/UserFunctionQueryAction.do?tableName=tblChequeNoOut&src=menu&operation=6');" title="支票领用"><img 

src="/style/images/flow/fin10.gif" width="48" height="48" border="0"></a></p>
       <p><a href="javascript:showreModule('支票领用','/UserFunctionQueryAction.do?tableName=tblChequeNoOut&operation=6');" title="支票领用">支票领用</a></p>
	   #else
	   <p><img src="/style/images/flow/fin10.gif" width="48" height="48" border="0"></p>
       <p class="fc">支票领用</p>
	   #end
	</td>
  </tr>
  <tr>
    <td height="40" rowspan="2" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
  <tr>
    <td rowspan="2" align="center"><p><img src="/style/images/flow/war6.gif" width="48" height="48" border="0"></p>
 <p class="fc">往来调账单</p></td>
    <td rowspan="2" align="center"><img src="/style/images/flow/flowwf.gif" width="60" height="100"></td>
    <td align="center">
	#if($globals.hasQuery("tblPayAdjust"))
	<p><a href="javascript:showreModule('应付调账单','/UserFunctionQueryAction.do?tableName=tblPayAdjust&src=menu&operation=6');" title="应付调账单"><img 

src="/style/images/flow/pro2.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('应付调账单','/UserFunctionQueryAction.do?tableName=tblPayAdjust&src=menu&operation=6');" title="应付调账单">应付调账单</a></p>
	 #else
	<p><img src="/style/images/flow/pro2.gif" width="48" height="48" border="0"></p>
    <p class="fc">应付调账单</p>
	 #end
	</td>
  </tr>
  <tr>
    <td height="20" align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblAccAdjust"))
	<p><a href="javascript:showreModule('应收调账单','/UserFunctionQueryAction.do?tableName=tblAccAdjust&src=menu&operation=6');" title="应收调账单"><img 

src="/style/images/flow/war5.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('应收调账单','/UserFunctionQueryAction.do?tableName=tblAccAdjust&src=menu&operation=6');" title="应收调账单">应收调账单</a></p>
	 #else
	<p><img src="/style/images/flow/war5.gif" width="48" height="48" border="0"></p>
    <p class="fc">应收调账单</p>
	 #end
	</td>
    </tr>
  <tr>
    <td height="20" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
  </tr>
</table>
</div>
#end
	</div>

		
		#else
		
		#if("$!globals.getVersion()"=="8" 
			|| "$!globals.getVersion()"=="1"
			|| "$!globals.getVersion()"=="10")

		<div class="scroll_function_small_menuReport"> 
#if("28141812_0811271603291400086"=="$!keyId")
<div style="margin-top:20px;">
 <table style="width:680px" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
    <td width="90" align="center">
    
	#if($globals.hasQuery("tblBuyChange"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.buyAdjustPrice")','/UserFunctionQueryAction.do?tableName=tblBuyChange&src=menu&operation=6');" title="$text.get('moduleFlow.lb.buyAdjustPrice')"><img src="/style/images/flow/pro1.gif" width="48" height="48" border="0"></a></p>
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.buyAdjustPrice")','/UserFunctionQueryAction.do?tableName=tblBuyChange&src=menu&operation=6');" title="$text.get('moduleFlow.lb.buyAdjustPrice')">$text.get("moduleFlow.lb.buyAdjustPrice")</a></p>
	#else
	<p><img src="/style/images/flow/pro1.gif" width="48" height="48" border="0"></p>
	<p class="fc">$text.get("moduleFlow.lb.buyAdjustPrice")</p>
	#end
	</td>
    
    </tr>
  <tr>
    <td height="60" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>    
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center"><img src="/style/images/flow/flowup.gif"></td>
    </tr>
  <tr>
  	<td align="center">
  	#if($globals.hasQuery("tblBuyAskPrice"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.BuyAskPrice")','/UserFunctionQueryAction.do?tableName=tblBuyAskPrice&src=menu&operation=6');" title="$text.get('moduleFlow.lb.BuyAskPrice')"><img src="/style/images/flow/pro4.gif" width="48" height="48" border="0"></a></p>
	<p>
	<a href="javascript:showreModule('$text.get("moduleFlow.lb.BuyAskPrice")','/UserFunctionQueryAction.do?tableName=tblBuyAskPrice&src=menu&operation=6');" title="$text.get('moduleFlow.lb.BuyAskPrice')">$text.get("moduleFlow.lb.BuyAskPrice")</a></p>
#else
	<p><img src="/style/images/flow/pro4.gif" width="48" height="48" border="0"></p>
	<p class="fc">$text.get("moduleFlow.lb.BuyAskPrice")</p>
	 #end
	
	</td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblBuyApplication"))
	<p><a href="javascript:showreModule('$text.get("QuoteBuyApplication")','/UserFunctionQueryAction.do?tableName=tblBuyApplication&src=menu&src=menu&operation=6');" title="$text.get('QuoteBuyApplication')"><img src="/style/images/flow/pro4.gif" width="48" height="48" border="0"></a></p>
	<p>
	<a href="javascript:showreModule('$text.get("QuoteBuyApplication")','/UserFunctionQueryAction.do?tableName=tblBuyApplication&src=menu&src=menu&operation=6');" title="$text.get('QuoteBuyApplication')">$text.get("QuoteBuyApplication")</a></p>
#else
	<p><img src="/style/images/flow/pro4.gif" width="48" height="48" border="0"></p>
	<p class="fc">$text.get("QuoteBuyApplication")</p>
	 #end
	 	
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblBuyOrder"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.Order")','/UserFunctionQueryAction.do?tableName=tblBuyOrder&src=menu&src=menu&operation=6');" title="$text.get('moduleFlow.lb.Order')"><img src="/style/images/flow/pro5.gif" width="48" height="48" border="0"></a></p>
	 <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.Order")','/UserFunctionQueryAction.do?tableName=tblBuyOrder&src=menu&src=menu&operation=6');" title="$text.get('moduleFlow.lb.Order')">$text.get("moduleFlow.lb.Order")</a></p>
	#else
	<p><img src="/style/images/flow/pro5.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.Order")</p>
	 #end 
	  </td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblBuyInStock"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OrderList")','/UserFunctionQueryAction.do?tableName=tblBuyInStock&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OrderList')"><img src="/style/images/flow/pro6.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OrderList")','/UserFunctionQueryAction.do?tableName=tblBuyInStock&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OrderList')">$text.get("moduleFlow.lb.OrderList")</a></p>
	  #else
	 <p><img src="/style/images/flow/pro6.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.OrderList")</p>
	   #end
	  </td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center" width="80">
	#if($globals.hasQuery("tblPay"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OrderPayList")','/UserFunctionQueryAction.do?tableName=tblPay&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OrderPayList')"><img src="/style/images/flow/pro2.gif" width="48" height="48" border="0"></a></p>
      <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OrderPayList")','/UserFunctionQueryAction.do?tableName=tblPay&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OrderPayList')">$text.get("moduleFlow.lb.OrderPayList")</a></p>
	   #else
	 <p><img src="/style/images/flow/pro2.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.OrderPayList")</p>
	  #end
	  </td>
	   <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center" width="100">
	#if($globals.hasQuery("tblBuyInvoiceInfo"))
	<p><a href="javascript:showreModule('采购开票管理','/UserFunctionQueryAction.do?tableName=tblBuyInvoiceInfo&src=menu&operation=6');" title="采购开票管理"><img src="/style/images/flow/pro2.gif" width="48" height="48" border="0"></a></p>
      <p><a href="javascript:showreModule('采购开票管理','/UserFunctionQueryAction.do?tableName=tblBuyInvoiceInfo&src=menu&operation=6');" title="采购开票管理">采购开票管理</a></p>
	   #else
	 <p><img src="/style/images/flow/pro2.gif" width="48" height="48" border="0"></p>
      <p class="fc">采购开票管理</p>
	  #end
	  </td>
    </tr>
   
  <tr>
    <td height="60" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>    
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center"><img src="/style/images/flow/flowdown.gif"></td>
    </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblBuyOutStock"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OrderTuiList")	','/UserFunctionQueryAction.do?tableName=tblBuyOutStock&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OrderTuiList')	"><img src="/style/images/flow/pro3.gif" width="48" height="48" border="0"></a></p>
      <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OrderTuiList")	','/UserFunctionQueryAction.do?tableName=tblBuyOutStock&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OrderTuiList')	">$text.get("moduleFlow.lb.OrderTuiList")	</a></p>
	   #else
	  <p><img src="/style/images/flow/pro3.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.OrderTuiList")	</p>
	   #end
	  </td>
    
    </tr>
	
	 <tr>
    <td height="60" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>   
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
     <td align="center"><img src="/style/images/flow/flowdown.gif"></td>
    </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblBuyReplace"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.BuyReplace")','/UserFunctionQueryAction.do?tableName=tblBuyReplace&src=menu&operation=6');" title="$text.get('moduleFlow.lb.BuyReplace')"><img src="/style/images/flow/pro3.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.BuyReplace")','/UserFunctionQueryAction.do?tableName=tblBuyReplace&src=menu&operation=6');" title="$text.get('moduleFlow.lb.BuyReplace')">$text.get("moduleFlow.lb.BuyReplace")</a></p>
	   #else
	  <p><img src="/style/images/flow/fin1.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.BuyReplace")</p>
	   #end
	  </td>
    
    </tr>
</table>
</div>
#elseif("28141812_0811271603017810084"=="$!keyId")
<div style="margin-top:20px;">
<table style="width:540px" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">
	#if($globals.hasQuery("tblSalesChange"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesChange")','/UserFunctionQueryAction.do?tableName=tblSalesChange&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesChange')"><img src="/style/images/flow/sale1.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesChange")','/UserFunctionQueryAction.do?tableName=tblSalesChange&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesChange')">$text.get("moduleFlow.lb.SalesChange")</a></p>
	  #else
	 <p><img src="/style/images/flow/sale1.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.SalesChange")</p>
	  #end
	  </td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
    </tr>
  <tr>
    <td height="60" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center"><img src="/style/images/flow/flowup.gif"></td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
  <tr>
    <td align="center">
	<p><img src="/style/images/flow/sale5.gif" width="48" height="48" border="0"></p>
	<p class="fc">$text.get("moduleFlow.lb.CRMsalesQuot")(CRM)</p></td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblSalesOrder"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesOrder")','/UserFunctionQueryAction.do?tableName=tblSalesOrder&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesOrder')"><img src="/style/images/flow/sale4.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesOrder")','/UserFunctionQueryAction.do?tableName=tblSalesOrder&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesOrder')">$text.get("moduleFlow.lb.SalesOrder")</a></p>
	 #else
	 <p><img src="/style/images/flow/sale4.gif" width="48" height="48" border="0"></p>
     <p class="fc">$text.get("moduleFlow.lb.SalesOrder")</p>
	 #end
	 </td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblSalesOutStock"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesOutStock")','/UserFunctionQueryAction.do?tableName=tblSalesOutStock&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesOutStock')"><img src="/style/images/flow/sale6.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesOutStock")','/UserFunctionQueryAction.do?tableName=tblSalesOutStock&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesOutStock')">$text.get("moduleFlow.lb.SalesOutStock")</a></p>
	#else
	<p><img src="/style/images/flow/sale6.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.SalesOutStock")</p>
	#end
	</td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblSaleReceive"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SaleReceive")','/UserFunctionQueryAction.do?tableName=tblSaleReceive&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SaleReceive')"><img src="/style/images/flow/sale2.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SaleReceive")','/UserFunctionQueryAction.do?tableName=tblSaleReceive&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SaleReceive')">$text.get("moduleFlow.lb.SaleReceive")</a></p>
	#else
	<p><img src="/style/images/flow/sale2.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.SaleReceive")</p>
	#end
	</td>
    </tr>
  <tr>
    <td height="60" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center"><img src="/style/images/flow/flowdown.gif"></td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
    #if($globals.hasQuery("/ReatailSingleAction.do?type=login","query"))
	<p><a href="javascript:showreModule('$text.get("common.lb.retailSingle")','/ReatailSingleAction.do?type=login')" title="$text.get('common.lb.retailSingle')"><img 

src="/style/images/flow/fin4.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("common.lb.retailSingle")','/ReatailSingleAction.do?type=login')" title="$text.get('common.lb.retailSingle')">$text.get("common.lb.retailSingle")</a></p>
	 #else
	 <p><img src="/style/images/flow/fin4.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("common.lb.retailSingle")</p>
	 #end</td>
    </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblSalesReturnStock"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesReturnStock")','/UserFunctionQueryAction.do?tableName=tblSalesReturnStock&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesReturnStock')"><img src="/style/images/flow/sale3.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesReturnStock")','/UserFunctionQueryAction.do?tableName=tblSalesReturnStock&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesReturnStock')">$text.get("moduleFlow.lb.SalesReturnStock")</a></p>
	 #else
	 <p><img src="/style/images/flow/sale3.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.SalesReturnStock")</p>
	 #end
	 </td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
	
	 <tr>
    <td height="60" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center"><img src="/style/images/flow/flowdown.gif"></td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblSalesReplace"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesReplace")','/UserFunctionQueryAction.do?tableName=tblSalesReplace&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesReplace')"><img src="/style/images/flow/pro3.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesReplace")','/UserFunctionQueryAction.do?tableName=tblSalesReplace&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesReplace')">$text.get("moduleFlow.lb.SalesReplace")</a></p>
	   #else
	  <p><img src="/style/images/flow/war5.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.SalesReplace")</p>
	   #end
	  </td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
	
</table>
</div>
#elseif("fca2035a_0910281030268750005"=="$!keyId")
<div style="margin-top:5px;">
<table height="470" border="0" cellpadding="0" cellspacing="0" style="width:540px">
  <tr>
    <td width="100" rowspan="2" align="center" valign="bottom">&nbsp;</td>
    <td width="10" rowspan="9" align="center" valign="middle"><img src="/style/images/flow/lines.gif" width="5" height="400"></td>
    <td width="100" rowspan="2" align="center"><p><img src="/style/images/flow/war4.gif" width="48" height="48"></p>
       <p class="fc">$text.get("moduleFlow.lb.Allot")</p></td>
    <td width="120" rowspan="2" align="center"><img src="/style/images/flow/flowwf.gif" width="60" height="100"></td>
    <td width="100" align="center" valign="middle">
	#if($globals.hasQuery("tblAllot"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.sameAllot")','/UserFunctionQueryAction.do?tableName=tblAllot&src=menu&operation=6');" title="$text.get('moduleFlow.lb.sameAllot')"><img src="/style/images/flow/war8.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.sameAllot")','/UserFunctionQueryAction.do?tableName=tblAllot&src=menu&operation=6');" title="$text.get('moduleFlow.lb.sameAllot')">$text.get("moduleFlow.lb.sameAllot")</a></p>
	 #else
	 <p><img src="/style/images/flow/war8.gif" width="48" height="48" border="0"></p>
     <p class="fc">$text.get("moduleFlow.lb.sameAllot")</p>
	  #end
	 </td>
    <td width="9" rowspan="9" align="center" valign="middle"><img src="/style/images/flow/lines.gif" width="5" height="400"></td>
    <td width="100" rowspan="2" align="center" valign="bottom"><table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td align="center" valign="bottom">
		#if($globals.hasQuery("tblAttributeAdjust"))
          <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.AttributeAdjust")','/UserFunctionQueryAction.do?tableName=tblAttributeAdjust&src=menu&operation=6');" title="$text.get('moduleFlow.lb.AttributeAdjust')"><img src="/style/images/flow/fin9.gif" width="48" height="48" border="0"></a></p>
          <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.AttributeAdjust")','/UserFunctionQueryAction.do?tableName=tblAttributeAdjust&src=menu&operation=6');" title="$text.get('moduleFlow.lb.AttributeAdjust')">$text.get("moduleFlow.lb.AttributeAdjust")</a></p>
          #else
          <p><img src="/style/images/flow/fin10.gif" width="48" height="48" border="0"></p>
          <p class="fc">$text.get("moduleFlow.lb.AttributeAdjust")</p>
          #end </td>
      </tr>
      <tr>
        <td align="center"><img src="/style/images/flow/flowup.gif"></td>
      </tr>
    </table></td>
    </tr>
  <tr>
    <td align="center">
	#if($globals.hasQuery("tblAllotChange"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.AllotChange")','/UserFunctionQueryAction.do?tableName=tblAllotChange&src=menu&operation=6');" title="$text.get('moduleFlow.lb.AllotChange')"><img src="/style/images/flow/war9.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.AllotChange")','/UserFunctionQueryAction.do?tableName=tblAllotChange&src=menu&operation=6');" title="$text.get('moduleFlow.lb.AllotChange')">$text.get("moduleFlow.lb.AllotChange")</a></p>
	 #else
	 <p><img src="/style/images/flow/war9.gif" width="48" height="48" border="0"></p>
     <p class="fc">$text.get("moduleFlow.lb.AllotChange")</p>
	 #end
	 </td>
    </tr>
  <tr>
    <td height="10" align="center">
	#if($globals.hasQuery("tblOtherIn"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OtherIn")','/UserFunctionQueryAction.do?tableName=tblOtherIn&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OtherIn')"><img src="/style/images/flow/pro6.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OtherIn")','/UserFunctionQueryAction.do?tableName=tblOtherIn&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OtherIn')">$text.get("moduleFlow.lb.OtherIn")</a></p>
	 #else
	  <p><img src="/style/images/flow/pro6.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.OtherIn")</p>
	 #end
	  </td>
    <td width="0" align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblAdjustPrice"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.AdjustPrice")','/UserFunctionQueryAction.do?tableName=tblAdjustPrice&src=menu&operation=6');" title="$text.get('moduleFlow.lb.AdjustPrice')"><img src="/style/images/flow/war3.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.AdjustPrice")','/UserFunctionQueryAction.do?tableName=tblAdjustPrice&src=menu&operation=6');" title="$text.get('moduleFlow.lb.AdjustPrice')">$text.get("moduleFlow.lb.AdjustPrice")</a></p>
	 #else
	<p><img src="/style/images/flow/war3.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.AdjustPrice")</p>
	 #end
	  
	  </td>
    <td align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblOtherOut"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OtherOut")','/UserFunctionQueryAction.do?tableName=tblOtherOut&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OtherOut')"><img src="/style/images/flow/fin9.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OtherOut")','/UserFunctionQueryAction.do?tableName=tblOtherOut&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OtherOut')">$text.get("moduleFlow.lb.OtherOut")</a></p>
	#else
	<p><img src="/style/images/flow/fin9.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.OtherOut")</p>
	 #end
	
	 </td>
  </tr>
  <tr>
    <td height="10" align="center">&nbsp;</td>
    <td width="0" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
  </tr>
  <tr>
    <td align="center">
	#if($globals.hasQuery("tblGoodsAssembly"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.GoodsAssembly")','/UserFunctionQueryAction.do?tableName=tblGoodsAssembly&src=menu&operation=6');" title="$text.get('moduleFlow.lb.GoodsAssembly')"><img src="/style/images/flow/war1.gif" width="48" height="48"></a></p>
	<a href="javascript:showreModule('$text.get("moduleFlow.lb.GoodsAssembly")','/UserFunctionQueryAction.do?tableName=tblGoodsAssembly&src=menu&operation=6');" title="$text.get('moduleFlow.lb.GoodsAssembly')">$text.get("moduleFlow.lb.GoodsAssembly")</a></p>
	#else
	<p><img src="/style/images/flow/war1.gif" width="48" height="48"></p>
	<p class="fc">$text.get("moduleFlow.lb.GoodsAssembly")</p>
	#end
	</td>
    <td align="center"><img src="/style/images/flow/flowl.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblGoodsAssemblySplit"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.GoodsAssemblySplit")','/UserFunctionQueryAction.do?tableName=tblGoodsAssemblySplit&src=menu&operation=6');" title="$text.get('moduleFlow.lb.GoodsAssemblySplit')"><img src="/style/images/flow/war2.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.GoodsAssemblySplit")','/UserFunctionQueryAction.do?tableName=tblGoodsAssemblySplit&src=menu&operation=6');" title="$text.get('moduleFlow.lb.GoodsAssemblySplit')">$text.get("moduleFlow.lb.GoodsAssemblySplit")</a></p>
	#else
	<p><img src="/style/images/flow/war2.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.GoodsAssemblySplit")</p>
	#end
	  </td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblGoodsSplitForm"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.GoodsSplitForm")','/UserFunctionQueryAction.do?tableName=tblGoodsSplitForm&src=menu&operation=6');" title="$text.get('moduleFlow.lb.GoodsSplitForm')"><img src="/style/images/flow/war10.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.GoodsSplitForm")','/UserFunctionQueryAction.do?tableName=tblGoodsSplitForm&src=menu&operation=6');" title="$text.get('moduleFlow.lb.GoodsSplitForm')">$text.get("moduleFlow.lb.GoodsSplitForm")</p>
	#else
	<p><img src="/style/images/flow/war10.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.GoodsSplitForm")</p>
	#end
	  </td>
    </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
  <tr>
    <td align="center">
	#if($globals.hasQuery("tblCheckMore"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.CheckMore")','/UserFunctionQueryAction.do?tableName=tblCheckMore&src=menu&operation=6');" title="$text.get('moduleFlow.lb.CheckMore')"><img src="/style/images/flow/war6.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.CheckMore")','/UserFunctionQueryAction.do?tableName=tblCheckMore&src=menu&operation=6');" title="$text.get('moduleFlow.lb.CheckMore')">$text.get("moduleFlow.lb.CheckMore")</a></p>
	#else
	<p><img src="/style/images/flow/war6.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.CheckMore")</p>
	#end
	  </td>
    <td align="center"><img src="/style/images/flow/flowl.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblCheckReady"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.kucunpandiandan")','/UserFunctionQueryAction.do?tableName=tblCheckReady&src=menu&operation=6');" title="$text.get('moduleFlow.lb.kucunpandiandan')"><img src="/style/images/flow/war7.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.kucunpandiandan")','/UserFunctionQueryAction.do?tableName=tblCheckReady&src=menu&operation=6');" title="$text.get('moduleFlow.lb.kucunpandiandan')">$text.get("moduleFlow.lb.kucunpandiandan")</a></p>
	#else
	<p><img src="/style/images/flow/war7.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.kucunpandiandan")</p>
	 #end
	  </td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblCheck"))
	 <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.Check")','/UserFunctionQueryAction.do?tableName=tblCheck&src=menu&operation=6');" title="$text.get('moduleFlow.lb.Check')"><img src="/style/images/flow/war5.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.Check")','/UserFunctionQueryAction.do?tableName=tblCheck&src=menu&operation=6');" title="$text.get('moduleFlow.lb.Check')">$text.get("moduleFlow.lb.Check")</a></p>
	 #else
	 <p><img src="/style/images/flow/war5.gif" width="48" height="48" border="0"></p>
     <p class="fc">$text.get("moduleFlow.lb.Check")</p>
	 #end 
	 </td>
  </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
</table>
</div>

#elseif("fca2035a_0910281034230930020"=="$!keyId")
<div style=" padding-top:10px;">
<table style=" width:640px;" width="640" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="169" rowspan="5" align="right"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="110" rowspan="3" align="center">
		#if($globals.hasQuery("tblIncome"))
		<p><a href="javascript:showreModule('$text.get("modulFlow.lb.Income")','/UserFunctionQueryAction.do?tableName=tblIncome&src=menu&operation=6');" title="$text.get('modulFlow.lb.Income')"><img src="/style/images/flow/war9.gif" width="48" height="48" border="0"></a></p>
         <p class="fc"><a href="javascript:showreModule('$text.get("modulFlow.lb.Income")','/UserFunctionQueryAction.do?tableName=tblIncome&src=menu&operation=6');" title="$text.get('modulFlow.lb.Income')">$text.get("modulFlow.lb.Income")</a></p>
		  #else
		 <p><img src="/style/images/flow/war9.gif" width="48" height="48" border="0"></p>
         <p  class="fc">$text.get("modulFlow.lb.Income")</p> 
		 #end 
      	
		#if($globals.hasQuery("tblUserTransAcc"))
          <p><a href="javascript:showreModule('$text.get("modulFlow.lb.UserTransAcc")','/UserFunctionQueryAction.do?tableName=tblUserTransAcc&src=menu&operation=6');" title="$text.get('modulFlow.lb.UserTransAcc')"><img src="/style/images/flow/war.gif" width="48" height="48" border="0"></a></p>
          <p><a href="javascript:showreModule('$text.get("modulFlow.lb.UserTransAcc")','/UserFunctionQueryAction.do?tableName=tblUserTransAcc&src=menu&operation=6');" title="$text.get('modulFlow.lb.UserTransAcc')">$text.get("modulFlow.lb.UserTransAcc")</a></p>
		 #else
		 <p><img src="/style/images/flow/war.gif" width="48" height="48" border="0"></p>
         <p class="fc">$text.get("modulFlow.lb.UserTransAcc")</p>
		 #end 
		  </td>
        <td width="60" align="center">
		#if($globals.hasQuery("tblPay"))
		 <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.Pay")','/UserFunctionQueryAction.do?tableName=tblPay&type=xxx&src=menu&operation=6');" title="$text.get('moduleFlow.lb.Pay')"><img src="/style/images/flow/fin10.gif" width="48" height="48" border="0"></a></p>
         <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.Pay")','/UserFunctionQueryAction.do?tableName=tblPay&type=xxx&src=menu&operation=6');" title="$text.get('moduleFlow.lb.Pay')">$text.get("moduleFlow.lb.Pay")</a></p>
		 #else
		  <p><img src="/style/images/flow/fin10.gif" width="48" height="48" border="0"></p>
          <p class="fc">$text.get("moduleFlow.lb.Pay")</p>
		 #end 
		  </td>
      </tr>
      <tr>
        <td align="center">
		#if($globals.hasQuery("tblSaleReceive"))
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.receive")','/UserFunctionQueryAction.do?tableName=tblSaleReceive&type=xxx&src=menu&operation=6');" title="$text.get('muduleFlow.lb.receive')"><img src="/style/images/flow/war8.gif" width="48" height="48" border="0"></a></p>
         <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.receive")','/UserFunctionQueryAction.do?tableName=tblSaleReceive&type=xxx&src=menu&operation=6');" title="$text.get('muduleFlow.lb.receive')">$text.get("muduleFlow.lb.receive")</a></p>
		   #else
		 <p><img src="/style/images/flow/war8.gif" width="48" height="48" border="0"></p>
         <p class="fc">$text.get("muduleFlow.lb.receive")</p>
		   #end 
		</td>
      </tr>
      <tr>
        <td align="center">
		#if($globals.hasQuery("tblExpensed"))
		 <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.ChargeBill")','/UserFunctionQueryAction.do?tableName=tblExpensed&src=menu&operation=6');" title="$text.get('muduleFlow.lb.ChargeBill')"><img src="/style/images/flow/fin1.gif" width="48" height="48" border="0"></a></p>
         <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.ChargeBill")','/UserFunctionQueryAction.do?tableName=tblExpensed&src=menu&operation=6');" title="$text.get('muduleFlow.lb.ChargeBill')">$text.get("muduleFlow.lb.ChargeBill")</a></p>
		 #else
		 <p><img src="/style/images/flow/fin1.gif" width="48" height="48" border="0"></p>
         <p class="fc">$text.get("muduleFlow.lb.ChargeBill")</p>
		 #end 
		 </td>
      </tr>
      <tr>
        <td width="110" align="center">&nbsp;</td>
        <td align="center">&nbsp;</td>
      </tr>
    </table></td>
    <td width="25" rowspan="10" align="center"><img src="/style/images/flow/linel.gif" width="24" height="400"></td>
    <td width="60" align="center">&nbsp;</td>
    <td width="80" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="80" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="25" rowspan="10" align="center"><img src="/style/images/flow/liner.gif" width="24" height="400"></td>
    <td width="80" rowspan="10" align="center" valign="middle"><table width="100%" height="100%" border="0" cellpadding="0"  style="color:#999999" cellspacing="0">
      <tr>
        <td align="center"><img src="/style/images/flow/fin22.gif" width="48" height="48" border="0"><br>$text.get("muduleFlow.lb.voucher")</td>
      </tr>
      <tr>
        <td height="35" align="center"><img src="/style/images/flow/flowx.gif" width="15" height="30"></td>
      </tr>
      <tr>
        <td align="center"><img src="/style/images/flow/fin6.gif" border="0"><br>$text.get("muduleFlow.lb.LoginPay")</td>
      </tr>
      <tr>
        <td height="35" align="center"><img src="/style/images/flow/flowx.gif" width="15" height="30"></td>
      </tr>
      <tr>
        <td align="center"><img src="/style/images/flow/fin4.gif" width="48" height="48" border="0"><br>$text.get("muduleFlow.lb.costLast")</td>
      </tr>
      <tr>
        <td height="35" align="center"><img src="/style/images/flow/flowx.gif" width="15" height="30"></td>
      </tr>
      <tr>
        <td align="center"><img src="/style/images/flow/fin2.gif" width="48" height="48" border="0"><br>$text.get("muduleFlow.lb.tocost")</td>
      </tr>
      <tr>
        <td height="35" align="center"><img src="/style/images/flow/flowx.gif" width="15" height="30"></td>
      </tr>
      <tr>
        <td align="center"><img src="/style/images/flow/fin3.gif" width="48" height="48" border="0"><br>$text.get("muduleFlow.lb.finallcost")</td>
      </tr>
    </table>    </td>
  </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
  </tr>
  <tr>
    <td align="center"><img src="/style/images/flow/flowr.gif" width="47" height="15"></td>
    <td align="center">
	#if($globals.hasQuery("tblAccMain"))
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.voucherlogin")','/VoucherManage.do?operation=6&isEspecial=list');" title="$text.get('muduleFlow.lb.voucherlogin')"><img src="/style/images/flow/pro3.gif" width="48" height="48" border="0"></a></p>
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.voucherlogin")','/VoucherManage.do?operation=6&isEspecial=list');" title="$text.get('muduleFlow.lb.voucherlogin')">$text.get("muduleFlow.lb.voucherlogin")</a></p>
		 #else
		<p><img src="/style/images/flow/pro3.gif" width="48" height="48" border="0"></p>
		<p class="fc">$text.get("muduleFlow.lb.voucherlogin")</p>
		#end
	  </td>
    <td align="center"><img src="/style/images/flow/flowr.gif" width="47" height="15"></td>
    <td align="center">
	#if($globals.hasQuery("tblAccMain"))
	<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.vouchermanage")','/VoucherManageAction.do?operation=4');" title="$text.get('muduleFlow.lb.vouchermanage')"><img src="/style/images/flow/fin12.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.vouchermanage")','/VoucherManageAction.do?operation=4');" title="$text.get('muduleFlow.lb.vouchermanage')">$text.get("muduleFlow.lb.vouchermanage")</a></p>
	  #else
	<p><img src="/style/images/flow/fin12.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("muduleFlow.lb.vouchermanage")</p>
	#end
	  </td>
    <td align="center"><img src="/style/images/flow/flowr2.gif" width="47" height="15"></td>
  </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td rowspan="6" align="right" valign="top" style="color:#999999"><p>&nbsp;</p>
      <p>&nbsp;</p>
      <p>&nbsp;</p>
      <p>&nbsp;</p>
      <p>$text.get("muduleFlow.lb.autorun1")</p>
      <p>$text.get("muduleFlow.lb.autorun2")</p>
      <p>$text.get("muduleFlow.lb.autorun3")</p>
	  <p>$text.get("muduleFlow.lb.autorun4")</p>
	</td>
  </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td rowspan="2" align="center"><p><img src="/style/images/flow/fin8.gif" width="48" height="48" border="0"></p>
<p class="fc">$text.get("muduleFlow.lb.ChequeNo")</p></td>
    <td rowspan="2" align="center"><img src="/style/images/flow/flowwf.gif" width="60" height="100"></td>
    <td align="center">
	#if($globals.hasQuery("tblChequeNoBuy"))
	<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.ChequeNobuy")','/UserFunctionQueryAction.do?tableName=tblChequeNoBuy&src=menu&operation=6');" title="$text.get('muduleFlow.lb.ChequeNobuy')"><img src="/style/images/flow/fin9.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.ChequeNobuy")','/UserFunctionQueryAction.do?tableName=tblChequeNoBuy&src=menu&operation=6');" title="$text.get('muduleFlow.lb.ChequeNobuy')">$text.get("muduleFlow.lb.ChequeNobuy")</a></p>
	#else
	<p><img src="/style/images/flow/fin9.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("muduleFlow.lb.ChequeNobuy")</p>
	#end
	 </td>
  </tr>
  <tr>
    <td rowspan="6" align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="31%" rowspan="4" align="center"><p><img src="/style/images/flow/fin7.gif" width="48" height="48" border="0" /></p>
 <p class="fc">$text.get("muduleFlow.lb.comegochange")</p></td>
        <td width="20%" rowspan="4" align="center"><img src="/style/images/flow/flow4wf.gif" width="62" height="192"></td>
        <td width="49%" align="center">
		#if($globals.hasQuery("tblTransferSale1"))
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferSale1")','/UserFunctionQueryAction.do?tableName=tblTransferSale1&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferSale1')"><img src="/style/images/flow/fin19.gif" width="48" height="48" border="0" /></a></p>
        <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferSale1")','/UserFunctionQueryAction.do?tableName=tblTransferSale1&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferSale1')">$text.get("muduleFlow.lb.TransferSale1")</a></p>
		#else
		<p><img src="/style/images/flow/fin19.gif" width="48" height="48" border="0" /></p>
        <p class="fc">$text.get("muduleFlow.lb.TransferSale1")</p>
		#end
		  </td>
      </tr>
      <tr>
        <td align="center">
		#if($globals.hasQuery("tblTransferSale2"))
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferSale2")','/UserFunctionQueryAction.do?tableName=tblTransferSale2&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferSale2')"><img src="/style/images/flow/fin18.gif" width="48" height="48" border="0" /></a></p>
        <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferSale2")','/UserFunctionQueryAction.do?tableName=tblTransferSale2&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferSale2')">$text.get("muduleFlow.lb.TransferSale2")</a></p>
		#else
		<p><img src="/style/images/flow/fin18.gif" width="48" height="48" border="0" /></p>
        <p class="fc">$text.get("muduleFlow.lb.TransferSale2")</p>
		#end
		 </td>
      </tr>
      <tr>
        <td align="center">
		#if($globals.hasQuery("tblTransferSale3"))
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferSale3")','/UserFunctionQueryAction.do?tableName=tblTransferSale3&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferSale3')"><img src="/style/images/flow/fin20.gif" width="48" height="48" border="0" /></a></p>
        <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferSale3")','/UserFunctionQueryAction.do?tableName=tblTransferSale3&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferSale3')">$text.get("muduleFlow.lb.TransferSale3")</p>
		#else
		<p><img src="/style/images/flow/fin20.gif" width="48" height="48" border="0" /></p>
        <p class="fc">$text.get("muduleFlow.lb.TransferSale3")</p>
		#end
		</td>
      </tr>
      <tr>
        <td align="center">
		#if($globals.hasQuery("tblTransferSale4"))
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferSale4")','/UserFunctionQueryAction.do?tableName=tblTransferSale4&src=menu&operation=6');" title="应收转应付"><img src="/style/images/flow/fin21.gif" width="48" height="48" border="0" /></a></p>
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferSale4")','/UserFunctionQueryAction.do?tableName=tblTransferSale4&src=menu&operation=6');" title="应收转应付">$text.get("muduleFlow.lb.TransferSale4")</a></p>
		#else
		<p><img src="/style/images/flow/fin21.gif" width="48" height="48" border="0" /></p>
		<p class="fc">$text.get("muduleFlow.lb.TransferSale4")</p>
		#end
		</td>
      </tr>
    </table></td>
    <td align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblChequeNoOut"))
	   <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.ChequeNoOut")','/UserFunctionQueryAction.do?tableName=tblChequeNoOut&src=menu&operation=6');" title="$text.get('muduleFlow.lb.ChequeNoOut')"><img src="/style/images/flow/fin10.gif" width="48" height="48" border="0"></a></p>
       <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.ChequeNoOut")','/UserFunctionQueryAction.do?tableName=tblChequeNoOut&operation=6');" title="$text.get('muduleFlow.lb.ChequeNoOut')">$text.get("muduleFlow.lb.ChequeNoOut")</a></p>
	   #else
	   <p><img src="/style/images/flow/fin10.gif" width="48" height="48" border="0"></p>
       <p class="fc">$text.get("muduleFlow.lb.ChequeNoOut")</p>
	   #end
	  </td>
  </tr>
  <tr>
    <td height="40" rowspan="2" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
  <tr>
    <td rowspan="2" align="center"><p><img src="/style/images/flow/war6.gif" width="48" height="48" border="0"></p>
 <p class="fc">$text.get("muduleFlow.lb.ComeGoChargebill")</p></td>
    <td rowspan="2" align="center"><img src="/style/images/flow/flowwf.gif" width="60" height="100"></td>
    <td align="center">
	#if($globals.hasQuery("tblPayAdjust"))
	<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferChBill")','/UserFunctionQueryAction.do?tableName=tblPayAdjust&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferChBill')"><img src="/style/images/flow/pro2.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferChBill")','/UserFunctionQueryAction.do?tableName=tblPayAdjust&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferChBill')">$text.get("muduleFlow.lb.TransferChBill")</a></p>
	 #else
	<p><img src="/style/images/flow/pro2.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("muduleFlow.lb.TransferChBill")</p>
	 #end
	</td>
  </tr>
  <tr>
    <td height="20" align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblAccAdjust"))
	<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferChBill1")','/UserFunctionQueryAction.do?tableName=tblAccAdjust&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferChBill1')"><img src="/style/images/flow/war5.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferChBill1")','/UserFunctionQueryAction.do?tableName=tblAccAdjust&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferChBill1')">$text.get("muduleFlow.lb.TransferChBill1")</a></p>
	 #else
	<p><img src="/style/images/flow/war5.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("muduleFlow.lb.TransferChBill1")</p>
	 #end
	</td>
    </tr>
  <tr>
    <td height="20" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
  </tr>
</table>
</div>
#elseif("61f637b7_0912091304564680013"=="$!keyId")
	<div style=" padding-top:10px;">
<table width="680" border="0" cellpadding="0" cellspacing="0" style="width:680px">
  <tr>
    <td width="101" align="center">&nbsp;</td>
    <td width="35" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td width="47" align="center">&nbsp;</td>
    <td colspan="2" align="center">
		<p>&nbsp;</p>     </td>
    <td align="center">&nbsp;</td>
    <td width="75" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td width="101" align="center">&nbsp;</td>
    </tr>
  
  <tr>
    <td height="217" align="center">

	  #if($globals.hasQuery("tblBuyOutStock"))
	   <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.produceplan")','/Mrp.do?method=orderSel&winCurIndex=3');" title="$text.get('muduleFlow.lb.produceplan')"><img src="/style/images/flow/sale1.gif" width="48" height="48" border="0"></a></p>	  
	  <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.produceplan")','/Mrp.do?method=orderSel&winCurIndex=3');" title="$text.get('muduleFlow.lb.produceplan')">$text.get("muduleFlow.lb.produceplan")</a></p>
	   #else
	  	<p><img src="/style/images/flow/sale1.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("muduleFlow.lb.produceplan")</p>
	   #end
	   
	  #if($globals.hasQuery("tblSalesOrder"))
      <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesOrder")','/UserFunctionQueryAction.do?tableName=tblSalesOrder&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesOrder')"><img src="/style/images/flow/sale6.gif" width="48" height="48" border="0"></a></p>	  
      <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesOrder")','/UserFunctionQueryAction.do?tableName=tblSalesOrder&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesOrder')">$text.get("moduleFlow.lb.SalesOrder")</a></p>
	  #else
	   <p><img src="/style/images/flow/sale6.gif" width="48" height="48" border="0"></p>	  
       <p class="fc">$text.get("moduleFlow.lb.SalesOrder")</p>
	   #end
	   
	    #if($globals.hasQuery("tblPlan"))
	  <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.produceplanbill")','/UserFunctionQueryAction.do?tableName=tblPlan&parentCode=&operation=6');"title="$text.get('muduleFlow.lb.produceplanbill')"><img src="/style/images/flow/pro6.gif" width="48" height="48" border="0"></a></p>	  
	  <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.produceplanbill")','/UserFunctionQueryAction.do?tableName=tblPlan&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.produceplanbill')">$text.get("muduleFlow.lb.produceplanbill")</a></p>
	  #else
	  <p><img src="/style/images/flow/pro6.gif" width="48" height="48" border="0"></p>	  
	  <p class="fc">$text.get("muduleFlow.lb.produceplanbill")</p>
	   #end
	   
	   #if($globals.hasQuery("tblBOM"))
	  <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.produceDetailList")','/BomAction.do?operation=6');" title="$text.get('muduleFlow.lb.produceDetailList')"><img src="/style/images/flow/fin10.gif" width="48" height="48" border="0"></a></p>	  
	  <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.produceDetailList")','/BomAction.do?operation=6');" title="$text.get('muduleFlow.lb.produceDetailList')">$text.get("muduleFlow.lb.produceDetailList")</a></p>
	  #else
	  <p><img src="/style/images/flow/fin10.gif" width="48" height="48" border="0"></p>	  
	  <p class="fc">$text.get("muduleFlow.lb.produceDetailList")</p>
	   #end
	  
	  </td>
    <td align="center"><img src="/style/images/flow/m_flow4wf2.gif"></td>
    <td width="73" align="center">
	#if($LoginBean.operationMap.get("/Mrp.do").query())
		<p><a href="javascript:showreModule('$text.get("mrp.lb.mrpCount")','/Mrp.do?method=orderSel');"title="$text.get('mrp.lb.mrpCount')"><img 

src="/style/images/flow/sale5.gif" width="48" height="48" border="0"></a></p>		
		<p><a href="javascript:showreModule('$text.get("mrp.lb.mrpCount")','/Mrp.do?method=orderSel');" title="$text.get('mrp.lb.mrpCount')">$text.get("mrp.lb.mrpCount")</a></p>
	#else
		 <p><img src="/style/images/flow/sale5.gif" width="48" height="48" border="0"></p>	  
	 	 <p class="fc">$text.get("mrp.lb.mrpCount")</p>
	#end
	</td>
    <td width="32" align="center"><img src="/style/images/flow/m_flowwf.gif"></td>
    <td width="74" align="center">
	#if($globals.hasQuery("tblBuyOrder"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.Order")','/UserFunctionQueryAction.do?tableName=tblBuyOrder&parentCode=&operation=6');" title="$text.get('moduleFlow.lb.Order')"><img src="/style/images/flow/pro2.gif" width="48" height="48" border="0"></a></p>
      <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.Order")','/UserFunctionQueryAction.do?tableName=tblBuyOrder&parentCode=&operation=6');" title="$text.get('moduleFlow.lb.Order')">$text.get("moduleFlow.lb.Order")</a></p>
	   #else
	  <p><img src="/style/images/flow/pro2.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.Order")</p>
	   #end
	  
#if($globals.hasQuery("tblProduce"))
      <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.produceTaskbill")','/UserFunctionQueryAction.do?tableName=tblProduce&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.produceTaskbill')"><img src="/style/images/flow/war8.gif" width="48" height="48" border="0"></a></p>
      <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.produceTaskbill")','/UserFunctionQueryAction.do?tableName=tblProduce&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.produceTaskbill')">$text.get("muduleFlow.lb.produceTaskbill")</a></p>
	  #else
	   <p><img src="/style/images/flow/war8.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("muduleFlow.lb.produceTaskbill")</p>
	  #end
	  
	  </td>
    <td align="right"><img src="/style/images/flow/m_flow4wf3.gif"></td>
    <td colspan="2" align="center">
	 #if($globals.hasQuery("tblOutMaterials"))
	<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.material1")','/UserFunctionQueryAction.do?tableName=tblOutMaterials&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.material1')"><img src="/style/images/flow/pro3.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.material1")','/UserFunctionQueryAction.do?tableName=tblOutMaterials&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.material1')">$text.get("muduleFlow.lb.material1")</a></p>
	#else
	<p><img src="/style/images/flow/pro3.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("muduleFlow.lb.material1")</p>
	  #end
	  
	 #if($globals.hasQuery("tblOutMaterials"))
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.material2")','/UserFunctionQueryAction.do?tableName=tblOutMaterials&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.material2')"><img src="/style/images/flow/war9.gif" width="48" height="48" border="0"></a></p>		
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.material2")','/UserFunctionQueryAction.do?tableName=tblOutMaterials&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.material2')">$text.get("muduleFlow.lb.material2")</a></p>
		#else
		<p><img src="/style/images/flow/war9.gif" width="48" height="48" border="0"></p>		
		<p class="fc">$text.get("muduleFlow.lb.material2")</p>
		 #end
		
		#if($globals.hasQuery("tblTransferMaterial"))
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.material3")','/UserFunctionQueryAction.do?tableName=tblTransferMaterial&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.material3')"><img src="/style/images/flow/war5.gif" width="48" height="48" border="0"></a></p>		
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.material3")','/UserFunctionQueryAction.do?tableName=tblTransferMaterial&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.material3')">$text.get("muduleFlow.lb.material3")</a></p>
		#else
		<p><img src="/style/images/flow/war5.gif" width="48" height="48" border="0"></p>		
		<p class="fc">$text.get("muduleFlow.lb.material3")</p>
		 #end
		
		#if($globals.hasQuery("tblInProducts"))
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.material4")','/UserFunctionQueryAction.do?tableName=tblInProducts&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.material4')"><img src="/style/images/flow/war7.gif" width="48" height="48" border="0"></a></p>		
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.material4")','/UserFunctionQueryAction.do?tableName=tblInProducts&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.material4')">$text.get("muduleFlow.lb.material4")</a></p>
		#else
		<p><img src="/style/images/flow/war7.gif" width="48" height="48" border="0"></p>		
		<p class="fc">$text.get("muduleFlow.lb.material4")</p>
		#end
		
		
		</td>
    <td width="47" align="left"><img src="/style/images/flow/m_line.gif"></td>
    <td width="75" align="center">
	#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=tblCosting").query())
	<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.costCount")','/UserFunctionQueryAction.do?tableName=tblCosting');" title="$text.get('muduleFlow.lb.costCount')"><img src="/style/images/flow/war3.gif" width="48" height="48" border="0"></a></p>
      <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.costCount")','/UserFunctionQueryAction.do?tableName=tblCosting');" title="$text.get('muduleFlow.lb.costCount')">$text.get("muduleFlow.lb.costCount")</a></p>
	  #else
	  <p><img src="/style/images/flow/war3.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("muduleFlow.lb.costCount")</p>
	  #end
	  </td>
    <td width="32" align="center"><img src="/style/images/flow/m_flow4wf.gif"></td>
    <td align="center" style="color:#999999"><p><img src="/style/images/flow/fin2.gif" width="48" height="48" border="0"></p>
        <p>$text.get("muduleFlow.lb.TermMaterial")</p>
        <p><img src="/style/images/flow/fin4.gif" width="48" height="48" border="0"></p>        
        <p>$text.get("muduleFlow.lb.TermMaterialed")</p>
        <p><img src="/style/images/flow/fin6.gif" width="48" height="48" border="0"></p>        
        <p>$text.get("muduleFlow.lb.moneyEveryDet")</p>
        <p><img src="/style/images/flow/fin22.gif" width="48" height="48" border="0"></p>        
        <p>$text.get("muduleFlow.lb.produceChengben")</p></td>
  </tr>
	
	 <tr>
    <td height="60" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td colspan="2" align="center"><p><img src="/style/images/flow/m_flowwf2.gif"></p>      </td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td colspan="2" align="center">
	#if($globals.hasQuery("tblEntrustOutGoods"))
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.returnbill")','/UserFunctionQueryAction.do?tableName=tblEntrustOutGoods&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.returnbill')"><img src="/style/images/flow/war2.gif" width="48" height="48" border="0"></a></p>		
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.returnbill")','/UserFunctionQueryAction.do?tableName=tblEntrustOutGoods&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.returnbill')">$text.get("muduleFlow.lb.returnbill")</a></p>
		 #else
		<p><img src="/style/images/flow/war2.gif" width="48" height="48" border="0"></p>		
		<p class="fc">$text.get("muduleFlow.lb.returnbill")</a></p>
		 #end
		
		</td>
    <td colspan="2" align="center">
	#if($globals.hasQuery("tblProduceCost"))
	<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.moneyEvery")','/UserFunctionQueryAction.do?tableName=tblProduceCost&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.moneyEvery')"><img src="/style/images/flow/fin9.gif" width="48" height="48" border="0"></a></p>
      <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.moneyEvery")','/UserFunctionQueryAction.do?tableName=tblProduceCost&parentCode=&operation=6');" title="$text.get('muduleFlow.lb.moneyEvery')">$text.get("muduleFlow.lb.moneyEvery")</a></p>
	  #else
	  <p><img src="/style/images/flow/fin9.gif" width="48" height="48" border="0"></a></p>
      <p class="fc">$text.get("muduleFlow.lb.moneyEvery")</a></p>
	  #end
	  </td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
</table>
</div>
#end
	</div>
		
#else
		
		<div class="scroll_function_small_menuReport">
#if("28141812_0811271603291400086"=="$!keyId")
<div style="margin-top:5px;">
 <table style="width:540px" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">
	#if($globals.hasQuery("tblBuyChange"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.buyAdjustPrice")','/UserFunctionQueryAction.do?tableName=tblBuyChange&src=menu&operation=6');" title="$text.get('moduleFlow.lb.buyAdjustPrice')"><img src="/style/images/flow/pro1.gif" width="48" height="48" border="0"></a></p>
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.buyAdjustPrice")','/UserFunctionQueryAction.do?tableName=tblBuyChange&src=menu&operation=6');" title="$text.get('moduleFlow.lb.buyAdjustPrice')">$text.get("moduleFlow.lb.buyAdjustPrice")</a></p>
	#else
	<p><img src="/style/images/flow/pro1.gif" width="48" height="48" border="0"></p>
	<p class="fc">$text.get("moduleFlow.lb.buyAdjustPrice")</p>
	#end
	</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
    </tr>
  <tr>
    <td height="60" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center"><img src="/style/images/flow/flowup.gif"></td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
  <tr>
    <td align="center">
	#if($globals.hasQuery("tblBuyAskPrice"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.BuyAskPrice")','/UserFunctionQueryAction.do?tableName=tblBuyAskPrice&src=menu&operation=6');" title="$text.get('moduleFlow.lb.BuyAskPrice')"><img src="/style/images/flow/pro4.gif" width="48" height="48" border="0"></a></p>
	<p>
	<a href="javascript:showreModule('$text.get("moduleFlow.lb.BuyAskPrice")','/UserFunctionQueryAction.do?tableName=tblBuyAskPrice&src=menu&operation=6');" title="$text.get('moduleFlow.lb.BuyAskPrice')">$text.get("moduleFlow.lb.BuyAskPrice")</a></p>
#else
	<p><img src="/style/images/flow/pro4.gif" width="48" height="48" border="0"></p>
	<p class="fc">$text.get("moduleFlow.lb.BuyAskPrice")</p>
	 #end
	</td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblBuyOrder"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.Order")','/UserFunctionQueryAction.do?tableName=tblBuyOrder&src=menu&src=menu&operation=6');" title="$text.get('moduleFlow.lb.Order')"><img src="/style/images/flow/pro5.gif" width="48" height="48" border="0"></a></p>
	 <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.Order")','/UserFunctionQueryAction.do?tableName=tblBuyOrder&src=menu&src=menu&operation=6');" title="$text.get('moduleFlow.lb.Order')">$text.get("moduleFlow.lb.Order")</a></p>
	#else
	<p><img src="/style/images/flow/pro5.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.Order")</p>
	 #end 
	  </td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblBuyInStock"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OrderList")','/UserFunctionQueryAction.do?tableName=tblBuyInStock&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OrderList')"><img src="/style/images/flow/pro6.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OrderList")','/UserFunctionQueryAction.do?tableName=tblBuyInStock&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OrderList')">$text.get("moduleFlow.lb.OrderList")</a></p>
	  #else
	 <p><img src="/style/images/flow/pro6.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.OrderList")</p>
	   #end
	  </td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblPay"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OrderPayList")','/UserFunctionQueryAction.do?tableName=tblPay&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OrderPayList')"><img src="/style/images/flow/pro2.gif" width="48" height="48" border="0"></a></p>
      <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OrderPayList")','/UserFunctionQueryAction.do?tableName=tblPay&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OrderPayList')">$text.get("moduleFlow.lb.OrderPayList")</a></p>
	   #else
	 <p><img src="/style/images/flow/pro2.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.OrderPayList")</p>
	  #end
	  </td>
    </tr>
  <tr>
    <td height="60" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center"><img src="/style/images/flow/flowdown.gif"></td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblBuyOutStock"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OrderTuiList")','/UserFunctionQueryAction.do?tableName=tblBuyOutStock&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OrderTuiList')"><img src="/style/images/flow/pro3.gif" width="48" height="48" border="0"></a></p>
      <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OrderTuiList")','/UserFunctionQueryAction.do?tableName=tblBuyOutStock&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OrderTuiList')">$text.get("moduleFlow.lb.OrderTuiList")</a></p>
	   #else
	  <p><img src="/style/images/flow/pro3.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.OrderTuiList")</p>
	   #end
	  </td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
</table>
</div>
#elseif("28141812_0811271603017810084"=="$!keyId")
<div style="margin-top:20px;">
<table style="width:540px" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">
	#if($globals.hasQuery("tblSalesChange"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesChange")','/UserFunctionQueryAction.do?tableName=tblSalesChange&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesChange')"><img src="/style/images/flow/sale1.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesChange")','/UserFunctionQueryAction.do?tableName=tblSalesChange&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesChange')">$text.get("moduleFlow.lb.SalesChange")</a></p>
	  #else
	 <p><img src="/style/images/flow/sale1.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.SalesChange")</p>
	  #end
	  </td>
    <td width="60" align="center">&nbsp;</td>
    <td width="90" align="center">&nbsp;</td>
    </tr>
  <tr>
    <td height="60" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center"><img src="/style/images/flow/flowup.gif"></td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
  <tr>
    <td align="center">
	<p><img src="/style/images/flow/sale5.gif" width="48" height="48" border="0"></p>
	<p class="fc">$text.get("moduleFlow.lb.CRMsalesQuot")(CRM)</p></td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblSalesOrder"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesOrder")','/UserFunctionQueryAction.do?tableName=tblSalesOrder&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesOrder')"><img src="/style/images/flow/sale4.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesOrder")','/UserFunctionQueryAction.do?tableName=tblSalesOrder&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesOrder')">$text.get("moduleFlow.lb.SalesOrder")</a></p>
	 #else
	 <p><img src="/style/images/flow/sale4.gif" width="48" height="48" border="0"></p>
     <p class="fc">$text.get("moduleFlow.lb.SalesOrder")</p>
	 #end
	 </td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblSalesOutStock"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesOutStock")','/UserFunctionQueryAction.do?tableName=tblSalesOutStock&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesOutStock')"><img src="/style/images/flow/sale6.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesOutStock")','/UserFunctionQueryAction.do?tableName=tblSalesOutStock&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesOutStock')">$text.get("moduleFlow.lb.SalesOutStock")</a></p>
	#else
	<p><img src="/style/images/flow/sale6.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.SalesOutStock")</p>
	#end
	</td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblSaleReceive"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SaleReceive")','/UserFunctionQueryAction.do?tableName=tblSaleReceive&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SaleReceive')"><img src="/style/images/flow/sale2.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SaleReceive")','/UserFunctionQueryAction.do?tableName=tblSaleReceive&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SaleReceive')">$text.get("moduleFlow.lb.SaleReceive")</a></p>
	#else
	<p><img src="/style/images/flow/sale2.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.SaleReceive")</p>
	#end
	</td>
    </tr>
  <tr>
    <td height="60" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center"><img src="/style/images/flow/flowdown.gif"></td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblSalesReturnStock"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesReturnStock")','/UserFunctionQueryAction.do?tableName=tblSalesReturnStock&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesReturnStock')"><img src="/style/images/flow/sale3.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.SalesReturnStock")','/UserFunctionQueryAction.do?tableName=tblSalesReturnStock&src=menu&operation=6');" title="$text.get('moduleFlow.lb.SalesReturnStock')">$text.get("moduleFlow.lb.SalesReturnStock")</a></p>
	 #else
	 <p><img src="/style/images/flow/sale3.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.SalesReturnStock")</p>
	 #end
	 </td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
</table>
</div>
#elseif("fca2035a_0910281030268750005"=="$!keyId")
<div style="margin-top:5px;"> 
<table height="470" border="0" cellpadding="0" cellspacing="0" style="width:540px">
  <tr>
    <td width="100" rowspan="2" align="center" valign="bottom">&nbsp;</td>
    <td width="10" rowspan="9" align="center" valign="middle"><img src="/style/images/flow/lines.gif" width="5" height="400"></td>
    <td width="100" rowspan="2" align="center"><p><img src="/style/images/flow/war4.gif" width="48" height="48"></p>
       <p class="fc">$text.get("moduleFlow.lb.Allot")</p></td>
    <td width="120" rowspan="2" align="center"><img src="/style/images/flow/flowwf.gif" width="60" height="100"></td>
    <td width="100" align="center" valign="middle">
	#if($globals.hasQuery("tblAllot"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.sameAllot")','/UserFunctionQueryAction.do?tableName=tblAllot&src=menu&operation=6');" title="$text.get('moduleFlow.lb.sameAllot')"><img src="/style/images/flow/war8.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.sameAllot")','/UserFunctionQueryAction.do?tableName=tblAllot&src=menu&operation=6');" title="$text.get('moduleFlow.lb.sameAllot')">$text.get("moduleFlow.lb.sameAllot")</a></p>
	 #else
	 <p><img src="/style/images/flow/war8.gif" width="48" height="48" border="0"></p>
     <p class="fc">$text.get("moduleFlow.lb.sameAllot")</p>
	  #end
	 </td>
    <td width="9" rowspan="9" align="center" valign="middle"><img src="/style/images/flow/lines.gif" width="5" height="400"></td>
    <td width="100" rowspan="2" align="center" valign="bottom">&nbsp;</td>
    </tr>
  <tr>
    <td align="center">
	#if($globals.hasQuery("tblAllotChange"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.AllotChange")','/UserFunctionQueryAction.do?tableName=tblAllotChange&src=menu&operation=6');" title="$text.get('moduleFlow.lb.AllotChange')"><img src="/style/images/flow/war9.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.AllotChange")','/UserFunctionQueryAction.do?tableName=tblAllotChange&src=menu&operation=6');" title="$text.get('moduleFlow.lb.AllotChange')">$text.get("moduleFlow.lb.AllotChange")</a></p>
	 #else
	 <p><img src="/style/images/flow/war9.gif" width="48" height="48" border="0"></p>
     <p class="fc">$text.get("moduleFlow.lb.AllotChange")</p>
	 #end
	 </td>
    </tr>
  <tr>
    <td height="10" align="center">
	#if($globals.hasQuery("tblOtherIn"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OtherIn")','/UserFunctionQueryAction.do?tableName=tblOtherIn&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OtherIn')"><img src="/style/images/flow/pro6.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OtherIn")','/UserFunctionQueryAction.do?tableName=tblOtherIn&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OtherIn')">$text.get("moduleFlow.lb.OtherIn")</a></p>
	 #else
	  <p><img src="/style/images/flow/pro6.gif" width="48" height="48" border="0"></p>
      <p class="fc">$text.get("moduleFlow.lb.OtherIn")</p>
	 #end
	  </td>
    <td width="0" align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblAdjustPrice"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.AdjustPrice")','/UserFunctionQueryAction.do?tableName=tblAdjustPrice&src=menu&operation=6');" title="$text.get('moduleFlow.lb.AdjustPrice')"><img src="/style/images/flow/war3.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.AdjustPrice")','/UserFunctionQueryAction.do?tableName=tblAdjustPrice&src=menu&operation=6');" title="$text.get('moduleFlow.lb.AdjustPrice')">$text.get("moduleFlow.lb.AdjustPrice")</a></p>
	 #else
	<p><img src="/style/images/flow/war3.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.AdjustPrice")</a></p>
	 #end
	  
	  </td>
    <td align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblOtherOut"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OtherOut")','/UserFunctionQueryAction.do?tableName=tblOtherOut&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OtherOut')"><img src="/style/images/flow/fin9.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.OtherOut")','/UserFunctionQueryAction.do?tableName=tblOtherOut&src=menu&operation=6');" title="$text.get('moduleFlow.lb.OtherOut')">$text.get("moduleFlow.lb.OtherOut")</a></p>
	#else
	<p><img src="/style/images/flow/fin9.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.OtherOut")</p>
	 #end
	
	 </td>
  </tr>
  <tr>
    <td height="10" align="center">&nbsp;</td>
    <td width="0" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
  </tr>
  <tr>
    <td align="center">
	#if($globals.hasQuery("tblGoodsConnect"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.GoodsAssembly")','/UserFunctionQueryAction.do?tableName=tblGoodsConnect&src=menu&operation=6');" title="$text.get('moduleFlow.lb.GoodsAssembly')"><img src="/style/images/flow/war1.gif" width="48" height="48"></a></p>
	<a href="javascript:showreModule('$text.get("moduleFlow.lb.GoodsAssembly")','/UserFunctionQueryAction.do?tableName=tblGoodsConnect&src=menu&operation=6');" title="$text.get('moduleFlow.lb.GoodsAssembly')">$text.get("moduleFlow.lb.GoodsAssembly")</a></p>
	#else
	<p><img src="/style/images/flow/war1.gif" width="48" height="48"></p>
	<p class="fc">$text.get("moduleFlow.lb.GoodsAssembly")</p>
	#end
	</td>
    <td align="center"><img src="/style/images/flow/flowl.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblBOMAssembly"))
	<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.fixMoudle")','/UserFunctionQueryAction.do?tableName=tblBOMAssembly&src=menu&operation=6');" title="$text.get('muduleFlow.lb.fixMoudle')"><img src="/style/images/flow/war2.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.fixMoudle")','/UserFunctionQueryAction.do?tableName=tblBOMAssembly&src=menu&operation=6');" title="$text.get('muduleFlow.lb.fixMoudle')">$text.get("muduleFlow.lb.fixMoudle")</a></p>
	#else
	<p><img src="/style/images/flow/war2.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("muduleFlow.lb.fixMoudle")</p>
	#end
	  </td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblGoodsDisconnect"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.GoodsSplitForm")','/UserFunctionQueryAction.do?tableName=tblGoodsDisconnect&src=menu&operation=6');" title="$text.get('moduleFlow.lb.GoodsSplitForm')"><img src="/style/images/flow/war10.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.GoodsSplitForm")','/UserFunctionQueryAction.do?tableName=tblGoodsDisconnect&src=menu&operation=6');" title="$text.get('moduleFlow.lb.GoodsSplitForm')">$text.get("moduleFlow.lb.GoodsSplitForm")</p>
	#else
	<p><img src="/style/images/flow/war10.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.GoodsSplitForm")</p>
	#end
	  </td>
    </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
  <tr>
  
    <td align="center">
	#if($globals.hasQuery("tblCheckMore"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.CheckMore")','/UserFunctionQueryAction.do?tableName=tblCheckMore&src=menu&operation=6');" title="$text.get('moduleFlow.lb.CheckMore')"><img src="/style/images/flow/war6.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.CheckMore")','/UserFunctionQueryAction.do?tableName=tblCheckMore&src=menu&operation=6');" title="$text.get('moduleFlow.lb.CheckMore')">$text.get("moduleFlow.lb.CheckMore")</a></p>
	#else
	<p><img src="/style/images/flow/war6.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.CheckMore")</p>
	#end
	  </td>
    <td align="center"><img src="/style/images/flow/flowl.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblStockCheck"))
	<p><a href="javascript:showreModule('$text.get("moduleFlow.lb.kucunpandiandan")','/UserFunctionQueryAction.do?tableName=tblStockCheck&src=menu&operation=6');" title="$text.get('moduleFlow.lb.kucunpandiandan')"><img src="/style/images/flow/war7.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.kucunpandiandan")','/UserFunctionQueryAction.do?tableName=tblStockCheck&src=menu&operation=6');" title="$text.get('moduleFlow.lb.kucunpandiandan')">$text.get("moduleFlow.lb.kucunpandiandan")</a></p>
	#else
	<p><img src="/style/images/flow/war7.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("moduleFlow.lb.kucunpandiandan")</p>
	 #end
	  </td>
    <td align="center"><img src="/style/images/flow/flowr.gif"></td>
    <td align="center">
	#if($globals.hasQuery("tblCheck"))
	 <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.Check")','/UserFunctionQueryAction.do?tableName=tblCheck&src=menu&operation=6');" title="$text.get('moduleFlow.lb.Check')"><img src="/style/images/flow/war5.gif" width="48" height="48" border="0"></a></p>
     <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.Check")','/UserFunctionQueryAction.do?tableName=tblCheck&src=menu&operation=6');" title="$text.get('moduleFlow.lb.Check')">$text.get("moduleFlow.lb.Check")</a></p>
	 #else
	 <p><img src="/style/images/flow/war5.gif" width="48" height="48" border="0"></p>
     <p class="fc">$text.get("moduleFlow.lb.Check")</p>
	 #end 
	 </td>
  </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
</table>
</div>
#elseif("fca2035a_0910281034230930020"=="$!keyId")
<div style=" padding-top:10px;">
<table style=" width:640px;" width="640" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="169" rowspan="5" align="right"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="110" rowspan="3" align="center">
		#if($globals.hasQuery("tblIncome"))
		<p><a href="javascript:showreModule('$text.get("modulFlow.lb.Income")','/UserFunctionQueryAction.do?tableName=tblIncome&src=menu&operation=6');" title="$text.get('modulFlow.lb.Income')"><img src="/style/images/flow/war9.gif" width="48" height="48" border="0"></a></p>
         <p class="fc"><a href="javascript:showreModule('$text.get("modulFlow.lb.Income")','/UserFunctionQueryAction.do?tableName=tblIncome&src=menu&operation=6');" title="$text.get('modulFlow.lb.Income')">$text.get("modulFlow.lb.Income")</a></p>
		  #else
		 <p><img src="/style/images/flow/war9.gif" width="48" height="48" border="0"></p>
         <p  class="fc">$text.get("modulFlow.lb.Income")</p> 
		 #end 
      	
		#if($globals.hasQuery("tblUserTransAcc"))
          <p><a href="javascript:showreModule('$text.get("modulFlow.lb.UserTransAcc")','/UserFunctionQueryAction.do?tableName=tblUserTransAcc&src=menu&operation=6');" title="$text.get('modulFlow.lb.UserTransAcc')"><img src="/style/images/flow/war.gif" width="48" height="48" border="0"></a></p>
          <p><a href="javascript:showreModule('$text.get("modulFlow.lb.UserTransAcc")','/UserFunctionQueryAction.do?tableName=tblUserTransAcc&src=menu&operation=6');" title="$text.get('modulFlow.lb.UserTransAcc')">$text.get("modulFlow.lb.UserTransAcc")</a></p>
		 #else
		 <p><img src="/style/images/flow/war.gif" width="48" height="48" border="0"></p>
         <p class="fc">$text.get("modulFlow.lb.UserTransAcc")</p>
		 #end 
		  </td>
        <td width="60" align="center">
		#if($globals.hasQuery("tblPay"))
		 <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.Pay")','/UserFunctionQueryAction.do?tableName=tblPay&type=xxx&src=menu&operation=6');" title="$text.get('moduleFlow.lb.Pay')"><img src="/style/images/flow/fin10.gif" width="48" height="48" border="0"></a></p>
         <p><a href="javascript:showreModule('$text.get("moduleFlow.lb.Pay")','/UserFunctionQueryAction.do?tableName=tblPay&type=xxx&src=menu&operation=6');" title="$text.get('moduleFlow.lb.Pay')">$text.get("moduleFlow.lb.Pay")</a></p>
		 #else
		  <p><img src="/style/images/flow/fin10.gif" width="48" height="48" border="0"></p>
          <p class="fc">$text.get("moduleFlow.lb.Pay")</p>
		 #end 
		  </td>
      </tr>
      <tr>
        <td align="center">
		#if($globals.hasQuery("tblSaleReceive"))
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.receive")','/UserFunctionQueryAction.do?tableName=tblSaleReceive&type=xxx&src=menu&operation=6');" title="$text.get('muduleFlow.lb.receive')"><img src="/style/images/flow/war8.gif" width="48" height="48" border="0"></a></p>
         <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.receive")','/UserFunctionQueryAction.do?tableName=tblSaleReceive&type=xxx&src=menu&operation=6');" title="$text.get('muduleFlow.lb.receive')">$text.get("muduleFlow.lb.receive")</a></p>
		   #else
		 <p><img src="/style/images/flow/war8.gif" width="48" height="48" border="0"></p>
         <p class="fc">$text.get("muduleFlow.lb.receive")</p>
		   #end 
		</td>
      </tr>
      <tr>
        <td align="center">
		#if($globals.hasQuery("tblExpensed"))
		 <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.ChargeBill")','/UserFunctionQueryAction.do?tableName=tblExpensed&src=menu&operation=6');" title="$text.get('muduleFlow.lb.receive')"><img src="/style/images/flow/fin1.gif" width="48" height="48" border="0"></a></p>
         <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.ChargeBill")','/UserFunctionQueryAction.do?tableName=tblExpensed&src=menu&operation=6');" title="$text.get('muduleFlow.lb.receive')">$text.get("muduleFlow.lb.ChargeBill")</a></p>
		 #else
		 <p><img src="/style/images/flow/fin1.gif" width="48" height="48" border="0"></p>
         <p class="fc">$text.get("muduleFlow.lb.receive")</p>
		 #end 
		 </td>
      </tr>
      <tr>
        <td width="110" align="center">&nbsp;</td>
        <td align="center">&nbsp;</td>
      </tr>
    </table></td>
    <td width="25" rowspan="10" align="center"><img src="/style/images/flow/linel.gif" width="24" height="400"></td>
    <td width="60" align="center">&nbsp;</td>
    <td width="80" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="80" align="center">&nbsp;</td>
    <td width="60" align="center">&nbsp;</td>
    <td width="25" rowspan="10" align="center"><img src="/style/images/flow/liner.gif" width="24" height="400"></td>
    <td width="80" rowspan="10" align="center" valign="middle"><table width="100%" height="100%" border="0" cellpadding="0"  style="color:#999999" cellspacing="0">
      <tr>
        <td align="center"><img src="/style/images/flow/fin22.gif" width="48" height="48" border="0"><br>$text.get("muduleFlow.lb.receive")</td>
      </tr>
      <tr>
        <td height="35" align="center"><img src="/style/images/flow/flowx.gif" width="15" height="30"></td>
      </tr>
      <tr>
        <td align="center"><img src="/style/images/flow/fin6.gif" border="0"><br>$text.get("muduleFlow.lb.LoginPay")</td>
      </tr>
      <tr>
        <td height="35" align="center"><img src="/style/images/flow/flowx.gif" width="15" height="30"></td>
      </tr>
      <tr>
        <td align="center"><img src="/style/images/flow/fin4.gif" width="48" height="48" border="0"><br>$text.get("muduleFlow.lb.costLast")</td>
      </tr>
      <tr>
        <td height="35" align="center"><img src="/style/images/flow/flowx.gif" width="15" height="30"></td>
      </tr>
      <tr>
        <td align="center"><img src="/style/images/flow/fin2.gif" width="48" height="48" border="0"><br>$text.get("muduleFlow.lb.tocost")</td>
      </tr>
      <tr>
        <td height="35" align="center"><img src="/style/images/flow/flowx.gif" width="15" height="30"></td>
      </tr>
      <tr>
        <td align="center"><img src="/style/images/flow/fin3.gif" width="48" height="48" border="0"><br>$text.get("muduleFlow.lb.finallcost")</td>
      </tr>
    </table>    </td>
  </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
  </tr>
  <tr>
    <td align="center"><img src="/style/images/flow/flowr.gif" width="47" height="15"></td>
    <td align="center">
	#if($globals.hasQuery("tblAccMain"))
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.voucherlogin")','/VoucherManage.do?operation=6&isEspecial=list');" title="$text.get('muduleFlow.lb.voucherlogin')"><img src="/style/images/flow/pro3.gif" width="48" height="48" border="0"></a></p>
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.voucherlogin")','/VoucherManage.do?operation=6&isEspecial=list');" title="$text.get('muduleFlow.lb.voucherlogin')">$text.get("muduleFlow.lb.voucherlogin")</a></p>
		 #else
		<p><img src="/style/images/flow/pro3.gif" width="48" height="48" border="0"></p>
		<p class="fc">$text.get("muduleFlow.lb.voucherlogin")</p>
		#end
	  </td>
    <td align="center"><img src="/style/images/flow/flowr.gif" width="47" height="15"></td>
    <td align="center">
	#if($globals.hasQuery("tblAccMain"))
	<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.vouchermanage")','/VoucherManageAction.do?operation=4');" title="$text.get('muduleFlow.lb.vouchermanage')"><img src="/style/images/flow/fin12.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.vouchermanage")','/VoucherManageAction.do?operation=4');" title="$text.get('muduleFlow.lb.vouchermanage')">$text.get("muduleFlow.lb.vouchermanage")</a></p>
	  #else
	<p><img src="/style/images/flow/fin12.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("muduleFlow.lb.vouchermanage")</p>
	#end
	  </td>
    <td align="center"><img src="/style/images/flow/flowr2.gif" width="47" height="15"></td>
  </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td rowspan="6" align="right" valign="top" style="color:#999999"><p>&nbsp;</p>
      <p>&nbsp;</p>
      <p>&nbsp;</p>
      <p>&nbsp;</p>
      <p>$text.get("muduleFlow.lb.autorun1")</p>
      <p>$text.get("muduleFlow.lb.autorun2")</p>
      <p>$text.get("muduleFlow.lb.autorun3")</p>
	  <p>$text.get("muduleFlow.lb.autorun4")</p>
	</td>
  </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td rowspan="2" align="center"><p><img src="/style/images/flow/fin8.gif" width="48" height="48" border="0"></p>
<p class="fc">$text.get("muduleFlow.lb.ChequeNo")</p></td>
    <td rowspan="2" align="center"><img src="/style/images/flow/flowwf.gif" width="60" height="100"></td>
    <td align="center">
	#if($globals.hasQuery("tblChequeNoBuy"))
	<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.ChequeNobuy")','/UserFunctionQueryAction.do?tableName=tblChequeNoBuy&src=menu&operation=6');" title="$text.get('muduleFlow.lb.ChequeNobuy')"><img src="/style/images/flow/fin9.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.ChequeNobuy")','/UserFunctionQueryAction.do?tableName=tblChequeNoBuy&src=menu&operation=6');" title="$text.get('muduleFlow.lb.ChequeNobuy')">$text.get("muduleFlow.lb.ChequeNobuy")</a></p>
	#else
	<p><img src="/style/images/flow/fin9.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("muduleFlow.lb.ChequeNobuy")</p>
	#end
	 </td>
  </tr>
  <tr>
    <td rowspan="6" align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="31%" rowspan="4" align="center"><p><img src="/style/images/flow/fin7.gif" width="48" height="48" border="0" /></p>
 <p class="fc">$text.get("muduleFlow.lb.comegochange")</p></td>
        <td width="20%" rowspan="4" align="center"><img src="/style/images/flow/flow4wf.gif" width="62" height="192"></td>
        <td width="49%" align="center">
		#if($globals.hasQuery("tblTransferSale1"))
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferSale1")','/UserFunctionQueryAction.do?tableName=tblTransferSale1&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferSale1')"><img src="/style/images/flow/fin19.gif" width="48" height="48" border="0" /></a></p>
        <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferSale1")','/UserFunctionQueryAction.do?tableName=tblTransferSale1&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferSale1')">$text.get("muduleFlow.lb.TransferSale1")</a></p>
		#else
		<p><img src="/style/images/flow/fin19.gif" width="48" height="48" border="0" /></p>
        <p class="fc">$text.get("muduleFlow.lb.TransferSale1")</p>
		#end
		  </td>
      </tr>
      <tr>
        <td align="center">
		#if($globals.hasQuery("tblTransferSale2"))
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferSale2")','/UserFunctionQueryAction.do?tableName=tblTransferSale2&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferSale2')"><img src="/style/images/flow/fin18.gif" width="48" height="48" border="0" /></a></p>
        <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferSale2")','/UserFunctionQueryAction.do?tableName=tblTransferSale2&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferSale2')">$text.get("muduleFlow.lb.TransferSale2")</a></p>
		#else
		<p><img src="/style/images/flow/fin18.gif" width="48" height="48" border="0" /></p>
        <p class="fc">$text.get("muduleFlow.lb.TransferSale2")</p>
		#end
		 </td>
      </tr>
      <tr>
        <td align="center">
		#if($globals.hasQuery("tblTransferSale3"))
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferSale3")','/UserFunctionQueryAction.do?tableName=tblTransferSale3&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferSale3')"><img src="/style/images/flow/fin20.gif" width="48" height="48" border="0" /></a></p>
        <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferSale3")','/UserFunctionQueryAction.do?tableName=tblTransferSale3&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferSale3')">$text.get("muduleFlow.lb.TransferSale3")</p>
		#else
		<p><img src="/style/images/flow/fin20.gif" width="48" height="48" border="0" /></p>
        <p class="fc">$text.get("muduleFlow.lb.TransferSale3")</p>
		#end
		</td>
      </tr>
      <tr>
        <td align="center">
		#if($globals.hasQuery("tblTransferSale4"))
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferSale4")','/UserFunctionQueryAction.do?tableName=tblTransferSale4&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferSale4')"><img src="/style/images/flow/fin21.gif" width="48" height="48" border="0" /></a></p>
		<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferSale4")','/UserFunctionQueryAction.do?tableName=tblTransferSale4&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferSale4')">$text.get("muduleFlow.lb.TransferSale4")</a></p>
		#else
		<p><img src="/style/images/flow/fin21.gif" width="48" height="48" border="0" /></p>
		<p class="fc">$text.get("muduleFlow.lb.TransferSale4")</p>
		#end
		</td>
      </tr>
    </table></td>
    <td align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblChequeNoOut"))
	   <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.ChequeNoOut")','/UserFunctionQueryAction.do?tableName=tblChequeNoOut&src=menu&operation=6');" title="$text.get('muduleFlow.lb.ChequeNoOut')"><img src="/style/images/flow/fin10.gif" width="48" height="48" border="0"></a></p>
       <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.ChequeNoOut")','/UserFunctionQueryAction.do?tableName=tblChequeNoOut&operation=6');" title="$text.get('muduleFlow.lb.ChequeNoOut')">$text.get("muduleFlow.lb.ChequeNoOut")</a></p>
	   #else
	   <p><img src="/style/images/flow/fin10.gif" width="48" height="48" border="0"></p>
       <p class="fc">$text.get("muduleFlow.lb.ChequeNoOut")</p>
	   #end
	  </td>
  </tr>
  <tr>
    <td height="40" rowspan="2" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    </tr>
  <tr>
    <td rowspan="2" align="center"><p><img src="/style/images/flow/war6.gif" width="48" height="48" border="0"></p>
 <p class="fc">$text.get("muduleFlow.lb.ComeGoChargebill")</p></td>
    <td rowspan="2" align="center"><img src="/style/images/flow/flowwf.gif" width="60" height="100"></td>
    <td align="center">
	#if($globals.hasQuery("tblPayAdjust"))
	<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferChBill")','/UserFunctionQueryAction.do?tableName=tblPayAdjust&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferChBill')"><img src="/style/images/flow/pro2.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferChBill")','/UserFunctionQueryAction.do?tableName=tblPayAdjust&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferChBill')">$text.get("muduleFlow.lb.TransferChBill")</a></p>
	 #else
	<p><img src="/style/images/flow/pro2.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("muduleFlow.lb.TransferChBill")</p>
	 #end
	</td>
  </tr>
  <tr>
    <td height="20" align="center">&nbsp;</td>
    <td align="center">
	#if($globals.hasQuery("tblAccAdjust"))
	<p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferChBill1")','/UserFunctionQueryAction.do?tableName=tblAccAdjust&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferChBill1')"><img src="/style/images/flow/war5.gif" width="48" height="48" border="0"></a></p>
    <p><a href="javascript:showreModule('$text.get("muduleFlow.lb.TransferChBill1")','/UserFunctionQueryAction.do?tableName=tblAccAdjust&src=menu&operation=6');" title="$text.get('muduleFlow.lb.TransferChBill1')">$text.get("muduleFlow.lb.TransferChBill1")</a></p>
	 #else
	<p><img src="/style/images/flow/war5.gif" width="48" height="48" border="0"></p>
    <p class="fc">$text.get("muduleFlow.lb.TransferChBill1")</p>
	 #end
	</td>
    </tr>
  <tr>
    <td height="20" align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
  </tr>
</table>
</div>
#end
	</div>
		#end
		#end
	
	&nbsp;</td>
 <td align="right" width="160" valign="top">
	
	#if($reportList.size() > 0)
		<div class="menuReport" align="left">
			<h1 class="menuCss" style="cursor:pointer;">$text.get("acc.lb.menuReport")</h1>
				<ul style="overflow-y:auto;">
					#foreach($report in $reportList)
						<li>
							<a href="javascript:showreModule('$report.ModuleItemName','$report.Link&src=menu')">$report.ModuleItemName</a>
						</li>
					#end
				</ul>
		</div>
	#end
	#if($statList.size()>0)
			<div class="menuReport"  align="left">
				<h1 class="menuCss" style="cursor:pointer;">$text.get("acc.lb.menuTotal")</h1>
					<ul style="overflow-y:auto; display:none;">
						#foreach($report in $statList)
							<li>
								<a href="javascript:showreModule('$report.ModuleItemName','$report.Link&src=menu')">$report.ModuleItemName</a>
							</li>
						#end
					</ul>
			</div>
	#end
	#if($detailList.size()>0)
			<div class="menuReport"  align="left">
				<h1 class="menuCss" style="cursor:pointer;">$text.get("acc.lb.menuDetail")</h1>
					<ul style="overflow-y:auto; display:none;">
						#foreach($report in $detailList)
							<li>
								<a href="javascript:showreModule('$report.ModuleItemName','$report.Link&src=menu')">$report.ModuleItemName</a>
							</li>
						#end
					</ul>
		    </div>
	#end
</div>

	&nbsp;</td>
  </tr>
</table>
</form>
</body>
</html>
