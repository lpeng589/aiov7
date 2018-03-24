<html> 
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>扫描入库</title>
<link type="text/css" rel="stylesheet" href="/style/css/about.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
$(function(){
	$("#scroll-wrap").height(document.body.clientHeight-18);
	$(document.body).click(function(){
		$('#scanId').focus();
	});
	$('#scanId').blur( function() {$('#scanId',document).focus();} ); 

});

//会话失效后重新登陆的回调函数
function reloginToUrl(){
	
}
var workOrderList;

function changeWO(){
	var ind = $("#WorkOrder").prop('selectedIndex');
	$("#NoInNumSpan").text(workOrderList[ind].NoInNum);
}

function scanChange(){ 
	var val = $("#scanId").val();
	if(val.length==0){
		return;
	}
	if(val.indexOf("`") > 0){
		val = val.substring(val.indexOf("`"));
	}
	if(val.indexOf("6927")==0){//以69279378开头说明这是扫描的商品，查询商品信息并显示
		
		jQuery.post("/hf/HFApi.jsp",{op:'GetGoods',barCode:val},function(json){ 
			if(json.code == "ERROR"){
				alert(json.msg);
			}else if(json.code == "NOLOGIN"){
				top.reLogin(window);
			}else{
				$("#GoodsDiv").empty();
				$("#GoodsDiv").append("\
				<div>编号："+json.GoodsNumber+"</div>\
				<div>名称："+json.GoodsFullName+"</div>\
				<div>规格："+json.GoodsSpec+"</div>\
				<div>条码："+json.barCode+"</div>");
				$(".curObj").removeClass("curObj");
				$("#GoodsDiv").addClass("curObj");
				$("#GoodsCode").val(json.GoodsCode);
				$("#WorkOrder").empty();
				$("#outPackageNum").val(json.outPackageNum);
				if(json.outPackageNum<=1){
					$("#yearNODiv").empty();
					$("#yearNODiv").append("本类型油不需扫描箱号");
				}
				$("#totalYearNo").text("0");
				$("#totalNum").text("0");
				
				workOrderList = json.WorkOrder;
				$("#NoInNumSpan").text(workOrderList[0].NoInNum);
				for(var i =0;i<json.WorkOrder.length;i++){
					var work = json.WorkOrder[i];
					$("#WorkOrder").append("<option value='"+work.id+"'>"+work.BillNo+"  "+work.BillDate+"  "+
							work.WorkShop+"</option>"); 
				}
			}
		},"json" ); 
	}else if(val.indexOf("999")==0){
		if(val.length !=7){
			alert("批号必须是999开头的7位数，请更正");
			return;
		}
		val = val.toUpperCase();
		$("#BatchDiv").empty();
		$("#BatchDiv").append(val);
		$("#BatchNo").val(val);
		$(".curObj").removeClass("curObj");
		$("#BatchDiv").addClass("curObj");
	}else if(val.indexOf("888")==0||val.indexOf("777")==0){
		if($("#outPackageNum").val()<=1){
			alert("本类型油不需扫描箱号");
			$("#scanId").val("");
			return;
		}
		if($("#GoodsCode").val()==""){
			alert("请先扫描商品条码");
		}else if($("#BatchNo").val()==""){
			alert("请输入批号");
		}else{
			if($("#yearNoNum").size() > 0 && Number($("#yearNoNum").text()) < Number($("#outPackageNum").val()) && val.indexOf("888")==0){
				alert("本箱未满不可以换箱号");
			}else{
				$("#yearNODiv").empty();
				$("#yearNODiv").append(val+"(<span id='yearNoNum'>0</span>/"+$("#outPackageNum").val()+")");
				$("#yearNO").val(val);
				$(".curObj").removeClass("curObj");
				$("#yearNODiv").addClass("curObj");
				$("#totalYearNo").text(Number($("#totalYearNo").text())+1);
				
			}
		}
	}else if(val.indexOf("695590")==0){
		if($("#GoodsCode").val()==""){
			alert("请先扫描商品条码");
		}else if($("#BatchNo").val()==""){
			alert("请输入批号");
		}else if(Number($("#outPackageNum").val()) > 1 && $("#yearNO").val()==""){
			alert("请先扫描箱号");
		}else{
			if(Number($("#yearNoNum").text()) >= Number($("#outPackageNum").val()) ){
				alert("本箱已满请换箱号");
			}else{
				if(typeof(top.jblockUI)!="undefined" ){
					top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
				}
				jQuery.post("/hf/HFApi.jsp",{op:"PutGoods",GoodsCode:$("#GoodsCode").val(),
					BatchNo:$("#BatchNo").val(),yearNO:$("#yearNO").val(),WorkOrder:$("#WorkOrder").val(),
					Seq:val},function(json){ 
				
					if(typeof(top.jblockUI)!="undefined" ){
							top.junblockUI()
					}
					if(json.code == "ERROR"){
						alert(json.msg);
					}else if(json.code == "NOLOGIN"){
						top.reLogin(window);
					}else{
						$("#yearNoNum").text(json.yearNoNum);
						$("#totalNum").text(Number($("#totalNum").text())+1);
						$("#NoInNumSpan").text(Number($("#NoInNumSpan").text())-1);
						
						$("#Goodslist").prepend('<div>箱号（'+$("#yearNO").val()+'）；物流码（'+val+'）</div>');
						$(".curObj").removeClass("curObj");
						$("#Goodslist div:eq(0)").addClass("curObj");
					}
					
				},"json" ); 
			}
		}
	}else if(val.indexOf("-695590")==0){
		if(typeof(top.jblockUI)!="undefined" ){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
		val = val.substring(1);
		jQuery.post("/hf/HFApi.jsp",{op:"DelGoods", Seq:val},function(json){ 
		
			if(typeof(top.jblockUI)!="undefined" ){
					top.junblockUI()
			}
			if(json.code == "ERROR"){
				alert(json.msg);
			}else if(json.code == "NOLOGIN"){
				top.reLogin(window);
			}else{
				$("#yearNoNum").text(json.yearNoNum);
				$("#totalNum").text(Number($("#totalNum").text())-1);
				$("#NoInNumSpan").text(Number($("#NoInNumSpan").text())+1);
				$("#Goodslist").prepend('<div>箱号（'+$("#yearNO").val()+'）；物流码（-'+val+'）</div>');
				$(".curObj").removeClass("curObj");
				$("#Goodslist div:eq(0)").addClass("curObj");
			}
			
		},"json" ); 
	}else{
		alert("扫描内容未能识别，请扫描或输入正确的信息");
	}
	$("#scanId").val("");
}


</script>
<style>
#GoodsDiv div{float:left;margin-right:20px}
.curObj{color:red;}
.title{
	font-weight: bold;
    font-size: 22px;
    color: #428bca;
    text-align: center;
    padding: 5px 0 5px 0;
}

.d-left {
    float: left;
    max-width: 1100px;
    display: inline-block;
    overflow: hidden;
}

.itemname {
    font-size: 18px;
    color: #166492;
    text-align: right;
    float: left;
    width: 170px;
    line-height: 26px;
}

#bodyUL{
	font-size: 18px;
    line-height: 25px;
    color: #666;
    padding: 10px 0;
    text-align: left;
}
</style>
</head>
<body class="html" onload="$('#scanId').focus();">
<input id='GoodsCode' type=hidden value="" />
<input id='BatchNo' type=hidden value="" />
<input id='yearNO' type=hidden value="" />
<input id='outPackageNum' type=hidden value="" />

<div id="scroll-wrap">
	<div class="wrap">
    	<p class="title">入库扫描</p>
    	<p class="title"><input type="text" style="width:200px;height:30px;font-size:18px" onkeydown="if(event.keyCode==13) {scanChange();}" id="scanId"/></p>
		<div id="bodyUl" style="padding-top:10px">
			<ul>
				
				<li>
					<div class="itemName">商品：</div>
					<div class="d-left" id="GoodsDiv">
						请扫描商品条码
					</div>
				</li>	
				<li>
					<div class="itemName">工令单：</div>
					<div class="d-left">
						<select id="WorkOrder" style="min-width: 600px;" onChange="changeWO()"><option>请先扫描商品条码，再选择工令单</option></select> 
						未入库数(<span id="NoInNumSpan">0</span>)
					</div>
				</li>
				<li>
					<div class="itemName">批号：</div>
					<div class="d-left" id="BatchDiv">
						请录入批号
					</div>
				</li>
				<li>
					<div class="itemName">当前箱号：</div>
					<div class="d-left" id="yearNODiv">
						请扫描箱号
					</div>
				</li>
				<li>
					<div class="itemName">已扫描：</div>
					<div class="d-left">
						<div>总数量(<span id="totalNum">0</span>)&nbsp;&nbsp;&nbsp; 总箱数(<span id="totalYearNo">0</span>)</div>
						<div id="Goodslist" style=" height: 200px;overflow-y: auto; width: 500px;">
						</div>
					</div>
				</li>
	      
	      </ul>
		</div>
	</div>
</div>
</body>
</html>