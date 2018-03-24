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
function addLabel(){
		var urls = '/CRMClientAction.do?operation=6&type=label&moduleId=$!moduleBean.id' ;
		asyncbox.open({
			id:'addLabelId',title :'新增标签',url:urls,cache:false,modal:true,width:440,height:223,btnsbar:jQuery.btn.OKCANCEL,
			callback : function(action,opener){
				if(action == 'ok'){
					opener.beforeSubmit()
					return false;
				}
			}
		});
}

function update(labelId,obj){
	var urls = '/CRMClientAction.do?operation=7&moduleId=$!moduleBean.id&type=label&labelId='+labelId ;
	asyncbox.open({
		id:'addLabelId',title :'修改标签',url:urls,cache:false,modal:true,width:440,height:223,btnsbar:jQuery.btn.OKCANCEL,
		callback : function(action,opener){
			if(action == 'ok'){
				opener.beforeSubmit()
				return false;
			}
		}
	});
}

function del(labelId){
	if(confirm('$text.get("oa.common.sureDelete")')){
		//parent.form.labelQueryIds.value = parent.form.labelQueryIds.value.replace(labelId+",",""); 
		window.location.href="/CRMClientAction.do?operation=3&moduleId=$!moduleBean.id&type=delLabel&isDelBean=true&labelId="+labelId
	}
	
}

function dealAsyn(){
	 window.location.href="/CRMClientAction.do?operation=4&type=label&moduleId="+jQuery("#moduleId").val();
}


function labelUse(moduleId,useFlag){
	jQuery.ajax({
	   type: "POST",
	   url: "/CRMClientAction.do?operation=2&type=labelUse&moduleId="+moduleId+"&useFlag="+useFlag,
	   success: function(msg){
	   		if(msg = "yes"){
	   			asyncbox.tips('设置成功!','success');
	   			window.location.href = "/CRMClientAction.do?operation=4&type=label&moduleId="+moduleId;
	   		}else{
	   			asyncbox.tips('设置失败','error');
	   		}
	   }
	});
	
	
}
</script>
</head>

<body >
<input type="hidden" id="moduleId" name="moduleId" value="$!moduleBean.id"/>
<div class="c_main  f_l" style="margin-left: 0;width: 505px;height:290px;overflow: auto;">
<div class="maintablearea">
<div class="hd">
<div>
#if($loginBean.operationMap.get("/ClientSettingAction.do").query())
	#if("$!moduleBean.isUserLabel" == "1")
	<input type="button" value="停用标签" onclick="labelUse('$!moduleBean.id','0');" style="float: right;margin-right: 5px;margin-top: -1px;margin-bottom: 2px;width: 70px;"/> 
	#else
	<input type="button" value="启用标签" onclick="labelUse('$!moduleBean.id','1');" style="float: right;margin-right: 5px;margin-top: -1px;margin-bottom: 2px;width: 70px;"/> 
	#end
#end

<input type="button" value="添加" onclick="addLabel();" style="float: right;margin-right: 5px;margin-top: -1px;margin-bottom: 2px;width: 50px;"/> <div>
<div class="bd"  >
<ul class="maintop" style="width: 100%;">
<li class="col_hy" style="background: none;width: 50%;text-align: center;"><span style="float: left;margin-left: 50px;">标签名称</span></li>
<li class="col_hy" style="width: 40%;"><span style="float: left;margin-left: 80px;">操作</span></li>
</ul>
#foreach ($row in $labelList )
<ul class="col" style="width: 100%;float: left;clear: none;" moduleId="$!row.id" moduleName="$!row.moduleName">
<li class="col_hy" style="background: none;width: 50%;text-align: left;margin-left: 40px;" title="$!row.labelName"><span style="width:12px;height:12px;float:left;margin-top:5px;margin-right:5px; background-color:$!row.labelColor "></span>$!row.labelName</li>
<li class="col_hy" style="width: 40%;text-align: left;">
	<a href="javascript:void(0);" onclick="update('$!row.id',this)" style="margin-left: 10px;" ><span class="NI_2" >$text.get("oa.common.upd")</span></a>
	<a href="#" onclick="del('$row.id');" style="margin-left: 10px;"><span class="NI_3">$text.get("oa.common.del")</span></a>	
</li>
</ul>
#end
</div>
</div>

</div>
</body>
</html>
