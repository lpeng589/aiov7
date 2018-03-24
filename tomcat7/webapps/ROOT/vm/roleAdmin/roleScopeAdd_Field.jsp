<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>group</title>
<link type="text/css"  rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" />
<link type="text/css"  rel="stylesheet" href="/vm/oa/album/ui/ztree/demoStyle/demo.css"/>
<link type="text/css"  rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" />
<link type="text/css"  rel="stylesheet" href="/style1/css/oa_news.css" />
<link type="text/css"  rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />

<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/ztree/demoTools.js"></script>

<script type="text/javascript">
var zTree1;
var setting;
var zNodes;
	zNodes=$!zNodes;
	setting = {
		expandSpeed : false,
		treeNodeKey : "id",
		treeNodeParentKey : "pId",
		isSimpleData : true,
		showLine : true,
		checkType : {"Y":"s", "N":"ps"}, 
		showLine: true,
		callback: {click: zTreeOnClick,}
};
	
$(document).ready(function(){
	reloadTree();			
	//initCopyEvent();
});

function reloadTree(node) {	
	zTree1 = $("#treeDemo").zTree(setting, zNodes);
	var nodes = zTree1.getNodes();	
}

function zTreeOnClick(event, treeId, treeNode) {
	checkeds(event,treeNode);
	if(treeNode.tbtype != "DIR"){
		url = "/RoleAction.do?roleId=$roleId&roleName=$!globals.encode($!roleName)&change=yes&operation=$globals.getOP("OP_SCOPE_RIGHT_ADD_PREPARE")&winCurIndex=$!winCurIndex&type=$!type&tableName="+treeNode.tableName+"&tbtype="+treeNode.tbtype+"&fromUser=$!fromUser&tableDisplay="+encodeURIComponent(treeNode.name);
		jQuery("#firameMain").attr("src",url);
	}
}

function checkeds(event,treeNode) {
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


/*

	function tbClick(tableName,tbtype,tableDisplay){
		$(".ico_1",document).attr("class","ico_No");
		$("#lm_"+tableName+" > span ",document).attr("class","ico_1");
		
		jQuery("#firameMain").attr("src","/RoleAction.do?roleId=$roleId&roleName=$!globals.encode($!roleName)&change=yes&operation=$globals.getOP("OP_SCOPE_RIGHT_ADD_PREPARE")&winCurIndex=$!winCurIndex&type=$!type&tableName="+tableName+"&tbtype="+tbtype+"&fromUser=$!fromUser&tableDisplay="+encodeURIComponent(tableDisplay));
	}
	
	*/
	
	
	
	
	
	
	
function save(){
	document.getElementById("firameMain").contentWindow.save();
}
function dealAsyn(act){ 
	//当删除范围权限时，只需刷新界面
	if(parent.jQuery.exist('AddUpdatediv')){
		parent.jQuery.reload('setScopediv'); 
		parent.jQuery.close('AddUpdatediv');
		
	}
}
</script>

<style type="text/css">
div.zTreeDemoBackground {width:auto;}
ul#treeDemo {width:auto;overflow:auto;}
#keyWord{font-family:微软雅黑;font-size:12px;width:139px;}
.leftMenu2{height:27px;padding:0 0 0 0;line-height:27px;font-family:微软雅黑;font-size:12px;}
.tree li a{font-family:微软雅黑;}
.leftMenu2>div>em{display:inline-block;float:left;margin:0 5px 0 0;font-family:微软雅黑;}
.leftMenu2>div>em.wt{color:red;}
.ico_1{margin:9px 5px 0 5px;}
.ico_No{margin:9px 5px 0 5px;}

.tree_w>ul{padding:10px 5px 0 5px;margin:0;font-family:微软雅黑;}
.tree_w>ul li{line-height:18px;float:left;display:inline-block;font-size:14px;padding:5px;background:#CBE6FF;margin:0 0 1px 1px;}
.tree_w>ul li:hover{background:#2E85C0;}
.tree_w>ul li:hover a{color:#fff;}
</style>
</head>
<body style="text-align: left;overflow:hidden;">
	<table cellpadding="0" cellspacing="0" class="frame2" width="100%">
		<tr>
			<td width="20%" style="border-right:1px #a1a1a1 dashed;padding:0 5px 0 0;" valign="top">
				<div class="leftModule" style="padding:3px 0 0 0;margin:0;border: 0px;">
				<div class="leftMenu">
					<div style="height:423px;overflow-y:auto;float:left;width:100%;">
						<div id="left_tree" style="display:block;float:left;margin-left: 10px;white-space: normal">
							<div class="zTreeDemoBackground">
								<ul id="treeDemo" class="tree"></ul>
							</div>		
						</div>												
					</div>					
				</div>
				</div>				
			</td>
			<td valign="top" class="list" id="listTd" style="padding:0 0 0 5px;">
				<iframe id="firameMain" frameborder=false src="/RoleAction.do?roleId=$roleId&roleName=$!globals.encode($!roleName)&change=yes&operation=$globals.getOP("OP_SCOPE_RIGHT_ADD_PREPARE")&winCurIndex=$!winCurIndex&type=$!type&tableName=NO&fromUser=$!fromUser&tbtype=&tableDisplay=" name = "firameMain" style="margin-top:3px;height:430px;" scrolling="no" width="99%"></iframe>
			</td>
		</tr>
	</table>
</body>
</html>