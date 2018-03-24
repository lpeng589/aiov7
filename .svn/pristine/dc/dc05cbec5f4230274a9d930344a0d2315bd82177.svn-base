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
	/**
	trobj = $(obj,document);
	keyi = trobj.find("input[name=keyId]");
	if(keyi.size() > 0){
		if(keyi.attr("type")=="radio"){
			keyi[0].checked = true;
		}else{		
			keyi[0].checked = !keyi[0].checked;
		}
	}*/
}

var MOID="$LoginBean.getOperationMap().get('/Mrp.do').getModuleId()";
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
		#set($popBean=$!globals.getPopupBean('ReportSelectClient_v2'))
		document.getElementById("CompanyCode").value=str;
		#foreach($fv in $popBean.returnFields) 
			#if("$fv.parentName" != "CompanyCode" && "$fv.parentName" != "isCatalog")
				#set($fn = $fv.parentName )
				#if($fv.type == 0)
					#set($fn = $fv.asName )
				#end	
			    document.getElementById("$fn").value='';
			#end
		#end
		fo=str.split("#|#");
		for(var i=0;i<fo.length;i++){
			var fs = fo[i].split("#;#");
			#foreach($fv in $popBean.returnFields)
				#if("$fv.parentName" != "CompanyCode" && "$fv.parentName" != "isCatalog")
					#set($fn = $fv.parentName )
					#if($fv.type == 0)
						#set($fn = $fv.asName )
					#end	
				    document.getElementById("$fn").value =document.getElementById("$fn").value + fs[${velocityCount}-1]+",";
				#end
			#end
		}
	}
	
	if(selectName=="ReportSelectGoods_v2"){
		#set($popBean=$!globals.getPopupBean('ReportSelectGoods_v2'))
		document.getElementById("GoodsCode").value=str; 
		#foreach($fv in $popBean.returnFields) 
			#if("$fv.parentName" != "GoodsCode" && "$fv.parentName" != "isCatalog")
				#set($fn = $fv.parentName )
				#if($fv.type == 0)
					#set($fn = $fv.asName )
				#end	
				if(document.getElementById("$fn") != undefined)
			    	document.getElementById("$fn").value='';
			#end
		#end
		fo=str.split("#|#");
		for(var i=0;i<fo.length;i++){
			var fs = fo[i].split("#;#");
			#foreach($fv in $popBean.returnFields)
				#if("$fv.parentName" != "GoodsCode" && "$fv.parentName" != "isCatalog")
					#set($fn = $fv.parentName )
					#if($fv.type == 0)
						#set($fn = $fv.asName )
					#end	
					if(document.getElementById("$fn") != undefined)
				    	document.getElementById("$fn").value =document.getElementById("$fn").value + fs[${velocityCount}-1]+",";
				#end
			#end
		}
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
function toCompute(){
	if($("input[name=keyId]:checked").size()==0){
		alert("请选择至少一个订单或制造需求单");
		return;
	}
	var saveValues=new Array();
	var error = "";
	$("input[name=keyId]:checked").each(function(){
		var pos = $("input[name=keyId]").index(this);
		var NeedQty= $("input[name=NeedQty]:eq("+pos+")").val();
		if(NeedQty<=0){
			error= $(this).attr("BillNo") + "不能小于等于0";
		}
		
		saveValues[saveValues.length] = {BillId:$(this).val(),BillType:$(this).attr("BillType"),CreateTime:$(this).attr("CreateTime"),GoodsCode:$(this).attr("GoodsCode"),NeedQty:NeedQty};
	});
	if(error != ""){
		alert(error);
		return ;
	}
	var postdata = JSON.stringify(saveValues); //生成所有需提交数据的json字符串
	if(typeof(top.jblockUI)!="undefined" ){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	jQuery.post("/Mrp.do?type=computeMRP",{data:postdata},function(json){ 
		if(json.code == "ERROR"){
			alert(json.msg);
		}else{
			alert("计算成功,现在进入原材料请购");
			changeConfirm();
			window.location.href="/Mrp.do?type=showBuyAppication";
		}
		
		if(typeof(top.jblockUI)!="undefined" ){
			top.junblockUI()
		}
	},"json" ); 
}
function toBuy(){
	window.location.href="/Mrp.do?type=showBuyAppication";
}
</script>
</head>
<body style="overflow:hidden" onload="changeConfirm();">

<div id="Larger_content">
<iframe   name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" id="form"  name="form" action="/Mrp.do?type=showSalesOrder"  target="">


<div class="Heading">
	<div class="HeadingTitle">
		<b class="icons b-list"></b>
		物料需求计算
	</div>
	<ul class="HeadingButton f-head-u">		
		<li><span class="btn btn-small" id="toGoods" name="toGoods" onClick="toCompute();" >开始运算</span></li>
		<li><span class="btn btn-small" id="toGoods" name="toGoods" onClick="toBuy();" >物料请购</span></li>
	</ul>
</div>
<div class="listRange_frame" id="listRange_frame">
<div class="listRange_3" id="conditionDIV">
		<table><tbody><tr><td>
		<div style="max-width:840px;float:left" id="conditionDIV2">
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
			<div class="swa_c1 d_4"><div class="d_box"><div class="d_test d_mid">单据类型</div></div></div>
			<div class="swa_c2">
				<select class="ls3_in" id="BillType" name="BillType" onkeydown="if(event.keyCode==13){if(!beforeButtonQuery())return false ; submitQuery();}">
					<option title="全部" value="" selected="">全部</option>
					<option title="正常" #if("$!BillType"=="tblSalesOrder") selected #end value="tblSalesOrder">销售订单</option>
					<option title="草稿" #if("$!BillType"=="PDProduceRequire") selected #end  value="PDProduceRequire">制造需求单</option>
				</select>
			</div>
		</li>
		<li>
			<div class="swa_c1 d_4"><div class="d_box"><div class="d_test d_mid">单 据 编 号</div></div></div>
			<div class="swa_c2">
				<input class="ls3_in" id="BillNo" name="BillNo" value="$!BillNo" onkeydown="if(event.keyCode==13) {if(!beforeButtonQuery())return false ; submitQuery();}" disableautocomplete="true" autocomplete="off">
			</div>
		</li>
		<input name="CompanyCode" rid="rid_CompanyCode" id="CompanyCode" belongtablename="tblCompany" value="$!CompanyCode" type="hidden" disableautocomplete="true" autocomplete="off">
		<li>
			<div class="swa_c1 d_4"><div class="d_box"><div class="d_test d_mid">客 户 编 号</div></div></div>
			<div class="swa_c2">
				<input name="tblCompany.ComNumber" id="tblCompany.ComNumber" rid="rid_CompanyCode" class="ls3_in" value="$!ComNumber" belongtablename="tblCompany" ondblclick="openSelect('ReportSelectClient_v2','tblCompany.ComNumber','客户');" onfocus="this.oldValue=this.value;this.select();" onkeyup="showData(this,'ReportSelectClient_v2','tblCompany.ComNumber','客户');" onblur="cl(this);" disableautocomplete="true" autocomplete="off">
				<b class="stBtn icon16" onclick="openSelect('ReportSelectClient_v2','tblCompany.ComNumber','客户');"></b>
			</div>
		</li>
		<li>
			<div class="swa_c1 d_4"><div class="d_box"><div class="d_test d_mid">客 户 简 称</div></div></div>
			<div class="swa_c2">
				<input name="tblCompany.ComName" id="tblCompany.ComName" rid="rid_CompanyCode" class="ls3_in" value="$!ComName" belongtablename="tblCompany" ondblclick="openSelect('ReportSelectClient_v2','tblCompany.ComName','客户');" onfocus="this.oldValue=this.value;this.select();" onkeyup="showData(this,'ReportSelectClient_v2','tblCompany.ComName','客户');" onblur="cl(this);" disableautocomplete="true" autocomplete="off">
				<b class="stBtn icon16" onclick="openSelect('ReportSelectClient_v2','tblCompany.ComName','客户');"></b>
			</div>
		</li>
		<input name="GoodsCode" rid="rid_GoodsCode" id="GoodsCode" belongtablename="tblGoods" value="$!GoodsCode" type="hidden" disableautocomplete="true" autocomplete="off">
		<li>
			<div class="swa_c1 d_4"><div class="d_box"><div class="d_test d_mid">料 件 编 号</div></div></div>
			<div class="swa_c2">
				<input name="tblGoods.GoodsNumber" id="tblGoods.GoodsNumber" rid="rid_GoodsCode" class="ls3_in" value="$!GoodsNumber" belongtablename="tblGoods" ondblclick="openSelect('ReportSelectGoods_v2','tblGoods.GoodsNumber','商品名称');" onfocus="this.oldValue=this.value;this.select();" onkeyup="showData(this,'ReportSelectGoods_v2','tblGoods.GoodsNumber','商品名称');" onblur="cl(this);" disableautocomplete="true" autocomplete="off">
				<b class="stBtn icon16" onclick="openSelect('ReportSelectGoods_v2','tblGoods.GoodsNumber','商品名称');"></b>
			</div>
		</li>
		<li>
			<div class="swa_c1 d_4"><div class="d_box"><div class="d_test d_mid">料 件 名 称</div></div></div>
			<div class="swa_c2">
				<input name="tblGoods.GoodsFullName" id="tblGoods.GoodsFullName" rid="rid_GoodsCode" class="ls3_in" value="$!GoodsFullName" belongtablename="tblGoods" ondblclick="openSelect('ReportSelectGoods_v2','tblGoods.GoodsFullName','商品名称');" onfocus="this.oldValue=this.value;this.select();" onkeyup="showData(this,'ReportSelectGoods_v2','tblGoods.GoodsFullName','商品名称');" onblur="cl(this);" disableautocomplete="true" autocomplete="off">
				<b class="stBtn icon16" onclick="openSelect('ReportSelectGoods_v2','tblGoods.GoodsFullName','商品名称');"></b>
			</div>
		</li>
		<li>
			<div class="swa_c1 d_4"><div class="d_box"><div class="d_test d_mid">规 格</div></div></div>
			<div class="swa_c2">
				<input name="tblGoods.GoodsSpec" id="tblGoods.GoodsSpec" rid="rid_GoodsCode" class="ls3_in" value="$!GoodsSpec" belongtablename="tblGoods" ondblclick="openSelect('ReportSelectGoods_v2','tblGoods.GoodsSpec','商品名称');" onfocus="this.oldValue=this.value;this.select();" onkeyup="showData(this,'ReportSelectGoods_v2','tblGoods.GoodsSpec','商品名称');" onblur="cl(this);" disableautocomplete="true" autocomplete="off">
				<b class="stBtn icon16" onclick="openSelect('ReportSelectGoods_v2','tblGoods.GoodsSpec','商品名称');"></b>
			</div>
		</li>
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
				<td width="24"><input type="checkbox" onclick="jQuery('input[name=\'keyId\']').attr('checked',this.checked);" disableautocomplete="true" autocomplete="off"></td>
				<td width="100">单据类型</td>
				<td width="80">单据数量</td>
				<td width="80">已运算数量</td>
				<td width="80">本次运算数量</td>
				<td width="100">单据编号</td>
				<td width="80">单据日期</td>
				<!--<td width="80">部门</td>-->
				<!--<td width="80">经手人</td>-->
				#foreach($rf in $ComFList)
					<td width="$globals.get($rf,2)">$globals.get($rf,1)</td>
				#end
				<!--<td width="80">制单人</td>-->
				#foreach($rf in $GoodsFList)
					<td width="$globals.get($rf,2)">$globals.get($rf,1)</td>
				#end
			</tr>
		</thead>
	</table>
	</div>
	<div id="k_data" style="z-index: 40; width: 100%; overflow: auto; position: absolute; top: 0px; left: 0px; height: 354px;">
	<table id="k_listgrid" style="table-layout:fixed" border="0" cellspacing="0" width="900">
		<thead>
			<tr height="24">
				<td width="35">No.</td>
				<td width="24"><input type="checkbox" onclick="jQuery('input[name=\'keyId\']').attr('checked',this.checked);" disableautocomplete="true" autocomplete="off"></td>
				<td width="100">单据类型</td>
				<td width="80">单据数量</td>
				<td width="80">已运算数量</td>
				<td width="80">本次运算数量</td>
				<td width="100">单据编号</td>
				<td width="80">单据日期</td>
				<!--<td width="80">部门</td>-->
				<!--<td width="80">经手人</td>-->
				#foreach($rf in $ComFList)
					<td width="$globals.get($rf,2)">$globals.get($rf,1)</td>
				#end
				<!--<td width="80">制单人</td>-->
				#foreach($rf in $GoodsFList)
					<td width="$globals.get($rf,2)">$globals.get($rf,1)</td>
				#end
			</tr>
		</thead>
		<tbody>
			#foreach($row in $salesList)
			<tr height="28px" onclick="gselectKeyId(this)" class="spaceRow">
				<td>$velocityCount</td>
				<td align="center"><input type="checkbox" value="$!row.get("BillId")" BillNo="$!row.get("BillNo")" BillType="$!row.get("BillType")" CreateTime="$!row.get("CreateTime")" GoodsCode="$!row.get("GoodsCode")"  checked name="keyId"  disableautocomplete="true" autocomplete="off"></td>
				<td align="left">#if($!row.get("BillType")=="PDProduceRequire")制造需求单#else 销售订单 #end</td>
				<td align="right">$globals.formatNumberS($!row.get("Qty"),false,true,'','tblSalesOrderDet.Qty')</td>
				<td align="right">$globals.formatNumberS($!row.get("MRPQty"),false,true,'','tblSalesOrderDet.Qty')</td>
				<td align="right"><input class="ls3_in" style="width:80px;" name="NeedQty" value="$globals.formatNumberS($!row.get("NeedQty"),false,false,'','tblSalesOrderDet.Qty')" disableautocomplete="true" autocomplete="off" ></td>
				<td align="left"><span style="color:blue;cursor:pointer" onclick="detail('$!row.get("BillType")','$!row.get("BillId")')">$!row.get("BillNo")</span> </td>
				<td align="left">$!row.get("BillDate")</td>
				<!--<td align="left">$!row.get("DeptFullName")</td>-->
				<!-- <td align="center">$!row.get("EmpFullName")</td>-->
				#foreach($rf in $ComFList)
					<td align="left">$!row.get($globals.get($rf,0))</td>
				#end
				<!--<td align="center">$!row.get("createBy")</td>-->
				#foreach($rf in $GoodsFList)
					<td align="left">$!row.get($globals.get($rf,0))</td>
				#end
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
