<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>工作流委托</title>
<link rel="stylesheet" href="/style1/css/workflow.css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript">
function goAction(type){
	$("#pcontent").attr("src","/WorkConsignAction.do?operation=4&type=list&queryType="+type);
}
</script>
	</head>
	<body>
		<table cellpadding="0" cellspacing="0" border="0" class="framework">
			<tr><!--左边菜单开始-->
				<td class="leftMenu">
					<div class="TopTitle"><span><img src="/style1/images/workflow/ti_001.gif" /></span><span>我的工作委托</span></div>
					<div class="LeftBorder">
						<div class="New_search">
							
						</div>
						<ul class="LeftMenu_list">		
							<li onclick="goAction('all');" style="cursor: pointer;"><span class="off">所有委托</span></li>	
						
							<li onclick="goAction('myself');" style="cursor: pointer;"><span class="off">委托给我</span></li>
							<li onclick="goAction('others');" style="cursor: pointer;"><span class="off">委托他人</span></li>
							<!-- 
							<li  style="cursor:pointer;"><span style="font-weight:bold; margin-left:10px; width:auto; padding-left:6px; float:left;">时间检索</span>
								<ul >
									<li onclick="goAction('day');" style="cursor: pointer;margin-top: 10px;">今天以内</li>
									<li onclick="goAction('wek');" style="cursor: pointer;">本周以内</li>
									<li onclick="goAction('month');" style="cursor: pointer;">本月以内</li>
									<li onclick="goAction('more');" style="cursor: pointer;">三月以上</li>
								</ul>
							</li>
							 -->
							<li></li>
						
						</ul>
					</div>				
				</td><!--左边菜单结束-->
				<td>
				<iframe src="/WorkConsignAction.do?operation=4&type=list" scrolling="no" noresize="noresize" id="pcontent" name="pcontent" style="width:100%;border:none;" frameborder="0" onload="this.height=pcontent.document.body.scrollHeight;"></iframe>
				</td>
			</tr>
		</table>
	</body>
</html>
