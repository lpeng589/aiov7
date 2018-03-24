<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("muduleFlow.lb.voucherlogin")</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<style type="text/css">
.TopSharing_button{width:auto;}
.alignDiv{align: center;}
.mains{padding: 20px 0px 15px 0px;width:90%; border: #dedede 1px solid;}
.btnlp{margin:6px;float: left;display:inline-block;cursor: pointer;}
.oa_signDocument1{padding-left: 20px;}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$("#save").bind("click",function(){
			//启用审核
			if($("#isAuditing").is(":checked")){
				if($("#auditingPersont").val()==""){
					alert("请选择审核人");
					return false;
				}
				if($("#reverseAuditing").val()==""){
					alert("请选择反审核人");
					return false;
				}
				if($("#binderPersont").val()==""){
					alert("请选择过账人");
					return false;
				}
				if($("#reverseBinder").val()==""){
					alert("请选择反过账人");
					return false;
				}
				$("#isAuditing").val('1');
			}else{
				$("#isCheck").val(0);
				$("#isAccountAuditing").val(0);
			}
			//启用凭证过账前必须出纳复核


			if($("#isCheck").is(":checked")){
				if($("#checkPersont").val()==""){
					alert("请选择复核人");
					return false;
				}
			}
			//启用指定现金流


			if($("#isCash").is(":checked")){
				if($("#cashPersont").val()==""){
					alert("请选择现金流量人");
					return false;
				}
			}
			//启用凭证过账前必须审核


			if(!$("#isAccountAuditing").is(":checked")){
				if($("#auditingPersont").val()==""){
					alert("请选择审核人");
					return false;
				}
			}
			form.submit();
		});
		$("#refur").bind("click",function(){
			window.location.reload();
		});
		$("#closes").bind("click",function(){
			closeWin();
		});
	});
	
	/* 人员弹出框*/
	var fieldNames = "";
	var fieldNameIds = ""
	function openPop(fieldName,fieldNameId,displayName){
		var urlstr = '/Accredit.do?popname=userGroup&inputType=checkbox';
		asyncbox.open({id:'Popdiv',title:displayName,url:urlstr,width:755,height:430,btnsbar:jQuery.btn.OKCANCEL,callback:function(action,opener){
			if(action == 'ok'){
				newOpenSelect(opener.strData,fieldName,fieldNameIds)
			}
		}})
		fieldNames = fieldName;
		fieldNameIds = fieldNameId;
	}
	
	function fillData(datas){	
		newOpenSelect(datas,fieldNames,fieldNameIds);
		jQuery.close('Popdiv');
	}

	/* 给文本框赋值*/
	function newOpenSelect(datas,fieldName,fieldNameIds){
		var datas = datas.split("|") ;
		for(var j=0;j<datas.length;j++){
			var field = datas[j].split(";");
			if(field!=""){
				var existOption = getValueById(fieldName).options;
				var length = existOption.length;
				var talg = false ;
				for(var i=0;i<length;i++){
					if(existOption[i].value==field[0]){
						talg = true;
					}
				}
				if(!talg){
					getValueById(fieldName).options.add(new Option(field[1],field[0]));
					getValueById(fieldNameIds).value+=field[0]+",";
				}
			}
		}
	}
 
	function getValueById(id){
		return document.getElementById(id);
	}
	
	function delOpation(formatNames,popedomIds){
		var formatName=document.getElementById(formatNames);
		var popedomId=document.getElementById(popedomIds);
		if(formatName.selectedIndex==-1){
			alert("$!text.get('oa.lb.common.pleaseChoose.remove')") ;
		}
		if(formatName.selectedIndex<0)return false ;
		var value = formatName.options[formatName.selectedIndex].value;
		var appcers = popedomId.value;
		appcers = appcers.replace(value+",","");
		popedomId.value =appcers;
		formatName.options.remove(formatName.selectedIndex);
	}
	
	function showGroup(){
		if($("#isAuditing").is(":checked")){
			$("#_context1").show();
			$("#_context2").show();
			$("#_context3").show();
			$("#_context4").show();
			#if("$!globals.getSysSetting('standardAcc')"=="true")
			$("#_context5").show();
			#end
			$("#isCheckspan").show();
			$("#isAccountspan").show();
		}else{
			$("#_context1").hide();
			$("#_context2").hide();
			$("#_context3").hide();
			$("#_context4").hide();
			#if("$!globals.getSysSetting('standardAcc')"=="true")
			$("#_context5").hide();
			#end
			$("#isCheckspan").hide();
			$("#isAccountspan").hide();
		}
	}

</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body style="background: #f5f5f5;" onload="showGroup()">
<form id="form" name="form" method="post" action="/VoucherSettingAction.do" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")"/>
<input type="hidden" name="tableName" id="tableName" value="tblAccMainSetting" />
<input type="hidden" name="id" id="id" value="$!setting.id"/>
<input type="hidden" name="auditingPersont" id="auditingPersont" value="$!setting.auditingPersont"/>
<input type="hidden" name="reverseAuditing" id="reverseAuditing" value="$!setting.reverseAuditing"/>
<input type="hidden" name="binderPersont" id="binderPersont" value="$!setting.binderPersont"/>
<input type="hidden" name="reverseBinder" id="reverseBinder" value="$!setting.reverseBinder"/>
<input type="hidden" name="checkPersont" id="checkPersont" value="$!setting.checkPersont"/>
<input type="hidden" name="cashPersont" id="cashPersont" value="$!setting.cashPersont"/>

<div style="width:90%;margin:0 auto;padding:5px 0 5px 0;overflow:hidden;">
	<div style="float:left;display:inline-block;">
		<img src="/style/images/desktop/msg_icon_3.jpg" style="float: left;"/>
		<h3 style="font: bold 14px '微软雅黑';float:left;display:inline-block;line-height:32px;">凭证设置</h3>
	</div>
	<div class="module_list f_l" id="btns" style="float:right;display:inline-block;">
			<div class="btnlp"><img src="/style/themes/icons/filesave.png"/><a id="save">保存</a></div>
			<div class="btnlp"><img src="/style/themes/icons/reload.png"/><a id="refur">刷新</a></div>
			<div class="btnlp"><img src="/style/images/delete.gif"/><a id="closes">$text.get("common.lb.close")</a></div>
	</div>
</div>
<div class="c_main winw1200 f_l mains" style="height: 400px;" id="divs">
	<div style="padding-left: 100px;"><span style="padding-left: 30px;">
		<input type="checkbox" id="isAuditing" name="isAuditing" value="1" #if("$setting.isAuditing"=="1")checked#end onclick="showGroup()"/>启用审核</span>
		<span style="padding-left: 30px;" id="isAccountspan">
		<input type="checkbox" id="isAccountAuditing" value="1" name="isAccountAuditing" #if("$setting.isAccountAuditing"=="1")checked#end />凭证过账前必须审核</span>
		<!-- <span style="padding-left: 30px;" id="isCheckspan">
		<input type="checkbox" id="isCheck" name="isCheck" value="1" #if("$setting.isCheck"=="1")checked#end/>凭证过账前必须出纳复核</span>
		<span style="color: #ccc;">(会计科目启用现金科目类,银行科目类,现金等价物类之一需要复核或者反复核)</span>
		<span style="padding-left: 30px;">
		<input type="checkbox" id="isCash" name="isCash" value="1" #if("$setting.isCash"=="1")checked#end/>录入凭证时必须指定现金流量</span> -->
	</div>
	
	<div style="padding-left: 130px;">
	<div id="_context1">
	<div class="oa_signDocument1" id="_context">
	<div class="oa_signDocument_3">
		<img src="/style1/images/St.gif" onclick="openPop('auditingPersontName','auditingPersont','选择审核人')" alt="选择审核人" class="search"/>&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" title="选择审核人" onclick="openPop('auditingPersontName','auditingPersont','选择审核人')">选择审核人</a>
	</div>
	<select name="auditingPersontName" id="auditingPersontName" multiple="multiple">
	#foreach($user in $auditingList)
		<option value="$user.id">$!user.EmpFullName</option>
	#end
	</select>
		<img align="top" onclick="delOpation('auditingPersontName','auditingPersont')" src="/style1/images/delete_button.gif" alt="移除"  title="移除" class="search"/>
	</div>
	</div>
	<div id="_context2">
	<div class="oa_signDocument1" id="_context">
	<div class="oa_signDocument_3">
		<img src="/style1/images/St.gif" onclick="openPop('reverseAuditingName','reverseAuditing','选择反审核人')"  alt="选择反审核人" class="search"/>&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" title="选择反审核人" onclick="openPop('reverseAuditingName','reverseAuditing','选择反审核人')">选择反审核人</a>
	</div>
	<select name="reverseAuditingName" id="reverseAuditingName" multiple="multiple">
	#foreach($user in $reverseAuditingList)
		<option value="$user.id">$!user.EmpFullName</option>
	#end
	</select>
		<img align="top" onclick="delOpation('reverseAuditingName','reverseAuditing')" src="/style1/images/delete_button.gif" alt="移除"  title="移除" class="search"/>
	</div>
	</div>
	<div id="_context3">
	<div class="oa_signDocument1" id="_context">
	<div class="oa_signDocument_3">
		<img src="/style1/images/St.gif" onclick="openPop('binderPersontName','binderPersont','选择过账人');"  alt="选择过账人" class="search"/>&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" title="选择过账人" onclick="openPop('binderPersontName','binderPersont','选择过账人');">选择过账人</a>
	</div>
	<select name="binderPersontName" id="binderPersontName" multiple="multiple">
	#foreach($user in $binderList)
		<option value="$user.id">$!user.EmpFullName</option>
	#end
	</select>
		<img align="top" onClick="delOpation('binderPersontName','binderPersont')" src="/style1/images/delete_button.gif" alt="移除"  title="移除" class="search"/>
	</div>
	</div>
	<div id="_context4">
	<div class="oa_signDocument1" id="_context">
	<div class="oa_signDocument_3">
		<img src="/style1/images/St.gif" onclick="openPop('reverseBinderName','reverseBinder','选择反过账人');"  alt="选择反过账人" class="search"/>&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" title="选择反过账人" onclick="openPop('reverseBinderName','reverseBinder','选择反过账人');">选择反过账人</a>
	</div>
	<select name="reverseBinderName" id="reverseBinderName" multiple="multiple">
	#foreach($user in $reverseBinderList)
		<option value="$user.id">$!user.EmpFullName</option>
	#end
	</select>
		<img align="top" onclick="delOpation('reverseBinderName','reverseBinder')" src="/style1/images/delete_button.gif" alt="移除"  title="移除" class="search"/>
	</div>
	</div>
	<!-- #if("$!globals.getSysSetting('standardAcc')"=="true")
	<div id="_context5">
	<div class="oa_signDocument1" id="_context">
	<div class="oa_signDocument_3">
		<img src="/style1/images/St.gif" onClick="openPop('checkPersontName','checkPersont','选择复核人');"  alt="选择复核人" class="search"/>&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" title="选择复核人" onClick="openPop('checkPersontName','checkPersont','选择复核人');">选择复核人</a>
	</div>
	
	<select name="checkPersontName" id="checkPersontName" multiple="multiple">
	#foreach($user in $checkPersontList)
		<option value="$user.id">$!user.EmpFullName</option>
	#end
	</select>
		<img align="top" onClick="delOpation('checkPersontName','checkPersont')" src="/style1/images/delete_button.gif" alt="移除"  title="移除" class="search"/>
	</div>
	</div>#end -->
	<!-- <div id="_context6">
	<div class="oa_signDocument1" id="_context">
	<div class="oa_signDocument_3">
		<img src="/style1/images/St.gif" onClick="openPop('cashPersontName','cashPersont','选择现金流量人');"  alt="选择现金流量人" class="search"/>&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" title="选择现金流量人" onClick="openPop('cashPersontName','cashPersont','选择现金流量人');">选择现金流量人</a>
	</div>
	<select name="cashPersontName" id="cashPersontName" multiple="multiple">
	#foreach($user in $cashPersontList)
		<option value="$user.id">$!user.EmpFullName</option>
	#end
	</select>
		<img align="top" onClick="delOpation('cashPersontName','cashPersont')" src="/style1/images/delete_button.gif" alt="移除"  title="移除" class="search"/>
	</div>
	</div> -->
	</div>
</div>
</form>
</body>
</html>