<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>凭证汇总表</title>
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
	.w_div .t_span{position:absolute;display:inline-block;z-index:2;background:#fff;padding:0 5px 0 5px;left:20px;line-height:20px;}
	.search_popup ul li{width: auto;height: auto;float: none;}
	.list_li .in_p{float:left;margin:3px 0 0 3px;}
	.list_li .in_lb{float:left;margin:0 0 0 3px;}
	.list_li .in_txt{float:right;}
	.data_list_head tr td{white-space:nowrap;padding:2px 5px;}
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
		check_input();
	});
	//查询
	function onsubmits(){
		if(!isvalidate()){
			return false;
		}
		var flag = false;
		var strValue = "";
		if($("#showAll").is(":checked")){
			strValue = "";
		}else{
			$(".list_li").find("tr").find("td").find("input[type=checkbox]").each(function(){
				if($(this).is(":checked")){
					strValue = strValue+$(this).val()+";"+$(this).parent().next().find("input").val()+";"+$(this).parent().next().next().find("input").val()+";]";
				}
			});
			if(strValue == ""){
				flag = true;
			}
		}
		if(flag){
			$('#highSearch').show();
			alert("请至少选择一种凭证字范围！");
			return false;
		}
		$("#credTypeStr").val(strValue);
		blockUI();
		form.submit();
	}
	
	function check_input(){
		if($("#showAll").is(":checked")){
			$(".list_li").find("input").attr("readonly","readonly");
			$(".list_li").find("input[type=checkbox]").attr("disabled","disabled");
		}else{
			$(".list_li").find("input").removeAttr("readonly");
			$(".list_li").find("input[type=checkbox]").removeAttr("disabled");
		}
	}
	
	function dealValue(obj){
		if(!(isInt2($(obj).val()) || $(obj).val()=="0") && $(obj).val()!=''){
			asyncbox.tips('凭证号应为正整数','提示',800);
			//$(obj).val('');
			$(obj).focus();
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
		$("#div_table").find("input").each(function(){
			$(this).val('');
		});
		$("#dateStart").val("$!globals.getDate()");
		$("#dateEnd").val("$!globals.getDate()");
		$("#levelStart").val(1);
		$("#levelEnd").val(7);
		$("#area").val(0);
		$("#takeBrowNo").attr("checked","checked");
		check_input();
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
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form method="post" scope="request" id="form" name="form" action="/FinanceReportAction.do?optype=ReportCertificateSum" target="">
<input type="hidden" name="comeMode" id="comeMode" value="false" />
<input type="hidden" name="credTypeStr" id="credTypeStr" value="$!conMap.credTypeStr"/>
<input type="hidden" id="type" name="type" value=""/>
	<div class="titlediv">
		<img src="/style1/images/workflow/ti_003.gif"/><span style="font-size: 14px;">凭证汇总表</span>
	</div>
	<div style="float: left;padding-bottom: 2px;margin: 20px 10px 10px 10px;">
	#if("$!comeMode"!="")
		日期区间: $!conMap.dateStart 至 $!conMap.dateEnd
		&nbsp;&nbsp;&nbsp;
		#if($!globals.getSysSetting('currency')=="true")
			币别：

			#if("$!currType"=="")
				<综合本位币>
			#elseif("$!currType"=="all")
				<所有币别多栏式>
			#else
				#foreach($currency in $!currencyList)
					#if("$!conMap.currencyName"=="$!globals.get($!currency,0)")
						$!globals.get($!currency,1)
					#end
				#end
			#end 
		#end
		&nbsp;&nbsp;&nbsp;
		范围：

		#if("$!conMap.area"=="0")
			所有凭证

		#elseif("$!conMap.area"=="1")
			未过账凭证

		#elseif("$!conMap.area"=="2")
			已过账凭证

		#end
		&nbsp;&nbsp;&nbsp;凭证字：
		#if("$!conMap.showAll"=="1")
			全部
		#else
			#foreach($erow in $globals.getEnumerationItems('CredTypeID'))
   				#set($objvalue = "")
    			#foreach ($str in $globals.strSplitByFlag("$!conMap.credTypeStr",";]"))
    				#set($objvalue = $!globals.substring("$str",";"))
    				#if("$erow.value"=="$objvalue")
    					$erow.name
    					#set($value1 = $!globals.get($!globals.strSplit("$str",";"),1))
    					#set($value2 = $!globals.get($!globals.strSplit("$str",";"),2))
    					#if("$value1"=="" && "$value2"=="")
    						(全部);
    					#else
    						($value1 - $value2);
    					#end
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
		#if($LoginBean.operationMap.get("/FinanceReportAction.do?optype=ReportCertificateSum").print() || $!LoginBean.id=="1")
		<div class="op sbtn41" onclick="printReport('FinanceReportAccCertificateSum','ReportCertificateSum');">
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
			#set($row = 1)
			#set($col = 1)
			#set($col1 = 1)
			#set($flag = "")
			#if("$!currType"=="" || "$!currType"=="isBase")
				#set($col1 = 2)
			#elseif("$!currType"=="all")
				#set($row = 2)
				#set($size1 = $!currencyList.size())
				#set($col = $size1+1)
				#set($col1 = $col+1)
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
					<td class="tdDes3" width="50px;" rowspan="$row">No.</td>
					<td class="tdDes3" width="200px;" rowspan="$row">科目代码</td>
					<td class="tdDes3" width="300px;" rowspan="$row">科目名称</td>
					<td class="tdDes3" width="250px;" colspan="$!col">借方金额</td>
					<td class="tdDes3" width="250px;" colspan="$!col">贷方金额</td>
					<td class="tdDes3" width="250px;" colspan="$!col1">余额</td>
				</tr>
					#if("$!flag"=="false")
					<tr>
						#foreach ( $foo in [1..3] )
							#if($foo == 3)
								<td class="tdDes3"></td>
							#end
							<td class="tdDes3">原币</td>
							<td class="tdDes3">本位币</td>
						#end
					</tr>
					#elseif("$!flag"=="true")
					<tr>
						#foreach($foo in [1..3])
							#if($foo == 3)
								<td class="tdDes3" width="100px"></td>
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
			#set($count=1)
			#if("$flag" == "" || "$flag" == "true")
				#set($list=["sumDebitAmountBase","sumLendAmountBase","isflag","sumBalanceAmountBase"])
			#elseif("$flag"=="false")
				#set($list=["sumDebitAmount","sumDebitAmountBase","sumLendAmount","sumLendAmountBase","isflag","sumBalanceAmount","sumBalanceAmountBase"])
			#end
			#foreach($acctype in $accList)
				<tr style="line-height: 24px;" onclick="setBacks(this)">
					<td class="tdDes3" width="50px;">$!count</td>
					<td>$!acctype.AccNumber</td>
					<td >$!acctype.AccFullName</td>
					#foreach ($element in $list)
						#set($valueData = $!acctype.get($element))
						#set($setclass="tdDes1")
						#if("$!element"=="isflag")
							#set($setclass="tdDes3")
						#end
						#if("$flag" == "true")
							#foreach($currency in $!currencyList)
								#if("$!element"!="isflag")
									#set($curId = $!globals.get($!currency,0))
									#if($!globals.get($!currency,2)=="true")
										#set($curId = "")
									#end
									#set($curName = $!globals.replaceString(${element},'Base','')+"_"+${curId})
									#set($curData = $!acctype.get($curName))
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
				#set($count= $count + 1)
			#end
			#if($accList.size()!=0)
				<tr style="line-height: 24px;background-color:lightyellow;">
					<td></td>
					<td></td>
					<td>合计</td>
					#foreach ($element in $list)
						#set($valueData = $!totalMap.get($element))
						#set($setclass="tdDes1")
						#if("$!element"=="isflag")
							#set($setclass="tdDes3")
						#end
						#if("$flag" == "true")
							#foreach($currency in $!currencyList)
								#if("$!element"!="isflag")
									#set($curId = $!globals.get($!currency,0))
									#if($!globals.get($!currency,2)=="true")
										#set($curId = "")
									#end
									#set($curName = $!globals.replaceString(${element},'Base','')+"_"+${curId})
									#set($curData = $!totalMap.get($curName))
									<td class="$setclass">#if('$!curData'!='') $NumericTool.format($!curData,'#,##0.00') #end</td>
									#set($curData = "")
									#set($curName = "")
									#set($curId = "")
								#end
							#end
						#end
						<td class="$setclass">#if('$!valueData'!='') $NumericTool.format($!valueData,'#,##0.00') #end</td>
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
		#elseif($accList.size()==0)
			<div class="querydiv1">
				<p style="width:150px;padding:50px 0 0 0;overflow:hidden;line-height:21px;margin:0 auto;">
					很抱歉，没有找到与您条件匹配的凭证汇总表信息!
				</p>
			</div>
		#end
		</div>
		<!-- 条件查询 -->
		<div id="highSearch" class="search_popup" style="display:none;height: 400px;top: 220px;">
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
						<tr><td class="tdDes1">科目级别:&nbsp;</td><td><input name="levelStart" id="levelStart" value="$!conMap.levelStart" size="13"/></td>
						<td class="tdDes1">至&nbsp;</td><td><input name="levelEnd" id="levelEnd" value="$!conMap.levelEnd" size="13"/></td>
						</tr>
						#if($!globals.getSysSetting('currency')=="true")
						<tr>
							<td class="tdDes1">币别:&nbsp;</td>
							<td colspan="3">
								<select name="currencyName" id="currencyName" style="width:110px;">
									#foreach($currency in $!currencyList)
										<option value="$!globals.get($!currency,0)" #if("$!conMap.currencyName"=="$!globals.get($!currency,0)")selected#end>$!globals.get($!currency,1)</option>
									#end
									<option value="" #if("$!conMap.currencyName"=="")selected#end><综合本位币></option>
									<option value="all" #if("$!conMap.currencyName"=="all")selected#end><所有币别多栏式></option>
								</select>
							</td>
						</tr>
						#end
						<tr>
							<td class="tdDes1">范围:&nbsp;</td>
							<td>
								<select name="area" id="area" style="width:110px;">
									<option value="0" #if("$!conMap.area"=="0")selected#end>所有凭证</option>
									#if("$!AccMainSetting.isAuditing"=="1")
									<option value="1" #if("$!conMap.area"=="1")selected#end>未过账凭证</option>
									#end
									<option value="2" #if("$!conMap.area"=="2")selected#end>已过账凭证</option>
								</select>
							</td>
							#if("$!globals.getSysSetting('standardAcc')"=="true")
							<td class="tdDes1"><input name="showItemDetail" id="showItemDetail" value="1" type="checkbox" #if("$conMap.showItemDetail"=="1")checked#end/></td><td><label for="showItemDetail">显示核算项目明细</label></td>
							#end
						</tr>
						</thead>
						<tbody>
						<tr>
							<td class="tdDes1"><input name="showAll" id="showAll" value="1" type="checkbox" #if("$conMap.showAll"=="1")checked#end onclick="check_input()"/></td><td><label for="showAll">包含所有凭证字号</label></td>
							<td class="tdDes1"><input name="takeBrowNo" id="takeBrowNo" value="1" type="checkbox" #if("$conMap.takeBrowNo"=="1")checked#end/></td><td><label for="takeBrowNo">无发生额不显示</label></td>
						</tr>
						<tr>
							<td colspan="4">
								<div class="w_div" id="div_table" style="width:100%;height:150px;overflow:hidden;position:relative;border: none;">
								  <div class="inside_div" style="padding:0px 10px 0px 0;overflow:hidden;border:1px #a1a1a1 solid;margin:0 auto;margin-top:10px;z-index:1;position:absolute;">
								    <ul class="list_u">
								    	<li class="list_li">
								    		<div style="overflow-y:auto;height:110px;border: none;">
								    		<table cellpadding="0" cellspacing="0" border="1" bordercolor="#a1a1a1" width="100%" style="overflow:auto;border-collapse:collapse;">
								    			<tr style="line-height: 17px;">
								    				<td class="tdDes3">凭证字</td>
								    				<td class="tdDes3">最小号</td>
								    				<td class="tdDes3">最大号</td>
								    			</tr>
								    			#foreach($erow in $globals.getEnumerationItems('CredTypeID'))
								    				#set($values = "")
								    				#set($obj = "")
								    				#set($objvalue = "")
									    			#foreach ($str in $globals.strSplitByFlag("$!conMap.credTypeStr",";]"))
									    				#set($objvalue = $!globals.substring("$str",";"))
									    				#if("$erow.value"=="$objvalue")
									    					#set($values = "true")
									    					#set($obj = $str)
									    				#end
									    			#end
									    			<tr style="line-height: 17px;">
									    				<td class="tdDes3"><input class="in_p" name="credType" id="credType" value="$erow.value" #if("$!values" == "true")checked#end type="checkbox"/><label class="in_lb" for="">$erow.name</label></td>
														<td class="tdDes1"><input class="in_txt" style="text-align:right;" name="minCount" id="minCount" size="5" onblur="dealValue(this)" value="$!globals.get($!globals.strSplit("$!obj",";"),1)"/></td>
														<td class="tdDes1"><input class="in_txt" style="text-align:right;" name="maxCount" id="maxCount" size="5" onblur="dealValue(this)" value="$!globals.get($!globals.strSplit("$!obj",";"),2)"/></td>
													</tr>
												#end
								    		</table>
								    		</div>
								      	</li>
								    </ul>
								  </div>
								  <span class="t_span">选择凭证字范围</span>
								</div>
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