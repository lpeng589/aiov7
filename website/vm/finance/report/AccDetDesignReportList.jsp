<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>多栏式明细账定义</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/vm/oa/album/ui/ztree/demoStyle/demo.css" type="text/css"/>
<link rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" type="text/css"/>
<link rel="stylesheet" href="/style1/css/oa_news.css" type="text/css"/>
<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css" />

<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/ztree/demoTools.js"></script>

<script type="text/javascript">
	var zTree1;
	var setting;
	var zNodes;
	#if("$!data"=="")
		zNodes = [];
	#else
		zNodes=$!data;
	#end
	setting = {
		checkable : false,
		expandSpeed : false,
		checkType : {"Y":"s", "N":"ps"}, 
		showLine: true,
		callback: {
				click: zTreeOnClick,
				dblclick: zTreeOnDBlClick
		}
	};
	
	$(document).ready(function(){
		reloadTree();
	});
	
	//点击组
	function zTreeOnClick(event, treeId, treeNode) {
		var setting1 = clone(setting);
		var srcNode = zTree1.getSelectedNode();
		if (treeNode.open) {
			if (srcNode) {
				zTree1.expandNode(srcNode, false,null);
			}
		} else {
			if (srcNode) {
				zTree1.expandNode(srcNode, true,null);
			}
		}
	}
	
	/* 树双击事件 */
	function zTreeOnDBlClick(event, treeId, treeNode) {
		if(treeNode!=null){
			var id = treeNode.id;
			var urlstr = "/FinanceReportAction.do?optype=ReporttblAccAllDet&type=dealDesign&dealType=updatePre&id="+id;
			asyncbox.open({id:'designdiv',title:"多栏式明细账定义",url:urlstr,cache:false,modal:true,width:800,height:420});
		}
	}
		
	
	//加载树
	function reloadTree(node) {
		var setting1 = clone(setting);
		setting1.treeNodeKey = "id";
		setting1.treeNodeParentKey = "pId";
		setting1.isSimpleData = true;
		zNodes1 = clone(zNodes);
		setting1.showLine = true;
		zTree1 = $("#treeDemo").zTree(setting1, zNodes1);
		var nodes = zTree1.getNodes();
	}	
	
	//关键字查询
	function insertkeyword(){
		var keywordVal = jQuery("#keyWord").val();
		if(keywordVal=="关键字搜索" || keywordVal.trim()==""){
			return false;
		}
		if(keywordVal=="关键字搜索..."){
			jQuery("#keyWord").val('');
		}
		form.submit();
	}
	
	//是否选中一个节点
	function checkNode(){
		var srcNode = zTree1.getSelectedNode();
		if (!srcNode) {
			alert("请先选中一条数据");
			return false;
		}
		return true;
	}
	
	//取消
	function closes(){
		parent.jQuery.close('popList');
	}
	
	//修改
	function updates(){
		if(!checkNode()){
			return false;
		}
		var node = zTree1.getSelectedNode();
		var id = node.id;
		var urlstr = "/FinanceReportAction.do?optype=ReporttblAccAllDet&type=dealDesign&dealType=updatePre&id="+id;
		asyncbox.open({id:'designdiv',title:"多栏式明细账定义",url:urlstr,cache:false,modal:true,width:800,height:420});
	}
	
	//删除成功
	function dels(){
		if(!checkNode()){
			return false;
		}
		asyncbox.confirm('你确定删除吗？','提示',function(action){
		　　 if(action == 'ok'){
				var node = zTree1.getSelectedNode();
				var id = node.id;
				jQuery.ajax({type: "POST", url: "/FinanceReportAction.do?optype=ReporttblAccAllDet&type=delDesign&id="+id, success: function(result){
					if(result == "ok"){
						//删除成功
						asyncbox.tips('删除成功','success');
					}else{
						asyncbox.tips('删除失败','success');
					}
					window.location="/FinanceReportAction.do?optype=ReporttblAccAllDet&type=designList";
				}});
			}
		});
		
	}
	
	//确定按钮
	function checknodes(){
		if(!checkNode()){
			return false;
		}
		var node = zTree1.getSelectedNode();
		var id = node.id;
		parent.accNames = id;
		parent.ajaxData();
		closes();
	}
	
	//增加
	function adds(){
		var urlstr = "/FinanceReportAction.do?optype=ReporttblAccAllDet&type=dealDesign&dealType=addPre";
		asyncbox.open({id:'designdiv',title:"多栏式明细账定义",url:urlstr,width:800,height:420});
	}
	
	//刷新
	function refurbish(){
		window.location.reload();
	}
</script>

<style type="text/css">
div.zTreeDemoBackground {width:auto;}
ul#treeDemo {width:auto;overflow:auto;}
#keyWord{font-family:微软雅黑;font-size:12px;width:250px;margin-left: 20px;color: black;}
.tree li a{font-family:微软雅黑;}
input.btn_bg{width:60px;height:24px;border:none;background:url(/style1/images/oaimages/button_bg.gif) no-repeat 0 0;margin:0 0 5px 0;line-height:26px;cursor: pointer;}
</style>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body style="text-align: left;overflow:hidden;">
<form method="post" action="/FinanceReportAction.do?optype=ReporttblAccAllDet&type=designList" scope="request" name="form" id="form" target="">
	<div style="padding:5px 0 0 5px; width:99%;"><input type="text" id="keyWord" name="keyWord" class="search_text" #if("$!keyWord" != "") value="$!keyWord" #else value="关键字搜索" #end 
		onKeyDown="if(event.keyCode==13) insertkeyword();" onblur="if(this.value=='')this.value='关键字搜索...';" 
		onfocus="if(this.value=='关键字搜索'){this.value='';}" /><input type="button" class="search_button" onclick="insertkeyword();"/>
	</div>
	<div style="border-bottom: 1px #a1a1a1 dashed;width:100%;height:6px;"></div>
	<div>
	<table cellpadding="0" cellspacing="0" class="frame2" width="100%">
		<tr>
			<td width="80%" style="padding:0 5px 0 0;" valign="top">
				<div style="height:255px;overflow-y:auto;float:left;width:100%;margin-top: 10px;">
					<div>
							<ul id="treeDemo" class="tree"></ul>
					</div>
					#if("$!data"=="[]")
						<div style="margin: 50px 0px 0px 30px">暂无相应的数据！</div>
					#end
				</div>
			</td>
			<td valign="top">
				<div>
					<ul>
						<li><input class="btn_bg" name="" id="" type="button" value="确定" onclick="checknodes()"/></li>
						<li><input class="btn_bg" name="" id="" type="button" value="取消" onclick="closes()" style="background-image: url('/style1/images/oaimages/button_bg.gif')"/></li>
						<li><input class="btn_bg" name="" id="" type="button" value="刷新" onclick="refurbish()"/></li>
						<li><input class="btn_bg" name="" id="" type="button" value="新增" onclick="adds()"/></li>
						<li><input class="btn_bg" name="" id="" type="button" value="修改" onclick="updates()"/></li>
						<li><input class="btn_bg" name="" id="" type="button" value="删除" onclick="dels()"/></li>
					</ul>
				</div>
			</td>
		</tr>
	</table></div>
</form>
</body>
</html>