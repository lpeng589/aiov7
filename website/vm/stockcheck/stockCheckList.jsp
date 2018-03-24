<!DOCTYPE html>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>盘点</title>
<link type="text/css" rel="stylesheet" href="/style1/css/ListRange.css" />
<link type="text/css" rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/stockCheck.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<link rel="stylesheet" href="/style1/css/tab.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript" src="/js/ztree/demoTools.js"></script>
<script type="text/javascript">
	var zTree1;
	var setting;
	var zNodes = $!stockData;
	setting = {
		expandSpeed : false,
		checkType : {"Y":"s", "N":"ps"}, 
		showLine: true,
		callback: {
				click: zTreeOnClick,
		}
	};
	
	$(document).ready(function(){
		reloadTree();
		loadRightData('','');
		$(".div_all").mouseover(function(){
  			$(".div_all").css("text-decoration","underline");
		});
		$(".div_all").mouseout(function(){
  			$(".div_all").css("text-decoration","none");
		});
	});
	
	function reloadTree(node) {
		var setting1 = clone(setting);
		setting1.treeNodeKey = "id";
		setting1.treeNodeParentKey = "pId";
		setting1.isSimpleData = true;
		zNodes1 = clone(zNodes);
		setting1.showLine = true;
		setting1.showIcon = false;
		zTree1 = $("#treeDemo").zTree(setting1, zNodes1);
		var nodes = zTree1.getNodes();
		if(nodes.length > 0){
			zTree1.selectNode(nodes[0]);
			zTree1.expandNode(nodes[0], true,null);
		}
		
	}
	/* 点击树事件 */
	function zTreeOnClick(event, treeId, treeNode) {
		$(".div_all").css("color","");
		var setting1 = clone(setting);
		if (treeNode.open) {
			zTree1.expandNode(treeNode, false,null);
		} else {
			zTree1.expandNode(treeNode, true,null);
		}
		btn_color();
		loadRightData('item',treeNode.id);
	}
	
	function btn_color(){
		$("#span_yes").css("background","#6ba9df");
		$("#span_no").css("background","#6ba9df");
	}
	
	/* 处理数据 */
	function loadRightData(searchType,searchValue){
		var optype = $("#optype").val();
		var operation = $("#operation").val();
		jQuery.ajax({
	    	type: "POST",
	    	url: "/StockCheckAction.do?operation="+operation,
	    	data: "&optype="+optype+"&searchType="+searchType+"&searchValue="+searchValue,
	    	async : false,
	    	cache: false,
	   		dataType: "json",
	    	success: function(data){ 
	    		$("#content_body").html('');
	    		if(data.length == 0){
	    			$("#cang").show();
	    		}
	    		for(var i = 0; i < data.length; i++) {
				    var datas = data[i];
				    var fullName = datas['$!StockMarkerName'];
				    var checkdate = "";
				    var str = '<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);"><td style="text-align:left;padding-left:20px;"><input type="hidden" id="stockPreId" name="stockPreId" value='+datas.stockPreId+'/><span title="'+datas['$!StockMarkerName']+'">'+fullName+'</span></td><td><span>';
				    if(datas.CheckDate == ""){
				    	checkdate += "&nbsp;";
				    }else{
				    	checkdate += datas.CheckDate;
				    }
				    var dateStr = '</span></td>';
					dateStr += '<td id="'+datas.classCode+'_pre_td"><span>';
				    if(datas.stockPreStatus=="0"){
					    dateStr += '<a class="noClick" href="javascript:prepare(\''+datas.classCode+'\')">准备</a>';
					}else{
						dateStr += '<a class="noClick" href="javascript:cancelPre(\''+datas.stockPreId+'\',\''+datas.classCode+'\')">取消准备</a>';
					}
					dateStr += '</span></td>';
				    dateStr += '<td id="'+datas.classCode+'_import_td"><span>';
				    if(datas.stockInputStatus=="0"){ 
				    	dateStr += '<a class="noClick" href="javascript:addStockCheck(\''+datas.stockPreId+'\',\''+datas.classCode+'\',\''+datas.CheckDate+'\',\''+(datas['$!StockMarkerName']).replace('&apos;','\\&apos;').replace('&quot;','\\&quot;')+'\')">录入</a>&nbsp;|&nbsp;';
				    	dateStr += '<a class="noClick" href="javascript:stockcheckDetail(\''+(datas['$!StockMarkerName']).replace('&apos;','\\&apos;').replace('&quot;','\\&quot;')+'\')">查看</a>&nbsp;';
				    }
				    if(datas.stockInputStatus=="0" || datas.stockDealStatus=="0"){
				    	dateStr += '|<a class="noClick" href="javascript:statisticsStockCheck(\''+datas.classCode+'\',\''+datas.stockPreId+'\',\''+(datas['$!StockMarkerName']).replace('&apos;','\\&apos;').replace('&quot;','\\&quot;')+'\')">统计</a>';
				    }
				    dateStr += '</span></td>';
				    dateStr += '<td id="'+datas.classCode+'_deal_td"><span>';
				    if(datas.stockDealStatus=="0"){
				    	dateStr += '<a class="noClick" href="javascript:stockCheckDeal(\''+datas.stockPreId+'\',\''+datas.classCode+'\',\''+datas.CheckDate+'\')">处理</a>&nbsp;';
				    }
				    dateStr += '</span></td>';
				    dateStr += '<td id="'+datas.classCode+'_his_td"><span>';
				    dateStr += '<a class="noClick" href="javascript:contrastStockCheck(\''+datas.classCode+'\')">历史查询</a>';
				    str += checkdate+dateStr;
				    str += "</span></td></tr>";
				   	$("#content_body").append(str);
				}
	    	}
		});
	}
	
	/* 盘点准备 */
	function prepare(stockCode){
		asyncbox.confirm('是否盘点准备此仓库！','提示',function(action){
		　　if(action == 'ok'){
				jQuery.ajax({type: "POST",url: "/StockCheckAction.do?operation=4&optype=stockPrepare&stockCode="+stockCode,async : false,success: function(data){
					if(data == "OK;"){
						alert("仓库准备成功！");
						window.location.reload();
					}else{
						alert("仓库准备失败！"+data.split(";")[1]);
					}
				}});
			}
		});
	}
	
	/* 取消准备 */
	function cancelPre(stockPreId,classCode){
		asyncbox.confirm('是否取消准备此仓库！取消准备时会影响以下数据：<br />&nbsp;&nbsp;1.删除相应的盘点单数据。<br />&nbsp;&nbsp;2.备份的数据删除。','提示',function(action){
		　　if(action == 'ok'){
				jQuery.ajax({type: "POST",url: "/StockCheckAction.do?operation=4&optype=stockCancelPrepare&stockPreId="+stockPreId+"&classCode="+classCode,async : false,success: function(data){
					if(data == "OK"){
						alert("取消准备仓库成功！");
						window.location.reload();
					}else if(data == "ERROR"){
						alert("取消准备仓库失败");
					}
				}});
			}
		});
	}
	
	/* 查询 */
	function keyWords(){
		$(".div_all").css("color","");
		zTree1.cancelSelectedNode(false);
		var keyword = $("#keySearch").val();
		if(keyword=="仓库搜索"){
			keyword = '';
		}
		btn_color();
		loadRightData('keyWord',keyword);
	}
	
	/* 查询已准备，未准备 */
	function pre(type){
		zTree1.cancelSelectedNode(false);
		if(type == "yes"){
			$("#span_yes").css("background","#FFCC66");
			$("#span_no").css("background","#6ba9df");
		}else{
			$("#span_yes").css("background","#6ba9df");
			$("#span_no").css("background","#FFCC66");
		}
		loadRightData('pre',type);
	}
	
	/* 录入盘点单 */
	function addStockCheck(stockPreId,stockCode,stockCheckDate,stockName){ 
		asyncbox.open({
		id:'addDiv',
	　　　html : '<div class="spe-warn"><span class="radio-wp"><input type="radio" id="addtype1" name="addtype" value="0" checked/><label for="addtype1">手工录入：进入盘点录入单进行手工录入。</label></span><span class="radio-wp"><input type="radio" id="addtype2" name="addtype" value="1"/><label for="addtype2">批量导入：根据模板进行批量导入。</label></span></div>',
	　　　width : 300,
		 modal　: true,
		 title : '盘点单录入方式',
		 btnsbar : jQuery.btn.OKCANCEL,
			 callback : function(action){
		　　　　　if(action == 'ok'){
					var addtype = jQuery("input[name='addtype']:checked").val();
					if(addtype == 0){
						var url = "UserFunctionAction.do?tableName=tblStockCheck&parentCode=&operation=6&moduleType=&f_brother=&parentTableName=&winCurIndex=1";
						//参数
						url += "&paramValue=true&StockCode="+stockCode+"&CheckDate="+encodeURI(stockCheckDate)+"&PrepareId="+stockPreId+"&tblStock.StockFullName="+encodeURI(stockName);
						mdiwin(url,'盘点录入单');
					}else if(addtype == 1){
						importStockCheck(stockCode);
					}
					jQuery.close("addDiv");
					return false;
		　　　　　}
		　　　}
	　　　});
	}
	
	/* 导出模板，导入盘点单 */
	function importStockCheck(stockCode){
		mdiwin('/StockCheckAction.do?operation=6&optype=importPreCheckBill&stockCode='+stockCode,'盘点单导入');
	}
	
	/* 盘点统计 */
	function statisticsStockCheck(stockCode,PrepareId,stockName){
		url = '/ReportDataAction.do?reportNumber=tblStockCheckDet&StockCode='+stockCode+'&hide_StockCode='+encodeURIComponent(stockCode+'#;#0')+'&tblStock.StockFullName='+encodeURIComponent(stockName)+'&PrepareId='+PrepareId+"&detTable_list=tblStockCheck&LinkType=@URL:";

		mdiwin(url,'盘点统计');
	}
	
	/* 历史查询 */
	function contrastStockCheck(stockCode){
		mdiwin('/ReportDataAction.do?reportNumber=tblStockCheckHistory&StockCode='+stockCode+'&detTable_list=tblStockCheck','盘点历史');
	}
	
	/* 录入查看 */
	function stockcheckDetail(stockName){
		var url = '/StockCheckAction.do?operation=4&optype=dealFormSubmit&tableName=tblStockCheck&status=0&StockFullName='+encodeURI(stockName);
		mdiwin(url,'盘点录入单');
	}
	
	/* 盘盈盘亏查看 */
	function contrastDetail(stockName){
		var url = '/StockCheckAction.do?operation=4&optype=dealFormSubmit&tableName=tblCheck&StockFullName='+encodeURI(stockName);
		mdiwin(url,'盘盈盘亏单');
	}
	
	/* 历史盘点单查询 */
	function hisStockCheck(){
		var url = '/StockCheckAction.do?operation=4&optype=dealFormSubmit&tableName=tblStockCheck';
		mdiwin(url,'盘点录入单');
	}
	
	/* 盘点处理 */
	function stockCheckDeal(stockPreId,stockCode,stockCheckDate){
		var urlstr = "/StockCheckAction.do?operation=4&optype=stockCheckDealList&stockPreId="+stockPreId+"&stockCode="+stockCode+"&stockCheckDate="+stockCheckDate;
		var heights = 530;
		var widths = 1100;
		var bHeight=parseInt(document.documentElement.scrollHeight);
		if(bHeight<heights){
			heights = bHeight-30;
		}
		var bWidth=parseInt(document.documentElement.scrollWidth);
		if(bWidth<widths){
			widths = bWidth-200;
		}
		asyncbox.open({id:'stockDiv',title:'盘点处理',url:urlstr,width:widths,height:heights,callback : function(action){
			if(action == 'close'){
	　　　　　　　window.location.reload();
	　　　　　}
		}});
	}
	
</script>
</head>
<body>
<input type="hidden" name="operation" id="operation" value="4"/>
<input type="hidden" name="optype" id="optype" value="stockNext"/>
<div class="title_div" style="clear:both;overflow:hidden;">
	<div style="float: left;display:inline-block;">
		<img src="/style/images/stockcheckflow.jpg"/>
	</div>
	<div style="float:left;display:inline-block;overflow:hidden;margin:10px 0 0 10px;">
	</div>
</div>
<div class="div_t">
	<div class="mBlock">
		<div style="border: #aecbd4 1px solid;padding:10px;" id="data_list_id">
			<script type="text/javascript"> 
				var oDiv=document.getElementById("data_list_id");
				var sHeight=document.documentElement.clientHeight-90;
				oDiv.style.height=sHeight+"px";
			</script>
			<div class="LeftMenu_list">
				<span>
					<span class="oBtn" id="span_yes" onclick="pre('yes')">已准备</span>
					<span class="oBtn" id="span_no" onclick="pre('no')">未准备</span>
					<input type="text" id="keySearch" name="keySearch" class="search_text" value="#if("$!searchValue"=="")仓库搜索#else$!searchValue#end" onkeydown="if(	event.keyCode==13){keyWords()}" onblur="if(this.value=='')this.value='仓库搜索';" onfocus="if(this.value.replace(/(^\s*)|(\s*$)/g,'')=='仓库搜索'){this.value='';}" /><input type="button" class="search_button" style="padding-left: 0px;cursor: pointer;" title="查询" onclick="keyWords()" />
				</span>
				<ul id="treeDemo" class="tree" style="overflow-x: auto; overflow-y: auto; height: 410px;margin:0 0 0 10px;"></ul>
			</div>
			<div class="mBlockRight" style="overflow-y:auto;height:450px; ">
			<table class="tblDepot" cellpadding="0" cellspacing="0">
				<thead>
					<tr>
						<td width="220">仓库名称</td>
						<td width="120">准备时间</td>
						<td width="100">盘点准备</td>
						<td width="150">盘点录入</td>
						<td width="100">盘点处理</td>
						<td width="100">历史</td>
					</tr>
				</thead>
				<tbody id="content_body"> 
				</tbody>
			</table>
				<div id="cang" style="display:none; text-align: center;width: 100%;	height: 234px;line-height: 140px;	background: url(/style1/images/workflow/no_data_bg.gif) no-repeat center;float: left;margin-top: 20px;margin-bottom: 2px;">
					暂无仓库盘点信息
				</div>
		</div></div>
	</div>
</div>
</body>
</html>
