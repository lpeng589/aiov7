<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>group</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<script src="/js/jquery.js" type="text/javascript"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<link rel="stylesheet" href="/style/css/common.css" type="text/css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="/style/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="/style/themes/icon.css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>

<script type="text/javascript">

var deptIds;	//获取组授权的部门的Id
var usId; //获取组授权的个人Id

function checkAlert(){
	var flag = true;
	$("input").each(function () {
	   	if($(this).attr('required') || $(this).attr('validType')) {
		   	if(!$(this).validatebox('isValid')) {
		       flag = false; 
		   	}
	   }
	});
	if(flag==true){
   		form.submit();
   	}
   	//window.parent.parent.frames["leftFrame"].refreshself(); 
	return flag;
}

$(function(){
	$('input').each(function () { 
    	if ($(this).attr('required') || $(this).attr('validType')) 
			$(this).validatebox(); 
    });
    jQuery.extend(jQuery.fn.validatebox.defaults.rules, {
     	groupName: {
	        validator: function(value, param){	        	
	            return strLength(value);
	        },
	        message: '$text.get("oa.input.lengtherror")'
	    },
	    isNum: {
	        validator: function(value,param){
	            return /^\+?[0-9][0-9]*$/.test(value);
	        },
	        message: '此字段必须是正整数！'
   	    }
	   
	 });
});

function strLength(str){
	var maxLen=25;
	var myLen=0;
	var i=0;
	for(;(i<str.length)&&(myLen<=maxLen*2);i++){
		if(str.charCodeAt(i)>0&&str.charCodeAt(i)<128)
			myLen++;
		else
			myLen+=2;
	}
	if(myLen>maxLen*2)
		return false;
	else
		return true;
}

function  isNumber(){ 
	var str=form.listOrder.value;
     var re=/^(-|\+)?\d+(\.\d+)?$/; 
    return re.test(str);
} 

var fieldNames;
var fieldNIds;
/*
 *查询部门
 *根据flag标识判断是否需要筛选部门 1表示不需要 2表示需要

*/
function Popdept(popname,fieldName,fieldNameIds,flag){
	
	var deptIds = document.getElementById("fuDept").value;
	var userIds = document.getElementById("UserIds").value;  
	var urls = "/Accredit.do?popname=" + popname + "&inputType=checkbox"
	if(flag != 1){  
		 urls += "&parameterCode=" + deptIds;
		 if(deptIds == "" && userIds != ""){
			urls += "0";
		}
	}
	fieldNames=fieldName;
	fieldNIds=fieldNameIds;
	asyncbox.open({
		id : 'Popdiv',
		 title : '请选择部门',
	　　　url : urls,
	　　　width : 750,
	　　　height : 430,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。

　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。
			var employees = opener.strData;
			newOpenSelect(employees,fieldName,fieldNameIds)
　　　　　	}
　　　	}
　	});
}

/*
 *查询个人
 *根据flag标识判断是否需要根据部门筛选人员,1表示不需要 2表示需要

*/
function PopUser(popname,fieldName,fieldNameIds,flag){
	var deptIds = document.getElementById("fuDept").value;
	var usId = document.getElementById("UserIds").value;  
	
	var urls = "/Accredit.do?popname=" + popname + "&inputType=checkbox";
	if(flag == 2){
		urls+="&userCode=" + usId + "&parameterCode=" + deptIds;
	}
	fieldNames=fieldName;
	fieldNIds=fieldNameIds;
	asyncbox.open({
		id : 'Popdiv',
	    title : '请选择个人',
	　　　url : urls,
	　　　width : 750,
	　　　height : 430,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。
　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。
			var employees = opener.strData;				
			newOpenSelect(employees,fieldName,fieldNameIds)
　　　　　	}
　　　	}
　	});
}


//下载部门选择
function selectdownDept(popname,fieldName,fieldNameIds){
	var popedomDeptId = document.getElementById("popedomDeptIds").value;
	
	var urls = "/Accredit.do?popname=" + popname + "&inputType=checkbox&parameterCode="+popedomDeptId;
	fieldNames=fieldName;
	fieldNIds=fieldNameIds;
	asyncbox.open({
		id : 'Popdiv',
		title : '请选择部门',
		url : urls,
	　　　width : 750,
	　　　height : 430,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。
　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。
			var employees = opener.strData;
			newOpenSelect(employees,fieldName,fieldNameIds)
　　　　　	}
　　　	}
　	});
}

//下载人员选择
function selectdownUser(popname,fieldName,fieldNameIds){
	var popedomDeptId = document.getElementById("popedomDeptIds").value;
	var popedomUserId = document.getElementById("popedomUserIds").value;
	
	var urls = "/Accredit.do?popname=" + popname + "&inputType=checkbox&parameterCode="+popedomDeptId+"&userCode="+popedomUserId+"&chooseData="+form.downUserIds.value;
	fieldNames=fieldName;
	fieldNIds=fieldNameIds;
	asyncbox.open({
		id : 'Popdiv',
	 title : '请选择个人',
	　　　url : urls,
	　　　width : 750,
	　　　height : 430,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。

　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。
			var employees = opener.strData;
			jQuery("#"+fieldName+" option").remove();
			getValueById(fieldNameIds).value="";
			newOpenSelect(employees,fieldName,fieldNameIds)
　　　　　	}
　　　	}
　	});
}
function fillData(datas){	
	newOpenSelect(datas,fieldNames,fieldNIds,1);
	parent.jQuery.close('Popdiv');
}
</script>
</head>
<body>
	<iframe name="formFrame" style="display:none"></iframe>
	<form method="post" name="form" action="OAOrdainGroupCenter.do?opeartion=4"
			target="formFrame">
			<input type="hidden" name="deptNames" id="deptNames" value="$!deptNames"/>
			<input type="hidden" name="fuDept" id="fuDept" value="$!fuDept"/>
			<input type="hidden" name="UserIds" id="UserIds" value="$!UserIds"/>
			<input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")"/>
			<input name="dealType" value="$!dealType" type="hidden" />
			#if("$!dealType" == "add")
				<input name="classCode" value="$!folderId" type="hidden" />
			#elseif("$!dealType" == "update")
				<input name="classCode" value="$!group.classCode" type="hidden" />
			#end
			<input type="hidden" name="popedomUserIds" id="popedomUserIds" size="100" value="$!group.popedomUserIds" />
			<input type="hidden" name="time" id="time" value="$!time" />
			<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value="$!group.popedomDeptIds" />
			<input type="hidden" name="downDeptIds" id="downDeptIds" value="$!group.downDeptIds"/>
			<input type="hidden" name="downUserIds" id="downUserIds" value="$!group.downUserIds"/>
			<input type="hidden" name="createBy" value="$!group.createBy" />
			<input type="hidden" name="createTime" value="$!group.createTime" />
			<input type="hidden" name="groupid" value="$!group.id" />
			<input type="hidden" name="isCatalog" value="$!group.isCatalog"/>
			<input type="hidden" name="flag" value="$!flag"/>
			<div class="Heading">
				<div class="HeadingIcon">
					<img src="/style1/images/Left/Icon_1.gif" />
				</div>
				<div class="HeadingTitle">
					#if("$!dealType" == "add")添加组#else 修改组 #end
				</div>
				<ul class="HeadingButton">
				</ul>
			</div>
			<div id="listRange_id">
				<div id="alertDivId"
					style="width: 520px;margin-top:5px;margin-left:10px;padding:1px;text-align: left">
					<table border="0" width="100%" cellpadding="0" cellspacing="0"
						class="oalistRange_list_function" name="table">
						<tr>
							<td valign="right" bgcolor="#F5F5F5" style="width:100px;">
								组名：

								<font color="red">*</font>
							</td>
							<td valign="top">
								<input name="groupName" id="groupName" size="55" value="$!group.groupName"
								 	class="easyui-validatebox" required="true"
									validType="groupName[25]" />
							</td>
						</tr>
						<tr>
							<td valign="middle" bgcolor="#F5F5F5">
								排序：
								<font color="red"></font>
							</td>
							<td valign="top">
								<input name="listOrder" id="listOrder" value="$!group.listOrder" class="easyui-validatebox"  validType="isNum"/>
							</td>
						</tr>
						<tr>
							<td valign="middle" bgcolor="#F5F5F5">
								目录授权：

								<font color="red"></font>
							</td>
							<td valign="middle">
								<div class="oa_signDocument1" id="_context">
									<div class="oa_signDocument_3">
										<img src="/$globals.getStylePath()/images/St.gif"
											onClick="Popdept('deptGroup','formatDeptName','popedomDeptIds','$!flag');" alt="$text.get("oa.common.adviceDept")" class="search" />
										&nbsp;
										<a href="javascript:void(0)" title="$text.get("oa.select.dept")" onClick="Popdept('deptGroup','formatDeptName','popedomDeptIds','$!flag');">$text.get("oa.select.dept")</a>
									</div>
									<select name="formatDeptName" id="formatDeptName"
										multiple="multiple">
										#foreach($dept in $targetDept)
										<option value="$!globals.get($dept,14)">
											$!globals.get($dept,2)
										</option>
										#end
									</select>
								</div>
								<div class="oa_signDocument2">
									<img onClick="deleteOpation2('formatDeptName','popedomDeptIds');"
										src="/$globals.getStylePath()/images/delete_button.gif"
										alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
								</div>
								
								
								<div class="oa_signDocument1" id="_context">
									<div class="oa_signDocument_3">
										<img src="/$globals.getStylePath()/images/St.gif"
											onClick="PopUser('userGroup','formatFileName','popedomUserIds','$!flag');" alt="$text.get("oa.common.advicePerson")" class="search" />
										&nbsp;
										<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="PopUser('userGroup','formatFileName','popedomUserIds','$!flag');">$text.get("oa.select.personal")</a>
									</div>
									<select name="formatFileName" id="formatFileName"
										multiple="multiple">
										#foreach($user in $targetUsers)
										<option value="$user.id">
											$!user.EmpFullName
										</option>
										#end
									</select>
								</div>
								<div class="oa_signDocument2">
									<img onClick="deleteOpation2('formatFileName','popedomUserIds');"
										src="/$globals.getStylePath()/images/delete_button.gif"
										alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
								</div>	
								<div style="float:left;color: gray;width:50%;">
								#if("$!fuDept"=="" && "$!UserIds"=="")
									注:若不选，所有人可见此目录信息。
								#end
								</div>
							</td>
						</tr>
						<tr>
						  <td valign="middle" bgcolor="#F5F5F5">下载授权：<font color="red"></font></td>
						  <td valign="middle">
							<div class="oa_signDocument1" id="_context">
							<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="selectdownDept('deptGroup','downDeptName','downDeptIds');"  alt="$text.get("oa.common.adviceDept")" class="search"/>&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" title="$text.get("oa.select.dept")" onClick="selectdownDept('deptGroup','downDeptName','downDeptIds');"">$text.get("oa.select.dept")</a></div>
							<select name="downDeptName" id="downDeptName" multiple="multiple">
								#foreach($dept in $downDept)
									<option value="$!globals.get($dept,14)">
											$!globals.get($dept,2)
									</option>
								#end
							</select></div>
							<div class="oa_signDocument2">
							<img onClick="deleteOpation2('downDeptName','downDeptIds');" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search"/>
							</div>
							<div class="oa_signDocument1" id="_context">
							<div class="oa_signDocument_3">
								<img src="/$globals.getStylePath()/images/St.gif"
														onClick="selectdownUser('userGroup','downEmpName','downUserIds');" alt="$text.get("oa.common.advicePerson")" class="search" />
													&nbsp;
													<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="selectdownUser('userGroup','downEmpName','downUserIds');">$text.get("oa.select.personal")</a>
							</div>
							<select name="downEmpName" id="downEmpName" multiple="multiple">
							#foreach($user in $downUsers)
								<option value="$user.id">$!user.EmpFullName</option>
							#end
							</select></div>
							<div class="oa_signDocument2">
							<img onClick="deleteOpation2('downEmpName','downUserIds');" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search"/>
							</div>
							<div style="float:left;color: gray;width:60%;">注:若不选，目录授权的人可下载附件。</div>
						</td>
						</tr>
						
						<tr>
							<td valign="middle" bgcolor="#F5F5F5">
								描述：
							</td>
							<td valign="top">
								<textarea rows="5" cols="50" id="description" name="description" >$!group.description</textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</form>
	</body>
</html>
