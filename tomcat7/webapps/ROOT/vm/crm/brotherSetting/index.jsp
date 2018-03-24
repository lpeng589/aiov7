<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">	<title>demo</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link type="text/css" rel="stylesheet" href="/style/css/brotherSetting.css"/>
	<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
function goFrame(type){
	var url = '';
	if(type == "fieldScope"){
		url = '/CRMBrotherSettingAction.do?operation=$globals.getOP("OP_QUERY")&tableName=$tableName&type='+type;
	}else{
		url = '/CRMBrotherSettingAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&tableName=$tableName&type='+type;
	}
	jQuery("#mainFrame").attr("src",url);
}
</script>
</head>

<body>
	<div class="wrap_dv">
		<div class="wrap_lt">
			<div class="t_title">
				<i class="w_i">视图设置</i>
			</div>
			<ul class="ls_u">
				<li class="ls_li">
					<em class="w_em" onclick="goFrame('maintain')">字段维护</em>
				</li>
				<li class="ls_li">
					<em class="w_em" onclick="goFrame('fieldDisplay')">显示设置</em>
				</li>
				<li class="ls_li">
					<em class="w_em" onclick="goFrame('fieldScope')">字段权限设置</em>
				</li>
				<li class="ls_li">
					<em class="w_em" onclick="goFrame('publicScope')">导出设置</em>
				</li>
				#if("$tableName" == "CRMPotentialClient")
				<li class="ls_li">
					<em class="w_em" onclick="goFrame('fieldMapping')">客户字段隐射</em>
				</li>
				#end
			</ul>
		</div>
		<div class="wrap_rt">
			<iframe id="mainFrame" class="rt_iframe" frameborder="0" scrolling="0" src="/CRMBrotherSettingAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&tableName=$tableName">
				
			</iframe>
		</div>
	</div>
</body>
</html>
