<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/brother_add.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/zTreeStyle.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/demo.css"/>
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<style type="text/css">
ul.ztree {margin-top: 10px;border: 0;background: white;width:210px;height:324px;overflow:auto;}
</style>
<SCRIPT type="text/javascript">
	
var setting = {
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		onClick: zTreeOnClick
	}
	
};
$(document).ready(function(){
	jQuery.fn.zTree.init(jQuery("#treeDemo"), setting, $goodsList);
	pageForward('0','1','');//初始化查询全部
	/*点击分页触发事件*/
	$('.page_a').live('click', function() {
		var classCode = jQuery("#classCode").val();
		pageForward(classCode,$(this).attr("pageNo"),'');
	});

	/*点击分页跳转按钮*/
	$('.go').live('click', function() {
		var selectPageNo = $(this).prev().val();//当前选择的页数


		var totalPage = $(this).attr("totalPage");//总数
		var classCode = jQuery("#classCode").val();
		if(checkPageNo(selectPageNo,totalPage)){
			pageForward(classCode,selectPageNo,'');
		}
	});
});

function zTreeOnClick(event, treeId, treeNode) {
	pageForward(treeNode.id,'1','');
};

/*异步刷新邻居表分页数据*/
function pageForward(classCode,pageNo,pageSize){
	var keyWord = jQuery("#keyWord").val();
	var selectOption = jQuery("#goodSelect").val();
	jQuery.ajax({
		type: "POST",
		url: "/CRMBrotherAction.do",
		data: "operation=4&type=goodsQuery&classCode="+classCode+"&pageNo="+pageNo+"&pageSize="+pageSize+"&keyWord="+keyWord+"&selectOption="+selectOption,
		success: function(msg){
			jQuery("#showGoods").html(msg);
		}
	});
}

//返回值


function returnVal(){
	var str ="";
	jQuery("#showGoods :checkbox:checked").each(function(){
		var li = $(this).parents("li");
		var classCode = li.find("span[name='classCode']").text();
		var goodsName = li.find("span[name='goodsName']").text();
		var price = li.find("span[name='price']").text();
		var unit = li.find("span[name='unit']").text();
		var unitName = li.find("span[name='unitName']").text();
		
		price = parseFloat(price).toFixed(2);//价格四舍五入
		
		if(typeof(goodsName)!="undefined" && goodsName != ""){
			str+=classCode+":"+goodsName+":"+price+":"+unit+":"+unitName+";"; 		
		}
		
	})
	return str;
}

/*选择每页数量*/
function pageSelect(obj){
	var classCode = jQuery("#classCode").val();
	pageForward(classCode,'1',$(obj).val());

}

function dbGoodsSelect(obj){
	var str ="";
	var classCode = $(obj).find("span[name='classCode']").text();
	var goodsName = $(obj).find("span[name='goodsName']").text();
	var price = $(obj).find("span[name='price']").text();
	var unit = $(obj).find("span[name='unit']").text();
	var unitName = $(obj).find("span[name='unitName']").text();
	if(typeof(goodsName)!="undefined" && goodsName != ""){
		str=classCode+":"+goodsName+":"+price+":"+unit+":"+unitName; 		
	}
	var popDiv = $(".asyncbox_normal",parent.document) ;
	if(popDiv.size()>1){
		var popId = popDiv.last().prev().attr("id");
		parent.jQuery.opener(popId).dbGoodsSelect(str);
	}else{
		var popId = popDiv.attr("id");
		parent.jQuery.opener(popId).dbGoodsSelect(str);
	}
	
}

/*
关键字查询方法
*/
function keywordSubmit(){
	var pageSize = jQuery("#pageSize").val()
	var classCode = jQuery("#classCode").val()
	pageForward(classCode,'1',pageSize);
}
</SCRIPT>

</head>
<body>
	<div class="det_page">
	<!-- 
		<div class="sdv">
			<div class="indv">
				<select class="slt_s">
					<option>编号搜索</option>
				</select>
				<input type="text" class="txt_ip" />
				<div class="btn_dv">搜</div>
			</div>
		</div>
	 -->
		<div class="page_lt">
			<div class="search_dv">
				<select class="slt" id="goodSelect">
					<option value="GoodsNumber">商品编号</option>
					<option value="goodsFullName">商品名称</option>
					<option value="GoodsSpec">商品规格</option>
				</select>
				<input type="text" class="ipt" id="keyWord" name="keyWord"  onKeyDown="if(event.keyCode==13) keywordSubmit();"/>
				<i class="search_btn" onclick="keywordSubmit()"></i>
			</div>
			<div class="tdv">
				<i class="icon"></i>
				<i class="ivar">商品列表</i>
			</div>
			<div class="list_tree">
				<ul id="treeDemo" class="ztree"></ul>
			</div>
		</div>
		<div class="page_rt" id="showGoods">
			<ul class="det_ul">
				<li class="det_tli">
					<span class="item_sp sp-1"><input class="cbox" type="checkbox" /></span>
					<span class="item_sp sp-2">商品编号</span>
					<span class="item_sp sp-3">商品名称</span>
					<span class="item_sp sp-2">商品规格</span>
					<span class="item_sp sp-2">商品单位</span>
					<span class="item_sp sp-2">单价</span>
				</li>
			</ul>
		</div>
	</div>
</body>
</html>
