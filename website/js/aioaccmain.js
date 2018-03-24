//以下是下拉弹出窗功能。
function showData(inp,selectName,fieldName,display){
	objectPop = inp;
	var dropdown=inp.dropdown;
	if(dropdown==undefined)
	{
    	var data = {
    	selectName:selectName,
    	operation:"DropdownPopup",
    	MOID:moduleId,
    	MOOP:"query",
    	selectField:fieldName,
    	};
		dropdown = new dropDownSelect("t_"+inp.id,data,inp,selectName);
		if(fieldName=="AccCodeName"){
			dropdown.retFun2 = setPodDivAcc; //科目的回填
		}else{
			dropdown.retFun = setFieldValue;
		}
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
//下拉弹窗回填科目
function setPodDivAcc(cols,vals){
	current_tr.find("#AccCodeOld").val("");
	current_tr.find("#hiddenData").find("input").val("");
	var col = cols.split(";");
	var val = vals.split("#;#");
	var acccode='';
	var accfullname='';
	for(var i=0;i<col.length;i++){
		var nm = col[i].replace(".","_");
		current_tr.find("input[name='"+nm+"']").val(val[i]);
		if(nm=='tblAccTypeInfo_AccFullName'){
			accfullname=val[i];
		}else if(nm=='tblAccDetail_AccCode'){
			acccode=val[i];
		}
	}
	
	//启用多币种 查询币种
	if(currencysettings=="true"){
		if(accCodes!=current_tr.find("input[name='tblAccDetail_AccCode']").val()){
			gainCurrency(current_tr.find("input[name='tblAccDetail_AccCode']"));
		}
	}
	current_tr.find("input[name='AccCodeName']").val(acccode+' - '+accfullname);
	current_tr.find("input[name='AccCodeName']").focus();
}
function setFieldValue(str,selectName,fieldName){
	if(fieldName=="AccCodeName"){
		objectinput = objectPop;
		exePopdivAcc(str);
		return;
	}
	
	showvalues = fieldName;
	if(fieldName=='IsClientName' || fieldName=='IsProviderName' || fieldName=='IsComeName'){
		hidevalues = 'CompanyCode';
	}else if(fieldName=="IsProjectName"){
		hidevalues = 'ProjectCode';
	}else if(fieldName=="isStockName"){
		hidevalues = 'StockCode';
	}else if(fieldName=="IsDeptName"){
		hidevalues = 'DepartmentCode';
	}else if(fieldName=="IsPersonalName"){
		hidevalues = 'EmployeeIDs';
	}else if(fieldName=="IsPersonalName"){
		hidevalues = 'EmployeeIDs';
	}else if(fieldName=="DepartmentName"){
		hidevalues = 'mDepartmentCode';
	}else if(fieldName=="EmployeeName"){
		hidevalues = 'EmployeeID';
	}
	
	exePopdiv(str);
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
  
  
  
	//根据数字金额取大写金额
	function showBigNum(obj){
		if($(obj).val()=="="){
			var name = $(obj).attr("name");
			if(name=="tblAccDetail_DebitAmount" || name=="tblAccDetail_LendAmount"){
				$(obj).val("0");
				var totalDebit = 0;
				var totalLend = 0;
				$("input[name=tblAccDetail_DebitAmount]").each(function(){
					totalDebit += Number($(this).val());
				});
				$("input[name=tblAccDetail_LendAmount]").each(function(){
					totalLend += Number($(this).val());
				});
				if(name=="tblAccDetail_DebitAmount"){//借
					$(obj).val(totalLend-totalDebit);
				}else if(name=="tblAccDetail_LendAmount"){ //贷
					$(obj).val(totalDebit -totalLend);
				}
			}
		}
		
		var moneyValue = $(obj).val();
		var changeValue = AmountInWords(moneyValue,null);
		$("#bigValue").text(changeValue);
	}


	var prevalue='';
	var nowid = 0;
	var divid='helper';
	var searchdivid = 'searchs';
	//提示框的定位，绝对位置
	function gettable(obj) {
		var pos = getabsposition(obj);
		pos.top += obj.offsetHeight;
		document.getElementById(divid).style.top = pos.top + "px";
		document.getElementById(divid).style.left = pos.left + "px";
		document.getElementById(divid).style.width = (obj.offsetWidth-5) + "px";
		document.getElementById(divid).style.display = '';
	}
	function init(inputobjects) {
		//如果用户输入的是.. 或空格，复制上一行摘要
		var val = $(inputobjects).val();
		if(val==".." || val==" "){
			//找上行的摘要
			var i = $("input[name=tblAccDetail_RecordComment]").index($(inputobjects));
			$(inputobjects).val($("input[name=tblAccDetail_RecordComment]:eq("+(i-1)+")").val());
			return;
		}
		var nowvalue = $(inputobjects).val();
		if(nowvalue == ""){
			$("#"+divid).hide();
		}else if(prevalue != nowvalue) {
			var resultValue = "";
			var url = "/VoucherManage.do?optype=queryDescription&description="+nowvalue;
			url = encodeURI(encodeURI(url));
			jQuery.ajax({ type: "POST", url: url, success: function(result){
			    getnameresponse(result,inputobjects);
			}});
		}
		prevalue = nowvalue;
		if(event.keyCode==13 && $("#"+divid).is(":hidden") ){
			chooserecord(this)
		}else if( !$("#"+divid).is(":hidden")) {
			event.stopPropagation();
			if(event.keyCode == 38)
			{
				classChangefx(true);
				return ;
			}else if(event.keyCode==40)
			{
				classChangefx(false);
				return ;
			}else if(event.keyCode==13)
			{	
				if($("tr[id^='trs'][class*='hlight']").size()>0){
					inx = $("tr[id^='trs']").index($("tr[id^='trs'][class*='hlight']"));
					selecttr(inx+1);
				}
				return ;
			}else if(event.keyCode==27)
			{
				hidediv();
				return ;
			}
			
		}
	} 
	function recommendDown(obj){
		if( !$("#"+divid).is(":hidden")&&$("#"+divid).find("tr").size()>0){
			event.stopPropagation();
		}
		
	}
	var inputobj = null;
	//展示提示信息
	function getnameresponse(result,inputobjects) {
		var info = result.split(";|");
		$("#"+searchdivid).html('');
		var html = '<table width="100%" id=\"tb\">';
		var len = info.length;
		if(len>10){
			$("#"+searchdivid).addClass("divsear");
		}else{
			$("#"+searchdivid).removeClass("divsear");
		}
		for(var i = 0;i < len; i++) {
			if(info[i]!=""){
				html += '<tr class=\"normal\" name=\"trname\" id="trs' + (i+1) +'" onmouseover="classchange(' + (i+1) +');">'
				   + '<td colspan="2"><div style="width:95%;float:left;" id="div1" onclick="selecttr(' + (i+1) +');">' + info[i].substring(info[i].indexOf(',')+1,info[i].length) + '</div><div style="float:left;padding-top:2px;cursor: pointer;" id=\"div2\"><img src=\"/style/plan/del_icon.gif\" title=\"删除该条摘要模板\" onclick=\"delcommons('+(i+1)+',\''+info[i].substring(0,info[i].indexOf(','))+'\')\"/></div></td></tr>';
			}
		} 
		html += '</table>';
		inputobj = inputobjects;
		$("#"+searchdivid).html(html);
		gettable(inputobjects);
	}
	function classChangefx(up){ //用键盘滚动
		inx = $("tr[id^='trs']").index($("tr[id^='trs'][class*='hlight']"));
		inx++;
		if(inx>0&&up){
			classchange(''+(inx-1));
		}else if(inx<$("tr[id^='trs']").size() && !up){
			console.log('ffkk'+inx);
			classchange(''+(inx+1));
		}
	}

	
	/* 添加摘要模板 */
	function saveModuls(){
		var values = $(inputobj).val();
		if(values == ""){
			alert("请输入数据");
			$(inputobj).focus()
			return false;
		}
		var url = "/VoucherManage.do?optype=addRecordComment&value="+values;
		jQuery.ajax({ type: "POST", url: url, success: function(result){
			    if(result == "OK"){
			    	asyncbox.tips('已保存至摘要库!','success',1000);
					url = "/VoucherManage.do?optype=queryDescription&description="+values;
					url = encodeURI(encodeURI(url));
					jQuery.ajax({ type: "POST", url: url, success: function(result){
					    getnameresponse(result,inputobj);
					}});
			    }
		}});
	}
	
	/* 删除摘要模板 */
	function delcommons(trsid,commonid){
		
		asyncbox.confirm('确定删除摘要吗?','提示',function(action){
			if(action == 'ok'){
				jQuery.ajax({ type: "POST", url: "/VoucherManage.do?optype=delRecordComment&id="+commonid, success: function(result){
			    if(result == "OK"){
			    	//删除成功
			    	var url = "/VoucherManage.do?optype=queryDescription&description="+$(inputobj).val();
					url = encodeURI(encodeURI(url));
					jQuery.ajax({ type: "POST", url: url, success: function(result){
					    getnameresponse(result,inputobj);
					}});
			    }
				}});
			}
			$("#"+divid).show();
		});
	}
	//隐藏功能
	function hidediv(){
		if(document.getElementById(divid).style.display ==''){
			document.getElementById(divid).style.display = 'none';
		}
	}
	//定位div到搜索框正下方            
	function getabsposition(obj) {
		var r = {
			left: obj.offsetLeft,
			top : obj.offsetTop
		};
		r.left = obj.offsetLeft;
		r.top  = obj.offsetTop;
		if(obj.offsetParent) {
			var tmp = getabsposition(obj.offsetParent);
			r.left += tmp.left;
			r.top  += tmp.top;
		}
		return r;
	 }
	function classchange(nextid){
		$("tr[id^='trs']").removeClass("hlight");
		$("#trs"+nextid).addClass("hlight");
	}
	function selecttr(nextid) {
		try{
			if(document.getElementById(divid).style.display == 'none') 
				return;
			$(inputobj).val($('#trs'+nextid).find("#div1").html());
			document.getElementById(divid).style.display ='none';
			prevalue = $(inputobjects).val();
		}catch (e){
		}
	}
	
	//金额小数点控制
	function subStringMoney(str){
		var len = lens;
		var strvalue = str;
		if (strvalue.indexOf(".")>0){
			strvalue = strvalue.substring(0,new Number(strvalue.indexOf("."))+new Number(len)+1);
		}
		return strvalue;
	}
	
	/* 改变日期计算是哪一年哪一期 */
	function dateChange(){
		var billdate = $("#BillDate").val();
		if(billdate==""){
			$("#BillDate").val(startTime);
		}
		billdate = $("#BillDate").val();
		var yearspan = billdate.substring(0,billdate.indexOf("-"));
		var periodspan = new Number(billdate.substring(billdate.indexOf("-")+1,billdate.indexOf("-")+3));
		$("#span1").html(yearspan);
		$("#span2").text(periodspan);
	}
	
	/* 统计文件数量*/
	function filenum(object){
		var attachvalue = $(object).val();
		var num = 0;
		if(attachvalue!=""){
			var attach = attachvalue.split(";");
			for(var i=0;i<attach.length;i++){
				if(attach[i]!=""){
					num ++;
				}
			}
		}
		$("#AcceNum").val(num);
	}
	
	/* 时间选择*/
	function openInputDate(obj){
		WdatePicker({lang:'zh_CN'});
	}
	
	/* 在摘要库中选择摘要*/
	var recordobject = null;
	function chooserecord(object){
		var value = $(object).val();
		var url = '/VoucherManage.do?optype=queryRecordComment&searchValue='+value;
		url = encodeURI(encodeURI(url));
		asyncbox.open({
			id : 'dealdiv',
	　　		url : url,
		    title : '摘要库',
		　　    width : 570,
		　　 	height : 370,
	    	btnsbar : jQuery.btn.CANCEL,
		    callback : function(action,opener){
	　　　　　  		//判断 action 值。
	　　	　	}
　		});
		recordobject = object;
	}
	
	/* 凭证模板弹出框 */
	function module(){
		var url = '/VoucherManage.do?operation=4&optype=voucherModule';
		url = encodeURI(encodeURI(url));
		asyncbox.open({
			id : 'moduleDiv',
	　　		url : url,
		    title : '凭证模板',
		　　 width : 570,
		　　 height : 370,
	    	btnsbar : [{
				text    : '关闭',
			    action  : 'new_btn_1'
  			}],
		    callback : function(action,opener){
	　　　　　  		//判断 action 值。
				if(action=="new_btn_1"){
					//window.location.reload();
				}
	　　	　	}
　		});
	}
	function saveModule(keyId){
		asyncbox.open({
			id : 'moduleDiv',
	　　		html : '<input  style="margin:30px 10px;width:200px" id="saveToName">',
		    title : '请输入另存模板名称',
		　　    width : 270,
		　　    height : 150,
		　　    btnsbar : jQuery.btn.OKCANCEL,
		    callback : function(action,opener){
	　　　　　  		//判断 action 值。
				if(action=="ok"){
					jQuery.post("/VoucherManage.do?optype=saveToAccTemp",
					  {name:$("#saveToName").val(),keyId:keyId}	,	
					  function(data){
					    alert(  data);
					   }
					); 
				}
	　　	　	}
　		});
	}
	
	var objectinput = null;
	var strtypes = "";
	var accCodes = "";
	var currencysettings = "";
	//双击文本框选择会计科目
	function selectAccCode(object,strtype,currencysetting,searchValue){
		objectinput = object;
		strtypes = strtype;
		currencysettings = currencysetting;
		accCode =$(object).next().val();
		var urlstr = '/PopupAction.do?popupName=popAccTypeInfo&chooseType=chooseChild&inputType=checkbox&isCheckItem=true&returnName=exePopdivAcc&';
		if(searchValue!=null && searchValue!=""){
			urlstr += "&selectType=keyword&selectValue="+searchValue;
		}
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
					dealStrDate('');
				}
			}
		});
	}
	
	/* 回填数据 */
	function exePopdivAcc(returnValue){
		dealStrDate(returnValue);
	}	
	
	/* 直接取数据 */
	function popValue(object,strtype,currencysetting){
		var values = $(object).val();
		if(values == ""){
			selectAccCode(object,strtype,currencysetting,values);
			return false;
		}
		objectinput = object;
		strtypes = strtype;
		currencysettings = currencysetting;
		accCode =$(object).next().val();
		var urls = "/PopupAction.do?operation=ajaxPopDetail&chooseType=chooseChildItem&selectValue="+values;
		urls = encodeURI(encodeURI(urls));
		jQuery.ajax({ type: "POST", url: urls,async: false,success: function(response){
				if(response == null || response == "" || response == "$msg"){
					selectAccCode(object,strtype,currencysetting,values);
				}else{
					exePopdivAcc(response);
				}
	    	}
		});
	}	
	
	function dealStrDate(str){
		if(typeof(str)=="undefined")return ;
		$("#computeDiv").find("input").val("");
		if(str == ""){
			$(objectinput).val("");
			$(objectinput).next().val("");			$(objectinput).find("tr").find("#AccCodeOld").val("");			$(objectinput).find("tr").find("#hiddenData").find("input").val("");
			gainCurrency($(objectinput).next());
		}
		var nateStr = str.split("#|#");
		for(var j=0;j<nateStr.length;j++){
			if(nateStr[j]!=""){
				var note = nateStr[j].split("#;#")
				if(j==0){
					$(objectinput).val(note[0]+" - "+note[1]);
					$(objectinput).next().val(note[0]);					$(objectinput).parents("tr").find("#AccCodeOld").val("");					$(objectinput).parents("tr").find("#hiddenData").find("input").val("");
					//启用多币种 查询币种
					if(currencysettings=="true"){
						if(accCodes!=$(objectinput).next().val()){
							gainCurrency($(objectinput).next());
						}
					}
				}else{
					oname = $(objectinput).attr("name");					ind = $("input[name="+oname+"]").index($(objectinput));					$("input[name="+oname+"]:eq("+(ind+1)+")").focus();	
					$("input[name="+oname+"]:eq("+(ind+1)+")").parents("tr").find("#AccCodeName").val(note[0]+" - "+note[1]);
					$("input[name="+oname+"]:eq("+(ind+1)+")").parents("tr").find("#AccCode").val(note[0]);					$("input[name="+oname+"]:eq("+(ind+1)+")").parents("tr").find("#AccCodeOld").val("");					$("input[name="+oname+"]:eq("+(ind+1)+")").parents("tr").find("#hiddenData").find("input").val("");
					if(currencysettings=="true"){
						gainCurrency($("input[name="+parent+"]:eq("+(ind+1)+")").parents("tr").find("#AccCode"));
					}
				}
			}
		}
	}

	/* 查询币种*/
	function gainCurrency(object){
		var values = $(object).val();
		$(object).parent().parent().find("#CurrencyRate").val("");
		if(values!=""){
			jQuery.ajax({ type: "POST", url: "/VoucherManage.do?optype=queryAccTemp&queryMode=self&accCode="+values,async: false,dataType: "json",success: function(data){
					var curr = data.currency;
					$(object).parent().next().find("select option").remove();
					var currencyobject = $(object).parent().next().find("select");
					currencyobject.append("<option value=''></option>");
					for(var i = 0; i < curr.length; i++) {
					    var datas = curr[i];
					   //if(i==0){
						//	$(object).parent().next().find("select option:first").remove();
						//}
					   	currencyobject.append("<option value='"+datas.id+"'>"+datas.value+"</option>");
					}   
					selectrate(currencyobject);
		    	}
			});
		}
	}
	
	
	/* 调用系统的计算器*/
	function Run(command){
		asyncbox.open({
			id : 'calcu',
	　　		url : '/common/calculator.jsp',
		    title : '计算器',
		　　    width : 350,
		　　 	height : 350,
		    callback : function(action,opener){
	　　　　　  		//判断 action 值。
	　　	　	}
　		});
	}
		
	function tips(obj,str){
		var offset = $(obj).offset();
		var l=offset.left;
		var t=offset.top;		
		$("#tips").html("提示："+str);		$("#tips").show();
		$("#tips").offset({ top: t, left: Number(l)+120 });		console.log("----"+$("#tips").offset().left+"; t="+$("#tips").offset().top);
		
	}
	function outtips(){
	    $("#tips").hide();
	}
	
	/* 返回数据 */
	function exePopdiv(returnValue){
		if(typeof(returnValue)=="undefined") return ;
		var note = returnValue.split("#;#") ;
		//如果是往来单位或者客户供应商时，清空项目数据
		if(showvalues == "IsComeName" || showvalues == "IsClientName" || showvalues == "IsProviderName"){
			//$(objectPop).parent().parent().parent().find("#ProjectCode").val('');
			//$(objectPop).parent().parent().parent().find("#IsProjectName").val('');
		}
		
		if(showvalues == "IsPersonalName" || showvalues == "EmployeeName"){
			//职员弹出框 
			if(showvalues == "IsPersonalName"){
				current_tr.find("#DepartmentCode").val(note[1]);				current_tr.find("#DeptName").val(note[2]);
				$(objectPop).parent().parent().parent().find("#IsDeptName").val(note[2]);
				$(objectPop).parent().find("#"+showvalues).val(note[4]);
				current_tr.find("#"+hidevalues).val(note[0]);
				current_tr.find("#"+(showvalues.substring(2))).val($(objectPop).parent().find("#"+showvalues).val());
			}else if(showvalues == "EmployeeName"){
				$("#mDepartmentCode").val(note[1]);				$("#DepartmentName").val(note[2]);
				$("#"+showvalues).val(note[4]);
				$("#"+hidevalues).val(note[0]);
			}
			
		}else  if(showvalues == "IsClientName"){			//客户			$(objectPop).parent().find("#"+showvalues).val(note[2]);			current_tr.find("#"+hidevalues).val(note[0]);			current_tr.find("#ComFullName").val($(objectPop).parent().find("#"+showvalues).val());		} else if(showvalues == "IsProviderName"){
			if(version == 3 || version == 11){
				$(objectPop).parent().find("#"+showvalues).val(note[3]);
			}else{
				$(objectPop).parent().find("#"+showvalues).val(note[2]);
			}
			current_tr.find("#"+hidevalues).val(note[0]);			current_tr.find("#ComFullName").val($(objectPop).parent().find("#"+showvalues).val());
		}else if(showvalues == "IsComeName"){
			//往来单位
			$(objectPop).parent().find("#"+showvalues).val(note[2]);
			current_tr.find("#"+hidevalues).val(note[0]);			current_tr.find("#ComFullName").val($(objectPop).parent().find("#"+showvalues).val());
		}else if(showvalues == "IsProjectName"){
			//项目
			$(objectPop).parent().find("#"+showvalues).val(note[1]);
			current_tr.find("#"+hidevalues).val(note[0]);			current_tr.find("#"+(showvalues.substring(2))).val($(objectPop).parent().find("#"+showvalues).val());
		}else if(showvalues == "DepartmentName"){
			//部门
			$("#EmployeeID").val('');
			$("#EmployeeName").val('');
			$("#"+showvalues).val(note[2]);
			$("#"+hidevalues).val(note[0]);
		} else{
			//其他
			$(objectPop).parent().find("#"+showvalues).val(note[2]);
			current_tr.find("#"+hidevalues).val(note[0]);			current_tr.find("#"+(showvalues.substring(2))).val($(objectPop).parent().find("#"+showvalues).val());
		}		
	}
	
	var showvalues = "";
	var popnames = "";
	var hidevalues = "";
	var objectPop = null;
	/* 弹出框选择*/	//selectPops(this,'SelectAccEmployee','IsPersonalName','EmployeeIDs','选择个人')
	function selectPops(object,popname,showvalue,hidevalue,name){
		var displayName=encodeURI(name) ;
		var urlstr = '/UserFunctionAction.do?operation=22&popupWin=Popdiv&tableName=&selectName='+popname+"&MOID="+moduleId+"&MOOP=add&LinkType=@URL:&displayName="+displayName;
		if(linkStr != ""){
			urlstr+=encodeURI(linkStr);
		}
		if(showvalue == "IsPersonalName" || showvalue == "EmployeeName"){				
			if(showvalue == "IsPersonalName"){
				deptname= $(object).parent().parent().parent().find("#IsDeptName").val();
			}else if(showvalue == "EmployeeName"){
				deptname= $(object).parent().parent().find("#DepartmentName").val();
			}
			if(deptname!=undefined && deptname!=""){
				deptname = encodeURI(encodeURI(deptname));
				urlstr += "&tblDepartment_DeptFullName="+deptname;
			}
		}else if(showvalue == "IsProjectName"){
			//核算项目时，要根据往来单位进行处理，传往来单位的编号进行过滤
			var companyCode = current_tr.find("#CompanyCode").val();
			if(companyCode!=undefined && companyCode!=""){
				urlstr += "&CompanyCode="+companyCode;
			}
		}
		asyncbox.open({id:'Popdiv',title:name,url:urlstr,width:750,height:470})
		showvalues = showvalue;
		objectPop = object;
		hidevalues = hidevalue;
	}
	
	var linkStr = "";
	/* 敲回车取数据 */
	function ajaxSelects(object,popname,showvalue,hidevalue,name){
		var url="/UtilServlet?operation=Ajax&selectName="+popname+"&MOID="+moduleId+"&MOOP=query&selectField="+showvalue+"&selectValue="+encodeURIComponent($(object).val());
		 AjaxRequest(url) ;
		 if(response == "" || response == null){
		 	selectPops(object,popname,showvalue,hidevalue,name)
		 }else if(response.indexOf("@condition:") >= 0){
			response = revertTextCode(response) ;
			var array = response.split("@condition:");
			linkStr="&keySearch="+array[1]+"&url=@URL:";
			selectPops(object,popname,showvalue,hidevalue,name)
		 }else if(response.indexOf(";") >= 0){
		 	showvalues = showvalue;
			objectPop = object;
			hidevalues = hidevalue;
			exePopdiv(response);
		 }
		 linkStr = "";
		 return false;
	}
	
	/* 修改汇率值触发*/
	function updateExchange(object){
		if($(object).val()!="" && isFloat($(object).val()) && $(object).val()>0){
			accountMoney($(object).parent().parent().find("#DebitAmount"));
			accountMoney($(object).parent().parent().find("#LendAmount"));
		}
	}
	
	function accAdd(arg1,arg2){
	     var r1,r2,m,c;     
	     try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0} 
	     try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}    
	     //m=Math.pow(10,1)   
	     //return (arg1*m+arg2*m)/m;
	     c = Math.abs(r1 - r2);  
		 m = Math.pow(10, Math.max(r1, r2))  
		 if (c > 0) {  
	        var cm = Math.pow(10, c);  
	        if (r1 > r2) {  
	            arg1 = Number(arg1.toString().replace(".", ""));  
	            arg2 = Number(arg2.toString().replace(".", "")) * cm;  
	        }  
	        else {  
	            arg1 = Number(arg1.toString().replace(".", "")) * cm;  
	            arg2 = Number(arg2.toString().replace(".", ""));  
	        }  
	    }else {  
	        arg1 = Number(arg1.toString().replace(".", ""));  
	        arg2 = Number(arg2.toString().replace(".", ""));  
	    }  
	    return (arg1 + arg2) / m
	} 
	
	/* 调用统计金额*/
	function sumMoney(objects){
		//存在汇率
		accountMoney($(objects));
		//统计
		account();
	}
	
	/* 存在汇率计算值*/
	function accountMoney(currobject){
		var currencyRate = $(currobject).parent().parent().find("#CurrencyRate").val();
		var debitamount = $(currobject).parent().parent().find("#DebitAmount").val();
		var debitcurramount = $(currobject).parent().parent().find("#DebitCurrencyAmount").val();
		var lendamount = $(currobject).parent().parent().find("#LendAmount").val();
		var lendcurramount = $(currobject).parent().parent().find("#LendCurrencyAmount").val();
		if(currencyRate!=""){
			currencyRate = new Number(currencyRate);
			if(currencyRate==0){
				return false;
			}
			var value = "";
			if($(currobject).attr("id") == "DebitAmount"){
				value = Math.round((debitamount/currencyRate)*1000)/1000;
				if(debitamount==""){
					value = "";
				}
				$(currobject).parent().parent().find("#DebitCurrencyAmount").val(value);
			}else if($(currobject).attr("id") == "DebitCurrencyAmount"){
				value = Math.round(debitcurramount*currencyRate*1000)/1000;
				$(currobject).parent().parent().find("#DebitAmount").val(value);
			}else if($(currobject).attr("id") == "LendAmount"){
				value = Math.round((lendamount/currencyRate)*1000)/1000;
				if(lendamount==""){
					value = "";
				}
				$(currobject).parent().parent().find("#LendCurrencyAmount").val(value);
			}else if($(currobject).attr("id") == "LendCurrencyAmount"){
				value = Math.round(lendcurramount*currencyRate*1000)/1000;
				$(currobject).parent().parent().find("#LendAmount").val(value);
			}
		}
	}	var current_tr;	function focusRow(obj){	  		var no = $(obj).attr("name");
		if(no!="AccCodeName"){
			$("#t_AccCodeName").remove();
		}
		tr = $(obj).parents("tr");			current_tr = tr;		$("#he_item table").empty(); //清空核算界面		//当本行会计科目发生变化时，检查核算项目		if(tr.find("#AccCode").val() != null && tr.find("#AccCode").val() != tr.find("#AccCodeOld").val()){			jQuery.ajax({ type: "POST", url: "/VoucherManage.do?optype=queryComputeItem&queryMode=self&accCode="+tr.find("#AccCode").val(),async: false,dataType: "json",success: function(data){					var breed = data.isbreed;					var IsDept = breed[0].IsDept;					var IsPersonal = breed[0].IsPersonal;					var IsCash = breed[0].IsCash;					var IsClient = breed[0].IsClient;					var IsProvider = breed[0].IsProvider;					var isStock = breed[0].isStock;					var IsCome = breed[0].IsCome;					var IsProject = breed[0].IsProject;					var ComputeItem = "IsDept:"+IsDept+";"+"IsPersonal:"+IsPersonal+";"+"IsCash:"+IsCash+						";"+"IsClient:"+IsClient+";"+"IsProvider:"+IsProvider+";"+"isStock:"+isStock+";"+						"IsCome:"+IsCome+";"+"IsProject:"+IsProject+";";					tr.find("#ComputeItem").val(ComputeItem);						tr.find("#AccCodeOld").val(tr.find("#AccCode").val());			 }});		}	 				var ComputeItem = tr.find("#ComputeItem").val();		if(ComputeItem != ""){			var breedObj = new Object();			var breed = ComputeItem.split(";");			for(var i = 0;i<breed.length;i++){				if(breed[i] != ""){					var n = breed[i].split(":");					breedObj[n[0]] = n[1];				}			}			if(breedObj.IsDept==1){				htmls = "<tr><td style='padding-left:10px'>部门：<br/><input name='IsDeptName' id='IsDeptName' value='' onkeyup=\"showData(this,'SelectAccDepartment','IsDeptName','选择部门')\" ondblclick=\"selectPops(this,'SelectAccDepartment','IsDeptName','DepartmentCode','选择部门');\"/>";				htmls += "<img style='padding-left:10px;' src='/style1/images/St.gif' onClick=\"selectPops(this,'SelectAccDepartment','IsDeptName','DepartmentCode','选择部门');\" alt='选择部门' class='search'/></td></tr>";				$("#he_item table").append(htmls);				$("#IsDeptName").val(tr.find("#DeptName").val());			}			if(breedObj.IsPersonal==1){				htmls = "<tr><td style='padding-left:10px'>职员：<br/><input name='IsPersonalName' id='IsPersonalName' value='' onkeyup=\"showData(this,'SelectAccEmployee','IsPersonalName','选择个人')\" ondblclick=\"selectPops(this,'SelectAccEmployee','IsPersonalName','EmployeeIDs','选择个人');\"/>";				htmls += "<img style='padding-left:10px;' src='/style1/images/St.gif' onClick=\"selectPops(this,'SelectAccEmployee','IsPersonalName','EmployeeIDs','选择个人');\" alt='选择个人' class='search'/></td></tr>";				$("#he_item table").append(htmls);				$("#IsPersonalName").val(tr.find("#PersonalName").val());			}
			if(breedObj.IsProject==1){
				//核算项目
				htmls = "<tr><td style='padding-left:10px'>项目：<br/><input name='IsProjectName' id='IsProjectName' value='' onkeyup=\"showData(this,'SelectAccProjectInfo','IsProjectName','选择项目')\" ondblclick=\"selectPops(this,'SelectAccProjectInfo','IsProjectName','ProjectCode','选择项目');\"/>";
				htmls += "<img style='padding-left:10px;' src='/style1/images/St.gif' onClick=\"selectPops(this,'SelectAccProjectInfo','IsProjectName','ProjectCode','选择项目');\" alt='选择项目' class='search'/></td></tr>";
				$("#he_item table").append(htmls);
				$("#IsProjectName").val(tr.find("#ProjectName").val());
			}
			if(breedObj.isStock==1){
				htmls = "<tr><td style='padding-left:10px'>仓库：<br/><input name='isStockName' id='isStockName' value='' onkeyup=\"showData(this,'SelectAccStocks','isStockName','选择仓库')\" ondblclick=\"selectPops(this,'SelectAccStocks','isStockName','StockCode','选择仓库');\"/>";
				htmls += "<img style='padding-left:10px;' src='/style1/images/St.gif' onClick=\"selectPops(this,'SelectAccStocks','isStockName','StockCode','选择仓库');\" alt='选择仓库' class='search'/></td></tr>";
				$("#he_item table").append(htmls);
				$("#isStockName").val(tr.find("#StockName").val());
			}			if(breedObj.IsCome==1){				htmls = "<tr><td style='padding-left:10px'>往来单位：<br/><input name='IsComeName' id='IsComeName' value=''  onkeyup=\"showData(this,'SelectAccExpensed','IsComeName','选择往来单位')\" ondblclick=\"selectPops(this,'SelectAccExpensed','IsComeName','CompanyCode','选择往来单位');\"/>";				htmls += "<img style='padding-left:10px;' src='/style1/images/St.gif' onClick=\"selectPops(this,'SelectAccExpensed','IsComeName','CompanyCode','选择往来单位');\" alt='选择往来单位' class='search'/></td></tr>";				$("#he_item table").append(htmls);				$("#IsComeName").val(tr.find("#ComFullName").val());			}else{				if(breedObj.IsClient==1){					htmls = "<tr><td style='padding-left:10px'>客户：<br/><input name='IsClientName' id='IsClientName' value='' onkeyup=\"showData(this,'SelectAccClient','IsClientName','选择客户')\" ondblclick=\"selectPops(this,'SelectAccClient','IsClientName','CompanyCode','选择客户');\"/>";					htmls += "<img style='padding-left:10px;' src='/style1/images/St.gif' onClick=\"selectPops(this,'SelectAccClient','IsClientName','CompanyCode','选择客户');\" alt='选择客户' class='search'/></td></tr>";					$("#he_item table").append(htmls);					$("#IsClientName").val(tr.find("#ComFullName").val());				}else if(breedObj.IsProvider==1){					htmls = "<tr><td style='padding-left:10px'>供应商：<br/><input name='IsProviderName' id='IsProviderName' value='' onkeyup=\"showData(this,'SelectAccProvider','IsProviderName','选择供应商')\"  ondblclick=\"selectPops(this,'SelectAccProvider','IsProviderName','CompanyCode','选择供应商');\"/>";					htmls += "<img style='padding-left:10px;' src='/style1/images/St.gif' onClick=\"selectPops(this,'SelectAccProvider','IsProviderName','CompanyCode','选择供应商');\" alt='选择供应商' class='search'/></td></tr>";					$("#he_item table").append(htmls);					$("#IsProviderName").val(tr.find("#ComFullName").val());				}else{					htmls = "<tr><td style='padding-left:10px'>往来单位：<br/><input name='IsComeName' isCome='0' id='IsComeName' value='' onkeyup=\"showData(this,'SelectAccExpensed','IsComeName','选择往来单位')\"  ondblclick=\"selectPops(this,'SelectAccExpensed','IsComeName','CompanyCode','选择往来单位');\"/>";					htmls += "<img style='padding-left:10px;' src='/style1/images/St.gif' onClick=\"selectPops(this,'SelectAccExpensed','IsComeName','CompanyCode','选择往来单位');\" alt='选择往来单位' class='search'/></td></tr>";					$("#he_item table").append(htmls);					$("#IsComeName").val(tr.find("#ComFullName").val());				}			}				   	}	   	outtips();		//显示核算项目		if($("#tableItem tr:visible").index(tr) == $("#tableItem tr:visible").size()-2){  		 	addRow('add');		}	}	
	
	/* 插入*/
	function addRow(strtype){
		
		row = $("#tableItem tr:eq(1)").clone(true);
				$("#tableItem tr:eq("+($("#tableItem tr").index(current_tr))+")").after(row);		row.find("input").val("");		row.find("select").val("");
		
		/*获取当前最大row_id*/
		row.attr('row_id',global_row);
		row.find('.RowNum').val(global_row++);
		/****end****/
	}
	
	/* 根据币种查询汇率*/
	function selectrate(currencyID){
		var values = $(currencyID).val();
		if(values!=""){
			jQuery.ajax({ type: "POST", url: "/VoucherManage.do?optype=querySetExchange&exchange="+values+"&newYearPeriod="+newYearPeriod+"&newPeriod="+newPeriod,async: false,success: function(data){
					$(currencyID).parent().next().find("#CurrencyRate").val(data);
					accountMoney($(currencyID).parent().parent().find("#DebitAmount"));
					accountMoney($(currencyID).parent().parent().find("#LendAmount"));
				}
			});
		}else{
			$(currencyID).parent().next().find("#CurrencyRate").val('');
			$(currencyID).parent().parent().find("#DebitCurrencyAmount").val('');
			$(currencyID).parent().parent().find("#LendCurrencyAmount").val('');
			$(currencyID).parent().parent().find("#DebitAmount").val('');
			$(currencyID).parent().parent().find("#LendAmount").val('');
		}
	}
	
	/* 统计页面中有关金额值*/
	function account(){
		var sum = 0;
		var flag = false;
		jQuery("input[id='DebitAmount']").each(function(i,n){
			if($(n).val()!="" && isFloat($(n).val()) && $(n).val()!=0){
				var money = subStringMoney($(n).val());
				$(n).val(money);
				sum = accAdd(sum,money);
			}else{
				$(n).val('');
			}
		});
		$("#debit").text(subStringMoney(new String(sum)));
		sum = 0;
		jQuery("input[id='LendAmount']").each(function(i,n){
			if($(n).val()!="" && isFloat($(n).val()) && $(n).val()!=0){
				var money = subStringMoney($(n).val());
				$(n).val(money);
				sum = accAdd(sum,money);
			}else{
				$(n).val('');
			}
		});
		$("#lend").text(subStringMoney(new String(sum)));
		sum = 0;
		jQuery("input[id='DebitCurrencyAmount']").each(function(i,n){
			if($(n).val()!="" && isFloat($(n).val()) && $(n).val()!=0){
				var money = subStringMoney($(n).val());
				$(n).val(money);
				sum = accAdd(sum,money);
			}else{
				$(n).val('');
			}
		});
		$("#currencydebit").text(subStringMoney(new String(sum)));
		sum = 0;
		jQuery("input[id='LendCurrencyAmount']").each(function(i,n){
			if($(n).val()!="" && isFloat($(n).val()) && $(n).val()!=0){
				var money = subStringMoney($(n).val());
				$(n).val(money);
				sum = accAdd(sum,money);
			}else{
				$(n).val('');
			}
		});
		$("#currencylend").text(subStringMoney(new String(sum)));
	}
	
	
	/* 加载子表数据时取外币*/
	function querycurrency(tr_id,code,value,strtype){
		jQuery.ajax({ type: "POST", url: "/VoucherManage.do?optype=queryAccTemp&queryMode=super&accCode="+code,async: false,dataType: "json",success: function(data){
				var curr = data.currency;
				var currencyobject = $("input[value="+code+"]").parent().parent().find("select");
				if(strtype == "update"){
					currencyobject.append("<option value=''></option>"); 
				}
				
				for(var i = 0; i < curr.length; i++) {
				    var datas = curr[i];
				    var ifvalue = "";
				    if(value==datas.id){
				    	ifvalue = "selected";
				    }
				   
				   	currencyobject.append("<option value='"+datas.id+"' "+ifvalue+" >"+datas.value+"</option>");
				} 
				
				
	    	}
		});
	}
	/* 选择模板 */
	function choosemodule(id){
		window.location.href="/VoucherManage.do?operation=6&moduleId="+id;
	}
	
	/* 删除一行 */
	function delimg(objects,strtype){		if($("#tableItem tr").size()<4){			$(objects).parents("tr").find("input").val("");			$(objects).parents("tr").find("select").val("");		}else{		
			$(objects).parent().parent().remove();		}
	  	account();
	}