<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("iniSet.lb.title")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript">
var FixRate="$!FixRate";

function iniValueChange(vars)
{		
	if(isNaN(vars.value)||vars.value.length==0){vars.value=0; return false;}

	var mi =document.getElementsByName(vars.name);
	
	var pos;
	for(i=0;i<mi.length;i++){
		if(mi[i] == vars){
			pos = i;	
		}	
	}
	
	var balance=document.getElementsByName("balance")[pos];	
	var year=document.getElementsByName("year")[pos];
	if(vars.name == "year"){		
		year.value=f(parseFloat(vars.value),$globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
	}
	var borrow=document.getElementsByName("borrow")[pos];
	if(vars.name == "borrow"){		
		borrow.value=f(parseFloat(vars.value),$globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
	}
	var lend=document.getElementsByName("lend")[pos];
	if(vars.name == "lend"){		
		lend.value=f(parseFloat(vars.value),$globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
	}
	var curbalance=document.getElementsByName("curbalance")[pos];
	var curyear=document.getElementsByName("curyear")[pos];
	var curborrow=document.getElementsByName("curborrow")[pos];
	var curlend=document.getElementsByName("curlend")[pos];
	
	var currencyRate=document.getElementsByName("currencyRate")[pos];
	
	if(vars.value.length==0){
		vars.value=0;
	}
	
	if(vars.name=="currencyRate"){
		balance.value=curbalance.value*currencyRate.value;
		borrow.value=curborrow.value*currencyRate.value;
		lend.value=curlend.value*currencyRate.value;
		year.value=curyear.value*currencyRate.value;
	}
	
	//余额计算汇率
	if(vars.name=="curbalance"){
		balance.value=curbalance.value*currencyRate.value;
		balance.value=formatNumber(balance.value);
	}
	
	//借计算汇率	
	if(vars.name=="curborrow"){
		borrow.value=curborrow.value*currencyRate.value;
		borrow.value=formatNumber(borrow.value);
	}
	
	//贷计算汇率	
	if(vars.name=="curlend"){
		lend.value=curlend.value*currencyRate.value;
		lend.value=formatNumber(lend.value);
	}
	
	//年初值计算汇率	
	if(vars.name=="curyear"){
		year.value=curyear.value*currencyRate.value;
		year.value=formatNumber(year.value);
	}
	
	vars.value=f(vars.value, $globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
	if("$!type"=="receive"||"$!type"=="prePay")
	{
		balance.value=f(parseFloat(year.value)+parseFloat(borrow.value)-parseFloat(lend.value),$globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
		#if($!OpenFCurrency=="true")
		curbalance.value=parseFloat(curyear.value)+parseFloat(curborrow.value)-parseFloat(curlend.value);
		#end
	}else if("$!type"=="pay"||"$!type"=="preReceive")
	{
		balance.value=f(parseFloat(year.value)+parseFloat(lend.value)-parseFloat(borrow.value),$globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
		#if($!OpenFCurrency=="true")
		curbalance.value=parseFloat(curyear.value)+parseFloat(curlend.value)-parseFloat(curborrow.value);
		#end
	}
	
	
}

function iniBalanceChange(vars)
{	
	if(isNaN(vars.value)||vars.value.length==0){vars.value=0;}

	var mi =document.getElementsByName(vars.name);
	
	var pos;
	for(i=0;i<mi.length;i++){
		if(mi[i] == vars){
			pos = i;	
		}	
	}
	
	var balance=document.getElementsByName("balance")[pos];
	var year=document.getElementsByName("year")[pos];
	var borrow=document.getElementsByName("borrow")[pos];
	var lend=document.getElementsByName("lend")[pos];
	var curbalance=document.getElementsByName("curbalance")[pos];
	var curyear=document.getElementsByName("curyear")[pos];
	var curborrow=document.getElementsByName("curborrow")[pos];
	var curlend=document.getElementsByName("curlend")[pos];
	
	var currencyRate=document.getElementsByName("currencyRate")[pos];
	
	if(vars.value.length==0){
		vars.value=0;
	}
	
	if(vars.name=="currencyRate"){
		balance.value=curbalance.value*currencyRate.value;
		borrow.value=curborrow.value*currencyRate.value;
		lend.value=curlend.value*currencyRate.value;
		year.value=curyear.value*currencyRate.value;
	}
	
	//余额计算汇率
	if(vars.name=="curbalance"){
		balance.value=curbalance.value*currencyRate.value;
		balance.value=formatNumber(balance.value);
	}
	
	//借计算汇率	
	if(vars.name=="curborrow"){
		borrow.value=curborrow.value*currencyRate.value;
		borrow.value=formatNumber(borrow.value);
	}
	
	//贷计算汇率	
	if(vars.name=="curlend"){
		lend.value=curlend.value*currencyRate.value;
		lend.value=formatNumber(lend.value);
	}
	
	//年初值计算汇率	
	if(vars.name=="curyear"){
		year.value=curyear.value*currencyRate.value;
		year.value=formatNumber(year.value);
	}
	vars.value=f(vars.value, $globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
	if("$!type"=="receive"||"$!type"=="prePay")
	{
		year.value=f(parseFloat(balance.value)-parseFloat(borrow.value)+parseFloat(lend.value),$globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
		curyear.value=f(parseFloat(curbalance.value)-parseFloat(curborrow.value)+parseFloat(curlend.value),$globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
	}else if("$!type"=="pay"||"$!type"=="preReceive")
	{
		year.value=f(parseFloat(balance.value)-parseFloat(lend.value)+parseFloat(borrow.value),$globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
		curyear.value=f(parseFloat(curbalance.value)-parseFloat(curlend.value)+parseFloat(curborrow.value),f$globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
	}
	
	
}

function beforSubmit(form){   
	if(!validateDigit("balance","$text.get("iniComany.lb.balance")",$globals.getFieldDigits("tblCompanyTotal","PayTotalRemain"),false))return false;
	form.winCurIndex.value="$!winCurIndex";
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}	
	document.form.submit();
}	
</script>
</head>

<body onLoad="showStatus();">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" target="formFrame" action="/IniCompanyQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
 <input type="hidden" name="type" value="$!type">
 <input type="hidden" name="operType" value="edit">
 <input type="hidden" name="classCode" value="$!companyCode">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("iniCompany.lb.head")_$text.get($!iniTypeText)</div>
	<ul class="HeadingButton">
	#if($globals.getMOperation().update())
	<li><button type="button" onClick="beforSubmit(document.form);" class="b2">$text.get("common.lb.save")</button></li>	
	#end
	#if($globals.getMOperation().query())
	#set($strCompanyCode=$!strCompanyCode+"00001")
	<li><button type="button" onClick="location.href='/IniCompanyQueryAction.do?type=$!type&companyCode=$!companyCode&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button></li>	
	#end
	
	</ul>
</div>
<div id="listRange_id">
		<div class="scroll_function_small_a" id="conter">
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
			<thead>
				<tr>
					<td width="30" align="center">$text.get("iniAcc.lb.iniAcc.lb.sequencenumber")</td>
					<td width="150" align="center">$text.get("company.lb.no")</td>
					<td width="150" align="center">$text.get("company.lb.name")</td>
					#if($!SimpleAccFlag!=1)
					<td width="120" align="center">$text.get("iniCompany.lb.year")</td>					
					<td width="120" align="center">$text.get("iniCompany.lb.borrow")</td>
					<td width="120" align="center">$text.get("iniCompany.lb.lend")</td>
					#end
					<td width="120" align="center">$text.get("iniComany.lb.balance")</td>				
					#if($!OpenFCurrency=="true")		
					<td width="120" align="center">$text.get("iniComany.lb.curID")</td>
					<td width="120" align="center">$text.get("iniComany.lb.curRate")</td>
					#if($!SimpleAccFlag!=1)
					<td width="120" align="center">$text.get("iniCompany.lb.curyear")</td>					
					<td width="120" align="center">$text.get("iniCompany.lb.curborrow")</td>
					<td width="120" align="center">$text.get("iniCompany.lb.curlend")</td>
					#end
					<td width="120" align="center">$text.get("iniComany.lb.curbalance")</td>
							
					#end
				</tr>			
			</thead>
			<tbody> #set($count=0)
					#foreach ($row in $result )
					 #set($count=$count+1)
				<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				<td align="left">$!count</td>
				<td align="left">$globals.get($row,1)</td>
				<td align="left">$globals.get($row,2)<input type="hidden" name="keyId" value="$!globals.get($row,0)"></td>
				#if($!SimpleAccFlag!=1)
				<td>
				<input type="text" onClick="select()" onKeyDown="if(event.keyCode==13)event.keyCode=9;"  onBlur="if(isNaN(this.value)){ alert('$text.get("common.validate.error.number")'); this.focus(); return false; }"  value="$!globals.get($row,7)" onChange="iniValueChange(this);"  size="15" name="year"></td>
				<td><input type="text" onClick="select()" onKeyDown="if(event.keyCode==13)event.keyCode=9;" onBlur="if(isNaN(this.value)){ alert('$text.get("common.validate.error.number")'); this.focus(); return false; }" onChange="iniValueChange(this);" value="$!globals.get($row,8)"  size="15" name="borrow"></td>
				<td><input type="text" onClick="select()" onKeyDown="if(event.keyCode==13)event.keyCode=9;" onBlur="if(isNaN(this.value)){ alert('$text.get("common.validate.error.number")'); this.focus(); return false; }"  onChange="iniValueChange(this);" value="$!globals.get($row,9)"  size="15" name="lend"></td>			
				#end	
				<td><input type="text" onClick="select()" onKeyDown="if(event.keyCode==13)event.keyCode=9;" onBlur="if(isNaN(this.value)){ alert('$text.get("common.validate.error.number")'); this.focus(); return false; }"  onchange="iniBalanceChange(this);" value="$!globals.get($row,10)"   size="15" name="balance"></td>
				#if($!OpenFCurrency=="true")
				<td><input type="hidden" name="currencyID" value="$!globals.get($row,4)">
				#if($!globals.get($row,5)=="&nbsp;")$!baseCur #else $!globals.get($row,5) #end
				 </td>
				<td><input type="hidden" name="currencyRate" value="$!globals.get($row,6)">$!globals.get($row,6)</td>
				#if($!SimpleAccFlag!=1)
				<td>
				<input type="text" onKeyDown="if(event.keyCode==13)event.keyCode=9;" onBlur="if(isNaN(this.value)){ alert('$text.get("common.validate.error.number")'); this.focus(); return false; }"  value="$!globals.get($row,11)" onChange="iniValueChange(this);"  size="15" name="curyear"></td>
				<td><input type="text" onKeyDown="if(event.keyCode==13)event.keyCode=9;" onBlur="if(isNaN(this.value)){ alert('$text.get("common.validate.error.number")'); this.focus(); return false; }" onChange="iniValueChange(this);" value="$!globals.get($row,12)"  size="15" name="curborrow"></td>
				<td><input type="text" onKeyDown="if(event.keyCode==13)event.keyCode=9;" onBlur="if(isNaN(this.value)){ alert('$text.get("common.validate.error.number")'); this.focus(); return false; }"  onChange="iniValueChange(this);" value="$!globals.get($row,13)"  size="15" name="curlend"></td>			
				#end	
				<td><input type="text"  onKeyDown="if(event.keyCode==13)event.keyCode=9;" onBlur="if(isNaN(this.value)){ alert('$text.get("common.validate.error.number")'); this.focus(); return false; }"  value="$!globals.get($row,14)" onChange="iniValueChange(this);"  size="15" name="curbalance"></td>
				#end
				</tr>
				#end
					</tbody>
		  </table>		
	</div>
<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-92;
oDiv.style.height=sHeight+"px";
//]]>
</script>
</div>
</form>
</body>
</html>
