<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<title>demo</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="/style/css/brotherSetting.css" />
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<style type="text/css">
body{width:820px;}
.x-d{padding: 1px 0;}
#templateName{width: 255px;}
.wp_bk .bk_c .ddv{padding: 5px 0;}
</style>

<script type="text/javascript">
//加载内容控件
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="content"]',{
		items : [
	        'undo', 'redo', '|','source', 'template', 'code', 'cut', 'copy', 'paste',
	        'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
	        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'clearhtml', 'fullscreen', '/',
	        'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
	        'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage',
	        'flash', 'media', 'table', 'hr', 'emoticons',
	        'anchor', 'link', 'unlink'
		],
		uploadJson:'/UtilServlet?operation=uploadFile'
	});
});

//双击回填数据(适用于多选下拉框),
function fillData(datas){
	newOpenSelect(datas,fieldNames,fieldNIds,flag);
	parent.jQuery.close('Popdiv');
}

function formSubmit(){
	if(jQuery("#templateName").val()==""){
		asyncbox.tips('名称不能为空');
		jQuery("#templateName").focus();
		return false;	
	}
	
	if(jQuery("#deptIds").val()=="" && jQuery("#userIds").val()==""){
		asyncbox.tips('必须选择设置对象.');
		return false;			
	}
	
	var templateContent= editor.html();
	if(templateContent==""){
		asyncbox.tips('内容不能为空.');
		return false;
	}else{
		jQuery("#content").val(templateContent);
	}
	
	form.submit();
}

</script>

</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form action="/OAWorkLogTemplateAction.do" name="form" method="post" target="formFrame">
<input type="hidden" name="operation" id="operation" value='$globals.getOP("OP_UPDATE")' />
<input type="hidden" name="templateId" id="templateId" value='$!templateBean.id' />
	<div class="wp_dv" >
		<div class="clear x-d">
			<i class="lf">模板名称:</i><input class="lf" type="text" id="templateName" name="name" value="$!templateBean.name"/>
		</div>
		<div class="clear x-d">
			<i class="lf">日志类型:</i>
			<lable class="l-cbox">
				<input type="radio" id="toplanType" name="toplanType" value="day" #if("$!templateBean.toplanType"=="day") checked="checked" #end/> 
				<i>日</i>
			</lable>
			<lable class="l-cbox">
				<input type="radio" id="toplanType" name="toplanType" value="week" #if("$!templateBean.toplanType"=="week") checked="checked" #end/> 
				<i>周</i>
			</lable>
		</div>
		<div class="clear x-d">
			<i class="lf">是否启用:</i>
			<lable class="l-cbox">
				<input type="radio"  name="statusId" value="0" #if("$!templateBean.statusId"=="0") checked="checked" #end/> 
				<i>是</i>
			</lable>
			<lable class="l-cbox">
				<input type="radio"  name="statusId" value="-1" #if("$!templateBean.statusId"=="-1") checked="checked" #end/> 
				<i>否</i>
			</lable>
		</div>
		<!-----------分割线 wp_bk板块 Start------------->
		<div class="wp_bk">
			<div class="bk_c">
				<div class="ddv">
					<!-----------分割线 xjj_slt选择 Start------------->
					<div class="slt_woy">
						<input type="hidden" name="deptIds" id="deptIds" value="$!templateBean.deptIds">
						<div class="woy_lt">
							<select class="slt" size="5" name="deptIdsName" id="deptIdsName" multiple="multiple">
							#if("$!templateBean.deptIds"!="")
								#foreach($deptId in $!templateBean.deptIds.split(","))
									#if("$!deptId" !="")
									<option value="$deptId">
										$!deptMap.get("$deptId")
									</option>
									#end
								#end
							#end
							</select>
						</div>
						<div class="woy_rt">
							<a class="add_a bga" title="添加" onClick="deptPop('deptGroup','deptIdsName','deptIds','1');"></a>
							<a class="del_a bga" title="删除" onClick="deleteOpation('deptIdsName','deptIds')"></a>
						</div>
						<p class="note_p">（选择部门）</p>
					</div>
					<!-----------分割线 xjj_slt选择 Start------------->
					<!-----------分割线 xjj_slt选择 Start------------->
					<div class="slt_woy">
						<input type="hidden" name="userIds" id="userIds" value="$!templateBean.userIds">
						<div class="woy_lt">
							<select class="slt" size="5" id="userIdsName" name="userIdsName" multiple="multiple">
							#if("$!templateBean.userIds"!="")
								#foreach($userId in $!templateBean.userIds.split(","))
									#if("$!userId" !="")
									<option value="$userId">
										$!globals.getEmpFullNameByUserId($userId)
									</option>
									#end
								#end
							#end
							</select>
						</div>
						<div class="woy_rt">
							<a class="add_a bga" title="添加" onClick="deptPop('userGroup','userIdsName','userIds','1');"></a>
							<a class="del_a bga" title="删除" onClick="deleteOpation('userIdsName','userIds')"></a>
						</div>
						<p class="note_p">（选择个人）</p>
					</div>
					<!-----------分割线 xjj_slt选择 Start------------->		
				</div>
			</div>
		</div>
		<!-----------分割线 wp_bk板块 End------------->
		<div>
			<textarea name="content" id="content" style="height:230px;width:930px;line-height:normal;">$templateBean.content</textarea>
		</div>
	</div>
</form>

</body>
</html>
