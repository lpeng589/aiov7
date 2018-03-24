<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title>$text.get("oa.advice.readAdvice")</title>
		<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
		<link  type="text/css" rel="stylesheet" href="/style/css/sharingStyle.css" />
		<script type="javascript" src="/js/function.js"></script>
		<script type="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
		<script type="javascript" src="/js/listPage.vjs"></script>
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="javascript" src="/js/public_utils.js"></script>
		
		<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
		<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
		
		<!-- tree -->
		<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
		<script src="/js/tree/jquery.treeview.js" type="text/javascript"></script>
		<link rel="stylesheet" href="/vm/oa/album/ui/ztree/demoStyle/demo.css" type="text/css"/>
 	    <link rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" type="text/css"/>
	    <script type="text/javascript" src="/js/ztree/demoTools.js"></script>
		<!-- easyui
		<link rel="stylesheet" type="text/css" href="/style/themes/default/easyui.css"/>
		<link rel="stylesheet" type="text/css" href="/style/themes/icon.css"/>
		<script type="text/javascript" src="/js/jquery.easyui.min.js"></script>
		 -->
		<script type="text/javascript">
			var isRefur = "false";//是否是刷新 如果是刷新将不改变右边数据


			var zTree1;
			var isFirst = false;
			var setting;
				setting = {
					checkable: false,
					async: true,
					expandSpeed : false,
					asyncUrl: "/PublicServlet?operation=treeData&type=0",  //获取节点数据的URL地址
					asyncParam: ["name", "id","dateId"],  //获取节点数据时，必须的数据名称，例如：id、name
					asyncParamOther: ["test","true"], //其它参数 ( key, value 键值对格式)
					callback:{
						asyncSuccess: zTreeOnAsyncSuccess,
						//asyncError: zTreeOnAsyncError,
						beforeAsync: zTreeBeforeAsync,
						//collapse: zTreeOnCollapse,
						//expand: zTreeOnExpand,
						//beforeClick: zTreeBeforeClick,
						click:	zTreeOnClick
					}
				};
			var zNodes =[];
			$(document).ready(function(){
				isFirst = true;
				refreshTree();
			});
			
			function zTreeBeforeAsync(treeId, treeNode) {
				isFirst = false;
				if (treeNode.level>3) {
					return false;
				}
				return true;
			}
			function zTreeOnClick(event, treeId, treeNode) {
				if(typeof(top.jblockUI)!="undefined"){
					top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
				}	
				var nodeId = treeNode.id;
				var dateId=treeNode.dateId;
				$("#newIframe").attr("src","/AlbumTreeQueryAction.do?operation=$globals.getOP('OP_QUERY')&requestType=ajax&id="+encodeURIComponent(nodeId)+"&dateId="+dateId);
		
			}
			function zTreeBeforeClick(treeId, treeNode) {
				var canClick = (treeNode!=zTree1.getSelectedNode());
				if (!canClick) {
					zTree1.cancelSelectedNode();
				}
				return canClick;
			}
			
			function updateNode(id,name,srcNode) {
				var ind = id.lastIndexOf("\\");
				var pId = id.substr(0,ind+1);
				pId = pId + name;
				if (!srcNode) {
					window.frames["newIframe"].asyncbox.tips('请先选中一个节点!','error');
					//asyncbox.alert('请先选中一个节点!','$text.get("clueTo")');
					return;
				}
				var oldName = srcNode.name;
				
				if (oldName != name) {
					var url = "/AlbumTreeQueryAction.do?operation=$globals.getOP('OP_UPDATE')&updateType=updateNode";
					
					jQuery.getJSON(url,{pId:id,newName:name,oldName:oldName}, function(data){
						if(data == true){ 
							//asyncbox.tips('修改成功!','$text.get("clueTo")');
							window.frames["newIframe"].asyncbox.tips('修改成功!','success');
							srcNode.name = name;
							zTree1.updateNode(srcNode);
							zTree1.selectNode(srcNode);
							//toRefur();将根节点刷新了


							var parNode = srcNode.parentNode;
							zTree1.reAsyncChildNodes(parNode, "refresh");
							$("#newIframe").attr("src","/AlbumTreeQueryAction.do?operation=$globals.getOP('OP_QUERY')&requestType=ajax&id="+encodeURIComponent(pId));
							
						}else{
							 window.frames["newIframe"].asyncbox.tips('修改失败,请检查是否出现相同名称!','error');
							//asyncbox.alert('修改失败!','$text.get("clueTo")');
						}
					})	
				}
				
			}
			
			function addNode(id,name) {
				var srcNode = zTree1.getSelectedNode();
				var pathstr = id+"\\"+name;
				var newNode = [{"id":pathstr, "name":name}];
				var url = "/AlbumTreeQueryAction.do?operation=$globals.getOP('OP_ADD')&addType=addNode";
				jQuery.getJSON(url,{path:pathstr}, function(data){
					if(data == true){
						//asyncbox.alert('添加成功!','$text.get("clueTo")');
						window.frames["newIframe"].asyncbox.tips('添加成功!','success');
						newNode[0].isParent = true;
						zTree1.addNodes(srcNode, newNode);
						
					}else{
						window.frames["newIframe"].asyncbox.tips('添加失败!','error');
						//asyncbox.alert('该目录下存在相同文件夹!','$text.get("clueTo")');
					}
				})
				
			}
			
			function removeTreeNode(id) {
				var srcNode = zTree1.getSelectedNode();
				if(srcNode==null){
			 		asyncbox.alert('请选择你要修改的子目录！','$text.get("clueTo")');
			 		return false;
			 	}
			 	var isParent = srcNode.icon;
			 	if (isParent == "1") {
			 		window.frames["newIframe"].asyncbox.tips('请在相册设置里面删除该根目录！');
			 		//asyncbox.alert('请在相册设置里面删除该根目录！','$text.get("clueTo")');
			 		return false;
			 	}
				var pathstr = id+"\\"+srcNode.name;
			
				if (srcNode) {
					if (srcNode.nodes && srcNode.nodes.length > 0) {
						var msg = "要删除的节点是父目录，如果删除将连同子目录一起删掉！";
						asyncbox.confirm(msg,'$text.get("clueTo")',function(action){
						　　　if(action == 'ok'){
								var url = "/AlbumTreeQueryAction.do?operation=$globals.getOP('OP_DELETE')&delType=delNode";
								jQuery.getJSON(url,{path:pathstr}, function(data){
									if(data == true){
										//asyncbox.alert('删除成功!','$text.get("clueTo")');
										window.frames["newIframe"].asyncbox.tips('删除成功!','success');
										var parNode = srcNode.parentNode;
										zTree1.removeNode(srcNode);
										//alert(parNode.isParent+","+parNode.name);
										if(parNode.isParent==false){
											parNode.isParent = true;
											zTree1.updateNode(parNode, true);
										}
									}else{
										window.frames["newIframe"].asyncbox.tips('删除失败!','error');
										//asyncbox.alert('删除失败!','$text.get("clueTo")');
									}
								})
						　　　}
				　  		});
					} else {
						var msg = "确定删除该目录吗？";
						asyncbox.confirm(msg,'$text.get("clueTo")',function(action){
						　　　if(action == 'ok'){
								var url = "/AlbumTreeQueryAction.do?operation=$globals.getOP('OP_DELETE')&delType=delNode";
								jQuery.getJSON(url,{path:pathstr}, function(data){
									if(data == true){
										window.frames["newIframe"].asyncbox.tips('删除成功!','success');
										var parNode = srcNode.parentNode;
										zTree1.removeNode(srcNode);
										//alert(parNode.isParent+","+parNode.name);
										if(parNode.isParent==false){
											parNode.isParent = true;
											zTree1.updateNode(parNode, true);
										}
										
									}else{
										window.frames["newIframe"].asyncbox.tips('删除失败!','error');
									}
								})	
						　　　　
						　　　}
				　  		});
					}
				}
			}
			function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
				
				if (isFirst && isRefur != "true") {
					//first to check nodes[0];
					var nodes = zTree1.getNodes();
					if (nodes.length > 0) {
						zTree1.selectNode(nodes[0]);
						//nodeId = nodeId.replace("\\","\\\\");
						var node = nodes[0];
						var nodeId = node.id;
						var	dateId=node.dateId;
						//window.parent.list.src="/AlbumTreeQueryAction.do?operation=$globals.getOP('OP_QUERY')&requestType=ajax&id="+encodeURIComponent(nodeId);
						$("#newIframe").attr("src","/AlbumTreeQueryAction.do?operation=$globals.getOP('OP_QUERY')&requestType=ajax&id="+encodeURIComponent(nodeId)+"&dateId="+dateId);
					} else {
						$("#listTds").toggleClass("listTd");
						//此刻显示无数据

						
						var query = "$!query";
						if (query == "true") {
							$("#newIframe").attr("src","/DirectorySettingAlbumQueryAction.do?isRoot=0");
						}
					}
				} else {
					//var nodeId = treeNode.id;
					//$("#newIframe").attr("src","/AlbumTreeQueryAction.do?operation=$globals.getOP('OP_QUERY')&requestType=ajax&id="+encodeURIComponent(nodeId));
				}
				if (isRefur == "true")　{
					isRefur = "false";
				}
			}
			function zTreeOnCollapse(event, treeId, treeNode) {
				var nodeId = treeNode.id;
				var icon = treeNode.icon;
				if (!icon) {//后台 根目录 icon有值。


					var index = nodeId.lastIndexOf("\\");
					nodeId = nodeId.substring(0,index);
				}
				$("#newIframe").attr("src","/AlbumTreeQueryAction.do?operation=$globals.getOP('OP_QUERY')&requestType=ajax&id="+encodeURIComponent(nodeId));
			}
			
			function zTreeOnExpand(event, treeId, treeNode) {
				var nodeId = treeNode.id;
				$("#newIframe").attr("src","/AlbumTreeQueryAction.do?operation=$globals.getOP('OP_QUERY')&requestType=ajax&id="+encodeURIComponent(nodeId));
			}
			
			function zTreeOnAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
				if (treeNode) {
					$("#onAsyncErrorNode").html( "[" + getTime() + "]  treeId=" + treeId + ";<br/>&nbsp;tId=" + treeNode.tId + "; Name=" + treeNode.name );
				} else {
					$("#onAsyncErrorNode").html( "[" + getTime() + "]  treeNode is Root!");
				}
			}
		
			function refreshTree(asyncUrl) {
				
				zTree1 = $("#treeDemo").zTree(setting, zNodes);
			}
			
			function showDir(){
				var url = "/DirectorySettingAlbumQueryAction.do";
				$("#newIframe").attr("src",url);
			}
			
			function toRefur(){
				//重新刷新
				isRefur = "true";
				refreshTree("/PublicServlet?operation=treeData");
			}
		</script>
	</head>
	<body >
	<ul class="TopTitle">
		<ol><span><img src="/vm/oa/directorySetting/images/PhotoAlbumIcon.jpg" /></span>企业相册</ol>
	</ul>
	<table cellpadding="0" cellspacing="0" class="frame2">
		<tr>
			<td valign="top" class="leftMenu2" style="border-right:1px #a1a1a1 solid;padding:0;margin:0;">
				<div style="padding:0 10px 10px 5px;overflow:hidden;">
					<p class="leftMenu_bgB" style="text-align: left;">
						<img src="/vm/oa/directorySetting/images/fiI.gif" />相册目录
						<span><img onclick="toRefur()" style="cursor:pointer;" src="/vm/oa/directorySetting/images/ti_05.gif"/></span>
					</p>
          <script type="text/javascript">
						$(function(){
							var zz = screen.height
							$("#left_tree").css("height",zz-283);
						});
					</script>
					<div id="left_tree" style="float:left;height:360px;">
						<div class="zTreeDemoBackground">
							<ul id="treeDemo" class="tree" style="overflow:auto;padding:0;margin:0;"></ul>
						</div>		
					</div>
				</div>
			</td>
			<td valign="top" class="list"  id="listTds">
				<iframe id="newIframe" frameborder=false src="" name = "newIframe" style="margin-top: 0px;"  height="600px;" scrolling="no" width="100%" onload="this.height=newIframe.document.body.scrollHeight"></iframe>
			</td>
		</tr>
	</table>
	</body>
</html>

