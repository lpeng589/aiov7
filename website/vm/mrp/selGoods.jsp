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
function openInputDate(obj)
{
	WdatePicker({lang:'$globals.getLocale()'});
}
//合并BOM，读取新的BOM结构,所有采购件都按BOM树的数量归为第一级BOM
function recGetBomItemMerge(bomList,liObj){
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
		so['attrType']=$(this).attr("attrType");
		so['dtype']=$(this).attr("dtype");
		so['GoodsCode']=$(this).attr("GoodsCode");
		so['baseLossRate']=$(this).attr("baseLossRate");
		so['baseQty']=$(this).attr("baseQty");
		
		console.log(so['GoodsCode']+";;"+so['dtype']);
		
		var isOpen="0";
		if(so['childBomId'] != "" && $(this).find(">ul").size() >0 && $(this).find(">ul").is(':visible')){
			isOpen = "1";
		}
		so['isOpen']=isOpen;
		if(so['dtype']=="1"){//只有采购件才加入
			bomList[bomList.length] = so;
		}else { //非采购件递归到下级物料
			recGetBomItemMerge(bomList,$(this));
		}
		
	});
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
		so['attrType']=$(this).attr("attrType");
		so['dtype']=$(this).attr("dtype");
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
function toSave(obj){
	var gdDiv = $(obj).parents(".goodsDiv");
	var ctype = gdDiv.find("select[name=type]").val();
	if(ctype==-1){
		alert("请先选择生产方式");
		return;
	}
	if(!confirm("确定规划，并保存吗？")){
		return;
	}
	changeTags($("#openBomDIVTitle")[0]); 
	var saveValues=new Object();
	saveValues.GoodsCode=gdDiv.attr("GoodsCode");
	saveValues.needQty=gdDiv.attr("NeedQty");
	saveValues.bomId=gdDiv.find("select[name=bomId]").val();
	saveValues.id=gdDiv.attr("mrpId");
	saveValues.MRPType=gdDiv.find("select[name=type]").val();
	saveValues.startWork = gdDiv.find("input[name=startWork]").val();
	saveValues.endWork = gdDiv.find("input[name=endWork]").val();
	var merge = gdDiv.find("input[name=mergeBom]").is(":checked");
	saveValues.merge = merge?"1":"0"; //是否合并

	var salesList = new Array();
	gdDiv.find("tr[name=salesList]").each(function(){
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
		so['qty']=$(this).attr("qty");
		so['Urgency']=$(this).attr("Urgency");
		
		salesList[salesList.length] = so;
	});
	saveValues.salesList = salesList;
	//上传bom
	/**
	var bomList = new Array();
	if(ctype==0||ctype==2){ //直接出库和采购不用展开BOM
		
		if(gdDiv.find("input[name=mergeBom]").is(":checked")){
			//合并BOM
			recGetBomItemMerge(bomList,gdDiv.find("#treeDemo_1"));
			console.log(bomList);
			//合并BOM相当于改变了BOM结构，形成一个新的BOM,因些调整bom中内容
			var newCode = 10000;
			for(var i=0;i<bomList.length;i++){
				bomList[i].bomId=saveValues.bomId;
				bomList[i]['childBomId']='';
				bomList[i]['detCode']=newCode+"";
				newCode++;
				bomList[i]['baseQty']=bomList[i]['qty'];
				bomList[i]['baseLossRate']=bomList[i]['lossRate'];
				bomList[i]['isOpen']="0";
			}
		}else{//不合并
			recGetBomItem(bomList,gdDiv.find("#treeDemo_1"));
		}
	}
	saveValues.bomList = bomList;
	*/
	//这里只需记录系统万能码的生产组织方式
	saveValues.goodsdeploy = new Array();
	gdDiv.find("li[attrType=4]").each(function(){
		var ob = new Object();
		ob.GoodsCode=$(this).attr("GoodsCode");
		ob.dtype=$(this).attr("dtype");
		saveValues.goodsdeploy[saveValues.goodsdeploy.length] = ob;
	});
	var postdata = JSON.stringify(saveValues); //生成所有需提交数据的json字符串
	console.log(postdata);
	if(typeof(top.jblockUI)!="undefined" ){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	jQuery.post("/Mrp.do?type=saveBom",{data:postdata},function(json){ 
		if(typeof(top.jblockUI)!="undefined" ){
			top.junblockUI()
		}
		if(json.code == "ERROR"){
			alert(json.msg);
		}else{
			$(obj).hide();
			alert("保存成功");
		}
	},"json" ); 
}

function selectItem(obj){
	$(".selectdBomDet").removeClass("selectdBomDet");
	$(obj).find("div").addClass("selectdBomDet");
}
function changeAlone(obj){
	var liobj = $(obj).parent().parent().parent().parent().parent().parent().parent();
	liobj.attr("dtype",$(obj).val());
	if($(obj).val() == '1'){ //采购
		liobj.find("#"+liobj.attr('id')+"_ul").hide();
		liobj.find(">button").removeClass("switch_bottom_open");
		liobj.find(">button").addClass("switch_bottom_close");
	}else{
		liobj.find("#"+liobj.attr('id')+"_ul").show();
		liobj.find(">button").removeClass("switch_bottom_close");
		liobj.find(">button").addClass("switch_bottom_open");
	}
}

function changeProduceQty(obj){
	$(obj).parent().parent().parent().parent().parent().parent().parent().attr("ProduceQty",$(obj).val());
}
function getctreeli(data,bomId,detCode,pqty,plossRate,width,NeedQty){
	//找出最大的li编号
	var maxli=1;
	$("li[id*=treeDemo]").each(function(){
		lid = Number($(this).attr("id").substring(9));
		if(lid>maxli){maxli=lid;}
	});
	maxli++;
	var totalQty = pqty*data.qty*NeedQty;
	totalQty = f(totalQty, $globals.getSysSetting("DigitsQty") );
	var totalLoss = (plossRate+data.lossRate)*totalQty/100;
	totalLoss = f(totalLoss,$globals.getSysSetting("DigitsQty") );
	var attrTypeName = "";
	if(data.attrType==0){  
		attrTypeName = "M自制";
	}else if(data.attrType==1){
		attrTypeName = "P采购";
	}else if(data.attrType==2){
		attrTypeName = "S委外";
	}else if(data.attrType==3){
		attrTypeName = "X客供";
	}else if(data.attrType==4){
		attrTypeName = "W万能";
	}
	
	return ' \
	<li id="treeDemo_'+maxli+'" treenode="" GoodsCode="'+data.classCode+'"  detCode="'+detCode+'"   bomId="'+bomId+'" bomDetId="'+data.bomDetId+'"  childBomId="'+data.childBomId+'" qty="'+pqty*data.qty+'" \
		lossRate="'+(plossRate+data.lossRate)+'"  baseQty="'+data.qty+'"  baseLossRate="'+(data.lossRate)+'" totalQty="'+(totalQty)+'" totalLoss="'+(totalLoss)+'" \
		attrType="'+data.attrType+'" dtype="'+data.attrType+'">\
		<button type="button"  style="float:left" id="treeDemo_'+maxli+'_switch" title=""  class="switch_bottom_docu" treenode_switch="" onfocus="this.blur();"></button>\
		<a id="treeDemo_'+maxli+'_a" treenode_a="" onclick="selectItem(this)"  target="_blank" style="float:left;width:'+width+'px;">\
			<button type="button"  style="float:left" id="treeDemo_'+maxli+'_ico" title="" treenode_ico="" onfocus="this.blur();" class=" ico_open" style=""></button>\
			<span id="treeDemo_'+maxli+'_span" style="width:'+(width-19)+'px;"  class="treeDemoSpan">\
				<div class="bomDet">\
					<span name="tno"></span>\
					<ul>	\
						<li><span style="width:180px;text-align:left;color:#000">'+data.GoodsNumber+'</span></li>\
						<li><span style="width:200px;text-align:left;color:#000">'+data.GoodsFullName+'</span></li>	\
						<li><span style="width:180px;text-align:left;color:#000" name="specLi">'+data.GoodsSpec+'</span></li>\
						<li><span style="width:40px;text-align:left;color:#000">'+attrTypeName+'</span></li>\
						<li><span style="width:55px;text-align:left;color:#000" name="dtype"></span></li>\
					</ul>\
				</div>\
			</span>\
		</a>\
		<ul id="treeDemo_'+maxli+'_ul" class="line" style="display:none"></ul>\
		<div style="clear:both"></div>\
	</li>\
	';
}

function recShowChild(li,mrpId,bomId,detCode,list,NeedQty){

	var id = li.attr("id");
	
	var pqty = li.attr("qty");
	if(pqty=="" || pqty == undefined) pqty = 1;
	else pqty = Number(pqty);
	var plossRate = li.attr("lossRate");
	if(plossRate=="" || plossRate == undefined) plossRate = 0;
	else plossRate = Number(plossRate);
	
	var ul = li.find("#"+id+"_ul");
	  
	var width=li.find(">a").css("width");
	width = width.substring(0,width.length-2);
	width = Number(width)-18;
	for(var i=0;i<list.length;i++){
		var newCode = detCode;
		for(var k=0;k<5 - (''+(i+1)).length;k++){
			newCode +='0';
		}
		newCode =newCode+(i+1);
		ul.append(getctreeli(list[i],bomId,newCode,pqty,plossRate,width,NeedQty));
		if(list[i].attrType != 1 && list[i].attrType != 3 && list[i].childBomId != ""){ //自制委外万能，且存在子bom，自动展开
			if(typeof(top.jblockUI)!="undefined" ){
				top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
			}
			jQuery.ajax({url:"/Mrp.do?type=openBom",data:"bomId="+list[i].childBomId+"&mrpId="+mrpId+"&parentCode="+newCode,success:function(json){ 
				if(typeof(top.jblockUI)!="undefined" ){
					top.junblockUI()
				}
				if(json.code == "ERROR"){
					alert("执行错误");
					return;
				} 
				recShowChild(li.find("li[detCode="+newCode+"]"),mrpId,json.bomId,newCode,json.list,NeedQty);
				li.find("li[detCode="+newCode+"]").find(">ul").show();
				//屏蔽半成品的用量信息
				var dTypeStr="";
				if(list[i].attrType == "4"){
					dTypeStr = "<option value='0'>自制</option><option value='1'>采购</option><option value='2'>委外</option> ";
					li.find("li[detCode="+newCode+"]").find(">a span[name=dtype]").append("<select name='dtype' onChange='changeAlone(this)'>"+dTypeStr+"</select>");
					li.find("li[detCode="+newCode+"]").attr("dtype",li.find("li[detCode="+newCode+"]").find(">a select[name=dtype]").val());
				}else{
					li.find("li[detCode="+newCode+"]").attr("dtype",list[i].attrType);
				}
				  
			},dataType:"json",async:false }); 
		}
	}
}
function setIcon(obj){
	if(obj.attr("childBomId") != ""){
		//有下级
		if(obj.find(">ul").is(":visible")){
			obj.find(">button").removeClass();
			obj.find(">button").addClass("switch_bottom_open");
		}else if(obj.find(">ul li").size()>0){
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
function changeBom(bomObj){
	if($(bomObj).val()!=$(bomObj).attr("lastId")){
		if(!confirm("是否确定选择旧版本的BOM用于本次生产？")){
			$(bomObj).val($(bomObj).attr("lastId"));
			return;
		}
	}
	var goodsDivObj = $(bomObj).parents(".goodsDiv");
	goodsDivObj.find("#treeTitle").width(maxWidth);
	goodsDivObj.find("#treeDemo").width(maxWidth);
	goodsDivObj.find("#trees").width(maxWidth);
	goodsDivObj.find("#treeDemo_1_span").width(maxWidth-18-19);
	goodsDivObj.find("#treeDemo_1_ul").empty(); //清空原展开数据
	
	if(typeof(top.jblockUI)!="undefined" ){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	jQuery.post("/Mrp.do?type=openBom",{bomId:$(bomObj).val(),mrpId:goodsDivObj.attr("mrpId"),parentCode:''},function(json){ 
		if(typeof(top.jblockUI)!="undefined" ){
			top.junblockUI()
		}
		if(json.code == "ERROR"){
			alert("执行错误");
			return;
		}
		recShowChild(goodsDivObj.find("#treeDemo_1"),goodsDivObj.attr("mrpId"),$(bomObj).val(),'',json.list,goodsDivObj.attr("NeedQty"));
		
		//隐藏所有采购件
		goodsDivObj.find("#treeDemo_1").find("li[attrType=1]").hide();
		goodsDivObj.find("span[name=specLi]").width(specLiWidth);
		goodsDivObj.find('li[treenode=""]').each(function(){
			setIcon($(this));
		});
		setNo(goodsDivObj.find("#treeDemo_1_ul"));
	},"json" ); 
	
}

function detail(tableName,keyId){
	turl="/UserFunctionAction.do?tableName="+tableName+"&keyId="+keyId+"&operation=$globals.getOP("OP_DETAIL")";
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	openPop('PopMainOpdiv','',turl,width,height,false,height==document.documentElement.clientHeight);  
	
}
function changeTags(obj){
	$(obj).addClass("tagSel").siblings("span").removeClass("tagSel");
	$(obj).parents(".goodsDiv").find("#"+$(obj).attr("show")).show().siblings("div").hide();
	$(obj).parents(".goodsDiv").find("#"+$(obj).attr("show")).find(":text:eq(0)").focus();
}
function changeType(obj){ 
	var type = $(obj).val();
	var gdDiv = $(obj).parents(".goodsDiv");
	if(type == 0 || type==2){
		//生产，展开物料
		
		var bomObj = gdDiv.find("select[name=bomId]");
		if(bomObj.size()==0){
			alert("本物料还没有建立BOM不可以选择生产");
			$(obj).val(0);
			return;
		}else{
			gdDiv.find(".line3").show();
			if(gdDiv.find(".line3").find("#treeDemo_1_ul").find("li").size() == 0)
			{
				changeBom(bomObj);
				//修改成品的生产方式
				gdDiv.find("#treeDemo_1").attr("dtype",type);
			}
		}
	}else{
		gdDiv.find(".line3").hide();
	}
}
//合并bom
function mergeBomFun(obj){
	var gdDiv = $(obj).parents(".goodsDiv");
	if($(obj).is(":checked")){
		//检查来源码是否一致
		var at0 =gdDiv.find("li[dtype=0]").size();
		var at2 =gdDiv.find("li[dtype=2]").size();
		if(at0>0 && at2>0){
			alert("BOM中料件来源码有自制也有委外，不可以合并BOM生产");
			$(obj).prop("checked",false);
			return false;
		}
		gdDiv.find("#treeDemo_1").find("#treeDemo_1_ul").hide();
		gdDiv.find("#treeDemo_1").find(">button").removeClass("switch_bottom_open");
		gdDiv.find("#treeDemo_1").find(">button").addClass("switch_bottom_close");
	}else{
		gdDiv.find("#treeDemo_1").find("#treeDemo_1_ul").show();
		gdDiv.find("#treeDemo_1").find(">button").removeClass("switch_bottom_close");
		gdDiv.find("#treeDemo_1").find(">button").addClass("switch_bottom_open");
	}
}

function toSales(){
	document.form.action="/Mrp.do?type=selSalesOrder";
	document.form.submit();
}

#if("$!msg" != "")
	alert("$!msg");
#end	
var maxWidth=0;
jQuery(document).ready(function(){
	maxWidth = $(".goodsDiv").width()-30;
	specLiWidth = maxWidth - 150-500;
	
});
</script>
<style type="text/css">
.HeadingTitle{white-space:nowrap;}
.scroll_function_small_a>div{background:#fff;}
.goodsDiv{z-index: 40; width: 96%; overflow: hidden; top: 0px; left: 0px;border: 1px solid #C1B7C1;margin:5px }
.goodsDiv .line1{padding:0px; height:30px; border-bottom: 1px solid #C1B7C1;background-color: #ECE1EC;}
.goodsDiv .line1 span{display:inline-block;overflow:hidden;  min-width: 60px;padding:2px}
.goodsDiv .line2{padding:0px; }
.goodsDiv .line2 span{display:inline-block;overflow:hidden;  min-width: 60px;padding:2px}
.goodsDiv .line3{padding:0px;border-top: 1px solid #C1B7C1; }

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

.showGridTags .tagSel {
    background: #D6DDE4;
    background-image: -webkit-linear-gradient(top,#ABB0B7,#4D5D6B);
    background-image: linear-gradient(top,#5fa3e7,#428bca);
    color: #fff;
    font-weight: bold;
}

</style>
</head>
<body style="overflow:hidden" onload="changeConfirm();">

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
<input type="hidden" name="saleOrders" value="$!saleOrders">

<div class="Heading">
	<div class="HeadingTitle">
		<b class="icons b-list"></b>
		选择商品（然后下一步）
	</div>
	<ul class="HeadingButton f-head-u">		
		<li><span class="btn btn-small" id="toGoods" name="toGoods" onClick="toSales();" >上一步</span></li>
		<li><span class="btn btn-small" id="toGoods" name="toGoods" onClick="closeWindows('PopMainOpdiv');" >返回</span></li>
	</ul>
</div>
<div class="listRange_frame" id="listRange_frame">
<div class="listRange_3" id="conditionDIV">
	       	
</div>

<div class="scroll_function_small_a" id="conter" style="overflow:hidden" >
<div style="width:100%;height:100%;overflow-x:hidden;overflow-y:auto;position:relative">
#foreach($row in $GoodsList)
	<div class="goodsDiv" GoodsCode="$row.get('GoodsCode')" mrpId="$row.get('mrpId')" 
	NeedQty="$row.get("NeedQty")"  >
		<div class="line1"> 
			#foreach($grow in $GoodsFList)
				<span style="min-width:$globals.get($grow,2)px">$globals.get($grow,1):$!globals.encodeHTML($row.get($globals.get($grow,0)))</span>
			#end
			<span style="width:120px;display:none">BOM版本：
			#if("$!row.get('bomList')" !="")
				#set($lastId = "")
				#foreach($bl in $row.get('bomList'))
		    		#if("$!bl.get('isLast')" == "1")
		    		#set($lastId = $bl.get("id"))
		    		#end
		    	#end
				<select name="bomId" lastId="$lastId" onchange="changeBom(this)">
				    	#foreach($bl in $row.get('bomList'))
				    		<option value="$bl.get("id")">$bl.get("version")</option>
				    	#end
				</select>
			#else
				无BOM	    	
			#end	    	
			</span>
			<span  style="width:350px;float:right">
				<span style="width:80px;">来源码:$globals.getEnumerationItemsDisplay('PDGoodsAttrType',$row.get("attrType")) </span>
				<span style="width:100px;">数量:$row.get("NeedQty") </span>
				<select name="type" onchange="changeType(this)" style="width:80px">
					 <option value="-1">请选择</option>
					 #if($row.get("attrType") ==0 || $row.get("attrType") ==4 ) <option value="0">自制生产</option> #end
					 #if($row.get("attrType") ==1 || $row.get("attrType") ==4 ) <option value="1">采购</option> #end
					 #if($row.get("attrType") ==2 || $row.get("attrType") ==4 ) <option value="2">委外生产</option> #end
				</select>
				<input  type="button" name="save" onclick="toSave(this)" value="保存规划"/>
			</span>
		</div>
		<div class="line3" style="display:none"> 
		<div style="margin: 10px 0 0 10px;"><span style="display:inline-block;width:90px;">计划开始生产：</span>
		<input name="startWork" class="ls3_in" style="width:105px;margin-right: 30px;" date="true" value=""  onclick="openInputDate(this);" disableautocomplete="true" autocomplete="off">
		<span  style="display:inline-block;width:90px;">计划生产完工：</span>
		<input name="endWork" class="ls3_in" style="width:105px;" date="true" value=""  onclick="openInputDate(this);" disableautocomplete="true" autocomplete="off">
		</div>
		<div class="showGridTags">
			<span show="trees" id="openBomDIVTitle" name="detTitle" class="tags tagSel" 
			onclick="changeTags(this);">BOM展开</span>
			<span show="billDetailTableDIV" id="billDetailTableDIVTitle" name="detTitle" class="tags " 
			onclick="changeTags(this);">单据明细</span>
			<div style="float:right;margin-right: 30px;">
				<input type="checkbox" id="mergeBom$row.get('mrpId')" onchange="mergeBomFun(this)" name="mergeBom" value="1"/>
				<label for="mergeBom$row.get('mrpId')" >合并BOM</label>
			</div>
		</div>
		<div class="scroll_function_small_ud"  style="height: auto;overflow:auto">
			<div style="height: auto;overflow-y:auto;overflow-x:hidden;float:left;width:1160px;padding-top: 0px;display:block" id="trees">
				<div class="bomDet" id="treeTitle" style="float:left;width:1100px;background: #5fa3e7; background-image: -webkit-linear-gradient(top,#ABB0B7,#4D5D6B); background-image: linear-gradient(top,#5fa3e7,#428bca);"><!-- 头部标题 -->
					<ul>
						<li><span style="width:180px">料件编号</span></li>
						<li><span style="width:200px">料件名称</span></li>
						<li><span style="width:180px" name="specLi">规格</span></li>
						<li><span style="width:40px">来源码</span></li>
						<li><span style="width:55px">类型</span></li>
					</ul>
				</div>
				<div class="zTreeDemoBackground">
					<ul id="treeDemo" class="tree" style="width:1101px;padding-top:0px;">
						<li id="treeDemo_1" treenode="" GoodsCode="$row.get('GoodsCode')" attrType="$row.get("attrType")" dtype="0">
							<button type="button" id="treeDemo_1_switch" style="float:left" title="" class="switch_root_open" treenode_switch="" onfocus="this.blur();"></button>
							<a id="treeDemo_1_a" treenode_a="" onclick="selectItem(this)" target="_blank"  style="float:left;;"  class="">
								<button type="button" id="treeDemo_1_ico" style="float:left" title="" treenode_ico="" onfocus="this.blur();" class=" ico_open" style=""></button>
								<span id="treeDemo_1_span" style="width:1063px;" class="treeDemoSpan">
									<div class="bomDet">
										<span name="tno">1</span>
										<ul>
											<li><span style="width:180px;text-align:left;color:#000">$!globals.encodeHTML($row.get('GoodsNumber'))</span></li>
											<li><span style="width:200px;text-align:left;color:#000">$!globals.encodeHTML($row.get('GoodsFullName'))</span></li>	
											<li><span style="width:180px;text-align:left;color:#000" name="specLi">$!globals.encodeHTML($row.get('GoodsSpec'))</span></li>
											<li><span style="width:40px;text-align:left;color:#000">$globals.getEnumerationItemsDisplay('PDGoodsAttrType',$row.get("attrType"))</span></li>
											<li><span style="width:55px;text-align:left;color:#000" name="dtype"></span></li>
										</ul>
									</div>
								</span>
							</a>
							<ul id="treeDemo_1_ul"  class="line" style="">
							</ul>
							<div style="clear:both"></div>
						</li>
					</ul>					
				</div>	
			</div>
			<div  name="billDetailTableDIV" id="billDetailTableDIV"  style="overflow:auto; width:1050px;display:none" > 
				<div id="k_data" style="z-index: 40; width: 1040px;overflow-x:hidden; overflow-y: auto; top: 0px; left: 0px; ">
					<table id="k_listgrid" style="table-layout:fixed" border="0" cellspacing="0" width="920px">
						<thead>
							<tr height="24">
								<td width="35">No.</td>
								<td width="70">单据类型</td>
								<td width="100">单据编号</td>
								<td width="80">单据日期</td>
								#if($!globals.getSysSetting("productByOrder")=="true")
								#foreach($comrow in $ComFList)
								<td width="$globals.get($comrow,2)">客户$globals.get($comrow,1)</td>
								#end
								#end
								<td width="50">紧急程度</td>
								<td width="80">单据总数量</td>
								<td width="80">已规划数量</td>
								<td width="80">本次规划量</td>
						</tr>
					</thead>
					<tbody>
						#foreach($brow in $row.get("BillList"))
						<tr height="28px" name="salesList" billId="$brow.get("BillId")" BillType="$brow.get("BillType")" BillNo="$brow.get("BillNo")" 
							BillDate="$brow.get("BillDate")" DepartmentCode="$!brow.get("DepartmentCode")" EmployeeId="$!brow.get("EmployeeId")" 
							CompanyCode="$brow.get("CompanyCode")" createBy="$!brow.get("createBy")" Urgency='$brow.get("Urgency")'
							MRPQty="$globals.newFormatNumber($brow.get("MRPQty"),false,false,true,"PDMRPBill","MRPQty",true)" 
							TotalQty="$globals.newFormatNumber($brow.get("TotalQty"),false,false,true,"PDMRPBill","TotalQty",true)" 
							qty="$globals.newFormatNumber($brow.get("NeedQty"),false,false,true,"PDMRPBill","qty",true)" class="spaceRow">
							<td>$velocityCount</td>
							<td align="left">#if($brow.get("BillType")=="PDProduceRequire")制造需求单#else 销售订单 #end</td>
							<td align="left"><span style="color:blue;cursor:pointer" onclick="detail('$brow.get("BillType")','$brow.get("BillId")')">$brow.get("BillNo")</span> </td>
							<td align="center">$brow.get("BillDate")</td>
							#if($!globals.getSysSetting("productByOrder")=="true")
							#foreach($comrow in $ComFList)
							<td align="left" >$brow.get($globals.get($comrow,0))</td>
							#end
							#end
							#set($uv = $brow.get("Urgency")) 
							<td align="center">$!globals.getEnumerationItemsDisplay('Urgency',"$uv")</td>
							<td align="right">$globals.newFormatNumber($brow.get("TotalQty"),false,false,true,"PDMRPBill","TotalQty",true)</td>
							<td align="right">$globals.newFormatNumber($brow.get("MRPQty"),false,false,true,"PDMRPBill","MRPQty",true)</td>
							<td align="right">$globals.newFormatNumber($brow.get("NeedQty"),false,false,true,"PDMRPBill","qty",true)</td>
						</tr>
						#end
					</tbody>
				</table>
				</div>				
			</div>
		</div>	
		</div><!-- line3 -->
	</div>
#end	
</div>
</div>
<script language="javascript">
	
var condHeight= 80;

var oDiv=document.getElementById("conter");
var dHeight=document.documentElement.clientHeight;
oDiv.style.height=dHeight-55-condHeight +"px";




</script>
		
</div>	

</form>
</div>
</body>
</html>
