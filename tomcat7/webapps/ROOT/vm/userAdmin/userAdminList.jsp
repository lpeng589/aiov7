<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统用户管理</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style1/css/ListRange.css"/>
<link type="text/css" rel="stylesheet" href="/style1/css/oa_news.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/roleAdmin.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/popOperation.css" />
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<link type="text/css" rel="stylesheet" href="/style1/css/fg.menu.css" media="screen"/>
<style type="text/css">
.opbt{background:none;}
#viewOpDiv{z-index: 99; position: absolute; left:40px; top:15px; display: none;}
.custom-menu li a{white-space:nowrap;}
.custom-menu li:hover {line-height: 19px!important;height: 19px!important;border: 1px solid #f47c02!important;background: #fa9b3d;}
.custom-menu li:hover a{color: #fff;}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/fg.menu.js"></script>
<script type="text/javascript" src="/js/popOperationNew.js"></script>
<script type="text/javascript">

$(document).ready(function() {
	//添加
	$("#add").bind("click", function(){
		location.href='/UserManageAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&winCurIndex=$!winCurIndex';
	});
	$("#batchdelete").bind("click", function(){
		var sd = sureDel('keyId'); 
		if(sd){document.form.submit();}
	});
	$(".tdimg").mouseover( function() {  $(this).find("img").show(); } ); 
	$(".tdimg").mouseout( function() { $(this).find("img").hide(); } ); 
	$("#roleMag").bind("click", function(){
		mdiwin('/RoleQueryAction.do?src=menu','权限分组');
	});
	
});

function sureDel(itemName){
	if(delHasChild(itemName)){
		alert('$text.get("common.exist.childcategory")');
		return false;
	}
	if(!isChecked(itemName)){
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	form.operation.value=$globals.getOP("OP_DELETE");
	if(!confirm('$text.get("common.msg.confirmDel")')){
		form.operation.value = $globals.getOP("OP_QUERY");
		cancelSelected("input");
		return false;
	}else{
		return true;
	}
}

function copyOpOK(sourceid,roleName,roleDesc){	
	var urls = "/Accredit.do?popname=userGroup&inputType=checkbox&condition=openFlagNoAdmin";
	titles = "请选择职员"
	
	asyncbox.open({
		 id : 'Popdiv',
	 	 title : titles,
	　　　url : urls,
	　　　width : 755,
	　　　height : 435,
		 top: 5,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。  
　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。 
				var employees = opener.strData;
				if(employees == ""){
					alert("目标用户不能为空");
					return;
				}
				emps  = employees.split("|");
				empstr = "";
				for(i=0;i<emps.length;i++){
					eid =emps[i].split(";")[0];
					if(eid !=""){
						if(eid!=sourceid){
							empstr +=eid+";";
						}else{
							alert("目标用户不能包括原用户");
							return;
						}
					}
				}
				if(empstr == ""){
					return;
				}
				jQuery.get("/UserQueryAction.do?operation=$globals.getOP("OP_UPDATE")&copyRight=true&sourceId="+sourceid+"&dirId="+empstr, 
				function(data){ alert(data); location.reload(); }); 
　　　　　	}
　　　	}
　	});
}	
//另存为






function copyOp(id,roleName,roleDesc){
	
	　asyncbox.confirm('将会复制所有的用户权限分组，模块权限，各种管辖范围权限至目标用户，且目标用户原有的权限设置将会被覆盖，是否继续？','复制确认',function(action){
	　　　if(action == 'ok'){
	　　　　　copyOpOK(id,roleName,roleDesc);
	　　　}
	　});
	return ;
}

//各种范围权限弹出窗的回调关闭函数
function dealAsyn(act){
	//当删除范围权限时，只需刷新界面
	if(act=="refresh"){
		jQuery.reload('setScopediv'); 
	}else{
		if(jQuery.exist('AddUpdatediv')){
			jQuery.close('AddUpdatediv');
			jQuery.reload('setScopediv'); 
		}else if(jQuery.exist('setScopediv')){
			jQuery.close('setScopediv'); 
		}
	}
	if(jQuery.exist('dealdiv')){
		jQuery.close('dealdiv'); 
	}
	
}

//设置各种范围权限
function setScope(type,id,roleName,tableName,tableDisplay){
	tit = '';
	opDiv= '';
	height=document.documentElement.clientHeight-50;
	if(type=="5"){ //部门管辖范围有确定按扭，其它没有，所以单独列出  
		tit = "部门管辖范围";
		asyncbox.open({
	 		id:'setScopediv',
			title : '设置[用户:'+roleName+']&nbsp;&nbsp;'+tit,
			url :'/RoleAction.do?operation=31&method=scopeMain&roleId='+id+'&winCurIndex=&!winCurIndex&roleName='+roleName+'&type='+type+'&fromUser=true',
			width : 800,
			height :height,
			btnsbar : jQuery.btn.OKCANCEL,
			callback : function(action,opener){
		  		if(action == 'ok'){
		  			opener.saveDept();
		  			return false;
		  		}
	  	  }
		});		
		return;
	}else if(type=="1"){
		tit = "职员管辖范围";
	}else if(type=="0"){
		tit = tableDisplay;
	}else if(type=="4"){
		tit = "单据列控制";
	}else if(type=="6"){
		tit = "查看范围值控制";
	}
	
	asyncbox.open({
 		id:'setScopediv',
		title : '设置[用户:'+roleName+']&nbsp;&nbsp;'+tit,
		url :'/RoleAction.do?operation=31&method=scopeMain&roleId='+id+'&winCurIndex=&!winCurIndex&roleName='+roleName+'&type='+type+'&tableName='+tableName+'&fromUser=true',
		width : 800,
		height :height,
		btnsbar : jQuery.btn.CANCEL,
		callback : function(action,opener){
	  		
  	  	}
	});		
	
}

//关键字搜索  
function doquery(){
	document.form.winCurIndex.value='$!winCurIndex'; 
	document.form.operation.value=$globals.getOP("OP_QUERY");
	beforeBtnQuery();
	document.form.submit();
}
function openSelect(){
	popname="deptGroup";
	var urls = "/Accredit.do?popname=" + popname + "&inputType=checkbox";
	var titles = "请选择部门";		
	asyncbox.open({
		 id : 'Popdiv',
	 	 title : titles,
	　　　url : urls,
	　　　width : 755,
	　　　height : 435,
		 top: 5,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。  
　　　　　	if(action == 'ok'){
				var employees = opener.strData;
				ems = employees.split("|");
				deps = "";
				for(i=0;i<ems.length;i++){
					if(ems[i] != ""){
						deps +=ems[i].split(";")[1]+";";
					}
				}
				$("#departMent").val(deps);
　　　　　	}
　　　	}
　	});
}

</script>
</head>
<body class="bg-html">
<iframe name="formFrame" style="display:none"></iframe>
<form class="wp-form" method="post" scope="request" name="form" action="/UserQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
 <input type="hidden" name="type" value="$type">
 <div class="TopTitle" >
	<ul class="t-ul-search">
		<li>
			<label>$text.get("userManager.lb.userName")</label>
			<input class="inp" onkeydown="if(event.keyCode==13){doquery()}" name="name" type="text" value="$!userSearchForm.getName()" maxlength="50">
		</li>
		<li>
			<label>$text.get("user.lb.sysName")</label>
			<input class="inp" onkeydown="if(event.keyCode==13){doquery()}" name="sysName" type="text" value="$!userSearchForm.getSysName()" maxlength="50">
		</li>
		<li>
			<label>$text.get("oa.dept.name")</label> 
			<input class="inp h-icon" onkeydown="if(event.keyCode==13){doquery()}" onDblClick="openSelect()" type="text"  id="departMent" name="departMent" value="$!userSearchForm.departMent">
			<b class="stBtn icon16" onClick="openSelect();"></b>
		</li>
		<li>
			<label>$text.get("role.lb.roleName")</label>
			<input class="inp" onkeydown="if(event.keyCode==13){doquery()}" name="roleName" type="text" value="$!userSearchForm.roleName" maxlength="50">
		</li>
		<li>
			<label>职位</label>
			<select class="slt" name="titleid">
				<option value=""></option>
				#foreach($er in $!globals.getEnumerationItems("duty",$!globals.getLocale()))
				<option value="$er.value" #if("$!userSearchForm.getTitleid()"=="$er.value") selected #end>$er.name</option>
				#end
			</select>
		</li>
		<span class="t-search_btn" onclick="doquery();" title="查询">查询</span>
	</ul>	
	<div class="t-d-btns">							
		#if($LoginBean.operationMap.get("/UserQueryAction.do").add())
		<span class="hBtns" id="add" title="$text.get("common.lb.add")">$text.get("common.lb.add")</span>
		#end						
		#if($LoginBean.operationMap.get("/UserQueryAction.do").delete())
		<span class="hBtns" id="batchdelete" title="删除">删除</span>							
		#end
		<span class="hBtns" id="roleMag" title="权限分组管理">
			权限分组
		</span>	
	</div>				
</div>	
	<ul class="head-ul-list">
		<li class="cbox-li">
			<input class="cbox" type="checkbox" name="selAll" onClick="checkAll('keyId');" />
		</li>
		<li class="ope-li">操作</li>
		<li class="num-li">$text.get("iniAcc.lb.iniAcc.lb.sequencenumber")</li>
		<li class="off-li">职员信息</li>
		<li>$text.get("user.lb.sysName")</li>
		<li class="fun-li">功能模块</li>
		#if($globals.getSysSetting("sunCompany")=="true")
		<li>$text.get("sunCompany.lb.sunCompany")</li>
		#end
		<li class="gro-li">$text.get("role.lb.roleName")</li>
	</ul>
	<div id="data_list_id" style="overflow-y:auto;overflow-x:hidden;width:100%;" >
		<script type="text/javascript"> 
			$("#data_list_id").height(document.documentElement.clientHeight-125);
		</script>	
		#foreach ($row in $result ) 
		<ul class="body-ul-list">
			<li class="cbox-li">
				#if("1" != "$globals.get($row,0)")
				<input class="cbox" type="checkbox" name="keyId" value="$globals.get($row,0)" />
				#else 
				&nbsp; 
				#end
			</li>
			<li class="ope-li opbt" #if($LoginBean.operationMap.get("/UserQueryAction.do").update())
									f1="修改:/UserManageAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId=$globals.get($row,0)&type=$type&winCurIndex=$!winCurIndex"
									#end
									#if("1" != "$globals.get($row,0)")
									f7="复制权限:javascript:void(0);copyOp('$globals.get($row,0)','$!globals.encodeHTMLLine($globals.get($row,1))','$!globals.encodeHTMLLine($globals.get($row,2))')"
									f8="分配模块权限:/RoleAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId=$globals.get($row,0)&roleName=$globals.urlEncode($globals.get($row,1))&fromUser=true&winCurIndex=$!winCurIndex"
									f9="部门管辖范围:javascript:void(0);setScope('5','$globals.get($row,0)','$!globals.encodeHTMLLine($globals.get($row,1))')"
									f10="职员管辖范围:javascript:void(0);setScope('1','$globals.get($row,0)','$!globals.encodeHTMLLine($globals.get($row,1))')"
									#foreach ($scrow in $scopeCata )
									#set($svc = $velocityCount+11)
									f$svc="$globals.get($scrow,3).get($globals.getLocale())管辖范围:javascript:void(0);setScope('0','$globals.get($row,0)','$!globals.encodeHTMLLine($globals.get($row,1))','$globals.get($scrow,0)','$globals.get($scrow,3).get($globals.getLocale())管辖范围')" 
									#end
									#if($!globals.hasMainModule("1"))
									f19="单据列控制:javascript:void(0);setScope('4','$globals.get($row,0)','$!globals.encodeHTMLLine($globals.get($row,1))')"
									f20="查看范围值控制:javascript:void(0);setScope('6','$globals.get($row,0)','$!globals.encodeHTMLLine($globals.get($row,1))')"
									#end
									#end>
				<span>操作<b class="triangle"></b></span>
			</li>
			<li class="num-li">$velocityCount</li>
			<li class="off-li">
				<p class="p-name" title='#if("1" ==$globals.get($row,0)) $!defaultSunCmpName #end $globals.get($row,1)'>
					#if("1" ==$globals.get($row,0)) $!defaultSunCmpName #end $globals.get($row,1)
				</p>
				<p class="p-post" title='$globals.getEnumerationItemsDisplay("duty",$globals.get($row,4))'>
					$globals.getEnumerationItemsDisplay("duty",$globals.get($row,4))
				</p>
				<p class="p-dep" title='$globals.get($row,2)'>
					$globals.get($row,2)
				</p>
			</li>
			<li class="def-li">$globals.get($row,3)</li>
			<li class="fun-li">
				<div class="d-short">
					#if("$globals.get($row,5)"=="1") ERP-进销存财务 #end 
					#if("$globals.get($row,6)"=="1") OA-办公自动化 #end
					#if("$globals.get($row,7)"=="1") CRM-客户关系管理 #end
				</div>
				<div class="d-long">
					#if("$globals.get($row,5)"=="1") ERP-进销存财务 #end 
					#if("$globals.get($row,6)"=="1") OA-办公自动化 #end
					#if("$globals.get($row,7)"=="1") CRM-客户关系管理 #end
				</div>
			</li>
			#if($globals.getSysSetting("sunCompany")=="true")
			<li>
		  	#foreach($sunCompany in $sunCompanys)
		   		#if($globals.get($row,0)== $globals.get($sunCompany,3))
		  		$globals.get($sunCompany,1)
			 	#end
			#end
			</li>					
			#end
			<li class="gro-li">
				<div class="d-short">
					#if("1" == $row.id)&nbsp;#else $globals.get($roleName.get($globals.get($row,0)),1)#end
				</div>
				<div class="d-long">
					#if("1" == $row.id)&nbsp;#else $globals.get($roleName.get($globals.get($row,0)),1)#end
				</div>
			</li>
		</ul>
		#end
	</div>
	<div style="overflow:hidden;">
		<div class="listRange_pagebar">
			$!pageBar
		</div>	
	</div>
</form>
</body>
</html>
