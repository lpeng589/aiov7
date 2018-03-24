<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>AIO</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style1/css/workflow2.css" />
<style type="text/css">
.NI_2,.NI_3,.NI_4{background:none;padding:0;display:inline-block;}
li.col_hy .wp_a{display:inline-block;width:40px;overflow:hidden;color:#0179BB;float:left;}
li.col_hy .wp_a:hover{color:red;}
li.col_hy .icon{background:url(/style/images/client/glyphicons-halflings_h.png) no-repeat;width:14px;height:14px;display:inline-block;margin:3px 0 0 0;float:left;}
li.col_hy .txt{float:left;display:inline-block;margin:0 0 0 1px;}
li.col_hy .NI_2 .icon{background-position:-96px -72px;}
li.col_hy .NI_3 .icon{background-position:-168px -96px;}
li.col_hy .NI_4 .icon{background-position:-25px -48px;}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
$(function() {
	if(typeof(top.junblockUI)!="undefined"){
		top.junblockUI();
	}
	jQuery(".bd ul").click(function(){
		if($(this).prev().attr("checked") == "checked"){
			$(this).prev().removeProp("checked");		
		}else{
			$(this).prev().attr("checked","checked");			
		}
	});
	parent.openModule($("#firstEnter").val(),$(".col:last").attr("moduleId"),$(".col:last").attr("moduleName"));
});

function addModule(){
		var urls = "/vm/crm/clientManager/addModule.jsp"
		asyncbox.open({
			id:'addModuleId',title :'新增客户模板',url:urls,cache:false,modal:true,width:400,height:160, btnsbar:jQuery.btn.OKCANCEL,
			callback : function(action,opener){
	　　　　　		if(action == 'ok'){
					opener.beforeSubmit()
					return false;
				}
		  　　}
	　	});
}

function update(moduleId,obj){
	var moduleName = $(obj).parent().prev().prev().attr("title");
	var screenWidth = 1020;
	if(window.screen.width > 1200){
		screenWidth = 1200;
	}
	var urls = "/ClientSettingAction.do?moduleId="+moduleId
		asyncbox.open({
			id:'addModuleId',title :'修改'+moduleName+'模板',url:urls,cache:false,modal:true,width:screenWidth,height:500,
			callback : function(action,opener){
				if(action == 'close'){
					parent.jQuery.close('modulediv');
				}
			
			}
	　});
}

function isDelete(moduleId,moduleCount,obj){
	var moduleName = $(obj).parent().prev().prev().attr("title")
	if(moduleCount !=""){
		alert(moduleName+"模板存在" + moduleCount +"个客户,不允许此操作");
		return false;
	}
	if(confirm('$text.get("oa.common.sureDelete")')){
		if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/style/images/load.gif'/>",css:{ background: 'transparent'}}); 
		}
		document.location = "/ClientSettingAction.do?operation=$globals.getOP("OP_DELETE")&keyId="+moduleId;		
	}	
	
}

function BatchDelete(){
	var selectIds="";
	if(jQuery("input[name='keyId']:checked").size() == 1 && jQuery("input[name='keyId']:checked").val()=="1"){
		alert("系统默认模版不可删除，请重新选择!");
		return false;
	}
	jQuery("input[name='keyId']").each(function(){
	   if(jQuery(this).attr("checked")=="checked" && $(this).val()!="1"){
	  		selectIds+=this.value+";";
	   }
	});
	if(selectIds==""){
		alert("$text.get('common.msg.mustSelectOne')");
		return false;
	}else{
		if(confirm("$text.get('oa.common.sureDelete')")){
			window.location.href="/ClientSettingAction.do?operation=3&keyId="+selectIds;
		}
	}
}

function dealAsyn(){
	window.location.href="/ClientSettingAction.do?operation=4&firstEnter=false";
}

function copy(moduleId){
	jQuery("#moduleId").val(moduleId);
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/style/images/load.gif'/>",css:{ background: 'transparent'}}); 
	}
	form.submit();
}

</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form name="form" action="/ClientSettingAction.do" method="post" target="formFrame" >
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_ADD')"/>
<input type="hidden" id="firstEnter" name="firstEnter" value="$!firstEnter"/>
<input type="hidden" id="moduleId" name="moduleId" value=""/>
<div class="c_main  f_l" style="margin:0;width:505px;height:290px;overflow:auto;">
	<div class="maintablearea">
		<div class="hd">
		<!-- <div><input type="button" value="删除" onclick="BatchDelete();" style="float: right;margin-right: 5px;margin-top: -1px;margin-bottom: 2px;"/> <div> -->
			<input type="button" value="添加" onclick="addModule();" style="float: right;margin-right: 5px;margin-top: -1px;margin-bottom: 2px;width: 50px;"/> 
		</div>
		<div class="bd"  >
			<ul class="maintop" style="width: 100%;">
				<li class="col_hy" style="background: none;width: 24%;text-align: left;margin-left: 25px;">模板名称</li>
				<li class="col_hy" style="width:45%;margin: 0">描述</li>
				<li class="col_hy" style="width: 22%;">操作</li>
			</ul>
			#foreach ($row in $moduleList )
				#if("$!row.id" !="weixinModuleId")
					<!-- #if($!row.id!='1')<input type="checkbox" name="keyId" value="$row.id" style="float: left;height:22px;margin-right: 2px; "/>#else <span style="float: left;height:22px;margin-right: 2px;width: 17px;"></span>  #end -->
					<ul class="col" style="width: 100%;float: left;clear: none;" moduleId="$!row.id" moduleName="$!row.moduleName">
						<li class="col_hy" style="background: none;width: 25%;text-align: left;margin-left: 20px;" title="$!row.moduleName">$velocityCount. $!row.moduleName</li>
						<li class="col_hy"  style="width: 40%;text-align: left;">$!row.moduleDesc</li>
						<li class="col_hy" style="width: 30%;text-align: left;">
							<a class="wp_a" href="javascript:void(0);" onclick="update('$!row.id',this)"><span class="NI_2" ><em class="icon"></em><em class="txt">$text.get("oa.common.upd")</em></span></a>
							#if($!row.id!='1')
							<a class="wp_a" href="#" onclick="return isDelete('$row.id','$!moduleCountMap.get("$row.id")',this);"><span class="NI_3"><em class="icon"></em><em class="txt">$text.get("oa.common.del")</em></span></a>	
							#end
							<a class="wp_a" href="javascript:void(0);" onclick="copy('$row.id');"><span class="NI_4" ><em class="icon"></em><em class="txt">复制</em></span></a> 
						</li>
					</ul>
				#end
			#end
		</div>
	</div>
</div>
</form>
</body>
</html>
