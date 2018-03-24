
/* 隐藏过滤div */
function closeSearch(){
	$('#highSearch').hide();
	if(document.getElementById('back')!=null){
		document.getElementById('back').parentNode.removeChild(document.getElementById('back'));
	}
}

var isIe=(document.all)?true:false;
/* 显示过滤div */
function showSearchDiv(){
	$('#highSearch').show();
	var bWidth=parseInt(document.documentElement.scrollWidth);
	var bHeight=parseInt(document.documentElement.scrollHeight);
	var back=document.createElement("div");
	back.id="back";
	var styleStr="top:0px;left:0px;position:absolute;background:#666;width:"+bWidth+"px;height:"+bHeight+"px;";
	styleStr+=(isIe)?"filter:alpha(opacity=0);":"opacity:0;";
	back.style.cssText=styleStr;
	document.body.appendChild(back);
	showBackground(back,50);
}

//让背景渐渐变暗
function showBackground(obj,endInt){
	if(isIe){
		obj.filters.alpha.opacity+=5;
		if(obj.filters.alpha.opacity<endInt){
			setTimeout(function(){showBackground(obj,endInt)},1);
		}
	}else{
		var al=parseFloat(obj.style.opacity);al+=0.05;
		obj.style.opacity=al;
		if(al<(endInt/100)){
			setTimeout(function(){showBackground(obj,endInt)},1);}
		}
	}

$(document).ready(function() {
	$(".searchinput").mouseover(function(){
   		$(this).find("img").show();
   	});
   	$(".searchinput").mouseout(function(){
   		//$(this).find("img").hide();
   	});
   	if(typeof(top.junblockUI)!="undefined"){
		top.junblockUI();
	}
});

var inputName = "";
/* 会计科目弹出框 */
function selectCode(value){
	inputName = value;
	var urlstr = '/PopupAction.do?popupName=popAccTypeInfo&chooseType=all&inputType=radio&returnName=exePopAccdiv&isCheckItem=false';
	urlstr += "&isCease="+$("#showBanAccTypeInfo").is(":checked");
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
				exePopAccdiv(values);
			}else if(action == 'clearbtn'){
				$("#"+inputName).val('');
			}
		}
	});
}

/* 回填数据 */
function exePopAccdiv(returnValue){
	if(typeof(returnValue)=="undefined") return ;
	var nateStr = returnValue.split("#|#");
	for(var j=0;j<nateStr.length;j++){
		if(nateStr[j]!=""){ 
			var note = nateStr[j].split("#;#"); 
			$("#"+inputName).val(note[0]);
		}
	}
}

//打印
function printReport(reportNumber,reportName){
	printControl('/UserFunctionQueryAction.do?tableName=tblBalance&reportNumber='+reportNumber+'&financeReportName='+reportName+'&moduleType=&operation=18&parentTableName=&winCurIndex=1');
}

//搜索条件验证
function isvalidate(){
	var periodYearStart = $("#periodYearStart").val();
	var periodStart = $("#periodStart").val();
	var periodYearEnd = $("#periodYearEnd").val();
	var periodEnd = $("#periodEnd").val();
	var levelStart = $("#levelStart").val();
	var levelEnd = $("#levelEnd").val();
	if(periodYearStart != undefined && periodYearStart == ""){
		asyncbox.tips('会计期间年不能为空','提示',1500);
		$("#periodYearStart").focus();
		return false;
	}
	if(periodYearStart != undefined && periodYearStart != "" && !isInt2(periodYearStart)){
		asyncbox.tips('会计期间年必须是正整数','提示',1500);
		$("#periodYearStart").focus();
		return false;
	}
	if(periodStart != undefined && periodStart == ""){
		asyncbox.tips('会计期间不能为空','提示',1500);
		$("#periodStart").focus();
		return false;
	}
	if(periodStart != undefined && periodStart != "" && !isInt2(periodStart)){
		asyncbox.tips('会计期间必须是正整数','提示',1500);
		$("#periodStart").focus();
		return false;
	}
	if(periodYearEnd != undefined && periodYearEnd == ""){
		asyncbox.tips('会计期间年不能为空','提示',1500);
		$("#periodYearEnd").focus();
		return false;
	}
	if(periodYearEnd != undefined && periodYearEnd != "" && !isInt2(periodYearEnd)){
		asyncbox.tips('会计期间年必须是正整数','提示',1500);
		$("#periodYearEnd").focus();
		return false;
	}
	if(periodEnd != undefined && periodEnd == ""){
		asyncbox.tips('会计期间不能为空','提示',1500);
		$("#periodEnd").focus();
		return false;
	}
	if(periodEnd != undefined && periodEnd != "" && !isInt2(periodEnd)){
		asyncbox.tips('会计期间必须是正整数','提示',1500);
		$("#periodEnd").focus();
		return false;
	}
	if(periodYearStart != undefined && periodYearEnd != undefined 
		&& periodStart != undefined && periodEnd != undefined){
		if(parseInt(periodYearStart)>parseInt(periodYearEnd) || (parseInt(periodYearStart)==parseInt(periodYearEnd) && parseInt(periodStart)>parseInt(periodEnd))){
			asyncbox.tips('会计期间输入有误','提示',1500);
			$("#periodYearStart").focus();
			return false;
		}
	}
	//会计期间开始判断，如果输入的开始期间小于开账期间验证不通过
	if(typeof(beginYear) != 'undefined' && typeof(beginPeriod) != 'undefined'){
		if((parseInt(beginYear)>parseInt(periodYearStart) || (parseInt(beginYear)==parseInt(periodYearStart) && parseInt(beginPeriod)>parseInt(periodStart)))){
			asyncbox.tips('会计期间必须大于等于开账期间'+beginYear+'.'+beginPeriod,'提示',1500);
			$("#periodYearStart").focus();
			return false;
		}
	}
	
	if(levelStart != undefined && levelStart != "" && !isInt2(levelStart)){
		asyncbox.tips('科目等级必须是正整数','提示',1500);
		$("#levelStart").focus();
		return false;
	}
	if(levelEnd != undefined && levelEnd != "" && !isInt2(levelEnd)){
		asyncbox.tips('科目等级必须是正整数','提示',1500);
		$("#levelEnd").focus();
		return false;
	}
	return true;
}

//刷新
function onrefurbish(){
	blockUI();
	window.location.reload();
}

/**
*	锁屏
*/
function blockUI(){
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: "<div style='width:135px;'><img src='/style/images/load.gif'/><div style='color:#0179bb;font-weight:bold;'>正在查询，请稍后。。。</div></div>",css:{ background: 'transparent'}}); 
	}
}
/*模糊查询*/
function submitQuery(){
	blockUI();
	form.submit();
}
/* 点击分页 */
function pageSubmit(pageNo){
	$("#pageNo").val(pageNo);
	blockUI();
	form.submit();
}

/* 关闭div */
function closePage(){
	closeWin();
}

/* 时间选择*/
function openInputDate(obj){
	WdatePicker({lang:'zh_CN'});
}

//处理金额转换为大写
function AmountInWords(dValue, maxDec) {
	  // 验证输入金额数值或数值字符串：
	  dValue = dValue.toString().replace(/,/g, ""); dValue = dValue.replace(/^0+/, ""); // 金额数值转字符、移除逗号、移除前导零
	  dValue = dValue.trim();
	  if (dValue == "") { return "零元整"; } // （错误：金额为空！）
	  else if (isNaN(dValue)) { return "错误：金额不是合法的数值！"; } 
	   
	  var minus = ""; // 负数的符号“-”的大写：“负”字。可自定义字符，如“（负）”。
	  var CN_SYMBOL = ""; // 币种名称（如“人民币”，默认空）
	  if (dValue.length > 1)
	  {
	  if (dValue.indexOf('-') == 0) { dValue = dValue.replace("-", ""); minus = "负"; } // 处理负数符号“-”
	  if (dValue.indexOf('+') == 0) { dValue = dValue.replace("+", ""); } // 处理前导正数符号“+”（无实际意义）
	  }
	  // 变量定义：
	  var vInt = ""; var vDec = ""; // 字符串：金额的整数部分、小数部分
	  var resAIW; // 字符串：要输出的结果
	  var parts; // 数组（整数部分.小数部分），length=1时则仅为整数。
	  var digits, radices, bigRadices, decimals; // 数组：数字（0~9——零~玖）；基（十进制记数系统中每个数字位的基是10——拾,佰,仟）；大基（万,亿,兆,京,垓,杼,穰,沟,涧,正）；辅币（元以下，角/分/厘/毫/丝）。
	  var zeroCount; // 零计数
	  var i, p, d; // 循环因子；前一位数字；当前位数字。
	  var quotient, modulus; // 整数部分计算用：商数、模数。
	
	  // 金额数值转换为字符，分割整数部分和小数部分：整数、小数分开来搞（小数部分有可能四舍五入后对整数部分有进位）。
	  var NoneDecLen = (typeof(maxDec) == "undefined" || maxDec == null || Number(maxDec) < 0 || Number(maxDec) > 5); // 是否未指定有效小数位（true/false）
	  parts = dValue.split('.'); // 数组赋值：（整数部分.小数部分），Array的length=1则仅为整数。
	  if (parts.length > 1) 
	  {
	  vInt = parts[0]; vDec = parts[1]; // 变量赋值：金额的整数部分、小数部分
	   
	  if(NoneDecLen) { maxDec = vDec.length > 5 ? 5 : vDec.length; } // 未指定有效小数位参数值时，自动取实际小数位长但不超5。
	  var rDec = Number("0." + vDec);  
	  rDec *= Math.pow(10, maxDec); rDec = Math.round(Math.abs(rDec)); rDec /= Math.pow(10, maxDec); // 小数四舍五入
	  var aIntDec = rDec.toString().split('.');
	  if(Number(aIntDec[0]) == 1) { vInt = (Number(vInt) + 1).toString(); } // 小数部分四舍五入后有可能向整数部分的个位进位（值1）
	  if(aIntDec.length > 1) { vDec = aIntDec[1]; } else { vDec = ""; }
	  }
	  else { vInt = dValue; vDec = ""; if(NoneDecLen) { maxDec = 0; } } 
	  if(vInt.length > 44) { return "错误：金额值太大了！整数位长【" + vInt.length.toString() + "】超过了上限——44位/千正/10^43（注：1正=1万涧=1亿亿亿亿亿，10^40）！"; }
	   
	  // 准备各字符数组 Prepare the characters corresponding to the digits:
	  digits = new Array("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"); // 零~玖
	  radices = new Array("", "拾", "佰", "仟"); // 拾,佰,仟
	  bigRadices = new Array("", "万", "亿", "兆", "京", "垓", "杼", "穰" ,"沟", "涧", "正"); // 万,亿,兆,京,垓,杼,穰,沟,涧,正
	  decimals = new Array("角", "分", "厘", "毫", "丝"); // 角/分/厘/毫/丝
	   
	  resAIW = ""; // 开始处理
	   
	  // 处理整数部分（如果有）
	  if (Number(vInt) > 0) 
	  {
	  zeroCount = 0;
	  for (i = 0; i < vInt.length; i++) 
	  {
	  p = vInt.length - i - 1; d = vInt.substr(i, 1); quotient = p / 4; modulus = p % 4;
	  if (d == "0") { zeroCount++; }
	  else 
	  {
	  if (zeroCount > 0) { resAIW += digits[0]; }
	  zeroCount = 0; resAIW += digits[Number(d)] + radices[modulus];
	  }
	  if (modulus == 0 && zeroCount < 4) { resAIW += bigRadices[quotient]; }
	  }
	  resAIW += "元";
	  }
	   
	  // 处理小数部分（如果有）
	  for (i = 0; i < vDec.length; i++) { d = vDec.substr(i, 1); if (d != "0") { resAIW += digits[Number(d)] + decimals[i]; } }
	   
	  // 处理结果
	  if (resAIW == "") { resAIW = "零" + "元"; } // 零元
	  if (vDec == "") { resAIW += "整"; } // ...元整
	  resAIW = CN_SYMBOL + minus + resAIW; // 人民币/负......元角分/整
	  return resAIW;
	   
	  // 备注：
	} 
  