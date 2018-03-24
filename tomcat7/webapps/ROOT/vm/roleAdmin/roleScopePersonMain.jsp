<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("scope.title.scopeAdmin")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style1/css/tab.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
function addmain(){
	var systype="$!type";
	
	asyncbox.open({
 		id:'AddUpdatediv',
		title : '[角色:$!roleName]&nbsp;&nbsp;职员管辖范围-添加',
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
		title : '[角色:$!roleName]&nbsp;&nbsp;职员管辖范围-修改',
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



$(document).ready(function() {
		//添加
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
<input type="hidden" name="keyId" value="">
<input type="hidden" name="fromUser" value="$!fromUser">


		<div class="scroll_function_small_a" id="conter" style="display: block;" >
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-40;
oDiv.style.height=sHeight+"px";
</script>	
	<div id="deptRightTypeDiv3" style="padding-left:5px;">
	
		<ul class="HeadingButton" style="margin:0;">
			<li id="add"  style="display:block;padding:5px 0;" ><span onClick="addmain();" class="hBtns">$text.get("common.lb.add")</span></li>		
		</ul>
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
				<thead>
					<tr>
						<td width="200" align="center">职员</td>
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