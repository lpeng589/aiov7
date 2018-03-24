<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("report.title.bossReport")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/date.vjs"></script>
<script language="javascript"> 
function openInputDate(obj)
{
	WdatePicker({lang:'$globals.getLocale()'});
}
var reportId = "reportId1" ;
function goReport(reportNumber,obj){
	document.getElementById(reportId).style.background="" ;
	reportId = obj ;
	document.getElementById(obj).style.background="#ffffff";
	document.getElementById("bossreport").src="/ReportDataAction.do?reportNumber="+reportNumber ;
}
function openDeptSelect(){
	displayName=encodeURI("$text.get('mrp.lb.department')") ;
	//var str  = window.showModalDialog("/UserFunctionAction.do?operation=$globals.getOP("OP_POPUP_SELECT")&selectName=SelectDepartmentName&MOID=$MOID&MOOP=query&LinkType=@URL:&displayName="+displayName,"","dialogWidth=730px;dialogHeight=450px"); 
	var urlstr = "/UserFunctionAction.do?operation=$globals.getOP("OP_POPUP_SELECT")&selectName=SelectDepartmentName&MOID=$MOID&MOOP=query&popupWin=Popdiv&LinkType=@URL:&displayName="+displayName;
	asyncbox.open({id:'Popdiv',title:'$text.get("mrp.lb.department")',url:urlstr,width:750,height:470});



}

function exePopdiv(returnValue){
	if(typeof(returnValue)!="undefined"){
		document.getElementById("deptName").value=returnValue.split("#;#")[1];
	}else{
		document.getElementById("deptName").value="";
	}
}
function getDateStr(AddDayCount) 
{ 
	var dd = new Date(); 
	dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期 
	var y = dd.getFullYear()+"";  
	var m = (dd.getMonth()+1)+"";//获取当前月份的日期 
	var d = dd.getDate()+""; 
	return y+"-"+(m.length==1?"0"+m:m)+"-"+(d.length==1?"0"+d:d); 
} 
function getMonthStr() 
{ 
	var dd = new Date(); 
	dd.setDate(1);//获取AddDayCount天后的日期 
	var y = dd.getFullYear()+"";  
	var m = (dd.getMonth()+1)+"";//获取当前月份的日期 
	var d = dd.getDate()+""; 
	return y+"-"+(m.length==1?"0"+m:m)+"-"+(d.length==1?"0"+d:d); 
} 
var showAdvanceDIV=false;
$(document).ready(function(){
	$(".h-child-btn").mouseover(function(){
		$(this).addClass("h-height").find(".d-more").show().siblings(".br-link").show();
	}).mouseout(function(){
		$(this).removeClass("h-height").find(".d-more").hide().siblings(".br-link").hide();
	});

	

   $("#qdate_zt").click(function(){ $(".curqd_selected").removeClass("curqd_selected"); $(this).addClass("curqd_selected");
		$("#date_start").val(getDateStr(-1));
		$("#date_end").val("");
	});	
	$("#qdate_jt").click(function(){ $(".curqd_selected").removeClass("curqd_selected"); $(this).addClass("curqd_selected");
		$("#date_start").val(getDateStr(0));
		$("#date_end").val("");
	});	
	$("#qdate_yz").click(function(){ $(".curqd_selected").removeClass("curqd_selected"); $(this).addClass("curqd_selected");
		$("#date_start").val(getDateStr(-7));
		$("#date_end").val("");
	});	
	$("#qdate_yy").click(function(){ $(".curqd_selected").removeClass("curqd_selected"); $(this).addClass("curqd_selected");
		$("#date_start").val(getDateStr(-30));
		$("#date_end").val("");
	});	
	$("#qdate_by").click(function(){ $(".curqd_selected").removeClass("curqd_selected"); $(this).addClass("curqd_selected");
		$("#date_start").val(getMonthStr());
		$("#date_end").val("");
	});	
	$("#qdate_default").click(function(){ $(".curqd_selected").removeClass("curqd_selected"); $(this).addClass("curqd_selected");
		$("#date_start").val($("#qdate_default").attr("def"));
		$("#date_end").val("");
	});
	$("#btnConfirmSCA").click(function() {
   		if( beforeButtonQuery()) {
   				document.forms[0].submit();
   		} 
   	});
   
   
   #if("$!ExportMsg" != "")   
   asyncbox.alert("$!ExportMsg",'下载导出结果');
   #end
   
});
</script>
<style> 
.bosstable {
	background:#FFFFFF;
	border-top:1px solid #AED3F1;
	
}
.bosstable tbody td{
	border-bottom:1px solid #AED3F1;
	border-left:1px solid #AED3F1;
	border-top:0px;
	padding-left:4px;
	padding-right:4px;
	
	font-size:12px;
	word-break:break-all;
	word-wrap :break-word;
}
.bossall {padding-left:2px}
.bosstop {float:left; width:100%; height:auto;}
.bosstall {float:left; width:170px; height:200px; background:#FFFFF0; margin-right:8px; margin-bottom:8px; border:1px solid #AED3F1;}
.bosstitle {height:22px; line-height:22px; text-indent:10px; background:url(/style/images/aiobg.gif) repeat-x left -21px;border-bottom:1px solid #AED3F1;}
.bosstdbg1 {font-weight:bolder; background:#FFFFF0; color:#0000FF; height:22px; line-height:180%;border:1px solid #AED3F1;}
.bosstdbg2 { background: #EEF7FF; height:22px; line-height:180%;border: 1px solid #AED3F1;}
.bosstdbg3 { background: #C4E1FF; height:5px; overflow:hidden; 	border:1px solid #AED3F1;}
.bottnav {height:150px;padding:5px;}
.bossfoot {float:left; width:100%; height:200px; margin-top:5px; border:1px solid #AED3F1; background:#C4E1FF;}
.bossswf { float:left; width:120px; background:#C4E1FF; height:240px;padding-left:10px;}
.bossswf ul { margin:0px; padding:0px}
.bossswf ul li { float:left; width:120px; height:24px; line-height:24px; text-indent:10px; }
.tdr {border:1px solid #AED3F1;}
.bossnav { width:100%;padding-right: 10px; height:auto; background: #FFFFFF; margin:5px;}
.bossshow_con { float:left; text-align:left; height:400px; }
.Heading2 {
	height:30px;
	width:100%;
}
.STYLE1 {font-weight: bolder; background: #FFFFF0; color: #0000FF; height: 22px; line-height: 180%; font-size: 12px; }
.STYLE3 {background: #EEF7FF; height: 22px; line-height: 180%; font-size: 12px; }
.STYLE4 {font-size: 12px}
</style>
</head>
<body>
<form action="/ReportDataAction.do?operation=bossReport" method="post">
	<div style="height: 5px;margin: 0px;padding: 0px;float: left;width: 100%;overflow: hidden;"></div>
			<div class="Heading2">
				<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif" /></div>
				<div class="HeadingTitle">$text.get("report.title.bossReport")</div>
				<ul class="HeadingButton">
					<li><button type="submit" class="b2">$text.get("common.lb.query")</button></li>
				</ul>
			</div>
			<div class="listRange_1" id="listid">
			<li><span>$text.get("mrp.lb.department")：</span><input name="deptName" id="deptName" value="$!deptNames" onDblClick="openDeptSelect()" /><img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="openDeptSelect()" ></li>				
				<li><span>单据日期：</span><input id="date_start" class="ls3_in" style="width:105px;" name="startDate" date="true" value="$!startDate" onKeyDown="if(event.keyCode==13){if(!beforeButtonQuery())return false ; submitQuery();}" onClick="openInputDate(this);" >
					-
					<input id="date_end" class="ls3_in" style="width:105px;" name="endDate" date="true" value="$!endDate" onKeyDown="if(event.keyCode==13){if(!beforeButtonQuery())return false ; submitQuery();}" onClick="openInputDate(this);" >
					<a href="javascript:void(0)" class="qd_a curqd_selected" def="2013-12-01" id="qdate_default">默认</a>
					<a href="javascript:void(0)" class="qd_a" id="qdate_zt">昨天</a>
					<a href="javascript:void(0)" class="qd_a"  id="qdate_jt">今天</a>
					<a href="javascript:void(0)" class="qd_a"  id="qdate_yz">7天内</a>
					<a href="javascript:void(0)" class="qd_a"  id="qdate_yy">30天内</a>
					<a href="javascript:void(0)" class="qd_a"  id="qdate_by">本月内</a>
</li>
				
				
				

			</div>
</form>
			<div class="scroll_function_small_a" id="conter">
<script type="text/javascript"> 
var oDiv=document.getElementById("conter");
var dHeight=document.body.clientHeight;
var sHeight=document.getElementById("listid").clientHeight;
oDiv.style.height=dHeight-sHeight-45+"px";
</script>
<table width="100%" border="0" cellspacing="0" class="boss_ta"  cellpadding="0">
  <tr class="bosstitle">
    <td class="tdr">$text.get("report.lb.fareStatus")</td>
    <td class="tdr">$text.get("reprt.lb.fareAnaly")</td>
    <td colspan="3">$text.get("report.lb.companyDynamic")</td>
    </tr>
  <tr>
    <td align="left" valign="top">
	  <table width="100%" border="0" height="160" cellpadding="0" align="center" cellspacing="0" class="bosstable" id="tblSort" name="table">
<tbody>
<tr>
<td class="bosstdbg1" height="30" colspan="5">$text.get("report.lb.exchange")</td>
</tr>
<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
  <td width="20%" align="center" class="bosstdbg2">&nbsp;</td>
  <td width="20%" align="center" class="bosstdbg2">$text.get("report.lb.Qty")</td>
  #if($globals.getVersion()==3)
  <td width="20%" align="center" class="bosstdbg2">$text.get("report.lb.Pieces")</td>
  #end
  <td width="20%" align="center" class="bosstdbg2">$text.get("report.lb.Amount")</td>
  <td width="20%" align="center" class="bosstdbg2">$text.get("report.lb.GrossProfit")</td>
</tr>
<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
<td align="right" class="bosstdbg2">$text.get("report.lb.Stock")：</td>
<td width="20%" align="right"><a href="/ReportDataAction.do?reportNumber=ReportBuyTotal&detTable_list=tblBuyInStock&startDate=$!startDate&endDate=$!endDate&DeptFullName=$!deptName&LinkType=@URL&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("BuyQty"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
#if($globals.getVersion()==3)
<td width="20%" align="right"><a href="/ReportDataAction.do?reportNumber=tblBuyInStockDet&detTable_list=tblBuyInStock&BillDate1=$!startDate&BillDate2=$!endDate&DeptFullName=$!deptName&LinkType=@URL&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("BuyPieces"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
#end
<td width="20%" align="right">#if($globals.getVersion()==3)<a href="/ReportDataAction.do?reportNumber=tblBuyInStockDet&detTable_list=tblBuyInStock&BillDate1=$!startDate&BillDate2=$!endDate&DeptFullName=$!deptName&LinkType=@URL&queryChannel=normal" target="_blank">#end$!globals.newFormatNumber($!values.get("BuyInAmount"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
<td width="20%" align="right">#if($globals.getVersion()==3)<a href="/ReportDataAction.do?reportNumber=tblBuyInStockDet&detTable_list=tblBuyInStock&BillDate1=$!startDate&BillDate2=$!endDate&DeptFullName=$!deptName&LinkType=@URL&queryChannel=normal" 

target="_blank">#end&nbsp;</a></td>
</tr>
<tr><td width="20%" align="right" class="bosstdbg2">$text.get("report.lb.StockReturn")：</td>
<td width="20%" align="right"><a href="/ReportDataAction.do?reportNumber=ReportBuyTotal&detTable_list=tblBuyInStock&startDate=$!startDate&endDate=$!endDate&DeptFullName=$!deptName&LinkType=@URL&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("BuyReturn"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
#if($globals.getVersion()==3)
<td width="20%" align="right"><a href="/ReportDataAction.do?reportNumber=ReportBuyReturn&detTable_list=tblBuyOutStock&BillDate1=$!startDate&BillDate2=$!endDate&DeptFullName=$!deptName&LinkType=@URL&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("BuyReturnPieces"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
#end
<td width="20%" align="right">#if($globals.getVersion()==3)<a href="/ReportDataAction.do?reportNumber=ReportBuyReturn&detTable_list=tblBuyOutStock&BillDate1=$!startDate&BillDate2=$!endDate&DeptFullName=$!deptName&LinkType=@URL&queryChannel=normal" target="_blank">#end$!globals.newFormatNumber($!values.get("BuyRtnAmount"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
<td width="20%" align="right">#if($globals.getVersion()==3)<a href="/ReportDataAction.do?reportNumber=ReportBuyReturn&detTable_list=tblBuyOutStock&BillDate1=$!startDate&BillDate2=$!endDate&DeptFullName=$!deptName&LinkType=@URL&queryChannel=normal" 

target="_blank">#end&nbsp;</a></td>
</tr>
<tr align="right"><td height="2" colspan="5" class="bosstdbg3">							</td>
</tr>
<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
<td align="right" class="bosstdbg2">$text.get("report.lb.expend")：</td>
<td width="20%" align="right"><a href="/ReportDataAction.do?reportNumber=ReportSalesTotalGrossProfit&detTable_list=tblSalesOutStock&startDate=$!startDate&endDate=$!endDate&DeptFullName=$!deptName&LinkType=@URL&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("SalesQty"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
#if($globals.getVersion()==3)
<td width="20%" align="right"><a href="/ReportDataAction.do?reportNumber=ReportSalesOutStockDet&StartDate=$!startDate&StopDate=$!endDate&DeptFullName=$!deptName&LinkType=@URL&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("SalesPieces"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
#end
<td width="20%" align="right">#if($globals.getVersion()==3)<a href="/ReportDataAction.do?reportNumber=ReportSalesOutStockDet&StartDate=$!startDate&StopDate=$!endDate&DeptFullName=$!deptName&LinkType=@URL&queryChannel=normal" target="_blank">#end$!globals.newFormatNumber($!values.get("SalesAmount"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
<td width="20%" align="right">#if($globals.getVersion()==3)<a href="/ReportDataAction.do?reportNumber=ReportSalesGrossProfit&startDate=$!startDate&endDate=$!endDate&department=$!deptName&ModelTypy=tblSalesOutStock&LinkType=@URL&queryChannel=normal&ModelName=$!globals.encode("$text.get('SalesReturnStock')")&LinkType=@URL:" target="_blank">#end$!globals.newFormatNumber($!values.get("SalesGrossProfit"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
</tr>

<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
<td align="right" class="bosstdbg2">$text.get("report.lb.expendReturn")：</td>
<td width="20%" align="right"><a href="/ReportDataAction.do?reportNumber=ReportSalesTotalGrossProfit&detTable_list=tblSalesOutStock&startDate=$!startDate&endDate=$!endDate&DeptFullName=$!deptName&LinkType=@URL&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("SalesReturn"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
#if($globals.getVersion()==3)
<td width="20%" align="right"><a href="/ReportDataAction.do?reportNumber=ReportSalesReturnStockDet&BillDate1=$!startDate&BillDate2=$!endDate&queryChannel=normal" 

target="_blank">$!globals.newFormatNumber($!values.get("SalesReturnPieces"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
#end
<td width="20%" align="right">#if($globals.getVersion()==3)<a href="/ReportDataAction.do?reportNumber=ReportSalesReturnStockDet&BillDate1=$!startDate&BillDate2=$!endDate&DepartmentCode=$!deptName&LinkType=@URL&queryChannel=normal" 

target="_blank">#end$!values.get("SalesReturnAmount")&nbsp;</a></td>
<td width="20%" align="right">#if($globals.getVersion()==3)<a href="/ReportDataAction.do?reportNumber=ReportSalesGrossProfit&startDate=$!startDate&endDate=$!endDate&department=$!deptName&ModelTypy=tblSalesReturnStock&LinkType=@URL&queryChannel=normal&ModelName=$globals.encode("$text.get('moduleFlow.lb.SalesReturnStock')")&LinkType=@URL:" target="_blank">#end$!globals.newFormatNumber($!values.get("SalesRtnGrossProfit"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)

&nbsp;</a></td>
</tr>

</tbody>
</table></td>
    <td align="left" valign="top"><table width="100%" height="160" border="0" cellpadding="0" align="center" cellspacing="0" class="bosstable" id="tblSort" name="table">
<tbody>
<tr>
<td class="bosstdbg1" height="30" colspan="4">$text.get("report.lb.buyOrderExec")</td>
</tr>
<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
  <td align="center" class="bosstdbg2">&nbsp;</td>
  <td align="center" class="bosstdbg2">$text.get("report.lb.Qty")</td>

  </tr>
<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
<td align="right" class="bosstdbg2">$text.get("report.lb.StockOrdered")：</td>
<td align="right"><a href="/ReportDataAction.do?reportNumber=tblBuyOrderDet&detTable_list=tblBuyOrder&StartDate=$!startDate&StopDate=$!endDate&DeptFullName=$!deptName&LinkType=@URL&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("BuyOrder"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>

</tr>
<tr><td align="right" class="bosstdbg2">$text.get("report.lb.Inreported")：</td>
<td align="right"><a href="/ReportDataAction.do?reportNumber=tblBuyOrderDet&detTable_list=tblBuyOrder&StartDate=$!startDate&StopDate=$!endDate&DeptFullName=$!deptName&LinkType=@URL&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("BuyIn"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)

&nbsp;</a></td>

</tr>

<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
<td align="right" class="bosstdbg2">$text.get("report.lb.NoInReport")：</td>
<td align="right" class="tdr"><a href="/ReportDataAction.do?reportNumber=tblBuyOrderDet&detTable_list=tblBuyOrder&StartDate=$!startDate&StopDate=$!endDate&DeptFullName=$!deptName&LinkType=@URL&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("BuyNoIn"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>

</tr>
<tr align="right"><td height="2" colspan="3" class="bosstdbg3">							</td>
</tr>
<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
<td align="right" class="bosstdbg2">$text.get("report.lb.expended")：</td>
<td align="right" class="tdr"><a href="/ReportDataAction.do?reportNumber=ReportSalesOrderDet&detTable_list=tblSalesOrder&detTable_list=tblSalesOrder&StartDate=$!startDate&StopDate=$!endDate&DeptFullName=$!deptName&LinkType=@URL&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("SalesOrder"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
</tr>
<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
<td align="right" class="bosstdbg2">$text.get("report.lb.outReported")：</td>
<td align="right" class="tdr"><a href="/ReportDataAction.do?reportNumber=ReportSalesOrderDet&detTable_list=tblSalesOrder&StartDate=$!startDate&StopDate=$!endDate&DeptFullName=$!deptName&LinkType=@URL&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("SalesOut"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
</tr>
<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);" target="_blank">
<td align="right" class="bosstdbg2">$text.get("report.lb.NoOutReport")：</td>
<td align="right" class="tdr"><a href="/ReportDataAction.do?reportNumber=ReportSalesOrderDet&detTable_list=tblSalesOrder&StartDate=$!startDate&StopDate=$!endDate&DeptFullName=$!deptName&LinkType=@URL&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("SalesNoOut"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
</tr>
<td height="2"></tbody>
</table></td>
    <td align="left" valign="top"><table width="100%" height="160"  border="0" align="center" cellpadding="0" cellspacing="0" class="bosstable" id="tblSort" name="table">
<tbody>
<tr>
<td class="bosstdbg1" height="30" colspan="2">$text.get("report.lb.stockInTrade")</td>
</tr>
<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
<td width="46%" align="right" class="bosstdbg2">$text.get("report.lb.LastStorage")：</td>
<td width="54%" align="right">$!globals.newFormatNumber($!values.get("LastQty"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</td>
</tr>
<tr>
  <td height="28" align="right" class="bosstdbg2">$text.get("report.lb.NowStorage")：</td>
  <td align="right"><a href="/ReportDataAction.do?reportNumber=ViewStoreDetail&lastQty=0&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("NowQty"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>

  </tr>

<tr>
  <td height="28" align="right" class="bosstdbg2">$text.get("report.lb.StorageBalance")：</td>
  <td align="right" class="tdr">$!globals.newFormatNumber($!values.get("LastAmount"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
</tr>
<tr>
  <td height="28" align="right" class="bosstdbg2">$text.get("report.lb.NowStorageBalance")：</td>
  <td align="right"><a href="/ReportDataAction.do?reportNumber=ViewStoreDetail&lastQty=0&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("NowAmount"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
</tr>

</table>
<tr>
</td>
  
<table width="100%" border="0" cellspacing="0" class="boss_ta"  cellpadding="0">
</tr>
  <tr class="bosstitle">
    <td class="tdr">$text.get("report.lb.fareStatus")</td>
    <td class="tdr">$text.get("reprt.lb.fareAnaly")</td>
    <td colspan="3">$text.get("report.lb.companyDynamic")</td>
    </tr>
  <tr>
    <td align="left" valign="top">
	  <table width="100%" border="0" height="160" cellpadding="0" align="center" cellspacing="0" class="bosstable" id="tblSort" name="table">
<tbody>
<tr align="right"><td height="2" colspan="3" class="bosstdbg3">							</td>
</tr>
  <td height="28" colspan="2" align="left" class="bosstdbg1">$text.get("report.lb.FinancingFlow")</td>
  </tr>
<tr>
  <td height="28" align="right" class="bosstdbg2">$text.get("report.lb.InFlow")：</td>
  <td align="right" class="tdr"><a href="/ReportDataAction.do?reportNumber=ReportCashAndBankFlow&StartDate=$!startDate&EndDate=$!endDate&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("AccInFlow"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
</tr>
<tr>
  <td height="28" align="right" class="bosstdbg2">$text.get("report.lb.OutFlow")：</td>
  <td align="right" class="tdr"><a href="/ReportDataAction.do?reportNumber=ReportCashAndBankFlow&StartDate=$!startDate&EndDate=$!endDate&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("AccOutFlow"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
</tr>
<tr><td height="28" align="right" class="bosstdbg2">$text.get("report.lb.NetIncome")：</td>
<td align="right" class="tdr"><a href="/ReportDataAction.do?reportNumber=ReportCashAndBankFlow&StartDate=$!startDate&EndDate=$!endDate&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("AccNetIncome"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
</tr>
</tbody>
</table>
</td>
	<td align="left" valign="top"><table width="100%" height="196"  border="0" align="center" cellpadding="0" cellspacing="0" class="bosstable" id="tblSort" name="table">
<tbody>
<tr>
<td class="bosstdbg1" height="30" colspan="2">$text.get("report.lb.CurrentAccount")</td>
</tr>
<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
<td width="50%" align="right" class="bosstdbg2">$text.get("report.lb.LastAccountPayable")：</td>
<td width="50%"  align="right"><a href="/ReportDataAction.do?reportNumber=RptCompanyG&#if($globals.getVersion()==8||$globals.getVersion()==12)startDate1#{else}startDate#end=$!startDate&#if($globals.getVersion()==8||$globals.getVersion()==12)endDate2#{else}endDate#end=$!endDate&ClientFlag=1&CompanyCode=&hide_CompanyCode=&accAmt=0&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("PayBegin"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
</tr>
<td width="50%" align="right" class="bosstdbg2">$text.get("report.lb.PayableBalance")：</td>
<td width="50%" align="right"><a href="/ReportDataAction.do?reportNumber=RptCompanyG&#if($globals.getVersion()==8||$globals.getVersion()==12)startDate1#{else}startDate#end=$!startDate&#if($globals.getVersion()==8||$globals.getVersion()==12)endDate2#{else}endDate#end=$!endDate&ClientFlag=1&CompanyCode=&hide_CompanyCode=&accAmt=0&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("PayEnd"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
</tr>
<tr align="right"><td height="5" colspan="2" class="bosstdbg3">							</td>
</tr>
<tr >
  <td width="50%" align="right" class="bosstdbg2">$text.get("report.lb.LastAccountReceivable")：</td>
  <td width="50%" align="right" ><a href="/ReportDataAction.do?reportNumber=RptCompanyC&startDate=$!startDate&endDate=$!endDate&ClientFlag=2&CompanyCode=&hide_CompanyCode=&accAmt=0&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("ReceiveBegin"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
</tr>
<tr >
<td width="50%" align="right" class="bosstdbg2">$text.get("report.lb.ReceivableBalance")：</td>
<td width="50%" align="right" ><a href="/ReportDataAction.do?reportNumber=RptCompanyC&startDate=$!startDate&endDate=$!endDate&ClientFlag=2&CompanyCode=&hide_CompanyCode=&accAmt=0&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("ReceiveEnd"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
</tr>
<tr>
  <td height="32">
<td height="32"></tbody>
<td width="50%" align="right"><a href=""></a></td>
</table></td>
    <td align="right" valign="top"><table width="100%" height="160" border="0" cellpadding="0" align="center" cellspacing="0" class="bosstable" id="tblSort" name="table">
<tbody>
<tr>
<td class="bosstdbg1" height="30" colspan="2">$text.get("common.lb.report.InOutMoney")</td>
</tr>
<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
<td align="right" class="bosstdbg2">$text.get("common.lb.report.expendIncome")：</td>
<td align="right" class="tdr"><a href="/ReportDataAction.do?reportNumber=ReportSalesTotalGrossProfit&startDate=$!startDate&endDate=$!endDate&queryChannel=normal" target="_blank">$!globals.newFormatNumber($!values.get("SalesInCome"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
</tr>
<td align="right" class="bosstdbg2">$text.get("common.lb.report.OtherIncome")：</td>
<td align="right" class="tdr"><a href="/ReportDataAction.do?reportNumber=tblAccMainDet&amp;AccCode=$values.get("otherInAccCode")&hide_AccCode=$values.get("otherInhideAccCode")&tblAccTypeInfo.AccNumber=$values.get("otherInAccNumber")&tblAccTypeInfo.AccName=$values.get("otherInAccName")&BillDateS=$!startDate&BillDateE=$!endDate&LinkType=@URL&queryChannel=normal&DeptFullName=$values.get("deptName")" target="_blank">$!globals.newFormatNumber($!values.get("OrtherInCome"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
</tr>
<tr align="right" class="tdr"><td height="5" colspan="2" class="bosstdbg3">							</td>
</tr>
<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);" target="_blank">
<td align="right" class="bosstdbg2">$text.get("common.lb.report.commonCharge")：</td>
<td align="right" class="tdr"><a href="/ReportDataAction.do?reportNumber=tblAccMainDet&amp;AccCode=$values.get("feeAccCode")&hide_AccCode=$values.get("feehideAccCode")&tblAccTypeInfo.AccNumber=$values.get("feeAccNumber")&tblAccTypeInfo.AccName=$values.get("feeAccName")&BillDateS=$!startDate&BillDateE=$!endDate&LinkType=@URL&queryChannel=normal&DeptFullName=$values.get("deptName")" target="_blank">$!globals.newFormatNumber($!values.get("ComExpensed"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
</tr>
<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
<td align="right" class="bosstdbg2">$text.get("common.lb.report.OtherCharge")：</td>
<td align="right" class="tdr"><a href="/ReportDataAction.do?reportNumber=tblAccMainDet&amp;AccCode=$values.get("otherFeeAccCode")&hide_AccCode=$values.get("otherFeehideAccCode")&tblAccTypeInfo.AccNumber=$values.get("otherFeeAccNumber")&tblAccTypeInfo.AccName=$values.get("otherFeeAccName")&BillDateS=$!startDate&BillDateE=$!endDate&LinkType=@URL&queryChannel=normal&DeptFullName=$values.get("deptName")" target="_blank">$!globals.newFormatNumber($!values.get("OrtherExpensed"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</a></td>
</tr>
<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
<td align="right" class="bosstdbg2">$text.get("common.lb.report.IncomeCharge")：</td>
<td align="right" class="tdr">$!globals.newFormatNumber($!values.get("InComeExpensed"),false,false,$!globals.getSysIntswitch(),'tblBuyInStock',"TotalTaxAmount",true)&nbsp;</td>
</tr>
</tbody>
</table>

</div>
	</body>
</html>
 

