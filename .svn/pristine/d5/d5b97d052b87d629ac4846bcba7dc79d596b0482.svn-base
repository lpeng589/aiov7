<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
<meta name="renderer" content="webkit">
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript">

putValidateItem("unitName","$text.get("unit.lb.name")","any","",false,0,10);
putValidateItem("remark","$text.get("unit.lb.remark")","any","",true,1,99 );

function beforSubmit(form){   
	if(!validate(form))return false;
	disableForm(form);
	return true;
}	

</script>
</head>
<body onLoad="showtable('tblSort');showStatus();">
<form action="/Client.do?txtLocal=$!txtLocal&txtRemote=$!txtRemote" method="post" name="form">

<div>
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	#if($rs.retVal.toString()=="[]") <table id="tblSort">	<label style="color:red;"> <h3>$text.get("call.help.info1")</h3></label></table>
#else
	<div class="HeadingTitlesmall">

	#if($!globals.get($rs.retVal.get(0),10)!=1)$text.get("call.lb.client") #end
	#if($!globals.get($rs.retVal.get(0),10)==2)$text.get("call.lb.plusSign")#end
	#if($!globals.get($rs.retVal.get(0),10)!=0)$text.get("call.lb.afford")#end
	$text.get("call.lb.data")  	
	</div>
	<ul class="HeadingButton_Pop-up">
	<li>$text.get("call.lb.tel")<label id="txtRemote">$!txtRemote</label>&nbsp;</li>
	</ul>
<div class="listRange_Pop-up_scroll">

<table border="0" align="center" width="100%" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
<thead>

	<tr>
		<td width="70">$text.get("call.lb.billdate")</td>
		<td width="100">$text.get("call.lb.billId")</td>
		<td width="100">$text.get("call.lb.goodsName")</td>
		<td width="60">$text.get("call.lb.spec")</td>
		<td width="60">$text.get("call.lb.unit")</td>
		<td >
		$text.get("call.lb.count")#if($!globals.get($rs.retVal.get(0),10)!=1)$text.get("call.lb.clientNote") #end
		#if($!globals.get($rs.retVal.get(0),10)==1)$text.get("call.lb.affordNote") #end
		</td>
		<td >		
		$text.get("call.lb.price")#if($!globals.get($rs.retVal.get(0),10)!=1)$text.get("call.lb.clientNote") #end
		#if($!globals.get($rs.retVal.get(0),10)==1)$text.get("call.lb.affordNote") #end
		</td>
		#if($!globals.get($rs.retVal.get(0),10)==2)
		<td>$text.get("call.lb.count")$text.get("call.lb.affordNote")</td>
		<td>$text.get("call.lb.price")$text.get("call.lb.affordNote")</td>
		#end
		<td width="60">$text.get("call.lb.transactor")</td>
	</tr>
	</thead>
			<tbody> 
#foreach ($row in $rs.retVal )
	<tr>
		
		<td align="center">$!row.BillDate &nbsp;</td>
		<td align="center">$!row.BillNo &nbsp;</td>
		<td align="center">$!row.goodsFullName &nbsp;</td>
		<td align="center">$!row.goodsSpec &nbsp;</td>
		<td align="center">$!row.unitName &nbsp;</td>
		<td align="center">
		#if($!row.ClientFlag!=1)$!row.InstoreQty #end &nbsp;			
		#if($!row.ClientFlag==1)$!row.OutstoreQty #end &nbsp;
		</td>
		<td align="center"> 
		#if($!row.ClientFlag!=1)$!row.InstorePrice #end &nbsp;
		#if($!row.ClientFlag==1)$!row.OutstorePrice#end &nbsp;
		</td>
		#if($!globals.get($rs.retVal.get(0),10)==2)
		<td>$!globals.get($row,6)&nbsp;</td>
		<td>$!globals.get($row,8)&nbsp;</td>
		#end
		<td align="center">$!row.empFullName &nbsp;</td>
	
#end
</tr>

</tbody>

	</table>
	<br>
<div class="listRange_pagebar"> $!pageBar </div>
</div>
#end
</form>
</body>
</html>
