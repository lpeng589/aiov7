<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>AIO</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link rel="stylesheet" href="/style1/css/workflow2.css" />
<script language="javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
$(function() {
	jQuery(".bd ul").click(function(){
		if($(this).prev().attr("checked") == "checked"){
			$(this).prev().removeProp("checked");		
		}else{
			$(this).prev().attr("checked","checked");			
		}
	});

	parent.openModuleView($("#firstEnter").val(),$(".col:last").attr("viewId"),$(".col:last").attr("viewName"),$(".col:last").attr("moduleId"));
});

function addModuleView(){
		var urls = "/vm/crm/clientManager/addModuleView.jsp?moduleId=$moduleId"
		asyncbox.open({
			id:'addModuleViewId',title :'新增模板视图',url:urls,cache:false,modal:true,width:400,height:160, btnsbar:jQuery.btn.OKCANCEL,
			callback : function(action,opener){
	　　　　　		if(action == 'ok'){
					opener.beforeSubmit()
					return false;
				}
		  　　}
	　	});
}

function update(viewId,obj){
	var viewName = $(obj).parent().prev().prev().attr("title");
	var screenWidth = 1020;
	if(window.screen.width > 1200){
		screenWidth = 1200;
	}
	var urls = "/ClientSettingAction.do?queryType=moduleView&moduleId=$moduleId&viewId="+viewId
	asyncbox.open({
		id:'addViewId',title :'修改'+viewName+'视图',url:urls,cache:false,modal:true,width:screenWidth,height:500,
		callback : function(action,opener){
			if(action == 'close'){
				parent.jQuery.close('moduleViewId');
			}
		}
    });
}

function isDelete(viewId,moduleCount,obj){
	/*
	var moduleName = $(obj).parent().prev().prev().attr("title")
	if(moduleCount !=""){
		alert(moduleName+"视图存在" + moduleCount +"个客户,不允许此操作");
		return false;
	}*/
	if(confirm('$text.get("oa.common.sureDelete")')){
		document.location = "/ClientSettingAction.do?delType=moduleView&moduleId=$moduleId&operation=$globals.getOP("OP_DELETE")&viewId="+viewId;		
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
	 window.location.href="/ClientSettingAction.do?operation=4&queryType=moduleView&firstEnter=false&moduleId=$moduleId";
}

</script>
</head>

<body >
<input type="hidden" id="firstEnter" name="firstEnter" value="$!firstEnter"/>
<div class="c_main  f_l" style="margin-left: 0;width: 505px;height:290px;overflow: auto;">
<div class="maintablearea">
<div class="hd">
<!-- <div><input type="button" value="删除" onclick="BatchDelete();" style="float: right;margin-right: 5px;margin-top: -1px;margin-bottom: 2px;"/> <div> -->
<div><input type="button" value="添加" onclick="addModuleView();" style="float: right;margin-right: 5px;margin-top: -1px;margin-bottom: 2px;width: 50px;"/> <div>
<div class="bd"  >
<ul class="maintop" style="width: 100%;">
<li class="col_hy" style="background: none;width: 24%;text-align: left;margin-left: 25px;">视图名称</li>
<li class="col_hy" style="width:45%;margin: 0">描述</li>
<li class="col_hy" style="width: 22%;">操作</li>
</ul>
#foreach ($row in $moduleViewList )
<!-- #if($!row.id!='1')<input type="checkbox" name="keyId" value="$row.id" style="float: left;height:22px;margin-right: 2px; "/>#else <span style="float: left;height:22px;margin-right: 2px;width: 17px;"></span>  #end -->
<ul class="col" style="width: 100%;float: left;clear: none;" viewId="$!row.id" viewName="$!row.viewName" moduleId="$!moduleId">
<li class="col_hy" style="background: none;width: 25%;text-align: left;margin-left: 20px;" title="$!row.viewName">$velocityCount. $!row.viewName</li>
<li class="col_hy"  style="width: 45%;text-align: left;">$!row.viewDesc</li>
<li class="col_hy" style="width: 22%;text-align: left;">
	<a href="javascript:void(0);" onclick="update('$!row.id',this)" style="margin-left: 10px;" ><span class="NI_2" >$text.get("oa.common.upd")</span></a>
	#if($!row.id!="1_" + $moduleId)
	<a href="#" onclick="return isDelete('$row.id','$!moduleViewCountMap.get("$row.id")',this);" style="margin-left: 10px;"><span class="NI_3" >$text.get("oa.common.del")</span></a>	
	#end
</li>
</ul>
#end
</div>
</div>

</div>
</body>
</html>
