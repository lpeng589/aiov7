<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>账务处理流程</title>
<link type="text/css" rel="stylesheet" href="/style/css/reg.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<style type="text/css">
body{font:12px/1.5 Microsoft Yahei,Arial;color:#000;background:url(/style/images/item/html_bg.jpg) repeat 0 0;margin:0;padding:0;}
.div_ts{width:120px;border: 2px aliceblue solid;border-radius:7px;cursor:pointer;text-align:center;}
.div_ts label{cursor:pointer;}
.d_table{width:700px;padding:80px;background:#fff;}

.d_table>p{text-align:center;font-size:20px;}
.div_ts_bcolor1{border: 2px lightblue solid;}
.showW{margin:20px 0 0 0;background:#f2f2f2;text-align: left;border-radius:2px;}
.showW>div{position:relative;padding:15px 0;}
.showW>div>b{width:16px;height:16px;display:inline-block;position:absolute;background:url(/style/images/body/i_017.gif) no-repeat 0 0;top:-6px;}
.b_1{left:30px;}
#if($!globals.getSysSetting('currency')=="true")
	.b_2{left:185px;}
	.b_3{left:335px;}
	.b_4{left:485px;}
	.b_5{left:640px;}
#else
	.b_2{left:225px;}
	.b_4{left:420px;}
	.b_5{left:615px;}
#end

.p_checkPre{text-align: left;}

.acc {border-top:1px solid #bbb;border-right:1px solid #bbb;margin:0 auto;margin-bottom:15px;width:90%;margin-top:5px;}
.acctr {height:22px;}
.acc td {border-left:1px solid #bbb;border-bottom:1px solid #bbb; text-align:center; height:22px;}

.resetCred{margin:0 auto;}
.resetCred td{text-align:center; height:22px;}

.d_table .t-p{position:absolute;left:20px;top:0;font-size:16px;line-height:34px;color:#666;padding:0 0 0 35px;font-weight:bolder;}
.t-p>b{width:29px;height:29px;background:url(/style/v7/images/v7_icons.png) no-repeat -40px -2px;display:inline-block;position:absolute;left:0;top:0;}

</style>

<script type="text/javascript">
	$(document).ready(function (){
		
		//鼠标移动到选项时，突出
		$(".div_ts").mouseover(function(){
			var sNum = $(this).attr("show");
			$(this).addClass("div_ts_bcolor1").parent("td").siblings("td").find("div.div_ts").removeClass("div_ts_bcolor1");
			$("."+sNum).show().siblings("div").hide();
		})
		
		//点击一项   
		$(".div_ts").click(function(){
			var showType = $(this).attr("show");
			$("."+showType).show().siblings("div").hide();
			if(showType == "settleProfitLossBegin"){
				//结转损益
				settleProfitLoss();
			}else if(showType == "resetCredNo"){
				//凭证生成
				resetCredNo();
			}else if(showType == "adjustExchange"){
				//期末调汇
				adjustExchange();
			}else if(showType == "sysAcc"){
				//月结
				sysAcc();
			}else if(showType == "businessAcc"){
				//业务月结
				businessAcc();
			}
		});
		
		var statusId = 1;
		#if("$!sysAccStatus.statusId" == "")
			statusId = 1;
		#else
			statusId = $!sysAccStatus.statusId;
		#end
		$(".div_ts").each(function(){
			var tg = $(this).attr("tg");
			if(parseInt(tg) != parseInt(statusId) && parseInt(tg) != 0){
				//$(this).unbind("click");
				//$(this).css("background-color","#f2f2f2");
			}
		});
		$("#div"+statusId).show();
	});
	
	
	/* 业务月结 */
	function businessAcc(){
		asyncbox.confirm('是否进行业务月结','提示',function(action){
		　　if(action == 'ok'){
			if(typeof(top.jblockUI)!="undefined"){
				top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
			}			
			document.getElementById("tip").innerHTML='$text.get("closeAcc.tip.title")';
			window.location.href="/SysAccAction.do?exe=exe&type=settleAcc&type2=billSettleAcc&winCurIndex=$winCurIndex";
		  }
		});
	}
	
	
	/* 月结 */
	function sysAcc(){
		asyncbox.confirm('$msgtext','提示',function(action){
		　　if(action == 'ok'){
			if(typeof(top.jblockUI)!="undefined"){
				top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
			}
			document.getElementById("tip").innerHTML='$text.get("closeAcc.tip.title")';
			displays();
			window.location.href="/SysAccAction.do?exe=exe&type=settleAcc&type2=AccSettleAcc&winCurIndex=$winCurIndex";
		  }
		});
	}
	
	/* 月结执行动态提示 */
	function displays(){ 
	    var obj=document.getElementById("course"); 
	    var urls="/UtilServlet?operation=closeAccCourse";
			 AjaxRequest(urls) ;
			 
			 if(response==""||response=="CLOSE_SUCCESS"){
				 obj.innerHTML='月结成功';	
				 if(typeof(top.jblockUI)!="undefined"){
					top.junblockUI(); 
				}	
				return; 
			}else if(new RegExp("^CLOSE_FAILURE:").test(response)){
				 obj.innerHTML=response.substring("CLOSE_FAILURE:".length);	
				 if(typeof(top.jblockUI)!="undefined"){
					top.junblockUI(); 
				}	 
				return;
			}else if(response==""||response=="checkNotAuditBill"){
				 obj.innerHTML='$text.get("closeacc.tip.notAuditBill")';		 
			}else if(response=="checkDraftBill"){
				 obj.innerHTML='$text.get("closeacc.tip.draftBill")';		 
			}else if(response=="settleCostBegin"){
				 obj.innerHTML='$text.get("closeacc.tip.setcost")';		 
			}else if(response=="settleModulesBegin"){		
			 	obj.innerHTML='$text.get("closeacc.tip.setmodules")';
			}else if(response=="settleProfitLossValidate"){
				 obj.innerHTML='$text.get("closeacc.prolossvalidate.info")';
			}else if(response=="updatePriodBegin"){		
				 obj.innerHTML='$text.get("closeacc.tip.updateperiod")';		 
			}else if(response=="settleCreateAcc"){		
				 obj.innerHTML='正在生成月结凭证';		 
			}
			setTimeout("courseDisplay()",20);
	 }
	
	/* 期末调汇 */
	function adjustExchange(){
		asyncbox.open({
		id:'dealdiv',
	　　　	url : '/AdjustExchangeAction.do?operation=4&optype=queryExchange&type=settleAcc',
	　　　	width : 800,
		height: 450,
		 modal　: true,
		 title : '期末调汇',
		 btnsbar : jQuery.btn.OKCANCEL,
			 callback : function(action,opener){
		　　　　　if(action == 'ok'){
				if(!opener.submitForm()){
					return false;
				}
		　　　　　}
		　　　}
	　　　});
	}
	
	/* 关闭 */
	function dealAsyn(){
		jQuery.close('dealdiv');
	}
	
	/* 凭证生成 */
	function resetCredNo(){
		mdiwin('/GenCertificateAction.do?src=menu','生成业务凭证');
	}
	
	/* 结转损益 */
	function settleProfitLoss(){
		asyncbox.confirm('是否进行结转损益！','提示',function(action){
		　　if(action == 'ok'){
				if(typeof(top.jblockUI)!="undefined"){
					top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
				}
				document.getElementById("tip").innerHTML='结转损益中......根据电脑配置不同可能需要几十秒至几分钟.';
				
				var accPass=document.getElementById("accPass"); 
				var accPassV="";
				if(accPass.checked){accPassV="true";}
				window.location.href='/SysAccAction.do?exe=exe&type=settleAcc&type2=settleProfitLoss&accPass='+accPassV;
			}
		});
	}
	
	function courseDisplay(){
	    var obj=document.getElementById("course"); 
	    var urls="/UtilServlet?operation=closeAccCourse";
		AjaxRequest(urls) ;
		if(response.trim()==""||response.trim()=="CLOSE_SUCCESS"){
			 obj.innerHTML='月结成功';	
			 if(typeof(top.jblockUI)!="undefined"){
				top.junblockUI(); 
			}	
			return; 
		}else if(response.trim().indexOf("CLOSE_FAILURE:")> -1){
			 obj.innerHTML=response.substring("CLOSE_FAILURE:".length);	
			 if(typeof(top.jblockUI)!="undefined"){
				top.junblockUI(); 
			}	 
			return;
		}else if(response==""||response=="checkNotAuditBill"){
		 obj.innerHTML='正在检查未审核的单据.....';		 
		}else if(response=="checkDraftBill"){
			 obj.innerHTML='正在检查未过账的草稿单据.....';		 
		}else if(response==""||response=="reCalBegin"){
			 obj.innerHTML='正在进行重算成本......';		 
		}else if(response=="settleCostBegin"){
			 obj.innerHTML='正在进行结转成本......';		 
		}else if(response=="settleProfitLossBegin"){
			 obj.innerHTML='正在进行结转损益......';		 
		}else if(response=="settleModulesBegin"){		
		 	obj.innerHTML='正在进行各模块结转处理......';		 
		}else if(response=="CLOSE_SUCCESS"){
			this.parent.location='/loginAction.do?operation=$globals.getOP("OP_LOGOUT")'
		}else if(response=="settleCreateAcc"){		
				 obj.innerHTML='正在生成月结凭证';		 
			}		
		setTimeout("courseDisplay()",10000);
 	}
	function validate(){
		#if($!SettleStatus!="Settleing"||$!AccHasNotAuditing=="true")
		if(typeof(top.junblockUI)!="undefined"){
			top.junblockUI();
		}
		#end
		#if($!returnBills)
		 if(confirm("存在如下入库单价为0的单据，是否继续业务月结？")){
		 	if(typeof(top.jblockUI)!="undefined"){
				top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
			}
			document.getElementById("tip").innerHTML='$text.get("closeAcc.tip.title")';
			window.location.href="/SysAccAction.do?exe=exe&type=settleAcc&type2=billSettleAcc&zeroPriceIn=true&winCurIndex=$winCurIndex";
		 }
		#end
		
		#if($!AccHasNotAuditing=="true")
		alert("当前财务期间中存在未过账凭证，请先进行过账，才可以进行此操作");
		mdiwin('/VoucherManageAction.do?opUrl=url&searchCredYearStart=$!accPeriodBean.getAccYear()&searchCredMonthStart=$!accPeriodBean.getAccMonth()&searchCredYearEnd=$!accPeriodBean.getAccYear()&searchCredMonthEnd=$!accPeriodBean.getAccMonth()&searchwName=notApprove','凭证管理')
		#end
	}
	
	function billDetail(billType,keyId){
		var width=document.documentElement.clientWidth;
		var height=document.documentElement.clientHeight;
	 
		turl="/UserFunctionAction.do?tableName="+billType+"&keyId="+keyId+"&operation=$globals.getOP("OP_DETAIL")&winCurIndex=";
		openPop('PopMainOpdiv','',turl,width,height,false,height==document.documentElement.clientHeight);  
	}
</script>
</head>

<body onLoad="validate();#if($!SettleStatus=="Settleing")courseDisplay();#end">
<div class="div_con" id="listRange_id" align="center" style="overflow-y:auto;">
	
<div class="d_table" style="margin-top:10px;min-height:350px;position:relative;">
	<p class="t-p">
		<b></b>
		月结
	</p>
	<div>
		<div id="tip" align="center" style="color: red;font-size: 14px;font-weight:bold;"></div>
		<div id="course" align="center" style="color: red;font-size: 14px;font-weight:bold;"></div>
	</div>
	<table border="0" cellpadding="0" cellspacing="0" style="width:700px;margin-botto:30px;"><tr>
		#if($!globals.getSysSetting('autoGenerateAcc')!="true")
		<td><div class="div_ts" show="resetCredNo" tg="2">
			<div><img src="/style/images/flow/war10.gif"/></div>
			<div><label>凭证生成</label></div>
		</div></td>
		<td><div>
			<div><img src="/style/images/flow/flowr.gif"/></div>
		</div></td>
		#end		
		<td><div class="div_ts" show="businessAcc" tg="1">
			<div><img src="/style/images/flow/war8.gif"/></div>
			<div><label>业务月结</label></div>
		</div></td>
		#if($!globals.getSysSetting('currency')=="true")
		<td><div>
			<div><img src="/style/images/flow/flowr.gif"/></div>
		</div></td>
		<td><div class="div_ts" show="adjustExchange" tg="3">
			<div><img src="/style/images/flow/war2.gif"/></div>
			<div><label>期末调汇</label></div>
		</div></td>
		#end
		<td><div>
			<div><img src="/style/images/flow/flowr.gif"/></div>
		</div></td>
		<td><div class="div_ts" show="settleProfitLossBegin" tg="4">
			<div><img src="/style/images/flow/pro3.gif"/></div>
			<div><label>结转损益</label></div>						
		</div><input type="checkbox" name="accPass"  value="true" id="accPass" checked="checked">产生凭证直接过账</td>
		<td><div>
			<div><img src="/style/images/flow/flowr.gif"/></div>
		</div></td>
		<td><div class="div_ts" show="sysAcc" tg="5">
			<div><img src="/style/images/flow/war5.gif"/></div>
			<div><label>财务月结</label></div>
		</div></td>
	</tr>
	</table>
	<div class="showW">
		<div class="businessAcc" style="display: none;" id="div1">
			<table border="0" cellpadding="0" cellspacing="0" class="tab_content acc" >
				<tbody>
				<tr class="acctr">
					<td width="100">$text.get("com.accPeriod.year")</td>
					<td width="100">$text.get("com.accPriod.period")</td>
					<td width="100" class="td1" align="center" rowspan="2">$text.get("com.accPriod.settleto")</td>
					<td width="100">$text.get("com.accPeriod.year")</td>
					<td width="100">$text.get("com.accPriod.period")</td>
				</tr>
				<tr>
					<td width="100">$!globals.get($from,0)</td>
					<td width="100">$!globals.get($from,1)</td>
					<td width="100">$!globals.get($to,0)</td>
					<td width="100">$!globals.get($to,1)</td>
				</tr>
				</tbody>
			</table>
			#if($result)
				$text.get("acc.close.notapprove")
				<table border="0" cellpadding="0" cellspacing="0" class="acc" name="table" id="tblSort">
					<thead>
					   #if($result)
						<tr>
							<td width="50"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
							<td width="100">$text.get("reCalculate.lb.billCode")</td>
							<td width="100">$text.get("reCalculate.lb.bilType")</td>
							<td width="100">$text.get("reCalculate.lb.bilDate")</td>
							<td width="100">$text.get("reCalculate.lb.stock")</td>
							<td width="100">$text.get("reCalculate.lb.goodsName")</td>
							#foreach($row in $existPropNames)
								<td width="100">$globals.get($row,1)</td>
								#end
							<td width="100">$text.get("reCalculate.lb.lastPrice")</td>
							<td width="100">$text.get("reCalculate.lb.totalQty")</td>
							<td width="100">$text.get("reCalculate.lb.totalAmt")</td>
						</tr>
						#end			
					</thead>
					<tbody>
					#foreach ($row in $result )
						<tr>
							<td align="center"><input type="checkbox" name="keyId" value="$globals.get($row,0)"></td>
							<td align="center">$globals.get($row,1) &nbsp;</td>
							<td align="center">$globals.get($row,2) &nbsp;</td>
							<td align="center">$globals.get($row,3) &nbsp;</td>
							<td align="center">$globals.get($row,4) &nbsp;</td>
							<td align="center">$globals.get($row,5) &nbsp;</td>
							#foreach($key in $propValues.keySet())
								  #if($globals.get($row,0)==$key)
								      #foreach($value in $propValues.get($key))
									  <td align="right">$value &nbsp;</td>
									  #end
								  #end
								#end
							<td align="center">$!globals.encodeHTMLLine($globals.get($row,6)) &nbsp;</td>
							<td align="center">$!globals.encodeHTMLLine($globals.get($row,7)) &nbsp;</td>
							<td align="center">$!globals.encodeHTMLLine($globals.get($row,8)) &nbsp;</td>
						</tr>
					#end
					</tbody>
				</table>
			#end
			#if($notAuditBill)
				$text.get("acc.close.notapprove")
				<table border="0" cellpadding="0" cellspacing="0" class="listRange_list" name="table" id="tblSort">
					<thead>
						<tr><td width="100">$text.get("reCalculate.lb.bilType")</td></tr>
					</thead>
					<tbody>
						#foreach ($row in $notAuditBill)
						<tr><td align="center">$row</td><tr>
						#end
					</tbody>
				</table>
			#end
			#if($lastQtyNegGoods)
				$text.get("settleAcc.lb.lastQtyNegGoods")
				<table border="0" cellpadding="0" cellspacing="0" class="acc" name="table" id="tblSort">
					<thead>
						<tr>
						<td width="100" align="center">$text.get("iniGoods.lb.stock")</td>
						<td width="100" align="center">$text.get("iniGoods.lb.goodsNo")</td>
						<td width="100" align="center">$text.get("reCalculate.lb.goodsName")</td>
						<td width="100" align="center">$text.get("iniGoods.lb.goodsSpec")</td>
						#foreach($prop in $!globals.getAllAtt())
							#if($prop.isUsed==1)
								<td width="80" align="center">$prop.languageId</td>
							#end
						#end
						<td width="100" align="center">$text.get("common.lb.lastQty")</td></tr>
					</thead>
					<tbody>
						#foreach($row in $lastQtyNegGoods)
						<tr><td>$!row.get("StockFullName")&nbsp;</td>
						<td>$!row.get("goodsNumber")&nbsp;</td>
						<td>$!row.get("goodsFullName")&nbsp;</td>
						<td>$!row.get("goodsSpec")&nbsp;</td>
						#foreach($prop in $!globals.getAllAtt())
						#if($prop.isUsed==1)
						<td>$!row.get($prop.propName)&nbsp;</td>
						#end
						#end
						<td align="right">&nbsp;$!globals.newFormatNumber($!row.get("totalQty"),false,false,$!globals.getSysIntswitch(),'tblStockDet','',true)</td><tr>
						#end
					</tbody>
				</table>
			#end

			$!request.getSession().removeAttribute("lastQtyNegGoods")
			
			#if($draftBill)
				$text.get("settleAcc.msg.draftBill")
				<table border="0" cellpadding="0" cellspacing="0" class="acc" name="table" id="tblSort">
					<thead>
						<tr><td width="100">$text.get("reCalculate.lb.bilType")</td></tr>
					</thead>
					<tbody>
						#foreach ($row in $draftBill )
						<tr><td align="center">$globals.get($row,1)</td><tr>
						#end
					</tbody>
				</table>
			#end
		
			#if($!returnBills)
				入库单价为0的单据




				<table border="0" cellpadding="0" cellspacing="0" class="acc" name="table" id="tblSort">
					<thead>
						<tr>
							<td width="100">单据类型</td>
							<td width="100">$text.get("mrp.lb.billDate")</td>
							<td width="100">$text.get("mrp.lb.relationId")</td>
						</tr>	
					</thead>
					<tbody>
			
					#foreach ($row in $returnBills)
					<tr>
						<td align="center">#if($globals.get($row,0)=="tblOtherIn")其他入库单#elseif($globals.get($row,0)=="tblSalesReplace")销售换货单#elseif($globals.get($row,0)=="tblSalesReturnStock")销售退货单#end&nbsp;</td>
						<td align="center">$globals.get($row,2)&nbsp;</td>
						<td align="center"><a href="#" onclick="billDetail('$globals.get($row,0)','$globals.get($row,1)')">$globals.get($row,3)</a>&nbsp;</td>
					</tr>
					#end
					
					
					</tbody>
				</table>
			#end
			<span style="padding:0 20px;display:inline-block;"><i style="padding-left:25px;display:inline-block;"></i>月结存将封存当前会计期间的所有账本数据，将该月份最终的结存值作为下一月份的期初值,月结账后，将不能 修改或删除已结账的业务单据或引起已结账数据产生变化的操作。 系统将根据以下单据类型生成不同的凭证: 销售单、销售退货单、组装与拆分单、其他出库单、报损单、调价单 期末结账后,将进入下一个进销存期间。结账之后，如果要返回上个进销存期间，请使用进销存期末反结账功能。 注:在做月结存的时候，请一定要先做数据备份</span>
			<b class="b_1"></b>
		</div>
		<div class="resetCredNo" style="display: none;" id="div2">
			<span style="padding:0 20px;display:inline-block;"><i style="padding-left:25px;display:inline-block;"></i>凭证生成：不启用系统配置"自动产生凭证"时需要手工生成凭证才能月结</span>
			<b class="b_2"></b>
		</div>
		<div class="adjustExchange" style="display: none;" id="div3">
			<span style="padding:0 20px;display:inline-block;"><i style="padding-left:25px;display:inline-block;"></i>期末调汇：对外币核算的科目在期末自动计算汇兑损益，生成汇兑损益凭证。期末各种外币账户的期末外币余额（包括外币现金、银行存款和以外币结算的债权和债务），按照期末市场汇率折合为记账本位币金额。按期末调整汇率折算出的科目本位币余额与调汇前的本位币余额的差额，将作为汇兑损益记入费用。</span>
			<b class="b_3"></b>
		</div>
		<div class="settleProfitLossBegin" style="display: none;" id="div4">
			<table border="0" cellpadding="0" cellspacing="0" class="tab_content acc" >
				<tbody>
				<tr class="acctr">
					<td width="100">$text.get("com.accPeriod.year")</td>
					<td width="100">$text.get("com.accPriod.period")</td>
				</tr>
				<tr>
					<td width="100">$!globals.get($Accfrom,0)</td>
					<td width="100">$!globals.get($Accfrom,1)</td>
				</tr>
				</tbody>
			</table>
			<span style="padding:0 20px;display:inline-block;"><i style="padding-left:25px;display:inline-block;"></i>结转损益：主要是将所有损益类科目的本期余额全部自动转入本年利润科目，并生成一张结转损益记账凭证。可以反映集团企业在一个会计期间内实现的利润或亏损总额。</span>
			<b class="b_4"></b>
		</div>
		<div class="sysAcc" style="display: none;" id="div5">
			<span style="padding:0 20px;display:inline-block;"><i style="padding-left:25px;display:inline-block;"></i>财务月结：</span>
			<table border="0" cellpadding="0" cellspacing="0" class="tab_content acc" >
				<tbody>
				<tr class="acctr">
					<td width="100">$text.get("com.accPeriod.year")</td>
					<td width="100">$text.get("com.accPriod.period")</td>
					<td width="100" class="td1" align="center" rowspan="2">$text.get("com.accPriod.settleto")</td>
					<td width="100">$text.get("com.accPeriod.year")</td>
					<td width="100">$text.get("com.accPriod.period")</td>
				</tr>
				<tr>
					<td width="100">$!globals.get($Accfrom,0)</td>
					<td width="100">$!globals.get($Accfrom,1)</td>
					<td width="100">$!globals.get($Accto,0)</td>
					<td width="100">$!globals.get($Accto,1)</td>
				</tr>
				</tbody>
			</table>
			<b class="b_5"></b>
		</div>
		</div>
	</div>
</div>

<script type="text/javascript"> 
	$("#listRange_id").css("height",document.documentElement.clientHeight);
</script>
</body>
</html>



