<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("muduleFlow.lb.voucherlogin")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" />
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/Voucher.css"/>
<link type="text/css" rel="stylesheet" href="/style1/css/listgrid.css" />
<script type="text/javascript" src="$globals.js("/js/formvalidate.vjs","",$text)"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery-ui.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script type="text/javascript" src="/js/aioaccmain.js?a=20151030145501"></script>
<script type="text/javascript" src="$globals.js("/js/dropdownselect.js","",$text)"></script>
<script type="text/javascript" src="/js/aioaccCashFlow.js"></script>

<script type="text/javascript">
	
	//现金流量主附表项目全局变量
	var global_cf={};
	global_cf.mItems = $!mItems;
	global_cf.sItems = $!sItems;
	
	global_row = $!values.TABLENAME_tblAccDetail.size()+1;
	
	var currencysettings = '$!globals.getSysSetting('currency')';
	var lens = "$!globals.getSysSetting('DigitsAmount')";
	var version = $!globals.getVersion();
	var moduleId = "$LoginBean.getOperationMap().get("/VoucherManageAction.do").moduleId";
	var newYearPeriod;
	var newPeriod;
	$(document).ready(function() {
		
		$("input[id!=OrderNo][id!=RecordComment]").attr("readonly",true);
		
		//初始化流量弹出窗
		jQuery.cf_initPop();
		jQuery.cf_initEvent();
		
		//流量
		$("#cashflow").bind("click",function(){
			jQuery.cf_setCashFlow();
		});
		
		//添加
		 $("#addrow").bind("click", function(){
		 	addRow('update');
		 });
		 
		 $("#add").bind("click", function(){
			window.location.href="/VoucherManage.do?operation=6&isEspecial=list&popWinName=$!popWinName";
		 });
		 
		 $("#tableItem tr").bind("mouseover",function(){
		 	$(this).css("background","#f9f9f9");
		 });
		 $("#tableItem tr").bind("mouseout",function(){
		 	$(this).css("background","#fff");
		 });
		 //表单提交
		 $("#save").bind("click",function(){
		 	checkForm('');
		 });
		 //返回
		 $("#backs").bind("click", function(){
		 	#if("$!NoButton"=="true")
		 		window.location.href="/VoucherManageAction.do?operation=5&tableName=tblAccMain&id=$!values.id&NoButton=$!NoButton";
		 	#else
			 	window.location.href="/VoucherManageAction.do?operation=4";
		 	#end
		 });
		 var billdate = $("#BillDate").val();
		 newYearPeriod = billdate.substring(0,billdate.indexOf("-"));
		 newPeriod = new Number(billdate.substring(billdate.indexOf("-")+1,billdate.indexOf("-")+3));
		 $("#span1").html(newYearPeriod);
		 $("#span2").text(newPeriod);
		 //关闭
		 $("#closes").bind("click", function(){
			 if("$!popWinName"!=""){
					window.parent.jQuery.close('$!popWinName');
				}else{
					window.parent.jQuery.close('PopQuerydiv');
				}
				closeWin();
		 });
		 //刷新
		 $("#refur").bind("click",function(){
			window.location.reload();
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
		//反复核  
		$("#reversecheck").bind("click",function(){
			dealcheck('reverseCheck');
		});
		//计算器  
		$("#calculator").bind("click",function(){
		 	Run('calc');
		 });
		//另存模板
		$("#modules").bind("click",function(){
			saveModule("$!values.id");
		 });
		//控制操作
		//controlHandle();
		//统计金额值  
		account();
		dateChange();
		$(".btnlp").attr("align","center");
		document.onclick = function(e){
		    var event = window.event || e;
		    var ele = event.srcElement || event.target;
			if(ele.id!="helper"){
				hidediv();
			}
		}
	});
	/* 控制操作*/
	function controlHandle(){
		var isAudits = "$!AccMainSetting.isAuditing";
		var AutoBillMarker = "$!values.AutoBillMarker";
		if(isAudits == "1" || AutoBillMarker == "1"){
			//启用标准财务
			//审核
			var auditing = "$!values.isAuditing";
			if(auditing == "finish" || AutoBillMarker == "1"){
				$("input").not("#AcceptMode").not("#AcceptNumber").attr("readonly","readonly");
				$("textarea ").attr("readonly","readonly");
			}
			//复核
			var check = "$!values.isReview";
			var wFlowName = "$!values.workFlowNodeName";
			var workFlowNode = "$!values.workFlowNode";
			if(check == "2" || wFlowName=="finish" || AutoBillMarker == "1"){
				//已复核  
				$("#AcceptMode").attr("readonly","readonly");
				$("#AcceptNumber").attr("readonly","readonly");
			}
			//已过账  
			if(wFlowName == "finish" || workFlowNode=="-1"){
				$("input").attr("readonly","readonly");
				$("#Remark").attr("readonly","readonly");
			}
			if(wFlowName == "finish" || auditing == "finish" || AutoBillMarker == "1" || workFlowNode=="-1"){
				$("#all_content").find("img").removeAttr("onClick");
				$("select").attr("disabled","disabled");
				$("input").removeAttr("onClick");
				$("input").removeAttr("ondblclick");
				$("#affix").attr("href","javascript:void(0)");
				$("#affix").html("");
			}
		}else{
			//不启用标准财务可以修改(生成凭证无法删除)
		}
	}
	/* 合计显示大写 */
	function showBig(obj){
		var objhtml = $(obj).find("span").html();
		if(objhtml!=null && objhtml!="" && objhtml!="&nbsp;"){
			var changeValue = AmountInWords(objhtml,null);
			$("#bigValue").html(changeValue);
		}
	}
	/* 提交*/
	function checkForm(types){
		$("#optype").val(types);
		var isAudits = "$!AccMainSetting.isAuditing";
		var AutoBillMarker = "$!values.AutoBillMarker";
		var refbilltype="$!values.RefBillType";
		if(types != "addModule"){
			if(isAudits == "1"){
				//审核
				var auditingName = "$!values.isAuditing";
				//复核
				var check = "$!values.isReview";
				//过账
				var wFlowNodeName = "$!values.workFlowNodeName";
				var workFlowNode = "$!values.workFlowNode";
				if(auditingName == "finish"){
					//已审核 
					alert("凭证已审核无法进行修改！");
					return false;
				}
				if(wFlowNodeName == "finish" || workFlowNode == "-1"){
					//已过账

					alert("凭证已过账无法进行修改！");
					return false;
				}
				if(refbilltype== "ProfitLossCarryForward"){
					//结转损益
					alert("该凭证是由结转损益生成过来的，无法进行修改！");
					return false;
				}
			}else{
				if(AutoBillMarker == "1"){
					//自动生成的凭证无法修改

                    if("$!values.RefBillNo"=="settleAcc"){
                        alert("该凭证是由单据生成过来的,无法进行修改！");
                    }else{                    
					    alert("该凭证是由单据$!values.RefBillNo生成过来的,无法进行修改！");
					}
					return false;
				}
			}
			
			var credTypeID = $("#CredTypeID").val();
			var BillDate = $("#BillDate").val();
			var minPeriodBegin = "$!globals.get($!periodStr,4)";
			var maxPeriodEnd = "$!globals.get($!periodStr,5)";
			if(credTypeID == ""){
				$("#CredTypeID").focus();
				tips($("#CredTypeID"),'请选择凭证字!');
				return false;
			}
			if(BillDate == ""){
				$("#BillDate").focus();
				tips($("#BillDate"),'请选择凭证日期!');
				return false;
			}
			
			/* 验证凭证日期是否在会计日期中*/
			var periodyear = "$!globals.get($accPeriod,0)";
			var periodperiod = new Number("$!globals.get($accPeriod,1)");
			if(periodperiod<10){
				periodperiod = "0"+periodperiod;
			}
			if(periodyear == "-1"){
				alert("未开账！");
				return false;
			}
			var CredYear = "$!values.CredYear";
			var Period = "$!values.Period";
			if(Period<10){
				Period="0"+Period
			}
			var times1 = CredYear+"-"+Period;
			var times2 = periodyear+"-"+periodperiod;

			if(times1<times2){
				alert("财务期间之前的凭证无法进行修改！");
				return false;
			}
			if(BillDate<minPeriodBegin || BillDate>maxPeriodEnd){
				asyncbox.tips('日期必须在'+minPeriodBegin+'/'+maxPeriodEnd+'之间','提示',1500);
				return false;
			}
			var hasError = false ;
			$("#tableItem tr:visible[name!=titles]").each(function(i,n){
				var accCode = $(n).find("#AccCode");
				var debitAmount = $(n).find("#DebitAmount");
				var lendAmount = $(n).find("#LendAmount");
				if(accCode.val()==""){ //本行没有科目，则删除				
					delimg($(n).find(".img1"),'keyId');
				}else{ //检查这一行数据

					var debit = $(n).find("input[id=DebitAmount]").val();
					var lend = $(n).find("input[id=LendAmount]").val();
					if(debit=="" && lend==""){
						$(n).find("input[id=DebitAmount]").focus();
						tips($(n).find("input[id=DebitAmount]"),'请录入金额!');
						hasError = true;
						return false;
					}
					if(debit!="" && lend!=""){
						$(n).find("input[id=DebitAmount]").focus();
						tips($(n).find("input[id=DebitAmount]"),'借方金额和贷方金额不能同时存在!');
						hasError = true;
						return false;
					}
					#if($!globals.getSysSetting('currency')=="true")
						var debitCurrency = $(n).find("input[id=DebitCurrencyAmount]").val();
						var lendCurrency = $(n).find("input[id=LendCurrencyAmount]").val();
						if((debitCurrency!="" && lendCurrency!="") && (debitCurrency!="0" && lendCurrency!="0")){
							$(n).find("input[id=DebitCurrencyAmount]").focus();
							tips($(n).find("input[id=DebitCurrencyAmount]"),'外币借金额和外币贷金额不能同时存在!');
							hasError = true;
							return false;
						}
					#end
					//核算项未输入
					var ComputeItem = $(n).find("#ComputeItem").val();
					if(ComputeItem != ""){
						var breedObj = new Object();
						var breed = ComputeItem.split(";");
						for(var i = 0;i<breed.length;i++){
							if(breed[i] != ""){
								var k = breed[i].split(":");
								breedObj[k[0]] = k[1];
							}
						}
			
						if((breedObj.IsDept==1 && $(n).find("input[id=DepartmentCode]").val()=="") ||
						   (breedObj.IsPersonal==1 && $(n).find("input[id=EmployeeIDs]").val()=="") ||
						   (breedObj.IsCome==1 && $(n).find("input[id=CompanyCode]").val()=="") ||
						   (breedObj.IsClient==1 && $(n).find("input[id=CompanyCode]").val()=="") ||
						   (breedObj.IsProvider==1 && $(n).find("input[id=CompanyCode]").val()=="") ||
						   (breedObj.IsProject==1 && $(n).find("input[id=ProjectCode]").val()=="") ||
						   (breedObj.isStock==1 && $(n).find("input[id=StockCode]").val()=="") ){
						    hasError = true;
						    $(n).find("#AccCodeName").focus();
							tips($(n).find("#AccCodeName"),'请输入核算项!');
							//$(n).find("input[id=DebitCurrencyAmount]").focus();
							//tips($(n).find("input[id=DebitCurrencyAmount]"),'请输入核算项!');
							return false;
						}
				   	}
				}
			});
			if(hasError){
				return false ;
			}
		}else{
			$("#operation").val("$globals.getOP("OP_ADD")");
		}
		$("#Attachment").val($("#attachFiles").val());
		if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
		
		form.submit();
	}
	var reportNumber2;
	var billId2;
	/* 打印*/
	function prints(billId,reportNumber){
		reportNumber2=reportNumber;
		billId2=billId
		printControl("/UserFunctionQueryAction.do?tableName=tblAccMain&reportNumber="+reportNumber+"&moduleType=&operation=$globals.getOP("OP_PRINT")&BillID="+billId+"&parentTableName=$!parentTableName&winCurIndex=");
		enableForm(form);
		form.button.value = "";
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
		window.location.href="/VoucherManageAction.do?operation=7&id="+id+"&number="+number+"&popWinName=$!popWinName";
	} 
	/* 审核不通过*/
	function noPass(){
		var url = '/VoucherManage.do?optype=approveRemarkPre&isList=update&voucherId=$!values.id';
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
	//回调函数刷新凭证模板
	function asyRefure(){
		jQuery.opener('moduleDiv').refure();
	}
	/* 审核通过与反审核*/
	function dealAuditing(dealType){
		form.optype.value="dealapprov";
		form.dealType.value=dealType;
		form.operation.value="";
		form.keyId.value="$!values.id";
		form.isList.value="update";
		form.submit();
	}
	/* 过账与反过账*/
	function dealbinder(dealType){
		form.optype.value="dealbinder";
		form.dealType.value=dealType;
		form.operation.value="";
		form.keyId.value="$!values.id";
		form.isList.value="update";
		form.submit();
	}
	/* 复核与反复核*/
	function dealcheck(dealType){
		form.optype.value="dealcheck";
		form.dealType.value=dealType;
		form.operation.value="";
		form.keyId.value="$!values.id";
		form.isList.value="update";
		form.submit();
	}
	
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body style="background: #f5f5f5;">
<form id="form" name="form" method="post" action="/VoucherManage.do" target="formFrame">
<input type="hidden" name="operation" id="operation" value="$globals.getOP("OP_UPDATE")"/>
<input type="hidden" name="id" id="id" value="$!values.id"/>
<input type="hidden" name="CredNo" id="CredNo" value="$!values.CredNo"/>
<input type="hidden" name="oldOrderNo" id="oldOrderNo" value="$!values.OrderNo"/>
<input type="hidden" name="oldCredTypeID" id="oldCredTypeID" value="$!values.CredTypeID"/>
<input type="hidden" name="delFiles" id="delFiles" value=""/>
<input type="hidden" name="tableName" id="tableName" value="tblAccMain" />
<input type="hidden" name="Attachment" id="Attachment" value="$!values.Attachment" />
<input type="hidden" name="attachFiles" id="attachFiles" value="$!values.Attachment" />
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
<input type="hidden" id="workFlowNode" name="workFlowNode" value="$!values.workFlowNode"/>
<input type="hidden" id="SCompanyID" name="SCompanyID" value="$!values.SCompanyID"/>
<input type="hidden" name="isAuditing" value="$!values.isAuditing" />
<input type="hidden" name="isReview" value="$!values.isReview"/>
<input type="hidden" name="AutoBillMarker" id="AutoBillMarker" value="$!values.AutoBillMarker"/>
<input type="hidden" id="ModuleName" name="ModuleName" value="" />
<input type="hidden" name="optype" id="optype" value=""/>
<input type="hidden" name="dealType" id="dealType" value=""/>
<input type="hidden" name="keyId" id="keyId" value=""/>
<input type="hidden" name="isList" id="isList" value=""/>
<input type="hidden" name="popWinName" id="popWinName" value="$!popWinName"/>
<input type="hidden" class="cf_flag" name="CashFlag"  value="$!values.CashFlag"/>

#if("$!AccMainSetting.isAuditing"=="1")
	#if($!values.workFlowNodeName=="finish" || $!values.workFlowNode=="-1")
		<!-- 已过账图片 -->
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
						#if("$!isEspecial" != "1")
						<div id="cashflowFlag" class="cashflowFlag" style="color:red;">
						#if($!values.CashFlag =="1")
							已指定
						#end
						</div>
						<div class="btnlp" id="cashflow"><img src="/style/themes/icons/reload.png" title="流量"/><br />流量</div>
						<div class="btnlp" id="save"><img src="/style/themes/icons/filesave.png" title="$text.get('mrp.lb.save')"/><br />$text.get('mrp.lb.save')</div>
						#end
						#if($LoginBean.operationMap.get("/VoucherManageAction.do").add() || $!LoginBean.id=="1")
							<div class="btnlp" id="add" align="center"><img src="/style/plan/M_3.gif" title="添加凭证"/><br />添加</div>
						#end
						<div class="btnlp" id="refur"><img src="/style/themes/icons/reload.png" title="$text.get('oa.common.refresh')"/><br />$text.get('oa.common.refresh')</div>
						<div><img src="/style1/images/desk/tab.jpg" style="height:40px;width:1px;"/></div>
						<!-- <div class="btnlp"><img src="/style/themes/icons/filesave.png" title="预览"/><br />预览</div> -->
						#if("$!isEspecial" != "1")
						#if("$!NoButton"!="true")
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
						<!-- <div class="btnlp" id="addrow"><img src="/style/themes/icons/edit_add.png" title="插入分录"/><br />插入</div>
						<div class="btnlp" id="delrow"><img src="/style/themes/icons/edit_remove.png" title="删除分录"/><br />删除</div> -->
							<!-- 启用审核 -->
							#if("$!AccMainSetting.isAuditing"=="1")
								<!-- 审核权限 -->
								#if($!globals.isExistInArray($!AccMainSetting.auditingPersont.split(','),$!LoginBean.id) || $!LoginBean.id=="1")
								<div class="btnlp" id="yesPass"><img src="/style1/images/oaimages/enable.png" title="凭证审核通过"/><br />审核通过</div>
								<div class="btnlp" id="noPass"><img src="/style1/images/oaimages/noenable.png" title="凭证审核不通过"/><br />审核不通过</div>
								#end
								<!-- 反审核权限 -->
								#if($!globals.isExistInArray($!AccMainSetting.reverseAuditing.split(','),$!LoginBean.id) || $!LoginBean.id=="1")
								<div class="btnlp" id="reversePass"><img src="/style1/images/refresh.gif" title="凭证反审核"/><br />反审核</div>
								<div><img src="/style1/images/desk/tab.jpg" style="height:40px;width:1px;"/></div>
								#end
							#end
							<!-- 复核/反复核 -->
							#if("$!AccMainSetting.isCheck"=="1")
								#if($!globals.isExistInArray($!AccMainSetting.checkPersont.split(','),$!LoginBean.id) || $!LoginBean.id=="1")
								<div class="btnlp" id="check"><img src="/style/themes/icons/redo.png" title="凭证复核"/><br />复核</div>
								<div class="btnlp" id="reversecheck"><img src="/style/themes/icons/undo.png" title="凭证反复核"/><br />反复核</div>
								<div><img src="/style1/images/desk/tab.jpg" style="height:40px;width:1px;"/></div>
								#end
							#end
							#if("$!AccMainSetting.isAuditing"=="1")
								<!-- 过账权限 -->
								#if($!globals.isExistInArray($!AccMainSetting.binderPersont.split(','),$!LoginBean.id) || $!LoginBean.id=="1")
								<div class="btnlp" id="binder"><img src="/style1/images/carefor/up.gif" title="凭证过账"/><br />过账</div>
								#end
								<!-- 反过账 -->
								#if($!globals.isExistInArray($!AccMainSetting.reverseBinder.split(','),$!LoginBean.id) || $!LoginBean.id=="1")
								<div class="btnlp" id="reverseBinder"><img src="/style1/images/carefor/front.gif" title="凭证反过账"/><br />反过账</div>
								<div><img src="/style1/images/desk/tab.jpg" style="height:40px;width:1px;"/></div>
								#end
							#end
						#end
						<!-- <div class="btnlp"><img src="/style/themes/icons/filesave.png" title="流量"/><br />流量</div> -->
						<div class="btnlp" id="modules"><img src="/style/plan/add_icon.gif" title="另存为凭证模板哟"/><br />模板</div>
						#if($LoginBean.operationMap.get("/VoucherManageAction.do").print() || $!LoginBean.id=="1")
						<div class="btnlp" onclick="prints()"><img src="/style/themes/icons/print.png" title="打印"/><br />打印</div>
						#end
						<div class="btnlp small" id="calculator"><img src="/style/themes/icons/w.bmp" title="计算器"/><br />计算器</div>
						
						<div class="btnlp" id="closes"><img src="/style/images/email_delete.gif" title="关闭"/><br />关闭</div>
					</div>
				</td>
		</tr>
</table>
<div style="border-bottom: #ccc 1px dashed;padding-bottom: 1px;clear:both;"></div>
<div class="div1" style="padding-top: 10px;">
<div align="center"><span style="font-size:25px;">记账凭证</span>
<div class="headdiv">
	<table>
		<tr><td>凭证字：</td><td><select name="CredTypeID" id="CredTypeID">
		#foreach($erow in $globals.getEnumerationItems('CredTypeID'))
			<option title="$erow.name" value="$erow.value" #if("$!values.CredTypeID"=="$erow.value") selected #end>$erow.name</option>
		#end
	</select></td></tr>
		<tr><td>凭证号：</td><td><input type="text"  name="OrderNo" id="OrderNo" value="$!values.OrderNo" /></td></tr>
		<tr><td>附件数：</td><td><input type="text" name="AcceNum" id="AcceNum" value="$!values.AcceNum"/></td></tr>
	</table>
</div><div>&nbsp;</div>
<div style="padding-left:60px;">日期：<input name="BillDate" id="BillDate" value="$!values.BillDate"  style="width: 80px;" onChange="dateChange()"/><label style="padding-left:10px;"><span id="span1"></span> 年 第 <span id="span2"></span> 期</label>
#if("$!values.RefBillNo"!="settleAcc" && "$!values.RefBillNo"!="adjustExchange" && "$!values.RefBillNo"!="")
	<label style="padding-left:10px;">关联单据：</label><label style="color: blue;cursor:pointer;" class="bill_Label" onclick="showBillNo('$!values.RefBillID','$!values.RefBillType')">$!values.RefBillNo</label>
#end
</div>
</div></div>

<div id="all_content" style="overflow-x:hidden;overflow-y:auto;margin-top: 10px;width:100%;">
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
   -
    <td width="20%"><div class="alignDiv">摘要...</div></td>
    <td width="25%"><div class="alignDiv">会计科目...</div></td>
    #if($!globals.getSysSetting('currency')=="true")
    <td width="5%"><div class="alignDiv">币种</div></td>
    <td width="5%"><div class="alignDiv">汇率</div></td>
    <td width="5%"><div class="alignDiv">外币借</div></td>
    <td width="5%"><div class="alignDiv">外币贷</div></td>
    #end
    <td width="8%"><div class="alignDiv">借方</div></td>
    <td width="8%"><div class="alignDiv">贷方</div></td>
  </tr>
  #set($count = 1)
  #foreach($detailList in $!values.TABLENAME_tblAccDetail)
  <tr id="copyTr" class="copyTr" row_id="$count">
    <td style="text-align: center;">
	<input type='hidden' class='RowNum' name='tblAccDetail_RowNum' value='$count'/>
	<input type='hidden' class='MainItem' name='tblAccDetail_MainItem' value ='$!detailList.MainItem' />
	<input type='hidden' class='SecItem' name='tblAccDetail_SecItem' value ='$!detailList.SecItem' />
	<input type='hidden' class='Refs' name='tblAccDetail_Refs' value ='$!detailList.Refs' />
    <input type="hidden" id="AccCodeOld" value="" />
    <div id="hiddenData">    
    <input type="hidden" id="ComputeItem" value="" />
    <input type='hidden' name='tblAccDetail_DepartmentCode' id='DepartmentCode' value='$!detailList.DepartmentCode'/>
    <input type='hidden' name='DeptName' id='DeptName' value='$!detailList.get("tblDepartment.DeptFullName")' />
    <input type='hidden' name='tblAccDetail_EmployeeID' id='EmployeeIDs' value='$!detailList.EmployeeID'/>
    <input type='hidden' name='PersonalName' id='PersonalName' value='$!detailList.get("tblEmployee.EmpFullName")'/>
    <input type='hidden' name='tblAccDetail_CompanyCode' id='CompanyCode' value='$!detailList.CompanyCode'/>
    <input type='hidden' name='ComFullName' id='ComFullName' value='$!detailList.get("tblCompany.ComFullName")'/>
    <input type='hidden' name='tblAccDetail_ProjectCode' id='ProjectCode' value='$!detailList.ProjectCode'/>
    <input type='hidden' name='ProjectName' id='ProjectName' value='$!detailList.get("tblProject.ProjectName")' />
    <input type='hidden' name='tblAccDetail_StockCode' id='StockCode' value='$!detailList.StockCode'/>
    <input type='hidden' name='StockName' id='StockName' value='$!detailList.get("tblStock.StockFullName")' />
    </div>
    -
    </td>
    <td><input name="tblAccDetail_RecordComment" id="RecordComment"  ondblclick="chooserecord(this)" value="$detailList.RecordComment" onkeyup="init(this)"  onkeydown="recommendDown(this)"  disableautocomplete="true" autocomplete="off"></td>
    #set($languageid = $!detailList.get("tblAccTypeInfo.AccFullName"))
    #set($lstr=$values.get("LANGUAGEQUERY").get($languageid).get("$globals.getLocale()"))
    <td><input name="AccCodeName" id="AccCodeName" style="width: 100%" value="${detailList.AccCode} - $!lstr"
     disableautocomplete="true" autocomplete="off"/>
    	<input name="tblAccDetail_AccCode" id="AccCode" type="hidden" value="$detailList.AccCode" /></td>
    #if($!globals.getSysSetting('currency')=="true")
    <td><select name="tblAccDetail_Currency" id="Currency" style="width: 100%" onfocus="focusRow(this)"  onchange="selectrate(this)">
    	<script type="text/javascript">
    		querycurrency('itemtr_$count','$detailList.AccCode','$!detailList.Currency','update');
    	</script>
    </select></td>
    #set($currencyRate = $!globals.formatNumber($!detailList.CurrencyRate,false,false,true,"tblAccDetail","CurrencyRate",true))
    #if($currencyRate==0 || "$!detailList.Currency"=="")
    	#set($currencyRate ="")
    #end
    <td><input name="tblAccDetail_CurrencyRate" id="CurrencyRate" value="$!currencyRate" onfocus="focusRow(this)"  onblur="updateExchange(this)" disableautocomplete="true" autocomplete="off"/></td>
    #set($debitCurrencyAmount = $!globals.formatNumber($!detailList.DebitCurrencyAmount,false,false,true,"tblAccDetail","DebitCurrencyAmount",true))
    #if($debitCurrencyAmount==0)
    	#set($debitCurrencyAmount = "")
    #end
    <td><input name="tblAccDetail_DebitCurrencyAmount" id="DebitCurrencyAmount"  onblur="sumMoney(this)" value="$!debitCurrencyAmount" class="moneyposition" onkeyup="showBigNum(this)" onfocus="showBigNum(this);" disableautocomplete="true" autocomplete="off"/></td>
    #set($lendCurrencyAmount = $!globals.formatNumber($!detailList.LendCurrencyAmount,false,false,true,"tblAccDetail","LendCurrencyAmount",true))
    #if($lendCurrencyAmount==0)
    	#set($lendCurrencyAmount ="")
    #end
    <td><input name="tblAccDetail_LendCurrencyAmount" id="LendCurrencyAmount"  onblur="sumMoney(this)" value="$!lendCurrencyAmount" class="moneyposition" onkeyup="showBigNum(this)" onfocus="showBigNum(this);" disableautocomplete="true" autocomplete="off"/></td>
    #end
    #set($debitamount = $!globals.formatNumber($!$detailList.DebitAmount,false,false,true,"tblAccDetail","DebitAmount",true))
    #if($debitamount==0)
    	#set($debitamount = "")
    #end
    <td><input name="tblAccDetail_DebitAmount" id="DebitAmount" onblur="sumMoney(this)" value="$!debitamount" class="moneyposition" onkeyup="showBigNum(this)" onfocus="showBigNum(this);" disableautocomplete="true" autocomplete="off"/></td>
    #set($lendamount = $!globals.formatNumber($!detailList.LendAmount,false,false,true,"tblAccDetail","LendAmount",true))
    #if($lendamount==0)
    	#set($lendamount = "")
    #end
    <td><input name="tblAccDetail_LendAmount" id="LendAmount" onblur="sumMoney(this)" value="$!lendamount" class="moneyposition" onkeyup="showBigNum(this)" onfocus="showBigNum(this);" disableautocomplete="true" autocomplete="off"/></td>
  </tr>
  #set($count=$count+1)
  #end

  <tr style="height: 30px;" name="titles">
  	<td></td>
  	<td colspan="2" align="center">合&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;计</td>
  	#if($!globals.getSysSetting('currency')=="true")
  	<td></td>
  	<td></td>
  	<td class="moneyposition" onclick="showBig(this)"><span id="currencydebit" class="moneyposition"></span></td>
  	<td class="moneyposition" onclick="showBig(this)"><span id="currencylend" class="moneyposition"></span></td>
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
			<div id='he_item' class='he_div'>
				<table style="padding: 10px 10px 20px 0px;">
				</table>
			</div>
		</li>
	</ul>
</div>
<div class="affixdiv">
	附件：<a href="javascript:openAttachUpload('/affix/tblAccMain/')" id="affix">&nbsp;<span></span>
			<img src="/style1/images/oaimages/uploading.gif" /><font style="margin-left: 2px;">附件上传</font></a>
			<div id="files_preview" style="padding-left: 40px;">
				#if($!values.Attachment.indexOf(";") > 0)
						#foreach ($str in $globals.strSplit($!values.Attachment,';'))
							<div id ="$str" style ="height:18px; color:#ff0000;">
							<a href="javascript:;" onclick="removeFile('$str');"><img src="/style/images/del.gif"></a>&nbsp;&nbsp;
							$str<input type=hidden name="attachFile" value="$str"/></div>					 
						#end	
			 	 	#end
			</div>
</div>
<div class="ABK_Add c_main winw1200 f_l mains divs" style="width:100%;height: 30px;padding: 5px 0px 5px 0px;margin-top: 5px;" id="divemp">
	<ul>
	<li>
		<span>部&nbsp;&nbsp;门：</span>
		<input name="DepartmentCode" id="mDepartmentCode" type="hidden" #if("$!values.DepartmentCode"=="") value="$globals.getLoginBean().departCode" #else value="$!values.DepartmentCode" #end/>
		<input name="DepartmentName" id="DepartmentName" #if("$!values.DepartmentCode"=="") value="$globals.getLoginBean().departmentName" #else value="$deptMap.get("$!values.DepartmentCode")" #end  />
		
	</li>
	<li>
		<span>经手人：</span>
		<input name="EmployeeID" id="EmployeeID" #if("$!values.EmployeeID"=="") value="$globals.getLoginBean().id" #else value="$!values.EmployeeID" #end type="hidden"/>
		<input name="EmployeeName" id="EmployeeName" #if("$!values.EmployeeID"=="") value="$globals.getLoginBean().empFullName" #else value="$!globals.getEmpNameById($!values.EmployeeID)" #end  />		
	</li>
	<li><span>结算方式：</span><input name="AcceptMode" id="AcceptMode" value="$!values.AcceptMode"/></li>
	<li><span>结算号：</span><input name="AcceptNumber" id="AcceptNumber" value="$!values.AcceptNumber"/></li>
	
	</ul>
</div>
<div class="ABK_Add c_main winw1200 f_l mains divs" style="width:100%;height:30px;padding: 5px 0px 5px 0px;" id="divs">
	<ul>
	<li><span>核&nbsp;&nbsp;准：</span><span>#if($globals.getEmpNameById($!values.approver)=="Other")&nbsp;#else$globals.getEmpNameById($!values.approver)#end</span></li>
	<li><span>复&nbsp;&nbsp;核：</span><span>#if($globals.getEmpNameById($!values.ReviewUser)=="Other")&nbsp;#else$globals.getEmpNameById($!values.ReviewUser)#end</span></li>
	<li><span>过&nbsp;&nbsp;账：</span><span>#if($globals.getEmpNameById($!values.PostingUser)=="Other")&nbsp;#else$globals.getEmpNameById($!values.PostingUser)#end</span></li>
	<li><span>制&nbsp;&nbsp;单：</span><span>$globals.getEmpNameById($!values.createBy)</span></li>
	</ul>
</div>
<div style="margin: 10px 0px 20px 15px;float:left;width:80%" id="remarkdiv" >
	<input name="approveRemark" id="approveRemark" type="hidden" value="$!values.approveRemark" />
	<table><tr><td>批注：</td><td>$!values.approveRemark</td></table>
</div>
<div id="helper" style="position: absolute; font-size: 12px;border:1px solid #C0C0C0;width:200px;display:none;background-color: white;">
 <div id="searchs" style="overflow-x:hidden;width:100%;overflow-y:auto;">
 </div>
 <div>
 	<span style="color:blue;cursor: pointer;padding-left: 5px;" onclick="saveModuls()"><img src="/style1/images/oaimages/22.gif" title="保存为摘要模板"/></span>
 </div>
</div>
<div id="tips" style="position:absolute;border:1px solid #ccc;padding:0px 3px;color:#f00;display:none;height:20px;line-height:20px;background:#fcfcfc"></div>
</form>
</body>
</html>