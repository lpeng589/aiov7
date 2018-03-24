<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("mrp.lb.mrpCount")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">		
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/setTime.js"></script>
<script type="text/javascript" src="$globals.js("/js/mrp.vjs","",$text)"></script>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/dragcols.js"></script>
<script>
function selCompany(){
	openSelect('/UserFunctionAction.do?tableName=tblBuyOrder&fieldName=CompanyCode&operation=22',this,'CompanyCode')
}

function returnOrder(){
	var companyId = $("companyId").value;
	if(!companyId){
		alert("$text.get('mrp.lb.pleaseChooseProvider')");
		return false;
	}
	window.returnValue=companyId;
	//window.close();
	var form = document.getElementsByName("form")[0];
	form.action="/MrpBP.do?method=saveBuyOrder";
	form.submit();
}	

function openSelect(urlstr,obj,field){  
	var str  = window.showModalDialog(urlstr+"&MOID=83e8760a_0811132128219840114&MOOP=add","","dialogWidth=730px;dialogHeight=450px"); 
	fs=str.split(";");  
	//保存字段
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
	new Ajax.Request(url,{
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

function saveBuyOrder(){
	var cb = getCbCheckedStr("keyId");
	var form=n("form")[0];
	form.action = "/MrpBP.do?method=saveBuyOrder&cb="+cb;
	form.submit();
	window.close();
}

function beforSubmit(form){   
	//验证主表小数位
	if(!validateDigit('TaxAmount','$text.get("mrp.lb.totalPrice")',0,true))return false;	
	if(!validateDigit('FreightAmount','$text.get("mrp.lb.conveyancePrice")',0,true))return false;
	if(!validateDigit('tblBuyOrderDet_Qty','$text.get("mrp.lb.count")',0,false))return false;	
	if(!validateDigit('tblBuyOrderDet_Price','$text.get("mrp.lb.price")',0,false))return false;	
	if(!validateDigit('tblBuyOrderDet_Amount','$text.get("mrp.lb.price2")',0,false))return false;																																																														if(!validateDigit('tblBuyOrderDet_UnitQty','辅助单位数量',0,false))return false;	
	if(!validateDigit('tblBuyOrderDet_UnitPrice','$text.get("mrp.lb.assistantUnitPrice")',0,false))return false;																																			
	if(!validate(form))return false;
	disableForm(form);
	return true;
}

//生成订单编号/日期
function setBillNo(){
	var billNo = "PB";
	var date = new Date();
	year = date.getFullYear();
	month = date.getMonth()+1+"";
	theDay = date.getDate()+"";
	second = date.getSeconds();
	if(month.length==1)
		month="0"+month;
	if(theDay.length==1)
		theDay="0"+theDay;
	billNo+=year+month+theDay+second;		
	//n("BillNo")[0].value=billNo;
	n("BillDate")[0].value=year+"-"+month+"-"+theDay
}

function mrpIsChecked(){
	var items = document.getElementsByName("keyId");
	var isProduces=document.getElementsByName("isProduce");
	for(var i=0;i<items.length;i++){
	    if(items[i].checked&&isProduces[i].value=="true"){
	    	return true;
	    }
	}
	return false;
}

function checkIsNum(obj){
	var val = parseFloat(obj.value);
	if(isNaN(val)){
		val=0;
	}
	obj.value=val;
}

function saveProduceOrder(){
	if(!mrpIsChecked()){		
		alert('$text.get("mrp.doOrder.goods")');
		return false;
	}
	var chooses=n("choose");
	var qtys=n("qty");
	for(var i=0;i<qtys.length;i++){
		if(chooses[i].value=="true"&&(!isFloat(qtys[i].value)||Number(qtys[i].value)<=0)){
			alert("$text.get("mrp.msg.qtyBigZero")");
			qtys[i].focus();
			return;
		}
	}
	if(!confirm('$text.get("mrp.msg.sureSave")')){return;};	
	var form=n("form")[0];
	suopin();
	form.action = "/MrpBP.do?method=saveProduceOrder";
	form.submit();
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
	
function uniteGood(){
	form.action="/MrpBP.do?method=uniteProSameGood";
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
<style type="text/css">
.resizeDiv{width:2px;overflow:hidden; float:right; cursor:col-resize;}
.listRange_list_function thead td{
	padding-left:1px;
	padding-right:1px;
}
</style>
</head>
<body onLoad="setBillNo();sortables_init('tblSort');jiesuo();">
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" scope="request" name="form" action="" target="formFrame">
<input type="hidden" name="mrpFrom" value="$!mrpFrom">
<input type="hidden" name="ProductMRPIds" value="$!ProductMRPIds">
<div>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("mrp.lb.mrpProduceOrder")<br></div>
	<ul class="HeadingButton">
		#if($globals.getSysSetting("showMrpProc").equals("true"))
		<li>
			<button type="button" onClick="uniteGood()" class="b4">$text.get("mrp.lb.uniteGood")</button>
		</li>
		#end
		<li>
			<button type="button" onClick="saveProduceOrder()">$text.get("mrp.lb.saveAdd")</button>
		</li>
		<li>
		#if($globals.getSysSetting("showMrpProc").equals("true"))
			<button type="button" onClick="location.href='/MrpBP.do?method=bomDemand&opeType=BackMrp&choose=true&ProductMRPIds=$!ProductMRPIds'" class="b2">$text.get("mrp.lb.back")</button>
		#else
			<li><button type="button" onClick="suopin();location.href='/MrpBP.do?method=bomDemand&opeType=BackMrp&mrpFrom=$!mrpFrom'" class="b2">$text.get("mrp.lb.back")</button></li>
		#end
		</li>
		#if($LoginBean.operationMap.get("/MrpBP.do").print())		
		<li><button type="button" onClick='window.showModalDialog("/ReportDataMrpAction.do?operation=printActiveX&reportNumber=MrpDoProduct",window,"dialogWidth=208px;dialogHeight=285px,scroll=no;")' class="b2">$text.get("common.lb.print")</button></li>
		#end
	</ul>
</div>
<div id="listRange_id">	
	<div class="listRange_1">

							<li>
								<span>$text.get("mrp.lb.billNo")：</span>
								<input name="BillNo" type="text" size="20"
									onKeyDown="if(event.keyCode==13) event.keyCode=9"
									value="$!BillNo" readonly="readonly">
							</li>

							<li>
								<span>$text.get("mrp.lb.billDate")：</span>
								<input id="BillDate" name="BillDate" type="text" size="20" onClick="openInputDate(this);" value="">
						
							
						</div>
<div class="scroll_function_small_a" id="conter">
			<script type="text/javascript">
				var oDiv=document.getElementById("conter");
				var sHeight=document.body.clientHeight-92;
				oDiv.style.height=sHeight+"px";
			</script>
						<table border="0" cellpadding="0" cellspacing="0"
							class="listRange_list_function" name="table" id="tblSort">
							<thead>
								<tr height="25" style="position:relative;top:expression(this.offsetParent.scrollTop);">
									<td width="30">&nbsp;</td>
									<td width="30">
										<input type="checkbox" name="cb" onClick="checkAll('keyId',this.checked)">
										&nbsp;
									</td>
									<td> 
										$text.get("mrp.lb.goodsNumber")
									</td>
									<td>
										$text.get("mrp.lb.goodsFullName")
									</td>
									<td width="250">
										$text.get("iniGoods.lb.goodsSpec")
									</td>
									<td width="80">
										$text.get("call.lb.unit")
									</td>
									#foreach($prop in $propList)
										#if($prop.isUsed==1&&!$!prop.propName.equals("Seq")&&$globals.getFieldBean("tblBOM",$!prop.propName))
											<td width="100">$prop.display.get($!locale)</td>
										#end
									#end
									<td>
										$text.get("iniGoods.lb.stock")
									</td>
									<td width="100">$text.get("mrp.lb.storeQty")</td>
									<td>
										$text.get("mrp.lb.count")
									</td>
								</tr>


							</thead>
							<tbody>
								#foreach($row in $result)
								<tr mainPath="$!row.get("mainPath")" #if($!row.get("isPlaceh")=="true")style="display:none"#end>
									<td>$velocityCount</td>
									<td width="30">
										<input type="checkbox" name="keyId" value="$velocityCount" onclick="selectIt(this);" #if(!$!row.get("existsPlace")&&$!row.get("needProduct")!=0)checked="true"#end #if($!row.get("existsPlace"))disabled="disabled"#end>
										<input type="hidden" name="goodsCode" value="$!row.get("goodsCode")">
										<input type="hidden" name="goodPropHash" value="$!row.get("goodPropHash")">
										<input type="hidden" name="MaterielAttribute" value="$!row.get("MaterielAttribute")">
										<input type="hidden" name="choose" value="#if(!$!row.get("existsPlace")&&$!row.get("needProduct")!=0)true#{else}false#end">
										<input type="hidden" name="trackNo" value="$!row.get("trackNo")">
										<input type="hidden" name="bomdetId" value="$!row.get("bomdetId")">
										<input type="hidden" name="mainDetId" value="$!row.get("mainDetId")">
										<input type="hidden" name="isProduce" value="$!row.get("isProduce")">
										<input type="hidden" name="goodsNumber" value="$!row.get("goodsNumber")">
										<input type="hidden" name="goodsFullName" value="$!row.get("goodsFullName")">
										<input type="hidden" name="price" value="$!row.get("price")">
										<input type="hidden" name="unitName" value="$!row.get("unitName")">
										<input type="hidden" name="store" value="$!row.get("store")">
										<input type="hidden" name="qty" value="$!row.get("needProduct")">
										<input type="hidden" name="mainPath" value="$!row.get("mainPath")"/>
										<input type="hidden" name="isPlace" value="$!row.get("isPlace")"/>
										<input type="hidden" name="isPlaceh" value="$!row.get("isPlaceh")"/>
										<input type="hidden" name="existsPlace" value="$!row.get("existsPlace")"/>
										<input type="hidden" name="path" value="$!row.get("path")"/> 
										<input type="hidden" name="startDate" value="$!row.get("startDate")"/> 
										<input type="hidden" name="submitDate" value="$!row.get("submitDate")"/> 
									</td>
									<td title="$!row.get("goodsNumber")">#if($!row.get("existsPlace")) <img src="/$globals.getStylePath()/images/tree/nolines_plus.gif" onClick="deployChange(this,'$!row.get("path")')"  />#end
										$!row.get("goodsNumber") &nbsp;
									</td>
									<td title="$!row.get("goodsFullName")">
										$!row.get("goodsFullName") &nbsp;
									</td>
									<td title="$!row.get("goodsSpec")">
										$!row.get("goodsSpec") &nbsp;
									</td>
									<td>
										$!row.get("unitName") &nbsp;
									</td>
									#foreach($prop in $propList)
										#if($prop.isUsed==1&&!$!prop.propName.equals("Seq")&&$globals.getFieldBean("tblBOM",$!prop.propName))
									<td>$!row.get($prop.propName)&nbsp;</td>
										#end
									#end
									<td>
										<input type="hidden" name="stockCode" id="stockCode"  value="$row.get("stockCode")"  />
										<input id="stockName" name="stockName" value="$row.get("stockName")" title="$row.get("stockName")"  readonly="readonly" />
									</td>
									<td>$!row.get("store")&nbsp;</td>
									<td>
										$!row.get("needProduct")
									</td>
								</tr>
								#end

							</tbody>
						</table>
					</div>
			</div>
		</form>
	</body>
</html>
