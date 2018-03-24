<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>	模块权限分配</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js"></script>

<script language="javascript"> 
 
function isSelect(checkId,keyId,content){
	var items = document.getElementsByName(checkId);
	var mydatasIds="";
	for(var i=0;i<items.length;i++){
		if(items[i].checked){
			var value = items[i].value;
			mydatasIds+=value+",";
		}
	}
	if(mydatasIds!="" && mydatasIds.length!=0){
			mydatasIds = mydatasIds.substr(0,mydatasIds.length-1);
			document.getElementById(keyId).value=mydatasIds;
	}else{
		alert('至少选择一个'+content);
		return false;
	}
	return true;
}

function checkForm(){	
	document.myform.submit();
}

function selectAll(obj,name){
	if(obj.checked){
		$(".roCk",document).each(function(){			
			$(this)[0].checked=true;
		});
	}else{
		$(".roCk",document).each(function(){
			$(this)[0].checked=false;
		});
	}	
	
}
function selectRoleAll(obj,roleId){
	var chk=document.getElementsByName("operationId_"+roleId);
	if(obj.checked){
		for(var i=0;i<chk.length;i++){
			chk[i].checked=true;
		}
	}else{
		for(var i=0;i<chk.length;i++){
			chk[i].checked=false;
		}
	}
}
</script>
<style type="text/css">
	.roCk {width:25px}
</style>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form action="/RoleQueryAction.do?operation=2&updateType=updateRoleModule" method="post" target="formFrame" name="myform">
	<input type="hidden" id="moduleIds" name="moduleIds" value="$!moduleIds"/>	
	<input type="hidden" id="operationIds" name="operationIds" value=""/>	
	<div class="Heading" style="margin-bottom: 10px;">
		<div class="HeadingIcon"><img src="/style1/images/Left/Icon_1.gif"></div>
		<div class="HeadingTitlesmall">($moname)模块权限分配</div>
		<ul class="HeadingButton_Pop-up">
			<li><button type="button"  id="sure"  class="b2" onClick="checkForm();" >确定</button></li>			
			<li><button type="button" onClick="javascript:window.parent.jQuery.close('MainPopup')" class="b2"> $text.get("com.lb.close")</button></li>	
		</ul>	
	</div>

	<div class="listRange_Pop-up_scroll" style="margin-top: 10px;width:97%;">
		<table  border=0 cellpadding="0" cellspacing="0" class="listRange_list">
			<thead>
				<tr>
					<td width="40" align="center"><input type="checkbox" style="width:15px;" name="selAll" onClick="selectAll(this,'roleId');"></td>
					<td width="150" align="center">$text.get("role.lb.roleName")</td>
					<td width="250" align="center">操作</td>
					<td width="150" align="center">$text.get("role.lb.roleDesc")</td>	
				</tr>
			</thead>
			<tbody>
				#foreach ($row in $result )
					#if($globals.get($row,1)!="admin")
					<input type="hidden" name="roleIds" value="$globals.get($row,0)"/>
						<tr>
							<td align="center">
							<input type="checkbox" style="width:15px;" name="roleId_$globals.get($row,0)" onClick="selectRoleAll(this,'$globals.get($row,0)');" />
							<td align="left">#if($globals.get($row,0) == "1")$globals.getLocaleDisplay($defaultSunCmpName)#end$!globals.encodeHTMLLine($globals.get($row,1)) &nbsp;</td>
							<td align="left">
							#foreach ($operation in $globals.getEnumerationItems("EOperation"))	
							#foreach ($mo in $mopeation)	
							#if($operation.value==$globals.get($mo,0))	
								<input  name="operationId_$globals.get($row,0)" class="roCk" style="width:20px"  type="checkbox" #if($!rop.get("$globals.get($row,0)_$globals.get($mo,0)") != "") checked #end  value="$globals.get($mo,1)" />$operation.name	
							#end	
							#end			
							#end
							 &nbsp;</td>
							<td align="left">$!globals.encodeHTMLLine($globals.get($row,2)) &nbsp;</td>
						</tr>
					#end
				#end
			</tbody>				
		</table>	
	</div>
</form>
</body>
</html>

