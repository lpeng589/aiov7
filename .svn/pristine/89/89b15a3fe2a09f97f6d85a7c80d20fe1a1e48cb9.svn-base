<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("muduleFlow.lb.voucherlogin")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<style type="text/css">
	.TopSharing_button{
		width:auto;
	}
	.alignDiv{
		align: center;
	}
	.mains{
		padding: 20px 0px 15px 0px;
		border: #dedede 1px solid;
	}
	.btnlp{
		margin:6px;
		float: left;
		cursor: pointer;
	}
	#titles td{
		background: url(/style/images/client/bg.gif) no-repeat 1000px 1000px;
		background-position: 0px -78px;
		background-repeat: repeat-x;
		text-align: center;
	}
	table{
		border: #dedede 1px solid;
		border-collapse : collapse;
	}
	#tableItem input{
		border: #dedede 1px solid;
	}
	#btndiv input{
		margin: 10px;
		border :0px;
		width:60px;
		height:24px;
		background-image: url("/style/images/plan/bu_02.gif");
	}
	#table tr{
		height:50px;
	}
</style>
<script type="text/javascript">
	function dealdiv(type){
		if(type == "1"){
			var flag = false;
			jQuery("input[name=currentexchange]").each(function(i,n){
				var value = jQuery(n).val();
				if(!isFloat(value) || value<0){
					alert("请输入正确的当前汇率!");
					jQuery(n).focus();
					flag = true;
					return false;
				}
			});
			jQuery("input[name=adjustexchange]").each(function(i,n){
				var value = jQuery(n).val();
				if(!isFloat(value) || value<0){
					alert("请输入正确的调整汇率!");
					jQuery(n).focus();
					flag = true;
					return false;
				}
			});
			if(flag){
				return false;
			}
			if($exchangeList.size()==0){
				alert("当前会计期间无外币汇率");
				return false;
			}
			jQuery("#exchangediv").hide();
			jQuery("#ajustdiv").show();
		}else{
			jQuery("#exchangediv").show();
			jQuery("#ajustdiv").hide();
		}
	}
	
	/* 时间选择*/
	function openInputDate(obj){
		WdatePicker({lang:'zh_CN'});
	}
	
	//双击文本框选择会计科目
	function selectAccCode(){
		var displayName=encodeURI("会计科目") ;
		var urlstr = '/PopupAction.do?popupName=popAccTypeInfo&chooseType=chooseChild&inputType=radio&isCheckItem=true&returnName=exePopdivAcc';
		urlstr = encodeURI(encodeURI(urlstr));
		asyncbox.open({
			id:'Popdiv',
			title:'会计科目',
			url:urlstr,
			width:750,
			height:470,
			btnsbar : [{
		     text    : '清&nbsp;&nbsp;&nbsp;空',
		      action  : 'clearbtn'
		  	}].concat(jQuery.btn.OKCANCEL),
			callback : function(action,opener){
				if(action == 'ok'){
					var values = opener.datas();
					exePopdivAcc(values);
				}else if(action == 'clearbtn'){
					jQuery("#accCode").val('');
					jQuery("#accCodeName").val('');
				}
			}
		});
	}
	
	/* 返回数据 */
	function exePopdivAcc(returnValue){
		if(typeof(returnValue)=="undefined") return ;
		var note = returnValue.split("#;#") ;
		jQuery("#accCode").val(note[0]);
		jQuery("#accCodeName").val(note[1]);
		
	}
	
	/* 期末调汇完成按钮提交Form*/
	function submitForm(){
		var accCode = jQuery("#accCode").val();
		var accTime = jQuery("#accTime").val();
		if(accCode==""){
			alert("请选择汇兑损益科目！");
			jQuery("#accCodeName").focus();
			return false;
		}
		if(accTime == ""){
			alert("请选择凭证日期！");
			jQuery("#accTime").focus();
			return false;
		}
		form.submit();
	}
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body style="background: #f5f5f5;">
<form id="form" name="form" method="post" action="/AdjustExchangeAction.do" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")"/>
<input type="hidden" name="type" value="settleAcc"/>
<div style="margin-top: 10px;" class="ABK_Add c_main winw1200 f_l mains" id="exchangediv">
<div style="padding-left: 10px;">
	<img src="/style/images/msg/Pages_up.gif"/><span style="font-size: 14px;">当前的外币汇率</span>
</div>
<div align="center">
<div class="tablediv" style="width: 700px;" align="center">
	<table width="100%" height="30px" cellpadding="0" cellspacing="0">
	  <tr id="titles" name="titles" style="background: #fff;">
		<td width="10%"><div class="alignDiv">币种符号</div></td>
		<td width="20%"><div class="alignDiv">币种名称</div></td>
		<td width="10%"><div class="alignDiv">会计期间年</div></td>
		<td width="10%"><div class="alignDiv">会计期间月</div></td>
		<td width="25%"><div class="alignDiv">当前汇率</div></td>
		<td width="25%"><div class="alignDiv">调整汇率</div></td>
	  </tr>
	</table>	
</div>
<div style="overflow-x:hidden;overflow-y:auto;width: 700px;" align="center" id="heart_id">
<script type="text/javascript"> 
	var oDiv=document.getElementById("heart_id");
	var sHeight=document.documentElement.clientHeight-220;
	oDiv.style.height=sHeight+"px";
</script>
	<table id="tableItem" cellpadding="0" cellspacing="0" style="border-top: 0px;width:100%">
	#set($year="") 
	#set($month="")
	#foreach($exchange in $exchangeList)
	<tr style="height: 30px;">
		<td width="10%" style="text-align: center;">$!globals.get($exchange,1)</td>
		<td width="20%" style="text-align: center;">$!globals.get($exchange,2)</td>
		<td width="10%" style="text-align: center;">$!globals.get($exchange,5)</td>
		<td width="10%" style="text-align: center;">$!globals.get($exchange,6)</td>
		<td width="25%">
		#set($year=$!globals.get($exchange,5)) #set($month=$!globals.get($exchange,6)) 
		#if("$!globals.get($exchange,3)" == "0E-8")
			<input id="currentexchange" readonly="readonly" name="currentexchange" value="0" />
		#else
			<input id="currentexchange" readonly="readonly" name="currentexchange" value="$!globals.newFormatNumber($!globals.get($exchange,3),false,false,$!globals.getSysIntswitch(),'tblSetExchange','RecordExchange',true)" />
		#end</td>
		<td width="25%"><input name="keyId" name="keyId" type="hidden" value="$!globals.get($exchange,0)" />
		#if("$!globals.get($exchange,4)" == "0E-8")
			<input id="adjustexchange" name="adjustexchange" value="0" />
		#else
			<input id="adjustexchange" name="adjustexchange" value="$!globals.newFormatNumber($!globals.get($exchange,4),false,false,$!globals.getSysIntswitch(),'tblSetExchange','AdjustExchange',true)" />
		#end
		</td>
	</tr>
	#end
	</table>
</div>
</div>
<div align="center" id="btndiv">
<input type="button" value="下一步" onclick="dealdiv('1')"/>
</div>
</div>



<div style="margin-top: 10px;;display: none;width:85%;" class="ABK_Add c_main winw1200 f_l mains" id="ajustdiv">
<div style="padding-left: 10px;">
	<img src="/style/images/msg/Pages_up.gif"/><span style="font-size: 14px;">期末调汇</span>
</div>
<div style="padding-left: 200px;">
	<div class="tablediv" id="table">
		<table>
			<tr><td>汇兑损益科目：</td><td><input name="accCode" id="accCode" type="hidden" value="660301"/>
				<input id="accCodeName" name="accCodeName" type="text" value="财务费用－汇兑损益" ondblclick="selectAccCode()" readonly="readonly"/>
				<img src="/style1/images/St.gif" onclick="selectAccCode()"  alt="选择科目" class="search"/></td>
			</tr>
			<!-- 
			<tr><td>生成凭证分类：</td><td><input type="checkbox" name="accAssort" id="accAssort" value="1"/>汇兑收益
				&nbsp;&nbsp;<input type="checkbox" name="accAssort" id="accAssort" value="2"/>汇兑损失
				&nbsp;&nbsp;<input type="checkbox" name="accAssort" id="accAssort" value="3" checked />汇兑损益</td>
			</tr> -->
			<tr><td>凭证日期：</td>
				<td><input name="accTime" id="accTime" onClick="openInputDate(this);" value="$!globals.getMonthLastDay($!year,$!month)"/></td>
			</tr>
			<tr><td>凭证字：</td>
				<td><select name="credTypeID" id="credTypeID">
				#foreach($erow in $globals.getEnumerationItems('CredTypeID'))
					<option title="$erow.name" value="$erow.value">$erow.name</option>
				#end
				</select></td>
			</tr>
			<tr>
				<td>凭证摘要：</td>
				<td><input name="recordCommon" value="结转汇总损益" id="recordCommon" /></td>
			</tr>
		</table>
	</div>
</div>
<div align="center" id="btndiv">
<input type="button" value="上一步" onclick="dealdiv('2')"/>
<!-- <input type="button" value="完成" onClick="submitForm()"/> -->
</div>
</div>
</form>
</body>
</html>