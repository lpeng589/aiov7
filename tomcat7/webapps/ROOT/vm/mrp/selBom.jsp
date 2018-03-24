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
<link type="text/css" rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" /> 
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
	trobj = $(obj,document);
	keyi = trobj.find("input[name=GoodsCode]");
	if(keyi.size() > 0){
		if(keyi.attr("type")=="radio"){
			keyi[0].checked = true;
		}else{		
			keyi[0].checked = !keyi[0].checked;
		}
	}
}
function detail(tableName,keyId){
	turl="/UserFunctionAction.do?tableName="+tableName+"&keyId="+keyId+"&operation=$globals.getOP("OP_DETAIL")";
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	openPop('PopMainOpdiv','',turl,width,height,false,height==document.documentElement.clientHeight);  
	
}
function changeTags(obj){
	$(obj).addClass("tagSel").siblings("span").removeClass("tagSel");
	$("#"+$(obj).attr("show")).show().siblings("div").hide();
	$("#"+$(obj).attr("show")).find(":text:eq(0)").focus();
}
function recGetBomItem(bomList,liObj){
	liObj.find(">ul >li").each(function(){
		var so = new Object();
		so['bomId']=$(this).attr("bomId");
		so['bomDetId']=$(this).attr("bomDetId");
		so['childBomId']=$(this).attr("childBomId");
		so['detCode']=$(this).attr("detCode");
		so['qty']=$(this).attr("qty");
		so['lossRate']=$(this).attr("lossRate");
		so['totalQty']=$(this).attr("totalQty");
		so['totalLoss']=$(this).attr("totalLoss");
		so['ProduceQty']=$(this).attr("ProduceQty");
		so['attrType']=$(this).attr("attrType");
		so['alone']=$(this).attr("alone");
		so['clientSupper']=$(this).attr("clientSupper");
		so['GoodsCode']=$(this).attr("GoodsCode");
		so['baseLossRate']=$(this).attr("baseLossRate");
		so['baseQty']=$(this).attr("baseQty");
		
		
		var isOpen="0";
		if(so['childBomId'] != "" && $(this).find(">ul").size() >0 && $(this).find(">ul").is(':visible')){
			isOpen = "1";
		}
		so['isOpen']=isOpen;
		bomList[bomList.length] = so;
		if(isOpen=="1"){
			recGetBomItem(bomList,$(this));
		}
		
	});
}
function toSave(){
	if(!confirm("确定BOM树的展开形式，并保存吗？")){
		return;
	}
	changeTags($("#openBomDIVTitle")[0]); 
	
	var saveValues=new Object();
	saveValues.GoodsCode=$("#GoodsCode").val();
	saveValues.needQty=$("#needQty").val();
	saveValues.StockQty=$("#StockQty").val();
	saveValues.OrderQty=$("#OrderQty").val();
	saveValues.ProduceQty=$("#ProduceQty").val();
	saveValues.PossesQty=$("#PossesQty").val();
	saveValues.bomId=$("#bomId").val();
	saveValues.id=$("#mrpId").val();
	var salesList = new Array();
	$("tr[name=salesList]").each(function(){
		var so = new Object();
		so['billId']=$(this).attr("billId");
		so['BillType']=$(this).attr("BillType");
		so['BillNo']=$(this).attr("BillNo");
		so['BillDate']=$(this).attr("BillDate");
		so['DepartmentCode']=$(this).attr("DepartmentCode");
		so['EmployeeId']=$(this).attr("EmployeeId");
		so['CompanyCode']=$(this).attr("CompanyCode");
		so['createBy']=$(this).attr("createBy");
		so['TotalQty']=$(this).attr("TotalQty");
		so['MRPQty']=$(this).attr("MRPQty");
		so['qty']=$(this).find("input[name=sQty]").val();
		
		salesList[salesList.length] = so;
	});
	saveValues.salesList = salesList;
	//上传bom
	var bomList = new Array();
	recGetBomItem(bomList,$("#treeDemo_1"));
	saveValues.bomList = bomList;
	
	var postdata = JSON.stringify(saveValues); //生成所有需提交数据的json字符串
	
	if(typeof(top.jblockUI)!="undefined" ){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	jQuery.post("/Mrp.do?type=saveBom",{data:postdata},function(json){ 
		if(json.code == "ERROR"){
			alert(json.msg);
		}else{
			alert("保存成功");
			changeConfirm();
			window.location.href="/Mrp.do?type=showBom&keyId="+json.keyId;
		}
		
		if(typeof(top.jblockUI)!="undefined" ){
			top.junblockUI()
		}
	},"json" ); 
}
function toGoods(){
	document.form.action="/Mrp.do?type=selGoods";
	document.form.submit();
}
//----------BOM树---------
function setIcon(obj){
	if(obj.attr("childBomId") != ""){
		//有下级
		if(obj.find(">ul").is(":visible")){
			obj.find(">button").removeClass();
			obj.find(">button").addClass("switch_bottom_open");
		}else{
			obj.find(">button").removeClass();
			obj.find(">button").addClass("switch_bottom_close");
		}
	}else if(obj.next().size()>0){//有同级元素
		obj.find(">button").removeClass();
		obj.find(">button").addClass("switch_center_docu");
	}else {//最后一个元素
		obj.find(">button").removeClass();
		obj.find(">button").addClass("switch_bottom_docu");
	}
	if(obj.parent("ul").parent("li").next().size()>0){
		//父级有同级元素
		if(obj.attr("id")!= "treeDemo" && obj.parent("ul").attr("id")!= "treeDemo"){
			obj.parent("ul").removeClass();
			obj.parent("ul").addClass("line");
		}
	}else{
		//父级有同级元素
		if(obj.attr("id")!= "treeDemo" && obj.parent("ul").attr("id")!= "treeDemo"){
			obj.parent("ul").removeClass();
		}
	}
}

function setNo(ulo){ 
	var no=1;
	var ptxt = ulo.parent().find(">a").find("span[name=tno]").text();
	if(ptxt!=""){
		ptxt=ptxt+".";
	}
	ulo.find(">li").each(function(){
		$(this).find("span[name=tno]").text(ptxt+no);
		no++;
		if($(this).find(">ul li").size()>0){
			setNo($(this).find(">ul"));
		}
	});
}
function selectItem(obj){
	$(".selectdBomDet").removeClass("selectdBomDet");
	$(obj).find("div").addClass("selectdBomDet");
}
function clicksf(obj){
	var liobj = $(obj).parent();
	if(liobj.attr("attrType")==3){
		alert("虚拟件物料必须展开");
		return;
	}
	if(liobj.attr("attrType")==1){
		alert("采购件物料不可以展开");
		return;
	}
	if(liobj.attr("attrType")==4){
		alert("杂项物料不可以展开");
		return;
	}
	id = $(obj).attr("id");
	id = id.substring(0,id.length - 7 );
	$("#"+id+"_ul").toggle();
	if($(obj).attr("class").indexOf("switch_bottom_close") > -1){
		
		
		$(obj).removeClass("switch_bottom_close");
		$(obj).addClass("switch_bottom_open");
		//如果存在下级Bomw但不存在ul项目，则ajax取数据
		if(liobj.attr("childBomId") != "" && liobj.find(">ul[id*=_ul] li").size()==0){
			if(typeof(top.jblockUI)!="undefined" ){
				top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
			}
			jQuery.ajax({url:"/Mrp.do?type=openBom",data:"bomId="+liobj.attr("childBomId")+"&mrpId="+$("#mrpId").val()+"&parentCode="+liobj.attr("detCode"),success:function(json){ 
				if(json.code == "ERROR"){
					if(typeof(top.jblockUI)!="undefined" ){
						top.junblockUI()
					}
					alert("执行错误");
					return;
				}
				recShowChild(liobj,json.bomId,liobj.attr("detCode"),json.list);
				liobj.find(">ul").show();
				
				$('li[treenode=""]').each(function(){
					setIcon($(this));
				});
				setNo($("#treeDemo_1_ul"));
				
				
			},dataType:"json",async:false }); 
			
			if(typeof(top.jblockUI)!="undefined" ){
				top.junblockUI()
			}
		}	
		//屏蔽半成品的用量信息
		//liobj.find(">a span[name=totalQty]").text("");
		//liobj.find(">a span[name=totalLoss]").text("");
		//liobj.find(">a span[name=ProduceQty]").text("");
		
		//设置是否独立工令
		liobj.find(">a span[name=alone]").append("<select name='alone' onclick='changeAlone(this)'><option value='no'>合并工令</option><option value='yes'>独立工令</option></select>");
		liobj.find(">a select[name=alone]").val(liobj.attr("alone"));
		if(liobj.attr("alone")=='yes'){
			liobj.find(">a span[name=Produce]").append("<input style='border: 1px; border-style: inset;' onchange='changeProduceQty(this)' name='ProduceQty'/> ");
			liobj.find(">a input[name=ProduceQty]").val(liobj.attr("ProduceQty"));
		}
		//设置是否客供
		liobj.find(">a span[name=clientSupper]").empty();
	}else{
		$(obj).removeClass("switch_bottom_open");
		$(obj).addClass("switch_bottom_close");
		
		//liobj.find(">a span[name=totalQty]").text(liobj.attr("totalQty"));
		//liobj.find(">a span[name=totalLoss]").text(liobj.attr("totalLoss"));
		//liobj.find(">a span[name=ProduceQty]").text(liobj.attr("ProduceQty"));
		liobj.find(">a span[name=alone]").empty();
		liobj.find(">a span[name=Produce]").empty();
		//设置是否客供
		liobj.find(">a span[name=clientSupper]").append('<select name="clientSupper" onclick="changeClientSupper(this)"><option value="no"></option><option value="isClient">客供</option>'+(liobj.attr("canSuper")=='1'?'<option value="isSupper">包料</option>':'')+'</select>');
		liobj.find(">a select[name=clientSupper]").val(liobj.attr("clientSupper"));
	}
}
function changeAlone(obj){
	var liobj = $(obj).parent().parent().parent().parent().parent().parent().parent();
	liobj.attr("alone",$(obj).val());
	if($(obj).val()=='yes'){
		liobj.find(">a span[name=Produce]").
		append("<input style='border: 1px; border-style: inset;' onchange='changeProduceQty(this)' name='ProduceQty'/> ");
		liobj.find(">a input[name=ProduceQty]").val(liobj.attr("ProduceQty"));
	}else{
		liobj.find(">a span[name=Produce]").empty();
	}
}
function changeClientSupper(obj){
	$(obj).parent().parent().parent().parent().parent().parent().parent().attr("clientSupper",$(obj).val());
}
function changeProduceQty(obj){
	$(obj).parent().parent().parent().parent().parent().parent().parent().attr("ProduceQty",$(obj).val());
}
function getctreeli(data,bomId,detCode,pqty,plossRate,width){
	//找出最大的li编号
	var maxli=1;
	$("li[id*=treeDemo]").each(function(){
		lid = Number($(this).attr("id").substring(9));
		if(lid>maxli){maxli=lid;}
	});
	maxli++;
	var totalQty = pqty*data.qty*$('#needQty').val();
	var totalLoss = (plossRate+data.lossRate)*totalQty/100;
	var attrTypeName = "";
	if(data.attrType==0){  
		attrTypeName = "M自制";
	}else if(data.attrType==1){
		attrTypeName = "P采购";
	}else if(data.attrType==2){
		attrTypeName = "S委外";
	}else if(data.attrType==3){
		attrTypeName = "X虚拟";
	}else if(data.attrType==4){
		attrTypeName = "Z杂项";
	}
	
	return ' \
	<li id="treeDemo_'+maxli+'" treenode="" GoodsCode="'+data.classCode+'"  detCode="'+detCode+'"   bomId="'+bomId+'" bomDetId="'+data.bomDetId+'"  childBomId="'+data.childBomId+'" qty="'+pqty*data.qty+'" \
		lossRate="'+(plossRate+data.lossRate)+'"  baseQty="'+data.qty+'"  baseLossRate="'+(data.lossRate)+'" totalQty="'+(totalQty)+'" totalLoss="'+(totalLoss)+'" \
		attrType="'+data.attrType+'"  ProduceQty="'+totalQty+'"  canSuper="'+data.canSuper+'" clientSupper="no" alone="no">\
		<button type="button"  style="float:left" id="treeDemo_'+maxli+'_switch" title="" onclick="clicksf(this)"  class="switch_bottom_docu" treenode_switch="" onfocus="this.blur();"></button>\
		<a id="treeDemo_'+maxli+'_a" treenode_a="" onclick="selectItem(this)"  target="_blank" style="float:left;width:'+width+'px;">\
			<button type="button"  style="float:left" id="treeDemo_'+maxli+'_ico" title="" treenode_ico="" onfocus="this.blur();" class=" ico_open" style=""></button>\
			<span id="treeDemo_'+maxli+'_span" style="width:'+(width-19)+'px;"  class="treeDemoSpan">\
				<div class="bomDet">\
					<span name="tno"></span>\
					<ul>	\
						<li><span style="width:150px;text-align:left;color:#000">'+data.GoodsNumber+'</span></li>\
						<li><span style="width:160px;text-align:left;color:#000">'+data.GoodsFullName+'</span></li>	\
						<li><span style="width:200px;text-align:left;color:#000">'+data.GoodsSpec+'</span></li>\
						<li><span style="width:40px;text-align:left;color:#000">'+data.BaseUnit+'</span></li>\
						<li><span style="width:40px;text-align:left;color:#000">'+attrTypeName+'</span></li>\
						<li><span style="width:60px;text-align:left;color:#000">'+pqty*data.qty+'</span></li>	\
						<li><span style="width:50px;text-align:left;color:#000">'+(plossRate+data.lossRate)+'</span></li>\
						<li><span style="width:70px;text-align:left;color:#000" name="totalQty">'+totalQty+'</span></li>\
						<li><span style="width:70px;text-align:left;color:#000" name="totalLoss">'+(totalLoss)+'</span></li>	\
						<li><span style="width:50px;text-align:left;color:#000" name="clientSupper">\
						<select name="clientSupper" onclick="changeClientSupper(this)"><option value="no"></option><option value="isClient">客供</option>'+(data.canSuper=='1'?'<option value="isSupper">包料</option>':'')+'</select>\
						</span></li>\
						<li><span style="width:60px;text-align:left;color:#000" name="alone"></span></li>\
						<li><span style="width:70px;text-align:left;color:#000" name="Produce"></span></li>\
					</ul>\
				</div>\
			</span>\
		</a>\
		<ul id="treeDemo_'+maxli+'_ul" class="line" style="display:none"></ul>\
		<div style="clear:both"></div>\
	</li>\
	';
	
}
function recShowChild(li,bomId,detCode,list){

	var id = li.attr("id");
	
	var pqty = li.attr("qty");
	if(pqty=="" || pqty == undefined) pqty = 1;
	else pqty = Number(pqty);
	var plossRate = li.attr("lossRate");
	if(plossRate=="" || plossRate == undefined) plossRate = 0;
	else plossRate = Number(plossRate);
	
	var ul = $("#"+id+"_ul");
	  
	var width=li.find(">a").css("width");
	width = width.substring(0,width.length-2);
	width = Number(width)-18;
	for(var i=0;i<list.length;i++){
		var newCode = detCode;
		for(var k=0;k<5 - (''+(i+1)).length;k++){
			newCode +='0';
		}
		newCode =newCode+(i+1);
		ul.append(getctreeli(list[i],bomId,newCode,pqty,plossRate,width));
		if(list[i].attrType != 1 && list[i].attrType != 4 && list[i].childBomId != ""){ //虚拟件，且存在子bom，自动展开
			jQuery.ajax({url:"/Mrp.do?type=openBom",data:"bomId="+list[i].childBomId+"&mrpId="+$("#mrpId").val()+"&parentCode="+newCode,success:function(json){ 
				if(json.code == "ERROR"){
					if(typeof(top.jblockUI)!="undefined" ){
						top.junblockUI()
					}
					alert("执行错误");
					return;
				}
				recShowChild($("li[detCode="+newCode+"]"),json.bomId,newCode,json.list);
				$("li[detCode="+newCode+"]").find(">ul").show();
				//屏蔽半成品的用量信息
				$("li[detCode="+newCode+"]").find(">a span[name=totalQty]").text("");
				$("li[detCode="+newCode+"]").find(">a span[name=totalLoss]").text("");
				$("li[detCode="+newCode+"]").find(">a span[name=ProduceQty]").text("");
				$("li[detCode="+newCode+"]").find(">a span[name=clientSupper]").empty();
				if(list[i].attrType !=3) {
					$("li[detCode="+newCode+"]").find(">a span[name=alone]").append("<select name='alone' onclick='changeAlone(this)'><option value='no'>合并工令</option><option value='yes'>独立工令</option></select>");
					$("li[detCode="+newCode+"]").find(">a select[name=alone]").val($("li[detCode="+newCode+"]").attr("alone"));
					if($("li[detCode="+newCode+"]").attr("alone")=='yes'){
						$("li[detCode="+newCode+"]").find(">a span[name=Produce]").append("<input style='border: 1px; border-style: inset;' onchange='changeProduceQty(this)' name='ProduceQty'/> ");
						$("li[detCode="+newCode+"]").find(">a input[name=ProduceQty]").val($("li[detCode="+newCode+"]").attr("ProduceQty"));
					}
					//设置是否客供
					$("li[detCode="+newCode+"]").find(">a span[name=clientSupper]").empty();
				}
				
			},dataType:"json",async:false }); 
		}
	}
}
function changeBom(){
	$("#treeDemo_1_ul").empty(); //清空原展开数据
	if(typeof(top.jblockUI)!="undefined" ){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	jQuery.post("/Mrp.do?type=openBom",{bomId:$("#bomId").val(),mrpId:$("#mrpId").val(),parentCode:''},function(json){ 
		if(json.code == "ERROR"){
			if(typeof(top.jblockUI)!="undefined" ){
				top.junblockUI()
			}
			alert("执行错误");
			return;
		}
		recShowChild($("#treeDemo_1"),$("#bomId").val(),'',json.list);
		$('li[treenode=""]').each(function(){
			setIcon($(this));
		});
		setNo($("#treeDemo_1_ul"));
		
		if(typeof(top.jblockUI)!="undefined" ){
			top.junblockUI()
		}
	},"json" ); 
	
}
function changeSQty(){
	var totalSqty=0;
	$("input[name=sQty]").each(function(){
		var ptr = $(this).parent().parent();
		totalSqty +=Number($(this).val());
	});
	$("#needQty").val(totalSqty);
	$("li[totalQty]").each(function(){
		var totalQty = Number($(this).attr("qty"))*totalSqty;
		$(this).attr("totalQty",totalQty);
		$(this).attr("ProduceQty",totalQty);
		$(this).attr("totalLoss",Number($(this).attr("lossRate"))*totalQty/100);
		if($(this).find(">a >span span[name=totalQty]").text()!=""){
			$(this).find(">a >span span[name=totalQty]").text(totalQty);
			$(this).find(">a >span span[name=totalLoss]").text(Number($(this).attr("lossRate"))*totalQty/100);
			$(this).find(">a >span input[name=ProduceQty]").val(totalQty);
		} 
	});
}

jQuery(document).ready(function(){
	changeBom();
});

</script>
<style type="text/css">
<!--
.bomDet{
	width:100%;
	height: 18px;
}
.selectdBomDet{
	background-color:#D6D67B;
}
.bomDet ul{
	text-align: center;
    float:right;
}
.bomDet ul li{
	float:left;  
	height: 18px;  
    color: #fff;
    line-height: 180%;
    border-left: 1px solid #bbb;
    text-align: center;
    font-size: 12px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}
.bomDet ul li span{
	display: inline-block;
}
.treeDemoSpan{
	display: inline-block;
	float:right;
	border-bottom: 1px solid #bbb;
    border-right: 1px solid #bbb;
}
.tree li a {
    margin: 0 0 0 -5px;
    display: inline-block;
    cursor: auto;
}

.tree input {
    height: 18px;
    display: block;
    float: left;
    background: none;
    box-sizing: border-box;
    color: #000;
    border: 0;
    text-align: left;
    width: 100%;
    height: expression(this.type=="text"?'100%');
    display: expression(this.type=="text"?'block');
    float: expression(this.type=="text"?'left');
}

.tree li button.ico_docu {background: url(/js/ztree/zTreeStyle/img/folder_Close.gif);}


-->
</style>
</head>
<body style="overflow:hidden"  onload="changeConfirm();">

<div id="Larger_content">
<iframe   name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" id="form"  name="form" action="/Mrp.do?type=selBom"  target="">
<input type="hidden" name="BillDate1" value="$!BillDate1">
<input type="hidden" name="BillDate2" value="$!BillDate2">
<input type="hidden" name="BillType" value="$!BillType">
<input type="hidden" name="BillNo" value="$!BillNo">
<input type="hidden" name="ComFullNamen" value="$!ComFullNamen">
<input type="hidden" name="EmpFullName" value="$!EmpFullName">
<input type="hidden" name="DeptFullName" value="$!DeptFullName">
<input type="hidden" id="saleOrders" name="saleOrders" value="$!saleOrders">
<input type="hidden" id="GoodsCode" name="GoodsCode" value="$!GoodsCode">
<input type="hidden" id="mrpId" name="mrpId" value="$mrpId">

<div class="Heading">
	<div class="HeadingTitle">
		<b class="icons b-list"></b>
		BOM展开
	</div>
	<ul class="HeadingButton f-head-u">		
		<li><span class="btn btn-small" id="toGoods" name="toGoods" onClick="toGoods();" >上一步</span></li>
		<li><span class="btn btn-small" id="toGoods" name="toGoods" onClick="toSave();" >保存</span></li>
		<li><span class="btn btn-small" id="toGoods" name="toGoods" onClick="closeWindows('PopMainOpdiv');" >返回</span></li>
	</ul>
</div>
<div id="listRange_id" style="position: relative; height: 210px;">
	<div class="wrapInside">
		<div class="listRange_1" id="listRange_mainField">
			<ul class="wp_ul">
			    <li >
				    <div class="swa_c1 d_6" style="color:#c0c0c0;" title="料件编号">
					    <div class="d_box">
						    <div class="d_test d_mid">料件编号</div>
					    </div>
				    	<div class="d_mh">&nbsp;</div>
			    	</div>
			    	<div class="swa_c2">
			    		<input id="tblGoods_GoodsNumber" name="tblGoods_GoodsNumber" value="$!goodsObj.get("GoodsNumber")" type="text"  readonly="readonly"  disableautocomplete="true" autocomplete="off" >
			    	</div>
			    </li>
			    <li >
				    <div class="swa_c1 d_6" style="color:#c0c0c0;" title="料件名称">
					    <div class="d_box">
						    <div class="d_test d_mid">料件名称</div>
					    </div>
					    <div class="d_mh">&nbsp;</div>
			    	</div>
			    	<div class="swa_c2">
			    		<input id="tblGoods_GoodsFullName" name="tblGoods_GoodsFullName" value="$!goodsObj.get("GoodsFullName")" type="text" readonly="readonly" disableautocomplete="true" autocomplete="off" >
			    	</div>
			    </li>
			    <li class="longChar">
				    <div class="swa_c1 d_6" style="color:#c0c0c0;" title="规格">
					    <div class="d_box">
					    	<div class="d_test d_mid">规 格</div>
					    </div>
					    <div class="d_mh">&nbsp;</div>
				    </div>
				    <div class="swa_c2">
				    	<input id="tblGoods_GoodsSpec" name="tblGoods_GoodsSpec" value="$!goodsObj.get("GoodsSpec")" type="text" class="inputMid input_type" readonly="readonly"  disableautocomplete="true" autocomplete="off" >
				    </div>
			    </li>
			    <li>
				    <div class="swa_c1 d_6" style="color:#c0c0c0;" title="单位">
					    <div class="d_box">
					    	<div class="d_test d_mid">单 位</div>
					    </div>
					    <div class="d_mh">&nbsp;</div>
				    </div>
				    <div class="swa_c2">
				    	<input id="tblGoods_BaseUnit" name="tblGoods_BaseUnit" value="$!goodsObj.get("BaseUnit")" type="text" readonly="readonly"  disableautocomplete="true" autocomplete="off" >
				    </div>
			    </li>
			    
			    <li>
				    <div class="swa_c1 d_6"  title="BOM版本">
					    <div class="d_box">
					    	<div class="d_test d_mid">BOM版本</div>
					    </div>
					    <div class="d_mh">&nbsp;</div>
				    </div>
				    <div class="swa_c2">
				    	<select id="bomId" name="bomId" onchange="changeBom()">
				    	#foreach($bl in $bomVersionList)
				    		<option value="$bl.get("id")">$bl.get("version")</option>
				    	#end
				    	</select>
				    </div>
			    </li>
			    <li>
				    <div class="swa_c1 d_6" style="color:#c0c0c0;"  title="库存量">
					    <div class="d_box">
					    	<div class="d_test d_mid">库存量</div>
					    </div>
					    <div class="d_mh">&nbsp;</div>
				    </div>
				    <div class="swa_c2">
				    	<input id="StockQty" name="StockQty" value="$!goodsObj.get("StockQty")" type="text" readonly="readonly"  disableautocomplete="true" autocomplete="off" >
				    </div>
			    </li>
			    <li>
				    <div class="swa_c1 d_6" style="color:#c0c0c0;"  title="在途量">
					    <div class="d_box">
					    	<div class="d_test d_mid">在途量</div>
					    </div>
					    <div class="d_mh">&nbsp;</div>
				    </div>
				    <div class="swa_c2">
				    	<input id="OrderQty" name="OrderQty" value="$!goodsObj.get("OrderQty")" type="text" readonly="readonly"  disableautocomplete="true" autocomplete="off" >
				    </div>
			    </li>
			    <li>
				    <div class="swa_c1 d_6" style="color:#c0c0c0;"  title="在制量">
					    <div class="d_box">
					    	<div class="d_test d_mid">在制量</div>
					    </div>
					    <div class="d_mh">&nbsp;</div>
				    </div>
				    <div class="swa_c2">
				    	<input id="ProduceQty" name="ProduceQty" value="$!goodsObj.get("ProduceQty")" type="text" readonly="readonly"  disableautocomplete="true" autocomplete="off" >
				    </div>
			    </li>
			    <li>
				    <div class="swa_c1 d_6" style="color:#c0c0c0;"  title="占用量">
					    <div class="d_box">
					    	<div class="d_test d_mid">占用量</div>
					    </div>
					    <div class="d_mh">&nbsp;</div>
				    </div>
				    <div class="swa_c2">
				    	<input id="PossesQty" name="PossesQty" value="$!goodsObj.get("PossesQty")" type="text" readonly="readonly"  disableautocomplete="true" autocomplete="off" >
				    </div>
			    </li>
			    <li>
				    <div class="swa_c1 d_6" style="color:#c0c0c0;"  title="可用量">
					    <div class="d_box">
					    	<div class="d_test d_mid">可用量</div>
					    </div>
					    <div class="d_mh">&nbsp;</div>
				    </div>
				    <div class="swa_c2">
				    	<input id="totalQty" name="totalQty" value="$!goodsObj.get("totalQty")" type="text" readonly="readonly"  disableautocomplete="true" autocomplete="off" >
				    </div>
			    </li>
			    <li>
				    <div class="swa_c1 d_6"style="color:#c0c0c0;" title="规划数量">
					    <div class="d_box">
					    	<div class="d_test d_mid">规划数量</div>
					    </div>
					    <div class="d_mh">&nbsp;</div>
				    </div>
				    <div class="swa_c2">
				    	<input id="needQty" name="needQty" type="text" value="$!qty" readonly="readonly"  onfocus="mainFocus(this);" onkeydown="if(event.keyCode==13){tabObj.next(this);}" disableautocomplete="true" autocomplete="off">
				    </div>
			    </li>
			    <div style="clear:both;"></div>
			</ul>
		</div>
		<div class="showGridTags">
			<span show="trees" id="openBomDIVTitle" name="detTitle" class="tags tagSel" 
			onclick="changeTags(this);">BOM展开</span>
			<span show="billDetailTableDIV" id="billDetailTableDIVTitle" name="detTitle" class="tags " 
			onclick="changeTags(this);">单据明细</span>
			<span show="calRemark" id="calRemarkTableDIVTitle" name="detTitle" class="tags " 
			onclick="changeTags(this);">规则说明</span>
		</div>

		<div class="scroll_function_small_ud" id="conter" style="height: 100px;overflow:auto">
				<div style="height: auto;overflow:auto;float:left;width:1300px;padding-top: 0px;" id="trees">
				<div class="zTreeDemoBackground">
					<ul id="treeDemo" class="tree" style="width:1201px;padding-top:0px;">
						<li id="treeDemo_1" treenode="">
							<button type="button" id="treeDemo_1_switch" style="float:left" title="" class="switch_root_open" treenode_switch="" onfocus="this.blur();"></button>
							<a id="treeDemo_1_a" treenode_a="" onclick="" target="_blank" style="float:left;width:1182px;background: #5fa3e7; background-image: -webkit-linear-gradient(top,#5fa3e7,#428bca); background-image: linear-gradient(top,#5fa3e7,#428bca);" class="">
								<button type="button" id="treeDemo_1_ico" style="float:left" title="" treenode_ico="" onfocus="this.blur();" class=" ico_open" style=""></button>
								<span id="treeDemo_1_span" style="width:1163px;" class="treeDemoSpan">
									<div class="bomDet">
										<ul>
											<li><span style="width:150px">料件编号</span></li>
											<li><span style="width:160px">料件名称</span></li>
											<li><span style="width:200px">规格</span></li>
											<li><span style="width:40px">单位</span></li>
											<li><span style="width:40px">来源码</span></li>
											<li><span style="width:60px">单位用量</span></li>
											<li><span style="width:50px">损耗率</span></li>		
											<li><span style="width:70px">需求量</span></li>
											<li><span style="width:70px">备品量</span></li>
											<li><span style="width:50px">客供包料</span></li>
											<li><span style="width:60px">独立工令</span></li>
											<li><span style="width:70px">计划数量</span></li>
										</ul>
									</div>
								</span>
							</a>
							<ul id="treeDemo_1_ul" class="" style="">
							</ul>
							<div style="clear:both"></div>
						</li>
					</ul>					

				</div>	
			</div>
			<div  name="billDetailTableDIV" id="billDetailTableDIV"  style="overflow:auto; width:1050px;display:none" > 
	<div style="width:100%;height:100%;position:relative">
	<div id="k_head" style="z-index: 50; overflow: hidden; width: 1040px; position: absolute; top: 0px; left: 0px;">
	<table id="k_listgrid" style="table-layout:fixed" border="0" cellspacing="0" width="920px">
		<thead>
			<tr height="24">
				<td width="35">No.</td>
				<td width="70">单据类型</td>
				<td width="100">单据编号</td>
				<td width="80">单据日期</td>
				#if($!globals.getSysSetting("productByOrder")=="true")
				<td width="100">客户编号</td>
				<td width="120">客户名称</td>
				#end
				<td width="60">制单人</td>
				<td width="80">单据总数量</td>
				<td width="80">已规划数量</td>
				<td width="80">本次规划量</td>
			</tr>
		</thead>
	</table>
	</div>
	<div id="k_data" style="z-index: 40; width: 1040px;overflow-x:hidden; overflow-y: auto; position: absolute; top: 0px; left: 0px; height: 354px;">
	<table id="k_listgrid" style="table-layout:fixed" border="0" cellspacing="0" width="920px">
		<thead>
			<tr height="24">
				<td width="35">No.</td>
				<td width="70">单据类型</td>
				<td width="100">单据编号</td>
				<td width="80">单据日期</td>
				#if($!globals.getSysSetting("productByOrder")=="true")
				<td width="100">客户编号</td>
				<td width="120">客户名称</td>
				#end
				<td width="60">制单人</td>
				<td width="80">单据总数量</td>
				<td width="80">已规划数量</td>
				<td width="80">本次规划量</td>
			</tr>
		</thead>
		<tbody>
			#foreach($row in $salesList)
			<tr height="28px" name="salesList" billId="$row.get("id")" BillType="$row.get("BillType")" BillNo="$row.get("BillNo")" 
				BillDate="$row.get("BillDate")" DepartmentCode="$row.get("DepartmentCode")" EmployeeId="$row.get("EmployeeId")" 
				CompanyCode="$row.get("CompanyCode")" createBy="$row.get("createBy")" 
				outqty="$globals.newFormatNumber($row.get("outqty"),false,false,true,"PDMRPBill","outqty",true)" 
				MRPQty="$globals.newFormatNumber($row.get("MRPQty"),false,false,true,"PDMRPBill","MRPQty",true)" 
				TotalQty="$globals.newFormatNumber($row.get("TotalQty"),false,false,true,"PDMRPBill","TotalQty",true)" 
				qty="$globals.newFormatNumber($row.get("qty"),false,false,true,"PDMRPBill","qty",true)"  onclick="gselectKeyId(this)" class="spaceRow">
				<td>$velocityCount</td>
				<td align="left">#if($row.get("BillType")=="PDProduceRequire")制造需求单#else 销售订单 #end</td>
				<td align="left"><span style="color:blue;cursor:pointer" onclick="detail('$row.get("BillType")','$row.get("id")')">$row.get("BillNo")</span> </td>
				<td align="center">$row.get("BillDate")</td>
				#if($!globals.getSysSetting("productByOrder")=="true")
				<td align="left">$row.get("ComNumber")</td>
				<td align="left">$row.get("ComName")</td>
				#end
				<td align="left">$row.get("createByName")</td>
				<td align="right">$globals.newFormatNumber($row.get("TotalQty"),false,false,true,"PDMRPBill","TotalQty",true)</td>
				<td align="right">$globals.newFormatNumber($row.get("MRPQty"),false,false,true,"PDMRPBill","MRPQty",true)</td>
				<td align="right"><input name="sQty" style=" width: 78px;" onchange="changeSQty()" value="$globals.newFormatNumber($row.get("qty"),false,false,true,"PDMRPBill","qty",true)"/></td>
			</tr>
			#end
		</tbody>
	</table>
	</div>
</div>

			</div>
			<div  name="calRemark" id="calRemark"  style="overflow:auto; width:1050px;display:none" >
			<div style="padding-left: 20px;font-size: 17px;border: solid 1px #CAC1CA;">
<p>说明：</p>
<div style="padding-left: 20px;font-size: 15px;">
<p>库存量=公司仓中实际库存量+委外仓中欠料数量（不包括委外仓，车间仓，客供仓中数量）</p>
<p>在途量=当前未结案请购单未入库数量（入库后转为库存量，在入库之前的所有状态包括请购、订购、检验都统一为在途量）</p>
<p>在制量=当前未结案工令单未缴库数量</p>
<p>占用量=未结案未终止销售订单已下工令数量</p>
<p>可用量=库存量+在途量+在制量-占用量</p>
<p>单位用量=BOM中生产1单位成品所需要耗用的原料的数量</p>
<p>需求量=成品的规划数量*原料BOM单位用量</p>
<p>损耗率=BOM中每种原料设定的标准损耗</p>
<p>备品量=标准损耗率*需求量 即标准损耗量</p>
<p>规划数量=单据明细规划数量之和，单据明细规划数量可根据需求增加或减少</p>
<p>BOM展开：当来源码是虚拟件时，BOM必须展开到下级，当来源码是采购件时不可以展开BOM下级，其它情况可自由决定BOM展开到哪一级别，如果展开，则原料采购以展开的下级物料为准，</p>
<p>客供：由客户提供原料生产，此时不产生原料的占用和请购</p>
<p>包料：委外生产时由加工厂提供原料生产，本生司不需发料，此时不产生原料的占用和请购且工令单原料表中不产生记录不可以发料</p>
<p>独立工令：该半成品产生一张独立的工令单，半成品需先缴库后再作为原料做发料单给上级工令单。如果选择独立工令，可以修改半成品工令的生产数量</p>
<p>合并工令：半成品不会产生工令单，发料时直接发半成品的下级物料，缴库时直接缴入成品免去中间半成品的缴库、出库、成本核算等过程。</p>
</div>
			</div>

			</div>
		</div> <!-- 明细图片区 -->

		<div id="listRange_remark"><!-- 显示备注和html编辑框 -->

 		</div>
		<div><span style="color:#0000FF;line-height:20px"></span></div>
	</div>
</div>

</form>
</div>
	<script type="text/javascript">  
		var oDiv2=document.getElementById("listRange_id"); 
		var sHeight2=document.documentElement.clientHeight-40;
		oDiv2.style.height=sHeight2+"px";
					
		var mainFieldH  = $("#listRange_mainField").height();	
		var remarkH = $("#listRange_remark").height();
		var tabH = sHeight2-mainFieldH-remarkH-45;
		if(tabH<150) tabH=150;
		
		$("#conter").height(tabH);	
		$("#conter").width(document.documentElement.clientWidth);
		$("#conter > div").height($("#conter").height());	
		
	</script>

<script>
	//在文字后加空格，chrome下实现两端对齐    
	cyh.lableAlign(); 
</script>
</body>
</html>
