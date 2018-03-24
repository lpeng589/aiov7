<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>科目日报表</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/style1/css/financereport.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/financereport.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<style type="text/css">
	.search_popup{height: 60%}
	.listRange_pagebar{width: auto;};
	.bordertr td{border: 1px;}
	.hideBorder{border-bottom: 0px;border-top: 0px;}
	.hideBottom{border-bottom: 0px;}
	.tr_back{background: #E7FCA9;}
	.data_list_head tr td{padding:2px 10px;word-break:keep-all;white-space:nowrap;}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		#if("$!comeMode"=="")
			showSearchDiv();
			$("#dateStart").val("$!globals.getDate()");
			$("#dateEnd").val("$!globals.getDate()");
			$("#levelStart").val(1);
			$("#levelEnd").val(7);
			$("#takeBrowNo").attr("checked","checked");
		#end
	});
	//查询
	function onsubmits(){
		if(!isvalidate()){
			return false;
		}
		var dateStart = $("#dateStart").val();
		var dateEnd = $("#dateEnd").val();
		if(dateStart == ""){
			asyncbox.tips('日期开始不能为空','提示',1500);
			$("#dateStart").focus();
			return false;
		}
		if(dateEnd == ""){
			asyncbox.tips('日期结束不能为空','提示',1500);
			$("#dateEnd").focus();
			return false;
		}
		if(dateStart>dateEnd){
			asyncbox.tips('日期开始不能大于日期结束','提示',1500);
			$("#dateEnd").focus();
			return false;
		}
		blockUI();
		form.submit();
	}
	
	/* 清空 */
	function cleaDate(){
		$("#highSearch").find("input[type=checkbox]").each(function (){
			$(this).removeAttr("checked");
		});
		$("#dateStart").val("$!globals.getDate()");
		$("#dateEnd").val("$!globals.getDate()");
		$("#levelStart").val(1);
		$("#levelEnd").val(7);
		$("#acctypeCodeStart").val('');
		$("#acctypeCodeEnd").val('');
		$("#takeBrowNo").attr("checked","checked");
	}
	
	/* 打印列表*/
	function prints(){
		var dataHeight = $("#data_list_id").css("height");
		$("#data_list_id").css("height","100%");
		$("#data_list_id").show();
		$(".btndiv").hide();
		window.print();//打印
		$("#data_list_id").css("height",dataHeight);
		$(".btndiv").show();
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
	
	var tr_obj = null;
	function setBacks(obj){
		if(tr_obj!=null){
			$(tr_obj).removeClass("tr_back");
		}
		$(obj).addClass("tr_back");
		tr_obj = obj;
	}
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form method="post" scope="request" id="form" name="form" action="/FinanceReportAction.do?optype=ReportAccTypeDayBalance" target="">
<input type="hidden" name="comeMode" id="comeMode" value="false" />
<input type="hidden" name="type" id="type" value=""/>
	<div class="titlediv">
		<img src="/style1/images/workflow/ti_003.gif"/><span style="font-size: 14px;">科目日报表</span>
	</div>
	<div style="float: left;padding-bottom: 2px;margin: 20px 10px 10px 100px;">
	#if("$!comeMode"!="")
		日期区间: $!conMap.dateStart 至 $!conMap.dateEnd
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
		#if($LoginBean.operationMap.get("/FinanceReportAction.do?optype=ReportAccTypeDayBalance").print() || $!LoginBean.id=="1")
		<div class="op sbtn41" onclick="printReport('FinanceReportAccTypeDayBalance','ReportAccTypeDayBalance');">
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
		#set($flag = "")
		#if("$!currType"=="" || "$!currType"=="isBase")
		#elseif("$!currType"=="all")
			#set($row = 2)
			#set($col = $curCount+1)
			#set($flag = "true")
		#else
			#set($row = 2)
			#set($col = 2)
			#set($flag = "false")
		#end
		<div id="data_list_id" class="rt_table3">
		<script type="text/javascript"> 
			var oDiv=document.getElementById("data_list_id");
			var sHeight=document.documentElement.clientHeight-100;
			oDiv.style.height=sHeight+"px";
		</script>
			<table class="data_list_head" cellpadding="0" cellspacing="0" border="1" borderColor="#808080"  style="table-layout:fixed;overflow:auto;border-collapse:collapse;">
			<thead>
				<tr id="trTitle">
					<td class="tdDes3" rowspan="$!row" width="50px;">No.</td>
					<td class="tdDes3" rowspan="$!row">科目代码</td>
					<td class="tdDes3" rowspan="$!row">科目名称</td>
					<td class="tdDes3" rowspan="$!row">方向</td>
					<td class="tdDes3" colspan="$!col">上日余额</td>
					<td class="tdDes3" colspan="$!col">本日借方发生额</td>
					<td class="tdDes3" colspan="$!col">本日贷方发生额</td>
					<td class="tdDes3" rowspan="$!row">方向</td>
					<td class="tdDes3" colspan="$!col">本日余额</td>
					<td class="tdDes3" rowspan="$!row">借方笔数</td>
					<td class="tdDes3" rowspan="$!row">贷方笔数</td>
				</tr>
				#if("$!flag"=="false")
				<tr>
					#foreach ( $foo in [1..4] )
						<td class="tdDes3">原币</td>
						<td class="tdDes3">本位币</td>
					#end
				</tr>
				#elseif("$!flag"=="true")
				<tr>
					#foreach($foo in [1..4])
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
				#set($count=1)
				#foreach($acctype in $!accTypeInfoList)
					<tr style="line-height: 24px;" onclick="setBacks(this)">
						<td class="tdDes3">$!count</td>
						<td>$!acctype.AccNumber</td>
						<td>$!acctype.AccFullName</td>
						<td class="tdDes3">#if("$!acctype.get('JdFlag')"=="1")借#else 贷#end</td>
						#if("$!flag"=="false")
							#set($curData = $!preMap.get("$!{acctype.classCode}").get("sumdisAmount"))
							<td class="tdDes1">#if('$!curData'!='') $NumericTool.format($!curData,'#,##0.00') #end</td>
						#elseif("$!flag"=="true")
							#foreach($currency in $!currencyList)
								#set($currId = "$!globals.get($!currency,0)")
								#if("$!globals.get($!currency,2)"=="true")
									#set($currId="")
								#end
								#set($curName = "sumdisAmount_"+${currId})
								#set($curData = $!preMap.get("$!{acctype.classCode}").get($curName))
								<td class="tdDes1">#if('$!curData'!='') $NumericTool.format($!curData,'#,##0.00') #end</td>
								#set($curData = "")
								#set($curName = "")
								#set($currId = "")
							#end
						#end
						<td class="tdDes1">						
							#set($val = $!preMap.get("$!{acctype.classCode}").get("sumdisAmountBase"))
							#if('$val'!='') $NumericTool.format($val,'#,##0.00') #end							
						</td>
						#if("$!flag"=="false")
							<td class="tdDes1">								
								#if('$!periodMap.get("$!{acctype.classCode}").get("sumDebitAmount")'!='') $NumericTool.format($!periodMap.get("$!{acctype.classCode}").get("sumDebitAmount"),'#,##0.00') #end
							</td>
						#elseif("$!flag"=="true")
							#foreach($currency in $!currencyList)
								#set($currId = "$!globals.get($!currency,0)")
								#if("$!globals.get($!currency,2)"=="true")
									#set($currId = "")
								#end
								#set($curName = "sumDebitAmount_"+${currId})
								#set($curData = $!periodMap.get("$!{acctype.classCode}").get($curName))
								<td class="tdDes1">#if('$!curData'!='') $NumericTool.format($!curData,'#,##0.00') #end</td>
								#set($curData = "")
								#set($curName = "")
								#set($currId = "")
							#end
						#end
						<td class="tdDes1">					
						#if('$!periodMap.get("$!{acctype.classCode}").get("sumDebitAmountBase")'!='') $NumericTool.format($!periodMap.get("$!{acctype.classCode}").get("sumDebitAmountBase"),'#,##0.00') #end
						</td>
						#if("$!flag"=="false")
							<td class="tdDes1">								
								#if('$!periodMap.get("$!{acctype.classCode}").get("sumLendAmount")'!='') $NumericTool.format($!periodMap.get("$!{acctype.classCode}").get("sumLendAmount"),'#,##0.00') #end
							</td>
						#elseif("$!flag"=="true")
							#set($currs = "")
							#foreach($currency in $!currencyList)
								#set($currId = "$!globals.get($!currency,0)")
								#if("$!globals.get($!currency,2)"=="true")
									#set($currId = "")
								#end
								#set($curName = "sumLendAmount_"+${currId})
								#set($curData = $!periodMap.get("$!{acctype.classCode}").get($curName))
								<td class="tdDes1">#if('$!curData'!='') $NumericTool.format($!curData,'#,##0.00') #end</td>
								#set($curData = "")
								#set($curName = "")
								#set($currId = "")
							#end
						#end
						<td class="tdDes1">
							#if('$!periodMap.get("$!{acctype.classCode}").get("sumLendAmountBase")'!='') $NumericTool.format($!periodMap.get("$!{acctype.classCode}").get("sumLendAmountBase"),'#,##0.00') #end							
						</td>
						<td class="tdDes3">#if("$!acctype.get('JdFlag')"=="1")借#else 贷#end</td>
						#if("$!flag"=="false")
							<td class="tdDes1">								
								#if('$!periodMap.get("$!{acctype.classCode}").get("totalsumdisAmount")'!='') $NumericTool.format($!periodMap.get("$!{acctype.classCode}").get("totalsumdisAmount"),'#,##0.00') #end
							</td>
						#elseif("$!flag"=="true")
							#set($currs = "")
							#foreach($currency in $!currencyList)
								#set($currId = "$!globals.get($!currency,0)")
								#if("$!globals.get($!currency,2)"=="true")
									#set($currId = "")
								#end
								#set($curName = "totalsumdisAmount_"+${currId})
								#set($curData = $!periodMap.get("$!{acctype.classCode}").get($curName))
								<td class="tdDes1">#if('$!curData'!='') $NumericTool.format($!curData,'#,##0.00') #end</td>
								#set($curData = "")
								#set($curName = "")
								#set($curId = "")
							#end
						#end
						<td class="tdDes1">							
							#if('$!periodMap.get("$!{acctype.classCode}").get("totalsumdisAmountBase")'!='') $NumericTool.format($!periodMap.get("$!{acctype.classCode}").get("totalsumdisAmountBase"),'#,##0.00') #end							
						</td>
						<td class="tdDes1">$!countMap.get("$!{acctype.classCode}_debit").get("count")</td>
						<td class="tdDes1">$!countMap.get("$!{acctype.classCode}_lend").get("count")</td>
					</tr>
					#set($count=$count+1)
				#end
				#if($!accTypeInfoList.size()!=0 && "$!conMap.showTotal"=="1")
				<tr style="line-height: 24px;background-color:lightyellow;">
					<td></td>
					<td></td>
					<td>合计</td>
					<td class="tdDes3"></td>
					#if("$flag" == "" || "$flag" == "true")
						#set($list=["pre_sumdisAmountBase","period_sumDebitAmountBase","period_sumLendAmountBase","JdFlag","period_totalsumdisAmountBase"])
					#elseif("$flag"=="false")
						#set($list=["pre_sumdisAmount","pre_sumdisAmountBase","period_sumDebitAmount","period_sumDebitAmountBase","period_sumLendAmount","period_sumLendAmountBase","JdFlag","period_totalsumdisAmount","period_totalsumdisAmountBase"])
					#end
					#foreach($element in $list)
						#set($valueData = $!totalMap.get($element))
						#set($setclass="tdDes1")
						#if("$!element"=="JdFlag")
							#set($setclass="tdDes3")
						#end
						#if("$flag" == "true")
							#foreach($currency in $!currencyList)
								#if("$!element"!="JdFlag")
									#set($curId = $!globals.get($!currency,0))
									#if($!globals.get($!currency,2)=="true")
										#set($curId = "")
									#end
									#set($curName = $!globals.replaceString(${element},'Base','')+"_"+${curId})
									#set($curData = $!totalMap.get($curName))
									<td class="$!setclass">#if('$!curData'!='') $NumericTool.format($!curData,'#,##0.00') #end</td>
									#set($curData = "")
									#set($curName = "")
									#set($curId = "")
								#end
							#end
						#end
						<td class="$!setclass">#if('$!valueData'!='') $NumericTool.format($!valueData,'#,##0.00') #end</td>
						#set($valueData="")
					#end
					<td class="tdDes1">$!totalMap.get("debitCount")</td>
					<td class="tdDes1">$!totalMap.get("lendCount")</td>
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
					很抱歉，没有找到与您条件匹配的科目日报表信息!
				</p>
			</div>
		#end
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
						<tr id="datetr"><td class="tdDes1">日期:&nbsp;</td><td><input name="dateStart" id="dateStart" value="$!conMap.dateStart" size="13" onclick="openInputDate(this);"/></td>
						<td class="tdDes1">至&nbsp;</td><td><input name="dateEnd" id="dateEnd" value="$!conMap.dateEnd" size="13" onclick="openInputDate(this);"/></td>
						</tr>
						<tr><td class="tdDes1">科目代码:&nbsp;</td><td style="white-space: nowrap;" class="searchinput"><input name="acctypeCodeStart" id="acctypeCodeStart" value="$!conMap.acctypeCodeStart" size="13" ondblclick="selectCode('acctypeCodeStart')" />
						<img src='/style1/images/search.gif' onclick="selectCode('acctypeCodeStart')" class='search' />
						</td><td class="tdDes1">至&nbsp;</td><td style="white-space: nowrap;" class="searchinput">
						<input name="acctypeCodeEnd" id="acctypeCodeEnd" value="$!conMap.acctypeCodeEnd" size="13" ondblclick="selectCode('acctypeCodeEnd')"/>
						<img src='/style1/images/search.gif' onclick="selectCode('acctypeCodeEnd')" class='search'/></td>
						</tr>
						<tr><td class="tdDes1">科目级别:&nbsp;</td>
							<!-- <td><input name="levelStart" id="levelStart" value="$!conMap.levelStart" size="13"/></td> 
						<td class="tdDes1">至&nbsp;</td>--><td><input name="levelEnd" id="levelEnd" value="$!conMap.levelEnd" size="13"/></td>
						</tr>
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
							#if("$!AccMainSetting.isAuditing"=="1")
								<td class="tdDes1"><input name="binderNo" id="binderNo" value="1" type="checkbox" #if("$conMap.binderNo"=="1")checked#end/></td><td><label for="binderNo">包括未过账凭证</label></td>
							#end
							<td class="tdDes1"><input name="balanceZero" id="balanceZero" value="1" type="checkbox" #if("$conMap.balanceZero"=="1")checked#end/></td><td><label for="balanceZero">余额为零不显示</label></td>
						</tr>
						<tr>
							<td class="tdDes1"><input name="takeBrowNo" id="takeBrowNo" value="1" type="checkbox" #if("$conMap.takeBrowNo"=="1")checked#end/></td><td><label for="takeBrowNo">无发生额不显示</label></td>
							<td class="tdDes1"><input name="showTotal" id="showTotal" value="1" type="checkbox" #if("$conMap.showTotal"=="1")checked#end/></td><td><label for="showTotal">输出合计</label></td>
						</tr>
						<tr>
							#if("$!globals.getSysSetting('standardAcc')"=="true")
							<td class="tdDes1"><input name="showItemDetail" id="showItemDetail" value="1" type="checkbox" #if("$conMap.showItemDetail"=="1")checked#end/></td><td><label for="showItemDetail">包括核算项目明细</label></td>
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