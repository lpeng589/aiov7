<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>核算项目明细账详情</title>
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
table.tb_wp{}
table.tb_wp td{padding:0 5px 0 5px;word-break: keep-all;white-space:nowrap;}
table.tb_wp tbody td{padding:5px;}
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
	
	function detailVoucher(id){
		var title="会计凭证";
		var url = "/VoucherManageAction.do?operation=5&tableName=tblAccMain&id="+id+"&isEspecial=1";
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
	
	/* 导出 */
	function exports(){
		var url = window.location.href;
		url += "&dealtype=exportList";
		window.location.href = url;
		blockUI();
	}
	
	/* 打印 */
	function prints(){
		var dataHeight = $("#data_list_id").css("height");
		$("#data_list_id").css("height","100%");
		window.print();//打印
		$("#data_list_id").css("height",dataHeight);
	}
	
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form method="post" scope="request" id="form" name="form" action="/FinanceReportAction.do?optype=ReporttblAccCalculateDet&type=detail" target="">
<div>
<div id="data_list_id" class="rt_table3">
	<script type="text/javascript"> 
		var oDiv=document.getElementById("data_list_id");
		var sHeight=document.documentElement.clientHeight-20;
		oDiv.style.height=sHeight+"px";
	</script>
	<table class="data_list_head tb_wp" cellpadding="0" cellspacing="0" border="1" borderColor="#808080" style="overflow:auto;border-collapse:collapse;">
		<thead>
			<tr id="trTitle">
				<td class="tdDes3" #if("$!isBase"=="false" || "$!isBase"=="all")rowspan="2"#end>日期</td>
				<td class="tdDes3" #if("$!isBase"=="false" || "$!isBase"=="all")rowspan="2"#end>期间</td>
				<td class="tdDes3" #if("$!isBase"=="false" || "$!isBase"=="all")rowspan="2"#end>凭证字号</td>
				<td class="tdDes3" #if("$!isBase"=="false" || "$!isBase"=="all")rowspan="2"#end>摘要</td>
				#if("$!isBase"=="false" || "$!isBase"=="all")
					<td class="tdDes3" rowspan="2">汇率</td>
				#end
				#set($currencyCount = $currencyList.size())
				#set($currencyCount = $currencyCount+1)
				<td class="tdDes3" #if("$!isBase"=="false")colspan="2"#elseif("$!isBase"=="all")colspan="$!currencyCount"#end>借方</td>
				<td class="tdDes3" #if("$!isBase"=="false")colspan="2"#elseif("$!isBase"=="all")colspan="$!currencyCount"#end>贷方</td>
				#set($currencyCount = $currencyCount+1)
				<td class="tdDes3" #if("$!isBase"=="false")colspan="3"#elseif("$!isBase"=="all")colspan="$currencyCount"#else colspan="2"#end>余额</td>
			</tr>
			<tr>
				#if("$!isBase"=="false" || "$!isBase"=="all")
				#foreach($foo in [1..3])
					#if($foo==3)
						<td class="tdDes3" width="20px;"></td>
					#end
					#if("$!isBase"=="false")
						<td class="tdDes3">原币</td>
						<td class="tdDes3">本位币</td>
					#elseif("$!isBase"=="all")
						#foreach($currency in $currencyList)
							<td>$!globals.get($!currency,1)</td>
						#end
						<td>本位币</td>
					#end
				#end
				#end
			</tr>
		</thead>
		<tbody>
			#set($currencyCount = ($currencyList.size()+1)*2)
			#if("$!comeMode"!="")
				<tr id="trTitle" onclick="setBacks(this)">
					<td class="tdDes3"></td>
					<td class="tdDes3">$!{detailData.initMap.accYear}.$!{detailData.initMap.accPeriod}</td>
					<td class="tdDes3"></td>
					<td class="tdDes2">期初余额</td>
					#if("$!isBase"=="false" || "$!isBase"=="all")
						<td class="tdDes3"></td>
					#end
					#if("$!isBase"=="false")
						#foreach($foo in [1..4])
							<td class="tdDes3"></td>
						#end
					#elseif("$!isBase"=="all")
						#foreach($foo in [1..$currencyCount])
							<td class="tdDes3"></td>
						#end
					#else
						
							<td class="tdDes3">$!detailData.initMap.sumDebit</td>
							<td class="tdDes3">$!detailData.initMap.sumLend</td>
					#end
					<td class="tdDes3">$!detailData.initMap.isInitflag</td>
					#if("$!isBase"=="false")
						<td class="tdDes1">$!detailData.initMap.sumCurrencyAmount</td>
					#elseif("$!isBase"=="all")
						#foreach($currency in $currencyList)
							<td></td>
						#end
					#end
					<td class="tdDes1">						
						#if('$!detailData.initMap.sumAmount'!='') $NumericTool.format($!detailData.initMap.sumAmount,'#,##0.00') #end
					</td>
				</tr>
			#end
			#foreach($period in $periodList)
				#set($periodname = $period.AccYear+"_"+$period.AccPeriod)
				#set($values = $detailData.get($periodname))
				#foreach($accMain in $values.get("accMainList"))
					<tr id="trTitle"  onclick="setBacks(this)">
						<td class="tdDes3">$accMain.AccDate</td>
						<td class="tdDes3">${accMain.CredYear}.${accMain.Period}</td>
						<td class="tdDes3"><a href="javascript:detailVoucher('$!accMain.id')">$accMain.credTypeIdOrderNo</a></td>
						<td class="tdDes2">$accMain.RecordComment</td>
						#if("$!isBase"=="false" || "$!isBase"=="all")
							<td>
								#if('$!accMain.CurrencyRate'!='') $NumericTool.format($!accMain.CurrencyRate,'#,##0.00') #end								
							</td>
						#end
						#if("$!isBase"=="false")
							<td class="tdDes1">								
								#if('$!accMain.DebitCurrencyAmount'!='') $NumericTool.format($!accMain.DebitCurrencyAmount,'#,##0.00') #end
							</td>
						#elseif("$!isBase"=="all")
							#foreach($currency in $currencyList)
								<td class="tdDes1">#if("$!globals.get($!currency,0)"=="$accMain.Currency")
										#if("$!globals.get($!currency,2)"=="true")
											#if('$!accMain.DebitAmount'!='') $NumericTool.format($!accMain.DebitAmount,'#,##0.00') #end											
										#else
											#if('$!accMain.DebitCurrencyAmount'!='') $NumericTool.format($!accMain.DebitCurrencyAmount,'#,##0.00') #end											
										#end
									#end
								</td>
							#end
						#end
						<td class="tdDes1">
							#if('$!accMain.DebitAmount'!='') $NumericTool.format($!accMain.DebitAmount,'#,##0.00') #end							
						</td>
						#if("$!isBase"=="false")
							<td class="tdDes1">
								#if('$!accMain.LendCurrencyAmount'!='') $NumericTool.format($!accMain.LendCurrencyAmount,'#,##0.00') #end								
							</td>
						#elseif("$!isBase"=="all")
							#foreach($currency in $currencyList)
								<td class="tdDes1">#if("$!globals.get($!currency,0)"=="$accMain.Currency")
										#if("$!globals.get($!currency,2)"=="true")											
											$NumericTool.format($!accMain.LendAmount,'#,##0.00')
										#else											
											$NumericTool.format($!accMain.LendCurrencyAmount,'#,##0.00')
										#end
									#end
								</td>
							#end
						#end
						<td class="tdDes1">
							#if('$!accMain.LendAmount'!='') $NumericTool.format($!accMain.LendAmount,'#,##0.00') #end							
						</td>
						<td class="tdDes3">$!accMain.isflag</td>
						#if("$!isBase"=="false")
							<td class="tdDes1">
								#if('$!accMain.sumCurrencyAmount'!='') $NumericTool.format($!accMain.sumCurrencyAmount,'#,##0.00') #end								
							</td>
						#elseif("$!isBase"=="all")
							#foreach($currency in $currencyList)
								<td class="tdDes1">#if("$!globals.get($!currency,0)"=="$accMain.Currency")
										#if("$!globals.get($!currency,2)"=="true")
											$NumericTool.format($!accMain.sumAmount,'#,##0.00')											
										#else
											$NumericTool.format($!accMain.sumCurrencyAmount,'#,##0.00')											
										#end
									#end
								</td>
							#end
						#end
						<td class="tdDes1">
							#if('$!accMain.sumAmount'!='') $NumericTool.format($!accMain.sumAmount,'#,##0.00') #end							
						</td>
					</tr>
				#end
				#if("$!conMap.takeBrowNo"=="1" && $detail.accMainList.size()==0)
				#else
				<tr id="trTitle" class="backcolor">
					<td class="tdDes3"></td>
					<td class="tdDes3">${period.AccYear}.${period.AccPeriod}</td>
					<td class="tdDes3"></td>
					<td class="tdDes2">本期合计</td>
					#if("$!isBase"=="false" || "$!isBase"=="all")
						<td class="tdDes3"></td>
					#end
					#if("$!isBase"=="false")
						<td class="tdDes1">							
							#if('$!values.sumDebitCurrencyAmount'!='') $NumericTool.format($!values.sumDebitCurrencyAmount,'#,##0.00') #end
						</td>
					#elseif("$!isBase"=="all")
						#set($currid="")
						#set($currs="")
						#foreach($currency in $currencyList)
							<td class="tdDes1">#if("$!globals.get($!currency,2)"=="true")
									$NumericTool.format($!currs.sumDebitAmount,'#,##0.00')									
								#else
									#set($currid = "curr_$globals.get($!currency,0)_sumDebitCurrencyAmount")
									#set($currs = $values.get($currid))									
									$NumericTool.format($currs,'#,##0.00')	
								#end
							</td>
						#end
					#end
					<td class="tdDes1">
						#if('$!values.sumDebitAmount'!='') $NumericTool.format($!values.sumDebitAmount,'#,##0.00') #end						
					</td>
					#if("$!isBase"=="false")
						<td class="tdDes1">							
							#if('$!values.sumLendCurrencyAmount'!='') $NumericTool.format($!values.sumLendCurrencyAmount,'#,##0.00') #end
						</td>
					#elseif("$!isBase"=="all")
						#set($currid="")
						#set($currs="")
						#foreach($currency in $currencyList)
							#set($currid = "curr_$globals.get($!currency,0)")
							#set($currs = $detail.get("period").get($currid))
							<td class="tdDes1">#if("$!globals.get($!currency,2)"=="true")									
									$NumericTool.format($!currs.sumLendAmount,'#,##0.00')
								#else
									$NumericTool.format($!currs.sumLendCurrencyAmount,'#,##0.00')									
								#end
							</td>
						#end
					#end
					<td class="tdDes1">
						#if('$!values.sumLendAmount'!='') $NumericTool.format($!values.sumLendAmount,'#,##0.00') #end						
					</td>
					<td class="tdDes3">$!values.isflag</td>
					#if("$!isBase"=="false")
						<td class="tdDes1">							
							#if('$!values.sumDCBalaCurrency'!='') $NumericTool.format($!values.sumDCBalaCurrency,'#,##0.00') #end	
						</td>
					#elseif("$!isBase"=="all")
						#set($currid="")
						#set($currs="")
						#foreach($currency in $currencyList)
							#set($currid = "curr_$globals.get($!currency,0)")
							#set($currs = $detail.get("period").get($currid))
							<td class="tdDes1">#if("$!globals.get($!currency,2)"=="true")									
									#if('$!currs.sumDCBala'!='') $NumericTool.format($!currs.sumDCBala,'#,##0.00') #end
								#else									
									#if('$!currs.sumDCBalaCurrency'!='') $NumericTool.format($!currs.sumDCBalaCurrency,'#,##0.00') #end
								#end
							</td>
						#end
					#end
					<td class="tdDes1">						
						#if('$!values.sumDCBala'!='') $NumericTool.format($!values.sumDCBala,'#,##0.00') #end
					</td>
				</tr>
				<tr id="trTitle" class="backcolor1">
					<td class="tdDes3"></td>
					<td class="tdDes3">${period.AccYear}.${period.AccPeriod}</td>
					<td class="tdDes3"></td>
					<td class="tdDes2">本年累计</td>
					#if("$!isBase"=="false" || "$!isBase"=="all")
						<td class="tdDes3"></td>
					#end
					#if("$!isBase"=="false")
						<td class="tdDes1">							
							#if('$!values.yearsumDebitCurrencyAmount'!='') $NumericTool.format($!values.yearsumDebitCurrencyAmount,'#,##0.00') #end
						</td>
					#elseif("$!isBase"=="all")
						#set($currid="")
						#set($currs="")
						#foreach($currency in $currencyList)
							#set($currid = "curr_$globals.get($!currency,0)")
							#set($currs = $detail.get("yearPeriod").get($currid))
							<td class="tdDes1">#if("$!globals.get($!currency,2)"=="true")									
									$NumericTool.format($!currs.sumDebitAmount,'#,##0.00')
								#else									
									$NumericTool.format($!currs.sumDebitCurrencyAmount,'#,##0.00')
								#end
							</td>
						#end
					#end
					<td class="tdDes1">
						#if('$!values.yearsumDebitAmount'!='') $NumericTool.format($!values.yearsumDebitAmount,'#,##0.00') #end						
					</td>
					#if("$!isBase"=="false")
						<td class="tdDes1">							
							#if('$!values.yearsumLendCurrencyAmount'!='') $NumericTool.format($!values.yearsumLendCurrencyAmount,'#,##0.00') #end
						</td>
					#elseif("$!isBase"=="all")
						#set($currid="")
						#set($currs="")
						#foreach($currency in $currencyList)
							#set($currid = "curr_$globals.get($!currency,0)")
							#set($currs = $detail.get("yearPeriod").get($currid))
							<td class="tdDes1">#if("$!globals.get($!currency,2)"=="true")									
									$NumericTool.format($!currs.sumLendAmount,'#,##0.00')
								#else
									$NumericTool.format($!currs.sumLendCurrencyAmount,'#,##0.00')									
								#end
							</td>
						#end
					#end
					<td class="tdDes1">						
						#if('$!values.yearsumLendAmount'!='') $NumericTool.format($!values.yearsumLendAmount,'#,##0.00') #end
					</td>
					<td class="tdDes3">$!values.yearisflag</td>
					#if("$!isBase"=="false")
						<td class="tdDes1">							
							#if('$!values.yearsumDCBalaCurrency'!='') $NumericTool.format($!values.yearsumDCBalaCurrency,'#,##0.00') #end
						</td>
					#elseif("$!isBase"=="all")
						#set($currid="")
						#set($currs="")
						#foreach($currency in $currencyList)
							#set($currid = "curr_$globals.get($!currency,0)")
							#set($currs = $detail.get("yearPeriod").get($currid))
							<td class="tdDes1">#if("$!globals.get($!currency,2)"=="true")									
									$NumericTool.format($!currs.sumDCBala,'#,##0.00')
								#else									
									$NumericTool.format($!currs.sumDCBalaCurrency,'#,##0.00')
								#end
							</td>
						#end
					#end
					<td class="tdDes1">						
						#if('$!values.yearsumDCBala'!='') $NumericTool.format($!values.yearsumDCBala,'#,##0.00') #end
					</td>
				</tr>
				#end
			#end
		</tbody>
	</table>
	#if("$!comeMode"=="")
		<div class="querydiv1">
		双击左边核算项目，进行数据查询



		</div>
	#elseif($accDetList.size()==0)
		<div class="querydiv1">
			<p style="width:150px;padding:50px 0 0 0;overflow:hidden;line-height:21px;margin:0 auto;">
			很抱歉，没有找到与您条件匹配的核算项目分类总账信息!
			</p>
		</div>
	#end
	</div>
</div>
</form>
</body>
</html>