<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script>

//流程版本
function openFlowSet(keyId){ 
	window.open("OAWorkFlowTempAction.do?queryType=design&ip=$host.replace('|',':')&keyId="+keyId+"&tableName=$!tableName");
}

//表单版本
function openTableView(keyId){
	var url="/OAWorkFlowTableAction.do?tableName=$!tableName&keyId="+keyId+"&src=menu&operation=$globals.getOP("OP_ADD_PREPARE")&view=true";
	window.open(url);
}

</script>
</head>
<body style="margin-left: 3px;overflow: auto;overflow-x:hidden;">
<input type="hidden" value="$!tableName" id="tableName" name="tableName"/>
	<!--  
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">
		流程版本
	</div>
	<ul class="HeadingButton">
		<li><button type="button" onclick="window.close();" class="b2">关闭</button></li>
	</ul>
</div>
	-->
<div id="listRange_id" style="margin-top: 5px;">
	<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
	<thead>
		<tr>
			<td width="50" align="center">版本号</td>
			<td width="100" align="center">修改人</td>
			<td width="130" align="center">修改时间</td>
			<td width="100" align="center">操作</td>
		</tr>
	</thead>
	<tbody>
	#if("$!versionType"=="flow")
		#foreach($ver in $versions)
		<tr>
			<td>$!globals.get($ver,0)&nbsp;</td>
			<td>$!globals.get($ver,2)&nbsp;</td>
			<td>$!globals.get($ver,1)&nbsp;</td>
			<td><a style="cursor: pointer;" href="javascript:openFlowSet('$!globals.get($ver,3)');">查看</a>&nbsp;
				<!--<a style="cursor: pointer;" onclick="">导出</a>&nbsp;-->
			</td>
		</tr>
		#end		
	#else
		#set($num=$versions.size())
		#foreach($ver in $versions)
		<tr>
			<td>$num&nbsp;</td>
			<td>$!globals.get($ver,1)&nbsp;</td>
			<td>$!globals.get($ver,0)&nbsp;</td>
			<td><a style="cursor: pointer;" href="javascript:openTableView('$!globals.get($ver,2)');">查看</a>&nbsp;
				<a style="cursor: pointer;" href="/UtilServlet?operation=exportFlow&view=true&keyId=$globals.get($ver,2)&tableName=$!tableName" target="_blank">导出</a>&nbsp;
			</td>
		</tr>
		#set($num = $num - 1)
		#end	
	#end	
	</tbody>
	</table>
</div>
</body>
</html>
