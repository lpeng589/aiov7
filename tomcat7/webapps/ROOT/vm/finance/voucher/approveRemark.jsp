<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>AIO</title>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript"> 
	function saves(){
		form.submit();
	}
	function reloads(){
		parent.location.reload();
	}
</script>
<style type="text/css">
</style>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form action="/VoucherManage.do" method="post" id="form" name="form" target="formFrame">
<input type="hidden" id="optype" name="optype" value="dealapprov"/>
#if($globals.isExist($voucherId,';'))
#foreach($voucher in $globals.strSplit($voucherId,';'))
	<input type="hidden" id="keyId" name="keyId" value="$voucher"/>
#end
#else
	<input type="hidden" id="keyId" name="keyId" value="$voucherId"/>
#end
<input type="hidden" name="isList" id="isList" value="$!isList"/>
<input type="hidden" name="dealType" id="dealType" value="noPass"/>
<table>
<tr>
	<td style="font-size: 12px;text-align: left;">批注：<span style="color: red;">*</span></td>
	<td><textarea rows="5" cols="40" name="remark" id="remark"></textarea></td>
</tr>
</table>
</form>
</body>
</html>

