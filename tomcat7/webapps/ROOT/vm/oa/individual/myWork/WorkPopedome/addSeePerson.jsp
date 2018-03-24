<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>group</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">

function checkeSet(){	
	form.submit();
}


function fillData(datas){	
	newOpenSelect(datas,fieldNames,fieldNIds,1);
	parent.jQuery.close('Popdiv');
}

</script>
<style type="text/css">
.borders{
	border: 1px solid #C8CECE;
}
</style>
</head>

<body>
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" name="form" action="/OAWorkPopedomeActon.do" target="formFrame">
<input type="hidden" name="operation" id="operation"  value="$!operation"/>
<input type="hidden" name="seeId" id="seeId" value="$!seeId"/>
<input type="hidden" name="seePersonIds" id="seePersonIds" value="$!workpopedomes.seePersonId"/>
<input type="hidden" name="popedomUserIds" id="popedomUserIds" value="$!workpopedomes.bySeeUserID"/>
<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value="$!workpopedomes.bySeeDeptOfClassCode"/>
<input type="hidden" name="empGroupId" id="empGroupId" value="$!workpopedomes.bySeeEmpGroup"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/style1/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">#if("$!operation" == "1")添加查看权限设置#elseif("$!operation" == "2" && "$!detail" != "detail") 修改查看权限设置 #else 查看权限设置详情 #end</div>
	<ul class="HeadingButton">
	</ul>
</div>
<div id="listRange_id">
	<div id="alertDivId" style="width: 630px;margin-top:5px;margin-left:10px;padding:1px;text-align: left">
		<table border="0" width="100%" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
			<tr>
			  <td valign="" bgcolor="#F5F5F5">查看类型：</td>
			  <td>
				#if("$!detail"=="detail")
					 #if("$!workpopedomes.seeType"=="1")<span style="margin-left:20px;"> 工作计划 </span>#end
					 #if("$!workpopedomes.seeType"=="2")<span style="margin-left:20px;"> 事件计划 </span>#end
					 #if("$!workpopedomes.seeType"=="0")<span style="margin-left:20px;">  全部 </span>#end
				#else
			  	<select name="seeType" id="seeType" style="border:1px solid #9E9E9E;width:20%;">
			  		<option value="1" #if("$!workpopedomes.seeType"=="1") selected="selected"#end>工作计划</option>
			  		<option value="2" #if("$!workpopedomes.seeType"=="2") selected="selected"#end>事件计划</option>
			  		<option value="0" #if("$!workpopedomes.seeType"=="0") selected="selected"#end>全部</option>
			  	</select>
			  	#end
			  </td>
			</tr>
					<tr>
			  <td valign="middle" bgcolor="#F5F5F5">员工/下级：</td>
			  <td valign="middle">
				<div class="oa_signDocument1" id="_context">
				<div class="oa_signDocument_3">
					#if("$!detail"=="detail")
						<span>部门：</span>
					#else
					<img src="/$globals.getStylePath()/images/St.gif" onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds');"  alt="$text.get("oa.common.adviceDept")" class="search"/>&nbsp;
					<a href="javascript:void(0)" title="$text.get("oa.select.dept")" onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds');">$text.get("oa.select.dept")</a>
					#end
				</div>
				<select name="formatDeptName" id="formatDeptName" multiple="multiple">
				 #foreach ($!dept in $!workpopedomes.bySeeDepts)
					<option value="$dept.classCode">$!dept.DeptFullName</option>
				 #end
				</select></div>
				<div class="oa_signDocument2">
					#if("$!detail"=="detail")
					#else
					<img onClick="deleteOpation2('formatDeptName','popedomDeptIds');" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search"/>
					#end
				</div>
				
				<div class="oa_signDocument1" id="_context">
					<div class="oa_signDocument_3">
						#if("$!detail"=="detail")
							<span>职员：</span>
						#else
						<img src="/$globals.getStylePath()/images/St.gif" onClick="deptPop('userGroup','formatFileName','popedomUserIds','1');" alt="$text.get("oa.common.advicePerson")" class="search"/>&nbsp;
						<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="deptPop('userGroup','formatFileName','popedomUserIds','1');">$text.get("oa.select.personal")</a>
						
						#end
					</div>
					<select name="formatFileName" id="formatFileName" multiple="multiple">
					#foreach($!user in $!workpopedomes.bySeeUserNames)
						<option value="$user.id">$!user.EmpFullName</option>
					#end
					</select>
				</div>
				<div class="oa_signDocument2">
					#if("$!detail"=="detail")
					#else
					<img onClick="deleteOpation2('formatFileName','popedomUserIds');" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search"/>
					#end
				</div>
				
				<div class="oa_signDocument1" id="_context">
					<div class="oa_signDocument_3">
						#if("$!detail"=="detail")
							<span>分组：</span>
						#else
						<img src="/$globals.getStylePath()/images/St.gif" onClick="deptPop('empGroup','EmpGroup','empGroupId');" alt="$text.get("oa.oamessage.usergroup")" class="search"/>&nbsp;
						<a href="javascript:void(0)"  style="cursor:pointer" title="$text.get('oa.oamessage.usergroup')" onClick="deptPop('empGroup','EmpGroup','empGroupId');">$text.get("oa.oamessage.usergroup")</a>
						#end
					</div>
					<select name="EmpGroup" id="EmpGroup" multiple="multiple">
					#foreach($!grp in $!workpopedomes.bySeeEmpGroups)
						<option value="$!globals.get($grp,0)">$!globals.get($!grp,1)</option>
					#end
					</select>
				</div>
				<div class="oa_signDocument2">
					#if("$!detail"=="detail")
					#else
					<img onClick="deleteOpation2('EmpGroup','empGroupId');" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search"/>
					#end
				</div>
			</td>
			</tr>
			<tr>
			  <td valign="middle" bgcolor="#F5F5F5">检视员/上级：</td>
			  <td valign="top">
			  			
				<div class="oa_signDocument1" id="_context">
					<div class="oa_signDocument_3">
					#if("$!detail"=="detail")
						<span>检视员/上级:</span>
					#else
					<img src="/$globals.getStylePath()/images/St.gif" onClick="deptPop('userGroup','formatSeeName','seePersonIds','1');" alt="$text.get("oa.common.advicePerson")" class="search"/>&nbsp;
					<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="deptPop('userGroup','formatSeeName','seePersonIds','1');">$text.get("oa.select.personal")</a>
					#end
					</div>
				<select name="formatSeeName" id="formatSeeName" multiple="multiple">
				#foreach($user in $!workpopedomes.seePersonNames)
					<option value="$user.id">$!user.EmpFullName</option>
				#end
				</select></div>
				<div class="oa_signDocument2">
					#if("$!detail"=="detail")
					#else
					<img onClick="deleteOpation2('formatSeeName','seePersonIds');" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search"/>
					#end
				</div>
				<div class="oa_signDocument1" id="_context">
				<div>描述：<span id="showDiv" style="margin-left:10px;width:150px; border:0px solid red; color:red;display:none; float:right;">  描述不能超过200个字！ </span>
	 			</div>
					
					#if("$!detail"=="detail")
						<div style="margin-bottom: 5px;"><textarea rows="4" cols="35" name="desContent"  id="desContent" readonly="readonly"></textarea></div>	 
					#else
						<div style="margin-bottom: 5px;"><textarea rows="4" cols="35" name="desContent" id="desContent"  onkeyup="chengeShow();"></textarea></div>
					#end
					<script type="text/javascript">
						$("#desContent").val("$!workpopedomes.desContent");
					</script>
				</div>
			  </td>
			</tr>
	  </table>	
	</div>
</div>
</form>
</body>
	<script type="text/javascript">
	function chengeShow(){
		var groupDis=document.getElementById("desContent");
	if(groupDis.value.length>190){
		document.getElementById("showDiv").style.display="block";
		var value=groupDis.value.substring(0,190);
		groupDis.value=value;
		 flag=false;
	}else{
		document.getElementById("showDiv").style.display="none";
		flag=true;
	}
}
	</script>
</html>