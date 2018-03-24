<!DOCTYPE html>
<html>
<head>
<title>易站通下单</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="/mobile/css/themes/default/jquery.mobile-1.4.5.min.css">
<link rel="stylesheet" type="text/css" href="/mobile/css/scrollbar.css">
<link rel="stylesheet" type="text/css" href="/mobile/css/mobile.css">

<link rel="shortcut icon" href="/favicon.ico">
<script src="/mobile/js/jquery.min.js"></script>
<script src="/mobile/js/jquery.mobile-1.4.5.min.js"></script>
<script src="/mobile/js/mobileapi.min.js"></script>
<script type="text/javascript">  
$(document).ready(function() { 
	//本部分是从微信消息直接链接打开后进入的部分，否则如果从列表进入这个头部会被截掉
	keyId = getUrlkeyId(); //从tzESales.jsp?keyId=2301202 url地址中取keyId参数
	update(keyId); //查询单据并显示内容
	$("#backbt").hide(); //独立运行时要隐藏返回按扭
});
</script>
</head>
<body>
<div class="pull-demo-page"  data-role="page" id="pageUpdate">
  <div data-role="header" data-position="fixed" data-tap-toggle="false" data-disable-page-zoom="false" style="position: fixed;">
    <a href="#pageone" data-transition="flip" id="backbt" data-icon="back" data-role="button">返回</a>
    <h1 id="titleh1">易站通 </h1>
    <a href="javascript:showOperation()" data-role="button" id="updateOpbt"  class="ui-btn-right" data-icon="check">操作</a>
    <span id="detailBtDiv" class="dropOperations" style="display:none; position: absolute;  right: 5px;  top: 40px;  background-color: #fff;  padding: 5px;  border: 1px solid #C9CBC5;  border-radius: 0 0 8px 8px;"   >
    	<ul id="detailBt" >
    	</ul>
    </span>
  </div>

  <div data-iscroll="" data-role="content" id="pageUpdateContent"><!-- 修改的界面显示 -->
	<ul data-role="listview">
      <li data-role="list-divider"  for="agentName">代理信息</li>
      <li  for="agentName"><label class="ilabel" style="display:inline-block" for="agentName">代理名称：</label><span id="agentNameSpan"></span><input type="hidden" id="agentName" /></li>
      <li  for="agentName"><label class="ilabel" style="display:inline-block" for="agentDistrict">所属地区：</label><span id="agentDistrictSpan"></span><input type="hidden" id="agentDistrict" /></li>
      <li data-role="list-divider"><span style="  padding-top: 15px;  display: inline-block;">客户信息 </span>
      	<span style="  width: 80px;  height: 30px;  display: inline-block;  float: right;" id="selectClientBt">
      		<button onclick="selectClient()" style="margin:0px" data-inline="true" data-mini="true" >选择客户</button>
      	</span>
      	<span style="  width: 80px;  height: 30px;  display: none;  float: right;" id="showHitBt">
      		<button onclick="showHit()" style="margin:0px" data-inline="true" data-mini="true" >撞单检查</button>
      	</span></li>
      <li><label class="ilabel" style="display:inline-block" for="tzclient_clientName">客户名称：</label><span id="tzclient_clientNameSpan"></span><input type="hidden" id="clientId" /></li>
      <li><label class="ilabel" style="display:inline-block" for="tblDistrict_DistrictFullName">所在城市：</label><span id="tblDistrict_DistrictFullNameSpan"></span></li>
	  <li><label class="ilabel" style="display:inline-block" for="tzclient_materialCount">素材数量：</label><span id="tzclient_materialCountSpan"></span></li>
	  <li><label class="ilabel" style="display:inline-block" for="tzclient_connect">联系人：</label><span id="tzclient_connectSpan"></span></li>
	  <li><label class="ilabel" style="display:inline-block" for="tzclient_tel">固定电话：</label><span id="tzclient_telSpan"></span></li>
	  <li><label class="ilabel" style="display:inline-block" for="tzclient_address">详细地址：</label><span id="tzclient_addressSpan"></span></li>	  
	  <li><label class="ilabel" style="display:inline-block" for="oldproduct">原订购产品：</label><span id="oldproductSpan"></span><input type="hidden" id="oldproduct" /></li>
	  <li for="oldMoney"><label class="ilabel" style="display:inline-block" for="oldMoney">原单金额：</label><span id="oldMoneySpan"></span><input type="hidden" id="oldMoney" /></li>
	  <li><label class="ilabel" style="display:inline-block" for="oldEndDate">原到期日期：</label><span id="oldEndDateSpan"></span><input type="hidden" id="oldEndDate" /></li>
	  <li><label class="ilabel" style="display:inline-block" for="leveDay">剩余天数：</label><span id="leveDaySpan"></span><input type="hidden" id="leveDay" /></li>
	  <li data-role="list-divider">订购信息</li>
	  <li><label class="ilabel" for="eName">易站通用户名：</label><input type="text" id="eName"></li>
      <li><label for="orderType">订单类型</label>
      	<select id="orderType" onchange="orderTypeChange()">
      		<option title="新单" value="1">新单</option>
      		<option title="续费" value="2">续费</option>
      		<option title="升级" value="3">升级</option>
      	</select>
      </li>
      <li><label for="product">订购产品</label>
		<select id="product" onchange="productChange()" >
		
		</select>
	  </li>
      <li><label for="qty">订购年限</label>
		<select id="qty" onchange="qtyChange()">
         	<option title="3个月" value="3">3个月</option>
			<option title="6个月" value="6">6个月</option>
			<option title="9个月" value="9">9个月</option>
			<option title="1年" value="12">1年</option>
			<option title="1年3个月" value="15">1年3个月</option>
			<option title="1年6个月" value="18">1年6个月</option>
			<option title="1年9个月" value="21">1年9个月</option>
			<option title="2年" value="24">2年</option>
			<option title="3年" value="36">3年</option>
			<option title="4年" value="48">4年</option>
			<option title="5年" value="60">5年</option>
        </select>
	  </li>
	  <li  for="leaveMoney"><label class="ilabel" style="display:inline-block"  for="leaveMoney">当前余额：</label><span id="leaveMoneySpan"></span><input type="hidden" id="leaveMoney" /></li>
	  <li for="zygrade"><label  class="ilabel" style="display:inline-block"    for="zygrade" >代理级别:</label><span id="zygradeSpan"></span><input type="hidden" id="zygrade" /></li>
	  <li for="money"><label class="ilabel" for="money">扣费金额</label><input type="text" id="money"></li>
	  <li for="remoney"><label class="ilabel" for="remoney">退费金额</label><input type="text" id="remoney"></li>
	  <li for="startDate"><label class="ilabel" for="startDate">开通日期</label><input type="date" onchange="startDateChange()" id="startDate"></li>
	  <li for="realStartDate"><label class="ilabel" for="realStartDate">实际开通日期</label><input type="date" onchange="startDateChange()" id="realStartDate"></li>
	  <li for="endDate"><label class="ilabel" for="endDate">结束日期</label><input type="date" id="endDate"></li>
	  <li for="couponMoney"><label class="ilabel" style="display:inline-block"  for="couponMoney">使用优惠券</label>
	  <span id="couponMoneySpan"></span><input type="hidden" id="couponMoney" />
	  <span style="  width: 60px;  height: 30px;  display: inline-block;  float: right;" id="selectcouponBt">
      		<button onclick="selectcoupon()" style="margin:0px"  data-inline="true" data-mini="true" >选择</button>
      	</span></li>
	  <li><label class="ilabel" for="remark">备注</label><textarea id="remark"></textarea></li>
    </ul>
  </div>  
<script type="text/javascript">
//注意本script必须包括在主界面div之内，因为当从列表通过ajax取本文件代码时主界面div之外的代码将会丢弃
//添加单据
function add(){
	keyId='';
	jQuery.mobile.changePage($("#pageUpdate"),{ transition: "flip"} );
	if( !hasEproduct){ //取产品类型，因为这个可以会发生变化，所以动态取，其它枚举可写死
		eProduct =getEnum('eProduct');
		for(i=0;i<eProduct.length;i++){
			$("#product").append('<option value='+eProduct[i].value+'>'+eProduct[i].name+'</option>');
		}
		$("#product").selectmenu('refresh');
		hasEproduct = true;
	}	
	addPrepare('tzESales','','','','',null,detailCalback);//执行添加前准备工作
}

var hasEproduct=false; //记录是否有产品的枚举，某为这个枚举会动态变化，为避免程序修改动态从服务器取
var keyId="";
//修改某条数据
function update(kId){
	keyId = kId;	
	jQuery.mobile.changePage($("#pageUpdate"),{ transition: "flip"} );		
	
	if( !hasEproduct){ //取产品类型，因为这个可以会发生变化，所以动态取，其它枚举可写死
		eProduct =getEnum('eProduct');
		for(i=0;i<eProduct.length;i++){
			$("#product").append('<option value='+eProduct[i].value+'>'+eProduct[i].name+'</option>');
		}
		$("#product").selectmenu('refresh');
		hasEproduct = true;
	}
	
	detail(keyId,'tzESales',detailCalback); //从服务器取数据	
}
//显示或关闭按扭组
function showOperation(){
	//$("#detailBtDiv").toggle() ;	
}
//添加或修改的保存
function saveBill(isCheck){ 
	//$("#detailBtDiv").toggle() ;	
	
	for(i=0;i< curBillObj.fieldList.length;i++){
		item =curBillObj.fieldList[i];
		//元素的隐藏与不隐藏
		if(item.isNull==1 && item.fieldName != 'id'){
			if($("#"+item.fieldName).val()==''){
				malert(item.display+'不能为空');
				returnn;
			}
		}
	}
	
	saveValues = new Object();
	$("input").each(function(){
		saveValues[$(this).attr("id")] = $(this).val();
	});
	$("textarea").each(function(){
		saveValues[$(this).attr("id")] = $(this).val();
	});
	$("select").each(function(){
		saveValues[$(this).attr("id")] = $(this).val();
	});
	var postdata = JSON.stringify(saveValues); //生成所有需提交数据的json字符串
	if(curBillObj.opType=='add'){
		//执行添加
		addBill('tzESales','','',isCheck,postdata,function(){ 
			detail(keyId,'tzESales',detailCalback); //更新数据
		});
	}else{
		//执行修改
		updateBill('tzESales','','',isCheck,postdata,function(){
			detail(keyId,'tzESales',detailCalback); //更新数据
		});
	}
}
function deliverCallback(){//详情转交回调函数
	detail(keyId,'tzESales',detailCalback); //更新数据
}
function cancelCallback(){//撤回回调函数
	detail(keyId,'tzESales',detailCalback); //更新数据
}
function retCheckCallback(){//反审核回调函数
	detail(keyId,'tzESales',detailCalback); //更新数据
}
function deleteCallback(){ //删除单据后的回调函数
	jQuery.mobile.changePage($("#pageOne"),{ transition: "flip"} );	
	query(true);
}


//从服务器取数据后的回调函数
var curBillObj;
function detailCalback(obj){
	//$("#detailBtDiv").hide();	
	$("#showHitBt").hide();
	curBillObj = obj;
	$("#pageUpdate").trigger('create');
	$("#updateOpbt").hide();
	//$("#detailBt").empty(); //清空按扭
	if(curBillObj.opType=='detail'){
		$("#titleh1").text("易站通 详情");
		$("#selectClientBt").hide(); //选择客户按扭
		$("#selectcouponBt").hide(); //选择优惠券按扭
		if(obj.checkRight){
			$("#updateOpbt").text("审核");
			$("#updateOpbt").attr("href",'javascript:checkBillDeliver(\'tzESales\',\''+keyId+'\',deliverCallback)');
			$("#updateOpbt").show();
		}
	}else if(curBillObj.opType=='update'){
		$("#titleh1").text("易站通 修改");
		$("#selectClientBt").show();
		$("#selectcouponBt").show();	
		//加上保存按扭
		$("#updateOpbt").text("保存并审核");
		$("#updateOpbt").attr("href",'javascript:saveBill(true)');
		$("#updateOpbt").show();
	}else if(curBillObj.opType=='add'){
		$("#titleh1").text("易站通 添加");
		$("#selectClientBt").show();
		$("#selectcouponBt").show();	
		//加上保存按扭
		$("#updateOpbt").text("保存并审核");
		$("#updateOpbt").attr("href",'javascript:saveBill(true)');
		$("#updateOpbt").show();
	}
	
	if(obj.retCheckRight){
		$("#updateOpbt").text("反审核");
		$("#updateOpbt").attr("href",'javascript:retCheck(\''+keyId+'\',\'tzESales\',retCheckCallback)');
		$("#updateOpbt").show();
	}
	if(obj.hurryTrans){
		$("#updateOpbt").text("催办");
		$("#updateOpbt").attr("href",'javascript:hurryTrans(\''+keyId+'\',\'tzESales\',\'易站通下单\')');
		$("#updateOpbt").show();
	}
	if(obj.hasCancel){
		$("#updateOpbt").text("撤回");
		$("#updateOpbt").attr("href",'javascript:cancelTo(\''+keyId+'\',\'tzESales\',cancelCallback)');
		$("#updateOpbt").show();
	}	
	
	
	$("span[id$=Span]").text('');
	
	//控制各字段的隐藏，只读，清空字段内容
	for(i=0;i< obj.fieldList.length;i++){
		item =obj.fieldList[i];
		name = item.fieldName.replace(".","_");
		$("#"+name).val(''); //清空，避免先修改后添加时，一些残留数据
		$("#"+name+"Span").text('');//有些字段不需要input而只是为了显示
		if(name=="orderType" || name == "product" || name == "qty" ){
			$("#"+name).selectmenu('refresh');
		}
				
		//元素的隐藏与不隐藏
		if(item.inputType==3){
			$("label[for="+item.fieldName+"]").hide();
			$("li[for="+item.fieldName+"]").hide();
		}else{
			$("label[for="+item.fieldName+"]").show();
			$("li[for="+item.fieldName+"]").show();
		}
		
		if(item.fieldName == "zygrade"){
		}else if (item.inputType == 1 || item.inputTypeOld == 1){ //如果是只读的下拉框，或详情界面的下拉框
			if( obj.opType=='detail' || item.inputType==8){
				$("#"+name).selectmenu("disable");
			}else{
				$("#"+name).selectmenu("enable");
			}		
		}else if(item.inputType==8 || obj.opType=='detail'){ //如果是详情界面，或只读对象，置只读
			$("#"+name).attr("readonly","readonly");
			if(name=="clientId" && obj.opType != 'detail'){ //如果客户只读则选择客户和优惠券按扭只读
				$("#selectClientBt").hide(); //选择客户按扭
				$("#selectcouponBt").hide(); //选择优惠券按扭
				$("#showHitBt").show(); //当客户按扭只读时，且不是详情
			}
		}else{
			$("#"+name).removeAttr("readonly");
		}
	}
	
	//设置界面各字段的值	
	for (item in obj.values){
		name = item.replace(".","_");
		//如果对应字段不存在，则自动添加隐藏字段。
		if($("#"+name).size()==0 && name !='LANGUAGEQUERY'){
			$("#pageUpdate").append("<input type='hidden' id='"+name+"'>");
		}
		$("#"+name).val(obj.values[item]);
		$("#"+name+"Span").text(obj.values[item]);//有些字段不需要input而只是为了显示
		if(name=="orderType" || name == "product" || name == "qty" ){
			$("#"+name).selectmenu('refresh');
		}
	}
	//设置代理级别的显示
	if($("#zygrade").val()=="0"){
		$("#zygradeSpan").text("总代");
	}else if($("#zygrade").val()=="1"){
		$("#zygradeSpan").text("五星");
	}else if($("#zygrade").val()=="2"){
		$("#zygradeSpan").text("四星");
	}else if($("#zygrade").val()=="3"){
		$("#zygradeSpan").text("三星");
	}
	
	showFlowDepict(obj.flowDepict,'pageUpdateContent'); //显示审核流程信息
}

// 客户弹出窗
function selectClient(){	
	createPopupSelect("tzESales","clientId","",5,'选择客户',
	function (showfields,result){
		//查询客户后的回调函数
		//showfields 是指显示字段的名字和中文名称的map
		//result 是指弹出窗的数据列表， 列表中是字段名和值的map
		//回调函数必须在列表对象popupselectListDiv中插入显示数据，且必须有单选或复选对象，对象的值对应行号
		for(i=0;i<result.length;i++){	
			row =result[i]; 
			$("#popupselectListDiv").append( 
				  '<label for="pkeyId_'+i+'">'+	row.tzclient_clientName+      
			      '</label><input type="radio" name="pkeyId" id=pkeyId_'+i+' value="'+i+'">'
			 ); 
		}	
		$("#popupselectListDiv").trigger('create');
	},	
	function doselectClient(result){ //客户弹出窗确定
		for(name in result){
			if($("#"+name).size()==0 ){ //象添加界面可能还没生成一些隐藏字段
				$("#pageUpdate").append("<input type='hidden' id='"+name+"'>");
			}
			$("#"+name).val(result[name]);
			$("#"+name+"Span").text(result[name]);//有些字段不需要input而只是为了显示
			if(name=="orderType" || name == "product" || name == "qty" ){
				$("#"+name).selectmenu('refresh');
			}
		}	  
	});
}
//优惠券弹出窗
function selectcoupon(){
	createPopupSelect("tzESales","couponMoney","",5,'选择优惠券',
	function (showfields,result){
		for(i=0;i<result.length;i++){	
			row =result[i]; 
			$("#popupselectListDiv").append( 
				  '<label for="pkeyId_'+i+'">'+	row.couponMoney+      
			      '</label><input type="radio" name="pkeyId" id=pkeyId_'+i+' value="'+i+'">'
			 ); 
		}	
		$("#popupselectListDiv").trigger('create');
	},	
	function (result){
		for(name in result){
			if($("#"+name).size()==0 ){ //象添加界面可能还没生成一些隐藏字段
				$("#pageUpdate").append("<input type='hidden' id='"+name+"'>");
			}
			$("#"+name).val(result[name]);
			$("#"+name+"Span").text(result[name]);//有些字段不需要input而只是为了显示			
		}	  
	});
}

function startDateChange(){ 
	if($('#product').val()=='eEval3'){ 
		$('#qty').val(''); 
		$('#endDate').val(getDateDiff($('#startDate').val(),'month',3));  
	} else if($('#product').val()=='eEval10'){ 
		$('#qty').val(''); 
		$('#endDate').val(getDateDiff($('#startDate').val(),'day',10));  
	}else{ 
		$('#endDate').val(getDateDiff($('#startDate').val(),'month',$('#qty').val())); 
	}   
}       

function productChange(){ 
	if($('#product').val()=='eEval3'){ 
		$('#qty').val(''); 
		if($('#startDate').val()==''){ 
			$('#startDate').val(getDateStr(0)); 
		} 
		$('#endDate').val(getDateDiff($('#startDate').val(),'month',3));  
	}else  if($('#product').val()=='eEval10'){ 
		$('#qty').val(''); 
		if($('#startDate').val()==''){ 
			$('#startDate').val(getDateStr(0)); 
		}
		$('#endDate').val(getDateDiff($('#startDate').val(),'day',10));  
	}  
} 
function qtyChange(){  
	if($('#product').val()=='eEval3' || $('#product').val()=='eEval10'){ 
		alert('体验产品不可选择订购年限');  
		$('#qty').val('');  
	}else{ 
		if($('#startDate').val()==''){ 
			if($('#oldEndDate').val()==''){ 
				$('#startDate').val(getDateStr(0)); 
			} else { 
				$('#startDate').val(getDateDiff($('#oldEndDate').val(),'day',1));
			} 
		}  
		$('#endDate').val(getDateDiff($('#startDate').val(),'month',$('#qty').val()));
		if($('#orderType').val()=='3'){
			if($('#leveDay').val()!=0){
				$('#endDate').val($('#oldEndDate').val());
			}
		} 
	}  
}  
function orderTypeChange(){ 
	if($('#orderType').val()=='1'){
		$('#startDate').val(getDateStr(0));
		if($('#qty').val()!=''){
			$('#endDate').val(getDateDiff($('#startDate').val(),'month',$('#qty').val()));
		}
	}else{ 
		if($('#orderType').val()=='2'){
			if($('#leveDay').val()!=0){
				$('#startDate').val($('#oldEndDate').val());
			}else{
				$('#startDate').val(getDateStr(0));
			}
			if($('#qty').val()!=''){
				$('#endDate').val(getDateDiff($('#startDate').val(),'month',$('#qty').val()));
			} 
		}else if($('#orderType').val()=='3'){
			if($('#leveDay').val()!=0){
				$('#startDate').val(getDateStr(0));
				$('#endDate').val($('#oldEndDate').val());
			}
		}else if($('#orderType').val()=='4'){
			if($('#leveDay').val()!=0){
				$('#startDate').val($('#oldEndDate').val());
			}else{
				$('#startDate').val(getDateStr(0));
			}
			if($('#qty').val()!=''){
				$('#endDate').val(getDateDiff($('#startDate').val(),'month',$('#qty').val()));
			}
		} 
	}   
}
function getDateStr(AddDayCount) 
{ 
	var dd = new Date(); 
	dd.setDate(dd.getDate()+Number(AddDayCount));//获取AddDayCount天后的日期 
	var y = dd.getFullYear()+"";  
	var m = (dd.getMonth()+1)+"";//获取当前月份的日期 
	var d = dd.getDate()+""; 
	return y+"-"+(m.length==1?"0"+m:m)+"-"+(d.length==1?"0"+d:d); 
} 
function getDateDiff(startDate,type,num){
	var year=startDate.substr(0,4);
    var month=Number(startDate.substr(5,2))-1;
    var date=Number(startDate.substr(8,2));
    if(type=="month"){
    //	month = month+Number(num);
    }else{
    //	date = date +Number(num);
    }
    var d=new Date(year,month,date);
    if(type=="month"){
    	yn = parseInt((month + Number(num))/12);
    	mn = (month + Number(num)) % 12;
    	//找到这个月的最大天数
    	d = new Date(Number(year)+yn,mn+1,'01');
    	d.setDate(d.getDate()-1);
    	maxday = d.getDate();
    	if(date>maxday){
    		date = maxday;
    	}
    	d = new Date(Number(year)+yn,mn,date);
    }else{
    	d.setDate(d.getDate()+num);
    }
    month=d.getMonth()+1;
    if(month<10)month='0'+month;
    if(d.getDate()<10)date='0'+d.getDate();else date=d.getDate();
    var newDate=d.getFullYear()+'-'+month+'-'+date;
	return newDate;
}
function showHit(){
	reportQuery("tzEClientReport",false,{clientName:$("#tzclient_clientNameSpan").text(),pageSize:pageSize,pageNo:pageNo},function(conditions,cols,rows){
		if($("#showHitPanal").size()==0){
			$(document.body).append('<div data-role="dialog" id="showHitPanal"><div data-role="header"><h1>撞单检查</h1></div> \
			<div id="showHitPanalscroller">	 \
					<ul data-role="listview" id="showHitPanalListDiv">  \
					</ul>  \
			</div>  \
		</div>');	
		}
		$("#showHitPanalListDiv").empty();
		for(var k=0;k<rows.length;k++){
			ro = rows[k];
			$("#showHitPanalListDiv").append('<li><span  style="display:block;white-space: normal;border-bottom: 1px dashed #E0E2EA;">全称:'+ro.clientName+'</span>\
			<span style="display:block;white-space: normal;border-bottom: 1px dashed #E0E2EA;">代理:'+ro.agtName+'</span>\
			<span style="display:block;white-space: normal;border-bottom: 1px dashed #E0E2EA;">简称:'+ro.clientSimpleName+'</span>\
			<span style="display:block;white-space: normal;border-bottom: 1px dashed #E0E2EA;">状态:'+ro.clientStatus+'</span>\
			<span style="display:block;white-space: normal;border-bottom: 1px dashed #E0E2EA;">保护结束时间:'+ro.protectEDate+'</span>\
			<span style="display:block;white-space: normal;border-bottom: 1px dashed #E0E2EA;">地区:'+ro.DistrictFullName+'</span>\
			<div style="display:block;white-space: normal;border-bottom: 1px dashed #E0E2EA;">详细地址:'+ro.address+'</div>\
			<span style="display:block;white-space: normal;border-bottom: 1px dashed #E0E2EA;">联系人:'+ro.connect+'</span>\
			<span style="display:block;white-space: normal;border-bottom: 1px dashed #E0E2EA;">手机号码:'+ro.mobile+'</span>\
			<span style="display:block;white-space: normal;border-bottom: 1px dashed #E0E2EA;">所有人:'+ro.EmpName+'</span>\
			</li>');
		}
		jQuery.mobile.changePage($("#showHitPanal"),{ transition: "pop"} );		
		$("#showHitPanalListDiv").listview('refresh');;
		
	});
}
</script>  
  
</div>
</body>
</html>
			