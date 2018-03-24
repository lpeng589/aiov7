<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("scope.title.scopeAdmin")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link rel="stylesheet" href="/style1/css/tab.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<script language="javascript">


function addmain(){
	var systype="$!type";
	
	asyncbox.open({
 		id:'AddUpdatediv',
		title : '[角色:$!roleName]&nbsp;&nbsp;部门管辖范围-添加',
		url :"/RoleAction.do?roleId=$roleId&roleName=$!globals.encode($!roleName)&change=yes&operation=$globals.getOP("OP_SCOPE_RIGHT_ADD_PREPARE")&winCurIndex=$!winCurIndex&type=$!type&fromUser=$!fromUser",
		width : 800,
		height :500,
		btnsbar : jQuery.btn.OKCANCEL,
		callback : function(action,opener){
	  		if(action == 'ok'){
	  			if(!opener.save()){
	  				return false;
	  			}
	  		}
  	  }
	});

}

function updatemain(keyId){
	//
	var systype="$!type";
	
	asyncbox.open({
 		id:'AddUpdatediv',
		title : '[角色:$!roleName]&nbsp;&nbsp;部门管辖范围-修改',
		url :"/RoleAction.do?operation=$globals.getOP("OP_SCOPE_RIGHT_UPDATE_PREPARE")&keyId="+keyId+"&roleId=$roleId&roleName=$!globals.encode($!roleName)&winCurIndex=$!winCurIndex&type=$!type&fromUser=$!fromUser",
		width : 800,
		height :520,
		btnsbar : jQuery.btn.OKCANCEL,
		callback : function(action,opener){
	  		if(action == 'ok'){
	  			if(!opener.save()){
	  				return false;
	  			}
	  		}
  	  }
	});

}

function isDelete(m){
	if(confirm('$text.get("oa.common.sureDelete")')){　	
		document.form.operation.value="$globals.getOP("OP_SCOPE_RIGHT_DELETE")";
		document.form.keyId.value=m;
		document.form.submit();
	}
}

function saveDept(){ 
		
	dt = $("input[name='deptRightType']:checked",document).val();	
	
	if(dt != "3"){ 
		document.form.operation.value="$globals.getOP("OP_SCOPE_RIGHT_UPDATE")";
		document.form.submit();
	}else{
		if($("#tblSort",document).find("tr").size() <= 1){
			alert("您还未添加高级设置");
		}else{
			parent.jQuery.close('setScopediv'); 
		}
	}
}

function deptRightTypeChange(type){
	if(type==0){
		$("#deptRightTypeDiv1",document).hide();
		$("#deptRightTypeDiv2",document).hide();
		$("#deptRightTypeDiv3",document).hide();
	}else if(type==1){
		$("#deptRightTypeDiv1",document).show();
		$("#deptRightTypeDiv2",document).hide();
		$("#deptRightTypeDiv3",document).hide();
	}else if(type==2){
		$("#deptRightTypeDiv1",document).hide();
		$("#deptRightTypeDiv2",document).show();
		$("#deptRightTypeDiv3",document).hide();
	}else if(type==3){
		$("#deptRightTypeDiv1",document).hide();
		$("#deptRightTypeDiv2",document).hide();
		$("#deptRightTypeDiv3",document).show();
	}
}

$(document).ready(function() {
		//添加
		dt = $("input[name='deptRightType']:checked",document).val();
		deptRightTypeChange(Number(dt));
});

</script>
</head>

<body onLoad="showtable('tblSort');showStatus();">
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/RoleAction.do" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP('OP_SCOPE_RIGHT_QUERY')">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
<input type="hidden" name="roleId" value="$!roleId">
<input type="hidden" name="winCurIndex" value="$!winCurIndex">
<input type="hidden" name="roleName" value="$!roleName">
<input type="hidden" name="type" id="type" value="$!type">
<input type="hidden" name="departPubSet" id="departPubSet" value="true">
<input type="hidden" name="keyId" value="">
<input type="hidden" name="fromUser" value="$!fromUser">


<div class="Heading" id="head" >
	<div style="padding-left:40px;">
		<div><input type="radio" name="deptRightType" id="deptRightType0" #if("$!deptRightType"=="" || "$!deptRightType"=="0") checked #end  value="0" onclick="deptRightTypeChange(0)"><label for="deptRightType0">取消部门管辖范围</label></div>
		<div><input type="radio" name="deptRightType" id="deptRightType1" #if("$!deptRightType"=="1") checked #end value="1" onclick="deptRightTypeChange(1)"><label for="deptRightType1" >管辖本人所在部门包括下级部门</label>
		<div id="deptRightTypeDiv1" style="float:right;padding-right:150px">
			<div style="float:left;width:80px;">授与权限：(</div>
			<div style="float:left;width:60px;"><input type="checkbox" name="deptRightTypeQuery1" id="deptRightTypeQuery1"  checked disabled=true value="true"><label for="deptRightTypeQuery1">查询</label></div>
			<div style="float:left;width:60px;"><input type="checkbox" name="deptRightTypeUpdate1" id="deptRightTypeUpdate1" #if("$!deptRightTypeUpdate" !="0" ) checked #end  value="1"><label for="deptRightTypeUpdate1">修改</label></div>
			<div style="float:left;width:60px;"><input type="checkbox" name="deptRightTypeDelete1" id="deptRightTypeDelete1" #if("$!deptRightTypeDelete" !="0" ) checked #end  value="1"><label for="deptRightTypeDelete1">删除)</label></div>
		</div>
		</div>
		<div><input type="radio" name="deptRightType" id="deptRightType2" #if("$!deptRightType"=="2") checked #end value="2" onclick="deptRightTypeChange(2)"><label for="deptRightType2">管辖全公司</label>
		<div id="deptRightTypeDiv2" style="float:right;padding-right:150px">
			<div style="float:left;width:80px;">授与权限：(</div>
			<div style="float:left;width:60px;"><input type="checkbox" name="deptRightTypeQuery2" id="deptRightTypeQuery2"  checked disabled=true value="true"><label for="deptRightTypeQuery2">查询</label></div>
			<div style="float:left;width:60px;"><input type="checkbox" name="deptRightTypeUpdate2" id="deptRightTypeUpdate2" #if("$!deptRightTypeUpdate" !="0" ) checked #end  value="1"><label for="deptRightTypeUpdate2">修改</label></div>
			<div style="float:left;width:60px;"><input type="checkbox" name="deptRightTypeDelete2" id="deptRightTypeDelete2" #if("$!deptRightTypeDelete" !="0" ) checked #end  value="1"><label for="deptRightTypeDelete2">删除)</label></div>
		</div>
		</div>
		<div><input type="radio" name="deptRightType" id="deptRightType3" #if("$!deptRightType"=="3") checked #end value="3" onclick="deptRightTypeChange(3)"><label for="deptRightType3">高级设置</label></div>
	</div>
	<ul class="HeadingButton">
		
		</ul>
</div>
		<div class="scroll_function_small_a" id="conter" style="display: block;" >
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-40;
oDiv.style.height=sHeight+"px";
</script>	
	<div id="deptRightTypeDiv3" style="padding-left:5px;">
	
		<ul class="HeadingButton">
			<li id="add"  style="display:block;" ><button type="button" onClick="addmain();" class="b2">$text.get("common.lb.add")</button></li>		
		</ul>
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
				<thead>
					<tr>
						<td width="200" align="center">部门</td>
						<td width="*" align="center">应用于模块</td>
						<td width="90" align="center" class="oabbs_tc">$text.get("com.lb.operation")</td>
					</tr>
				</thead>
				
				<tbody>
					#foreach ($row in $result )
						<tr>
							<td >$globals.get($row,4)&nbsp;</td>
							<td >#if("$!globals.get($row,6)"=="1") 所有模块（查询 #if("$!globals.get($row,8)"=="1")修改 #end &nbsp;#if("$!globals.get($row,7)"=="1")删除 #end）  #else  $globals.get($row,2) #end &nbsp;</td>
							<td class="oabbs_tc">
								<a href='javascript:updatemain("$globals.get($row,0)");'>$text.get("common.lb.update")</a>
								<a href='javascript:isDelete("$globals.get($row,0)");'>$text.get("common.lb.del")</a>
							</td>					
						</tr>
					#end
					
				</tbody>
			</table>
			
			<div class="listRange_pagebar">  
			</div>
		</div>	
		</div>
	
</form>
</body>
</html>