<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>
<link rel="stylesheet" href="/style1/css/workflow.css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/jquery.cookie.js" ></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script type="text/javascript">

function goAction(goType,action){
	var url = "";
	var moduleId = $("#moduleId").val();
	if(goType == "crmModule"){
		url = "/ClientSettingAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId="+moduleId+"&moduleId="+moduleId;
	}else if(goType == "moduleView" || goType == "childDisplay"){
		url = "/ClientSettingAction.do?updPreType="+goType+"&operation=$globals.getOP("OP_UPDATE_PREPARE")&viewId=$viewId&moduleId=$!moduleId";
	}else{
		url = "/"+action+".do?operation=4&moduleId=$moduleId&viewId=$viewId&queryType="+goType;
	}
	goFarme(url);
}

function goFarme(url){	
	window.parent.f_mainFrame.location=url;	
}

function comeAction(){
	var selectType="$!selectType";
	if(selectType!=null){
		goAction(selectType,'ClientSettingAction');
	}
}
document.onkeydown = keyDown; 
</script>
</head>
<body >
<input type="hidden" name="moduleId" id="moduleId" value="$!moduleId"/>

	<table cellpadding="10px;" cellspacing="0" border="0" class="frame" style="margin-top:4px;">
		<tr>
			<td class="leftMenu">
				<div class="TopTitle"><span><img src="/style1/images/workflow/ti_001.gif" /></span><span>#if("$!viewEnter" != "true")模板设置#else视图设置#end</span></div>
				<div class="LeftBorder">
				<div id="cc" style=" width:100%; overflow:hidden;overflow-y: auto;">
<script type="text/javascript">
	var oDiv=document.getElementById("cc");
	var sHeight=document.documentElement.clientHeight-100;
	oDiv.style.height=sHeight+"px";
</script>
					<ul class="LeftMenu_list">		
						#if("$!leftFlag" == "")
						<li onclick="goAction('fieldsMaintain','ClientSettingAction');" style="cursor: pointer;"><img  style="margin-left: 10px;margin-right:5px;" src="/style1/images/workflow/2.png" /><b>模板字段维护</b></li>
						<li onclick="goAction('crmModule','ClientSettingAction');" style="cursor: pointer;border-top: 0px;"><img  style="margin-left: 10px;margin-right:5px;" src="/style1/images/workflow/2.png" /><b>模板显示设置</b></li>		 
						<li onclick="goAction('clientTransfer','ClientSettingAction');" style="cursor: pointer;border-top: 0px;"><img  style="margin-left: 10px;margin-right:5px;" src="/style1/images/workflow/2.png" /><b>ERP字段映射设置</b></li>		 
						#else
						<li onclick="goAction('moduleView','ClientSettingAction');" style="cursor: pointer;border-top: 0px;"><img  style="margin-left: 10px;margin-right:5px;" src="/style1/images/workflow/2.png" /><b>视图显示设置</b></li>	
						<li onclick="goAction('fieldScopeSet','FieldScopeSetAction');" style="cursor: pointer;"><img  style="margin-left: 10px;margin-right:5px;" src="/style1/images/workflow/2.png" /><b>字段权限设置</b></li>									
						<li onclick="goAction('childDisplay','ClientSettingAction');" style="cursor: pointer;"><img  style="margin-left: 10px;margin-right:5px;" src="/style1/images/workflow/2.png" /><b>明细显示设置</b></li>
						<!-- <li onclick="goAction('crmEnumeration','ClientSettingAction');" style="cursor: pointer;"><img  style="margin-left: 10px;margin-right:5px;" src="/style1/images/workflow/2.png" /><b>选项数据管理</b></li>		 -->
						<li onclick="goAction('crmScope','ClientSettingAction');" style="cursor: pointer;"><img  style="margin-left: 10px;margin-right:5px;" src="/style1/images/workflow/2.png" /><b>导入导出设置</b></li>	
						#end
					</ul>
				</div>	
				</div>			
			</td>
		</tr>
	</table>
</body>
</html>
