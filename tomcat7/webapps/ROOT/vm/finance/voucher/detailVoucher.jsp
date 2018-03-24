<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>凭证详情</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<link type="text/css" rel="stylesheet" href="/style/css/Voucher.css"/>
<script type="text/javascript" src="$globals.js("/js/formvalidate.vjs","",$text)"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script type="text/javascript" src="/js/aioaccmain.js"></script>
<script type="text/javascript">
	var if_tr;
	var item_value = "item_2";
	$(document).ready(function() {
		 $("#tableItem tr").bind("mouseover",function(){
		 	$(this).css("background","#f9f9f9");
		 });
		 $("#tableItem tr").bind("mouseout",function(){
		 	$(this).css("background","#fff");
		 });
		 $("#backs").bind("click", function(){
		 	window.location.href="/VoucherManageAction.do?operation=4";
		 });
		 $("#add").bind("click", function(){
			window.location.href="/VoucherManage.do?operation=6&isEspecial=list&popWinName=$!popWinName";
		 });
		 //刷新
		 $("#refur").bind("click",function(){
			window.location.reload();
		 });
		 //计算器




		$("#calculator").bind("click",function(){
		 	Run('calc');
		 });
		$("#updates").bind("click",function(){
			updates();
		});
		//关闭
		$("#closes").bind("click", function(){
			if("$!popWinName"!=""){
				window.parent.jQuery.close('$!popWinName');
			}else{
				window.parent.jQuery.close('PopQuerydiv');
			}
			closeWin();
		});
		//审核不通过
		$("#noPass").bind("click",function(){
			noPass();
		});
		//审核通过
		$("#yesPass").bind("click",function(){
			dealAuditing('yesPass');
		});
		//反审核




		$("#reversePass").bind("click",function(){
			dealAuditing('reversePass');
		});
		//过账
		$("#binder").bind("click",function(){
			dealbinder('binder');
		});
		//反过账




		$("#reverseBinder").bind("click",function(){
			dealbinder('reverseBinder');
		});
		//复核
		$("#check").bind("click",function(){
			dealcheck('check');
		});
		//另存模板
		$("#modules").bind("click",function(){
			saveModule("$!values.id");
		 });



		$("#reversecheck").bind("click",function(){
			dealcheck('reverseCheck');
		});
		controlHandle();
		account();
		dateChange();
		$(".btnlp").attr("align","center");
	});
	
	
	/* 控制操作*/
	function controlHandle(){
		//审核
		var auditing = "$!values.isAuditing";
		if(auditing == "finish"){
			$("input").not("#AcceptMode").not("#AcceptNumber").attr("readonly","readonly");
		}
		//复核过账
		var check = "$!values.isReview";
		var wFlowName = "$!values.workFlowNodeName";
		var workFlowNode = "$!values.workFlowNode";
		if(check == "2" || wFlowName=="finish" || workFlowNode=="-1"){
			//已复核和已过账




			$("#AcceptMode").attr("readonly","readonly");
			$("#AcceptNumber").attr("readonly","readonly");
		}
	}
	/* 审核不通过*/
	function noPass(){
		var url = '/VoucherManage.do?optype=approveRemarkPre&voucherId=$!values.id&isList=detail';
		url = encodeURI(encodeURI(url));
		asyncbox.open({
			id : 'nopassDiv',
	　　		url : url,
		    title : '批注',
		　　 width : 480,
		　　 height : 220,
	    	btnsbar : jQuery.btn.OKCANCEL,
		    callback : function(action,opener){
		    	if(action == 'ok'){
		    		var remark = opener.document.getElementById("remark");
					if(remark.value == ""){
						alert("批注不能为空！");
						remark.focus();
						return false;
					}
					opener.saves();
					return false;
		    	}
	　　	　	}
　		});
	}
	/* 审核通过与反审核*/
	function dealAuditing(dealType){
		form.optype.value="dealapprov";
		form.dealType.value=dealType;
		form.operation.value="";
		form.keyId.value="$!values.id";
		form.isList.value="detail";
		form.submit();
	}
	/* 过账与反过账*/
	function dealbinder(dealType){
		form.optype.value="dealbinder";
		form.dealType.value=dealType;
		form.operation.value="";
		form.keyId.value="$!values.id";
		form.isList.value="detail";
		form.submit();
	}
	/* 复核与反复核*/
	function dealcheck(dealType){
		form.optype.value="dealcheck";
		form.dealType.value=dealType;
		form.operation.value="";
		form.keyId.value="$!values.id";
		form.isList.value="detail";
		form.submit();
	}
	/* 合计显示大写 */
	function showBig(obj){
		var objhtml = $(obj).find("span").html();
		if(objhtml!=null && objhtml!="" && objhtml!="&nbsp;"){
			var changeValue = AmountInWords(objhtml,null);
			$("#bigValue").html(changeValue);
		}
	}
	//点击tr显示核算项




	function show_Item(object){
		var itemvalue = $(object).find("td input[id=keyIds]").val();
		$(".he_div").hide();
		$("#he_"+itemvalue).show();
	}
	//金额小数点控制




	function subStringMoney(str){
		var len = "$!globals.getSysSetting('DigitsAmount')";
		var strvalue = str;
		if (strvalue.indexOf(".")>0){
			strvalue = strvalue.substring(0,new Number(strvalue.indexOf("."))+new Number(len)+1);
		}
		return strvalue;
	}
	/* 打印*/
	function prints(){
		printControl("/UserFunctionQueryAction.do?tableName=tblAccMain&reportNumber=ReportAccMain&moduleType=&BillID=$!values.id&operation=18&parentTableName=&winCurIndex=3");
	}
	/* 第一条，上一条，下一条，最后一条*/
	function chooseVoucher(id,number){
		if("$!values.id"==id){
			asyncbox.tips('凭证已是第一条或者最后一条','提示',1500);
			return false;
		}
		window.location.href="/VoucherManageAction.do?operation=5&id="+id+"&number="+number+"&popWinName=$!popWinName";
	} 
	/* 修改*/
	function updates(){
		window.location.href="/VoucherManageAction.do?operation=7&tableName=tblAccMain&id=$!values.id&number=$!number&NoButton=$!NoButton&popWinName=$!popWinName";
	}
	/* 得到核算项*/
	function hesItem(code,count){
		jQuery.ajax({ type: "POST", url: "/VoucherManage.do?optype=queryComputeItem&queryMode=super&accCode="+code,async: false,dataType: "json",success: function(data){
				var breed = data.isbreed;
				var IsDept = breed[0].IsDept;
				var IsPersonal = breed[0].IsPersonal;
				var IsCash = breed[0].IsCash;
				var IsClient = breed[0].IsClient;
				var IsProvider = breed[0].IsProvider;
				var isStock = breed[0].isStock;
				var IsCome = breed[0].IsCome;
				var htmls = "";
				if(IsCome!=1){
					if(IsClient==1){
						$("#he_itemtr_"+count).find("tr[id=trs] span[id=spans]").html("客户：");
						$("#he_itemtr_"+count).find("tr[id=trs]").find("#IsComeName").removeAttr("isCome");
					}else if(IsProvider==1){
						$("#he_itemtr_"+count).find("tr[id=trs] span[id=spans]").html("供应商：");
						$("#he_itemtr_"+count).find("tr[id=trs]").find("#IsComeName").removeAttr("isCome");
					}
				}
	    	}
		});
	}
	/* 改变时间计算是哪一年哪一期 */
	function dateChange(){
		var billdate = "$!values.BillDate";
		var yearspan = billdate.substring(0,billdate.indexOf("-"));
		var periodspan = new Number(billdate.substring(billdate.indexOf("-")+1,billdate.indexOf("-")+3));
		$("#span1").html(yearspan);
		$("#span2").text(periodspan);
	}
	
	function showBillNo(billId,billtype){
		var width = document.documentElement.clientWidth;
		var height = document.documentElement.clientHeight;
		usrc ='/UserFunctionAction.do?tableName='+billtype+'&keyId='+billId+'&operation=5&noback=false&queryChannel=normal&LinkType=@URL:';
		if(billtype == "tblFixedAssetAdd"){
			usrc += "&moduleType=1";
		}
		openPop('PopMainOpdiv','',usrc,width,height,true,height);  
	}
</script>
</head>
<iframe name="formFrame" style="display:none;"></iframe>
<body style="background: #f5f5f5;">
<form id="form" name="form" method="post" action="/VoucherManage.do" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_DETAIL")"/>
<input type="hidden" name="CredNo" id="CredNo" value="$!values.CredNo"/>
<input type="hidden" name="OrderNo" id="OrderNo" value="$!values.OrderNo"/>
<input type="hidden" name="delFiles" id="delFiles" value=""/>
<input type="hidden" name="tableName" id="tableName" value="tblAccMain" />
<input type="hidden" name="Attachment" id="Attachment" value="" />
<input type="hidden" name="attachFiles" value="" />
<input type="hidden" id="recordValue" name="recordValue" value=""/>
<input type="hidden" id="CredYear" name="CredYear" value="$!values.CredYear"/>
<input type="hidden" id="CredMonth" name="CredMonth" value="$!values.CredMonth"/>
<input type="hidden" id="Period" name="Period" value="$!values.Period"/>
<input type="hidden" id="noback" name="noback" value="false"/>
<input type="hidden" id="button" name="button" />
<input type="hidden" id="moduleType" name="moduleType" value=""/>
<input type="hidden" id="winCurIndex" name="winCurIndex" value="4"/>
<input type="hidden" id="workFlowNodeName" name="workFlowNodeName" value="$!values.workFlowNodeName"/>
<input type="hidden" id="RefBillType" name="RefBillType" value="$!values.RefBillType"/>
<input type="hidden" id="createBy" name="createBy" value="$!values.createBy"/>
<input type="hidden" id="createTime" name="createTime" value="$!values.createTime"/>

<input type="hidden" name="optype" id="optype" value=""/>
<input type="hidden" name="dealType" id="dealType" value=""/>
<input type="hidden" name="keyId" id="keyId" value=""/>
<input type="hidden" name="isList" id="isList" value=""/>
<input type="hidden" name="NoButton" id="NoButton" value="$!NoButton"/>
<input type="hidden" name="isEspecial" id="isEspecial" value="$!isEspecial"/>
<input type="hidden" name="popWinName" id="popWinName" value="$!popWinName"/>

#if("$!AccMainSetting.isAuditing"=="1")
	#if("$!values.workFlowNodeName"=="finish" || "$!values.workFlowNode"=="-1")
		<div class="data_img1">
			<img src="/style/images/plan/gz.png"/>
		</div>
	#else
		<!-- 审核通过出现已审核图片 -->
		#if("$!values.isAuditing"=="finish")
		<div class="data_img1">
			<img src="/style/images/plan/shenhe.png"/>
		</div>
		#end
	#end
#else
#end
<table cellpadding="0" cellspacing="0" border="0" class="framework" style="width:100%">
			<tr style="float:right">
				<td>
					<div class="TopTitle">
						#if("$!isEspecial" != "1" )
						<div class="btnlp" id="updates"><img src="/style1/images/oaimages/ni_2.gif" title="修改"/><br />修改</div>
						#end
						#if(($LoginBean.operationMap.get("/VoucherManageAction.do").add() || $!LoginBean.id=="1") && "$!NoButton"!="true" && "$!popWinName"=="")
							<div class="btnlp" id="add" align="center"><img src="/style/plan/M_3.gif" title="添加凭证"/><br />添加</div>
						#end
						<div class="btnlp" id="refur"><img src="/style/themes/icons/reload.png" title="$text.get('oa.common.refresh')"/><br />$text.get('oa.common.refresh')</div>
						<div><img src="/style1/images/desk/tab.jpg" style="height:40px;width:1px;"/></div>
						#if("$!isEspecial" != "1" && "$!NoButton"!="true")
							#if("$firstId"=="$!values.id")
								<div class="btnlp" style="cursor: auto;"><img src="/style/themes/gray/images/pagination_first_no.png" title="第一"/><br />第一条</div>
							#else
								<div class="btnlp" onclick="chooseVoucher('$firstId','$firstNumber')"><img src="/style/themes/gray/images/pagination_first.gif" title="第一"/><br />第一条</div>
							#end
							#if("$preId"=="$!values.id")
								<div class="btnlp" style="cursor: auto;"><img src="/style/themes/gray/images/pagination_prev_no.png" title="上一"/><br />向上</div>
							#else
								<div class="btnlp" onclick="chooseVoucher('$preId','$preNumber')"><img src="/style/themes/gray/images/pagination_prev.gif" title="上一"/><br />向上</div>
							#end
							#if("$nextId"=="$!values.id")
								<div class="btnlp" style="cursor: auto;"><img src="/style/themes/gray/images/pagination_next_no.png" title="下一"/><br />向下</div>
							#else
								<div class="btnlp" onclick="chooseVoucher('$nextId','$nextNumber')"><img src="/style/themes/gray/images/pagination_next.gif" title="下一"/><br />向下</div>
							#end
							#if("$endId"=="$!values.id")
								<div class="btnlp" style="cursor: auto;"><img src="/style/themes/gray/images/pagination_last_no.png" title="最后"/><br />最后条</div>
							#else
								<div class="btnlp" onclick="chooseVoucher('$endId','$endNumber')"><img src="/style/themes/gray/images/pagination_last.gif" title="最后"/><br />最后条</div>
							#end
						<div><img src="/style1/images/desk/tab.jpg" style="height:40px;width:1px;"/></div>
						#end
						#if("$!isEspecial" != "1" || "$!NoButton"=="true")
							<!-- 启用审核 -->
							#if("$!AccMainSetting.isAuditing"=="1")
								<!-- 审核权限 -->
								#if($!globals.isExistInArray($!AccMainSetting.auditingPersont.split(','),$!LoginBean.id) || $!LoginBean.id=="1")
								<div class="btnlp" id="yesPass"><img src="/style1/images/oaimages/enable.png" title="审核"/><br />审核</div>
								<div class="btnlp" id="noPass"><img src="/style1/images/oaimages/noenable.png" title="审核不通过"/><br />审核不通过</div>
								#end
								<!-- 反审核权限 -->
								#if($!globals.isExistInArray($!AccMainSetting.reverseAuditing.split(','),$!LoginBean.id) || $!LoginBean.id=="1")
								<div class="btnlp" id="reversePass"><img src="/style1/images/refresh.gif" title="反审核"/><br />反审核</div>
								<div><img src="/style1/images/desk/tab.jpg" style="height:40px;width:1px;"/></div>
								#end
							#end
							<!-- 复核/反复核 -->
							#if("$!AccMainSetting.isCheck"=="1")
								#if($!globals.isExistInArray($!AccMainSetting.checkPersont.split(','),$!LoginBean.id) || $!LoginBean.id=="1")
								<div class="btnlp" id="check"><img src="/style/themes/icons/redo.png" title="复核"/><br />复核</div>
								<div class="btnlp" id="reversecheck"><img src="/style/themes/icons/undo.png" title="反复核"/><br />反复核</div>
								<div><img src="/style1/images/desk/tab.jpg" style="height:40px;width:1px;"/></div>
								#end
							#end
							#if("$!AccMainSetting.isAuditing"=="1")
								<!-- 过账权限 -->
								#if($!globals.isExistInArray($!AccMainSetting.binderPersont.split(','),$!LoginBean.id) || $!LoginBean.id=="1")
									<div class="btnlp" id="binder"><img src="/style1/images/carefor/up.gif" title="过账"/><br />过账</div>
								#end
								<!-- 反过账 -->
								#if($!globals.isExistInArray($!AccMainSetting.reverseBinder.split(','),$!LoginBean.id) || $!LoginBean.id=="1")
									<div class="btnlp" id="reverseBinder"><img src="/style1/images/carefor/front.gif" title="反过账"/><br />反过账</div>
									<div><img src="/style1/images/desk/tab.jpg" style="height:40px;width:1px;"/></div>
								#end
							#end
						#end
						<div class="btnlp" id="modules"><img src="/style/plan/add_icon.gif" title="另存凭证模板哟"/><br />模板</div>
						<div class="btnlp" onclick="prints()"><img src="/style/themes/icons/print.png" title="打印"/><br />打印</div>
						<div class="btnlp" id="calculator"><img src="/style/themes/icons/w.bmp" title="计算器"/><br />计算器</div>
						
						<div class="btnlp" id="closes"><img src="/style/images/email_delete.gif" title="关闭"/><br />关闭</div>
						
					</div>
				</td>
		</tr>
</table>
<div style="border-bottom: #ccc 1px dashed;padding-bottom: 1px;clear:both;"></div>
<div class="div1" style="padding:10px 0 10px 0;overflow:hidden;">
<div align="center"><span style="font-size:25px;">记账凭证</span>
<div class="headdiv">
	<table>
		<tr><td>凭证字：</td><td><input name="credTypeId" id="credTypeId" value="$!values.CredTypeID" readonly="readonly"/></td></tr>
		<tr><td>凭证号：</td><td><input type="text" name="OrderNo" id="OrderNo" value="$!values.OrderNo" readonly="readonly"/></td></tr>
		<tr><td>附件数：</td><td><input type="text" name="AcceNum" id="AcceNum" value="$!values.AcceNum" readonly="readonly"/></td></tr>
	</table>
</div><div>&nbsp;</div>
<div style="padding-left:60px;">日期：$!values.BillDate<label style="padding-left:10px;"><span id="span1"></span> 年 第 <span id="span2"></span> 期</label>
#if("$!values.RefBillNo"!="settleAcc" && "$!values.RefBillNo"!="adjustExchange" && "$!values.RefBillNo"!="")
	<label style="padding-left:10px;">关联单据：</label><label style="color: blue;cursor:pointer;" class="bill_Label" onclick="showBillNo('$!values.RefBillID','$!values.RefBillType')">$!values.RefBillNo</label>
#end
</div>
</div></div>
<div id="all_content" style="overflow-x:hidden;overflow-y:auto;width:100%;">
<script type="text/javascript"> 
	var oDiv=document.getElementById("all_content");
	var sHeight=document.documentElement.clientHeight-170;
	oDiv.style.height=sHeight+"px";
</script>
<div style="width:80%;" class="ABK_Add c_main winw1200 f_l mains">
<div style="padding-left: 10px;"></div>
<div class="tablediv"  style="overflow-x:hidden;overflow-y:auto;" id="heart_id">
<script type="text/javascript"> 
	var oDiv=document.getElementById("heart_id");
	var sHeight=document.documentElement.clientHeight-250;
	oDiv.style.height=sHeight+"px";
</script>
<table height="30px" border="1" id="tableItem">
  <tr id="titles" name="titles">
    <td width="3%"><div class="alignDiv">
    No.
    </div></td>
    <td width="20%"><div class="alignDiv">摘要</div></td>
    <td width="25%"><div class="alignDiv">会计科目</div></td>
    #if($!globals.getSysSetting('currency')=="true")
    <td width="5%"><div class="alignDiv">币种</div></td>
    <td width="5%"><div class="alignDiv">汇率</div></td>
    <td width="5%"><div class="alignDiv">外币借</div></td>
    <td width="5%"><div class="alignDiv">外币贷</div></td>
    #end
    <td width="10%"><div class="alignDiv">借方</div></td>
    <td width="10%"><div class="alignDiv">贷方</div></td>
  </tr>
  #set($count = 1)
  #foreach($detailList in $!values.TABLENAME_tblAccDetail)
  <tr id="copyTr" onclick="show_Item(this)">
    <td style="text-align: center;">
    $count<input name="keyIds" id="keyIds" type="hidden" value="itemtr_$count"/>
    </td>
    <td>$detailList.RecordComment</td>
    #set($languageid = $!detailList.get("tblAccTypeInfo.AccFullName"))
    #set($lstr=$values.get("LANGUAGEQUERY").get($languageid).get("$globals.getLocale()"))
    <td>${detailList.AccCode} - $!lstr
    	<input name="tblAccDetail_AccCode" id="AccCode" type="hidden" value="$detailList.AccCode" /></td>
    #set($currencyname = "")
    #if($!globals.getSysSetting('currency')=="true")
    <td>
    	#set($currencyname = $!detailList.get("tblSetExchange.CurrencyName"))
    	$!currencyname
    </td>
    #set($currencyRate = $!globals.formatNumber($!detailList.CurrencyRate,false,false,true,"tblAccDetail","CurrencyRate",true))
    #if($currencyRate==0 || $currencyname=="")
    	#set($currencyRate ="")
    #end
    <td><input name="tblAccDetail_CurrencyRate" id="CurrencyRate" readonly="readonly" value="$!currencyRate"/></td>
    #set($debitCurrencyAmount = $!globals.formatNumber($!detailList.DebitCurrencyAmount,false,false,true,"tblAccDetail","DebitCurrencyAmount",true))
    #if($debitCurrencyAmount==0)
    	#set($debitCurrencyAmount ="")
    #end
    <td><input name="tblAccDetail_DebitCurrencyAmount" id="DebitCurrencyAmount" readonly="readonly" value="$!debitCurrencyAmount" class="moneyposition" onfocus="showBigNum(this)"/></td>
    #set($lendCurrencyAmount = $!globals.formatNumber($!detailList.LendCurrencyAmount,false,false,true,"tblAccDetail","LendCurrencyAmount",true))
    #if($lendCurrencyAmount==0)
    	#set($lendCurrencyAmount ="")
    #end
    <td><input name="tblAccDetail_LendCurrencyAmount" id="LendCurrencyAmount" readonly="readonly" value="$!lendCurrencyAmount" class="moneyposition" onfocus="showBigNum(this)"/></td>
    #end
    #set($debitamount = $!globals.formatNumber($!$detailList.DebitAmount,false,false,true,"tblAccDetail","DebitAmount",true))
    #if($debitamount==0)
    	#set($debitamount = "")
    #end
    <td><input name="tblAccDetail_DebitAmount" id="DebitAmount" readonly="readonly" value="$!debitamount" class="moneyposition" onfocus="showBigNum(this)"/></td>
    #set($lendamount = $!globals.formatNumber($!detailList.LendAmount,false,false,true,"tblAccDetail","LendAmount",true))
    #if($lendamount==0)
    	#set($lendamount = "")
    #end
    <td><input name="tblAccDetail_LendAmount" id="LendAmount" readonly="readonly" value="$!lendamount" class="moneyposition" onfocus="showBigNum(this)"/></td>
  </tr>
  #set($count=$count+1)
  #end
  <tr style="height: 30px;" name="titles">
  	<td></td>
  	<td colspan="2" align="center">合&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;计</td>
  	#if($!globals.getSysSetting('currency')=="true")
  	<td></td>
  	<td></td>
  	<td class="moneyposition" onclick="showBig(this)"><span id="currencydebit"></span></td>
  	<td class="moneyposition" onclick="showBig(this)"><span id="currencylend"></span></td>
  	#end
  	<td class="moneyposition" onclick="showBig(this)"><span id="debit"></span></td>
  	<td class="moneyposition" onclick="showBig(this)"><span id="lend"></span></td>
  </tr>
</table>
</div><div align="center">大写金额：<span id="bigValue"></span></div>
</div>
<div id="computeDiv">
<script type="text/javascript"> 
	var oDiv=document.getElementById("computeDiv");
	var sHeight=document.documentElement.clientHeight-300;
	oDiv.style.height=sHeight+"px";
</script>
	<ul>
		<li>
		#set($count=1)
		#foreach($detailList in $!values.TABLENAME_tblAccDetail)
			<div id='he_itemtr_$count' class='he_div' style='display:none;'><table style="padding: 10px 10px 20px 0px;">
				<script type="text/javascript">
					jQuery.ajax({ type: "POST", url: "/VoucherManage.do?optype=queryComputeItem&queryMode=super&accCode=$detailList.AccCode",async: false,dataType: "json",success: function(data){
						var breed = data.isbreed;
						var IsDept = breed[0].IsDept;
						var IsPersonal = breed[0].IsPersonal;
						var IsCash = breed[0].IsCash;
						var IsClient = breed[0].IsClient;
						var IsProvider = breed[0].IsProvider;
						var isStock = breed[0].isStock;
						var IsCome = breed[0].IsCome;
						var IsProject = breed[0].IsProject;
						var htmls = "";
						if(IsDept==1){
							htmls += "<tr><td>部门：</td><td style='height:25px;'><input type='hidden' name='tblAccDetail_DepartmentCode' id='DepartmentCode' value='$!detailList.DepartmentCode'/><input name='IsDeptName' id='IsDeptName' value='$!detailList.get("tblDepartment.DeptFullName")' ondblclick=\"selectPops(this,'SelectAccDepartment','IsDeptName','DepartmentCode','选择部门');\"/>";
							htmls += "</td></tr>";
						}else{
							htmls += "<tr><td><input type='hidden' name='tblAccDetail_DepartmentCode' id='DepartmentCode' value='$!detailList.DepartmentCode'/></td></tr>";
						}
						if(IsPersonal==1){
							htmls += "<tr><td>职员：</td><td><input type='hidden' name='tblAccDetail_EmployeeID' id='EmployeeIDs' value='$!detailList.EmployeeID'/><input name='IsPersonalName' id='IsPersonalName' value='$!detailList.get("tblEmployee.EmpFullName")' ondblclick=\"selectPops(this,'SelectAccEmployee','IsPersonalName','EmployeeIDs','选择个人');\"/>";
							htmls += "</td></tr>";
						}else{
							htmls += "<tr><td><input type='hidden' name='tblAccDetail_EmployeeID' id='EmployeeIDs' value='$!detailList.EmployeeID'/></td></tr>";
						}
						if(IsProject==1){
							//核算项目
							htmls += "<tr><td>项目：</td><td><input type='hidden' name='tblAccDetail_ProjectCode' id='ProjectCode' value='$!detailList.ProjectCode'/><input name='IsProjectName' id='IsProjectName' value='$!detailList.get("tblProject.ProjectName")' ondblclick=\"selectPops(this,'SelectAccProjectInfo','IsProjectName','ProjectCode','选择项目');\"/>";
							htmls += "</td></tr>";
						}else{
							htmls += "<tr><td><input type='hidden' name='tblAccDetail_ProjectCode' id='ProjectCode' value='$!detailList.ProjectCode'/></td></tr>";
						}
						if(isStock==1){
							htmls += "<tr><td>仓库：</td><td><input type='hidden' name='tblAccDetail_StockCode' id='StockCode' value='$!detailList.StockCode'/><input name='isStockName' id='isStockName' value='$!detailList.get("tblStock.StockFullName")' ondblclick=\"selectPops(this,'SelectAccStocks','isStockName','StockCode','选择仓库');\"/>";
							htmls += "</td></tr>";
						}else{
							htmls += "<tr><td><input type='hidden' name='tblAccDetail_StockCode' id='StockCode' value='$!detailList.StockCode'/></td></tr>";
						}
						if(IsCome==1){
							htmls += "<tr><td>往来单位：</td><td><input type='hidden' name='tblAccDetail_CompanyCode' id='CompanyCode' value='$!detailList.CompanyCode'/><input name='IsComeName' id='IsComeName' value='$!detailList.get("tblCompany.ComFullName")' ondblclick=\"selectPops(this,'SelectAccExpensed','IsComeName','CompanyCode','选择往来单位');\"/>";
							htmls += "</td></tr>";
						}else{
							if(IsClient==1){
								htmls += "<tr><td>客户：</td><td><input type='hidden' name='tblAccDetail_CompanyCode' id='CompanyCode' value='$!detailList.CompanyCode'/><input name='IsClientName' id='IsClientName' value='$!detailList.get("tblCompany.ComFullName")' ondblclick=\"selectPops(this,'SelectAccClient','IsClientName','CompanyCode','选择客户');\"/>";
								htmls += "</td></tr>";
							}else if(IsProvider==1){
								htmls += "<tr><td>供应商：</td><td><input type='hidden' name='tblAccDetail_CompanyCode' id='CompanyCode' value='$!detailList.CompanyCode'/><input name='IsProviderName' id='IsProviderName' value='$!detailList.get("tblCompany.ComFullName")' ondblclick=\"selectPops(this,'SelectAccProvider','IsProviderName','CompanyCode','选择供应商');\"/>";
								htmls += "</td></tr>";
							}else{
								//htmls += "<tr><td><input type='hidden' name='tblAccDetail_CompanyCode' id='CompanyCode' value=''/></td></tr>";
								htmls += "<tr><td>往来单位：</td><td><input type='hidden' name='tblAccDetail_CompanyCode' id='CompanyCode' value='$!detailList.CompanyCode'/><input name='IsComeName' isCome='0' id='IsComeName' value='$!detailList.get("tblCompany.ComFullName")' ondblclick=\"selectPops(this,'SelectAccExpensed','IsComeName','CompanyCode','选择往来单位');\"/>";
								htmls += "</td></tr>";
							}
						}
						
						$("#he_itemtr_$count").find("table").append(htmls);
			    	}
				});
				</script>
				</table></div>
			#set($count = $count+1)
		#end
		</li>
	</ul>
</div>
<div class="affixdiv">
	<div style="width: 50px;float: left">附件：</div><div id="files_preview" style="float: left;">
				#if($!values.Attachment.indexOf(";") > 0)
						#foreach ($str in $globals.strSplit($!values.Attachment,';'))
							<div  id ="$str" style ="height:18px; color:#ff0000;"><img src="/$globals.getStylePath()/images/down.gif"/>&nbsp;&nbsp;
							<a href='/ReadFile?tempFile=path&path=/affix/tblAccMain/&fileName=$!globals.encode($!str)' target="_blank" title="$str">$str</a>
							<input type=hidden name="attachFile" value="$str"/></div>					 
						#end	
			 	 	#end
			</div>
</div>
<div class="ABK_Add c_main winw1200 f_l mains divs" style="height: 30px;padding: 5px 0px 5px 0px;margin-top: 5px;" id="divemp">
	<ul>
	<li>
		<span>部 门：</span>
		<input name="DepartmentCode" id="DepartmentCode" type="hidden" #if("$!values.DepartmentCode"=="") value="$globals.getLoginBean().departCode" #else value="$!values.DepartmentCode" #end/>
		<input name="DepartmentName" id="DepartmentName" #if("$!values.DepartmentCode"=="") value="$globals.getLoginBean().departmentName" #else value="$deptMap.get("$!values.DepartmentCode")" #end/>
	</li>
	<li>
		<span>经手人：</span>
		<input name="EmployeeID" id="EmployeeID" #if("$!values.EmployeeID"=="") value="$globals.getLoginBean().id" #else value="$!values.EmployeeID" #end type="hidden"/>
		<input name="EmployeeName" id="EmployeeName" #if("$!values.EmployeeID"=="") value="$globals.getLoginBean().empFullName" #else value="$!globals.getEmpNameById($!values.EmployeeID)" #end/>
	</li>
	<li>
		<span>结算方式：</span>
		<input name="AcceptMode" id="AcceptMode" value="$!values.AcceptMode"/>
	</li>
	<li>
		<span>结算号：</span>
		<input name="AcceptNumber" id="AcceptNumber" value="$!values.AcceptNumber"/>
	</li>
	</ul>
</div>
<div class="ABK_Add c_main winw1200 f_l mains divs" style="margin:5px 0 0 10px;" id="divs">
	<ul style="overflow:hidden;padding:10px 0 0 10px;">
	<li><span>核 准：</span>#if($globals.getEmpNameById($!values.approver)=="Other") #else $globals.getEmpNameById($!values.approver)#end</li>
	<li><span>复 核：</span>#if($globals.getEmpNameById($!values.ReviewUser)=="Other") #else $globals.getEmpNameById($!values.ReviewUser)#end</li>
	<li><span>过 账：</span>#if($globals.getEmpNameById($!values.PostingUser)=="Other") #else $globals.getEmpNameById($!values.PostingUser)#end</li>
	<li><span>制 单：</span>$globals.getEmpNameById($!values.createBy)</li>
	</ul>
</div>
<div style="margin: 10px 0px 20px 15px;float:left;width:80%" id="remarkdiv" >
	<input name="approveRemark" id="approveRemark" type="hidden" value="$!values.approveRemark" />
	<table><tr><td>批注：</td><td>$!values.approveRemark</td></table>
</div>
</div>
</form>
</body>
</html>