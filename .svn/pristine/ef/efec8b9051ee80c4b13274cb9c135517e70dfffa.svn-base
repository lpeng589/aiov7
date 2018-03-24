<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>科目余额表</title>
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
	.search_popup{height: 60%}
	.data_list_head tr td{ white-space:nowrap;padding:2px 5px;}
	.data_list_body tr td{ white-space:nowrap;padding:2px 5px;}
	.listRange_pagebar{width: auto;};
	.bordertr td{border: 1px;}
	.hideBorder{border-bottom: 0px;border-top: 0px;}
	.hideBottom{border-bottom: 0px;}
	.tr_back{background: #E7FCA9;}
	
	.data_list_body thead tr td{background:url(../images/workflow/data_list_head_bg.gif); height:25px; line-height:25px;white-space: nowrap;}

	.data_list_head thead tr td{background:#fff;}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		$(".data_list_head").css("width",$(".data_list_body").width()+2);
		$(".data_container").css("height",$(document.body).height()*0.9);
		#if("$!comeMode"=="")
			showSearchDiv();
			$("#periodYearStart").val("$!accPeriod.accYear");
			$("#periodYearEnd").val("$!accPeriod.accYear");
			$("#periodStart").val("$!accPeriod.accPeriod");
			$("#periodEnd").val("$!accPeriod.accPeriod");
		#end
		
		$(".data_list_head tbody").find(".tdDes1").mouseover(function(){
			var objhtml = $(this).html();
			if(objhtml!=null && objhtml!="" && objhtml!="&nbsp;"){
				var changeValue = AmountInWords(objhtml,null);
				$(this).attr("title",changeValue);
			}
		});
		
		var wWidth = $("#data_list_id").width();
		var sWidth = $("#data_list_id .tb_wp").width();
		if(sWidth < wWidth){
			$("#data_list_id .tb_wp").width("100%");
		}
		
	});
	
	function tabWd()
	{
		if($(".data_list_head").width() < $(document.body).outerWidth(true))
		{
			//$(".data_list_head").width("98%");
		}
	}
	var beginYear = "$!globals.get($!beginPeriod,0)";
	var beginPeriod = "$!globals.get($!beginPeriod,1)";
	//查询
	function onsubmits(){
		if(!isvalidate()){
			return false;
		}
		blockUI();
		form.submit();
	}
	
	var tr_obj = null;
	function setBacks(obj){
		if(tr_obj!=null){
			$(tr_obj).removeClass("tr_back");
		}
		$(obj).addClass("tr_back");
		tr_obj = obj;
	}
	
	/* 清空 */
	function cleaDate(){
		$("#highSearch").find("input[type=checkbox]").each(function (){
			$(this).removeAttr("checked");
		});
		$("#periodYearStart").val("$!accPeriod.accYear");
		$("#periodYearEnd").val("$!accPeriod.accYear");
		$("#periodStart").val("$!accPeriod.accPeriod");
		$("#periodEnd").val("$!accPeriod.accPeriod");
		$("#acctypeCodeStart").val('');
		$("#acctypeCodeEnd").val('');
		$("#levelStart").val('');
	}
	/* 打印 */
	function prints(){
		var dataHeight = $("#data_list_id").css("height");
		var dataWidth = $("#data_list_id").css("width");
		$("#data_list_id").css("height","100%");
		$("#data_list_id").css("width","100%");
		$("#data_list_id").show();
		$(".btndiv").hide();
		$(".titlediv").hide();
		window.print();//打印
		$("#data_list_id").css("height",dataHeight);
		$("#data_list_id").css("width",dataWidth);
		$(".btndiv").show();
		$(".titlediv").show();
	}
	
	/* 导出列表 */
	function exports(){
		form.type.value = "exportList";
		blockUI();
		form.submit();
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
<body onload="tabWd();">
<form method="post" scope="request" id="form" name="form" action="/FinanceReportAction.do?optype=ReportAccTypeInfoBalance" target="">
<input type="hidden" name="comeMode" id="comeMode" value="false" />
<input type="hidden" id="org.apache.struts.taglib.html.TOKEN" name="org.apache.struts.taglib.html.TOKEN" value=""/>
<input type="hidden" id="SQLSave" name="SQLSave" value="@SQL:$saveSql@ParamList:@end:"/>
<input type="hidden" id="type" name="type" value=""/>
	<div class="titlediv">
		<img src="/style1/images/workflow/ti_003.gif"/><span style="font-size: 14px;">科目余额表</span>
	</div>
	<div style="float: left;padding-bottom: 2px;margin: 20px 10px 10px 100px;">
	#if("$!comeMode"!="")
		期间：$!conMap.periodYearStart年第$!conMap.periodStart期至
		#if("$!conMap.periodYearStart"!="$!conMap.periodYearEnd")$!conMap.periodYearEnd年#end第$!conMap.periodEnd期



		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		#if($!globals.getSysSetting('currency')=="true")
		币别：



		#if("$!conMap.currencyName"=="")
			<综合本位币>
		#elseif("$!conMap.currencyName"=="all")
			<所有币别多栏式>
		#else
			#foreach($currency in $!currencyList)
				#if("$!conMap.currencyName"=="$!globals.get($!currency,0)")
					$!globals.get($!currency,1)
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
		#if($LoginBean.operationMap.get("/FinanceReportAction.do?optype=ReportAccTypeInfoBalance").print() || $!LoginBean.id=="1")
		<div class="op sbtn41" onClick="printReport('FinanceReportAccTypeInfoBalance','ReportAccTypeInfoBalance');">
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
		#set($curCount = 0)
				#foreach($currency in $!currencyList)
					#set($curCount = $curCount + 1)
				#end
				#set($row = 1)
				#set($col = 1)
				#set($nextcol = 1)
				#set($flag = "")
				#if("$!currType"=="" || "$!currType"=="isBase")
					#set($row = 2)
					#set($col = 2)
				#elseif("$!currType"=="all")
					#set($row = 3)
					#set($col = 2*($curCount+1))
					#set($nextcol = $curCount+1)
					#set($flag = "true")
				#else
					#set($row = 3)
					#set($col = 4)
					#set($nextcol = 2)
					#set($flag = "false")
				#end
		<div class="data_container" style="overflow: hidden; height: 560px;width:100%;box-sizing:border-box;">
			<div class="data_box" style="width:100%;height:100%;overflow:hidden;position:relative;">
				<div class="data_list_top" style="margin-left:10px;z-index: 55; width: 100%; overflow: hidden; position: absolute; top: 0px; left: 0px;">
					<table class="data_list_head" cellpadding="0" cellspacing="0" border="1" borderColor="#808080" style="table-layout:fixed;overflow:auto;border-collapse:collapse;">
						<thead >
							<tr id="trTitle" >
								<td class="tdDes3" rowspan="$!row" width=100>No.</td>
								<td class="tdDes3" rowspan="$!row" width=100>科目代码</td>
								<td class="tdDes3" rowspan="$!row" width=250>科目名称</td>
								<td class="tdDes3" colspan="$!col" width=150>期初余额</td>
								<td class="tdDes3" colspan="$!col" width=150>本期发生</td>
								<td class="tdDes3" colspan="$!col" width=150>本年累计</td>
								<td class="tdDes3" colspan="$!col" width=150>期末余额</td>
							</tr>
							<tr>
								#foreach ( $foo in [1..4] )
									<td class="tdDes3" colspan="$!nextcol">借方</td>
									<td class="tdDes3" colspan="$!nextcol">贷方</td>
								#end
							</tr>
							#if("$!flag"=="false")
								<tr>
									#foreach ( $foo in [1..8] )
										<td class="tdDes3">原币</td>
										<td class="tdDes3">本位币</td>
									#end
								</tr>
							#elseif("$!flag"=="true")
								<tr>
								#foreach ( $foo in [1..8] )
								#foreach($currency in $!currencyList)
									<td class="tdDes3">$!globals.get($!currency,1)</td>
								#end
									<td class="tdDes3">本位币</td>
								#end
								</tr>
							#end
						</thead>
					</table>
				</div>
				<div id="data_list_id" class="rt_table3" style="z-index: 40; width: 100%; overflow: auto; position: absolute; top: 0px; left: 0px; height: 303px;">
				<script type="text/javascript"> 
					var oDiv=document.getElementById("data_list_id");
					var sHeight=document.documentElement.clientHeight-100;
					oDiv.style.height=sHeight+"px";
				</script>
					
					<table class="data_list_body" cellpadding="0" cellspacing="0" border="1" borderColor="#808080" style="overflow:auto;border-collapse:collapse;table-layout:fixed;width:100%;">
					
					<thead >
						<tr id="trTitle" >
							<td class="tdDes3" rowspan="$!row" width=100>No.</td>
							<td class="tdDes3" rowspan="$!row" width=100>科目代码</td>
							<td class="tdDes3" rowspan="$!row" width=250>科目名称</td>
							<td class="tdDes3" colspan="$!col" width=150>期初余额</td>
							<td class="tdDes3" colspan="$!col" width=150>本期发生</td>
							<td class="tdDes3" colspan="$!col" width=150>本年累计</td>
							<td class="tdDes3" colspan="$!col" width=150>期末余额</td>
						</tr>
						<tr>
							#foreach ( $foo in [1..4] )
								<td class="tdDes3" colspan="$!nextcol">借方</td>
								<td class="tdDes3" colspan="$!nextcol">贷方</td>
							#end
						</tr>
						#if("$!flag"=="false")
							<tr>
								#foreach ( $foo in [1..8] )
									<td class="tdDes3">原币</td>
									<td class="tdDes3">本位币</td>
								#end
							</tr>
						#elseif("$!flag"=="true")
							<tr>
							#foreach ( $foo in [1..8] )
							#foreach($currency in $!currencyList)
								<td class="tdDes3">$!globals.get($!currency,1)</td>
							#end
								<td class="tdDes3">本位币</td>
							#end
							</tr>
						#end
					</thead>
					<tbody>
						<!-- 数据显示 -->
						#set($count = 1)
						#foreach($accData in $!accTypeList)
						#set($classType = "")
						#if("$!accData.isCalculate"=="1" && "$!conMap.partitionLine"!="1")
							#set($classType = "hideBorder")
						#else
							#set($classType = "")
						#end
						<tr style="line-height: 24px;" onclick="setBacks(this)">
							<td class="tdDes3">$!count</td>
							<td class="$!classType hideBottom" #if("$!accData.isCalculate"=="1") style="padding-left: 20px;" #end>$!accData.AccNumber</td>
							<td class="$!classType hideBottom" #if("$!accData.isCalculate"=="1") style="padding-left: 20px;" #end><a href="javascript:openDetail('$!accData.classCode')">$!accData.AccFullName</a></td>
							#if("$flag" == "" || "$flag" == "true")
								#set($list=["PeriodIniDebitBase","PeriodIniCreditBase","PeriodDebitSumBase","PeriodCreditSumBase","CurrYIniDebitSumBase","CurrYIniCreditSumBase","PeriodDebitBalaBase","PeriodCreditBalaBase"])
							#elseif("$flag"=="false")
								#set($list=["PeriodIniDebit","PeriodIniDebitBase","PeriodIniCredit","PeriodIniCreditBase","PeriodDebitSum","PeriodDebitSumBase","PeriodCreditSum","PeriodCreditSumBase","CurrYIniDebitSum","CurrYIniDebitSumBase","CurrYIniCreditSum","CurrYIniCreditSumBase","PeriodDebitBala","PeriodDebitBalaBase","PeriodCreditBala","PeriodCreditBalaBase"])
							#end
							#foreach ($element in $list)
								#if("$flag" == "true")
									#foreach($currency in $!currencyList)
										#set($curId = $!globals.get($!currency,0))
										#if($!globals.get($!currency,2)=="true")
											#set($curId = "")
										#end
										#set($curName = $!globals.replaceString(${element},'Base','')+"_"+${curId})
										#set($curData = $!accData.get($curName))
										<td id="moneys" class="$!classType tdDes1 hideBottom">#if('$!curData'!='') $NumericTool.format($!curData,'#,##0.00') #end</td>
										#set($curData = "")
										#set($curName = "")
										#set($curId = "")
									#end
								#end
								#set($valueData = $!accData.get($element))
								<td id="moneys" class="$!classType tdDes1 hideBottom">#if('$!valueData'!='') $NumericTool.format($!valueData,'#,##0.00') #end</td>
								#set($valueData="")
							#end
						</tr>
						#set($count = $!count+1)
						#end
						#if($!accTypeList.size()!=0)
						<tr style="line-height: 24px;background-color:lightyellow;">
							<td colspan="2"></td>
							<td>合计</td>
							#foreach ($element in $list)
								#if("$flag" == "true")
									#foreach($currency in $!currencyList)
										#set($curId = $!globals.get($!currency,0))
										#if($!globals.get($!currency,2)=="true")
											#set($curId = "")
										#end
										#set($curName = $!globals.replaceString(${element},'Base','')+"_"+${curId})
										#set($curData = $!totalMap.get($curName))
										<td id="moneys" class="$!classType tdDes1 hideBottom">#if('$!curData'!='') $NumericTool.format($!curData,'#,##0.00') #end</td>
										#set($curData = "")
										#set($curName = "")
										#set($curId = "")
									#end
								#end
								#set($valueData = $!totalMap.get($element))
								<td class="tdDes1">#if('$!valueData'!='') $NumericTool.format($!valueData,'#,##0.00') #end</td>
								#set($valueData="")
							#end
						</tr>
						#end
					</tbody>
				</table>
				#if("$!comeMode"=="")
					<div class="querydiv1">
					点击条件查询按钮，进行数据查询
		
		
		
		
		
					</div>
				#elseif($accTypeList.size()==0)
					<div class="querydiv1">
						<p style="width:150px;padding:50px 0 0 0;overflow:hidden;line-height:21px;margin:0 auto;">
							很抱歉，没有找到与您条件匹配的科目余额表信息!
						</p>
					</div>
				#end
				</div>
			</div>
		</div>
		<!-- 条件查询 -->
		<div id="highSearch" class="search_popup" style="display:none;height: 300px;top: 220px;">
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
						<tr><td class="tdDes1" width="15%">会计期间:&nbsp;</td><td width="35%"><input name="periodYearStart" id="periodYearStart" style="width:40px;" value="$!conMap.periodYearStart" size="2"/>&nbsp;年





							<input name="periodStart" id="periodStart" value="$!conMap.periodStart" style="width:40px;" size="2"/>&nbsp;期</td>
						<td class="tdDes1" width="10%">至&nbsp;</td><td width="35%"><input name="periodYearEnd" id="periodYearEnd" style="width:40px;" value="$!conMap.periodYearEnd" size="2"/>&nbsp;年





							<input name="periodEnd" id="periodEnd" value="$!conMap.periodEnd" style="width:40px;" size="2"/>&nbsp;期</td>
						</tr>
						<tr><td class="tdDes1">科目代码:&nbsp;</td><td style="white-space: nowrap;" class="searchinput"><input name="acctypeCodeStart" id="acctypeCodeStart" value="$!conMap.acctypeCodeStart" size="13" ondblclick="selectCode('acctypeCodeStart')" />
						<img src='/style1/images/search.gif' onClick="selectCode('acctypeCodeStart')" class='search' />
						</td><td class="tdDes1">至&nbsp;</td><td style="white-space: nowrap;" class="searchinput">
						<input name="acctypeCodeEnd" id="acctypeCodeEnd" value="$!conMap.acctypeCodeEnd" size="13" ondblclick="selectCode('acctypeCodeEnd')"/>
						<img src='/style1/images/search.gif' onClick="selectCode('acctypeCodeEnd')" class='search'/></td>
						</tr>
						<tr><td class="tdDes1">科目级别:&nbsp;</td><td><input name="levelStart" id="levelStart" value="$!conMap.levelStart" size="13"/></td></tr>
						#if($!globals.getSysSetting('currency')=="true")
						<tr>
							<td class="tdDes1">币别:&nbsp;</td>
							<td colspan="3">
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
							#if("$!globals.getSysSetting('standardAcc')"=="true")
							<td class="tdDes1"><input name="showItemDetail" id="showItemDetail" value="1" type="checkbox" #if("$conMap.showItemDetail"=="1")checked#end/></td><td><label for="showItemDetail">显示核算项目明细</label></td>
							#end
							#if("$!AccMainSetting.isAuditing"=="1")
								<td class="tdDes1"><input name="binderNo" id="binderNo" value="1" type="checkbox" #if("$conMap.binderNo"=="1")checked#end/></td><td><label for="binderNo">包括未过账凭证</label></td>
							#end
						</tr>
						<!-- <tr>
							<td class="tdDes1"><input name="accTypeNoOperation" id="accTypeNoOperation" value="1" type="checkbox" #if("$conMap.accTypeNoOperation"=="1")checked#end/></td><td colspan="3"><label for="accTypeNoOperation">包括没有业务发生的科目（期初，本年累计）</label></td>
						</tr>
						<tr>
							<td class="tdDes1"><input name="accTypeNoPeriod" id="accTypeNoPeriod" value="1" type="checkbox" #if("$conMap.accTypeNoPeriod"=="1")checked#end/></td><td colspan="3"><label for="accTypeNoPeriod">包括本期没有发生额的科目</label></td>
						</tr> -->
						<tr>
							<!--<td class="tdDes1"><input name="accTypeNoYear" id="accTypeNoYear" value="1" type="checkbox" #if("$conMap.accTypeNoYear"=="1")checked#end/></td><td><label for="accTypeNoYear">包括本年没有发生额的科目</label></td>-->
							<td class="tdDes1"><input name="takeBrowNo" id="takeBrowNo" value="1" type="checkbox" #if("$conMap.takeBrowNo"=="1")checked#end/></td><td><label for="takeBrowNo">包括无发生额的科目</label></td>
						</tr>
						<tr>
							#if("$!globals.getSysSetting('standardAcc')"=="true")
							<td class="tdDes1"><input name="partitionLine" id="partitionLine" value="1" type="checkbox" #if("$conMap.partitionLine"=="1")checked#end/></td><td><label for="partitionLine">核算项目用网络线隔开</label></td>
							#end
							<td class="tdDes1"><input name="showBanAccTypeInfo" id="showBanAccTypeInfo" value="1" type="checkbox" #if("$conMap.showBanAccTypeInfo"=="1")checked#end/></td><td><label for="showBanAccTypeInfo">显示禁用科目</label></td>
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