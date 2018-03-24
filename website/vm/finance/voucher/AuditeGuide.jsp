<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>AIO</title>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript"> 
	
	/* 时间选择*/
	function openInputDate(obj){
		WdatePicker({lang:'zh_CN'});
	}
	
	function saves(){
		form.submit();
	}
	
	function showtime(){
		jQuery("#vouchertime").show();
	}
	
	function hidetime(){
		jQuery("#vouchertime").hide();
	}
	
	function showperiod(){
		jQuery("#voucherPeriodYear").show();
		jQuery("#voucherPeriod").show();
	}
	
	function hideperiod(){
		jQuery("#voucherPeriodYear").hide();
		jQuery("#voucherPeriod").hide();
	}
	
	function reloads(){
		parent.location.reload();
	}
</script>
<style type="text/css">
	td{
		font-size: 12px;
	}
	table tr td{
		border: 1px solid #b4b4b4;
	}
</style>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form action="/VoucherManage.do" method="post" id="form" name="form" target="formFrame">
<input type="hidden" id="optype" name="optype" value="batchAudite"/>
<div style="margin: 20px 0px 0px 30px;">
<table cellpadding="0" cellspacing="0" style="border-collapse:collapse;width: 400px;height: 170px;">
<tr>
	<td><div><input type="radio" name="voucherarea" id="voucherarea" value="1" checked onclick="hidetime();hideperiod();"/>全部未审核凭证<span style="padding-left: 50px;"/></div>
	<div style="height: 24px;"><input type="radio" name="voucherarea" id="voucherarea" value="2" onclick="showtime();hideperiod();" />指定日期之前的凭证&nbsp;&nbsp;&nbsp;
	<input type="text" id="vouchertime" name="vouchertime" onclick="openInputDate(this)" style="display: none;width: 100px;"/></div>
	<div style="height: 24px;"><input type="radio" name="voucherarea" id="voucherarea" value="3" onclick="showperiod();hidetime();" />指定期间的凭证&nbsp;&nbsp;&nbsp;
	<input type="text" id="voucherPeriodYear" name="voucherPeriodYear" style="display: none;width: 50px;" value="$!accPeriodBean.getAccYear()"/><input type="text" id="voucherPeriod" name="voucherPeriod" style="display: none;width: 50px;" value="$!accPeriodBean.getAccPeriod()"/></div></td>
</tr>
</table>
</div>
</form>
</body>
</html>

