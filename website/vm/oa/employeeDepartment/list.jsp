<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>
<link type="text/css" href="/js/skins/ZCMS/asyncbox.css" rel="stylesheet" />
<link type="text/css" href="/style/css/chooseInfo.css" rel="stylesheet" />
<link type="text/css" href="/js/jBox/jbox.css"  rel="stylesheet" />
<link type="text/css" href="/js/tree/jquery.treeview.css" rel="stylesheet" /> 
<link type="text/css" rel="stylesheet" href="/style/css/popOperation.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/jquery.cookie.js"></script>
<script type="text/javascript" src="/js/tree/jquery.treeview.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script src="/js/jBox/jquery.jBox-2.3.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/jBox/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="/js/popOperation.js"></script>
<style type="text/css">

.treeMenu{padding-left: 10px;}
.treeMenu a{color:black;}
.treeMenu span{padding: 0 0 0 10px}
.tblInfo thead tr td{font-weight: bold;}

a:link{text-decoration: none;color:#000000;}
a:hover{text-decoration: underline;}
#viewOpDiv a{color:black;}
</style>
<script type="text/javascript"> 

function showEmp(type){
	if(type == 'leave'){
		$("#leave_li").addClass("sel");
		$("#in_li").removeClass("sel");
		$("#employeeType").val('leave');
	}else{
		$("#in_li").addClass("sel");
		$("#leave_li").removeClass("sel");
		$("#employeeType").val('');
	}
	form.submit();
}

/* 关键字搜索 */
function keywords(){
	$("#departmentCode").val('');
	$("#employeeType").val('');
	parent.cancelNode();
	form.submit();
}

/* 关闭部门div */
function closes(){
	$(".addBranch").hide();
}

/* 添加职员 */
function addOperate(){
	parent.dealOperate('1','');
}

function submitQuery(){
	form.submit();
}

function pageSubmit(pageNo){
	$("#pageNo").val(pageNo);
	form.submit();
}


/* 离职 */
function dealLeave(type,id){
	var titleName = "你确定设置为离职状态吗？";
	if(type == "0"){
		titleName = "你确定设置为启用状态吗？";
	}
	asyncbox.confirm(titleName,'提示',function(action){
	　　if(action == 'ok'){
			jQuery.ajax({type: "POST",url: "/EmployeeDepartmentAction.do?operation=2&opType=setLeave&id="+id+"&status="+type,async : false,success: function(data){
				if(data == "OK"){
					//asyncbox.success('设置成功！','提示',function(action){
					asyncbox.tips('设置成功！','提示',3000);
					setTimeout(function(){
						form.submit();
				    },500);
				}else if(data == "ERROR"){
					asyncbox.error('设置失败！','提示');
				}
			}});
		}
	});
}

/* 删除职员 */
function delEmp(id){
	asyncbox.confirm('你确定删除职员吗？','提示',function(action){
	　　if(action == 'ok'){
			jQuery.ajax({type: "POST",url: "/EmployeeDepartmentAction.do?operation=3&opType=delEmployee&id="+id,async : false,success: function(data){
				if(data == "OK"){
					form.submit();
				}else if(data == "ERROR"){
					asyncbox.error('删除职员失败！','提示');
				}
			}});
		}
	});
}

/* 设置用户 */
function setEmp(id,openFlag,deptName,empName){
	if(openFlag == "1"){
		//存在进行搜索
		//var urls = '/UserQueryAction.do?winCurIndex=17&paramValue=true&departMent='+encodeURI(deptName+";")+'&name='+encodeURI(empName);
		//mdiwin(urls,'系统用户管理');
		mdiwin('UserManageAction.do?operation=7&winCurIndex=1&keyId='+id,'系统用户管理');
	}else if(openFlag == "0"){
		//不存在进添加界面
		mdiwin('/UserManageAction.do?operation=6&winCurIndex=1&keyId='+id,'系统用户管理');
	}
}

/* 调用修改界面 */
function updateEmp(id){
	parent.dealOperate('2',id);
}

/* 调用详情界面 */
function detailEmp(id){
	parent.detailOperate(id);
}

/* 职员数据导入 */
function importEmp(){
	mdiwin('importDataAction.do?fromPage=importList&operation=91&tableName=tblEmployee&parentTableName=&moduleType=&winCurIndex=5&NoBack=NoBack','数据导入')
}

/* 部门导入 */
function importDept(){
	mdiwin('importDataAction.do?fromPage=importList&operation=91&tableName=tblDepartment&parentTableName=&moduleType=&winCurIndex=5&NoBack=NoBack','数据导入')
}

/* 导出 */
function exportData(tableName,type){
	if(tableName != "tblDepartment" && type == "1"){
		if((typeof(selected)=="undefined" && !isChecked("keyId")) 
			|| (typeof(selected)!="undefined" && selected && !isChecked("keyId"))){		
			alert('$text.get("common.msg.mustSelectOne")');
			return false;
		}
		if(!confirm("$text.get('common.msg.whetherExport')"))return
	}
	$("#ButtonType").val("billExport");
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	$("#opType").val("exportData");
	$("#tableName").val(tableName);
	if(type != "1"){
		$("#export").val("All");
	}
	form.submit();
}

/* 添加部门 */
function addDept(){
	parent.adddept();
	//var deptName = "";
	//#if($!globals.getCompanyName('')=="")
	//	deptName = "$text.get("com.framework.from")";
	//#else
	//	deptName = "$!globals.getCompanyName('')";
	//#end
	
	//var selectedNode = zTree1.getSelectedNode();
	//var treeId = "";
	//var treeName = "";
	//if(selectedNode !=null && selectedNode.id != "0"){
	//	treeId = selectedNode.id;
	//	treeName = selectedNode.name;
	//}
	//
	//parent.accnumber('','tblDepartment','deptCode');
	//parent.showDept('1','',deptName,parent.number,'');
}


/* 打印职员 */
function prints(){
	printControl("/UserFunctionQueryAction.do?tableName=tblEmployee&reportNumber=tblEmployee&moduleType=&operation=18&parentTableName=&winCurIndex=12");
}

</script>
</head>
<body>
<div id="viewOpDiv" class="custom-menu" style="z-index: 99; position: absolute; left: 100px; top: 20px; display: none;">
    <ul >        
    </ul>
</div>
<form action="/EmployeeDepartmentAction.do" method="post" name="form" id="form"> 
<input type="hidden" id="operation" name="operation" value=""/>
<input type="hidden" id="opType" name="opType" value="loadEmployee" />
<input type="hidden" id="employeeType" name="employeeType" value="$!employeeType"/>
<input type="hidden" id="departmentCode" name="departmentCode" value="$!departmentCode" />
<input type="hidden" id="ButtonType" name="ButtonType" value=""/>
<input type="hidden" id="tableName" name="tableName" value=""/>
<input type="hidden" id="export" name="export" value="" />
<input type="hidden" id="SQLSave" name="SQLSave" value="@SQL:$saveSql@ParamList:@end:" />
<div class="main">
   	<div class="mainRight">
   	<div class="topR">
       	<span style="font-weight:bold;padding-left:10px;">
       	</span>
           <div class="btnItems">
           #if($LoginBean.operationMap.get("/EmployeeDepartmentAction.do").add() || $!LoginBean.id=="1")
           	<span class="pr">
            	<em class="oBtn">&nbsp;&nbsp;&nbsp;新建&nbsp;&nbsp;&nbsp;&nbsp;</em>
            	<ul class="pa c-list-ul">
            		<li class="oBtn" onclick="addOperate();" title="新建职员">新建职员</li>
            		<li class="oBtn" onclick="addDept();" title="新建部门">新建部门</li>
            	</ul>
            </span>
            <span class="pr">
            	<em class="oBtn">导入</em>
            	<ul class="pa c-list-ul">
            		<li class="oBtn" onclick="importEmp();" title="导入职员">职员</li>
            		<li class="oBtn" onclick="importDept();" title="导入部门">部门</li>
            	</ul>
            </span>
           #end
            <span class="pr">
            	<em class="oBtn">导出数据</em>
            	<ul class="pa c-list-ul">
            		<li class="oBtn" onclick="exportData('tblEmployee','1')" title="导出职员">导出职员</li>
            		<li class="oBtn" onclick="exportData('tblEmployee');" title="全部职员">全部职员</li>
            		<li class="oBtn" onclick="exportData('tblDepartment');" title="导出部门">导出部门</li>
            	</ul>
            </span>
            #if($LoginBean.operationMap.get("/EmployeeDepartmentAction.do").print() || $!LoginBean.id=="1")
            <span class="pr">
            	<em class="oBtn" onclick="prints();">打印职员</em>
            </span>
            #end
            <span class="cTxt">
            	<input type="text" placeholder="搜索同事" id="keyword" name="keyword" value="$!keyword" onkeydown="if(event.keyCode==13){keywords();}"/>
                <b onclick="keywords()" title="搜索"></b>
            </span>
           </div>
       </div>
       <div class="infoList">
       	<ul class="uTag">
           	<li #if("$!employeeType"=="") class="sel" #end onclick="showEmp('')" id="in_li">在职职员</li>
            <li #if("$!employeeType"=="leave") class="sel" #end onclick="showEmp('leave')" id="leave_li">离职职员</li>
        </ul>
        <table class="tblInfo" border="0" cellpadding="0" cellspacing="0">
           	<thead>
               	<tr>
                  	  <td width="30" style="text-align: center;"><input type="checkbox" id="selAll" onClick="checkAll('keyId')"/></td>
                      <td width="70" style="text-align: center;"><em style="padding:0 10px 0 0;">操作</em></td>
                      <td width="100">编号</td>
                      <td width="100">姓名</td>
                      <td width="100">所属部门</td>
                      <td width="100">职位</td>
                      <td width="100">直接上司</td>
                      <td width="100">系统用户名称</td>
                      <td width="100">手机号码</td>
                   </tr>
               </thead>
          </table>
          <div id="div_content" style="overflow:hidden;overflow-y:auto;"> 
           <table class="tblInfo" border="0" cellpadding="0" cellspacing="0" style="margin:0px 0 0 0;">
               <tbody>
               #foreach($data in $list)
               <tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);" ondblclick="detailEmp('$!data.id');">
                   	<td width="33" style="text-align: center;"><input type="checkbox" id="keyId" name="keyId" value="$!data.id"/></td>
                    <td width="70" style="text-align: center;">
                    	<div class="operation-wp">
                    		<em>操作</em>
                    		<b class="triangle"></b>
                    		<ul class="operation-ul">
                    			<li onclick="detailEmp('$!data.id');">详情</li>
                    			#if($LoginBean.operationMap.get("/EmployeeDepartmentAction.do").update() || $!LoginBean.id=="1")
                    				<li onclick="updateEmp('$!data.id');">修改</li>
                    			#end
                    			#if("$!employeeType"!="leave")
	                    			#if(($LoginBean.operationMap.get("/UserQueryAction.do").query() && "$!data.id"!="1") || $!LoginBean.id=="1")
	                    				<li onclick="setEmp('$!data.id','$!data.openFlag','$!data.deptFullName','$!data.empFullName')">设置用户</li>
	                    			#end
	                    		#end
                    			#if($LoginBean.operationMap.get("/EmployeeDepartmentAction.do").update() || $!LoginBean.id=="1")
                    				#if("$!data.id"!="1")
		                    			#if("$!data.statusId"=="0")
		                    				<li onclick="dealLeave('-1','$!data.id')">离职</li>
		                    			#else
		                    				<li onclick="dealLeave('0','$!data.id')">启用</li>
		                    			#end
		                    		#end
		                    	#end
                    		</ul>
                    	</div>
                    </td>	
                    <td>$!data.empNumber</td>
                    <td title="$!data.empFullName">$!data.empFullName</td>
                    <td title="$!data.deptFullName">$!data.deptFullName</td>
                    <td>$!data.titleName</td>
                    <td>$!globals.getEmpFullNameByUserId($!data.directBoss)</td>
                    <td>$!data.sysName</td>
                    <td>$!data.mobile</td>
               </tr>
               #end
               </tbody>
           </table>
           </div>
       </div>
       <div class="listRange_pagebar">
			$!pageBar
		</div>	
       <script type="text/javascript"> 
			var oDiv=document.getElementById("div_content");
			var sHeight=document.documentElement.clientHeight-180;
			oDiv.style.height=sHeight+"px";
		</script>
    </div>
</div>
</form>
</body>
</html>

