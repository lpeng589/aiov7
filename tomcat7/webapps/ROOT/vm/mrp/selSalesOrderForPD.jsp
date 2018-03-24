<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$reportName</title>
<link type="text/css" href="/$globals.getStylePath()/css/classFunction.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/CRselectBoxList.css" />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/listgrid.css" />
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css" />
<link type="text/css" rel="stylesheet" href="/style/css/popOperation.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" /> 
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<style type="text/css">
.HeadingTitle{white-space:nowrap;}
.scroll_function_small_a>div{background:#fff;}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.divbox.js"></script>
<script type="text/javascript" src="/js/menu/jquery.bgiframe.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>

<script type="text/javascript" src="$globals.js("/js/editPage.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/listGrid.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/function.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/k_listgrid.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/popOperation.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/dropdownselect.js","",$text)"></script>

<script language="javascript">
function gselectKeyId(obj){
	ind = jQuery(obj).index();//取得行号
	trobj = $(obj,document);
	keyi = trobj.find("input[name=keyId]");
	if(keyi.size() > 0){
		if(keyi.attr("type")=="radio"){
			keyi[0].checked = true;
		}else{		
			keyi[0].checked = !keyi[0].checked;
		}
	}
}

var MOID="$LoginBean.getOperationMap().get('/Mrp.do?type=selSalesOrderForPD').getModuleId()";
function openInputDate(obj)
{
	WdatePicker({lang:'$globals.getLocale()'});
}

//以下是下拉弹出窗功能。
function showData(inp,selectName,fieldName,display){

	var dropdown=inp.dropdown;
	if(dropdown==undefined)
	{
		if(event.keyCode==13){
			submitQuery();
			return;
		}
		
    	var data = {
    	selectName:selectName,
    	operation:"DropdownPopup",
    	MOID:MOID,
    	MOOP:"query",
    	selectField:fieldName,
    	};
		dropdown = new dropDownSelect("t_"+inp.id,data,inp,selectName);
    	dropdown.retFun = setFieldValue;
		dropdown.showData();
		return ;
	}
	if(event.keyCode == 38)
	{
		if(dropdown!=undefined)
		{
			dropdown.selectUp();
		}
		return ;
	}else if(event.keyCode==40)
	{
		if(dropdown!=undefined)
		{
			dropdown.selectDown();
		}
		return ;
	}else if(event.keyCode==13)
	{
		if(dropdown!=undefined)
		{
			dropdown.curValue();
		}
		return ;
	}else if(event.keyCode==27)
	{
		if(dropdown!=undefined)
		{
			dropdown.close();
		}
		return ;
	}
	
	dropdown.showData();
}
function setFieldValue(str,selectName,fieldName){
	var fs=str.split("#;#");
	
	if(selectName=="ReportSelectClient_v2"){
		if(fs[0]==""){
			$("#CompanyCode").val("");
		}else{
			$("#CompanyCode").val(str);
		}
		
		#set($popBean=$!globals.getPopupBean('ReportSelectClient_v2'))
		#if($popBean.saveFields.size()>0)
			#foreach($fv in $popBean.displayField)
			#if($fv.width > 0)
				document.getElementById("$fv.asName").value=fs[${velocityCount}+1];
			#end
			#end
		#end	
	}
	if(selectName=="ReportSelectGoods_v2"){
		if(fs[0]==""){
			$("#GoodsCode").val("");
		}else{
			$("#GoodsCode").val(str);
		}
		#set($popBean=$!globals.getPopupBean('ReportSelectGoods_v2'))
		#if($popBean.saveFields.size()>0)
			#foreach($fv in $popBean.displayField)
			#if($fv.width > 0)
				document.getElementById("$fv.asName").value=fs[${velocityCount}+1];
			#end
			#end
		#end
	} 
	if(selectName=="ReportSelectEmployee"){
		#set($popBean=$!globals.getPopupBean('ReportSelectEmployee'))
		#if($popBean.saveFields.size()>0)
			#foreach($fv in $popBean.saveFields)
		    document.getElementById("$fv.parentName").value=fs[${velocityCount}-1];
				#if(${velocityCount}==$popBean.saveFields.size())
			var lastObject = document.getElementById("$fv.parentName");
				#end
			#end
		#else
		document.getElementById(fieldName).value=fs[0];
		#end
	}
	if(selectName=="ReportSelectDepartment"){
		#set($popBean=$!globals.getPopupBean('ReportSelectDepartment'))
		#if($popBean.saveFields.size()>0)
			#foreach($fv in $popBean.saveFields)
		    document.getElementById("$fv.parentName").value=fs[${velocityCount}-1];
				#if(${velocityCount}==$popBean.saveFields.size())
			var lastObject = document.getElementById("$fv.parentName");
				#end
			#end
		#else
		document.getElementById(fieldName).value=fs[0];
		#end
	}
}
var hideFieldName;//存放fieldName值
var hideSelectName;//存放selectName值
function openSelect(selectName,fieldName,display){ 
	hideFieldName = fieldName;
	hideSelectName = selectName;
	var urlstr = '/UserFunctionAction.do?operation=22&src=menu&parentTableName=&moduleType=&displayName='+encodeURI(display)+'&LinkType=@URL:&tableName=&selectName='+selectName ;
	
	if(urlstr.indexOf("#")!=-1){
		urlstr=urlstr.replaceAll("#","%23") ;
    }
    if(urlstr.indexOf("+")!=-1){
		urlstr = urlstr.replaceAll("\\+",encodeURIComponent("+")) ;
    }
	linkStr="";
	
	urlstr += "&MOID="+MOID+"&MOOP=query&popupWin=Popdiv";
	asyncbox.open({id:'Popdiv',title:display,url:urlstr,width:750,height:470});
}  
function exePopdiv(returnValue){
	var str = returnValue;
	var fieldName = hideFieldName;
	var selectName = hideSelectName
	if(typeof(str)!="undefined"){
		setFieldValue(str,selectName,fieldName);
	}
}
function beforeButtonQuery(){
	return true;
}
function submitQuery(){
	window.save=true;
	document.forms[0].submit();
}
function clearCondition(){
	$("#conditionDIV").find("input[type=text]").val("");
	$("#conditionDIV").find("input:not([type])").val("");
	$("#conditionDIV").find("select").val("");
	$("#conditionDIV").find("input[belongTableName]").val("");
	
}
function cl(inp)
{
	setTimeout(function(){if(inp.dropdown != undefined)inp.dropdown.close();},200);
}
function detail(tableName,keyId){
	turl="/UserFunctionAction.do?tableName="+tableName+"&keyId="+keyId+"&operation=$globals.getOP("OP_DETAIL")";
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	openPop('PopMainOpdiv','',turl,width,height,false,height==document.documentElement.clientHeight);  
	
}
function toGoods(){
	if($("input[name=keyId]:checked").size()==0){
		alert("请选择至少一个订单或制造需求单");
		return;
	}
	window.save=true;
	document.form.action="/Mrp.do?type=showSalesOrderLack";
	document.form.submit();
}
</script>
</head>
<body style="overflow:hidden" onload="changeConfirm();">

<div id="Larger_content">
<iframe   name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" id="form"  name="form" action="/Mrp.do?type=selSalesOrderForPD"  target="">
<input type="hidden" name="saleOrders" value="">

<div class="Heading">
	<div class="HeadingTitle">
		<b class="icons b-list"></b>
		选择销售订单（然后下一步）
	</div>
	<ul class="HeadingButton f-head-u">		
		<li><span class="btn btn-small" id="toGoods" name="toGoods" onClick="toGoods();" >下一步</span></li>
		<li><span class="btn btn-small" id="toGoods" name="toGoods" onClick="closeWindows('PopMainOpdiv');" >返回</span></li>
	</ul>
</div>
<div class="listRange_frame" id="listRange_frame">
<div class="listRange_3" id="conditionDIV">
		<table><tbody><tr><td>
		<div style="height:80px;max-width:840px;float:left" id="conditionDIV2">
			<ul class="search-list-ul">	

		<li style="min-width:600px;width:75%;" class="dateQueryLi">
			<div class="swa_c1 d_4">
				<div class="d_box"><div class="d_test d_mid">单据日期</div></div>
			</div>
			<div class="swa_c2">
				<input id="date_start" class="ls3_in" style="width:105px;" name="BillDate1" date="true" value="$!BillDate1" onkeydown="if(event.keyCode==13){if(!beforeButtonQuery())return false ; submitQuery();}" onclick="openInputDate(this);" disableautocomplete="true" autocomplete="off">
				-
				<input id="date_end" class="ls3_in" style="width:105px;" name="BillDate2" date="true" value="$!BillDate2" onkeydown="if(event.keyCode==13){if(!beforeButtonQuery())return false ; submitQuery();}" onclick="openInputDate(this);" disableautocomplete="true" autocomplete="off">
				
				<a href="javascript:void(0)" class="qd_a" id="qdate_zt">昨天</a>
				<a href="javascript:void(0)" class="qd_a" id="qdate_jt">今天</a>
				<a href="javascript:void(0)" class="qd_a" id="qdate_yz">7天内</a>
				<a href="javascript:void(0)" class="qd_a" id="qdate_yy">30天内</a>
				<a href="javascript:void(0)" class="qd_a curqd_selected" id="qdate_by">本月内</a>
			</div>
		</li>
		
		<li>
			<div class="swa_c1 d_4"><div class="d_box"><div class="d_test d_mid">单 据 编 号</div></div></div>
			<div class="swa_c2">
				<input class="ls3_in" id="BillNo" name="BillNo" value="$!BillNo" onkeydown="if(event.keyCode==13) {if(!beforeButtonQuery())return false ; submitQuery();}" disableautocomplete="true" autocomplete="off">
			</div>
		</li>
		#set($vals = $CompanyCode.split("#;#"))
		<input name="CompanyCode" rid="rid_CompanyCode" id="CompanyCode" belongtablename="tblCompany" value="$!CompanyCode" type="hidden" disableautocomplete="true" autocomplete="off">
		
		#set($popBean=$!globals.getPopupBean('ReportSelectClient_v2'))
		#if($popBean.saveFields.size()>0)
			#foreach($fv in $popBean.displayField)
			#if($fv.width > 0)
				#if("$!fv.display" !="" && $!fv.display.indexOf(".")==-1)
					#set($dis = $fv.display)
				#else 
					#set($dis = $globals.getFieldDisplay($fv.fieldName)) 
				#end
				#set($col =1+${velocityCount} )
		<li>
			<div class="swa_c1 d_4"><div class="d_box"><div class="d_test d_mid">$dis</div></div></div>
			<div class="swa_c2">
				<input name="$fv.asName" id="$fv.asName"  class="ls3_in" value="$!globals.get($vals,$col)"
				 ondblclick="openSelect('ReportSelectClient_v2','$fv.asName','$dis');" 
				 onfocus="this.oldValue=this.value;this.select();" 
				 onkeyup="showData(this,'ReportSelectClient_v2','$fv.asName','$dis');" 
				 onblur="cl(this);" disableautocomplete="true" autocomplete="off">
				<b class="stBtn icon16" onclick="openSelect('ReportSelectClient_v2','$fv.asName','$dis');"></b>
			</div>
		</li>
			#end
			#end
		#end
		
	<div class="clear"></div>
	</ul>
	</div>
	</td><td style="width: 200px;vertical-align: top;">		
			<ul class="floatleft b-btns-ul">
	            <li style="float:none">
	            	<span id="btnConfirmSCA" onClick="submitQuery()" class="floatleft more-txt">查询</span>
	            	<span id="btnClearSCA" onClick="clearCondition()" class="floatleft more-txt" style="border-radius: 0 3px 3px 0;">清空</span>
	            </li>
	       	</ul>
	       	<div class="clear"></div>
	</td></tr></tbody></table>	       	
	       	
</div>

<div class="scroll_function_small_a" id="conter" style="overflow:hidden" >
<div style="width:100%;height:100%;overflow-x:hidden;overflow:hidden;position:relative">
	<div id="k_head" style="z-index: 50; overflow: hidden; width: 1000px; position: absolute; top: 0px; left: 0px;">
	<table id="k_listgrid" style="table-layout:fixed" border="0" cellspacing="0" width="900">
		<thead>
			<tr height="24">
				<td width="35">No.</td> 
				<td width="24"><input type="checkbox" onclick="jQuery('input[name=\'keyId\']').attr('checked',this.checked);" checked disableautocomplete="true" autocomplete="off"></td>
				<td width="80">单据日期</td>
				<td width="100">单据编号</td>
				#foreach($row in $ComFList)
				<td width="$globals.get($row,2)">客户$globals.get($row,1)</td>
				#end
				<td width="80">回复交期</td>
				<td width="80">制单人</td>
			</tr>
		</thead>
	</table>
	</div>
	<div id="k_data" style="z-index: 40; width: 100%; overflow: auto; position: absolute; top: 0px; left: 0px; height: 354px;">
	<table id="k_listgrid" style="table-layout:fixed" border="0" cellspacing="0" width="900">
		<thead>
			<tr height="24">
				<td width="35">No.</td>
				<td width="24"><input type="checkbox" onclick="jQuery('input[name=\'keyId\']').attr('checked',this.checked);" checked disableautocomplete="true" autocomplete="off"></td>
				<td width="80">单据日期</td>
				<td width="100">单据编号</td>
				#foreach($row in $ComFList)
				<td width="$globals.get($row,2)">客户$globals.get($row,1)</td>
				#end
				<td width="80">回复交期</td>
				<td width="80">制单人</td>
			</tr>
		</thead>
		<tbody>
			#foreach($srow in $salesList)
			<tr height="28px" onclick="gselectKeyId(this)" class="spaceRow">
				<td>$velocityCount</td>
				<td align="center"><input type="checkbox" value="$srow.get("id")"   name="keyId"  checked onclick="this.checked=!this.checked" disableautocomplete="true" autocomplete="off"></td>
				<td width="80">$srow.get("BillDate")</td>
				<td width="100">$srow.get("BillNo")</td>
				#foreach($row in $ComFList)
				<td width="$globals.get($row,2)">$srow.get($globals.get($row,0))</td>
				#end
				
				<td width="80">$srow.ReplyDate</td>
				<td width="80">$srow.EmpFullName</td>
			</tr>
			#end
		</tbody>
	</table>
	</div> 
</div>
</div>
<script language="javascript">
	
var condHeight= 80;

var oDiv=document.getElementById("conter");
var dHeight=document.documentElement.clientHeight;
oDiv.style.height=dHeight-55-condHeight +"px";

$("#k_data").height($("#conter").height()-10);
$("#k_head").width($("#k_data")[0].clientWidth);
jQuery("#kt_head").width(jQuery("#k_column>table").outerWidth(true));
jQuery("#k_data tbody tr").click(function(){jQuery(this).toggleClass("highlightrow");});

$("#k_data").scroll(function () { 
	$("#k_head").scrollLeft($("#k_data").scrollLeft()); 
}); 


</script>
		
</div>	

</form>
</div>
</body>
</html>
