
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("muduleFlow.lb.voucherlogin")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
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
	#if("$!mItems"=="")
		#set($mItems = "{}")
 	#end
 	#if("$!sItems"=="")
		#set($sItems = "{}")
 	#end
	global_cf.mItems = $!mItems;
	global_cf.sItems = $!sItems;
	
	global_row = 6;
	
	var currencysettings = '$!globals.getSysSetting('currency')';
	var startTime = "$!startTime";
	var lens = "$!globals.getSysSetting('DigitsAmount')";
	var version = $!globals.getVersion();
	var moduleId = "$LoginBean.getOperationMap().get("/VoucherManageAction.do").moduleId";
	var newYearPeriod;
	var newPeriod;
	$(document).ready(function() {
		//初始化流量弹出窗
		jQuery.cf_initPop();
		jQuery.cf_initEvent();
		
		//流量
		$("#cashflow").bind("click",function(){
			jQuery.cf_setCashFlow();
		});
		//添加
		 $("#addrow").bind("click", function(){
		 	addRow('add');
		 });
		 $("#tableItem tr").bind("mouseover",function(){
		 	$(this).css("background","#f9f9f9");
		 });
		 $("#tableItem tr").bind("mouseout",function(){
		 	$(this).css("background","#fff");
		 });
		 $("#save").bind("click",function(){
		 	checkForm('');
		 });
		 $("#saveoradd").bind("click",function(){
		 	checkForm('saveOrAdd');
		 });
		 $("#backs").bind("click", function(){
		 	window.location.href="/VoucherManageAction.do?operation=4";
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
		 $("#refur").bind("click",function(){
			window.location.reload();
		 });
		 $("#modules").bind("click",function(){
		 	module();
		 });
		 //计算器



		var billdate = $("#BillDate").val();
		if(billdate==""){
			$("#BillDate").val(startTime);
		}
		billdate = $("#BillDate").val();
		newYearPeriod = billdate.substring(0,billdate.indexOf("-"));
		newPeriod = new Number(billdate.substring(billdate.indexOf("-")+1,billdate.indexOf("-")+3));
		$("#span1").html(newYearPeriod);
		$("#span2").text(newPeriod);
		
		 $("#calculator").bind("click",function(){
		 	Run('calc');
		 });
		 account();
		 $(".btnlp").attr("align","center");
		 
		 document.onclick = function(e){
		    var event = window.event || e;
		    var ele = event.srcElement || event.target;
			if(ele.id!="helper"){
				hidediv();
			}
		  }
	});
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
	//提交
	function checkForm(types){
		$("#optype").val(types);
		var credTypeID = $("#CredTypeID").val();
		var BillDate = $("#BillDate").val();
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
		var times = periodyear+"-"+periodperiod;
		if(BillDate<times){
			alert("凭证日期不能在财务期间之前!");
			$("#BillDate").focus();
			return false;
		}
		var maxAccYear= "$!globals.get($periodStr,0)";
		var maxAccPeriod= "$!globals.get($periodStr,1)";
		var yearspan = BillDate.substring(0,BillDate.indexOf("-"));
		var periodspan = new Number(BillDate.substring(BillDate.indexOf("-")+1,BillDate.indexOf("-")+3));
		if(yearspan>=maxAccYear && periodspan>maxAccPeriod){
			alert("未存在该财务期间!");
			$("#BillDate").focus();
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
		$("#Attachment").val($("#attachFiles").val());
		if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
		outtips();
		
		//****提交表单前校验流量标志位*****//、
		jQuery.cf_check();
		
		form.submit();
	}
	/* 打印*/
	function printSave(){
		form.button.value="printSave";
		window.save=true; 
		document.form.submit();
		return false ;
	}
	//回调函数刷新凭证模板
	function asyRefure(){
		jQuery.opener('moduleDiv').refure();
	}
	
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body style="background: #f5f5f5;" >
<form id="form" name="form" method="post" action="/VoucherManage.do" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")"/>
<input type="hidden" name="CredNo" id="CredNo" value=""/>
<input type="hidden" name="delFiles" id="delFiles" value=""/>
<input type="hidden" name="tableName" id="tableName" value="tblAccMain" />
<input type="hidden" name="attachFiles" id="attachFiles" value=""/>
<input type="hidden" id="recordValue" name="recordValue" value=""/>
<input type="hidden" id="CredYear" name="CredYear" value="$!globals.get($accPeriod,0)"/>
<input type="hidden" id="CredMonth" name="CredMonth" value="$!globals.get($accPeriod,1)"/>
<input type="hidden" id="Period" name="Period" value="$!globals.get($accPeriod,1)"/>
<input type="hidden" id="noback" name="noback" value="false"/>
<input type="hidden" id="button" name="button" />
<input type="hidden" id="moduleType" name="moduleType" value=""/>
<input type="hidden" id="winCurIndex" name="winCurIndex" value="4"/>
<input type="hidden" id="isEspecial" name="isEspecial" value="$!isEspecial"/>
<input type="hidden" id="ModuleName" name="ModuleName" value="" />
<input type="hidden" id="optype" name="optype" value=""/>
<input type="hidden" name="popWinName" id="popWinName" value="$!popWinName"/>
<input type="hidden" class="cf_flag" name="CashFlag"  value=""/>
<table cellpadding="0" cellspacing="0" border="0" class="framework" style="width:100%">
			<tr style="float:right">
				<td>
					<div class="TopTitle">
						<div class="btnlp" id="cashflowFlag" class="cashflowFlag"><br /></div>
						<div class="btnlp" id="cashflow"><img src="/style/themes/icons/reload.png" title="流量"/><br />流量</div>
						<div class="btnlp" id="save"><img src="/style/themes/icons/filesave.png" title="保存凭证"/><br />保存</div>
						<div class="btnlp" id="saveoradd"><img src="/style/themes/icons/filesave.png" title="保存新增凭证"/><br />保存新增</div>
						<div class="btnlp" id="refur"><img src="/style/themes/icons/reload.png" title="刷新"/><br />刷新</div>
						<div><img src="/style1/images/desk/tab.jpg" style="height:40px;width:1px;"/></div>
						<!-- <div class="btnlp" id="addrow"><img src="/style/themes/icons/edit_add.png" title="插入"/><br />插入</div>
						<div class="btnlp" id="delrow"><img src="/style/themes/icons/edit_remove.png" title="删除"/><br />删除</div> -->
						<div class="btnlp" id="modules"><img src="/style/plan/add_icon.gif" title="点击我可以调用凭证模板哟"/><br />模板</div>
						<div class="btnlp" onclick="printSave()"><img src="/style/themes/icons/print.png" title="打印"/><br />打印</div>
						<div class="btnlp" id="calculator"><img src="/style/themes/icons/w.bmp" title="计算器"/><br />计算器</div>
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
		<tr><td>凭证号：</td><td><input type="text" name="OrderNo" id="OrderNo" #if("$!OrderNo"=="") value="1" #else value="$!OrderNo" #end disabled="disabled"/></td></tr>
		<tr><td>附件数：</td><td><input type="text" name="AcceNum" id="AcceNum" value="$!values.AcceNum"/></td></tr>
	</table>
</div><div>&nbsp;</div>
<div style="padding-left:60px;">日期：<input name="BillDate" id="BillDate"#if("$!accDate"!="") value="$!accDate" #elseif("$!values.BillDate"!="") value="$!values.BillDate" #else value="$!startTime" #end onClick="openInputDate(this);" style="width: 80px;" onChange="dateChange()"/><label style="padding-left:10px;"><span id="span1"></span> 年 第 <span id="span2"></span> 期</label>
</div>
</div></div>

<div id="all_content" style="overflow-x:hidden;overflow-y:auto;margin-top: 10px;width:100%;">

<div style="width:80%;" class="ABK_Add c_main winw1200 f_l mains">
<div style="padding-left: 10px;"></div>
<div class="tablediv" style="overflow-x:hidden;overflow-y:auto;" id="heart_id">
<script type="text/javascript"> 
	var oDiv=document.getElementById("heart_id");
	var sHeight=document.documentElement.clientHeight-250;
	oDiv.style.height=sHeight+"px";
</script>
<table  height="30px" border="1" id="tableItem">
  <tr id="titles" name="titles">
    <td width="3%"><div class="alignDiv">
    <!-- <input id="selAll" onClick="checkAll('keyId')" type="checkbox"/> -->
    <img src="/style1/images/workflow/Add.gif" title="插入行" class="img1" id="addrow"/>
    </div></td>
    <td width="20%"><div class="alignDiv">摘要...</div></td>
    <td width="25%"><div class="alignDiv">会计科目...</div></td>
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
  #if($!values.TABLENAME_tblAccDetailTemplete != "")
  #foreach($detailList in $!values.TABLENAME_tblAccDetailTemplete)
  <tr id="copyTr">
    <td style="text-align: center;">
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
    <input type='hidden' name='ProjectName' id='ProjectName' value='$!detailList.get("LANGUAGEQUERY").get($!detailList.get("tblProject.ProjectName")).get("$globals.getLocale()"))' />
    <input type='hidden' name='tblAccDetail_StockCode' id='StockCode' value='$!detailList.StockCode'/>
    <input type='hidden' name='StockName' id='StockName' value='$!detailList.get("tblStock.StockFullName")' />
    </div>
    <img src="/style/plan/del_icon.gif" title="删除行" class="img1" onClick="delimg(this,'keyId')"/>
    </td>
    <td><input name="tblAccDetail_RecordComment" id="RecordComment" onfocus="focusRow(this)" ondblclick="chooserecord(this)" value="$detailList.RecordComment" onkeyup="init(this)" onkeydown="recommendDown(this)"  disableautocomplete="true" autocomplete="off"></td>
    #set($languageid = $!detailList.get("tblAccTypeInfo.AccFullName"))
    #set($lstr=$values.get("LANGUAGEQUERY").get($languageid).get("$globals.getLocale()"))
    <td><input name="AccCodeName" id="AccCodeName" style="width: 100%" onfocus="focusRow(this)"  ondblclick="selectAccCode(this,'add','$!globals.getSysSetting('currency')')" value="${detailList.AccCode} - $!lstr"
    onKeyDown="if(event.keyCode==13) {event.keyCode=9;popValue(this,'add','$!globals.getSysSetting('currency')')}" disableautocomplete="true" autocomplete="off"/>
    	<input name="tblAccDetail_AccCode" id="AccCode" type="hidden" value="$detailList.AccCode" /></td>
    #if($!globals.getSysSetting('currency')=="true")
    <td><select name="tblAccDetail_Currency" id="Currency" style="width: 100%" onfocus="focusRow(this)"  onchange="selectrate(this)">
    	<option value=""></option>
    	<script type="text/javascript">
    		querycurrency('itemtr_$count','$detailList.AccCode','$!detailList.Currency','add');
    	</script>
    </select></td>
    #set($currencyRate = $!globals.formatNumber($!detailList.CurrencyRate,false,false,true,"tblAccDetail","CurrencyRate",true))
    #if($currencyRate==0 || "$!detailList.Currency"=="")
    	#set($currencyRate ="")
    #end
    <td><input name="tblAccDetail_CurrencyRate" id="CurrencyRate" value="$!currencyRate" onfocus="focusRow(this)"  onblur="updateExchange(this)" disableautocomplete="true" autocomplete="off"/></td>
    #set($debitCurrencyAmount = $!globals.formatNumber($!detailList.DebitCurrencyAmount,false,false,true,"tblAccDetail","DebitCurrencyAmount",true))
    #if($debitCurrencyAmount==0)
    	#set($debitCurrencyAmount ="")
    #end
    <td><input name="tblAccDetail_DebitCurrencyAmount" id="DebitCurrencyAmount" onblur="sumMoney(this)" value="$!debitCurrencyAmount" onkeyup="showBigNum(this)" onfocus="showBigNum(this);focusRow(this)" disableautocomplete="true" autocomplete="off"/></td>
    #set($lendCurrencyAmount = $!globals.formatNumber($!detailList.LendCurrencyAmount,false,false,true,"tblAccDetail","LendCurrencyAmount",true))
    #if($lendCurrencyAmount==0)
    	#set($lendCurrencyAmount ="")
    #end
    <td><input name="tblAccDetail_LendCurrencyAmount" id="LendCurrencyAmount" onblur="sumMoney(this)" value="$!lendCurrencyAmount" onkeyup="showBigNum(this)" onfocus="showBigNum(this);focusRow(this)" disableautocomplete="true" autocomplete="off"/></td>
    #end
    #set($debitamount = $!globals.formatNumber($!$detailList.DebitAmount,false,false,true,"tblAccDetail","DebitAmount",true))
    #if($debitamount==0)
    	#set($debitamount = "")
    #end
    <td><input name="tblAccDetail_DebitAmount" id="DebitAmount" onblur="sumMoney(this)"  value="$!debitamount" onkeyup="showBigNum(this)" onfocus="showBigNum(this);focusRow(this)" disableautocomplete="true" autocomplete="off"/></td>
    #set($lendamount = $!globals.formatNumber($!detailList.LendAmount,false,false,true,"tblAccDetail","LendAmount",true))
    #if($lendamount==0)
    	#set($lendamount = "")
    #end
    <td><input name="tblAccDetail_LendAmount" id="LendAmount" onblur="sumMoney(this)" value="$!lendamount" onkeyup="showBigNum(this)" onfocus="showBigNum(this);focusRow(this)" disableautocomplete="true" autocomplete="off"/></td>
  </tr>
  #set($count=$count+1)
  #end
  #else
  	#foreach ( $foo in [1..5] )
  	<tr id="copyTr" class="copyTr" row_id=$foo>
	    <td style="text-align: center;">
	        <input type='hidden' class='RowNum' name='tblAccDetail_RowNum' value='$foo'/>
	        <input type='hidden' class='MainItem' name='tblAccDetail_MainItem' value ='' />
	        <input type='hidden' class='SecItem' name='tblAccDetail_SecItem' value ='' />
	        <input type='hidden' class='Refs' name='tblAccDetail_Refs' value ='' />
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
		    <input type='hidden' name='ProjectName' id='ProjectName' value='$!detailList.get("LANGUAGEQUERY").get($!detailList.get("tblProject.ProjectName")).get("$globals.getLocale()"))' />
		    <input type='hidden' name='tblAccDetail_StockCode' id='StockCode' value='$!detailList.StockCode'/>
		    <input type='hidden' name='StockName' id='StockName' value='$!detailList.get("tblStock.StockFullName")' />
		    </div>
	    <img src="/style/plan/del_icon.gif" title="删除行" class="img1" onClick="delimg(this,'keyId')"/></td>
	    <td><input name="tblAccDetail_RecordComment" id="RecordComment" onfocus="focusRow(this)"   ondblclick="chooserecord(this)" onkeyup="init(this)"  onkeydown="recommendDown(this)"  disableautocomplete="true" autocomplete="off"></td>
	    <td><input name="AccCodeName" id="AccCodeName" style="width: 100%" onfocus="focusRow(this)"  
	    onkeyup="currencysettings ='$!globals.getSysSetting('currency')'; showData(this,'AccInfoAccDet','AccCodeName','选择科目')"
	    ondblclick="selectAccCode(this,'add','$!globals.getSysSetting('currency')')" 
	      disableautocomplete="true" autocomplete="off"/>
	    	<input name="tblAccDetail_AccCode" id="AccCode" type="hidden" value="" /></td>
	    #if($!globals.getSysSetting('currency')=="true")
	    <td><select name="tblAccDetail_Currency" id="Currency" style="width: 100%" onfocus="focusRow(this)"  onchange="selectrate(this)">
	    	<option value=""></option>
	    </select></td>
	    <td><input name="tblAccDetail_CurrencyRate" id="CurrencyRate" onfocus="focusRow(this)"  onblur="updateExchange(this)"  disableautocomplete="true" autocomplete="off"/></td>
	    <td><input name="tblAccDetail_DebitCurrencyAmount" id="DebitCurrencyAmount"  onblur="sumMoney(this)" onkeyup="showBigNum(this)" onfocus="showBigNum(this);focusRow(this)" disableautocomplete="true" autocomplete="off"/></td>
	    <td><input name="tblAccDetail_LendCurrencyAmount" id="LendCurrencyAmount" onblur="sumMoney(this)" onkeyup="showBigNum(this)" onfocus="showBigNum(this);focusRow(this)" disableautocomplete="true" autocomplete="off"/></td>
	    #end
	    <td><input name="tblAccDetail_DebitAmount" id="DebitAmount" onblur="sumMoney(this)" onkeyup="showBigNum(this)" onfocus="showBigNum(this);focusRow(this)" disableautocomplete="true" autocomplete="off"/></td>
	    <td><input name="tblAccDetail_LendAmount" id="LendAmount" onblur="sumMoney(this)" onkeyup="showBigNum(this)" onfocus="showBigNum(this);focusRow(this)" disableautocomplete="true" autocomplete="off"/></td>
  	</tr>
  	#end
  #end

  <tr style="height: 30px;" name="titles">
  	<td></td>
  	<td colspan="2" align="center">合&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;计</td>
  	#if($!globals.getSysSetting('currency')=="true")
  	<td></td>
  	<td></td>
  	<td><span id="currencydebit"></span></td>
  	<td><span id="currencylend"></span></td>
  	#end
  	<td><span id="debit"></span></td>
  	<td><span id="lend"></span></td>
  </tr>
</table>
</div><div align="center">大写金额：<span id="bigValue"></span></div>
</div>

<div id="computeDiv" >
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
	附件：<a href="javascript:openAttachUpload('/affix/tblAccMain/')" >&nbsp;<span></span>
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
<div class="ABK_Add c_main winw1200 f_l mains divs" style="height:30px;width:100%;padding: 5px 0px 5px 0px;margin-top: 5px;" id="divemp">
	<ul>
	<li>
		<span>部&nbsp;&nbsp;门：</span>
		<input name="DepartmentCode" id="mDepartmentCode" type="hidden" #if("$!values.DepartmentCode"=="") value="$globals.getLoginBean().departCode" #else value="$!values.DepartmentCode" #end/>
		<input name="DepartmentName" id="DepartmentName" #if("$!values.DepartmentCode"=="") value="$globals.getLoginBean().departmentName" #else value="$deptMap.get("$!values.DepartmentCode")" #end  onkeyup="showData(this,'SelectAccDepartment','DepartmentName','选择部门')" ondblclick="selectPops(this,'SelectAccDepartment','DepartmentName','mDepartmentCode','选择部门')"/>
		<img src='/style1/images/St.gif' onClick="selectPops(this,'SelectAccDepartment','DepartmentName','mDepartmentCode','选择部门')" alt='选择部门' class='search'/>
	</li>
	<li>
		<span>经手人：</span>
		<input name="EmployeeID" id="EmployeeID" #if("$!values.EmployeeID"=="") value="$globals.getLoginBean().id" #else value="$!values.EmployeeID" #end type="hidden"/>
		<input name="EmployeeName" id="EmployeeName" #if("$!values.EmployeeID"=="") value="$globals.getLoginBean().empFullName" #else value="$!globals.getEmpNameById($!values.EmployeeID)" #end  onkeyup="showData(this,'SelectAccEmployee','EmployeeName','选择部门')" ondblclick="selectPops(this,'SelectAccEmployee','EmployeeName','EmployeeID','选择经手人')"/>
		<img src='/style1/images/St.gif' onClick="selectPops(this,'SelectAccEmployee','EmployeeName','EmployeeID','选择经手人')" alt='选择经手人' class='search'/>
	</li>
	<li><span>结算方式：</span><input name="AcceptMode" id="AcceptMode" value="$!values.AcceptMode"/></li>
	<li><span>结算号：</span><input name="AcceptNumber" id="AcceptNumber" value="$!values.AcceptNumber"/></li>
	</ul>
</div>
<div class="ABK_Add c_main winw1200 f_l mains divs" style="height:30px;width:100%;padding: 5px 0px 5px 0px;" id="divs">
	<ul>
	<li><span>核&nbsp;&nbsp;准：</span><span></span></li>
	<li><span>复&nbsp;&nbsp;核：</span><span></span></li>
	<li><span>过&nbsp;&nbsp;账：</span><span></span></li>
	<li><span>制&nbsp;&nbsp;单：</span><span>$globals.getLoginBean().empFullName</span></li>
	</ul>
</div>
<!-- <div style="margin: 445px 0px 20px 15px;">
	<div style="float: left;padding-top: 10px;">备注：</div><div style="padding-top: 10px;"><textarea rows="5" cols="110" name="Remark" id="Remark"></textarea></div>
</div> -->
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