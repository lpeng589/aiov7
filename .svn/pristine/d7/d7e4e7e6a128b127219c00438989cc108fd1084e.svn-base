<!DOCTYPE html>
<html class="html">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link type="text/css" rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<link rel="stylesheet" href="/style/css/chooseInfo.css" type="text/css"/>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/listgrid.css" />
<script type="text/javascript" src="/js/k_listgrid.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script src="/js/jquery.js" type="text/javascript"></script>
<script src="/js/jquery.cookie.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript" src="/js/ztree/demoTools.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>

<style type="text/css">
.tree span{
	//width:100px;
	overflow:hidden;
	display: inline-block;
	white-space:nowrap; 
	text-overflow:ellipsis
}
.tree li a{
	width:200px;
	display: inline-block;
}
</style>
<script type="text/javascript">
var addModule = "false";
var updateModule = "false";
var delModule = "false";
#if($LoginBean.operationMap.get("/EmployeeDepartmentAction.do").add() || $!LoginBean.id=="1")
	addModule = "true";
#end
#if($LoginBean.operationMap.get("/EmployeeDepartmentAction.do").update() || $!LoginBean.id=="1")
	updateModule = "true";
#end
#if($LoginBean.operationMap.get("/EmployeeDepartmentAction.do").delete() || $!LoginBean.id=="1")
	delModule = "true";
#end


var moduleId = "$LoginBean.getOperationMap().get("/EmployeeDepartmentAction.do").moduleId";
var htmlDept = "";
var htmlEmp = "";
var zTree1;
var setting;
var zNodes = $!deptData;
setting = {
	editable: true,
	expandSpeed : false,
	checkType : {"Y":"s", "N":"ps"}, 
	showLine: true,
	callback: {
			dblclick: zTreeOnDblclick,
			click: zTreeOnClick,
	}
};
$(document).ready(function(){
	//加载树


	reloadTree();
	
	//加载职员数据-默认显示在职员工
	$("#frameMain").attr("src","/EmployeeDepartmentAction.do?opType=loadEmployee");
	
	//获取部门
	htmlDept = jQuery("#addBranch").html();
	jQuery("#addBranch").remove();
	
	$(".tagS i").bind("click",function(){
		var status = $(this).attr("status");
		window.location.href = "/EmployeeDepartmentAction.do?status="+status;
	})
	
});


/* 加载树 */
function reloadTree(node) {
	var setting1 = clone(setting);
	setting1.treeNodeKey = "id";
	setting1.treeNodeParentKey = "pId";
	setting1.isSimpleData = true;
	zNodes1 = clone(zNodes);
	setting1.showLine = true;
	setting1.showIcon = false;
	setting1.edit_addBtn = true;
	setting1.edit_renameBtn = true;
	setting1.edit_removeBtn = true;
	zTree1 = $("#treeDemo").zTree(setting1, zNodes1);
	var nodes = zTree1.getNodes();
	if(nodes.length > 0){
		zTree1.expandNode(nodes[0], true,null);
	}
}
/* 双击树节点 */
function zTreeOnDblclick(event, treeId, treeNode){
	//$("#frameMain").attr("src","/EmployeeDepartmentAction.do?opType=loadEmployee&departmentCode="+treeNode.id);
}

/* 单击树节点 */
function zTreeOnClick(event, treeId, treeNode){
	$("#frameMain").attr("src","/EmployeeDepartmentAction.do?opType=loadEmployee&departmentCode="+treeNode.id);
}


/* 添加treeNode的下级部门 */
function addData(treeNode){
	var treeId = treeNode.id;
	var treeName = treeNode.name;
	if(treeId == "0"){
		treeId = "";
	}
	treeName = treeName.substring(treeName.indexOf('-')+1);
	accnumber(treeId,'tblDepartment','deptCode');
	showDept('1',treeId,treeName,number,''); 
}

/* 获取最新的编号 */
function accnumber(parentCode,tableName,fieldName){
	jQuery.ajax({
		type:"POST",
		async: false,
		url:"/EmployeeDepartmentAction.do?opType=queryAccnumber",
		data:"tableName="+tableName+"&fieldName="+fieldName+"&parentCode="+parentCode,
		success: function(msg){
			number = msg;
		}
	});
}

/* 修改treeNode部门 */
function editData(treeNode){
	var name = treeNode.name;
	name = name.substring(name.indexOf('-')+1);
	var parentName = "";
	if(treeNode.parentNode && treeNode.parentNode.name)
		parentName = treeNode.parentNode.name;
	parentName = parentName.substring(parentName.indexOf('-')+1);
	showDept('2',treeNode.id,parentName,treeNode.number,name,treeNode.statusId); 
}

/* 删除treeNode部门 */
function removeData(treeNode){
	asyncbox.confirm('是否删除此部门！','提示',function(action){
		if(action == 'ok'){
			var urls = "/EmployeeDepartmentAction.do?opType=delDepartment&operation=3";
			jQuery.ajax({
				type:"POST",
				url:urls,
				data:"classCode="+treeNode.id,
				success: function(msg){
					if(msg == "OK"){
						asyncbox.tips('删除成功！','success');
						reload();
					}else if(msg == "isEmp"){
						asyncbox.tips('删除的部门中存在职员，请先删除职员！','error');	
					}else if(msg == "isDept"){
						asyncbox.tips('删除的部门中存在下级部门，请先删除下级部门！','error');	
					}else{
						asyncbox.tips('删除失败！','error');	
					}
				}
			});
		}
	});
}

/* 按钮添加部门 */
function adddept(){
	var selectedNode = zTree1.getSelectedNode();
	var treeId = "";
	var treeName = "";
	if(selectedNode !=null && selectedNode.id != "0"){
		treeId = selectedNode.id;
		treeName = selectedNode.name;
		treeName = treeName.substring(treeName.indexOf('-')+1);
	}else{
		#if($!globals.getCompanyName('')=="")
			treeName = "$text.get("com.framework.from")";
		#else
			treeName = "$!globals.getCompanyName('')";
		#end
	}
	accnumber(treeId,'tblDepartment','deptCode');
	showDept('1',treeId,treeName,number,''); 
}

/* 取消选中 */
function cancelNode(){
	zTree1.cancelSelectedNode(false);
}

/*刷新*/
function reload(){
	window.location.reload();
}

/* 添加修改职员 */
function dealOperate(type,id){
	var titles = "添加员工";
	var url = "/EmployeeDepartmentAction.do?operation=6&opType=employeePre&tableName=tblEmployee&fieldName=EmpNumber";
	var selectedNode = zTree1.getSelectedNode();
	if(selectedNode !=null && selectedNode.id != "0"){
		url += "&departmentCode="+selectedNode.id;
	}
	if(type == "2"){
		titles="修改员工";
		url = "/EmployeeDepartmentAction.do?operation=7&opType=updatePreEmployee&id="+id;
	}
	asyncbox.open({
		id:'dealdiv',
		url : url,
		width : 850,
		height: 450,
		modal　: true,
		title : titles,
		btnsbar : jQuery.btn.OKCANCEL,
		callback : function(action,opener){
			if(action == 'ok'){
				if(!opener.sumits()){
					return false;
				}
			}
		}
	});
}

/* 职员详情 */
function detailOperate(id){
	var urls = "/EmployeeDepartmentAction.do?operation=5&opType=detailEmployee&id="+id;
	asyncbox.open({
		id:'detailEmp',
		url : urls,
		width : 850,
		height: 510,
		modal　: true,
		title : "详情",
		btnsbar : jQuery.btn.CANCEL
	});
	jQuery("#form").find("input").attr("readonly","readonly");
	jQuery("#form").find("input[type=radio]").attr("disabled","disabled");
	jQuery("#form").find("select").attr("disabled","disabled");
	jQuery("#form").find("input").removeAttr("onClick").removeAttr("ondblclick");
	jQuery("#form").find("b").removeAttr("onClick");
}




/* 显示部门div */
function showDept(type,classCode,name,number,deptName,statusId){
	var title = "新增部门";
	var optype = "department";
	var btns = jQuery.btn.OKCANCEL;
	if(type != "1"){
		optype = "updateDepartment";
		title = "修改部门";
		if(statusId == "0"){
			btns = [{text:'停用部门',action:'black'}].concat(jQuery.btn.OKCANCEL);
		}else{
			btns = [{text:'启用部门',action:'black'}].concat(jQuery.btn.OKCANCEL);
		}
	}
	asyncbox.open({
		id:'dealDept',
		html : htmlDept,
		width : 300,
		modal　: true,
		title : title,
		btnsbar : btns,
		callback : function(action){
			if(action == 'ok'){
				var deptname = $("#deptname").val();
				var deptCode = $("#deptCode").val();
				if(deptCode == ""){
					asyncbox.tips('部门编号不能为空！','alert');
					$("#deptCode").focus();
					return false;
				}
				if(deptname == ""){
					asyncbox.tips('部门名称不能为空！','alert');
					$("#deptname").focus();
					return false;
				}
				var topDeptCode = $("#topDeptCode").val();
				var topDeptName = $("#topDeptName").val();
				var urls = "/EmployeeDepartmentAction.do?opType="+optype+"&operation="+type;
				jQuery.ajax({
					type:"POST",
					url:urls,
					data:"topClassCode="+topDeptCode+"&deptName="+deptname+"&deptCode="+deptCode,
					success: function(msg){
						if(msg == "OK"){
							asyncbox.tips('保存成功！','success');
							reload();
						}else if(msg == "isEmp"){
							asyncbox.tips('部门存在职员，不允许添加下级！','error');
						}else if(msg == "ERROR"){
							asyncbox.error('保存失败！','提示');
						}else{
							asyncbox.error(msg,'提示');
						}
					}
				});
			}else if(action == "black"){
				var status = "0";
				var staName = "启用部门";
				if(statusId == "0"){
					status = "-1";
					staName = "停用部门";
				}
				var topDeptCode = $("#topDeptCode").val();
				jQuery.ajax({
					type:"POST",
					url: "/EmployeeDepartmentAction.do?opType=setDeptStatus&operation=2&status="+status+"&classCode="+topDeptCode,
					success: function(msg){
						if(msg == "OK"){
							asyncbox.tips(staName+'成功！','success');
							reload();
						}else if(msg.indexOf("ERROR")==0){
							asyncbox.error(msg.substring(5),'提示');
						}else{
							asyncbox.error(staName+'失败！','提示');
						}
					}
				});
			}
		}
	});
	jQuery("#topDeptCode").val(classCode);
	jQuery("#topDeptName").val(name);
	jQuery("#deptCode").val(number);
	jQuery("#deptname").val(deptName);
}

/* 组织架构 */
function framework(){
	mdiwin('/FrameworkAction.do?src=menu','组织架构');
}

/* 保存返回 */
function dealAsyn(type){
	if(type != undefined){
		if(type.indexOf("updateOK")!=-1 || type.indexOf("addOK")!=-1){
			var content = "添加";
			if(type.indexOf("updateOK")!=-1){
				content = "修改";
			}
			asyncbox.success(content+'职员成功！','提示',function(action){
		　　　if(action == 'ok'){
				$("#frameMain").attr("src","/EmployeeDepartmentAction.do?opType=loadEmployee&departmentCode="+type.substring(type.indexOf(";")+1));
				jQuery.close('dealdiv');
		　　　}
		　});
		}else if(type == "updateERROR" || type == "addERROR"){
			var content = type=="updateERROR"?"修改":"添加";
			asyncbox.error(content+'职员失败！','提示');
		}else if(type.indexOf("updateNo")!=-1){
			asyncbox.error(type.substring(type.indexOf(";")+1),'提示');
		}
	}else{
		jQuery.opener('dealdiv').updateEnumer(type);
	}
}

/* 选项数据回填方法 */
function filllanguage(datas){
	jQuery.opener('EnumWinow').filllanguage(datas);
}

</script>
</head>
<body class="mg-t">
<iframe name="formFrame" style="display:none"></iframe>
<div class="main">
	<div class="mainLeft">
    	<div class="topL">
        	<span onclick="framework()" title="进入组织架构">组织架构</span>
        	<div class="tagS rf">
                 <span class="lf pr wp-status" status="$!status">
                 	#if("$!status"=="0")
						<i status="0" title="显示启用的部门">启用</i>
					#else
						<i status="-1" title="显示停用的部门">停用</i>
                 	#end
					<b class="triangle"></b>
					<div class="other-status">
						#if("$!status"=="0")
							<i status="-1" title="显示停用的部门">停用</i>
						#else
							<i status="0" title="显示启用的部门">启用</i>
	                 	#end
					</div>
				</span>
             </div>
        </div>
        <div class="treeMenu" id="treeDiv" >
        	<ul id="treeDemo" class="tree" style="overflow-x: scroll; overflow-y: auto; height: 410px;margin:0 0 0 10px;"></ul>
        </div>
    </div>
    <div class="mainRight">
    	<iframe id="frameMain" frameborder=false src="" name="frameMain" style="margin-top:10px;" scrolling="no" width="100%"></iframe>
    </div>
    <script type="text/javascript"> 
		var oDiv=document.getElementById("frameMain");
		var sHeight=document.documentElement.clientHeight-20;
		oDiv.style.height=sHeight+"px";
	</script>
</div>
<!-- 添加部门 Start -->
<div class="addBranch" id="addBranch">
    <ul class="add-branch-ul">
    	<li>
        	<span>上级部门：</span><input type="hidden" id="topDeptCode" name="topDeptCode" value=""/>
        	<input type="text" id="topDeptName" class="topDeptName" name="topDeptName" value="" disabled="disabled"/>
        </li>
        <li>
        	<span style="color:red;">部门编号：</span>
            <input type="text" id="deptCode" class="deptCode" name="deptCode" value="" placeholder="请填写部门编号"/>
        </li>
        <li>
        	<span style="color:red;">部门名称：</span>
            <input type="text" id="deptname" class="deptname" name="deptname" value="" placeholder="请填写部门名称"/>
        </li>
    </ul>
</div>
<!-- 添加部门 End -->
</body>
</html>

