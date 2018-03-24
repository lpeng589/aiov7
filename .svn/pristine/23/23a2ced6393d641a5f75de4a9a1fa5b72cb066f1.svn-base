<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>明细分类账</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" />
<link type="text/css" rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" />
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<link type="text/css" rel="stylesheet" href="/style1/css/financereport.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/CRselectBoxList.css"/>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript"  src="/js/aioselect.js"></script>
<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript" src="/js/financereport.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/ztree/demoTools.js"></script>
<style type="text/css">
.search_popup{height: 75%}
.listRange_pagebar{width: auto;}
.set1{background: url(/style/images/client/bg.gif) repeat-x 1000px 1000px;background-position: 0px -173px;font-weight: bold;text-align: center;}
.set2{text-align: center;}
.optiondiv a{display: block;margin-left: 6px;}
.optiondiv a:link{color:#000}
.optiondiv li{padding: 0px 12px 2px 12px;}
#condition2 {display: none;}
.title1{width: auto;height: 50px;border-bottom: #d7dcdc 2px solid;}
.tdleft{width:18%;height:98%;border-right: #d7dcdc 2px solid;border-bottom: #d7dcdc 2px solid;}
.w_div .t_span{position:absolute;display:inline-block;z-index:2;background:#fff;padding:0 5px 0 5px;left:20px;line-height:20px;}
.list_u{padding:0 0 0 0;overflow:hidden;margin:0 auto;}
.list_u .list_li{float:left;display:inline;width:200px;margin:0 0 5px 0;}
.list_u .list_li .li_span{float:left;display:inline-block;line-height:20px;width:60px;text-align:left;}
.list_u .list_li .li_sl{float:left;display:inline-block;width:110px;}
.list_u .list_li .list_div{float:left;display:inline-block;overflow:hidden;}
.list_u .list_li .list_div .list_s_a{float:left;display:inline-block;width:20px;height:20px;background:url(/style1/images/search.gif) no-repeat 0 0;cursor: pointer;}
.list_u .list_li .list_txt{float:left;display:inline-block;width:105px;border: #b4b4b4 1px solid;height: 20px;}
.CRselectBox .CRselectBoxOptions{width:110px;}
</style>

<script type="text/javascript">

var zTree1;
var setting;
var zNodes;
#if("$!folderTree"=="")
	zNodes = [];
#else
	zNodes=$!folderTree;
#end
setting = {
	checkable : true,
	expandSpeed : false,
	checkType : {"Y":"s", "N":"ps"}, 
	showLine: true,
	callback: {
		dblclick: zTreeOnDBlClick,
		click: zTreeOnClick
	}
};

var exportUrl = "";

$(document).ready(function(){
	reloadTree();
	#if("$!comeMode"=="")
		showSearchDiv();
		$("#periodYearStart").val("$!accPeriod.accYear");
		$("#periodYearEnd").val("$!accPeriod.accYear");
		$("#periodStart").val("$!accPeriod.accPeriod");
		$("#periodEnd").val("$!accPeriod.accPeriod");
		$("#dateType1").attr("checked","checked");
		$("#datetr").hide();
		$("#moment").hide();
		$("#momentType").val("day");
		$("#dateStart").val("$!globals.getDate()");
		$("#dateEnd").val("$!globals.getDate()");
	#else
		#if("$!conMap.dateType"=="1")
			dealdatetr('periodtr');
		#elseif("$!conMap.dateType"=="2")
			dealdatetr('datetr');
		#elseif("$!conMap.dateType"=="3")
			dealdatetr('moment');
		#end
	#end
	itemcontrol();
	itemdetail();
	
	var postdX;
	document.getElementById("center_td").onmousedown=function(e){
		if(!e) e = window.event;
	    posX = e.clientX - parseInt(8);
	    document.onmousemove = mousemove;           
	}

	document.onmouseup = function(){
	    document.onmousemove = null;
	}
	function mousemove(ev){
	    if(ev==null) ev = window.event;
	    if(event.clientX >200){
		    $("#left_td").css("width",(event.clientX-posX) + "px");
			$("#trees").css("width",(event.clientX-8) + "px");
			$("#right_td").css("width",document.body.clientWidth-event.clientX-25);	
		}
	}
});

var dbclassCode = "";
function reloadTree(node) {
	var setting1 = clone(setting);
	setting1.treeNodeKey = "asClassCode";
	setting1.isSimpleData = true;
	zNodes1 = clone(zNodes);
	setting1.showLine = true;
	zTree1 = jQuery("#treeDemo").zTree(setting1, zNodes1);
	var nodes = zTree1.getNodes();
	var url = "/FinanceReportAction.do?optype=ReporttblAccDet&type=detail";
	if(nodes.length > 0){
		var node = nodes[0];
		url +="&$!conditions";
		#if("$!conMap.gradeShow"=="")
			var nodeclassCode = node.classCode;
		#end
		#if("$!oldClassCode"!="")
			var oldcode = "$!oldClassCode";
			dbclassCode = oldcode;
			var pNode = zTree1.getNodeByParam("classCode", oldcode);
			zTree1.selectNode(pNode);
			url += "&classCode="+oldcode;
		#end
	}
	exportUrl = url;
	$("#firameMain").attr("src",url);
}

function zTreeOnClick(event, treeId, treeNode){
	if(treeNode!=null){
		var srcNode = zTree1.getSelectedNode();
		if (srcNode) {
			if(treeNode.open){
				zTree1.expandNode(srcNode, false, false);
			}else{
				zTree1.expandNode(srcNode, true, false);
			}
			var flag = false;
			if("$!conMap.gradeShow"==""){
				if(treeNode.isItem=="1"){
					var childNodes = zTree1.transformToArray(treeNode);
					for(var i = 1; i < childNodes.length; i++) {
						zTree1.removeNode(childNodes[i]);
					}
					flag = true;
				}
				zTree1.reAsyncChildNodes(srcNode, "refresh");
			}else{
				if(srcNode.asClassCode.indexOf("00000")!=-1){
					var childNodes = zTree1.transformToArray(treeNode);
					for(var i = 1; i < childNodes.length; i++) {
						zTree1.removeNode(childNodes[i]);
					}
					flag = true;
				}
			}
			if(flag){
				jQuery.ajax({type: "POST", url: getUrl(treeNode), success: function(result){
					var datas = result.split(";");
					for(var i=0 ;i<datas.length;i++){
						var obj = jQuery.parseJSON(datas[i]);
						var newNode = obj;
						zTree1.addNodes(srcNode, newNode);
					}
				}});
			}
		}
	}
}

/* 加载核算项url */
function getUrl(treeNode) {
	var param = "&asClassCode="+treeNode.asClassCode+"&classCode="+treeNode.classCode+"&gradeShow=$!conMap.gradeShow&showItemDetail=$!conMap.showItemDetail&showBanAccTypeInfo=$!conMap.showBanAccTypeInfo&";
	param += "itemSort=$!conMap.itemSort&itemLevel=$!conMap.itemLevel&itemCodeStart=$!conMap.itemCodeStart&itemCodeEnd=$!conMap.itemCodeEnd";
	return "/FinanceReportAction.do?optype=ReporttblAccDet&type=loadIsItem" + param;
}


/* 树双击事件 */
function zTreeOnDBlClick(event, treeId, treeNode) {
	if(treeNode!=null){
		var value=$("#_view").val();
		dbclassCode = treeNode.classCode;
		if(dbclassCode.length==5){
			return false;
		}
		$("#oldClassCode").val(treeNode.classCode);
		var url = "/FinanceReportAction.do?optype=ReporttblAccDet&type=detail&orderby="+value+"&classCode="+treeNode.classCode+"&$!conditions";
		exportUrl = url;
		$("#firameMain").attr("src",url);
		frames["firameMain"].blockui();
	}
}

function selectOver(){
	var value=$("#_view").val();
	var url = "/FinanceReportAction.do?optype=ReporttblAccDet&type=detail&orderby="+value+"&classCode="+dbclassCode+"&$!conditions";
	$("#firameMain").attr("src",url);
	frames["firameMain"].blockui();
}


function conditionshow(divid){
	if(divid == "condition1"){
		$("#li1").attr("class","set1");
		$("#li2").attr("class","set2");
		$("#condition0").show();
		$("#condition1").show();
		$("#condition2").hide();
	}else if(divid == "condition2"){
		$("#li1").attr("class","set2");
		$("#li2").attr("class","set1");
		$("#condition0").hide();
		$("#condition1").hide();
		$("#condition2").show();
	}
}

function dealdatetr(tr_id){
	if(tr_id=="periodtr"){
		$("#periodtr").show();
		$("#datetr").hide();
		$("#moment").hide();
	}else if(tr_id=="datetr"){
		$("#periodtr").hide();
		$("#datetr").show();
		$("#moment").hide();
	}else if(tr_id=="moment"){
		$("#periodtr").hide();
		$("#moment").show();
		$("#datetr").hide();
	}
}

//查询
function onsubmits(type){
	var keyword = jQuery("#keyWord").val();
	if(type == "search"){
		jQuery("#keyWord").val('');
	}
	if(keyword == "关键字搜索"){
		jQuery("#keyWord").val('');
	}
	if(!isvalidate()){
		return false;
	}
	var minAccYear = $!globals.get($!periodStr,0);
	var maxAccYear = $!globals.get($!periodStr,1);
	var minAccPeriod = $!globals.get($!periodStr,2);
	var maxAccPeriod = $!globals.get($!periodStr,3);
	var minPeriodBegin = "$!globals.get($!periodStr,4)";
	var maxPeriodEnd = "$!globals.get($!periodStr,5)";
	if($("#dateType1").is(":checked")){
		if(minAccYear>$("#periodYearStart").val() || $("#periodYearStart").val()>maxAccYear || 
			minAccYear>$("#periodYearEnd").val() || $("#periodYearEnd").val()>maxAccYear){
			asyncbox.tips('期间年必须在'+minAccYear+'-'+maxAccYear+'之间','提示',1500);
			$("#periodYearStart").focus();
			return false;
		}
		if($("#periodStart").val()>maxAccPeriod || $("#periodEnd").val()>maxAccPeriod){
			asyncbox.tips('期间必须在'+minAccPeriod+'-'+maxAccPeriod+'之间','提示',1500);
			$("#periodStart").focus();
			return false;
		}
	}else if($("#dateType2").is(":checked")){
		if($("#dateStart").val() == ""){
			asyncbox.tips('开始时间不能为空','提示',1500);
			$("#dateStart").focus();
			return false;
		}
		if($("#dateEnd").val() == ""){
			asyncbox.tips('结束时间不能为空','提示',1500);
			$("#dateEnd").focus();
			return false;
		}
		if($("#dateStart").val()<minPeriodBegin || $("#dateStart").val()>maxPeriodEnd ||
			$("#dateEnd").val()<minPeriodBegin || $("#dateEnd").val()>maxPeriodEnd){
			asyncbox.tips('时间必须在'+minPeriodBegin+'/'+maxPeriodEnd+'之间','提示',1500);
			//$("#dateStart").focus();
			return false;
		}
		if($("#dateStart").val()>$("#dateEnd").val()){
			asyncbox.tips('结束时间不能大于开始时间','提示',1500);
			$("#dateEnd").focus();
			return false;
		}
	}
	blockUI();
	form.submit();
}

function addOrders(){
	$("#contbody").append(patterns());
}

function delOrder(obj){
	$(obj).parent().parent().remove();
}

function patterns(){
	var options= "<tr><td>";
	options += "<select name='orderbyName' id='orderbyName' style='width:150px;margin:0 0 0 10px;'><option value=''></option><option value='BillDate'>凭证日期</option><option value='CredTypeOrderNo'>凭证字号</option></select>&nbsp;";
	options += "<select name='orderbyValue' id='orderbyValue' style='width:150px;'><option value=''></option><option value='ASC'>升序</option><option value='DESC'>降序</option></select>";
	options += "&nbsp;&nbsp;&nbsp;&nbsp;<img src='/style4/images/delete_button.gif' style='cursor:pointer;' title='删除' onclick='delOrder(this)'/>";
	options += "</td></tr>";
	return options;
}

var itemObj = "";
var itemSorts = "";
/* 搜索的弹出框 */
function selectPops(itemName){
	if(!$("#showItemDetail").is(":checked")){
		return ;
	}
	var itemSort = $("#itemSort").val();
	if(itemSort == "all"){
		return ;
	}
	var name = "请选择";
	var urlstr = "/UserFunctionAction.do?operation=22&popupWin=Popdiv&tableName=&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName=&selectName=";
	if(itemSort == "DepartmentCode"){
		//部门弹出框

		urlstr += "SelectDepartment";
		name += "部门";
	}else if(itemSort == "EmployeeID"){
		//职员弹出框

		urlstr += "SelectAccEmployee";
		name += "职员";
	}else if(itemSort == "StockCode"){
		urlstr += "SelectAccStocks";
		name += "仓库";
	}else if(itemSort == "ClientCode"){
		urlstr += "SelectAccClient";
		name += "客户";
	}else if(itemSort == "SuplierCode"){
		urlstr += "SelectAccProvider";
		name += "供应商";
	}
	itemSorts = itemSort;
	itemObj = itemName;
	asyncbox.open({id:'Popdiv',title:name,url:urlstr,width:750,height:470})
}

/* 返回数据 */
function exePopdiv(returnValue){
	if(typeof(returnValue)=="undefined") return ;
	var note = returnValue.split("#;#") ;
	if(itemSorts == "EmployeeID"){
		$("#"+itemObj).val(note[1]);
	}else{
		$("#"+itemObj).val(note[0]);
	}
}

/* 控制分级显示 */
function itemcontrol(){
	var itemvalue = $("#itemSort").val();
	if(itemvalue == "all"){
		$("#gradeShow").attr("disabled","disabled");
		$("#gradeShow").removeAttr("checked");
		$(".inside_div").find("input").attr("disabled","disabled");
		$("#gradeShow").attr("disabled","disabled");
	}else{
		$("#gradeShow").removeAttr("disabled");
		$(".inside_div").find("input").removeAttr("disabled");
		$("#gradeShow").removeAttr("disabled");
	}
	$("#itemCodeStart").val('');
	$("#itemCodeEnd").val('');
}

/* 控制指定核算项目 */
function itemdetail(){
	if($("#showItemDetail").is(":checked")){
		$(".inside_div").find("select").removeAttr("disabled");
		$(".inside_div").find("input").removeAttr("disabled");
		$("#gradeShow").removeAttr("disabled");
	}else{
		$(".inside_div").find("select").attr("disabled","disabled");
		$(".inside_div").find("input").attr("disabled","disabled");
		$("#gradeShow").attr("disabled","disabled");
	}
}

/* 导出列表 */
function onexport(){
	window.frames["firameMain"].exports();
}

/* 打印 */
function onprint(){
	window.frames["firameMain"].prints();
}

/* 清空 */
function cleaDate(){
	$("#highSearch").find("input[type=checkbox]").each(function (){
		$(this).removeAttr("checked");
	});
	$("#periodYearStart").val("$!accPeriod.accYear");
	$("#periodYearEnd").val("$!accPeriod.accYear");
	$("#periodStart").val("$!accPeriod.accPeriod");
	$("#periodEnd").val("$!accPeriod.accPeriod");
	$("#dateStart").val("$!globals.getDate()");
	$("#dateEnd").val("$!globals.getDate()");
	$("#momentType").val("day");
}

/* 关键字查询 */
function insertkeyword(){
	if("$!comeMode"==""){
		alert("请先进行条件查询!");
		return false;
	}
	onsubmits('');
}
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form method="post" scope="request" id="form" name="form" action="/FinanceReportAction.do?optype=ReporttblAccDet" target="">
<input type="hidden" name="comeMode" id="comeMode" value="false" />
<input type="hidden" name="oldClassCode" id="oldClassCode" value="$!oldClassCode"/>
<input type="hidden" id="org.apache.struts.taglib.html.TOKEN" name="org.apache.struts.taglib.html.TOKEN" value=""/>
<div class="title1">
	<div class="titlediv">
		<img src="/style1/images/workflow/ti_003.gif"/><span style="font-size: 14px;">明细分类账</span>
	</div>
	<div style="float: left;padding-bottom: 2px;margin: 20px 10px 10px 100px;">
	#if("$!comeMode"!="")
		#if("$conMap.dateType"=="1")
			期间：$!conMap.periodYearStart年第$!conMap.periodStart期至#if("$!conMap.periodYearStart"!="$!conMap.periodYearEnd")$!conMap.periodYearEnd年#end第$!conMap.periodEnd期



		#elseif("$conMap.dateType"=="2")
			日期：$!conMap.dateStart 至 $!conMap.dateEnd
		#elseif("$conMap.dateType"=="3")
			阶段类别：



			#if("$!conMap.momentType"=="year")本年
			#elseif("$!conMap.momentType"=="halfyear")最近半年



			#elseif("$!conMap.momentType"=="threemonth")最近三个月
			#elseif("$!conMap.momentType"=="tomonth")上一个月
			#elseif("$!conMap.momentType"=="month")本月
			#elseif("$!conMap.momentType"=="week")本周
			#elseif("$!conMap.momentType"=="threeday")最近三天



			#elseif("$!conMap.momentType"=="day")今天
			#elseif("$!conMap.momentType"=="yesterday")昨天
			#end
		#end
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		#if($!globals.getSysSetting('currency')=="true")
		#if("$!conMap.currencyName"=="")
			币别：综合本位币
		#elseif("$!conMap.currencyName"=="all")
			币别：所有币别多栏式选项
		#elseif("$!conMap.currencyName"=="currency")
			币别：所有币别



		#else
			#foreach($currency in $!currencyList)
				#if("$!conMap.currencyName"=="$!globals.get($!currency,0)")
					币别：$!globals.get($!currency,1)
				#end
			#end
		#end
		#end
	#end
	</div>
	<div style="float: left;padding-bottom: 2px;margin: 20px 10px 10px 0px;">
	 <script type="text/javascript">
	  	aioselect('_view',[
		  	{value:'',name:'请选择排序'},
		  	{value:'BillDate ASC',name:'凭证日期 ↑'},
		  	{value:'BillDate DESC',name:'凭证日期 ↓'},
			{value:'CredTypeId ASC',name:'凭证字号 ↑'},
			{value:'CredTypeId DESC',name:'凭证字号 ↓'}
		],'','100','selectOver()');
	</script>
	</div>
	
	<div class="btndiv">
		<div class="op sbtn63" onclick="showSearchDiv()">
			<span class="a"></span>
			<a href="javascript:void(0);">条件查询</a>
			<span class="c"></span>
		</div>
		<div class="op sbtn41" onclick="onsubmits('')">
			<span class="a"></span>
			<a href="javascript:void(0);">刷新</a>
			<span class="c"></span>
		</div>
		#if($LoginBean.operationMap.get("/FinanceReportAction.do?optype=ReporttblAccDet").print() || $!LoginBean.id=="1")
		<div class="op sbtn41" onclick="printReport('FinanceReportAccDetDetail','ReporttblAccDet');">
			<span class="a"></span>
			<a>打印</a>
			<span class="c"></span>
		</div>
		#end
		<div class="op sbtn41" onclick="onexport()">
			<span class="a"></span>
			<a href="javascript:void(0);">导出</a>
			<span class="c"></span>
		</div>
		<div class="op sbtn41" onclick="closePage()">
			<span class="a"></span>
			<a href="javascript:void(0);" >关闭</a>
			<span class="c"></span>
		</div>
	</div>
</div>
	<div style="width:99%;" id="data_list_id">
		<table style="width:100%;height:100%">
			<tr>
				<td class="tdleft" valign="top" id="left_td">
					<div style="padding:5px 0 0 30px; width:99%"><input type="text" id="keyWord" name="keyWord" class="search_text" #if("$!conMap.keyWord" != "") value="$!conMap.keyWord" #else value="关键字搜索" #end 
					onKeyDown="if(event.keyCode==13) insertkeyword();" onblur="if(this.value=='')this.value='关键字搜索';" 
					onfocus="if(this.value=='关键字搜索'){this.value='';}" /><input type="button" class="search_button" onclick="insertkeyword();"/>
					</div>
					<div style="padding:0 0 0 20px;height: 100%;width:100%;">
						<p class="leftMenu_bgB" style="margin:10px 0 0 0;">
							<span style="padding-left: 20px;"><img src="/vm/oa/directorySetting/images/fiI.gif" />&nbsp;会计科目目录</span>
						</p>
						<div style="overflow:auto;float:left;width:210px;padding-top: 5px;" id="trees">
							<script type="text/javascript"> 
								var oDiv=document.getElementById("trees");
								var sHeight=document.documentElement.clientHeight-130;
								oDiv.style.height=sHeight+"px";
							</script>
							<div class="zTreeDemoBackground">
								<ul id="treeDemo" class="tree"></ul>
								#if("$!comeMode"=="")
									<div style="margin:50px 0px 0px 0px">点击条件查询按钮，进行数据查询</div>
								#elseif("$!folderTree"=="[]")
									<div style="margin:50px 0px 0px 0px">很抱歉，没有找到与您条件匹配的科目!</div>
								#end
							</div>	
						</div>
					</div>
				</td>
				<td valign="top" id="center_td" style="width:5px;height:100%;cursor:e-resize;" >
				</td>
				<td valign="top" id="right_td">
					<iframe id="firameMain" frameborder="false" src="" name="firameMain" style="margin-top: 10px;" scrolling="no" width="100%">
					</iframe>
				</td>
			</tr>
		</table>
		<script type="text/javascript"> 
			var oDiv=document.getElementById("data_list_id");
			var sHeight=document.documentElement.clientHeight-80;
			oDiv.style.height=sHeight+"px";
			document.getElementById("firameMain").style.height= sHeight+"px";
		</script>
	</div>
		
		<!-- 条件查询 -->
		<div id="highSearch" class="search_popup" style="display:none;height: 330px;top: 220px;">
		<script type="text/javascript">
			var sLeft=document.body.offsetWidth/2
			$("#highSearch").css("left",sLeft);
		</script>
		<input type="hidden" name="queryType" id="queryType" value=""/>
		<div id="Divtitle" style="cursor: move;width:98%;">
			<span class="ico_4"></span>&nbsp;条件查询
		</div>
			<table style="width:100%;padding-top: 5px;" id="tableSearch">
				<tr>
					<td>
					<div class="optiondiv" style="height:25px;border: none;padding-bottom: 0px;">
						<ul style="margin: 0px;">
							<li id="li1" class="set1" onclick="conditionshow('condition1')">
								<span style="cursor: pointer">条件</span>
							</li>
							<li id="li2" class="set2" onclick="conditionshow('condition2')">
								<span style="cursor: pointer">高级</span>
							</li>
						</ul>
					</div>
					<div id="condition0"><table cellpadding="0" cellspacing="0" width="100%">
						<thead>
							<tr><td class="tdDes1"><input name="dateType" id="dateType1" type="radio" style="border: 0px;" value="1" #if("$conMap.dateType"=="1")checked#end onclick="dealdatetr('periodtr')"/></td><td width="23%"><label for="dateType1">按期间查询</label></td>
						<td class="tdDes1"><input name="dateType" id="dateType2" type="radio" style="border: 0px;" value="2" #if("$conMap.dateType"=="2")checked#end onclick="dealdatetr('datetr')"/></td><td><label for="dateType2">按日期查询</label></td>
						<td class="tdDes1"><input name="dateType" id="dateType3" type="radio" style="border: 0px;" value="3" #if("$conMap.dateType"=="3")checked#end onclick="dealdatetr('moment')"/></td><td><label for="dateType3">按阶段查询</label></td></tr>
						<tr id="periodtr"><td class="tdDes3" colspan="3" width="50%">会计期间:&nbsp;<input name="periodYearStart" style="width:40px;" id="periodYearStart" value="$!conMap.periodYearStart" size="2"/>&nbsp;年



							<input name="periodStart" id="periodStart" style="width:40px;" value="$!conMap.periodStart" size="2"/>&nbsp;期</td>
						<td class="tdDes2" colspan="3" width="50%">至&nbsp;<input name="periodYearEnd" id="periodYearEnd" style="width:40px;" value="$!conMap.periodYearEnd" size="2"/>&nbsp;年



							<input name="periodEnd" id="periodEnd" style="width:40px;" value="$!conMap.periodEnd" size="2"/>&nbsp;期</td>
						</tr>
						<tr id="datetr"><td colspan="3" width="50%" class="tdDes3">日期:&nbsp;
						<input name="dateStart" id="dateStart" value="$!conMap.dateStart" size="13" onClick="openInputDate(this);"/></td>
						<td colspan="3" width="50%" class="tdDes2">至&nbsp;<input name="dateEnd" id="dateEnd" value="$!conMap.dateEnd" size="13" onClick="openInputDate(this);"/></td>
						</tr>
						<tr id="moment" align="center">
						<td colspan="6">
							阶段类别:&nbsp;<select name="momentType" id="momentType">
								<option value="year" #if("$!conMap.momentType"=="year")selected#end>本年</option>
								<option value="halfyear" #if("$!conMap.momentType"=="halfyear")selected#end>最近半年</option>
								<option value="threemonth" #if("$!conMap.momentType"=="threemonth")selected#end>最近三个月</option>
								<option value="tomonth" #if("$!conMap.momentType"=="tomonth")selected#end>上一个月</option>
								<option value="month" #if("$!conMap.momentType"=="month")selected#end>本月</option>
								<option value="week" #if("$!conMap.momentType"=="week")selected#end>本周</option>
								<option value="threeday" #if("$!conMap.momentType"=="threeday")selected#end>最近三天</option>
								<option value="day" #if("$!conMap.momentType"=="day")selected#end>今天</option>
								<option value="yesterday" #if("$!conMap.momentType"=="yesterday")selected#end >昨天</option>
							</select>
							#set($row = "")
							#set($ro = "6")
						</td>
						</tr>
						</thead>
					</table></div>
					<div id="condition1" style="margin-top: 10px;"><table cellpadding="0" cellspacing="0" width="100%">
						<thead>
						#if($!globals.getSysSetting('currency')=="true")
						<tr>
							<td colspan="6" style="padding-left: 20px;">币别:&nbsp;
								<select name="currencyName" id="currencyName" style="width:140px;">
									#foreach($currency in $!currencyList)
										<option value="$!globals.get($!currency,0)" #if("$!conMap.currencyName"=="$!globals.get($!currency,0)")selected#end>$!globals.get($!currency,1)</option>
									#end
									<option value="currency" #if("$!conMap.currencyName"=="currency")selected#end><所有币别></option>
									<option value="" #if("$!conMap.currencyName"=="")selected#end><综合本位币></option>
									<option value="all" #if("$!conMap.currencyName"=="all")selected#end><所有币别多栏式选项></option>
								</select>
							</td>
						</tr>
						#end
						</thead>
						<tbody>
						<tr>
							<td class="tdDes1"><input name="showInAccTypeInfo" id="showInAccTypeInfo" value="1" type="checkbox" #if("$conMap.showInAccTypeInfo"=="1")checked#end/></td><td><label for="showInAccTypeInfo">显示对方科目</label></td>
							<td class="tdDes1"><input name="showBanAccTypeInfo" id="showBanAccTypeInfo" value="1" type="checkbox" #if("$conMap.showBanAccTypeInfo"=="1")checked#end/></td><td><label for="showBanAccTypeInfo">显示禁用科目</label></td>
						</tr>
						<tr>
							#if("$!AccMainSetting.isAuditing"=="1")
							<td class="tdDes1">
								<input name="binderNo" id="binderNo" value="1" type="checkbox" #if("$conMap.binderNo"=="1")checked#end/></td><td><label for="binderNo">包括未过账凭证</label>
							</td>
							#end
							<td class="tdDes1"><input name="showIsItem" id="showIsItem" value="1" type="checkbox" #if("$conMap.showIsItem"=="1")checked#end/></td><td><label for="showIsItem">只显示明细科目</label></td>
							<td class="tdDes1"><input name="takeBrowNo" id="takeBrowNo" value="1" type="checkbox" #if("$conMap.takeBrowNo"=="1")checked#end/></td><td><label for="takeBrowNo">无发生额不显示</label></td>
						<!-- </tr>
						<tr>
							<td class="tdDes1"><input name="balanceAndTakeBrowNo" id="balanceAndTakeBrowNo" value="1" type="checkbox" #if("$conMap.balanceAndTakeBrowNo"=="1")checked#end/></td><td><label for="balanceAndTakeBrowNo">余额为零且无发生额不显示</label></td> -->
						</tr>
						<tr>
							#if("$!globals.getSysSetting('standardAcc')"=="true")
							<td class="tdDes1">
								<input name="showIsAccType" id="showIsAccType" value="1" type="checkbox" #if("$conMap.showIsAccType"=="1")checked#end/></td><td><label for="showIsAccType">显示对方科目核算项目</label>
							</td>
							#end
							<td class="tdDes1"><input name="showTakePeriod" id="showTakePeriod" value="1" type="checkbox" #if("$conMap.showTakePeriod"=="1")checked#end/></td><td><label for="showTakePeriod">显示无发生额的期间合计</label></td>
							<td class="tdDes1"><input name="showAccType" id="showAccType" value="1" type="checkbox" #if("$conMap.showAccType"=="1")checked#end/></td><td><label for="showAccType">按对方科目多条显示</label></td>
						<!-- </tr>
						<tr> -->
						</tr>
						</tbody>
					</table></div>
					<div id="condition2">
					<table cellpadding="0" cellspacing="0" width="100%">
					<tbody>
						<tr>
							<td colspan="2"><input style="margin:0 0 0 10px;" name="showDate" id="showDate" value="1" type="checkbox" #if("$conMap.showDate"=="1")checked#end/><label for="showDate">&nbsp;显示业务日期</label></td>
							<td colspan="2"><input name="showMessage" id="showMessage" value="1" type="checkbox" #if("$conMap.showMessage"=="1")checked#end/><label for="showMessage">&nbsp;显示凭证业务信息</label></td>
						</tr>
						#if("$!globals.getSysSetting('standardAcc')"=="true")
						<tr>
							<td colspan="2"><input style="margin:0 0 0 10px;" name="showItemDetail" id="showItemDetail" value="1" type="checkbox" #if("$conMap.showItemDetail"=="1")checked#end onclick="itemdetail(this)"/><label for="showItemDetail">&nbsp;显示核算项目明细</label></td>
							<td colspan="2"><input name="gradeShow" id="gradeShow" value="1" type="checkbox" #if("$conMap.gradeShow"=="1")checked#end/><label for="gradeShow">&nbsp;分级显示</label></td>
						</tr>
						<tr>
							<td colspan="4">
								<div class="w_div" style="width:425px;height:120px;overflow:hidden;margin:15px 0 0 0;position:relative;border: none;">
								  <div class="inside_div" style="width:420px;padding:15px 0 10px 0;overflow:hidden;border:1px #a1a1a1 solid;margin:0 auto;margin-top:10px;z-index:1;position:absolute;">
								    <ul class="list_u">
								    	<li class="list_li">
								        <span class="li_span">
								          项目类别：



								        </span>
								        <select class="li_sl" id="itemSort" name="itemSort" onchange="itemcontrol()">
											<option value="DepartmentCode" #if("$conMap.itemSort"=="DepartmentCode")selected#end>部门</option>
											<option value="EmployeeID" #if("$conMap.itemSort"=="EmployeeID")selected#end>职员</option>
											<option value="StockCode" #if("$conMap.itemSort"=="StockCode")selected#end>仓库</option>
											#set($isHide = $!globals.hideViewRight('tblAccTypeInfo','','Customer','/FinanceReportAction.do?optype=ReporttblAccDet'))
											#if($isHide)
											<option value="ClientCode" #if("$conMap.itemSort"=="ClientCode")selected#end>客户</option>
											#end
											#set($isHide = $!globals.hideViewRight('tblAccTypeInfo','','Supplier','/FinanceReportAction.do?optype=ReporttblAccDet'))
											#if($isHide)
											<option value="SuplierCode" #if("$conMap.itemSort"=="SuplierCode")selected#end>供应商</option>
											#end
											<option value="all" #if("$conMap.itemSort"=="all")selected#end>所有类别多核算</option>
										</select>
								      </li>
								      <li class="list_li">
								        <span class="li_span">
								          项目级别：



								        </span>
								        <input type="text" class="list_txt" id="itemLevel" name="itemLevel" value="$!conMap.itemLevel"/>
								      </li>
								      <li class="list_li">
								        <span class="li_span">
								          项目代码：



								        </span>
								        <div class="list_div" style="border: none;margin:0;padding:0;">
								          <input type="text" class="list_txt" name="itemCodeStart" id="itemCodeStart" ondblclick="selectPops('itemCodeStart')" value="$!conMap.itemCodeStart"/>
								          <a class="list_s_a" onClick="selectPops('itemCodeStart')"></a>
								        </div>
								      </li>
								      <li class="list_li">
								        <span class="li_span">
								          至



								        </span>
								        <div class="list_div" style="border: none;margin:0;padding:0;">
								          <input type="text" class="list_txt" id="itemCodeEnd" name="itemCodeEnd" ondblclick="selectPops('itemCodeEnd')" value="$!conMap.itemCodeEnd"/>
								          <a class="list_s_a" onClick="selectPops('itemCodeEnd')"></a>
								        </div>
								      </li>
								    </ul>
								  </div>
								  <span class="t_span">指定核算项目</span>
								</div>
							</td>
						</tr>
						#end
					</tbody>
					</table>
					</div>
					</td>
				</tr>
			</table>
			<span class="search_popup_bu"><input type="button" 
					onclick="onsubmits('search');" class="bu_1"  />
				<input type="button" class="bu_3" value="" onclick="cleaDate()"/>
				<input type="button" onclick="closeSearch();" class="bu_2" />
			</span>
			<script language="javascript">
			var posX;
			var posY;       
			fdiv = document.getElementById("highSearch");         
			document.getElementById("Divtitle").onmousedown=function(e){
				if(!e) e = window.event;  //IE
			    posX = e.clientX - parseInt(fdiv.style.left);
			    posY = e.clientY - parseInt(fdiv.style.top);
			    document.onmousemove = mousemove;           
			}
 
			document.onmouseup = function(){
			    document.onmousemove = null;
			}
			function mousemove(ev){
			    if(ev==null) ev = window.event;//IE
			    fdiv.style.left = (ev.clientX - posX) + "px";
			    fdiv.style.top = (ev.clientY - posY) + "px";
			}
			</script>
	</div>
</form>
</body>
</html>