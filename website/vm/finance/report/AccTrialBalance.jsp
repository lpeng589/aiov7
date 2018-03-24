<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>试算平衡表</title>
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
			$("#periodStart").val("$!accPeriod.accPeriod");
			$("#accTypeNoItem").attr("checked","checked");
		#end
		/* 根据数字金额转换为大写金额 */
		$(".data_list_head tbody").find(".tdDes1").mouseover(function(){
			var objhtml = $(this).html();
			if(objhtml!=null && objhtml!="" && objhtml!="&nbsp;"){
				var changeValue = ChangeToBig(objhtml);
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
		$("#periodStart").val("$!accPeriod.accPeriod");
		$("#levelStart").val('');
		$("#accTypeNoItem").attr("checked","checked");
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
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form method="post" scope="request" id="form" name="form" action="/FinanceReportAction.do?optype=ReporttblTrialBalance" target="">
<input type="hidden" name="comeMode" id="comeMode" value="false" />
<input type="hidden" id="org.apache.struts.taglib.html.TOKEN" name="org.apache.struts.taglib.html.TOKEN" value=""/>
<input type="hidden" id="SQLSave" name="SQLSave" value="@SQL:$saveSql@ParamList:@end:"/>
<input type="hidden" id="type" name="type" value=""/>
	<div class="titlediv">
		<img src="/style1/images/workflow/ti_003.gif"/><span style="font-size: 14px;">试算平衡表</span>
	</div>
	<div style="float:left;display:inline-block;padding:0 0 2px 0;margin: 0 10px 10px 100px;">
		#if("$!comeMode"!="")
		<div style="float:left;display:inline-block;margin:20px 0 0 0;">
			期间：$!conMap.periodYearStart年第$!conMap.periodStart期



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
			</div>
			<div style="float:left;display:inline-block;margin:10px 0 0 15px;">
				#if("$!totalData.flag"=="true")
					<span style="float:left;width:32px;height:32px;display:inline-block;background:url(/style1/images/sxo.jpg) no-repeat 0 0;"></span>
					<span style="float:left;margin:10px 0 0 5px;">试算结果平衡</span>
				#else
					<span style="float:left;width:32px;height:32px;display:inline-block;background:url(/style1/images/sxo.jpg) no-repeat 0 -66px;"></span>
					<span style="float:left;margin:10px 0 0 5px;color: red;">试算结果不平衡</span>
				#end
			</div>
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
		#if($LoginBean.operationMap.get("/FinanceReportAction.do?optype=ReporttblTrialBalance").print() || $!LoginBean.id=="1")
		<div class="op sbtn41" onclick="printReport('FinanceReportAccTrialBalance','ReporttblTrialBalance');">
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
			<table class="data_list_head" cellpadding="0" cellspacing="0" border="1" borderColor="#808080"  style="width:98%;overflow:auto;border-collapse:collapse;">
			<thead>
				<tr id="trTitle">
					<td class="tdDes3" rowspan="$!row">科目代码</td>
					<td class="tdDes3" rowspan="$!row">科目名称</td>
					<td class="tdDes3" colspan="$!col">期初余额</td>
					<td class="tdDes3" colspan="$!col">本期发生额</td>
					<td class="tdDes3" colspan="$!col">期末余额</td>
				</tr>
				<tr>
					#foreach($foo in [1..3])
						<td class="tdDes3" colspan="$!nextcol">借方</td>
						<td class="tdDes3" colspan="$!nextcol">贷方</td>
					#end
				</tr>
				#if("$!flag"=="false")
					<tr>
						#foreach ( $foo in [1..6] )
							<td class="tdDes3">原币</td>
							<td class="tdDes3">本位币</td>
						#end
					</tr>
				#elseif("$!flag"=="true")
					<tr>
					#foreach ( $foo in [1..6] )
					#foreach($currency in $!currencyList)
						<td class="tdDes3">$!globals.get($!currency,1)</td>
					#end
						<td class="tdDes3">本位币</td>
					#end
					</tr>
				#end
			</thead>
			<tbody>
				#if("$flag" == "" || "$flag" == "true")
					#set($list=["PeriodIniDebitBase","PeriodIniCreditBase","PeriodDebitSumBase","PeriodCreditSumBase","PeriodDebitBalaBase","PeriodCreditBalaBase"])
				#elseif("$flag"=="false")
					#set($list=["PeriodIniDebit","PeriodIniDebitBase","PeriodIniCredit","PeriodIniCreditBase","PeriodDebitSum","PeriodDebitSumBase","PeriodCreditSum","PeriodCreditSumBase","PeriodDebitBala","PeriodDebitBalaBase","PeriodCreditBala","PeriodCreditBalaBase"])
				#end
				#foreach($accMap in $accTypeInfoList)
					<tr  style="line-height: 24px;" onclick="setBacks(this)">
						<td>$!accMap.AccNumber</td>
						<td>$!accMap.AccFullName</td>
						#foreach ($element in $list)
							#if("$flag" == "true")
								#foreach($currency in $!currencyList)
									#set($curId = $!globals.get($!currency,0))
									#if($!globals.get($!currency,2)=="true")
										#set($curId = "")
									#end
									#set($curName = $!globals.replaceString(${element},'Base','')+"_"+${curId})
									#set($curData = $!accMap.get($curName))
									<td class="tdDes1">#if('$!curData'!='') $NumericTool.format($!curData,'#,##0.00') #end</td>
									#set($curData = "")
									#set($curName = "")
									#set($curId = "")
								#end
							#end
							#set($valueData = $!accMap.get($element))
							<td class="tdDes1">#if('$!valueData'!='') $NumericTool.format($!valueData,'#,##0.00') #end</td>
							#set($valueData="")
						#end
					</tr>
				#end
				#if($accTypeInfoList.size()!=0)
				<tr style="line-height: 24px;">
					<td></td>
					<td>合计</td>
					#foreach ($element in $list)
						#if("$flag" == "true")
							#foreach($currency in $!currencyList)
								#set($curId = $!globals.get($!currency,0))
								#if($!globals.get($!currency,2)=="true")
									#set($curId = "")
								#end
								#set($curName = $!globals.replaceString(${element},'Base','')+"_"+${curId})
								#set($curData = $!totalData.get($curName))
								<td class="tdDes1">#if('$!curData'!='') $NumericTool.format($!curData,'#,##0.00') #end</td>
								#set($curData = "")
								#set($curName = "")
								#set($curId = "")
							#end
						#end
						#set($valueData = $!totalData.get($element))
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
		#elseif($accTypeInfoList.size()==0)
			<div class="querydiv1">
				<p style="width:150px;padding:50px 0 0 0;overflow:hidden;line-height:21px;margin:0 auto;">
					很抱歉，没有找到与您条件匹配的试算平衡表信息!
				</p>
			</div>
		#end
		</div>
		<!-- 条件查询 -->
		<div id="highSearch" class="search_popup" style="display:none;height: 290px;min-width:492px;top: 220px;">
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
						<tr><td class="tdDes1" width="15%">会计期间:&nbsp;</td><td width="35%"><input name="periodYearStart" id="periodYearStart" value="$!conMap.periodYearStart" style="width:40px;" size="2"/>&nbsp;年



							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="periodStart" id="periodStart" value="$!conMap.periodStart" style="width:40px;" size="2"/>&nbsp;期</td>
						</tr>
						<tr><td class="tdDes1">科目级别:&nbsp;</td><td><input name="levelStart" id="levelStart" value="$!conMap.levelStart" size="2"/></td>
						</tr>
						#if($!globals.getSysSetting('currency')=="true")
						<tr>
							<td class="tdDes1">币别:&nbsp;</td>
							<td>
								<select name="currencyName" id="currencyName" style="width:150px">
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
							<td class="tdDes1"><input name="showDetail" id="showDetail" value="1" type="checkbox" #if("$conMap.showDetail"=="1")checked#end/></td><td><label for="showDetail">只显示明细科目</label></td>
							#if("$!globals.getSysSetting('standardAcc')"=="true")
							<td class="tdDes1"><input name="showItemDetail" id="showItemDetail" value="1" type="checkbox" #if("$conMap.showItemDetail"=="1")checked#end/></td><td><label for="showItemDetail">显示核算项目明细</label></td>
							#else
							<td>&nbsp;</td>
							#end
						</tr>
						<tr>
							#if("$!AccMainSetting.isAuditing"=="1")
								<td class="tdDes1">
									<input name="binderNo" id="binderNo" value="1" type="checkbox" #if("$conMap.binderNo"=="1")checked#end/></td><td><label for="binderNo">包括未过账凭证</label>
								</td>
							#end
							<td class="tdDes1"><input name="showBanAccTypeInfo" id="showBanAccTypeInfo" value="1" type="checkbox" #if("$conMap.showBanAccTypeInfo"=="1")checked#end/></td><td colspan="3"><label for="showBanAccTypeInfo">显示禁用科目</label></td>
						</tr>
						<tr>
							<td class="tdDes1">
								<input name="accTypeNoItem" id="accTypeNoItem" value="1" type="checkbox" #if("$conMap.accTypeNoItem"=="1")checked#end/></td><td><label for="accTypeNoItem">科目无记录不显示</label>
							</td>
						</tr>
						</tbody>
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