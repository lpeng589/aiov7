<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>期初录入</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<link type="text/css" rel="stylesheet" href="/js/ui/jquery-ui-1.8.18.custom.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<link type="text/css" href="/js/dialog/skins/default.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/listgrid.css" />
<script type="text/javascript" src="/js/k_listgrid.js"></script> 
<script type="text/javascript" src="/js/dropdownselect.js"></script>

<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/editPage.vjs","",$text)"></script>
<script type="text/javascript" src="/js/Map.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="$globals.js("/js/Main.vjs","",$text)"></script>
<script type="text/javascript" src="/js/setTime.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/formtab.js"></script>
<script type="text/javascript" src="/js/dialog/artDialog.source.js"></script>
<script type="text/javascript" src="/js/dialog/plugins/iframeTools.source.js"></script>  
<script type="text/javascript" src="/js/ui/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script type="text/javascript" src="/js/scrollgrid.js"></script>
<script type="text/javascript">

var operation = $globals.getOP("OP_UPDATE_PREPARE");
var minValue = -9999999999;
var maxValue = 9999999999;
#foreach($item in $!isItem)
	#set($itemobj = $!globals.get($!globals.strSplit($!item,';'),1))
	#set($itemPop = $!globals.get($!globals.strSplit($!item,';'),3))
	#set($itemName = $!globals.get($!globals.strSplit($!item,';'),0))
	#set($iName = "$!{itemobj}Name")
	
	putValidateItem("$!itemobj",'$itemName',"any","",false,minValue,maxValue,true);	
#end
#if($!currList.size()==0)
 putValidateItem("BeginAmount",'本币年初值',"double","",false,minValue,maxValue,true);
 putValidateItem("TotalDebit",'本币本年累计借',"double","",false,minValue,maxValue,true);
 putValidateItem("TotalLend",'本币本年累计贷',"double","",false,minValue,maxValue,true);
 putValidateItem("TotalRemain",'本币余额',"double","",false,minValue,maxValue,true);
#else

 putValidateItem("CurrencyRate",'外币汇率',"double","",false,minValue,maxValue,true);
 putValidateItem("CurBeginAmount",'外币年初值',"double","",false,minValue,maxValue,true);
 putValidateItem("CurTotalDebit",'外币本年累计借',"double","",false,minValue,maxValue,true);
 putValidateItem("CurTotalLend",'外币本年累计贷',"double","",false,minValue,maxValue,true);
 putValidateItem("CurTotalRemain",'外币余额',"double","",false,minValue,maxValue,true);
#end	
					
function beforSubmit(form){ 

	#if($!currList.size()==0)
	 if(!validateDigit("BeginAmount",'本币年初值',$globals.getDigits(),true))return false;	
	 if(!validateDigit("TotalDebit",'本币本年累计借',$globals.getDigits(),true))return false;
	 if(!validateDigit("TotalLend",'本币本年累计贷',$globals.getDigits(),true))return false;
	 if(!validateDigit("TotalRemain",'本币余额',$globals.getDigits(),true))return false;
	#else
	 if(!validateDigit("CurrencyRate",'外币年初值',$globals.getDigits(),true))return false;	
	 if(!validateDigit("CurBeginAmount",'外币年初值',$globals.getDigits(),true))return false;	
	 if(!validateDigit("CurTotalDebit",'外币本年累计借',$globals.getDigits(),true))return false;
	 if(!validateDigit("CurTotalLend",'外币本年累计贷',$globals.getDigits(),true))return false;
	 if(!validateDigit("CurTotalRemain",'外币余额',$globals.getDigits(),true))return false;
	#end	

	submitBefore = false ;
	if(!validate(form))return false; 
	
	disableForm(form);
	
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	if(typeof(operation) != "undefined"){
		form.operation.value= operation;
	}
	#if("$!accMap.get('id')" !="")
	form.opType.value= "updateIniItem";
	#else
	form.opType.value= "addIniItem";
	#end 
	return true;
}

function m(value){
	return document.getElementById(value) ;
}
function n(value){
	if(document.getElementsByName(value).length==0){
		return document.getElementsByName("conver");
	}
	return document.getElementsByName(value) ;
}
 

jQuery(document).ready(function(){
	jQuery("#listRange_mainField :text").each(function(){
	this.title=this.value;});
});
var moduleId = "$LoginBean.getOperationMap().get("/IniAccQueryAction.do").moduleId";

/* 弹出框选择（外币，核算项） */
var objs;
var hideValues;
var showValues;
var popNames;
function popSelect(obj,popName,hideValue,showValue,name){
	var displayName=encodeURI(name) ;
	if(("$!accNumber"=="1122" || "$!accNumber"=="1123" 
		|| "$!accNumber"=="2202" || "$!accNumber"=="2203") && (popName=="SelectIniClient" || popName=="SelectIniProvider")){
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
	asyncbox.open({id:'Popdiv',title:name,url:urlstr,width:700,height:350})
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
	if(("$!accNumber"=="1122" || "$!accNumber"=="1123" 
		|| "$!accNumber"=="2202" || "$!accNumber"=="2203") && (popName=="SelectIniClient" || popName=="SelectIniProvider")){
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
		if(("$!accNumber"=="1122" || "$!accNumber"=="1123" 
			|| "$!accNumber"=="2202" || "$!accNumber"=="2203") && (popNames=="SelectIniClientCheck" || popNames=="SelectIniProviderCheck")){
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

function countMoney(obj,type){
	$(obj).val(f(Number($(obj).val()),$digits));
	if(type=="base"){
		$("#TotalRemain").val(f(Number($("#BeginAmount").val())+Number($("#TotalDebit").val())-Number($("#TotalLend").val()),$digits));
	}else{
		$("#CurTotalRemain").val(f(Number($("#CurBeginAmount").val())+Number($("#CurTotalDebit").val())-Number($("#CurTotalLend").val()),$digits));
	}
}
</script>
</head>

<body scroll="no"  onKeyDown="down();">

<iframe name="formFrame" style="display:none"></iframe>
<!--  button onclick="document.form.Remark.value=document.body.innerHTML">ddd</button>-->
<form  method="post" onKeyDown="down()" scope="request" id="form" name="form" action="/IniAccQueryAction.do" onSubmit="return beforSubmit(this);" target="formFrame"> 
<input type="hidden" id="conver" name="conver" />
<input type="hidden" id="operation" name="operation" value="$globals.getOP("OP_UPDATE")" />
<input type="hidden" id="org.apache.struts.taglib.html.TOKEN" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()" />
<input type="hidden" id="opType" name="opType" />
<input type="hidden" id="id" name="id" value="$!accMap.get("id")" />
<input type="hidden" id="impId" name="impId" value="$!accMap.get("impId")" />
<input type="hidden" id="accCode" name="accCode" value="$accCode" />
<input type="hidden" id="accNumber" name="accNumber" value="$accNumber" />
<input type="hidden" id="accNumber" name="accName" value="$accName" />
<input type="hidden" id="popWinName" name="popWinName" value="$!popWinName"/>

<div class="Heading">
	
	<div class="HeadingTitle">
		<b class="icons"></b>
		期初明细 
	#if("$!accMap.get('id')" !="")
	修改
	#else
	添加
	#end 	
	</div>
	
	<ul class="HeadingButton f-head-u">	
		<li><span class="btn btn-small" onclick="if(beforSubmit(document.form)) { window.save=true; document.form.submit();}">确定</span></li>
		<li><span class="btn btn-small"  id="backList"  name="backList" onClick="closeWindows('$!popWinName');">$text.get("common.lb.close")</span></li>
	</ul>
</div>

	<div id="listRange_id" style="position:relative;">
		<div class="wrapInside">
			<div class="listRange_1" id="listRange_mainField">
				<ul class="wp_ul">
				#foreach($item in $!isItem)
					#set($itemobj = $!globals.get($!globals.strSplit($!item,';'),1))
					#set($itemPop = $!globals.get($!globals.strSplit($!item,';'),3))
					#set($itemName = $!globals.get($!globals.strSplit($!item,';'),0))
					#set($iName = "$!{itemobj}Name")
					<li><div class="swa_c1"  title='$itemName'><div class="d_box"><div class="d_test">$itemName</div></div><div class="d_mh"></div></div>
					<input type="hidden" name="$!itemobj" id="$!itemobj" value="$!accMap.get("$!itemobj")" />					
					<div class="swa_c2"> <input type="text" name="$!{itemobj}Name" id="$!{itemobj}Name" value="$!accMap.get("$!{itemobj}Name")"  size="20" class="input_type" 
						ondblclick="popSelect(this,'$!itemPop','$!itemobj','$!{itemobj}Name','选择$itemName')"
						onkeyup="popSelect2(this,'$!{itemPop}','$!{itemobj}','$!{itemobj}Name','选择$!itemName')"
						onblur="cl(this);" disableautocomplete="true" autocomplete="off">
						<b class="stBtn icon16" onclick="popSelect(this,'$!itemPop','$!itemobj','$!{itemobj}Name','选择$itemName')"></b></div></li>
				#end
				#if($!currList.size()==0)
				<li><div class="swa_c1"  ><div class="d_box"><div class="d_test">$globals.getFieldDisplay("tblIniAccDet.BeginAmount")</div></div><div class="d_mh"></div></div>
					<div class="swa_c2"> <input id="BeginAmount" name="BeginAmount" type="text" size="20" class="input_type" value="$!accMap.get("BeginAmount")"  onblur="countMoney(this,'base')" disableautocomplete="true" autocomplete="off"></div></li>
				<li><div class="swa_c1" ><div class="d_box"><div class="d_test">$globals.getFieldDisplay("tblIniAccDet.TotalDebit")</div></div><div class="d_mh"></div></div>
					<div class="swa_c2"> <input id="TotalDebit" name="TotalDebit" type="text" size="20" class="input_type" value="$!accMap.get("TotalDebit")" onblur="countMoney(this,'base')" disableautocomplete="true" autocomplete="off"></div></li>
				<li><div class="swa_c1" ><div class="d_box"><div class="d_test">$globals.getFieldDisplay("tblIniAccDet.TotalLend")</div></div><div class="d_mh"></div></div>
					<div class="swa_c2"> <input id="TotalLend" name="TotalLend" type="text" size="20" class="input_type" value="$!accMap.get("TotalLend")" onblur="countMoney(this,'base')" disableautocomplete="true" autocomplete="off"></div></li>
				<li><div class="swa_c1"  ><div class="d_box"><div class="d_test">$globals.getFieldDisplay("tblIniAccDet.TotalRemain")</div></div><div class="d_mh"></div></div>
					<div class="swa_c2"> <input id="TotalRemain" name="TotalRemain" type="text" size="20" class="input_type" value="$!accMap.get("TotalRemain")" onblur="countMoney(this,'base')" disableautocomplete="true" autocomplete="off"></div></li>	
				#else
				<li><div class="swa_c1"  ><div class="d_box"><div class="d_test">$globals.getFieldDisplay("tblIniAccDet.Currency")</div></div><div class="d_mh"></div></div>
					<div class="swa_c2"> 
						<select id="Currency" name="Currency" #if("$isFlag"=="true" ) disabled="disabled" #end>
							#foreach($curr in $currList)
								<option value="$!globals.get($!curr,0)" #if("$!accMap.get('Currency')"=="$!globals.get($!curr,0)") selected #end>$!globals.get($!curr,1)</option>
							#end
						</select></div></li>
				<li><div class="swa_c1"  ><div class="d_box"><div class="d_test">$globals.getFieldDisplay("tblIniAccDet.CurrencyRate")</div></div><div class="d_mh"></div></div>
					<div class="swa_c2"> <input id="CurrencyRate" name="CurrencyRate" type="text" size="20" class="input_type" #if("$!accMap.get('CurrencyRate')"=="") value="1" #else value="$!accMap.get("CurrencyRate")" #end disableautocomplete="true" autocomplete="off"></div></li>
				<li><div class="swa_c1"  ><div class="d_box"><div class="d_test">$globals.getFieldDisplay("tblIniAccDet.CurBeginAmount")</div></div><div class="d_mh"></div></div>
					<div class="swa_c2"> <input id="CurBeginAmount" name="CurBeginAmount" type="text" size="20" class="input_type" value="$!accMap.get("CurBeginAmount")" onblur="countMoney(this,'curr')" disableautocomplete="true" autocomplete="off"></div></li>
				<li><div class="swa_c1"  ><div class="d_box"><div class="d_test">$globals.getFieldDisplay("tblIniAccDet.CurTotalDebit")</div></div><div class="d_mh"></div></div>
					<div class="swa_c2"> <input id="CurTotalDebit" name="CurTotalDebit" type="text" size="20" class="input_type" value="$!accMap.get("CurTotalDebit")" onblur="countMoney(this,'curr')" disableautocomplete="true" autocomplete="off"></div></li>
				<li><div class="swa_c1"  ><div class="d_box"><div class="d_test">$globals.getFieldDisplay("tblIniAccDet.CurTotalLend")</div></div><div class="d_mh"></div></div>
					<div class="swa_c2"> <input id="CurTotalLend" name="CurTotalLend" type="text" size="20" class="input_type" value="$!accMap.get("CurTotalLend")" onblur="countMoney(this,'curr')" disableautocomplete="true" autocomplete="off"></div></li>
				<li><div class="swa_c1"  ><div class="d_box"><div class="d_test">$globals.getFieldDisplay("tblIniAccDet.CurTotalRemain")</div></div><div class="d_mh"></div></div>
					<div class="swa_c2"> <input id="CurTotalRemain" name="CurTotalRemain" type="text" size="20" class="input_type" value="$!accMap.get("CurTotalRemain")" onblur="countMoney(this,'curr')" disableautocomplete="true" autocomplete="off"></div></li>	
				#end						
					<div style="clear:both;"></div>
				</ul>				
			</div>
			<div id="listRange_remark"><div class="listRange_1_photoAndDoc_1"><span>$globals.getFieldDisplay("tblIniAccDet.Remark")： </span>
<textarea id="Remark" name="Remark" rows="8">$!accMap.get("Remark")</textarea>
</div>	
 </div>
		</div>
	</div>
</form> 


	<script type="text/javascript"> 
		var oDiv2=m("listRange_id");		
		var sHeight2=document.documentElement.clientHeight-40;		
		oDiv2.style.height=sHeight2+"px";		
	</script>
<script>	
	//在文字后加空格，chrome下实现两端对齐  
	cyh.lableAlign(); 
	
</script>
</body>
</html>
