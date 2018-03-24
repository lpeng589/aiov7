<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>模板列表</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script language="javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<style type="text/css">
.maintablearea .bd li.col_hy{
	width: auto;
}
</style>
<script type="text/javascript">

function addModule(){
		var urls = "/StockGoodsAction.do?operation=6"
		asyncbox.open({
			id:'dealModule',title :'新增模板',url:urls,cache:false,modal:true,width:700,height:400
	　	});
}


/* 修改 */
function updateModule(id){
	var urls = "/StockGoodsAction.do?operation=7&id="+id
	asyncbox.open({
		id:'dealModule',title :'修改模板',url:urls,cache:false,modal:true,width:700,height:400,
		callback : function(action,opener){
			if(action == 'close'){
				parent.jQuery.close('dealModule');
			}
		}
    });
}

/* 删除 */
function deleteModule(id){
	asyncbox.confirm('你确定删除吗？','提示',function(action){
	　　　if(action == 'ok'){
	　　　　　	jQuery.ajax({type: "POST", url: "/StockGoodsAction.do?operation=3&id="+id, success: function(result){
				if(result == "ok"){
					//删除成功
					asyncbox.tips('删除成功','success');
				}else{
					asyncbox.tips('删除失败','success');
				}
				window.location="/StockGoodsAction.do?optype=queryModule";
			}});
	　　　}
　	});
}

/* 刷新 */
function refurbish(){
	window.location.reload();
}

</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form method="post" action="/StockGoodsAction.do?optype=queryModule" scope="request" name="form" id="form" target="">
<div class="c_main  f_l" style="margin-left: 0;width: 505px;height:290px;overflow: auto;">
<div class="maintablearea">
<div class="hd">
<div><input type="button" value="添加" onclick="addModule();" style="float: right;margin-right: 5px;margin-top: -1px;margin-bottom: 2px;width: 50px;"/><div>
<div class="bd"  >
<ul class="maintop" style="width: 100%;">
<li class="col_hy" style="background: none;width: 34%;text-align: left;margin-left: 25px;">模板名称</li>
<li class="col_hy" style="width:45%;margin: 0">描述</li>
<li class="col_hy" style="text-align: center;">操作</li>
</ul>
#foreach($modules in $list)
<ul class="col" style="width: 100%;float: left;clear: none;">
<li class="col_hy" style="background: none;width: 35%;text-align: left;margin-left: 20px;" title="$!modules.moduleName">$!globals.subTitle($!modules.moduleName,30)</li>
<li class="col_hy"  style="width: 45%;text-align: left;" title="$!modules.moduleDesc">$!globals.subTitle($!modules.moduleDesc,40)</li>
<li class="col_hy" style="text-align: center;">
	<a href="javascript:void(0);" onclick="updateModule('$!modules.id')" style="margin-left: 10px;" ><span class="NI_2" >修改</span></a>
	#if("$modules.id"!="0")
	<a href="javascript:void(0);" onclick="deleteModule('$!modules.id')" ><span class="NI_2" >删除</span></a>
	#end
</li>
</ul>
#end
</div>
</div>

</div>
</body>
</html>
