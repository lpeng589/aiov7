<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("mrp.lb.mrpCount")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="$globals.js("/js/mrp.vjs","",$text)"></script>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/dragcols.js"></script>
<script language="javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<style type="text/css">
#tblSort{
	width: 50%;
}
</style>

<script>
var cbNames = new Array();//复选框name数组
var ckdCbNames = new Array();//被选中的复选框数组。

function m(value){
	return document.getElementById(value) ;
}

function validateCCChoosed(){//供应商检查
    var keyIds=document.getElementsByName("keyId");
    for(var i=0;i<keyIds.length;i++){
	   if(keyIds[i].checked){
	     	var ccIdObj=document.getElementById("ccIdId_"+keyIds[i].value);
		 	if(!ccIdObj.value){//查看选中的物料是否都有供应商，没有则返回false
				return false;
			}
	   }
	}
	return true;
}

//选中一个商品
function chooseAGoods(obj,cbNum){
	var nextVal = $('ccIdId_'+(cbNum+1)).value;
	nextVal = nextVal?nextVal:"-1";
	m('ccIdId_'+cbNum).value=obj.checked?nextVal:'';
	checkAll("keyId",obj.checked)
}

function validateChoosedGoods(){//选中商品检查
	for(var i=cbNames.length-1;i>=0;i--){//检查所有商品代表的复选框是否有被选中的。
		var goodsObj = document.getElementsByName(cbNames[i])[0];//商品标识对象
		if(goodsObj.checked){//如果有一个商品被选中则返回true,否则执行下一次循环
			return true;
		}else{
			continue;
		}
		return false;//如果没有一个商品被选中则返回false;
	}
}

function selCompany(){
	openSelect('/UserFunctionAction.do?tableName=tblBuyOrder&fieldName=CompanyCode&operation=22',this,'CompanyCode')
}

function returnOrder(){
	var companyId = m("companyId").value;
	if(!companyId){
		alert("$text.get('mrp.lb.pleaseChooseProvider')");
		return false;
	}
	window.returnValue=companyId;
	var form = document.getElementsByName("form")[0];
	form.action="/MrpBP.do?method=saveBuyOrder";
	form.submit();
}	

var obj;
function openSelectBuyPerson(obj,index){
	window.obj = obj;  
	var urlstr  = "/UserFunctionAction.do?tableName=tblEmployee&selectName=SelectEmployee&operation=22&popupWin=SelectPerson&BillDate="+form.BillDate.value+"&MOID=83e8760a_0811132128219840114&MOOP=add"; 
	window.index = index;
	asyncbox.open({id:'SelectPerson',title:'选择供应商',url:urlstr,width:800,height:470}); 
}

function exeSelectPerson(str){
	if(!str) return;//如果没有返回数据
	var fs=str.split(";"); 
	document.getElementsByName("buyPerson")[index-1].value=fs[0];//代码
	document.getElementsByName("buyPersonName")[index-1].value=fs[3];//名称
}

var obj;
var field;
var index;
function openSelect(urlstr,obj,field,index){
	window.obj = obj;
	window.field = field;
	window.index = index;  
	var urlstr = urlstr+"&popupWin=SelectPopup&BillDate="+form.BillDate.value+"&MOID=83e8760a_0811132128219840114&MOOP=add"; 
	asyncbox.open({id:'SelectPopup',title:'选择',url:urlstr,width:800,height:470}); 
}

function exeSelectPopup(str){
	if(!str)return;//如果没有返回数据
	fs=str.split(";"); 
	if(field=="companyCode"){//如果点击的是列中的供应商
		document.getElementsByName("companyCode")[index-1].value=fs[0];//代码
		document.getElementsByName("companyName")[index-1].value=fs[2];//名称
		return;
	}else if(field=="stockCode"){
		document.getElementsByName("stockCode")[index-1].value=fs[0];//代码
		document.getElementsByName("stockName")[index-1].value=fs[1];//名称
		return;
	}
	
	//保存字段
	for(var k=cbNames.length-1;k>=0;k--){
		var cbs = document.getElementsByName(cbNames[k]);//全部复选框
		var ckdCbIndexs = getCheckedIndex(cbNames[k]);//选中的复选框下标
		for(var i=ckdCbIndexs.length-1;i>=0;i--){
			var ccIdId = "ccIdId_"+cbs[ckdCbIndexs[i]].value;
			var ccNameId = "ccNameId_"+cbs[ckdCbIndexs[i]].value;
			if(document.getElementById(ccIdId)){
				document.getElementById(ccIdId).value=fs[0];
				document.getElementById(ccNameId).value=fs[1];
			}
		}
	}
	if(field == "CompanyCode"){
		var saveField=document.getElementsByName("CompanyCode");	
		saveField[0].value = fs[0];			
		var obj  = document.getElementById("tbl_tblCompany_classCode");		
		if(obj != null){
			obj.value = fs[0];
		}
							
		var saveField=document.getElementsByName("tblCompany_ComFullName");
		saveField[0].value = fs[1];
		var obj  = document.getElementById("tbl_tblCompany_ComFullName");
		if(obj != null){
			obj.value = fs[1];
		}
	}
				
	return 0;
}

//主表异步查询
function mainSelect(urlstr,openUrl,obj,field){
	var url = urlstr+"&MOID=83e8760a_0811132128219840114&MOOP=add&selectField="+obj.name+"&selectValue="+obj.value;
	new Ajax.Request(url,
   {
         method:'get',
         parameters: {},
         onSuccess: function(transport){
             var response = transport.responseText;
			 if(response == "" || response == null){
				openSelect(openUrl,obj,field);
			 }else if(response.indexOf("#") >= 0){
			 	var array = response.split("#");
				var arr = obj.name.split("_");
				openUrl = openUrl+"&"+arr[0]+"."+arr[1]+"="+array[1]+"&url=@URL:";
				
				openSelect(openUrl,obj,field);
			 }else if(response.indexOf(";") >= 0){
				setFieldValue(response,field);
			 }
         },
         onFailure: function(){  }
     });
}
function isCheckField(obj,fieldName){
	var name = document.getElementById("tbl_"+obj.name).value;  
	if(obj.value != name){
		resetSubmit(fieldName);		
	}
}

function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}

//保存到采购订单
function saveBuyOrder(){	
	var cbs = n("cb");
	var chooses=n("choose");
	var companyCodes=n("companyCode");
	var qtys=n("qty");
	if(!isChecked("keyId")){		
		alert('$text.get("mrp.doOrder.materiel")');
		return false;
	}
	
	if(!validateDigit("qty","$text.get("mrp.lb.buy")",$globals.getSysSetting("DigitsQty"),false))return false;
	
	for(var i=0;i<qtys.length;i++){
		if(chooses[i].value=="true"&&(!isFloat(qtys[i].value)||Number(qtys[i].value)<=0)){
			alert("#if($!method=="doOrder")$text.get("mrp.lb.buy")#else$text.get("mrp.lb.quote")#end$text.get("mrp.msg.qtyBigZero")");
			qtys[i].focus();
			return false;
		}
	}
	#if($!method=="doOrder")
	for(var i=0;i<chooses.length;i++){
		if(chooses[i].value=="true"&&companyCodes[i].value.length==0){
			alert("$text.get('mrp.doOrder.chooseSupOfMat')");
			return false;
		}
	}
	#else
	for(var i=0;i<chooses.length;i++){
		if(chooses[i].value=="true"&&qtys[i].value==0){
			alert("$text.get('mrp.doOrder.chooseQtyOfMat')");
			return false;
		}
	}
	#end
	if(!confirm('#if($!method=="doOrder")$text.get("mrp.msg.saveBuySure")#else$text.get("mrp.msg.saveQuoteSure")#end')){return false;}
	var form=n("form")[0];
	#if($!method=="doOrder")
	form.action = "/MrpBP.do?method=saveBuyOrder";
	#else
	form.action = "/MrpBP.do?method=saveQuoteBuy";
	#end
	
	if(event!=null && event.srcElement.name=="deliverToBut"){
		form.deliverTo.value="true";
	}else{
		form.deliverTo.value="";
	}
	suopin();
	form.submit();
}

function beforSubmit(form){   
	//验证主表小数位
	if(!validateDigit('TaxAmount','$text.get("mrp.lb.totalPrice")',0,true))return false;	
	if(!validateDigit('FreightAmount','$text.get("mrp.lb.conveyancePrice")',0,true))return false;
	if(!validateDigit('tblBuyOrderDet_Qty','$text.get("mrp.lb.count")',0,false))return false;	
	if(!validateDigit('tblBuyOrderDet_Price','$text.get("mrp.lb.price")',0,false))return false;	
	if(!validateDigit('tblBuyOrderDet_Amount','$text.get("mrp.lb.price2")',0,false))return false;	
																																																															if(!validateDigit('tblBuyOrderDet_UnitQty','辅助单位数量',0,false))return false;	
	if(!validateDigit('tblBuyOrderDet_UnitPrice','$text.get("mrp.lb.assistantUnitPrice")',0,false))return false;	
																																				
	if(!validate(form))return false;
	disableForm(form);
	return true;
}
	
//生成订单编号/日期
function setBillNo(){
	var billNo = "BO";
	var date = new Date();
	year = date.getFullYear();
	month = date.getMonth()+1+"";
	theDay = date.getDate()+"";
	second = date.getSeconds();
	if(month.length==1)
		month="0"+month;
	if(theDay.length==1)
		theDay="0"+theDay;
	n("BillDate")[0].value=year+"-"+month+"-"+theDay
}

function checkIsNum(obj){
	var val = parseFloat(obj.value);
	if(isNaN(val)){
		val=0;
	}
	obj.value=val;
}
	
function checkAll(name,ckAll){
	var cbs = document.getElementsByName(name);
	for(i=0;i<cbs.length;i++){
		if(!cbs[i].disabled){
			cbs[i].checked=ckAll;
		}
	}
	var chooses = document.getElementsByName("choose");
	for(i=0;i<chooses.length;i++){
		if(!cbs[i].disabled){
			chooses[i].value=ckAll;
		}
	}
}

function selectIt(chk){
	var chooses = document.getElementsByName("choose");
	var value = parseInt(chk.value)-1;
	for(i=0; i<chooses.length; i++){
		if(i==value){
			if(chk.checked){
				chooses[i].value="true";
			}else{
				chooses[i].value="false";
			}
		}
	}
}
	
function uniteGood(){
	form.action="/MrpBP.do?method=uniteSameGood";
	form.target="";
	form.submit();
}

function deployChange(obj,path){
	if(obj.src.indexOf("/$globals.getStylePath()/images/tree/nolines_plus.gif")>=0){
		obj.src="/$globals.getStylePath()/images/tree/nolines_minus.gif";		
		var items = document.getElementsByTagName("tr");
		for(var i=0;i<items.length;i++){	
			if(items[i].getAttribute("mainPath") != undefined ){
				if(path==items[i].getAttribute("mainPath")){
					items[i].style.display ="block";
				}
			}		
		}
	}else{
		obj.src="/$globals.getStylePath()/images/tree/nolines_plus.gif";
		var items = document.getElementsByTagName("tr");
		for(var i=0;i<items.length;i++){	
			if(items[i].getAttribute("mainPath") != undefined ){
				if(path==items[i].getAttribute("mainPath")){
					items[i].style.display ="none";
				}
			}		
		}
	}
}

function print(urls){
	asyncbox.open({id:'print',title:'打印单据',url:urls,width:300,height:280});
}

function suopin(){
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
}
function jiesuo(){
	if(typeof(top.junblockUI)!="undefined"){
		top.junblockUI();
	}
}
</script>
</head>
<body onLoad="setBillNo();sortables_init('tblSort');jiesuo();">
<iframe name="formFrame" style="display: none"></iframe>
<form method="post" scope="request" name="form" action="/MrpBP.do?method=saveBuyOrder"  target="formFrame">
<input type="hidden" name="mrpFrom" value="$!mrpFrom">
<input type="hidden" name="submitType" value="$!method">
<input type="hidden" name="deliverTo" value="">
<input type="hidden" id="retValUrl" value="">
<input type="hidden" name="ProductMRPIds" value="$!ProductMRPIds">
<input type="hidden" name="method" value="$!method">
<div class="Heading">
	<div class="HeadingIcon">
		<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
	</div>
	<div class="HeadingTitle">
	#if($!method=="doOrder") $text.get("mrp.lb.mrpBuyOrder") #else $text.get("mrp.lb.mrpQuoteBuyOrder") #end
	</div>
	<ul class="HeadingButton">
		#if($globals.getSysSetting("showMrpProc").equals("true"))
		<li><button type="button" onClick="uniteGood()" class="b4">$text.get("mrp.lb.uniteGood")</button></li>
		#end
		#if($!isOpenWork&&$!method!="doOrder")
		<li><button type="button" name="copySave" title="Ctrl+S" onClick="return saveBuyOrder()" class="b2">$text.get("common.lb.TemporarilySave")</button></li>
		<li><button type="button" name="deliverToBut" title="Ctrl+S" onClick="return saveBuyOrder()" class="b2">$text.get("common.lb.DeliverTo")</button></li>	
		#else
		<li><button type="button" onClick="return saveBuyOrder()" class="b4">
			#if($!method=="doOrder")$text.get("mrp.lb.buyBom")#else$text.get("mrp.lb.quoteBuyBom")#end</button>
		</li>
		#end
		
		#if($globals.getSysSetting("showMrpProc").equals("true"))
		<li><button type="button" onClick="location.href='/MrpBP.do?method=bomDemand&opeType=BackMrp&ProductMRPIds=$!ProductMRPIds'" class="b2">$text.get("mrp.lb.back")</button></li>
		#else
		<li><button type="button" onClick="suopin();location.href='/MrpBP.do?method=bomDemand&opeType=BackMrp&mrpFrom=$!mrpFrom'" class="b2">$text.get("mrp.lb.back")</button></li>
		#end
		#if($LoginBean.operationMap.get("/MrpBP.do").print())
		#if($!method=="doOrder")
		<li><button type="button" onClick='print("/ReportDataMrpAction.do?operation=printActiveX&reportNumber=MrpDoOrder");'
				class="b2">$text.get("common.lb.print")</button>
		</li>
		#else
		<li>
			<button type="button" onClick='print("/ReportDataMrpAction.do?operation=printActiveX&reportNumber=MrpApplication");'
				class="b2">$text.get("common.lb.print")</button>
		</li>
		#end 
		#end
	</ul>
	<div class="listRange_Pop-up">
		<ul class="listRange_1_Pop-up">
			<li><span>$text.get("mrp.lb.billNo")：</span>
				<input name="BillNo" type="text" size="20" onKeyDown="if(event.keyCode==13) event.keyCode=9"
								value="$!BillNo" readonly="readonly">
			</li>
			<li><span>$text.get("mrp.lb.billDate")：</span>
					<input id="BillDate" name="BillDate" type="text" size="20" onClick="openInputDate(this);" value="">
			</li>
		</ul>
	</div>
	<div class="scroll_function_small_a" id="conter">
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
		<thead>
			<tr style="position:relative;top:expression(this.offsetParent.scrollTop);">
				<td style="width:30px;">&nbsp;</td>
				<td style="width:30px;"><input type="checkbox" name="cb" onClick="checkAll('keyId',this.checked)" value="" /></td>
				<td style="width:100px;">$text.get("mrp.lb.goodsNumber")</td>
				<td style="width:250px;">$text.get("mrp.lb.goodsFullName")</td>
				<td style="width:110px;">$text.get("iniGoods.lb.goodsSpec")</td>
				<td style="width:80px;">$text.get("call.lb.unit")</td>
				<td style="width:80px;">$text.get("mrp.lb.storeQty")</td>
				#if($!method=="doQuoteBuy")
				<td style="width:100px;">$text.get("mrp.lb.buyPerson")...</td>
				#end 
				#foreach($prop in $propList)
				#if($prop.isUsed==1&&!$!prop.propName.equals("Seq")&&$globals.getFieldBean("tblBOM",$!prop.propName))
				<td style="width:80px;">$prop.display.get($!locale)</td>
				#end 
				#end
				<td style="width:100px;">$text.get("mrp.lb.leastQty")</td>
				<td style="width:100px;">$text.get("mrp.lb.buyOrderQty")</td>
				<td style="width:100px;">$text.get("iniGoods.lb.stock")#if($!method!="doQuoteBuy")...#end</td>
				#if($!method=="doOrder")
				<td style="width:100px;">$text.get("call.lb.afford")...</td>
				#end
			</tr>
		</thead>
		<tbody>
		#foreach($row in $list)
		<input type="hidden" name="goodsCode" value="$!row.get("goodsCode")"/>
		<input type="hidden" name="choose" value="false"/>
		<input type="hidden" name="trackNo" value="$!row.get("trackNo")"/>
		<input type="hidden" name="bomdetId" value="$!row.get("bomdetId")"/>
		<input type="hidden" name="goodsNumber" value="$!row.get("goodsNumber")"/>
		<input type="hidden" name="store" value="$!row.get("store")"/>
		<input type="hidden" name="mainPath" value="$!row.get("mainPath")"/>
		<input type="hidden" name="isPlace" value="$!row.get("isPlace")"/>
		<input type="hidden" name="existsPlace" value="$!row.get("existsPlace")"/>
		<input type="hidden" name="path" value="$!row.get("path")"/> 
		<input type="hidden" name="submitDate" value="$!row.get("submitDate")"/> 
		<input type="hidden" name="price"value="$!globals.formatNumber($!row.get("price"),false,false,$!globals.getSysIntswitch(),"tblBOM","Price",true)"/>
		<tr mainPath="$!row.get("mainPath")" #if($!row.get("isPlace")=="true")style="display:none"#end>
			<td>$velocityCount</td>
			<td><input type="checkbox" class="cbox" name="keyId" onclick="selectIt(this);" value="$velocityCount" #if($!row.get("existsPlace"))disabled="disabled"#end></td>
			<td title="$!row.get("goodsNumber")">#if($!row.get("existsPlace")) <img src="/$globals.getStylePath()/images/tree/nolines_plus.gif" onClick="deployChange(this,'$!row.get("path")')"  />#end$!row.get("goodsNumber")&nbsp;</td>
			<td title="$!row.get("goodsFullName")">$!row.get("goodsFullName")&nbsp;</td>
			<td title="$!row.get("goodsSpec")">$!row.get("goodsSpec")&nbsp;</td>
			<td title="$!row.get("unitName")">$!row.get("unitName")&nbsp;</td>
			<td>$!row.get("store")&nbsp;</td>
			#if($!method=="doQuoteBuy")
			<input type="hidden" name="buyPerson" id="buyPerson" value="$!row.get(" buyPerson")"/>
			<td><input id="buyPersonName" style="width:90px;" readonly="readonly" title="$text.get('mrp.doOrder.dblclickbuyPerson')" name="buyPersonName" value="$!row.get("buyPersonName")" onDblClick="openSelectBuyPerson(this,$velocityCount);" /></td>
			#end 
			#foreach($prop in $propList)
			#if($prop.isUsed==1&&!$!prop.propName.equals("Seq")&&$globals.getFieldBean("tblBOM",$!prop.propName))
			#if($prop.getPropName().equals("Hue")||$prop.getPropName().equals("yearNO")||$prop.getPropName().equals("User1")
			||$prop.getPropName().equals("User2")||$prop.getPropName().equals("Design")||$prop.getPropName().equals("color"))
			#set($field=$prop.propName+"Val")
			<input type="hidden" name="$!prop.propName" value="$!row.get($!field)"/>			
			#if($prop.getPropName().equals("color"))
			<td><input type="text" style="width: 70px;" name="colorName" value="$!row.get($prop.propName)">&nbsp;</td>
			#else
			<td>$!row.get($prop.propName)&nbsp;</td>
			#end
			#else
			<td><input type="text" style="width: 70px;" name="$!prop.propName" value="$!row.get($prop.propName)">&nbsp;</td>
			#end #end #end
			<td>$!row.get("leastQty")&nbsp;</td>
			<td><input type="text" style="width: 90px;" name="qty" value="$!globals.formatNumber($!row.get("qty"),false,false,$!globals.getSysIntswitch(),"tblBOM","qty",true)">&nbsp;</td>
			<input type="hidden" name="stockCode" id="stockCode" value="$row.get("stockCode")"  />
			<td><input id="stockName" style="width: 90px;" value="$row.get("stockName")" name="stockName" readonly="readonly" #if($!method!="doQuoteBuy") title="$text.get('mrp.doOrder.dblclickStock')" onDblClick="openSelect('/UserFunctionAction.do?tableName=tblBuyOrder&fieldName=StockCode&operation=22',this,'stockCode',$velocityCount)" #end /></td>
			#if($!method=="doOrder")
			<input type="hidden" name="companyCode" id="companyCode" value="$!row.get("companyCode")"/>
			<td><input id="companyName" style="width: 90px;" readonly="readonly" title="$text.get('mrp.doOrder.dblclickHelp1')" name="companyName" value="$!row.get("companyName")" onDblClick="openSelect('/UserFunctionAction.do?tableName=tblBuyOrder&fieldName=CompanyCode&operation=22',this,'companyCode',$velocityCount)" /></td>
			#end
		</tr>
		#end
		</tbody>
	</table>
</div>
</form>
</body>
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.documentElement.clientHeight-80;
oDiv.style.height=sHeight+"px";
</script>
</html>
