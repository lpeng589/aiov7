<link type="text/css" rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" />
<script language="javascript">


function setIcon(obj){ 
	if(obj.find("ul[id*='treeDemo'] li").size()>0){
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


function selectItem(obj){
	$(".selectdBomDet").removeClass("selectdBomDet");
	$(obj).find("div").addClass("selectdBomDet");
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
function getctreeli(row,width){
	//找出最大的li编号
	var maxli=1;
	$("li[id*=treeDemo]").each(function(){
		lid = Number($(this).attr("id").substring(9));
		if(lid>maxli){maxli=lid;}
	});
	var attrType = row.get('tblGoods_attrType');
	var clas = row.get('GoodsCode');
	if(attrType==0)
		attrType='M自制';
	else if(attrType==1)
		attrType='P采购';
	else if(attrType==2)
		attrType='S委外';
	else if(attrType==3)
		attrType='X虚拟';
	else if(attrType==4)
		attrType='Z杂项';
	maxli++;
	
	var isWork = false;
	if(row.get('isOpen')==1 && row.get('ismaterial')==1){
		isWork=true;
	}
	
	return ' \
	<li id="treeDemo_'+maxli+'" good="'+clas+'" treenode="">\
		<button type="button"  style="float:left" id="treeDemo_'+maxli+'_switch" title="" onclick="clicksf(this)"  class="switch_bottom_docu" treenode_switch="" onfocus="this.blur();"></button>\
		<a id="treeDemo_'+maxli+'_a" treenode_a=""  target="_blank" style="float:left;width:'+width+'px;background: #DEE3E8; background-image: -webkit-linear-gradient(top,#DEE3E8,#E2EEF9); background-image: linear-gradient(top,#DEE3E8,#E2EEF9);">\
			<button type="button"  style="float:left" id="treeDemo_'+maxli+'_ico" title="" treenode_ico="" onfocus="this.blur();" class=" ico_open" style=""></button>\
			<span id="treeDemo_'+maxli+'_span" style="width:'+(width-19)+'px;"  class="treeDemoSpan">\
				<div class="bomDet">\
					<span name="tno"></span>\
					<ul>\
						<input type="hidden" name="PDCostEstimateDet_GoodsCode" value="'+row.get('GoodsCode')+'"/>\
						<input type="hidden" name="PDCostEstimateDet_attrType" value="'+row.get('attrType')+'"/>\
						<input type="hidden" name="PDCostEstimateDet_Qty" value="'+row.get('Qty')+'"/>\
						<input type="hidden" name="PDCostEstimateDet_Amount" value="'+row.get('Amount')+'"/>\
						<input type="hidden" name="PDCostEstimateDet_WorkAmount" value="'+row.get('WorkAmount')+'"/>\
						<input type="hidden" name="PDCostEstimateDet_isOpen" value="'+row.get('isOpen')+'"/>\
						<input type="hidden" name="PDCostEstimateDet_detCode" value="'+row.get('detCode')+'"/>\
						<input type="hidden" name="PDCostEstimateDet_ismaterial" value="'+row.get('ismaterial')+'"/>\
						<li><span style="width:120px;text-align:left;color:#000">'+row.get('tblGoods_GoodsNumber')+'</span></li>\
						<li><span style="width:180px;text-align:left;color:#000">'+row.get('tblGoods_GoodsFullName')+'</span></li>\
						<li><span style="width:330px;text-align:left;color:#000">'+row.get('tblGoods_GoodsSpec')+'</span></li>\
						<li><span style="width:40px;text-align:left;color:#000">'+attrType+'</span></li>\
						<li><span style="width:50px;text-align:left;color:#000">'+row.get('tblGoods_BaseUnit')+'</span></li>\
						<li><span style="width:50px;text-align: left; color: #000;">'+row.get('Qty')+'</span></li>\
						<li><span style="width:70px;text-align: left; color: #000;" val='+row.get('Price')+' name="cd_Price"><input name=PDCostEstimateDet_Price onchange=changePrice(this) style="'+ (isWork?'display:none; ':'') +'background-color: #fff;"  value="'+row.get('Price')+'"/></span></li>\
						<li><span style="width:50px;text-align: left; color: #000;" val='+row.get('WorkTime')+' name="cd_WorkTime"><input name="PDCostEstimateDet_WorkTime" onchange=changeWorkTime(this)  style="'+ (isWork?'':'display:none;') +'background-color: #fff;" value="'+row.get('WorkTime')+'"/></span></li>\
						<li><span style="width:70px;text-align: left; color: #000;" val='+ (isWork? row.get('WorkAmount'):row.get('Amount')) +' name="cd_Amount">'+ (isWork? row.get('WorkAmount'):row.get('Amount')) +'</span></li>\
					</ul>\
				</div>\
			</span>\
		</a>\
		<ul id="treeDemo_'+maxli+'_ul" class="line" style="display:none"></ul>\
		<div style="clear:both"></div>\
	</li>\
	';
}
var priceDigist = 6;
function changePrice(obj){
	ul = $(obj).parent().parent().parent();
	ul.find("span[name=cd_Price]").attr("val",$(obj).val());
	var amount = f($(obj).val()*ul.find("input[name=PDCostEstimateDet_Qty]").val(),priceDigist);
	ul.find("input[name=PDCostEstimateDet_Amount]").val(amount);
	if(!(ul.find("input[name=PDCostEstimateDet_isOpen]").val()==1 && ul.find("input[name=PDCostEstimateDet_ismaterial]").val()==1 )){
		ul.find("span[name=cd_Amount]").attr("val",amount);
		ul.find("span[name=cd_Amount]").text(amount);
	}
	sumamount();
}
function changeWorkTime(obj){
	ul = $(obj).parent().parent().parent();
	ul.find("span[name=cd_WorkTime]").attr("val",$(obj).val());
	var amount = f(($(obj).val()*ul.find("input[name=PDCostEstimateDet_Qty]").val()/3600)*$("#WorkRate").val(),priceDigist);
	ul.find("input[name=PDCostEstimateDet_WorkAmount]").val(amount);
	if((ul.find("input[name=PDCostEstimateDet_isOpen]").val()==1 && ul.find("input[name=PDCostEstimateDet_ismaterial]").val()==1 )){
		ul.find("span[name=cd_Amount]").attr("val",amount);
		ul.find("span[name=cd_Amount]").text(amount);
	}
	sumamount();
}

var materialCost=0;
var assembleCost=0
function sumamount(){
	materialCost=0;
	assembleCost=0;
	recsumamount($("ul[id=treeDemo_1_ul]"));
	
	$("#materialCost").val(f(materialCost,priceDigist));
	$("#assembleCost").val(f(assembleCost,priceDigist));
	
	$("#saleRate").trigger("change");
	
}

function recsumamount(ul){
	if(ul.css("display") == 'none')
		return;
	
	ul.find(">li").each(function(){
		if($(this).find(">a").find("input[name=PDCostEstimateDet_isOpen]").size()>0){
			if($(this).find(">a").find("input[name=PDCostEstimateDet_isOpen]").val()==1 && $(this).find(">a").find("input[name=PDCostEstimateDet_ismaterial]").val()==1 ){
				assembleCost += Number( $(this).find(">a").find("input[name=PDCostEstimateDet_WorkAmount]").val());
			}else{
				materialCost += Number($(this).find(">a").find("input[name=PDCostEstimateDet_Amount]").val());
			}
		}
		recsumamount($(this).find(">ul[id*=treeDemo]"));
	});
	
}
function opentree(a,isOpen){
	var ul = a.find("ul");
	if(isOpen){
		ul.find("input[name=PDCostEstimateDet_isOpen]").val(1);
		ul.find("input[name=PDCostEstimateDet_Price]").hide();
		ul.find("input[name=PDCostEstimateDet_WorkTime]").show();
		
		ul.find("span[name=cd_Amount]").text(ul.find("input[name=PDCostEstimateDet_WorkAmount]").val());
	}else{
		ul.find("input[name=PDCostEstimateDet_isOpen]").val(0);
		ul.find("input[name=PDCostEstimateDet_Price]").show();
		ul.find("input[name=PDCostEstimateDet_WorkTime]").hide();
		
		ul.find("span[name=cd_Amount]").text(ul.find("input[name=PDCostEstimateDet_Amount]").val());
	}
	sumamount();
}

function clicksf(obj){
	id = $(obj).attr("id");
	id = id.substring(0,id.length - 7 );
	$("#"+id+"_ul").toggle();
	if($(obj).attr("class").indexOf("switch_bottom_close") > -1){
		$(obj).removeClass("switch_bottom_close");
		$(obj).addClass("switch_bottom_open");
		opentree($(obj).parent().find(">a"),true);
	}else{
		$(obj).removeClass("switch_bottom_open");
		$(obj).addClass("switch_bottom_close");
		opentree($(obj).parent().find(">a"),false);
	}
}
function recShowChild(li,list,parCode){
	var ul = $("#treeDemo_1_ul");
	var width=0;
	if(parCode.length >0){
		var id = li.attr("id");
		var ul = $("#"+id+"_ul");
		var width=li.find(">a").css("width");
		width = width.substring(0,width.length-2);
		width = Number(width)-18;
	}else{
		width=1154;
	}
	
	
	for(var i=0;i<list.length;i++){
		row = list[i];
		if(row.get("detCode").indexOf(parCode)==0 && row.get("detCode").length==parCode.length+5 ){
			ul.append(getctreeli(row,width));
			if(row.get("ismaterial") == 1){
				recShowChild(ul.find(">li[good="+row.get("GoodsCode")+"]") ,list,row.get("detCode"));
			}
		}
	}
	if(ul.parent().find(">a").find("input[name=PDCostEstimateDet_isOpen]").val() == 1 && ul.parent().find(">a").find("input[name=PDCostEstimateDet_ismaterial]").val()==1){
		ul.show();
	}
}



jQuery(document).ready(function(){
	var detData = eval($('#tableName').val()+'DetData');
	
	recShowChild(null,detData.rows,'');
	
	$("a[href*=batchInput]").remove();
	$("a[href*=calc]").remove();
	if($("#toolMore a").size()==0){
		$("#toolMore").parent().parent().remove();
	}
	
	$('li[treenode=""]').each(function(){
		setIcon($(this));
	});
	setNo($("#treeDemo_1_ul"));
	var tnn = $("#tableName").val();
	
	$("#trees").css("height","auto");
	
	jQuery("#listRange_tableInfo2").css("max-width",(document.body.clientWidth-20)+"px");
	$("#listRange_tableInfo2 div").css("max-width","1200px");
	changeConfirm();
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

		<div class="showGridTags">
			
			<div class="scroll_function_small_ud" id="listRange_tableInfo2" style="overflow-y:none;overflow-x:auto;height: auto;width: 1200px;">
			
			<div style="height: auto;overflow:auto;float:left;width:1200px;padding-top: 0px;" id="trees">
				<div class="zTreeDemoBackground">
					<ul id="treeDemo" class="tree" style="padding-top:0px;">
						<li id="treeDemo_1" treenode="">
							<button type="button" id="treeDemo_1_switch" style="float:left" title="" class="switch_root_open" treenode_switch="" onfocus="this.blur();"></button>
							<a id="treeDemo_1_a" treenode_a="" onclick="" target="_blank" style="float:left;width:1172px;background: #5fa3e7; background-image: -webkit-linear-gradient(top,#5fa3e7,#428bca); background-image: linear-gradient(top,#5fa3e7,#428bca);" class="">
								<button type="button" id="treeDemo_1_ico"  style="float:left" title="" treenode_ico="" onfocus="this.blur();" class=" ico_open" style=""></button>
								<span id="treeDemo_1_span" style="width:1153px;" class="treeDemoSpan">
									<div class="bomDet">
										<ul>
											<li><span style="width:120px">物料编号</span></li>
											<li><span style="width:180px">名称</span></li>
											<li><span style="width:330px">规格</span></li>
											<li><span style="width:40px">来源</span></li>
											<li><span style="width:50px">单位</span></li>
											<li><span style="width:50px">数量</span></li>
											<li><span style="width:70px">成本</span></li>
											<li><span style="width:50px">标准工时</span></li>
											<li><span style="width:70px">金额</span></li>
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
				
			</div>	
		</div> <!-- 明细图片区 -->
		
	<script type="text/javascript"> 
		var sHeight2=document.documentElement.clientHeight- 30;
		
		var mainFieldH  =m("listRange_mainField").offsetHeight;
		var tabH = sHeight2-mainFieldH-250;
		if(tabH<150) tabH=150;
		//$("#listRange_tableInfo").height(tabH);		
		
</script>