<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("tableInfo.title.add")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript" src="/js/editGrid.js"></script>
<script language="javascript">
var minValue=-999999999
var maxValue=999999999
var FixRate="$!FixRate";
var tableId="edittable";
var editgriddata = new gridData();
childData[0]='';
var curs=":$!baseCur;";
 #foreach ($cur in $!curs)
           curs+="$!globals.get($cur,0).trim():$!globals.get($cur,1);";  
 #end	
addCols(editgriddata,'id','id','0',2,false,'',0,-2,'');
#if($!SimpleAccFlag!=1)
addCols(editgriddata,'year','$text.get("iniCompany.lb.year")','100',1,false,'0',0,0,'');
putChildValidateItem("year","$text.get("iniCompany.lb.year")","double","",false,minValue,maxValue,false);	
addCols(editgriddata,'borrow','$text.get("iniCompany.lb.borrow")','100',1,false,'0',0,0,'');
putChildValidateItem("borrow","$text.get("iniCompany.lb.borrow")","double","",false,minValue,maxValue,false);	
addCols(editgriddata,'lend','$text.get("iniCompany.lb.lend")','100',1,false,'0',0,0,'');
putChildValidateItem("lend","$text.get("iniCompany.lb.lend")","double","",false,minValue,maxValue,false);	
#end
addCols(editgriddata,'balance','$text.get("iniComany.lb.balance")','100',1,false,'0',0,0,'');
putChildValidateItem("balance","$text.get("iniComany.lb.balance")","double","",false,minValue,maxValue,false);	
#if($!OpenFCurrency=="true")
addCols(editgriddata,'currencyID','$text.get("iniComany.lb.curID")','100',2,false,'0',0,1,curs);
addCols(editgriddata,'currencyRate','$text.get("iniComany.lb.curRate")','100',1,false,'0',0,0,'');
#if($!SimpleAccFlag!=1)
addCols(editgriddata,'curyear','$text.get("iniCompany.lb.curyear")','100',1,false,'0',0,0,'');
putChildValidateItem("curyear","$text.get("iniCompany.lb.curyear")","double","",false,minValue,maxValue,false);
addCols(editgriddata,'curborrow','$text.get("iniCompany.lb.curborrow")','100',1,false,'0',0,0,'');
putChildValidateItem("curborrow","$text.get("iniCompany.lb.curborrow")","double","",false,minValue,maxValue,false);
addCols(editgriddata,'curlend','$text.get("iniCompany.lb.curlend")','100',1,false,'0',0,0,'');
putChildValidateItem("curlend","$text.get("iniCompany.lb.curlend")","double","",false,minValue,maxValue,false);
#end
addCols(editgriddata,'curbalance','$text.get("iniComany.lb.curbalance")','100',1,false,'0',0,0,'');
putChildValidateItem("curbalance","$text.get("iniComany.lb.curbalance")","double","",false,minValue,maxValue,false);
#end
	   
#foreach ($row in $rows)
addRows(editgriddata,'$globals.get($row,0)'#if($!SimpleAccFlag!=1),'$globals.get($row,5)','$globals.get($row,6)','$globals.get($row,7)'#end,'$globals.get($row,8)'#if($!OpenFCurrency=="true"),'$!globals.get($row,3)','$globals.get($row,4)'#if($!SimpleAccFlag!=1),'$globals.get($row,9)','$globals.get($row,10)','$globals.get($row,11)'#end,'$globals.get($row,12)'#end);
#end

function initCalculate (){
#if($!OpenFCurrency=="true")
var currencyID=document.getElementsByName('currencyID');
		for(i = 0;i<currencyID.length;i++){
			currencyID[i].onchange =function() {
				var pos=0;
				var flag=false;
				var curID=document.getElementsByName('currencyID');
				for(i = 0;i<curID.length;i++){
					if(curID[i]==this){
						pos=i;						
					}
					if(curID[i]!=this&&curID[i].value==this.value){
						alert("$text.get("common.msg.currencySelected")");	
						this.options[0].selected="true";	
						return;
					}
				}
				
				var currencyId=this.value;

				if(currencyId.length>0){
			    AjaxRequest('/UtilServlet?operation=currencyRate&currency='+currencyId) ;
        		     var value = response;
					 if(FixRate=="true"&&value==0){
			 			alert("$text.get("common.msg.RET_NOTREC_RECORDEXCHANGE")");
						document.getElementsByName("currencyRate")[pos].value=0;
						document.getElementsByName("currencyRate")[pos].disabled=true;			
			 		 }else{			 
			 			document.getElementsByName("currencyRate")[pos].disabled=false;
						document.getElementsByName("currencyRate")[pos].value=value;
					 }			
            }else{
   	           document.getElementsByName("currencyRate")[pos].disabled=false;
	           document.getElementsByName("currencyRate")[pos].value=0;
  	      	}
			iniValueChange(document.getElementsByName("currencyRate")[pos]);
		}
	}
#end
	var year=document.getElementsByName('year');
		for(i = 0;i<year.length;i++){
			year[i].onchange=function (){iniValueChange(this);};
		}
	var borrow=document.getElementsByName('borrow');
		for(i = 0;i<borrow.length;i++){
			borrow[i].onchange=function (){iniValueChange(this);};
		}
	var lend=document.getElementsByName('lend');
		for(i = 0;i<lend.length;i++){
			lend[i].onchange=function (){iniValueChange(this);};
		}
	var balance=document.getElementsByName('balance');
		for(i = 0;i<balance.length;i++){
			balance[i].onchange=function (){iniBalanceChange(this);};
		}
	var curyear=document.getElementsByName('curyear');
		for(i = 0;i<curyear.length;i++){
			curyear[i].onchange=function (){iniValueChange(this);};
		}
	var curborrow=document.getElementsByName('curborrow');
		for(i = 0;i<curborrow.length;i++){
			curborrow[i].onchange=function (){iniValueChange(this);};
		}
	var curlend=document.getElementsByName('curlend');
		for(i = 0;i<curlend.length;i++){
			curlend[i].onchange=function (){iniValueChange(this);};
		}
	var curbalance=document.getElementsByName('curbalance');
		for(i = 0;i<curbalance.length;i++){
			curbalance[i].onchange=function (){iniValueChange(this);};
		}
	var currencyRate=document.getElementsByName('currencyRate');
		for(i = 0;i<currencyRate.length;i++){
			currencyRate[i].onchange=function (){iniValueChange(this);};
		}
}

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
	#if($!SimpleAccFlag!=1)
	var year=document.getElementsByName("year")[pos];	
	var borrow=document.getElementsByName("borrow")[pos];
	var lend=document.getElementsByName("lend")[pos];
	#end
	#if($!OpenFCurrency=="true")
	var curbalance=document.getElementsByName("curbalance")[pos];
	#if($!SimpleAccFlag!=1)
	var curyear=document.getElementsByName("curyear")[pos];
	var curborrow=document.getElementsByName("curborrow")[pos];
	var curlend=document.getElementsByName("curlend")[pos];
	#end
	
	var currencyRate=document.getElementsByName("currencyRate")[pos];
	
	if(vars.name=="currencyRate"){
		balance.value=curbalance.value*currencyRate.value;
		#if($!SimpleAccFlag!=1)
		borrow.value=curborrow.value*currencyRate.value;
		lend.value=curlend.value*currencyRate.value;
		year.value=curyear.value*currencyRate.value;
		#end
	}
	
	
	//余额计算汇率
	if(vars.name=="curbalance"){
		balance.value=curbalance.value*currencyRate.value;
		balance.value=formatNumber(balance.value);
	}
	
	#if($!SimpleAccFlag!=1)
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
	#end
	#end	

	vars.value=f(vars.value, $globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
	#if($!SimpleAccFlag!=1)
	if("$!type"=="receive"||"$!type"=="prePay")
	{
		balance.value=f(parseFloat(year.value)+parseFloat(borrow.value)-parseFloat(lend.value),$globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
		#if($!OpenFCurrency=="true")
		curbalance.value=f(parseFloat(curyear.value)+parseFloat(curborrow.value)-parseFloat(curlend.value),$globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
		#end
	}else if("$!type"=="pay"||"$!type"=="preReceive")
	{
		balance.value=f(parseFloat(year.value)+parseFloat(lend.value)-parseFloat(borrow.value),$globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
		#if($!OpenFCurrency=="true")
		curbalance.value=f(parseFloat(curyear.value)+parseFloat(curlend.value)-parseFloat(curborrow.value),$globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
		#end
	}
	#end
	
	
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
	if(vars.value.length==0){
			vars.value=0;
	}
	#if($!OpenFCurrency=="true")
		var curbalance=document.getElementsByName("curbalance")[pos];
		var curyear=document.getElementsByName("curyear")[pos];
		var curborrow=document.getElementsByName("curborrow")[pos];
		var curlend=document.getElementsByName("curlend")[pos];
		
		var currencyRate=document.getElementsByName("currencyRate")[pos];
		
		
		
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
	#end
	vars.value=f(vars.value, $globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
	
	if("$!type"=="receive"||"$!type"=="prePay")
	{
		year.value=f(parseFloat(balance.value)-parseFloat(borrow.value)+parseFloat(lend.value),$globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
		#if($!OpenFCurrency=="true")
			curyear.value=f(parseFloat(curbalance.value)-parseFloat(curborrow.value)+parseFloat(curlend.value),$globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
		#end
	}else if("$!type"=="pay"||"$!type"=="preReceive")
	{
		year.value=f(parseFloat(balance.value)-parseFloat(lend.value)+parseFloat(borrow.value),$globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
		#if($!OpenFCurrency=="true")
			curyear.value=f(parseFloat(curbalance.value)-parseFloat(curlend.value)+parseFloat(curborrow.value),$globals.getDigitsOrSysSetting("tblCompanyIni","ReceiveTotalRemain"));
		#end
	}
	
}


function beforSubmit(form){   
	if(!validate(form))return false;	
	if(!validateDigit("balance","$text.get("iniComany.lb.balance")",$globals.getFieldDigits("tblCompanyIni","PayTotalRemain"),false))return false;		
	disableForm(form);
	form.winCurIndex.value="$!winCurIndex";
	document.form.submit();
	return true;
}	


</script>
</head>

<body onLoad="showtable('edittable');  initTableList(edittableDIV,edittable,editgriddata);initCalculate();">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/IniCompanyAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")">
<input type="hidden" name="type" value="$!type">
<input type="hidden" name="companyCode" value="$companyCode">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="winCurIndex" value="$!winCurIndex">
<input type="hidden" name="pageNo" value="$!pageNo">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("iniCompany.lb.head")_$text.get($!iniTypeText)</div>
	<ul class="HeadingButton">	
	   #if($globals.getMOperation().update())
		<li><button type="button" onClick="beforSubmit(document.form);" class="b2">$text.get("common.lb.save")</button></li>
		#end
		#if($globals.getMOperation().query())
			<li>	<button type="button" onClick='location.href="/IniCompanyQueryAction.do?operation=$globals.getOP("OP_QUERY")&type=$!type&listType=back&companyCode=$!companyCode&winCurIndex=$!winCurIndex&pageNo=$!pageNo"' class="b2">$text.get("common.lb.back")</button></li>
	   #end
	</ul>
</div>
<div id="listRange_id">
			<script type="text/javascript">
				var oDiv=document.getElementById("listRange_id");
				var sHeight=document.body.clientHeight-38;
				oDiv.style.height=sHeight+"px";
			</script>
		<div class="listRange_1">	
			<li><span>$text.get("company.lb.no")：</span><input  type="text"  name="companyNo" disabled="disabled" value="$!companyNo" ></li>
			<li><span>$text.get("company.lb.name")：</span><input  type="text"  name="companyName" disabled="disabled" value="$!companyName" ></li>			
		</div>
		<div class="scroll_function_small_a">
			<div  name="edittableDIV" id="edittableDIV"> 
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="edittable" id="edittable">
			</table>
			</div>
		</div>
	</div>
</div>
</form> 
</body>
</html>
