<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>总分类账</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/style1/css/financereport.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/financereport.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<style type="text/css">
	.data_list_head tr td{white-space:nowrap;padding:2px 10px;}
	.search_popup{height: 60%}
	.listRange_pagebar{width: auto;};
	.bordertr td{border: 1px;}
	.hideBorder{border-bottom: 0px;border-top: 0px;}
	
	.hideBottom{border-bottom: 0px;}
	@media print {
		.titlediv{display: none;}
		.btndiv{display: none;}
	}
	.tr_back{background: #E7FCA9;}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		#if("$!comeMode"=="")
			showSearchDiv();
			$("#periodYearStart").val("$!accPeriod.accYear");
			$("#periodYearEnd").val("$!accPeriod.accYear");
			$("#periodStart").val("$!accPeriod.accPeriod");
			$("#periodEnd").val("$!accPeriod.accPeriod");
			$("#levelStart").val(1);
			$("#levelEnd").val(7);
		#end
		/* 根据数字金额转换为大写金额 */
		$(".data_list_head tbody").find(".tdDes1").mouseover(function(){
			var objhtml = $(this).html();
			if(objhtml!=null && objhtml!="" && objhtml!="&nbsp;"){
				var changeValue = AmountInWords(objhtml,null);
				$(this).attr("title",changeValue);
			}
		});
	});
	//查询
	var beginYear = "$!globals.get($!beginPeriod,0)";
	var beginPeriod = "$!globals.get($!beginPeriod,1)";
	function onsubmits(){
		if(!isvalidate()){
			return false;
		}
		blockUI();
		form.submit();
	}
	
	function dealMoney(obj){
		var objhtml = $(obj).html();
			if(objhtml!=null && objhtml!="" && objhtml!="&nbsp;"){
				var changeValue = ChangeToBig(objhtml);
				$(this).attr("title",changeValue);
			}
	}
	
	/* 打印列表*/
	function prints(){
		var dataHeight = $("#data_list_id").css("height");
		$("#data_list_id").css("height","100%");
		$("#data_list_id").show();
		$(".titlediv").hide();
		window.print();//打印
		$("#data_list_id").css("height",dataHeight);
		$(".titlediv").show();
	}
	
	
	/* 导出列表 */
	function exports(){
		if(!isvalidate()){
			return false;
		}
		form.type.value = "exportList";
		blockUI();
		form.submit();
	}
	
	/* 清空 */
	function cleaDate(){
		$("#highSearch").find("input[type=checkbox]").each(function (){
			$(this).removeAttr("checked");
		});
		$("#periodYearStart").val('$!accPeriod.accYear');
		$("#periodYearEnd").val('$!accPeriod.accYear');
		$("#periodStart").val('$!accPeriod.accPeriod');
		$("#periodEnd").val('$!accPeriod.accPeriod');
		$("#levelStart").val(1);
		$("#levelEnd").val(7);
		$("#acctypeCodeStart").val('');
		$("#acctypeCodeEnd").val('');
	}
	
	var tr_obj = null;
	function setBacks(obj){
		if(tr_obj!=null){
			$(tr_obj).removeClass("tr_back");
		}
		$(obj).addClass("tr_back");
		tr_obj = obj;
	}
	function openDetail(classCode){
		width=document.documentElement.clientWidth-100;
		height=document.documentElement.clientHeight-50;
		
		var now=new Date(); 
		var number = now.getTime();
		var usrc = "/FinanceReportAction.do?optype=ReporttblAccDet&type=detail&orderby=&classCode="+classCode+
				"&periodStart="+$("#periodStart").val()+
				"&periodYearEnd="+$("#periodYearEnd").val()+"&dateEnd=2015-10-31&periodYearStart="+$("#periodYearStart").val()+
				"&dateType=1&dateStart=2015-10-31&keyWord=&momentType=day&periodEnd="+$("#periodEnd").val();
		openPop("openDetail","查看明细分类帐",usrc,width,height,false,false);
	}
	
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form method="post" scope="request" id="form" name="form" action="/FinanceReportAction.do?optype=ReporttblAccBalance" target="">
<input type="hidden" name="comeMode" id="comeMode" value="false" />
<input type="hidden" id="org.apache.struts.taglib.html.TOKEN" name="org.apache.struts.taglib.html.TOKEN" value=""/>
<input type="hidden" id="SQLSave" name="SQLSave" value="@SQL:$saveSql@ParamList:@end:"/>
<input type="hidden" id="type" name="type" value=""/>
	<div class="titlediv">
		<img src="/style1/images/workflow/ti_003.gif"/><span style="font-size: 14px;">总分类账</span>
	</div>
	<div style="float: left;padding-bottom: 2px;margin: 20px 10px 10px 100px;">
	#if("$!comeMode"!="")
		期间：$!conMap.periodYearStart年第$!conMap.periodStart期至#if("$!conMap.periodYearStart"!="$!conMap.periodYearEnd")$!conMap.periodYearEnd年#end第$!conMap.periodEnd期




		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		#if($!globals.getSysSetting('currency')=="true")
			#if("$!conMap.currencyName"=="")
				币别：综合本位币
			#elseif("$!conMap.currencyName"=="allCurrency")
				币别：所有币别多栏式选项
			#else
				#foreach($currency in $!currencyList)
					#if("$!conMap.currencyName"=="$!globals.get($!currency,0)")
						币别：$!globals.get($!currency,1)
					#end
				#end
			#end
		#end
	#end
	</div>
	<div class="btndiv">
		<div class="op sbtn63" onclick="showSearchDiv()">
			<span class="a"></span>
			<a href="javascript:void(0)">条件查询</a>
			<span class="c"></span>
		</div>
		<div class="op sbtn41" onclick="onsubmits()">
			<span class="a"></span>
			<a href="javascript:void(0)">刷新</a>
			<span class="c"></span>
		</div>
		#if($LoginBean.operationMap.get("/FinanceReportAction.do?optype=ReporttblAccBalance").print() || $!LoginBean.id=="1")
		<div class="op sbtn41" onclick="printReport('FinanceReportAccBalance','ReporttblAccBalance');">
			<span class="a"></span>
			<a href="javascript:void(0)">打印</a>
			<span class="c"></span>
		</div>
		#end
		<div class="op sbtn41" onclick="exports()">
			<span class="a"></span>
			<a href="javascript:void(0)">导出</a>
			<span class="c"></span>
		</div>
		<div class="op sbtn41" onclick="closePage()">
			<span class="a"></span>
			<a href="javascript:void(0)">关闭</a>
			<span class="c"></span>
		</div>
	</div>
		<div id="data_list_id" class="rt_table3">
		<script type="text/javascript"> 
			var oDiv=document.getElementById("data_list_id");
			var sHeight=document.documentElement.clientHeight-100;
			oDiv.style.height=sHeight+"px";
		</script>
			#set($curCount = 0)
			#foreach($currency in $!currencyList)
				#set($curCount = $curCount + 1)
			#end
			#set($row = 1)
			#set($col = 1)
			#set($col1 = 1)
			#set($flag = "")
			#if("$!currType"=="" || "$!currType"=="isBase")
				#set($col1 = 2)
			#elseif("$!currType"=="all")
				#set($row = 2)
				#set($col = $curCount+1)
				#set($col1 = $curCount+2)
				#set($flag = "true")
			#else
				#set($row = 2)
				#set($col = 2)
				#set($col1 = 3)
				#set($flag = "false")
			#end
			<table class="data_list_head" cellpadding="0" cellspacing="0" border="1" borderColor="#808080"  style="table-layout:fixed;overflow:auto;border-collapse:collapse;">
			<thead>
				<tr id="trTitle">
					<td width="100px" class="tdDes3" rowspan="$!row">科目代码</td>
					<td width="200px" class="tdDes3" rowspan="$!row">科目名称</td>
					<td width="50px" class="tdDes3" rowspan="$!row">期间</td>
					<td width="50px" class="tdDes3" nowrap="nowrap" rowspan="$!row">凭证字号</td>
					<td width="100px" class="tdDes3" rowspan="$!row">摘要</td>
					<td width="100px" class="tdDes3" colspan="$!col">借方</td>
					<td width="100px" class="tdDes3" colspan="$!col">贷方</td>
					<td width="100px" class="tdDes3" colspan="$!col1">余额</td>
				</tr>
				#if("$!flag"=="false")
				<tr>
					#foreach ( $foo in [1..3] )
						#if($foo==3)
							<td class="tdDes3"></td>
						#end
						<td class="tdDes3">原币</td>
						<td class="tdDes3">本位币</td>
					#end
				</tr>
				#elseif("$!flag"=="true")
				<tr>
					#foreach($foo in [1..3])
						#if($foo==3)
							<td class="tdDes3"></td>
						#end
						#foreach($currency in $!currencyList)
							<td class="tdDes3" width="100px">$!globals.get($!currency,1)</td>
						#end
						<td class="tdDes3" width="100px">本位币</td>
					#end
				</tr>
				#end
			</thead>
			<tbody>
				#foreach($!accBalance in $!accBalanceList)
				#if(($!accBalance.periodList.size()==0 && "$!conMap.takeBrowNo"=="1" && "$!accBalance.PeriodIniBase"=="") 
					|| ($!accBalance.periodList.size()==0 && "$!conMap.balanceAndTakeBrowNo"=="1" && "$!accBalance.PeriodIniBase"==""))
				#else
				<tr onclick="setBacks(this)" id="$!{accBalance.AccNumber}_tr">
					<td class="hideBottom">$!accBalance.AccNumber</td>
					<td class="hideBottom"><a href="javascript:openDetail('$!accBalance.classCode')">$!accBalance.AccFullName</a> </td>
					<td class="tdDes3 hideBottom">
						#if("$!conMap.periodYearStart"!="$!conMap.periodYearEnd")
						$!{conMap.periodYearStart}.$!{conMap.periodStart}
						#else
						$!conMap.periodStart
						#end
					</td>
					<td class="hideBottom"></td>
					<td class="tdDes3 hideBottom">期初余额</td>
					#if("$!flag"=="true")
						#set($currsizes = $!currencyList.size())
						#set($currsizes = ($currsizes+1)*2)
						#foreach ( $foo in [1..$currsizes] )
							<td width="50px" class="tdDes1 hideBottom">&nbsp;</td>
						#end
						<td class="tdDes3 hideBottom">$!accBalance.isIniflag</td>
						#foreach($currency in $!currencyList)
							#set($curId = $!globals.get($!currency,0))
							#if($!globals.get($!currency,2)=="true")
								#set($curId = "")
							#end
							#set($curName = "PeriodIni_"+${curId})
							#set($curData = $!accBalance.get($curName))
							<td class="tdDes1 hideBottom">$!curData</td>
							#set($curData = "")
							#set($curName = "")
							#set($curId = "")
						#end
						<td width="50px" class="tdDes1 hideBottom">$!accBalance.PeriodIniBase</td>						
					#elseif("$!flag"=="false")
						#foreach ( $foo in [1..4] )
							<td width="50px" class="tdDes1 hideBottom">&nbsp;</td>
						#end
						<td class="tdDes3 hideBottom">$!accBalance.isIniflag</td>
						<td width="50px" class="tdDes1 hideBottom">$!accBalance.PeriodIni</td>
						<td width="50px" class="tdDes1 hideBottom">$!accBalance.PeriodIniBase</td>
					#else
						#foreach ( $foo in [1..2] )
							<td class="tdDes1 hideBottom">&nbsp;</td>
						#end
						<td width="30px" class="tdDes3 hideBottom">$!accBalance.isIniflag</td>
						<td width="70px" class="tdDes1 hideBottom">$!accBalance.PeriodIniBase</td>
					#end
				</tr>
				#set($count = 0)
				#foreach($!periodData in $!accBalance.periodList)
				<tr onclick="setBacks(this)">
					<td class="hideBorder"></td>
					<td class="hideBorder"></td>
					<td class="tdDes3 hideBorder">
						#if("$!conMap.periodYearStart"!="$!conMap.periodYearEnd")
						$!{periodData.Nyear}.$!{periodData.Period}
						#else
						$!{periodData.Period}
						#end
					</td>
					<td class="hideBorder">$!periodData.get("credTypeidOrderNo")</td>
					<td class="tdDes3 hideBorder">本期合计</td>
					#if("$flag" == "" || "$flag" == "true")
						#set($list=["PeriodDebitSumBase","PeriodCreditSumBase","PeriodDCBalaBaseisflag","PeriodDCBalaBase"])
					#elseif("$flag"=="false")
						#set($list=["PeriodDebitSum","PeriodDebitSumBase","PeriodCreditSum","PeriodCreditSumBase","PeriodDCBalaBaseisflag","PeriodDCBala","PeriodDCBalaBase"])
					#end
					#foreach ($element in $list)
						#set($valueData = $!periodData.get($element))
						#set($setclass="tdDes1")
						#if("$!element"=="PeriodDCBalaBaseisflag")
							#set($setclass="tdDes3")
						#end
						#if("$flag" == "true")
							#foreach($currency in $!currencyList)
								#if("$!element"!="PeriodDCBalaBaseisflag")
									#set($curId = $!globals.get($!currency,0))
									#if($!globals.get($!currency,2)=="true")
										#set($curId = "")
									#end
									#set($curName = $!globals.replaceString(${element},'Base','')+"_"+${curId})
									#set($curData = $!periodData.get($curName))
									<td class="$!setclass hideBorder">$!curData</td>
									#set($curData = "")
									#set($curName = "")
									#set($curId = "")
								#end
							#end
						#end
						<td class="$!setclass hideBorder">#if('$!valueData'!='') $NumericTool.format($!valueData,'#,##0.00') #end</td>
						#set($valueData="")
					#end
				</tr>
				<!-- 本年累计 -->
				<tr onclick="setBacks(this)">
					<td class="hideBorder"></td>
					<td class="hideBorder"></td>
					<td class="tdDes3 hideBorder">
						#if("$!conMap.periodYearStart"!="$!conMap.periodYearEnd")
						$!{periodData.Nyear}.$!{periodData.Period}
						#else
						$!{periodData.Period}
						#end
					</td>
					<td class="hideBorder"></td>
					<td class="tdDes3 hideBorder">本年累计</td>
					#if("$flag" == "" || "$flag" == "true")
						#set($list=["CurrYIniDebitSumBase","CurrYIniCreditSumBase","CurrYIniAmountisflag","CurrYIniAmount"])
					#elseif("$flag"=="false")
						#set($list=["CurrYIniDebitSum","CurrYIniDebitSumBase","CurrYIniCreditSum","CurrYIniCreditSumBase","CurrYIniAmountisflag","CurrYIni","CurrYIniAmount"])
					#end
					#foreach ($element in $list)
						#set($valueData = $!periodData.get($element))
						#set($setclass="tdDes1")
						#if("$!element"=="CurrYIniAmountisflag")
							#set($setclass="tdDes3")
						#end
						#if("$flag" == "true")
							#foreach($currency in $!currencyList)
								#if("$!element"!="CurrYIniAmountisflag")
									#set($curId = $!globals.get($!currency,0))
									#if($!globals.get($!currency,2)=="true")
										#set($curId = "")
									#end
									#if("$!element"=="CurrYIniAmount")
										#set($curName = $!globals.replaceString(${element},'Amount','')+"_"+${curId})
									#else
										#set($curName = $!globals.replaceString(${element},'Base','')+"_"+${curId})
									#end
									#set($curData = $!periodData.get($curName))
									<td class="$!setclass hideBorder">$!curData</td>
									#set($curData = "")
									#set($curName = "")
									#set($curId = "")
								#end
							#end
						#end					
						<td class="$!setclass hideBorder">#if('$!valueData'!='') $NumericTool.format($!valueData,'#,##0.00') #end</td>
						#set($valueData="")
					#end
					 
				</tr>
				#set($count = $count+1)
				#end
				#end
				#end
			</tbody>
		</table>
		#if("$!comeMode"=="")
			<div class="querydiv1">
			点击条件查询按钮，进行数据查询




			</div>
		#elseif($accBalanceList.size()==0)
			<div class="querydiv1">
				<p style="width:150px;padding:50px 0 0 0;overflow:hidden;line-height:21px;margin:0 auto;">
					很抱歉，没有找到与您条件匹配的总分类账信息!
				</p>
			</div>
		#end
		</div>
		<!-- 条件查询 -->
		<div id="highSearch" class="search_popup" style="display:none;height: 320px;top: 220px;">
		<script type="text/javascript">
			var sLeft=document.body.offsetWidth/2
			$("#highSearch").css("left",sLeft);
		</script>
		<input type="hidden" name="queryType" id="queryType" value=""/>
		<div id="Divtitle" style="cursor: move;width:98%;">
			<span class="ico_4"></span>&nbsp;条件查询
		</div>
			<table style="width:100%;padding-top: 5px;" id="tableSearch">
				<tr>
					<td><div><table cellpadding="0" cellspacing="0" width="100%">
						<thead>
						<tr><td class="tdDes1" width="15%">会计期间:&nbsp;</td><td width="35%"><input name="periodYearStart" style="width:40px;" id="periodYearStart" value="$!conMap.periodYearStart" size="2"/>&nbsp;年




							<input name="periodStart" id="periodStart" style="width:40px;" value="$!conMap.periodStart" size="2"/>&nbsp;期</td>
						<td class="tdDes1" width="10%">至&nbsp;</td><td width="35%"><input name="periodYearEnd" style="width:40px;" id="periodYearEnd" value="$!conMap.periodYearEnd" size="2"/>&nbsp;年




							<input name="periodEnd" id="periodEnd" style="width:40px;" value="$!conMap.periodEnd" size="2"/>&nbsp;期</td>
						</tr>
						<tr><td class="tdDes1">科目级别:&nbsp;</td><td><input name="levelStart" id="levelStart" value="$!conMap.levelStart" size="13"/>
						</td><td class="tdDes1">至&nbsp;</td><td><input name="levelEnd" id="levelEnd" value="$!conMap.levelEnd" size="13"/></td></tr>
						<tr><td class="tdDes1">科目代码:&nbsp;</td><td style="white-space: nowrap;" class="searchinput"><input name="acctypeCodeStart" id="acctypeCodeStart" value="$!conMap.acctypeCodeStart" size="13" ondblclick="selectCode('acctypeCodeStart')" />
						<img src='/style1/images/search.gif' onClick="selectCode('acctypeCodeStart')" class='search' />
						</td><td class="tdDes1">至&nbsp;</td><td style="white-space: nowrap;" class="searchinput">
						<input name="acctypeCodeEnd" id="acctypeCodeEnd" value="$!conMap.acctypeCodeEnd" size="13" ondblclick="selectCode('acctypeCodeEnd')"/>
						<img src='/style1/images/search.gif' onClick="selectCode('acctypeCodeEnd')" class='search'/></td>
						</tr>
						#if($!globals.getSysSetting('currency')=="true")
						<tr>
							<td class="tdDes1">币别:&nbsp;</td>
							<td>
								<select name="currencyName" id="currencyName">
									#foreach($currency in $!currencyList)
										<option value="$!globals.get($!currency,0)" #if("$!conMap.currencyName"=="$!globals.get($!currency,0)")selected#end>$!globals.get($!currency,1)</option>
									#end
									<option value="" #if("$!conMap.currencyName"=="")selected#end><综合本位币></option>
									<option value="all" #if("$!conMap.currencyName"=="all")selected#end><所有币别多栏式></option>
								</select>
							</td>
						</tr>
						#end
						</thead>
						<tbody>
						<tr>
							<td class="tdDes1"><input name="takeBrowNo" id="takeBrowNo" value="1" type="checkbox" #if("$conMap.takeBrowNo"=="1")checked#end/></td><td><label for="takeBrowNo">无发生额不显示</label></td>
							<td class="tdDes1"><input name="balanceAndTakeBrowNo" id="balanceAndTakeBrowNo" value="1" type="checkbox" #if("$conMap.balanceAndTakeBrowNo"=="1")checked#end/></td><td><label for="balanceAndTakeBrowNo">余额为零且无发生额不显示</label></td>
						</tr>
						<tr>
							#if("$!AccMainSetting.isAuditing"=="1")
							<td class="tdDes1">
								<input name="binderNo" id="binderNo" value="1" type="checkbox" #if("$conMap.binderNo"=="1")checked#end/></td><td><label for="binderNo">包括未过账凭证</label>
							</td>
							#end
							#if("$!globals.getSysSetting('standardAcc')"=="true")
							<td class="tdDes1">
								<input name="showIsItem" id="showIsItem" value="1" type="checkbox" #if("$conMap.showIsItem"=="1")checked#end/></td><td><label for="showIsItem">显示核算项目明细</label>
							</td>
							#end
						</tr>
						<tr>
							<td class="tdDes1"><input name="showBanAccTypeInfo" id="showBanAccTypeInfo" value="1" type="checkbox" #if("$conMap.showBanAccTypeInfo"=="1")checked#end/></td><td colspan="3"><label for="showBanAccTypeInfo">显示禁用科目</label></td>
						</tr></tbody>
					</table></div><td>
				</tr>
			</table>
			<span class="search_popup_bu"><input type="button" 
					onclick="onsubmits();" class="bu_1"  />
				<input type="button" class="bu_3" value="" onclick="cleaDate()"/>
				<input type="button" onClick="closeSearch();" class="bu_2" />
			</span>
			<script language="javascript">
			var posX;
			var posY;       
			fdiv = document.getElementById("highSearch");         
			document.getElementById("Divtitle").onmousedown=function(e){
				if(!e) e = window.event;  //IE
			    posX = e.clientX - parseInt(fdiv.style.left);
			    posY = e.clientY - parseInt(fdiv.style.top);
			    document.onmousemove = mousemove;           
			}
 
			document.onmouseup = function(){
			    document.onmousemove = null;
			}
			function mousemove(ev){
			    if(ev==null) ev = window.event;//IE
			    fdiv.style.left = (ev.clientX - posX) + "px";
			    fdiv.style.top = (ev.clientY - posY) + "px";
			}
			</script>
		</div>
</form>
</body>
</html>