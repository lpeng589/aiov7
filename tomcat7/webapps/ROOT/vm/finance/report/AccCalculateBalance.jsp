<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>核算项目余额表</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/style1/css/financereport.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/listgrid.css" />
<script type="text/javascript" src="/js/k_listgrid.js"></script>
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
	.w_div .t_span{position:absolute;display:inline-block;z-index:2;background:#fff;padding:0 5px 0 5px;left:20px;line-height:20px;}
	.search_popup ul li{width: auto;height: auto;float: none;}
	.list_li .in_p{float:left;margin:3px 0 0 3px;}
	.list_li .in_lb{float:left;margin:0 0 0 3px;}
	.list_li .in_txt{float:right;}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		#if("$!comeMode"=="")
			showSearchDiv();
			$("#periodYearStart").val("$!accPeriod.accYear");
			$("#periodYearEnd").val("$!accPeriod.accYear");
			$("#periodStart").val("$!accPeriod.accPeriod");
			$("#periodEnd").val("$!accPeriod.accPeriod");
			$("#takeBrowNo").attr("checked","checked");
		#end
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
		var accCode = $("#accCode").val();
		if(accCode == ""){
			asyncbox.tips('会计科目不能为空','提示',1500);
			$("#accCode").focus();
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
		$("#periodYearStart").val('$!accPeriod.accYear');
		$("#periodYearEnd").val('$!accPeriod.accYear');
		$("#periodStart").val('$!accPeriod.accPeriod');
		$("#periodEnd").val('$!accPeriod.accPeriod');
		$("#accCode").val('');
		$("#itemSort").val("DepartmentCode");
		$("#takeBrowNo").attr("checked","checked");
		changecode();
	}
	
	/* 改变项目类别时清空项目代码 */
	function changecode(){
		$("#itemCodeStart").val('');
		$("#itemCodeEnd").val('');
	}
	
	
	var itemObj = "";
	var itemSorts = "";
	/* 搜索的弹出框 */
	function selectPops(itemName){
		var itemSort = $("#itemSort").val();
		itemSorts = itemSort;
		var name = "请选择";
		var urlstr = "/UserFunctionAction.do?operation=22&popupWin=Popdiv&tableName=&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName=&selectName=";
		if(itemSort == "DepartmentCode"){
			//部门弹出框



			urlstr += "SelectAccDepartment";
			name += "部门";
		}else if(itemSort == "EmployeeID"){
			//职员弹出框



			urlstr += "SelectAccEmployee";
			name += "职员";
		}else if(itemSort == "StockCode"){
			urlstr += "SelectAccStocks";
			name += "仓库";
		}else if(itemSort == "ClientCode"){
			urlstr += "SelectAccClient";
			name += "客户";
		}else if(itemSort == "SuplierCode"){
			urlstr += "SelectAccProvider";
			name += "供应商";
		}else if(itemSort == "ProjectCode"){
			urlstr += "SelectAccProject";
			name += "项目";
		}
		itemObj = itemName;
		asyncbox.open({id:'Popdiv',title:name,url:urlstr,width:750,height:470})
	}
	
	/* 返回数据 */
	function exePopdiv(returnValue){
		if(typeof(returnValue)=="undefined") return ;
		var note = returnValue.split("#;#") ;
		if(itemSorts == "EmployeeID"){
			$("#"+itemObj).val(note[3]);
		}else{
			$("#"+itemObj).val(note[1]);
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
	
	/* 打印 */
	function prints(){
		var dataHeight = $("#data_list_id").css("height");
		$("#data_list_id").css("height","100%");
		$(".btndiv").hide();
		window.print();//打印
		$("#data_list_id").css("height",dataHeight);
		$(".btndiv").show();
	}
	
	/* 导出 */
	function exports(){
		form.type.value = "exportList";
		blockUI();
		form.submit();
	}
	
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form method="post" scope="request" id="form" name="form" action="/FinanceReportAction.do?optype=ReportAccCalculateBalance" target="">
<input type="hidden" name="comeMode" id="comeMode" value="false" />
<input type="hidden" name="type" id="type" value=""/>
	<div class="titlediv">
		<img src="/style1/images/workflow/ti_003.gif"/><span style="font-size: 14px;">核算项目余额表</span>
	</div>
	<div style="float: left;padding-bottom: 2px;margin: 20px 10px 10px 10px;">
	#if("$!comeMode"!="")
		期间：$!conMap.periodYearStart年第$!conMap.periodStart期至#if("$!conMap.periodYearStart"!="$!conMap.periodYearEnd")$!conMap.periodYearEnd年#end第$!conMap.periodEnd期



		&nbsp;&nbsp;&nbsp;
		会计科目：$!globals.get($!acctypeData,1)
		&nbsp;&nbsp;&nbsp;
		项目类别：#if("$conMap.itemSort"=="DepartmentCode")部门
				#elseif("$conMap.itemSort"=="EmployeeID")职员
				#elseif("$conMap.itemSort"=="StockCode")仓库
				#elseif("$conMap.itemSort"=="ClientCode")客户
				#elseif("$conMap.itemSort"=="ProjectCode")项目
				#elseif("$conMap.itemSort"=="SuplierCode")供应商#end
		#if($!globals.getSysSetting('currency')=="true") 币别：



			#if("$!conMap.currencyName"=="")
				综合本位币



			#elseif("$!conMap.currencyName"=="all")
				所有币别



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
		#if($LoginBean.operationMap.get("/FinanceReportAction.do?optype=ReportAccCalculateBalance").print() || $!LoginBean.id=="1")
		<div class="op sbtn41" onclick="printReport('FinanceReportAccCalculateBalance','ReportAccCalculateBalance');">
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
		#set($row = 2)
		#set($col = 2)
		#set($col1 = 1)
		#set($flag = "")
		#if("$!currType"=="" || "$!currType"=="isBase")
		#elseif("$!currType"=="all")
			#set($row = 3)
			#set($col = 2*($curCount+1))
			#set($col1 = $curCount+1)
			#set($flag = "true")
		#else
			#set($row = 3)
			#set($col = 4)
			#set($col1 = 2)
			#set($flag = "false")
		#end
			<table class="data_list_head" cellpadding="0" cellspacing="0" border="1" borderColor="#808080"  style="width:100%;overflow:auto;border-collapse:collapse;">
			<thead>
				<tr id="trTitle">
					<td class="tdDes3" rowspan="$!row">期间</td>
					<td class="tdDes3" rowspan="$!row">项目代码</td>
					<td class="tdDes3" rowspan="$!row">项目名称</td>
					<td class="tdDes3" colspan="$!col">期初余额</td>
					<td class="tdDes3" colspan="$!col">本期发生</td>
					<td class="tdDes3" colspan="$!col">本年累计</td>
					<td class="tdDes3" colspan="$!col">期末余额</td>
				</tr>
				<tr>
					#foreach($foo in [1..4])
						<td class="tdDes3" colspan="$!col1">借方</td>
						<td class="tdDes3" colspan="$!col1">贷方</td>
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
					#foreach($foo in [1..8])
						#foreach($currency in $!currencyList)
							<td class="tdDes3">$!globals.get($!currency,1)</td>
						#end
						<td class="tdDes3">本位币</td>
					#end
				</tr>
				#end
			</thead>
			<tbody>
			#foreach($acctype in $accList)
				#foreach($maps in $acctype.keySet())
					#set($obj = $acctype.get($maps))
					<tr style="line-height: 24px;" onclick="setBacks(this)">
						<td>${obj.AccYear}.${obj.AccPeriod}</td>
						<td>$!obj.AccCode</td>
						<td>$!obj.AccFullName</td>
						#if("$!flag"=="false")
							<td class="tdDes1">#if('$!obj.init_sumDebitAmount'!='') $NumericTool.format($!obj.init_sumDebitAmount,'#,##0.00') #end</td>
						#elseif("$!flag"=="true")
							#foreach($currency in $!currencyList)
								#set($currId = "$!globals.get($!currency,0)")
								#if("$!globals.get($!currency,2)"=="true")
									#set($currId="")
								#end
								#set($curName = "init_sumDebitAmount_"+${currId})
								#set($curData = $!obj.get($curName))
								<td class="tdDes1">#if('$!curData'!='') $NumericTool.format($!curData,'#,##0.00') #end</td>
								#set($curData = "")
								#set($curName = "")
								#set($currId = "")
							#end
						#end
						<td>#if('$!obj.init_sumDebitAmountBase'!='') $NumericTool.format($!obj.init_sumDebitAmountBase,'#,##0.00') #end</td>
						#if("$!flag"=="false")
							<td class="tdDes1">#if('$!obj.init_sumLendAmount'!='') $NumericTool.format($!obj.init_sumLendAmount,'#,##0.00') #end</td>
						#elseif("$!flag"=="true")
							#foreach($currency in $!currencyList)
								#set($currId = "$!globals.get($!currency,0)")
								#if("$!globals.get($!currency,2)"=="true")
									#set($currId="")
								#end
								#set($curName = "init_sumLendAmount_"+${currId})
								#set($curData = $!obj.get($curName))
								<td class="tdDes1">$!curData</td>
								#set($curData = "")
								#set($curName = "")
								#set($currId = "")
							#end
						#end
						<td>#if('$!obj.init_sumLendAmountBase'!='') $NumericTool.format($!obj.init_sumLendAmountBase,'#,##0.00') #end</td>
						#if("$!flag"=="false")
							<td class="tdDes1">#if('$!obj.sumDebitAmount'!='') $NumericTool.format($!obj.sumDebitAmount,'#,##0.00') #end</td>
						#elseif("$!flag"=="true")
							#foreach($currency in $!currencyList)
								#set($currId = "$!globals.get($!currency,0)")
								#if("$!globals.get($!currency,2)"=="true")
									#set($currId="")
								#end
								#set($curName = "sumDebitAmount_"+${currId})
								#set($curData = $!obj.get($curName))
								<td class="tdDes1">#if('$!curData'!='') $NumericTool.format($!curData,'#,##0.00') #end</td>
								#set($curData = "")
								#set($curName = "")
								#set($currId = "")
							#end
						#end
						<td>$!obj.sumDebitAmountBase</td>
						#if("$!flag"=="false")
							<td class="tdDes1">#if('$!obj.sumLendAmount'!='') $NumericTool.format($!obj.sumLendAmount,'#,##0.00') #end</td>
						#elseif("$!flag"=="true")
							#foreach($currency in $!currencyList)
								#set($currId = "$!globals.get($!currency,0)")
								#if("$!globals.get($!currency,2)"=="true")
									#set($currId="")
								#end
								#set($curName = "sumLendAmount_"+${currId})
								#set($curData = $!obj.get($curName))
								<td class="tdDes1">#if('$!curData'!='') $NumericTool.format($!curData,'#,##0.00') #end</td>
								#set($curData = "")
								#set($curName = "")
								#set($currId = "")
							#end
						#end
						<td>$!obj.sumLendAmountBase</td>
						#if("$!flag"=="false")
							<td class="tdDes1">$!obj.year_sumDebitAmount</td>
						#elseif("$!flag"=="true")
							#foreach($currency in $!currencyList)
								#set($currId = "$!globals.get($!currency,0)")
								#if("$!globals.get($!currency,2)"=="true")
									#set($currId="")
								#end
								#set($curName = "year_sumDebitAmount_"+${currId})
								#set($curData = $!obj.get($curName))
								<td class="tdDes1">#if('$!curData'!='') $NumericTool.format($!curData,'#,##0.00') #end</td>
								#set($curData = "")
								#set($curName = "")
								#set($currId = "")
							#end
						#end
						<td>#if('$!obj.year_sumDebitAmountBase'!='') $NumericTool.format($!obj.year_sumDebitAmountBase,'#,##0.00') #end</td>
						#if("$!flag"=="false")
							<td class="tdDes1">#if('$!obj.year_sumLendAmount'!='') $NumericTool.format($!obj.year_sumLendAmount,'#,##0.00') #end</td>
						#elseif("$!flag"=="true")
							#foreach($currency in $!currencyList)
								#set($currId = "$!globals.get($!currency,0)")
								#if("$!globals.get($!currency,2)"=="true")
									#set($currId="")
								#end
								#set($curName = "year_sumLendAmount_"+${currId})
								#set($curData = $!obj.get($curName))
								<td class="tdDes1">#if('$!curData'!='') $NumericTool.format($!curData,'#,##0.00') #end</td>
								#set($curData = "")
								#set($curName = "")
								#set($currId = "")
							#end
						#end
						<td>#if('$!obj.year_sumLendAmountBase'!='') $NumericTool.format($!obj.year_sumLendAmountBase,'#,##0.00') #end</td>
						#if("$!flag"=="false")
							<td class="tdDes1">#if('$!obj.end_sumDebitAmount'!='') $NumericTool.format($!obj.end_sumDebitAmount,'#,##0.00') #end</td>
						#elseif("$!flag"=="true")
							#foreach($currency in $!currencyList)
								#set($currId = "$!globals.get($!currency,0)")
								#if("$!globals.get($!currency,2)"=="true")
									#set($currId="")
								#end
								#set($curName = "end_sumDebitAmount_"+${currId})
								#set($curData = $!obj.get($curName))
								<td class="tdDes1">#if('$!curData'!='') $NumericTool.format($!curData,'#,##0.00') #end</td>
								#set($curData = "")
								#set($curName = "")
								#set($currId = "")
							#end
						#end
						
						
						<td>#if('$obj.end_sumDebitAmountBase' != '0')
								$NumericTool.format($obj.end_sumDebitAmountBase,'#,##0.00')
							#end							
						</td>
						#if("$!flag"=="false")
							<td class="tdDes1">#if('$!obj.end_sumLendAmount'!='') $NumericTool.format($!obj.end_sumLendAmount,'#,##0.00') #end</td>
						#elseif("$!flag"=="true")
							#foreach($currency in $!currencyList)
								#set($currId = "$!globals.get($!currency,0)")
								#if("$!globals.get($!currency,2)"=="true")
									#set($currId="")
								#end
								#set($curName = "end_sumLendAmount_"+${currId})
								#set($curData = $!obj.get($curName))
								<td class="tdDes1">#if('$!curData'!='') $NumericTool.format($!curData,'#,##0.00') #end</td>
								#set($curData = "")
								#set($curName = "")
								#set($currId = "")
							#end
						#end
						<td>#if('$obj.end_sumLendAmountBase' != '0')
								$NumericTool.format($obj.end_sumLendAmountBase,'#,##0.00')
							#end
						</td>
					</tr>
				#end
			#end
			</tbody>
		</table>
		#if("$!comeMode"=="")
			<div class="querydiv1">
			点击条件查询按钮，进行数据查询



			</div>
		#elseif($accList.size()==0)
			<div class="querydiv1">
				<p style="width:150px;padding:50px 0 0 0;overflow:hidden;line-height:21px;margin:0 auto;">
					很抱歉，没有找到与您条件匹配的核算项目余额表信息!
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
						<tr id="periodtr"><td class="tdDes1">会计期间:&nbsp;</td><td><input name="periodYearStart" id="periodYearStart" style="width:40px;" value="$!conMap.periodYearStart" size="2"/>&nbsp;年



							<input name="periodStart" id="periodStart" style="width:40px;" value="$!conMap.periodStart" size="2"/>&nbsp;期</td>
						<td class="tdDes1">至&nbsp;</td><td><input name="periodYearEnd" id="periodYearEnd" style="width:40px;" value="$!conMap.periodYearEnd" size="2"/>&nbsp;年



							<input name="periodEnd" id="periodEnd" style="width:40px;" value="$!conMap.periodEnd" size="2"/>&nbsp;期</td>
						</tr>
						<tr><td class="tdDes1">会计科目:&nbsp;</td><td style="white-space: nowrap;" class="searchinput" colspan="3"><input name="accCode" id="accCode" value="$!conMap.accCode" size="13" ondblclick="selectCode('accCode')" />
							<img src='/style1/images/search.gif' onclick="selectCode('accCode')" class='search' /><span style="color:red;">*</span>
							</td>
						</tr>
						<tr>
							#if($!globals.getSysSetting('currency')=="true")
							<td class="tdDes1">币别:&nbsp;</td>
							<td>
								<select name="currencyName" id="currencyName" style="width:110px;">
									#foreach($currency in $!currencyList)
										<option value="$!globals.get($!currency,0)" #if("$!conMap.currencyName"=="$!globals.get($!currency,0)")selected#end>$!globals.get($!currency,1)</option>
									#end
									<option value="" #if("$!conMap.currencyName"=="")selected#end><综合本位币></option>
									<option value="all" #if("$!conMap.currencyName"=="all")selected#end><所有币别></option>
								</select>
							</td>
							#end
							<td class="tdDes1">项目类别:&nbsp;</td>
							<td>
								<select name="itemSort" id="itemSort" style="width:110px;" onchange="changecode()">
									<option value="DepartmentCode" #if("$conMap.itemSort"=="DepartmentCode")selected#end>部门</option>
									<option value="EmployeeID" #if("$conMap.itemSort"=="EmployeeID")selected#end>职员</option>
									<option value="StockCode" #if("$conMap.itemSort"=="StockCode")selected#end>仓库</option>
									#set($isHide = $!globals.hideViewRight('tblAccTypeInfo','','Customer','/FinanceReportAction.do?optype=ReportAccCalculateBalance'))
									#if($isHide)
									<option value="ClientCode" #if("$conMap.itemSort"=="ClientCode")selected#end>客户</option>
									#end
									#if($!globals.getSysSetting('Project')=="true")
									<option value="ProjectCode" #if("$conMap.itemSort"=="ProjectCode")selected#end>项目</option>
									#end
									#set($isHide = $!globals.hideViewRight('tblAccTypeInfo','','Supplier','/FinanceReportAction.do?optype=ReportAccCalculateBalance'))
									#if($isHide)
									<option value="SuplierCode" #if("$conMap.itemSort"=="SuplierCode")selected#end>供应商</option>
									#end
								</select>
							</td>
						</tr>
						<tr><td class="tdDes1">项目代码:&nbsp;</td><td style="white-space: nowrap;" class="searchinput"><input name="itemCodeStart" id="itemCodeStart" value="$!conMap.itemCodeStart" size="13" ondblclick="selectPops('itemCodeStart')" />
							<img src='/style1/images/search.gif' onClick="selectPops('itemCodeStart')" class='search' />
							</td><td class="tdDes1">至&nbsp;</td><td style="white-space: nowrap;" class="searchinput">
							<input name="itemCodeEnd" id="itemCodeEnd" value="$!conMap.itemCodeEnd" size="13" ondblclick="selectPops('itemCodeEnd')"/>
							<img src='/style1/images/search.gif' onClick="selectPops('itemCodeEnd')" class='search'/></td>
						</tr>
						<!-- <tr>
							<td class="tdDes1">项目级次:&nbsp;</td>
							<td colspan="3"><input name="levelStart" id="levelStart" value="$!conMap.levelStart" style="width:40px;"/></td>
						</tr> -->
						</thead>
						<tbody>
						<tr>
							#if("$!AccMainSetting.isAuditing"=="1")
								<td class="tdDes1"><input name="binderNo" id="binderNo" value="1" type="checkbox" #if("$conMap.binderNo"=="1")checked#end/></td><td><label for="binderNo">包括未过账凭证</label></td>
							#end
							<td class="tdDes1"><input name="takeBrowNo" id="takeBrowNo" value="1" type="checkbox" #if("$conMap.takeBrowNo"=="1")checked#end/></td><td><label for="takeBrowNo">余额为零不显示</label></td>
						</tr>
						<tr>
							<td class="tdDes1"><input name="balanceAndTakeBrowNo" id="balanceAndTakeBrowNo" value="1" type="checkbox" #if("$conMap.balanceAndTakeBrowNo"=="1")checked#end/></td><td><label for="balanceAndTakeBrowNo">显示发生额为零的记录</label></td>
							<td class="tdDes1"><input name="showBanAccTypeInfo" id="showBanAccTypeInfo" value="1" type="checkbox" #if("$conMap.showBanAccTypeInfo"=="1")checked#end/></td><td><label for="showBanAccTypeInfo">显示禁用科目</label></td>
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