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


<script type="text/javascript" src="$globals.js("/js/listGrid.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/function.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/k_listgrid.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/popOperation.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/dropdownselect.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/editPage.vjs","",$text)"></script>

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
	alert("不可以修改BOM展开形式");
}

function getctreeli(data,bomId,detCode,width){
	//找出最大的li编号
	var maxli=1;
	$("li[id*=treeDemo]").each(function(){
		lid = Number($(this).attr("id").substring(9));
		if(lid>maxli){maxli=lid;}
	});
	maxli++;
	
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
	var dtypeName="";  
	if((data.attrType == 0 || data.attrType == 2) && data.childBomId != "" ){
		
		if(data.dtype==1){
			dtypeName = "直接采购";
		}else if(data.dtype==2){
			dtypeName = "自制合并BOM";
		}else if(data.dtype==3){
			dtypeName = "自制不合并BOM";
		}else if(data.dtype==4){
			dtypeName = "委外合并BOM";
		}else if(data.dtype==5){
			dtypeName = "委外不合并BOM";
		}
	}
		
	return ' \
	<li id="treeDemo_'+maxli+'" treenode="" GoodsCode="'+data.GoodsCode+'"  detCode="'+detCode+'"   bomId="'+bomId+'" bomDetId="'+data.bomDetId+'"  childBomId="'+data.childBomId+'" qty="'+data.qty+'" \
		lossRate="'+(data.lossRate)+'" totalQty="'+(data.totalQty)+'" totalLoss="'+(data.totalLoss)+'" \
		ProduceQty="'+(data.ProduceQty)+'"  \
		attrType="'+data.attrType+'" dtype='+data.dtype+'>\
		<button type="button"  style="float:left" id="treeDemo_'+maxli+'_switch" title="" onclick="clicksf(this)"    class="switch_bottom_docu" treenode_switch="" onfocus="this.blur();"></button>\
		<a id="treeDemo_'+maxli+'_a" treenode_a="" onclick="selectItem(this)"  target="_blank" style="float:left;width:'+width+'px;">\
			<button type="button"  style="float:left" id="treeDemo_'+maxli+'_ico" title="" treenode_ico="" onfocus="this.blur();" class=" ico_open" style=""></button>\
			<span id="treeDemo_'+maxli+'_span" style="width:'+(width-19)+'px;"  class="treeDemoSpan">\
				<div class="bomDet">\
					<span name="tno"></span>\
					<ul>	\
						<li><span style="width:120px;text-align:left;color:#000">'+data.GoodsNumber+'</span></li>\
						<li><span style="width:140px;text-align:left;color:#000">'+data.GoodsFullName+'</span></li>	\
						<li><span style="width:180px;text-align:left;color:#000">'+data.GoodsSpec+'</span></li>\
						<li><span style="width:40px;text-align:left;color:#000">'+attrTypeName+'</span></li>\
						<li><span style="width:50px;text-align:left;color:#000">'+data.qty+'</span></li>	\
						<li><span style="width:40px;text-align:left;color:#000">'+(data.lossRate)+'</span></li>\
						<li><span style="width:70px;text-align:left;color:#000" name="totalQty">'+data.totalQty+'</span></li>\
						<li><span style="width:60px;text-align:left;color:#000" name="totalLoss">'+(data.totalLoss)+'</span></li>	\
						<li><span style="width:50px;text-align:left;color:#000" name="clientSupper">'+(data.clientSupper=="isClient"?'客供':(data.clientSupper=="isSupper"?'包料':''))+'</span></li>\
						<li><span style="width:90px;text-align:left;color:#000" name="alone">'+dtypeName+'</span></li>\
						<li><span style="width:60px;text-align:left;color:#000" name="Produce">'+(data.isOpen=="1" && (data.dtype=="3"||data.dtype=="5")?data.ProduceQty:'')+'</span></li>\
					</ul>\
				</div>\
			</span>\
		</a>\
		<ul id="treeDemo_'+maxli+'_ul" class="line" style="display:none"></ul>\
		<div style="clear:both"></div>\
	</li>\
	';
}
function recShowChild(li,parentDetCode,list){

	var id = li.attr("id");
	
	var ul = $("#"+id+"_ul");
	  
	var width=li.find(">a").css("width");
	width = width.substring(0,width.length-2);
	width = Number(width)-18;
	for(var i=0;i<list.length;i++){
		if(list[i].detCode.indexOf(parentDetCode)==0 && list[i].detCode.length == parentDetCode.length+5){ 
			ul.append(getctreeli(list[i],list[i].bomId,list[i].detCode,width));
			if(list[i].childBomId != "" && list[i].isOpen=="1"){ //且存在子bom，自动展开
				
				recShowChild($("li[detCode="+list[i].detCode+"]"),list[i].detCode,list);
				$("li[detCode="+list[i].detCode+"]").find(">ul").show();
				//屏蔽半成品的用量信息
				if(list[i].attrType==3){
					$("li[detCode="+list[i].detCode+"]").find(">a span[name=totalQty]").text("");
					$("li[detCode="+list[i].detCode+"]").find(">a span[name=totalLoss]").text("");
					$("li[detCode="+list[i].detCode+"]").find(">a span[name=ProduceQty]").text("");
				}
			}
		}
	}
}
function changeBom(){
	var list = new Array();
	#foreach($row in $bomList)
	list[list.length]={'GoodsCode':'$row.get("GoodsCode")','GoodsNumber':'$row.get("GoodsNumber")','GoodsFullName':'$row.get("GoodsFullName")',
		'GoodsSpec':'$row.get("GoodsSpec")','BaseUnit':'$row.get("BaseUnit")','bomId':'$row.get("BomId")','bomDetId':'$row.get("BomDetId")',
		'childBomId':'$row.get("childBomId")',
		'qty':'$globals.newFormatNumber($row.get("qty"),false,false,true,"PDMRPBill","qty",true)',
		'lossRate':'$globals.newFormatNumber($row.get("lossRate"),false,false,true,"PDMRPBill","qty",true)',
		'totalQty':'$globals.newFormatNumber($row.get("totalQty"),false,false,true,"PDMRPBill","qty",true)',
		'totalLoss':'$globals.newFormatNumber($row.get("totalLoss"),false,false,true,"PDMRPBill","qty",true)',
		'ProduceQty':'$globals.newFormatNumber($row.get("ProduceQty"),false,false,true,"PDMRPBill","qty",true)',
		'attrType':'$row.get("attrType")','clientSupper':'$row.get("clientSupper")','dtype':'$row.get("dtype")','isOpen':'$row.get("isOpen")','detCode':'$row.get("detCode")'};
	#end
	
	recShowChild($("#treeDemo_1"),'',list);
	
	$('li[treenode=""]').each(function(){
		setIcon($(this));
	});
	setNo($("#treeDemo_1_ul"));
}

function toBuy(){
	var buyCount = 0;
	jQuery.ajax({url:"/Mrp.do?type=hasBuy",data:"keyId="+$("#keyId").val(),success:function(json){ 
		if(json.code == "OK"){
			buyCount =json.count;
		}
		
	},dataType:"json",async:false }); 
	
	if(buyCount > 0){
		alert("已经下请购单不允许再重复请购");
		return;
		//if(!confirm("您已经下过请购单了，确定要再次下达请购吗？")){
		//	return;
		//}
	}else{
		if(!confirm("确定要下达请购吗？")){
			return;
		}
	}
	
	if(typeof(top.jblockUI)!="undefined" ){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	jQuery.post("/Mrp.do?type=toBuy",{keyId:$("#keyId").val()},function(json){ 
		if(json.code == "ERROR"){
			if(typeof(top.jblockUI)!="undefined" ){
				top.junblockUI()
			}
			alert(json.msg);
			return;
		}else{
			if(typeof(top.jblockUI)!="undefined" ){
				top.junblockUI()
			}
			alert("请购单已经成功下达，别忘了过帐提交审核哦！");
			detail("tblBuyApplication",json.keyId);
			
		}
	},"json" ); 
}

function detail(tableName,keyId){
	
	turl="/UserFunctionAction.do?tableName="+tableName+"&keyId="+keyId+"&operation=$globals.getOP("OP_DETAIL")";
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	openPop('PopMainOpdiv','',turl,width,height,false,height==document.documentElement.clientHeight);  
	
}
function buyList(){
	turl="/UserFunctionQueryAction.do?tableName=tblBuyApplication&PDMRPId="+$("#keyId").val();
	width=document.documentElement.clientWidth-100;
	height=document.documentElement.clientHeight-50;
	openPop('PopMainOpdiv','',turl,width,height,false,height==document.documentElement.clientHeight);  
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
<body style="overflow:hidden" onload="changeConfirm();">

<div id="Larger_content">
<iframe   name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" id="form"  name="form" action="/Mrp.do?type=selBom"  target="">
<input type="hidden" id="keyId" value="$!goodsObj.get("mrpId")">

<div class="Heading">
	<div class="HeadingTitle">
		<b class="icons b-list"></b>
		生产规划详情
	</div>
	<ul class="HeadingButton f-head-u">		
		<li><span class="btn btn-small" id="toGoods" name="toGoods" onClick="closeWindows('PopMainOpdiv');" >返回</span></li>
	</ul>
</div>
<div id="listRange_id" style="position: relative; height: 210px;">
	<div class="wrapInside">
		<div class="listRange_1" id="listRange_mainField">
			<ul class="wp_ul">
			    <li>
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
			    <li>
				    <div class="swa_c1 d_6" style="color:#c0c0c0;" title="料件名称">
					    <div class="d_box">
						    <div class="d_test d_mid">料件名称</div>
					    </div>
					    <div class="d_mh">&nbsp;</div>
			    	</div>
			    	<div class="swa_c2">
			    		<input id="tblGoods_GoodsFullName" name="tblGoods_GoodsFullName" value="$!goodsObj.get("GoodsFullName")" type="text"  readonly="readonly" disableautocomplete="true" autocomplete="off" >
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
				    	<input id="bomId" name="bomId" type="hidden" value="$!goodsObj.get("bomId")" >
				    	<input id="bomIdVersion" name="bomIdVersion" type="text" value="$!goodsObj.get("version")" readonly="readonly"  onfocus="mainFocus(this);" onkeydown="if(event.keyCode==13){tabObj.next(this);}" disableautocomplete="true" autocomplete="off">
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
				    	<input id="StockQty" name="StockQty" value="$globals.newFormatNumber($!goodsObj.get("StockQty"),false,true,true,"PDMRP","StockQty",true)" type="text" readonly="readonly"  disableautocomplete="true" autocomplete="off" >
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
				    	<input id="OrderQty" name="OrderQty" value="$globals.newFormatNumber($!goodsObj.get("OrderQty"),false,true,true,"PDMRP","StockQty",true)" type="text" readonly="readonly"  disableautocomplete="true" autocomplete="off" >
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
				    	<input id="ProduceQty" name="ProduceQty" value="$globals.newFormatNumber($!goodsObj.get("ProduceQty"),false,true,true,"PDMRP","StockQty",true)" type="text" readonly="readonly"  disableautocomplete="true" autocomplete="off" >
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
				    	<input id="PossesQty" name="PossesQty" value="$globals.newFormatNumber($!goodsObj.get("PossesQty"),false,true,true,"PDMRP","StockQty",true)" type="text" readonly="readonly"  disableautocomplete="true" autocomplete="off" >
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
				    	<input id="totalQty" name="totalQty" value="$globals.newFormatNumber($!goodsObj.get("totalQty"),false,true,true,"PDMRP","StockQty",true)" type="text" readonly="readonly"  disableautocomplete="true" autocomplete="off" >
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
			    <li>
				    <div class="swa_c1 d_6"style="color:#c0c0c0;" title="计划开始生产">
					    <div class="d_box">
					    	<div class="d_test d_mid">计划开始生产</div>
					    </div>
					    <div class="d_mh">&nbsp;</div>
				    </div>
				    <div class="swa_c2">
				    	<input id="needQty" name="startWork" type="text" value="$!goodsObj.get("startWork")" readonly="readonly"  onfocus="mainFocus(this);" onkeydown="if(event.keyCode==13){tabObj.next(this);}" disableautocomplete="true" autocomplete="off">
				    </div>
			    </li>
			    <li>
				    <div class="swa_c1 d_6"style="color:#c0c0c0;" title="计划生产完工">
					    <div class="d_box">
					    	<div class="d_test d_mid">计划生产完工</div>
					    </div>
					    <div class="d_mh">&nbsp;</div>
				    </div>
				    <div class="swa_c2">
				    	<input id="needQty" name="endWork" type="text" value="$!goodsObj.get("endWork")" readonly="readonly"  onfocus="mainFocus(this);" onkeydown="if(event.keyCode==13){tabObj.next(this);}" disableautocomplete="true" autocomplete="off">
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

		<div class="scroll_function_small_ud" id="conter" style="height: 366px; overflow: auto; width: 1329px;">
				<div style="height: 366px; overflow: auto; float: left; width: 1160px; padding-top: 0px; display: block;" id="trees">
				<div class="zTreeDemoBackground">
					<ul id="treeDemo" class="tree" style="width:1101px;padding-top:0px;">
						<li id="treeDemo_1" treenode="">
							<button type="button" id="treeDemo_1_switch" style="float:left" title="" class="switch_root_open" treenode_switch="" onfocus="this.blur();"></button>
							<a id="treeDemo_1_a" treenode_a="" onclick="" target="_blank" style="float:left;width:1082px;background-image: -webkit-linear-gradient(top,#5fa3e7,#428bca); background-image: linear-gradient(top,#5fa3e7,#428bca);" class="">
								<button type="button" id="treeDemo_1_ico" style="float:left" title="" treenode_ico="" onfocus="this.blur();" class=" ico_open" style=""></button>
								<span id="treeDemo_1_span" style="width:1063px;" class="treeDemoSpan">
									<div class="bomDet">
										<ul>
											<li><span style="width:120px">料件编号</span></li>
											<li><span style="width:140px">料件名称</span></li>
											<li><span style="width:180px">规格</span></li>
											<li><span style="width:40px">来源码</span></li>
											<li><span style="width:50px">单位用量</span></li>
											<li><span style="width:40px">损耗%</span></li>		
											<li><span style="width:70px">需求量</span></li>
											<li><span style="width:60px">备品量</span></li>
											<li><span style="width:50px">客供包料</span></li>
											<li><span style="width:90px">类型</span></li>
											<li><span style="width:60px">规划数量</span></li>
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
				<td width="50">紧急程度</td>
				<td width="80">单据总数量</td>
				<td width="80">已规划数量</td>
				<td width="80">本次规划量</td>
			</tr>
		</thead>
	</table>
	</div>
	<div id="k_data" style="z-index: 40; width: 1040px;overflow-x:hidden; overflow-y: auto; position: absolute; top: 0px; left: 0px; height: 326px;">
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
				<td width="50">紧急程度</td>
				<td width="80">单据总数量</td>
				<td width="80">已规划数量</td>
				<td width="80">本次规划量</td>
			</tr>
		</thead>
		<tbody>
			#foreach($row in $salesList)
			<tr height="28px" name="salesList" billId="$row.get("billId")" BillType="$row.get("BillType")" BillNo="$row.get("BillNo")" 
				BillDate="$row.get("BillDate")" DepartmentCode="$row.get("DepartmentCode")" EmployeeId="$row.get("EmployeeId")" 
				CompanyCode="$row.get("CompanyCode")" createBy="$row.get("createBy")" 
				qty="$globals.newFormatNumber($row.get("qty"),false,false,true,"PDMRPBill","qty",true)"  onclick="gselectKeyId(this)" class="spaceRow">
				<td>$velocityCount</td>
				<td align="left">#if($row.get("BillType")=="PDProduceRequire")制造需求单#else 销售订单 #end</td>
				<td align="left"><span style="color:blue;cursor:pointer" onclick="detail('$row.get("BillType")','$row.get("billId")')">$row.get("BillNo")</span> </td>
				<td align="center">$row.get("BillDate")</td>
				#if($!globals.getSysSetting("productByOrder")=="true")
				<td align="left">$row.get("ComNumber")</td>
				<td align="left">$row.get("ComName")</td>
				#end
				#set($uv = $row.get("Urgency")) 
				<td align="center">$!globals.getEnumerationItemsDisplay('Urgency',"$uv")</td>
				<td align="right">$globals.newFormatNumber($row.get("TotalQty"),false,false,true,"PDMRPBill","TotalQty",true)</td>
				<td align="right">$globals.newFormatNumber($row.get("MRPQty"),false,false,true,"PDMRPBill","MRPQty",true)</td>
				<td align="right">$globals.newFormatNumber($row.get("qty"),false,false,true,"PDMRPBill","qty",true)</td>
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
<p>合并BOM：半成品不会产生工令单，发料时直接发半成品的下级物料，缴库时直接缴入成品免去中间半成品的缴库、出库、成本核算等过程。否则半成品产生一张独立的工令单，半成品需先缴库后再作为原料做发料单给上级工令单。如果选择不合并BOM，可以修改半成品工令的生产数量</p>
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
