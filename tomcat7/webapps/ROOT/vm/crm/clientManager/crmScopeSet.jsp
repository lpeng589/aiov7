<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="/style1/css/workflow2.css" />
<link rel="stylesheet" type="text/css" href="/style/css/clientModule.css" />
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css"/>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<style type="text/css">
a{color:#000000;}
a:link{color:#000000;}
a:visited{color:#000000;}
a:hover{color:#000000;}
</style>
<script type="text/javascript">

function checkRadio(obj,title){
	if(obj.checked==true){
		if(obj.value==1){
		 	$("#"+title).show();		 			
		}else{
			$("#"+title).hide();
		}
	}
}

function subForm(){

	if(!selectRadio('import')){
		alert("导入权限设置为指定，请选择授权对象");
		return false;
	}

	if(!selectRadio('export')){
		alert("导出权限设置为指定，请选择授权对象");
		return false;
	}
	if(!selectRadio('print')){
		alert("打印权限设置为指定，请选择授权对象");
		return false;
	}
	
	var x1=	$("input[name='isAlonePopedmon1']:checked").val();
	$("#printisAll").val(x1);
	if(x1=="0"){
		$("#printdomDeptIds").val('');
		$("#printdomUserIds").val('');
		$("#printdomEmpGroupIds").val('');
	}
	var x2=	$("input[name='isAlonePopedmon2']:checked").val();
	$("#importisAll").val(x2);
	if(x2=="0"){
		$("#importdomDeptIds").val('');
		$("#importdomUserIds").val('');
		$("#importdomEmpGroupIds").val('');
	}
	var x3=	$("input[name='isAlonePopedmon3']:checked").val();
	$("#exportisAll").val(x3);
	if(x3=="0"){
		$("#exportdomDeptIds").val('');
		$("#exportdomUserIds").val('');
		$("#exportdomEmpGroupIds").val('');
	}
	form.submit();
}

function fillData(datas){
	newOpenSelect(datas,fieldNames,fieldNIds,1);
	jQuery.close('Popdiv');
}

function selectRadio(str){
	var dept=$("#"+str+"domDeptIds").val();
	var user=$("#"+str+"domUserIds").val();
	var group=$("#"+str+"domEmpGroupIds").val();
	if($("#"+str+"radiobutton").attr("checked")=="checked" && dept=="" && user=="" && group==""){
		return false;
	}
	return true;
}
document.onkeydown = keyDown; 

</script>
</head>
<body>
	<form method="post" scope="request" name="form"
		action="/ClientSettingAction.do?operation=$globals.getOP("OP_UPDATE")&updType=updateCrmScope" class="mytable">
		<input type="hidden" name="printdomDeptIds" id="printdomDeptIds" value="$globals.get($crmScopeList.get(0),2)" />
		<input type="hidden" name="printdomUserIds" id="printdomUserIds" value="$globals.get($crmScopeList.get(0),3)" />
		<input type="hidden" name="printdomEmpGroupIds" id="printdomEmpGroupIds" value="$globals.get($crmScopeList.get(0),4)" />
		<input type="hidden" name="importdomDeptIds" id="importdomDeptIds" value="$globals.get($crmScopeList.get(1),2)" />
		<input type="hidden" name="importdomUserIds" id="importdomUserIds" value="$globals.get($crmScopeList.get(1),3)" />
		<input type="hidden" name="importdomEmpGroupIds" id="importdomEmpGroupIds" value="$globals.get($crmScopeList.get(1),4)" />
		<input type="hidden" name="exportdomDeptIds" id="exportdomDeptIds" value="$globals.get($crmScopeList.get(2),2)" />
		<input type="hidden" name="exportdomUserIds" id="exportdomUserIds" value="$globals.get($crmScopeList.get(2),3)" />
		<input type="hidden" name="exportdomEmpGroupIds" id="exportdomEmpGroupIds" value="$globals.get($crmScopeList.get(2),4)" />
		<input type="hidden" name="printisAll" id="printisAll" value="$globals.get($crmScopeList.get(0),1)" />
		<input type="hidden" name="importisAll" id="importisAll" value="$globals.get($crmScopeList.get(1),1)" />
		<input type="hidden" name="exportisAll" id="exportisAll" value="$globals.get($crmScopeList.get(2),1)" />
		<input type="hidden" name="moduleId" id="moduleId" value="$!moduleId" />
		<input type="hidden" name="viewId" id="viewId" value="$!viewId" />
		<table cellpadding="0" cellspacing="0" class="framework">
			<tr>
				<td>
					<div class="TopTitle">
						<span>当前位置:导入导出权限设置 </span>
						<div>
							<input type="button" class="bu_02" onclick="subForm();" value="保存" />
						</div>
					</div>
					<div id="cc" class="data"
						style="overflow:hidden;overflow-y:auto;width:100%;">
						<script type="text/javascript">
							var oDiv=document.getElementById("cc");
							var sHeight=document.documentElement.clientHeight-55;
							oDiv.style.height=sHeight+"px";
						</script>
						<div class="boxbg2 subbox_w700">
							<div class="subbox cf">
								<div class="inputbox">
									
									<h4>
										导入权限控制
									</h4>
									 #set ($isAlone = $globals.get($crmScopeList.get(1),1)) 
										#if($isAlone=="0"||"$!isAlone"=="") 
											#set ($check3="checked='checked'") 
											#set ($style2="margin-top: 15px;display:none") 
										#else 
											#set ($check4="checked='checked'") 
											#set ($style2="margin-top: 15px;display:block") 
									 #end
									<ul class="cf">
										<span style="width: 100%;margin-left: 100px;float: left;">权限控制：	
											<input name="isAlonePopedmon2" onClick="checkRadio(this,'_title2')"  $!check3  type="radio" id="radio"  value="0" />
												$text.get("oa.common.all")
											<input name="isAlonePopedmon2" onClick="checkRadio(this,'_title2')"  $!check4  type="radio" id="importradiobutton" value="1" />
												$text.get("oa.common.appoint")
										</span>
										<div id="_title2" style="$style2">
										<div class="oa_signDocument1" id="_context" style="margin-left: 100px;float: left;">
										<div class="oa_signDocument_3">
											<img src="/$globals.getStylePath()/images/St.gif"
												onClick="deptPop('deptGroup','formatDeptName2','importdomDeptIds','1');" alt="$text.get("oa.common.adviceDept")" class="search" />
											&nbsp;
											<a href="javascript:void(0)" title="$text.get("oa.select.dept")" onClick="deptPop('deptGroup','formatDeptName2','importdomDeptIds','1');">
											$text.get("oa.select.dept")</a>
										</div>
										<select name="formatDeptName2" id="formatDeptName2" size="5"
											multiple="multiple">
											#foreach($dept in $importDept)
											<option value="$!globals.get($dept,14)">
												$!globals.get($dept,2) 
											</option>
											#end
										</select>
										</div>
										<div class="oa_signDocument2">
											<img onClick="deleteOpation('formatDeptName2','importdomDeptIds')"
												src="/$globals.getStylePath()/images/delete_button.gif"
												alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search" />
										</div>
										<div class="oa_signDocument1" id="_context" >
											<div class="oa_signDocument_3">
												<img src="/$globals.getStylePath()/images/St.gif"
													onClick="deptPop('userGroup','formatFileName2','importdomUserIds','1');" alt="$text.get("oa.common.advicePerson")" class="search" />
												&nbsp;
												<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="deptPop('userGroup','formatFileName2','importdomUserIds','1');">$text.get("oa.select.personal")</a>
											</div>
											<select name="formatFileName2" id="formatFileName2" size="5"
												multiple="multiple">
												#foreach($user in $importUsers)
												<option value="$user.id">
													$!user.EmpFullName
												</option>
												#end
											</select>
										</div>
										<div class="oa_signDocument2">
											<img onClick="deleteOpation('formatFileName2','importdomUserIds')"
												src="/$globals.getStylePath()/images/delete_button.gif"
												alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search" />
										</div>
									
										<div class="oa_signDocument1" id="_context" >
										<div class="oa_signDocument_3">
											<img src="/$globals.getStylePath()/images/St.gif"
												onClick="deptPop('empGroup','EmpGroup2','importdomEmpGroupIds','1');"
												alt="$text.get("oa.oamessage.usergroup")" class="search" />
											&nbsp;
											<a href="javascript:void(0)" style="cursor:pointer"
												title="$text.get('oa.oamessage.usergroup')"
												onClick="deptPop('empGroup','EmpGroup2','importdomEmpGroupIds','1');">选择分组</a>
										</div>
										<select name="EmpGroup2" id="EmpGroup2" size="5" multiple="multiple">
											#foreach($grp in $importEmpGroup)
											<option value="$!globals.get($grp,0)">
												$!globals.get($grp,5) 
											</option>
											#end
										</select>
										</div>
										<div class="oa_signDocument2">
											<img onClick="deleteOpation('EmpGroup2','importdomEmpGroupIds')"
												src="/$globals.getStylePath()/images/delete_button.gif"
												alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search" />
										</div>
										</div>
									</ul>
									<h4>
										导出权限控制
									</h4>
									 #set ($isAlone = $globals.get($crmScopeList.get(2),1)) 
										#if($isAlone=="0"||"$!isAlone"=="") 
											#set ($check5="checked") 
											#set ($style3="margin-top: 15px;display:none") 
										#else 
											#set ($check6="checked") 
											#set ($style3="margin-top: 15px;display:block") 
									 #end
									<ul class="cf">
										<span style="width: 100%;margin-left: 100px;float: left;">权限控制：	
											<input name="isAlonePopedmon3" onClick="checkRadio(this,'_title3')"  $!check5  type="radio" id="radio"  value="0" />
												$text.get("oa.common.all")
											<input name="isAlonePopedmon3" onClick="checkRadio(this,'_title3')"  $!check6  type="radio" id="exportradiobutton" value="1" />
												$text.get("oa.common.appoint")
										</span>
										<div id="_title3" style="$style3">
										<div class="oa_signDocument1" id="_context" style="margin-left: 100px;float: left;">
										<div class="oa_signDocument_3">
											<img src="/$globals.getStylePath()/images/St.gif"
												onClick="deptPop('deptGroup','formatDeptName3','exportdomDeptIds','1');" alt="$text.get("oa.common.adviceDept")" class="search" />
											&nbsp;
											<a href="javascript:void(0)" title="$text.get("oa.select.dept")" onClick="deptPop('deptGroup','formatDeptName3','exportdomDeptIds','1');">
											$text.get("oa.select.dept")</a>
										</div>
										<select name="formatDeptName3" id="formatDeptName3" size="5"
											multiple="multiple">
											#foreach($dept in $exportDept)
											<option value="$!globals.get($dept,14)">
												$!globals.get($dept,2) 
											</option>
											#end
										</select>
										</div>
										<div class="oa_signDocument2">
											<img onClick="deleteOpation('formatDeptName3','exportdomDeptIds')"
												src="/$globals.getStylePath()/images/delete_button.gif"
												alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search" />
										</div>
										<div class="oa_signDocument1" id="_context" >
											<div class="oa_signDocument_3">
												<img src="/$globals.getStylePath()/images/St.gif"
													onClick="deptPop('userGroup','formatFileName3','exportdomUserIds','1');" alt="$text.get("oa.common.advicePerson")" class="search" />
												&nbsp;
												<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="deptPop('userGroup','formatFileName3','exportdomUserIds','1');">$text.get("oa.select.personal")</a>
											</div>
											<select name="formatFileName3" id="formatFileName3" size="5"
												multiple="multiple">
												#foreach($user in $exportUsers)
												<option value="$user.id">
													$!user.EmpFullName
												</option>
												#end
											</select>
										</div>
										<div class="oa_signDocument2">
											<img onClick="deleteOpation('formatFileName3','exportdomUserIds')"
												src="/$globals.getStylePath()/images/delete_button.gif"
												alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search" />
										</div>
										<div class="oa_signDocument1" id="_context" >
										<div class="oa_signDocument_3">
											<img src="/$globals.getStylePath()/images/St.gif"
												onClick="deptPop('empGroup','EmpGroup3','exportdomEmpGroupIds','1');"
												alt="$text.get("oa.oamessage.usergroup")" class="search" />
											&nbsp;
											<a href="javascript:void(0)" style="cursor:pointer"
												title="$text.get('oa.oamessage.usergroup')"
												onClick="deptPop('empGroup','EmpGroup3','exportdomEmpGroupIds','1');">选择分组</a>
										</div>
										<select name="EmpGroup3" id="EmpGroup3" size="5" multiple="multiple">
											#foreach($grp in $importEmpGroup)
											<option value="$!globals.get($grp,0)">
												$!globals.get($grp,5) 
											</option>
											#end
										</select>
										</div>
										<div class="oa_signDocument2">
											<img onClick="deleteOpation('EmpGroup3','exportdomEmpGroupIds')"
												src="/$globals.getStylePath()/images/delete_button.gif"
												alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search" />
										</div>
										</div>
									</ul>
									<h4 style="margin-top: 1px;">
										打印权限控制
									</h4>
									 #set ($isAlone = $globals.get($crmScopeList.get(0),1)) 
										#if($isAlone=="0"||"$!isAlone"=="") 
											#set ($check1="checked") 
											#set ($style1="margin-top: 15px;display:none") 
										#else 
											#set ($check2="checked") 
											#set ($style1="margin-top: 15px;display:block") 
									 #end
									<ul class="cf">
										<span style="width: 100%;margin-left: 100px;float: left;">权限控制：	
											<input name="isAlonePopedmon1" onClick="checkRadio(this,'_title1')"  $!check1  type="radio" id="radio"  value="0" />
												$text.get("oa.common.all")
											<input name="isAlonePopedmon1" onClick="checkRadio(this,'_title1')"  $!check2  type="radio" id="printradiobutton" value="1" />
												$text.get("oa.common.appoint")
										</span>
										<div id="_title1" style="$style1">
											<div class="oa_signDocument1" id="_context" style="margin-left: 100px;float: left;">
												<div class="oa_signDocument_3">
													<img src="/$globals.getStylePath()/images/St.gif"
														onClick="deptPop('deptGroup','formatDeptName1','printdomDeptIds','1');" alt="$text.get("oa.common.adviceDept")" class="search" />
													&nbsp;
													<a href="javascript:void(0)" title="$text.get("oa.select.dept")" onClick="deptPop('deptGroup','formatDeptName1','printdomDeptIds','1');">
													$text.get("oa.select.dept")</a>
												</div>
												<select name="formatDeptName1" id="formatDeptName1" size="5"
													multiple="multiple">
													#foreach($dept in $printDept)
													<option value="$!globals.get($dept,14)">
														$!globals.get($dept,2) 
													</option>
													#end
												</select>
											</div>
											<div class="oa_signDocument2">
												<img onClick="deleteOpation('formatDeptName1','printdomDeptIds')"
													src="/$globals.getStylePath()/images/delete_button.gif"
													alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search" />
											</div>
											<div class="oa_signDocument1" id="_context" >
												<div class="oa_signDocument_3">
													<img src="/$globals.getStylePath()/images/St.gif"
														onClick="deptPop('userGroup','formatFileName1','printdomUserIds','1');" alt="$text.get("oa.common.advicePerson")" class="search" />
													&nbsp;
													<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="deptPop('userGroup','formatFileName1','printdomUserIds','1');">$text.get("oa.select.personal")</a>
												</div>
												<select name="formatFileName1" id="formatFileName1" size="5"
													multiple="multiple">
													#foreach($user in $printUsers)
													<option value="$user.id">
														$!user.EmpFullName
													</option>
													#end
												</select>
											</div>
											<div class="oa_signDocument2">
												<img onClick="deleteOpation('formatFileName1','printdomUserIds')"
													src="/$globals.getStylePath()/images/delete_button.gif"
													alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search" />
											</div>
										
											<div class="oa_signDocument1" id="_context" >
												<div class="oa_signDocument_3">
													<img src="/$globals.getStylePath()/images/St.gif"
														onClick="deptPop('empGroup','EmpGroup1','printdomEmpGroupIds','1');"
														alt="$text.get("oa.oamessage.usergroup")" class="search" />
													&nbsp;
													<a href="javascript:void(0)" style="cursor:pointer"
														title="$text.get('oa.oamessage.usergroup')"
														onClick="deptPop('empGroup','EmpGroup1','printdomEmpGroupIds','1');">选择分组</a>
												</div>
												<select name="EmpGroup1" id="EmpGroup1" size="5" multiple="multiple">
												#foreach($grp in $printEmpGroup)
												<option value="$!globals.get($grp,0)">
													$!globals.get($grp,5) 
												</option>
												#end
												</select>
											</div>
											<div class="oa_signDocument2">
												<img onClick="deleteOpation('EmpGroup1','printdomEmpGroupIds')"
													src="/$globals.getStylePath()/images/delete_button.gif"
													alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search" />
											</div>
										</div>
									</ul>
									<br />
								</div>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
