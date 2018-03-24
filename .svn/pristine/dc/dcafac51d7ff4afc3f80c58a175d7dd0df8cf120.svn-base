<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>期初录入</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/listgrid.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/classFunction.css"/>


<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/dropdownselect.js"></script>

<style type="text/css">
.td_right{text-align: right;padding-right: 5px;vertical-align: bottom;}
.inpsearch_btn{background: url(/style/images/client/bg.gif) no-repeat 1000px 1000px;width:20px;background-position: right -413px;height: 20px;color:#fff;display: block;text-indent: -999em;overflow: hidden;font-size: 1px;}
img {border:0px}
li {list-style-type:none;}
em,i{font-style:normal;}
.oamainhead {float:left;width:100%;height:31px;padding-top:5px;border-bottom:1px solid #ECECEC;}
.scroll_function_small_a{margin:3px 0 0 3px;float:left;overflow:auto;width:98%; overflow-x:auto;}
.scroll_function_small_bbs{margin-left:3px;float:left;margin-top:3px;height:auto;width:99%;background:#ffffff;overflow-x:hidden;}
#edittableDIV{max-height:400px;overflow:auto;width:100%;}
.listRange_list_function{table-layout:fixed;border-collapse:collapse;border-top:1px #d2d2d2 solid;border-right:1px #d2d2d2 solid;}
.listRange_list_function td{border-bottom:1px #d2d2d2 solid;border-left:1px #d2d2d2 solid;white-space:nowrap;}
.listRange_list_function thead{background:#2e8cbc;}
.listRange_list_function thead td{padding:4px 4px 0 4px;height:28px;text-align:center;color:#fff;word-break:break-all;word-wrap :break-word;position: relative;}
.listRange_list_function tbody td{padding:0 4px;height:28px;white-space:nowrap;overflow: hidden;text-overflow: ellipsis;}
.listRange_list_function select{width:100%;border:1px solid #ccc;padding:0px;text-align:left;}
.bDel{width:7px;height:7px;display:inline-block;background:url(/style/images/client/CloseBtn.png) no-repeat 0 0;cursor:pointer;float:left;margin:2px 0 0 6px;}
.money_right{text-align: right;}
.listRange_list_function input{border:0;width:100%;}
.hBtns{padding:1px 12px;font-weight:bold;cursor:pointer;display:inline-block;height:21px;line-height:21px;color:#666;border:1px #bbb solid;border-radius:3px;}
.hBtns:hover{background:#f2f2f2;}
.subbox .operate2{padding: 6px 3px 2px;}

.page-wp{text-align:right;height:30px}
.listRange_pagebar{padding:5px 15px 0 0;;text-align:left;display:inline-block;}
.listRange_pagebar>div{float:left;display:inline-block;width:auto;padding:0;height:20px;line-height:25px;}
.listRange_pagebar>button {width:21px;height:21px;border:0px;font-size:0;background:url(/style/images/client/first_btn.png) no-repeat 0 -64px;vertical-align:top;cursor:pointer;outline:0;}
.listRange_pagebar>button:hover{background:url(/style/images/client/first_btn.png) no-repeat -32px -64px;}
.listRange_pagebar select {float:left;display:inline-block;border:1px #eaeaea solid;border-radius:4px;}
.listRange_pagebar input{border:1px #ebebeb solid;width:30px;height:18px;float:left;border-radius:4px;}
.listRange_pagebar_small{margin:2px 0 0 2px;float:right;text-align:right;width:280px;}
.listRange_pagebar_small div{margin-top:3px;float:left;}
.listRange_pagebar_small ul{margin-top:3px;float:left;}
.listRange_pagebar_small li{float:left;}
.listRange_pagebar_small input{border:0px;border-bottom:1px solid #666666;width:20px; margin-left:3px;text-align:center;}
.listRange_pagebar_small button {margin:0px;padding:0px;}

.listRange_pagebar_small2{margin:2px 30% 0 0;float:right;text-align:right;width:280px;}
.listRange_pagebar_small2 div{margin-top:5px;float:left;	}
.listRange_pagebar_small2 ul{margin-top:5px;float:left;}
.listRange_pagebar_small2 li{float:left;}
.listRange_pagebar_small2 input{border:0px;border-bottom:1px solid #666666;width:20px; margin-left:3px;text-align:center;}
.listRange_pagebar_small2 button {margin:0px;padding:0px;}.p_first,.p_prep,.p_next,.p_last{background-image:url(/style/images/client/first_btn.png);background-repeat:no-repeat;width:16px;height:16px;margin:2px 5px 0px 8px; display:inline-block;vertical-align:middle;cursor:pointer;float: left;}
.p_first{background-position:-16px 0;}
.p_prep{background-position:-16px -16px;}
.p_next{background-position:-16px -48px;}
.p_last{background-position:-16px -32px;}
.p_first2,.p_prep2,.p_next2,.p_last2{background-image:url(/style/images/client/first_btn.png);background-repeat:no-repeat;width:16px;height:16px;margin:4px 5px;cursor:default;float:left;display:inline-block;vertical-align: middle;}
.p_first2{background-position:0 0;}
.p_prep2{background-position:0 -16px;}
.p_next2{background-position:0 -48px;}
.p_last2{background-position:0 -32px;}

</style>
#set($isFlag = "false")
#if("$!iniData.isCalculate"=="1" || "$!detail"=="true")
	#set($isFlag = "true")
#end
<script type="text/javascript">
	var moduleId = "$LoginBean.getOperationMap().get("/IniAccQueryAction.do").moduleId";
	
	jQuery(function(){
		var ss = jQuery("#applyTbl thead tr td").size();
		jQuery(jQuery("#applyTbl").width(ss*80));
		
		#if($isItem.size() != 0 || $isCalculates == true)
			/* 不信息设置未只读 */
			jQuery("#conters").find("input").attr("disabled","disabled");
		#end
		if("$!iniData.isCalculate"=="1" || "$!detail"=="true"){
			jQuery("#conters").find("input").attr("disabled","disabled");
			jQuery("#applyTody").find("input").attr("readonly","readonly");
		}
	})
	

	
	function isMoney(money){
		var moneys = "";
		if(money != "" && isFloat(money) && money != 0){
			moneys = new Number(money);
		}else{
			moneys = 0;
		}
		return moneys;
	}

	
	/* 小数点控制 */
	var lens = "$!globals.getSysSetting('DigitsAmount')";
	function subStringMoney(str){
		var len = lens;
		var strvalue = new String(str);
		if (strvalue.indexOf(".")>0){
			strvalue = strvalue.substring(0,new Number(strvalue.indexOf("."))+new Number(len)+3);
			var tNum = Math.pow(10,lens);
			strvalue = Math.round(strvalue*tNum)/tNum;
		}
		return strvalue;
	}
	
	/* 弹出框选择（外币，核算项） */
	var objs;
	var hideValues;
	var showValues;
	var popNames;
	function popSelect(obj,popName,hideValue,showValue,name){
		var displayName=encodeURI(name) ;
		if(("$!iniData.AccNumber"=="1122" || "$!iniData.AccNumber"=="1123" 
			|| "$!iniData.AccNumber"=="2202" || "$!iniData.AccNumber"=="2203") && (popName=="SelectIniClient" || popName=="SelectIniProvider")){
			popName = popName+"Check";
		}
		var urlstr = '/UserFunctionAction.do?operation=22&popupWin=Popdiv&tableName=&selectName='+popName+"&MOID="+moduleId+"&MOOP=add&LinkType=@URL:&displayName="+displayName;
		if(showValue == "EmployeeIDName"){
			//职员选择框

			deptname= $(obj).parent().parent().find("#DepartmentCodeName").val();
			if(deptname!=undefined && deptname!=""){
				deptname = encodeURI(encodeURI(deptname));
				urlstr += "&tblDepartment_DeptFullName="+deptname;
			}
		}
		asyncbox.open({id:'Popdiv',title:name,url:urlstr,width:750,height:470})
		showValues = showValue;
		objs = obj;
		hideValues = hideValue;
		popNames = popName;
	}
	
	function popSelect2(obj,popName,hideValue,showValue,name){
		showValues = showValue;
		objs = obj;
		hideValues = hideValue;
		var displayName=encodeURI(name) ;
		if(("$!iniData.AccNumber"=="1122" || "$!iniData.AccNumber"=="1123" 
			|| "$!iniData.AccNumber"=="2202" || "$!iniData.AccNumber"=="2203") && (popName=="SelectIniClient" || popName=="SelectIniProvider")){
			popName = popName+"Check";
		}
		popNames = popName;
		var urlstr = '/UserFunctionAction.do?popupWin=Popdiv&selectName='+popName+"&MOID="+moduleId+"&MOOP=add&LinkType=@URL:&displayName="+displayName;
		var dropdown=obj.dropdown;
		if(dropdown==undefined){
			var tmpUrl = urlstr.substring(urlstr.indexOf("?")+1);
	    	var temp_par = tmpUrl.split("&");
	    	var data = {
	    		operation:"DropdownPopup"
	    	};
	    	
	    	for(var i = 0; i<temp_par.length ;i++){
	    		var temp_item =temp_par[i].split("=");
	    		jQuery(data).attr(temp_item[0],temp_item[1]);
	    	}
			
			dropdown = new dropDownSelect("t_"+obj.id+new Date().getTime(),data,obj);
			dropdown.retFun = exePopdiv;
			dropdown.showData();
			return ;
		}
		if(event.keyCode == 38){
			if(dropdown!=undefined){
				dropdown.selectUp();
			}
			return ;
		}else if(event.keyCode==40){
			if(dropdown!=undefined){
				dropdown.selectDown();
			}
			return ;
		}else if(event.keyCode==13){
			if(dropdown!=undefined){
				dropdown.curValue();
			}
			return ;
		}else if(event.keyCode==27){
			if(dropdown!=undefined){
				dropdown.close();
			}
			return ;
		}
		dropdown.showData();
	}
	
	/* 弹出框返回数据 */
	function exePopdiv(returnValue){
		if(typeof(returnValue)=="undefined") return ;
		var note = returnValue.split("#;#") ;
		if(showValues == "DepartmentCodeName"){
			//部门弹出框

			jQuery(objs).parent().parent().find("#EmployeeID").val('');
			jQuery(objs).parent().parent().find("#EmployeeIDName").val('');
		}
		if(showValues == "EmployeeIDName"){
			//职员弹出框

			jQuery(objs).parent().parent().find("#DepartmentCode").val(note[1]);
			jQuery(objs).parent().parent().find("#DepartmentCodeName").val(note[2]);
			jQuery(objs).parent().parent().find("#"+hideValues).val(note[0]);
			jQuery(objs).parent().parent().find("#"+showValues).val(note[4]);
		}else{
			if(("$!iniData.AccNumber"=="1122" || "$!iniData.AccNumber"=="1123" 
				|| "$!iniData.AccNumber"=="2202" || "$!iniData.AccNumber"=="2203") && (popNames=="SelectIniClientCheck" || popNames=="SelectIniProviderCheck")){
				var strs = returnValue.split("#|#");
				var counts = 0;
				for(var i=0;i<strs.length;i++){
					if(strs[i]!="" && typeof(strs[i])!="undefined"){
						var values = strs[i].split("#;#");
						if(counts == 0){
							jQuery(objs).parent().parent().find("#"+hideValues).val(values[0]);
							jQuery(objs).parent().parent().find("#"+showValues).val(values[1]);
						}else{
							jQuery("#applyTody").find("tr:last").find("#"+hideValues).val(values[0]);
							jQuery("#applyTody").find("tr:last").find("#"+showValues).val(values[1]);
							addRows();
						}
						counts++;
					}
				} 
			}else{
				jQuery(objs).parent().parent().find("#"+hideValues).val(note[0]);
				jQuery(objs).parent().parent().find("#"+showValues).val(note[1]);
			}
		}
	}
	
	/* 验证 */
	function paterns(){
		var arrayItem = new Array();
		var arrayItemName = new Array();
		#foreach($item in $!isItem)
			arrayItem[new Number($!velocityCount)-1] = "$!globals.get($!globals.strSplit($!item,';'),1)";
			arrayItemName[new Number($!velocityCount)-1] = "$!globals.get($!globals.strSplit($!item,';'),0)";
		#end
		var f = false;
		for(var k=0;k<arrayItem.length;k++){
			var flag = false;
			var sum = 0;
			jQuery("input[id='"+arrayItem[k]+"']").each(function(i,n){
				var TotalRemain = $(n).parent().parent().find("#TotalRemain").val();
				var BeginAmount = $(n).parent().parent().find("#BeginAmount").val();
				var TotalDebit = $(n).parent().parent().find("#TotalDebit").val();
				var TotalLend = $(n).parent().parent().find("#TotalLend").val();
				if(((TotalRemain!="" && TotalRemain!="0") || (BeginAmount!=""&&BeginAmount!="0") || (TotalDebit!=""&&TotalDebit!="0") || (TotalLend!=""&&TotalLend!="0") ) && $(n).val() == ""){
					var title = '当前会计科目已经启用核算'+arrayItemName[k]+'，'+arrayItemName[k]+'不能为空！';
					asyncbox.tips(title,'alert',1500);
					jQuery(n).focus();
					flag = true;
					f = true;
					return false;
				}
			});
		}
		return flag;
	}
	
	/* 保存数据 */
	function saves(){
		if(paterns()){
			return false;
		}
		form.submit();
	}
	
	
	/* 获取汇率 */
	function gainExchange(obj){
		var currencyId=jQuery(obj).val();
		if(currencyId.length>0){
			AjaxRequest('/UtilServlet?operation=currencyRate&currency='+currencyId) ;
	        	 var value = response;
				 if(value == 0){
		      		jQuery(obj).parent().parent().find("#CurrencyRate").val("");
				 }else{			 
					jQuery(obj).parent().parent().find("#CurrencyRate").val(value);
				 }			
	     }else{
		 	jQuery(obj).parent().parent().find("#CurrencyRate").val("");
		 	jQuery(obj).parent().parent().find("#CurBeginAmount").val("");
			jQuery(obj).parent().parent().find("#CurTotalDebit").val("");
			jQuery(obj).parent().parent().find("#CurTotalLend").val("");
			jQuery(obj).parent().parent().find("#CurTotalRemain").val("");
	  	 }
	  	 countMoney(obj,'base');
	}
	
	/* 修改前 */
	function update(id){
		width=850;
		height=400;		
		usrc ='/IniAccQueryAction.do?operation=7&accCode=$!iniData.classCode&accNumber=$!iniData.AccNumber&opType=updateIniItemPre&keyId='+id;
		openPop('PopMainOpdiv','',usrc,width,height,true,height==document.documentElement.clientHeight);  
	}
	/* 添加前 */
	function addPre(){	
		width=850;
		height=400;		
		usrc ='/IniAccQueryAction.do?operation=7&accCode=$!iniData.classCode&accNumber=$!iniData.AccNumber&opType=addIniItemPre';
		openPop('PopMainOpdiv','',usrc,width,height,true,height==document.documentElement.clientHeight);  
	}
	function deleteItem(){
		document.form.opType.value="delIniItem";
		document.form.submit();
	}
	
	function countToMoney(obj){ 
		$(obj).val(f(Number($(obj).val()),$digits));
		$("#TotalRemains").val(f(Number($("#BeginAmounts").val())+Number($("#TotalDebits").val())-Number($("#TotalLends").val()),$digits));
		$("#TotalRemainsShow").val(f(Number($("#BeginAmounts").val())+Number($("#TotalDebits").val())-Number($("#TotalLends").val()),$digits));
	}
	
	/* 关闭div */
	function closeIniDiv(){
		parent.jQuery.close('IniDiv');
	}
	/* 导入期初数据 */
	function imports(){
		var heights =  document.documentElement.clientHeight-70;
		asyncbox.open({
		 	title : '期初导入',
		 	id : 'importIni',
		　　	//url : '/IniAccImportAction.do?operation=7&opType=importPre',
			url : '/importDataAction.do?fromPage=importList&operation=91&tableName=tblIniAccDet&moduleParam=$!iniData.AccNumber&NoBack=NoBack',
			width : 500,
			height : heights,
			callback : function(action,opener){
				
	　　　	}
	　	});
	}
	function importHis(){
		var heights =  document.documentElement.clientHeight-70;
		asyncbox.open({
		 	title : '期初导入历史',
		 	id : 'importIni',
			url : '/IniAccQueryAction.do?operation=7&accCode=$!iniData.classCode&accNumber=$!iniData.AccNumber&opType=importHistory',
			width : 500,
			height : heights,
			callback : function(action,opener){
				
	　　　	}
	　	});
	}
</script>

</head>
<body>
<form method="post" scope="request" id="form" name="form" action="/IniAccQueryAction.do">
<input type="hidden" name="id" id="id" value="$!iniData.id" />
<input type="hidden" name="accCode" id="accCode" value="$!iniData.classCode" />
<input type="hidden" name="accNumber" id="accNumber" value="$!iniData.AccNumber" />
<input type="hidden" name="oldaccName" id="oldaccName" value="$!iniData.AccName" />
<input type="hidden" name="popWinName" id="popWinName" value="$!popWinName" />
<input type="hidden" name="opType" id="opType" value="" />
<input type="hidden" name="operation" id="operation" #if($isItem.size() == 0 && $isCalculates != true) value="2" #end value="7" />
<input type="hidden" name="isItems" id="isItems" #if($isItem.size() == 0 && $isCalculates != true) value="true" #end />

<div class="" style="width:100%">
<div class="subbox cf">
<div class="operate operate2" id="handle">
  <ul>
  	<li class="sel">基础信息</li>  	
  	
  	<li style="float: right;"><span class="hBtns" onclick="closeIniDiv()">关闭</span></li>  	

	#if($!NowPeriod == -1 && $isCalculates != true && ( $LoginBean.operationMap.get("/IniAccQueryAction.do").update() || $LoginBean.id=="1"))
	  	<li style="float: right;"><span class="hBtns" onclick="saves()">确定</span></li>
	#end
  	#if($!NowPeriod == -1 && $isCalculates && ( $LoginBean.operationMap.get("/IniAccQueryAction.do").update() || $LoginBean.id=="1"))	
  	  	
  		<li style="float: right;"><span type="button" onClick="importHis()" class="hBtns">导入历史</span></li>
		<li style="float: right;"><span type="button" onClick="imports()" class="hBtns">导入</span></li>
	#end
  </ul>
  <div class="closel"></div>
</div>

<div class="listRange_frame" id="listRange_frame" style="padding-top: 10px;padding-left: 10px;width:98%;">
	<div id="conters" style="border: #81b2e3 1px solid;">
		<table border="0" width="100%" style="margin: 5px 5px 5px 5px;text-align: center;">
			<tr>
				<td align="right">$text.get("iniAcc.lb.accNumber")：</td><td align="left">

				<input name="accNumberShow" id="accNumberShow" type="text" value="$!iniData.AccNumber" disabled="disabled"/></td>
				<td align="right">$text.get("iniAcc.lb.accName")：</td><td align="left">
				<input name="accName" id="accName" value="$!iniData.AccName" disabled="disabled"/></td>
				<td align="right">科目方向：</td><td colspan="3"  align="left">
				<input name="JdFlag" id="JdFlag" #if("$!iniData.JdFlag"=="1") value="借" #else value="贷"#end disabled="disabled"/></td>
			</tr>
			<tr>
				<td align="right">$globals.getFieldDisplay("tblIniAccDet.BeginAmount")：</td><td align="left">
				
				<input name="BeginAmounts" id="BeginAmounts" type="text" value="$!iniData.CurrYIniBase" onblur="countToMoney(this)"/></td>
				<td align="right">&nbsp;&nbsp;&nbsp;$globals.getFieldDisplay("tblIniAccDet.TotalDebit")：</td><td align="left">

				<input name="TotalDebits" id="TotalDebits" value="$!iniData.CurrYIniDebitSumBase" onblur="countToMoney(this)"/></td>
				<td align="right">&nbsp;&nbsp;&nbsp;$globals.getFieldDisplay("tblIniAccDet.TotalLend")：</td><td align="left"> 

				<input name="TotalLends" id="TotalLends" value="$!iniData.CurrYIniCreditSumBase" onblur="countToMoney(this)"/></td>
				<td align="right">$globals.getFieldDisplay("tblIniAccDet.TotalRemain")：</td><td align="left">
				<input name="TotalRemains" id="TotalRemains" type="hidden" value="$!iniData.CurrYIniBalaBase"/>
				<input name="TotalRemainsShow" id="TotalRemainsShow" type="text" value="$!iniData.CurrYIniBalaBase" disabled="disabled" /></td>
			</tr>
		</table>
	</div>
</div>
  
#if($isCalculates == true)
<div class="operate operate2" id="handle">
  <ul>
  	<li class="sel" style="margin-right:50px;">明细信息</li>  
  	
  	#foreach($item in $!isItem)
		#set($itemobj = $!globals.get($!globals.strSplit($!item,';'),1))
		#set($itemPop = $!globals.get($!globals.strSplit($!item,';'),3))
		#set($itemName = $!globals.get($!globals.strSplit($!item,';'),0))
		#set($iName = "$!{itemobj}Name")
		<li><span >$itemName :</span>
		<input type="hidden" name="con$!itemobj" id="con$!itemobj" value="$!conMap.get("con$!itemobj")" />					
		<input type="text" name="con$!{itemobj}Name" id="con$!{itemobj}Name" value="$!conMap.get("con$!{itemobj}Name")"  size="20" class="input_type" 
			ondblclick="popSelect(this,'$!itemPop','con$!itemobj','con$!{itemobj}Name','选择$itemName')"
			onkeyup="popSelect2(this,'$!{itemPop}','con$!{itemobj}','con$!{itemobj}Name','选择$!itemName')"
			onblur="cl(this);" disableautocomplete="true" autocomplete="off">
			<b class="stBtn icon16" style="right: 2px;top: 4px;background-position: -32px 0;cursor: pointer;" onclick="popSelect(this,'$!itemPop','con$!itemobj','con$!{itemobj}Name','选择$itemName')"></b></li>
	#end
  	
  	#if($!currList.size()>0)	
 	<li style=""><span >$globals.getFieldDisplay("tblIniAccDet.Currency"):</span>
 		<select id="conCurrency" name="conCurrency" >
			<option value="all"></option>
			#foreach($curr in $currList)
				<option value="$!globals.get($!curr,0)" #if("$!conCurrency"=="$!globals.get($!curr,0)" && "$!globals.get($!curr,0)" != "") selected #end>$!globals.get($!curr,1)</option>
			#end
		</select></li>	
 	#end
 	<li style=""><span class="hBtns" onclick="#if($!detail) document.form.operation.value=5; #end document.form.submit()">查询</span></li> 	
 		#if($!NowPeriod == -1 && ($LoginBean.operationMap.get("/IniAccQueryAction.do").update() || $LoginBean.id=="1" ))
 			<li style="float: right;"><span class="hBtns" onclick="deleteItem()">册除</span></li>
 		#end
 		#if($!NowPeriod == -1 && ( $LoginBean.operationMap.get("/IniAccQueryAction.do").update() || $LoginBean.id=="1"))
 			<li style="float: right;"><span class="hBtns" onclick="addPre()">添加</span></li>
 		#end
  </ul>
  <div class="closel"></div>
</div>
<div class="scroll_function_small_a" id="scroll_small">
	<script type="text/javascript"> 
		var oDiv=document.getElementById("scroll_small");
		var sHeight=document.documentElement.clientHeight-200;
		oDiv.style.height=sHeight+"px";
	</script>
	
	<table border="0" cellpadding="0" align="center" cellspacing="0" class="listRange_list_function" id="applyTbl">
		<thead>
			<tr>		
				<td width="24"><input type="checkbox" onclick="jQuery('input[name=\'keyId\']').attr('checked',this.checked);" disableautocomplete="true" autocomplete="off"></td>
				<td width="30px">NO.</td>
				#if($!NowPeriod == -1 && ( $LoginBean.operationMap.get("/IniAccQueryAction.do").update() || $LoginBean.id=="1")) <td width="30px">修改 </td> #end
				#foreach($item in $!isItem)
					#set($isHide = $!globals.hideViewRight('tblAccTypeInfo','',$!globals.get($!globals.strSplit($!item,';'),4),'/IniAccQueryAction.do'))
					#if("$isFlag"=="true" && !$isHide)
					#else
					<td width="300px">$!globals.get($!globals.strSplit($!item,';'),0)..</td>
					#end
				#end
				<td width="80px">$globals.getFieldDisplay("tblIniAccDet.BeginAmount")</td>
				<td width="80px">$globals.getFieldDisplay("tblIniAccDet.TotalDebit")</td>
				<td width="80px">$globals.getFieldDisplay("tblIniAccDet.TotalLend")</td>
				<td width="80px">$globals.getFieldDisplay("tblIniAccDet.TotalRemain")</td>
				#if($!currList.size()>0)
				<td width="80px">$globals.getFieldDisplay("tblIniAccDet.Currency")</td>
				<td width="80px">$globals.getFieldDisplay("tblIniAccDet.CurrencyRate")</td>
				<td width="80px">$globals.getFieldDisplay("tblIniAccDet.CurBeginAmount")</td>
				<td width="80px">$globals.getFieldDisplay("tblIniAccDet.CurTotalDebit")</td>
				<td width="80px">$globals.getFieldDisplay("tblIniAccDet.CurTotalLend")</td>
				<td width="80px">$globals.getFieldDisplay("tblIniAccDet.CurTotalRemain")</td>
				#end				
				<td width="100px">$globals.getFieldDisplay("tblIniAccDet.Remark")</td>
			</tr>
		</thead>		
		<tbody id="applyTody" name="applyTody">				
				#foreach($ini in $iniList)
				<tr>
					<td align="center">
					<input type="checkbox" value="$!ini.get('id')" name="keyId"  disableautocomplete="true" autocomplete="off"></td>
					<td algin="center"><input style="width:30px;" id="order" name="order" value="$velocityCount" readonly="readonly"></td>
					#if($!NowPeriod == -1 && ( $LoginBean.operationMap.get("/IniAccQueryAction.do").update() || $LoginBean.id=="1"))
						<td> <a href="javascript:void(0)" onclick="update('$!ini.get('id')')"> 修改 </a></td>
					#end
					#foreach($item in $!isItem)
						#set($itemobj = $!globals.get($!globals.strSplit($!item,';'),1))
						#set($iName = "$!{itemobj}Name")
						#set($isHide = $!globals.hideViewRight('tblAccTypeInfo','',$!globals.get($!globals.strSplit($!item,';'),4),'/IniAccQueryAction.do'))
						#if("$isFlag"=="true" && !$isHide)
						#else
						<td>
							$ini.get($!iName) &nbsp;
						</td>
						#end
					#end
					
					<td  >$!ini.get('BeginAmount')</td>
					<td >$!ini.get('TotalDebit')</td>
					<td  >$!ini.get('TotalLend')</td>
					<td  >$!ini.get('TotalRemain')</td>
					#if($!currList.size()>0)
					<td  >#if("$!ini.get('CurrencyName')" == "") 本位币 #else $!ini.get('CurrencyName') #end</td>
					<td  >$!ini.get('CurrencyRate')</td>
					<td  >$!ini.get('CurBeginAmount')</td>
					<td >$!ini.get('CurTotalDebit')</td>
					<td  >$!ini.get('CurTotalLend')</td>
					<td  >$!ini.get('CurTotalRemain')</td>
					#end			
					<td><input type="text" name="Remark" id="Remark" value="$!ini.get('Remark')" /></td>
				</tr>
				#end
		 </tbody>
</table>
</div>
<div class="page-wp" >
	<div class="listRange_pagebar" style="position:relative"> $!pageBar </div>
</div>
#end

</form>
</body>
</html>

