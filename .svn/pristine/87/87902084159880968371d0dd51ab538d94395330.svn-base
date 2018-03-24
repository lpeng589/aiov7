<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>核算项目分类总账详情</title>
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
		//$(".data_list_head tbody").find("td").addClass("td_height");
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
<form method="post" scope="request" id="form" name="form" action="/FinanceReportAction.do?optype=ReporttblAccCalculate&type=detail" target="">
<div>
<div id="data_list_id" class="rt_table3">
	<script type="text/javascript"> 
		var oDiv=document.getElementById("data_list_id");
		var sHeight=document.documentElement.clientHeight-20;
		oDiv.style.height=sHeight+"px";
	</script>
	#set($curCount = 0)
	#foreach($currency in $!currencyList)
		#set($curCount = $curCount + 1)
	#end
	#set($row = 1)
	#set($col = 1)
	#set($col1 = 1)
	#set($col2 = 1)
	#set($flag = "")
	#if("$!currType"=="" || "$!currType"=="isBase")
		#set($row = 2)
		#set($col = 2)
		#set($col1 = 2)
	#else
		#set($row = 2)
		#set($col = 3)
		#set($col1 = 3)
		#set($col2 = 2)
		#set($flag = "false")
	#end
	<table class="data_list_head tb_wp" cellpadding="0" cellspacing="0" border="1" borderColor="#808080" style="overflow:auto;border-collapse:collapse;">
		<thead>
			<tr id="trTitle">
				<td class="tdDes3" rowspan="$row">期间</td>
				<td class="tdDes3" rowspan="$row">科目代码</td>
				<td class="tdDes3" rowspan="$row">科目名称</td>
				<td class="tdDes3" colspan="$col">期初余额</td>
				<td class="tdDes3" #if("$flag" != "false")rowspan="$row"#else colspan="$col2" #end>本期借方</td>
				<td class="tdDes3" #if("$flag" != "false")rowspan="$row"#else colspan="$col2" #end>本期贷方</td>
				<td class="tdDes3" #if("$flag" != "false")rowspan="$row"#else colspan="$col2" #end>本年借方</td>
				<td class="tdDes3" #if("$flag" != "false")rowspan="$row"#else colspan="$col2" #end>本年贷方</td>
				<td class="tdDes3" colspan="$col">期末余额</td>
			</tr>
			<tr>
				#if("$flag" == "")
					#foreach($foo in [1..2])
						<td class="tdDes3">方向</td>
						<td class="tdDes3">余额</td>
					#end
				#elseif("$flag" == "false")
					#foreach($foo in [1..6])
						#if($foo == 1 || $foo == 6)
						<td class="tdDes3">方向</td>
						#end
						<td class="tdDes3">原币</td>
						<td class="tdDes3">本位币</td>
					#end
				#end
			</tr>
		</thead>
		<tbody>
			#foreach($accdetail in $accDetList)
				#set($count = 0)
				#set($detvalues = "")
				#set($detailhash = "")
				#set($foo = 0)
				#set($PeriodBalaBase = "")
				#set($PeriodBala = "")
				#foreach($accperiod in $accdetail.period)
					#set($detvalues = "")
					#set($detailhash = "$!{accdetail.classCode}_$!{accperiod.AccYear}_$!{accperiod.AccPeriod}")
					#set($detvalues = $accDetHash.get($detailhash))
					#if("$!conMap.takeBrowNo"=="1")
						#set($foo = $count)
					#else
						#set($foo = $accdetail.period.size())
					#end
					#if("$flag" == "")
						#set($list=["initAmount","sumDebitAmount","sumLendAmount","sumYearDebitAmount","sumYearLendAmount","isflag","PeriodBalaBase"])
					#elseif("$flag"=="false")
						#set($list=["initCurrencyAmount","initAmount","sumDebitCurrencyAmount","sumDebitAmount","sumLendCurrencyAmount","sumLendAmount","sumYearDebitCurrencyAmount","sumYearDebitAmount","sumYearLendCurrencyAmount","sumYearLendAmount","isflag","PeriodBala","PeriodBalaBase"])
					#end
					#if("$!conMap.takeBrowNo"=="1" && "$!detvalues.sumDebitAmount"=="" && "$!detvalues.sumLendAmount"=="")
					#else
						<tr onclick="setBacks(this)">
							<td #if($count==0) class="hideBottom"#else class="hideBorder"#end>$!{accperiod.AccYear}.$!{accperiod.AccPeriod}</td>
							#if($count==0)<td class="hideBottom">$!accdetail.AccNumber</td>#else<td class="hideBorder"></td>#end
							#if($count==0)<td class="hideBottom">$!accdetail.AccFullName</td>#else<td class="hideBorder"></td>#end
							<td #if($count==0)class="hideBottom"#else class="hideBorder"#end style="text-align: center;">$!detvalues.isInitflag</td>
							#foreach ($element in $list)
								#set($valueData = $!detvalues.get($element))
								<td #if($count==0)class="hideBottom tdDes1"#else class="hideBorder tdDes1"#end>#if('$!valueData'!='') $NumericTool.format($!valueData,'#,##0.00') #end</td>
								#set($valueData="")
							#end
							#set($PeriodBala = $!detvalues.PeriodBala)
							#set($PeriodBalaBase = $!detvalues.PeriodBalaBase)
						</tr>
						#set($count = $count+1)
					#end
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