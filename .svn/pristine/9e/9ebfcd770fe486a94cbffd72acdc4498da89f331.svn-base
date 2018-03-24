<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>多栏式明细账</title>
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
	.listRange_pagebar{width: auto;};
	.bordertr td{border: 1px;}
	.hideBorder{border-bottom: 0px;border-top: 0px;}
	.hideBottom{border-bottom: 0px;}
	.tr_back{background: #E7FCA9;}
	.td_left1{padding-left: 50px;}
	.td_left2{padding-left: 20px;}
	.data_list_head tr td{padding:5px 10px;word-break: keep-all;white-space:nowrap;}
	.backcolor{background-color:lightyellow;}
	.backcolor1{background-color:#EEF1E0;}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		#if("$!comeMode"=="")
			showSearchDiv();
			$("#periodYearStart").val("$!accPeriod.accYear");
			$("#periodYearEnd").val("$!accPeriod.accYear");
			$("#periodStart").val("$!accPeriod.accPeriod");
			$("#periodEnd").val("$!accPeriod.accPeriod");
			$("#showFormCurrency").attr("checked","checked");
			$("#init_tr").remove();
		#end
		ajaxData();
		/* 根据数字金额转换为大写金额 */
		$(".data_list_head tbody").find(".tdDes1").mouseover(function(){
			var objhtml = $(this).html();
			if(objhtml!=null && objhtml!="" && objhtml!="&nbsp;"){
				$(this).attr("title",AmountInWords(objhtml,null));
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
		var accname = jQuery("#accName").val();
		if(accname==null){
			alert("请先设计多栏式明细账定义");
			return false;
		}
		var periodYearStart = $("#periodYearStart").val();
		var periodStart = $("#periodStart").val();
		var periodYearEnd = $("#periodYearEnd").val();
		var periodEnd = $("#periodEnd").val();
		if(periodStart<10){
			periodStart = '0'+periodStart;
		}
		if(periodEnd<10){
			periodEnd = '0'+periodEnd;
		}
		if(periodYearStart+"-"+periodStart>periodYearEnd+"-"+periodEnd){
			alert("结束期间不能小于开始期间");
			$("#periodYearStart").focus();
			$('#highSearch').show();
			return false;
		}
		blockUI();
		form.submit();
	}
	
	var accNames = "";
	function ajaxData(){
		jQuery.ajax({type:"post",url: "/FinanceReportAction.do?optype=ReporttblAccAllDet&type=queryDesign",async:false, success: function (result){
			$("#accName").html(result);
		}});
		if(accNames!=""){
			$("#accName").val(accNames);
		}else{
			$("#accName").val("$!conMap.accName");
		}
	}
	
	//设计
	function dealdesign(){
		var urlstr = "/FinanceReportAction.do?optype=ReporttblAccAllDet&type=designList";
		asyncbox.open({id:'popList',title:"多栏式明细账定义",url:urlstr,width:620,height:350});
	}
	
	function dealAsyn(){
		jQuery.close('designdiv');
		if(jQuery.exist('popList')){
			jQuery.opener('popList').refurbish();
		}
		ajaxData();
	}
	
	function detailVoucher(id){
		var title="会计凭证";
		var url = "/VoucherManageAction.do?operation=5&tableName=tblAccMain&id="+id+"&isEspecial=1";
		if(typeof(window.parent.frames["f_mainFrame"])=="undefined"){
			window.open(url);
		}else{
			top.mdiwin(url,title);
		}
	}
	
	/* 核算项目弹出框 */
	var fieldName = "";
	var itemSorts = "";
	function selectItemCode(value){
		var name = "";
		var popname = "";
		fieldName = value;
		var itemSort = $("#itemSort").val();
		if(itemSort=="DepartmentCode"){
			popname = "SelectAccDepartment";
			name = "请选择部门";
		}else if(itemSort=="EmployeeID"){
			popname = "SelectAccEmployee";
			name = "请选择职员";
		}else if(itemSort=="StockCode"){
			popname = "SelectAccStocks";
			name = "请选择仓库";
		}else if(itemSort=="ClientCode"){
			popname = "SelectAccClient";
			name = "请选择客户";
		}else if(itemSort=="SuplierCode"){
			popname = "SelectAccProvider";
			name = "请选择供应商";
		}else if(itemSort=="ProjectCode"){
			popname = "SelectAccProject";
			name = "请选择项目";
		}
		itemSorts = itemSort;
		var displayName=encodeURI(name) ;
		var urlstr = '/UserFunctionAction.do?operation=22&popupWin=Popdiv&tableName=&selectName='+popname+"&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName;
		asyncbox.open({id:'Popdiv',title:name,url:urlstr,width:750,height:470})
	}
	
	/* 返回数据 */
	function exePopdiv(returnValue){
		if(typeof(returnValue)=="undefined") return ;
		var note = returnValue.split("#;#") ;
		if(itemSorts == "EmployeeID"){
			$("#"+fieldName).val(note[3]);
		}else{
			$("#"+fieldName).val(note[1]);
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
	
	function dealItem(){
		$("#acctypeCodeStart").val('');
		$("#acctypeCodeEnd").val('');
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
		$("#itemSort").val("DepartmentCode");
		$("#acctypeCodeStart").val('');
		$("#acctypeCodeEnd").val('');
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
</script>
<style type="text/css">

</style>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form method="post" scope="request" id="form" name="form" action="/FinanceReportAction.do?optype=ReporttblAccAllDet" target="">
<input type="hidden" name="comeMode" id="comeMode" value="false" />
<input type="hidden" id="org.apache.struts.taglib.html.TOKEN" name="org.apache.struts.taglib.html.TOKEN" value=""/>
<input type="hidden" id="SQLSave" name="SQLSave" value="@SQL:$saveSql@ParamList:@end:"/>
<input type="hidden" id="type" name="type" value=""/>
	<div class="titlediv">
		<img src="/style1/images/workflow/ti_003.gif"/><span style="font-size: 14px;">多栏式明细账</span>
	</div>
	<div style="float: left;padding-bottom: 2px;margin: 20px 10px 10px 10px;">
		#if("$!comeMode"!="")
		期间：$!conMap.periodYearStart年第$!conMap.periodStart期至#if("$!conMap.periodYearStart"!="$!conMap.periodYearEnd")$!conMap.periodYearEnd年#end第$!conMap.periodEnd期

		&nbsp;&nbsp;&nbsp;
		多栏式名称：$!globals.get($!designBean,2)
		<!-- &nbsp;&nbsp;&nbsp;
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
		#end -->
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
		<div class="op sbtn41" onclick="prints()">
			<span class="a"></span>
			<a href="javascript:void(0)">打印</a>
			<span class="c"></span>
		</div>
		#end
		<!-- 
		<div class="op sbtn41" onclick="exports()">
			<span class="a"></span>
			<a href="javascript:void(0)">导出</a>
			<span class="c"></span>
		</div>  -->
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
			<table class="data_list_head" cellpadding="0" cellspacing="0" border="1" borderColor="#808080"  style="table-layout:fixed;overflow:auto;border-collapse:collapse;">
			<thead>
				#set($showCurrency = "$conMap.showFormCurrency")
				#set($size1 = 0)
				#set($size2 = 0)
				#foreach($!items in $!globals.strSplitByFlag($!globals.get($!designBean,3),'//'))
					#set($itemsvalue = $!globals.strSplitByFlag($!items,';'))
          			#if("$!globals.get($!itemsvalue,0)"=="1")
          				#set($size1 = $size1+1)
          			#elseif("$!globals.get($!itemsvalue,0)"=="2")
          				#set($size2 = $size2+1)
          			#end
				#end
				#set($size9 = 0)
				#set($size11 = 0)
				#foreach($!currency in $!currencyList)
	            	#foreach($!designcurr in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
	            		#if("$!designcurr"=="$!globals.get($!currency,0)")
	            			#if("$!globals.get($!currency,3)"=="true" || "$showCurrency"!="1")
	            				#set($size9 = $size9+1)
	            			#else
	            				#set($size9 = $size9+2)
	            				#set($size11 = $size11+1)
	            			#end
	            		#end
	            	#end
	            #end
				#set($size3 = 1)
				#set($size4 = 1)
				#set($size5 = 1)
				#set($size6 = 1)
				#set($isflag = "")
				#if("$!allDetData.currIsBase" == "isbase" || "$!allDetData.currIsBase" == "all")
					#set($size3 = 2)
					#set($size4 = 2)
					#set($size5 = 1)
					#set($isflag = "true")
				#else
					#set($size3 = 4)
					#set($size4 = 2)
					#if("$showCurrency"=="1")
						#set($size5 = 3)
					#else
						#set($size5 = 2)
					#end
					#set($size6 = $size9+1)
					#set($isflag = "false")
				#end
				#if($size9 == 0)
					#set($size7 = $size1)
					#set($size8 = $size2)
				#else
					#if("$isflag" == "false")
						#set($size7 = $size1*$size9)
						#set($size8 = $size2*$size9)
					#else
						#set($size7 = $size1)
						#set($size8 = $size2)
					#end
				#end
				<tr id="trTitle">
					<td class="tdDes3" rowspan="$size3">日期</td>
					<td class="tdDes3" rowspan="$size3">凭证字号</td>
					<td class="tdDes3" rowspan="$size3">摘要</td>
					<td class="tdDes3" rowspan="$size4" colspan="$size6">借方</td>
					<td class="tdDes3" rowspan="$size4" colspan="$size6">贷方</td>
					<td class="tdDes3" colspan="2" rowspan="$size5">余额</td>
					#if("$size1"!="0")
					<td class="tdDes3" colspan="$size7">借方</td>
					#end
					#if("$size2"!="0")
					<td class="tdDes3" colspan="$size8">贷方</td>
					#end
				</tr>
				#if("$isflag" == "false")
					<tr>
						#if("$!globals.get($!designBean,0)"!="")
				          	#set($itemsvalue = "")
							#foreach($!items in $!globals.strSplitByFlag($!globals.get($!designBean,3),'//'))
		          				#set($itemsvalue = $!globals.strSplitByFlag($!items,';'))
		          				#if("$!globals.get($!itemsvalue,0)"=="1")
		          					<td class="tdDes3" colspan="$size9">$!globals.get($!itemsvalue,2)</td>
		          				#end
					    	#end
					    	#foreach($!items in $!globals.strSplitByFlag($!globals.get($!designBean,3),'//'))
		          				#set($itemsvalue = $!globals.strSplitByFlag($!items,';'))
		          				#if("$!globals.get($!itemsvalue,0)"=="2")
		          					<td class="tdDes3" colspan="$size9">$!globals.get($!itemsvalue,2)</td>
		          				#end
					    	#end
				    	#end
					</tr>
					<tr>
						#set($counts = $size1+$size2+2)
						#foreach( $foo in [1..$counts])
				          	#foreach($!currency in $!currencyList)
				            	#foreach($!designcurr in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
				            		#if("$!designcurr"=="$!globals.get($!currency,0)")
				            			#if("$!globals.get($!currency,3)"=="true")
				            				<td class="tdDes3" #if("$showCurrency"=="1")rowspan="2"#end>$!globals.get($!currency,1)</td>
				            			#else
					            			<td class="tdDes3" #if("$showCurrency"=="1")colspan="2"#end>$!globals.get($!currency,1)</td>
				            			#end
				            		#end
				            	#end
				            #end
				            #if($foo<=2)
				            	<td class="tdDes3" #if("$showCurrency"=="1")rowspan="2"#end>合计本位币</td>
				            #end
				            #if("$showCurrency"!="1" && $foo==2)
				            	<td class="tdDes3">方向</td>
								<td class="tdDes3">&nbsp;&nbsp;&nbsp;</td>
				            #end
						#end
					</tr>
					#if("$showCurrency"=="1")
					<tr>
						#foreach($foo in [1..2])
							#foreach($!currency in $!currencyList)
					            #foreach($!designcurr in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
					            	#if("$!designcurr"=="$!globals.get($!currency,0)")
					            		#if("$!globals.get($!currency,3)"!="true")
											<td class="tdDes3">原币</td>
											<td class="tdDes3">本位币</td>
					            		#end
					            	#end
								#end
							#end
						#end
						<td class="tdDes3">方向</td>
						<td class="tdDes3"></td>
						#set($c=($size1+$size2)*$size11)
						#foreach($foo in [1..$c])
							<td class="tdDes3">原币</td>
							<td class="tdDes3">本位币</td>
						#end
					</tr>
					#end
				#else
					<tr>
						<td class="tdDes3">方向</td>
						<td class="tdDes3"></td>
						#if("$!globals.get($!designBean,0)"!="")
				          	#set($itemsvalue = "")
							#foreach($!items in $!globals.strSplitByFlag($!globals.get($!designBean,3),'//'))
		          				#set($itemsvalue = $!globals.strSplitByFlag($!items,';'))
		          				#if("$!globals.get($!itemsvalue,0)"=="1")
		          					<td class="tdDes3">$!globals.get($!itemsvalue,2)</td>
		          				#end
					    	#end
					    	#foreach($!items in $!globals.strSplitByFlag($!globals.get($!designBean,3),'//'))
		          				#set($itemsvalue = $!globals.strSplitByFlag($!items,';'))
		          				#if("$!globals.get($!itemsvalue,0)"=="2")
		          					<td class="tdDes3">$!globals.get($!itemsvalue,2)</td>
		          				#end
					    	#end
				    	#end
					</tr>
				#end
			</thead>
			<tbody>
				<!-- 期初余额 -->
				<tr id="init_tr" onclick="setBacks(this)">
					#set($initData = $!allDetData.initData)
					<td class="tdDes3 hideBottom">$!allDetData.AccDate</td>
					<td class="hideBottom"></td>
					<td class="tdDes2 hideBottom">
						#if("$!conMap.periodYearStart"!="$!conMap.periodYearEnd")
							年初余额
						#else
							期初余额
						#end
					</td>
					#set($itemTotal = ["totalsumDebitAmount","totalsumLendAmount"])
					#set($itemCur = ["sumDebitCurrencyAmount","sumLendCurrencyAmount"])
					#set($item = ["sumDebitAmount","sumLendAmount"])
					#foreach($foo in $itemTotal)
						#if("$isflag" == "false")
							#set($count = $math.sub($velocityCount,1))
							#foreach($!designcurr in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
								#set($currs = "$!designcurr")
								#if("$currs"=="$!allDetData.isbaseCurr")
									#set($currs="")
								#end
								#set($itemVal1 = $itemCur.get($count))
								#set($itemVal2 = $item.get($count))
								#set($datamoney1 = "curr_$!{currs}_$itemVal1")
								#set($datamoney2 = "curr_$!{currs}_$itemVal2")
								#set($money1 = $initData.get($datamoney1))
								#set($money2 = $initData.get($datamoney2))
								#if("$showCurrency"=="1")
									<td class="tdDes1 hideBottom">$!money1</td>
								#end
								<td class="tdDes1 hideBottom">$!money2</td>
								#set($datamoney1="")
								#set($datamoney2="")
								#set($money1 = "")
								#set($money2 = "")
							#end
							
						#end
						<td class="tdDes1 hideBottom">$!initData.get($foo)</td>
					#end
					<td class="tdDes3 hideBottom">$!initData.totalBalanceisflag</td>
					<td class="tdDes1 hideBottom">$!initData.totalBalance</td>
					#set($itemTotal = ["totalsumDebitAmount"])
					#set($itemCur = ["sumDebitCurrencyAmount"])
					#set($item = ["sumDebitAmount"])
						#foreach($!items in $!globals.strSplitByFlag($!globals.get($!designBean,3),'//'))
							#set($itemsvalue = $!globals.strSplitByFlag($!items,';'))
							#if("$!globals.get($!itemsvalue,0)"=="1")
								#foreach($foo in $itemTotal)
									#if("$isflag" == "false")
										#set($count = $math.sub($velocityCount,1))
										#foreach($!designcurr in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
											#set($currs = "$!designcurr")
											#if("$currs"=="$!allDetData.isbaseCurr")
												#set($currs="")
											#end
											#set($itemVal1 = $itemCur.get($count))
											#set($itemVal2 = $item.get($count))
											#set($datamoney1 = "curr_$!{currs}_$!{globals.get($!itemsvalue,1)}_$itemVal1")
											#set($datamoney2 = "curr_$!{currs}_$!{globals.get($!itemsvalue,1)}_$itemVal2")
											#set($money1 = $initData.get($datamoney1))
											#set($money2 = $initData.get($datamoney2))
											#if("$showCurrency"=="1")
												<td class="tdDes1 hideBottom">$!money1</td>
											#end
											<td class="tdDes1 hideBottom">$!money2</td>
											#set($datamoney1="")
											#set($datamoney2="")
											#set($money1 = "")
											#set($money2 = "")
										#end
									#end
								#end
							#end
						#end
					#set($itemTotal = ["totalsumLendAmount"])
					#set($itemCur = ["sumLendCurrencyAmount"])
					#set($item = ["sumLendAmount"])
						#foreach($!items in $!globals.strSplitByFlag($!globals.get($!designBean,3),'//'))
							#set($itemsvalue = $!globals.strSplitByFlag($!items,';'))
							#if("$!globals.get($!itemsvalue,0)"=="2")
								#foreach($foo in $itemTotal)
									#if("$isflag" == "false")
										#set($count = $math.sub($velocityCount,1))
										#foreach($!designcurr in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
											#set($currs = "$!designcurr")
											#if("$currs"=="$!allDetData.isbaseCurr")
												#set($currs="")
											#end
											#set($itemVal1 = $itemCur.get($count))
											#set($itemVal2 = $item.get($count))
											#set($datamoney1 = "curr_$!{currs}_$!{globals.get($!itemsvalue,1)}_$itemVal1")
											#set($datamoney2 = "curr_$!{currs}_$!{globals.get($!itemsvalue,1)}_$itemVal2")
											#set($money1 = $initData.get($datamoney1))
											#set($money2 = $initData.get($datamoney2))
											#if("$showCurrency"=="1")
												<td class="tdDes1 hideBottom">$!money1</td>
											#end
											<td class="tdDes1 hideBottom">$!money2</td>
											#set($datamoney1="")
											#set($datamoney2="")
											#set($money1 = "")
											#set($money2 = "")
										#end
									#end
								#end
							#end
						#end
				</tr>
				#foreach($periods in $allDetData.periodList)
					<!-- 循环凭证明细取数据 -->
					#set($dates="")
					#set($curData="")
					#set($money1 = "")
					#set($money2 = "")
					#set($acctypename1="")
					#set($acctypename2="")
					#set($datamoney1="")
					#set($datamoney2="")
					#set($currs = "")
					#set($detailKey = "detail_$periods.get('AccYear')_$periods.get('AccPeriod')")
					#set($detailData = $!allDetData.get($detailKey))
					#set($itemTotal = ["totalsumDebitAmount","totalsumLendAmount"])
					#set($itemCur = ["DebitCurrencyAmount","LendCurrencyAmount"])
					#set($item = ["DebitAmount","LendAmount"])
					#foreach($detail in $detailData)
					<tr onclick="setBacks(this)">
						<td class="tdDes3 hideBottom">$detail.AccDate</td>
						<td class="tdDes3 hideBottom"><a href="javascript:detailVoucher('$!detail.id')">$detail.credTypeIdOrderNo</a></td>
						<td class="tdDes2 hideBottom">$detail.RecordComment</td>
						#if("$isflag" == "false")
							#foreach($!designcurr in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
								#set($money1 = "")
								#set($money2 = "")
								#set($currs = "$!designcurr")
								#if("$currs"=="$!allDetData.isbaseCurr")
									#set($currs="")
								#end
								#if("$!currs"=="$detail.Currency")
									#set($money1 = $detail.DebitCurrencyAmount)
									#set($money2 = $detail.DebitAmount)
								#end
								#if("$showCurrency"=="1")
									<td class="tdDes1 hideBottom">$!money1</td>
								#end
								<td class="tdDes1 hideBottom">$!money2</td>
							#end
							<td class="tdDes1 hideBottom">$detail.DebitAmount</td>
							#foreach($!designcurr in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
								#set($money1 = "")
								#set($money2 = "")
								#set($currs = "$!designcurr")
								#if("$currs"=="$!allDetData.isbaseCurr")
									#set($currs="")
								#end
								#if("$!currs"=="$detail.Currency")
									#set($money1 = $detail.LendCurrencyAmount)
									#set($money2 = $detail.LendAmount)
								#end
								#if("$showCurrency"=="1")
									<td class="tdDes1 hideBottom">$!money1</td>
								#end
								<td class="tdDes1 hideBottom">$!money2</td>
							#end
							<td class="tdDes1 hideBottom">$detail.LendAmount</td>
						#else
							<td class="tdDes1 hideBottom">$detail.DebitAmount</td>
							<td class="tdDes1 hideBottom">$detail.LendAmount</td>
						#end
						<td class="tdDes3 hideBottom">$!detail.isflag</td>
						<td class="tdDes1 hideBottom">$!detail.sumAmount</td>
						#foreach($!items in $!globals.strSplitByFlag($!globals.get($!designBean,3),'//'))
							#set($itemsvalue = $!globals.strSplitByFlag($!items,';'))
							#if("$!globals.get($!itemsvalue,0)"=="1")
								#foreach($!itemcurrency in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
									#set($money1="")
									#set($money2="")
									#set($icurrency = "$!itemcurrency")
									#if("$!icurrency"=="$!allDetData.isbaseCurr")
										#set($icurrency="") 
									#end
									#set($acctypename1 = "curr_$!{icurrency}_$!{globals.get($!itemsvalue,1)}_DebitCurrencyAmount")
									#set($acctypename2 = "curr_$!{icurrency}_$!{globals.get($!itemsvalue,1)}_DebitAmount")
									#set($money1 = $detail.get($acctypename1))
									#set($money2 = $detail.get($acctypename2))
									#if("$showCurrency"=="1")
										<td class="tdDes1 hideBottom">$money1</td>
									#end
									<td class="tdDes1 hideBottom">$money2</td>
								#end
							#end
						#end
						#foreach($!items in $!globals.strSplitByFlag($!globals.get($!designBean,3),'//'))
							#set($itemsvalue = $!globals.strSplitByFlag($!items,';'))
							#if("$!globals.get($!itemsvalue,0)"=="2")
								#foreach($!itemcurrency in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
									#set($money1="")
									#set($money2="")
									#set($icurrency = "$!itemcurrency")
									#if("$!icurrency"=="$!allDetData.isbaseCurr")
										#set($icurrency="") 
									#end
									#set($acctypename1 = "curr_$!{icurrency}_$!{globals.get($!itemsvalue,1)}_LendCurrencyAmount")
									#set($acctypename2 = "curr_$!{icurrency}_$!{globals.get($!itemsvalue,1)}_LendAmount")
									#set($money1 = $detail.get($acctypename1))
									#set($money2 = $detail.get($acctypename2))
									#if("$showCurrency"=="1")
										<td class="tdDes1 hideBottom">$money1</td>
									#end
									<td class="tdDes1 hideBottom">$money2</td>
								#end
							#end
						#end
					</tr>
					#end
					
					#set($dates = "period_$periods.get('AccYear')_$periods.get('AccPeriod')")
					#set($curData = $!allDetData.get($dates))
					#if("$!conMap.takeBrowNo"!="1" || "$!curData"!="")
					<!-- 本期合计 -->
					<tr class="backcolor">
						<td class="tdDes3 hideBottom">$periods.get('periodEnd')</td>
						<td class="hideBottom"></td>
						<td class="tdDes2 hideBottom">本期合计</td>
						#if("$isflag" == "false")
							#foreach($!designcurr in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
								#set($datamoney1="")
								#set($datamoney2="")
								#set($money1 = "")
								#set($money2 = "")
								#set($currs = "$!designcurr")
								#if("$currs"=="$!allDetData.isbaseCurr")
									#set($currs="")
								#end
								#set($datamoney1 = "curr_$!{currs}_sumDebitCurrencyAmount")
								#set($datamoney2 = "curr_$!{currs}_sumDebitAmount")
								#set($money1 = $curData.get($datamoney1))
								#set($money2 = $curData.get($datamoney2))
									#if("$showCurrency"=="1")
										<td class="tdDes1 hideBottom">$!money1</td>
									#end
								<td class="tdDes1 hideBottom">$!money2</td>
							#end
							<td class="tdDes1 hideBottom">$!curData.totalsumDebitAmount</td>
							#foreach($!designcurr in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
								#set($datamoney1="")
								#set($datamoney2="")
								#set($money1 = "")
								#set($money2 = "")
								#set($currs = "$!designcurr")
								#if("$currs"=="$!allDetData.isbaseCurr")
									#set($currs="")
								#end
								#set($datamoney1 = "curr_$!{currs}_sumLendCurrencyAmount")
								#set($datamoney2 = "curr_$!{currs}_sumLendAmount")
								#set($money1 = $curData.get($datamoney1))
								#set($money2 = $curData.get($datamoney2))
									#if("$showCurrency"=="1")
										<td class="tdDes1 hideBottom">$!money1</td>
									#end
								<td class="tdDes1 hideBottom">$!money2</td>
							#end
							<td class="tdDes1 hideBottom">$!curData.totalsumLendAmount</td>
						#else
							<td class="tdDes1 hideBottom">$!curData.totalsumDebitAmount</td>
							<td class="tdDes1 hideBottom">$!curData.totalsumLendAmount</td>
						#end
						<td class="tdDes3 hideBottom">$!curData.totalBalanceisflag</td>
						<td class="tdDes1 hideBottom">$!curData.totalBalance</td>
						#foreach($!items in $!globals.strSplitByFlag($!globals.get($!designBean,3),'//'))
							#set($itemsvalue = $!globals.strSplitByFlag($!items,';'))
							#if("$!globals.get($!itemsvalue,0)"=="1")
								#foreach($!itemcurrency in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
									#set($money1="")
									#set($money2="")
									#set($acctypename1="")
									#set($acctypename2="")
									#set($icurrency = "$!itemcurrency")
									#if("$!icurrency"=="$!allDetData.isbaseCurr")
										<!-- 是本位币 -->
										#set($icurrency="") 
									#end
									#set($acctypename1 = "curr_$!{icurrency}_$!{globals.get($!itemsvalue,1)}_sumDebitCurrencyAmount")
									#set($acctypename2 = "curr_$!{icurrency}_$!{globals.get($!itemsvalue,1)}_sumDebitAmount")
									#set($money1 = $curData.get($acctypename1))
									#set($money2 = $curData.get($acctypename2))
										#if("$showCurrency"=="1")
											<td class="tdDes1 hideBottom">$money1</td>
										#end
									<td class="tdDes1 hideBottom">$money2</td>
								#end
							#end
						#end
						#foreach($!items in $!globals.strSplitByFlag($!globals.get($!designBean,3),'//'))
							#set($itemsvalue = $!globals.strSplitByFlag($!items,';'))
							#if("$!globals.get($!itemsvalue,0)"=="2")
								#foreach($!itemcurrency in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
									#set($icurrency = "$!itemcurrency")
									#set($acctypename1="")
									#set($acctypename2="")
									#set($money1="")
									#set($money2="")
									#if("$!icurrency"=="$!allDetData.isbaseCurr")
										<!-- 是本位币 -->
										#set($icurrency="") 
									#end
									#set($acctypename1 = "curr_$!{icurrency}_$!{globals.get($!itemsvalue,1)}_sumLendCurrencyAmount")
									#set($acctypename2 = "curr_$!{icurrency}_$!{globals.get($!itemsvalue,1)}_sumLendAmount")
									#set($money1 = $curData.get($acctypename1))
									#set($money2 = $curData.get($acctypename2))
										#if("$showCurrency"=="1")
											<td class="tdDes1 hideBottom">$money1</td>
										#end
									<td class="tdDes1 hideBottom">$money2</td>
								#end
							#end
						#end
					</tr>
					
					<!-- 本年累计 -->
					<tr class="backcolor1">
						#set($dates = "CredYear_$periods.get('AccYear')_$periods.get('AccPeriod')")
						#set($curData = $!allDetData.get($dates))
						<td class="tdDes3 hideBottom">$periods.get('periodEnd')</td>
						<td class="hideBottom"></td>
						<td class="tdDes2 hideBottom">本年累计</td>
						#if("$isflag" == "false")
							#foreach($!designcurr in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
								#set($datamoney1="")
								#set($datamoney2="")
								#set($money1 = "")
								#set($money2 = "")
								#set($currs = "$!designcurr")
								#if("$currs"=="$!allDetData.isbaseCurr")
									#set($currs="")
								#end
								#set($datamoney1 = "curr_$!{currs}_sumDebitCurrencyAmount")
								#set($datamoney2 = "curr_$!{currs}_sumDebitAmount")
								#set($money1 = $curData.get($datamoney1))
								#set($money2 = $curData.get($datamoney2))
									#if("$showCurrency"=="1")
										<td class="tdDes1 hideBottom">$!money1</td>
									#end
								<td class="tdDes1 hideBottom">$!money2</td>
							#end
							<td class="tdDes1 hideBottom">$!curData.totalsumDebitAmount</td>
							#foreach($!designcurr in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
								#set($datamoney1="")
								#set($datamoney2="")
								#set($money1 = "")
								#set($money2 = "")
								#set($currs = "$!designcurr")
								#if("$currs"=="$!allDetData.isbaseCurr")
									#set($currs="")
								#end
								#set($datamoney1 = "curr_$!{currs}_sumLendCurrencyAmount")
								#set($datamoney2 = "curr_$!{currs}_sumLendAmount")
								#set($money1 = $curData.get($datamoney1))
								#set($money2 = $curData.get($datamoney2))
									#if("$showCurrency"=="1")
									<td class="tdDes1 hideBottom">$!money1</td>
									#end
								<td class="tdDes1 hideBottom">$!money2</td>
							#end
							<td class="tdDes1 hideBottom">$!curData.totalsumLendAmount</td>
						#else
							<td class="tdDes1 hideBottom">$!curData.totalsumDebitAmount</td>
							<td class="tdDes1 hideBottom">$!curData.totalsumLendAmount</td>
						#end
						<td class="tdDes3 hideBottom">$!curData.totalBalanceisflag</td>
						<td class="tdDes1 hideBottom">$!curData.totalBalance</td>
						#foreach($!items in $!globals.strSplitByFlag($!globals.get($!designBean,3),'//'))
							#set($itemsvalue = $!globals.strSplitByFlag($!items,';'))
							#if("$!globals.get($!itemsvalue,0)"=="1")
								#foreach($!itemcurrency in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
									#set($money1="")
									#set($money2="")
									#set($acctypename1="")
									#set($acctypename2="")
									#set($icurrency = "$!itemcurrency")
									#if("$!icurrency"=="$!allDetData.isbaseCurr")
										<!-- 是本位币 -->
										#set($icurrency="") 
									#end
									#set($acctypename1 = "curr_$!{icurrency}_$!{globals.get($!itemsvalue,1)}_sumDebitCurrencyAmount")
									#set($acctypename2 = "curr_$!{icurrency}_$!{globals.get($!itemsvalue,1)}_sumDebitAmount")
									#set($money1 = $curData.get($acctypename1))
									#set($money2 = $curData.get($acctypename2))
										#if("$showCurrency"=="1")
										<td class="tdDes1 hideBottom">$money1</td>
										#end
									<td class="tdDes1 hideBottom">$money2</td>
								#end
							#end
						#end
						#foreach($!items in $!globals.strSplitByFlag($!globals.get($!designBean,3),'//'))
							#set($itemsvalue = $!globals.strSplitByFlag($!items,';'))
							#if("$!globals.get($!itemsvalue,0)"=="2")
								#foreach($!itemcurrency in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
									#set($icurrency = "$!itemcurrency")
									#set($acctypename1="")
									#set($acctypename2="")
									#set($money1="")
									#set($money2="")
									#if("$!icurrency"=="$!allDetData.isbaseCurr")
										<!-- 是本位币 -->
										#set($icurrency="") 
									#end
									#set($acctypename1 = "curr_$!{icurrency}_$!{globals.get($!itemsvalue,1)}_sumLendCurrencyAmount")
									#set($acctypename2 = "curr_$!{icurrency}_$!{globals.get($!itemsvalue,1)}_sumLendAmount")
									#set($money1 = $curData.get($acctypename1))
									#set($money2 = $curData.get($acctypename2))
										#if("$showCurrency"=="1")
										<td class="tdDes1 hideBottom">$money1</td>
										#end
									<td class="tdDes1 hideBottom">$money2</td>
								#end
							#end
						#end
					</tr>
					
					#if("$!conMap.showPeriodBalaBase"=="1")
					<!-- 期末余额 -->
					<tr>
						#set($dates = "CredYear_$periods.get('AccYear')_$periods.get('AccPeriod')")
						#set($curData = $!allDetData.get($dates))
						<td class="tdDes3 hideBottom">$periods.get('periodEnd')</td>
						<td class="hideBottom"></td>
						<td class="tdDes2 hideBottom">期末余额</td>
						#if("$isflag" == "false")
							#foreach($!designcurr in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
									#if("$showCurrency"=="1")
										<td class="tdDes1 hideBottom"></td>
									#end
								<td class="tdDes1 hideBottom"></td>
							#end
							<td class="tdDes1 hideBottom"></td>
							#foreach($!designcurr in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
									#if("$showCurrency"=="1")
									<td class="tdDes1 hideBottom"></td>
									#end
								<td class="tdDes1 hideBottom"></td>
							#end
							<td class="tdDes1 hideBottom"></td>
						#else
							<td class="tdDes1 hideBottom">$!curData.totalsumDebitAmount</td>
							<td class="tdDes1 hideBottom">$!curData.totalsumLendAmount</td>
						#end
						<td class="tdDes3 hideBottom">$!curData.totalBalanceisflag</td>
						<td class="tdDes1 hideBottom">$!curData.totalBalance</td>
						#foreach($!items in $!globals.strSplitByFlag($!globals.get($!designBean,3),'//'))
							#set($itemsvalue = $!globals.strSplitByFlag($!items,';'))
							#if("$!globals.get($!itemsvalue,0)"=="1")
								#foreach($!itemcurrency in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
									#set($money1="")
									#set($money2="")
									#set($acctypename1="")
									#set($acctypename2="")
									#set($icurrency = "$!itemcurrency")
									#if("$!icurrency"=="$!allDetData.isbaseCurr")
										<!-- 是本位币 -->
										#set($icurrency="") 
									#end
									#set($acctypename1 = "curr_$!{icurrency}_$!{globals.get($!itemsvalue,1)}_sumDebitCurrencyAmount")
									#set($acctypename2 = "curr_$!{icurrency}_$!{globals.get($!itemsvalue,1)}_sumDebitAmount")
									#set($money1 = $curData.get($acctypename1))
									#set($money2 = $curData.get($acctypename2))
										#if("$showCurrency"=="1")
										<td class="tdDes1 hideBottom">$money1</td>
										#end
									<td class="tdDes1 hideBottom">$money2</td>
								#end
							#end
						#end
						#foreach($!items in $!globals.strSplitByFlag($!globals.get($!designBean,3),'//'))
							#set($itemsvalue = $!globals.strSplitByFlag($!items,';'))
							#if("$!globals.get($!itemsvalue,0)"=="2")
								#foreach($!itemcurrency in $!globals.strSplitByFlag($!globals.get($!designBean,4),','))
									#set($icurrency = "$!itemcurrency")
									#set($acctypename1="")
									#set($acctypename2="")
									#set($money1="")
									#set($money2="")
									#if("$!icurrency"=="$!allDetData.isbaseCurr")
										<!-- 是本位币 -->
										#set($icurrency="") 
									#end
									#set($acctypename1 = "curr_$!{icurrency}_$!{globals.get($!itemsvalue,1)}_sumLendCurrencyAmount")
									#set($acctypename2 = "curr_$!{icurrency}_$!{globals.get($!itemsvalue,1)}_sumLendAmount")
									#set($money1 = $curData.get($acctypename1))
									#set($money2 = $curData.get($acctypename2))
										#if("$showCurrency"=="1")
										<td class="tdDes1 hideBottom">$money1</td>
										#end
									<td class="tdDes1 hideBottom">$money2</td>
								#end
							#end
						#end
					</tr>
					#end
					#end
				#end
			</tbody>
		</table>
		</div>
		<!-- 条件查询 -->
		<div id="highSearch" class="search_popup" style="display:none;height: 360px;top: 220px;">
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
						<tr>
							<td class="tdDes1">多栏账名称：&nbsp;</td>
							<td colspan="3" valign="bottom">
								<select style="width:250px;" id="accName" name="accName">
								</select>
								<input class="bu_4" type="button" style="height:25px;border:none;" onclick="dealdesign()"/>
							</td>
						</tr>
						<tr><td class="tdDes1" width="19%">会计期间：&nbsp;</td><td width="35%"><input name="periodYearStart" id="periodYearStart" value="$!conMap.periodYearStart" size="2"/>&nbsp;年

							<input name="periodStart" id="periodStart" value="$!conMap.periodStart" size="2"/>&nbsp;期</td>
						<td class="tdDes1" width="10%">至&nbsp;</td><td width="35%"><input name="periodYearEnd" id="periodYearEnd" value="$!conMap.periodYearEnd" size="2"/>&nbsp;年

							<input name="periodEnd" id="periodEnd" value="$!conMap.periodEnd" size="2"/>&nbsp;期</td>
						</tr>
						<!-- #if($!globals.getSysSetting('currency')=="true")
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
						#end -->
						</thead>
						<tbody>
						<tr>
							#if($!globals.getSysSetting('currency')=="true")
								<td class="td_left1" colspan="2"><input name="showFormCurrency" id="showFormCurrency" value="1" type="checkbox" #if("$conMap.showFormCurrency"=="1")checked#end/><label for="showFormCurrency">显示原币金额</label></td>
							#end
							<td #if($!globals.getSysSetting('currency')=="true")class="td_left2"#else class="td_left1"#end colspan="2"><input name="showOperationBranch" id="showOperationBranch" value="1" type="checkbox" #if("$conMap.showOperationBranch"=="1")checked#end/><label for="showOperationBranch">业务记录分行显示</label></td>
						</tr>
						<tr>
							#if("$!AccMainSetting.isAuditing"=="1")
							<td class="td_left1" colspan="2"><input name="binderNo" id="binderNo" value="1" type="checkbox" #if("$conMap.binderNo"=="1")checked#end/><label for="binderNo">包括未过账凭证</label></td>
							#end
							<td #if("$!AccMainSetting.isAuditing"=="1")class="td_left2"#else class="td_left1"#end colspan="2"><input name="showPeriodBalaBase" id="showPeriodBalaBase" value="1" type="checkbox" #if("$conMap.showPeriodBalaBase"=="1")checked#end/><label for="showPeriodBalaBase">显示明细项目期末余额</label></td>
						</tr>
						<tr>
							<td class="td_left1" colspan="2"><input name="showBanAccTypeInfo" id="showBanAccTypeInfo" value="1" type="checkbox" #if("$conMap.showBanAccTypeInfo"=="1")checked#end/><label for="showBanAccTypeInfo">显示禁用科目</label></td>
							<td class="td_left2" colspan="2"><input name="takeBrowNo" id="takeBrowNo" value="1" type="checkbox" #if("$conMap.takeBrowNo"=="1")checked#end/><label for="takeBrowNo">无发生额不显示</label></td>
						</tr>
						<tr>
							<td class="td_left1" colspan="4"><input name="balanceAndTakeBrowNo" id="balanceAndTakeBrowNo" value="1" type="checkbox" #if("$conMap.balanceAndTakeBrowNo"=="1")checked#end/><label for="balanceAndTakeBrowNo">余额为零且无发生额不显示</label></td>
						</tr>
						</tbody>
					</table></div>
					#if("$!globals.getSysSetting('standardAcc')"=="true")
					<div style="margin-top: 10px;">
						<table cellpadding="0" cellspacing="0" width="100%">
							<thead>
								<tr>
									<td class="td_left2">项目类别：</td>
									<td>
										<select class="li_sl" id="itemSort" name="itemSort" onchange="dealItem()">
											<option value="DepartmentCode" #if("$conMap.itemSort"=="DepartmentCode")selected#end>部门</option>
											<option value="EmployeeID" #if("$conMap.itemSort"=="EmployeeID")selected#end>职员</option>
											<option value="StockCode" #if("$conMap.itemSort"=="StockCode")selected#end>仓库</option>
											<option value="ClientCode" #if("$conMap.itemSort"=="ClientCode")selected#end>客户</option>
											#if($!globals.getSysSetting('Project')=="true")
											<option value="ProjectCode" #if("$conMap.itemSort"=="ProjectCode")selected#end>项目</option>
											#end
											<option value="SuplierCode" #if("$conMap.itemSort"=="SuplierCode")selected#end>供应商</option>
										</select>
									</td>
								</tr>
								<tr>
									<td class="td_left2">项目代码：</td>
									<td style="white-space: nowrap;" class="searchinput"><input name="acctypeCodeStart" id="acctypeCodeStart" value="$!conMap.acctypeCodeStart" size="13" ondblclick="selectItemCode('acctypeCodeStart')" />
									<img src='/style1/images/search.gif' onClick="selectItemCode('acctypeCodeStart')" class='search' />
									<span style="padding-left: 30px;">至&nbsp;</span>
									<input name="acctypeCodeEnd" id="acctypeCodeEnd" value="$!conMap.acctypeCodeEnd" size="13" ondblclick="selectItemCode('acctypeCodeEnd')"/>
									<img src='/style1/images/search.gif' onClick="selectItemCode('acctypeCodeEnd')" class='search'/></td>
								</tr>
							</thead>
						</table>
					</div>
					#end
					<td>
				</tr>
			</table>
			<span class="search_popup_bu">
				<input type="button" onclick="onsubmits();" class="bu_1"/>
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