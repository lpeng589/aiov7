<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>单据编号规则</title>
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<style type="text/css">
	.td_right{
		text-align: right;
		padding-right: 5px;
		vertical-align: bottom;
	}
	input[type='text']{
		border-width:0px;
		border-bottom:#1e95e9 1px solid;
		padding: 4px;
	}
</style>
<script type="text/javascript">
	/*var strvalue = new Array('{date yy}','{date MM}','{date dd}','{date yyyyMMdd}','{date yyyy-MM-dd}','{date yyyy/MM/dd}',
				'{login.getempFullName}','{login.getname}','{login.getdepartCode}','{login.getdepartmentName}','{serial 0000}','{serial 0,000}');
	var strname = new Array('年年','月月','日日','年年年年月月日日','年年年年-月月-日日','年年年年/月月/日日','用户全称','登录名','部门编号','部门名称','0000','0,000');
	*/
	$(document).ready(function (){
		$("input").attr("readonly","readonly");
		//$("#start").html(new Number($!bean.start)+1);
	});
</script>
<style type="text/css">
	.inpsearch_btn{
		background: url(/style/images/client/bg.gif) no-repeat 1000px 1000px;
		width:20px;
		background-position: right -413px;
		height: 20px;
		color:#fff;
		display: block;
		text-indent: -999em;
		overflow: hidden;
		font-size: 1px;
	}
</style>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" scope="request" id="form" name="form" action="/BillNo.do" target="formFrame">
<div class="boxbg2 subbox_w660" style="width:100%">
<div class="subbox cf">
<div class="operate operate2" id="handle">
  <ul>
  	<li class="sel">单据编号规则详情</li>
  </ul>
  <div class="closel"></div>
  </div>
<div class="listRange_frame" id="listRange_frame" style="padding-top: 10px;padding-left: 10px;width:98%;">
<div id="conter" style="border: #81b2e3 1px solid;">
	<table border="0" width="100%" style="margin: 5px 5px 5px 5px;">
	<tr><td width="140px;" class="td_right">
		模块标识：</td><td>
		$!bean.key
		</td></tr>
	<tr><td class="td_right">模块名称：</td><td>$!bean.billName</td></tr>
	<tr id='noRole'><td style="text-align: right;padding-right: 5px;">单据编号规则：</td><td id="copyTd">
		$!bean.explain
	</td></tr>
	<tr><td></td><td>
	</tr></tr>
	<tr><td class="td_right">起始流水号：</td><td><span id="start">$!bean.start</span></td></tr>
	<tr><td class="td_right">流水号增量：</td><td>$!bean.step</td></tr>
	<tr><td class="td_right">重置周期：</td><td>
		#if("$!bean.reset"=="1")
			年

		#elseif("$!bean.reset"=="2")
			月

		#elseif("$!bean.reset"=="5")
			日

		#end
	</td></tr>
	<tr><td class="td_right">经手人默认为登录者：</td>
		<td>
			#if("$!bean.isDefaultLoginPerson"=="0") 是 #else 否 #end
		</td>
	</tr>
	<tr><td class="td_right">启用补号：</td><td>
		#if("$!bean.isfillback"=="true" && "$!bean.isAddbeform"=="false") 保存时生成编号
		#elseif("$!bean.isfillback"=="true" && "$!bean.isAddbeform"=="true") 补号 
		#else 新增时生成编号 
		#end
	</td></tr>
	</table>
</div>
</div>
</form>
</body>
</html>

