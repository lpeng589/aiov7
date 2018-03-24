<!DOCTYPE html>
<html>
<head>
<title>易站通客户</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="/mobile/css/themes/default/jquery.mobile-1.4.5.min.css">
<link rel="stylesheet" type="text/css" href="/mobile/css/scrollbar.css">
<link rel="stylesheet" type="text/css" href="/mobile/css/mobile.css">

<link rel="shortcut icon" href="/favicon.ico">
<script src="/mobile/js/jquery.min.js"></script>
<script src="/mobile/js/jquery.mobile-1.4.5.min.js"></script>
<script src="/mobile/js/mobileapi.min.js?111=1"></script>
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
    <h1 id="titleh1">易站通客户 </h1>
    <a href="javascript:showOperation()" data-role="button" id="updateOpbt"  class="ui-btn-right" data-icon="check">操作</a>
    <span id="detailBtDiv" class="dropOperations" style="display:none; position: absolute;  right: 5px;  top: 40px;  background-color: #fff;  padding: 5px;  border: 1px solid #C9CBC5;  border-radius: 0 0 8px 8px;"   >
    	<ul id="detailBt" >
    	</ul>
    </span>
  </div>

  <div data-iscroll="" data-role="content" id="pageUpdateContent"><!-- 修改的界面显示 -->
	<ul data-role="listview">
      <li><label class="ilabel" for="eName">易站通用户名：</label><input type="text" id="eName" /></li>
      <li><label class="ilabel" style="display:inline-block;height: 30px;"  for="clientSimpleName">简称</label>
	  <span style="  width: 80px;  height: 30px;  display: inline-block;  float: right;" id="showHitBt">
      		<button onclick="showHit()" style="margin:0px"  data-inline="true" data-mini="true" >撞单检查</button>
      	</span><input type="text" id="clientSimpleName" /></li>
      <li><label class="ilabel" for="clientName">全称：</label><input type="text" id="clientName" /></li>
      <li><label class="ilabel" style="display:inline-block;height: 30px;"  for="tblDistrict_DistrictFullName">所在城市</label>
	  <input type="hidden" id="district" />
	  <span style="  width: 60px;  height: 30px;  display: inline-block;  float: right;" id="selectDistrictBt">
      		<button onclick="selectDistrict()" style="margin:0px"  data-inline="true" data-mini="true" >选择</button>
      	</span><input type="text" readonly id="tblDistrict_DistrictFullName" /></li>
      <li><label class="ilabel" for="url">公司官网：</label><input type="text" id="url" /></li>
      <li><label class="ilabel" for="address">详细地址：</label><input type="text" id="address" /></li>
      <li><label class="ilabel" for="connect">联系人：</label><input type="text" id="connect" /></li>
      <li><label class="ilabel" for="mobile">手机号码：</label><input type="text" id="mobile" /></li>
      <li><label class="ilabel" for="tel">固定电话：</label><input type="text" id="tel" /></li>
      <li><label class="ilabel" for="clientStatus">客户状态：</label>
      <select id="clientStatus" >
     		<option title="" value=""></option>
     		<option title="正在培养" value="0">正在培养</option>
     		<option title="放弃客户" value="1">放弃客户</option>
     		<option title="不存在客户" value="2">不存在客户</option>
     		<option title="已成交客户" value="100">已成交客户</option>
      </select></li>
      <li><label class="ilabel" style="display:inline-block;height: 30px;"  for="tztrade_tradeName">行业</label>
	  <input type="hidden" id="trade" />
	  <span style="  width: 60px;  height: 30px;  display: inline-block;  float: right;" id="selectTradeBt">
      		<button onclick="selectTrade()" style="margin:0px"  data-inline="true" data-mini="true" >选择</button>
      	</span><input type="text" readonly id="tztrade_tradeName" /></li>
      <li><label class="ilabel" for="mainProduct">主营产品：</label><input type="text" id="mainProduct" /></li>
      <li>
		<fieldset data-role="controlgroup">
		<legend>购买意向</legend>
		<label for="objProd_1">标准版</label>
		<input type="checkbox" id="objProd_1" name="objProd" value="1" >
		<label for="objProd_2" >至尊版</label>
		<input type="checkbox" id="objProd_2" name="objProd" value="2">
		<label for="objProd_3" >金牌商盟</label>
		<input type="checkbox" id="objProd_3" name="objProd" value="3">
		<label for="objProd_4">卓越版</label>
		<input type="checkbox" id="objProd_4" name="objProd" value="4">
		<label for="objProd_5">钻石版</label>
		<input type="checkbox" id="objProd_5" name="objProd" value="5" >
		</fieldset>
		</li>
	  <li>
		<fieldset data-role="controlgroup">
		<legend>是否有推广</legend>
		<label for="ifAdv_Yes">是</label>
		<input type="checkbox" id="ifAdv_Yes" name="ifAdv" value="Yes" >
		</fieldset>
		</li>	
	  <li><label class="ilabel" for="advEval">推广评估</label><textarea id="advEval"></textarea></li>
	  <li><label class="ilabel" for="advEval">官网评估</label><textarea id="urlEval"></textarea></li>
	  <li><label class="ilabel" for="advEval">备注</label><textarea id="remark"></textarea></li>
    </ul>
  </div>  
<script type="text/javascript">
//注意本script必须包括在主界面div之内，因为当从列表通过ajax取本文件代码时主界面div之外的代码将会丢弃
//添加单据
function add(){
	keyId='';
	jQuery.mobile.changePage($("#pageUpdate"),{ transition: "flip"} );
	addPrepare('tzclient','','','','',null,detailCalback);//执行添加前准备工作
}

var keyId="";
//修改某条数据
function update(kId){
	keyId = kId;	
	jQuery.mobile.changePage($("#pageUpdate"),{ transition: "flip"} );		
	detail(keyId,'tzclient',detailCalback); //从服务器取数据	
}
//显示或关闭按扭组
function showOperation(){
	//$("#detailBtDiv").toggle() ;	
}
//添加或修改的保存
function saveBill(isCheck){ 
	
	for(i=0;i< curBillObj.fieldList.length;i++){
		item =curBillObj.fieldList[i];
		//元素的隐藏与不隐藏
		if(item.isNull==1 && item.fieldName != 'id' && item.inputType!=3 && item.inputType!=100){
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
	objProds="";
	$("input[type=checkbox][name=objProd]:checked").each(function(){
		objProds +=$(this).val() + ",";
	});
	saveValues['objProd'] = objProds;
	
	if($("#ifAdv_Yes:checked").size() > 0 ){
		saveValues['ifAdv'] = "Yes,";
	}
	
	var postdata = JSON.stringify(saveValues); //生成所有需提交数据的json字符串
	if(curBillObj.opType=='add'){
		//执行添加
		addBill('tzclient','','',isCheck,postdata,function(){ 
			detail(keyId,'tzclient',detailCalback); //更新数据
		});
	}else{
		//执行修改
		updateBill('tzclient','','',isCheck,postdata,function(){
			detail(keyId,'tzclient',detailCalback); //更新数据
		});
	}
	
}

function deleteCallback(){ //删除单据后的回调函数
	jQuery.mobile.changePage($("#pageOne"),{ transition: "flip"} );	
	query(true);
}


//从服务器取数据后的回调函数
var curBillObj;
function detailCalback(obj){
	//$("#detailBtDiv").hide();	
	$("#pageUpdate").trigger('create');
	curBillObj = obj;	
	$("#updateOpbt").hide();
	if(curBillObj.opType=='detail'){
		$("#titleh1").text("易站通客户 详情");
		$("#selectDistrictBt").hide(); //选择客户按扭
		$("#selectTradeBt").hide(); //选择优惠券按扭
	}else if(curBillObj.opType=='update'){
		$("#titleh1").text("易站通客户 修改");
		$("#selectDistrictBt").show();
		$("#selectTradeBt").show();	
		//加上保存按扭
		$("#updateOpbt").text("保存");
		$("#updateOpbt").attr("href",'javascript:saveBill(false)');
		$("#updateOpbt").show();
	}else if(curBillObj.opType=='add'){
		$("#titleh1").text("易站通客户 添加");
		$("#selectDistrictBt").show();
		$("#selectTradeBt").show();	
		//加上保存按扭
		$("#updateOpbt").text("保存");
		$("#updateOpbt").attr("href",'javascript:saveBill(false)');
		$("#updateOpbt").show();
	}
			
	$("span[id$=Span]").text('');
	$("#tblDistrict_DistrictFullName").val("");
	$("#tztrade_tradeName").val("");
	
	//控制各字段的隐藏，只读，清空字段内容
	for(i=0;i< obj.fieldList.length;i++){
		item =obj.fieldList[i];
		name = item.fieldName.replace(".","_");
		$("#"+name).val(''); //清空，避免先修改后添加时，一些残留数据
		$("#"+name+"Span").text('');//有些字段不需要input而只是为了显示
		if(name=="clientStatus"){
			$("#"+name).selectmenu('refresh');
		}
		$('input[type="checkbox"][name="'+name+'"]').attr('checked',false);
		
		//元素的隐藏与不隐藏
		if(item.inputType==3){
			$("label[for="+item.fieldName+"]").hide();
			$("li[for="+item.fieldName+"]").hide();
		}else{
			$("label[for="+item.fieldName+"]").show();
			$("li[for="+item.fieldName+"]").show();
		}
		
		if (item.inputType == 1 || item.inputTypeOld == 1){ //如果是只读的下拉框，或详情界面的下拉框
			if( obj.opType=='detail' || item.inputType==8){
				$("#"+name).selectmenu("disable");
			}else{
				$("#"+name).selectmenu("enable");
			}		
		}else if(item.inputType==8 || obj.opType=='detail'){ //如果是详情界面，或只读对象，置只读
			$("#"+name).attr("readonly","readonly");
		}else  if(item.fieldName!='tblDistrict_DistrictFullName'){
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
		if(name=="clientStatus"){
			$("#"+name).selectmenu('refresh');
		}else if(name == "objProd"){
			vs = obj.values[item].split(",");
			for(var i =0;i<vs.length;i++){
				if(vs[i] != ""){
					$("#objProd_"+vs[i]).attr('checked',true); 
				}
			}
		}else if(name == "ifAdv"){
			vs = obj.values[item].split(",");
			for(var i =0;i<vs.length;i++){
				if(vs[i] != ""){
					$("#ifAdv_"+vs[i]).attr('checked',true); 
				}
			}
		}
	}
	$('input[type="checkbox"]').checkboxradio("refresh"); 
	console.log($('input[type="checkbox"]')[0]);
	$('input[type="checkbox"]:checked').each(function(i){ 
		$('label[for='+$(this).attr('id')+']').removeClass("ui-checkbox-off").addClass("ui-checkbox-on");
	}); 
	//$("#pageUpdate").trigger('create');
}

// 地区弹出窗
function selectDistrict(){	
	createPopupSelect("tzclient","district","",5,'选择地区',
	function (showfields,result){
		for(var i=0;i<result.length;i++){	
			row =result[i]; 
			$("#popupselectListDiv").append( 
				  '<label for="pkeyId_'+i+'">'+	row.tblDistrict_DistrictFullName+      
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
		}	  
	});
}
//行业弹出窗
function selectTrade(){
	createPopupSelect("tzclient","trade","",5,'选择行业',
	function (showfields,result){
		for(i=0;i<result.length;i++){	
			row =result[i]; 
			$("#popupselectListDiv").append( 
				  '<label for="pkeyId_'+i+'">'+	row.tztrade_tradeName+      
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

function showHit(){
	reportQuery("tzclientHitReport",false,{keyword:$("#clientSimpleName").val(),pageSize:pageSize,pageNo:pageNo},function(conditions,cols,rows){
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
			var ttt = '<li><span  style="display:block;white-space: normal;border-bottom: 1px dashed #E0E2EA;">全称:'+ro.clientName+'</span>\
			<span style="display:block;white-space: normal;border-bottom: 1px dashed #E0E2EA;">简称:'+ro.clientSimpleName+'</span>\
			<span style="display:block;white-space: normal;border-bottom: 1px dashed #E0E2EA;">业务员:'+ro.name+'</span>';
			
			if(ro.back != ''){
				back = ro.back.split('#;#')[1].trim();
				back = back.substring(0,back.length-1)+',backcallback)';
				backtd = ro.backtd.split('#;#')[1].trim();
				backtd = backtd.substring(0,backtd.length-1)+',backcallback)';
				backstr = '<a href='+back+'  id="backbt" data-inline="true">回收</a>';
				backstr +='<a style="margin-left: 30px;" href='+backtd+'  id="backtdbt"  data-inline="true">提单回收</a>';
				ttt += '<span style="display:block;white-space: normal;border-bottom: 1px dashed #E0E2EA;">'+backstr+'</span>';
					
			}
			ttt += '</li>';
			$("#showHitPanalListDiv").append(ttt);
		}
		if(rows.length==0){
			$("#showHitPanalListDiv").append('<li><span  style="display:block;white-space: normal;border-bottom: 1px dashed #E0E2EA;">恭喜你，没有撞单的</span></li>');
		}
		jQuery.mobile.changePage($("#showHitPanal"),{ transition: "pop"} );		
		
		$("#showHitPanalListDiv").listview('refresh');
		
		
	});
}
function backcallback(){
	malert('执行成功');
	$('.ui-dialog').dialog('close'); //关闭弹出窗	
}
</script>  
  
</div>
</body>
</html>
			