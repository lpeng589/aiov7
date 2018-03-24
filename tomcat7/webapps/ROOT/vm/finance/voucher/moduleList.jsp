<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>AIO</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link rel="stylesheet" href="/style1/css/workflow2.css" />
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript"> 
	//添加
	
	function addModule(){
		var value = jQuery("#searchValue").val();
		if(value==""){
			alert("请输入模板名称！");
			jQuery("#searchValue").focus();
			return false;
		}
		parent.document.getElementById('ModuleName').value = value;
		parent.checkForm('addModule');
	}
	
	//刷新列表
	function refure(){
		window.location.reload();
	}
	
	//删除模板
	function delModule(id){
		if(!confirm("你确定删除吗？"))return
		jQuery.ajax({ type: "POST", url: "/VoucherManage.do?operation=3&optype=deleteModule&tableName=tblAccMainTemplete&keyId="+id,success: function(msg){
		     if(msg=="OK"){
		     	jQuery("#"+id).remove();
		     }else{
		     	//删除失败
		     	alert("删除失败！");
		     }
		   }
		});
	}
	
	function readModule(id){
		parent.choosemodule(id);
	}
</script>
<style type="text/css">
	#searchDiv ul li{
		height: 30px;
		float: left;
	}
	#searchDiv{
		padding: 5px 0px 0px 10px;
		height: 35px;
	}
	input{
		padding-top: 4px;
	}
</style>
</head>
<body>
<form action="/VoucherManage.do" method="post" id="form" name="form">
<input type="hidden" id="operation" name="operation" value="$globals.getOP("OP_QUERY")"/>
<input type="hidden" id="optype" name="optype" value="voucherModule"/>
<div class="c_main  f_l" style="margin-left: 0;width:525px;height:275px;">
<div class="maintablearea">
<div class="hd">
<div id="searchDiv"><ul><li><input name="searchValue" id="searchValue" style="width: 300px;" value="$!searchValue"/></li>
<li><img src="/style1/images/Add_button.gif" alt="添加" style="padding-left: 10px;" onclick="addModule()" /></li></ul></div>
<div class="bd">
<ul class="maintop" style="width: 100%;">
<li class="col_hy" style="background: none;width: 70%;text-align: left;margin-left: 25px;">名称</li>
<li class="col_hy" style="width: 15%;">操作</li>
</ul>
#foreach($!module in $!moduleList)
<ul class="col" style="width: 100%;float: left;clear: none;" id="$!globals.get($!module,0)" ondblclick="readModule('$!globals.get($!module,0)')">
<li class="col_hy" style="background: none;width: 70%;text-align: left;margin-left: 20px;" title="$!globals.get($!module,1)">$!globals.get($!module,1)</li>
<li class="col_hy" style="width: 15%;text-align: left;">
	<a href="javascript:void(0);" onclick="delModule('$!globals.get($!module,0)')" style="margin-left: 10px;" ><span class="NI_3" >删除</span></a>
	</li>
</ul>
#end
</div>
</div></div>
</div>
</form>
</body>
</html>

