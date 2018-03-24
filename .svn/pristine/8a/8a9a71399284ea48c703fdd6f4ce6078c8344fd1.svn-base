<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<title>demo</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="/style/css/brotherSetting.css" />
<link type="text/css" rel="stylesheet"  href="/js/skins/ZCMS/asyncbox.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<style type="text/css">
body{width:820px;}
</style>

<script type="text/javascript">
$(function(){
	#if("$!operationSuccess" == "true")
		asyncbox.tips('操作成功!','success');
	#end
})
/*
	权限显示关闭
	scopeDiv:权限DIV
	scopeFlag:open 显示 close:隐藏
*/
function scopeDisplay(scopeDiv,scopeFlag){
	var wH = jQuery("#"+scopeDiv).find(".ddv").outerHeight(true);
	if(scopeFlag == "close"){
		//jQuery("#"+scopeDiv).hide();
		jQuery("#"+scopeDiv).stop().animate({height:"0"},300);
	}else{
		//jQuery("#"+scopeDiv).show();
		jQuery("#"+scopeDiv).stop().animate({height:wH},300);
	}
}
/*提交前*/
function beforeSubmit(){
	if($(":radio[name='importScopeFlag']:checked").val() == "1"){
		if($("#importDeptIds").val() == "" && $("#importUserIds").val() == "" && $("#importGroupIds").val() == ""){
			alert("导入控制权限为指定,至少选择一个对象");
			return false;
		}
	}
	
	if($(":radio[name='exportScopeFlag']:checked").val() == "1"){
		if($("#exportDeptIds").val() == "" && $("#exportUserIds").val() == "" && $("#exportGroupIds").val() == ""){
			alert("导出控制权限为指定,至少选择一个对象");
			return false;
		}
	}
	
	if($(":radio[name='printScopeFlag']:checked").val()== "1"){
		if($("#printDeptIds").val() == "" && $("#printUserIds").val() == "" && $("#printGroupIds").val() == ""){
			alert("打印控制权限为指定,至少选择一个对象");
			return false;
		}
	}
	form.submit();
}
</script>
</head>
<body>
<form action="/CRMBrotherPublicScopeAction.do" name="form" method="post">
	<input type="hidden" name="operation" id="operation" value="$globals.getOP("OP_UPDATE")"/>
	<input type="hidden" name="type" id="type" value="publicScope"/>
	<input type="hidden" name="tableName" id="tableName" value="$tableName"/>
	<input type="hidden" name="importDeptIds" id="importDeptIds" value='$!scopeMap.get("import").deptIds'/>
	<input type="hidden" name="importUserIds" id="importUserIds" value='$!scopeMap.get("import").userIds'/>
	<input type="hidden" name="importGroupIds" id="importGroupIds" value='$!scopeMap.get("import").groupIds'/>
	<input type="hidden" name="exportDeptIds" id="exportDeptIds" value='$!scopeMap.get("export").deptIds'/>
	<input type="hidden" name="exportUserIds" id="exportUserIds" value='$!scopeMap.get("export").userIds'/>
	<input type="hidden" name="exportGroupIds" id="exportGroupIds" value='$!scopeMap.get("export").groupIds'/>
	<input type="hidden" name="printDeptIds" id="printDeptIds" value='$!scopeMap.get("print").deptIds'/>
	<input type="hidden" name="printUserIds" id="printUserIds" value='$!scopeMap.get("print").userIds'/>
	<input type="hidden" name="printGroupIds" id="printGroupIds" value='$!scopeMap.get("print").groupIds'/>
	<div style="overflow:hidden;padding:5px 0 0 10px;">
		<div style="float:left;display:inline-block;line-height:26px;">当前位置：导入导出设置</div>
		<div class="btn btn-small" onclick="beforeSubmit()" style="float:right;">保存</div>
	</div>
	<div class="wp_dv" style="height:360px;overflow:auto;margin:5px 0 0 10px;">
		<div class="wp_bk">
			<!-- 
			<div class="bk_t">
				<i>导入权限控制</i>
				<div class="bk_radio">
					<div class="bk_radio_lt">权限控制：</div>
					<div class="bk_radio_rt">
						<div class="radio_items">
							<input class="radio_inp" type="radio" id="importScopeFlagAny" name="importScopeFlag" value="0" #if($scopeMap.get("import").scopeFlag==0 || "$!scopeMap.get('import')" == "") checked="checked" #end onclick="scopeDisplay('importScopeFlagDiv','close')"/>
							<label class="radio_lb" for="importScopeFlagAny">全部</label>					
						</div>
						<div class="radio_items">
							<input class="radio_inp" type="radio" id="importScopeFlagOther" name="importScopeFlag" value="1" #if($scopeMap.get("import").scopeFlag==1) checked="checked" #end onclick="scopeDisplay('importScopeFlagDiv','open')"/>
							<label class="radio_lb" for="importScopeFlagOther">指定</label>					
						</div>
					</div>
				</div>
			</div>
			<div class="bk_c" #if($scopeMap.get("import").scopeFlag==0 || "$!scopeMap.get('import')" == "") style="height:0;" #end id="importScopeFlagDiv">
				<div class="ddv">
					<div class="slt_woy">
						<div class="woy_lt">
							<select class="slt" size="5" name="importDeptName" id="importDeptName" multiple="multiple">
								#foreach($deptId in $scopeMap.get("import").deptIds.split(","))
									#if("$!deptId" !="")
									<option value="$deptId">
										$deptMap.get("$deptId")
									</option>
									#end
								#end
							</select>
						</div>
						<div class="woy_rt">
							<a class="add_a bga" title="添加" onClick="deptPop('deptGroup','importDeptName','importDeptIds','1');"></a>
							<a class="del_a bga" title="删除" onClick="deleteOpation('importDeptName','importDeptIds')"></a>
						</div>
						<p class="note_p">（选择部门）</p>
					</div>
					<div class="slt_woy">
						<div class="woy_lt">
							<select class="slt" size="5" id="importUserName" name="importUserName" multiple="multiple">
								#foreach($userId in $scopeMap.get("import").userIds.split(","))
									#if("$!userId" !="")
									<option value="$userId">
										$!globals.getEmpFullNameByUserId($userId)
									</option>
									#end
								#end
							</select>
						</div>
						<div class="woy_rt">
							<a class="add_a bga" title="添加" onClick="deptPop('userGroup','importUserName','importUserIds','1');"></a>
							<a class="del_a bga" title="删除" onClick="deleteOpation('importUserName','importUserIds')"></a>
						</div>
						<p class="note_p">（选择个人）</p>
					</div>
					<div class="slt_woy">
						<div class="woy_lt">
							<select class="slt" size="5" id="importGroupName" name="importGroupName" multiple="multiple">
								#foreach($groupId in $scopeMap.get("import").groupIds.split(","))
									#if("$!groupId" !="")
									<option value="$groupId">
										#set($groupIdNew = "import"+$groupId)
										$groupNameMap.get("$groupIdNew")  
									</option>
									#end
								#end
							</select>
						</div>
						<div class="woy_rt">
							<a class="add_a bga" title="添加" onClick="deptPop('empGroup','importGroupName','importGroupIds','1');"></a>
							<a class="del_a bga" title="删除" onClick="deleteOpation('importGroupName','importGroupIds');"></a>
						</div>
						<p class="note_p">（选择分组）</p>
					</div>
				</div>
			</div>
		</div>
		 -->
		<div class="wp_bk">
			<div class="bk_t">
				<i>导出权限控制</i>
				<div class="bk_radio">
					<div class="bk_radio_lt">权限控制：</div>
					<div class="bk_radio_rt">
						<div class="radio_items">
							<input class="radio_inp" type="radio" id="exportScopeFlagAny" name="exportScopeFlag" value="0" #if($scopeMap.get("export").scopeFlag==0 || "$!scopeMap.get('export')" == "") checked="checked" #end onclick="scopeDisplay('exportScopeFlagDiv','close')"/>
							<label class="radio_lb" for="exportScopeFlagAny">全部</label>					
						</div>
						<div class="radio_items">
							<input class="radio_inp" type="radio" id="exportScopeFlagOther" name="exportScopeFlag" value="1" #if($scopeMap.get("export").scopeFlag==1) checked="checked" #end onclick="scopeDisplay('exportScopeFlagDiv','open')"/>
							<label class="radio_lb" for="exportScopeFlagOther" >指定</label>					
						</div>
					</div>
				</div>
			</div>
			<div class="bk_c" #if($scopeMap.get("export").scopeFlag==0 || "$!scopeMap.get('export')" == "") style="height:0;" #end id="exportScopeFlagDiv">
				<div class="ddv">
					<!-----------分割线 xjj_slt选择 Start------------->
					<div class="slt_woy">
						<div class="woy_lt">
							<select class="slt" size="5" name="exportDeptName" id="exportDeptName" multiple="multiple">
								#foreach($deptId in $scopeMap.get("export").deptIds.split(","))
									#if("$!deptId" !="")
									<option value="$deptId">
										$deptMap.get("$deptId")
									</option>
									#end
								#end
							</select>
						</div>
						<div class="woy_rt">
							<a class="add_a bga" title="添加" onClick="deptPop('deptGroup','exportDeptName','exportDeptIds','1');"></a>
							<a class="del_a bga" title="删除" onClick="deleteOpation('exportDeptName','exportDeptIds')"></a>
						</div>
						<p class="note_p">（选择部门）</p>
					</div>
					<!-----------分割线 xjj_slt选择 Start------------->
					<!-----------分割线 xjj_slt选择 Start------------->
					<div class="slt_woy">
						<div class="woy_lt">
							<select class="slt" size="5" id="exportUserName" name="exportUserName" multiple="multiple">
								#foreach($userId in $scopeMap.get("export").userIds.split(","))
									#if("$!userId" !="")
									<option value="$userId">
										$!globals.getEmpFullNameByUserId($userId)
									</option>
									#end
								#end
							</select>
						</div>
						<div class="woy_rt">
							<a class="add_a bga" title="添加" onClick="deptPop('userGroup','exportUserName','exportUserIds','1');"></a>
							<a class="del_a bga" title="删除" onClick="deleteOpation('exportUserName','exportUserIds')"></a>
						</div>
						<p class="note_p">（选择个人）</p>
					</div>
					<!-----------分割线 xjj_slt选择 Start------------->
					<!-----------分割线 xjj_slt选择 Start------------->
					<div class="slt_woy">
						<div class="woy_lt">
							<select class="slt" size="5" id="exportGroupName" name="exportGroupName" multiple="multiple">
								#foreach($groupId in $scopeMap.get("export").groupIds.split(","))
									#if("$!groupId" !="")
									<option value="$groupId">
										#set($groupIdNew = "export"+$groupId)
										$groupNameMap.get("$groupIdNew")  
									</option>
									#end
								#end
							</select>
						</div>
						<div class="woy_rt">
							<a class="add_a bga" title="添加" onClick="deptPop('empGroup','exportGroupName','exportGroupIds','1');"></a>
							<a class="del_a bga" title="删除" onClick="deleteOpation('exportGroupName','exportGroupIds');"></a>
						</div>
						<p class="note_p">（选择分组）</p>
					</div>
					<!-----------分割线 xjj_slt选择 Start------------->
				</div>
			</div>
		</div>
		<!-----------分割线 wp_bk板块 End------------->
		
		<!-----------分割线 wp_bk板块 Start------------->
		<div class="wp_bk">
			<div class="bk_t">
				<i>打印权限控制</i>
				<div class="bk_radio">
					<div class="bk_radio_lt">权限控制：</div>
					<div class="bk_radio_rt">
						<div class="radio_items">
							<input class="radio_inp" type="radio" id="printScopeFlagAny" name="printScopeFlag" value="0" #if($scopeMap.get("print").scopeFlag==0 || "$!scopeMap.get('print')" == "") checked="checked" #end onclick="scopeDisplay('printScopeFlagDiv','close')"/>
							<label class="radio_lb" for="printScopeFlagAny">全部</label>					
						</div>
						<div class="radio_items">
							<input class="radio_inp" type="radio" id="printScopeFlagOther" name="printScopeFlag" value="1" #if($scopeMap.get("print").scopeFlag==1) checked="checked" #end onclick="scopeDisplay('printScopeFlagDiv','open')"/>
							<label class="radio_lb" for="printScopeFlagOther" >指定</label>					
						</div>
					</div>
				</div>
			</div>
			<div class="bk_c" #if($scopeMap.get("print").scopeFlag==0 || "$!scopeMap.get('print')" == "") style="height:0;" #end id="printScopeFlagDiv">
				<div class="ddv">
					<!-----------分割线 xjj_slt选择 Start------------->
					<div class="slt_woy">
						<div class="woy_lt">
							<select class="slt" size="5" name="printDeptName" id="printDeptName" multiple="multiple">
								#foreach($deptId in $scopeMap.get("print").deptIds.split(","))
									#if("$!deptId" !="")
									<option value="$deptId">
										$deptMap.get("$deptId")
									</option>
									#end
								#end
							</select>
						</div>
						<div class="woy_rt">
							<a class="add_a bga" title="添加" onClick="deptPop('deptGroup','printDeptName','printDeptIds','1');"></a>
							<a class="del_a bga" title="删除" onClick="deleteOpation('printDeptName','printDeptIds')"></a>
						</div>
						<p class="note_p">（选择部门）</p>
					</div>
					<!-----------分割线 xjj_slt选择 Start------------->
					<!-----------分割线 xjj_slt选择 Start------------->
					<div class="slt_woy">
						<div class="woy_lt">
							<select class="slt" size="5" id="printUserName" name="printUserName" multiple="multiple">
								#foreach($userId in $scopeMap.get("print").userIds.split(","))
									#if("$!userId" !="")
									<option value="$userId">
										$!globals.getEmpFullNameByUserId($userId)
									</option>
									#end
								#end
							</select>
						</div>
						<div class="woy_rt">
							<a class="add_a bga" title="添加" onClick="deptPop('userGroup','printUserName','printUserIds','1');"></a>
							<a class="del_a bga" title="删除" onClick="deleteOpation('printUserName','printUserIds')"></a>
						</div>
						<p class="note_p">（选择个人）</p>
					</div>
					<!-----------分割线 xjj_slt选择 Start------------->
					<!-----------分割线 xjj_slt选择 Start------------->
					<div class="slt_woy">
						<div class="woy_lt">
							<select class="slt" size="5" id="printGroupName" name="printGroupName" multiple="multiple">
								#foreach($groupId in $scopeMap.get("print").groupIds.split(","))
									#if("$!groupId" !="")
									<option value="$groupId">
										#set($groupIdNew = "print"+$groupId)
										$groupNameMap.get("$groupIdNew")  
									</option>
									#end
								#end
							</select>
						</div>
						<div class="woy_rt">
							<a class="add_a bga" title="添加" onClick="deptPop('empGroup','printGroupName','printGroupIds','1');"></a>
							<a class="del_a bga" title="删除" onClick="deleteOpation('printGroupName','printGroupIds');"></a>
						</div>
						<p class="note_p">（选择分组）</p>
					</div>
					<!-----------分割线 xjj_slt选择 Start------------->
				</div>
			</div>
		</div>
		<!-----------分割线 wp_bk板块 End------------->
	</div>
</form>
	</body>
</html>
