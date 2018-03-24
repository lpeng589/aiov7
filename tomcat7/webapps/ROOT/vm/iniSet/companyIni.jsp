<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("iniSet.lb.title")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript">
  function editPre(){
  	if(!isChecked("keyId")){		
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	form.operation.value="$globals.getOP("OP_UPDATE_PREPARE")";
	form.winCurIndex.value="$!winCurIndex";
	return true;
  }
</script>
</head>
<body onLoad="showtable('tblSort');showStatus();clearForm(form) ">
<form  method="post" scope="request" name="form" action="/IniCompanyQueryAction.do">
        <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
        <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
        <input type="hidden" name="type" value="$!type">
		<input type="hidden" name="companyCode" value="$!companyCode">
		 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("iniCompany.lb.head")_$text.get($!iniTypeText)</div>
<ul class="HeadingButton">
	#if($globals.getMOperation().update())
	<li><button type="submit" onClick="return editPre();" class="b2">$text.get("common.lb.edit")</button></li>	
	#end
	#if($globals.getMOperation().query())
	<li>
		<button name="Submit" type="submit" onClick="forms[0].operation.value=$globals.getOP("OP_QUERY");return beforeBtnQuery();" class="b2">$text.get("common.lb.query")</button>
	</li>
	#end
	#if($globals.getMOperation().query() && "$!companyCode"!="")
	<li><button type="button" onClick="location.href='/IniCompanyQueryAction.do?type=$!type&companyCode=$!companyCode&winCurIndex=$!winCurIndex&operation=$globals.getOP("OP_QUERY")&listType=back'" class="b2">$text.get("common.lb.back")</button></li>
	#end
	</ul>
</div>
<div id="listRange_id">
		<div class="listRange_1">
			<li><span>$text.get("company.lb.no")：</span><input name="companyNo" type="text" value="$!iniCompanySearchForm.companyNo" onKeyDown="if(event.keyCode==13)event.keyCode=9;"></li>
			<li><span>$text.get("company.lb.name")：</span><input name="companyName" type="text" value="$!iniCompanySearchForm.companyName" onKeyDown="if(event.keyCode==13)event.keyCode=9;"></li>
			<li><span>$text.get("common.lb.dimQuery")：</span><input name="dimQuery" type="text" value="$!iniCompanySearchForm.dimQuery" onKeyDown="if(event.keyCode==13)event.keyCode=9;"></li>	
		</div>
		#if($parentName.length()>0)			
			<div class="scroll_function_big"><span>$text.get("common.msg.currstation")：$!parentName</span></div>
		#end
		<div class="scroll_function_small_a" id="conter">
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
			<thead>
				<tr class="scrollColThead">
				   <td width="45" align="center"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
					<td width="150" align="center">$text.get("company.lb.no")</td>
					<td width="150" align="center">$text.get("company.lb.name")</td>
					#if($!SimpleAccFlag!=1)
					<td width="120" align="center">$text.get("iniCompany.lb.year")</td>					
					<td width="120" align="center">$text.get("iniCompany.lb.borrow")</td>
					<td width="120" align="center">$text.get("iniCompany.lb.lend")</td>
					#end
					<td width="120" align="center">$text.get("iniComany.lb.balance")</td>
					#if($!OpenFCurrency=="true")
					<td width="120" align="center">$text.get("iniAcc.lb.CurrYIniBala")</td>	
					<td width="120" align="center">$text.get("iniAcc.lb.curRate")</td>		
					#end
					<td width="50" align="center">$text.get("common.lb.update")</td>
					<td width="100" align="center">$text.get("common.lb.showNext")</td>
				</tr>
			</thead>
			<tbody>
				#foreach ($row in $result )
				<tr #if($globals.getMOperation().query())onDblClick="location.href='/IniCompanyQueryAction.do?listType=next&type=$!type&companyCode=$globals.get($row,0)&winCurIndex=$!winCurIndex'"#end onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
					<td align ="center" class="listheadonerow"><input type="checkbox" name="keyId" value="$globals.get($row,0)"></td>
					<td align="left">$!globals.encodeHTMLLine($globals.get($row,1)) &nbsp;</td>
					<td align="left">
					#if($globals.getMOperation().query())
					<a href='/IniCompanyQueryAction.do?keyId=$globals.get($row,0)&listType=next&type=$!type&companyCode=$globals.get($row,0)&winCurIndex=$!winCurIndex'>$!globals.encodeHTMLLine($globals.get($row,2)) &nbsp;</a>
					#else
					&nbsp;
					#end
					</td>					#if($!SimpleAccFlag!=1)
					<td align="right">$!globals.get($row,3) &nbsp;</td>					
					<td align="right">$!globals.get($row,4) &nbsp;</td>
					<td align="right">$!globals.get($row,5) &nbsp;</td>
					#end
					<td align="right">$globals.get($row,6) &nbsp;<input name="balance" type="hidden" value="$globals.get($row,6)" /><input name="curBalance" type="hidden" value="$globals.get($row,7)" /></td>	
					#if($!OpenFCurrency=="true")
					<td align="right">$globals.get($row,7) &nbsp;</td>
					<td align="right">$globals.get($row,8) &nbsp;</td>	
					#end			
					<td align ="center">#if($globals.getMOperation().update())<a href='/IniCompanyQueryAction.do?listType=next&type=$!type&companyCode=$globals.get($row,0)&winCurIndex=$!winCurIndex&pageNo=$!pageNo'>$text.get("common.lb.update")</a>
					#else
					&nbsp;
					#end
					</td>
					<td align ="center">
					#if($globals.getMOperation().query())
					#if($globals.get($row,9)>0)
					<a href='/IniCompanyQueryAction.do?keyId=$globals.get($row,0)&listType=next&type=$!type&companyCode=$globals.get($row,0)&winCurIndex=$!winCurIndex'>$text.get("common.lb.showNext")</a>
					#else
						$text.get("common.lb.noChild")
					#end
					#else
					&nbsp;
					#end
					</td>
				</tr>
				#end
				
				<tr>
					<td align ="center"><strong>$text.get("common.lb.total")</strong></td>
					<td align="left">&nbsp; </td>
					<td align="left">&nbsp; </td>					
					#if($!SimpleAccFlag!=1)
					<td align="right">&nbsp; </td>					
					<td align="right">&nbsp; </td>
					<td align="right">&nbsp; </td>
					#end
					<td align="right" id="balanceTotal">&nbsp; </td>
					#if($!OpenFCurrency=="true")	
					<td align="right" id="curBalanceTotal">&nbsp; </td>
					<td align="right">&nbsp; </td>		
					#end		
					<td align ="center">&nbsp;</td>
					<td align ="center">&nbsp;</td>
				</tr>
				
				<script language="javascript" type="text/javascript">
					//生成合计数





					var balances = document.getElementsByName("balance");	
					var curBalances = document.getElementsByName("curBalance");					
					var balanceTotal = 0;				
					var curBalanceTotal = 0;
					for(var i=0;i<balances.length;i++){
						balanceTotal += parseFloat(balances[i].value.replaceAll(",",""));	
						#if($!OpenFCurrency=="true")
							curBalanceTotal += parseFloat(curBalances[i].value.replaceAll(",",""));		
						#end
					}					
					document.getElementById("balanceTotal").innerHTML="<strong>"+Number(balanceTotal).toFixed(2)+"</strong>";
					#if($!OpenFCurrency=="true")
						document.getElementById("curBalanceTotal").innerHTML="<strong>"+Number(curBalanceTotal).toFixed(2)+"</strong>";
					#end
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
