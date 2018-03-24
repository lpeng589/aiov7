<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<title>demo</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="/style/css/brotherSetting.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script language="javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<style type="text/css">
body{width:820px;}
</style>
	
<script type="text/javascript">
$(function(){
	#if("$!operationSuccess" == "true")
		asyncbox.tips('添加成功!','success');
	#end
})

/*全选*/
function selectAll(obj){
	if($(obj).attr("checked") == "checked"){
		$(":checkbox").attr("checked","checked");
	}else{
		$(":checkbox").removeAttr("checked");
	}
}

/*添加*/
function add(){
	window.location.href = "/CRMBrotherSettingAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&tableName=$tableName";		
}

/*修改*/
function update(keyId){
	window.location.href = "/CRMBrotherSettingAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&type=fieldScope&tableName=$tableName&keyId="+keyId;		
}

/*删除方法*/
function del(keyId){
	ajaxDel(keyId)
}

/*批量删除*/
function barchDel(){
	var keyIds="";
	$(":checkbox[id!='all']:checked").each(function(){
		keyIds += $(this).attr("id") +",";
	})
	if(keyIds==""){
		alert("请选择删除项。");
		return false;
	}
	ajaxDel(keyIds);
}
/*异步删除调用方法*/
function ajaxDel(keyIds){
	jQuery.ajax({
	   type: "POST",
	   url: "/CRMBrotherSettingAction.do",
	   data: 'operation=$globals.getOP("OP_DELETE")&tableName=$tableName&ids='+keyIds,
	   success: function(msg){
	   		if(msg=="success"){
	   			asyncbox.tips('删除成功','success');
	   			setTimeout(function(){window.location.reload();},1500);//成功两秒后刷新

	   		}else{
	   			alert("删除失败");
	   		}
	   }
	});
}
</script>
</head>
<body>
	<div style="overflow:hidden;padding:5px 0 0 10px;">
		<div style="float:left;display:inline-block;line-height:26px;">当前位置：字段权限设置</div>
		<div class="btn btn-small" onclick="barchDel()" style="float:right;margin:0 0 0 5px;">删除</div>
		<div class="btn btn-small" onclick="add()" style="float:right;">添加</div>
	</div>
	<div class="wp_auto">
		<!-----------分割线 wp_blt板块 Start------------->
		<div class="wp_blt_power">
			<div class="blt_head">
				<ul class="head_ul">
					<li class="head_li hd_2">
						<i class="char">
							序号
						</i>
					</li>
					<li class="head_li hd_1">
						<input class="cbox" type="checkbox" id="all" onclick="selectAll(this)"/>
					</li>
					<li class="head_li hd_4">
						<i class="char">
							字段名称
						</i>
					</li>
					<li class="head_li hd_3">
						<i class="char">
							创建人


						</i>
					</li>
					<li class="head_li hd_5">
						<i class="char">
							创建时间
						</i>
					</li>
					<li class="head_li hd_3">
						<i class="char">
							操作
						</i>
					</li>
				</ul>
			</div>
			<div class="blt_main">
				#foreach($fieldScopeBean in $list)
				<ul class="c_ul">
					<li class="c_li hd_2">
						<i class="char">
							$velocityCount
						</i>
					</li>
					<li class="c_li hd_1">
						<input class="cbox" type="checkbox" id="$fieldScopeBean.id"/>
					</li>
					<li class="c_li hd_4">
						<i class="char">
							 #foreach($fieldName in $fieldScopeBean.fieldsName.split(","))	
						   		#if($fieldName.indexOf("child")==-1)
						   			#set($fieldBean=$globals.getFieldBean("$!tableName",$fieldName))
						   			$fieldBean.display.get("$globals.getLocale()"),
								#else
									#set($replaceName=$fieldName.replaceAll('child',''))	
				    				#set($fieldBean=$globals.getFieldBean($!childTableName,$replaceName))
									明细$fieldBean.display.get("$globals.getLocale()"),
								#end
						    #end
							
						</i>
					</li>
					<li class="c_li hd_3">
						<i class="char">
							$!globals.getEmpFullNameByUserId($fieldScopeBean.createBy)
						</i>
					</li>
					<li class="c_li hd_5">
						<i class="char">
							$fieldScopeBean.createTime
						</i>
					</li>
					<li class="c_li hd_3">
						<i class="char update" onclick="update('$fieldScopeBean.id')">
							修改
						</i>
						<i class="char del" onclick="del('$fieldScopeBean.id')">
							删除
						</i>
					</li>				
				</ul>
				#end
			</div>
		</div>
		<!-----------分割线 wp_blt板块 End------------->
		
	</div>
</body>
</html>
