<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="/js/jquery.js"></script>
<link type="text/css" rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />

<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<title>button</title>
<script>

var zTree1;
var setting;
var zNodes=$!treedata;

var addModule = "false";
var updateModule = "false";
var delModule = "false";

#set($hasRight = false)

#if($LoginBean.getOperationMapKeyId().get($MOID).add() )
	addModule = "true";
	#set($hasRight = true)
#end
#if($LoginBean.getOperationMapKeyId().get($MOID).update() )
	updateModule = "true";
	#set($hasRight = true)
#end
#if($LoginBean.getOperationMapKeyId().get($MOID).delete() )
	delModule = "true";
	#set($hasRight = true)
#end

setting = {
	expandSpeed : false,
	checkType : {"Y":"s", "N":"ps"}, 
	treeNodeKey : "classCode",
	showLine: true,
	showIcon : true,
	isSimpleData : true,
	callback: {
		dblclick: zTreeOnDBlClick,
		click:zTreeOnDBlClick
	}
};

$(document).ready(function(){
	reloadTree(zNodes);
});

function reloadTree(nodes) {
	setting1 = clone(setting);
	
	#if("$!editable"== "true"&& $hasRight)
	setting1.editable= true,
	setting1.edit_addBtn = true;
	setting1.edit_renameBtn = true;
	setting1.edit_removeBtn = true;
	#end
	
	zNodes1 = clone(nodes);	
	zTree1 = jQuery("#treeDemo").zTree(setting1, zNodes1);
	//zTree1.expandAll(true);
	if(zNodes1.length>0){
		zTree1.expandNode(zNodes1[0],true,false,true,false);
	}
	
	
}

function addData(treeNode){
	var classCode = treeNode.classCode;	
	var name = treeNode.name;
	var number = treeNode.number;
	
	#if("$!hasScopeRight"=="true")
	//有范围权限后，不可以在根据目录下添加
	if(classCode == ""){
		asyncbox.tips('无权限添加一级目录');
		return;
	}
	#end
	
	showPanel('1',classCode,name,number,'','',''); 
}
/* 修改treeNode部门 */
function editData(treeNode){
	var name = treeNode.name;
	var classCode = treeNode.classCode;
	var id = treeNode.id;
	if(classCode == ""){
		asyncbox.tips('根类不允许修改');
		return;
	}
	var number = treeNode.number;
	
	var parentName = treeNode.parentNode.name;
	var parentClassCode = treeNode.parentNode.classCode;
	var parentNumber = treeNode.parentNode.number;
	
	
	showPanel('2',parentClassCode,parentName,parentNumber,classCode,name,number,id); 
}

function showPanel(type,parentClassCode,parentName,parentNumber,classCode,name,number,id){
	  //取消事件冒泡 
	 var e=arguments.callee.caller.arguments[0]||event; //若省略此句，下面的e改为event，IE运行可以，但是其他浏览器就不兼容
	 if (e && e.stopPropagation) { 
	  e.stopPropagation(); 
	 } else if (window.event) { 
	  window.event.cancelBubble = true; 
	 }     


	var title = "新增$!tableDisplay下级";
	//var btns = jQuery.btn.OKCANCEL.concat([{text    : '详情修改',action  : 'detailUpdate'},{text    : '转子结点',action  : 'convertFle'}]);
	var btns = jQuery.btn.OKCANCEL;
	#if("$globals.getSysSetting('showparentClass')"=="false")
		btns = btns.concat([{text    : '详情修改',action  : 'detailUpdate'}]);
	#end	
	
	var opType = "add";
	if(type != "1"){
		title = "修改$!tableDisplay";
		opType = "update";
	}
	var wino = parent.frames['mainTreeFrame'].contentWindow;
	if(wino==undefined){
		wino = parent.frames['mainTreeFrame'];
	}
	wino.asyncbox.open({
		id:'dealTree',
		html : jQuery("#addBranch").html(),
		width : 340,
		modal　: true,
		title : title,
		btnsbar : btns,
		callback : function(action){
			if(action == 'ok'){
				var nameF = wino.$("#nameField").val();
				
				var nameFName = wino.$("#nameFieldName").val();
				#if("$!numberField_bak" != "") ##有编号字段时，新增要取默认编号	
				var numberF = wino.$("#numberField").val();
				var numberFName = wino.$("#numberFieldName").val();
				#else
				var numberF = '';
				var numberFName = '';
				#end
				if(wino.$("#numberField").size()> 0  && numberF == ""){
					wino.asyncbox.tips('$!numberDisaplay不能为空！','alert');
					wino.$("#numberField").focus();
					return false;
				}
				if(nameF == ""){
					wino.asyncbox.tips('$!nameDisaplay不能为空！','alert');
					wino.$("#numberField").focus();
					return false;
				}
				var urls = "/TreeAction.do?opType="+opType;
				jQuery.ajax({
					type:"POST",
					url:urls,
					data:"tableName=$!tableName&parentCode="+parentClassCode+"&nameFieldName="+nameFName+"&nameField="+nameF+"&numberFieldName="+numberFName+"&numberField="+numberF+"&classCodeField="+classCode+"&MOID=$!MOID&moduleType=$!moduleType",
					success: function(msg){
						if(msg == "OK"){
							wino.asyncbox.tips('保存成功！','success');
							reload();
						}else{
							wino.asyncbox.error(msg,'提示');
						}
					}
				});
			}else if(action == 'detailUpdate'){
				wino.update(id+','+classCode)
			}else if(action == 'convertFle'){
				var urls = "/TreeAction.do?opType=convertFile";
				jQuery.ajax({
					type:"POST",
					url:urls,
					data:"tableName=$!tableName&parentCode="+parentClassCode+"&classCodeField="+classCode+"&keyId="+id+"&MOID=$!MOID&moduleType=$!moduleType",
					success: function(msg){
						if(msg == "OK"){
							wino.asyncbox.tips('保存成功！','success');
							reload();
						}else{
							wino.asyncbox.error(msg,'提示');
						}
					}
				});
			}
		}
	});
	
	wino.$("#topName").val(parentName); //设置上级名字
	

	if(type == "1"){
	#if("$!numberField" != "") ##有编号字段时，新增要取默认编号	
		jQuery.ajax({
			type:"POST",
			async: false,
			url:"/TreeAction.do?opType=getLastValue",
			data:"tableName=$!tableName&numberField=$!numberField&parentCode="+parentClassCode,
			success: function(msg){
				wino.$("#numberField").val(msg); 
			}
		});
	#end
	}else{
		wino.$("#numberField").val(number); 
		wino.$("#nameField").val(name); 
	}

	
}
/*刷新*/
function reload(){
	window.location.reload();
}


/* 删除treeNode部门 */
function removeData(treeNode){
	 //取消事件冒泡 
	 var e=arguments.callee.caller.arguments[0]||event; //若省略此句，下面的e改为event，IE运行可以，但是其他浏览器就不兼容
	 if (e && e.stopPropagation) { 
	  e.stopPropagation(); 
	 } else if (window.event) { 
	  window.event.cancelBubble = true; 
	 }
	var classCode = treeNode.classCode;
	if(classCode == ""){
		asyncbox.tips('根类不允许删除');
		return;
	}

	var wino = parent.frames['mainTreeFrame'].contentWindow;
	if(wino==undefined){
		wino = parent.frames['mainTreeFrame'];
	}
	wino.asyncbox.confirm('是否删除此目录！删除后将不可恢复','提示',function(action){ 
		if(action == 'ok'){
			var urls = "/TreeAction.do?opType=delete";
			jQuery.ajax({
				type:"POST",
				url:urls,
				data:"tableName=$!tableName&classCodeField="+classCode,
				success: function(msg){
					if(msg == "OK"){
						wino.asyncbox.tips('删除成功！','success');
						reload();
					}else{
						wino.asyncbox.tips(msg,'error');	
					}
				}
			});
		}
	});
}



//单击事件用于展开树
function zTreeOnClick(event, treeId, treeNode){
	if(treeNode!=null){
		var srcNode = zTree1.getSelectedNode();
		if (srcNode) {
			if(treeNode.open){
				zTree1.expandNode(srcNode, false, false);
			}else{
				zTree1.expandNode(srcNode, true, false);
			}
		}	
	}
}
/* 树双击事件 */
function zTreeOnDBlClick(event, treeId, treeNode) {
	if(treeNode!=null){
		dbclassCode = treeNode.classCode;				
		name = treeNode.name;
		if(parent.frames['mainTreeFrame'].contentWindow){
			if(parent.frames['mainTreeFrame'].contentWindow.doChangeParent){
				parent.frames['mainTreeFrame'].contentWindow.doChangeParent(dbclassCode,name,'$tableName');
			}else{
				parent.frames['mainTreeFrame'].contentWindow.location.href="/UserFunctionQueryAction.do?tableName=$!tableName&parentCode="+dbclassCode+
						"&operation=$globals.getOP("OP_QUERY")&moduleType=$!moduleType&winCurIndex=$!winCurIndex&parentTableName=$!parentTableName&checkTab=Y&selectType=normal";
			}
		}else{
			if(parent.frames['mainTreeFrame'].doChangeParent){
				parent.frames['mainTreeFrame'].doChangeParent(dbclassCode,name,'$tableName');
			}else{
				parent.frames['mainTreeFrame'].location.href="/UserFunctionQueryAction.do?tableName=$!tableName&parentCode="+dbclassCode+
						"&operation=$globals.getOP("OP_QUERY")&moduleType=$!moduleType&winCurIndex=$!winCurIndex&parentTableName=$!parentTableName&checkTab=Y&selectType=normal";
			}
			
		}
	}
}
function doRoot(){
	if(parent.frames['mainTreeFrame'].contentWindow){
		parent.frames['mainTreeFrame'].contentWindow.doChangeParent("",'','$tableName');
	}else{
		parent.frames['mainTreeFrame'].doChangeParent("",'','$tableName');
	}
}

function clone(jsonObj, newName) {
    var buf;
    if (jsonObj instanceof Array) {
        buf = [];
        var i = jsonObj.length;
        while (i--) {
            buf[i] = clone(jsonObj[i], newName);
        }
        return buf;
    }else if (typeof jsonObj == "function"){
        return jsonObj;
    }else if (jsonObj instanceof Object){
        buf = {};
        for (var k in jsonObj) {
	        if (k!="parentNode") {
	            buf[k] = clone(jsonObj[k], newName);
	            if (newName && k=="name") buf[k] += newName;
	        }
        }
        return buf;
    }else{
        return jsonObj;
    }
}
function recSearch(kw,oldnd,newnd){
	for(var i=0;oldnd != null && oldnd != undefined && i<oldnd.length;i++){
		od = oldnd[i];
		if(od.name.indexOf(kw)> -1){
			newnd[newnd.length] = od;
		}else{
			recSearch(kw,od.nodes,newnd);
		}
	}
}

function insertkeyword(){
	kw = $("#keyWord").val();
	if(kw == ""){
		reloadTree(zNodes);
	}else{
		//查询树
		var newnd = [{classCode:"",name:"分类",nocheck:true,nodes:[]}];
		recSearch(kw,zNodes,newnd[0].nodes);
		reloadTree(newnd);
	}
}

function startEdit(){
	var xSetting = zTree1.getSetting();
	xSetting.editable= true,
	xSetting.edit_addBtn = true;
	xSetting.edit_renameBtn = true;
	xSetting.edit_removeBtn = true;
	zTree1.updateSetting(xSetting);
	alert("目录编辑模式已打开，请用鼠标移动至目录树上进行添加修改删除操作");
}
</script>
<style type="text/css">
<!--

 .ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
body {	margin: 0px;padding: 0px;font-size: 12px;background: #fff;}
ul, li, ol {list-style: none;margin: 0px;padding: 0px;}
.tree li button.ico_docu {background: url(/js/ztree/zTreeStyle/img/folder_Close.gif);}
.LeftMenu_list,.LeftMenu_list li,.LeftMenu_list li ul,.LeftMenu_list li ul li{ width:100%; float:left;}
.LeftMenu_list li{ background:url(/style1/images/workflow/leftMenu_bg.gif) repeat-x; line-height: 27px;padding-left: 10px; border-bottom:1px solid #b4b4b4;}
.LeftMenu_list li ul{ border-top:2px solid #e9e9e9; float:left; width:100%; margin-bottom:30px; }
.LeftMenu_list li ul li{ background:#fff; border:0px; margin-left:20px; width:auto; float:none; height:24px; line-height:24px;}
.leftMenu_bgB {background: url(/style/images/left_menu_bg.jpg) no-repeat 0px 0px;height: 26px;line-height: 26px;text-align: left;}
.search_button {height: 24px;vertical-align: middle;border: 0px;border-top: 1px solid #bbbbbb;border-right: 1px solid #bbbbbb;border-bottom: 1px solid #bbbbbb;width: 20px;background: url(/style1/images/oaimages/search2.gif) center;}
.search_text {outline: 0;padding: 0px;height: 22px;line-height: 20px;border: 0px;vertical-align: middle;border-top: 1px solid #bbbbbb;border-left: 1px solid #bbbbbb;border-bottom: 1px solid #bbbbbb;width: 100px;}
.refreshbt {height: 24px;vertical-align: middle;border: 0px;border-top: 1px solid #bbbbbb;border-right: 1px solid #bbbbbb;border-bottom: 1px solid #bbbbbb;width: 40px;}


-->
</style>
</head>
<body onload=' ' style="padding:0 0 0 10px;overflow:hidden" >
	<div style="padding:5px 0 0 5px; width:99%">
		<input type="text" id="keyWord" name="keyWord" class="search_text" value="目录搜索" onkeydown="if(event.keyCode==13) insertkeyword();" onblur="if(this.value=='')this.value='目录搜索';" onfocus="if(this.value=='目录搜索'){this.value='';}"><input type="button" class="search_button" onclick="insertkeyword();">
		<input type="button" class="refreshbt" value="刷新" onclick="window.reload()"/>
	</div>
	<p class="leftMenu_bgB" style="margin:10px 0 0 0;width:200px">
		<span style="padding-left: 20px; "><img src="/vm/oa/directorySetting/images/fiI.gif">&nbsp;目录结构
		#if("$!editable"== "true"&& $hasRight)
		<!-- <a style="float:right;margin:0px 55px 0px 0px;" href="javascript:startEdit()">编辑目录</a>  -->
		#end 
		</span>
	</p>
	<div style="overflow:auto;float:left;width:100%;padding-top: 5px;" id="trees">
		<script type="text/javascript"> 
			var oDiv=document.getElementById("trees");
			var sHeight=document.documentElement.clientHeight-80;
			oDiv.style.height=sHeight+"px";
		</script>
		<div class="zTreeDemoBackground">
			<ul id="treeDemo" class="tree"></ul>
										</div>	
	</div>
	<!-- 添加 Start -->
<div style="width:300px;height:200px;border:1px #6ba9df solid;background:#fff;margin:-400px 0px 0px 150px;z-index: 99;display: block;position:absolute;" id="addBranch">
    <ul>
    	<li style="margin:12px 0">
        	<span style="width:120px;text-align:right;display:inline-block;">上级分类$!tableDisplay_bak：</span>        	
        	<input type="text" id="topName" style="border:1px #d9d9d9 solid;width:180px;height:20px;" name="topName" value="" disabled="disabled"/>
        </li>
        #if("$!numberField_bak" != "")
        <li style="margin:12px 0">
        	<span style="color:red;width:120px;text-align:right;display:inline-block;">$!numberDisaplay：</span>
        	<input type="hidden" id="numberFieldName" name="numberFieldName" value="$!numberField"/>
            <input type="text" id="numberField" style="border:1px #d9d9d9 solid;width:180px;height:20px;" name="numberField" value=""/>
        </li>
        #end
        <li style="margin:12px 0">
        	<span style="color:red;width:120px;text-align:right;display:inline-block;">$!nameDisaplay_bak 分类：</span>
        	<input type="hidden" id="nameFieldName" name="nameFieldName" value="$!nameField"/>
            <input type="text" id="nameField" style="border:1px #d9d9d9 solid;width:180px;height:20px;" name="nameField" value=""/>
        </li>
    </ul>
</div>
<!-- 添加 End -->
	
</body>

</html>