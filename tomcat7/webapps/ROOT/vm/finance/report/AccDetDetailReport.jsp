<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>明细分类账详情</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/style1/css/financereport.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/financereport.js"></script>

<style type="text/css">
.hideBorder{border-top: 0px;border-bottom: 0px;}
.hideBorder1{border-top: 0px;}
.hideBottom{border-bottom: 0px;}
.backcolor{background-color:lightyellow;}
.backcolor1{background-color:#EEF1E0;}
.td_height{height:22px;}
.tr_back{background: #E7FCA9;}
.drapTable{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}
.tdDes3{padding:0 10px;}
</style>

<script type="text/javascript">
	function blockui(){
		blockUI();
	}
	
	$(document).ready(function(){
		$(".data_list_head tbody").find("td").addClass("td_height");
		/* 根据数字金额转换为大写金额 */
		$(".data_list_head tbody").find(".tdDes1").mouseover(function(){
			var objhtml = $(this).html();
			if(objhtml!=null && objhtml!="" && objhtml!="&nbsp;"){
				$(this).attr("title",AmountInWords(objhtml,null));
			}
		});
		
		if($(".data_list_head").width() < $("#data_list_id").width()){
			$(".data_list_head").width("100%");
		};
	});
	
	function detailVoucher(id){
		var title="会计凭证";
		var url = "/VoucherManageAction.do?operation=5&tableName=tblAccMain&id="+id;
		if(typeof(window.parent.frames["f_mainFrame"])=="undefined"){
			window.open(url);
		}else{
			top.mdiwin(url,title);
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
		window.print();//打印
		$("#data_list_id").css("height",dataHeight);
	}
	
	/* 导出 */
	function exports(){
		var url = window.location.href;
		url += "&dealtype=exportList";
		window.location.href = url;
		blockUI();
	}
	
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form method="post" scope="request" id="form" name="form" action="/FinanceReportAction.do?optype=ReporttblAccDet&type=detail" target="">
<input type="hidden" name="dealtype" id="dealtype" value=""/>
<div style="width: 99%;">
<div id="data_list_id" class="rt_table3">
	<script type="text/javascript"> 
		var oDiv=document.getElementById("data_list_id");
		var sHeight=document.documentElement.clientHeight-20;
		oDiv.style.height=sHeight+"px";
	</script>
	#set($sizes = 2)
	#set($widths = "60px")
	#set($curr = "false")
	#set($row = 1)
	#set($col = 1)
	#set($col1 = 2)
	#set($flag = "")
	#if("$!currType"=="all")
		#set($size1 = $!currencyList.size())
		#set($col = $size1+1)
		#set($row = 2)
		#set($col1 = $size1+2)
		#set($widths = 100*$sizes+"px")
		#set($flag = "all")
	#elseif("$!currType"=="currency")
		#set($flag = "allCurr")
	#elseif("$!currType"=="" || "$!currType"=="isBase")
		#set($widths = "100px")
		#set($flag = "isBase")
	#else
		#set($row = 2)
		#set($col = 2)
		#set($col1 = 3)
		#set($flag = "curr")
	#end
	<table class="data_list_head" cellpadding="0" cellspacing="0" border="1" bordercolor="#808080" style="overflow:auto;border-collapse:collapse;">
		<thead>
			<tr id="trTitle">
				<td class="tdDes3" style="min-width:90px;width:10%;" rowspan="$!row">日期</td>
				#if("$!conMap.showDate"=="1")
				<td class="tdDes3" style="min-width:90px;width:10%;" rowspan="$!row">业务日期</td>
				#end
				<td class="tdDes3" style="min-width:120px;width:10%;" rowspan="$!row">凭证字号</td>
				<td class="tdDes3" style="min-width:120px;width:10%;" rowspan="$!row">摘要</td>
				#if("$!conMap.showInAccTypeInfo"=="1")
				<td class="tdDes3" rowspan="$!row">对方科目</td>
				#end
				#if("$!conMap.showMessage"=="1")
				<!-- 显示系统模块，业务描述列 -->
				<td class="tdDes3" style="min-width:120px;width:10%;" rowspan="$!row">系统模块</td>
				<td class="tdDes3" style="min-width:120px;width:10%;" rowspan="$!row">业务描述</td>
				#end
				<!-- <td width="60px" class="tdDes3" #if("$flag"=="true")rowspan="2"#end></td> -->
				#if("$flag"=="curr" || "$flag" == "allCurr")
					<!-- 所有币种 -->
					#if("$flag" == "allCurr")
						<td class="tdDes3" rowspan="$!row">币别</td>
					#end
					<td class="tdDes3" rowspan="$!row">汇率</td>
					#if("$flag" == "allCurr")
						<td class="tdDes3" rowspan="$!row">原币金额</td>
					#end
				#end
				<td class="tdDes3" style="min-width:120px;width:10%;" colspan="$!col">借方</td>
				<td class="tdDes3" style="min-width:120px;width:10%;" colspan="$!col">贷方</td>
				<td class="tdDes3" style="min-width:120px;width:18%;" colspan="$!col1">余额</td>
			</tr>
			#if("$!flag"=="all")
			<tr>
				#foreach($foo in [1..3])
					#if($foo == 3)
						<td class="tdDes3" width="50px"></td>
					#end
					#foreach($currency in $!currencyList)
						<td class="tdDes3" width="50px">$!globals.get($!currency,1)</td>
					#end
					<td class="tdDes3" width="50px">本位币</td>
				#end
			</tr>
			#elseif("$!flag"=="curr")
			<tr>
				#foreach ( $foo in [1..3] )
					#if($foo == 3)
						<td class="tdDes3" width="50px"></td>
					#end
					<td class="tdDes3">原币</td>
					<td class="tdDes3">本位币</td>
				#end
			</tr>
			#end
		</thead>
		<tbody>
			#set($accnumber="")
			#set($counts=1)
			#if("$flag" == "isBase" || "$flag" == "allCurr" || "$flag" == "all")
				#set($list=["sumDebitAmountBase","sumLendAmountBase","isflag","sumBalanceAmountBase"])
				#set($listInit=["","","isflag","sumBalanceAmountBase"])
			#elseif("$flag"=="curr")
				#set($list=["sumDebitAmount","sumDebitAmountBase","sumLendAmount","sumLendAmountBase","isflag","sumBalanceAmount","sumBalanceAmountBase"])
				#set($listInit=["","","","","isflag","sumBalanceAmount","sumBalanceAmountBase"])
			#end
			#foreach($!accDetail in $!accDetList)
				#if($counts==1)
				<tr class="backcolor">
					<td>
						#if("$!conMap.dateType"=="1")
							$!initMap.periodBegin
						#elseif("$!conMap.dateType"=="2")
							$!conMap.dateStart
						#end
					</td>
					#if("$!conMap.showDate"=="1")
						<td></td>
					#end
					<td></td>
					<td>期初余额</td>
					#if("$!conMap.showInAccTypeInfo"=="1")
					<td class="tdDes3"></td>
					#end
					#if("$!conMap.showMessage"=="1")
						<td class="tdDes3"></td><td class="tdDes3"></td>
					#end
					#if("$flag"=="curr" || "$flag" == "allCurr")
						<td class="tdDes3"></td>
						#if("$flag" == "allCurr")
							<td class="tdDes3"></td>
							<td class="tdDes3"></td>
						#end
					#end
					#foreach ($element in $listInit)
							#set($setclass="tdDes1")
							#if("$!element"=="isflag")
								#set($setclass="tdDes3")
								#set($element = "isIniflag")
							#end
							#set($valueData = $!initMap.get($element))
							#if("$flag" == "all")
								#foreach($currency in $!currencyList)
									#if("$!element"!="isIniflag")
										#set($curId = $!globals.get($!currency,0))
										#if($!globals.get($!currency,2)=="true")
											#set($curId = "")
										#end
										#set($curName = $!globals.replaceString(${element},'Base','')+"_"+${curId})
										#set($curData = $!initMap.get($curName))
										<td class="$!setclass">$!curData</td>
										#set($curData = "")
										#set($curName = "")
										#set($curId = "")
									#end
								#end
							#end							
							<td class="$!setclass">#if('$!valueData'!='') $NumericTool.format($!valueData,'#,##0.00') #end</td>
							#set($valueData="")
						#end
				</tr>
				#end
				#foreach($!accmain in $!accDetail.detail)
					<tr onclick="setBacks(this)">
						<td>$!accmain.BillDate</td>
						#if("$!conMap.showDate"=="1")
						<td>$!accmain.BillDate</td>
						#end
						<td><a href="javascript:detailVoucher('$!accmain.accmainid')">$!accmain.CredTypeOrderNo</a></td>
						<td class="drapTable" title="$!accmain.RecordComment">$globals.subTitle($!accmain.RecordComment,70)</td>
						#if("$!conMap.showInAccTypeInfo"=="1")
						<td class="drapTable" title="$!accmain.detailacccode $!accmain.accname">$!accmain.detailacccode $!accmain.accname </td>
						#end
						#if("$!conMap.showMessage"=="1")
							<td>$!accmain.RefModuleName</td>
							<td>$!accmain.RefBillTypeName</td>
						#end
						#if("$flag"=="curr" || "$!flag"=="allCurr")
							#if("$!flag"=="allCurr")
							<td class="tdDes1">$!accmain.currencyName</td>
							#end
							<td class="tdDes1">$!globals.newFormatNumber($!accmain.CurrencyRate,false,false,$!globals.getSysIntswitch(),'tblAccDetail','CurrencyRate',true)</td>
							#if("$!flag"=="allCurr")
								#set($debitsMoney = "")
								#if("$!accmain.Currency" == "")
									#set($debitsMoney = $!accmain.sumDebitAmountBase + $!accmain.sumLendAmountBase)
								#else
									#set($debitsMoney = $!accmain.sumDebitAmount + $!accmain.sumLendAmount)
								#end
								<td class="tdDes1">#if('$!debitsMoney'!='') $NumericTool.format($!debitsMoney,'#,##0.00') #end</td>
							#end
						#end
						#foreach ($element in $list)
							#set($valueData = $!accmain.get($element))
							#set($setclass="tdDes1")
							#if("$!element"=="isflag")
								#set($setclass="tdDes3")
							#end
							#if("$flag" == "all")
								#foreach($currency in $!currencyList)
									#if("$!element"!="isflag")
										#set($curId = $!globals.get($!currency,0))
										#if($!globals.get($!currency,2)=="true")
											#set($curId = "")
										#end
										#set($curName = $!globals.replaceString(${element},'Base','')+"_"+${curId})
										#set($curData = $!accmain.get($curName))
										<td class="$!setclass">$!curData</td>
										#set($curData = "")
										#set($curName = "")
										#set($curId = "")
									#end
								#end
							#end
							<td class="$!setclass">#if('$!valueData'!='') $NumericTool.format($!valueData,'#,##0.00') #end</td>
							#set($valueData="")
						#end
					</tr>
				#end
				#if((("$!accDetail.sumDebitAmountBase"!="" || "$!accDetail.sumLendAmountBase"!="") && "$!conMap.showTakePeriod"=="") || "$!conMap.showTakePeriod"!="")
				<tr class="backcolor">
					<td>
						#if("$!conMap.dateType"=="1")
							$!accDetail.periodEnd
						#elseif("$!conMap.dateType"=="2")
							#if($counts==$accDetList.size())
								$!conMap.dateEnd
							#else
								$!accDetail.periodEnd
							#end
						#elseif("$!conMap.dateType"=="3")
							$!accDetail.periodEnd
						#end
					</td>
					<td></td>
					#if("$!conMap.showDate"=="1")
						<td></td>
					#end
					<td>本期合计</td>
					#if("$!conMap.showInAccTypeInfo"=="1")
					<td></td>
					#end
					#if("$!conMap.showMessage"=="1")
						<td></td>
						<td></td>
					#end
					<!-- <td class="tdDes3">$!accDetail.isflag</td> -->
					#if("$flag"=="curr" || "$flag" == "allCurr")
						<td class="tdDes1"></td>
						#if("$flag" == "allCurr")
							<td class="tdDes1"></td>
							<td class="tdDes1"></td>
						#end
					#end
					#foreach ($element in $list)
						#set($valueData = $!accDetail.get($element))
						#set($setclass="tdDes1")
						#if("$!element"=="isflag")
							#set($setclass="tdDes3")
						#end
						#if("$flag" == "all")
							#foreach($currency in $!currencyList)
								#if("$!element"!="isflag")
									#set($curId = $!globals.get($!currency,0))
									#if($!globals.get($!currency,2)=="true")
										#set($curId = "")
									#end
									#set($curName = $!globals.replaceString(${element},'Base','')+"_"+${curId})
									#set($curData = $!accDetail.get($curName))
									<td class="$!setclass">$!curData</td>
									#set($curData = "")
									#set($curName = "")
									#set($curId = "")
								#end
							#end
						#end
						<td class="$!setclass">#if('$!valueData'!='') $NumericTool.format($!valueData,'#,##0.00') #end</td>
						#set($valueData="")
					#end
				</tr>
				#end
				<tr class="backcolor1">
					<td>
						#if("$!conMap.dateType"=="1")
							$!accDetail.periodEnd
						#elseif("$!conMap.dateType"=="2")
							#if($counts==$accDetList.size())
								$!conMap.dateEnd
							#else
								$!accDetail.periodEnd
							#end
						#elseif("$!conMap.dateType"=="3")
							$!accDetail.periodEnd
						#end
					</td>
					<td></td>
					#if("$!conMap.showDate"=="1")
						<td></td>
					#end
					<td>本年累计</td>
					#if("$!conMap.showInAccTypeInfo"=="1")
					<td class="tdDes3"></td>
					#end
					#if("$!conMap.showMessage"=="1")
						<td></td>
						<td></td>
					#end
					#if("$flag"=="curr" || "$flag" == "allCurr")
						<td class="tdDes3"></td>
						#if("$!flag"=="allCurr")
							<td class="tdDes3"></td>
							<td class="tdDes3"></td>
						#end
					#end
					#foreach ($element in $list)
						#if("$element"!="isflag")
							#set($element = "year_$element")
						#else
							#set($element = "isYearflag")
						#end
						#set($valueData = $!accDetail.get($element))
						#set($setclass="tdDes1")
						#if("$!element"=="isYearflag")
							#set($setclass="tdDes3")
						#end
						#if("$flag" == "all")
							#foreach($currency in $!currencyList)
								#if("$!element"!="isYearflag")
									#set($curId = $!globals.get($!currency,0))
									#if($!globals.get($!currency,2)=="true")
										#set($curId = "")
									#end
									#set($curName = $!globals.replaceString(${element},'Base','')+"_"+${curId})
									#set($curData = $!accDetail.get($curName))
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
				</tr>
				#set($accnumber = $!accDetail.AccNumber)
				#set($counts=$counts+1)
			#end
		</tbody>
	</table>
	#if("$!comeMode"=="")
		<div class="querydiv1">
		双击左边科目，进行数据查询



		</div>
	#elseif($accDetList.size()==0)
		<div class="querydiv1">
			<p style="width:150px;padding:50px 0 0 0;overflow:hidden;line-height:21px;margin:0 auto;">
			很抱歉，没有找到与您条件匹配的明细分类账信息!
			</p>
		</div>
	#end
	</div>
</div>
</form>
</body>
</html>