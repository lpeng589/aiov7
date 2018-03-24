<link type="text/css" rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" />
<script language="javascript">

function addDet(field){
	var tnn = $("#tableName").val();
	var urls = "/UserFunctionAction.do?tableName="+tnn+"&selectName=PDBomDetGoods&operation=22&MOID="+MOID+"&MOOP=add&popupWin=Popdiv";
	asyncbox.open({id:'Popdiv',title:'物料弹出框',url:urls,width:750,height:470});
	
}
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

//自定义弹出框回填函数
function exePopdiv(returnValue){
	var strs = returnValue.split("#|#");
	var gc= "";
	for(var i=0;i<strs.length;i++){
		if(strs[i]!="" && typeof(strs[i])!="undefined"){
			var clas = strs[i].split("#;#");
			gc += clas+",";
			$("#treeDemo_1_ul").append(gettreeli(clas[1],clas[2],clas[3],clas[4],clas[5],clas[0],'0','0','2',''));
		}
		
	} 	
	if(typeof(top.jblockUI)!="undefined" && submitBefore){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}	
	jQuery.getJSON("/BomAction.do?type=getChild",
	       {goods:gc},
	       function(json){
				for(obj in json){
					if(json[obj].length > 0){
						recShowChild($("#treeDemo_1_ul > li[good="+obj+"]"),json[obj]);
					}
				}
				$('li[treenode=""]').each(function(){
					setIcon($(this));
				});
				setNo($("#treeDemo_1_ul"));
				var tnn = $("#tableName").val();
				$("input[name="+tnn+"Det_qty]").trigger("change"); 
				$("input[name="+tnn+"Det_lossRate]").trigger("change"); 
				if(typeof(top.jblockUI)!="undefined" ){
					top.junblockUI()
				}
	       } 
	); 
	
}
function selectItem(obj){
	$(".selectdBomDet").removeClass("selectdBomDet");
	$(obj).find("div").addClass("selectdBomDet");
}

function delDet(){
	$(".selectdBomDet").parent().parent().parent().remove();
	setNo($("#treeDemo_1_ul"));
}
function changeQty(obj){
	var li = $(obj).parents("li[good]")
	var cli = li.find("> ul >li").each(function(){
		var span = $(this).find(">a").find('span[name="cd_qty"]');
		var val = span.attr("val")*obj.value;
		var num = Number(val.toFixed(8));
		span.text(num);
		if($(this).find(">ul >li").size() > 0){
			recQty(num,$(this).find(">ul >li"));
		}
	});
}
function recQty(num,lis){
	lis.each(function(){
		var span = $(this).find(">a").find('span[name="cd_qty"]');
		var val = span.attr("val")*num;
		var fnum = Number(val.toFixed(8));
		span.text(fnum);
		if($(this).find(">ul >li").size() > 0){
			recQty(fnum,$(this).find(">ul >li"));
		}
	});
}
function changeLoss(obj){
	var li = $(obj).parents("li[good]")
	var cli = li.find("> ul >li").each(function(){
		var span = $(this).find(">a").find('span[name="cd_lossRate"]');
		var val = Number(span.attr("val"))+Number(obj.value);
		var num = Number(val.toFixed(8));
		span.text(num);
		if($(this).find(">ul >li").size() > 0){
			recLoss(num,$(this).find(">ul >li"));
		}
	});
}
function recLoss(num,lis){
	lis.each(function(){
		var span = $(this).find(">a").find('span[name="cd_lossRate"]');
		var val = Number(span.attr("val"))+num;
		var fnum = Number(val.toFixed(8));
		span.text(fnum);
		if($(this).find(">ul >li").size() > 0){
			recLoss(fnum,$(this).find(">ul >li"));
		}
	});
}
function viewReplace(goodscode){
	
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	
	value= "/UserFunctionAction.do?tableName=PDGoodsReplace&operation=88&fieldName=GoodsCode&VAL="+goodscode+"&defaultValue=true&GoodsCode="+goodscode+"&LinkType=@URL:&src=menu";
	
	openPop('PopTooldiv','替换料',value,width,height,true,true);
}

function gettreeli(GoodsNumber,GoodsFullName,GoodsSpec,BaseUnit,attrType,clas,qty,lossRate,canSuper,remark){
	
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
	//找出最大的li编号
	var maxli=1;
	$("li[id*=treeDemo]").each(function(){
		lid = Number($(this).attr("id").substring(9));
		if(lid>maxli){maxli=lid;}
	});
	maxli++;
	var tnn = $("#tableName").val();
	return ' \
	<li id="treeDemo_'+maxli+'" good="'+clas+'" treenode="">\
		<button type="button"  style="float:left" id="treeDemo_'+maxli+'_switch" onclick="clicksf(this)" title="" class="switch_bottom_docu" treenode_switch="" onfocus="this.blur();"></button>\
		<a id="treeDemo_'+maxli+'_a" treenode_a="" onclick="selectItem(this)" target="_blank" style="float:left;width:1154px;">\
			<button type="button"  style="float:left" id="treeDemo_'+maxli+'_ico" title="" treenode_ico="" onfocus="this.blur();" class=" ico_open" style=""></button>\
			<span id="treeDemo_'+maxli+'_span" style="width:1135px;"  class="treeDemoSpan">\
				<div class="bomDet">\
					<span name="tno"></span>\
					<input name="'+tnn+'Det_GoodsCode" value="'+clas+'" type="hidden" />\
					<ul>\
						<li><span style="width:120px;text-align:left;color:#000">'+GoodsNumber+'</span></li>\
						<li><span style="width:110px;text-align:left;color:#000">'+GoodsFullName+'</span></li>\
						<li><span style="width:350px;text-align:left;color:#000">'+GoodsSpec+'</span></li>\
						<li><span style="width:40px;text-align:left;color:#000">'+attrType+'</span></li>\
						<li><span style="width:50px;text-align:left;color:#000">'+BaseUnit+'</span></li>\
						<li><span style="width:50px"><input list="true"  name="'+tnn+'Det_qty" style="width:80px" type="text"  value="'+qty+'" onchange="changeQty(this)" maxlength="1000000" size="1"  disableautocomplete="true" autocomplete="off"></span></li>\
						<li><span style="width:50px"><input list="true"  name="'+tnn+'Det_lossRate" style="width:50px" type="text"  onchange="changeLoss(this)"  value="'+lossRate+'" maxlength="1000000" size="1"  disableautocomplete="true" autocomplete="off"></span></li>\
						<li><span style="width:50px"><select list="true"  name="'+tnn+'Det_canSuper" style="width:50px;border: none;" ><option value=2 '+(canSuper !='1'?'selected':'')+'>否</option><option value=1 '+(canSuper =='1'?'selected':'')+'>是</option></select></span></li>\
						<li><span style="width:140px"><input list="true"  name="'+tnn+'Det_remark" style="width:200px" type="text"  value="'+remark+'" maxlength="1000000" size="1"  disableautocomplete="true" autocomplete="off"></span></li>\
						<li><span style="width:50px;color: #000;cursor: pointer;" onclick="viewReplace(\''+clas+'\')">查看</span></li>\
					</ul>\
				</div>\
			</span>\
		</a>\
		<ul id="treeDemo_'+maxli+'_ul" class="line" style="display:none"></ul>\
		<div style="clear:both"></div>\
	</li>\
	';
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
function getctreeli(GoodsNumber,GoodsFullName,GoodsSpec,BaseUnit,attrType,clas,qty,lossRate,canSuper,remark,width){
	//找出最大的li编号
	var maxli=1;
	$("li[id*=treeDemo]").each(function(){
		lid = Number($(this).attr("id").substring(9));
		if(lid>maxli){maxli=lid;}
	});
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
	return ' \
	<li id="treeDemo_'+maxli+'" good="'+clas+'" treenode="">\
		<button type="button"  style="float:left" id="treeDemo_'+maxli+'_switch" title="" onclick="clicksf(this)"  class="switch_bottom_docu" treenode_switch="" onfocus="this.blur();"></button>\
		<a id="treeDemo_'+maxli+'_a" treenode_a=""  target="_blank" style="float:left;width:'+width+'px;background: #DEE3E8; background-image: -webkit-linear-gradient(top,#DEE3E8,#E2EEF9); background-image: linear-gradient(top,#DEE3E8,#E2EEF9);">\
			<button type="button"  style="float:left" id="treeDemo_'+maxli+'_ico" title="" treenode_ico="" onfocus="this.blur();" class=" ico_open" style=""></button>\
			<span id="treeDemo_'+maxli+'_span" style="width:'+(width-19)+'px;"  class="treeDemoSpan">\
				<div class="bomDet">\
					<span name="tno"></span>\
					<ul>\
						<li><span style="width:120px;text-align:left;color:#000">'+GoodsNumber+'</span></li>\
						<li><span style="width:110px;text-align:left;color:#000">'+GoodsFullName+'</span></li>\
						<li><span style="width:350px;text-align:left;color:#000">'+GoodsSpec+'</span></li>\
						<li><span style="width:40px;text-align:left;color:#000">'+attrType+'</span></li>\
						<li><span style="width:50px;text-align:left;color:#000">'+BaseUnit+'</span></li>\
						<li><span style="width:50px;text-align: left; color: #000;" val='+qty+' name="cd_qty">'+qty+'</span></li>\
						<li><span style="width:50px;text-align: left; color: #000;" val='+lossRate+' name="cd_lossRate">'+lossRate+'</span></li>\
						<li><span style="width:50px;text-align: left; color: #000;" val='+canSuper+' name="cd_canSuper">&nbsp;'+(canSuper=='1'?'是':'否')+'</span></li>\
						<li><span style="width:140px;text-align: left; color: #000;" val='+remark+' name="cd_remark">'+remark+'</span></li>\
						<li><span style="width:50px; color: #000;cursor: pointer;"  onclick="viewReplace(\''+clas+'\')">查看</span></li>\
					</ul>\
				</div>\
			</span>\
		</a>\
		<ul id="treeDemo_'+maxli+'_ul" class="line" style="display:none"></ul>\
		<div style="clear:both"></div>\
	</li>\
	';
}
function clicksf(obj){
	id = $(obj).attr("id");
	id = id.substring(0,id.length - 7 );
	$("#"+id+"_ul").toggle();
	if($(obj).attr("class").indexOf("switch_bottom_close") > -1){
		$(obj).removeClass("switch_bottom_close");
		$(obj).addClass("switch_bottom_open");
	}else{
		$(obj).removeClass("switch_bottom_open");
		$(obj).addClass("switch_bottom_close");
	}
	

}
function recShowChild(li,list){

	var id = li.attr("id");
	
	var ul = $("#"+id+"_ul");
	
	var width=li.find(">a").css("width");
	width = width.substring(0,width.length-2);
	width = Number(width)-18;
	for(var i=0;i<list.length;i++){
		ul.append(getctreeli(list[i].GoodsNumber,list[i].GoodsFullName,list[i].GoodsSpec,list[i].BaseUnit,list[i].attrType,list[i].GoodsCode,list[i].qty,list[i].lossRate,list[i].canSuper,list[i].remark,width));
		if(list[i].childs.length > 0){
			recShowChild(ul.find(">li[good="+list[i].GoodsCode+"]") ,list[i].childs);
		}
	}
}
function convertToProduct(){
	if(confirm("确定将本BOM及其所有子级BOM转为生产BOM？ ")){
		jQuery.get("/BomAction.do?type=convert&goods="+$("#GoodsCode").val(),
		       {},
		       function(obj){
					alert(obj);
		       } 
		       
		); 
	}
}

function fastInput(obj){
	var bsvalue = $(obj).val();
	if(event.keyCode==13 && bsvalue != ""){ 
		var tableName = $("#tableName").val();
		var param = {
		operation:"DropdownPopup",
		MOID:MOID,
		MOOP:"add",
		selectField:'',
		selectValue:bsvalue,
		tableName:tableName,
		fieldName:'GoodsCode',
		CompanyCode:''
		};

		//取值
		param.operation="DropdownPopup";	
		var btstr = jQuery.ajax({url: "/UtilServlet",
	 					async: false,
	 					type: "POST",
	 					data:   param
					}).responseText; 
		var retDataObj = eval("("+btstr+")");	
		if(retDataObj.length==0) {
			alert("找不到"+bsvalue+"对应的数据");
			return false;
		}
		retDataObj[0][1] = retDataObj[0][1].replace("&apos;","'");
		retDataObj[0][1] = retDataObj[0][1].replace("&quot;",'"');
		retDataObj[0][1] = retDataObj[0][1].replace("&#92;","\\");
		
		exePopdiv(retDataObj[0][1]);

		$(obj).val("");
		stopBubble(event);
	}
}


jQuery(document).ready(function(){
	var gc = "";
	var detData = eval($('#tableName').val()+'DetData');
	
	for(i=0;i<detData.rows.length;i++){
		row = detData.rows[i];
		$("#treeDemo_1_ul").append(gettreeli(row.get('tblGoods_GoodsNumber'),row.get('tblGoods_GoodsFullName'),row.get('tblGoods_GoodsSpec'),row.get('tblGoods_BaseUnit'),row.get('tblGoods_attrType'),row.get('GoodsCode'),row.get('qty'),row.get('lossRate'),row.get('canSuper'),row.get('remark')));
		gc += row.get('GoodsCode')+",";
	}
	if(typeof(top.jblockUI)!="undefined" && submitBefore){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}	
	$("a[href*=batchInput]").remove();
	$("a[href*=calc]").remove();
	if($("#toolMore a").size()==0){
		$("#toolMore").parent().parent().remove();
	}
	if($("#workHours").attr("readOnly")=="readonly" || workFlowNode=='-1'){
		$("#wlOpbt").hide();
		$("input[name=PDBomDet_qty]").attr("readOnly","readOnly");
		$("input[name=PDBomDet_qty]").css("cursor","not-allowed");
		$("input[name=PDBomDet_lossRate]").attr("readOnly","readOnly");
		$("input[name=PDBomDet_lossRate]").css("cursor","not-allowed");
	}
	
	jQuery.getJSON("/BomAction.do?type=getChild",
	       {goods:gc},
	       function(json){
				for(obj in json){
					if(json[obj].length > 0){
						recShowChild($("#treeDemo_1_ul > li[good="+obj+"]"),json[obj]);
					}
				}
				$('li[treenode=""]').each(function(){
					setIcon($(this));
				});
				setNo($("#treeDemo_1_ul"));
				var tnn = $("#tableName").val();
				$("input[name="+tnn+"Det_qty]").trigger("change"); 
				$("input[name="+tnn+"Det_lossRate]").trigger("change"); 
				if(typeof(top.jblockUI)!="undefined" ){
					top.junblockUI()
				}
				$("#trees").css("height","auto");
				$("#listRange_tableInfo div").css("max-width","1200px");
				jQuery("#listRange_tableInfo").css("max-width",(document.body.clientWidth-20)+"px");
				changeConfirm();

	       } 
	       
	); 
	
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
			<span id="wlOpbt" style="margin: 5px 0 0 25px;    height: 26px; display: inline-block;">
				<span class="btn btn-small" onClick="addDet();">添加物料</span>
				<span class="btn btn-small"  onClick="delDet();">删除物料</span>
				<span class="btn-small" style="margin-left:20px" >快速录入物料&nbsp;&nbsp;&nbsp;
				<!-- input type="text" id="fastBt" onkeyup="fastInput(this)"/></span> -->
			</span>
			<div class="scroll_function_small_ud" id="listRange_tableInfo" style="overflow:none;height: auto;width: 1200px;">
			
			<div style="height: auto;overflow:auto;float:left;width:100%;padding-top: 0px;" id="trees">
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
											<li><span style="width:110px">名称</span></li>
											<li><span style="width:350px">规格</span></li>
											<li><span style="width:40px">来源</span></li>
											<li><span style="width:50px">单位</span></li>
											<li><span style="width:50px">数量</span></li>
											<li><span style="width:50px">损耗率</span></li>
											<li><span style="width:50px">允许包料</span></li>
											<li><span style="width:140px">备注</span></li>
											<li><span style="width:50px">替换料</span></li>
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