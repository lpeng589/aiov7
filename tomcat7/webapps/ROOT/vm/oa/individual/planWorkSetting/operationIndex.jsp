<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>

<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/jquery.cookie.js" ></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script>
function goAction(goType,action){
	var	url=action;
	goFarme(url);
}
function goFarme(url){	
	window.parent.settingList.location=url;	
}
</script>

</head>
<body>
	<div style="margin-left:5px;" align="center" >
	 #if("$!planSetting" == "true")
		<div style="margin-top:15px; width:87px;" onmouseover="moveImg(this)" onmouseout="overImg(this)" onclick="goAction('planMould','/UserFunctionQueryAction.do?tableName=tblPlanTemplate&src=menu&winCurIndex=$!winCurIndex');">
			<img src="/icon/fin11.png"/>
			<div style="margin:10px auto;">计划模板</div>
		</div>
	 #end
	 #if("$!assSetting" == "true")
		<div style="margin-top:15px; width:87px;" onmouseover="moveImg(this)" onmouseout="overImg(this)" onclick="goAction('planAssociate','/UserFunctionQueryAction.do?tableName=tblPlanAssociate&src=menu&winCurIndex=$!winCurIndex');">
			<div><img src="/icon/publicClient.png"/></div>
			<div style="margin:10px auto;">计划关联项</div>
		</div>
	#end
	 #if("$!paramSetting" == "true")
		<div style="margin-top:15px; width:87px;" onmouseover="moveImg(this)" onmouseout="overImg(this)" onclick="goAction('paramSetting','/OAWorkPlanAction.do?operation=4&opType=param');">
			<div><img src="/icon/pss_process.png"/></div>
			<div style="margin:10px auto;">检视参数设置</div>
		</div>
	 #end
	  #if("$!seeSetting" == "true")
		<div style="margin-top:15px; width:87px; " onmouseover="moveImg(this)" onmouseout="overImg(this)" onclick="goAction('seeSetting','/OAWorkPopedomeActon.do');">
			<div><img src="/icon/crm_purpose.png"/></div>
			<div style="margin:10px auto;">查看权限设置</div>
		</div>
	#end
	</div>
</body>
<script type="text/javascript">
	function moveImg(img){
		img.style.cursor="pointer";
		img.style.background="#04B0FB";
		//img.style.opacity="0.7";
		//img.style.filter="alpha(opacity=70)"
	}
	function overImg(img){
		img.style.background="none";
	}
	
</script>
</html>